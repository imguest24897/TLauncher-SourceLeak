/*      */ package org.apache.commons.lang3.math;
/*      */ 
/*      */ import java.lang.reflect.Array;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.math.RoundingMode;
/*      */ import java.util.Objects;
/*      */ import org.apache.commons.lang3.StringUtils;
/*      */ import org.apache.commons.lang3.Validate;
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
/*      */ public class NumberUtils
/*      */ {
/*   36 */   public static final Long LONG_ZERO = Long.valueOf(0L);
/*      */   
/*   38 */   public static final Long LONG_ONE = Long.valueOf(1L);
/*      */   
/*   40 */   public static final Long LONG_MINUS_ONE = Long.valueOf(-1L);
/*      */   
/*   42 */   public static final Integer INTEGER_ZERO = Integer.valueOf(0);
/*      */   
/*   44 */   public static final Integer INTEGER_ONE = Integer.valueOf(1);
/*      */   
/*   46 */   public static final Integer INTEGER_TWO = Integer.valueOf(2);
/*      */   
/*   48 */   public static final Integer INTEGER_MINUS_ONE = Integer.valueOf(-1);
/*      */   
/*   50 */   public static final Short SHORT_ZERO = Short.valueOf((short)0);
/*      */   
/*   52 */   public static final Short SHORT_ONE = Short.valueOf((short)1);
/*      */   
/*   54 */   public static final Short SHORT_MINUS_ONE = Short.valueOf((short)-1);
/*      */   
/*   56 */   public static final Byte BYTE_ZERO = Byte.valueOf((byte)0);
/*      */   
/*   58 */   public static final Byte BYTE_ONE = Byte.valueOf((byte)1);
/*      */   
/*   60 */   public static final Byte BYTE_MINUS_ONE = Byte.valueOf((byte)-1);
/*      */   
/*   62 */   public static final Double DOUBLE_ZERO = Double.valueOf(0.0D);
/*      */   
/*   64 */   public static final Double DOUBLE_ONE = Double.valueOf(1.0D);
/*      */   
/*   66 */   public static final Double DOUBLE_MINUS_ONE = Double.valueOf(-1.0D);
/*      */   
/*   68 */   public static final Float FLOAT_ZERO = Float.valueOf(0.0F);
/*      */   
/*   70 */   public static final Float FLOAT_ONE = Float.valueOf(1.0F);
/*      */   
/*   72 */   public static final Float FLOAT_MINUS_ONE = Float.valueOf(-1.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   79 */   public static final Long LONG_INT_MAX_VALUE = Long.valueOf(2147483647L);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   86 */   public static final Long LONG_INT_MIN_VALUE = Long.valueOf(-2147483648L);
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
/*      */   public static int toInt(String str) {
/*  117 */     return toInt(str, 0);
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
/*      */   public static int toInt(String str, int defaultValue) {
/*  138 */     if (str == null) {
/*  139 */       return defaultValue;
/*      */     }
/*      */     try {
/*  142 */       return Integer.parseInt(str);
/*  143 */     } catch (NumberFormatException nfe) {
/*  144 */       return defaultValue;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long toLong(String str) {
/*  166 */     return toLong(str, 0L);
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
/*      */   public static long toLong(String str, long defaultValue) {
/*  187 */     if (str == null) {
/*  188 */       return defaultValue;
/*      */     }
/*      */     try {
/*  191 */       return Long.parseLong(str);
/*  192 */     } catch (NumberFormatException nfe) {
/*  193 */       return defaultValue;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float toFloat(String str) {
/*  216 */     return toFloat(str, 0.0F);
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
/*      */   public static float toFloat(String str, float defaultValue) {
/*  239 */     if (str == null) {
/*  240 */       return defaultValue;
/*      */     }
/*      */     try {
/*  243 */       return Float.parseFloat(str);
/*  244 */     } catch (NumberFormatException nfe) {
/*  245 */       return defaultValue;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double toDouble(String str) {
/*  268 */     return toDouble(str, 0.0D);
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
/*      */   public static double toDouble(String str, double defaultValue) {
/*  291 */     if (str == null) {
/*  292 */       return defaultValue;
/*      */     }
/*      */     try {
/*  295 */       return Double.parseDouble(str);
/*  296 */     } catch (NumberFormatException nfe) {
/*  297 */       return defaultValue;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double toDouble(BigDecimal value) {
/*  318 */     return toDouble(value, 0.0D);
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
/*      */   public static double toDouble(BigDecimal value, double defaultValue) {
/*  339 */     return (value == null) ? defaultValue : value.doubleValue();
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
/*      */   public static byte toByte(String str) {
/*  360 */     return toByte(str, (byte)0);
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
/*      */   public static byte toByte(String str, byte defaultValue) {
/*  381 */     if (str == null) {
/*  382 */       return defaultValue;
/*      */     }
/*      */     try {
/*  385 */       return Byte.parseByte(str);
/*  386 */     } catch (NumberFormatException nfe) {
/*  387 */       return defaultValue;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short toShort(String str) {
/*  409 */     return toShort(str, (short)0);
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
/*      */   public static short toShort(String str, short defaultValue) {
/*  430 */     if (str == null) {
/*  431 */       return defaultValue;
/*      */     }
/*      */     try {
/*  434 */       return Short.parseShort(str);
/*  435 */     } catch (NumberFormatException nfe) {
/*  436 */       return defaultValue;
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
/*      */ 
/*      */ 
/*      */   
/*      */   public static BigDecimal toScaledBigDecimal(BigDecimal value) {
/*  453 */     return toScaledBigDecimal(value, INTEGER_TWO.intValue(), RoundingMode.HALF_EVEN);
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
/*      */   public static BigDecimal toScaledBigDecimal(BigDecimal value, int scale, RoundingMode roundingMode) {
/*  469 */     if (value == null) {
/*  470 */       return BigDecimal.ZERO;
/*      */     }
/*  472 */     return value.setScale(scale, (roundingMode == null) ? RoundingMode.HALF_EVEN : roundingMode);
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
/*      */   public static BigDecimal toScaledBigDecimal(Float value) {
/*  491 */     return toScaledBigDecimal(value, INTEGER_TWO.intValue(), RoundingMode.HALF_EVEN);
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
/*      */   public static BigDecimal toScaledBigDecimal(Float value, int scale, RoundingMode roundingMode) {
/*  507 */     if (value == null) {
/*  508 */       return BigDecimal.ZERO;
/*      */     }
/*  510 */     return toScaledBigDecimal(
/*  511 */         BigDecimal.valueOf(value.floatValue()), scale, roundingMode);
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
/*      */   public static BigDecimal toScaledBigDecimal(Double value) {
/*  530 */     return toScaledBigDecimal(value, INTEGER_TWO.intValue(), RoundingMode.HALF_EVEN);
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
/*      */   public static BigDecimal toScaledBigDecimal(Double value, int scale, RoundingMode roundingMode) {
/*  546 */     if (value == null) {
/*  547 */       return BigDecimal.ZERO;
/*      */     }
/*  549 */     return toScaledBigDecimal(
/*  550 */         BigDecimal.valueOf(value.doubleValue()), scale, roundingMode);
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
/*      */   public static BigDecimal toScaledBigDecimal(String value) {
/*  569 */     return toScaledBigDecimal(value, INTEGER_TWO.intValue(), RoundingMode.HALF_EVEN);
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
/*      */   public static BigDecimal toScaledBigDecimal(String value, int scale, RoundingMode roundingMode) {
/*  585 */     if (value == null) {
/*  586 */       return BigDecimal.ZERO;
/*      */     }
/*  588 */     return toScaledBigDecimal(
/*  589 */         createBigDecimal(value), scale, roundingMode);
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
/*      */   public static Number createNumber(String str) {
/*      */     String mant, dec, exp;
/*  662 */     if (str == null) {
/*  663 */       return null;
/*      */     }
/*  665 */     if (StringUtils.isBlank(str)) {
/*  666 */       throw new NumberFormatException("A blank string is not a valid number");
/*      */     }
/*      */     
/*  669 */     String[] hexPrefixes = { "0x", "0X", "#" };
/*  670 */     int length = str.length();
/*  671 */     int offset = (str.charAt(0) == '+' || str.charAt(0) == '-') ? 1 : 0;
/*  672 */     int pfxLen = 0;
/*  673 */     for (String pfx : hexPrefixes) {
/*  674 */       if (str.startsWith(pfx, offset)) {
/*  675 */         pfxLen += pfx.length() + offset;
/*      */         break;
/*      */       } 
/*      */     } 
/*  679 */     if (pfxLen > 0) {
/*  680 */       char firstSigDigit = Character.MIN_VALUE;
/*  681 */       for (int i = pfxLen; i < length; i++) {
/*  682 */         firstSigDigit = str.charAt(i);
/*  683 */         if (firstSigDigit != '0') {
/*      */           break;
/*      */         }
/*  686 */         pfxLen++;
/*      */       } 
/*  688 */       int hexDigits = length - pfxLen;
/*  689 */       if (hexDigits > 16 || (hexDigits == 16 && firstSigDigit > '7')) {
/*  690 */         return createBigInteger(str);
/*      */       }
/*  692 */       if (hexDigits > 8 || (hexDigits == 8 && firstSigDigit > '7')) {
/*  693 */         return createLong(str);
/*      */       }
/*  695 */       return createInteger(str);
/*      */     } 
/*  697 */     char lastChar = str.charAt(length - 1);
/*      */ 
/*      */ 
/*      */     
/*  701 */     int decPos = str.indexOf('.');
/*  702 */     int expPos = str.indexOf('e') + str.indexOf('E') + 1;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  707 */     boolean requestType = (!Character.isDigit(lastChar) && lastChar != '.');
/*  708 */     if (decPos > -1) {
/*  709 */       if (expPos > -1) {
/*  710 */         if (expPos < decPos || expPos > length) {
/*  711 */           throw new NumberFormatException(str + " is not a valid number.");
/*      */         }
/*  713 */         dec = str.substring(decPos + 1, expPos);
/*      */       } else {
/*      */         
/*  716 */         dec = str.substring(decPos + 1, requestType ? (length - 1) : length);
/*      */       } 
/*  718 */       mant = getMantissa(str, decPos);
/*      */     } else {
/*  720 */       if (expPos > -1) {
/*  721 */         if (expPos > length) {
/*  722 */           throw new NumberFormatException(str + " is not a valid number.");
/*      */         }
/*  724 */         mant = getMantissa(str, expPos);
/*      */       } else {
/*      */         
/*  727 */         mant = getMantissa(str, requestType ? (length - 1) : length);
/*      */       } 
/*  729 */       dec = null;
/*      */     } 
/*  731 */     if (requestType) {
/*  732 */       if (expPos > -1 && expPos < length - 1) {
/*  733 */         exp = str.substring(expPos + 1, length - 1);
/*      */       } else {
/*  735 */         exp = null;
/*      */       } 
/*      */       
/*  738 */       String numeric = str.substring(0, length - 1);
/*  739 */       switch (lastChar) {
/*      */         case 'L':
/*      */         case 'l':
/*  742 */           if (dec == null && exp == null && ((
/*      */             
/*  744 */             !numeric.isEmpty() && numeric.charAt(0) == '-' && isDigits(numeric.substring(1))) || isDigits(numeric))) {
/*      */             try {
/*  746 */               return createLong(numeric);
/*  747 */             } catch (NumberFormatException numberFormatException) {
/*      */ 
/*      */               
/*  750 */               return createBigInteger(numeric);
/*      */             } 
/*      */           }
/*  753 */           throw new NumberFormatException(str + " is not a valid number.");
/*      */         case 'F':
/*      */         case 'f':
/*      */           try {
/*  757 */             Float f = createFloat(str);
/*  758 */             if (!f.isInfinite() && (f.floatValue() != 0.0F || isZero(mant, dec)))
/*      */             {
/*      */               
/*  761 */               return f;
/*      */             }
/*      */           }
/*  764 */           catch (NumberFormatException numberFormatException) {}
/*      */ 
/*      */ 
/*      */         
/*      */         case 'D':
/*      */         case 'd':
/*      */           try {
/*  771 */             Double d = createDouble(str);
/*  772 */             if (!d.isInfinite() && (d.doubleValue() != 0.0D || isZero(mant, dec))) {
/*  773 */               return d;
/*      */             }
/*  775 */           } catch (NumberFormatException numberFormatException) {}
/*      */ 
/*      */           
/*      */           try {
/*  779 */             return createBigDecimal(numeric);
/*  780 */           } catch (NumberFormatException numberFormatException) {
/*      */             break;
/*      */           } 
/*      */       } 
/*      */       
/*  785 */       throw new NumberFormatException(str + " is not a valid number.");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  791 */     if (expPos > -1 && expPos < length - 1) {
/*  792 */       exp = str.substring(expPos + 1);
/*      */     } else {
/*  794 */       exp = null;
/*      */     } 
/*  796 */     if (dec == null && exp == null) {
/*      */       
/*      */       try {
/*  799 */         return createInteger(str);
/*  800 */       } catch (NumberFormatException numberFormatException) {
/*      */ 
/*      */         
/*      */         try {
/*  804 */           return createLong(str);
/*  805 */         } catch (NumberFormatException numberFormatException1) {
/*      */ 
/*      */           
/*  808 */           return createBigInteger(str);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     try {
/*  813 */       Float f = createFloat(str);
/*  814 */       Double d = createDouble(str);
/*  815 */       if (!f.isInfinite() && (f
/*  816 */         .floatValue() != 0.0F || isZero(mant, dec)) && f
/*  817 */         .toString().equals(d.toString())) {
/*  818 */         return f;
/*      */       }
/*  820 */       if (!d.isInfinite() && (d.doubleValue() != 0.0D || isZero(mant, dec))) {
/*  821 */         BigDecimal b = createBigDecimal(str);
/*  822 */         if (b.compareTo(BigDecimal.valueOf(d.doubleValue())) == 0) {
/*  823 */           return d;
/*      */         }
/*  825 */         return b;
/*      */       } 
/*  827 */     } catch (NumberFormatException numberFormatException) {}
/*      */ 
/*      */     
/*  830 */     return createBigDecimal(str);
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
/*      */   private static String getMantissa(String str, int stopPos) {
/*  843 */     char firstChar = str.charAt(0);
/*  844 */     boolean hasSign = (firstChar == '-' || firstChar == '+');
/*      */     
/*  846 */     return hasSign ? str.substring(1, stopPos) : str.substring(0, stopPos);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isZero(String mant, String dec) {
/*  876 */     return (isAllZeros(mant) && isAllZeros(dec));
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
/*      */   private static boolean isAllZeros(String str) {
/*  888 */     if (str == null) {
/*  889 */       return true;
/*      */     }
/*  891 */     for (int i = str.length() - 1; i >= 0; i--) {
/*  892 */       if (str.charAt(i) != '0') {
/*  893 */         return false;
/*      */       }
/*      */     } 
/*  896 */     return true;
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
/*      */   public static Float createFloat(String str) {
/*  909 */     if (str == null) {
/*  910 */       return null;
/*      */     }
/*  912 */     return Float.valueOf(str);
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
/*      */   public static Double createDouble(String str) {
/*  925 */     if (str == null) {
/*  926 */       return null;
/*      */     }
/*  928 */     return Double.valueOf(str);
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
/*      */   public static Integer createInteger(String str) {
/*  943 */     if (str == null) {
/*  944 */       return null;
/*      */     }
/*      */     
/*  947 */     return Integer.decode(str);
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
/*      */   public static Long createLong(String str) {
/*  962 */     if (str == null) {
/*  963 */       return null;
/*      */     }
/*  965 */     return Long.decode(str);
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
/*      */   public static BigInteger createBigInteger(String str) {
/*  979 */     if (str == null) {
/*  980 */       return null;
/*      */     }
/*  982 */     if (str.isEmpty()) {
/*  983 */       throw new NumberFormatException("An empty string is not a valid number");
/*      */     }
/*  985 */     int pos = 0;
/*  986 */     int radix = 10;
/*  987 */     boolean negate = false;
/*  988 */     char char0 = str.charAt(0);
/*  989 */     if (char0 == '-') {
/*  990 */       negate = true;
/*  991 */       pos = 1;
/*  992 */     } else if (char0 == '+') {
/*  993 */       pos = 1;
/*      */     } 
/*  995 */     if (str.startsWith("0x", pos) || str.startsWith("0X", pos)) {
/*  996 */       radix = 16;
/*  997 */       pos += 2;
/*  998 */     } else if (str.startsWith("#", pos)) {
/*  999 */       radix = 16;
/* 1000 */       pos++;
/* 1001 */     } else if (str.startsWith("0", pos) && str.length() > pos + 1) {
/* 1002 */       radix = 8;
/* 1003 */       pos++;
/*      */     } 
/*      */     
/* 1006 */     BigInteger value = new BigInteger(str.substring(pos), radix);
/* 1007 */     return negate ? value.negate() : value;
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
/*      */   public static BigDecimal createBigDecimal(String str) {
/* 1020 */     if (str == null) {
/* 1021 */       return null;
/*      */     }
/*      */     
/* 1024 */     if (StringUtils.isBlank(str)) {
/* 1025 */       throw new NumberFormatException("A blank string is not a valid number");
/*      */     }
/* 1027 */     return new BigDecimal(str);
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
/*      */   public static long min(long... array) {
/* 1041 */     validateArray(array);
/*      */ 
/*      */     
/* 1044 */     long min = array[0];
/* 1045 */     for (int i = 1; i < array.length; i++) {
/* 1046 */       if (array[i] < min) {
/* 1047 */         min = array[i];
/*      */       }
/*      */     } 
/*      */     
/* 1051 */     return min;
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
/*      */   public static int min(int... array) {
/* 1065 */     validateArray(array);
/*      */ 
/*      */     
/* 1068 */     int min = array[0];
/* 1069 */     for (int j = 1; j < array.length; j++) {
/* 1070 */       if (array[j] < min) {
/* 1071 */         min = array[j];
/*      */       }
/*      */     } 
/*      */     
/* 1075 */     return min;
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
/*      */   public static short min(short... array) {
/* 1089 */     validateArray(array);
/*      */ 
/*      */     
/* 1092 */     short min = array[0];
/* 1093 */     for (int i = 1; i < array.length; i++) {
/* 1094 */       if (array[i] < min) {
/* 1095 */         min = array[i];
/*      */       }
/*      */     } 
/*      */     
/* 1099 */     return min;
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
/*      */   public static byte min(byte... array) {
/* 1113 */     validateArray(array);
/*      */ 
/*      */     
/* 1116 */     byte min = array[0];
/* 1117 */     for (int i = 1; i < array.length; i++) {
/* 1118 */       if (array[i] < min) {
/* 1119 */         min = array[i];
/*      */       }
/*      */     } 
/*      */     
/* 1123 */     return min;
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
/*      */   public static double min(double... array) {
/* 1138 */     validateArray(array);
/*      */ 
/*      */     
/* 1141 */     double min = array[0];
/* 1142 */     for (int i = 1; i < array.length; i++) {
/* 1143 */       if (Double.isNaN(array[i])) {
/* 1144 */         return Double.NaN;
/*      */       }
/* 1146 */       if (array[i] < min) {
/* 1147 */         min = array[i];
/*      */       }
/*      */     } 
/*      */     
/* 1151 */     return min;
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
/*      */   public static float min(float... array) {
/* 1166 */     validateArray(array);
/*      */ 
/*      */     
/* 1169 */     float min = array[0];
/* 1170 */     for (int i = 1; i < array.length; i++) {
/* 1171 */       if (Float.isNaN(array[i])) {
/* 1172 */         return Float.NaN;
/*      */       }
/* 1174 */       if (array[i] < min) {
/* 1175 */         min = array[i];
/*      */       }
/*      */     } 
/*      */     
/* 1179 */     return min;
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
/*      */   public static long max(long... array) {
/* 1193 */     validateArray(array);
/*      */ 
/*      */     
/* 1196 */     long max = array[0];
/* 1197 */     for (int j = 1; j < array.length; j++) {
/* 1198 */       if (array[j] > max) {
/* 1199 */         max = array[j];
/*      */       }
/*      */     } 
/*      */     
/* 1203 */     return max;
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
/*      */   public static int max(int... array) {
/* 1217 */     validateArray(array);
/*      */ 
/*      */     
/* 1220 */     int max = array[0];
/* 1221 */     for (int j = 1; j < array.length; j++) {
/* 1222 */       if (array[j] > max) {
/* 1223 */         max = array[j];
/*      */       }
/*      */     } 
/*      */     
/* 1227 */     return max;
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
/*      */   public static short max(short... array) {
/* 1241 */     validateArray(array);
/*      */ 
/*      */     
/* 1244 */     short max = array[0];
/* 1245 */     for (int i = 1; i < array.length; i++) {
/* 1246 */       if (array[i] > max) {
/* 1247 */         max = array[i];
/*      */       }
/*      */     } 
/*      */     
/* 1251 */     return max;
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
/*      */   public static byte max(byte... array) {
/* 1265 */     validateArray(array);
/*      */ 
/*      */     
/* 1268 */     byte max = array[0];
/* 1269 */     for (int i = 1; i < array.length; i++) {
/* 1270 */       if (array[i] > max) {
/* 1271 */         max = array[i];
/*      */       }
/*      */     } 
/*      */     
/* 1275 */     return max;
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
/*      */   public static double max(double... array) {
/* 1290 */     validateArray(array);
/*      */ 
/*      */     
/* 1293 */     double max = array[0];
/* 1294 */     for (int j = 1; j < array.length; j++) {
/* 1295 */       if (Double.isNaN(array[j])) {
/* 1296 */         return Double.NaN;
/*      */       }
/* 1298 */       if (array[j] > max) {
/* 1299 */         max = array[j];
/*      */       }
/*      */     } 
/*      */     
/* 1303 */     return max;
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
/*      */   public static float max(float... array) {
/* 1318 */     validateArray(array);
/*      */ 
/*      */     
/* 1321 */     float max = array[0];
/* 1322 */     for (int j = 1; j < array.length; j++) {
/* 1323 */       if (Float.isNaN(array[j])) {
/* 1324 */         return Float.NaN;
/*      */       }
/* 1326 */       if (array[j] > max) {
/* 1327 */         max = array[j];
/*      */       }
/*      */     } 
/*      */     
/* 1331 */     return max;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void validateArray(Object array) {
/* 1342 */     Objects.requireNonNull(array, "array");
/* 1343 */     Validate.isTrue((Array.getLength(array) != 0), "Array cannot be empty.", new Object[0]);
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
/*      */   public static long min(long a, long b, long c) {
/* 1356 */     if (b < a) {
/* 1357 */       a = b;
/*      */     }
/* 1359 */     if (c < a) {
/* 1360 */       a = c;
/*      */     }
/* 1362 */     return a;
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
/*      */   public static int min(int a, int b, int c) {
/* 1374 */     if (b < a) {
/* 1375 */       a = b;
/*      */     }
/* 1377 */     if (c < a) {
/* 1378 */       a = c;
/*      */     }
/* 1380 */     return a;
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
/*      */   public static short min(short a, short b, short c) {
/* 1392 */     if (b < a) {
/* 1393 */       a = b;
/*      */     }
/* 1395 */     if (c < a) {
/* 1396 */       a = c;
/*      */     }
/* 1398 */     return a;
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
/*      */   public static byte min(byte a, byte b, byte c) {
/* 1410 */     if (b < a) {
/* 1411 */       a = b;
/*      */     }
/* 1413 */     if (c < a) {
/* 1414 */       a = c;
/*      */     }
/* 1416 */     return a;
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
/*      */   public static double min(double a, double b, double c) {
/* 1432 */     return Math.min(Math.min(a, b), c);
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
/*      */   public static float min(float a, float b, float c) {
/* 1448 */     return Math.min(Math.min(a, b), c);
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
/*      */   public static long max(long a, long b, long c) {
/* 1461 */     if (b > a) {
/* 1462 */       a = b;
/*      */     }
/* 1464 */     if (c > a) {
/* 1465 */       a = c;
/*      */     }
/* 1467 */     return a;
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
/*      */   public static int max(int a, int b, int c) {
/* 1479 */     if (b > a) {
/* 1480 */       a = b;
/*      */     }
/* 1482 */     if (c > a) {
/* 1483 */       a = c;
/*      */     }
/* 1485 */     return a;
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
/*      */   public static short max(short a, short b, short c) {
/* 1497 */     if (b > a) {
/* 1498 */       a = b;
/*      */     }
/* 1500 */     if (c > a) {
/* 1501 */       a = c;
/*      */     }
/* 1503 */     return a;
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
/*      */   public static byte max(byte a, byte b, byte c) {
/* 1515 */     if (b > a) {
/* 1516 */       a = b;
/*      */     }
/* 1518 */     if (c > a) {
/* 1519 */       a = c;
/*      */     }
/* 1521 */     return a;
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
/*      */   public static double max(double a, double b, double c) {
/* 1537 */     return Math.max(Math.max(a, b), c);
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
/*      */   public static float max(float a, float b, float c) {
/* 1553 */     return Math.max(Math.max(a, b), c);
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
/*      */   public static boolean isDigits(String str) {
/* 1567 */     return StringUtils.isNumeric(str);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static boolean isNumber(String str) {
/* 1597 */     return isCreatable(str);
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
/*      */   
/*      */   public static boolean isCreatable(String str) {
/* 1623 */     if (StringUtils.isEmpty(str)) {
/* 1624 */       return false;
/*      */     }
/* 1626 */     char[] chars = str.toCharArray();
/* 1627 */     int sz = chars.length;
/* 1628 */     boolean hasExp = false;
/* 1629 */     boolean hasDecPoint = false;
/* 1630 */     boolean allowSigns = false;
/* 1631 */     boolean foundDigit = false;
/*      */     
/* 1633 */     int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
/* 1634 */     if (sz > start + 1 && chars[start] == '0' && !StringUtils.contains(str, 46)) {
/* 1635 */       if (chars[start + 1] == 'x' || chars[start + 1] == 'X') {
/* 1636 */         int j = start + 2;
/* 1637 */         if (j == sz) {
/* 1638 */           return false;
/*      */         }
/*      */         
/* 1641 */         for (; j < chars.length; j++) {
/* 1642 */           if ((chars[j] < '0' || chars[j] > '9') && (chars[j] < 'a' || chars[j] > 'f') && (chars[j] < 'A' || chars[j] > 'F'))
/*      */           {
/*      */             
/* 1645 */             return false;
/*      */           }
/*      */         } 
/* 1648 */         return true;
/*      */       } 
/* 1650 */       if (Character.isDigit(chars[start + 1])) {
/*      */         
/* 1652 */         int j = start + 1;
/* 1653 */         for (; j < chars.length; j++) {
/* 1654 */           if (chars[j] < '0' || chars[j] > '7') {
/* 1655 */             return false;
/*      */           }
/*      */         } 
/* 1658 */         return true;
/*      */       } 
/*      */     } 
/* 1661 */     sz--;
/*      */     
/* 1663 */     int i = start;
/*      */ 
/*      */     
/* 1666 */     while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
/* 1667 */       if (chars[i] >= '0' && chars[i] <= '9') {
/* 1668 */         foundDigit = true;
/* 1669 */         allowSigns = false;
/*      */       }
/* 1671 */       else if (chars[i] == '.') {
/* 1672 */         if (hasDecPoint || hasExp)
/*      */         {
/* 1674 */           return false;
/*      */         }
/* 1676 */         hasDecPoint = true;
/* 1677 */       } else if (chars[i] == 'e' || chars[i] == 'E') {
/*      */         
/* 1679 */         if (hasExp)
/*      */         {
/* 1681 */           return false;
/*      */         }
/* 1683 */         if (!foundDigit) {
/* 1684 */           return false;
/*      */         }
/* 1686 */         hasExp = true;
/* 1687 */         allowSigns = true;
/* 1688 */       } else if (chars[i] == '+' || chars[i] == '-') {
/* 1689 */         if (!allowSigns) {
/* 1690 */           return false;
/*      */         }
/* 1692 */         allowSigns = false;
/* 1693 */         foundDigit = false;
/*      */       } else {
/* 1695 */         return false;
/*      */       } 
/* 1697 */       i++;
/*      */     } 
/* 1699 */     if (i < chars.length) {
/* 1700 */       if (chars[i] >= '0' && chars[i] <= '9')
/*      */       {
/* 1702 */         return true;
/*      */       }
/* 1704 */       if (chars[i] == 'e' || chars[i] == 'E')
/*      */       {
/* 1706 */         return false;
/*      */       }
/* 1708 */       if (chars[i] == '.') {
/* 1709 */         if (hasDecPoint || hasExp)
/*      */         {
/* 1711 */           return false;
/*      */         }
/*      */         
/* 1714 */         return foundDigit;
/*      */       } 
/* 1716 */       if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F'))
/*      */       {
/*      */ 
/*      */ 
/*      */         
/* 1721 */         return foundDigit;
/*      */       }
/* 1723 */       if (chars[i] == 'l' || chars[i] == 'L')
/*      */       {
/*      */         
/* 1726 */         return (foundDigit && !hasExp && !hasDecPoint);
/*      */       }
/*      */       
/* 1729 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 1733 */     return (!allowSigns && foundDigit);
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
/*      */   public static boolean isParsable(String str) {
/* 1754 */     if (StringUtils.isEmpty(str)) {
/* 1755 */       return false;
/*      */     }
/* 1757 */     if (str.charAt(str.length() - 1) == '.') {
/* 1758 */       return false;
/*      */     }
/* 1760 */     if (str.charAt(0) == '-') {
/* 1761 */       if (str.length() == 1) {
/* 1762 */         return false;
/*      */       }
/* 1764 */       return withDecimalsParsing(str, 1);
/*      */     } 
/* 1766 */     return withDecimalsParsing(str, 0);
/*      */   }
/*      */   
/*      */   private static boolean withDecimalsParsing(String str, int beginIdx) {
/* 1770 */     int decimalPoints = 0;
/* 1771 */     for (int i = beginIdx; i < str.length(); i++) {
/* 1772 */       boolean isDecimalPoint = (str.charAt(i) == '.');
/* 1773 */       if (isDecimalPoint) {
/* 1774 */         decimalPoints++;
/*      */       }
/* 1776 */       if (decimalPoints > 1) {
/* 1777 */         return false;
/*      */       }
/* 1779 */       if (!isDecimalPoint && !Character.isDigit(str.charAt(i))) {
/* 1780 */         return false;
/*      */       }
/*      */     } 
/* 1783 */     return true;
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
/*      */   public static int compare(int x, int y) {
/* 1797 */     if (x == y) {
/* 1798 */       return 0;
/*      */     }
/* 1800 */     return (x < y) ? -1 : 1;
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
/*      */   public static int compare(long x, long y) {
/* 1814 */     if (x == y) {
/* 1815 */       return 0;
/*      */     }
/* 1817 */     return (x < y) ? -1 : 1;
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
/*      */   public static int compare(short x, short y) {
/* 1831 */     if (x == y) {
/* 1832 */       return 0;
/*      */     }
/* 1834 */     return (x < y) ? -1 : 1;
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
/*      */   public static int compare(byte x, byte y) {
/* 1848 */     return x - y;
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\math\NumberUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */