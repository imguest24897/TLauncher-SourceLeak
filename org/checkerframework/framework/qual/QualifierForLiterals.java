package org.checkerframework.framework.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface QualifierForLiterals {
  LiteralKind[] value() default {};
  
  String[] stringPatterns() default {};
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\framework\qual\QualifierForLiterals.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */