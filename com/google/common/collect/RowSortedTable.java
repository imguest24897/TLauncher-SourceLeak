package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

@GwtCompatible
public interface RowSortedTable<R, C, V> extends Table<R, C, V> {
  SortedSet<R> rowKeySet();
  
  SortedMap<R, Map<C, V>> rowMap();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\RowSortedTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */