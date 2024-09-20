package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.ImmutableMultimap;

@GwtIncompatible
interface ServiceManagerBridge {
  ImmutableMultimap<Service.State, Service> servicesByState();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\commo\\util\concurrent\ServiceManagerBridge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */