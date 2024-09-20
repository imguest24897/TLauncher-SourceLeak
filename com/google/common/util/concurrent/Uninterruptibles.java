/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Verify;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.time.Duration;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Uninterruptibles
/*     */ {
/*     */   @GwtIncompatible
/*     */   public static void awaitUninterruptibly(CountDownLatch latch) {
/*  56 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/*  60 */         latch.await();
/*     */         return;
/*  62 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/*  67 */         if (interrupted) {
/*  68 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   @Beta
/*     */   public static boolean awaitUninterruptibly(CountDownLatch latch, Duration timeout) {
/*  83 */     return awaitUninterruptibly(latch, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public static boolean awaitUninterruptibly(CountDownLatch latch, long timeout, TimeUnit unit) {
/*  94 */     boolean interrupted = false;
/*     */     try {
/*  96 */       long remainingNanos = unit.toNanos(timeout);
/*  97 */       long end = System.nanoTime() + remainingNanos;
/*     */ 
/*     */       
/*     */       while (true) {
/*     */         try {
/* 102 */           return latch.await(remainingNanos, TimeUnit.NANOSECONDS);
/* 103 */         } catch (InterruptedException e) {
/* 104 */           interrupted = true;
/* 105 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 109 */       if (interrupted) {
/* 110 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @Beta
/*     */   public static boolean awaitUninterruptibly(Condition condition, Duration timeout) {
/* 124 */     return awaitUninterruptibly(condition, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static boolean awaitUninterruptibly(Condition condition, long timeout, TimeUnit unit) {
/* 136 */     boolean interrupted = false;
/*     */     try {
/* 138 */       long remainingNanos = unit.toNanos(timeout);
/* 139 */       long end = System.nanoTime() + remainingNanos;
/*     */       
/*     */       while (true) {
/*     */         try {
/* 143 */           return condition.await(remainingNanos, TimeUnit.NANOSECONDS);
/* 144 */         } catch (InterruptedException e) {
/* 145 */           interrupted = true;
/* 146 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 150 */       if (interrupted) {
/* 151 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static void joinUninterruptibly(Thread toJoin) {
/* 159 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/* 163 */         toJoin.join();
/*     */         return;
/* 165 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/* 170 */         if (interrupted) {
/* 171 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @Beta
/*     */   public static void joinUninterruptibly(Thread toJoin, Duration timeout) {
/* 185 */     joinUninterruptibly(toJoin, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static void joinUninterruptibly(Thread toJoin, long timeout, TimeUnit unit) {
/* 195 */     Preconditions.checkNotNull(toJoin);
/* 196 */     boolean interrupted = false;
/*     */     try {
/* 198 */       long remainingNanos = unit.toNanos(timeout);
/* 199 */       long end = System.nanoTime() + remainingNanos;
/*     */       
/*     */       while (true) {
/*     */         try {
/* 203 */           TimeUnit.NANOSECONDS.timedJoin(toJoin, remainingNanos);
/*     */           return;
/* 205 */         } catch (InterruptedException e) {
/* 206 */           interrupted = true;
/* 207 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 211 */       if (interrupted) {
/* 212 */         Thread.currentThread().interrupt();
/*     */       }
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
/*     */   public static <V> V getUninterruptibly(Future<V> future) throws ExecutionException {
/* 236 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/* 240 */         return future.get();
/* 241 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/* 246 */         if (interrupted) {
/* 247 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       } 
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
/*     */ 
/*     */ 
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
/*     */   @GwtIncompatible
/*     */   @Beta
/*     */   public static <V> V getUninterruptibly(Future<V> future, Duration timeout) throws ExecutionException, TimeoutException {
/* 276 */     return getUninterruptibly(future, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
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
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public static <V> V getUninterruptibly(Future<V> future, long timeout, TimeUnit unit) throws ExecutionException, TimeoutException {
/* 302 */     boolean interrupted = false;
/*     */     try {
/* 304 */       long remainingNanos = unit.toNanos(timeout);
/* 305 */       long end = System.nanoTime() + remainingNanos;
/*     */ 
/*     */       
/*     */       while (true) {
/*     */         try {
/* 310 */           return future.get(remainingNanos, TimeUnit.NANOSECONDS);
/* 311 */         } catch (InterruptedException e) {
/* 312 */           interrupted = true;
/* 313 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 317 */       if (interrupted) {
/* 318 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <E> E takeUninterruptibly(BlockingQueue<E> queue) {
/* 326 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/* 330 */         return queue.take();
/* 331 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/* 336 */         if (interrupted) {
/* 337 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       } 
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
/*     */   @GwtIncompatible
/*     */   public static <E> void putUninterruptibly(BlockingQueue<E> queue, E element) {
/* 352 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/* 356 */         queue.put(element);
/*     */         return;
/* 358 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/* 363 */         if (interrupted) {
/* 364 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @Beta
/*     */   public static void sleepUninterruptibly(Duration sleepFor) {
/* 378 */     sleepUninterruptibly(Internal.toNanosSaturated(sleepFor), TimeUnit.NANOSECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static void sleepUninterruptibly(long sleepFor, TimeUnit unit) {
/* 386 */     boolean interrupted = false;
/*     */     try {
/* 388 */       long remainingNanos = unit.toNanos(sleepFor);
/* 389 */       long end = System.nanoTime() + remainingNanos;
/*     */       
/*     */       while (true) {
/*     */         try {
/* 393 */           TimeUnit.NANOSECONDS.sleep(remainingNanos);
/*     */           return;
/* 395 */         } catch (InterruptedException e) {
/* 396 */           interrupted = true;
/* 397 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 401 */       if (interrupted) {
/* 402 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @Beta
/*     */   public static boolean tryAcquireUninterruptibly(Semaphore semaphore, Duration timeout) {
/* 416 */     return tryAcquireUninterruptibly(semaphore, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
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
/*     */   @GwtIncompatible
/*     */   public static boolean tryAcquireUninterruptibly(Semaphore semaphore, long timeout, TimeUnit unit) {
/* 429 */     return tryAcquireUninterruptibly(semaphore, 1, timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @Beta
/*     */   public static boolean tryAcquireUninterruptibly(Semaphore semaphore, int permits, Duration timeout) {
/* 442 */     return tryAcquireUninterruptibly(semaphore, permits, 
/* 443 */         Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
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
/*     */   @GwtIncompatible
/*     */   public static boolean tryAcquireUninterruptibly(Semaphore semaphore, int permits, long timeout, TimeUnit unit) {
/* 456 */     boolean interrupted = false;
/*     */     try {
/* 458 */       long remainingNanos = unit.toNanos(timeout);
/* 459 */       long end = System.nanoTime() + remainingNanos;
/*     */ 
/*     */       
/*     */       while (true) {
/*     */         try {
/* 464 */           return semaphore.tryAcquire(permits, remainingNanos, TimeUnit.NANOSECONDS);
/* 465 */         } catch (InterruptedException e) {
/* 466 */           interrupted = true;
/* 467 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 471 */       if (interrupted) {
/* 472 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @Beta
/*     */   public static boolean tryLockUninterruptibly(Lock lock, Duration timeout) {
/* 486 */     return tryLockUninterruptibly(lock, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static boolean tryLockUninterruptibly(Lock lock, long timeout, TimeUnit unit) {
/* 498 */     boolean interrupted = false;
/*     */     try {
/* 500 */       long remainingNanos = unit.toNanos(timeout);
/* 501 */       long end = System.nanoTime() + remainingNanos;
/*     */       
/*     */       while (true) {
/*     */         try {
/* 505 */           return lock.tryLock(remainingNanos, TimeUnit.NANOSECONDS);
/* 506 */         } catch (InterruptedException e) {
/* 507 */           interrupted = true;
/* 508 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 512 */       if (interrupted) {
/* 513 */         Thread.currentThread().interrupt();
/*     */       }
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
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static void awaitTerminationUninterruptibly(ExecutorService executor) {
/* 528 */     Verify.verify(awaitTerminationUninterruptibly(executor, Long.MAX_VALUE, TimeUnit.NANOSECONDS));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static boolean awaitTerminationUninterruptibly(ExecutorService executor, Duration timeout) {
/* 541 */     return awaitTerminationUninterruptibly(executor, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
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
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static boolean awaitTerminationUninterruptibly(ExecutorService executor, long timeout, TimeUnit unit) {
/* 555 */     boolean interrupted = false;
/*     */     try {
/* 557 */       long remainingNanos = unit.toNanos(timeout);
/* 558 */       long end = System.nanoTime() + remainingNanos;
/*     */       
/*     */       while (true) {
/*     */         try {
/* 562 */           return executor.awaitTermination(remainingNanos, TimeUnit.NANOSECONDS);
/* 563 */         } catch (InterruptedException e) {
/* 564 */           interrupted = true;
/* 565 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 569 */       if (interrupted)
/* 570 */         Thread.currentThread().interrupt(); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\commo\\util\concurrent\Uninterruptibles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */