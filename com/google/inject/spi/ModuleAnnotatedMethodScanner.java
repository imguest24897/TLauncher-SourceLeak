package com.google.inject.spi;

import com.google.inject.Binder;
import com.google.inject.Key;
import java.lang.annotation.Annotation;
import java.util.Set;

public abstract class ModuleAnnotatedMethodScanner {
  public abstract Set<? extends Class<? extends Annotation>> annotationClasses();
  
  public abstract <T> Key<T> prepareMethod(Binder paramBinder, Annotation paramAnnotation, Key<T> paramKey, InjectionPoint paramInjectionPoint);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\ModuleAnnotatedMethodScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */