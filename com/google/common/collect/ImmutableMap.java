/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.DoNotMock;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.BinaryOperator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @DoNotMock("Use ImmutableMap.of or another implementation")
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableMap<K, V>
/*     */   implements Map<K, V>, Serializable
/*     */ {
/*     */   public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*  83 */     return CollectCollectors.toImmutableMap(keyFunction, valueFunction);
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
/*     */   public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/* 101 */     return CollectCollectors.toImmutableMap(keyFunction, valueFunction, mergeFunction);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of() {
/* 111 */     return (ImmutableMap)RegularImmutableMap.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1) {
/* 120 */     return ImmutableBiMap.of(k1, v1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2) {
/* 129 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/* 138 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 147 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/* 148 */           entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4)
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 158 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/* 159 */           entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5)
/*     */         });
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
/*     */   static <K, V> Map.Entry<K, V> entryOf(K key, V value) {
/* 172 */     CollectPreconditions.checkEntryNotNull(key, value);
/* 173 */     return new AbstractMap.SimpleImmutableEntry<>(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 181 */     return new Builder<>();
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
/*     */   @Beta
/*     */   public static <K, V> Builder<K, V> builderWithExpectedSize(int expectedSize) {
/* 198 */     CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
/* 199 */     return new Builder<>(expectedSize);
/*     */   }
/*     */ 
/*     */   
/*     */   static void checkNoConflict(boolean safe, String conflictDescription, Map.Entry<?, ?> entry1, Map.Entry<?, ?> entry2) {
/* 204 */     if (!safe) {
/* 205 */       throw conflictException(conflictDescription, entry1, entry2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static IllegalArgumentException conflictException(String conflictDescription, Object entry1, Object entry2) {
/* 211 */     String str1 = String.valueOf(entry1), str2 = String.valueOf(entry2); return new IllegalArgumentException((new StringBuilder(34 + String.valueOf(conflictDescription).length() + String.valueOf(str1).length() + String.valueOf(str2).length())).append("Multiple entries with same ").append(conflictDescription).append(": ").append(str1).append(" and ").append(str2).toString());
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
/*     */   public static class Builder<K, V>
/*     */   {
/*     */     Comparator<? super V> valueComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Map.Entry<K, V>[] entries;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int size;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean entriesUsed;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder() {
/* 256 */       this(4);
/*     */     }
/*     */ 
/*     */     
/*     */     Builder(int initialCapacity) {
/* 261 */       this.entries = (Map.Entry<K, V>[])new Map.Entry[initialCapacity];
/* 262 */       this.size = 0;
/* 263 */       this.entriesUsed = false;
/*     */     }
/*     */     
/*     */     private void ensureCapacity(int minCapacity) {
/* 267 */       if (minCapacity > this.entries.length) {
/* 268 */         this
/* 269 */           .entries = Arrays.<Map.Entry<K, V>>copyOf(this.entries, 
/* 270 */             ImmutableCollection.Builder.expandedCapacity(this.entries.length, minCapacity));
/* 271 */         this.entriesUsed = false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(K key, V value) {
/* 281 */       ensureCapacity(this.size + 1);
/* 282 */       Map.Entry<K, V> entry = ImmutableMap.entryOf(key, value);
/*     */       
/* 284 */       this.entries[this.size++] = entry;
/* 285 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
/* 296 */       return put(entry.getKey(), entry.getValue());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
/* 307 */       return putAll(map.entrySet());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     @Beta
/*     */     public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 320 */       if (entries instanceof Collection) {
/* 321 */         ensureCapacity(this.size + ((Collection)entries).size());
/*     */       }
/* 323 */       for (Map.Entry<? extends K, ? extends V> entry : entries) {
/* 324 */         put(entry);
/*     */       }
/* 326 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     @Beta
/*     */     public Builder<K, V> orderEntriesByValue(Comparator<? super V> valueComparator) {
/* 342 */       Preconditions.checkState((this.valueComparator == null), "valueComparator was already set");
/* 343 */       this.valueComparator = (Comparator<? super V>)Preconditions.checkNotNull(valueComparator, "valueComparator");
/* 344 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<K, V> combine(Builder<K, V> other) {
/* 349 */       Preconditions.checkNotNull(other);
/* 350 */       ensureCapacity(this.size + other.size);
/* 351 */       System.arraycopy(other.entries, 0, this.entries, this.size, other.size);
/* 352 */       this.size += other.size;
/* 353 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableMap<K, V> build() {
/* 376 */       if (this.valueComparator != null) {
/* 377 */         if (this.entriesUsed) {
/* 378 */           this.entries = Arrays.<Map.Entry<K, V>>copyOf(this.entries, this.size);
/*     */         }
/* 380 */         Arrays.sort(this.entries, 0, this.size, 
/* 381 */             Ordering.<V>from(this.valueComparator).onResultOf(Maps.valueFunction()));
/*     */       } 
/* 383 */       switch (this.size) {
/*     */         case 0:
/* 385 */           return ImmutableMap.of();
/*     */         case 1:
/* 387 */           return ImmutableMap.of(this.entries[0].getKey(), this.entries[0].getValue());
/*     */       } 
/* 389 */       this.entriesUsed = true;
/* 390 */       return RegularImmutableMap.fromEntryArray(this.size, this.entries);
/*     */     }
/*     */ 
/*     */     
/*     */     @VisibleForTesting
/*     */     ImmutableMap<K, V> buildJdkBacked() {
/* 396 */       Preconditions.checkState((this.valueComparator == null), "buildJdkBacked is only for testing; can't use valueComparator");
/*     */       
/* 398 */       switch (this.size) {
/*     */         case 0:
/* 400 */           return ImmutableMap.of();
/*     */         case 1:
/* 402 */           return ImmutableMap.of(this.entries[0].getKey(), this.entries[0].getValue());
/*     */       } 
/* 404 */       this.entriesUsed = true;
/* 405 */       return JdkBackedImmutableMap.create(this.size, this.entries);
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
/*     */   public static <K, V> ImmutableMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
/* 423 */     if (map instanceof ImmutableMap && !(map instanceof java.util.SortedMap)) {
/*     */       
/* 425 */       ImmutableMap<K, V> kvMap = (ImmutableMap)map;
/* 426 */       if (!kvMap.isPartialView()) {
/* 427 */         return kvMap;
/*     */       }
/* 429 */     } else if (map instanceof EnumMap) {
/*     */       
/* 431 */       ImmutableMap<K, V> kvMap = (ImmutableMap)copyOfEnumMap((EnumMap)map);
/* 432 */       return kvMap;
/*     */     } 
/* 434 */     return copyOf(map.entrySet());
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
/*     */   @Beta
/*     */   public static <K, V> ImmutableMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/*     */     Map.Entry<K, V> onlyEntry;
/* 449 */     Map.Entry[] arrayOfEntry = Iterables.<Map.Entry>toArray((Iterable)entries, (Map.Entry[])EMPTY_ENTRY_ARRAY);
/* 450 */     switch (arrayOfEntry.length) {
/*     */       case 0:
/* 452 */         return of();
/*     */       case 1:
/* 454 */         onlyEntry = arrayOfEntry[0];
/* 455 */         return of(onlyEntry.getKey(), onlyEntry.getValue());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 461 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])arrayOfEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K extends Enum<K>, V> ImmutableMap<K, V> copyOfEnumMap(EnumMap<K, ? extends V> original) {
/* 467 */     EnumMap<K, V> copy = new EnumMap<>(original);
/* 468 */     for (Map.Entry<?, ?> entry : copy.entrySet()) {
/* 469 */       CollectPreconditions.checkEntryNotNull(entry.getKey(), entry.getValue());
/*     */     }
/* 471 */     return ImmutableEnumMap.asImmutable(copy); } @LazyInit @RetainedWith
/*     */   private transient ImmutableSet<Map.Entry<K, V>> entrySet; @LazyInit
/*     */   @RetainedWith
/* 474 */   private transient ImmutableSet<K> keySet; static final Map.Entry<?, ?>[] EMPTY_ENTRY_ARRAY = (Map.Entry<?, ?>[])new Map.Entry[0]; @LazyInit
/*     */   @RetainedWith
/*     */   private transient ImmutableCollection<V> values; @LazyInit
/*     */   private transient ImmutableSetMultimap<K, V> multimapView;
/*     */   
/*     */   static abstract class IteratorBasedImmutableMap<K, V> extends ImmutableMap<K, V> { Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 480 */       return Spliterators.spliterator(
/* 481 */           entryIterator(), 
/* 482 */           size(), 1297);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     ImmutableSet<K> createKeySet() {
/* 488 */       return new ImmutableMapKeySet<>(this);
/*     */     }
/*     */     
/*     */     ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/*     */       class EntrySetImpl
/*     */         extends ImmutableMapEntrySet<K, V>
/*     */       {
/*     */         ImmutableMap<K, V> map() {
/* 496 */           return ImmutableMap.IteratorBasedImmutableMap.this;
/*     */         }
/*     */ 
/*     */         
/*     */         public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/* 501 */           return ImmutableMap.IteratorBasedImmutableMap.this.entryIterator();
/*     */         }
/*     */       };
/* 504 */       return new EntrySetImpl();
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableCollection<V> createValues() {
/* 509 */       return new ImmutableMapValues<>(this);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract UnmodifiableIterator<Map.Entry<K, V>> entryIterator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final V put(K k, V v) {
/* 525 */     throw new UnsupportedOperationException();
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
/*     */   @CanIgnoreReturnValue
/*     */   public final V putIfAbsent(K key, V value) {
/* 538 */     throw new UnsupportedOperationException();
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
/*     */   public final boolean replace(K key, V oldValue, V newValue) {
/* 550 */     throw new UnsupportedOperationException();
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
/*     */   public final V replace(K key, V value) {
/* 562 */     throw new UnsupportedOperationException();
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
/*     */   public final V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
/* 574 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   public final V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
/* 587 */     throw new UnsupportedOperationException();
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
/*     */   public final V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
/* 599 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   public final V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
/* 612 */     throw new UnsupportedOperationException();
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
/*     */   public final void putAll(Map<? extends K, ? extends V> map) {
/* 624 */     throw new UnsupportedOperationException();
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
/*     */   public final void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/* 636 */     throw new UnsupportedOperationException();
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
/*     */   public final V remove(Object o) {
/* 648 */     throw new UnsupportedOperationException();
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
/*     */   public final boolean remove(Object key, Object value) {
/* 660 */     throw new UnsupportedOperationException();
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
/*     */   public final void clear() {
/* 672 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 677 */     return (size() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 682 */     return (get(key) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 687 */     return values().contains(value);
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
/*     */   public final V getOrDefault(Object key, V defaultValue) {
/* 701 */     V result = get(key);
/* 702 */     return (result != null) ? result : defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<Map.Entry<K, V>> entrySet() {
/* 713 */     ImmutableSet<Map.Entry<K, V>> result = this.entrySet;
/* 714 */     return (result == null) ? (this.entrySet = createEntrySet()) : result;
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
/*     */   public ImmutableSet<K> keySet() {
/* 727 */     ImmutableSet<K> result = this.keySet;
/* 728 */     return (result == null) ? (this.keySet = createKeySet()) : result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   UnmodifiableIterator<K> keyIterator() {
/* 739 */     final UnmodifiableIterator<Map.Entry<K, V>> entryIterator = entrySet().iterator();
/* 740 */     return new UnmodifiableIterator<K>(this)
/*     */       {
/*     */         public boolean hasNext() {
/* 743 */           return entryIterator.hasNext();
/*     */         }
/*     */ 
/*     */         
/*     */         public K next() {
/* 748 */           return (K)((Map.Entry)entryIterator.next()).getKey();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   Spliterator<K> keySpliterator() {
/* 754 */     return CollectSpliterators.map(entrySet().spliterator(), Map.Entry::getKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableCollection<V> values() {
/* 765 */     ImmutableCollection<V> result = this.values;
/* 766 */     return (result == null) ? (this.values = createValues()) : result;
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
/*     */   public ImmutableSetMultimap<K, V> asMultimap() {
/* 785 */     if (isEmpty()) {
/* 786 */       return ImmutableSetMultimap.of();
/*     */     }
/* 788 */     ImmutableSetMultimap<K, V> result = this.multimapView;
/* 789 */     return (result == null) ? (
/* 790 */       this
/* 791 */       .multimapView = new ImmutableSetMultimap<>(new MapViewOfValuesAsSingletonSets(), size(), null)) : 
/* 792 */       result;
/*     */   }
/*     */   
/*     */   private final class MapViewOfValuesAsSingletonSets
/*     */     extends IteratorBasedImmutableMap<K, ImmutableSet<V>>
/*     */   {
/*     */     private MapViewOfValuesAsSingletonSets() {}
/*     */     
/*     */     public int size() {
/* 801 */       return ImmutableMap.this.size();
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet<K> createKeySet() {
/* 806 */       return ImmutableMap.this.keySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 811 */       return ImmutableMap.this.containsKey(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableSet<V> get(Object key) {
/* 816 */       V outerValue = (V)ImmutableMap.this.get(key);
/* 817 */       return (outerValue == null) ? null : ImmutableSet.<V>of(outerValue);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 822 */       return ImmutableMap.this.isPartialView();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 828 */       return ImmutableMap.this.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isHashCodeFast() {
/* 833 */       return ImmutableMap.this.isHashCodeFast();
/*     */     }
/*     */ 
/*     */     
/*     */     UnmodifiableIterator<Map.Entry<K, ImmutableSet<V>>> entryIterator() {
/* 838 */       final Iterator<Map.Entry<K, V>> backingIterator = ImmutableMap.this.entrySet().iterator();
/* 839 */       return new UnmodifiableIterator<Map.Entry<K, ImmutableSet<V>>>(this)
/*     */         {
/*     */           public boolean hasNext() {
/* 842 */             return backingIterator.hasNext();
/*     */           }
/*     */ 
/*     */           
/*     */           public Map.Entry<K, ImmutableSet<V>> next() {
/* 847 */             final Map.Entry<K, V> backingEntry = backingIterator.next();
/* 848 */             return (Map.Entry)new AbstractMapEntry<K, ImmutableSet<ImmutableSet<V>>>(this)
/*     */               {
/*     */                 public K getKey() {
/* 851 */                   return (K)backingEntry.getKey();
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public ImmutableSet<V> getValue() {
/* 856 */                   return ImmutableSet.of((V)backingEntry.getValue());
/*     */                 }
/*     */               };
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 866 */     return Maps.equalsImpl(this, object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 873 */     return Sets.hashCodeImpl(entrySet());
/*     */   }
/*     */   
/*     */   boolean isHashCodeFast() {
/* 877 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 882 */     return Maps.toStringImpl(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class SerializedForm<K, V>
/*     */     implements Serializable
/*     */   {
/*     */     private static final boolean USE_LEGACY_SERIALIZATION = true;
/*     */ 
/*     */     
/*     */     private final Object keys;
/*     */ 
/*     */     
/*     */     private final Object values;
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     
/*     */     SerializedForm(ImmutableMap<K, V> map) {
/* 903 */       Object[] keys = new Object[map.size()];
/* 904 */       Object[] values = new Object[map.size()];
/* 905 */       int i = 0;
/* 906 */       for (UnmodifiableIterator<Map.Entry<?, ?>> unmodifiableIterator = map.entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<?, ?> entry = unmodifiableIterator.next();
/* 907 */         keys[i] = entry.getKey();
/* 908 */         values[i] = entry.getValue();
/* 909 */         i++; }
/*     */       
/* 911 */       this.keys = keys;
/* 912 */       this.values = values;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final Object readResolve() {
/* 921 */       if (!(this.keys instanceof ImmutableSet)) {
/* 922 */         return legacyReadResolve();
/*     */       }
/*     */       
/* 925 */       ImmutableSet<K> keySet = (ImmutableSet<K>)this.keys;
/* 926 */       ImmutableCollection<V> values = (ImmutableCollection<V>)this.values;
/*     */       
/* 928 */       ImmutableMap.Builder<K, V> builder = makeBuilder(keySet.size());
/*     */       
/* 930 */       UnmodifiableIterator<K> keyIter = keySet.iterator();
/* 931 */       UnmodifiableIterator<V> valueIter = values.iterator();
/*     */       
/* 933 */       while (keyIter.hasNext()) {
/* 934 */         builder.put(keyIter.next(), valueIter.next());
/*     */       }
/*     */       
/* 937 */       return builder.build();
/*     */     }
/*     */ 
/*     */     
/*     */     final Object legacyReadResolve() {
/* 942 */       K[] keys = (K[])this.keys;
/* 943 */       V[] values = (V[])this.values;
/*     */       
/* 945 */       ImmutableMap.Builder<K, V> builder = makeBuilder(keys.length);
/*     */       
/* 947 */       for (int i = 0; i < keys.length; i++) {
/* 948 */         builder.put(keys[i], values[i]);
/*     */       }
/* 950 */       return builder.build();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ImmutableMap.Builder<K, V> makeBuilder(int size) {
/* 957 */       return new ImmutableMap.Builder<>(size);
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
/*     */   Object writeReplace() {
/* 969 */     return new SerializedForm<>(this);
/*     */   }
/*     */   
/*     */   public abstract V get(Object paramObject);
/*     */   
/*     */   abstract ImmutableSet<Map.Entry<K, V>> createEntrySet();
/*     */   
/*     */   abstract ImmutableSet<K> createKeySet();
/*     */   
/*     */   abstract ImmutableCollection<V> createValues();
/*     */   
/*     */   abstract boolean isPartialView();
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\ImmutableMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */