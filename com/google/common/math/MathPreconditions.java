/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.math.BigInteger;
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
/*     */ @GwtCompatible
/*     */ @CanIgnoreReturnValue
/*     */ final class MathPreconditions
/*     */ {
/*     */   static int checkPositive(String role, int x) {
/*  32 */     if (x <= 0) {
/*  33 */       throw new IllegalArgumentException((new StringBuilder(26 + String.valueOf(role).length())).append(role).append(" (").append(x).append(") must be > 0").toString());
/*     */     }
/*  35 */     return x;
/*     */   }
/*     */   
/*     */   static long checkPositive(String role, long x) {
/*  39 */     if (x <= 0L) {
/*  40 */       throw new IllegalArgumentException((new StringBuilder(35 + String.valueOf(role).length())).append(role).append(" (").append(x).append(") must be > 0").toString());
/*     */     }
/*  42 */     return x;
/*     */   }
/*     */   
/*     */   static BigInteger checkPositive(String role, BigInteger x) {
/*  46 */     if (x.signum() <= 0) {
/*  47 */       String str = String.valueOf(x); throw new IllegalArgumentException((new StringBuilder(15 + String.valueOf(role).length() + String.valueOf(str).length())).append(role).append(" (").append(str).append(") must be > 0").toString());
/*     */     } 
/*  49 */     return x;
/*     */   }
/*     */   
/*     */   static int checkNonNegative(String role, int x) {
/*  53 */     if (x < 0) {
/*  54 */       throw new IllegalArgumentException((new StringBuilder(27 + String.valueOf(role).length())).append(role).append(" (").append(x).append(") must be >= 0").toString());
/*     */     }
/*  56 */     return x;
/*     */   }
/*     */   
/*     */   static long checkNonNegative(String role, long x) {
/*  60 */     if (x < 0L) {
/*  61 */       throw new IllegalArgumentException((new StringBuilder(36 + String.valueOf(role).length())).append(role).append(" (").append(x).append(") must be >= 0").toString());
/*     */     }
/*  63 */     return x;
/*     */   }
/*     */   
/*     */   static BigInteger checkNonNegative(String role, BigInteger x) {
/*  67 */     if (x.signum() < 0) {
/*  68 */       String str = String.valueOf(x); throw new IllegalArgumentException((new StringBuilder(16 + String.valueOf(role).length() + String.valueOf(str).length())).append(role).append(" (").append(str).append(") must be >= 0").toString());
/*     */     } 
/*  70 */     return x;
/*     */   }
/*     */   
/*     */   static double checkNonNegative(String role, double x) {
/*  74 */     if (x < 0.0D) {
/*  75 */       throw new IllegalArgumentException((new StringBuilder(40 + String.valueOf(role).length())).append(role).append(" (").append(x).append(") must be >= 0").toString());
/*     */     }
/*  77 */     return x;
/*     */   }
/*     */   
/*     */   static void checkRoundingUnnecessary(boolean condition) {
/*  81 */     if (!condition) {
/*  82 */       throw new ArithmeticException("mode was UNNECESSARY, but rounding was necessary");
/*     */     }
/*     */   }
/*     */   
/*     */   static void checkInRangeForRoundingInputs(boolean condition, double input, RoundingMode mode) {
/*  87 */     if (!condition) {
/*  88 */       String str = String.valueOf(mode); throw new ArithmeticException((new StringBuilder(83 + String.valueOf(str).length())).append("rounded value is out of range for input ").append(input).append(" and rounding mode ").append(str).toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static void checkNoOverflow(boolean condition, String methodName, int a, int b) {
/*  94 */     if (!condition) {
/*  95 */       throw new ArithmeticException((new StringBuilder(36 + String.valueOf(methodName).length())).append("overflow: ").append(methodName).append("(").append(a).append(", ").append(b).append(")").toString());
/*     */     }
/*     */   }
/*     */   
/*     */   static void checkNoOverflow(boolean condition, String methodName, long a, long b) {
/* 100 */     if (!condition)
/* 101 */       throw new ArithmeticException((new StringBuilder(54 + String.valueOf(methodName).length())).append("overflow: ").append(methodName).append("(").append(a).append(", ").append(b).append(")").toString()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\math\MathPreconditions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */