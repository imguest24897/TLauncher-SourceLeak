/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Objects;
/*     */ import java.util.TreeMap;
/*     */ import java.util.function.BinaryOperator;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.ToIntFunction;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
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
/*     */ @GwtCompatible
/*     */ final class CollectCollectors
/*     */ {
/*  45 */   private static final Collector<Object, ?, ImmutableList<Object>> TO_IMMUTABLE_LIST = Collector.of(ImmutableList::builder, ImmutableList.Builder::add, ImmutableList.Builder::combine, ImmutableList.Builder::build, new Collector.Characteristics[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   private static final Collector<Object, ?, ImmutableSet<Object>> TO_IMMUTABLE_SET = Collector.of(ImmutableSet::builder, ImmutableSet.Builder::add, ImmutableSet.Builder::combine, ImmutableSet.Builder::build, new Collector.Characteristics[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*  61 */   private static final Collector<Range<Comparable<?>>, ?, ImmutableRangeSet<Comparable<?>>> TO_IMMUTABLE_RANGE_SET = Collector.of(ImmutableRangeSet::builder, ImmutableRangeSet.Builder::add, ImmutableRangeSet.Builder::combine, ImmutableRangeSet.Builder::build, new Collector.Characteristics[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
/*  71 */     return (Collector)TO_IMMUTABLE_LIST;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
/*  78 */     return (Collector)TO_IMMUTABLE_SET;
/*     */   }
/*     */ 
/*     */   
/*     */   static <E> Collector<E, ?, ImmutableSortedSet<E>> toImmutableSortedSet(Comparator<? super E> comparator) {
/*  83 */     Preconditions.checkNotNull(comparator);
/*  84 */     return Collector.of(() -> new ImmutableSortedSet.Builder(comparator), ImmutableSortedSet.Builder::add, ImmutableSortedSet.Builder::combine, ImmutableSortedSet.Builder::build, new Collector.Characteristics[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E extends Enum<E>> Collector<E, ?, ImmutableSet<E>> toImmutableEnumSet() {
/*  93 */     return (Collector)EnumSetAccumulator.TO_IMMUTABLE_ENUM_SET;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class EnumSetAccumulator<E extends Enum<E>>
/*     */   {
/* 100 */     static final Collector<Enum<?>, ?, ImmutableSet<? extends Enum<?>>> TO_IMMUTABLE_ENUM_SET = Collector.of(EnumSetAccumulator::new, EnumSetAccumulator::add, EnumSetAccumulator::combine, EnumSetAccumulator::toImmutableSet, new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
/*     */ 
/*     */ 
/*     */     
/*     */     private EnumSet<E> set;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void add(E e) {
/* 110 */       if (this.set == null) {
/* 111 */         this.set = EnumSet.of(e);
/*     */       } else {
/* 113 */         this.set.add(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     EnumSetAccumulator<E> combine(EnumSetAccumulator<E> other) {
/* 118 */       if (this.set == null)
/* 119 */         return other; 
/* 120 */       if (other.set == null) {
/* 121 */         return this;
/*     */       }
/* 123 */       this.set.addAll(other.set);
/* 124 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet<E> toImmutableSet() {
/* 129 */       return (this.set == null) ? ImmutableSet.<E>of() : ImmutableEnumSet.asImmutable(this.set);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   static <E extends Comparable<? super E>> Collector<Range<E>, ?, ImmutableRangeSet<E>> toImmutableRangeSet() {
/* 137 */     return (Collector)TO_IMMUTABLE_RANGE_SET;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T, E> Collector<T, ?, ImmutableMultiset<E>> toImmutableMultiset(Function<? super T, ? extends E> elementFunction, ToIntFunction<? super T> countFunction) {
/* 144 */     Preconditions.checkNotNull(elementFunction);
/* 145 */     Preconditions.checkNotNull(countFunction);
/* 146 */     return Collector.of(LinkedHashMultiset::create, (multiset, t) -> multiset.add(Preconditions.checkNotNull(elementFunction.apply(t)), countFunction.applyAsInt(t)), (multiset1, multiset2) -> { multiset1.addAll(multiset2); return multiset1; }multiset -> ImmutableMultiset.copyFromEntries(multiset.entrySet()), new Collector.Characteristics[0]);
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
/*     */   static <T, E, M extends Multiset<E>> Collector<T, ?, M> toMultiset(Function<? super T, E> elementFunction, ToIntFunction<? super T> countFunction, Supplier<M> multisetSupplier) {
/* 161 */     Preconditions.checkNotNull(elementFunction);
/* 162 */     Preconditions.checkNotNull(countFunction);
/* 163 */     Preconditions.checkNotNull(multisetSupplier);
/* 164 */     return (Collector)Collector.of(multisetSupplier, (ms, t) -> ms.add(elementFunction.apply(t), countFunction.applyAsInt(t)), (ms1, ms2) -> { ms1.addAll(ms2); return ms1; }new Collector.Characteristics[0]);
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
/*     */   static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/* 178 */     Preconditions.checkNotNull(keyFunction);
/* 179 */     Preconditions.checkNotNull(valueFunction);
/* 180 */     return Collector.of(Builder::new, (builder, input) -> builder.put(keyFunction.apply(input), valueFunction.apply(input)), ImmutableMap.Builder::combine, ImmutableMap.Builder::build, new Collector.Characteristics[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/* 191 */     Preconditions.checkNotNull(keyFunction);
/* 192 */     Preconditions.checkNotNull(valueFunction);
/* 193 */     Preconditions.checkNotNull(mergeFunction);
/* 194 */     return Collectors.collectingAndThen(
/* 195 */         Collectors.toMap(keyFunction, valueFunction, mergeFunction, java.util.LinkedHashMap::new), ImmutableMap::copyOf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(Comparator<? super K> comparator, Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/* 203 */     Preconditions.checkNotNull(comparator);
/* 204 */     Preconditions.checkNotNull(keyFunction);
/* 205 */     Preconditions.checkNotNull(valueFunction);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 210 */     return Collector.of(() -> new ImmutableSortedMap.Builder<>(comparator), (builder, input) -> builder.put(keyFunction.apply(input), valueFunction.apply(input)), ImmutableSortedMap.Builder::combine, ImmutableSortedMap.Builder::build, new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
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
/*     */   static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(Comparator<? super K> comparator, Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/* 223 */     Preconditions.checkNotNull(comparator);
/* 224 */     Preconditions.checkNotNull(keyFunction);
/* 225 */     Preconditions.checkNotNull(valueFunction);
/* 226 */     Preconditions.checkNotNull(mergeFunction);
/* 227 */     return Collectors.collectingAndThen(
/* 228 */         Collectors.toMap(keyFunction, valueFunction, mergeFunction, () -> new TreeMap<>(comparator)), ImmutableSortedMap::copyOfSorted);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T, K, V> Collector<T, ?, ImmutableBiMap<K, V>> toImmutableBiMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/* 236 */     Preconditions.checkNotNull(keyFunction);
/* 237 */     Preconditions.checkNotNull(valueFunction);
/* 238 */     return Collector.of(Builder::new, (builder, input) -> builder.put(keyFunction.apply(input), valueFunction.apply(input)), ImmutableBiMap.Builder::combine, ImmutableBiMap.Builder::build, new Collector.Characteristics[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T, K extends Enum<K>, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/* 249 */     Preconditions.checkNotNull(keyFunction);
/* 250 */     Preconditions.checkNotNull(valueFunction);
/* 251 */     return Collector.of(() -> new EnumMapAccumulator<>(()), (accum, t) -> { Enum enum_ = (Enum)Preconditions.checkNotNull(keyFunction.apply(t), "Null key for input %s", t); V newValue = (V)Preconditions.checkNotNull(valueFunction.apply(t), "Null value for input %s", t); accum.put(enum_, newValue); }EnumMapAccumulator::combine, EnumMapAccumulator::toImmutableMap, new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
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
/*     */   static <T, K extends Enum<K>, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/* 271 */     Preconditions.checkNotNull(keyFunction);
/* 272 */     Preconditions.checkNotNull(valueFunction);
/* 273 */     Preconditions.checkNotNull(mergeFunction);
/*     */     
/* 275 */     return Collector.of(() -> new EnumMapAccumulator<>(mergeFunction), (accum, t) -> { Enum enum_ = (Enum)Preconditions.checkNotNull(keyFunction.apply(t), "Null key for input %s", t); V newValue = (V)Preconditions.checkNotNull(valueFunction.apply(t), "Null value for input %s", t); accum.put(enum_, newValue); }EnumMapAccumulator::combine, EnumMapAccumulator::toImmutableMap, new Collector.Characteristics[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class EnumMapAccumulator<K extends Enum<K>, V>
/*     */   {
/*     */     private final BinaryOperator<V> mergeFunction;
/*     */ 
/*     */ 
/*     */     
/* 288 */     private EnumMap<K, V> map = null;
/*     */     
/*     */     EnumMapAccumulator(BinaryOperator<V> mergeFunction) {
/* 291 */       this.mergeFunction = mergeFunction;
/*     */     }
/*     */     
/*     */     void put(K key, V value) {
/* 295 */       if (this.map == null) {
/* 296 */         this.map = new EnumMap<>(key.getDeclaringClass());
/*     */       }
/* 298 */       this.map.merge(key, value, this.mergeFunction);
/*     */     }
/*     */     
/*     */     EnumMapAccumulator<K, V> combine(EnumMapAccumulator<K, V> other) {
/* 302 */       if (this.map == null)
/* 303 */         return other; 
/* 304 */       if (other.map == null) {
/* 305 */         return this;
/*     */       }
/* 307 */       other.map.forEach(this::put);
/* 308 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<K, V> toImmutableMap() {
/* 313 */       return (this.map == null) ? ImmutableMap.<K, V>of() : ImmutableEnumMap.<K, V>asImmutable(this.map);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   static <T, K extends Comparable<? super K>, V> Collector<T, ?, ImmutableRangeMap<K, V>> toImmutableRangeMap(Function<? super T, Range<K>> keyFunction, Function<? super T, ? extends V> valueFunction) {
/* 322 */     Preconditions.checkNotNull(keyFunction);
/* 323 */     Preconditions.checkNotNull(valueFunction);
/* 324 */     return Collector.of(ImmutableRangeMap::builder, (builder, input) -> builder.put(keyFunction.apply(input), valueFunction.apply(input)), ImmutableRangeMap.Builder::combine, ImmutableRangeMap.Builder::build, new Collector.Characteristics[0]);
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
/*     */   static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> toImmutableListMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/* 336 */     Preconditions.checkNotNull(keyFunction, "keyFunction");
/* 337 */     Preconditions.checkNotNull(valueFunction, "valueFunction");
/* 338 */     return Collector.of(ImmutableListMultimap::builder, (builder, t) -> builder.put(keyFunction.apply(t), valueFunction.apply(t)), ImmutableListMultimap.Builder::combine, ImmutableListMultimap.Builder::build, new Collector.Characteristics[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> flatteningToImmutableListMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valuesFunction) {
/* 348 */     Preconditions.checkNotNull(keyFunction);
/* 349 */     Preconditions.checkNotNull(valuesFunction);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 354 */     Objects.requireNonNull(MultimapBuilder.linkedHashKeys().arrayListValues()); return Collectors.collectingAndThen(flatteningToMultimap(input -> Preconditions.checkNotNull(keyFunction.apply(input)), input -> ((Stream)valuesFunction.apply(input)).peek(Preconditions::checkNotNull), MultimapBuilder.linkedHashKeys().arrayListValues()::build), ImmutableListMultimap::copyOf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> toImmutableSetMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/* 361 */     Preconditions.checkNotNull(keyFunction, "keyFunction");
/* 362 */     Preconditions.checkNotNull(valueFunction, "valueFunction");
/* 363 */     return Collector.of(ImmutableSetMultimap::builder, (builder, t) -> builder.put(keyFunction.apply(t), valueFunction.apply(t)), ImmutableSetMultimap.Builder::combine, ImmutableSetMultimap.Builder::build, new Collector.Characteristics[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> flatteningToImmutableSetMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valuesFunction) {
/* 373 */     Preconditions.checkNotNull(keyFunction);
/* 374 */     Preconditions.checkNotNull(valuesFunction);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 379 */     Objects.requireNonNull(MultimapBuilder.linkedHashKeys().linkedHashSetValues()); return Collectors.collectingAndThen(flatteningToMultimap(input -> Preconditions.checkNotNull(keyFunction.apply(input)), input -> ((Stream)valuesFunction.apply(input)).peek(Preconditions::checkNotNull), MultimapBuilder.linkedHashKeys().linkedHashSetValues()::build), ImmutableSetMultimap::copyOf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T, K, V, M extends Multimap<K, V>> Collector<T, ?, M> toMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, Supplier<M> multimapSupplier) {
/* 387 */     Preconditions.checkNotNull(keyFunction);
/* 388 */     Preconditions.checkNotNull(valueFunction);
/* 389 */     Preconditions.checkNotNull(multimapSupplier);
/* 390 */     return (Collector)Collector.of(multimapSupplier, (multimap, input) -> multimap.put(keyFunction.apply(input), valueFunction.apply(input)), (multimap1, multimap2) -> { multimap1.putAll(multimap2); return multimap1; }new Collector.Characteristics[0]);
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
/*     */   static <T, K, V, M extends Multimap<K, V>> Collector<T, ?, M> flatteningToMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valueFunction, Supplier<M> multimapSupplier) {
/* 404 */     Preconditions.checkNotNull(keyFunction);
/* 405 */     Preconditions.checkNotNull(valueFunction);
/* 406 */     Preconditions.checkNotNull(multimapSupplier);
/* 407 */     return (Collector)Collector.of(multimapSupplier, (multimap, input) -> { K key = keyFunction.apply(input); Collection<V> valuesForKey = multimap.get(key); Objects.requireNonNull(valuesForKey); ((Stream)valueFunction.apply(input)).forEachOrdered(valuesForKey::add); }(multimap1, multimap2) -> { multimap1.putAll(multimap2); return multimap1; }new Collector.Characteristics[0]);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\CollectCollectors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */