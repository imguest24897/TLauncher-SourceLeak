/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.inject.spi.InjectionPoint;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
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
/*     */ final class DefaultConstructionProxyFactory<T>
/*     */   implements ConstructionProxyFactory<T>
/*     */ {
/*     */   private final InjectionPoint injectionPoint;
/*     */   
/*     */   DefaultConstructionProxyFactory(InjectionPoint injectionPoint) {
/*  40 */     this.injectionPoint = injectionPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstructionProxy<T> create() {
/*  46 */     Constructor<T> constructor = (Constructor<T>)this.injectionPoint.getMember();
/*     */     
/*  48 */     if (InternalFlags.isBytecodeGenEnabled()) {
/*     */       
/*     */       try {
/*  51 */         BiFunction<Object, Object[], Object> fastConstructor = BytecodeGen.fastConstructor(constructor);
/*  52 */         if (fastConstructor != null) {
/*  53 */           return new FastClassProxy<>(this.injectionPoint, constructor, fastConstructor);
/*     */         }
/*  55 */       } catch (Exception|LinkageError exception) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  60 */     return new ReflectiveProxy<>(this.injectionPoint, constructor);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class FastClassProxy<T>
/*     */     implements ConstructionProxy<T>
/*     */   {
/*     */     final InjectionPoint injectionPoint;
/*     */     
/*     */     final Constructor<T> constructor;
/*     */     final BiFunction<Object, Object[], Object> fastConstructor;
/*     */     
/*     */     FastClassProxy(InjectionPoint injectionPoint, Constructor<T> constructor, BiFunction<Object, Object[], Object> fastConstructor) {
/*  73 */       this.injectionPoint = injectionPoint;
/*  74 */       this.constructor = constructor;
/*  75 */       this.fastConstructor = fastConstructor;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public T newInstance(Object... arguments) throws InvocationTargetException {
/*     */       try {
/*  82 */         return (T)this.fastConstructor.apply(null, arguments);
/*  83 */       } catch (Throwable e) {
/*  84 */         throw new InvocationTargetException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public InjectionPoint getInjectionPoint() {
/*  90 */       return this.injectionPoint;
/*     */     }
/*     */ 
/*     */     
/*     */     public Constructor<T> getConstructor() {
/*  95 */       return this.constructor;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableMap<Method, List<MethodInterceptor>> getMethodInterceptors() {
/* 100 */       return ImmutableMap.of();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ReflectiveProxy<T> implements ConstructionProxy<T> {
/*     */     final Constructor<T> constructor;
/*     */     final InjectionPoint injectionPoint;
/*     */     
/*     */     ReflectiveProxy(InjectionPoint injectionPoint, Constructor<T> constructor) {
/* 109 */       if (!Modifier.isPublic(constructor.getDeclaringClass().getModifiers()) || 
/* 110 */         !Modifier.isPublic(constructor.getModifiers())) {
/* 111 */         constructor.setAccessible(true);
/*     */       }
/* 113 */       this.injectionPoint = injectionPoint;
/* 114 */       this.constructor = constructor;
/*     */     }
/*     */ 
/*     */     
/*     */     public T newInstance(Object... arguments) throws InvocationTargetException {
/*     */       try {
/* 120 */         return this.constructor.newInstance(arguments);
/* 121 */       } catch (InstantiationException e) {
/* 122 */         throw new AssertionError(e);
/* 123 */       } catch (IllegalAccessException e) {
/* 124 */         throw new AssertionError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public InjectionPoint getInjectionPoint() {
/* 130 */       return this.injectionPoint;
/*     */     }
/*     */ 
/*     */     
/*     */     public Constructor<T> getConstructor() {
/* 135 */       return this.constructor;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableMap<Method, List<MethodInterceptor>> getMethodInterceptors() {
/* 140 */       return ImmutableMap.of();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\DefaultConstructionProxyFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */