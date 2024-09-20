/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.inject.Binder;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.spi.BindingTargetVisitor;
/*     */ import com.google.inject.spi.Dependency;
/*     */ import com.google.inject.spi.Elements;
/*     */ import com.google.inject.spi.HasDependencies;
/*     */ import com.google.inject.spi.ProviderKeyBinding;
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
/*     */ final class LinkedProviderBindingImpl<T>
/*     */   extends BindingImpl<T>
/*     */   implements ProviderKeyBinding<T>, HasDependencies, DelayedInitialize
/*     */ {
/*     */   final Key<? extends Provider<? extends T>> providerKey;
/*     */   final DelayedInitialize delayedInitializer;
/*     */   
/*     */   private LinkedProviderBindingImpl(InjectorImpl injector, Key<T> key, Object source, InternalFactory<? extends T> internalFactory, Scoping scoping, Key<? extends Provider<? extends T>> providerKey, DelayedInitialize delayedInitializer) {
/*  47 */     super(injector, key, source, internalFactory, scoping);
/*  48 */     this.providerKey = providerKey;
/*  49 */     this.delayedInitializer = delayedInitializer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkedProviderBindingImpl(InjectorImpl injector, Key<T> key, Object source, InternalFactory<? extends T> internalFactory, Scoping scoping, Key<? extends Provider<? extends T>> providerKey) {
/*  59 */     this(injector, key, source, internalFactory, scoping, providerKey, (DelayedInitialize)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   LinkedProviderBindingImpl(Object source, Key<T> key, Scoping scoping, Key<? extends Provider<? extends T>> providerKey) {
/*  67 */     super(source, key, scoping);
/*  68 */     this.providerKey = providerKey;
/*  69 */     this.delayedInitializer = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> LinkedProviderBindingImpl<T> createWithInitializer(InjectorImpl injector, Key<T> key, Object source, InternalFactory<? extends T> internalFactory, Scoping scoping, Key<? extends Provider<? extends T>> providerKey, DelayedInitialize delayedInitializer) {
/*  80 */     return new LinkedProviderBindingImpl<>(injector, key, source, internalFactory, scoping, providerKey, delayedInitializer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> visitor) {
/*  86 */     return (V)visitor.visit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Key<? extends Provider<? extends T>> getProviderKey() {
/*  91 */     return this.providerKey;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initialize(InjectorImpl injector, Errors errors) throws ErrorsException {
/*  96 */     if (this.delayedInitializer != null) {
/*  97 */       this.delayedInitializer.initialize(injector, errors);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Dependency<?>> getDependencies() {
/* 103 */     return (Set<Dependency<?>>)ImmutableSet.of(Dependency.get(this.providerKey));
/*     */   }
/*     */ 
/*     */   
/*     */   public BindingImpl<T> withScoping(Scoping scoping) {
/* 108 */     return new LinkedProviderBindingImpl(getSource(), getKey(), scoping, this.providerKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public BindingImpl<T> withKey(Key<T> key) {
/* 113 */     return new LinkedProviderBindingImpl(getSource(), key, getScoping(), this.providerKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyTo(Binder binder) {
/* 118 */     getScoping()
/* 119 */       .applyTo(
/* 120 */         Elements.withTrustedSource(GuiceInternal.GUICE_INTERNAL, binder, getSource())
/* 121 */         .bind(getKey())
/* 122 */         .toProvider(getProviderKey()));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 127 */     return MoreObjects.toStringHelper(ProviderKeyBinding.class)
/* 128 */       .add("key", getKey())
/* 129 */       .add("source", getSource())
/* 130 */       .add("scope", getScoping())
/* 131 */       .add("provider", this.providerKey)
/* 132 */       .toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 137 */     if (obj instanceof LinkedProviderBindingImpl) {
/* 138 */       LinkedProviderBindingImpl<?> o = (LinkedProviderBindingImpl)obj;
/* 139 */       return (getKey().equals(o.getKey()) && 
/* 140 */         getScoping().equals(o.getScoping()) && 
/* 141 */         Objects.equal(this.providerKey, o.providerKey));
/*     */     } 
/* 143 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 149 */     return Objects.hashCode(new Object[] { getKey(), getScoping(), this.providerKey });
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\LinkedProviderBindingImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */