/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ class StandardTable<R, C, V>
/*     */   extends AbstractTable<R, C, V>
/*     */   implements Serializable
/*     */ {
/*     */   @GwtTransient
/*     */   final Map<R, Map<C, V>> backingMap;
/*     */   @GwtTransient
/*     */   final Supplier<? extends Map<C, V>> factory;
/*     */   private transient Set<C> columnKeySet;
/*     */   private transient Map<R, Map<C, V>> rowMap;
/*     */   private transient ColumnMap columnMap;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   StandardTable(Map<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
/*  72 */     this.backingMap = backingMap;
/*  73 */     this.factory = factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Object rowKey, Object columnKey) {
/*  80 */     return (rowKey != null && columnKey != null && super.contains(rowKey, columnKey));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsColumn(Object columnKey) {
/*  85 */     if (columnKey == null) {
/*  86 */       return false;
/*     */     }
/*  88 */     for (Map<C, V> map : this.backingMap.values()) {
/*  89 */       if (Maps.safeContainsKey(map, columnKey)) {
/*  90 */         return true;
/*     */       }
/*     */     } 
/*  93 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsRow(Object rowKey) {
/*  98 */     return (rowKey != null && Maps.safeContainsKey(this.backingMap, rowKey));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 103 */     return (value != null && super.containsValue(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object rowKey, Object columnKey) {
/* 108 */     return (rowKey == null || columnKey == null) ? null : super.get(rowKey, columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 113 */     return this.backingMap.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 118 */     int size = 0;
/* 119 */     for (Map<C, V> map : this.backingMap.values()) {
/* 120 */       size += map.size();
/*     */     }
/* 122 */     return size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 129 */     this.backingMap.clear();
/*     */   }
/*     */   
/*     */   private Map<C, V> getOrCreate(R rowKey) {
/* 133 */     Map<C, V> map = this.backingMap.get(rowKey);
/* 134 */     if (map == null) {
/* 135 */       map = (Map<C, V>)this.factory.get();
/* 136 */       this.backingMap.put(rowKey, map);
/*     */     } 
/* 138 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V put(R rowKey, C columnKey, V value) {
/* 144 */     Preconditions.checkNotNull(rowKey);
/* 145 */     Preconditions.checkNotNull(columnKey);
/* 146 */     Preconditions.checkNotNull(value);
/* 147 */     return getOrCreate(rowKey).put(columnKey, value);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V remove(Object rowKey, Object columnKey) {
/* 153 */     if (rowKey == null || columnKey == null) {
/* 154 */       return null;
/*     */     }
/* 156 */     Map<C, V> map = Maps.<Map<C, V>>safeGet(this.backingMap, rowKey);
/* 157 */     if (map == null) {
/* 158 */       return null;
/*     */     }
/* 160 */     V value = map.remove(columnKey);
/* 161 */     if (map.isEmpty()) {
/* 162 */       this.backingMap.remove(rowKey);
/*     */     }
/* 164 */     return value;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private Map<R, V> removeColumn(Object column) {
/* 169 */     Map<R, V> output = new LinkedHashMap<>();
/* 170 */     Iterator<Map.Entry<R, Map<C, V>>> iterator = this.backingMap.entrySet().iterator();
/* 171 */     while (iterator.hasNext()) {
/* 172 */       Map.Entry<R, Map<C, V>> entry = iterator.next();
/* 173 */       V value = (V)((Map)entry.getValue()).remove(column);
/* 174 */       if (value != null) {
/* 175 */         output.put(entry.getKey(), value);
/* 176 */         if (((Map)entry.getValue()).isEmpty()) {
/* 177 */           iterator.remove();
/*     */         }
/*     */       } 
/*     */     } 
/* 181 */     return output;
/*     */   }
/*     */   
/*     */   private boolean containsMapping(Object rowKey, Object columnKey, Object value) {
/* 185 */     return (value != null && value.equals(get(rowKey, columnKey)));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean removeMapping(Object rowKey, Object columnKey, Object value) {
/* 190 */     if (containsMapping(rowKey, columnKey, value)) {
/* 191 */       remove(rowKey, columnKey);
/* 192 */       return true;
/*     */     } 
/* 194 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private abstract class TableSet<T>
/*     */     extends Sets.ImprovedAbstractSet<T>
/*     */   {
/*     */     private TableSet() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 207 */       return StandardTable.this.backingMap.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 212 */       StandardTable.this.backingMap.clear();
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
/*     */   public Set<Table.Cell<R, C, V>> cellSet() {
/* 227 */     return super.cellSet();
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Table.Cell<R, C, V>> cellIterator() {
/* 232 */     return new CellIterator();
/*     */   }
/*     */   
/*     */   private class CellIterator implements Iterator<Table.Cell<R, C, V>> {
/* 236 */     final Iterator<Map.Entry<R, Map<C, V>>> rowIterator = StandardTable.this.backingMap.entrySet().iterator();
/*     */     Map.Entry<R, Map<C, V>> rowEntry;
/* 238 */     Iterator<Map.Entry<C, V>> columnIterator = Iterators.emptyModifiableIterator();
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 242 */       return (this.rowIterator.hasNext() || this.columnIterator.hasNext());
/*     */     }
/*     */ 
/*     */     
/*     */     public Table.Cell<R, C, V> next() {
/* 247 */       if (!this.columnIterator.hasNext()) {
/* 248 */         this.rowEntry = this.rowIterator.next();
/* 249 */         this.columnIterator = ((Map<C, V>)this.rowEntry.getValue()).entrySet().iterator();
/*     */       } 
/* 251 */       Map.Entry<C, V> columnEntry = this.columnIterator.next();
/* 252 */       return Tables.immutableCell(this.rowEntry.getKey(), columnEntry.getKey(), columnEntry.getValue());
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 257 */       this.columnIterator.remove();
/* 258 */       if (((Map)this.rowEntry.getValue()).isEmpty()) {
/* 259 */         this.rowIterator.remove();
/* 260 */         this.rowEntry = null;
/*     */       } 
/*     */     }
/*     */     
/*     */     private CellIterator() {} }
/*     */   
/*     */   Spliterator<Table.Cell<R, C, V>> cellSpliterator() {
/* 267 */     return CollectSpliterators.flatMap(this.backingMap
/* 268 */         .entrySet().spliterator(), rowEntry -> CollectSpliterators.map(((Map)rowEntry.getValue()).entrySet().spliterator(), ()), 65, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 276 */         size());
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<C, V> row(R rowKey) {
/* 281 */     return new Row(rowKey);
/*     */   }
/*     */   
/*     */   class Row extends Maps.IteratorBasedAbstractMap<C, V> { final R rowKey;
/*     */     Map<C, V> backingRowMap;
/*     */     
/*     */     Row(R rowKey) {
/* 288 */       this.rowKey = (R)Preconditions.checkNotNull(rowKey);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Map<C, V> backingRowMap() {
/* 294 */       return (this.backingRowMap == null || (this.backingRowMap.isEmpty() && StandardTable.this.backingMap.containsKey(this.rowKey))) ? (
/* 295 */         this.backingRowMap = computeBackingRowMap()) : 
/* 296 */         this.backingRowMap;
/*     */     }
/*     */     
/*     */     Map<C, V> computeBackingRowMap() {
/* 300 */       return (Map<C, V>)StandardTable.this.backingMap.get(this.rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     void maintainEmptyInvariant() {
/* 305 */       if (backingRowMap() != null && this.backingRowMap.isEmpty()) {
/* 306 */         StandardTable.this.backingMap.remove(this.rowKey);
/* 307 */         this.backingRowMap = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 313 */       Map<C, V> backingRowMap = backingRowMap();
/* 314 */       return (key != null && backingRowMap != null && Maps.safeContainsKey(backingRowMap, key));
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(Object key) {
/* 319 */       Map<C, V> backingRowMap = backingRowMap();
/* 320 */       return (key != null && backingRowMap != null) ? Maps.<V>safeGet(backingRowMap, key) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public V put(C key, V value) {
/* 325 */       Preconditions.checkNotNull(key);
/* 326 */       Preconditions.checkNotNull(value);
/* 327 */       if (this.backingRowMap != null && !this.backingRowMap.isEmpty()) {
/* 328 */         return this.backingRowMap.put(key, value);
/*     */       }
/* 330 */       return StandardTable.this.put(this.rowKey, key, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public V remove(Object key) {
/* 335 */       Map<C, V> backingRowMap = backingRowMap();
/* 336 */       if (backingRowMap == null) {
/* 337 */         return null;
/*     */       }
/* 339 */       V result = Maps.safeRemove(backingRowMap, key);
/* 340 */       maintainEmptyInvariant();
/* 341 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 346 */       Map<C, V> backingRowMap = backingRowMap();
/* 347 */       if (backingRowMap != null) {
/* 348 */         backingRowMap.clear();
/*     */       }
/* 350 */       maintainEmptyInvariant();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 355 */       Map<C, V> map = backingRowMap();
/* 356 */       return (map == null) ? 0 : map.size();
/*     */     }
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<C, V>> entryIterator() {
/* 361 */       Map<C, V> map = backingRowMap();
/* 362 */       if (map == null) {
/* 363 */         return Iterators.emptyModifiableIterator();
/*     */       }
/* 365 */       final Iterator<Map.Entry<C, V>> iterator = map.entrySet().iterator();
/* 366 */       return new Iterator<Map.Entry<C, V>>()
/*     */         {
/*     */           public boolean hasNext() {
/* 369 */             return iterator.hasNext();
/*     */           }
/*     */ 
/*     */           
/*     */           public Map.Entry<C, V> next() {
/* 374 */             return StandardTable.Row.this.wrapEntry(iterator.next());
/*     */           }
/*     */ 
/*     */           
/*     */           public void remove() {
/* 379 */             iterator.remove();
/* 380 */             StandardTable.Row.this.maintainEmptyInvariant();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     Spliterator<Map.Entry<C, V>> entrySpliterator() {
/* 387 */       Map<C, V> map = backingRowMap();
/* 388 */       if (map == null) {
/* 389 */         return Spliterators.emptySpliterator();
/*     */       }
/* 391 */       return CollectSpliterators.map(map.entrySet().spliterator(), this::wrapEntry);
/*     */     }
/*     */     
/*     */     Map.Entry<C, V> wrapEntry(final Map.Entry<C, V> entry) {
/* 395 */       return new ForwardingMapEntry<C, V>(this)
/*     */         {
/*     */           protected Map.Entry<C, V> delegate() {
/* 398 */             return entry;
/*     */           }
/*     */ 
/*     */           
/*     */           public V setValue(V value) {
/* 403 */             return super.setValue((V)Preconditions.checkNotNull(value));
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public boolean equals(Object object) {
/* 409 */             return standardEquals(object);
/*     */           }
/*     */         };
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<R, V> column(C columnKey) {
/* 422 */     return new Column(columnKey);
/*     */   }
/*     */   
/*     */   private class Column extends Maps.ViewCachingAbstractMap<R, V> {
/*     */     final C columnKey;
/*     */     
/*     */     Column(C columnKey) {
/* 429 */       this.columnKey = (C)Preconditions.checkNotNull(columnKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public V put(R key, V value) {
/* 434 */       return StandardTable.this.put(key, this.columnKey, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(Object key) {
/* 439 */       return (V)StandardTable.this.get(key, this.columnKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 444 */       return StandardTable.this.contains(key, this.columnKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public V remove(Object key) {
/* 449 */       return (V)StandardTable.this.remove(key, this.columnKey);
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     boolean removeFromColumnIf(Predicate<? super Map.Entry<R, V>> predicate) {
/* 455 */       boolean changed = false;
/* 456 */       Iterator<Map.Entry<R, Map<C, V>>> iterator = StandardTable.this.backingMap.entrySet().iterator();
/* 457 */       while (iterator.hasNext()) {
/* 458 */         Map.Entry<R, Map<C, V>> entry = iterator.next();
/* 459 */         Map<C, V> map = entry.getValue();
/* 460 */         V value = map.get(this.columnKey);
/* 461 */         if (value != null && predicate.apply(Maps.immutableEntry(entry.getKey(), value))) {
/* 462 */           map.remove(this.columnKey);
/* 463 */           changed = true;
/* 464 */           if (map.isEmpty()) {
/* 465 */             iterator.remove();
/*     */           }
/*     */         } 
/*     */       } 
/* 469 */       return changed;
/*     */     }
/*     */ 
/*     */     
/*     */     Set<Map.Entry<R, V>> createEntrySet() {
/* 474 */       return new EntrySet();
/*     */     }
/*     */     
/*     */     private class EntrySet extends Sets.ImprovedAbstractSet<Map.Entry<R, V>> {
/*     */       private EntrySet() {}
/*     */       
/*     */       public Iterator<Map.Entry<R, V>> iterator() {
/* 481 */         return new StandardTable.Column.EntrySetIterator();
/*     */       }
/*     */ 
/*     */       
/*     */       public int size() {
/* 486 */         int size = 0;
/* 487 */         for (Map<C, V> map : (Iterable<Map<C, V>>)StandardTable.this.backingMap.values()) {
/* 488 */           if (map.containsKey(StandardTable.Column.this.columnKey)) {
/* 489 */             size++;
/*     */           }
/*     */         } 
/* 492 */         return size;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isEmpty() {
/* 497 */         return !StandardTable.this.containsColumn(StandardTable.Column.this.columnKey);
/*     */       }
/*     */ 
/*     */       
/*     */       public void clear() {
/* 502 */         StandardTable.Column.this.removeFromColumnIf(Predicates.alwaysTrue());
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean contains(Object o) {
/* 507 */         if (o instanceof Map.Entry) {
/* 508 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 509 */           return StandardTable.this.containsMapping(entry.getKey(), StandardTable.Column.this.columnKey, entry.getValue());
/*     */         } 
/* 511 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(Object obj) {
/* 516 */         if (obj instanceof Map.Entry) {
/* 517 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 518 */           return StandardTable.this.removeMapping(entry.getKey(), StandardTable.Column.this.columnKey, entry.getValue());
/*     */         } 
/* 520 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean retainAll(Collection<?> c) {
/* 525 */         return StandardTable.Column.this.removeFromColumnIf(Predicates.not(Predicates.in(c)));
/*     */       }
/*     */     }
/*     */     
/*     */     private class EntrySetIterator extends AbstractIterator<Map.Entry<R, V>> {
/* 530 */       final Iterator<Map.Entry<R, Map<C, V>>> iterator = StandardTable.this.backingMap.entrySet().iterator();
/*     */ 
/*     */       
/*     */       protected Map.Entry<R, V> computeNext() {
/* 534 */         while (this.iterator.hasNext())
/* 535 */         { final Map.Entry<R, Map<C, V>> entry = this.iterator.next();
/* 536 */           if (((Map)entry.getValue()).containsKey(StandardTable.Column.this.columnKey))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 554 */             return new EntryImpl(); }  }  class EntryImpl extends AbstractMapEntry<R, V> {
/*     */           public R getKey() { return (R)entry.getKey(); }
/*     */           public V getValue() { return (V)((Map)entry.getValue()).get(StandardTable.Column.this.columnKey); } public V setValue(V value) { return ((Map<C, V>)entry.getValue()).put(StandardTable.Column.this.columnKey, (V)Preconditions.checkNotNull(value)); }
/* 557 */         }; return endOfData();
/*     */       }
/*     */       
/*     */       private EntrySetIterator() {} }
/*     */     
/*     */     Set<R> createKeySet() {
/* 563 */       return new KeySet();
/*     */     }
/*     */     
/*     */     private class KeySet
/*     */       extends Maps.KeySet<R, V> {
/*     */       KeySet() {
/* 569 */         super(StandardTable.Column.this);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean contains(Object obj) {
/* 574 */         return StandardTable.this.contains(obj, StandardTable.Column.this.columnKey);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(Object obj) {
/* 579 */         return (StandardTable.this.remove(obj, StandardTable.Column.this.columnKey) != null);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean retainAll(Collection<?> c) {
/* 584 */         return StandardTable.Column.this.removeFromColumnIf((Predicate)Maps.keyPredicateOnEntries(Predicates.not(Predicates.in(c))));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     Collection<V> createValues() {
/* 590 */       return new Values();
/*     */     }
/*     */     
/*     */     private class Values
/*     */       extends Maps.Values<R, V> {
/*     */       Values() {
/* 596 */         super(StandardTable.Column.this);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(Object obj) {
/* 601 */         return (obj != null && StandardTable.Column.this.removeFromColumnIf((Predicate)Maps.valuePredicateOnEntries(Predicates.equalTo(obj))));
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean removeAll(Collection<?> c) {
/* 606 */         return StandardTable.Column.this.removeFromColumnIf((Predicate)Maps.valuePredicateOnEntries(Predicates.in(c)));
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean retainAll(Collection<?> c) {
/* 611 */         return StandardTable.Column.this.removeFromColumnIf((Predicate)Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(c))));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<R> rowKeySet() {
/* 618 */     return rowMap().keySet();
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
/*     */   public Set<C> columnKeySet() {
/* 633 */     Set<C> result = this.columnKeySet;
/* 634 */     return (result == null) ? (this.columnKeySet = new ColumnKeySet()) : result;
/*     */   }
/*     */   
/*     */   private class ColumnKeySet extends TableSet<C> {
/*     */     private ColumnKeySet() {}
/*     */     
/*     */     public Iterator<C> iterator() {
/* 641 */       return StandardTable.this.createColumnKeyIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 646 */       return Iterators.size(iterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object obj) {
/* 651 */       if (obj == null) {
/* 652 */         return false;
/*     */       }
/* 654 */       boolean changed = false;
/* 655 */       Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
/* 656 */       while (iterator.hasNext()) {
/* 657 */         Map<C, V> map = iterator.next();
/* 658 */         if (map.keySet().remove(obj)) {
/* 659 */           changed = true;
/* 660 */           if (map.isEmpty()) {
/* 661 */             iterator.remove();
/*     */           }
/*     */         } 
/*     */       } 
/* 665 */       return changed;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection<?> c) {
/* 670 */       Preconditions.checkNotNull(c);
/* 671 */       boolean changed = false;
/* 672 */       Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
/* 673 */       while (iterator.hasNext()) {
/* 674 */         Map<C, V> map = iterator.next();
/*     */ 
/*     */         
/* 677 */         if (Iterators.removeAll(map.keySet().iterator(), c)) {
/* 678 */           changed = true;
/* 679 */           if (map.isEmpty()) {
/* 680 */             iterator.remove();
/*     */           }
/*     */         } 
/*     */       } 
/* 684 */       return changed;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection<?> c) {
/* 689 */       Preconditions.checkNotNull(c);
/* 690 */       boolean changed = false;
/* 691 */       Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
/* 692 */       while (iterator.hasNext()) {
/* 693 */         Map<C, V> map = iterator.next();
/* 694 */         if (map.keySet().retainAll(c)) {
/* 695 */           changed = true;
/* 696 */           if (map.isEmpty()) {
/* 697 */             iterator.remove();
/*     */           }
/*     */         } 
/*     */       } 
/* 701 */       return changed;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object obj) {
/* 706 */       return StandardTable.this.containsColumn(obj);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<C> createColumnKeyIterator() {
/* 712 */     return new ColumnKeyIterator();
/*     */   }
/*     */   
/*     */   private class ColumnKeyIterator
/*     */     extends AbstractIterator<C>
/*     */   {
/* 718 */     final Map<C, V> seen = (Map<C, V>)StandardTable.this.factory.get();
/* 719 */     final Iterator<Map<C, V>> mapIterator = StandardTable.this.backingMap.values().iterator();
/* 720 */     Iterator<Map.Entry<C, V>> entryIterator = Iterators.emptyIterator();
/*     */ 
/*     */     
/*     */     protected C computeNext() {
/*     */       while (true) {
/* 725 */         while (this.entryIterator.hasNext()) {
/* 726 */           Map.Entry<C, V> entry = this.entryIterator.next();
/* 727 */           if (!this.seen.containsKey(entry.getKey())) {
/* 728 */             this.seen.put(entry.getKey(), entry.getValue());
/* 729 */             return entry.getKey();
/*     */           } 
/* 731 */         }  if (this.mapIterator.hasNext()) {
/* 732 */           this.entryIterator = ((Map<C, V>)this.mapIterator.next()).entrySet().iterator(); continue;
/*     */         }  break;
/* 734 */       }  return endOfData();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private ColumnKeyIterator() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 748 */     return super.values();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<R, Map<C, V>> rowMap() {
/* 755 */     Map<R, Map<C, V>> result = this.rowMap;
/* 756 */     return (result == null) ? (this.rowMap = createRowMap()) : result;
/*     */   }
/*     */   
/*     */   Map<R, Map<C, V>> createRowMap() {
/* 760 */     return new RowMap();
/*     */   }
/*     */   
/*     */   class RowMap
/*     */     extends Maps.ViewCachingAbstractMap<R, Map<C, V>>
/*     */   {
/*     */     public boolean containsKey(Object key) {
/* 767 */       return StandardTable.this.containsRow(key);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<C, V> get(Object key) {
/* 774 */       return StandardTable.this.containsRow(key) ? StandardTable.this.row(key) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, V> remove(Object key) {
/* 779 */       return (key == null) ? null : (Map<C, V>)StandardTable.this.backingMap.remove(key);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Set<Map.Entry<R, Map<C, V>>> createEntrySet() {
/* 784 */       return new EntrySet();
/*     */     }
/*     */     
/*     */     class EntrySet
/*     */       extends StandardTable<R, C, V>.TableSet<Map.Entry<R, Map<C, V>>>
/*     */     {
/*     */       public Iterator<Map.Entry<R, Map<C, V>>> iterator() {
/* 791 */         return Maps.asMapEntryIterator(StandardTable.this.backingMap
/* 792 */             .keySet(), new Function<R, Map<C, V>>()
/*     */             {
/*     */               public Map<C, V> apply(R rowKey)
/*     */               {
/* 796 */                 return StandardTable.this.row(rowKey);
/*     */               }
/*     */             });
/*     */       }
/*     */ 
/*     */       
/*     */       public int size() {
/* 803 */         return StandardTable.this.backingMap.size();
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean contains(Object obj) {
/* 808 */         if (obj instanceof Map.Entry) {
/* 809 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 810 */           return (entry.getKey() != null && entry
/* 811 */             .getValue() instanceof Map && 
/* 812 */             Collections2.safeContains(StandardTable.this.backingMap.entrySet(), entry));
/*     */         } 
/* 814 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(Object obj) {
/* 819 */         if (obj instanceof Map.Entry) {
/* 820 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 821 */           return (entry.getKey() != null && entry
/* 822 */             .getValue() instanceof Map && StandardTable.this.backingMap
/* 823 */             .entrySet().remove(entry));
/*     */         } 
/* 825 */         return false;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<C, Map<R, V>> columnMap() {
/* 834 */     ColumnMap result = this.columnMap;
/* 835 */     return (result == null) ? (this.columnMap = new ColumnMap()) : result;
/*     */   }
/*     */ 
/*     */   
/*     */   private class ColumnMap
/*     */     extends Maps.ViewCachingAbstractMap<C, Map<R, V>>
/*     */   {
/*     */     private ColumnMap() {}
/*     */     
/*     */     public Map<R, V> get(Object key) {
/* 845 */       return StandardTable.this.containsColumn(key) ? StandardTable.this.column(key) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 850 */       return StandardTable.this.containsColumn(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, V> remove(Object key) {
/* 855 */       return StandardTable.this.containsColumn(key) ? StandardTable.this.removeColumn(key) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Map.Entry<C, Map<R, V>>> createEntrySet() {
/* 860 */       return new ColumnMapEntrySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<C> keySet() {
/* 865 */       return StandardTable.this.columnKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     Collection<Map<R, V>> createValues() {
/* 870 */       return new ColumnMapValues();
/*     */     }
/*     */     
/*     */     class ColumnMapEntrySet
/*     */       extends StandardTable<R, C, V>.TableSet<Map.Entry<C, Map<R, V>>>
/*     */     {
/*     */       public Iterator<Map.Entry<C, Map<R, V>>> iterator() {
/* 877 */         return Maps.asMapEntryIterator(StandardTable.this
/* 878 */             .columnKeySet(), new Function<C, Map<R, V>>()
/*     */             {
/*     */               public Map<R, V> apply(C columnKey)
/*     */               {
/* 882 */                 return StandardTable.this.column(columnKey);
/*     */               }
/*     */             });
/*     */       }
/*     */ 
/*     */       
/*     */       public int size() {
/* 889 */         return StandardTable.this.columnKeySet().size();
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean contains(Object obj) {
/* 894 */         if (obj instanceof Map.Entry) {
/* 895 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 896 */           if (StandardTable.this.containsColumn(entry.getKey())) {
/*     */ 
/*     */ 
/*     */             
/* 900 */             C columnKey = (C)entry.getKey();
/* 901 */             return StandardTable.ColumnMap.this.get(columnKey).equals(entry.getValue());
/*     */           } 
/*     */         } 
/* 904 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(Object obj) {
/* 909 */         if (contains(obj)) {
/* 910 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 911 */           StandardTable.this.removeColumn(entry.getKey());
/* 912 */           return true;
/*     */         } 
/* 914 */         return false;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public boolean removeAll(Collection<?> c) {
/* 925 */         Preconditions.checkNotNull(c);
/* 926 */         return Sets.removeAllImpl(this, c.iterator());
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean retainAll(Collection<?> c) {
/* 931 */         Preconditions.checkNotNull(c);
/* 932 */         boolean changed = false;
/* 933 */         for (C columnKey : Lists.newArrayList(StandardTable.this.columnKeySet().iterator())) {
/* 934 */           if (!c.contains(Maps.immutableEntry(columnKey, StandardTable.this.column(columnKey)))) {
/* 935 */             StandardTable.this.removeColumn(columnKey);
/* 936 */             changed = true;
/*     */           } 
/*     */         } 
/* 939 */         return changed;
/*     */       }
/*     */     }
/*     */     
/*     */     private class ColumnMapValues
/*     */       extends Maps.Values<C, Map<R, V>> {
/*     */       ColumnMapValues() {
/* 946 */         super(StandardTable.ColumnMap.this);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(Object obj) {
/* 951 */         for (Map.Entry<C, Map<R, V>> entry : StandardTable.ColumnMap.this.entrySet()) {
/* 952 */           if (((Map)entry.getValue()).equals(obj)) {
/* 953 */             StandardTable.this.removeColumn(entry.getKey());
/* 954 */             return true;
/*     */           } 
/*     */         } 
/* 957 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean removeAll(Collection<?> c) {
/* 962 */         Preconditions.checkNotNull(c);
/* 963 */         boolean changed = false;
/* 964 */         for (C columnKey : Lists.newArrayList(StandardTable.this.columnKeySet().iterator())) {
/* 965 */           if (c.contains(StandardTable.this.column(columnKey))) {
/* 966 */             StandardTable.this.removeColumn(columnKey);
/* 967 */             changed = true;
/*     */           } 
/*     */         } 
/* 970 */         return changed;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean retainAll(Collection<?> c) {
/* 975 */         Preconditions.checkNotNull(c);
/* 976 */         boolean changed = false;
/* 977 */         for (C columnKey : Lists.newArrayList(StandardTable.this.columnKeySet().iterator())) {
/* 978 */           if (!c.contains(StandardTable.this.column(columnKey))) {
/* 979 */             StandardTable.this.removeColumn(columnKey);
/* 980 */             changed = true;
/*     */           } 
/*     */         } 
/* 983 */         return changed;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\StandardTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */