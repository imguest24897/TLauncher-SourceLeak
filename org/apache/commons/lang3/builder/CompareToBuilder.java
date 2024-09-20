/*      */ package org.apache.commons.lang3.builder;
/*      */ 
/*      */ import java.lang.reflect.AccessibleObject;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Objects;
/*      */ import org.apache.commons.lang3.ArrayUtils;
/*      */ import org.apache.commons.lang3.ObjectUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CompareToBuilder
/*      */   implements Builder<Integer>
/*      */ {
/*      */   private int comparison;
/*      */   
/*      */   private static void reflectionAppend(Object lhs, Object rhs, Class<?> clazz, CompareToBuilder builder, boolean useTransients, String[] excludeFields) {
/*  118 */     Field[] fields = clazz.getDeclaredFields();
/*  119 */     AccessibleObject.setAccessible((AccessibleObject[])fields, true);
/*  120 */     for (int i = 0; i < fields.length && builder.comparison == 0; i++) {
/*  121 */       Field field = fields[i];
/*  122 */       if (!ArrayUtils.contains((Object[])excludeFields, field.getName()) && 
/*  123 */         !field.getName().contains("$") && (useTransients || 
/*  124 */         !Modifier.isTransient(field.getModifiers())) && 
/*  125 */         !Modifier.isStatic(field.getModifiers()))
/*      */       {
/*      */         
/*  128 */         builder.append(Reflection.getUnchecked(field, lhs), Reflection.getUnchecked(field, rhs));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int reflectionCompare(Object lhs, Object rhs) {
/*  160 */     return reflectionCompare(lhs, rhs, false, null, new String[0]);
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
/*      */   public static int reflectionCompare(Object lhs, Object rhs, boolean compareTransients) {
/*  192 */     return reflectionCompare(lhs, rhs, compareTransients, null, new String[0]);
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
/*      */   public static int reflectionCompare(Object lhs, Object rhs, boolean compareTransients, Class<?> reflectUpToClass, String... excludeFields) {
/*  234 */     if (lhs == rhs) {
/*  235 */       return 0;
/*      */     }
/*  237 */     Objects.requireNonNull(lhs, "lhs");
/*  238 */     Objects.requireNonNull(rhs, "rhs");
/*      */     
/*  240 */     Class<?> lhsClazz = lhs.getClass();
/*  241 */     if (!lhsClazz.isInstance(rhs)) {
/*  242 */       throw new ClassCastException();
/*      */     }
/*  244 */     CompareToBuilder compareToBuilder = new CompareToBuilder();
/*  245 */     reflectionAppend(lhs, rhs, lhsClazz, compareToBuilder, compareTransients, excludeFields);
/*  246 */     while (lhsClazz.getSuperclass() != null && lhsClazz != reflectUpToClass) {
/*  247 */       lhsClazz = lhsClazz.getSuperclass();
/*  248 */       reflectionAppend(lhs, rhs, lhsClazz, compareToBuilder, compareTransients, excludeFields);
/*      */     } 
/*  250 */     return compareToBuilder.toComparison();
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
/*      */   public static int reflectionCompare(Object lhs, Object rhs, Collection<String> excludeFields) {
/*  283 */     return reflectionCompare(lhs, rhs, ReflectionToStringBuilder.toNoNullStringArray(excludeFields));
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
/*      */   public static int reflectionCompare(Object lhs, Object rhs, String... excludeFields) {
/*  316 */     return reflectionCompare(lhs, rhs, false, null, excludeFields);
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
/*      */   public CompareToBuilder() {
/*  332 */     this.comparison = 0;
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
/*      */   public CompareToBuilder append(boolean lhs, boolean rhs) {
/*  344 */     if (this.comparison != 0) {
/*  345 */       return this;
/*      */     }
/*  347 */     if (lhs == rhs) {
/*  348 */       return this;
/*      */     }
/*  350 */     if (lhs) {
/*  351 */       this.comparison = 1;
/*      */     } else {
/*  353 */       this.comparison = -1;
/*      */     } 
/*  355 */     return this;
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
/*      */   public CompareToBuilder append(boolean[] lhs, boolean[] rhs) {
/*  374 */     if (this.comparison != 0) {
/*  375 */       return this;
/*      */     }
/*  377 */     if (lhs == rhs) {
/*  378 */       return this;
/*      */     }
/*  380 */     if (lhs == null) {
/*  381 */       this.comparison = -1;
/*  382 */       return this;
/*      */     } 
/*  384 */     if (rhs == null) {
/*  385 */       this.comparison = 1;
/*  386 */       return this;
/*      */     } 
/*  388 */     if (lhs.length != rhs.length) {
/*  389 */       this.comparison = (lhs.length < rhs.length) ? -1 : 1;
/*  390 */       return this;
/*      */     } 
/*  392 */     for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
/*  393 */       append(lhs[i], rhs[i]);
/*      */     }
/*  395 */     return this;
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
/*      */   public CompareToBuilder append(byte lhs, byte rhs) {
/*  407 */     if (this.comparison != 0) {
/*  408 */       return this;
/*      */     }
/*  410 */     this.comparison = Byte.compare(lhs, rhs);
/*  411 */     return this;
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
/*      */   public CompareToBuilder append(byte[] lhs, byte[] rhs) {
/*  430 */     if (this.comparison != 0) {
/*  431 */       return this;
/*      */     }
/*  433 */     if (lhs == rhs) {
/*  434 */       return this;
/*      */     }
/*  436 */     if (lhs == null) {
/*  437 */       this.comparison = -1;
/*  438 */       return this;
/*      */     } 
/*  440 */     if (rhs == null) {
/*  441 */       this.comparison = 1;
/*  442 */       return this;
/*      */     } 
/*  444 */     if (lhs.length != rhs.length) {
/*  445 */       this.comparison = (lhs.length < rhs.length) ? -1 : 1;
/*  446 */       return this;
/*      */     } 
/*  448 */     for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
/*  449 */       append(lhs[i], rhs[i]);
/*      */     }
/*  451 */     return this;
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
/*      */   public CompareToBuilder append(char lhs, char rhs) {
/*  463 */     if (this.comparison != 0) {
/*  464 */       return this;
/*      */     }
/*  466 */     this.comparison = Character.compare(lhs, rhs);
/*  467 */     return this;
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
/*      */   public CompareToBuilder append(char[] lhs, char[] rhs) {
/*  486 */     if (this.comparison != 0) {
/*  487 */       return this;
/*      */     }
/*  489 */     if (lhs == rhs) {
/*  490 */       return this;
/*      */     }
/*  492 */     if (lhs == null) {
/*  493 */       this.comparison = -1;
/*  494 */       return this;
/*      */     } 
/*  496 */     if (rhs == null) {
/*  497 */       this.comparison = 1;
/*  498 */       return this;
/*      */     } 
/*  500 */     if (lhs.length != rhs.length) {
/*  501 */       this.comparison = (lhs.length < rhs.length) ? -1 : 1;
/*  502 */       return this;
/*      */     } 
/*  504 */     for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
/*  505 */       append(lhs[i], rhs[i]);
/*      */     }
/*  507 */     return this;
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
/*      */   public CompareToBuilder append(double lhs, double rhs) {
/*  524 */     if (this.comparison != 0) {
/*  525 */       return this;
/*      */     }
/*  527 */     this.comparison = Double.compare(lhs, rhs);
/*  528 */     return this;
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
/*      */   public CompareToBuilder append(double[] lhs, double[] rhs) {
/*  547 */     if (this.comparison != 0) {
/*  548 */       return this;
/*      */     }
/*  550 */     if (lhs == rhs) {
/*  551 */       return this;
/*      */     }
/*  553 */     if (lhs == null) {
/*  554 */       this.comparison = -1;
/*  555 */       return this;
/*      */     } 
/*  557 */     if (rhs == null) {
/*  558 */       this.comparison = 1;
/*  559 */       return this;
/*      */     } 
/*  561 */     if (lhs.length != rhs.length) {
/*  562 */       this.comparison = (lhs.length < rhs.length) ? -1 : 1;
/*  563 */       return this;
/*      */     } 
/*  565 */     for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
/*  566 */       append(lhs[i], rhs[i]);
/*      */     }
/*  568 */     return this;
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
/*      */   public CompareToBuilder append(float lhs, float rhs) {
/*  585 */     if (this.comparison != 0) {
/*  586 */       return this;
/*      */     }
/*  588 */     this.comparison = Float.compare(lhs, rhs);
/*  589 */     return this;
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
/*      */   public CompareToBuilder append(float[] lhs, float[] rhs) {
/*  608 */     if (this.comparison != 0) {
/*  609 */       return this;
/*      */     }
/*  611 */     if (lhs == rhs) {
/*  612 */       return this;
/*      */     }
/*  614 */     if (lhs == null) {
/*  615 */       this.comparison = -1;
/*  616 */       return this;
/*      */     } 
/*  618 */     if (rhs == null) {
/*  619 */       this.comparison = 1;
/*  620 */       return this;
/*      */     } 
/*  622 */     if (lhs.length != rhs.length) {
/*  623 */       this.comparison = (lhs.length < rhs.length) ? -1 : 1;
/*  624 */       return this;
/*      */     } 
/*  626 */     for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
/*  627 */       append(lhs[i], rhs[i]);
/*      */     }
/*  629 */     return this;
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
/*      */   public CompareToBuilder append(int lhs, int rhs) {
/*  641 */     if (this.comparison != 0) {
/*  642 */       return this;
/*      */     }
/*  644 */     this.comparison = Integer.compare(lhs, rhs);
/*  645 */     return this;
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
/*      */   public CompareToBuilder append(int[] lhs, int[] rhs) {
/*  664 */     if (this.comparison != 0) {
/*  665 */       return this;
/*      */     }
/*  667 */     if (lhs == rhs) {
/*  668 */       return this;
/*      */     }
/*  670 */     if (lhs == null) {
/*  671 */       this.comparison = -1;
/*  672 */       return this;
/*      */     } 
/*  674 */     if (rhs == null) {
/*  675 */       this.comparison = 1;
/*  676 */       return this;
/*      */     } 
/*  678 */     if (lhs.length != rhs.length) {
/*  679 */       this.comparison = (lhs.length < rhs.length) ? -1 : 1;
/*  680 */       return this;
/*      */     } 
/*  682 */     for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
/*  683 */       append(lhs[i], rhs[i]);
/*      */     }
/*  685 */     return this;
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
/*      */   public CompareToBuilder append(long lhs, long rhs) {
/*  697 */     if (this.comparison != 0) {
/*  698 */       return this;
/*      */     }
/*  700 */     this.comparison = Long.compare(lhs, rhs);
/*  701 */     return this;
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
/*      */   public CompareToBuilder append(long[] lhs, long[] rhs) {
/*  720 */     if (this.comparison != 0) {
/*  721 */       return this;
/*      */     }
/*  723 */     if (lhs == rhs) {
/*  724 */       return this;
/*      */     }
/*  726 */     if (lhs == null) {
/*  727 */       this.comparison = -1;
/*  728 */       return this;
/*      */     } 
/*  730 */     if (rhs == null) {
/*  731 */       this.comparison = 1;
/*  732 */       return this;
/*      */     } 
/*  734 */     if (lhs.length != rhs.length) {
/*  735 */       this.comparison = (lhs.length < rhs.length) ? -1 : 1;
/*  736 */       return this;
/*      */     } 
/*  738 */     for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
/*  739 */       append(lhs[i], rhs[i]);
/*      */     }
/*  741 */     return this;
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
/*      */   public CompareToBuilder append(Object lhs, Object rhs) {
/*  764 */     return append(lhs, rhs, (Comparator<?>)null);
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
/*      */   public CompareToBuilder append(Object lhs, Object rhs, Comparator<?> comparator) {
/*  793 */     if (this.comparison != 0) {
/*  794 */       return this;
/*      */     }
/*  796 */     if (lhs == rhs) {
/*  797 */       return this;
/*      */     }
/*  799 */     if (lhs == null) {
/*  800 */       this.comparison = -1;
/*  801 */       return this;
/*      */     } 
/*  803 */     if (rhs == null) {
/*  804 */       this.comparison = 1;
/*  805 */       return this;
/*      */     } 
/*  807 */     if (ObjectUtils.isArray(lhs)) {
/*      */       
/*  809 */       appendArray(lhs, rhs, comparator);
/*      */     }
/*  811 */     else if (comparator == null) {
/*      */       
/*  813 */       Comparable<Object> comparable = (Comparable<Object>)lhs;
/*  814 */       this.comparison = comparable.compareTo(rhs);
/*      */     } else {
/*      */       
/*  817 */       Comparator<Object> comparator2 = (Comparator)comparator;
/*  818 */       this.comparison = comparator2.compare(lhs, rhs);
/*      */     } 
/*  820 */     return this;
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
/*      */   public CompareToBuilder append(Object[] lhs, Object[] rhs) {
/*  844 */     return append(lhs, rhs, (Comparator<?>)null);
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
/*      */   public CompareToBuilder append(Object[] lhs, Object[] rhs, Comparator<?> comparator) {
/*  871 */     if (this.comparison != 0) {
/*  872 */       return this;
/*      */     }
/*  874 */     if (lhs == rhs) {
/*  875 */       return this;
/*      */     }
/*  877 */     if (lhs == null) {
/*  878 */       this.comparison = -1;
/*  879 */       return this;
/*      */     } 
/*  881 */     if (rhs == null) {
/*  882 */       this.comparison = 1;
/*  883 */       return this;
/*      */     } 
/*  885 */     if (lhs.length != rhs.length) {
/*  886 */       this.comparison = (lhs.length < rhs.length) ? -1 : 1;
/*  887 */       return this;
/*      */     } 
/*  889 */     for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
/*  890 */       append(lhs[i], rhs[i], comparator);
/*      */     }
/*  892 */     return this;
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
/*      */   public CompareToBuilder append(short lhs, short rhs) {
/*  904 */     if (this.comparison != 0) {
/*  905 */       return this;
/*      */     }
/*  907 */     this.comparison = Short.compare(lhs, rhs);
/*  908 */     return this;
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
/*      */   public CompareToBuilder append(short[] lhs, short[] rhs) {
/*  927 */     if (this.comparison != 0) {
/*  928 */       return this;
/*      */     }
/*  930 */     if (lhs == rhs) {
/*  931 */       return this;
/*      */     }
/*  933 */     if (lhs == null) {
/*  934 */       this.comparison = -1;
/*  935 */       return this;
/*      */     } 
/*  937 */     if (rhs == null) {
/*  938 */       this.comparison = 1;
/*  939 */       return this;
/*      */     } 
/*  941 */     if (lhs.length != rhs.length) {
/*  942 */       this.comparison = (lhs.length < rhs.length) ? -1 : 1;
/*  943 */       return this;
/*      */     } 
/*  945 */     for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
/*  946 */       append(lhs[i], rhs[i]);
/*      */     }
/*  948 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void appendArray(Object lhs, Object rhs, Comparator<?> comparator) {
/*  955 */     if (lhs instanceof long[]) {
/*  956 */       append((long[])lhs, (long[])rhs);
/*  957 */     } else if (lhs instanceof int[]) {
/*  958 */       append((int[])lhs, (int[])rhs);
/*  959 */     } else if (lhs instanceof short[]) {
/*  960 */       append((short[])lhs, (short[])rhs);
/*  961 */     } else if (lhs instanceof char[]) {
/*  962 */       append((char[])lhs, (char[])rhs);
/*  963 */     } else if (lhs instanceof byte[]) {
/*  964 */       append((byte[])lhs, (byte[])rhs);
/*  965 */     } else if (lhs instanceof double[]) {
/*  966 */       append((double[])lhs, (double[])rhs);
/*  967 */     } else if (lhs instanceof float[]) {
/*  968 */       append((float[])lhs, (float[])rhs);
/*  969 */     } else if (lhs instanceof boolean[]) {
/*  970 */       append((boolean[])lhs, (boolean[])rhs);
/*      */     }
/*      */     else {
/*      */       
/*  974 */       append((Object[])lhs, (Object[])rhs, comparator);
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
/*      */   public CompareToBuilder appendSuper(int superCompareTo) {
/*  987 */     if (this.comparison != 0) {
/*  988 */       return this;
/*      */     }
/*  990 */     this.comparison = superCompareTo;
/*  991 */     return this;
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
/*      */   public Integer build() {
/* 1006 */     return Integer.valueOf(toComparison());
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
/*      */   public int toComparison() {
/* 1019 */     return this.comparison;
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\builder\CompareToBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */