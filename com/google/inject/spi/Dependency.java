/*     */ package com.google.inject.spi;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.internal.MoreTypes;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Dependency<T>
/*     */ {
/*     */   private final InjectionPoint injectionPoint;
/*     */   private final Key<T> key;
/*     */   private final boolean nullable;
/*     */   private final int parameterIndex;
/*     */   
/*     */   Dependency(InjectionPoint injectionPoint, Key<T> key, boolean nullable, int parameterIndex) {
/*  46 */     this.injectionPoint = injectionPoint;
/*  47 */     this.key = (Key<T>)Preconditions.checkNotNull(key, "key");
/*  48 */     this.nullable = nullable;
/*  49 */     this.parameterIndex = parameterIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Dependency<T> get(Key<T> key) {
/*  57 */     return new Dependency<>(null, MoreTypes.canonicalizeKey(key), true, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Set<Dependency<?>> forInjectionPoints(Set<InjectionPoint> injectionPoints) {
/*  62 */     List<Dependency<?>> dependencies = Lists.newArrayList();
/*  63 */     for (InjectionPoint injectionPoint : injectionPoints) {
/*  64 */       dependencies.addAll(injectionPoint.getDependencies());
/*     */     }
/*  66 */     return (Set<Dependency<?>>)ImmutableSet.copyOf(dependencies);
/*     */   }
/*     */ 
/*     */   
/*     */   public Key<T> getKey() {
/*  71 */     return this.key;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isNullable() {
/*  76 */     return this.nullable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InjectionPoint getInjectionPoint() {
/*  84 */     return this.injectionPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getParameterIndex() {
/*  93 */     return this.parameterIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  98 */     return Objects.hashCode(new Object[] { this.injectionPoint, Integer.valueOf(this.parameterIndex), this.key });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 103 */     if (o instanceof Dependency) {
/* 104 */       Dependency<?> dependency = (Dependency)o;
/* 105 */       return (Objects.equal(this.injectionPoint, dependency.injectionPoint) && this.parameterIndex == dependency.parameterIndex && this.key
/*     */         
/* 107 */         .equals(dependency.key));
/*     */     } 
/* 109 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 115 */     StringBuilder builder = new StringBuilder();
/* 116 */     builder.append(this.key);
/* 117 */     if (this.injectionPoint != null) {
/* 118 */       builder.append("@").append(this.injectionPoint);
/* 119 */       if (this.parameterIndex != -1) {
/* 120 */         builder.append("[").append(this.parameterIndex).append("]");
/*     */       }
/*     */     } 
/* 123 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\Dependency.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */