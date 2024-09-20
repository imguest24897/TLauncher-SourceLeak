/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
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
/*     */ final class SingletonImmutableBiMap<K, V>
/*     */   extends ImmutableBiMap<K, V>
/*     */ {
/*     */   final transient K singleKey;
/*     */   final transient V singleValue;
/*     */   private final transient ImmutableBiMap<V, K> inverse;
/*     */   @LazyInit
/*     */   @RetainedWith
/*     */   private transient ImmutableBiMap<V, K> lazyInverse;
/*     */   
/*     */   SingletonImmutableBiMap(K singleKey, V singleValue) {
/*  42 */     CollectPreconditions.checkEntryNotNull(singleKey, singleValue);
/*  43 */     this.singleKey = singleKey;
/*  44 */     this.singleValue = singleValue;
/*  45 */     this.inverse = null;
/*     */   }
/*     */   
/*     */   private SingletonImmutableBiMap(K singleKey, V singleValue, ImmutableBiMap<V, K> inverse) {
/*  49 */     this.singleKey = singleKey;
/*  50 */     this.singleValue = singleValue;
/*  51 */     this.inverse = inverse;
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/*  56 */     return this.singleKey.equals(key) ? this.singleValue : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  61 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/*  66 */     ((BiConsumer<K, V>)Preconditions.checkNotNull(action)).accept(this.singleKey, this.singleValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/*  71 */     return this.singleKey.equals(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/*  76 */     return this.singleValue.equals(value);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/*  81 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/*  86 */     return ImmutableSet.of(Maps.immutableEntry(this.singleKey, this.singleValue));
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<K> createKeySet() {
/*  91 */     return ImmutableSet.of(this.singleKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableBiMap<V, K> inverse() {
/*  99 */     if (this.inverse != null) {
/* 100 */       return this.inverse;
/*     */     }
/*     */     
/* 103 */     ImmutableBiMap<V, K> result = this.lazyInverse;
/* 104 */     if (result == null) {
/* 105 */       return this.lazyInverse = new SingletonImmutableBiMap((K)this.singleValue, (V)this.singleKey, this);
/*     */     }
/* 107 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\SingletonImmutableBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */