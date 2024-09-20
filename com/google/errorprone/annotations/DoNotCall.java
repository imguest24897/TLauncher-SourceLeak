package com.google.errorprone.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
public @interface DoNotCall {
  String value() default "";
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\errorprone\annotations\DoNotCall.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */