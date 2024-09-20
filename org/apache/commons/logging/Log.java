package org.apache.commons.logging;

public interface Log {
  boolean isDebugEnabled();
  
  boolean isErrorEnabled();
  
  boolean isFatalEnabled();
  
  boolean isInfoEnabled();
  
  boolean isTraceEnabled();
  
  boolean isWarnEnabled();
  
  void trace(Object paramObject);
  
  void trace(Object paramObject, Throwable paramThrowable);
  
  void debug(Object paramObject);
  
  void debug(Object paramObject, Throwable paramThrowable);
  
  void info(Object paramObject);
  
  void info(Object paramObject, Throwable paramThrowable);
  
  void warn(Object paramObject);
  
  void warn(Object paramObject, Throwable paramThrowable);
  
  void error(Object paramObject);
  
  void error(Object paramObject, Throwable paramThrowable);
  
  void fatal(Object paramObject);
  
  void fatal(Object paramObject, Throwable paramThrowable);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\logging\Log.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */