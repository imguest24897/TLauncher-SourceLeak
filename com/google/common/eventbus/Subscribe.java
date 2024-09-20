package com.google.common.eventbus;

import com.google.common.annotations.Beta;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Beta
public @interface Subscribe {}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\eventbus\Subscribe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */