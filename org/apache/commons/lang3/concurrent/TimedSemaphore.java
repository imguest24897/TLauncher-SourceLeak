/*     */ package org.apache.commons.lang3.concurrent;
/*     */ 
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TimedSemaphore
/*     */ {
/*     */   public static final int NO_LIMIT = 0;
/*     */   private static final int THREAD_POOL_SIZE = 1;
/*     */   private final ScheduledExecutorService executorService;
/*     */   private final long period;
/*     */   private final TimeUnit unit;
/*     */   private final boolean ownExecutor;
/*     */   private ScheduledFuture<?> task;
/*     */   private long totalAcquireCount;
/*     */   private long periodCount;
/*     */   private int limit;
/*     */   private int acquireCount;
/*     */   private int lastCallsPerPeriod;
/*     */   private boolean shutdown;
/*     */   
/*     */   public TimedSemaphore(long timePeriod, TimeUnit timeUnit, int limit) {
/* 196 */     this(null, timePeriod, timeUnit, limit);
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
/*     */   public TimedSemaphore(ScheduledExecutorService service, long timePeriod, TimeUnit timeUnit, int limit) {
/* 213 */     Validate.inclusiveBetween(1L, Long.MAX_VALUE, timePeriod, "Time period must be greater than 0!");
/*     */     
/* 215 */     this.period = timePeriod;
/* 216 */     this.unit = timeUnit;
/*     */     
/* 218 */     if (service != null) {
/* 219 */       this.executorService = service;
/* 220 */       this.ownExecutor = false;
/*     */     } else {
/* 222 */       ScheduledThreadPoolExecutor s = new ScheduledThreadPoolExecutor(1);
/*     */       
/* 224 */       s.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
/* 225 */       s.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
/* 226 */       this.executorService = s;
/* 227 */       this.ownExecutor = true;
/*     */     } 
/*     */     
/* 230 */     setLimit(limit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized int getLimit() {
/* 241 */     return this.limit;
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
/*     */   public final synchronized void setLimit(int limit) {
/* 255 */     this.limit = limit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void shutdown() {
/* 264 */     if (!this.shutdown) {
/*     */       
/* 266 */       if (this.ownExecutor)
/*     */       {
/*     */         
/* 269 */         getExecutorService().shutdownNow();
/*     */       }
/* 271 */       if (this.task != null) {
/* 272 */         this.task.cancel(false);
/*     */       }
/*     */       
/* 275 */       this.shutdown = true;
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
/*     */   public synchronized boolean isShutdown() {
/* 287 */     return this.shutdown;
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
/*     */   public synchronized void acquire() throws InterruptedException {
/*     */     boolean canPass;
/* 302 */     prepareAcquire();
/*     */ 
/*     */     
/*     */     do {
/* 306 */       canPass = acquirePermit();
/* 307 */       if (canPass)
/* 308 */         continue;  wait();
/*     */     }
/* 310 */     while (!canPass);
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
/*     */   public synchronized boolean tryAcquire() {
/* 325 */     prepareAcquire();
/* 326 */     return acquirePermit();
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
/*     */   public synchronized int getLastAcquiresPerPeriod() {
/* 340 */     return this.lastCallsPerPeriod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int getAcquireCount() {
/* 350 */     return this.acquireCount;
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
/*     */   public synchronized int getAvailablePermits() {
/* 365 */     return getLimit() - getAcquireCount();
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
/*     */   public synchronized double getAverageCallsPerPeriod() {
/* 378 */     return (this.periodCount == 0L) ? 0.0D : (this.totalAcquireCount / this.periodCount);
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
/*     */   public long getPeriod() {
/* 390 */     return this.period;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeUnit getUnit() {
/* 399 */     return this.unit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ScheduledExecutorService getExecutorService() {
/* 408 */     return this.executorService;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ScheduledFuture<?> startTimer() {
/* 419 */     return getExecutorService().scheduleAtFixedRate(this::endOfPeriod, getPeriod(), getPeriod(), getUnit());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized void endOfPeriod() {
/* 428 */     this.lastCallsPerPeriod = this.acquireCount;
/* 429 */     this.totalAcquireCount += this.acquireCount;
/* 430 */     this.periodCount++;
/* 431 */     this.acquireCount = 0;
/* 432 */     notifyAll();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void prepareAcquire() {
/* 440 */     if (isShutdown()) {
/* 441 */       throw new IllegalStateException("TimedSemaphore is shut down!");
/*     */     }
/*     */     
/* 444 */     if (this.task == null) {
/* 445 */       this.task = startTimer();
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
/*     */   private boolean acquirePermit() {
/* 458 */     if (getLimit() <= 0 || this.acquireCount < getLimit()) {
/* 459 */       this.acquireCount++;
/* 460 */       return true;
/*     */     } 
/* 462 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\concurrent\TimedSemaphore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */