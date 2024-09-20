package com.google.inject.spi;

import com.google.inject.Binder;
import com.google.inject.Binding;

public interface ExposedBinding<T> extends Binding<T>, HasDependencies {
  PrivateElements getPrivateElements();
  
  void applyTo(Binder paramBinder);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\ExposedBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */