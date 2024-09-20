package com.google.errorprone.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface RestrictedApi {
  String checkerName() default "RestrictedApi";
  
  String explanation();
  
  String link();
  
  String allowedOnPath() default "";
  
  Class<? extends Annotation>[] whitelistAnnotations() default {};
  
  Class<? extends Annotation>[] whitelistWithWarningAnnotations() default {};
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\errorprone\annotations\RestrictedApi.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */