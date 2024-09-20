/*    */ package org.apache.commons.text.lookup;
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
/*    */ final class IllegalArgumentExceptions
/*    */ {
/*    */   static IllegalArgumentException format(String format, Object... args) {
/* 36 */     return new IllegalArgumentException(String.format(format, args));
/*    */   }
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
/*    */   static IllegalArgumentException format(Throwable t, String format, Object... args) {
/* 49 */     return new IllegalArgumentException(String.format(format, args), t);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\lookup\IllegalArgumentExceptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */