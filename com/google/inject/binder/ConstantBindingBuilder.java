package com.google.inject.binder;

public interface ConstantBindingBuilder {
  void to(String paramString);
  
  void to(int paramInt);
  
  void to(long paramLong);
  
  void to(boolean paramBoolean);
  
  void to(double paramDouble);
  
  void to(float paramFloat);
  
  void to(short paramShort);
  
  void to(char paramChar);
  
  void to(byte paramByte);
  
  void to(Class<?> paramClass);
  
  <E extends Enum<E>> void to(E paramE);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\binder\ConstantBindingBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */