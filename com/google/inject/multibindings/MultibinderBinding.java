package com.google.inject.multibindings;

import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.Element;
import java.util.List;
import java.util.Set;

public interface MultibinderBinding<T> {
  Key<T> getSetKey();
  
  Set<Key<?>> getAlternateSetKeys();
  
  TypeLiteral<?> getElementTypeLiteral();
  
  List<Binding<?>> getElements();
  
  boolean permitsDuplicates();
  
  boolean containsElement(Element paramElement);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\multibindings\MultibinderBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */