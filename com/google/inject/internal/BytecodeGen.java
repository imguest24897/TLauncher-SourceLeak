/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.google.common.collect.MapMaker;
/*     */ import com.google.inject.internal.aop.ClassBuilding;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Executable;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.BitSet;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BytecodeGen
/*     */ {
/*  54 */   private static final Map<Class<?>, Boolean> circularProxyTypeCache = (new MapMaker())
/*  55 */     .weakKeys().makeMap();
/*     */   public static final String ENHANCER_BY_GUICE_MARKER = "$$EnhancerByGuice$$";
/*     */   
/*     */   public static boolean isCircularProxy(Object object) {
/*  59 */     return (object != null && circularProxyTypeCache.containsKey(object.getClass()));
/*     */   }
/*     */   public static final String FASTCLASS_BY_GUICE_MARKER = "$$FastClassByGuice$$";
/*     */   
/*     */   static <T> T newCircularProxy(Class<T> type, InvocationHandler handler) {
/*  64 */     Object proxy = Proxy.newProxyInstance(type.getClassLoader(), new Class[] { type }, handler);
/*  65 */     circularProxyTypeCache.put(proxy.getClass(), Boolean.TRUE);
/*  66 */     return type.cast(proxy);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static EnhancerBuilder enhancerBuilder(Class<?> hostClass) {
/* 104 */     return (EnhancerBuilder)ENHANCER_BUILDERS.getUnchecked(hostClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static BiFunction<Object, Object[], Object> enhancedConstructor(Function<String, BiFunction<Object, Object[], Object>> enhancer, Constructor<?> constructor) {
/* 113 */     Preconditions.checkArgument(ClassBuilding.canEnhance(constructor), "Constructor is not visible");
/* 114 */     return enhancer.apply(ClassBuilding.signature(constructor));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static BiFunction<Object, Object[], Object> superMethod(Function<String, BiFunction<Object, Object[], Object>> enhancer, Method method) {
/* 124 */     return enhancer.apply(ClassBuilding.signature(method));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static BiFunction<Object, Object[], Object> fastConstructor(Constructor<?> constructor) {
/* 134 */     if (ClassBuilding.canFastInvoke(constructor)) {
/* 135 */       return fastClass(constructor).apply(ClassBuilding.signature(constructor));
/*     */     }
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static BiFunction<Object, Object[], Object> fastMethod(Method method) {
/* 147 */     if (ClassBuilding.canFastInvoke(method)) {
/* 148 */       return fastClass(method).apply(ClassBuilding.signature(method));
/*     */     }
/* 150 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Function<String, BiFunction<Object, Object[], Object>> fastClass(Executable member) {
/* 157 */     return FAST_CLASSES.get(member.getDeclaringClass());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 166 */   private static final LoadingCache<Class<?>, EnhancerBuilder> ENHANCER_BUILDERS = CacheBuilder.newBuilder()
/* 167 */     .weakKeys()
/* 168 */     .weakValues()
/* 169 */     .build(CacheLoader.from(ClassBuilding::buildEnhancerBuilder));
/*     */ 
/*     */   
/* 172 */   private static final ClassValue<Function<String, BiFunction<Object, Object[], Object>>> FAST_CLASSES = new ClassValue<Function<String, BiFunction<Object, Object[], Object>>>()
/*     */     {
/*     */       protected Function<String, BiFunction<Object, Object[], Object>> computeValue(Class<?> hostClass)
/*     */       {
/* 176 */         return ClassBuilding.buildFastClass(hostClass);
/*     */       }
/*     */     };
/*     */   
/*     */   public static interface EnhancerBuilder {
/*     */     Method[] getEnhanceableMethods();
/*     */     
/*     */     Function<String, BiFunction<Object, Object[], Object>> buildEnhancer(BitSet param1BitSet);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\BytecodeGen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */