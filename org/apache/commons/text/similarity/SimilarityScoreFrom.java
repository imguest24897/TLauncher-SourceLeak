/*     */ package org.apache.commons.text.similarity;
/*     */ 
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
/*     */ public class SimilarityScoreFrom<R>
/*     */ {
/*     */   private final SimilarityScore<R> similarityScore;
/*     */   private final CharSequence left;
/*     */   
/*     */   public SimilarityScoreFrom(SimilarityScore<R> similarityScore, CharSequence left) {
/*  74 */     Validate.isTrue((similarityScore != null), "The edit distance may not be null.", new Object[0]);
/*     */     
/*  76 */     this.similarityScore = similarityScore;
/*  77 */     this.left = left;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public R apply(CharSequence right) {
/*  88 */     return this.similarityScore.apply(this.left, right);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharSequence getLeft() {
/*  97 */     return this.left;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimilarityScore<R> getSimilarityScore() {
/* 106 */     return this.similarityScore;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\similarity\SimilarityScoreFrom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */