package org.checkerframework.framework.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@InheritedAnnotation
@Repeatable(EnsuresQualifierIf.List.class)
public @interface EnsuresQualifierIf {
  String[] expression();
  
  Class<? extends Annotation> qualifier();
  
  boolean result();
  
  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.METHOD})
  @InheritedAnnotation
  public static @interface List {
    EnsuresQualifierIf[] value();
  }
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\framework\qual\EnsuresQualifierIf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */