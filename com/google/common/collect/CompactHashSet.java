/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Consumer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ class CompactHashSet<E>
/*     */   extends AbstractSet<E>
/*     */   implements Serializable
/*     */ {
/*     */   @VisibleForTesting
/*     */   static final double HASH_FLOODING_FPP = 0.001D;
/*     */   private static final int MAX_HASH_BUCKET_LENGTH = 9;
/*     */   private transient Object table;
/*     */   private transient int[] entries;
/*     */   @VisibleForTesting
/*     */   transient Object[] elements;
/*     */   private transient int metadata;
/*     */   private transient int size;
/*     */   
/*     */   public static <E> CompactHashSet<E> create() {
/*  82 */     return new CompactHashSet<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> CompactHashSet<E> create(Collection<? extends E> collection) {
/*  93 */     CompactHashSet<E> set = createWithExpectedSize(collection.size());
/*  94 */     set.addAll(collection);
/*  95 */     return set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*     */   public static <E> CompactHashSet<E> create(E... elements) {
/* 107 */     CompactHashSet<E> set = createWithExpectedSize(elements.length);
/* 108 */     Collections.addAll(set, elements);
/* 109 */     return set;
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
/*     */   public static <E> CompactHashSet<E> createWithExpectedSize(int expectedSize) {
/* 122 */     return new CompactHashSet<>(expectedSize);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CompactHashSet() {
/* 196 */     init(3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CompactHashSet(int expectedSize) {
/* 205 */     init(expectedSize);
/*     */   }
/*     */ 
/*     */   
/*     */   void init(int expectedSize) {
/* 210 */     Preconditions.checkArgument((expectedSize >= 0), "Expected size must be >= 0");
/*     */ 
/*     */     
/* 213 */     this.metadata = Ints.constrainToRange(expectedSize, 1, 1073741823);
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   boolean needsAllocArrays() {
/* 219 */     return (this.table == null);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   int allocArrays() {
/* 225 */     Preconditions.checkState(needsAllocArrays(), "Arrays already allocated");
/*     */     
/* 227 */     int expectedSize = this.metadata;
/* 228 */     int buckets = CompactHashing.tableSize(expectedSize);
/* 229 */     this.table = CompactHashing.createTable(buckets);
/* 230 */     setHashTableMask(buckets - 1);
/*     */     
/* 232 */     this.entries = new int[expectedSize];
/* 233 */     this.elements = new Object[expectedSize];
/*     */     
/* 235 */     return expectedSize;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   Set<E> delegateOrNull() {
/* 242 */     if (this.table instanceof Set) {
/* 243 */       return (Set<E>)this.table;
/*     */     }
/* 245 */     return null;
/*     */   }
/*     */   
/*     */   private Set<E> createHashFloodingResistantDelegate(int tableSize) {
/* 249 */     return new LinkedHashSet<>(tableSize, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   @CanIgnoreReturnValue
/*     */   Set<E> convertToHashFloodingResistantImplementation() {
/* 256 */     Set<E> newDelegate = createHashFloodingResistantDelegate(hashTableMask() + 1);
/* 257 */     for (int i = firstEntryIndex(); i >= 0; i = getSuccessor(i)) {
/* 258 */       newDelegate.add((E)this.elements[i]);
/*     */     }
/* 260 */     this.table = newDelegate;
/* 261 */     this.entries = null;
/* 262 */     this.elements = null;
/* 263 */     incrementModCount();
/* 264 */     return newDelegate;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   boolean isUsingHashFloodingResistance() {
/* 269 */     return (delegateOrNull() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setHashTableMask(int mask) {
/* 274 */     int hashTableBits = 32 - Integer.numberOfLeadingZeros(mask);
/* 275 */     this
/* 276 */       .metadata = CompactHashing.maskCombine(this.metadata, hashTableBits, 31);
/*     */   }
/*     */ 
/*     */   
/*     */   private int hashTableMask() {
/* 281 */     return (1 << (this.metadata & 0x1F)) - 1;
/*     */   }
/*     */   
/*     */   void incrementModCount() {
/* 285 */     this.metadata += 32;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean add(E object) {
/* 291 */     if (needsAllocArrays()) {
/* 292 */       allocArrays();
/*     */     }
/* 294 */     Set<E> delegate = delegateOrNull();
/* 295 */     if (delegate != null) {
/* 296 */       return delegate.add(object);
/*     */     }
/* 298 */     int[] entries = this.entries;
/* 299 */     Object[] elements = this.elements;
/*     */     
/* 301 */     int newEntryIndex = this.size;
/* 302 */     int newSize = newEntryIndex + 1;
/* 303 */     int hash = Hashing.smearedHash(object);
/* 304 */     int mask = hashTableMask();
/* 305 */     int tableIndex = hash & mask;
/* 306 */     int next = CompactHashing.tableGet(this.table, tableIndex);
/* 307 */     if (next == 0) {
/* 308 */       if (newSize > mask) {
/*     */         
/* 310 */         mask = resizeTable(mask, CompactHashing.newCapacity(mask), hash, newEntryIndex);
/*     */       } else {
/* 312 */         CompactHashing.tableSet(this.table, tableIndex, newEntryIndex + 1);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 317 */       int entryIndex, entry, hashPrefix = CompactHashing.getHashPrefix(hash, mask);
/* 318 */       int bucketLength = 0;
/*     */       do {
/* 320 */         entryIndex = next - 1;
/* 321 */         entry = entries[entryIndex];
/* 322 */         if (CompactHashing.getHashPrefix(entry, mask) == hashPrefix && 
/* 323 */           Objects.equal(object, elements[entryIndex])) {
/* 324 */           return false;
/*     */         }
/* 326 */         next = CompactHashing.getNext(entry, mask);
/* 327 */         bucketLength++;
/* 328 */       } while (next != 0);
/*     */       
/* 330 */       if (bucketLength >= 9) {
/* 331 */         return convertToHashFloodingResistantImplementation().add(object);
/*     */       }
/*     */       
/* 334 */       if (newSize > mask) {
/*     */         
/* 336 */         mask = resizeTable(mask, CompactHashing.newCapacity(mask), hash, newEntryIndex);
/*     */       } else {
/* 338 */         entries[entryIndex] = CompactHashing.maskCombine(entry, newEntryIndex + 1, mask);
/*     */       } 
/*     */     } 
/* 341 */     resizeMeMaybe(newSize);
/* 342 */     insertEntry(newEntryIndex, object, hash, mask);
/* 343 */     this.size = newSize;
/* 344 */     incrementModCount();
/* 345 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void insertEntry(int entryIndex, E object, int hash, int mask) {
/* 352 */     this.entries[entryIndex] = CompactHashing.maskCombine(hash, 0, mask);
/* 353 */     this.elements[entryIndex] = object;
/*     */   }
/*     */ 
/*     */   
/*     */   private void resizeMeMaybe(int newSize) {
/* 358 */     int entriesSize = this.entries.length;
/* 359 */     if (newSize > entriesSize) {
/*     */ 
/*     */       
/* 362 */       int newCapacity = Math.min(1073741823, entriesSize + Math.max(1, entriesSize >>> 1) | 0x1);
/* 363 */       if (newCapacity != entriesSize) {
/* 364 */         resizeEntries(newCapacity);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void resizeEntries(int newCapacity) {
/* 374 */     this.entries = Arrays.copyOf(this.entries, newCapacity);
/* 375 */     this.elements = Arrays.copyOf(this.elements, newCapacity);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private int resizeTable(int mask, int newCapacity, int targetHash, int targetEntryIndex) {
/* 380 */     Object newTable = CompactHashing.createTable(newCapacity);
/* 381 */     int newMask = newCapacity - 1;
/*     */     
/* 383 */     if (targetEntryIndex != 0)
/*     */     {
/* 385 */       CompactHashing.tableSet(newTable, targetHash & newMask, targetEntryIndex + 1);
/*     */     }
/*     */     
/* 388 */     Object table = this.table;
/* 389 */     int[] entries = this.entries;
/*     */ 
/*     */     
/* 392 */     for (int tableIndex = 0; tableIndex <= mask; tableIndex++) {
/* 393 */       int next = CompactHashing.tableGet(table, tableIndex);
/* 394 */       while (next != 0) {
/* 395 */         int entryIndex = next - 1;
/* 396 */         int entry = entries[entryIndex];
/*     */ 
/*     */         
/* 399 */         int hash = CompactHashing.getHashPrefix(entry, mask) | tableIndex;
/*     */         
/* 401 */         int newTableIndex = hash & newMask;
/* 402 */         int newNext = CompactHashing.tableGet(newTable, newTableIndex);
/* 403 */         CompactHashing.tableSet(newTable, newTableIndex, next);
/* 404 */         entries[entryIndex] = CompactHashing.maskCombine(hash, newNext, newMask);
/*     */         
/* 406 */         next = CompactHashing.getNext(entry, mask);
/*     */       } 
/*     */     } 
/*     */     
/* 410 */     this.table = newTable;
/* 411 */     setHashTableMask(newMask);
/* 412 */     return newMask;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/* 417 */     if (needsAllocArrays()) {
/* 418 */       return false;
/*     */     }
/* 420 */     Set<E> delegate = delegateOrNull();
/* 421 */     if (delegate != null) {
/* 422 */       return delegate.contains(object);
/*     */     }
/* 424 */     int hash = Hashing.smearedHash(object);
/* 425 */     int mask = hashTableMask();
/* 426 */     int next = CompactHashing.tableGet(this.table, hash & mask);
/* 427 */     if (next == 0) {
/* 428 */       return false;
/*     */     }
/* 430 */     int hashPrefix = CompactHashing.getHashPrefix(hash, mask);
/*     */     while (true) {
/* 432 */       int entryIndex = next - 1;
/* 433 */       int entry = this.entries[entryIndex];
/* 434 */       if (CompactHashing.getHashPrefix(entry, mask) == hashPrefix && 
/* 435 */         Objects.equal(object, this.elements[entryIndex])) {
/* 436 */         return true;
/*     */       }
/* 438 */       next = CompactHashing.getNext(entry, mask);
/* 439 */       if (next == 0)
/* 440 */         return false; 
/*     */     } 
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean remove(Object object) {
/* 446 */     if (needsAllocArrays()) {
/* 447 */       return false;
/*     */     }
/* 449 */     Set<E> delegate = delegateOrNull();
/* 450 */     if (delegate != null) {
/* 451 */       return delegate.remove(object);
/*     */     }
/* 453 */     int mask = hashTableMask();
/*     */     
/* 455 */     int index = CompactHashing.remove(object, null, mask, this.table, this.entries, this.elements, null);
/*     */     
/* 457 */     if (index == -1) {
/* 458 */       return false;
/*     */     }
/*     */     
/* 461 */     moveLastEntry(index, mask);
/* 462 */     this.size--;
/* 463 */     incrementModCount();
/*     */     
/* 465 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void moveLastEntry(int dstIndex, int mask) {
/* 472 */     int srcIndex = size() - 1;
/* 473 */     if (dstIndex < srcIndex) {
/*     */       
/* 475 */       Object object = this.elements[srcIndex];
/* 476 */       this.elements[dstIndex] = object;
/* 477 */       this.elements[srcIndex] = null;
/*     */ 
/*     */       
/* 480 */       this.entries[dstIndex] = this.entries[srcIndex];
/* 481 */       this.entries[srcIndex] = 0;
/*     */ 
/*     */       
/* 484 */       int tableIndex = Hashing.smearedHash(object) & mask;
/* 485 */       int next = CompactHashing.tableGet(this.table, tableIndex);
/* 486 */       int srcNext = srcIndex + 1;
/* 487 */       if (next == srcNext) {
/*     */         
/* 489 */         CompactHashing.tableSet(this.table, tableIndex, dstIndex + 1);
/*     */       } else {
/*     */         int entryIndex, entry;
/*     */ 
/*     */         
/*     */         do {
/* 495 */           entryIndex = next - 1;
/* 496 */           entry = this.entries[entryIndex];
/* 497 */           next = CompactHashing.getNext(entry, mask);
/* 498 */         } while (next != srcNext);
/*     */         
/* 500 */         this.entries[entryIndex] = CompactHashing.maskCombine(entry, dstIndex + 1, mask);
/*     */       } 
/*     */     } else {
/* 503 */       this.elements[dstIndex] = null;
/* 504 */       this.entries[dstIndex] = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   int firstEntryIndex() {
/* 509 */     return isEmpty() ? -1 : 0;
/*     */   }
/*     */   
/*     */   int getSuccessor(int entryIndex) {
/* 513 */     return (entryIndex + 1 < this.size) ? (entryIndex + 1) : -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int adjustAfterRemove(int indexBeforeRemove, int indexRemoved) {
/* 522 */     return indexBeforeRemove - 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 527 */     Set<E> delegate = delegateOrNull();
/* 528 */     if (delegate != null) {
/* 529 */       return delegate.iterator();
/*     */     }
/* 531 */     return new Iterator<E>() {
/* 532 */         int expectedMetadata = CompactHashSet.this.metadata;
/* 533 */         int currentIndex = CompactHashSet.this.firstEntryIndex();
/* 534 */         int indexToRemove = -1;
/*     */ 
/*     */         
/*     */         public boolean hasNext() {
/* 538 */           return (this.currentIndex >= 0);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public E next() {
/* 544 */           checkForConcurrentModification();
/* 545 */           if (!hasNext()) {
/* 546 */             throw new NoSuchElementException();
/*     */           }
/* 548 */           this.indexToRemove = this.currentIndex;
/* 549 */           E result = (E)CompactHashSet.this.elements[this.currentIndex];
/* 550 */           this.currentIndex = CompactHashSet.this.getSuccessor(this.currentIndex);
/* 551 */           return result;
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 556 */           checkForConcurrentModification();
/* 557 */           CollectPreconditions.checkRemove((this.indexToRemove >= 0));
/* 558 */           incrementExpectedModCount();
/* 559 */           CompactHashSet.this.remove(CompactHashSet.this.elements[this.indexToRemove]);
/* 560 */           this.currentIndex = CompactHashSet.this.adjustAfterRemove(this.currentIndex, this.indexToRemove);
/* 561 */           this.indexToRemove = -1;
/*     */         }
/*     */         
/*     */         void incrementExpectedModCount() {
/* 565 */           this.expectedMetadata += 32;
/*     */         }
/*     */         
/*     */         private void checkForConcurrentModification() {
/* 569 */           if (CompactHashSet.this.metadata != this.expectedMetadata) {
/* 570 */             throw new ConcurrentModificationException();
/*     */           }
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<E> spliterator() {
/* 578 */     if (needsAllocArrays()) {
/* 579 */       return Spliterators.spliterator(new Object[0], 17);
/*     */     }
/* 581 */     Set<E> delegate = delegateOrNull();
/* 582 */     return (delegate != null) ? 
/* 583 */       delegate.spliterator() : 
/* 584 */       Spliterators.<E>spliterator(this.elements, 0, this.size, 17);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void forEach(Consumer<? super E> action) {
/* 590 */     Preconditions.checkNotNull(action);
/* 591 */     Set<E> delegate = delegateOrNull();
/* 592 */     if (delegate != null) {
/* 593 */       delegate.forEach(action);
/*     */     } else {
/* 595 */       for (int i = firstEntryIndex(); i >= 0; i = getSuccessor(i)) {
/* 596 */         action.accept((E)this.elements[i]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 603 */     Set<E> delegate = delegateOrNull();
/* 604 */     return (delegate != null) ? delegate.size() : this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 609 */     return (size() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 614 */     if (needsAllocArrays()) {
/* 615 */       return new Object[0];
/*     */     }
/* 617 */     Set<E> delegate = delegateOrNull();
/* 618 */     return (delegate != null) ? delegate.toArray() : Arrays.<Object>copyOf(this.elements, this.size);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public <T> T[] toArray(T[] a) {
/* 624 */     if (needsAllocArrays()) {
/* 625 */       if (a.length > 0) {
/* 626 */         a[0] = null;
/*     */       }
/* 628 */       return a;
/*     */     } 
/* 630 */     Set<E> delegate = delegateOrNull();
/* 631 */     return (delegate != null) ? 
/* 632 */       delegate.<T>toArray(a) : 
/* 633 */       ObjectArrays.<T>toArrayImpl(this.elements, 0, this.size, a);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trimToSize() {
/* 641 */     if (needsAllocArrays()) {
/*     */       return;
/*     */     }
/* 644 */     Set<E> delegate = delegateOrNull();
/* 645 */     if (delegate != null) {
/* 646 */       Set<E> newDelegate = createHashFloodingResistantDelegate(size());
/* 647 */       newDelegate.addAll(delegate);
/* 648 */       this.table = newDelegate;
/*     */       return;
/*     */     } 
/* 651 */     int size = this.size;
/* 652 */     if (size < this.entries.length) {
/* 653 */       resizeEntries(size);
/*     */     }
/* 655 */     int minimumTableSize = CompactHashing.tableSize(size);
/* 656 */     int mask = hashTableMask();
/* 657 */     if (minimumTableSize < mask) {
/* 658 */       resizeTable(mask, minimumTableSize, 0, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 664 */     if (needsAllocArrays()) {
/*     */       return;
/*     */     }
/* 667 */     incrementModCount();
/* 668 */     Set<E> delegate = delegateOrNull();
/* 669 */     if (delegate != null) {
/* 670 */       this
/* 671 */         .metadata = Ints.constrainToRange(size(), 3, 1073741823);
/* 672 */       delegate.clear();
/* 673 */       this.table = null;
/* 674 */       this.size = 0;
/*     */     } else {
/* 676 */       Arrays.fill(this.elements, 0, this.size, (Object)null);
/* 677 */       CompactHashing.tableClear(this.table);
/* 678 */       Arrays.fill(this.entries, 0, this.size, 0);
/* 679 */       this.size = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 684 */     stream.defaultWriteObject();
/* 685 */     stream.writeInt(size());
/* 686 */     for (E e : this) {
/* 687 */       stream.writeObject(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 693 */     stream.defaultReadObject();
/* 694 */     int elementCount = stream.readInt();
/* 695 */     if (elementCount < 0) {
/* 696 */       throw new InvalidObjectException((new StringBuilder(25)).append("Invalid size: ").append(elementCount).toString());
/*     */     }
/* 698 */     init(elementCount);
/* 699 */     for (int i = 0; i < elementCount; i++) {
/* 700 */       E element = (E)stream.readObject();
/* 701 */       add(element);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\CompactHashSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */