/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MapIteratorCache<K, V>
/*     */ {
/*     */   private final Map<K, V> backingMap;
/*     */   private volatile transient Map.Entry<K, V> cacheEntry;
/*     */   
/*     */   MapIteratorCache(Map<K, V> backingMap) {
/*  59 */     this.backingMap = (Map<K, V>)Preconditions.checkNotNull(backingMap);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final V put(K key, V value) {
/*  64 */     clearCache();
/*  65 */     return this.backingMap.put(key, value);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final V remove(Object key) {
/*  70 */     clearCache();
/*  71 */     return this.backingMap.remove(key);
/*     */   }
/*     */   
/*     */   public final void clear() {
/*  75 */     clearCache();
/*  76 */     this.backingMap.clear();
/*     */   }
/*     */   
/*     */   public V get(Object key) {
/*  80 */     V value = getIfCached(key);
/*  81 */     return (value != null) ? value : getWithoutCaching(key);
/*     */   }
/*     */   
/*     */   public final V getWithoutCaching(Object key) {
/*  85 */     return this.backingMap.get(key);
/*     */   }
/*     */   
/*     */   public final boolean containsKey(Object key) {
/*  89 */     return (getIfCached(key) != null || this.backingMap.containsKey(key));
/*     */   }
/*     */   
/*     */   public final Set<K> unmodifiableKeySet() {
/*  93 */     return new AbstractSet<K>()
/*     */       {
/*     */         public UnmodifiableIterator<K> iterator() {
/*  96 */           final Iterator<Map.Entry<K, V>> entryIterator = MapIteratorCache.this.backingMap.entrySet().iterator();
/*     */           
/*  98 */           return new UnmodifiableIterator<K>()
/*     */             {
/*     */               public boolean hasNext() {
/* 101 */                 return entryIterator.hasNext();
/*     */               }
/*     */ 
/*     */               
/*     */               public K next() {
/* 106 */                 Map.Entry<K, V> entry = entryIterator.next();
/* 107 */                 MapIteratorCache.this.cacheEntry = entry;
/* 108 */                 return entry.getKey();
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/* 115 */           return MapIteratorCache.this.backingMap.size();
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean contains(Object key) {
/* 120 */           return MapIteratorCache.this.containsKey(key);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected V getIfCached(Object key) {
/* 128 */     Map.Entry<K, V> entry = this.cacheEntry;
/*     */ 
/*     */     
/* 131 */     if (entry != null && entry.getKey() == key) {
/* 132 */       return entry.getValue();
/*     */     }
/* 134 */     return null;
/*     */   }
/*     */   
/*     */   protected void clearCache() {
/* 138 */     this.cacheEntry = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\graph\MapIteratorCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */