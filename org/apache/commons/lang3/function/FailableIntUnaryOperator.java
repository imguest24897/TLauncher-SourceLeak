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
/*    */ public interface FailableIntUnaryOperator<E extends Throwable>
/*    */ {
/*    */   public static final FailableIntUnaryOperator NOP = t -> 0;
/*    */   
/*    */   static <E extends Throwable> FailableIntUnaryOperator<E> identity() {
/* 42 */     return t -> t;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static <E extends Throwable> FailableIntUnaryOperator<E> nop() {
/* 53 */     return NOP;
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
/*    */   default FailableIntUnaryOperator<E> andThen(FailableIntUnaryOperator<E> after) {
/* 65 */     Objects.requireNonNull(after);
/* 66 */     return t -> after.applyAsInt(applyAsInt(t));
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
/*    */   default FailableIntUnaryOperator<E> compose(FailableIntUnaryOperator<E> before) {
/* 87 */     Objects.requireNonNull(before);
/* 88 */     return v -> applyAsInt(before.applyAsInt(v));
/*    */   }
/*    */   
/*    */   int applyAsInt(int paramInt) throws E;
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\function\FailableIntUnaryOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */