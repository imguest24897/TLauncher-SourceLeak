package com.google.j2objc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD})
public @interface Property {
  String value() default "";
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\j2objc\annotations\Property.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */