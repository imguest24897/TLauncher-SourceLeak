/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.lang.reflect.UndeclaredThrowableException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.commons.lang3.exception.ExceptionUtils;
/*     */ import org.apache.commons.lang3.function.Failable;
/*     */ import org.apache.commons.lang3.function.FailableBooleanSupplier;
/*     */ import org.apache.commons.lang3.function.FailableRunnable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class Functions
/*     */ {
/*     */   public static <O1, O2, T extends Throwable> void accept(FailableBiConsumer<O1, O2, T> consumer, O1 object1, O2 object2) {
/* 297 */     run(() -> consumer.accept(object1, object2));
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
/*     */   public static <O, T extends Throwable> void accept(FailableConsumer<O, T> consumer, O object) {
/* 309 */     run(() -> consumer.accept(object));
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
/*     */   public static <O1, O2, O, T extends Throwable> O apply(FailableBiFunction<O1, O2, O, T> function, O1 input1, O2 input2) {
/* 326 */     return get(() -> function.apply(input1, input2));
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
/*     */   public static <I, O, T extends Throwable> O apply(FailableFunction<I, O, T> function, I input) {
/* 340 */     return get(() -> function.apply(input));
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
/*     */   public static <O1, O2> BiConsumer<O1, O2> asBiConsumer(FailableBiConsumer<O1, O2, ?> consumer) {
/* 353 */     return (input1, input2) -> accept(consumer, input1, input2);
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
/*     */   public static <O1, O2, O> BiFunction<O1, O2, O> asBiFunction(FailableBiFunction<O1, O2, O, ?> function) {
/* 367 */     return (input1, input2) -> apply(function, input1, input2);
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
/*     */   public static <O1, O2> BiPredicate<O1, O2> asBiPredicate(FailableBiPredicate<O1, O2, ?> predicate) {
/* 380 */     return (input1, input2) -> test(predicate, input1, input2);
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
/*     */   public static <O> Callable<O> asCallable(FailableCallable<O, ?> callable) {
/* 392 */     return () -> call(callable);
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
/*     */   public static <I> Consumer<I> asConsumer(FailableConsumer<I, ?> consumer) {
/* 404 */     return input -> accept(consumer, input);
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
/*     */   public static <I, O> Function<I, O> asFunction(FailableFunction<I, O, ?> function) {
/* 417 */     return input -> apply(function, input);
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
/*     */   public static <I> Predicate<I> asPredicate(FailablePredicate<I, ?> predicate) {
/* 429 */     return input -> test(predicate, input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Runnable asRunnable(FailableRunnable<?> runnable) {
/* 440 */     return () -> run(runnable);
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
/*     */   public static <O> Supplier<O> asSupplier(FailableSupplier<O, ?> supplier) {
/* 452 */     return () -> get(supplier);
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
/*     */   public static <O, T extends Throwable> O call(FailableCallable<O, T> callable) {
/* 464 */     return get(callable::call);
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
/*     */   public static <O, T extends Throwable> O get(FailableSupplier<O, T> supplier) {
/*     */     try {
/* 478 */       return supplier.get();
/* 479 */     } catch (Throwable t) {
/* 480 */       throw rethrow(t);
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
/*     */   private static <T extends Throwable> boolean getAsBoolean(FailableBooleanSupplier<T> supplier) {
/*     */     try {
/* 493 */       return supplier.getAsBoolean();
/* 494 */     } catch (Throwable t) {
/* 495 */       throw rethrow(t);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RuntimeException rethrow(Throwable throwable) {
/* 524 */     Objects.requireNonNull(throwable, "throwable");
/* 525 */     ExceptionUtils.throwUnchecked(throwable);
/* 526 */     if (throwable instanceof IOException) {
/* 527 */       throw new UncheckedIOException((IOException)throwable);
/*     */     }
/* 529 */     throw new UndeclaredThrowableException(throwable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Throwable> void run(FailableRunnable<T> runnable) {
/*     */     try {
/* 540 */       runnable.run();
/* 541 */     } catch (Throwable t) {
/* 542 */       throw rethrow(t);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <O> Streams.FailableStream<O> stream(Collection<O> collection) {
/* 559 */     return new Streams.FailableStream<>(collection.stream());
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
/*     */   public static <O> Streams.FailableStream<O> stream(Stream<O> stream) {
/* 574 */     return new Streams.FailableStream<>(stream);
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
/*     */   public static <O1, O2, T extends Throwable> boolean test(FailableBiPredicate<O1, O2, T> predicate, O1 object1, O2 object2) {
/* 590 */     return getAsBoolean(() -> predicate.test(object1, object2));
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
/*     */   public static <O, T extends Throwable> boolean test(FailablePredicate<O, T> predicate, O object) {
/* 603 */     return getAsBoolean(() -> predicate.test(object));
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
/*     */   @SafeVarargs
/*     */   public static void tryWithResources(FailableRunnable<? extends Throwable> action, FailableConsumer<Throwable, ? extends Throwable> errorHandler, FailableRunnable<? extends Throwable>... resources) {
/* 630 */     FailableRunnable[] arrayOfFailableRunnable = new FailableRunnable[resources.length];
/* 631 */     Arrays.setAll(arrayOfFailableRunnable, i -> ());
/* 632 */     Failable.tryWithResources(action::run, (errorHandler != null) ? errorHandler::accept : null, arrayOfFailableRunnable);
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
/*     */   @SafeVarargs
/*     */   public static void tryWithResources(FailableRunnable<? extends Throwable> action, FailableRunnable<? extends Throwable>... resources) {
/* 656 */     tryWithResources(action, null, resources);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   @FunctionalInterface
/*     */   public static interface FailableSupplier<R, T extends Throwable> {
/*     */     R get() throws T;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   @FunctionalInterface
/*     */   public static interface FailableRunnable<T extends Throwable> {
/*     */     void run() throws T;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   @FunctionalInterface
/*     */   public static interface FailablePredicate<I, T extends Throwable> {
/*     */     boolean test(I param1I) throws T;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   @FunctionalInterface
/*     */   public static interface FailableFunction<I, R, T extends Throwable> {
/*     */     R apply(I param1I) throws T;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   @FunctionalInterface
/*     */   public static interface FailableConsumer<O, T extends Throwable> {
/*     */     void accept(O param1O) throws T;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   @FunctionalInterface
/*     */   public static interface FailableCallable<R, T extends Throwable> {
/*     */     R call() throws T;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   @FunctionalInterface
/*     */   public static interface FailableBiPredicate<O1, O2, T extends Throwable> {
/*     */     boolean test(O1 param1O1, O2 param1O2) throws T;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   @FunctionalInterface
/*     */   public static interface FailableBiFunction<O1, O2, R, T extends Throwable> {
/*     */     R apply(O1 param1O1, O2 param1O2) throws T;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   @FunctionalInterface
/*     */   public static interface FailableBiConsumer<O1, O2, T extends Throwable> {
/*     */     void accept(O1 param1O1, O2 param1O2) throws T;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\Functions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */