/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.util.concurrent.ThreadLocalRandom;
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
/*     */ @Deprecated
/*     */ public class RandomUtils
/*     */ {
/*     */   public static boolean nextBoolean() {
/*  46 */     return random().nextBoolean();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] nextBytes(int count) {
/*  58 */     Validate.isTrue((count >= 0), "Count cannot be negative.", new Object[0]);
/*     */     
/*  60 */     byte[] result = new byte[count];
/*  61 */     random().nextBytes(result);
/*  62 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double nextDouble() {
/*  73 */     return nextDouble(0.0D, Double.MAX_VALUE);
/*     */   }
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
/*     */   public static double nextDouble(double startInclusive, double endExclusive) {
/*  89 */     Validate.isTrue((endExclusive >= startInclusive), "Start value must be smaller or equal to end value.", new Object[0]);
/*     */     
/*  91 */     Validate.isTrue((startInclusive >= 0.0D), "Both range values must be non-negative.", new Object[0]);
/*     */     
/*  93 */     if (startInclusive == endExclusive) {
/*  94 */       return startInclusive;
/*     */     }
/*     */     
/*  97 */     return startInclusive + (endExclusive - startInclusive) * random().nextDouble();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float nextFloat() {
/* 108 */     return nextFloat(0.0F, Float.MAX_VALUE);
/*     */   }
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
/*     */   public static float nextFloat(float startInclusive, float endExclusive) {
/* 124 */     Validate.isTrue((endExclusive >= startInclusive), "Start value must be smaller or equal to end value.", new Object[0]);
/*     */     
/* 126 */     Validate.isTrue((startInclusive >= 0.0F), "Both range values must be non-negative.", new Object[0]);
/*     */     
/* 128 */     if (startInclusive == endExclusive) {
/* 129 */       return startInclusive;
/*     */     }
/*     */     
/* 132 */     return startInclusive + (endExclusive - startInclusive) * random().nextFloat();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int nextInt() {
/* 143 */     return nextInt(0, 2147483647);
/*     */   }
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
/*     */   public static int nextInt(int startInclusive, int endExclusive) {
/* 159 */     Validate.isTrue((endExclusive >= startInclusive), "Start value must be smaller or equal to end value.", new Object[0]);
/*     */     
/* 161 */     Validate.isTrue((startInclusive >= 0), "Both range values must be non-negative.", new Object[0]);
/*     */     
/* 163 */     if (startInclusive == endExclusive) {
/* 164 */       return startInclusive;
/*     */     }
/*     */     
/* 167 */     return startInclusive + random().nextInt(endExclusive - startInclusive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long nextLong() {
/* 178 */     return nextLong(Long.MAX_VALUE);
/*     */   }
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
/*     */   private static long nextLong(long n) {
/*     */     long bits;
/*     */     long val;
/*     */     do {
/* 194 */       bits = random().nextLong() >>> 1L;
/* 195 */       val = bits % n;
/* 196 */     } while (bits - val + n - 1L < 0L);
/*     */     
/* 198 */     return val;
/*     */   }
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
/*     */   public static long nextLong(long startInclusive, long endExclusive) {
/* 214 */     Validate.isTrue((endExclusive >= startInclusive), "Start value must be smaller or equal to end value.", new Object[0]);
/*     */     
/* 216 */     Validate.isTrue((startInclusive >= 0L), "Both range values must be non-negative.", new Object[0]);
/*     */     
/* 218 */     if (startInclusive == endExclusive) {
/* 219 */       return startInclusive;
/*     */     }
/*     */     
/* 222 */     return startInclusive + nextLong(endExclusive - startInclusive);
/*     */   }
/*     */   
/*     */   private static ThreadLocalRandom random() {
/* 226 */     return ThreadLocalRandom.current();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\RandomUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */