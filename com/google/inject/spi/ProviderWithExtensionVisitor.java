package com.google.inject.spi;

import com.google.inject.Provider;

public interface ProviderWithExtensionVisitor<T> extends Provider<T> {
  <B, V> V acceptExtensionVisitor(BindingTargetVisitor<B, V> paramBindingTargetVisitor, ProviderInstanceBinding<? extends B> paramProviderInstanceBinding);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\ProviderWithExtensionVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */