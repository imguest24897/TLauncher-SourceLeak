package org.checkerframework.common.value.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.SubtypeOf;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@SubtypeOf({UnknownVal.class})
public @interface IntRange {
  long from() default -9223372036854775808L;
  
  long to() default 9223372036854775807L;
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\common\value\qual\IntRange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */