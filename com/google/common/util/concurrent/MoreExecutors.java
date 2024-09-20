/*      */ package com.google.common.util.concurrent;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Supplier;
/*      */ import com.google.common.base.Throwables;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Queues;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.time.Duration;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.concurrent.BlockingQueue;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.Delayed;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.ExecutorService;
/*      */ import java.util.concurrent.Executors;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.RejectedExecutionException;
/*      */ import java.util.concurrent.ScheduledExecutorService;
/*      */ import java.util.concurrent.ScheduledFuture;
/*      */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*      */ import java.util.concurrent.ThreadFactory;
/*      */ import java.util.concurrent.ThreadPoolExecutor;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.TimeoutException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */ public final class MoreExecutors
/*      */ {
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static ExecutorService getExitingExecutorService(ThreadPoolExecutor executor, Duration terminationTimeout) {
/*   85 */     return getExitingExecutorService(executor, 
/*   86 */         Internal.toNanosSaturated(terminationTimeout), TimeUnit.NANOSECONDS);
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
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static ExecutorService getExitingExecutorService(ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
/*  107 */     return (new Application()).getExitingExecutorService(executor, terminationTimeout, timeUnit);
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
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static ExecutorService getExitingExecutorService(ThreadPoolExecutor executor) {
/*  126 */     return (new Application()).getExitingExecutorService(executor);
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
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor, Duration terminationTimeout) {
/*  146 */     return getExitingScheduledExecutorService(executor, 
/*  147 */         Internal.toNanosSaturated(terminationTimeout), TimeUnit.NANOSECONDS);
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
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
/*  168 */     return (new Application())
/*  169 */       .getExitingScheduledExecutorService(executor, terminationTimeout, timeUnit);
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
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor) {
/*  189 */     return (new Application()).getExitingScheduledExecutorService(executor);
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
/*      */   public static void addDelayedShutdownHook(ExecutorService service, Duration terminationTimeout) {
/*  206 */     addDelayedShutdownHook(service, Internal.toNanosSaturated(terminationTimeout), TimeUnit.NANOSECONDS);
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
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static void addDelayedShutdownHook(ExecutorService service, long terminationTimeout, TimeUnit timeUnit) {
/*  225 */     (new Application()).addDelayedShutdownHook(service, terminationTimeout, timeUnit);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   @VisibleForTesting
/*      */   static class Application
/*      */   {
/*      */     final ExecutorService getExitingExecutorService(ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
/*  235 */       MoreExecutors.useDaemonThreadFactory(executor);
/*  236 */       ExecutorService service = Executors.unconfigurableExecutorService(executor);
/*  237 */       addDelayedShutdownHook(executor, terminationTimeout, timeUnit);
/*  238 */       return service;
/*      */     }
/*      */     
/*      */     final ExecutorService getExitingExecutorService(ThreadPoolExecutor executor) {
/*  242 */       return getExitingExecutorService(executor, 120L, TimeUnit.SECONDS);
/*      */     }
/*      */ 
/*      */     
/*      */     final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
/*  247 */       MoreExecutors.useDaemonThreadFactory(executor);
/*  248 */       ScheduledExecutorService service = Executors.unconfigurableScheduledExecutorService(executor);
/*  249 */       addDelayedShutdownHook(executor, terminationTimeout, timeUnit);
/*  250 */       return service;
/*      */     }
/*      */ 
/*      */     
/*      */     final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor) {
/*  255 */       return getExitingScheduledExecutorService(executor, 120L, TimeUnit.SECONDS);
/*      */     }
/*      */ 
/*      */     
/*      */     final void addDelayedShutdownHook(final ExecutorService service, final long terminationTimeout, final TimeUnit timeUnit) {
/*  260 */       Preconditions.checkNotNull(service);
/*  261 */       Preconditions.checkNotNull(timeUnit);
/*  262 */       String str = String.valueOf(service); addShutdownHook(
/*  263 */           MoreExecutors.newThread((new StringBuilder(24 + String.valueOf(str).length())).append("DelayedShutdownHook-for-").append(str).toString(), new Runnable(this)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               public void run()
/*      */               {
/*      */                 try {
/*  274 */                   service.shutdown();
/*  275 */                   service.awaitTermination(terminationTimeout, timeUnit);
/*  276 */                 } catch (InterruptedException interruptedException) {}
/*      */               }
/*      */             }));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @VisibleForTesting
/*      */     void addShutdownHook(Thread hook) {
/*  285 */       Runtime.getRuntime().addShutdownHook(hook);
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   private static void useDaemonThreadFactory(ThreadPoolExecutor executor) {
/*  291 */     executor.setThreadFactory((new ThreadFactoryBuilder())
/*      */         
/*  293 */         .setDaemon(true)
/*  294 */         .setThreadFactory(executor.getThreadFactory())
/*  295 */         .build());
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   private static final class DirectExecutorService
/*      */     extends AbstractListeningExecutorService
/*      */   {
/*  302 */     private final Object lock = new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("lock")
/*  311 */     private int runningTasks = 0;
/*      */ 
/*      */     
/*      */     @GuardedBy("lock")
/*      */     private boolean shutdown = false;
/*      */ 
/*      */     
/*      */     public void execute(Runnable command) {
/*  319 */       startTask();
/*      */       try {
/*  321 */         command.run();
/*      */       } finally {
/*  323 */         endTask();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isShutdown() {
/*  329 */       synchronized (this.lock) {
/*  330 */         return this.shutdown;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void shutdown() {
/*  336 */       synchronized (this.lock) {
/*  337 */         this.shutdown = true;
/*  338 */         if (this.runningTasks == 0) {
/*  339 */           this.lock.notifyAll();
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public List<Runnable> shutdownNow() {
/*  347 */       shutdown();
/*  348 */       return Collections.emptyList();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isTerminated() {
/*  353 */       synchronized (this.lock) {
/*  354 */         return (this.shutdown && this.runningTasks == 0);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
/*  360 */       long nanos = unit.toNanos(timeout);
/*  361 */       synchronized (this.lock) {
/*      */         while (true) {
/*  363 */           if (this.shutdown && this.runningTasks == 0)
/*  364 */             return true; 
/*  365 */           if (nanos <= 0L) {
/*  366 */             return false;
/*      */           }
/*  368 */           long now = System.nanoTime();
/*  369 */           TimeUnit.NANOSECONDS.timedWait(this.lock, nanos);
/*  370 */           nanos -= System.nanoTime() - now;
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
/*      */     private void startTask() {
/*  382 */       synchronized (this.lock) {
/*  383 */         if (this.shutdown) {
/*  384 */           throw new RejectedExecutionException("Executor already shutdown");
/*      */         }
/*  386 */         this.runningTasks++;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private void endTask() {
/*  392 */       synchronized (this.lock) {
/*  393 */         int numRunning = --this.runningTasks;
/*  394 */         if (numRunning == 0) {
/*  395 */           this.lock.notifyAll();
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
/*      */     private DirectExecutorService() {}
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
/*      */   @GwtIncompatible
/*      */   public static ListeningExecutorService newDirectExecutorService() {
/*  429 */     return new DirectExecutorService();
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
/*      */   public static Executor directExecutor() {
/*  479 */     return DirectExecutor.INSTANCE;
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
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static Executor newSequentialExecutor(Executor delegate) {
/*  526 */     return new SequentialExecutor(delegate);
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
/*      */   @GwtIncompatible
/*      */   public static ListeningExecutorService listeningDecorator(ExecutorService delegate) {
/*  546 */     return (delegate instanceof ListeningExecutorService) ? 
/*  547 */       (ListeningExecutorService)delegate : (
/*  548 */       (delegate instanceof ScheduledExecutorService) ? 
/*  549 */       new ScheduledListeningDecorator((ScheduledExecutorService)delegate) : 
/*  550 */       new ListeningDecorator(delegate));
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
/*      */   public static ListeningScheduledExecutorService listeningDecorator(ScheduledExecutorService delegate) {
/*  572 */     return (delegate instanceof ListeningScheduledExecutorService) ? 
/*  573 */       (ListeningScheduledExecutorService)delegate : 
/*  574 */       new ScheduledListeningDecorator(delegate);
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   private static class ListeningDecorator extends AbstractListeningExecutorService {
/*      */     private final ExecutorService delegate;
/*      */     
/*      */     ListeningDecorator(ExecutorService delegate) {
/*  582 */       this.delegate = (ExecutorService)Preconditions.checkNotNull(delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     public final boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
/*  587 */       return this.delegate.awaitTermination(timeout, unit);
/*      */     }
/*      */ 
/*      */     
/*      */     public final boolean isShutdown() {
/*  592 */       return this.delegate.isShutdown();
/*      */     }
/*      */ 
/*      */     
/*      */     public final boolean isTerminated() {
/*  597 */       return this.delegate.isTerminated();
/*      */     }
/*      */ 
/*      */     
/*      */     public final void shutdown() {
/*  602 */       this.delegate.shutdown();
/*      */     }
/*      */ 
/*      */     
/*      */     public final List<Runnable> shutdownNow() {
/*  607 */       return this.delegate.shutdownNow();
/*      */     }
/*      */ 
/*      */     
/*      */     public final void execute(Runnable command) {
/*  612 */       this.delegate.execute(command);
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   private static final class ScheduledListeningDecorator
/*      */     extends ListeningDecorator
/*      */     implements ListeningScheduledExecutorService {
/*      */     final ScheduledExecutorService delegate;
/*      */     
/*      */     ScheduledListeningDecorator(ScheduledExecutorService delegate) {
/*  623 */       super(delegate);
/*  624 */       this.delegate = (ScheduledExecutorService)Preconditions.checkNotNull(delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     public ListenableScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
/*  629 */       TrustedListenableFutureTask<Void> task = TrustedListenableFutureTask.create(command, null);
/*  630 */       ScheduledFuture<?> scheduled = this.delegate.schedule(task, delay, unit);
/*  631 */       return new ListenableScheduledTask(task, scheduled);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public <V> ListenableScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
/*  637 */       TrustedListenableFutureTask<V> task = TrustedListenableFutureTask.create(callable);
/*  638 */       ScheduledFuture<?> scheduled = this.delegate.schedule(task, delay, unit);
/*  639 */       return new ListenableScheduledTask<>(task, scheduled);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public ListenableScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
/*  645 */       NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
/*  646 */       ScheduledFuture<?> scheduled = this.delegate.scheduleAtFixedRate(task, initialDelay, period, unit);
/*  647 */       return new ListenableScheduledTask(task, scheduled);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public ListenableScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
/*  653 */       NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
/*      */       
/*  655 */       ScheduledFuture<?> scheduled = this.delegate.scheduleWithFixedDelay(task, initialDelay, delay, unit);
/*  656 */       return new ListenableScheduledTask(task, scheduled);
/*      */     }
/*      */     
/*      */     private static final class ListenableScheduledTask<V>
/*      */       extends ForwardingListenableFuture.SimpleForwardingListenableFuture<V>
/*      */       implements ListenableScheduledFuture<V>
/*      */     {
/*      */       private final ScheduledFuture<?> scheduledDelegate;
/*      */       
/*      */       public ListenableScheduledTask(ListenableFuture<V> listenableDelegate, ScheduledFuture<?> scheduledDelegate) {
/*  666 */         super(listenableDelegate);
/*  667 */         this.scheduledDelegate = scheduledDelegate;
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean cancel(boolean mayInterruptIfRunning) {
/*  672 */         boolean cancelled = super.cancel(mayInterruptIfRunning);
/*  673 */         if (cancelled)
/*      */         {
/*  675 */           this.scheduledDelegate.cancel(mayInterruptIfRunning);
/*      */         }
/*      */ 
/*      */         
/*  679 */         return cancelled;
/*      */       }
/*      */ 
/*      */       
/*      */       public long getDelay(TimeUnit unit) {
/*  684 */         return this.scheduledDelegate.getDelay(unit);
/*      */       }
/*      */ 
/*      */       
/*      */       public int compareTo(Delayed other) {
/*  689 */         return this.scheduledDelegate.compareTo(other);
/*      */       }
/*      */     }
/*      */     
/*      */     @GwtIncompatible
/*      */     private static final class NeverSuccessfulListenableFutureTask
/*      */       extends AbstractFuture.TrustedFuture<Void> implements Runnable {
/*      */       private final Runnable delegate;
/*      */       
/*      */       public NeverSuccessfulListenableFutureTask(Runnable delegate) {
/*  699 */         this.delegate = (Runnable)Preconditions.checkNotNull(delegate);
/*      */       }
/*      */ 
/*      */       
/*      */       public void run() {
/*      */         try {
/*  705 */           this.delegate.run();
/*  706 */         } catch (Throwable t) {
/*  707 */           setException(t);
/*  708 */           throw Throwables.propagate(t);
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
/*      */   @GwtIncompatible
/*      */   static <T> T invokeAnyImpl(ListeningExecutorService executorService, Collection<? extends Callable<T>> tasks, boolean timed, Duration timeout) throws InterruptedException, ExecutionException, TimeoutException {
/*  735 */     return invokeAnyImpl(executorService, tasks, timed, 
/*  736 */         Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
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
/*      */   static <T> T invokeAnyImpl(ListeningExecutorService executorService, Collection<? extends Callable<T>> tasks, boolean timed, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*  751 */     Preconditions.checkNotNull(executorService);
/*  752 */     Preconditions.checkNotNull(unit);
/*  753 */     int ntasks = tasks.size();
/*  754 */     Preconditions.checkArgument((ntasks > 0));
/*  755 */     List<Future<T>> futures = Lists.newArrayListWithCapacity(ntasks);
/*  756 */     BlockingQueue<Future<T>> futureQueue = Queues.newLinkedBlockingQueue();
/*  757 */     long timeoutNanos = unit.toNanos(timeout);
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
/*      */   @GwtIncompatible
/*      */   private static <T> ListenableFuture<T> submitAndAddQueueListener(ListeningExecutorService executorService, Callable<T> task, final BlockingQueue<Future<T>> queue) {
/*  828 */     final ListenableFuture<T> future = executorService.submit(task);
/*  829 */     future.addListener(new Runnable()
/*      */         {
/*      */           public void run()
/*      */           {
/*  833 */             queue.add(future);
/*      */           }
/*      */         }, 
/*  836 */         directExecutor());
/*  837 */     return future;
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
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static ThreadFactory platformThreadFactory() {
/*  853 */     if (!isAppEngineWithApiClasses()) {
/*  854 */       return Executors.defaultThreadFactory();
/*      */     }
/*      */     try {
/*  857 */       return 
/*  858 */         (ThreadFactory)Class.forName("com.google.appengine.api.ThreadManager")
/*  859 */         .getMethod("currentRequestThreadFactory", new Class[0])
/*  860 */         .invoke(null, new Object[0]);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  866 */     catch (IllegalAccessException e) {
/*  867 */       throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
/*  868 */     } catch (ClassNotFoundException e) {
/*  869 */       throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
/*  870 */     } catch (NoSuchMethodException e) {
/*  871 */       throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
/*  872 */     } catch (InvocationTargetException e) {
/*  873 */       throw Throwables.propagate(e.getCause());
/*      */     } 
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   private static boolean isAppEngineWithApiClasses() {
/*  879 */     if (System.getProperty("com.google.appengine.runtime.environment") == null) {
/*  880 */       return false;
/*      */     }
/*      */     try {
/*  883 */       Class.forName("com.google.appengine.api.utils.SystemProperty");
/*  884 */     } catch (ClassNotFoundException e) {
/*  885 */       return false;
/*      */     } 
/*      */     
/*      */     try {
/*  889 */       return 
/*      */         
/*  891 */         (Class.forName("com.google.apphosting.api.ApiProxy").getMethod("getCurrentEnvironment", new Class[0]).invoke(null, new Object[0]) != null);
/*      */     }
/*  893 */     catch (ClassNotFoundException e) {
/*      */       
/*  895 */       return false;
/*  896 */     } catch (InvocationTargetException e) {
/*      */       
/*  898 */       return false;
/*  899 */     } catch (IllegalAccessException e) {
/*      */       
/*  901 */       return false;
/*  902 */     } catch (NoSuchMethodException e) {
/*      */       
/*  904 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   static Thread newThread(String name, Runnable runnable) {
/*  914 */     Preconditions.checkNotNull(name);
/*  915 */     Preconditions.checkNotNull(runnable);
/*  916 */     Thread result = platformThreadFactory().newThread(runnable);
/*      */     try {
/*  918 */       result.setName(name);
/*  919 */     } catch (SecurityException securityException) {}
/*      */ 
/*      */     
/*  922 */     return result;
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
/*      */   @GwtIncompatible
/*      */   static Executor renamingDecorator(final Executor executor, final Supplier<String> nameSupplier) {
/*  942 */     Preconditions.checkNotNull(executor);
/*  943 */     Preconditions.checkNotNull(nameSupplier);
/*  944 */     return new Executor()
/*      */       {
/*      */         public void execute(Runnable command) {
/*  947 */           executor.execute(Callables.threadRenaming(command, nameSupplier));
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
/*      */   @GwtIncompatible
/*      */   static ExecutorService renamingDecorator(ExecutorService service, final Supplier<String> nameSupplier) {
/*  967 */     Preconditions.checkNotNull(service);
/*  968 */     Preconditions.checkNotNull(nameSupplier);
/*  969 */     return new WrappingExecutorService(service)
/*      */       {
/*      */         protected <T> Callable<T> wrapTask(Callable<T> callable) {
/*  972 */           return Callables.threadRenaming(callable, nameSupplier);
/*      */         }
/*      */ 
/*      */         
/*      */         protected Runnable wrapTask(Runnable command) {
/*  977 */           return Callables.threadRenaming(command, nameSupplier);
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
/*      */   @GwtIncompatible
/*      */   static ScheduledExecutorService renamingDecorator(ScheduledExecutorService service, final Supplier<String> nameSupplier) {
/*  997 */     Preconditions.checkNotNull(service);
/*  998 */     Preconditions.checkNotNull(nameSupplier);
/*  999 */     return new WrappingScheduledExecutorService(service)
/*      */       {
/*      */         protected <T> Callable<T> wrapTask(Callable<T> callable) {
/* 1002 */           return Callables.threadRenaming(callable, nameSupplier);
/*      */         }
/*      */ 
/*      */         
/*      */         protected Runnable wrapTask(Runnable command) {
/* 1007 */           return Callables.threadRenaming(command, nameSupplier);
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
/*      */   @Beta
/*      */   @CanIgnoreReturnValue
/*      */   @GwtIncompatible
/*      */   public static boolean shutdownAndAwaitTermination(ExecutorService service, Duration timeout) {
/* 1039 */     return shutdownAndAwaitTermination(service, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
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
/*      */   @Beta
/*      */   @CanIgnoreReturnValue
/*      */   @GwtIncompatible
/*      */   public static boolean shutdownAndAwaitTermination(ExecutorService service, long timeout, TimeUnit unit) {
/* 1072 */     long halfTimeoutNanos = unit.toNanos(timeout) / 2L;
/*      */     
/* 1074 */     service.shutdown();
/*      */     
/*      */     try {
/* 1077 */       if (!service.awaitTermination(halfTimeoutNanos, TimeUnit.NANOSECONDS)) {
/*      */         
/* 1079 */         service.shutdownNow();
/*      */         
/* 1081 */         service.awaitTermination(halfTimeoutNanos, TimeUnit.NANOSECONDS);
/*      */       } 
/* 1083 */     } catch (InterruptedException ie) {
/*      */       
/* 1085 */       Thread.currentThread().interrupt();
/*      */       
/* 1087 */       service.shutdownNow();
/*      */     } 
/* 1089 */     return service.isTerminated();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Executor rejectionPropagatingExecutor(final Executor delegate, final AbstractFuture<?> future) {
/* 1100 */     Preconditions.checkNotNull(delegate);
/* 1101 */     Preconditions.checkNotNull(future);
/* 1102 */     if (delegate == directExecutor())
/*      */     {
/* 1104 */       return delegate;
/*      */     }
/* 1106 */     return new Executor()
/*      */       {
/*      */         public void execute(Runnable command) {
/*      */           try {
/* 1110 */             delegate.execute(command);
/* 1111 */           } catch (RejectedExecutionException e) {
/* 1112 */             future.setException(e);
/*      */           } 
/*      */         }
/*      */       };
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\commo\\util\concurrent\MoreExecutors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */