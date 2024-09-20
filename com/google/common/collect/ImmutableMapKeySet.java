/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.Serializable;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Spliterator;
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible(emulated = true)
/*    */ final class ImmutableMapKeySet<K, V>
/*    */   extends IndexedImmutableSet<K>
/*    */ {
/*    */   private final ImmutableMap<K, V> map;
/*    */   
/*    */   ImmutableMapKeySet(ImmutableMap<K, V> map) {
/* 39 */     this.map = map;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 44 */     return this.map.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public UnmodifiableIterator<K> iterator() {
/* 49 */     return this.map.keyIterator();
/*    */   }
/*    */ 
/*    */   
/*    */   public Spliterator<K> spliterator() {
/* 54 */     return this.map.keySpliterator();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(Object object) {
/* 59 */     return this.map.containsKey(object);
/*    */   }
/*    */ 
/*    */   
/*    */   K get(int index) {
/* 64 */     return (K)((Map.Entry)this.map.entrySet().asList().get(index)).getKey();
/*    */   }
/*    */ 
/*    */   
/*    */   public void forEach(Consumer<? super K> action) {
/* 69 */     Preconditions.checkNotNull(action);
/* 70 */     this.map.forEach((k, v) -> action.accept(k));
/*    */   }
/*    */ 
/*    */   
/*    */   boolean isPartialView() {
/* 75 */     return true;
/*    */   }
/*    */   
/*    */   @GwtIncompatible
/*    */   private static class KeySetSerializedForm<K>
/*    */     implements Serializable {
/*    */     final ImmutableMap<K, ?> map;
/*    */     private static final long serialVersionUID = 0L;
/*    */     
/*    */     KeySetSerializedForm(ImmutableMap<K, ?> map) {
/* 85 */       this.map = map;
/*    */     }
/*    */     
/*    */     Object readResolve() {
/* 89 */       return this.map.keySet();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\ImmutableMapKeySet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */