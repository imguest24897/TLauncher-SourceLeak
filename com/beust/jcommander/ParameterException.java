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
/*    */ public class ParameterException
/*    */   extends RuntimeException
/*    */ {
/*    */   private JCommander jc;
/*    */   
/*    */   public ParameterException(Throwable t) {
/* 30 */     super(t);
/*    */   }
/*    */   
/*    */   public ParameterException(String string) {
/* 34 */     super(string);
/*    */   }
/*    */   
/*    */   public ParameterException(String string, Throwable t) {
/* 38 */     super(string, t);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setJCommander(JCommander jc) {
/* 44 */     this.jc = jc;
/*    */   }
/*    */   
/*    */   public JCommander getJCommander() {
/* 48 */     return this.jc;
/*    */   }
/*    */   
/*    */   public void usage() {
/* 52 */     if (this.jc != null)
/* 53 */       this.jc.usage(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\ParameterException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */