package org.checkerframework.checker.nullness.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.InvisibleQualifier;
import org.checkerframework.framework.qual.SubtypeOf;
import org.checkerframework.framework.qual.TargetLocations;
import org.checkerframework.framework.qual.TypeUseLocation;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@TargetLocations({TypeUseLocation.EXPLICIT_LOWER_BOUND, TypeUseLocation.EXPLICIT_UPPER_BOUND})
@InvisibleQualifier
@SubtypeOf({KeyFor.class})
public @interface KeyForBottom {}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\checker\nullness\qual\KeyForBottom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */