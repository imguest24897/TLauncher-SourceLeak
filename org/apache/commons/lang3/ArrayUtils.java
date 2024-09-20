/*      */ package org.apache.commons.lang3;
/*      */ 
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Type;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Random;
/*      */ import java.util.concurrent.ThreadLocalRandom;
/*      */ import java.util.function.IntFunction;
/*      */ import java.util.function.Supplier;
/*      */ import org.apache.commons.lang3.builder.EqualsBuilder;
/*      */ import org.apache.commons.lang3.builder.HashCodeBuilder;
/*      */ import org.apache.commons.lang3.builder.ToStringBuilder;
/*      */ import org.apache.commons.lang3.builder.ToStringStyle;
/*      */ import org.apache.commons.lang3.math.NumberUtils;
/*      */ import org.apache.commons.lang3.mutable.MutableInt;
/*      */ import org.apache.commons.lang3.stream.Streams;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ArrayUtils
/*      */ {
/*   62 */   public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   67 */   public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   72 */   public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   77 */   public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   82 */   public static final char[] EMPTY_CHAR_ARRAY = new char[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   87 */   public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   92 */   public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   97 */   public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  102 */   public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = new Double[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  109 */   public static final Field[] EMPTY_FIELD_ARRAY = new Field[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  114 */   public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  119 */   public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  124 */   public static final int[] EMPTY_INT_ARRAY = new int[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  129 */   public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  134 */   public static final long[] EMPTY_LONG_ARRAY = new long[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  139 */   public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  146 */   public static final Method[] EMPTY_METHOD_ARRAY = new Method[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  151 */   public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  156 */   public static final short[] EMPTY_SHORT_ARRAY = new short[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  161 */   public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  166 */   public static final String[] EMPTY_STRING_ARRAY = new String[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  173 */   public static final Throwable[] EMPTY_THROWABLE_ARRAY = new Throwable[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  180 */   public static final Type[] EMPTY_TYPE_ARRAY = new Type[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int INDEX_NOT_FOUND = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] add(boolean[] array, boolean element) {
/*  212 */     boolean[] newArray = (boolean[])copyArrayGrow1(array, boolean.class);
/*  213 */     newArray[newArray.length - 1] = element;
/*  214 */     return newArray;
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
/*      */   @Deprecated
/*      */   public static boolean[] add(boolean[] array, int index, boolean element) {
/*  249 */     return (boolean[])add(array, index, Boolean.valueOf(element), boolean.class);
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
/*      */   public static byte[] add(byte[] array, byte element) {
/*  275 */     byte[] newArray = (byte[])copyArrayGrow1(array, byte.class);
/*  276 */     newArray[newArray.length - 1] = element;
/*  277 */     return newArray;
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
/*      */   @Deprecated
/*      */   public static byte[] add(byte[] array, int index, byte element) {
/*  313 */     return (byte[])add(array, index, Byte.valueOf(element), byte.class);
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
/*      */   public static char[] add(char[] array, char element) {
/*  339 */     char[] newArray = (char[])copyArrayGrow1(array, char.class);
/*  340 */     newArray[newArray.length - 1] = element;
/*  341 */     return newArray;
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
/*      */   @Deprecated
/*      */   public static char[] add(char[] array, int index, char element) {
/*  378 */     return (char[])add(array, index, Character.valueOf(element), char.class);
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
/*      */   public static double[] add(double[] array, double element) {
/*  405 */     double[] newArray = (double[])copyArrayGrow1(array, double.class);
/*  406 */     newArray[newArray.length - 1] = element;
/*  407 */     return newArray;
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
/*      */   @Deprecated
/*      */   public static double[] add(double[] array, int index, double element) {
/*  443 */     return (double[])add(array, index, Double.valueOf(element), double.class);
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
/*      */   public static float[] add(float[] array, float element) {
/*  469 */     float[] newArray = (float[])copyArrayGrow1(array, float.class);
/*  470 */     newArray[newArray.length - 1] = element;
/*  471 */     return newArray;
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
/*      */   @Deprecated
/*      */   public static float[] add(float[] array, int index, float element) {
/*  507 */     return (float[])add(array, index, Float.valueOf(element), float.class);
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
/*      */   public static int[] add(int[] array, int element) {
/*  533 */     int[] newArray = (int[])copyArrayGrow1(array, int.class);
/*  534 */     newArray[newArray.length - 1] = element;
/*  535 */     return newArray;
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
/*      */   @Deprecated
/*      */   public static int[] add(int[] array, int index, int element) {
/*  571 */     return (int[])add(array, index, Integer.valueOf(element), int.class);
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
/*      */   @Deprecated
/*      */   public static long[] add(long[] array, int index, long element) {
/*  607 */     return (long[])add(array, index, Long.valueOf(element), long.class);
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
/*      */   public static long[] add(long[] array, long element) {
/*  633 */     long[] newArray = (long[])copyArrayGrow1(array, long.class);
/*  634 */     newArray[newArray.length - 1] = element;
/*  635 */     return newArray;
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
/*      */   private static Object add(Object array, int index, Object element, Class<?> clss) {
/*  650 */     if (array == null) {
/*  651 */       if (index != 0) {
/*  652 */         throw new IndexOutOfBoundsException("Index: " + index + ", Length: 0");
/*      */       }
/*  654 */       Object joinedArray = Array.newInstance(clss, 1);
/*  655 */       Array.set(joinedArray, 0, element);
/*  656 */       return joinedArray;
/*      */     } 
/*  658 */     int length = Array.getLength(array);
/*  659 */     if (index > length || index < 0) {
/*  660 */       throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
/*      */     }
/*  662 */     Object result = Array.newInstance(clss, length + 1);
/*  663 */     System.arraycopy(array, 0, result, 0, index);
/*  664 */     Array.set(result, index, element);
/*  665 */     if (index < length) {
/*  666 */       System.arraycopy(array, index, result, index + 1, length - index);
/*      */     }
/*  668 */     return result;
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
/*      */   @Deprecated
/*      */   public static short[] add(short[] array, int index, short element) {
/*  704 */     return (short[])add(array, index, Short.valueOf(element), short.class);
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
/*      */   public static short[] add(short[] array, short element) {
/*  730 */     short[] newArray = (short[])copyArrayGrow1(array, short.class);
/*  731 */     newArray[newArray.length - 1] = element;
/*  732 */     return newArray;
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
/*      */   @Deprecated
/*      */   public static <T> T[] add(T[] array, int index, T element) {
/*      */     Class<T> clss;
/*  771 */     if (array != null) {
/*  772 */       clss = getComponentType(array);
/*  773 */     } else if (element != null) {
/*  774 */       clss = ObjectUtils.getClass(element);
/*      */     } else {
/*  776 */       throw new IllegalArgumentException("Array and element cannot both be null");
/*      */     } 
/*  778 */     return (T[])add(array, index, element, clss);
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
/*      */   public static <T> T[] add(T[] array, T element) {
/*      */     Class<?> type;
/*  814 */     if (array != null) {
/*  815 */       type = array.getClass().getComponentType();
/*  816 */     } else if (element != null) {
/*  817 */       type = element.getClass();
/*      */     } else {
/*  819 */       throw new IllegalArgumentException("Arguments cannot both be null");
/*      */     } 
/*      */ 
/*      */     
/*  823 */     T[] newArray = (T[])copyArrayGrow1(array, type);
/*  824 */     newArray[newArray.length - 1] = element;
/*  825 */     return newArray;
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
/*      */   public static boolean[] addAll(boolean[] array1, boolean... array2) {
/*  847 */     if (array1 == null) {
/*  848 */       return clone(array2);
/*      */     }
/*  850 */     if (array2 == null) {
/*  851 */       return clone(array1);
/*      */     }
/*  853 */     boolean[] joinedArray = new boolean[array1.length + array2.length];
/*  854 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/*  855 */     System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/*  856 */     return joinedArray;
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
/*      */   public static byte[] addAll(byte[] array1, byte... array2) {
/*  878 */     if (array1 == null) {
/*  879 */       return clone(array2);
/*      */     }
/*  881 */     if (array2 == null) {
/*  882 */       return clone(array1);
/*      */     }
/*  884 */     byte[] joinedArray = new byte[array1.length + array2.length];
/*  885 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/*  886 */     System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/*  887 */     return joinedArray;
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
/*      */   public static char[] addAll(char[] array1, char... array2) {
/*  909 */     if (array1 == null) {
/*  910 */       return clone(array2);
/*      */     }
/*  912 */     if (array2 == null) {
/*  913 */       return clone(array1);
/*      */     }
/*  915 */     char[] joinedArray = new char[array1.length + array2.length];
/*  916 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/*  917 */     System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/*  918 */     return joinedArray;
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
/*      */   public static double[] addAll(double[] array1, double... array2) {
/*  940 */     if (array1 == null) {
/*  941 */       return clone(array2);
/*      */     }
/*  943 */     if (array2 == null) {
/*  944 */       return clone(array1);
/*      */     }
/*  946 */     double[] joinedArray = new double[array1.length + array2.length];
/*  947 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/*  948 */     System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/*  949 */     return joinedArray;
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
/*      */   public static float[] addAll(float[] array1, float... array2) {
/*  971 */     if (array1 == null) {
/*  972 */       return clone(array2);
/*      */     }
/*  974 */     if (array2 == null) {
/*  975 */       return clone(array1);
/*      */     }
/*  977 */     float[] joinedArray = new float[array1.length + array2.length];
/*  978 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/*  979 */     System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/*  980 */     return joinedArray;
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
/*      */   public static int[] addAll(int[] array1, int... array2) {
/* 1002 */     if (array1 == null) {
/* 1003 */       return clone(array2);
/*      */     }
/* 1005 */     if (array2 == null) {
/* 1006 */       return clone(array1);
/*      */     }
/* 1008 */     int[] joinedArray = new int[array1.length + array2.length];
/* 1009 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/* 1010 */     System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/* 1011 */     return joinedArray;
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
/*      */   public static long[] addAll(long[] array1, long... array2) {
/* 1033 */     if (array1 == null) {
/* 1034 */       return clone(array2);
/*      */     }
/* 1036 */     if (array2 == null) {
/* 1037 */       return clone(array1);
/*      */     }
/* 1039 */     long[] joinedArray = new long[array1.length + array2.length];
/* 1040 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/* 1041 */     System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/* 1042 */     return joinedArray;
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
/*      */   public static short[] addAll(short[] array1, short... array2) {
/* 1064 */     if (array1 == null) {
/* 1065 */       return clone(array2);
/*      */     }
/* 1067 */     if (array2 == null) {
/* 1068 */       return clone(array1);
/*      */     }
/* 1070 */     short[] joinedArray = new short[array1.length + array2.length];
/* 1071 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/* 1072 */     System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/* 1073 */     return joinedArray;
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
/*      */   public static <T> T[] addAll(T[] array1, T... array2) {
/* 1102 */     if (array1 == null) {
/* 1103 */       return clone(array2);
/*      */     }
/* 1105 */     if (array2 == null) {
/* 1106 */       return clone(array1);
/*      */     }
/* 1108 */     Class<T> type1 = getComponentType(array1);
/* 1109 */     T[] joinedArray = newInstance(type1, array1.length + array2.length);
/* 1110 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/*      */     try {
/* 1112 */       System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/* 1113 */     } catch (ArrayStoreException ase) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1120 */       Class<?> type2 = array2.getClass().getComponentType();
/* 1121 */       if (!type1.isAssignableFrom(type2)) {
/* 1122 */         throw new IllegalArgumentException("Cannot store " + type2.getName() + " in an array of " + type1
/* 1123 */             .getName(), ase);
/*      */       }
/* 1125 */       throw ase;
/*      */     } 
/* 1127 */     return joinedArray;
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
/*      */   public static boolean[] addFirst(boolean[] array, boolean element) {
/* 1153 */     return (array == null) ? add(array, element) : insert(0, array, new boolean[] { element });
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
/*      */   public static byte[] addFirst(byte[] array, byte element) {
/* 1179 */     return (array == null) ? add(array, element) : insert(0, array, new byte[] { element });
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
/*      */   public static char[] addFirst(char[] array, char element) {
/* 1205 */     return (array == null) ? add(array, element) : insert(0, array, new char[] { element });
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
/*      */   public static double[] addFirst(double[] array, double element) {
/* 1231 */     return (array == null) ? add(array, element) : insert(0, array, new double[] { element });
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
/*      */   public static float[] addFirst(float[] array, float element) {
/* 1257 */     return (array == null) ? add(array, element) : insert(0, array, new float[] { element });
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
/*      */   public static int[] addFirst(int[] array, int element) {
/* 1283 */     return (array == null) ? add(array, element) : insert(0, array, new int[] { element });
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
/*      */   public static long[] addFirst(long[] array, long element) {
/* 1309 */     return (array == null) ? add(array, element) : insert(0, array, new long[] { element });
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
/*      */   public static short[] addFirst(short[] array, short element) {
/* 1335 */     return (array == null) ? add(array, element) : insert(0, array, new short[] { element });
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
/*      */   public static <T> T[] addFirst(T[] array, T element) {
/* 1366 */     return (array == null) ? add(array, element) : insert(0, array, (T[])new Object[] { element });
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
/*      */   public static boolean[] clone(boolean[] array) {
/* 1380 */     return (array != null) ? (boolean[])array.clone() : null;
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
/*      */   public static byte[] clone(byte[] array) {
/* 1394 */     return (array != null) ? (byte[])array.clone() : null;
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
/*      */   public static char[] clone(char[] array) {
/* 1408 */     return (array != null) ? (char[])array.clone() : null;
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
/*      */   public static double[] clone(double[] array) {
/* 1422 */     return (array != null) ? (double[])array.clone() : null;
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
/*      */   public static float[] clone(float[] array) {
/* 1436 */     return (array != null) ? (float[])array.clone() : null;
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
/*      */   public static int[] clone(int[] array) {
/* 1450 */     return (array != null) ? (int[])array.clone() : null;
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
/*      */   public static long[] clone(long[] array) {
/* 1464 */     return (array != null) ? (long[])array.clone() : null;
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
/*      */   public static short[] clone(short[] array) {
/* 1478 */     return (array != null) ? (short[])array.clone() : null;
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
/*      */   public static <T> T[] clone(T[] array) {
/* 1497 */     return (array != null) ? (T[])array.clone() : null;
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
/*      */   public static boolean contains(boolean[] array, boolean valueToFind) {
/* 1511 */     return (indexOf(array, valueToFind) != -1);
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
/*      */   public static boolean contains(byte[] array, byte valueToFind) {
/* 1525 */     return (indexOf(array, valueToFind) != -1);
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
/*      */   public static boolean contains(char[] array, char valueToFind) {
/* 1540 */     return (indexOf(array, valueToFind) != -1);
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
/*      */   public static boolean contains(double[] array, double valueToFind) {
/* 1554 */     return (indexOf(array, valueToFind) != -1);
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
/*      */   public static boolean contains(double[] array, double valueToFind, double tolerance) {
/* 1572 */     return (indexOf(array, valueToFind, 0, tolerance) != -1);
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
/*      */   public static boolean contains(float[] array, float valueToFind) {
/* 1586 */     return (indexOf(array, valueToFind) != -1);
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
/*      */   public static boolean contains(int[] array, int valueToFind) {
/* 1600 */     return (indexOf(array, valueToFind) != -1);
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
/*      */   public static boolean contains(long[] array, long valueToFind) {
/* 1614 */     return (indexOf(array, valueToFind) != -1);
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
/*      */   public static boolean contains(Object[] array, Object objectToFind) {
/* 1628 */     return (indexOf(array, objectToFind) != -1);
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
/*      */   public static boolean contains(short[] array, short valueToFind) {
/* 1642 */     return (indexOf(array, valueToFind) != -1);
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
/*      */   public static boolean containsAny(Object[] array, Object... objectsToFind) {
/* 1657 */     return Streams.of(objectsToFind).anyMatch(e -> contains(array, e));
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
/*      */   private static Object copyArrayGrow1(Object array, Class<?> newArrayComponentType) {
/* 1670 */     if (array != null) {
/* 1671 */       int arrayLength = Array.getLength(array);
/* 1672 */       Object newArray = Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
/* 1673 */       System.arraycopy(array, 0, newArray, 0, arrayLength);
/* 1674 */       return newArray;
/*      */     } 
/* 1676 */     return Array.newInstance(newArrayComponentType, 1);
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
/*      */   public static <T> T get(T[] array, int index) {
/* 1689 */     return get(array, index, null);
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
/*      */   public static <T> T get(T[] array, int index, T defaultValue) {
/* 1703 */     return isArrayIndexValid(array, index) ? array[index] : defaultValue;
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
/*      */   public static <T> Class<T> getComponentType(T[] array) {
/* 1715 */     return ClassUtils.getComponentType(ObjectUtils.getClass(array));
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
/*      */   public static int getLength(Object array) {
/* 1739 */     return (array != null) ? Array.getLength(array) : 0;
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
/*      */   public static int hashCode(Object array) {
/* 1752 */     return (new HashCodeBuilder()).append(array).toHashCode();
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
/*      */   public static BitSet indexesOf(boolean[] array, boolean valueToFind) {
/* 1768 */     return indexesOf(array, valueToFind, 0);
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
/*      */   public static BitSet indexesOf(boolean[] array, boolean valueToFind, int startIndex) {
/* 1790 */     BitSet bitSet = new BitSet();
/*      */     
/* 1792 */     if (array == null) {
/* 1793 */       return bitSet;
/*      */     }
/*      */     
/* 1796 */     while (startIndex < array.length) {
/* 1797 */       startIndex = indexOf(array, valueToFind, startIndex);
/*      */       
/* 1799 */       if (startIndex == -1) {
/*      */         break;
/*      */       }
/*      */       
/* 1803 */       bitSet.set(startIndex);
/* 1804 */       startIndex++;
/*      */     } 
/*      */     
/* 1807 */     return bitSet;
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
/*      */   public static BitSet indexesOf(byte[] array, byte valueToFind) {
/* 1822 */     return indexesOf(array, valueToFind, 0);
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
/*      */   public static BitSet indexesOf(byte[] array, byte valueToFind, int startIndex) {
/* 1841 */     BitSet bitSet = new BitSet();
/*      */     
/* 1843 */     if (array == null) {
/* 1844 */       return bitSet;
/*      */     }
/*      */     
/* 1847 */     while (startIndex < array.length) {
/* 1848 */       startIndex = indexOf(array, valueToFind, startIndex);
/*      */       
/* 1850 */       if (startIndex == -1) {
/*      */         break;
/*      */       }
/*      */       
/* 1854 */       bitSet.set(startIndex);
/* 1855 */       startIndex++;
/*      */     } 
/*      */     
/* 1858 */     return bitSet;
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
/*      */   public static BitSet indexesOf(char[] array, char valueToFind) {
/* 1873 */     return indexesOf(array, valueToFind, 0);
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
/*      */   public static BitSet indexesOf(char[] array, char valueToFind, int startIndex) {
/* 1892 */     BitSet bitSet = new BitSet();
/*      */     
/* 1894 */     if (array == null) {
/* 1895 */       return bitSet;
/*      */     }
/*      */     
/* 1898 */     while (startIndex < array.length) {
/* 1899 */       startIndex = indexOf(array, valueToFind, startIndex);
/*      */       
/* 1901 */       if (startIndex == -1) {
/*      */         break;
/*      */       }
/*      */       
/* 1905 */       bitSet.set(startIndex);
/* 1906 */       startIndex++;
/*      */     } 
/*      */     
/* 1909 */     return bitSet;
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
/*      */   public static BitSet indexesOf(double[] array, double valueToFind) {
/* 1924 */     return indexesOf(array, valueToFind, 0);
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
/*      */   public static BitSet indexesOf(double[] array, double valueToFind, double tolerance) {
/* 1945 */     return indexesOf(array, valueToFind, 0, tolerance);
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
/*      */   public static BitSet indexesOf(double[] array, double valueToFind, int startIndex) {
/* 1964 */     BitSet bitSet = new BitSet();
/*      */     
/* 1966 */     if (array == null) {
/* 1967 */       return bitSet;
/*      */     }
/*      */     
/* 1970 */     while (startIndex < array.length) {
/* 1971 */       startIndex = indexOf(array, valueToFind, startIndex);
/*      */       
/* 1973 */       if (startIndex == -1) {
/*      */         break;
/*      */       }
/*      */       
/* 1977 */       bitSet.set(startIndex);
/* 1978 */       startIndex++;
/*      */     } 
/*      */     
/* 1981 */     return bitSet;
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
/*      */   public static BitSet indexesOf(double[] array, double valueToFind, int startIndex, double tolerance) {
/* 2006 */     BitSet bitSet = new BitSet();
/*      */     
/* 2008 */     if (array == null) {
/* 2009 */       return bitSet;
/*      */     }
/*      */     
/* 2012 */     while (startIndex < array.length) {
/* 2013 */       startIndex = indexOf(array, valueToFind, startIndex, tolerance);
/*      */       
/* 2015 */       if (startIndex == -1) {
/*      */         break;
/*      */       }
/*      */       
/* 2019 */       bitSet.set(startIndex);
/* 2020 */       startIndex++;
/*      */     } 
/*      */     
/* 2023 */     return bitSet;
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
/*      */   public static BitSet indexesOf(float[] array, float valueToFind) {
/* 2038 */     return indexesOf(array, valueToFind, 0);
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
/*      */   public static BitSet indexesOf(float[] array, float valueToFind, int startIndex) {
/* 2057 */     BitSet bitSet = new BitSet();
/*      */     
/* 2059 */     if (array == null) {
/* 2060 */       return bitSet;
/*      */     }
/*      */     
/* 2063 */     while (startIndex < array.length) {
/* 2064 */       startIndex = indexOf(array, valueToFind, startIndex);
/*      */       
/* 2066 */       if (startIndex == -1) {
/*      */         break;
/*      */       }
/*      */       
/* 2070 */       bitSet.set(startIndex);
/* 2071 */       startIndex++;
/*      */     } 
/*      */     
/* 2074 */     return bitSet;
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
/*      */   public static BitSet indexesOf(int[] array, int valueToFind) {
/* 2089 */     return indexesOf(array, valueToFind, 0);
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
/*      */   public static BitSet indexesOf(int[] array, int valueToFind, int startIndex) {
/* 2108 */     BitSet bitSet = new BitSet();
/*      */     
/* 2110 */     if (array == null) {
/* 2111 */       return bitSet;
/*      */     }
/*      */     
/* 2114 */     while (startIndex < array.length) {
/* 2115 */       startIndex = indexOf(array, valueToFind, startIndex);
/*      */       
/* 2117 */       if (startIndex == -1) {
/*      */         break;
/*      */       }
/*      */       
/* 2121 */       bitSet.set(startIndex);
/* 2122 */       startIndex++;
/*      */     } 
/*      */     
/* 2125 */     return bitSet;
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
/*      */   public static BitSet indexesOf(long[] array, long valueToFind) {
/* 2140 */     return indexesOf(array, valueToFind, 0);
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
/*      */   public static BitSet indexesOf(long[] array, long valueToFind, int startIndex) {
/* 2159 */     BitSet bitSet = new BitSet();
/*      */     
/* 2161 */     if (array == null) {
/* 2162 */       return bitSet;
/*      */     }
/*      */     
/* 2165 */     while (startIndex < array.length) {
/* 2166 */       startIndex = indexOf(array, valueToFind, startIndex);
/*      */       
/* 2168 */       if (startIndex == -1) {
/*      */         break;
/*      */       }
/*      */       
/* 2172 */       bitSet.set(startIndex);
/* 2173 */       startIndex++;
/*      */     } 
/*      */     
/* 2176 */     return bitSet;
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
/*      */   public static BitSet indexesOf(Object[] array, Object objectToFind) {
/* 2191 */     return indexesOf(array, objectToFind, 0);
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
/*      */   public static BitSet indexesOf(Object[] array, Object objectToFind, int startIndex) {
/* 2210 */     BitSet bitSet = new BitSet();
/*      */     
/* 2212 */     if (array == null) {
/* 2213 */       return bitSet;
/*      */     }
/*      */     
/* 2216 */     while (startIndex < array.length) {
/* 2217 */       startIndex = indexOf(array, objectToFind, startIndex);
/*      */       
/* 2219 */       if (startIndex == -1) {
/*      */         break;
/*      */       }
/*      */       
/* 2223 */       bitSet.set(startIndex);
/* 2224 */       startIndex++;
/*      */     } 
/*      */     
/* 2227 */     return bitSet;
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
/*      */   public static BitSet indexesOf(short[] array, short valueToFind) {
/* 2242 */     return indexesOf(array, valueToFind, 0);
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
/*      */   public static BitSet indexesOf(short[] array, short valueToFind, int startIndex) {
/* 2261 */     BitSet bitSet = new BitSet();
/*      */     
/* 2263 */     if (array == null) {
/* 2264 */       return bitSet;
/*      */     }
/*      */     
/* 2267 */     while (startIndex < array.length) {
/* 2268 */       startIndex = indexOf(array, valueToFind, startIndex);
/*      */       
/* 2270 */       if (startIndex == -1) {
/*      */         break;
/*      */       }
/*      */       
/* 2274 */       bitSet.set(startIndex);
/* 2275 */       startIndex++;
/*      */     } 
/*      */     
/* 2278 */     return bitSet;
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
/*      */   public static int indexOf(boolean[] array, boolean valueToFind) {
/* 2293 */     return indexOf(array, valueToFind, 0);
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
/*      */   public static int indexOf(boolean[] array, boolean valueToFind, int startIndex) {
/* 2314 */     if (isEmpty(array)) {
/* 2315 */       return -1;
/*      */     }
/* 2317 */     if (startIndex < 0) {
/* 2318 */       startIndex = 0;
/*      */     }
/* 2320 */     for (int i = startIndex; i < array.length; i++) {
/* 2321 */       if (valueToFind == array[i]) {
/* 2322 */         return i;
/*      */       }
/*      */     } 
/* 2325 */     return -1;
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
/*      */   public static int indexOf(byte[] array, byte valueToFind) {
/* 2340 */     return indexOf(array, valueToFind, 0);
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
/*      */   public static int indexOf(byte[] array, byte valueToFind, int startIndex) {
/* 2360 */     if (array == null) {
/* 2361 */       return -1;
/*      */     }
/* 2363 */     if (startIndex < 0) {
/* 2364 */       startIndex = 0;
/*      */     }
/* 2366 */     for (int i = startIndex; i < array.length; i++) {
/* 2367 */       if (valueToFind == array[i]) {
/* 2368 */         return i;
/*      */       }
/*      */     } 
/* 2371 */     return -1;
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
/*      */   public static int indexOf(char[] array, char valueToFind) {
/* 2387 */     return indexOf(array, valueToFind, 0);
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
/*      */   public static int indexOf(char[] array, char valueToFind, int startIndex) {
/* 2408 */     if (array == null) {
/* 2409 */       return -1;
/*      */     }
/* 2411 */     if (startIndex < 0) {
/* 2412 */       startIndex = 0;
/*      */     }
/* 2414 */     for (int i = startIndex; i < array.length; i++) {
/* 2415 */       if (valueToFind == array[i]) {
/* 2416 */         return i;
/*      */       }
/*      */     } 
/* 2419 */     return -1;
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
/*      */   public static int indexOf(double[] array, double valueToFind) {
/* 2434 */     return indexOf(array, valueToFind, 0);
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
/*      */   public static int indexOf(double[] array, double valueToFind, double tolerance) {
/* 2452 */     return indexOf(array, valueToFind, 0, tolerance);
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
/*      */   public static int indexOf(double[] array, double valueToFind, int startIndex) {
/* 2472 */     if (isEmpty(array)) {
/* 2473 */       return -1;
/*      */     }
/* 2475 */     if (startIndex < 0) {
/* 2476 */       startIndex = 0;
/*      */     }
/* 2478 */     boolean searchNaN = Double.isNaN(valueToFind);
/* 2479 */     for (int i = startIndex; i < array.length; i++) {
/* 2480 */       double element = array[i];
/* 2481 */       if (valueToFind == element || (searchNaN && Double.isNaN(element))) {
/* 2482 */         return i;
/*      */       }
/*      */     } 
/* 2485 */     return -1;
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
/*      */   public static int indexOf(double[] array, double valueToFind, int startIndex, double tolerance) {
/* 2508 */     if (isEmpty(array)) {
/* 2509 */       return -1;
/*      */     }
/* 2511 */     if (startIndex < 0) {
/* 2512 */       startIndex = 0;
/*      */     }
/* 2514 */     double min = valueToFind - tolerance;
/* 2515 */     double max = valueToFind + tolerance;
/* 2516 */     for (int i = startIndex; i < array.length; i++) {
/* 2517 */       if (array[i] >= min && array[i] <= max) {
/* 2518 */         return i;
/*      */       }
/*      */     } 
/* 2521 */     return -1;
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
/*      */   public static int indexOf(float[] array, float valueToFind) {
/* 2536 */     return indexOf(array, valueToFind, 0);
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
/*      */   public static int indexOf(float[] array, float valueToFind, int startIndex) {
/* 2556 */     if (isEmpty(array)) {
/* 2557 */       return -1;
/*      */     }
/* 2559 */     if (startIndex < 0) {
/* 2560 */       startIndex = 0;
/*      */     }
/* 2562 */     boolean searchNaN = Float.isNaN(valueToFind);
/* 2563 */     for (int i = startIndex; i < array.length; i++) {
/* 2564 */       float element = array[i];
/* 2565 */       if (valueToFind == element || (searchNaN && Float.isNaN(element))) {
/* 2566 */         return i;
/*      */       }
/*      */     } 
/* 2569 */     return -1;
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
/*      */   public static int indexOf(int[] array, int valueToFind) {
/* 2584 */     return indexOf(array, valueToFind, 0);
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
/*      */   public static int indexOf(int[] array, int valueToFind, int startIndex) {
/* 2604 */     if (array == null) {
/* 2605 */       return -1;
/*      */     }
/* 2607 */     if (startIndex < 0) {
/* 2608 */       startIndex = 0;
/*      */     }
/* 2610 */     for (int i = startIndex; i < array.length; i++) {
/* 2611 */       if (valueToFind == array[i]) {
/* 2612 */         return i;
/*      */       }
/*      */     } 
/* 2615 */     return -1;
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
/*      */   public static int indexOf(long[] array, long valueToFind) {
/* 2630 */     return indexOf(array, valueToFind, 0);
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
/*      */   public static int indexOf(long[] array, long valueToFind, int startIndex) {
/* 2650 */     if (array == null) {
/* 2651 */       return -1;
/*      */     }
/* 2653 */     if (startIndex < 0) {
/* 2654 */       startIndex = 0;
/*      */     }
/* 2656 */     for (int i = startIndex; i < array.length; i++) {
/* 2657 */       if (valueToFind == array[i]) {
/* 2658 */         return i;
/*      */       }
/*      */     } 
/* 2661 */     return -1;
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
/*      */   public static int indexOf(Object[] array, Object objectToFind) {
/* 2676 */     return indexOf(array, objectToFind, 0);
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
/*      */   public static int indexOf(Object[] array, Object objectToFind, int startIndex) {
/* 2696 */     if (array == null) {
/* 2697 */       return -1;
/*      */     }
/* 2699 */     if (startIndex < 0) {
/* 2700 */       startIndex = 0;
/*      */     }
/* 2702 */     if (objectToFind == null) {
/* 2703 */       for (int i = startIndex; i < array.length; i++) {
/* 2704 */         if (array[i] == null) {
/* 2705 */           return i;
/*      */         }
/*      */       } 
/*      */     } else {
/* 2709 */       for (int i = startIndex; i < array.length; i++) {
/* 2710 */         if (objectToFind.equals(array[i])) {
/* 2711 */           return i;
/*      */         }
/*      */       } 
/*      */     } 
/* 2715 */     return -1;
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
/*      */   public static int indexOf(short[] array, short valueToFind) {
/* 2730 */     return indexOf(array, valueToFind, 0);
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
/*      */   public static int indexOf(short[] array, short valueToFind, int startIndex) {
/* 2750 */     if (array == null) {
/* 2751 */       return -1;
/*      */     }
/* 2753 */     if (startIndex < 0) {
/* 2754 */       startIndex = 0;
/*      */     }
/* 2756 */     for (int i = startIndex; i < array.length; i++) {
/* 2757 */       if (valueToFind == array[i]) {
/* 2758 */         return i;
/*      */       }
/*      */     } 
/* 2761 */     return -1;
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
/*      */   public static boolean[] insert(int index, boolean[] array, boolean... values) {
/* 2784 */     if (array == null) {
/* 2785 */       return null;
/*      */     }
/* 2787 */     if (isEmpty(values)) {
/* 2788 */       return clone(array);
/*      */     }
/* 2790 */     if (index < 0 || index > array.length) {
/* 2791 */       throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
/*      */     }
/*      */     
/* 2794 */     boolean[] result = new boolean[array.length + values.length];
/*      */     
/* 2796 */     System.arraycopy(values, 0, result, index, values.length);
/* 2797 */     if (index > 0) {
/* 2798 */       System.arraycopy(array, 0, result, 0, index);
/*      */     }
/* 2800 */     if (index < array.length) {
/* 2801 */       System.arraycopy(array, index, result, index + values.length, array.length - index);
/*      */     }
/* 2803 */     return result;
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
/*      */   public static byte[] insert(int index, byte[] array, byte... values) {
/* 2826 */     if (array == null) {
/* 2827 */       return null;
/*      */     }
/* 2829 */     if (isEmpty(values)) {
/* 2830 */       return clone(array);
/*      */     }
/* 2832 */     if (index < 0 || index > array.length) {
/* 2833 */       throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
/*      */     }
/*      */     
/* 2836 */     byte[] result = new byte[array.length + values.length];
/*      */     
/* 2838 */     System.arraycopy(values, 0, result, index, values.length);
/* 2839 */     if (index > 0) {
/* 2840 */       System.arraycopy(array, 0, result, 0, index);
/*      */     }
/* 2842 */     if (index < array.length) {
/* 2843 */       System.arraycopy(array, index, result, index + values.length, array.length - index);
/*      */     }
/* 2845 */     return result;
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
/*      */   public static char[] insert(int index, char[] array, char... values) {
/* 2868 */     if (array == null) {
/* 2869 */       return null;
/*      */     }
/* 2871 */     if (isEmpty(values)) {
/* 2872 */       return clone(array);
/*      */     }
/* 2874 */     if (index < 0 || index > array.length) {
/* 2875 */       throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
/*      */     }
/*      */     
/* 2878 */     char[] result = new char[array.length + values.length];
/*      */     
/* 2880 */     System.arraycopy(values, 0, result, index, values.length);
/* 2881 */     if (index > 0) {
/* 2882 */       System.arraycopy(array, 0, result, 0, index);
/*      */     }
/* 2884 */     if (index < array.length) {
/* 2885 */       System.arraycopy(array, index, result, index + values.length, array.length - index);
/*      */     }
/* 2887 */     return result;
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
/*      */   public static double[] insert(int index, double[] array, double... values) {
/* 2910 */     if (array == null) {
/* 2911 */       return null;
/*      */     }
/* 2913 */     if (isEmpty(values)) {
/* 2914 */       return clone(array);
/*      */     }
/* 2916 */     if (index < 0 || index > array.length) {
/* 2917 */       throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
/*      */     }
/*      */     
/* 2920 */     double[] result = new double[array.length + values.length];
/*      */     
/* 2922 */     System.arraycopy(values, 0, result, index, values.length);
/* 2923 */     if (index > 0) {
/* 2924 */       System.arraycopy(array, 0, result, 0, index);
/*      */     }
/* 2926 */     if (index < array.length) {
/* 2927 */       System.arraycopy(array, index, result, index + values.length, array.length - index);
/*      */     }
/* 2929 */     return result;
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
/*      */   public static float[] insert(int index, float[] array, float... values) {
/* 2952 */     if (array == null) {
/* 2953 */       return null;
/*      */     }
/* 2955 */     if (isEmpty(values)) {
/* 2956 */       return clone(array);
/*      */     }
/* 2958 */     if (index < 0 || index > array.length) {
/* 2959 */       throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
/*      */     }
/*      */     
/* 2962 */     float[] result = new float[array.length + values.length];
/*      */     
/* 2964 */     System.arraycopy(values, 0, result, index, values.length);
/* 2965 */     if (index > 0) {
/* 2966 */       System.arraycopy(array, 0, result, 0, index);
/*      */     }
/* 2968 */     if (index < array.length) {
/* 2969 */       System.arraycopy(array, index, result, index + values.length, array.length - index);
/*      */     }
/* 2971 */     return result;
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
/*      */   public static int[] insert(int index, int[] array, int... values) {
/* 2994 */     if (array == null) {
/* 2995 */       return null;
/*      */     }
/* 2997 */     if (isEmpty(values)) {
/* 2998 */       return clone(array);
/*      */     }
/* 3000 */     if (index < 0 || index > array.length) {
/* 3001 */       throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
/*      */     }
/*      */     
/* 3004 */     int[] result = new int[array.length + values.length];
/*      */     
/* 3006 */     System.arraycopy(values, 0, result, index, values.length);
/* 3007 */     if (index > 0) {
/* 3008 */       System.arraycopy(array, 0, result, 0, index);
/*      */     }
/* 3010 */     if (index < array.length) {
/* 3011 */       System.arraycopy(array, index, result, index + values.length, array.length - index);
/*      */     }
/* 3013 */     return result;
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
/*      */   public static long[] insert(int index, long[] array, long... values) {
/* 3036 */     if (array == null) {
/* 3037 */       return null;
/*      */     }
/* 3039 */     if (isEmpty(values)) {
/* 3040 */       return clone(array);
/*      */     }
/* 3042 */     if (index < 0 || index > array.length) {
/* 3043 */       throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
/*      */     }
/*      */     
/* 3046 */     long[] result = new long[array.length + values.length];
/*      */     
/* 3048 */     System.arraycopy(values, 0, result, index, values.length);
/* 3049 */     if (index > 0) {
/* 3050 */       System.arraycopy(array, 0, result, 0, index);
/*      */     }
/* 3052 */     if (index < array.length) {
/* 3053 */       System.arraycopy(array, index, result, index + values.length, array.length - index);
/*      */     }
/* 3055 */     return result;
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
/*      */   public static short[] insert(int index, short[] array, short... values) {
/* 3078 */     if (array == null) {
/* 3079 */       return null;
/*      */     }
/* 3081 */     if (isEmpty(values)) {
/* 3082 */       return clone(array);
/*      */     }
/* 3084 */     if (index < 0 || index > array.length) {
/* 3085 */       throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
/*      */     }
/*      */     
/* 3088 */     short[] result = new short[array.length + values.length];
/*      */     
/* 3090 */     System.arraycopy(values, 0, result, index, values.length);
/* 3091 */     if (index > 0) {
/* 3092 */       System.arraycopy(array, 0, result, 0, index);
/*      */     }
/* 3094 */     if (index < array.length) {
/* 3095 */       System.arraycopy(array, index, result, index + values.length, array.length - index);
/*      */     }
/* 3097 */     return result;
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
/*      */   @SafeVarargs
/*      */   public static <T> T[] insert(int index, T[] array, T... values) {
/* 3130 */     if (array == null) {
/* 3131 */       return null;
/*      */     }
/* 3133 */     if (isEmpty((Object[])values)) {
/* 3134 */       return clone(array);
/*      */     }
/* 3136 */     if (index < 0 || index > array.length) {
/* 3137 */       throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
/*      */     }
/*      */     
/* 3140 */     Class<T> type = getComponentType(array);
/* 3141 */     int length = array.length + values.length;
/* 3142 */     T[] result = newInstance(type, length);
/*      */     
/* 3144 */     System.arraycopy(values, 0, result, index, values.length);
/* 3145 */     if (index > 0) {
/* 3146 */       System.arraycopy(array, 0, result, 0, index);
/*      */     }
/* 3148 */     if (index < array.length) {
/* 3149 */       System.arraycopy(array, index, result, index + values.length, array.length - index);
/*      */     }
/* 3151 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isArrayEmpty(Object array) {
/* 3161 */     return (getLength(array) == 0);
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
/*      */   public static <T> boolean isArrayIndexValid(T[] array, int index) {
/* 3180 */     return (index >= 0 && getLength(array) > index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(boolean[] array) {
/* 3191 */     return isArrayEmpty(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(byte[] array) {
/* 3202 */     return isArrayEmpty(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(char[] array) {
/* 3213 */     return isArrayEmpty(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(double[] array) {
/* 3224 */     return isArrayEmpty(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(float[] array) {
/* 3235 */     return isArrayEmpty(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(int[] array) {
/* 3246 */     return isArrayEmpty(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(long[] array) {
/* 3257 */     return isArrayEmpty(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(Object[] array) {
/* 3268 */     return isArrayEmpty(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(short[] array) {
/* 3279 */     return isArrayEmpty(array);
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
/*      */   @Deprecated
/*      */   public static boolean isEquals(Object array1, Object array2) {
/* 3297 */     return (new EqualsBuilder()).append(array1, array2).isEquals();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(boolean[] array) {
/* 3308 */     return !isEmpty(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(byte[] array) {
/* 3319 */     return !isEmpty(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(char[] array) {
/* 3330 */     return !isEmpty(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(double[] array) {
/* 3341 */     return !isEmpty(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(float[] array) {
/* 3352 */     return !isEmpty(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(int[] array) {
/* 3363 */     return !isEmpty(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(long[] array) {
/* 3374 */     return !isEmpty(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(short[] array) {
/* 3385 */     return !isEmpty(array);
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
/*      */   public static <T> boolean isNotEmpty(T[] array) {
/* 3397 */     return !isEmpty((Object[])array);
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
/*      */   public static boolean isSameLength(boolean[] array1, boolean[] array2) {
/* 3410 */     return (getLength(array1) == getLength(array2));
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
/*      */   public static boolean isSameLength(byte[] array1, byte[] array2) {
/* 3423 */     return (getLength(array1) == getLength(array2));
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
/*      */   public static boolean isSameLength(char[] array1, char[] array2) {
/* 3436 */     return (getLength(array1) == getLength(array2));
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
/*      */   public static boolean isSameLength(double[] array1, double[] array2) {
/* 3449 */     return (getLength(array1) == getLength(array2));
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
/*      */   public static boolean isSameLength(float[] array1, float[] array2) {
/* 3462 */     return (getLength(array1) == getLength(array2));
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
/*      */   public static boolean isSameLength(int[] array1, int[] array2) {
/* 3475 */     return (getLength(array1) == getLength(array2));
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
/*      */   public static boolean isSameLength(long[] array1, long[] array2) {
/* 3488 */     return (getLength(array1) == getLength(array2));
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
/*      */   public static boolean isSameLength(Object array1, Object array2) {
/* 3505 */     return (getLength(array1) == getLength(array2));
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
/*      */   public static boolean isSameLength(Object[] array1, Object[] array2) {
/* 3521 */     return (getLength(array1) == getLength(array2));
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
/*      */   public static boolean isSameLength(short[] array1, short[] array2) {
/* 3535 */     return (getLength(array1) == getLength(array2));
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
/*      */   public static boolean isSameType(Object array1, Object array2) {
/* 3548 */     if (array1 == null || array2 == null) {
/* 3549 */       throw new IllegalArgumentException("The Array must not be null");
/*      */     }
/* 3551 */     return array1.getClass().getName().equals(array2.getClass().getName());
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
/*      */   public static boolean isSorted(boolean[] array) {
/* 3563 */     if (getLength(array) < 2) {
/* 3564 */       return true;
/*      */     }
/*      */     
/* 3567 */     boolean previous = array[0];
/* 3568 */     int n = array.length;
/* 3569 */     for (int i = 1; i < n; i++) {
/* 3570 */       boolean current = array[i];
/* 3571 */       if (BooleanUtils.compare(previous, current) > 0) {
/* 3572 */         return false;
/*      */       }
/*      */       
/* 3575 */       previous = current;
/*      */     } 
/* 3577 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSorted(byte[] array) {
/* 3588 */     if (getLength(array) < 2) {
/* 3589 */       return true;
/*      */     }
/*      */     
/* 3592 */     byte previous = array[0];
/* 3593 */     int n = array.length;
/* 3594 */     for (int i = 1; i < n; i++) {
/* 3595 */       byte current = array[i];
/* 3596 */       if (NumberUtils.compare(previous, current) > 0) {
/* 3597 */         return false;
/*      */       }
/*      */       
/* 3600 */       previous = current;
/*      */     } 
/* 3602 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSorted(char[] array) {
/* 3613 */     if (getLength(array) < 2) {
/* 3614 */       return true;
/*      */     }
/*      */     
/* 3617 */     char previous = array[0];
/* 3618 */     int n = array.length;
/* 3619 */     for (int i = 1; i < n; i++) {
/* 3620 */       char current = array[i];
/* 3621 */       if (CharUtils.compare(previous, current) > 0) {
/* 3622 */         return false;
/*      */       }
/*      */       
/* 3625 */       previous = current;
/*      */     } 
/* 3627 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSorted(double[] array) {
/* 3638 */     if (getLength(array) < 2) {
/* 3639 */       return true;
/*      */     }
/*      */     
/* 3642 */     double previous = array[0];
/* 3643 */     int n = array.length;
/* 3644 */     for (int i = 1; i < n; i++) {
/* 3645 */       double current = array[i];
/* 3646 */       if (Double.compare(previous, current) > 0) {
/* 3647 */         return false;
/*      */       }
/*      */       
/* 3650 */       previous = current;
/*      */     } 
/* 3652 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSorted(float[] array) {
/* 3663 */     if (getLength(array) < 2) {
/* 3664 */       return true;
/*      */     }
/*      */     
/* 3667 */     float previous = array[0];
/* 3668 */     int n = array.length;
/* 3669 */     for (int i = 1; i < n; i++) {
/* 3670 */       float current = array[i];
/* 3671 */       if (Float.compare(previous, current) > 0) {
/* 3672 */         return false;
/*      */       }
/*      */       
/* 3675 */       previous = current;
/*      */     } 
/* 3677 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSorted(int[] array) {
/* 3688 */     if (getLength(array) < 2) {
/* 3689 */       return true;
/*      */     }
/*      */     
/* 3692 */     int previous = array[0];
/* 3693 */     int n = array.length;
/* 3694 */     for (int i = 1; i < n; i++) {
/* 3695 */       int current = array[i];
/* 3696 */       if (NumberUtils.compare(previous, current) > 0) {
/* 3697 */         return false;
/*      */       }
/*      */       
/* 3700 */       previous = current;
/*      */     } 
/* 3702 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSorted(long[] array) {
/* 3713 */     if (getLength(array) < 2) {
/* 3714 */       return true;
/*      */     }
/*      */     
/* 3717 */     long previous = array[0];
/* 3718 */     int n = array.length;
/* 3719 */     for (int i = 1; i < n; i++) {
/* 3720 */       long current = array[i];
/* 3721 */       if (NumberUtils.compare(previous, current) > 0) {
/* 3722 */         return false;
/*      */       }
/*      */       
/* 3725 */       previous = current;
/*      */     } 
/* 3727 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSorted(short[] array) {
/* 3738 */     if (getLength(array) < 2) {
/* 3739 */       return true;
/*      */     }
/*      */     
/* 3742 */     short previous = array[0];
/* 3743 */     int n = array.length;
/* 3744 */     for (int i = 1; i < n; i++) {
/* 3745 */       short current = array[i];
/* 3746 */       if (NumberUtils.compare(previous, current) > 0) {
/* 3747 */         return false;
/*      */       }
/*      */       
/* 3750 */       previous = current;
/*      */     } 
/* 3752 */     return true;
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
/*      */   public static <T extends Comparable<? super T>> boolean isSorted(T[] array) {
/* 3765 */     return isSorted(array, Comparable::compareTo);
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
/*      */   public static <T> boolean isSorted(T[] array, Comparator<T> comparator) {
/* 3779 */     Objects.requireNonNull(comparator, "comparator");
/* 3780 */     if (getLength(array) < 2) {
/* 3781 */       return true;
/*      */     }
/* 3783 */     T previous = array[0];
/* 3784 */     int n = array.length;
/* 3785 */     for (int i = 1; i < n; i++) {
/* 3786 */       T current = array[i];
/* 3787 */       if (comparator.compare(previous, current) > 0) {
/* 3788 */         return false;
/*      */       }
/*      */       
/* 3791 */       previous = current;
/*      */     } 
/* 3793 */     return true;
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
/*      */   public static int lastIndexOf(boolean[] array, boolean valueToFind) {
/* 3809 */     return lastIndexOf(array, valueToFind, 2147483647);
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
/*      */   public static int lastIndexOf(boolean[] array, boolean valueToFind, int startIndex) {
/* 3829 */     if (isEmpty(array) || startIndex < 0) {
/* 3830 */       return -1;
/*      */     }
/* 3832 */     if (startIndex >= array.length) {
/* 3833 */       startIndex = array.length - 1;
/*      */     }
/* 3835 */     for (int i = startIndex; i >= 0; i--) {
/* 3836 */       if (valueToFind == array[i]) {
/* 3837 */         return i;
/*      */       }
/*      */     } 
/* 3840 */     return -1;
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
/*      */   public static int lastIndexOf(byte[] array, byte valueToFind) {
/* 3855 */     return lastIndexOf(array, valueToFind, 2147483647);
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
/*      */   public static int lastIndexOf(byte[] array, byte valueToFind, int startIndex) {
/* 3875 */     if (array == null || startIndex < 0) {
/* 3876 */       return -1;
/*      */     }
/* 3878 */     if (startIndex >= array.length) {
/* 3879 */       startIndex = array.length - 1;
/*      */     }
/* 3881 */     for (int i = startIndex; i >= 0; i--) {
/* 3882 */       if (valueToFind == array[i]) {
/* 3883 */         return i;
/*      */       }
/*      */     } 
/* 3886 */     return -1;
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
/*      */   public static int lastIndexOf(char[] array, char valueToFind) {
/* 3902 */     return lastIndexOf(array, valueToFind, 2147483647);
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
/*      */   public static int lastIndexOf(char[] array, char valueToFind, int startIndex) {
/* 3923 */     if (array == null || startIndex < 0) {
/* 3924 */       return -1;
/*      */     }
/* 3926 */     if (startIndex >= array.length) {
/* 3927 */       startIndex = array.length - 1;
/*      */     }
/* 3929 */     for (int i = startIndex; i >= 0; i--) {
/* 3930 */       if (valueToFind == array[i]) {
/* 3931 */         return i;
/*      */       }
/*      */     } 
/* 3934 */     return -1;
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
/*      */   public static int lastIndexOf(double[] array, double valueToFind) {
/* 3949 */     return lastIndexOf(array, valueToFind, 2147483647);
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
/*      */   public static int lastIndexOf(double[] array, double valueToFind, double tolerance) {
/* 3967 */     return lastIndexOf(array, valueToFind, 2147483647, tolerance);
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
/*      */   public static int lastIndexOf(double[] array, double valueToFind, int startIndex) {
/* 3987 */     if (isEmpty(array) || startIndex < 0) {
/* 3988 */       return -1;
/*      */     }
/* 3990 */     if (startIndex >= array.length) {
/* 3991 */       startIndex = array.length - 1;
/*      */     }
/* 3993 */     for (int i = startIndex; i >= 0; i--) {
/* 3994 */       if (valueToFind == array[i]) {
/* 3995 */         return i;
/*      */       }
/*      */     } 
/* 3998 */     return -1;
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
/*      */   public static int lastIndexOf(double[] array, double valueToFind, int startIndex, double tolerance) {
/* 4021 */     if (isEmpty(array) || startIndex < 0) {
/* 4022 */       return -1;
/*      */     }
/* 4024 */     if (startIndex >= array.length) {
/* 4025 */       startIndex = array.length - 1;
/*      */     }
/* 4027 */     double min = valueToFind - tolerance;
/* 4028 */     double max = valueToFind + tolerance;
/* 4029 */     for (int i = startIndex; i >= 0; i--) {
/* 4030 */       if (array[i] >= min && array[i] <= max) {
/* 4031 */         return i;
/*      */       }
/*      */     } 
/* 4034 */     return -1;
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
/*      */   public static int lastIndexOf(float[] array, float valueToFind) {
/* 4049 */     return lastIndexOf(array, valueToFind, 2147483647);
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
/*      */   public static int lastIndexOf(float[] array, float valueToFind, int startIndex) {
/* 4069 */     if (isEmpty(array) || startIndex < 0) {
/* 4070 */       return -1;
/*      */     }
/* 4072 */     if (startIndex >= array.length) {
/* 4073 */       startIndex = array.length - 1;
/*      */     }
/* 4075 */     for (int i = startIndex; i >= 0; i--) {
/* 4076 */       if (valueToFind == array[i]) {
/* 4077 */         return i;
/*      */       }
/*      */     } 
/* 4080 */     return -1;
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
/*      */   public static int lastIndexOf(int[] array, int valueToFind) {
/* 4096 */     return lastIndexOf(array, valueToFind, 2147483647);
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
/*      */   public static int lastIndexOf(int[] array, int valueToFind, int startIndex) {
/* 4116 */     if (array == null || startIndex < 0) {
/* 4117 */       return -1;
/*      */     }
/* 4119 */     if (startIndex >= array.length) {
/* 4120 */       startIndex = array.length - 1;
/*      */     }
/* 4122 */     for (int i = startIndex; i >= 0; i--) {
/* 4123 */       if (valueToFind == array[i]) {
/* 4124 */         return i;
/*      */       }
/*      */     } 
/* 4127 */     return -1;
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
/*      */   public static int lastIndexOf(long[] array, long valueToFind) {
/* 4142 */     return lastIndexOf(array, valueToFind, 2147483647);
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
/*      */   public static int lastIndexOf(long[] array, long valueToFind, int startIndex) {
/* 4162 */     if (array == null || startIndex < 0) {
/* 4163 */       return -1;
/*      */     }
/* 4165 */     if (startIndex >= array.length) {
/* 4166 */       startIndex = array.length - 1;
/*      */     }
/* 4168 */     for (int i = startIndex; i >= 0; i--) {
/* 4169 */       if (valueToFind == array[i]) {
/* 4170 */         return i;
/*      */       }
/*      */     } 
/* 4173 */     return -1;
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
/*      */   public static int lastIndexOf(Object[] array, Object objectToFind) {
/* 4188 */     return lastIndexOf(array, objectToFind, 2147483647);
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
/*      */   public static int lastIndexOf(Object[] array, Object objectToFind, int startIndex) {
/* 4208 */     if (array == null || startIndex < 0) {
/* 4209 */       return -1;
/*      */     }
/* 4211 */     if (startIndex >= array.length) {
/* 4212 */       startIndex = array.length - 1;
/*      */     }
/* 4214 */     if (objectToFind == null) {
/* 4215 */       for (int i = startIndex; i >= 0; i--) {
/* 4216 */         if (array[i] == null) {
/* 4217 */           return i;
/*      */         }
/*      */       } 
/* 4220 */     } else if (array.getClass().getComponentType().isInstance(objectToFind)) {
/* 4221 */       for (int i = startIndex; i >= 0; i--) {
/* 4222 */         if (objectToFind.equals(array[i])) {
/* 4223 */           return i;
/*      */         }
/*      */       } 
/*      */     } 
/* 4227 */     return -1;
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
/*      */   public static int lastIndexOf(short[] array, short valueToFind) {
/* 4242 */     return lastIndexOf(array, valueToFind, 2147483647);
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
/*      */   public static int lastIndexOf(short[] array, short valueToFind, int startIndex) {
/* 4262 */     if (array == null || startIndex < 0) {
/* 4263 */       return -1;
/*      */     }
/* 4265 */     if (startIndex >= array.length) {
/* 4266 */       startIndex = array.length - 1;
/*      */     }
/* 4268 */     for (int i = startIndex; i >= 0; i--) {
/* 4269 */       if (valueToFind == array[i]) {
/* 4270 */         return i;
/*      */       }
/*      */     } 
/* 4273 */     return -1;
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
/*      */   public static <T> T[] newInstance(Class<T> componentType, int length) {
/* 4288 */     return (T[])Array.newInstance(componentType, length);
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
/*      */   public static boolean[] nullToEmpty(boolean[] array) {
/* 4307 */     return isEmpty(array) ? EMPTY_BOOLEAN_ARRAY : array;
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
/*      */   public static Boolean[] nullToEmpty(Boolean[] array) {
/* 4326 */     return isEmpty((Object[])array) ? EMPTY_BOOLEAN_OBJECT_ARRAY : array;
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
/*      */   public static byte[] nullToEmpty(byte[] array) {
/* 4345 */     return isEmpty(array) ? EMPTY_BYTE_ARRAY : array;
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
/*      */   public static Byte[] nullToEmpty(Byte[] array) {
/* 4364 */     return isEmpty((Object[])array) ? EMPTY_BYTE_OBJECT_ARRAY : array;
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
/*      */   public static char[] nullToEmpty(char[] array) {
/* 4383 */     return isEmpty(array) ? EMPTY_CHAR_ARRAY : array;
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
/*      */   public static Character[] nullToEmpty(Character[] array) {
/* 4402 */     return isEmpty((Object[])array) ? EMPTY_CHARACTER_OBJECT_ARRAY : array;
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
/*      */   public static Class<?>[] nullToEmpty(Class<?>[] array) {
/* 4421 */     return isEmpty((Object[])array) ? EMPTY_CLASS_ARRAY : array;
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
/*      */   public static double[] nullToEmpty(double[] array) {
/* 4440 */     return isEmpty(array) ? EMPTY_DOUBLE_ARRAY : array;
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
/*      */   public static Double[] nullToEmpty(Double[] array) {
/* 4459 */     return isEmpty((Object[])array) ? EMPTY_DOUBLE_OBJECT_ARRAY : array;
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
/*      */   public static float[] nullToEmpty(float[] array) {
/* 4478 */     return isEmpty(array) ? EMPTY_FLOAT_ARRAY : array;
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
/*      */   public static Float[] nullToEmpty(Float[] array) {
/* 4497 */     return isEmpty((Object[])array) ? EMPTY_FLOAT_OBJECT_ARRAY : array;
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
/*      */   public static int[] nullToEmpty(int[] array) {
/* 4516 */     return isEmpty(array) ? EMPTY_INT_ARRAY : array;
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
/*      */   public static Integer[] nullToEmpty(Integer[] array) {
/* 4535 */     return isEmpty((Object[])array) ? EMPTY_INTEGER_OBJECT_ARRAY : array;
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
/*      */   public static long[] nullToEmpty(long[] array) {
/* 4554 */     return isEmpty(array) ? EMPTY_LONG_ARRAY : array;
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
/*      */   public static Long[] nullToEmpty(Long[] array) {
/* 4573 */     return isEmpty((Object[])array) ? EMPTY_LONG_OBJECT_ARRAY : array;
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
/*      */   public static Object[] nullToEmpty(Object[] array) {
/* 4592 */     return isEmpty(array) ? EMPTY_OBJECT_ARRAY : array;
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
/*      */   public static short[] nullToEmpty(short[] array) {
/* 4611 */     return isEmpty(array) ? EMPTY_SHORT_ARRAY : array;
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
/*      */   public static Short[] nullToEmpty(Short[] array) {
/* 4630 */     return isEmpty((Object[])array) ? EMPTY_SHORT_OBJECT_ARRAY : array;
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
/*      */   public static String[] nullToEmpty(String[] array) {
/* 4649 */     return isEmpty((Object[])array) ? EMPTY_STRING_ARRAY : array;
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
/*      */   public static <T> T[] nullToEmpty(T[] array, Class<T[]> type) {
/* 4667 */     if (type == null) {
/* 4668 */       throw new IllegalArgumentException("The type must not be null");
/*      */     }
/*      */     
/* 4671 */     if (array == null) {
/* 4672 */       return type.cast(Array.newInstance(type.getComponentType(), 0));
/*      */     }
/* 4674 */     return array;
/*      */   }
/*      */   
/*      */   private static ThreadLocalRandom random() {
/* 4678 */     return ThreadLocalRandom.current();
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
/*      */   public static boolean[] remove(boolean[] array, int index) {
/* 4711 */     return (boolean[])remove(array, index);
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
/*      */   public static byte[] remove(byte[] array, int index) {
/* 4744 */     return (byte[])remove(array, index);
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
/*      */   public static char[] remove(char[] array, int index) {
/* 4777 */     return (char[])remove(array, index);
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
/*      */   public static double[] remove(double[] array, int index) {
/* 4810 */     return (double[])remove(array, index);
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
/*      */   public static float[] remove(float[] array, int index) {
/* 4843 */     return (float[])remove(array, index);
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
/*      */   public static int[] remove(int[] array, int index) {
/* 4876 */     return (int[])remove(array, index);
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
/*      */   public static long[] remove(long[] array, int index) {
/* 4909 */     return (long[])remove(array, index);
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
/*      */   private static Object remove(Object array, int index) {
/* 4936 */     int length = getLength(array);
/* 4937 */     if (index < 0 || index >= length) {
/* 4938 */       throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
/*      */     }
/*      */     
/* 4941 */     Object result = Array.newInstance(array.getClass().getComponentType(), length - 1);
/* 4942 */     System.arraycopy(array, 0, result, 0, index);
/* 4943 */     if (index < length - 1) {
/* 4944 */       System.arraycopy(array, index + 1, result, index, length - index - 1);
/*      */     }
/*      */     
/* 4947 */     return result;
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
/*      */   public static short[] remove(short[] array, int index) {
/* 4980 */     return (short[])remove(array, index);
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
/*      */   public static <T> T[] remove(T[] array, int index) {
/* 5015 */     return (T[])remove(array, index);
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
/*      */   public static boolean[] removeAll(boolean[] array, int... indices) {
/* 5045 */     return (boolean[])removeAll(array, indices);
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
/*      */   public static byte[] removeAll(byte[] array, int... indices) {
/* 5079 */     return (byte[])removeAll(array, indices);
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
/*      */   public static char[] removeAll(char[] array, int... indices) {
/* 5113 */     return (char[])removeAll(array, indices);
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
/*      */   public static double[] removeAll(double[] array, int... indices) {
/* 5147 */     return (double[])removeAll(array, indices);
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
/*      */   public static float[] removeAll(float[] array, int... indices) {
/* 5181 */     return (float[])removeAll(array, indices);
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
/*      */   public static int[] removeAll(int[] array, int... indices) {
/* 5215 */     return (int[])removeAll(array, indices);
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
/*      */   public static long[] removeAll(long[] array, int... indices) {
/* 5249 */     return (long[])removeAll(array, indices);
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
/*      */   static Object removeAll(Object array, BitSet indices) {
/* 5262 */     if (array == null) {
/* 5263 */       return null;
/*      */     }
/*      */     
/* 5266 */     int srcLength = getLength(array);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5273 */     int removals = indices.cardinality();
/* 5274 */     Object result = Array.newInstance(array.getClass().getComponentType(), srcLength - removals);
/* 5275 */     int srcIndex = 0;
/* 5276 */     int destIndex = 0;
/*      */     
/*      */     int set;
/* 5279 */     while ((set = indices.nextSetBit(srcIndex)) != -1) {
/* 5280 */       int i = set - srcIndex;
/* 5281 */       if (i > 0) {
/* 5282 */         System.arraycopy(array, srcIndex, result, destIndex, i);
/* 5283 */         destIndex += i;
/*      */       } 
/* 5285 */       srcIndex = indices.nextClearBit(set);
/*      */     } 
/* 5287 */     int count = srcLength - srcIndex;
/* 5288 */     if (count > 0) {
/* 5289 */       System.arraycopy(array, srcIndex, result, destIndex, count);
/*      */     }
/* 5291 */     return result;
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
/*      */   static Object removeAll(Object array, int... indices) {
/* 5304 */     int length = getLength(array);
/* 5305 */     int diff = 0;
/* 5306 */     int[] clonedIndices = ArraySorter.sort(clone(indices));
/*      */ 
/*      */     
/* 5309 */     if (isNotEmpty(clonedIndices)) {
/* 5310 */       int i = clonedIndices.length;
/* 5311 */       int prevIndex = length;
/* 5312 */       while (--i >= 0) {
/* 5313 */         int index = clonedIndices[i];
/* 5314 */         if (index < 0 || index >= length) {
/* 5315 */           throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
/*      */         }
/* 5317 */         if (index >= prevIndex) {
/*      */           continue;
/*      */         }
/* 5320 */         diff++;
/* 5321 */         prevIndex = index;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 5326 */     Object result = Array.newInstance(array.getClass().getComponentType(), length - diff);
/* 5327 */     if (diff < length) {
/* 5328 */       int end = length;
/* 5329 */       int dest = length - diff;
/* 5330 */       for (int i = clonedIndices.length - 1; i >= 0; i--) {
/* 5331 */         int index = clonedIndices[i];
/* 5332 */         if (end - index > 1) {
/* 5333 */           int cp = end - index - 1;
/* 5334 */           dest -= cp;
/* 5335 */           System.arraycopy(array, index + 1, result, dest, cp);
/*      */         } 
/*      */         
/* 5338 */         end = index;
/*      */       } 
/* 5340 */       if (end > 0) {
/* 5341 */         System.arraycopy(array, 0, result, 0, end);
/*      */       }
/*      */     } 
/* 5344 */     return result;
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
/*      */   public static short[] removeAll(short[] array, int... indices) {
/* 5378 */     return (short[])removeAll(array, indices);
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
/*      */   public static <T> T[] removeAll(T[] array, int... indices) {
/* 5410 */     return (T[])removeAll(array, indices);
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
/*      */   @Deprecated
/*      */   public static boolean[] removeAllOccurences(boolean[] array, boolean element) {
/* 5430 */     return (boolean[])removeAll(array, indexesOf(array, element));
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
/*      */   @Deprecated
/*      */   public static byte[] removeAllOccurences(byte[] array, byte element) {
/* 5450 */     return (byte[])removeAll(array, indexesOf(array, element));
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
/*      */   @Deprecated
/*      */   public static char[] removeAllOccurences(char[] array, char element) {
/* 5470 */     return (char[])removeAll(array, indexesOf(array, element));
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
/*      */   @Deprecated
/*      */   public static double[] removeAllOccurences(double[] array, double element) {
/* 5490 */     return (double[])removeAll(array, indexesOf(array, element));
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
/*      */   @Deprecated
/*      */   public static float[] removeAllOccurences(float[] array, float element) {
/* 5510 */     return (float[])removeAll(array, indexesOf(array, element));
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
/*      */   @Deprecated
/*      */   public static int[] removeAllOccurences(int[] array, int element) {
/* 5530 */     return (int[])removeAll(array, indexesOf(array, element));
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
/*      */   @Deprecated
/*      */   public static long[] removeAllOccurences(long[] array, long element) {
/* 5550 */     return (long[])removeAll(array, indexesOf(array, element));
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
/*      */   @Deprecated
/*      */   public static short[] removeAllOccurences(short[] array, short element) {
/* 5570 */     return (short[])removeAll(array, indexesOf(array, element));
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
/*      */   @Deprecated
/*      */   public static <T> T[] removeAllOccurences(T[] array, T element) {
/* 5591 */     return (T[])removeAll(array, indexesOf((Object[])array, element));
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
/*      */   public static boolean[] removeAllOccurrences(boolean[] array, boolean element) {
/* 5609 */     return (boolean[])removeAll(array, indexesOf(array, element));
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
/*      */   public static byte[] removeAllOccurrences(byte[] array, byte element) {
/* 5627 */     return (byte[])removeAll(array, indexesOf(array, element));
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
/*      */   public static char[] removeAllOccurrences(char[] array, char element) {
/* 5645 */     return (char[])removeAll(array, indexesOf(array, element));
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
/*      */   public static double[] removeAllOccurrences(double[] array, double element) {
/* 5663 */     return (double[])removeAll(array, indexesOf(array, element));
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
/*      */   public static float[] removeAllOccurrences(float[] array, float element) {
/* 5681 */     return (float[])removeAll(array, indexesOf(array, element));
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
/*      */   public static int[] removeAllOccurrences(int[] array, int element) {
/* 5699 */     return (int[])removeAll(array, indexesOf(array, element));
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
/*      */   public static long[] removeAllOccurrences(long[] array, long element) {
/* 5717 */     return (long[])removeAll(array, indexesOf(array, element));
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
/*      */   public static short[] removeAllOccurrences(short[] array, short element) {
/* 5735 */     return (short[])removeAll(array, indexesOf(array, element));
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
/*      */   public static <T> T[] removeAllOccurrences(T[] array, T element) {
/* 5754 */     return (T[])removeAll(array, indexesOf((Object[])array, element));
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
/*      */   public static boolean[] removeElement(boolean[] array, boolean element) {
/* 5783 */     int index = indexOf(array, element);
/* 5784 */     return (index == -1) ? clone(array) : remove(array, index);
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
/*      */   public static byte[] removeElement(byte[] array, byte element) {
/* 5813 */     int index = indexOf(array, element);
/* 5814 */     return (index == -1) ? clone(array) : remove(array, index);
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
/*      */   public static char[] removeElement(char[] array, char element) {
/* 5843 */     int index = indexOf(array, element);
/* 5844 */     return (index == -1) ? clone(array) : remove(array, index);
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
/*      */   public static double[] removeElement(double[] array, double element) {
/* 5873 */     int index = indexOf(array, element);
/* 5874 */     return (index == -1) ? clone(array) : remove(array, index);
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
/*      */   public static float[] removeElement(float[] array, float element) {
/* 5903 */     int index = indexOf(array, element);
/* 5904 */     return (index == -1) ? clone(array) : remove(array, index);
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
/*      */   public static int[] removeElement(int[] array, int element) {
/* 5933 */     int index = indexOf(array, element);
/* 5934 */     return (index == -1) ? clone(array) : remove(array, index);
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
/*      */   public static long[] removeElement(long[] array, long element) {
/* 5963 */     int index = indexOf(array, element);
/* 5964 */     return (index == -1) ? clone(array) : remove(array, index);
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
/*      */   public static short[] removeElement(short[] array, short element) {
/* 5993 */     int index = indexOf(array, element);
/* 5994 */     return (index == -1) ? clone(array) : remove(array, index);
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
/*      */   public static <T> T[] removeElement(T[] array, Object element) {
/* 6024 */     int index = indexOf((Object[])array, element);
/* 6025 */     return (index == -1) ? clone(array) : remove(array, index);
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
/*      */   public static boolean[] removeElements(boolean[] array, boolean... values) {
/* 6056 */     if (isEmpty(array) || isEmpty(values)) {
/* 6057 */       return clone(array);
/*      */     }
/* 6059 */     HashMap<Boolean, MutableInt> occurrences = new HashMap<>(2);
/* 6060 */     for (boolean v : values) {
/* 6061 */       Boolean boxed = Boolean.valueOf(v);
/* 6062 */       MutableInt count = occurrences.get(boxed);
/* 6063 */       if (count == null) {
/* 6064 */         occurrences.put(boxed, new MutableInt(1));
/*      */       } else {
/* 6066 */         count.increment();
/*      */       } 
/*      */     } 
/* 6069 */     BitSet toRemove = new BitSet();
/* 6070 */     for (int i = 0; i < array.length; i++) {
/* 6071 */       boolean key = array[i];
/* 6072 */       MutableInt count = occurrences.get(Boolean.valueOf(key));
/* 6073 */       if (count != null) {
/* 6074 */         if (count.decrementAndGet() == 0) {
/* 6075 */           occurrences.remove(Boolean.valueOf(key));
/*      */         }
/* 6077 */         toRemove.set(i);
/*      */       } 
/*      */     } 
/* 6080 */     return (boolean[])removeAll(array, toRemove);
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
/*      */   public static byte[] removeElements(byte[] array, byte... values) {
/* 6111 */     if (isEmpty(array) || isEmpty(values)) {
/* 6112 */       return clone(array);
/*      */     }
/* 6114 */     Map<Byte, MutableInt> occurrences = new HashMap<>(values.length);
/* 6115 */     for (byte v : values) {
/* 6116 */       Byte boxed = Byte.valueOf(v);
/* 6117 */       MutableInt count = occurrences.get(boxed);
/* 6118 */       if (count == null) {
/* 6119 */         occurrences.put(boxed, new MutableInt(1));
/*      */       } else {
/* 6121 */         count.increment();
/*      */       } 
/*      */     } 
/* 6124 */     BitSet toRemove = new BitSet();
/* 6125 */     for (int i = 0; i < array.length; i++) {
/* 6126 */       byte key = array[i];
/* 6127 */       MutableInt count = occurrences.get(Byte.valueOf(key));
/* 6128 */       if (count != null) {
/* 6129 */         if (count.decrementAndGet() == 0) {
/* 6130 */           occurrences.remove(Byte.valueOf(key));
/*      */         }
/* 6132 */         toRemove.set(i);
/*      */       } 
/*      */     } 
/* 6135 */     return (byte[])removeAll(array, toRemove);
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
/*      */   public static char[] removeElements(char[] array, char... values) {
/* 6166 */     if (isEmpty(array) || isEmpty(values)) {
/* 6167 */       return clone(array);
/*      */     }
/* 6169 */     HashMap<Character, MutableInt> occurrences = new HashMap<>(values.length);
/* 6170 */     for (char v : values) {
/* 6171 */       Character boxed = Character.valueOf(v);
/* 6172 */       MutableInt count = occurrences.get(boxed);
/* 6173 */       if (count == null) {
/* 6174 */         occurrences.put(boxed, new MutableInt(1));
/*      */       } else {
/* 6176 */         count.increment();
/*      */       } 
/*      */     } 
/* 6179 */     BitSet toRemove = new BitSet();
/* 6180 */     for (int i = 0; i < array.length; i++) {
/* 6181 */       char key = array[i];
/* 6182 */       MutableInt count = occurrences.get(Character.valueOf(key));
/* 6183 */       if (count != null) {
/* 6184 */         if (count.decrementAndGet() == 0) {
/* 6185 */           occurrences.remove(Character.valueOf(key));
/*      */         }
/* 6187 */         toRemove.set(i);
/*      */       } 
/*      */     } 
/* 6190 */     return (char[])removeAll(array, toRemove);
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
/*      */   public static double[] removeElements(double[] array, double... values) {
/* 6221 */     if (isEmpty(array) || isEmpty(values)) {
/* 6222 */       return clone(array);
/*      */     }
/* 6224 */     HashMap<Double, MutableInt> occurrences = new HashMap<>(values.length);
/* 6225 */     for (double v : values) {
/* 6226 */       Double boxed = Double.valueOf(v);
/* 6227 */       MutableInt count = occurrences.get(boxed);
/* 6228 */       if (count == null) {
/* 6229 */         occurrences.put(boxed, new MutableInt(1));
/*      */       } else {
/* 6231 */         count.increment();
/*      */       } 
/*      */     } 
/* 6234 */     BitSet toRemove = new BitSet();
/* 6235 */     for (int i = 0; i < array.length; i++) {
/* 6236 */       double key = array[i];
/* 6237 */       MutableInt count = occurrences.get(Double.valueOf(key));
/* 6238 */       if (count != null) {
/* 6239 */         if (count.decrementAndGet() == 0) {
/* 6240 */           occurrences.remove(Double.valueOf(key));
/*      */         }
/* 6242 */         toRemove.set(i);
/*      */       } 
/*      */     } 
/* 6245 */     return (double[])removeAll(array, toRemove);
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
/*      */   public static float[] removeElements(float[] array, float... values) {
/* 6276 */     if (isEmpty(array) || isEmpty(values)) {
/* 6277 */       return clone(array);
/*      */     }
/* 6279 */     HashMap<Float, MutableInt> occurrences = new HashMap<>(values.length);
/* 6280 */     for (float v : values) {
/* 6281 */       Float boxed = Float.valueOf(v);
/* 6282 */       MutableInt count = occurrences.get(boxed);
/* 6283 */       if (count == null) {
/* 6284 */         occurrences.put(boxed, new MutableInt(1));
/*      */       } else {
/* 6286 */         count.increment();
/*      */       } 
/*      */     } 
/* 6289 */     BitSet toRemove = new BitSet();
/* 6290 */     for (int i = 0; i < array.length; i++) {
/* 6291 */       float key = array[i];
/* 6292 */       MutableInt count = occurrences.get(Float.valueOf(key));
/* 6293 */       if (count != null) {
/* 6294 */         if (count.decrementAndGet() == 0) {
/* 6295 */           occurrences.remove(Float.valueOf(key));
/*      */         }
/* 6297 */         toRemove.set(i);
/*      */       } 
/*      */     } 
/* 6300 */     return (float[])removeAll(array, toRemove);
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
/*      */   public static int[] removeElements(int[] array, int... values) {
/* 6331 */     if (isEmpty(array) || isEmpty(values)) {
/* 6332 */       return clone(array);
/*      */     }
/* 6334 */     HashMap<Integer, MutableInt> occurrences = new HashMap<>(values.length);
/* 6335 */     for (int v : values) {
/* 6336 */       Integer boxed = Integer.valueOf(v);
/* 6337 */       MutableInt count = occurrences.get(boxed);
/* 6338 */       if (count == null) {
/* 6339 */         occurrences.put(boxed, new MutableInt(1));
/*      */       } else {
/* 6341 */         count.increment();
/*      */       } 
/*      */     } 
/* 6344 */     BitSet toRemove = new BitSet();
/* 6345 */     for (int i = 0; i < array.length; i++) {
/* 6346 */       int key = array[i];
/* 6347 */       MutableInt count = occurrences.get(Integer.valueOf(key));
/* 6348 */       if (count != null) {
/* 6349 */         if (count.decrementAndGet() == 0) {
/* 6350 */           occurrences.remove(Integer.valueOf(key));
/*      */         }
/* 6352 */         toRemove.set(i);
/*      */       } 
/*      */     } 
/* 6355 */     return (int[])removeAll(array, toRemove);
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
/*      */   public static long[] removeElements(long[] array, long... values) {
/* 6386 */     if (isEmpty(array) || isEmpty(values)) {
/* 6387 */       return clone(array);
/*      */     }
/* 6389 */     HashMap<Long, MutableInt> occurrences = new HashMap<>(values.length);
/* 6390 */     for (long v : values) {
/* 6391 */       Long boxed = Long.valueOf(v);
/* 6392 */       MutableInt count = occurrences.get(boxed);
/* 6393 */       if (count == null) {
/* 6394 */         occurrences.put(boxed, new MutableInt(1));
/*      */       } else {
/* 6396 */         count.increment();
/*      */       } 
/*      */     } 
/* 6399 */     BitSet toRemove = new BitSet();
/* 6400 */     for (int i = 0; i < array.length; i++) {
/* 6401 */       long key = array[i];
/* 6402 */       MutableInt count = occurrences.get(Long.valueOf(key));
/* 6403 */       if (count != null) {
/* 6404 */         if (count.decrementAndGet() == 0) {
/* 6405 */           occurrences.remove(Long.valueOf(key));
/*      */         }
/* 6407 */         toRemove.set(i);
/*      */       } 
/*      */     } 
/* 6410 */     return (long[])removeAll(array, toRemove);
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
/*      */   public static short[] removeElements(short[] array, short... values) {
/* 6441 */     if (isEmpty(array) || isEmpty(values)) {
/* 6442 */       return clone(array);
/*      */     }
/* 6444 */     HashMap<Short, MutableInt> occurrences = new HashMap<>(values.length);
/* 6445 */     for (short v : values) {
/* 6446 */       Short boxed = Short.valueOf(v);
/* 6447 */       MutableInt count = occurrences.get(boxed);
/* 6448 */       if (count == null) {
/* 6449 */         occurrences.put(boxed, new MutableInt(1));
/*      */       } else {
/* 6451 */         count.increment();
/*      */       } 
/*      */     } 
/* 6454 */     BitSet toRemove = new BitSet();
/* 6455 */     for (int i = 0; i < array.length; i++) {
/* 6456 */       short key = array[i];
/* 6457 */       MutableInt count = occurrences.get(Short.valueOf(key));
/* 6458 */       if (count != null) {
/* 6459 */         if (count.decrementAndGet() == 0) {
/* 6460 */           occurrences.remove(Short.valueOf(key));
/*      */         }
/* 6462 */         toRemove.set(i);
/*      */       } 
/*      */     } 
/* 6465 */     return (short[])removeAll(array, toRemove);
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
/*      */   @SafeVarargs
/*      */   public static <T> T[] removeElements(T[] array, T... values) {
/* 6498 */     if (isEmpty((Object[])array) || isEmpty((Object[])values)) {
/* 6499 */       return clone(array);
/*      */     }
/* 6501 */     HashMap<T, MutableInt> occurrences = new HashMap<>(values.length);
/* 6502 */     for (T v : values) {
/* 6503 */       MutableInt count = occurrences.get(v);
/* 6504 */       if (count == null) {
/* 6505 */         occurrences.put(v, new MutableInt(1));
/*      */       } else {
/* 6507 */         count.increment();
/*      */       } 
/*      */     } 
/* 6510 */     BitSet toRemove = new BitSet();
/* 6511 */     for (int i = 0; i < array.length; i++) {
/* 6512 */       T key = array[i];
/* 6513 */       MutableInt count = occurrences.get(key);
/* 6514 */       if (count != null) {
/* 6515 */         if (count.decrementAndGet() == 0) {
/* 6516 */           occurrences.remove(key);
/*      */         }
/* 6518 */         toRemove.set(i);
/*      */       } 
/*      */     } 
/*      */     
/* 6522 */     T[] result = (T[])removeAll(array, toRemove);
/* 6523 */     return result;
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
/*      */   public static void reverse(boolean[] array) {
/* 6535 */     if (array == null) {
/*      */       return;
/*      */     }
/* 6538 */     reverse(array, 0, array.length);
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
/*      */   public static void reverse(boolean[] array, int startIndexInclusive, int endIndexExclusive) {
/* 6558 */     if (array == null) {
/*      */       return;
/*      */     }
/* 6561 */     int i = Math.max(startIndexInclusive, 0);
/* 6562 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 6564 */     while (j > i) {
/* 6565 */       boolean tmp = array[j];
/* 6566 */       array[j] = array[i];
/* 6567 */       array[i] = tmp;
/* 6568 */       j--;
/* 6569 */       i++;
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
/*      */   public static void reverse(byte[] array) {
/* 6582 */     if (array != null) {
/* 6583 */       reverse(array, 0, array.length);
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
/*      */   public static void reverse(byte[] array, int startIndexInclusive, int endIndexExclusive) {
/* 6604 */     if (array == null) {
/*      */       return;
/*      */     }
/* 6607 */     int i = Math.max(startIndexInclusive, 0);
/* 6608 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 6610 */     while (j > i) {
/* 6611 */       byte tmp = array[j];
/* 6612 */       array[j] = array[i];
/* 6613 */       array[i] = tmp;
/* 6614 */       j--;
/* 6615 */       i++;
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
/*      */   public static void reverse(char[] array) {
/* 6628 */     if (array != null) {
/* 6629 */       reverse(array, 0, array.length);
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
/*      */   public static void reverse(char[] array, int startIndexInclusive, int endIndexExclusive) {
/* 6650 */     if (array == null) {
/*      */       return;
/*      */     }
/* 6653 */     int i = Math.max(startIndexInclusive, 0);
/* 6654 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 6656 */     while (j > i) {
/* 6657 */       char tmp = array[j];
/* 6658 */       array[j] = array[i];
/* 6659 */       array[i] = tmp;
/* 6660 */       j--;
/* 6661 */       i++;
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
/*      */   public static void reverse(double[] array) {
/* 6674 */     if (array != null) {
/* 6675 */       reverse(array, 0, array.length);
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
/*      */   public static void reverse(double[] array, int startIndexInclusive, int endIndexExclusive) {
/* 6696 */     if (array == null) {
/*      */       return;
/*      */     }
/* 6699 */     int i = Math.max(startIndexInclusive, 0);
/* 6700 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 6702 */     while (j > i) {
/* 6703 */       double tmp = array[j];
/* 6704 */       array[j] = array[i];
/* 6705 */       array[i] = tmp;
/* 6706 */       j--;
/* 6707 */       i++;
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
/*      */   public static void reverse(float[] array) {
/* 6720 */     if (array != null) {
/* 6721 */       reverse(array, 0, array.length);
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
/*      */   public static void reverse(float[] array, int startIndexInclusive, int endIndexExclusive) {
/* 6742 */     if (array == null) {
/*      */       return;
/*      */     }
/* 6745 */     int i = Math.max(startIndexInclusive, 0);
/* 6746 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 6748 */     while (j > i) {
/* 6749 */       float tmp = array[j];
/* 6750 */       array[j] = array[i];
/* 6751 */       array[i] = tmp;
/* 6752 */       j--;
/* 6753 */       i++;
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
/*      */   public static void reverse(int[] array) {
/* 6766 */     if (array != null) {
/* 6767 */       reverse(array, 0, array.length);
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
/*      */   public static void reverse(int[] array, int startIndexInclusive, int endIndexExclusive) {
/* 6788 */     if (array == null) {
/*      */       return;
/*      */     }
/* 6791 */     int i = Math.max(startIndexInclusive, 0);
/* 6792 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 6794 */     while (j > i) {
/* 6795 */       int tmp = array[j];
/* 6796 */       array[j] = array[i];
/* 6797 */       array[i] = tmp;
/* 6798 */       j--;
/* 6799 */       i++;
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
/*      */   public static void reverse(long[] array) {
/* 6812 */     if (array != null) {
/* 6813 */       reverse(array, 0, array.length);
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
/*      */   public static void reverse(long[] array, int startIndexInclusive, int endIndexExclusive) {
/* 6834 */     if (array == null) {
/*      */       return;
/*      */     }
/* 6837 */     int i = Math.max(startIndexInclusive, 0);
/* 6838 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 6840 */     while (j > i) {
/* 6841 */       long tmp = array[j];
/* 6842 */       array[j] = array[i];
/* 6843 */       array[i] = tmp;
/* 6844 */       j--;
/* 6845 */       i++;
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
/*      */   public static void reverse(Object[] array) {
/* 6861 */     if (array != null) {
/* 6862 */       reverse(array, 0, array.length);
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
/*      */   public static void reverse(Object[] array, int startIndexInclusive, int endIndexExclusive) {
/* 6883 */     if (array == null) {
/*      */       return;
/*      */     }
/* 6886 */     int i = Math.max(startIndexInclusive, 0);
/* 6887 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 6889 */     while (j > i) {
/* 6890 */       Object tmp = array[j];
/* 6891 */       array[j] = array[i];
/* 6892 */       array[i] = tmp;
/* 6893 */       j--;
/* 6894 */       i++;
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
/*      */   public static void reverse(short[] array) {
/* 6907 */     if (array != null) {
/* 6908 */       reverse(array, 0, array.length);
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
/*      */   public static void reverse(short[] array, int startIndexInclusive, int endIndexExclusive) {
/* 6929 */     if (array == null) {
/*      */       return;
/*      */     }
/* 6932 */     int i = Math.max(startIndexInclusive, 0);
/* 6933 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 6935 */     while (j > i) {
/* 6936 */       short tmp = array[j];
/* 6937 */       array[j] = array[i];
/* 6938 */       array[i] = tmp;
/* 6939 */       j--;
/* 6940 */       i++;
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
/*      */   public static <T> T[] setAll(T[] array, IntFunction<? extends T> generator) {
/* 6958 */     if (array != null && generator != null) {
/* 6959 */       Arrays.setAll(array, generator);
/*      */     }
/* 6961 */     return array;
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
/*      */   public static <T> T[] setAll(T[] array, Supplier<? extends T> generator) {
/* 6978 */     if (array != null && generator != null) {
/* 6979 */       for (int i = 0; i < array.length; i++) {
/* 6980 */         array[i] = generator.get();
/*      */       }
/*      */     }
/* 6983 */     return array;
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
/*      */   public static void shift(boolean[] array, int offset) {
/* 6999 */     if (array != null) {
/* 7000 */       shift(array, 0, array.length, offset);
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
/*      */   public static void shift(boolean[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
/* 7024 */     if (array == null || startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
/*      */       return;
/*      */     }
/* 7027 */     if (startIndexInclusive < 0) {
/* 7028 */       startIndexInclusive = 0;
/*      */     }
/* 7030 */     if (endIndexExclusive >= array.length) {
/* 7031 */       endIndexExclusive = array.length;
/*      */     }
/* 7033 */     int n = endIndexExclusive - startIndexInclusive;
/* 7034 */     if (n <= 1) {
/*      */       return;
/*      */     }
/* 7037 */     offset %= n;
/* 7038 */     if (offset < 0) {
/* 7039 */       offset += n;
/*      */     }
/*      */ 
/*      */     
/* 7043 */     while (n > 1 && offset > 0) {
/* 7044 */       int nOffset = n - offset;
/*      */       
/* 7046 */       if (offset > nOffset) {
/* 7047 */         swap(array, startIndexInclusive, startIndexInclusive + n - nOffset, nOffset);
/* 7048 */         n = offset;
/* 7049 */         offset -= nOffset; continue;
/* 7050 */       }  if (offset < nOffset) {
/* 7051 */         swap(array, startIndexInclusive, startIndexInclusive + nOffset, offset);
/* 7052 */         startIndexInclusive += offset;
/* 7053 */         n = nOffset; continue;
/*      */       } 
/* 7055 */       swap(array, startIndexInclusive, startIndexInclusive + nOffset, offset);
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
/*      */   public static void shift(byte[] array, int offset) {
/* 7074 */     if (array != null) {
/* 7075 */       shift(array, 0, array.length, offset);
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
/*      */   public static void shift(byte[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
/* 7099 */     if (array == null || startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
/*      */       return;
/*      */     }
/* 7102 */     if (startIndexInclusive < 0) {
/* 7103 */       startIndexInclusive = 0;
/*      */     }
/* 7105 */     if (endIndexExclusive >= array.length) {
/* 7106 */       endIndexExclusive = array.length;
/*      */     }
/* 7108 */     int n = endIndexExclusive - startIndexInclusive;
/* 7109 */     if (n <= 1) {
/*      */       return;
/*      */     }
/* 7112 */     offset %= n;
/* 7113 */     if (offset < 0) {
/* 7114 */       offset += n;
/*      */     }
/*      */ 
/*      */     
/* 7118 */     while (n > 1 && offset > 0) {
/* 7119 */       int nOffset = n - offset;
/*      */       
/* 7121 */       if (offset > nOffset) {
/* 7122 */         swap(array, startIndexInclusive, startIndexInclusive + n - nOffset, nOffset);
/* 7123 */         n = offset;
/* 7124 */         offset -= nOffset; continue;
/* 7125 */       }  if (offset < nOffset) {
/* 7126 */         swap(array, startIndexInclusive, startIndexInclusive + nOffset, offset);
/* 7127 */         startIndexInclusive += offset;
/* 7128 */         n = nOffset; continue;
/*      */       } 
/* 7130 */       swap(array, startIndexInclusive, startIndexInclusive + nOffset, offset);
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
/*      */   public static void shift(char[] array, int offset) {
/* 7149 */     if (array != null) {
/* 7150 */       shift(array, 0, array.length, offset);
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
/*      */   public static void shift(char[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
/* 7174 */     if (array == null || startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
/*      */       return;
/*      */     }
/* 7177 */     if (startIndexInclusive < 0) {
/* 7178 */       startIndexInclusive = 0;
/*      */     }
/* 7180 */     if (endIndexExclusive >= array.length) {
/* 7181 */       endIndexExclusive = array.length;
/*      */     }
/* 7183 */     int n = endIndexExclusive - startIndexInclusive;
/* 7184 */     if (n <= 1) {
/*      */       return;
/*      */     }
/* 7187 */     offset %= n;
/* 7188 */     if (offset < 0) {
/* 7189 */       offset += n;
/*      */     }
/*      */ 
/*      */     
/* 7193 */     while (n > 1 && offset > 0) {
/* 7194 */       int nOffset = n - offset;
/*      */       
/* 7196 */       if (offset > nOffset) {
/* 7197 */         swap(array, startIndexInclusive, startIndexInclusive + n - nOffset, nOffset);
/* 7198 */         n = offset;
/* 7199 */         offset -= nOffset; continue;
/* 7200 */       }  if (offset < nOffset) {
/* 7201 */         swap(array, startIndexInclusive, startIndexInclusive + nOffset, offset);
/* 7202 */         startIndexInclusive += offset;
/* 7203 */         n = nOffset; continue;
/*      */       } 
/* 7205 */       swap(array, startIndexInclusive, startIndexInclusive + nOffset, offset);
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
/*      */   public static void shift(double[] array, int offset) {
/* 7224 */     if (array != null) {
/* 7225 */       shift(array, 0, array.length, offset);
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
/*      */   public static void shift(double[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
/* 7249 */     if (array == null || startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
/*      */       return;
/*      */     }
/* 7252 */     if (startIndexInclusive < 0) {
/* 7253 */       startIndexInclusive = 0;
/*      */     }
/* 7255 */     if (endIndexExclusive >= array.length) {
/* 7256 */       endIndexExclusive = array.length;
/*      */     }
/* 7258 */     int n = endIndexExclusive - startIndexInclusive;
/* 7259 */     if (n <= 1) {
/*      */       return;
/*      */     }
/* 7262 */     offset %= n;
/* 7263 */     if (offset < 0) {
/* 7264 */       offset += n;
/*      */     }
/*      */ 
/*      */     
/* 7268 */     while (n > 1 && offset > 0) {
/* 7269 */       int nOffset = n - offset;
/*      */       
/* 7271 */       if (offset > nOffset) {
/* 7272 */         swap(array, startIndexInclusive, startIndexInclusive + n - nOffset, nOffset);
/* 7273 */         n = offset;
/* 7274 */         offset -= nOffset; continue;
/* 7275 */       }  if (offset < nOffset) {
/* 7276 */         swap(array, startIndexInclusive, startIndexInclusive + nOffset, offset);
/* 7277 */         startIndexInclusive += offset;
/* 7278 */         n = nOffset; continue;
/*      */       } 
/* 7280 */       swap(array, startIndexInclusive, startIndexInclusive + nOffset, offset);
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
/*      */   public static void shift(float[] array, int offset) {
/* 7299 */     if (array != null) {
/* 7300 */       shift(array, 0, array.length, offset);
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
/*      */   public static void shift(float[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
/* 7324 */     if (array == null || startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
/*      */       return;
/*      */     }
/* 7327 */     if (startIndexInclusive < 0) {
/* 7328 */       startIndexInclusive = 0;
/*      */     }
/* 7330 */     if (endIndexExclusive >= array.length) {
/* 7331 */       endIndexExclusive = array.length;
/*      */     }
/* 7333 */     int n = endIndexExclusive - startIndexInclusive;
/* 7334 */     if (n <= 1) {
/*      */       return;
/*      */     }
/* 7337 */     offset %= n;
/* 7338 */     if (offset < 0) {
/* 7339 */       offset += n;
/*      */     }
/*      */ 
/*      */     
/* 7343 */     while (n > 1 && offset > 0) {
/* 7344 */       int nOffset = n - offset;
/*      */       
/* 7346 */       if (offset > nOffset) {
/* 7347 */         swap(array, startIndexInclusive, startIndexInclusive + n - nOffset, nOffset);
/* 7348 */         n = offset;
/* 7349 */         offset -= nOffset; continue;
/* 7350 */       }  if (offset < nOffset) {
/* 7351 */         swap(array, startIndexInclusive, startIndexInclusive + nOffset, offset);
/* 7352 */         startIndexInclusive += offset;
/* 7353 */         n = nOffset; continue;
/*      */       } 
/* 7355 */       swap(array, startIndexInclusive, startIndexInclusive + nOffset, offset);
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
/*      */   public static void shift(int[] array, int offset) {
/* 7374 */     if (array != null) {
/* 7375 */       shift(array, 0, array.length, offset);
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
/*      */   public static void shift(int[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
/* 7399 */     if (array == null || startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
/*      */       return;
/*      */     }
/* 7402 */     if (startIndexInclusive < 0) {
/* 7403 */       startIndexInclusive = 0;
/*      */     }
/* 7405 */     if (endIndexExclusive >= array.length) {
/* 7406 */       endIndexExclusive = array.length;
/*      */     }
/* 7408 */     int n = endIndexExclusive - startIndexInclusive;
/* 7409 */     if (n <= 1) {
/*      */       return;
/*      */     }
/* 7412 */     offset %= n;
/* 7413 */     if (offset < 0) {
/* 7414 */       offset += n;
/*      */     }
/*      */ 
/*      */     
/* 7418 */     while (n > 1 && offset > 0) {
/* 7419 */       int nOffset = n - offset;
/*      */       
/* 7421 */       if (offset > nOffset) {
/* 7422 */         swap(array, startIndexInclusive, startIndexInclusive + n - nOffset, nOffset);
/* 7423 */         n = offset;
/* 7424 */         offset -= nOffset; continue;
/* 7425 */       }  if (offset < nOffset) {
/* 7426 */         swap(array, startIndexInclusive, startIndexInclusive + nOffset, offset);
/* 7427 */         startIndexInclusive += offset;
/* 7428 */         n = nOffset; continue;
/*      */       } 
/* 7430 */       swap(array, startIndexInclusive, startIndexInclusive + nOffset, offset);
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
/*      */   public static void shift(long[] array, int offset) {
/* 7449 */     if (array != null) {
/* 7450 */       shift(array, 0, array.length, offset);
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
/*      */   public static void shift(long[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
/* 7474 */     if (array == null || startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
/*      */       return;
/*      */     }
/* 7477 */     if (startIndexInclusive < 0) {
/* 7478 */       startIndexInclusive = 0;
/*      */     }
/* 7480 */     if (endIndexExclusive >= array.length) {
/* 7481 */       endIndexExclusive = array.length;
/*      */     }
/* 7483 */     int n = endIndexExclusive - startIndexInclusive;
/* 7484 */     if (n <= 1) {
/*      */       return;
/*      */     }
/* 7487 */     offset %= n;
/* 7488 */     if (offset < 0) {
/* 7489 */       offset += n;
/*      */     }
/*      */ 
/*      */     
/* 7493 */     while (n > 1 && offset > 0) {
/* 7494 */       int nOffset = n - offset;
/*      */       
/* 7496 */       if (offset > nOffset) {
/* 7497 */         swap(array, startIndexInclusive, startIndexInclusive + n - nOffset, nOffset);
/* 7498 */         n = offset;
/* 7499 */         offset -= nOffset; continue;
/* 7500 */       }  if (offset < nOffset) {
/* 7501 */         swap(array, startIndexInclusive, startIndexInclusive + nOffset, offset);
/* 7502 */         startIndexInclusive += offset;
/* 7503 */         n = nOffset; continue;
/*      */       } 
/* 7505 */       swap(array, startIndexInclusive, startIndexInclusive + nOffset, offset);
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
/*      */   public static void shift(Object[] array, int offset) {
/* 7524 */     if (array != null) {
/* 7525 */       shift(array, 0, array.length, offset);
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
/*      */   public static void shift(Object[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
/* 7549 */     if (array == null || startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
/*      */       return;
/*      */     }
/* 7552 */     if (startIndexInclusive < 0) {
/* 7553 */       startIndexInclusive = 0;
/*      */     }
/* 7555 */     if (endIndexExclusive >= array.length) {
/* 7556 */       endIndexExclusive = array.length;
/*      */     }
/* 7558 */     int n = endIndexExclusive - startIndexInclusive;
/* 7559 */     if (n <= 1) {
/*      */       return;
/*      */     }
/* 7562 */     offset %= n;
/* 7563 */     if (offset < 0) {
/* 7564 */       offset += n;
/*      */     }
/*      */ 
/*      */     
/* 7568 */     while (n > 1 && offset > 0) {
/* 7569 */       int nOffset = n - offset;
/*      */       
/* 7571 */       if (offset > nOffset) {
/* 7572 */         swap(array, startIndexInclusive, startIndexInclusive + n - nOffset, nOffset);
/* 7573 */         n = offset;
/* 7574 */         offset -= nOffset; continue;
/* 7575 */       }  if (offset < nOffset) {
/* 7576 */         swap(array, startIndexInclusive, startIndexInclusive + nOffset, offset);
/* 7577 */         startIndexInclusive += offset;
/* 7578 */         n = nOffset; continue;
/*      */       } 
/* 7580 */       swap(array, startIndexInclusive, startIndexInclusive + nOffset, offset);
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
/*      */   public static void shift(short[] array, int offset) {
/* 7599 */     if (array != null) {
/* 7600 */       shift(array, 0, array.length, offset);
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
/*      */   public static void shift(short[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
/* 7624 */     if (array == null || startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
/*      */       return;
/*      */     }
/* 7627 */     if (startIndexInclusive < 0) {
/* 7628 */       startIndexInclusive = 0;
/*      */     }
/* 7630 */     if (endIndexExclusive >= array.length) {
/* 7631 */       endIndexExclusive = array.length;
/*      */     }
/* 7633 */     int n = endIndexExclusive - startIndexInclusive;
/* 7634 */     if (n <= 1) {
/*      */       return;
/*      */     }
/* 7637 */     offset %= n;
/* 7638 */     if (offset < 0) {
/* 7639 */       offset += n;
/*      */     }
/*      */ 
/*      */     
/* 7643 */     while (n > 1 && offset > 0) {
/* 7644 */       int nOffset = n - offset;
/*      */       
/* 7646 */       if (offset > nOffset) {
/* 7647 */         swap(array, startIndexInclusive, startIndexInclusive + n - nOffset, nOffset);
/* 7648 */         n = offset;
/* 7649 */         offset -= nOffset; continue;
/* 7650 */       }  if (offset < nOffset) {
/* 7651 */         swap(array, startIndexInclusive, startIndexInclusive + nOffset, offset);
/* 7652 */         startIndexInclusive += offset;
/* 7653 */         n = nOffset; continue;
/*      */       } 
/* 7655 */       swap(array, startIndexInclusive, startIndexInclusive + nOffset, offset);
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
/*      */   public static void shuffle(boolean[] array) {
/* 7669 */     shuffle(array, random());
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
/*      */   public static void shuffle(boolean[] array, Random random) {
/* 7681 */     for (int i = array.length; i > 1; i--) {
/* 7682 */       swap(array, i - 1, random.nextInt(i), 1);
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
/*      */   public static void shuffle(byte[] array) {
/* 7694 */     shuffle(array, random());
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
/*      */   public static void shuffle(byte[] array, Random random) {
/* 7706 */     for (int i = array.length; i > 1; i--) {
/* 7707 */       swap(array, i - 1, random.nextInt(i), 1);
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
/*      */   public static void shuffle(char[] array) {
/* 7719 */     shuffle(array, random());
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
/*      */   public static void shuffle(char[] array, Random random) {
/* 7731 */     for (int i = array.length; i > 1; i--) {
/* 7732 */       swap(array, i - 1, random.nextInt(i), 1);
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
/*      */   public static void shuffle(double[] array) {
/* 7744 */     shuffle(array, random());
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
/*      */   public static void shuffle(double[] array, Random random) {
/* 7756 */     for (int i = array.length; i > 1; i--) {
/* 7757 */       swap(array, i - 1, random.nextInt(i), 1);
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
/*      */   public static void shuffle(float[] array) {
/* 7769 */     shuffle(array, random());
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
/*      */   public static void shuffle(float[] array, Random random) {
/* 7781 */     for (int i = array.length; i > 1; i--) {
/* 7782 */       swap(array, i - 1, random.nextInt(i), 1);
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
/*      */   public static void shuffle(int[] array) {
/* 7794 */     shuffle(array, random());
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
/*      */   public static void shuffle(int[] array, Random random) {
/* 7806 */     for (int i = array.length; i > 1; i--) {
/* 7807 */       swap(array, i - 1, random.nextInt(i), 1);
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
/*      */   public static void shuffle(long[] array) {
/* 7819 */     shuffle(array, random());
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
/*      */   public static void shuffle(long[] array, Random random) {
/* 7831 */     for (int i = array.length; i > 1; i--) {
/* 7832 */       swap(array, i - 1, random.nextInt(i), 1);
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
/*      */   public static void shuffle(Object[] array) {
/* 7844 */     shuffle(array, random());
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
/*      */   public static void shuffle(Object[] array, Random random) {
/* 7856 */     for (int i = array.length; i > 1; i--) {
/* 7857 */       swap(array, i - 1, random.nextInt(i), 1);
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
/*      */   public static void shuffle(short[] array) {
/* 7869 */     shuffle(array, random());
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
/*      */   public static void shuffle(short[] array, Random random) {
/* 7881 */     for (int i = array.length; i > 1; i--) {
/* 7882 */       swap(array, i - 1, random.nextInt(i), 1);
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
/*      */   public static boolean[] subarray(boolean[] array, int startIndexInclusive, int endIndexExclusive) {
/* 7908 */     if (array == null) {
/* 7909 */       return null;
/*      */     }
/* 7911 */     if (startIndexInclusive < 0) {
/* 7912 */       startIndexInclusive = 0;
/*      */     }
/* 7914 */     if (endIndexExclusive > array.length) {
/* 7915 */       endIndexExclusive = array.length;
/*      */     }
/* 7917 */     int newSize = endIndexExclusive - startIndexInclusive;
/* 7918 */     if (newSize <= 0) {
/* 7919 */       return EMPTY_BOOLEAN_ARRAY;
/*      */     }
/*      */     
/* 7922 */     boolean[] subarray = new boolean[newSize];
/* 7923 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/* 7924 */     return subarray;
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
/*      */   public static byte[] subarray(byte[] array, int startIndexInclusive, int endIndexExclusive) {
/* 7949 */     if (array == null) {
/* 7950 */       return null;
/*      */     }
/* 7952 */     if (startIndexInclusive < 0) {
/* 7953 */       startIndexInclusive = 0;
/*      */     }
/* 7955 */     if (endIndexExclusive > array.length) {
/* 7956 */       endIndexExclusive = array.length;
/*      */     }
/* 7958 */     int newSize = endIndexExclusive - startIndexInclusive;
/* 7959 */     if (newSize <= 0) {
/* 7960 */       return EMPTY_BYTE_ARRAY;
/*      */     }
/*      */     
/* 7963 */     byte[] subarray = new byte[newSize];
/* 7964 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/* 7965 */     return subarray;
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
/*      */   public static char[] subarray(char[] array, int startIndexInclusive, int endIndexExclusive) {
/* 7990 */     if (array == null) {
/* 7991 */       return null;
/*      */     }
/* 7993 */     if (startIndexInclusive < 0) {
/* 7994 */       startIndexInclusive = 0;
/*      */     }
/* 7996 */     if (endIndexExclusive > array.length) {
/* 7997 */       endIndexExclusive = array.length;
/*      */     }
/* 7999 */     int newSize = endIndexExclusive - startIndexInclusive;
/* 8000 */     if (newSize <= 0) {
/* 8001 */       return EMPTY_CHAR_ARRAY;
/*      */     }
/*      */     
/* 8004 */     char[] subarray = new char[newSize];
/* 8005 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/* 8006 */     return subarray;
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
/*      */   public static double[] subarray(double[] array, int startIndexInclusive, int endIndexExclusive) {
/* 8031 */     if (array == null) {
/* 8032 */       return null;
/*      */     }
/* 8034 */     if (startIndexInclusive < 0) {
/* 8035 */       startIndexInclusive = 0;
/*      */     }
/* 8037 */     if (endIndexExclusive > array.length) {
/* 8038 */       endIndexExclusive = array.length;
/*      */     }
/* 8040 */     int newSize = endIndexExclusive - startIndexInclusive;
/* 8041 */     if (newSize <= 0) {
/* 8042 */       return EMPTY_DOUBLE_ARRAY;
/*      */     }
/*      */     
/* 8045 */     double[] subarray = new double[newSize];
/* 8046 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/* 8047 */     return subarray;
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
/*      */   public static float[] subarray(float[] array, int startIndexInclusive, int endIndexExclusive) {
/* 8072 */     if (array == null) {
/* 8073 */       return null;
/*      */     }
/* 8075 */     if (startIndexInclusive < 0) {
/* 8076 */       startIndexInclusive = 0;
/*      */     }
/* 8078 */     if (endIndexExclusive > array.length) {
/* 8079 */       endIndexExclusive = array.length;
/*      */     }
/* 8081 */     int newSize = endIndexExclusive - startIndexInclusive;
/* 8082 */     if (newSize <= 0) {
/* 8083 */       return EMPTY_FLOAT_ARRAY;
/*      */     }
/*      */     
/* 8086 */     float[] subarray = new float[newSize];
/* 8087 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/* 8088 */     return subarray;
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
/*      */   public static int[] subarray(int[] array, int startIndexInclusive, int endIndexExclusive) {
/* 8113 */     if (array == null) {
/* 8114 */       return null;
/*      */     }
/* 8116 */     if (startIndexInclusive < 0) {
/* 8117 */       startIndexInclusive = 0;
/*      */     }
/* 8119 */     if (endIndexExclusive > array.length) {
/* 8120 */       endIndexExclusive = array.length;
/*      */     }
/* 8122 */     int newSize = endIndexExclusive - startIndexInclusive;
/* 8123 */     if (newSize <= 0) {
/* 8124 */       return EMPTY_INT_ARRAY;
/*      */     }
/*      */     
/* 8127 */     int[] subarray = new int[newSize];
/* 8128 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/* 8129 */     return subarray;
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
/*      */   public static long[] subarray(long[] array, int startIndexInclusive, int endIndexExclusive) {
/* 8154 */     if (array == null) {
/* 8155 */       return null;
/*      */     }
/* 8157 */     if (startIndexInclusive < 0) {
/* 8158 */       startIndexInclusive = 0;
/*      */     }
/* 8160 */     if (endIndexExclusive > array.length) {
/* 8161 */       endIndexExclusive = array.length;
/*      */     }
/* 8163 */     int newSize = endIndexExclusive - startIndexInclusive;
/* 8164 */     if (newSize <= 0) {
/* 8165 */       return EMPTY_LONG_ARRAY;
/*      */     }
/*      */     
/* 8168 */     long[] subarray = new long[newSize];
/* 8169 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/* 8170 */     return subarray;
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
/*      */   public static short[] subarray(short[] array, int startIndexInclusive, int endIndexExclusive) {
/* 8195 */     if (array == null) {
/* 8196 */       return null;
/*      */     }
/* 8198 */     if (startIndexInclusive < 0) {
/* 8199 */       startIndexInclusive = 0;
/*      */     }
/* 8201 */     if (endIndexExclusive > array.length) {
/* 8202 */       endIndexExclusive = array.length;
/*      */     }
/* 8204 */     int newSize = endIndexExclusive - startIndexInclusive;
/* 8205 */     if (newSize <= 0) {
/* 8206 */       return EMPTY_SHORT_ARRAY;
/*      */     }
/*      */     
/* 8209 */     short[] subarray = new short[newSize];
/* 8210 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/* 8211 */     return subarray;
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
/*      */   public static <T> T[] subarray(T[] array, int startIndexInclusive, int endIndexExclusive) {
/* 8245 */     if (array == null) {
/* 8246 */       return null;
/*      */     }
/* 8248 */     if (startIndexInclusive < 0) {
/* 8249 */       startIndexInclusive = 0;
/*      */     }
/* 8251 */     if (endIndexExclusive > array.length) {
/* 8252 */       endIndexExclusive = array.length;
/*      */     }
/* 8254 */     int newSize = endIndexExclusive - startIndexInclusive;
/* 8255 */     Class<T> type = getComponentType(array);
/* 8256 */     if (newSize <= 0) {
/* 8257 */       return newInstance(type, 0);
/*      */     }
/* 8259 */     T[] subarray = newInstance(type, newSize);
/* 8260 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/* 8261 */     return subarray;
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
/*      */   public static void swap(boolean[] array, int offset1, int offset2) {
/* 8286 */     swap(array, offset1, offset2, 1);
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
/*      */   public static void swap(boolean[] array, int offset1, int offset2, int len) {
/* 8315 */     if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
/*      */       return;
/*      */     }
/* 8318 */     if (offset1 < 0) {
/* 8319 */       offset1 = 0;
/*      */     }
/* 8321 */     if (offset2 < 0) {
/* 8322 */       offset2 = 0;
/*      */     }
/* 8324 */     len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
/* 8325 */     for (int i = 0; i < len; i++, offset1++, offset2++) {
/* 8326 */       boolean aux = array[offset1];
/* 8327 */       array[offset1] = array[offset2];
/* 8328 */       array[offset2] = aux;
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
/*      */   public static void swap(byte[] array, int offset1, int offset2) {
/* 8354 */     swap(array, offset1, offset2, 1);
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
/*      */   public static void swap(byte[] array, int offset1, int offset2, int len) {
/* 8382 */     if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
/*      */       return;
/*      */     }
/* 8385 */     if (offset1 < 0) {
/* 8386 */       offset1 = 0;
/*      */     }
/* 8388 */     if (offset2 < 0) {
/* 8389 */       offset2 = 0;
/*      */     }
/* 8391 */     len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
/* 8392 */     for (int i = 0; i < len; i++, offset1++, offset2++) {
/* 8393 */       byte aux = array[offset1];
/* 8394 */       array[offset1] = array[offset2];
/* 8395 */       array[offset2] = aux;
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
/*      */   public static void swap(char[] array, int offset1, int offset2) {
/* 8421 */     swap(array, offset1, offset2, 1);
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
/*      */   public static void swap(char[] array, int offset1, int offset2, int len) {
/* 8449 */     if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
/*      */       return;
/*      */     }
/* 8452 */     if (offset1 < 0) {
/* 8453 */       offset1 = 0;
/*      */     }
/* 8455 */     if (offset2 < 0) {
/* 8456 */       offset2 = 0;
/*      */     }
/* 8458 */     len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
/* 8459 */     for (int i = 0; i < len; i++, offset1++, offset2++) {
/* 8460 */       char aux = array[offset1];
/* 8461 */       array[offset1] = array[offset2];
/* 8462 */       array[offset2] = aux;
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
/*      */   public static void swap(double[] array, int offset1, int offset2) {
/* 8488 */     swap(array, offset1, offset2, 1);
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
/*      */   public static void swap(double[] array, int offset1, int offset2, int len) {
/* 8516 */     if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
/*      */       return;
/*      */     }
/* 8519 */     if (offset1 < 0) {
/* 8520 */       offset1 = 0;
/*      */     }
/* 8522 */     if (offset2 < 0) {
/* 8523 */       offset2 = 0;
/*      */     }
/* 8525 */     len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
/* 8526 */     for (int i = 0; i < len; i++, offset1++, offset2++) {
/* 8527 */       double aux = array[offset1];
/* 8528 */       array[offset1] = array[offset2];
/* 8529 */       array[offset2] = aux;
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
/*      */   public static void swap(float[] array, int offset1, int offset2) {
/* 8555 */     swap(array, offset1, offset2, 1);
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
/*      */   public static void swap(float[] array, int offset1, int offset2, int len) {
/* 8583 */     if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
/*      */       return;
/*      */     }
/* 8586 */     if (offset1 < 0) {
/* 8587 */       offset1 = 0;
/*      */     }
/* 8589 */     if (offset2 < 0) {
/* 8590 */       offset2 = 0;
/*      */     }
/* 8592 */     len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
/* 8593 */     for (int i = 0; i < len; i++, offset1++, offset2++) {
/* 8594 */       float aux = array[offset1];
/* 8595 */       array[offset1] = array[offset2];
/* 8596 */       array[offset2] = aux;
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
/*      */   public static void swap(int[] array, int offset1, int offset2) {
/* 8623 */     swap(array, offset1, offset2, 1);
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
/*      */   public static void swap(int[] array, int offset1, int offset2, int len) {
/* 8651 */     if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
/*      */       return;
/*      */     }
/* 8654 */     if (offset1 < 0) {
/* 8655 */       offset1 = 0;
/*      */     }
/* 8657 */     if (offset2 < 0) {
/* 8658 */       offset2 = 0;
/*      */     }
/* 8660 */     len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
/* 8661 */     for (int i = 0; i < len; i++, offset1++, offset2++) {
/* 8662 */       int aux = array[offset1];
/* 8663 */       array[offset1] = array[offset2];
/* 8664 */       array[offset2] = aux;
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
/*      */   public static void swap(long[] array, int offset1, int offset2) {
/* 8690 */     swap(array, offset1, offset2, 1);
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
/*      */   public static void swap(long[] array, int offset1, int offset2, int len) {
/* 8718 */     if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
/*      */       return;
/*      */     }
/* 8721 */     if (offset1 < 0) {
/* 8722 */       offset1 = 0;
/*      */     }
/* 8724 */     if (offset2 < 0) {
/* 8725 */       offset2 = 0;
/*      */     }
/* 8727 */     len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
/* 8728 */     for (int i = 0; i < len; i++, offset1++, offset2++) {
/* 8729 */       long aux = array[offset1];
/* 8730 */       array[offset1] = array[offset2];
/* 8731 */       array[offset2] = aux;
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
/*      */   public static void swap(Object[] array, int offset1, int offset2) {
/* 8757 */     swap(array, offset1, offset2, 1);
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
/*      */   public static void swap(Object[] array, int offset1, int offset2, int len) {
/* 8785 */     if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
/*      */       return;
/*      */     }
/* 8788 */     if (offset1 < 0) {
/* 8789 */       offset1 = 0;
/*      */     }
/* 8791 */     if (offset2 < 0) {
/* 8792 */       offset2 = 0;
/*      */     }
/* 8794 */     len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
/* 8795 */     for (int i = 0; i < len; i++, offset1++, offset2++) {
/* 8796 */       Object aux = array[offset1];
/* 8797 */       array[offset1] = array[offset2];
/* 8798 */       array[offset2] = aux;
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
/*      */   public static void swap(short[] array, int offset1, int offset2) {
/* 8824 */     swap(array, offset1, offset2, 1);
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
/*      */   public static void swap(short[] array, int offset1, int offset2, int len) {
/* 8852 */     if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
/*      */       return;
/*      */     }
/* 8855 */     if (offset1 < 0) {
/* 8856 */       offset1 = 0;
/*      */     }
/* 8858 */     if (offset2 < 0) {
/* 8859 */       offset2 = 0;
/*      */     }
/* 8861 */     if (offset1 == offset2) {
/*      */       return;
/*      */     }
/* 8864 */     len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
/* 8865 */     for (int i = 0; i < len; i++, offset1++, offset2++) {
/* 8866 */       short aux = array[offset1];
/* 8867 */       array[offset1] = array[offset2];
/* 8868 */       array[offset2] = aux;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T[] toArray(T... items) {
/* 8912 */     return items;
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
/*      */   public static Map<Object, Object> toMap(Object[] array) {
/* 8943 */     if (array == null) {
/* 8944 */       return null;
/*      */     }
/* 8946 */     Map<Object, Object> map = new HashMap<>((int)(array.length * 1.5D));
/* 8947 */     for (int i = 0; i < array.length; i++) {
/* 8948 */       Object object = array[i];
/* 8949 */       if (object instanceof Map.Entry) {
/* 8950 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/* 8951 */         map.put(entry.getKey(), entry.getValue());
/* 8952 */       } else if (object instanceof Object[]) {
/* 8953 */         Object[] entry = (Object[])object;
/* 8954 */         if (entry.length < 2) {
/* 8955 */           throw new IllegalArgumentException("Array element " + i + ", '" + object + "', has a length less than 2");
/*      */         }
/*      */ 
/*      */         
/* 8959 */         map.put(entry[0], entry[1]);
/*      */       } else {
/* 8961 */         throw new IllegalArgumentException("Array element " + i + ", '" + object + "', is neither of type Map.Entry nor an Array");
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 8966 */     return map;
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
/*      */   public static Boolean[] toObject(boolean[] array) {
/* 8978 */     if (array == null) {
/* 8979 */       return null;
/*      */     }
/* 8981 */     if (array.length == 0) {
/* 8982 */       return EMPTY_BOOLEAN_OBJECT_ARRAY;
/*      */     }
/* 8984 */     Boolean[] result = new Boolean[array.length];
/* 8985 */     return setAll(result, i -> array[i] ? Boolean.TRUE : Boolean.FALSE);
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
/*      */   public static Byte[] toObject(byte[] array) {
/* 8997 */     if (array == null) {
/* 8998 */       return null;
/*      */     }
/* 9000 */     if (array.length == 0) {
/* 9001 */       return EMPTY_BYTE_OBJECT_ARRAY;
/*      */     }
/* 9003 */     return setAll(new Byte[array.length], i -> Byte.valueOf(array[i]));
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
/*      */   public static Character[] toObject(char[] array) {
/* 9015 */     if (array == null) {
/* 9016 */       return null;
/*      */     }
/* 9018 */     if (array.length == 0) {
/* 9019 */       return EMPTY_CHARACTER_OBJECT_ARRAY;
/*      */     }
/* 9021 */     return setAll(new Character[array.length], i -> Character.valueOf(array[i]));
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
/*      */   public static Double[] toObject(double[] array) {
/* 9033 */     if (array == null) {
/* 9034 */       return null;
/*      */     }
/* 9036 */     if (array.length == 0) {
/* 9037 */       return EMPTY_DOUBLE_OBJECT_ARRAY;
/*      */     }
/* 9039 */     return setAll(new Double[array.length], i -> Double.valueOf(array[i]));
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
/*      */   public static Float[] toObject(float[] array) {
/* 9051 */     if (array == null) {
/* 9052 */       return null;
/*      */     }
/* 9054 */     if (array.length == 0) {
/* 9055 */       return EMPTY_FLOAT_OBJECT_ARRAY;
/*      */     }
/* 9057 */     return setAll(new Float[array.length], i -> Float.valueOf(array[i]));
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
/*      */   public static Integer[] toObject(int[] array) {
/* 9069 */     if (array == null) {
/* 9070 */       return null;
/*      */     }
/* 9072 */     if (array.length == 0) {
/* 9073 */       return EMPTY_INTEGER_OBJECT_ARRAY;
/*      */     }
/* 9075 */     return setAll(new Integer[array.length], i -> Integer.valueOf(array[i]));
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
/*      */   public static Long[] toObject(long[] array) {
/* 9087 */     if (array == null) {
/* 9088 */       return null;
/*      */     }
/* 9090 */     if (array.length == 0) {
/* 9091 */       return EMPTY_LONG_OBJECT_ARRAY;
/*      */     }
/* 9093 */     return setAll(new Long[array.length], i -> Long.valueOf(array[i]));
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
/*      */   public static Short[] toObject(short[] array) {
/* 9105 */     if (array == null) {
/* 9106 */       return null;
/*      */     }
/* 9108 */     if (array.length == 0) {
/* 9109 */       return EMPTY_SHORT_OBJECT_ARRAY;
/*      */     }
/* 9111 */     return setAll(new Short[array.length], i -> Short.valueOf(array[i]));
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
/*      */   public static boolean[] toPrimitive(Boolean[] array) {
/* 9127 */     return toPrimitive(array, false);
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
/*      */   public static boolean[] toPrimitive(Boolean[] array, boolean valueForNull) {
/* 9141 */     if (array == null) {
/* 9142 */       return null;
/*      */     }
/* 9144 */     if (array.length == 0) {
/* 9145 */       return EMPTY_BOOLEAN_ARRAY;
/*      */     }
/* 9147 */     boolean[] result = new boolean[array.length];
/* 9148 */     for (int i = 0; i < array.length; i++) {
/* 9149 */       Boolean b = array[i];
/* 9150 */       result[i] = (b == null) ? valueForNull : b.booleanValue();
/*      */     } 
/* 9152 */     return result;
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
/*      */   public static byte[] toPrimitive(Byte[] array) {
/* 9166 */     if (array == null) {
/* 9167 */       return null;
/*      */     }
/* 9169 */     if (array.length == 0) {
/* 9170 */       return EMPTY_BYTE_ARRAY;
/*      */     }
/* 9172 */     byte[] result = new byte[array.length];
/* 9173 */     for (int i = 0; i < array.length; i++) {
/* 9174 */       result[i] = array[i].byteValue();
/*      */     }
/* 9176 */     return result;
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
/*      */   public static byte[] toPrimitive(Byte[] array, byte valueForNull) {
/* 9190 */     if (array == null) {
/* 9191 */       return null;
/*      */     }
/* 9193 */     if (array.length == 0) {
/* 9194 */       return EMPTY_BYTE_ARRAY;
/*      */     }
/* 9196 */     byte[] result = new byte[array.length];
/* 9197 */     for (int i = 0; i < array.length; i++) {
/* 9198 */       Byte b = array[i];
/* 9199 */       result[i] = (b == null) ? valueForNull : b.byteValue();
/*      */     } 
/* 9201 */     return result;
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
/*      */   public static char[] toPrimitive(Character[] array) {
/* 9215 */     if (array == null) {
/* 9216 */       return null;
/*      */     }
/* 9218 */     if (array.length == 0) {
/* 9219 */       return EMPTY_CHAR_ARRAY;
/*      */     }
/* 9221 */     char[] result = new char[array.length];
/* 9222 */     for (int i = 0; i < array.length; i++) {
/* 9223 */       result[i] = array[i].charValue();
/*      */     }
/* 9225 */     return result;
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
/*      */   public static char[] toPrimitive(Character[] array, char valueForNull) {
/* 9239 */     if (array == null) {
/* 9240 */       return null;
/*      */     }
/* 9242 */     if (array.length == 0) {
/* 9243 */       return EMPTY_CHAR_ARRAY;
/*      */     }
/* 9245 */     char[] result = new char[array.length];
/* 9246 */     for (int i = 0; i < array.length; i++) {
/* 9247 */       Character b = array[i];
/* 9248 */       result[i] = (b == null) ? valueForNull : b.charValue();
/*      */     } 
/* 9250 */     return result;
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
/*      */   public static double[] toPrimitive(Double[] array) {
/* 9264 */     if (array == null) {
/* 9265 */       return null;
/*      */     }
/* 9267 */     if (array.length == 0) {
/* 9268 */       return EMPTY_DOUBLE_ARRAY;
/*      */     }
/* 9270 */     double[] result = new double[array.length];
/* 9271 */     for (int i = 0; i < array.length; i++) {
/* 9272 */       result[i] = array[i].doubleValue();
/*      */     }
/* 9274 */     return result;
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
/*      */   public static double[] toPrimitive(Double[] array, double valueForNull) {
/* 9288 */     if (array == null) {
/* 9289 */       return null;
/*      */     }
/* 9291 */     if (array.length == 0) {
/* 9292 */       return EMPTY_DOUBLE_ARRAY;
/*      */     }
/* 9294 */     double[] result = new double[array.length];
/* 9295 */     for (int i = 0; i < array.length; i++) {
/* 9296 */       Double b = array[i];
/* 9297 */       result[i] = (b == null) ? valueForNull : b.doubleValue();
/*      */     } 
/* 9299 */     return result;
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
/*      */   public static float[] toPrimitive(Float[] array) {
/* 9313 */     if (array == null) {
/* 9314 */       return null;
/*      */     }
/* 9316 */     if (array.length == 0) {
/* 9317 */       return EMPTY_FLOAT_ARRAY;
/*      */     }
/* 9319 */     float[] result = new float[array.length];
/* 9320 */     for (int i = 0; i < array.length; i++) {
/* 9321 */       result[i] = array[i].floatValue();
/*      */     }
/* 9323 */     return result;
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
/*      */   public static float[] toPrimitive(Float[] array, float valueForNull) {
/* 9337 */     if (array == null) {
/* 9338 */       return null;
/*      */     }
/* 9340 */     if (array.length == 0) {
/* 9341 */       return EMPTY_FLOAT_ARRAY;
/*      */     }
/* 9343 */     float[] result = new float[array.length];
/* 9344 */     for (int i = 0; i < array.length; i++) {
/* 9345 */       Float b = array[i];
/* 9346 */       result[i] = (b == null) ? valueForNull : b.floatValue();
/*      */     } 
/* 9348 */     return result;
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
/*      */   public static int[] toPrimitive(Integer[] array) {
/* 9362 */     if (array == null) {
/* 9363 */       return null;
/*      */     }
/* 9365 */     if (array.length == 0) {
/* 9366 */       return EMPTY_INT_ARRAY;
/*      */     }
/* 9368 */     int[] result = new int[array.length];
/* 9369 */     for (int i = 0; i < array.length; i++) {
/* 9370 */       result[i] = array[i].intValue();
/*      */     }
/* 9372 */     return result;
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
/*      */   public static int[] toPrimitive(Integer[] array, int valueForNull) {
/* 9386 */     if (array == null) {
/* 9387 */       return null;
/*      */     }
/* 9389 */     if (array.length == 0) {
/* 9390 */       return EMPTY_INT_ARRAY;
/*      */     }
/* 9392 */     int[] result = new int[array.length];
/* 9393 */     for (int i = 0; i < array.length; i++) {
/* 9394 */       Integer b = array[i];
/* 9395 */       result[i] = (b == null) ? valueForNull : b.intValue();
/*      */     } 
/* 9397 */     return result;
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
/*      */   public static long[] toPrimitive(Long[] array) {
/* 9411 */     if (array == null) {
/* 9412 */       return null;
/*      */     }
/* 9414 */     if (array.length == 0) {
/* 9415 */       return EMPTY_LONG_ARRAY;
/*      */     }
/* 9417 */     long[] result = new long[array.length];
/* 9418 */     for (int i = 0; i < array.length; i++) {
/* 9419 */       result[i] = array[i].longValue();
/*      */     }
/* 9421 */     return result;
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
/*      */   public static long[] toPrimitive(Long[] array, long valueForNull) {
/* 9435 */     if (array == null) {
/* 9436 */       return null;
/*      */     }
/* 9438 */     if (array.length == 0) {
/* 9439 */       return EMPTY_LONG_ARRAY;
/*      */     }
/* 9441 */     long[] result = new long[array.length];
/* 9442 */     for (int i = 0; i < array.length; i++) {
/* 9443 */       Long b = array[i];
/* 9444 */       result[i] = (b == null) ? valueForNull : b.longValue();
/*      */     } 
/* 9446 */     return result;
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
/*      */   public static Object toPrimitive(Object array) {
/* 9460 */     if (array == null) {
/* 9461 */       return null;
/*      */     }
/* 9463 */     Class<?> ct = array.getClass().getComponentType();
/* 9464 */     Class<?> pt = ClassUtils.wrapperToPrimitive(ct);
/* 9465 */     if (boolean.class.equals(pt)) {
/* 9466 */       return toPrimitive((Boolean[])array);
/*      */     }
/* 9468 */     if (char.class.equals(pt)) {
/* 9469 */       return toPrimitive((Character[])array);
/*      */     }
/* 9471 */     if (byte.class.equals(pt)) {
/* 9472 */       return toPrimitive((Byte[])array);
/*      */     }
/* 9474 */     if (int.class.equals(pt)) {
/* 9475 */       return toPrimitive((Integer[])array);
/*      */     }
/* 9477 */     if (long.class.equals(pt)) {
/* 9478 */       return toPrimitive((Long[])array);
/*      */     }
/* 9480 */     if (short.class.equals(pt)) {
/* 9481 */       return toPrimitive((Short[])array);
/*      */     }
/* 9483 */     if (double.class.equals(pt)) {
/* 9484 */       return toPrimitive((Double[])array);
/*      */     }
/* 9486 */     if (float.class.equals(pt)) {
/* 9487 */       return toPrimitive((Float[])array);
/*      */     }
/* 9489 */     return array;
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
/*      */   public static short[] toPrimitive(Short[] array) {
/* 9503 */     if (array == null) {
/* 9504 */       return null;
/*      */     }
/* 9506 */     if (array.length == 0) {
/* 9507 */       return EMPTY_SHORT_ARRAY;
/*      */     }
/* 9509 */     short[] result = new short[array.length];
/* 9510 */     for (int i = 0; i < array.length; i++) {
/* 9511 */       result[i] = array[i].shortValue();
/*      */     }
/* 9513 */     return result;
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
/*      */   public static short[] toPrimitive(Short[] array, short valueForNull) {
/* 9527 */     if (array == null) {
/* 9528 */       return null;
/*      */     }
/* 9530 */     if (array.length == 0) {
/* 9531 */       return EMPTY_SHORT_ARRAY;
/*      */     }
/* 9533 */     short[] result = new short[array.length];
/* 9534 */     for (int i = 0; i < array.length; i++) {
/* 9535 */       Short b = array[i];
/* 9536 */       result[i] = (b == null) ? valueForNull : b.shortValue();
/*      */     } 
/* 9538 */     return result;
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
/*      */   public static String toString(Object array) {
/* 9555 */     return toString(array, "{}");
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
/*      */   public static String toString(Object array, String stringIfNull) {
/* 9573 */     if (array == null) {
/* 9574 */       return stringIfNull;
/*      */     }
/* 9576 */     return (new ToStringBuilder(array, ToStringStyle.SIMPLE_STYLE)).append(array).toString();
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
/*      */   public static String[] toStringArray(Object[] array) {
/* 9592 */     if (array == null) {
/* 9593 */       return null;
/*      */     }
/* 9595 */     if (array.length == 0) {
/* 9596 */       return EMPTY_STRING_ARRAY;
/*      */     }
/*      */     
/* 9599 */     String[] result = new String[array.length];
/* 9600 */     for (int i = 0; i < array.length; i++) {
/* 9601 */       result[i] = array[i].toString();
/*      */     }
/*      */     
/* 9604 */     return result;
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
/*      */   public static String[] toStringArray(Object[] array, String valueForNullElements) {
/* 9620 */     if (null == array) {
/* 9621 */       return null;
/*      */     }
/* 9623 */     if (array.length == 0) {
/* 9624 */       return EMPTY_STRING_ARRAY;
/*      */     }
/*      */     
/* 9627 */     String[] result = new String[array.length];
/* 9628 */     for (int i = 0; i < array.length; i++) {
/* 9629 */       Object object = array[i];
/* 9630 */       result[i] = (object == null) ? valueForNullElements : object.toString();
/*      */     } 
/*      */     
/* 9633 */     return result;
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\ArrayUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */