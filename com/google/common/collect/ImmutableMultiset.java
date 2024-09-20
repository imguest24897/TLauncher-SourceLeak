/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.ToIntFunction;
/*     */ import java.util.stream.Collector;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableMultiset<E>
/*     */   extends ImmutableMultisetGwtSerializationDependencies<E>
/*     */   implements Multiset<E>
/*     */ {
/*     */   @LazyInit
/*     */   private transient ImmutableList<E> asList;
/*     */   @LazyInit
/*     */   private transient ImmutableSet<Multiset.Entry<E>> entrySet;
/*     */   
/*     */   public static <E> Collector<E, ?, ImmutableMultiset<E>> toImmutableMultiset() {
/*  67 */     return CollectCollectors.toImmutableMultiset((Function)Function.identity(), e -> 1);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T, E> Collector<T, ?, ImmutableMultiset<E>> toImmutableMultiset(Function<? super T, ? extends E> elementFunction, ToIntFunction<? super T> countFunction) {
/*  84 */     return CollectCollectors.toImmutableMultiset(elementFunction, countFunction);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> of() {
/*  90 */     return (ImmutableMultiset)RegularImmutableMultiset.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> of(E element) {
/* 101 */     return copyFromElements((E[])new Object[] { element });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> of(E e1, E e2) {
/* 112 */     return copyFromElements((E[])new Object[] { e1, e2 });
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
/*     */   public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3) {
/* 124 */     return copyFromElements((E[])new Object[] { e1, e2, e3 });
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
/*     */   public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4) {
/* 136 */     return copyFromElements((E[])new Object[] { e1, e2, e3, e4 });
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
/*     */   public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4, E e5) {
/* 148 */     return copyFromElements((E[])new Object[] { e1, e2, e3, e4, e5 });
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
/*     */   public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... others) {
/* 160 */     return (new Builder<>()).add(e1).add(e2).add(e3).add(e4).add(e5).add(e6).add(others).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> copyOf(E[] elements) {
/* 171 */     return copyFromElements(elements);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> copyOf(Iterable<? extends E> elements) {
/* 181 */     if (elements instanceof ImmutableMultiset) {
/*     */       
/* 183 */       ImmutableMultiset<E> result = (ImmutableMultiset)elements;
/* 184 */       if (!result.isPartialView()) {
/* 185 */         return result;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 192 */     Multiset<? extends E> multiset = (elements instanceof Multiset) ? Multisets.<E>cast(elements) : LinkedHashMultiset.<E>create(elements);
/*     */     
/* 194 */     return copyFromEntries(multiset.entrySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> copyOf(Iterator<? extends E> elements) {
/* 204 */     Multiset<E> multiset = LinkedHashMultiset.create();
/* 205 */     Iterators.addAll(multiset, elements);
/* 206 */     return copyFromEntries(multiset.entrySet());
/*     */   }
/*     */   
/*     */   private static <E> ImmutableMultiset<E> copyFromElements(E... elements) {
/* 210 */     Multiset<E> multiset = LinkedHashMultiset.create();
/* 211 */     Collections.addAll(multiset, elements);
/* 212 */     return copyFromEntries(multiset.entrySet());
/*     */   }
/*     */ 
/*     */   
/*     */   static <E> ImmutableMultiset<E> copyFromEntries(Collection<? extends Multiset.Entry<? extends E>> entries) {
/* 217 */     if (entries.isEmpty()) {
/* 218 */       return of();
/*     */     }
/* 220 */     return RegularImmutableMultiset.create(entries);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/* 228 */     final Iterator<Multiset.Entry<E>> entryIterator = entrySet().iterator();
/* 229 */     return new UnmodifiableIterator<E>(this)
/*     */       {
/*     */         int remaining;
/*     */         E element;
/*     */         
/*     */         public boolean hasNext() {
/* 235 */           return (this.remaining > 0 || entryIterator.hasNext());
/*     */         }
/*     */ 
/*     */         
/*     */         public E next() {
/* 240 */           if (this.remaining <= 0) {
/* 241 */             Multiset.Entry<E> entry = entryIterator.next();
/* 242 */             this.element = entry.getElement();
/* 243 */             this.remaining = entry.getCount();
/*     */           } 
/* 245 */           this.remaining--;
/* 246 */           return this.element;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableList<E> asList() {
/* 255 */     ImmutableList<E> result = this.asList;
/* 256 */     return (result == null) ? (this.asList = super.asList()) : result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/* 261 */     return (count(object) > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final int add(E element, int occurrences) {
/* 274 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final int remove(Object element, int occurrences) {
/* 287 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final int setCount(E element, int count) {
/* 300 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean setCount(E element, int oldCount, int newCount) {
/* 313 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   int copyIntoArray(Object[] dst, int offset) {
/* 319 */     for (UnmodifiableIterator<Multiset.Entry<E>> unmodifiableIterator = entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Multiset.Entry<E> entry = unmodifiableIterator.next();
/* 320 */       Arrays.fill(dst, offset, offset + entry.getCount(), entry.getElement());
/* 321 */       offset += entry.getCount(); }
/*     */     
/* 323 */     return offset;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 328 */     return Multisets.equalsImpl(this, object);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 333 */     return Sets.hashCodeImpl(entrySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 338 */     return entrySet().toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<Multiset.Entry<E>> entrySet() {
/* 349 */     ImmutableSet<Multiset.Entry<E>> es = this.entrySet;
/* 350 */     return (es == null) ? (this.entrySet = createEntrySet()) : es;
/*     */   }
/*     */   
/*     */   private ImmutableSet<Multiset.Entry<E>> createEntrySet() {
/* 354 */     return isEmpty() ? ImmutableSet.<Multiset.Entry<E>>of() : new EntrySet();
/*     */   }
/*     */   
/*     */   private final class EntrySet extends IndexedImmutableSet<Multiset.Entry<E>> {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private EntrySet() {}
/*     */     
/*     */     boolean isPartialView() {
/* 363 */       return ImmutableMultiset.this.isPartialView();
/*     */     }
/*     */ 
/*     */     
/*     */     Multiset.Entry<E> get(int index) {
/* 368 */       return ImmutableMultiset.this.getEntry(index);
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 373 */       return ImmutableMultiset.this.elementSet().size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object o) {
/* 378 */       if (o instanceof Multiset.Entry) {
/* 379 */         Multiset.Entry<?> entry = (Multiset.Entry)o;
/* 380 */         if (entry.getCount() <= 0) {
/* 381 */           return false;
/*     */         }
/* 383 */         int count = ImmutableMultiset.this.count(entry.getElement());
/* 384 */         return (count == entry.getCount());
/*     */       } 
/* 386 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 391 */       return ImmutableMultiset.this.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/* 397 */       return new ImmutableMultiset.EntrySetSerializedForm(ImmutableMultiset.this);
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   static class EntrySetSerializedForm<E>
/*     */     implements Serializable
/*     */   {
/*     */     final ImmutableMultiset<E> multiset;
/*     */     
/*     */     EntrySetSerializedForm(ImmutableMultiset<E> multiset) {
/* 408 */       this.multiset = multiset;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 412 */       return this.multiset.entrySet();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 419 */     return new SerializedForm(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Builder<E> builder() {
/* 427 */     return new Builder<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract ImmutableSet<E> elementSet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract Multiset.Entry<E> getEntry(int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<E>
/*     */     extends ImmutableCollection.Builder<E>
/*     */   {
/*     */     final Multiset<E> contents;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder() {
/* 457 */       this(LinkedHashMultiset.create());
/*     */     }
/*     */     
/*     */     Builder(Multiset<E> contents) {
/* 461 */       this.contents = contents;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> add(E element) {
/* 474 */       this.contents.add((E)Preconditions.checkNotNull(element));
/* 475 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> add(E... elements) {
/* 488 */       super.add(elements);
/* 489 */       return this;
/*     */     }
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addCopies(E element, int occurrences) {
/* 505 */       this.contents.add((E)Preconditions.checkNotNull(element), occurrences);
/* 506 */       return this;
/*     */     }
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> setCount(E element, int count) {
/* 521 */       this.contents.setCount((E)Preconditions.checkNotNull(element), count);
/* 522 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterable<? extends E> elements) {
/* 535 */       if (elements instanceof Multiset) {
/* 536 */         Multiset<? extends E> multiset = Multisets.cast(elements);
/* 537 */         multiset.forEachEntry((e, n) -> this.contents.add((E)Preconditions.checkNotNull(e), n));
/*     */       } else {
/* 539 */         super.addAll(elements);
/*     */       } 
/* 541 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterator<? extends E> elements) {
/* 554 */       super.addAll(elements);
/* 555 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableMultiset<E> build() {
/* 564 */       return ImmutableMultiset.copyOf(this.contents);
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     ImmutableMultiset<E> buildJdkBacked() {
/* 569 */       if (this.contents.isEmpty()) {
/* 570 */         return ImmutableMultiset.of();
/*     */       }
/* 572 */       return JdkBackedImmutableMultiset.create(this.contents.entrySet());
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ElementSet<E>
/*     */     extends ImmutableSet.Indexed<E> {
/*     */     private final List<Multiset.Entry<E>> entries;
/*     */     private final Multiset<E> delegate;
/*     */     
/*     */     ElementSet(List<Multiset.Entry<E>> entries, Multiset<E> delegate) {
/* 582 */       this.entries = entries;
/* 583 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     E get(int index) {
/* 588 */       return ((Multiset.Entry<E>)this.entries.get(index)).getElement();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object object) {
/* 593 */       return this.delegate.contains(object);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 598 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 603 */       return this.entries.size();
/*     */     } }
/*     */   
/*     */   static final class SerializedForm implements Serializable {
/*     */     final Object[] elements;
/*     */     final int[] counts;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(Multiset<?> multiset) {
/* 612 */       int distinct = multiset.entrySet().size();
/* 613 */       this.elements = new Object[distinct];
/* 614 */       this.counts = new int[distinct];
/* 615 */       int i = 0;
/* 616 */       for (Multiset.Entry<?> entry : multiset.entrySet()) {
/* 617 */         this.elements[i] = entry.getElement();
/* 618 */         this.counts[i] = entry.getCount();
/* 619 */         i++;
/*     */       } 
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 624 */       LinkedHashMultiset<Object> multiset = LinkedHashMultiset.create(this.elements.length);
/* 625 */       for (int i = 0; i < this.elements.length; i++) {
/* 626 */         multiset.add(this.elements[i], this.counts[i]);
/*     */       }
/* 628 */       return ImmutableMultiset.copyOf(multiset);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\ImmutableMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */