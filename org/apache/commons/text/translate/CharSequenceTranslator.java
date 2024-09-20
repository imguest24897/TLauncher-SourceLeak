/*     */ package org.apache.commons.text.translate;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringWriter;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.io.Writer;
/*     */ import java.util.Locale;
/*     */ import org.apache.commons.lang3.Validate;
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
/*     */ public abstract class CharSequenceTranslator
/*     */ {
/*  39 */   static final char[] HEX_DIGITS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String hex(int codePoint) {
/*  49 */     return Integer.toHexString(codePoint).toUpperCase(Locale.ENGLISH);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String translate(CharSequence input) {
/*  58 */     if (input == null) {
/*  59 */       return null;
/*     */     }
/*     */     try {
/*  62 */       StringWriter writer = new StringWriter(input.length() * 2);
/*  63 */       translate(input, writer);
/*  64 */       return writer.toString();
/*  65 */     } catch (IOException ioe) {
/*     */       
/*  67 */       throw new UncheckedIOException(ioe);
/*     */     } 
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
/*     */   public abstract int translate(CharSequence paramCharSequence, int paramInt, Writer paramWriter) throws IOException;
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
/*     */   public final void translate(CharSequence input, Writer writer) throws IOException {
/*  94 */     Validate.isTrue((writer != null), "The Writer must not be null", new Object[0]);
/*  95 */     if (input == null) {
/*     */       return;
/*     */     }
/*  98 */     int pos = 0;
/*  99 */     int len = input.length();
/* 100 */     while (pos < len) {
/* 101 */       int consumed = translate(input, pos, writer);
/* 102 */       if (consumed == 0) {
/*     */ 
/*     */         
/* 105 */         char c1 = input.charAt(pos);
/* 106 */         writer.write(c1);
/* 107 */         pos++;
/* 108 */         if (Character.isHighSurrogate(c1) && pos < len) {
/* 109 */           char c2 = input.charAt(pos);
/* 110 */           if (Character.isLowSurrogate(c2)) {
/* 111 */             writer.write(c2);
/* 112 */             pos++;
/*     */           } 
/*     */         } 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 119 */       for (int pt = 0; pt < consumed; pt++) {
/* 120 */         pos += Character.charCount(Character.codePointAt(input, pos));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final CharSequenceTranslator with(CharSequenceTranslator... translators) {
/* 133 */     CharSequenceTranslator[] newArray = new CharSequenceTranslator[translators.length + 1];
/* 134 */     newArray[0] = this;
/* 135 */     System.arraycopy(translators, 0, newArray, 1, translators.length);
/* 136 */     return new AggregateTranslator(newArray);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\translate\CharSequenceTranslator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */