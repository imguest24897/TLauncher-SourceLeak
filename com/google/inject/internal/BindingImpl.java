/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.inject.Binding;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.Provider;
/*     */ import com.google.inject.spi.BindingScopingVisitor;
/*     */ import com.google.inject.spi.ElementVisitor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BindingImpl<T>
/*     */   implements Binding<T>
/*     */ {
/*     */   private final InjectorImpl injector;
/*     */   private final Key<T> key;
/*     */   private final Object source;
/*     */   private final Scoping scoping;
/*     */   private final InternalFactory<? extends T> internalFactory;
/*     */   private volatile Provider<T> provider;
/*     */   
/*     */   BindingImpl(InjectorImpl injector, Key<T> key, Object source, InternalFactory<? extends T> internalFactory, Scoping scoping) {
/*  42 */     this.injector = injector;
/*  43 */     this.key = key;
/*  44 */     this.source = source;
/*  45 */     this.internalFactory = internalFactory;
/*  46 */     this.scoping = scoping;
/*     */   }
/*     */   
/*     */   BindingImpl(Object source, Key<T> key, Scoping scoping) {
/*  50 */     this.internalFactory = null;
/*  51 */     this.injector = null;
/*  52 */     this.source = source;
/*  53 */     this.key = key;
/*  54 */     this.scoping = scoping;
/*     */   }
/*     */ 
/*     */   
/*     */   public Key<T> getKey() {
/*  59 */     return this.key;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getSource() {
/*  64 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Provider<T> getProvider() {
/*  71 */     if (this.provider == null) {
/*  72 */       if (this.injector == null) {
/*  73 */         throw new UnsupportedOperationException("getProvider() not supported for module bindings");
/*     */       }
/*     */       
/*  76 */       this.provider = this.injector.getProvider(this.key);
/*     */     } 
/*  78 */     return this.provider;
/*     */   }
/*     */   
/*     */   public InternalFactory<? extends T> getInternalFactory() {
/*  82 */     return this.internalFactory;
/*     */   }
/*     */   
/*     */   public Scoping getScoping() {
/*  86 */     return this.scoping;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstant() {
/*  94 */     return this instanceof com.google.inject.spi.InstanceBinding;
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> V acceptVisitor(ElementVisitor<V> visitor) {
/*  99 */     return (V)visitor.visit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> V acceptScopingVisitor(BindingScopingVisitor<V> visitor) {
/* 104 */     return this.scoping.acceptVisitor(visitor);
/*     */   }
/*     */   
/*     */   protected BindingImpl<T> withScoping(Scoping scoping) {
/* 108 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */   protected BindingImpl<T> withKey(Key<T> key) {
/* 112 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 117 */     return MoreObjects.toStringHelper(Binding.class)
/* 118 */       .add("key", this.key)
/* 119 */       .add("scope", this.scoping)
/* 120 */       .add("source", this.source)
/* 121 */       .toString();
/*     */   }
/*     */   
/*     */   public InjectorImpl getInjector() {
/* 125 */     return this.injector;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\BindingImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */