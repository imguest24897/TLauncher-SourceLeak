/*    */ package org.apache.commons.text.translate;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnicodeUnescaper
/*    */   extends CharSequenceTranslator
/*    */ {
/*    */   public int translate(CharSequence input, int index, Writer writer) throws IOException {
/* 36 */     if (input.charAt(index) == '\\' && index + 1 < input.length() && input.charAt(index + 1) == 'u') {
/*    */       
/* 38 */       int i = 2;
/* 39 */       while (index + i < input.length() && input.charAt(index + i) == 'u') {
/* 40 */         i++;
/*    */       }
/*    */       
/* 43 */       if (index + i < input.length() && input.charAt(index + i) == '+') {
/* 44 */         i++;
/*    */       }
/*    */       
/* 47 */       if (index + i + 4 <= input.length()) {
/*    */         
/* 49 */         CharSequence unicode = input.subSequence(index + i, index + i + 4);
/*    */         
/*    */         try {
/* 52 */           int value = Integer.parseInt(unicode.toString(), 16);
/* 53 */           writer.write((char)value);
/* 54 */         } catch (NumberFormatException nfe) {
/* 55 */           throw new IllegalArgumentException("Unable to parse unicode value: " + unicode, nfe);
/*    */         } 
/* 57 */         return i + 4;
/*    */       } 
/* 59 */       throw new IllegalArgumentException("Less than 4 hex digits in unicode value: '" + input
/* 60 */           .subSequence(index, input.length()) + "' due to end of CharSequence");
/*    */     } 
/*    */     
/* 63 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\translate\UnicodeUnescaper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */