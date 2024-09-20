package com.google.inject.spi;

import com.google.inject.Binding;
import com.google.inject.Key;
import javax.inject.Provider;

public interface ProviderKeyBinding<T> extends Binding<T> {
  Key<? extends Provider<? extends T>> getProviderKey();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\ProviderKeyBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */