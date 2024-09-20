package com.google.inject.spi;

import com.google.inject.TypeLiteral;

public interface TypeListener {
  <I> void hear(TypeLiteral<I> paramTypeLiteral, TypeEncounter<I> paramTypeEncounter);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\TypeListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */