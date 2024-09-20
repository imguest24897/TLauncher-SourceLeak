package com.google.inject.internal;

import com.google.inject.spi.Dependency;

interface InternalFactory<T> {
  T get(InternalContext paramInternalContext, Dependency<?> paramDependency, boolean paramBoolean) throws InternalProvisionException;
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\InternalFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */