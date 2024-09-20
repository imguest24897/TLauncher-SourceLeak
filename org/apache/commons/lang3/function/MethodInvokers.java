/*     */ package org.apache.commons.lang3.function;
/*     */ 
/*     */ import java.lang.invoke.MethodHandle;
/*     */ import java.lang.invoke.MethodHandleProxies;
/*     */ import java.lang.invoke.MethodHandles;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import org.apache.commons.lang3.exception.UncheckedIllegalAccessException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MethodInvokers
/*     */ {
/*     */   public static <T, U> BiConsumer<T, U> asBiConsumer(Method method) {
/*  85 */     return asInterfaceInstance((Class)BiConsumer.class, method);
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
/*     */   public static <T, U, R> BiFunction<T, U, R> asBiFunction(Method method) {
/* 110 */     return asInterfaceInstance((Class)BiFunction.class, method);
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
/*     */   public static <T, U> FailableBiConsumer<T, U, Throwable> asFailableBiConsumer(Method method) {
/* 125 */     return asInterfaceInstance((Class)FailableBiConsumer.class, method);
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
/*     */   public static <T, U, R> FailableBiFunction<T, U, R, Throwable> asFailableBiFunction(Method method) {
/* 141 */     return asInterfaceInstance((Class)FailableBiFunction.class, method);
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
/*     */   public static <T, R> FailableFunction<T, R, Throwable> asFailableFunction(Method method) {
/* 155 */     return asInterfaceInstance((Class)FailableFunction.class, method);
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
/*     */   public static <R> FailableSupplier<R, Throwable> asFailableSupplier(Method method) {
/* 171 */     return asInterfaceInstance((Class)FailableSupplier.class, method);
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
/*     */   public static <T, R> Function<T, R> asFunction(Method method) {
/* 194 */     return asInterfaceInstance((Class)Function.class, method);
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
/*     */   public static <T> T asInterfaceInstance(Class<T> interfaceClass, Method method) {
/* 210 */     return MethodHandleProxies.asInterfaceInstance(Objects.<Class<T>>requireNonNull(interfaceClass, "interfaceClass"), unreflectUnchecked(method));
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
/*     */   public static <R> Supplier<R> asSupplier(Method method) {
/* 226 */     return asInterfaceInstance((Class)Supplier.class, method);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Method requireMethod(Method method) {
/* 237 */     return Objects.<Method>requireNonNull(method, "method");
/*     */   }
/*     */   
/*     */   private static MethodHandle unreflect(Method method) throws IllegalAccessException {
/* 241 */     return MethodHandles.lookup().unreflect(requireMethod(method));
/*     */   }
/*     */   
/*     */   private static MethodHandle unreflectUnchecked(Method method) {
/*     */     try {
/* 246 */       return unreflect(method);
/* 247 */     } catch (IllegalAccessException e) {
/* 248 */       throw new UncheckedIllegalAccessException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\function\MethodInvokers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */