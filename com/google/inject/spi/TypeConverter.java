package com.google.inject.spi;

import com.google.inject.TypeLiteral;

public interface TypeConverter {
  Object convert(String paramString, TypeLiteral<?> paramTypeLiteral);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\TypeConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */