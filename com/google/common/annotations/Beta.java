package com.google.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Documented
@GwtCompatible
public @interface Beta {}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\annotations\Beta.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */