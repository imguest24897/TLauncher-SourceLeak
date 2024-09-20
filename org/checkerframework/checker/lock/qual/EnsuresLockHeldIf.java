package org.checkerframework.checker.lock.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.ConditionalPostconditionAnnotation;
import org.checkerframework.framework.qual.InheritedAnnotation;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@ConditionalPostconditionAnnotation(qualifier = LockHeld.class)
@InheritedAnnotation
@Repeatable(EnsuresLockHeldIf.List.class)
public @interface EnsuresLockHeldIf {
  String[] expression();
  
  boolean result();
  
  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
  @ConditionalPostconditionAnnotation(qualifier = LockHeld.class)
  @InheritedAnnotation
  public static @interface List {
    EnsuresLockHeldIf[] value();
  }
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\checker\lock\qual\EnsuresLockHeldIf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */