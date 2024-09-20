package com.google.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Provider;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ProvidedBy {
  Class<? extends Provider<?>> value();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\ProvidedBy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */