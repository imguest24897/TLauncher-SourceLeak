/*    */ package com.google.inject.internal.util;
/*    */ 
/*    */ import com.google.common.base.Stopwatch;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.logging.Logger;
/*    */ import javax.annotation.concurrent.NotThreadSafe;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @NotThreadSafe
/*    */ public final class ContinuousStopwatch
/*    */ {
/* 32 */   private final Logger logger = Logger.getLogger(ContinuousStopwatch.class.getName());
/*    */ 
/*    */ 
/*    */   
/*    */   private final Stopwatch stopwatch;
/*    */ 
/*    */ 
/*    */   
/*    */   public ContinuousStopwatch(Stopwatch stopwatch) {
/* 41 */     this.stopwatch = stopwatch;
/* 42 */     reset();
/*    */   }
/*    */ 
/*    */   
/*    */   public long reset() {
/* 47 */     long elapsedTimeMs = this.stopwatch.elapsed(TimeUnit.MILLISECONDS);
/* 48 */     this.stopwatch.reset();
/* 49 */     this.stopwatch.start();
/* 50 */     return elapsedTimeMs;
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetAndLog(String label) {
/* 55 */     long l = reset(); this.logger.fine((new StringBuilder(24 + String.valueOf(label).length())).append(label).append(": ").append(l).append("ms").toString());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\interna\\util\ContinuousStopwatch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */