/*    */ package by.gdev.updater;
/*    */ 
/*    */ import by.gdev.util.DesktopUtil;
/*    */ import by.gdev.util.OSInfo;
/*    */ import java.awt.Font;
/*    */ import java.awt.Insets;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import javax.swing.JEditorPane;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.event.HyperlinkEvent;
/*    */ import javax.swing.event.HyperlinkListener;
/*    */ import javax.swing.text.html.StyleSheet;
/*    */ 
/*    */ 
/*    */ public class EditorPane
/*    */   extends JEditorPane
/*    */ {
/*    */   private static final long serialVersionUID = -2857352867725574106L;
/*    */   
/*    */   public EditorPane(Font font) {
/* 22 */     if (font != null) {
/* 23 */       setFont(font);
/*    */     } else {
/* 25 */       font = getFont();
/*    */     } 
/* 27 */     StyleSheet css = new StyleSheet();
/* 28 */     css.importStyleSheet(getClass().getResource("styles.css"));
/* 29 */     css.addRule("body { font-family: " + font.getFamily() + "; font-size: " + font
/* 30 */         .getSize() + "pt; } " + "a { text-decoration: underline; }");
/*    */     
/* 32 */     ExtendedHTMLEditorKit html = new ExtendedHTMLEditorKit();
/* 33 */     html.setStyleSheet(css);
/*    */     
/* 35 */     getDocument().putProperty("IgnoreCharsetDirective", Boolean.TRUE);
/* 36 */     setMargin(new Insets(0, 0, 0, 0));
/* 37 */     setEditorKit(html);
/* 38 */     setEditable(false);
/* 39 */     setOpaque(false);
/*    */     
/* 41 */     addHyperlinkListener(new HyperlinkListener()
/*    */         {
/*    */           public void hyperlinkUpdate(HyperlinkEvent e) {
/* 44 */             if (!e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
/*    */               return;
/*    */             }
/* 47 */             URL url = e.getURL();
/*    */             
/* 49 */             if (url == null) {
/*    */               return;
/*    */             }
/* 52 */             DesktopUtil.openLink(OSInfo.getOSType(), url.toString());
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   public EditorPane() {
/* 58 */     this((new JLabel()).getFont());
/*    */   }
/*    */   
/*    */   public EditorPane(URL initialPage) throws IOException {
/* 62 */     this();
/* 63 */     setPage(initialPage);
/*    */   }
/*    */   
/*    */   public EditorPane(String url) throws IOException {
/* 67 */     this();
/* 68 */     setPage(url);
/*    */   }
/*    */   
/*    */   public EditorPane(String type, String text) {
/* 72 */     this();
/* 73 */     setContentType(type);
/* 74 */     setText(text);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gde\\updater\EditorPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */