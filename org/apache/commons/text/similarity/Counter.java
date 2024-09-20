/*    */ package org.apache.commons.text.similarity;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class Counter
/*    */ {
/*    */   public static Map<CharSequence, Integer> of(CharSequence[] tokens) {
/* 43 */     Map<CharSequence, Integer> innerCounter = new HashMap<>();
/* 44 */     for (CharSequence token : tokens) {
/* 45 */       Integer integer = innerCounter.get(token);
/* 46 */       innerCounter.put(token, Integer.valueOf((integer != null) ? (integer.intValue() + 1) : 1));
/*    */     } 
/* 48 */     return innerCounter;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\similarity\Counter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */