/*      */ package com.google.common.math;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.primitives.Longs;
/*      */ import com.google.common.primitives.UnsignedLongs;
/*      */ import java.math.RoundingMode;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class LongMath
/*      */ {
/*      */   @VisibleForTesting
/*      */   static final long MAX_SIGNED_POWER_OF_TWO = 4611686018427387904L;
/*      */   @VisibleForTesting
/*      */   static final long MAX_POWER_OF_SQRT2_UNSIGNED = -5402926248376769404L;
/*      */   
/*      */   @Beta
/*      */   public static long ceilingPowerOfTwo(long x) {
/*   68 */     MathPreconditions.checkPositive("x", x);
/*   69 */     if (x > 4611686018427387904L) {
/*   70 */       throw new ArithmeticException((new StringBuilder(70)).append("ceilingPowerOfTwo(").append(x).append(") is not representable as a long").toString());
/*      */     }
/*   72 */     return 1L << -Long.numberOfLeadingZeros(x - 1L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static long floorPowerOfTwo(long x) {
/*   84 */     MathPreconditions.checkPositive("x", x);
/*      */ 
/*      */ 
/*      */     
/*   88 */     return 1L << 63 - Long.numberOfLeadingZeros(x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPowerOfTwo(long x) {
/*   98 */     return ((x > 0L)) & (((x & x - 1L) == 0L));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   static int lessThanBranchFree(long x, long y) {
/*  109 */     return (int)((x - y ^ 0xFFFFFFFFFFFFFFFFL ^ 0xFFFFFFFFFFFFFFFFL) >>> 63L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int log2(long x, RoundingMode mode) {
/*      */     int leadingZeros;
/*      */     long cmp;
/*      */     int logFloor;
/*  122 */     MathPreconditions.checkPositive("x", x);
/*  123 */     switch (mode) {
/*      */       case UNNECESSARY:
/*  125 */         MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
/*      */       
/*      */       case DOWN:
/*      */       case FLOOR:
/*  129 */         return 63 - Long.numberOfLeadingZeros(x);
/*      */       
/*      */       case UP:
/*      */       case CEILING:
/*  133 */         return 64 - Long.numberOfLeadingZeros(x - 1L);
/*      */ 
/*      */       
/*      */       case HALF_DOWN:
/*      */       case HALF_UP:
/*      */       case HALF_EVEN:
/*  139 */         leadingZeros = Long.numberOfLeadingZeros(x);
/*  140 */         cmp = -5402926248376769404L >>> leadingZeros;
/*      */         
/*  142 */         logFloor = 63 - leadingZeros;
/*  143 */         return logFloor + lessThanBranchFree(cmp, x);
/*      */     } 
/*      */     
/*  146 */     throw new AssertionError("impossible");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static int log10(long x, RoundingMode mode) {
/*  164 */     MathPreconditions.checkPositive("x", x);
/*  165 */     int logFloor = log10Floor(x);
/*  166 */     long floorPow = powersOf10[logFloor];
/*  167 */     switch (mode) {
/*      */       case UNNECESSARY:
/*  169 */         MathPreconditions.checkRoundingUnnecessary((x == floorPow));
/*      */       
/*      */       case DOWN:
/*      */       case FLOOR:
/*  173 */         return logFloor;
/*      */       case UP:
/*      */       case CEILING:
/*  176 */         return logFloor + lessThanBranchFree(floorPow, x);
/*      */       
/*      */       case HALF_DOWN:
/*      */       case HALF_UP:
/*      */       case HALF_EVEN:
/*  181 */         return logFloor + lessThanBranchFree(halfPowersOf10[logFloor], x);
/*      */     } 
/*  183 */     throw new AssertionError();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   static int log10Floor(long x) {
/*  196 */     int y = maxLog10ForLeadingZeros[Long.numberOfLeadingZeros(x)];
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  201 */     return y - lessThanBranchFree(x, powersOf10[y]);
/*      */   }
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*  206 */   static final byte[] maxLog10ForLeadingZeros = new byte[] { 19, 18, 18, 18, 18, 17, 17, 17, 16, 16, 16, 15, 15, 15, 15, 14, 14, 14, 13, 13, 13, 12, 12, 12, 12, 11, 11, 11, 10, 10, 10, 9, 9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 1, 1, 1, 0, 0, 0 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   @VisibleForTesting
/*  214 */   static final long[] powersOf10 = new long[] { 1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 1000000000L, 10000000000L, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   @VisibleForTesting
/*  239 */   static final long[] halfPowersOf10 = new long[] { 3L, 31L, 316L, 3162L, 31622L, 316227L, 3162277L, 31622776L, 316227766L, 3162277660L, 31622776601L, 316227766016L, 3162277660168L, 31622776601683L, 316227766016837L, 3162277660168379L, 31622776601683793L, 316227766016837933L, 3162277660168379331L };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   static final long FLOOR_SQRT_MAX_LONG = 3037000499L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static long pow(long b, int k) {
/*  270 */     MathPreconditions.checkNonNegative("exponent", k);
/*  271 */     if (-2L <= b && b <= 2L) {
/*  272 */       switch ((int)b) {
/*      */         case 0:
/*  274 */           return (k == 0) ? 1L : 0L;
/*      */         case 1:
/*  276 */           return 1L;
/*      */         case -1:
/*  278 */           return ((k & 0x1) == 0) ? 1L : -1L;
/*      */         case 2:
/*  280 */           return (k < 64) ? (1L << k) : 0L;
/*      */         case -2:
/*  282 */           if (k < 64) {
/*  283 */             return ((k & 0x1) == 0) ? (1L << k) : -(1L << k);
/*      */           }
/*  285 */           return 0L;
/*      */       } 
/*      */       
/*  288 */       throw new AssertionError();
/*      */     } 
/*      */     
/*  291 */     for (long accum = 1L;; k >>= 1) {
/*  292 */       switch (k) {
/*      */         case 0:
/*  294 */           return accum;
/*      */         case 1:
/*  296 */           return accum * b;
/*      */       } 
/*  298 */       accum *= ((k & 0x1) == 0) ? 1L : b;
/*  299 */       b *= b;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static long sqrt(long x, RoundingMode mode) {
/*      */     long sqrtFloor, halfSquare;
/*  314 */     MathPreconditions.checkNonNegative("x", x);
/*  315 */     if (fitsInInt(x)) {
/*  316 */       return IntMath.sqrt((int)x, mode);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  333 */     long guess = (long)Math.sqrt(x);
/*      */     
/*  335 */     long guessSquared = guess * guess;
/*      */ 
/*      */     
/*  338 */     switch (mode) {
/*      */       case UNNECESSARY:
/*  340 */         MathPreconditions.checkRoundingUnnecessary((guessSquared == x));
/*  341 */         return guess;
/*      */       case DOWN:
/*      */       case FLOOR:
/*  344 */         if (x < guessSquared) {
/*  345 */           return guess - 1L;
/*      */         }
/*  347 */         return guess;
/*      */       case UP:
/*      */       case CEILING:
/*  350 */         if (x > guessSquared) {
/*  351 */           return guess + 1L;
/*      */         }
/*  353 */         return guess;
/*      */       case HALF_DOWN:
/*      */       case HALF_UP:
/*      */       case HALF_EVEN:
/*  357 */         sqrtFloor = guess - ((x < guessSquared) ? 1L : 0L);
/*  358 */         halfSquare = sqrtFloor * sqrtFloor + sqrtFloor;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  370 */         return sqrtFloor + lessThanBranchFree(halfSquare, x);
/*      */     } 
/*  372 */     throw new AssertionError();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static long divide(long p, long q, RoundingMode mode) {
/*      */     boolean increment;
/*      */     long absRem, cmpRemToHalfDivisor;
/*  386 */     Preconditions.checkNotNull(mode);
/*  387 */     long div = p / q;
/*  388 */     long rem = p - q * div;
/*      */     
/*  390 */     if (rem == 0L) {
/*  391 */       return div;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  401 */     int signum = 0x1 | (int)((p ^ q) >> 63L);
/*      */     
/*  403 */     switch (mode) {
/*      */       case UNNECESSARY:
/*  405 */         MathPreconditions.checkRoundingUnnecessary((rem == 0L));
/*      */       
/*      */       case DOWN:
/*  408 */         increment = false;
/*      */         break;
/*      */       case UP:
/*  411 */         increment = true;
/*      */         break;
/*      */       case CEILING:
/*  414 */         increment = (signum > 0);
/*      */         break;
/*      */       case FLOOR:
/*  417 */         increment = (signum < 0);
/*      */         break;
/*      */       case HALF_DOWN:
/*      */       case HALF_UP:
/*      */       case HALF_EVEN:
/*  422 */         absRem = Math.abs(rem);
/*  423 */         cmpRemToHalfDivisor = absRem - Math.abs(q) - absRem;
/*      */ 
/*      */         
/*  426 */         if (cmpRemToHalfDivisor == 0L) {
/*  427 */           int i = ((mode == RoundingMode.HALF_UP) ? 1 : 0) | ((mode == RoundingMode.HALF_EVEN) ? 1 : 0) & (((div & 0x1L) != 0L) ? 1 : 0); break;
/*      */         } 
/*  429 */         increment = (cmpRemToHalfDivisor > 0L);
/*      */         break;
/*      */       
/*      */       default:
/*  433 */         throw new AssertionError();
/*      */     } 
/*  435 */     return increment ? (div + signum) : div;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static int mod(long x, int m) {
/*  459 */     return (int)mod(x, m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static long mod(long x, long m) {
/*  482 */     if (m <= 0L) {
/*  483 */       throw new ArithmeticException("Modulus must be positive");
/*      */     }
/*  485 */     long result = x % m;
/*  486 */     return (result >= 0L) ? result : (result + m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long gcd(long a, long b) {
/*  501 */     MathPreconditions.checkNonNegative("a", a);
/*  502 */     MathPreconditions.checkNonNegative("b", b);
/*  503 */     if (a == 0L)
/*      */     {
/*      */       
/*  506 */       return b; } 
/*  507 */     if (b == 0L) {
/*  508 */       return a;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  514 */     int aTwos = Long.numberOfTrailingZeros(a);
/*  515 */     a >>= aTwos;
/*  516 */     int bTwos = Long.numberOfTrailingZeros(b);
/*  517 */     b >>= bTwos;
/*  518 */     while (a != b) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  526 */       long delta = a - b;
/*      */       
/*  528 */       long minDeltaOrZero = delta & delta >> 63L;
/*      */ 
/*      */       
/*  531 */       a = delta - minDeltaOrZero - minDeltaOrZero;
/*      */ 
/*      */       
/*  534 */       b += minDeltaOrZero;
/*  535 */       a >>= Long.numberOfTrailingZeros(a);
/*      */     } 
/*  537 */     return a << Math.min(aTwos, bTwos);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static long checkedAdd(long a, long b) {
/*  547 */     long result = a + b;
/*  548 */     MathPreconditions.checkNoOverflow((((a ^ b) < 0L)) | (((a ^ result) >= 0L)), "checkedAdd", a, b);
/*  549 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static long checkedSubtract(long a, long b) {
/*  559 */     long result = a - b;
/*  560 */     MathPreconditions.checkNoOverflow((((a ^ b) >= 0L)) | (((a ^ result) >= 0L)), "checkedSubtract", a, b);
/*  561 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long checkedMultiply(long a, long b) {
/*  575 */     int leadingZeros = Long.numberOfLeadingZeros(a) + Long.numberOfLeadingZeros(a ^ 0xFFFFFFFFFFFFFFFFL) + Long.numberOfLeadingZeros(b) + Long.numberOfLeadingZeros(b ^ 0xFFFFFFFFFFFFFFFFL);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  586 */     if (leadingZeros > 65) {
/*  587 */       return a * b;
/*      */     }
/*  589 */     MathPreconditions.checkNoOverflow((leadingZeros >= 64), "checkedMultiply", a, b);
/*  590 */     MathPreconditions.checkNoOverflow(((a >= 0L)) | ((b != Long.MIN_VALUE)), "checkedMultiply", a, b);
/*  591 */     long result = a * b;
/*  592 */     MathPreconditions.checkNoOverflow((a == 0L || result / a == b), "checkedMultiply", a, b);
/*  593 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static long checkedPow(long b, int k) {
/*  604 */     MathPreconditions.checkNonNegative("exponent", k);
/*  605 */     if ((((b >= -2L) ? 1 : 0) & ((b <= 2L) ? 1 : 0)) != 0) {
/*  606 */       switch ((int)b) {
/*      */         case 0:
/*  608 */           return (k == 0) ? 1L : 0L;
/*      */         case 1:
/*  610 */           return 1L;
/*      */         case -1:
/*  612 */           return ((k & 0x1) == 0) ? 1L : -1L;
/*      */         case 2:
/*  614 */           MathPreconditions.checkNoOverflow((k < 63), "checkedPow", b, k);
/*  615 */           return 1L << k;
/*      */         case -2:
/*  617 */           MathPreconditions.checkNoOverflow((k < 64), "checkedPow", b, k);
/*  618 */           return ((k & 0x1) == 0) ? (1L << k) : (-1L << k);
/*      */       } 
/*  620 */       throw new AssertionError();
/*      */     } 
/*      */     
/*  623 */     long accum = 1L;
/*      */     while (true) {
/*  625 */       switch (k) {
/*      */         case 0:
/*  627 */           return accum;
/*      */         case 1:
/*  629 */           return checkedMultiply(accum, b);
/*      */       } 
/*  631 */       if ((k & 0x1) != 0) {
/*  632 */         accum = checkedMultiply(accum, b);
/*      */       }
/*  634 */       k >>= 1;
/*  635 */       if (k > 0) {
/*  636 */         MathPreconditions.checkNoOverflow((-3037000499L <= b && b <= 3037000499L), "checkedPow", b, k);
/*      */         
/*  638 */         b *= b;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static long saturatedAdd(long a, long b) {
/*  652 */     long naiveSum = a + b;
/*  653 */     if (((((a ^ b) < 0L) ? 1 : 0) | (((a ^ naiveSum) >= 0L) ? 1 : 0)) != 0)
/*      */     {
/*      */       
/*  656 */       return naiveSum;
/*      */     }
/*      */     
/*  659 */     return Long.MAX_VALUE + (naiveSum >>> 63L ^ 0x1L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static long saturatedSubtract(long a, long b) {
/*  670 */     long naiveDifference = a - b;
/*  671 */     if (((((a ^ b) >= 0L) ? 1 : 0) | (((a ^ naiveDifference) >= 0L) ? 1 : 0)) != 0)
/*      */     {
/*      */       
/*  674 */       return naiveDifference;
/*      */     }
/*      */     
/*  677 */     return Long.MAX_VALUE + (naiveDifference >>> 63L ^ 0x1L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static long saturatedMultiply(long a, long b) {
/*  693 */     int leadingZeros = Long.numberOfLeadingZeros(a) + Long.numberOfLeadingZeros(a ^ 0xFFFFFFFFFFFFFFFFL) + Long.numberOfLeadingZeros(b) + Long.numberOfLeadingZeros(b ^ 0xFFFFFFFFFFFFFFFFL);
/*  694 */     if (leadingZeros > 65) {
/*  695 */       return a * b;
/*      */     }
/*      */     
/*  698 */     long limit = Long.MAX_VALUE + ((a ^ b) >>> 63L);
/*  699 */     if ((((leadingZeros < 64) ? 1 : 0) | ((a < 0L) ? 1 : 0) & ((b == Long.MIN_VALUE) ? 1 : 0)) != 0)
/*      */     {
/*  701 */       return limit;
/*      */     }
/*  703 */     long result = a * b;
/*  704 */     if (a == 0L || result / a == b) {
/*  705 */       return result;
/*      */     }
/*  707 */     return limit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static long saturatedPow(long b, int k) {
/*  718 */     MathPreconditions.checkNonNegative("exponent", k);
/*  719 */     if ((((b >= -2L) ? 1 : 0) & ((b <= 2L) ? 1 : 0)) != 0) {
/*  720 */       switch ((int)b) {
/*      */         case 0:
/*  722 */           return (k == 0) ? 1L : 0L;
/*      */         case 1:
/*  724 */           return 1L;
/*      */         case -1:
/*  726 */           return ((k & 0x1) == 0) ? 1L : -1L;
/*      */         case 2:
/*  728 */           if (k >= 63) {
/*  729 */             return Long.MAX_VALUE;
/*      */           }
/*  731 */           return 1L << k;
/*      */         case -2:
/*  733 */           if (k >= 64) {
/*  734 */             return Long.MAX_VALUE + (k & 0x1);
/*      */           }
/*  736 */           return ((k & 0x1) == 0) ? (1L << k) : (-1L << k);
/*      */       } 
/*  738 */       throw new AssertionError();
/*      */     } 
/*      */     
/*  741 */     long accum = 1L;
/*      */     
/*  743 */     long limit = Long.MAX_VALUE + (b >>> 63L & (k & 0x1));
/*      */     while (true) {
/*  745 */       switch (k) {
/*      */         case 0:
/*  747 */           return accum;
/*      */         case 1:
/*  749 */           return saturatedMultiply(accum, b);
/*      */       } 
/*  751 */       if ((k & 0x1) != 0) {
/*  752 */         accum = saturatedMultiply(accum, b);
/*      */       }
/*  754 */       k >>= 1;
/*  755 */       if (k > 0) {
/*  756 */         if ((((-3037000499L > b) ? 1 : 0) | ((b > 3037000499L) ? 1 : 0)) != 0) {
/*  757 */           return limit;
/*      */         }
/*  759 */         b *= b;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static long factorial(int n) {
/*  775 */     MathPreconditions.checkNonNegative("n", n);
/*  776 */     return (n < factorials.length) ? factorials[n] : Long.MAX_VALUE;
/*      */   }
/*      */   
/*  779 */   static final long[] factorials = new long[] { 1L, 1L, 2L, 6L, 24L, 120L, 720L, 5040L, 40320L, 362880L, 3628800L, 39916800L, 479001600L, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long binomial(int n, int k) {
/*  810 */     MathPreconditions.checkNonNegative("n", n);
/*  811 */     MathPreconditions.checkNonNegative("k", k);
/*  812 */     Preconditions.checkArgument((k <= n), "k (%s) > n (%s)", k, n);
/*  813 */     if (k > n >> 1) {
/*  814 */       k = n - k;
/*      */     }
/*  816 */     switch (k) {
/*      */       case 0:
/*  818 */         return 1L;
/*      */       case 1:
/*  820 */         return n;
/*      */     } 
/*  822 */     if (n < factorials.length)
/*  823 */       return factorials[n] / factorials[k] * factorials[n - k]; 
/*  824 */     if (k >= biggestBinomials.length || n > biggestBinomials[k])
/*  825 */       return Long.MAX_VALUE; 
/*  826 */     if (k < biggestSimpleBinomials.length && n <= biggestSimpleBinomials[k]) {
/*      */       
/*  828 */       long l = n--;
/*  829 */       for (int j = 2; j <= k; n--, j++) {
/*  830 */         l *= n;
/*  831 */         l /= j;
/*      */       } 
/*  833 */       return l;
/*      */     } 
/*  835 */     int nBits = log2(n, RoundingMode.CEILING);
/*      */     
/*  837 */     long result = 1L;
/*  838 */     long numerator = n--;
/*  839 */     long denominator = 1L;
/*      */     
/*  841 */     int numeratorBits = nBits;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  849 */     for (int i = 2; i <= k; i++, n--) {
/*  850 */       if (numeratorBits + nBits < 63) {
/*      */         
/*  852 */         numerator *= n;
/*  853 */         denominator *= i;
/*  854 */         numeratorBits += nBits;
/*      */       }
/*      */       else {
/*      */         
/*  858 */         result = multiplyFraction(result, numerator, denominator);
/*  859 */         numerator = n;
/*  860 */         denominator = i;
/*  861 */         numeratorBits = nBits;
/*      */       } 
/*      */     } 
/*  864 */     return multiplyFraction(result, numerator, denominator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static long multiplyFraction(long x, long numerator, long denominator) {
/*  871 */     if (x == 1L) {
/*  872 */       return numerator / denominator;
/*      */     }
/*  874 */     long commonDivisor = gcd(x, denominator);
/*  875 */     x /= commonDivisor;
/*  876 */     denominator /= commonDivisor;
/*      */ 
/*      */     
/*  879 */     return x * numerator / denominator;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  885 */   static final int[] biggestBinomials = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 3810779, 121977, 16175, 4337, 1733, 887, 534, 361, 265, 206, 169, 143, 125, 111, 101, 94, 88, 83, 79, 76, 74, 72, 70, 69, 68, 67, 67, 66, 66, 66, 66 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*  927 */   static final int[] biggestSimpleBinomials = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 2642246, 86251, 11724, 3218, 1313, 684, 419, 287, 214, 169, 139, 119, 105, 95, 87, 81, 76, 73, 70, 68, 66, 64, 63, 62, 62, 61, 61, 61 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int SIEVE_30 = -545925251;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean fitsInInt(long x) {
/*  964 */     return ((int)x == x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long mean(long x, long y) {
/*  977 */     return (x & y) + ((x ^ y) >> 1L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   @Beta
/*      */   public static boolean isPrime(long n) {
/* 1004 */     if (n < 2L) {
/* 1005 */       MathPreconditions.checkNonNegative("n", n);
/* 1006 */       return false;
/*      */     } 
/* 1008 */     if (n < 66L) {
/*      */       
/* 1010 */       long mask = 722865708377213483L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1030 */       return ((mask >> (int)n - 2 & 0x1L) != 0L);
/*      */     } 
/*      */     
/* 1033 */     if ((0xDF75D77D & 1 << (int)(n % 30L)) != 0) {
/* 1034 */       return false;
/*      */     }
/* 1036 */     if (n % 7L == 0L || n % 11L == 0L || n % 13L == 0L) {
/* 1037 */       return false;
/*      */     }
/* 1039 */     if (n < 289L) {
/* 1040 */       return true;
/*      */     }
/*      */     
/* 1043 */     for (long[] baseSet : millerRabinBaseSets) {
/* 1044 */       if (n <= baseSet[0]) {
/* 1045 */         for (int i = 1; i < baseSet.length; i++) {
/* 1046 */           if (!MillerRabinTester.test(baseSet[i], n)) {
/* 1047 */             return false;
/*      */           }
/*      */         } 
/* 1050 */         return true;
/*      */       } 
/*      */     } 
/* 1053 */     throw new AssertionError();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1063 */   private static final long[][] millerRabinBaseSets = new long[][] { { 291830L, 126401071349994536L }, { 885594168L, 725270293939359937L, 3569819667048198375L }, { 273919523040L, 15L, 7363882082L, 992620450144556L }, { 47636622961200L, 2L, 2570940L, 211991001L, 3749873356L }, { 7999252175582850L, 2L, 4130806001517L, 149795463772692060L, 186635894390467037L, 3967304179347715805L }, { 585226005592931976L, 2L, 123635709730000L, 9233062284813009L, 43835965440333360L, 761179012939631437L, 1263739024124850375L }, { Long.MAX_VALUE, 2L, 325L, 9375L, 28178L, 450775L, 9780504L, 1795265022L } };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private enum MillerRabinTester
/*      */   {
/* 1090 */     SMALL
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       long mulMod(long a, long b, long m)
/*      */       {
/* 1099 */         return a * b % m;
/*      */       }
/*      */ 
/*      */       
/*      */       long squareMod(long a, long m) {
/* 1104 */         return a * a % m;
/*      */       }
/*      */     },
/*      */     
/* 1108 */     LARGE
/*      */     {
/*      */       private long plusMod(long a, long b, long m) {
/* 1111 */         return (a >= m - b) ? (a + b - m) : (a + b);
/*      */       }
/*      */ 
/*      */       
/*      */       private long times2ToThe32Mod(long a, long m) {
/* 1116 */         int remainingPowersOf2 = 32;
/*      */         while (true) {
/* 1118 */           int shift = Math.min(remainingPowersOf2, Long.numberOfLeadingZeros(a));
/*      */ 
/*      */           
/* 1121 */           a = UnsignedLongs.remainder(a << shift, m);
/* 1122 */           remainingPowersOf2 -= shift;
/* 1123 */           if (remainingPowersOf2 <= 0)
/* 1124 */             return a; 
/*      */         } 
/*      */       }
/*      */       
/*      */       long mulMod(long a, long b, long m) {
/* 1129 */         long aHi = a >>> 32L;
/* 1130 */         long bHi = b >>> 32L;
/* 1131 */         long aLo = a & 0xFFFFFFFFL;
/* 1132 */         long bLo = b & 0xFFFFFFFFL;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1142 */         long result = times2ToThe32Mod(aHi * bHi, m);
/* 1143 */         result += aHi * bLo;
/* 1144 */         if (result < 0L) {
/* 1145 */           result = UnsignedLongs.remainder(result, m);
/*      */         }
/*      */         
/* 1148 */         result += aLo * bHi;
/* 1149 */         result = times2ToThe32Mod(result, m);
/* 1150 */         return plusMod(result, UnsignedLongs.remainder(aLo * bLo, m), m);
/*      */       }
/*      */ 
/*      */       
/*      */       long squareMod(long a, long m) {
/* 1155 */         long aHi = a >>> 32L;
/* 1156 */         long aLo = a & 0xFFFFFFFFL;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1165 */         long result = times2ToThe32Mod(aHi * aHi, m);
/* 1166 */         long hiLo = aHi * aLo * 2L;
/* 1167 */         if (hiLo < 0L) {
/* 1168 */           hiLo = UnsignedLongs.remainder(hiLo, m);
/*      */         }
/*      */         
/* 1171 */         result += hiLo;
/* 1172 */         result = times2ToThe32Mod(result, m);
/* 1173 */         return plusMod(result, UnsignedLongs.remainder(aLo * aLo, m), m);
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */     
/*      */     static boolean test(long base, long n) {
/* 1180 */       return ((n <= 3037000499L) ? SMALL : LARGE).testWitness(base, n);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private long powMod(long a, long p, long m) {
/* 1191 */       long res = 1L;
/* 1192 */       for (; p != 0L; p >>= 1L) {
/* 1193 */         if ((p & 0x1L) != 0L) {
/* 1194 */           res = mulMod(res, a, m);
/*      */         }
/* 1196 */         a = squareMod(a, m);
/*      */       } 
/* 1198 */       return res;
/*      */     }
/*      */ 
/*      */     
/*      */     private boolean testWitness(long base, long n) {
/* 1203 */       int r = Long.numberOfTrailingZeros(n - 1L);
/* 1204 */       long d = n - 1L >> r;
/* 1205 */       base %= n;
/* 1206 */       if (base == 0L) {
/* 1207 */         return true;
/*      */       }
/*      */       
/* 1210 */       long a = powMod(base, d, n);
/*      */ 
/*      */ 
/*      */       
/* 1214 */       if (a == 1L) {
/* 1215 */         return true;
/*      */       }
/* 1217 */       int j = 0;
/* 1218 */       while (a != n - 1L) {
/* 1219 */         if (++j == r) {
/* 1220 */           return false;
/*      */         }
/* 1222 */         a = squareMod(a, n);
/*      */       } 
/* 1224 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract long mulMod(long param1Long1, long param1Long2, long param1Long3);
/*      */ 
/*      */ 
/*      */     
/*      */     abstract long squareMod(long param1Long1, long param1Long2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static double roundToDouble(long x, RoundingMode mode) {
/*      */     int cmpXToRoundArbitrarily;
/*      */     long roundFloor;
/*      */     double roundFloorAsDouble;
/*      */     long roundCeiling;
/*      */     double roundCeilingAsDouble;
/*      */     long deltaToFloor, deltaToCeiling;
/*      */     int diff;
/* 1248 */     double roundArbitrarily = x;
/* 1249 */     long roundArbitrarilyAsLong = (long)roundArbitrarily;
/*      */ 
/*      */     
/* 1252 */     if (roundArbitrarilyAsLong == Long.MAX_VALUE) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1263 */       cmpXToRoundArbitrarily = -1;
/*      */     } else {
/* 1265 */       cmpXToRoundArbitrarily = Longs.compare(x, roundArbitrarilyAsLong);
/*      */     } 
/*      */     
/* 1268 */     switch (mode) {
/*      */       case UNNECESSARY:
/* 1270 */         MathPreconditions.checkRoundingUnnecessary((cmpXToRoundArbitrarily == 0));
/* 1271 */         return roundArbitrarily;
/*      */       case FLOOR:
/* 1273 */         return (cmpXToRoundArbitrarily >= 0) ? 
/* 1274 */           roundArbitrarily : 
/* 1275 */           DoubleUtils.nextDown(roundArbitrarily);
/*      */       case CEILING:
/* 1277 */         return (cmpXToRoundArbitrarily <= 0) ? roundArbitrarily : Math.nextUp(roundArbitrarily);
/*      */       case DOWN:
/* 1279 */         if (x >= 0L) {
/* 1280 */           return (cmpXToRoundArbitrarily >= 0) ? 
/* 1281 */             roundArbitrarily : 
/* 1282 */             DoubleUtils.nextDown(roundArbitrarily);
/*      */         }
/* 1284 */         return (cmpXToRoundArbitrarily <= 0) ? roundArbitrarily : Math.nextUp(roundArbitrarily);
/*      */       
/*      */       case UP:
/* 1287 */         if (x >= 0L) {
/* 1288 */           return (cmpXToRoundArbitrarily <= 0) ? roundArbitrarily : Math.nextUp(roundArbitrarily);
/*      */         }
/* 1290 */         return (cmpXToRoundArbitrarily >= 0) ? 
/* 1291 */           roundArbitrarily : 
/* 1292 */           DoubleUtils.nextDown(roundArbitrarily);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case HALF_DOWN:
/*      */       case HALF_UP:
/*      */       case HALF_EVEN:
/* 1303 */         if (cmpXToRoundArbitrarily >= 0) {
/* 1304 */           roundFloorAsDouble = roundArbitrarily;
/* 1305 */           roundFloor = roundArbitrarilyAsLong;
/* 1306 */           roundCeilingAsDouble = Math.nextUp(roundArbitrarily);
/* 1307 */           roundCeiling = (long)Math.ceil(roundCeilingAsDouble);
/*      */         } else {
/* 1309 */           roundCeilingAsDouble = roundArbitrarily;
/* 1310 */           roundCeiling = roundArbitrarilyAsLong;
/* 1311 */           roundFloorAsDouble = DoubleUtils.nextDown(roundArbitrarily);
/* 1312 */           roundFloor = (long)Math.floor(roundFloorAsDouble);
/*      */         } 
/*      */         
/* 1315 */         deltaToFloor = x - roundFloor;
/* 1316 */         deltaToCeiling = roundCeiling - x;
/*      */         
/* 1318 */         if (roundCeiling == Long.MAX_VALUE)
/*      */         {
/*      */           
/* 1321 */           deltaToCeiling++;
/*      */         }
/*      */         
/* 1324 */         diff = Longs.compare(deltaToFloor, deltaToCeiling);
/* 1325 */         if (diff < 0)
/* 1326 */           return roundFloorAsDouble; 
/* 1327 */         if (diff > 0) {
/* 1328 */           return roundCeilingAsDouble;
/*      */         }
/*      */         
/* 1331 */         switch (mode) {
/*      */           case HALF_EVEN:
/* 1333 */             return ((DoubleUtils.getSignificand(roundFloorAsDouble) & 0x1L) == 0L) ? 
/* 1334 */               roundFloorAsDouble : 
/* 1335 */               roundCeilingAsDouble;
/*      */           case HALF_DOWN:
/* 1337 */             return (x >= 0L) ? roundFloorAsDouble : roundCeilingAsDouble;
/*      */           case HALF_UP:
/* 1339 */             return (x >= 0L) ? roundCeilingAsDouble : roundFloorAsDouble;
/*      */         } 
/* 1341 */         throw new AssertionError("impossible");
/*      */     } 
/*      */ 
/*      */     
/* 1345 */     throw new AssertionError("impossible");
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\math\LongMath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */