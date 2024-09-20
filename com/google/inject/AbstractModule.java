/*     */ package com.google.inject;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.inject.binder.AnnotatedBindingBuilder;
/*     */ import com.google.inject.binder.AnnotatedConstantBindingBuilder;
/*     */ import com.google.inject.binder.LinkedBindingBuilder;
/*     */ import com.google.inject.matcher.Matcher;
/*     */ import com.google.inject.spi.Message;
/*     */ import com.google.inject.spi.ProvisionListener;
/*     */ import com.google.inject.spi.TypeConverter;
/*     */ import com.google.inject.spi.TypeListener;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
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
/*     */ public abstract class AbstractModule
/*     */   implements Module
/*     */ {
/*     */   Binder binder;
/*     */   
/*     */   public final synchronized void configure(Binder builder) {
/*  60 */     Preconditions.checkState((this.binder == null), "Re-entry is not allowed.");
/*     */     
/*  62 */     this.binder = (Binder)Preconditions.checkNotNull(builder, "builder");
/*     */     try {
/*  64 */       configure();
/*     */     } finally {
/*  66 */       this.binder = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void configure() {}
/*     */ 
/*     */   
/*     */   protected Binder binder() {
/*  75 */     Preconditions.checkState((this.binder != null), "The binder can only be used inside configure()");
/*  76 */     return this.binder;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void bindScope(Class<? extends Annotation> scopeAnnotation, Scope scope) {
/*  81 */     binder().bindScope(scopeAnnotation, scope);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> LinkedBindingBuilder<T> bind(Key<T> key) {
/*  86 */     return binder().bind(key);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> AnnotatedBindingBuilder<T> bind(TypeLiteral<T> typeLiteral) {
/*  91 */     return binder().bind(typeLiteral);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> AnnotatedBindingBuilder<T> bind(Class<T> clazz) {
/*  96 */     return binder().bind(clazz);
/*     */   }
/*     */ 
/*     */   
/*     */   protected AnnotatedConstantBindingBuilder bindConstant() {
/* 101 */     return binder().bindConstant();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void install(Module module) {
/* 106 */     binder().install(module);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addError(String message, Object... arguments) {
/* 111 */     binder().addError(message, arguments);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addError(Throwable t) {
/* 116 */     binder().addError(t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addError(Message message) {
/* 124 */     binder().addError(message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void requestInjection(Object instance) {
/* 132 */     binder().requestInjection(instance);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void requestStaticInjection(Class<?>... types) {
/* 137 */     binder().requestStaticInjection(types);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void bindInterceptor(Matcher<? super Class<?>> classMatcher, Matcher<? super Method> methodMatcher, MethodInterceptor... interceptors) {
/* 147 */     binder().bindInterceptor(classMatcher, methodMatcher, interceptors);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void requireBinding(Key<?> key) {
/* 158 */     binder().getProvider(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void requireBinding(Class<?> type) {
/* 169 */     binder().getProvider(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> Provider<T> getProvider(Key<T> key) {
/* 177 */     return binder().getProvider(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> Provider<T> getProvider(Class<T> type) {
/* 185 */     return binder().getProvider(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void convertToTypes(Matcher<? super TypeLiteral<?>> typeMatcher, TypeConverter converter) {
/* 194 */     binder().convertToTypes(typeMatcher, converter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Stage currentStage() {
/* 202 */     return binder().currentStage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> MembersInjector<T> getMembersInjector(Class<T> type) {
/* 210 */     return binder().getMembersInjector(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> type) {
/* 218 */     return binder().getMembersInjector(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void bindListener(Matcher<? super TypeLiteral<?>> typeMatcher, TypeListener listener) {
/* 226 */     binder().bindListener(typeMatcher, listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void bindListener(Matcher<? super Binding<?>> bindingMatcher, ProvisionListener... listener) {
/* 235 */     binder().bindListener(bindingMatcher, listener);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\AbstractModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */