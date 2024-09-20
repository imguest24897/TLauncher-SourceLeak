/*     */ package com.google.inject.util;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.inject.Inject;
/*     */ import com.google.inject.Injector;
/*     */ import com.google.inject.Provider;
/*     */ import com.google.inject.spi.Dependency;
/*     */ import com.google.inject.spi.InjectionPoint;
/*     */ import com.google.inject.spi.ProviderWithDependencies;
/*     */ import java.util.Set;
/*     */ import javax.inject.Provider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Providers
/*     */ {
/*     */   public static <T> Provider<T> of(T instance) {
/*  51 */     return new ConstantProvider<>(instance);
/*     */   }
/*     */   
/*     */   private static final class ConstantProvider<T> implements Provider<T> {
/*     */     private final T instance;
/*     */     
/*     */     private ConstantProvider(T instance) {
/*  58 */       this.instance = instance;
/*     */     }
/*     */ 
/*     */     
/*     */     public T get() {
/*  63 */       return this.instance;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  68 */       String str = String.valueOf(this.instance); return (new StringBuilder(4 + String.valueOf(str).length())).append("of(").append(str).append(")").toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/*  73 */       return (obj instanceof ConstantProvider && 
/*  74 */         Objects.equal(this.instance, ((ConstantProvider)obj).instance));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/*  79 */       return Objects.hashCode(new Object[] { this.instance });
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
/*     */   public static <T> Provider<T> guicify(Provider<T> provider) {
/*  91 */     if (provider instanceof Provider) {
/*  92 */       return (Provider<T>)provider;
/*     */     }
/*     */     
/*  95 */     Provider<T> delegate = (Provider<T>)Preconditions.checkNotNull(provider, "provider");
/*     */ 
/*     */ 
/*     */     
/*  99 */     Set<InjectionPoint> injectionPoints = InjectionPoint.forInstanceMethodsAndFields(provider.getClass());
/* 100 */     if (injectionPoints.isEmpty()) {
/* 101 */       return new GuicifiedProvider<>(delegate);
/*     */     }
/* 103 */     Set<Dependency<?>> mutableDeps = Sets.newHashSet();
/* 104 */     for (InjectionPoint ip : injectionPoints) {
/* 105 */       mutableDeps.addAll(ip.getDependencies());
/*     */     }
/* 107 */     ImmutableSet immutableSet = ImmutableSet.copyOf(mutableDeps);
/* 108 */     return new GuicifiedProviderWithDependencies<>((Set)immutableSet, delegate);
/*     */   }
/*     */   
/*     */   private static class GuicifiedProvider<T>
/*     */     implements Provider<T> {
/*     */     protected final Provider<T> delegate;
/*     */     
/*     */     private GuicifiedProvider(Provider<T> delegate) {
/* 116 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public T get() {
/* 121 */       return (T)this.delegate.get();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 126 */       String str = String.valueOf(this.delegate); return (new StringBuilder(11 + String.valueOf(str).length())).append("guicified(").append(str).append(")").toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 131 */       return (obj instanceof GuicifiedProvider && 
/* 132 */         Objects.equal(this.delegate, ((GuicifiedProvider)obj).delegate));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 137 */       return Objects.hashCode(new Object[] { this.delegate });
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class GuicifiedProviderWithDependencies<T>
/*     */     extends GuicifiedProvider<T>
/*     */     implements ProviderWithDependencies<T> {
/*     */     private final Set<Dependency<?>> dependencies;
/*     */     
/*     */     private GuicifiedProviderWithDependencies(Set<Dependency<?>> dependencies, Provider<T> delegate) {
/* 147 */       super(delegate);
/* 148 */       this.dependencies = dependencies;
/*     */     }
/*     */ 
/*     */     
/*     */     @Inject
/*     */     void initialize(Injector injector) {
/* 154 */       injector.injectMembers(this.delegate);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Dependency<?>> getDependencies() {
/* 159 */       return this.dependencies;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\injec\\util\Providers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */