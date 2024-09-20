package org.checkerframework.framework.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.PACKAGE, ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.PARAMETER})
@Repeatable(DefaultQualifier.List.class)
public @interface DefaultQualifier {
  Class<? extends Annotation> value();
  
  TypeUseLocation[] locations() default {TypeUseLocation.ALL};
  
  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.PACKAGE, ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.PARAMETER})
  public static @interface List {
    DefaultQualifier[] value();
  }
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\framework\qual\DefaultQualifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */