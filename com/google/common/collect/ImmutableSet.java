/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.io.Serializable;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableSet<E>
/*     */   extends ImmutableCollection<E>
/*     */   implements Set<E>
/*     */ {
/*     */   static final int SPLITERATOR_CHARACTERISTICS = 1297;
/*     */   @LazyInit
/*     */   @RetainedWith
/*     */   private transient ImmutableList<E> asList;
/*     */   static final int MAX_TABLE_SIZE = 1073741824;
/*     */   private static final double DESIRED_LOAD_FACTOR = 0.7D;
/*     */   private static final int CUTOFF = 751619276;
/*     */   static final double HASH_FLOODING_FPP = 0.001D;
/*     */   static final int MAX_RUN_MULTIPLIER = 13;
/*     */   
/*     */   public static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
/*  67 */     return CollectCollectors.toImmutableSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> of() {
/*  76 */     return RegularImmutableSet.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> of(E element) {
/*  85 */     return new SingletonImmutableSet<>(element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> of(E e1, E e2) {
/*  94 */     return construct(2, 2, new Object[] { e1, e2 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> of(E e1, E e2, E e3) {
/* 103 */     return construct(3, 3, new Object[] { e1, e2, e3 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4) {
/* 112 */     return construct(4, 4, new Object[] { e1, e2, e3, e4 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4, E e5) {
/* 121 */     return construct(5, 5, new Object[] { e1, e2, e3, e4, e5 });
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
/*     */   @SafeVarargs
/*     */   public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... others) {
/* 135 */     Preconditions.checkArgument((others.length <= 2147483641), "the total number of elements must fit in an int");
/*     */     
/* 137 */     int paramCount = 6;
/* 138 */     Object[] elements = new Object[6 + others.length];
/* 139 */     elements[0] = e1;
/* 140 */     elements[1] = e2;
/* 141 */     elements[2] = e3;
/* 142 */     elements[3] = e4;
/* 143 */     elements[4] = e5;
/* 144 */     elements[5] = e6;
/* 145 */     System.arraycopy(others, 0, elements, 6, others.length);
/* 146 */     return construct(elements.length, elements.length, elements);
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
/*     */   private static <E> ImmutableSet<E> constructUnknownDuplication(int n, Object... elements) {
/* 166 */     return construct(n, 
/*     */         
/* 168 */         Math.max(4, 
/*     */           
/* 170 */           IntMath.sqrt(n, RoundingMode.CEILING)), elements);
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
/*     */   private static <E> ImmutableSet<E> construct(int n, int expectedSize, Object... elements) {
/*     */     E elem;
/* 189 */     switch (n) {
/*     */       case 0:
/* 191 */         return of();
/*     */       
/*     */       case 1:
/* 194 */         elem = (E)elements[0];
/* 195 */         return of(elem);
/*     */     } 
/* 197 */     SetBuilderImpl<E> builder = new RegularSetBuilderImpl<>(expectedSize);
/* 198 */     for (int i = 0; i < n; i++) {
/*     */       
/* 200 */       E e = (E)Preconditions.checkNotNull(elements[i]);
/* 201 */       builder = builder.add(e);
/*     */     } 
/* 203 */     return builder.review().build();
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
/*     */   public static <E> ImmutableSet<E> copyOf(Collection<? extends E> elements) {
/* 225 */     if (elements instanceof ImmutableSet && !(elements instanceof java.util.SortedSet)) {
/*     */       
/* 227 */       ImmutableSet<E> set = (ImmutableSet)elements;
/* 228 */       if (!set.isPartialView()) {
/* 229 */         return set;
/*     */       }
/* 231 */     } else if (elements instanceof EnumSet) {
/* 232 */       return copyOfEnumSet((EnumSet)elements);
/*     */     } 
/* 234 */     Object[] array = elements.toArray();
/* 235 */     if (elements instanceof Set)
/*     */     {
/* 237 */       return construct(array.length, array.length, array);
/*     */     }
/* 239 */     return constructUnknownDuplication(array.length, array);
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
/*     */   public static <E> ImmutableSet<E> copyOf(Iterable<? extends E> elements) {
/* 256 */     return (elements instanceof Collection) ? 
/* 257 */       copyOf((Collection<? extends E>)elements) : 
/* 258 */       copyOf(elements.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> copyOf(Iterator<? extends E> elements) {
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
/*     */   
/*     */   public static <E> ImmutableSet<E> copyOf(E[] elements) {
/* 288 */     switch (elements.length) {
/*     */       case 0:
/* 290 */         return of();
/*     */       case 1:
/* 292 */         return of(elements[0]);
/*     */     } 
/* 294 */     return constructUnknownDuplication(elements.length, (Object[])elements.clone());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static ImmutableSet copyOfEnumSet(EnumSet<Enum> enumSet) {
/* 300 */     return ImmutableEnumSet.asImmutable(EnumSet.copyOf(enumSet));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isHashCodeFast() {
/* 307 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 312 */     if (object == this)
/* 313 */       return true; 
/* 314 */     if (object instanceof ImmutableSet && 
/* 315 */       isHashCodeFast() && ((ImmutableSet)object)
/* 316 */       .isHashCodeFast() && 
/* 317 */       hashCode() != object.hashCode()) {
/* 318 */       return false;
/*     */     }
/* 320 */     return Sets.equalsImpl(this, object);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 325 */     return Sets.hashCodeImpl(this);
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
/*     */   public ImmutableList<E> asList() {
/* 337 */     ImmutableList<E> result = this.asList;
/* 338 */     return (result == null) ? (this.asList = createAsList()) : result;
/*     */   }
/*     */   
/*     */   ImmutableList<E> createAsList() {
/* 342 */     return new RegularImmutableAsList<>(this, toArray());
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class Indexed<E>
/*     */     extends ImmutableSet<E>
/*     */   {
/*     */     public UnmodifiableIterator<E> iterator() {
/* 350 */       return asList().iterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<E> spliterator() {
/* 355 */       return CollectSpliterators.indexed(size(), 1297, this::get);
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<? super E> consumer) {
/* 360 */       Preconditions.checkNotNull(consumer);
/* 361 */       int n = size();
/* 362 */       for (int i = 0; i < n; i++) {
/* 363 */         consumer.accept(get(i));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     int copyIntoArray(Object[] dst, int offset) {
/* 369 */       return asList().copyIntoArray(dst, offset);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableList<E> createAsList() {
/* 374 */       return new ImmutableAsList<E>()
/*     */         {
/*     */           public E get(int index) {
/* 377 */             return ImmutableSet.Indexed.this.get(index);
/*     */           }
/*     */ 
/*     */           
/*     */           ImmutableSet.Indexed<E> delegateCollection() {
/* 382 */             return ImmutableSet.Indexed.this;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     abstract E get(int param1Int);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class SerializedForm
/*     */     implements Serializable
/*     */   {
/*     */     final Object[] elements;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(Object[] elements) {
/* 399 */       this.elements = elements;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 403 */       return ImmutableSet.copyOf(this.elements);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 411 */     return new SerializedForm(toArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Builder<E> builder() {
/* 419 */     return new Builder<>();
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
/* 436 */     CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
/* 437 */     return new Builder<>(expectedSize);
/*     */   }
/*     */ 
/*     */   
/*     */   static Object[] rebuildHashTable(int newTableSize, Object[] elements, int n) {
/* 442 */     Object[] hashTable = new Object[newTableSize];
/* 443 */     int mask = hashTable.length - 1;
/* 444 */     for (int i = 0; i < n; ) {
/* 445 */       Object e = elements[i];
/* 446 */       int j0 = Hashing.smear(e.hashCode());
/* 447 */       int j = j0; for (;; i++) {
/* 448 */         int index = j & mask;
/* 449 */         if (hashTable[index] == null) {
/* 450 */           hashTable[index] = e;
/*     */         } else {
/*     */           j++; continue;
/*     */         } 
/*     */       } 
/* 455 */     }  return hashTable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<E>
/*     */     extends ImmutableCollection.Builder<E>
/*     */   {
/*     */     private ImmutableSet.SetBuilderImpl<E> impl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean forceCopy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder() {
/* 481 */       this(4);
/*     */     }
/*     */     
/*     */     Builder(int capacity) {
/* 485 */       this.impl = new ImmutableSet.RegularSetBuilderImpl<>(capacity);
/*     */     }
/*     */     
/*     */     Builder(boolean subclass) {
/* 489 */       this.impl = null;
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     void forceJdk() {
/* 494 */       this.impl = new ImmutableSet.JdkBackedSetBuilderImpl<>(this.impl);
/*     */     }
/*     */     
/*     */     final void copyIfNecessary() {
/* 498 */       if (this.forceCopy) {
/* 499 */         copy();
/* 500 */         this.forceCopy = false;
/*     */       } 
/*     */     }
/*     */     
/*     */     void copy() {
/* 505 */       this.impl = this.impl.copy();
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> add(E element) {
/* 511 */       Preconditions.checkNotNull(element);
/* 512 */       copyIfNecessary();
/* 513 */       this.impl = this.impl.add(element);
/* 514 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> add(E... elements) {
/* 520 */       super.add(elements);
/* 521 */       return this;
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
/* 535 */       super.addAll(elements);
/* 536 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterator<? extends E> elements) {
/* 542 */       super.addAll(elements);
/* 543 */       return this;
/*     */     }
/*     */     
/*     */     Builder<E> combine(Builder<E> other) {
/* 547 */       copyIfNecessary();
/* 548 */       this.impl = this.impl.combine(other.impl);
/* 549 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableSet<E> build() {
/* 554 */       this.forceCopy = true;
/* 555 */       this.impl = this.impl.review();
/* 556 */       return this.impl.build();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static abstract class SetBuilderImpl<E>
/*     */   {
/*     */     E[] dedupedElements;
/*     */     int distinct;
/*     */     
/*     */     SetBuilderImpl(int expectedCapacity) {
/* 567 */       this.dedupedElements = (E[])new Object[expectedCapacity];
/* 568 */       this.distinct = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     SetBuilderImpl(SetBuilderImpl<E> toCopy) {
/* 573 */       this.dedupedElements = Arrays.copyOf(toCopy.dedupedElements, toCopy.dedupedElements.length);
/* 574 */       this.distinct = toCopy.distinct;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void ensureCapacity(int minCapacity) {
/* 582 */       if (minCapacity > this.dedupedElements.length) {
/*     */         
/* 584 */         int newCapacity = ImmutableCollection.Builder.expandedCapacity(this.dedupedElements.length, minCapacity);
/* 585 */         this.dedupedElements = Arrays.copyOf(this.dedupedElements, newCapacity);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     final void addDedupedElement(E e) {
/* 591 */       ensureCapacity(this.distinct + 1);
/* 592 */       this.dedupedElements[this.distinct++] = e;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract SetBuilderImpl<E> add(E param1E);
/*     */ 
/*     */ 
/*     */     
/*     */     final SetBuilderImpl<E> combine(SetBuilderImpl<E> other) {
/* 603 */       SetBuilderImpl<E> result = this;
/* 604 */       for (int i = 0; i < other.distinct; i++) {
/* 605 */         result = result.add(other.dedupedElements[i]);
/*     */       }
/* 607 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract SetBuilderImpl<E> copy();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     SetBuilderImpl<E> review() {
/* 621 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract ImmutableSet<E> build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static int chooseTableSize(int setSize) {
/* 643 */     setSize = Math.max(setSize, 2);
/*     */     
/* 645 */     if (setSize < 751619276) {
/*     */       
/* 647 */       int tableSize = Integer.highestOneBit(setSize - 1) << 1;
/* 648 */       while (tableSize * 0.7D < setSize) {
/* 649 */         tableSize <<= 1;
/*     */       }
/* 651 */       return tableSize;
/*     */     } 
/*     */ 
/*     */     
/* 655 */     Preconditions.checkArgument((setSize < 1073741824), "collection too large");
/* 656 */     return 1073741824;
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
/*     */   static boolean hashFloodingDetected(Object[] hashTable) {
/* 698 */     int maxRunBeforeFallback = maxRunBeforeFallback(hashTable.length);
/*     */     
/*     */     int endOfStartRun;
/*     */     
/* 702 */     for (endOfStartRun = 0; endOfStartRun < hashTable.length && 
/* 703 */       hashTable[endOfStartRun] != null; ) {
/*     */ 
/*     */       
/* 706 */       endOfStartRun++;
/* 707 */       if (endOfStartRun > maxRunBeforeFallback) {
/* 708 */         return true;
/*     */       }
/*     */     } 
/*     */     int startOfEndRun;
/* 712 */     for (startOfEndRun = hashTable.length - 1; startOfEndRun > endOfStartRun && 
/* 713 */       hashTable[startOfEndRun] != null; startOfEndRun--) {
/*     */ 
/*     */       
/* 716 */       if (endOfStartRun + hashTable.length - 1 - startOfEndRun > maxRunBeforeFallback) {
/* 717 */         return true;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 723 */     int testBlockSize = maxRunBeforeFallback / 2;
/*     */     int i;
/* 725 */     for (i = endOfStartRun + 1; i + testBlockSize <= startOfEndRun; i += testBlockSize) {
/* 726 */       int j = 0; while (true) { if (j < testBlockSize) {
/* 727 */           if (hashTable[i + j] == null)
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 731 */         return true; }
/*     */     
/* 733 */     }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int maxRunBeforeFallback(int tableSize) {
/* 742 */     return 13 * IntMath.log2(tableSize, RoundingMode.UNNECESSARY);
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract UnmodifiableIterator<E> iterator();
/*     */ 
/*     */   
/*     */   private static final class RegularSetBuilderImpl<E>
/*     */     extends SetBuilderImpl<E>
/*     */   {
/*     */     private Object[] hashTable;
/*     */     
/*     */     private int maxRunBeforeFallback;
/*     */     
/*     */     private int expandTableThreshold;
/*     */     private int hashCode;
/*     */     
/*     */     RegularSetBuilderImpl(int expectedCapacity) {
/* 760 */       super(expectedCapacity);
/* 761 */       int tableSize = ImmutableSet.chooseTableSize(expectedCapacity);
/* 762 */       this.hashTable = new Object[tableSize];
/* 763 */       this.maxRunBeforeFallback = ImmutableSet.maxRunBeforeFallback(tableSize);
/* 764 */       this.expandTableThreshold = (int)(0.7D * tableSize);
/*     */     }
/*     */     
/*     */     RegularSetBuilderImpl(RegularSetBuilderImpl<E> toCopy) {
/* 768 */       super(toCopy);
/* 769 */       this.hashTable = Arrays.copyOf(toCopy.hashTable, toCopy.hashTable.length);
/* 770 */       this.maxRunBeforeFallback = toCopy.maxRunBeforeFallback;
/* 771 */       this.expandTableThreshold = toCopy.expandTableThreshold;
/* 772 */       this.hashCode = toCopy.hashCode;
/*     */     }
/*     */     
/*     */     void ensureTableCapacity(int minCapacity) {
/* 776 */       if (minCapacity > this.expandTableThreshold && this.hashTable.length < 1073741824) {
/* 777 */         int newTableSize = this.hashTable.length * 2;
/* 778 */         this.hashTable = ImmutableSet.rebuildHashTable(newTableSize, (Object[])this.dedupedElements, this.distinct);
/* 779 */         this.maxRunBeforeFallback = ImmutableSet.maxRunBeforeFallback(newTableSize);
/* 780 */         this.expandTableThreshold = (int)(0.7D * newTableSize);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet.SetBuilderImpl<E> add(E e) {
/* 786 */       Preconditions.checkNotNull(e);
/* 787 */       int eHash = e.hashCode();
/* 788 */       int i0 = Hashing.smear(eHash);
/* 789 */       int mask = this.hashTable.length - 1;
/* 790 */       for (int i = i0; i - i0 < this.maxRunBeforeFallback; i++) {
/* 791 */         int index = i & mask;
/* 792 */         Object tableEntry = this.hashTable[index];
/* 793 */         if (tableEntry == null) {
/* 794 */           addDedupedElement(e);
/* 795 */           this.hashTable[index] = e;
/* 796 */           this.hashCode += eHash;
/* 797 */           ensureTableCapacity(this.distinct);
/* 798 */           return this;
/* 799 */         }  if (tableEntry.equals(e)) {
/* 800 */           return this;
/*     */         }
/*     */       } 
/*     */       
/* 804 */       return (new ImmutableSet.JdkBackedSetBuilderImpl<>(this)).add(e);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet.SetBuilderImpl<E> copy() {
/* 809 */       return new RegularSetBuilderImpl(this);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet.SetBuilderImpl<E> review() {
/* 814 */       int targetTableSize = ImmutableSet.chooseTableSize(this.distinct);
/* 815 */       if (targetTableSize * 2 < this.hashTable.length) {
/* 816 */         this.hashTable = ImmutableSet.rebuildHashTable(targetTableSize, (Object[])this.dedupedElements, this.distinct);
/* 817 */         this.maxRunBeforeFallback = ImmutableSet.maxRunBeforeFallback(targetTableSize);
/* 818 */         this.expandTableThreshold = (int)(0.7D * targetTableSize);
/*     */       } 
/* 820 */       return ImmutableSet.hashFloodingDetected(this.hashTable) ? new ImmutableSet.JdkBackedSetBuilderImpl<>(this) : this;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet<E> build() {
/* 825 */       switch (this.distinct) {
/*     */         case 0:
/* 827 */           return ImmutableSet.of();
/*     */         case 1:
/* 829 */           return ImmutableSet.of(this.dedupedElements[0]);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 834 */       Object[] elements = (this.distinct == this.dedupedElements.length) ? (Object[])this.dedupedElements : Arrays.<Object>copyOf((Object[])this.dedupedElements, this.distinct);
/* 835 */       return new RegularImmutableSet<>(elements, this.hashCode, this.hashTable, this.hashTable.length - 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class JdkBackedSetBuilderImpl<E>
/*     */     extends SetBuilderImpl<E>
/*     */   {
/*     */     private final Set<Object> delegate;
/*     */ 
/*     */     
/*     */     JdkBackedSetBuilderImpl(ImmutableSet.SetBuilderImpl<E> toCopy) {
/* 847 */       super(toCopy);
/* 848 */       this.delegate = Sets.newHashSetWithExpectedSize(this.distinct);
/* 849 */       for (int i = 0; i < this.distinct; i++) {
/* 850 */         this.delegate.add(this.dedupedElements[i]);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet.SetBuilderImpl<E> add(E e) {
/* 856 */       Preconditions.checkNotNull(e);
/* 857 */       if (this.delegate.add(e)) {
/* 858 */         addDedupedElement(e);
/*     */       }
/* 860 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet.SetBuilderImpl<E> copy() {
/* 865 */       return new JdkBackedSetBuilderImpl(this);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet<E> build() {
/* 870 */       switch (this.distinct) {
/*     */         case 0:
/* 872 */           return ImmutableSet.of();
/*     */         case 1:
/* 874 */           return ImmutableSet.of(this.dedupedElements[0]);
/*     */       } 
/* 876 */       return new JdkBackedImmutableSet<>(this.delegate, 
/* 877 */           ImmutableList.asImmutableList((Object[])this.dedupedElements, this.distinct));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\ImmutableSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */