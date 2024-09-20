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
/*     */ import com.google.inject.spi.LinkedKeyBinding;
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
/*     */ final class LinkedBindingImpl<T>
/*     */   extends BindingImpl<T>
/*     */   implements LinkedKeyBinding<T>, HasDependencies
/*     */ {
/*     */   final Key<? extends T> targetKey;
/*     */   
/*     */   LinkedBindingImpl(InjectorImpl injector, Key<T> key, Object source, InternalFactory<? extends T> internalFactory, Scoping scoping, Key<? extends T> targetKey) {
/*  45 */     super(injector, key, source, internalFactory, scoping);
/*  46 */     this.targetKey = targetKey;
/*     */   }
/*     */   
/*     */   LinkedBindingImpl(Object source, Key<T> key, Scoping scoping, Key<? extends T> targetKey) {
/*  50 */     super(source, key, scoping);
/*  51 */     this.targetKey = targetKey;
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> visitor) {
/*  56 */     return (V)visitor.visit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Key<? extends T> getLinkedKey() {
/*  61 */     return this.targetKey;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Dependency<?>> getDependencies() {
/*  66 */     return (Set<Dependency<?>>)ImmutableSet.of(Dependency.get(this.targetKey));
/*     */   }
/*     */ 
/*     */   
/*     */   public BindingImpl<T> withScoping(Scoping scoping) {
/*  71 */     return new LinkedBindingImpl(getSource(), getKey(), scoping, this.targetKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public BindingImpl<T> withKey(Key<T> key) {
/*  76 */     return new LinkedBindingImpl(getSource(), key, getScoping(), this.targetKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyTo(Binder binder) {
/*  81 */     getScoping()
/*  82 */       .applyTo(
/*  83 */         Elements.withTrustedSource(GuiceInternal.GUICE_INTERNAL, binder, getSource())
/*  84 */         .bind(getKey())
/*  85 */         .to(getLinkedKey()));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  90 */     return MoreObjects.toStringHelper(LinkedKeyBinding.class)
/*  91 */       .add("key", getKey())
/*  92 */       .add("source", getSource())
/*  93 */       .add("scope", getScoping())
/*  94 */       .add("target", this.targetKey)
/*  95 */       .toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 100 */     if (obj instanceof LinkedBindingImpl) {
/* 101 */       LinkedBindingImpl<?> o = (LinkedBindingImpl)obj;
/* 102 */       return (getKey().equals(o.getKey()) && 
/* 103 */         getScoping().equals(o.getScoping()) && 
/* 104 */         Objects.equal(this.targetKey, o.targetKey));
/*     */     } 
/* 106 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 112 */     return Objects.hashCode(new Object[] { getKey(), getScoping(), this.targetKey });
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\LinkedBindingImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */