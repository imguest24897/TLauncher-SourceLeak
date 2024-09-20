package com.google.errorprone.annotations.concurrent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface UnlockMethod {
  String[] value();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\errorprone\annotations\concurrent\UnlockMethod.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */