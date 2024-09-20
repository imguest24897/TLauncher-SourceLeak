/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Doubles;
/*     */ import com.google.common.primitives.Ints;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Quantiles
/*     */ {
/*     */   public static ScaleAndIndex median() {
/* 135 */     return scale(2).index(1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Scale quartiles() {
/* 140 */     return scale(4);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Scale percentiles() {
/* 145 */     return scale(100);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Scale scale(int scale) {
/* 155 */     return new Scale(scale);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Scale
/*     */   {
/*     */     private final int scale;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Scale(int scale) {
/* 169 */       Preconditions.checkArgument((scale > 0), "Quantile scale must be positive");
/* 170 */       this.scale = scale;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Quantiles.ScaleAndIndex index(int index) {
/* 179 */       return new Quantiles.ScaleAndIndex(this.scale, index);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Quantiles.ScaleAndIndexes indexes(int... indexes) {
/* 192 */       return new Quantiles.ScaleAndIndexes(this.scale, (int[])indexes.clone());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Quantiles.ScaleAndIndexes indexes(Collection<Integer> indexes) {
/* 205 */       return new Quantiles.ScaleAndIndexes(this.scale, Ints.toArray(indexes));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class ScaleAndIndex
/*     */   {
/*     */     private final int scale;
/*     */ 
/*     */     
/*     */     private final int index;
/*     */ 
/*     */ 
/*     */     
/*     */     private ScaleAndIndex(int scale, int index) {
/* 221 */       Quantiles.checkIndex(index, scale);
/* 222 */       this.scale = scale;
/* 223 */       this.index = index;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double compute(Collection<? extends Number> dataset) {
/* 235 */       return computeInPlace(Doubles.toArray(dataset));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double compute(double... dataset) {
/* 246 */       return computeInPlace((double[])dataset.clone());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double compute(long... dataset) {
/* 258 */       return computeInPlace(Quantiles.longsToDoubles(dataset));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double compute(int... dataset) {
/* 269 */       return computeInPlace(Quantiles.intsToDoubles(dataset));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double computeInPlace(double... dataset) {
/* 280 */       Preconditions.checkArgument((dataset.length > 0), "Cannot calculate quantiles of an empty dataset");
/* 281 */       if (Quantiles.containsNaN(dataset)) {
/* 282 */         return Double.NaN;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 292 */       long numerator = this.index * (dataset.length - 1);
/*     */ 
/*     */ 
/*     */       
/* 296 */       int quotient = (int)LongMath.divide(numerator, this.scale, RoundingMode.DOWN);
/* 297 */       int remainder = (int)(numerator - quotient * this.scale);
/* 298 */       Quantiles.selectInPlace(quotient, dataset, 0, dataset.length - 1);
/* 299 */       if (remainder == 0) {
/* 300 */         return dataset[quotient];
/*     */       }
/* 302 */       Quantiles.selectInPlace(quotient + 1, dataset, quotient + 1, dataset.length - 1);
/* 303 */       return Quantiles.interpolate(dataset[quotient], dataset[quotient + 1], remainder, this.scale);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class ScaleAndIndexes
/*     */   {
/*     */     private final int scale;
/*     */ 
/*     */     
/*     */     private final int[] indexes;
/*     */ 
/*     */ 
/*     */     
/*     */     private ScaleAndIndexes(int scale, int[] indexes) {
/* 320 */       for (int index : indexes) {
/* 321 */         Quantiles.checkIndex(index, scale);
/*     */       }
/* 323 */       Preconditions.checkArgument((indexes.length > 0), "Indexes must be a non empty array");
/* 324 */       this.scale = scale;
/* 325 */       this.indexes = indexes;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Integer, Double> compute(Collection<? extends Number> dataset) {
/* 340 */       return computeInPlace(Doubles.toArray(dataset));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Integer, Double> compute(double... dataset) {
/* 354 */       return computeInPlace((double[])dataset.clone());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Integer, Double> compute(long... dataset) {
/* 369 */       return computeInPlace(Quantiles.longsToDoubles(dataset));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Integer, Double> compute(int... dataset) {
/* 383 */       return computeInPlace(Quantiles.intsToDoubles(dataset));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Integer, Double> computeInPlace(double... dataset) {
/* 397 */       Preconditions.checkArgument((dataset.length > 0), "Cannot calculate quantiles of an empty dataset");
/* 398 */       if (Quantiles.containsNaN(dataset)) {
/* 399 */         Map<Integer, Double> nanMap = new LinkedHashMap<>();
/* 400 */         for (int index : this.indexes) {
/* 401 */           nanMap.put(Integer.valueOf(index), Double.valueOf(Double.NaN));
/*     */         }
/* 403 */         return Collections.unmodifiableMap(nanMap);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 411 */       int[] quotients = new int[this.indexes.length];
/* 412 */       int[] remainders = new int[this.indexes.length];
/*     */       
/* 414 */       int[] requiredSelections = new int[this.indexes.length * 2];
/* 415 */       int requiredSelectionsCount = 0;
/* 416 */       for (int i = 0; i < this.indexes.length; i++) {
/*     */ 
/*     */         
/* 419 */         long numerator = this.indexes[i] * (dataset.length - 1);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 424 */         int quotient = (int)LongMath.divide(numerator, this.scale, RoundingMode.DOWN);
/* 425 */         int remainder = (int)(numerator - quotient * this.scale);
/* 426 */         quotients[i] = quotient;
/* 427 */         remainders[i] = remainder;
/* 428 */         requiredSelections[requiredSelectionsCount] = quotient;
/* 429 */         requiredSelectionsCount++;
/* 430 */         if (remainder != 0) {
/* 431 */           requiredSelections[requiredSelectionsCount] = quotient + 1;
/* 432 */           requiredSelectionsCount++;
/*     */         } 
/*     */       } 
/* 435 */       Arrays.sort(requiredSelections, 0, requiredSelectionsCount);
/* 436 */       Quantiles.selectAllInPlace(requiredSelections, 0, requiredSelectionsCount - 1, dataset, 0, dataset.length - 1);
/*     */       
/* 438 */       Map<Integer, Double> ret = new LinkedHashMap<>();
/* 439 */       for (int j = 0; j < this.indexes.length; j++) {
/* 440 */         int quotient = quotients[j];
/* 441 */         int remainder = remainders[j];
/* 442 */         if (remainder == 0) {
/* 443 */           ret.put(Integer.valueOf(this.indexes[j]), Double.valueOf(dataset[quotient]));
/*     */         } else {
/* 445 */           ret.put(
/* 446 */               Integer.valueOf(this.indexes[j]), Double.valueOf(Quantiles.interpolate(dataset[quotient], dataset[quotient + 1], remainder, this.scale)));
/*     */         } 
/*     */       } 
/* 449 */       return Collections.unmodifiableMap(ret);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean containsNaN(double... dataset) {
/* 455 */     for (double value : dataset) {
/* 456 */       if (Double.isNaN(value)) {
/* 457 */         return true;
/*     */       }
/*     */     } 
/* 460 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static double interpolate(double lower, double upper, double remainder, double scale) {
/* 469 */     if (lower == Double.NEGATIVE_INFINITY) {
/* 470 */       if (upper == Double.POSITIVE_INFINITY)
/*     */       {
/* 472 */         return Double.NaN;
/*     */       }
/*     */       
/* 475 */       return Double.NEGATIVE_INFINITY;
/*     */     } 
/* 477 */     if (upper == Double.POSITIVE_INFINITY)
/*     */     {
/* 479 */       return Double.POSITIVE_INFINITY;
/*     */     }
/* 481 */     return lower + (upper - lower) * remainder / scale;
/*     */   }
/*     */   
/*     */   private static void checkIndex(int index, int scale) {
/* 485 */     if (index < 0 || index > scale) {
/* 486 */       throw new IllegalArgumentException((new StringBuilder(70)).append("Quantile indexes must be between 0 and the scale, which is ").append(scale).toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static double[] longsToDoubles(long[] longs) {
/* 492 */     int len = longs.length;
/* 493 */     double[] doubles = new double[len];
/* 494 */     for (int i = 0; i < len; i++) {
/* 495 */       doubles[i] = longs[i];
/*     */     }
/* 497 */     return doubles;
/*     */   }
/*     */   
/*     */   private static double[] intsToDoubles(int[] ints) {
/* 501 */     int len = ints.length;
/* 502 */     double[] doubles = new double[len];
/* 503 */     for (int i = 0; i < len; i++) {
/* 504 */       doubles[i] = ints[i];
/*     */     }
/* 506 */     return doubles;
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
/*     */   private static void selectInPlace(int required, double[] array, int from, int to) {
/* 533 */     if (required == from) {
/* 534 */       int min = from;
/* 535 */       for (int index = from + 1; index <= to; index++) {
/* 536 */         if (array[min] > array[index]) {
/* 537 */           min = index;
/*     */         }
/*     */       } 
/* 540 */       if (min != from) {
/* 541 */         swap(array, min, from);
/*     */       }
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 548 */     while (to > from) {
/* 549 */       int partitionPoint = partition(array, from, to);
/* 550 */       if (partitionPoint >= required) {
/* 551 */         to = partitionPoint - 1;
/*     */       }
/* 553 */       if (partitionPoint <= required) {
/* 554 */         from = partitionPoint + 1;
/*     */       }
/*     */     } 
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
/*     */   private static int partition(double[] array, int from, int to) {
/* 569 */     movePivotToStartOfSlice(array, from, to);
/* 570 */     double pivot = array[from];
/*     */ 
/*     */ 
/*     */     
/* 574 */     int partitionPoint = to;
/* 575 */     for (int i = to; i > from; i--) {
/* 576 */       if (array[i] > pivot) {
/* 577 */         swap(array, partitionPoint, i);
/* 578 */         partitionPoint--;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 585 */     swap(array, from, partitionPoint);
/* 586 */     return partitionPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void movePivotToStartOfSlice(double[] array, int from, int to) {
/* 596 */     int mid = from + to >>> 1;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 601 */     boolean toLessThanMid = (array[to] < array[mid]);
/* 602 */     boolean midLessThanFrom = (array[mid] < array[from]);
/* 603 */     boolean toLessThanFrom = (array[to] < array[from]);
/* 604 */     if (toLessThanMid == midLessThanFrom) {
/*     */       
/* 606 */       swap(array, mid, from);
/* 607 */     } else if (toLessThanMid != toLessThanFrom) {
/*     */       
/* 609 */       swap(array, from, to);
/*     */     } 
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
/*     */   private static void selectAllInPlace(int[] allRequired, int requiredFrom, int requiredTo, double[] array, int from, int to) {
/* 622 */     int requiredChosen = chooseNextSelection(allRequired, requiredFrom, requiredTo, from, to);
/* 623 */     int required = allRequired[requiredChosen];
/*     */ 
/*     */     
/* 626 */     selectInPlace(required, array, from, to);
/*     */ 
/*     */     
/* 629 */     int requiredBelow = requiredChosen - 1;
/* 630 */     while (requiredBelow >= requiredFrom && allRequired[requiredBelow] == required) {
/* 631 */       requiredBelow--;
/*     */     }
/* 633 */     if (requiredBelow >= requiredFrom) {
/* 634 */       selectAllInPlace(allRequired, requiredFrom, requiredBelow, array, from, required - 1);
/*     */     }
/*     */ 
/*     */     
/* 638 */     int requiredAbove = requiredChosen + 1;
/* 639 */     while (requiredAbove <= requiredTo && allRequired[requiredAbove] == required) {
/* 640 */       requiredAbove++;
/*     */     }
/* 642 */     if (requiredAbove <= requiredTo) {
/* 643 */       selectAllInPlace(allRequired, requiredAbove, requiredTo, array, required + 1, to);
/*     */     }
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
/*     */   private static int chooseNextSelection(int[] allRequired, int requiredFrom, int requiredTo, int from, int to) {
/* 658 */     if (requiredFrom == requiredTo) {
/* 659 */       return requiredFrom;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 664 */     int centerFloor = from + to >>> 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 671 */     int low = requiredFrom;
/* 672 */     int high = requiredTo;
/* 673 */     while (high > low + 1) {
/* 674 */       int mid = low + high >>> 1;
/* 675 */       if (allRequired[mid] > centerFloor) {
/* 676 */         high = mid; continue;
/* 677 */       }  if (allRequired[mid] < centerFloor) {
/* 678 */         low = mid; continue;
/*     */       } 
/* 680 */       return mid;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 685 */     if (from + to - allRequired[low] - allRequired[high] > 0) {
/* 686 */       return high;
/*     */     }
/* 688 */     return low;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void swap(double[] array, int i, int j) {
/* 694 */     double temp = array[i];
/* 695 */     array[i] = array[j];
/* 696 */     array[j] = temp;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\math\Quantiles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */