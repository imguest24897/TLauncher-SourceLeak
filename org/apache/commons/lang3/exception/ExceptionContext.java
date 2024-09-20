package org.apache.commons.lang3.exception;

import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;

public interface ExceptionContext {
  ExceptionContext addContextValue(String paramString, Object paramObject);
  
  List<Pair<String, Object>> getContextEntries();
  
  Set<String> getContextLabels();
  
  List<Object> getContextValues(String paramString);
  
  Object getFirstContextValue(String paramString);
  
  String getFormattedExceptionMessage(String paramString);
  
  ExceptionContext setContextValue(String paramString, Object paramObject);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\exception\ExceptionContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */