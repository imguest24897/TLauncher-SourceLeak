package org.checkerframework.common.value.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.SubtypeOf;

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({})
@SubtypeOf({UnknownVal.class})
public @interface IntRangeFromPositive {}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\common\value\qual\IntRangeFromPositive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */