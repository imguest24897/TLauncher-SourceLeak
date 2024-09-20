/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.math.IntMath;
/*      */ import com.google.common.primitives.Ints;
/*      */ import java.io.Serializable;
/*      */ import java.math.RoundingMode;
/*      */ import java.util.AbstractList;
/*      */ import java.util.AbstractSequentialList;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.RandomAccess;
/*      */ import java.util.concurrent.CopyOnWriteArrayList;
/*      */ import java.util.function.Predicate;
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
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Lists
/*      */ {
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayList() {
/*   83 */     return new ArrayList<>();
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
/*      */   @SafeVarargs
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayList(E... elements) {
/*  103 */     Preconditions.checkNotNull(elements);
/*      */     
/*  105 */     int capacity = computeArrayListCapacity(elements.length);
/*  106 */     ArrayList<E> list = new ArrayList<>(capacity);
/*  107 */     Collections.addAll(list, elements);
/*  108 */     return list;
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) {
/*  126 */     Preconditions.checkNotNull(elements);
/*      */     
/*  128 */     return (elements instanceof Collection) ? 
/*  129 */       new ArrayList<>((Collection<? extends E>)elements) : 
/*  130 */       newArrayList(elements.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements) {
/*  142 */     ArrayList<E> list = newArrayList();
/*  143 */     Iterators.addAll(list, elements);
/*  144 */     return list;
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   static int computeArrayListCapacity(int arraySize) {
/*  149 */     CollectPreconditions.checkNonnegative(arraySize, "arraySize");
/*      */ 
/*      */     
/*  152 */     return Ints.saturatedCast(5L + arraySize + (arraySize / 10));
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayListWithCapacity(int initialArraySize) {
/*  173 */     CollectPreconditions.checkNonnegative(initialArraySize, "initialArraySize");
/*  174 */     return new ArrayList<>(initialArraySize);
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayListWithExpectedSize(int estimatedSize) {
/*  192 */     return new ArrayList<>(computeArrayListCapacity(estimatedSize));
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> LinkedList<E> newLinkedList() {
/*  214 */     return new LinkedList<>();
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> LinkedList<E> newLinkedList(Iterable<? extends E> elements) {
/*  236 */     LinkedList<E> list = newLinkedList();
/*  237 */     Iterables.addAll(list, elements);
/*  238 */     return list;
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
/*      */   @GwtIncompatible
/*      */   public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList() {
/*  252 */     return new CopyOnWriteArrayList<>();
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
/*      */   @GwtIncompatible
/*      */   public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(Iterable<? extends E> elements) {
/*  270 */     Collection<? extends E> elementsCollection = (elements instanceof Collection) ? (Collection<? extends E>)elements : newArrayList(elements);
/*  271 */     return new CopyOnWriteArrayList<>(elementsCollection);
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
/*      */   public static <E> List<E> asList(E first, E[] rest) {
/*  289 */     return new OnePlusArrayList<>(first, rest);
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
/*      */   public static <E> List<E> asList(E first, E second, E[] rest) {
/*  309 */     return new TwoPlusArrayList<>(first, second, rest);
/*      */   }
/*      */   
/*      */   private static class OnePlusArrayList<E>
/*      */     extends AbstractList<E> implements Serializable, RandomAccess {
/*      */     final E first;
/*      */     final E[] rest;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     OnePlusArrayList(E first, E[] rest) {
/*  319 */       this.first = first;
/*  320 */       this.rest = (E[])Preconditions.checkNotNull(rest);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  325 */       return IntMath.saturatedAdd(this.rest.length, 1);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public E get(int index) {
/*  331 */       Preconditions.checkElementIndex(index, size());
/*  332 */       return (index == 0) ? this.first : this.rest[index - 1];
/*      */     }
/*      */   }
/*      */   
/*      */   private static class TwoPlusArrayList<E>
/*      */     extends AbstractList<E>
/*      */     implements Serializable, RandomAccess
/*      */   {
/*      */     final E first;
/*      */     final E second;
/*      */     final E[] rest;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     TwoPlusArrayList(E first, E second, E[] rest) {
/*  346 */       this.first = first;
/*  347 */       this.second = second;
/*  348 */       this.rest = (E[])Preconditions.checkNotNull(rest);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  353 */       return IntMath.saturatedAdd(this.rest.length, 2);
/*      */     }
/*      */ 
/*      */     
/*      */     public E get(int index) {
/*  358 */       switch (index) {
/*      */         case 0:
/*  360 */           return this.first;
/*      */         case 1:
/*  362 */           return this.second;
/*      */       } 
/*      */       
/*  365 */       Preconditions.checkElementIndex(index, size());
/*  366 */       return this.rest[index - 2];
/*      */     }
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
/*      */   public static <B> List<List<B>> cartesianProduct(List<? extends List<? extends B>> lists) {
/*  428 */     return CartesianList.create(lists);
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
/*      */   @SafeVarargs
/*      */   public static <B> List<List<B>> cartesianProduct(List<? extends B>... lists) {
/*  487 */     return cartesianProduct(Arrays.asList(lists));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <F, T> List<T> transform(List<F> fromList, Function<? super F, ? extends T> function) {
/*  524 */     return (fromList instanceof RandomAccess) ? 
/*  525 */       new TransformingRandomAccessList<>(fromList, function) : 
/*  526 */       new TransformingSequentialList<>(fromList, function);
/*      */   }
/*      */ 
/*      */   
/*      */   private static class TransformingSequentialList<F, T>
/*      */     extends AbstractSequentialList<T>
/*      */     implements Serializable
/*      */   {
/*      */     final List<F> fromList;
/*      */     
/*      */     final Function<? super F, ? extends T> function;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     TransformingSequentialList(List<F> fromList, Function<? super F, ? extends T> function) {
/*  540 */       this.fromList = (List<F>)Preconditions.checkNotNull(fromList);
/*  541 */       this.function = (Function<? super F, ? extends T>)Preconditions.checkNotNull(function);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/*  550 */       this.fromList.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  555 */       return this.fromList.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<T> listIterator(int index) {
/*  560 */       return new TransformedListIterator<F, T>(this.fromList.listIterator(index))
/*      */         {
/*      */           T transform(F from) {
/*  563 */             return (T)Lists.TransformingSequentialList.this.function.apply(from);
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeIf(Predicate<? super T> filter) {
/*  570 */       Preconditions.checkNotNull(filter);
/*  571 */       return this.fromList.removeIf(element -> filter.test(this.function.apply(element)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TransformingRandomAccessList<F, T>
/*      */     extends AbstractList<T>
/*      */     implements RandomAccess, Serializable
/*      */   {
/*      */     final List<F> fromList;
/*      */ 
/*      */     
/*      */     final Function<? super F, ? extends T> function;
/*      */     
/*      */     private static final long serialVersionUID = 0L;
/*      */ 
/*      */     
/*      */     TransformingRandomAccessList(List<F> fromList, Function<? super F, ? extends T> function) {
/*  590 */       this.fromList = (List<F>)Preconditions.checkNotNull(fromList);
/*  591 */       this.function = (Function<? super F, ? extends T>)Preconditions.checkNotNull(function);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  596 */       this.fromList.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public T get(int index) {
/*  601 */       return (T)this.function.apply(this.fromList.get(index));
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<T> iterator() {
/*  606 */       return listIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<T> listIterator(int index) {
/*  611 */       return new TransformedListIterator<F, T>(this.fromList.listIterator(index))
/*      */         {
/*      */           T transform(F from) {
/*  614 */             return (T)Lists.TransformingRandomAccessList.this.function.apply(from);
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  621 */       return this.fromList.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeIf(Predicate<? super T> filter) {
/*  626 */       Preconditions.checkNotNull(filter);
/*  627 */       return this.fromList.removeIf(element -> filter.test(this.function.apply(element)));
/*      */     }
/*      */ 
/*      */     
/*      */     public T remove(int index) {
/*  632 */       return (T)this.function.apply(this.fromList.remove(index));
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  637 */       return this.fromList.size();
/*      */     }
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
/*      */   public static <T> List<List<T>> partition(List<T> list, int size) {
/*  659 */     Preconditions.checkNotNull(list);
/*  660 */     Preconditions.checkArgument((size > 0));
/*  661 */     return (list instanceof RandomAccess) ? 
/*  662 */       new RandomAccessPartition<>(list, size) : 
/*  663 */       new Partition<>(list, size);
/*      */   }
/*      */   
/*      */   private static class Partition<T> extends AbstractList<List<T>> {
/*      */     final List<T> list;
/*      */     final int size;
/*      */     
/*      */     Partition(List<T> list, int size) {
/*  671 */       this.list = list;
/*  672 */       this.size = size;
/*      */     }
/*      */ 
/*      */     
/*      */     public List<T> get(int index) {
/*  677 */       Preconditions.checkElementIndex(index, size());
/*  678 */       int start = index * this.size;
/*  679 */       int end = Math.min(start + this.size, this.list.size());
/*  680 */       return this.list.subList(start, end);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  685 */       return IntMath.divide(this.list.size(), this.size, RoundingMode.CEILING);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  690 */       return this.list.isEmpty();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class RandomAccessPartition<T> extends Partition<T> implements RandomAccess {
/*      */     RandomAccessPartition(List<T> list, int size) {
/*  696 */       super(list, size);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ImmutableList<Character> charactersOf(String string) {
/*  706 */     return new StringAsImmutableList((String)Preconditions.checkNotNull(string));
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
/*      */   @Beta
/*      */   public static List<Character> charactersOf(CharSequence sequence) {
/*  720 */     return new CharSequenceAsList((CharSequence)Preconditions.checkNotNull(sequence));
/*      */   }
/*      */   
/*      */   private static final class StringAsImmutableList
/*      */     extends ImmutableList<Character>
/*      */   {
/*      */     private final String string;
/*      */     
/*      */     StringAsImmutableList(String string) {
/*  729 */       this.string = string;
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexOf(Object object) {
/*  734 */       return (object instanceof Character) ? this.string.indexOf(((Character)object).charValue()) : -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public int lastIndexOf(Object object) {
/*  739 */       return (object instanceof Character) ? this.string.lastIndexOf(((Character)object).charValue()) : -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public ImmutableList<Character> subList(int fromIndex, int toIndex) {
/*  744 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
/*  745 */       return Lists.charactersOf(this.string.substring(fromIndex, toIndex));
/*      */     }
/*      */ 
/*      */     
/*      */     boolean isPartialView() {
/*  750 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public Character get(int index) {
/*  755 */       Preconditions.checkElementIndex(index, size());
/*  756 */       return Character.valueOf(this.string.charAt(index));
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  761 */       return this.string.length();
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class CharSequenceAsList extends AbstractList<Character> {
/*      */     private final CharSequence sequence;
/*      */     
/*      */     CharSequenceAsList(CharSequence sequence) {
/*  769 */       this.sequence = sequence;
/*      */     }
/*      */ 
/*      */     
/*      */     public Character get(int index) {
/*  774 */       Preconditions.checkElementIndex(index, size());
/*  775 */       return Character.valueOf(this.sequence.charAt(index));
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  780 */       return this.sequence.length();
/*      */     }
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
/*      */   public static <T> List<T> reverse(List<T> list) {
/*  796 */     if (list instanceof ImmutableList)
/*  797 */       return ((ImmutableList<T>)list).reverse(); 
/*  798 */     if (list instanceof ReverseList)
/*  799 */       return ((ReverseList<T>)list).getForwardList(); 
/*  800 */     if (list instanceof RandomAccess) {
/*  801 */       return new RandomAccessReverseList<>(list);
/*      */     }
/*  803 */     return new ReverseList<>(list);
/*      */   }
/*      */   
/*      */   private static class ReverseList<T>
/*      */     extends AbstractList<T> {
/*      */     private final List<T> forwardList;
/*      */     
/*      */     ReverseList(List<T> forwardList) {
/*  811 */       this.forwardList = (List<T>)Preconditions.checkNotNull(forwardList);
/*      */     }
/*      */     
/*      */     List<T> getForwardList() {
/*  815 */       return this.forwardList;
/*      */     }
/*      */     
/*      */     private int reverseIndex(int index) {
/*  819 */       int size = size();
/*  820 */       Preconditions.checkElementIndex(index, size);
/*  821 */       return size - 1 - index;
/*      */     }
/*      */     
/*      */     private int reversePosition(int index) {
/*  825 */       int size = size();
/*  826 */       Preconditions.checkPositionIndex(index, size);
/*  827 */       return size - index;
/*      */     }
/*      */ 
/*      */     
/*      */     public void add(int index, T element) {
/*  832 */       this.forwardList.add(reversePosition(index), element);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  837 */       this.forwardList.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public T remove(int index) {
/*  842 */       return this.forwardList.remove(reverseIndex(index));
/*      */     }
/*      */ 
/*      */     
/*      */     protected void removeRange(int fromIndex, int toIndex) {
/*  847 */       subList(fromIndex, toIndex).clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public T set(int index, T element) {
/*  852 */       return this.forwardList.set(reverseIndex(index), element);
/*      */     }
/*      */ 
/*      */     
/*      */     public T get(int index) {
/*  857 */       return this.forwardList.get(reverseIndex(index));
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  862 */       return this.forwardList.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public List<T> subList(int fromIndex, int toIndex) {
/*  867 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
/*  868 */       return Lists.reverse(this.forwardList.subList(reversePosition(toIndex), reversePosition(fromIndex)));
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<T> iterator() {
/*  873 */       return listIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<T> listIterator(int index) {
/*  878 */       int start = reversePosition(index);
/*  879 */       final ListIterator<T> forwardIterator = this.forwardList.listIterator(start);
/*  880 */       return new ListIterator<T>()
/*      */         {
/*      */           boolean canRemoveOrSet;
/*      */ 
/*      */           
/*      */           public void add(T e) {
/*  886 */             forwardIterator.add(e);
/*  887 */             forwardIterator.previous();
/*  888 */             this.canRemoveOrSet = false;
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean hasNext() {
/*  893 */             return forwardIterator.hasPrevious();
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean hasPrevious() {
/*  898 */             return forwardIterator.hasNext();
/*      */           }
/*      */ 
/*      */           
/*      */           public T next() {
/*  903 */             if (!hasNext()) {
/*  904 */               throw new NoSuchElementException();
/*      */             }
/*  906 */             this.canRemoveOrSet = true;
/*  907 */             return forwardIterator.previous();
/*      */           }
/*      */ 
/*      */           
/*      */           public int nextIndex() {
/*  912 */             return Lists.ReverseList.this.reversePosition(forwardIterator.nextIndex());
/*      */           }
/*      */ 
/*      */           
/*      */           public T previous() {
/*  917 */             if (!hasPrevious()) {
/*  918 */               throw new NoSuchElementException();
/*      */             }
/*  920 */             this.canRemoveOrSet = true;
/*  921 */             return forwardIterator.next();
/*      */           }
/*      */ 
/*      */           
/*      */           public int previousIndex() {
/*  926 */             return nextIndex() - 1;
/*      */           }
/*      */ 
/*      */           
/*      */           public void remove() {
/*  931 */             CollectPreconditions.checkRemove(this.canRemoveOrSet);
/*  932 */             forwardIterator.remove();
/*  933 */             this.canRemoveOrSet = false;
/*      */           }
/*      */ 
/*      */           
/*      */           public void set(T e) {
/*  938 */             Preconditions.checkState(this.canRemoveOrSet);
/*  939 */             forwardIterator.set(e);
/*      */           }
/*      */         };
/*      */     }
/*      */   }
/*      */   
/*      */   private static class RandomAccessReverseList<T> extends ReverseList<T> implements RandomAccess {
/*      */     RandomAccessReverseList(List<T> forwardList) {
/*  947 */       super(forwardList);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static int hashCodeImpl(List<?> list) {
/*  954 */     int hashCode = 1;
/*  955 */     for (Object o : list) {
/*  956 */       hashCode = 31 * hashCode + ((o == null) ? 0 : o.hashCode());
/*      */       
/*  958 */       hashCode = hashCode ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*      */     } 
/*      */     
/*  961 */     return hashCode;
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean equalsImpl(List<?> thisList, Object other) {
/*  966 */     if (other == Preconditions.checkNotNull(thisList)) {
/*  967 */       return true;
/*      */     }
/*  969 */     if (!(other instanceof List)) {
/*  970 */       return false;
/*      */     }
/*  972 */     List<?> otherList = (List)other;
/*  973 */     int size = thisList.size();
/*  974 */     if (size != otherList.size()) {
/*  975 */       return false;
/*      */     }
/*  977 */     if (thisList instanceof RandomAccess && otherList instanceof RandomAccess) {
/*      */       
/*  979 */       for (int i = 0; i < size; i++) {
/*  980 */         if (!Objects.equal(thisList.get(i), otherList.get(i))) {
/*  981 */           return false;
/*      */         }
/*      */       } 
/*  984 */       return true;
/*      */     } 
/*  986 */     return Iterators.elementsEqual(thisList.iterator(), otherList.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> boolean addAllImpl(List<E> list, int index, Iterable<? extends E> elements) {
/*  992 */     boolean changed = false;
/*  993 */     ListIterator<E> listIterator = list.listIterator(index);
/*  994 */     for (E e : elements) {
/*  995 */       listIterator.add(e);
/*  996 */       changed = true;
/*      */     } 
/*  998 */     return changed;
/*      */   }
/*      */ 
/*      */   
/*      */   static int indexOfImpl(List<?> list, Object element) {
/* 1003 */     if (list instanceof RandomAccess) {
/* 1004 */       return indexOfRandomAccess(list, element);
/*      */     }
/* 1006 */     ListIterator<?> listIterator = list.listIterator();
/* 1007 */     while (listIterator.hasNext()) {
/* 1008 */       if (Objects.equal(element, listIterator.next())) {
/* 1009 */         return listIterator.previousIndex();
/*      */       }
/*      */     } 
/* 1012 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int indexOfRandomAccess(List<?> list, Object element) {
/* 1017 */     int size = list.size();
/* 1018 */     if (element == null) {
/* 1019 */       for (int i = 0; i < size; i++) {
/* 1020 */         if (list.get(i) == null) {
/* 1021 */           return i;
/*      */         }
/*      */       } 
/*      */     } else {
/* 1025 */       for (int i = 0; i < size; i++) {
/* 1026 */         if (element.equals(list.get(i))) {
/* 1027 */           return i;
/*      */         }
/*      */       } 
/*      */     } 
/* 1031 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   static int lastIndexOfImpl(List<?> list, Object element) {
/* 1036 */     if (list instanceof RandomAccess) {
/* 1037 */       return lastIndexOfRandomAccess(list, element);
/*      */     }
/* 1039 */     ListIterator<?> listIterator = list.listIterator(list.size());
/* 1040 */     while (listIterator.hasPrevious()) {
/* 1041 */       if (Objects.equal(element, listIterator.previous())) {
/* 1042 */         return listIterator.nextIndex();
/*      */       }
/*      */     } 
/* 1045 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int lastIndexOfRandomAccess(List<?> list, Object element) {
/* 1050 */     if (element == null) {
/* 1051 */       for (int i = list.size() - 1; i >= 0; i--) {
/* 1052 */         if (list.get(i) == null) {
/* 1053 */           return i;
/*      */         }
/*      */       } 
/*      */     } else {
/* 1057 */       for (int i = list.size() - 1; i >= 0; i--) {
/* 1058 */         if (element.equals(list.get(i))) {
/* 1059 */           return i;
/*      */         }
/*      */       } 
/*      */     } 
/* 1063 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   static <E> ListIterator<E> listIteratorImpl(List<E> list, int index) {
/* 1068 */     return (new AbstractListWrapper<>(list)).listIterator(index);
/*      */   }
/*      */ 
/*      */   
/*      */   static <E> List<E> subListImpl(List<E> list, int fromIndex, int toIndex) {
/*      */     List<E> wrapper;
/* 1074 */     if (list instanceof RandomAccess) {
/* 1075 */       wrapper = new RandomAccessListWrapper<E>(list) {
/*      */           private static final long serialVersionUID = 0L;
/*      */           
/*      */           public ListIterator<E> listIterator(int index) {
/* 1079 */             return this.backingList.listIterator(index);
/*      */           }
/*      */         };
/*      */     }
/*      */     else {
/*      */       
/* 1085 */       wrapper = new AbstractListWrapper<E>(list) {
/*      */           private static final long serialVersionUID = 0L;
/*      */           
/*      */           public ListIterator<E> listIterator(int index) {
/* 1089 */             return this.backingList.listIterator(index);
/*      */           }
/*      */         };
/*      */     } 
/*      */ 
/*      */     
/* 1095 */     return wrapper.subList(fromIndex, toIndex);
/*      */   }
/*      */   
/*      */   private static class AbstractListWrapper<E> extends AbstractList<E> {
/*      */     final List<E> backingList;
/*      */     
/*      */     AbstractListWrapper(List<E> backingList) {
/* 1102 */       this.backingList = (List<E>)Preconditions.checkNotNull(backingList);
/*      */     }
/*      */ 
/*      */     
/*      */     public void add(int index, E element) {
/* 1107 */       this.backingList.add(index, element);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addAll(int index, Collection<? extends E> c) {
/* 1112 */       return this.backingList.addAll(index, c);
/*      */     }
/*      */ 
/*      */     
/*      */     public E get(int index) {
/* 1117 */       return this.backingList.get(index);
/*      */     }
/*      */ 
/*      */     
/*      */     public E remove(int index) {
/* 1122 */       return this.backingList.remove(index);
/*      */     }
/*      */ 
/*      */     
/*      */     public E set(int index, E element) {
/* 1127 */       return this.backingList.set(index, element);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1132 */       return this.backingList.contains(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1137 */       return this.backingList.size();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class RandomAccessListWrapper<E>
/*      */     extends AbstractListWrapper<E> implements RandomAccess {
/*      */     RandomAccessListWrapper(List<E> backingList) {
/* 1144 */       super(backingList);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static <T> List<T> cast(Iterable<T> iterable) {
/* 1150 */     return (List<T>)iterable;
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\Lists.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */