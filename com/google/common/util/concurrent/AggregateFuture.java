/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableCollection;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.errorprone.annotations.ForOverride;
/*     */ import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ abstract class AggregateFuture<InputT, OutputT>
/*     */   extends AggregateFutureState<OutputT>
/*     */ {
/*  43 */   private static final Logger logger = Logger.getLogger(AggregateFuture.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean allMustSucceed;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean collectsValues;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AggregateFuture(ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures, boolean allMustSucceed, boolean collectsValues) {
/*  64 */     super(futures.size());
/*  65 */     this.futures = (ImmutableCollection<? extends ListenableFuture<? extends InputT>>)Preconditions.checkNotNull(futures);
/*  66 */     this.allMustSucceed = allMustSucceed;
/*  67 */     this.collectsValues = collectsValues;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void afterDone() {
/*  72 */     super.afterDone();
/*     */     
/*  74 */     ImmutableCollection<? extends Future<?>> localFutures = (ImmutableCollection)this.futures;
/*  75 */     releaseResources(ReleaseResourcesReason.OUTPUT_FUTURE_DONE);
/*     */     
/*  77 */     if ((isCancelled() & ((localFutures != null) ? 1 : 0)) != 0) {
/*  78 */       boolean wasInterrupted = wasInterrupted();
/*  79 */       for (UnmodifiableIterator<Future> unmodifiableIterator = localFutures.iterator(); unmodifiableIterator.hasNext(); ) { Future<?> future = unmodifiableIterator.next();
/*  80 */         future.cancel(wasInterrupted); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String pendingToString() {
/*  91 */     ImmutableCollection<? extends Future<?>> localFutures = (ImmutableCollection)this.futures;
/*  92 */     if (localFutures != null) {
/*  93 */       String str = String.valueOf(localFutures); return (new StringBuilder(8 + String.valueOf(str).length())).append("futures=").append(str).toString();
/*     */     } 
/*  95 */     return super.pendingToString();
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
/*     */   final void init() {
/* 107 */     if (this.futures.isEmpty()) {
/* 108 */       handleAllCompleted();
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 115 */     if (this.allMustSucceed) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 125 */       int i = 0;
/* 126 */       for (UnmodifiableIterator<ListenableFuture<? extends InputT>> unmodifiableIterator = this.futures.iterator(); unmodifiableIterator.hasNext(); ) { final ListenableFuture<? extends InputT> future = unmodifiableIterator.next();
/* 127 */         final int index = i++;
/* 128 */         future.addListener(new Runnable()
/*     */             {
/*     */               public void run()
/*     */               {
/*     */                 try {
/* 133 */                   if (future.isCancelled()) {
/*     */ 
/*     */                     
/* 136 */                     AggregateFuture.this.futures = null;
/* 137 */                     AggregateFuture.this.cancel(false);
/*     */                   } else {
/* 139 */                     AggregateFuture.this.collectValueFromNonCancelledFuture(index, future);
/*     */                   
/*     */                   }
/*     */ 
/*     */                 
/*     */                 }
/*     */                 finally {
/*     */                   
/* 147 */                   AggregateFuture.this.decrementCountAndMaybeComplete((ImmutableCollection<? extends Future<? extends InputT>>)null);
/*     */                 }
/*     */               
/*     */               }
/* 151 */             }MoreExecutors.directExecutor());
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */          }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 171 */       final ImmutableCollection<? extends ListenableFuture<? extends InputT>> localFutures = this.collectsValues ? this.futures : null;
/* 172 */       Runnable listener = new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 176 */             AggregateFuture.this.decrementCountAndMaybeComplete(localFutures);
/*     */           }
/*     */         };
/* 179 */       for (UnmodifiableIterator<ListenableFuture<? extends InputT>> unmodifiableIterator = this.futures.iterator(); unmodifiableIterator.hasNext(); ) { final ListenableFuture<? extends InputT> future = unmodifiableIterator.next();
/* 180 */         future.addListener(listener, MoreExecutors.directExecutor()); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleException(Throwable throwable) {
/* 192 */     Preconditions.checkNotNull(throwable);
/*     */     
/* 194 */     if (this.allMustSucceed) {
/*     */ 
/*     */       
/* 197 */       boolean completedWithFailure = setException(throwable);
/* 198 */       if (!completedWithFailure) {
/*     */ 
/*     */         
/* 201 */         boolean firstTimeSeeingThisException = addCausalChain(getOrInitSeenExceptions(), throwable);
/* 202 */         if (firstTimeSeeingThisException) {
/* 203 */           log(throwable);
/*     */ 
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 213 */     if (throwable instanceof Error)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 221 */       log(throwable);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void log(Throwable throwable) {
/* 229 */     String message = (throwable instanceof Error) ? "Input Future failed with Error" : "Got more than one input Future failure. Logging failures after the first";
/* 230 */     logger.log(Level.SEVERE, message, throwable);
/*     */   }
/*     */ 
/*     */   
/*     */   final void addInitialException(Set<Throwable> seen) {
/* 235 */     Preconditions.checkNotNull(seen);
/* 236 */     if (!isCancelled())
/*     */     {
/* 238 */       boolean bool = addCausalChain(seen, tryInternalFastPathGetFailure());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void collectValueFromNonCancelledFuture(int index, Future<? extends InputT> future) {
/*     */     try {
/* 249 */       collectOneValue(index, Futures.getDone((Future)future));
/* 250 */     } catch (ExecutionException e) {
/* 251 */       handleException(e.getCause());
/* 252 */     } catch (Throwable t) {
/* 253 */       handleException(t);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void decrementCountAndMaybeComplete(ImmutableCollection<? extends Future<? extends InputT>> futuresIfNeedToCollectAtCompletion) {
/* 261 */     int newRemaining = decrementRemainingAndGet();
/* 262 */     Preconditions.checkState((newRemaining >= 0), "Less than 0 remaining futures");
/* 263 */     if (newRemaining == 0) {
/* 264 */       processCompleted(futuresIfNeedToCollectAtCompletion);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processCompleted(ImmutableCollection<? extends Future<? extends InputT>> futuresIfNeedToCollectAtCompletion) {
/* 272 */     if (futuresIfNeedToCollectAtCompletion != null) {
/* 273 */       int i = 0;
/* 274 */       for (UnmodifiableIterator<Future<? extends InputT>> unmodifiableIterator = futuresIfNeedToCollectAtCompletion.iterator(); unmodifiableIterator.hasNext(); ) { Future<? extends InputT> future = unmodifiableIterator.next();
/* 275 */         if (!future.isCancelled()) {
/* 276 */           collectValueFromNonCancelledFuture(i, future);
/*     */         }
/* 278 */         i++; }
/*     */     
/*     */     } 
/* 281 */     clearSeenExceptions();
/* 282 */     handleAllCompleted();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 290 */     releaseResources(ReleaseResourcesReason.ALL_INPUT_FUTURES_PROCESSED);
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
/*     */   @ForOverride
/*     */   @OverridingMethodsMustInvokeSuper
/*     */   void releaseResources(ReleaseResourcesReason reason) {
/* 306 */     Preconditions.checkNotNull(reason);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 313 */     this.futures = null;
/*     */   }
/*     */   
/*     */   enum ReleaseResourcesReason {
/* 317 */     OUTPUT_FUTURE_DONE,
/* 318 */     ALL_INPUT_FUTURES_PROCESSED;
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
/*     */   private static boolean addCausalChain(Set<Throwable> seen, Throwable t) {
/* 331 */     for (; t != null; t = t.getCause()) {
/* 332 */       boolean firstTimeSeen = seen.add(t);
/* 333 */       if (!firstTimeSeen)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 340 */         return false;
/*     */       }
/*     */     } 
/* 343 */     return true;
/*     */   }
/*     */   
/*     */   abstract void collectOneValue(int paramInt, InputT paramInputT);
/*     */   
/*     */   abstract void handleAllCompleted();
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\commo\\util\concurrent\AggregateFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */