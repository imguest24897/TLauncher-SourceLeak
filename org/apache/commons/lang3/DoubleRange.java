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
/*    */ public final class DoubleRange
/*    */   extends NumberRange<Double>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public static DoubleRange of(double fromInclusive, double toInclusive) {
/* 49 */     return of(Double.valueOf(fromInclusive), Double.valueOf(toInclusive));
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
/*    */   public static DoubleRange of(Double fromInclusive, Double toInclusive) {
/* 69 */     return new DoubleRange(fromInclusive, toInclusive);
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
/*    */   private DoubleRange(Double number1, Double number2) {
/* 81 */     super(number1, number2, null);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\DoubleRange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */