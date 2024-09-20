/*      */ package com.google.common.util.concurrent;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.collect.ImmutableCollection;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.util.concurrent.internal.InternalFutureFailureAccess;
/*      */ import com.google.common.util.concurrent.internal.InternalFutures;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.time.Duration;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.ScheduledExecutorService;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.TimeoutException;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */ public final class Futures
/*      */   extends GwtFuturesCatchingSpecialization
/*      */ {
/*      */   public static <V> ListenableFuture<V> immediateFuture(V value) {
/*  131 */     if (value == null) {
/*      */ 
/*      */       
/*  134 */       ListenableFuture<V> typedNull = (ListenableFuture)ImmediateFuture.NULL;
/*  135 */       return typedNull;
/*      */     } 
/*  137 */     return new ImmediateFuture<>(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ListenableFuture<Void> immediateVoidFuture() {
/*  148 */     return (ListenableFuture)ImmediateFuture.NULL;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> ListenableFuture<V> immediateFailedFuture(Throwable throwable) {
/*  159 */     Preconditions.checkNotNull(throwable);
/*  160 */     return new ImmediateFuture.ImmediateFailedFuture<>(throwable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> ListenableFuture<V> immediateCancelledFuture() {
/*  170 */     return new ImmediateFuture.ImmediateCancelledFuture<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <O> ListenableFuture<O> submit(Callable<O> callable, Executor executor) {
/*  181 */     TrustedListenableFutureTask<O> task = TrustedListenableFutureTask.create(callable);
/*  182 */     executor.execute(task);
/*  183 */     return task;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static ListenableFuture<Void> submit(Runnable runnable, Executor executor) {
/*  195 */     TrustedListenableFutureTask<Void> task = TrustedListenableFutureTask.create(runnable, (Void)null);
/*  196 */     executor.execute(task);
/*  197 */     return task;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <O> ListenableFuture<O> submitAsync(AsyncCallable<O> callable, Executor executor) {
/*  208 */     TrustedListenableFutureTask<O> task = TrustedListenableFutureTask.create(callable);
/*  209 */     executor.execute(task);
/*  210 */     return task;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static <O> ListenableFuture<O> scheduleAsync(AsyncCallable<O> callable, Duration delay, ScheduledExecutorService executorService) {
/*  223 */     return scheduleAsync(callable, Internal.toNanosSaturated(delay), TimeUnit.NANOSECONDS, executorService);
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
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static <O> ListenableFuture<O> scheduleAsync(AsyncCallable<O> callable, long delay, TimeUnit timeUnit, ScheduledExecutorService executorService) {
/*  240 */     TrustedListenableFutureTask<O> task = TrustedListenableFutureTask.create(callable);
/*  241 */     final Future<?> scheduled = executorService.schedule(task, delay, timeUnit);
/*  242 */     task.addListener(new Runnable()
/*      */         {
/*      */           
/*      */           public void run()
/*      */           {
/*  247 */             scheduled.cancel(false);
/*      */           }
/*      */         }, 
/*  250 */         MoreExecutors.directExecutor());
/*  251 */     return task;
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
/*      */   @Beta
/*      */   @GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
/*      */   public static <V, X extends Throwable> ListenableFuture<V> catching(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback, Executor executor) {
/*  297 */     return AbstractCatchingFuture.create(input, exceptionType, fallback, executor);
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
/*      */   @Beta
/*      */   @GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
/*      */   public static <V, X extends Throwable> ListenableFuture<V> catchingAsync(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback, Executor executor) {
/*  362 */     return AbstractCatchingFuture.create(input, exceptionType, fallback, executor);
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
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static <V> ListenableFuture<V> withTimeout(ListenableFuture<V> delegate, Duration time, ScheduledExecutorService scheduledExecutor) {
/*  380 */     return withTimeout(delegate, Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS, scheduledExecutor);
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
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static <V> ListenableFuture<V> withTimeout(ListenableFuture<V> delegate, long time, TimeUnit unit, ScheduledExecutorService scheduledExecutor) {
/*  403 */     if (delegate.isDone()) {
/*  404 */       return delegate;
/*      */     }
/*  406 */     return TimeoutFuture.create(delegate, time, unit, scheduledExecutor);
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
/*      */   @Beta
/*      */   public static <I, O> ListenableFuture<O> transformAsync(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor) {
/*  446 */     return AbstractTransformFuture.create(input, function, executor);
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
/*      */   @Beta
/*      */   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, Function<? super I, ? extends O> function, Executor executor) {
/*  481 */     return AbstractTransformFuture.create(input, function, executor);
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
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static <I, O> Future<O> lazyTransform(final Future<I> input, final Function<? super I, ? extends O> function) {
/*  508 */     Preconditions.checkNotNull(input);
/*  509 */     Preconditions.checkNotNull(function);
/*  510 */     return new Future<O>()
/*      */       {
/*      */         public boolean cancel(boolean mayInterruptIfRunning)
/*      */         {
/*  514 */           return input.cancel(mayInterruptIfRunning);
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isCancelled() {
/*  519 */           return input.isCancelled();
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isDone() {
/*  524 */           return input.isDone();
/*      */         }
/*      */ 
/*      */         
/*      */         public O get() throws InterruptedException, ExecutionException {
/*  529 */           return applyTransformation(input.get());
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         public O get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*  535 */           return applyTransformation(input.get(timeout, unit));
/*      */         }
/*      */         
/*      */         private O applyTransformation(I input) throws ExecutionException {
/*      */           try {
/*  540 */             return (O)function.apply(input);
/*  541 */           } catch (Throwable t) {
/*  542 */             throw new ExecutionException(t);
/*      */           } 
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
/*      */   @SafeVarargs
/*      */   @Beta
/*      */   public static <V> ListenableFuture<List<V>> allAsList(ListenableFuture<? extends V>... futures) {
/*  567 */     return new CollectionFuture.ListFuture<>((ImmutableCollection<? extends ListenableFuture<? extends V>>)ImmutableList.copyOf((Object[])futures), true);
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
/*      */   @Beta
/*      */   public static <V> ListenableFuture<List<V>> allAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
/*  589 */     return new CollectionFuture.ListFuture<>((ImmutableCollection<? extends ListenableFuture<? extends V>>)ImmutableList.copyOf(futures), true);
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
/*      */   @SafeVarargs
/*      */   @Beta
/*      */   public static <V> FutureCombiner<V> whenAllComplete(ListenableFuture<? extends V>... futures) {
/*  603 */     return new FutureCombiner<>(false, ImmutableList.copyOf((Object[])futures));
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
/*      */   @Beta
/*      */   public static <V> FutureCombiner<V> whenAllComplete(Iterable<? extends ListenableFuture<? extends V>> futures) {
/*  617 */     return new FutureCombiner<>(false, ImmutableList.copyOf(futures));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @SafeVarargs
/*      */   @Beta
/*      */   public static <V> FutureCombiner<V> whenAllSucceed(ListenableFuture<? extends V>... futures) {
/*  630 */     return new FutureCombiner<>(true, ImmutableList.copyOf((Object[])futures));
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
/*      */   @Beta
/*      */   public static <V> FutureCombiner<V> whenAllSucceed(Iterable<? extends ListenableFuture<? extends V>> futures) {
/*  643 */     return new FutureCombiner<>(true, ImmutableList.copyOf(futures));
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
/*      */   @Beta
/*      */   @CanIgnoreReturnValue
/*      */   @GwtCompatible
/*      */   public static final class FutureCombiner<V>
/*      */   {
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
/*      */     private final ImmutableList<ListenableFuture<? extends V>> futures;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private FutureCombiner(boolean allMustSucceed, ImmutableList<ListenableFuture<? extends V>> futures) {
/*  681 */       this.allMustSucceed = allMustSucceed;
/*  682 */       this.futures = futures;
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
/*      */     public <C> ListenableFuture<C> callAsync(AsyncCallable<C> combiner, Executor executor) {
/*  700 */       return new CombinedFuture<>((ImmutableCollection<? extends ListenableFuture<?>>)this.futures, this.allMustSucceed, executor, combiner);
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
/*      */     @CanIgnoreReturnValue
/*      */     public <C> ListenableFuture<C> call(Callable<C> combiner, Executor executor) {
/*  719 */       return new CombinedFuture<>((ImmutableCollection<? extends ListenableFuture<?>>)this.futures, this.allMustSucceed, executor, combiner);
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
/*      */     public ListenableFuture<?> run(final Runnable combiner, Executor executor) {
/*  734 */       return call(new Callable<Void>(this)
/*      */           {
/*      */             public Void call() throws Exception
/*      */             {
/*  738 */               combiner.run();
/*  739 */               return null;
/*      */             }
/*      */           }executor);
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
/*      */   @Beta
/*      */   public static <V> ListenableFuture<V> nonCancellationPropagating(ListenableFuture<V> future) {
/*  755 */     if (future.isDone()) {
/*  756 */       return future;
/*      */     }
/*  758 */     NonCancellationPropagatingFuture<V> output = new NonCancellationPropagatingFuture<>(future);
/*  759 */     future.addListener(output, MoreExecutors.directExecutor());
/*  760 */     return output;
/*      */   }
/*      */   
/*      */   private static final class NonCancellationPropagatingFuture<V>
/*      */     extends AbstractFuture.TrustedFuture<V>
/*      */     implements Runnable {
/*      */     private ListenableFuture<V> delegate;
/*      */     
/*      */     NonCancellationPropagatingFuture(ListenableFuture<V> delegate) {
/*  769 */       this.delegate = delegate;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void run() {
/*  776 */       ListenableFuture<V> localDelegate = this.delegate;
/*  777 */       if (localDelegate != null) {
/*  778 */         setFuture(localDelegate);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     protected String pendingToString() {
/*  784 */       ListenableFuture<V> localDelegate = this.delegate;
/*  785 */       if (localDelegate != null) {
/*  786 */         String str = String.valueOf(localDelegate); return (new StringBuilder(11 + String.valueOf(str).length())).append("delegate=[").append(str).append("]").toString();
/*      */       } 
/*  788 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void afterDone() {
/*  793 */       this.delegate = null;
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
/*      */   @SafeVarargs
/*      */   @Beta
/*      */   public static <V> ListenableFuture<List<V>> successfulAsList(ListenableFuture<? extends V>... futures) {
/*  819 */     return new CollectionFuture.ListFuture<>((ImmutableCollection<? extends ListenableFuture<? extends V>>)ImmutableList.copyOf((Object[])futures), false);
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
/*      */   @Beta
/*      */   public static <V> ListenableFuture<List<V>> successfulAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
/*  843 */     return new CollectionFuture.ListFuture<>((ImmutableCollection<? extends ListenableFuture<? extends V>>)ImmutableList.copyOf(futures), false);
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
/*      */   @Beta
/*      */   public static <T> ImmutableList<ListenableFuture<T>> inCompletionOrder(Iterable<? extends ListenableFuture<? extends T>> futures) {
/*      */     ImmutableList immutableList;
/*  872 */     if (futures instanceof Collection) {
/*  873 */       Collection<ListenableFuture<? extends T>> collection = (Collection)futures;
/*      */     } else {
/*  875 */       immutableList = ImmutableList.copyOf(futures);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  880 */     ListenableFuture[] arrayOfListenableFuture = (ListenableFuture[])immutableList.toArray((Object[])new ListenableFuture[immutableList.size()]);
/*  881 */     final InCompletionOrderState<T> state = new InCompletionOrderState<>(arrayOfListenableFuture);
/*  882 */     ImmutableList.Builder<AbstractFuture<T>> delegatesBuilder = ImmutableList.builder();
/*  883 */     for (int i = 0; i < arrayOfListenableFuture.length; i++) {
/*  884 */       delegatesBuilder.add(new InCompletionOrderFuture(state));
/*      */     }
/*      */     
/*  887 */     final ImmutableList<AbstractFuture<T>> delegates = delegatesBuilder.build();
/*  888 */     for (int j = 0; j < arrayOfListenableFuture.length; j++) {
/*  889 */       final int localI = j;
/*  890 */       arrayOfListenableFuture[j].addListener(new Runnable()
/*      */           {
/*      */             public void run()
/*      */             {
/*  894 */               state.recordInputCompletion(delegates, localI);
/*      */             }
/*  897 */           }MoreExecutors.directExecutor());
/*      */     } 
/*      */ 
/*      */     
/*  901 */     return (ImmutableList)delegates;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class InCompletionOrderFuture<T>
/*      */     extends AbstractFuture<T>
/*      */   {
/*      */     private Futures.InCompletionOrderState<T> state;
/*      */ 
/*      */     
/*      */     private InCompletionOrderFuture(Futures.InCompletionOrderState<T> state) {
/*  912 */       this.state = state;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean cancel(boolean interruptIfRunning) {
/*  917 */       Futures.InCompletionOrderState<T> localState = this.state;
/*  918 */       if (super.cancel(interruptIfRunning)) {
/*  919 */         localState.recordOutputCancellation(interruptIfRunning);
/*  920 */         return true;
/*      */       } 
/*  922 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void afterDone() {
/*  927 */       this.state = null;
/*      */     }
/*      */ 
/*      */     
/*      */     protected String pendingToString() {
/*  932 */       Futures.InCompletionOrderState<T> localState = this.state;
/*  933 */       if (localState != null) {
/*      */ 
/*      */ 
/*      */         
/*  937 */         int i = localState.inputFutures.length;
/*      */         
/*  939 */         int j = localState.incompleteOutputCount.get(); return (new StringBuilder(49)).append("inputCount=[").append(i).append("], remaining=[").append(j).append("]").toString();
/*      */       } 
/*      */       
/*  942 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class InCompletionOrderState<T>
/*      */   {
/*      */     private boolean wasCancelled = false;
/*      */     
/*      */     private boolean shouldInterrupt = true;
/*      */     private final AtomicInteger incompleteOutputCount;
/*      */     private final ListenableFuture<? extends T>[] inputFutures;
/*  954 */     private volatile int delegateIndex = 0;
/*      */     
/*      */     private InCompletionOrderState(ListenableFuture<? extends T>[] inputFutures) {
/*  957 */       this.inputFutures = inputFutures;
/*  958 */       this.incompleteOutputCount = new AtomicInteger(inputFutures.length);
/*      */     }
/*      */     
/*      */     private void recordOutputCancellation(boolean interruptIfRunning) {
/*  962 */       this.wasCancelled = true;
/*      */ 
/*      */       
/*  965 */       if (!interruptIfRunning) {
/*  966 */         this.shouldInterrupt = false;
/*      */       }
/*  968 */       recordCompletion();
/*      */     }
/*      */ 
/*      */     
/*      */     private void recordInputCompletion(ImmutableList<AbstractFuture<T>> delegates, int inputFutureIndex) {
/*  973 */       ListenableFuture<? extends T> inputFuture = this.inputFutures[inputFutureIndex];
/*      */       
/*  975 */       this.inputFutures[inputFutureIndex] = null;
/*  976 */       for (int i = this.delegateIndex; i < delegates.size(); i++) {
/*  977 */         if (((AbstractFuture<T>)delegates.get(i)).setFuture(inputFuture)) {
/*  978 */           recordCompletion();
/*      */           
/*  980 */           this.delegateIndex = i + 1;
/*      */ 
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*      */       
/*  987 */       this.delegateIndex = delegates.size();
/*      */     }
/*      */     
/*      */     private void recordCompletion() {
/*  991 */       if (this.incompleteOutputCount.decrementAndGet() == 0 && this.wasCancelled) {
/*  992 */         for (ListenableFuture<?> toCancel : this.inputFutures) {
/*  993 */           if (toCancel != null) {
/*  994 */             toCancel.cancel(this.shouldInterrupt);
/*      */           }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> void addCallback(ListenableFuture<V> future, FutureCallback<? super V> callback, Executor executor) {
/* 1046 */     Preconditions.checkNotNull(callback);
/* 1047 */     future.addListener(new CallbackListener<>(future, callback), executor);
/*      */   }
/*      */   
/*      */   private static final class CallbackListener<V>
/*      */     implements Runnable {
/*      */     final Future<V> future;
/*      */     final FutureCallback<? super V> callback;
/*      */     
/*      */     CallbackListener(Future<V> future, FutureCallback<? super V> callback) {
/* 1056 */       this.future = future;
/* 1057 */       this.callback = callback;
/*      */     }
/*      */     
/*      */     public void run() {
/*      */       V value;
/* 1062 */       if (this.future instanceof InternalFutureFailureAccess) {
/*      */         
/* 1064 */         Throwable failure = InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess)this.future);
/* 1065 */         if (failure != null) {
/* 1066 */           this.callback.onFailure(failure);
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*      */       try {
/* 1072 */         value = Futures.getDone(this.future);
/* 1073 */       } catch (ExecutionException e) {
/* 1074 */         this.callback.onFailure(e.getCause());
/*      */         return;
/* 1076 */       } catch (RuntimeException|Error e) {
/* 1077 */         this.callback.onFailure(e);
/*      */         return;
/*      */       } 
/* 1080 */       this.callback.onSuccess(value);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1085 */       return MoreObjects.toStringHelper(this).addValue(this.callback).toString();
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
/*      */   @CanIgnoreReturnValue
/*      */   public static <V> V getDone(Future<V> future) throws ExecutionException {
/* 1122 */     Preconditions.checkState(future.isDone(), "Future was expected to be done: %s", future);
/* 1123 */     return Uninterruptibles.getUninterruptibly(future);
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
/*      */   @Beta
/*      */   @CanIgnoreReturnValue
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass) throws X {
/* 1173 */     return FuturesGetChecked.getChecked(future, exceptionClass);
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
/*      */   @Beta
/*      */   @CanIgnoreReturnValue
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass, Duration timeout) throws X {
/* 1224 */     return getChecked(future, exceptionClass, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
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
/*      */   @Beta
/*      */   @CanIgnoreReturnValue
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass, long timeout, TimeUnit unit) throws X {
/* 1276 */     return FuturesGetChecked.getChecked(future, exceptionClass, timeout, unit);
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
/*      */   @CanIgnoreReturnValue
/*      */   public static <V> V getUnchecked(Future<V> future) {
/* 1315 */     Preconditions.checkNotNull(future);
/*      */     try {
/* 1317 */       return Uninterruptibles.getUninterruptibly(future);
/* 1318 */     } catch (ExecutionException e) {
/* 1319 */       wrapAndThrowUnchecked(e.getCause());
/* 1320 */       throw new AssertionError();
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void wrapAndThrowUnchecked(Throwable cause) {
/* 1325 */     if (cause instanceof Error) {
/* 1326 */       throw new ExecutionError((Error)cause);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1333 */     throw new UncheckedExecutionException(cause);
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\commo\\util\concurrent\Futures.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */