/*     */ package org.apache.commons.lang3.concurrent;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ public class BasicThreadFactory
/*     */   implements ThreadFactory
/*     */ {
/*     */   private final AtomicLong threadCounter;
/*     */   private final ThreadFactory wrappedFactory;
/*     */   private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
/*     */   private final String namingPattern;
/*     */   private final Integer priority;
/*     */   private final Boolean daemon;
/*     */   
/*     */   private BasicThreadFactory(Builder builder) {
/* 117 */     if (builder.wrappedFactory == null) {
/* 118 */       this.wrappedFactory = Executors.defaultThreadFactory();
/*     */     } else {
/* 120 */       this.wrappedFactory = builder.wrappedFactory;
/*     */     } 
/*     */     
/* 123 */     this.namingPattern = builder.namingPattern;
/* 124 */     this.priority = builder.priority;
/* 125 */     this.daemon = builder.daemon;
/* 126 */     this.uncaughtExceptionHandler = builder.exceptionHandler;
/*     */     
/* 128 */     this.threadCounter = new AtomicLong();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ThreadFactory getWrappedFactory() {
/* 140 */     return this.wrappedFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getNamingPattern() {
/* 150 */     return this.namingPattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Boolean getDaemonFlag() {
/* 162 */     return this.daemon;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Integer getPriority() {
/* 172 */     return this.priority;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Thread.UncaughtExceptionHandler getUncaughtExceptionHandler() {
/* 182 */     return this.uncaughtExceptionHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getThreadCount() {
/* 193 */     return this.threadCounter.get();
/*     */   }
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
/*     */   public Thread newThread(Runnable runnable) {
/* 206 */     Thread thread = getWrappedFactory().newThread(runnable);
/* 207 */     initializeThread(thread);
/*     */     
/* 209 */     return thread;
/*     */   }
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
/*     */   private void initializeThread(Thread thread) {
/* 222 */     if (getNamingPattern() != null) {
/* 223 */       Long count = Long.valueOf(this.threadCounter.incrementAndGet());
/* 224 */       thread.setName(String.format(getNamingPattern(), new Object[] { count }));
/*     */     } 
/*     */     
/* 227 */     if (getUncaughtExceptionHandler() != null) {
/* 228 */       thread.setUncaughtExceptionHandler(getUncaughtExceptionHandler());
/*     */     }
/*     */     
/* 231 */     if (getPriority() != null) {
/* 232 */       thread.setPriority(getPriority().intValue());
/*     */     }
/*     */     
/* 235 */     if (getDaemonFlag() != null) {
/* 236 */       thread.setDaemon(getDaemonFlag().booleanValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */     implements org.apache.commons.lang3.builder.Builder<BasicThreadFactory>
/*     */   {
/*     */     private ThreadFactory wrappedFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Thread.UncaughtExceptionHandler exceptionHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private String namingPattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Integer priority;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Boolean daemon;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder wrappedFactory(ThreadFactory factory) {
/* 282 */       Objects.requireNonNull(factory, "factory");
/*     */       
/* 284 */       this.wrappedFactory = factory;
/* 285 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder namingPattern(String pattern) {
/* 297 */       Objects.requireNonNull(pattern, "pattern");
/*     */       
/* 299 */       this.namingPattern = pattern;
/* 300 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder daemon(boolean daemon) {
/* 312 */       this.daemon = Boolean.valueOf(daemon);
/* 313 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder priority(int priority) {
/* 324 */       this.priority = Integer.valueOf(priority);
/* 325 */       return this;
/*     */     }
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
/*     */     public Builder uncaughtExceptionHandler(Thread.UncaughtExceptionHandler handler) {
/* 339 */       Objects.requireNonNull(handler, "handler");
/*     */       
/* 341 */       this.exceptionHandler = handler;
/* 342 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void reset() {
/* 352 */       this.wrappedFactory = null;
/* 353 */       this.exceptionHandler = null;
/* 354 */       this.namingPattern = null;
/* 355 */       this.priority = null;
/* 356 */       this.daemon = null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public BasicThreadFactory build() {
/* 368 */       BasicThreadFactory factory = new BasicThreadFactory(this);
/* 369 */       reset();
/* 370 */       return factory;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\concurrent\BasicThreadFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */