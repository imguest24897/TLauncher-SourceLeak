/*    */ package ch.qos.logback.classic.net.server;
/*    */ 
/*    */ import ch.qos.logback.classic.net.LoggingEventPreSerializationTransformer;
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.core.net.server.AbstractServerSocketAppender;
/*    */ import ch.qos.logback.core.spi.PreSerializationTransformer;
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
/*    */ public class ServerSocketAppender
/*    */   extends AbstractServerSocketAppender<ILoggingEvent>
/*    */ {
/* 30 */   private static final PreSerializationTransformer<ILoggingEvent> pst = (PreSerializationTransformer<ILoggingEvent>)new LoggingEventPreSerializationTransformer();
/*    */   
/*    */   private boolean includeCallerData;
/*    */ 
/*    */   
/*    */   protected void postProcessEvent(ILoggingEvent event) {
/* 36 */     if (isIncludeCallerData()) {
/* 37 */       event.getCallerData();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected PreSerializationTransformer<ILoggingEvent> getPST() {
/* 43 */     return pst;
/*    */   }
/*    */   
/*    */   public boolean isIncludeCallerData() {
/* 47 */     return this.includeCallerData;
/*    */   }
/*    */   
/*    */   public void setIncludeCallerData(boolean includeCallerData) {
/* 51 */     this.includeCallerData = includeCallerData;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\ch\qos\logback\classic\net\server\ServerSocketAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */