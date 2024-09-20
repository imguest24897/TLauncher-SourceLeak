/*     */ package org.apache.commons.text.translate;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.security.InvalidParameterException;
/*     */ import java.util.BitSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class LookupTranslator
/*     */   extends CharSequenceTranslator
/*     */ {
/*     */   private final Map<String, String> lookupMap;
/*     */   private final BitSet prefixSet;
/*     */   private final int shortest;
/*     */   private final int longest;
/*     */   
/*     */   public LookupTranslator(Map<CharSequence, CharSequence> lookupMap) {
/*  57 */     if (lookupMap == null) {
/*  58 */       throw new InvalidParameterException("lookupMap cannot be null");
/*     */     }
/*  60 */     this.lookupMap = new HashMap<>();
/*  61 */     this.prefixSet = new BitSet();
/*  62 */     int currentShortest = Integer.MAX_VALUE;
/*  63 */     int currentLongest = 0;
/*     */     
/*  65 */     for (Map.Entry<CharSequence, CharSequence> pair : lookupMap.entrySet()) {
/*  66 */       this.lookupMap.put(((CharSequence)pair.getKey()).toString(), ((CharSequence)pair.getValue()).toString());
/*  67 */       this.prefixSet.set(((CharSequence)pair.getKey()).charAt(0));
/*  68 */       int sz = ((CharSequence)pair.getKey()).length();
/*  69 */       if (sz < currentShortest) {
/*  70 */         currentShortest = sz;
/*     */       }
/*  72 */       if (sz > currentLongest) {
/*  73 */         currentLongest = sz;
/*     */       }
/*     */     } 
/*  76 */     this.shortest = currentShortest;
/*  77 */     this.longest = currentLongest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int translate(CharSequence input, int index, Writer writer) throws IOException {
/*  86 */     if (this.prefixSet.get(input.charAt(index))) {
/*  87 */       int max = this.longest;
/*  88 */       if (index + this.longest > input.length()) {
/*  89 */         max = input.length() - index;
/*     */       }
/*     */       
/*  92 */       for (int i = max; i >= this.shortest; i--) {
/*  93 */         CharSequence subSeq = input.subSequence(index, index + i);
/*  94 */         String result = this.lookupMap.get(subSeq.toString());
/*     */         
/*  96 */         if (result != null) {
/*  97 */           writer.write(result);
/*  98 */           return Character.codePointCount(subSeq, 0, subSeq.length());
/*     */         } 
/*     */       } 
/*     */     } 
/* 102 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\translate\LookupTranslator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */