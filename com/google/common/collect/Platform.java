/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Strings;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ final class Platform
/*     */ {
/*  33 */   private static final Logger logger = Logger.getLogger(Platform.class.getName());
/*     */ 
/*     */   
/*     */   static <K, V> Map<K, V> newHashMapWithExpectedSize(int expectedSize) {
/*  37 */     return Maps.newHashMapWithExpectedSize(expectedSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> Map<K, V> newLinkedHashMapWithExpectedSize(int expectedSize) {
/*  45 */     return Maps.newLinkedHashMapWithExpectedSize(expectedSize);
/*     */   }
/*     */ 
/*     */   
/*     */   static <E> Set<E> newHashSetWithExpectedSize(int expectedSize) {
/*  50 */     return Sets.newHashSetWithExpectedSize(expectedSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> Set<E> newLinkedHashSetWithExpectedSize(int expectedSize) {
/*  58 */     return Sets.newLinkedHashSetWithExpectedSize(expectedSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> Map<K, V> preservesInsertionOrderOnPutsMap() {
/*  66 */     return Maps.newLinkedHashMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> Set<E> preservesInsertionOrderOnAddsSet() {
/*  74 */     return Sets.newLinkedHashSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> T[] newArray(T[] reference, int length) {
/*  84 */     Class<?> type = reference.getClass().getComponentType();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     T[] result = (T[])Array.newInstance(type, length);
/*  90 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   static <T> T[] copy(Object[] source, int from, int to, T[] arrayOfType) {
/*  95 */     return Arrays.copyOfRange(source, from, to, (Class)arrayOfType.getClass());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static MapMaker tryWeakKeys(MapMaker mapMaker) {
/* 104 */     return mapMaker.weakKeys();
/*     */   }
/*     */   
/*     */   static int reduceIterationsIfGwt(int iterations) {
/* 108 */     return iterations;
/*     */   }
/*     */   
/*     */   static int reduceExponentIfGwt(int exponent) {
/* 112 */     return exponent;
/*     */   }
/*     */   
/*     */   static void checkGwtRpcEnabled() {
/* 116 */     String propertyName = "guava.gwt.emergency_reenable_rpc";
/*     */     
/* 118 */     if (!Boolean.parseBoolean(System.getProperty(propertyName, "false"))) {
/* 119 */       throw new UnsupportedOperationException(
/* 120 */           Strings.lenientFormat("We are removing GWT-RPC support for Guava types. You can temporarily reenable support by setting the system property %s to true. For more about system properties, see %s. For more about Guava's GWT-RPC support, see %s.", new Object[] { propertyName, "https://stackoverflow.com/q/5189914/28465", "https://groups.google.com/d/msg/guava-announce/zHZTFg7YF3o/rQNnwdHeEwAJ" }));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 128 */     logger.log(Level.WARNING, "Later in 2020, we will remove GWT-RPC support for Guava types. You are seeing this warning because you are sending a Guava type over GWT-RPC, which will break. You can identify which type by looking at the class name in the attached stack trace.", new Throwable());
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\Platform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */