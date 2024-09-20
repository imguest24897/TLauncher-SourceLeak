/*    */ package com.google.common.math;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.math.BigDecimal;
/*    */ import java.math.RoundingMode;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtIncompatible
/*    */ public class BigDecimalMath
/*    */ {
/*    */   public static double roundToDouble(BigDecimal x, RoundingMode mode) {
/* 53 */     return BigDecimalToDoubleRounder.INSTANCE.roundToDouble(x, mode);
/*    */   }
/*    */   
/*    */   private static class BigDecimalToDoubleRounder extends ToDoubleRounder<BigDecimal> {
/* 57 */     static final BigDecimalToDoubleRounder INSTANCE = new BigDecimalToDoubleRounder();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     double roundToDoubleArbitrarily(BigDecimal bigDecimal) {
/* 63 */       return bigDecimal.doubleValue();
/*    */     }
/*    */ 
/*    */     
/*    */     int sign(BigDecimal bigDecimal) {
/* 68 */       return bigDecimal.signum();
/*    */     }
/*    */ 
/*    */     
/*    */     BigDecimal toX(double d, RoundingMode mode) {
/* 73 */       return new BigDecimal(d);
/*    */     }
/*    */ 
/*    */     
/*    */     BigDecimal minus(BigDecimal a, BigDecimal b) {
/* 78 */       return a.subtract(b);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\math\BigDecimalMath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */