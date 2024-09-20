package com.google.inject.binder;

import java.lang.annotation.Annotation;

public interface AnnotatedConstantBindingBuilder {
  ConstantBindingBuilder annotatedWith(Class<? extends Annotation> paramClass);
  
  ConstantBindingBuilder annotatedWith(Annotation paramAnnotation);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\binder\AnnotatedConstantBindingBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */