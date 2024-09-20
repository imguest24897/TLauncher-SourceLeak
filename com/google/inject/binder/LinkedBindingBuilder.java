package com.google.inject.binder;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import java.lang.reflect.Constructor;
import javax.inject.Provider;

public interface LinkedBindingBuilder<T> extends ScopedBindingBuilder {
  ScopedBindingBuilder to(Class<? extends T> paramClass);
  
  ScopedBindingBuilder to(TypeLiteral<? extends T> paramTypeLiteral);
  
  ScopedBindingBuilder to(Key<? extends T> paramKey);
  
  void toInstance(T paramT);
  
  ScopedBindingBuilder toProvider(Provider<? extends T> paramProvider);
  
  ScopedBindingBuilder toProvider(Provider<? extends T> paramProvider);
  
  ScopedBindingBuilder toProvider(Class<? extends Provider<? extends T>> paramClass);
  
  ScopedBindingBuilder toProvider(TypeLiteral<? extends Provider<? extends T>> paramTypeLiteral);
  
  ScopedBindingBuilder toProvider(Key<? extends Provider<? extends T>> paramKey);
  
  <S extends T> ScopedBindingBuilder toConstructor(Constructor<S> paramConstructor);
  
  <S extends T> ScopedBindingBuilder toConstructor(Constructor<S> paramConstructor, TypeLiteral<? extends S> paramTypeLiteral);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\binder\LinkedBindingBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */