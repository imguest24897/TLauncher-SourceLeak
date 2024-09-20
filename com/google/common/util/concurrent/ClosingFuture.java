/*      */ package com.google.common.util.concurrent;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Functions;
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.collect.FluentIterable;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.DoNotMock;
/*      */ import com.google.j2objc.annotations.RetainedWith;
/*      */ import java.io.Closeable;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.CountDownLatch;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.RejectedExecutionException;
/*      */ import java.util.concurrent.atomic.AtomicReference;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @DoNotMock("Use ClosingFuture.from(Futures.immediate*Future)")
/*      */ @Beta
/*      */ public final class ClosingFuture<V>
/*      */ {
/*  196 */   private static final Logger logger = Logger.getLogger(ClosingFuture.class.getName());
/*      */ 
/*      */   
/*      */   public static final class DeferredCloser
/*      */   {
/*      */     @RetainedWith
/*      */     private final ClosingFuture.CloseableList list;
/*      */ 
/*      */     
/*      */     DeferredCloser(ClosingFuture.CloseableList list) {
/*  206 */       this.list = list;
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
/*      */ 
/*      */ 
/*      */ 
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
/*      */     public <C extends AutoCloseable> C eventuallyClose(C closeable, Executor closingExecutor) {
/*  235 */       Preconditions.checkNotNull(closingExecutor);
/*  236 */       if (closeable != null) {
/*  237 */         this.list.add((AutoCloseable)closeable, closingExecutor);
/*      */       }
/*  239 */       return closeable;
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
/*      */   public static final class ValueAndCloser<V>
/*      */   {
/*      */     private final ClosingFuture<? extends V> closingFuture;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ValueAndCloser(ClosingFuture<? extends V> closingFuture) {
/*  330 */       this.closingFuture = (ClosingFuture<? extends V>)Preconditions.checkNotNull(closingFuture);
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
/*      */     public V get() throws ExecutionException {
/*  345 */       return Futures.getDone(this.closingFuture.future);
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
/*      */     public void closeAsync() {
/*  359 */       this.closingFuture.close();
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
/*      */   public static <V> ClosingFuture<V> submit(ClosingCallable<V> callable, Executor executor) {
/*  384 */     return new ClosingFuture<>(callable, executor);
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
/*      */   public static <V> ClosingFuture<V> submitAsync(AsyncClosingCallable<V> callable, Executor executor) {
/*  396 */     return new ClosingFuture<>(callable, executor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> ClosingFuture<V> from(ListenableFuture<V> future) {
/*  407 */     return new ClosingFuture<>(future);
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
/*      */   @Deprecated
/*      */   public static <C extends AutoCloseable> ClosingFuture<C> eventuallyClosing(ListenableFuture<C> future, final Executor closingExecutor) {
/*  435 */     Preconditions.checkNotNull(closingExecutor);
/*  436 */     final ClosingFuture<C> closingFuture = new ClosingFuture<>(Futures.nonCancellationPropagating(future));
/*  437 */     Futures.addCallback(future, (FutureCallback)new FutureCallback<AutoCloseable>()
/*      */         {
/*      */           
/*      */           public void onSuccess(AutoCloseable result)
/*      */           {
/*  442 */             closingFuture.closeables.closer.eventuallyClose(result, closingExecutor);
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*      */           public void onFailure(Throwable t) {}
/*  448 */         }MoreExecutors.directExecutor());
/*  449 */     return closingFuture;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Combiner whenAllComplete(Iterable<? extends ClosingFuture<?>> futures) {
/*  459 */     return new Combiner(false, futures);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Combiner whenAllComplete(ClosingFuture<?> future1, ClosingFuture<?>... moreFutures) {
/*  470 */     return whenAllComplete(Lists.asList(future1, (Object[])moreFutures));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Combiner whenAllSucceed(Iterable<? extends ClosingFuture<?>> futures) {
/*  481 */     return new Combiner(true, futures);
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
/*      */   public static <V1, V2> Combiner2<V1, V2> whenAllSucceed(ClosingFuture<V1> future1, ClosingFuture<V2> future2) {
/*  496 */     return new Combiner2<>(future1, future2);
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
/*      */   public static <V1, V2, V3> Combiner3<V1, V2, V3> whenAllSucceed(ClosingFuture<V1> future1, ClosingFuture<V2> future2, ClosingFuture<V3> future3) {
/*  511 */     return new Combiner3<>(future1, future2, future3);
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
/*      */   public static <V1, V2, V3, V4> Combiner4<V1, V2, V3, V4> whenAllSucceed(ClosingFuture<V1> future1, ClosingFuture<V2> future2, ClosingFuture<V3> future3, ClosingFuture<V4> future4) {
/*  529 */     return new Combiner4<>(future1, future2, future3, future4);
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
/*      */   public static <V1, V2, V3, V4, V5> Combiner5<V1, V2, V3, V4, V5> whenAllSucceed(ClosingFuture<V1> future1, ClosingFuture<V2> future2, ClosingFuture<V3> future3, ClosingFuture<V4> future4, ClosingFuture<V5> future5) {
/*  548 */     return new Combiner5<>(future1, future2, future3, future4, future5);
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
/*      */   public static Combiner whenAllSucceed(ClosingFuture<?> future1, ClosingFuture<?> future2, ClosingFuture<?> future3, ClosingFuture<?> future4, ClosingFuture<?> future5, ClosingFuture<?> future6, ClosingFuture<?>... moreFutures) {
/*  566 */     return whenAllSucceed(
/*  567 */         (Iterable<? extends ClosingFuture<?>>)FluentIterable.of(future1, (Object[])new ClosingFuture[] { future2, future3, future4, future5, future6
/*  568 */           }).append((Object[])moreFutures));
/*      */   }
/*      */   
/*  571 */   private final AtomicReference<State> state = new AtomicReference<>(State.OPEN);
/*  572 */   private final CloseableList closeables = new CloseableList();
/*      */   private final FluentFuture<V> future;
/*      */   
/*      */   private ClosingFuture(ListenableFuture<V> future) {
/*  576 */     this.future = FluentFuture.from(future);
/*      */   }
/*      */   
/*      */   private ClosingFuture(final ClosingCallable<V> callable, Executor executor) {
/*  580 */     Preconditions.checkNotNull(callable);
/*      */     
/*  582 */     TrustedListenableFutureTask<V> task = TrustedListenableFutureTask.create(new Callable<V>()
/*      */         {
/*      */           public V call() throws Exception
/*      */           {
/*  586 */             return callable.call(ClosingFuture.this.closeables.closer);
/*      */           }
/*      */ 
/*      */           
/*      */           public String toString() {
/*  591 */             return callable.toString();
/*      */           }
/*      */         });
/*  594 */     executor.execute(task);
/*  595 */     this.future = task;
/*      */   }
/*      */   
/*      */   private ClosingFuture(final AsyncClosingCallable<V> callable, Executor executor) {
/*  599 */     Preconditions.checkNotNull(callable);
/*      */     
/*  601 */     TrustedListenableFutureTask<V> task = TrustedListenableFutureTask.create(new AsyncCallable<V>()
/*      */         {
/*      */           public ListenableFuture<V> call() throws Exception
/*      */           {
/*  605 */             ClosingFuture.CloseableList newCloseables = new ClosingFuture.CloseableList();
/*      */             try {
/*  607 */               ClosingFuture<V> closingFuture = callable.call(newCloseables.closer);
/*  608 */               closingFuture.becomeSubsumedInto(ClosingFuture.this.closeables);
/*  609 */               return closingFuture.future;
/*      */             } finally {
/*  611 */               ClosingFuture.this.closeables.add(newCloseables, MoreExecutors.directExecutor());
/*      */             } 
/*      */           }
/*      */ 
/*      */           
/*      */           public String toString() {
/*  617 */             return callable.toString();
/*      */           }
/*      */         });
/*  620 */     executor.execute(task);
/*  621 */     this.future = task;
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
/*      */   public ListenableFuture<?> statusFuture() {
/*  636 */     return Futures.nonCancellationPropagating(this.future.transform(Functions.constant(null), MoreExecutors.directExecutor()));
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
/*      */   public <U> ClosingFuture<U> transform(final ClosingFunction<? super V, U> function, Executor executor) {
/*  674 */     Preconditions.checkNotNull(function);
/*  675 */     AsyncFunction<V, U> applyFunction = new AsyncFunction<V, U>()
/*      */       {
/*      */         public ListenableFuture<U> apply(V input) throws Exception
/*      */         {
/*  679 */           return ClosingFuture.this.closeables.applyClosingFunction(function, input);
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/*  684 */           return function.toString();
/*      */         }
/*      */       };
/*      */     
/*  688 */     return derive(this.future.transformAsync(applyFunction, executor));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <U> ClosingFuture<U> transformAsync(final AsyncClosingFunction<? super V, U> function, Executor executor) {
/*  768 */     Preconditions.checkNotNull(function);
/*  769 */     AsyncFunction<V, U> applyFunction = new AsyncFunction<V, U>()
/*      */       {
/*      */         public ListenableFuture<U> apply(V input) throws Exception
/*      */         {
/*  773 */           return ClosingFuture.this.closeables.applyAsyncClosingFunction(function, input);
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/*  778 */           return function.toString();
/*      */         }
/*      */       };
/*  781 */     return derive(this.future.transformAsync(applyFunction, executor));
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
/*      */   public static <V, U> AsyncClosingFunction<V, U> withoutCloser(final AsyncFunction<V, U> function) {
/*  812 */     Preconditions.checkNotNull(function);
/*  813 */     return new AsyncClosingFunction<V, U>()
/*      */       {
/*      */         public ClosingFuture<U> apply(ClosingFuture.DeferredCloser closer, V input) throws Exception {
/*  816 */           return ClosingFuture.from(function.apply(input));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <X extends Throwable> ClosingFuture<V> catching(Class<X> exceptionType, ClosingFunction<? super X, ? extends V> fallback, Executor executor) {
/*  864 */     return catchingMoreGeneric(exceptionType, fallback, executor);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private <X extends Throwable, W extends V> ClosingFuture<V> catchingMoreGeneric(Class<X> exceptionType, final ClosingFunction<? super X, W> fallback, Executor executor) {
/*  870 */     Preconditions.checkNotNull(fallback);
/*  871 */     AsyncFunction<X, W> applyFallback = new AsyncFunction<X, W>()
/*      */       {
/*      */         public ListenableFuture<W> apply(X exception) throws Exception
/*      */         {
/*  875 */           return ClosingFuture.this.closeables.applyClosingFunction(fallback, exception);
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/*  880 */           return fallback.toString();
/*      */         }
/*      */       };
/*      */     
/*  884 */     return derive(this.future.catchingAsync(exceptionType, applyFallback, executor));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <X extends Throwable> ClosingFuture<V> catchingAsync(Class<X> exceptionType, AsyncClosingFunction<? super X, ? extends V> fallback, Executor executor) {
/*  961 */     return catchingAsyncMoreGeneric(exceptionType, fallback, executor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private <X extends Throwable, W extends V> ClosingFuture<V> catchingAsyncMoreGeneric(Class<X> exceptionType, final AsyncClosingFunction<? super X, W> fallback, Executor executor) {
/*  969 */     Preconditions.checkNotNull(fallback);
/*  970 */     AsyncFunction<X, W> asyncFunction = new AsyncFunction<X, W>()
/*      */       {
/*      */         public ListenableFuture<W> apply(X exception) throws Exception
/*      */         {
/*  974 */           return ClosingFuture.this.closeables.applyAsyncClosingFunction(fallback, exception);
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/*  979 */           return fallback.toString();
/*      */         }
/*      */       };
/*  982 */     return derive(this.future.catchingAsync(exceptionType, asyncFunction, executor));
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
/*      */   public FluentFuture<V> finishToFuture() {
/*  997 */     if (compareAndUpdateState(State.OPEN, State.WILL_CLOSE)) {
/*  998 */       logger.log(Level.FINER, "will close {0}", this);
/*  999 */       this.future.addListener(new Runnable()
/*      */           {
/*      */             public void run()
/*      */             {
/* 1003 */               ClosingFuture.this.checkAndUpdateState(ClosingFuture.State.WILL_CLOSE, ClosingFuture.State.CLOSING);
/* 1004 */               ClosingFuture.this.close();
/* 1005 */               ClosingFuture.this.checkAndUpdateState(ClosingFuture.State.CLOSING, ClosingFuture.State.CLOSED);
/*      */             }
/* 1008 */           }MoreExecutors.directExecutor());
/*      */     } else {
/* 1010 */       switch ((State)this.state.get()) {
/*      */         case SUBSUMED:
/* 1012 */           throw new IllegalStateException("Cannot call finishToFuture() after deriving another step");
/*      */ 
/*      */         
/*      */         case WILL_CREATE_VALUE_AND_CLOSER:
/* 1016 */           throw new IllegalStateException("Cannot call finishToFuture() after calling finishToValueAndCloser()");
/*      */ 
/*      */         
/*      */         case WILL_CLOSE:
/*      */         case CLOSING:
/*      */         case CLOSED:
/* 1022 */           throw new IllegalStateException("Cannot call finishToFuture() twice");
/*      */         
/*      */         case OPEN:
/* 1025 */           throw new AssertionError();
/*      */       } 
/*      */     } 
/* 1028 */     return this.future;
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
/*      */   public void finishToValueAndCloser(final ValueAndCloserConsumer<? super V> consumer, Executor executor) {
/* 1044 */     Preconditions.checkNotNull(consumer);
/* 1045 */     if (!compareAndUpdateState(State.OPEN, State.WILL_CREATE_VALUE_AND_CLOSER)) {
/* 1046 */       switch ((State)this.state.get()) {
/*      */         case SUBSUMED:
/* 1048 */           throw new IllegalStateException("Cannot call finishToValueAndCloser() after deriving another step");
/*      */ 
/*      */         
/*      */         case WILL_CLOSE:
/*      */         case CLOSING:
/*      */         case CLOSED:
/* 1054 */           throw new IllegalStateException("Cannot call finishToValueAndCloser() after calling finishToFuture()");
/*      */ 
/*      */         
/*      */         case WILL_CREATE_VALUE_AND_CLOSER:
/* 1058 */           throw new IllegalStateException("Cannot call finishToValueAndCloser() twice");
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1063 */       throw new AssertionError(this.state);
/*      */     } 
/* 1065 */     this.future.addListener(new Runnable()
/*      */         {
/*      */           public void run()
/*      */           {
/* 1069 */             ClosingFuture.provideValueAndCloser(consumer, ClosingFuture.this);
/*      */           }
/*      */         }executor);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static <C, V extends C> void provideValueAndCloser(ValueAndCloserConsumer<C> consumer, ClosingFuture<V> closingFuture) {
/* 1077 */     consumer.accept(new ValueAndCloser<>(closingFuture));
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
/*      */   @CanIgnoreReturnValue
/*      */   public boolean cancel(boolean mayInterruptIfRunning) {
/* 1098 */     logger.log(Level.FINER, "cancelling {0}", this);
/* 1099 */     boolean cancelled = this.future.cancel(mayInterruptIfRunning);
/* 1100 */     if (cancelled) {
/* 1101 */       close();
/*      */     }
/* 1103 */     return cancelled;
/*      */   }
/*      */   
/*      */   private void close() {
/* 1107 */     logger.log(Level.FINER, "closing {0}", this);
/* 1108 */     this.closeables.close();
/*      */   }
/*      */   
/*      */   private <U> ClosingFuture<U> derive(FluentFuture<U> future) {
/* 1112 */     ClosingFuture<U> derived = new ClosingFuture(future);
/* 1113 */     becomeSubsumedInto(derived.closeables);
/* 1114 */     return derived;
/*      */   }
/*      */   
/*      */   private void becomeSubsumedInto(CloseableList otherCloseables) {
/* 1118 */     checkAndUpdateState(State.OPEN, State.SUBSUMED);
/* 1119 */     otherCloseables.add(this.closeables, MoreExecutors.directExecutor());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class Peeker
/*      */   {
/*      */     private final ImmutableList<ClosingFuture<?>> futures;
/*      */ 
/*      */     
/*      */     private volatile boolean beingCalled;
/*      */ 
/*      */     
/*      */     private Peeker(ImmutableList<ClosingFuture<?>> futures) {
/* 1133 */       this.futures = (ImmutableList<ClosingFuture<?>>)Preconditions.checkNotNull(futures);
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
/*      */     public final <D> D getDone(ClosingFuture<D> closingFuture) throws ExecutionException {
/* 1149 */       Preconditions.checkState(this.beingCalled);
/* 1150 */       Preconditions.checkArgument(this.futures.contains(closingFuture));
/* 1151 */       return Futures.getDone(closingFuture.future);
/*      */     }
/*      */ 
/*      */     
/*      */     private <V> V call(ClosingFuture.Combiner.CombiningCallable<V> combiner, ClosingFuture.CloseableList closeables) throws Exception {
/* 1156 */       this.beingCalled = true;
/* 1157 */       ClosingFuture.CloseableList newCloseables = new ClosingFuture.CloseableList();
/*      */       try {
/* 1159 */         return combiner.call(newCloseables.closer, this);
/*      */       } finally {
/* 1161 */         closeables.add(newCloseables, MoreExecutors.directExecutor());
/* 1162 */         this.beingCalled = false;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private <V> FluentFuture<V> callAsync(ClosingFuture.Combiner.AsyncCombiningCallable<V> combiner, ClosingFuture.CloseableList closeables) throws Exception {
/* 1168 */       this.beingCalled = true;
/* 1169 */       ClosingFuture.CloseableList newCloseables = new ClosingFuture.CloseableList();
/*      */       try {
/* 1171 */         ClosingFuture<V> closingFuture = combiner.call(newCloseables.closer, this);
/* 1172 */         closingFuture.becomeSubsumedInto(closeables);
/* 1173 */         return closingFuture.future;
/*      */       } finally {
/* 1175 */         closeables.add(newCloseables, MoreExecutors.directExecutor());
/* 1176 */         this.beingCalled = false;
/*      */       } 
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
/*      */   @DoNotMock("Use ClosingFuture.whenAllSucceed() or .whenAllComplete() instead.")
/*      */   public static class Combiner
/*      */   {
/* 1209 */     private final ClosingFuture.CloseableList closeables = new ClosingFuture.CloseableList();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final boolean allMustSucceed;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected final ImmutableList<ClosingFuture<?>> inputs;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Combiner(boolean allMustSucceed, Iterable<? extends ClosingFuture<?>> inputs) {
/* 1255 */       this.allMustSucceed = allMustSucceed;
/* 1256 */       this.inputs = ImmutableList.copyOf(inputs);
/* 1257 */       for (ClosingFuture<?> input : inputs) {
/* 1258 */         input.becomeSubsumedInto(this.closeables);
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
/*      */ 
/*      */ 
/*      */     
/*      */     public <V> ClosingFuture<V> call(final CombiningCallable<V> combiningCallable, Executor executor) {
/* 1278 */       Callable<V> callable = new Callable<V>()
/*      */         {
/*      */           public V call() throws Exception
/*      */           {
/* 1282 */             return (new ClosingFuture.Peeker(ClosingFuture.Combiner.this.inputs)).call(combiningCallable, ClosingFuture.Combiner.this.closeables);
/*      */           }
/*      */ 
/*      */           
/*      */           public String toString() {
/* 1287 */             return combiningCallable.toString();
/*      */           }
/*      */         };
/* 1290 */       ClosingFuture<V> derived = new ClosingFuture<>(futureCombiner().call(callable, executor));
/* 1291 */       derived.closeables.add(this.closeables, MoreExecutors.directExecutor());
/* 1292 */       return derived;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <V> ClosingFuture<V> callAsync(final AsyncCombiningCallable<V> combiningCallable, Executor executor) {
/* 1334 */       AsyncCallable<V> asyncCallable = new AsyncCallable<V>()
/*      */         {
/*      */           public ListenableFuture<V> call() throws Exception
/*      */           {
/* 1338 */             return (new ClosingFuture.Peeker(ClosingFuture.Combiner.this.inputs)).callAsync(combiningCallable, ClosingFuture.Combiner.this.closeables);
/*      */           }
/*      */ 
/*      */           
/*      */           public String toString() {
/* 1343 */             return combiningCallable.toString();
/*      */           }
/*      */         };
/*      */       
/* 1347 */       ClosingFuture<V> derived = new ClosingFuture<>(futureCombiner().callAsync(asyncCallable, executor));
/* 1348 */       derived.closeables.add(this.closeables, MoreExecutors.directExecutor());
/* 1349 */       return derived;
/*      */     }
/*      */     
/*      */     private Futures.FutureCombiner<Object> futureCombiner() {
/* 1353 */       return this.allMustSucceed ? 
/* 1354 */         Futures.<Object>whenAllSucceed((Iterable)inputFutures()) : 
/* 1355 */         Futures.<Object>whenAllComplete((Iterable)inputFutures());
/*      */     }
/*      */     
/* 1358 */     private static final Function<ClosingFuture<?>, FluentFuture<?>> INNER_FUTURE = new Function<ClosingFuture<?>, FluentFuture<?>>()
/*      */       {
/*      */         public FluentFuture<?> apply(ClosingFuture<?> future)
/*      */         {
/* 1362 */           return future.future;
/*      */         }
/*      */       };
/*      */     
/*      */     private ImmutableList<FluentFuture<?>> inputFutures() {
/* 1367 */       return FluentIterable.from((Iterable)this.inputs).transform(INNER_FUTURE).toList();
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
/*      */     @FunctionalInterface
/*      */     public static interface CombiningCallable<V>
/*      */     {
/*      */       V call(ClosingFuture.DeferredCloser param2DeferredCloser, ClosingFuture.Peeker param2Peeker) throws Exception;
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
/*      */     @FunctionalInterface
/*      */     public static interface AsyncCombiningCallable<V>
/*      */     {
/*      */       ClosingFuture<V> call(ClosingFuture.DeferredCloser param2DeferredCloser, ClosingFuture.Peeker param2Peeker) throws Exception;
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
/*      */   public static final class Combiner2<V1, V2>
/*      */     extends Combiner
/*      */   {
/*      */     private final ClosingFuture<V1> future1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ClosingFuture<V2> future2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Combiner2(ClosingFuture<V1> future1, ClosingFuture<V2> future2) {
/* 1432 */       super(true, (Iterable)ImmutableList.of(future1, future2));
/* 1433 */       this.future1 = future1;
/* 1434 */       this.future2 = future2;
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
/*      */ 
/*      */     
/*      */     public <U> ClosingFuture<U> call(final ClosingFunction2<V1, V2, U> function, Executor executor) {
/* 1452 */       return call(new ClosingFuture.Combiner.CombiningCallable<U>()
/*      */           {
/*      */             public U call(ClosingFuture.DeferredCloser closer, ClosingFuture.Peeker peeker) throws Exception
/*      */             {
/* 1456 */               return (U)function.apply(closer, peeker.getDone(ClosingFuture.Combiner2.this.future1), peeker.getDone(ClosingFuture.Combiner2.this.future2));
/*      */             }
/*      */ 
/*      */             
/*      */             public String toString() {
/* 1461 */               return function.toString();
/*      */             }
/*      */           }executor);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <U> ClosingFuture<U> callAsync(final AsyncClosingFunction2<V1, V2, U> function, Executor executor) {
/* 1505 */       return callAsync(new ClosingFuture.Combiner.AsyncCombiningCallable<U>()
/*      */           {
/*      */             public ClosingFuture<U> call(ClosingFuture.DeferredCloser closer, ClosingFuture.Peeker peeker) throws Exception
/*      */             {
/* 1509 */               return function.apply(closer, peeker.getDone(ClosingFuture.Combiner2.this.future1), peeker.getDone(ClosingFuture.Combiner2.this.future2));
/*      */             }
/*      */ 
/*      */             
/*      */             public String toString() {
/* 1514 */               return function.toString();
/*      */             }
/*      */           }executor);
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
/*      */     @FunctionalInterface
/*      */     public static interface ClosingFunction2<V1, V2, U>
/*      */     {
/*      */       U apply(ClosingFuture.DeferredCloser param2DeferredCloser, V1 param2V1, V2 param2V2) throws Exception;
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
/*      */     @FunctionalInterface
/*      */     public static interface AsyncClosingFunction2<V1, V2, U>
/*      */     {
/*      */       ClosingFuture<U> apply(ClosingFuture.DeferredCloser param2DeferredCloser, V1 param2V1, V2 param2V2) throws Exception;
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
/*      */   public static final class Combiner3<V1, V2, V3>
/*      */     extends Combiner
/*      */   {
/*      */     private final ClosingFuture<V1> future1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ClosingFuture<V2> future2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ClosingFuture<V3> future3;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Combiner3(ClosingFuture<V1> future1, ClosingFuture<V2> future2, ClosingFuture<V3> future3) {
/* 1592 */       super(true, (Iterable)ImmutableList.of(future1, future2, future3));
/* 1593 */       this.future1 = future1;
/* 1594 */       this.future2 = future2;
/* 1595 */       this.future3 = future3;
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
/*      */ 
/*      */     
/*      */     public <U> ClosingFuture<U> call(final ClosingFunction3<V1, V2, V3, U> function, Executor executor) {
/* 1613 */       return call(new ClosingFuture.Combiner.CombiningCallable<U>()
/*      */           {
/*      */             public U call(ClosingFuture.DeferredCloser closer, ClosingFuture.Peeker peeker) throws Exception
/*      */             {
/* 1617 */               return (U)function.apply(closer, peeker
/*      */                   
/* 1619 */                   .getDone(ClosingFuture.Combiner3.this.future1), peeker
/* 1620 */                   .getDone(ClosingFuture.Combiner3.this.future2), peeker
/* 1621 */                   .getDone(ClosingFuture.Combiner3.this.future3));
/*      */             }
/*      */ 
/*      */             
/*      */             public String toString() {
/* 1626 */               return function.toString();
/*      */             }
/*      */           }executor);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <U> ClosingFuture<U> callAsync(final AsyncClosingFunction3<V1, V2, V3, U> function, Executor executor) {
/* 1670 */       return callAsync(new ClosingFuture.Combiner.AsyncCombiningCallable<U>()
/*      */           {
/*      */             public ClosingFuture<U> call(ClosingFuture.DeferredCloser closer, ClosingFuture.Peeker peeker) throws Exception
/*      */             {
/* 1674 */               return function.apply(closer, peeker
/*      */                   
/* 1676 */                   .getDone(ClosingFuture.Combiner3.this.future1), peeker
/* 1677 */                   .getDone(ClosingFuture.Combiner3.this.future2), peeker
/* 1678 */                   .getDone(ClosingFuture.Combiner3.this.future3));
/*      */             }
/*      */ 
/*      */             
/*      */             public String toString() {
/* 1683 */               return function.toString();
/*      */             }
/*      */           }executor);
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
/*      */     @FunctionalInterface
/*      */     public static interface ClosingFunction3<V1, V2, V3, U>
/*      */     {
/*      */       U apply(ClosingFuture.DeferredCloser param2DeferredCloser, V1 param2V1, V2 param2V2, V3 param2V3) throws Exception;
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
/*      */     @FunctionalInterface
/*      */     public static interface AsyncClosingFunction3<V1, V2, V3, U>
/*      */     {
/*      */       ClosingFuture<U> apply(ClosingFuture.DeferredCloser param2DeferredCloser, V1 param2V1, V2 param2V2, V3 param2V3) throws Exception;
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
/*      */   public static final class Combiner4<V1, V2, V3, V4>
/*      */     extends Combiner
/*      */   {
/*      */     private final ClosingFuture<V1> future1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ClosingFuture<V2> future2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ClosingFuture<V3> future3;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ClosingFuture<V4> future4;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Combiner4(ClosingFuture<V1> future1, ClosingFuture<V2> future2, ClosingFuture<V3> future3, ClosingFuture<V4> future4) {
/* 1774 */       super(true, (Iterable)ImmutableList.of(future1, future2, future3, future4));
/* 1775 */       this.future1 = future1;
/* 1776 */       this.future2 = future2;
/* 1777 */       this.future3 = future3;
/* 1778 */       this.future4 = future4;
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
/*      */ 
/*      */     
/*      */     public <U> ClosingFuture<U> call(final ClosingFunction4<V1, V2, V3, V4, U> function, Executor executor) {
/* 1796 */       return call(new ClosingFuture.Combiner.CombiningCallable<U>()
/*      */           {
/*      */             public U call(ClosingFuture.DeferredCloser closer, ClosingFuture.Peeker peeker) throws Exception
/*      */             {
/* 1800 */               return (U)function.apply(closer, peeker
/*      */                   
/* 1802 */                   .getDone(ClosingFuture.Combiner4.this.future1), peeker
/* 1803 */                   .getDone(ClosingFuture.Combiner4.this.future2), peeker
/* 1804 */                   .getDone(ClosingFuture.Combiner4.this.future3), peeker
/* 1805 */                   .getDone(ClosingFuture.Combiner4.this.future4));
/*      */             }
/*      */ 
/*      */             
/*      */             public String toString() {
/* 1810 */               return function.toString();
/*      */             }
/*      */           }executor);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <U> ClosingFuture<U> callAsync(final AsyncClosingFunction4<V1, V2, V3, V4, U> function, Executor executor) {
/* 1854 */       return callAsync(new ClosingFuture.Combiner.AsyncCombiningCallable<U>()
/*      */           {
/*      */             public ClosingFuture<U> call(ClosingFuture.DeferredCloser closer, ClosingFuture.Peeker peeker) throws Exception
/*      */             {
/* 1858 */               return function.apply(closer, peeker
/*      */                   
/* 1860 */                   .getDone(ClosingFuture.Combiner4.this.future1), peeker
/* 1861 */                   .getDone(ClosingFuture.Combiner4.this.future2), peeker
/* 1862 */                   .getDone(ClosingFuture.Combiner4.this.future3), peeker
/* 1863 */                   .getDone(ClosingFuture.Combiner4.this.future4));
/*      */             }
/*      */ 
/*      */             
/*      */             public String toString() {
/* 1868 */               return function.toString();
/*      */             }
/*      */           }executor);
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
/*      */     @FunctionalInterface
/*      */     public static interface ClosingFunction4<V1, V2, V3, V4, U>
/*      */     {
/*      */       U apply(ClosingFuture.DeferredCloser param2DeferredCloser, V1 param2V1, V2 param2V2, V3 param2V3, V4 param2V4) throws Exception;
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
/*      */     @FunctionalInterface
/*      */     public static interface AsyncClosingFunction4<V1, V2, V3, V4, U>
/*      */     {
/*      */       ClosingFuture<U> apply(ClosingFuture.DeferredCloser param2DeferredCloser, V1 param2V1, V2 param2V2, V3 param2V3, V4 param2V4) throws Exception;
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
/*      */   public static final class Combiner5<V1, V2, V3, V4, V5>
/*      */     extends Combiner
/*      */   {
/*      */     private final ClosingFuture<V1> future1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ClosingFuture<V2> future2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ClosingFuture<V3> future3;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ClosingFuture<V4> future4;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ClosingFuture<V5> future5;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Combiner5(ClosingFuture<V1> future1, ClosingFuture<V2> future2, ClosingFuture<V3> future3, ClosingFuture<V4> future4, ClosingFuture<V5> future5) {
/* 1970 */       super(true, (Iterable)ImmutableList.of(future1, future2, future3, future4, future5));
/* 1971 */       this.future1 = future1;
/* 1972 */       this.future2 = future2;
/* 1973 */       this.future3 = future3;
/* 1974 */       this.future4 = future4;
/* 1975 */       this.future5 = future5;
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
/*      */ 
/*      */ 
/*      */     
/*      */     public <U> ClosingFuture<U> call(final ClosingFunction5<V1, V2, V3, V4, V5, U> function, Executor executor) {
/* 1994 */       return call(new ClosingFuture.Combiner.CombiningCallable<U>()
/*      */           {
/*      */             public U call(ClosingFuture.DeferredCloser closer, ClosingFuture.Peeker peeker) throws Exception
/*      */             {
/* 1998 */               return (U)function.apply(closer, peeker
/*      */                   
/* 2000 */                   .getDone(ClosingFuture.Combiner5.this.future1), peeker
/* 2001 */                   .getDone(ClosingFuture.Combiner5.this.future2), peeker
/* 2002 */                   .getDone(ClosingFuture.Combiner5.this.future3), peeker
/* 2003 */                   .getDone(ClosingFuture.Combiner5.this.future4), peeker
/* 2004 */                   .getDone(ClosingFuture.Combiner5.this.future5));
/*      */             }
/*      */ 
/*      */             
/*      */             public String toString() {
/* 2009 */               return function.toString();
/*      */             }
/*      */           }executor);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <U> ClosingFuture<U> callAsync(final AsyncClosingFunction5<V1, V2, V3, V4, V5, U> function, Executor executor) {
/* 2054 */       return callAsync(new ClosingFuture.Combiner.AsyncCombiningCallable<U>()
/*      */           {
/*      */             public ClosingFuture<U> call(ClosingFuture.DeferredCloser closer, ClosingFuture.Peeker peeker) throws Exception
/*      */             {
/* 2058 */               return function.apply(closer, peeker
/*      */                   
/* 2060 */                   .getDone(ClosingFuture.Combiner5.this.future1), peeker
/* 2061 */                   .getDone(ClosingFuture.Combiner5.this.future2), peeker
/* 2062 */                   .getDone(ClosingFuture.Combiner5.this.future3), peeker
/* 2063 */                   .getDone(ClosingFuture.Combiner5.this.future4), peeker
/* 2064 */                   .getDone(ClosingFuture.Combiner5.this.future5));
/*      */             }
/*      */ 
/*      */             
/*      */             public String toString() {
/* 2069 */               return function.toString();
/*      */             }
/*      */           }executor);
/*      */     } @FunctionalInterface
/*      */     public static interface ClosingFunction5<V1, V2, V3, V4, V5, U> {
/*      */       U apply(ClosingFuture.DeferredCloser param2DeferredCloser, V1 param2V1, V2 param2V2, V3 param2V3, V4 param2V4, V5 param2V5) throws Exception; }
/*      */     @FunctionalInterface
/*      */     public static interface AsyncClosingFunction5<V1, V2, V3, V4, V5, U> { ClosingFuture<U> apply(ClosingFuture.DeferredCloser param2DeferredCloser, V1 param2V1, V2 param2V2, V3 param2V3, V4 param2V4, V5 param2V5) throws Exception; }
/*      */   }
/*      */   public String toString() {
/* 2079 */     return MoreObjects.toStringHelper(this).add("state", this.state.get()).addValue(this.future).toString();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void finalize() {
/* 2084 */     if (((State)this.state.get()).equals(State.OPEN)) {
/* 2085 */       logger.log(Level.SEVERE, "Uh oh! An open ClosingFuture has leaked and will close: {0}", this);
/* 2086 */       FluentFuture<V> fluentFuture = finishToFuture();
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void closeQuietly(final AutoCloseable closeable, Executor executor) {
/* 2091 */     if (closeable == null) {
/*      */       return;
/*      */     }
/*      */     try {
/* 2095 */       executor.execute(new Runnable()
/*      */           {
/*      */             public void run()
/*      */             {
/*      */               try {
/* 2100 */                 closeable.close();
/* 2101 */               } catch (Exception e) {
/* 2102 */                 ClosingFuture.logger.log(Level.WARNING, "thrown by close()", e);
/*      */               } 
/*      */             }
/*      */           });
/* 2106 */     } catch (RejectedExecutionException e) {
/* 2107 */       if (logger.isLoggable(Level.WARNING)) {
/* 2108 */         logger.log(Level.WARNING, 
/* 2109 */             String.format("while submitting close to %s; will close inline", new Object[] { executor }), e);
/*      */       }
/* 2111 */       closeQuietly(closeable, MoreExecutors.directExecutor());
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkAndUpdateState(State oldState, State newState) {
/* 2116 */     Preconditions.checkState(
/* 2117 */         compareAndUpdateState(oldState, newState), "Expected state to be %s, but it was %s", oldState, newState);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean compareAndUpdateState(State oldState, State newState) {
/* 2124 */     return this.state.compareAndSet(oldState, newState);
/*      */   }
/*      */   
/*      */   private static final class CloseableList
/*      */     extends IdentityHashMap<AutoCloseable, Executor>
/*      */     implements Closeable {
/* 2130 */     private final ClosingFuture.DeferredCloser closer = new ClosingFuture.DeferredCloser(this);
/*      */     
/*      */     private volatile boolean closed;
/*      */     
/*      */     private volatile CountDownLatch whenClosed;
/*      */     
/*      */     <V, U> ListenableFuture<U> applyClosingFunction(ClosingFuture.ClosingFunction<? super V, U> transformation, V input) throws Exception {
/* 2137 */       CloseableList newCloseables = new CloseableList();
/*      */       try {
/* 2139 */         return (ListenableFuture)Futures.immediateFuture(transformation.apply(newCloseables.closer, input));
/*      */       } finally {
/* 2141 */         add(newCloseables, MoreExecutors.directExecutor());
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     <V, U> FluentFuture<U> applyAsyncClosingFunction(ClosingFuture.AsyncClosingFunction<V, U> transformation, V input) throws Exception {
/* 2148 */       CloseableList newCloseables = new CloseableList();
/*      */       try {
/* 2150 */         ClosingFuture<U> closingFuture = transformation.apply(newCloseables.closer, input);
/* 2151 */         closingFuture.becomeSubsumedInto(newCloseables);
/* 2152 */         return closingFuture.future;
/*      */       } finally {
/* 2154 */         add(newCloseables, MoreExecutors.directExecutor());
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void close() {
/* 2160 */       if (this.closed) {
/*      */         return;
/*      */       }
/* 2163 */       synchronized (this) {
/* 2164 */         if (this.closed) {
/*      */           return;
/*      */         }
/* 2167 */         this.closed = true;
/*      */       } 
/* 2169 */       for (Map.Entry<AutoCloseable, Executor> entry : entrySet()) {
/* 2170 */         ClosingFuture.closeQuietly(entry.getKey(), entry.getValue());
/*      */       }
/* 2172 */       clear();
/* 2173 */       if (this.whenClosed != null) {
/* 2174 */         this.whenClosed.countDown();
/*      */       }
/*      */     }
/*      */     
/*      */     void add(AutoCloseable closeable, Executor executor) {
/* 2179 */       Preconditions.checkNotNull(executor);
/* 2180 */       if (closeable == null) {
/*      */         return;
/*      */       }
/* 2183 */       synchronized (this) {
/* 2184 */         if (!this.closed) {
/* 2185 */           put(closeable, executor);
/*      */           return;
/*      */         } 
/*      */       } 
/* 2189 */       ClosingFuture.closeQuietly(closeable, executor);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     CountDownLatch whenClosedCountDown() {
/* 2196 */       if (this.closed) {
/* 2197 */         return new CountDownLatch(0);
/*      */       }
/* 2199 */       synchronized (this) {
/* 2200 */         if (this.closed) {
/* 2201 */           return new CountDownLatch(0);
/*      */         }
/* 2203 */         Preconditions.checkState((this.whenClosed == null));
/* 2204 */         return this.whenClosed = new CountDownLatch(1);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private CloseableList() {}
/*      */   }
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   CountDownLatch whenClosedCountDown() {
/* 2215 */     return this.closeables.whenClosedCountDown();
/*      */   }
/*      */ 
/*      */   
/*      */   enum State
/*      */   {
/* 2221 */     OPEN,
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2227 */     SUBSUMED,
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2233 */     WILL_CLOSE,
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2239 */     CLOSING,
/*      */ 
/*      */     
/* 2242 */     CLOSED,
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2248 */     WILL_CREATE_VALUE_AND_CLOSER;
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface ValueAndCloserConsumer<V> {
/*      */     void accept(ClosingFuture.ValueAndCloser<V> param1ValueAndCloser);
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface AsyncClosingFunction<T, U> {
/*      */     ClosingFuture<U> apply(ClosingFuture.DeferredCloser param1DeferredCloser, T param1T) throws Exception;
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface ClosingFunction<T, U> {
/*      */     U apply(ClosingFuture.DeferredCloser param1DeferredCloser, T param1T) throws Exception;
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface AsyncClosingCallable<V> {
/*      */     ClosingFuture<V> call(ClosingFuture.DeferredCloser param1DeferredCloser) throws Exception;
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface ClosingCallable<V> {
/*      */     V call(ClosingFuture.DeferredCloser param1DeferredCloser) throws Exception;
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\commo\\util\concurrent\ClosingFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */