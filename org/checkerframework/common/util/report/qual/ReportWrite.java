package org.checkerframework.common.util.report.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ReportWrite {}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\commo\\util\report\qual\ReportWrite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */