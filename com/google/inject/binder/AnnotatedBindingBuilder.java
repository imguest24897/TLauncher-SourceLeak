package com.google.inject.binder;

import java.lang.annotation.Annotation;

public interface AnnotatedBindingBuilder<T> extends LinkedBindingBuilder<T> {
  LinkedBindingBuilder<T> annotatedWith(Class<? extends Annotation> paramClass);
  
  LinkedBindingBuilder<T> annotatedWith(Annotation paramAnnotation);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\binder\AnnotatedBindingBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */