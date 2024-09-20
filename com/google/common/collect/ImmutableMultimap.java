/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.DoNotMock;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.BiConsumer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ public abstract class ImmutableMultimap<K, V>
/*     */   extends BaseImmutableMultimap<K, V>
/*     */   implements Serializable
/*     */ {
/*     */   final transient ImmutableMap<K, ? extends ImmutableCollection<V>> map;
/*     */   final transient int size;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of() {
/*  74 */     return ImmutableListMultimap.of();
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1) {
/*  79 */     return ImmutableListMultimap.of(k1, v1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2) {
/*  84 */     return ImmutableListMultimap.of(k1, v1, k2, v2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/*  92 */     return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 100 */     return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 109 */     return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 119 */     return new Builder<>();
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
/*     */   @DoNotMock
/*     */   public static class Builder<K, V>
/*     */   {
/* 152 */     Map<K, Collection<V>> builderMap = Platform.preservesInsertionOrderOnPutsMap(); Comparator<? super K> keyComparator;
/*     */     Comparator<? super V> valueComparator;
/*     */     
/*     */     Collection<V> newMutableValueCollection() {
/* 156 */       return new ArrayList<>();
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(K key, V value) {
/* 162 */       CollectPreconditions.checkEntryNotNull(key, value);
/* 163 */       Collection<V> valueCollection = this.builderMap.get(key);
/* 164 */       if (valueCollection == null) {
/* 165 */         this.builderMap.put(key, valueCollection = newMutableValueCollection());
/*     */       }
/* 167 */       valueCollection.add(value);
/* 168 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
/* 178 */       return put(entry.getKey(), entry.getValue());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     @Beta
/*     */     public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 189 */       for (Map.Entry<? extends K, ? extends V> entry : entries) {
/* 190 */         put(entry);
/*     */       }
/* 192 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
/* 203 */       if (key == null) {
/* 204 */         String.valueOf(Iterables.toString(values)); throw new NullPointerException((String.valueOf(Iterables.toString(values)).length() != 0) ? "null key in entry: null=".concat(String.valueOf(Iterables.toString(values))) : new String("null key in entry: null="));
/*     */       } 
/* 206 */       Collection<V> valueCollection = this.builderMap.get(key);
/* 207 */       if (valueCollection != null) {
/* 208 */         for (V value : values) {
/* 209 */           CollectPreconditions.checkEntryNotNull(key, value);
/* 210 */           valueCollection.add(value);
/*     */         } 
/* 212 */         return this;
/*     */       } 
/* 214 */       Iterator<? extends V> valuesItr = values.iterator();
/* 215 */       if (!valuesItr.hasNext()) {
/* 216 */         return this;
/*     */       }
/* 218 */       valueCollection = newMutableValueCollection();
/* 219 */       while (valuesItr.hasNext()) {
/* 220 */         V value = valuesItr.next();
/* 221 */         CollectPreconditions.checkEntryNotNull(key, value);
/* 222 */         valueCollection.add(value);
/*     */       } 
/* 224 */       this.builderMap.put(key, valueCollection);
/* 225 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(K key, V... values) {
/* 236 */       return putAll(key, Arrays.asList(values));
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
/*     */     public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
/* 250 */       for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : (Iterable<Map.Entry<? extends K, ? extends Collection<? extends V>>>)multimap.asMap().entrySet()) {
/* 251 */         putAll(entry.getKey(), entry.getValue());
/*     */       }
/* 253 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> orderKeysBy(Comparator<? super K> keyComparator) {
/* 263 */       this.keyComparator = (Comparator<? super K>)Preconditions.checkNotNull(keyComparator);
/* 264 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
/* 274 */       this.valueComparator = (Comparator<? super V>)Preconditions.checkNotNull(valueComparator);
/* 275 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<K, V> combine(Builder<K, V> other) {
/* 280 */       for (Map.Entry<K, Collection<V>> entry : other.builderMap.entrySet()) {
/* 281 */         putAll(entry.getKey(), entry.getValue());
/*     */       }
/* 283 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableMultimap<K, V> build() {
/* 288 */       Collection<Map.Entry<K, Collection<V>>> mapEntries = this.builderMap.entrySet();
/* 289 */       if (this.keyComparator != null) {
/* 290 */         mapEntries = Ordering.<K>from(this.keyComparator).onKeys().immutableSortedCopy(mapEntries);
/*     */       }
/* 292 */       return ImmutableListMultimap.fromMapEntries(mapEntries, this.valueComparator);
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
/*     */   public static <K, V> ImmutableMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
/* 307 */     if (multimap instanceof ImmutableMultimap) {
/*     */       
/* 309 */       ImmutableMultimap<K, V> kvMultimap = (ImmutableMultimap)multimap;
/* 310 */       if (!kvMultimap.isPartialView()) {
/* 311 */         return kvMultimap;
/*     */       }
/*     */     } 
/* 314 */     return ImmutableListMultimap.copyOf(multimap);
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
/*     */   public static <K, V> ImmutableMultimap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 328 */     return ImmutableListMultimap.copyOf(entries);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   static class FieldSettersHolder
/*     */   {
/* 340 */     static final Serialization.FieldSetter<ImmutableMultimap> MAP_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "map");
/*     */     
/* 342 */     static final Serialization.FieldSetter<ImmutableMultimap> SIZE_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "size");
/*     */   }
/*     */   
/*     */   ImmutableMultimap(ImmutableMap<K, ? extends ImmutableCollection<V>> map, int size) {
/* 346 */     this.map = map;
/* 347 */     this.size = size;
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public ImmutableCollection<V> removeAll(Object key) {
/* 362 */     throw new UnsupportedOperationException();
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
/*     */   public ImmutableCollection<V> replaceValues(K key, Iterable<? extends V> values) {
/* 375 */     throw new UnsupportedOperationException();
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
/* 387 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public boolean put(K key, V value) {
/* 416 */     throw new UnsupportedOperationException();
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
/*     */   public boolean putAll(K key, Iterable<? extends V> values) {
/* 429 */     throw new UnsupportedOperationException();
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
/*     */   public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/* 442 */     throw new UnsupportedOperationException();
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
/*     */   public boolean remove(Object key, Object value) {
/* 455 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 465 */     return this.map.isPartialView();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 472 */     return this.map.containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 477 */     return (value != null && super.containsValue(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 482 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<K> keySet() {
/* 493 */     return this.map.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   Set<K> createKeySet() {
/* 498 */     throw new AssertionError("unreachable");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<K, Collection<V>> asMap() {
/* 508 */     return (ImmutableMap)this.map;
/*     */   }
/*     */ 
/*     */   
/*     */   Map<K, Collection<V>> createAsMap() {
/* 513 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableCollection<Map.Entry<K, V>> entries() {
/* 519 */     return (ImmutableCollection<Map.Entry<K, V>>)super.entries();
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableCollection<Map.Entry<K, V>> createEntries() {
/* 524 */     return new EntryCollection<>(this);
/*     */   }
/*     */   private static class EntryCollection<K, V> extends ImmutableCollection<Map.Entry<K, V>> { @Weak
/*     */     final ImmutableMultimap<K, V> multimap;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     EntryCollection(ImmutableMultimap<K, V> multimap) {
/* 531 */       this.multimap = multimap;
/*     */     }
/*     */ 
/*     */     
/*     */     public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/* 536 */       return this.multimap.entryIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 541 */       return this.multimap.isPartialView();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 546 */       return this.multimap.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object object) {
/* 551 */       if (object instanceof Map.Entry) {
/* 552 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/* 553 */         return this.multimap.containsEntry(entry.getKey(), entry.getValue());
/*     */       } 
/* 555 */       return false;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   UnmodifiableIterator<Map.Entry<K, V>> entryIterator() {
/* 563 */     return new UnmodifiableIterator<Map.Entry<K, V>>() {
/* 564 */         final Iterator<? extends Map.Entry<K, ? extends ImmutableCollection<V>>> asMapItr = ImmutableMultimap.this.map
/* 565 */           .entrySet().iterator();
/* 566 */         K currentKey = null;
/* 567 */         Iterator<V> valueItr = Iterators.emptyIterator();
/*     */ 
/*     */         
/*     */         public boolean hasNext() {
/* 571 */           return (this.valueItr.hasNext() || this.asMapItr.hasNext());
/*     */         }
/*     */ 
/*     */         
/*     */         public Map.Entry<K, V> next() {
/* 576 */           if (!this.valueItr.hasNext()) {
/* 577 */             Map.Entry<K, ? extends ImmutableCollection<V>> entry = this.asMapItr.next();
/* 578 */             this.currentKey = entry.getKey();
/* 579 */             this.valueItr = ((ImmutableCollection<V>)entry.getValue()).iterator();
/*     */           } 
/* 581 */           return Maps.immutableEntry(this.currentKey, this.valueItr.next());
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 588 */     return CollectSpliterators.flatMap(
/* 589 */         asMap().entrySet().spliterator(), keyToValueCollectionEntry -> { K key = (K)keyToValueCollectionEntry.getKey(); Collection<V> valueCollection = (Collection<V>)keyToValueCollectionEntry.getValue(); return CollectSpliterators.map(valueCollection.spliterator(), ()); }0x40 | (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 596 */         (this instanceof SetMultimap) ? 1 : 0), 
/* 597 */         size());
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 602 */     Preconditions.checkNotNull(action);
/* 603 */     asMap()
/* 604 */       .forEach((key, valueCollection) -> valueCollection.forEach(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMultiset<K> keys() {
/* 615 */     return (ImmutableMultiset<K>)super.keys();
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableMultiset<K> createKeys() {
/* 620 */     return new Keys();
/*     */   }
/*     */ 
/*     */   
/*     */   class Keys
/*     */     extends ImmutableMultiset<K>
/*     */   {
/*     */     public boolean contains(Object object) {
/* 628 */       return ImmutableMultimap.this.containsKey(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int count(Object element) {
/* 633 */       Collection<V> values = (Collection<V>)ImmutableMultimap.this.map.get(element);
/* 634 */       return (values == null) ? 0 : values.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableSet<K> elementSet() {
/* 639 */       return ImmutableMultimap.this.keySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 644 */       return ImmutableMultimap.this.size();
/*     */     }
/*     */ 
/*     */     
/*     */     Multiset.Entry<K> getEntry(int index) {
/* 649 */       Map.Entry<K, ? extends Collection<V>> entry = ImmutableMultimap.this.map.entrySet().asList().get(index);
/* 650 */       return Multisets.immutableEntry(entry.getKey(), ((Collection)entry.getValue()).size());
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 655 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/* 661 */       return new ImmutableMultimap.KeysSerializedForm(ImmutableMultimap.this);
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static final class KeysSerializedForm implements Serializable {
/*     */     final ImmutableMultimap<?, ?> multimap;
/*     */     
/*     */     KeysSerializedForm(ImmutableMultimap<?, ?> multimap) {
/* 670 */       this.multimap = multimap;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 674 */       return this.multimap.keys();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableCollection<V> values() {
/* 684 */     return (ImmutableCollection<V>)super.values();
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableCollection<V> createValues() {
/* 689 */     return new Values<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   UnmodifiableIterator<V> valueIterator() {
/* 694 */     return new UnmodifiableIterator<V>() {
/* 695 */         Iterator<? extends ImmutableCollection<V>> valueCollectionItr = ImmutableMultimap.this.map.values().iterator();
/* 696 */         Iterator<V> valueItr = Iterators.emptyIterator();
/*     */ 
/*     */         
/*     */         public boolean hasNext() {
/* 700 */           return (this.valueItr.hasNext() || this.valueCollectionItr.hasNext());
/*     */         }
/*     */ 
/*     */         
/*     */         public V next() {
/* 705 */           if (!this.valueItr.hasNext()) {
/* 706 */             this.valueItr = ((ImmutableCollection<V>)this.valueCollectionItr.next()).iterator();
/*     */           }
/* 708 */           return this.valueItr.next();
/*     */         }
/*     */       };
/*     */   }
/*     */   public abstract ImmutableCollection<V> get(K paramK);
/*     */   public abstract ImmutableMultimap<V, K> inverse();
/*     */   private static final class Values<K, V> extends ImmutableCollection<V> { @Weak
/*     */     private final transient ImmutableMultimap<K, V> multimap;
/*     */     Values(ImmutableMultimap<K, V> multimap) {
/* 717 */       this.multimap = multimap;
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public boolean contains(Object object) {
/* 722 */       return this.multimap.containsValue(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public UnmodifiableIterator<V> iterator() {
/* 727 */       return this.multimap.valueIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     int copyIntoArray(Object[] dst, int offset) {
/* 733 */       for (UnmodifiableIterator<ImmutableCollection<V>> unmodifiableIterator = this.multimap.map.values().iterator(); unmodifiableIterator.hasNext(); ) { ImmutableCollection<V> valueCollection = unmodifiableIterator.next();
/* 734 */         offset = valueCollection.copyIntoArray(dst, offset); }
/*     */       
/* 736 */       return offset;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 741 */       return this.multimap.size();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 746 */       return true;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\ImmutableMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */