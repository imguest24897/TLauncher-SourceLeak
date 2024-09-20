/*    */ package ch.qos.logback.classic.pattern;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import java.util.concurrent.atomic.AtomicLong;
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
/*    */ public class LocalSequenceNumberConverter
/*    */   extends ClassicConverter
/*    */ {
/* 30 */   AtomicLong sequenceNumber = new AtomicLong(System.currentTimeMillis());
/*    */ 
/*    */   
/*    */   public String convert(ILoggingEvent event) {
/* 34 */     return Long.toString(this.sequenceNumber.getAndIncrement());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\ch\qos\logback\classic\pattern\LocalSequenceNumberConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */