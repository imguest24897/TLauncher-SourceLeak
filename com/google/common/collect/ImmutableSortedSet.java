/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.SortedSet;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Consumer;
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
/*     */ public abstract class ImmutableSortedSet<E>
/*     */   extends ImmutableSortedSetFauxverideShim<E>
/*     */   implements NavigableSet<E>, SortedIterable<E>
/*     */ {
/*     */   static final int SPLITERATOR_CHARACTERISTICS = 1301;
/*     */   final transient Comparator<? super E> comparator;
/*     */   @LazyInit
/*     */   @GwtIncompatible
/*     */   transient ImmutableSortedSet<E> descendingSet;
/*     */   
/*     */   public static <E> Collector<E, ?, ImmutableSortedSet<E>> toImmutableSortedSet(Comparator<? super E> comparator) {
/*  80 */     return CollectCollectors.toImmutableSortedSet(comparator);
/*     */   }
/*     */   
/*     */   static <E> RegularImmutableSortedSet<E> emptySet(Comparator<? super E> comparator) {
/*  84 */     if (Ordering.<Comparable>natural().equals(comparator)) {
/*  85 */       return (RegularImmutableSortedSet)RegularImmutableSortedSet.NATURAL_EMPTY_SET;
/*     */     }
/*  87 */     return new RegularImmutableSortedSet<>(ImmutableList.of(), comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSortedSet<E> of() {
/*  93 */     return (ImmutableSortedSet)RegularImmutableSortedSet.NATURAL_EMPTY_SET;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E element) {
/*  98 */     return new RegularImmutableSortedSet<>(ImmutableList.of(element), Ordering.natural());
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2) {
/* 110 */     return construct(Ordering.natural(), 2, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2 });
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3) {
/* 122 */     return construct(Ordering.natural(), 3, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3 });
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4) {
/* 134 */     return construct(Ordering.natural(), 4, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4 });
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5) {
/* 147 */     return construct(Ordering.natural(), 5, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4, (Comparable)e5 });
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... remaining) {
/* 161 */     Comparable[] contents = new Comparable[6 + remaining.length];
/* 162 */     contents[0] = (Comparable)e1;
/* 163 */     contents[1] = (Comparable)e2;
/* 164 */     contents[2] = (Comparable)e3;
/* 165 */     contents[3] = (Comparable)e4;
/* 166 */     contents[4] = (Comparable)e5;
/* 167 */     contents[5] = (Comparable)e6;
/* 168 */     System.arraycopy(remaining, 0, contents, 6, remaining.length);
/* 169 */     return construct(Ordering.natural(), contents.length, (E[])contents);
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> copyOf(E[] elements) {
/* 183 */     return construct(Ordering.natural(), elements.length, (E[])elements.clone());
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Iterable<? extends E> elements) {
/* 211 */     Ordering<E> naturalOrder = Ordering.natural();
/* 212 */     return copyOf(naturalOrder, elements);
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Collection<? extends E> elements) {
/* 243 */     Ordering<E> naturalOrder = Ordering.natural();
/* 244 */     return copyOf(naturalOrder, elements);
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
/*     */   
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Iterator<? extends E> elements) {
/* 262 */     Ordering<E> naturalOrder = Ordering.natural();
/* 263 */     return copyOf(naturalOrder, elements);
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterator<? extends E> elements) {
/* 275 */     return (new Builder<>(comparator)).addAll(elements).build();
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterable<? extends E> elements) {
/* 291 */     Preconditions.checkNotNull(comparator);
/* 292 */     boolean hasSameComparator = SortedIterables.hasSameComparator(comparator, elements);
/*     */     
/* 294 */     if (hasSameComparator && elements instanceof ImmutableSortedSet) {
/*     */       
/* 296 */       ImmutableSortedSet<E> original = (ImmutableSortedSet)elements;
/* 297 */       if (!original.isPartialView()) {
/* 298 */         return original;
/*     */       }
/*     */     } 
/*     */     
/* 302 */     E[] array = (E[])Iterables.toArray(elements);
/* 303 */     return construct(comparator, array.length, array);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Collection<? extends E> elements) {
/* 323 */     return copyOf(comparator, elements);
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
/*     */   
/*     */   public static <E> ImmutableSortedSet<E> copyOfSorted(SortedSet<E> sortedSet) {
/* 341 */     Comparator<? super E> comparator = SortedIterables.comparator(sortedSet);
/* 342 */     ImmutableList<E> list = ImmutableList.copyOf(sortedSet);
/* 343 */     if (list.isEmpty()) {
/* 344 */       return emptySet(comparator);
/*     */     }
/* 346 */     return new RegularImmutableSortedSet<>(list, comparator);
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
/*     */   static <E> ImmutableSortedSet<E> construct(Comparator<? super E> comparator, int n, E... contents) {
/* 363 */     if (n == 0) {
/* 364 */       return emptySet(comparator);
/*     */     }
/* 366 */     ObjectArrays.checkElementsNotNull((Object[])contents, n);
/* 367 */     Arrays.sort(contents, 0, n, comparator);
/* 368 */     int uniques = 1;
/* 369 */     for (int i = 1; i < n; i++) {
/* 370 */       E cur = contents[i];
/* 371 */       E prev = contents[uniques - 1];
/* 372 */       if (comparator.compare(cur, prev) != 0) {
/* 373 */         contents[uniques++] = cur;
/*     */       }
/*     */     } 
/* 376 */     Arrays.fill((Object[])contents, uniques, n, (Object)null);
/* 377 */     return new RegularImmutableSortedSet<>(
/* 378 */         ImmutableList.asImmutableList((Object[])contents, uniques), comparator);
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
/*     */   public static <E> Builder<E> orderedBy(Comparator<E> comparator) {
/* 390 */     return new Builder<>(comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<?>> Builder<E> reverseOrder() {
/* 398 */     return new Builder<>(Collections.reverseOrder());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<?>> Builder<E> naturalOrder() {
/* 408 */     return new Builder<>(Ordering.natural());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Builder<E>
/*     */     extends ImmutableSet.Builder<E>
/*     */   {
/*     */     private final Comparator<? super E> comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private E[] elements;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int n;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder(Comparator<? super E> comparator) {
/* 438 */       super(true);
/* 439 */       this.comparator = (Comparator<? super E>)Preconditions.checkNotNull(comparator);
/* 440 */       this.elements = (E[])new Object[4];
/* 441 */       this.n = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     void copy() {
/* 446 */       this.elements = Arrays.copyOf(this.elements, this.elements.length);
/*     */     }
/*     */     
/*     */     private void sortAndDedup() {
/* 450 */       if (this.n == 0) {
/*     */         return;
/*     */       }
/* 453 */       Arrays.sort(this.elements, 0, this.n, this.comparator);
/* 454 */       int unique = 1;
/* 455 */       for (int i = 1; i < this.n; i++) {
/* 456 */         int cmp = this.comparator.compare(this.elements[unique - 1], this.elements[i]);
/* 457 */         if (cmp < 0) {
/* 458 */           this.elements[unique++] = this.elements[i];
/* 459 */         } else if (cmp > 0) {
/* 460 */           String str = String.valueOf(this.comparator); throw new AssertionError((new StringBuilder(48 + String.valueOf(str).length())).append("Comparator ").append(str).append(" compare method violates its contract").toString());
/*     */         } 
/*     */       } 
/*     */       
/* 464 */       Arrays.fill((Object[])this.elements, unique, this.n, (Object)null);
/* 465 */       this.n = unique;
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
/*     */     public Builder<E> add(E element) {
/* 480 */       Preconditions.checkNotNull(element);
/* 481 */       copyIfNecessary();
/* 482 */       if (this.n == this.elements.length) {
/* 483 */         sortAndDedup();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 489 */         int newLength = ImmutableCollection.Builder.expandedCapacity(this.n, this.n + 1);
/* 490 */         if (newLength > this.elements.length) {
/* 491 */           this.elements = Arrays.copyOf(this.elements, newLength);
/*     */         }
/*     */       } 
/* 494 */       this.elements[this.n++] = element;
/* 495 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> add(E... elements) {
/* 509 */       ObjectArrays.checkElementsNotNull((Object[])elements);
/* 510 */       for (E e : elements) {
/* 511 */         add(e);
/*     */       }
/* 513 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterable<? extends E> elements) {
/* 527 */       super.addAll(elements);
/* 528 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterator<? extends E> elements) {
/* 542 */       super.addAll(elements);
/* 543 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<E> combine(ImmutableSet.Builder<E> builder) {
/* 549 */       copyIfNecessary();
/* 550 */       Builder<E> other = (Builder<E>)builder;
/* 551 */       for (int i = 0; i < other.n; i++) {
/* 552 */         add(other.elements[i]);
/*     */       }
/* 554 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableSortedSet<E> build() {
/* 563 */       sortAndDedup();
/* 564 */       if (this.n == 0) {
/* 565 */         return ImmutableSortedSet.emptySet(this.comparator);
/*     */       }
/* 567 */       this.forceCopy = true;
/* 568 */       return new RegularImmutableSortedSet<>(
/* 569 */           ImmutableList.asImmutableList((Object[])this.elements, this.n), this.comparator);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   int unsafeCompare(Object a, Object b) {
/* 575 */     return unsafeCompare(this.comparator, a, b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int unsafeCompare(Comparator<?> comparator, Object a, Object b) {
/* 583 */     Comparator<Object> unsafeComparator = (Comparator)comparator;
/* 584 */     return unsafeComparator.compare(a, b);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableSortedSet(Comparator<? super E> comparator) {
/* 590 */     this.comparator = comparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<? super E> comparator() {
/* 600 */     return this.comparator;
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
/*     */   
/*     */   public ImmutableSortedSet<E> headSet(E toElement) {
/* 618 */     return headSet(toElement, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSortedSet<E> headSet(E toElement, boolean inclusive) {
/* 624 */     return headSetImpl((E)Preconditions.checkNotNull(toElement), inclusive);
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
/*     */   public ImmutableSortedSet<E> subSet(E fromElement, E toElement) {
/* 641 */     return subSet(fromElement, true, toElement, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public ImmutableSortedSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 649 */     Preconditions.checkNotNull(fromElement);
/* 650 */     Preconditions.checkNotNull(toElement);
/* 651 */     Preconditions.checkArgument((this.comparator.compare(fromElement, toElement) <= 0));
/* 652 */     return subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
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
/*     */   public ImmutableSortedSet<E> tailSet(E fromElement) {
/* 667 */     return tailSet(fromElement, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSortedSet<E> tailSet(E fromElement, boolean inclusive) {
/* 673 */     return tailSetImpl((E)Preconditions.checkNotNull(fromElement), inclusive);
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
/*     */   @GwtIncompatible
/*     */   public E lower(E e) {
/* 691 */     return Iterators.getNext(headSet(e, false).descendingIterator(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E floor(E e) {
/* 697 */     return Iterators.getNext(headSet(e, true).descendingIterator(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E ceiling(E e) {
/* 703 */     return Iterables.getFirst(tailSet(e, true), null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public E higher(E e) {
/* 710 */     return Iterables.getFirst(tailSet(e, false), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public E first() {
/* 715 */     return iterator().next();
/*     */   }
/*     */ 
/*     */   
/*     */   public E last() {
/* 720 */     return descendingIterator().next();
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public final E pollFirst() {
/* 735 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public final E pollLast() {
/* 750 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public ImmutableSortedSet<E> descendingSet() {
/* 762 */     ImmutableSortedSet<E> result = this.descendingSet;
/* 763 */     if (result == null) {
/* 764 */       result = this.descendingSet = createDescendingSet();
/* 765 */       result.descendingSet = this;
/*     */     } 
/* 767 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Spliterator<E> spliterator() {
/* 778 */     return new Spliterators.AbstractSpliterator<E>(
/* 779 */         size(), 1365) {
/* 780 */         final UnmodifiableIterator<E> iterator = ImmutableSortedSet.this.iterator();
/*     */ 
/*     */         
/*     */         public boolean tryAdvance(Consumer<? super E> action) {
/* 784 */           if (this.iterator.hasNext()) {
/* 785 */             action.accept(this.iterator.next());
/* 786 */             return true;
/*     */           } 
/* 788 */           return false;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public Comparator<? super E> getComparator() {
/* 794 */           return ImmutableSortedSet.this.comparator;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SerializedForm<E>
/*     */     implements Serializable
/*     */   {
/*     */     final Comparator<? super E> comparator;
/*     */ 
/*     */ 
/*     */     
/*     */     final Object[] elements;
/*     */ 
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */     
/*     */     public SerializedForm(Comparator<? super E> comparator, Object[] elements) {
/* 818 */       this.comparator = comparator;
/* 819 */       this.elements = elements;
/*     */     }
/*     */ 
/*     */     
/*     */     Object readResolve() {
/* 824 */       return (new ImmutableSortedSet.Builder((Comparator)this.comparator)).add(this.elements).build();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream unused) throws InvalidObjectException {
/* 831 */     throw new InvalidObjectException("Use SerializedForm");
/*     */   }
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 836 */     return new SerializedForm<>(this.comparator, toArray());
/*     */   }
/*     */   
/*     */   public abstract UnmodifiableIterator<E> iterator();
/*     */   
/*     */   abstract ImmutableSortedSet<E> headSetImpl(E paramE, boolean paramBoolean);
/*     */   
/*     */   abstract ImmutableSortedSet<E> subSetImpl(E paramE1, boolean paramBoolean1, E paramE2, boolean paramBoolean2);
/*     */   
/*     */   abstract ImmutableSortedSet<E> tailSetImpl(E paramE, boolean paramBoolean);
/*     */   
/*     */   @GwtIncompatible
/*     */   abstract ImmutableSortedSet<E> createDescendingSet();
/*     */   
/*     */   @GwtIncompatible
/*     */   public abstract UnmodifiableIterator<E> descendingIterator();
/*     */   
/*     */   abstract int indexOf(Object paramObject);
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\ImmutableSortedSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */