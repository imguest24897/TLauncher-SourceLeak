package org.checkerframework.checker.nullness.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.ConditionalPostconditionAnnotation;
import org.checkerframework.framework.qual.InheritedAnnotation;
import org.checkerframework.framework.qual.JavaExpression;
import org.checkerframework.framework.qual.QualifierArgument;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@ConditionalPostconditionAnnotation(qualifier = KeyFor.class)
@InheritedAnnotation
@Repeatable(EnsuresKeyForIf.List.class)
public @interface EnsuresKeyForIf {
  boolean result();
  
  String[] expression();
  
  @JavaExpression
  @QualifierArgument("value")
  String[] map();
  
  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
  @ConditionalPostconditionAnnotation(qualifier = KeyFor.class)
  @InheritedAnnotation
  public static @interface List {
    EnsuresKeyForIf[] value();
  }
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\checker\nullness\qual\EnsuresKeyForIf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */