/*    */ package ch.qos.logback.core.status;
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
/*    */ public class ErrorStatus
/*    */   extends StatusBase
/*    */ {
/*    */   public ErrorStatus(String msg, Object origin) {
/* 19 */     super(2, msg, origin);
/*    */   }
/*    */   
/*    */   public ErrorStatus(String msg, Object origin, Throwable t) {
/* 23 */     super(2, msg, origin, t);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\ch\qos\logback\core\status\ErrorStatus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */