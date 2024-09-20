/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.math.RoundingMode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ abstract class ToDoubleRounder<X extends Number & Comparable<X>>
/*     */ {
/*     */   abstract double roundToDoubleArbitrarily(X paramX);
/*     */   
/*     */   abstract int sign(X paramX);
/*     */   
/*     */   abstract X toX(double paramDouble, RoundingMode paramRoundingMode);
/*     */   
/*     */   abstract X minus(X paramX1, X paramX2);
/*     */   
/*     */   final double roundToDouble(X x, RoundingMode mode) {
/*     */     X roundFloor;
/*     */     double roundFloorAsDouble;
/*     */     X roundCeiling;
/*     */     double roundCeilingAsDouble;
/*     */     X deltaToFloor, deltaToCeiling;
/*     */     int diff;
/*  46 */     Preconditions.checkNotNull(x, "x");
/*  47 */     Preconditions.checkNotNull(mode, "mode");
/*  48 */     double roundArbitrarily = roundToDoubleArbitrarily(x);
/*  49 */     if (Double.isInfinite(roundArbitrarily)) {
/*  50 */       String str; switch (mode) {
/*     */         case DOWN:
/*     */         case HALF_EVEN:
/*     */         case HALF_DOWN:
/*     */         case HALF_UP:
/*  55 */           return Double.MAX_VALUE * sign(x);
/*     */         case FLOOR:
/*  57 */           return (roundArbitrarily == Double.POSITIVE_INFINITY) ? 
/*  58 */             Double.MAX_VALUE : 
/*  59 */             Double.NEGATIVE_INFINITY;
/*     */         case CEILING:
/*  61 */           return (roundArbitrarily == Double.POSITIVE_INFINITY) ? 
/*  62 */             Double.POSITIVE_INFINITY : 
/*  63 */             -1.7976931348623157E308D;
/*     */         case UP:
/*  65 */           return roundArbitrarily;
/*     */         case UNNECESSARY:
/*  67 */           str = String.valueOf(x); throw new ArithmeticException((new StringBuilder(44 + String.valueOf(str).length())).append(str).append(" cannot be represented precisely as a double").toString());
/*     */       } 
/*     */     } 
/*  70 */     X roundArbitrarilyAsX = toX(roundArbitrarily, RoundingMode.UNNECESSARY);
/*  71 */     int cmpXToRoundArbitrarily = ((Comparable<X>)x).compareTo(roundArbitrarilyAsX);
/*  72 */     switch (mode) {
/*     */       case UNNECESSARY:
/*  74 */         MathPreconditions.checkRoundingUnnecessary((cmpXToRoundArbitrarily == 0));
/*  75 */         return roundArbitrarily;
/*     */       case FLOOR:
/*  77 */         return (cmpXToRoundArbitrarily >= 0) ? 
/*  78 */           roundArbitrarily : 
/*  79 */           DoubleUtils.nextDown(roundArbitrarily);
/*     */       case CEILING:
/*  81 */         return (cmpXToRoundArbitrarily <= 0) ? roundArbitrarily : Math.nextUp(roundArbitrarily);
/*     */       case DOWN:
/*  83 */         if (sign(x) >= 0) {
/*  84 */           return (cmpXToRoundArbitrarily >= 0) ? 
/*  85 */             roundArbitrarily : 
/*  86 */             DoubleUtils.nextDown(roundArbitrarily);
/*     */         }
/*  88 */         return (cmpXToRoundArbitrarily <= 0) ? roundArbitrarily : Math.nextUp(roundArbitrarily);
/*     */       
/*     */       case UP:
/*  91 */         if (sign(x) >= 0) {
/*  92 */           return (cmpXToRoundArbitrarily <= 0) ? roundArbitrarily : Math.nextUp(roundArbitrarily);
/*     */         }
/*  94 */         return (cmpXToRoundArbitrarily >= 0) ? 
/*  95 */           roundArbitrarily : 
/*  96 */           DoubleUtils.nextDown(roundArbitrarily);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case HALF_EVEN:
/*     */       case HALF_DOWN:
/*     */       case HALF_UP:
/* 107 */         if (cmpXToRoundArbitrarily >= 0) {
/* 108 */           roundFloorAsDouble = roundArbitrarily;
/* 109 */           roundFloor = roundArbitrarilyAsX;
/* 110 */           roundCeilingAsDouble = Math.nextUp(roundArbitrarily);
/* 111 */           if (roundCeilingAsDouble == Double.POSITIVE_INFINITY) {
/* 112 */             return roundFloorAsDouble;
/*     */           }
/* 114 */           roundCeiling = toX(roundCeilingAsDouble, RoundingMode.CEILING);
/*     */         } else {
/* 116 */           roundCeilingAsDouble = roundArbitrarily;
/* 117 */           roundCeiling = roundArbitrarilyAsX;
/* 118 */           roundFloorAsDouble = DoubleUtils.nextDown(roundArbitrarily);
/* 119 */           if (roundFloorAsDouble == Double.NEGATIVE_INFINITY) {
/* 120 */             return roundCeilingAsDouble;
/*     */           }
/* 122 */           roundFloor = toX(roundFloorAsDouble, RoundingMode.FLOOR);
/*     */         } 
/*     */         
/* 125 */         deltaToFloor = minus(x, roundFloor);
/* 126 */         deltaToCeiling = minus(roundCeiling, x);
/* 127 */         diff = ((Comparable<X>)deltaToFloor).compareTo(deltaToCeiling);
/* 128 */         if (diff < 0)
/* 129 */           return roundFloorAsDouble; 
/* 130 */         if (diff > 0) {
/* 131 */           return roundCeilingAsDouble;
/*     */         }
/*     */         
/* 134 */         switch (mode) {
/*     */ 
/*     */           
/*     */           case HALF_EVEN:
/* 138 */             return ((Double.doubleToRawLongBits(roundFloorAsDouble) & 0x1L) == 0L) ? 
/* 139 */               roundFloorAsDouble : 
/* 140 */               roundCeilingAsDouble;
/*     */           case HALF_DOWN:
/* 142 */             return (sign(x) >= 0) ? roundFloorAsDouble : roundCeilingAsDouble;
/*     */           case HALF_UP:
/* 144 */             return (sign(x) >= 0) ? roundCeilingAsDouble : roundFloorAsDouble;
/*     */         } 
/* 146 */         throw new AssertionError("impossible");
/*     */     } 
/*     */ 
/*     */     
/* 150 */     throw new AssertionError("impossible");
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\math\ToDoubleRounder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */