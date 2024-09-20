package com.google.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Inject {
  boolean optional() default false;
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\Inject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */