/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.common.base.Supplier;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*      */ import com.google.j2objc.annotations.Weak;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.SortedSet;
/*      */ import java.util.Spliterator;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.stream.Collector;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */ public final class Multimaps
/*      */ {
/*      */   public static <T, K, V, M extends Multimap<K, V>> Collector<T, ?, M> toMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, Supplier<M> multimapSupplier) {
/*  113 */     return CollectCollectors.toMultimap(keyFunction, valueFunction, multimapSupplier);
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
/*      */   @Beta
/*      */   public static <T, K, V, M extends Multimap<K, V>> Collector<T, ?, M> flatteningToMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valueFunction, Supplier<M> multimapSupplier) {
/*  155 */     return CollectCollectors.flatteningToMultimap(keyFunction, valueFunction, multimapSupplier);
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
/*      */   public static <K, V> Multimap<K, V> newMultimap(Map<K, Collection<V>> map, Supplier<? extends Collection<V>> factory) {
/*  196 */     return new CustomMultimap<>(map, factory);
/*      */   }
/*      */   private static class CustomMultimap<K, V> extends AbstractMapBasedMultimap<K, V> { transient Supplier<? extends Collection<V>> factory;
/*      */     @GwtIncompatible
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     CustomMultimap(Map<K, Collection<V>> map, Supplier<? extends Collection<V>> factory) {
/*  203 */       super(map);
/*  204 */       this.factory = (Supplier<? extends Collection<V>>)Preconditions.checkNotNull(factory);
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/*  209 */       return createMaybeNavigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Map<K, Collection<V>> createAsMap() {
/*  214 */       return createMaybeNavigableAsMap();
/*      */     }
/*      */ 
/*      */     
/*      */     protected Collection<V> createCollection() {
/*  219 */       return (Collection<V>)this.factory.get();
/*      */     }
/*      */ 
/*      */     
/*      */     <E> Collection<E> unmodifiableCollectionSubclass(Collection<E> collection) {
/*  224 */       if (collection instanceof NavigableSet)
/*  225 */         return Sets.unmodifiableNavigableSet((NavigableSet<E>)collection); 
/*  226 */       if (collection instanceof SortedSet)
/*  227 */         return Collections.unmodifiableSortedSet((SortedSet<E>)collection); 
/*  228 */       if (collection instanceof Set)
/*  229 */         return Collections.unmodifiableSet((Set<? extends E>)collection); 
/*  230 */       if (collection instanceof List) {
/*  231 */         return Collections.unmodifiableList((List<? extends E>)collection);
/*      */       }
/*  233 */       return Collections.unmodifiableCollection(collection);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Collection<V> wrapCollection(K key, Collection<V> collection) {
/*  239 */       if (collection instanceof List)
/*  240 */         return wrapList(key, (List<V>)collection, null); 
/*  241 */       if (collection instanceof NavigableSet)
/*  242 */         return new AbstractMapBasedMultimap.WrappedNavigableSet(this, key, (NavigableSet<V>)collection, null); 
/*  243 */       if (collection instanceof SortedSet)
/*  244 */         return new AbstractMapBasedMultimap.WrappedSortedSet(this, key, (SortedSet<V>)collection, null); 
/*  245 */       if (collection instanceof Set) {
/*  246 */         return new AbstractMapBasedMultimap.WrappedSet(this, key, (Set<V>)collection);
/*      */       }
/*  248 */       return new AbstractMapBasedMultimap.WrappedCollection(this, key, collection, null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  258 */       stream.defaultWriteObject();
/*  259 */       stream.writeObject(this.factory);
/*  260 */       stream.writeObject(backingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  266 */       stream.defaultReadObject();
/*  267 */       this.factory = (Supplier<? extends Collection<V>>)stream.readObject();
/*  268 */       Map<K, Collection<V>> map = (Map<K, Collection<V>>)stream.readObject();
/*  269 */       setMap(map);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ListMultimap<K, V> newListMultimap(Map<K, Collection<V>> map, Supplier<? extends List<V>> factory) {
/*  308 */     return new CustomListMultimap<>(map, factory);
/*      */   }
/*      */   private static class CustomListMultimap<K, V> extends AbstractListMultimap<K, V> { transient Supplier<? extends List<V>> factory;
/*      */     @GwtIncompatible
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     CustomListMultimap(Map<K, Collection<V>> map, Supplier<? extends List<V>> factory) {
/*  315 */       super(map);
/*  316 */       this.factory = (Supplier<? extends List<V>>)Preconditions.checkNotNull(factory);
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/*  321 */       return createMaybeNavigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Map<K, Collection<V>> createAsMap() {
/*  326 */       return createMaybeNavigableAsMap();
/*      */     }
/*      */ 
/*      */     
/*      */     protected List<V> createCollection() {
/*  331 */       return (List<V>)this.factory.get();
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  337 */       stream.defaultWriteObject();
/*  338 */       stream.writeObject(this.factory);
/*  339 */       stream.writeObject(backingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  345 */       stream.defaultReadObject();
/*  346 */       this.factory = (Supplier<? extends List<V>>)stream.readObject();
/*  347 */       Map<K, Collection<V>> map = (Map<K, Collection<V>>)stream.readObject();
/*  348 */       setMap(map);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SetMultimap<K, V> newSetMultimap(Map<K, Collection<V>> map, Supplier<? extends Set<V>> factory) {
/*  386 */     return new CustomSetMultimap<>(map, factory);
/*      */   }
/*      */   private static class CustomSetMultimap<K, V> extends AbstractSetMultimap<K, V> { transient Supplier<? extends Set<V>> factory;
/*      */     @GwtIncompatible
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     CustomSetMultimap(Map<K, Collection<V>> map, Supplier<? extends Set<V>> factory) {
/*  393 */       super(map);
/*  394 */       this.factory = (Supplier<? extends Set<V>>)Preconditions.checkNotNull(factory);
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/*  399 */       return createMaybeNavigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Map<K, Collection<V>> createAsMap() {
/*  404 */       return createMaybeNavigableAsMap();
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<V> createCollection() {
/*  409 */       return (Set<V>)this.factory.get();
/*      */     }
/*      */ 
/*      */     
/*      */     <E> Collection<E> unmodifiableCollectionSubclass(Collection<E> collection) {
/*  414 */       if (collection instanceof NavigableSet)
/*  415 */         return Sets.unmodifiableNavigableSet((NavigableSet<E>)collection); 
/*  416 */       if (collection instanceof SortedSet) {
/*  417 */         return Collections.unmodifiableSortedSet((SortedSet<E>)collection);
/*      */       }
/*  419 */       return Collections.unmodifiableSet((Set<? extends E>)collection);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Collection<V> wrapCollection(K key, Collection<V> collection) {
/*  425 */       if (collection instanceof NavigableSet)
/*  426 */         return new AbstractMapBasedMultimap.WrappedNavigableSet(this, key, (NavigableSet<V>)collection, null); 
/*  427 */       if (collection instanceof SortedSet) {
/*  428 */         return new AbstractMapBasedMultimap.WrappedSortedSet(this, key, (SortedSet<V>)collection, null);
/*      */       }
/*  430 */       return new AbstractMapBasedMultimap.WrappedSet(this, key, (Set<V>)collection);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  437 */       stream.defaultWriteObject();
/*  438 */       stream.writeObject(this.factory);
/*  439 */       stream.writeObject(backingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  445 */       stream.defaultReadObject();
/*  446 */       this.factory = (Supplier<? extends Set<V>>)stream.readObject();
/*  447 */       Map<K, Collection<V>> map = (Map<K, Collection<V>>)stream.readObject();
/*  448 */       setMap(map);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedSetMultimap<K, V> newSortedSetMultimap(Map<K, Collection<V>> map, Supplier<? extends SortedSet<V>> factory) {
/*  486 */     return new CustomSortedSetMultimap<>(map, factory);
/*      */   }
/*      */   private static class CustomSortedSetMultimap<K, V> extends AbstractSortedSetMultimap<K, V> { transient Supplier<? extends SortedSet<V>> factory;
/*      */     transient Comparator<? super V> valueComparator;
/*      */     @GwtIncompatible
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     CustomSortedSetMultimap(Map<K, Collection<V>> map, Supplier<? extends SortedSet<V>> factory) {
/*  494 */       super(map);
/*  495 */       this.factory = (Supplier<? extends SortedSet<V>>)Preconditions.checkNotNull(factory);
/*  496 */       this.valueComparator = ((SortedSet<V>)factory.get()).comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/*  501 */       return createMaybeNavigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Map<K, Collection<V>> createAsMap() {
/*  506 */       return createMaybeNavigableAsMap();
/*      */     }
/*      */ 
/*      */     
/*      */     protected SortedSet<V> createCollection() {
/*  511 */       return (SortedSet<V>)this.factory.get();
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super V> valueComparator() {
/*  516 */       return this.valueComparator;
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  522 */       stream.defaultWriteObject();
/*  523 */       stream.writeObject(this.factory);
/*  524 */       stream.writeObject(backingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  530 */       stream.defaultReadObject();
/*  531 */       this.factory = (Supplier<? extends SortedSet<V>>)stream.readObject();
/*  532 */       this.valueComparator = ((SortedSet<V>)this.factory.get()).comparator();
/*  533 */       Map<K, Collection<V>> map = (Map<K, Collection<V>>)stream.readObject();
/*  534 */       setMap(map);
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
/*      */   @CanIgnoreReturnValue
/*      */   public static <K, V, M extends Multimap<K, V>> M invertFrom(Multimap<? extends V, ? extends K> source, M dest) {
/*  555 */     Preconditions.checkNotNull(dest);
/*  556 */     for (Map.Entry<? extends V, ? extends K> entry : source.entries()) {
/*  557 */       dest.put(entry.getValue(), entry.getKey());
/*      */     }
/*  559 */     return dest;
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
/*      */   public static <K, V> Multimap<K, V> synchronizedMultimap(Multimap<K, V> multimap) {
/*  595 */     return Synchronized.multimap(multimap, null);
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
/*      */   public static <K, V> Multimap<K, V> unmodifiableMultimap(Multimap<K, V> delegate) {
/*  610 */     if (delegate instanceof UnmodifiableMultimap || delegate instanceof ImmutableMultimap) {
/*  611 */       return delegate;
/*      */     }
/*  613 */     return new UnmodifiableMultimap<>(delegate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <K, V> Multimap<K, V> unmodifiableMultimap(ImmutableMultimap<K, V> delegate) {
/*  624 */     return (Multimap<K, V>)Preconditions.checkNotNull(delegate);
/*      */   }
/*      */   
/*      */   private static class UnmodifiableMultimap<K, V>
/*      */     extends ForwardingMultimap<K, V>
/*      */     implements Serializable {
/*      */     final Multimap<K, V> delegate;
/*      */     @LazyInit
/*      */     transient Collection<Map.Entry<K, V>> entries;
/*      */     @LazyInit
/*      */     transient Multiset<K> keys;
/*      */     
/*      */     UnmodifiableMultimap(Multimap<K, V> delegate) {
/*  637 */       this.delegate = (Multimap<K, V>)Preconditions.checkNotNull(delegate);
/*      */     } @LazyInit
/*      */     transient Set<K> keySet; @LazyInit
/*      */     transient Collection<V> values; @LazyInit
/*      */     transient Map<K, Collection<V>> map; private static final long serialVersionUID = 0L; protected Multimap<K, V> delegate() {
/*  642 */       return this.delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  647 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<K, Collection<V>> asMap() {
/*  652 */       Map<K, Collection<V>> result = this.map;
/*  653 */       if (result == null)
/*      */       {
/*      */         
/*  656 */         result = this.map = Collections.<K, V>unmodifiableMap(
/*  657 */             Maps.transformValues(this.delegate
/*  658 */               .asMap(), new Function<Collection<V>, Collection<V>>(this)
/*      */               {
/*      */                 public Collection<V> apply(Collection<V> collection)
/*      */                 {
/*  662 */                   return Multimaps.unmodifiableValueCollection(collection);
/*      */                 }
/*      */               }));
/*      */       }
/*  666 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<Map.Entry<K, V>> entries() {
/*  671 */       Collection<Map.Entry<K, V>> result = this.entries;
/*  672 */       if (result == null) {
/*  673 */         this.entries = result = Multimaps.unmodifiableEntries(this.delegate.entries());
/*      */       }
/*  675 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(BiConsumer<? super K, ? super V> consumer) {
/*  680 */       this.delegate.forEach((BiConsumer<? super K, ? super V>)Preconditions.checkNotNull(consumer));
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> get(K key) {
/*  685 */       return Multimaps.unmodifiableValueCollection(this.delegate.get(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public Multiset<K> keys() {
/*  690 */       Multiset<K> result = this.keys;
/*  691 */       if (result == null) {
/*  692 */         this.keys = result = Multisets.unmodifiableMultiset(this.delegate.keys());
/*      */       }
/*  694 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/*  699 */       Set<K> result = this.keySet;
/*  700 */       if (result == null) {
/*  701 */         this.keySet = result = Collections.unmodifiableSet(this.delegate.keySet());
/*      */       }
/*  703 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean put(K key, V value) {
/*  708 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(K key, Iterable<? extends V> values) {
/*  713 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/*  718 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object key, Object value) {
/*  723 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> removeAll(Object key) {
/*  728 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
/*  733 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/*  738 */       Collection<V> result = this.values;
/*  739 */       if (result == null) {
/*  740 */         this.values = result = Collections.unmodifiableCollection(this.delegate.values());
/*      */       }
/*  742 */       return result;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class UnmodifiableListMultimap<K, V>
/*      */     extends UnmodifiableMultimap<K, V> implements ListMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableListMultimap(ListMultimap<K, V> delegate) {
/*  751 */       super(delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     public ListMultimap<K, V> delegate() {
/*  756 */       return (ListMultimap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> get(K key) {
/*  761 */       return Collections.unmodifiableList(delegate().get(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> removeAll(Object key) {
/*  766 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> replaceValues(K key, Iterable<? extends V> values) {
/*  771 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class UnmodifiableSetMultimap<K, V>
/*      */     extends UnmodifiableMultimap<K, V> implements SetMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableSetMultimap(SetMultimap<K, V> delegate) {
/*  780 */       super(delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SetMultimap<K, V> delegate() {
/*  785 */       return (SetMultimap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<V> get(K key) {
/*  794 */       return Collections.unmodifiableSet(delegate().get(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entries() {
/*  799 */       return Maps.unmodifiableEntrySet(delegate().entries());
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> removeAll(Object key) {
/*  804 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> replaceValues(K key, Iterable<? extends V> values) {
/*  809 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class UnmodifiableSortedSetMultimap<K, V>
/*      */     extends UnmodifiableSetMultimap<K, V> implements SortedSetMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableSortedSetMultimap(SortedSetMultimap<K, V> delegate) {
/*  818 */       super(delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSetMultimap<K, V> delegate() {
/*  823 */       return (SortedSetMultimap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> get(K key) {
/*  828 */       return Collections.unmodifiableSortedSet(delegate().get(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> removeAll(Object key) {
/*  833 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> replaceValues(K key, Iterable<? extends V> values) {
/*  838 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super V> valueComparator() {
/*  843 */       return delegate().valueComparator();
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
/*      */   public static <K, V> SetMultimap<K, V> synchronizedSetMultimap(SetMultimap<K, V> multimap) {
/*  860 */     return Synchronized.setMultimap(multimap, null);
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
/*      */   public static <K, V> SetMultimap<K, V> unmodifiableSetMultimap(SetMultimap<K, V> delegate) {
/*  875 */     if (delegate instanceof UnmodifiableSetMultimap || delegate instanceof ImmutableSetMultimap) {
/*  876 */       return delegate;
/*      */     }
/*  878 */     return new UnmodifiableSetMultimap<>(delegate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <K, V> SetMultimap<K, V> unmodifiableSetMultimap(ImmutableSetMultimap<K, V> delegate) {
/*  890 */     return (SetMultimap<K, V>)Preconditions.checkNotNull(delegate);
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
/*      */   public static <K, V> SortedSetMultimap<K, V> synchronizedSortedSetMultimap(SortedSetMultimap<K, V> multimap) {
/*  906 */     return Synchronized.sortedSetMultimap(multimap, null);
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
/*      */   public static <K, V> SortedSetMultimap<K, V> unmodifiableSortedSetMultimap(SortedSetMultimap<K, V> delegate) {
/*  922 */     if (delegate instanceof UnmodifiableSortedSetMultimap) {
/*  923 */       return delegate;
/*      */     }
/*  925 */     return new UnmodifiableSortedSetMultimap<>(delegate);
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
/*      */   public static <K, V> ListMultimap<K, V> synchronizedListMultimap(ListMultimap<K, V> multimap) {
/*  937 */     return Synchronized.listMultimap(multimap, null);
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
/*      */   public static <K, V> ListMultimap<K, V> unmodifiableListMultimap(ListMultimap<K, V> delegate) {
/*  952 */     if (delegate instanceof UnmodifiableListMultimap || delegate instanceof ImmutableListMultimap) {
/*  953 */       return delegate;
/*      */     }
/*  955 */     return new UnmodifiableListMultimap<>(delegate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <K, V> ListMultimap<K, V> unmodifiableListMultimap(ImmutableListMultimap<K, V> delegate) {
/*  967 */     return (ListMultimap<K, V>)Preconditions.checkNotNull(delegate);
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
/*      */   private static <V> Collection<V> unmodifiableValueCollection(Collection<V> collection) {
/*  979 */     if (collection instanceof SortedSet)
/*  980 */       return Collections.unmodifiableSortedSet((SortedSet<V>)collection); 
/*  981 */     if (collection instanceof Set)
/*  982 */       return Collections.unmodifiableSet((Set<? extends V>)collection); 
/*  983 */     if (collection instanceof List) {
/*  984 */       return Collections.unmodifiableList((List<? extends V>)collection);
/*      */     }
/*  986 */     return Collections.unmodifiableCollection(collection);
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
/*      */   private static <K, V> Collection<Map.Entry<K, V>> unmodifiableEntries(Collection<Map.Entry<K, V>> entries) {
/*  999 */     if (entries instanceof Set) {
/* 1000 */       return Maps.unmodifiableEntrySet((Set<Map.Entry<K, V>>)entries);
/*      */     }
/* 1002 */     return new Maps.UnmodifiableEntries<>(Collections.unmodifiableCollection(entries));
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
/*      */   @Beta
/*      */   public static <K, V> Map<K, List<V>> asMap(ListMultimap<K, V> multimap) {
/* 1015 */     return (Map)multimap.asMap();
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
/*      */   @Beta
/*      */   public static <K, V> Map<K, Set<V>> asMap(SetMultimap<K, V> multimap) {
/* 1028 */     return (Map)multimap.asMap();
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
/*      */   @Beta
/*      */   public static <K, V> Map<K, SortedSet<V>> asMap(SortedSetMultimap<K, V> multimap) {
/* 1041 */     return (Map)multimap.asMap();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <K, V> Map<K, Collection<V>> asMap(Multimap<K, V> multimap) {
/* 1052 */     return multimap.asMap();
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
/*      */   public static <K, V> SetMultimap<K, V> forMap(Map<K, V> map) {
/* 1071 */     return new MapMultimap<>(map);
/*      */   }
/*      */   
/*      */   private static class MapMultimap<K, V>
/*      */     extends AbstractMultimap<K, V> implements SetMultimap<K, V>, Serializable {
/*      */     final Map<K, V> map;
/*      */     private static final long serialVersionUID = 7845222491160860175L;
/*      */     
/*      */     MapMultimap(Map<K, V> map) {
/* 1080 */       this.map = (Map<K, V>)Preconditions.checkNotNull(map);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1085 */       return this.map.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 1090 */       return this.map.containsKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsValue(Object value) {
/* 1095 */       return this.map.containsValue(value);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsEntry(Object key, Object value) {
/* 1100 */       return this.map.entrySet().contains(Maps.immutableEntry(key, value));
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> get(final K key) {
/* 1105 */       return new Sets.ImprovedAbstractSet<V>()
/*      */         {
/*      */           public Iterator<V> iterator() {
/* 1108 */             return new Iterator<V>()
/*      */               {
/*      */                 int i;
/*      */                 
/*      */                 public boolean hasNext() {
/* 1113 */                   return (this.i == 0 && Multimaps.MapMultimap.this.map.containsKey(key));
/*      */                 }
/*      */ 
/*      */                 
/*      */                 public V next() {
/* 1118 */                   if (!hasNext()) {
/* 1119 */                     throw new NoSuchElementException();
/*      */                   }
/* 1121 */                   this.i++;
/* 1122 */                   return (V)Multimaps.MapMultimap.this.map.get(key);
/*      */                 }
/*      */ 
/*      */                 
/*      */                 public void remove() {
/* 1127 */                   CollectPreconditions.checkRemove((this.i == 1));
/* 1128 */                   this.i = -1;
/* 1129 */                   Multimaps.MapMultimap.this.map.remove(key);
/*      */                 }
/*      */               };
/*      */           }
/*      */ 
/*      */           
/*      */           public int size() {
/* 1136 */             return Multimaps.MapMultimap.this.map.containsKey(key) ? 1 : 0;
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean put(K key, V value) {
/* 1143 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(K key, Iterable<? extends V> values) {
/* 1148 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/* 1153 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> replaceValues(K key, Iterable<? extends V> values) {
/* 1158 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object key, Object value) {
/* 1163 */       return this.map.entrySet().remove(Maps.immutableEntry(key, value));
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> removeAll(Object key) {
/* 1168 */       Set<V> values = new HashSet<>(2);
/* 1169 */       if (!this.map.containsKey(key)) {
/* 1170 */         return values;
/*      */       }
/* 1172 */       values.add(this.map.remove(key));
/* 1173 */       return values;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1178 */       this.map.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/* 1183 */       return this.map.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Collection<V> createValues() {
/* 1188 */       return this.map.values();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entries() {
/* 1193 */       return this.map.entrySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Collection<Map.Entry<K, V>> createEntries() {
/* 1198 */       throw new AssertionError("unreachable");
/*      */     }
/*      */ 
/*      */     
/*      */     Multiset<K> createKeys() {
/* 1203 */       return new Multimaps.Keys<>(this);
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V>> entryIterator() {
/* 1208 */       return this.map.entrySet().iterator();
/*      */     }
/*      */ 
/*      */     
/*      */     Map<K, Collection<V>> createAsMap() {
/* 1213 */       return new Multimaps.AsMap<>(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1218 */       return this.map.hashCode();
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
/*      */   public static <K, V1, V2> Multimap<K, V2> transformValues(Multimap<K, V1> fromMultimap, Function<? super V1, V2> function) {
/* 1267 */     Preconditions.checkNotNull(function);
/* 1268 */     Maps.EntryTransformer<K, V1, V2> transformer = Maps.asEntryTransformer(function);
/* 1269 */     return transformEntries(fromMultimap, transformer);
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
/*      */   public static <K, V1, V2> ListMultimap<K, V2> transformValues(ListMultimap<K, V1> fromMultimap, Function<? super V1, V2> function) {
/* 1314 */     Preconditions.checkNotNull(function);
/* 1315 */     Maps.EntryTransformer<K, V1, V2> transformer = Maps.asEntryTransformer(function);
/* 1316 */     return transformEntries(fromMultimap, transformer);
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
/*      */   public static <K, V1, V2> Multimap<K, V2> transformEntries(Multimap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1372 */     return new TransformedEntriesMultimap<>(fromMap, transformer);
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
/*      */   public static <K, V1, V2> ListMultimap<K, V2> transformEntries(ListMultimap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1425 */     return new TransformedEntriesListMultimap<>(fromMap, transformer);
/*      */   }
/*      */   
/*      */   private static class TransformedEntriesMultimap<K, V1, V2>
/*      */     extends AbstractMultimap<K, V2>
/*      */   {
/*      */     final Multimap<K, V1> fromMultimap;
/*      */     final Maps.EntryTransformer<? super K, ? super V1, V2> transformer;
/*      */     
/*      */     TransformedEntriesMultimap(Multimap<K, V1> fromMultimap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1435 */       this.fromMultimap = (Multimap<K, V1>)Preconditions.checkNotNull(fromMultimap);
/* 1436 */       this.transformer = (Maps.EntryTransformer<? super K, ? super V1, V2>)Preconditions.checkNotNull(transformer);
/*      */     }
/*      */     
/*      */     Collection<V2> transform(K key, Collection<V1> values) {
/* 1440 */       Function<? super V1, V2> function = Maps.asValueToValueFunction(this.transformer, key);
/* 1441 */       if (values instanceof List) {
/* 1442 */         return Lists.transform((List<V1>)values, function);
/*      */       }
/* 1444 */       return Collections2.transform(values, function);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Map<K, Collection<V2>> createAsMap() {
/* 1450 */       return Maps.transformEntries(this.fromMultimap
/* 1451 */           .asMap(), (Maps.EntryTransformer)new Maps.EntryTransformer<K, Collection<Collection<V1>>, Collection<Collection<V2>>>()
/*      */           {
/*      */             public Collection<V2> transformEntry(K key, Collection<V1> value)
/*      */             {
/* 1455 */               return Multimaps.TransformedEntriesMultimap.this.transform(key, value);
/*      */             }
/*      */           });
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1462 */       this.fromMultimap.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 1467 */       return this.fromMultimap.containsKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     Collection<Map.Entry<K, V2>> createEntries() {
/* 1472 */       return new AbstractMultimap.Entries(this);
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V2>> entryIterator() {
/* 1477 */       return Iterators.transform(this.fromMultimap
/* 1478 */           .entries().iterator(), Maps.asEntryToEntryFunction(this.transformer));
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V2> get(K key) {
/* 1483 */       return transform(key, this.fromMultimap.get(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 1488 */       return this.fromMultimap.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/* 1493 */       return this.fromMultimap.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Multiset<K> createKeys() {
/* 1498 */       return this.fromMultimap.keys();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean put(K key, V2 value) {
/* 1503 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(K key, Iterable<? extends V2> values) {
/* 1508 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(Multimap<? extends K, ? extends V2> multimap) {
/* 1513 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean remove(Object key, Object value) {
/* 1519 */       return get((K)key).remove(value);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Collection<V2> removeAll(Object key) {
/* 1525 */       return transform((K)key, this.fromMultimap.removeAll(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V2> replaceValues(K key, Iterable<? extends V2> values) {
/* 1530 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1535 */       return this.fromMultimap.size();
/*      */     }
/*      */ 
/*      */     
/*      */     Collection<V2> createValues() {
/* 1540 */       return Collections2.transform(this.fromMultimap
/* 1541 */           .entries(), Maps.asEntryToValueFunction(this.transformer));
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class TransformedEntriesListMultimap<K, V1, V2>
/*      */     extends TransformedEntriesMultimap<K, V1, V2>
/*      */     implements ListMultimap<K, V2>
/*      */   {
/*      */     TransformedEntriesListMultimap(ListMultimap<K, V1> fromMultimap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1550 */       super(fromMultimap, transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     List<V2> transform(K key, Collection<V1> values) {
/* 1555 */       return Lists.transform((List)values, Maps.asValueToValueFunction(this.transformer, key));
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V2> get(K key) {
/* 1560 */       return transform(key, this.fromMultimap.get(key));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public List<V2> removeAll(Object key) {
/* 1566 */       return transform((K)key, this.fromMultimap.removeAll(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V2> replaceValues(K key, Iterable<? extends V2> values) {
/* 1571 */       throw new UnsupportedOperationException();
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
/*      */   public static <K, V> ImmutableListMultimap<K, V> index(Iterable<V> values, Function<? super V, K> keyFunction) {
/* 1612 */     return index(values.iterator(), keyFunction);
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
/*      */   public static <K, V> ImmutableListMultimap<K, V> index(Iterator<V> values, Function<? super V, K> keyFunction) {
/* 1653 */     Preconditions.checkNotNull(keyFunction);
/* 1654 */     ImmutableListMultimap.Builder<K, V> builder = ImmutableListMultimap.builder();
/* 1655 */     while (values.hasNext()) {
/* 1656 */       V value = values.next();
/* 1657 */       Preconditions.checkNotNull(value, values);
/* 1658 */       builder.put((K)keyFunction.apply(value), value);
/*      */     } 
/* 1660 */     return builder.build();
/*      */   }
/*      */   
/*      */   static class Keys<K, V> extends AbstractMultiset<K> { @Weak
/*      */     final Multimap<K, V> multimap;
/*      */     
/*      */     Keys(Multimap<K, V> multimap) {
/* 1667 */       this.multimap = multimap;
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Multiset.Entry<K>> entryIterator() {
/* 1672 */       return new TransformedIterator<Map.Entry<K, Collection<V>>, Multiset.Entry<K>>(this, this.multimap
/* 1673 */           .asMap().entrySet().iterator())
/*      */         {
/*      */           Multiset.Entry<K> transform(final Map.Entry<K, Collection<V>> backingEntry) {
/* 1676 */             return new Multisets.AbstractEntry<K>(this)
/*      */               {
/*      */                 public K getElement() {
/* 1679 */                   return (K)backingEntry.getKey();
/*      */                 }
/*      */ 
/*      */                 
/*      */                 public int getCount() {
/* 1684 */                   return ((Collection)backingEntry.getValue()).size();
/*      */                 }
/*      */               };
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public Spliterator<K> spliterator() {
/* 1693 */       return CollectSpliterators.map(this.multimap.entries().spliterator(), Map.Entry::getKey);
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> consumer) {
/* 1698 */       Preconditions.checkNotNull(consumer);
/* 1699 */       this.multimap.entries().forEach(entry -> consumer.accept(entry.getKey()));
/*      */     }
/*      */ 
/*      */     
/*      */     int distinctElements() {
/* 1704 */       return this.multimap.asMap().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1709 */       return this.multimap.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object element) {
/* 1714 */       return this.multimap.containsKey(element);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> iterator() {
/* 1719 */       return Maps.keyIterator(this.multimap.entries().iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public int count(Object element) {
/* 1724 */       Collection<V> values = Maps.<Collection<V>>safeGet(this.multimap.asMap(), element);
/* 1725 */       return (values == null) ? 0 : values.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public int remove(Object element, int occurrences) {
/* 1730 */       CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/* 1731 */       if (occurrences == 0) {
/* 1732 */         return count(element);
/*      */       }
/*      */       
/* 1735 */       Collection<V> values = Maps.<Collection<V>>safeGet(this.multimap.asMap(), element);
/*      */       
/* 1737 */       if (values == null) {
/* 1738 */         return 0;
/*      */       }
/*      */       
/* 1741 */       int oldCount = values.size();
/* 1742 */       if (occurrences >= oldCount) {
/* 1743 */         values.clear();
/*      */       } else {
/* 1745 */         Iterator<V> iterator = values.iterator();
/* 1746 */         for (int i = 0; i < occurrences; i++) {
/* 1747 */           iterator.next();
/* 1748 */           iterator.remove();
/*      */         } 
/*      */       } 
/* 1751 */       return oldCount;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1756 */       this.multimap.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> elementSet() {
/* 1761 */       return this.multimap.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<K> elementIterator() {
/* 1766 */       throw new AssertionError("should never be called");
/*      */     } }
/*      */ 
/*      */   
/*      */   static abstract class Entries<K, V>
/*      */     extends AbstractCollection<Map.Entry<K, V>>
/*      */   {
/*      */     abstract Multimap<K, V> multimap();
/*      */     
/*      */     public int size() {
/* 1776 */       return multimap().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1781 */       if (o instanceof Map.Entry) {
/* 1782 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 1783 */         return multimap().containsEntry(entry.getKey(), entry.getValue());
/*      */       } 
/* 1785 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 1790 */       if (o instanceof Map.Entry) {
/* 1791 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 1792 */         return multimap().remove(entry.getKey(), entry.getValue());
/*      */       } 
/* 1794 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1799 */       multimap().clear();
/*      */     }
/*      */   }
/*      */   
/*      */   static final class AsMap<K, V> extends Maps.ViewCachingAbstractMap<K, Collection<V>> {
/*      */     @Weak
/*      */     private final Multimap<K, V> multimap;
/*      */     
/*      */     AsMap(Multimap<K, V> multimap) {
/* 1808 */       this.multimap = (Multimap<K, V>)Preconditions.checkNotNull(multimap);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1813 */       return this.multimap.keySet().size();
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, Collection<V>>> createEntrySet() {
/* 1818 */       return new EntrySet();
/*      */     }
/*      */     
/*      */     void removeValuesForKey(Object key) {
/* 1822 */       this.multimap.keySet().remove(key);
/*      */     }
/*      */     
/*      */     class EntrySet
/*      */       extends Maps.EntrySet<K, Collection<V>>
/*      */     {
/*      */       Map<K, Collection<V>> map() {
/* 1829 */         return Multimaps.AsMap.this;
/*      */       }
/*      */ 
/*      */       
/*      */       public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/* 1834 */         return Maps.asMapEntryIterator(Multimaps.AsMap.this
/* 1835 */             .multimap.keySet(), new Function<K, Collection<V>>()
/*      */             {
/*      */               public Collection<V> apply(K key)
/*      */               {
/* 1839 */                 return Multimaps.AsMap.this.multimap.get(key);
/*      */               }
/*      */             });
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean remove(Object o) {
/* 1846 */         if (!contains(o)) {
/* 1847 */           return false;
/*      */         }
/* 1849 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 1850 */         Multimaps.AsMap.this.removeValuesForKey(entry.getKey());
/* 1851 */         return true;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Collection<V> get(Object key) {
/* 1858 */       return containsKey(key) ? this.multimap.get((K)key) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> remove(Object key) {
/* 1863 */       return containsKey(key) ? this.multimap.removeAll(key) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 1868 */       return this.multimap.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 1873 */       return this.multimap.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 1878 */       return this.multimap.containsKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1883 */       this.multimap.clear();
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
/*      */   public static <K, V> Multimap<K, V> filterKeys(Multimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 1916 */     if (unfiltered instanceof SetMultimap)
/* 1917 */       return filterKeys((SetMultimap<K, V>)unfiltered, keyPredicate); 
/* 1918 */     if (unfiltered instanceof ListMultimap)
/* 1919 */       return filterKeys((ListMultimap<K, V>)unfiltered, keyPredicate); 
/* 1920 */     if (unfiltered instanceof FilteredKeyMultimap) {
/* 1921 */       FilteredKeyMultimap<K, V> prev = (FilteredKeyMultimap<K, V>)unfiltered;
/* 1922 */       return new FilteredKeyMultimap<>(prev.unfiltered, 
/* 1923 */           Predicates.and(prev.keyPredicate, keyPredicate));
/* 1924 */     }  if (unfiltered instanceof FilteredMultimap) {
/* 1925 */       FilteredMultimap<K, V> prev = (FilteredMultimap<K, V>)unfiltered;
/* 1926 */       return filterFiltered(prev, (Predicate)Maps.keyPredicateOnEntries(keyPredicate));
/*      */     } 
/* 1928 */     return new FilteredKeyMultimap<>(unfiltered, keyPredicate);
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
/*      */   public static <K, V> SetMultimap<K, V> filterKeys(SetMultimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 1961 */     if (unfiltered instanceof FilteredKeySetMultimap) {
/* 1962 */       FilteredKeySetMultimap<K, V> prev = (FilteredKeySetMultimap<K, V>)unfiltered;
/* 1963 */       return new FilteredKeySetMultimap<>(prev
/* 1964 */           .unfiltered(), Predicates.and(prev.keyPredicate, keyPredicate));
/* 1965 */     }  if (unfiltered instanceof FilteredSetMultimap) {
/* 1966 */       FilteredSetMultimap<K, V> prev = (FilteredSetMultimap<K, V>)unfiltered;
/* 1967 */       return filterFiltered(prev, (Predicate)Maps.keyPredicateOnEntries(keyPredicate));
/*      */     } 
/* 1969 */     return new FilteredKeySetMultimap<>(unfiltered, keyPredicate);
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
/*      */   public static <K, V> ListMultimap<K, V> filterKeys(ListMultimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 2002 */     if (unfiltered instanceof FilteredKeyListMultimap) {
/* 2003 */       FilteredKeyListMultimap<K, V> prev = (FilteredKeyListMultimap<K, V>)unfiltered;
/* 2004 */       return new FilteredKeyListMultimap<>(prev
/* 2005 */           .unfiltered(), Predicates.and(prev.keyPredicate, keyPredicate));
/*      */     } 
/* 2007 */     return new FilteredKeyListMultimap<>(unfiltered, keyPredicate);
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
/*      */   public static <K, V> Multimap<K, V> filterValues(Multimap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2040 */     return filterEntries(unfiltered, (Predicate)Maps.valuePredicateOnEntries(valuePredicate));
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
/*      */   public static <K, V> SetMultimap<K, V> filterValues(SetMultimap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2072 */     return filterEntries(unfiltered, (Predicate)Maps.valuePredicateOnEntries(valuePredicate));
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
/*      */   public static <K, V> Multimap<K, V> filterEntries(Multimap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2102 */     Preconditions.checkNotNull(entryPredicate);
/* 2103 */     if (unfiltered instanceof SetMultimap) {
/* 2104 */       return filterEntries((SetMultimap<K, V>)unfiltered, entryPredicate);
/*      */     }
/* 2106 */     return (unfiltered instanceof FilteredMultimap) ? 
/* 2107 */       filterFiltered((FilteredMultimap<K, V>)unfiltered, entryPredicate) : 
/* 2108 */       new FilteredEntryMultimap<>((Multimap<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
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
/*      */   public static <K, V> SetMultimap<K, V> filterEntries(SetMultimap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2138 */     Preconditions.checkNotNull(entryPredicate);
/* 2139 */     return (unfiltered instanceof FilteredSetMultimap) ? 
/* 2140 */       filterFiltered((FilteredSetMultimap<K, V>)unfiltered, entryPredicate) : 
/* 2141 */       new FilteredEntrySetMultimap<>((SetMultimap<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
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
/*      */   private static <K, V> Multimap<K, V> filterFiltered(FilteredMultimap<K, V> multimap, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2153 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(multimap.entryPredicate(), entryPredicate);
/* 2154 */     return new FilteredEntryMultimap<>(multimap.unfiltered(), predicate);
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
/*      */   private static <K, V> SetMultimap<K, V> filterFiltered(FilteredSetMultimap<K, V> multimap, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2166 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(multimap.entryPredicate(), entryPredicate);
/* 2167 */     return new FilteredEntrySetMultimap<>(multimap.unfiltered(), predicate);
/*      */   }
/*      */   
/*      */   static boolean equalsImpl(Multimap<?, ?> multimap, Object object) {
/* 2171 */     if (object == multimap) {
/* 2172 */       return true;
/*      */     }
/* 2174 */     if (object instanceof Multimap) {
/* 2175 */       Multimap<?, ?> that = (Multimap<?, ?>)object;
/* 2176 */       return multimap.asMap().equals(that.asMap());
/*      */     } 
/* 2178 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\Multimaps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */