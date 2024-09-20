/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
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
/*     */ @GwtIncompatible
/*     */ class CompactLinkedHashMap<K, V>
/*     */   extends CompactHashMap<K, V>
/*     */ {
/*     */   private static final int ENDPOINT = -2;
/*     */   @VisibleForTesting
/*     */   transient long[] links;
/*     */   private transient int firstEntry;
/*     */   private transient int lastEntry;
/*     */   private final boolean accessOrder;
/*     */   
/*     */   public static <K, V> CompactLinkedHashMap<K, V> create() {
/*  58 */     return new CompactLinkedHashMap<>();
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
/*     */   public static <K, V> CompactLinkedHashMap<K, V> createWithExpectedSize(int expectedSize) {
/*  71 */     return new CompactLinkedHashMap<>(expectedSize);
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
/*     */   CompactLinkedHashMap() {
/*  96 */     this(3);
/*     */   }
/*     */   
/*     */   CompactLinkedHashMap(int expectedSize) {
/* 100 */     this(expectedSize, false);
/*     */   }
/*     */   
/*     */   CompactLinkedHashMap(int expectedSize, boolean accessOrder) {
/* 104 */     super(expectedSize);
/* 105 */     this.accessOrder = accessOrder;
/*     */   }
/*     */ 
/*     */   
/*     */   void init(int expectedSize) {
/* 110 */     super.init(expectedSize);
/* 111 */     this.firstEntry = -2;
/* 112 */     this.lastEntry = -2;
/*     */   }
/*     */ 
/*     */   
/*     */   int allocArrays() {
/* 117 */     int expectedSize = super.allocArrays();
/* 118 */     this.links = new long[expectedSize];
/* 119 */     return expectedSize;
/*     */   }
/*     */ 
/*     */   
/*     */   Map<K, V> createHashFloodingResistantDelegate(int tableSize) {
/* 124 */     return new LinkedHashMap<>(tableSize, 1.0F, this.accessOrder);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   Map<K, V> convertToHashFloodingResistantImplementation() {
/* 130 */     Map<K, V> result = super.convertToHashFloodingResistantImplementation();
/* 131 */     this.links = null;
/* 132 */     return result;
/*     */   }
/*     */   
/*     */   private int getPredecessor(int entry) {
/* 136 */     return (int)(this.links[entry] >>> 32L) - 1;
/*     */   }
/*     */ 
/*     */   
/*     */   int getSuccessor(int entry) {
/* 141 */     return (int)this.links[entry] - 1;
/*     */   }
/*     */   
/*     */   private void setSuccessor(int entry, int succ) {
/* 145 */     long succMask = 4294967295L;
/* 146 */     this.links[entry] = this.links[entry] & (succMask ^ 0xFFFFFFFFFFFFFFFFL) | (succ + 1) & succMask;
/*     */   }
/*     */   
/*     */   private void setPredecessor(int entry, int pred) {
/* 150 */     long predMask = -4294967296L;
/* 151 */     this.links[entry] = this.links[entry] & (predMask ^ 0xFFFFFFFFFFFFFFFFL) | (pred + 1) << 32L;
/*     */   }
/*     */   
/*     */   private void setSucceeds(int pred, int succ) {
/* 155 */     if (pred == -2) {
/* 156 */       this.firstEntry = succ;
/*     */     } else {
/* 158 */       setSuccessor(pred, succ);
/*     */     } 
/*     */     
/* 161 */     if (succ == -2) {
/* 162 */       this.lastEntry = pred;
/*     */     } else {
/* 164 */       setPredecessor(succ, pred);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void insertEntry(int entryIndex, K key, V value, int hash, int mask) {
/* 170 */     super.insertEntry(entryIndex, key, value, hash, mask);
/* 171 */     setSucceeds(this.lastEntry, entryIndex);
/* 172 */     setSucceeds(entryIndex, -2);
/*     */   }
/*     */ 
/*     */   
/*     */   void accessEntry(int index) {
/* 177 */     if (this.accessOrder) {
/*     */       
/* 179 */       setSucceeds(getPredecessor(index), getSuccessor(index));
/*     */       
/* 181 */       setSucceeds(this.lastEntry, index);
/* 182 */       setSucceeds(index, -2);
/* 183 */       incrementModCount();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void moveLastEntry(int dstIndex, int mask) {
/* 189 */     int srcIndex = size() - 1;
/* 190 */     super.moveLastEntry(dstIndex, mask);
/*     */     
/* 192 */     setSucceeds(getPredecessor(dstIndex), getSuccessor(dstIndex));
/* 193 */     if (dstIndex < srcIndex) {
/* 194 */       setSucceeds(getPredecessor(srcIndex), dstIndex);
/* 195 */       setSucceeds(dstIndex, getSuccessor(srcIndex));
/*     */     } 
/* 197 */     this.links[srcIndex] = 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   void resizeEntries(int newCapacity) {
/* 202 */     super.resizeEntries(newCapacity);
/* 203 */     this.links = Arrays.copyOf(this.links, newCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   int firstEntryIndex() {
/* 208 */     return this.firstEntry;
/*     */   }
/*     */ 
/*     */   
/*     */   int adjustAfterRemove(int indexBeforeRemove, int indexRemoved) {
/* 213 */     return (indexBeforeRemove >= size()) ? indexRemoved : indexBeforeRemove;
/*     */   }
/*     */   
/*     */   Set<Map.Entry<K, V>> createEntrySet() {
/*     */     class EntrySetImpl extends CompactHashMap<K, V>.EntrySetView {
/*     */       EntrySetImpl(CompactLinkedHashMap this$0) {
/* 219 */         super(this$0);
/*     */       }
/*     */       public Spliterator<Map.Entry<K, V>> spliterator() {
/* 222 */         return Spliterators.spliterator(this, 17);
/*     */       }
/*     */     };
/* 225 */     return new EntrySetImpl(this);
/*     */   }
/*     */   
/*     */   Set<K> createKeySet() {
/*     */     class KeySetImpl extends CompactHashMap<K, V>.KeySetView {
/*     */       KeySetImpl(CompactLinkedHashMap this$0) {
/* 231 */         super(this$0);
/*     */       }
/*     */       public Object[] toArray() {
/* 234 */         return ObjectArrays.toArrayImpl(this);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T[] toArray(T[] a) {
/* 239 */         return ObjectArrays.toArrayImpl(this, a);
/*     */       }
/*     */ 
/*     */       
/*     */       public Spliterator<K> spliterator() {
/* 244 */         return Spliterators.spliterator(this, 17);
/*     */       }
/*     */     };
/* 247 */     return new KeySetImpl(this);
/*     */   }
/*     */   
/*     */   Collection<V> createValues() {
/*     */     class ValuesImpl extends CompactHashMap<K, V>.ValuesView {
/*     */       ValuesImpl(CompactLinkedHashMap this$0) {
/* 253 */         super(this$0);
/*     */       }
/*     */       public Object[] toArray() {
/* 256 */         return ObjectArrays.toArrayImpl(this);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T[] toArray(T[] a) {
/* 261 */         return ObjectArrays.toArrayImpl(this, a);
/*     */       }
/*     */ 
/*     */       
/*     */       public Spliterator<V> spliterator() {
/* 266 */         return Spliterators.spliterator(this, 16);
/*     */       }
/*     */     };
/* 269 */     return new ValuesImpl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 274 */     if (needsAllocArrays()) {
/*     */       return;
/*     */     }
/* 277 */     this.firstEntry = -2;
/* 278 */     this.lastEntry = -2;
/* 279 */     if (this.links != null) {
/* 280 */       Arrays.fill(this.links, 0, size(), 0L);
/*     */     }
/* 282 */     super.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\CompactLinkedHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */