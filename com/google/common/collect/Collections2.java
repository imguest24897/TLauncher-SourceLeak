/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.math.IntMath;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public final class Collections2
/*     */ {
/*     */   public static <E> Collection<E> filter(Collection<E> unfiltered, Predicate<? super E> predicate) {
/*  87 */     if (unfiltered instanceof FilteredCollection)
/*     */     {
/*     */       
/*  90 */       return ((FilteredCollection<E>)unfiltered).createCombined(predicate);
/*     */     }
/*     */     
/*  93 */     return new FilteredCollection<>((Collection<E>)Preconditions.checkNotNull(unfiltered), (Predicate<? super E>)Preconditions.checkNotNull(predicate));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean safeContains(Collection<?> collection, Object object) {
/* 101 */     Preconditions.checkNotNull(collection);
/*     */     try {
/* 103 */       return collection.contains(object);
/* 104 */     } catch (ClassCastException|NullPointerException e) {
/* 105 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean safeRemove(Collection<?> collection, Object object) {
/* 114 */     Preconditions.checkNotNull(collection);
/*     */     try {
/* 116 */       return collection.remove(object);
/* 117 */     } catch (ClassCastException|NullPointerException e) {
/* 118 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   static class FilteredCollection<E> extends AbstractCollection<E> {
/*     */     final Collection<E> unfiltered;
/*     */     final Predicate<? super E> predicate;
/*     */     
/*     */     FilteredCollection(Collection<E> unfiltered, Predicate<? super E> predicate) {
/* 127 */       this.unfiltered = unfiltered;
/* 128 */       this.predicate = predicate;
/*     */     }
/*     */     
/*     */     FilteredCollection<E> createCombined(Predicate<? super E> newPredicate) {
/* 132 */       return new FilteredCollection(this.unfiltered, Predicates.and(this.predicate, newPredicate));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean add(E element) {
/* 138 */       Preconditions.checkArgument(this.predicate.apply(element));
/* 139 */       return this.unfiltered.add(element);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean addAll(Collection<? extends E> collection) {
/* 144 */       for (E element : collection) {
/* 145 */         Preconditions.checkArgument(this.predicate.apply(element));
/*     */       }
/* 147 */       return this.unfiltered.addAll(collection);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 152 */       Iterables.removeIf(this.unfiltered, this.predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object element) {
/* 157 */       if (Collections2.safeContains(this.unfiltered, element)) {
/*     */         
/* 159 */         E e = (E)element;
/* 160 */         return this.predicate.apply(e);
/*     */       } 
/* 162 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsAll(Collection<?> collection) {
/* 167 */       return Collections2.containsAllImpl(this, collection);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 172 */       return !Iterables.any(this.unfiltered, this.predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<E> iterator() {
/* 177 */       return Iterators.filter(this.unfiltered.iterator(), this.predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<E> spliterator() {
/* 182 */       return CollectSpliterators.filter(this.unfiltered.spliterator(), (Predicate<? super E>)this.predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<? super E> action) {
/* 187 */       Preconditions.checkNotNull(action);
/* 188 */       this.unfiltered.forEach(e -> {
/*     */             if (this.predicate.test(e)) {
/*     */               action.accept(e);
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean remove(Object element) {
/* 198 */       return (contains(element) && this.unfiltered.remove(element));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection<?> collection) {
/* 203 */       Objects.requireNonNull(collection); return removeIf(collection::contains);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection<?> collection) {
/* 208 */       return removeIf(element -> !collection.contains(element));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeIf(Predicate<? super E> filter) {
/* 213 */       Preconditions.checkNotNull(filter);
/* 214 */       return this.unfiltered.removeIf(element -> (this.predicate.apply(element) && filter.test(element)));
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 219 */       int size = 0;
/* 220 */       for (E e : this.unfiltered) {
/* 221 */         if (this.predicate.apply(e)) {
/* 222 */           size++;
/*     */         }
/*     */       } 
/* 225 */       return size;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 231 */       return Lists.<E>newArrayList(iterator()).toArray();
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 236 */       return (T[])Lists.<E>newArrayList(iterator()).toArray((Object[])array);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <F, T> Collection<T> transform(Collection<F> fromCollection, Function<? super F, T> function) {
/* 261 */     return new TransformedCollection<>(fromCollection, function);
/*     */   }
/*     */   
/*     */   static class TransformedCollection<F, T> extends AbstractCollection<T> {
/*     */     final Collection<F> fromCollection;
/*     */     final Function<? super F, ? extends T> function;
/*     */     
/*     */     TransformedCollection(Collection<F> fromCollection, Function<? super F, ? extends T> function) {
/* 269 */       this.fromCollection = (Collection<F>)Preconditions.checkNotNull(fromCollection);
/* 270 */       this.function = (Function<? super F, ? extends T>)Preconditions.checkNotNull(function);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 275 */       this.fromCollection.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 280 */       return this.fromCollection.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<T> iterator() {
/* 285 */       return Iterators.transform(this.fromCollection.iterator(), this.function);
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<T> spliterator() {
/* 290 */       return CollectSpliterators.map(this.fromCollection.spliterator(), (Function<? super F, ? extends T>)this.function);
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<? super T> action) {
/* 295 */       Preconditions.checkNotNull(action);
/* 296 */       this.fromCollection.forEach(f -> action.accept(this.function.apply(f)));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeIf(Predicate<? super T> filter) {
/* 301 */       Preconditions.checkNotNull(filter);
/* 302 */       return this.fromCollection.removeIf(element -> filter.test(this.function.apply(element)));
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 307 */       return this.fromCollection.size();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean containsAllImpl(Collection<?> self, Collection<?> c) {
/* 323 */     for (Object o : c) {
/* 324 */       if (!self.contains(o)) {
/* 325 */         return false;
/*     */       }
/*     */     } 
/* 328 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   static String toStringImpl(Collection<?> collection) {
/* 333 */     StringBuilder sb = newStringBuilderForCollection(collection.size()).append('[');
/* 334 */     boolean first = true;
/* 335 */     for (Object o : collection) {
/* 336 */       if (!first) {
/* 337 */         sb.append(", ");
/*     */       }
/* 339 */       first = false;
/* 340 */       if (o == collection) {
/* 341 */         sb.append("(this Collection)"); continue;
/*     */       } 
/* 343 */       sb.append(o);
/*     */     } 
/*     */     
/* 346 */     return sb.append(']').toString();
/*     */   }
/*     */ 
/*     */   
/*     */   static StringBuilder newStringBuilderForCollection(int size) {
/* 351 */     CollectPreconditions.checkNonnegative(size, "size");
/* 352 */     return new StringBuilder((int)Math.min(size * 8L, 1073741824L));
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
/*     */   @Beta
/*     */   public static <E extends Comparable<? super E>> Collection<List<E>> orderedPermutations(Iterable<E> elements) {
/* 380 */     return orderedPermutations(elements, Ordering.natural());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static <E> Collection<List<E>> orderedPermutations(Iterable<E> elements, Comparator<? super E> comparator) {
/* 432 */     return new OrderedPermutationCollection<>(elements, comparator);
/*     */   }
/*     */   
/*     */   private static final class OrderedPermutationCollection<E> extends AbstractCollection<List<E>> {
/*     */     final ImmutableList<E> inputList;
/*     */     final Comparator<? super E> comparator;
/*     */     final int size;
/*     */     
/*     */     OrderedPermutationCollection(Iterable<E> input, Comparator<? super E> comparator) {
/* 441 */       this.inputList = ImmutableList.sortedCopyOf(comparator, input);
/* 442 */       this.comparator = comparator;
/* 443 */       this.size = calculateSize(this.inputList, comparator);
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
/*     */     private static <E> int calculateSize(List<E> sortedInputList, Comparator<? super E> comparator) {
/* 457 */       int permutations = 1;
/* 458 */       int n = 1;
/* 459 */       int r = 1;
/* 460 */       while (n < sortedInputList.size()) {
/* 461 */         int comparison = comparator.compare(sortedInputList.get(n - 1), sortedInputList.get(n));
/* 462 */         if (comparison < 0) {
/*     */           
/* 464 */           permutations = IntMath.saturatedMultiply(permutations, IntMath.binomial(n, r));
/* 465 */           r = 0;
/* 466 */           if (permutations == Integer.MAX_VALUE) {
/* 467 */             return Integer.MAX_VALUE;
/*     */           }
/*     */         } 
/* 470 */         n++;
/* 471 */         r++;
/*     */       } 
/* 473 */       return IntMath.saturatedMultiply(permutations, IntMath.binomial(n, r));
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 478 */       return this.size;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 483 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<List<E>> iterator() {
/* 488 */       return new Collections2.OrderedPermutationIterator<>(this.inputList, this.comparator);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object obj) {
/* 493 */       if (obj instanceof List) {
/* 494 */         List<?> list = (List)obj;
/* 495 */         return Collections2.isPermutation(this.inputList, list);
/*     */       } 
/* 497 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 502 */       String str = String.valueOf(this.inputList); return (new StringBuilder(30 + String.valueOf(str).length())).append("orderedPermutationCollection(").append(str).append(")").toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class OrderedPermutationIterator<E> extends AbstractIterator<List<E>> {
/*     */     List<E> nextPermutation;
/*     */     final Comparator<? super E> comparator;
/*     */     
/*     */     OrderedPermutationIterator(List<E> list, Comparator<? super E> comparator) {
/* 511 */       this.nextPermutation = Lists.newArrayList(list);
/* 512 */       this.comparator = comparator;
/*     */     }
/*     */ 
/*     */     
/*     */     protected List<E> computeNext() {
/* 517 */       if (this.nextPermutation == null) {
/* 518 */         return endOfData();
/*     */       }
/* 520 */       ImmutableList<E> next = ImmutableList.copyOf(this.nextPermutation);
/* 521 */       calculateNextPermutation();
/* 522 */       return next;
/*     */     }
/*     */     
/*     */     void calculateNextPermutation() {
/* 526 */       int j = findNextJ();
/* 527 */       if (j == -1) {
/* 528 */         this.nextPermutation = null;
/*     */         
/*     */         return;
/*     */       } 
/* 532 */       int l = findNextL(j);
/* 533 */       Collections.swap(this.nextPermutation, j, l);
/* 534 */       int n = this.nextPermutation.size();
/* 535 */       Collections.reverse(this.nextPermutation.subList(j + 1, n));
/*     */     }
/*     */     
/*     */     int findNextJ() {
/* 539 */       for (int k = this.nextPermutation.size() - 2; k >= 0; k--) {
/* 540 */         if (this.comparator.compare(this.nextPermutation.get(k), this.nextPermutation.get(k + 1)) < 0) {
/* 541 */           return k;
/*     */         }
/*     */       } 
/* 544 */       return -1;
/*     */     }
/*     */     
/*     */     int findNextL(int j) {
/* 548 */       E ak = this.nextPermutation.get(j);
/* 549 */       for (int l = this.nextPermutation.size() - 1; l > j; l--) {
/* 550 */         if (this.comparator.compare(ak, this.nextPermutation.get(l)) < 0) {
/* 551 */           return l;
/*     */         }
/*     */       } 
/* 554 */       throw new AssertionError("this statement should be unreachable");
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
/*     */ 
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
/*     */   public static <E> Collection<List<E>> permutations(Collection<E> elements) {
/* 577 */     return new PermutationCollection<>(ImmutableList.copyOf(elements));
/*     */   }
/*     */   
/*     */   private static final class PermutationCollection<E> extends AbstractCollection<List<E>> {
/*     */     final ImmutableList<E> inputList;
/*     */     
/*     */     PermutationCollection(ImmutableList<E> input) {
/* 584 */       this.inputList = input;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 589 */       return IntMath.factorial(this.inputList.size());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 594 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<List<E>> iterator() {
/* 599 */       return new Collections2.PermutationIterator<>(this.inputList);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object obj) {
/* 604 */       if (obj instanceof List) {
/* 605 */         List<?> list = (List)obj;
/* 606 */         return Collections2.isPermutation(this.inputList, list);
/*     */       } 
/* 608 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 613 */       String str = String.valueOf(this.inputList); return (new StringBuilder(14 + String.valueOf(str).length())).append("permutations(").append(str).append(")").toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class PermutationIterator<E> extends AbstractIterator<List<E>> {
/*     */     final List<E> list;
/*     */     final int[] c;
/*     */     final int[] o;
/*     */     int j;
/*     */     
/*     */     PermutationIterator(List<E> list) {
/* 624 */       this.list = new ArrayList<>(list);
/* 625 */       int n = list.size();
/* 626 */       this.c = new int[n];
/* 627 */       this.o = new int[n];
/* 628 */       Arrays.fill(this.c, 0);
/* 629 */       Arrays.fill(this.o, 1);
/* 630 */       this.j = Integer.MAX_VALUE;
/*     */     }
/*     */ 
/*     */     
/*     */     protected List<E> computeNext() {
/* 635 */       if (this.j <= 0) {
/* 636 */         return endOfData();
/*     */       }
/* 638 */       ImmutableList<E> next = ImmutableList.copyOf(this.list);
/* 639 */       calculateNextPermutation();
/* 640 */       return next;
/*     */     }
/*     */     
/*     */     void calculateNextPermutation() {
/* 644 */       this.j = this.list.size() - 1;
/* 645 */       int s = 0;
/*     */ 
/*     */ 
/*     */       
/* 649 */       if (this.j == -1) {
/*     */         return;
/*     */       }
/*     */       
/*     */       while (true) {
/* 654 */         int q = this.c[this.j] + this.o[this.j];
/* 655 */         if (q < 0) {
/* 656 */           switchDirection();
/*     */           continue;
/*     */         } 
/* 659 */         if (q == this.j + 1) {
/* 660 */           if (this.j == 0) {
/*     */             break;
/*     */           }
/* 663 */           s++;
/* 664 */           switchDirection();
/*     */           
/*     */           continue;
/*     */         } 
/* 668 */         Collections.swap(this.list, this.j - this.c[this.j] + s, this.j - q + s);
/* 669 */         this.c[this.j] = q;
/*     */         break;
/*     */       } 
/*     */     }
/*     */     
/*     */     void switchDirection() {
/* 675 */       this.o[this.j] = -this.o[this.j];
/* 676 */       this.j--;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isPermutation(List<?> first, List<?> second) {
/* 682 */     if (first.size() != second.size()) {
/* 683 */       return false;
/*     */     }
/* 685 */     Multiset<?> firstMultiset = HashMultiset.create(first);
/* 686 */     Multiset<?> secondMultiset = HashMultiset.create(second);
/* 687 */     return firstMultiset.equals(secondMultiset);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\Collections2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */