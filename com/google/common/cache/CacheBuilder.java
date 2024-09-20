/*      */ package com.google.common.cache;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Ascii;
/*      */ import com.google.common.base.Equivalence;
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Supplier;
/*      */ import com.google.common.base.Suppliers;
/*      */ import com.google.common.base.Ticker;
/*      */ import com.google.errorprone.annotations.CheckReturnValue;
/*      */ import java.time.Duration;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */ public final class CacheBuilder<K, V>
/*      */ {
/*      */   private static final int DEFAULT_INITIAL_CAPACITY = 16;
/*      */   private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
/*      */   private static final int DEFAULT_EXPIRATION_NANOS = 0;
/*      */   private static final int DEFAULT_REFRESH_NANOS = 0;
/*      */   
/*  165 */   static final Supplier<? extends AbstractCache.StatsCounter> NULL_STATS_COUNTER = Suppliers.ofInstance(new AbstractCache.StatsCounter()
/*      */       {
/*      */         public void recordHits(int count) {}
/*      */ 
/*      */ 
/*      */         
/*      */         public void recordMisses(int count) {}
/*      */ 
/*      */ 
/*      */         
/*      */         public void recordLoadSuccess(long loadTime) {}
/*      */ 
/*      */ 
/*      */         
/*      */         public void recordLoadException(long loadTime) {}
/*      */ 
/*      */         
/*      */         public void recordEviction() {}
/*      */ 
/*      */         
/*      */         public CacheStats snapshot() {
/*  186 */           return CacheBuilder.EMPTY_STATS;
/*      */         }
/*      */       });
/*  189 */   static final CacheStats EMPTY_STATS = new CacheStats(0L, 0L, 0L, 0L, 0L, 0L);
/*      */   
/*  191 */   static final Supplier<AbstractCache.StatsCounter> CACHE_STATS_COUNTER = new Supplier<AbstractCache.StatsCounter>()
/*      */     {
/*      */       public AbstractCache.StatsCounter get()
/*      */       {
/*  195 */         return new AbstractCache.SimpleStatsCounter();
/*      */       }
/*      */     };
/*      */   
/*      */   enum NullListener implements RemovalListener<Object, Object> {
/*  200 */     INSTANCE;
/*      */     
/*      */     public void onRemoval(RemovalNotification<Object, Object> notification) {}
/*      */   }
/*      */   
/*      */   enum OneWeigher
/*      */     implements Weigher<Object, Object> {
/*  207 */     INSTANCE;
/*      */ 
/*      */     
/*      */     public int weigh(Object key, Object value) {
/*  211 */       return 1;
/*      */     }
/*      */   }
/*      */   
/*  215 */   static final Ticker NULL_TICKER = new Ticker()
/*      */     {
/*      */       public long read()
/*      */       {
/*  219 */         return 0L;
/*      */       }
/*      */     };
/*      */   
/*  223 */   private static final Logger logger = Logger.getLogger(CacheBuilder.class.getName());
/*      */   
/*      */   static final int UNSET_INT = -1;
/*      */   
/*      */   boolean strictParsing = true;
/*      */   
/*  229 */   int initialCapacity = -1;
/*  230 */   int concurrencyLevel = -1;
/*  231 */   long maximumSize = -1L;
/*  232 */   long maximumWeight = -1L;
/*      */   
/*      */   Weigher<? super K, ? super V> weigher;
/*      */   
/*      */   LocalCache.Strength keyStrength;
/*      */   LocalCache.Strength valueStrength;
/*  238 */   long expireAfterWriteNanos = -1L;
/*      */ 
/*      */   
/*  241 */   long expireAfterAccessNanos = -1L;
/*      */ 
/*      */   
/*  244 */   long refreshNanos = -1L;
/*      */   
/*      */   Equivalence<Object> keyEquivalence;
/*      */   
/*      */   Equivalence<Object> valueEquivalence;
/*      */   
/*      */   RemovalListener<? super K, ? super V> removalListener;
/*      */   
/*      */   Ticker ticker;
/*  253 */   Supplier<? extends AbstractCache.StatsCounter> statsCounterSupplier = NULL_STATS_COUNTER;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CacheBuilder<Object, Object> newBuilder() {
/*  265 */     return new CacheBuilder<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static CacheBuilder<Object, Object> from(CacheBuilderSpec spec) {
/*  275 */     return spec.toCacheBuilder().lenientParsing();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static CacheBuilder<Object, Object> from(String spec) {
/*  287 */     return from(CacheBuilderSpec.parse(spec));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   CacheBuilder<K, V> lenientParsing() {
/*  297 */     this.strictParsing = false;
/*  298 */     return this;
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
/*      */   @GwtIncompatible
/*      */   CacheBuilder<K, V> keyEquivalence(Equivalence<Object> equivalence) {
/*  311 */     Preconditions.checkState((this.keyEquivalence == null), "key equivalence was already set to %s", this.keyEquivalence);
/*  312 */     this.keyEquivalence = (Equivalence<Object>)Preconditions.checkNotNull(equivalence);
/*  313 */     return this;
/*      */   }
/*      */   
/*      */   Equivalence<Object> getKeyEquivalence() {
/*  317 */     return (Equivalence<Object>)MoreObjects.firstNonNull(this.keyEquivalence, getKeyStrength().defaultEquivalence());
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
/*      */   CacheBuilder<K, V> valueEquivalence(Equivalence<Object> equivalence) {
/*  331 */     Preconditions.checkState((this.valueEquivalence == null), "value equivalence was already set to %s", this.valueEquivalence);
/*      */     
/*  333 */     this.valueEquivalence = (Equivalence<Object>)Preconditions.checkNotNull(equivalence);
/*  334 */     return this;
/*      */   }
/*      */   
/*      */   Equivalence<Object> getValueEquivalence() {
/*  338 */     return (Equivalence<Object>)MoreObjects.firstNonNull(this.valueEquivalence, getValueStrength().defaultEquivalence());
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
/*      */   public CacheBuilder<K, V> initialCapacity(int initialCapacity) {
/*  353 */     Preconditions.checkState((this.initialCapacity == -1), "initial capacity was already set to %s", this.initialCapacity);
/*      */ 
/*      */ 
/*      */     
/*  357 */     Preconditions.checkArgument((initialCapacity >= 0));
/*  358 */     this.initialCapacity = initialCapacity;
/*  359 */     return this;
/*      */   }
/*      */   
/*      */   int getInitialCapacity() {
/*  363 */     return (this.initialCapacity == -1) ? 16 : this.initialCapacity;
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
/*      */   public CacheBuilder<K, V> concurrencyLevel(int concurrencyLevel) {
/*  398 */     Preconditions.checkState((this.concurrencyLevel == -1), "concurrency level was already set to %s", this.concurrencyLevel);
/*      */ 
/*      */ 
/*      */     
/*  402 */     Preconditions.checkArgument((concurrencyLevel > 0));
/*  403 */     this.concurrencyLevel = concurrencyLevel;
/*  404 */     return this;
/*      */   }
/*      */   
/*      */   int getConcurrencyLevel() {
/*  408 */     return (this.concurrencyLevel == -1) ? 4 : this.concurrencyLevel;
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
/*      */   public CacheBuilder<K, V> maximumSize(long maximumSize) {
/*  433 */     Preconditions.checkState((this.maximumSize == -1L), "maximum size was already set to %s", this.maximumSize);
/*      */     
/*  435 */     Preconditions.checkState((this.maximumWeight == -1L), "maximum weight was already set to %s", this.maximumWeight);
/*      */ 
/*      */ 
/*      */     
/*  439 */     Preconditions.checkState((this.weigher == null), "maximum size can not be combined with weigher");
/*  440 */     Preconditions.checkArgument((maximumSize >= 0L), "maximum size must not be negative");
/*  441 */     this.maximumSize = maximumSize;
/*  442 */     return this;
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
/*      */   @GwtIncompatible
/*      */   public CacheBuilder<K, V> maximumWeight(long maximumWeight) {
/*  474 */     Preconditions.checkState((this.maximumWeight == -1L), "maximum weight was already set to %s", this.maximumWeight);
/*      */ 
/*      */ 
/*      */     
/*  478 */     Preconditions.checkState((this.maximumSize == -1L), "maximum size was already set to %s", this.maximumSize);
/*      */     
/*  480 */     Preconditions.checkArgument((maximumWeight >= 0L), "maximum weight must not be negative");
/*  481 */     this.maximumWeight = maximumWeight;
/*  482 */     return this;
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
/*      */   @GwtIncompatible
/*      */   public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> weigher(Weigher<? super K1, ? super V1> weigher) {
/*  517 */     Preconditions.checkState((this.weigher == null));
/*  518 */     if (this.strictParsing) {
/*  519 */       Preconditions.checkState((this.maximumSize == -1L), "weigher can not be combined with maximum size", this.maximumSize);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  527 */     CacheBuilder<K1, V1> me = this;
/*  528 */     me.weigher = (Weigher<? super K, ? super V>)Preconditions.checkNotNull(weigher);
/*  529 */     return me;
/*      */   }
/*      */   
/*      */   long getMaximumWeight() {
/*  533 */     if (this.expireAfterWriteNanos == 0L || this.expireAfterAccessNanos == 0L) {
/*  534 */       return 0L;
/*      */     }
/*  536 */     return (this.weigher == null) ? this.maximumSize : this.maximumWeight;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   <K1 extends K, V1 extends V> Weigher<K1, V1> getWeigher() {
/*  542 */     return (Weigher<K1, V1>)MoreObjects.firstNonNull(this.weigher, OneWeigher.INSTANCE);
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
/*      */   @GwtIncompatible
/*      */   public CacheBuilder<K, V> weakKeys() {
/*  563 */     return setKeyStrength(LocalCache.Strength.WEAK);
/*      */   }
/*      */   
/*      */   CacheBuilder<K, V> setKeyStrength(LocalCache.Strength strength) {
/*  567 */     Preconditions.checkState((this.keyStrength == null), "Key strength was already set to %s", this.keyStrength);
/*  568 */     this.keyStrength = (LocalCache.Strength)Preconditions.checkNotNull(strength);
/*  569 */     return this;
/*      */   }
/*      */   
/*      */   LocalCache.Strength getKeyStrength() {
/*  573 */     return (LocalCache.Strength)MoreObjects.firstNonNull(this.keyStrength, LocalCache.Strength.STRONG);
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
/*      */   @GwtIncompatible
/*      */   public CacheBuilder<K, V> weakValues() {
/*  595 */     return setValueStrength(LocalCache.Strength.WEAK);
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
/*      */   @GwtIncompatible
/*      */   public CacheBuilder<K, V> softValues() {
/*  620 */     return setValueStrength(LocalCache.Strength.SOFT);
/*      */   }
/*      */   
/*      */   CacheBuilder<K, V> setValueStrength(LocalCache.Strength strength) {
/*  624 */     Preconditions.checkState((this.valueStrength == null), "Value strength was already set to %s", this.valueStrength);
/*  625 */     this.valueStrength = (LocalCache.Strength)Preconditions.checkNotNull(strength);
/*  626 */     return this;
/*      */   }
/*      */   
/*      */   LocalCache.Strength getValueStrength() {
/*  630 */     return (LocalCache.Strength)MoreObjects.firstNonNull(this.valueStrength, LocalCache.Strength.STRONG);
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
/*      */   @GwtIncompatible
/*      */   public CacheBuilder<K, V> expireAfterWrite(Duration duration) {
/*  657 */     return expireAfterWrite(toNanosSaturated(duration), TimeUnit.NANOSECONDS);
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
/*      */   public CacheBuilder<K, V> expireAfterWrite(long duration, TimeUnit unit) {
/*  684 */     Preconditions.checkState((this.expireAfterWriteNanos == -1L), "expireAfterWrite was already set to %s ns", this.expireAfterWriteNanos);
/*      */ 
/*      */ 
/*      */     
/*  688 */     Preconditions.checkArgument((duration >= 0L), "duration cannot be negative: %s %s", duration, unit);
/*  689 */     this.expireAfterWriteNanos = unit.toNanos(duration);
/*  690 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   long getExpireAfterWriteNanos() {
/*  695 */     return (this.expireAfterWriteNanos == -1L) ? 0L : this.expireAfterWriteNanos;
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
/*      */   @GwtIncompatible
/*      */   public CacheBuilder<K, V> expireAfterAccess(Duration duration) {
/*  727 */     return expireAfterAccess(toNanosSaturated(duration), TimeUnit.NANOSECONDS);
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
/*      */   public CacheBuilder<K, V> expireAfterAccess(long duration, TimeUnit unit) {
/*  759 */     Preconditions.checkState((this.expireAfterAccessNanos == -1L), "expireAfterAccess was already set to %s ns", this.expireAfterAccessNanos);
/*      */ 
/*      */ 
/*      */     
/*  763 */     Preconditions.checkArgument((duration >= 0L), "duration cannot be negative: %s %s", duration, unit);
/*  764 */     this.expireAfterAccessNanos = unit.toNanos(duration);
/*  765 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   long getExpireAfterAccessNanos() {
/*  770 */     return (this.expireAfterAccessNanos == -1L) ? 
/*  771 */       0L : 
/*  772 */       this.expireAfterAccessNanos;
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
/*      */   @GwtIncompatible
/*      */   public CacheBuilder<K, V> refreshAfterWrite(Duration duration) {
/*  805 */     return refreshAfterWrite(toNanosSaturated(duration), TimeUnit.NANOSECONDS);
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
/*      */   @GwtIncompatible
/*      */   public CacheBuilder<K, V> refreshAfterWrite(long duration, TimeUnit unit) {
/*  840 */     Preconditions.checkNotNull(unit);
/*  841 */     Preconditions.checkState((this.refreshNanos == -1L), "refresh was already set to %s ns", this.refreshNanos);
/*  842 */     Preconditions.checkArgument((duration > 0L), "duration must be positive: %s %s", duration, unit);
/*  843 */     this.refreshNanos = unit.toNanos(duration);
/*  844 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   long getRefreshNanos() {
/*  849 */     return (this.refreshNanos == -1L) ? 0L : this.refreshNanos;
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
/*      */   public CacheBuilder<K, V> ticker(Ticker ticker) {
/*  863 */     Preconditions.checkState((this.ticker == null));
/*  864 */     this.ticker = (Ticker)Preconditions.checkNotNull(ticker);
/*  865 */     return this;
/*      */   }
/*      */   
/*      */   Ticker getTicker(boolean recordsTime) {
/*  869 */     if (this.ticker != null) {
/*  870 */       return this.ticker;
/*      */     }
/*  872 */     return recordsTime ? Ticker.systemTicker() : NULL_TICKER;
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
/*      */   @CheckReturnValue
/*      */   public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> removalListener(RemovalListener<? super K1, ? super V1> listener) {
/*  899 */     Preconditions.checkState((this.removalListener == null));
/*      */ 
/*      */ 
/*      */     
/*  903 */     CacheBuilder<K1, V1> me = this;
/*  904 */     me.removalListener = (RemovalListener<? super K, ? super V>)Preconditions.checkNotNull(listener);
/*  905 */     return me;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   <K1 extends K, V1 extends V> RemovalListener<K1, V1> getRemovalListener() {
/*  911 */     return 
/*  912 */       (RemovalListener<K1, V1>)MoreObjects.firstNonNull(this.removalListener, NullListener.INSTANCE);
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
/*      */   public CacheBuilder<K, V> recordStats() {
/*  925 */     this.statsCounterSupplier = CACHE_STATS_COUNTER;
/*  926 */     return this;
/*      */   }
/*      */   
/*      */   boolean isRecordingStats() {
/*  930 */     return (this.statsCounterSupplier == CACHE_STATS_COUNTER);
/*      */   }
/*      */   
/*      */   Supplier<? extends AbstractCache.StatsCounter> getStatsCounterSupplier() {
/*  934 */     return this.statsCounterSupplier;
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
/*      */   public <K1 extends K, V1 extends V> LoadingCache<K1, V1> build(CacheLoader<? super K1, V1> loader) {
/*  951 */     checkWeightWithWeigher();
/*  952 */     return new LocalCache.LocalLoadingCache<>(this, loader);
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
/*      */   public <K1 extends K, V1 extends V> Cache<K1, V1> build() {
/*  968 */     checkWeightWithWeigher();
/*  969 */     checkNonLoadingCache();
/*  970 */     return new LocalCache.LocalManualCache<>(this);
/*      */   }
/*      */   
/*      */   private void checkNonLoadingCache() {
/*  974 */     Preconditions.checkState((this.refreshNanos == -1L), "refreshAfterWrite requires a LoadingCache");
/*      */   }
/*      */   
/*      */   private void checkWeightWithWeigher() {
/*  978 */     if (this.weigher == null) {
/*  979 */       Preconditions.checkState((this.maximumWeight == -1L), "maximumWeight requires weigher");
/*      */     }
/*  981 */     else if (this.strictParsing) {
/*  982 */       Preconditions.checkState((this.maximumWeight != -1L), "weigher requires maximumWeight");
/*      */     }
/*  984 */     else if (this.maximumWeight == -1L) {
/*  985 */       logger.log(Level.WARNING, "ignoring weigher specified without maximumWeight");
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
/*      */   public String toString() {
/*  997 */     MoreObjects.ToStringHelper s = MoreObjects.toStringHelper(this);
/*  998 */     if (this.initialCapacity != -1) {
/*  999 */       s.add("initialCapacity", this.initialCapacity);
/*      */     }
/* 1001 */     if (this.concurrencyLevel != -1) {
/* 1002 */       s.add("concurrencyLevel", this.concurrencyLevel);
/*      */     }
/* 1004 */     if (this.maximumSize != -1L) {
/* 1005 */       s.add("maximumSize", this.maximumSize);
/*      */     }
/* 1007 */     if (this.maximumWeight != -1L) {
/* 1008 */       s.add("maximumWeight", this.maximumWeight);
/*      */     }
/* 1010 */     if (this.expireAfterWriteNanos != -1L) {
/* 1011 */       long l = this.expireAfterWriteNanos; s.add("expireAfterWrite", (new StringBuilder(22)).append(l).append("ns").toString());
/*      */     } 
/* 1013 */     if (this.expireAfterAccessNanos != -1L) {
/* 1014 */       long l = this.expireAfterAccessNanos; s.add("expireAfterAccess", (new StringBuilder(22)).append(l).append("ns").toString());
/*      */     } 
/* 1016 */     if (this.keyStrength != null) {
/* 1017 */       s.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
/*      */     }
/* 1019 */     if (this.valueStrength != null) {
/* 1020 */       s.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
/*      */     }
/* 1022 */     if (this.keyEquivalence != null) {
/* 1023 */       s.addValue("keyEquivalence");
/*      */     }
/* 1025 */     if (this.valueEquivalence != null) {
/* 1026 */       s.addValue("valueEquivalence");
/*      */     }
/* 1028 */     if (this.removalListener != null) {
/* 1029 */       s.addValue("removalListener");
/*      */     }
/* 1031 */     return s.toString();
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
/*      */   @GwtIncompatible
/*      */   private static long toNanosSaturated(Duration duration) {
/*      */     try {
/* 1047 */       return duration.toNanos();
/* 1048 */     } catch (ArithmeticException tooBig) {
/* 1049 */       return duration.isNegative() ? Long.MIN_VALUE : Long.MAX_VALUE;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\cache\CacheBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */