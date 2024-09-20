/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Spliterator;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ abstract class ImmutableMapEntrySet<K, V>
/*     */   extends ImmutableSet<Map.Entry<K, V>>
/*     */ {
/*     */   abstract ImmutableMap<K, V> map();
/*     */   
/*     */   static final class RegularEntrySet<K, V>
/*     */     extends ImmutableMapEntrySet<K, V>
/*     */   {
/*     */     private final transient ImmutableMap<K, V> map;
/*     */     private final transient ImmutableList<Map.Entry<K, V>> entries;
/*     */     
/*     */     RegularEntrySet(ImmutableMap<K, V> map, Map.Entry<K, V>[] entries) {
/*  40 */       this(map, ImmutableList.asImmutableList((Object[])entries));
/*     */     }
/*     */     
/*     */     RegularEntrySet(ImmutableMap<K, V> map, ImmutableList<Map.Entry<K, V>> entries) {
/*  44 */       this.map = map;
/*  45 */       this.entries = entries;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<K, V> map() {
/*  50 */       return this.map;
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible("not used in GWT")
/*     */     int copyIntoArray(Object[] dst, int offset) {
/*  56 */       return this.entries.copyIntoArray(dst, offset);
/*     */     }
/*     */ 
/*     */     
/*     */     public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/*  61 */       return this.entries.iterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<Map.Entry<K, V>> spliterator() {
/*  66 */       return this.entries.spliterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<? super Map.Entry<K, V>> action) {
/*  71 */       this.entries.forEach(action);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableList<Map.Entry<K, V>> createAsList() {
/*  76 */       return new RegularImmutableAsList<>(this, this.entries);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/*  86 */     return map().size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/*  91 */     if (object instanceof Map.Entry) {
/*  92 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/*  93 */       V value = map().get(entry.getKey());
/*  94 */       return (value != null && value.equals(entry.getValue()));
/*     */     } 
/*  96 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 101 */     return map().isPartialView();
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   boolean isHashCodeFast() {
/* 107 */     return map().isHashCodeFast();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 112 */     return map().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 118 */     return new EntrySetSerializedForm<>(map());
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static class EntrySetSerializedForm<K, V> implements Serializable {
/*     */     final ImmutableMap<K, V> map;
/*     */     
/*     */     EntrySetSerializedForm(ImmutableMap<K, V> map) {
/* 126 */       this.map = map;
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     Object readResolve() {
/* 130 */       return this.map.entrySet();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\ImmutableMapEntrySet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */