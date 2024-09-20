package org.checkerframework.checker.index.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER, ElementType.METHOD})
public @interface LengthOf {
  String[] value();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\checker\index\qual\LengthOf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */