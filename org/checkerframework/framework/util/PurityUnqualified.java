package org.checkerframework.framework.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;
import org.checkerframework.framework.qual.InvisibleQualifier;
import org.checkerframework.framework.qual.SubtypeOf;

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@SubtypeOf({})
@DefaultQualifierInHierarchy
@InvisibleQualifier
public @interface PurityUnqualified {}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\framewor\\util\PurityUnqualified.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */