/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ @GwtIncompatible
/*     */ final class TimeoutFuture<V>
/*     */   extends FluentFuture.TrustedFuture<V>
/*     */ {
/*     */   private ListenableFuture<V> delegateRef;
/*     */   private ScheduledFuture<?> timer;
/*     */   
/*     */   static <V> ListenableFuture<V> create(ListenableFuture<V> delegate, long time, TimeUnit unit, ScheduledExecutorService scheduledExecutor) {
/*  43 */     TimeoutFuture<V> result = new TimeoutFuture<>(delegate);
/*  44 */     Fire<V> fire = new Fire<>(result);
/*  45 */     result.timer = scheduledExecutor.schedule(fire, time, unit);
/*  46 */     delegate.addListener(fire, MoreExecutors.directExecutor());
/*  47 */     return result;
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
/*     */   private TimeoutFuture(ListenableFuture<V> delegate) {
/*  78 */     this.delegateRef = (ListenableFuture<V>)Preconditions.checkNotNull(delegate);
/*     */   }
/*     */   
/*     */   private static final class Fire<V>
/*     */     implements Runnable {
/*     */     TimeoutFuture<V> timeoutFutureRef;
/*     */     
/*     */     Fire(TimeoutFuture<V> timeoutFuture) {
/*  86 */       this.timeoutFutureRef = timeoutFuture;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/*  93 */       TimeoutFuture<V> timeoutFuture = this.timeoutFutureRef;
/*  94 */       if (timeoutFuture == null) {
/*     */         return;
/*     */       }
/*  97 */       ListenableFuture<V> delegate = timeoutFuture.delegateRef;
/*  98 */       if (delegate == null) {
/*     */         return;
/*     */       }
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
/* 114 */       this.timeoutFutureRef = null;
/* 115 */       if (delegate.isDone()) {
/* 116 */         timeoutFuture.setFuture(delegate);
/*     */       } else {
/*     */         try {
/* 119 */           ScheduledFuture<?> timer = timeoutFuture.timer;
/* 120 */           timeoutFuture.timer = null;
/* 121 */           String message = "Timed out";
/*     */ 
/*     */           
/*     */           try {
/* 125 */             if (timer != null) {
/* 126 */               long overDelayMs = Math.abs(timer.getDelay(TimeUnit.MILLISECONDS));
/* 127 */               if (overDelayMs > 10L) {
/* 128 */                 String str = String.valueOf(message); message = (new StringBuilder(66 + String.valueOf(str).length())).append(str).append(" (timeout delayed by ").append(overDelayMs).append(" ms after scheduled time)").toString();
/*     */               } 
/*     */             } 
/* 131 */             String str1 = String.valueOf(message), str2 = String.valueOf(delegate); message = (new StringBuilder(2 + String.valueOf(str1).length() + String.valueOf(str2).length())).append(str1).append(": ").append(str2).toString();
/*     */           } finally {
/* 133 */             timeoutFuture.setException(new TimeoutFuture.TimeoutFutureException(message));
/*     */           } 
/*     */         } finally {
/* 136 */           delegate.cancel(true);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class TimeoutFutureException extends TimeoutException {
/*     */     private TimeoutFutureException(String message) {
/* 144 */       super(message);
/*     */     }
/*     */ 
/*     */     
/*     */     public synchronized Throwable fillInStackTrace() {
/* 149 */       setStackTrace(new StackTraceElement[0]);
/* 150 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected String pendingToString() {
/* 156 */     ListenableFuture<? extends V> localInputFuture = this.delegateRef;
/* 157 */     ScheduledFuture<?> localTimer = this.timer;
/* 158 */     if (localInputFuture != null) {
/* 159 */       String str1 = String.valueOf(localInputFuture), message = (new StringBuilder(14 + String.valueOf(str1).length())).append("inputFuture=[").append(str1).append("]").toString();
/* 160 */       if (localTimer != null) {
/* 161 */         long delay = localTimer.getDelay(TimeUnit.MILLISECONDS);
/*     */         
/* 163 */         if (delay > 0L) {
/* 164 */           String str = String.valueOf(message); message = (new StringBuilder(43 + String.valueOf(str).length())).append(str).append(", remaining delay=[").append(delay).append(" ms]").toString();
/*     */         } 
/*     */       } 
/* 167 */       return message;
/*     */     } 
/* 169 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void afterDone() {
/* 174 */     maybePropagateCancellationTo(this.delegateRef);
/*     */     
/* 176 */     Future<?> localTimer = this.timer;
/*     */ 
/*     */ 
/*     */     
/* 180 */     if (localTimer != null) {
/* 181 */       localTimer.cancel(false);
/*     */     }
/*     */     
/* 184 */     this.delegateRef = null;
/* 185 */     this.timer = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\commo\\util\concurrent\TimeoutFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */