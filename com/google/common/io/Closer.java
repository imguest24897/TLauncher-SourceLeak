/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.logging.Level;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class Closer
/*     */   implements Closeable
/*     */ {
/*  95 */   private static final Suppressor SUPPRESSOR = SuppressingSuppressor.isAvailable() ? 
/*  96 */     SuppressingSuppressor.INSTANCE : 
/*  97 */     LoggingSuppressor.INSTANCE;
/*     */ 
/*     */   
/*     */   public static Closer create() {
/* 101 */     return new Closer(SUPPRESSOR);
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   final Suppressor suppressor;
/* 107 */   private final Deque<Closeable> stack = new ArrayDeque<>(4);
/*     */   private Throwable thrown;
/*     */   
/*     */   @VisibleForTesting
/*     */   Closer(Suppressor suppressor) {
/* 112 */     this.suppressor = (Suppressor)Preconditions.checkNotNull(suppressor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public <C extends Closeable> C register(C closeable) {
/* 124 */     if (closeable != null) {
/* 125 */       this.stack.addFirst((Closeable)closeable);
/*     */     }
/*     */     
/* 128 */     return closeable;
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
/*     */   public RuntimeException rethrow(Throwable e) throws IOException {
/* 145 */     Preconditions.checkNotNull(e);
/* 146 */     this.thrown = e;
/* 147 */     Throwables.propagateIfPossible(e, IOException.class);
/* 148 */     throw new RuntimeException(e);
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
/*     */   public <X extends Exception> RuntimeException rethrow(Throwable e, Class<X> declaredType) throws IOException, X {
/* 167 */     Preconditions.checkNotNull(e);
/* 168 */     this.thrown = e;
/* 169 */     Throwables.propagateIfPossible(e, IOException.class);
/* 170 */     Throwables.propagateIfPossible(e, declaredType);
/* 171 */     throw new RuntimeException(e);
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
/*     */   public <X1 extends Exception, X2 extends Exception> RuntimeException rethrow(Throwable e, Class<X1> declaredType1, Class<X2> declaredType2) throws IOException, X1, X2 {
/* 191 */     Preconditions.checkNotNull(e);
/* 192 */     this.thrown = e;
/* 193 */     Throwables.propagateIfPossible(e, IOException.class);
/* 194 */     Throwables.propagateIfPossible(e, declaredType1, declaredType2);
/* 195 */     throw new RuntimeException(e);
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
/*     */   public void close() throws IOException {
/* 207 */     Throwable throwable = this.thrown;
/*     */ 
/*     */     
/* 210 */     while (!this.stack.isEmpty()) {
/* 211 */       Closeable closeable = this.stack.removeFirst();
/*     */       try {
/* 213 */         closeable.close();
/* 214 */       } catch (Throwable e) {
/* 215 */         if (throwable == null) {
/* 216 */           throwable = e; continue;
/*     */         } 
/* 218 */         this.suppressor.suppress(closeable, throwable, e);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 223 */     if (this.thrown == null && throwable != null) {
/* 224 */       Throwables.propagateIfPossible(throwable, IOException.class);
/* 225 */       throw new AssertionError(throwable);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static interface Suppressor
/*     */   {
/*     */     void suppress(Closeable param1Closeable, Throwable param1Throwable1, Throwable param1Throwable2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class LoggingSuppressor
/*     */     implements Suppressor
/*     */   {
/* 244 */     static final LoggingSuppressor INSTANCE = new LoggingSuppressor();
/*     */ 
/*     */ 
/*     */     
/*     */     public void suppress(Closeable closeable, Throwable thrown, Throwable suppressed) {
/* 249 */       String str = String.valueOf(closeable); Closeables.logger.log(Level.WARNING, (new StringBuilder(42 + String.valueOf(str).length())).append("Suppressing exception thrown when closing ").append(str).toString(), suppressed);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class SuppressingSuppressor
/*     */     implements Suppressor
/*     */   {
/* 261 */     static final SuppressingSuppressor INSTANCE = new SuppressingSuppressor();
/*     */     
/*     */     static boolean isAvailable() {
/* 264 */       return (addSuppressed != null);
/*     */     }
/*     */     
/* 267 */     static final Method addSuppressed = addSuppressedMethodOrNull();
/*     */     
/*     */     private static Method addSuppressedMethodOrNull() {
/*     */       try {
/* 271 */         return Throwable.class.getMethod("addSuppressed", new Class[] { Throwable.class });
/* 272 */       } catch (Throwable e) {
/* 273 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void suppress(Closeable closeable, Throwable thrown, Throwable suppressed) {
/* 280 */       if (thrown == suppressed) {
/*     */         return;
/*     */       }
/*     */       try {
/* 284 */         addSuppressed.invoke(thrown, new Object[] { suppressed });
/* 285 */       } catch (Throwable e) {
/*     */         
/* 287 */         Closer.LoggingSuppressor.INSTANCE.suppress(closeable, thrown, suppressed);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\io\Closer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */