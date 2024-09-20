/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.j2objc.annotations.ReflectionSupport;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ @ReflectionSupport(ReflectionSupport.Level.FULL)
/*     */ abstract class AggregateFutureState<OutputT>
/*     */   extends AbstractFuture.TrustedFuture<OutputT>
/*     */ {
/*     */   static {
/*     */     AtomicHelper helper;
/*     */   }
/*     */   
/*  43 */   private volatile Set<Throwable> seenExceptions = null;
/*     */   
/*     */   private volatile int remaining;
/*     */   
/*     */   private static final AtomicHelper ATOMIC_HELPER;
/*     */   
/*  49 */   private static final Logger log = Logger.getLogger(AggregateFutureState.class.getName());
/*     */ 
/*     */   
/*     */   static {
/*  53 */     Throwable thrownReflectionFailure = null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  58 */       helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(AggregateFutureState.class, Set.class, "seenExceptions"), AtomicIntegerFieldUpdater.newUpdater(AggregateFutureState.class, "remaining"));
/*  59 */     } catch (Throwable reflectionFailure) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  64 */       thrownReflectionFailure = reflectionFailure;
/*  65 */       helper = new SynchronizedAtomicHelper();
/*     */     } 
/*  67 */     ATOMIC_HELPER = helper;
/*     */ 
/*     */     
/*  70 */     if (thrownReflectionFailure != null) {
/*  71 */       log.log(Level.SEVERE, "SafeAtomicHelper is broken!", thrownReflectionFailure);
/*     */     }
/*     */   }
/*     */   
/*     */   AggregateFutureState(int remainingFutures) {
/*  76 */     this.remaining = remainingFutures;
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
/*     */   final Set<Throwable> getOrInitSeenExceptions() {
/*  96 */     Set<Throwable> seenExceptionsLocal = this.seenExceptions;
/*  97 */     if (seenExceptionsLocal == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 113 */       seenExceptionsLocal = Sets.newConcurrentHashSet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 119 */       addInitialException(seenExceptionsLocal);
/*     */       
/* 121 */       ATOMIC_HELPER.compareAndSetSeenExceptions(this, null, seenExceptionsLocal);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 128 */       seenExceptionsLocal = this.seenExceptions;
/*     */     } 
/* 130 */     return seenExceptionsLocal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final int decrementRemainingAndGet() {
/* 137 */     return ATOMIC_HELPER.decrementAndGetRemainingCount(this);
/*     */   }
/*     */   
/*     */   final void clearSeenExceptions() {
/* 141 */     this.seenExceptions = null;
/*     */   }
/*     */   
/*     */   abstract void addInitialException(Set<Throwable> paramSet);
/*     */   
/*     */   private static abstract class AtomicHelper
/*     */   {
/*     */     private AtomicHelper() {}
/*     */     
/*     */     abstract void compareAndSetSeenExceptions(AggregateFutureState param1AggregateFutureState, Set<Throwable> param1Set1, Set<Throwable> param1Set2);
/*     */     
/*     */     abstract int decrementAndGetRemainingCount(AggregateFutureState param1AggregateFutureState);
/*     */   }
/*     */   
/*     */   private static final class SafeAtomicHelper
/*     */     extends AtomicHelper {
/*     */     final AtomicReferenceFieldUpdater<AggregateFutureState, Set<Throwable>> seenExceptionsUpdater;
/*     */     final AtomicIntegerFieldUpdater<AggregateFutureState> remainingCountUpdater;
/*     */     
/*     */     SafeAtomicHelper(AtomicReferenceFieldUpdater<AggregateFutureState, Set<Throwable>> seenExceptionsUpdater, AtomicIntegerFieldUpdater<AggregateFutureState> remainingCountUpdater) {
/* 161 */       this.seenExceptionsUpdater = seenExceptionsUpdater;
/* 162 */       this.remainingCountUpdater = remainingCountUpdater;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void compareAndSetSeenExceptions(AggregateFutureState state, Set<Throwable> expect, Set<Throwable> update) {
/* 168 */       this.seenExceptionsUpdater.compareAndSet(state, expect, update);
/*     */     }
/*     */ 
/*     */     
/*     */     int decrementAndGetRemainingCount(AggregateFutureState state) {
/* 173 */       return this.remainingCountUpdater.decrementAndGet(state);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SynchronizedAtomicHelper extends AtomicHelper {
/*     */     private SynchronizedAtomicHelper() {}
/*     */     
/*     */     void compareAndSetSeenExceptions(AggregateFutureState state, Set<Throwable> expect, Set<Throwable> update) {
/* 181 */       synchronized (state) {
/* 182 */         if (state.seenExceptions == expect) {
/* 183 */           state.seenExceptions = update;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     int decrementAndGetRemainingCount(AggregateFutureState state) {
/* 190 */       synchronized (state) {
/* 191 */         return --state.remaining;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\commo\\util\concurrent\AggregateFutureState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */