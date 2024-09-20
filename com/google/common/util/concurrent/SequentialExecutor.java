/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.concurrent.Executor;
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
/*     */ @GwtIncompatible
/*     */ final class SequentialExecutor
/*     */   implements Executor
/*     */ {
/*  51 */   private static final Logger log = Logger.getLogger(SequentialExecutor.class.getName());
/*     */   private final Executor executor;
/*     */   
/*     */   enum WorkerRunningState {
/*  55 */     IDLE,
/*     */     
/*  57 */     QUEUING,
/*     */     
/*  59 */     QUEUED,
/*  60 */     RUNNING;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("queue")
/*  66 */   private final Deque<Runnable> queue = new ArrayDeque<>();
/*     */ 
/*     */   
/*     */   @GuardedBy("queue")
/*  70 */   private WorkerRunningState workerRunningState = WorkerRunningState.IDLE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("queue")
/*  80 */   private long workerRunCount = 0L;
/*     */   
/*     */   @RetainedWith
/*  83 */   private final QueueWorker worker = new QueueWorker();
/*     */ 
/*     */   
/*     */   SequentialExecutor(Executor executor) {
/*  87 */     this.executor = (Executor)Preconditions.checkNotNull(executor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(final Runnable task) {
/*     */     Runnable submittedTask;
/*     */     long oldRunCount;
/*  98 */     Preconditions.checkNotNull(task);
/*     */ 
/*     */     
/* 101 */     synchronized (this.queue) {
/*     */ 
/*     */       
/* 104 */       if (this.workerRunningState == WorkerRunningState.RUNNING || this.workerRunningState == WorkerRunningState.QUEUED) {
/* 105 */         this.queue.add(task);
/*     */         
/*     */         return;
/*     */       } 
/* 109 */       oldRunCount = this.workerRunCount;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 117 */       submittedTask = new Runnable(this)
/*     */         {
/*     */           public void run()
/*     */           {
/* 121 */             task.run();
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 126 */             return task.toString();
/*     */           }
/*     */         };
/* 129 */       this.queue.add(submittedTask);
/* 130 */       this.workerRunningState = WorkerRunningState.QUEUING;
/*     */     } 
/*     */     
/*     */     try {
/* 134 */       this.executor.execute(this.worker);
/* 135 */     } catch (RuntimeException|Error t) {
/* 136 */       synchronized (this.queue) {
/*     */ 
/*     */         
/* 139 */         boolean removed = ((this.workerRunningState == WorkerRunningState.IDLE || this.workerRunningState == WorkerRunningState.QUEUING) && this.queue.removeLastOccurrence(submittedTask));
/*     */ 
/*     */         
/* 142 */         if (!(t instanceof java.util.concurrent.RejectedExecutionException) || removed) {
/* 143 */           throw t;
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 160 */     boolean alreadyMarkedQueued = (this.workerRunningState != WorkerRunningState.QUEUING);
/* 161 */     if (alreadyMarkedQueued) {
/*     */       return;
/*     */     }
/* 164 */     synchronized (this.queue) {
/* 165 */       if (this.workerRunCount == oldRunCount && this.workerRunningState == WorkerRunningState.QUEUING)
/* 166 */         this.workerRunningState = WorkerRunningState.QUEUED; 
/*     */     } 
/*     */   }
/*     */   
/*     */   private final class QueueWorker
/*     */     implements Runnable {
/*     */     Runnable task;
/*     */     
/*     */     private QueueWorker() {}
/*     */     
/*     */     public void run() {
/*     */       try {
/* 178 */         workOnQueue();
/* 179 */       } catch (Error e) {
/* 180 */         synchronized (SequentialExecutor.this.queue) {
/* 181 */           SequentialExecutor.this.workerRunningState = SequentialExecutor.WorkerRunningState.IDLE;
/*     */         } 
/* 183 */         throw e;
/*     */       } 
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
/*     */ 
/*     */     
/*     */     private void workOnQueue() {
/* 202 */       boolean interruptedDuringTask = false;
/* 203 */       boolean hasSetRunning = false;
/*     */       try {
/*     */         while (true) {
/* 206 */           synchronized (SequentialExecutor.this.queue) {
/*     */ 
/*     */             
/* 209 */             if (!hasSetRunning) {
/* 210 */               if (SequentialExecutor.this.workerRunningState == SequentialExecutor.WorkerRunningState.RUNNING) {
/*     */                 return;
/*     */               }
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 217 */               SequentialExecutor.this.workerRunCount++;
/* 218 */               SequentialExecutor.this.workerRunningState = SequentialExecutor.WorkerRunningState.RUNNING;
/* 219 */               hasSetRunning = true;
/*     */             } 
/*     */             
/* 222 */             this.task = SequentialExecutor.this.queue.poll();
/* 223 */             if (this.task == null) {
/* 224 */               SequentialExecutor.this.workerRunningState = SequentialExecutor.WorkerRunningState.IDLE;
/*     */ 
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/*     */           
/* 231 */           interruptedDuringTask |= Thread.interrupted();
/*     */           try {
/* 233 */             this.task.run();
/* 234 */           } catch (RuntimeException e) {
/* 235 */             String str = String.valueOf(this.task); SequentialExecutor.log.log(Level.SEVERE, (new StringBuilder(35 + String.valueOf(str).length())).append("Exception while executing runnable ").append(str).toString(), e);
/*     */           } finally {
/* 237 */             this.task = null;
/*     */           }
/*     */         
/*     */         }
/*     */       
/*     */       } finally {
/*     */         
/* 244 */         if (interruptedDuringTask) {
/* 245 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 253 */       Runnable currentlyRunning = this.task;
/* 254 */       if (currentlyRunning != null) {
/* 255 */         String str1 = String.valueOf(currentlyRunning); return (new StringBuilder(34 + String.valueOf(str1).length())).append("SequentialExecutorWorker{running=").append(str1).append("}").toString();
/*     */       } 
/* 257 */       String str = String.valueOf(SequentialExecutor.this.workerRunningState); return (new StringBuilder(32 + String.valueOf(str).length())).append("SequentialExecutorWorker{state=").append(str).append("}").toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 263 */     int i = System.identityHashCode(this); String str = String.valueOf(this.executor); return (new StringBuilder(32 + String.valueOf(str).length())).append("SequentialExecutor@").append(i).append("{").append(str).append("}").toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\commo\\util\concurrent\SequentialExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */