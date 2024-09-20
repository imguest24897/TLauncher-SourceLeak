/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Doubles;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class PairedStatsAccumulator
/*     */ {
/*  39 */   private final StatsAccumulator xStats = new StatsAccumulator();
/*  40 */   private final StatsAccumulator yStats = new StatsAccumulator();
/*  41 */   private double sumOfProductsOfDeltas = 0.0D;
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
/*     */   public void add(double x, double y) {
/*  56 */     this.xStats.add(x);
/*  57 */     if (Doubles.isFinite(x) && Doubles.isFinite(y)) {
/*  58 */       if (this.xStats.count() > 1L) {
/*  59 */         this.sumOfProductsOfDeltas += (x - this.xStats.mean()) * (y - this.yStats.mean());
/*     */       }
/*     */     } else {
/*  62 */       this.sumOfProductsOfDeltas = Double.NaN;
/*     */     } 
/*  64 */     this.yStats.add(y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(PairedStats values) {
/*  72 */     if (values.count() == 0L) {
/*     */       return;
/*     */     }
/*     */     
/*  76 */     this.xStats.addAll(values.xStats());
/*  77 */     if (this.yStats.count() == 0L) {
/*  78 */       this.sumOfProductsOfDeltas = values.sumOfProductsOfDeltas();
/*     */     
/*     */     }
/*     */     else {
/*     */       
/*  83 */       this.sumOfProductsOfDeltas += values
/*  84 */         .sumOfProductsOfDeltas() + (values
/*  85 */         .xStats().mean() - this.xStats.mean()) * (values
/*  86 */         .yStats().mean() - this.yStats.mean()) * values
/*  87 */         .count();
/*     */     } 
/*  89 */     this.yStats.addAll(values.yStats());
/*     */   }
/*     */ 
/*     */   
/*     */   public PairedStats snapshot() {
/*  94 */     return new PairedStats(this.xStats.snapshot(), this.yStats.snapshot(), this.sumOfProductsOfDeltas);
/*     */   }
/*     */ 
/*     */   
/*     */   public long count() {
/*  99 */     return this.xStats.count();
/*     */   }
/*     */ 
/*     */   
/*     */   public Stats xStats() {
/* 104 */     return this.xStats.snapshot();
/*     */   }
/*     */ 
/*     */   
/*     */   public Stats yStats() {
/* 109 */     return this.yStats.snapshot();
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
/*     */ 
/*     */ 
/*     */   
/*     */   public double populationCovariance() {
/* 127 */     Preconditions.checkState((count() != 0L));
/* 128 */     return this.sumOfProductsOfDeltas / count();
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
/*     */ 
/*     */   
/*     */   public final double sampleCovariance() {
/* 145 */     Preconditions.checkState((count() > 1L));
/* 146 */     return this.sumOfProductsOfDeltas / (count() - 1L);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double pearsonsCorrelationCoefficient() {
/* 166 */     Preconditions.checkState((count() > 1L));
/* 167 */     if (Double.isNaN(this.sumOfProductsOfDeltas)) {
/* 168 */       return Double.NaN;
/*     */     }
/* 170 */     double xSumOfSquaresOfDeltas = this.xStats.sumOfSquaresOfDeltas();
/* 171 */     double ySumOfSquaresOfDeltas = this.yStats.sumOfSquaresOfDeltas();
/* 172 */     Preconditions.checkState((xSumOfSquaresOfDeltas > 0.0D));
/* 173 */     Preconditions.checkState((ySumOfSquaresOfDeltas > 0.0D));
/*     */ 
/*     */ 
/*     */     
/* 177 */     double productOfSumsOfSquaresOfDeltas = ensurePositive(xSumOfSquaresOfDeltas * ySumOfSquaresOfDeltas);
/* 178 */     return ensureInUnitRange(this.sumOfProductsOfDeltas / Math.sqrt(productOfSumsOfSquaresOfDeltas));
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
/*     */   public final LinearTransformation leastSquaresFit() {
/* 213 */     Preconditions.checkState((count() > 1L));
/* 214 */     if (Double.isNaN(this.sumOfProductsOfDeltas)) {
/* 215 */       return LinearTransformation.forNaN();
/*     */     }
/* 217 */     double xSumOfSquaresOfDeltas = this.xStats.sumOfSquaresOfDeltas();
/* 218 */     if (xSumOfSquaresOfDeltas > 0.0D) {
/* 219 */       if (this.yStats.sumOfSquaresOfDeltas() > 0.0D) {
/* 220 */         return LinearTransformation.mapping(this.xStats.mean(), this.yStats.mean())
/* 221 */           .withSlope(this.sumOfProductsOfDeltas / xSumOfSquaresOfDeltas);
/*     */       }
/* 223 */       return LinearTransformation.horizontal(this.yStats.mean());
/*     */     } 
/*     */     
/* 226 */     Preconditions.checkState((this.yStats.sumOfSquaresOfDeltas() > 0.0D));
/* 227 */     return LinearTransformation.vertical(this.xStats.mean());
/*     */   }
/*     */ 
/*     */   
/*     */   private double ensurePositive(double value) {
/* 232 */     if (value > 0.0D) {
/* 233 */       return value;
/*     */     }
/* 235 */     return Double.MIN_VALUE;
/*     */   }
/*     */ 
/*     */   
/*     */   private static double ensureInUnitRange(double value) {
/* 240 */     return Doubles.constrainToRange(value, -1.0D, 1.0D);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\math\PairedStatsAccumulator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */