package org.aopalliance.intercept;

import java.lang.reflect.AccessibleObject;

public interface Joinpoint {
  Object proceed() throws Throwable;
  
  Object getThis();
  
  AccessibleObject getStaticPart();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\aopalliance\intercept\Joinpoint.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */