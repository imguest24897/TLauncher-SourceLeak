/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Ordering;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CopyOnWriteArraySet;
/*     */ import java.util.concurrent.ExecutionException;
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
/*     */ @GwtIncompatible
/*     */ final class FuturesGetChecked
/*     */ {
/*     */   @CanIgnoreReturnValue
/*     */   static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass) throws X {
/*  45 */     return getChecked(bestGetCheckedTypeValidator(), future, exceptionClass);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   @VisibleForTesting
/*     */   static <V, X extends Exception> V getChecked(GetCheckedTypeValidator validator, Future<V> future, Class<X> exceptionClass) throws X {
/*  53 */     validator.validateClass(exceptionClass);
/*     */     try {
/*  55 */       return future.get();
/*  56 */     } catch (InterruptedException e) {
/*  57 */       Thread.currentThread().interrupt();
/*  58 */       throw newWithCause(exceptionClass, e);
/*  59 */     } catch (ExecutionException e) {
/*  60 */       wrapAndThrowExceptionOrError(e.getCause(), exceptionClass);
/*  61 */       throw (X)new AssertionError();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass, long timeout, TimeUnit unit) throws X {
/*  70 */     bestGetCheckedTypeValidator().validateClass(exceptionClass);
/*     */     try {
/*  72 */       return future.get(timeout, unit);
/*  73 */     } catch (InterruptedException e) {
/*  74 */       Thread.currentThread().interrupt();
/*  75 */       throw newWithCause(exceptionClass, e);
/*  76 */     } catch (TimeoutException e) {
/*  77 */       throw newWithCause(exceptionClass, e);
/*  78 */     } catch (ExecutionException e) {
/*  79 */       wrapAndThrowExceptionOrError(e.getCause(), exceptionClass);
/*  80 */       throw (X)new AssertionError();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static GetCheckedTypeValidator bestGetCheckedTypeValidator() {
/*  90 */     return GetCheckedTypeValidatorHolder.BEST_VALIDATOR;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static GetCheckedTypeValidator weakSetValidator() {
/*  95 */     return GetCheckedTypeValidatorHolder.WeakSetValidator.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static GetCheckedTypeValidator classValueValidator() {
/* 101 */     return GetCheckedTypeValidatorHolder.ClassValueValidator.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static class GetCheckedTypeValidatorHolder
/*     */   {
/* 113 */     static final String CLASS_VALUE_VALIDATOR_NAME = String.valueOf(GetCheckedTypeValidatorHolder.class.getName()).concat("$ClassValueValidator");
/*     */     
/* 115 */     static final FuturesGetChecked.GetCheckedTypeValidator BEST_VALIDATOR = getBestValidator();
/*     */     
/*     */     @IgnoreJRERequirement
/*     */     enum ClassValueValidator
/*     */       implements FuturesGetChecked.GetCheckedTypeValidator {
/* 120 */       INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 126 */       private static final ClassValue<Boolean> isValidClass = new ClassValue<Boolean>()
/*     */         {
/*     */           protected Boolean computeValue(Class<?> type)
/*     */           {
/* 130 */             FuturesGetChecked.checkExceptionClassValidity(type.asSubclass(Exception.class));
/* 131 */             return Boolean.valueOf(true);
/*     */           }
/*     */         }; static {
/*     */       
/*     */       }
/*     */       public void validateClass(Class<? extends Exception> exceptionClass) {
/* 137 */         isValidClass.get(exceptionClass);
/*     */       }
/*     */     }
/*     */     
/*     */     enum WeakSetValidator implements FuturesGetChecked.GetCheckedTypeValidator {
/* 142 */       INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 152 */       private static final Set<WeakReference<Class<? extends Exception>>> validClasses = new CopyOnWriteArraySet<>();
/*     */       static {
/*     */       
/*     */       }
/*     */       public void validateClass(Class<? extends Exception> exceptionClass) {
/* 157 */         for (WeakReference<Class<? extends Exception>> knownGood : validClasses) {
/* 158 */           if (exceptionClass.equals(knownGood.get())) {
/*     */             return;
/*     */           }
/*     */         } 
/*     */         
/* 163 */         FuturesGetChecked.checkExceptionClassValidity(exceptionClass);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 174 */         if (validClasses.size() > 1000) {
/* 175 */           validClasses.clear();
/*     */         }
/*     */         
/* 178 */         validClasses.add(new WeakReference<>(exceptionClass));
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static FuturesGetChecked.GetCheckedTypeValidator getBestValidator() {
/*     */       try {
/* 188 */         Class<?> theClass = Class.forName(CLASS_VALUE_VALIDATOR_NAME);
/* 189 */         return (FuturesGetChecked.GetCheckedTypeValidator)theClass.getEnumConstants()[0];
/* 190 */       } catch (Throwable t) {
/* 191 */         return FuturesGetChecked.weakSetValidator();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <X extends Exception> void wrapAndThrowExceptionOrError(Throwable cause, Class<X> exceptionClass) throws X {
/* 199 */     if (cause instanceof Error) {
/* 200 */       throw (X)new ExecutionError((Error)cause);
/*     */     }
/* 202 */     if (cause instanceof RuntimeException) {
/* 203 */       throw (X)new UncheckedExecutionException(cause);
/*     */     }
/* 205 */     throw newWithCause(exceptionClass, cause);
/*     */   }
/*     */   @IgnoreJRERequirement enum ClassValueValidator implements GetCheckedTypeValidator {
/*     */     INSTANCE;
/*     */     private static final ClassValue<Boolean> isValidClass = new ClassValue<Boolean>() { protected Boolean computeValue(Class<?> type) { FuturesGetChecked.checkExceptionClassValidity(type.asSubclass(Exception.class)); return Boolean.valueOf(true); } }
/*     */     ; static {  } public void validateClass(Class<? extends Exception> exceptionClass) { isValidClass.get(exceptionClass); } } enum WeakSetValidator implements GetCheckedTypeValidator {
/*     */     INSTANCE; private static final Set<WeakReference<Class<? extends Exception>>> validClasses = new CopyOnWriteArraySet<>(); static {  } public void validateClass(Class<? extends Exception> exceptionClass) { for (WeakReference<Class<? extends Exception>> knownGood : validClasses) { if (exceptionClass.equals(knownGood.get()))
/*     */           return;  }
/*     */        FuturesGetChecked.checkExceptionClassValidity(exceptionClass); if (validClasses.size() > 1000)
/*     */         validClasses.clear();  validClasses.add(new WeakReference<>(exceptionClass)); }
/*     */   } private static boolean hasConstructorUsableByGetChecked(Class<? extends Exception> exceptionClass) { try {
/* 216 */       Exception unused = newWithCause((Class)exceptionClass, new Exception());
/* 217 */       return true;
/* 218 */     } catch (Exception e) {
/* 219 */       return false;
/*     */     }  }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <X extends Exception> X newWithCause(Class<X> exceptionClass, Throwable cause) {
/* 226 */     List<Constructor<X>> constructors = (List)Arrays.asList(exceptionClass.getConstructors());
/* 227 */     for (Constructor<X> constructor : preferringStrings(constructors)) {
/* 228 */       Exception exception = newFromConstructor(constructor, cause);
/* 229 */       if (exception != null) {
/* 230 */         if (exception.getCause() == null) {
/* 231 */           exception.initCause(cause);
/*     */         }
/* 233 */         return (X)exception;
/*     */       } 
/*     */     } 
/* 236 */     String str = String.valueOf(exceptionClass); throw new IllegalArgumentException((new StringBuilder(82 + String.valueOf(str).length())).append("No appropriate constructor for exception of type ").append(str).append(" in response to chained exception").toString(), cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <X extends Exception> List<Constructor<X>> preferringStrings(List<Constructor<X>> constructors) {
/* 245 */     return WITH_STRING_PARAM_FIRST.sortedCopy(constructors);
/*     */   }
/*     */ 
/*     */   
/* 249 */   private static final Ordering<Constructor<?>> WITH_STRING_PARAM_FIRST = Ordering.natural()
/* 250 */     .onResultOf(new Function<Constructor<?>, Boolean>()
/*     */       {
/*     */         public Boolean apply(Constructor<?> input)
/*     */         {
/* 254 */           return Boolean.valueOf(Arrays.<Class<?>>asList(input.getParameterTypes()).contains(String.class));
/*     */         }
/* 257 */       }).reverse();
/*     */   
/*     */   private static <X> X newFromConstructor(Constructor<X> constructor, Throwable cause) {
/* 260 */     Class<?>[] paramTypes = constructor.getParameterTypes();
/* 261 */     Object[] params = new Object[paramTypes.length];
/* 262 */     for (int i = 0; i < paramTypes.length; i++) {
/* 263 */       Class<?> paramType = paramTypes[i];
/* 264 */       if (paramType.equals(String.class)) {
/* 265 */         params[i] = cause.toString();
/* 266 */       } else if (paramType.equals(Throwable.class)) {
/* 267 */         params[i] = cause;
/*     */       } else {
/* 269 */         return null;
/*     */       } 
/*     */     } 
/*     */     try {
/* 273 */       return constructor.newInstance(params);
/* 274 */     } catch (IllegalArgumentException|InstantiationException|IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/*     */ 
/*     */ 
/*     */       
/* 278 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static boolean isCheckedException(Class<? extends Exception> type) {
/* 284 */     return !RuntimeException.class.isAssignableFrom(type);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static void checkExceptionClassValidity(Class<? extends Exception> exceptionClass) {
/* 289 */     Preconditions.checkArgument(
/* 290 */         isCheckedException(exceptionClass), "Futures.getChecked exception type (%s) must not be a RuntimeException", exceptionClass);
/*     */ 
/*     */     
/* 293 */     Preconditions.checkArgument(
/* 294 */         hasConstructorUsableByGetChecked(exceptionClass), "Futures.getChecked exception type (%s) must be an accessible class with an accessible constructor whose parameters (if any) must be of type String and/or Throwable", exceptionClass);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static interface GetCheckedTypeValidator {
/*     */     void validateClass(Class<? extends Exception> param1Class);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\commo\\util\concurrent\FuturesGetChecked.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */