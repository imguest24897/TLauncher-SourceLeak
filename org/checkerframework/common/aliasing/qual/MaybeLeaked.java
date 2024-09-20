package org.checkerframework.common.aliasing.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;
import org.checkerframework.framework.qual.InvisibleQualifier;
import org.checkerframework.framework.qual.SubtypeOf;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
@DefaultQualifierInHierarchy
@SubtypeOf({LeakedToResult.class})
@InvisibleQualifier
public @interface MaybeLeaked {}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\common\aliasing\qual\MaybeLeaked.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */