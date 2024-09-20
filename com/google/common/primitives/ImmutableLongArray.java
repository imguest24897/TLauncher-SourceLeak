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
/*     */ import java.util.function.LongConsumer;
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
/*     */ public final class ImmutableLongArray
/*     */   implements Serializable
/*     */ {
/*  92 */   private static final ImmutableLongArray EMPTY = new ImmutableLongArray(new long[0]);
/*     */   private final long[] array;
/*     */   
/*     */   public static ImmutableLongArray of() {
/*  96 */     return EMPTY;
/*     */   }
/*     */   private final transient int start; private final int end;
/*     */   
/*     */   public static ImmutableLongArray of(long e0) {
/* 101 */     return new ImmutableLongArray(new long[] { e0 });
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableLongArray of(long e0, long e1) {
/* 106 */     return new ImmutableLongArray(new long[] { e0, e1 });
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableLongArray of(long e0, long e1, long e2) {
/* 111 */     return new ImmutableLongArray(new long[] { e0, e1, e2 });
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableLongArray of(long e0, long e1, long e2, long e3) {
/* 116 */     return new ImmutableLongArray(new long[] { e0, e1, e2, e3 });
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableLongArray of(long e0, long e1, long e2, long e3, long e4) {
/* 121 */     return new ImmutableLongArray(new long[] { e0, e1, e2, e3, e4 });
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableLongArray of(long e0, long e1, long e2, long e3, long e4, long e5) {
/* 126 */     return new ImmutableLongArray(new long[] { e0, e1, e2, e3, e4, e5 });
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
/*     */   public static ImmutableLongArray of(long first, long... rest) {
/* 139 */     Preconditions.checkArgument((rest.length <= 2147483646), "the total number of elements must fit in an int");
/*     */     
/* 141 */     long[] array = new long[rest.length + 1];
/* 142 */     array[0] = first;
/* 143 */     System.arraycopy(rest, 0, array, 1, rest.length);
/* 144 */     return new ImmutableLongArray(array);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableLongArray copyOf(long[] values) {
/* 149 */     return (values.length == 0) ? 
/* 150 */       EMPTY : 
/* 151 */       new ImmutableLongArray(Arrays.copyOf(values, values.length));
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableLongArray copyOf(Collection<Long> values) {
/* 156 */     return values.isEmpty() ? EMPTY : new ImmutableLongArray(Longs.toArray((Collection)values));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ImmutableLongArray copyOf(Iterable<Long> values) {
/* 167 */     if (values instanceof Collection) {
/* 168 */       return copyOf((Collection<Long>)values);
/*     */     }
/* 170 */     return builder().addAll(values).build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ImmutableLongArray copyOf(LongStream stream) {
/* 176 */     long[] array = stream.toArray();
/* 177 */     return (array.length == 0) ? EMPTY : new ImmutableLongArray(array);
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
/* 191 */     Preconditions.checkArgument((initialCapacity >= 0), "Invalid initialCapacity: %s", initialCapacity);
/* 192 */     return new Builder(initialCapacity);
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
/* 204 */     return new Builder(10);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static final class Builder
/*     */   {
/*     */     private long[] array;
/*     */     
/* 214 */     private int count = 0;
/*     */     
/*     */     Builder(int initialCapacity) {
/* 217 */       this.array = new long[initialCapacity];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder add(long value) {
/* 225 */       ensureRoomFor(1);
/* 226 */       this.array[this.count] = value;
/* 227 */       this.count++;
/* 228 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(long[] values) {
/* 236 */       ensureRoomFor(values.length);
/* 237 */       System.arraycopy(values, 0, this.array, this.count, values.length);
/* 238 */       this.count += values.length;
/* 239 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(Iterable<Long> values) {
/* 247 */       if (values instanceof Collection) {
/* 248 */         return addAll((Collection<Long>)values);
/*     */       }
/* 250 */       for (Long value : values) {
/* 251 */         add(value.longValue());
/*     */       }
/* 253 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(Collection<Long> values) {
/* 261 */       ensureRoomFor(values.size());
/* 262 */       for (Long value : values) {
/* 263 */         this.array[this.count++] = value.longValue();
/*     */       }
/* 265 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(LongStream stream) {
/* 273 */       Spliterator.OfLong spliterator = stream.spliterator();
/* 274 */       long size = spliterator.getExactSizeIfKnown();
/* 275 */       if (size > 0L) {
/* 276 */         ensureRoomFor(Ints.saturatedCast(size));
/*     */       }
/* 278 */       spliterator.forEachRemaining(this::add);
/* 279 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(ImmutableLongArray values) {
/* 287 */       ensureRoomFor(values.length());
/* 288 */       System.arraycopy(values.array, values.start, this.array, this.count, values.length());
/* 289 */       this.count += values.length();
/* 290 */       return this;
/*     */     }
/*     */     
/*     */     private void ensureRoomFor(int numberToAdd) {
/* 294 */       int newCount = this.count + numberToAdd;
/* 295 */       if (newCount > this.array.length) {
/* 296 */         this.array = Arrays.copyOf(this.array, expandedCapacity(this.array.length, newCount));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     private static int expandedCapacity(int oldCapacity, int minCapacity) {
/* 302 */       if (minCapacity < 0) {
/* 303 */         throw new AssertionError("cannot store more than MAX_VALUE elements");
/*     */       }
/*     */       
/* 306 */       int newCapacity = oldCapacity + (oldCapacity >> 1) + 1;
/* 307 */       if (newCapacity < minCapacity) {
/* 308 */         newCapacity = Integer.highestOneBit(minCapacity - 1) << 1;
/*     */       }
/* 310 */       if (newCapacity < 0) {
/* 311 */         newCapacity = Integer.MAX_VALUE;
/*     */       }
/* 313 */       return newCapacity;
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
/*     */     public ImmutableLongArray build() {
/* 326 */       return (this.count == 0) ? ImmutableLongArray.EMPTY : new ImmutableLongArray(this.array, 0, this.count);
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
/*     */   private ImmutableLongArray(long[] array) {
/* 347 */     this(array, 0, array.length);
/*     */   }
/*     */   
/*     */   private ImmutableLongArray(long[] array, int start, int end) {
/* 351 */     this.array = array;
/* 352 */     this.start = start;
/* 353 */     this.end = end;
/*     */   }
/*     */ 
/*     */   
/*     */   public int length() {
/* 358 */     return this.end - this.start;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 363 */     return (this.end == this.start);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long get(int index) {
/* 373 */     Preconditions.checkElementIndex(index, length());
/* 374 */     return this.array[this.start + index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOf(long target) {
/* 382 */     for (int i = this.start; i < this.end; i++) {
/* 383 */       if (this.array[i] == target) {
/* 384 */         return i - this.start;
/*     */       }
/*     */     } 
/* 387 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int lastIndexOf(long target) {
/* 395 */     for (int i = this.end - 1; i >= this.start; i--) {
/* 396 */       if (this.array[i] == target) {
/* 397 */         return i - this.start;
/*     */       }
/*     */     } 
/* 400 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(long target) {
/* 408 */     return (indexOf(target) >= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(LongConsumer consumer) {
/* 413 */     Preconditions.checkNotNull(consumer);
/* 414 */     for (int i = this.start; i < this.end; i++) {
/* 415 */       consumer.accept(this.array[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public LongStream stream() {
/* 421 */     return Arrays.stream(this.array, this.start, this.end);
/*     */   }
/*     */ 
/*     */   
/*     */   public long[] toArray() {
/* 426 */     return Arrays.copyOfRange(this.array, this.start, this.end);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableLongArray subArray(int startIndex, int endIndex) {
/* 437 */     Preconditions.checkPositionIndexes(startIndex, endIndex, length());
/* 438 */     return (startIndex == endIndex) ? 
/* 439 */       EMPTY : 
/* 440 */       new ImmutableLongArray(this.array, this.start + startIndex, this.start + endIndex);
/*     */   }
/*     */   
/*     */   private Spliterator.OfLong spliterator() {
/* 444 */     return Spliterators.spliterator(this.array, this.start, this.end, 1040);
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
/*     */   public List<Long> asList() {
/* 460 */     return new AsList(this);
/*     */   }
/*     */   
/*     */   static class AsList extends AbstractList<Long> implements RandomAccess, Serializable {
/*     */     private final ImmutableLongArray parent;
/*     */     
/*     */     private AsList(ImmutableLongArray parent) {
/* 467 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 474 */       return this.parent.length();
/*     */     }
/*     */ 
/*     */     
/*     */     public Long get(int index) {
/* 479 */       return Long.valueOf(this.parent.get(index));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 484 */       return (indexOf(target) >= 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 489 */       return (target instanceof Long) ? this.parent.indexOf(((Long)target).longValue()) : -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 494 */       return (target instanceof Long) ? this.parent.lastIndexOf(((Long)target).longValue()) : -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Long> subList(int fromIndex, int toIndex) {
/* 499 */       return this.parent.subArray(fromIndex, toIndex).asList();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Spliterator<Long> spliterator() {
/* 505 */       return this.parent.spliterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 510 */       if (object instanceof AsList) {
/* 511 */         AsList asList = (AsList)object;
/* 512 */         return this.parent.equals(asList.parent);
/*     */       } 
/*     */       
/* 515 */       if (!(object instanceof List)) {
/* 516 */         return false;
/*     */       }
/* 518 */       List<?> that = (List)object;
/* 519 */       if (size() != that.size()) {
/* 520 */         return false;
/*     */       }
/* 522 */       int i = this.parent.start;
/*     */       
/* 524 */       for (Object element : that) {
/* 525 */         if (!(element instanceof Long) || this.parent.array[i++] != ((Long)element).longValue()) {
/* 526 */           return false;
/*     */         }
/*     */       } 
/* 529 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 535 */       return this.parent.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 540 */       return this.parent.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 550 */     if (object == this) {
/* 551 */       return true;
/*     */     }
/* 553 */     if (!(object instanceof ImmutableLongArray)) {
/* 554 */       return false;
/*     */     }
/* 556 */     ImmutableLongArray that = (ImmutableLongArray)object;
/* 557 */     if (length() != that.length()) {
/* 558 */       return false;
/*     */     }
/* 560 */     for (int i = 0; i < length(); i++) {
/* 561 */       if (get(i) != that.get(i)) {
/* 562 */         return false;
/*     */       }
/*     */     } 
/* 565 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 571 */     int hash = 1;
/* 572 */     for (int i = this.start; i < this.end; i++) {
/* 573 */       hash *= 31;
/* 574 */       hash += Longs.hashCode(this.array[i]);
/*     */     } 
/* 576 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 585 */     if (isEmpty()) {
/* 586 */       return "[]";
/*     */     }
/* 588 */     StringBuilder builder = new StringBuilder(length() * 5);
/* 589 */     builder.append('[').append(this.array[this.start]);
/*     */     
/* 591 */     for (int i = this.start + 1; i < this.end; i++) {
/* 592 */       builder.append(", ").append(this.array[i]);
/*     */     }
/* 594 */     builder.append(']');
/* 595 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableLongArray trimmed() {
/* 605 */     return isPartialView() ? new ImmutableLongArray(toArray()) : this;
/*     */   }
/*     */   
/*     */   private boolean isPartialView() {
/* 609 */     return (this.start > 0 || this.end < this.array.length);
/*     */   }
/*     */   
/*     */   Object writeReplace() {
/* 613 */     return trimmed();
/*     */   }
/*     */   
/*     */   Object readResolve() {
/* 617 */     return isEmpty() ? EMPTY : this;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\primitives\ImmutableLongArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */