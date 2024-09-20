/*     */ package com.google.inject.internal.aop;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.inject.internal.BytecodeGen;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Executable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.TreeMap;
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
/*     */ final class EnhancerBuilderImpl
/*     */   implements BytecodeGen.EnhancerBuilder
/*     */ {
/*  47 */   private static final ClassValue<Map<BitSet, Function<String, BiFunction<Object, Object[], Object>>>> ENHANCERS = new ClassValue<Map<BitSet, Function<String, BiFunction<Object, Object[], Object>>>>()
/*     */     {
/*     */       
/*     */       protected Map<BitSet, Function<String, BiFunction<Object, Object[], Object>>> computeValue(Class<?> hostClass)
/*     */       {
/*  52 */         return new HashMap<>();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   private final Class<?> hostClass;
/*     */ 
/*     */   
/*     */   private final Method[] enhanceableMethods;
/*     */   
/*     */   private final Map<Method, Method> bridgeDelegates;
/*     */ 
/*     */   
/*     */   EnhancerBuilderImpl(Class<?> hostClass, Collection<Method> enhanceableMethods, Map<Method, Method> bridgeDelegates) {
/*  67 */     this.hostClass = hostClass;
/*  68 */     this.enhanceableMethods = enhanceableMethods.<Method>toArray(new Method[0]);
/*  69 */     this.bridgeDelegates = (Map<Method, Method>)ImmutableMap.copyOf(bridgeDelegates);
/*     */   }
/*     */ 
/*     */   
/*     */   public Method[] getEnhanceableMethods() {
/*  74 */     return this.enhanceableMethods;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Function<String, BiFunction<Object, Object[], Object>> buildEnhancer(BitSet methodIndices) {
/*  80 */     if ((this.hostClass.getModifiers() & 0x10) != 0) {
/*  81 */       String str = String.valueOf(this.hostClass); throw new IllegalArgumentException((new StringBuilder(22 + String.valueOf(str).length())).append("Cannot subclass final ").append(str).toString());
/*     */     } 
/*     */ 
/*     */     
/*  85 */     Map<BitSet, Function<String, BiFunction<Object, Object[], Object>>> enhancers = ENHANCERS.get(this.hostClass);
/*  86 */     synchronized (enhancers) {
/*  87 */       return enhancers.computeIfAbsent(methodIndices, this::doBuildEnhancer);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Function<String, BiFunction<Object, Object[], Object>> doBuildEnhancer(BitSet methodIndices) {
/*  93 */     NavigableMap<String, Executable> glueMap = new TreeMap<>();
/*     */     
/*  95 */     ClassBuilding.visitMembers(this.hostClass
/*  96 */         .getDeclaredConstructors(), 
/*  97 */         ClassDefining.hasPackageAccess(), ctor -> glueMap.put(ClassBuilding.signature(ctor), ctor));
/*     */ 
/*     */     
/* 100 */     int methodIndex = methodIndices.nextSetBit(0);
/* 101 */     for (; methodIndex >= 0; 
/* 102 */       methodIndex = methodIndices.nextSetBit(methodIndex + 1)) {
/* 103 */       Method method = this.enhanceableMethods[methodIndex];
/* 104 */       glueMap.put(ClassBuilding.signature(method), method);
/*     */     } 
/*     */     
/* 107 */     return (new Enhancer(this.hostClass, this.bridgeDelegates)).glue(glueMap);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\aop\EnhancerBuilderImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */