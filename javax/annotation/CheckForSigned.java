package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.meta.TypeQualifierNickname;
import javax.annotation.meta.When;

@Documented
@Nonnegative(when = When.MAYBE)
@Retention(RetentionPolicy.RUNTIME)
@TypeQualifierNickname
public @interface CheckForSigned {}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\javax\annotation\CheckForSigned.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */