/*      */ package org.apache.commons.lang3;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.time.Duration;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.TreeSet;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.stream.Stream;
/*      */ import org.apache.commons.lang3.exception.CloneFailedException;
/*      */ import org.apache.commons.lang3.function.Suppliers;
/*      */ import org.apache.commons.lang3.mutable.MutableInt;
/*      */ import org.apache.commons.lang3.stream.Streams;
/*      */ import org.apache.commons.lang3.text.StrBuilder;
/*      */ import org.apache.commons.lang3.time.DurationUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ObjectUtils
/*      */ {
/*      */   private static final char AT_SIGN = '@';
/*      */   
/*      */   public static class Null
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 7092611880189329093L;
/*      */     
/*      */     private Object readResolve() {
/*   93 */       return ObjectUtils.NULL;
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
/*  114 */   public static final Null NULL = new Null();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean allNotNull(Object... values) {
/*  142 */     return (values != null && Stream.<Object>of(values).noneMatch(Objects::isNull));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean allNull(Object... values) {
/*  168 */     return !anyNotNull(values);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean anyNotNull(Object... values) {
/*  195 */     return (firstNonNull(values) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean anyNull(Object... values) {
/*  223 */     return !allNotNull(values);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T clone(T obj) {
/*  236 */     if (obj instanceof Cloneable) {
/*      */       Object result;
/*  238 */       if (isArray(obj)) {
/*  239 */         Class<?> componentType = obj.getClass().getComponentType();
/*  240 */         if (componentType.isPrimitive()) {
/*  241 */           int length = Array.getLength(obj);
/*  242 */           result = Array.newInstance(componentType, length);
/*  243 */           while (length-- > 0) {
/*  244 */             Array.set(result, length, Array.get(obj, length));
/*      */           }
/*      */         } else {
/*  247 */           result = ((Object[])obj).clone();
/*      */         } 
/*      */       } else {
/*      */         try {
/*  251 */           Method clone = obj.getClass().getMethod("clone", new Class[0]);
/*  252 */           result = clone.invoke(obj, new Object[0]);
/*  253 */         } catch (NoSuchMethodException e) {
/*  254 */           throw new CloneFailedException("Cloneable type " + obj
/*  255 */               .getClass().getName() + " has no clone method", e);
/*      */         }
/*  257 */         catch (IllegalAccessException e) {
/*  258 */           throw new CloneFailedException("Cannot clone Cloneable type " + obj
/*  259 */               .getClass().getName(), e);
/*  260 */         } catch (InvocationTargetException e) {
/*  261 */           throw new CloneFailedException("Exception cloning Cloneable type " + obj
/*  262 */               .getClass().getName(), e.getCause());
/*      */         } 
/*      */       } 
/*      */       
/*  266 */       T checked = (T)result;
/*  267 */       return checked;
/*      */     } 
/*      */     
/*  270 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T cloneIfPossible(T obj) {
/*  290 */     T clone = clone(obj);
/*  291 */     return (clone == null) ? obj : clone;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends Comparable<? super T>> int compare(T c1, T c2) {
/*  306 */     return compare(c1, c2, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends Comparable<? super T>> int compare(T c1, T c2, boolean nullGreater) {
/*  324 */     if (c1 == c2) {
/*  325 */       return 0;
/*      */     }
/*  327 */     if (c1 == null) {
/*  328 */       return nullGreater ? 1 : -1;
/*      */     }
/*  330 */     if (c2 == null) {
/*  331 */       return nullGreater ? -1 : 1;
/*      */     }
/*  333 */     return c1.compareTo(c2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean CONST(boolean v) {
/*  354 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte CONST(byte v) {
/*  375 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char CONST(char v) {
/*  396 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double CONST(double v) {
/*  417 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float CONST(float v) {
/*  438 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int CONST(int v) {
/*  459 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long CONST(long v) {
/*  480 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short CONST(short v) {
/*  501 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T CONST(T v) {
/*  523 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte CONST_BYTE(int v) {
/*  547 */     if (v < -128 || v > 127) {
/*  548 */       throw new IllegalArgumentException("Supplied value must be a valid byte literal between -128 and 127: [" + v + "]");
/*      */     }
/*  550 */     return (byte)v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short CONST_SHORT(int v) {
/*  574 */     if (v < -32768 || v > 32767) {
/*  575 */       throw new IllegalArgumentException("Supplied value must be a valid byte literal between -32768 and 32767: [" + v + "]");
/*      */     }
/*  577 */     return (short)v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T defaultIfNull(T object, T defaultValue) {
/*  598 */     return (object != null) ? object : defaultValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static boolean equals(Object object1, Object object2) {
/*  625 */     return Objects.equals(object1, object2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> T firstNonNull(T... values) {
/*  652 */     return Streams.of((Object[])values).filter(Objects::nonNull).findFirst().orElse(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Class<T> getClass(T object) {
/*  665 */     return (object == null) ? null : (Class)object.getClass();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> T getFirstNonNull(Supplier<T>... suppliers) {
/*  694 */     return Streams.of((Object[])suppliers).map(s -> (s != null) ? s.get() : null).filter(Objects::nonNull).findFirst().orElse(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T getIfNull(T object, Supplier<T> defaultSupplier) {
/*  721 */     return (object != null) ? object : (T)Suppliers.get(defaultSupplier);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static int hashCode(Object obj) {
/*  742 */     return Objects.hashCode(obj);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String hashCodeHex(Object object) {
/*  756 */     return Integer.toHexString(Objects.hashCode(object));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static int hashCodeMulti(Object... objects) {
/*  784 */     int hash = 1;
/*  785 */     if (objects != null) {
/*  786 */       for (Object object : objects) {
/*  787 */         int tmpHash = Objects.hashCode(object);
/*  788 */         hash = hash * 31 + tmpHash;
/*      */       } 
/*      */     }
/*  791 */     return hash;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String identityHashCodeHex(Object object) {
/*  805 */     return Integer.toHexString(System.identityHashCode(object));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void identityToString(Appendable appendable, Object object) throws IOException {
/*  825 */     Objects.requireNonNull(object, "object");
/*  826 */     appendable.append(object.getClass().getName())
/*  827 */       .append('@')
/*  828 */       .append(identityHashCodeHex(object));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String identityToString(Object object) {
/*  848 */     if (object == null) {
/*  849 */       return null;
/*      */     }
/*  851 */     String name = object.getClass().getName();
/*  852 */     String hexString = identityHashCodeHex(object);
/*  853 */     StringBuilder builder = new StringBuilder(name.length() + 1 + hexString.length());
/*      */     
/*  855 */     builder.append(name)
/*  856 */       .append('@')
/*  857 */       .append(hexString);
/*      */     
/*  859 */     return builder.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static void identityToString(StrBuilder builder, Object object) {
/*  881 */     Objects.requireNonNull(object, "object");
/*  882 */     String name = object.getClass().getName();
/*  883 */     String hexString = identityHashCodeHex(object);
/*  884 */     builder.ensureCapacity(builder.length() + name.length() + 1 + hexString.length());
/*  885 */     builder.append(name)
/*  886 */       .append('@')
/*  887 */       .append(hexString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void identityToString(StringBuffer buffer, Object object) {
/*  906 */     Objects.requireNonNull(object, "object");
/*  907 */     String name = object.getClass().getName();
/*  908 */     String hexString = identityHashCodeHex(object);
/*  909 */     buffer.ensureCapacity(buffer.length() + name.length() + 1 + hexString.length());
/*  910 */     buffer.append(name)
/*  911 */       .append('@')
/*  912 */       .append(hexString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void identityToString(StringBuilder builder, Object object) {
/*  931 */     Objects.requireNonNull(object, "object");
/*  932 */     String name = object.getClass().getName();
/*  933 */     String hexString = identityHashCodeHex(object);
/*  934 */     builder.ensureCapacity(builder.length() + name.length() + 1 + hexString.length());
/*  935 */     builder.append(name)
/*  936 */       .append('@')
/*  937 */       .append(hexString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isArray(Object object) {
/*  982 */     return (object != null && object.getClass().isArray());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(Object object) {
/* 1015 */     if (object == null) {
/* 1016 */       return true;
/*      */     }
/* 1018 */     if (object instanceof CharSequence) {
/* 1019 */       return (((CharSequence)object).length() == 0);
/*      */     }
/* 1021 */     if (isArray(object)) {
/* 1022 */       return (Array.getLength(object) == 0);
/*      */     }
/* 1024 */     if (object instanceof Collection) {
/* 1025 */       return ((Collection)object).isEmpty();
/*      */     }
/* 1027 */     if (object instanceof Map) {
/* 1028 */       return ((Map)object).isEmpty();
/*      */     }
/* 1030 */     if (object instanceof Optional)
/*      */     {
/* 1032 */       return !((Optional)object).isPresent();
/*      */     }
/* 1034 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(Object object) {
/* 1066 */     return !isEmpty(object);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T extends Comparable<? super T>> T max(T... values) {
/* 1085 */     T result = null;
/* 1086 */     if (values != null) {
/* 1087 */       for (T value : values) {
/* 1088 */         if (compare(value, result, false) > 0) {
/* 1089 */           result = value;
/*      */         }
/*      */       } 
/*      */     }
/* 1093 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> T median(Comparator<T> comparator, T... items) {
/* 1109 */     Validate.notEmpty(items, "null/empty items", new Object[0]);
/* 1110 */     Validate.noNullElements(items);
/* 1111 */     Objects.requireNonNull(comparator, "comparator");
/* 1112 */     TreeSet<T> treeSet = new TreeSet<>(comparator);
/* 1113 */     Collections.addAll(treeSet, items);
/*      */     
/* 1115 */     T result = (T)treeSet.toArray()[(treeSet.size() - 1) / 2];
/* 1116 */     return result;
/*      */   }
/*      */ 
/*      */ 
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
/*      */   public static <T extends Comparable<? super T>> T median(T... items) {
/* 1131 */     Validate.notEmpty(items);
/* 1132 */     Validate.noNullElements(items);
/* 1133 */     TreeSet<T> sort = new TreeSet<>();
/* 1134 */     Collections.addAll(sort, items);
/*      */     
/* 1136 */     return (T)sort.toArray()[(sort.size() - 1) / 2];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T extends Comparable<? super T>> T min(T... values) {
/* 1156 */     T result = null;
/* 1157 */     if (values != null) {
/* 1158 */       for (T value : values) {
/* 1159 */         if (compare(value, result, true) < 0) {
/* 1160 */           result = value;
/*      */         }
/*      */       } 
/*      */     }
/* 1164 */     return result;
/*      */   }
/*      */ 
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
/*      */   public static <T> T mode(T... items) {
/* 1178 */     if (ArrayUtils.isNotEmpty(items)) {
/* 1179 */       HashMap<T, MutableInt> occurrences = new HashMap<>(items.length);
/* 1180 */       for (T t : items) {
/* 1181 */         MutableInt count = occurrences.get(t);
/* 1182 */         if (count == null) {
/* 1183 */           occurrences.put(t, new MutableInt(1));
/*      */         } else {
/* 1185 */           count.increment();
/*      */         } 
/*      */       } 
/* 1188 */       T result = null;
/* 1189 */       int max = 0;
/* 1190 */       for (Map.Entry<T, MutableInt> e : occurrences.entrySet()) {
/* 1191 */         int cmp = ((MutableInt)e.getValue()).intValue();
/* 1192 */         if (cmp == max) {
/* 1193 */           result = null; continue;
/* 1194 */         }  if (cmp > max) {
/* 1195 */           max = cmp;
/* 1196 */           result = e.getKey();
/*      */         } 
/*      */       } 
/* 1199 */       return result;
/*      */     } 
/* 1201 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean notEqual(Object object1, Object object2) {
/* 1224 */     return !Objects.equals(object1, object2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T requireNonEmpty(T obj) {
/* 1250 */     return requireNonEmpty(obj, "object");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T requireNonEmpty(T obj, String message) {
/* 1278 */     Objects.requireNonNull(obj, message);
/* 1279 */     if (isEmpty(obj)) {
/* 1280 */       throw new IllegalArgumentException(message);
/*      */     }
/* 1282 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static String toString(Object obj) {
/* 1307 */     return (obj == null) ? "" : obj.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static String toString(Object obj, String nullStr) {
/* 1333 */     return (obj == null) ? nullStr : obj.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(Object obj, Supplier<String> supplier) {
/* 1357 */     return (obj == null) ? (String)Suppliers.get(supplier) : obj.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void wait(Object obj, Duration duration) throws InterruptedException {
/* 1374 */     DurationUtils.accept(obj::wait, DurationUtils.zeroIfNull(duration));
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\ObjectUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */