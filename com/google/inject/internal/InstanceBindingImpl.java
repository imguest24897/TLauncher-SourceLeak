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
/*     */ import com.google.inject.spi.InjectionPoint;
/*     */ import com.google.inject.spi.InstanceBinding;
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
/*     */ final class InstanceBindingImpl<T>
/*     */   extends BindingImpl<T>
/*     */   implements InstanceBinding<T>
/*     */ {
/*     */   final T instance;
/*     */   final ImmutableSet<InjectionPoint> injectionPoints;
/*     */   
/*     */   public InstanceBindingImpl(InjectorImpl injector, Key<T> key, Object source, InternalFactory<? extends T> internalFactory, Set<InjectionPoint> injectionPoints, T instance) {
/*  46 */     super(injector, key, source, internalFactory, Scoping.EAGER_SINGLETON);
/*  47 */     this.injectionPoints = ImmutableSet.copyOf(injectionPoints);
/*  48 */     this.instance = instance;
/*     */   }
/*     */ 
/*     */   
/*     */   public InstanceBindingImpl(Object source, Key<T> key, Scoping scoping, Set<InjectionPoint> injectionPoints, T instance) {
/*  53 */     super(source, key, scoping);
/*  54 */     this.injectionPoints = ImmutableSet.copyOf(injectionPoints);
/*  55 */     this.instance = instance;
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> visitor) {
/*  60 */     return (V)visitor.visit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public T getInstance() {
/*  65 */     return this.instance;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<InjectionPoint> getInjectionPoints() {
/*  70 */     return (Set<InjectionPoint>)this.injectionPoints;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Dependency<?>> getDependencies() {
/*  75 */     return (this.instance instanceof HasDependencies) ? 
/*  76 */       (Set<Dependency<?>>)ImmutableSet.copyOf(((HasDependencies)this.instance).getDependencies()) : 
/*  77 */       Dependency.forInjectionPoints((Set)this.injectionPoints);
/*     */   }
/*     */ 
/*     */   
/*     */   public BindingImpl<T> withScoping(Scoping scoping) {
/*  82 */     return new InstanceBindingImpl(getSource(), getKey(), scoping, (Set<InjectionPoint>)this.injectionPoints, this.instance);
/*     */   }
/*     */ 
/*     */   
/*     */   public BindingImpl<T> withKey(Key<T> key) {
/*  87 */     return new InstanceBindingImpl(getSource(), key, getScoping(), (Set<InjectionPoint>)this.injectionPoints, this.instance);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyTo(Binder binder) {
/*  93 */     Elements.withTrustedSource(GuiceInternal.GUICE_INTERNAL, binder, getSource()).bind(getKey()).toInstance(this.instance);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  98 */     return MoreObjects.toStringHelper(InstanceBinding.class)
/*  99 */       .add("key", getKey())
/* 100 */       .add("source", getSource())
/* 101 */       .add("instance", this.instance)
/* 102 */       .toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 107 */     if (obj instanceof InstanceBindingImpl) {
/* 108 */       InstanceBindingImpl<?> o = (InstanceBindingImpl)obj;
/* 109 */       return (getKey().equals(o.getKey()) && 
/* 110 */         getScoping().equals(o.getScoping()) && 
/* 111 */         Objects.equal(this.instance, o.instance));
/*     */     } 
/* 113 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 119 */     return Objects.hashCode(new Object[] { getKey(), getScoping() });
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\InstanceBindingImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */