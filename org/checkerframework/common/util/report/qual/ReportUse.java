package org.checkerframework.common.util.report.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PACKAGE, ElementType.TYPE})
public @interface ReportUse {}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\commo\\util\report\qual\ReportUse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */