package org.checkerframework.checker.nullness.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.DefaultFor;
import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;
import org.checkerframework.framework.qual.InvisibleQualifier;
import org.checkerframework.framework.qual.LiteralKind;
import org.checkerframework.framework.qual.QualifierForLiterals;
import org.checkerframework.framework.qual.SubtypeOf;
import org.checkerframework.framework.qual.TypeUseLocation;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@InvisibleQualifier
@SubtypeOf({})
@DefaultQualifierInHierarchy
@DefaultFor(value = {TypeUseLocation.LOWER_BOUND}, types = {Void.class})
@QualifierForLiterals({LiteralKind.NULL})
public @interface UnknownKeyFor {}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\checker\nullness\qual\UnknownKeyFor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */