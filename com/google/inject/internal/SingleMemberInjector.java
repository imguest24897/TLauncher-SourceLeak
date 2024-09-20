package com.google.inject.internal;

import com.google.inject.spi.InjectionPoint;

interface SingleMemberInjector {
  void inject(InternalContext paramInternalContext, Object paramObject) throws InternalProvisionException;
  
  InjectionPoint getInjectionPoint();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\SingleMemberInjector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */