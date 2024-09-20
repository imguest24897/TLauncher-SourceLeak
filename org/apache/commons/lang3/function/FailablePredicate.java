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
/*    */ @FunctionalInterface
/*    */ public interface FailablePredicate<T, E extends Throwable>
/*    */ {
/*    */   public static final FailablePredicate FALSE = t -> false;
/*    */   public static final FailablePredicate TRUE = t -> true;
/*    */   
/*    */   static <T, E extends Throwable> FailablePredicate<T, E> falsePredicate() {
/* 50 */     return FALSE;
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
/*    */   static <T, E extends Throwable> FailablePredicate<T, E> truePredicate() {
/* 62 */     return TRUE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default FailablePredicate<T, E> and(FailablePredicate<? super T, E> other) {
/* 73 */     Objects.requireNonNull(other);
/* 74 */     return t -> (test((T)t) && other.test(t));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default FailablePredicate<T, E> negate() {
/* 83 */     return t -> !test((T)t);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default FailablePredicate<T, E> or(FailablePredicate<? super T, E> other) {
/* 94 */     Objects.requireNonNull(other);
/* 95 */     return t -> (test((T)t) || other.test(t));
/*    */   }
/*    */   
/*    */   boolean test(T paramT) throws E;
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\function\FailablePredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */