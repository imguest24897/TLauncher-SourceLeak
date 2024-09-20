/*     */ package org.apache.commons.lang3.math;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.util.Objects;
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
/*     */ public final class Fraction
/*     */   extends Number
/*     */   implements Comparable<Fraction>
/*     */ {
/*     */   private static final long serialVersionUID = 65382027393090L;
/*  47 */   public static final Fraction ZERO = new Fraction(0, 1);
/*     */ 
/*     */ 
/*     */   
/*  51 */   public static final Fraction ONE = new Fraction(1, 1);
/*     */ 
/*     */ 
/*     */   
/*  55 */   public static final Fraction ONE_HALF = new Fraction(1, 2);
/*     */ 
/*     */ 
/*     */   
/*  59 */   public static final Fraction ONE_THIRD = new Fraction(1, 3);
/*     */ 
/*     */ 
/*     */   
/*  63 */   public static final Fraction TWO_THIRDS = new Fraction(2, 3);
/*     */ 
/*     */ 
/*     */   
/*  67 */   public static final Fraction ONE_QUARTER = new Fraction(1, 4);
/*     */ 
/*     */ 
/*     */   
/*  71 */   public static final Fraction TWO_QUARTERS = new Fraction(2, 4);
/*     */ 
/*     */ 
/*     */   
/*  75 */   public static final Fraction THREE_QUARTERS = new Fraction(3, 4);
/*     */ 
/*     */ 
/*     */   
/*  79 */   public static final Fraction ONE_FIFTH = new Fraction(1, 5);
/*     */ 
/*     */ 
/*     */   
/*  83 */   public static final Fraction TWO_FIFTHS = new Fraction(2, 5);
/*     */ 
/*     */ 
/*     */   
/*  87 */   public static final Fraction THREE_FIFTHS = new Fraction(3, 5);
/*     */ 
/*     */ 
/*     */   
/*  91 */   public static final Fraction FOUR_FIFTHS = new Fraction(4, 5);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int numerator;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int denominator;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient int hashCode;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient String toString;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient String toProperString;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Fraction(int numerator, int denominator) {
/* 124 */     this.numerator = numerator;
/* 125 */     this.denominator = denominator;
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
/*     */   public static Fraction getFraction(int numerator, int denominator) {
/* 141 */     if (denominator == 0) {
/* 142 */       throw new ArithmeticException("The denominator must not be zero");
/*     */     }
/* 144 */     if (denominator < 0) {
/* 145 */       if (numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE) {
/* 146 */         throw new ArithmeticException("overflow: can't negate");
/*     */       }
/* 148 */       numerator = -numerator;
/* 149 */       denominator = -denominator;
/*     */     } 
/* 151 */     return new Fraction(numerator, denominator);
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
/*     */   public static Fraction getFraction(int whole, int numerator, int denominator) {
/*     */     long numeratorValue;
/* 171 */     if (denominator == 0) {
/* 172 */       throw new ArithmeticException("The denominator must not be zero");
/*     */     }
/* 174 */     if (denominator < 0) {
/* 175 */       throw new ArithmeticException("The denominator must not be negative");
/*     */     }
/* 177 */     if (numerator < 0) {
/* 178 */       throw new ArithmeticException("The numerator must not be negative");
/*     */     }
/*     */     
/* 181 */     if (whole < 0) {
/* 182 */       numeratorValue = whole * denominator - numerator;
/*     */     } else {
/* 184 */       numeratorValue = whole * denominator + numerator;
/*     */     } 
/* 186 */     if (numeratorValue < -2147483648L || numeratorValue > 2147483647L) {
/* 187 */       throw new ArithmeticException("Numerator too large to represent as an Integer.");
/*     */     }
/* 189 */     return new Fraction((int)numeratorValue, denominator);
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
/*     */   public static Fraction getReducedFraction(int numerator, int denominator) {
/* 207 */     if (denominator == 0) {
/* 208 */       throw new ArithmeticException("The denominator must not be zero");
/*     */     }
/* 210 */     if (numerator == 0) {
/* 211 */       return ZERO;
/*     */     }
/*     */     
/* 214 */     if (denominator == Integer.MIN_VALUE && (numerator & 0x1) == 0) {
/* 215 */       numerator /= 2;
/* 216 */       denominator /= 2;
/*     */     } 
/* 218 */     if (denominator < 0) {
/* 219 */       if (numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE) {
/* 220 */         throw new ArithmeticException("overflow: can't negate");
/*     */       }
/* 222 */       numerator = -numerator;
/* 223 */       denominator = -denominator;
/*     */     } 
/*     */     
/* 226 */     int gcd = greatestCommonDivisor(numerator, denominator);
/* 227 */     numerator /= gcd;
/* 228 */     denominator /= gcd;
/* 229 */     return new Fraction(numerator, denominator);
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
/*     */   public static Fraction getFraction(double value) {
/*     */     int denom2;
/*     */     double delta1;
/* 247 */     int sign = (value < 0.0D) ? -1 : 1;
/* 248 */     value = Math.abs(value);
/* 249 */     if (value > 2.147483647E9D || Double.isNaN(value)) {
/* 250 */       throw new ArithmeticException("The value must not be greater than Integer.MAX_VALUE or NaN");
/*     */     }
/* 252 */     int wholeNumber = (int)value;
/* 253 */     value -= wholeNumber;
/*     */     
/* 255 */     int numer0 = 0;
/* 256 */     int denom0 = 1;
/* 257 */     int numer1 = 1;
/* 258 */     int denom1 = 0;
/*     */ 
/*     */     
/* 261 */     int a1 = (int)value;
/*     */     
/* 263 */     double x1 = 1.0D;
/*     */     
/* 265 */     double y1 = value - a1;
/*     */     
/* 267 */     double delta2 = Double.MAX_VALUE;
/*     */     
/* 269 */     int i = 1;
/*     */     do {
/* 271 */       delta1 = delta2;
/* 272 */       int a2 = (int)(x1 / y1);
/* 273 */       double x2 = y1;
/* 274 */       double y2 = x1 - a2 * y1;
/* 275 */       int numer2 = a1 * numer1 + numer0;
/* 276 */       denom2 = a1 * denom1 + denom0;
/* 277 */       double fraction = numer2 / denom2;
/* 278 */       delta2 = Math.abs(value - fraction);
/* 279 */       a1 = a2;
/* 280 */       x1 = x2;
/* 281 */       y1 = y2;
/* 282 */       numer0 = numer1;
/* 283 */       denom0 = denom1;
/* 284 */       numer1 = numer2;
/* 285 */       denom1 = denom2;
/* 286 */       i++;
/* 287 */     } while (delta1 > delta2 && denom2 <= 10000 && denom2 > 0 && i < 25);
/* 288 */     if (i == 25) {
/* 289 */       throw new ArithmeticException("Unable to convert double to fraction");
/*     */     }
/* 291 */     return getReducedFraction((numer0 + wholeNumber * denom0) * sign, denom0);
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
/*     */   public static Fraction getFraction(String str) {
/* 313 */     Objects.requireNonNull(str, "str");
/*     */     
/* 315 */     int pos = str.indexOf('.');
/* 316 */     if (pos >= 0) {
/* 317 */       return getFraction(Double.parseDouble(str));
/*     */     }
/*     */ 
/*     */     
/* 321 */     pos = str.indexOf(' ');
/* 322 */     if (pos > 0) {
/* 323 */       int whole = Integer.parseInt(str.substring(0, pos));
/* 324 */       str = str.substring(pos + 1);
/* 325 */       pos = str.indexOf('/');
/* 326 */       if (pos < 0) {
/* 327 */         throw new NumberFormatException("The fraction could not be parsed as the format X Y/Z");
/*     */       }
/* 329 */       int i = Integer.parseInt(str.substring(0, pos));
/* 330 */       int j = Integer.parseInt(str.substring(pos + 1));
/* 331 */       return getFraction(whole, i, j);
/*     */     } 
/*     */ 
/*     */     
/* 335 */     pos = str.indexOf('/');
/* 336 */     if (pos < 0)
/*     */     {
/* 338 */       return getFraction(Integer.parseInt(str), 1);
/*     */     }
/* 340 */     int numer = Integer.parseInt(str.substring(0, pos));
/* 341 */     int denom = Integer.parseInt(str.substring(pos + 1));
/* 342 */     return getFraction(numer, denom);
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
/*     */   public int getNumerator() {
/* 354 */     return this.numerator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDenominator() {
/* 363 */     return this.denominator;
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
/*     */   public int getProperNumerator() {
/* 378 */     return Math.abs(this.numerator % this.denominator);
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
/*     */   public int getProperWhole() {
/* 393 */     return this.numerator / this.denominator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int intValue() {
/* 404 */     return this.numerator / this.denominator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long longValue() {
/* 415 */     return this.numerator / this.denominator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float floatValue() {
/* 426 */     return this.numerator / this.denominator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double doubleValue() {
/* 437 */     return this.numerator / this.denominator;
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
/*     */   public Fraction reduce() {
/* 450 */     if (this.numerator == 0) {
/* 451 */       return equals(ZERO) ? this : ZERO;
/*     */     }
/* 453 */     int gcd = greatestCommonDivisor(Math.abs(this.numerator), this.denominator);
/* 454 */     if (gcd == 1) {
/* 455 */       return this;
/*     */     }
/* 457 */     return getFraction(this.numerator / gcd, this.denominator / gcd);
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
/*     */   public Fraction invert() {
/* 470 */     if (this.numerator == 0) {
/* 471 */       throw new ArithmeticException("Unable to invert zero.");
/*     */     }
/* 473 */     if (this.numerator == Integer.MIN_VALUE) {
/* 474 */       throw new ArithmeticException("overflow: can't negate numerator");
/*     */     }
/* 476 */     if (this.numerator < 0) {
/* 477 */       return new Fraction(-this.denominator, -this.numerator);
/*     */     }
/* 479 */     return new Fraction(this.denominator, this.numerator);
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
/*     */   public Fraction negate() {
/* 491 */     if (this.numerator == Integer.MIN_VALUE) {
/* 492 */       throw new ArithmeticException("overflow: too large to negate");
/*     */     }
/* 494 */     return new Fraction(-this.numerator, this.denominator);
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
/*     */   public Fraction abs() {
/* 507 */     if (this.numerator >= 0) {
/* 508 */       return this;
/*     */     }
/* 510 */     return negate();
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
/*     */   public Fraction pow(int power) {
/* 526 */     if (power == 1) {
/* 527 */       return this;
/*     */     }
/* 529 */     if (power == 0) {
/* 530 */       return ONE;
/*     */     }
/* 532 */     if (power < 0) {
/* 533 */       if (power == Integer.MIN_VALUE) {
/* 534 */         return invert().pow(2).pow(-(power / 2));
/*     */       }
/* 536 */       return invert().pow(-power);
/*     */     } 
/* 538 */     Fraction f = multiplyBy(this);
/* 539 */     if (power % 2 == 0) {
/* 540 */       return f.pow(power / 2);
/*     */     }
/* 542 */     return f.pow(power / 2).multiplyBy(this);
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
/*     */   private static int greatestCommonDivisor(int u, int v) {
/* 557 */     if (u == 0 || v == 0) {
/* 558 */       if (u == Integer.MIN_VALUE || v == Integer.MIN_VALUE) {
/* 559 */         throw new ArithmeticException("overflow: gcd is 2^31");
/*     */       }
/* 561 */       return Math.abs(u) + Math.abs(v);
/*     */     } 
/*     */     
/* 564 */     if (Math.abs(u) == 1 || Math.abs(v) == 1) {
/* 565 */       return 1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 571 */     if (u > 0) {
/* 572 */       u = -u;
/*     */     }
/* 574 */     if (v > 0) {
/* 575 */       v = -v;
/*     */     }
/*     */     
/* 578 */     int k = 0;
/* 579 */     while ((u & 0x1) == 0 && (v & 0x1) == 0 && k < 31) {
/* 580 */       u /= 2;
/* 581 */       v /= 2;
/* 582 */       k++;
/*     */     } 
/* 584 */     if (k == 31) {
/* 585 */       throw new ArithmeticException("overflow: gcd is 2^31");
/*     */     }
/*     */ 
/*     */     
/* 589 */     int t = ((u & 0x1) == 1) ? v : -(u / 2);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 595 */       while ((t & 0x1) == 0) {
/* 596 */         t /= 2;
/*     */       }
/*     */       
/* 599 */       if (t > 0) {
/* 600 */         u = -t;
/*     */       } else {
/* 602 */         v = t;
/*     */       } 
/*     */       
/* 605 */       t = (v - u) / 2;
/*     */ 
/*     */       
/* 608 */       if (t == 0) {
/* 609 */         return -u * (1 << k);
/*     */       }
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
/*     */   private static int mulAndCheck(int x, int y) {
/* 622 */     long m = x * y;
/* 623 */     if (m < -2147483648L || m > 2147483647L) {
/* 624 */       throw new ArithmeticException("overflow: mul");
/*     */     }
/* 626 */     return (int)m;
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
/*     */   private static int mulPosAndCheck(int x, int y) {
/* 640 */     long m = x * y;
/* 641 */     if (m > 2147483647L) {
/* 642 */       throw new ArithmeticException("overflow: mulPos");
/*     */     }
/* 644 */     return (int)m;
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
/*     */   private static int addAndCheck(int x, int y) {
/* 657 */     long s = x + y;
/* 658 */     if (s < -2147483648L || s > 2147483647L) {
/* 659 */       throw new ArithmeticException("overflow: add");
/*     */     }
/* 661 */     return (int)s;
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
/*     */   private static int subAndCheck(int x, int y) {
/* 674 */     long s = x - y;
/* 675 */     if (s < -2147483648L || s > 2147483647L) {
/* 676 */       throw new ArithmeticException("overflow: add");
/*     */     }
/* 678 */     return (int)s;
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
/*     */   public Fraction add(Fraction fraction) {
/* 692 */     return addSub(fraction, true);
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
/*     */   public Fraction subtract(Fraction fraction) {
/* 706 */     return addSub(fraction, false);
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
/*     */   private Fraction addSub(Fraction fraction, boolean isAdd) {
/* 720 */     Objects.requireNonNull(fraction, "fraction");
/*     */     
/* 722 */     if (this.numerator == 0) {
/* 723 */       return isAdd ? fraction : fraction.negate();
/*     */     }
/* 725 */     if (fraction.numerator == 0) {
/* 726 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 730 */     int d1 = greatestCommonDivisor(this.denominator, fraction.denominator);
/* 731 */     if (d1 == 1) {
/*     */       
/* 733 */       int i = mulAndCheck(this.numerator, fraction.denominator);
/* 734 */       int j = mulAndCheck(fraction.numerator, this.denominator);
/* 735 */       return new Fraction(isAdd ? addAndCheck(i, j) : subAndCheck(i, j), mulPosAndCheck(this.denominator, fraction.denominator));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 741 */     BigInteger uvp = BigInteger.valueOf(this.numerator).multiply(BigInteger.valueOf((fraction.denominator / d1)));
/* 742 */     BigInteger upv = BigInteger.valueOf(fraction.numerator).multiply(BigInteger.valueOf((this.denominator / d1)));
/* 743 */     BigInteger t = isAdd ? uvp.add(upv) : uvp.subtract(upv);
/*     */ 
/*     */     
/* 746 */     int tmodd1 = t.mod(BigInteger.valueOf(d1)).intValue();
/* 747 */     int d2 = (tmodd1 == 0) ? d1 : greatestCommonDivisor(tmodd1, d1);
/*     */ 
/*     */     
/* 750 */     BigInteger w = t.divide(BigInteger.valueOf(d2));
/* 751 */     if (w.bitLength() > 31) {
/* 752 */       throw new ArithmeticException("overflow: numerator too large after multiply");
/*     */     }
/* 754 */     return new Fraction(w.intValue(), mulPosAndCheck(this.denominator / d1, fraction.denominator / d2));
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
/*     */   public Fraction multiplyBy(Fraction fraction) {
/* 768 */     Objects.requireNonNull(fraction, "fraction");
/* 769 */     if (this.numerator == 0 || fraction.numerator == 0) {
/* 770 */       return ZERO;
/*     */     }
/*     */ 
/*     */     
/* 774 */     int d1 = greatestCommonDivisor(this.numerator, fraction.denominator);
/* 775 */     int d2 = greatestCommonDivisor(fraction.numerator, this.denominator);
/* 776 */     return getReducedFraction(mulAndCheck(this.numerator / d1, fraction.numerator / d2), 
/* 777 */         mulPosAndCheck(this.denominator / d2, fraction.denominator / d1));
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
/*     */   public Fraction divideBy(Fraction fraction) {
/* 791 */     Objects.requireNonNull(fraction, "fraction");
/* 792 */     if (fraction.numerator == 0) {
/* 793 */       throw new ArithmeticException("The fraction to divide by must not be zero");
/*     */     }
/* 795 */     return multiplyBy(fraction.invert());
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
/*     */   public boolean equals(Object obj) {
/* 808 */     if (obj == this) {
/* 809 */       return true;
/*     */     }
/* 811 */     if (!(obj instanceof Fraction)) {
/* 812 */       return false;
/*     */     }
/* 814 */     Fraction other = (Fraction)obj;
/* 815 */     return (getNumerator() == other.getNumerator() && getDenominator() == other.getDenominator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 825 */     if (this.hashCode == 0)
/*     */     {
/* 827 */       this.hashCode = 37 * (629 + getNumerator()) + getDenominator();
/*     */     }
/* 829 */     return this.hashCode;
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
/*     */   public int compareTo(Fraction other) {
/* 846 */     if (this == other) {
/* 847 */       return 0;
/*     */     }
/* 849 */     if (this.numerator == other.numerator && this.denominator == other.denominator) {
/* 850 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 854 */     long first = this.numerator * other.denominator;
/* 855 */     long second = other.numerator * this.denominator;
/* 856 */     return Long.compare(first, second);
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
/*     */   public String toString() {
/* 868 */     if (this.toString == null) {
/* 869 */       this.toString = getNumerator() + "/" + getDenominator();
/*     */     }
/* 871 */     return this.toString;
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
/*     */   public String toProperString() {
/* 884 */     if (this.toProperString == null) {
/* 885 */       if (this.numerator == 0) {
/* 886 */         this.toProperString = "0";
/* 887 */       } else if (this.numerator == this.denominator) {
/* 888 */         this.toProperString = "1";
/* 889 */       } else if (this.numerator == -1 * this.denominator) {
/* 890 */         this.toProperString = "-1";
/* 891 */       } else if (((this.numerator > 0) ? -this.numerator : this.numerator) < -this.denominator) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 896 */         int properNumerator = getProperNumerator();
/* 897 */         if (properNumerator == 0) {
/* 898 */           this.toProperString = Integer.toString(getProperWhole());
/*     */         } else {
/* 900 */           this.toProperString = getProperWhole() + " " + properNumerator + "/" + getDenominator();
/*     */         } 
/*     */       } else {
/* 903 */         this.toProperString = getNumerator() + "/" + getDenominator();
/*     */       } 
/*     */     }
/* 906 */     return this.toProperString;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\math\Fraction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */