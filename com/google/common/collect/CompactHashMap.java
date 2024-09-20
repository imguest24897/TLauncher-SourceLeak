/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.primitives.Ints;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.io.IOException;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.Spliterator;
/*      */ import java.util.Spliterators;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Consumer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtIncompatible
/*      */ class CompactHashMap<K, V>
/*      */   extends AbstractMap<K, V>
/*      */   implements Serializable
/*      */ {
/*      */   public static <K, V> CompactHashMap<K, V> create() {
/*   90 */     return new CompactHashMap<>();
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
/*      */   public static <K, V> CompactHashMap<K, V> createWithExpectedSize(int expectedSize) {
/*  103 */     return new CompactHashMap<>(expectedSize);
/*      */   }
/*      */   
/*  106 */   private static final Object NOT_FOUND = new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   static final double HASH_FLOODING_FPP = 0.001D;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int MAX_HASH_BUCKET_LENGTH = 9;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private transient Object table;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   transient int[] entries;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   transient Object[] keys;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   transient Object[] values;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private transient int metadata;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private transient int size;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private transient Set<K> keySetView;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private transient Set<Map.Entry<K, V>> entrySetView;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private transient Collection<V> valuesView;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   CompactHashMap() {
/*  185 */     init(3);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   CompactHashMap(int expectedSize) {
/*  194 */     init(expectedSize);
/*      */   }
/*      */ 
/*      */   
/*      */   void init(int expectedSize) {
/*  199 */     Preconditions.checkArgument((expectedSize >= 0), "Expected size must be >= 0");
/*      */ 
/*      */     
/*  202 */     this.metadata = Ints.constrainToRange(expectedSize, 1, 1073741823);
/*      */   }
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   boolean needsAllocArrays() {
/*  208 */     return (this.table == null);
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   int allocArrays() {
/*  214 */     Preconditions.checkState(needsAllocArrays(), "Arrays already allocated");
/*      */     
/*  216 */     int expectedSize = this.metadata;
/*  217 */     int buckets = CompactHashing.tableSize(expectedSize);
/*  218 */     this.table = CompactHashing.createTable(buckets);
/*  219 */     setHashTableMask(buckets - 1);
/*      */     
/*  221 */     this.entries = new int[expectedSize];
/*  222 */     this.keys = new Object[expectedSize];
/*  223 */     this.values = new Object[expectedSize];
/*      */     
/*  225 */     return expectedSize;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   Map<K, V> delegateOrNull() {
/*  232 */     if (this.table instanceof Map) {
/*  233 */       return (Map<K, V>)this.table;
/*      */     }
/*  235 */     return null;
/*      */   }
/*      */   
/*      */   Map<K, V> createHashFloodingResistantDelegate(int tableSize) {
/*  239 */     return new LinkedHashMap<>(tableSize, 1.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   @CanIgnoreReturnValue
/*      */   Map<K, V> convertToHashFloodingResistantImplementation() {
/*  246 */     Map<K, V> newDelegate = createHashFloodingResistantDelegate(hashTableMask() + 1);
/*  247 */     for (int i = firstEntryIndex(); i >= 0; i = getSuccessor(i)) {
/*  248 */       newDelegate.put((K)this.keys[i], (V)this.values[i]);
/*      */     }
/*  250 */     this.table = newDelegate;
/*  251 */     this.entries = null;
/*  252 */     this.keys = null;
/*  253 */     this.values = null;
/*  254 */     incrementModCount();
/*  255 */     return newDelegate;
/*      */   }
/*      */ 
/*      */   
/*      */   private void setHashTableMask(int mask) {
/*  260 */     int hashTableBits = 32 - Integer.numberOfLeadingZeros(mask);
/*  261 */     this
/*  262 */       .metadata = CompactHashing.maskCombine(this.metadata, hashTableBits, 31);
/*      */   }
/*      */ 
/*      */   
/*      */   private int hashTableMask() {
/*  267 */     return (1 << (this.metadata & 0x1F)) - 1;
/*      */   }
/*      */   
/*      */   void incrementModCount() {
/*  271 */     this.metadata += 32;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void accessEntry(int index) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public V put(K key, V value) {
/*  285 */     if (needsAllocArrays()) {
/*  286 */       allocArrays();
/*      */     }
/*  288 */     Map<K, V> delegate = delegateOrNull();
/*  289 */     if (delegate != null) {
/*  290 */       return delegate.put(key, value);
/*      */     }
/*  292 */     int[] entries = this.entries;
/*  293 */     Object[] keys = this.keys;
/*  294 */     Object[] values = this.values;
/*      */     
/*  296 */     int newEntryIndex = this.size;
/*  297 */     int newSize = newEntryIndex + 1;
/*  298 */     int hash = Hashing.smearedHash(key);
/*  299 */     int mask = hashTableMask();
/*  300 */     int tableIndex = hash & mask;
/*  301 */     int next = CompactHashing.tableGet(this.table, tableIndex);
/*  302 */     if (next == 0) {
/*  303 */       if (newSize > mask) {
/*      */         
/*  305 */         mask = resizeTable(mask, CompactHashing.newCapacity(mask), hash, newEntryIndex);
/*      */       } else {
/*  307 */         CompactHashing.tableSet(this.table, tableIndex, newEntryIndex + 1);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  312 */       int entryIndex, entry, hashPrefix = CompactHashing.getHashPrefix(hash, mask);
/*  313 */       int bucketLength = 0;
/*      */       do {
/*  315 */         entryIndex = next - 1;
/*  316 */         entry = entries[entryIndex];
/*  317 */         if (CompactHashing.getHashPrefix(entry, mask) == hashPrefix && 
/*  318 */           Objects.equal(key, keys[entryIndex])) {
/*      */ 
/*      */           
/*  321 */           V oldValue = (V)values[entryIndex];
/*      */           
/*  323 */           values[entryIndex] = value;
/*  324 */           accessEntry(entryIndex);
/*  325 */           return oldValue;
/*      */         } 
/*  327 */         next = CompactHashing.getNext(entry, mask);
/*  328 */         bucketLength++;
/*  329 */       } while (next != 0);
/*      */       
/*  331 */       if (bucketLength >= 9) {
/*  332 */         return convertToHashFloodingResistantImplementation().put(key, value);
/*      */       }
/*      */       
/*  335 */       if (newSize > mask) {
/*      */         
/*  337 */         mask = resizeTable(mask, CompactHashing.newCapacity(mask), hash, newEntryIndex);
/*      */       } else {
/*  339 */         entries[entryIndex] = CompactHashing.maskCombine(entry, newEntryIndex + 1, mask);
/*      */       } 
/*      */     } 
/*  342 */     resizeMeMaybe(newSize);
/*  343 */     insertEntry(newEntryIndex, key, value, hash, mask);
/*  344 */     this.size = newSize;
/*  345 */     incrementModCount();
/*  346 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void insertEntry(int entryIndex, K key, V value, int hash, int mask) {
/*  353 */     this.entries[entryIndex] = CompactHashing.maskCombine(hash, 0, mask);
/*  354 */     this.keys[entryIndex] = key;
/*  355 */     this.values[entryIndex] = value;
/*      */   }
/*      */ 
/*      */   
/*      */   private void resizeMeMaybe(int newSize) {
/*  360 */     int entriesSize = this.entries.length;
/*  361 */     if (newSize > entriesSize) {
/*      */ 
/*      */       
/*  364 */       int newCapacity = Math.min(1073741823, entriesSize + Math.max(1, entriesSize >>> 1) | 0x1);
/*  365 */       if (newCapacity != entriesSize) {
/*  366 */         resizeEntries(newCapacity);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void resizeEntries(int newCapacity) {
/*  376 */     this.entries = Arrays.copyOf(this.entries, newCapacity);
/*  377 */     this.keys = Arrays.copyOf(this.keys, newCapacity);
/*  378 */     this.values = Arrays.copyOf(this.values, newCapacity);
/*      */   }
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   private int resizeTable(int mask, int newCapacity, int targetHash, int targetEntryIndex) {
/*  383 */     Object newTable = CompactHashing.createTable(newCapacity);
/*  384 */     int newMask = newCapacity - 1;
/*      */     
/*  386 */     if (targetEntryIndex != 0)
/*      */     {
/*  388 */       CompactHashing.tableSet(newTable, targetHash & newMask, targetEntryIndex + 1);
/*      */     }
/*      */     
/*  391 */     Object table = this.table;
/*  392 */     int[] entries = this.entries;
/*      */ 
/*      */     
/*  395 */     for (int tableIndex = 0; tableIndex <= mask; tableIndex++) {
/*  396 */       int next = CompactHashing.tableGet(table, tableIndex);
/*  397 */       while (next != 0) {
/*  398 */         int entryIndex = next - 1;
/*  399 */         int entry = entries[entryIndex];
/*      */ 
/*      */         
/*  402 */         int hash = CompactHashing.getHashPrefix(entry, mask) | tableIndex;
/*      */         
/*  404 */         int newTableIndex = hash & newMask;
/*  405 */         int newNext = CompactHashing.tableGet(newTable, newTableIndex);
/*  406 */         CompactHashing.tableSet(newTable, newTableIndex, next);
/*  407 */         entries[entryIndex] = CompactHashing.maskCombine(hash, newNext, newMask);
/*      */         
/*  409 */         next = CompactHashing.getNext(entry, mask);
/*      */       } 
/*      */     } 
/*      */     
/*  413 */     this.table = newTable;
/*  414 */     setHashTableMask(newMask);
/*  415 */     return newMask;
/*      */   }
/*      */   
/*      */   private int indexOf(Object key) {
/*  419 */     if (needsAllocArrays()) {
/*  420 */       return -1;
/*      */     }
/*  422 */     int hash = Hashing.smearedHash(key);
/*  423 */     int mask = hashTableMask();
/*  424 */     int next = CompactHashing.tableGet(this.table, hash & mask);
/*  425 */     if (next == 0) {
/*  426 */       return -1;
/*      */     }
/*  428 */     int hashPrefix = CompactHashing.getHashPrefix(hash, mask);
/*      */     while (true) {
/*  430 */       int entryIndex = next - 1;
/*  431 */       int entry = this.entries[entryIndex];
/*  432 */       if (CompactHashing.getHashPrefix(entry, mask) == hashPrefix && 
/*  433 */         Objects.equal(key, this.keys[entryIndex])) {
/*  434 */         return entryIndex;
/*      */       }
/*  436 */       next = CompactHashing.getNext(entry, mask);
/*  437 */       if (next == 0)
/*  438 */         return -1; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsKey(Object key) {
/*  443 */     Map<K, V> delegate = delegateOrNull();
/*  444 */     return (delegate != null) ? delegate.containsKey(key) : ((indexOf(key) != -1));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public V get(Object key) {
/*  450 */     Map<K, V> delegate = delegateOrNull();
/*  451 */     if (delegate != null) {
/*  452 */       return delegate.get(key);
/*      */     }
/*  454 */     int index = indexOf(key);
/*  455 */     if (index == -1) {
/*  456 */       return null;
/*      */     }
/*  458 */     accessEntry(index);
/*  459 */     return (V)this.values[index];
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public V remove(Object key) {
/*  466 */     Map<K, V> delegate = delegateOrNull();
/*  467 */     if (delegate != null) {
/*  468 */       return delegate.remove(key);
/*      */     }
/*  470 */     Object oldValue = removeHelper(key);
/*  471 */     return (oldValue == NOT_FOUND) ? null : (V)oldValue;
/*      */   }
/*      */   
/*      */   private Object removeHelper(Object key) {
/*  475 */     if (needsAllocArrays()) {
/*  476 */       return NOT_FOUND;
/*      */     }
/*  478 */     int mask = hashTableMask();
/*      */     
/*  480 */     int index = CompactHashing.remove(key, null, mask, this.table, this.entries, this.keys, null);
/*      */     
/*  482 */     if (index == -1) {
/*  483 */       return NOT_FOUND;
/*      */     }
/*      */     
/*  486 */     Object oldValue = this.values[index];
/*      */     
/*  488 */     moveLastEntry(index, mask);
/*  489 */     this.size--;
/*  490 */     incrementModCount();
/*      */     
/*  492 */     return oldValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void moveLastEntry(int dstIndex, int mask) {
/*  499 */     int srcIndex = size() - 1;
/*  500 */     if (dstIndex < srcIndex) {
/*      */       
/*  502 */       Object key = this.keys[srcIndex];
/*  503 */       this.keys[dstIndex] = key;
/*  504 */       this.values[dstIndex] = this.values[srcIndex];
/*  505 */       this.keys[srcIndex] = null;
/*  506 */       this.values[srcIndex] = null;
/*      */ 
/*      */       
/*  509 */       this.entries[dstIndex] = this.entries[srcIndex];
/*  510 */       this.entries[srcIndex] = 0;
/*      */ 
/*      */       
/*  513 */       int tableIndex = Hashing.smearedHash(key) & mask;
/*  514 */       int next = CompactHashing.tableGet(this.table, tableIndex);
/*  515 */       int srcNext = srcIndex + 1;
/*  516 */       if (next == srcNext) {
/*      */         
/*  518 */         CompactHashing.tableSet(this.table, tableIndex, dstIndex + 1);
/*      */       } else {
/*      */         int entryIndex, entry;
/*      */ 
/*      */         
/*      */         do {
/*  524 */           entryIndex = next - 1;
/*  525 */           entry = this.entries[entryIndex];
/*  526 */           next = CompactHashing.getNext(entry, mask);
/*  527 */         } while (next != srcNext);
/*      */         
/*  529 */         this.entries[entryIndex] = CompactHashing.maskCombine(entry, dstIndex + 1, mask);
/*      */       } 
/*      */     } else {
/*  532 */       this.keys[dstIndex] = null;
/*  533 */       this.values[dstIndex] = null;
/*  534 */       this.entries[dstIndex] = 0;
/*      */     } 
/*      */   }
/*      */   
/*      */   int firstEntryIndex() {
/*  539 */     return isEmpty() ? -1 : 0;
/*      */   }
/*      */   
/*      */   int getSuccessor(int entryIndex) {
/*  543 */     return (entryIndex + 1 < this.size) ? (entryIndex + 1) : -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int adjustAfterRemove(int indexBeforeRemove, int indexRemoved) {
/*  552 */     return indexBeforeRemove - 1;
/*      */   }
/*      */   
/*      */   private abstract class Itr<T> implements Iterator<T> {
/*  556 */     int expectedMetadata = CompactHashMap.this.metadata;
/*  557 */     int currentIndex = CompactHashMap.this.firstEntryIndex();
/*  558 */     int indexToRemove = -1;
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/*  562 */       return (this.currentIndex >= 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public T next() {
/*  569 */       checkForConcurrentModification();
/*  570 */       if (!hasNext()) {
/*  571 */         throw new NoSuchElementException();
/*      */       }
/*  573 */       this.indexToRemove = this.currentIndex;
/*  574 */       T result = getOutput(this.currentIndex);
/*  575 */       this.currentIndex = CompactHashMap.this.getSuccessor(this.currentIndex);
/*  576 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/*  581 */       checkForConcurrentModification();
/*  582 */       CollectPreconditions.checkRemove((this.indexToRemove >= 0));
/*  583 */       incrementExpectedModCount();
/*  584 */       CompactHashMap.this.remove(CompactHashMap.this.keys[this.indexToRemove]);
/*  585 */       this.currentIndex = CompactHashMap.this.adjustAfterRemove(this.currentIndex, this.indexToRemove);
/*  586 */       this.indexToRemove = -1;
/*      */     }
/*      */     
/*      */     void incrementExpectedModCount() {
/*  590 */       this.expectedMetadata += 32;
/*      */     }
/*      */     
/*      */     private void checkForConcurrentModification() {
/*  594 */       if (CompactHashMap.this.metadata != this.expectedMetadata)
/*  595 */         throw new ConcurrentModificationException(); 
/*      */     }
/*      */     
/*      */     private Itr() {}
/*      */     
/*      */     abstract T getOutput(int param1Int); }
/*      */   
/*      */   public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/*  603 */     Preconditions.checkNotNull(function);
/*  604 */     Map<K, V> delegate = delegateOrNull();
/*  605 */     if (delegate != null) {
/*  606 */       delegate.replaceAll(function);
/*      */     } else {
/*  608 */       for (int i = 0; i < this.size; i++) {
/*  609 */         this.values[i] = function.apply((K)this.keys[i], (V)this.values[i]);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<K> keySet() {
/*  618 */     return (this.keySetView == null) ? (this.keySetView = createKeySet()) : this.keySetView;
/*      */   }
/*      */   
/*      */   Set<K> createKeySet() {
/*  622 */     return new KeySetView();
/*      */   }
/*      */   
/*      */   class KeySetView
/*      */     extends Maps.KeySet<K, V> {
/*      */     KeySetView() {
/*  628 */       super(CompactHashMap.this);
/*      */     }
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/*  633 */       if (CompactHashMap.this.needsAllocArrays()) {
/*  634 */         return new Object[0];
/*      */       }
/*  636 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/*  637 */       return (delegate != null) ? 
/*  638 */         delegate.keySet().toArray() : 
/*  639 */         ObjectArrays.copyAsObjectArray(CompactHashMap.this.keys, 0, CompactHashMap.this.size);
/*      */     }
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] a) {
/*  644 */       if (CompactHashMap.this.needsAllocArrays()) {
/*  645 */         if (a.length > 0) {
/*  646 */           a[0] = null;
/*      */         }
/*  648 */         return a;
/*      */       } 
/*  650 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/*  651 */       return (delegate != null) ? 
/*  652 */         (T[])delegate.keySet().toArray((Object[])a) : 
/*  653 */         ObjectArrays.<T>toArrayImpl(CompactHashMap.this.keys, 0, CompactHashMap.this.size, a);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/*  658 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/*  659 */       return (delegate != null) ? 
/*  660 */         delegate.keySet().remove(o) : (
/*  661 */         (CompactHashMap.this.removeHelper(o) != CompactHashMap.NOT_FOUND));
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> iterator() {
/*  666 */       return CompactHashMap.this.keySetIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public Spliterator<K> spliterator() {
/*  671 */       if (CompactHashMap.this.needsAllocArrays()) {
/*  672 */         return Spliterators.spliterator(new Object[0], 17);
/*      */       }
/*  674 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/*  675 */       return (delegate != null) ? 
/*  676 */         delegate.keySet().spliterator() : 
/*  677 */         Spliterators.<K>spliterator(CompactHashMap.this.keys, 0, CompactHashMap.this.size, 17);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> action) {
/*  683 */       Preconditions.checkNotNull(action);
/*  684 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/*  685 */       if (delegate != null) {
/*  686 */         delegate.keySet().forEach(action);
/*      */       } else {
/*  688 */         for (int i = CompactHashMap.this.firstEntryIndex(); i >= 0; i = CompactHashMap.this.getSuccessor(i)) {
/*  689 */           action.accept((K)CompactHashMap.this.keys[i]);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   Iterator<K> keySetIterator() {
/*  696 */     Map<K, V> delegate = delegateOrNull();
/*  697 */     if (delegate != null) {
/*  698 */       return delegate.keySet().iterator();
/*      */     }
/*  700 */     return new Itr<K>()
/*      */       {
/*      */         K getOutput(int entry)
/*      */         {
/*  704 */           return (K)CompactHashMap.this.keys[entry];
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void forEach(BiConsumer<? super K, ? super V> action) {
/*  712 */     Preconditions.checkNotNull(action);
/*  713 */     Map<K, V> delegate = delegateOrNull();
/*  714 */     if (delegate != null) {
/*  715 */       delegate.forEach(action);
/*      */     } else {
/*  717 */       for (int i = firstEntryIndex(); i >= 0; i = getSuccessor(i)) {
/*  718 */         action.accept((K)this.keys[i], (V)this.values[i]);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<Map.Entry<K, V>> entrySet() {
/*  727 */     return (this.entrySetView == null) ? (this.entrySetView = createEntrySet()) : this.entrySetView;
/*      */   }
/*      */   
/*      */   Set<Map.Entry<K, V>> createEntrySet() {
/*  731 */     return new EntrySetView();
/*      */   }
/*      */   
/*      */   class EntrySetView
/*      */     extends Maps.EntrySet<K, V>
/*      */   {
/*      */     Map<K, V> map() {
/*  738 */       return CompactHashMap.this;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/*  743 */       return CompactHashMap.this.entrySetIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public Spliterator<Map.Entry<K, V>> spliterator() {
/*  748 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/*  749 */       return (delegate != null) ? 
/*  750 */         delegate.entrySet().spliterator() : 
/*  751 */         CollectSpliterators.<Map.Entry<K, V>>indexed(CompactHashMap.this
/*  752 */           .size, 17, x$0 -> new CompactHashMap.MapEntry(x$0));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  757 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/*  758 */       if (delegate != null)
/*  759 */         return delegate.entrySet().contains(o); 
/*  760 */       if (o instanceof Map.Entry) {
/*  761 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/*  762 */         int index = CompactHashMap.this.indexOf(entry.getKey());
/*  763 */         return (index != -1 && Objects.equal(CompactHashMap.this.values[index], entry.getValue()));
/*      */       } 
/*  765 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/*  770 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/*  771 */       if (delegate != null)
/*  772 */         return delegate.entrySet().remove(o); 
/*  773 */       if (o instanceof Map.Entry) {
/*  774 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/*  775 */         if (CompactHashMap.this.needsAllocArrays()) {
/*  776 */           return false;
/*      */         }
/*  778 */         int mask = CompactHashMap.this.hashTableMask();
/*      */         
/*  780 */         int index = CompactHashing.remove(entry
/*  781 */             .getKey(), entry.getValue(), mask, CompactHashMap.this.table, CompactHashMap.this.entries, CompactHashMap.this.keys, CompactHashMap.this.values);
/*  782 */         if (index == -1) {
/*  783 */           return false;
/*      */         }
/*      */         
/*  786 */         CompactHashMap.this.moveLastEntry(index, mask);
/*  787 */         CompactHashMap.this.size--;
/*  788 */         CompactHashMap.this.incrementModCount();
/*      */         
/*  790 */         return true;
/*      */       } 
/*  792 */       return false;
/*      */     }
/*      */   }
/*      */   
/*      */   Iterator<Map.Entry<K, V>> entrySetIterator() {
/*  797 */     Map<K, V> delegate = delegateOrNull();
/*  798 */     if (delegate != null) {
/*  799 */       return delegate.entrySet().iterator();
/*      */     }
/*  801 */     return new Itr<Map.Entry<K, V>>()
/*      */       {
/*      */         Map.Entry<K, V> getOutput(int entry) {
/*  804 */           return new CompactHashMap.MapEntry(entry);
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   final class MapEntry
/*      */     extends AbstractMapEntry<K, V>
/*      */   {
/*      */     private final K key;
/*      */     private int lastKnownIndex;
/*      */     
/*      */     MapEntry(int index) {
/*  816 */       this.key = (K)CompactHashMap.this.keys[index];
/*  817 */       this.lastKnownIndex = index;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public K getKey() {
/*  823 */       return this.key;
/*      */     }
/*      */     
/*      */     private void updateLastKnownIndex() {
/*  827 */       if (this.lastKnownIndex == -1 || this.lastKnownIndex >= CompactHashMap.this
/*  828 */         .size() || 
/*  829 */         !Objects.equal(this.key, CompactHashMap.this.keys[this.lastKnownIndex])) {
/*  830 */         this.lastKnownIndex = CompactHashMap.this.indexOf(this.key);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public V getValue() {
/*  837 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/*  838 */       if (delegate != null) {
/*  839 */         return delegate.get(this.key);
/*      */       }
/*  841 */       updateLastKnownIndex();
/*  842 */       return (this.lastKnownIndex == -1) ? null : (V)CompactHashMap.this.values[this.lastKnownIndex];
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public V setValue(V value) {
/*  848 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/*  849 */       if (delegate != null) {
/*  850 */         return delegate.put(this.key, value);
/*      */       }
/*  852 */       updateLastKnownIndex();
/*  853 */       if (this.lastKnownIndex == -1) {
/*  854 */         CompactHashMap.this.put(this.key, value);
/*  855 */         return null;
/*      */       } 
/*  857 */       V old = (V)CompactHashMap.this.values[this.lastKnownIndex];
/*  858 */       CompactHashMap.this.values[this.lastKnownIndex] = value;
/*  859 */       return old;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  866 */     Map<K, V> delegate = delegateOrNull();
/*  867 */     return (delegate != null) ? delegate.size() : this.size;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/*  872 */     return (size() == 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsValue(Object value) {
/*  877 */     Map<K, V> delegate = delegateOrNull();
/*  878 */     if (delegate != null) {
/*  879 */       return delegate.containsValue(value);
/*      */     }
/*  881 */     for (int i = 0; i < this.size; i++) {
/*  882 */       if (Objects.equal(value, this.values[i])) {
/*  883 */         return true;
/*      */       }
/*      */     } 
/*  886 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> values() {
/*  893 */     return (this.valuesView == null) ? (this.valuesView = createValues()) : this.valuesView;
/*      */   }
/*      */   
/*      */   Collection<V> createValues() {
/*  897 */     return new ValuesView();
/*      */   }
/*      */   
/*      */   class ValuesView
/*      */     extends Maps.Values<K, V> {
/*      */     ValuesView() {
/*  903 */       super(CompactHashMap.this);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<V> iterator() {
/*  908 */       return CompactHashMap.this.valuesIterator();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super V> action) {
/*  914 */       Preconditions.checkNotNull(action);
/*  915 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/*  916 */       if (delegate != null) {
/*  917 */         delegate.values().forEach(action);
/*      */       } else {
/*  919 */         for (int i = CompactHashMap.this.firstEntryIndex(); i >= 0; i = CompactHashMap.this.getSuccessor(i)) {
/*  920 */           action.accept((V)CompactHashMap.this.values[i]);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Spliterator<V> spliterator() {
/*  927 */       if (CompactHashMap.this.needsAllocArrays()) {
/*  928 */         return Spliterators.spliterator(new Object[0], 16);
/*      */       }
/*  930 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/*  931 */       return (delegate != null) ? 
/*  932 */         delegate.values().spliterator() : 
/*  933 */         Spliterators.<V>spliterator(CompactHashMap.this.values, 0, CompactHashMap.this.size, 16);
/*      */     }
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/*  938 */       if (CompactHashMap.this.needsAllocArrays()) {
/*  939 */         return new Object[0];
/*      */       }
/*  941 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/*  942 */       return (delegate != null) ? 
/*  943 */         delegate.values().toArray() : 
/*  944 */         ObjectArrays.copyAsObjectArray(CompactHashMap.this.values, 0, CompactHashMap.this.size);
/*      */     }
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] a) {
/*  949 */       if (CompactHashMap.this.needsAllocArrays()) {
/*  950 */         if (a.length > 0) {
/*  951 */           a[0] = null;
/*      */         }
/*  953 */         return a;
/*      */       } 
/*  955 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/*  956 */       return (delegate != null) ? 
/*  957 */         (T[])delegate.values().toArray((Object[])a) : 
/*  958 */         ObjectArrays.<T>toArrayImpl(CompactHashMap.this.values, 0, CompactHashMap.this.size, a);
/*      */     }
/*      */   }
/*      */   
/*      */   Iterator<V> valuesIterator() {
/*  963 */     Map<K, V> delegate = delegateOrNull();
/*  964 */     if (delegate != null) {
/*  965 */       return delegate.values().iterator();
/*      */     }
/*  967 */     return new Itr<V>()
/*      */       {
/*      */         V getOutput(int entry)
/*      */         {
/*  971 */           return (V)CompactHashMap.this.values[entry];
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void trimToSize() {
/*  981 */     if (needsAllocArrays()) {
/*      */       return;
/*      */     }
/*  984 */     Map<K, V> delegate = delegateOrNull();
/*  985 */     if (delegate != null) {
/*  986 */       Map<K, V> newDelegate = createHashFloodingResistantDelegate(size());
/*  987 */       newDelegate.putAll(delegate);
/*  988 */       this.table = newDelegate;
/*      */       return;
/*      */     } 
/*  991 */     int size = this.size;
/*  992 */     if (size < this.entries.length) {
/*  993 */       resizeEntries(size);
/*      */     }
/*  995 */     int minimumTableSize = CompactHashing.tableSize(size);
/*  996 */     int mask = hashTableMask();
/*  997 */     if (minimumTableSize < mask) {
/*  998 */       resizeTable(mask, minimumTableSize, 0, 0);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void clear() {
/* 1004 */     if (needsAllocArrays()) {
/*      */       return;
/*      */     }
/* 1007 */     incrementModCount();
/* 1008 */     Map<K, V> delegate = delegateOrNull();
/* 1009 */     if (delegate != null) {
/* 1010 */       this
/* 1011 */         .metadata = Ints.constrainToRange(size(), 3, 1073741823);
/* 1012 */       delegate.clear();
/* 1013 */       this.table = null;
/* 1014 */       this.size = 0;
/*      */     } else {
/* 1016 */       Arrays.fill(this.keys, 0, this.size, (Object)null);
/* 1017 */       Arrays.fill(this.values, 0, this.size, (Object)null);
/* 1018 */       CompactHashing.tableClear(this.table);
/* 1019 */       Arrays.fill(this.entries, 0, this.size, 0);
/* 1020 */       this.size = 0;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 1025 */     stream.defaultWriteObject();
/* 1026 */     stream.writeInt(size());
/* 1027 */     Iterator<Map.Entry<K, V>> entryIterator = entrySetIterator();
/* 1028 */     while (entryIterator.hasNext()) {
/* 1029 */       Map.Entry<K, V> e = entryIterator.next();
/* 1030 */       stream.writeObject(e.getKey());
/* 1031 */       stream.writeObject(e.getValue());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 1037 */     stream.defaultReadObject();
/* 1038 */     int elementCount = stream.readInt();
/* 1039 */     if (elementCount < 0) {
/* 1040 */       throw new InvalidObjectException((new StringBuilder(25)).append("Invalid size: ").append(elementCount).toString());
/*      */     }
/* 1042 */     init(elementCount);
/* 1043 */     for (int i = 0; i < elementCount; i++) {
/* 1044 */       K key = (K)stream.readObject();
/* 1045 */       V value = (V)stream.readObject();
/* 1046 */       put(key, value);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\CompactHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */