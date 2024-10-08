/*     */ package org.apache.commons.logging.impl;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.commons.logging.Log;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NoOpLog
/*     */   implements Log, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 561423906191706148L;
/*     */   
/*     */   public NoOpLog() {}
/*     */   
/*     */   public NoOpLog(String name) {}
/*     */   
/*     */   public void trace(Object message) {}
/*     */   
/*     */   public void trace(Object message, Throwable t) {}
/*     */   
/*     */   public void debug(Object message) {}
/*     */   
/*     */   public void debug(Object message, Throwable t) {}
/*     */   
/*     */   public void info(Object message) {}
/*     */   
/*     */   public void info(Object message, Throwable t) {}
/*     */   
/*     */   public void warn(Object message) {}
/*     */   
/*     */   public void warn(Object message, Throwable t) {}
/*     */   
/*     */   public void error(Object message) {}
/*     */   
/*     */   public void error(Object message, Throwable t) {}
/*     */   
/*     */   public void fatal(Object message) {}
/*     */   
/*     */   public void fatal(Object message, Throwable t) {}
/*     */   
/*     */   public final boolean isDebugEnabled() {
/*  97 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isErrorEnabled() {
/* 106 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isFatalEnabled() {
/* 115 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isInfoEnabled() {
/* 124 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isTraceEnabled() {
/* 133 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isWarnEnabled() {
/* 142 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\logging\impl\NoOpLog.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */