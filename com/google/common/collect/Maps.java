/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Converter;
/*      */ import com.google.common.base.Equivalence;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.j2objc.annotations.RetainedWith;
/*      */ import com.google.j2objc.annotations.Weak;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumMap;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableMap;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.SortedSet;
/*      */ import java.util.Spliterator;
/*      */ import java.util.Spliterators;
/*      */ import java.util.TreeMap;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.BinaryOperator;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.stream.Collector;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Maps
/*      */ {
/*      */   private enum EntryFunction
/*      */     implements Function<Map.Entry<?, ?>, Object>
/*      */   {
/*   92 */     KEY
/*      */     {
/*      */       public Object apply(Map.Entry<?, ?> entry) {
/*   95 */         return entry.getKey();
/*      */       }
/*      */     },
/*   98 */     VALUE
/*      */     {
/*      */       public Object apply(Map.Entry<?, ?> entry) {
/*  101 */         return entry.getValue();
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   
/*      */   static <K> Function<Map.Entry<K, ?>, K> keyFunction() {
/*  108 */     return EntryFunction.KEY;
/*      */   }
/*      */ 
/*      */   
/*      */   static <V> Function<Map.Entry<?, V>, V> valueFunction() {
/*  113 */     return EntryFunction.VALUE;
/*      */   }
/*      */   
/*      */   static <K, V> Iterator<K> keyIterator(Iterator<Map.Entry<K, V>> entryIterator) {
/*  117 */     return new TransformedIterator<Map.Entry<K, V>, K>(entryIterator)
/*      */       {
/*      */         K transform(Map.Entry<K, V> entry) {
/*  120 */           return entry.getKey();
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   static <K, V> Iterator<V> valueIterator(Iterator<Map.Entry<K, V>> entryIterator) {
/*  126 */     return new TransformedIterator<Map.Entry<K, V>, V>(entryIterator)
/*      */       {
/*      */         V transform(Map.Entry<K, V> entry) {
/*  129 */           return entry.getValue();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <K extends Enum<K>, V> ImmutableMap<K, V> immutableEnumMap(Map<K, ? extends V> map) {
/*  148 */     if (map instanceof ImmutableEnumMap) {
/*      */       
/*  150 */       ImmutableEnumMap<K, V> result = (ImmutableEnumMap)map;
/*  151 */       return result;
/*      */     } 
/*  153 */     Iterator<? extends Map.Entry<K, ? extends V>> entryItr = map.entrySet().iterator();
/*  154 */     if (!entryItr.hasNext()) {
/*  155 */       return ImmutableMap.of();
/*      */     }
/*  157 */     Map.Entry<K, ? extends V> entry1 = entryItr.next();
/*  158 */     Enum<K> enum_ = (Enum)entry1.getKey();
/*  159 */     V value1 = entry1.getValue();
/*  160 */     CollectPreconditions.checkEntryNotNull(enum_, value1);
/*  161 */     Class<K> clazz = enum_.getDeclaringClass();
/*  162 */     EnumMap<K, V> enumMap = new EnumMap<>(clazz);
/*  163 */     enumMap.put((K)enum_, value1);
/*  164 */     while (entryItr.hasNext()) {
/*  165 */       Map.Entry<K, ? extends V> entry = entryItr.next();
/*  166 */       Enum enum_1 = (Enum)entry.getKey();
/*  167 */       V value = entry.getValue();
/*  168 */       CollectPreconditions.checkEntryNotNull(enum_1, value);
/*  169 */       enumMap.put((K)enum_1, value);
/*      */     } 
/*  171 */     return ImmutableEnumMap.asImmutable(enumMap);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T, K extends Enum<K>, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*  192 */     return CollectCollectors.toImmutableEnumMap(keyFunction, valueFunction);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T, K extends Enum<K>, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/*  211 */     return CollectCollectors.toImmutableEnumMap(keyFunction, valueFunction, mergeFunction);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> HashMap<K, V> newHashMap() {
/*  228 */     return new HashMap<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> HashMap<K, V> newHashMap(Map<? extends K, ? extends V> map) {
/*  246 */     return new HashMap<>(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int expectedSize) {
/*  261 */     return new HashMap<>(capacity(expectedSize));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int capacity(int expectedSize) {
/*  269 */     if (expectedSize < 3) {
/*  270 */       CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
/*  271 */       return expectedSize + 1;
/*      */     } 
/*  273 */     if (expectedSize < 1073741824)
/*      */     {
/*      */ 
/*      */       
/*  277 */       return (int)(expectedSize / 0.75F + 1.0F);
/*      */     }
/*  279 */     return Integer.MAX_VALUE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
/*  294 */     return new LinkedHashMap<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(Map<? extends K, ? extends V> map) {
/*  311 */     return new LinkedHashMap<>(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> LinkedHashMap<K, V> newLinkedHashMapWithExpectedSize(int expectedSize) {
/*  327 */     return new LinkedHashMap<>(capacity(expectedSize));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ConcurrentMap<K, V> newConcurrentMap() {
/*  336 */     return new ConcurrentHashMap<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K extends Comparable, V> TreeMap<K, V> newTreeMap() {
/*  352 */     return new TreeMap<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> TreeMap<K, V> newTreeMap(SortedMap<K, ? extends V> map) {
/*  372 */     return new TreeMap<>(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <C, K extends C, V> TreeMap<K, V> newTreeMap(Comparator<C> comparator) {
/*  394 */     return new TreeMap<>(comparator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Class<K> type) {
/*  404 */     return new EnumMap<>((Class<K>)Preconditions.checkNotNull(type));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Map<K, ? extends V> map) {
/*  420 */     return new EnumMap<>(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
/*  433 */     return new IdentityHashMap<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right) {
/*  454 */     if (left instanceof SortedMap) {
/*  455 */       SortedMap<K, ? extends V> sortedLeft = (SortedMap)left;
/*  456 */       return difference(sortedLeft, right);
/*      */     } 
/*  458 */     return difference(left, right, Equivalence.equals());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right, Equivalence<? super V> valueEquivalence) {
/*  479 */     Preconditions.checkNotNull(valueEquivalence);
/*      */     
/*  481 */     Map<K, V> onlyOnLeft = newLinkedHashMap();
/*  482 */     Map<K, V> onlyOnRight = new LinkedHashMap<>(right);
/*  483 */     Map<K, V> onBoth = newLinkedHashMap();
/*  484 */     Map<K, MapDifference.ValueDifference<V>> differences = newLinkedHashMap();
/*  485 */     doDifference(left, right, valueEquivalence, onlyOnLeft, onlyOnRight, onBoth, differences);
/*  486 */     return new MapDifferenceImpl<>(onlyOnLeft, onlyOnRight, onBoth, differences);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedMapDifference<K, V> difference(SortedMap<K, ? extends V> left, Map<? extends K, ? extends V> right) {
/*  508 */     Preconditions.checkNotNull(left);
/*  509 */     Preconditions.checkNotNull(right);
/*  510 */     Comparator<? super K> comparator = orNaturalOrder(left.comparator());
/*  511 */     SortedMap<K, V> onlyOnLeft = newTreeMap(comparator);
/*  512 */     SortedMap<K, V> onlyOnRight = newTreeMap(comparator);
/*  513 */     onlyOnRight.putAll(right);
/*  514 */     SortedMap<K, V> onBoth = newTreeMap(comparator);
/*  515 */     SortedMap<K, MapDifference.ValueDifference<V>> differences = newTreeMap(comparator);
/*  516 */     doDifference(left, right, Equivalence.equals(), onlyOnLeft, onlyOnRight, onBoth, differences);
/*  517 */     return new SortedMapDifferenceImpl<>(onlyOnLeft, onlyOnRight, onBoth, differences);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> void doDifference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right, Equivalence<? super V> valueEquivalence, Map<K, V> onlyOnLeft, Map<K, V> onlyOnRight, Map<K, V> onBoth, Map<K, MapDifference.ValueDifference<V>> differences) {
/*  528 */     for (Map.Entry<? extends K, ? extends V> entry : left.entrySet()) {
/*  529 */       K leftKey = entry.getKey();
/*  530 */       V leftValue = entry.getValue();
/*  531 */       if (right.containsKey(leftKey)) {
/*  532 */         V rightValue = onlyOnRight.remove(leftKey);
/*  533 */         if (valueEquivalence.equivalent(leftValue, rightValue)) {
/*  534 */           onBoth.put(leftKey, leftValue); continue;
/*      */         } 
/*  536 */         differences.put(leftKey, ValueDifferenceImpl.create(leftValue, rightValue));
/*      */         continue;
/*      */       } 
/*  539 */       onlyOnLeft.put(leftKey, leftValue);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static <K, V> Map<K, V> unmodifiableMap(Map<K, ? extends V> map) {
/*  545 */     if (map instanceof SortedMap) {
/*  546 */       return Collections.unmodifiableSortedMap((SortedMap<K, ? extends V>)map);
/*      */     }
/*  548 */     return Collections.unmodifiableMap(map);
/*      */   }
/*      */ 
/*      */   
/*      */   static class MapDifferenceImpl<K, V>
/*      */     implements MapDifference<K, V>
/*      */   {
/*      */     final Map<K, V> onlyOnLeft;
/*      */     
/*      */     final Map<K, V> onlyOnRight;
/*      */     
/*      */     final Map<K, V> onBoth;
/*      */     final Map<K, MapDifference.ValueDifference<V>> differences;
/*      */     
/*      */     MapDifferenceImpl(Map<K, V> onlyOnLeft, Map<K, V> onlyOnRight, Map<K, V> onBoth, Map<K, MapDifference.ValueDifference<V>> differences) {
/*  563 */       this.onlyOnLeft = Maps.unmodifiableMap(onlyOnLeft);
/*  564 */       this.onlyOnRight = Maps.unmodifiableMap(onlyOnRight);
/*  565 */       this.onBoth = Maps.unmodifiableMap(onBoth);
/*  566 */       this.differences = (Map)Maps.unmodifiableMap((Map)differences);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean areEqual() {
/*  571 */       return (this.onlyOnLeft.isEmpty() && this.onlyOnRight.isEmpty() && this.differences.isEmpty());
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<K, V> entriesOnlyOnLeft() {
/*  576 */       return this.onlyOnLeft;
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<K, V> entriesOnlyOnRight() {
/*  581 */       return this.onlyOnRight;
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<K, V> entriesInCommon() {
/*  586 */       return this.onBoth;
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<K, MapDifference.ValueDifference<V>> entriesDiffering() {
/*  591 */       return this.differences;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/*  596 */       if (object == this) {
/*  597 */         return true;
/*      */       }
/*  599 */       if (object instanceof MapDifference) {
/*  600 */         MapDifference<?, ?> other = (MapDifference<?, ?>)object;
/*  601 */         return (entriesOnlyOnLeft().equals(other.entriesOnlyOnLeft()) && 
/*  602 */           entriesOnlyOnRight().equals(other.entriesOnlyOnRight()) && 
/*  603 */           entriesInCommon().equals(other.entriesInCommon()) && 
/*  604 */           entriesDiffering().equals(other.entriesDiffering()));
/*      */       } 
/*  606 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  611 */       return Objects.hashCode(new Object[] {
/*  612 */             entriesOnlyOnLeft(), entriesOnlyOnRight(), entriesInCommon(), entriesDiffering()
/*      */           });
/*      */     }
/*      */     
/*      */     public String toString() {
/*  617 */       if (areEqual()) {
/*  618 */         return "equal";
/*      */       }
/*      */       
/*  621 */       StringBuilder result = new StringBuilder("not equal");
/*  622 */       if (!this.onlyOnLeft.isEmpty()) {
/*  623 */         result.append(": only on left=").append(this.onlyOnLeft);
/*      */       }
/*  625 */       if (!this.onlyOnRight.isEmpty()) {
/*  626 */         result.append(": only on right=").append(this.onlyOnRight);
/*      */       }
/*  628 */       if (!this.differences.isEmpty()) {
/*  629 */         result.append(": value differences=").append(this.differences);
/*      */       }
/*  631 */       return result.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   static class ValueDifferenceImpl<V> implements MapDifference.ValueDifference<V> {
/*      */     private final V left;
/*      */     private final V right;
/*      */     
/*      */     static <V> MapDifference.ValueDifference<V> create(V left, V right) {
/*  640 */       return new ValueDifferenceImpl<>(left, right);
/*      */     }
/*      */     
/*      */     private ValueDifferenceImpl(V left, V right) {
/*  644 */       this.left = left;
/*  645 */       this.right = right;
/*      */     }
/*      */ 
/*      */     
/*      */     public V leftValue() {
/*  650 */       return this.left;
/*      */     }
/*      */ 
/*      */     
/*      */     public V rightValue() {
/*  655 */       return this.right;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/*  660 */       if (object instanceof MapDifference.ValueDifference) {
/*  661 */         MapDifference.ValueDifference<?> that = (MapDifference.ValueDifference)object;
/*  662 */         return (Objects.equal(this.left, that.leftValue()) && 
/*  663 */           Objects.equal(this.right, that.rightValue()));
/*      */       } 
/*  665 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  670 */       return Objects.hashCode(new Object[] { this.left, this.right });
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  675 */       String str1 = String.valueOf(this.left), str2 = String.valueOf(this.right); return (new StringBuilder(4 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("(").append(str1).append(", ").append(str2).append(")").toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class SortedMapDifferenceImpl<K, V>
/*      */     extends MapDifferenceImpl<K, V>
/*      */     implements SortedMapDifference<K, V>
/*      */   {
/*      */     SortedMapDifferenceImpl(SortedMap<K, V> onlyOnLeft, SortedMap<K, V> onlyOnRight, SortedMap<K, V> onBoth, SortedMap<K, MapDifference.ValueDifference<V>> differences) {
/*  686 */       super(onlyOnLeft, onlyOnRight, onBoth, differences);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, MapDifference.ValueDifference<V>> entriesDiffering() {
/*  691 */       return (SortedMap<K, MapDifference.ValueDifference<V>>)super.entriesDiffering();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> entriesInCommon() {
/*  696 */       return (SortedMap<K, V>)super.entriesInCommon();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> entriesOnlyOnLeft() {
/*  701 */       return (SortedMap<K, V>)super.entriesOnlyOnLeft();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> entriesOnlyOnRight() {
/*  706 */       return (SortedMap<K, V>)super.entriesOnlyOnRight();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> Comparator<? super E> orNaturalOrder(Comparator<? super E> comparator) {
/*  717 */     if (comparator != null) {
/*  718 */       return comparator;
/*      */     }
/*  720 */     return Ordering.natural();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Map<K, V> asMap(Set<K> set, Function<? super K, V> function) {
/*  748 */     return new AsMapView<>(set, function);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedMap<K, V> asMap(SortedSet<K> set, Function<? super K, V> function) {
/*  775 */     return new SortedAsMapView<>(set, function);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <K, V> NavigableMap<K, V> asMap(NavigableSet<K> set, Function<? super K, V> function) {
/*  804 */     return new NavigableAsMapView<>(set, function);
/*      */   }
/*      */   
/*      */   private static class AsMapView<K, V>
/*      */     extends ViewCachingAbstractMap<K, V> {
/*      */     private final Set<K> set;
/*      */     final Function<? super K, V> function;
/*      */     
/*      */     Set<K> backingSet() {
/*  813 */       return this.set;
/*      */     }
/*      */     
/*      */     AsMapView(Set<K> set, Function<? super K, V> function) {
/*  817 */       this.set = (Set<K>)Preconditions.checkNotNull(set);
/*  818 */       this.function = (Function<? super K, V>)Preconditions.checkNotNull(function);
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> createKeySet() {
/*  823 */       return Maps.removeOnlySet(backingSet());
/*      */     }
/*      */ 
/*      */     
/*      */     Collection<V> createValues() {
/*  828 */       return Collections2.transform(this.set, this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  833 */       return backingSet().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/*  838 */       return backingSet().contains(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(Object key) {
/*  843 */       return getOrDefault(key, null);
/*      */     }
/*      */ 
/*      */     
/*      */     public V getOrDefault(Object key, V defaultValue) {
/*  848 */       if (Collections2.safeContains(backingSet(), key)) {
/*      */         
/*  850 */         K k = (K)key;
/*  851 */         return (V)this.function.apply(k);
/*      */       } 
/*  853 */       return defaultValue;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public V remove(Object key) {
/*  859 */       if (backingSet().remove(key)) {
/*      */         
/*  861 */         K k = (K)key;
/*  862 */         return (V)this.function.apply(k);
/*      */       } 
/*  864 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/*  870 */       backingSet().clear();
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, V>> createEntrySet() {
/*      */       class EntrySetImpl
/*      */         extends Maps.EntrySet<K, V>
/*      */       {
/*      */         Map<K, V> map() {
/*  879 */           return Maps.AsMapView.this;
/*      */         }
/*      */ 
/*      */         
/*      */         public Iterator<Map.Entry<K, V>> iterator() {
/*  884 */           return Maps.asMapEntryIterator(Maps.AsMapView.this.backingSet(), Maps.AsMapView.this.function);
/*      */         }
/*      */       };
/*  887 */       return new EntrySetImpl();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(BiConsumer<? super K, ? super V> action) {
/*  892 */       Preconditions.checkNotNull(action);
/*      */       
/*  894 */       backingSet().forEach(k -> action.accept(k, this.function.apply(k)));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> Iterator<Map.Entry<K, V>> asMapEntryIterator(Set<K> set, final Function<? super K, V> function) {
/*  900 */     return new TransformedIterator<K, Map.Entry<K, V>>(set.iterator())
/*      */       {
/*      */         Map.Entry<K, V> transform(K key) {
/*  903 */           return Maps.immutableEntry(key, (V)function.apply(key));
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   private static class SortedAsMapView<K, V>
/*      */     extends AsMapView<K, V> implements SortedMap<K, V> {
/*      */     SortedAsMapView(SortedSet<K> set, Function<? super K, V> function) {
/*  911 */       super(set, function);
/*      */     }
/*      */ 
/*      */     
/*      */     SortedSet<K> backingSet() {
/*  916 */       return (SortedSet<K>)super.backingSet();
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/*  921 */       return backingSet().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/*  926 */       return Maps.removeOnlySortedSet(backingSet());
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> subMap(K fromKey, K toKey) {
/*  931 */       return Maps.asMap(backingSet().subSet(fromKey, toKey), this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> headMap(K toKey) {
/*  936 */       return Maps.asMap(backingSet().headSet(toKey), this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> tailMap(K fromKey) {
/*  941 */       return Maps.asMap(backingSet().tailSet(fromKey), this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public K firstKey() {
/*  946 */       return backingSet().first();
/*      */     }
/*      */ 
/*      */     
/*      */     public K lastKey() {
/*  951 */       return backingSet().last();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   private static final class NavigableAsMapView<K, V>
/*      */     extends AbstractNavigableMap<K, V>
/*      */   {
/*      */     private final NavigableSet<K> set;
/*      */     
/*      */     private final Function<? super K, V> function;
/*      */ 
/*      */     
/*      */     NavigableAsMapView(NavigableSet<K> ks, Function<? super K, V> vFunction) {
/*  966 */       this.set = (NavigableSet<K>)Preconditions.checkNotNull(ks);
/*  967 */       this.function = (Function<? super K, V>)Preconditions.checkNotNull(vFunction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/*  973 */       return Maps.asMap(this.set.subSet(fromKey, fromInclusive, toKey, toInclusive), this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
/*  978 */       return Maps.asMap(this.set.headSet(toKey, inclusive), this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
/*  983 */       return Maps.asMap(this.set.tailSet(fromKey, inclusive), this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/*  988 */       return this.set.comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(Object key) {
/*  993 */       return getOrDefault(key, null);
/*      */     }
/*      */ 
/*      */     
/*      */     public V getOrDefault(Object key, V defaultValue) {
/*  998 */       if (Collections2.safeContains(this.set, key)) {
/*      */         
/* 1000 */         K k = (K)key;
/* 1001 */         return (V)this.function.apply(k);
/*      */       } 
/* 1003 */       return defaultValue;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1009 */       this.set.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V>> entryIterator() {
/* 1014 */       return Maps.asMapEntryIterator(this.set, this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 1019 */       return CollectSpliterators.map(this.set.spliterator(), e -> Maps.immutableEntry(e, this.function.apply(e)));
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(BiConsumer<? super K, ? super V> action) {
/* 1024 */       this.set.forEach(k -> action.accept(k, this.function.apply(k)));
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V>> descendingEntryIterator() {
/* 1029 */       return descendingMap().entrySet().iterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 1034 */       return Maps.removeOnlyNavigableSet(this.set);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1039 */       return this.set.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> descendingMap() {
/* 1044 */       return Maps.asMap(this.set.descendingSet(), this.function);
/*      */     }
/*      */   }
/*      */   
/*      */   private static <E> Set<E> removeOnlySet(final Set<E> set) {
/* 1049 */     return new ForwardingSet<E>()
/*      */       {
/*      */         protected Set<E> delegate() {
/* 1052 */           return set;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean add(E element) {
/* 1057 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean addAll(Collection<? extends E> es) {
/* 1062 */           throw new UnsupportedOperationException();
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   private static <E> SortedSet<E> removeOnlySortedSet(final SortedSet<E> set) {
/* 1068 */     return new ForwardingSortedSet<E>()
/*      */       {
/*      */         protected SortedSet<E> delegate() {
/* 1071 */           return set;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean add(E element) {
/* 1076 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean addAll(Collection<? extends E> es) {
/* 1081 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public SortedSet<E> headSet(E toElement) {
/* 1086 */           return Maps.removeOnlySortedSet(super.headSet(toElement));
/*      */         }
/*      */ 
/*      */         
/*      */         public SortedSet<E> subSet(E fromElement, E toElement) {
/* 1091 */           return Maps.removeOnlySortedSet(super.subSet(fromElement, toElement));
/*      */         }
/*      */ 
/*      */         
/*      */         public SortedSet<E> tailSet(E fromElement) {
/* 1096 */           return Maps.removeOnlySortedSet(super.tailSet(fromElement));
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   private static <E> NavigableSet<E> removeOnlyNavigableSet(final NavigableSet<E> set) {
/* 1103 */     return new ForwardingNavigableSet<E>()
/*      */       {
/*      */         protected NavigableSet<E> delegate() {
/* 1106 */           return set;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean add(E element) {
/* 1111 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean addAll(Collection<? extends E> es) {
/* 1116 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public SortedSet<E> headSet(E toElement) {
/* 1121 */           return Maps.removeOnlySortedSet(super.headSet(toElement));
/*      */         }
/*      */ 
/*      */         
/*      */         public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 1126 */           return Maps.removeOnlyNavigableSet(super.headSet(toElement, inclusive));
/*      */         }
/*      */ 
/*      */         
/*      */         public SortedSet<E> subSet(E fromElement, E toElement) {
/* 1131 */           return Maps.removeOnlySortedSet(super.subSet(fromElement, toElement));
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 1137 */           return Maps.removeOnlyNavigableSet(super
/* 1138 */               .subSet(fromElement, fromInclusive, toElement, toInclusive));
/*      */         }
/*      */ 
/*      */         
/*      */         public SortedSet<E> tailSet(E fromElement) {
/* 1143 */           return Maps.removeOnlySortedSet(super.tailSet(fromElement));
/*      */         }
/*      */ 
/*      */         
/*      */         public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 1148 */           return Maps.removeOnlyNavigableSet(super.tailSet(fromElement, inclusive));
/*      */         }
/*      */ 
/*      */         
/*      */         public NavigableSet<E> descendingSet() {
/* 1153 */           return Maps.removeOnlyNavigableSet(super.descendingSet());
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ImmutableMap<K, V> toMap(Iterable<K> keys, Function<? super K, V> valueFunction) {
/* 1176 */     return toMap(keys.iterator(), valueFunction);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ImmutableMap<K, V> toMap(Iterator<K> keys, Function<? super K, V> valueFunction) {
/* 1194 */     Preconditions.checkNotNull(valueFunction);
/*      */     
/* 1196 */     Map<K, V> builder = newLinkedHashMap();
/* 1197 */     while (keys.hasNext()) {
/* 1198 */       K key = keys.next();
/* 1199 */       builder.put(key, (V)valueFunction.apply(key));
/*      */     } 
/* 1201 */     return ImmutableMap.copyOf(builder);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterable<V> values, Function<? super V, K> keyFunction) {
/* 1236 */     return uniqueIndex(values.iterator(), keyFunction);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterator<V> values, Function<? super V, K> keyFunction) {
/* 1271 */     Preconditions.checkNotNull(keyFunction);
/* 1272 */     ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
/* 1273 */     while (values.hasNext()) {
/* 1274 */       V value = values.next();
/* 1275 */       builder.put((K)keyFunction.apply(value), value);
/*      */     } 
/*      */     try {
/* 1278 */       return builder.build();
/* 1279 */     } catch (IllegalArgumentException duplicateKeys) {
/* 1280 */       throw new IllegalArgumentException(
/* 1281 */           String.valueOf(duplicateKeys.getMessage()).concat(". To index multiple values under a key, use Multimaps.index."));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static ImmutableMap<String, String> fromProperties(Properties properties) {
/* 1298 */     ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
/*      */     
/* 1300 */     for (Enumeration<?> e = properties.propertyNames(); e.hasMoreElements(); ) {
/* 1301 */       String key = (String)e.nextElement();
/* 1302 */       builder.put(key, properties.getProperty(key));
/*      */     } 
/*      */     
/* 1305 */     return builder.build();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <K, V> Map.Entry<K, V> immutableEntry(K key, V value) {
/* 1322 */     return new ImmutableEntry<>(key, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> Set<Map.Entry<K, V>> unmodifiableEntrySet(Set<Map.Entry<K, V>> entrySet) {
/* 1334 */     return new UnmodifiableEntrySet<>(Collections.unmodifiableSet(entrySet));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> Map.Entry<K, V> unmodifiableEntry(final Map.Entry<? extends K, ? extends V> entry) {
/* 1347 */     Preconditions.checkNotNull(entry);
/* 1348 */     return new AbstractMapEntry<K, V>()
/*      */       {
/*      */         public K getKey() {
/* 1351 */           return (K)entry.getKey();
/*      */         }
/*      */ 
/*      */         
/*      */         public V getValue() {
/* 1356 */           return (V)entry.getValue();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> UnmodifiableIterator<Map.Entry<K, V>> unmodifiableEntryIterator(final Iterator<Map.Entry<K, V>> entryIterator) {
/* 1363 */     return new UnmodifiableIterator<Map.Entry<K, V>>()
/*      */       {
/*      */         public boolean hasNext() {
/* 1366 */           return entryIterator.hasNext();
/*      */         }
/*      */ 
/*      */         
/*      */         public Map.Entry<K, V> next() {
/* 1371 */           return Maps.unmodifiableEntry(entryIterator.next());
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   static class UnmodifiableEntries<K, V>
/*      */     extends ForwardingCollection<Map.Entry<K, V>> {
/*      */     private final Collection<Map.Entry<K, V>> entries;
/*      */     
/*      */     UnmodifiableEntries(Collection<Map.Entry<K, V>> entries) {
/* 1381 */       this.entries = entries;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Collection<Map.Entry<K, V>> delegate() {
/* 1386 */       return this.entries;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/* 1391 */       return Maps.unmodifiableEntryIterator(this.entries.iterator());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 1398 */       return standardToArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] array) {
/* 1403 */       return (T[])standardToArray((Object[])array);
/*      */     }
/*      */   }
/*      */   
/*      */   static class UnmodifiableEntrySet<K, V>
/*      */     extends UnmodifiableEntries<K, V>
/*      */     implements Set<Map.Entry<K, V>> {
/*      */     UnmodifiableEntrySet(Set<Map.Entry<K, V>> entries) {
/* 1411 */       super(entries);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/* 1418 */       return Sets.equalsImpl(this, object);
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1423 */       return Sets.hashCodeImpl(this);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <A, B> Converter<A, B> asConverter(BiMap<A, B> bimap) {
/* 1438 */     return new BiMapConverter<>(bimap);
/*      */   }
/*      */   
/*      */   private static final class BiMapConverter<A, B> extends Converter<A, B> implements Serializable { private final BiMap<A, B> bimap;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     BiMapConverter(BiMap<A, B> bimap) {
/* 1445 */       this.bimap = (BiMap<A, B>)Preconditions.checkNotNull(bimap);
/*      */     }
/*      */ 
/*      */     
/*      */     protected B doForward(A a) {
/* 1450 */       return convert(this.bimap, a);
/*      */     }
/*      */ 
/*      */     
/*      */     protected A doBackward(B b) {
/* 1455 */       return convert(this.bimap.inverse(), b);
/*      */     }
/*      */     
/*      */     private static <X, Y> Y convert(BiMap<X, Y> bimap, X input) {
/* 1459 */       Y output = bimap.get(input);
/* 1460 */       Preconditions.checkArgument((output != null), "No non-null mapping present for input: %s", input);
/* 1461 */       return output;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/* 1466 */       if (object instanceof BiMapConverter) {
/* 1467 */         BiMapConverter<?, ?> that = (BiMapConverter<?, ?>)object;
/* 1468 */         return this.bimap.equals(that.bimap);
/*      */       } 
/* 1470 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1475 */       return this.bimap.hashCode();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1481 */       String str = String.valueOf(this.bimap); return (new StringBuilder(18 + String.valueOf(str).length())).append("Maps.asConverter(").append(str).append(")").toString();
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> BiMap<K, V> synchronizedBiMap(BiMap<K, V> bimap) {
/* 1517 */     return Synchronized.biMap(bimap, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> BiMap<K, V> unmodifiableBiMap(BiMap<? extends K, ? extends V> bimap) {
/* 1532 */     return new UnmodifiableBiMap<>(bimap, null);
/*      */   }
/*      */   
/*      */   private static class UnmodifiableBiMap<K, V> extends ForwardingMap<K, V> implements BiMap<K, V>, Serializable {
/*      */     final Map<K, V> unmodifiableMap;
/*      */     final BiMap<? extends K, ? extends V> delegate;
/*      */     @RetainedWith
/*      */     BiMap<V, K> inverse;
/*      */     transient Set<V> values;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableBiMap(BiMap<? extends K, ? extends V> delegate, BiMap<V, K> inverse) {
/* 1544 */       this.unmodifiableMap = Collections.unmodifiableMap(delegate);
/* 1545 */       this.delegate = delegate;
/* 1546 */       this.inverse = inverse;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Map<K, V> delegate() {
/* 1551 */       return this.unmodifiableMap;
/*      */     }
/*      */ 
/*      */     
/*      */     public V forcePut(K key, V value) {
/* 1556 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public BiMap<V, K> inverse() {
/* 1561 */       BiMap<V, K> result = this.inverse;
/* 1562 */       return (result == null) ? (
/* 1563 */         this.inverse = new UnmodifiableBiMap(this.delegate.inverse(), this)) : 
/* 1564 */         result;
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> values() {
/* 1569 */       Set<V> result = this.values;
/* 1570 */       return (result == null) ? (this.values = Collections.unmodifiableSet(this.delegate.values())) : result;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V1, V2> Map<K, V2> transformValues(Map<K, V1> fromMap, Function<? super V1, V2> function) {
/* 1611 */     return transformEntries(fromMap, asEntryTransformer(function));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V1, V2> SortedMap<K, V2> transformValues(SortedMap<K, V1> fromMap, Function<? super V1, V2> function) {
/* 1652 */     return transformEntries(fromMap, asEntryTransformer(function));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <K, V1, V2> NavigableMap<K, V2> transformValues(NavigableMap<K, V1> fromMap, Function<? super V1, V2> function) {
/* 1696 */     return transformEntries(fromMap, asEntryTransformer(function));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V1, V2> Map<K, V2> transformEntries(Map<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1748 */     return new TransformedEntriesMap<>(fromMap, transformer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V1, V2> SortedMap<K, V2> transformEntries(SortedMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1800 */     return new TransformedEntriesSortedMap<>(fromMap, transformer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <K, V1, V2> NavigableMap<K, V2> transformEntries(NavigableMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1854 */     return new TransformedEntriesNavigableMap<>(fromMap, transformer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V1, V2> EntryTransformer<K, V1, V2> asEntryTransformer(final Function<? super V1, V2> function) {
/* 1888 */     Preconditions.checkNotNull(function);
/* 1889 */     return new EntryTransformer<K, V1, V2>()
/*      */       {
/*      */         public V2 transformEntry(K key, V1 value) {
/* 1892 */           return (V2)function.apply(value);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V1, V2> Function<V1, V2> asValueToValueFunction(final EntryTransformer<? super K, V1, V2> transformer, final K key) {
/* 1899 */     Preconditions.checkNotNull(transformer);
/* 1900 */     return new Function<V1, V2>()
/*      */       {
/*      */         public V2 apply(V1 v1) {
/* 1903 */           return transformer.transformEntry(key, v1);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V1, V2> Function<Map.Entry<K, V1>, V2> asEntryToValueFunction(final EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1911 */     Preconditions.checkNotNull(transformer);
/* 1912 */     return new Function<Map.Entry<K, V1>, V2>()
/*      */       {
/*      */         public V2 apply(Map.Entry<K, V1> entry) {
/* 1915 */           return (V2)transformer.transformEntry(entry.getKey(), entry.getValue());
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <V2, K, V1> Map.Entry<K, V2> transformEntry(final EntryTransformer<? super K, ? super V1, V2> transformer, final Map.Entry<K, V1> entry) {
/* 1923 */     Preconditions.checkNotNull(transformer);
/* 1924 */     Preconditions.checkNotNull(entry);
/* 1925 */     return new AbstractMapEntry<K, V2>()
/*      */       {
/*      */         public K getKey() {
/* 1928 */           return (K)entry.getKey();
/*      */         }
/*      */ 
/*      */         
/*      */         public V2 getValue() {
/* 1933 */           return (V2)transformer.transformEntry(entry.getKey(), entry.getValue());
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V1, V2> Function<Map.Entry<K, V1>, Map.Entry<K, V2>> asEntryToEntryFunction(final EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1941 */     Preconditions.checkNotNull(transformer);
/* 1942 */     return new Function<Map.Entry<K, V1>, Map.Entry<K, V2>>()
/*      */       {
/*      */         public Map.Entry<K, V2> apply(Map.Entry<K, V1> entry) {
/* 1945 */           return Maps.transformEntry(transformer, entry);
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   static class TransformedEntriesMap<K, V1, V2>
/*      */     extends IteratorBasedAbstractMap<K, V2> {
/*      */     final Map<K, V1> fromMap;
/*      */     final Maps.EntryTransformer<? super K, ? super V1, V2> transformer;
/*      */     
/*      */     TransformedEntriesMap(Map<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1956 */       this.fromMap = (Map<K, V1>)Preconditions.checkNotNull(fromMap);
/* 1957 */       this.transformer = (Maps.EntryTransformer<? super K, ? super V1, V2>)Preconditions.checkNotNull(transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1962 */       return this.fromMap.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 1967 */       return this.fromMap.containsKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public V2 get(Object key) {
/* 1972 */       return getOrDefault(key, null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public V2 getOrDefault(Object key, V2 defaultValue) {
/* 1979 */       V1 value = this.fromMap.get(key);
/* 1980 */       return (value != null || this.fromMap.containsKey(key)) ? 
/* 1981 */         this.transformer.transformEntry((K)key, value) : 
/* 1982 */         defaultValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public V2 remove(Object key) {
/* 1989 */       return this.fromMap.containsKey(key) ? 
/* 1990 */         this.transformer.transformEntry((K)key, this.fromMap.remove(key)) : 
/* 1991 */         null;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1996 */       this.fromMap.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 2001 */       return this.fromMap.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V2>> entryIterator() {
/* 2006 */       return Iterators.transform(this.fromMap
/* 2007 */           .entrySet().iterator(), Maps.asEntryToEntryFunction(this.transformer));
/*      */     }
/*      */ 
/*      */     
/*      */     Spliterator<Map.Entry<K, V2>> entrySpliterator() {
/* 2012 */       return CollectSpliterators.map(this.fromMap
/* 2013 */           .entrySet().spliterator(), (Function<?, ? extends Map.Entry<K, V2>>)Maps.asEntryToEntryFunction(this.transformer));
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(BiConsumer<? super K, ? super V2> action) {
/* 2018 */       Preconditions.checkNotNull(action);
/*      */       
/* 2020 */       this.fromMap.forEach((k, v1) -> action.accept(k, this.transformer.transformEntry((K)k, (V1)v1)));
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V2> values() {
/* 2025 */       return new Maps.Values<>(this);
/*      */     }
/*      */   }
/*      */   
/*      */   static class TransformedEntriesSortedMap<K, V1, V2>
/*      */     extends TransformedEntriesMap<K, V1, V2>
/*      */     implements SortedMap<K, V2> {
/*      */     protected SortedMap<K, V1> fromMap() {
/* 2033 */       return (SortedMap<K, V1>)this.fromMap;
/*      */     }
/*      */ 
/*      */     
/*      */     TransformedEntriesSortedMap(SortedMap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 2038 */       super(fromMap, transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 2043 */       return fromMap().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public K firstKey() {
/* 2048 */       return fromMap().firstKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V2> headMap(K toKey) {
/* 2053 */       return Maps.transformEntries(fromMap().headMap(toKey), this.transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public K lastKey() {
/* 2058 */       return fromMap().lastKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V2> subMap(K fromKey, K toKey) {
/* 2063 */       return Maps.transformEntries(fromMap().subMap(fromKey, toKey), this.transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V2> tailMap(K fromKey) {
/* 2068 */       return Maps.transformEntries(fromMap().tailMap(fromKey), this.transformer);
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   private static class TransformedEntriesNavigableMap<K, V1, V2>
/*      */     extends TransformedEntriesSortedMap<K, V1, V2>
/*      */     implements NavigableMap<K, V2>
/*      */   {
/*      */     TransformedEntriesNavigableMap(NavigableMap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 2078 */       super(fromMap, transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V2> ceilingEntry(K key) {
/* 2083 */       return transformEntry(fromMap().ceilingEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public K ceilingKey(K key) {
/* 2088 */       return fromMap().ceilingKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingKeySet() {
/* 2093 */       return fromMap().descendingKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> descendingMap() {
/* 2098 */       return Maps.transformEntries(fromMap().descendingMap(), this.transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V2> firstEntry() {
/* 2103 */       return transformEntry(fromMap().firstEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V2> floorEntry(K key) {
/* 2108 */       return transformEntry(fromMap().floorEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public K floorKey(K key) {
/* 2113 */       return fromMap().floorKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> headMap(K toKey) {
/* 2118 */       return headMap(toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> headMap(K toKey, boolean inclusive) {
/* 2123 */       return Maps.transformEntries(fromMap().headMap(toKey, inclusive), this.transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V2> higherEntry(K key) {
/* 2128 */       return transformEntry(fromMap().higherEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public K higherKey(K key) {
/* 2133 */       return fromMap().higherKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V2> lastEntry() {
/* 2138 */       return transformEntry(fromMap().lastEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V2> lowerEntry(K key) {
/* 2143 */       return transformEntry(fromMap().lowerEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public K lowerKey(K key) {
/* 2148 */       return fromMap().lowerKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 2153 */       return fromMap().navigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V2> pollFirstEntry() {
/* 2158 */       return transformEntry(fromMap().pollFirstEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V2> pollLastEntry() {
/* 2163 */       return transformEntry(fromMap().pollLastEntry());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 2169 */       return Maps.transformEntries(
/* 2170 */           fromMap().subMap(fromKey, fromInclusive, toKey, toInclusive), this.transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> subMap(K fromKey, K toKey) {
/* 2175 */       return subMap(fromKey, true, toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> tailMap(K fromKey) {
/* 2180 */       return tailMap(fromKey, true);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> tailMap(K fromKey, boolean inclusive) {
/* 2185 */       return Maps.transformEntries(fromMap().tailMap(fromKey, inclusive), this.transformer);
/*      */     }
/*      */     
/*      */     private Map.Entry<K, V2> transformEntry(Map.Entry<K, V1> entry) {
/* 2189 */       return (entry == null) ? null : Maps.<V2, K, V1>transformEntry(this.transformer, entry);
/*      */     }
/*      */ 
/*      */     
/*      */     protected NavigableMap<K, V1> fromMap() {
/* 2194 */       return (NavigableMap<K, V1>)super.fromMap();
/*      */     }
/*      */   }
/*      */   
/*      */   static <K> Predicate<Map.Entry<K, ?>> keyPredicateOnEntries(Predicate<? super K> keyPredicate) {
/* 2199 */     return Predicates.compose(keyPredicate, keyFunction());
/*      */   }
/*      */   
/*      */   static <V> Predicate<Map.Entry<?, V>> valuePredicateOnEntries(Predicate<? super V> valuePredicate) {
/* 2203 */     return Predicates.compose(valuePredicate, valueFunction());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Map<K, V> filterKeys(Map<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 2231 */     Preconditions.checkNotNull(keyPredicate);
/* 2232 */     Predicate<Map.Entry<K, ?>> entryPredicate = keyPredicateOnEntries(keyPredicate);
/* 2233 */     return (unfiltered instanceof AbstractFilteredMap) ? 
/* 2234 */       filterFiltered((AbstractFilteredMap<K, V>)unfiltered, (Predicate)entryPredicate) : 
/* 2235 */       new FilteredKeyMap<>((Map<K, V>)Preconditions.checkNotNull(unfiltered), keyPredicate, (Predicate)entryPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedMap<K, V> filterKeys(SortedMap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 2268 */     return filterEntries(unfiltered, (Predicate)keyPredicateOnEntries(keyPredicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <K, V> NavigableMap<K, V> filterKeys(NavigableMap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 2302 */     return filterEntries(unfiltered, (Predicate)keyPredicateOnEntries(keyPredicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> BiMap<K, V> filterKeys(BiMap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 2331 */     Preconditions.checkNotNull(keyPredicate);
/* 2332 */     return filterEntries(unfiltered, (Predicate)keyPredicateOnEntries(keyPredicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Map<K, V> filterValues(Map<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2360 */     return filterEntries(unfiltered, (Predicate)valuePredicateOnEntries(valuePredicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedMap<K, V> filterValues(SortedMap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2391 */     return filterEntries(unfiltered, (Predicate)valuePredicateOnEntries(valuePredicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <K, V> NavigableMap<K, V> filterValues(NavigableMap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2423 */     return filterEntries(unfiltered, (Predicate)valuePredicateOnEntries(valuePredicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> BiMap<K, V> filterValues(BiMap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2455 */     return filterEntries(unfiltered, (Predicate)valuePredicateOnEntries(valuePredicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Map<K, V> filterEntries(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2484 */     Preconditions.checkNotNull(entryPredicate);
/* 2485 */     return (unfiltered instanceof AbstractFilteredMap) ? 
/* 2486 */       filterFiltered((AbstractFilteredMap<K, V>)unfiltered, entryPredicate) : 
/* 2487 */       new FilteredEntryMap<>((Map<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedMap<K, V> filterEntries(SortedMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2518 */     Preconditions.checkNotNull(entryPredicate);
/* 2519 */     return (unfiltered instanceof FilteredEntrySortedMap) ? 
/* 2520 */       filterFiltered((FilteredEntrySortedMap<K, V>)unfiltered, entryPredicate) : 
/* 2521 */       new FilteredEntrySortedMap<>((SortedMap<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <K, V> NavigableMap<K, V> filterEntries(NavigableMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2553 */     Preconditions.checkNotNull(entryPredicate);
/* 2554 */     return (unfiltered instanceof FilteredEntryNavigableMap) ? 
/* 2555 */       filterFiltered((FilteredEntryNavigableMap<K, V>)unfiltered, entryPredicate) : 
/* 2556 */       new FilteredEntryNavigableMap<>((NavigableMap<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> BiMap<K, V> filterEntries(BiMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2588 */     Preconditions.checkNotNull(unfiltered);
/* 2589 */     Preconditions.checkNotNull(entryPredicate);
/* 2590 */     return (unfiltered instanceof FilteredEntryBiMap) ? 
/* 2591 */       filterFiltered((FilteredEntryBiMap<K, V>)unfiltered, entryPredicate) : 
/* 2592 */       new FilteredEntryBiMap<>(unfiltered, entryPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> Map<K, V> filterFiltered(AbstractFilteredMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2601 */     return new FilteredEntryMap<>(map.unfiltered, 
/* 2602 */         Predicates.and(map.predicate, entryPredicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> SortedMap<K, V> filterFiltered(FilteredEntrySortedMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2611 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(map.predicate, entryPredicate);
/* 2612 */     return new FilteredEntrySortedMap<>(map.sortedMap(), predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   private static <K, V> NavigableMap<K, V> filterFiltered(FilteredEntryNavigableMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2623 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(map.entryPredicate, entryPredicate);
/* 2624 */     return new FilteredEntryNavigableMap<>(map.unfiltered, predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> BiMap<K, V> filterFiltered(FilteredEntryBiMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2633 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(map.predicate, entryPredicate);
/* 2634 */     return new FilteredEntryBiMap<>(map.unfiltered(), predicate);
/*      */   }
/*      */   
/*      */   private static abstract class AbstractFilteredMap<K, V> extends ViewCachingAbstractMap<K, V> {
/*      */     final Map<K, V> unfiltered;
/*      */     final Predicate<? super Map.Entry<K, V>> predicate;
/*      */     
/*      */     AbstractFilteredMap(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> predicate) {
/* 2642 */       this.unfiltered = unfiltered;
/* 2643 */       this.predicate = predicate;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean apply(Object key, V value) {
/* 2650 */       K k = (K)key;
/* 2651 */       return this.predicate.apply(Maps.immutableEntry(k, value));
/*      */     }
/*      */ 
/*      */     
/*      */     public V put(K key, V value) {
/* 2656 */       Preconditions.checkArgument(apply(key, value));
/* 2657 */       return this.unfiltered.put(key, value);
/*      */     }
/*      */ 
/*      */     
/*      */     public void putAll(Map<? extends K, ? extends V> map) {
/* 2662 */       for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 2663 */         Preconditions.checkArgument(apply(entry.getKey(), entry.getValue()));
/*      */       }
/* 2665 */       this.unfiltered.putAll(map);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 2670 */       return (this.unfiltered.containsKey(key) && apply(key, this.unfiltered.get(key)));
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(Object key) {
/* 2675 */       V value = this.unfiltered.get(key);
/* 2676 */       return (value != null && apply(key, value)) ? value : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 2681 */       return entrySet().isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public V remove(Object key) {
/* 2686 */       return containsKey(key) ? this.unfiltered.remove(key) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     Collection<V> createValues() {
/* 2691 */       return new Maps.FilteredMapValues<>(this, this.unfiltered, this.predicate);
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class FilteredMapValues<K, V>
/*      */     extends Values<K, V> {
/*      */     final Map<K, V> unfiltered;
/*      */     final Predicate<? super Map.Entry<K, V>> predicate;
/*      */     
/*      */     FilteredMapValues(Map<K, V> filteredMap, Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> predicate) {
/* 2701 */       super(filteredMap);
/* 2702 */       this.unfiltered = unfiltered;
/* 2703 */       this.predicate = predicate;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 2708 */       Iterator<Map.Entry<K, V>> entryItr = this.unfiltered.entrySet().iterator();
/* 2709 */       while (entryItr.hasNext()) {
/* 2710 */         Map.Entry<K, V> entry = entryItr.next();
/* 2711 */         if (this.predicate.apply(entry) && Objects.equal(entry.getValue(), o)) {
/* 2712 */           entryItr.remove();
/* 2713 */           return true;
/*      */         } 
/*      */       } 
/* 2716 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> collection) {
/* 2721 */       Iterator<Map.Entry<K, V>> entryItr = this.unfiltered.entrySet().iterator();
/* 2722 */       boolean result = false;
/* 2723 */       while (entryItr.hasNext()) {
/* 2724 */         Map.Entry<K, V> entry = entryItr.next();
/* 2725 */         if (this.predicate.apply(entry) && collection.contains(entry.getValue())) {
/* 2726 */           entryItr.remove();
/* 2727 */           result = true;
/*      */         } 
/*      */       } 
/* 2730 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> collection) {
/* 2735 */       Iterator<Map.Entry<K, V>> entryItr = this.unfiltered.entrySet().iterator();
/* 2736 */       boolean result = false;
/* 2737 */       while (entryItr.hasNext()) {
/* 2738 */         Map.Entry<K, V> entry = entryItr.next();
/* 2739 */         if (this.predicate.apply(entry) && !collection.contains(entry.getValue())) {
/* 2740 */           entryItr.remove();
/* 2741 */           result = true;
/*      */         } 
/*      */       } 
/* 2744 */       return result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 2750 */       return Lists.<V>newArrayList(iterator()).toArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] array) {
/* 2755 */       return (T[])Lists.<V>newArrayList(iterator()).toArray((Object[])array);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class FilteredKeyMap<K, V>
/*      */     extends AbstractFilteredMap<K, V>
/*      */   {
/*      */     final Predicate<? super K> keyPredicate;
/*      */     
/*      */     FilteredKeyMap(Map<K, V> unfiltered, Predicate<? super K> keyPredicate, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2766 */       super(unfiltered, entryPredicate);
/* 2767 */       this.keyPredicate = keyPredicate;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, V>> createEntrySet() {
/* 2772 */       return Sets.filter(this.unfiltered.entrySet(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/* 2777 */       return Sets.filter(this.unfiltered.keySet(), this.keyPredicate);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 2785 */       return (this.unfiltered.containsKey(key) && this.keyPredicate.apply(key));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static class FilteredEntryMap<K, V>
/*      */     extends AbstractFilteredMap<K, V>
/*      */   {
/*      */     final Set<Map.Entry<K, V>> filteredEntrySet;
/*      */ 
/*      */     
/*      */     FilteredEntryMap(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2797 */       super(unfiltered, entryPredicate);
/* 2798 */       this.filteredEntrySet = Sets.filter(unfiltered.entrySet(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, V>> createEntrySet() {
/* 2803 */       return new EntrySet();
/*      */     }
/*      */     
/*      */     private class EntrySet extends ForwardingSet<Map.Entry<K, V>> {
/*      */       private EntrySet() {}
/*      */       
/*      */       protected Set<Map.Entry<K, V>> delegate() {
/* 2810 */         return Maps.FilteredEntryMap.this.filteredEntrySet;
/*      */       }
/*      */ 
/*      */       
/*      */       public Iterator<Map.Entry<K, V>> iterator() {
/* 2815 */         return new TransformedIterator<Map.Entry<K, V>, Map.Entry<K, V>>(Maps.FilteredEntryMap.this.filteredEntrySet.iterator())
/*      */           {
/*      */             Map.Entry<K, V> transform(final Map.Entry<K, V> entry) {
/* 2818 */               return new ForwardingMapEntry<K, V>()
/*      */                 {
/*      */                   protected Map.Entry<K, V> delegate() {
/* 2821 */                     return entry;
/*      */                   }
/*      */ 
/*      */                   
/*      */                   public V setValue(V newValue) {
/* 2826 */                     Preconditions.checkArgument(Maps.FilteredEntryMap.this.apply(getKey(), newValue));
/* 2827 */                     return super.setValue(newValue);
/*      */                   }
/*      */                 };
/*      */             }
/*      */           };
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/* 2837 */       return new KeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     static <K, V> boolean removeAllKeys(Map<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate, Collection<?> keyCollection) {
/* 2842 */       Iterator<Map.Entry<K, V>> entryItr = map.entrySet().iterator();
/* 2843 */       boolean result = false;
/* 2844 */       while (entryItr.hasNext()) {
/* 2845 */         Map.Entry<K, V> entry = entryItr.next();
/* 2846 */         if (entryPredicate.apply(entry) && keyCollection.contains(entry.getKey())) {
/* 2847 */           entryItr.remove();
/* 2848 */           result = true;
/*      */         } 
/*      */       } 
/* 2851 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     static <K, V> boolean retainAllKeys(Map<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate, Collection<?> keyCollection) {
/* 2856 */       Iterator<Map.Entry<K, V>> entryItr = map.entrySet().iterator();
/* 2857 */       boolean result = false;
/* 2858 */       while (entryItr.hasNext()) {
/* 2859 */         Map.Entry<K, V> entry = entryItr.next();
/* 2860 */         if (entryPredicate.apply(entry) && !keyCollection.contains(entry.getKey())) {
/* 2861 */           entryItr.remove();
/* 2862 */           result = true;
/*      */         } 
/*      */       } 
/* 2865 */       return result;
/*      */     }
/*      */     
/*      */     class KeySet
/*      */       extends Maps.KeySet<K, V> {
/*      */       KeySet() {
/* 2871 */         super(Maps.FilteredEntryMap.this);
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean remove(Object o) {
/* 2876 */         if (Maps.FilteredEntryMap.this.containsKey(o)) {
/* 2877 */           Maps.FilteredEntryMap.this.unfiltered.remove(o);
/* 2878 */           return true;
/*      */         } 
/* 2880 */         return false;
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean removeAll(Collection<?> collection) {
/* 2885 */         return Maps.FilteredEntryMap.removeAllKeys(Maps.FilteredEntryMap.this.unfiltered, Maps.FilteredEntryMap.this.predicate, collection);
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean retainAll(Collection<?> collection) {
/* 2890 */         return Maps.FilteredEntryMap.retainAllKeys(Maps.FilteredEntryMap.this.unfiltered, Maps.FilteredEntryMap.this.predicate, collection);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public Object[] toArray() {
/* 2896 */         return Lists.<K>newArrayList(iterator()).toArray();
/*      */       }
/*      */ 
/*      */       
/*      */       public <T> T[] toArray(T[] array) {
/* 2901 */         return (T[])Lists.<K>newArrayList(iterator()).toArray((Object[])array);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static class FilteredEntrySortedMap<K, V>
/*      */     extends FilteredEntryMap<K, V>
/*      */     implements SortedMap<K, V>
/*      */   {
/*      */     FilteredEntrySortedMap(SortedMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2911 */       super(unfiltered, entryPredicate);
/*      */     }
/*      */     
/*      */     SortedMap<K, V> sortedMap() {
/* 2915 */       return (SortedMap<K, V>)this.unfiltered;
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> keySet() {
/* 2920 */       return (SortedSet<K>)super.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     SortedSet<K> createKeySet() {
/* 2925 */       return new SortedKeySet();
/*      */     }
/*      */     
/*      */     class SortedKeySet
/*      */       extends Maps.FilteredEntryMap<K, V>.KeySet
/*      */       implements SortedSet<K> {
/*      */       public Comparator<? super K> comparator() {
/* 2932 */         return Maps.FilteredEntrySortedMap.this.sortedMap().comparator();
/*      */       }
/*      */ 
/*      */       
/*      */       public SortedSet<K> subSet(K fromElement, K toElement) {
/* 2937 */         return (SortedSet<K>)Maps.FilteredEntrySortedMap.this.subMap(fromElement, toElement).keySet();
/*      */       }
/*      */ 
/*      */       
/*      */       public SortedSet<K> headSet(K toElement) {
/* 2942 */         return (SortedSet<K>)Maps.FilteredEntrySortedMap.this.headMap(toElement).keySet();
/*      */       }
/*      */ 
/*      */       
/*      */       public SortedSet<K> tailSet(K fromElement) {
/* 2947 */         return (SortedSet<K>)Maps.FilteredEntrySortedMap.this.tailMap(fromElement).keySet();
/*      */       }
/*      */ 
/*      */       
/*      */       public K first() {
/* 2952 */         return (K)Maps.FilteredEntrySortedMap.this.firstKey();
/*      */       }
/*      */ 
/*      */       
/*      */       public K last() {
/* 2957 */         return (K)Maps.FilteredEntrySortedMap.this.lastKey();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 2963 */       return sortedMap().comparator();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public K firstKey() {
/* 2969 */       return keySet().iterator().next();
/*      */     }
/*      */ 
/*      */     
/*      */     public K lastKey() {
/* 2974 */       SortedMap<K, V> headMap = sortedMap();
/*      */       
/*      */       while (true) {
/* 2977 */         K key = headMap.lastKey();
/* 2978 */         if (apply(key, this.unfiltered.get(key))) {
/* 2979 */           return key;
/*      */         }
/* 2981 */         headMap = sortedMap().headMap(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> headMap(K toKey) {
/* 2987 */       return new FilteredEntrySortedMap(sortedMap().headMap(toKey), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 2992 */       return new FilteredEntrySortedMap(sortedMap().subMap(fromKey, toKey), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> tailMap(K fromKey) {
/* 2997 */       return new FilteredEntrySortedMap(sortedMap().tailMap(fromKey), this.predicate);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   private static class FilteredEntryNavigableMap<K, V>
/*      */     extends AbstractNavigableMap<K, V>
/*      */   {
/*      */     private final NavigableMap<K, V> unfiltered;
/*      */     
/*      */     private final Predicate<? super Map.Entry<K, V>> entryPredicate;
/*      */     
/*      */     private final Map<K, V> filteredDelegate;
/*      */ 
/*      */     
/*      */     FilteredEntryNavigableMap(NavigableMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 3015 */       this.unfiltered = (NavigableMap<K, V>)Preconditions.checkNotNull(unfiltered);
/* 3016 */       this.entryPredicate = entryPredicate;
/* 3017 */       this.filteredDelegate = new Maps.FilteredEntryMap<>(unfiltered, entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 3022 */       return this.unfiltered.comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 3027 */       return new Maps.NavigableKeySet<K, V>(this)
/*      */         {
/*      */           public boolean removeAll(Collection<?> collection) {
/* 3030 */             return Maps.FilteredEntryMap.removeAllKeys(Maps.FilteredEntryNavigableMap.this.unfiltered, Maps.FilteredEntryNavigableMap.this.entryPredicate, collection);
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean retainAll(Collection<?> collection) {
/* 3035 */             return Maps.FilteredEntryMap.retainAllKeys(Maps.FilteredEntryNavigableMap.this.unfiltered, Maps.FilteredEntryNavigableMap.this.entryPredicate, collection);
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/* 3042 */       return new Maps.FilteredMapValues<>(this, this.unfiltered, this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V>> entryIterator() {
/* 3047 */       return Iterators.filter(this.unfiltered.entrySet().iterator(), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V>> descendingEntryIterator() {
/* 3052 */       return Iterators.filter(this.unfiltered.descendingMap().entrySet().iterator(), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 3057 */       return this.filteredDelegate.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 3062 */       return !Iterables.any(this.unfiltered.entrySet(), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(Object key) {
/* 3067 */       return this.filteredDelegate.get(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 3072 */       return this.filteredDelegate.containsKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public V put(K key, V value) {
/* 3077 */       return this.filteredDelegate.put(key, value);
/*      */     }
/*      */ 
/*      */     
/*      */     public V remove(Object key) {
/* 3082 */       return this.filteredDelegate.remove(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public void putAll(Map<? extends K, ? extends V> m) {
/* 3087 */       this.filteredDelegate.putAll(m);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3092 */       this.filteredDelegate.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 3097 */       return this.filteredDelegate.entrySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> pollFirstEntry() {
/* 3102 */       return Iterables.<Map.Entry<K, V>>removeFirstMatching(this.unfiltered.entrySet(), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> pollLastEntry() {
/* 3107 */       return Iterables.<Map.Entry<K, V>>removeFirstMatching(this.unfiltered.descendingMap().entrySet(), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> descendingMap() {
/* 3112 */       return Maps.filterEntries(this.unfiltered.descendingMap(), this.entryPredicate);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 3118 */       return Maps.filterEntries(this.unfiltered
/* 3119 */           .subMap(fromKey, fromInclusive, toKey, toInclusive), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
/* 3124 */       return Maps.filterEntries(this.unfiltered.headMap(toKey, inclusive), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
/* 3129 */       return Maps.filterEntries(this.unfiltered.tailMap(fromKey, inclusive), this.entryPredicate);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class FilteredEntryBiMap<K, V>
/*      */     extends FilteredEntryMap<K, V> implements BiMap<K, V> {
/*      */     @RetainedWith
/*      */     private final BiMap<V, K> inverse;
/*      */     
/*      */     private static <K, V> Predicate<Map.Entry<V, K>> inversePredicate(final Predicate<? super Map.Entry<K, V>> forwardPredicate) {
/* 3139 */       return new Predicate<Map.Entry<V, K>>()
/*      */         {
/*      */           public boolean apply(Map.Entry<V, K> input) {
/* 3142 */             return forwardPredicate.apply(Maps.immutableEntry(input.getValue(), input.getKey()));
/*      */           }
/*      */         };
/*      */     }
/*      */     
/*      */     FilteredEntryBiMap(BiMap<K, V> delegate, Predicate<? super Map.Entry<K, V>> predicate) {
/* 3148 */       super(delegate, predicate);
/* 3149 */       this
/* 3150 */         .inverse = new FilteredEntryBiMap(delegate.inverse(), inversePredicate(predicate), this);
/*      */     }
/*      */ 
/*      */     
/*      */     private FilteredEntryBiMap(BiMap<K, V> delegate, Predicate<? super Map.Entry<K, V>> predicate, BiMap<V, K> inverse) {
/* 3155 */       super(delegate, predicate);
/* 3156 */       this.inverse = inverse;
/*      */     }
/*      */     
/*      */     BiMap<K, V> unfiltered() {
/* 3160 */       return (BiMap<K, V>)this.unfiltered;
/*      */     }
/*      */ 
/*      */     
/*      */     public V forcePut(K key, V value) {
/* 3165 */       Preconditions.checkArgument(apply(key, value));
/* 3166 */       return unfiltered().forcePut(key, value);
/*      */     }
/*      */ 
/*      */     
/*      */     public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/* 3171 */       unfiltered()
/* 3172 */         .replaceAll((key, value) -> this.predicate.apply(Maps.immutableEntry(key, value)) ? function.apply(key, value) : value);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public BiMap<V, K> inverse() {
/* 3181 */       return this.inverse;
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> values() {
/* 3186 */       return this.inverse.keySet();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <K, V> NavigableMap<K, V> unmodifiableNavigableMap(NavigableMap<K, ? extends V> map) {
/* 3211 */     Preconditions.checkNotNull(map);
/* 3212 */     if (map instanceof UnmodifiableNavigableMap)
/*      */     {
/* 3214 */       return (NavigableMap)map;
/*      */     }
/*      */     
/* 3217 */     return new UnmodifiableNavigableMap<>(map);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> Map.Entry<K, V> unmodifiableOrNull(Map.Entry<K, ? extends V> entry) {
/* 3223 */     return (entry == null) ? null : unmodifiableEntry(entry);
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   static class UnmodifiableNavigableMap<K, V> extends ForwardingSortedMap<K, V> implements NavigableMap<K, V>, Serializable {
/*      */     private final NavigableMap<K, ? extends V> delegate;
/*      */     private transient UnmodifiableNavigableMap<K, V> descendingMap;
/*      */     
/*      */     UnmodifiableNavigableMap(NavigableMap<K, ? extends V> delegate) {
/* 3232 */       this.delegate = delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     UnmodifiableNavigableMap(NavigableMap<K, ? extends V> delegate, UnmodifiableNavigableMap<K, V> descendingMap) {
/* 3237 */       this.delegate = delegate;
/* 3238 */       this.descendingMap = descendingMap;
/*      */     }
/*      */ 
/*      */     
/*      */     protected SortedMap<K, V> delegate() {
/* 3243 */       return Collections.unmodifiableSortedMap(this.delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> lowerEntry(K key) {
/* 3248 */       return Maps.unmodifiableOrNull(this.delegate.lowerEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public K lowerKey(K key) {
/* 3253 */       return this.delegate.lowerKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> floorEntry(K key) {
/* 3258 */       return Maps.unmodifiableOrNull(this.delegate.floorEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public K floorKey(K key) {
/* 3263 */       return this.delegate.floorKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> ceilingEntry(K key) {
/* 3268 */       return Maps.unmodifiableOrNull(this.delegate.ceilingEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public K ceilingKey(K key) {
/* 3273 */       return this.delegate.ceilingKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> higherEntry(K key) {
/* 3278 */       return Maps.unmodifiableOrNull(this.delegate.higherEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public K higherKey(K key) {
/* 3283 */       return this.delegate.higherKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> firstEntry() {
/* 3288 */       return Maps.unmodifiableOrNull(this.delegate.firstEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> lastEntry() {
/* 3293 */       return Maps.unmodifiableOrNull(this.delegate.lastEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     public final Map.Entry<K, V> pollFirstEntry() {
/* 3298 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public final Map.Entry<K, V> pollLastEntry() {
/* 3303 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> descendingMap() {
/* 3310 */       UnmodifiableNavigableMap<K, V> result = this.descendingMap;
/* 3311 */       return (result == null) ? (
/* 3312 */         this.descendingMap = new UnmodifiableNavigableMap(this.delegate.descendingMap(), this)) : 
/* 3313 */         result;
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 3318 */       return navigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 3323 */       return Sets.unmodifiableNavigableSet(this.delegate.navigableKeySet());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingKeySet() {
/* 3328 */       return Sets.unmodifiableNavigableSet(this.delegate.descendingKeySet());
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 3333 */       return subMap(fromKey, true, toKey, false);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 3339 */       return Maps.unmodifiableNavigableMap(this.delegate
/* 3340 */           .subMap(fromKey, fromInclusive, toKey, toInclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> headMap(K toKey) {
/* 3345 */       return headMap(toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
/* 3350 */       return Maps.unmodifiableNavigableMap(this.delegate.headMap(toKey, inclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> tailMap(K fromKey) {
/* 3355 */       return tailMap(fromKey, true);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
/* 3360 */       return Maps.unmodifiableNavigableMap(this.delegate.tailMap(fromKey, inclusive));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <K, V> NavigableMap<K, V> synchronizedNavigableMap(NavigableMap<K, V> navigableMap) {
/* 3416 */     return Synchronized.navigableMap(navigableMap);
/*      */   }
/*      */ 
/*      */   
/*      */   @GwtCompatible
/*      */   static abstract class ViewCachingAbstractMap<K, V>
/*      */     extends AbstractMap<K, V>
/*      */   {
/*      */     private transient Set<Map.Entry<K, V>> entrySet;
/*      */     
/*      */     private transient Set<K> keySet;
/*      */     
/*      */     private transient Collection<V> values;
/*      */ 
/*      */     
/*      */     abstract Set<Map.Entry<K, V>> createEntrySet();
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 3435 */       Set<Map.Entry<K, V>> result = this.entrySet;
/* 3436 */       return (result == null) ? (this.entrySet = createEntrySet()) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 3443 */       Set<K> result = this.keySet;
/* 3444 */       return (result == null) ? (this.keySet = createKeySet()) : result;
/*      */     }
/*      */     
/*      */     Set<K> createKeySet() {
/* 3448 */       return new Maps.KeySet<>(this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/* 3455 */       Collection<V> result = this.values;
/* 3456 */       return (result == null) ? (this.values = createValues()) : result;
/*      */     }
/*      */     
/*      */     Collection<V> createValues() {
/* 3460 */       return new Maps.Values<>(this);
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class IteratorBasedAbstractMap<K, V>
/*      */     extends AbstractMap<K, V> {
/*      */     public abstract int size();
/*      */     
/*      */     abstract Iterator<Map.Entry<K, V>> entryIterator();
/*      */     
/*      */     Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 3471 */       return Spliterators.spliterator(
/* 3472 */           entryIterator(), size(), 65);
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 3477 */       return new Maps.EntrySet<K, V>()
/*      */         {
/*      */           Map<K, V> map() {
/* 3480 */             return Maps.IteratorBasedAbstractMap.this;
/*      */           }
/*      */ 
/*      */           
/*      */           public Iterator<Map.Entry<K, V>> iterator() {
/* 3485 */             return Maps.IteratorBasedAbstractMap.this.entryIterator();
/*      */           }
/*      */ 
/*      */           
/*      */           public Spliterator<Map.Entry<K, V>> spliterator() {
/* 3490 */             return Maps.IteratorBasedAbstractMap.this.entrySpliterator();
/*      */           }
/*      */ 
/*      */           
/*      */           public void forEach(Consumer<? super Map.Entry<K, V>> action) {
/* 3495 */             Maps.IteratorBasedAbstractMap.this.forEachEntry(action);
/*      */           }
/*      */         };
/*      */     }
/*      */     
/*      */     void forEachEntry(Consumer<? super Map.Entry<K, V>> action) {
/* 3501 */       entryIterator().forEachRemaining(action);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3506 */       Iterators.clear(entryIterator());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <V> V safeGet(Map<?, V> map, Object key) {
/* 3515 */     Preconditions.checkNotNull(map);
/*      */     try {
/* 3517 */       return map.get(key);
/* 3518 */     } catch (ClassCastException|NullPointerException e) {
/* 3519 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean safeContainsKey(Map<?, ?> map, Object key) {
/* 3528 */     Preconditions.checkNotNull(map);
/*      */     try {
/* 3530 */       return map.containsKey(key);
/* 3531 */     } catch (ClassCastException|NullPointerException e) {
/* 3532 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <V> V safeRemove(Map<?, V> map, Object key) {
/* 3541 */     Preconditions.checkNotNull(map);
/*      */     try {
/* 3543 */       return map.remove(key);
/* 3544 */     } catch (ClassCastException|NullPointerException e) {
/* 3545 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean containsKeyImpl(Map<?, ?> map, Object key) {
/* 3551 */     return Iterators.contains(keyIterator(map.entrySet().iterator()), key);
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean containsValueImpl(Map<?, ?> map, Object value) {
/* 3556 */     return Iterators.contains(valueIterator(map.entrySet().iterator()), value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> boolean containsEntryImpl(Collection<Map.Entry<K, V>> c, Object o) {
/* 3572 */     if (!(o instanceof Map.Entry)) {
/* 3573 */       return false;
/*      */     }
/* 3575 */     return c.contains(unmodifiableEntry((Map.Entry<?, ?>)o));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> boolean removeEntryImpl(Collection<Map.Entry<K, V>> c, Object o) {
/* 3590 */     if (!(o instanceof Map.Entry)) {
/* 3591 */       return false;
/*      */     }
/* 3593 */     return c.remove(unmodifiableEntry((Map.Entry<?, ?>)o));
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean equalsImpl(Map<?, ?> map, Object object) {
/* 3598 */     if (map == object)
/* 3599 */       return true; 
/* 3600 */     if (object instanceof Map) {
/* 3601 */       Map<?, ?> o = (Map<?, ?>)object;
/* 3602 */       return map.entrySet().equals(o.entrySet());
/*      */     } 
/* 3604 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   static String toStringImpl(Map<?, ?> map) {
/* 3609 */     StringBuilder sb = Collections2.newStringBuilderForCollection(map.size()).append('{');
/* 3610 */     boolean first = true;
/* 3611 */     for (Map.Entry<?, ?> entry : map.entrySet()) {
/* 3612 */       if (!first) {
/* 3613 */         sb.append(", ");
/*      */       }
/* 3615 */       first = false;
/* 3616 */       sb.append(entry.getKey()).append('=').append(entry.getValue());
/*      */     } 
/* 3618 */     return sb.append('}').toString();
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> void putAllImpl(Map<K, V> self, Map<? extends K, ? extends V> map) {
/* 3623 */     for (Map.Entry<? extends K, ? extends V> entry : map.entrySet())
/* 3624 */       self.put(entry.getKey(), entry.getValue()); 
/*      */   }
/*      */   
/*      */   static class KeySet<K, V> extends Sets.ImprovedAbstractSet<K> {
/*      */     @Weak
/*      */     final Map<K, V> map;
/*      */     
/*      */     KeySet(Map<K, V> map) {
/* 3632 */       this.map = (Map<K, V>)Preconditions.checkNotNull(map);
/*      */     }
/*      */     
/*      */     Map<K, V> map() {
/* 3636 */       return this.map;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> iterator() {
/* 3641 */       return Maps.keyIterator(map().entrySet().iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> action) {
/* 3646 */       Preconditions.checkNotNull(action);
/*      */       
/* 3648 */       this.map.forEach((k, v) -> action.accept(k));
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 3653 */       return map().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 3658 */       return map().isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 3663 */       return map().containsKey(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 3668 */       if (contains(o)) {
/* 3669 */         map().remove(o);
/* 3670 */         return true;
/*      */       } 
/* 3672 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3677 */       map().clear();
/*      */     }
/*      */   }
/*      */   
/*      */   static <K> K keyOrNull(Map.Entry<K, ?> entry) {
/* 3682 */     return (entry == null) ? null : entry.getKey();
/*      */   }
/*      */   
/*      */   static <V> V valueOrNull(Map.Entry<?, V> entry) {
/* 3686 */     return (entry == null) ? null : entry.getValue();
/*      */   }
/*      */   
/*      */   static class SortedKeySet<K, V> extends KeySet<K, V> implements SortedSet<K> {
/*      */     SortedKeySet(SortedMap<K, V> map) {
/* 3691 */       super(map);
/*      */     }
/*      */ 
/*      */     
/*      */     SortedMap<K, V> map() {
/* 3696 */       return (SortedMap<K, V>)super.map();
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 3701 */       return map().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> subSet(K fromElement, K toElement) {
/* 3706 */       return new SortedKeySet(map().subMap(fromElement, toElement));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> headSet(K toElement) {
/* 3711 */       return new SortedKeySet(map().headMap(toElement));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> tailSet(K fromElement) {
/* 3716 */       return new SortedKeySet(map().tailMap(fromElement));
/*      */     }
/*      */ 
/*      */     
/*      */     public K first() {
/* 3721 */       return map().firstKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public K last() {
/* 3726 */       return map().lastKey();
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   static class NavigableKeySet<K, V> extends SortedKeySet<K, V> implements NavigableSet<K> {
/*      */     NavigableKeySet(NavigableMap<K, V> map) {
/* 3733 */       super(map);
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableMap<K, V> map() {
/* 3738 */       return (NavigableMap<K, V>)this.map;
/*      */     }
/*      */ 
/*      */     
/*      */     public K lower(K e) {
/* 3743 */       return map().lowerKey(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public K floor(K e) {
/* 3748 */       return map().floorKey(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public K ceiling(K e) {
/* 3753 */       return map().ceilingKey(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public K higher(K e) {
/* 3758 */       return map().higherKey(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public K pollFirst() {
/* 3763 */       return Maps.keyOrNull(map().pollFirstEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     public K pollLast() {
/* 3768 */       return Maps.keyOrNull(map().pollLastEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingSet() {
/* 3773 */       return map().descendingKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> descendingIterator() {
/* 3778 */       return descendingSet().iterator();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> subSet(K fromElement, boolean fromInclusive, K toElement, boolean toInclusive) {
/* 3784 */       return map().subMap(fromElement, fromInclusive, toElement, toInclusive).navigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> subSet(K fromElement, K toElement) {
/* 3789 */       return subSet(fromElement, true, toElement, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> headSet(K toElement, boolean inclusive) {
/* 3794 */       return map().headMap(toElement, inclusive).navigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> headSet(K toElement) {
/* 3799 */       return headSet(toElement, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> tailSet(K fromElement, boolean inclusive) {
/* 3804 */       return map().tailMap(fromElement, inclusive).navigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> tailSet(K fromElement) {
/* 3809 */       return tailSet(fromElement, true);
/*      */     } }
/*      */   
/*      */   static class Values<K, V> extends AbstractCollection<V> {
/*      */     @Weak
/*      */     final Map<K, V> map;
/*      */     
/*      */     Values(Map<K, V> map) {
/* 3817 */       this.map = (Map<K, V>)Preconditions.checkNotNull(map);
/*      */     }
/*      */     
/*      */     final Map<K, V> map() {
/* 3821 */       return this.map;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<V> iterator() {
/* 3826 */       return Maps.valueIterator(map().entrySet().iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super V> action) {
/* 3831 */       Preconditions.checkNotNull(action);
/*      */       
/* 3833 */       this.map.forEach((k, v) -> action.accept(v));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/*      */       try {
/* 3839 */         return super.remove(o);
/* 3840 */       } catch (UnsupportedOperationException e) {
/* 3841 */         for (Map.Entry<K, V> entry : map().entrySet()) {
/* 3842 */           if (Objects.equal(o, entry.getValue())) {
/* 3843 */             map().remove(entry.getKey());
/* 3844 */             return true;
/*      */           } 
/*      */         } 
/* 3847 */         return false;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*      */       try {
/* 3854 */         return super.removeAll((Collection)Preconditions.checkNotNull(c));
/* 3855 */       } catch (UnsupportedOperationException e) {
/* 3856 */         Set<K> toRemove = Sets.newHashSet();
/* 3857 */         for (Map.Entry<K, V> entry : map().entrySet()) {
/* 3858 */           if (c.contains(entry.getValue())) {
/* 3859 */             toRemove.add(entry.getKey());
/*      */           }
/*      */         } 
/* 3862 */         return map().keySet().removeAll(toRemove);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*      */       try {
/* 3869 */         return super.retainAll((Collection)Preconditions.checkNotNull(c));
/* 3870 */       } catch (UnsupportedOperationException e) {
/* 3871 */         Set<K> toRetain = Sets.newHashSet();
/* 3872 */         for (Map.Entry<K, V> entry : map().entrySet()) {
/* 3873 */           if (c.contains(entry.getValue())) {
/* 3874 */             toRetain.add(entry.getKey());
/*      */           }
/*      */         } 
/* 3877 */         return map().keySet().retainAll(toRetain);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 3883 */       return map().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 3888 */       return map().isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 3893 */       return map().containsValue(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3898 */       map().clear();
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class EntrySet<K, V>
/*      */     extends Sets.ImprovedAbstractSet<Map.Entry<K, V>> {
/*      */     abstract Map<K, V> map();
/*      */     
/*      */     public int size() {
/* 3907 */       return map().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3912 */       map().clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 3917 */       if (o instanceof Map.Entry) {
/* 3918 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 3919 */         Object key = entry.getKey();
/* 3920 */         V value = Maps.safeGet(map(), key);
/* 3921 */         return (Objects.equal(value, entry.getValue()) && (value != null || map().containsKey(key)));
/*      */       } 
/* 3923 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 3928 */       return map().isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 3933 */       if (contains(o)) {
/* 3934 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 3935 */         return map().keySet().remove(entry.getKey());
/*      */       } 
/* 3937 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*      */       try {
/* 3943 */         return super.removeAll((Collection)Preconditions.checkNotNull(c));
/* 3944 */       } catch (UnsupportedOperationException e) {
/*      */         
/* 3946 */         return Sets.removeAllImpl(this, c.iterator());
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*      */       try {
/* 3953 */         return super.retainAll((Collection)Preconditions.checkNotNull(c));
/* 3954 */       } catch (UnsupportedOperationException e) {
/*      */         
/* 3956 */         Set<Object> keys = Sets.newHashSetWithExpectedSize(c.size());
/* 3957 */         for (Object o : c) {
/* 3958 */           if (contains(o)) {
/* 3959 */             Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 3960 */             keys.add(entry.getKey());
/*      */           } 
/*      */         } 
/* 3963 */         return map().keySet().retainAll(keys);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   static abstract class DescendingMap<K, V>
/*      */     extends ForwardingMap<K, V> implements NavigableMap<K, V> {
/*      */     private transient Comparator<? super K> comparator;
/*      */     private transient Set<Map.Entry<K, V>> entrySet;
/*      */     private transient NavigableSet<K> navigableKeySet;
/*      */     
/*      */     protected final Map<K, V> delegate() {
/* 3976 */       return forward();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 3984 */       Comparator<? super K> result = this.comparator;
/* 3985 */       if (result == null) {
/* 3986 */         Comparator<? super K> forwardCmp = forward().comparator();
/* 3987 */         if (forwardCmp == null) {
/* 3988 */           forwardCmp = Ordering.natural();
/*      */         }
/* 3990 */         result = this.comparator = reverse(forwardCmp);
/*      */       } 
/* 3992 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     private static <T> Ordering<T> reverse(Comparator<T> forward) {
/* 3997 */       return Ordering.<T>from(forward).reverse();
/*      */     }
/*      */ 
/*      */     
/*      */     public K firstKey() {
/* 4002 */       return forward().lastKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public K lastKey() {
/* 4007 */       return forward().firstKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> lowerEntry(K key) {
/* 4012 */       return forward().higherEntry(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public K lowerKey(K key) {
/* 4017 */       return forward().higherKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> floorEntry(K key) {
/* 4022 */       return forward().ceilingEntry(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public K floorKey(K key) {
/* 4027 */       return forward().ceilingKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> ceilingEntry(K key) {
/* 4032 */       return forward().floorEntry(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public K ceilingKey(K key) {
/* 4037 */       return forward().floorKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> higherEntry(K key) {
/* 4042 */       return forward().lowerEntry(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public K higherKey(K key) {
/* 4047 */       return forward().lowerKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> firstEntry() {
/* 4052 */       return forward().lastEntry();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> lastEntry() {
/* 4057 */       return forward().firstEntry();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> pollFirstEntry() {
/* 4062 */       return forward().pollLastEntry();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> pollLastEntry() {
/* 4067 */       return forward().pollFirstEntry();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> descendingMap() {
/* 4072 */       return forward();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 4079 */       Set<Map.Entry<K, V>> result = this.entrySet;
/* 4080 */       return (result == null) ? (this.entrySet = createEntrySet()) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Set<Map.Entry<K, V>> createEntrySet() {
/*      */       class EntrySetImpl
/*      */         extends Maps.EntrySet<K, V>
/*      */       {
/*      */         Map<K, V> map() {
/* 4090 */           return Maps.DescendingMap.this;
/*      */         }
/*      */ 
/*      */         
/*      */         public Iterator<Map.Entry<K, V>> iterator() {
/* 4095 */           return Maps.DescendingMap.this.entryIterator();
/*      */         }
/*      */       };
/* 4098 */       return new EntrySetImpl();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 4103 */       return navigableKeySet();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 4110 */       NavigableSet<K> result = this.navigableKeySet;
/* 4111 */       return (result == null) ? (this.navigableKeySet = new Maps.NavigableKeySet<>(this)) : result;
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingKeySet() {
/* 4116 */       return forward().navigableKeySet();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 4122 */       return forward().subMap(toKey, toInclusive, fromKey, fromInclusive).descendingMap();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 4127 */       return subMap(fromKey, true, toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
/* 4132 */       return forward().tailMap(toKey, inclusive).descendingMap();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> headMap(K toKey) {
/* 4137 */       return headMap(toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
/* 4142 */       return forward().headMap(fromKey, inclusive).descendingMap();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> tailMap(K fromKey) {
/* 4147 */       return tailMap(fromKey, true);
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/* 4152 */       return new Maps.Values<>(this);
/*      */     }
/*      */     abstract NavigableMap<K, V> forward();
/*      */     
/*      */     public String toString() {
/* 4157 */       return standardToString();
/*      */     }
/*      */     
/*      */     abstract Iterator<Map.Entry<K, V>> entryIterator(); }
/*      */   
/*      */   static <E> ImmutableMap<E, Integer> indexMap(Collection<E> list) {
/* 4163 */     ImmutableMap.Builder<E, Integer> builder = new ImmutableMap.Builder<>(list.size());
/* 4164 */     int i = 0;
/* 4165 */     for (E e : list) {
/* 4166 */       builder.put(e, Integer.valueOf(i++));
/*      */     }
/* 4168 */     return builder.build();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static <K extends Comparable<? super K>, V> NavigableMap<K, V> subMap(NavigableMap<K, V> map, Range<K> range) {
/* 4191 */     if (map.comparator() != null && map
/* 4192 */       .comparator() != Ordering.natural() && range
/* 4193 */       .hasLowerBound() && range
/* 4194 */       .hasUpperBound()) {
/* 4195 */       Preconditions.checkArgument(
/* 4196 */           (map.comparator().compare(range.lowerEndpoint(), range.upperEndpoint()) <= 0), "map is using a custom comparator which is inconsistent with the natural ordering.");
/*      */     }
/*      */     
/* 4199 */     if (range.hasLowerBound() && range.hasUpperBound())
/* 4200 */       return map.subMap(range
/* 4201 */           .lowerEndpoint(), 
/* 4202 */           (range.lowerBoundType() == BoundType.CLOSED), range
/* 4203 */           .upperEndpoint(), 
/* 4204 */           (range.upperBoundType() == BoundType.CLOSED)); 
/* 4205 */     if (range.hasLowerBound())
/* 4206 */       return map.tailMap(range.lowerEndpoint(), (range.lowerBoundType() == BoundType.CLOSED)); 
/* 4207 */     if (range.hasUpperBound()) {
/* 4208 */       return map.headMap(range.upperEndpoint(), (range.upperBoundType() == BoundType.CLOSED));
/*      */     }
/* 4210 */     return (NavigableMap<K, V>)Preconditions.checkNotNull(map);
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface EntryTransformer<K, V1, V2> {
/*      */     V2 transformEntry(K param1K, V1 param1V1);
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\Maps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */