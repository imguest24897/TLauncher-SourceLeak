package com.google.inject.spi;

import com.google.inject.Binding;
import com.google.inject.Key;
import java.util.Set;

public interface ConvertedConstantBinding<T> extends Binding<T>, HasDependencies {
  T getValue();
  
  TypeConverterBinding getTypeConverterBinding();
  
  Key<String> getSourceKey();
  
  Set<Dependency<?>> getDependencies();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\ConvertedConstantBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */