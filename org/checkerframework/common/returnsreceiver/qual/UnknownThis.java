package org.checkerframework.common.returnsreceiver.qual;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.DefaultFor;
import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;
import org.checkerframework.framework.qual.LiteralKind;
import org.checkerframework.framework.qual.QualifierForLiterals;
import org.checkerframework.framework.qual.SubtypeOf;
import org.checkerframework.framework.qual.TypeUseLocation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@DefaultQualifierInHierarchy
@SubtypeOf({})
@QualifierForLiterals({LiteralKind.NULL})
@DefaultFor({TypeUseLocation.LOWER_BOUND})
public @interface UnknownThis {}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\common\returnsreceiver\qual\UnknownThis.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */