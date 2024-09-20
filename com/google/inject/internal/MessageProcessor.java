/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.inject.Guice;
/*    */ import com.google.inject.spi.Message;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
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
/*    */ final class MessageProcessor
/*    */   extends AbstractProcessor
/*    */ {
/* 32 */   private static final Logger logger = Logger.getLogger(Guice.class.getName());
/*    */   
/*    */   MessageProcessor(Errors errors) {
/* 35 */     super(errors);
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean visit(Message message) {
/* 40 */     if (message.getCause() != null) {
/* 41 */       String rootMessage = getRootMessage(message.getCause());
/* 42 */       String.valueOf(rootMessage); logger.log(Level.INFO, (String.valueOf(rootMessage).length() != 0) ? "An exception was caught and reported. Message: ".concat(String.valueOf(rootMessage)) : new String("An exception was caught and reported. Message: "), message
/*    */ 
/*    */           
/* 45 */           .getCause());
/*    */     } 
/*    */     
/* 48 */     this.errors.addMessage(message);
/* 49 */     return Boolean.valueOf(true);
/*    */   }
/*    */   
/*    */   public static String getRootMessage(Throwable t) {
/* 53 */     Throwable cause = t.getCause();
/* 54 */     return (cause == null) ? t.toString() : getRootMessage(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\MessageProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */