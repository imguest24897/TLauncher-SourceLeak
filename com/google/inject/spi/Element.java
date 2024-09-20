package com.google.inject.spi;

import com.google.inject.Binder;

public interface Element {
  Object getSource();
  
  <T> T acceptVisitor(ElementVisitor<T> paramElementVisitor);
  
  void applyTo(Binder paramBinder);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\Element.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */