package com.google.inject;

public interface Scope {
  <T> Provider<T> scope(Key<T> paramKey, Provider<T> paramProvider);
  
  String toString();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\Scope.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */