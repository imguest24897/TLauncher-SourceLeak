/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class ImmutableSetMultimap<K, V>
/*     */   extends ImmutableMultimap<K, V>
/*     */   implements SetMultimap<K, V>
/*     */ {
/*     */   private final transient ImmutableSet<V> emptySet;
/*     */   @LazyInit
/*     */   @RetainedWith
/*     */   private transient ImmutableSetMultimap<V, K> inverse;
/*     */   @LazyInit
/*     */   @RetainedWith
/*     */   private transient ImmutableSet<Map.Entry<K, V>> entries;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> toImmutableSetMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*  88 */     return CollectCollectors.toImmutableSetMultimap(keyFunction, valueFunction);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> flatteningToImmutableSetMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valuesFunction) {
/* 136 */     return CollectCollectors.flatteningToImmutableSetMultimap(keyFunction, valuesFunction);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of() {
/* 143 */     return EmptyImmutableSetMultimap.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1) {
/* 148 */     Builder<K, V> builder = builder();
/* 149 */     builder.put(k1, v1);
/* 150 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2) {
/* 158 */     Builder<K, V> builder = builder();
/* 159 */     builder.put(k1, v1);
/* 160 */     builder.put(k2, v2);
/* 161 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/* 169 */     Builder<K, V> builder = builder();
/* 170 */     builder.put(k1, v1);
/* 171 */     builder.put(k2, v2);
/* 172 */     builder.put(k3, v3);
/* 173 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 182 */     Builder<K, V> builder = builder();
/* 183 */     builder.put(k1, v1);
/* 184 */     builder.put(k2, v2);
/* 185 */     builder.put(k3, v3);
/* 186 */     builder.put(k4, v4);
/* 187 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 196 */     Builder<K, V> builder = builder();
/* 197 */     builder.put(k1, v1);
/* 198 */     builder.put(k2, v2);
/* 199 */     builder.put(k3, v3);
/* 200 */     builder.put(k4, v4);
/* 201 */     builder.put(k5, v5);
/* 202 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 209 */     return new Builder<>();
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Builder<K, V>
/*     */     extends ImmutableMultimap.Builder<K, V>
/*     */   {
/*     */     Collection<V> newMutableValueCollection() {
/* 242 */       return Platform.preservesInsertionOrderOnAddsSet();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(K key, V value) {
/* 249 */       super.put(key, value);
/* 250 */       return this;
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
/* 261 */       super.put(entry);
/* 262 */       return this;
/*     */     }
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
/* 274 */       super.putAll(entries);
/* 275 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
/* 281 */       super.putAll(key, values);
/* 282 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(K key, V... values) {
/* 288 */       return putAll(key, Arrays.asList(values));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
/* 295 */       for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : (Iterable<Map.Entry<? extends K, ? extends Collection<? extends V>>>)multimap.asMap().entrySet()) {
/* 296 */         putAll(entry.getKey(), entry.getValue());
/*     */       }
/* 298 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<K, V> combine(ImmutableMultimap.Builder<K, V> other) {
/* 304 */       super.combine(other);
/* 305 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> orderKeysBy(Comparator<? super K> keyComparator) {
/* 316 */       super.orderKeysBy(keyComparator);
/* 317 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
/* 334 */       super.orderValuesBy(valueComparator);
/* 335 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableSetMultimap<K, V> build() {
/* 341 */       Collection<Map.Entry<K, Collection<V>>> mapEntries = this.builderMap.entrySet();
/* 342 */       if (this.keyComparator != null) {
/* 343 */         mapEntries = Ordering.<K>from(this.keyComparator).onKeys().immutableSortedCopy(mapEntries);
/*     */       }
/* 345 */       return ImmutableSetMultimap.fromMapEntries(mapEntries, this.valueComparator);
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
/*     */   public static <K, V> ImmutableSetMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
/* 363 */     return copyOf(multimap, (Comparator<? super V>)null);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <K, V> ImmutableSetMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap, Comparator<? super V> valueComparator) {
/* 368 */     Preconditions.checkNotNull(multimap);
/* 369 */     if (multimap.isEmpty() && valueComparator == null) {
/* 370 */       return of();
/*     */     }
/*     */     
/* 373 */     if (multimap instanceof ImmutableSetMultimap) {
/*     */       
/* 375 */       ImmutableSetMultimap<K, V> kvMultimap = (ImmutableSetMultimap)multimap;
/* 376 */       if (!kvMultimap.isPartialView()) {
/* 377 */         return kvMultimap;
/*     */       }
/*     */     } 
/*     */     
/* 381 */     return fromMapEntries(multimap.asMap().entrySet(), valueComparator);
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
/*     */   public static <K, V> ImmutableSetMultimap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 396 */     return (new Builder<>()).putAll(entries).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> ImmutableSetMultimap<K, V> fromMapEntries(Collection<? extends Map.Entry<? extends K, ? extends Collection<? extends V>>> mapEntries, Comparator<? super V> valueComparator) {
/* 403 */     if (mapEntries.isEmpty()) {
/* 404 */       return of();
/*     */     }
/*     */     
/* 407 */     ImmutableMap.Builder<K, ImmutableSet<V>> builder = new ImmutableMap.Builder<>(mapEntries.size());
/* 408 */     int size = 0;
/*     */     
/* 410 */     for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : mapEntries) {
/* 411 */       K key = entry.getKey();
/* 412 */       Collection<? extends V> values = entry.getValue();
/* 413 */       ImmutableSet<V> set = valueSet(valueComparator, values);
/* 414 */       if (!set.isEmpty()) {
/* 415 */         builder.put(key, set);
/* 416 */         size += set.size();
/*     */       } 
/*     */     } 
/*     */     
/* 420 */     return new ImmutableSetMultimap<>(builder.build(), size, valueComparator);
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
/*     */   ImmutableSetMultimap(ImmutableMap<K, ImmutableSet<V>> map, int size, Comparator<? super V> valueComparator) {
/* 433 */     super((ImmutableMap)map, size);
/* 434 */     this.emptySet = emptySet(valueComparator);
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
/*     */   public ImmutableSet<V> get(K key) {
/* 447 */     ImmutableSet<V> set = (ImmutableSet<V>)this.map.get(key);
/* 448 */     return (ImmutableSet<V>)MoreObjects.firstNonNull(set, this.emptySet);
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
/*     */   public ImmutableSetMultimap<V, K> inverse() {
/* 462 */     ImmutableSetMultimap<V, K> result = this.inverse;
/* 463 */     return (result == null) ? (this.inverse = invert()) : result;
/*     */   }
/*     */   
/*     */   private ImmutableSetMultimap<V, K> invert() {
/* 467 */     Builder<V, K> builder = builder();
/* 468 */     for (UnmodifiableIterator<Map.Entry<K, V>> unmodifiableIterator = entries().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<K, V> entry = unmodifiableIterator.next();
/* 469 */       builder.put(entry.getValue(), entry.getKey()); }
/*     */     
/* 471 */     ImmutableSetMultimap<V, K> invertedMultimap = builder.build();
/* 472 */     invertedMultimap.inverse = this;
/* 473 */     return invertedMultimap;
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
/*     */   public ImmutableSet<V> removeAll(Object key) {
/* 486 */     throw new UnsupportedOperationException();
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
/*     */   public ImmutableSet<V> replaceValues(K key, Iterable<? extends V> values) {
/* 499 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<Map.Entry<K, V>> entries() {
/* 510 */     ImmutableSet<Map.Entry<K, V>> result = this.entries;
/* 511 */     return (result == null) ? (this.entries = new EntrySet<>(this)) : result;
/*     */   }
/*     */   
/*     */   private static final class EntrySet<K, V> extends ImmutableSet<Map.Entry<K, V>> { @Weak
/*     */     private final transient ImmutableSetMultimap<K, V> multimap;
/*     */     
/*     */     EntrySet(ImmutableSetMultimap<K, V> multimap) {
/* 518 */       this.multimap = multimap;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object object) {
/* 523 */       if (object instanceof Map.Entry) {
/* 524 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/* 525 */         return this.multimap.containsEntry(entry.getKey(), entry.getValue());
/*     */       } 
/* 527 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 532 */       return this.multimap.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/* 537 */       return this.multimap.entryIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 542 */       return false;
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <V> ImmutableSet<V> valueSet(Comparator<? super V> valueComparator, Collection<? extends V> values) {
/* 548 */     return (valueComparator == null) ? 
/* 549 */       ImmutableSet.<V>copyOf(values) : 
/* 550 */       ImmutableSortedSet.<V>copyOf(valueComparator, values);
/*     */   }
/*     */   
/*     */   private static <V> ImmutableSet<V> emptySet(Comparator<? super V> valueComparator) {
/* 554 */     return (valueComparator == null) ? 
/* 555 */       ImmutableSet.<V>of() : 
/* 556 */       ImmutableSortedSet.<V>emptySet(valueComparator);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <V> ImmutableSet.Builder<V> valuesBuilder(Comparator<? super V> valueComparator) {
/* 561 */     return (valueComparator == null) ? 
/* 562 */       new ImmutableSet.Builder<>() : 
/* 563 */       new ImmutableSortedSet.Builder<>(valueComparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 572 */     stream.defaultWriteObject();
/* 573 */     stream.writeObject(valueComparator());
/* 574 */     Serialization.writeMultimap(this, stream);
/*     */   }
/*     */ 
/*     */   
/*     */   Comparator<? super V> valueComparator() {
/* 579 */     return (this.emptySet instanceof ImmutableSortedSet) ? (
/* 580 */       (ImmutableSortedSet<V>)this.emptySet).comparator() : 
/* 581 */       null;
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static final class SetFieldSettersHolder
/*     */   {
/* 587 */     static final Serialization.FieldSetter<ImmutableSetMultimap> EMPTY_SET_FIELD_SETTER = Serialization.getFieldSetter(ImmutableSetMultimap.class, "emptySet");
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*     */     ImmutableMap<Object, ImmutableSet<Object>> tmpMap;
/* 594 */     stream.defaultReadObject();
/* 595 */     Comparator<Object> valueComparator = (Comparator<Object>)stream.readObject();
/* 596 */     int keyCount = stream.readInt();
/* 597 */     if (keyCount < 0) {
/* 598 */       throw new InvalidObjectException((new StringBuilder(29)).append("Invalid key count ").append(keyCount).toString());
/*     */     }
/* 600 */     ImmutableMap.Builder<Object, ImmutableSet<Object>> builder = ImmutableMap.builder();
/* 601 */     int tmpSize = 0;
/*     */     
/* 603 */     for (int i = 0; i < keyCount; i++) {
/* 604 */       Object key = stream.readObject();
/* 605 */       int valueCount = stream.readInt();
/* 606 */       if (valueCount <= 0) {
/* 607 */         throw new InvalidObjectException((new StringBuilder(31)).append("Invalid value count ").append(valueCount).toString());
/*     */       }
/*     */       
/* 610 */       ImmutableSet.Builder<Object> valuesBuilder = valuesBuilder(valueComparator);
/* 611 */       for (int j = 0; j < valueCount; j++) {
/* 612 */         valuesBuilder.add(stream.readObject());
/*     */       }
/* 614 */       ImmutableSet<Object> valueSet = valuesBuilder.build();
/* 615 */       if (valueSet.size() != valueCount) {
/* 616 */         String str = String.valueOf(key); throw new InvalidObjectException((new StringBuilder(40 + String.valueOf(str).length())).append("Duplicate key-value pairs exist for key ").append(str).toString());
/*     */       } 
/* 618 */       builder.put(key, valueSet);
/* 619 */       tmpSize += valueCount;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 624 */       tmpMap = builder.build();
/* 625 */     } catch (IllegalArgumentException e) {
/* 626 */       throw (InvalidObjectException)(new InvalidObjectException(e.getMessage())).initCause(e);
/*     */     } 
/*     */     
/* 629 */     ImmutableMultimap.FieldSettersHolder.MAP_FIELD_SETTER.set(this, tmpMap);
/* 630 */     ImmutableMultimap.FieldSettersHolder.SIZE_FIELD_SETTER.set(this, tmpSize);
/* 631 */     SetFieldSettersHolder.EMPTY_SET_FIELD_SETTER.set(this, emptySet(valueComparator));
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\ImmutableSetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */