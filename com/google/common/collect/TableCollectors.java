/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ @GwtCompatible
/*     */ final class TableCollectors
/*     */ {
/*     */   static <T, R, C, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction) {
/*  38 */     Preconditions.checkNotNull(rowFunction, "rowFunction");
/*  39 */     Preconditions.checkNotNull(columnFunction, "columnFunction");
/*  40 */     Preconditions.checkNotNull(valueFunction, "valueFunction");
/*  41 */     return Collector.of(Builder::new, (builder, t) -> builder.put(rowFunction.apply(t), columnFunction.apply(t), valueFunction.apply(t)), ImmutableTable.Builder::combine, ImmutableTable.Builder::build, new Collector.Characteristics[0]);
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
/*     */   static <T, R, C, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/*  55 */     Preconditions.checkNotNull(rowFunction, "rowFunction");
/*  56 */     Preconditions.checkNotNull(columnFunction, "columnFunction");
/*  57 */     Preconditions.checkNotNull(valueFunction, "valueFunction");
/*  58 */     Preconditions.checkNotNull(mergeFunction, "mergeFunction");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  66 */     return Collector.of(() -> new ImmutableTableCollectorState<>(), (state, input) -> state.put(rowFunction.apply(input), columnFunction.apply(input), valueFunction.apply(input), mergeFunction), (s1, s2) -> s1.combine(s2, mergeFunction), state -> state.toTable(), new Collector.Characteristics[0]);
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
/*     */   static <T, R, C, V, I extends Table<R, C, V>> Collector<T, ?, I> toTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction, Supplier<I> tableSupplier) {
/*  84 */     return toTable(rowFunction, columnFunction, valueFunction, (v1, v2) -> { String str1 = String.valueOf(v1); String str2 = String.valueOf(v2); throw new IllegalStateException((new StringBuilder(24 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("Conflicting values ").append(str1).append(" and ").append(str2).toString()); }tableSupplier);
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
/*     */   static <T, R, C, V, I extends Table<R, C, V>> Collector<T, ?, I> toTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction, Supplier<I> tableSupplier) {
/* 100 */     Preconditions.checkNotNull(rowFunction);
/* 101 */     Preconditions.checkNotNull(columnFunction);
/* 102 */     Preconditions.checkNotNull(valueFunction);
/* 103 */     Preconditions.checkNotNull(mergeFunction);
/* 104 */     Preconditions.checkNotNull(tableSupplier);
/* 105 */     return (Collector)Collector.of(tableSupplier, (table, input) -> mergeTables(table, rowFunction.apply(input), columnFunction.apply(input), valueFunction.apply(input), mergeFunction), (table1, table2) -> { for (Table.Cell<R, C, V> cell2 : (Iterable<Table.Cell<R, C, V>>)table2.cellSet()) mergeTables(table1, cell2.getRowKey(), cell2.getColumnKey(), cell2.getValue(), mergeFunction);  return table1; }new Collector.Characteristics[0]);
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
/*     */   private static final class ImmutableTableCollectorState<R, C, V>
/*     */   {
/* 124 */     final List<TableCollectors.MutableCell<R, C, V>> insertionOrder = new ArrayList<>();
/* 125 */     final Table<R, C, TableCollectors.MutableCell<R, C, V>> table = HashBasedTable.create();
/*     */     
/*     */     void put(R row, C column, V value, BinaryOperator<V> merger) {
/* 128 */       TableCollectors.MutableCell<R, C, V> oldCell = this.table.get(row, column);
/* 129 */       if (oldCell == null) {
/* 130 */         TableCollectors.MutableCell<R, C, V> cell = new TableCollectors.MutableCell<>(row, column, value);
/* 131 */         this.insertionOrder.add(cell);
/* 132 */         this.table.put(row, column, cell);
/*     */       } else {
/* 134 */         oldCell.merge(value, merger);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableTableCollectorState<R, C, V> combine(ImmutableTableCollectorState<R, C, V> other, BinaryOperator<V> merger) {
/* 140 */       for (TableCollectors.MutableCell<R, C, V> cell : other.insertionOrder) {
/* 141 */         put(cell.getRowKey(), cell.getColumnKey(), cell.getValue(), merger);
/*     */       }
/* 143 */       return this;
/*     */     }
/*     */     
/*     */     ImmutableTable<R, C, V> toTable() {
/* 147 */       return ImmutableTable.copyOf((Iterable)this.insertionOrder);
/*     */     }
/*     */     
/*     */     private ImmutableTableCollectorState() {} }
/*     */   
/*     */   private static final class MutableCell<R, C, V> extends Tables.AbstractCell<R, C, V> { private final R row;
/*     */     private final C column;
/*     */     private V value;
/*     */     
/*     */     MutableCell(R row, C column, V value) {
/* 157 */       this.row = (R)Preconditions.checkNotNull(row, "row");
/* 158 */       this.column = (C)Preconditions.checkNotNull(column, "column");
/* 159 */       this.value = (V)Preconditions.checkNotNull(value, "value");
/*     */     }
/*     */ 
/*     */     
/*     */     public R getRowKey() {
/* 164 */       return this.row;
/*     */     }
/*     */ 
/*     */     
/*     */     public C getColumnKey() {
/* 169 */       return this.column;
/*     */     }
/*     */ 
/*     */     
/*     */     public V getValue() {
/* 174 */       return this.value;
/*     */     }
/*     */     
/*     */     void merge(V value, BinaryOperator<V> mergeFunction) {
/* 178 */       Preconditions.checkNotNull(value, "value");
/* 179 */       this.value = (V)Preconditions.checkNotNull(mergeFunction.apply(this.value, value), "mergeFunction.apply");
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <R, C, V> void mergeTables(Table<R, C, V> table, R row, C column, V value, BinaryOperator<V> mergeFunction) {
/* 185 */     Preconditions.checkNotNull(value);
/* 186 */     V oldValue = table.get(row, column);
/* 187 */     if (oldValue == null) {
/* 188 */       table.put(row, column, value);
/*     */     } else {
/* 190 */       V newValue = mergeFunction.apply(oldValue, value);
/* 191 */       if (newValue == null) {
/* 192 */         table.remove(row, column);
/*     */       } else {
/* 194 */         table.put(row, column, newValue);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\TableCollectors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */