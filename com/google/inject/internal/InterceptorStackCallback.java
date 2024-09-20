/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
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
/*     */ final class InterceptorStackCallback
/*     */   implements InvocationHandler
/*     */ {
/*     */   private static final String GUICE_INTERNAL_AOP_PACKAGE = "com.google.inject.internal.aop";
/*     */   final Method method;
/*     */   final MethodInterceptor[] interceptors;
/*     */   final BiFunction<Object, Object[], Object> superInvoker;
/*     */   
/*     */   public InterceptorStackCallback(Method method, List<MethodInterceptor> interceptors, BiFunction<Object, Object[], Object> superInvoker) {
/*  48 */     this.method = method;
/*  49 */     this.interceptors = interceptors.<MethodInterceptor>toArray(new MethodInterceptor[interceptors.size()]);
/*  50 */     this.superInvoker = superInvoker;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object invoke(Object proxy, Method unused, Object[] arguments) throws Throwable {
/*  55 */     return (new InterceptedMethodInvocation(proxy, arguments, 0)).proceed();
/*     */   }
/*     */   
/*     */   private class InterceptedMethodInvocation
/*     */     implements MethodInvocation {
/*     */     final Object proxy;
/*     */     final Object[] arguments;
/*     */     final int interceptorIndex;
/*     */     
/*     */     public InterceptedMethodInvocation(Object proxy, Object[] arguments, int interceptorIndex) {
/*  65 */       this.proxy = proxy;
/*  66 */       this.arguments = arguments;
/*  67 */       this.interceptorIndex = interceptorIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object proceed() throws Throwable {
/*     */       try {
/*  73 */         return (this.interceptorIndex == InterceptorStackCallback.this.interceptors.length) ? 
/*  74 */           InterceptorStackCallback.this.superInvoker.apply(this.proxy, this.arguments) : 
/*  75 */           InterceptorStackCallback.this.interceptors[this.interceptorIndex].invoke(new InterceptedMethodInvocation(this.proxy, this.arguments, this.interceptorIndex + 1));
/*     */       }
/*  77 */       catch (Throwable t) {
/*  78 */         InterceptorStackCallback.this.pruneStacktrace(t);
/*  79 */         throw t;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Method getMethod() {
/*  85 */       return InterceptorStackCallback.this.method;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] getArguments() {
/*  90 */       return this.arguments;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getThis() {
/*  95 */       return this.proxy;
/*     */     }
/*     */ 
/*     */     
/*     */     public AccessibleObject getStaticPart() {
/* 100 */       return getMethod();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void pruneStacktrace(Throwable throwable) {
/* 109 */     for (Throwable t = throwable; t != null; t = t.getCause()) {
/* 110 */       StackTraceElement[] stackTrace = t.getStackTrace();
/* 111 */       List<StackTraceElement> pruned = Lists.newArrayList();
/* 112 */       for (StackTraceElement element : stackTrace) {
/* 113 */         String className = element.getClassName();
/* 114 */         if (!className.startsWith(InterceptorStackCallback.class.getName()) && 
/* 115 */           !className.startsWith("com.google.inject.internal.aop") && 
/* 116 */           !className.contains("$$EnhancerByGuice$$")) {
/* 117 */           pruned.add(element);
/*     */         }
/*     */       } 
/* 120 */       t.setStackTrace(pruned.<StackTraceElement>toArray(new StackTraceElement[pruned.size()]));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\InterceptorStackCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */