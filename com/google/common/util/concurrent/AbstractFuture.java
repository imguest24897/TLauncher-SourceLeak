/*      */ package com.google.common.util.concurrent;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Strings;
/*      */ import com.google.common.base.Throwables;
/*      */ import com.google.common.util.concurrent.internal.InternalFutureFailureAccess;
/*      */ import com.google.common.util.concurrent.internal.InternalFutures;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.ForOverride;
/*      */ import com.google.j2objc.annotations.ReflectionSupport;
/*      */ import java.lang.reflect.Field;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.Locale;
/*      */ import java.util.concurrent.CancellationException;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.ScheduledFuture;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.TimeoutException;
/*      */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*      */ import java.util.concurrent.locks.LockSupport;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import sun.misc.Unsafe;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */ @ReflectionSupport(ReflectionSupport.Level.FULL)
/*      */ public abstract class AbstractFuture<V>
/*      */   extends InternalFutureFailureAccess
/*      */   implements ListenableFuture<V>
/*      */ {
/*      */   private static final boolean GENERATE_CANCELLATION_CAUSES;
/*      */   
/*      */   static {
/*      */     boolean generateCancellationCauses;
/*      */     AtomicHelper helper;
/*      */     try {
/*   81 */       generateCancellationCauses = Boolean.parseBoolean(
/*   82 */           System.getProperty("guava.concurrent.generate_cancellation_cause", "false"));
/*   83 */     } catch (SecurityException e) {
/*   84 */       generateCancellationCauses = false;
/*      */     } 
/*   86 */     GENERATE_CANCELLATION_CAUSES = generateCancellationCauses;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static interface Trusted<V>
/*      */     extends ListenableFuture<V> {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class TrustedFuture<V>
/*      */     extends AbstractFuture<V>
/*      */     implements Trusted<V>
/*      */   {
/*      */     @CanIgnoreReturnValue
/*      */     public final V get() throws InterruptedException, ExecutionException {
/*  104 */       return super.get();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public final V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*  111 */       return super.get(timeout, unit);
/*      */     }
/*      */ 
/*      */     
/*      */     public final boolean isDone() {
/*  116 */       return super.isDone();
/*      */     }
/*      */ 
/*      */     
/*      */     public final boolean isCancelled() {
/*  121 */       return super.isCancelled();
/*      */     }
/*      */ 
/*      */     
/*      */     public final void addListener(Runnable listener, Executor executor) {
/*  126 */       super.addListener(listener, executor);
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public final boolean cancel(boolean mayInterruptIfRunning) {
/*  132 */       return super.cancel(mayInterruptIfRunning);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*  137 */   private static final Logger log = Logger.getLogger(AbstractFuture.class.getName());
/*      */ 
/*      */   
/*      */   private static final long SPIN_THRESHOLD_NANOS = 1000L;
/*      */ 
/*      */   
/*      */   private static final AtomicHelper ATOMIC_HELPER;
/*      */ 
/*      */   
/*      */   static {
/*  147 */     Throwable thrownUnsafeFailure = null;
/*  148 */     Throwable thrownAtomicReferenceFieldUpdaterFailure = null;
/*      */     
/*      */     try {
/*  151 */       helper = new UnsafeAtomicHelper();
/*  152 */     } catch (Throwable unsafeFailure) {
/*  153 */       thrownUnsafeFailure = unsafeFailure;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  164 */         helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Thread.class, "thread"), AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Waiter.class, "next"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Waiter.class, "waiters"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Listener.class, "listeners"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Object.class, "value"));
/*  165 */       } catch (Throwable atomicReferenceFieldUpdaterFailure) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  170 */         thrownAtomicReferenceFieldUpdaterFailure = atomicReferenceFieldUpdaterFailure;
/*  171 */         helper = new SynchronizedHelper();
/*      */       } 
/*      */     } 
/*  174 */     ATOMIC_HELPER = helper;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  179 */     Class<?> ensureLoaded = LockSupport.class;
/*      */ 
/*      */ 
/*      */     
/*  183 */     if (thrownAtomicReferenceFieldUpdaterFailure != null) {
/*  184 */       log.log(Level.SEVERE, "UnsafeAtomicHelper is broken!", thrownUnsafeFailure);
/*  185 */       log.log(Level.SEVERE, "SafeAtomicHelper is broken!", thrownAtomicReferenceFieldUpdaterFailure);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class Waiter
/*      */   {
/*  192 */     static final Waiter TOMBSTONE = new Waiter(false);
/*      */ 
/*      */     
/*      */     volatile Thread thread;
/*      */ 
/*      */     
/*      */     volatile Waiter next;
/*      */ 
/*      */     
/*      */     Waiter(boolean unused) {}
/*      */ 
/*      */     
/*      */     Waiter() {
/*  205 */       AbstractFuture.ATOMIC_HELPER.putThread(this, Thread.currentThread());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     void setNext(Waiter next) {
/*  211 */       AbstractFuture.ATOMIC_HELPER.putNext(this, next);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void unpark() {
/*  218 */       Thread w = this.thread;
/*  219 */       if (w != null) {
/*  220 */         this.thread = null;
/*  221 */         LockSupport.unpark(w);
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
/*      */   private void removeWaiter(Waiter node) {
/*  238 */     node.thread = null;
/*      */     
/*      */     label22: while (true) {
/*  241 */       Waiter pred = null;
/*  242 */       Waiter curr = this.waiters;
/*  243 */       if (curr == Waiter.TOMBSTONE) {
/*      */         return;
/*      */       }
/*      */       
/*  247 */       while (curr != null) {
/*  248 */         Waiter succ = curr.next;
/*  249 */         if (curr.thread != null) {
/*  250 */           pred = curr;
/*  251 */         } else if (pred != null) {
/*  252 */           pred.next = succ;
/*  253 */           if (pred.thread == null) {
/*      */             continue label22;
/*      */           }
/*  256 */         } else if (!ATOMIC_HELPER.casWaiters(this, curr, succ)) {
/*      */           continue label22;
/*      */         } 
/*  259 */         curr = succ;
/*      */       } 
/*      */       break;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static final class Listener
/*      */   {
/*  267 */     static final Listener TOMBSTONE = new Listener(null, null);
/*      */     
/*      */     final Runnable task;
/*      */     
/*      */     final Executor executor;
/*      */     Listener next;
/*      */     
/*      */     Listener(Runnable task, Executor executor) {
/*  275 */       this.task = task;
/*  276 */       this.executor = executor;
/*      */     }
/*      */   }
/*      */   private volatile Object value;
/*      */   private volatile Listener listeners;
/*  281 */   private static final Object NULL = new Object();
/*      */   private volatile Waiter waiters;
/*      */   
/*      */   private static final class Failure {
/*  285 */     static final Failure FALLBACK_INSTANCE = new Failure(new Throwable("Failure occurred while trying to finish a future.")
/*      */         {
/*      */           
/*      */           public synchronized Throwable fillInStackTrace()
/*      */           {
/*  290 */             return this;
/*      */           }
/*      */         });
/*      */ 
/*      */     
/*      */     Failure(Throwable exception) {
/*  296 */       this.exception = (Throwable)Preconditions.checkNotNull(exception);
/*      */     }
/*      */     
/*      */     final Throwable exception; }
/*      */   
/*      */   private static final class Cancellation { static final Cancellation CAUSELESS_INTERRUPTED;
/*      */     static final Cancellation CAUSELESS_CANCELLED;
/*      */     final boolean wasInterrupted;
/*      */     final Throwable cause;
/*      */     
/*      */     static {
/*  307 */       if (AbstractFuture.GENERATE_CANCELLATION_CAUSES) {
/*  308 */         CAUSELESS_CANCELLED = null;
/*  309 */         CAUSELESS_INTERRUPTED = null;
/*      */       } else {
/*  311 */         CAUSELESS_CANCELLED = new Cancellation(false, null);
/*  312 */         CAUSELESS_INTERRUPTED = new Cancellation(true, null);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Cancellation(boolean wasInterrupted, Throwable cause) {
/*  320 */       this.wasInterrupted = wasInterrupted;
/*  321 */       this.cause = cause;
/*      */     } }
/*      */ 
/*      */   
/*      */   private static final class SetFuture<V>
/*      */     implements Runnable {
/*      */     final AbstractFuture<V> owner;
/*      */     final ListenableFuture<? extends V> future;
/*      */     
/*      */     SetFuture(AbstractFuture<V> owner, ListenableFuture<? extends V> future) {
/*  331 */       this.owner = owner;
/*  332 */       this.future = future;
/*      */     }
/*      */ 
/*      */     
/*      */     public void run() {
/*  337 */       if (this.owner.value != this) {
/*      */         return;
/*      */       }
/*      */       
/*  341 */       Object valueToSet = AbstractFuture.getFutureValue(this.future);
/*  342 */       if (AbstractFuture.ATOMIC_HELPER.casValue(this.owner, this, valueToSet)) {
/*  343 */         AbstractFuture.complete(this.owner);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public V get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException {
/*  414 */     long timeoutNanos = unit.toNanos(timeout);
/*  415 */     long remainingNanos = timeoutNanos;
/*  416 */     if (Thread.interrupted()) {
/*  417 */       throw new InterruptedException();
/*      */     }
/*  419 */     Object localValue = this.value;
/*  420 */     if ((((localValue != null) ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
/*  421 */       return getDoneValue(localValue);
/*      */     }
/*      */     
/*  424 */     long endNanos = (remainingNanos > 0L) ? (System.nanoTime() + remainingNanos) : 0L;
/*      */     
/*  426 */     if (remainingNanos >= 1000L) {
/*  427 */       Waiter oldHead = this.waiters;
/*  428 */       if (oldHead != Waiter.TOMBSTONE) {
/*  429 */         Waiter node = new Waiter();
/*      */         label77: while (true) {
/*  431 */           node.setNext(oldHead);
/*  432 */           if (ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
/*      */             do {
/*  434 */               OverflowAvoidingLockSupport.parkNanos(this, remainingNanos);
/*      */               
/*  436 */               if (Thread.interrupted()) {
/*  437 */                 removeWaiter(node);
/*  438 */                 throw new InterruptedException();
/*      */               } 
/*      */ 
/*      */ 
/*      */               
/*  443 */               localValue = this.value;
/*  444 */               if ((((localValue != null) ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
/*  445 */                 return getDoneValue(localValue);
/*      */               }
/*      */ 
/*      */               
/*  449 */               remainingNanos = endNanos - System.nanoTime();
/*  450 */             } while (remainingNanos >= 1000L);
/*      */             
/*  452 */             removeWaiter(node);
/*      */             
/*      */             break;
/*      */           } 
/*      */           
/*  457 */           oldHead = this.waiters;
/*  458 */           if (oldHead == Waiter.TOMBSTONE)
/*      */             break label77; 
/*      */         } 
/*      */       } else {
/*  462 */         return getDoneValue(this.value);
/*      */       } 
/*      */     } 
/*      */     
/*  466 */     while (remainingNanos > 0L) {
/*  467 */       localValue = this.value;
/*  468 */       if ((((localValue != null) ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
/*  469 */         return getDoneValue(localValue);
/*      */       }
/*  471 */       if (Thread.interrupted()) {
/*  472 */         throw new InterruptedException();
/*      */       }
/*  474 */       remainingNanos = endNanos - System.nanoTime();
/*      */     } 
/*      */     
/*  477 */     String futureToString = toString();
/*  478 */     String unitString = unit.toString().toLowerCase(Locale.ROOT);
/*  479 */     String str1 = unit.toString().toLowerCase(Locale.ROOT), message = (new StringBuilder(28 + String.valueOf(str1).length())).append("Waited ").append(timeout).append(" ").append(str1).toString();
/*      */     
/*  481 */     if (remainingNanos + 1000L < 0L) {
/*      */       
/*  483 */       message = String.valueOf(message).concat(" (plus ");
/*  484 */       long overWaitNanos = -remainingNanos;
/*  485 */       long overWaitUnits = unit.convert(overWaitNanos, TimeUnit.NANOSECONDS);
/*  486 */       long overWaitLeftoverNanos = overWaitNanos - unit.toNanos(overWaitUnits);
/*  487 */       boolean shouldShowExtraNanos = (overWaitUnits == 0L || overWaitLeftoverNanos > 1000L);
/*      */       
/*  489 */       if (overWaitUnits > 0L) {
/*  490 */         String str = String.valueOf(message); message = (new StringBuilder(21 + String.valueOf(str).length() + String.valueOf(unitString).length())).append(str).append(overWaitUnits).append(" ").append(unitString).toString();
/*  491 */         if (shouldShowExtraNanos) {
/*  492 */           message = String.valueOf(message).concat(",");
/*      */         }
/*  494 */         message = String.valueOf(message).concat(" ");
/*      */       } 
/*  496 */       if (shouldShowExtraNanos) {
/*  497 */         String str = String.valueOf(message); message = (new StringBuilder(33 + String.valueOf(str).length())).append(str).append(overWaitLeftoverNanos).append(" nanoseconds ").toString();
/*      */       } 
/*      */       
/*  500 */       message = String.valueOf(message).concat("delay)");
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  505 */     if (isDone()) {
/*  506 */       throw new TimeoutException(String.valueOf(message).concat(" but future completed as timeout expired"));
/*      */     }
/*  508 */     str1 = message; throw new TimeoutException((new StringBuilder(5 + String.valueOf(str1).length() + String.valueOf(futureToString).length())).append(str1).append(" for ").append(futureToString).toString());
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
/*      */   @CanIgnoreReturnValue
/*      */   public V get() throws InterruptedException, ExecutionException {
/*  522 */     if (Thread.interrupted()) {
/*  523 */       throw new InterruptedException();
/*      */     }
/*  525 */     Object localValue = this.value;
/*  526 */     if ((((localValue != null) ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
/*  527 */       return getDoneValue(localValue);
/*      */     }
/*  529 */     Waiter oldHead = this.waiters;
/*  530 */     if (oldHead != Waiter.TOMBSTONE) {
/*  531 */       Waiter node = new Waiter();
/*      */       do {
/*  533 */         node.setNext(oldHead);
/*  534 */         if (ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
/*      */           while (true) {
/*      */             
/*  537 */             LockSupport.park(this);
/*      */             
/*  539 */             if (Thread.interrupted()) {
/*  540 */               removeWaiter(node);
/*  541 */               throw new InterruptedException();
/*      */             } 
/*      */ 
/*      */             
/*  545 */             localValue = this.value;
/*  546 */             if ((((localValue != null) ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
/*  547 */               return getDoneValue(localValue);
/*      */             }
/*      */           } 
/*      */         }
/*  551 */         oldHead = this.waiters;
/*  552 */       } while (oldHead != Waiter.TOMBSTONE);
/*      */     } 
/*      */ 
/*      */     
/*  556 */     return getDoneValue(this.value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private V getDoneValue(Object obj) throws ExecutionException {
/*  563 */     if (obj instanceof Cancellation)
/*  564 */       throw cancellationExceptionWithCause("Task was cancelled.", ((Cancellation)obj).cause); 
/*  565 */     if (obj instanceof Failure)
/*  566 */       throw new ExecutionException(((Failure)obj).exception); 
/*  567 */     if (obj == NULL) {
/*  568 */       return null;
/*      */     }
/*      */     
/*  571 */     V asV = (V)obj;
/*  572 */     return asV;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDone() {
/*  578 */     Object localValue = this.value;
/*  579 */     return ((localValue != null)) & (!(localValue instanceof SetFuture));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCancelled() {
/*  584 */     Object localValue = this.value;
/*  585 */     return localValue instanceof Cancellation;
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
/*      */   @CanIgnoreReturnValue
/*      */   public boolean cancel(boolean mayInterruptIfRunning) {
/*  607 */     Object localValue = this.value;
/*  608 */     boolean rValue = false;
/*  609 */     if ((((localValue == null) ? 1 : 0) | localValue instanceof SetFuture) != 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  618 */       Object valueToSet = GENERATE_CANCELLATION_CAUSES ? new Cancellation(mayInterruptIfRunning, new CancellationException("Future.cancel() was called.")) : (mayInterruptIfRunning ? Cancellation.CAUSELESS_INTERRUPTED : Cancellation.CAUSELESS_CANCELLED);
/*  619 */       AbstractFuture<?> abstractFuture = this;
/*      */       do {
/*  621 */         while (ATOMIC_HELPER.casValue(abstractFuture, localValue, valueToSet)) {
/*  622 */           rValue = true;
/*      */ 
/*      */           
/*  625 */           if (mayInterruptIfRunning) {
/*  626 */             abstractFuture.interruptTask();
/*      */           }
/*  628 */           complete(abstractFuture);
/*  629 */           if (localValue instanceof SetFuture) {
/*      */ 
/*      */             
/*  632 */             ListenableFuture<?> futureToPropagateTo = ((SetFuture)localValue).future;
/*  633 */             if (futureToPropagateTo instanceof Trusted) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  641 */               AbstractFuture<?> trusted = (AbstractFuture)futureToPropagateTo;
/*  642 */               localValue = trusted.value;
/*  643 */               if ((((localValue == null) ? 1 : 0) | localValue instanceof SetFuture) != 0) {
/*  644 */                 abstractFuture = trusted;
/*      */                 continue;
/*      */               } 
/*      */               // Byte code: goto -> 190
/*      */             } 
/*  649 */             futureToPropagateTo.cancel(mayInterruptIfRunning);
/*      */             
/*      */             break;
/*      */           } 
/*      */           // Byte code: goto -> 190
/*      */         } 
/*  655 */         localValue = abstractFuture.value;
/*  656 */       } while (localValue instanceof SetFuture);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  664 */     return rValue;
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
/*      */   protected void interruptTask() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean wasInterrupted() {
/*  687 */     Object localValue = this.value;
/*  688 */     return (localValue instanceof Cancellation && ((Cancellation)localValue).wasInterrupted);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addListener(Runnable listener, Executor executor) {
/*  698 */     Preconditions.checkNotNull(listener, "Runnable was null.");
/*  699 */     Preconditions.checkNotNull(executor, "Executor was null.");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  709 */     if (!isDone()) {
/*  710 */       Listener oldHead = this.listeners;
/*  711 */       if (oldHead != Listener.TOMBSTONE) {
/*  712 */         Listener newNode = new Listener(listener, executor);
/*      */         do {
/*  714 */           newNode.next = oldHead;
/*  715 */           if (ATOMIC_HELPER.casListeners(this, oldHead, newNode)) {
/*      */             return;
/*      */           }
/*  718 */           oldHead = this.listeners;
/*  719 */         } while (oldHead != Listener.TOMBSTONE);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  724 */     executeListener(listener, executor);
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
/*      */   @CanIgnoreReturnValue
/*      */   protected boolean set(V value) {
/*  744 */     Object valueToSet = (value == null) ? NULL : value;
/*  745 */     if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
/*  746 */       complete(this);
/*  747 */       return true;
/*      */     } 
/*  749 */     return false;
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
/*      */   @CanIgnoreReturnValue
/*      */   protected boolean setException(Throwable throwable) {
/*  769 */     Object valueToSet = new Failure((Throwable)Preconditions.checkNotNull(throwable));
/*  770 */     if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
/*  771 */       complete(this);
/*  772 */       return true;
/*      */     } 
/*  774 */     return false;
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
/*      */   @CanIgnoreReturnValue
/*      */   protected boolean setFuture(ListenableFuture<? extends V> future) {
/*  808 */     Preconditions.checkNotNull(future);
/*  809 */     Object localValue = this.value;
/*  810 */     if (localValue == null) {
/*  811 */       if (future.isDone()) {
/*  812 */         Object value = getFutureValue(future);
/*  813 */         if (ATOMIC_HELPER.casValue(this, null, value)) {
/*  814 */           complete(this);
/*  815 */           return true;
/*      */         } 
/*  817 */         return false;
/*      */       } 
/*  819 */       SetFuture<V> valueToSet = new SetFuture<>(this, future);
/*  820 */       if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
/*      */ 
/*      */         
/*      */         try {
/*  824 */           future.addListener(valueToSet, DirectExecutor.INSTANCE);
/*  825 */         } catch (Throwable t) {
/*      */           Failure failure;
/*      */ 
/*      */ 
/*      */           
/*      */           try {
/*  831 */             failure = new Failure(t);
/*  832 */           } catch (Throwable oomMostLikely) {
/*  833 */             failure = Failure.FALLBACK_INSTANCE;
/*      */           } 
/*      */           
/*  836 */           boolean bool = ATOMIC_HELPER.casValue(this, valueToSet, failure);
/*      */         } 
/*  838 */         return true;
/*      */       } 
/*  840 */       localValue = this.value;
/*      */     } 
/*      */ 
/*      */     
/*  844 */     if (localValue instanceof Cancellation)
/*      */     {
/*  846 */       future.cancel(((Cancellation)localValue).wasInterrupted);
/*      */     }
/*  848 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Object getFutureValue(ListenableFuture<?> future) {
/*  858 */     if (future instanceof Trusted) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  863 */       Object v = ((AbstractFuture)future).value;
/*  864 */       if (v instanceof Cancellation) {
/*      */ 
/*      */ 
/*      */         
/*  868 */         Cancellation c = (Cancellation)v;
/*  869 */         if (c.wasInterrupted)
/*      */         {
/*      */ 
/*      */           
/*  873 */           v = (c.cause != null) ? new Cancellation(false, c.cause) : Cancellation.CAUSELESS_CANCELLED;
/*      */         }
/*      */       } 
/*  876 */       return v;
/*      */     } 
/*  878 */     if (future instanceof InternalFutureFailureAccess) {
/*      */       
/*  880 */       Throwable throwable = InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess)future);
/*  881 */       if (throwable != null) {
/*  882 */         return new Failure(throwable);
/*      */       }
/*      */     } 
/*  885 */     boolean wasCancelled = future.isCancelled();
/*      */     
/*  887 */     if (((!GENERATE_CANCELLATION_CAUSES ? 1 : 0) & wasCancelled) != 0) {
/*  888 */       return Cancellation.CAUSELESS_CANCELLED;
/*      */     }
/*      */     
/*      */     try {
/*  892 */       Object v = getUninterruptibly(future);
/*  893 */       if (wasCancelled) {
/*  894 */         String str = String.valueOf(future); return new Cancellation(false, new IllegalArgumentException((new StringBuilder(84 + String.valueOf(str).length())).append("get() did not throw CancellationException, despite reporting isCancelled() == true: ").append(str).toString()));
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  901 */       return (v == null) ? NULL : v;
/*  902 */     } catch (ExecutionException exception) {
/*  903 */       if (wasCancelled) {
/*  904 */         String str = String.valueOf(future); return new Cancellation(false, new IllegalArgumentException((new StringBuilder(84 + String.valueOf(str).length())).append("get() did not throw CancellationException, despite reporting isCancelled() == true: ").append(str).toString(), exception));
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  912 */       return new Failure(exception.getCause());
/*  913 */     } catch (CancellationException cancellation) {
/*  914 */       if (!wasCancelled) {
/*  915 */         String str = String.valueOf(future); return new Failure(new IllegalArgumentException((new StringBuilder(77 + String.valueOf(str).length())).append("get() threw CancellationException, despite reporting isCancelled() == false: ").append(str).toString(), cancellation));
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  921 */       return new Cancellation(false, cancellation);
/*  922 */     } catch (Throwable t) {
/*  923 */       return new Failure(t);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <V> V getUninterruptibly(Future<V> future) throws ExecutionException {
/*  932 */     boolean interrupted = false;
/*      */     
/*      */     while (true) {
/*      */       try {
/*  936 */         return future.get();
/*  937 */       } catch (InterruptedException e) {
/*      */ 
/*      */       
/*      */       } finally {
/*      */         
/*  942 */         if (interrupted) {
/*  943 */           Thread.currentThread().interrupt();
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void complete(AbstractFuture<?> future) {
/*  950 */     Listener next = null;
/*      */     
/*      */     label17: while (true) {
/*  953 */       future.releaseWaiters();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  959 */       future.afterDone();
/*      */       
/*  961 */       next = future.clearListeners(next);
/*  962 */       future = null;
/*  963 */       while (next != null) {
/*  964 */         Listener curr = next;
/*  965 */         next = next.next;
/*  966 */         Runnable task = curr.task;
/*  967 */         if (task instanceof SetFuture) {
/*  968 */           SetFuture<?> setFuture = (SetFuture)task;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  974 */           future = setFuture.owner;
/*  975 */           if (future.value == setFuture) {
/*  976 */             Object valueToSet = getFutureValue(setFuture.future);
/*  977 */             if (ATOMIC_HELPER.casValue(future, setFuture, valueToSet)) {
/*      */               continue label17;
/*      */             }
/*      */           } 
/*      */           continue;
/*      */         } 
/*  983 */         executeListener(task, curr.executor);
/*      */       } 
/*      */       break;
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
/*      */   @Beta
/*      */   @ForOverride
/*      */   protected void afterDone() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Throwable tryInternalFastPathGetFailure() {
/* 1028 */     if (this instanceof Trusted) {
/* 1029 */       Object obj = this.value;
/* 1030 */       if (obj instanceof Failure) {
/* 1031 */         return ((Failure)obj).exception;
/*      */       }
/*      */     } 
/* 1034 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void maybePropagateCancellationTo(Future<?> related) {
/* 1042 */     if ((((related != null) ? 1 : 0) & isCancelled()) != 0) {
/* 1043 */       related.cancel(wasInterrupted());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void releaseWaiters() {
/*      */     while (true) {
/* 1051 */       Waiter head = this.waiters;
/* 1052 */       if (ATOMIC_HELPER.casWaiters(this, head, Waiter.TOMBSTONE)) {
/* 1053 */         for (Waiter currentWaiter = head; currentWaiter != null; currentWaiter = currentWaiter.next) {
/* 1054 */           currentWaiter.unpark();
/*      */         }
/*      */         return;
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
/*      */   private Listener clearListeners(Listener onto) {
/*      */     while (true) {
/* 1071 */       Listener head = this.listeners;
/* 1072 */       if (ATOMIC_HELPER.casListeners(this, head, Listener.TOMBSTONE)) {
/* 1073 */         Listener reversedList = onto;
/* 1074 */         while (head != null) {
/* 1075 */           Listener tmp = head;
/* 1076 */           head = head.next;
/* 1077 */           tmp.next = reversedList;
/* 1078 */           reversedList = tmp;
/*      */         } 
/* 1080 */         return reversedList;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1087 */     StringBuilder builder = new StringBuilder();
/* 1088 */     if (getClass().getName().startsWith("com.google.common.util.concurrent.")) {
/* 1089 */       builder.append(getClass().getSimpleName());
/*      */     } else {
/* 1091 */       builder.append(getClass().getName());
/*      */     } 
/* 1093 */     builder.append('@').append(Integer.toHexString(System.identityHashCode(this))).append("[status=");
/* 1094 */     if (isCancelled()) {
/* 1095 */       builder.append("CANCELLED");
/* 1096 */     } else if (isDone()) {
/* 1097 */       addDoneString(builder);
/*      */     } else {
/* 1099 */       addPendingString(builder);
/*      */     } 
/* 1101 */     return builder.append("]").toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String pendingToString() {
/* 1112 */     if (this instanceof ScheduledFuture) {
/*      */       
/* 1114 */       long l = ((ScheduledFuture)this).getDelay(TimeUnit.MILLISECONDS); return (new StringBuilder(41)).append("remaining delay=[").append(l).append(" ms]").toString();
/*      */     } 
/*      */     
/* 1117 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void addPendingString(StringBuilder builder) {
/* 1123 */     int truncateLength = builder.length();
/*      */     
/* 1125 */     builder.append("PENDING");
/*      */     
/* 1127 */     Object localValue = this.value;
/* 1128 */     if (localValue instanceof SetFuture) {
/* 1129 */       builder.append(", setFuture=[");
/* 1130 */       appendUserObject(builder, ((SetFuture)localValue).future);
/* 1131 */       builder.append("]");
/*      */     } else {
/*      */       String pendingDescription;
/*      */       try {
/* 1135 */         pendingDescription = Strings.emptyToNull(pendingToString());
/* 1136 */       } catch (RuntimeException|StackOverflowError e) {
/*      */ 
/*      */         
/* 1139 */         String str = String.valueOf(e.getClass()); pendingDescription = (new StringBuilder(38 + String.valueOf(str).length())).append("Exception thrown from implementation: ").append(str).toString();
/*      */       } 
/* 1141 */       if (pendingDescription != null) {
/* 1142 */         builder.append(", info=[").append(pendingDescription).append("]");
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1148 */     if (isDone()) {
/*      */       
/* 1150 */       builder.delete(truncateLength, builder.length());
/* 1151 */       addDoneString(builder);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void addDoneString(StringBuilder builder) {
/*      */     try {
/* 1157 */       V value = getUninterruptibly(this);
/* 1158 */       builder.append("SUCCESS, result=[");
/* 1159 */       appendResultObject(builder, value);
/* 1160 */       builder.append("]");
/* 1161 */     } catch (ExecutionException e) {
/* 1162 */       builder.append("FAILURE, cause=[").append(e.getCause()).append("]");
/* 1163 */     } catch (CancellationException e) {
/* 1164 */       builder.append("CANCELLED");
/* 1165 */     } catch (RuntimeException e) {
/* 1166 */       builder.append("UNKNOWN, cause=[").append(e.getClass()).append(" thrown from get()]");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void appendResultObject(StringBuilder builder, Object o) {
/* 1176 */     if (o == null) {
/* 1177 */       builder.append("null");
/* 1178 */     } else if (o == this) {
/* 1179 */       builder.append("this future");
/*      */     } else {
/* 1181 */       builder
/* 1182 */         .append(o.getClass().getName())
/* 1183 */         .append("@")
/* 1184 */         .append(Integer.toHexString(System.identityHashCode(o)));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void appendUserObject(StringBuilder builder, Object o) {
/*      */     try {
/* 1195 */       if (o == this) {
/* 1196 */         builder.append("this future");
/*      */       } else {
/* 1198 */         builder.append(o);
/*      */       } 
/* 1200 */     } catch (RuntimeException|StackOverflowError e) {
/*      */ 
/*      */       
/* 1203 */       builder.append("Exception thrown from implementation: ").append(e.getClass());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void executeListener(Runnable runnable, Executor executor) {
/*      */     try {
/* 1213 */       executor.execute(runnable);
/* 1214 */     } catch (RuntimeException e) {
/*      */ 
/*      */ 
/*      */       
/* 1218 */       String str1 = String.valueOf(runnable), str2 = String.valueOf(executor); log.log(Level.SEVERE, (new StringBuilder(57 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("RuntimeException while executing runnable ").append(str1).append(" with executor ").append(str2).toString(), e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static abstract class AtomicHelper
/*      */   {
/*      */     private AtomicHelper() {}
/*      */ 
/*      */     
/*      */     abstract void putThread(AbstractFuture.Waiter param1Waiter, Thread param1Thread);
/*      */ 
/*      */     
/*      */     abstract void putNext(AbstractFuture.Waiter param1Waiter1, AbstractFuture.Waiter param1Waiter2);
/*      */ 
/*      */     
/*      */     abstract boolean casWaiters(AbstractFuture<?> param1AbstractFuture, AbstractFuture.Waiter param1Waiter1, AbstractFuture.Waiter param1Waiter2);
/*      */ 
/*      */     
/*      */     abstract boolean casListeners(AbstractFuture<?> param1AbstractFuture, AbstractFuture.Listener param1Listener1, AbstractFuture.Listener param1Listener2);
/*      */ 
/*      */     
/*      */     abstract boolean casValue(AbstractFuture<?> param1AbstractFuture, Object param1Object1, Object param1Object2);
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class UnsafeAtomicHelper
/*      */     extends AtomicHelper
/*      */   {
/*      */     static final Unsafe UNSAFE;
/*      */     static final long LISTENERS_OFFSET;
/*      */     static final long WAITERS_OFFSET;
/*      */     static final long VALUE_OFFSET;
/*      */     static final long WAITER_THREAD_OFFSET;
/*      */     static final long WAITER_NEXT_OFFSET;
/*      */     
/*      */     private UnsafeAtomicHelper() {}
/*      */     
/*      */     static {
/* 1257 */       Unsafe unsafe = null;
/*      */       try {
/* 1259 */         unsafe = Unsafe.getUnsafe();
/* 1260 */       } catch (SecurityException tryReflectionInstead) {
/*      */         
/*      */         try {
/* 1263 */           unsafe = AccessController.<Unsafe>doPrivileged(new PrivilegedExceptionAction<Unsafe>()
/*      */               {
/*      */                 public Unsafe run() throws Exception
/*      */                 {
/* 1267 */                   Class<Unsafe> k = Unsafe.class;
/* 1268 */                   for (Field f : k.getDeclaredFields()) {
/* 1269 */                     f.setAccessible(true);
/* 1270 */                     Object x = f.get(null);
/* 1271 */                     if (k.isInstance(x)) {
/* 1272 */                       return k.cast(x);
/*      */                     }
/*      */                   } 
/* 1275 */                   throw new NoSuchFieldError("the Unsafe");
/*      */                 }
/*      */               });
/* 1278 */         } catch (PrivilegedActionException e) {
/* 1279 */           throw new RuntimeException("Could not initialize intrinsics", e.getCause());
/*      */         } 
/*      */       } 
/*      */       try {
/* 1283 */         Class<?> abstractFuture = AbstractFuture.class;
/* 1284 */         WAITERS_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("waiters"));
/* 1285 */         LISTENERS_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("listeners"));
/* 1286 */         VALUE_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("value"));
/* 1287 */         WAITER_THREAD_OFFSET = unsafe.objectFieldOffset(AbstractFuture.Waiter.class.getDeclaredField("thread"));
/* 1288 */         WAITER_NEXT_OFFSET = unsafe.objectFieldOffset(AbstractFuture.Waiter.class.getDeclaredField("next"));
/* 1289 */         UNSAFE = unsafe;
/* 1290 */       } catch (Exception e) {
/* 1291 */         Throwables.throwIfUnchecked(e);
/* 1292 */         throw new RuntimeException(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     void putThread(AbstractFuture.Waiter waiter, Thread newValue) {
/* 1298 */       UNSAFE.putObject(waiter, WAITER_THREAD_OFFSET, newValue);
/*      */     }
/*      */ 
/*      */     
/*      */     void putNext(AbstractFuture.Waiter waiter, AbstractFuture.Waiter newValue) {
/* 1303 */       UNSAFE.putObject(waiter, WAITER_NEXT_OFFSET, newValue);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     boolean casWaiters(AbstractFuture<?> future, AbstractFuture.Waiter expect, AbstractFuture.Waiter update) {
/* 1309 */       return UNSAFE.compareAndSwapObject(future, WAITERS_OFFSET, expect, update);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     boolean casListeners(AbstractFuture<?> future, AbstractFuture.Listener expect, AbstractFuture.Listener update) {
/* 1315 */       return UNSAFE.compareAndSwapObject(future, LISTENERS_OFFSET, expect, update);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     boolean casValue(AbstractFuture<?> future, Object expect, Object update) {
/* 1321 */       return UNSAFE.compareAndSwapObject(future, VALUE_OFFSET, expect, update);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class SafeAtomicHelper
/*      */     extends AtomicHelper
/*      */   {
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture.Waiter, Thread> waiterThreadUpdater;
/*      */     
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture.Waiter, AbstractFuture.Waiter> waiterNextUpdater;
/*      */     
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Waiter> waitersUpdater;
/*      */     
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Listener> listenersUpdater;
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater;
/*      */     
/*      */     SafeAtomicHelper(AtomicReferenceFieldUpdater<AbstractFuture.Waiter, Thread> waiterThreadUpdater, AtomicReferenceFieldUpdater<AbstractFuture.Waiter, AbstractFuture.Waiter> waiterNextUpdater, AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Waiter> waitersUpdater, AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Listener> listenersUpdater, AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater) {
/* 1339 */       this.waiterThreadUpdater = waiterThreadUpdater;
/* 1340 */       this.waiterNextUpdater = waiterNextUpdater;
/* 1341 */       this.waitersUpdater = waitersUpdater;
/* 1342 */       this.listenersUpdater = listenersUpdater;
/* 1343 */       this.valueUpdater = valueUpdater;
/*      */     }
/*      */ 
/*      */     
/*      */     void putThread(AbstractFuture.Waiter waiter, Thread newValue) {
/* 1348 */       this.waiterThreadUpdater.lazySet(waiter, newValue);
/*      */     }
/*      */ 
/*      */     
/*      */     void putNext(AbstractFuture.Waiter waiter, AbstractFuture.Waiter newValue) {
/* 1353 */       this.waiterNextUpdater.lazySet(waiter, newValue);
/*      */     }
/*      */ 
/*      */     
/*      */     boolean casWaiters(AbstractFuture<?> future, AbstractFuture.Waiter expect, AbstractFuture.Waiter update) {
/* 1358 */       return this.waitersUpdater.compareAndSet(future, expect, update);
/*      */     }
/*      */ 
/*      */     
/*      */     boolean casListeners(AbstractFuture<?> future, AbstractFuture.Listener expect, AbstractFuture.Listener update) {
/* 1363 */       return this.listenersUpdater.compareAndSet(future, expect, update);
/*      */     }
/*      */ 
/*      */     
/*      */     boolean casValue(AbstractFuture<?> future, Object expect, Object update) {
/* 1368 */       return this.valueUpdater.compareAndSet(future, expect, update);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class SynchronizedHelper
/*      */     extends AtomicHelper
/*      */   {
/*      */     private SynchronizedHelper() {}
/*      */ 
/*      */     
/*      */     void putThread(AbstractFuture.Waiter waiter, Thread newValue) {
/* 1381 */       waiter.thread = newValue;
/*      */     }
/*      */ 
/*      */     
/*      */     void putNext(AbstractFuture.Waiter waiter, AbstractFuture.Waiter newValue) {
/* 1386 */       waiter.next = newValue;
/*      */     }
/*      */ 
/*      */     
/*      */     boolean casWaiters(AbstractFuture<?> future, AbstractFuture.Waiter expect, AbstractFuture.Waiter update) {
/* 1391 */       synchronized (future) {
/* 1392 */         if (future.waiters == expect) {
/* 1393 */           future.waiters = update;
/* 1394 */           return true;
/*      */         } 
/* 1396 */         return false;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     boolean casListeners(AbstractFuture<?> future, AbstractFuture.Listener expect, AbstractFuture.Listener update) {
/* 1402 */       synchronized (future) {
/* 1403 */         if (future.listeners == expect) {
/* 1404 */           future.listeners = update;
/* 1405 */           return true;
/*      */         } 
/* 1407 */         return false;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     boolean casValue(AbstractFuture<?> future, Object expect, Object update) {
/* 1413 */       synchronized (future) {
/* 1414 */         if (future.value == expect) {
/* 1415 */           future.value = update;
/* 1416 */           return true;
/*      */         } 
/* 1418 */         return false;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static CancellationException cancellationExceptionWithCause(String message, Throwable cause) {
/* 1425 */     CancellationException exception = new CancellationException(message);
/* 1426 */     exception.initCause(cause);
/* 1427 */     return exception;
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\commo\\util\concurrent\AbstractFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */