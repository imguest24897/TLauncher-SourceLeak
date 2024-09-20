package javax.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\javax\inject\Scope.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */