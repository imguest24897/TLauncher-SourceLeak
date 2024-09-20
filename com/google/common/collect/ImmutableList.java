/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.RandomAccess;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.UnaryOperator;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableList<E>
/*     */   extends ImmutableCollection<E>
/*     */   implements List<E>, RandomAccess
/*     */ {
/*     */   public static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
/*  72 */     return CollectCollectors.toImmutableList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of() {
/*  83 */     return (ImmutableList)RegularImmutableList.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E element) {
/*  94 */     return new SingletonImmutableList<>(element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2) {
/* 103 */     return construct(new Object[] { e1, e2 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3) {
/* 112 */     return construct(new Object[] { e1, e2, e3 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4) {
/* 121 */     return construct(new Object[] { e1, e2, e3, e4 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5) {
/* 130 */     return construct(new Object[] { e1, e2, e3, e4, e5 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
/* 139 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
/* 148 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
/* 157 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
/* 166 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
/* 176 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9, e10 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11) {
/* 186 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11 });
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
/*     */   @SafeVarargs
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11, E e12, E... others) {
/* 203 */     Preconditions.checkArgument((others.length <= 2147483635), "the total number of elements must fit in an int");
/*     */     
/* 205 */     Object[] array = new Object[12 + others.length];
/* 206 */     array[0] = e1;
/* 207 */     array[1] = e2;
/* 208 */     array[2] = e3;
/* 209 */     array[3] = e4;
/* 210 */     array[4] = e5;
/* 211 */     array[5] = e6;
/* 212 */     array[6] = e7;
/* 213 */     array[7] = e8;
/* 214 */     array[8] = e9;
/* 215 */     array[9] = e10;
/* 216 */     array[10] = e11;
/* 217 */     array[11] = e12;
/* 218 */     System.arraycopy(others, 0, array, 12, others.length);
/* 219 */     return construct(array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> copyOf(Iterable<? extends E> elements) {
/* 230 */     Preconditions.checkNotNull(elements);
/* 231 */     return (elements instanceof Collection) ? 
/* 232 */       copyOf((Collection<? extends E>)elements) : 
/* 233 */       copyOf(elements.iterator());
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
/*     */   public static <E> ImmutableList<E> copyOf(Collection<? extends E> elements) {
/* 254 */     if (elements instanceof ImmutableCollection) {
/*     */       
/* 256 */       ImmutableList<E> list = ((ImmutableCollection)elements).asList();
/* 257 */       return list.isPartialView() ? asImmutableList(list.toArray()) : list;
/*     */     } 
/* 259 */     return construct(elements.toArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> copyOf(Iterator<? extends E> elements) {
/* 269 */     if (!elements.hasNext()) {
/* 270 */       return of();
/*     */     }
/* 272 */     E first = elements.next();
/* 273 */     if (!elements.hasNext()) {
/* 274 */       return of(first);
/*     */     }
/* 276 */     return (new Builder<>()).add(first).addAll(elements).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> copyOf(E[] elements) {
/* 287 */     switch (elements.length) {
/*     */       case 0:
/* 289 */         return of();
/*     */       case 1:
/* 291 */         return of(elements[0]);
/*     */     } 
/* 293 */     return construct((Object[])elements.clone());
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
/*     */   public static <E extends Comparable<? super E>> ImmutableList<E> sortedCopyOf(Iterable<? extends E> elements) {
/* 314 */     Comparable[] arrayOfComparable = Iterables.<Comparable>toArray(elements, new Comparable[0]);
/* 315 */     ObjectArrays.checkElementsNotNull((Object[])arrayOfComparable);
/* 316 */     Arrays.sort((Object[])arrayOfComparable);
/* 317 */     return asImmutableList((Object[])arrayOfComparable);
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
/*     */   public static <E> ImmutableList<E> sortedCopyOf(Comparator<? super E> comparator, Iterable<? extends E> elements) {
/* 337 */     Preconditions.checkNotNull(comparator);
/*     */     
/* 339 */     E[] array = (E[])Iterables.toArray(elements);
/* 340 */     ObjectArrays.checkElementsNotNull((Object[])array);
/* 341 */     Arrays.sort(array, comparator);
/* 342 */     return asImmutableList((Object[])array);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <E> ImmutableList<E> construct(Object... elements) {
/* 347 */     return asImmutableList(ObjectArrays.checkElementsNotNull(elements));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> ImmutableList<E> asImmutableList(Object[] elements) {
/* 356 */     return asImmutableList(elements, elements.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> ImmutableList<E> asImmutableList(Object[] elements, int length) {
/* 364 */     switch (length) {
/*     */       case 0:
/* 366 */         return of();
/*     */       case 1:
/* 368 */         return of((E)elements[0]);
/*     */     } 
/* 370 */     if (length < elements.length) {
/* 371 */       elements = Arrays.copyOf(elements, length);
/*     */     }
/* 373 */     return new RegularImmutableList<>(elements);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/* 383 */     return listIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableListIterator<E> listIterator() {
/* 388 */     return listIterator(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableListIterator<E> listIterator(int index) {
/* 393 */     return new AbstractIndexedListIterator<E>(size(), index)
/*     */       {
/*     */         protected E get(int index) {
/* 396 */           return ImmutableList.this.get(index);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(Consumer<? super E> consumer) {
/* 403 */     Preconditions.checkNotNull(consumer);
/* 404 */     int n = size();
/* 405 */     for (int i = 0; i < n; i++) {
/* 406 */       consumer.accept(get(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(Object object) {
/* 412 */     return (object == null) ? -1 : Lists.indexOfImpl(this, object);
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(Object object) {
/* 417 */     return (object == null) ? -1 : Lists.lastIndexOfImpl(this, object);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/* 422 */     return (indexOf(object) >= 0);
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
/*     */   public ImmutableList<E> subList(int fromIndex, int toIndex) {
/* 434 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
/* 435 */     int length = toIndex - fromIndex;
/* 436 */     if (length == size())
/* 437 */       return this; 
/* 438 */     if (length == 0)
/* 439 */       return of(); 
/* 440 */     if (length == 1) {
/* 441 */       return of(get(fromIndex));
/*     */     }
/* 443 */     return subListUnchecked(fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableList<E> subListUnchecked(int fromIndex, int toIndex) {
/* 452 */     return new SubList(fromIndex, toIndex - fromIndex);
/*     */   }
/*     */   
/*     */   class SubList extends ImmutableList<E> {
/*     */     final transient int offset;
/*     */     final transient int length;
/*     */     
/*     */     SubList(int offset, int length) {
/* 460 */       this.offset = offset;
/* 461 */       this.length = length;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 466 */       return this.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public E get(int index) {
/* 471 */       Preconditions.checkElementIndex(index, this.length);
/* 472 */       return ImmutableList.this.get(index + this.offset);
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableList<E> subList(int fromIndex, int toIndex) {
/* 477 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, this.length);
/* 478 */       return ImmutableList.this.subList(fromIndex + this.offset, toIndex + this.offset);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 483 */       return true;
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean addAll(int index, Collection<? extends E> newElements) {
/* 497 */     throw new UnsupportedOperationException();
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
/*     */   public final E set(int index, E element) {
/* 510 */     throw new UnsupportedOperationException();
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
/*     */   public final void add(int index, E element) {
/* 522 */     throw new UnsupportedOperationException();
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
/*     */   public final E remove(int index) {
/* 535 */     throw new UnsupportedOperationException();
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
/*     */   public final void replaceAll(UnaryOperator<E> operator) {
/* 547 */     throw new UnsupportedOperationException();
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
/*     */   public final void sort(Comparator<? super E> c) {
/* 559 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ImmutableList<E> asList() {
/* 569 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<E> spliterator() {
/* 574 */     return CollectSpliterators.indexed(size(), 1296, this::get);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   int copyIntoArray(Object[] dst, int offset) {
/* 580 */     int size = size();
/* 581 */     for (int i = 0; i < size; i++) {
/* 582 */       dst[offset + i] = get(i);
/*     */     }
/* 584 */     return offset + size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableList<E> reverse() {
/* 595 */     return (size() <= 1) ? this : new ReverseImmutableList<>(this);
/*     */   }
/*     */   
/*     */   private static class ReverseImmutableList<E> extends ImmutableList<E> {
/*     */     private final transient ImmutableList<E> forwardList;
/*     */     
/*     */     ReverseImmutableList(ImmutableList<E> backingList) {
/* 602 */       this.forwardList = backingList;
/*     */     }
/*     */     
/*     */     private int reverseIndex(int index) {
/* 606 */       return size() - 1 - index;
/*     */     }
/*     */     
/*     */     private int reversePosition(int index) {
/* 610 */       return size() - index;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableList<E> reverse() {
/* 615 */       return this.forwardList;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object object) {
/* 620 */       return this.forwardList.contains(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int indexOf(Object object) {
/* 625 */       int index = this.forwardList.lastIndexOf(object);
/* 626 */       return (index >= 0) ? reverseIndex(index) : -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object object) {
/* 631 */       int index = this.forwardList.indexOf(object);
/* 632 */       return (index >= 0) ? reverseIndex(index) : -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableList<E> subList(int fromIndex, int toIndex) {
/* 637 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
/* 638 */       return this.forwardList.subList(reversePosition(toIndex), reversePosition(fromIndex)).reverse();
/*     */     }
/*     */ 
/*     */     
/*     */     public E get(int index) {
/* 643 */       Preconditions.checkElementIndex(index, size());
/* 644 */       return this.forwardList.get(reverseIndex(index));
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 649 */       return this.forwardList.size();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 654 */       return this.forwardList.isPartialView();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 660 */     return Lists.equalsImpl(this, obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 665 */     int hashCode = 1;
/* 666 */     int n = size();
/* 667 */     for (int i = 0; i < n; i++) {
/* 668 */       hashCode = 31 * hashCode + get(i).hashCode();
/*     */       
/* 670 */       hashCode = hashCode ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*     */     } 
/*     */     
/* 673 */     return hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   static class SerializedForm
/*     */     implements Serializable
/*     */   {
/*     */     final Object[] elements;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(Object[] elements) {
/* 684 */       this.elements = elements;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 688 */       return ImmutableList.copyOf(this.elements);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 695 */     throw new InvalidObjectException("Use SerializedForm");
/*     */   }
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 700 */     return new SerializedForm(toArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Builder<E> builder() {
/* 708 */     return new Builder<>();
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
/*     */   @Beta
/*     */   public static <E> Builder<E> builderWithExpectedSize(int expectedSize) {
/* 725 */     CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
/* 726 */     return new Builder<>(expectedSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Builder<E>
/*     */     extends ImmutableCollection.Builder<E>
/*     */   {
/*     */     @VisibleForTesting
/*     */     Object[] contents;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int size;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean forceCopy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder() {
/* 759 */       this(4);
/*     */     }
/*     */     
/*     */     Builder(int capacity) {
/* 763 */       this.contents = new Object[capacity];
/* 764 */       this.size = 0;
/*     */     }
/*     */     
/*     */     private void getReadyToExpandTo(int minCapacity) {
/* 768 */       if (this.contents.length < minCapacity) {
/* 769 */         this.contents = Arrays.copyOf(this.contents, expandedCapacity(this.contents.length, minCapacity));
/* 770 */         this.forceCopy = false;
/* 771 */       } else if (this.forceCopy) {
/* 772 */         this.contents = Arrays.copyOf(this.contents, this.contents.length);
/* 773 */         this.forceCopy = false;
/*     */       } 
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
/* 787 */       Preconditions.checkNotNull(element);
/* 788 */       getReadyToExpandTo(this.size + 1);
/* 789 */       this.contents[this.size++] = element;
/* 790 */       return this;
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
/* 803 */       ObjectArrays.checkElementsNotNull((Object[])elements);
/* 804 */       add((Object[])elements, elements.length);
/* 805 */       return this;
/*     */     }
/*     */     
/*     */     private void add(Object[] elements, int n) {
/* 809 */       getReadyToExpandTo(this.size + n);
/* 810 */       System.arraycopy(elements, 0, this.contents, this.size, n);
/* 811 */       this.size += n;
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
/* 824 */       Preconditions.checkNotNull(elements);
/* 825 */       if (elements instanceof Collection) {
/* 826 */         Collection<?> collection = (Collection)elements;
/* 827 */         getReadyToExpandTo(this.size + collection.size());
/* 828 */         if (collection instanceof ImmutableCollection) {
/* 829 */           ImmutableCollection<?> immutableCollection = (ImmutableCollection)collection;
/* 830 */           this.size = immutableCollection.copyIntoArray(this.contents, this.size);
/* 831 */           return this;
/*     */         } 
/*     */       } 
/* 834 */       super.addAll(elements);
/* 835 */       return this;
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
/* 848 */       super.addAll(elements);
/* 849 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<E> combine(Builder<E> builder) {
/* 854 */       Preconditions.checkNotNull(builder);
/* 855 */       add(builder.contents, builder.size);
/* 856 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableList<E> build() {
/* 864 */       this.forceCopy = true;
/* 865 */       return ImmutableList.asImmutableList(this.contents, this.size);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\ImmutableList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */