/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.time.Duration;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtIncompatible
/*    */ final class Internal
/*    */ {
/*    */   static long toNanosSaturated(Duration duration) {
/*    */     try {
/* 35 */       return duration.toNanos();
/* 36 */     } catch (ArithmeticException tooBig) {
/* 37 */       return duration.isNegative() ? Long.MIN_VALUE : Long.MAX_VALUE;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\commo\\util\concurrent\Internal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */