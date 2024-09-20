package com.google.j2objc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.PACKAGE})
@Retention(RetentionPolicy.CLASS)
public @interface ObjectiveCName {
  String value();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\j2objc\annotations\ObjectiveCName.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */