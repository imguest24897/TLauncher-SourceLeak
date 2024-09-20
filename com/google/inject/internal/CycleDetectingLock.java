/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableListMultimap;
/*     */ import com.google.common.collect.LinkedHashMultimap;
/*     */ import com.google.common.collect.ListMultimap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.common.collect.MultimapBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ interface CycleDetectingLock<ID>
/*     */ {
/*     */   ListMultimap<Thread, ID> lockOrDetectPotentialLocksCycle();
/*     */   
/*     */   void unlock();
/*     */   
/*     */   public static class CycleDetectingLockFactory<ID>
/*     */   {
/*  81 */     private static Map<Thread, ReentrantCycleDetectingLock<?>> lockThreadIsWaitingOn = Maps.newHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     private static final Multimap<Thread, ReentrantCycleDetectingLock<?>> locksOwnedByThread = (Multimap<Thread, ReentrantCycleDetectingLock<?>>)LinkedHashMultimap.create();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     CycleDetectingLock<ID> create(ID userLockId) {
/* 111 */       return new ReentrantCycleDetectingLock<>(this, userLockId, new ReentrantLock());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     static class ReentrantCycleDetectingLock<ID>
/*     */       implements CycleDetectingLock<ID>
/*     */     {
/*     */       private final Lock lockImplementation;
/*     */ 
/*     */       
/*     */       private final ID userLockId;
/*     */       
/*     */       private final CycleDetectingLock.CycleDetectingLockFactory<ID> lockFactory;
/*     */       
/* 126 */       private Thread lockOwnerThread = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 132 */       private int lockReentranceCount = 0;
/*     */ 
/*     */       
/*     */       ReentrantCycleDetectingLock(CycleDetectingLock.CycleDetectingLockFactory<ID> lockFactory, ID userLockId, Lock lockImplementation) {
/* 136 */         this.lockFactory = lockFactory;
/* 137 */         this.userLockId = (ID)Preconditions.checkNotNull(userLockId, "userLockId");
/* 138 */         this
/* 139 */           .lockImplementation = (Lock)Preconditions.checkNotNull(lockImplementation, "lockImplementation");
/*     */       }
/*     */ 
/*     */       
/*     */       public ListMultimap<Thread, ID> lockOrDetectPotentialLocksCycle() {
/* 144 */         Thread currentThread = Thread.currentThread();
/* 145 */         synchronized (CycleDetectingLock.CycleDetectingLockFactory.class) {
/* 146 */           checkState();
/*     */           
/* 148 */           CycleDetectingLock.CycleDetectingLockFactory.lockThreadIsWaitingOn.put(currentThread, this);
/* 149 */           ListMultimap<Thread, ID> locksInCycle = detectPotentialLocksCycle();
/* 150 */           if (!locksInCycle.isEmpty()) {
/*     */             
/* 152 */             CycleDetectingLock.CycleDetectingLockFactory.lockThreadIsWaitingOn.remove(currentThread);
/*     */             
/* 154 */             return locksInCycle;
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 159 */         this.lockImplementation.lock();
/*     */         
/* 161 */         synchronized (CycleDetectingLock.CycleDetectingLockFactory.class) {
/*     */           
/* 163 */           CycleDetectingLock.CycleDetectingLockFactory.lockThreadIsWaitingOn.remove(currentThread);
/* 164 */           checkState();
/*     */ 
/*     */           
/* 167 */           this.lockOwnerThread = currentThread;
/* 168 */           this.lockReentranceCount++;
/*     */           
/* 170 */           CycleDetectingLock.CycleDetectingLockFactory.locksOwnedByThread.put(currentThread, this);
/*     */         } 
/*     */         
/* 173 */         return (ListMultimap<Thread, ID>)ImmutableListMultimap.of();
/*     */       }
/*     */ 
/*     */       
/*     */       public void unlock() {
/* 178 */         Thread currentThread = Thread.currentThread();
/* 179 */         synchronized (CycleDetectingLock.CycleDetectingLockFactory.class) {
/* 180 */           checkState();
/* 181 */           Preconditions.checkState((this.lockOwnerThread != null), "Thread is trying to unlock a lock that is not locked");
/*     */           
/* 183 */           Preconditions.checkState((this.lockOwnerThread == currentThread), "Thread is trying to unlock a lock owned by another thread");
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 188 */           this.lockImplementation.unlock();
/*     */ 
/*     */           
/* 191 */           this.lockReentranceCount--;
/* 192 */           if (this.lockReentranceCount == 0) {
/*     */             
/* 194 */             this.lockOwnerThread = null;
/* 195 */             Preconditions.checkState(CycleDetectingLock.CycleDetectingLockFactory
/* 196 */                 .locksOwnedByThread.remove(currentThread, this), "Internal error: Can not find this lock in locks owned by a current thread");
/*     */             
/* 198 */             if (CycleDetectingLock.CycleDetectingLockFactory.locksOwnedByThread.get(currentThread).isEmpty())
/*     */             {
/* 200 */               CycleDetectingLock.CycleDetectingLockFactory.locksOwnedByThread.removeAll(currentThread);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       void checkState() throws IllegalStateException {
/* 208 */         Thread currentThread = Thread.currentThread();
/* 209 */         Preconditions.checkState(
/* 210 */             !CycleDetectingLock.CycleDetectingLockFactory.lockThreadIsWaitingOn.containsKey(currentThread), "Internal error: Thread should not be in a waiting thread on a lock now");
/*     */         
/* 212 */         if (this.lockOwnerThread != null) {
/*     */           
/* 214 */           Preconditions.checkState((this.lockReentranceCount >= 0), "Internal error: Lock ownership and reentrance count internal states do not match");
/*     */ 
/*     */           
/* 217 */           Preconditions.checkState(CycleDetectingLock.CycleDetectingLockFactory
/* 218 */               .locksOwnedByThread.get(this.lockOwnerThread).contains(this), "Internal error: Set of locks owned by a current thread and lock ownership status do not match");
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 223 */           Preconditions.checkState((this.lockReentranceCount == 0), "Internal error: Reentrance count of a non locked lock is expect to be zero");
/*     */ 
/*     */           
/* 226 */           Preconditions.checkState(
/* 227 */               !CycleDetectingLock.CycleDetectingLockFactory.locksOwnedByThread.values().contains(this), "Internal error: Non locked lock should not be owned by any thread");
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       private ListMultimap<Thread, ID> detectPotentialLocksCycle() {
/* 241 */         Thread currentThread = Thread.currentThread();
/* 242 */         if (this.lockOwnerThread == null || this.lockOwnerThread == currentThread)
/*     */         {
/*     */           
/* 245 */           return (ListMultimap<Thread, ID>)ImmutableListMultimap.of();
/*     */         }
/*     */ 
/*     */         
/* 249 */         ListMultimap<Thread, ID> potentialLocksCycle = MultimapBuilder.linkedHashKeys().arrayListValues().build();
/*     */         
/* 251 */         ReentrantCycleDetectingLock<?> lockOwnerWaitingOn = this;
/*     */         
/* 253 */         while (lockOwnerWaitingOn != null && lockOwnerWaitingOn.lockOwnerThread != null) {
/* 254 */           Thread threadOwnerThreadWaits = lockOwnerWaitingOn.lockOwnerThread;
/*     */ 
/*     */           
/* 257 */           lockOwnerWaitingOn = addAllLockIdsAfter(threadOwnerThreadWaits, lockOwnerWaitingOn, potentialLocksCycle);
/* 258 */           if (threadOwnerThreadWaits == currentThread)
/*     */           {
/* 260 */             return potentialLocksCycle;
/*     */           }
/*     */         } 
/*     */         
/* 264 */         return (ListMultimap<Thread, ID>)ImmutableListMultimap.of();
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       private ReentrantCycleDetectingLock<?> addAllLockIdsAfter(Thread thread, ReentrantCycleDetectingLock<?> lock, ListMultimap<Thread, ID> potentialLocksCycle) {
/* 275 */         boolean found = false;
/* 276 */         Collection<ReentrantCycleDetectingLock<?>> ownedLocks = CycleDetectingLock.CycleDetectingLockFactory.locksOwnedByThread.get(thread);
/* 277 */         Preconditions.checkNotNull(ownedLocks, "Internal error: No locks were found taken by a thread");
/*     */         
/* 279 */         for (ReentrantCycleDetectingLock<?> ownedLock : ownedLocks) {
/* 280 */           if (ownedLock == lock) {
/* 281 */             found = true;
/*     */           }
/* 283 */           if (found && ownedLock.lockFactory == this.lockFactory) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 289 */             ID userLockId = ownedLock.userLockId;
/* 290 */             potentialLocksCycle.put(thread, userLockId);
/*     */           } 
/*     */         } 
/* 293 */         Preconditions.checkState(found, "Internal error: We can not find locks that created a cycle that we detected");
/*     */         
/* 295 */         ReentrantCycleDetectingLock<?> unownedLock = (ReentrantCycleDetectingLock)CycleDetectingLock.CycleDetectingLockFactory.lockThreadIsWaitingOn.get(thread);
/*     */         
/* 297 */         if (unownedLock != null && unownedLock.lockFactory == this.lockFactory) {
/*     */           
/* 299 */           ID typed = unownedLock.userLockId;
/* 300 */           potentialLocksCycle.put(thread, typed);
/*     */         } 
/* 302 */         return unownedLock;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public String toString() {
/* 309 */         Thread thread = this.lockOwnerThread;
/* 310 */         if (thread != null) {
/* 311 */           return String.format("%s[%s][locked by %s]", new Object[] { super.toString(), this.userLockId, thread });
/*     */         }
/* 313 */         return String.format("%s[%s][unlocked]", new Object[] { super.toString(), this.userLockId });
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\CycleDetectingLock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */