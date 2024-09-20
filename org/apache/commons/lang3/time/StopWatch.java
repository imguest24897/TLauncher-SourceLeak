/*     */ package org.apache.commons.lang3.time;
/*     */ 
/*     */ import java.util.Objects;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StopWatch
/*     */ {
/*     */   private static final long NANO_2_MILLIS = 1000000L;
/*     */   private final String message;
/*     */   
/*     */   private enum SplitState
/*     */   {
/*  66 */     SPLIT,
/*  67 */     UNSPLIT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private enum State
/*     */   {
/*  75 */     RUNNING
/*     */     {
/*     */       boolean isStarted() {
/*  78 */         return true;
/*     */       }
/*     */       
/*     */       boolean isStopped() {
/*  82 */         return false;
/*     */       }
/*     */       
/*     */       boolean isSuspended() {
/*  86 */         return false;
/*     */       }
/*     */     },
/*  89 */     STOPPED
/*     */     {
/*     */       boolean isStarted() {
/*  92 */         return false;
/*     */       }
/*     */       
/*     */       boolean isStopped() {
/*  96 */         return true;
/*     */       }
/*     */       
/*     */       boolean isSuspended() {
/* 100 */         return false;
/*     */       }
/*     */     },
/* 103 */     SUSPENDED
/*     */     {
/*     */       boolean isStarted() {
/* 106 */         return true;
/*     */       }
/*     */       
/*     */       boolean isStopped() {
/* 110 */         return false;
/*     */       }
/*     */       
/*     */       boolean isSuspended() {
/* 114 */         return true;
/*     */       }
/*     */     },
/* 117 */     UNSTARTED
/*     */     {
/*     */       boolean isStarted() {
/* 120 */         return false;
/*     */       }
/*     */       
/*     */       boolean isStopped() {
/* 124 */         return true;
/*     */       }
/*     */       
/*     */       boolean isSuspended() {
/* 128 */         return false;
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
/*     */     abstract boolean isStarted();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract boolean isStopped();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract boolean isSuspended();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StopWatch create() {
/* 166 */     return new StopWatch();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StopWatch createStarted() {
/* 177 */     StopWatch sw = new StopWatch();
/* 178 */     sw.start();
/* 179 */     return sw;
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
/* 192 */   private State runningState = State.UNSTARTED;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 197 */   private SplitState splitState = SplitState.UNSPLIT;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long startTimeNanos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long startTimeMillis;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long stopTimeMillis;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long stopTimeNanos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StopWatch() {
/* 228 */     this(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StopWatch(String message) {
/* 238 */     this.message = message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String formatSplitTime() {
/* 248 */     return DurationFormatUtils.formatDurationHMS(getSplitTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String formatTime() {
/* 258 */     return DurationFormatUtils.formatDurationHMS(getTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/* 268 */     return this.message;
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
/*     */   public long getNanoTime() {
/* 284 */     if (this.runningState == State.STOPPED || this.runningState == State.SUSPENDED) {
/* 285 */       return this.stopTimeNanos - this.startTimeNanos;
/*     */     }
/* 287 */     if (this.runningState == State.UNSTARTED) {
/* 288 */       return 0L;
/*     */     }
/* 290 */     if (this.runningState == State.RUNNING) {
/* 291 */       return System.nanoTime() - this.startTimeNanos;
/*     */     }
/* 293 */     throw new IllegalStateException("Illegal running state has occurred.");
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
/*     */   public long getSplitNanoTime() {
/* 310 */     if (this.splitState != SplitState.SPLIT) {
/* 311 */       throw new IllegalStateException("Stopwatch must be split to get the split time.");
/*     */     }
/* 313 */     return this.stopTimeNanos - this.startTimeNanos;
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
/*     */   public long getSplitTime() {
/* 330 */     return getSplitNanoTime() / 1000000L;
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
/*     */   public long getStartTime() {
/* 343 */     if (this.runningState == State.UNSTARTED) {
/* 344 */       throw new IllegalStateException("Stopwatch has not been started");
/*     */     }
/*     */     
/* 347 */     return this.startTimeMillis;
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
/*     */   public long getStopTime() {
/* 360 */     if (this.runningState == State.UNSTARTED) {
/* 361 */       throw new IllegalStateException("Stopwatch has not been started");
/*     */     }
/*     */     
/* 364 */     return this.stopTimeMillis;
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
/*     */   public long getTime() {
/* 378 */     return getNanoTime() / 1000000L;
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
/*     */   public long getTime(TimeUnit timeUnit) {
/* 396 */     return timeUnit.convert(getNanoTime(), TimeUnit.NANOSECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStarted() {
/* 406 */     return this.runningState.isStarted();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStopped() {
/* 417 */     return this.runningState.isStopped();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSuspended() {
/* 428 */     return this.runningState.isSuspended();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 439 */     this.runningState = State.UNSTARTED;
/* 440 */     this.splitState = SplitState.UNSPLIT;
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
/*     */   public void resume() {
/* 455 */     if (this.runningState != State.SUSPENDED) {
/* 456 */       throw new IllegalStateException("Stopwatch must be suspended to resume. ");
/*     */     }
/* 458 */     this.startTimeNanos += System.nanoTime() - this.stopTimeNanos;
/* 459 */     this.runningState = State.RUNNING;
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
/*     */   public void split() {
/* 474 */     if (this.runningState != State.RUNNING) {
/* 475 */       throw new IllegalStateException("Stopwatch is not running. ");
/*     */     }
/* 477 */     this.stopTimeNanos = System.nanoTime();
/* 478 */     this.splitState = SplitState.SPLIT;
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
/*     */   public void start() {
/* 492 */     if (this.runningState == State.STOPPED) {
/* 493 */       throw new IllegalStateException("Stopwatch must be reset before being restarted. ");
/*     */     }
/* 495 */     if (this.runningState != State.UNSTARTED) {
/* 496 */       throw new IllegalStateException("Stopwatch already started. ");
/*     */     }
/* 498 */     this.startTimeNanos = System.nanoTime();
/* 499 */     this.startTimeMillis = System.currentTimeMillis();
/* 500 */     this.runningState = State.RUNNING;
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
/*     */   public void stop() {
/* 514 */     if (this.runningState != State.RUNNING && this.runningState != State.SUSPENDED) {
/* 515 */       throw new IllegalStateException("Stopwatch is not running. ");
/*     */     }
/* 517 */     if (this.runningState == State.RUNNING) {
/* 518 */       this.stopTimeNanos = System.nanoTime();
/* 519 */       this.stopTimeMillis = System.currentTimeMillis();
/*     */     } 
/* 521 */     this.runningState = State.STOPPED;
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
/*     */   public void suspend() {
/* 536 */     if (this.runningState != State.RUNNING) {
/* 537 */       throw new IllegalStateException("Stopwatch must be running to suspend. ");
/*     */     }
/* 539 */     this.stopTimeNanos = System.nanoTime();
/* 540 */     this.stopTimeMillis = System.currentTimeMillis();
/* 541 */     this.runningState = State.SUSPENDED;
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
/*     */   public String toSplitString() {
/* 556 */     String msgStr = Objects.toString(this.message, "");
/* 557 */     String formattedTime = formatSplitTime();
/* 558 */     return msgStr.isEmpty() ? formattedTime : (msgStr + " " + formattedTime);
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
/*     */   public String toString() {
/* 573 */     String msgStr = Objects.toString(this.message, "");
/* 574 */     String formattedTime = formatTime();
/* 575 */     return msgStr.isEmpty() ? formattedTime : (msgStr + " " + formattedTime);
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
/*     */   public void unsplit() {
/* 590 */     if (this.splitState != SplitState.SPLIT) {
/* 591 */       throw new IllegalStateException("Stopwatch has not been split. ");
/*     */     }
/* 593 */     this.splitState = SplitState.UNSPLIT;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\time\StopWatch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */