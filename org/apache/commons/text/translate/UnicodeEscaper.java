/*     */ package org.apache.commons.text.translate;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
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
/*     */ public class UnicodeEscaper
/*     */   extends CodePointTranslator
/*     */ {
/*     */   private final int below;
/*     */   private final int above;
/*     */   private final boolean between;
/*     */   
/*     */   public static UnicodeEscaper above(int codePoint) {
/*  36 */     return outsideOf(0, codePoint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnicodeEscaper below(int codePoint) {
/*  45 */     return outsideOf(codePoint, 2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnicodeEscaper between(int codePointLow, int codePointHigh) {
/*  55 */     return new UnicodeEscaper(codePointLow, codePointHigh, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnicodeEscaper outsideOf(int codePointLow, int codePointHigh) {
/*  66 */     return new UnicodeEscaper(codePointLow, codePointHigh, false);
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
/*     */   public UnicodeEscaper() {
/*  83 */     this(0, 2147483647, true);
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
/*     */   protected UnicodeEscaper(int below, int above, boolean between) {
/*  97 */     this.below = below;
/*  98 */     this.above = above;
/*  99 */     this.between = between;
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
/*     */   protected String toUtf16Escape(int codePoint) {
/* 111 */     return "\\u" + hex(codePoint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean translate(int codePoint, Writer writer) throws IOException {
/* 119 */     if (this.between) {
/* 120 */       if (codePoint < this.below || codePoint > this.above) {
/* 121 */         return false;
/*     */       }
/* 123 */     } else if (codePoint >= this.below && codePoint <= this.above) {
/* 124 */       return false;
/*     */     } 
/*     */     
/* 127 */     if (codePoint > 65535) {
/* 128 */       writer.write(toUtf16Escape(codePoint));
/*     */     } else {
/* 130 */       writer.write("\\u");
/* 131 */       writer.write(HEX_DIGITS[codePoint >> 12 & 0xF]);
/* 132 */       writer.write(HEX_DIGITS[codePoint >> 8 & 0xF]);
/* 133 */       writer.write(HEX_DIGITS[codePoint >> 4 & 0xF]);
/* 134 */       writer.write(HEX_DIGITS[codePoint & 0xF]);
/*     */     } 
/* 136 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\translate\UnicodeEscaper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */