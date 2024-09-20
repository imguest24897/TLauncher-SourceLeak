/*    */ package by.gdev.updater;
/*    */ 
/*    */ import by.gdev.util.CoreUtil;
/*    */ import by.gdev.util.DesktopUtil;
/*    */ import by.gdev.util.OSInfo;
/*    */ import java.awt.Color;
/*    */ import java.awt.Insets;
/*    */ import java.net.URL;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.JEditorPane;
/*    */ import javax.swing.JScrollPane;
/*    */ import javax.swing.event.HyperlinkEvent;
/*    */ import javax.swing.text.html.HTMLEditorKit;
/*    */ import javax.swing.text.html.StyleSheet;
/*    */ 
/*    */ 
/*    */ public class HtmlTextPane
/*    */   extends JEditorPane
/*    */ {
/* 20 */   private static final HtmlTextPane HTML_TEXT_PANE = new HtmlTextPane("text/html", "");
/* 21 */   private static final HtmlTextPane HTML_TEXT_PANE_WIDTH = new HtmlTextPane("text/html", "");
/*    */   
/*    */   public HtmlTextPane(String type, String text) {
/* 24 */     super(type, text);
/* 25 */     getDocument().putProperty("IgnoreCharsetDirective", Boolean.TRUE);
/* 26 */     setMargin(new Insets(0, 0, 0, 0));
/* 27 */     setForeground(Color.BLACK);
/* 28 */     setEditable(false);
/* 29 */     setOpaque(false);
/* 30 */     HTMLEditorKit kit = new HTMLEditorKit();
/* 31 */     setEditorKit(kit);
/* 32 */     addHyperlinkListener(e -> {
/*    */           if (!e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
/*    */             return;
/*    */           }
/*    */           URL url = e.getURL();
/*    */           if (url == null)
/*    */             return; 
/*    */           DesktopUtil.openLink(OSInfo.getOSType(), url.toString());
/*    */         });
/*    */   }
/*    */   
/*    */   public static HtmlTextPane get(String text) {
/* 44 */     HTML_TEXT_PANE.setText(text);
/* 45 */     return HTML_TEXT_PANE;
/*    */   }
/*    */   
/*    */   public static HtmlTextPane get(String text, int width) {
/* 49 */     HTMLEditorKit kit = (HTMLEditorKit)HTML_TEXT_PANE_WIDTH.getEditorKit();
/* 50 */     kit.getStyleSheet().addRule(String.format("body {width:%spx;}", new Object[] { Integer.valueOf(width) }));
/* 51 */     kit.getStyleSheet().addRule("a { text-decoration: underline; color: #147de0;}");
/* 52 */     HTML_TEXT_PANE_WIDTH.setText(text);
/* 53 */     return HTML_TEXT_PANE_WIDTH;
/*    */   }
/*    */   
/*    */   public static HtmlTextPane getWithWidth(String text, int width) {
/* 57 */     HtmlTextPane h = new HtmlTextPane("text/html", "");
/* 58 */     HTMLEditorKit kit = (HTMLEditorKit)h.getEditorKit();
/* 59 */     kit.getStyleSheet().addRule(String.format("body {width:%spx;}", new Object[] { Integer.valueOf(width) }));
/* 60 */     kit.getStyleSheet().addRule("a { text-decoration: underline; color: #147de0;}");
/* 61 */     h.setText(text);
/* 62 */     return h;
/*    */   }
/*    */ 
/*    */   
/*    */   public static HtmlTextPane createNew(String text, int width) {
/* 67 */     HtmlTextPane pane = new HtmlTextPane("text/html", "");
/* 68 */     pane.setText(text);
/* 69 */     HTMLEditorKit kit = (HTMLEditorKit)pane.getEditorKit();
/* 70 */     StyleSheet ss = new StyleSheet();
/* 71 */     ss.importStyleSheet(CoreUtil.class.getResource("updater.css"));
/* 72 */     kit.getStyleSheet().addStyleSheet(ss);
/* 73 */     return pane;
/*    */   }
/*    */ 
/*    */   
/*    */   public static JScrollPane createNew1(String text, int width) {
/* 78 */     HtmlTextPane pane = new HtmlTextPane("text/html", "");
/* 79 */     pane.setText(text);
/* 80 */     HTMLEditorKit kit = (HTMLEditorKit)pane.getEditorKit();
/* 81 */     StyleSheet ss = new StyleSheet();
/* 82 */     kit.getStyleSheet().addStyleSheet(ss);
/* 83 */     return wrap(pane);
/*    */   }
/*    */   
/*    */   public static JScrollPane createNewAndWrap(String text, int width) {
/* 87 */     return wrap(createNew(text, width));
/*    */   }
/*    */   
/*    */   private static JScrollPane wrap(HtmlTextPane pane) {
/* 91 */     JScrollPane jScrollPane = new JScrollPane(pane, 21, 31);
/*    */     
/* 93 */     jScrollPane.getViewport().setOpaque(false);
/* 94 */     jScrollPane.setOpaque(false);
/* 95 */     jScrollPane.setBorder(BorderFactory.createEmptyBorder());
/* 96 */     return jScrollPane;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gde\\updater\HtmlTextPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */