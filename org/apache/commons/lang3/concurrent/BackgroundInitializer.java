/*     */ package org.apache.commons.lang3.concurrent;
/*     */ 
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
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
/*     */ public abstract class BackgroundInitializer<T>
/*     */   implements ConcurrentInitializer<T>
/*     */ {
/*     */   private ExecutorService externalExecutor;
/*     */   private ExecutorService executor;
/*     */   private Future<T> future;
/*     */   
/*     */   protected BackgroundInitializer() {
/* 100 */     this(null);
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
/*     */   
/*     */   protected BackgroundInitializer(ExecutorService exec) {
/* 114 */     setExternalExecutor(exec);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized ExecutorService getExternalExecutor() {
/* 123 */     return this.externalExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isStarted() {
/* 134 */     return (this.future != null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized void setExternalExecutor(ExecutorService externalExecutor) {
/* 153 */     if (isStarted()) {
/* 154 */       throw new IllegalStateException("Cannot set ExecutorService after start()!");
/*     */     }
/*     */ 
/*     */     
/* 158 */     this.externalExecutor = externalExecutor;
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
/*     */ 
/*     */   
/*     */   public synchronized boolean start() {
/* 173 */     if (!isStarted()) {
/*     */       ExecutorService tempExec;
/*     */ 
/*     */ 
/*     */       
/* 178 */       this.executor = getExternalExecutor();
/* 179 */       if (this.executor == null) {
/* 180 */         this.executor = tempExec = createExecutor();
/*     */       } else {
/* 182 */         tempExec = null;
/*     */       } 
/*     */       
/* 185 */       this.future = this.executor.submit(createTask(tempExec));
/*     */       
/* 187 */       return true;
/*     */     } 
/*     */     
/* 190 */     return false;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T get() throws ConcurrentException {
/*     */     try {
/* 210 */       return getFuture().get();
/* 211 */     } catch (ExecutionException execex) {
/* 212 */       ConcurrentUtils.handleCause(execex);
/* 213 */       return null;
/* 214 */     } catch (InterruptedException iex) {
/*     */       
/* 216 */       Thread.currentThread().interrupt();
/* 217 */       throw new ConcurrentException(iex);
/*     */     } 
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
/*     */   public synchronized Future<T> getFuture() {
/* 230 */     if (this.future == null) {
/* 231 */       throw new IllegalStateException("start() must be called first!");
/*     */     }
/*     */     
/* 234 */     return this.future;
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
/*     */   protected final synchronized ExecutorService getActiveExecutor() {
/* 247 */     return this.executor;
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
/*     */ 
/*     */   
/*     */   protected int getTaskCount() {
/* 262 */     return 1;
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
/*     */ 
/*     */   
/*     */   protected abstract T initialize() throws Exception;
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
/*     */   private Callable<T> createTask(ExecutorService execDestroy) {
/* 289 */     return new InitializationTask(execDestroy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ExecutorService createExecutor() {
/* 299 */     return Executors.newFixedThreadPool(getTaskCount());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class InitializationTask
/*     */     implements Callable<T>
/*     */   {
/*     */     private final ExecutorService execFinally;
/*     */ 
/*     */ 
/*     */     
/*     */     InitializationTask(ExecutorService exec) {
/* 313 */       this.execFinally = exec;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public T call() throws Exception {
/*     */       try {
/* 325 */         return (T)BackgroundInitializer.this.initialize();
/*     */       } finally {
/* 327 */         if (this.execFinally != null)
/* 328 */           this.execFinally.shutdown(); 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\concurrent\BackgroundInitializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */