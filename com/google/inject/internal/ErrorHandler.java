package com.google.inject.internal;

import com.google.inject.spi.Message;

interface ErrorHandler {
  void handle(Object paramObject, Errors paramErrors);
  
  void handle(Message paramMessage);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ErrorHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */