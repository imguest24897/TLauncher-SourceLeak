package com.google.inject.spi;

import com.google.inject.Scope;
import java.lang.annotation.Annotation;

public interface BindingScopingVisitor<V> {
  V visitEagerSingleton();
  
  V visitScope(Scope paramScope);
  
  V visitScopeAnnotation(Class<? extends Annotation> paramClass);
  
  V visitNoScoping();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\BindingScopingVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */