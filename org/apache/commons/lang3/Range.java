/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Range<T>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final Comparator<T> comparator;
/*     */   private transient int hashCode;
/*     */   private final T maximum;
/*     */   private final T minimum;
/*     */   private transient String toString;
/*     */   
/*     */   private enum ComparableComparator
/*     */     implements Comparator
/*     */   {
/*  38 */     INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int compare(Object obj1, Object obj2) {
/*  49 */       return ((Comparable<Object>)obj1).compareTo(obj2);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static <T extends Comparable<? super T>> Range<T> between(T fromInclusive, T toInclusive) {
/*  80 */     return of(fromInclusive, toInclusive, null);
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
/*     */   @Deprecated
/*     */   public static <T> Range<T> between(T fromInclusive, T toInclusive, Comparator<T> comparator) {
/* 104 */     return new Range<>(fromInclusive, toInclusive, comparator);
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
/*     */   public static <T extends Comparable<? super T>> Range<T> is(T element) {
/* 121 */     return of(element, element, null);
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
/*     */   public static <T> Range<T> is(T element, Comparator<T> comparator) {
/* 139 */     return of(element, element, comparator);
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
/*     */   public static <T extends Comparable<? super T>> Range<T> of(T fromInclusive, T toInclusive) {
/* 160 */     return of(fromInclusive, toInclusive, null);
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
/*     */   public static <T> Range<T> of(T fromInclusive, T toInclusive, Comparator<T> comparator) {
/* 183 */     return new Range<>(fromInclusive, toInclusive, comparator);
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
/*     */   Range(T element1, T element2, Comparator<T> comp) {
/* 222 */     Objects.requireNonNull(element1, "element1");
/* 223 */     Objects.requireNonNull(element2, "element2");
/* 224 */     if (comp == null) {
/* 225 */       this.comparator = ComparableComparator.INSTANCE;
/*     */     } else {
/* 227 */       this.comparator = comp;
/*     */     } 
/* 229 */     if (this.comparator.compare(element1, element2) < 1) {
/* 230 */       this.minimum = element1;
/* 231 */       this.maximum = element2;
/*     */     } else {
/* 233 */       this.minimum = element2;
/* 234 */       this.maximum = element1;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(T element) {
/* 245 */     if (element == null) {
/* 246 */       return false;
/*     */     }
/* 248 */     return (this.comparator.compare(element, this.minimum) > -1 && this.comparator.compare(element, this.maximum) < 1);
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
/*     */   public boolean containsRange(Range<T> otherRange) {
/* 261 */     if (otherRange == null) {
/* 262 */       return false;
/*     */     }
/* 264 */     return (contains(otherRange.minimum) && 
/* 265 */       contains(otherRange.maximum));
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
/*     */   public int elementCompareTo(T element) {
/* 281 */     Objects.requireNonNull(element, "element");
/* 282 */     if (isAfter(element)) {
/* 283 */       return -1;
/*     */     }
/* 285 */     if (isBefore(element)) {
/* 286 */       return 1;
/*     */     }
/* 288 */     return 0;
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
/*     */   public boolean equals(Object obj) {
/* 302 */     if (obj == this) {
/* 303 */       return true;
/*     */     }
/* 305 */     if (obj == null || obj.getClass() != getClass()) {
/* 306 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 310 */     Range<T> range = (Range<T>)obj;
/* 311 */     return (this.minimum.equals(range.minimum) && this.maximum
/* 312 */       .equals(range.maximum));
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
/*     */   public T fit(T element) {
/* 338 */     Objects.requireNonNull(element, "element");
/* 339 */     if (isAfter(element)) {
/* 340 */       return this.minimum;
/*     */     }
/* 342 */     if (isBefore(element)) {
/* 343 */       return this.maximum;
/*     */     }
/* 345 */     return element;
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
/*     */   public Comparator<T> getComparator() {
/* 357 */     return this.comparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T getMaximum() {
/* 366 */     return this.maximum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T getMinimum() {
/* 375 */     return this.minimum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 385 */     int result = this.hashCode;
/* 386 */     if (this.hashCode == 0) {
/* 387 */       result = 17;
/* 388 */       result = 37 * result + getClass().hashCode();
/* 389 */       result = 37 * result + this.minimum.hashCode();
/* 390 */       result = 37 * result + this.maximum.hashCode();
/* 391 */       this.hashCode = result;
/*     */     } 
/* 393 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Range<T> intersectionWith(Range<T> other) {
/* 404 */     if (!isOverlappedBy(other)) {
/* 405 */       throw new IllegalArgumentException(String.format("Cannot calculate intersection with non-overlapping range %s", new Object[] { other }));
/*     */     }
/*     */     
/* 408 */     if (equals(other)) {
/* 409 */       return this;
/*     */     }
/* 411 */     T min = (getComparator().compare(this.minimum, other.minimum) < 0) ? other.minimum : this.minimum;
/* 412 */     T max = (getComparator().compare(this.maximum, other.maximum) < 0) ? this.maximum : other.maximum;
/* 413 */     return of(min, max, getComparator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAfter(T element) {
/* 423 */     if (element == null) {
/* 424 */       return false;
/*     */     }
/* 426 */     return (this.comparator.compare(element, this.minimum) < 0);
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
/*     */   public boolean isAfterRange(Range<T> otherRange) {
/* 439 */     if (otherRange == null) {
/* 440 */       return false;
/*     */     }
/* 442 */     return isAfter(otherRange.maximum);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBefore(T element) {
/* 452 */     if (element == null) {
/* 453 */       return false;
/*     */     }
/* 455 */     return (this.comparator.compare(element, this.maximum) > 0);
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
/*     */   public boolean isBeforeRange(Range<T> otherRange) {
/* 468 */     if (otherRange == null) {
/* 469 */       return false;
/*     */     }
/* 471 */     return isBefore(otherRange.minimum);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEndedBy(T element) {
/* 481 */     if (element == null) {
/* 482 */       return false;
/*     */     }
/* 484 */     return (this.comparator.compare(element, this.maximum) == 0);
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
/*     */   public boolean isNaturalOrdering() {
/* 496 */     return (this.comparator == ComparableComparator.INSTANCE);
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
/*     */   public boolean isOverlappedBy(Range<T> otherRange) {
/* 512 */     if (otherRange == null) {
/* 513 */       return false;
/*     */     }
/* 515 */     return (otherRange.contains(this.minimum) || otherRange
/* 516 */       .contains(this.maximum) || 
/* 517 */       contains(otherRange.minimum));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStartedBy(T element) {
/* 527 */     if (element == null) {
/* 528 */       return false;
/*     */     }
/* 530 */     return (this.comparator.compare(element, this.minimum) == 0);
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
/*     */   public String toString() {
/* 542 */     if (this.toString == null) {
/* 543 */       this.toString = "[" + this.minimum + ".." + this.maximum + "]";
/*     */     }
/* 545 */     return this.toString;
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
/*     */   public String toString(String format) {
/* 561 */     return String.format(format, new Object[] { this.minimum, this.maximum, this.comparator });
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\Range.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */