/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.cache.Cache;
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.RemovalCause;
/*     */ import com.google.common.cache.RemovalNotification;
/*     */ import com.google.common.collect.LinkedHashMultiset;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Multiset;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.internal.util.SourceProvider;
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
/*     */ final class WeakKeySet
/*     */ {
/*     */   private Map<Key<?>, Multiset<Object>> backingMap;
/*     */   private final Object lock;
/*  54 */   private final Cache<InjectorBindingData, Set<KeyAndSource>> evictionCache = CacheBuilder.newBuilder().weakKeys().removalListener(this::cleanupOnRemoval).build();
/*     */ 
/*     */   
/*     */   private void cleanupOnRemoval(RemovalNotification<InjectorBindingData, Set<KeyAndSource>> notification) {
/*  58 */     Preconditions.checkState(RemovalCause.COLLECTED.equals(notification.getCause()));
/*     */ 
/*     */ 
/*     */     
/*  62 */     synchronized (this.lock) {
/*  63 */       for (KeyAndSource keyAndSource : notification.getValue()) {
/*  64 */         Multiset<Object> set = this.backingMap.get(keyAndSource.key);
/*  65 */         if (set != null) {
/*  66 */           set.remove(keyAndSource.source);
/*  67 */           if (set.isEmpty()) {
/*  68 */             this.backingMap.remove(keyAndSource.key);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   WeakKeySet(Object lock) {
/*  76 */     this.lock = lock;
/*     */   }
/*     */   
/*     */   public void add(Key<?> key, InjectorBindingData state, Object source) {
/*  80 */     if (this.backingMap == null) {
/*  81 */       this.backingMap = Maps.newHashMap();
/*     */     }
/*     */ 
/*     */     
/*  85 */     if (source instanceof Class || source == SourceProvider.UNKNOWN_SOURCE) {
/*  86 */       source = null;
/*     */     }
/*  88 */     Object convertedSource = Errors.convert(source);
/*  89 */     ((Multiset)this.backingMap.computeIfAbsent(key, k -> LinkedHashMultiset.create())).add(convertedSource);
/*     */ 
/*     */     
/*  92 */     if (state.parent().isPresent()) {
/*  93 */       Set<KeyAndSource> keyAndSources = (Set<KeyAndSource>)this.evictionCache.getIfPresent(state);
/*  94 */       if (keyAndSources == null) {
/*  95 */         this.evictionCache.put(state, keyAndSources = Sets.newHashSet());
/*     */       }
/*  97 */       keyAndSources.add(new KeyAndSource(key, convertedSource));
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean contains(Key<?> key) {
/* 102 */     this.evictionCache.cleanUp();
/* 103 */     return (this.backingMap != null && this.backingMap.containsKey(key));
/*     */   }
/*     */   
/*     */   public Set<Object> getSources(Key<?> key) {
/* 107 */     this.evictionCache.cleanUp();
/* 108 */     Multiset<Object> sources = (this.backingMap == null) ? null : this.backingMap.get(key);
/* 109 */     return (sources == null) ? null : sources.elementSet();
/*     */   }
/*     */   
/*     */   private static final class KeyAndSource {
/*     */     final Key<?> key;
/*     */     final Object source;
/*     */     
/*     */     KeyAndSource(Key<?> key, Object source) {
/* 117 */       this.key = key;
/* 118 */       this.source = source;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 123 */       return Objects.hashCode(new Object[] { this.key, this.source });
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 128 */       if (this == obj) {
/* 129 */         return true;
/*     */       }
/*     */       
/* 132 */       if (!(obj instanceof KeyAndSource)) {
/* 133 */         return false;
/*     */       }
/*     */       
/* 136 */       KeyAndSource other = (KeyAndSource)obj;
/* 137 */       return (Objects.equal(this.key, other.key) && Objects.equal(this.source, other.source));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\WeakKeySet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */