/*    */ package by.gdev.handler;
/*    */ 
/*    */ import by.gdev.model.ExceptionMessage;
/*    */ import java.util.Objects;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ public class ValidateTempNull implements ValidateEnvironment {
/*    */   public ValidateTempNull(ResourceBundle bundle) {
/*  9 */     this.bundle = bundle;
/*    */   }
/*    */   
/*    */   ResourceBundle bundle;
/*    */   
/*    */   public boolean validate() {
/* 15 */     return Objects.nonNull(System.getProperty("java.io.tmpdir"));
/*    */   }
/*    */ 
/*    */   
/*    */   public ExceptionMessage getExceptionMessage() {
/* 20 */     return new ExceptionMessage(this.bundle.getString("validate.tempnull"));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\handler\ValidateTempNull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */