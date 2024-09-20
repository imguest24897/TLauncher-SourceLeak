/*    */ package org.apache.commons.lang3.function;
/*    */ 
/*    */ import java.util.Objects;
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
/*    */ @FunctionalInterface
/*    */ public interface FailableFunction<T, R, E extends Throwable>
/*    */ {
/*    */   public static final FailableFunction NOP = t -> null;
/*    */   
/*    */   static <T, E extends Throwable> FailableFunction<T, T, E> identity() {
/* 46 */     return t -> t;
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
/*    */   
/*    */   static <T, R, E extends Throwable> FailableFunction<T, R, E> nop() {
/* 59 */     return NOP;
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
/*    */   default <V> FailableFunction<T, V, E> andThen(FailableFunction<? super R, ? extends V, E> after) {
/* 71 */     Objects.requireNonNull(after);
/* 72 */     return t -> after.apply(apply((T)t));
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
/*    */   default <V> FailableFunction<V, R, E> compose(FailableFunction<? super V, ? extends T, E> before) {
/* 94 */     Objects.requireNonNull(before);
/* 95 */     return v -> apply((T)before.apply(v));
/*    */   }
/*    */   
/*    */   R apply(T paramT) throws E;
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\function\FailableFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */