/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.concurrent.ExecutionException;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ class ImmediateFuture<V>
/*    */   implements ListenableFuture<V>
/*    */ {
/* 32 */   static final ListenableFuture<?> NULL = new ImmediateFuture(null);
/*    */   
/* 34 */   private static final Logger log = Logger.getLogger(ImmediateFuture.class.getName());
/*    */   
/*    */   private final V value;
/*    */   
/*    */   ImmediateFuture(V value) {
/* 39 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addListener(Runnable listener, Executor executor) {
/* 44 */     Preconditions.checkNotNull(listener, "Runnable was null.");
/* 45 */     Preconditions.checkNotNull(executor, "Executor was null.");
/*    */     try {
/* 47 */       executor.execute(listener);
/* 48 */     } catch (RuntimeException e) {
/*    */ 
/*    */       
/* 51 */       String str1 = String.valueOf(listener), str2 = String.valueOf(executor); log.log(Level.SEVERE, (new StringBuilder(57 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("RuntimeException while executing runnable ").append(str1).append(" with executor ").append(str2).toString(), e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean cancel(boolean mayInterruptIfRunning) {
/* 60 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public V get() {
/* 66 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public V get(long timeout, TimeUnit unit) throws ExecutionException {
/* 71 */     Preconditions.checkNotNull(unit);
/* 72 */     return get();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancelled() {
/* 77 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDone() {
/* 82 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 88 */     String str1 = super.toString(), str2 = String.valueOf(this.value); return (new StringBuilder(27 + String.valueOf(str1).length() + String.valueOf(str2).length())).append(str1).append("[status=SUCCESS, result=[").append(str2).append("]]").toString();
/*    */   }
/*    */   
/*    */   static final class ImmediateFailedFuture<V> extends AbstractFuture.TrustedFuture<V> {
/*    */     ImmediateFailedFuture(Throwable thrown) {
/* 93 */       setException(thrown);
/*    */     }
/*    */   }
/*    */   
/*    */   static final class ImmediateCancelledFuture<V> extends AbstractFuture.TrustedFuture<V> {
/*    */     ImmediateCancelledFuture() {
/* 99 */       cancel(false);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\commo\\util\concurrent\ImmediateFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */