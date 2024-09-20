/*    */ package by.gdev.updater;
/*    */ 
/*    */ import by.gdev.util.CoreUtil;
/*    */ import javax.swing.ImageIcon;
/*    */ import javax.swing.JCheckBox;
/*    */ 
/*    */ public class OwnImageCheckBox
/*    */   extends JCheckBox {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public OwnImageCheckBox(String text, String onText, String offText) {
/* 12 */     super(text);
/* 13 */     ImageIcon on = CoreUtil.getIcon(onText);
/* 14 */     ImageIcon off = CoreUtil.getIcon(offText);
/* 15 */     setSelectedIcon(on);
/* 16 */     setDisabledSelectedIcon(on);
/* 17 */     setPressedIcon(on);
/* 18 */     setIcon(off);
/* 19 */     setDisabledIcon(off);
/* 20 */     setOpaque(false);
/* 21 */     setFocusable(false);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gde\\updater\OwnImageCheckBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */