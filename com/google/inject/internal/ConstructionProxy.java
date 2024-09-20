package com.google.inject.internal;

import com.google.common.collect.ImmutableMap;
import com.google.inject.spi.InjectionPoint;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.aopalliance.intercept.MethodInterceptor;

interface ConstructionProxy<T> {
  T newInstance(Object... paramVarArgs) throws InvocationTargetException;
  
  InjectionPoint getInjectionPoint();
  
  Constructor<T> getConstructor();
  
  ImmutableMap<Method, List<MethodInterceptor>> getMethodInterceptors();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ConstructionProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */