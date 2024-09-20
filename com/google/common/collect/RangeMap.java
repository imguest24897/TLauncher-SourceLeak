package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.DoNotMock;
import java.util.Map;
import java.util.function.BiFunction;

@DoNotMock("Use ImmutableRangeMap or TreeRangeMap")
@Beta
@GwtIncompatible
public interface RangeMap<K extends Comparable, V> {
  V get(K paramK);
  
  Map.Entry<Range<K>, V> getEntry(K paramK);
  
  Range<K> span();
  
  void put(Range<K> paramRange, V paramV);
  
  void putCoalescing(Range<K> paramRange, V paramV);
  
  void putAll(RangeMap<K, V> paramRangeMap);
  
  void clear();
  
  void remove(Range<K> paramRange);
  
  void merge(Range<K> paramRange, V paramV, BiFunction<? super V, ? super V, ? extends V> paramBiFunction);
  
  Map<Range<K>, V> asMapOfRanges();
  
  Map<Range<K>, V> asDescendingMapOfRanges();
  
  RangeMap<K, V> subRangeMap(Range<K> paramRange);
  
  boolean equals(Object paramObject);
  
  int hashCode();
  
  String toString();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\RangeMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */