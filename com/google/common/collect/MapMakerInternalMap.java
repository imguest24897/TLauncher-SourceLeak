/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Equivalence;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.primitives.Ints;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*      */ import com.google.j2objc.annotations.Weak;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.CancellationException;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtIncompatible
/*      */ class MapMakerInternalMap<K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>, S extends MapMakerInternalMap.Segment<K, V, E, S>>
/*      */   extends AbstractMap<K, V>
/*      */   implements ConcurrentMap<K, V>, Serializable
/*      */ {
/*      */   static final int MAXIMUM_CAPACITY = 1073741824;
/*      */   static final int MAX_SEGMENTS = 65536;
/*      */   static final int CONTAINS_VALUE_RETRIES = 3;
/*      */   static final int DRAIN_THRESHOLD = 63;
/*      */   static final int DRAIN_MAX = 16;
/*      */   static final long CLEANUP_EXECUTOR_DELAY_SECS = 60L;
/*      */   final transient int segmentMask;
/*      */   final transient int segmentShift;
/*      */   final transient Segment<K, V, E, S>[] segments;
/*      */   final int concurrencyLevel;
/*      */   final Equivalence<Object> keyEquivalence;
/*      */   final transient InternalEntryHelper<K, V, E, S> entryHelper;
/*      */   
/*      */   private MapMakerInternalMap(MapMaker builder, InternalEntryHelper<K, V, E, S> entryHelper) {
/*  161 */     this.concurrencyLevel = Math.min(builder.getConcurrencyLevel(), 65536);
/*      */     
/*  163 */     this.keyEquivalence = builder.getKeyEquivalence();
/*  164 */     this.entryHelper = entryHelper;
/*      */     
/*  166 */     int initialCapacity = Math.min(builder.getInitialCapacity(), 1073741824);
/*      */ 
/*      */ 
/*      */     
/*  170 */     int segmentShift = 0;
/*  171 */     int segmentCount = 1;
/*  172 */     while (segmentCount < this.concurrencyLevel) {
/*  173 */       segmentShift++;
/*  174 */       segmentCount <<= 1;
/*      */     } 
/*  176 */     this.segmentShift = 32 - segmentShift;
/*  177 */     this.segmentMask = segmentCount - 1;
/*      */     
/*  179 */     this.segments = newSegmentArray(segmentCount);
/*      */     
/*  181 */     int segmentCapacity = initialCapacity / segmentCount;
/*  182 */     if (segmentCapacity * segmentCount < initialCapacity) {
/*  183 */       segmentCapacity++;
/*      */     }
/*      */     
/*  186 */     int segmentSize = 1;
/*  187 */     while (segmentSize < segmentCapacity) {
/*  188 */       segmentSize <<= 1;
/*      */     }
/*      */     
/*  191 */     for (int i = 0; i < this.segments.length; i++) {
/*  192 */       this.segments[i] = createSegment(segmentSize, -1);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> MapMakerInternalMap<K, V, ? extends InternalEntry<K, V, ?>, ?> create(MapMaker builder) {
/*  199 */     if (builder.getKeyStrength() == Strength.STRONG && builder
/*  200 */       .getValueStrength() == Strength.STRONG) {
/*  201 */       return new MapMakerInternalMap<>(builder, (InternalEntryHelper)StrongKeyStrongValueEntry.Helper.instance());
/*      */     }
/*  203 */     if (builder.getKeyStrength() == Strength.STRONG && builder
/*  204 */       .getValueStrength() == Strength.WEAK) {
/*  205 */       return new MapMakerInternalMap<>(builder, (InternalEntryHelper)StrongKeyWeakValueEntry.Helper.instance());
/*      */     }
/*  207 */     if (builder.getKeyStrength() == Strength.WEAK && builder
/*  208 */       .getValueStrength() == Strength.STRONG) {
/*  209 */       return new MapMakerInternalMap<>(builder, (InternalEntryHelper)WeakKeyStrongValueEntry.Helper.instance());
/*      */     }
/*  211 */     if (builder.getKeyStrength() == Strength.WEAK && builder.getValueStrength() == Strength.WEAK) {
/*  212 */       return new MapMakerInternalMap<>(builder, (InternalEntryHelper)WeakKeyWeakValueEntry.Helper.instance());
/*      */     }
/*  214 */     throw new AssertionError();
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
/*      */   static <K> MapMakerInternalMap<K, MapMaker.Dummy, ? extends InternalEntry<K, MapMaker.Dummy, ?>, ?> createWithDummyValues(MapMaker builder) {
/*  230 */     if (builder.getKeyStrength() == Strength.STRONG && builder
/*  231 */       .getValueStrength() == Strength.STRONG) {
/*  232 */       return new MapMakerInternalMap<>(builder, (InternalEntryHelper)StrongKeyDummyValueEntry.Helper.instance());
/*      */     }
/*  234 */     if (builder.getKeyStrength() == Strength.WEAK && builder
/*  235 */       .getValueStrength() == Strength.STRONG) {
/*  236 */       return new MapMakerInternalMap<>(builder, (InternalEntryHelper)WeakKeyDummyValueEntry.Helper.instance());
/*      */     }
/*  238 */     if (builder.getValueStrength() == Strength.WEAK) {
/*  239 */       throw new IllegalArgumentException("Map cannot have both weak and dummy values");
/*      */     }
/*  241 */     throw new AssertionError();
/*      */   }
/*      */   
/*      */   enum Strength {
/*  245 */     STRONG
/*      */     {
/*      */       Equivalence<Object> defaultEquivalence() {
/*  248 */         return Equivalence.equals();
/*      */       }
/*      */     },
/*      */     
/*  252 */     WEAK
/*      */     {
/*      */       Equivalence<Object> defaultEquivalence() {
/*  255 */         return Equivalence.identity();
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract Equivalence<Object> defaultEquivalence();
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
/*      */   static abstract class AbstractStrongKeyEntry<K, V, E extends InternalEntry<K, V, E>>
/*      */     implements InternalEntry<K, V, E>
/*      */   {
/*      */     final K key;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final int hash;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final E next;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     AbstractStrongKeyEntry(K key, int hash, E next) {
/*  345 */       this.key = key;
/*  346 */       this.hash = hash;
/*  347 */       this.next = next;
/*      */     }
/*      */ 
/*      */     
/*      */     public K getKey() {
/*  352 */       return this.key;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/*  357 */       return this.hash;
/*      */     }
/*      */ 
/*      */     
/*      */     public E getNext() {
/*  362 */       return this.next;
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
/*      */   static <K, V, E extends InternalEntry<K, V, E>> WeakValueReference<K, V, E> unsetWeakValueReference() {
/*  385 */     return (WeakValueReference)UNSET_WEAK_VALUE_REFERENCE;
/*      */   }
/*      */   
/*      */   static final class StrongKeyStrongValueEntry<K, V>
/*      */     extends AbstractStrongKeyEntry<K, V, StrongKeyStrongValueEntry<K, V>>
/*      */     implements StrongValueEntry<K, V, StrongKeyStrongValueEntry<K, V>>
/*      */   {
/*  392 */     private volatile V value = null;
/*      */     
/*      */     StrongKeyStrongValueEntry(K key, int hash, StrongKeyStrongValueEntry<K, V> next) {
/*  395 */       super(key, hash, next);
/*      */     }
/*      */ 
/*      */     
/*      */     public V getValue() {
/*  400 */       return this.value;
/*      */     }
/*      */     
/*      */     void setValue(V value) {
/*  404 */       this.value = value;
/*      */     }
/*      */     
/*      */     StrongKeyStrongValueEntry<K, V> copy(StrongKeyStrongValueEntry<K, V> newNext) {
/*  408 */       StrongKeyStrongValueEntry<K, V> newEntry = new StrongKeyStrongValueEntry(this.key, this.hash, newNext);
/*      */       
/*  410 */       newEntry.value = this.value;
/*  411 */       return newEntry;
/*      */     }
/*      */ 
/*      */     
/*      */     static final class Helper<K, V>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, V, StrongKeyStrongValueEntry<K, V>, MapMakerInternalMap.StrongKeyStrongValueSegment<K, V>>
/*      */     {
/*  418 */       private static final Helper<?, ?> INSTANCE = new Helper();
/*      */ 
/*      */       
/*      */       static <K, V> Helper<K, V> instance() {
/*  422 */         return (Helper)INSTANCE;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength() {
/*  427 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength() {
/*  432 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V>, MapMakerInternalMap.StrongKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/*  442 */         return new MapMakerInternalMap.StrongKeyStrongValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> copy(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> entry, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> newNext) {
/*  450 */         return entry.copy(newNext);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public void setValue(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> entry, V value) {
/*  458 */         entry.setValue(value);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> newEntry(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, K key, int hash, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> next)
/*      */       {
/*  467 */         return new MapMakerInternalMap.StrongKeyStrongValueEntry<>(key, hash, next); } } } static final class Helper<K, V> implements InternalEntryHelper<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> { private static final Helper<?, ?> INSTANCE = new Helper(); public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> newEntry(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, K key, int hash, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> next) { return new MapMakerInternalMap.StrongKeyStrongValueEntry<>(key, hash, next); } static <K, V> Helper<K, V> instance() { return (Helper)INSTANCE; } public MapMakerInternalMap.Strength keyStrength() { return MapMakerInternalMap.Strength.STRONG; } public MapMakerInternalMap.Strength valueStrength() {
/*      */       return MapMakerInternalMap.Strength.STRONG;
/*      */     } public MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V>, MapMakerInternalMap.StrongKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/*      */       return new MapMakerInternalMap.StrongKeyStrongValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */     } public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> copy(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> entry, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> newNext) {
/*      */       return entry.copy(newNext);
/*      */     } public void setValue(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> entry, V value) {
/*      */       entry.setValue(value);
/*      */     } }
/*      */    static final class StrongKeyWeakValueEntry<K, V> extends AbstractStrongKeyEntry<K, V, StrongKeyWeakValueEntry<K, V>> implements WeakValueEntry<K, V, StrongKeyWeakValueEntry<K, V>> {
/*  477 */     private volatile MapMakerInternalMap.WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> valueReference = MapMakerInternalMap.unsetWeakValueReference();
/*      */     
/*      */     StrongKeyWeakValueEntry(K key, int hash, StrongKeyWeakValueEntry<K, V> next) {
/*  480 */       super(key, hash, next);
/*      */     }
/*      */ 
/*      */     
/*      */     public V getValue() {
/*  485 */       return this.valueReference.get();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clearValue() {
/*  490 */       this.valueReference.clear();
/*      */     }
/*      */     
/*      */     void setValue(V value, ReferenceQueue<V> queueForValues) {
/*  494 */       MapMakerInternalMap.WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> previous = this.valueReference;
/*  495 */       this.valueReference = new MapMakerInternalMap.WeakValueReferenceImpl<>(queueForValues, value, this);
/*  496 */       previous.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     StrongKeyWeakValueEntry<K, V> copy(ReferenceQueue<V> queueForValues, StrongKeyWeakValueEntry<K, V> newNext) {
/*  501 */       StrongKeyWeakValueEntry<K, V> newEntry = new StrongKeyWeakValueEntry(this.key, this.hash, newNext);
/*  502 */       newEntry.valueReference = this.valueReference.copyFor(queueForValues, newEntry);
/*  503 */       return newEntry;
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> getValueReference() {
/*  508 */       return this.valueReference;
/*      */     }
/*      */ 
/*      */     
/*      */     static final class Helper<K, V>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, V, StrongKeyWeakValueEntry<K, V>, MapMakerInternalMap.StrongKeyWeakValueSegment<K, V>>
/*      */     {
/*  515 */       private static final Helper<?, ?> INSTANCE = new Helper();
/*      */ 
/*      */       
/*      */       static <K, V> Helper<K, V> instance() {
/*  519 */         return (Helper)INSTANCE;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength() {
/*  524 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength() {
/*  529 */         return MapMakerInternalMap.Strength.WEAK;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>, MapMakerInternalMap.StrongKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/*  538 */         return new MapMakerInternalMap.StrongKeyWeakValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> copy(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> newNext) {
/*  546 */         if (MapMakerInternalMap.Segment.isCollected(entry)) {
/*  547 */           return null;
/*      */         }
/*  549 */         return entry.copy(segment.queueForValues, newNext);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public void setValue(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry, V value) {
/*  555 */         entry.setValue(value, segment.queueForValues);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> newEntry(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, K key, int hash, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> next)
/*      */       {
/*  564 */         return new MapMakerInternalMap.StrongKeyWeakValueEntry<>(key, hash, next); } } } static final class Helper<K, V> implements InternalEntryHelper<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> { private static final Helper<?, ?> INSTANCE = new Helper(); static <K, V> Helper<K, V> instance() { return (Helper)INSTANCE; } public MapMakerInternalMap.Strength keyStrength() { return MapMakerInternalMap.Strength.STRONG; } public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> newEntry(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, K key, int hash, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> next) { return new MapMakerInternalMap.StrongKeyWeakValueEntry<>(key, hash, next); } public MapMakerInternalMap.Strength valueStrength() { return MapMakerInternalMap.Strength.WEAK; } public MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>, MapMakerInternalMap.StrongKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/*      */       return new MapMakerInternalMap.StrongKeyWeakValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */     } public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> copy(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> newNext) {
/*      */       if (MapMakerInternalMap.Segment.isCollected(entry))
/*      */         return null; 
/*      */       return entry.copy(segment.queueForValues, newNext);
/*      */     } public void setValue(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry, V value) {
/*      */       entry.setValue(value, segment.queueForValues);
/*      */     } }
/*      */   static final class StrongKeyDummyValueEntry<K> extends AbstractStrongKeyEntry<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>> implements StrongValueEntry<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>> { StrongKeyDummyValueEntry(K key, int hash, StrongKeyDummyValueEntry<K> next) {
/*  574 */       super(key, hash, next);
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMaker.Dummy getValue() {
/*  579 */       return MapMaker.Dummy.VALUE;
/*      */     }
/*      */     
/*      */     void setValue(MapMaker.Dummy value) {}
/*      */     
/*      */     StrongKeyDummyValueEntry<K> copy(StrongKeyDummyValueEntry<K> newNext) {
/*  585 */       return new StrongKeyDummyValueEntry(this.key, this.hash, newNext);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static final class Helper<K>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, MapMakerInternalMap.StrongKeyDummyValueSegment<K>>
/*      */     {
/*  595 */       private static final Helper<?> INSTANCE = new Helper();
/*      */ 
/*      */       
/*      */       static <K> Helper<K> instance() {
/*  599 */         return (Helper)INSTANCE;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength() {
/*  604 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength() {
/*  609 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyDummyValueSegment<K> newSegment(MapMakerInternalMap<K, MapMaker.Dummy, MapMakerInternalMap.StrongKeyDummyValueEntry<K>, MapMakerInternalMap.StrongKeyDummyValueSegment<K>> map, int initialCapacity, int maxSegmentSize) {
/*  618 */         return new MapMakerInternalMap.StrongKeyDummyValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyDummyValueEntry<K> copy(MapMakerInternalMap.StrongKeyDummyValueSegment<K> segment, MapMakerInternalMap.StrongKeyDummyValueEntry<K> entry, MapMakerInternalMap.StrongKeyDummyValueEntry<K> newNext) {
/*  626 */         return entry.copy(newNext);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public void setValue(MapMakerInternalMap.StrongKeyDummyValueSegment<K> segment, MapMakerInternalMap.StrongKeyDummyValueEntry<K> entry, MapMaker.Dummy value) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyDummyValueEntry<K> newEntry(MapMakerInternalMap.StrongKeyDummyValueSegment<K> segment, K key, int hash, MapMakerInternalMap.StrongKeyDummyValueEntry<K> next)
/*      */       {
/*  639 */         return new MapMakerInternalMap.StrongKeyDummyValueEntry<>(key, hash, next); } } } static final class Helper<K> implements InternalEntryHelper<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>> { private static final Helper<?> INSTANCE = new Helper(); static <K> Helper<K> instance() { return (Helper)INSTANCE; } public MapMakerInternalMap.Strength keyStrength() { return MapMakerInternalMap.Strength.STRONG; } public MapMakerInternalMap.StrongKeyDummyValueEntry<K> newEntry(MapMakerInternalMap.StrongKeyDummyValueSegment<K> segment, K key, int hash, MapMakerInternalMap.StrongKeyDummyValueEntry<K> next) { return new MapMakerInternalMap.StrongKeyDummyValueEntry<>(key, hash, next); }
/*      */      public MapMakerInternalMap.Strength valueStrength() {
/*      */       return MapMakerInternalMap.Strength.STRONG;
/*      */     } public MapMakerInternalMap.StrongKeyDummyValueSegment<K> newSegment(MapMakerInternalMap<K, MapMaker.Dummy, MapMakerInternalMap.StrongKeyDummyValueEntry<K>, MapMakerInternalMap.StrongKeyDummyValueSegment<K>> map, int initialCapacity, int maxSegmentSize) {
/*      */       return new MapMakerInternalMap.StrongKeyDummyValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */     }
/*      */     public MapMakerInternalMap.StrongKeyDummyValueEntry<K> copy(MapMakerInternalMap.StrongKeyDummyValueSegment<K> segment, MapMakerInternalMap.StrongKeyDummyValueEntry<K> entry, MapMakerInternalMap.StrongKeyDummyValueEntry<K> newNext) {
/*      */       return entry.copy(newNext);
/*      */     }
/*      */     public void setValue(MapMakerInternalMap.StrongKeyDummyValueSegment<K> segment, MapMakerInternalMap.StrongKeyDummyValueEntry<K> entry, MapMaker.Dummy value) {} }
/*      */   static abstract class AbstractWeakKeyEntry<K, V, E extends InternalEntry<K, V, E>> extends WeakReference<K> implements InternalEntry<K, V, E> { final int hash; final E next;
/*      */     AbstractWeakKeyEntry(ReferenceQueue<K> queue, K key, int hash, E next) {
/*  651 */       super(key, queue);
/*  652 */       this.hash = hash;
/*  653 */       this.next = next;
/*      */     }
/*      */ 
/*      */     
/*      */     public K getKey() {
/*  658 */       return get();
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/*  663 */       return this.hash;
/*      */     }
/*      */ 
/*      */     
/*      */     public E getNext() {
/*  668 */       return this.next;
/*      */     } }
/*      */ 
/*      */ 
/*      */   
/*      */   static final class WeakKeyDummyValueEntry<K>
/*      */     extends AbstractWeakKeyEntry<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>>
/*      */     implements StrongValueEntry<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>>
/*      */   {
/*      */     WeakKeyDummyValueEntry(ReferenceQueue<K> queue, K key, int hash, WeakKeyDummyValueEntry<K> next) {
/*  678 */       super(queue, key, hash, next);
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMaker.Dummy getValue() {
/*  683 */       return MapMaker.Dummy.VALUE;
/*      */     }
/*      */ 
/*      */     
/*      */     void setValue(MapMaker.Dummy value) {}
/*      */     
/*      */     WeakKeyDummyValueEntry<K> copy(ReferenceQueue<K> queueForKeys, WeakKeyDummyValueEntry<K> newNext) {
/*  690 */       return new WeakKeyDummyValueEntry(queueForKeys, getKey(), this.hash, newNext);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static final class Helper<K>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, MapMakerInternalMap.WeakKeyDummyValueSegment<K>>
/*      */     {
/*  700 */       private static final Helper<?> INSTANCE = new Helper();
/*      */ 
/*      */       
/*      */       static <K> Helper<K> instance() {
/*  704 */         return (Helper)INSTANCE;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength() {
/*  709 */         return MapMakerInternalMap.Strength.WEAK;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength() {
/*  714 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyDummyValueSegment<K> newSegment(MapMakerInternalMap<K, MapMaker.Dummy, MapMakerInternalMap.WeakKeyDummyValueEntry<K>, MapMakerInternalMap.WeakKeyDummyValueSegment<K>> map, int initialCapacity, int maxSegmentSize) {
/*  722 */         return new MapMakerInternalMap.WeakKeyDummyValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyDummyValueEntry<K> copy(MapMakerInternalMap.WeakKeyDummyValueSegment<K> segment, MapMakerInternalMap.WeakKeyDummyValueEntry<K> entry, MapMakerInternalMap.WeakKeyDummyValueEntry<K> newNext) {
/*  730 */         if (entry.getKey() == null)
/*      */         {
/*  732 */           return null;
/*      */         }
/*  734 */         return entry.copy(segment.queueForKeys, newNext);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public void setValue(MapMakerInternalMap.WeakKeyDummyValueSegment<K> segment, MapMakerInternalMap.WeakKeyDummyValueEntry<K> entry, MapMaker.Dummy value) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyDummyValueEntry<K> newEntry(MapMakerInternalMap.WeakKeyDummyValueSegment<K> segment, K key, int hash, MapMakerInternalMap.WeakKeyDummyValueEntry<K> next)
/*      */       {
/*  747 */         return new MapMakerInternalMap.WeakKeyDummyValueEntry<>(segment.queueForKeys, key, hash, next); } } } static final class Helper<K> implements InternalEntryHelper<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>> { private static final Helper<?> INSTANCE = new Helper(); static <K> Helper<K> instance() { return (Helper)INSTANCE; } public MapMakerInternalMap.Strength keyStrength() { return MapMakerInternalMap.Strength.WEAK; } public MapMakerInternalMap.WeakKeyDummyValueEntry<K> newEntry(MapMakerInternalMap.WeakKeyDummyValueSegment<K> segment, K key, int hash, MapMakerInternalMap.WeakKeyDummyValueEntry<K> next) { return new MapMakerInternalMap.WeakKeyDummyValueEntry<>(segment.queueForKeys, key, hash, next); }
/*      */      public MapMakerInternalMap.Strength valueStrength() {
/*      */       return MapMakerInternalMap.Strength.STRONG;
/*      */     } public MapMakerInternalMap.WeakKeyDummyValueSegment<K> newSegment(MapMakerInternalMap<K, MapMaker.Dummy, MapMakerInternalMap.WeakKeyDummyValueEntry<K>, MapMakerInternalMap.WeakKeyDummyValueSegment<K>> map, int initialCapacity, int maxSegmentSize) {
/*      */       return new MapMakerInternalMap.WeakKeyDummyValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */     } public MapMakerInternalMap.WeakKeyDummyValueEntry<K> copy(MapMakerInternalMap.WeakKeyDummyValueSegment<K> segment, MapMakerInternalMap.WeakKeyDummyValueEntry<K> entry, MapMakerInternalMap.WeakKeyDummyValueEntry<K> newNext) {
/*      */       if (entry.getKey() == null)
/*      */         return null; 
/*      */       return entry.copy(segment.queueForKeys, newNext);
/*  756 */     } public void setValue(MapMakerInternalMap.WeakKeyDummyValueSegment<K> segment, MapMakerInternalMap.WeakKeyDummyValueEntry<K> entry, MapMaker.Dummy value) {} } static final class WeakKeyStrongValueEntry<K, V> extends AbstractWeakKeyEntry<K, V, WeakKeyStrongValueEntry<K, V>> implements StrongValueEntry<K, V, WeakKeyStrongValueEntry<K, V>> { private volatile V value = null;
/*      */ 
/*      */     
/*      */     WeakKeyStrongValueEntry(ReferenceQueue<K> queue, K key, int hash, WeakKeyStrongValueEntry<K, V> next) {
/*  760 */       super(queue, key, hash, next);
/*      */     }
/*      */ 
/*      */     
/*      */     public V getValue() {
/*  765 */       return this.value;
/*      */     }
/*      */     
/*      */     void setValue(V value) {
/*  769 */       this.value = value;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     WeakKeyStrongValueEntry<K, V> copy(ReferenceQueue<K> queueForKeys, WeakKeyStrongValueEntry<K, V> newNext) {
/*  775 */       WeakKeyStrongValueEntry<K, V> newEntry = new WeakKeyStrongValueEntry(queueForKeys, getKey(), this.hash, newNext);
/*  776 */       newEntry.setValue(this.value);
/*  777 */       return newEntry;
/*      */     }
/*      */ 
/*      */     
/*      */     static final class Helper<K, V>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, V, WeakKeyStrongValueEntry<K, V>, MapMakerInternalMap.WeakKeyStrongValueSegment<K, V>>
/*      */     {
/*  784 */       private static final Helper<?, ?> INSTANCE = new Helper();
/*      */ 
/*      */       
/*      */       static <K, V> Helper<K, V> instance() {
/*  788 */         return (Helper)INSTANCE;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength() {
/*  793 */         return MapMakerInternalMap.Strength.WEAK;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength() {
/*  798 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V>, MapMakerInternalMap.WeakKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/*  807 */         return new MapMakerInternalMap.WeakKeyStrongValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> copy(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> entry, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> newNext) {
/*  815 */         if (entry.getKey() == null)
/*      */         {
/*  817 */           return null;
/*      */         }
/*  819 */         return entry.copy(segment.queueForKeys, newNext);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public void setValue(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> entry, V value) {
/*  825 */         entry.setValue(value);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> newEntry(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, K key, int hash, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> next)
/*      */       {
/*  834 */         return new MapMakerInternalMap.WeakKeyStrongValueEntry<>(segment.queueForKeys, key, hash, next); } } } static final class Helper<K, V> implements InternalEntryHelper<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> { private static final Helper<?, ?> INSTANCE = new Helper(); static <K, V> Helper<K, V> instance() { return (Helper)INSTANCE; } public MapMakerInternalMap.Strength keyStrength() { return MapMakerInternalMap.Strength.WEAK; } public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> newEntry(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, K key, int hash, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> next) { return new MapMakerInternalMap.WeakKeyStrongValueEntry<>(segment.queueForKeys, key, hash, next); } public MapMakerInternalMap.Strength valueStrength() {
/*      */       return MapMakerInternalMap.Strength.STRONG;
/*      */     } public MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V>, MapMakerInternalMap.WeakKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/*      */       return new MapMakerInternalMap.WeakKeyStrongValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */     } public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> copy(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> entry, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> newNext) {
/*      */       if (entry.getKey() == null)
/*      */         return null; 
/*      */       return entry.copy(segment.queueForKeys, newNext);
/*      */     } public void setValue(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> entry, V value) {
/*      */       entry.setValue(value);
/*  844 */     } } static final class WeakKeyWeakValueEntry<K, V> extends AbstractWeakKeyEntry<K, V, WeakKeyWeakValueEntry<K, V>> implements WeakValueEntry<K, V, WeakKeyWeakValueEntry<K, V>> { private volatile MapMakerInternalMap.WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> valueReference = MapMakerInternalMap.unsetWeakValueReference();
/*      */ 
/*      */     
/*      */     WeakKeyWeakValueEntry(ReferenceQueue<K> queue, K key, int hash, WeakKeyWeakValueEntry<K, V> next) {
/*  848 */       super(queue, key, hash, next);
/*      */     }
/*      */ 
/*      */     
/*      */     public V getValue() {
/*  853 */       return this.valueReference.get();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     WeakKeyWeakValueEntry<K, V> copy(ReferenceQueue<K> queueForKeys, ReferenceQueue<V> queueForValues, WeakKeyWeakValueEntry<K, V> newNext) {
/*  861 */       WeakKeyWeakValueEntry<K, V> newEntry = new WeakKeyWeakValueEntry(queueForKeys, getKey(), this.hash, newNext);
/*  862 */       newEntry.valueReference = this.valueReference.copyFor(queueForValues, newEntry);
/*  863 */       return newEntry;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clearValue() {
/*  868 */       this.valueReference.clear();
/*      */     }
/*      */     
/*      */     void setValue(V value, ReferenceQueue<V> queueForValues) {
/*  872 */       MapMakerInternalMap.WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> previous = this.valueReference;
/*  873 */       this.valueReference = new MapMakerInternalMap.WeakValueReferenceImpl<>(queueForValues, value, this);
/*  874 */       previous.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> getValueReference() {
/*  879 */       return this.valueReference;
/*      */     }
/*      */ 
/*      */     
/*      */     static final class Helper<K, V>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, V, WeakKeyWeakValueEntry<K, V>, MapMakerInternalMap.WeakKeyWeakValueSegment<K, V>>
/*      */     {
/*  886 */       private static final Helper<?, ?> INSTANCE = new Helper();
/*      */ 
/*      */       
/*      */       static <K, V> Helper<K, V> instance() {
/*  890 */         return (Helper)INSTANCE;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength() {
/*  895 */         return MapMakerInternalMap.Strength.WEAK;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength() {
/*  900 */         return MapMakerInternalMap.Strength.WEAK;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>, MapMakerInternalMap.WeakKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/*  908 */         return new MapMakerInternalMap.WeakKeyWeakValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> copy(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> newNext) {
/*  916 */         if (entry.getKey() == null)
/*      */         {
/*  918 */           return null;
/*      */         }
/*  920 */         if (MapMakerInternalMap.Segment.isCollected(entry)) {
/*  921 */           return null;
/*      */         }
/*  923 */         return entry.copy(segment.queueForKeys, segment.queueForValues, newNext);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public void setValue(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry, V value) {
/*  929 */         entry.setValue(value, segment.queueForValues);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> newEntry(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, K key, int hash, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> next)
/*      */       {
/*  938 */         return new MapMakerInternalMap.WeakKeyWeakValueEntry<>(segment.queueForKeys, key, hash, next); } } } static final class Helper<K, V> implements InternalEntryHelper<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> { private static final Helper<?, ?> INSTANCE = new Helper(); static <K, V> Helper<K, V> instance() { return (Helper)INSTANCE; } public MapMakerInternalMap.Strength keyStrength() { return MapMakerInternalMap.Strength.WEAK; } public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> newEntry(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, K key, int hash, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> next) { return new MapMakerInternalMap.WeakKeyWeakValueEntry<>(segment.queueForKeys, key, hash, next); }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.Strength valueStrength() {
/*      */       return MapMakerInternalMap.Strength.WEAK;
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>, MapMakerInternalMap.WeakKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/*      */       return new MapMakerInternalMap.WeakKeyWeakValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> copy(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> newNext) {
/*      */       if (entry.getKey() == null) {
/*      */         return null;
/*      */       }
/*      */       if (MapMakerInternalMap.Segment.isCollected(entry)) {
/*      */         return null;
/*      */       }
/*      */       return entry.copy(segment.queueForKeys, segment.queueForValues, newNext);
/*      */     }
/*      */ 
/*      */     
/*      */     public void setValue(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry, V value) {
/*      */       entry.setValue(value, segment.queueForValues);
/*      */     } }
/*      */ 
/*      */   
/*      */   static final class DummyInternalEntry
/*      */     implements InternalEntry<Object, Object, DummyInternalEntry>
/*      */   {
/*      */     private DummyInternalEntry() {
/*  972 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     
/*      */     public DummyInternalEntry getNext() {
/*  977 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/*  982 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getKey() {
/*  987 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getValue() {
/*  992 */       throw new AssertionError();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1000 */   static final WeakValueReference<Object, Object, DummyInternalEntry> UNSET_WEAK_VALUE_REFERENCE = new WeakValueReference<Object, Object, DummyInternalEntry>()
/*      */     {
/*      */       public MapMakerInternalMap.DummyInternalEntry getEntry()
/*      */       {
/* 1004 */         return null;
/*      */       }
/*      */ 
/*      */       
/*      */       public void clear() {}
/*      */ 
/*      */       
/*      */       public Object get() {
/* 1012 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakValueReference<Object, Object, MapMakerInternalMap.DummyInternalEntry> copyFor(ReferenceQueue<Object> queue, MapMakerInternalMap.DummyInternalEntry entry) {
/* 1018 */         return this;
/*      */       }
/*      */     };
/*      */   transient Set<K> keySet; transient Collection<V> values; transient Set<Map.Entry<K, V>> entrySet;
/*      */   private static final long serialVersionUID = 5L;
/*      */   
/*      */   static final class WeakValueReferenceImpl<K, V, E extends InternalEntry<K, V, E>> extends WeakReference<V> implements WeakValueReference<K, V, E> { @Weak
/*      */     final E entry;
/*      */     
/*      */     WeakValueReferenceImpl(ReferenceQueue<V> queue, V referent, E entry) {
/* 1028 */       super(referent, queue);
/* 1029 */       this.entry = entry;
/*      */     }
/*      */ 
/*      */     
/*      */     public E getEntry() {
/* 1034 */       return this.entry;
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, E> copyFor(ReferenceQueue<V> queue, E entry) {
/* 1039 */       return new WeakValueReferenceImpl(queue, get(), entry);
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
/*      */   static int rehash(int h) {
/* 1055 */     h += h << 15 ^ 0xFFFFCD7D;
/* 1056 */     h ^= h >>> 10;
/* 1057 */     h += h << 3;
/* 1058 */     h ^= h >>> 6;
/* 1059 */     h += (h << 2) + (h << 14);
/* 1060 */     return h ^ h >>> 16;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   E copyEntry(E original, E newNext) {
/* 1069 */     int hash = original.getHash();
/* 1070 */     return segmentFor(hash).copyEntry(original, newNext);
/*      */   }
/*      */   
/*      */   int hash(Object key) {
/* 1074 */     int h = this.keyEquivalence.hash(key);
/* 1075 */     return rehash(h);
/*      */   }
/*      */   
/*      */   void reclaimValue(WeakValueReference<K, V, E> valueReference) {
/* 1079 */     E entry = valueReference.getEntry();
/* 1080 */     int hash = entry.getHash();
/* 1081 */     segmentFor(hash).reclaimValue((K)entry.getKey(), hash, valueReference);
/*      */   }
/*      */   
/*      */   void reclaimKey(E entry) {
/* 1085 */     int hash = entry.getHash();
/* 1086 */     segmentFor(hash).reclaimKey(entry, hash);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   boolean isLiveForTesting(InternalEntry<K, V, ?> entry) {
/* 1095 */     return (segmentFor(entry.getHash()).getLiveValueForTesting(entry) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Segment<K, V, E, S> segmentFor(int hash) {
/* 1106 */     return this.segments[hash >>> this.segmentShift & this.segmentMask];
/*      */   }
/*      */   
/*      */   Segment<K, V, E, S> createSegment(int initialCapacity, int maxSegmentSize) {
/* 1110 */     return (Segment<K, V, E, S>)this.entryHelper.newSegment(this, initialCapacity, maxSegmentSize);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   V getLiveValue(E entry) {
/* 1118 */     if (entry.getKey() == null) {
/* 1119 */       return null;
/*      */     }
/* 1121 */     return (V)entry.getValue();
/*      */   }
/*      */ 
/*      */   
/*      */   final Segment<K, V, E, S>[] newSegmentArray(int ssize) {
/* 1126 */     return (Segment<K, V, E, S>[])new Segment[ssize];
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
/*      */   static abstract class Segment<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>>
/*      */     extends ReentrantLock
/*      */   {
/*      */     @Weak
/*      */     final MapMakerInternalMap<K, V, E, S> map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     volatile int count;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int modCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int threshold;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     volatile AtomicReferenceArray<E> table;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final int maxSegmentSize;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1200 */     final AtomicInteger readCount = new AtomicInteger();
/*      */     
/*      */     Segment(MapMakerInternalMap<K, V, E, S> map, int initialCapacity, int maxSegmentSize) {
/* 1203 */       this.map = map;
/* 1204 */       this.maxSegmentSize = maxSegmentSize;
/* 1205 */       initTable(newEntryArray(initialCapacity));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract S self();
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void maybeDrainReferenceQueues() {}
/*      */ 
/*      */ 
/*      */     
/*      */     void maybeClearReferenceQueues() {}
/*      */ 
/*      */ 
/*      */     
/*      */     void setValue(E entry, V value) {
/* 1225 */       this.map.entryHelper.setValue(self(), entry, value);
/*      */     }
/*      */ 
/*      */     
/*      */     E copyEntry(E original, E newNext) {
/* 1230 */       return this.map.entryHelper.copy(self(), original, newNext);
/*      */     }
/*      */     
/*      */     AtomicReferenceArray<E> newEntryArray(int size) {
/* 1234 */       return new AtomicReferenceArray<>(size);
/*      */     }
/*      */     
/*      */     void initTable(AtomicReferenceArray<E> newTable) {
/* 1238 */       this.threshold = newTable.length() * 3 / 4;
/* 1239 */       if (this.threshold == this.maxSegmentSize)
/*      */       {
/* 1241 */         this.threshold++;
/*      */       }
/* 1243 */       this.table = newTable;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract E castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> param1InternalEntry);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ReferenceQueue<K> getKeyReferenceQueueForTesting() {
/* 1259 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceQueue<V> getValueReferenceQueueForTesting() {
/* 1264 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     
/*      */     MapMakerInternalMap.WeakValueReference<K, V, E> getWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 1269 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     MapMakerInternalMap.WeakValueReference<K, V, E> newWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry, V value) {
/* 1278 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void setWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> valueReference) {
/* 1288 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void setTableEntryForTesting(int i, MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 1295 */       this.table.set(i, castForTesting(entry));
/*      */     }
/*      */ 
/*      */     
/*      */     E copyForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry, MapMakerInternalMap.InternalEntry<K, V, ?> newNext) {
/* 1300 */       return this.map.entryHelper.copy(self(), castForTesting(entry), castForTesting(newNext));
/*      */     }
/*      */ 
/*      */     
/*      */     void setValueForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry, V value) {
/* 1305 */       this.map.entryHelper.setValue(self(), castForTesting(entry), value);
/*      */     }
/*      */ 
/*      */     
/*      */     E newEntryForTesting(K key, int hash, MapMakerInternalMap.InternalEntry<K, V, ?> next) {
/* 1310 */       return this.map.entryHelper.newEntry(self(), key, hash, castForTesting(next));
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     boolean removeTableEntryForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 1316 */       return removeEntryForTesting(castForTesting(entry));
/*      */     }
/*      */ 
/*      */     
/*      */     E removeFromChainForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> first, MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 1321 */       return removeFromChain(castForTesting(first), castForTesting(entry));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     V getLiveValueForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 1329 */       return getLiveValue(castForTesting(entry));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void tryDrainReferenceQueues() {
/* 1336 */       if (tryLock()) {
/*      */         try {
/* 1338 */           maybeDrainReferenceQueues();
/*      */         } finally {
/* 1340 */           unlock();
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void drainKeyReferenceQueue(ReferenceQueue<K> keyReferenceQueue) {
/* 1348 */       int i = 0; Reference<? extends K> ref;
/* 1349 */       while ((ref = keyReferenceQueue.poll()) != null) {
/*      */         
/* 1351 */         MapMakerInternalMap.InternalEntry internalEntry = (MapMakerInternalMap.InternalEntry)ref;
/* 1352 */         this.map.reclaimKey((E)internalEntry);
/* 1353 */         if (++i == 16) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void drainValueReferenceQueue(ReferenceQueue<V> valueReferenceQueue) {
/* 1362 */       int i = 0; Reference<? extends V> ref;
/* 1363 */       while ((ref = valueReferenceQueue.poll()) != null) {
/*      */         
/* 1365 */         MapMakerInternalMap.WeakValueReference<K, V, E> valueReference = (MapMakerInternalMap.WeakValueReference)ref;
/* 1366 */         this.map.reclaimValue(valueReference);
/* 1367 */         if (++i == 16) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     <T> void clearReferenceQueue(ReferenceQueue<T> referenceQueue) {
/* 1374 */       while (referenceQueue.poll() != null);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     E getFirst(int hash) {
/* 1380 */       AtomicReferenceArray<E> table = this.table;
/* 1381 */       return table.get(hash & table.length() - 1);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     E getEntry(Object key, int hash) {
/* 1387 */       if (this.count != 0) {
/* 1388 */         for (E e = getFirst(hash); e != null; e = (E)e.getNext()) {
/* 1389 */           if (e.getHash() == hash) {
/*      */ 
/*      */ 
/*      */             
/* 1393 */             K entryKey = (K)e.getKey();
/* 1394 */             if (entryKey == null) {
/* 1395 */               tryDrainReferenceQueues();
/*      */ 
/*      */             
/*      */             }
/* 1399 */             else if (this.map.keyEquivalence.equivalent(key, entryKey)) {
/* 1400 */               return e;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/* 1405 */       return null;
/*      */     }
/*      */     
/*      */     E getLiveEntry(Object key, int hash) {
/* 1409 */       return getEntry(key, hash);
/*      */     }
/*      */     
/*      */     V get(Object key, int hash) {
/*      */       try {
/* 1414 */         E e = getLiveEntry(key, hash);
/* 1415 */         if (e == null) {
/* 1416 */           return null;
/*      */         }
/*      */         
/* 1419 */         V value = (V)e.getValue();
/* 1420 */         if (value == null) {
/* 1421 */           tryDrainReferenceQueues();
/*      */         }
/* 1423 */         return value;
/*      */       } finally {
/* 1425 */         postReadCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     boolean containsKey(Object key, int hash) {
/*      */       try {
/* 1431 */         if (this.count != 0) {
/* 1432 */           E e = getLiveEntry(key, hash);
/* 1433 */           return (e != null && e.getValue() != null);
/*      */         } 
/*      */         
/* 1436 */         return false;
/*      */       } finally {
/* 1438 */         postReadCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @VisibleForTesting
/*      */     boolean containsValue(Object value) {
/*      */       try {
/* 1449 */         if (this.count != 0) {
/* 1450 */           AtomicReferenceArray<E> table = this.table;
/* 1451 */           int length = table.length();
/* 1452 */           for (int i = 0; i < length; i++) {
/* 1453 */             for (MapMakerInternalMap.InternalEntry internalEntry = (MapMakerInternalMap.InternalEntry)table.get(i); internalEntry != null; internalEntry = (MapMakerInternalMap.InternalEntry)internalEntry.getNext()) {
/* 1454 */               V entryValue = getLiveValue((E)internalEntry);
/* 1455 */               if (entryValue != null)
/*      */               {
/*      */                 
/* 1458 */                 if (this.map.valueEquivalence().equivalent(value, entryValue)) {
/* 1459 */                   return true;
/*      */                 }
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/* 1465 */         return false;
/*      */       } finally {
/* 1467 */         postReadCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     V put(K key, int hash, V value, boolean onlyIfAbsent) {
/* 1472 */       lock();
/*      */       try {
/* 1474 */         preWriteCleanup();
/*      */         
/* 1476 */         int newCount = this.count + 1;
/* 1477 */         if (newCount > this.threshold) {
/* 1478 */           expand();
/* 1479 */           newCount = this.count + 1;
/*      */         } 
/*      */         
/* 1482 */         AtomicReferenceArray<E> table = this.table;
/* 1483 */         int index = hash & table.length() - 1;
/* 1484 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */ 
/*      */         
/* 1487 */         for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1488 */           K entryKey = (K)internalEntry2.getKey();
/* 1489 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1491 */             .equivalent(key, entryKey)) {
/*      */ 
/*      */             
/* 1494 */             V entryValue = (V)internalEntry2.getValue();
/*      */             
/* 1496 */             if (entryValue == null) {
/* 1497 */               this.modCount++;
/* 1498 */               setValue((E)internalEntry2, value);
/* 1499 */               newCount = this.count;
/* 1500 */               this.count = newCount;
/* 1501 */               return null;
/* 1502 */             }  if (onlyIfAbsent)
/*      */             {
/*      */ 
/*      */               
/* 1506 */               return entryValue;
/*      */             }
/*      */             
/* 1509 */             this.modCount++;
/* 1510 */             setValue((E)internalEntry2, value);
/* 1511 */             return entryValue;
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1517 */         this.modCount++;
/* 1518 */         E newEntry = this.map.entryHelper.newEntry(self(), key, hash, (E)internalEntry1);
/* 1519 */         setValue(newEntry, value);
/* 1520 */         table.set(index, newEntry);
/* 1521 */         this.count = newCount;
/* 1522 */         return null;
/*      */       } finally {
/* 1524 */         unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void expand() {
/* 1531 */       AtomicReferenceArray<E> oldTable = this.table;
/* 1532 */       int oldCapacity = oldTable.length();
/* 1533 */       if (oldCapacity >= 1073741824) {
/*      */         return;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1547 */       int newCount = this.count;
/* 1548 */       AtomicReferenceArray<E> newTable = newEntryArray(oldCapacity << 1);
/* 1549 */       this.threshold = newTable.length() * 3 / 4;
/* 1550 */       int newMask = newTable.length() - 1;
/* 1551 */       for (int oldIndex = 0; oldIndex < oldCapacity; oldIndex++) {
/*      */ 
/*      */         
/* 1554 */         MapMakerInternalMap.InternalEntry internalEntry = (MapMakerInternalMap.InternalEntry)oldTable.get(oldIndex);
/*      */         
/* 1556 */         if (internalEntry != null) {
/* 1557 */           E next = (E)internalEntry.getNext();
/* 1558 */           int headIndex = internalEntry.getHash() & newMask;
/*      */ 
/*      */           
/* 1561 */           if (next == null) {
/* 1562 */             newTable.set(headIndex, (E)internalEntry);
/*      */           } else {
/*      */             E e1;
/*      */ 
/*      */             
/* 1567 */             MapMakerInternalMap.InternalEntry internalEntry1 = internalEntry;
/* 1568 */             int tailIndex = headIndex;
/* 1569 */             for (E e = next; e != null; e = (E)e.getNext()) {
/* 1570 */               int newIndex = e.getHash() & newMask;
/* 1571 */               if (newIndex != tailIndex) {
/*      */                 
/* 1573 */                 tailIndex = newIndex;
/* 1574 */                 e1 = e;
/*      */               } 
/*      */             } 
/* 1577 */             newTable.set(tailIndex, e1);
/*      */ 
/*      */             
/* 1580 */             for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry; internalEntry2 != e1; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1581 */               int newIndex = internalEntry2.getHash() & newMask;
/* 1582 */               MapMakerInternalMap.InternalEntry internalEntry3 = (MapMakerInternalMap.InternalEntry)newTable.get(newIndex);
/* 1583 */               E newFirst = copyEntry((E)internalEntry2, (E)internalEntry3);
/* 1584 */               if (newFirst != null) {
/* 1585 */                 newTable.set(newIndex, newFirst);
/*      */               } else {
/* 1587 */                 newCount--;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 1593 */       this.table = newTable;
/* 1594 */       this.count = newCount;
/*      */     }
/*      */     
/*      */     boolean replace(K key, int hash, V oldValue, V newValue) {
/* 1598 */       lock();
/*      */       try {
/* 1600 */         preWriteCleanup();
/*      */         
/* 1602 */         AtomicReferenceArray<E> table = this.table;
/* 1603 */         int index = hash & table.length() - 1;
/* 1604 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1606 */         for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1607 */           K entryKey = (K)internalEntry2.getKey();
/* 1608 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1610 */             .equivalent(key, entryKey)) {
/*      */ 
/*      */             
/* 1613 */             V entryValue = (V)internalEntry2.getValue();
/* 1614 */             if (entryValue == null) {
/* 1615 */               if (isCollected(internalEntry2)) {
/* 1616 */                 int newCount = this.count - 1;
/* 1617 */                 this.modCount++;
/* 1618 */                 E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1619 */                 newCount = this.count - 1;
/* 1620 */                 table.set(index, newFirst);
/* 1621 */                 this.count = newCount;
/*      */               } 
/* 1623 */               return false;
/*      */             } 
/*      */             
/* 1626 */             if (this.map.valueEquivalence().equivalent(oldValue, entryValue)) {
/* 1627 */               this.modCount++;
/* 1628 */               setValue((E)internalEntry2, newValue);
/* 1629 */               return true;
/*      */             } 
/*      */ 
/*      */             
/* 1633 */             return false;
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 1638 */         return false;
/*      */       } finally {
/* 1640 */         unlock();
/*      */       } 
/*      */     }
/*      */     
/*      */     V replace(K key, int hash, V newValue) {
/* 1645 */       lock();
/*      */       try {
/* 1647 */         preWriteCleanup();
/*      */         
/* 1649 */         AtomicReferenceArray<E> table = this.table;
/* 1650 */         int index = hash & table.length() - 1;
/* 1651 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         MapMakerInternalMap.InternalEntry internalEntry2;
/* 1653 */         for (internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1654 */           K entryKey = (K)internalEntry2.getKey();
/* 1655 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1657 */             .equivalent(key, entryKey)) {
/*      */ 
/*      */             
/* 1660 */             V entryValue = (V)internalEntry2.getValue();
/* 1661 */             if (entryValue == null) {
/* 1662 */               if (isCollected(internalEntry2)) {
/* 1663 */                 int newCount = this.count - 1;
/* 1664 */                 this.modCount++;
/* 1665 */                 E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1666 */                 newCount = this.count - 1;
/* 1667 */                 table.set(index, newFirst);
/* 1668 */                 this.count = newCount;
/*      */               } 
/* 1670 */               return null;
/*      */             } 
/*      */             
/* 1673 */             this.modCount++;
/* 1674 */             setValue((E)internalEntry2, newValue);
/* 1675 */             return entryValue;
/*      */           } 
/*      */         } 
/*      */         
/* 1679 */         internalEntry2 = null; return (V)internalEntry2;
/*      */       } finally {
/* 1681 */         unlock();
/*      */       } 
/*      */     }
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     V remove(Object key, int hash) {
/* 1687 */       lock();
/*      */       try {
/* 1689 */         preWriteCleanup();
/*      */         
/* 1691 */         int newCount = this.count - 1;
/* 1692 */         AtomicReferenceArray<E> table = this.table;
/* 1693 */         int index = hash & table.length() - 1;
/* 1694 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         MapMakerInternalMap.InternalEntry internalEntry2;
/* 1696 */         for (internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1697 */           K entryKey = (K)internalEntry2.getKey();
/* 1698 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1700 */             .equivalent(key, entryKey)) {
/* 1701 */             V entryValue = (V)internalEntry2.getValue();
/*      */             
/* 1703 */             if (entryValue == null)
/*      */             {
/* 1705 */               if (!isCollected(internalEntry2))
/*      */               {
/*      */                 
/* 1708 */                 return null;
/*      */               }
/*      */             }
/* 1711 */             this.modCount++;
/* 1712 */             E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1713 */             newCount = this.count - 1;
/* 1714 */             table.set(index, newFirst);
/* 1715 */             this.count = newCount;
/* 1716 */             return entryValue;
/*      */           } 
/*      */         } 
/*      */         
/* 1720 */         internalEntry2 = null; return (V)internalEntry2;
/*      */       } finally {
/* 1722 */         unlock();
/*      */       } 
/*      */     }
/*      */     
/*      */     boolean remove(Object key, int hash, Object value) {
/* 1727 */       lock();
/*      */       try {
/* 1729 */         preWriteCleanup();
/*      */         
/* 1731 */         int newCount = this.count - 1;
/* 1732 */         AtomicReferenceArray<E> table = this.table;
/* 1733 */         int index = hash & table.length() - 1;
/* 1734 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1736 */         for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1737 */           K entryKey = (K)internalEntry2.getKey();
/* 1738 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1740 */             .equivalent(key, entryKey)) {
/* 1741 */             V entryValue = (V)internalEntry2.getValue();
/*      */             
/* 1743 */             boolean explicitRemoval = false;
/* 1744 */             if (this.map.valueEquivalence().equivalent(value, entryValue)) {
/* 1745 */               explicitRemoval = true;
/* 1746 */             } else if (!isCollected(internalEntry2)) {
/*      */ 
/*      */               
/* 1749 */               return false;
/*      */             } 
/*      */             
/* 1752 */             this.modCount++;
/* 1753 */             E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1754 */             newCount = this.count - 1;
/* 1755 */             table.set(index, newFirst);
/* 1756 */             this.count = newCount;
/* 1757 */             return explicitRemoval;
/*      */           } 
/*      */         } 
/*      */         
/* 1761 */         return false;
/*      */       } finally {
/* 1763 */         unlock();
/*      */       } 
/*      */     }
/*      */     
/*      */     void clear() {
/* 1768 */       if (this.count != 0) {
/* 1769 */         lock();
/*      */         try {
/* 1771 */           AtomicReferenceArray<E> table = this.table;
/* 1772 */           for (int i = 0; i < table.length(); i++) {
/* 1773 */             table.set(i, null);
/*      */           }
/* 1775 */           maybeClearReferenceQueues();
/* 1776 */           this.readCount.set(0);
/*      */           
/* 1778 */           this.modCount++;
/* 1779 */           this.count = 0;
/*      */         } finally {
/* 1781 */           unlock();
/*      */         } 
/*      */       } 
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
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     E removeFromChain(E first, E entry) {
/* 1800 */       int newCount = this.count;
/* 1801 */       E newFirst = (E)entry.getNext();
/* 1802 */       for (E e = first; e != entry; e = (E)e.getNext()) {
/* 1803 */         E next = copyEntry(e, newFirst);
/* 1804 */         if (next != null) {
/* 1805 */           newFirst = next;
/*      */         } else {
/* 1807 */           newCount--;
/*      */         } 
/*      */       } 
/* 1810 */       this.count = newCount;
/* 1811 */       return newFirst;
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     boolean reclaimKey(E entry, int hash) {
/* 1817 */       lock();
/*      */       try {
/* 1819 */         int newCount = this.count - 1;
/* 1820 */         AtomicReferenceArray<E> table = this.table;
/* 1821 */         int index = hash & table.length() - 1;
/* 1822 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1824 */         for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1825 */           if (internalEntry2 == entry) {
/* 1826 */             this.modCount++;
/* 1827 */             E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1828 */             newCount = this.count - 1;
/* 1829 */             table.set(index, newFirst);
/* 1830 */             this.count = newCount;
/* 1831 */             return true;
/*      */           } 
/*      */         } 
/*      */         
/* 1835 */         return false;
/*      */       } finally {
/* 1837 */         unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     boolean reclaimValue(K key, int hash, MapMakerInternalMap.WeakValueReference<K, V, E> valueReference) {
/* 1844 */       lock();
/*      */       try {
/* 1846 */         int newCount = this.count - 1;
/* 1847 */         AtomicReferenceArray<E> table = this.table;
/* 1848 */         int index = hash & table.length() - 1;
/* 1849 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1851 */         for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1852 */           K entryKey = (K)internalEntry2.getKey();
/* 1853 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1855 */             .equivalent(key, entryKey)) {
/* 1856 */             MapMakerInternalMap.WeakValueReference<K, V, E> v = ((MapMakerInternalMap.WeakValueEntry<K, V, E>)internalEntry2).getValueReference();
/* 1857 */             if (v == valueReference) {
/* 1858 */               this.modCount++;
/* 1859 */               E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1860 */               newCount = this.count - 1;
/* 1861 */               table.set(index, newFirst);
/* 1862 */               this.count = newCount;
/* 1863 */               return true;
/*      */             } 
/* 1865 */             return false;
/*      */           } 
/*      */         } 
/*      */         
/* 1869 */         return false;
/*      */       } finally {
/* 1871 */         unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     boolean clearValueForTesting(K key, int hash, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> valueReference) {
/* 1881 */       lock();
/*      */       try {
/* 1883 */         AtomicReferenceArray<E> table = this.table;
/* 1884 */         int index = hash & table.length() - 1;
/* 1885 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1887 */         for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1888 */           K entryKey = (K)internalEntry2.getKey();
/* 1889 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1891 */             .equivalent(key, entryKey)) {
/* 1892 */             MapMakerInternalMap.WeakValueReference<K, V, E> v = ((MapMakerInternalMap.WeakValueEntry<K, V, E>)internalEntry2).getValueReference();
/* 1893 */             if (v == valueReference) {
/* 1894 */               E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1895 */               table.set(index, newFirst);
/* 1896 */               return true;
/*      */             } 
/* 1898 */             return false;
/*      */           } 
/*      */         } 
/*      */         
/* 1902 */         return false;
/*      */       } finally {
/* 1904 */         unlock();
/*      */       } 
/*      */     }
/*      */     
/*      */     @GuardedBy("this")
/*      */     boolean removeEntryForTesting(E entry) {
/* 1910 */       int hash = entry.getHash();
/* 1911 */       int newCount = this.count - 1;
/* 1912 */       AtomicReferenceArray<E> table = this.table;
/* 1913 */       int index = hash & table.length() - 1;
/* 1914 */       MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */       
/* 1916 */       for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1917 */         if (internalEntry2 == entry) {
/* 1918 */           this.modCount++;
/* 1919 */           E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1920 */           newCount = this.count - 1;
/* 1921 */           table.set(index, newFirst);
/* 1922 */           this.count = newCount;
/* 1923 */           return true;
/*      */         } 
/*      */       } 
/*      */       
/* 1927 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static <K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>> boolean isCollected(E entry) {
/* 1935 */       return (entry.getValue() == null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     V getLiveValue(E entry) {
/* 1944 */       if (entry.getKey() == null) {
/* 1945 */         tryDrainReferenceQueues();
/* 1946 */         return null;
/*      */       } 
/* 1948 */       V value = (V)entry.getValue();
/* 1949 */       if (value == null) {
/* 1950 */         tryDrainReferenceQueues();
/* 1951 */         return null;
/*      */       } 
/*      */       
/* 1954 */       return value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void postReadCleanup() {
/* 1963 */       if ((this.readCount.incrementAndGet() & 0x3F) == 0) {
/* 1964 */         runCleanup();
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void preWriteCleanup() {
/* 1974 */       runLockedCleanup();
/*      */     }
/*      */     
/*      */     void runCleanup() {
/* 1978 */       runLockedCleanup();
/*      */     }
/*      */     
/*      */     void runLockedCleanup() {
/* 1982 */       if (tryLock()) {
/*      */         try {
/* 1984 */           maybeDrainReferenceQueues();
/* 1985 */           this.readCount.set(0);
/*      */         } finally {
/* 1987 */           unlock();
/*      */         } 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final class StrongKeyStrongValueSegment<K, V>
/*      */     extends Segment<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>>
/*      */   {
/*      */     StrongKeyStrongValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/* 2002 */       super(map, initialCapacity, maxSegmentSize);
/*      */     }
/*      */ 
/*      */     
/*      */     StrongKeyStrongValueSegment<K, V> self() {
/* 2007 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 2013 */       return (MapMakerInternalMap.StrongKeyStrongValueEntry)entry;
/*      */     }
/*      */   }
/*      */   
/*      */   static final class StrongKeyWeakValueSegment<K, V>
/*      */     extends Segment<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>>
/*      */   {
/* 2020 */     private final ReferenceQueue<V> queueForValues = new ReferenceQueue<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     StrongKeyWeakValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/* 2027 */       super(map, initialCapacity, maxSegmentSize);
/*      */     }
/*      */ 
/*      */     
/*      */     StrongKeyWeakValueSegment<K, V> self() {
/* 2032 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceQueue<V> getValueReferenceQueueForTesting() {
/* 2037 */       return this.queueForValues;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 2043 */       return (MapMakerInternalMap.StrongKeyWeakValueEntry)entry;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>> getWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e) {
/* 2049 */       return castForTesting(e).getValueReference();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>> newWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e, V value) {
/* 2055 */       return new MapMakerInternalMap.WeakValueReferenceImpl<>(this.queueForValues, value, castForTesting(e));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> valueReference) {
/* 2062 */       MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry = castForTesting(e);
/*      */       
/* 2064 */       MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> weakValueReference = valueReference;
/*      */       
/* 2066 */       MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>> previous = entry.valueReference;
/* 2067 */       entry.valueReference = weakValueReference;
/* 2068 */       previous.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeDrainReferenceQueues() {
/* 2073 */       drainValueReferenceQueue(this.queueForValues);
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeClearReferenceQueues() {
/* 2078 */       clearReferenceQueue(this.queueForValues);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final class StrongKeyDummyValueSegment<K>
/*      */     extends Segment<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>>
/*      */   {
/*      */     StrongKeyDummyValueSegment(MapMakerInternalMap<K, MapMaker.Dummy, MapMakerInternalMap.StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>> map, int initialCapacity, int maxSegmentSize) {
/* 2090 */       super(map, initialCapacity, maxSegmentSize);
/*      */     }
/*      */ 
/*      */     
/*      */     StrongKeyDummyValueSegment<K> self() {
/* 2095 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.StrongKeyDummyValueEntry<K> castForTesting(MapMakerInternalMap.InternalEntry<K, MapMaker.Dummy, ?> entry) {
/* 2101 */       return (MapMakerInternalMap.StrongKeyDummyValueEntry)entry;
/*      */     }
/*      */   }
/*      */   
/*      */   static final class WeakKeyStrongValueSegment<K, V>
/*      */     extends Segment<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>>
/*      */   {
/* 2108 */     private final ReferenceQueue<K> queueForKeys = new ReferenceQueue<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     WeakKeyStrongValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/* 2115 */       super(map, initialCapacity, maxSegmentSize);
/*      */     }
/*      */ 
/*      */     
/*      */     WeakKeyStrongValueSegment<K, V> self() {
/* 2120 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceQueue<K> getKeyReferenceQueueForTesting() {
/* 2125 */       return this.queueForKeys;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 2131 */       return (MapMakerInternalMap.WeakKeyStrongValueEntry)entry;
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeDrainReferenceQueues() {
/* 2136 */       drainKeyReferenceQueue(this.queueForKeys);
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeClearReferenceQueues() {
/* 2141 */       clearReferenceQueue(this.queueForKeys);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class WeakKeyWeakValueSegment<K, V>
/*      */     extends Segment<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>>
/*      */   {
/* 2148 */     private final ReferenceQueue<K> queueForKeys = new ReferenceQueue<>();
/* 2149 */     private final ReferenceQueue<V> queueForValues = new ReferenceQueue<>();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     WeakKeyWeakValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/* 2155 */       super(map, initialCapacity, maxSegmentSize);
/*      */     }
/*      */ 
/*      */     
/*      */     WeakKeyWeakValueSegment<K, V> self() {
/* 2160 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceQueue<K> getKeyReferenceQueueForTesting() {
/* 2165 */       return this.queueForKeys;
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceQueue<V> getValueReferenceQueueForTesting() {
/* 2170 */       return this.queueForValues;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 2176 */       return (MapMakerInternalMap.WeakKeyWeakValueEntry)entry;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>> getWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e) {
/* 2182 */       return castForTesting(e).getValueReference();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>> newWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e, V value) {
/* 2188 */       return new MapMakerInternalMap.WeakValueReferenceImpl<>(this.queueForValues, value, castForTesting(e));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> valueReference) {
/* 2195 */       MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry = castForTesting(e);
/*      */       
/* 2197 */       MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> weakValueReference = valueReference;
/*      */       
/* 2199 */       MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>> previous = entry.valueReference;
/* 2200 */       entry.valueReference = weakValueReference;
/* 2201 */       previous.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeDrainReferenceQueues() {
/* 2206 */       drainKeyReferenceQueue(this.queueForKeys);
/* 2207 */       drainValueReferenceQueue(this.queueForValues);
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeClearReferenceQueues() {
/* 2212 */       clearReferenceQueue(this.queueForKeys);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class WeakKeyDummyValueSegment<K>
/*      */     extends Segment<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>>
/*      */   {
/* 2219 */     private final ReferenceQueue<K> queueForKeys = new ReferenceQueue<>();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     WeakKeyDummyValueSegment(MapMakerInternalMap<K, MapMaker.Dummy, MapMakerInternalMap.WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>> map, int initialCapacity, int maxSegmentSize) {
/* 2225 */       super(map, initialCapacity, maxSegmentSize);
/*      */     }
/*      */ 
/*      */     
/*      */     WeakKeyDummyValueSegment<K> self() {
/* 2230 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceQueue<K> getKeyReferenceQueueForTesting() {
/* 2235 */       return this.queueForKeys;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakKeyDummyValueEntry<K> castForTesting(MapMakerInternalMap.InternalEntry<K, MapMaker.Dummy, ?> entry) {
/* 2241 */       return (MapMakerInternalMap.WeakKeyDummyValueEntry)entry;
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeDrainReferenceQueues() {
/* 2246 */       drainKeyReferenceQueue(this.queueForKeys);
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeClearReferenceQueues() {
/* 2251 */       clearReferenceQueue(this.queueForKeys);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class CleanupMapTask implements Runnable {
/*      */     final WeakReference<MapMakerInternalMap<?, ?, ?, ?>> mapReference;
/*      */     
/*      */     public CleanupMapTask(MapMakerInternalMap<?, ?, ?, ?> map) {
/* 2259 */       this.mapReference = new WeakReference<>(map);
/*      */     }
/*      */ 
/*      */     
/*      */     public void run() {
/* 2264 */       MapMakerInternalMap<?, ?, ?, ?> map = this.mapReference.get();
/* 2265 */       if (map == null) {
/* 2266 */         throw new CancellationException();
/*      */       }
/*      */       
/* 2269 */       for (MapMakerInternalMap.Segment<?, ?, ?, ?> segment : map.segments) {
/* 2270 */         segment.runCleanup();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   Strength keyStrength() {
/* 2277 */     return this.entryHelper.keyStrength();
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   Strength valueStrength() {
/* 2282 */     return this.entryHelper.valueStrength();
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   Equivalence<Object> valueEquivalence() {
/* 2287 */     return this.entryHelper.valueStrength().defaultEquivalence();
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
/*      */   public boolean isEmpty() {
/* 2301 */     long sum = 0L;
/* 2302 */     Segment<K, V, E, S>[] segments = this.segments; int i;
/* 2303 */     for (i = 0; i < segments.length; i++) {
/* 2304 */       if ((segments[i]).count != 0) {
/* 2305 */         return false;
/*      */       }
/* 2307 */       sum += (segments[i]).modCount;
/*      */     } 
/*      */     
/* 2310 */     if (sum != 0L) {
/* 2311 */       for (i = 0; i < segments.length; i++) {
/* 2312 */         if ((segments[i]).count != 0) {
/* 2313 */           return false;
/*      */         }
/* 2315 */         sum -= (segments[i]).modCount;
/*      */       } 
/* 2317 */       return (sum == 0L);
/*      */     } 
/* 2319 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public int size() {
/* 2324 */     Segment<K, V, E, S>[] segments = this.segments;
/* 2325 */     long sum = 0L;
/* 2326 */     for (int i = 0; i < segments.length; i++) {
/* 2327 */       sum += (segments[i]).count;
/*      */     }
/* 2329 */     return Ints.saturatedCast(sum);
/*      */   }
/*      */ 
/*      */   
/*      */   public V get(Object key) {
/* 2334 */     if (key == null) {
/* 2335 */       return null;
/*      */     }
/* 2337 */     int hash = hash(key);
/* 2338 */     return segmentFor(hash).get(key, hash);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   E getEntry(Object key) {
/* 2346 */     if (key == null) {
/* 2347 */       return null;
/*      */     }
/* 2349 */     int hash = hash(key);
/* 2350 */     return segmentFor(hash).getEntry(key, hash);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsKey(Object key) {
/* 2355 */     if (key == null) {
/* 2356 */       return false;
/*      */     }
/* 2358 */     int hash = hash(key);
/* 2359 */     return segmentFor(hash).containsKey(key, hash);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsValue(Object value) {
/* 2364 */     if (value == null) {
/* 2365 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2373 */     Segment<K, V, E, S>[] segments = this.segments;
/* 2374 */     long last = -1L;
/* 2375 */     for (int i = 0; i < 3; i++) {
/* 2376 */       long sum = 0L;
/* 2377 */       for (Segment<K, V, E, S> segment : segments) {
/*      */         
/* 2379 */         int unused = segment.count;
/*      */         
/* 2381 */         AtomicReferenceArray<E> table = segment.table;
/* 2382 */         for (int j = 0; j < table.length(); j++) {
/* 2383 */           for (InternalEntry internalEntry = (InternalEntry)table.get(j); internalEntry != null; internalEntry = (InternalEntry)internalEntry.getNext()) {
/* 2384 */             V v = segment.getLiveValue((E)internalEntry);
/* 2385 */             if (v != null && valueEquivalence().equivalent(value, v)) {
/* 2386 */               return true;
/*      */             }
/*      */           } 
/*      */         } 
/* 2390 */         sum += segment.modCount;
/*      */       } 
/* 2392 */       if (sum == last) {
/*      */         break;
/*      */       }
/* 2395 */       last = sum;
/*      */     } 
/* 2397 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public V put(K key, V value) {
/* 2403 */     Preconditions.checkNotNull(key);
/* 2404 */     Preconditions.checkNotNull(value);
/* 2405 */     int hash = hash(key);
/* 2406 */     return segmentFor(hash).put(key, hash, value, false);
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public V putIfAbsent(K key, V value) {
/* 2412 */     Preconditions.checkNotNull(key);
/* 2413 */     Preconditions.checkNotNull(value);
/* 2414 */     int hash = hash(key);
/* 2415 */     return segmentFor(hash).put(key, hash, value, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends V> m) {
/* 2420 */     for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
/* 2421 */       put(e.getKey(), e.getValue());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public V remove(Object key) {
/* 2428 */     if (key == null) {
/* 2429 */       return null;
/*      */     }
/* 2431 */     int hash = hash(key);
/* 2432 */     return segmentFor(hash).remove(key, hash);
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public boolean remove(Object key, Object value) {
/* 2438 */     if (key == null || value == null) {
/* 2439 */       return false;
/*      */     }
/* 2441 */     int hash = hash(key);
/* 2442 */     return segmentFor(hash).remove(key, hash, value);
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public boolean replace(K key, V oldValue, V newValue) {
/* 2448 */     Preconditions.checkNotNull(key);
/* 2449 */     Preconditions.checkNotNull(newValue);
/* 2450 */     if (oldValue == null) {
/* 2451 */       return false;
/*      */     }
/* 2453 */     int hash = hash(key);
/* 2454 */     return segmentFor(hash).replace(key, hash, oldValue, newValue);
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public V replace(K key, V value) {
/* 2460 */     Preconditions.checkNotNull(key);
/* 2461 */     Preconditions.checkNotNull(value);
/* 2462 */     int hash = hash(key);
/* 2463 */     return segmentFor(hash).replace(key, hash, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public void clear() {
/* 2468 */     for (Segment<K, V, E, S> segment : this.segments) {
/* 2469 */       segment.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<K> keySet() {
/* 2477 */     Set<K> ks = this.keySet;
/* 2478 */     return (ks != null) ? ks : (this.keySet = new KeySet());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> values() {
/* 2485 */     Collection<V> vs = this.values;
/* 2486 */     return (vs != null) ? vs : (this.values = new Values());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<Map.Entry<K, V>> entrySet() {
/* 2493 */     Set<Map.Entry<K, V>> es = this.entrySet;
/* 2494 */     return (es != null) ? es : (this.entrySet = new EntrySet());
/*      */   }
/*      */ 
/*      */   
/*      */   abstract class HashIterator<T>
/*      */     implements Iterator<T>
/*      */   {
/*      */     int nextSegmentIndex;
/*      */     int nextTableIndex;
/*      */     MapMakerInternalMap.Segment<K, V, E, S> currentSegment;
/*      */     AtomicReferenceArray<E> currentTable;
/*      */     E nextEntry;
/*      */     MapMakerInternalMap<K, V, E, S>.WriteThroughEntry nextExternal;
/*      */     MapMakerInternalMap<K, V, E, S>.WriteThroughEntry lastReturned;
/*      */     
/*      */     HashIterator() {
/* 2510 */       this.nextSegmentIndex = MapMakerInternalMap.this.segments.length - 1;
/* 2511 */       this.nextTableIndex = -1;
/* 2512 */       advance();
/*      */     }
/*      */ 
/*      */     
/*      */     public abstract T next();
/*      */     
/*      */     final void advance() {
/* 2519 */       this.nextExternal = null;
/*      */       
/* 2521 */       if (nextInChain()) {
/*      */         return;
/*      */       }
/*      */       
/* 2525 */       if (nextInTable()) {
/*      */         return;
/*      */       }
/*      */       
/* 2529 */       while (this.nextSegmentIndex >= 0) {
/* 2530 */         this.currentSegment = MapMakerInternalMap.this.segments[this.nextSegmentIndex--];
/* 2531 */         if (this.currentSegment.count != 0) {
/* 2532 */           this.currentTable = this.currentSegment.table;
/* 2533 */           this.nextTableIndex = this.currentTable.length() - 1;
/* 2534 */           if (nextInTable()) {
/*      */             return;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     boolean nextInChain() {
/* 2543 */       if (this.nextEntry != null) {
/* 2544 */         for (this.nextEntry = (E)this.nextEntry.getNext(); this.nextEntry != null; this.nextEntry = (E)this.nextEntry.getNext()) {
/* 2545 */           if (advanceTo(this.nextEntry)) {
/* 2546 */             return true;
/*      */           }
/*      */         } 
/*      */       }
/* 2550 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     boolean nextInTable() {
/* 2555 */       while (this.nextTableIndex >= 0) {
/* 2556 */         if ((this.nextEntry = this.currentTable.get(this.nextTableIndex--)) != null && (
/* 2557 */           advanceTo(this.nextEntry) || nextInChain())) {
/* 2558 */           return true;
/*      */         }
/*      */       } 
/*      */       
/* 2562 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean advanceTo(E entry) {
/*      */       try {
/* 2571 */         K key = (K)entry.getKey();
/* 2572 */         V value = (V)MapMakerInternalMap.this.getLiveValue(entry);
/* 2573 */         if (value != null) {
/* 2574 */           this.nextExternal = new MapMakerInternalMap.WriteThroughEntry(key, value);
/* 2575 */           return true;
/*      */         } 
/*      */         
/* 2578 */         return false;
/*      */       } finally {
/*      */         
/* 2581 */         this.currentSegment.postReadCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 2587 */       return (this.nextExternal != null);
/*      */     }
/*      */     
/*      */     MapMakerInternalMap<K, V, E, S>.WriteThroughEntry nextEntry() {
/* 2591 */       if (this.nextExternal == null) {
/* 2592 */         throw new NoSuchElementException();
/*      */       }
/* 2594 */       this.lastReturned = this.nextExternal;
/* 2595 */       advance();
/* 2596 */       return this.lastReturned;
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 2601 */       CollectPreconditions.checkRemove((this.lastReturned != null));
/* 2602 */       MapMakerInternalMap.this.remove(this.lastReturned.getKey());
/* 2603 */       this.lastReturned = null;
/*      */     } }
/*      */   
/*      */   final class KeyIterator extends HashIterator<K> { KeyIterator(MapMakerInternalMap this$0) {
/* 2607 */       super(this$0);
/*      */     }
/*      */     
/*      */     public K next() {
/* 2611 */       return nextEntry().getKey();
/*      */     } }
/*      */   
/*      */   final class ValueIterator extends HashIterator<V> { ValueIterator(MapMakerInternalMap this$0) {
/* 2615 */       super(this$0);
/*      */     }
/*      */     
/*      */     public V next() {
/* 2619 */       return nextEntry().getValue();
/*      */     } }
/*      */ 
/*      */ 
/*      */   
/*      */   final class WriteThroughEntry
/*      */     extends AbstractMapEntry<K, V>
/*      */   {
/*      */     final K key;
/*      */     
/*      */     V value;
/*      */     
/*      */     WriteThroughEntry(K key, V value) {
/* 2632 */       this.key = key;
/* 2633 */       this.value = value;
/*      */     }
/*      */ 
/*      */     
/*      */     public K getKey() {
/* 2638 */       return this.key;
/*      */     }
/*      */ 
/*      */     
/*      */     public V getValue() {
/* 2643 */       return this.value;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/* 2649 */       if (object instanceof Map.Entry) {
/* 2650 */         Map.Entry<?, ?> that = (Map.Entry<?, ?>)object;
/* 2651 */         return (this.key.equals(that.getKey()) && this.value.equals(that.getValue()));
/*      */       } 
/* 2653 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 2659 */       return this.key.hashCode() ^ this.value.hashCode();
/*      */     }
/*      */ 
/*      */     
/*      */     public V setValue(V newValue) {
/* 2664 */       V oldValue = (V)MapMakerInternalMap.this.put(this.key, newValue);
/* 2665 */       this.value = newValue;
/* 2666 */       return oldValue;
/*      */     } }
/*      */   
/*      */   final class EntryIterator extends HashIterator<Map.Entry<K, V>> { EntryIterator(MapMakerInternalMap this$0) {
/* 2670 */       super(this$0);
/*      */     }
/*      */     
/*      */     public Map.Entry<K, V> next() {
/* 2674 */       return nextEntry();
/*      */     } }
/*      */ 
/*      */ 
/*      */   
/*      */   final class KeySet
/*      */     extends SafeToArraySet<K>
/*      */   {
/*      */     public Iterator<K> iterator() {
/* 2683 */       return new MapMakerInternalMap.KeyIterator(MapMakerInternalMap.this);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 2688 */       return MapMakerInternalMap.this.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 2693 */       return MapMakerInternalMap.this.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 2698 */       return MapMakerInternalMap.this.containsKey(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 2703 */       return (MapMakerInternalMap.this.remove(o) != null);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 2708 */       MapMakerInternalMap.this.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   final class Values
/*      */     extends AbstractCollection<V>
/*      */   {
/*      */     public Iterator<V> iterator() {
/* 2717 */       return new MapMakerInternalMap.ValueIterator(MapMakerInternalMap.this);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 2722 */       return MapMakerInternalMap.this.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 2727 */       return MapMakerInternalMap.this.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 2732 */       return MapMakerInternalMap.this.containsValue(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 2737 */       MapMakerInternalMap.this.clear();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 2745 */       return MapMakerInternalMap.toArrayList(this).toArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] a) {
/* 2750 */       return (T[])MapMakerInternalMap.toArrayList(this).toArray((Object[])a);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   final class EntrySet
/*      */     extends SafeToArraySet<Map.Entry<K, V>>
/*      */   {
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/* 2759 */       return new MapMakerInternalMap.EntryIterator(MapMakerInternalMap.this);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 2764 */       if (!(o instanceof Map.Entry)) {
/* 2765 */         return false;
/*      */       }
/* 2767 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 2768 */       Object key = e.getKey();
/* 2769 */       if (key == null) {
/* 2770 */         return false;
/*      */       }
/* 2772 */       V v = (V)MapMakerInternalMap.this.get(key);
/*      */       
/* 2774 */       return (v != null && MapMakerInternalMap.this.valueEquivalence().equivalent(e.getValue(), v));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 2779 */       if (!(o instanceof Map.Entry)) {
/* 2780 */         return false;
/*      */       }
/* 2782 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 2783 */       Object key = e.getKey();
/* 2784 */       return (key != null && MapMakerInternalMap.this.remove(key, e.getValue()));
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 2789 */       return MapMakerInternalMap.this.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 2794 */       return MapMakerInternalMap.this.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 2799 */       MapMakerInternalMap.this.clear();
/*      */     }
/*      */   }
/*      */   
/*      */   private static abstract class SafeToArraySet<E>
/*      */     extends AbstractSet<E>
/*      */   {
/*      */     private SafeToArraySet() {}
/*      */     
/*      */     public Object[] toArray() {
/* 2809 */       return MapMakerInternalMap.toArrayList(this).toArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] a) {
/* 2814 */       return (T[])MapMakerInternalMap.toArrayList(this).toArray((Object[])a);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static <E> ArrayList<E> toArrayList(Collection<E> c) {
/* 2820 */     ArrayList<E> result = new ArrayList<>(c.size());
/* 2821 */     Iterators.addAll(result, c.iterator());
/* 2822 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Object writeReplace() {
/* 2830 */     return new SerializationProxy<>(this.entryHelper
/* 2831 */         .keyStrength(), this.entryHelper
/* 2832 */         .valueStrength(), this.keyEquivalence, this.entryHelper
/*      */         
/* 2834 */         .valueStrength().defaultEquivalence(), this.concurrencyLevel, this);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class AbstractSerializationProxy<K, V>
/*      */     extends ForwardingConcurrentMap<K, V>
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 3L;
/*      */ 
/*      */     
/*      */     final MapMakerInternalMap.Strength keyStrength;
/*      */ 
/*      */     
/*      */     final MapMakerInternalMap.Strength valueStrength;
/*      */ 
/*      */     
/*      */     final Equivalence<Object> keyEquivalence;
/*      */     
/*      */     final Equivalence<Object> valueEquivalence;
/*      */     
/*      */     final int concurrencyLevel;
/*      */     
/*      */     transient ConcurrentMap<K, V> delegate;
/*      */ 
/*      */     
/*      */     AbstractSerializationProxy(MapMakerInternalMap.Strength keyStrength, MapMakerInternalMap.Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, int concurrencyLevel, ConcurrentMap<K, V> delegate) {
/* 2862 */       this.keyStrength = keyStrength;
/* 2863 */       this.valueStrength = valueStrength;
/* 2864 */       this.keyEquivalence = keyEquivalence;
/* 2865 */       this.valueEquivalence = valueEquivalence;
/* 2866 */       this.concurrencyLevel = concurrencyLevel;
/* 2867 */       this.delegate = delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     protected ConcurrentMap<K, V> delegate() {
/* 2872 */       return this.delegate;
/*      */     }
/*      */     
/*      */     void writeMapTo(ObjectOutputStream out) throws IOException {
/* 2876 */       out.writeInt(this.delegate.size());
/* 2877 */       for (Map.Entry<K, V> entry : this.delegate.entrySet()) {
/* 2878 */         out.writeObject(entry.getKey());
/* 2879 */         out.writeObject(entry.getValue());
/*      */       } 
/* 2881 */       out.writeObject(null);
/*      */     }
/*      */ 
/*      */     
/*      */     MapMaker readMapMaker(ObjectInputStream in) throws IOException {
/* 2886 */       int size = in.readInt();
/* 2887 */       return (new MapMaker())
/* 2888 */         .initialCapacity(size)
/* 2889 */         .setKeyStrength(this.keyStrength)
/* 2890 */         .setValueStrength(this.valueStrength)
/* 2891 */         .keyEquivalence(this.keyEquivalence)
/* 2892 */         .concurrencyLevel(this.concurrencyLevel);
/*      */     }
/*      */ 
/*      */     
/*      */     void readEntries(ObjectInputStream in) throws IOException, ClassNotFoundException {
/*      */       while (true) {
/* 2898 */         K key = (K)in.readObject();
/* 2899 */         if (key == null) {
/*      */           break;
/*      */         }
/* 2902 */         V value = (V)in.readObject();
/* 2903 */         this.delegate.put(key, value);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class SerializationProxy<K, V>
/*      */     extends AbstractSerializationProxy<K, V>
/*      */   {
/*      */     private static final long serialVersionUID = 3L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     SerializationProxy(MapMakerInternalMap.Strength keyStrength, MapMakerInternalMap.Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, int concurrencyLevel, ConcurrentMap<K, V> delegate) {
/* 2922 */       super(keyStrength, valueStrength, keyEquivalence, valueEquivalence, concurrencyLevel, delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     private void writeObject(ObjectOutputStream out) throws IOException {
/* 2927 */       out.defaultWriteObject();
/* 2928 */       writeMapTo(out);
/*      */     }
/*      */     
/*      */     private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 2932 */       in.defaultReadObject();
/* 2933 */       MapMaker mapMaker = readMapMaker(in);
/* 2934 */       this.delegate = mapMaker.makeMap();
/* 2935 */       readEntries(in);
/*      */     }
/*      */     
/*      */     private Object readResolve() {
/* 2939 */       return this.delegate;
/*      */     }
/*      */   }
/*      */   
/*      */   static interface WeakValueReference<K, V, E extends InternalEntry<K, V, E>> {
/*      */     V get();
/*      */     
/*      */     E getEntry();
/*      */     
/*      */     void clear();
/*      */     
/*      */     WeakValueReference<K, V, E> copyFor(ReferenceQueue<V> param1ReferenceQueue, E param1E);
/*      */   }
/*      */   
/*      */   static interface WeakValueEntry<K, V, E extends InternalEntry<K, V, E>> extends InternalEntry<K, V, E> {
/*      */     MapMakerInternalMap.WeakValueReference<K, V, E> getValueReference();
/*      */     
/*      */     void clearValue();
/*      */   }
/*      */   
/*      */   static interface StrongValueEntry<K, V, E extends InternalEntry<K, V, E>> extends InternalEntry<K, V, E> {}
/*      */   
/*      */   static interface InternalEntry<K, V, E extends InternalEntry<K, V, E>> {
/*      */     E getNext();
/*      */     
/*      */     int getHash();
/*      */     
/*      */     K getKey();
/*      */     
/*      */     V getValue();
/*      */   }
/*      */   
/*      */   static interface InternalEntryHelper<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>> {
/*      */     MapMakerInternalMap.Strength keyStrength();
/*      */     
/*      */     MapMakerInternalMap.Strength valueStrength();
/*      */     
/*      */     S newSegment(MapMakerInternalMap<K, V, E, S> param1MapMakerInternalMap, int param1Int1, int param1Int2);
/*      */     
/*      */     E newEntry(S param1S, K param1K, int param1Int, E param1E);
/*      */     
/*      */     E copy(S param1S, E param1E1, E param1E2);
/*      */     
/*      */     void setValue(S param1S, E param1E, V param1V);
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\MapMakerInternalMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */