/*     */ package com.google.common.graph;
/*     */ 
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MapRetrievalCache<K, V>
/*     */   extends MapIteratorCache<K, V>
/*     */ {
/*     */   private volatile transient CacheEntry<K, V> cacheEntry1;
/*     */   private volatile transient CacheEntry<K, V> cacheEntry2;
/*     */   
/*     */   MapRetrievalCache(Map<K, V> backingMap) {
/*  34 */     super(backingMap);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/*  40 */     V value = getIfCached(key);
/*  41 */     if (value != null) {
/*  42 */       return value;
/*     */     }
/*     */     
/*  45 */     value = getWithoutCaching(key);
/*  46 */     if (value != null) {
/*  47 */       addToCache((K)key, value);
/*     */     }
/*  49 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected V getIfCached(Object key) {
/*  56 */     V value = super.getIfCached(key);
/*  57 */     if (value != null) {
/*  58 */       return value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  66 */     CacheEntry<K, V> entry = this.cacheEntry1;
/*  67 */     if (entry != null && entry.key == key) {
/*  68 */       return entry.value;
/*     */     }
/*  70 */     entry = this.cacheEntry2;
/*  71 */     if (entry != null && entry.key == key) {
/*     */ 
/*     */       
/*  74 */       addToCache(entry);
/*  75 */       return entry.value;
/*     */     } 
/*  77 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void clearCache() {
/*  82 */     super.clearCache();
/*  83 */     this.cacheEntry1 = null;
/*  84 */     this.cacheEntry2 = null;
/*     */   }
/*     */   
/*     */   private void addToCache(K key, V value) {
/*  88 */     addToCache(new CacheEntry<>(key, value));
/*     */   }
/*     */ 
/*     */   
/*     */   private void addToCache(CacheEntry<K, V> entry) {
/*  93 */     this.cacheEntry2 = this.cacheEntry1;
/*  94 */     this.cacheEntry1 = entry;
/*     */   }
/*     */   
/*     */   private static final class CacheEntry<K, V> {
/*     */     final K key;
/*     */     final V value;
/*     */     
/*     */     CacheEntry(K key, V value) {
/* 102 */       this.key = key;
/* 103 */       this.value = value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\graph\MapRetrievalCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */