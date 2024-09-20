/*    */ package by.gdev.handler;
/*    */ 
/*    */ import by.gdev.model.ExceptionMessage;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.FileStore;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Paths;
/*    */ import java.util.ResourceBundle;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class ValidatedPartionSize implements ValidateEnvironment {
/* 14 */   private static final Logger log = LoggerFactory.getLogger(ValidatedPartionSize.class); public ValidatedPartionSize(long minMemorySize, File workDir, ResourceBundle bundle) {
/* 15 */     this.minMemorySize = minMemorySize; this.workDir = workDir; this.bundle = bundle;
/*    */   }
/*    */   
/*    */   private long minMemorySize;
/*    */   private File workDir;
/*    */   private ResourceBundle bundle;
/*    */   
/*    */   public boolean validate() {
/*    */     try {
/* 24 */       if (!this.workDir.exists())
/* 25 */         this.workDir.mkdirs(); 
/* 26 */       FileStore store = Files.getFileStore(Paths.get(this.workDir.getAbsolutePath(), new String[0]));
/* 27 */       long res = store.getUsableSpace();
/* 28 */       return (res > this.minMemorySize * 1024L * 1024L);
/* 29 */     } catch (IOException e) {
/* 30 */       log.error("Error", e);
/*    */       
/* 32 */       return true;
/*    */     } 
/*    */   }
/*    */   
/*    */   public ExceptionMessage getExceptionMessage() {
/* 37 */     return new ExceptionMessage(String.format(this.bundle.getString("validate.size"), new Object[] { this.workDir, Long.valueOf(this.workDir.getFreeSpace() / 1024L / 1024L), this.workDir }));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\handler\ValidatedPartionSize.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */