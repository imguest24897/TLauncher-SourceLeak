/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.util.AbstractQueue;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Queue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtCompatible
/*     */ public final class MinMaxPriorityQueue<E>
/*     */   extends AbstractQueue<E>
/*     */ {
/*     */   private final Heap minHeap;
/*     */   private final Heap maxHeap;
/*     */   @VisibleForTesting
/*     */   final int maximumSize;
/*     */   private Object[] queue;
/*     */   private int size;
/*     */   private int modCount;
/*     */   private static final int EVEN_POWERS_OF_TWO = 1431655765;
/*     */   private static final int ODD_POWERS_OF_TWO = -1431655766;
/*     */   private static final int DEFAULT_CAPACITY = 11;
/*     */   
/*     */   public static <E extends Comparable<E>> MinMaxPriorityQueue<E> create() {
/* 108 */     return (new Builder(Ordering.natural())).create();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<E>> MinMaxPriorityQueue<E> create(Iterable<? extends E> initialContents) {
/* 117 */     return (new Builder(Ordering.natural())).create(initialContents);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <B> Builder<B> orderedBy(Comparator<B> comparator) {
/* 125 */     return new Builder<>(comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder<Comparable> expectedSize(int expectedSize) {
/* 133 */     return (new Builder<>(Ordering.natural())).expectedSize(expectedSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder<Comparable> maximumSize(int maximumSize) {
/* 143 */     return (new Builder<>(Ordering.natural())).maximumSize(maximumSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static final class Builder<B>
/*     */   {
/*     */     private static final int UNSET_EXPECTED_SIZE = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Comparator<B> comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 165 */     private int expectedSize = -1;
/* 166 */     private int maximumSize = Integer.MAX_VALUE;
/*     */     
/*     */     private Builder(Comparator<B> comparator) {
/* 169 */       this.comparator = (Comparator<B>)Preconditions.checkNotNull(comparator);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<B> expectedSize(int expectedSize) {
/* 178 */       Preconditions.checkArgument((expectedSize >= 0));
/* 179 */       this.expectedSize = expectedSize;
/* 180 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<B> maximumSize(int maximumSize) {
/* 191 */       Preconditions.checkArgument((maximumSize > 0));
/* 192 */       this.maximumSize = maximumSize;
/* 193 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T extends B> MinMaxPriorityQueue<T> create() {
/* 201 */       return create(Collections.emptySet());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T extends B> MinMaxPriorityQueue<T> create(Iterable<? extends T> initialContents) {
/* 211 */       MinMaxPriorityQueue<T> queue = new MinMaxPriorityQueue<>(this, MinMaxPriorityQueue.initialQueueSize(this.expectedSize, this.maximumSize, initialContents));
/* 212 */       for (T element : initialContents) {
/* 213 */         queue.offer(element);
/*     */       }
/* 215 */       return queue;
/*     */     }
/*     */ 
/*     */     
/*     */     private <T extends B> Ordering<T> ordering() {
/* 220 */       return Ordering.from(this.comparator);
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
/*     */   private MinMaxPriorityQueue(Builder<? super E> builder, int queueSize) {
/* 232 */     Ordering<E> ordering = builder.ordering();
/* 233 */     this.minHeap = new Heap(ordering);
/* 234 */     this.maxHeap = new Heap(ordering.reverse());
/* 235 */     this.minHeap.otherHeap = this.maxHeap;
/* 236 */     this.maxHeap.otherHeap = this.minHeap;
/*     */     
/* 238 */     this.maximumSize = builder.maximumSize;
/*     */     
/* 240 */     this.queue = new Object[queueSize];
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 245 */     return this.size;
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
/*     */   @CanIgnoreReturnValue
/*     */   public boolean add(E element) {
/* 258 */     offer(element);
/* 259 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean addAll(Collection<? extends E> newElements) {
/* 265 */     boolean modified = false;
/* 266 */     for (E element : newElements) {
/* 267 */       offer(element);
/* 268 */       modified = true;
/*     */     } 
/* 270 */     return modified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean offer(E element) {
/* 281 */     Preconditions.checkNotNull(element);
/* 282 */     this.modCount++;
/* 283 */     int insertIndex = this.size++;
/*     */     
/* 285 */     growIfNeeded();
/*     */ 
/*     */ 
/*     */     
/* 289 */     heapForIndex(insertIndex).bubbleUp(insertIndex, element);
/* 290 */     return (this.size <= this.maximumSize || pollLast() != element);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E poll() {
/* 296 */     return isEmpty() ? null : removeAndGet(0);
/*     */   }
/*     */ 
/*     */   
/*     */   E elementData(int index) {
/* 301 */     return (E)this.queue[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public E peek() {
/* 306 */     return isEmpty() ? null : elementData(0);
/*     */   }
/*     */ 
/*     */   
/*     */   private int getMaxElementIndex() {
/* 311 */     switch (this.size) {
/*     */       case 1:
/* 313 */         return 0;
/*     */       case 2:
/* 315 */         return 1;
/*     */     } 
/*     */ 
/*     */     
/* 319 */     return (this.maxHeap.compareElements(1, 2) <= 0) ? 1 : 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E pollFirst() {
/* 329 */     return poll();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E removeFirst() {
/* 339 */     return remove();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E peekFirst() {
/* 347 */     return peek();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E pollLast() {
/* 356 */     return isEmpty() ? null : removeAndGet(getMaxElementIndex());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E removeLast() {
/* 366 */     if (isEmpty()) {
/* 367 */       throw new NoSuchElementException();
/*     */     }
/* 369 */     return removeAndGet(getMaxElementIndex());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E peekLast() {
/* 377 */     return isEmpty() ? null : elementData(getMaxElementIndex());
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
/*     */   @VisibleForTesting
/*     */   @CanIgnoreReturnValue
/*     */   MoveDesc<E> removeAt(int index) {
/* 396 */     Preconditions.checkPositionIndex(index, this.size);
/* 397 */     this.modCount++;
/* 398 */     this.size--;
/* 399 */     if (this.size == index) {
/* 400 */       this.queue[this.size] = null;
/* 401 */       return null;
/*     */     } 
/* 403 */     E actualLastElement = elementData(this.size);
/* 404 */     int lastElementAt = heapForIndex(this.size).swapWithConceptuallyLastElement(actualLastElement);
/* 405 */     if (lastElementAt == index) {
/*     */ 
/*     */ 
/*     */       
/* 409 */       this.queue[this.size] = null;
/* 410 */       return null;
/*     */     } 
/* 412 */     E toTrickle = elementData(this.size);
/* 413 */     this.queue[this.size] = null;
/* 414 */     MoveDesc<E> changes = fillHole(index, toTrickle);
/* 415 */     if (lastElementAt < index) {
/*     */       
/* 417 */       if (changes == null)
/*     */       {
/* 419 */         return new MoveDesc<>(actualLastElement, toTrickle);
/*     */       }
/*     */ 
/*     */       
/* 423 */       return new MoveDesc<>(actualLastElement, changes.replaced);
/*     */     } 
/*     */ 
/*     */     
/* 427 */     return changes;
/*     */   }
/*     */   
/*     */   private MoveDesc<E> fillHole(int index, E toTrickle) {
/* 431 */     Heap heap = heapForIndex(index);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 439 */     int vacated = heap.fillHoleAt(index);
/*     */     
/* 441 */     int bubbledTo = heap.bubbleUpAlternatingLevels(vacated, toTrickle);
/* 442 */     if (bubbledTo == vacated)
/*     */     {
/*     */ 
/*     */       
/* 446 */       return heap.tryCrossOverAndBubbleUp(index, vacated, toTrickle);
/*     */     }
/* 448 */     return (bubbledTo < index) ? new MoveDesc<>(toTrickle, elementData(index)) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   static class MoveDesc<E>
/*     */   {
/*     */     final E toTrickle;
/*     */     final E replaced;
/*     */     
/*     */     MoveDesc(E toTrickle, E replaced) {
/* 458 */       this.toTrickle = toTrickle;
/* 459 */       this.replaced = replaced;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private E removeAndGet(int index) {
/* 465 */     E value = elementData(index);
/* 466 */     removeAt(index);
/* 467 */     return value;
/*     */   }
/*     */   
/*     */   private Heap heapForIndex(int i) {
/* 471 */     return isEvenLevel(i) ? this.minHeap : this.maxHeap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static boolean isEvenLevel(int index) {
/* 479 */     int oneBased = index + 1 ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/* 480 */     Preconditions.checkState((oneBased > 0), "negative index");
/* 481 */     return ((oneBased & 0x55555555) > (oneBased & 0xAAAAAAAA));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   boolean isIntact() {
/* 491 */     for (int i = 1; i < this.size; i++) {
/* 492 */       if (!heapForIndex(i).verifyIndex(i)) {
/* 493 */         return false;
/*     */       }
/*     */     } 
/* 496 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class Heap
/*     */   {
/*     */     final Ordering<E> ordering;
/*     */     
/*     */     @Weak
/*     */     Heap otherHeap;
/*     */ 
/*     */     
/*     */     Heap(Ordering<E> ordering) {
/* 510 */       this.ordering = ordering;
/*     */     }
/*     */     
/*     */     int compareElements(int a, int b) {
/* 514 */       return this.ordering.compare(MinMaxPriorityQueue.this.elementData(a), MinMaxPriorityQueue.this.elementData(b));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     MinMaxPriorityQueue.MoveDesc<E> tryCrossOverAndBubbleUp(int removeIndex, int vacated, E toTrickle) {
/*     */       E parent;
/* 522 */       int crossOver = crossOver(vacated, toTrickle);
/* 523 */       if (crossOver == vacated) {
/* 524 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 532 */       if (crossOver < removeIndex) {
/*     */ 
/*     */         
/* 535 */         parent = MinMaxPriorityQueue.this.elementData(removeIndex);
/*     */       } else {
/* 537 */         parent = MinMaxPriorityQueue.this.elementData(getParentIndex(removeIndex));
/*     */       } 
/*     */       
/* 540 */       if (this.otherHeap.bubbleUpAlternatingLevels(crossOver, toTrickle) < removeIndex) {
/* 541 */         return new MinMaxPriorityQueue.MoveDesc<>(toTrickle, parent);
/*     */       }
/* 543 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     void bubbleUp(int index, E x) {
/*     */       Heap heap;
/* 549 */       int crossOver = crossOverUp(index, x);
/*     */ 
/*     */       
/* 552 */       if (crossOver == index) {
/* 553 */         heap = this;
/*     */       } else {
/* 555 */         index = crossOver;
/* 556 */         heap = this.otherHeap;
/*     */       } 
/* 558 */       heap.bubbleUpAlternatingLevels(index, x);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     int bubbleUpAlternatingLevels(int index, E x) {
/* 567 */       while (index > 2) {
/* 568 */         int grandParentIndex = getGrandparentIndex(index);
/* 569 */         E e = MinMaxPriorityQueue.this.elementData(grandParentIndex);
/* 570 */         if (this.ordering.compare(e, x) <= 0) {
/*     */           break;
/*     */         }
/* 573 */         MinMaxPriorityQueue.this.queue[index] = e;
/* 574 */         index = grandParentIndex;
/*     */       } 
/* 576 */       MinMaxPriorityQueue.this.queue[index] = x;
/* 577 */       return index;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int findMin(int index, int len) {
/* 585 */       if (index >= MinMaxPriorityQueue.this.size) {
/* 586 */         return -1;
/*     */       }
/* 588 */       Preconditions.checkState((index > 0));
/* 589 */       int limit = Math.min(index, MinMaxPriorityQueue.this.size - len) + len;
/* 590 */       int minIndex = index;
/* 591 */       for (int i = index + 1; i < limit; i++) {
/* 592 */         if (compareElements(i, minIndex) < 0) {
/* 593 */           minIndex = i;
/*     */         }
/*     */       } 
/* 596 */       return minIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     int findMinChild(int index) {
/* 601 */       return findMin(getLeftChildIndex(index), 2);
/*     */     }
/*     */ 
/*     */     
/*     */     int findMinGrandChild(int index) {
/* 606 */       int leftChildIndex = getLeftChildIndex(index);
/* 607 */       if (leftChildIndex < 0) {
/* 608 */         return -1;
/*     */       }
/* 610 */       return findMin(getLeftChildIndex(leftChildIndex), 4);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int crossOverUp(int index, E x) {
/* 618 */       if (index == 0) {
/* 619 */         MinMaxPriorityQueue.this.queue[0] = x;
/* 620 */         return 0;
/*     */       } 
/* 622 */       int parentIndex = getParentIndex(index);
/* 623 */       E parentElement = MinMaxPriorityQueue.this.elementData(parentIndex);
/* 624 */       if (parentIndex != 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 629 */         int grandparentIndex = getParentIndex(parentIndex);
/* 630 */         int uncleIndex = getRightChildIndex(grandparentIndex);
/* 631 */         if (uncleIndex != parentIndex && getLeftChildIndex(uncleIndex) >= MinMaxPriorityQueue.this.size) {
/* 632 */           E uncleElement = MinMaxPriorityQueue.this.elementData(uncleIndex);
/* 633 */           if (this.ordering.compare(uncleElement, parentElement) < 0) {
/* 634 */             parentIndex = uncleIndex;
/* 635 */             parentElement = uncleElement;
/*     */           } 
/*     */         } 
/*     */       } 
/* 639 */       if (this.ordering.compare(parentElement, x) < 0) {
/* 640 */         MinMaxPriorityQueue.this.queue[index] = parentElement;
/* 641 */         MinMaxPriorityQueue.this.queue[parentIndex] = x;
/* 642 */         return parentIndex;
/*     */       } 
/* 644 */       MinMaxPriorityQueue.this.queue[index] = x;
/* 645 */       return index;
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
/*     */     int swapWithConceptuallyLastElement(E actualLastElement) {
/* 658 */       int parentIndex = getParentIndex(MinMaxPriorityQueue.this.size);
/* 659 */       if (parentIndex != 0) {
/* 660 */         int grandparentIndex = getParentIndex(parentIndex);
/* 661 */         int uncleIndex = getRightChildIndex(grandparentIndex);
/* 662 */         if (uncleIndex != parentIndex && getLeftChildIndex(uncleIndex) >= MinMaxPriorityQueue.this.size) {
/* 663 */           E uncleElement = MinMaxPriorityQueue.this.elementData(uncleIndex);
/* 664 */           if (this.ordering.compare(uncleElement, actualLastElement) < 0) {
/* 665 */             MinMaxPriorityQueue.this.queue[uncleIndex] = actualLastElement;
/* 666 */             MinMaxPriorityQueue.this.queue[MinMaxPriorityQueue.this.size] = uncleElement;
/* 667 */             return uncleIndex;
/*     */           } 
/*     */         } 
/*     */       } 
/* 671 */       return MinMaxPriorityQueue.this.size;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int crossOver(int index, E x) {
/* 681 */       int minChildIndex = findMinChild(index);
/*     */ 
/*     */       
/* 684 */       if (minChildIndex > 0 && this.ordering.compare(MinMaxPriorityQueue.this.elementData(minChildIndex), x) < 0) {
/* 685 */         MinMaxPriorityQueue.this.queue[index] = MinMaxPriorityQueue.this.elementData(minChildIndex);
/* 686 */         MinMaxPriorityQueue.this.queue[minChildIndex] = x;
/* 687 */         return minChildIndex;
/*     */       } 
/* 689 */       return crossOverUp(index, x);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int fillHoleAt(int index) {
/*     */       int minGrandchildIndex;
/* 701 */       while ((minGrandchildIndex = findMinGrandChild(index)) > 0) {
/* 702 */         MinMaxPriorityQueue.this.queue[index] = MinMaxPriorityQueue.this.elementData(minGrandchildIndex);
/* 703 */         index = minGrandchildIndex;
/*     */       } 
/* 705 */       return index;
/*     */     }
/*     */     
/*     */     private boolean verifyIndex(int i) {
/* 709 */       if (getLeftChildIndex(i) < MinMaxPriorityQueue.this.size && compareElements(i, getLeftChildIndex(i)) > 0) {
/* 710 */         return false;
/*     */       }
/* 712 */       if (getRightChildIndex(i) < MinMaxPriorityQueue.this.size && compareElements(i, getRightChildIndex(i)) > 0) {
/* 713 */         return false;
/*     */       }
/* 715 */       if (i > 0 && compareElements(i, getParentIndex(i)) > 0) {
/* 716 */         return false;
/*     */       }
/* 718 */       if (i > 2 && compareElements(getGrandparentIndex(i), i) > 0) {
/* 719 */         return false;
/*     */       }
/* 721 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private int getLeftChildIndex(int i) {
/* 727 */       return i * 2 + 1;
/*     */     }
/*     */     
/*     */     private int getRightChildIndex(int i) {
/* 731 */       return i * 2 + 2;
/*     */     }
/*     */     
/*     */     private int getParentIndex(int i) {
/* 735 */       return (i - 1) / 2;
/*     */     }
/*     */     
/*     */     private int getGrandparentIndex(int i) {
/* 739 */       return getParentIndex(getParentIndex(i));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class QueueIterator
/*     */     implements Iterator<E>
/*     */   {
/* 749 */     private int cursor = -1;
/* 750 */     private int nextCursor = -1;
/* 751 */     private int expectedModCount = MinMaxPriorityQueue.this.modCount;
/*     */     
/*     */     private Queue<E> forgetMeNot;
/*     */     
/*     */     private List<E> skipMe;
/*     */     
/*     */     private E lastFromForgetMeNot;
/*     */     private boolean canRemove;
/*     */     
/*     */     public boolean hasNext() {
/* 761 */       checkModCount();
/* 762 */       nextNotInSkipMe(this.cursor + 1);
/* 763 */       return (this.nextCursor < MinMaxPriorityQueue.this.size() || (this.forgetMeNot != null && !this.forgetMeNot.isEmpty()));
/*     */     }
/*     */ 
/*     */     
/*     */     public E next() {
/* 768 */       checkModCount();
/* 769 */       nextNotInSkipMe(this.cursor + 1);
/* 770 */       if (this.nextCursor < MinMaxPriorityQueue.this.size()) {
/* 771 */         this.cursor = this.nextCursor;
/* 772 */         this.canRemove = true;
/* 773 */         return MinMaxPriorityQueue.this.elementData(this.cursor);
/* 774 */       }  if (this.forgetMeNot != null) {
/* 775 */         this.cursor = MinMaxPriorityQueue.this.size();
/* 776 */         this.lastFromForgetMeNot = this.forgetMeNot.poll();
/* 777 */         if (this.lastFromForgetMeNot != null) {
/* 778 */           this.canRemove = true;
/* 779 */           return this.lastFromForgetMeNot;
/*     */         } 
/*     */       } 
/* 782 */       throw new NoSuchElementException("iterator moved past last element in queue.");
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 787 */       CollectPreconditions.checkRemove(this.canRemove);
/* 788 */       checkModCount();
/* 789 */       this.canRemove = false;
/* 790 */       this.expectedModCount++;
/* 791 */       if (this.cursor < MinMaxPriorityQueue.this.size()) {
/* 792 */         MinMaxPriorityQueue.MoveDesc<E> moved = MinMaxPriorityQueue.this.removeAt(this.cursor);
/* 793 */         if (moved != null) {
/* 794 */           if (this.forgetMeNot == null) {
/* 795 */             this.forgetMeNot = new ArrayDeque<>();
/* 796 */             this.skipMe = new ArrayList<>(3);
/*     */           } 
/* 798 */           if (!foundAndRemovedExactReference(this.skipMe, moved.toTrickle)) {
/* 799 */             this.forgetMeNot.add(moved.toTrickle);
/*     */           }
/* 801 */           if (!foundAndRemovedExactReference(this.forgetMeNot, moved.replaced)) {
/* 802 */             this.skipMe.add(moved.replaced);
/*     */           }
/*     */         } 
/* 805 */         this.cursor--;
/* 806 */         this.nextCursor--;
/*     */       } else {
/* 808 */         Preconditions.checkState(removeExact(this.lastFromForgetMeNot));
/* 809 */         this.lastFromForgetMeNot = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean foundAndRemovedExactReference(Iterable<E> elements, E target) {
/* 815 */       for (Iterator<E> it = elements.iterator(); it.hasNext(); ) {
/* 816 */         E element = it.next();
/* 817 */         if (element == target) {
/* 818 */           it.remove();
/* 819 */           return true;
/*     */         } 
/*     */       } 
/* 822 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean removeExact(Object target) {
/* 827 */       for (int i = 0; i < MinMaxPriorityQueue.this.size; i++) {
/* 828 */         if (MinMaxPriorityQueue.this.queue[i] == target) {
/* 829 */           MinMaxPriorityQueue.this.removeAt(i);
/* 830 */           return true;
/*     */         } 
/*     */       } 
/* 833 */       return false;
/*     */     }
/*     */     
/*     */     private void checkModCount() {
/* 837 */       if (MinMaxPriorityQueue.this.modCount != this.expectedModCount) {
/* 838 */         throw new ConcurrentModificationException();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void nextNotInSkipMe(int c) {
/* 847 */       if (this.nextCursor < c) {
/* 848 */         if (this.skipMe != null) {
/* 849 */           while (c < MinMaxPriorityQueue.this.size() && foundAndRemovedExactReference(this.skipMe, MinMaxPriorityQueue.this.elementData(c))) {
/* 850 */             c++;
/*     */           }
/*     */         }
/* 853 */         this.nextCursor = c;
/*     */       } 
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
/*     */     private QueueIterator() {}
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
/*     */   public Iterator<E> iterator() {
/* 879 */     return new QueueIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 884 */     for (int i = 0; i < this.size; i++) {
/* 885 */       this.queue[i] = null;
/*     */     }
/* 887 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 892 */     Object[] copyTo = new Object[this.size];
/* 893 */     System.arraycopy(this.queue, 0, copyTo, 0, this.size);
/* 894 */     return copyTo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<? super E> comparator() {
/* 903 */     return this.minHeap.ordering;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   int capacity() {
/* 908 */     return this.queue.length;
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
/*     */   @VisibleForTesting
/*     */   static int initialQueueSize(int configuredExpectedSize, int maximumSize, Iterable<?> initialContents) {
/* 922 */     int result = (configuredExpectedSize == -1) ? 11 : configuredExpectedSize;
/*     */ 
/*     */     
/* 925 */     if (initialContents instanceof Collection) {
/* 926 */       int initialSize = ((Collection)initialContents).size();
/* 927 */       result = Math.max(result, initialSize);
/*     */     } 
/*     */ 
/*     */     
/* 931 */     return capAtMaximumSize(result, maximumSize);
/*     */   }
/*     */   
/*     */   private void growIfNeeded() {
/* 935 */     if (this.size > this.queue.length) {
/* 936 */       int newCapacity = calculateNewCapacity();
/* 937 */       Object[] newQueue = new Object[newCapacity];
/* 938 */       System.arraycopy(this.queue, 0, newQueue, 0, this.queue.length);
/* 939 */       this.queue = newQueue;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int calculateNewCapacity() {
/* 945 */     int oldCapacity = this.queue.length;
/*     */     
/* 947 */     int newCapacity = (oldCapacity < 64) ? ((oldCapacity + 1) * 2) : IntMath.checkedMultiply(oldCapacity / 2, 3);
/* 948 */     return capAtMaximumSize(newCapacity, this.maximumSize);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int capAtMaximumSize(int queueSize, int maximumSize) {
/* 953 */     return Math.min(queueSize - 1, maximumSize) + 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\MinMaxPriorityQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */