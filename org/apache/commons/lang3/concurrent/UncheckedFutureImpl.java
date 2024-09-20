/*    */ package org.apache.commons.lang3.concurrent;
/*    */ 
/*    */ import java.util.concurrent.ExecutionException;
/*    */ import java.util.concurrent.Future;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.TimeoutException;
/*    */ import org.apache.commons.lang3.exception.UncheckedInterruptedException;
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
/*    */ class UncheckedFutureImpl<V>
/*    */   extends AbstractFutureProxy<V>
/*    */   implements UncheckedFuture<V>
/*    */ {
/*    */   UncheckedFutureImpl(Future<V> future) {
/* 37 */     super(future);
/*    */   }
/*    */ 
/*    */   
/*    */   public V get() {
/*    */     try {
/* 43 */       return super.get();
/* 44 */     } catch (InterruptedException e) {
/* 45 */       throw new UncheckedInterruptedException(e);
/* 46 */     } catch (ExecutionException e) {
/* 47 */       throw new UncheckedExecutionException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public V get(long timeout, TimeUnit unit) {
/*    */     try {
/* 54 */       return super.get(timeout, unit);
/* 55 */     } catch (InterruptedException e) {
/* 56 */       throw new UncheckedInterruptedException(e);
/* 57 */     } catch (ExecutionException e) {
/* 58 */       throw new UncheckedExecutionException(e);
/* 59 */     } catch (TimeoutException e) {
/* 60 */       throw new UncheckedTimeoutException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\concurrent\UncheckedFutureImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */