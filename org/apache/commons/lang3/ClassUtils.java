/*      */ package org.apache.commons.lang3;
/*      */ 
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.stream.Collectors;
/*      */ import org.apache.commons.lang3.mutable.MutableObject;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ClassUtils
/*      */ {
/*      */   private static final Comparator<Class<?>> COMPARATOR;
/*      */   public static final char PACKAGE_SEPARATOR_CHAR = '.';
/*      */   
/*      */   public enum Interfaces
/*      */   {
/*   61 */     INCLUDE,
/*      */ 
/*      */     
/*   64 */     EXCLUDE; }
/*      */   
/*      */   static {
/*   67 */     COMPARATOR = ((o1, o2) -> Objects.compare(getName(o1), getName(o2), String::compareTo));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   77 */   public static final String PACKAGE_SEPARATOR = String.valueOf('.');
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final char INNER_CLASS_SEPARATOR_CHAR = '$';
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   87 */   public static final String INNER_CLASS_SEPARATOR = String.valueOf('$');
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   92 */   private static final Map<String, Class<?>> namePrimitiveMap = new HashMap<>();
/*      */   
/*      */   static {
/*   95 */     namePrimitiveMap.put("boolean", boolean.class);
/*   96 */     namePrimitiveMap.put("byte", byte.class);
/*   97 */     namePrimitiveMap.put("char", char.class);
/*   98 */     namePrimitiveMap.put("short", short.class);
/*   99 */     namePrimitiveMap.put("int", int.class);
/*  100 */     namePrimitiveMap.put("long", long.class);
/*  101 */     namePrimitiveMap.put("double", double.class);
/*  102 */     namePrimitiveMap.put("float", float.class);
/*  103 */     namePrimitiveMap.put("void", void.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  109 */   private static final Map<Class<?>, Class<?>> primitiveWrapperMap = new HashMap<>();
/*      */   
/*      */   static {
/*  112 */     primitiveWrapperMap.put(boolean.class, Boolean.class);
/*  113 */     primitiveWrapperMap.put(byte.class, Byte.class);
/*  114 */     primitiveWrapperMap.put(char.class, Character.class);
/*  115 */     primitiveWrapperMap.put(short.class, Short.class);
/*  116 */     primitiveWrapperMap.put(int.class, Integer.class);
/*  117 */     primitiveWrapperMap.put(long.class, Long.class);
/*  118 */     primitiveWrapperMap.put(double.class, Double.class);
/*  119 */     primitiveWrapperMap.put(float.class, Float.class);
/*  120 */     primitiveWrapperMap.put(void.class, void.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  126 */   private static final Map<Class<?>, Class<?>> wrapperPrimitiveMap = new HashMap<>();
/*      */   
/*      */   static {
/*  129 */     primitiveWrapperMap.forEach((primitiveClass, wrapperClass) -> {
/*      */           if (!primitiveClass.equals(wrapperClass)) {
/*      */             wrapperPrimitiveMap.put(wrapperClass, primitiveClass);
/*      */           }
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  148 */     Map<String, String> map = new HashMap<>();
/*  149 */     map.put("int", "I");
/*  150 */     map.put("boolean", "Z");
/*  151 */     map.put("float", "F");
/*  152 */     map.put("long", "J");
/*  153 */     map.put("short", "S");
/*  154 */     map.put("byte", "B");
/*  155 */     map.put("double", "D");
/*  156 */     map.put("char", "C");
/*  157 */     abbreviationMap = Collections.unmodifiableMap(map);
/*  158 */     reverseAbbreviationMap = Collections.unmodifiableMap((Map<? extends String, ? extends String>)map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey)));
/*      */   }
/*      */ 
/*      */   
/*      */   private static final Map<String, String> abbreviationMap;
/*      */   
/*      */   private static final Map<String, String> reverseAbbreviationMap;
/*      */ 
/*      */   
/*      */   public static Comparator<Class<?>> comparator() {
/*  168 */     return COMPARATOR;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<String> convertClassesToClassNames(List<Class<?>> classes) {
/*  183 */     return (classes == null) ? null : (List<String>)classes.stream().map(e -> getName(e, (String)null)).collect(Collectors.toList());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<Class<?>> convertClassNamesToClasses(List<String> classNames) {
/*  199 */     if (classNames == null) {
/*  200 */       return null;
/*      */     }
/*  202 */     List<Class<?>> classes = new ArrayList<>(classNames.size());
/*  203 */     classNames.forEach(className -> {
/*      */           try {
/*      */             classes.add(Class.forName(className));
/*  206 */           } catch (Exception ex) {
/*      */             classes.add((Class<?>)null);
/*      */           } 
/*      */         });
/*  210 */     return classes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getAbbreviatedName(Class<?> cls, int lengthHint) {
/*  224 */     if (cls == null) {
/*  225 */       return "";
/*      */     }
/*  227 */     return getAbbreviatedName(cls.getName(), lengthHint);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getAbbreviatedName(String className, int lengthHint) {
/*  304 */     if (lengthHint <= 0) {
/*  305 */       throw new IllegalArgumentException("len must be > 0");
/*      */     }
/*  307 */     if (className == null) {
/*  308 */       return "";
/*      */     }
/*  310 */     if (className.length() <= lengthHint) {
/*  311 */       return className;
/*      */     }
/*  313 */     char[] abbreviated = className.toCharArray();
/*  314 */     int target = 0;
/*  315 */     int source = 0;
/*  316 */     while (source < abbreviated.length) {
/*      */       
/*  318 */       int runAheadTarget = target;
/*  319 */       while (source < abbreviated.length && abbreviated[source] != '.') {
/*  320 */         abbreviated[runAheadTarget++] = abbreviated[source++];
/*      */       }
/*      */       
/*  323 */       target++;
/*  324 */       if (useFull(runAheadTarget, source, abbreviated.length, lengthHint) || target > runAheadTarget) {
/*  325 */         target = runAheadTarget;
/*      */       }
/*      */ 
/*      */       
/*  329 */       if (source < abbreviated.length) {
/*  330 */         abbreviated[target++] = abbreviated[source++];
/*      */       }
/*      */     } 
/*  333 */     return new String(abbreviated, 0, target);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<Class<?>> getAllInterfaces(Class<?> cls) {
/*  349 */     if (cls == null) {
/*  350 */       return null;
/*      */     }
/*      */     
/*  353 */     LinkedHashSet<Class<?>> interfacesFound = new LinkedHashSet<>();
/*  354 */     getAllInterfaces(cls, interfacesFound);
/*      */     
/*  356 */     return new ArrayList<>(interfacesFound);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void getAllInterfaces(Class<?> cls, HashSet<Class<?>> interfacesFound) {
/*  366 */     while (cls != null) {
/*  367 */       Class<?>[] interfaces = cls.getInterfaces();
/*      */       
/*  369 */       for (Class<?> i : interfaces) {
/*  370 */         if (interfacesFound.add(i)) {
/*  371 */           getAllInterfaces(i, interfacesFound);
/*      */         }
/*      */       } 
/*      */       
/*  375 */       cls = cls.getSuperclass();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<Class<?>> getAllSuperclasses(Class<?> cls) {
/*  386 */     if (cls == null) {
/*  387 */       return null;
/*      */     }
/*  389 */     List<Class<?>> classes = new ArrayList<>();
/*  390 */     Class<?> superclass = cls.getSuperclass();
/*  391 */     while (superclass != null) {
/*  392 */       classes.add(superclass);
/*  393 */       superclass = superclass.getSuperclass();
/*      */     } 
/*  395 */     return classes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getCanonicalName(Class<?> cls) {
/*  407 */     return getCanonicalName(cls, "");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getCanonicalName(Class<?> cls, String valueIfNull) {
/*  420 */     if (cls == null) {
/*  421 */       return valueIfNull;
/*      */     }
/*  423 */     String canonicalName = cls.getCanonicalName();
/*  424 */     return (canonicalName == null) ? valueIfNull : canonicalName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getCanonicalName(Object object) {
/*  436 */     return getCanonicalName(object, "");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getCanonicalName(Object object, String valueIfNull) {
/*  449 */     if (object == null) {
/*  450 */       return valueIfNull;
/*      */     }
/*  452 */     String canonicalName = object.getClass().getCanonicalName();
/*  453 */     return (canonicalName == null) ? valueIfNull : canonicalName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String getCanonicalName(String className) {
/*  478 */     className = StringUtils.deleteWhitespace(className);
/*  479 */     if (className == null) {
/*  480 */       return null;
/*      */     }
/*  482 */     int dim = 0;
/*  483 */     while (className.startsWith("[")) {
/*  484 */       dim++;
/*  485 */       className = className.substring(1);
/*      */     } 
/*  487 */     if (dim < 1) {
/*  488 */       return className;
/*      */     }
/*  490 */     if (className.startsWith("L")) {
/*  491 */       className = className.substring(1, className.endsWith(";") ? (className.length() - 1) : className.length());
/*  492 */     } else if (!className.isEmpty()) {
/*  493 */       className = reverseAbbreviationMap.get(className.substring(0, 1));
/*      */     } 
/*  495 */     StringBuilder canonicalClassNameBuffer = new StringBuilder(className);
/*  496 */     for (int i = 0; i < dim; i++) {
/*  497 */       canonicalClassNameBuffer.append("[]");
/*      */     }
/*  499 */     return canonicalClassNameBuffer.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> getClass(ClassLoader classLoader, String className) throws ClassNotFoundException {
/*  514 */     return getClass(classLoader, className, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> getClass(ClassLoader classLoader, String className, boolean initialize) throws ClassNotFoundException {
/*      */     try {
/*  531 */       Class<?> clazz = namePrimitiveMap.get(className);
/*  532 */       return (clazz != null) ? clazz : Class.forName(toCanonicalName(className), initialize, classLoader);
/*  533 */     } catch (ClassNotFoundException ex) {
/*      */       
/*  535 */       int lastDotIndex = className.lastIndexOf('.');
/*      */       
/*  537 */       if (lastDotIndex != -1) {
/*      */         try {
/*  539 */           return getClass(classLoader, className.substring(0, lastDotIndex) + '$' + className.substring(lastDotIndex + 1), initialize);
/*      */         }
/*  541 */         catch (ClassNotFoundException classNotFoundException) {}
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  546 */       throw ex;
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
/*      */   public static Class<?> getClass(String className) throws ClassNotFoundException {
/*  561 */     return getClass(className, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> getClass(String className, boolean initialize) throws ClassNotFoundException {
/*  576 */     ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
/*  577 */     ClassLoader loader = (contextCL == null) ? ClassUtils.class.getClassLoader() : contextCL;
/*  578 */     return getClass(loader, className, initialize);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Class<T> getComponentType(Class<T[]> cls) {
/*  592 */     return (cls == null) ? null : (Class)cls.getComponentType();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getName(Class<?> cls) {
/*  604 */     return getName(cls, "");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getName(Class<?> cls, String valueIfNull) {
/*  617 */     return (cls == null) ? valueIfNull : cls.getName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getName(Object object) {
/*  629 */     return getName(object, "");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getName(Object object, String valueIfNull) {
/*  642 */     return (object == null) ? valueIfNull : object.getClass().getName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getPackageCanonicalName(Class<?> cls) {
/*  653 */     if (cls == null) {
/*  654 */       return "";
/*      */     }
/*  656 */     return getPackageCanonicalName(cls.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getPackageCanonicalName(Object object, String valueIfNull) {
/*  668 */     if (object == null) {
/*  669 */       return valueIfNull;
/*      */     }
/*  671 */     return getPackageCanonicalName(object.getClass().getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getPackageCanonicalName(String name) {
/*  689 */     return getPackageName(getCanonicalName(name));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getPackageName(Class<?> cls) {
/*  699 */     if (cls == null) {
/*  700 */       return "";
/*      */     }
/*  702 */     return getPackageName(cls.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getPackageName(Object object, String valueIfNull) {
/*  713 */     if (object == null) {
/*  714 */       return valueIfNull;
/*      */     }
/*  716 */     return getPackageName(object.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getPackageName(String className) {
/*  733 */     if (StringUtils.isEmpty(className)) {
/*  734 */       return "";
/*      */     }
/*      */ 
/*      */     
/*  738 */     while (className.charAt(0) == '[') {
/*  739 */       className = className.substring(1);
/*      */     }
/*      */     
/*  742 */     if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
/*  743 */       className = className.substring(1);
/*      */     }
/*      */     
/*  746 */     int i = className.lastIndexOf('.');
/*  747 */     if (i == -1) {
/*  748 */       return "";
/*      */     }
/*  750 */     return className.substring(0, i);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Method getPublicMethod(Class<?> cls, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
/*  775 */     Method declaredMethod = cls.getMethod(methodName, parameterTypes);
/*  776 */     if (isPublic(declaredMethod.getDeclaringClass())) {
/*  777 */       return declaredMethod;
/*      */     }
/*      */     
/*  780 */     List<Class<?>> candidateClasses = new ArrayList<>(getAllInterfaces(cls));
/*  781 */     candidateClasses.addAll(getAllSuperclasses(cls));
/*      */     
/*  783 */     for (Class<?> candidateClass : candidateClasses) {
/*  784 */       Method candidateMethod; if (!isPublic(candidateClass)) {
/*      */         continue;
/*      */       }
/*      */       
/*      */       try {
/*  789 */         candidateMethod = candidateClass.getMethod(methodName, parameterTypes);
/*  790 */       } catch (NoSuchMethodException ex) {
/*      */         continue;
/*      */       } 
/*  793 */       if (Modifier.isPublic(candidateMethod.getDeclaringClass().getModifiers())) {
/*  794 */         return candidateMethod;
/*      */       }
/*      */     } 
/*      */     
/*  798 */     throw new NoSuchMethodException("Can't find a public method for " + methodName + " " + ArrayUtils.toString(parameterTypes));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShortCanonicalName(Class<?> cls) {
/*  810 */     return (cls == null) ? "" : getShortCanonicalName(cls.getCanonicalName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShortCanonicalName(Object object, String valueIfNull) {
/*  823 */     return (object == null) ? valueIfNull : getShortCanonicalName(object.getClass().getCanonicalName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShortCanonicalName(String canonicalName) {
/*  921 */     return getShortClassName(getCanonicalName(canonicalName));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShortClassName(Class<?> cls) {
/*  937 */     if (cls == null) {
/*  938 */       return "";
/*      */     }
/*  940 */     return getShortClassName(cls.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShortClassName(Object object, String valueIfNull) {
/*  957 */     if (object == null) {
/*  958 */       return valueIfNull;
/*      */     }
/*  960 */     return getShortClassName(object.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShortClassName(String className) {
/*  995 */     if (StringUtils.isEmpty(className)) {
/*  996 */       return "";
/*      */     }
/*      */     
/*  999 */     StringBuilder arrayPrefix = new StringBuilder();
/*      */ 
/*      */     
/* 1002 */     if (className.startsWith("[")) {
/* 1003 */       while (className.charAt(0) == '[') {
/* 1004 */         className = className.substring(1);
/* 1005 */         arrayPrefix.append("[]");
/*      */       } 
/*      */       
/* 1008 */       if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
/* 1009 */         className = className.substring(1, className.length() - 1);
/*      */       }
/*      */       
/* 1012 */       if (reverseAbbreviationMap.containsKey(className)) {
/* 1013 */         className = reverseAbbreviationMap.get(className);
/*      */       }
/*      */     } 
/*      */     
/* 1017 */     int lastDotIdx = className.lastIndexOf('.');
/* 1018 */     int innerIdx = className.indexOf('$', (lastDotIdx == -1) ? 0 : (lastDotIdx + 1));
/* 1019 */     String out = className.substring(lastDotIdx + 1);
/* 1020 */     if (innerIdx != -1) {
/* 1021 */       out = out.replace('$', '.');
/*      */     }
/* 1023 */     return out + arrayPrefix;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getSimpleName(Class<?> cls) {
/* 1035 */     return getSimpleName(cls, "");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getSimpleName(Class<?> cls, String valueIfNull) {
/* 1048 */     return (cls == null) ? valueIfNull : cls.getSimpleName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getSimpleName(Object object) {
/* 1068 */     return getSimpleName(object, "");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getSimpleName(Object object, String valueIfNull) {
/* 1081 */     return (object == null) ? valueIfNull : object.getClass().getSimpleName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Iterable<Class<?>> hierarchy(Class<?> type) {
/* 1093 */     return hierarchy(type, Interfaces.EXCLUDE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Iterable<Class<?>> hierarchy(Class<?> type, Interfaces interfacesBehavior) {
/* 1105 */     Iterable<Class<?>> classes = () -> {
/*      */         MutableObject<Class<?>> next = new MutableObject(type);
/*      */         return new Iterator<Class<?>>()
/*      */           {
/*      */             public boolean hasNext()
/*      */             {
/* 1111 */               return (next.getValue() != null);
/*      */             }
/*      */ 
/*      */             
/*      */             public Class<?> next() {
/* 1116 */               Class<?> result = (Class)next.getValue();
/* 1117 */               next.setValue(result.getSuperclass());
/* 1118 */               return result;
/*      */             }
/*      */ 
/*      */             
/*      */             public void remove() {
/* 1123 */               throw new UnsupportedOperationException();
/*      */             }
/*      */           };
/*      */       };
/*      */     
/* 1128 */     if (interfacesBehavior != Interfaces.INCLUDE) {
/* 1129 */       return classes;
/*      */     }
/* 1131 */     return () -> {
/*      */         final Set<Class<?>> seenInterfaces = new HashSet<>();
/*      */         final Iterator<Class<?>> wrapped = classes.iterator();
/*      */         return new Iterator<Class<?>>()
/*      */           {
/* 1136 */             Iterator interfaces = Collections.emptyIterator();
/*      */ 
/*      */             
/*      */             public boolean hasNext() {
/* 1140 */               return (this.interfaces.hasNext() || wrapped.hasNext());
/*      */             }
/*      */ 
/*      */             
/*      */             public Class<?> next() {
/* 1145 */               if (this.interfaces.hasNext()) {
/* 1146 */                 Class<?> nextInterface = this.interfaces.next();
/* 1147 */                 seenInterfaces.add(nextInterface);
/* 1148 */                 return nextInterface;
/*      */               } 
/* 1150 */               Class<?> nextSuperclass = wrapped.next();
/* 1151 */               Set<Class<?>> currentInterfaces = new LinkedHashSet();
/* 1152 */               walkInterfaces(currentInterfaces, nextSuperclass);
/* 1153 */               this.interfaces = currentInterfaces.iterator();
/* 1154 */               return nextSuperclass;
/*      */             }
/*      */ 
/*      */             
/*      */             public void remove() {
/* 1159 */               throw new UnsupportedOperationException();
/*      */             }
/*      */             
/*      */             private void walkInterfaces(Set<Class<?>> addTo, Class c) {
/* 1163 */               for (Class<?> iface : c.getInterfaces()) {
/* 1164 */                 if (!seenInterfaces.contains(iface)) {
/* 1165 */                   addTo.add(iface);
/*      */                 }
/* 1167 */                 walkInterfaces(addTo, iface);
/*      */               } 
/*      */             }
/*      */           };
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isAssignable(Class<?> cls, Class<?> toClass) {
/* 1211 */     return isAssignable(cls, toClass, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isAssignable(Class<?> cls, Class<?> toClass, boolean autoboxing) {
/* 1245 */     if (toClass == null) {
/* 1246 */       return false;
/*      */     }
/*      */     
/* 1249 */     if (cls == null) {
/* 1250 */       return !toClass.isPrimitive();
/*      */     }
/*      */     
/* 1253 */     if (autoboxing) {
/* 1254 */       if (cls.isPrimitive() && !toClass.isPrimitive()) {
/* 1255 */         cls = primitiveToWrapper(cls);
/* 1256 */         if (cls == null) {
/* 1257 */           return false;
/*      */         }
/*      */       } 
/* 1260 */       if (toClass.isPrimitive() && !cls.isPrimitive()) {
/* 1261 */         cls = wrapperToPrimitive(cls);
/* 1262 */         if (cls == null) {
/* 1263 */           return false;
/*      */         }
/*      */       } 
/*      */     } 
/* 1267 */     if (cls.equals(toClass)) {
/* 1268 */       return true;
/*      */     }
/* 1270 */     if (cls.isPrimitive()) {
/* 1271 */       if (!toClass.isPrimitive()) {
/* 1272 */         return false;
/*      */       }
/* 1274 */       if (int.class.equals(cls)) {
/* 1275 */         return (long.class.equals(toClass) || float.class.equals(toClass) || double.class.equals(toClass));
/*      */       }
/* 1277 */       if (long.class.equals(cls)) {
/* 1278 */         return (float.class.equals(toClass) || double.class.equals(toClass));
/*      */       }
/* 1280 */       if (boolean.class.equals(cls)) {
/* 1281 */         return false;
/*      */       }
/* 1283 */       if (double.class.equals(cls)) {
/* 1284 */         return false;
/*      */       }
/* 1286 */       if (float.class.equals(cls)) {
/* 1287 */         return double.class.equals(toClass);
/*      */       }
/* 1289 */       if (char.class.equals(cls) || short.class.equals(cls)) {
/* 1290 */         return (int.class.equals(toClass) || long.class.equals(toClass) || float.class.equals(toClass) || double.class.equals(toClass));
/*      */       }
/* 1292 */       if (byte.class.equals(cls)) {
/* 1293 */         return (short.class.equals(toClass) || int.class.equals(toClass) || long.class.equals(toClass) || float.class.equals(toClass) || double.class
/* 1294 */           .equals(toClass));
/*      */       }
/*      */       
/* 1297 */       return false;
/*      */     } 
/* 1299 */     return toClass.isAssignableFrom(cls);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isAssignable(Class<?>[] classArray, Class<?>... toClassArray) {
/* 1344 */     return isAssignable(classArray, toClassArray, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isAssignable(Class<?>[] classArray, Class<?>[] toClassArray, boolean autoboxing) {
/* 1384 */     if (!ArrayUtils.isSameLength((Object[])classArray, (Object[])toClassArray)) {
/* 1385 */       return false;
/*      */     }
/* 1387 */     if (classArray == null) {
/* 1388 */       classArray = ArrayUtils.EMPTY_CLASS_ARRAY;
/*      */     }
/* 1390 */     if (toClassArray == null) {
/* 1391 */       toClassArray = ArrayUtils.EMPTY_CLASS_ARRAY;
/*      */     }
/* 1393 */     for (int i = 0; i < classArray.length; i++) {
/* 1394 */       if (!isAssignable(classArray[i], toClassArray[i], autoboxing)) {
/* 1395 */         return false;
/*      */       }
/*      */     } 
/* 1398 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isInnerClass(Class<?> cls) {
/* 1408 */     return (cls != null && cls.getEnclosingClass() != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPublic(Class<?> cls) {
/* 1418 */     return Modifier.isPublic(cls.getModifiers());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrimitiveOrWrapper(Class<?> type) {
/* 1430 */     if (type == null) {
/* 1431 */       return false;
/*      */     }
/* 1433 */     return (type.isPrimitive() || isPrimitiveWrapper(type));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrimitiveWrapper(Class<?> type) {
/* 1446 */     return wrapperPrimitiveMap.containsKey(type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?>[] primitivesToWrappers(Class<?>... classes) {
/* 1458 */     if (classes == null) {
/* 1459 */       return null;
/*      */     }
/*      */     
/* 1462 */     if (classes.length == 0) {
/* 1463 */       return classes;
/*      */     }
/*      */     
/* 1466 */     Class<?>[] convertedClasses = new Class[classes.length];
/* 1467 */     Arrays.setAll(convertedClasses, i -> primitiveToWrapper(classes[i]));
/* 1468 */     return convertedClasses;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> primitiveToWrapper(Class<?> cls) {
/* 1484 */     Class<?> convertedClass = cls;
/* 1485 */     if (cls != null && cls.isPrimitive()) {
/* 1486 */       convertedClass = primitiveWrapperMap.get(cls);
/*      */     }
/* 1488 */     return convertedClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String toCanonicalName(String className) {
/* 1499 */     String canonicalName = StringUtils.deleteWhitespace(className);
/* 1500 */     Objects.requireNonNull(canonicalName, "className");
/* 1501 */     if (canonicalName.endsWith("[]")) {
/* 1502 */       StringBuilder classNameBuffer = new StringBuilder();
/* 1503 */       while (canonicalName.endsWith("[]")) {
/* 1504 */         canonicalName = canonicalName.substring(0, canonicalName.length() - 2);
/* 1505 */         classNameBuffer.append("[");
/*      */       } 
/* 1507 */       String abbreviation = abbreviationMap.get(canonicalName);
/* 1508 */       if (abbreviation != null) {
/* 1509 */         classNameBuffer.append(abbreviation);
/*      */       } else {
/* 1511 */         classNameBuffer.append("L").append(canonicalName).append(";");
/*      */       } 
/* 1513 */       canonicalName = classNameBuffer.toString();
/*      */     } 
/* 1515 */     return canonicalName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?>[] toClass(Object... array) {
/* 1531 */     if (array == null) {
/* 1532 */       return null;
/*      */     }
/* 1534 */     if (array.length == 0) {
/* 1535 */       return ArrayUtils.EMPTY_CLASS_ARRAY;
/*      */     }
/* 1537 */     Class<?>[] classes = new Class[array.length];
/* 1538 */     Arrays.setAll(classes, i -> (array[i] == null) ? null : array[i].getClass());
/* 1539 */     return classes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean useFull(int runAheadTarget, int source, int originalLength, int desiredLength) {
/* 1564 */     return (source >= originalLength || runAheadTarget + originalLength - source <= desiredLength);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?>[] wrappersToPrimitives(Class<?>... classes) {
/* 1581 */     if (classes == null) {
/* 1582 */       return null;
/*      */     }
/*      */     
/* 1585 */     if (classes.length == 0) {
/* 1586 */       return classes;
/*      */     }
/*      */     
/* 1589 */     Class<?>[] convertedClasses = new Class[classes.length];
/* 1590 */     Arrays.setAll(convertedClasses, i -> wrapperToPrimitive(classes[i]));
/* 1591 */     return convertedClasses;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> wrapperToPrimitive(Class<?> cls) {
/* 1609 */     return wrapperPrimitiveMap.get(cls);
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\ClassUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */