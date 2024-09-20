/*    */ package com.beust.jcommander;
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
/*    */ public class MissingCommandException
/*    */   extends ParameterException
/*    */ {
/*    */   private final String unknownCommand;
/*    */   
/*    */   public MissingCommandException(String message) {
/* 35 */     this(message, null);
/*    */   }
/*    */   
/*    */   public MissingCommandException(String message, String command) {
/* 39 */     super(message);
/* 40 */     this.unknownCommand = command;
/*    */   }
/*    */   
/*    */   public String getUnknownCommand() {
/* 44 */     return this.unknownCommand;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\MissingCommandException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */