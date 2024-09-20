package com.google.inject.spi;

import com.google.inject.Binding;
import com.google.inject.Key;

public interface LinkedKeyBinding<T> extends Binding<T> {
  Key<? extends T> getLinkedKey();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\LinkedKeyBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */