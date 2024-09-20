/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.inject.Key;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class InjectorJitBindingData
/*     */ {
/*  18 */   private final Map<Key<?>, BindingImpl<?>> jitBindings = Maps.newHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  23 */   private final Set<Key<?>> failedJitBindings = Sets.newHashSet();
/*     */ 
/*     */ 
/*     */   
/*     */   private final WeakKeySet bannedKeys;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Optional<InjectorJitBindingData> parent;
/*     */ 
/*     */   
/*     */   private final Object lock;
/*     */ 
/*     */ 
/*     */   
/*     */   InjectorJitBindingData(Optional<InjectorJitBindingData> parent) {
/*  39 */     this.parent = parent;
/*  40 */     this.lock = parent.isPresent() ? ((InjectorJitBindingData)parent.get()).lock() : this;
/*  41 */     this.bannedKeys = new WeakKeySet(this.lock);
/*     */   }
/*     */   
/*     */   Map<Key<?>, BindingImpl<?>> getJitBindings() {
/*  45 */     return Collections.unmodifiableMap(this.jitBindings);
/*     */   }
/*     */   
/*     */   BindingImpl<?> getJitBinding(Key<?> key) {
/*  49 */     return this.jitBindings.get(key);
/*     */   }
/*     */   
/*     */   void putJitBinding(Key<?> key, BindingImpl<?> binding) {
/*  53 */     this.jitBindings.put(key, binding);
/*     */   }
/*     */   
/*     */   void removeJitBinding(Key<?> key) {
/*  57 */     this.jitBindings.remove(key);
/*     */   }
/*     */   
/*     */   boolean isFailedJitBinding(Key<?> key) {
/*  61 */     return this.failedJitBindings.contains(key);
/*     */   }
/*     */   
/*     */   void addFailedJitBinding(Key<?> key) {
/*  65 */     this.failedJitBindings.add(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void banKey(Key<?> key, InjectorBindingData injectorBindingData, Object source) {
/*  75 */     banKeyInParent(key, injectorBindingData, source);
/*  76 */     this.bannedKeys.add(key, injectorBindingData, source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void banKeyInParent(Key<?> key, InjectorBindingData injectorBindingData, Object source) {
/*  85 */     if (this.parent.isPresent()) {
/*  86 */       ((InjectorJitBindingData)this.parent.get()).banKey(key, injectorBindingData, source);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isBannedKey(Key<?> key) {
/*  95 */     return this.bannedKeys.contains(key);
/*     */   }
/*     */ 
/*     */   
/*     */   Set<Object> getSourcesForBannedKey(Key<?> key) {
/* 100 */     return this.bannedKeys.getSources(key);
/*     */   }
/*     */   
/*     */   Object lock() {
/* 104 */     return this.lock;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\InjectorJitBindingData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */