/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.DoNotMock;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.BinaryOperator;
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
/*     */ @GwtCompatible
/*     */ public abstract class ImmutableTable<R, C, V>
/*     */   extends AbstractTable<R, C, V>
/*     */   implements Serializable
/*     */ {
/*     */   public static <T, R, C, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction) {
/*  65 */     return TableCollectors.toImmutableTable(rowFunction, columnFunction, valueFunction);
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
/*     */   public static <T, R, C, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/*  85 */     return TableCollectors.toImmutableTable(rowFunction, columnFunction, valueFunction, mergeFunction);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <R, C, V> ImmutableTable<R, C, V> of() {
/*  92 */     return (ImmutableTable)SparseImmutableTable.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <R, C, V> ImmutableTable<R, C, V> of(R rowKey, C columnKey, V value) {
/*  97 */     return new SingletonImmutableTable<>(rowKey, columnKey, value);
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
/*     */   public static <R, C, V> ImmutableTable<R, C, V> copyOf(Table<? extends R, ? extends C, ? extends V> table) {
/* 115 */     if (table instanceof ImmutableTable) {
/*     */       
/* 117 */       ImmutableTable<R, C, V> parameterizedTable = (ImmutableTable)table;
/* 118 */       return parameterizedTable;
/*     */     } 
/* 120 */     return copyOf(table.cellSet());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static <R, C, V> ImmutableTable<R, C, V> copyOf(Iterable<? extends Table.Cell<? extends R, ? extends C, ? extends V>> cells) {
/* 126 */     Builder<R, C, V> builder = builder();
/* 127 */     for (Table.Cell<? extends R, ? extends C, ? extends V> cell : cells) {
/* 128 */       builder.put(cell);
/*     */     }
/* 130 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <R, C, V> Builder<R, C, V> builder() {
/* 138 */     return new Builder<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <R, C, V> Table.Cell<R, C, V> cellOf(R rowKey, C columnKey, V value) {
/* 146 */     return Tables.immutableCell(
/* 147 */         (R)Preconditions.checkNotNull(rowKey, "rowKey"), 
/* 148 */         (C)Preconditions.checkNotNull(columnKey, "columnKey"), 
/* 149 */         (V)Preconditions.checkNotNull(value, "value"));
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
/*     */   @DoNotMock
/*     */   public static final class Builder<R, C, V>
/*     */   {
/* 180 */     private final List<Table.Cell<R, C, V>> cells = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */     
/*     */     private Comparator<? super R> rowComparator;
/*     */ 
/*     */     
/*     */     private Comparator<? super C> columnComparator;
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<R, C, V> orderRowsBy(Comparator<? super R> rowComparator) {
/* 193 */       this.rowComparator = (Comparator<? super R>)Preconditions.checkNotNull(rowComparator, "rowComparator");
/* 194 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<R, C, V> orderColumnsBy(Comparator<? super C> columnComparator) {
/* 200 */       this.columnComparator = (Comparator<? super C>)Preconditions.checkNotNull(columnComparator, "columnComparator");
/* 201 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<R, C, V> put(R rowKey, C columnKey, V value) {
/* 210 */       this.cells.add(ImmutableTable.cellOf(rowKey, columnKey, value));
/* 211 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<R, C, V> put(Table.Cell<? extends R, ? extends C, ? extends V> cell) {
/* 220 */       if (cell instanceof Tables.ImmutableCell) {
/* 221 */         Preconditions.checkNotNull(cell.getRowKey(), "row");
/* 222 */         Preconditions.checkNotNull(cell.getColumnKey(), "column");
/* 223 */         Preconditions.checkNotNull(cell.getValue(), "value");
/*     */         
/* 225 */         Table.Cell<? extends R, ? extends C, ? extends V> cell1 = cell;
/* 226 */         this.cells.add(cell1);
/*     */       } else {
/* 228 */         put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
/*     */       } 
/* 230 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<R, C, V> putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 241 */       for (Table.Cell<? extends R, ? extends C, ? extends V> cell : table.cellSet()) {
/* 242 */         put(cell);
/*     */       }
/* 244 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<R, C, V> combine(Builder<R, C, V> other) {
/* 249 */       this.cells.addAll(other.cells);
/* 250 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableTable<R, C, V> build() {
/* 259 */       int size = this.cells.size();
/* 260 */       switch (size) {
/*     */         case 0:
/* 262 */           return ImmutableTable.of();
/*     */         case 1:
/* 264 */           return new SingletonImmutableTable<>(Iterables.<Table.Cell<R, C, V>>getOnlyElement(this.cells));
/*     */       } 
/* 266 */       return RegularImmutableTable.forCells(this.cells, this.rowComparator, this.columnComparator);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<Table.Cell<R, C, V>> cellSet() {
/* 275 */     return (ImmutableSet<Table.Cell<R, C, V>>)super.cellSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final UnmodifiableIterator<Table.Cell<R, C, V>> cellIterator() {
/* 283 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */   
/*     */   final Spliterator<Table.Cell<R, C, V>> cellSpliterator() {
/* 288 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableCollection<V> values() {
/* 293 */     return (ImmutableCollection<V>)super.values();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final Iterator<V> valuesIterator() {
/* 301 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<R, V> column(C columnKey) {
/* 311 */     Preconditions.checkNotNull(columnKey, "columnKey");
/* 312 */     return (ImmutableMap<R, V>)MoreObjects.firstNonNull(
/* 313 */         columnMap().get(columnKey), ImmutableMap.of());
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<C> columnKeySet() {
/* 318 */     return columnMap().keySet();
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
/*     */   public ImmutableMap<C, V> row(R rowKey) {
/* 337 */     Preconditions.checkNotNull(rowKey, "rowKey");
/* 338 */     return (ImmutableMap<C, V>)MoreObjects.firstNonNull(
/* 339 */         rowMap().get(rowKey), ImmutableMap.of());
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<R> rowKeySet() {
/* 344 */     return rowMap().keySet();
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
/*     */   public boolean contains(Object rowKey, Object columnKey) {
/* 358 */     return (get(rowKey, columnKey) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 363 */     return values().contains(value);
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
/*     */   public final void clear() {
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
/*     */   @CanIgnoreReturnValue
/*     */   public final V put(R rowKey, C columnKey, V value) {
/* 388 */     throw new UnsupportedOperationException();
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
/*     */   public final void putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 400 */     throw new UnsupportedOperationException();
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
/*     */   public final V remove(Object rowKey, Object columnKey) {
/* 413 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class SerializedForm
/*     */     implements Serializable
/*     */   {
/*     */     private final Object[] rowKeys;
/*     */ 
/*     */     
/*     */     private final Object[] columnKeys;
/*     */ 
/*     */     
/*     */     private final Object[] cellValues;
/*     */     
/*     */     private final int[] cellRowIndices;
/*     */     
/*     */     private final int[] cellColumnIndices;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     
/*     */     private SerializedForm(Object[] rowKeys, Object[] columnKeys, Object[] cellValues, int[] cellRowIndices, int[] cellColumnIndices) {
/* 437 */       this.rowKeys = rowKeys;
/* 438 */       this.columnKeys = columnKeys;
/* 439 */       this.cellValues = cellValues;
/* 440 */       this.cellRowIndices = cellRowIndices;
/* 441 */       this.cellColumnIndices = cellColumnIndices;
/*     */     }
/*     */ 
/*     */     
/*     */     static SerializedForm create(ImmutableTable<?, ?, ?> table, int[] cellRowIndices, int[] cellColumnIndices) {
/* 446 */       return new SerializedForm(table
/* 447 */           .rowKeySet().toArray(), table
/* 448 */           .columnKeySet().toArray(), table
/* 449 */           .values().toArray(), cellRowIndices, cellColumnIndices);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Object readResolve() {
/* 455 */       if (this.cellValues.length == 0) {
/* 456 */         return ImmutableTable.of();
/*     */       }
/* 458 */       if (this.cellValues.length == 1) {
/* 459 */         return ImmutableTable.of(this.rowKeys[0], this.columnKeys[0], this.cellValues[0]);
/*     */       }
/* 461 */       ImmutableList.Builder<Table.Cell<Object, Object, Object>> cellListBuilder = new ImmutableList.Builder<>(this.cellValues.length);
/*     */       
/* 463 */       for (int i = 0; i < this.cellValues.length; i++) {
/* 464 */         cellListBuilder.add(
/* 465 */             ImmutableTable.cellOf(this.rowKeys[this.cellRowIndices[i]], this.columnKeys[this.cellColumnIndices[i]], this.cellValues[i]));
/*     */       }
/* 467 */       return RegularImmutableTable.forOrderedComponents(cellListBuilder
/* 468 */           .build(), ImmutableSet.copyOf(this.rowKeys), ImmutableSet.copyOf(this.columnKeys));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final Object writeReplace() {
/* 475 */     return createSerializedForm();
/*     */   }
/*     */   
/*     */   abstract ImmutableSet<Table.Cell<R, C, V>> createCellSet();
/*     */   
/*     */   abstract ImmutableCollection<V> createValues();
/*     */   
/*     */   public abstract ImmutableMap<C, Map<R, V>> columnMap();
/*     */   
/*     */   public abstract ImmutableMap<R, Map<C, V>> rowMap();
/*     */   
/*     */   abstract SerializedForm createSerializedForm();
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\ImmutableTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */