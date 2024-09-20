/*     */ package org.apache.commons.text.translate;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import org.apache.commons.lang3.Range;
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
/*     */ public class NumericEntityEscaper
/*     */   extends CodePointTranslator
/*     */ {
/*     */   private final boolean between;
/*     */   private final Range<Integer> range;
/*     */   
/*     */   public static NumericEntityEscaper above(int codePoint) {
/*  38 */     return outsideOf(0, codePoint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NumericEntityEscaper below(int codePoint) {
/*  48 */     return outsideOf(codePoint, 2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NumericEntityEscaper between(int codePointLow, int codePointHigh) {
/*  59 */     return new NumericEntityEscaper(codePointLow, codePointHigh, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NumericEntityEscaper outsideOf(int codePointLow, int codePointHigh) {
/*  70 */     return new NumericEntityEscaper(codePointLow, codePointHigh, false);
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
/*     */   public NumericEntityEscaper() {
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
/*     */   private NumericEntityEscaper(int below, int above, boolean between) {
/*  97 */     this.range = Range.of(Integer.valueOf(below), Integer.valueOf(above));
/*  98 */     this.between = between;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean translate(int codePoint, Writer writer) throws IOException {
/* 106 */     if (this.between != this.range.contains(Integer.valueOf(codePoint))) {
/* 107 */       return false;
/*     */     }
/* 109 */     writer.write("&#");
/* 110 */     writer.write(Integer.toString(codePoint, 10));
/* 111 */     writer.write(59);
/* 112 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\translate\NumericEntityEscaper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */