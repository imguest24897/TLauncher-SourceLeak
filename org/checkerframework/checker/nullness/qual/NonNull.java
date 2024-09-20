package org.checkerframework.checker.nullness.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.DefaultFor;
import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;
import org.checkerframework.framework.qual.LiteralKind;
import org.checkerframework.framework.qual.QualifierForLiterals;
import org.checkerframework.framework.qual.SubtypeOf;
import org.checkerframework.framework.qual.TypeKind;
import org.checkerframework.framework.qual.TypeUseLocation;
import org.checkerframework.framework.qual.UpperBoundFor;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@SubtypeOf({MonotonicNonNull.class})
@QualifierForLiterals({LiteralKind.STRING})
@DefaultQualifierInHierarchy
@DefaultFor({TypeUseLocation.EXCEPTION_PARAMETER})
@UpperBoundFor(typeKinds = {TypeKind.PACKAGE, TypeKind.INT, TypeKind.BOOLEAN, TypeKind.CHAR, TypeKind.DOUBLE, TypeKind.FLOAT, TypeKind.LONG, TypeKind.SHORT, TypeKind.BYTE})
public @interface NonNull {}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\checker\nullness\qual\NonNull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */