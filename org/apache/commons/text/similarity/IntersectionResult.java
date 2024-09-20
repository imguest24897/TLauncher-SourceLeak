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
/*     */ public class IntersectionResult
/*     */ {
/*     */   private final int sizeA;
/*     */   private final int sizeB;
/*     */   private final int intersection;
/*     */   
/*     */   public IntersectionResult(int sizeA, int sizeB, int intersection) {
/*  59 */     if (sizeA < 0) {
/*  60 */       throw new IllegalArgumentException("Set size |A| is not positive: " + sizeA);
/*     */     }
/*  62 */     if (sizeB < 0) {
/*  63 */       throw new IllegalArgumentException("Set size |B| is not positive: " + sizeB);
/*     */     }
/*  65 */     if (intersection < 0 || intersection > Math.min(sizeA, sizeB)) {
/*  66 */       throw new IllegalArgumentException("Invalid intersection of A and B: " + intersection);
/*     */     }
/*  68 */     this.sizeA = sizeA;
/*  69 */     this.sizeB = sizeB;
/*  70 */     this.intersection = intersection;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  75 */     if (this == o) {
/*  76 */       return true;
/*     */     }
/*  78 */     if (o == null || getClass() != o.getClass()) {
/*  79 */       return false;
/*     */     }
/*  81 */     IntersectionResult result = (IntersectionResult)o;
/*  82 */     return (this.sizeA == result.sizeA && this.sizeB == result.sizeB && this.intersection == result.intersection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIntersection() {
/*  91 */     return this.intersection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSizeA() {
/* 100 */     return this.sizeA;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSizeB() {
/* 109 */     return this.sizeB;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 114 */     return Objects.hash(new Object[] { Integer.valueOf(this.sizeA), Integer.valueOf(this.sizeB), Integer.valueOf(this.intersection) });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 119 */     return "Size A: " + this.sizeA + ", Size B: " + this.sizeB + ", Intersection: " + this.intersection;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\similarity\IntersectionResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */