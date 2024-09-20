/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.common.math.IntMath;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.SortedSet;
/*      */ import java.util.TreeSet;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.CopyOnWriteArraySet;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Predicate;
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
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Sets
/*      */ {
/*      */   static abstract class ImprovedAbstractSet<E>
/*      */     extends AbstractSet<E>
/*      */   {
/*      */     public boolean removeAll(Collection<?> c) {
/*   79 */       return Sets.removeAllImpl(this, c);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*   84 */       return super.retainAll((Collection)Preconditions.checkNotNull(c));
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(E anElement, E... otherElements) {
/*  103 */     return ImmutableEnumSet.asImmutable(EnumSet.of(anElement, otherElements));
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(Iterable<E> elements) {
/*  119 */     if (elements instanceof ImmutableEnumSet)
/*  120 */       return (ImmutableEnumSet)elements; 
/*  121 */     if (elements instanceof Collection) {
/*  122 */       Collection<E> collection = (Collection<E>)elements;
/*  123 */       if (collection.isEmpty()) {
/*  124 */         return ImmutableSet.of();
/*      */       }
/*  126 */       return ImmutableEnumSet.asImmutable(EnumSet.copyOf(collection));
/*      */     } 
/*      */     
/*  129 */     Iterator<E> itr = elements.iterator();
/*  130 */     if (itr.hasNext()) {
/*  131 */       EnumSet<E> enumSet = EnumSet.of(itr.next());
/*  132 */       Iterators.addAll(enumSet, itr);
/*  133 */       return ImmutableEnumSet.asImmutable(enumSet);
/*      */     } 
/*  135 */     return ImmutableSet.of();
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
/*      */   public static <E extends Enum<E>> Collector<E, ?, ImmutableSet<E>> toImmutableEnumSet() {
/*  149 */     return CollectCollectors.toImmutableEnumSet();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E extends Enum<E>> EnumSet<E> newEnumSet(Iterable<E> iterable, Class<E> elementType) {
/*  159 */     EnumSet<E> set = EnumSet.noneOf(elementType);
/*  160 */     Iterables.addAll(set, iterable);
/*  161 */     return set;
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
/*      */   public static <E> HashSet<E> newHashSet() {
/*  179 */     return new HashSet<>();
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
/*      */   public static <E> HashSet<E> newHashSet(E... elements) {
/*  196 */     HashSet<E> set = newHashSetWithExpectedSize(elements.length);
/*  197 */     Collections.addAll(set, elements);
/*  198 */     return set;
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
/*      */   public static <E> HashSet<E> newHashSet(Iterable<? extends E> elements) {
/*  220 */     return (elements instanceof Collection) ? 
/*  221 */       new HashSet<>((Collection<? extends E>)elements) : 
/*  222 */       newHashSet(elements.iterator());
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
/*      */   public static <E> HashSet<E> newHashSet(Iterator<? extends E> elements) {
/*  238 */     HashSet<E> set = newHashSet();
/*  239 */     Iterators.addAll(set, elements);
/*  240 */     return set;
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
/*      */   public static <E> HashSet<E> newHashSetWithExpectedSize(int expectedSize) {
/*  256 */     return new HashSet<>(Maps.capacity(expectedSize));
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
/*      */   public static <E> Set<E> newConcurrentHashSet() {
/*  270 */     return Collections.newSetFromMap(new ConcurrentHashMap<>());
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
/*      */   public static <E> Set<E> newConcurrentHashSet(Iterable<? extends E> elements) {
/*  287 */     Set<E> set = newConcurrentHashSet();
/*  288 */     Iterables.addAll(set, elements);
/*  289 */     return set;
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
/*      */   public static <E> LinkedHashSet<E> newLinkedHashSet() {
/*  306 */     return new LinkedHashSet<>();
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
/*      */   public static <E> LinkedHashSet<E> newLinkedHashSet(Iterable<? extends E> elements) {
/*  325 */     if (elements instanceof Collection) {
/*  326 */       return new LinkedHashSet<>((Collection<? extends E>)elements);
/*      */     }
/*  328 */     LinkedHashSet<E> set = newLinkedHashSet();
/*  329 */     Iterables.addAll(set, elements);
/*  330 */     return set;
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
/*      */   public static <E> LinkedHashSet<E> newLinkedHashSetWithExpectedSize(int expectedSize) {
/*  346 */     return new LinkedHashSet<>(Maps.capacity(expectedSize));
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
/*      */   public static <E extends Comparable> TreeSet<E> newTreeSet() {
/*  364 */     return new TreeSet<>();
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
/*      */   public static <E extends Comparable> TreeSet<E> newTreeSet(Iterable<? extends E> elements) {
/*  389 */     TreeSet<E> set = newTreeSet();
/*  390 */     Iterables.addAll(set, elements);
/*  391 */     return set;
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
/*      */   public static <E> TreeSet<E> newTreeSet(Comparator<? super E> comparator) {
/*  411 */     return new TreeSet<>((Comparator<? super E>)Preconditions.checkNotNull(comparator));
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
/*      */   public static <E> Set<E> newIdentityHashSet() {
/*  424 */     return Collections.newSetFromMap(Maps.newIdentityHashMap());
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
/*      */   public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet() {
/*  438 */     return new CopyOnWriteArraySet<>();
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
/*      */   @GwtIncompatible
/*      */   public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet(Iterable<? extends E> elements) {
/*  455 */     Collection<? extends E> elementsCollection = (elements instanceof Collection) ? (Collection<? extends E>)elements : Lists.<E>newArrayList(elements);
/*  456 */     return new CopyOnWriteArraySet<>(elementsCollection);
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
/*      */   public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> collection) {
/*  473 */     if (collection instanceof EnumSet) {
/*  474 */       return EnumSet.complementOf((EnumSet<E>)collection);
/*      */     }
/*  476 */     Preconditions.checkArgument(
/*  477 */         !collection.isEmpty(), "collection is empty; use the other version of this method");
/*  478 */     Class<E> type = ((Enum<E>)collection.iterator().next()).getDeclaringClass();
/*  479 */     return makeComplementByHand(collection, type);
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
/*      */   public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> collection, Class<E> type) {
/*  494 */     Preconditions.checkNotNull(collection);
/*  495 */     return (collection instanceof EnumSet) ? 
/*  496 */       EnumSet.<E>complementOf((EnumSet<E>)collection) : 
/*  497 */       makeComplementByHand(collection, type);
/*      */   }
/*      */ 
/*      */   
/*      */   private static <E extends Enum<E>> EnumSet<E> makeComplementByHand(Collection<E> collection, Class<E> type) {
/*  502 */     EnumSet<E> result = EnumSet.allOf(type);
/*  503 */     result.removeAll(collection);
/*  504 */     return result;
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
/*      */   @Deprecated
/*      */   public static <E> Set<E> newSetFromMap(Map<E, Boolean> map) {
/*  538 */     return Collections.newSetFromMap(map);
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
/*      */   public static abstract class SetView<E>
/*      */     extends AbstractSet<E>
/*      */   {
/*      */     private SetView() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ImmutableSet<E> immutableCopy() {
/*  562 */       return ImmutableSet.copyOf(this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public <S extends Set<E>> S copyInto(S set) {
/*  576 */       set.addAll(this);
/*  577 */       return set;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     public final boolean add(E e) {
/*  590 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     public final boolean remove(Object object) {
/*  603 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     public final boolean addAll(Collection<? extends E> newElements) {
/*  616 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     public final boolean removeAll(Collection<?> oldElements) {
/*  629 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     public final boolean removeIf(Predicate<? super E> filter) {
/*  642 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     public final boolean retainAll(Collection<?> elementsToKeep) {
/*  655 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public final void clear() {
/*  667 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public abstract UnmodifiableIterator<E> iterator();
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
/*      */   public static <E> SetView<E> union(final Set<? extends E> set1, final Set<? extends E> set2) {
/*  690 */     Preconditions.checkNotNull(set1, "set1");
/*  691 */     Preconditions.checkNotNull(set2, "set2");
/*      */     
/*  693 */     return new SetView<E>()
/*      */       {
/*      */         public int size() {
/*  696 */           int size = set1.size();
/*  697 */           for (E e : set2) {
/*  698 */             if (!set1.contains(e)) {
/*  699 */               size++;
/*      */             }
/*      */           } 
/*  702 */           return size;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/*  707 */           return (set1.isEmpty() && set2.isEmpty());
/*      */         }
/*      */ 
/*      */         
/*      */         public UnmodifiableIterator<E> iterator() {
/*  712 */           return new AbstractIterator() {
/*  713 */               final Iterator<? extends E> itr1 = set1.iterator();
/*  714 */               final Iterator<? extends E> itr2 = set2.iterator();
/*      */ 
/*      */               
/*      */               protected E computeNext() {
/*  718 */                 if (this.itr1.hasNext()) {
/*  719 */                   return this.itr1.next();
/*      */                 }
/*  721 */                 while (this.itr2.hasNext()) {
/*  722 */                   E e = this.itr2.next();
/*  723 */                   if (!set1.contains(e)) {
/*  724 */                     return e;
/*      */                   }
/*      */                 } 
/*  727 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         public Stream<E> stream() {
/*  734 */           return Stream.concat(set1.stream(), set2.stream().filter(e -> !set1.contains(e)));
/*      */         }
/*      */ 
/*      */         
/*      */         public Stream<E> parallelStream() {
/*  739 */           return stream().parallel();
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean contains(Object object) {
/*  744 */           return (set1.contains(object) || set2.contains(object));
/*      */         }
/*      */ 
/*      */         
/*      */         public <S extends Set<E>> S copyInto(S set) {
/*  749 */           set.addAll(set1);
/*  750 */           set.addAll(set2);
/*  751 */           return set;
/*      */         }
/*      */ 
/*      */         
/*      */         public ImmutableSet<E> immutableCopy() {
/*  756 */           return (new ImmutableSet.Builder<>()).addAll(set1).addAll(set2).build();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> SetView<E> intersection(final Set<E> set1, final Set<?> set2) {
/*  789 */     Preconditions.checkNotNull(set1, "set1");
/*  790 */     Preconditions.checkNotNull(set2, "set2");
/*      */     
/*  792 */     return new SetView<E>()
/*      */       {
/*      */         public UnmodifiableIterator<E> iterator() {
/*  795 */           return new AbstractIterator() {
/*  796 */               final Iterator<E> itr = set1.iterator();
/*      */ 
/*      */               
/*      */               protected E computeNext() {
/*  800 */                 while (this.itr.hasNext()) {
/*  801 */                   E e = this.itr.next();
/*  802 */                   if (set2.contains(e)) {
/*  803 */                     return e;
/*      */                   }
/*      */                 } 
/*  806 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         public Stream<E> stream() {
/*  813 */           Objects.requireNonNull(set2); return set1.stream().filter(set2::contains);
/*      */         }
/*      */ 
/*      */         
/*      */         public Stream<E> parallelStream() {
/*  818 */           Objects.requireNonNull(set2); return set1.parallelStream().filter(set2::contains);
/*      */         }
/*      */ 
/*      */         
/*      */         public int size() {
/*  823 */           int size = 0;
/*  824 */           for (E e : set1) {
/*  825 */             if (set2.contains(e)) {
/*  826 */               size++;
/*      */             }
/*      */           } 
/*  829 */           return size;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/*  834 */           return Collections.disjoint(set2, set1);
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean contains(Object object) {
/*  839 */           return (set1.contains(object) && set2.contains(object));
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean containsAll(Collection<?> collection) {
/*  844 */           return (set1.containsAll(collection) && set2.containsAll(collection));
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
/*      */   public static <E> SetView<E> difference(final Set<E> set1, final Set<?> set2) {
/*  860 */     Preconditions.checkNotNull(set1, "set1");
/*  861 */     Preconditions.checkNotNull(set2, "set2");
/*      */     
/*  863 */     return new SetView<E>()
/*      */       {
/*      */         public UnmodifiableIterator<E> iterator() {
/*  866 */           return new AbstractIterator() {
/*  867 */               final Iterator<E> itr = set1.iterator();
/*      */ 
/*      */               
/*      */               protected E computeNext() {
/*  871 */                 while (this.itr.hasNext()) {
/*  872 */                   E e = this.itr.next();
/*  873 */                   if (!set2.contains(e)) {
/*  874 */                     return e;
/*      */                   }
/*      */                 } 
/*  877 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         public Stream<E> stream() {
/*  884 */           return set1.stream().filter(e -> !set2.contains(e));
/*      */         }
/*      */ 
/*      */         
/*      */         public Stream<E> parallelStream() {
/*  889 */           return set1.parallelStream().filter(e -> !set2.contains(e));
/*      */         }
/*      */ 
/*      */         
/*      */         public int size() {
/*  894 */           int size = 0;
/*  895 */           for (E e : set1) {
/*  896 */             if (!set2.contains(e)) {
/*  897 */               size++;
/*      */             }
/*      */           } 
/*  900 */           return size;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/*  905 */           return set2.containsAll(set1);
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean contains(Object element) {
/*  910 */           return (set1.contains(element) && !set2.contains(element));
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
/*      */   public static <E> SetView<E> symmetricDifference(final Set<? extends E> set1, final Set<? extends E> set2) {
/*  928 */     Preconditions.checkNotNull(set1, "set1");
/*  929 */     Preconditions.checkNotNull(set2, "set2");
/*      */     
/*  931 */     return new SetView<E>()
/*      */       {
/*      */         public UnmodifiableIterator<E> iterator() {
/*  934 */           final Iterator<? extends E> itr1 = set1.iterator();
/*  935 */           final Iterator<? extends E> itr2 = set2.iterator();
/*  936 */           return new AbstractIterator()
/*      */             {
/*      */               public E computeNext() {
/*  939 */                 while (itr1.hasNext()) {
/*  940 */                   E elem1 = itr1.next();
/*  941 */                   if (!set2.contains(elem1)) {
/*  942 */                     return elem1;
/*      */                   }
/*      */                 } 
/*  945 */                 while (itr2.hasNext()) {
/*  946 */                   E elem2 = itr2.next();
/*  947 */                   if (!set1.contains(elem2)) {
/*  948 */                     return elem2;
/*      */                   }
/*      */                 } 
/*  951 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         public int size() {
/*  958 */           int size = 0;
/*  959 */           for (E e : set1) {
/*  960 */             if (!set2.contains(e)) {
/*  961 */               size++;
/*      */             }
/*      */           } 
/*  964 */           for (E e : set2) {
/*  965 */             if (!set1.contains(e)) {
/*  966 */               size++;
/*      */             }
/*      */           } 
/*  969 */           return size;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/*  974 */           return set1.equals(set2);
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean contains(Object element) {
/*  979 */           return set1.contains(element) ^ set2.contains(element);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> Set<E> filter(Set<E> unfiltered, Predicate<? super E> predicate) {
/* 1012 */     if (unfiltered instanceof SortedSet) {
/* 1013 */       return filter((SortedSet<E>)unfiltered, predicate);
/*      */     }
/* 1015 */     if (unfiltered instanceof FilteredSet) {
/*      */ 
/*      */       
/* 1018 */       FilteredSet<E> filtered = (FilteredSet<E>)unfiltered;
/* 1019 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/* 1020 */       return new FilteredSet<>((Set<E>)filtered.unfiltered, combinedPredicate);
/*      */     } 
/*      */     
/* 1023 */     return new FilteredSet<>((Set<E>)Preconditions.checkNotNull(unfiltered), (Predicate<? super E>)Preconditions.checkNotNull(predicate));
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
/*      */   public static <E> SortedSet<E> filter(SortedSet<E> unfiltered, Predicate<? super E> predicate) {
/* 1051 */     if (unfiltered instanceof FilteredSet) {
/*      */ 
/*      */       
/* 1054 */       FilteredSet<E> filtered = (FilteredSet<E>)unfiltered;
/* 1055 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/* 1056 */       return new FilteredSortedSet<>((SortedSet<E>)filtered.unfiltered, combinedPredicate);
/*      */     } 
/*      */     
/* 1059 */     return new FilteredSortedSet<>((SortedSet<E>)Preconditions.checkNotNull(unfiltered), (Predicate<? super E>)Preconditions.checkNotNull(predicate));
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
/*      */   @GwtIncompatible
/*      */   public static <E> NavigableSet<E> filter(NavigableSet<E> unfiltered, Predicate<? super E> predicate) {
/* 1090 */     if (unfiltered instanceof FilteredSet) {
/*      */ 
/*      */       
/* 1093 */       FilteredSet<E> filtered = (FilteredSet<E>)unfiltered;
/* 1094 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/* 1095 */       return new FilteredNavigableSet<>((NavigableSet<E>)filtered.unfiltered, combinedPredicate);
/*      */     } 
/*      */     
/* 1098 */     return new FilteredNavigableSet<>((NavigableSet<E>)Preconditions.checkNotNull(unfiltered), (Predicate<? super E>)Preconditions.checkNotNull(predicate));
/*      */   }
/*      */   
/*      */   private static class FilteredSet<E> extends Collections2.FilteredCollection<E> implements Set<E> {
/*      */     FilteredSet(Set<E> unfiltered, Predicate<? super E> predicate) {
/* 1103 */       super(unfiltered, predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/* 1108 */       return Sets.equalsImpl(this, object);
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1113 */       return Sets.hashCodeImpl(this);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class FilteredSortedSet<E>
/*      */     extends FilteredSet<E> implements SortedSet<E> {
/*      */     FilteredSortedSet(SortedSet<E> unfiltered, Predicate<? super E> predicate) {
/* 1120 */       super(unfiltered, predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super E> comparator() {
/* 1125 */       return ((SortedSet<E>)this.unfiltered).comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> subSet(E fromElement, E toElement) {
/* 1130 */       return new FilteredSortedSet(((SortedSet<E>)this.unfiltered)
/* 1131 */           .subSet(fromElement, toElement), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> headSet(E toElement) {
/* 1136 */       return new FilteredSortedSet(((SortedSet<E>)this.unfiltered).headSet(toElement), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> tailSet(E fromElement) {
/* 1141 */       return new FilteredSortedSet(((SortedSet<E>)this.unfiltered).tailSet(fromElement), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public E first() {
/* 1146 */       return Iterators.find(this.unfiltered.iterator(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public E last() {
/* 1151 */       SortedSet<E> sortedUnfiltered = (SortedSet<E>)this.unfiltered;
/*      */       while (true) {
/* 1153 */         E element = sortedUnfiltered.last();
/* 1154 */         if (this.predicate.apply(element)) {
/* 1155 */           return element;
/*      */         }
/* 1157 */         sortedUnfiltered = sortedUnfiltered.headSet(element);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   private static class FilteredNavigableSet<E>
/*      */     extends FilteredSortedSet<E> implements NavigableSet<E> {
/*      */     FilteredNavigableSet(NavigableSet<E> unfiltered, Predicate<? super E> predicate) {
/* 1166 */       super(unfiltered, predicate);
/*      */     }
/*      */     
/*      */     NavigableSet<E> unfiltered() {
/* 1170 */       return (NavigableSet<E>)this.unfiltered;
/*      */     }
/*      */ 
/*      */     
/*      */     public E lower(E e) {
/* 1175 */       return Iterators.find(unfiltered().headSet(e, false).descendingIterator(), this.predicate, null);
/*      */     }
/*      */ 
/*      */     
/*      */     public E floor(E e) {
/* 1180 */       return Iterators.find(unfiltered().headSet(e, true).descendingIterator(), this.predicate, null);
/*      */     }
/*      */ 
/*      */     
/*      */     public E ceiling(E e) {
/* 1185 */       return Iterables.find(unfiltered().tailSet(e, true), this.predicate, null);
/*      */     }
/*      */ 
/*      */     
/*      */     public E higher(E e) {
/* 1190 */       return Iterables.find(unfiltered().tailSet(e, false), this.predicate, null);
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollFirst() {
/* 1195 */       return Iterables.removeFirstMatching(unfiltered(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollLast() {
/* 1200 */       return Iterables.removeFirstMatching(unfiltered().descendingSet(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> descendingSet() {
/* 1205 */       return Sets.filter(unfiltered().descendingSet(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> descendingIterator() {
/* 1210 */       return Iterators.filter(unfiltered().descendingIterator(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public E last() {
/* 1215 */       return Iterators.find(unfiltered().descendingIterator(), this.predicate);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 1221 */       return Sets.filter(
/* 1222 */           unfiltered().subSet(fromElement, fromInclusive, toElement, toInclusive), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 1227 */       return Sets.filter(unfiltered().headSet(toElement, inclusive), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 1232 */       return Sets.filter(unfiltered().tailSet(fromElement, inclusive), this.predicate);
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
/*      */   public static <B> Set<List<B>> cartesianProduct(List<? extends Set<? extends B>> sets) {
/* 1290 */     return CartesianSet.create(sets);
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
/*      */   @SafeVarargs
/*      */   public static <B> Set<List<B>> cartesianProduct(Set<? extends B>... sets) {
/* 1348 */     return cartesianProduct(Arrays.asList(sets));
/*      */   }
/*      */   
/*      */   private static final class CartesianSet<E>
/*      */     extends ForwardingCollection<List<E>> implements Set<List<E>> {
/*      */     private final transient ImmutableList<ImmutableSet<E>> axes;
/*      */     private final transient CartesianList<E> delegate;
/*      */     
/*      */     static <E> Set<List<E>> create(List<? extends Set<? extends E>> sets) {
/* 1357 */       ImmutableList.Builder<ImmutableSet<E>> axesBuilder = new ImmutableList.Builder<>(sets.size());
/* 1358 */       for (Set<? extends E> set : sets) {
/* 1359 */         ImmutableSet<E> copy = ImmutableSet.copyOf(set);
/* 1360 */         if (copy.isEmpty()) {
/* 1361 */           return ImmutableSet.of();
/*      */         }
/* 1363 */         axesBuilder.add(copy);
/*      */       } 
/* 1365 */       final ImmutableList<ImmutableSet<E>> axes = axesBuilder.build();
/* 1366 */       ImmutableList<List<E>> listAxes = (ImmutableList)new ImmutableList<List<List<E>>>()
/*      */         {
/*      */           public int size()
/*      */           {
/* 1370 */             return axes.size();
/*      */           }
/*      */ 
/*      */           
/*      */           public List<E> get(int index) {
/* 1375 */             return ((ImmutableSet<E>)axes.get(index)).asList();
/*      */           }
/*      */ 
/*      */           
/*      */           boolean isPartialView() {
/* 1380 */             return true;
/*      */           }
/*      */         };
/* 1383 */       return new CartesianSet<>(axes, new CartesianList<>(listAxes));
/*      */     }
/*      */     
/*      */     private CartesianSet(ImmutableList<ImmutableSet<E>> axes, CartesianList<E> delegate) {
/* 1387 */       this.axes = axes;
/* 1388 */       this.delegate = delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Collection<List<E>> delegate() {
/* 1393 */       return this.delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object object) {
/* 1398 */       if (!(object instanceof List)) {
/* 1399 */         return false;
/*      */       }
/* 1401 */       List<?> list = (List)object;
/* 1402 */       if (list.size() != this.axes.size()) {
/* 1403 */         return false;
/*      */       }
/* 1405 */       int i = 0;
/* 1406 */       for (Object o : list) {
/* 1407 */         if (!((ImmutableSet)this.axes.get(i)).contains(o)) {
/* 1408 */           return false;
/*      */         }
/* 1410 */         i++;
/*      */       } 
/* 1412 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/* 1419 */       if (object instanceof CartesianSet) {
/* 1420 */         CartesianSet<?> that = (CartesianSet)object;
/* 1421 */         return this.axes.equals(that.axes);
/*      */       } 
/* 1423 */       return super.equals(object);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1432 */       int adjust = size() - 1;
/* 1433 */       for (int i = 0; i < this.axes.size(); i++) {
/* 1434 */         adjust *= 31;
/* 1435 */         adjust = adjust ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*      */       } 
/*      */       
/* 1438 */       int hash = 1;
/* 1439 */       for (UnmodifiableIterator<ImmutableSet<E>> unmodifiableIterator = this.axes.iterator(); unmodifiableIterator.hasNext(); ) { Set<E> axis = unmodifiableIterator.next();
/* 1440 */         hash = 31 * hash + size() / axis.size() * axis.hashCode();
/*      */         
/* 1442 */         hash = hash ^ 0xFFFFFFFF ^ 0xFFFFFFFF; }
/*      */       
/* 1444 */       hash += adjust;
/* 1445 */       return hash ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
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
/*      */   @GwtCompatible(serializable = false)
/*      */   public static <E> Set<Set<E>> powerSet(Set<E> set) {
/* 1475 */     return new PowerSet<>(set);
/*      */   }
/*      */   
/*      */   private static final class SubSet<E> extends AbstractSet<E> {
/*      */     private final ImmutableMap<E, Integer> inputSet;
/*      */     private final int mask;
/*      */     
/*      */     SubSet(ImmutableMap<E, Integer> inputSet, int mask) {
/* 1483 */       this.inputSet = inputSet;
/* 1484 */       this.mask = mask;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/* 1489 */       return new UnmodifiableIterator<E>() {
/* 1490 */           final ImmutableList<E> elements = Sets.SubSet.this.inputSet.keySet().asList();
/* 1491 */           int remainingSetBits = Sets.SubSet.this.mask;
/*      */ 
/*      */           
/*      */           public boolean hasNext() {
/* 1495 */             return (this.remainingSetBits != 0);
/*      */           }
/*      */ 
/*      */           
/*      */           public E next() {
/* 1500 */             int index = Integer.numberOfTrailingZeros(this.remainingSetBits);
/* 1501 */             if (index == 32) {
/* 1502 */               throw new NoSuchElementException();
/*      */             }
/* 1504 */             this.remainingSetBits &= 1 << index ^ 0xFFFFFFFF;
/* 1505 */             return this.elements.get(index);
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1512 */       return Integer.bitCount(this.mask);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1517 */       Integer index = this.inputSet.get(o);
/* 1518 */       return (index != null && (this.mask & 1 << index.intValue()) != 0);
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class PowerSet<E> extends AbstractSet<Set<E>> {
/*      */     final ImmutableMap<E, Integer> inputSet;
/*      */     
/*      */     PowerSet(Set<E> input) {
/* 1526 */       Preconditions.checkArgument(
/* 1527 */           (input.size() <= 30), "Too many elements to create power set: %s > 30", input.size());
/* 1528 */       this.inputSet = Maps.indexMap(input);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1533 */       return 1 << this.inputSet.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 1538 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<Set<E>> iterator() {
/* 1543 */       return (Iterator)new AbstractIndexedListIterator<Set<Set<E>>>(size())
/*      */         {
/*      */           protected Set<E> get(int setBits) {
/* 1546 */             return new Sets.SubSet<>(Sets.PowerSet.this.inputSet, setBits);
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object obj) {
/* 1553 */       if (obj instanceof Set) {
/* 1554 */         Set<?> set = (Set)obj;
/* 1555 */         return this.inputSet.keySet().containsAll(set);
/*      */       } 
/* 1557 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/* 1562 */       if (obj instanceof PowerSet) {
/* 1563 */         PowerSet<?> that = (PowerSet)obj;
/* 1564 */         return this.inputSet.keySet().equals(that.inputSet.keySet());
/*      */       } 
/* 1566 */       return super.equals(obj);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1576 */       return this.inputSet.keySet().hashCode() << this.inputSet.size() - 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1581 */       String str = String.valueOf(this.inputSet); return (new StringBuilder(10 + String.valueOf(str).length())).append("powerSet(").append(str).append(")").toString();
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
/*      */   @Beta
/*      */   public static <E> Set<Set<E>> combinations(Set<E> set, final int size) {
/* 1611 */     final ImmutableMap<E, Integer> index = Maps.indexMap(set);
/* 1612 */     CollectPreconditions.checkNonnegative(size, "size");
/* 1613 */     Preconditions.checkArgument((size <= index.size()), "size (%s) must be <= set.size() (%s)", size, index.size());
/* 1614 */     if (size == 0)
/* 1615 */       return ImmutableSet.of(ImmutableSet.of()); 
/* 1616 */     if (size == index.size()) {
/* 1617 */       return ImmutableSet.of(index.keySet());
/*      */     }
/* 1619 */     return (Set)new AbstractSet<Set<Set<E>>>()
/*      */       {
/*      */         public boolean contains(Object o) {
/* 1622 */           if (o instanceof Set) {
/* 1623 */             Set<?> s = (Set)o;
/* 1624 */             return (s.size() == size && index.keySet().containsAll(s));
/*      */           } 
/* 1626 */           return false;
/*      */         }
/*      */ 
/*      */         
/*      */         public Iterator<Set<E>> iterator() {
/* 1631 */           return new AbstractIterator() {
/* 1632 */               final BitSet bits = new BitSet(index.size());
/*      */ 
/*      */               
/*      */               protected Set<E> computeNext() {
/* 1636 */                 if (this.bits.isEmpty()) {
/* 1637 */                   this.bits.set(0, size);
/*      */                 } else {
/* 1639 */                   int firstSetBit = this.bits.nextSetBit(0);
/* 1640 */                   int bitToFlip = this.bits.nextClearBit(firstSetBit);
/*      */                   
/* 1642 */                   if (bitToFlip == index.size()) {
/* 1643 */                     return endOfData();
/*      */                   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/* 1659 */                   this.bits.set(0, bitToFlip - firstSetBit - 1);
/* 1660 */                   this.bits.clear(bitToFlip - firstSetBit - 1, bitToFlip);
/* 1661 */                   this.bits.set(bitToFlip);
/*      */                 } 
/* 1663 */                 final BitSet copy = (BitSet)this.bits.clone();
/* 1664 */                 return new AbstractSet<E>()
/*      */                   {
/*      */                     public boolean contains(Object o) {
/* 1667 */                       Integer i = (Integer)index.get(o);
/* 1668 */                       return (i != null && copy.get(i.intValue()));
/*      */                     }
/*      */ 
/*      */                     
/*      */                     public Iterator<E> iterator() {
/* 1673 */                       return new AbstractIterator() {
/* 1674 */                           int i = -1;
/*      */ 
/*      */                           
/*      */                           protected E computeNext() {
/* 1678 */                             this.i = copy.nextSetBit(this.i + 1);
/* 1679 */                             if (this.i == -1) {
/* 1680 */                               return endOfData();
/*      */                             }
/* 1682 */                             return index.keySet().asList().get(this.i);
/*      */                           }
/*      */                         };
/*      */                     }
/*      */ 
/*      */                     
/*      */                     public int size() {
/* 1689 */                       return size;
/*      */                     }
/*      */                   };
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         public int size() {
/* 1698 */           return IntMath.binomial(index.size(), size);
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/* 1703 */           String str = String.valueOf(index.keySet()); int i = size; return (new StringBuilder(32 + String.valueOf(str).length())).append("Sets.combinations(").append(str).append(", ").append(i).append(")").toString();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   static int hashCodeImpl(Set<?> s) {
/* 1710 */     int hashCode = 0;
/* 1711 */     for (Object o : s) {
/* 1712 */       hashCode += (o != null) ? o.hashCode() : 0;
/*      */       
/* 1714 */       hashCode = hashCode ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*      */     } 
/*      */     
/* 1717 */     return hashCode;
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean equalsImpl(Set<?> s, Object object) {
/* 1722 */     if (s == object) {
/* 1723 */       return true;
/*      */     }
/* 1725 */     if (object instanceof Set) {
/* 1726 */       Set<?> o = (Set)object;
/*      */       
/*      */       try {
/* 1729 */         return (s.size() == o.size() && s.containsAll(o));
/* 1730 */       } catch (NullPointerException|ClassCastException ignored) {
/* 1731 */         return false;
/*      */       } 
/*      */     } 
/* 1734 */     return false;
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
/*      */   public static <E> NavigableSet<E> unmodifiableNavigableSet(NavigableSet<E> set) {
/* 1751 */     if (set instanceof ImmutableCollection || set instanceof UnmodifiableNavigableSet) {
/* 1752 */       return set;
/*      */     }
/* 1754 */     return new UnmodifiableNavigableSet<>(set);
/*      */   }
/*      */   
/*      */   static final class UnmodifiableNavigableSet<E> extends ForwardingSortedSet<E> implements NavigableSet<E>, Serializable { private final NavigableSet<E> delegate;
/*      */     private final SortedSet<E> unmodifiableDelegate;
/*      */     private transient UnmodifiableNavigableSet<E> descendingSet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableNavigableSet(NavigableSet<E> delegate) {
/* 1763 */       this.delegate = (NavigableSet<E>)Preconditions.checkNotNull(delegate);
/* 1764 */       this.unmodifiableDelegate = Collections.unmodifiableSortedSet(delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     protected SortedSet<E> delegate() {
/* 1769 */       return this.unmodifiableDelegate;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean removeIf(Predicate<? super E> filter) {
/* 1776 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Stream<E> stream() {
/* 1781 */       return this.delegate.stream();
/*      */     }
/*      */ 
/*      */     
/*      */     public Stream<E> parallelStream() {
/* 1786 */       return this.delegate.parallelStream();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super E> action) {
/* 1791 */       this.delegate.forEach(action);
/*      */     }
/*      */ 
/*      */     
/*      */     public E lower(E e) {
/* 1796 */       return this.delegate.lower(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E floor(E e) {
/* 1801 */       return this.delegate.floor(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E ceiling(E e) {
/* 1806 */       return this.delegate.ceiling(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E higher(E e) {
/* 1811 */       return this.delegate.higher(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollFirst() {
/* 1816 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollLast() {
/* 1821 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> descendingSet() {
/* 1828 */       UnmodifiableNavigableSet<E> result = this.descendingSet;
/* 1829 */       if (result == null) {
/* 1830 */         result = this.descendingSet = new UnmodifiableNavigableSet(this.delegate.descendingSet());
/* 1831 */         result.descendingSet = this;
/*      */       } 
/* 1833 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> descendingIterator() {
/* 1838 */       return Iterators.unmodifiableIterator(this.delegate.descendingIterator());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 1844 */       return Sets.unmodifiableNavigableSet(this.delegate
/* 1845 */           .subSet(fromElement, fromInclusive, toElement, toInclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 1850 */       return Sets.unmodifiableNavigableSet(this.delegate.headSet(toElement, inclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 1855 */       return Sets.unmodifiableNavigableSet(this.delegate.tailSet(fromElement, inclusive));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <E> NavigableSet<E> synchronizedNavigableSet(NavigableSet<E> navigableSet) {
/* 1908 */     return Synchronized.navigableSet(navigableSet);
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean removeAllImpl(Set<?> set, Iterator<?> iterator) {
/* 1913 */     boolean changed = false;
/* 1914 */     while (iterator.hasNext()) {
/* 1915 */       changed |= set.remove(iterator.next());
/*      */     }
/* 1917 */     return changed;
/*      */   }
/*      */   
/*      */   static boolean removeAllImpl(Set<?> set, Collection<?> collection) {
/* 1921 */     Preconditions.checkNotNull(collection);
/* 1922 */     if (collection instanceof Multiset) {
/* 1923 */       collection = ((Multiset)collection).elementSet();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1932 */     if (collection instanceof Set && collection.size() > set.size()) {
/* 1933 */       return Iterators.removeAll(set.iterator(), collection);
/*      */     }
/* 1935 */     return removeAllImpl(set, collection.iterator());
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   static class DescendingSet<E>
/*      */     extends ForwardingNavigableSet<E> {
/*      */     private final NavigableSet<E> forward;
/*      */     
/*      */     DescendingSet(NavigableSet<E> forward) {
/* 1944 */       this.forward = forward;
/*      */     }
/*      */ 
/*      */     
/*      */     protected NavigableSet<E> delegate() {
/* 1949 */       return this.forward;
/*      */     }
/*      */ 
/*      */     
/*      */     public E lower(E e) {
/* 1954 */       return this.forward.higher(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E floor(E e) {
/* 1959 */       return this.forward.ceiling(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E ceiling(E e) {
/* 1964 */       return this.forward.floor(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E higher(E e) {
/* 1969 */       return this.forward.lower(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollFirst() {
/* 1974 */       return this.forward.pollLast();
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollLast() {
/* 1979 */       return this.forward.pollFirst();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> descendingSet() {
/* 1984 */       return this.forward;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> descendingIterator() {
/* 1989 */       return this.forward.iterator();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 1995 */       return this.forward.subSet(toElement, toInclusive, fromElement, fromInclusive).descendingSet();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> subSet(E fromElement, E toElement) {
/* 2000 */       return standardSubSet(fromElement, toElement);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 2005 */       return this.forward.tailSet(toElement, inclusive).descendingSet();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> headSet(E toElement) {
/* 2010 */       return standardHeadSet(toElement);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 2015 */       return this.forward.headSet(fromElement, inclusive).descendingSet();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> tailSet(E fromElement) {
/* 2020 */       return standardTailSet(fromElement);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Comparator<? super E> comparator() {
/* 2026 */       Comparator<? super E> forwardComparator = this.forward.comparator();
/* 2027 */       if (forwardComparator == null) {
/* 2028 */         return Ordering.<Comparable>natural().reverse();
/*      */       }
/* 2030 */       return reverse(forwardComparator);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private static <T> Ordering<T> reverse(Comparator<T> forward) {
/* 2036 */       return Ordering.<T>from(forward).reverse();
/*      */     }
/*      */ 
/*      */     
/*      */     public E first() {
/* 2041 */       return this.forward.last();
/*      */     }
/*      */ 
/*      */     
/*      */     public E last() {
/* 2046 */       return this.forward.first();
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/* 2051 */       return this.forward.descendingIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 2056 */       return standardToArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] array) {
/* 2061 */       return (T[])standardToArray((Object[])array);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 2066 */       return standardToString();
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
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static <K extends Comparable<? super K>> NavigableSet<K> subSet(NavigableSet<K> set, Range<K> range) {
/* 2090 */     if (set.comparator() != null && set
/* 2091 */       .comparator() != Ordering.natural() && range
/* 2092 */       .hasLowerBound() && range
/* 2093 */       .hasUpperBound()) {
/* 2094 */       Preconditions.checkArgument(
/* 2095 */           (set.comparator().compare(range.lowerEndpoint(), range.upperEndpoint()) <= 0), "set is using a custom comparator which is inconsistent with the natural ordering.");
/*      */     }
/*      */     
/* 2098 */     if (range.hasLowerBound() && range.hasUpperBound())
/* 2099 */       return set.subSet(range
/* 2100 */           .lowerEndpoint(), 
/* 2101 */           (range.lowerBoundType() == BoundType.CLOSED), range
/* 2102 */           .upperEndpoint(), 
/* 2103 */           (range.upperBoundType() == BoundType.CLOSED)); 
/* 2104 */     if (range.hasLowerBound())
/* 2105 */       return set.tailSet(range.lowerEndpoint(), (range.lowerBoundType() == BoundType.CLOSED)); 
/* 2106 */     if (range.hasUpperBound()) {
/* 2107 */       return set.headSet(range.upperEndpoint(), (range.upperBoundType() == BoundType.CLOSED));
/*      */     }
/* 2109 */     return (NavigableSet<K>)Preconditions.checkNotNull(set);
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\Sets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */