/*     */ package org.checkerframework.checker.nullness;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Opt
/*     */ {
/*     */   private Opt() {
/*  37 */     throw new AssertionError("shouldn't be instantiated");
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
/*     */   public static <T> T get(T primary) {
/*  50 */     if (primary == null) {
/*  51 */       throw new NoSuchElementException("No value present");
/*     */     }
/*  53 */     return primary;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EnsuresNonNullIf(expression = {"#1"}, result = true)
/*     */   public static boolean isPresent(Object primary) {
/*  63 */     return (primary != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> void ifPresent(T primary, Consumer<? super T> consumer) {
/*  72 */     if (primary != null) {
/*  73 */       consumer.accept(primary);
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
/*     */   public static <T> T filter(T primary, Predicate<? super T> predicate) {
/*  85 */     if (primary == null) {
/*  86 */       return null;
/*     */     }
/*  88 */     return predicate.test(primary) ? primary : null;
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
/*     */   public static <T, U> U map(T primary, Function<? super T, ? extends U> mapper) {
/* 100 */     if (primary == null) {
/* 101 */       return null;
/*     */     }
/* 103 */     return mapper.apply(primary);
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
/*     */   public static <T> T orElse(T primary, T other) {
/* 115 */     return (primary != null) ? primary : other;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T orElseGet(T primary, Supplier<? extends T> other) {
/* 125 */     return (primary != null) ? primary : other.get();
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
/*     */   public static <T, X extends Throwable> T orElseThrow(T primary, Supplier<? extends X> exceptionSupplier) throws X {
/* 137 */     if (primary != null) {
/* 138 */       return primary;
/*     */     }
/* 140 */     throw (X)exceptionSupplier.get();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\checker\nullness\Opt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */