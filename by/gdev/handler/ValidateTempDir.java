/*    */ package by.gdev.handler;
/*    */ import by.gdev.model.ExceptionMessage;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import java.nio.file.attribute.FileAttribute;
/*    */ import java.util.ResourceBundle;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class ValidateTempDir implements ValidateEnvironment {
/* 13 */   private static final Logger log = LoggerFactory.getLogger(ValidateTempDir.class); public ValidateTempDir(ResourceBundle bundle) {
/* 14 */     this.bundle = bundle;
/*    */   }
/*    */ 
/*    */   
/*    */   ResourceBundle bundle;
/*    */   
/*    */   public boolean validate() {
/* 21 */     Path folder = Paths.get(System.getProperty("java.io.tmpdir"), new String[0]);
/*    */     try {
/* 23 */       if (Files.isRegularFile(folder, new java.nio.file.LinkOption[0]))
/* 24 */         Files.delete(folder); 
/* 25 */       if (!Files.exists(folder, new java.nio.file.LinkOption[0]))
/* 26 */         Files.createDirectory(folder, (FileAttribute<?>[])new FileAttribute[0]); 
/* 27 */     } catch (IOException e) {
/* 28 */       if (e.getMessage().contains("createScrollWrapper"))
/* 29 */         return false; 
/* 30 */       log.error("Error", e);
/*    */     } 
/* 32 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public ExceptionMessage getExceptionMessage() {
/* 37 */     return new ExceptionMessage(this.bundle.getString("validate.tempdir"));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\handler\ValidateTempDir.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */