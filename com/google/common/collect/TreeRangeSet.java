/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public class TreeRangeSet<C extends Comparable<?>>
/*     */   extends AbstractRangeSet<C>
/*     */   implements Serializable
/*     */ {
/*     */   @VisibleForTesting
/*     */   final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
/*     */   private transient Set<Range<C>> asRanges;
/*     */   private transient Set<Range<C>> asDescendingSetOfRanges;
/*     */   private transient RangeSet<C> complement;
/*     */   
/*     */   public static <C extends Comparable<?>> TreeRangeSet<C> create() {
/*  50 */     return new TreeRangeSet<>(new TreeMap<>());
/*     */   }
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> TreeRangeSet<C> create(RangeSet<C> rangeSet) {
/*  55 */     TreeRangeSet<C> result = create();
/*  56 */     result.addAll(rangeSet);
/*  57 */     return result;
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
/*     */   public static <C extends Comparable<?>> TreeRangeSet<C> create(Iterable<Range<C>> ranges) {
/*  70 */     TreeRangeSet<C> result = create();
/*  71 */     result.addAll(ranges);
/*  72 */     return result;
/*     */   }
/*     */   
/*     */   private TreeRangeSet(NavigableMap<Cut<C>, Range<C>> rangesByLowerCut) {
/*  76 */     this.rangesByLowerBound = rangesByLowerCut;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Range<C>> asRanges() {
/*  84 */     Set<Range<C>> result = this.asRanges;
/*  85 */     return (result == null) ? (this.asRanges = new AsRanges(this, this.rangesByLowerBound.values())) : result;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Range<C>> asDescendingSetOfRanges() {
/*  90 */     Set<Range<C>> result = this.asDescendingSetOfRanges;
/*  91 */     return (result == null) ? (
/*  92 */       this.asDescendingSetOfRanges = new AsRanges(this, this.rangesByLowerBound.descendingMap().values())) : 
/*  93 */       result;
/*     */   }
/*     */   
/*     */   final class AsRanges
/*     */     extends ForwardingCollection<Range<C>> implements Set<Range<C>> {
/*     */     final Collection<Range<C>> delegate;
/*     */     
/*     */     AsRanges(TreeRangeSet this$0, Collection<Range<C>> delegate) {
/* 101 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Collection<Range<C>> delegate() {
/* 106 */       return this.delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 111 */       return Sets.hashCodeImpl(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 116 */       return Sets.equalsImpl(this, o);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Range<C> rangeContaining(C value) {
/* 122 */     Preconditions.checkNotNull(value);
/* 123 */     Map.Entry<Cut<C>, Range<C>> floorEntry = this.rangesByLowerBound.floorEntry((Cut)Cut.belowValue((Comparable)value));
/* 124 */     if (floorEntry != null && ((Range)floorEntry.getValue()).contains(value)) {
/* 125 */       return floorEntry.getValue();
/*     */     }
/*     */     
/* 128 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean intersects(Range<C> range) {
/* 134 */     Preconditions.checkNotNull(range);
/* 135 */     Map.Entry<Cut<C>, Range<C>> ceilingEntry = this.rangesByLowerBound.ceilingEntry(range.lowerBound);
/* 136 */     if (ceilingEntry != null && ((Range)ceilingEntry
/* 137 */       .getValue()).isConnected(range) && 
/* 138 */       !((Range)ceilingEntry.getValue()).intersection(range).isEmpty()) {
/* 139 */       return true;
/*     */     }
/* 141 */     Map.Entry<Cut<C>, Range<C>> priorEntry = this.rangesByLowerBound.lowerEntry(range.lowerBound);
/* 142 */     return (priorEntry != null && ((Range)priorEntry
/* 143 */       .getValue()).isConnected(range) && 
/* 144 */       !((Range)priorEntry.getValue()).intersection(range).isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean encloses(Range<C> range) {
/* 149 */     Preconditions.checkNotNull(range);
/* 150 */     Map.Entry<Cut<C>, Range<C>> floorEntry = this.rangesByLowerBound.floorEntry(range.lowerBound);
/* 151 */     return (floorEntry != null && ((Range)floorEntry.getValue()).encloses(range));
/*     */   }
/*     */   
/*     */   private Range<C> rangeEnclosing(Range<C> range) {
/* 155 */     Preconditions.checkNotNull(range);
/* 156 */     Map.Entry<Cut<C>, Range<C>> floorEntry = this.rangesByLowerBound.floorEntry(range.lowerBound);
/* 157 */     return (floorEntry != null && ((Range)floorEntry.getValue()).encloses(range)) ? 
/* 158 */       floorEntry.getValue() : 
/* 159 */       null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Range<C> span() {
/* 164 */     Map.Entry<Cut<C>, Range<C>> firstEntry = this.rangesByLowerBound.firstEntry();
/* 165 */     Map.Entry<Cut<C>, Range<C>> lastEntry = this.rangesByLowerBound.lastEntry();
/* 166 */     if (firstEntry == null) {
/* 167 */       throw new NoSuchElementException();
/*     */     }
/* 169 */     return Range.create(((Range)firstEntry.getValue()).lowerBound, ((Range)lastEntry.getValue()).upperBound);
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(Range<C> rangeToAdd) {
/* 174 */     Preconditions.checkNotNull(rangeToAdd);
/*     */     
/* 176 */     if (rangeToAdd.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 182 */     Cut<C> lbToAdd = rangeToAdd.lowerBound;
/* 183 */     Cut<C> ubToAdd = rangeToAdd.upperBound;
/*     */     
/* 185 */     Map.Entry<Cut<C>, Range<C>> entryBelowLB = this.rangesByLowerBound.lowerEntry(lbToAdd);
/* 186 */     if (entryBelowLB != null) {
/*     */       
/* 188 */       Range<C> rangeBelowLB = entryBelowLB.getValue();
/* 189 */       if (rangeBelowLB.upperBound.compareTo(lbToAdd) >= 0) {
/*     */         
/* 191 */         if (rangeBelowLB.upperBound.compareTo(ubToAdd) >= 0)
/*     */         {
/* 193 */           ubToAdd = rangeBelowLB.upperBound;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 199 */         lbToAdd = rangeBelowLB.lowerBound;
/*     */       } 
/*     */     } 
/*     */     
/* 203 */     Map.Entry<Cut<C>, Range<C>> entryBelowUB = this.rangesByLowerBound.floorEntry(ubToAdd);
/* 204 */     if (entryBelowUB != null) {
/*     */       
/* 206 */       Range<C> rangeBelowUB = entryBelowUB.getValue();
/* 207 */       if (rangeBelowUB.upperBound.compareTo(ubToAdd) >= 0)
/*     */       {
/* 209 */         ubToAdd = rangeBelowUB.upperBound;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 214 */     this.rangesByLowerBound.subMap(lbToAdd, ubToAdd).clear();
/*     */     
/* 216 */     replaceRangeWithSameLowerBound(Range.create(lbToAdd, ubToAdd));
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Range<C> rangeToRemove) {
/* 221 */     Preconditions.checkNotNull(rangeToRemove);
/*     */     
/* 223 */     if (rangeToRemove.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 230 */     Map.Entry<Cut<C>, Range<C>> entryBelowLB = this.rangesByLowerBound.lowerEntry(rangeToRemove.lowerBound);
/* 231 */     if (entryBelowLB != null) {
/*     */       
/* 233 */       Range<C> rangeBelowLB = entryBelowLB.getValue();
/* 234 */       if (rangeBelowLB.upperBound.compareTo(rangeToRemove.lowerBound) >= 0) {
/*     */         
/* 236 */         if (rangeToRemove.hasUpperBound() && rangeBelowLB.upperBound
/* 237 */           .compareTo(rangeToRemove.upperBound) >= 0)
/*     */         {
/* 239 */           replaceRangeWithSameLowerBound(
/* 240 */               Range.create(rangeToRemove.upperBound, rangeBelowLB.upperBound));
/*     */         }
/* 242 */         replaceRangeWithSameLowerBound(
/* 243 */             Range.create(rangeBelowLB.lowerBound, rangeToRemove.lowerBound));
/*     */       } 
/*     */     } 
/*     */     
/* 247 */     Map.Entry<Cut<C>, Range<C>> entryBelowUB = this.rangesByLowerBound.floorEntry(rangeToRemove.upperBound);
/* 248 */     if (entryBelowUB != null) {
/*     */       
/* 250 */       Range<C> rangeBelowUB = entryBelowUB.getValue();
/* 251 */       if (rangeToRemove.hasUpperBound() && rangeBelowUB.upperBound
/* 252 */         .compareTo(rangeToRemove.upperBound) >= 0)
/*     */       {
/* 254 */         replaceRangeWithSameLowerBound(
/* 255 */             Range.create(rangeToRemove.upperBound, rangeBelowUB.upperBound));
/*     */       }
/*     */     } 
/*     */     
/* 259 */     this.rangesByLowerBound.subMap(rangeToRemove.lowerBound, rangeToRemove.upperBound).clear();
/*     */   }
/*     */   
/*     */   private void replaceRangeWithSameLowerBound(Range<C> range) {
/* 263 */     if (range.isEmpty()) {
/* 264 */       this.rangesByLowerBound.remove(range.lowerBound);
/*     */     } else {
/* 266 */       this.rangesByLowerBound.put(range.lowerBound, range);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RangeSet<C> complement() {
/* 274 */     RangeSet<C> result = this.complement;
/* 275 */     return (result == null) ? (this.complement = new Complement()) : result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class RangesByUpperBound<C extends Comparable<?>>
/*     */     extends AbstractNavigableMap<Cut<C>, Range<C>>
/*     */   {
/*     */     private final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
/*     */     
/*     */     private final Range<Cut<C>> upperBoundWindow;
/*     */ 
/*     */     
/*     */     RangesByUpperBound(NavigableMap<Cut<C>, Range<C>> rangesByLowerBound) {
/* 290 */       this.rangesByLowerBound = rangesByLowerBound;
/* 291 */       this.upperBoundWindow = Range.all();
/*     */     }
/*     */ 
/*     */     
/*     */     private RangesByUpperBound(NavigableMap<Cut<C>, Range<C>> rangesByLowerBound, Range<Cut<C>> upperBoundWindow) {
/* 296 */       this.rangesByLowerBound = rangesByLowerBound;
/* 297 */       this.upperBoundWindow = upperBoundWindow;
/*     */     }
/*     */     
/*     */     private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> window) {
/* 301 */       if (window.isConnected(this.upperBoundWindow)) {
/* 302 */         return new RangesByUpperBound(this.rangesByLowerBound, window.intersection(this.upperBoundWindow));
/*     */       }
/* 304 */       return ImmutableSortedMap.of();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> fromKey, boolean fromInclusive, Cut<C> toKey, boolean toInclusive) {
/* 311 */       return subMap(
/* 312 */           Range.range(fromKey, 
/* 313 */             BoundType.forBoolean(fromInclusive), toKey, 
/* 314 */             BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> toKey, boolean inclusive) {
/* 319 */       return subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> fromKey, boolean inclusive) {
/* 324 */       return subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public Comparator<? super Cut<C>> comparator() {
/* 329 */       return Ordering.natural();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 334 */       return (get(key) != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public Range<C> get(Object key) {
/* 339 */       if (key instanceof Cut) {
/*     */         
/*     */         try {
/* 342 */           Cut<C> cut = (Cut<C>)key;
/* 343 */           if (!this.upperBoundWindow.contains(cut)) {
/* 344 */             return null;
/*     */           }
/* 346 */           Map.Entry<Cut<C>, Range<C>> candidate = this.rangesByLowerBound.lowerEntry(cut);
/* 347 */           if (candidate != null && ((Range)candidate.getValue()).upperBound.equals(cut)) {
/* 348 */             return candidate.getValue();
/*     */           }
/* 350 */         } catch (ClassCastException e) {
/* 351 */           return null;
/*     */         } 
/*     */       }
/* 354 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator() {
/*     */       final Iterator<Range<C>> backingItr;
/* 364 */       if (!this.upperBoundWindow.hasLowerBound()) {
/* 365 */         backingItr = this.rangesByLowerBound.values().iterator();
/*     */       } else {
/*     */         
/* 368 */         Map.Entry<Cut<C>, Range<C>> lowerEntry = this.rangesByLowerBound.lowerEntry(this.upperBoundWindow.lowerEndpoint());
/* 369 */         if (lowerEntry == null) {
/* 370 */           backingItr = this.rangesByLowerBound.values().iterator();
/* 371 */         } else if (this.upperBoundWindow.lowerBound.isLessThan(((Range)lowerEntry.getValue()).upperBound)) {
/* 372 */           backingItr = this.rangesByLowerBound.tailMap(lowerEntry.getKey(), true).values().iterator();
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */           
/* 378 */           backingItr = this.rangesByLowerBound.tailMap(this.upperBoundWindow.lowerEndpoint(), true).values().iterator();
/*     */         } 
/*     */       } 
/* 381 */       return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>()
/*     */         {
/*     */           protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/* 384 */             if (!backingItr.hasNext()) {
/* 385 */               return endOfData();
/*     */             }
/* 387 */             Range<C> range = backingItr.next();
/* 388 */             if (TreeRangeSet.RangesByUpperBound.this.upperBoundWindow.upperBound.isLessThan(range.upperBound)) {
/* 389 */               return endOfData();
/*     */             }
/* 391 */             return Maps.immutableEntry(range.upperBound, range);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
/*     */       Collection<Range<C>> candidates;
/* 400 */       if (this.upperBoundWindow.hasUpperBound()) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 405 */         candidates = this.rangesByLowerBound.headMap(this.upperBoundWindow.upperEndpoint(), false).descendingMap().values();
/*     */       } else {
/* 407 */         candidates = this.rangesByLowerBound.descendingMap().values();
/*     */       } 
/* 409 */       final PeekingIterator<Range<C>> backingItr = Iterators.peekingIterator(candidates.iterator());
/* 410 */       if (backingItr.hasNext() && this.upperBoundWindow.upperBound
/* 411 */         .isLessThan(((Range)backingItr.peek()).upperBound)) {
/* 412 */         backingItr.next();
/*     */       }
/* 414 */       return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>()
/*     */         {
/*     */           protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/* 417 */             if (!backingItr.hasNext()) {
/* 418 */               return endOfData();
/*     */             }
/* 420 */             Range<C> range = backingItr.next();
/* 421 */             return TreeRangeSet.RangesByUpperBound.this.upperBoundWindow.lowerBound.isLessThan(range.upperBound) ? 
/* 422 */               Maps.<Cut<C>, Range<C>>immutableEntry(range.upperBound, range) : 
/* 423 */               endOfData();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 430 */       if (this.upperBoundWindow.equals(Range.all())) {
/* 431 */         return this.rangesByLowerBound.size();
/*     */       }
/* 433 */       return Iterators.size(entryIterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 438 */       return this.upperBoundWindow.equals(Range.all()) ? 
/* 439 */         this.rangesByLowerBound.isEmpty() : (
/* 440 */         !entryIterator().hasNext());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ComplementRangesByLowerBound<C extends Comparable<?>>
/*     */     extends AbstractNavigableMap<Cut<C>, Range<C>>
/*     */   {
/*     */     private final NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound;
/*     */     
/*     */     private final NavigableMap<Cut<C>, Range<C>> positiveRangesByUpperBound;
/*     */     
/*     */     private final Range<Cut<C>> complementLowerBoundWindow;
/*     */ 
/*     */     
/*     */     ComplementRangesByLowerBound(NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound) {
/* 457 */       this(positiveRangesByLowerBound, Range.all());
/*     */     }
/*     */ 
/*     */     
/*     */     private ComplementRangesByLowerBound(NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound, Range<Cut<C>> window) {
/* 462 */       this.positiveRangesByLowerBound = positiveRangesByLowerBound;
/* 463 */       this.positiveRangesByUpperBound = new TreeRangeSet.RangesByUpperBound<>(positiveRangesByLowerBound);
/* 464 */       this.complementLowerBoundWindow = window;
/*     */     }
/*     */     
/*     */     private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> subWindow) {
/* 468 */       if (!this.complementLowerBoundWindow.isConnected(subWindow)) {
/* 469 */         return ImmutableSortedMap.of();
/*     */       }
/* 471 */       subWindow = subWindow.intersection(this.complementLowerBoundWindow);
/* 472 */       return new ComplementRangesByLowerBound(this.positiveRangesByLowerBound, subWindow);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> fromKey, boolean fromInclusive, Cut<C> toKey, boolean toInclusive) {
/* 479 */       return subMap(
/* 480 */           Range.range(fromKey, 
/* 481 */             BoundType.forBoolean(fromInclusive), toKey, 
/* 482 */             BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> toKey, boolean inclusive) {
/* 487 */       return subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> fromKey, boolean inclusive) {
/* 492 */       return subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public Comparator<? super Cut<C>> comparator() {
/* 497 */       return Ordering.natural();
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
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator() {
/*     */       Collection<Range<C>> positiveRanges;
/*     */       final Cut<C> firstComplementRangeLowerBound;
/* 512 */       if (this.complementLowerBoundWindow.hasLowerBound()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 518 */         positiveRanges = this.positiveRangesByUpperBound.tailMap(this.complementLowerBoundWindow.lowerEndpoint(), (this.complementLowerBoundWindow.lowerBoundType() == BoundType.CLOSED)).values();
/*     */       } else {
/* 520 */         positiveRanges = this.positiveRangesByUpperBound.values();
/*     */       } 
/*     */       
/* 523 */       final PeekingIterator<Range<C>> positiveItr = Iterators.peekingIterator(positiveRanges.iterator());
/*     */       
/* 525 */       if (this.complementLowerBoundWindow.contains((Cut)Cut.belowAll()) && (
/* 526 */         !positiveItr.hasNext() || ((Range)positiveItr.peek()).lowerBound != Cut.belowAll())) {
/* 527 */         firstComplementRangeLowerBound = Cut.belowAll();
/* 528 */       } else if (positiveItr.hasNext()) {
/* 529 */         firstComplementRangeLowerBound = ((Range)positiveItr.next()).upperBound;
/*     */       } else {
/* 531 */         return Iterators.emptyIterator();
/*     */       } 
/* 533 */       return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() {
/* 534 */           Cut<C> nextComplementRangeLowerBound = firstComplementRangeLowerBound;
/*     */           
/*     */           protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/*     */             Range<C> negativeRange;
/* 538 */             if (TreeRangeSet.ComplementRangesByLowerBound.this.complementLowerBoundWindow.upperBound.isLessThan(this.nextComplementRangeLowerBound) || this.nextComplementRangeLowerBound == 
/* 539 */               Cut.aboveAll()) {
/* 540 */               return endOfData();
/*     */             }
/*     */             
/* 543 */             if (positiveItr.hasNext()) {
/* 544 */               Range<C> positiveRange = positiveItr.next();
/* 545 */               negativeRange = Range.create(this.nextComplementRangeLowerBound, positiveRange.lowerBound);
/* 546 */               this.nextComplementRangeLowerBound = positiveRange.upperBound;
/*     */             } else {
/* 548 */               negativeRange = Range.create(this.nextComplementRangeLowerBound, (Cut)Cut.aboveAll());
/* 549 */               this.nextComplementRangeLowerBound = Cut.aboveAll();
/*     */             } 
/* 551 */             return Maps.immutableEntry(negativeRange.lowerBound, negativeRange);
/*     */           }
/*     */         };
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
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
/* 569 */       Cut<C> cut, startingPoint = this.complementLowerBoundWindow.hasUpperBound() ? this.complementLowerBoundWindow.upperEndpoint() : (Cut)Cut.<Comparable>aboveAll();
/*     */ 
/*     */       
/* 572 */       boolean inclusive = (this.complementLowerBoundWindow.hasUpperBound() && this.complementLowerBoundWindow.upperBoundType() == BoundType.CLOSED);
/*     */       
/* 574 */       final PeekingIterator<Range<C>> positiveItr = Iterators.peekingIterator(this.positiveRangesByUpperBound
/*     */           
/* 576 */           .headMap(startingPoint, inclusive)
/* 577 */           .descendingMap()
/* 578 */           .values()
/* 579 */           .iterator());
/*     */       
/* 581 */       if (positiveItr.hasNext())
/*     */       
/*     */       { 
/*     */         
/* 585 */         cut = (((Range)positiveItr.peek()).upperBound == Cut.aboveAll()) ? ((Range)positiveItr.next()).lowerBound : this.positiveRangesByLowerBound.higherKey(((Range)positiveItr.peek()).upperBound); }
/* 586 */       else { if (!this.complementLowerBoundWindow.contains((Cut)Cut.belowAll()) || this.positiveRangesByLowerBound
/* 587 */           .containsKey(Cut.belowAll())) {
/* 588 */           return Iterators.emptyIterator();
/*     */         }
/* 590 */         cut = this.positiveRangesByLowerBound.higherKey((Cut)Cut.belowAll()); }
/*     */ 
/*     */       
/* 593 */       final Cut<C> firstComplementRangeUpperBound = (Cut<C>)MoreObjects.firstNonNull(cut, Cut.aboveAll());
/* 594 */       return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() {
/* 595 */           Cut<C> nextComplementRangeUpperBound = firstComplementRangeUpperBound;
/*     */ 
/*     */           
/*     */           protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/* 599 */             if (this.nextComplementRangeUpperBound == Cut.belowAll())
/* 600 */               return endOfData(); 
/* 601 */             if (positiveItr.hasNext()) {
/* 602 */               Range<C> positiveRange = positiveItr.next();
/*     */               
/* 604 */               Range<C> negativeRange = Range.create(positiveRange.upperBound, this.nextComplementRangeUpperBound);
/* 605 */               this.nextComplementRangeUpperBound = positiveRange.lowerBound;
/* 606 */               if (TreeRangeSet.ComplementRangesByLowerBound.this.complementLowerBoundWindow.lowerBound.isLessThan(negativeRange.lowerBound)) {
/* 607 */                 return Maps.immutableEntry(negativeRange.lowerBound, negativeRange);
/*     */               }
/* 609 */             } else if (TreeRangeSet.ComplementRangesByLowerBound.this.complementLowerBoundWindow.lowerBound.isLessThan(Cut.belowAll())) {
/* 610 */               Range<C> negativeRange = Range.create((Cut)Cut.belowAll(), this.nextComplementRangeUpperBound);
/* 611 */               this.nextComplementRangeUpperBound = Cut.belowAll();
/* 612 */               return Maps.immutableEntry((Cut)Cut.belowAll(), negativeRange);
/*     */             } 
/* 614 */             return endOfData();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 621 */       return Iterators.size(entryIterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public Range<C> get(Object key) {
/* 626 */       if (key instanceof Cut) {
/*     */         
/*     */         try {
/* 629 */           Cut<C> cut = (Cut<C>)key;
/*     */           
/* 631 */           Map.Entry<Cut<C>, Range<C>> firstEntry = tailMap(cut, true).firstEntry();
/* 632 */           if (firstEntry != null && ((Cut)firstEntry.getKey()).equals(cut)) {
/* 633 */             return firstEntry.getValue();
/*     */           }
/* 635 */         } catch (ClassCastException e) {
/* 636 */           return null;
/*     */         } 
/*     */       }
/* 639 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 644 */       return (get(key) != null);
/*     */     }
/*     */   }
/*     */   
/*     */   private final class Complement extends TreeRangeSet<C> {
/*     */     Complement() {
/* 650 */       super(new TreeRangeSet.ComplementRangesByLowerBound<>(TreeRangeSet.this.rangesByLowerBound));
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(Range<C> rangeToAdd) {
/* 655 */       TreeRangeSet.this.remove(rangeToAdd);
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove(Range<C> rangeToRemove) {
/* 660 */       TreeRangeSet.this.add(rangeToRemove);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(C value) {
/* 665 */       return !TreeRangeSet.this.contains((Comparable)value);
/*     */     }
/*     */ 
/*     */     
/*     */     public RangeSet<C> complement() {
/* 670 */       return TreeRangeSet.this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class SubRangeSetRangesByLowerBound<C extends Comparable<?>>
/*     */     extends AbstractNavigableMap<Cut<C>, Range<C>>
/*     */   {
/*     */     private final Range<Cut<C>> lowerBoundWindow;
/*     */ 
/*     */ 
/*     */     
/*     */     private final Range<C> restriction;
/*     */ 
/*     */     
/*     */     private final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
/*     */ 
/*     */     
/*     */     private final NavigableMap<Cut<C>, Range<C>> rangesByUpperBound;
/*     */ 
/*     */ 
/*     */     
/*     */     private SubRangeSetRangesByLowerBound(Range<Cut<C>> lowerBoundWindow, Range<C> restriction, NavigableMap<Cut<C>, Range<C>> rangesByLowerBound) {
/* 695 */       this.lowerBoundWindow = (Range<Cut<C>>)Preconditions.checkNotNull(lowerBoundWindow);
/* 696 */       this.restriction = (Range<C>)Preconditions.checkNotNull(restriction);
/* 697 */       this.rangesByLowerBound = (NavigableMap<Cut<C>, Range<C>>)Preconditions.checkNotNull(rangesByLowerBound);
/* 698 */       this.rangesByUpperBound = new TreeRangeSet.RangesByUpperBound<>(rangesByLowerBound);
/*     */     }
/*     */     
/*     */     private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> window) {
/* 702 */       if (!window.isConnected(this.lowerBoundWindow)) {
/* 703 */         return ImmutableSortedMap.of();
/*     */       }
/* 705 */       return new SubRangeSetRangesByLowerBound(this.lowerBoundWindow
/* 706 */           .intersection(window), this.restriction, this.rangesByLowerBound);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> fromKey, boolean fromInclusive, Cut<C> toKey, boolean toInclusive) {
/* 713 */       return subMap(
/* 714 */           Range.range(fromKey, 
/*     */             
/* 716 */             BoundType.forBoolean(fromInclusive), toKey, 
/*     */             
/* 718 */             BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> toKey, boolean inclusive) {
/* 723 */       return subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> fromKey, boolean inclusive) {
/* 728 */       return subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public Comparator<? super Cut<C>> comparator() {
/* 733 */       return Ordering.natural();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 738 */       return (get(key) != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public Range<C> get(Object key) {
/* 743 */       if (key instanceof Cut) {
/*     */         
/*     */         try {
/* 746 */           Cut<C> cut = (Cut<C>)key;
/* 747 */           if (!this.lowerBoundWindow.contains(cut) || cut
/* 748 */             .compareTo(this.restriction.lowerBound) < 0 || cut
/* 749 */             .compareTo(this.restriction.upperBound) >= 0)
/* 750 */             return null; 
/* 751 */           if (cut.equals(this.restriction.lowerBound)) {
/*     */             
/* 753 */             Range<C> candidate = Maps.<Range<C>>valueOrNull(this.rangesByLowerBound.floorEntry(cut));
/* 754 */             if (candidate != null && candidate.upperBound.compareTo(this.restriction.lowerBound) > 0) {
/* 755 */               return candidate.intersection(this.restriction);
/*     */             }
/*     */           } else {
/* 758 */             Range<C> result = this.rangesByLowerBound.get(cut);
/* 759 */             if (result != null) {
/* 760 */               return result.intersection(this.restriction);
/*     */             }
/*     */           } 
/* 763 */         } catch (ClassCastException e) {
/* 764 */           return null;
/*     */         } 
/*     */       }
/* 767 */       return null;
/*     */     }
/*     */     
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator() {
/*     */       final Iterator<Range<C>> completeRangeItr;
/* 772 */       if (this.restriction.isEmpty()) {
/* 773 */         return Iterators.emptyIterator();
/*     */       }
/*     */       
/* 776 */       if (this.lowerBoundWindow.upperBound.isLessThan(this.restriction.lowerBound))
/* 777 */         return Iterators.emptyIterator(); 
/* 778 */       if (this.lowerBoundWindow.lowerBound.isLessThan(this.restriction.lowerBound)) {
/*     */ 
/*     */         
/* 781 */         completeRangeItr = this.rangesByUpperBound.tailMap(this.restriction.lowerBound, false).values().iterator();
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */ 
/*     */         
/* 790 */         completeRangeItr = this.rangesByLowerBound.tailMap(this.lowerBoundWindow.lowerBound.endpoint(), (this.lowerBoundWindow.lowerBoundType() == BoundType.CLOSED)).values().iterator();
/*     */       } 
/*     */ 
/*     */       
/* 794 */       final Cut<Cut<C>> upperBoundOnLowerBounds = (Cut<Cut<C>>)Ordering.<Comparable>natural().min(this.lowerBoundWindow.upperBound, Cut.belowValue(this.restriction.upperBound));
/* 795 */       return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>()
/*     */         {
/*     */           protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/* 798 */             if (!completeRangeItr.hasNext()) {
/* 799 */               return endOfData();
/*     */             }
/* 801 */             Range<C> nextRange = completeRangeItr.next();
/* 802 */             if (upperBoundOnLowerBounds.isLessThan(nextRange.lowerBound)) {
/* 803 */               return endOfData();
/*     */             }
/* 805 */             nextRange = nextRange.intersection(TreeRangeSet.SubRangeSetRangesByLowerBound.this.restriction);
/* 806 */             return Maps.immutableEntry(nextRange.lowerBound, nextRange);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
/* 814 */       if (this.restriction.isEmpty()) {
/* 815 */         return Iterators.emptyIterator();
/*     */       }
/*     */ 
/*     */       
/* 819 */       Cut<Cut<C>> upperBoundOnLowerBounds = (Cut<Cut<C>>)Ordering.<Comparable>natural().min(this.lowerBoundWindow.upperBound, Cut.belowValue(this.restriction.upperBound));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 827 */       final Iterator<Range<C>> completeRangeItr = this.rangesByLowerBound.headMap(upperBoundOnLowerBounds.endpoint(), (upperBoundOnLowerBounds.typeAsUpperBound() == BoundType.CLOSED)).descendingMap().values().iterator();
/* 828 */       return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>()
/*     */         {
/*     */           protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/* 831 */             if (!completeRangeItr.hasNext()) {
/* 832 */               return endOfData();
/*     */             }
/* 834 */             Range<C> nextRange = completeRangeItr.next();
/* 835 */             if (TreeRangeSet.SubRangeSetRangesByLowerBound.this.restriction.lowerBound.compareTo(nextRange.upperBound) >= 0) {
/* 836 */               return endOfData();
/*     */             }
/* 838 */             nextRange = nextRange.intersection(TreeRangeSet.SubRangeSetRangesByLowerBound.this.restriction);
/* 839 */             if (TreeRangeSet.SubRangeSetRangesByLowerBound.this.lowerBoundWindow.contains(nextRange.lowerBound)) {
/* 840 */               return Maps.immutableEntry(nextRange.lowerBound, nextRange);
/*     */             }
/* 842 */             return endOfData();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 850 */       return Iterators.size(entryIterator());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public RangeSet<C> subRangeSet(Range<C> view) {
/* 856 */     return view.equals(Range.all()) ? this : new SubRangeSet(view);
/*     */   }
/*     */   
/*     */   private final class SubRangeSet extends TreeRangeSet<C> {
/*     */     private final Range<C> restriction;
/*     */     
/*     */     SubRangeSet(Range<C> restriction) {
/* 863 */       super(new TreeRangeSet.SubRangeSetRangesByLowerBound<>(
/*     */             
/* 865 */             Range.all(), restriction, TreeRangeSet.this.rangesByLowerBound, null));
/* 866 */       this.restriction = restriction;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean encloses(Range<C> range) {
/* 871 */       if (!this.restriction.isEmpty() && this.restriction.encloses(range)) {
/* 872 */         Range<C> enclosing = TreeRangeSet.this.rangeEnclosing(range);
/* 873 */         return (enclosing != null && !enclosing.intersection(this.restriction).isEmpty());
/*     */       } 
/* 875 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Range<C> rangeContaining(C value) {
/* 880 */       if (!this.restriction.contains(value)) {
/* 881 */         return null;
/*     */       }
/* 883 */       Range<C> result = TreeRangeSet.this.rangeContaining(value);
/* 884 */       return (result == null) ? null : result.intersection(this.restriction);
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(Range<C> rangeToAdd) {
/* 889 */       Preconditions.checkArgument(this.restriction
/* 890 */           .encloses(rangeToAdd), "Cannot add range %s to subRangeSet(%s)", rangeToAdd, this.restriction);
/*     */ 
/*     */ 
/*     */       
/* 894 */       TreeRangeSet.this.add(rangeToAdd);
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove(Range<C> rangeToRemove) {
/* 899 */       if (rangeToRemove.isConnected(this.restriction)) {
/* 900 */         TreeRangeSet.this.remove(rangeToRemove.intersection(this.restriction));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(C value) {
/* 906 */       return (this.restriction.contains(value) && TreeRangeSet.this.contains((Comparable)value));
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 911 */       TreeRangeSet.this.remove(this.restriction);
/*     */     }
/*     */ 
/*     */     
/*     */     public RangeSet<C> subRangeSet(Range<C> view) {
/* 916 */       if (view.encloses(this.restriction))
/* 917 */         return this; 
/* 918 */       if (view.isConnected(this.restriction)) {
/* 919 */         return new SubRangeSet(this.restriction.intersection(view));
/*     */       }
/* 921 */       return (RangeSet)ImmutableRangeSet.of();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\TreeRangeSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */