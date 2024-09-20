/*     */ package org.apache.commons.text.similarity;
/*     */ 
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
/*     */ public class LevenshteinResults
/*     */ {
/*     */   private final Integer distance;
/*     */   private final Integer insertCount;
/*     */   private final Integer deleteCount;
/*     */   private final Integer substituteCount;
/*     */   
/*     */   public LevenshteinResults(Integer distance, Integer insertCount, Integer deleteCount, Integer substituteCount) {
/*  63 */     this.distance = distance;
/*  64 */     this.insertCount = insertCount;
/*  65 */     this.deleteCount = deleteCount;
/*  66 */     this.substituteCount = substituteCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  71 */     if (this == o) {
/*  72 */       return true;
/*     */     }
/*  74 */     if (o == null || getClass() != o.getClass()) {
/*  75 */       return false;
/*     */     }
/*  77 */     LevenshteinResults result = (LevenshteinResults)o;
/*  78 */     return (Objects.equals(this.distance, result.distance) && Objects.equals(this.insertCount, result.insertCount) && 
/*  79 */       Objects.equals(this.deleteCount, result.deleteCount) && 
/*  80 */       Objects.equals(this.substituteCount, result.substituteCount));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getDeleteCount() {
/*  89 */     return this.deleteCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getDistance() {
/*  98 */     return this.distance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getInsertCount() {
/* 107 */     return this.insertCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getSubstituteCount() {
/* 116 */     return this.substituteCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 121 */     return Objects.hash(new Object[] { this.distance, this.insertCount, this.deleteCount, this.substituteCount });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 126 */     return "Distance: " + this.distance + ", Insert: " + this.insertCount + ", Delete: " + this.deleteCount + ", Substitute: " + this.substituteCount;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\similarity\LevenshteinResults.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */