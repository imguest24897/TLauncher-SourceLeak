package org.checkerframework.checker.index.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.JavaExpression;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface HasSubsequence {
  @JavaExpression
  String subsequence();
  
  @JavaExpression
  String from();
  
  @JavaExpression
  String to();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\checker\index\qual\HasSubsequence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */