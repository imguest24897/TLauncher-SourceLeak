/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.ForOverride;
/*     */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*     */ import java.time.Duration;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class AbstractService
/*     */   implements Service
/*     */ {
/*  53 */   private static final ListenerCallQueue.Event<Service.Listener> STARTING_EVENT = new ListenerCallQueue.Event<Service.Listener>()
/*     */     {
/*     */       public void call(Service.Listener listener)
/*     */       {
/*  57 */         listener.starting();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/*  62 */         return "starting()";
/*     */       }
/*     */     };
/*  65 */   private static final ListenerCallQueue.Event<Service.Listener> RUNNING_EVENT = new ListenerCallQueue.Event<Service.Listener>()
/*     */     {
/*     */       public void call(Service.Listener listener)
/*     */       {
/*  69 */         listener.running();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/*  74 */         return "running()";
/*     */       }
/*     */     };
/*     */   
/*  78 */   private static final ListenerCallQueue.Event<Service.Listener> STOPPING_FROM_STARTING_EVENT = stoppingEvent(Service.State.STARTING);
/*     */   
/*  80 */   private static final ListenerCallQueue.Event<Service.Listener> STOPPING_FROM_RUNNING_EVENT = stoppingEvent(Service.State.RUNNING);
/*     */ 
/*     */   
/*  83 */   private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_NEW_EVENT = terminatedEvent(Service.State.NEW);
/*     */   
/*  85 */   private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_STARTING_EVENT = terminatedEvent(Service.State.STARTING);
/*     */   
/*  87 */   private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_RUNNING_EVENT = terminatedEvent(Service.State.RUNNING);
/*     */   
/*  89 */   private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_STOPPING_EVENT = terminatedEvent(Service.State.STOPPING);
/*     */   
/*     */   private static ListenerCallQueue.Event<Service.Listener> terminatedEvent(final Service.State from) {
/*  92 */     return new ListenerCallQueue.Event<Service.Listener>()
/*     */       {
/*     */         public void call(Service.Listener listener) {
/*  95 */           listener.terminated(from);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 100 */           String str = String.valueOf(from); return (new StringBuilder(21 + String.valueOf(str).length())).append("terminated({from = ").append(str).append("})").toString();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static ListenerCallQueue.Event<Service.Listener> stoppingEvent(final Service.State from) {
/* 106 */     return new ListenerCallQueue.Event<Service.Listener>()
/*     */       {
/*     */         public void call(Service.Listener listener) {
/* 109 */           listener.stopping(from);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 114 */           String str = String.valueOf(from); return (new StringBuilder(19 + String.valueOf(str).length())).append("stopping({from = ").append(str).append("})").toString();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/* 119 */   private final Monitor monitor = new Monitor();
/*     */   
/* 121 */   private final Monitor.Guard isStartable = new IsStartableGuard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class IsStartableGuard
/*     */     extends Monitor.Guard
/*     */   {
/*     */     public boolean isSatisfied() {
/* 131 */       return (AbstractService.this.state() == Service.State.NEW);
/*     */     }
/*     */   }
/*     */   
/* 135 */   private final Monitor.Guard isStoppable = new IsStoppableGuard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class IsStoppableGuard
/*     */     extends Monitor.Guard
/*     */   {
/*     */     public boolean isSatisfied() {
/* 145 */       return (AbstractService.this.state().compareTo(Service.State.RUNNING) <= 0);
/*     */     }
/*     */   }
/*     */   
/* 149 */   private final Monitor.Guard hasReachedRunning = new HasReachedRunningGuard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class HasReachedRunningGuard
/*     */     extends Monitor.Guard
/*     */   {
/*     */     public boolean isSatisfied() {
/* 159 */       return (AbstractService.this.state().compareTo(Service.State.RUNNING) >= 0);
/*     */     }
/*     */   }
/*     */   
/* 163 */   private final Monitor.Guard isStopped = new IsStoppedGuard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class IsStoppedGuard
/*     */     extends Monitor.Guard
/*     */   {
/*     */     public boolean isSatisfied() {
/* 173 */       return AbstractService.this.state().isTerminal();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 178 */   private final ListenerCallQueue<Service.Listener> listeners = new ListenerCallQueue<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 189 */   private volatile StateSnapshot snapshot = new StateSnapshot(Service.State.NEW);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   @ForOverride
/*     */   protected void doCancelStart() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public final Service startAsync() {
/* 245 */     if (this.monitor.enterIf(this.isStartable)) {
/*     */       try {
/* 247 */         this.snapshot = new StateSnapshot(Service.State.STARTING);
/* 248 */         enqueueStartingEvent();
/* 249 */         doStart();
/* 250 */       } catch (Throwable startupFailure) {
/* 251 */         notifyFailed(startupFailure);
/*     */       } finally {
/* 253 */         this.monitor.leave();
/* 254 */         dispatchListenerEvents();
/*     */       } 
/*     */     } else {
/* 257 */       String str = String.valueOf(this); throw new IllegalStateException((new StringBuilder(33 + String.valueOf(str).length())).append("Service ").append(str).append(" has already been started").toString());
/*     */     } 
/* 259 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Service stopAsync() {
/* 265 */     if (this.monitor.enterIf(this.isStoppable)) {
/*     */       try {
/* 267 */         String str; Service.State previous = state();
/* 268 */         switch (previous) {
/*     */           case NEW:
/* 270 */             this.snapshot = new StateSnapshot(Service.State.TERMINATED);
/* 271 */             enqueueTerminatedEvent(Service.State.NEW);
/*     */             break;
/*     */           case STARTING:
/* 274 */             this.snapshot = new StateSnapshot(Service.State.STARTING, true, null);
/* 275 */             enqueueStoppingEvent(Service.State.STARTING);
/* 276 */             doCancelStart();
/*     */             break;
/*     */           case RUNNING:
/* 279 */             this.snapshot = new StateSnapshot(Service.State.STOPPING);
/* 280 */             enqueueStoppingEvent(Service.State.RUNNING);
/* 281 */             doStop();
/*     */             break;
/*     */           
/*     */           case STOPPING:
/*     */           case TERMINATED:
/*     */           case FAILED:
/* 287 */             str = String.valueOf(previous); throw new AssertionError((new StringBuilder(45 + String.valueOf(str).length())).append("isStoppable is incorrectly implemented, saw: ").append(str).toString());
/*     */         } 
/* 289 */       } catch (Throwable shutdownFailure) {
/* 290 */         notifyFailed(shutdownFailure);
/*     */       } finally {
/* 292 */         this.monitor.leave();
/* 293 */         dispatchListenerEvents();
/*     */       } 
/*     */     }
/* 296 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void awaitRunning() {
/* 301 */     this.monitor.enterWhenUninterruptibly(this.hasReachedRunning);
/*     */     try {
/* 303 */       checkCurrentState(Service.State.RUNNING);
/*     */     } finally {
/* 305 */       this.monitor.leave();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void awaitRunning(Duration timeout) throws TimeoutException {
/* 312 */     super.awaitRunning(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException {
/* 317 */     if (this.monitor.enterWhenUninterruptibly(this.hasReachedRunning, timeout, unit)) {
/*     */       try {
/* 319 */         checkCurrentState(Service.State.RUNNING);
/*     */       } finally {
/* 321 */         this.monitor.leave();
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 328 */       String str = String.valueOf(this); throw new TimeoutException((new StringBuilder(50 + String.valueOf(str).length())).append("Timed out waiting for ").append(str).append(" to reach the RUNNING state.").toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void awaitTerminated() {
/* 334 */     this.monitor.enterWhenUninterruptibly(this.isStopped);
/*     */     try {
/* 336 */       checkCurrentState(Service.State.TERMINATED);
/*     */     } finally {
/* 338 */       this.monitor.leave();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void awaitTerminated(Duration timeout) throws TimeoutException {
/* 345 */     super.awaitTerminated(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException {
/* 350 */     if (this.monitor.enterWhenUninterruptibly(this.isStopped, timeout, unit)) {
/*     */       try {
/* 352 */         checkCurrentState(Service.State.TERMINATED);
/*     */       } finally {
/* 354 */         this.monitor.leave();
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 361 */       String str1 = String.valueOf(this);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 366 */       String str2 = String.valueOf(state()); throw new TimeoutException((new StringBuilder(65 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("Timed out waiting for ").append(str1).append(" to reach a terminal state. Current state: ").append(str2).toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @GuardedBy("monitor")
/*     */   private void checkCurrentState(Service.State expected) {
/* 373 */     Service.State actual = state();
/* 374 */     if (actual != expected) {
/* 375 */       if (actual == Service.State.FAILED) {
/*     */         
/* 377 */         String str4 = String.valueOf(this), str5 = String.valueOf(expected); throw new IllegalStateException((new StringBuilder(56 + String.valueOf(str4).length() + String.valueOf(str5).length())).append("Expected the service ").append(str4).append(" to be ").append(str5).append(", but the service has FAILED").toString(), 
/*     */             
/* 379 */             failureCause());
/*     */       } 
/* 381 */       String str1 = String.valueOf(this), str2 = String.valueOf(expected), str3 = String.valueOf(actual); throw new IllegalStateException((new StringBuilder(38 + String.valueOf(str1).length() + String.valueOf(str2).length() + String.valueOf(str3).length())).append("Expected the service ").append(str1).append(" to be ").append(str2).append(", but was ").append(str3).toString());
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
/*     */   protected final void notifyStarted() {
/* 393 */     this.monitor.enter();
/*     */ 
/*     */     
/*     */     try {
/* 397 */       if (this.snapshot.state != Service.State.STARTING) {
/* 398 */         String str = String.valueOf(this.snapshot.state); IllegalStateException failure = new IllegalStateException((new StringBuilder(43 + String.valueOf(str).length())).append("Cannot notifyStarted() when the service is ").append(str).toString());
/*     */ 
/*     */         
/* 401 */         notifyFailed(failure);
/* 402 */         throw failure;
/*     */       } 
/*     */       
/* 405 */       if (this.snapshot.shutdownWhenStartupFinishes) {
/* 406 */         this.snapshot = new StateSnapshot(Service.State.STOPPING);
/*     */ 
/*     */         
/* 409 */         doStop();
/*     */       } else {
/* 411 */         this.snapshot = new StateSnapshot(Service.State.RUNNING);
/* 412 */         enqueueRunningEvent();
/*     */       } 
/*     */     } finally {
/* 415 */       this.monitor.leave();
/* 416 */       dispatchListenerEvents();
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
/*     */   protected final void notifyStopped() {
/* 429 */     this.monitor.enter(); try {
/*     */       String str;
/* 431 */       Service.State previous = state();
/* 432 */       switch (previous) {
/*     */         case NEW:
/*     */         case TERMINATED:
/*     */         case FAILED:
/* 436 */           str = String.valueOf(previous); throw new IllegalStateException((new StringBuilder(43 + String.valueOf(str).length())).append("Cannot notifyStopped() when the service is ").append(str).toString());
/*     */         case STARTING:
/*     */         case RUNNING:
/*     */         case STOPPING:
/* 440 */           this.snapshot = new StateSnapshot(Service.State.TERMINATED);
/* 441 */           enqueueTerminatedEvent(previous);
/*     */           break;
/*     */       } 
/*     */     } finally {
/* 445 */       this.monitor.leave();
/* 446 */       dispatchListenerEvents();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void notifyFailed(Throwable cause) {
/* 456 */     Preconditions.checkNotNull(cause);
/*     */     
/* 458 */     this.monitor.enter(); try {
/*     */       String str;
/* 460 */       Service.State previous = state();
/* 461 */       switch (previous) {
/*     */         case NEW:
/*     */         case TERMINATED:
/* 464 */           str = String.valueOf(previous); throw new IllegalStateException((new StringBuilder(22 + String.valueOf(str).length())).append("Failed while in state:").append(str).toString(), cause);
/*     */         case STARTING:
/*     */         case RUNNING:
/*     */         case STOPPING:
/* 468 */           this.snapshot = new StateSnapshot(Service.State.FAILED, false, cause);
/* 469 */           enqueueFailedEvent(previous, cause);
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */     
/*     */     } finally {
/* 476 */       this.monitor.leave();
/* 477 */       dispatchListenerEvents();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isRunning() {
/* 483 */     return (state() == Service.State.RUNNING);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Service.State state() {
/* 488 */     return this.snapshot.externalState();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Throwable failureCause() {
/* 494 */     return this.snapshot.failureCause();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void addListener(Service.Listener listener, Executor executor) {
/* 500 */     this.listeners.addListener(listener, executor);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 505 */     String str1 = getClass().getSimpleName(), str2 = String.valueOf(state()); return (new StringBuilder(3 + String.valueOf(str1).length() + String.valueOf(str2).length())).append(str1).append(" [").append(str2).append("]").toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void dispatchListenerEvents() {
/* 513 */     if (!this.monitor.isOccupiedByCurrentThread()) {
/* 514 */       this.listeners.dispatch();
/*     */     }
/*     */   }
/*     */   
/*     */   private void enqueueStartingEvent() {
/* 519 */     this.listeners.enqueue(STARTING_EVENT);
/*     */   }
/*     */   
/*     */   private void enqueueRunningEvent() {
/* 523 */     this.listeners.enqueue(RUNNING_EVENT);
/*     */   }
/*     */   
/*     */   private void enqueueStoppingEvent(Service.State from) {
/* 527 */     if (from == Service.State.STARTING) {
/* 528 */       this.listeners.enqueue(STOPPING_FROM_STARTING_EVENT);
/* 529 */     } else if (from == Service.State.RUNNING) {
/* 530 */       this.listeners.enqueue(STOPPING_FROM_RUNNING_EVENT);
/*     */     } else {
/* 532 */       throw new AssertionError();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void enqueueTerminatedEvent(Service.State from) {
/* 537 */     switch (from) {
/*     */       case NEW:
/* 539 */         this.listeners.enqueue(TERMINATED_FROM_NEW_EVENT);
/*     */         break;
/*     */       case STARTING:
/* 542 */         this.listeners.enqueue(TERMINATED_FROM_STARTING_EVENT);
/*     */         break;
/*     */       case RUNNING:
/* 545 */         this.listeners.enqueue(TERMINATED_FROM_RUNNING_EVENT);
/*     */         break;
/*     */       case STOPPING:
/* 548 */         this.listeners.enqueue(TERMINATED_FROM_STOPPING_EVENT);
/*     */         break;
/*     */       case TERMINATED:
/*     */       case FAILED:
/* 552 */         throw new AssertionError();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void enqueueFailedEvent(final Service.State from, final Throwable cause) {
/* 558 */     this.listeners.enqueue(new ListenerCallQueue.Event<Service.Listener>(this)
/*     */         {
/*     */           public void call(Service.Listener listener)
/*     */           {
/* 562 */             listener.failed(from, cause);
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 567 */             String str1 = String.valueOf(from), str2 = String.valueOf(cause); return (new StringBuilder(27 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("failed({from = ").append(str1).append(", cause = ").append(str2).append("})").toString();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @ForOverride
/*     */   protected abstract void doStart();
/*     */ 
/*     */ 
/*     */   
/*     */   @ForOverride
/*     */   protected abstract void doStop();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class StateSnapshot
/*     */   {
/*     */     final Service.State state;
/*     */     
/*     */     final boolean shutdownWhenStartupFinishes;
/*     */     
/*     */     final Throwable failure;
/*     */ 
/*     */     
/*     */     StateSnapshot(Service.State internalState) {
/* 594 */       this(internalState, false, null);
/*     */     }
/*     */ 
/*     */     
/*     */     StateSnapshot(Service.State internalState, boolean shutdownWhenStartupFinishes, Throwable failure) {
/* 599 */       Preconditions.checkArgument((!shutdownWhenStartupFinishes || internalState == Service.State.STARTING), "shutdownWhenStartupFinishes can only be set if state is STARTING. Got %s instead.", internalState);
/*     */ 
/*     */ 
/*     */       
/* 603 */       Preconditions.checkArgument(((((failure != null) ? 1 : 0) ^ ((internalState == Service.State.FAILED) ? 1 : 0)) == 0), "A failure cause should be set if and only if the state is failed.  Got %s and %s instead.", internalState, failure);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 609 */       this.state = internalState;
/* 610 */       this.shutdownWhenStartupFinishes = shutdownWhenStartupFinishes;
/* 611 */       this.failure = failure;
/*     */     }
/*     */ 
/*     */     
/*     */     Service.State externalState() {
/* 616 */       if (this.shutdownWhenStartupFinishes && this.state == Service.State.STARTING) {
/* 617 */         return Service.State.STOPPING;
/*     */       }
/* 619 */       return this.state;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Throwable failureCause() {
/* 625 */       Preconditions.checkState((this.state == Service.State.FAILED), "failureCause() is only valid if the service has failed, service is %s", this.state);
/*     */ 
/*     */ 
/*     */       
/* 629 */       return this.failure;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\commo\\util\concurrent\AbstractService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */