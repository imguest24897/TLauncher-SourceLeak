package com.google.inject.spi;

public interface ProvidesMethodTargetVisitor<T, V> extends BindingTargetVisitor<T, V> {
  V visit(ProvidesMethodBinding<? extends T> paramProvidesMethodBinding);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\ProvidesMethodTargetVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */