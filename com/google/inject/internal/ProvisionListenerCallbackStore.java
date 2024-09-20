/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.inject.Binding;
/*     */ import com.google.inject.Injector;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.Stage;
/*     */ import com.google.inject.spi.ProvisionListener;
/*     */ import com.google.inject.spi.ProvisionListenerBinding;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
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
/*     */ final class ProvisionListenerCallbackStore
/*     */ {
/*  44 */   private static final ImmutableSet<Key<?>> INTERNAL_BINDINGS = ImmutableSet.of(Key.get(Injector.class), Key.get(Stage.class), Key.get(Logger.class));
/*     */   
/*     */   private final ImmutableList<ProvisionListenerBinding> listenerBindings;
/*     */ 
/*     */   
/*  49 */   private final LoadingCache<KeyBinding, ProvisionListenerStackCallback<?>> cache = CacheBuilder.newBuilder()
/*  50 */     .build(new CacheLoader<KeyBinding, ProvisionListenerStackCallback<?>>()
/*     */       {
/*     */         public ProvisionListenerStackCallback<?> load(ProvisionListenerCallbackStore.KeyBinding key)
/*     */         {
/*  54 */           return ProvisionListenerCallbackStore.this.create((Binding)key.binding);
/*     */         }
/*     */       });
/*     */   
/*     */   ProvisionListenerCallbackStore(List<ProvisionListenerBinding> listenerBindings) {
/*  59 */     this.listenerBindings = ImmutableList.copyOf(listenerBindings);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ProvisionListenerStackCallback<T> get(Binding<T> binding) {
/*  70 */     if (!INTERNAL_BINDINGS.contains(binding.getKey())) {
/*     */ 
/*     */       
/*  73 */       ProvisionListenerStackCallback<T> callback = (ProvisionListenerStackCallback<T>)this.cache.getUnchecked(new KeyBinding(binding.getKey(), binding));
/*  74 */       return callback.hasListeners() ? callback : null;
/*     */     } 
/*  76 */     return null;
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
/*     */   boolean remove(Binding<?> type) {
/*  89 */     return (this.cache.asMap().remove(type) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <T> ProvisionListenerStackCallback<T> create(Binding<T> binding) {
/*  96 */     List<ProvisionListener> listeners = null;
/*  97 */     for (UnmodifiableIterator<ProvisionListenerBinding> unmodifiableIterator = this.listenerBindings.iterator(); unmodifiableIterator.hasNext(); ) { ProvisionListenerBinding provisionBinding = unmodifiableIterator.next();
/*  98 */       if (provisionBinding.getBindingMatcher().matches(binding)) {
/*  99 */         if (listeners == null) {
/* 100 */           listeners = Lists.newArrayList();
/*     */         }
/* 102 */         listeners.addAll(provisionBinding.getListeners());
/*     */       }  }
/*     */     
/* 105 */     if (listeners == null || listeners.isEmpty())
/*     */     {
/*     */       
/* 108 */       return ProvisionListenerStackCallback.emptyListener();
/*     */     }
/* 110 */     return new ProvisionListenerStackCallback<>(binding, listeners);
/*     */   }
/*     */   
/*     */   private static class KeyBinding
/*     */   {
/*     */     final Key<?> key;
/*     */     final Binding<?> binding;
/*     */     
/*     */     KeyBinding(Key<?> key, Binding<?> binding) {
/* 119 */       this.key = key;
/* 120 */       this.binding = binding;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 125 */       return (obj instanceof KeyBinding && this.key.equals(((KeyBinding)obj).key));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 130 */       return this.key.hashCode();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ProvisionListenerCallbackStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */