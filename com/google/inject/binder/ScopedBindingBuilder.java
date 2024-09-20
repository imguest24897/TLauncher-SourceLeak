package com.google.inject.binder;

import com.google.inject.Scope;
import java.lang.annotation.Annotation;

public interface ScopedBindingBuilder {
  void in(Class<? extends Annotation> paramClass);
  
  void in(Scope paramScope);
  
  void asEagerSingleton();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\binder\ScopedBindingBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */