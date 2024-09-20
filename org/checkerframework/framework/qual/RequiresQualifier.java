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
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Repeatable(RequiresQualifier.List.class)
public @interface RequiresQualifier {
  String[] expression();
  
  Class<? extends Annotation> qualifier();
  
  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
  public static @interface List {
    RequiresQualifier[] value();
  }
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\framework\qual\RequiresQualifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */