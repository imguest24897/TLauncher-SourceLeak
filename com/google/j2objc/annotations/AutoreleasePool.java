package com.google.j2objc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.SOURCE)
public @interface AutoreleasePool {}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\j2objc\annotations\AutoreleasePool.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */