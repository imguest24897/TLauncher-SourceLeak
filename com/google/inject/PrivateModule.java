/*     */ package com.google.inject;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.inject.binder.AnnotatedBindingBuilder;
/*     */ import com.google.inject.binder.AnnotatedConstantBindingBuilder;
/*     */ import com.google.inject.binder.AnnotatedElementBuilder;
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
/*     */ public abstract class PrivateModule
/*     */   implements Module
/*     */ {
/*     */   private PrivateBinder binder;
/*     */   
/*     */   public final synchronized void configure(Binder binder) {
/*  97 */     Preconditions.checkState((this.binder == null), "Re-entry is not allowed.");
/*     */ 
/*     */     
/* 100 */     this.binder = (PrivateBinder)binder.skipSources(new Class[] { PrivateModule.class });
/*     */     try {
/* 102 */       configure();
/*     */     } finally {
/* 104 */       this.binder = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void configure();
/*     */ 
/*     */ 
/*     */   
/*     */   protected final <T> void expose(Key<T> key) {
/* 116 */     binder().expose(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final AnnotatedElementBuilder expose(Class<?> type) {
/* 125 */     return binder().expose(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final AnnotatedElementBuilder expose(TypeLiteral<?> type) {
/* 134 */     return binder().expose(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final PrivateBinder binder() {
/* 141 */     Preconditions.checkState((this.binder != null), "The binder can only be used inside configure()");
/* 142 */     return this.binder;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void bindScope(Class<? extends Annotation> scopeAnnotation, Scope scope) {
/* 147 */     binder().bindScope(scopeAnnotation, scope);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final <T> LinkedBindingBuilder<T> bind(Key<T> key) {
/* 152 */     return binder().bind(key);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final <T> AnnotatedBindingBuilder<T> bind(TypeLiteral<T> typeLiteral) {
/* 157 */     return binder().bind(typeLiteral);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final <T> AnnotatedBindingBuilder<T> bind(Class<T> clazz) {
/* 162 */     return binder().bind(clazz);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final AnnotatedConstantBindingBuilder bindConstant() {
/* 167 */     return binder().bindConstant();
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void install(Module module) {
/* 172 */     binder().install(module);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void addError(String message, Object... arguments) {
/* 177 */     binder().addError(message, arguments);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void addError(Throwable t) {
/* 182 */     binder().addError(t);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void addError(Message message) {
/* 187 */     binder().addError(message);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void requestInjection(Object instance) {
/* 192 */     binder().requestInjection(instance);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void requestStaticInjection(Class<?>... types) {
/* 197 */     binder().requestStaticInjection(types);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void bindInterceptor(Matcher<? super Class<?>> classMatcher, Matcher<? super Method> methodMatcher, MethodInterceptor... interceptors) {
/* 208 */     binder().bindInterceptor(classMatcher, methodMatcher, interceptors);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void requireBinding(Key<?> key) {
/* 213 */     binder().getProvider(key);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void requireBinding(Class<?> type) {
/* 218 */     binder().getProvider(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final <T> Provider<T> getProvider(Key<T> key) {
/* 223 */     return binder().getProvider(key);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final <T> Provider<T> getProvider(Class<T> type) {
/* 228 */     return binder().getProvider(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void convertToTypes(Matcher<? super TypeLiteral<?>> typeMatcher, TypeConverter converter) {
/* 237 */     binder().convertToTypes(typeMatcher, converter);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final Stage currentStage() {
/* 242 */     return binder().currentStage();
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> MembersInjector<T> getMembersInjector(Class<T> type) {
/* 247 */     return binder().getMembersInjector(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> type) {
/* 252 */     return binder().getMembersInjector(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void bindListener(Matcher<? super TypeLiteral<?>> typeMatcher, TypeListener listener) {
/* 259 */     binder().bindListener(typeMatcher, listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void bindListener(Matcher<? super Binding<?>> bindingMatcher, ProvisionListener... listeners) {
/* 268 */     binder().bindListener(bindingMatcher, listeners);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\PrivateModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */