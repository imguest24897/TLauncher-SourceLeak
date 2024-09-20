package com.google.inject.spi;

import com.google.inject.Binding;
import com.google.inject.Key;

public interface ProviderBinding<T extends com.google.inject.Provider<?>> extends Binding<T> {
  Key<?> getProvidedKey();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\ProviderBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */