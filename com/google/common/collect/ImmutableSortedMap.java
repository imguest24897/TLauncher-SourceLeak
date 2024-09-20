/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BinaryOperator;
/*     */ import java.util.function.Consumer;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public final class ImmutableSortedMap<K, V>
/*     */   extends ImmutableSortedMapFauxverideShim<K, V>
/*     */   implements NavigableMap<K, V>
/*     */ {
/*     */   public static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(Comparator<? super K> comparator, Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*  80 */     return CollectCollectors.toImmutableSortedMap(comparator, keyFunction, valueFunction);
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
/*     */   public static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(Comparator<? super K> comparator, Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/* 100 */     return CollectCollectors.toImmutableSortedMap(comparator, keyFunction, valueFunction, mergeFunction);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
/*     */   
/* 110 */   private static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP = new ImmutableSortedMap(
/*     */       
/* 112 */       ImmutableSortedSet.emptySet(Ordering.natural()), ImmutableList.of());
/*     */   
/*     */   static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<? super K> comparator) {
/* 115 */     if (Ordering.<Comparable>natural().equals(comparator)) {
/* 116 */       return of();
/*     */     }
/* 118 */     return new ImmutableSortedMap<>(
/* 119 */         ImmutableSortedSet.emptySet(comparator), ImmutableList.of());
/*     */   }
/*     */   
/*     */   private final transient RegularImmutableSortedSet<K> keySet;
/*     */   private final transient ImmutableList<V> valueList;
/*     */   private transient ImmutableSortedMap<K, V> descendingMap;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K, V> ImmutableSortedMap<K, V> of() {
/* 128 */     return (ImmutableSortedMap)NATURAL_EMPTY_MAP;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1) {
/* 133 */     return of(Ordering.natural(), k1, v1);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <K, V> ImmutableSortedMap<K, V> of(Comparator<? super K> comparator, K k1, V v1) {
/* 138 */     return new ImmutableSortedMap<>(new RegularImmutableSortedSet<>(
/* 139 */           ImmutableList.of(k1), (Comparator<? super K>)Preconditions.checkNotNull(comparator)), 
/* 140 */         ImmutableList.of(v1));
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
/*     */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2) {
/* 152 */     return ofEntries((Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2) });
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
/*     */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/* 164 */     return ofEntries((Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3) });
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
/*     */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 176 */     return ofEntries((Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4) });
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
/*     */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 188 */     return ofEntries((Map.Entry<K, V>[])new Map.Entry[] {
/* 189 */           entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5)
/*     */         });
/*     */   }
/*     */   
/*     */   private static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> ofEntries(Map.Entry<K, V>... entries) {
/* 194 */     return fromEntries(Ordering.natural(), false, entries, entries.length);
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
/*     */   public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
/* 216 */     Ordering<K> naturalOrder = (Ordering)NATURAL_ORDER;
/* 217 */     return copyOfInternal(map, naturalOrder);
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
/*     */   public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
/* 233 */     return copyOfInternal(map, (Comparator<? super K>)Preconditions.checkNotNull(comparator));
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
/*     */   @Beta
/*     */   public static <K, V> ImmutableSortedMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 253 */     Ordering<K> naturalOrder = (Ordering)NATURAL_ORDER;
/* 254 */     return copyOf(entries, naturalOrder);
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
/*     */   @Beta
/*     */   public static <K, V> ImmutableSortedMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries, Comparator<? super K> comparator) {
/* 269 */     return fromEntries((Comparator<? super K>)Preconditions.checkNotNull(comparator), false, entries);
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
/*     */   public static <K, V> ImmutableSortedMap<K, V> copyOfSorted(SortedMap<K, ? extends V> map) {
/*     */     Comparator<Comparable> comparator1;
/* 284 */     Comparator<? super K> comparator = map.comparator();
/* 285 */     if (comparator == null)
/*     */     {
/*     */       
/* 288 */       comparator1 = NATURAL_ORDER;
/*     */     }
/* 290 */     if (map instanceof ImmutableSortedMap) {
/*     */ 
/*     */ 
/*     */       
/* 294 */       ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap)map;
/* 295 */       if (!kvMap.isPartialView()) {
/* 296 */         return kvMap;
/*     */       }
/*     */     } 
/* 299 */     return fromEntries((Comparator)comparator1, true, map.entrySet());
/*     */   }
/*     */ 
/*     */   
/*     */   private static <K, V> ImmutableSortedMap<K, V> copyOfInternal(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
/* 304 */     boolean sameComparator = false;
/* 305 */     if (map instanceof SortedMap) {
/* 306 */       SortedMap<?, ?> sortedMap = (SortedMap<?, ?>)map;
/* 307 */       Comparator<?> comparator2 = sortedMap.comparator();
/*     */       
/* 309 */       sameComparator = (comparator2 == null) ? ((comparator == NATURAL_ORDER)) : comparator.equals(comparator2);
/*     */     } 
/*     */     
/* 312 */     if (sameComparator && map instanceof ImmutableSortedMap) {
/*     */ 
/*     */ 
/*     */       
/* 316 */       ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap)map;
/* 317 */       if (!kvMap.isPartialView()) {
/* 318 */         return kvMap;
/*     */       }
/*     */     } 
/* 321 */     return fromEntries(comparator, sameComparator, map.entrySet());
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
/*     */   private static <K, V> ImmutableSortedMap<K, V> fromEntries(Comparator<? super K> comparator, boolean sameComparator, Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 336 */     Map.Entry[] arrayOfEntry = Iterables.<Map.Entry>toArray((Iterable)entries, (Map.Entry[])EMPTY_ENTRY_ARRAY);
/* 337 */     return fromEntries(comparator, sameComparator, (Map.Entry<K, V>[])arrayOfEntry, arrayOfEntry.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K, V> ImmutableSortedMap<K, V> fromEntries(final Comparator<? super K> comparator, boolean sameComparator, Map.Entry<K, V>[] entryArray, int size) {
/* 345 */     switch (size) {
/*     */       case 0:
/* 347 */         return emptyMap(comparator);
/*     */       case 1:
/* 349 */         return of(comparator, entryArray[0]
/* 350 */             .getKey(), entryArray[0].getValue());
/*     */     } 
/* 352 */     Object[] keys = new Object[size];
/* 353 */     Object[] values = new Object[size];
/* 354 */     if (sameComparator) {
/*     */       
/* 356 */       for (int i = 0; i < size; i++) {
/* 357 */         Object key = entryArray[i].getKey();
/* 358 */         Object value = entryArray[i].getValue();
/* 359 */         CollectPreconditions.checkEntryNotNull(key, value);
/* 360 */         keys[i] = key;
/* 361 */         values[i] = value;
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 367 */       Arrays.sort(entryArray, 0, size, new Comparator<Map.Entry<K, V>>()
/*     */           {
/*     */ 
/*     */ 
/*     */             
/*     */             public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2)
/*     */             {
/* 374 */               return comparator.compare(e1.getKey(), e2.getKey());
/*     */             }
/*     */           });
/* 377 */       K prevKey = entryArray[0].getKey();
/* 378 */       keys[0] = prevKey;
/* 379 */       values[0] = entryArray[0].getValue();
/* 380 */       CollectPreconditions.checkEntryNotNull(keys[0], values[0]);
/* 381 */       for (int i = 1; i < size; i++) {
/* 382 */         K key = entryArray[i].getKey();
/* 383 */         V value = entryArray[i].getValue();
/* 384 */         CollectPreconditions.checkEntryNotNull(key, value);
/* 385 */         keys[i] = key;
/* 386 */         values[i] = value;
/* 387 */         checkNoConflict(
/* 388 */             (comparator.compare(prevKey, key) != 0), "key", entryArray[i - 1], entryArray[i]);
/* 389 */         prevKey = key;
/*     */       } 
/*     */     } 
/* 392 */     return new ImmutableSortedMap<>(new RegularImmutableSortedSet<>(new RegularImmutableList<>(keys), comparator), new RegularImmutableList<>(values));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<?>, V> Builder<K, V> naturalOrder() {
/* 403 */     return new Builder<>(Ordering.natural());
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
/*     */   public static <K, V> Builder<K, V> orderedBy(Comparator<K> comparator) {
/* 415 */     return new Builder<>(comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<?>, V> Builder<K, V> reverseOrder() {
/* 423 */     return new Builder<>(Ordering.<Comparable>natural().reverse());
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
/*     */   public static class Builder<K, V>
/*     */     extends ImmutableMap.Builder<K, V>
/*     */   {
/*     */     private final Comparator<? super K> comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder(Comparator<? super K> comparator) {
/* 456 */       this.comparator = (Comparator<? super K>)Preconditions.checkNotNull(comparator);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(K key, V value) {
/* 467 */       super.put(key, value);
/* 468 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
/* 481 */       super.put(entry);
/* 482 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
/* 495 */       super.putAll(map);
/* 496 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     @Beta
/*     */     public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 511 */       super.putAll(entries);
/* 512 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     @CanIgnoreReturnValue
/*     */     @Beta
/*     */     public Builder<K, V> orderEntriesByValue(Comparator<? super V> valueComparator) {
/* 526 */       throw new UnsupportedOperationException("Not available on ImmutableSortedMap.Builder");
/*     */     }
/*     */ 
/*     */     
/*     */     Builder<K, V> combine(ImmutableMap.Builder<K, V> other) {
/* 531 */       super.combine(other);
/* 532 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableSortedMap<K, V> build() {
/* 543 */       switch (this.size) {
/*     */         case 0:
/* 545 */           return ImmutableSortedMap.emptyMap(this.comparator);
/*     */         case 1:
/* 547 */           return ImmutableSortedMap.of(this.comparator, this.entries[0].getKey(), this.entries[0].getValue());
/*     */       } 
/* 549 */       return ImmutableSortedMap.fromEntries(this.comparator, false, this.entries, this.size);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableSortedMap(RegularImmutableSortedSet<K> keySet, ImmutableList<V> valueList) {
/* 559 */     this(keySet, valueList, (ImmutableSortedMap<K, V>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableSortedMap(RegularImmutableSortedSet<K> keySet, ImmutableList<V> valueList, ImmutableSortedMap<K, V> descendingMap) {
/* 566 */     this.keySet = keySet;
/* 567 */     this.valueList = valueList;
/* 568 */     this.descendingMap = descendingMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 573 */     return this.valueList.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 578 */     Preconditions.checkNotNull(action);
/* 579 */     ImmutableList<K> keyList = this.keySet.asList();
/* 580 */     for (int i = 0; i < size(); i++) {
/* 581 */       action.accept(keyList.get(i), this.valueList.get(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/* 587 */     int index = this.keySet.indexOf(key);
/* 588 */     return (index == -1) ? null : this.valueList.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 593 */     return (this.keySet.isPartialView() || this.valueList.isPartialView());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<Map.Entry<K, V>> entrySet() {
/* 599 */     return super.entrySet();
/*     */   }
/*     */   
/*     */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/*     */     class EntrySet
/*     */       extends ImmutableMapEntrySet<K, V>
/*     */     {
/*     */       public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/* 607 */         return asList().iterator();
/*     */       }
/*     */ 
/*     */       
/*     */       public Spliterator<Map.Entry<K, V>> spliterator() {
/* 612 */         return asList().spliterator();
/*     */       }
/*     */ 
/*     */       
/*     */       public void forEach(Consumer<? super Map.Entry<K, V>> action) {
/* 617 */         asList().forEach(action);
/*     */       }
/*     */ 
/*     */       
/*     */       ImmutableList<Map.Entry<K, V>> createAsList() {
/* 622 */         return new ImmutableAsList<Map.Entry<K, V>>()
/*     */           {
/*     */             public Map.Entry<K, V> get(int index) {
/* 625 */               return new AbstractMap.SimpleImmutableEntry<>(ImmutableSortedMap.this
/* 626 */                   .keySet.asList().get(index), (V)ImmutableSortedMap.this.valueList.get(index));
/*     */             }
/*     */ 
/*     */             
/*     */             public Spliterator<Map.Entry<K, V>> spliterator() {
/* 631 */               return CollectSpliterators.indexed(
/* 632 */                   size(), 1297, this::get);
/*     */             }
/*     */ 
/*     */             
/*     */             ImmutableCollection<Map.Entry<K, V>> delegateCollection() {
/* 637 */               return ImmutableSortedMap.EntrySet.this;
/*     */             }
/*     */           };
/*     */       }
/*     */ 
/*     */       
/*     */       ImmutableMap<K, V> map() {
/* 644 */         return ImmutableSortedMap.this;
/*     */       }
/*     */     };
/* 647 */     return isEmpty() ? ImmutableSet.<Map.Entry<K, V>>of() : new EntrySet();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSortedSet<K> keySet() {
/* 653 */     return this.keySet;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<K> createKeySet() {
/* 658 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableCollection<V> values() {
/* 667 */     return this.valueList;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableCollection<V> createValues() {
/* 672 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<? super K> comparator() {
/* 682 */     return keySet().comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public K firstKey() {
/* 687 */     return keySet().first();
/*     */   }
/*     */ 
/*     */   
/*     */   public K lastKey() {
/* 692 */     return keySet().last();
/*     */   }
/*     */   
/*     */   private ImmutableSortedMap<K, V> getSubMap(int fromIndex, int toIndex) {
/* 696 */     if (fromIndex == 0 && toIndex == size())
/* 697 */       return this; 
/* 698 */     if (fromIndex == toIndex) {
/* 699 */       return emptyMap(comparator());
/*     */     }
/* 701 */     return new ImmutableSortedMap(this.keySet
/* 702 */         .getSubSet(fromIndex, toIndex), this.valueList.subList(fromIndex, toIndex));
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
/*     */   public ImmutableSortedMap<K, V> headMap(K toKey) {
/* 717 */     return headMap(toKey, false);
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
/*     */   public ImmutableSortedMap<K, V> headMap(K toKey, boolean inclusive) {
/* 733 */     return getSubMap(0, this.keySet.headIndex((K)Preconditions.checkNotNull(toKey), inclusive));
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
/*     */   public ImmutableSortedMap<K, V> subMap(K fromKey, K toKey) {
/* 748 */     return subMap(fromKey, true, toKey, false);
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
/*     */   public ImmutableSortedMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 767 */     Preconditions.checkNotNull(fromKey);
/* 768 */     Preconditions.checkNotNull(toKey);
/* 769 */     Preconditions.checkArgument(
/* 770 */         (comparator().compare(fromKey, toKey) <= 0), "expected fromKey <= toKey but %s > %s", fromKey, toKey);
/*     */ 
/*     */ 
/*     */     
/* 774 */     return headMap(toKey, toInclusive).tailMap(fromKey, fromInclusive);
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
/*     */   public ImmutableSortedMap<K, V> tailMap(K fromKey) {
/* 788 */     return tailMap(fromKey, true);
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
/*     */   public ImmutableSortedMap<K, V> tailMap(K fromKey, boolean inclusive) {
/* 804 */     return getSubMap(this.keySet.tailIndex((K)Preconditions.checkNotNull(fromKey), inclusive), size());
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> lowerEntry(K key) {
/* 809 */     return headMap(key, false).lastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public K lowerKey(K key) {
/* 814 */     return Maps.keyOrNull(lowerEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> floorEntry(K key) {
/* 819 */     return headMap(key, true).lastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public K floorKey(K key) {
/* 824 */     return Maps.keyOrNull(floorEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> ceilingEntry(K key) {
/* 829 */     return tailMap(key, true).firstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public K ceilingKey(K key) {
/* 834 */     return Maps.keyOrNull(ceilingEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> higherEntry(K key) {
/* 839 */     return tailMap(key, false).firstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public K higherKey(K key) {
/* 844 */     return Maps.keyOrNull(higherEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> firstEntry() {
/* 849 */     return isEmpty() ? null : entrySet().asList().get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> lastEntry() {
/* 854 */     return isEmpty() ? null : entrySet().asList().get(size() - 1);
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
/*     */   public final Map.Entry<K, V> pollFirstEntry() {
/* 867 */     throw new UnsupportedOperationException();
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
/*     */   public final Map.Entry<K, V> pollLastEntry() {
/* 880 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSortedMap<K, V> descendingMap() {
/* 887 */     ImmutableSortedMap<K, V> result = this.descendingMap;
/* 888 */     if (result == null) {
/* 889 */       if (isEmpty()) {
/* 890 */         return result = emptyMap(Ordering.from(comparator()).reverse());
/*     */       }
/* 892 */       return 
/*     */         
/* 894 */         result = new ImmutableSortedMap((RegularImmutableSortedSet<K>)this.keySet.descendingSet(), this.valueList.reverse(), this);
/*     */     } 
/*     */     
/* 897 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSortedSet<K> navigableKeySet() {
/* 902 */     return this.keySet;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSortedSet<K> descendingKeySet() {
/* 907 */     return this.keySet.descendingSet();
/*     */   }
/*     */ 
/*     */   
/*     */   private static class SerializedForm<K, V>
/*     */     extends ImmutableMap.SerializedForm<K, V>
/*     */   {
/*     */     private final Comparator<? super K> comparator;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(ImmutableSortedMap<K, V> sortedMap) {
/* 919 */       super(sortedMap);
/* 920 */       this.comparator = sortedMap.comparator();
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSortedMap.Builder<K, V> makeBuilder(int size) {
/* 925 */       return new ImmutableSortedMap.Builder<>(this.comparator);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 933 */     return new SerializedForm<>(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\ImmutableSortedMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */