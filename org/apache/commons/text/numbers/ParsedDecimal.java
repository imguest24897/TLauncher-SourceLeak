/*     */ package org.apache.commons.text.numbers;
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
/*     */ final class ParsedDecimal
/*     */ {
/*     */   private static final char MINUS_CHAR = '-';
/*     */   private static final char DECIMAL_SEP_CHAR = '.';
/*     */   private static final char EXPONENT_CHAR = 'E';
/*     */   private static final char ZERO_CHAR = '0';
/*     */   private static final int THOUSANDS_GROUP_SIZE = 3;
/*     */   private static final int DECIMAL_RADIX = 10;
/*     */   private static final int ROUND_CENTER = 5;
/*     */   private static final int ENG_EXPONENT_MOD = 3;
/*     */   final boolean negative;
/*     */   final int[] digits;
/*     */   int digitCount;
/*     */   int exponent;
/*     */   private char[] outputChars;
/*     */   private int outputIdx;
/*     */   
/*     */   private static int digitValue(char ch) {
/* 135 */     return ch - 48;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ParsedDecimal from(double d) {
/* 145 */     if (!Double.isFinite(d)) {
/* 146 */       throw new IllegalArgumentException("Double is not finite");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 156 */     char[] strChars = Double.toString(d).toCharArray();
/*     */     
/* 158 */     boolean negative = (strChars[0] == '-');
/* 159 */     int digitStartIdx = negative ? 1 : 0;
/*     */     
/* 161 */     int[] digits = new int[strChars.length - digitStartIdx - 1];
/*     */     
/* 163 */     boolean foundDecimalPoint = false;
/* 164 */     int digitCount = 0;
/* 165 */     int significantDigitCount = 0;
/* 166 */     int decimalPos = 0;
/*     */     
/*     */     int i;
/* 169 */     for (i = digitStartIdx; i < strChars.length; i++) {
/* 170 */       char ch = strChars[i];
/*     */       
/* 172 */       if (ch == '.')
/* 173 */       { foundDecimalPoint = true;
/* 174 */         decimalPos = digitCount; }
/* 175 */       else { if (ch == 'E') {
/*     */           break;
/*     */         }
/* 178 */         if (ch != '0' || digitCount > 0) {
/*     */           
/* 180 */           int val = digitValue(ch);
/* 181 */           digits[digitCount++] = val;
/*     */           
/* 183 */           if (val > 0) {
/* 184 */             significantDigitCount = digitCount;
/*     */           }
/* 186 */         } else if (foundDecimalPoint) {
/*     */           
/* 188 */           decimalPos--;
/*     */         }  }
/*     */     
/*     */     } 
/* 192 */     if (digitCount > 0) {
/*     */ 
/*     */ 
/*     */       
/* 196 */       int explicitExponent = (i < strChars.length) ? parseExponent(strChars, i + 1) : 0;
/* 197 */       int exponent = explicitExponent + decimalPos - significantDigitCount;
/*     */       
/* 199 */       return new ParsedDecimal(negative, digits, significantDigitCount, exponent);
/*     */     } 
/*     */ 
/*     */     
/* 203 */     return new ParsedDecimal(negative, new int[] { 0 }, 1, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int parseExponent(char[] chars, int start) {
/* 214 */     int i = start;
/* 215 */     boolean neg = (chars[i] == '-');
/* 216 */     if (neg) {
/* 217 */       i++;
/*     */     }
/*     */     
/* 220 */     int exp = 0;
/* 221 */     for (; i < chars.length; i++) {
/* 222 */       exp = exp * 10 + digitValue(chars[i]);
/*     */     }
/*     */     
/* 225 */     return neg ? -exp : exp;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ParsedDecimal(boolean negative, int[] digits, int digitCount, int exponent) {
/* 255 */     this.negative = negative;
/* 256 */     this.digits = digits;
/* 257 */     this.digitCount = digitCount;
/* 258 */     this.exponent = exponent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void append(char ch) {
/* 266 */     this.outputChars[this.outputIdx++] = ch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void append(char[] chars) {
/* 274 */     for (char c : chars) {
/* 275 */       append(c);
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
/*     */   private void appendFraction(int zeroCount, int startIdx, FormatOptions opts) {
/* 287 */     char[] localizedDigits = opts.getDigits();
/* 288 */     char localizedZero = localizedDigits[0];
/*     */     
/* 290 */     if (startIdx < this.digitCount) {
/* 291 */       append(opts.getDecimalSeparator());
/*     */       
/*     */       int i;
/* 294 */       for (i = 0; i < zeroCount; i++) {
/* 295 */         append(localizedZero);
/*     */       }
/*     */ 
/*     */       
/* 299 */       for (i = startIdx; i < this.digitCount; i++) {
/* 300 */         appendLocalizedDigit(this.digits[i], localizedDigits);
/*     */       }
/* 302 */     } else if (opts.isIncludeFractionPlaceholder()) {
/* 303 */       append(opts.getDecimalSeparator());
/* 304 */       append(localizedZero);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendLocalizedDigit(int n, char[] digitChars) {
/* 315 */     append(digitChars[n]);
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
/*     */   private int appendWhole(int wholeCount, FormatOptions opts) {
/* 327 */     if (shouldIncludeMinus(opts)) {
/* 328 */       append(opts.getMinusSign());
/*     */     }
/*     */     
/* 331 */     char[] localizedDigits = opts.getDigits();
/* 332 */     char localizedZero = localizedDigits[0];
/*     */     
/* 334 */     int significantDigitCount = Math.max(0, Math.min(wholeCount, this.digitCount));
/*     */     
/* 336 */     if (significantDigitCount > 0) {
/*     */       int i;
/* 338 */       for (i = 0; i < significantDigitCount; i++) {
/* 339 */         appendLocalizedDigit(this.digits[i], localizedDigits);
/*     */       }
/*     */       
/* 342 */       for (; i < wholeCount; i++) {
/* 343 */         append(localizedZero);
/*     */       }
/*     */     } else {
/* 346 */       append(localizedZero);
/*     */     } 
/*     */     
/* 349 */     return significantDigitCount;
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
/*     */   private int appendWholeGrouped(int wholeCount, FormatOptions opts) {
/* 361 */     if (shouldIncludeMinus(opts)) {
/* 362 */       append(opts.getMinusSign());
/*     */     }
/*     */     
/* 365 */     char[] localizedDigits = opts.getDigits();
/* 366 */     char localizedZero = localizedDigits[0];
/* 367 */     char groupingChar = opts.getGroupingSeparator();
/*     */     
/* 369 */     int appendCount = Math.max(0, Math.min(wholeCount, this.digitCount));
/*     */     
/* 371 */     if (appendCount > 0) {
/*     */       
/* 373 */       int pos = wholeCount; int i;
/* 374 */       for (i = 0; i < appendCount; i++, pos--) {
/* 375 */         appendLocalizedDigit(this.digits[i], localizedDigits);
/* 376 */         if (requiresGroupingSeparatorAfterPosition(pos)) {
/* 377 */           append(groupingChar);
/*     */         }
/*     */       } 
/*     */       
/* 381 */       for (; i < wholeCount; i++, pos--) {
/* 382 */         append(localizedZero);
/* 383 */         if (requiresGroupingSeparatorAfterPosition(pos)) {
/* 384 */           append(groupingChar);
/*     */         }
/*     */       } 
/*     */     } else {
/* 388 */       append(localizedZero);
/*     */     } 
/*     */     
/* 391 */     return appendCount;
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
/*     */   private int getDigitStringSize(int decimalPos, FormatOptions opts) {
/* 403 */     int size = this.digitCount;
/* 404 */     if (shouldIncludeMinus(opts)) {
/* 405 */       size++;
/*     */     }
/* 407 */     if (decimalPos < 1) {
/*     */ 
/*     */       
/* 410 */       size += 2 + Math.abs(decimalPos);
/* 411 */     } else if (decimalPos >= this.digitCount) {
/*     */ 
/*     */       
/* 414 */       size += decimalPos - this.digitCount;
/* 415 */       if (opts.isIncludeFractionPlaceholder()) {
/* 416 */         size += 2;
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 421 */       size++;
/*     */     } 
/*     */     
/* 424 */     return size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getExponent() {
/* 433 */     return this.exponent;
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
/*     */   private int getPlainStringSize(int decimalPos, FormatOptions opts) {
/* 445 */     int size = getDigitStringSize(decimalPos, opts);
/*     */ 
/*     */     
/* 448 */     if (opts.isGroupThousands() && decimalPos > 0) {
/* 449 */       size += (decimalPos - 1) / 3;
/*     */     }
/*     */     
/* 452 */     return size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getScientificExponent() {
/* 462 */     return this.digitCount + this.exponent - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isZero() {
/* 471 */     return (this.digits[0] == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void maxPrecision(int precision) {
/* 482 */     if (precision > 0 && precision < this.digitCount) {
/* 483 */       if (shouldRoundUp(precision)) {
/* 484 */         roundUp(precision);
/*     */       } else {
/* 486 */         truncate(precision);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String outputString() {
/* 496 */     String str = String.valueOf(this.outputChars);
/* 497 */     this.outputChars = null;
/* 498 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void prepareOutput(int size) {
/* 506 */     this.outputChars = new char[size];
/* 507 */     this.outputIdx = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean requiresGroupingSeparatorAfterPosition(int pos) {
/* 518 */     return (pos > 1 && pos % 3 == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void round(int roundExponent) {
/* 528 */     if (roundExponent > this.exponent) {
/* 529 */       int max = this.digitCount + this.exponent;
/*     */       
/* 531 */       if (roundExponent < max) {
/*     */         
/* 533 */         maxPrecision(max - roundExponent);
/* 534 */       } else if (roundExponent == max && shouldRoundUp(0)) {
/*     */         
/* 536 */         setSingleDigitValue(1, roundExponent);
/*     */       } else {
/*     */         
/* 539 */         setSingleDigitValue(0, 0);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void roundUp(int count) {
/* 550 */     int removedDigits = this.digitCount - count;
/*     */     int i;
/* 552 */     for (i = count - 1; i >= 0; i--) {
/* 553 */       int d = this.digits[i] + 1;
/*     */       
/* 555 */       if (d < 10) {
/*     */         
/* 557 */         this.digits[i] = d;
/*     */         
/*     */         break;
/*     */       } 
/*     */       
/* 562 */       removedDigits++;
/*     */     } 
/*     */     
/* 565 */     if (i < 0) {
/*     */       
/* 567 */       setSingleDigitValue(1, this.exponent + removedDigits);
/*     */     } else {
/*     */       
/* 570 */       truncate(this.digitCount - removedDigits);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setSingleDigitValue(int digit, int newExponent) {
/* 581 */     this.digits[0] = digit;
/* 582 */     this.digitCount = 1;
/* 583 */     this.exponent = newExponent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean shouldIncludeExponent(int targetExponent, FormatOptions opts) {
/* 594 */     return (targetExponent != 0 || opts.isAlwaysIncludeExponent());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean shouldIncludeMinus(FormatOptions opts) {
/* 604 */     return (this.negative && (opts.isSignedZero() || !isZero()));
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
/*     */   private boolean shouldRoundUp(int count) {
/* 622 */     int digitAfterLast = this.digits[count];
/*     */     
/* 624 */     return (digitAfterLast > 5 || (digitAfterLast == 5 && (count < this.digitCount - 1 || this.digits[count - 1] % 2 != 0)));
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
/*     */   public String toEngineeringString(FormatOptions opts) {
/* 646 */     int decimalPos = 1 + Math.floorMod(getScientificExponent(), 3);
/* 647 */     return toScientificString(decimalPos, opts);
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
/*     */   public String toPlainString(FormatOptions opts) {
/* 666 */     int decimalPos = this.digitCount + this.exponent;
/*     */ 
/*     */     
/* 669 */     int fractionZeroCount = (decimalPos < 1) ? Math.abs(decimalPos) : 0;
/*     */     
/* 671 */     prepareOutput(getPlainStringSize(decimalPos, opts));
/*     */ 
/*     */ 
/*     */     
/* 675 */     int fractionStartIdx = opts.isGroupThousands() ? appendWholeGrouped(decimalPos, opts) : appendWhole(decimalPos, opts);
/*     */     
/* 677 */     appendFraction(fractionZeroCount, fractionStartIdx, opts);
/*     */     
/* 679 */     return outputString();
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
/*     */   public String toScientificString(FormatOptions opts) {
/* 699 */     return toScientificString(1, opts);
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
/*     */   private String toScientificString(int decimalPos, FormatOptions opts) {
/* 711 */     int targetExponent = this.digitCount + this.exponent - decimalPos;
/* 712 */     int absTargetExponent = Math.abs(targetExponent);
/* 713 */     boolean includeExponent = shouldIncludeExponent(targetExponent, opts);
/* 714 */     boolean negativeExponent = (targetExponent < 0);
/*     */ 
/*     */ 
/*     */     
/* 718 */     int size = getDigitStringSize(decimalPos, opts);
/* 719 */     int exponentDigitCount = 0;
/* 720 */     if (includeExponent) {
/*     */ 
/*     */       
/* 723 */       exponentDigitCount = (absTargetExponent > 0) ? ((int)Math.floor(Math.log10(absTargetExponent)) + 1) : 1;
/*     */       
/* 725 */       size += (opts.getExponentSeparatorChars()).length + exponentDigitCount;
/* 726 */       if (negativeExponent) {
/* 727 */         size++;
/*     */       }
/*     */     } 
/*     */     
/* 731 */     prepareOutput(size);
/*     */ 
/*     */     
/* 734 */     int fractionStartIdx = appendWhole(decimalPos, opts);
/* 735 */     appendFraction(0, fractionStartIdx, opts);
/*     */     
/* 737 */     if (includeExponent) {
/*     */       
/* 739 */       append(opts.getExponentSeparatorChars());
/*     */       
/* 741 */       if (negativeExponent) {
/* 742 */         append(opts.getMinusSign());
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 748 */       char[] localizedDigits = opts.getDigits();
/* 749 */       int rem = absTargetExponent;
/* 750 */       for (int i = size - 1; i >= this.outputIdx; i--) {
/* 751 */         this.outputChars[i] = localizedDigits[rem % 10];
/* 752 */         rem /= 10;
/*     */       } 
/* 754 */       this.outputIdx = size;
/*     */     } 
/*     */     
/* 757 */     return outputString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void truncate(int count) {
/* 768 */     int nonZeroCount = count;
/* 769 */     int i = count - 1;
/* 770 */     for (; i > 0 && this.digits[i] == 0; 
/* 771 */       i--) {
/* 772 */       nonZeroCount--;
/*     */     }
/* 774 */     this.exponent += this.digitCount - nonZeroCount;
/* 775 */     this.digitCount = nonZeroCount;
/*     */   }
/*     */   
/*     */   static interface FormatOptions {
/*     */     char getDecimalSeparator();
/*     */     
/*     */     char[] getDigits();
/*     */     
/*     */     char[] getExponentSeparatorChars();
/*     */     
/*     */     char getGroupingSeparator();
/*     */     
/*     */     char getMinusSign();
/*     */     
/*     */     boolean isAlwaysIncludeExponent();
/*     */     
/*     */     boolean isGroupThousands();
/*     */     
/*     */     boolean isIncludeFractionPlaceholder();
/*     */     
/*     */     boolean isSignedZero();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\numbers\ParsedDecimal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */