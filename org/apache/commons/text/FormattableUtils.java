/*     */ package org.apache.commons.text;
/*     */ 
/*     */ import java.util.Formattable;
/*     */ import java.util.Formatter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FormattableUtils
/*     */ {
/*     */   private static final String SIMPLEST_FORMAT = "%s";
/*     */   
/*     */   public static Formatter append(CharSequence seq, Formatter formatter, int flags, int width, int precision) {
/*  56 */     return append(seq, formatter, flags, width, precision, ' ', null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Formatter append(CharSequence seq, Formatter formatter, int flags, int width, int precision, char padChar) {
/*  73 */     return append(seq, formatter, flags, width, precision, padChar, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Formatter append(CharSequence seq, Formatter formatter, int flags, int width, int precision, char padChar, CharSequence truncateEllipsis) {
/*  93 */     if (truncateEllipsis != null && precision >= 0 && truncateEllipsis.length() > precision)
/*  94 */       throw new IllegalArgumentException(
/*  95 */           String.format("Specified ellipsis '%s' exceeds precision of %s", new Object[] {
/*     */               
/*  97 */               truncateEllipsis, Integer.valueOf(precision)
/*     */             })); 
/*  99 */     StringBuilder buf = new StringBuilder(seq);
/* 100 */     if (precision >= 0 && precision < seq.length()) {
/*     */       CharSequence ellipsis;
/* 102 */       if (truncateEllipsis == null) {
/* 103 */         ellipsis = "";
/*     */       } else {
/* 105 */         ellipsis = truncateEllipsis;
/*     */       } 
/* 107 */       buf.replace(precision - ellipsis.length(), seq.length(), ellipsis.toString());
/*     */     } 
/* 109 */     boolean leftJustify = ((flags & 0x1) == 1);
/* 110 */     for (int i = buf.length(); i < width; i++) {
/* 111 */       buf.insert(leftJustify ? i : 0, padChar);
/*     */     }
/* 113 */     formatter.format(buf.toString(), new Object[0]);
/* 114 */     return formatter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Formatter append(CharSequence seq, Formatter formatter, int flags, int width, int precision, CharSequence ellipsis) {
/* 134 */     return append(seq, formatter, flags, width, precision, ' ', ellipsis);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(Formattable formattable) {
/* 145 */     return String.format("%s", new Object[] { formattable });
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\FormattableUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */