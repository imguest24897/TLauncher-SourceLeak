/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.FutureTask;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ @GwtIncompatible
/*     */ public class ListenableFutureTask<V>
/*     */   extends FutureTask<V>
/*     */   implements ListenableFuture<V>
/*     */ {
/*  50 */   private final ExecutionList executionList = new ExecutionList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <V> ListenableFutureTask<V> create(Callable<V> callable) {
/*  60 */     return new ListenableFutureTask<>(callable);
/*     */   }
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
/*     */   public static <V> ListenableFutureTask<V> create(Runnable runnable, V result) {
/*  74 */     return new ListenableFutureTask<>(runnable, result);
/*     */   }
/*     */   
/*     */   ListenableFutureTask(Callable<V> callable) {
/*  78 */     super(callable);
/*     */   }
/*     */   
/*     */   ListenableFutureTask(Runnable runnable, V result) {
/*  82 */     super(runnable, result);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addListener(Runnable listener, Executor exec) {
/*  87 */     this.executionList.add(listener, exec);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V get(long timeout, TimeUnit unit) throws TimeoutException, InterruptedException, ExecutionException {
/*  95 */     long timeoutNanos = unit.toNanos(timeout);
/*  96 */     if (timeoutNanos <= 2147483647999999999L) {
/*  97 */       return super.get(timeout, unit);
/*     */     }
/*     */     
/* 100 */     return super.get(
/* 101 */         Math.min(timeoutNanos, 2147483647999999999L), TimeUnit.NANOSECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void done() {
/* 107 */     this.executionList.execute();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\commo\\util\concurrent\ListenableFutureTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */