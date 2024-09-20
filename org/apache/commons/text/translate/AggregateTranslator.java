/*    */ package org.apache.commons.text.translate;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
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
/*    */ public class AggregateTranslator
/*    */   extends CharSequenceTranslator
/*    */ {
/* 37 */   private final List<CharSequenceTranslator> translators = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AggregateTranslator(CharSequenceTranslator... translators) {
/* 45 */     if (translators != null) {
/* 46 */       Objects.requireNonNull(this.translators); Stream.<CharSequenceTranslator>of(translators).filter(Objects::nonNull).forEach(this.translators::add);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int translate(CharSequence input, int index, Writer writer) throws IOException {
/* 57 */     for (CharSequenceTranslator translator : this.translators) {
/* 58 */       int consumed = translator.translate(input, index, writer);
/* 59 */       if (consumed != 0) {
/* 60 */         return consumed;
/*    */       }
/*    */     } 
/* 63 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\translate\AggregateTranslator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */