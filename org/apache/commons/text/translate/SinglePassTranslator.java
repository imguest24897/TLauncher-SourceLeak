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
/*    */ abstract class SinglePassTranslator
/*    */   extends CharSequenceTranslator
/*    */ {
/*    */   private String getClassName() {
/* 34 */     Class<? extends SinglePassTranslator> clazz = (Class)getClass();
/* 35 */     return clazz.isAnonymousClass() ? clazz.getName() : clazz.getSimpleName();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int translate(CharSequence input, int index, Writer writer) throws IOException {
/* 44 */     if (index != 0) {
/* 45 */       throw new IllegalArgumentException(getClassName() + ".translate(final CharSequence input, final int index, final Writer out) can not handle a non-zero index.");
/*    */     }
/*    */ 
/*    */     
/* 49 */     translateWhole(input, writer);
/*    */     
/* 51 */     return Character.codePointCount(input, index, input.length());
/*    */   }
/*    */   
/*    */   abstract void translateWhole(CharSequence paramCharSequence, Writer paramWriter) throws IOException;
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\translate\SinglePassTranslator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */