package com.beust.jcommander;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface SubParameter {
  int order() default -1;
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\SubParameter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */