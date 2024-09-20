package com.google.inject.spi;

import com.google.inject.Binding;
import com.google.inject.Provider;
import java.util.Set;
import javax.inject.Provider;

public interface ProviderInstanceBinding<T> extends Binding<T>, HasDependencies {
  @Deprecated
  Provider<? extends T> getProviderInstance();
  
  Provider<? extends T> getUserSuppliedProvider();
  
  Set<InjectionPoint> getInjectionPoints();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\ProviderInstanceBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */