package org.checkerframework.checker.index.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.InheritedAnnotation;
import org.checkerframework.framework.qual.JavaExpression;
import org.checkerframework.framework.qual.PostconditionAnnotation;
import org.checkerframework.framework.qual.QualifierArgument;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@PostconditionAnnotation(qualifier = LTLengthOf.class)
@InheritedAnnotation
@Repeatable(EnsuresLTLengthOf.List.class)
public @interface EnsuresLTLengthOf {
  @JavaExpression
  String[] value();
  
  @JavaExpression
  @QualifierArgument("value")
  String[] targetValue();
  
  @JavaExpression
  @QualifierArgument("offset")
  String[] offset() default {};
  
  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
  @PostconditionAnnotation(qualifier = LTLengthOf.class)
  @InheritedAnnotation
  public static @interface List {
    EnsuresLTLengthOf[] value();
  }
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\checker\index\qual\EnsuresLTLengthOf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */