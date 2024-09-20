/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.collect.ImmutableCollection;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ abstract class CollectionFuture<V, C>
/*     */   extends AggregateFuture<V, C>
/*     */ {
/*     */   private List<Present<V>> values;
/*     */   
/*     */   CollectionFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed) {
/*  42 */     super(futures, allMustSucceed, true);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  47 */     List<Present<V>> values = futures.isEmpty() ? (List<Present<V>>)ImmutableList.of() : Lists.newArrayListWithCapacity(futures.size());
/*     */ 
/*     */     
/*  50 */     for (int i = 0; i < futures.size(); i++) {
/*  51 */       values.add(null);
/*     */     }
/*     */     
/*  54 */     this.values = values;
/*     */   }
/*     */ 
/*     */   
/*     */   final void collectOneValue(int index, V returnValue) {
/*  59 */     List<Present<V>> localValues = this.values;
/*  60 */     if (localValues != null) {
/*  61 */       localValues.set(index, new Present<>(returnValue));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   final void handleAllCompleted() {
/*  67 */     List<Present<V>> localValues = this.values;
/*  68 */     if (localValues != null) {
/*  69 */       set(combine(localValues));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void releaseResources(AggregateFuture.ReleaseResourcesReason reason) {
/*  75 */     super.releaseResources(reason);
/*  76 */     this.values = null;
/*     */   }
/*     */ 
/*     */   
/*     */   abstract C combine(List<Present<V>> paramList);
/*     */   
/*     */   static final class ListFuture<V>
/*     */     extends CollectionFuture<V, List<V>>
/*     */   {
/*     */     ListFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed) {
/*  86 */       super(futures, allMustSucceed);
/*  87 */       init();
/*     */     }
/*     */ 
/*     */     
/*     */     public List<V> combine(List<CollectionFuture.Present<V>> values) {
/*  92 */       List<V> result = Lists.newArrayListWithCapacity(values.size());
/*  93 */       for (CollectionFuture.Present<V> element : values) {
/*  94 */         result.add((element != null) ? element.value : null);
/*     */       }
/*  96 */       return Collections.unmodifiableList(result);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Present<V>
/*     */   {
/*     */     V value;
/*     */     
/*     */     Present(V value) {
/* 105 */       this.value = value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\commo\\util\concurrent\CollectionFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */