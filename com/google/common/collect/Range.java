/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.SortedSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Range<C extends Comparable>
/*     */   extends RangeGwtSerializationDependencies
/*     */   implements Predicate<C>, Serializable
/*     */ {
/*     */   static class LowerBoundFn
/*     */     implements Function<Range, Cut>
/*     */   {
/* 124 */     static final LowerBoundFn INSTANCE = new LowerBoundFn();
/*     */ 
/*     */     
/*     */     public Cut apply(Range range) {
/* 128 */       return range.lowerBound;
/*     */     }
/*     */   }
/*     */   
/*     */   static class UpperBoundFn implements Function<Range, Cut> {
/* 133 */     static final UpperBoundFn INSTANCE = new UpperBoundFn();
/*     */ 
/*     */     
/*     */     public Cut apply(Range range) {
/* 137 */       return range.upperBound;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static <C extends Comparable<?>> Function<Range<C>, Cut<C>> lowerBoundFn() {
/* 143 */     return LowerBoundFn.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   static <C extends Comparable<?>> Function<Range<C>, Cut<C>> upperBoundFn() {
/* 148 */     return UpperBoundFn.INSTANCE;
/*     */   }
/*     */   
/*     */   static <C extends Comparable<?>> Ordering<Range<C>> rangeLexOrdering() {
/* 152 */     return (Ordering)RangeLexOrdering.INSTANCE;
/*     */   }
/*     */   
/*     */   static <C extends Comparable<?>> Range<C> create(Cut<C> lowerBound, Cut<C> upperBound) {
/* 156 */     return (Range)new Range<>(lowerBound, upperBound);
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
/*     */   public static <C extends Comparable<?>> Range<C> open(C lower, C upper) {
/* 169 */     return create((Cut)Cut.aboveValue((Comparable)lower), (Cut)Cut.belowValue((Comparable)upper));
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
/*     */   public static <C extends Comparable<?>> Range<C> closed(C lower, C upper) {
/* 181 */     return create((Cut)Cut.belowValue((Comparable)lower), (Cut)Cut.aboveValue((Comparable)upper));
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
/*     */   public static <C extends Comparable<?>> Range<C> closedOpen(C lower, C upper) {
/* 193 */     return create((Cut)Cut.belowValue((Comparable)lower), (Cut)Cut.belowValue((Comparable)upper));
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
/*     */   public static <C extends Comparable<?>> Range<C> openClosed(C lower, C upper) {
/* 205 */     return create((Cut)Cut.aboveValue((Comparable)lower), (Cut)Cut.aboveValue((Comparable)upper));
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
/*     */   public static <C extends Comparable<?>> Range<C> range(C lower, BoundType lowerType, C upper, BoundType upperType) {
/* 218 */     Preconditions.checkNotNull(lowerType);
/* 219 */     Preconditions.checkNotNull(upperType);
/*     */ 
/*     */     
/* 222 */     Cut<C> lowerBound = (lowerType == BoundType.OPEN) ? (Cut)Cut.<Comparable>aboveValue((Comparable)lower) : (Cut)Cut.<Comparable>belowValue((Comparable)lower);
/*     */     
/* 224 */     Cut<C> upperBound = (upperType == BoundType.OPEN) ? (Cut)Cut.<Comparable>belowValue((Comparable)upper) : (Cut)Cut.<Comparable>aboveValue((Comparable)upper);
/* 225 */     return create(lowerBound, upperBound);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> lessThan(C endpoint) {
/* 234 */     return create((Cut)Cut.belowAll(), (Cut)Cut.belowValue((Comparable)endpoint));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> atMost(C endpoint) {
/* 243 */     return create((Cut)Cut.belowAll(), (Cut)Cut.aboveValue((Comparable)endpoint));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> upTo(C endpoint, BoundType boundType) {
/* 253 */     switch (boundType) {
/*     */       case OPEN:
/* 255 */         return lessThan(endpoint);
/*     */       case CLOSED:
/* 257 */         return atMost(endpoint);
/*     */     } 
/* 259 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> greaterThan(C endpoint) {
/* 269 */     return create((Cut)Cut.aboveValue((Comparable)endpoint), (Cut)Cut.aboveAll());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> atLeast(C endpoint) {
/* 278 */     return create((Cut)Cut.belowValue((Comparable)endpoint), (Cut)Cut.aboveAll());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> downTo(C endpoint, BoundType boundType) {
/* 288 */     switch (boundType) {
/*     */       case OPEN:
/* 290 */         return greaterThan(endpoint);
/*     */       case CLOSED:
/* 292 */         return atLeast(endpoint);
/*     */     } 
/* 294 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   
/* 298 */   private static final Range<Comparable> ALL = new Range((Cut)Cut.belowAll(), (Cut)Cut.aboveAll());
/*     */   
/*     */   final Cut<C> lowerBound;
/*     */   
/*     */   final Cut<C> upperBound;
/*     */   
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> all() {
/* 307 */     return (Range)ALL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> singleton(C value) {
/* 317 */     return closed(value, value);
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
/*     */   public static <C extends Comparable<?>> Range<C> encloseAll(Iterable<C> values) {
/* 330 */     Preconditions.checkNotNull(values);
/* 331 */     if (values instanceof SortedSet) {
/* 332 */       SortedSet<? extends C> set = cast(values);
/* 333 */       Comparator<?> comparator = set.comparator();
/* 334 */       if (Ordering.<Comparable>natural().equals(comparator) || comparator == null) {
/* 335 */         return closed(set.first(), set.last());
/*     */       }
/*     */     } 
/* 338 */     Iterator<C> valueIterator = values.iterator();
/* 339 */     Comparable comparable1 = (Comparable)Preconditions.checkNotNull((Comparable)valueIterator.next());
/* 340 */     Comparable comparable2 = comparable1;
/* 341 */     while (valueIterator.hasNext()) {
/* 342 */       Comparable comparable = (Comparable)Preconditions.checkNotNull((Comparable)valueIterator.next());
/* 343 */       comparable1 = (Comparable)Ordering.<Comparable>natural().min(comparable1, comparable);
/* 344 */       comparable2 = (Comparable)Ordering.<Comparable>natural().max(comparable2, comparable);
/*     */     } 
/* 346 */     return closed((C)comparable1, (C)comparable2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Range(Cut<C> lowerBound, Cut<C> upperBound) {
/* 353 */     this.lowerBound = (Cut<C>)Preconditions.checkNotNull(lowerBound);
/* 354 */     this.upperBound = (Cut<C>)Preconditions.checkNotNull(upperBound);
/* 355 */     if (lowerBound.compareTo(upperBound) > 0 || lowerBound == 
/* 356 */       Cut.aboveAll() || upperBound == 
/* 357 */       Cut.belowAll()) {
/* 358 */       String.valueOf(toString(lowerBound, upperBound)); throw new IllegalArgumentException((String.valueOf(toString(lowerBound, upperBound)).length() != 0) ? "Invalid range: ".concat(String.valueOf(toString(lowerBound, upperBound))) : new String("Invalid range: "));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasLowerBound() {
/* 364 */     return (this.lowerBound != Cut.belowAll());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public C lowerEndpoint() {
/* 374 */     return this.lowerBound.endpoint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundType lowerBoundType() {
/* 385 */     return this.lowerBound.typeAsLowerBound();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasUpperBound() {
/* 390 */     return (this.upperBound != Cut.aboveAll());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public C upperEndpoint() {
/* 400 */     return this.upperBound.endpoint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundType upperBoundType() {
/* 411 */     return this.upperBound.typeAsUpperBound();
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
/*     */   public boolean isEmpty() {
/* 424 */     return this.lowerBound.equals(this.upperBound);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(C value) {
/* 433 */     Preconditions.checkNotNull(value);
/*     */     
/* 435 */     return (this.lowerBound.isLessThan(value) && !this.upperBound.isLessThan(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean apply(C input) {
/* 445 */     return contains(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsAll(Iterable<? extends C> values) {
/* 453 */     if (Iterables.isEmpty(values)) {
/* 454 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 458 */     if (values instanceof SortedSet) {
/* 459 */       SortedSet<? extends C> set = cast(values);
/* 460 */       Comparator<?> comparator = set.comparator();
/* 461 */       if (Ordering.<Comparable>natural().equals(comparator) || comparator == null) {
/* 462 */         return (contains(set.first()) && contains(set.last()));
/*     */       }
/*     */     } 
/*     */     
/* 466 */     for (Comparable comparable : values) {
/* 467 */       if (!contains((C)comparable)) {
/* 468 */         return false;
/*     */       }
/*     */     } 
/* 471 */     return true;
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
/*     */   public boolean encloses(Range<C> other) {
/* 498 */     return (this.lowerBound.compareTo(other.lowerBound) <= 0 && this.upperBound
/* 499 */       .compareTo(other.upperBound) >= 0);
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
/*     */   public boolean isConnected(Range<C> other) {
/* 528 */     return (this.lowerBound.compareTo(other.upperBound) <= 0 && other.lowerBound
/* 529 */       .compareTo(this.upperBound) <= 0);
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
/*     */   public Range<C> intersection(Range<C> connectedRange) {
/* 549 */     int lowerCmp = this.lowerBound.compareTo(connectedRange.lowerBound);
/* 550 */     int upperCmp = this.upperBound.compareTo(connectedRange.upperBound);
/* 551 */     if (lowerCmp >= 0 && upperCmp <= 0)
/* 552 */       return this; 
/* 553 */     if (lowerCmp <= 0 && upperCmp >= 0) {
/* 554 */       return connectedRange;
/*     */     }
/* 556 */     Cut<C> newLower = (lowerCmp >= 0) ? this.lowerBound : connectedRange.lowerBound;
/* 557 */     Cut<C> newUpper = (upperCmp <= 0) ? this.upperBound : connectedRange.upperBound;
/* 558 */     return (Range)create(newLower, newUpper);
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
/*     */   public Range<C> gap(Range<C> otherRange) {
/* 590 */     if (this.lowerBound.compareTo(otherRange.upperBound) < 0 && otherRange.lowerBound
/* 591 */       .compareTo(this.upperBound) < 0) {
/* 592 */       String str1 = String.valueOf(this), str2 = String.valueOf(otherRange); throw new IllegalArgumentException((new StringBuilder(39 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("Ranges have a nonempty intersection: ").append(str1).append(", ").append(str2).toString());
/*     */     } 
/*     */ 
/*     */     
/* 596 */     boolean isThisFirst = (this.lowerBound.compareTo(otherRange.lowerBound) < 0);
/* 597 */     Range<C> firstRange = isThisFirst ? this : otherRange;
/* 598 */     Range<C> secondRange = isThisFirst ? otherRange : this;
/* 599 */     return (Range)create(firstRange.upperBound, secondRange.lowerBound);
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
/*     */   public Range<C> span(Range<C> other) {
/* 614 */     int lowerCmp = this.lowerBound.compareTo(other.lowerBound);
/* 615 */     int upperCmp = this.upperBound.compareTo(other.upperBound);
/* 616 */     if (lowerCmp <= 0 && upperCmp >= 0)
/* 617 */       return this; 
/* 618 */     if (lowerCmp >= 0 && upperCmp <= 0) {
/* 619 */       return other;
/*     */     }
/* 621 */     Cut<C> newLower = (lowerCmp <= 0) ? this.lowerBound : other.lowerBound;
/* 622 */     Cut<C> newUpper = (upperCmp >= 0) ? this.upperBound : other.upperBound;
/* 623 */     return (Range)create(newLower, newUpper);
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
/*     */   public Range<C> canonical(DiscreteDomain<C> domain) {
/* 652 */     Preconditions.checkNotNull(domain);
/* 653 */     Cut<C> lower = this.lowerBound.canonical(domain);
/* 654 */     Cut<C> upper = this.upperBound.canonical(domain);
/* 655 */     return (lower == this.lowerBound && upper == this.upperBound) ? this : (Range)create(lower, upper);
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
/*     */   public boolean equals(Object object) {
/* 667 */     if (object instanceof Range) {
/* 668 */       Range<?> other = (Range)object;
/* 669 */       return (this.lowerBound.equals(other.lowerBound) && this.upperBound.equals(other.upperBound));
/*     */     } 
/* 671 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 677 */     return this.lowerBound.hashCode() * 31 + this.upperBound.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 686 */     return toString(this.lowerBound, this.upperBound);
/*     */   }
/*     */   
/*     */   private static String toString(Cut<?> lowerBound, Cut<?> upperBound) {
/* 690 */     StringBuilder sb = new StringBuilder(16);
/* 691 */     lowerBound.describeAsLowerBound(sb);
/* 692 */     sb.append("..");
/* 693 */     upperBound.describeAsUpperBound(sb);
/* 694 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> SortedSet<T> cast(Iterable<T> iterable) {
/* 699 */     return (SortedSet<T>)iterable;
/*     */   }
/*     */   
/*     */   Object readResolve() {
/* 703 */     if (equals(ALL)) {
/* 704 */       return all();
/*     */     }
/* 706 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static int compareOrThrow(Comparable<Comparable> left, Comparable right) {
/* 712 */     return left.compareTo(right);
/*     */   }
/*     */   
/*     */   private static class RangeLexOrdering
/*     */     extends Ordering<Range<?>> implements Serializable {
/* 717 */     static final Ordering<Range<?>> INSTANCE = new RangeLexOrdering();
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public int compare(Range<?> left, Range<?> right) {
/* 721 */       return ComparisonChain.start()
/* 722 */         .compare(left.lowerBound, right.lowerBound)
/* 723 */         .compare(left.upperBound, right.upperBound)
/* 724 */         .result();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\Range.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */