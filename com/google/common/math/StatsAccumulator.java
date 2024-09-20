/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Doubles;
/*     */ import java.util.Iterator;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.DoubleStream;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
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
/*     */ public final class StatsAccumulator
/*     */ {
/*  44 */   private long count = 0L;
/*  45 */   private double mean = 0.0D;
/*  46 */   private double sumOfSquaresOfDeltas = 0.0D;
/*  47 */   private double min = Double.NaN;
/*  48 */   private double max = Double.NaN;
/*     */ 
/*     */   
/*     */   public void add(double value) {
/*  52 */     if (this.count == 0L) {
/*  53 */       this.count = 1L;
/*  54 */       this.mean = value;
/*  55 */       this.min = value;
/*  56 */       this.max = value;
/*  57 */       if (!Doubles.isFinite(value)) {
/*  58 */         this.sumOfSquaresOfDeltas = Double.NaN;
/*     */       }
/*     */     } else {
/*  61 */       this.count++;
/*  62 */       if (Doubles.isFinite(value) && Doubles.isFinite(this.mean)) {
/*     */         
/*  64 */         double delta = value - this.mean;
/*  65 */         this.mean += delta / this.count;
/*  66 */         this.sumOfSquaresOfDeltas += delta * (value - this.mean);
/*     */       } else {
/*  68 */         this.mean = calculateNewMeanNonFinite(this.mean, value);
/*  69 */         this.sumOfSquaresOfDeltas = Double.NaN;
/*     */       } 
/*  71 */       this.min = Math.min(this.min, value);
/*  72 */       this.max = Math.max(this.max, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(Iterable<? extends Number> values) {
/*  83 */     for (Number value : values) {
/*  84 */       add(value.doubleValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(Iterator<? extends Number> values) {
/*  95 */     while (values.hasNext()) {
/*  96 */       add(((Number)values.next()).doubleValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(double... values) {
/* 106 */     for (double value : values) {
/* 107 */       add(value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(int... values) {
/* 117 */     for (int value : values) {
/* 118 */       add(value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(long... values) {
/* 129 */     for (long value : values) {
/* 130 */       add(value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(DoubleStream values) {
/* 141 */     addAll(values.<StatsAccumulator>collect(StatsAccumulator::new, StatsAccumulator::add, StatsAccumulator::addAll));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(IntStream values) {
/* 151 */     addAll(values.<StatsAccumulator>collect(StatsAccumulator::new, StatsAccumulator::add, StatsAccumulator::addAll));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(LongStream values) {
/* 162 */     addAll(values.<StatsAccumulator>collect(StatsAccumulator::new, StatsAccumulator::add, StatsAccumulator::addAll));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(Stats values) {
/* 170 */     if (values.count() == 0L) {
/*     */       return;
/*     */     }
/* 173 */     merge(values.count(), values.mean(), values.sumOfSquaresOfDeltas(), values.min(), values.max());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(StatsAccumulator values) {
/* 183 */     if (values.count() == 0L) {
/*     */       return;
/*     */     }
/* 186 */     merge(values.count(), values.mean(), values.sumOfSquaresOfDeltas(), values.min(), values.max());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void merge(long otherCount, double otherMean, double otherSumOfSquaresOfDeltas, double otherMin, double otherMax) {
/* 195 */     if (this.count == 0L) {
/* 196 */       this.count = otherCount;
/* 197 */       this.mean = otherMean;
/* 198 */       this.sumOfSquaresOfDeltas = otherSumOfSquaresOfDeltas;
/* 199 */       this.min = otherMin;
/* 200 */       this.max = otherMax;
/*     */     } else {
/* 202 */       this.count += otherCount;
/* 203 */       if (Doubles.isFinite(this.mean) && Doubles.isFinite(otherMean)) {
/*     */         
/* 205 */         double delta = otherMean - this.mean;
/* 206 */         this.mean += delta * otherCount / this.count;
/* 207 */         this.sumOfSquaresOfDeltas += otherSumOfSquaresOfDeltas + delta * (otherMean - this.mean) * otherCount;
/*     */       } else {
/* 209 */         this.mean = calculateNewMeanNonFinite(this.mean, otherMean);
/* 210 */         this.sumOfSquaresOfDeltas = Double.NaN;
/*     */       } 
/* 212 */       this.min = Math.min(this.min, otherMin);
/* 213 */       this.max = Math.max(this.max, otherMax);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Stats snapshot() {
/* 219 */     return new Stats(this.count, this.mean, this.sumOfSquaresOfDeltas, this.min, this.max);
/*     */   }
/*     */ 
/*     */   
/*     */   public long count() {
/* 224 */     return this.count;
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
/*     */   public double mean() {
/* 246 */     Preconditions.checkState((this.count != 0L));
/* 247 */     return this.mean;
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
/*     */   public final double sum() {
/* 263 */     return this.mean * this.count;
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
/*     */   public final double populationVariance() {
/* 282 */     Preconditions.checkState((this.count != 0L));
/* 283 */     if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
/* 284 */       return Double.NaN;
/*     */     }
/* 286 */     if (this.count == 1L) {
/* 287 */       return 0.0D;
/*     */     }
/* 289 */     return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / this.count;
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
/*     */   public final double populationStandardDeviation() {
/* 309 */     return Math.sqrt(populationVariance());
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
/*     */   public final double sampleVariance() {
/* 329 */     Preconditions.checkState((this.count > 1L));
/* 330 */     if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
/* 331 */       return Double.NaN;
/*     */     }
/* 333 */     return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / (this.count - 1L);
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
/*     */   public final double sampleStandardDeviation() {
/* 355 */     return Math.sqrt(sampleVariance());
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
/*     */   public double min() {
/* 372 */     Preconditions.checkState((this.count != 0L));
/* 373 */     return this.min;
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
/*     */   public double max() {
/* 390 */     Preconditions.checkState((this.count != 0L));
/* 391 */     return this.max;
/*     */   }
/*     */   
/*     */   double sumOfSquaresOfDeltas() {
/* 395 */     return this.sumOfSquaresOfDeltas;
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
/*     */   static double calculateNewMeanNonFinite(double previousMean, double value) {
/* 417 */     if (Doubles.isFinite(previousMean))
/*     */     {
/* 419 */       return value; } 
/* 420 */     if (Doubles.isFinite(value) || previousMean == value)
/*     */     {
/* 422 */       return previousMean;
/*     */     }
/*     */     
/* 425 */     return Double.NaN;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\math\StatsAccumulator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */