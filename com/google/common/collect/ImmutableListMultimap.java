/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public class ImmutableListMultimap<K, V>
/*     */   extends ImmutableMultimap<K, V>
/*     */   implements ListMultimap<K, V>
/*     */ {
/*     */   @LazyInit
/*     */   @RetainedWith
/*     */   private transient ImmutableListMultimap<V, K> inverse;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> toImmutableListMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*  83 */     return CollectCollectors.toImmutableListMultimap(keyFunction, valueFunction);
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
/*     */   public static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> flatteningToImmutableListMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valuesFunction) {
/* 122 */     return CollectCollectors.flatteningToImmutableListMultimap(keyFunction, valuesFunction);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of() {
/* 129 */     return EmptyImmutableListMultimap.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1) {
/* 134 */     Builder<K, V> builder = builder();
/* 135 */     builder.put(k1, v1);
/* 136 */     return builder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2) {
/* 141 */     Builder<K, V> builder = builder();
/* 142 */     builder.put(k1, v1);
/* 143 */     builder.put(k2, v2);
/* 144 */     return builder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/* 149 */     Builder<K, V> builder = builder();
/* 150 */     builder.put(k1, v1);
/* 151 */     builder.put(k2, v2);
/* 152 */     builder.put(k3, v3);
/* 153 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 159 */     Builder<K, V> builder = builder();
/* 160 */     builder.put(k1, v1);
/* 161 */     builder.put(k2, v2);
/* 162 */     builder.put(k3, v3);
/* 163 */     builder.put(k4, v4);
/* 164 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 170 */     Builder<K, V> builder = builder();
/* 171 */     builder.put(k1, v1);
/* 172 */     builder.put(k2, v2);
/* 173 */     builder.put(k3, v3);
/* 174 */     builder.put(k4, v4);
/* 175 */     builder.put(k5, v5);
/* 176 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 186 */     return new Builder<>();
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
/*     */   public static final class Builder<K, V>
/*     */     extends ImmutableMultimap.Builder<K, V>
/*     */   {
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(K key, V value) {
/* 218 */       super.put(key, value);
/* 219 */       return this;
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
/* 230 */       super.put(entry);
/* 231 */       return this;
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
/* 243 */       super.putAll(entries);
/* 244 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
/* 250 */       super.putAll(key, values);
/* 251 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(K key, V... values) {
/* 257 */       super.putAll(key, values);
/* 258 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
/* 264 */       super.putAll(multimap);
/* 265 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<K, V> combine(ImmutableMultimap.Builder<K, V> other) {
/* 271 */       super.combine(other);
/* 272 */       return this;
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
/* 283 */       super.orderKeysBy(keyComparator);
/* 284 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
/* 295 */       super.orderValuesBy(valueComparator);
/* 296 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableListMultimap<K, V> build() {
/* 302 */       return (ImmutableListMultimap<K, V>)super.build();
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
/*     */   public static <K, V> ImmutableListMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
/* 319 */     if (multimap.isEmpty()) {
/* 320 */       return of();
/*     */     }
/*     */ 
/*     */     
/* 324 */     if (multimap instanceof ImmutableListMultimap) {
/*     */       
/* 326 */       ImmutableListMultimap<K, V> kvMultimap = (ImmutableListMultimap)multimap;
/* 327 */       if (!kvMultimap.isPartialView()) {
/* 328 */         return kvMultimap;
/*     */       }
/*     */     } 
/*     */     
/* 332 */     return fromMapEntries(multimap.asMap().entrySet(), (Comparator<? super V>)null);
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
/*     */   public static <K, V> ImmutableListMultimap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 346 */     return (new Builder<>()).putAll(entries).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> ImmutableListMultimap<K, V> fromMapEntries(Collection<? extends Map.Entry<? extends K, ? extends Collection<? extends V>>> mapEntries, Comparator<? super V> valueComparator) {
/* 353 */     if (mapEntries.isEmpty()) {
/* 354 */       return of();
/*     */     }
/*     */     
/* 357 */     ImmutableMap.Builder<K, ImmutableList<V>> builder = new ImmutableMap.Builder<>(mapEntries.size());
/* 358 */     int size = 0;
/*     */     
/* 360 */     for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : mapEntries) {
/* 361 */       K key = entry.getKey();
/* 362 */       Collection<? extends V> values = entry.getValue();
/*     */ 
/*     */ 
/*     */       
/* 366 */       ImmutableList<V> list = (valueComparator == null) ? ImmutableList.<V>copyOf(values) : ImmutableList.<V>sortedCopyOf(valueComparator, values);
/* 367 */       if (!list.isEmpty()) {
/* 368 */         builder.put(key, list);
/* 369 */         size += list.size();
/*     */       } 
/*     */     } 
/*     */     
/* 373 */     return new ImmutableListMultimap<>(builder.build(), size);
/*     */   }
/*     */   
/*     */   ImmutableListMultimap(ImmutableMap<K, ImmutableList<V>> map, int size) {
/* 377 */     super((ImmutableMap)map, size);
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
/*     */   public ImmutableList<V> get(K key) {
/* 390 */     ImmutableList<V> list = (ImmutableList<V>)this.map.get(key);
/* 391 */     return (list == null) ? ImmutableList.<V>of() : list;
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
/*     */   public ImmutableListMultimap<V, K> inverse() {
/* 407 */     ImmutableListMultimap<V, K> result = this.inverse;
/* 408 */     return (result == null) ? (this.inverse = invert()) : result;
/*     */   }
/*     */   
/*     */   private ImmutableListMultimap<V, K> invert() {
/* 412 */     Builder<V, K> builder = builder();
/* 413 */     for (UnmodifiableIterator<Map.Entry<K, V>> unmodifiableIterator = entries().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<K, V> entry = unmodifiableIterator.next();
/* 414 */       builder.put(entry.getValue(), entry.getKey()); }
/*     */     
/* 416 */     ImmutableListMultimap<V, K> invertedMultimap = builder.build();
/* 417 */     invertedMultimap.inverse = this;
/* 418 */     return invertedMultimap;
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
/*     */   public ImmutableList<V> removeAll(Object key) {
/* 431 */     throw new UnsupportedOperationException();
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
/*     */   public ImmutableList<V> replaceValues(K key, Iterable<? extends V> values) {
/* 444 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 453 */     stream.defaultWriteObject();
/* 454 */     Serialization.writeMultimap(this, stream);
/*     */   }
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*     */     ImmutableMap<Object, ImmutableList<Object>> tmpMap;
/* 459 */     stream.defaultReadObject();
/* 460 */     int keyCount = stream.readInt();
/* 461 */     if (keyCount < 0) {
/* 462 */       throw new InvalidObjectException((new StringBuilder(29)).append("Invalid key count ").append(keyCount).toString());
/*     */     }
/* 464 */     ImmutableMap.Builder<Object, ImmutableList<Object>> builder = ImmutableMap.builder();
/* 465 */     int tmpSize = 0;
/*     */     
/* 467 */     for (int i = 0; i < keyCount; i++) {
/* 468 */       Object key = stream.readObject();
/* 469 */       int valueCount = stream.readInt();
/* 470 */       if (valueCount <= 0) {
/* 471 */         throw new InvalidObjectException((new StringBuilder(31)).append("Invalid value count ").append(valueCount).toString());
/*     */       }
/*     */       
/* 474 */       ImmutableList.Builder<Object> valuesBuilder = ImmutableList.builder();
/* 475 */       for (int j = 0; j < valueCount; j++) {
/* 476 */         valuesBuilder.add(stream.readObject());
/*     */       }
/* 478 */       builder.put(key, valuesBuilder.build());
/* 479 */       tmpSize += valueCount;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 484 */       tmpMap = builder.build();
/* 485 */     } catch (IllegalArgumentException e) {
/* 486 */       throw (InvalidObjectException)(new InvalidObjectException(e.getMessage())).initCause(e);
/*     */     } 
/*     */     
/* 489 */     ImmutableMultimap.FieldSettersHolder.MAP_FIELD_SETTER.set(this, tmpMap);
/* 490 */     ImmutableMultimap.FieldSettersHolder.SIZE_FIELD_SETTER.set(this, tmpSize);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\ImmutableListMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */