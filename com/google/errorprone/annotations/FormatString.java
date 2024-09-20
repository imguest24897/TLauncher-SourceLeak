package com.google.errorprone.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.PARAMETER})
public @interface FormatString {}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\errorprone\annotations\FormatString.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */