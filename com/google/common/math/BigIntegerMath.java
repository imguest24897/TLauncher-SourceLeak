/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class BigIntegerMath
/*     */ {
/*     */   @VisibleForTesting
/*     */   static final int SQRT2_PRECOMPUTE_THRESHOLD = 256;
/*     */   
/*     */   @Beta
/*     */   public static BigInteger ceilingPowerOfTwo(BigInteger x) {
/*  61 */     return BigInteger.ZERO.setBit(log2(x, RoundingMode.CEILING));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static BigInteger floorPowerOfTwo(BigInteger x) {
/*  73 */     return BigInteger.ZERO.setBit(log2(x, RoundingMode.FLOOR));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isPowerOfTwo(BigInteger x) {
/*  78 */     Preconditions.checkNotNull(x);
/*  79 */     return (x.signum() > 0 && x.getLowestSetBit() == x.bitLength() - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int log2(BigInteger x, RoundingMode mode) {
/*     */     BigInteger x2;
/*     */     int logX2Floor;
/*  92 */     MathPreconditions.checkPositive("x", (BigInteger)Preconditions.checkNotNull(x));
/*  93 */     int logFloor = x.bitLength() - 1;
/*  94 */     switch (mode) {
/*     */       case UNNECESSARY:
/*  96 */         MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
/*     */       case DOWN:
/*     */       case FLOOR:
/*  99 */         return logFloor;
/*     */       
/*     */       case UP:
/*     */       case CEILING:
/* 103 */         return isPowerOfTwo(x) ? logFloor : (logFloor + 1);
/*     */       
/*     */       case HALF_DOWN:
/*     */       case HALF_UP:
/*     */       case HALF_EVEN:
/* 108 */         if (logFloor < 256) {
/*     */           
/* 110 */           BigInteger halfPower = SQRT2_PRECOMPUTED_BITS.shiftRight(256 - logFloor);
/* 111 */           if (x.compareTo(halfPower) <= 0) {
/* 112 */             return logFloor;
/*     */           }
/* 114 */           return logFloor + 1;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 121 */         x2 = x.pow(2);
/* 122 */         logX2Floor = x2.bitLength() - 1;
/* 123 */         return (logX2Floor < 2 * logFloor + 1) ? logFloor : (logFloor + 1);
/*     */     } 
/*     */     
/* 126 */     throw new AssertionError();
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
/*     */   @VisibleForTesting
/* 138 */   static final BigInteger SQRT2_PRECOMPUTED_BITS = new BigInteger("16a09e667f3bcc908b2fb1366ea957d3e3adec17512775099da2f590b0667322a", 16);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static int log10(BigInteger x, RoundingMode mode) {
/*     */     BigInteger x2, halfPowerSquared;
/* 151 */     MathPreconditions.checkPositive("x", x);
/* 152 */     if (fitsInLong(x)) {
/* 153 */       return LongMath.log10(x.longValue(), mode);
/*     */     }
/*     */     
/* 156 */     int approxLog10 = (int)(log2(x, RoundingMode.FLOOR) * LN_2 / LN_10);
/* 157 */     BigInteger approxPow = BigInteger.TEN.pow(approxLog10);
/* 158 */     int approxCmp = approxPow.compareTo(x);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 165 */     if (approxCmp > 0) {
/*     */ 
/*     */       
/*     */       do {
/*     */ 
/*     */ 
/*     */         
/* 172 */         approxLog10--;
/* 173 */         approxPow = approxPow.divide(BigInteger.TEN);
/* 174 */         approxCmp = approxPow.compareTo(x);
/* 175 */       } while (approxCmp > 0);
/*     */     } else {
/* 177 */       BigInteger nextPow = BigInteger.TEN.multiply(approxPow);
/* 178 */       int nextCmp = nextPow.compareTo(x);
/* 179 */       while (nextCmp <= 0) {
/* 180 */         approxLog10++;
/* 181 */         approxPow = nextPow;
/* 182 */         approxCmp = nextCmp;
/* 183 */         nextPow = BigInteger.TEN.multiply(approxPow);
/* 184 */         nextCmp = nextPow.compareTo(x);
/*     */       } 
/*     */     } 
/*     */     
/* 188 */     int floorLog = approxLog10;
/* 189 */     BigInteger floorPow = approxPow;
/* 190 */     int floorCmp = approxCmp;
/*     */     
/* 192 */     switch (mode) {
/*     */       case UNNECESSARY:
/* 194 */         MathPreconditions.checkRoundingUnnecessary((floorCmp == 0));
/*     */       
/*     */       case DOWN:
/*     */       case FLOOR:
/* 198 */         return floorLog;
/*     */       
/*     */       case UP:
/*     */       case CEILING:
/* 202 */         return floorPow.equals(x) ? floorLog : (floorLog + 1);
/*     */ 
/*     */       
/*     */       case HALF_DOWN:
/*     */       case HALF_UP:
/*     */       case HALF_EVEN:
/* 208 */         x2 = x.pow(2);
/* 209 */         halfPowerSquared = floorPow.pow(2).multiply(BigInteger.TEN);
/* 210 */         return (x2.compareTo(halfPowerSquared) <= 0) ? floorLog : (floorLog + 1);
/*     */     } 
/* 212 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   
/* 216 */   private static final double LN_10 = Math.log(10.0D);
/* 217 */   private static final double LN_2 = Math.log(2.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static BigInteger sqrt(BigInteger x, RoundingMode mode) {
/*     */     int sqrtFloorInt;
/*     */     boolean sqrtFloorIsExact;
/*     */     BigInteger halfSquare;
/* 229 */     MathPreconditions.checkNonNegative("x", x);
/* 230 */     if (fitsInLong(x)) {
/* 231 */       return BigInteger.valueOf(LongMath.sqrt(x.longValue(), mode));
/*     */     }
/* 233 */     BigInteger sqrtFloor = sqrtFloor(x);
/* 234 */     switch (mode) {
/*     */       case UNNECESSARY:
/* 236 */         MathPreconditions.checkRoundingUnnecessary(sqrtFloor.pow(2).equals(x));
/*     */       case DOWN:
/*     */       case FLOOR:
/* 239 */         return sqrtFloor;
/*     */       case UP:
/*     */       case CEILING:
/* 242 */         sqrtFloorInt = sqrtFloor.intValue();
/*     */ 
/*     */         
/* 245 */         sqrtFloorIsExact = (sqrtFloorInt * sqrtFloorInt == x.intValue() && sqrtFloor.pow(2).equals(x));
/* 246 */         return sqrtFloorIsExact ? sqrtFloor : sqrtFloor.add(BigInteger.ONE);
/*     */       case HALF_DOWN:
/*     */       case HALF_UP:
/*     */       case HALF_EVEN:
/* 250 */         halfSquare = sqrtFloor.pow(2).add(sqrtFloor);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 256 */         return (halfSquare.compareTo(x) >= 0) ? sqrtFloor : sqrtFloor.add(BigInteger.ONE);
/*     */     } 
/* 258 */     throw new AssertionError();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private static BigInteger sqrtFloor(BigInteger x) {
/*     */     BigInteger sqrt0;
/* 284 */     int log2 = log2(x, RoundingMode.FLOOR);
/* 285 */     if (log2 < 1023) {
/* 286 */       sqrt0 = sqrtApproxWithDoubles(x);
/*     */     } else {
/* 288 */       int shift = log2 - 52 & 0xFFFFFFFE;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 293 */       sqrt0 = sqrtApproxWithDoubles(x.shiftRight(shift)).shiftLeft(shift >> 1);
/*     */     } 
/* 295 */     BigInteger sqrt1 = sqrt0.add(x.divide(sqrt0)).shiftRight(1);
/* 296 */     if (sqrt0.equals(sqrt1)) {
/* 297 */       return sqrt0;
/*     */     }
/*     */     while (true) {
/* 300 */       sqrt0 = sqrt1;
/* 301 */       sqrt1 = sqrt0.add(x.divide(sqrt0)).shiftRight(1);
/* 302 */       if (sqrt1.compareTo(sqrt0) >= 0)
/* 303 */         return sqrt0; 
/*     */     } 
/*     */   }
/*     */   @GwtIncompatible
/*     */   private static BigInteger sqrtApproxWithDoubles(BigInteger x) {
/* 308 */     return DoubleMath.roundToBigInteger(Math.sqrt(DoubleUtils.bigToDouble(x)), RoundingMode.HALF_EVEN);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static double roundToDouble(BigInteger x, RoundingMode mode) {
/* 334 */     return BigIntegerToDoubleRounder.INSTANCE.roundToDouble(x, mode);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static class BigIntegerToDoubleRounder extends ToDoubleRounder<BigInteger> {
/* 339 */     static final BigIntegerToDoubleRounder INSTANCE = new BigIntegerToDoubleRounder();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     double roundToDoubleArbitrarily(BigInteger bigInteger) {
/* 345 */       return DoubleUtils.bigToDouble(bigInteger);
/*     */     }
/*     */ 
/*     */     
/*     */     int sign(BigInteger bigInteger) {
/* 350 */       return bigInteger.signum();
/*     */     }
/*     */ 
/*     */     
/*     */     BigInteger toX(double d, RoundingMode mode) {
/* 355 */       return DoubleMath.roundToBigInteger(d, mode);
/*     */     }
/*     */ 
/*     */     
/*     */     BigInteger minus(BigInteger a, BigInteger b) {
/* 360 */       return a.subtract(b);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static BigInteger divide(BigInteger p, BigInteger q, RoundingMode mode) {
/* 373 */     BigDecimal pDec = new BigDecimal(p);
/* 374 */     BigDecimal qDec = new BigDecimal(q);
/* 375 */     return pDec.divide(qDec, 0, mode).toBigIntegerExact();
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
/*     */   public static BigInteger factorial(int n) {
/* 391 */     MathPreconditions.checkNonNegative("n", n);
/*     */ 
/*     */     
/* 394 */     if (n < LongMath.factorials.length) {
/* 395 */       return BigInteger.valueOf(LongMath.factorials[n]);
/*     */     }
/*     */ 
/*     */     
/* 399 */     int approxSize = IntMath.divide(n * IntMath.log2(n, RoundingMode.CEILING), 64, RoundingMode.CEILING);
/* 400 */     ArrayList<BigInteger> bignums = new ArrayList<>(approxSize);
/*     */ 
/*     */     
/* 403 */     int startingNumber = LongMath.factorials.length;
/* 404 */     long product = LongMath.factorials[startingNumber - 1];
/*     */     
/* 406 */     int shift = Long.numberOfTrailingZeros(product);
/* 407 */     product >>= shift;
/*     */ 
/*     */     
/* 410 */     int productBits = LongMath.log2(product, RoundingMode.FLOOR) + 1;
/* 411 */     int bits = LongMath.log2(startingNumber, RoundingMode.FLOOR) + 1;
/*     */     
/* 413 */     int nextPowerOfTwo = 1 << bits - 1;
/*     */     
/*     */     long num;
/* 416 */     for (num = startingNumber; num <= n; num++) {
/*     */       
/* 418 */       if ((num & nextPowerOfTwo) != 0L) {
/* 419 */         nextPowerOfTwo <<= 1;
/* 420 */         bits++;
/*     */       } 
/*     */       
/* 423 */       int tz = Long.numberOfTrailingZeros(num);
/* 424 */       long normalizedNum = num >> tz;
/* 425 */       shift += tz;
/*     */       
/* 427 */       int normalizedBits = bits - tz;
/*     */       
/* 429 */       if (normalizedBits + productBits >= 64) {
/* 430 */         bignums.add(BigInteger.valueOf(product));
/* 431 */         product = 1L;
/* 432 */         productBits = 0;
/*     */       } 
/* 434 */       product *= normalizedNum;
/* 435 */       productBits = LongMath.log2(product, RoundingMode.FLOOR) + 1;
/*     */     } 
/*     */     
/* 438 */     if (product > 1L) {
/* 439 */       bignums.add(BigInteger.valueOf(product));
/*     */     }
/*     */     
/* 442 */     return listProduct(bignums).shiftLeft(shift);
/*     */   }
/*     */   
/*     */   static BigInteger listProduct(List<BigInteger> nums) {
/* 446 */     return listProduct(nums, 0, nums.size());
/*     */   }
/*     */   
/*     */   static BigInteger listProduct(List<BigInteger> nums, int start, int end) {
/* 450 */     switch (end - start) {
/*     */       case 0:
/* 452 */         return BigInteger.ONE;
/*     */       case 1:
/* 454 */         return nums.get(start);
/*     */       case 2:
/* 456 */         return ((BigInteger)nums.get(start)).multiply(nums.get(start + 1));
/*     */       case 3:
/* 458 */         return ((BigInteger)nums.get(start)).multiply(nums.get(start + 1)).multiply(nums.get(start + 2));
/*     */     } 
/*     */     
/* 461 */     int m = end + start >>> 1;
/* 462 */     return listProduct(nums, start, m).multiply(listProduct(nums, m, end));
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
/*     */   public static BigInteger binomial(int n, int k) {
/* 475 */     MathPreconditions.checkNonNegative("n", n);
/* 476 */     MathPreconditions.checkNonNegative("k", k);
/* 477 */     Preconditions.checkArgument((k <= n), "k (%s) > n (%s)", k, n);
/* 478 */     if (k > n >> 1) {
/* 479 */       k = n - k;
/*     */     }
/* 481 */     if (k < LongMath.biggestBinomials.length && n <= LongMath.biggestBinomials[k]) {
/* 482 */       return BigInteger.valueOf(LongMath.binomial(n, k));
/*     */     }
/*     */     
/* 485 */     BigInteger accum = BigInteger.ONE;
/*     */     
/* 487 */     long numeratorAccum = n;
/* 488 */     long denominatorAccum = 1L;
/*     */     
/* 490 */     int bits = LongMath.log2(n, RoundingMode.CEILING);
/*     */     
/* 492 */     int numeratorBits = bits;
/*     */     
/* 494 */     for (int i = 1; i < k; i++) {
/* 495 */       int p = n - i;
/* 496 */       int q = i + 1;
/*     */ 
/*     */ 
/*     */       
/* 500 */       if (numeratorBits + bits >= 63) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 506 */         accum = accum.multiply(BigInteger.valueOf(numeratorAccum)).divide(BigInteger.valueOf(denominatorAccum));
/* 507 */         numeratorAccum = p;
/* 508 */         denominatorAccum = q;
/* 509 */         numeratorBits = bits;
/*     */       } else {
/*     */         
/* 512 */         numeratorAccum *= p;
/* 513 */         denominatorAccum *= q;
/* 514 */         numeratorBits += bits;
/*     */       } 
/*     */     } 
/* 517 */     return accum
/* 518 */       .multiply(BigInteger.valueOf(numeratorAccum))
/* 519 */       .divide(BigInteger.valueOf(denominatorAccum));
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   static boolean fitsInLong(BigInteger x) {
/* 525 */     return (x.bitLength() <= 63);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\math\BigIntegerMath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */