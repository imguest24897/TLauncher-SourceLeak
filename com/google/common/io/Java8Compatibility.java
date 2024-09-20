/*    */ package com.google.common.io;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.nio.Buffer;
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
/*    */ final class Java8Compatibility
/*    */ {
/*    */   static void clear(Buffer b) {
/* 27 */     b.clear();
/*    */   }
/*    */   
/*    */   static void flip(Buffer b) {
/* 31 */     b.flip();
/*    */   }
/*    */   
/*    */   static void limit(Buffer b, int limit) {
/* 35 */     b.limit(limit);
/*    */   }
/*    */   
/*    */   static void position(Buffer b, int position) {
/* 39 */     b.position(position);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\io\Java8Compatibility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */