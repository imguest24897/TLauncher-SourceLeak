/*    */ package by.gdev.handler;
/*    */ 
/*    */ import by.gdev.model.ExceptionMessage;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ public class ValidateUpdate
/*    */   implements ValidateEnvironment {
/*    */   public ValidateUpdate(ResourceBundle bundle) {
/*  9 */     this.bundle = bundle;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   ResourceBundle bundle;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean validate() {
/* 24 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public ExceptionMessage getExceptionMessage() {
/* 29 */     return new ExceptionMessage(this.bundle.getString("validate.update"));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\handler\ValidateUpdate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */