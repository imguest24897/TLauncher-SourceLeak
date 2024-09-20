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
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface FailableIntPredicate<E extends Throwable>
/*    */ {
/*    */   public static final FailableIntPredicate FALSE = t -> false;
/*    */   public static final FailableIntPredicate TRUE = t -> true;
/*    */   
/*    */   static <E extends Throwable> FailableIntPredicate<E> falsePredicate() {
/* 48 */     return FALSE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static <E extends Throwable> FailableIntPredicate<E> truePredicate() {
/* 59 */     return TRUE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default FailableIntPredicate<E> and(FailableIntPredicate<E> other) {
/* 70 */     Objects.requireNonNull(other);
/* 71 */     return t -> (test(t) && other.test(t));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default FailableIntPredicate<E> negate() {
/* 80 */     return t -> !test(t);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default FailableIntPredicate<E> or(FailableIntPredicate<E> other) {
/* 91 */     Objects.requireNonNull(other);
/* 92 */     return t -> (test(t) || other.test(t));
/*    */   }
/*    */   
/*    */   boolean test(int paramInt) throws E;
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\function\FailableIntPredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */