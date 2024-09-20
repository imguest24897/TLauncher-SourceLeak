/*    */ package by.gdev.handler;
/*    */ 
/*    */ import by.gdev.model.ExceptionMessage;
/*    */ import java.io.File;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Paths;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ public class ValidateWorkDir implements ValidateEnvironment {
/*    */   public ValidateWorkDir(String workDir, ResourceBundle bundle) {
/* 11 */     this.workDir = workDir; this.bundle = bundle;
/*    */   }
/*    */ 
/*    */   
/*    */   String workDir;
/*    */   ResourceBundle bundle;
/*    */   
/*    */   public boolean validate() {
/* 19 */     if ((new File(this.workDir)).exists() && (
/* 20 */       !Files.isWritable(Paths.get(this.workDir, new String[0])) || !Files.isReadable(Paths.get(this.workDir, new String[0])))) {
/* 21 */       return false;
/*    */     }
/* 23 */     return true;
/*    */   }
/*    */   
/*    */   public ExceptionMessage getExceptionMessage() {
/* 27 */     return new ExceptionMessage(String.format(this.bundle.getString("validate.workdir"), new Object[] { this.workDir }));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\handler\ValidateWorkDir.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */