package org.checkerframework.checker.fenum.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.PolymorphicQualifier;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@PolymorphicQualifier(FenumTop.class)
public @interface PolyFenum {}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\checker\fenum\qual\PolyFenum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */