/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.time.Duration;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Collection;
/*     */ import java.util.Deque;
/*     */ import java.util.PriorityQueue;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ArrayBlockingQueue;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.LinkedBlockingDeque;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.PriorityBlockingQueue;
/*     */ import java.util.concurrent.SynchronousQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Queues
/*     */ {
/*     */   @GwtIncompatible
/*     */   public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(int capacity) {
/*  55 */     return new ArrayBlockingQueue<>(capacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ArrayDeque<E> newArrayDeque() {
/*  66 */     return new ArrayDeque<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ArrayDeque<E> newArrayDeque(Iterable<? extends E> elements) {
/*  76 */     if (elements instanceof Collection) {
/*  77 */       return new ArrayDeque<>((Collection<? extends E>)elements);
/*     */     }
/*  79 */     ArrayDeque<E> deque = new ArrayDeque<>();
/*  80 */     Iterables.addAll(deque, elements);
/*  81 */     return deque;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue() {
/*  89 */     return new ConcurrentLinkedQueue<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue(Iterable<? extends E> elements) {
/*  99 */     if (elements instanceof Collection) {
/* 100 */       return new ConcurrentLinkedQueue<>((Collection<? extends E>)elements);
/*     */     }
/* 102 */     ConcurrentLinkedQueue<E> queue = new ConcurrentLinkedQueue<>();
/* 103 */     Iterables.addAll(queue, elements);
/* 104 */     return queue;
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
/*     */   public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque() {
/* 116 */     return new LinkedBlockingDeque<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(int capacity) {
/* 127 */     return new LinkedBlockingDeque<>(capacity);
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
/*     */   public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(Iterable<? extends E> elements) {
/* 139 */     if (elements instanceof Collection) {
/* 140 */       return new LinkedBlockingDeque<>((Collection<? extends E>)elements);
/*     */     }
/* 142 */     LinkedBlockingDeque<E> deque = new LinkedBlockingDeque<>();
/* 143 */     Iterables.addAll(deque, elements);
/* 144 */     return deque;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue() {
/* 152 */     return new LinkedBlockingQueue<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(int capacity) {
/* 162 */     return new LinkedBlockingQueue<>(capacity);
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
/*     */   public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(Iterable<? extends E> elements) {
/* 175 */     if (elements instanceof Collection) {
/* 176 */       return new LinkedBlockingQueue<>((Collection<? extends E>)elements);
/*     */     }
/* 178 */     LinkedBlockingQueue<E> queue = new LinkedBlockingQueue<>();
/* 179 */     Iterables.addAll(queue, elements);
/* 180 */     return queue;
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
/*     */   @GwtIncompatible
/*     */   public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue() {
/* 195 */     return new PriorityBlockingQueue<>();
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
/*     */   @GwtIncompatible
/*     */   public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue(Iterable<? extends E> elements) {
/* 209 */     if (elements instanceof Collection) {
/* 210 */       return new PriorityBlockingQueue<>((Collection<? extends E>)elements);
/*     */     }
/* 212 */     PriorityBlockingQueue<E> queue = new PriorityBlockingQueue<>();
/* 213 */     Iterables.addAll(queue, elements);
/* 214 */     return queue;
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
/*     */   public static <E extends Comparable> PriorityQueue<E> newPriorityQueue() {
/* 226 */     return new PriorityQueue<>();
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
/*     */   public static <E extends Comparable> PriorityQueue<E> newPriorityQueue(Iterable<? extends E> elements) {
/* 239 */     if (elements instanceof Collection) {
/* 240 */       return new PriorityQueue<>((Collection<? extends E>)elements);
/*     */     }
/* 242 */     PriorityQueue<E> queue = new PriorityQueue<>();
/* 243 */     Iterables.addAll(queue, elements);
/* 244 */     return queue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <E> SynchronousQueue<E> newSynchronousQueue() {
/* 252 */     return new SynchronousQueue<>();
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
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public static <E> int drain(BlockingQueue<E> q, Collection<? super E> buffer, int numElements, Duration timeout) throws InterruptedException {
/* 274 */     return drain(q, buffer, numElements, timeout.toNanos(), TimeUnit.NANOSECONDS);
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
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public static <E> int drain(BlockingQueue<E> q, Collection<? super E> buffer, int numElements, long timeout, TimeUnit unit) throws InterruptedException {
/* 300 */     Preconditions.checkNotNull(buffer);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 306 */     long deadline = System.nanoTime() + unit.toNanos(timeout);
/* 307 */     int added = 0;
/* 308 */     while (added < numElements) {
/*     */ 
/*     */       
/* 311 */       added += q.drainTo(buffer, numElements - added);
/* 312 */       if (added < numElements) {
/* 313 */         E e = q.poll(deadline - System.nanoTime(), TimeUnit.NANOSECONDS);
/* 314 */         if (e == null) {
/*     */           break;
/*     */         }
/* 317 */         buffer.add(e);
/* 318 */         added++;
/*     */       } 
/*     */     } 
/* 321 */     return added;
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
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public static <E> int drainUninterruptibly(BlockingQueue<E> q, Collection<? super E> buffer, int numElements, Duration timeout) {
/* 346 */     return drainUninterruptibly(q, buffer, numElements, timeout.toNanos(), TimeUnit.NANOSECONDS);
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
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public static <E> int drainUninterruptibly(BlockingQueue<E> q, Collection<? super E> buffer, int numElements, long timeout, TimeUnit unit) {
/* 372 */     Preconditions.checkNotNull(buffer);
/* 373 */     long deadline = System.nanoTime() + unit.toNanos(timeout);
/* 374 */     int added = 0;
/* 375 */     boolean interrupted = false;
/*     */     try {
/* 377 */       while (added < numElements) {
/*     */ 
/*     */         
/* 380 */         added += q.drainTo(buffer, numElements - added);
/* 381 */         if (added < numElements) {
/*     */           E e;
/*     */           while (true) {
/*     */             try {
/* 385 */               e = q.poll(deadline - System.nanoTime(), TimeUnit.NANOSECONDS);
/*     */               break;
/* 387 */             } catch (InterruptedException ex) {
/* 388 */               interrupted = true;
/*     */             } 
/*     */           } 
/* 391 */           if (e == null) {
/*     */             break;
/*     */           }
/* 394 */           buffer.add(e);
/* 395 */           added++;
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 399 */       if (interrupted) {
/* 400 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } 
/* 403 */     return added;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Queue<E> synchronizedQueue(Queue<E> queue) {
/* 436 */     return Synchronized.queue(queue, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Deque<E> synchronizedDeque(Deque<E> deque) {
/* 469 */     return Synchronized.deque(deque, null);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\Queues.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */