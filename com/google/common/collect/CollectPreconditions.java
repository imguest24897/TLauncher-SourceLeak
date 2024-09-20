/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
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
/*    */ @GwtCompatible
/*    */ final class CollectPreconditions
/*    */ {
/*    */   static void checkEntryNotNull(Object key, Object value) {
/* 29 */     if (key == null) {
/* 30 */       String str = String.valueOf(value); throw new NullPointerException((new StringBuilder(24 + String.valueOf(str).length())).append("null key in entry: null=").append(str).toString());
/* 31 */     }  if (value == null) {
/* 32 */       String str = String.valueOf(key); throw new NullPointerException((new StringBuilder(26 + String.valueOf(str).length())).append("null value in entry: ").append(str).append("=null").toString());
/*    */     } 
/*    */   }
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   static int checkNonnegative(int value, String name) {
/* 38 */     if (value < 0) {
/* 39 */       throw new IllegalArgumentException((new StringBuilder(40 + String.valueOf(name).length())).append(name).append(" cannot be negative but was: ").append(value).toString());
/*    */     }
/* 41 */     return value;
/*    */   }
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   static long checkNonnegative(long value, String name) {
/* 46 */     if (value < 0L) {
/* 47 */       throw new IllegalArgumentException((new StringBuilder(49 + String.valueOf(name).length())).append(name).append(" cannot be negative but was: ").append(value).toString());
/*    */     }
/* 49 */     return value;
/*    */   }
/*    */   
/*    */   static void checkPositive(int value, String name) {
/* 53 */     if (value <= 0) {
/* 54 */       throw new IllegalArgumentException((new StringBuilder(38 + String.valueOf(name).length())).append(name).append(" must be positive but was: ").append(value).toString());
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static void checkRemove(boolean canRemove) {
/* 63 */     Preconditions.checkState(canRemove, "no calls to next() since the last call to remove()");
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\CollectPreconditions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */