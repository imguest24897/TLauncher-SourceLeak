package com.beust.jcommander;

import com.beust.jcommander.converters.IParameterSplitter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Parameter {
  public static final int DEFAULT_ARITY = -1;
  
  String[] names() default {};
  
  String description() default "";
  
  boolean required() default false;
  
  String descriptionKey() default "";
  
  int arity() default -1;
  
  boolean password() default false;
  
  Class<? extends IStringConverter<?>> converter() default com.beust.jcommander.converters.NoConverter.class;
  
  Class<? extends IStringConverter<?>> listConverter() default com.beust.jcommander.converters.NoConverter.class;
  
  boolean hidden() default false;
  
  Class<? extends IParameterValidator>[] validateWith() default {com.beust.jcommander.validators.NoValidator.class};
  
  Class<? extends IValueValidator>[] validateValueWith() default {com.beust.jcommander.validators.NoValueValidator.class};
  
  boolean variableArity() default false;
  
  Class<? extends IParameterSplitter> splitter() default com.beust.jcommander.converters.CommaParameterSplitter.class;
  
  boolean echoInput() default false;
  
  boolean help() default false;
  
  boolean forceNonOverwritable() default false;
  
  int order() default -1;
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\Parameter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */