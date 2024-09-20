/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.j2objc.annotations.RetainedWith;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Deque;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableMap;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.Queue;
/*      */ import java.util.RandomAccess;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.SortedSet;
/*      */ import java.util.Spliterator;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.function.UnaryOperator;
/*      */ import java.util.stream.Stream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */ final class Synchronized
/*      */ {
/*      */   static class SynchronizedObject
/*      */     implements Serializable
/*      */   {
/*      */     final Object delegate;
/*      */     final Object mutex;
/*      */     @GwtIncompatible
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedObject(Object delegate, Object mutex) {
/*   74 */       this.delegate = Preconditions.checkNotNull(delegate);
/*   75 */       this.mutex = (mutex == null) ? this : mutex;
/*      */     }
/*      */     
/*      */     Object delegate() {
/*   79 */       return this.delegate;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*   86 */       synchronized (this.mutex) {
/*   87 */         return this.delegate.toString();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*   98 */       synchronized (this.mutex) {
/*   99 */         stream.defaultWriteObject();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> Collection<E> collection(Collection<E> collection, Object mutex) {
/*  108 */     return new SynchronizedCollection<>(collection, mutex);
/*      */   }
/*      */   @VisibleForTesting
/*      */   static class SynchronizedCollection<E> extends SynchronizedObject implements Collection<E> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     private SynchronizedCollection(Collection<E> delegate, Object mutex) {
/*  114 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Collection<E> delegate() {
/*  120 */       return (Collection<E>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean add(E e) {
/*  125 */       synchronized (this.mutex) {
/*  126 */         return delegate().add(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addAll(Collection<? extends E> c) {
/*  132 */       synchronized (this.mutex) {
/*  133 */         return delegate().addAll(c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  139 */       synchronized (this.mutex) {
/*  140 */         delegate().clear();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  146 */       synchronized (this.mutex) {
/*  147 */         return delegate().contains(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  153 */       synchronized (this.mutex) {
/*  154 */         return delegate().containsAll(c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  160 */       synchronized (this.mutex) {
/*  161 */         return delegate().isEmpty();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/*  167 */       return delegate().iterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public Spliterator<E> spliterator() {
/*  172 */       synchronized (this.mutex) {
/*  173 */         return delegate().spliterator();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Stream<E> stream() {
/*  179 */       synchronized (this.mutex) {
/*  180 */         return delegate().stream();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Stream<E> parallelStream() {
/*  186 */       synchronized (this.mutex) {
/*  187 */         return delegate().parallelStream();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super E> action) {
/*  193 */       synchronized (this.mutex) {
/*  194 */         delegate().forEach(action);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/*  200 */       synchronized (this.mutex) {
/*  201 */         return delegate().remove(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*  207 */       synchronized (this.mutex) {
/*  208 */         return delegate().removeAll(c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*  214 */       synchronized (this.mutex) {
/*  215 */         return delegate().retainAll(c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeIf(Predicate<? super E> filter) {
/*  221 */       synchronized (this.mutex) {
/*  222 */         return delegate().removeIf(filter);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  228 */       synchronized (this.mutex) {
/*  229 */         return delegate().size();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/*  235 */       synchronized (this.mutex) {
/*  236 */         return delegate().toArray();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] a) {
/*  242 */       synchronized (this.mutex) {
/*  243 */         return delegate().toArray(a);
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   static <E> Set<E> set(Set<E> set, Object mutex) {
/*  252 */     return new SynchronizedSet<>(set, mutex);
/*      */   }
/*      */   
/*      */   static class SynchronizedSet<E> extends SynchronizedCollection<E> implements Set<E> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedSet(Set<E> delegate, Object mutex) {
/*  258 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     Set<E> delegate() {
/*  263 */       return (Set<E>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  268 */       if (o == this) {
/*  269 */         return true;
/*      */       }
/*  271 */       synchronized (this.mutex) {
/*  272 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  278 */       synchronized (this.mutex) {
/*  279 */         return delegate().hashCode();
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> SortedSet<E> sortedSet(SortedSet<E> set, Object mutex) {
/*  287 */     return new SynchronizedSortedSet<>(set, mutex);
/*      */   }
/*      */   static class SynchronizedSortedSet<E> extends SynchronizedSet<E> implements SortedSet<E> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedSortedSet(SortedSet<E> delegate, Object mutex) {
/*  292 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     SortedSet<E> delegate() {
/*  297 */       return (SortedSet<E>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super E> comparator() {
/*  302 */       synchronized (this.mutex) {
/*  303 */         return delegate().comparator();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> subSet(E fromElement, E toElement) {
/*  309 */       synchronized (this.mutex) {
/*  310 */         return Synchronized.sortedSet(delegate().subSet(fromElement, toElement), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> headSet(E toElement) {
/*  316 */       synchronized (this.mutex) {
/*  317 */         return Synchronized.sortedSet(delegate().headSet(toElement), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> tailSet(E fromElement) {
/*  323 */       synchronized (this.mutex) {
/*  324 */         return Synchronized.sortedSet(delegate().tailSet(fromElement), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E first() {
/*  330 */       synchronized (this.mutex) {
/*  331 */         return delegate().first();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E last() {
/*  337 */       synchronized (this.mutex) {
/*  338 */         return delegate().last();
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> List<E> list(List<E> list, Object mutex) {
/*  346 */     return (list instanceof RandomAccess) ? 
/*  347 */       new SynchronizedRandomAccessList<>(list, mutex) : 
/*  348 */       new SynchronizedList<>(list, mutex);
/*      */   }
/*      */   private static class SynchronizedList<E> extends SynchronizedCollection<E> implements List<E> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedList(List<E> delegate, Object mutex) {
/*  353 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     List<E> delegate() {
/*  358 */       return (List<E>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public void add(int index, E element) {
/*  363 */       synchronized (this.mutex) {
/*  364 */         delegate().add(index, element);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addAll(int index, Collection<? extends E> c) {
/*  370 */       synchronized (this.mutex) {
/*  371 */         return delegate().addAll(index, c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E get(int index) {
/*  377 */       synchronized (this.mutex) {
/*  378 */         return delegate().get(index);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexOf(Object o) {
/*  384 */       synchronized (this.mutex) {
/*  385 */         return delegate().indexOf(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int lastIndexOf(Object o) {
/*  391 */       synchronized (this.mutex) {
/*  392 */         return delegate().lastIndexOf(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<E> listIterator() {
/*  398 */       return delegate().listIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<E> listIterator(int index) {
/*  403 */       return delegate().listIterator(index);
/*      */     }
/*      */ 
/*      */     
/*      */     public E remove(int index) {
/*  408 */       synchronized (this.mutex) {
/*  409 */         return delegate().remove(index);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E set(int index, E element) {
/*  415 */       synchronized (this.mutex) {
/*  416 */         return delegate().set(index, element);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void replaceAll(UnaryOperator<E> operator) {
/*  422 */       synchronized (this.mutex) {
/*  423 */         delegate().replaceAll(operator);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void sort(Comparator<? super E> c) {
/*  429 */       synchronized (this.mutex) {
/*  430 */         delegate().sort(c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public List<E> subList(int fromIndex, int toIndex) {
/*  436 */       synchronized (this.mutex) {
/*  437 */         return Synchronized.list(delegate().subList(fromIndex, toIndex), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  443 */       if (o == this) {
/*  444 */         return true;
/*      */       }
/*  446 */       synchronized (this.mutex) {
/*  447 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  453 */       synchronized (this.mutex) {
/*  454 */         return delegate().hashCode();
/*      */       } 
/*      */     } }
/*      */ 
/*      */   
/*      */   private static class SynchronizedRandomAccessList<E>
/*      */     extends SynchronizedList<E> implements RandomAccess {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedRandomAccessList(List<E> list, Object mutex) {
/*  464 */       super(list, mutex);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> Multiset<E> multiset(Multiset<E> multiset, Object mutex) {
/*  471 */     if (multiset instanceof SynchronizedMultiset || multiset instanceof ImmutableMultiset) {
/*  472 */       return multiset;
/*      */     }
/*  474 */     return new SynchronizedMultiset<>(multiset, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedMultiset<E> extends SynchronizedCollection<E> implements Multiset<E> {
/*      */     transient Set<E> elementSet;
/*      */     transient Set<Multiset.Entry<E>> entrySet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedMultiset(Multiset<E> delegate, Object mutex) {
/*  483 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     Multiset<E> delegate() {
/*  488 */       return (Multiset<E>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public int count(Object o) {
/*  493 */       synchronized (this.mutex) {
/*  494 */         return delegate().count(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int add(E e, int n) {
/*  500 */       synchronized (this.mutex) {
/*  501 */         return delegate().add(e, n);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int remove(Object o, int n) {
/*  507 */       synchronized (this.mutex) {
/*  508 */         return delegate().remove(o, n);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int setCount(E element, int count) {
/*  514 */       synchronized (this.mutex) {
/*  515 */         return delegate().setCount(element, count);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean setCount(E element, int oldCount, int newCount) {
/*  521 */       synchronized (this.mutex) {
/*  522 */         return delegate().setCount(element, oldCount, newCount);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<E> elementSet() {
/*  528 */       synchronized (this.mutex) {
/*  529 */         if (this.elementSet == null) {
/*  530 */           this.elementSet = Synchronized.typePreservingSet(delegate().elementSet(), this.mutex);
/*      */         }
/*  532 */         return this.elementSet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Multiset.Entry<E>> entrySet() {
/*  538 */       synchronized (this.mutex) {
/*  539 */         if (this.entrySet == null) {
/*  540 */           this.entrySet = (Set)Synchronized.typePreservingSet((Set)delegate().entrySet(), this.mutex);
/*      */         }
/*  542 */         return this.entrySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  548 */       if (o == this) {
/*  549 */         return true;
/*      */       }
/*  551 */       synchronized (this.mutex) {
/*  552 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  558 */       synchronized (this.mutex) {
/*  559 */         return delegate().hashCode();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> Multimap<K, V> multimap(Multimap<K, V> multimap, Object mutex) {
/*  567 */     if (multimap instanceof SynchronizedMultimap || multimap instanceof BaseImmutableMultimap) {
/*  568 */       return multimap;
/*      */     }
/*  570 */     return new SynchronizedMultimap<>(multimap, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedMultimap<K, V>
/*      */     extends SynchronizedObject
/*      */     implements Multimap<K, V> {
/*      */     transient Set<K> keySet;
/*      */     transient Collection<V> valuesCollection;
/*      */     transient Collection<Map.Entry<K, V>> entries;
/*      */     transient Map<K, Collection<V>> asMap;
/*      */     transient Multiset<K> keys;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     Multimap<K, V> delegate() {
/*  584 */       return (Multimap<K, V>)super.delegate();
/*      */     }
/*      */     
/*      */     SynchronizedMultimap(Multimap<K, V> delegate, Object mutex) {
/*  588 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  593 */       synchronized (this.mutex) {
/*  594 */         return delegate().size();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  600 */       synchronized (this.mutex) {
/*  601 */         return delegate().isEmpty();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/*  607 */       synchronized (this.mutex) {
/*  608 */         return delegate().containsKey(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsValue(Object value) {
/*  614 */       synchronized (this.mutex) {
/*  615 */         return delegate().containsValue(value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsEntry(Object key, Object value) {
/*  621 */       synchronized (this.mutex) {
/*  622 */         return delegate().containsEntry(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> get(K key) {
/*  628 */       synchronized (this.mutex) {
/*  629 */         return Synchronized.typePreservingCollection(delegate().get(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean put(K key, V value) {
/*  635 */       synchronized (this.mutex) {
/*  636 */         return delegate().put(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(K key, Iterable<? extends V> values) {
/*  642 */       synchronized (this.mutex) {
/*  643 */         return delegate().putAll(key, values);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/*  649 */       synchronized (this.mutex) {
/*  650 */         return delegate().putAll(multimap);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
/*  656 */       synchronized (this.mutex) {
/*  657 */         return delegate().replaceValues(key, values);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object key, Object value) {
/*  663 */       synchronized (this.mutex) {
/*  664 */         return delegate().remove(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> removeAll(Object key) {
/*  670 */       synchronized (this.mutex) {
/*  671 */         return delegate().removeAll(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  677 */       synchronized (this.mutex) {
/*  678 */         delegate().clear();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/*  684 */       synchronized (this.mutex) {
/*  685 */         if (this.keySet == null) {
/*  686 */           this.keySet = Synchronized.typePreservingSet(delegate().keySet(), this.mutex);
/*      */         }
/*  688 */         return this.keySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/*  694 */       synchronized (this.mutex) {
/*  695 */         if (this.valuesCollection == null) {
/*  696 */           this.valuesCollection = Synchronized.collection(delegate().values(), this.mutex);
/*      */         }
/*  698 */         return this.valuesCollection;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<Map.Entry<K, V>> entries() {
/*  704 */       synchronized (this.mutex) {
/*  705 */         if (this.entries == null) {
/*  706 */           this.entries = (Collection)Synchronized.typePreservingCollection((Collection)delegate().entries(), this.mutex);
/*      */         }
/*  708 */         return this.entries;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(BiConsumer<? super K, ? super V> action) {
/*  714 */       synchronized (this.mutex) {
/*  715 */         delegate().forEach(action);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<K, Collection<V>> asMap() {
/*  721 */       synchronized (this.mutex) {
/*  722 */         if (this.asMap == null) {
/*  723 */           this.asMap = new Synchronized.SynchronizedAsMap<>(delegate().asMap(), this.mutex);
/*      */         }
/*  725 */         return this.asMap;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Multiset<K> keys() {
/*  731 */       synchronized (this.mutex) {
/*  732 */         if (this.keys == null) {
/*  733 */           this.keys = Synchronized.multiset(delegate().keys(), this.mutex);
/*      */         }
/*  735 */         return this.keys;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  741 */       if (o == this) {
/*  742 */         return true;
/*      */       }
/*  744 */       synchronized (this.mutex) {
/*  745 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  751 */       synchronized (this.mutex) {
/*  752 */         return delegate().hashCode();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> ListMultimap<K, V> listMultimap(ListMultimap<K, V> multimap, Object mutex) {
/*  761 */     if (multimap instanceof SynchronizedListMultimap || multimap instanceof BaseImmutableMultimap) {
/*  762 */       return multimap;
/*      */     }
/*  764 */     return new SynchronizedListMultimap<>(multimap, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedListMultimap<K, V> extends SynchronizedMultimap<K, V> implements ListMultimap<K, V> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedListMultimap(ListMultimap<K, V> delegate, Object mutex) {
/*  770 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     ListMultimap<K, V> delegate() {
/*  775 */       return (ListMultimap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> get(K key) {
/*  780 */       synchronized (this.mutex) {
/*  781 */         return Synchronized.list(delegate().get(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> removeAll(Object key) {
/*  787 */       synchronized (this.mutex) {
/*  788 */         return delegate().removeAll(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> replaceValues(K key, Iterable<? extends V> values) {
/*  794 */       synchronized (this.mutex) {
/*  795 */         return delegate().replaceValues(key, values);
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> SetMultimap<K, V> setMultimap(SetMultimap<K, V> multimap, Object mutex) {
/*  803 */     if (multimap instanceof SynchronizedSetMultimap || multimap instanceof BaseImmutableMultimap) {
/*  804 */       return multimap;
/*      */     }
/*  806 */     return new SynchronizedSetMultimap<>(multimap, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedSetMultimap<K, V> extends SynchronizedMultimap<K, V> implements SetMultimap<K, V> {
/*      */     transient Set<Map.Entry<K, V>> entrySet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedSetMultimap(SetMultimap<K, V> delegate, Object mutex) {
/*  814 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     SetMultimap<K, V> delegate() {
/*  819 */       return (SetMultimap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> get(K key) {
/*  824 */       synchronized (this.mutex) {
/*  825 */         return Synchronized.set(delegate().get(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> removeAll(Object key) {
/*  831 */       synchronized (this.mutex) {
/*  832 */         return delegate().removeAll(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> replaceValues(K key, Iterable<? extends V> values) {
/*  838 */       synchronized (this.mutex) {
/*  839 */         return delegate().replaceValues(key, values);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entries() {
/*  845 */       synchronized (this.mutex) {
/*  846 */         if (this.entrySet == null) {
/*  847 */           this.entrySet = Synchronized.set(delegate().entries(), this.mutex);
/*      */         }
/*  849 */         return this.entrySet;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> SortedSetMultimap<K, V> sortedSetMultimap(SortedSetMultimap<K, V> multimap, Object mutex) {
/*  858 */     if (multimap instanceof SynchronizedSortedSetMultimap) {
/*  859 */       return multimap;
/*      */     }
/*  861 */     return new SynchronizedSortedSetMultimap<>(multimap, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedSortedSetMultimap<K, V> extends SynchronizedSetMultimap<K, V> implements SortedSetMultimap<K, V> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedSortedSetMultimap(SortedSetMultimap<K, V> delegate, Object mutex) {
/*  867 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     SortedSetMultimap<K, V> delegate() {
/*  872 */       return (SortedSetMultimap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> get(K key) {
/*  877 */       synchronized (this.mutex) {
/*  878 */         return Synchronized.sortedSet(delegate().get(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> removeAll(Object key) {
/*  884 */       synchronized (this.mutex) {
/*  885 */         return delegate().removeAll(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> replaceValues(K key, Iterable<? extends V> values) {
/*  891 */       synchronized (this.mutex) {
/*  892 */         return delegate().replaceValues(key, values);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super V> valueComparator() {
/*  898 */       synchronized (this.mutex) {
/*  899 */         return delegate().valueComparator();
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> Collection<E> typePreservingCollection(Collection<E> collection, Object mutex) {
/*  908 */     if (collection instanceof SortedSet) {
/*  909 */       return sortedSet((SortedSet<E>)collection, mutex);
/*      */     }
/*  911 */     if (collection instanceof Set) {
/*  912 */       return set((Set<E>)collection, mutex);
/*      */     }
/*  914 */     if (collection instanceof List) {
/*  915 */       return list((List<E>)collection, mutex);
/*      */     }
/*  917 */     return collection(collection, mutex);
/*      */   }
/*      */   
/*      */   private static <E> Set<E> typePreservingSet(Set<E> set, Object mutex) {
/*  921 */     if (set instanceof SortedSet) {
/*  922 */       return sortedSet((SortedSet<E>)set, mutex);
/*      */     }
/*  924 */     return set(set, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedAsMapEntries<K, V> extends SynchronizedSet<Map.Entry<K, Collection<V>>> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedAsMapEntries(Set<Map.Entry<K, Collection<V>>> delegate, Object mutex) {
/*  931 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/*  937 */       return new TransformedIterator<Map.Entry<K, Collection<V>>, Map.Entry<K, Collection<V>>>(super
/*  938 */           .iterator())
/*      */         {
/*      */           Map.Entry<K, Collection<V>> transform(final Map.Entry<K, Collection<V>> entry) {
/*  941 */             return (Map.Entry)new ForwardingMapEntry<K, Collection<Collection<V>>>()
/*      */               {
/*      */                 protected Map.Entry<K, Collection<V>> delegate() {
/*  944 */                   return entry;
/*      */                 }
/*      */ 
/*      */                 
/*      */                 public Collection<V> getValue() {
/*  949 */                   return Synchronized.typePreservingCollection((Collection)entry.getValue(), Synchronized.SynchronizedAsMapEntries.this.mutex);
/*      */                 }
/*      */               };
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/*  960 */       synchronized (this.mutex) {
/*  961 */         return ObjectArrays.toArrayImpl(delegate());
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] array) {
/*  967 */       synchronized (this.mutex) {
/*  968 */         return ObjectArrays.toArrayImpl(delegate(), array);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  974 */       synchronized (this.mutex) {
/*  975 */         return Maps.containsEntryImpl(delegate(), o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  981 */       synchronized (this.mutex) {
/*  982 */         return Collections2.containsAllImpl(delegate(), c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  988 */       if (o == this) {
/*  989 */         return true;
/*      */       }
/*  991 */       synchronized (this.mutex) {
/*  992 */         return Sets.equalsImpl(delegate(), o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/*  998 */       synchronized (this.mutex) {
/*  999 */         return Maps.removeEntryImpl(delegate(), o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/* 1005 */       synchronized (this.mutex) {
/* 1006 */         return Iterators.removeAll(delegate().iterator(), c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/* 1012 */       synchronized (this.mutex) {
/* 1013 */         return Iterators.retainAll(delegate().iterator(), c);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   static <K, V> Map<K, V> map(Map<K, V> map, Object mutex) {
/* 1022 */     return new SynchronizedMap<>(map, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedMap<K, V> extends SynchronizedObject implements Map<K, V> { transient Set<K> keySet;
/*      */     transient Collection<V> values;
/*      */     transient Set<Map.Entry<K, V>> entrySet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedMap(Map<K, V> delegate, Object mutex) {
/* 1031 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Map<K, V> delegate() {
/* 1037 */       return (Map<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1042 */       synchronized (this.mutex) {
/* 1043 */         delegate().clear();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 1049 */       synchronized (this.mutex) {
/* 1050 */         return delegate().containsKey(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsValue(Object value) {
/* 1056 */       synchronized (this.mutex) {
/* 1057 */         return delegate().containsValue(value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 1063 */       synchronized (this.mutex) {
/* 1064 */         if (this.entrySet == null) {
/* 1065 */           this.entrySet = Synchronized.set(delegate().entrySet(), this.mutex);
/*      */         }
/* 1067 */         return this.entrySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(BiConsumer<? super K, ? super V> action) {
/* 1073 */       synchronized (this.mutex) {
/* 1074 */         delegate().forEach(action);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(Object key) {
/* 1080 */       synchronized (this.mutex) {
/* 1081 */         return delegate().get(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V getOrDefault(Object key, V defaultValue) {
/* 1087 */       synchronized (this.mutex) {
/* 1088 */         return delegate().getOrDefault(key, defaultValue);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 1094 */       synchronized (this.mutex) {
/* 1095 */         return delegate().isEmpty();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 1101 */       synchronized (this.mutex) {
/* 1102 */         if (this.keySet == null) {
/* 1103 */           this.keySet = Synchronized.set(delegate().keySet(), this.mutex);
/*      */         }
/* 1105 */         return this.keySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V put(K key, V value) {
/* 1111 */       synchronized (this.mutex) {
/* 1112 */         return delegate().put(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V putIfAbsent(K key, V value) {
/* 1118 */       synchronized (this.mutex) {
/* 1119 */         return delegate().putIfAbsent(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean replace(K key, V oldValue, V newValue) {
/* 1125 */       synchronized (this.mutex) {
/* 1126 */         return delegate().replace(key, oldValue, newValue);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V replace(K key, V value) {
/* 1132 */       synchronized (this.mutex) {
/* 1133 */         return delegate().replace(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
/* 1139 */       synchronized (this.mutex) {
/* 1140 */         return delegate().computeIfAbsent(key, mappingFunction);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
/* 1147 */       synchronized (this.mutex) {
/* 1148 */         return delegate().computeIfPresent(key, remappingFunction);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
/* 1154 */       synchronized (this.mutex) {
/* 1155 */         return delegate().compute(key, remappingFunction);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
/* 1162 */       synchronized (this.mutex) {
/* 1163 */         return delegate().merge(key, value, remappingFunction);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void putAll(Map<? extends K, ? extends V> map) {
/* 1169 */       synchronized (this.mutex) {
/* 1170 */         delegate().putAll(map);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/* 1176 */       synchronized (this.mutex) {
/* 1177 */         delegate().replaceAll(function);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V remove(Object key) {
/* 1183 */       synchronized (this.mutex) {
/* 1184 */         return delegate().remove(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object key, Object value) {
/* 1190 */       synchronized (this.mutex) {
/* 1191 */         return delegate().remove(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1197 */       synchronized (this.mutex) {
/* 1198 */         return delegate().size();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/* 1204 */       synchronized (this.mutex) {
/* 1205 */         if (this.values == null) {
/* 1206 */           this.values = Synchronized.collection(delegate().values(), this.mutex);
/*      */         }
/* 1208 */         return this.values;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/* 1214 */       if (o == this) {
/* 1215 */         return true;
/*      */       }
/* 1217 */       synchronized (this.mutex) {
/* 1218 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1224 */       synchronized (this.mutex) {
/* 1225 */         return delegate().hashCode();
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> SortedMap<K, V> sortedMap(SortedMap<K, V> sortedMap, Object mutex) {
/* 1233 */     return new SynchronizedSortedMap<>(sortedMap, mutex);
/*      */   }
/*      */   
/*      */   static class SynchronizedSortedMap<K, V> extends SynchronizedMap<K, V> implements SortedMap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedSortedMap(SortedMap<K, V> delegate, Object mutex) {
/* 1240 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     SortedMap<K, V> delegate() {
/* 1245 */       return (SortedMap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 1250 */       synchronized (this.mutex) {
/* 1251 */         return delegate().comparator();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public K firstKey() {
/* 1257 */       synchronized (this.mutex) {
/* 1258 */         return delegate().firstKey();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> headMap(K toKey) {
/* 1264 */       synchronized (this.mutex) {
/* 1265 */         return Synchronized.sortedMap(delegate().headMap(toKey), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public K lastKey() {
/* 1271 */       synchronized (this.mutex) {
/* 1272 */         return delegate().lastKey();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 1278 */       synchronized (this.mutex) {
/* 1279 */         return Synchronized.sortedMap(delegate().subMap(fromKey, toKey), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> tailMap(K fromKey) {
/* 1285 */       synchronized (this.mutex) {
/* 1286 */         return Synchronized.sortedMap(delegate().tailMap(fromKey), this.mutex);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> BiMap<K, V> biMap(BiMap<K, V> bimap, Object mutex) {
/* 1294 */     if (bimap instanceof SynchronizedBiMap || bimap instanceof ImmutableBiMap) {
/* 1295 */       return bimap;
/*      */     }
/* 1297 */     return new SynchronizedBiMap<>(bimap, mutex, null);
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   static class SynchronizedBiMap<K, V> extends SynchronizedMap<K, V> implements BiMap<K, V>, Serializable {
/*      */     private transient Set<V> valueSet;
/*      */     @RetainedWith
/*      */     private transient BiMap<V, K> inverse;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     private SynchronizedBiMap(BiMap<K, V> delegate, Object mutex, BiMap<V, K> inverse) {
/* 1308 */       super(delegate, mutex);
/* 1309 */       this.inverse = inverse;
/*      */     }
/*      */ 
/*      */     
/*      */     BiMap<K, V> delegate() {
/* 1314 */       return (BiMap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> values() {
/* 1319 */       synchronized (this.mutex) {
/* 1320 */         if (this.valueSet == null) {
/* 1321 */           this.valueSet = Synchronized.set(delegate().values(), this.mutex);
/*      */         }
/* 1323 */         return this.valueSet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V forcePut(K key, V value) {
/* 1329 */       synchronized (this.mutex) {
/* 1330 */         return delegate().forcePut(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public BiMap<V, K> inverse() {
/* 1336 */       synchronized (this.mutex) {
/* 1337 */         if (this.inverse == null) {
/* 1338 */           this.inverse = new SynchronizedBiMap(delegate().inverse(), this.mutex, this);
/*      */         }
/* 1340 */         return this.inverse;
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SynchronizedAsMap<K, V>
/*      */     extends SynchronizedMap<K, Collection<V>> {
/*      */     transient Set<Map.Entry<K, Collection<V>>> asMapEntrySet;
/*      */     transient Collection<Collection<V>> asMapValues;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedAsMap(Map<K, Collection<V>> delegate, Object mutex) {
/* 1352 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> get(Object key) {
/* 1357 */       synchronized (this.mutex) {
/* 1358 */         Collection<V> collection = super.get(key);
/* 1359 */         return (collection == null) ? null : Synchronized.typePreservingCollection(collection, this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, Collection<V>>> entrySet() {
/* 1365 */       synchronized (this.mutex) {
/* 1366 */         if (this.asMapEntrySet == null) {
/* 1367 */           this.asMapEntrySet = new Synchronized.SynchronizedAsMapEntries<>(delegate().entrySet(), this.mutex);
/*      */         }
/* 1369 */         return this.asMapEntrySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<Collection<V>> values() {
/* 1375 */       synchronized (this.mutex) {
/* 1376 */         if (this.asMapValues == null) {
/* 1377 */           this.asMapValues = new Synchronized.SynchronizedAsMapValues<>(delegate().values(), this.mutex);
/*      */         }
/* 1379 */         return this.asMapValues;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean containsValue(Object o) {
/* 1386 */       return values().contains(o);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SynchronizedAsMapValues<V> extends SynchronizedCollection<Collection<V>> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedAsMapValues(Collection<Collection<V>> delegate, Object mutex) {
/* 1394 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Iterator<Collection<V>> iterator() {
/* 1400 */       return new TransformedIterator<Collection<V>, Collection<V>>(super.iterator())
/*      */         {
/*      */           Collection<V> transform(Collection<V> from) {
/* 1403 */             return Synchronized.typePreservingCollection(from, Synchronized.SynchronizedAsMapValues.this.mutex);
/*      */           }
/*      */         };
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   @VisibleForTesting
/*      */   static class SynchronizedNavigableSet<E> extends SynchronizedSortedSet<E> implements NavigableSet<E> {
/*      */     transient NavigableSet<E> descendingSet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedNavigableSet(NavigableSet<E> delegate, Object mutex) {
/* 1416 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableSet<E> delegate() {
/* 1421 */       return (NavigableSet<E>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public E ceiling(E e) {
/* 1426 */       synchronized (this.mutex) {
/* 1427 */         return delegate().ceiling(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> descendingIterator() {
/* 1433 */       return delegate().descendingIterator();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> descendingSet() {
/* 1440 */       synchronized (this.mutex) {
/* 1441 */         if (this.descendingSet == null) {
/* 1442 */           NavigableSet<E> dS = Synchronized.navigableSet(delegate().descendingSet(), this.mutex);
/* 1443 */           this.descendingSet = dS;
/* 1444 */           return dS;
/*      */         } 
/* 1446 */         return this.descendingSet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E floor(E e) {
/* 1452 */       synchronized (this.mutex) {
/* 1453 */         return delegate().floor(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 1459 */       synchronized (this.mutex) {
/* 1460 */         return Synchronized.navigableSet(delegate().headSet(toElement, inclusive), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> headSet(E toElement) {
/* 1466 */       return headSet(toElement, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public E higher(E e) {
/* 1471 */       synchronized (this.mutex) {
/* 1472 */         return delegate().higher(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E lower(E e) {
/* 1478 */       synchronized (this.mutex) {
/* 1479 */         return delegate().lower(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollFirst() {
/* 1485 */       synchronized (this.mutex) {
/* 1486 */         return delegate().pollFirst();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollLast() {
/* 1492 */       synchronized (this.mutex) {
/* 1493 */         return delegate().pollLast();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 1500 */       synchronized (this.mutex) {
/* 1501 */         return Synchronized.navigableSet(
/* 1502 */             delegate().subSet(fromElement, fromInclusive, toElement, toInclusive), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> subSet(E fromElement, E toElement) {
/* 1508 */       return subSet(fromElement, true, toElement, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 1513 */       synchronized (this.mutex) {
/* 1514 */         return Synchronized.navigableSet(delegate().tailSet(fromElement, inclusive), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> tailSet(E fromElement) {
/* 1520 */       return tailSet(fromElement, true);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   static <E> NavigableSet<E> navigableSet(NavigableSet<E> navigableSet, Object mutex) {
/* 1528 */     return new SynchronizedNavigableSet<>(navigableSet, mutex);
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   static <E> NavigableSet<E> navigableSet(NavigableSet<E> navigableSet) {
/* 1533 */     return navigableSet(navigableSet, null);
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   static <K, V> NavigableMap<K, V> navigableMap(NavigableMap<K, V> navigableMap) {
/* 1538 */     return navigableMap(navigableMap, null);
/*      */   }
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   static <K, V> NavigableMap<K, V> navigableMap(NavigableMap<K, V> navigableMap, Object mutex) {
/* 1544 */     return new SynchronizedNavigableMap<>(navigableMap, mutex);
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   @VisibleForTesting
/*      */   static class SynchronizedNavigableMap<K, V> extends SynchronizedSortedMap<K, V> implements NavigableMap<K, V> { transient NavigableSet<K> descendingKeySet;
/*      */     transient NavigableMap<K, V> descendingMap;
/*      */     
/*      */     SynchronizedNavigableMap(NavigableMap<K, V> delegate, Object mutex) {
/* 1553 */       super(delegate, mutex);
/*      */     }
/*      */     transient NavigableSet<K> navigableKeySet; private static final long serialVersionUID = 0L;
/*      */     
/*      */     NavigableMap<K, V> delegate() {
/* 1558 */       return (NavigableMap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> ceilingEntry(K key) {
/* 1563 */       synchronized (this.mutex) {
/* 1564 */         return Synchronized.nullableSynchronizedEntry(delegate().ceilingEntry(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public K ceilingKey(K key) {
/* 1570 */       synchronized (this.mutex) {
/* 1571 */         return delegate().ceilingKey(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingKeySet() {
/* 1579 */       synchronized (this.mutex) {
/* 1580 */         if (this.descendingKeySet == null) {
/* 1581 */           return this.descendingKeySet = Synchronized.navigableSet(delegate().descendingKeySet(), this.mutex);
/*      */         }
/* 1583 */         return this.descendingKeySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> descendingMap() {
/* 1591 */       synchronized (this.mutex) {
/* 1592 */         if (this.descendingMap == null) {
/* 1593 */           return this.descendingMap = Synchronized.<K, V>navigableMap(delegate().descendingMap(), this.mutex);
/*      */         }
/* 1595 */         return this.descendingMap;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> firstEntry() {
/* 1601 */       synchronized (this.mutex) {
/* 1602 */         return Synchronized.nullableSynchronizedEntry(delegate().firstEntry(), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> floorEntry(K key) {
/* 1608 */       synchronized (this.mutex) {
/* 1609 */         return Synchronized.nullableSynchronizedEntry(delegate().floorEntry(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public K floorKey(K key) {
/* 1615 */       synchronized (this.mutex) {
/* 1616 */         return delegate().floorKey(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
/* 1622 */       synchronized (this.mutex) {
/* 1623 */         return Synchronized.navigableMap(delegate().headMap(toKey, inclusive), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> headMap(K toKey) {
/* 1629 */       return headMap(toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> higherEntry(K key) {
/* 1634 */       synchronized (this.mutex) {
/* 1635 */         return Synchronized.nullableSynchronizedEntry(delegate().higherEntry(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public K higherKey(K key) {
/* 1641 */       synchronized (this.mutex) {
/* 1642 */         return delegate().higherKey(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> lastEntry() {
/* 1648 */       synchronized (this.mutex) {
/* 1649 */         return Synchronized.nullableSynchronizedEntry(delegate().lastEntry(), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> lowerEntry(K key) {
/* 1655 */       synchronized (this.mutex) {
/* 1656 */         return Synchronized.nullableSynchronizedEntry(delegate().lowerEntry(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public K lowerKey(K key) {
/* 1662 */       synchronized (this.mutex) {
/* 1663 */         return delegate().lowerKey(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 1669 */       return navigableKeySet();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 1676 */       synchronized (this.mutex) {
/* 1677 */         if (this.navigableKeySet == null) {
/* 1678 */           return this.navigableKeySet = Synchronized.navigableSet(delegate().navigableKeySet(), this.mutex);
/*      */         }
/* 1680 */         return this.navigableKeySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> pollFirstEntry() {
/* 1686 */       synchronized (this.mutex) {
/* 1687 */         return Synchronized.nullableSynchronizedEntry(delegate().pollFirstEntry(), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> pollLastEntry() {
/* 1693 */       synchronized (this.mutex) {
/* 1694 */         return Synchronized.nullableSynchronizedEntry(delegate().pollLastEntry(), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 1701 */       synchronized (this.mutex) {
/* 1702 */         return Synchronized.navigableMap(delegate().subMap(fromKey, fromInclusive, toKey, toInclusive), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 1708 */       return subMap(fromKey, true, toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
/* 1713 */       synchronized (this.mutex) {
/* 1714 */         return Synchronized.navigableMap(delegate().tailMap(fromKey, inclusive), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> tailMap(K fromKey) {
/* 1720 */       return tailMap(fromKey, true);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   private static <K, V> Map.Entry<K, V> nullableSynchronizedEntry(Map.Entry<K, V> entry, Object mutex) {
/* 1729 */     if (entry == null) {
/* 1730 */       return null;
/*      */     }
/* 1732 */     return new SynchronizedEntry<>(entry, mutex);
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   private static class SynchronizedEntry<K, V> extends SynchronizedObject implements Map.Entry<K, V> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedEntry(Map.Entry<K, V> delegate, Object mutex) {
/* 1739 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Map.Entry<K, V> delegate() {
/* 1745 */       return (Map.Entry<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/* 1750 */       synchronized (this.mutex) {
/* 1751 */         return delegate().equals(obj);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1757 */       synchronized (this.mutex) {
/* 1758 */         return delegate().hashCode();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public K getKey() {
/* 1764 */       synchronized (this.mutex) {
/* 1765 */         return delegate().getKey();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V getValue() {
/* 1771 */       synchronized (this.mutex) {
/* 1772 */         return delegate().getValue();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V setValue(V value) {
/* 1778 */       synchronized (this.mutex) {
/* 1779 */         return delegate().setValue(value);
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> Queue<E> queue(Queue<E> queue, Object mutex) {
/* 1787 */     return (queue instanceof SynchronizedQueue) ? queue : new SynchronizedQueue<>(queue, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedQueue<E> extends SynchronizedCollection<E> implements Queue<E> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedQueue(Queue<E> delegate, Object mutex) {
/* 1793 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     Queue<E> delegate() {
/* 1798 */       return (Queue<E>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public E element() {
/* 1803 */       synchronized (this.mutex) {
/* 1804 */         return delegate().element();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean offer(E e) {
/* 1810 */       synchronized (this.mutex) {
/* 1811 */         return delegate().offer(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E peek() {
/* 1817 */       synchronized (this.mutex) {
/* 1818 */         return delegate().peek();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E poll() {
/* 1824 */       synchronized (this.mutex) {
/* 1825 */         return delegate().poll();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E remove() {
/* 1831 */       synchronized (this.mutex) {
/* 1832 */         return delegate().remove();
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> Deque<E> deque(Deque<E> deque, Object mutex) {
/* 1840 */     return new SynchronizedDeque<>(deque, mutex);
/*      */   }
/*      */   
/*      */   private static final class SynchronizedDeque<E> extends SynchronizedQueue<E> implements Deque<E> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedDeque(Deque<E> delegate, Object mutex) {
/* 1846 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     Deque<E> delegate() {
/* 1851 */       return (Deque<E>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public void addFirst(E e) {
/* 1856 */       synchronized (this.mutex) {
/* 1857 */         delegate().addFirst(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void addLast(E e) {
/* 1863 */       synchronized (this.mutex) {
/* 1864 */         delegate().addLast(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean offerFirst(E e) {
/* 1870 */       synchronized (this.mutex) {
/* 1871 */         return delegate().offerFirst(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean offerLast(E e) {
/* 1877 */       synchronized (this.mutex) {
/* 1878 */         return delegate().offerLast(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E removeFirst() {
/* 1884 */       synchronized (this.mutex) {
/* 1885 */         return delegate().removeFirst();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E removeLast() {
/* 1891 */       synchronized (this.mutex) {
/* 1892 */         return delegate().removeLast();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollFirst() {
/* 1898 */       synchronized (this.mutex) {
/* 1899 */         return delegate().pollFirst();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollLast() {
/* 1905 */       synchronized (this.mutex) {
/* 1906 */         return delegate().pollLast();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E getFirst() {
/* 1912 */       synchronized (this.mutex) {
/* 1913 */         return delegate().getFirst();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E getLast() {
/* 1919 */       synchronized (this.mutex) {
/* 1920 */         return delegate().getLast();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E peekFirst() {
/* 1926 */       synchronized (this.mutex) {
/* 1927 */         return delegate().peekFirst();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E peekLast() {
/* 1933 */       synchronized (this.mutex) {
/* 1934 */         return delegate().peekLast();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeFirstOccurrence(Object o) {
/* 1940 */       synchronized (this.mutex) {
/* 1941 */         return delegate().removeFirstOccurrence(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeLastOccurrence(Object o) {
/* 1947 */       synchronized (this.mutex) {
/* 1948 */         return delegate().removeLastOccurrence(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void push(E e) {
/* 1954 */       synchronized (this.mutex) {
/* 1955 */         delegate().push(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E pop() {
/* 1961 */       synchronized (this.mutex) {
/* 1962 */         return delegate().pop();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> descendingIterator() {
/* 1968 */       synchronized (this.mutex) {
/* 1969 */         return delegate().descendingIterator();
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <R, C, V> Table<R, C, V> table(Table<R, C, V> table, Object mutex) {
/* 1977 */     return new SynchronizedTable<>(table, mutex);
/*      */   }
/*      */   
/*      */   private static final class SynchronizedTable<R, C, V>
/*      */     extends SynchronizedObject
/*      */     implements Table<R, C, V> {
/*      */     SynchronizedTable(Table<R, C, V> delegate, Object mutex) {
/* 1984 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Table<R, C, V> delegate() {
/* 1990 */       return (Table<R, C, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object rowKey, Object columnKey) {
/* 1995 */       synchronized (this.mutex) {
/* 1996 */         return delegate().contains(rowKey, columnKey);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsRow(Object rowKey) {
/* 2002 */       synchronized (this.mutex) {
/* 2003 */         return delegate().containsRow(rowKey);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsColumn(Object columnKey) {
/* 2009 */       synchronized (this.mutex) {
/* 2010 */         return delegate().containsColumn(columnKey);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsValue(Object value) {
/* 2016 */       synchronized (this.mutex) {
/* 2017 */         return delegate().containsValue(value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(Object rowKey, Object columnKey) {
/* 2023 */       synchronized (this.mutex) {
/* 2024 */         return delegate().get(rowKey, columnKey);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 2030 */       synchronized (this.mutex) {
/* 2031 */         return delegate().isEmpty();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 2037 */       synchronized (this.mutex) {
/* 2038 */         return delegate().size();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 2044 */       synchronized (this.mutex) {
/* 2045 */         delegate().clear();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V put(R rowKey, C columnKey, V value) {
/* 2051 */       synchronized (this.mutex) {
/* 2052 */         return delegate().put(rowKey, columnKey, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 2058 */       synchronized (this.mutex) {
/* 2059 */         delegate().putAll(table);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V remove(Object rowKey, Object columnKey) {
/* 2065 */       synchronized (this.mutex) {
/* 2066 */         return delegate().remove(rowKey, columnKey);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<C, V> row(R rowKey) {
/* 2072 */       synchronized (this.mutex) {
/* 2073 */         return Synchronized.map(delegate().row(rowKey), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<R, V> column(C columnKey) {
/* 2079 */       synchronized (this.mutex) {
/* 2080 */         return Synchronized.map(delegate().column(columnKey), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Table.Cell<R, C, V>> cellSet() {
/* 2086 */       synchronized (this.mutex) {
/* 2087 */         return Synchronized.set(delegate().cellSet(), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<R> rowKeySet() {
/* 2093 */       synchronized (this.mutex) {
/* 2094 */         return Synchronized.set(delegate().rowKeySet(), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<C> columnKeySet() {
/* 2100 */       synchronized (this.mutex) {
/* 2101 */         return Synchronized.set(delegate().columnKeySet(), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/* 2107 */       synchronized (this.mutex) {
/* 2108 */         return Synchronized.collection(delegate().values(), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<R, Map<C, V>> rowMap() {
/* 2114 */       synchronized (this.mutex) {
/* 2115 */         return Synchronized.map(
/* 2116 */             Maps.transformValues(
/* 2117 */               delegate().rowMap(), new Function<Map<C, V>, Map<C, V>>()
/*      */               {
/*      */                 public Map<C, V> apply(Map<C, V> t)
/*      */                 {
/* 2121 */                   return Synchronized.map(t, Synchronized.SynchronizedTable.this.mutex);
/*      */                 }
/*      */               }), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Map<C, Map<R, V>> columnMap() {
/* 2130 */       synchronized (this.mutex) {
/* 2131 */         return Synchronized.map(
/* 2132 */             Maps.transformValues(
/* 2133 */               delegate().columnMap(), new Function<Map<R, V>, Map<R, V>>()
/*      */               {
/*      */                 public Map<R, V> apply(Map<R, V> t)
/*      */                 {
/* 2137 */                   return Synchronized.map(t, Synchronized.SynchronizedTable.this.mutex);
/*      */                 }
/*      */               }), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 2146 */       synchronized (this.mutex) {
/* 2147 */         return delegate().hashCode();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/* 2153 */       if (this == obj) {
/* 2154 */         return true;
/*      */       }
/* 2156 */       synchronized (this.mutex) {
/* 2157 */         return delegate().equals(obj);
/*      */       } 
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\Synchronized.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */