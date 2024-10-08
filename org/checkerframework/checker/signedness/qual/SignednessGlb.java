package org.checkerframework.checker.signedness.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.LiteralKind;
import org.checkerframework.framework.qual.QualifierForLiterals;
import org.checkerframework.framework.qual.SubtypeOf;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@SubtypeOf({Unsigned.class, Signed.class})
@QualifierForLiterals({LiteralKind.INT, LiteralKind.LONG, LiteralKind.CHAR})
public @interface SignednessGlb {}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\checker\signedness\qual\SignednessGlb.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */