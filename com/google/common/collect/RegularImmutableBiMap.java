/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ class RegularImmutableBiMap<K, V>
/*     */   extends ImmutableBiMap<K, V>
/*     */ {
/*  44 */   static final RegularImmutableBiMap<Object, Object> EMPTY = new RegularImmutableBiMap(null, null, (Map.Entry[])ImmutableMap.EMPTY_ENTRY_ARRAY, 0, 0);
/*     */   static final double MAX_LOAD_FACTOR = 1.2D;
/*     */   private final transient ImmutableMapEntry<K, V>[] keyTable;
/*     */   private final transient ImmutableMapEntry<K, V>[] valueTable;
/*     */   @VisibleForTesting
/*     */   final transient Map.Entry<K, V>[] entries;
/*     */   private final transient int mask;
/*     */   private final transient int hashCode;
/*     */   @LazyInit
/*     */   @RetainedWith
/*     */   private transient ImmutableBiMap<V, K> inverse;
/*     */   
/*     */   static <K, V> ImmutableBiMap<K, V> fromEntries(Map.Entry<K, V>... entries) {
/*  57 */     return fromEntryArray(entries.length, entries);
/*     */   }
/*     */   static <K, V> ImmutableBiMap<K, V> fromEntryArray(int n, Map.Entry<K, V>[] entryArray) {
/*     */     ImmutableMapEntry[] arrayOfImmutableMapEntry3;
/*  61 */     Preconditions.checkPositionIndex(n, entryArray.length);
/*  62 */     int tableSize = Hashing.closedTableSize(n, 1.2D);
/*  63 */     int mask = tableSize - 1;
/*  64 */     ImmutableMapEntry[] arrayOfImmutableMapEntry1 = (ImmutableMapEntry[])ImmutableMapEntry.createEntryArray(tableSize);
/*  65 */     ImmutableMapEntry[] arrayOfImmutableMapEntry2 = (ImmutableMapEntry[])ImmutableMapEntry.createEntryArray(tableSize);
/*     */     
/*  67 */     if (n == entryArray.length) {
/*  68 */       Map.Entry<K, V>[] entries = entryArray;
/*     */     } else {
/*  70 */       arrayOfImmutableMapEntry3 = ImmutableMapEntry.createEntryArray(n);
/*     */     } 
/*  72 */     int hashCode = 0;
/*     */     
/*  74 */     for (int i = 0; i < n; i++) {
/*     */       
/*  76 */       Map.Entry<K, V> entry = entryArray[i];
/*  77 */       K key = entry.getKey();
/*  78 */       V value = entry.getValue();
/*  79 */       CollectPreconditions.checkEntryNotNull(key, value);
/*  80 */       int keyHash = key.hashCode();
/*  81 */       int valueHash = value.hashCode();
/*  82 */       int keyBucket = Hashing.smear(keyHash) & mask;
/*  83 */       int valueBucket = Hashing.smear(valueHash) & mask;
/*     */       
/*  85 */       ImmutableMapEntry<K, V> nextInKeyBucket = arrayOfImmutableMapEntry1[keyBucket];
/*  86 */       int keyBucketLength = RegularImmutableMap.checkNoConflictInKeyBucket(key, entry, nextInKeyBucket);
/*  87 */       ImmutableMapEntry<K, V> nextInValueBucket = arrayOfImmutableMapEntry2[valueBucket];
/*  88 */       int valueBucketLength = checkNoConflictInValueBucket(value, entry, nextInValueBucket);
/*  89 */       if (keyBucketLength > 8 || valueBucketLength > 8)
/*     */       {
/*  91 */         return JdkBackedImmutableBiMap.create(n, entryArray);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  96 */       ImmutableMapEntry<K, V> newEntry = (nextInValueBucket == null && nextInKeyBucket == null) ? RegularImmutableMap.<K, V>makeImmutable(entry, key, value) : new ImmutableMapEntry.NonTerminalImmutableBiMapEntry<>(key, value, nextInKeyBucket, nextInValueBucket);
/*     */       
/*  98 */       arrayOfImmutableMapEntry1[keyBucket] = newEntry;
/*  99 */       arrayOfImmutableMapEntry2[valueBucket] = newEntry;
/* 100 */       arrayOfImmutableMapEntry3[i] = newEntry;
/* 101 */       hashCode += keyHash ^ valueHash;
/*     */     } 
/* 103 */     return new RegularImmutableBiMap<>((ImmutableMapEntry<K, V>[])arrayOfImmutableMapEntry1, (ImmutableMapEntry<K, V>[])arrayOfImmutableMapEntry2, (Map.Entry<K, V>[])arrayOfImmutableMapEntry3, mask, hashCode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RegularImmutableBiMap(ImmutableMapEntry<K, V>[] keyTable, ImmutableMapEntry<K, V>[] valueTable, Map.Entry<K, V>[] entries, int mask, int hashCode) {
/* 112 */     this.keyTable = keyTable;
/* 113 */     this.valueTable = valueTable;
/* 114 */     this.entries = entries;
/* 115 */     this.mask = mask;
/* 116 */     this.hashCode = hashCode;
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
/*     */   private static int checkNoConflictInValueBucket(Object value, Map.Entry<?, ?> entry, ImmutableMapEntry<?, ?> valueBucketHead) {
/* 128 */     int bucketSize = 0;
/* 129 */     for (; valueBucketHead != null; valueBucketHead = valueBucketHead.getNextInValueBucket()) {
/* 130 */       checkNoConflict(!value.equals(valueBucketHead.getValue()), "value", entry, valueBucketHead);
/* 131 */       bucketSize++;
/*     */     } 
/* 133 */     return bucketSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/* 138 */     return (this.keyTable == null) ? null : RegularImmutableMap.<V>get(key, (ImmutableMapEntry<?, V>[])this.keyTable, this.mask);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/* 143 */     return isEmpty() ? 
/* 144 */       ImmutableSet.<Map.Entry<K, V>>of() : 
/* 145 */       new ImmutableMapEntrySet.RegularEntrySet<>(this, this.entries);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<K> createKeySet() {
/* 150 */     return new ImmutableMapKeySet<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 155 */     Preconditions.checkNotNull(action);
/* 156 */     for (Map.Entry<K, V> entry : this.entries) {
/* 157 */       action.accept(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isHashCodeFast() {
/* 163 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 168 */     return this.hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 173 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 178 */     return this.entries.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableBiMap<V, K> inverse() {
/* 185 */     if (isEmpty()) {
/* 186 */       return ImmutableBiMap.of();
/*     */     }
/* 188 */     ImmutableBiMap<V, K> result = this.inverse;
/* 189 */     return (result == null) ? (this.inverse = new Inverse()) : result;
/*     */   }
/*     */   
/*     */   private final class Inverse extends ImmutableBiMap<V, K> {
/*     */     private Inverse() {}
/*     */     
/*     */     public int size() {
/* 196 */       return inverse().size();
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableBiMap<K, V> inverse() {
/* 201 */       return RegularImmutableBiMap.this;
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(BiConsumer<? super V, ? super K> action) {
/* 206 */       Preconditions.checkNotNull(action);
/* 207 */       RegularImmutableBiMap.this.forEach((k, v) -> action.accept(v, k));
/*     */     }
/*     */ 
/*     */     
/*     */     public K get(Object value) {
/* 212 */       if (value == null || RegularImmutableBiMap.this.valueTable == null) {
/* 213 */         return null;
/*     */       }
/* 215 */       int bucket = Hashing.smear(value.hashCode()) & RegularImmutableBiMap.this.mask;
/* 216 */       ImmutableMapEntry<K, V> entry = RegularImmutableBiMap.this.valueTable[bucket];
/* 217 */       for (; entry != null; 
/* 218 */         entry = entry.getNextInValueBucket()) {
/* 219 */         if (value.equals(entry.getValue())) {
/* 220 */           return entry.getKey();
/*     */         }
/*     */       } 
/* 223 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet<V> createKeySet() {
/* 228 */       return new ImmutableMapKeySet<>(this);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet<Map.Entry<V, K>> createEntrySet() {
/* 233 */       return new InverseEntrySet();
/*     */     }
/*     */     
/*     */     final class InverseEntrySet
/*     */       extends ImmutableMapEntrySet<V, K> {
/*     */       ImmutableMap<V, K> map() {
/* 239 */         return RegularImmutableBiMap.Inverse.this;
/*     */       }
/*     */ 
/*     */       
/*     */       boolean isHashCodeFast() {
/* 244 */         return true;
/*     */       }
/*     */ 
/*     */       
/*     */       public int hashCode() {
/* 249 */         return RegularImmutableBiMap.this.hashCode;
/*     */       }
/*     */ 
/*     */       
/*     */       public UnmodifiableIterator<Map.Entry<V, K>> iterator() {
/* 254 */         return asList().iterator();
/*     */       }
/*     */ 
/*     */       
/*     */       public void forEach(Consumer<? super Map.Entry<V, K>> action) {
/* 259 */         asList().forEach(action);
/*     */       }
/*     */ 
/*     */       
/*     */       ImmutableList<Map.Entry<V, K>> createAsList() {
/* 264 */         return new ImmutableAsList<Map.Entry<V, K>>()
/*     */           {
/*     */             public Map.Entry<V, K> get(int index) {
/* 267 */               Map.Entry<K, V> entry = RegularImmutableBiMap.this.entries[index];
/* 268 */               return Maps.immutableEntry(entry.getValue(), entry.getKey());
/*     */             }
/*     */ 
/*     */             
/*     */             ImmutableCollection<Map.Entry<V, K>> delegateCollection() {
/* 273 */               return RegularImmutableBiMap.Inverse.InverseEntrySet.this;
/*     */             }
/*     */           };
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 281 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     Object writeReplace() {
/* 286 */       return new RegularImmutableBiMap.InverseSerializedForm<>(RegularImmutableBiMap.this);
/*     */     } }
/*     */   
/*     */   private static class InverseSerializedForm<K, V> implements Serializable {
/*     */     private final ImmutableBiMap<K, V> forward;
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     InverseSerializedForm(ImmutableBiMap<K, V> forward) {
/* 294 */       this.forward = forward;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 298 */       return this.forward.inverse();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\RegularImmutableBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */