/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Doubles;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.util.Iterator;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collector;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Stats
/*     */   implements Serializable
/*     */ {
/*     */   private final long count;
/*     */   private final double mean;
/*     */   private final double sumOfSquaresOfDeltas;
/*     */   private final double min;
/*     */   private final double max;
/*     */   static final int BYTES = 40;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   Stats(long count, double mean, double sumOfSquaresOfDeltas, double min, double max) {
/*  90 */     this.count = count;
/*  91 */     this.mean = mean;
/*  92 */     this.sumOfSquaresOfDeltas = sumOfSquaresOfDeltas;
/*  93 */     this.min = min;
/*  94 */     this.max = max;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stats of(Iterable<? extends Number> values) {
/* 104 */     StatsAccumulator accumulator = new StatsAccumulator();
/* 105 */     accumulator.addAll(values);
/* 106 */     return accumulator.snapshot();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stats of(Iterator<? extends Number> values) {
/* 117 */     StatsAccumulator accumulator = new StatsAccumulator();
/* 118 */     accumulator.addAll(values);
/* 119 */     return accumulator.snapshot();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stats of(double... values) {
/* 128 */     StatsAccumulator acummulator = new StatsAccumulator();
/* 129 */     acummulator.addAll(values);
/* 130 */     return acummulator.snapshot();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stats of(int... values) {
/* 139 */     StatsAccumulator acummulator = new StatsAccumulator();
/* 140 */     acummulator.addAll(values);
/* 141 */     return acummulator.snapshot();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stats of(long... values) {
/* 151 */     StatsAccumulator acummulator = new StatsAccumulator();
/* 152 */     acummulator.addAll(values);
/* 153 */     return acummulator.snapshot();
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
/*     */   public static Stats of(DoubleStream values) {
/* 167 */     return ((StatsAccumulator)values
/* 168 */       .<StatsAccumulator>collect(StatsAccumulator::new, StatsAccumulator::add, StatsAccumulator::addAll))
/* 169 */       .snapshot();
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
/*     */   public static Stats of(IntStream values) {
/* 183 */     return ((StatsAccumulator)values
/* 184 */       .<StatsAccumulator>collect(StatsAccumulator::new, StatsAccumulator::add, StatsAccumulator::addAll))
/* 185 */       .snapshot();
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
/*     */   public static Stats of(LongStream values) {
/* 200 */     return ((StatsAccumulator)values
/* 201 */       .<StatsAccumulator>collect(StatsAccumulator::new, StatsAccumulator::add, StatsAccumulator::addAll))
/* 202 */       .snapshot();
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
/*     */   public static Collector<Number, StatsAccumulator, Stats> toStats() {
/* 217 */     return Collector.of(StatsAccumulator::new, (a, x) -> a.add(x.doubleValue()), (l, r) -> { l.addAll(r); return l; }StatsAccumulator::snapshot, new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
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
/*     */   public long count() {
/* 230 */     return this.count;
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
/*     */   public double mean() {
/* 255 */     Preconditions.checkState((this.count != 0L));
/* 256 */     return this.mean;
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
/*     */   public double sum() {
/* 272 */     return this.mean * this.count;
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
/*     */   public double populationVariance() {
/* 291 */     Preconditions.checkState((this.count > 0L));
/* 292 */     if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
/* 293 */       return Double.NaN;
/*     */     }
/* 295 */     if (this.count == 1L) {
/* 296 */       return 0.0D;
/*     */     }
/* 298 */     return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / count();
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
/*     */   public double populationStandardDeviation() {
/* 318 */     return Math.sqrt(populationVariance());
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
/*     */   public double sampleVariance() {
/* 338 */     Preconditions.checkState((this.count > 1L));
/* 339 */     if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
/* 340 */       return Double.NaN;
/*     */     }
/* 342 */     return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / (this.count - 1L);
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
/*     */   public double sampleStandardDeviation() {
/* 364 */     return Math.sqrt(sampleVariance());
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
/* 381 */     Preconditions.checkState((this.count != 0L));
/* 382 */     return this.min;
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
/* 399 */     Preconditions.checkState((this.count != 0L));
/* 400 */     return this.max;
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
/*     */   public boolean equals(Object obj) {
/* 420 */     if (obj == null) {
/* 421 */       return false;
/*     */     }
/* 423 */     if (getClass() != obj.getClass()) {
/* 424 */       return false;
/*     */     }
/* 426 */     Stats other = (Stats)obj;
/* 427 */     return (this.count == other.count && 
/* 428 */       Double.doubleToLongBits(this.mean) == Double.doubleToLongBits(other.mean) && 
/* 429 */       Double.doubleToLongBits(this.sumOfSquaresOfDeltas) == Double.doubleToLongBits(other.sumOfSquaresOfDeltas) && 
/* 430 */       Double.doubleToLongBits(this.min) == Double.doubleToLongBits(other.min) && 
/* 431 */       Double.doubleToLongBits(this.max) == Double.doubleToLongBits(other.max));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 442 */     return Objects.hashCode(new Object[] { Long.valueOf(this.count), Double.valueOf(this.mean), Double.valueOf(this.sumOfSquaresOfDeltas), Double.valueOf(this.min), Double.valueOf(this.max) });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 447 */     if (count() > 0L) {
/* 448 */       return MoreObjects.toStringHelper(this)
/* 449 */         .add("count", this.count)
/* 450 */         .add("mean", this.mean)
/* 451 */         .add("populationStandardDeviation", populationStandardDeviation())
/* 452 */         .add("min", this.min)
/* 453 */         .add("max", this.max)
/* 454 */         .toString();
/*     */     }
/* 456 */     return MoreObjects.toStringHelper(this).add("count", this.count).toString();
/*     */   }
/*     */ 
/*     */   
/*     */   double sumOfSquaresOfDeltas() {
/* 461 */     return this.sumOfSquaresOfDeltas;
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
/*     */   public static double meanOf(Iterable<? extends Number> values) {
/* 475 */     return meanOf(values.iterator());
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
/*     */   public static double meanOf(Iterator<? extends Number> values) {
/* 489 */     Preconditions.checkArgument(values.hasNext());
/* 490 */     long count = 1L;
/* 491 */     double mean = ((Number)values.next()).doubleValue();
/* 492 */     while (values.hasNext()) {
/* 493 */       double value = ((Number)values.next()).doubleValue();
/* 494 */       count++;
/* 495 */       if (Doubles.isFinite(value) && Doubles.isFinite(mean)) {
/*     */         
/* 497 */         mean += (value - mean) / count; continue;
/*     */       } 
/* 499 */       mean = StatsAccumulator.calculateNewMeanNonFinite(mean, value);
/*     */     } 
/*     */     
/* 502 */     return mean;
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
/*     */   public static double meanOf(double... values) {
/* 515 */     Preconditions.checkArgument((values.length > 0));
/* 516 */     double mean = values[0];
/* 517 */     for (int index = 1; index < values.length; index++) {
/* 518 */       double value = values[index];
/* 519 */       if (Doubles.isFinite(value) && Doubles.isFinite(mean)) {
/*     */         
/* 521 */         mean += (value - mean) / (index + 1);
/*     */       } else {
/* 523 */         mean = StatsAccumulator.calculateNewMeanNonFinite(mean, value);
/*     */       } 
/*     */     } 
/* 526 */     return mean;
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
/*     */   public static double meanOf(int... values) {
/* 539 */     Preconditions.checkArgument((values.length > 0));
/* 540 */     double mean = values[0];
/* 541 */     for (int index = 1; index < values.length; index++) {
/* 542 */       double value = values[index];
/* 543 */       if (Doubles.isFinite(value) && Doubles.isFinite(mean)) {
/*     */         
/* 545 */         mean += (value - mean) / (index + 1);
/*     */       } else {
/* 547 */         mean = StatsAccumulator.calculateNewMeanNonFinite(mean, value);
/*     */       } 
/*     */     } 
/* 550 */     return mean;
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
/*     */   public static double meanOf(long... values) {
/* 564 */     Preconditions.checkArgument((values.length > 0));
/* 565 */     double mean = values[0];
/* 566 */     for (int index = 1; index < values.length; index++) {
/* 567 */       double value = values[index];
/* 568 */       if (Doubles.isFinite(value) && Doubles.isFinite(mean)) {
/*     */         
/* 570 */         mean += (value - mean) / (index + 1);
/*     */       } else {
/* 572 */         mean = StatsAccumulator.calculateNewMeanNonFinite(mean, value);
/*     */       } 
/*     */     } 
/* 575 */     return mean;
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
/*     */   public byte[] toByteArray() {
/* 590 */     ByteBuffer buff = ByteBuffer.allocate(40).order(ByteOrder.LITTLE_ENDIAN);
/* 591 */     writeTo(buff);
/* 592 */     return buff.array();
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
/*     */   void writeTo(ByteBuffer buffer) {
/* 606 */     Preconditions.checkNotNull(buffer);
/* 607 */     Preconditions.checkArgument(
/* 608 */         (buffer.remaining() >= 40), "Expected at least Stats.BYTES = %s remaining , got %s", 40, buffer
/*     */ 
/*     */         
/* 611 */         .remaining());
/* 612 */     buffer
/* 613 */       .putLong(this.count)
/* 614 */       .putDouble(this.mean)
/* 615 */       .putDouble(this.sumOfSquaresOfDeltas)
/* 616 */       .putDouble(this.min)
/* 617 */       .putDouble(this.max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stats fromByteArray(byte[] byteArray) {
/* 628 */     Preconditions.checkNotNull(byteArray);
/* 629 */     Preconditions.checkArgument((byteArray.length == 40), "Expected Stats.BYTES = %s remaining , got %s", 40, byteArray.length);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 634 */     return readFrom(ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN));
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
/*     */   static Stats readFrom(ByteBuffer buffer) {
/* 648 */     Preconditions.checkNotNull(buffer);
/* 649 */     Preconditions.checkArgument(
/* 650 */         (buffer.remaining() >= 40), "Expected at least Stats.BYTES = %s remaining , got %s", 40, buffer
/*     */ 
/*     */         
/* 653 */         .remaining());
/* 654 */     return new Stats(buffer
/* 655 */         .getLong(), buffer
/* 656 */         .getDouble(), buffer
/* 657 */         .getDouble(), buffer
/* 658 */         .getDouble(), buffer
/* 659 */         .getDouble());
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\math\Stats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */