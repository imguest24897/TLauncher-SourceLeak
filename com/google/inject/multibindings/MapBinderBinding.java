package com.google.inject.multibindings;

import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.Element;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MapBinderBinding<T> {
  Key<T> getMapKey();
  
  Set<Key<?>> getAlternateMapKeys();
  
  TypeLiteral<?> getKeyTypeLiteral();
  
  TypeLiteral<?> getValueTypeLiteral();
  
  List<Map.Entry<?, Binding<?>>> getEntries();
  
  List<Map.Entry<?, Binding<?>>> getEntries(Iterable<? extends Element> paramIterable);
  
  boolean permitsDuplicates();
  
  boolean containsElement(Element paramElement);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\multibindings\MapBinderBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */