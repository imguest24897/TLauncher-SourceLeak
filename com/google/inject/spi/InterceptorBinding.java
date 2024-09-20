/*    */ package com.google.inject.spi;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.inject.Binder;
/*    */ import com.google.inject.matcher.Matcher;
/*    */ import java.lang.reflect.Method;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class InterceptorBinding
/*    */   implements Element
/*    */ {
/*    */   private final Object source;
/*    */   private final Matcher<? super Class<?>> classMatcher;
/*    */   private final Matcher<? super Method> methodMatcher;
/*    */   private final ImmutableList<MethodInterceptor> interceptors;
/*    */   
/*    */   InterceptorBinding(Object source, Matcher<? super Class<?>> classMatcher, Matcher<? super Method> methodMatcher, MethodInterceptor[] interceptors) {
/* 55 */     this.source = Preconditions.checkNotNull(source, "source");
/* 56 */     this.classMatcher = (Matcher<? super Class<?>>)Preconditions.checkNotNull(classMatcher, "classMatcher");
/* 57 */     this.methodMatcher = (Matcher<? super Method>)Preconditions.checkNotNull(methodMatcher, "methodMatcher");
/* 58 */     this.interceptors = ImmutableList.copyOf((Object[])interceptors);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getSource() {
/* 63 */     return this.source;
/*    */   }
/*    */   
/*    */   public Matcher<? super Class<?>> getClassMatcher() {
/* 67 */     return this.classMatcher;
/*    */   }
/*    */   
/*    */   public Matcher<? super Method> getMethodMatcher() {
/* 71 */     return this.methodMatcher;
/*    */   }
/*    */   
/*    */   public List<MethodInterceptor> getInterceptors() {
/* 75 */     return (List<MethodInterceptor>)this.interceptors;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T acceptVisitor(ElementVisitor<T> visitor) {
/* 80 */     return visitor.visit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void applyTo(Binder binder) {
/* 85 */     binder
/* 86 */       .withSource(getSource())
/* 87 */       .bindInterceptor(this.classMatcher, this.methodMatcher, (MethodInterceptor[])this.interceptors
/*    */ 
/*    */         
/* 90 */         .toArray((Object[])new MethodInterceptor[this.interceptors.size()]));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\InterceptorBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */