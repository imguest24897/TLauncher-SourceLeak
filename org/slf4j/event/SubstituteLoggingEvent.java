/*    */ package org.slf4j.event;
/*    */ 
/*    */ import org.slf4j.Marker;
/*    */ import org.slf4j.helpers.SubstituteLogger;
/*    */ 
/*    */ public class SubstituteLoggingEvent
/*    */   implements LoggingEvent {
/*    */   Level level;
/*    */   Marker marker;
/*    */   String loggerName;
/*    */   SubstituteLogger logger;
/*    */   String threadName;
/*    */   String message;
/*    */   Object[] argArray;
/*    */   long timeStamp;
/*    */   Throwable throwable;
/*    */   
/*    */   public Level getLevel() {
/* 19 */     return this.level;
/*    */   }
/*    */   
/*    */   public void setLevel(Level level) {
/* 23 */     this.level = level;
/*    */   }
/*    */   
/*    */   public Marker getMarker() {
/* 27 */     return this.marker;
/*    */   }
/*    */   
/*    */   public void setMarker(Marker marker) {
/* 31 */     this.marker = marker;
/*    */   }
/*    */   
/*    */   public String getLoggerName() {
/* 35 */     return this.loggerName;
/*    */   }
/*    */   
/*    */   public void setLoggerName(String loggerName) {
/* 39 */     this.loggerName = loggerName;
/*    */   }
/*    */   
/*    */   public SubstituteLogger getLogger() {
/* 43 */     return this.logger;
/*    */   }
/*    */   
/*    */   public void setLogger(SubstituteLogger logger) {
/* 47 */     this.logger = logger;
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 51 */     return this.message;
/*    */   }
/*    */   
/*    */   public void setMessage(String message) {
/* 55 */     this.message = message;
/*    */   }
/*    */   
/*    */   public Object[] getArgumentArray() {
/* 59 */     return this.argArray;
/*    */   }
/*    */   
/*    */   public void setArgumentArray(Object[] argArray) {
/* 63 */     this.argArray = argArray;
/*    */   }
/*    */   
/*    */   public long getTimeStamp() {
/* 67 */     return this.timeStamp;
/*    */   }
/*    */   
/*    */   public void setTimeStamp(long timeStamp) {
/* 71 */     this.timeStamp = timeStamp;
/*    */   }
/*    */   
/*    */   public String getThreadName() {
/* 75 */     return this.threadName;
/*    */   }
/*    */   
/*    */   public void setThreadName(String threadName) {
/* 79 */     this.threadName = threadName;
/*    */   }
/*    */   
/*    */   public Throwable getThrowable() {
/* 83 */     return this.throwable;
/*    */   }
/*    */   
/*    */   public void setThrowable(Throwable throwable) {
/* 87 */     this.throwable = throwable;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\slf4j\event\SubstituteLoggingEvent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */