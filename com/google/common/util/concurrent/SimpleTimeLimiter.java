/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ObjectArrays;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class SimpleTimeLimiter
/*     */   implements TimeLimiter
/*     */ {
/*     */   private final ExecutorService executor;
/*     */   
/*     */   private SimpleTimeLimiter(ExecutorService executor) {
/*  53 */     this.executor = (ExecutorService)Preconditions.checkNotNull(executor);
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
/*     */   public static SimpleTimeLimiter create(ExecutorService executor) {
/*  68 */     return new SimpleTimeLimiter(executor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T newProxy(final T target, Class<T> interfaceType, final long timeoutDuration, final TimeUnit timeoutUnit) {
/*  77 */     Preconditions.checkNotNull(target);
/*  78 */     Preconditions.checkNotNull(interfaceType);
/*  79 */     Preconditions.checkNotNull(timeoutUnit);
/*  80 */     checkPositiveTimeout(timeoutDuration);
/*  81 */     Preconditions.checkArgument(interfaceType.isInterface(), "interfaceType must be an interface type");
/*     */     
/*  83 */     final Set<Method> interruptibleMethods = findInterruptibleMethods(interfaceType);
/*     */     
/*  85 */     InvocationHandler handler = new InvocationHandler()
/*     */       {
/*     */         
/*     */         public Object invoke(Object obj, final Method method, final Object[] args) throws Throwable
/*     */         {
/*  90 */           Callable<Object> callable = new Callable()
/*     */             {
/*     */               public Object call() throws Exception
/*     */               {
/*     */                 try {
/*  95 */                   return method.invoke(target, args);
/*  96 */                 } catch (InvocationTargetException e) {
/*  97 */                   throw SimpleTimeLimiter.throwCause(e, false);
/*     */                 } 
/*     */               }
/*     */             };
/* 101 */           return SimpleTimeLimiter.this.callWithTimeout((Callable)callable, timeoutDuration, timeoutUnit, interruptibleMethods
/* 102 */               .contains(method));
/*     */         }
/*     */       };
/* 105 */     return newProxy(interfaceType, handler);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> T newProxy(Class<T> interfaceType, InvocationHandler handler) {
/* 111 */     Object object = Proxy.newProxyInstance(interfaceType
/* 112 */         .getClassLoader(), new Class[] { interfaceType }, handler);
/* 113 */     return interfaceType.cast(object);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private <T> T callWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit, boolean amInterruptible) throws Exception {
/* 119 */     Preconditions.checkNotNull(callable);
/* 120 */     Preconditions.checkNotNull(timeoutUnit);
/* 121 */     checkPositiveTimeout(timeoutDuration);
/*     */     
/* 123 */     Future<T> future = this.executor.submit(callable);
/*     */     
/*     */     try {
/* 126 */       if (amInterruptible) {
/*     */         try {
/* 128 */           return future.get(timeoutDuration, timeoutUnit);
/* 129 */         } catch (InterruptedException e) {
/* 130 */           future.cancel(true);
/* 131 */           throw e;
/*     */         } 
/*     */       }
/* 134 */       return Uninterruptibles.getUninterruptibly(future, timeoutDuration, timeoutUnit);
/*     */     }
/* 136 */     catch (ExecutionException e) {
/* 137 */       throw throwCause(e, true);
/* 138 */     } catch (TimeoutException e) {
/* 139 */       future.cancel(true);
/* 140 */       throw new UncheckedTimeoutException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public <T> T callWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit) throws TimeoutException, InterruptedException, ExecutionException {
/* 148 */     Preconditions.checkNotNull(callable);
/* 149 */     Preconditions.checkNotNull(timeoutUnit);
/* 150 */     checkPositiveTimeout(timeoutDuration);
/*     */     
/* 152 */     Future<T> future = this.executor.submit(callable);
/*     */     
/*     */     try {
/* 155 */       return future.get(timeoutDuration, timeoutUnit);
/* 156 */     } catch (InterruptedException|TimeoutException e) {
/* 157 */       future.cancel(true);
/* 158 */       throw e;
/* 159 */     } catch (ExecutionException e) {
/* 160 */       wrapAndThrowExecutionExceptionOrError(e.getCause());
/* 161 */       throw new AssertionError();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public <T> T callUninterruptiblyWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit) throws TimeoutException, ExecutionException {
/* 170 */     Preconditions.checkNotNull(callable);
/* 171 */     Preconditions.checkNotNull(timeoutUnit);
/* 172 */     checkPositiveTimeout(timeoutDuration);
/*     */     
/* 174 */     Future<T> future = this.executor.submit(callable);
/*     */     
/*     */     try {
/* 177 */       return Uninterruptibles.getUninterruptibly(future, timeoutDuration, timeoutUnit);
/* 178 */     } catch (TimeoutException e) {
/* 179 */       future.cancel(true);
/* 180 */       throw e;
/* 181 */     } catch (ExecutionException e) {
/* 182 */       wrapAndThrowExecutionExceptionOrError(e.getCause());
/* 183 */       throw new AssertionError();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void runWithTimeout(Runnable runnable, long timeoutDuration, TimeUnit timeoutUnit) throws TimeoutException, InterruptedException {
/* 190 */     Preconditions.checkNotNull(runnable);
/* 191 */     Preconditions.checkNotNull(timeoutUnit);
/* 192 */     checkPositiveTimeout(timeoutDuration);
/*     */     
/* 194 */     Future<?> future = this.executor.submit(runnable);
/*     */     
/*     */     try {
/* 197 */       future.get(timeoutDuration, timeoutUnit);
/* 198 */     } catch (InterruptedException|TimeoutException e) {
/* 199 */       future.cancel(true);
/* 200 */       throw e;
/* 201 */     } catch (ExecutionException e) {
/* 202 */       wrapAndThrowRuntimeExecutionExceptionOrError(e.getCause());
/* 203 */       throw new AssertionError();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void runUninterruptiblyWithTimeout(Runnable runnable, long timeoutDuration, TimeUnit timeoutUnit) throws TimeoutException {
/* 210 */     Preconditions.checkNotNull(runnable);
/* 211 */     Preconditions.checkNotNull(timeoutUnit);
/* 212 */     checkPositiveTimeout(timeoutDuration);
/*     */     
/* 214 */     Future<?> future = this.executor.submit(runnable);
/*     */     
/*     */     try {
/* 217 */       Uninterruptibles.getUninterruptibly(future, timeoutDuration, timeoutUnit);
/* 218 */     } catch (TimeoutException e) {
/* 219 */       future.cancel(true);
/* 220 */       throw e;
/* 221 */     } catch (ExecutionException e) {
/* 222 */       wrapAndThrowRuntimeExecutionExceptionOrError(e.getCause());
/* 223 */       throw new AssertionError();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Exception throwCause(Exception e, boolean combineStackTraces) throws Exception {
/* 228 */     Throwable cause = e.getCause();
/* 229 */     if (cause == null) {
/* 230 */       throw e;
/*     */     }
/* 232 */     if (combineStackTraces) {
/*     */       
/* 234 */       StackTraceElement[] combined = (StackTraceElement[])ObjectArrays.concat((Object[])cause.getStackTrace(), (Object[])e.getStackTrace(), StackTraceElement.class);
/* 235 */       cause.setStackTrace(combined);
/*     */     } 
/* 237 */     if (cause instanceof Exception) {
/* 238 */       throw (Exception)cause;
/*     */     }
/* 240 */     if (cause instanceof Error) {
/* 241 */       throw (Error)cause;
/*     */     }
/*     */     
/* 244 */     throw e;
/*     */   }
/*     */   
/*     */   private static Set<Method> findInterruptibleMethods(Class<?> interfaceType) {
/* 248 */     Set<Method> set = Sets.newHashSet();
/* 249 */     for (Method m : interfaceType.getMethods()) {
/* 250 */       if (declaresInterruptedEx(m)) {
/* 251 */         set.add(m);
/*     */       }
/*     */     } 
/* 254 */     return set;
/*     */   }
/*     */   
/*     */   private static boolean declaresInterruptedEx(Method method) {
/* 258 */     for (Class<?> exType : method.getExceptionTypes()) {
/*     */       
/* 260 */       if (exType == InterruptedException.class) {
/* 261 */         return true;
/*     */       }
/*     */     } 
/* 264 */     return false;
/*     */   }
/*     */   
/*     */   private void wrapAndThrowExecutionExceptionOrError(Throwable cause) throws ExecutionException {
/* 268 */     if (cause instanceof Error)
/* 269 */       throw new ExecutionError((Error)cause); 
/* 270 */     if (cause instanceof RuntimeException) {
/* 271 */       throw new UncheckedExecutionException(cause);
/*     */     }
/* 273 */     throw new ExecutionException(cause);
/*     */   }
/*     */ 
/*     */   
/*     */   private void wrapAndThrowRuntimeExecutionExceptionOrError(Throwable cause) {
/* 278 */     if (cause instanceof Error) {
/* 279 */       throw new ExecutionError((Error)cause);
/*     */     }
/* 281 */     throw new UncheckedExecutionException(cause);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void checkPositiveTimeout(long timeoutDuration) {
/* 286 */     Preconditions.checkArgument((timeoutDuration > 0L), "timeout must be positive: %s", timeoutDuration);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\commo\\util\concurrent\SimpleTimeLimiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */