package com.google.inject.spi;

import com.google.inject.Binding;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aopalliance.intercept.MethodInterceptor;

public interface ConstructorBinding<T> extends Binding<T>, HasDependencies {
  InjectionPoint getConstructor();
  
  Set<InjectionPoint> getInjectableMembers();
  
  Map<Method, List<MethodInterceptor>> getMethodInterceptors();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\ConstructorBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */