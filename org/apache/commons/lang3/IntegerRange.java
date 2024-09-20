/*    */ package org.apache.commons.lang3;
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
/*    */ public final class IntegerRange
/*    */   extends NumberRange<Integer>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public static IntegerRange of(int fromInclusive, int toInclusive) {
/* 49 */     return of(Integer.valueOf(fromInclusive), Integer.valueOf(toInclusive));
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
/*    */   public static IntegerRange of(Integer fromInclusive, Integer toInclusive) {
/* 69 */     return new IntegerRange(fromInclusive, toInclusive);
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
/*    */   private IntegerRange(Integer number1, Integer number2) {
/* 81 */     super(number1, number2, null);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\IntegerRange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */