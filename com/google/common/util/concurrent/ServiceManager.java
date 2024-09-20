/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.google.common.collect.Collections2;
/*     */ import com.google.common.collect.ImmutableCollection;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableMultimap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.ImmutableSetMultimap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.MultimapBuilder;
/*     */ import com.google.common.collect.Multimaps;
/*     */ import com.google.common.collect.Multiset;
/*     */ import com.google.common.collect.Ordering;
/*     */ import com.google.common.collect.SetMultimap;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.time.Duration;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class ServiceManager
/*     */   implements ServiceManagerBridge
/*     */ {
/* 124 */   private static final Logger logger = Logger.getLogger(ServiceManager.class.getName());
/* 125 */   private static final ListenerCallQueue.Event<Listener> HEALTHY_EVENT = new ListenerCallQueue.Event<Listener>()
/*     */     {
/*     */       public void call(ServiceManager.Listener listener)
/*     */       {
/* 129 */         listener.healthy();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 134 */         return "healthy()";
/*     */       }
/*     */     };
/* 137 */   private static final ListenerCallQueue.Event<Listener> STOPPED_EVENT = new ListenerCallQueue.Event<Listener>()
/*     */     {
/*     */       public void call(ServiceManager.Listener listener)
/*     */       {
/* 141 */         listener.stopped();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 146 */         return "stopped()";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ServiceManagerState state;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ImmutableList<Service> services;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class Listener
/*     */   {
/*     */     public void healthy() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void stopped() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void failure(Service service) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServiceManager(Iterable<? extends Service> services) {
/* 203 */     ImmutableList<Service> copy = ImmutableList.copyOf(services);
/* 204 */     if (copy.isEmpty()) {
/*     */ 
/*     */       
/* 207 */       logger.log(Level.WARNING, "ServiceManager configured with no services.  Is your application configured properly?", new EmptyServiceManagerWarning());
/*     */ 
/*     */ 
/*     */       
/* 211 */       copy = ImmutableList.of(new NoOpService());
/*     */     } 
/* 213 */     this.state = new ServiceManagerState((ImmutableCollection<Service>)copy);
/* 214 */     this.services = copy;
/* 215 */     WeakReference<ServiceManagerState> stateReference = new WeakReference<>(this.state);
/* 216 */     for (UnmodifiableIterator<Service> unmodifiableIterator = copy.iterator(); unmodifiableIterator.hasNext(); ) { Service service = unmodifiableIterator.next();
/* 217 */       service.addListener(new ServiceListener(service, stateReference), MoreExecutors.directExecutor());
/*     */ 
/*     */       
/* 220 */       Preconditions.checkArgument((service.state() == Service.State.NEW), "Can only manage NEW services, %s", service); }
/*     */ 
/*     */ 
/*     */     
/* 224 */     this.state.markReady();
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
/*     */   public void addListener(Listener listener, Executor executor) {
/* 252 */     this.state.addListener(listener, executor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public ServiceManager startAsync() {
/*     */     UnmodifiableIterator<Service> unmodifiableIterator;
/* 265 */     for (unmodifiableIterator = this.services.iterator(); unmodifiableIterator.hasNext(); ) { Service service = unmodifiableIterator.next();
/* 266 */       Service.State state = service.state();
/* 267 */       Preconditions.checkState((state == Service.State.NEW), "Service %s is %s, cannot start it.", service, state); }
/*     */     
/* 269 */     for (unmodifiableIterator = this.services.iterator(); unmodifiableIterator.hasNext(); ) { Service service = unmodifiableIterator.next();
/*     */       try {
/* 271 */         this.state.tryStartTiming(service);
/* 272 */         service.startAsync();
/* 273 */       } catch (IllegalStateException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 278 */         String str = String.valueOf(service); logger.log(Level.WARNING, (new StringBuilder(24 + String.valueOf(str).length())).append("Unable to start Service ").append(str).toString(), e);
/*     */       }  }
/*     */     
/* 281 */     return this;
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
/*     */   public void awaitHealthy() {
/* 293 */     this.state.awaitHealthy();
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
/*     */   public void awaitHealthy(Duration timeout) throws TimeoutException {
/* 308 */     awaitHealthy(Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
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
/*     */   public void awaitHealthy(long timeout, TimeUnit unit) throws TimeoutException {
/* 324 */     this.state.awaitHealthy(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public ServiceManager stopAsync() {
/* 335 */     for (UnmodifiableIterator<Service> unmodifiableIterator = this.services.iterator(); unmodifiableIterator.hasNext(); ) { Service service = unmodifiableIterator.next();
/* 336 */       service.stopAsync(); }
/*     */     
/* 338 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void awaitStopped() {
/* 347 */     this.state.awaitStopped();
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
/*     */   public void awaitStopped(Duration timeout) throws TimeoutException {
/* 360 */     awaitStopped(Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
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
/*     */   public void awaitStopped(long timeout, TimeUnit unit) throws TimeoutException {
/* 374 */     this.state.awaitStopped(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isHealthy() {
/* 384 */     for (UnmodifiableIterator<Service> unmodifiableIterator = this.services.iterator(); unmodifiableIterator.hasNext(); ) { Service service = unmodifiableIterator.next();
/* 385 */       if (!service.isRunning()) {
/* 386 */         return false;
/*     */       } }
/*     */     
/* 389 */     return true;
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
/*     */   public ImmutableSetMultimap<Service.State, Service> servicesByState() {
/* 402 */     return this.state.servicesByState();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<Service, Long> startupTimes() {
/* 413 */     return this.state.startupTimes();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 418 */     return MoreObjects.toStringHelper(ServiceManager.class)
/* 419 */       .add("services", Collections2.filter((Collection)this.services, Predicates.not(Predicates.instanceOf(NoOpService.class))))
/* 420 */       .toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ServiceManagerState
/*     */   {
/* 428 */     final Monitor monitor = new Monitor();
/*     */ 
/*     */     
/*     */     @GuardedBy("monitor")
/* 432 */     final SetMultimap<Service.State, Service> servicesByState = MultimapBuilder.enumKeys(Service.State.class).linkedHashSetValues().build();
/*     */     @GuardedBy("monitor")
/* 434 */     final Multiset<Service.State> states = this.servicesByState
/* 435 */       .keys();
/*     */     
/*     */     @GuardedBy("monitor")
/* 438 */     final Map<Service, Stopwatch> startupTimers = Maps.newIdentityHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @GuardedBy("monitor")
/*     */     boolean ready;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @GuardedBy("monitor")
/*     */     boolean transitioned;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final int numberOfServices;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 465 */     final Monitor.Guard awaitHealthGuard = new AwaitHealthGuard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final class AwaitHealthGuard
/*     */       extends Monitor.Guard
/*     */     {
/*     */       @GuardedBy("ServiceManagerState.this.monitor")
/*     */       public boolean isSatisfied() {
/* 477 */         return (ServiceManager.ServiceManagerState.this.states.count(Service.State.RUNNING) == ServiceManager.ServiceManagerState.this.numberOfServices || ServiceManager.ServiceManagerState.this.states
/* 478 */           .contains(Service.State.STOPPING) || ServiceManager.ServiceManagerState.this.states
/* 479 */           .contains(Service.State.TERMINATED) || ServiceManager.ServiceManagerState.this.states
/* 480 */           .contains(Service.State.FAILED));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 485 */     final Monitor.Guard stoppedGuard = new StoppedGuard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final class StoppedGuard
/*     */       extends Monitor.Guard
/*     */     {
/*     */       @GuardedBy("ServiceManagerState.this.monitor")
/*     */       public boolean isSatisfied() {
/* 496 */         return (ServiceManager.ServiceManagerState.this.states.count(Service.State.TERMINATED) + ServiceManager.ServiceManagerState.this.states.count(Service.State.FAILED) == ServiceManager.ServiceManagerState.this.numberOfServices);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 501 */     final ListenerCallQueue<ServiceManager.Listener> listeners = new ListenerCallQueue<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ServiceManagerState(ImmutableCollection<Service> services) {
/* 510 */       this.numberOfServices = services.size();
/* 511 */       this.servicesByState.putAll(Service.State.NEW, (Iterable)services);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void tryStartTiming(Service service) {
/* 519 */       this.monitor.enter();
/*     */       try {
/* 521 */         Stopwatch stopwatch = this.startupTimers.get(service);
/* 522 */         if (stopwatch == null) {
/* 523 */           this.startupTimers.put(service, Stopwatch.createStarted());
/*     */         }
/*     */       } finally {
/* 526 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void markReady() {
/* 535 */       this.monitor.enter();
/*     */       try {
/* 537 */         if (!this.transitioned) {
/*     */           
/* 539 */           this.ready = true;
/*     */         } else {
/*     */           
/* 542 */           List<Service> servicesInBadStates = Lists.newArrayList();
/* 543 */           for (UnmodifiableIterator<Service> unmodifiableIterator = servicesByState().values().iterator(); unmodifiableIterator.hasNext(); ) { Service service = unmodifiableIterator.next();
/* 544 */             if (service.state() != Service.State.NEW) {
/* 545 */               servicesInBadStates.add(service);
/*     */             } }
/*     */           
/* 548 */           String str = String.valueOf(servicesInBadStates); throw new IllegalArgumentException((new StringBuilder(89 + String.valueOf(str).length())).append("Services started transitioning asynchronously before the ServiceManager was constructed: ").append(str).toString());
/*     */         }
/*     */       
/*     */       }
/*     */       finally {
/*     */         
/* 554 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */     
/*     */     void addListener(ServiceManager.Listener listener, Executor executor) {
/* 559 */       this.listeners.addListener(listener, executor);
/*     */     }
/*     */     
/*     */     void awaitHealthy() {
/* 563 */       this.monitor.enterWhenUninterruptibly(this.awaitHealthGuard);
/*     */       try {
/* 565 */         checkHealthy();
/*     */       } finally {
/* 567 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */     
/*     */     void awaitHealthy(long timeout, TimeUnit unit) throws TimeoutException {
/* 572 */       this.monitor.enter();
/*     */       try {
/* 574 */         if (!this.monitor.waitForUninterruptibly(this.awaitHealthGuard, timeout, unit)) {
/*     */ 
/*     */ 
/*     */           
/* 578 */           String str = String.valueOf(Multimaps.filterKeys(this.servicesByState, Predicates.in((Collection)ImmutableSet.of(Service.State.NEW, Service.State.STARTING)))); throw new TimeoutException((new StringBuilder(93 + String.valueOf(str).length())).append("Timeout waiting for the services to become healthy. The following services have not started: ").append(str).toString());
/*     */         } 
/* 580 */         checkHealthy();
/*     */       } finally {
/* 582 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */     
/*     */     void awaitStopped() {
/* 587 */       this.monitor.enterWhenUninterruptibly(this.stoppedGuard);
/* 588 */       this.monitor.leave();
/*     */     }
/*     */     
/*     */     void awaitStopped(long timeout, TimeUnit unit) throws TimeoutException {
/* 592 */       this.monitor.enter();
/*     */       try {
/* 594 */         if (!this.monitor.waitForUninterruptibly(this.stoppedGuard, timeout, unit)) {
/*     */ 
/*     */ 
/*     */           
/* 598 */           String str = String.valueOf(Multimaps.filterKeys(this.servicesByState, Predicates.not(Predicates.in(EnumSet.of(Service.State.TERMINATED, Service.State.FAILED))))); throw new TimeoutException((new StringBuilder(83 + String.valueOf(str).length())).append("Timeout waiting for the services to stop. The following services have not stopped: ").append(str).toString());
/*     */         } 
/*     */       } finally {
/* 601 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */     
/*     */     ImmutableSetMultimap<Service.State, Service> servicesByState() {
/* 606 */       ImmutableSetMultimap.Builder<Service.State, Service> builder = ImmutableSetMultimap.builder();
/* 607 */       this.monitor.enter();
/*     */       try {
/* 609 */         for (Map.Entry<Service.State, Service> entry : (Iterable<Map.Entry<Service.State, Service>>)this.servicesByState.entries()) {
/* 610 */           if (!(entry.getValue() instanceof ServiceManager.NoOpService)) {
/* 611 */             builder.put(entry);
/*     */           }
/*     */         } 
/*     */       } finally {
/* 615 */         this.monitor.leave();
/*     */       } 
/* 617 */       return builder.build();
/*     */     }
/*     */     
/*     */     ImmutableMap<Service, Long> startupTimes() {
/*     */       List<Map.Entry<Service, Long>> loadTimes;
/* 622 */       this.monitor.enter();
/*     */       try {
/* 624 */         loadTimes = Lists.newArrayListWithCapacity(this.startupTimers.size());
/*     */         
/* 626 */         for (Map.Entry<Service, Stopwatch> entry : this.startupTimers.entrySet()) {
/* 627 */           Service service = entry.getKey();
/* 628 */           Stopwatch stopWatch = entry.getValue();
/* 629 */           if (!stopWatch.isRunning() && !(service instanceof ServiceManager.NoOpService)) {
/* 630 */             loadTimes.add(Maps.immutableEntry(service, Long.valueOf(stopWatch.elapsed(TimeUnit.MILLISECONDS))));
/*     */           }
/*     */         } 
/*     */       } finally {
/* 634 */         this.monitor.leave();
/*     */       } 
/* 636 */       Collections.sort(loadTimes, 
/*     */           
/* 638 */           (Comparator<? super Map.Entry<Service, Long>>)Ordering.natural()
/* 639 */           .onResultOf(new Function<Map.Entry<Service, Long>, Long>(this)
/*     */             {
/*     */               public Long apply(Map.Entry<Service, Long> input)
/*     */               {
/* 643 */                 return input.getValue();
/*     */               }
/*     */             }));
/* 646 */       return ImmutableMap.copyOf(loadTimes);
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
/*     */ 
/*     */     
/*     */     void transitionService(Service service, Service.State from, Service.State to) {
/* 662 */       Preconditions.checkNotNull(service);
/* 663 */       Preconditions.checkArgument((from != to));
/* 664 */       this.monitor.enter();
/*     */       try {
/* 666 */         this.transitioned = true;
/* 667 */         if (!this.ready) {
/*     */           return;
/*     */         }
/*     */         
/* 671 */         Preconditions.checkState(this.servicesByState
/* 672 */             .remove(from, service), "Service %s not at the expected location in the state map %s", service, from);
/*     */ 
/*     */ 
/*     */         
/* 676 */         Preconditions.checkState(this.servicesByState
/* 677 */             .put(to, service), "Service %s in the state map unexpectedly at %s", service, to);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 682 */         Stopwatch stopwatch = this.startupTimers.get(service);
/* 683 */         if (stopwatch == null) {
/*     */           
/* 685 */           stopwatch = Stopwatch.createStarted();
/* 686 */           this.startupTimers.put(service, stopwatch);
/*     */         } 
/* 688 */         if (to.compareTo(Service.State.RUNNING) >= 0 && stopwatch.isRunning()) {
/*     */           
/* 690 */           stopwatch.stop();
/* 691 */           if (!(service instanceof ServiceManager.NoOpService)) {
/* 692 */             ServiceManager.logger.log(Level.FINE, "Started {0} in {1}.", new Object[] { service, stopwatch });
/*     */           }
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 698 */         if (to == Service.State.FAILED) {
/* 699 */           enqueueFailedEvent(service);
/*     */         }
/*     */         
/* 702 */         if (this.states.count(Service.State.RUNNING) == this.numberOfServices) {
/*     */ 
/*     */           
/* 705 */           enqueueHealthyEvent();
/* 706 */         } else if (this.states.count(Service.State.TERMINATED) + this.states.count(Service.State.FAILED) == this.numberOfServices) {
/* 707 */           enqueueStoppedEvent();
/*     */         } 
/*     */       } finally {
/* 710 */         this.monitor.leave();
/*     */         
/* 712 */         dispatchListenerEvents();
/*     */       } 
/*     */     }
/*     */     
/*     */     void enqueueStoppedEvent() {
/* 717 */       this.listeners.enqueue(ServiceManager.STOPPED_EVENT);
/*     */     }
/*     */     
/*     */     void enqueueHealthyEvent() {
/* 721 */       this.listeners.enqueue(ServiceManager.HEALTHY_EVENT);
/*     */     }
/*     */     
/*     */     void enqueueFailedEvent(final Service service) {
/* 725 */       this.listeners.enqueue(new ListenerCallQueue.Event<ServiceManager.Listener>(this)
/*     */           {
/*     */             public void call(ServiceManager.Listener listener)
/*     */             {
/* 729 */               listener.failure(service);
/*     */             }
/*     */ 
/*     */             
/*     */             public String toString() {
/* 734 */               String str = String.valueOf(service); return (new StringBuilder(18 + String.valueOf(str).length())).append("failed({service=").append(str).append("})").toString();
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*     */     void dispatchListenerEvents() {
/* 741 */       Preconditions.checkState(
/* 742 */           !this.monitor.isOccupiedByCurrentThread(), "It is incorrect to execute listeners with the monitor held.");
/*     */       
/* 744 */       this.listeners.dispatch();
/*     */     }
/*     */     
/*     */     @GuardedBy("monitor")
/*     */     void checkHealthy() {
/* 749 */       if (this.states.count(Service.State.RUNNING) != this.numberOfServices) {
/*     */ 
/*     */ 
/*     */         
/* 753 */         String str = String.valueOf(Multimaps.filterKeys(this.servicesByState, Predicates.not(Predicates.equalTo(Service.State.RUNNING)))); IllegalStateException exception = new IllegalStateException((new StringBuilder(79 + String.valueOf(str).length())).append("Expected to be healthy after starting. The following services are not running: ").append(str).toString());
/* 754 */         for (Service service : this.servicesByState.get(Service.State.FAILED)) {
/* 755 */           exception.addSuppressed(new ServiceManager.FailedService(service));
/*     */         }
/* 757 */         throw exception;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ServiceListener
/*     */     extends Service.Listener
/*     */   {
/*     */     final Service service;
/*     */ 
/*     */     
/*     */     final WeakReference<ServiceManager.ServiceManagerState> state;
/*     */ 
/*     */     
/*     */     ServiceListener(Service service, WeakReference<ServiceManager.ServiceManagerState> state) {
/* 774 */       this.service = service;
/* 775 */       this.state = state;
/*     */     }
/*     */ 
/*     */     
/*     */     public void starting() {
/* 780 */       ServiceManager.ServiceManagerState state = this.state.get();
/* 781 */       if (state != null) {
/* 782 */         state.transitionService(this.service, Service.State.NEW, Service.State.STARTING);
/* 783 */         if (!(this.service instanceof ServiceManager.NoOpService)) {
/* 784 */           ServiceManager.logger.log(Level.FINE, "Starting {0}.", this.service);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void running() {
/* 791 */       ServiceManager.ServiceManagerState state = this.state.get();
/* 792 */       if (state != null) {
/* 793 */         state.transitionService(this.service, Service.State.STARTING, Service.State.RUNNING);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void stopping(Service.State from) {
/* 799 */       ServiceManager.ServiceManagerState state = this.state.get();
/* 800 */       if (state != null) {
/* 801 */         state.transitionService(this.service, from, Service.State.STOPPING);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void terminated(Service.State from) {
/* 807 */       ServiceManager.ServiceManagerState state = this.state.get();
/* 808 */       if (state != null) {
/* 809 */         if (!(this.service instanceof ServiceManager.NoOpService)) {
/* 810 */           ServiceManager.logger.log(Level.FINE, "Service {0} has terminated. Previous state was: {1}", new Object[] { this.service, from });
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 815 */         state.transitionService(this.service, from, Service.State.TERMINATED);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void failed(Service.State from, Throwable failure) {
/* 821 */       ServiceManager.ServiceManagerState state = this.state.get();
/* 822 */       if (state != null) {
/*     */ 
/*     */         
/* 825 */         boolean log = !(this.service instanceof ServiceManager.NoOpService);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 830 */         int i = log & ((from != Service.State.STARTING) ? 1 : 0);
/* 831 */         if (i != 0) {
/* 832 */           String str1 = String.valueOf(this.service), str2 = String.valueOf(from); ServiceManager.logger.log(Level.SEVERE, (new StringBuilder(34 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("Service ").append(str1).append(" has failed in the ").append(str2).append(" state.").toString(), failure);
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 837 */         state.transitionService(this.service, from, Service.State.FAILED);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class NoOpService
/*     */     extends AbstractService
/*     */   {
/*     */     private NoOpService() {}
/*     */ 
/*     */ 
/*     */     
/*     */     protected void doStart() {
/* 853 */       notifyStarted();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void doStop() {
/* 858 */       notifyStopped();
/*     */     } }
/*     */   
/*     */   private static final class EmptyServiceManagerWarning extends Throwable {
/*     */     private EmptyServiceManagerWarning() {}
/*     */   }
/*     */   
/*     */   private static final class FailedService extends Throwable {
/*     */     FailedService(Service service) {
/* 867 */       super(service
/* 868 */           .toString(), service
/* 869 */           .failureCause(), false, false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\commo\\util\concurrent\ServiceManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */