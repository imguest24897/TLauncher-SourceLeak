package com.google.inject.spi;

public interface BindingTargetVisitor<T, V> {
  V visit(InstanceBinding<? extends T> paramInstanceBinding);
  
  V visit(ProviderInstanceBinding<? extends T> paramProviderInstanceBinding);
  
  V visit(ProviderKeyBinding<? extends T> paramProviderKeyBinding);
  
  V visit(LinkedKeyBinding<? extends T> paramLinkedKeyBinding);
  
  V visit(ExposedBinding<? extends T> paramExposedBinding);
  
  V visit(UntargettedBinding<? extends T> paramUntargettedBinding);
  
  V visit(ConstructorBinding<? extends T> paramConstructorBinding);
  
  V visit(ConvertedConstantBinding<? extends T> paramConvertedConstantBinding);
  
  V visit(ProviderBinding<? extends T> paramProviderBinding);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\BindingTargetVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */