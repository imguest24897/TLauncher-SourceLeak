package com.google.inject;

import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.AnnotatedConstantBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.Message;
import com.google.inject.spi.ModuleAnnotatedMethodScanner;
import com.google.inject.spi.ProvisionListener;
import com.google.inject.spi.TypeConverter;
import com.google.inject.spi.TypeListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInterceptor;

public interface Binder {
  void bindInterceptor(Matcher<? super Class<?>> paramMatcher, Matcher<? super Method> paramMatcher1, MethodInterceptor... paramVarArgs);
  
  void bindScope(Class<? extends Annotation> paramClass, Scope paramScope);
  
  <T> LinkedBindingBuilder<T> bind(Key<T> paramKey);
  
  <T> AnnotatedBindingBuilder<T> bind(TypeLiteral<T> paramTypeLiteral);
  
  <T> AnnotatedBindingBuilder<T> bind(Class<T> paramClass);
  
  AnnotatedConstantBindingBuilder bindConstant();
  
  <T> void requestInjection(TypeLiteral<T> paramTypeLiteral, T paramT);
  
  void requestInjection(Object paramObject);
  
  void requestStaticInjection(Class<?>... paramVarArgs);
  
  void install(Module paramModule);
  
  Stage currentStage();
  
  void addError(String paramString, Object... paramVarArgs);
  
  void addError(Throwable paramThrowable);
  
  void addError(Message paramMessage);
  
  <T> Provider<T> getProvider(Key<T> paramKey);
  
  <T> Provider<T> getProvider(Dependency<T> paramDependency);
  
  <T> Provider<T> getProvider(Class<T> paramClass);
  
  <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> paramTypeLiteral);
  
  <T> MembersInjector<T> getMembersInjector(Class<T> paramClass);
  
  void convertToTypes(Matcher<? super TypeLiteral<?>> paramMatcher, TypeConverter paramTypeConverter);
  
  void bindListener(Matcher<? super TypeLiteral<?>> paramMatcher, TypeListener paramTypeListener);
  
  void bindListener(Matcher<? super Binding<?>> paramMatcher, ProvisionListener... paramVarArgs);
  
  Binder withSource(Object paramObject);
  
  Binder skipSources(Class<?>... paramVarArgs);
  
  PrivateBinder newPrivateBinder();
  
  void requireExplicitBindings();
  
  void disableCircularProxies();
  
  void requireAtInjectOnConstructors();
  
  void requireExactBindingAnnotations();
  
  void scanModulesForAnnotatedMethods(ModuleAnnotatedMethodScanner paramModuleAnnotatedMethodScanner);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\Binder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */