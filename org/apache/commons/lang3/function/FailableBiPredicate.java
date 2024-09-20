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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface FailableBiPredicate<T, U, E extends Throwable>
/*    */ {
/*    */   public static final FailableBiPredicate FALSE = (t, u) -> false;
/*    */   public static final FailableBiPredicate TRUE = (t, u) -> true;
/*    */   
/*    */   static <T, U, E extends Throwable> FailableBiPredicate<T, U, E> falsePredicate() {
/* 52 */     return FALSE;
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
/*    */   static <T, U, E extends Throwable> FailableBiPredicate<T, U, E> truePredicate() {
/* 65 */     return TRUE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default FailableBiPredicate<T, U, E> and(FailableBiPredicate<? super T, ? super U, E> other) {
/* 76 */     Objects.requireNonNull(other);
/* 77 */     return (t, u) -> (test((T)t, (U)u) && other.test(t, u));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default FailableBiPredicate<T, U, E> negate() {
/* 86 */     return (t, u) -> !test((T)t, (U)u);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default FailableBiPredicate<T, U, E> or(FailableBiPredicate<? super T, ? super U, E> other) {
/* 97 */     Objects.requireNonNull(other);
/* 98 */     return (t, u) -> (test((T)t, (U)u) || other.test(t, u));
/*    */   }
/*    */   
/*    */   boolean test(T paramT, U paramU) throws E;
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\function\FailableBiPredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */