/*    */ package org.apache.commons.lang3.concurrent;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.ExecutionException;
/*    */ import java.util.concurrent.Future;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.TimeoutException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractFutureProxy<V>
/*    */   implements Future<V>
/*    */ {
/*    */   private final Future<V> future;
/*    */   
/*    */   public AbstractFutureProxy(Future<V> future) {
/* 41 */     this.future = Objects.<Future<V>>requireNonNull(future, "future");
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean cancel(boolean mayInterruptIfRunning) {
/* 46 */     return this.future.cancel(mayInterruptIfRunning);
/*    */   }
/*    */ 
/*    */   
/*    */   public V get() throws InterruptedException, ExecutionException {
/* 51 */     return this.future.get();
/*    */   }
/*    */ 
/*    */   
/*    */   public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/* 56 */     return this.future.get(timeout, unit);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Future<V> getFuture() {
/* 65 */     return this.future;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancelled() {
/* 70 */     return this.future.isCancelled();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDone() {
/* 75 */     return this.future.isDone();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\concurrent\AbstractFutureProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */