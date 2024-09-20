/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.inject.matcher.Matcher;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import org.aopalliance.intercept.MethodInterceptor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class MethodAspect
/*    */ {
/*    */   private final Matcher<? super Class<?>> classMatcher;
/*    */   private final Matcher<? super Method> methodMatcher;
/*    */   private final List<MethodInterceptor> interceptors;
/*    */   
/*    */   MethodAspect(Matcher<? super Class<?>> classMatcher, Matcher<? super Method> methodMatcher, List<MethodInterceptor> interceptors) {
/* 49 */     this.classMatcher = (Matcher<? super Class<?>>)Preconditions.checkNotNull(classMatcher, "class matcher");
/* 50 */     this.methodMatcher = (Matcher<? super Method>)Preconditions.checkNotNull(methodMatcher, "method matcher");
/* 51 */     this.interceptors = (List<MethodInterceptor>)Preconditions.checkNotNull(interceptors, "interceptors");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   MethodAspect(Matcher<? super Class<?>> classMatcher, Matcher<? super Method> methodMatcher, MethodInterceptor... interceptors) {
/* 58 */     this(classMatcher, methodMatcher, Arrays.asList(interceptors));
/*    */   }
/*    */   
/*    */   boolean matches(Class<?> clazz) {
/* 62 */     return this.classMatcher.matches(clazz);
/*    */   }
/*    */   
/*    */   boolean matches(Method method) {
/* 66 */     return this.methodMatcher.matches(method);
/*    */   }
/*    */   
/*    */   List<MethodInterceptor> interceptors() {
/* 70 */     return this.interceptors;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\MethodAspect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */