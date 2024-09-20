package com.google.inject.multibindings;

import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.spi.Element;
import java.util.Set;

public interface OptionalBinderBinding<T> {
  Key<T> getKey();
  
  Set<Key<?>> getAlternateKeys();
  
  Binding<?> getDefaultBinding();
  
  Binding<?> getActualBinding();
  
  boolean containsElement(Element paramElement);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\multibindings\OptionalBinderBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */