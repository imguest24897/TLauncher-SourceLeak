/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.collect.ArrayListMultimap;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.inject.spi.InjectionPoint;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.BitSet;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
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
/*     */ final class ProxyFactory<T>
/*     */   implements ConstructionProxyFactory<T>
/*     */ {
/*  45 */   private static final Logger logger = Logger.getLogger(ProxyFactory.class.getName());
/*     */   
/*     */   private final InjectionPoint injectionPoint;
/*     */   
/*     */   private final Function<String, BiFunction<Object, Object[], Object>> enhancer;
/*     */   
/*     */   private final ImmutableMap<Method, List<MethodInterceptor>> interceptors;
/*     */   private final InvocationHandler[] callbacks;
/*     */   
/*     */   ProxyFactory(InjectionPoint injectionPoint, Iterable<MethodAspect> methodAspects) throws ErrorsException {
/*  55 */     this.injectionPoint = injectionPoint;
/*     */     
/*  57 */     Class<?> hostClass = injectionPoint.getMember().getDeclaringClass();
/*     */ 
/*     */     
/*  60 */     List<MethodAspect> applicableAspects = Lists.newArrayList();
/*  61 */     for (MethodAspect methodAspect : methodAspects) {
/*  62 */       if (methodAspect.matches(hostClass)) {
/*  63 */         applicableAspects.add(methodAspect);
/*     */       }
/*     */     } 
/*     */     
/*  67 */     if (applicableAspects.isEmpty()) {
/*  68 */       this.enhancer = null;
/*  69 */       this.interceptors = ImmutableMap.of();
/*  70 */       this.callbacks = null;
/*     */       
/*     */       return;
/*     */     } 
/*  74 */     BytecodeGen.EnhancerBuilder enhancerBuilder = BytecodeGen.enhancerBuilder(hostClass);
/*     */     
/*  76 */     Method[] methods = enhancerBuilder.getEnhanceableMethods();
/*  77 */     int numMethods = methods.length;
/*     */     
/*  79 */     ArrayListMultimap arrayListMultimap = ArrayListMultimap.create();
/*  80 */     BitSet matchedMethodIndices = new BitSet();
/*     */ 
/*     */     
/*  83 */     for (MethodAspect methodAspect : applicableAspects) {
/*  84 */       for (int i = 0; i < numMethods; i++) {
/*  85 */         Method method = methods[i];
/*  86 */         if (methodAspect.matches(method)) {
/*  87 */           if (method.isSynthetic()) {
/*  88 */             logger.log(Level.WARNING, "Method [{0}] is synthetic and is being intercepted by {1}. This could indicate a bug.  The method may be intercepted twice, or may not be intercepted at all.", new Object[] { method, methodAspect
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/*  93 */                   .interceptors() });
/*     */           }
/*     */           
/*  96 */           arrayListMultimap.putAll(method, methodAspect.interceptors());
/*  97 */           matchedMethodIndices.set(i);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 102 */     if (matchedMethodIndices.isEmpty()) {
/* 103 */       this.enhancer = null;
/* 104 */       this.interceptors = ImmutableMap.of();
/* 105 */       this.callbacks = null;
/*     */       
/*     */       return;
/*     */     } 
/*     */     try {
/* 110 */       this.enhancer = enhancerBuilder.buildEnhancer(matchedMethodIndices);
/* 111 */     } catch (Throwable e) {
/* 112 */       throw (new Errors()).errorEnhancingClass(hostClass, e).toException();
/*     */     } 
/*     */     
/* 115 */     this.callbacks = new InvocationHandler[matchedMethodIndices.cardinality()];
/*     */ 
/*     */     
/* 118 */     ImmutableMap.Builder<Method, List<MethodInterceptor>> interceptorsMapBuilder = ImmutableMap.builder();
/*     */     
/* 120 */     int callbackIndex = 0;
/* 121 */     int methodIndex = matchedMethodIndices.nextSetBit(0);
/* 122 */     for (; methodIndex >= 0; 
/* 123 */       methodIndex = matchedMethodIndices.nextSetBit(methodIndex + 1)) {
/*     */       
/* 125 */       Method method = methods[methodIndex];
/*     */       
/* 127 */       ImmutableList immutableList = ImmutableSet.copyOf(arrayListMultimap.get(method)).asList();
/* 128 */       interceptorsMapBuilder.put(method, immutableList);
/*     */       
/* 130 */       BiFunction<Object, Object[], Object> superInvoker = BytecodeGen.superMethod(this.enhancer, method);
/* 131 */       this.callbacks[callbackIndex++] = new InterceptorStackCallback(method, (List<MethodInterceptor>)immutableList, superInvoker);
/*     */     } 
/*     */     
/* 134 */     this.interceptors = interceptorsMapBuilder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableMap<Method, List<MethodInterceptor>> getInterceptors() {
/* 139 */     return this.interceptors;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConstructionProxy<T> create() throws ErrorsException {
/* 144 */     if (this.interceptors.isEmpty()) {
/* 145 */       return (new DefaultConstructionProxyFactory<>(this.injectionPoint)).create();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 151 */       return new ProxyConstructor<>(this.injectionPoint, this.enhancer, this.interceptors, this.callbacks);
/* 152 */     } catch (Throwable e) {
/* 153 */       throw (new Errors())
/* 154 */         .errorEnhancingClass(this.injectionPoint.getMember().getDeclaringClass(), e)
/* 155 */         .toException();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ProxyConstructor<T>
/*     */     implements ConstructionProxy<T>
/*     */   {
/*     */     final InjectionPoint injectionPoint;
/*     */     
/*     */     final Constructor<T> constructor;
/*     */     
/*     */     final BiFunction<Object, Object[], Object> enhancedConstructor;
/*     */     
/*     */     final ImmutableMap<Method, List<MethodInterceptor>> interceptors;
/*     */     final InvocationHandler[] callbacks;
/*     */     
/*     */     ProxyConstructor(InjectionPoint injectionPoint, Function<String, BiFunction<Object, Object[], Object>> enhancer, ImmutableMap<Method, List<MethodInterceptor>> interceptors, InvocationHandler[] callbacks) {
/* 173 */       this.injectionPoint = injectionPoint;
/* 174 */       this.constructor = (Constructor<T>)injectionPoint.getMember();
/* 175 */       this.enhancedConstructor = BytecodeGen.enhancedConstructor(enhancer, this.constructor);
/* 176 */       this.interceptors = interceptors;
/* 177 */       this.callbacks = callbacks;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public T newInstance(Object... arguments) throws InvocationTargetException {
/* 183 */       return (T)this.enhancedConstructor.apply(this.callbacks, arguments);
/*     */     }
/*     */ 
/*     */     
/*     */     public InjectionPoint getInjectionPoint() {
/* 188 */       return this.injectionPoint;
/*     */     }
/*     */ 
/*     */     
/*     */     public Constructor<T> getConstructor() {
/* 193 */       return this.constructor;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableMap<Method, List<MethodInterceptor>> getMethodInterceptors() {
/* 198 */       return this.interceptors;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ProxyFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */