/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*     */ import java.util.Arrays;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class MoreObjects
/*     */ {
/*     */   public static <T> T firstNonNull(T first, T second) {
/*  61 */     if (first != null) {
/*  62 */       return first;
/*     */     }
/*  64 */     if (second != null) {
/*  65 */       return second;
/*     */     }
/*  67 */     throw new NullPointerException("Both parameters are null");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ToStringHelper toStringHelper(Object self) {
/* 111 */     return new ToStringHelper(self.getClass().getSimpleName());
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
/*     */   public static ToStringHelper toStringHelper(Class<?> clazz) {
/* 125 */     return new ToStringHelper(clazz.getSimpleName());
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
/*     */   public static ToStringHelper toStringHelper(String className) {
/* 137 */     return new ToStringHelper(className);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class ToStringHelper
/*     */   {
/*     */     @GuardedBy("ToStringHelper.class")
/*     */     private static boolean performedJava8CompatibilityCheck;
/*     */ 
/*     */     
/*     */     private final String className;
/*     */ 
/*     */     
/*     */     private static void java8CompatibilityCheck() {
/* 152 */       boolean racyReadForDoubleCheckedLock = performedJava8CompatibilityCheck;
/* 153 */       if (racyReadForDoubleCheckedLock) {
/*     */         return;
/*     */       }
/* 156 */       synchronized (ToStringHelper.class) {
/* 157 */         if (performedJava8CompatibilityCheck) {
/*     */           return;
/*     */         }
/* 160 */         performedJava8CompatibilityCheck = true;
/*     */       } 
/*     */       
/*     */       try {
/* 164 */         Java8Usage.performCheck();
/* 165 */       } catch (Throwable underlying) {
/* 166 */         Exception toLog = new Exception("Guava will drop support for Java 7 in 2021. Please let us know if this will cause you problems: https://github.com/google/guava/issues/5269", underlying);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 171 */         Logger.getLogger(ToStringHelper.class.getName())
/* 172 */           .log(Level.WARNING, "Java 7 compatibility warning: See https://github.com/google/guava/issues/5269", toLog);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 180 */     private final ValueHolder holderHead = new ValueHolder();
/* 181 */     private ValueHolder holderTail = this.holderHead;
/*     */     
/*     */     private boolean omitNullValues = false;
/*     */     
/*     */     private ToStringHelper(String className) {
/* 186 */       java8CompatibilityCheck();
/* 187 */       this.className = Preconditions.<String>checkNotNull(className);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper omitNullValues() {
/* 199 */       this.omitNullValues = true;
/* 200 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, Object value) {
/* 210 */       return addHolder(name, value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, boolean value) {
/* 220 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, char value) {
/* 230 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, double value) {
/* 240 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, float value) {
/* 250 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, int value) {
/* 260 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, long value) {
/* 270 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(Object value) {
/* 281 */       return addHolder(value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(boolean value) {
/* 294 */       return addHolder(String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(char value) {
/* 307 */       return addHolder(String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(double value) {
/* 320 */       return addHolder(String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(float value) {
/* 333 */       return addHolder(String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(int value) {
/* 346 */       return addHolder(String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(long value) {
/* 359 */       return addHolder(String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 373 */       boolean omitNullValuesSnapshot = this.omitNullValues;
/* 374 */       String nextSeparator = "";
/* 375 */       StringBuilder builder = (new StringBuilder(32)).append(this.className).append('{');
/* 376 */       ValueHolder valueHolder = this.holderHead.next;
/* 377 */       for (; valueHolder != null; 
/* 378 */         valueHolder = valueHolder.next) {
/* 379 */         Object value = valueHolder.value;
/* 380 */         if (!omitNullValuesSnapshot || value != null) {
/* 381 */           builder.append(nextSeparator);
/* 382 */           nextSeparator = ", ";
/*     */           
/* 384 */           if (valueHolder.name != null) {
/* 385 */             builder.append(valueHolder.name).append('=');
/*     */           }
/* 387 */           if (value != null && value.getClass().isArray()) {
/* 388 */             Object[] objectArray = { value };
/* 389 */             String arrayString = Arrays.deepToString(objectArray);
/* 390 */             builder.append(arrayString, 1, arrayString.length() - 1);
/*     */           } else {
/* 392 */             builder.append(value);
/*     */           } 
/*     */         } 
/*     */       } 
/* 396 */       return builder.append('}').toString();
/*     */     }
/*     */     
/*     */     private ValueHolder addHolder() {
/* 400 */       ValueHolder valueHolder = new ValueHolder();
/* 401 */       this.holderTail = this.holderTail.next = valueHolder;
/* 402 */       return valueHolder;
/*     */     }
/*     */     
/*     */     private ToStringHelper addHolder(Object value) {
/* 406 */       ValueHolder valueHolder = addHolder();
/* 407 */       valueHolder.value = value;
/* 408 */       return this;
/*     */     }
/*     */     
/*     */     private ToStringHelper addHolder(String name, Object value) {
/* 412 */       ValueHolder valueHolder = addHolder();
/* 413 */       valueHolder.value = value;
/* 414 */       valueHolder.name = Preconditions.<String>checkNotNull(name);
/* 415 */       return this;
/*     */     }
/*     */     
/*     */     private static final class ValueHolder {
/*     */       String name;
/*     */       Object value;
/*     */       ValueHolder next;
/*     */       
/*     */       private ValueHolder() {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\base\MoreObjects.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */