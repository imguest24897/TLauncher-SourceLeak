/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import java.util.concurrent.locks.LockSupport;
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
/*    */ 
/*    */ final class OverflowAvoidingLockSupport
/*    */ {
/*    */   static final long MAX_NANOSECONDS_THRESHOLD = 2147483647999999999L;
/*    */   
/*    */   static void parkNanos(Object blocker, long nanos) {
/* 35 */     LockSupport.parkNanos(blocker, Math.min(nanos, 2147483647999999999L));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\commo\\util\concurrent\OverflowAvoidingLockSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */