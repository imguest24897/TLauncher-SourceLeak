package com.google.inject.internal;

import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;

interface Lookups {
  <T> Provider<T> getProvider(Key<T> paramKey);
  
  <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> paramTypeLiteral);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\Lookups.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */