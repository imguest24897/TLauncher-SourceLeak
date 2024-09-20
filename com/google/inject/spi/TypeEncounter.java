package com.google.inject.spi;

import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;
import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInterceptor;

public interface TypeEncounter<I> {
  void addError(String paramString, Object... paramVarArgs);
  
  void addError(Throwable paramThrowable);
  
  void addError(Message paramMessage);
  
  <T> Provider<T> getProvider(Key<T> paramKey);
  
  <T> Provider<T> getProvider(Class<T> paramClass);
  
  <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> paramTypeLiteral);
  
  <T> MembersInjector<T> getMembersInjector(Class<T> paramClass);
  
  void register(MembersInjector<? super I> paramMembersInjector);
  
  void register(InjectionListener<? super I> paramInjectionListener);
  
  void bindInterceptor(Matcher<? super Method> paramMatcher, MethodInterceptor... paramVarArgs);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\TypeEncounter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */