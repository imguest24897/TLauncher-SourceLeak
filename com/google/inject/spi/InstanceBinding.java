package com.google.inject.spi;

import com.google.inject.Binding;
import java.util.Set;

public interface InstanceBinding<T> extends Binding<T>, HasDependencies {
  T getInstance();
  
  Set<InjectionPoint> getInjectionPoints();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\InstanceBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */