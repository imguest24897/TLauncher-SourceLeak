/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ abstract class AbstractBiMap<K, V>
/*     */   extends ForwardingMap<K, V>
/*     */   implements BiMap<K, V>, Serializable
/*     */ {
/*     */   private transient Map<K, V> delegate;
/*     */   @RetainedWith
/*     */   transient AbstractBiMap<V, K> inverse;
/*     */   private transient Set<K> keySet;
/*     */   private transient Set<V> valueSet;
/*     */   private transient Set<Map.Entry<K, V>> entrySet;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   AbstractBiMap(Map<K, V> forward, Map<V, K> backward) {
/*  58 */     setDelegates(forward, backward);
/*     */   }
/*     */ 
/*     */   
/*     */   private AbstractBiMap(Map<K, V> backward, AbstractBiMap<V, K> forward) {
/*  63 */     this.delegate = backward;
/*  64 */     this.inverse = forward;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<K, V> delegate() {
/*  69 */     return this.delegate;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   K checkKey(K key) {
/*  75 */     return key;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   V checkValue(V value) {
/*  81 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setDelegates(Map<K, V> forward, Map<V, K> backward) {
/*  89 */     Preconditions.checkState((this.delegate == null));
/*  90 */     Preconditions.checkState((this.inverse == null));
/*  91 */     Preconditions.checkArgument(forward.isEmpty());
/*  92 */     Preconditions.checkArgument(backward.isEmpty());
/*  93 */     Preconditions.checkArgument((forward != backward));
/*  94 */     this.delegate = forward;
/*  95 */     this.inverse = makeInverse(backward);
/*     */   }
/*     */   
/*     */   AbstractBiMap<V, K> makeInverse(Map<V, K> backward) {
/*  99 */     return new Inverse<>(backward, this);
/*     */   }
/*     */   
/*     */   void setInverse(AbstractBiMap<V, K> inverse) {
/* 103 */     this.inverse = inverse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 110 */     return this.inverse.containsKey(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V put(K key, V value) {
/* 118 */     return putInBothMaps(key, value, false);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V forcePut(K key, V value) {
/* 124 */     return putInBothMaps(key, value, true);
/*     */   }
/*     */   
/*     */   private V putInBothMaps(K key, V value, boolean force) {
/* 128 */     checkKey(key);
/* 129 */     checkValue(value);
/* 130 */     boolean containedKey = containsKey(key);
/* 131 */     if (containedKey && Objects.equal(value, get(key))) {
/* 132 */       return value;
/*     */     }
/* 134 */     if (force) {
/* 135 */       inverse().remove(value);
/*     */     } else {
/* 137 */       Preconditions.checkArgument(!containsValue(value), "value already present: %s", value);
/*     */     } 
/* 139 */     V oldValue = this.delegate.put(key, value);
/* 140 */     updateInverseMap(key, containedKey, oldValue, value);
/* 141 */     return oldValue;
/*     */   }
/*     */   
/*     */   private void updateInverseMap(K key, boolean containedKey, V oldValue, V newValue) {
/* 145 */     if (containedKey) {
/* 146 */       removeFromInverseMap(oldValue);
/*     */     }
/* 148 */     this.inverse.delegate.put((K)newValue, (V)key);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V remove(Object key) {
/* 154 */     return containsKey(key) ? removeFromBothMaps(key) : null;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private V removeFromBothMaps(Object key) {
/* 159 */     V oldValue = this.delegate.remove(key);
/* 160 */     removeFromInverseMap(oldValue);
/* 161 */     return oldValue;
/*     */   }
/*     */   
/*     */   private void removeFromInverseMap(V oldValue) {
/* 165 */     this.inverse.delegate.remove(oldValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> map) {
/* 172 */     for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 173 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/* 179 */     this.delegate.replaceAll(function);
/* 180 */     this.inverse.delegate.clear();
/* 181 */     Map.Entry<K, V> broken = null;
/* 182 */     Iterator<Map.Entry<K, V>> itr = this.delegate.entrySet().iterator();
/* 183 */     while (itr.hasNext()) {
/* 184 */       Map.Entry<K, V> entry = itr.next();
/* 185 */       K k = entry.getKey();
/* 186 */       V v = entry.getValue();
/* 187 */       K conflict = (K)this.inverse.delegate.putIfAbsent((K)v, (V)k);
/* 188 */       if (conflict != null) {
/* 189 */         broken = entry;
/*     */ 
/*     */         
/* 192 */         itr.remove();
/*     */       } 
/*     */     } 
/* 195 */     if (broken != null) {
/* 196 */       String str = String.valueOf(broken.getValue()); throw new IllegalArgumentException((new StringBuilder(23 + String.valueOf(str).length())).append("value already present: ").append(str).toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 202 */     this.delegate.clear();
/* 203 */     this.inverse.delegate.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BiMap<V, K> inverse() {
/* 210 */     return this.inverse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 217 */     Set<K> result = this.keySet;
/* 218 */     return (result == null) ? (this.keySet = new KeySet()) : result;
/*     */   }
/*     */   
/*     */   private class KeySet extends ForwardingSet<K> {
/*     */     private KeySet() {}
/*     */     
/*     */     protected Set<K> delegate() {
/* 225 */       return AbstractBiMap.this.delegate.keySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 230 */       AbstractBiMap.this.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object key) {
/* 235 */       if (!contains(key)) {
/* 236 */         return false;
/*     */       }
/* 238 */       AbstractBiMap.this.removeFromBothMaps(key);
/* 239 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection<?> keysToRemove) {
/* 244 */       return standardRemoveAll(keysToRemove);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection<?> keysToRetain) {
/* 249 */       return standardRetainAll(keysToRetain);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<K> iterator() {
/* 254 */       return Maps.keyIterator(AbstractBiMap.this.entrySet().iterator());
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
/*     */   public Set<V> values() {
/* 266 */     Set<V> result = this.valueSet;
/* 267 */     return (result == null) ? (this.valueSet = new ValueSet()) : result;
/*     */   }
/*     */   
/*     */   private class ValueSet
/*     */     extends ForwardingSet<V> {
/* 272 */     final Set<V> valuesDelegate = AbstractBiMap.this.inverse.keySet();
/*     */ 
/*     */     
/*     */     protected Set<V> delegate() {
/* 276 */       return this.valuesDelegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<V> iterator() {
/* 281 */       return Maps.valueIterator(AbstractBiMap.this.entrySet().iterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 286 */       return standardToArray();
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 291 */       return (T[])standardToArray((Object[])array);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 296 */       return standardToString();
/*     */     }
/*     */ 
/*     */     
/*     */     private ValueSet() {}
/*     */   }
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 304 */     Set<Map.Entry<K, V>> result = this.entrySet;
/* 305 */     return (result == null) ? (this.entrySet = new EntrySet()) : result;
/*     */   }
/*     */   
/*     */   class BiMapEntry extends ForwardingMapEntry<K, V> {
/*     */     private final Map.Entry<K, V> delegate;
/*     */     
/*     */     BiMapEntry(Map.Entry<K, V> delegate) {
/* 312 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Map.Entry<K, V> delegate() {
/* 317 */       return this.delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public V setValue(V value) {
/* 322 */       AbstractBiMap.this.checkValue(value);
/*     */       
/* 324 */       Preconditions.checkState(AbstractBiMap.this.entrySet().contains(this), "entry no longer in map");
/*     */       
/* 326 */       if (Objects.equal(value, getValue())) {
/* 327 */         return value;
/*     */       }
/* 329 */       Preconditions.checkArgument(!AbstractBiMap.this.containsValue(value), "value already present: %s", value);
/* 330 */       V oldValue = this.delegate.setValue(value);
/* 331 */       Preconditions.checkState(Objects.equal(value, AbstractBiMap.this.get(getKey())), "entry no longer in map");
/* 332 */       AbstractBiMap.this.updateInverseMap(getKey(), true, oldValue, value);
/* 333 */       return oldValue;
/*     */     }
/*     */   }
/*     */   
/*     */   Iterator<Map.Entry<K, V>> entrySetIterator() {
/* 338 */     final Iterator<Map.Entry<K, V>> iterator = this.delegate.entrySet().iterator();
/* 339 */     return new Iterator<Map.Entry<K, V>>()
/*     */       {
/*     */         Map.Entry<K, V> entry;
/*     */         
/*     */         public boolean hasNext() {
/* 344 */           return iterator.hasNext();
/*     */         }
/*     */ 
/*     */         
/*     */         public Map.Entry<K, V> next() {
/* 349 */           this.entry = iterator.next();
/* 350 */           return new AbstractBiMap.BiMapEntry(this.entry);
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 355 */           CollectPreconditions.checkRemove((this.entry != null));
/* 356 */           V value = this.entry.getValue();
/* 357 */           iterator.remove();
/* 358 */           AbstractBiMap.this.removeFromInverseMap(value);
/* 359 */           this.entry = null;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private class EntrySet
/*     */     extends ForwardingSet<Map.Entry<K, V>> {
/* 366 */     final Set<Map.Entry<K, V>> esDelegate = AbstractBiMap.this.delegate.entrySet();
/*     */ 
/*     */     
/*     */     protected Set<Map.Entry<K, V>> delegate() {
/* 370 */       return this.esDelegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 375 */       AbstractBiMap.this.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object object) {
/* 380 */       if (!this.esDelegate.contains(object)) {
/* 381 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 385 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/* 386 */       AbstractBiMap.this.inverse.delegate.remove(entry.getValue());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 392 */       this.esDelegate.remove(entry);
/* 393 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 398 */       return AbstractBiMap.this.entrySetIterator();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 405 */       return standardToArray();
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 410 */       return (T[])standardToArray((Object[])array);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object o) {
/* 415 */       return Maps.containsEntryImpl(delegate(), o);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsAll(Collection<?> c) {
/* 420 */       return standardContainsAll(c);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection<?> c) {
/* 425 */       return standardRemoveAll(c);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection<?> c) {
/* 430 */       return standardRetainAll(c);
/*     */     }
/*     */     
/*     */     private EntrySet() {} }
/*     */   
/*     */   static class Inverse<K, V> extends AbstractBiMap<K, V> {
/*     */     Inverse(Map<K, V> backward, AbstractBiMap<V, K> forward) {
/* 437 */       super(backward, forward);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     K checkKey(K key) {
/* 451 */       return this.inverse.checkValue(key);
/*     */     }
/*     */ 
/*     */     
/*     */     V checkValue(V value) {
/* 456 */       return this.inverse.checkKey(value);
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     private void writeObject(ObjectOutputStream stream) throws IOException {
/* 462 */       stream.defaultWriteObject();
/* 463 */       stream.writeObject(inverse());
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 469 */       stream.defaultReadObject();
/* 470 */       setInverse((AbstractBiMap<V, K>)stream.readObject());
/*     */     }
/*     */     
/*     */     @GwtIncompatible
/*     */     Object readResolve() {
/* 475 */       return inverse().inverse();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\AbstractBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */