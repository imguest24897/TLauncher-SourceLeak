/*     */ package org.apache.commons.lang3.text.translate;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringWriter;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.io.Writer;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
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
/*     */   public final String translate(CharSequence input) {
/*  61 */     if (input == null) {
/*  62 */       return null;
/*     */     }
/*     */     try {
/*  65 */       StringWriter writer = new StringWriter(input.length() * 2);
/*  66 */       translate(input, writer);
/*  67 */       return writer.toString();
/*  68 */     } catch (IOException ioe) {
/*     */       
/*  70 */       throw new UncheckedIOException(ioe);
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
/*     */   public final void translate(CharSequence input, Writer writer) throws IOException {
/*  83 */     Objects.requireNonNull(writer, "writer");
/*  84 */     if (input == null) {
/*     */       return;
/*     */     }
/*  87 */     int pos = 0;
/*  88 */     int len = input.length();
/*  89 */     while (pos < len) {
/*  90 */       int consumed = translate(input, pos, writer);
/*  91 */       if (consumed == 0) {
/*     */ 
/*     */         
/*  94 */         char c1 = input.charAt(pos);
/*  95 */         writer.write(c1);
/*  96 */         pos++;
/*  97 */         if (Character.isHighSurrogate(c1) && pos < len) {
/*  98 */           char c2 = input.charAt(pos);
/*  99 */           if (Character.isLowSurrogate(c2)) {
/* 100 */             writer.write(c2);
/* 101 */             pos++;
/*     */           } 
/*     */         } 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 108 */       for (int pt = 0; pt < consumed; pt++) {
/* 109 */         pos += Character.charCount(Character.codePointAt(input, pos));
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
/* 122 */     CharSequenceTranslator[] newArray = new CharSequenceTranslator[translators.length + 1];
/* 123 */     newArray[0] = this;
/* 124 */     System.arraycopy(translators, 0, newArray, 1, translators.length);
/* 125 */     return new AggregateTranslator(newArray);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String hex(int codePoint) {
/* 136 */     return Integer.toHexString(codePoint).toUpperCase(Locale.ENGLISH);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\text\translate\CharSequenceTranslator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */