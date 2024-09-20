/*    */ package by.gdev.ui;
/*    */ 
/*    */ import javax.swing.JLabel;
/*    */ 
/*    */ public class JLabelHtmlWrapper
/*    */   extends JLabel {
/*    */   private static final long serialVersionUID = 2703012842940047505L;
/*    */   
/*    */   public JLabelHtmlWrapper(String s) {
/* 10 */     setText(s);
/*    */   }
/*    */   
/*    */   public void setText(String text) {
/* 14 */     super.setText("<html>" + text + "</html>");
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gde\\ui\JLabelHtmlWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */