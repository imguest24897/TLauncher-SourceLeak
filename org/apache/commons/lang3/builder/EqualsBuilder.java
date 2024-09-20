/*      */ package org.apache.commons.lang3.builder;
/*      */ 
/*      */ import java.lang.reflect.AccessibleObject;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import org.apache.commons.lang3.ArrayUtils;
/*      */ import org.apache.commons.lang3.ClassUtils;
/*      */ import org.apache.commons.lang3.tuple.Pair;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class EqualsBuilder
/*      */   implements Builder<Boolean>
/*      */ {
/*   96 */   private static final ThreadLocal<Set<Pair<IDKey, IDKey>>> REGISTRY = new ThreadLocal<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Set<Pair<IDKey, IDKey>> getRegistry() {
/*  123 */     return REGISTRY.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Pair<IDKey, IDKey> getRegisterPair(Object lhs, Object rhs) {
/*  135 */     IDKey left = new IDKey(lhs);
/*  136 */     IDKey right = new IDKey(rhs);
/*  137 */     return Pair.of(left, right);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean isRegistered(Object lhs, Object rhs) {
/*  152 */     Set<Pair<IDKey, IDKey>> registry = getRegistry();
/*  153 */     Pair<IDKey, IDKey> pair = getRegisterPair(lhs, rhs);
/*  154 */     Pair<IDKey, IDKey> swappedPair = Pair.of(pair.getRight(), pair.getLeft());
/*      */     
/*  156 */     return (registry != null && (registry
/*  157 */       .contains(pair) || registry.contains(swappedPair)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void register(Object lhs, Object rhs) {
/*  168 */     Set<Pair<IDKey, IDKey>> registry = getRegistry();
/*  169 */     if (registry == null) {
/*  170 */       registry = new HashSet<>();
/*  171 */       REGISTRY.set(registry);
/*      */     } 
/*  173 */     Pair<IDKey, IDKey> pair = getRegisterPair(lhs, rhs);
/*  174 */     registry.add(pair);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void unregister(Object lhs, Object rhs) {
/*  188 */     Set<Pair<IDKey, IDKey>> registry = getRegistry();
/*  189 */     if (registry != null) {
/*  190 */       registry.remove(getRegisterPair(lhs, rhs));
/*  191 */       if (registry.isEmpty()) {
/*  192 */         REGISTRY.remove();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isEquals = true;
/*      */ 
/*      */   
/*      */   private boolean testTransients;
/*      */ 
/*      */   
/*      */   private boolean testRecursive;
/*      */ 
/*      */   
/*      */   private List<Class<?>> bypassReflectionClasses;
/*      */ 
/*      */   
/*      */   private Class<?> reflectUpToClass;
/*      */   
/*      */   private String[] excludeFields;
/*      */ 
/*      */   
/*      */   public EqualsBuilder() {
/*  217 */     this.bypassReflectionClasses = new ArrayList<>(1);
/*  218 */     this.bypassReflectionClasses.add(String.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EqualsBuilder setTestTransients(boolean testTransients) {
/*  228 */     this.testTransients = testTransients;
/*  229 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EqualsBuilder setTestRecursive(boolean testRecursive) {
/*  242 */     this.testRecursive = testRecursive;
/*  243 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EqualsBuilder setBypassReflectionClasses(List<Class<?>> bypassReflectionClasses) {
/*  260 */     this.bypassReflectionClasses = bypassReflectionClasses;
/*  261 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EqualsBuilder setReflectUpToClass(Class<?> reflectUpToClass) {
/*  271 */     this.reflectUpToClass = reflectUpToClass;
/*  272 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EqualsBuilder setExcludeFields(String... excludeFields) {
/*  282 */     this.excludeFields = excludeFields;
/*  283 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean reflectionEquals(Object lhs, Object rhs, Collection<String> excludeFields) {
/*  310 */     return reflectionEquals(lhs, rhs, ReflectionToStringBuilder.toNoNullStringArray(excludeFields));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean reflectionEquals(Object lhs, Object rhs, String... excludeFields) {
/*  336 */     return reflectionEquals(lhs, rhs, false, null, excludeFields);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients) {
/*  363 */     return reflectionEquals(lhs, rhs, testTransients, null, new String[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients, Class<?> reflectUpToClass, String... excludeFields) {
/*  397 */     return reflectionEquals(lhs, rhs, testTransients, reflectUpToClass, false, excludeFields);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients, Class<?> reflectUpToClass, boolean testRecursive, String... excludeFields) {
/*  438 */     if (lhs == rhs) {
/*  439 */       return true;
/*      */     }
/*  441 */     if (lhs == null || rhs == null) {
/*  442 */       return false;
/*      */     }
/*  444 */     return (new EqualsBuilder())
/*  445 */       .setExcludeFields(excludeFields)
/*  446 */       .setReflectUpToClass(reflectUpToClass)
/*  447 */       .setTestTransients(testTransients)
/*  448 */       .setTestRecursive(testRecursive)
/*  449 */       .reflectionAppend(lhs, rhs)
/*  450 */       .isEquals();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EqualsBuilder reflectionAppend(Object lhs, Object rhs) {
/*      */     Class<?> testClass;
/*  481 */     if (!this.isEquals) {
/*  482 */       return this;
/*      */     }
/*  484 */     if (lhs == rhs) {
/*  485 */       return this;
/*      */     }
/*  487 */     if (lhs == null || rhs == null) {
/*  488 */       this.isEquals = false;
/*  489 */       return this;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  496 */     Class<?> lhsClass = lhs.getClass();
/*  497 */     Class<?> rhsClass = rhs.getClass();
/*      */     
/*  499 */     if (lhsClass.isInstance(rhs)) {
/*  500 */       testClass = lhsClass;
/*  501 */       if (!rhsClass.isInstance(lhs))
/*      */       {
/*  503 */         testClass = rhsClass;
/*      */       }
/*  505 */     } else if (rhsClass.isInstance(lhs)) {
/*  506 */       testClass = rhsClass;
/*  507 */       if (!lhsClass.isInstance(rhs))
/*      */       {
/*  509 */         testClass = lhsClass;
/*      */       }
/*      */     } else {
/*      */       
/*  513 */       this.isEquals = false;
/*  514 */       return this;
/*      */     } 
/*      */     
/*      */     try {
/*  518 */       if (testClass.isArray()) {
/*  519 */         append(lhs, rhs);
/*      */       }
/*  521 */       else if (this.bypassReflectionClasses != null && (this.bypassReflectionClasses
/*  522 */         .contains(lhsClass) || this.bypassReflectionClasses.contains(rhsClass))) {
/*  523 */         this.isEquals = lhs.equals(rhs);
/*      */       } else {
/*  525 */         reflectionAppend(lhs, rhs, testClass);
/*  526 */         while (testClass.getSuperclass() != null && testClass != this.reflectUpToClass) {
/*  527 */           testClass = testClass.getSuperclass();
/*  528 */           reflectionAppend(lhs, rhs, testClass);
/*      */         } 
/*      */       } 
/*  531 */     } catch (IllegalArgumentException e) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  537 */       this.isEquals = false;
/*      */     } 
/*  539 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void reflectionAppend(Object lhs, Object rhs, Class<?> clazz) {
/*  555 */     if (isRegistered(lhs, rhs)) {
/*      */       return;
/*      */     }
/*      */     
/*      */     try {
/*  560 */       register(lhs, rhs);
/*  561 */       Field[] fields = clazz.getDeclaredFields();
/*  562 */       AccessibleObject.setAccessible((AccessibleObject[])fields, true);
/*  563 */       for (int i = 0; i < fields.length && this.isEquals; i++) {
/*  564 */         Field field = fields[i];
/*  565 */         if (!ArrayUtils.contains((Object[])this.excludeFields, field.getName()) && 
/*  566 */           !field.getName().contains("$") && (this.testTransients || 
/*  567 */           !Modifier.isTransient(field.getModifiers())) && 
/*  568 */           !Modifier.isStatic(field.getModifiers()) && 
/*  569 */           !field.isAnnotationPresent((Class)EqualsExclude.class)) {
/*  570 */           append(Reflection.getUnchecked(field, lhs), Reflection.getUnchecked(field, rhs));
/*      */         }
/*      */       } 
/*      */     } finally {
/*  574 */       unregister(lhs, rhs);
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
/*      */   public EqualsBuilder appendSuper(boolean superEquals) {
/*  586 */     if (!this.isEquals) {
/*  587 */       return this;
/*      */     }
/*  589 */     this.isEquals = superEquals;
/*  590 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EqualsBuilder append(Object lhs, Object rhs) {
/*  605 */     if (!this.isEquals) {
/*  606 */       return this;
/*      */     }
/*  608 */     if (lhs == rhs) {
/*  609 */       return this;
/*      */     }
/*  611 */     if (lhs == null || rhs == null) {
/*  612 */       setEquals(false);
/*  613 */       return this;
/*      */     } 
/*  615 */     Class<?> lhsClass = lhs.getClass();
/*  616 */     if (lhsClass.isArray()) {
/*      */ 
/*      */       
/*  619 */       appendArray(lhs, rhs);
/*      */     }
/*  621 */     else if (this.testRecursive && !ClassUtils.isPrimitiveOrWrapper(lhsClass)) {
/*  622 */       reflectionAppend(lhs, rhs);
/*      */     } else {
/*  624 */       this.isEquals = lhs.equals(rhs);
/*      */     } 
/*  626 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void appendArray(Object lhs, Object rhs) {
/*  639 */     if (lhs.getClass() != rhs.getClass()) {
/*  640 */       setEquals(false);
/*  641 */     } else if (lhs instanceof long[]) {
/*  642 */       append((long[])lhs, (long[])rhs);
/*  643 */     } else if (lhs instanceof int[]) {
/*  644 */       append((int[])lhs, (int[])rhs);
/*  645 */     } else if (lhs instanceof short[]) {
/*  646 */       append((short[])lhs, (short[])rhs);
/*  647 */     } else if (lhs instanceof char[]) {
/*  648 */       append((char[])lhs, (char[])rhs);
/*  649 */     } else if (lhs instanceof byte[]) {
/*  650 */       append((byte[])lhs, (byte[])rhs);
/*  651 */     } else if (lhs instanceof double[]) {
/*  652 */       append((double[])lhs, (double[])rhs);
/*  653 */     } else if (lhs instanceof float[]) {
/*  654 */       append((float[])lhs, (float[])rhs);
/*  655 */     } else if (lhs instanceof boolean[]) {
/*  656 */       append((boolean[])lhs, (boolean[])rhs);
/*      */     } else {
/*      */       
/*  659 */       append((Object[])lhs, (Object[])rhs);
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
/*      */   public EqualsBuilder append(long lhs, long rhs) {
/*  673 */     if (!this.isEquals) {
/*  674 */       return this;
/*      */     }
/*  676 */     this.isEquals = (lhs == rhs);
/*  677 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EqualsBuilder append(int lhs, int rhs) {
/*  688 */     if (!this.isEquals) {
/*  689 */       return this;
/*      */     }
/*  691 */     this.isEquals = (lhs == rhs);
/*  692 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EqualsBuilder append(short lhs, short rhs) {
/*  703 */     if (!this.isEquals) {
/*  704 */       return this;
/*      */     }
/*  706 */     this.isEquals = (lhs == rhs);
/*  707 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EqualsBuilder append(char lhs, char rhs) {
/*  718 */     if (!this.isEquals) {
/*  719 */       return this;
/*      */     }
/*  721 */     this.isEquals = (lhs == rhs);
/*  722 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EqualsBuilder append(byte lhs, byte rhs) {
/*  733 */     if (!this.isEquals) {
/*  734 */       return this;
/*      */     }
/*  736 */     this.isEquals = (lhs == rhs);
/*  737 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EqualsBuilder append(double lhs, double rhs) {
/*  754 */     if (!this.isEquals) {
/*  755 */       return this;
/*      */     }
/*  757 */     return append(Double.doubleToLongBits(lhs), Double.doubleToLongBits(rhs));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EqualsBuilder append(float lhs, float rhs) {
/*  774 */     if (!this.isEquals) {
/*  775 */       return this;
/*      */     }
/*  777 */     return append(Float.floatToIntBits(lhs), Float.floatToIntBits(rhs));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EqualsBuilder append(boolean lhs, boolean rhs) {
/*  788 */     if (!this.isEquals) {
/*  789 */       return this;
/*      */     }
/*  791 */     this.isEquals = (lhs == rhs);
/*  792 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EqualsBuilder append(Object[] lhs, Object[] rhs) {
/*  809 */     if (!this.isEquals) {
/*  810 */       return this;
/*      */     }
/*  812 */     if (lhs == rhs) {
/*  813 */       return this;
/*      */     }
/*  815 */     if (lhs == null || rhs == null) {
/*  816 */       setEquals(false);
/*  817 */       return this;
/*      */     } 
/*  819 */     if (lhs.length != rhs.length) {
/*  820 */       setEquals(false);
/*  821 */       return this;
/*      */     } 
/*  823 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/*  824 */       append(lhs[i], rhs[i]);
/*      */     }
/*  826 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EqualsBuilder append(long[] lhs, long[] rhs) {
/*  840 */     if (!this.isEquals) {
/*  841 */       return this;
/*      */     }
/*  843 */     if (lhs == rhs) {
/*  844 */       return this;
/*      */     }
/*  846 */     if (lhs == null || rhs == null) {
/*  847 */       setEquals(false);
/*  848 */       return this;
/*      */     } 
/*  850 */     if (lhs.length != rhs.length) {
/*  851 */       setEquals(false);
/*  852 */       return this;
/*      */     } 
/*  854 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/*  855 */       append(lhs[i], rhs[i]);
/*      */     }
/*  857 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EqualsBuilder append(int[] lhs, int[] rhs) {
/*  871 */     if (!this.isEquals) {
/*  872 */       return this;
/*      */     }
/*  874 */     if (lhs == rhs) {
/*  875 */       return this;
/*      */     }
/*  877 */     if (lhs == null || rhs == null) {
/*  878 */       setEquals(false);
/*  879 */       return this;
/*      */     } 
/*  881 */     if (lhs.length != rhs.length) {
/*  882 */       setEquals(false);
/*  883 */       return this;
/*      */     } 
/*  885 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/*  886 */       append(lhs[i], rhs[i]);
/*      */     }
/*  888 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EqualsBuilder append(short[] lhs, short[] rhs) {
/*  902 */     if (!this.isEquals) {
/*  903 */       return this;
/*      */     }
/*  905 */     if (lhs == rhs) {
/*  906 */       return this;
/*      */     }
/*  908 */     if (lhs == null || rhs == null) {
/*  909 */       setEquals(false);
/*  910 */       return this;
/*      */     } 
/*  912 */     if (lhs.length != rhs.length) {
/*  913 */       setEquals(false);
/*  914 */       return this;
/*      */     } 
/*  916 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/*  917 */       append(lhs[i], rhs[i]);
/*      */     }
/*  919 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EqualsBuilder append(char[] lhs, char[] rhs) {
/*  933 */     if (!this.isEquals) {
/*  934 */       return this;
/*      */     }
/*  936 */     if (lhs == rhs) {
/*  937 */       return this;
/*      */     }
/*  939 */     if (lhs == null || rhs == null) {
/*  940 */       setEquals(false);
/*  941 */       return this;
/*      */     } 
/*  943 */     if (lhs.length != rhs.length) {
/*  944 */       setEquals(false);
/*  945 */       return this;
/*      */     } 
/*  947 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/*  948 */       append(lhs[i], rhs[i]);
/*      */     }
/*  950 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EqualsBuilder append(byte[] lhs, byte[] rhs) {
/*  964 */     if (!this.isEquals) {
/*  965 */       return this;
/*      */     }
/*  967 */     if (lhs == rhs) {
/*  968 */       return this;
/*      */     }
/*  970 */     if (lhs == null || rhs == null) {
/*  971 */       setEquals(false);
/*  972 */       return this;
/*      */     } 
/*  974 */     if (lhs.length != rhs.length) {
/*  975 */       setEquals(false);
/*  976 */       return this;
/*      */     } 
/*  978 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/*  979 */       append(lhs[i], rhs[i]);
/*      */     }
/*  981 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EqualsBuilder append(double[] lhs, double[] rhs) {
/*  995 */     if (!this.isEquals) {
/*  996 */       return this;
/*      */     }
/*  998 */     if (lhs == rhs) {
/*  999 */       return this;
/*      */     }
/* 1001 */     if (lhs == null || rhs == null) {
/* 1002 */       setEquals(false);
/* 1003 */       return this;
/*      */     } 
/* 1005 */     if (lhs.length != rhs.length) {
/* 1006 */       setEquals(false);
/* 1007 */       return this;
/*      */     } 
/* 1009 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/* 1010 */       append(lhs[i], rhs[i]);
/*      */     }
/* 1012 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EqualsBuilder append(float[] lhs, float[] rhs) {
/* 1026 */     if (!this.isEquals) {
/* 1027 */       return this;
/*      */     }
/* 1029 */     if (lhs == rhs) {
/* 1030 */       return this;
/*      */     }
/* 1032 */     if (lhs == null || rhs == null) {
/* 1033 */       setEquals(false);
/* 1034 */       return this;
/*      */     } 
/* 1036 */     if (lhs.length != rhs.length) {
/* 1037 */       setEquals(false);
/* 1038 */       return this;
/*      */     } 
/* 1040 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/* 1041 */       append(lhs[i], rhs[i]);
/*      */     }
/* 1043 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EqualsBuilder append(boolean[] lhs, boolean[] rhs) {
/* 1057 */     if (!this.isEquals) {
/* 1058 */       return this;
/*      */     }
/* 1060 */     if (lhs == rhs) {
/* 1061 */       return this;
/*      */     }
/* 1063 */     if (lhs == null || rhs == null) {
/* 1064 */       setEquals(false);
/* 1065 */       return this;
/*      */     } 
/* 1067 */     if (lhs.length != rhs.length) {
/* 1068 */       setEquals(false);
/* 1069 */       return this;
/*      */     } 
/* 1071 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/* 1072 */       append(lhs[i], rhs[i]);
/*      */     }
/* 1074 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEquals() {
/* 1084 */     return this.isEquals;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Boolean build() {
/* 1098 */     return Boolean.valueOf(isEquals());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setEquals(boolean isEquals) {
/* 1108 */     this.isEquals = isEquals;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void reset() {
/* 1116 */     this.isEquals = true;
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\builder\EqualsBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */