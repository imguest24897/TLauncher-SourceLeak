package com.beust.jcommander.validators;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

public class NoValueValidator<T> implements IValueValidator<T> {
  public void validate(String parameterName, T parameterValue) throws ParameterException {}
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\validators\NoValueValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */