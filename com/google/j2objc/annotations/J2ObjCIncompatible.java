package com.google.j2objc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface J2ObjCIncompatible {}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\j2objc\annotations\J2ObjCIncompatible.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */