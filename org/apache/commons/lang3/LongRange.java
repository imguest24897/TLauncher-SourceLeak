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
/*    */ public final class LongRange
/*    */   extends NumberRange<Long>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public static LongRange of(long fromInclusive, long toInclusive) {
/* 49 */     return of(Long.valueOf(fromInclusive), Long.valueOf(toInclusive));
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
/*    */   public static LongRange of(Long fromInclusive, Long toInclusive) {
/* 69 */     return new LongRange(fromInclusive, toInclusive);
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
/*    */   private LongRange(Long number1, Long number2) {
/* 81 */     super(number1, number2, null);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\LongRange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */