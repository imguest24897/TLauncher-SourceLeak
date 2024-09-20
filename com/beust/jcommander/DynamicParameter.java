package com.beust.jcommander;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DynamicParameter {
  String[] names() default {};
  
  boolean required() default false;
  
  String description() default "";
  
  String descriptionKey() default "";
  
  boolean hidden() default false;
  
  Class<? extends IParameterValidator>[] validateWith() default {com.beust.jcommander.validators.NoValidator.class};
  
  String assignment() default "=";
  
  Class<? extends IValueValidator>[] validateValueWith() default {com.beust.jcommander.validators.NoValueValidator.class};
  
  int order() default -1;
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\DynamicParameter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */