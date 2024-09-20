package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.meta.TypeQualifierDefault;

@Documented
@Nullable
@TypeQualifierDefault({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParametersAreNullableByDefault {}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\javax\annotation\ParametersAreNullableByDefault.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */