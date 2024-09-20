package com.google.inject;

import com.google.inject.spi.BindingScopingVisitor;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.Element;

public interface Binding<T> extends Element {
  Key<T> getKey();
  
  Provider<T> getProvider();
  
  <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> paramBindingTargetVisitor);
  
  <V> V acceptScopingVisitor(BindingScopingVisitor<V> paramBindingScopingVisitor);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\Binding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */