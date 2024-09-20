/*      */ package org.apache.commons.lang3;
/*      */ 
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.function.Consumer;
/*      */ import org.apache.commons.lang3.math.NumberUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class BooleanUtils
/*      */ {
/*   38 */   private static final List<Boolean> BOOLEAN_LIST = Collections.unmodifiableList(Arrays.asList(new Boolean[] { Boolean.FALSE, Boolean.TRUE }));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String FALSE = "false";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String NO = "no";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String OFF = "off";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String ON = "on";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String TRUE = "true";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String YES = "yes";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean and(boolean... array) {
/*  101 */     ObjectUtils.requireNonEmpty(array, "array");
/*  102 */     for (boolean element : array) {
/*  103 */       if (!element) {
/*  104 */         return false;
/*      */       }
/*      */     } 
/*  107 */     return true;
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
/*      */   public static Boolean and(Boolean... array) {
/*  133 */     ObjectUtils.requireNonEmpty(array, "array");
/*  134 */     return and(ArrayUtils.toPrimitive(array)) ? Boolean.TRUE : Boolean.FALSE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Boolean[] booleanValues() {
/*  143 */     return new Boolean[] { Boolean.FALSE, Boolean.TRUE };
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
/*      */   public static int compare(boolean x, boolean y) {
/*  157 */     if (x == y) {
/*  158 */       return 0;
/*      */     }
/*  160 */     return x ? 1 : -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void forEach(Consumer<Boolean> action) {
/*  170 */     values().forEach(action);
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
/*      */   public static boolean isFalse(Boolean bool) {
/*  188 */     return Boolean.FALSE.equals(bool);
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
/*      */   public static boolean isNotFalse(Boolean bool) {
/*  206 */     return !isFalse(bool);
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
/*      */   public static boolean isNotTrue(Boolean bool) {
/*  224 */     return !isTrue(bool);
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
/*      */   public static boolean isTrue(Boolean bool) {
/*  242 */     return Boolean.TRUE.equals(bool);
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
/*      */   public static Boolean negate(Boolean bool) {
/*  262 */     if (bool == null) {
/*  263 */       return null;
/*      */     }
/*  265 */     return bool.booleanValue() ? Boolean.FALSE : Boolean.TRUE;
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
/*      */   public static boolean oneHot(boolean... array) {
/*  282 */     ObjectUtils.requireNonEmpty(array, "array");
/*  283 */     boolean result = false;
/*  284 */     for (boolean element : array) {
/*  285 */       if (element) {
/*  286 */         if (result) {
/*  287 */           return false;
/*      */         }
/*  289 */         result = true;
/*      */       } 
/*      */     } 
/*  292 */     return result;
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
/*      */   public static Boolean oneHot(Boolean... array) {
/*  313 */     return Boolean.valueOf(oneHot(ArrayUtils.toPrimitive(array)));
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
/*      */   public static boolean or(boolean... array) {
/*  335 */     ObjectUtils.requireNonEmpty(array, "array");
/*  336 */     for (boolean element : array) {
/*  337 */       if (element) {
/*  338 */         return true;
/*      */       }
/*      */     } 
/*  341 */     return false;
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
/*      */   public static Boolean or(Boolean... array) {
/*  368 */     ObjectUtils.requireNonEmpty(array, "array");
/*  369 */     return or(ArrayUtils.toPrimitive(array)) ? Boolean.TRUE : Boolean.FALSE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] primitiveValues() {
/*  378 */     return new boolean[] { false, true };
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
/*      */   public static boolean toBoolean(Boolean bool) {
/*  395 */     return (bool != null && bool.booleanValue());
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
/*      */   public static boolean toBoolean(int value) {
/*  413 */     return (value != 0);
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
/*      */   public static boolean toBoolean(int value, int trueValue, int falseValue) {
/*  438 */     if (value == trueValue) {
/*  439 */       return true;
/*      */     }
/*  441 */     if (value == falseValue) {
/*  442 */       return false;
/*      */     }
/*  444 */     throw new IllegalArgumentException("The Integer did not match either specified value");
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
/*      */   public static boolean toBoolean(Integer value, Integer trueValue, Integer falseValue) {
/*  465 */     if (value == null) {
/*  466 */       if (trueValue == null) {
/*  467 */         return true;
/*      */       }
/*  469 */       if (falseValue == null)
/*  470 */         return false; 
/*      */     } else {
/*  472 */       if (value.equals(trueValue))
/*  473 */         return true; 
/*  474 */       if (value.equals(falseValue))
/*  475 */         return false; 
/*      */     } 
/*  477 */     throw new IllegalArgumentException("The Integer did not match either specified value");
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
/*      */   public static boolean toBoolean(String str) {
/*  510 */     return (toBooleanObject(str) == Boolean.TRUE);
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
/*      */   public static boolean toBoolean(String str, String trueString, String falseString) {
/*  528 */     if (str == trueString) {
/*  529 */       return true;
/*      */     }
/*  531 */     if (str == falseString) {
/*  532 */       return false;
/*      */     }
/*  534 */     if (str != null) {
/*  535 */       if (str.equals(trueString)) {
/*  536 */         return true;
/*      */       }
/*  538 */       if (str.equals(falseString)) {
/*  539 */         return false;
/*      */       }
/*      */     } 
/*  542 */     throw new IllegalArgumentException("The String did not match either specified value");
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
/*      */   public static boolean toBooleanDefaultIfNull(Boolean bool, boolean valueIfNull) {
/*  562 */     if (bool == null) {
/*  563 */       return valueIfNull;
/*      */     }
/*  565 */     return bool.booleanValue();
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
/*      */   public static Boolean toBooleanObject(int value) {
/*  583 */     return (value == 0) ? Boolean.FALSE : Boolean.TRUE;
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
/*      */   public static Boolean toBooleanObject(int value, int trueValue, int falseValue, int nullValue) {
/*  612 */     if (value == trueValue) {
/*  613 */       return Boolean.TRUE;
/*      */     }
/*  615 */     if (value == falseValue) {
/*  616 */       return Boolean.FALSE;
/*      */     }
/*  618 */     if (value == nullValue) {
/*  619 */       return null;
/*      */     }
/*  621 */     throw new IllegalArgumentException("The Integer did not match any specified value");
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
/*      */   public static Boolean toBooleanObject(Integer value) {
/*  644 */     if (value == null) {
/*  645 */       return null;
/*      */     }
/*  647 */     return (value.intValue() == 0) ? Boolean.FALSE : Boolean.TRUE;
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
/*      */   public static Boolean toBooleanObject(Integer value, Integer trueValue, Integer falseValue, Integer nullValue) {
/*  676 */     if (value == null) {
/*  677 */       if (trueValue == null) {
/*  678 */         return Boolean.TRUE;
/*      */       }
/*  680 */       if (falseValue == null) {
/*  681 */         return Boolean.FALSE;
/*      */       }
/*  683 */       if (nullValue == null)
/*  684 */         return null; 
/*      */     } else {
/*  686 */       if (value.equals(trueValue))
/*  687 */         return Boolean.TRUE; 
/*  688 */       if (value.equals(falseValue))
/*  689 */         return Boolean.FALSE; 
/*  690 */       if (value.equals(nullValue))
/*  691 */         return null; 
/*      */     } 
/*  693 */     throw new IllegalArgumentException("The Integer did not match any specified value");
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
/*      */   public static Boolean toBooleanObject(String str) {
/*      */     char ch0;
/*      */     char ch1;
/*      */     char ch2;
/*      */     char ch3;
/*      */     char ch4;
/*  740 */     if (str == "true") {
/*  741 */       return Boolean.TRUE;
/*      */     }
/*  743 */     if (str == null) {
/*  744 */       return null;
/*      */     }
/*  746 */     switch (str.length()) {
/*      */       case 1:
/*  748 */         ch0 = str.charAt(0);
/*  749 */         if (ch0 == 'y' || ch0 == 'Y' || ch0 == 't' || ch0 == 'T' || ch0 == '1')
/*      */         {
/*      */           
/*  752 */           return Boolean.TRUE;
/*      */         }
/*  754 */         if (ch0 == 'n' || ch0 == 'N' || ch0 == 'f' || ch0 == 'F' || ch0 == '0')
/*      */         {
/*      */           
/*  757 */           return Boolean.FALSE;
/*      */         }
/*      */         break;
/*      */       
/*      */       case 2:
/*  762 */         ch0 = str.charAt(0);
/*  763 */         ch1 = str.charAt(1);
/*  764 */         if ((ch0 == 'o' || ch0 == 'O') && (ch1 == 'n' || ch1 == 'N'))
/*      */         {
/*  766 */           return Boolean.TRUE;
/*      */         }
/*  768 */         if ((ch0 == 'n' || ch0 == 'N') && (ch1 == 'o' || ch1 == 'O'))
/*      */         {
/*  770 */           return Boolean.FALSE;
/*      */         }
/*      */         break;
/*      */       
/*      */       case 3:
/*  775 */         ch0 = str.charAt(0);
/*  776 */         ch1 = str.charAt(1);
/*  777 */         ch2 = str.charAt(2);
/*  778 */         if ((ch0 == 'y' || ch0 == 'Y') && (ch1 == 'e' || ch1 == 'E') && (ch2 == 's' || ch2 == 'S'))
/*      */         {
/*      */           
/*  781 */           return Boolean.TRUE;
/*      */         }
/*  783 */         if ((ch0 == 'o' || ch0 == 'O') && (ch1 == 'f' || ch1 == 'F') && (ch2 == 'f' || ch2 == 'F'))
/*      */         {
/*      */           
/*  786 */           return Boolean.FALSE;
/*      */         }
/*      */         break;
/*      */       
/*      */       case 4:
/*  791 */         ch0 = str.charAt(0);
/*  792 */         ch1 = str.charAt(1);
/*  793 */         ch2 = str.charAt(2);
/*  794 */         ch3 = str.charAt(3);
/*  795 */         if ((ch0 == 't' || ch0 == 'T') && (ch1 == 'r' || ch1 == 'R') && (ch2 == 'u' || ch2 == 'U') && (ch3 == 'e' || ch3 == 'E'))
/*      */         {
/*      */ 
/*      */           
/*  799 */           return Boolean.TRUE;
/*      */         }
/*      */         break;
/*      */       
/*      */       case 5:
/*  804 */         ch0 = str.charAt(0);
/*  805 */         ch1 = str.charAt(1);
/*  806 */         ch2 = str.charAt(2);
/*  807 */         ch3 = str.charAt(3);
/*  808 */         ch4 = str.charAt(4);
/*  809 */         if ((ch0 == 'f' || ch0 == 'F') && (ch1 == 'a' || ch1 == 'A') && (ch2 == 'l' || ch2 == 'L') && (ch3 == 's' || ch3 == 'S') && (ch4 == 'e' || ch4 == 'E'))
/*      */         {
/*      */ 
/*      */ 
/*      */           
/*  814 */           return Boolean.FALSE;
/*      */         }
/*      */         break;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  822 */     return null;
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
/*      */   public static Boolean toBooleanObject(String str, String trueString, String falseString, String nullString) {
/*  852 */     if (str == null) {
/*  853 */       if (trueString == null) {
/*  854 */         return Boolean.TRUE;
/*      */       }
/*  856 */       if (falseString == null) {
/*  857 */         return Boolean.FALSE;
/*      */       }
/*  859 */       if (nullString == null)
/*  860 */         return null; 
/*      */     } else {
/*  862 */       if (str.equals(trueString))
/*  863 */         return Boolean.TRUE; 
/*  864 */       if (str.equals(falseString))
/*  865 */         return Boolean.FALSE; 
/*  866 */       if (str.equals(nullString)) {
/*  867 */         return null;
/*      */       }
/*      */     } 
/*  870 */     throw new IllegalArgumentException("The String did not match any specified value");
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
/*      */   public static int toInteger(boolean bool) {
/*  886 */     return bool ? 1 : 0;
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
/*      */   public static int toInteger(boolean bool, int trueValue, int falseValue) {
/*  903 */     return bool ? trueValue : falseValue;
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
/*      */   public static int toInteger(Boolean bool, int trueValue, int falseValue, int nullValue) {
/*  922 */     if (bool == null) {
/*  923 */       return nullValue;
/*      */     }
/*  925 */     return bool.booleanValue() ? trueValue : falseValue;
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
/*      */   public static Integer toIntegerObject(boolean bool) {
/*  941 */     return bool ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO;
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
/*      */   public static Integer toIntegerObject(boolean bool, Integer trueValue, Integer falseValue) {
/*  958 */     return bool ? trueValue : falseValue;
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
/*      */   public static Integer toIntegerObject(Boolean bool) {
/*  976 */     if (bool == null) {
/*  977 */       return null;
/*      */     }
/*  979 */     return bool.booleanValue() ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO;
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
/*      */   public static Integer toIntegerObject(Boolean bool, Integer trueValue, Integer falseValue, Integer nullValue) {
/*  998 */     if (bool == null) {
/*  999 */       return nullValue;
/*      */     }
/* 1001 */     return bool.booleanValue() ? trueValue : falseValue;
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
/*      */   public static String toString(boolean bool, String trueString, String falseString) {
/* 1018 */     return bool ? trueString : falseString;
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
/*      */   public static String toString(Boolean bool, String trueString, String falseString, String nullString) {
/* 1037 */     if (bool == null) {
/* 1038 */       return nullString;
/*      */     }
/* 1040 */     return bool.booleanValue() ? trueString : falseString;
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
/*      */   public static String toStringOnOff(boolean bool) {
/* 1056 */     return toString(bool, "on", "off");
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
/*      */   public static String toStringOnOff(Boolean bool) {
/* 1073 */     return toString(bool, "on", "off", null);
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
/*      */   public static String toStringTrueFalse(boolean bool) {
/* 1089 */     return toString(bool, "true", "false");
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
/*      */   public static String toStringTrueFalse(Boolean bool) {
/* 1106 */     return toString(bool, "true", "false", null);
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
/*      */   public static String toStringYesNo(boolean bool) {
/* 1122 */     return toString(bool, "yes", "no");
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
/*      */   public static String toStringYesNo(Boolean bool) {
/* 1139 */     return toString(bool, "yes", "no", null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<Boolean> values() {
/* 1149 */     return BOOLEAN_LIST;
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
/*      */   public static boolean xor(boolean... array) {
/* 1175 */     ObjectUtils.requireNonEmpty(array, "array");
/*      */     
/* 1177 */     boolean result = false;
/* 1178 */     for (boolean element : array) {
/* 1179 */       result ^= element;
/*      */     }
/*      */     
/* 1182 */     return result;
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
/*      */   public static Boolean xor(Boolean... array) {
/* 1205 */     ObjectUtils.requireNonEmpty(array, "array");
/* 1206 */     return xor(ArrayUtils.toPrimitive(array)) ? Boolean.TRUE : Boolean.FALSE;
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\BooleanUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */