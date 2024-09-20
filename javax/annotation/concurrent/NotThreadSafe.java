package javax.annotation.concurrent;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface NotThreadSafe {}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\javax\annotation\concurrent\NotThreadSafe.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */