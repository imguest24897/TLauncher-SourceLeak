package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.meta.TypeQualifier;
import javax.annotation.meta.When;

@Documented
@TypeQualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface Untainted {
  When when() default When.ALWAYS;
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\javax\annotation\Untainted.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */