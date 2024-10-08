/*    */ package org.apache.commons.lang3.text.translate;
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
/*    */ @Deprecated
/*    */ public abstract class CodePointTranslator
/*    */   extends CharSequenceTranslator
/*    */ {
/*    */   public final int translate(CharSequence input, int index, Writer out) throws IOException {
/* 40 */     int codePoint = Character.codePointAt(input, index);
/* 41 */     boolean consumed = translate(codePoint, out);
/* 42 */     return consumed ? 1 : 0;
/*    */   }
/*    */   
/*    */   public abstract boolean translate(int paramInt, Writer paramWriter) throws IOException;
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\text\translate\CodePointTranslator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */