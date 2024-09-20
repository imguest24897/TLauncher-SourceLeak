/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.DoNotMock;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class ImmutableRangeMap<K extends Comparable<?>, V>
/*     */   implements RangeMap<K, V>, Serializable
/*     */ {
/*  49 */   private static final ImmutableRangeMap<Comparable<?>, Object> EMPTY = new ImmutableRangeMap(
/*  50 */       ImmutableList.of(), ImmutableList.of());
/*     */ 
/*     */   
/*     */   private final transient ImmutableList<Range<K>> ranges;
/*     */ 
/*     */   
/*     */   private final transient ImmutableList<V> values;
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = 0L;
/*     */ 
/*     */   
/*     */   public static <T, K extends Comparable<? super K>, V> Collector<T, ?, ImmutableRangeMap<K, V>> toImmutableRangeMap(Function<? super T, Range<K>> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*  63 */     return CollectCollectors.toImmutableRangeMap(keyFunction, valueFunction);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of() {
/*  69 */     return (ImmutableRangeMap)EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of(Range<K> range, V value) {
/*  74 */     return new ImmutableRangeMap<>(ImmutableList.of(range), ImmutableList.of(value));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> copyOf(RangeMap<K, ? extends V> rangeMap) {
/*  80 */     if (rangeMap instanceof ImmutableRangeMap) {
/*  81 */       return (ImmutableRangeMap)rangeMap;
/*     */     }
/*  83 */     Map<Range<K>, ? extends V> map = rangeMap.asMapOfRanges();
/*  84 */     ImmutableList.Builder<Range<K>> rangesBuilder = new ImmutableList.Builder<>(map.size());
/*  85 */     ImmutableList.Builder<V> valuesBuilder = new ImmutableList.Builder<>(map.size());
/*  86 */     for (Map.Entry<Range<K>, ? extends V> entry : map.entrySet()) {
/*  87 */       rangesBuilder.add(entry.getKey());
/*  88 */       valuesBuilder.add(entry.getValue());
/*     */     } 
/*  90 */     return new ImmutableRangeMap<>(rangesBuilder.build(), valuesBuilder.build());
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<?>, V> Builder<K, V> builder() {
/*  95 */     return new Builder<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @DoNotMock
/*     */   public static final class Builder<K extends Comparable<?>, V>
/*     */   {
/* 108 */     private final List<Map.Entry<Range<K>, V>> entries = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(Range<K> range, V value) {
/* 118 */       Preconditions.checkNotNull(range);
/* 119 */       Preconditions.checkNotNull(value);
/* 120 */       Preconditions.checkArgument(!range.isEmpty(), "Range must not be empty, but was %s", range);
/* 121 */       this.entries.add(Maps.immutableEntry(range, value));
/* 122 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(RangeMap<K, ? extends V> rangeMap) {
/* 128 */       for (Map.Entry<Range<K>, ? extends V> entry : (Iterable<Map.Entry<Range<K>, ? extends V>>)rangeMap.asMapOfRanges().entrySet()) {
/* 129 */         put(entry.getKey(), entry.getValue());
/*     */       }
/* 131 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<K, V> combine(Builder<K, V> builder) {
/* 136 */       this.entries.addAll(builder.entries);
/* 137 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableRangeMap<K, V> build() {
/* 147 */       Collections.sort(this.entries, Range.<Comparable<?>>rangeLexOrdering().onKeys());
/* 148 */       ImmutableList.Builder<Range<K>> rangesBuilder = new ImmutableList.Builder<>(this.entries.size());
/* 149 */       ImmutableList.Builder<V> valuesBuilder = new ImmutableList.Builder<>(this.entries.size());
/* 150 */       for (int i = 0; i < this.entries.size(); i++) {
/* 151 */         Range<K> range = (Range<K>)((Map.Entry)this.entries.get(i)).getKey();
/* 152 */         if (i > 0) {
/* 153 */           Range<K> prevRange = (Range<K>)((Map.Entry)this.entries.get(i - 1)).getKey();
/* 154 */           if (range.isConnected(prevRange) && !range.intersection(prevRange).isEmpty()) {
/* 155 */             String str1 = String.valueOf(prevRange), str2 = String.valueOf(range); throw new IllegalArgumentException((new StringBuilder(47 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("Overlapping ranges: range ").append(str1).append(" overlaps with entry ").append(str2).toString());
/*     */           } 
/*     */         } 
/*     */         
/* 159 */         rangesBuilder.add(range);
/* 160 */         valuesBuilder.add((V)((Map.Entry)this.entries.get(i)).getValue());
/*     */       } 
/* 162 */       return new ImmutableRangeMap<>(rangesBuilder.build(), valuesBuilder.build());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableRangeMap(ImmutableList<Range<K>> ranges, ImmutableList<V> values) {
/* 170 */     this.ranges = ranges;
/* 171 */     this.values = values;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V get(K key) {
/* 177 */     int index = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 179 */         (Function)Range.lowerBoundFn(), 
/* 180 */         Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */ 
/*     */     
/* 183 */     if (index == -1) {
/* 184 */       return null;
/*     */     }
/* 186 */     Range<K> range = this.ranges.get(index);
/* 187 */     return range.contains(key) ? this.values.get(index) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map.Entry<Range<K>, V> getEntry(K key) {
/* 194 */     int index = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 196 */         (Function)Range.lowerBoundFn(), 
/* 197 */         Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */ 
/*     */     
/* 200 */     if (index == -1) {
/* 201 */       return null;
/*     */     }
/* 203 */     Range<K> range = this.ranges.get(index);
/* 204 */     return range.contains(key) ? Maps.<Range<K>, V>immutableEntry(range, this.values.get(index)) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Range<K> span() {
/* 210 */     if (this.ranges.isEmpty()) {
/* 211 */       throw new NoSuchElementException();
/*     */     }
/* 213 */     Range<K> firstRange = this.ranges.get(0);
/* 214 */     Range<K> lastRange = this.ranges.get(this.ranges.size() - 1);
/* 215 */     return Range.create(firstRange.lowerBound, lastRange.upperBound);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void put(Range<K> range, V value) {
/* 227 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void putCoalescing(Range<K> range, V value) {
/* 239 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void putAll(RangeMap<K, V> rangeMap) {
/* 251 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void clear() {
/* 263 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void remove(Range<K> range) {
/* 275 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   public void merge(Range<K> range, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
/* 290 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableMap<Range<K>, V> asMapOfRanges() {
/* 295 */     if (this.ranges.isEmpty()) {
/* 296 */       return ImmutableMap.of();
/*     */     }
/*     */     
/* 299 */     RegularImmutableSortedSet<Range<K>> rangeSet = new RegularImmutableSortedSet<>(this.ranges, (Comparator)Range.rangeLexOrdering());
/* 300 */     return new ImmutableSortedMap<>(rangeSet, this.values);
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableMap<Range<K>, V> asDescendingMapOfRanges() {
/* 305 */     if (this.ranges.isEmpty()) {
/* 306 */       return ImmutableMap.of();
/*     */     }
/*     */     
/* 309 */     RegularImmutableSortedSet<Range<K>> rangeSet = new RegularImmutableSortedSet<>(this.ranges.reverse(), Range.<Comparable<?>>rangeLexOrdering().reverse());
/* 310 */     return new ImmutableSortedMap<>(rangeSet, this.values.reverse());
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableRangeMap<K, V> subRangeMap(final Range<K> range) {
/* 315 */     if (((Range)Preconditions.checkNotNull(range)).isEmpty())
/* 316 */       return of(); 
/* 317 */     if (this.ranges.isEmpty() || range.encloses(span())) {
/* 318 */       return this;
/*     */     }
/*     */     
/* 321 */     int lowerIndex = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 323 */         Range.upperBoundFn(), range.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 328 */     int upperIndex = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 330 */         Range.lowerBoundFn(), range.upperBound, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */ 
/*     */ 
/*     */     
/* 334 */     if (lowerIndex >= upperIndex) {
/* 335 */       return of();
/*     */     }
/* 337 */     final int off = lowerIndex;
/* 338 */     final int len = upperIndex - lowerIndex;
/* 339 */     ImmutableList<Range<K>> subRanges = new ImmutableList<Range<K>>()
/*     */       {
/*     */         public int size()
/*     */         {
/* 343 */           return len;
/*     */         }
/*     */ 
/*     */         
/*     */         public Range<K> get(int index) {
/* 348 */           Preconditions.checkElementIndex(index, len);
/* 349 */           if (index == 0 || index == len - 1) {
/* 350 */             return ((Range<K>)ImmutableRangeMap.this.ranges.get(index + off)).intersection(range);
/*     */           }
/* 352 */           return ImmutableRangeMap.this.ranges.get(index + off);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         boolean isPartialView() {
/* 358 */           return true;
/*     */         }
/*     */       };
/* 361 */     final ImmutableRangeMap<K, V> outer = this;
/* 362 */     return new ImmutableRangeMap<K, V>(this, subRanges, this.values.subList(lowerIndex, upperIndex))
/*     */       {
/*     */         public ImmutableRangeMap<K, V> subRangeMap(Range<K> subRange) {
/* 365 */           if (range.isConnected(subRange)) {
/* 366 */             return outer.subRangeMap(subRange.intersection(range));
/*     */           }
/* 368 */           return ImmutableRangeMap.of();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 376 */     return asMapOfRanges().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 381 */     if (o instanceof RangeMap) {
/* 382 */       RangeMap<?, ?> rangeMap = (RangeMap<?, ?>)o;
/* 383 */       return asMapOfRanges().equals(rangeMap.asMapOfRanges());
/*     */     } 
/* 385 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 390 */     return asMapOfRanges().toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static class SerializedForm<K extends Comparable<?>, V>
/*     */     implements Serializable
/*     */   {
/*     */     private final ImmutableMap<Range<K>, V> mapOfRanges;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(ImmutableMap<Range<K>, V> mapOfRanges) {
/* 402 */       this.mapOfRanges = mapOfRanges;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 406 */       if (this.mapOfRanges.isEmpty()) {
/* 407 */         return ImmutableRangeMap.of();
/*     */       }
/* 409 */       return createRangeMap();
/*     */     }
/*     */ 
/*     */     
/*     */     Object createRangeMap() {
/* 414 */       ImmutableRangeMap.Builder<K, V> builder = new ImmutableRangeMap.Builder<>();
/* 415 */       for (UnmodifiableIterator<Map.Entry<Range<K>, V>> unmodifiableIterator = this.mapOfRanges.entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<Range<K>, V> entry = unmodifiableIterator.next();
/* 416 */         builder.put(entry.getKey(), entry.getValue()); }
/*     */       
/* 418 */       return builder.build();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 425 */     return new SerializedForm<>(asMapOfRanges());
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\ImmutableRangeMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */