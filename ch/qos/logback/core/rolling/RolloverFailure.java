/*    */ package ch.qos.logback.core.rolling;
/*    */ 
/*    */ import ch.qos.logback.core.LogbackException;
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
/*    */ 
/*    */ public class RolloverFailure
/*    */   extends LogbackException
/*    */ {
/*    */   private static final long serialVersionUID = -4407533730831239458L;
/*    */   
/*    */   public RolloverFailure(String msg) {
/* 28 */     super(msg);
/*    */   }
/*    */   
/*    */   public RolloverFailure(String message, Throwable cause) {
/* 32 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\ch\qos\logback\core\rolling\RolloverFailure.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */