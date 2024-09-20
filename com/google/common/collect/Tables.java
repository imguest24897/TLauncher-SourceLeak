/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.BinaryOperator;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
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
/*     */ @GwtCompatible
/*     */ public final class Tables
/*     */ {
/*     */   @Beta
/*     */   public static <T, R, C, V, I extends Table<R, C, V>> Collector<T, ?, I> toTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction, Supplier<I> tableSupplier) {
/*  72 */     return TableCollectors.toTable(rowFunction, columnFunction, valueFunction, tableSupplier);
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
/*     */   public static <T, R, C, V, I extends Table<R, C, V>> Collector<T, ?, I> toTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction, Supplier<I> tableSupplier) {
/*  96 */     return TableCollectors.toTable(rowFunction, columnFunction, valueFunction, mergeFunction, tableSupplier);
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
/*     */   public static <R, C, V> Table.Cell<R, C, V> immutableCell(R rowKey, C columnKey, V value) {
/* 111 */     return new ImmutableCell<>(rowKey, columnKey, value);
/*     */   }
/*     */   
/*     */   static final class ImmutableCell<R, C, V>
/*     */     extends AbstractCell<R, C, V> implements Serializable {
/*     */     private final R rowKey;
/*     */     private final C columnKey;
/*     */     
/*     */     ImmutableCell(R rowKey, C columnKey, V value) {
/* 120 */       this.rowKey = rowKey;
/* 121 */       this.columnKey = columnKey;
/* 122 */       this.value = value;
/*     */     }
/*     */     private final V value; private static final long serialVersionUID = 0L;
/*     */     
/*     */     public R getRowKey() {
/* 127 */       return this.rowKey;
/*     */     }
/*     */ 
/*     */     
/*     */     public C getColumnKey() {
/* 132 */       return this.columnKey;
/*     */     }
/*     */ 
/*     */     
/*     */     public V getValue() {
/* 137 */       return this.value;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static abstract class AbstractCell<R, C, V>
/*     */     implements Table.Cell<R, C, V>
/*     */   {
/*     */     public boolean equals(Object obj) {
/* 149 */       if (obj == this) {
/* 150 */         return true;
/*     */       }
/* 152 */       if (obj instanceof Table.Cell) {
/* 153 */         Table.Cell<?, ?, ?> other = (Table.Cell<?, ?, ?>)obj;
/* 154 */         return (Objects.equal(getRowKey(), other.getRowKey()) && 
/* 155 */           Objects.equal(getColumnKey(), other.getColumnKey()) && 
/* 156 */           Objects.equal(getValue(), other.getValue()));
/*     */       } 
/* 158 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 163 */       return Objects.hashCode(new Object[] { getRowKey(), getColumnKey(), getValue() });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 168 */       String str1 = String.valueOf(getRowKey()), str2 = String.valueOf(getColumnKey()), str3 = String.valueOf(getValue()); return (new StringBuilder(4 + String.valueOf(str1).length() + String.valueOf(str2).length() + String.valueOf(str3).length())).append("(").append(str1).append(",").append(str2).append(")=").append(str3).toString();
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
/*     */   public static <R, C, V> Table<C, R, V> transpose(Table<R, C, V> table) {
/* 185 */     return (table instanceof TransposeTable) ? 
/* 186 */       ((TransposeTable)table).original : 
/* 187 */       new TransposeTable<>(table);
/*     */   }
/*     */   
/*     */   private static class TransposeTable<C, R, V> extends AbstractTable<C, R, V> {
/*     */     final Table<R, C, V> original;
/*     */     
/*     */     TransposeTable(Table<R, C, V> original) {
/* 194 */       this.original = (Table<R, C, V>)Preconditions.checkNotNull(original);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 199 */       this.original.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, V> column(R columnKey) {
/* 204 */       return this.original.row(columnKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<R> columnKeySet() {
/* 209 */       return this.original.rowKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, Map<C, V>> columnMap() {
/* 214 */       return this.original.rowMap();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object rowKey, Object columnKey) {
/* 219 */       return this.original.contains(columnKey, rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsColumn(Object columnKey) {
/* 224 */       return this.original.containsRow(columnKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsRow(Object rowKey) {
/* 229 */       return this.original.containsColumn(rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsValue(Object value) {
/* 234 */       return this.original.containsValue(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(Object rowKey, Object columnKey) {
/* 239 */       return this.original.get(columnKey, rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public V put(C rowKey, R columnKey, V value) {
/* 244 */       return this.original.put(columnKey, rowKey, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void putAll(Table<? extends C, ? extends R, ? extends V> table) {
/* 249 */       this.original.putAll(Tables.transpose(table));
/*     */     }
/*     */ 
/*     */     
/*     */     public V remove(Object rowKey, Object columnKey) {
/* 254 */       return this.original.remove(columnKey, rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, V> row(C rowKey) {
/* 259 */       return this.original.column(rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<C> rowKeySet() {
/* 264 */       return this.original.columnKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, Map<R, V>> rowMap() {
/* 269 */       return this.original.columnMap();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 274 */       return this.original.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<V> values() {
/* 279 */       return this.original.values();
/*     */     }
/*     */ 
/*     */     
/* 283 */     private static final Function<Table.Cell<?, ?, ?>, Table.Cell<?, ?, ?>> TRANSPOSE_CELL = new Function<Table.Cell<?, ?, ?>, Table.Cell<?, ?, ?>>()
/*     */       {
/*     */         public Table.Cell<?, ?, ?> apply(Table.Cell<?, ?, ?> cell)
/*     */         {
/* 287 */           return Tables.immutableCell(cell.getColumnKey(), cell.getRowKey(), cell.getValue());
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */     
/*     */     Iterator<Table.Cell<C, R, V>> cellIterator() {
/* 294 */       return (Iterator)Iterators.transform(this.original.cellSet().iterator(), TRANSPOSE_CELL);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Spliterator<Table.Cell<C, R, V>> cellSpliterator() {
/* 300 */       return (Spliterator)CollectSpliterators.map(this.original.cellSet().spliterator(), (Function<? super Table.Cell<?, ?, ?>, ? extends Table.Cell<?, ?, ?>>)TRANSPOSE_CELL);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static <R, C, V> Table<R, C, V> newCustomTable(Map<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
/* 345 */     Preconditions.checkArgument(backingMap.isEmpty());
/* 346 */     Preconditions.checkNotNull(factory);
/*     */     
/* 348 */     return new StandardTable<>(backingMap, factory);
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
/*     */   @Beta
/*     */   public static <R, C, V1, V2> Table<R, C, V2> transformValues(Table<R, C, V1> fromTable, Function<? super V1, V2> function) {
/* 375 */     return new TransformedTable<>(fromTable, function);
/*     */   }
/*     */   
/*     */   private static class TransformedTable<R, C, V1, V2> extends AbstractTable<R, C, V2> {
/*     */     final Table<R, C, V1> fromTable;
/*     */     final Function<? super V1, V2> function;
/*     */     
/*     */     TransformedTable(Table<R, C, V1> fromTable, Function<? super V1, V2> function) {
/* 383 */       this.fromTable = (Table<R, C, V1>)Preconditions.checkNotNull(fromTable);
/* 384 */       this.function = (Function<? super V1, V2>)Preconditions.checkNotNull(function);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object rowKey, Object columnKey) {
/* 389 */       return this.fromTable.contains(rowKey, columnKey);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public V2 get(Object rowKey, Object columnKey) {
/* 396 */       return contains(rowKey, columnKey) ? (V2)this.function.apply(this.fromTable.get(rowKey, columnKey)) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 401 */       return this.fromTable.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 406 */       this.fromTable.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public V2 put(R rowKey, C columnKey, V2 value) {
/* 411 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public void putAll(Table<? extends R, ? extends C, ? extends V2> table) {
/* 416 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public V2 remove(Object rowKey, Object columnKey) {
/* 421 */       return contains(rowKey, columnKey) ? 
/* 422 */         (V2)this.function.apply(this.fromTable.remove(rowKey, columnKey)) : 
/* 423 */         null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, V2> row(R rowKey) {
/* 428 */       return Maps.transformValues(this.fromTable.row(rowKey), this.function);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, V2> column(C columnKey) {
/* 433 */       return Maps.transformValues(this.fromTable.column(columnKey), this.function);
/*     */     }
/*     */     
/*     */     Function<Table.Cell<R, C, V1>, Table.Cell<R, C, V2>> cellFunction() {
/* 437 */       return new Function<Table.Cell<R, C, V1>, Table.Cell<R, C, V2>>()
/*     */         {
/*     */           public Table.Cell<R, C, V2> apply(Table.Cell<R, C, V1> cell) {
/* 440 */             return Tables.immutableCell(cell
/* 441 */                 .getRowKey(), cell.getColumnKey(), (V2)Tables.TransformedTable.this.function.apply(cell.getValue()));
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     Iterator<Table.Cell<R, C, V2>> cellIterator() {
/* 448 */       return Iterators.transform(this.fromTable.cellSet().iterator(), cellFunction());
/*     */     }
/*     */ 
/*     */     
/*     */     Spliterator<Table.Cell<R, C, V2>> cellSpliterator() {
/* 453 */       return CollectSpliterators.map(this.fromTable.cellSet().spliterator(), (Function<? super Table.Cell<R, C, V1>, ? extends Table.Cell<R, C, V2>>)cellFunction());
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<R> rowKeySet() {
/* 458 */       return this.fromTable.rowKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<C> columnKeySet() {
/* 463 */       return this.fromTable.columnKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     Collection<V2> createValues() {
/* 468 */       return Collections2.transform(this.fromTable.values(), this.function);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, Map<C, V2>> rowMap() {
/* 473 */       Function<Map<C, V1>, Map<C, V2>> rowFunction = new Function<Map<C, V1>, Map<C, V2>>()
/*     */         {
/*     */           public Map<C, V2> apply(Map<C, V1> row)
/*     */           {
/* 477 */             return Maps.transformValues(row, Tables.TransformedTable.this.function);
/*     */           }
/*     */         };
/* 480 */       return Maps.transformValues(this.fromTable.rowMap(), rowFunction);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, Map<R, V2>> columnMap() {
/* 485 */       Function<Map<R, V1>, Map<R, V2>> columnFunction = new Function<Map<R, V1>, Map<R, V2>>()
/*     */         {
/*     */           public Map<R, V2> apply(Map<R, V1> column)
/*     */           {
/* 489 */             return Maps.transformValues(column, Tables.TransformedTable.this.function);
/*     */           }
/*     */         };
/* 492 */       return Maps.transformValues(this.fromTable.columnMap(), columnFunction);
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
/*     */   public static <R, C, V> Table<R, C, V> unmodifiableTable(Table<? extends R, ? extends C, ? extends V> table) {
/* 510 */     return new UnmodifiableTable<>(table);
/*     */   }
/*     */   
/*     */   private static class UnmodifiableTable<R, C, V> extends ForwardingTable<R, C, V> implements Serializable {
/*     */     final Table<? extends R, ? extends C, ? extends V> delegate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     UnmodifiableTable(Table<? extends R, ? extends C, ? extends V> delegate) {
/* 518 */       this.delegate = (Table<? extends R, ? extends C, ? extends V>)Preconditions.checkNotNull(delegate);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected Table<R, C, V> delegate() {
/* 524 */       return (Table)this.delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Table.Cell<R, C, V>> cellSet() {
/* 529 */       return Collections.unmodifiableSet(super.cellSet());
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 534 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, V> column(C columnKey) {
/* 539 */       return Collections.unmodifiableMap(super.column(columnKey));
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<C> columnKeySet() {
/* 544 */       return Collections.unmodifiableSet(super.columnKeySet());
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, Map<R, V>> columnMap() {
/* 549 */       Function<Map<R, V>, Map<R, V>> wrapper = Tables.unmodifiableWrapper();
/* 550 */       return Collections.unmodifiableMap(Maps.transformValues(super.columnMap(), wrapper));
/*     */     }
/*     */ 
/*     */     
/*     */     public V put(R rowKey, C columnKey, V value) {
/* 555 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 560 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public V remove(Object rowKey, Object columnKey) {
/* 565 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, V> row(R rowKey) {
/* 570 */       return Collections.unmodifiableMap(super.row(rowKey));
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<R> rowKeySet() {
/* 575 */       return Collections.unmodifiableSet(super.rowKeySet());
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, Map<C, V>> rowMap() {
/* 580 */       Function<Map<C, V>, Map<C, V>> wrapper = Tables.unmodifiableWrapper();
/* 581 */       return Collections.unmodifiableMap(Maps.transformValues(super.rowMap(), wrapper));
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<V> values() {
/* 586 */       return Collections.unmodifiableCollection(super.values());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static <R, C, V> RowSortedTable<R, C, V> unmodifiableRowSortedTable(RowSortedTable<R, ? extends C, ? extends V> table) {
/* 612 */     return new UnmodifiableRowSortedMap<>(table);
/*     */   }
/*     */   
/*     */   static final class UnmodifiableRowSortedMap<R, C, V> extends UnmodifiableTable<R, C, V> implements RowSortedTable<R, C, V> {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public UnmodifiableRowSortedMap(RowSortedTable<R, ? extends C, ? extends V> delegate) {
/* 619 */       super(delegate);
/*     */     }
/*     */ 
/*     */     
/*     */     protected RowSortedTable<R, C, V> delegate() {
/* 624 */       return (RowSortedTable<R, C, V>)super.delegate();
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedMap<R, Map<C, V>> rowMap() {
/* 629 */       Function<Map<C, V>, Map<C, V>> wrapper = Tables.unmodifiableWrapper();
/* 630 */       return Collections.unmodifiableSortedMap(Maps.transformValues(delegate().rowMap(), wrapper));
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSet<R> rowKeySet() {
/* 635 */       return Collections.unmodifiableSortedSet(delegate().rowKeySet());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K, V> Function<Map<K, V>, Map<K, V>> unmodifiableWrapper() {
/* 643 */     return (Function)UNMODIFIABLE_WRAPPER;
/*     */   }
/*     */   
/* 646 */   private static final Function<? extends Map<?, ?>, ? extends Map<?, ?>> UNMODIFIABLE_WRAPPER = new Function<Map<Object, Object>, Map<Object, Object>>()
/*     */     {
/*     */       public Map<Object, Object> apply(Map<Object, Object> input)
/*     */       {
/* 650 */         return Collections.unmodifiableMap(input);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <R, C, V> Table<R, C, V> synchronizedTable(Table<R, C, V> table) {
/* 684 */     return Synchronized.table(table, null);
/*     */   }
/*     */   
/*     */   static boolean equalsImpl(Table<?, ?, ?> table, Object obj) {
/* 688 */     if (obj == table)
/* 689 */       return true; 
/* 690 */     if (obj instanceof Table) {
/* 691 */       Table<?, ?, ?> that = (Table<?, ?, ?>)obj;
/* 692 */       return table.cellSet().equals(that.cellSet());
/*     */     } 
/* 694 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\Tables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */