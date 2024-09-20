package javax.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Named {
  String value() default "";
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\javax\inject\Named.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */