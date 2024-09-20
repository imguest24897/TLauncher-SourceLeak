package com.google.inject.multibindings;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@MapKey(unwrapValue = true)
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StringMapKey {
  String value();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\multibindings\StringMapKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */