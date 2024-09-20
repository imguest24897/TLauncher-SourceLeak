package ch.qos.logback.core.joran.conditional;

import ch.qos.logback.core.joran.event.SaxEvent;
import java.util.List;

class IfState {
  Boolean boolResult;
  
  List<SaxEvent> thenSaxEventList;
  
  List<SaxEvent> elseSaxEventList;
  
  boolean active;
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\ch\qos\logback\core\joran\conditional\IfState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */