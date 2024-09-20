/*     */ package by.gdev.updater;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.JPanel;
/*     */ 
/*     */ class AlertPanel extends JPanel {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final int MAX_CHARS = 80;
/*     */   
/*     */   AlertPanel(String rawMessage, Object rawTextarea) {
/*     */     String message;
/*  11 */     setLayout(new BoxLayout(this, 1));
/*     */ 
/*     */ 
/*     */     
/*  15 */     if (rawMessage == null) {
/*  16 */       message = null;
/*     */     } else {
/*  18 */       String b = "<html>" + rawMessage + "</html>";
/*     */       
/*  20 */       message = wrap(b.toCharArray(), 80, true, false);
/*     */     } 
/*     */     
/*  23 */     EditorPane label = new EditorPane("text/html", message);
/*  24 */     label.setAlignmentX(0.0F);
/*  25 */     label.setFocusable(false);
/*  26 */     add(label);
/*     */     
/*  28 */     if (rawTextarea == null)
/*     */       return; 
/*     */   }
/*     */   
/*     */   public static String wrap(char[] s, int maxChars, boolean rudeBreaking, boolean detectHTML) {
/*  33 */     if (s == null)
/*  34 */       throw new NullPointerException("sequence"); 
/*  35 */     if (maxChars < 1) {
/*  36 */       throw new IllegalArgumentException("maxChars < 1");
/*     */     }
/*  38 */     detectHTML = (detectHTML && isHTML(s));
/*     */     
/*  40 */     String lineBreak = detectHTML ? "<br />" : "\n";
/*     */     
/*  42 */     StringBuilder builder = new StringBuilder();
/*     */     
/*  44 */     int len = s.length, remaining = maxChars;
/*  45 */     boolean tagDetecting = false, ignoreCurrent = false;
/*     */ 
/*     */     
/*  48 */     for (int x = 0; x < len; x++) {
/*  49 */       char current = s[x];
/*     */       
/*  51 */       if (current == '<' && detectHTML) {
/*     */         
/*  53 */         tagDetecting = true;
/*  54 */         ignoreCurrent = true;
/*  55 */       } else if (tagDetecting) {
/*     */         
/*  57 */         if (current == '>') {
/*  58 */           tagDetecting = false;
/*     */         }
/*  60 */         ignoreCurrent = true;
/*     */       } 
/*     */       
/*  63 */       if (ignoreCurrent) {
/*  64 */         ignoreCurrent = false;
/*     */         
/*  66 */         builder.append(current);
/*     */       }
/*     */       else {
/*     */         
/*  70 */         remaining--;
/*     */         
/*  72 */         if (s[x] == '\n' || (remaining < 1 && current == ' ')) {
/*  73 */           remaining = maxChars;
/*  74 */           builder.append(lineBreak);
/*     */         } else {
/*  76 */           if (lookForward(s, x, lineBreak)) {
/*  77 */             remaining = maxChars;
/*     */           }
/*     */           
/*  80 */           builder.append(current);
/*     */           
/*  82 */           if (remaining <= 0)
/*     */           {
/*  84 */             if (rudeBreaking)
/*     */             
/*     */             { 
/*  87 */               remaining = maxChars;
/*  88 */               builder.append(lineBreak); }  } 
/*     */         } 
/*     */       } 
/*  91 */     }  return builder.toString();
/*     */   }
/*     */   
/*     */   public static boolean isHTML(char[] s) {
/*  95 */     if (s != null && 
/*  96 */       s.length >= 6 && s[0] == '<' && s[5] == '>') {
/*  97 */       String tag = new String(s, 1, 4);
/*  98 */       return tag.equalsIgnoreCase("html");
/*     */     } 
/*     */     
/* 101 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean lookForward(char[] c, int caret, CharSequence search) {
/* 105 */     if (c == null) {
/* 106 */       throw new NullPointerException("char array");
/*     */     }
/* 108 */     if (caret < 0) {
/* 109 */       throw new IllegalArgumentException("caret < 0");
/*     */     }
/* 111 */     if (caret >= c.length) {
/* 112 */       return false;
/*     */     }
/* 114 */     int length = search.length(), available = c.length - caret;
/*     */     
/* 116 */     if (length < available) {
/* 117 */       return false;
/*     */     }
/* 119 */     for (int i = 0; i < length; i++) {
/* 120 */       if (c[caret + i] != search.charAt(i))
/* 121 */         return false; 
/* 122 */     }  return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gde\\updater\AlertPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */