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
/*    */ @Deprecated
/*    */ public class UnicodeUnpairedSurrogateRemover
/*    */   extends CodePointTranslator
/*    */ {
/*    */   public boolean translate(int codePoint, Writer out) throws IOException {
/* 38 */     return (codePoint >= 55296 && codePoint <= 57343);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\text\translate\UnicodeUnpairedSurrogateRemover.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */