/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class ExecutionSequencer
/*     */ {
/*     */   public static ExecutionSequencer create() {
/*  87 */     return new ExecutionSequencer();
/*     */   }
/*     */ 
/*     */   
/*  91 */   private final AtomicReference<ListenableFuture<Void>> ref = new AtomicReference<>(
/*  92 */       Futures.immediateVoidFuture());
/*     */   
/*  94 */   private ThreadConfinedTaskQueue latestTaskQueue = new ThreadConfinedTaskQueue();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ThreadConfinedTaskQueue
/*     */   {
/*     */     Thread thread;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Runnable nextTask;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Executor nextExecutor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private ThreadConfinedTaskQueue() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<T> submit(final Callable<T> callable, Executor executor) {
/* 140 */     Preconditions.checkNotNull(callable);
/* 141 */     Preconditions.checkNotNull(executor);
/* 142 */     return submitAsync(new AsyncCallable<T>(this)
/*     */         {
/*     */           public ListenableFuture<T> call() throws Exception
/*     */           {
/* 146 */             return Futures.immediateFuture(callable.call());
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 151 */             return callable.toString();
/*     */           }
/*     */         }executor);
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
/*     */   public <T> ListenableFuture<T> submitAsync(final AsyncCallable<T> callable, Executor executor) {
/* 166 */     Preconditions.checkNotNull(callable);
/* 167 */     Preconditions.checkNotNull(executor);
/* 168 */     final TaskNonReentrantExecutor taskExecutor = new TaskNonReentrantExecutor(executor, this);
/* 169 */     AsyncCallable<T> task = new AsyncCallable<T>(this)
/*     */       {
/*     */         public ListenableFuture<T> call() throws Exception
/*     */         {
/* 173 */           if (!taskExecutor.trySetStarted()) {
/* 174 */             return Futures.immediateCancelledFuture();
/*     */           }
/* 176 */           return callable.call();
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 181 */           return callable.toString();
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 195 */     final SettableFuture<Void> newFuture = SettableFuture.create();
/*     */     
/* 197 */     final ListenableFuture<Void> oldFuture = this.ref.getAndSet(newFuture);
/*     */ 
/*     */     
/* 200 */     final TrustedListenableFutureTask<T> taskFuture = TrustedListenableFutureTask.create(task);
/* 201 */     oldFuture.addListener(taskFuture, taskExecutor);
/*     */     
/* 203 */     final ListenableFuture<T> outputFuture = Futures.nonCancellationPropagating(taskFuture);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 209 */     Runnable listener = new Runnable(this)
/*     */       {
/*     */         public void run()
/*     */         {
/* 213 */           if (taskFuture.isDone()) {
/*     */ 
/*     */ 
/*     */             
/* 217 */             newFuture.setFuture(oldFuture);
/* 218 */           } else if (outputFuture.isCancelled() && taskExecutor.trySetCancelled()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 245 */             taskFuture.cancel(false);
/*     */           } 
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */     
/* 252 */     outputFuture.addListener(listener, MoreExecutors.directExecutor());
/* 253 */     taskFuture.addListener(listener, MoreExecutors.directExecutor());
/*     */     
/* 255 */     return outputFuture;
/*     */   }
/*     */   
/*     */   enum RunningState {
/* 259 */     NOT_RUN,
/* 260 */     CANCELLED,
/* 261 */     STARTED;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class TaskNonReentrantExecutor
/*     */     extends AtomicReference<RunningState>
/*     */     implements Executor, Runnable
/*     */   {
/*     */     ExecutionSequencer sequencer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Executor delegate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Runnable task;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Thread submitting;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private TaskNonReentrantExecutor(Executor delegate, ExecutionSequencer sequencer) {
/* 308 */       super(ExecutionSequencer.RunningState.NOT_RUN);
/* 309 */       this.delegate = delegate;
/* 310 */       this.sequencer = sequencer;
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
/*     */     
/*     */     public void execute(Runnable task) {
/* 324 */       if (get() == ExecutionSequencer.RunningState.CANCELLED) {
/* 325 */         this.delegate = null;
/* 326 */         this.sequencer = null;
/*     */         return;
/*     */       } 
/* 329 */       this.submitting = Thread.currentThread();
/*     */       try {
/* 331 */         ExecutionSequencer.ThreadConfinedTaskQueue submittingTaskQueue = this.sequencer.latestTaskQueue;
/* 332 */         if (submittingTaskQueue.thread == this.submitting) {
/* 333 */           this.sequencer = null;
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 338 */           Preconditions.checkState((submittingTaskQueue.nextTask == null));
/* 339 */           submittingTaskQueue.nextTask = task;
/* 340 */           submittingTaskQueue.nextExecutor = this.delegate;
/* 341 */           this.delegate = null;
/*     */         } else {
/* 343 */           Executor localDelegate = this.delegate;
/* 344 */           this.delegate = null;
/* 345 */           this.task = task;
/* 346 */           localDelegate.execute(this);
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */       finally {
/*     */         
/* 353 */         this.submitting = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/* 360 */       Thread currentThread = Thread.currentThread();
/* 361 */       if (currentThread != this.submitting) {
/* 362 */         Runnable localTask = this.task;
/* 363 */         this.task = null;
/* 364 */         localTask.run();
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 370 */       ExecutionSequencer.ThreadConfinedTaskQueue executingTaskQueue = new ExecutionSequencer.ThreadConfinedTaskQueue();
/* 371 */       executingTaskQueue.thread = currentThread;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 381 */       this.sequencer.latestTaskQueue = executingTaskQueue;
/* 382 */       this.sequencer = null;
/*     */       try {
/* 384 */         Runnable localTask = this.task;
/* 385 */         this.task = null;
/* 386 */         localTask.run();
/*     */         
/*     */         while (true) {
/*     */           Runnable queuedTask;
/*     */           Executor queuedExecutor;
/* 391 */           if (((((queuedTask = executingTaskQueue.nextTask) != null) ? 1 : 0) & (((queuedExecutor = executingTaskQueue.nextExecutor) != null) ? 1 : 0)) != 0) {
/*     */             
/* 393 */             executingTaskQueue.nextTask = null;
/* 394 */             executingTaskQueue.nextExecutor = null;
/* 395 */             queuedExecutor.execute(queuedTask);
/*     */ 
/*     */             
/*     */             continue;
/*     */           } 
/*     */           
/*     */           break;
/*     */         } 
/*     */       } finally {
/* 404 */         executingTaskQueue.thread = null;
/*     */       } 
/*     */     }
/*     */     
/*     */     private boolean trySetStarted() {
/* 409 */       return compareAndSet(ExecutionSequencer.RunningState.NOT_RUN, ExecutionSequencer.RunningState.STARTED);
/*     */     }
/*     */     
/*     */     private boolean trySetCancelled() {
/* 413 */       return compareAndSet(ExecutionSequencer.RunningState.NOT_RUN, ExecutionSequencer.RunningState.CANCELLED);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\commo\\util\concurrent\ExecutionSequencer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */