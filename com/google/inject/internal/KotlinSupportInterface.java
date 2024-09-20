package com.google.inject.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Predicate;

public interface KotlinSupportInterface {
  Annotation[] getAnnotations(Field paramField);
  
  boolean isNullable(Field paramField);
  
  Predicate<Integer> getIsParameterKotlinNullablePredicate(Constructor<?> paramConstructor);
  
  Predicate<Integer> getIsParameterKotlinNullablePredicate(Method paramMethod);
  
  void checkConstructorParameterAnnotations(Constructor<?> paramConstructor, Errors paramErrors);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\KotlinSupportInterface.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */