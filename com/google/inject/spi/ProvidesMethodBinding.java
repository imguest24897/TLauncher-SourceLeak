package com.google.inject.spi;

import com.google.inject.Key;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface ProvidesMethodBinding<T> extends HasDependencies {
  Method getMethod();
  
  Object getEnclosingInstance();
  
  Key<T> getKey();
  
  Annotation getAnnotation();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\ProvidesMethodBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */