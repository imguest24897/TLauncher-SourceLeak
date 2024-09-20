/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.IntMath;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ final class TopKSelector<T>
/*     */ {
/*     */   private final int k;
/*     */   private final Comparator<? super T> comparator;
/*     */   private final T[] buffer;
/*     */   private int bufferSize;
/*     */   private T threshold;
/*     */   
/*     */   public static <T extends Comparable<? super T>> TopKSelector<T> least(int k) {
/*  64 */     return least(k, Ordering.natural());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> TopKSelector<T> least(int k, Comparator<? super T> comparator) {
/*  74 */     return new TopKSelector<>(comparator, k);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Comparable<? super T>> TopKSelector<T> greatest(int k) {
/*  85 */     return greatest(k, Ordering.natural());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> TopKSelector<T> greatest(int k, Comparator<? super T> comparator) {
/*  95 */     return new TopKSelector<>(Ordering.<T>from(comparator).reverse(), k);
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
/*     */   private TopKSelector(Comparator<? super T> comparator, int k) {
/* 116 */     this.comparator = (Comparator<? super T>)Preconditions.checkNotNull(comparator, "comparator");
/* 117 */     this.k = k;
/* 118 */     Preconditions.checkArgument((k >= 0), "k (%s) must be >= 0", k);
/* 119 */     Preconditions.checkArgument((k <= 1073741823), "k (%s) must be <= Integer.MAX_VALUE / 2", k);
/* 120 */     this.buffer = (T[])new Object[IntMath.checkedMultiply(k, 2)];
/* 121 */     this.bufferSize = 0;
/* 122 */     this.threshold = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void offer(T elem) {
/* 130 */     if (this.k == 0)
/*     */       return; 
/* 132 */     if (this.bufferSize == 0) {
/* 133 */       this.buffer[0] = elem;
/* 134 */       this.threshold = elem;
/* 135 */       this.bufferSize = 1;
/* 136 */     } else if (this.bufferSize < this.k) {
/* 137 */       this.buffer[this.bufferSize++] = elem;
/* 138 */       if (this.comparator.compare(elem, this.threshold) > 0) {
/* 139 */         this.threshold = elem;
/*     */       }
/* 141 */     } else if (this.comparator.compare(elem, this.threshold) < 0) {
/*     */       
/* 143 */       this.buffer[this.bufferSize++] = elem;
/* 144 */       if (this.bufferSize == 2 * this.k) {
/* 145 */         trim();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void trim() {
/* 155 */     int left = 0;
/* 156 */     int right = 2 * this.k - 1;
/*     */     
/* 158 */     int minThresholdPosition = 0;
/*     */ 
/*     */ 
/*     */     
/* 162 */     int iterations = 0;
/* 163 */     int maxIterations = IntMath.log2(right - left, RoundingMode.CEILING) * 3;
/* 164 */     while (left < right) {
/* 165 */       int pivotIndex = left + right + 1 >>> 1;
/*     */       
/* 167 */       int pivotNewIndex = partition(left, right, pivotIndex);
/*     */       
/* 169 */       if (pivotNewIndex > this.k) {
/* 170 */         right = pivotNewIndex - 1;
/* 171 */       } else if (pivotNewIndex < this.k) {
/* 172 */         left = Math.max(pivotNewIndex, left + 1);
/* 173 */         minThresholdPosition = pivotNewIndex;
/*     */       } else {
/*     */         break;
/*     */       } 
/* 177 */       iterations++;
/* 178 */       if (iterations >= maxIterations) {
/*     */         
/* 180 */         Arrays.sort(this.buffer, left, right, this.comparator);
/*     */         break;
/*     */       } 
/*     */     } 
/* 184 */     this.bufferSize = this.k;
/*     */     
/* 186 */     this.threshold = this.buffer[minThresholdPosition];
/* 187 */     for (int i = minThresholdPosition + 1; i < this.k; i++) {
/* 188 */       if (this.comparator.compare(this.buffer[i], this.threshold) > 0) {
/* 189 */         this.threshold = this.buffer[i];
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
/*     */   private int partition(int left, int right, int pivotIndex) {
/* 201 */     T pivotValue = this.buffer[pivotIndex];
/* 202 */     this.buffer[pivotIndex] = this.buffer[right];
/*     */     
/* 204 */     int pivotNewIndex = left;
/* 205 */     for (int i = left; i < right; i++) {
/* 206 */       if (this.comparator.compare(this.buffer[i], pivotValue) < 0) {
/* 207 */         swap(pivotNewIndex, i);
/* 208 */         pivotNewIndex++;
/*     */       } 
/*     */     } 
/* 211 */     this.buffer[right] = this.buffer[pivotNewIndex];
/* 212 */     this.buffer[pivotNewIndex] = pivotValue;
/* 213 */     return pivotNewIndex;
/*     */   }
/*     */   
/*     */   private void swap(int i, int j) {
/* 217 */     T tmp = this.buffer[i];
/* 218 */     this.buffer[i] = this.buffer[j];
/* 219 */     this.buffer[j] = tmp;
/*     */   }
/*     */   
/*     */   TopKSelector<T> combine(TopKSelector<T> other) {
/* 223 */     for (int i = 0; i < other.bufferSize; i++) {
/* 224 */       offer(other.buffer[i]);
/*     */     }
/* 226 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void offerAll(Iterable<? extends T> elements) {
/* 237 */     offerAll(elements.iterator());
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
/*     */   public void offerAll(Iterator<? extends T> elements) {
/* 249 */     while (elements.hasNext()) {
/* 250 */       offer(elements.next());
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
/*     */   public List<T> topK() {
/* 263 */     Arrays.sort(this.buffer, 0, this.bufferSize, this.comparator);
/* 264 */     if (this.bufferSize > this.k) {
/* 265 */       Arrays.fill((Object[])this.buffer, this.k, this.buffer.length, (Object)null);
/* 266 */       this.bufferSize = this.k;
/* 267 */       this.threshold = this.buffer[this.k - 1];
/*     */     } 
/*     */     
/* 270 */     return Collections.unmodifiableList(Arrays.asList(Arrays.copyOf(this.buffer, this.bufferSize)));
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\TopKSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */