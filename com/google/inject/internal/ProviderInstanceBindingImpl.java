/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.inject.Binder;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.Provider;
/*     */ import com.google.inject.spi.BindingTargetVisitor;
/*     */ import com.google.inject.spi.Dependency;
/*     */ import com.google.inject.spi.Elements;
/*     */ import com.google.inject.spi.HasDependencies;
/*     */ import com.google.inject.spi.InjectionPoint;
/*     */ import com.google.inject.spi.ProviderInstanceBinding;
/*     */ import com.google.inject.spi.ProviderWithExtensionVisitor;
/*     */ import com.google.inject.util.Providers;
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
/*     */ class ProviderInstanceBindingImpl<T>
/*     */   extends BindingImpl<T>
/*     */   implements ProviderInstanceBinding<T>
/*     */ {
/*     */   final Provider<? extends T> providerInstance;
/*     */   final ImmutableSet<InjectionPoint> injectionPoints;
/*     */   
/*     */   public ProviderInstanceBindingImpl(InjectorImpl injector, Key<T> key, Object source, InternalFactory<? extends T> internalFactory, Scoping scoping, Provider<? extends T> providerInstance, Set<InjectionPoint> injectionPoints) {
/*  50 */     super(injector, key, source, internalFactory, scoping);
/*  51 */     this.providerInstance = providerInstance;
/*  52 */     this.injectionPoints = ImmutableSet.copyOf(injectionPoints);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProviderInstanceBindingImpl(Object source, Key<T> key, Scoping scoping, Set<InjectionPoint> injectionPoints, Provider<? extends T> providerInstance) {
/*  61 */     super(source, key, scoping);
/*  62 */     this.injectionPoints = ImmutableSet.copyOf(injectionPoints);
/*  63 */     this.providerInstance = providerInstance;
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> visitor) {
/*  68 */     if (this.providerInstance instanceof ProviderWithExtensionVisitor) {
/*  69 */       return (V)((ProviderWithExtensionVisitor)this.providerInstance)
/*  70 */         .acceptExtensionVisitor(visitor, this);
/*     */     }
/*  72 */     return (V)visitor.visit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Provider<? extends T> getProviderInstance() {
/*  77 */     return Providers.guicify(this.providerInstance);
/*     */   }
/*     */ 
/*     */   
/*     */   public Provider<? extends T> getUserSuppliedProvider() {
/*  82 */     return this.providerInstance;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<InjectionPoint> getInjectionPoints() {
/*  87 */     return (Set<InjectionPoint>)this.injectionPoints;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Dependency<?>> getDependencies() {
/*  92 */     return (this.providerInstance instanceof HasDependencies) ? 
/*  93 */       (Set<Dependency<?>>)ImmutableSet.copyOf(((HasDependencies)this.providerInstance).getDependencies()) : 
/*  94 */       Dependency.forInjectionPoints((Set)this.injectionPoints);
/*     */   }
/*     */ 
/*     */   
/*     */   public BindingImpl<T> withScoping(Scoping scoping) {
/*  99 */     return new ProviderInstanceBindingImpl(
/* 100 */         getSource(), getKey(), scoping, (Set<InjectionPoint>)this.injectionPoints, this.providerInstance);
/*     */   }
/*     */ 
/*     */   
/*     */   public BindingImpl<T> withKey(Key<T> key) {
/* 105 */     return new ProviderInstanceBindingImpl(
/* 106 */         getSource(), key, getScoping(), (Set<InjectionPoint>)this.injectionPoints, this.providerInstance);
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyTo(Binder binder) {
/* 111 */     getScoping()
/* 112 */       .applyTo(
/* 113 */         Elements.withTrustedSource(GuiceInternal.GUICE_INTERNAL, binder, getSource())
/* 114 */         .bind(getKey())
/* 115 */         .toProvider(getUserSuppliedProvider()));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 120 */     return MoreObjects.toStringHelper(ProviderInstanceBinding.class)
/* 121 */       .add("key", getKey())
/* 122 */       .add("source", getSource())
/* 123 */       .add("scope", getScoping())
/* 124 */       .add("provider", this.providerInstance)
/* 125 */       .toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 130 */     if (obj instanceof ProviderInstanceBindingImpl) {
/* 131 */       ProviderInstanceBindingImpl<?> o = (ProviderInstanceBindingImpl)obj;
/* 132 */       return (getKey().equals(o.getKey()) && 
/* 133 */         getScoping().equals(o.getScoping()) && 
/* 134 */         Objects.equal(this.providerInstance, o.providerInstance));
/*     */     } 
/* 136 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 142 */     return Objects.hashCode(new Object[] { getKey(), getScoping() });
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ProviderInstanceBindingImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */