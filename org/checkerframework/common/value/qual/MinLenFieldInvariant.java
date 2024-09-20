package org.checkerframework.common.value.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface MinLenFieldInvariant {
  int[] minLen();
  
  String[] field();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\common\value\qual\MinLenFieldInvariant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */