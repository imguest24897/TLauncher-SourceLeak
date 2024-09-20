/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableMap;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.RandomAccess;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.SortedSet;
/*      */ import java.util.Spliterator;
/*      */ import java.util.function.BiConsumer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */ abstract class AbstractMapBasedMultimap<K, V>
/*      */   extends AbstractMultimap<K, V>
/*      */   implements Serializable
/*      */ {
/*      */   private transient Map<K, Collection<V>> map;
/*      */   private transient int totalSize;
/*      */   private static final long serialVersionUID = 2447537837011683357L;
/*      */   
/*      */   protected AbstractMapBasedMultimap(Map<K, Collection<V>> map) {
/*  117 */     Preconditions.checkArgument(map.isEmpty());
/*  118 */     this.map = map;
/*      */   }
/*      */ 
/*      */   
/*      */   final void setMap(Map<K, Collection<V>> map) {
/*  123 */     this.map = map;
/*  124 */     this.totalSize = 0;
/*  125 */     for (Collection<V> values : map.values()) {
/*  126 */       Preconditions.checkArgument(!values.isEmpty());
/*  127 */       this.totalSize += values.size();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Collection<V> createUnmodifiableEmptyCollection() {
/*  137 */     return unmodifiableCollectionSubclass(createCollection());
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
/*      */   Collection<V> createCollection(K key) {
/*  161 */     return createCollection();
/*      */   }
/*      */   
/*      */   Map<K, Collection<V>> backingMap() {
/*  165 */     return this.map;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  172 */     return this.totalSize;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsKey(Object key) {
/*  177 */     return this.map.containsKey(key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean put(K key, V value) {
/*  184 */     Collection<V> collection = this.map.get(key);
/*  185 */     if (collection == null) {
/*  186 */       collection = createCollection(key);
/*  187 */       if (collection.add(value)) {
/*  188 */         this.totalSize++;
/*  189 */         this.map.put(key, collection);
/*  190 */         return true;
/*      */       } 
/*  192 */       throw new AssertionError("New Collection violated the Collection spec");
/*      */     } 
/*  194 */     if (collection.add(value)) {
/*  195 */       this.totalSize++;
/*  196 */       return true;
/*      */     } 
/*  198 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private Collection<V> getOrCreateCollection(K key) {
/*  203 */     Collection<V> collection = this.map.get(key);
/*  204 */     if (collection == null) {
/*  205 */       collection = createCollection(key);
/*  206 */       this.map.put(key, collection);
/*      */     } 
/*  208 */     return collection;
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
/*      */   public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
/*  220 */     Iterator<? extends V> iterator = values.iterator();
/*  221 */     if (!iterator.hasNext()) {
/*  222 */       return removeAll(key);
/*      */     }
/*      */ 
/*      */     
/*  226 */     Collection<V> collection = getOrCreateCollection(key);
/*  227 */     Collection<V> oldValues = createCollection();
/*  228 */     oldValues.addAll(collection);
/*      */     
/*  230 */     this.totalSize -= collection.size();
/*  231 */     collection.clear();
/*      */     
/*  233 */     while (iterator.hasNext()) {
/*  234 */       if (collection.add(iterator.next())) {
/*  235 */         this.totalSize++;
/*      */       }
/*      */     } 
/*      */     
/*  239 */     return unmodifiableCollectionSubclass(oldValues);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> removeAll(Object key) {
/*  249 */     Collection<V> collection = this.map.remove(key);
/*      */     
/*  251 */     if (collection == null) {
/*  252 */       return createUnmodifiableEmptyCollection();
/*      */     }
/*      */     
/*  255 */     Collection<V> output = createCollection();
/*  256 */     output.addAll(collection);
/*  257 */     this.totalSize -= collection.size();
/*  258 */     collection.clear();
/*      */     
/*  260 */     return unmodifiableCollectionSubclass(output);
/*      */   }
/*      */   
/*      */   <E> Collection<E> unmodifiableCollectionSubclass(Collection<E> collection) {
/*  264 */     return Collections.unmodifiableCollection(collection);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void clear() {
/*  270 */     for (Collection<V> collection : this.map.values()) {
/*  271 */       collection.clear();
/*      */     }
/*  273 */     this.map.clear();
/*  274 */     this.totalSize = 0;
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
/*      */   public Collection<V> get(K key) {
/*  286 */     Collection<V> collection = this.map.get(key);
/*  287 */     if (collection == null) {
/*  288 */       collection = createCollection(key);
/*      */     }
/*  290 */     return wrapCollection(key, collection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Collection<V> wrapCollection(K key, Collection<V> collection) {
/*  298 */     return new WrappedCollection(key, collection, null);
/*      */   }
/*      */   
/*      */   final List<V> wrapList(K key, List<V> list, WrappedCollection ancestor) {
/*  302 */     return (list instanceof RandomAccess) ? 
/*  303 */       new RandomAccessWrappedList(this, key, list, ancestor) : 
/*  304 */       new WrappedList(key, list, ancestor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class WrappedCollection
/*      */     extends AbstractCollection<V>
/*      */   {
/*      */     final K key;
/*      */ 
/*      */ 
/*      */     
/*      */     Collection<V> delegate;
/*      */ 
/*      */ 
/*      */     
/*      */     final WrappedCollection ancestor;
/*      */ 
/*      */ 
/*      */     
/*      */     final Collection<V> ancestorDelegate;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     WrappedCollection(K key, Collection<V> delegate, WrappedCollection ancestor) {
/*  331 */       this.key = key;
/*  332 */       this.delegate = delegate;
/*  333 */       this.ancestor = ancestor;
/*  334 */       this.ancestorDelegate = (ancestor == null) ? null : ancestor.getDelegate();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void refreshIfEmpty() {
/*  345 */       if (this.ancestor != null) {
/*  346 */         this.ancestor.refreshIfEmpty();
/*  347 */         if (this.ancestor.getDelegate() != this.ancestorDelegate) {
/*  348 */           throw new ConcurrentModificationException();
/*      */         }
/*  350 */       } else if (this.delegate.isEmpty()) {
/*  351 */         Collection<V> newDelegate = (Collection<V>)AbstractMapBasedMultimap.this.map.get(this.key);
/*  352 */         if (newDelegate != null) {
/*  353 */           this.delegate = newDelegate;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void removeIfEmpty() {
/*  363 */       if (this.ancestor != null) {
/*  364 */         this.ancestor.removeIfEmpty();
/*  365 */       } else if (this.delegate.isEmpty()) {
/*  366 */         AbstractMapBasedMultimap.this.map.remove(this.key);
/*      */       } 
/*      */     }
/*      */     
/*      */     K getKey() {
/*  371 */       return this.key;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void addToMap() {
/*  381 */       if (this.ancestor != null) {
/*  382 */         this.ancestor.addToMap();
/*      */       } else {
/*  384 */         AbstractMapBasedMultimap.this.map.put(this.key, this.delegate);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  390 */       refreshIfEmpty();
/*  391 */       return this.delegate.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/*  396 */       if (object == this) {
/*  397 */         return true;
/*      */       }
/*  399 */       refreshIfEmpty();
/*  400 */       return this.delegate.equals(object);
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  405 */       refreshIfEmpty();
/*  406 */       return this.delegate.hashCode();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  411 */       refreshIfEmpty();
/*  412 */       return this.delegate.toString();
/*      */     }
/*      */     
/*      */     Collection<V> getDelegate() {
/*  416 */       return this.delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<V> iterator() {
/*  421 */       refreshIfEmpty();
/*  422 */       return new WrappedIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public Spliterator<V> spliterator() {
/*  427 */       refreshIfEmpty();
/*  428 */       return this.delegate.spliterator();
/*      */     }
/*      */     
/*      */     class WrappedIterator
/*      */       implements Iterator<V> {
/*      */       final Iterator<V> delegateIterator;
/*  434 */       final Collection<V> originalDelegate = AbstractMapBasedMultimap.WrappedCollection.this.delegate;
/*      */       
/*      */       WrappedIterator() {
/*  437 */         this.delegateIterator = AbstractMapBasedMultimap.iteratorOrListIterator(AbstractMapBasedMultimap.WrappedCollection.this.delegate);
/*      */       }
/*      */       
/*      */       WrappedIterator(Iterator<V> delegateIterator) {
/*  441 */         this.delegateIterator = delegateIterator;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       void validateIterator() {
/*  448 */         AbstractMapBasedMultimap.WrappedCollection.this.refreshIfEmpty();
/*  449 */         if (AbstractMapBasedMultimap.WrappedCollection.this.delegate != this.originalDelegate) {
/*  450 */           throw new ConcurrentModificationException();
/*      */         }
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean hasNext() {
/*  456 */         validateIterator();
/*  457 */         return this.delegateIterator.hasNext();
/*      */       }
/*      */ 
/*      */       
/*      */       public V next() {
/*  462 */         validateIterator();
/*  463 */         return this.delegateIterator.next();
/*      */       }
/*      */ 
/*      */       
/*      */       public void remove() {
/*  468 */         this.delegateIterator.remove();
/*  469 */         AbstractMapBasedMultimap.this.totalSize--;
/*  470 */         AbstractMapBasedMultimap.WrappedCollection.this.removeIfEmpty();
/*      */       }
/*      */       
/*      */       Iterator<V> getDelegateIterator() {
/*  474 */         validateIterator();
/*  475 */         return this.delegateIterator;
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean add(V value) {
/*  481 */       refreshIfEmpty();
/*  482 */       boolean wasEmpty = this.delegate.isEmpty();
/*  483 */       boolean changed = this.delegate.add(value);
/*  484 */       if (changed) {
/*  485 */         AbstractMapBasedMultimap.this.totalSize++;
/*  486 */         if (wasEmpty) {
/*  487 */           addToMap();
/*      */         }
/*      */       } 
/*  490 */       return changed;
/*      */     }
/*      */     
/*      */     WrappedCollection getAncestor() {
/*  494 */       return this.ancestor;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean addAll(Collection<? extends V> collection) {
/*  501 */       if (collection.isEmpty()) {
/*  502 */         return false;
/*      */       }
/*  504 */       int oldSize = size();
/*  505 */       boolean changed = this.delegate.addAll(collection);
/*  506 */       if (changed) {
/*  507 */         int newSize = this.delegate.size();
/*  508 */         AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
/*  509 */         if (oldSize == 0) {
/*  510 */           addToMap();
/*      */         }
/*      */       } 
/*  513 */       return changed;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  518 */       refreshIfEmpty();
/*  519 */       return this.delegate.contains(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  524 */       refreshIfEmpty();
/*  525 */       return this.delegate.containsAll(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  530 */       int oldSize = size();
/*  531 */       if (oldSize == 0) {
/*      */         return;
/*      */       }
/*  534 */       this.delegate.clear();
/*  535 */       AbstractMapBasedMultimap.this.totalSize -= oldSize;
/*  536 */       removeIfEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/*  541 */       refreshIfEmpty();
/*  542 */       boolean changed = this.delegate.remove(o);
/*  543 */       if (changed) {
/*  544 */         AbstractMapBasedMultimap.this.totalSize--;
/*  545 */         removeIfEmpty();
/*      */       } 
/*  547 */       return changed;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*  552 */       if (c.isEmpty()) {
/*  553 */         return false;
/*      */       }
/*  555 */       int oldSize = size();
/*  556 */       boolean changed = this.delegate.removeAll(c);
/*  557 */       if (changed) {
/*  558 */         int newSize = this.delegate.size();
/*  559 */         AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
/*  560 */         removeIfEmpty();
/*      */       } 
/*  562 */       return changed;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*  567 */       Preconditions.checkNotNull(c);
/*  568 */       int oldSize = size();
/*  569 */       boolean changed = this.delegate.retainAll(c);
/*  570 */       if (changed) {
/*  571 */         int newSize = this.delegate.size();
/*  572 */         AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
/*  573 */         removeIfEmpty();
/*      */       } 
/*  575 */       return changed;
/*      */     }
/*      */   }
/*      */   
/*      */   private static <E> Iterator<E> iteratorOrListIterator(Collection<E> collection) {
/*  580 */     return (collection instanceof List) ? (
/*  581 */       (List<E>)collection).listIterator() : 
/*  582 */       collection.iterator();
/*      */   }
/*      */   
/*      */   class WrappedSet
/*      */     extends WrappedCollection
/*      */     implements Set<V> {
/*      */     WrappedSet(K key, Set<V> delegate) {
/*  589 */       super(key, delegate, null);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*  594 */       if (c.isEmpty()) {
/*  595 */         return false;
/*      */       }
/*  597 */       int oldSize = size();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  602 */       boolean changed = Sets.removeAllImpl((Set)this.delegate, c);
/*  603 */       if (changed) {
/*  604 */         int newSize = this.delegate.size();
/*  605 */         AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
/*  606 */         removeIfEmpty();
/*      */       } 
/*  608 */       return changed;
/*      */     }
/*      */   }
/*      */   
/*      */   class WrappedSortedSet
/*      */     extends WrappedCollection
/*      */     implements SortedSet<V> {
/*      */     WrappedSortedSet(K key, SortedSet<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
/*  616 */       super(key, delegate, ancestor);
/*      */     }
/*      */     
/*      */     SortedSet<V> getSortedSetDelegate() {
/*  620 */       return (SortedSet<V>)getDelegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super V> comparator() {
/*  625 */       return getSortedSetDelegate().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public V first() {
/*  630 */       refreshIfEmpty();
/*  631 */       return getSortedSetDelegate().first();
/*      */     }
/*      */ 
/*      */     
/*      */     public V last() {
/*  636 */       refreshIfEmpty();
/*  637 */       return getSortedSetDelegate().last();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> headSet(V toElement) {
/*  642 */       refreshIfEmpty();
/*  643 */       return new WrappedSortedSet(
/*  644 */           getKey(), 
/*  645 */           getSortedSetDelegate().headSet(toElement), 
/*  646 */           (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> subSet(V fromElement, V toElement) {
/*  651 */       refreshIfEmpty();
/*  652 */       return new WrappedSortedSet(
/*  653 */           getKey(), 
/*  654 */           getSortedSetDelegate().subSet(fromElement, toElement), 
/*  655 */           (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> tailSet(V fromElement) {
/*  660 */       refreshIfEmpty();
/*  661 */       return new WrappedSortedSet(
/*  662 */           getKey(), 
/*  663 */           getSortedSetDelegate().tailSet(fromElement), 
/*  664 */           (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */   }
/*      */   
/*      */   class WrappedNavigableSet
/*      */     extends WrappedSortedSet
/*      */     implements NavigableSet<V> {
/*      */     WrappedNavigableSet(K key, NavigableSet<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
/*  672 */       super(key, delegate, ancestor);
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableSet<V> getSortedSetDelegate() {
/*  677 */       return (NavigableSet<V>)super.getSortedSetDelegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public V lower(V v) {
/*  682 */       return getSortedSetDelegate().lower(v);
/*      */     }
/*      */ 
/*      */     
/*      */     public V floor(V v) {
/*  687 */       return getSortedSetDelegate().floor(v);
/*      */     }
/*      */ 
/*      */     
/*      */     public V ceiling(V v) {
/*  692 */       return getSortedSetDelegate().ceiling(v);
/*      */     }
/*      */ 
/*      */     
/*      */     public V higher(V v) {
/*  697 */       return getSortedSetDelegate().higher(v);
/*      */     }
/*      */ 
/*      */     
/*      */     public V pollFirst() {
/*  702 */       return Iterators.pollNext(iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public V pollLast() {
/*  707 */       return Iterators.pollNext(descendingIterator());
/*      */     }
/*      */     
/*      */     private NavigableSet<V> wrap(NavigableSet<V> wrapped) {
/*  711 */       return new WrappedNavigableSet(this.key, wrapped, (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<V> descendingSet() {
/*  716 */       return wrap(getSortedSetDelegate().descendingSet());
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<V> descendingIterator() {
/*  721 */       return new AbstractMapBasedMultimap.WrappedCollection.WrappedIterator(this, getSortedSetDelegate().descendingIterator());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<V> subSet(V fromElement, boolean fromInclusive, V toElement, boolean toInclusive) {
/*  727 */       return wrap(
/*  728 */           getSortedSetDelegate().subSet(fromElement, fromInclusive, toElement, toInclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<V> headSet(V toElement, boolean inclusive) {
/*  733 */       return wrap(getSortedSetDelegate().headSet(toElement, inclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<V> tailSet(V fromElement, boolean inclusive) {
/*  738 */       return wrap(getSortedSetDelegate().tailSet(fromElement, inclusive));
/*      */     }
/*      */   }
/*      */   
/*      */   class WrappedList
/*      */     extends WrappedCollection
/*      */     implements List<V> {
/*      */     WrappedList(K key, List<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
/*  746 */       super(key, delegate, ancestor);
/*      */     }
/*      */     
/*      */     List<V> getListDelegate() {
/*  750 */       return (List<V>)getDelegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addAll(int index, Collection<? extends V> c) {
/*  755 */       if (c.isEmpty()) {
/*  756 */         return false;
/*      */       }
/*  758 */       int oldSize = size();
/*  759 */       boolean changed = getListDelegate().addAll(index, c);
/*  760 */       if (changed) {
/*  761 */         int newSize = getDelegate().size();
/*  762 */         AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
/*  763 */         if (oldSize == 0) {
/*  764 */           addToMap();
/*      */         }
/*      */       } 
/*  767 */       return changed;
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(int index) {
/*  772 */       refreshIfEmpty();
/*  773 */       return getListDelegate().get(index);
/*      */     }
/*      */ 
/*      */     
/*      */     public V set(int index, V element) {
/*  778 */       refreshIfEmpty();
/*  779 */       return getListDelegate().set(index, element);
/*      */     }
/*      */ 
/*      */     
/*      */     public void add(int index, V element) {
/*  784 */       refreshIfEmpty();
/*  785 */       boolean wasEmpty = getDelegate().isEmpty();
/*  786 */       getListDelegate().add(index, element);
/*  787 */       AbstractMapBasedMultimap.this.totalSize++;
/*  788 */       if (wasEmpty) {
/*  789 */         addToMap();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public V remove(int index) {
/*  795 */       refreshIfEmpty();
/*  796 */       V value = getListDelegate().remove(index);
/*  797 */       AbstractMapBasedMultimap.this.totalSize--;
/*  798 */       removeIfEmpty();
/*  799 */       return value;
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexOf(Object o) {
/*  804 */       refreshIfEmpty();
/*  805 */       return getListDelegate().indexOf(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public int lastIndexOf(Object o) {
/*  810 */       refreshIfEmpty();
/*  811 */       return getListDelegate().lastIndexOf(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<V> listIterator() {
/*  816 */       refreshIfEmpty();
/*  817 */       return new WrappedListIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<V> listIterator(int index) {
/*  822 */       refreshIfEmpty();
/*  823 */       return new WrappedListIterator(index);
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> subList(int fromIndex, int toIndex) {
/*  828 */       refreshIfEmpty();
/*  829 */       return AbstractMapBasedMultimap.this.wrapList(
/*  830 */           getKey(), 
/*  831 */           getListDelegate().subList(fromIndex, toIndex), 
/*  832 */           (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */     
/*      */     private class WrappedListIterator
/*      */       extends AbstractMapBasedMultimap<K, V>.WrappedCollection.WrappedIterator implements ListIterator<V> {
/*      */       WrappedListIterator() {}
/*      */       
/*      */       public WrappedListIterator(int index) {
/*  840 */         super(AbstractMapBasedMultimap.WrappedList.this.getListDelegate().listIterator(index));
/*      */       }
/*      */       
/*      */       private ListIterator<V> getDelegateListIterator() {
/*  844 */         return (ListIterator<V>)getDelegateIterator();
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean hasPrevious() {
/*  849 */         return getDelegateListIterator().hasPrevious();
/*      */       }
/*      */ 
/*      */       
/*      */       public V previous() {
/*  854 */         return getDelegateListIterator().previous();
/*      */       }
/*      */ 
/*      */       
/*      */       public int nextIndex() {
/*  859 */         return getDelegateListIterator().nextIndex();
/*      */       }
/*      */ 
/*      */       
/*      */       public int previousIndex() {
/*  864 */         return getDelegateListIterator().previousIndex();
/*      */       }
/*      */ 
/*      */       
/*      */       public void set(V value) {
/*  869 */         getDelegateListIterator().set(value);
/*      */       }
/*      */ 
/*      */       
/*      */       public void add(V value) {
/*  874 */         boolean wasEmpty = AbstractMapBasedMultimap.WrappedList.this.isEmpty();
/*  875 */         getDelegateListIterator().add(value);
/*  876 */         AbstractMapBasedMultimap.this.totalSize++;
/*  877 */         if (wasEmpty) {
/*  878 */           AbstractMapBasedMultimap.WrappedList.this.addToMap();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class RandomAccessWrappedList
/*      */     extends WrappedList
/*      */     implements RandomAccess
/*      */   {
/*      */     RandomAccessWrappedList(AbstractMapBasedMultimap this$0, K key, List<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
/*  891 */       super(this$0, key, delegate, ancestor);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   Set<K> createKeySet() {
/*  897 */     return new KeySet(this.map);
/*      */   }
/*      */   
/*      */   final Set<K> createMaybeNavigableKeySet() {
/*  901 */     if (this.map instanceof NavigableMap)
/*  902 */       return new NavigableKeySet((NavigableMap<K, Collection<V>>)this.map); 
/*  903 */     if (this.map instanceof SortedMap) {
/*  904 */       return new SortedKeySet((SortedMap<K, Collection<V>>)this.map);
/*      */     }
/*  906 */     return new KeySet(this.map);
/*      */   }
/*      */   
/*      */   private class KeySet
/*      */     extends Maps.KeySet<K, Collection<V>>
/*      */   {
/*      */     KeySet(Map<K, Collection<V>> subMap) {
/*  913 */       super(subMap);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> iterator() {
/*  918 */       final Iterator<Map.Entry<K, Collection<V>>> entryIterator = map().entrySet().iterator();
/*  919 */       return new Iterator<K>()
/*      */         {
/*      */           Map.Entry<K, Collection<V>> entry;
/*      */           
/*      */           public boolean hasNext() {
/*  924 */             return entryIterator.hasNext();
/*      */           }
/*      */ 
/*      */           
/*      */           public K next() {
/*  929 */             this.entry = entryIterator.next();
/*  930 */             return this.entry.getKey();
/*      */           }
/*      */ 
/*      */           
/*      */           public void remove() {
/*  935 */             CollectPreconditions.checkRemove((this.entry != null));
/*  936 */             Collection<V> collection = this.entry.getValue();
/*  937 */             entryIterator.remove();
/*  938 */             AbstractMapBasedMultimap.this.totalSize -= collection.size();
/*  939 */             collection.clear();
/*  940 */             this.entry = null;
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Spliterator<K> spliterator() {
/*  949 */       return map().keySet().spliterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object key) {
/*  954 */       int count = 0;
/*  955 */       Collection<V> collection = map().remove(key);
/*  956 */       if (collection != null) {
/*  957 */         count = collection.size();
/*  958 */         collection.clear();
/*  959 */         AbstractMapBasedMultimap.this.totalSize -= count;
/*      */       } 
/*  961 */       return (count > 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  966 */       Iterators.clear(iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  971 */       return map().keySet().containsAll(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/*  976 */       return (this == object || map().keySet().equals(object));
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  981 */       return map().keySet().hashCode();
/*      */     }
/*      */   }
/*      */   
/*      */   private class SortedKeySet
/*      */     extends KeySet
/*      */     implements SortedSet<K> {
/*      */     SortedKeySet(SortedMap<K, Collection<V>> subMap) {
/*  989 */       super(subMap);
/*      */     }
/*      */     
/*      */     SortedMap<K, Collection<V>> sortedMap() {
/*  993 */       return (SortedMap<K, Collection<V>>)map();
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/*  998 */       return sortedMap().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public K first() {
/* 1003 */       return sortedMap().firstKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> headSet(K toElement) {
/* 1008 */       return new SortedKeySet(sortedMap().headMap(toElement));
/*      */     }
/*      */ 
/*      */     
/*      */     public K last() {
/* 1013 */       return sortedMap().lastKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> subSet(K fromElement, K toElement) {
/* 1018 */       return new SortedKeySet(sortedMap().subMap(fromElement, toElement));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> tailSet(K fromElement) {
/* 1023 */       return new SortedKeySet(sortedMap().tailMap(fromElement));
/*      */     }
/*      */   }
/*      */   
/*      */   class NavigableKeySet
/*      */     extends SortedKeySet implements NavigableSet<K> {
/*      */     NavigableKeySet(NavigableMap<K, Collection<V>> subMap) {
/* 1030 */       super(subMap);
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableMap<K, Collection<V>> sortedMap() {
/* 1035 */       return (NavigableMap<K, Collection<V>>)super.sortedMap();
/*      */     }
/*      */ 
/*      */     
/*      */     public K lower(K k) {
/* 1040 */       return sortedMap().lowerKey(k);
/*      */     }
/*      */ 
/*      */     
/*      */     public K floor(K k) {
/* 1045 */       return sortedMap().floorKey(k);
/*      */     }
/*      */ 
/*      */     
/*      */     public K ceiling(K k) {
/* 1050 */       return sortedMap().ceilingKey(k);
/*      */     }
/*      */ 
/*      */     
/*      */     public K higher(K k) {
/* 1055 */       return sortedMap().higherKey(k);
/*      */     }
/*      */ 
/*      */     
/*      */     public K pollFirst() {
/* 1060 */       return Iterators.pollNext(iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public K pollLast() {
/* 1065 */       return Iterators.pollNext(descendingIterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingSet() {
/* 1070 */       return new NavigableKeySet(sortedMap().descendingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> descendingIterator() {
/* 1075 */       return descendingSet().iterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> headSet(K toElement) {
/* 1080 */       return headSet(toElement, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> headSet(K toElement, boolean inclusive) {
/* 1085 */       return new NavigableKeySet(sortedMap().headMap(toElement, inclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> subSet(K fromElement, K toElement) {
/* 1090 */       return subSet(fromElement, true, toElement, false);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> subSet(K fromElement, boolean fromInclusive, K toElement, boolean toInclusive) {
/* 1096 */       return new NavigableKeySet(
/* 1097 */           sortedMap().subMap(fromElement, fromInclusive, toElement, toInclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> tailSet(K fromElement) {
/* 1102 */       return tailSet(fromElement, true);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> tailSet(K fromElement, boolean inclusive) {
/* 1107 */       return new NavigableKeySet(sortedMap().tailMap(fromElement, inclusive));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void removeValuesForKey(Object key) {
/* 1113 */     Collection<V> collection = Maps.<Collection<V>>safeRemove(this.map, key);
/*      */     
/* 1115 */     if (collection != null) {
/* 1116 */       int count = collection.size();
/* 1117 */       collection.clear();
/* 1118 */       this.totalSize -= count;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private abstract class Itr<T>
/*      */     implements Iterator<T>
/*      */   {
/* 1129 */     final Iterator<Map.Entry<K, Collection<V>>> keyIterator = AbstractMapBasedMultimap.this.map.entrySet().iterator();
/* 1130 */     K key = null;
/* 1131 */     Collection<V> collection = null;
/* 1132 */     Iterator<V> valueIterator = Iterators.emptyModifiableIterator();
/*      */ 
/*      */     
/*      */     abstract T output(K param1K, V param1V);
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1139 */       return (this.keyIterator.hasNext() || this.valueIterator.hasNext());
/*      */     }
/*      */ 
/*      */     
/*      */     public T next() {
/* 1144 */       if (!this.valueIterator.hasNext()) {
/* 1145 */         Map.Entry<K, Collection<V>> mapEntry = this.keyIterator.next();
/* 1146 */         this.key = mapEntry.getKey();
/* 1147 */         this.collection = mapEntry.getValue();
/* 1148 */         this.valueIterator = this.collection.iterator();
/*      */       } 
/* 1150 */       return output(this.key, this.valueIterator.next());
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1155 */       this.valueIterator.remove();
/* 1156 */       if (this.collection.isEmpty()) {
/* 1157 */         this.keyIterator.remove();
/*      */       }
/* 1159 */       AbstractMapBasedMultimap.this.totalSize--;
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
/*      */   public Collection<V> values() {
/* 1171 */     return super.values();
/*      */   }
/*      */ 
/*      */   
/*      */   Collection<V> createValues() {
/* 1176 */     return new AbstractMultimap.Values(this);
/*      */   }
/*      */ 
/*      */   
/*      */   Iterator<V> valueIterator() {
/* 1181 */     return new Itr<V>(this)
/*      */       {
/*      */         V output(K key, V value) {
/* 1184 */           return value;
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   Spliterator<V> valueSpliterator() {
/* 1191 */     return CollectSpliterators.flatMap(this.map
/* 1192 */         .values().spliterator(), Collection::spliterator, 64, size());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Multiset<K> createKeys() {
/* 1203 */     return new Multimaps.Keys<>(this);
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
/*      */   public Collection<Map.Entry<K, V>> entries() {
/* 1217 */     return super.entries();
/*      */   }
/*      */ 
/*      */   
/*      */   Collection<Map.Entry<K, V>> createEntries() {
/* 1222 */     if (this instanceof SetMultimap) {
/* 1223 */       return new AbstractMultimap.EntrySet(this);
/*      */     }
/* 1225 */     return new AbstractMultimap.Entries(this);
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
/*      */   Iterator<Map.Entry<K, V>> entryIterator() {
/* 1239 */     return new Itr<Map.Entry<K, V>>(this)
/*      */       {
/*      */         Map.Entry<K, V> output(K key, V value) {
/* 1242 */           return Maps.immutableEntry(key, value);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 1249 */     return CollectSpliterators.flatMap(this.map
/* 1250 */         .entrySet().spliterator(), keyToValueCollectionEntry -> { K key = (K)keyToValueCollectionEntry.getKey(); Collection<V> valueCollection = (Collection<V>)keyToValueCollectionEntry.getValue(); return CollectSpliterators.map(valueCollection.spliterator(), ()); }64, 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1258 */         size());
/*      */   }
/*      */ 
/*      */   
/*      */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 1263 */     Preconditions.checkNotNull(action);
/* 1264 */     this.map.forEach((key, valueCollection) -> valueCollection.forEach(()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   Map<K, Collection<V>> createAsMap() {
/* 1270 */     return new AsMap(this.map);
/*      */   }
/*      */   
/*      */   final Map<K, Collection<V>> createMaybeNavigableAsMap() {
/* 1274 */     if (this.map instanceof NavigableMap)
/* 1275 */       return new NavigableAsMap((NavigableMap<K, Collection<V>>)this.map); 
/* 1276 */     if (this.map instanceof SortedMap) {
/* 1277 */       return new SortedAsMap((SortedMap<K, Collection<V>>)this.map);
/*      */     }
/* 1279 */     return new AsMap(this.map);
/*      */   }
/*      */ 
/*      */   
/*      */   abstract Collection<V> createCollection();
/*      */ 
/*      */   
/*      */   private class AsMap
/*      */     extends Maps.ViewCachingAbstractMap<K, Collection<V>>
/*      */   {
/*      */     final transient Map<K, Collection<V>> submap;
/*      */     
/*      */     AsMap(Map<K, Collection<V>> submap) {
/* 1292 */       this.submap = submap;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, Collection<V>>> createEntrySet() {
/* 1297 */       return new AsMapEntries();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 1304 */       return Maps.safeContainsKey(this.submap, key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> get(Object key) {
/* 1309 */       Collection<V> collection = Maps.<Collection<V>>safeGet(this.submap, key);
/* 1310 */       if (collection == null) {
/* 1311 */         return null;
/*      */       }
/*      */       
/* 1314 */       K k = (K)key;
/* 1315 */       return AbstractMapBasedMultimap.this.wrapCollection(k, collection);
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 1320 */       return AbstractMapBasedMultimap.this.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1325 */       return this.submap.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> remove(Object key) {
/* 1330 */       Collection<V> collection = this.submap.remove(key);
/* 1331 */       if (collection == null) {
/* 1332 */         return null;
/*      */       }
/*      */       
/* 1335 */       Collection<V> output = AbstractMapBasedMultimap.this.createCollection();
/* 1336 */       output.addAll(collection);
/* 1337 */       AbstractMapBasedMultimap.this.totalSize -= collection.size();
/* 1338 */       collection.clear();
/* 1339 */       return output;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/* 1344 */       return (this == object || this.submap.equals(object));
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1349 */       return this.submap.hashCode();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1354 */       return this.submap.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1359 */       if (this.submap == AbstractMapBasedMultimap.this.map) {
/* 1360 */         AbstractMapBasedMultimap.this.clear();
/*      */       } else {
/* 1362 */         Iterators.clear(new AsMapIterator());
/*      */       } 
/*      */     }
/*      */     
/*      */     Map.Entry<K, Collection<V>> wrapEntry(Map.Entry<K, Collection<V>> entry) {
/* 1367 */       K key = entry.getKey();
/* 1368 */       return Maps.immutableEntry(key, AbstractMapBasedMultimap.this.wrapCollection(key, entry.getValue()));
/*      */     }
/*      */     
/*      */     class AsMapEntries
/*      */       extends Maps.EntrySet<K, Collection<V>>
/*      */     {
/*      */       Map<K, Collection<V>> map() {
/* 1375 */         return AbstractMapBasedMultimap.AsMap.this;
/*      */       }
/*      */ 
/*      */       
/*      */       public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/* 1380 */         return new AbstractMapBasedMultimap.AsMap.AsMapIterator();
/*      */       }
/*      */ 
/*      */       
/*      */       public Spliterator<Map.Entry<K, Collection<V>>> spliterator() {
/* 1385 */         return CollectSpliterators.map(AbstractMapBasedMultimap.AsMap.this.submap.entrySet().spliterator(), AbstractMapBasedMultimap.AsMap.this::wrapEntry);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public boolean contains(Object o) {
/* 1392 */         return Collections2.safeContains(AbstractMapBasedMultimap.AsMap.this.submap.entrySet(), o);
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean remove(Object o) {
/* 1397 */         if (!contains(o)) {
/* 1398 */           return false;
/*      */         }
/* 1400 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 1401 */         AbstractMapBasedMultimap.this.removeValuesForKey(entry.getKey());
/* 1402 */         return true;
/*      */       }
/*      */     }
/*      */     
/*      */     class AsMapIterator
/*      */       implements Iterator<Map.Entry<K, Collection<V>>> {
/* 1408 */       final Iterator<Map.Entry<K, Collection<V>>> delegateIterator = AbstractMapBasedMultimap.AsMap.this.submap.entrySet().iterator();
/*      */       
/*      */       Collection<V> collection;
/*      */       
/*      */       public boolean hasNext() {
/* 1413 */         return this.delegateIterator.hasNext();
/*      */       }
/*      */ 
/*      */       
/*      */       public Map.Entry<K, Collection<V>> next() {
/* 1418 */         Map.Entry<K, Collection<V>> entry = this.delegateIterator.next();
/* 1419 */         this.collection = entry.getValue();
/* 1420 */         return AbstractMapBasedMultimap.AsMap.this.wrapEntry(entry);
/*      */       }
/*      */ 
/*      */       
/*      */       public void remove() {
/* 1425 */         CollectPreconditions.checkRemove((this.collection != null));
/* 1426 */         this.delegateIterator.remove();
/* 1427 */         AbstractMapBasedMultimap.this.totalSize -= this.collection.size();
/* 1428 */         this.collection.clear();
/* 1429 */         this.collection = null;
/*      */       } }
/*      */   }
/*      */   
/*      */   private class SortedAsMap extends AsMap implements SortedMap<K, Collection<V>> {
/*      */     SortedSet<K> sortedKeySet;
/*      */     
/*      */     SortedAsMap(SortedMap<K, Collection<V>> submap) {
/* 1437 */       super(submap);
/*      */     }
/*      */     
/*      */     SortedMap<K, Collection<V>> sortedMap() {
/* 1441 */       return (SortedMap<K, Collection<V>>)this.submap;
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 1446 */       return sortedMap().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public K firstKey() {
/* 1451 */       return sortedMap().firstKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public K lastKey() {
/* 1456 */       return sortedMap().lastKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, Collection<V>> headMap(K toKey) {
/* 1461 */       return new SortedAsMap(sortedMap().headMap(toKey));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, Collection<V>> subMap(K fromKey, K toKey) {
/* 1466 */       return new SortedAsMap(sortedMap().subMap(fromKey, toKey));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, Collection<V>> tailMap(K fromKey) {
/* 1471 */       return new SortedAsMap(sortedMap().tailMap(fromKey));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SortedSet<K> keySet() {
/* 1480 */       SortedSet<K> result = this.sortedKeySet;
/* 1481 */       return (result == null) ? (this.sortedKeySet = createKeySet()) : result;
/*      */     }
/*      */ 
/*      */     
/*      */     SortedSet<K> createKeySet() {
/* 1486 */       return new AbstractMapBasedMultimap.SortedKeySet(sortedMap());
/*      */     }
/*      */   }
/*      */   
/*      */   class NavigableAsMap
/*      */     extends SortedAsMap implements NavigableMap<K, Collection<V>> {
/*      */     NavigableAsMap(NavigableMap<K, Collection<V>> submap) {
/* 1493 */       super(submap);
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableMap<K, Collection<V>> sortedMap() {
/* 1498 */       return (NavigableMap<K, Collection<V>>)super.sortedMap();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> lowerEntry(K key) {
/* 1503 */       Map.Entry<K, Collection<V>> entry = sortedMap().lowerEntry(key);
/* 1504 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public K lowerKey(K key) {
/* 1509 */       return sortedMap().lowerKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> floorEntry(K key) {
/* 1514 */       Map.Entry<K, Collection<V>> entry = sortedMap().floorEntry(key);
/* 1515 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public K floorKey(K key) {
/* 1520 */       return sortedMap().floorKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> ceilingEntry(K key) {
/* 1525 */       Map.Entry<K, Collection<V>> entry = sortedMap().ceilingEntry(key);
/* 1526 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public K ceilingKey(K key) {
/* 1531 */       return sortedMap().ceilingKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> higherEntry(K key) {
/* 1536 */       Map.Entry<K, Collection<V>> entry = sortedMap().higherEntry(key);
/* 1537 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public K higherKey(K key) {
/* 1542 */       return sortedMap().higherKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> firstEntry() {
/* 1547 */       Map.Entry<K, Collection<V>> entry = sortedMap().firstEntry();
/* 1548 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> lastEntry() {
/* 1553 */       Map.Entry<K, Collection<V>> entry = sortedMap().lastEntry();
/* 1554 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> pollFirstEntry() {
/* 1559 */       return pollAsMapEntry(entrySet().iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> pollLastEntry() {
/* 1564 */       return pollAsMapEntry(descendingMap().entrySet().iterator());
/*      */     }
/*      */     
/*      */     Map.Entry<K, Collection<V>> pollAsMapEntry(Iterator<Map.Entry<K, Collection<V>>> entryIterator) {
/* 1568 */       if (!entryIterator.hasNext()) {
/* 1569 */         return null;
/*      */       }
/* 1571 */       Map.Entry<K, Collection<V>> entry = entryIterator.next();
/* 1572 */       Collection<V> output = AbstractMapBasedMultimap.this.createCollection();
/* 1573 */       output.addAll(entry.getValue());
/* 1574 */       entryIterator.remove();
/* 1575 */       return Maps.immutableEntry(entry.getKey(), AbstractMapBasedMultimap.this.unmodifiableCollectionSubclass(output));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> descendingMap() {
/* 1580 */       return new NavigableAsMap(sortedMap().descendingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> keySet() {
/* 1585 */       return (NavigableSet<K>)super.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableSet<K> createKeySet() {
/* 1590 */       return new AbstractMapBasedMultimap.NavigableKeySet(sortedMap());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 1595 */       return keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingKeySet() {
/* 1600 */       return descendingMap().navigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> subMap(K fromKey, K toKey) {
/* 1605 */       return subMap(fromKey, true, toKey, false);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 1611 */       return new NavigableAsMap(sortedMap().subMap(fromKey, fromInclusive, toKey, toInclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> headMap(K toKey) {
/* 1616 */       return headMap(toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> headMap(K toKey, boolean inclusive) {
/* 1621 */       return new NavigableAsMap(sortedMap().headMap(toKey, inclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> tailMap(K fromKey) {
/* 1626 */       return tailMap(fromKey, true);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> tailMap(K fromKey, boolean inclusive) {
/* 1631 */       return new NavigableAsMap(sortedMap().tailMap(fromKey, inclusive));
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\AbstractMapBasedMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */