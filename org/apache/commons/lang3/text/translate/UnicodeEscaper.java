/*     */ package org.apache.commons.lang3.text.translate;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class UnicodeEscaper
/*     */   extends CodePointTranslator
/*     */ {
/*     */   private final int below;
/*     */   private final int above;
/*     */   private final boolean between;
/*     */   
/*     */   public UnicodeEscaper() {
/*  41 */     this(0, 2147483647, true);
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
/*  55 */     this.below = below;
/*  56 */     this.above = above;
/*  57 */     this.between = between;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnicodeEscaper below(int codePoint) {
/*  67 */     return outsideOf(codePoint, 2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnicodeEscaper above(int codePoint) {
/*  77 */     return outsideOf(0, codePoint);
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
/*  88 */     return new UnicodeEscaper(codePointLow, codePointHigh, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnicodeEscaper between(int codePointLow, int codePointHigh) {
/*  99 */     return new UnicodeEscaper(codePointLow, codePointHigh, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean translate(int codePoint, Writer out) throws IOException {
/* 107 */     if (this.between) {
/* 108 */       if (codePoint < this.below || codePoint > this.above) {
/* 109 */         return false;
/*     */       }
/* 111 */     } else if (codePoint >= this.below && codePoint <= this.above) {
/* 112 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 116 */     if (codePoint > 65535) {
/* 117 */       out.write(toUtf16Escape(codePoint));
/*     */     } else {
/* 119 */       out.write("\\u");
/* 120 */       out.write(HEX_DIGITS[codePoint >> 12 & 0xF]);
/* 121 */       out.write(HEX_DIGITS[codePoint >> 8 & 0xF]);
/* 122 */       out.write(HEX_DIGITS[codePoint >> 4 & 0xF]);
/* 123 */       out.write(HEX_DIGITS[codePoint & 0xF]);
/*     */     } 
/* 125 */     return true;
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
/*     */   protected String toUtf16Escape(int codePoint) {
/* 138 */     return "\\u" + hex(codePoint);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\text\translate\UnicodeEscaper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */