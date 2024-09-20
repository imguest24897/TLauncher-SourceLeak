package com.google.inject.spi;

import com.google.inject.Injector;
import com.google.inject.Key;
import java.util.List;
import java.util.Set;

public interface PrivateElements extends Element {
  List<Element> getElements();
  
  Injector getInjector();
  
  Set<Key<?>> getExposedKeys();
  
  Object getExposedSource(Key<?> paramKey);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\PrivateElements.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */