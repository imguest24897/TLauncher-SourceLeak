/*    */ package by.gdev.handler;
/*    */ 
/*    */ import by.gdev.model.ExceptionMessage;
/*    */ import by.gdev.util.DesktopUtil;
/*    */ import java.util.ResourceBundle;
/*    */ import javax.swing.UIManager;
/*    */ 
/*    */ public class ValidateFont
/*    */   implements ValidateEnvironment {
/*    */   public ValidateFont(ResourceBundle bundle) {
/* 11 */     this.bundle = bundle;
/*    */   }
/*    */ 
/*    */   
/*    */   ResourceBundle bundle;
/*    */   
/*    */   public boolean validate() {
/* 18 */     DesktopUtil.initLookAndFeel();
/*    */     try {
/* 20 */       UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
/* 21 */     } catch (ExceptionInInitializerError error) {
/* 22 */       if (error.getCause() instanceof IllegalArgumentException && 
/* 23 */         error.getCause().getMessage().contains("Text-specific LCD")) {
/* 24 */         return false;
/*    */       }
/*    */     }
/* 27 */     catch (Exception exception) {}
/*    */     
/* 29 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public ExceptionMessage getExceptionMessage() {
/* 34 */     return new ExceptionMessage(this.bundle.getString("validate.font"), String.format("https://tlauncher.org/%s/font-error.html", new Object[] { this.bundle.getLocale() }));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\handler\ValidateFont.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */