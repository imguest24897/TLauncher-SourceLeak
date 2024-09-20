/*     */ package org.apache.commons.text.similarity;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class CosineSimilarity
/*     */ {
/*  39 */   static final CosineSimilarity INSTANCE = new CosineSimilarity();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Double cosineSimilarity(Map<CharSequence, Integer> leftVector, Map<CharSequence, Integer> rightVector) {
/*     */     double cosineSimilarity;
/*  50 */     if (leftVector == null || rightVector == null) {
/*  51 */       throw new IllegalArgumentException("Vectors must not be null");
/*     */     }
/*     */     
/*  54 */     Set<CharSequence> intersection = getIntersection(leftVector, rightVector);
/*     */     
/*  56 */     double dotProduct = dot(leftVector, rightVector, intersection);
/*  57 */     double d1 = 0.0D;
/*  58 */     for (Integer value : leftVector.values()) {
/*  59 */       d1 += Math.pow(value.intValue(), 2.0D);
/*     */     }
/*  61 */     double d2 = 0.0D;
/*  62 */     for (Integer value : rightVector.values()) {
/*  63 */       d2 += Math.pow(value.intValue(), 2.0D);
/*     */     }
/*     */     
/*  66 */     if (d1 <= 0.0D || d2 <= 0.0D) {
/*  67 */       cosineSimilarity = 0.0D;
/*     */     } else {
/*  69 */       cosineSimilarity = dotProduct / Math.sqrt(d1) * Math.sqrt(d2);
/*     */     } 
/*  71 */     return Double.valueOf(cosineSimilarity);
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
/*     */   
/*     */   private double dot(Map<CharSequence, Integer> leftVector, Map<CharSequence, Integer> rightVector, Set<CharSequence> intersection) {
/*  86 */     long dotProduct = 0L;
/*  87 */     for (CharSequence key : intersection) {
/*  88 */       dotProduct += ((Integer)leftVector.get(key)).intValue() * ((Integer)rightVector.get(key)).intValue();
/*     */     }
/*  90 */     return dotProduct;
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
/*     */   private Set<CharSequence> getIntersection(Map<CharSequence, Integer> leftVector, Map<CharSequence, Integer> rightVector) {
/* 102 */     Set<CharSequence> intersection = new HashSet<>(leftVector.keySet());
/* 103 */     intersection.retainAll(rightVector.keySet());
/* 104 */     return intersection;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\similarity\CosineSimilarity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */