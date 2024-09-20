package com.beust.jcommander;

public interface IStringConverterInstanceFactory {
  IStringConverter<?> getConverterInstance(Parameter paramParameter, Class<?> paramClass, String paramString);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\IStringConverterInstanceFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */