/*     */ package org.apache.commons.lang3.function;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.lang.reflect.UndeclaredThrowableException;
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
/*     */ import org.apache.commons.lang3.stream.Streams;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Failable
/*     */ {
/*     */   public static <T, U, E extends Throwable> void accept(FailableBiConsumer<T, U, E> consumer, T object1, U object2) {
/*  85 */     run(() -> consumer.accept(object1, object2));
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
/*     */   public static <T, E extends Throwable> void accept(FailableConsumer<T, E> consumer, T object) {
/*  97 */     run(() -> consumer.accept(object));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Throwable> void accept(FailableDoubleConsumer<E> consumer, double value) {
/* 108 */     run(() -> consumer.accept(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Throwable> void accept(FailableIntConsumer<E> consumer, int value) {
/* 119 */     run(() -> consumer.accept(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Throwable> void accept(FailableLongConsumer<E> consumer, long value) {
/* 130 */     run(() -> consumer.accept(value));
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
/*     */   public static <T, U, R, E extends Throwable> R apply(FailableBiFunction<T, U, R, E> function, T input1, U input2) {
/* 147 */     return get(() -> function.apply(input1, input2));
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
/*     */   public static <T, R, E extends Throwable> R apply(FailableFunction<T, R, E> function, T input) {
/* 161 */     return get(() -> function.apply(input));
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
/*     */   public static <E extends Throwable> double applyAsDouble(FailableDoubleBinaryOperator<E> function, double left, double right) {
/* 175 */     return getAsDouble(() -> function.applyAsDouble(left, right));
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
/*     */   public static <T, U> BiConsumer<T, U> asBiConsumer(FailableBiConsumer<T, U, ?> consumer) {
/* 187 */     return (input1, input2) -> accept(consumer, input1, input2);
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
/*     */   public static <T, U, R> BiFunction<T, U, R> asBiFunction(FailableBiFunction<T, U, R, ?> function) {
/* 200 */     return (input1, input2) -> apply(function, input1, input2);
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
/*     */   public static <T, U> BiPredicate<T, U> asBiPredicate(FailableBiPredicate<T, U, ?> predicate) {
/* 212 */     return (input1, input2) -> test(predicate, input1, input2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <V> Callable<V> asCallable(FailableCallable<V, ?> callable) {
/* 223 */     return () -> call(callable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Consumer<T> asConsumer(FailableConsumer<T, ?> consumer) {
/* 234 */     return input -> accept(consumer, input);
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
/*     */   public static <T, R> Function<T, R> asFunction(FailableFunction<T, R, ?> function) {
/* 246 */     return input -> apply(function, input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> asPredicate(FailablePredicate<T, ?> predicate) {
/* 257 */     return input -> test(predicate, input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Runnable asRunnable(FailableRunnable<?> runnable) {
/* 267 */     return () -> run(runnable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Supplier<T> asSupplier(FailableSupplier<T, ?> supplier) {
/* 278 */     return () -> get(supplier);
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
/*     */   public static <V, E extends Throwable> V call(FailableCallable<V, E> callable) {
/* 290 */     return get(callable::call);
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
/*     */   public static <T, E extends Throwable> T get(FailableSupplier<T, E> supplier) {
/*     */     try {
/* 303 */       return supplier.get();
/* 304 */     } catch (Throwable t) {
/* 305 */       throw rethrow(t);
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
/*     */   public static <E extends Throwable> boolean getAsBoolean(FailableBooleanSupplier<E> supplier) {
/*     */     try {
/* 318 */       return supplier.getAsBoolean();
/* 319 */     } catch (Throwable t) {
/* 320 */       throw rethrow(t);
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
/*     */   public static <E extends Throwable> double getAsDouble(FailableDoubleSupplier<E> supplier) {
/*     */     try {
/* 333 */       return supplier.getAsDouble();
/* 334 */     } catch (Throwable t) {
/* 335 */       throw rethrow(t);
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
/*     */   public static <E extends Throwable> int getAsInt(FailableIntSupplier<E> supplier) {
/*     */     try {
/* 348 */       return supplier.getAsInt();
/* 349 */     } catch (Throwable t) {
/* 350 */       throw rethrow(t);
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
/*     */   public static <E extends Throwable> long getAsLong(FailableLongSupplier<E> supplier) {
/*     */     try {
/* 363 */       return supplier.getAsLong();
/* 364 */     } catch (Throwable t) {
/* 365 */       throw rethrow(t);
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
/*     */   public static <E extends Throwable> short getAsShort(FailableShortSupplier<E> supplier) {
/*     */     try {
/* 378 */       return supplier.getAsShort();
/* 379 */     } catch (Throwable t) {
/* 380 */       throw rethrow(t);
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
/*     */   
/*     */   public static RuntimeException rethrow(Throwable throwable) {
/* 410 */     Objects.requireNonNull(throwable, "throwable");
/* 411 */     ExceptionUtils.throwUnchecked(throwable);
/* 412 */     if (throwable instanceof IOException) {
/* 413 */       throw new UncheckedIOException((IOException)throwable);
/*     */     }
/* 415 */     throw new UndeclaredThrowableException(throwable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Throwable> void run(FailableRunnable<E> runnable) {
/*     */     try {
/* 426 */       runnable.run();
/* 427 */     } catch (Throwable t) {
/* 428 */       throw rethrow(t);
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
/*     */   public static <E> Streams.FailableStream<E> stream(Collection<E> collection) {
/* 445 */     return new Streams.FailableStream(collection.stream());
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
/*     */   public static <T> Streams.FailableStream<T> stream(Stream<T> stream) {
/* 459 */     return new Streams.FailableStream(stream);
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
/*     */   public static <T, U, E extends Throwable> boolean test(FailableBiPredicate<T, U, E> predicate, T object1, U object2) {
/* 475 */     return getAsBoolean(() -> predicate.test(object1, object2));
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
/*     */   public static <T, E extends Throwable> boolean test(FailablePredicate<T, E> predicate, T object) {
/* 488 */     return getAsBoolean(() -> predicate.test(object));
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
/*     */   @SafeVarargs
/*     */   public static void tryWithResources(FailableRunnable<? extends Throwable> action, FailableConsumer<Throwable, ? extends Throwable> errorHandler, FailableRunnable<? extends Throwable>... resources) {
/*     */     FailableConsumer<Throwable, ? extends Throwable> actualErrorHandler;
/* 515 */     if (errorHandler == null) {
/* 516 */       actualErrorHandler = Failable::rethrow;
/*     */     } else {
/* 518 */       actualErrorHandler = errorHandler;
/*     */     } 
/* 520 */     Streams.of((Object[])resources).forEach(r -> (FailableRunnable)Objects.<FailableRunnable>requireNonNull(r, "runnable"));
/* 521 */     Throwable th = null;
/*     */     try {
/* 523 */       action.run();
/* 524 */     } catch (Throwable t) {
/* 525 */       th = t;
/*     */     } 
/* 527 */     if (resources != null) {
/* 528 */       for (FailableRunnable<?> runnable : resources) {
/*     */         try {
/* 530 */           runnable.run();
/* 531 */         } catch (Throwable t) {
/* 532 */           if (th == null) {
/* 533 */             th = t;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/* 538 */     if (th != null) {
/*     */       try {
/* 540 */         actualErrorHandler.accept(th);
/* 541 */       } catch (Throwable t) {
/* 542 */         throw rethrow(t);
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
/*     */ 
/*     */ 
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
/* 567 */     tryWithResources(action, null, resources);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\function\Failable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */