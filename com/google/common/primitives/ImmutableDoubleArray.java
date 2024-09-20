/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.CheckReturnValue;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.RandomAccess;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.DoubleConsumer;
/*     */ import java.util.stream.DoubleStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public final class ImmutableDoubleArray
/*     */   implements Serializable
/*     */ {
/*  92 */   private static final ImmutableDoubleArray EMPTY = new ImmutableDoubleArray(new double[0]);
/*     */   private final double[] array;
/*     */   
/*     */   public static ImmutableDoubleArray of() {
/*  96 */     return EMPTY;
/*     */   }
/*     */   private final transient int start; private final int end;
/*     */   
/*     */   public static ImmutableDoubleArray of(double e0) {
/* 101 */     return new ImmutableDoubleArray(new double[] { e0 });
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableDoubleArray of(double e0, double e1) {
/* 106 */     return new ImmutableDoubleArray(new double[] { e0, e1 });
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableDoubleArray of(double e0, double e1, double e2) {
/* 111 */     return new ImmutableDoubleArray(new double[] { e0, e1, e2 });
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableDoubleArray of(double e0, double e1, double e2, double e3) {
/* 116 */     return new ImmutableDoubleArray(new double[] { e0, e1, e2, e3 });
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableDoubleArray of(double e0, double e1, double e2, double e3, double e4) {
/* 121 */     return new ImmutableDoubleArray(new double[] { e0, e1, e2, e3, e4 });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ImmutableDoubleArray of(double e0, double e1, double e2, double e3, double e4, double e5) {
/* 127 */     return new ImmutableDoubleArray(new double[] { e0, e1, e2, e3, e4, e5 });
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
/*     */   public static ImmutableDoubleArray of(double first, double... rest) {
/* 140 */     Preconditions.checkArgument((rest.length <= 2147483646), "the total number of elements must fit in an int");
/*     */     
/* 142 */     double[] array = new double[rest.length + 1];
/* 143 */     array[0] = first;
/* 144 */     System.arraycopy(rest, 0, array, 1, rest.length);
/* 145 */     return new ImmutableDoubleArray(array);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableDoubleArray copyOf(double[] values) {
/* 150 */     return (values.length == 0) ? 
/* 151 */       EMPTY : 
/* 152 */       new ImmutableDoubleArray(Arrays.copyOf(values, values.length));
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableDoubleArray copyOf(Collection<Double> values) {
/* 157 */     return values.isEmpty() ? EMPTY : new ImmutableDoubleArray(Doubles.toArray((Collection)values));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ImmutableDoubleArray copyOf(Iterable<Double> values) {
/* 168 */     if (values instanceof Collection) {
/* 169 */       return copyOf((Collection<Double>)values);
/*     */     }
/* 171 */     return builder().addAll(values).build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ImmutableDoubleArray copyOf(DoubleStream stream) {
/* 177 */     double[] array = stream.toArray();
/* 178 */     return (array.length == 0) ? EMPTY : new ImmutableDoubleArray(array);
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
/*     */   public static Builder builder(int initialCapacity) {
/* 192 */     Preconditions.checkArgument((initialCapacity >= 0), "Invalid initialCapacity: %s", initialCapacity);
/* 193 */     return new Builder(initialCapacity);
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
/*     */   public static Builder builder() {
/* 205 */     return new Builder(10);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static final class Builder
/*     */   {
/*     */     private double[] array;
/*     */     
/* 215 */     private int count = 0;
/*     */     
/*     */     Builder(int initialCapacity) {
/* 218 */       this.array = new double[initialCapacity];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder add(double value) {
/* 226 */       ensureRoomFor(1);
/* 227 */       this.array[this.count] = value;
/* 228 */       this.count++;
/* 229 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(double[] values) {
/* 237 */       ensureRoomFor(values.length);
/* 238 */       System.arraycopy(values, 0, this.array, this.count, values.length);
/* 239 */       this.count += values.length;
/* 240 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(Iterable<Double> values) {
/* 248 */       if (values instanceof Collection) {
/* 249 */         return addAll((Collection<Double>)values);
/*     */       }
/* 251 */       for (Double value : values) {
/* 252 */         add(value.doubleValue());
/*     */       }
/* 254 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(Collection<Double> values) {
/* 262 */       ensureRoomFor(values.size());
/* 263 */       for (Double value : values) {
/* 264 */         this.array[this.count++] = value.doubleValue();
/*     */       }
/* 266 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(DoubleStream stream) {
/* 274 */       Spliterator.OfDouble spliterator = stream.spliterator();
/* 275 */       long size = spliterator.getExactSizeIfKnown();
/* 276 */       if (size > 0L) {
/* 277 */         ensureRoomFor(Ints.saturatedCast(size));
/*     */       }
/* 279 */       spliterator.forEachRemaining(this::add);
/* 280 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(ImmutableDoubleArray values) {
/* 288 */       ensureRoomFor(values.length());
/* 289 */       System.arraycopy(values.array, values.start, this.array, this.count, values.length());
/* 290 */       this.count += values.length();
/* 291 */       return this;
/*     */     }
/*     */     
/*     */     private void ensureRoomFor(int numberToAdd) {
/* 295 */       int newCount = this.count + numberToAdd;
/* 296 */       if (newCount > this.array.length) {
/* 297 */         this.array = Arrays.copyOf(this.array, expandedCapacity(this.array.length, newCount));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     private static int expandedCapacity(int oldCapacity, int minCapacity) {
/* 303 */       if (minCapacity < 0) {
/* 304 */         throw new AssertionError("cannot store more than MAX_VALUE elements");
/*     */       }
/*     */       
/* 307 */       int newCapacity = oldCapacity + (oldCapacity >> 1) + 1;
/* 308 */       if (newCapacity < minCapacity) {
/* 309 */         newCapacity = Integer.highestOneBit(minCapacity - 1) << 1;
/*     */       }
/* 311 */       if (newCapacity < 0) {
/* 312 */         newCapacity = Integer.MAX_VALUE;
/*     */       }
/* 314 */       return newCapacity;
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
/*     */     @CheckReturnValue
/*     */     public ImmutableDoubleArray build() {
/* 327 */       return (this.count == 0) ? ImmutableDoubleArray.EMPTY : new ImmutableDoubleArray(this.array, 0, this.count);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ImmutableDoubleArray(double[] array) {
/* 348 */     this(array, 0, array.length);
/*     */   }
/*     */   
/*     */   private ImmutableDoubleArray(double[] array, int start, int end) {
/* 352 */     this.array = array;
/* 353 */     this.start = start;
/* 354 */     this.end = end;
/*     */   }
/*     */ 
/*     */   
/*     */   public int length() {
/* 359 */     return this.end - this.start;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 364 */     return (this.end == this.start);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double get(int index) {
/* 374 */     Preconditions.checkElementIndex(index, length());
/* 375 */     return this.array[this.start + index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOf(double target) {
/* 384 */     for (int i = this.start; i < this.end; i++) {
/* 385 */       if (areEqual(this.array[i], target)) {
/* 386 */         return i - this.start;
/*     */       }
/*     */     } 
/* 389 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int lastIndexOf(double target) {
/* 398 */     for (int i = this.end - 1; i >= this.start; i--) {
/* 399 */       if (areEqual(this.array[i], target)) {
/* 400 */         return i - this.start;
/*     */       }
/*     */     } 
/* 403 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(double target) {
/* 411 */     return (indexOf(target) >= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(DoubleConsumer consumer) {
/* 416 */     Preconditions.checkNotNull(consumer);
/* 417 */     for (int i = this.start; i < this.end; i++) {
/* 418 */       consumer.accept(this.array[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public DoubleStream stream() {
/* 424 */     return Arrays.stream(this.array, this.start, this.end);
/*     */   }
/*     */ 
/*     */   
/*     */   public double[] toArray() {
/* 429 */     return Arrays.copyOfRange(this.array, this.start, this.end);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableDoubleArray subArray(int startIndex, int endIndex) {
/* 440 */     Preconditions.checkPositionIndexes(startIndex, endIndex, length());
/* 441 */     return (startIndex == endIndex) ? 
/* 442 */       EMPTY : 
/* 443 */       new ImmutableDoubleArray(this.array, this.start + startIndex, this.start + endIndex);
/*     */   }
/*     */   
/*     */   private Spliterator.OfDouble spliterator() {
/* 447 */     return Spliterators.spliterator(this.array, this.start, this.end, 1040);
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
/*     */   public List<Double> asList() {
/* 463 */     return new AsList(this);
/*     */   }
/*     */   
/*     */   static class AsList extends AbstractList<Double> implements RandomAccess, Serializable {
/*     */     private final ImmutableDoubleArray parent;
/*     */     
/*     */     private AsList(ImmutableDoubleArray parent) {
/* 470 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 477 */       return this.parent.length();
/*     */     }
/*     */ 
/*     */     
/*     */     public Double get(int index) {
/* 482 */       return Double.valueOf(this.parent.get(index));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 487 */       return (indexOf(target) >= 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 492 */       return (target instanceof Double) ? this.parent.indexOf(((Double)target).doubleValue()) : -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 497 */       return (target instanceof Double) ? this.parent.lastIndexOf(((Double)target).doubleValue()) : -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Double> subList(int fromIndex, int toIndex) {
/* 502 */       return this.parent.subArray(fromIndex, toIndex).asList();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Spliterator<Double> spliterator() {
/* 508 */       return this.parent.spliterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 513 */       if (object instanceof AsList) {
/* 514 */         AsList asList = (AsList)object;
/* 515 */         return this.parent.equals(asList.parent);
/*     */       } 
/*     */       
/* 518 */       if (!(object instanceof List)) {
/* 519 */         return false;
/*     */       }
/* 521 */       List<?> that = (List)object;
/* 522 */       if (size() != that.size()) {
/* 523 */         return false;
/*     */       }
/* 525 */       int i = this.parent.start;
/*     */       
/* 527 */       for (Object element : that) {
/* 528 */         if (!(element instanceof Double) || !ImmutableDoubleArray.areEqual(this.parent.array[i++], ((Double)element).doubleValue())) {
/* 529 */           return false;
/*     */         }
/*     */       } 
/* 532 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 538 */       return this.parent.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 543 */       return this.parent.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 553 */     if (object == this) {
/* 554 */       return true;
/*     */     }
/* 556 */     if (!(object instanceof ImmutableDoubleArray)) {
/* 557 */       return false;
/*     */     }
/* 559 */     ImmutableDoubleArray that = (ImmutableDoubleArray)object;
/* 560 */     if (length() != that.length()) {
/* 561 */       return false;
/*     */     }
/* 563 */     for (int i = 0; i < length(); i++) {
/* 564 */       if (!areEqual(get(i), that.get(i))) {
/* 565 */         return false;
/*     */       }
/*     */     } 
/* 568 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean areEqual(double a, double b) {
/* 573 */     return (Double.doubleToLongBits(a) == Double.doubleToLongBits(b));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 579 */     int hash = 1;
/* 580 */     for (int i = this.start; i < this.end; i++) {
/* 581 */       hash *= 31;
/* 582 */       hash += Doubles.hashCode(this.array[i]);
/*     */     } 
/* 584 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 593 */     if (isEmpty()) {
/* 594 */       return "[]";
/*     */     }
/* 596 */     StringBuilder builder = new StringBuilder(length() * 5);
/* 597 */     builder.append('[').append(this.array[this.start]);
/*     */     
/* 599 */     for (int i = this.start + 1; i < this.end; i++) {
/* 600 */       builder.append(", ").append(this.array[i]);
/*     */     }
/* 602 */     builder.append(']');
/* 603 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableDoubleArray trimmed() {
/* 613 */     return isPartialView() ? new ImmutableDoubleArray(toArray()) : this;
/*     */   }
/*     */   
/*     */   private boolean isPartialView() {
/* 617 */     return (this.start > 0 || this.end < this.array.length);
/*     */   }
/*     */   
/*     */   Object writeReplace() {
/* 621 */     return trimmed();
/*     */   }
/*     */   
/*     */   Object readResolve() {
/* 625 */     return isEmpty() ? EMPTY : this;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\primitives\ImmutableDoubleArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */