package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@GwtCompatible
final class Partially {
  @Retention(RetentionPolicy.CLASS)
  @Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
  @Documented
  static @interface GwtIncompatible {
    String value();
  }
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\commo\\util\concurrent\Partially.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */