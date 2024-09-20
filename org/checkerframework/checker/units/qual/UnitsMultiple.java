package org.checkerframework.checker.units.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface UnitsMultiple {
  Class<? extends Annotation> quantity();
  
  Prefix prefix() default Prefix.one;
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\checke\\units\qual\UnitsMultiple.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */