/*    */ package by.gdev.updater;
/*    */ 
/*    */ import by.gdev.util.DesktopUtil;
/*    */ import by.gdev.util.OSInfo;
/*    */ import javax.swing.text.Element;
/*    */ import javax.swing.text.View;
/*    */ import javax.swing.text.html.HTMLEditorKit;
/*    */ 
/*    */ public class ExtendedHTMLEditorKit
/*    */   extends HTMLEditorKit {
/*    */   private static final long serialVersionUID = 1L;
/* 12 */   protected static final ExtendedHTMLFactory extendedFactory = new ExtendedHTMLFactory();
/*    */   
/* 14 */   public static final HyperlinkProcessor defaultHyperlinkProcessor = new HyperlinkProcessor()
/*    */     {
/*    */       public void process(String link) {
/* 17 */         if (link == null)
/*    */           return; 
/* 19 */         DesktopUtil.openLink(OSInfo.getOSType(), link);
/*    */       }
/*    */     };
/*    */   
/*    */   public static class ExtendedHTMLFactory
/*    */     extends HTMLEditorKit.HTMLFactory {
/*    */     public View create(Element elem) {
/* 26 */       return super.create(elem);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gde\\updater\ExtendedHTMLEditorKit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */