/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.common.math.IntMath;
/*      */ import com.google.common.primitives.Ints;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.io.Serializable;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Iterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.Spliterator;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.function.ToIntFunction;
/*      */ import java.util.stream.Collector;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible
/*      */ public final class Multisets
/*      */ {
/*      */   public static <T, E, M extends Multiset<E>> Collector<T, ?, M> toMultiset(Function<? super T, E> elementFunction, ToIntFunction<? super T> countFunction, Supplier<M> multisetSupplier) {
/*   80 */     return CollectCollectors.toMultiset(elementFunction, countFunction, multisetSupplier);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> Multiset<E> unmodifiableMultiset(Multiset<? extends E> multiset) {
/*   94 */     if (multiset instanceof UnmodifiableMultiset || multiset instanceof ImmutableMultiset)
/*      */     {
/*   96 */       return (Multiset)multiset;
/*      */     }
/*      */     
/*   99 */     return new UnmodifiableMultiset<>((Multiset<? extends E>)Preconditions.checkNotNull(multiset));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <E> Multiset<E> unmodifiableMultiset(ImmutableMultiset<E> multiset) {
/*  110 */     return (Multiset<E>)Preconditions.checkNotNull(multiset);
/*      */   }
/*      */   
/*      */   static class UnmodifiableMultiset<E> extends ForwardingMultiset<E> implements Serializable { final Multiset<? extends E> delegate;
/*      */     transient Set<E> elementSet;
/*      */     
/*      */     UnmodifiableMultiset(Multiset<? extends E> delegate) {
/*  117 */       this.delegate = delegate;
/*      */     }
/*      */     
/*      */     transient Set<Multiset.Entry<E>> entrySet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     protected Multiset<E> delegate() {
/*  124 */       return (Multiset)this.delegate;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Set<E> createElementSet() {
/*  130 */       return Collections.unmodifiableSet(this.delegate.elementSet());
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<E> elementSet() {
/*  135 */       Set<E> es = this.elementSet;
/*  136 */       return (es == null) ? (this.elementSet = createElementSet()) : es;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Multiset.Entry<E>> entrySet() {
/*  144 */       Set<Multiset.Entry<E>> es = this.entrySet;
/*  145 */       return (es == null) ? (
/*      */ 
/*      */         
/*  148 */         this.entrySet = Collections.unmodifiableSet(this.delegate.entrySet())) : 
/*  149 */         es;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/*  154 */       return Iterators.unmodifiableIterator(this.delegate.iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean add(E element) {
/*  159 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public int add(E element, int occurences) {
/*  164 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addAll(Collection<? extends E> elementsToAdd) {
/*  169 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object element) {
/*  174 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public int remove(Object element, int occurrences) {
/*  179 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> elementsToRemove) {
/*  184 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> elementsToRetain) {
/*  189 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  194 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public int setCount(E element, int count) {
/*  199 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean setCount(E element, int oldCount, int newCount) {
/*  204 */       throw new UnsupportedOperationException();
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <E> SortedMultiset<E> unmodifiableSortedMultiset(SortedMultiset<E> sortedMultiset) {
/*  224 */     return new UnmodifiableSortedMultiset<>((SortedMultiset<E>)Preconditions.checkNotNull(sortedMultiset));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> Multiset.Entry<E> immutableEntry(E e, int n) {
/*  236 */     return new ImmutableEntry<>(e, n);
/*      */   }
/*      */   
/*      */   static class ImmutableEntry<E> extends AbstractEntry<E> implements Serializable { private final E element;
/*      */     private final int count;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     ImmutableEntry(E element, int count) {
/*  244 */       this.element = element;
/*  245 */       this.count = count;
/*  246 */       CollectPreconditions.checkNonnegative(count, "count");
/*      */     }
/*      */ 
/*      */     
/*      */     public final E getElement() {
/*  251 */       return this.element;
/*      */     }
/*      */ 
/*      */     
/*      */     public final int getCount() {
/*  256 */       return this.count;
/*      */     }
/*      */     
/*      */     public ImmutableEntry<E> nextInBucket() {
/*  260 */       return null;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <E> Multiset<E> filter(Multiset<E> unfiltered, Predicate<? super E> predicate) {
/*  293 */     if (unfiltered instanceof FilteredMultiset) {
/*      */ 
/*      */       
/*  296 */       FilteredMultiset<E> filtered = (FilteredMultiset<E>)unfiltered;
/*  297 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/*  298 */       return new FilteredMultiset<>(filtered.unfiltered, combinedPredicate);
/*      */     } 
/*  300 */     return new FilteredMultiset<>(unfiltered, predicate);
/*      */   }
/*      */   
/*      */   private static final class FilteredMultiset<E> extends ViewMultiset<E> {
/*      */     final Multiset<E> unfiltered;
/*      */     final Predicate<? super E> predicate;
/*      */     
/*      */     FilteredMultiset(Multiset<E> unfiltered, Predicate<? super E> predicate) {
/*  308 */       this.unfiltered = (Multiset<E>)Preconditions.checkNotNull(unfiltered);
/*  309 */       this.predicate = (Predicate<? super E>)Preconditions.checkNotNull(predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public UnmodifiableIterator<E> iterator() {
/*  314 */       return Iterators.filter(this.unfiltered.iterator(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     Set<E> createElementSet() {
/*  319 */       return Sets.filter(this.unfiltered.elementSet(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<E> elementIterator() {
/*  324 */       throw new AssertionError("should never be called");
/*      */     }
/*      */ 
/*      */     
/*      */     Set<Multiset.Entry<E>> createEntrySet() {
/*  329 */       return Sets.filter(this.unfiltered
/*  330 */           .entrySet(), new Predicate<Multiset.Entry<E>>()
/*      */           {
/*      */             public boolean apply(Multiset.Entry<E> entry)
/*      */             {
/*  334 */               return Multisets.FilteredMultiset.this.predicate.apply(entry.getElement());
/*      */             }
/*      */           });
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Multiset.Entry<E>> entryIterator() {
/*  341 */       throw new AssertionError("should never be called");
/*      */     }
/*      */ 
/*      */     
/*      */     public int count(Object element) {
/*  346 */       int count = this.unfiltered.count(element);
/*  347 */       if (count > 0) {
/*      */         
/*  349 */         E e = (E)element;
/*  350 */         return this.predicate.apply(e) ? count : 0;
/*      */       } 
/*  352 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public int add(E element, int occurrences) {
/*  357 */       Preconditions.checkArgument(this.predicate
/*  358 */           .apply(element), "Element %s does not match predicate %s", element, this.predicate);
/*  359 */       return this.unfiltered.add(element, occurrences);
/*      */     }
/*      */ 
/*      */     
/*      */     public int remove(Object element, int occurrences) {
/*  364 */       CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/*  365 */       if (occurrences == 0) {
/*  366 */         return count(element);
/*      */       }
/*  368 */       return contains(element) ? this.unfiltered.remove(element, occurrences) : 0;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int inferDistinctElements(Iterable<?> elements) {
/*  379 */     if (elements instanceof Multiset) {
/*  380 */       return ((Multiset)elements).elementSet().size();
/*      */     }
/*  382 */     return 11;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <E> Multiset<E> union(final Multiset<? extends E> multiset1, final Multiset<? extends E> multiset2) {
/*  400 */     Preconditions.checkNotNull(multiset1);
/*  401 */     Preconditions.checkNotNull(multiset2);
/*      */     
/*  403 */     return new ViewMultiset<E>()
/*      */       {
/*      */         public boolean contains(Object element) {
/*  406 */           return (multiset1.contains(element) || multiset2.contains(element));
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/*  411 */           return (multiset1.isEmpty() && multiset2.isEmpty());
/*      */         }
/*      */ 
/*      */         
/*      */         public int count(Object element) {
/*  416 */           return Math.max(multiset1.count(element), multiset2.count(element));
/*      */         }
/*      */ 
/*      */         
/*      */         Set<E> createElementSet() {
/*  421 */           return Sets.union(multiset1.elementSet(), multiset2.elementSet());
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<E> elementIterator() {
/*  426 */           throw new AssertionError("should never be called");
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<Multiset.Entry<E>> entryIterator() {
/*  431 */           final Iterator<? extends Multiset.Entry<? extends E>> iterator1 = multiset1.entrySet().iterator();
/*  432 */           final Iterator<? extends Multiset.Entry<? extends E>> iterator2 = multiset2.entrySet().iterator();
/*      */           
/*  434 */           return new AbstractIterator()
/*      */             {
/*      */               protected Multiset.Entry<E> computeNext() {
/*  437 */                 if (iterator1.hasNext()) {
/*  438 */                   Multiset.Entry<? extends E> entry1 = iterator1.next();
/*  439 */                   E element = entry1.getElement();
/*  440 */                   int count = Math.max(entry1.getCount(), multiset2.count(element));
/*  441 */                   return Multisets.immutableEntry(element, count);
/*      */                 } 
/*  443 */                 while (iterator2.hasNext()) {
/*  444 */                   Multiset.Entry<? extends E> entry2 = iterator2.next();
/*  445 */                   E element = entry2.getElement();
/*  446 */                   if (!multiset1.contains(element)) {
/*  447 */                     return Multisets.immutableEntry(element, entry2.getCount());
/*      */                   }
/*      */                 } 
/*  450 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> Multiset<E> intersection(final Multiset<E> multiset1, final Multiset<?> multiset2) {
/*  471 */     Preconditions.checkNotNull(multiset1);
/*  472 */     Preconditions.checkNotNull(multiset2);
/*      */     
/*  474 */     return new ViewMultiset<E>()
/*      */       {
/*      */         public int count(Object element) {
/*  477 */           int count1 = multiset1.count(element);
/*  478 */           return (count1 == 0) ? 0 : Math.min(count1, multiset2.count(element));
/*      */         }
/*      */ 
/*      */         
/*      */         Set<E> createElementSet() {
/*  483 */           return Sets.intersection(multiset1.elementSet(), multiset2.elementSet());
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<E> elementIterator() {
/*  488 */           throw new AssertionError("should never be called");
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<Multiset.Entry<E>> entryIterator() {
/*  493 */           final Iterator<Multiset.Entry<E>> iterator1 = multiset1.entrySet().iterator();
/*      */           
/*  495 */           return new AbstractIterator()
/*      */             {
/*      */               protected Multiset.Entry<E> computeNext() {
/*  498 */                 while (iterator1.hasNext()) {
/*  499 */                   Multiset.Entry<E> entry1 = iterator1.next();
/*  500 */                   E element = entry1.getElement();
/*  501 */                   int count = Math.min(entry1.getCount(), multiset2.count(element));
/*  502 */                   if (count > 0) {
/*  503 */                     return Multisets.immutableEntry(element, count);
/*      */                   }
/*      */                 } 
/*  506 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <E> Multiset<E> sum(final Multiset<? extends E> multiset1, final Multiset<? extends E> multiset2) {
/*  528 */     Preconditions.checkNotNull(multiset1);
/*  529 */     Preconditions.checkNotNull(multiset2);
/*      */ 
/*      */     
/*  532 */     return new ViewMultiset<E>()
/*      */       {
/*      */         public boolean contains(Object element) {
/*  535 */           return (multiset1.contains(element) || multiset2.contains(element));
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/*  540 */           return (multiset1.isEmpty() && multiset2.isEmpty());
/*      */         }
/*      */ 
/*      */         
/*      */         public int size() {
/*  545 */           return IntMath.saturatedAdd(multiset1.size(), multiset2.size());
/*      */         }
/*      */ 
/*      */         
/*      */         public int count(Object element) {
/*  550 */           return multiset1.count(element) + multiset2.count(element);
/*      */         }
/*      */ 
/*      */         
/*      */         Set<E> createElementSet() {
/*  555 */           return Sets.union(multiset1.elementSet(), multiset2.elementSet());
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<E> elementIterator() {
/*  560 */           throw new AssertionError("should never be called");
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<Multiset.Entry<E>> entryIterator() {
/*  565 */           final Iterator<? extends Multiset.Entry<? extends E>> iterator1 = multiset1.entrySet().iterator();
/*  566 */           final Iterator<? extends Multiset.Entry<? extends E>> iterator2 = multiset2.entrySet().iterator();
/*  567 */           return new AbstractIterator()
/*      */             {
/*      */               protected Multiset.Entry<E> computeNext() {
/*  570 */                 if (iterator1.hasNext()) {
/*  571 */                   Multiset.Entry<? extends E> entry1 = iterator1.next();
/*  572 */                   E element = entry1.getElement();
/*  573 */                   int count = entry1.getCount() + multiset2.count(element);
/*  574 */                   return Multisets.immutableEntry(element, count);
/*      */                 } 
/*  576 */                 while (iterator2.hasNext()) {
/*  577 */                   Multiset.Entry<? extends E> entry2 = iterator2.next();
/*  578 */                   E element = entry2.getElement();
/*  579 */                   if (!multiset1.contains(element)) {
/*  580 */                     return Multisets.immutableEntry(element, entry2.getCount());
/*      */                   }
/*      */                 } 
/*  583 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <E> Multiset<E> difference(final Multiset<E> multiset1, final Multiset<?> multiset2) {
/*  605 */     Preconditions.checkNotNull(multiset1);
/*  606 */     Preconditions.checkNotNull(multiset2);
/*      */ 
/*      */     
/*  609 */     return new ViewMultiset<E>()
/*      */       {
/*      */         public int count(Object element) {
/*  612 */           int count1 = multiset1.count(element);
/*  613 */           return (count1 == 0) ? 0 : Math.max(0, count1 - multiset2.count(element));
/*      */         }
/*      */ 
/*      */         
/*      */         public void clear() {
/*  618 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<E> elementIterator() {
/*  623 */           final Iterator<Multiset.Entry<E>> iterator1 = multiset1.entrySet().iterator();
/*  624 */           return new AbstractIterator()
/*      */             {
/*      */               protected E computeNext() {
/*  627 */                 while (iterator1.hasNext()) {
/*  628 */                   Multiset.Entry<E> entry1 = iterator1.next();
/*  629 */                   E element = entry1.getElement();
/*  630 */                   if (entry1.getCount() > multiset2.count(element)) {
/*  631 */                     return element;
/*      */                   }
/*      */                 } 
/*  634 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<Multiset.Entry<E>> entryIterator() {
/*  641 */           final Iterator<Multiset.Entry<E>> iterator1 = multiset1.entrySet().iterator();
/*  642 */           return new AbstractIterator()
/*      */             {
/*      */               protected Multiset.Entry<E> computeNext() {
/*  645 */                 while (iterator1.hasNext()) {
/*  646 */                   Multiset.Entry<E> entry1 = iterator1.next();
/*  647 */                   E element = entry1.getElement();
/*  648 */                   int count = entry1.getCount() - multiset2.count(element);
/*  649 */                   if (count > 0) {
/*  650 */                     return Multisets.immutableEntry(element, count);
/*      */                   }
/*      */                 } 
/*  653 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         int distinctElements() {
/*  660 */           return Iterators.size(entryIterator());
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean containsOccurrences(Multiset<?> superMultiset, Multiset<?> subMultiset) {
/*  673 */     Preconditions.checkNotNull(superMultiset);
/*  674 */     Preconditions.checkNotNull(subMultiset);
/*  675 */     for (Multiset.Entry<?> entry : subMultiset.entrySet()) {
/*  676 */       int superCount = superMultiset.count(entry.getElement());
/*  677 */       if (superCount < entry.getCount()) {
/*  678 */         return false;
/*      */       }
/*      */     } 
/*  681 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean retainOccurrences(Multiset<?> multisetToModify, Multiset<?> multisetToRetain) {
/*  703 */     return retainOccurrencesImpl(multisetToModify, multisetToRetain);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> boolean retainOccurrencesImpl(Multiset<E> multisetToModify, Multiset<?> occurrencesToRetain) {
/*  709 */     Preconditions.checkNotNull(multisetToModify);
/*  710 */     Preconditions.checkNotNull(occurrencesToRetain);
/*      */     
/*  712 */     Iterator<Multiset.Entry<E>> entryIterator = multisetToModify.entrySet().iterator();
/*  713 */     boolean changed = false;
/*  714 */     while (entryIterator.hasNext()) {
/*  715 */       Multiset.Entry<E> entry = entryIterator.next();
/*  716 */       int retainCount = occurrencesToRetain.count(entry.getElement());
/*  717 */       if (retainCount == 0) {
/*  718 */         entryIterator.remove();
/*  719 */         changed = true; continue;
/*  720 */       }  if (retainCount < entry.getCount()) {
/*  721 */         multisetToModify.setCount(entry.getElement(), retainCount);
/*  722 */         changed = true;
/*      */       } 
/*      */     } 
/*  725 */     return changed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean removeOccurrences(Multiset<?> multisetToModify, Iterable<?> occurrencesToRemove) {
/*  754 */     if (occurrencesToRemove instanceof Multiset) {
/*  755 */       return removeOccurrences(multisetToModify, (Multiset)occurrencesToRemove);
/*      */     }
/*  757 */     Preconditions.checkNotNull(multisetToModify);
/*  758 */     Preconditions.checkNotNull(occurrencesToRemove);
/*  759 */     boolean changed = false;
/*  760 */     for (Object o : occurrencesToRemove) {
/*  761 */       changed |= multisetToModify.remove(o);
/*      */     }
/*  763 */     return changed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean removeOccurrences(Multiset<?> multisetToModify, Multiset<?> occurrencesToRemove) {
/*  792 */     Preconditions.checkNotNull(multisetToModify);
/*  793 */     Preconditions.checkNotNull(occurrencesToRemove);
/*      */     
/*  795 */     boolean changed = false;
/*  796 */     Iterator<? extends Multiset.Entry<?>> entryIterator = multisetToModify.entrySet().iterator();
/*  797 */     while (entryIterator.hasNext()) {
/*  798 */       Multiset.Entry<?> entry = entryIterator.next();
/*  799 */       int removeCount = occurrencesToRemove.count(entry.getElement());
/*  800 */       if (removeCount >= entry.getCount()) {
/*  801 */         entryIterator.remove();
/*  802 */         changed = true; continue;
/*  803 */       }  if (removeCount > 0) {
/*  804 */         multisetToModify.remove(entry.getElement(), removeCount);
/*  805 */         changed = true;
/*      */       } 
/*      */     } 
/*  808 */     return changed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class AbstractEntry<E>
/*      */     implements Multiset.Entry<E>
/*      */   {
/*      */     public boolean equals(Object object) {
/*  822 */       if (object instanceof Multiset.Entry) {
/*  823 */         Multiset.Entry<?> that = (Multiset.Entry)object;
/*  824 */         return (getCount() == that.getCount() && 
/*  825 */           Objects.equal(getElement(), that.getElement()));
/*      */       } 
/*  827 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  836 */       E e = getElement();
/*  837 */       return ((e == null) ? 0 : e.hashCode()) ^ getCount();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  848 */       String text = String.valueOf(getElement());
/*  849 */       int n = getCount();
/*  850 */       return (n == 1) ? text : (new StringBuilder(14 + String.valueOf(text).length())).append(text).append(" x ").append(n).toString();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean equalsImpl(Multiset<?> multiset, Object object) {
/*  856 */     if (object == multiset) {
/*  857 */       return true;
/*      */     }
/*  859 */     if (object instanceof Multiset) {
/*  860 */       Multiset<?> that = (Multiset)object;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  867 */       if (multiset.size() != that.size() || multiset.entrySet().size() != that.entrySet().size()) {
/*  868 */         return false;
/*      */       }
/*  870 */       for (Multiset.Entry<?> entry : that.entrySet()) {
/*  871 */         if (multiset.count(entry.getElement()) != entry.getCount()) {
/*  872 */           return false;
/*      */         }
/*      */       } 
/*  875 */       return true;
/*      */     } 
/*  877 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   static <E> boolean addAllImpl(Multiset<E> self, Collection<? extends E> elements) {
/*  882 */     Preconditions.checkNotNull(self);
/*  883 */     Preconditions.checkNotNull(elements);
/*  884 */     if (elements instanceof Multiset)
/*  885 */       return addAllImpl(self, cast(elements)); 
/*  886 */     if (elements.isEmpty()) {
/*  887 */       return false;
/*      */     }
/*  889 */     return Iterators.addAll(self, elements.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> boolean addAllImpl(Multiset<E> self, Multiset<? extends E> elements) {
/*  895 */     if (elements.isEmpty()) {
/*  896 */       return false;
/*      */     }
/*  898 */     Objects.requireNonNull(self); elements.forEachEntry(self::add);
/*  899 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean removeAllImpl(Multiset<?> self, Collection<?> elementsToRemove) {
/*  907 */     Collection<?> collection = (elementsToRemove instanceof Multiset) ? ((Multiset)elementsToRemove).elementSet() : elementsToRemove;
/*      */     
/*  909 */     return self.elementSet().removeAll(collection);
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean retainAllImpl(Multiset<?> self, Collection<?> elementsToRetain) {
/*  914 */     Preconditions.checkNotNull(elementsToRetain);
/*      */ 
/*      */ 
/*      */     
/*  918 */     Collection<?> collection = (elementsToRetain instanceof Multiset) ? ((Multiset)elementsToRetain).elementSet() : elementsToRetain;
/*      */     
/*  920 */     return self.elementSet().retainAll(collection);
/*      */   }
/*      */ 
/*      */   
/*      */   static <E> int setCountImpl(Multiset<E> self, E element, int count) {
/*  925 */     CollectPreconditions.checkNonnegative(count, "count");
/*      */     
/*  927 */     int oldCount = self.count(element);
/*      */     
/*  929 */     int delta = count - oldCount;
/*  930 */     if (delta > 0) {
/*  931 */       self.add(element, delta);
/*  932 */     } else if (delta < 0) {
/*  933 */       self.remove(element, -delta);
/*      */     } 
/*      */     
/*  936 */     return oldCount;
/*      */   }
/*      */ 
/*      */   
/*      */   static <E> boolean setCountImpl(Multiset<E> self, E element, int oldCount, int newCount) {
/*  941 */     CollectPreconditions.checkNonnegative(oldCount, "oldCount");
/*  942 */     CollectPreconditions.checkNonnegative(newCount, "newCount");
/*      */     
/*  944 */     if (self.count(element) == oldCount) {
/*  945 */       self.setCount(element, newCount);
/*  946 */       return true;
/*      */     } 
/*  948 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   static <E> Iterator<E> elementIterator(Iterator<Multiset.Entry<E>> entryIterator) {
/*  953 */     return new TransformedIterator<Multiset.Entry<E>, E>(entryIterator)
/*      */       {
/*      */         E transform(Multiset.Entry<E> entry) {
/*  956 */           return entry.getElement();
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   static abstract class ElementSet<E>
/*      */     extends Sets.ImprovedAbstractSet<E> {
/*      */     abstract Multiset<E> multiset();
/*      */     
/*      */     public void clear() {
/*  966 */       multiset().clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  971 */       return multiset().contains(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  976 */       return multiset().containsAll(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  981 */       return multiset().isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public abstract Iterator<E> iterator();
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/*  989 */       return (multiset().remove(o, 2147483647) > 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  994 */       return multiset().entrySet().size();
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class EntrySet<E>
/*      */     extends Sets.ImprovedAbstractSet<Multiset.Entry<E>> {
/*      */     abstract Multiset<E> multiset();
/*      */     
/*      */     public boolean contains(Object o) {
/* 1003 */       if (o instanceof Multiset.Entry) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1008 */         Multiset.Entry<?> entry = (Multiset.Entry)o;
/* 1009 */         if (entry.getCount() <= 0) {
/* 1010 */           return false;
/*      */         }
/* 1012 */         int count = multiset().count(entry.getElement());
/* 1013 */         return (count == entry.getCount());
/*      */       } 
/* 1015 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean remove(Object object) {
/* 1022 */       if (object instanceof Multiset.Entry) {
/* 1023 */         Multiset.Entry<?> entry = (Multiset.Entry)object;
/* 1024 */         Object element = entry.getElement();
/* 1025 */         int entryCount = entry.getCount();
/* 1026 */         if (entryCount != 0) {
/*      */ 
/*      */           
/* 1029 */           Multiset<Object> multiset = (Multiset)multiset();
/* 1030 */           return multiset.setCount(element, entryCount, 0);
/*      */         } 
/*      */       } 
/* 1033 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1038 */       multiset().clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static <E> Iterator<E> iteratorImpl(Multiset<E> multiset) {
/* 1044 */     return new MultisetIteratorImpl<>(multiset, multiset.entrySet().iterator());
/*      */   }
/*      */ 
/*      */   
/*      */   static final class MultisetIteratorImpl<E>
/*      */     implements Iterator<E>
/*      */   {
/*      */     private final Multiset<E> multiset;
/*      */     
/*      */     private final Iterator<Multiset.Entry<E>> entryIterator;
/*      */     
/*      */     private Multiset.Entry<E> currentEntry;
/*      */     private int laterCount;
/*      */     private int totalCount;
/*      */     private boolean canRemove;
/*      */     
/*      */     MultisetIteratorImpl(Multiset<E> multiset, Iterator<Multiset.Entry<E>> entryIterator) {
/* 1061 */       this.multiset = multiset;
/* 1062 */       this.entryIterator = entryIterator;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1067 */       return (this.laterCount > 0 || this.entryIterator.hasNext());
/*      */     }
/*      */ 
/*      */     
/*      */     public E next() {
/* 1072 */       if (!hasNext()) {
/* 1073 */         throw new NoSuchElementException();
/*      */       }
/* 1075 */       if (this.laterCount == 0) {
/* 1076 */         this.currentEntry = this.entryIterator.next();
/* 1077 */         this.totalCount = this.laterCount = this.currentEntry.getCount();
/*      */       } 
/* 1079 */       this.laterCount--;
/* 1080 */       this.canRemove = true;
/* 1081 */       return this.currentEntry.getElement();
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1086 */       CollectPreconditions.checkRemove(this.canRemove);
/* 1087 */       if (this.totalCount == 1) {
/* 1088 */         this.entryIterator.remove();
/*      */       } else {
/* 1090 */         this.multiset.remove(this.currentEntry.getElement());
/*      */       } 
/* 1092 */       this.totalCount--;
/* 1093 */       this.canRemove = false;
/*      */     }
/*      */   }
/*      */   
/*      */   static <E> Spliterator<E> spliteratorImpl(Multiset<E> multiset) {
/* 1098 */     Spliterator<Multiset.Entry<E>> entrySpliterator = multiset.entrySet().spliterator();
/* 1099 */     return CollectSpliterators.flatMap(entrySpliterator, entry -> Collections.nCopies(entry.getCount(), entry.getElement()).spliterator(), 0x40 | entrySpliterator
/*      */ 
/*      */ 
/*      */         
/* 1103 */         .characteristics() & 0x510, multiset
/*      */         
/* 1105 */         .size());
/*      */   }
/*      */ 
/*      */   
/*      */   static int linearTimeSizeImpl(Multiset<?> multiset) {
/* 1110 */     long size = 0L;
/* 1111 */     for (Multiset.Entry<?> entry : multiset.entrySet()) {
/* 1112 */       size += entry.getCount();
/*      */     }
/* 1114 */     return Ints.saturatedCast(size);
/*      */   }
/*      */ 
/*      */   
/*      */   static <T> Multiset<T> cast(Iterable<T> iterable) {
/* 1119 */     return (Multiset<T>)iterable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <E> ImmutableMultiset<E> copyHighestCountFirst(Multiset<E> multiset) {
/* 1130 */     Multiset.Entry[] arrayOfEntry = (Multiset.Entry[])multiset.entrySet().toArray((Object[])new Multiset.Entry[0]);
/* 1131 */     Arrays.sort(arrayOfEntry, DecreasingCount.INSTANCE);
/* 1132 */     return ImmutableMultiset.copyFromEntries(Arrays.asList((Multiset.Entry<? extends E>[])arrayOfEntry));
/*      */   }
/*      */   
/*      */   private static final class DecreasingCount implements Comparator<Multiset.Entry<?>> {
/* 1136 */     static final DecreasingCount INSTANCE = new DecreasingCount();
/*      */ 
/*      */     
/*      */     public int compare(Multiset.Entry<?> entry1, Multiset.Entry<?> entry2) {
/* 1140 */       return entry2.getCount() - entry1.getCount();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static abstract class ViewMultiset<E>
/*      */     extends AbstractMultiset<E>
/*      */   {
/*      */     private ViewMultiset() {}
/*      */     
/*      */     public int size() {
/* 1151 */       return Multisets.linearTimeSizeImpl(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1156 */       elementSet().clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/* 1161 */       return Multisets.iteratorImpl(this);
/*      */     }
/*      */ 
/*      */     
/*      */     int distinctElements() {
/* 1166 */       return elementSet().size();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\Multisets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */