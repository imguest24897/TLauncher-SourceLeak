/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.ObjIntConsumer;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ abstract class AbstractMapBasedMultiset<E>
/*     */   extends AbstractMultiset<E>
/*     */   implements Serializable
/*     */ {
/*     */   private transient Map<E, Count> backingMap;
/*     */   private transient long size;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = -2250766705698539974L;
/*     */   
/*     */   protected AbstractMapBasedMultiset(Map<E, Count> backingMap) {
/*  60 */     Preconditions.checkArgument(backingMap.isEmpty());
/*  61 */     this.backingMap = backingMap;
/*     */   }
/*     */ 
/*     */   
/*     */   void setBackingMap(Map<E, Count> backingMap) {
/*  66 */     this.backingMap = backingMap;
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
/*     */   
/*     */   public Set<Multiset.Entry<E>> entrySet() {
/*  80 */     return super.entrySet();
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<E> elementIterator() {
/*  85 */     final Iterator<Map.Entry<E, Count>> backingEntries = this.backingMap.entrySet().iterator();
/*  86 */     return new Iterator<E>()
/*     */       {
/*     */         Map.Entry<E, Count> toRemove;
/*     */         
/*     */         public boolean hasNext() {
/*  91 */           return backingEntries.hasNext();
/*     */         }
/*     */ 
/*     */         
/*     */         public E next() {
/*  96 */           Map.Entry<E, Count> mapEntry = backingEntries.next();
/*  97 */           this.toRemove = mapEntry;
/*  98 */           return mapEntry.getKey();
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 103 */           CollectPreconditions.checkRemove((this.toRemove != null));
/* 104 */           AbstractMapBasedMultiset.this.size -= ((Count)this.toRemove.getValue()).getAndSet(0);
/* 105 */           backingEntries.remove();
/* 106 */           this.toRemove = null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Multiset.Entry<E>> entryIterator() {
/* 113 */     final Iterator<Map.Entry<E, Count>> backingEntries = this.backingMap.entrySet().iterator();
/* 114 */     return (Iterator)new Iterator<Multiset.Entry<Multiset.Entry<E>>>()
/*     */       {
/*     */         Map.Entry<E, Count> toRemove;
/*     */         
/*     */         public boolean hasNext() {
/* 119 */           return backingEntries.hasNext();
/*     */         }
/*     */ 
/*     */         
/*     */         public Multiset.Entry<E> next() {
/* 124 */           final Map.Entry<E, Count> mapEntry = backingEntries.next();
/* 125 */           this.toRemove = mapEntry;
/* 126 */           return new Multisets.AbstractEntry<E>()
/*     */             {
/*     */               public E getElement() {
/* 129 */                 return (E)mapEntry.getKey();
/*     */               }
/*     */ 
/*     */               
/*     */               public int getCount() {
/* 134 */                 Count count = (Count)mapEntry.getValue();
/* 135 */                 if (count == null || count.get() == 0) {
/* 136 */                   Count frequency = (Count)AbstractMapBasedMultiset.this.backingMap.get(getElement());
/* 137 */                   if (frequency != null) {
/* 138 */                     return frequency.get();
/*     */                   }
/*     */                 } 
/* 141 */                 return (count == null) ? 0 : count.get();
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 148 */           CollectPreconditions.checkRemove((this.toRemove != null));
/* 149 */           AbstractMapBasedMultiset.this.size -= ((Count)this.toRemove.getValue()).getAndSet(0);
/* 150 */           backingEntries.remove();
/* 151 */           this.toRemove = null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEachEntry(ObjIntConsumer<? super E> action) {
/* 158 */     Preconditions.checkNotNull(action);
/* 159 */     this.backingMap.forEach((element, count) -> action.accept(element, count.get()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 164 */     for (Count frequency : this.backingMap.values()) {
/* 165 */       frequency.set(0);
/*     */     }
/* 167 */     this.backingMap.clear();
/* 168 */     this.size = 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   int distinctElements() {
/* 173 */     return this.backingMap.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 180 */     return Ints.saturatedCast(this.size);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 185 */     return new MapBasedMultisetIterator();
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
/*     */   private class MapBasedMultisetIterator
/*     */     implements Iterator<E>
/*     */   {
/* 200 */     final Iterator<Map.Entry<E, Count>> entryIterator = AbstractMapBasedMultiset.this.backingMap.entrySet().iterator();
/*     */     
/*     */     Map.Entry<E, Count> currentEntry;
/*     */     
/*     */     public boolean hasNext() {
/* 205 */       return (this.occurrencesLeft > 0 || this.entryIterator.hasNext());
/*     */     }
/*     */     int occurrencesLeft; boolean canRemove;
/*     */     
/*     */     public E next() {
/* 210 */       if (this.occurrencesLeft == 0) {
/* 211 */         this.currentEntry = this.entryIterator.next();
/* 212 */         this.occurrencesLeft = ((Count)this.currentEntry.getValue()).get();
/*     */       } 
/* 214 */       this.occurrencesLeft--;
/* 215 */       this.canRemove = true;
/* 216 */       return this.currentEntry.getKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 221 */       CollectPreconditions.checkRemove(this.canRemove);
/* 222 */       int frequency = ((Count)this.currentEntry.getValue()).get();
/* 223 */       if (frequency <= 0) {
/* 224 */         throw new ConcurrentModificationException();
/*     */       }
/* 226 */       if (((Count)this.currentEntry.getValue()).addAndGet(-1) == 0) {
/* 227 */         this.entryIterator.remove();
/*     */       }
/* 229 */       AbstractMapBasedMultiset.this.size--;
/* 230 */       this.canRemove = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int count(Object element) {
/* 236 */     Count frequency = Maps.<Count>safeGet(this.backingMap, element);
/* 237 */     return (frequency == null) ? 0 : frequency.get();
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
/*     */   @CanIgnoreReturnValue
/*     */   public int add(E element, int occurrences) {
/*     */     int oldCount;
/* 251 */     if (occurrences == 0) {
/* 252 */       return count(element);
/*     */     }
/* 254 */     Preconditions.checkArgument((occurrences > 0), "occurrences cannot be negative: %s", occurrences);
/* 255 */     Count frequency = this.backingMap.get(element);
/*     */     
/* 257 */     if (frequency == null) {
/* 258 */       oldCount = 0;
/* 259 */       this.backingMap.put(element, new Count(occurrences));
/*     */     } else {
/* 261 */       oldCount = frequency.get();
/* 262 */       long newCount = oldCount + occurrences;
/* 263 */       Preconditions.checkArgument((newCount <= 2147483647L), "too many occurrences: %s", newCount);
/* 264 */       frequency.add(occurrences);
/*     */     } 
/* 266 */     this.size += occurrences;
/* 267 */     return oldCount;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int remove(Object element, int occurrences) {
/*     */     int numberRemoved;
/* 273 */     if (occurrences == 0) {
/* 274 */       return count(element);
/*     */     }
/* 276 */     Preconditions.checkArgument((occurrences > 0), "occurrences cannot be negative: %s", occurrences);
/* 277 */     Count frequency = this.backingMap.get(element);
/* 278 */     if (frequency == null) {
/* 279 */       return 0;
/*     */     }
/*     */     
/* 282 */     int oldCount = frequency.get();
/*     */ 
/*     */     
/* 285 */     if (oldCount > occurrences) {
/* 286 */       numberRemoved = occurrences;
/*     */     } else {
/* 288 */       numberRemoved = oldCount;
/* 289 */       this.backingMap.remove(element);
/*     */     } 
/*     */     
/* 292 */     frequency.add(-numberRemoved);
/* 293 */     this.size -= numberRemoved;
/* 294 */     return oldCount;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int setCount(E element, int count) {
/*     */     int oldCount;
/* 301 */     CollectPreconditions.checkNonnegative(count, "count");
/*     */ 
/*     */ 
/*     */     
/* 305 */     if (count == 0) {
/* 306 */       Count existingCounter = this.backingMap.remove(element);
/* 307 */       oldCount = getAndSet(existingCounter, count);
/*     */     } else {
/* 309 */       Count existingCounter = this.backingMap.get(element);
/* 310 */       oldCount = getAndSet(existingCounter, count);
/*     */       
/* 312 */       if (existingCounter == null) {
/* 313 */         this.backingMap.put(element, new Count(count));
/*     */       }
/*     */     } 
/*     */     
/* 317 */     this.size += (count - oldCount);
/* 318 */     return oldCount;
/*     */   }
/*     */   
/*     */   private static int getAndSet(Count i, int count) {
/* 322 */     if (i == null) {
/* 323 */       return 0;
/*     */     }
/*     */     
/* 326 */     return i.getAndSet(count);
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObjectNoData() throws ObjectStreamException {
/* 332 */     throw new InvalidObjectException("Stream data required");
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\AbstractMapBasedMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */