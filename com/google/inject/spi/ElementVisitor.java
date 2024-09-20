package com.google.inject.spi;

import com.google.inject.Binding;

public interface ElementVisitor<V> {
  <T> V visit(Binding<T> paramBinding);
  
  V visit(InterceptorBinding paramInterceptorBinding);
  
  V visit(ScopeBinding paramScopeBinding);
  
  V visit(TypeConverterBinding paramTypeConverterBinding);
  
  V visit(InjectionRequest<?> paramInjectionRequest);
  
  V visit(StaticInjectionRequest paramStaticInjectionRequest);
  
  <T> V visit(ProviderLookup<T> paramProviderLookup);
  
  <T> V visit(MembersInjectorLookup<T> paramMembersInjectorLookup);
  
  V visit(Message paramMessage);
  
  V visit(PrivateElements paramPrivateElements);
  
  V visit(TypeListenerBinding paramTypeListenerBinding);
  
  V visit(ProvisionListenerBinding paramProvisionListenerBinding);
  
  V visit(RequireExplicitBindingsOption paramRequireExplicitBindingsOption);
  
  V visit(DisableCircularProxiesOption paramDisableCircularProxiesOption);
  
  V visit(RequireAtInjectOnConstructorsOption paramRequireAtInjectOnConstructorsOption);
  
  V visit(RequireExactBindingAnnotationsOption paramRequireExactBindingAnnotationsOption);
  
  V visit(ModuleAnnotatedMethodScannerBinding paramModuleAnnotatedMethodScannerBinding);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\ElementVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */