/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class TreeRangeMap<K extends Comparable, V>
/*     */   implements RangeMap<K, V>
/*     */ {
/*     */   private final NavigableMap<Cut<K>, RangeMapEntry<K, V>> entriesByLowerBound;
/*     */   
/*     */   public static <K extends Comparable, V> TreeRangeMap<K, V> create() {
/*  59 */     return new TreeRangeMap<>();
/*     */   }
/*     */   
/*     */   private TreeRangeMap() {
/*  63 */     this.entriesByLowerBound = Maps.newTreeMap();
/*     */   }
/*     */   
/*     */   private static final class RangeMapEntry<K extends Comparable, V>
/*     */     extends AbstractMapEntry<Range<K>, V> {
/*     */     private final Range<K> range;
/*     */     private final V value;
/*     */     
/*     */     RangeMapEntry(Cut<K> lowerBound, Cut<K> upperBound, V value) {
/*  72 */       this(Range.create(lowerBound, upperBound), value);
/*     */     }
/*     */     
/*     */     RangeMapEntry(Range<K> range, V value) {
/*  76 */       this.range = range;
/*  77 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public Range<K> getKey() {
/*  82 */       return this.range;
/*     */     }
/*     */ 
/*     */     
/*     */     public V getValue() {
/*  87 */       return this.value;
/*     */     }
/*     */     
/*     */     public boolean contains(K value) {
/*  91 */       return this.range.contains(value);
/*     */     }
/*     */     
/*     */     Cut<K> getLowerBound() {
/*  95 */       return this.range.lowerBound;
/*     */     }
/*     */     
/*     */     Cut<K> getUpperBound() {
/*  99 */       return this.range.upperBound;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(K key) {
/* 105 */     Map.Entry<Range<K>, V> entry = getEntry(key);
/* 106 */     return (entry == null) ? null : entry.getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map.Entry<Range<K>, V> getEntry(K key) {
/* 112 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntry = this.entriesByLowerBound.floorEntry(Cut.belowValue(key));
/* 113 */     if (mapEntry != null && ((RangeMapEntry)mapEntry.getValue()).contains(key)) {
/* 114 */       return mapEntry.getValue();
/*     */     }
/* 116 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(Range<K> range, V value) {
/* 122 */     if (!range.isEmpty()) {
/* 123 */       Preconditions.checkNotNull(value);
/* 124 */       remove(range);
/* 125 */       this.entriesByLowerBound.put(range.lowerBound, new RangeMapEntry<>(range, value));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void putCoalescing(Range<K> range, V value) {
/* 132 */     if (this.entriesByLowerBound.isEmpty()) {
/* 133 */       put(range, value);
/*     */       
/*     */       return;
/*     */     } 
/* 137 */     Range<K> coalescedRange = coalescedRange(range, (V)Preconditions.checkNotNull(value));
/* 138 */     put(coalescedRange, value);
/*     */   }
/*     */ 
/*     */   
/*     */   private Range<K> coalescedRange(Range<K> range, V value) {
/* 143 */     Range<K> coalescedRange = range;
/*     */     
/* 145 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> lowerEntry = this.entriesByLowerBound.lowerEntry(range.lowerBound);
/* 146 */     coalescedRange = coalesce(coalescedRange, value, lowerEntry);
/*     */ 
/*     */     
/* 149 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> higherEntry = this.entriesByLowerBound.floorEntry(range.upperBound);
/* 150 */     coalescedRange = coalesce(coalescedRange, value, higherEntry);
/*     */     
/* 152 */     return coalescedRange;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K extends Comparable, V> Range<K> coalesce(Range<K> range, V value, Map.Entry<Cut<K>, RangeMapEntry<K, V>> entry) {
/* 158 */     if (entry != null && ((RangeMapEntry)entry
/* 159 */       .getValue()).getKey().isConnected(range) && ((RangeMapEntry)entry
/* 160 */       .getValue()).getValue().equals(value)) {
/* 161 */       return range.span(((RangeMapEntry)entry.getValue()).getKey());
/*     */     }
/* 163 */     return range;
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(RangeMap<K, V> rangeMap) {
/* 168 */     for (Map.Entry<Range<K>, V> entry : (Iterable<Map.Entry<Range<K>, V>>)rangeMap.asMapOfRanges().entrySet()) {
/* 169 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 175 */     this.entriesByLowerBound.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Range<K> span() {
/* 180 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> firstEntry = this.entriesByLowerBound.firstEntry();
/* 181 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> lastEntry = this.entriesByLowerBound.lastEntry();
/* 182 */     if (firstEntry == null) {
/* 183 */       throw new NoSuchElementException();
/*     */     }
/* 185 */     return Range.create(
/* 186 */         (((RangeMapEntry)firstEntry.getValue()).getKey()).lowerBound, (((RangeMapEntry)lastEntry.getValue()).getKey()).upperBound);
/*     */   }
/*     */   
/*     */   private void putRangeMapEntry(Cut<K> lowerBound, Cut<K> upperBound, V value) {
/* 190 */     this.entriesByLowerBound.put(lowerBound, new RangeMapEntry<>(lowerBound, upperBound, value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Range<K> rangeToRemove) {
/* 195 */     if (rangeToRemove.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 204 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntryBelowToTruncate = this.entriesByLowerBound.lowerEntry(rangeToRemove.lowerBound);
/* 205 */     if (mapEntryBelowToTruncate != null) {
/*     */       
/* 207 */       RangeMapEntry<K, V> rangeMapEntry = mapEntryBelowToTruncate.getValue();
/* 208 */       if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.lowerBound) > 0) {
/*     */         
/* 210 */         if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.upperBound) > 0)
/*     */         {
/*     */           
/* 213 */           putRangeMapEntry(rangeToRemove.upperBound, rangeMapEntry
/*     */               
/* 215 */               .getUpperBound(), (V)((RangeMapEntry)mapEntryBelowToTruncate
/* 216 */               .getValue()).getValue());
/*     */         }
/*     */         
/* 219 */         putRangeMapEntry(rangeMapEntry
/* 220 */             .getLowerBound(), rangeToRemove.lowerBound, (V)((RangeMapEntry)mapEntryBelowToTruncate
/*     */             
/* 222 */             .getValue()).getValue());
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 227 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntryAboveToTruncate = this.entriesByLowerBound.lowerEntry(rangeToRemove.upperBound);
/* 228 */     if (mapEntryAboveToTruncate != null) {
/*     */       
/* 230 */       RangeMapEntry<K, V> rangeMapEntry = mapEntryAboveToTruncate.getValue();
/* 231 */       if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.upperBound) > 0)
/*     */       {
/*     */         
/* 234 */         putRangeMapEntry(rangeToRemove.upperBound, rangeMapEntry
/*     */             
/* 236 */             .getUpperBound(), (V)((RangeMapEntry)mapEntryAboveToTruncate
/* 237 */             .getValue()).getValue());
/*     */       }
/*     */     } 
/* 240 */     this.entriesByLowerBound.subMap(rangeToRemove.lowerBound, rangeToRemove.upperBound).clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void split(Cut<K> cut) {
/* 248 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntryToSplit = this.entriesByLowerBound.lowerEntry(cut);
/* 249 */     if (mapEntryToSplit == null) {
/*     */       return;
/*     */     }
/*     */     
/* 253 */     RangeMapEntry<K, V> rangeMapEntry = mapEntryToSplit.getValue();
/* 254 */     if (rangeMapEntry.getUpperBound().compareTo(cut) <= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 258 */     putRangeMapEntry(rangeMapEntry.getLowerBound(), cut, rangeMapEntry.getValue());
/* 259 */     putRangeMapEntry(cut, rangeMapEntry.getUpperBound(), rangeMapEntry.getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void merge(Range<K> range, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
/* 267 */     Preconditions.checkNotNull(range);
/* 268 */     Preconditions.checkNotNull(remappingFunction);
/*     */     
/* 270 */     if (range.isEmpty()) {
/*     */       return;
/*     */     }
/* 273 */     split(range.lowerBound);
/* 274 */     split(range.upperBound);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 279 */     Set<Map.Entry<Cut<K>, RangeMapEntry<K, V>>> entriesInMergeRange = this.entriesByLowerBound.subMap(range.lowerBound, range.upperBound).entrySet();
/*     */ 
/*     */     
/* 282 */     ImmutableMap.Builder<Cut<K>, RangeMapEntry<K, V>> gaps = ImmutableMap.builder();
/* 283 */     if (value != null) {
/*     */       
/* 285 */       Iterator<Map.Entry<Cut<K>, RangeMapEntry<K, V>>> iterator = entriesInMergeRange.iterator();
/* 286 */       Cut<K> lowerBound = range.lowerBound;
/* 287 */       while (iterator.hasNext()) {
/* 288 */         RangeMapEntry<K, V> entry = (RangeMapEntry<K, V>)((Map.Entry)iterator.next()).getValue();
/* 289 */         Cut<K> upperBound = entry.getLowerBound();
/* 290 */         if (!lowerBound.equals(upperBound)) {
/* 291 */           gaps.put(lowerBound, new RangeMapEntry<>(lowerBound, upperBound, value));
/*     */         }
/* 293 */         lowerBound = entry.getUpperBound();
/*     */       } 
/* 295 */       if (!lowerBound.equals(range.upperBound)) {
/* 296 */         gaps.put(lowerBound, new RangeMapEntry<>(lowerBound, range.upperBound, value));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 301 */     Iterator<Map.Entry<Cut<K>, RangeMapEntry<K, V>>> backingItr = entriesInMergeRange.iterator();
/* 302 */     while (backingItr.hasNext()) {
/* 303 */       Map.Entry<Cut<K>, RangeMapEntry<K, V>> entry = backingItr.next();
/* 304 */       V newValue = remappingFunction.apply((V)((RangeMapEntry)entry.getValue()).getValue(), value);
/* 305 */       if (newValue == null) {
/* 306 */         backingItr.remove(); continue;
/*     */       } 
/* 308 */       entry.setValue(new RangeMapEntry<>(((RangeMapEntry)entry
/*     */             
/* 310 */             .getValue()).getLowerBound(), ((RangeMapEntry)entry.getValue()).getUpperBound(), newValue));
/*     */     } 
/*     */ 
/*     */     
/* 314 */     this.entriesByLowerBound.putAll(gaps.build());
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Range<K>, V> asMapOfRanges() {
/* 319 */     return new AsMapOfRanges(this.entriesByLowerBound.values());
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Range<K>, V> asDescendingMapOfRanges() {
/* 324 */     return new AsMapOfRanges(this.entriesByLowerBound.descendingMap().values());
/*     */   }
/*     */   
/*     */   private final class AsMapOfRanges
/*     */     extends Maps.IteratorBasedAbstractMap<Range<K>, V>
/*     */   {
/*     */     final Iterable<Map.Entry<Range<K>, V>> entryIterable;
/*     */     
/*     */     AsMapOfRanges(Iterable<TreeRangeMap.RangeMapEntry<K, V>> entryIterable) {
/* 333 */       this.entryIterable = (Iterable)entryIterable;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 338 */       return (get(key) != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(Object key) {
/* 343 */       if (key instanceof Range) {
/* 344 */         Range<?> range = (Range)key;
/* 345 */         TreeRangeMap.RangeMapEntry<K, V> rangeMapEntry = (TreeRangeMap.RangeMapEntry<K, V>)TreeRangeMap.this.entriesByLowerBound.get(range.lowerBound);
/* 346 */         if (rangeMapEntry != null && rangeMapEntry.getKey().equals(range)) {
/* 347 */           return rangeMapEntry.getValue();
/*     */         }
/*     */       } 
/* 350 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 355 */       return TreeRangeMap.this.entriesByLowerBound.size();
/*     */     }
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<Range<K>, V>> entryIterator() {
/* 360 */       return this.entryIterable.iterator();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public RangeMap<K, V> subRangeMap(Range<K> subRange) {
/* 366 */     if (subRange.equals(Range.all())) {
/* 367 */       return this;
/*     */     }
/* 369 */     return new SubRangeMap(subRange);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private RangeMap<K, V> emptySubRangeMap() {
/* 375 */     return EMPTY_SUB_RANGE_MAP;
/*     */   }
/*     */   
/* 378 */   private static final RangeMap EMPTY_SUB_RANGE_MAP = new RangeMap<Comparable, Object>()
/*     */     {
/*     */       public Object get(Comparable key)
/*     */       {
/* 382 */         return null;
/*     */       }
/*     */ 
/*     */       
/*     */       public Map.Entry<Range, Object> getEntry(Comparable key) {
/* 387 */         return null;
/*     */       }
/*     */ 
/*     */       
/*     */       public Range span() {
/* 392 */         throw new NoSuchElementException();
/*     */       }
/*     */ 
/*     */       
/*     */       public void put(Range range, Object value) {
/* 397 */         Preconditions.checkNotNull(range);
/* 398 */         String str = String.valueOf(range); throw new IllegalArgumentException((new StringBuilder(46 + String.valueOf(str).length())).append("Cannot insert range ").append(str).append(" into an empty subRangeMap").toString());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void putCoalescing(Range range, Object value) {
/* 404 */         Preconditions.checkNotNull(range);
/* 405 */         String str = String.valueOf(range); throw new IllegalArgumentException((new StringBuilder(46 + String.valueOf(str).length())).append("Cannot insert range ").append(str).append(" into an empty subRangeMap").toString());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void putAll(RangeMap rangeMap) {
/* 411 */         if (!rangeMap.asMapOfRanges().isEmpty()) {
/* 412 */           throw new IllegalArgumentException("Cannot putAll(nonEmptyRangeMap) into an empty subRangeMap");
/*     */         }
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void clear() {}
/*     */ 
/*     */       
/*     */       public void remove(Range range) {
/* 422 */         Preconditions.checkNotNull(range);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void merge(Range range, Object value, BiFunction remappingFunction) {
/* 428 */         Preconditions.checkNotNull(range);
/* 429 */         String str = String.valueOf(range); throw new IllegalArgumentException((new StringBuilder(45 + String.valueOf(str).length())).append("Cannot merge range ").append(str).append(" into an empty subRangeMap").toString());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public Map<Range, Object> asMapOfRanges() {
/* 435 */         return Collections.emptyMap();
/*     */       }
/*     */ 
/*     */       
/*     */       public Map<Range, Object> asDescendingMapOfRanges() {
/* 440 */         return Collections.emptyMap();
/*     */       }
/*     */ 
/*     */       
/*     */       public RangeMap subRangeMap(Range range) {
/* 445 */         Preconditions.checkNotNull(range);
/* 446 */         return this;
/*     */       }
/*     */     };
/*     */   
/*     */   private class SubRangeMap
/*     */     implements RangeMap<K, V> {
/*     */     private final Range<K> subRange;
/*     */     
/*     */     SubRangeMap(Range<K> subRange) {
/* 455 */       this.subRange = subRange;
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(K key) {
/* 460 */       return this.subRange.contains(key) ? TreeRangeMap.this.get(key) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Map.Entry<Range<K>, V> getEntry(K key) {
/* 465 */       if (this.subRange.contains(key)) {
/* 466 */         Map.Entry<Range<K>, V> entry = TreeRangeMap.this.getEntry(key);
/* 467 */         if (entry != null) {
/* 468 */           return Maps.immutableEntry(((Range<K>)entry.getKey()).intersection(this.subRange), entry.getValue());
/*     */         }
/*     */       } 
/* 471 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Range<K> span() {
/*     */       Cut<K> lowerBound, upperBound;
/* 478 */       Map.Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> lowerEntry = TreeRangeMap.this.entriesByLowerBound.floorEntry(this.subRange.lowerBound);
/* 479 */       if (lowerEntry != null && ((TreeRangeMap.RangeMapEntry)lowerEntry
/* 480 */         .getValue()).getUpperBound().compareTo(this.subRange.lowerBound) > 0) {
/* 481 */         lowerBound = this.subRange.lowerBound;
/*     */       } else {
/* 483 */         lowerBound = (Cut<K>)TreeRangeMap.this.entriesByLowerBound.ceilingKey(this.subRange.lowerBound);
/* 484 */         if (lowerBound == null || lowerBound.compareTo(this.subRange.upperBound) >= 0) {
/* 485 */           throw new NoSuchElementException();
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 491 */       Map.Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> upperEntry = TreeRangeMap.this.entriesByLowerBound.lowerEntry(this.subRange.upperBound);
/* 492 */       if (upperEntry == null)
/* 493 */         throw new NoSuchElementException(); 
/* 494 */       if (((TreeRangeMap.RangeMapEntry)upperEntry.getValue()).getUpperBound().compareTo(this.subRange.upperBound) >= 0) {
/* 495 */         upperBound = this.subRange.upperBound;
/*     */       } else {
/* 497 */         upperBound = ((TreeRangeMap.RangeMapEntry)upperEntry.getValue()).getUpperBound();
/*     */       } 
/* 499 */       return Range.create(lowerBound, upperBound);
/*     */     }
/*     */ 
/*     */     
/*     */     public void put(Range<K> range, V value) {
/* 504 */       Preconditions.checkArgument(this.subRange
/* 505 */           .encloses(range), "Cannot put range %s into a subRangeMap(%s)", range, this.subRange);
/* 506 */       TreeRangeMap.this.put(range, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void putCoalescing(Range<K> range, V value) {
/* 511 */       if (TreeRangeMap.this.entriesByLowerBound.isEmpty() || !this.subRange.encloses(range)) {
/* 512 */         put(range, value);
/*     */         
/*     */         return;
/*     */       } 
/* 516 */       Range<K> coalescedRange = TreeRangeMap.this.coalescedRange(range, (V)Preconditions.checkNotNull(value));
/*     */       
/* 518 */       put(coalescedRange.intersection(this.subRange), value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void putAll(RangeMap<K, V> rangeMap) {
/* 523 */       if (rangeMap.asMapOfRanges().isEmpty()) {
/*     */         return;
/*     */       }
/* 526 */       Range<K> span = rangeMap.span();
/* 527 */       Preconditions.checkArgument(this.subRange
/* 528 */           .encloses(span), "Cannot putAll rangeMap with span %s into a subRangeMap(%s)", span, this.subRange);
/*     */ 
/*     */ 
/*     */       
/* 532 */       TreeRangeMap.this.putAll(rangeMap);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 537 */       TreeRangeMap.this.remove(this.subRange);
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove(Range<K> range) {
/* 542 */       if (range.isConnected(this.subRange)) {
/* 543 */         TreeRangeMap.this.remove(range.intersection(this.subRange));
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void merge(Range<K> range, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
/* 552 */       Preconditions.checkArgument(this.subRange
/* 553 */           .encloses(range), "Cannot merge range %s into a subRangeMap(%s)", range, this.subRange);
/*     */ 
/*     */ 
/*     */       
/* 557 */       TreeRangeMap.this.merge(range, value, remappingFunction);
/*     */     }
/*     */ 
/*     */     
/*     */     public RangeMap<K, V> subRangeMap(Range<K> range) {
/* 562 */       if (!range.isConnected(this.subRange)) {
/* 563 */         return TreeRangeMap.this.emptySubRangeMap();
/*     */       }
/* 565 */       return TreeRangeMap.this.subRangeMap(range.intersection(this.subRange));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Range<K>, V> asMapOfRanges() {
/* 571 */       return new SubRangeMapAsMap();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<Range<K>, V> asDescendingMapOfRanges() {
/* 576 */       return new SubRangeMapAsMap()
/*     */         {
/*     */           Iterator<Map.Entry<Range<K>, V>> entryIterator()
/*     */           {
/* 580 */             if (TreeRangeMap.SubRangeMap.this.subRange.isEmpty()) {
/* 581 */               return Iterators.emptyIterator();
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 588 */             final Iterator<TreeRangeMap.RangeMapEntry<K, V>> backingItr = TreeRangeMap.this.entriesByLowerBound.headMap(TreeRangeMap.SubRangeMap.this.subRange.upperBound, false).descendingMap().values().iterator();
/* 589 */             return new AbstractIterator<Map.Entry<Range<K>, V>>()
/*     */               {
/*     */                 protected Map.Entry<Range<K>, V> computeNext()
/*     */                 {
/* 593 */                   if (backingItr.hasNext()) {
/* 594 */                     TreeRangeMap.RangeMapEntry<K, V> entry = backingItr.next();
/* 595 */                     if (entry.getUpperBound().compareTo(TreeRangeMap.SubRangeMap.this.subRange.lowerBound) <= 0) {
/* 596 */                       return endOfData();
/*     */                     }
/* 598 */                     return Maps.immutableEntry(entry.getKey().intersection(TreeRangeMap.SubRangeMap.this.subRange), entry.getValue());
/*     */                   } 
/* 600 */                   return endOfData();
/*     */                 }
/*     */               };
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 609 */       if (o instanceof RangeMap) {
/* 610 */         RangeMap<?, ?> rangeMap = (RangeMap<?, ?>)o;
/* 611 */         return asMapOfRanges().equals(rangeMap.asMapOfRanges());
/*     */       } 
/* 613 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 618 */       return asMapOfRanges().hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 623 */       return asMapOfRanges().toString();
/*     */     }
/*     */     
/*     */     class SubRangeMapAsMap
/*     */       extends AbstractMap<Range<K>, V>
/*     */     {
/*     */       public boolean containsKey(Object key) {
/* 630 */         return (get(key) != null);
/*     */       }
/*     */ 
/*     */       
/*     */       public V get(Object key) {
/*     */         try {
/* 636 */           if (key instanceof Range) {
/*     */             
/* 638 */             Range<K> r = (Range<K>)key;
/* 639 */             if (!TreeRangeMap.SubRangeMap.this.subRange.encloses(r) || r.isEmpty()) {
/* 640 */               return null;
/*     */             }
/* 642 */             TreeRangeMap.RangeMapEntry<K, V> candidate = null;
/* 643 */             if (r.lowerBound.compareTo(TreeRangeMap.SubRangeMap.this.subRange.lowerBound) == 0) {
/*     */ 
/*     */               
/* 646 */               Map.Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> entry = TreeRangeMap.this.entriesByLowerBound.floorEntry(r.lowerBound);
/* 647 */               if (entry != null) {
/* 648 */                 candidate = entry.getValue();
/*     */               }
/*     */             } else {
/* 651 */               candidate = (TreeRangeMap.RangeMapEntry<K, V>)TreeRangeMap.this.entriesByLowerBound.get(r.lowerBound);
/*     */             } 
/*     */             
/* 654 */             if (candidate != null && candidate
/* 655 */               .getKey().isConnected(TreeRangeMap.SubRangeMap.this.subRange) && candidate
/* 656 */               .getKey().intersection(TreeRangeMap.SubRangeMap.this.subRange).equals(r)) {
/* 657 */               return candidate.getValue();
/*     */             }
/*     */           } 
/* 660 */         } catch (ClassCastException e) {
/* 661 */           return null;
/*     */         } 
/* 663 */         return null;
/*     */       }
/*     */ 
/*     */       
/*     */       public V remove(Object key) {
/* 668 */         V value = get(key);
/* 669 */         if (value != null) {
/*     */           
/* 671 */           Range<K> range = (Range<K>)key;
/* 672 */           TreeRangeMap.this.remove(range);
/* 673 */           return value;
/*     */         } 
/* 675 */         return null;
/*     */       }
/*     */ 
/*     */       
/*     */       public void clear() {
/* 680 */         TreeRangeMap.SubRangeMap.this.clear();
/*     */       }
/*     */       
/*     */       private boolean removeEntryIf(Predicate<? super Map.Entry<Range<K>, V>> predicate) {
/* 684 */         List<Range<K>> toRemove = Lists.newArrayList();
/* 685 */         for (Map.Entry<Range<K>, V> entry : entrySet()) {
/* 686 */           if (predicate.apply(entry)) {
/* 687 */             toRemove.add(entry.getKey());
/*     */           }
/*     */         } 
/* 690 */         for (Range<K> range : toRemove) {
/* 691 */           TreeRangeMap.this.remove(range);
/*     */         }
/* 693 */         return !toRemove.isEmpty();
/*     */       }
/*     */ 
/*     */       
/*     */       public Set<Range<K>> keySet() {
/* 698 */         return (Set)new Maps.KeySet<Range<Range<K>>, V>(this)
/*     */           {
/*     */             public boolean remove(Object o) {
/* 701 */               return (TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.remove(o) != null);
/*     */             }
/*     */ 
/*     */             
/*     */             public boolean retainAll(Collection<?> c) {
/* 706 */               return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.removeEntryIf(Predicates.compose(Predicates.not(Predicates.in(c)), Maps.keyFunction()));
/*     */             }
/*     */           };
/*     */       }
/*     */ 
/*     */       
/*     */       public Set<Map.Entry<Range<K>, V>> entrySet() {
/* 713 */         return (Set)new Maps.EntrySet<Range<Range<K>>, V>()
/*     */           {
/*     */             Map<Range<K>, V> map() {
/* 716 */               return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this;
/*     */             }
/*     */ 
/*     */             
/*     */             public Iterator<Map.Entry<Range<K>, V>> iterator() {
/* 721 */               return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.entryIterator();
/*     */             }
/*     */ 
/*     */             
/*     */             public boolean retainAll(Collection<?> c) {
/* 726 */               return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.removeEntryIf(Predicates.not(Predicates.in(c)));
/*     */             }
/*     */ 
/*     */             
/*     */             public int size() {
/* 731 */               return Iterators.size(iterator());
/*     */             }
/*     */ 
/*     */             
/*     */             public boolean isEmpty() {
/* 736 */               return !iterator().hasNext();
/*     */             }
/*     */           };
/*     */       }
/*     */       
/*     */       Iterator<Map.Entry<Range<K>, V>> entryIterator() {
/* 742 */         if (TreeRangeMap.SubRangeMap.this.subRange.isEmpty()) {
/* 743 */           return Iterators.emptyIterator();
/*     */         }
/*     */         
/* 746 */         Cut<K> cutToStart = (Cut<K>)MoreObjects.firstNonNull(TreeRangeMap.this
/* 747 */             .entriesByLowerBound.floorKey(TreeRangeMap.SubRangeMap.this.subRange.lowerBound), TreeRangeMap.SubRangeMap.this.subRange.lowerBound);
/*     */         
/* 749 */         final Iterator<TreeRangeMap.RangeMapEntry<K, V>> backingItr = TreeRangeMap.this.entriesByLowerBound.tailMap(cutToStart, true).values().iterator();
/* 750 */         return new AbstractIterator<Map.Entry<Range<K>, V>>()
/*     */           {
/*     */             protected Map.Entry<Range<K>, V> computeNext()
/*     */             {
/* 754 */               while (backingItr.hasNext()) {
/* 755 */                 TreeRangeMap.RangeMapEntry<K, V> entry = backingItr.next();
/* 756 */                 if (entry.getLowerBound().compareTo(TreeRangeMap.SubRangeMap.this.subRange.upperBound) >= 0)
/* 757 */                   return endOfData(); 
/* 758 */                 if (entry.getUpperBound().compareTo(TreeRangeMap.SubRangeMap.this.subRange.lowerBound) > 0)
/*     */                 {
/* 760 */                   return Maps.immutableEntry(entry.getKey().intersection(TreeRangeMap.SubRangeMap.this.subRange), entry.getValue());
/*     */                 }
/*     */               } 
/* 763 */               return endOfData();
/*     */             }
/*     */           };
/*     */       }
/*     */ 
/*     */       
/*     */       public Collection<V> values() {
/* 770 */         return new Maps.Values<Range<Range<K>>, V>(this)
/*     */           {
/*     */             public boolean removeAll(Collection<?> c) {
/* 773 */               return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.removeEntryIf(Predicates.compose(Predicates.in(c), Maps.valueFunction()));
/*     */             }
/*     */ 
/*     */             
/*     */             public boolean retainAll(Collection<?> c) {
/* 778 */               return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.removeEntryIf(Predicates.compose(Predicates.not(Predicates.in(c)), Maps.valueFunction()));
/*     */             }
/*     */           };
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 787 */     if (o instanceof RangeMap) {
/* 788 */       RangeMap<?, ?> rangeMap = (RangeMap<?, ?>)o;
/* 789 */       return asMapOfRanges().equals(rangeMap.asMapOfRanges());
/*     */     } 
/* 791 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 796 */     return asMapOfRanges().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 801 */     return this.entriesByLowerBound.values().toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\TreeRangeMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */