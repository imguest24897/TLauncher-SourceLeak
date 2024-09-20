package com.beust.jcommander;

public interface IValueValidator<T> {
  void validate(String paramString, T paramT) throws ParameterException;
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\IValueValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */