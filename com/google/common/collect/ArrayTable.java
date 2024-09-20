/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class ArrayTable<R, C, V>
/*     */   extends AbstractTable<R, C, V>
/*     */   implements Serializable
/*     */ {
/*     */   private final ImmutableList<R> rowList;
/*     */   private final ImmutableList<C> columnList;
/*     */   private final ImmutableMap<R, Integer> rowKeyToIndex;
/*     */   private final ImmutableMap<C, Integer> columnKeyToIndex;
/*     */   private final V[][] array;
/*     */   private transient ColumnMap columnMap;
/*     */   private transient RowMap rowMap;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <R, C, V> ArrayTable<R, C, V> create(Iterable<? extends R> rowKeys, Iterable<? extends C> columnKeys) {
/*  94 */     return new ArrayTable<>(rowKeys, columnKeys);
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
/*     */   public static <R, C, V> ArrayTable<R, C, V> create(Table<R, C, V> table) {
/* 122 */     return (table instanceof ArrayTable) ? 
/* 123 */       new ArrayTable<>((ArrayTable<R, C, V>)table) : 
/* 124 */       new ArrayTable<>(table);
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
/*     */   private ArrayTable(Iterable<? extends R> rowKeys, Iterable<? extends C> columnKeys) {
/* 136 */     this.rowList = ImmutableList.copyOf(rowKeys);
/* 137 */     this.columnList = ImmutableList.copyOf(columnKeys);
/* 138 */     Preconditions.checkArgument((this.rowList.isEmpty() == this.columnList.isEmpty()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 146 */     this.rowKeyToIndex = Maps.indexMap(this.rowList);
/* 147 */     this.columnKeyToIndex = Maps.indexMap(this.columnList);
/*     */ 
/*     */     
/* 150 */     V[][] tmpArray = (V[][])new Object[this.rowList.size()][this.columnList.size()];
/* 151 */     this.array = tmpArray;
/*     */     
/* 153 */     eraseAll();
/*     */   }
/*     */   
/*     */   private ArrayTable(Table<R, C, V> table) {
/* 157 */     this(table.rowKeySet(), table.columnKeySet());
/* 158 */     putAll(table);
/*     */   }
/*     */   
/*     */   private ArrayTable(ArrayTable<R, C, V> table) {
/* 162 */     this.rowList = table.rowList;
/* 163 */     this.columnList = table.columnList;
/* 164 */     this.rowKeyToIndex = table.rowKeyToIndex;
/* 165 */     this.columnKeyToIndex = table.columnKeyToIndex;
/*     */     
/* 167 */     V[][] copy = (V[][])new Object[this.rowList.size()][this.columnList.size()];
/* 168 */     this.array = copy;
/* 169 */     for (int i = 0; i < this.rowList.size(); i++)
/* 170 */       System.arraycopy(table.array[i], 0, copy[i], 0, (table.array[i]).length); 
/*     */   }
/*     */   
/*     */   private static abstract class ArrayMap<K, V>
/*     */     extends Maps.IteratorBasedAbstractMap<K, V> {
/*     */     private final ImmutableMap<K, Integer> keyIndex;
/*     */     
/*     */     private ArrayMap(ImmutableMap<K, Integer> keyIndex) {
/* 178 */       this.keyIndex = keyIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<K> keySet() {
/* 183 */       return this.keyIndex.keySet();
/*     */     }
/*     */     
/*     */     K getKey(int index) {
/* 187 */       return this.keyIndex.keySet().asList().get(index);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 198 */       return this.keyIndex.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 203 */       return this.keyIndex.isEmpty();
/*     */     }
/*     */     
/*     */     Map.Entry<K, V> getEntry(final int index) {
/* 207 */       Preconditions.checkElementIndex(index, size());
/* 208 */       return new AbstractMapEntry<K, V>()
/*     */         {
/*     */           public K getKey() {
/* 211 */             return (K)ArrayTable.ArrayMap.this.getKey(index);
/*     */           }
/*     */ 
/*     */           
/*     */           public V getValue() {
/* 216 */             return (V)ArrayTable.ArrayMap.this.getValue(index);
/*     */           }
/*     */ 
/*     */           
/*     */           public V setValue(V value) {
/* 221 */             return (V)ArrayTable.ArrayMap.this.setValue(index, value);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<K, V>> entryIterator() {
/* 228 */       return new AbstractIndexedListIterator<Map.Entry<K, V>>(size())
/*     */         {
/*     */           protected Map.Entry<K, V> get(int index) {
/* 231 */             return ArrayTable.ArrayMap.this.getEntry(index);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 238 */       return CollectSpliterators.indexed(size(), 16, this::getEntry);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 245 */       return this.keyIndex.containsKey(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(Object key) {
/* 250 */       Integer index = this.keyIndex.get(key);
/* 251 */       if (index == null) {
/* 252 */         return null;
/*     */       }
/* 254 */       return getValue(index.intValue());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public V put(K key, V value) {
/* 260 */       Integer index = this.keyIndex.get(key);
/* 261 */       if (index == null) {
/*     */         
/* 263 */         String str1 = getKeyRole(), str2 = String.valueOf(key), str3 = String.valueOf(this.keyIndex.keySet()); throw new IllegalArgumentException((new StringBuilder(9 + String.valueOf(str1).length() + String.valueOf(str2).length() + String.valueOf(str3).length())).append(str1).append(" ").append(str2).append(" not in ").append(str3).toString());
/*     */       } 
/* 265 */       return setValue(index.intValue(), value);
/*     */     }
/*     */ 
/*     */     
/*     */     public V remove(Object key) {
/* 270 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     abstract String getKeyRole();
/*     */     abstract V getValue(int param1Int);
/*     */     public void clear() {
/* 275 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     abstract V setValue(int param1Int, V param1V);
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableList<R> rowKeyList() {
/* 284 */     return this.rowList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableList<C> columnKeyList() {
/* 292 */     return this.columnList;
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
/*     */   public V at(int rowIndex, int columnIndex) {
/* 309 */     Preconditions.checkElementIndex(rowIndex, this.rowList.size());
/* 310 */     Preconditions.checkElementIndex(columnIndex, this.columnList.size());
/* 311 */     return this.array[rowIndex][columnIndex];
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
/*     */   @CanIgnoreReturnValue
/*     */   public V set(int rowIndex, int columnIndex, V value) {
/* 330 */     Preconditions.checkElementIndex(rowIndex, this.rowList.size());
/* 331 */     Preconditions.checkElementIndex(columnIndex, this.columnList.size());
/* 332 */     V oldValue = this.array[rowIndex][columnIndex];
/* 333 */     this.array[rowIndex][columnIndex] = value;
/* 334 */     return oldValue;
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
/*     */   @GwtIncompatible
/*     */   public V[][] toArray(Class<V> valueClass) {
/* 349 */     V[][] copy = (V[][])Array.newInstance(valueClass, new int[] { this.rowList.size(), this.columnList.size() });
/* 350 */     for (int i = 0; i < this.rowList.size(); i++) {
/* 351 */       System.arraycopy(this.array[i], 0, copy[i], 0, (this.array[i]).length);
/*     */     }
/* 353 */     return copy;
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
/* 365 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void eraseAll() {
/* 370 */     for (V[] row : this.array) {
/* 371 */       Arrays.fill((Object[])row, (Object)null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Object rowKey, Object columnKey) {
/* 381 */     return (containsRow(rowKey) && containsColumn(columnKey));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsColumn(Object columnKey) {
/* 390 */     return this.columnKeyToIndex.containsKey(columnKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsRow(Object rowKey) {
/* 399 */     return this.rowKeyToIndex.containsKey(rowKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 404 */     for (V[] row : this.array) {
/* 405 */       for (V element : row) {
/* 406 */         if (Objects.equal(value, element)) {
/* 407 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 411 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object rowKey, Object columnKey) {
/* 416 */     Integer rowIndex = this.rowKeyToIndex.get(rowKey);
/* 417 */     Integer columnIndex = this.columnKeyToIndex.get(columnKey);
/* 418 */     return (rowIndex == null || columnIndex == null) ? null : at(rowIndex.intValue(), columnIndex.intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 426 */     return (this.rowList.isEmpty() || this.columnList.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V put(R rowKey, C columnKey, V value) {
/* 438 */     Preconditions.checkNotNull(rowKey);
/* 439 */     Preconditions.checkNotNull(columnKey);
/* 440 */     Integer rowIndex = this.rowKeyToIndex.get(rowKey);
/* 441 */     Preconditions.checkArgument((rowIndex != null), "Row %s not in %s", rowKey, this.rowList);
/* 442 */     Integer columnIndex = this.columnKeyToIndex.get(columnKey);
/* 443 */     Preconditions.checkArgument((columnIndex != null), "Column %s not in %s", columnKey, this.columnList);
/* 444 */     return set(rowIndex.intValue(), columnIndex.intValue(), value);
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
/*     */   public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 464 */     super.putAll(table);
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
/*     */   public V remove(Object rowKey, Object columnKey) {
/* 477 */     throw new UnsupportedOperationException();
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
/*     */   @CanIgnoreReturnValue
/*     */   public V erase(Object rowKey, Object columnKey) {
/* 495 */     Integer rowIndex = this.rowKeyToIndex.get(rowKey);
/* 496 */     Integer columnIndex = this.columnKeyToIndex.get(columnKey);
/* 497 */     if (rowIndex == null || columnIndex == null) {
/* 498 */       return null;
/*     */     }
/* 500 */     return set(rowIndex.intValue(), columnIndex.intValue(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 507 */     return this.rowList.size() * this.columnList.size();
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
/*     */   public Set<Table.Cell<R, C, V>> cellSet() {
/* 523 */     return super.cellSet();
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Table.Cell<R, C, V>> cellIterator() {
/* 528 */     return new AbstractIndexedListIterator<Table.Cell<R, C, V>>(size())
/*     */       {
/*     */         protected Table.Cell<R, C, V> get(int index) {
/* 531 */           return ArrayTable.this.getCell(index);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Spliterator<Table.Cell<R, C, V>> cellSpliterator() {
/* 538 */     return CollectSpliterators.indexed(
/* 539 */         size(), 273, this::getCell);
/*     */   }
/*     */   
/*     */   private Table.Cell<R, C, V> getCell(final int index) {
/* 543 */     return new Tables.AbstractCell<R, C, V>() {
/* 544 */         final int rowIndex = index / ArrayTable.this.columnList.size();
/* 545 */         final int columnIndex = index % ArrayTable.this.columnList.size();
/*     */ 
/*     */         
/*     */         public R getRowKey() {
/* 549 */           return (R)ArrayTable.this.rowList.get(this.rowIndex);
/*     */         }
/*     */ 
/*     */         
/*     */         public C getColumnKey() {
/* 554 */           return (C)ArrayTable.this.columnList.get(this.columnIndex);
/*     */         }
/*     */ 
/*     */         
/*     */         public V getValue() {
/* 559 */           return (V)ArrayTable.this.at(this.rowIndex, this.columnIndex);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private V getValue(int index) {
/* 565 */     int rowIndex = index / this.columnList.size();
/* 566 */     int columnIndex = index % this.columnList.size();
/* 567 */     return at(rowIndex, columnIndex);
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
/*     */   public Map<R, V> column(C columnKey) {
/* 583 */     Preconditions.checkNotNull(columnKey);
/* 584 */     Integer columnIndex = this.columnKeyToIndex.get(columnKey);
/* 585 */     return (columnIndex == null) ? ImmutableMap.<R, V>of() : new Column(columnIndex.intValue());
/*     */   }
/*     */   
/*     */   private class Column extends ArrayMap<R, V> {
/*     */     final int columnIndex;
/*     */     
/*     */     Column(int columnIndex) {
/* 592 */       super(ArrayTable.this.rowKeyToIndex);
/* 593 */       this.columnIndex = columnIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     String getKeyRole() {
/* 598 */       return "Row";
/*     */     }
/*     */ 
/*     */     
/*     */     V getValue(int index) {
/* 603 */       return (V)ArrayTable.this.at(index, this.columnIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     V setValue(int index, V newValue) {
/* 608 */       return (V)ArrayTable.this.set(index, this.columnIndex, newValue);
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
/*     */   public ImmutableSet<C> columnKeySet() {
/* 620 */     return this.columnKeyToIndex.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<C, Map<R, V>> columnMap() {
/* 627 */     ColumnMap map = this.columnMap;
/* 628 */     return (map == null) ? (this.columnMap = new ColumnMap()) : map;
/*     */   }
/*     */   
/*     */   private class ColumnMap
/*     */     extends ArrayMap<C, Map<R, V>> {
/*     */     private ColumnMap() {
/* 634 */       super(ArrayTable.this.columnKeyToIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     String getKeyRole() {
/* 639 */       return "Column";
/*     */     }
/*     */ 
/*     */     
/*     */     Map<R, V> getValue(int index) {
/* 644 */       return new ArrayTable.Column(index);
/*     */     }
/*     */ 
/*     */     
/*     */     Map<R, V> setValue(int index, Map<R, V> newValue) {
/* 649 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, V> put(C key, Map<R, V> value) {
/* 654 */       throw new UnsupportedOperationException();
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
/*     */   public Map<C, V> row(R rowKey) {
/* 671 */     Preconditions.checkNotNull(rowKey);
/* 672 */     Integer rowIndex = this.rowKeyToIndex.get(rowKey);
/* 673 */     return (rowIndex == null) ? ImmutableMap.<C, V>of() : new Row(rowIndex.intValue());
/*     */   }
/*     */   
/*     */   private class Row extends ArrayMap<C, V> {
/*     */     final int rowIndex;
/*     */     
/*     */     Row(int rowIndex) {
/* 680 */       super(ArrayTable.this.columnKeyToIndex);
/* 681 */       this.rowIndex = rowIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     String getKeyRole() {
/* 686 */       return "Column";
/*     */     }
/*     */ 
/*     */     
/*     */     V getValue(int index) {
/* 691 */       return (V)ArrayTable.this.at(this.rowIndex, index);
/*     */     }
/*     */ 
/*     */     
/*     */     V setValue(int index, V newValue) {
/* 696 */       return (V)ArrayTable.this.set(this.rowIndex, index, newValue);
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
/*     */   public ImmutableSet<R> rowKeySet() {
/* 708 */     return this.rowKeyToIndex.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<R, Map<C, V>> rowMap() {
/* 715 */     RowMap map = this.rowMap;
/* 716 */     return (map == null) ? (this.rowMap = new RowMap()) : map;
/*     */   }
/*     */   
/*     */   private class RowMap
/*     */     extends ArrayMap<R, Map<C, V>> {
/*     */     private RowMap() {
/* 722 */       super(ArrayTable.this.rowKeyToIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     String getKeyRole() {
/* 727 */       return "Row";
/*     */     }
/*     */ 
/*     */     
/*     */     Map<C, V> getValue(int index) {
/* 732 */       return new ArrayTable.Row(index);
/*     */     }
/*     */ 
/*     */     
/*     */     Map<C, V> setValue(int index, Map<C, V> newValue) {
/* 737 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, V> put(R key, Map<C, V> value) {
/* 742 */       throw new UnsupportedOperationException();
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
/*     */   public Collection<V> values() {
/* 757 */     return super.values();
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<V> valuesIterator() {
/* 762 */     return new AbstractIndexedListIterator<V>(size())
/*     */       {
/*     */         protected V get(int index) {
/* 765 */           return ArrayTable.this.getValue(index);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Spliterator<V> valuesSpliterator() {
/* 772 */     return CollectSpliterators.indexed(size(), 16, this::getValue);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\ArrayTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */