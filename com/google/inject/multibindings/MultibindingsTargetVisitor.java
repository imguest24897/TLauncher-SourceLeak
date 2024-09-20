package com.google.inject.multibindings;

import com.google.inject.spi.BindingTargetVisitor;

public interface MultibindingsTargetVisitor<T, V> extends BindingTargetVisitor<T, V> {
  V visit(MultibinderBinding<? extends T> paramMultibinderBinding);
  
  V visit(MapBinderBinding<? extends T> paramMapBinderBinding);
  
  V visit(OptionalBinderBinding<? extends T> paramOptionalBinderBinding);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\multibindings\MultibindingsTargetVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */