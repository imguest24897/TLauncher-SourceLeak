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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OctalUnescaper
/*    */   extends CharSequenceTranslator
/*    */ {
/*    */   private boolean isOctalDigit(char ch) {
/* 41 */     return (ch >= '0' && ch <= '7');
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean isZeroToThree(char ch) {
/* 51 */     return (ch >= '0' && ch <= '3');
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int translate(CharSequence input, int index, Writer writer) throws IOException {
/* 59 */     int remaining = input.length() - index - 1;
/* 60 */     StringBuilder builder = new StringBuilder();
/* 61 */     if (input.charAt(index) == '\\' && remaining > 0 && isOctalDigit(input.charAt(index + 1))) {
/* 62 */       int next = index + 1;
/* 63 */       int next2 = index + 2;
/* 64 */       int next3 = index + 3;
/*    */ 
/*    */       
/* 67 */       builder.append(input.charAt(next));
/*    */       
/* 69 */       if (remaining > 1 && isOctalDigit(input.charAt(next2))) {
/* 70 */         builder.append(input.charAt(next2));
/* 71 */         if (remaining > 2 && isZeroToThree(input.charAt(next)) && isOctalDigit(input.charAt(next3))) {
/* 72 */           builder.append(input.charAt(next3));
/*    */         }
/*    */       } 
/*    */       
/* 76 */       writer.write(Integer.parseInt(builder.toString(), 8));
/* 77 */       return 1 + builder.length();
/*    */     } 
/* 79 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\translate\OctalUnescaper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */