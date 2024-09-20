/*    */ package org.apache.commons.lang3.stream;
/*    */ 
/*    */ import java.util.stream.IntStream;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IntStreams
/*    */ {
/*    */   public static IntStream range(int endExclusive) {
/* 38 */     return IntStream.range(0, endExclusive);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static IntStream rangeClosed(int endInclusive) {
/* 48 */     return IntStream.rangeClosed(0, endInclusive);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\stream\IntStreams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */