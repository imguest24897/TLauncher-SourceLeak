package com.google.inject.name;

import com.google.inject.BindingAnnotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@BindingAnnotation
public @interface Named {
  String value();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\name\Named.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */