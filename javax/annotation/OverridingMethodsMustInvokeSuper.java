package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OverridingMethodsMustInvokeSuper {}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\javax\annotation\OverridingMethodsMustInvokeSuper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */