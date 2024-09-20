/*    */ package ch.qos.logback.core;
/*    */ 
/*    */ import ch.qos.logback.core.spi.ContextAwareBase;
/*    */ import ch.qos.logback.core.spi.PropertyDefiner;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class PropertyDefinerBase
/*    */   extends ContextAwareBase
/*    */   implements PropertyDefiner
/*    */ {
/*    */   protected static String booleanAsStr(boolean bool) {
/* 27 */     return bool ? Boolean.TRUE.toString() : Boolean.FALSE.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\ch\qos\logback\core\PropertyDefinerBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */