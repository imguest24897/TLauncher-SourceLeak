/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.util.concurrent.internal.InternalFutureFailureAccess;
/*     */ import com.google.common.util.concurrent.internal.InternalFutures;
/*     */ import com.google.errorprone.annotations.ForOverride;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
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
/*     */ @GwtCompatible
/*     */ abstract class AbstractCatchingFuture<V, X extends Throwable, F, T>
/*     */   extends FluentFuture.TrustedFuture<V>
/*     */   implements Runnable
/*     */ {
/*     */   ListenableFuture<? extends V> inputFuture;
/*     */   Class<X> exceptionType;
/*     */   F fallback;
/*     */   
/*     */   static <V, X extends Throwable> ListenableFuture<V> create(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback, Executor executor) {
/*  40 */     CatchingFuture<V, X> future = new CatchingFuture<>(input, exceptionType, fallback);
/*  41 */     input.addListener(future, MoreExecutors.rejectionPropagatingExecutor(executor, future));
/*  42 */     return future;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <X extends Throwable, V> ListenableFuture<V> create(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback, Executor executor) {
/*  50 */     AsyncCatchingFuture<V, X> future = new AsyncCatchingFuture<>(input, exceptionType, fallback);
/*  51 */     input.addListener(future, MoreExecutors.rejectionPropagatingExecutor(executor, future));
/*  52 */     return future;
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
/*     */   AbstractCatchingFuture(ListenableFuture<? extends V> inputFuture, Class<X> exceptionType, F fallback) {
/*  65 */     this.inputFuture = (ListenableFuture<? extends V>)Preconditions.checkNotNull(inputFuture);
/*  66 */     this.exceptionType = (Class<X>)Preconditions.checkNotNull(exceptionType);
/*  67 */     this.fallback = (F)Preconditions.checkNotNull(fallback);
/*     */   }
/*     */   
/*     */   public final void run() {
/*     */     T fallbackResult;
/*  72 */     ListenableFuture<? extends V> localInputFuture = this.inputFuture;
/*  73 */     Class<X> localExceptionType = this.exceptionType;
/*  74 */     F localFallback = this.fallback;
/*  75 */     if ((((localInputFuture == null) ? 1 : 0) | ((localExceptionType == null) ? 1 : 0) | ((localFallback == null) ? 1 : 0)) != 0 || 
/*     */       
/*  77 */       isCancelled()) {
/*     */       return;
/*     */     }
/*  80 */     this.inputFuture = null;
/*     */ 
/*     */     
/*  83 */     V sourceResult = null;
/*  84 */     Throwable throwable = null;
/*     */     try {
/*  86 */       if (localInputFuture instanceof InternalFutureFailureAccess)
/*     */       {
/*  88 */         throwable = InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess)localInputFuture);
/*     */       }
/*     */       
/*  91 */       if (throwable == null) {
/*  92 */         sourceResult = Futures.getDone((Future)localInputFuture);
/*     */       }
/*  94 */     } catch (ExecutionException e) {
/*  95 */       throwable = e.getCause();
/*  96 */       if (throwable == null)
/*     */       {
/*     */ 
/*     */         
/* 100 */         String str1 = String.valueOf(localInputFuture.getClass());
/*     */         
/* 102 */         String str2 = String.valueOf(e.getClass()); throwable = new NullPointerException((new StringBuilder(35 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("Future type ").append(str1).append(" threw ").append(str2).append(" without a cause").toString());
/*     */       }
/*     */     
/* 105 */     } catch (Throwable e) {
/* 106 */       throwable = e;
/*     */     } 
/*     */     
/* 109 */     if (throwable == null) {
/* 110 */       set(sourceResult);
/*     */       
/*     */       return;
/*     */     } 
/* 114 */     if (!Platform.isInstanceOfThrowableClass(throwable, localExceptionType)) {
/* 115 */       setFuture(localInputFuture);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 121 */     Throwable throwable1 = throwable;
/*     */     
/*     */     try {
/* 124 */       fallbackResult = doFallback(localFallback, (X)throwable1);
/* 125 */     } catch (Throwable t) {
/* 126 */       setException(t);
/*     */       return;
/*     */     } finally {
/* 129 */       this.exceptionType = null;
/* 130 */       this.fallback = null;
/*     */     } 
/*     */     
/* 133 */     setResult(fallbackResult);
/*     */   }
/*     */ 
/*     */   
/*     */   protected String pendingToString() {
/* 138 */     ListenableFuture<? extends V> localInputFuture = this.inputFuture;
/* 139 */     Class<X> localExceptionType = this.exceptionType;
/* 140 */     F localFallback = this.fallback;
/* 141 */     String superString = super.pendingToString();
/* 142 */     String resultString = "";
/* 143 */     if (localInputFuture != null) {
/* 144 */       String str = String.valueOf(localInputFuture); resultString = (new StringBuilder(16 + String.valueOf(str).length())).append("inputFuture=[").append(str).append("], ").toString();
/*     */     } 
/* 146 */     if (localExceptionType != null && localFallback != null) {
/* 147 */       String str1 = resultString, str2 = String.valueOf(localExceptionType), str3 = String.valueOf(localFallback); return (new StringBuilder(29 + String.valueOf(str1).length() + String.valueOf(str2).length() + String.valueOf(str3).length())).append(str1).append("exceptionType=[").append(str2).append("], fallback=[").append(str3).append("]").toString();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 153 */     if (superString != null) {
/* 154 */       String.valueOf(superString); return (String.valueOf(superString).length() != 0) ? String.valueOf(resultString).concat(String.valueOf(superString)) : new String(String.valueOf(resultString));
/*     */     } 
/* 156 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @ForOverride
/*     */   abstract T doFallback(F paramF, X paramX) throws Exception;
/*     */ 
/*     */   
/*     */   @ForOverride
/*     */   abstract void setResult(T paramT);
/*     */ 
/*     */   
/*     */   protected final void afterDone() {
/* 169 */     maybePropagateCancellationTo(this.inputFuture);
/* 170 */     this.inputFuture = null;
/* 171 */     this.exceptionType = null;
/* 172 */     this.fallback = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class AsyncCatchingFuture<V, X extends Throwable>
/*     */     extends AbstractCatchingFuture<V, X, AsyncFunction<? super X, ? extends V>, ListenableFuture<? extends V>>
/*     */   {
/*     */     AsyncCatchingFuture(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback) {
/* 186 */       super(input, exceptionType, fallback);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     ListenableFuture<? extends V> doFallback(AsyncFunction<? super X, ? extends V> fallback, X cause) throws Exception {
/* 192 */       ListenableFuture<? extends V> replacement = fallback.apply(cause);
/* 193 */       Preconditions.checkNotNull(replacement, "AsyncFunction.apply returned null instead of a Future. Did you mean to return immediateFuture(null)? %s", fallback);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 198 */       return replacement;
/*     */     }
/*     */ 
/*     */     
/*     */     void setResult(ListenableFuture<? extends V> result) {
/* 203 */       setFuture(result);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class CatchingFuture<V, X extends Throwable>
/*     */     extends AbstractCatchingFuture<V, X, Function<? super X, ? extends V>, V>
/*     */   {
/*     */     CatchingFuture(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback) {
/* 217 */       super(input, exceptionType, fallback);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     V doFallback(Function<? super X, ? extends V> fallback, X cause) throws Exception {
/* 223 */       return (V)fallback.apply(cause);
/*     */     }
/*     */ 
/*     */     
/*     */     void setResult(V result) {
/* 228 */       set(result);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\commo\\util\concurrent\AbstractCatchingFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */