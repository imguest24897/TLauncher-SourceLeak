package com.beust.jcommander;

public interface IStringConverterFactory {
  Class<? extends IStringConverter<?>> getConverter(Class<?> paramClass);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\IStringConverterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */