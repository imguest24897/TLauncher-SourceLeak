package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.meta.TypeQualifierDefault;

@Documented
@Nonnull
@TypeQualifierDefault({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParametersAreNonnullByDefault {}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\javax\annotation\ParametersAreNonnullByDefault.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */