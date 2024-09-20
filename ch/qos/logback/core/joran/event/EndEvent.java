/*    */ package ch.qos.logback.core.joran.event;
/*    */ 
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EndEvent
/*    */   extends SaxEvent
/*    */ {
/*    */   EndEvent(String namespaceURI, String localName, String qName, Locator locator) {
/* 21 */     super(namespaceURI, localName, qName, locator);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 26 */     return "  EndEvent(" + getQName() + ")  [" + this.locator.getLineNumber() + "," + this.locator.getColumnNumber() + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\ch\qos\logback\core\joran\event\EndEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */