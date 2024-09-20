package com.google.inject;

import com.google.inject.spi.Element;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.TypeConverterBinding;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Injector {
  void injectMembers(Object paramObject);
  
  <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> paramTypeLiteral);
  
  <T> MembersInjector<T> getMembersInjector(Class<T> paramClass);
  
  Map<Key<?>, Binding<?>> getBindings();
  
  Map<Key<?>, Binding<?>> getAllBindings();
  
  <T> Binding<T> getBinding(Key<T> paramKey);
  
  <T> Binding<T> getBinding(Class<T> paramClass);
  
  <T> Binding<T> getExistingBinding(Key<T> paramKey);
  
  <T> List<Binding<T>> findBindingsByType(TypeLiteral<T> paramTypeLiteral);
  
  <T> Provider<T> getProvider(Key<T> paramKey);
  
  <T> Provider<T> getProvider(Class<T> paramClass);
  
  <T> T getInstance(Key<T> paramKey);
  
  <T> T getInstance(Class<T> paramClass);
  
  Injector getParent();
  
  Injector createChildInjector(Iterable<? extends Module> paramIterable);
  
  Injector createChildInjector(Module... paramVarArgs);
  
  Map<Class<? extends Annotation>, Scope> getScopeBindings();
  
  Set<TypeConverterBinding> getTypeConverterBindings();
  
  List<Element> getElements();
  
  Map<TypeLiteral<?>, List<InjectionPoint>> getAllMembersInjectorInjectionPoints();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\Injector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */