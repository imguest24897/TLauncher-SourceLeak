/*     */ package org.apache.commons.lang3.concurrent;
/*     */ 
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.function.Function;
/*     */ import org.apache.commons.lang3.exception.ExceptionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Memoizer<I, O>
/*     */   implements Computable<I, O>
/*     */ {
/*  50 */   private final ConcurrentMap<I, Future<O>> cache = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Function<? super I, ? extends Future<O>> mappingFunction;
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean recalculate;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Memoizer(Computable<I, O> computable) {
/*  65 */     this(computable, false);
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
/*     */   public Memoizer(Computable<I, O> computable, boolean recalculate) {
/*  77 */     this.recalculate = recalculate;
/*  78 */     this.mappingFunction = (k -> FutureTasks.run(()));
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
/*     */   public Memoizer(Function<I, O> function) {
/*  93 */     this(function, false);
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
/*     */   public Memoizer(Function<I, O> function, boolean recalculate) {
/* 106 */     this.recalculate = recalculate;
/* 107 */     this.mappingFunction = (k -> FutureTasks.run(()));
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
/*     */   public O compute(I arg) throws InterruptedException {
/*     */     while (true) {
/* 126 */       Future<O> future = this.cache.computeIfAbsent(arg, this.mappingFunction);
/*     */       try {
/* 128 */         return future.get();
/* 129 */       } catch (CancellationException e) {
/* 130 */         this.cache.remove(arg, future);
/* 131 */       } catch (ExecutionException e) {
/* 132 */         if (this.recalculate) {
/* 133 */           this.cache.remove(arg, future);
/*     */         }
/* 135 */         throw launderException(e.getCause());
/*     */       } 
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
/*     */   private RuntimeException launderException(Throwable throwable) {
/* 148 */     throw new IllegalStateException("Unchecked exception", (Throwable)ExceptionUtils.throwUnchecked(throwable));
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\concurrent\Memoizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */