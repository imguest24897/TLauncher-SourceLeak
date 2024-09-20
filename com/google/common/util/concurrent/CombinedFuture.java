/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableCollection;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ final class CombinedFuture<V>
/*     */   extends AggregateFuture<Object, V>
/*     */ {
/*     */   private CombinedFutureInterruptibleTask<?> task;
/*     */   
/*     */   CombinedFuture(ImmutableCollection<? extends ListenableFuture<?>> futures, boolean allMustSucceed, Executor listenerExecutor, AsyncCallable<V> callable) {
/*  40 */     super(futures, allMustSucceed, false);
/*  41 */     this.task = new AsyncCallableInterruptibleTask(callable, listenerExecutor);
/*  42 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CombinedFuture(ImmutableCollection<? extends ListenableFuture<?>> futures, boolean allMustSucceed, Executor listenerExecutor, Callable<V> callable) {
/*  50 */     super(futures, allMustSucceed, false);
/*  51 */     this.task = new CallableInterruptibleTask(callable, listenerExecutor);
/*  52 */     init();
/*     */   }
/*     */ 
/*     */   
/*     */   void collectOneValue(int index, Object returnValue) {}
/*     */ 
/*     */   
/*     */   void handleAllCompleted() {
/*  60 */     CombinedFutureInterruptibleTask<?> localTask = this.task;
/*  61 */     if (localTask != null) {
/*  62 */       localTask.execute();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void releaseResources(AggregateFuture.ReleaseResourcesReason reason) {
/*  68 */     super.releaseResources(reason);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  76 */     if (reason == AggregateFuture.ReleaseResourcesReason.OUTPUT_FUTURE_DONE) {
/*  77 */       this.task = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void interruptTask() {
/*  83 */     CombinedFutureInterruptibleTask<?> localTask = this.task;
/*  84 */     if (localTask != null)
/*  85 */       localTask.interruptTask(); 
/*     */   }
/*     */   
/*     */   private abstract class CombinedFutureInterruptibleTask<T>
/*     */     extends InterruptibleTask<T>
/*     */   {
/*     */     private final Executor listenerExecutor;
/*     */     
/*     */     CombinedFutureInterruptibleTask(Executor listenerExecutor) {
/*  94 */       this.listenerExecutor = (Executor)Preconditions.checkNotNull(listenerExecutor);
/*     */     }
/*     */ 
/*     */     
/*     */     final boolean isDone() {
/*  99 */       return CombinedFuture.this.isDone();
/*     */     }
/*     */     
/*     */     final void execute() {
/*     */       try {
/* 104 */         this.listenerExecutor.execute(this);
/* 105 */       } catch (RejectedExecutionException e) {
/* 106 */         CombinedFuture.this.setException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final void afterRanInterruptibly(T result, Throwable error) {
/* 123 */       CombinedFuture.this.task = null;
/*     */       
/* 125 */       if (error != null) {
/* 126 */         if (error instanceof java.util.concurrent.ExecutionException) {
/* 127 */           CombinedFuture.this.setException(error.getCause());
/* 128 */         } else if (error instanceof java.util.concurrent.CancellationException) {
/* 129 */           CombinedFuture.this.cancel(false);
/*     */         } else {
/* 131 */           CombinedFuture.this.setException(error);
/*     */         } 
/*     */       } else {
/* 134 */         setValue(result);
/*     */       } 
/*     */     }
/*     */     
/*     */     abstract void setValue(T param1T);
/*     */   }
/*     */   
/*     */   private final class AsyncCallableInterruptibleTask
/*     */     extends CombinedFutureInterruptibleTask<ListenableFuture<V>>
/*     */   {
/*     */     private final AsyncCallable<V> callable;
/*     */     
/*     */     AsyncCallableInterruptibleTask(AsyncCallable<V> callable, Executor listenerExecutor) {
/* 147 */       super(listenerExecutor);
/* 148 */       this.callable = (AsyncCallable<V>)Preconditions.checkNotNull(callable);
/*     */     }
/*     */ 
/*     */     
/*     */     ListenableFuture<V> runInterruptibly() throws Exception {
/* 153 */       ListenableFuture<V> result = this.callable.call();
/* 154 */       return (ListenableFuture<V>)Preconditions.checkNotNull(result, "AsyncCallable.call returned null instead of a Future. Did you mean to return immediateFuture(null)? %s", this.callable);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setValue(ListenableFuture<V> value) {
/* 163 */       CombinedFuture.this.setFuture(value);
/*     */     }
/*     */ 
/*     */     
/*     */     String toPendingString() {
/* 168 */       return this.callable.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private final class CallableInterruptibleTask
/*     */     extends CombinedFutureInterruptibleTask<V> {
/*     */     private final Callable<V> callable;
/*     */     
/*     */     CallableInterruptibleTask(Callable<V> callable, Executor listenerExecutor) {
/* 177 */       super(listenerExecutor);
/* 178 */       this.callable = (Callable<V>)Preconditions.checkNotNull(callable);
/*     */     }
/*     */ 
/*     */     
/*     */     V runInterruptibly() throws Exception {
/* 183 */       return this.callable.call();
/*     */     }
/*     */ 
/*     */     
/*     */     void setValue(V value) {
/* 188 */       CombinedFuture.this.set(value);
/*     */     }
/*     */ 
/*     */     
/*     */     String toPendingString() {
/* 193 */       return this.callable.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\commo\\util\concurrent\CombinedFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */