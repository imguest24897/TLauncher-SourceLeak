/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
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
/*     */ @GwtIncompatible
/*     */ class CompactLinkedHashSet<E>
/*     */   extends CompactHashSet<E>
/*     */ {
/*     */   private static final int ENDPOINT = -2;
/*     */   private transient int[] predecessor;
/*     */   private transient int[] successor;
/*     */   private transient int firstEntry;
/*     */   private transient int lastEntry;
/*     */   
/*     */   public static <E> CompactLinkedHashSet<E> create() {
/*  55 */     return new CompactLinkedHashSet<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> CompactLinkedHashSet<E> create(Collection<? extends E> collection) {
/*  66 */     CompactLinkedHashSet<E> set = createWithExpectedSize(collection.size());
/*  67 */     set.addAll(collection);
/*  68 */     return set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*     */   public static <E> CompactLinkedHashSet<E> create(E... elements) {
/*  80 */     CompactLinkedHashSet<E> set = createWithExpectedSize(elements.length);
/*  81 */     Collections.addAll(set, elements);
/*  82 */     return set;
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
/*     */   public static <E> CompactLinkedHashSet<E> createWithExpectedSize(int expectedSize) {
/*  95 */     return new CompactLinkedHashSet<>(expectedSize);
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
/*     */   CompactLinkedHashSet() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CompactLinkedHashSet(int expectedSize) {
/* 127 */     super(expectedSize);
/*     */   }
/*     */ 
/*     */   
/*     */   void init(int expectedSize) {
/* 132 */     super.init(expectedSize);
/* 133 */     this.firstEntry = -2;
/* 134 */     this.lastEntry = -2;
/*     */   }
/*     */ 
/*     */   
/*     */   int allocArrays() {
/* 139 */     int expectedSize = super.allocArrays();
/* 140 */     this.predecessor = new int[expectedSize];
/* 141 */     this.successor = new int[expectedSize];
/* 142 */     return expectedSize;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   Set<E> convertToHashFloodingResistantImplementation() {
/* 148 */     Set<E> result = super.convertToHashFloodingResistantImplementation();
/* 149 */     this.predecessor = null;
/* 150 */     this.successor = null;
/* 151 */     return result;
/*     */   }
/*     */   
/*     */   private int getPredecessor(int entry) {
/* 155 */     return this.predecessor[entry] - 1;
/*     */   }
/*     */ 
/*     */   
/*     */   int getSuccessor(int entry) {
/* 160 */     return this.successor[entry] - 1;
/*     */   }
/*     */   
/*     */   private void setSuccessor(int entry, int succ) {
/* 164 */     this.successor[entry] = succ + 1;
/*     */   }
/*     */   
/*     */   private void setPredecessor(int entry, int pred) {
/* 168 */     this.predecessor[entry] = pred + 1;
/*     */   }
/*     */   
/*     */   private void setSucceeds(int pred, int succ) {
/* 172 */     if (pred == -2) {
/* 173 */       this.firstEntry = succ;
/*     */     } else {
/* 175 */       setSuccessor(pred, succ);
/*     */     } 
/*     */     
/* 178 */     if (succ == -2) {
/* 179 */       this.lastEntry = pred;
/*     */     } else {
/* 181 */       setPredecessor(succ, pred);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void insertEntry(int entryIndex, E object, int hash, int mask) {
/* 187 */     super.insertEntry(entryIndex, object, hash, mask);
/* 188 */     setSucceeds(this.lastEntry, entryIndex);
/* 189 */     setSucceeds(entryIndex, -2);
/*     */   }
/*     */ 
/*     */   
/*     */   void moveLastEntry(int dstIndex, int mask) {
/* 194 */     int srcIndex = size() - 1;
/* 195 */     super.moveLastEntry(dstIndex, mask);
/*     */     
/* 197 */     setSucceeds(getPredecessor(dstIndex), getSuccessor(dstIndex));
/* 198 */     if (dstIndex < srcIndex) {
/* 199 */       setSucceeds(getPredecessor(srcIndex), dstIndex);
/* 200 */       setSucceeds(dstIndex, getSuccessor(srcIndex));
/*     */     } 
/* 202 */     this.predecessor[srcIndex] = 0;
/* 203 */     this.successor[srcIndex] = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   void resizeEntries(int newCapacity) {
/* 208 */     super.resizeEntries(newCapacity);
/* 209 */     this.predecessor = Arrays.copyOf(this.predecessor, newCapacity);
/* 210 */     this.successor = Arrays.copyOf(this.successor, newCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   int firstEntryIndex() {
/* 215 */     return this.firstEntry;
/*     */   }
/*     */ 
/*     */   
/*     */   int adjustAfterRemove(int indexBeforeRemove, int indexRemoved) {
/* 220 */     return (indexBeforeRemove >= size()) ? indexRemoved : indexBeforeRemove;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 225 */     return ObjectArrays.toArrayImpl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] a) {
/* 230 */     return ObjectArrays.toArrayImpl(this, a);
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<E> spliterator() {
/* 235 */     return Spliterators.spliterator(this, 17);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 240 */     if (needsAllocArrays()) {
/*     */       return;
/*     */     }
/* 243 */     this.firstEntry = -2;
/* 244 */     this.lastEntry = -2;
/* 245 */     if (this.predecessor != null) {
/* 246 */       Arrays.fill(this.predecessor, 0, size(), 0);
/* 247 */       Arrays.fill(this.successor, 0, size(), 0);
/*     */     } 
/* 249 */     super.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\CompactLinkedHashSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */