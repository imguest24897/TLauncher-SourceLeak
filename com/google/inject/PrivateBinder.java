package com.google.inject;

import com.google.inject.binder.AnnotatedElementBuilder;

public interface PrivateBinder extends Binder {
  void expose(Key<?> paramKey);
  
  AnnotatedElementBuilder expose(Class<?> paramClass);
  
  AnnotatedElementBuilder expose(TypeLiteral<?> paramTypeLiteral);
  
  PrivateBinder withSource(Object paramObject);
  
  PrivateBinder skipSources(Class<?>... paramVarArgs);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\PrivateBinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */