/*    */ package org.apache.commons.lang3.concurrent;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.Future;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
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
/*    */ public interface UncheckedFuture<V>
/*    */   extends Future<V>
/*    */ {
/*    */   static <T> Stream<UncheckedFuture<T>> map(Collection<Future<T>> futures) {
/* 49 */     return futures.stream().map(UncheckedFuture::on);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static <T> Collection<UncheckedFuture<T>> on(Collection<Future<T>> futures) {
/* 61 */     return (Collection<UncheckedFuture<T>>)map(futures).collect(Collectors.toList());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static <T> UncheckedFuture<T> on(Future<T> future) {
/* 73 */     return new UncheckedFutureImpl<>(future);
/*    */   }
/*    */   
/*    */   V get();
/*    */   
/*    */   V get(long paramLong, TimeUnit paramTimeUnit);
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\concurrent\UncheckedFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */