/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class HashBiMap<K, V>
/*     */   extends Maps.IteratorBasedAbstractMap<K, V>
/*     */   implements BiMap<K, V>, Serializable
/*     */ {
/*     */   private static final double LOAD_FACTOR = 1.0D;
/*     */   private transient BiEntry<K, V>[] hashTableKToV;
/*     */   private transient BiEntry<K, V>[] hashTableVToK;
/*     */   @Weak
/*     */   private transient BiEntry<K, V> firstInKeyInsertionOrder;
/*     */   @Weak
/*     */   private transient BiEntry<K, V> lastInKeyInsertionOrder;
/*     */   private transient int size;
/*     */   private transient int mask;
/*     */   private transient int modCount;
/*     */   @LazyInit
/*     */   @RetainedWith
/*     */   private transient BiMap<V, K> inverse;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K, V> HashBiMap<K, V> create() {
/*  64 */     return create(16);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> HashBiMap<K, V> create(int expectedSize) {
/*  74 */     return new HashBiMap<>(expectedSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> HashBiMap<K, V> create(Map<? extends K, ? extends V> map) {
/*  82 */     HashBiMap<K, V> bimap = create(map.size());
/*  83 */     bimap.putAll(map);
/*  84 */     return bimap;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class BiEntry<K, V>
/*     */     extends ImmutableEntry<K, V>
/*     */   {
/*     */     final int keyHash;
/*     */     
/*     */     final int valueHash;
/*     */     
/*     */     BiEntry<K, V> nextInKToVBucket;
/*     */     @Weak
/*     */     BiEntry<K, V> nextInVToKBucket;
/*     */     @Weak
/*     */     BiEntry<K, V> nextInKeyInsertionOrder;
/*     */     @Weak
/*     */     BiEntry<K, V> prevInKeyInsertionOrder;
/*     */     
/*     */     BiEntry(K key, int keyHash, V value, int valueHash) {
/* 104 */       super(key, value);
/* 105 */       this.keyHash = keyHash;
/* 106 */       this.valueHash = valueHash;
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
/*     */   private HashBiMap(int expectedSize) {
/* 121 */     init(expectedSize);
/*     */   }
/*     */   
/*     */   private void init(int expectedSize) {
/* 125 */     CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
/* 126 */     int tableSize = Hashing.closedTableSize(expectedSize, 1.0D);
/* 127 */     this.hashTableKToV = createTable(tableSize);
/* 128 */     this.hashTableVToK = createTable(tableSize);
/* 129 */     this.firstInKeyInsertionOrder = null;
/* 130 */     this.lastInKeyInsertionOrder = null;
/* 131 */     this.size = 0;
/* 132 */     this.mask = tableSize - 1;
/* 133 */     this.modCount = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void delete(BiEntry<K, V> entry) {
/* 141 */     int keyBucket = entry.keyHash & this.mask;
/* 142 */     BiEntry<K, V> prevBucketEntry = null;
/* 143 */     BiEntry<K, V> bucketEntry = this.hashTableKToV[keyBucket];
/*     */     
/* 145 */     for (;; bucketEntry = bucketEntry.nextInKToVBucket) {
/* 146 */       if (bucketEntry == entry) {
/* 147 */         if (prevBucketEntry == null) {
/* 148 */           this.hashTableKToV[keyBucket] = entry.nextInKToVBucket; break;
/*     */         } 
/* 150 */         prevBucketEntry.nextInKToVBucket = entry.nextInKToVBucket;
/*     */         
/*     */         break;
/*     */       } 
/* 154 */       prevBucketEntry = bucketEntry;
/*     */     } 
/*     */     
/* 157 */     int valueBucket = entry.valueHash & this.mask;
/* 158 */     prevBucketEntry = null;
/* 159 */     BiEntry<K, V> biEntry1 = this.hashTableVToK[valueBucket];
/*     */     
/* 161 */     for (;; biEntry1 = biEntry1.nextInVToKBucket) {
/* 162 */       if (biEntry1 == entry) {
/* 163 */         if (prevBucketEntry == null) {
/* 164 */           this.hashTableVToK[valueBucket] = entry.nextInVToKBucket; break;
/*     */         } 
/* 166 */         prevBucketEntry.nextInVToKBucket = entry.nextInVToKBucket;
/*     */         
/*     */         break;
/*     */       } 
/* 170 */       prevBucketEntry = biEntry1;
/*     */     } 
/*     */     
/* 173 */     if (entry.prevInKeyInsertionOrder == null) {
/* 174 */       this.firstInKeyInsertionOrder = entry.nextInKeyInsertionOrder;
/*     */     } else {
/* 176 */       entry.prevInKeyInsertionOrder.nextInKeyInsertionOrder = entry.nextInKeyInsertionOrder;
/*     */     } 
/*     */     
/* 179 */     if (entry.nextInKeyInsertionOrder == null) {
/* 180 */       this.lastInKeyInsertionOrder = entry.prevInKeyInsertionOrder;
/*     */     } else {
/* 182 */       entry.nextInKeyInsertionOrder.prevInKeyInsertionOrder = entry.prevInKeyInsertionOrder;
/*     */     } 
/*     */     
/* 185 */     this.size--;
/* 186 */     this.modCount++;
/*     */   }
/*     */   
/*     */   private void insert(BiEntry<K, V> entry, BiEntry<K, V> oldEntryForKey) {
/* 190 */     int keyBucket = entry.keyHash & this.mask;
/* 191 */     entry.nextInKToVBucket = this.hashTableKToV[keyBucket];
/* 192 */     this.hashTableKToV[keyBucket] = entry;
/*     */     
/* 194 */     int valueBucket = entry.valueHash & this.mask;
/* 195 */     entry.nextInVToKBucket = this.hashTableVToK[valueBucket];
/* 196 */     this.hashTableVToK[valueBucket] = entry;
/*     */     
/* 198 */     if (oldEntryForKey == null) {
/* 199 */       entry.prevInKeyInsertionOrder = this.lastInKeyInsertionOrder;
/* 200 */       entry.nextInKeyInsertionOrder = null;
/* 201 */       if (this.lastInKeyInsertionOrder == null) {
/* 202 */         this.firstInKeyInsertionOrder = entry;
/*     */       } else {
/* 204 */         this.lastInKeyInsertionOrder.nextInKeyInsertionOrder = entry;
/*     */       } 
/* 206 */       this.lastInKeyInsertionOrder = entry;
/*     */     } else {
/* 208 */       entry.prevInKeyInsertionOrder = oldEntryForKey.prevInKeyInsertionOrder;
/* 209 */       if (entry.prevInKeyInsertionOrder == null) {
/* 210 */         this.firstInKeyInsertionOrder = entry;
/*     */       } else {
/* 212 */         entry.prevInKeyInsertionOrder.nextInKeyInsertionOrder = entry;
/*     */       } 
/* 214 */       entry.nextInKeyInsertionOrder = oldEntryForKey.nextInKeyInsertionOrder;
/* 215 */       if (entry.nextInKeyInsertionOrder == null) {
/* 216 */         this.lastInKeyInsertionOrder = entry;
/*     */       } else {
/* 218 */         entry.nextInKeyInsertionOrder.prevInKeyInsertionOrder = entry;
/*     */       } 
/*     */     } 
/*     */     
/* 222 */     this.size++;
/* 223 */     this.modCount++;
/*     */   }
/*     */   
/*     */   private BiEntry<K, V> seekByKey(Object key, int keyHash) {
/* 227 */     BiEntry<K, V> entry = this.hashTableKToV[keyHash & this.mask];
/* 228 */     for (; entry != null; 
/* 229 */       entry = entry.nextInKToVBucket) {
/* 230 */       if (keyHash == entry.keyHash && Objects.equal(key, entry.key)) {
/* 231 */         return entry;
/*     */       }
/*     */     } 
/* 234 */     return null;
/*     */   }
/*     */   
/*     */   private BiEntry<K, V> seekByValue(Object value, int valueHash) {
/* 238 */     BiEntry<K, V> entry = this.hashTableVToK[valueHash & this.mask];
/* 239 */     for (; entry != null; 
/* 240 */       entry = entry.nextInVToKBucket) {
/* 241 */       if (valueHash == entry.valueHash && Objects.equal(value, entry.value)) {
/* 242 */         return entry;
/*     */       }
/*     */     } 
/* 245 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 250 */     return (seekByKey(key, Hashing.smearedHash(key)) != null);
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
/*     */   public boolean containsValue(Object value) {
/* 265 */     return (seekByValue(value, Hashing.smearedHash(value)) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/* 270 */     return Maps.valueOrNull(seekByKey(key, Hashing.smearedHash(key)));
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V put(K key, V value) {
/* 276 */     return put(key, value, false);
/*     */   }
/*     */   
/*     */   private V put(K key, V value, boolean force) {
/* 280 */     int keyHash = Hashing.smearedHash(key);
/* 281 */     int valueHash = Hashing.smearedHash(value);
/*     */     
/* 283 */     BiEntry<K, V> oldEntryForKey = seekByKey(key, keyHash);
/* 284 */     if (oldEntryForKey != null && valueHash == oldEntryForKey.valueHash && 
/*     */       
/* 286 */       Objects.equal(value, oldEntryForKey.value)) {
/* 287 */       return value;
/*     */     }
/*     */     
/* 290 */     BiEntry<K, V> oldEntryForValue = seekByValue(value, valueHash);
/* 291 */     if (oldEntryForValue != null) {
/* 292 */       if (force) {
/* 293 */         delete(oldEntryForValue);
/*     */       } else {
/* 295 */         String str = String.valueOf(value); throw new IllegalArgumentException((new StringBuilder(23 + String.valueOf(str).length())).append("value already present: ").append(str).toString());
/*     */       } 
/*     */     }
/*     */     
/* 299 */     BiEntry<K, V> newEntry = new BiEntry<>(key, keyHash, value, valueHash);
/* 300 */     if (oldEntryForKey != null) {
/* 301 */       delete(oldEntryForKey);
/* 302 */       insert(newEntry, oldEntryForKey);
/* 303 */       oldEntryForKey.prevInKeyInsertionOrder = null;
/* 304 */       oldEntryForKey.nextInKeyInsertionOrder = null;
/* 305 */       return oldEntryForKey.value;
/*     */     } 
/* 307 */     insert(newEntry, (BiEntry<K, V>)null);
/* 308 */     rehashIfNecessary();
/* 309 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V forcePut(K key, V value) {
/* 316 */     return put(key, value, true);
/*     */   }
/*     */   
/*     */   private K putInverse(V value, K key, boolean force) {
/* 320 */     int valueHash = Hashing.smearedHash(value);
/* 321 */     int keyHash = Hashing.smearedHash(key);
/*     */     
/* 323 */     BiEntry<K, V> oldEntryForValue = seekByValue(value, valueHash);
/* 324 */     BiEntry<K, V> oldEntryForKey = seekByKey(key, keyHash);
/* 325 */     if (oldEntryForValue != null && keyHash == oldEntryForValue.keyHash && 
/*     */       
/* 327 */       Objects.equal(key, oldEntryForValue.key))
/* 328 */       return key; 
/* 329 */     if (oldEntryForKey != null && !force) {
/* 330 */       String str = String.valueOf(key); throw new IllegalArgumentException((new StringBuilder(21 + String.valueOf(str).length())).append("key already present: ").append(str).toString());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 340 */     if (oldEntryForValue != null) {
/* 341 */       delete(oldEntryForValue);
/*     */     }
/*     */     
/* 344 */     if (oldEntryForKey != null) {
/* 345 */       delete(oldEntryForKey);
/*     */     }
/*     */     
/* 348 */     BiEntry<K, V> newEntry = new BiEntry<>(key, keyHash, value, valueHash);
/* 349 */     insert(newEntry, oldEntryForKey);
/*     */     
/* 351 */     if (oldEntryForKey != null) {
/* 352 */       oldEntryForKey.prevInKeyInsertionOrder = null;
/* 353 */       oldEntryForKey.nextInKeyInsertionOrder = null;
/*     */     } 
/* 355 */     if (oldEntryForValue != null) {
/* 356 */       oldEntryForValue.prevInKeyInsertionOrder = null;
/* 357 */       oldEntryForValue.nextInKeyInsertionOrder = null;
/*     */     } 
/* 359 */     rehashIfNecessary();
/* 360 */     return Maps.keyOrNull(oldEntryForValue);
/*     */   }
/*     */   
/*     */   private void rehashIfNecessary() {
/* 364 */     BiEntry<K, V>[] oldKToV = this.hashTableKToV;
/* 365 */     if (Hashing.needsResizing(this.size, oldKToV.length, 1.0D)) {
/* 366 */       int newTableSize = oldKToV.length * 2;
/*     */       
/* 368 */       this.hashTableKToV = createTable(newTableSize);
/* 369 */       this.hashTableVToK = createTable(newTableSize);
/* 370 */       this.mask = newTableSize - 1;
/* 371 */       this.size = 0;
/*     */       
/* 373 */       BiEntry<K, V> entry = this.firstInKeyInsertionOrder;
/* 374 */       for (; entry != null; 
/* 375 */         entry = entry.nextInKeyInsertionOrder) {
/* 376 */         insert(entry, entry);
/*     */       }
/* 378 */       this.modCount++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private BiEntry<K, V>[] createTable(int length) {
/* 384 */     return (BiEntry<K, V>[])new BiEntry[length];
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V remove(Object key) {
/* 390 */     BiEntry<K, V> entry = seekByKey(key, Hashing.smearedHash(key));
/* 391 */     if (entry == null) {
/* 392 */       return null;
/*     */     }
/* 394 */     delete(entry);
/* 395 */     entry.prevInKeyInsertionOrder = null;
/* 396 */     entry.nextInKeyInsertionOrder = null;
/* 397 */     return entry.value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 403 */     this.size = 0;
/* 404 */     Arrays.fill((Object[])this.hashTableKToV, (Object)null);
/* 405 */     Arrays.fill((Object[])this.hashTableVToK, (Object)null);
/* 406 */     this.firstInKeyInsertionOrder = null;
/* 407 */     this.lastInKeyInsertionOrder = null;
/* 408 */     this.modCount++;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 413 */     return this.size;
/*     */   }
/*     */   
/*     */   abstract class Itr<T> implements Iterator<T> {
/* 417 */     HashBiMap.BiEntry<K, V> next = HashBiMap.this.firstInKeyInsertionOrder;
/* 418 */     HashBiMap.BiEntry<K, V> toRemove = null;
/* 419 */     int expectedModCount = HashBiMap.this.modCount;
/* 420 */     int remaining = HashBiMap.this.size();
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 424 */       if (HashBiMap.this.modCount != this.expectedModCount) {
/* 425 */         throw new ConcurrentModificationException();
/*     */       }
/* 427 */       return (this.next != null && this.remaining > 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/* 432 */       if (!hasNext()) {
/* 433 */         throw new NoSuchElementException();
/*     */       }
/*     */       
/* 436 */       HashBiMap.BiEntry<K, V> entry = this.next;
/* 437 */       this.next = entry.nextInKeyInsertionOrder;
/* 438 */       this.toRemove = entry;
/* 439 */       this.remaining--;
/* 440 */       return output(entry);
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 445 */       if (HashBiMap.this.modCount != this.expectedModCount) {
/* 446 */         throw new ConcurrentModificationException();
/*     */       }
/* 448 */       CollectPreconditions.checkRemove((this.toRemove != null));
/* 449 */       HashBiMap.this.delete(this.toRemove);
/* 450 */       this.expectedModCount = HashBiMap.this.modCount;
/* 451 */       this.toRemove = null;
/*     */     }
/*     */ 
/*     */     
/*     */     abstract T output(HashBiMap.BiEntry<K, V> param1BiEntry);
/*     */   }
/*     */   
/*     */   public Set<K> keySet() {
/* 459 */     return new KeySet();
/*     */   }
/*     */   
/*     */   private final class KeySet extends Maps.KeySet<K, V> {
/*     */     KeySet() {
/* 464 */       super(HashBiMap.this);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<K> iterator() {
/* 469 */       return new HashBiMap<K, V>.Itr<K>(this)
/*     */         {
/*     */           K output(HashBiMap.BiEntry<K, V> entry) {
/* 472 */             return entry.key;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object o) {
/* 479 */       HashBiMap.BiEntry<K, V> entry = HashBiMap.this.seekByKey(o, Hashing.smearedHash(o));
/* 480 */       if (entry == null) {
/* 481 */         return false;
/*     */       }
/* 483 */       HashBiMap.this.delete(entry);
/* 484 */       entry.prevInKeyInsertionOrder = null;
/* 485 */       entry.nextInKeyInsertionOrder = null;
/* 486 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<V> values() {
/* 493 */     return inverse().keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Map.Entry<K, V>> entryIterator() {
/* 498 */     return new Itr<Map.Entry<K, V>>()
/*     */       {
/*     */         Map.Entry<K, V> output(HashBiMap.BiEntry<K, V> entry) {
/* 501 */           return new MapEntry(entry);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         class MapEntry
/*     */           extends AbstractMapEntry<K, V>
/*     */         {
/*     */           HashBiMap.BiEntry<K, V> delegate;
/*     */ 
/*     */           
/*     */           public K getKey() {
/* 513 */             return this.delegate.key;
/*     */           }
/*     */ 
/*     */           
/*     */           public V getValue() {
/* 518 */             return this.delegate.value;
/*     */           }
/*     */ 
/*     */           
/*     */           public V setValue(V value) {
/* 523 */             V oldValue = this.delegate.value;
/* 524 */             int valueHash = Hashing.smearedHash(value);
/* 525 */             if (valueHash == this.delegate.valueHash && Objects.equal(value, oldValue)) {
/* 526 */               return value;
/*     */             }
/* 528 */             Preconditions.checkArgument((HashBiMap.this.seekByValue(value, valueHash) == null), "value already present: %s", value);
/* 529 */             HashBiMap.this.delete(this.delegate);
/* 530 */             HashBiMap.BiEntry<K, V> newEntry = new HashBiMap.BiEntry<>(this.delegate.key, this.delegate.keyHash, value, valueHash);
/* 531 */             HashBiMap.this.insert(newEntry, this.delegate);
/* 532 */             this.delegate.prevInKeyInsertionOrder = null;
/* 533 */             this.delegate.nextInKeyInsertionOrder = null;
/* 534 */             HashBiMap.null.this.expectedModCount = HashBiMap.this.modCount;
/* 535 */             if (HashBiMap.null.this.toRemove == this.delegate) {
/* 536 */               HashBiMap.null.this.toRemove = newEntry;
/*     */             }
/* 538 */             this.delegate = newEntry;
/* 539 */             return oldValue;
/*     */           }
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 547 */     Preconditions.checkNotNull(action);
/* 548 */     BiEntry<K, V> entry = this.firstInKeyInsertionOrder;
/* 549 */     for (; entry != null; 
/* 550 */       entry = entry.nextInKeyInsertionOrder) {
/* 551 */       action.accept(entry.key, entry.value);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/* 557 */     Preconditions.checkNotNull(function);
/* 558 */     BiEntry<K, V> oldFirst = this.firstInKeyInsertionOrder;
/* 559 */     clear();
/* 560 */     for (BiEntry<K, V> entry = oldFirst; entry != null; entry = entry.nextInKeyInsertionOrder) {
/* 561 */       put(entry.key, function.apply(entry.key, entry.value));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BiMap<V, K> inverse() {
/* 569 */     BiMap<V, K> result = this.inverse;
/* 570 */     return (result == null) ? (this.inverse = new Inverse()) : result;
/*     */   }
/*     */   
/*     */   private final class Inverse extends Maps.IteratorBasedAbstractMap<V, K> implements BiMap<V, K>, Serializable { private Inverse() {}
/*     */     
/*     */     BiMap<K, V> forward() {
/* 576 */       return HashBiMap.this;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 581 */       return HashBiMap.this.size;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 586 */       forward().clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object value) {
/* 591 */       return forward().containsValue(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public K get(Object value) {
/* 596 */       return Maps.keyOrNull(HashBiMap.this.seekByValue(value, Hashing.smearedHash(value)));
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public K put(V value, K key) {
/* 602 */       return HashBiMap.this.putInverse(value, key, false);
/*     */     }
/*     */ 
/*     */     
/*     */     public K forcePut(V value, K key) {
/* 607 */       return HashBiMap.this.putInverse(value, key, true);
/*     */     }
/*     */ 
/*     */     
/*     */     public K remove(Object value) {
/* 612 */       HashBiMap.BiEntry<K, V> entry = HashBiMap.this.seekByValue(value, Hashing.smearedHash(value));
/* 613 */       if (entry == null) {
/* 614 */         return null;
/*     */       }
/* 616 */       HashBiMap.this.delete(entry);
/* 617 */       entry.prevInKeyInsertionOrder = null;
/* 618 */       entry.nextInKeyInsertionOrder = null;
/* 619 */       return entry.key;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public BiMap<K, V> inverse() {
/* 625 */       return forward();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<V> keySet() {
/* 630 */       return new InverseKeySet();
/*     */     }
/*     */     
/*     */     private final class InverseKeySet extends Maps.KeySet<V, K> {
/*     */       InverseKeySet() {
/* 635 */         super(HashBiMap.Inverse.this);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(Object o) {
/* 640 */         HashBiMap.BiEntry<K, V> entry = HashBiMap.this.seekByValue(o, Hashing.smearedHash(o));
/* 641 */         if (entry == null) {
/* 642 */           return false;
/*     */         }
/* 644 */         HashBiMap.this.delete(entry);
/* 645 */         return true;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public Iterator<V> iterator() {
/* 651 */         return new HashBiMap<K, V>.Itr<V>(this)
/*     */           {
/*     */             V output(HashBiMap.BiEntry<K, V> entry) {
/* 654 */               return entry.value;
/*     */             }
/*     */           };
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<K> values() {
/* 662 */       return forward().keySet();
/*     */     }
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<V, K>> entryIterator() {
/* 667 */       return new HashBiMap<K, V>.Itr<Map.Entry<V, K>>()
/*     */         {
/*     */           Map.Entry<V, K> output(HashBiMap.BiEntry<K, V> entry) {
/* 670 */             return new InverseEntry(entry);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           class InverseEntry
/*     */             extends AbstractMapEntry<V, K>
/*     */           {
/*     */             HashBiMap.BiEntry<K, V> delegate;
/*     */ 
/*     */             
/*     */             public V getKey() {
/* 682 */               return this.delegate.value;
/*     */             }
/*     */ 
/*     */             
/*     */             public K getValue() {
/* 687 */               return this.delegate.key;
/*     */             }
/*     */ 
/*     */             
/*     */             public K setValue(K key) {
/* 692 */               K oldKey = this.delegate.key;
/* 693 */               int keyHash = Hashing.smearedHash(key);
/* 694 */               if (keyHash == this.delegate.keyHash && Objects.equal(key, oldKey)) {
/* 695 */                 return key;
/*     */               }
/* 697 */               Preconditions.checkArgument((HashBiMap.this.seekByKey(key, keyHash) == null), "value already present: %s", key);
/* 698 */               HashBiMap.this.delete(this.delegate);
/* 699 */               HashBiMap.BiEntry<K, V> newEntry = new HashBiMap.BiEntry<>(key, keyHash, this.delegate.value, this.delegate.valueHash);
/*     */               
/* 701 */               this.delegate = newEntry;
/* 702 */               HashBiMap.this.insert(newEntry, (HashBiMap.BiEntry<K, V>)null);
/* 703 */               HashBiMap.Inverse.null.this.expectedModCount = HashBiMap.this.modCount;
/* 704 */               return oldKey;
/*     */             }
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(BiConsumer<? super V, ? super K> action) {
/* 712 */       Preconditions.checkNotNull(action);
/* 713 */       HashBiMap.this.forEach((k, v) -> action.accept(v, k));
/*     */     }
/*     */ 
/*     */     
/*     */     public void replaceAll(BiFunction<? super V, ? super K, ? extends K> function) {
/* 718 */       Preconditions.checkNotNull(function);
/* 719 */       HashBiMap.BiEntry<K, V> oldFirst = HashBiMap.this.firstInKeyInsertionOrder;
/* 720 */       clear();
/* 721 */       for (HashBiMap.BiEntry<K, V> entry = oldFirst; entry != null; entry = entry.nextInKeyInsertionOrder) {
/* 722 */         put(entry.value, function.apply(entry.value, entry.key));
/*     */       }
/*     */     }
/*     */     
/*     */     Object writeReplace() {
/* 727 */       return new HashBiMap.InverseSerializedForm<>(HashBiMap.this);
/*     */     } }
/*     */ 
/*     */   
/*     */   private static final class InverseSerializedForm<K, V> implements Serializable {
/*     */     private final HashBiMap<K, V> bimap;
/*     */     
/*     */     InverseSerializedForm(HashBiMap<K, V> bimap) {
/* 735 */       this.bimap = bimap;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 739 */       return this.bimap.inverse();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 748 */     stream.defaultWriteObject();
/* 749 */     Serialization.writeMap(this, stream);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 754 */     stream.defaultReadObject();
/* 755 */     int size = Serialization.readCount(stream);
/* 756 */     init(16);
/* 757 */     Serialization.populateMap(this, stream, size);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\HashBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */