/*      */ package com.google.common.util.concurrent;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.primitives.Longs;
/*      */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*      */ import com.google.j2objc.annotations.Weak;
/*      */ import java.time.Duration;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.locks.Condition;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import java.util.function.BooleanSupplier;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Beta
/*      */ @GwtIncompatible
/*      */ public final class Monitor
/*      */ {
/*      */   private final boolean fair;
/*      */   private final ReentrantLock lock;
/*      */   
/*      */   @Beta
/*      */   public static abstract class Guard
/*      */   {
/*      */     @Weak
/*      */     final Monitor monitor;
/*      */     final Condition condition;
/*      */     @GuardedBy("monitor.lock")
/*  312 */     int waiterCount = 0;
/*      */ 
/*      */     
/*      */     @GuardedBy("monitor.lock")
/*      */     Guard next;
/*      */ 
/*      */ 
/*      */     
/*      */     protected Guard(Monitor monitor) {
/*  321 */       this.monitor = (Monitor)Preconditions.checkNotNull(monitor, "monitor");
/*  322 */       this.condition = monitor.lock.newCondition();
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
/*      */     public abstract boolean isSatisfied();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*  343 */   private Guard activeGuards = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Monitor() {
/*  351 */     this(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Monitor(boolean fair) {
/*  361 */     this.fair = fair;
/*  362 */     this.lock = new ReentrantLock(fair);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Guard newGuard(final BooleanSupplier isSatisfied) {
/*  373 */     Preconditions.checkNotNull(isSatisfied, "isSatisfied");
/*  374 */     return new Guard(this, this)
/*      */       {
/*      */         public boolean isSatisfied() {
/*  377 */           return isSatisfied.getAsBoolean();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   public void enter() {
/*  384 */     this.lock.lock();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enter(Duration time) {
/*  394 */     return enter(Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enter(long time, TimeUnit unit) {
/*  404 */     long timeoutNanos = toSafeNanos(time, unit);
/*  405 */     ReentrantLock lock = this.lock;
/*  406 */     if (!this.fair && lock.tryLock()) {
/*  407 */       return true;
/*      */     }
/*  409 */     boolean interrupted = Thread.interrupted();
/*      */     try {
/*  411 */       long startTime = System.nanoTime();
/*  412 */       long remainingNanos = timeoutNanos; while (true) {
/*      */         try {
/*  414 */           return lock.tryLock(remainingNanos, TimeUnit.NANOSECONDS);
/*  415 */         } catch (InterruptedException interrupt) {
/*  416 */           interrupted = true;
/*  417 */           remainingNanos = remainingNanos(startTime, timeoutNanos);
/*      */         } 
/*      */       } 
/*      */     } finally {
/*  421 */       if (interrupted) {
/*  422 */         Thread.currentThread().interrupt();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enterInterruptibly() throws InterruptedException {
/*  433 */     this.lock.lockInterruptibly();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enterInterruptibly(Duration time) throws InterruptedException {
/*  444 */     return enterInterruptibly(Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enterInterruptibly(long time, TimeUnit unit) throws InterruptedException {
/*  455 */     return this.lock.tryLock(time, unit);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean tryEnter() {
/*  466 */     return this.lock.tryLock();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enterWhen(Guard guard) throws InterruptedException {
/*  475 */     if (guard.monitor != this) {
/*  476 */       throw new IllegalMonitorStateException();
/*      */     }
/*  478 */     ReentrantLock lock = this.lock;
/*  479 */     boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
/*  480 */     lock.lockInterruptibly();
/*      */     
/*  482 */     boolean satisfied = false;
/*      */     try {
/*  484 */       if (!guard.isSatisfied()) {
/*  485 */         await(guard, signalBeforeWaiting);
/*      */       }
/*  487 */       satisfied = true;
/*      */     } finally {
/*  489 */       if (!satisfied) {
/*  490 */         leave();
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
/*      */   public boolean enterWhen(Guard guard, Duration time) throws InterruptedException {
/*  505 */     return enterWhen(guard, Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS);
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
/*      */   public boolean enterWhen(Guard guard, long time, TimeUnit unit) throws InterruptedException {
/*      */     // Byte code:
/*      */     //   0: lload_2
/*      */     //   1: aload #4
/*      */     //   3: invokestatic toSafeNanos : (JLjava/util/concurrent/TimeUnit;)J
/*      */     //   6: lstore #5
/*      */     //   8: aload_1
/*      */     //   9: getfield monitor : Lcom/google/common/util/concurrent/Monitor;
/*      */     //   12: aload_0
/*      */     //   13: if_acmpeq -> 24
/*      */     //   16: new java/lang/IllegalMonitorStateException
/*      */     //   19: dup
/*      */     //   20: invokespecial <init> : ()V
/*      */     //   23: athrow
/*      */     //   24: aload_0
/*      */     //   25: getfield lock : Ljava/util/concurrent/locks/ReentrantLock;
/*      */     //   28: astore #7
/*      */     //   30: aload #7
/*      */     //   32: invokevirtual isHeldByCurrentThread : ()Z
/*      */     //   35: istore #8
/*      */     //   37: lconst_0
/*      */     //   38: lstore #9
/*      */     //   40: aload_0
/*      */     //   41: getfield fair : Z
/*      */     //   44: ifne -> 72
/*      */     //   47: invokestatic interrupted : ()Z
/*      */     //   50: ifeq -> 61
/*      */     //   53: new java/lang/InterruptedException
/*      */     //   56: dup
/*      */     //   57: invokespecial <init> : ()V
/*      */     //   60: athrow
/*      */     //   61: aload #7
/*      */     //   63: invokevirtual tryLock : ()Z
/*      */     //   66: ifeq -> 72
/*      */     //   69: goto -> 92
/*      */     //   72: lload #5
/*      */     //   74: invokestatic initNanoTime : (J)J
/*      */     //   77: lstore #9
/*      */     //   79: aload #7
/*      */     //   81: lload_2
/*      */     //   82: aload #4
/*      */     //   84: invokevirtual tryLock : (JLjava/util/concurrent/TimeUnit;)Z
/*      */     //   87: ifne -> 92
/*      */     //   90: iconst_0
/*      */     //   91: ireturn
/*      */     //   92: iconst_0
/*      */     //   93: istore #11
/*      */     //   95: iconst_1
/*      */     //   96: istore #12
/*      */     //   98: aload_1
/*      */     //   99: invokevirtual isSatisfied : ()Z
/*      */     //   102: ifne -> 134
/*      */     //   105: aload_0
/*      */     //   106: aload_1
/*      */     //   107: lload #9
/*      */     //   109: lconst_0
/*      */     //   110: lcmp
/*      */     //   111: ifne -> 119
/*      */     //   114: lload #5
/*      */     //   116: goto -> 126
/*      */     //   119: lload #9
/*      */     //   121: lload #5
/*      */     //   123: invokestatic remainingNanos : (JJ)J
/*      */     //   126: iload #8
/*      */     //   128: invokespecial awaitNanos : (Lcom/google/common/util/concurrent/Monitor$Guard;JZ)Z
/*      */     //   131: ifeq -> 138
/*      */     //   134: iconst_1
/*      */     //   135: goto -> 139
/*      */     //   138: iconst_0
/*      */     //   139: istore #11
/*      */     //   141: iconst_0
/*      */     //   142: istore #12
/*      */     //   144: iload #11
/*      */     //   146: istore #13
/*      */     //   148: iload #11
/*      */     //   150: ifne -> 185
/*      */     //   153: iload #12
/*      */     //   155: ifeq -> 167
/*      */     //   158: iload #8
/*      */     //   160: ifne -> 167
/*      */     //   163: aload_0
/*      */     //   164: invokespecial signalNextWaiter : ()V
/*      */     //   167: aload #7
/*      */     //   169: invokevirtual unlock : ()V
/*      */     //   172: goto -> 185
/*      */     //   175: astore #14
/*      */     //   177: aload #7
/*      */     //   179: invokevirtual unlock : ()V
/*      */     //   182: aload #14
/*      */     //   184: athrow
/*      */     //   185: iload #13
/*      */     //   187: ireturn
/*      */     //   188: astore #15
/*      */     //   190: iload #11
/*      */     //   192: ifne -> 227
/*      */     //   195: iload #12
/*      */     //   197: ifeq -> 209
/*      */     //   200: iload #8
/*      */     //   202: ifne -> 209
/*      */     //   205: aload_0
/*      */     //   206: invokespecial signalNextWaiter : ()V
/*      */     //   209: aload #7
/*      */     //   211: invokevirtual unlock : ()V
/*      */     //   214: goto -> 227
/*      */     //   217: astore #16
/*      */     //   219: aload #7
/*      */     //   221: invokevirtual unlock : ()V
/*      */     //   224: aload #16
/*      */     //   226: athrow
/*      */     //   227: aload #15
/*      */     //   229: athrow
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #518	-> 0
/*      */     //   #519	-> 8
/*      */     //   #520	-> 16
/*      */     //   #522	-> 24
/*      */     //   #523	-> 30
/*      */     //   #524	-> 37
/*      */     //   #528	-> 40
/*      */     //   #530	-> 47
/*      */     //   #531	-> 53
/*      */     //   #533	-> 61
/*      */     //   #534	-> 69
/*      */     //   #537	-> 72
/*      */     //   #538	-> 79
/*      */     //   #539	-> 90
/*      */     //   #543	-> 92
/*      */     //   #544	-> 95
/*      */     //   #546	-> 98
/*      */     //   #547	-> 99
/*      */     //   #550	-> 107
/*      */     //   #548	-> 128
/*      */     //   #552	-> 141
/*      */     //   #553	-> 144
/*      */     //   #555	-> 148
/*      */     //   #558	-> 153
/*      */     //   #559	-> 163
/*      */     //   #562	-> 167
/*      */     //   #563	-> 172
/*      */     //   #562	-> 175
/*      */     //   #563	-> 182
/*      */     //   #553	-> 185
/*      */     //   #555	-> 188
/*      */     //   #558	-> 195
/*      */     //   #559	-> 205
/*      */     //   #562	-> 209
/*      */     //   #563	-> 214
/*      */     //   #562	-> 217
/*      */     //   #563	-> 224
/*      */     //   #565	-> 227
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	230	0	this	Lcom/google/common/util/concurrent/Monitor;
/*      */     //   0	230	1	guard	Lcom/google/common/util/concurrent/Monitor$Guard;
/*      */     //   0	230	2	time	J
/*      */     //   0	230	4	unit	Ljava/util/concurrent/TimeUnit;
/*      */     //   8	222	5	timeoutNanos	J
/*      */     //   30	200	7	lock	Ljava/util/concurrent/locks/ReentrantLock;
/*      */     //   37	193	8	reentrant	Z
/*      */     //   40	190	9	startTime	J
/*      */     //   95	135	11	satisfied	Z
/*      */     //   98	132	12	threw	Z
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   98	148	188	finally
/*      */     //   153	167	175	finally
/*      */     //   175	177	175	finally
/*      */     //   188	190	188	finally
/*      */     //   195	209	217	finally
/*      */     //   217	219	217	finally
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
/*      */   public void enterWhenUninterruptibly(Guard guard) {
/*  570 */     if (guard.monitor != this) {
/*  571 */       throw new IllegalMonitorStateException();
/*      */     }
/*  573 */     ReentrantLock lock = this.lock;
/*  574 */     boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
/*  575 */     lock.lock();
/*      */     
/*  577 */     boolean satisfied = false;
/*      */     try {
/*  579 */       if (!guard.isSatisfied()) {
/*  580 */         awaitUninterruptibly(guard, signalBeforeWaiting);
/*      */       }
/*  582 */       satisfied = true;
/*      */     } finally {
/*  584 */       if (!satisfied) {
/*  585 */         leave();
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
/*      */   public boolean enterWhenUninterruptibly(Guard guard, Duration time) {
/*  598 */     return enterWhenUninterruptibly(guard, Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enterWhenUninterruptibly(Guard guard, long time, TimeUnit unit) {
/*  609 */     long timeoutNanos = toSafeNanos(time, unit);
/*  610 */     if (guard.monitor != this) {
/*  611 */       throw new IllegalMonitorStateException();
/*      */     }
/*  613 */     ReentrantLock lock = this.lock;
/*  614 */     long startTime = 0L;
/*  615 */     boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
/*  616 */     boolean interrupted = Thread.interrupted();
/*      */     try {
/*  618 */       if (this.fair || !lock.tryLock()) {
/*  619 */         startTime = initNanoTime(timeoutNanos);
/*  620 */         long remainingNanos = timeoutNanos; while (true) {
/*      */           try {
/*  622 */             if (lock.tryLock(remainingNanos, TimeUnit.NANOSECONDS)) {
/*      */               break;
/*      */             }
/*  625 */             return false;
/*      */           }
/*  627 */           catch (InterruptedException interrupt) {
/*  628 */             interrupted = true;
/*  629 */             remainingNanos = remainingNanos(startTime, timeoutNanos);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  634 */       boolean satisfied = false;
/*      */       
/*      */       while (true) {
/*      */         try {
/*  638 */           if (guard.isSatisfied()) {
/*  639 */             satisfied = true;
/*      */           } else {
/*      */             long remainingNanos;
/*  642 */             if (startTime == 0L) {
/*  643 */               startTime = initNanoTime(timeoutNanos);
/*  644 */               remainingNanos = timeoutNanos;
/*      */             } else {
/*  646 */               remainingNanos = remainingNanos(startTime, timeoutNanos);
/*      */             } 
/*  648 */             satisfied = awaitNanos(guard, remainingNanos, signalBeforeWaiting);
/*      */           } 
/*  650 */           return satisfied;
/*  651 */         } catch (InterruptedException interrupt) {
/*  652 */           interrupted = true;
/*      */         
/*      */         }
/*      */         finally {
/*      */           
/*  657 */           if (!satisfied)
/*  658 */             lock.unlock(); 
/*      */         } 
/*      */       } 
/*      */     } finally {
/*  662 */       if (interrupted) {
/*  663 */         Thread.currentThread().interrupt();
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
/*      */   public boolean enterIf(Guard guard) {
/*  675 */     if (guard.monitor != this) {
/*  676 */       throw new IllegalMonitorStateException();
/*      */     }
/*  678 */     ReentrantLock lock = this.lock;
/*  679 */     lock.lock();
/*      */     
/*  681 */     boolean satisfied = false;
/*      */     try {
/*  683 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  685 */       if (!satisfied) {
/*  686 */         lock.unlock();
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
/*      */   public boolean enterIf(Guard guard, Duration time) {
/*  699 */     return enterIf(guard, Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enterIf(Guard guard, long time, TimeUnit unit) {
/*  710 */     if (guard.monitor != this) {
/*  711 */       throw new IllegalMonitorStateException();
/*      */     }
/*  713 */     if (!enter(time, unit)) {
/*  714 */       return false;
/*      */     }
/*      */     
/*  717 */     boolean satisfied = false;
/*      */     try {
/*  719 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  721 */       if (!satisfied) {
/*  722 */         this.lock.unlock();
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
/*      */   public boolean enterIfInterruptibly(Guard guard) throws InterruptedException {
/*  735 */     if (guard.monitor != this) {
/*  736 */       throw new IllegalMonitorStateException();
/*      */     }
/*  738 */     ReentrantLock lock = this.lock;
/*  739 */     lock.lockInterruptibly();
/*      */     
/*  741 */     boolean satisfied = false;
/*      */     try {
/*  743 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  745 */       if (!satisfied) {
/*  746 */         lock.unlock();
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
/*      */   public boolean enterIfInterruptibly(Guard guard, Duration time) throws InterruptedException {
/*  759 */     return enterIfInterruptibly(guard, Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS);
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
/*      */   public boolean enterIfInterruptibly(Guard guard, long time, TimeUnit unit) throws InterruptedException {
/*  771 */     if (guard.monitor != this) {
/*  772 */       throw new IllegalMonitorStateException();
/*      */     }
/*  774 */     ReentrantLock lock = this.lock;
/*  775 */     if (!lock.tryLock(time, unit)) {
/*  776 */       return false;
/*      */     }
/*      */     
/*  779 */     boolean satisfied = false;
/*      */     try {
/*  781 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  783 */       if (!satisfied) {
/*  784 */         lock.unlock();
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
/*      */   public boolean tryEnterIf(Guard guard) {
/*  798 */     if (guard.monitor != this) {
/*  799 */       throw new IllegalMonitorStateException();
/*      */     }
/*  801 */     ReentrantLock lock = this.lock;
/*  802 */     if (!lock.tryLock()) {
/*  803 */       return false;
/*      */     }
/*      */     
/*  806 */     boolean satisfied = false;
/*      */     try {
/*  808 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  810 */       if (!satisfied) {
/*  811 */         lock.unlock();
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
/*      */   public void waitFor(Guard guard) throws InterruptedException {
/*  823 */     if ((((guard.monitor == this) ? 1 : 0) & this.lock.isHeldByCurrentThread()) == 0) {
/*  824 */       throw new IllegalMonitorStateException();
/*      */     }
/*  826 */     if (!guard.isSatisfied()) {
/*  827 */       await(guard, true);
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
/*      */   public boolean waitFor(Guard guard, Duration time) throws InterruptedException {
/*  840 */     return waitFor(guard, Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS);
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
/*      */   public boolean waitFor(Guard guard, long time, TimeUnit unit) throws InterruptedException {
/*  852 */     long timeoutNanos = toSafeNanos(time, unit);
/*  853 */     if ((((guard.monitor == this) ? 1 : 0) & this.lock.isHeldByCurrentThread()) == 0) {
/*  854 */       throw new IllegalMonitorStateException();
/*      */     }
/*  856 */     if (guard.isSatisfied()) {
/*  857 */       return true;
/*      */     }
/*  859 */     if (Thread.interrupted()) {
/*  860 */       throw new InterruptedException();
/*      */     }
/*  862 */     return awaitNanos(guard, timeoutNanos, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void waitForUninterruptibly(Guard guard) {
/*  870 */     if ((((guard.monitor == this) ? 1 : 0) & this.lock.isHeldByCurrentThread()) == 0) {
/*  871 */       throw new IllegalMonitorStateException();
/*      */     }
/*  873 */     if (!guard.isSatisfied()) {
/*  874 */       awaitUninterruptibly(guard, true);
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
/*      */   public boolean waitForUninterruptibly(Guard guard, Duration time) {
/*  886 */     return waitForUninterruptibly(guard, Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean waitForUninterruptibly(Guard guard, long time, TimeUnit unit) {
/*  897 */     long timeoutNanos = toSafeNanos(time, unit);
/*  898 */     if ((((guard.monitor == this) ? 1 : 0) & this.lock.isHeldByCurrentThread()) == 0) {
/*  899 */       throw new IllegalMonitorStateException();
/*      */     }
/*  901 */     if (guard.isSatisfied()) {
/*  902 */       return true;
/*      */     }
/*  904 */     boolean signalBeforeWaiting = true;
/*  905 */     long startTime = initNanoTime(timeoutNanos);
/*  906 */     boolean interrupted = Thread.interrupted();
/*      */     try {
/*  908 */       long remainingNanos = timeoutNanos; while (true) {
/*      */         try {
/*  910 */           return awaitNanos(guard, remainingNanos, signalBeforeWaiting);
/*  911 */         } catch (InterruptedException interrupt) {
/*  912 */           interrupted = true;
/*  913 */           if (guard.isSatisfied()) {
/*  914 */             return true;
/*      */           }
/*  916 */           signalBeforeWaiting = false;
/*  917 */           remainingNanos = remainingNanos(startTime, timeoutNanos);
/*      */         } 
/*      */       } 
/*      */     } finally {
/*  921 */       if (interrupted) {
/*  922 */         Thread.currentThread().interrupt();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void leave() {
/*  929 */     ReentrantLock lock = this.lock;
/*      */     
/*      */     try {
/*  932 */       if (lock.getHoldCount() == 1) {
/*  933 */         signalNextWaiter();
/*      */       }
/*      */     } finally {
/*  936 */       lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFair() {
/*  942 */     return this.fair;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOccupied() {
/*  950 */     return this.lock.isLocked();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOccupiedByCurrentThread() {
/*  958 */     return this.lock.isHeldByCurrentThread();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getOccupiedDepth() {
/*  966 */     return this.lock.getHoldCount();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getQueueLength() {
/*  976 */     return this.lock.getQueueLength();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasQueuedThreads() {
/*  986 */     return this.lock.hasQueuedThreads();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasQueuedThread(Thread thread) {
/*  996 */     return this.lock.hasQueuedThread(thread);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasWaiters(Guard guard) {
/* 1006 */     return (getWaitQueueLength(guard) > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getWaitQueueLength(Guard guard) {
/* 1016 */     if (guard.monitor != this) {
/* 1017 */       throw new IllegalMonitorStateException();
/*      */     }
/* 1019 */     this.lock.lock();
/*      */     try {
/* 1021 */       return guard.waiterCount;
/*      */     } finally {
/* 1023 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static long toSafeNanos(long time, TimeUnit unit) {
/* 1033 */     long timeoutNanos = unit.toNanos(time);
/* 1034 */     return Longs.constrainToRange(timeoutNanos, 0L, 6917529027641081853L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static long initNanoTime(long timeoutNanos) {
/* 1042 */     if (timeoutNanos <= 0L) {
/* 1043 */       return 0L;
/*      */     }
/* 1045 */     long startTime = System.nanoTime();
/* 1046 */     return (startTime == 0L) ? 1L : startTime;
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
/*      */   private static long remainingNanos(long startTime, long timeoutNanos) {
/* 1062 */     return (timeoutNanos <= 0L) ? 0L : (timeoutNanos - System.nanoTime() - startTime);
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
/*      */   @GuardedBy("lock")
/*      */   private void signalNextWaiter() {
/* 1091 */     for (Guard guard = this.activeGuards; guard != null; guard = guard.next) {
/* 1092 */       if (isSatisfied(guard)) {
/* 1093 */         guard.condition.signal();
/*      */         break;
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
/*      */   @GuardedBy("lock")
/*      */   private boolean isSatisfied(Guard guard) {
/*      */     try {
/* 1124 */       return guard.isSatisfied();
/* 1125 */     } catch (Throwable throwable) {
/* 1126 */       signalAllWaiters();
/* 1127 */       throw throwable;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void signalAllWaiters() {
/* 1134 */     for (Guard guard = this.activeGuards; guard != null; guard = guard.next) {
/* 1135 */       guard.condition.signalAll();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void beginWaitingFor(Guard guard) {
/* 1142 */     int waiters = guard.waiterCount++;
/* 1143 */     if (waiters == 0) {
/*      */       
/* 1145 */       guard.next = this.activeGuards;
/* 1146 */       this.activeGuards = guard;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void endWaitingFor(Guard guard) {
/* 1153 */     int waiters = --guard.waiterCount;
/* 1154 */     if (waiters == 0)
/*      */     {
/* 1156 */       for (Guard p = this.activeGuards, pred = null;; pred = p, p = p.next) {
/* 1157 */         if (p == guard) {
/* 1158 */           if (pred == null) {
/* 1159 */             this.activeGuards = p.next;
/*      */           } else {
/* 1161 */             pred.next = p.next;
/*      */           } 
/* 1163 */           p.next = null;
/*      */           break;
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
/*      */   @GuardedBy("lock")
/*      */   private void await(Guard guard, boolean signalBeforeWaiting) throws InterruptedException {
/* 1178 */     if (signalBeforeWaiting) {
/* 1179 */       signalNextWaiter();
/*      */     }
/* 1181 */     beginWaitingFor(guard);
/*      */     try {
/*      */       do {
/* 1184 */         guard.condition.await();
/* 1185 */       } while (!guard.isSatisfied());
/*      */     } finally {
/* 1187 */       endWaitingFor(guard);
/*      */     } 
/*      */   }
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void awaitUninterruptibly(Guard guard, boolean signalBeforeWaiting) {
/* 1193 */     if (signalBeforeWaiting) {
/* 1194 */       signalNextWaiter();
/*      */     }
/* 1196 */     beginWaitingFor(guard);
/*      */     try {
/*      */       do {
/* 1199 */         guard.condition.awaitUninterruptibly();
/* 1200 */       } while (!guard.isSatisfied());
/*      */     } finally {
/* 1202 */       endWaitingFor(guard);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private boolean awaitNanos(Guard guard, long nanos, boolean signalBeforeWaiting) throws InterruptedException {
/* 1210 */     boolean firstTime = true;
/*      */     
/*      */     try { while (true) {
/* 1213 */         if (nanos <= 0L) {
/* 1214 */           return false;
/*      */         }
/* 1216 */         if (firstTime) {
/* 1217 */           if (signalBeforeWaiting) {
/* 1218 */             signalNextWaiter();
/*      */           }
/* 1220 */           beginWaitingFor(guard);
/* 1221 */           firstTime = false;
/*      */         } 
/* 1223 */         nanos = guard.condition.awaitNanos(nanos);
/* 1224 */         if (guard.isSatisfied())
/* 1225 */           return true; 
/*      */       }  }
/* 1227 */     finally { if (!firstTime)
/* 1228 */         endWaitingFor(guard);  }
/*      */   
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\commo\\util\concurrent\Monitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */