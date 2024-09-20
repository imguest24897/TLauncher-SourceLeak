package com.google.inject.spi;

import com.google.inject.Binding;

public interface ProvisionListener {
  <T> void onProvision(ProvisionInvocation<T> paramProvisionInvocation);
  
  public static abstract class ProvisionInvocation<T> {
    public abstract Binding<T> getBinding();
    
    public abstract T provision();
  }
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\ProvisionListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */