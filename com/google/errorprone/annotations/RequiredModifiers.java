package com.google.errorprone.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.lang.model.element.Modifier;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.ANNOTATION_TYPE})
public @interface RequiredModifiers {
  Modifier[] value();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\errorprone\annotations\RequiredModifiers.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */