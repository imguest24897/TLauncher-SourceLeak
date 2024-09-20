/*    */ package com.google.inject.internal;
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
/*    */ public class ErrorsException
/*    */   extends Exception
/*    */ {
/*    */   private final Errors errors;
/*    */   
/*    */   public ErrorsException(Errors errors) {
/* 33 */     this.errors = errors;
/*    */   }
/*    */   
/*    */   public Errors getErrors() {
/* 37 */     return this.errors;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ErrorsException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */