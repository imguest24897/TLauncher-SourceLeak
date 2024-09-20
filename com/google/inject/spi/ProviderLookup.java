/*     */ package com.google.inject.spi;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.inject.Binder;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.Provider;
/*     */ import com.google.inject.internal.Errors;
/*     */ import com.google.inject.util.Types;
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
/*     */ 
/*     */ public final class ProviderLookup<T>
/*     */   implements Element
/*     */ {
/*     */   private final Object source;
/*     */   private final Dependency<T> dependency;
/*     */   private Provider<T> delegate;
/*     */   
/*     */   public ProviderLookup(Object source, Key<T> key) {
/*  49 */     this(source, Dependency.get((Key<T>)Preconditions.checkNotNull(key, "key")));
/*     */   }
/*     */ 
/*     */   
/*     */   public ProviderLookup(Object source, Dependency<T> dependency) {
/*  54 */     this.source = Preconditions.checkNotNull(source, "source");
/*  55 */     this.dependency = (Dependency<T>)Preconditions.checkNotNull(dependency, "dependency");
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getSource() {
/*  60 */     return this.source;
/*     */   }
/*     */   
/*     */   public Key<T> getKey() {
/*  64 */     return this.dependency.getKey();
/*     */   }
/*     */ 
/*     */   
/*     */   public Dependency<T> getDependency() {
/*  69 */     return this.dependency;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T acceptVisitor(ElementVisitor<T> visitor) {
/*  74 */     return visitor.visit(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initializeDelegate(Provider<T> delegate) {
/*  83 */     Preconditions.checkState((this.delegate == null), "delegate already initialized");
/*  84 */     this.delegate = (Provider<T>)Preconditions.checkNotNull(delegate, "delegate");
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyTo(Binder binder) {
/*  89 */     initializeDelegate(binder.withSource(getSource()).getProvider(this.dependency));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Provider<T> getDelegate() {
/*  97 */     return this.delegate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Provider<T> getProvider() {
/* 106 */     return new ProviderWithDependencies<T>()
/*     */       {
/*     */         public T get() {
/* 109 */           Provider<T> local = ProviderLookup.this.delegate;
/* 110 */           if (local == null) {
/* 111 */             throw new IllegalStateException("This Provider cannot be used until the Injector has been created.");
/*     */           }
/*     */           
/* 114 */           return (T)local.get();
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public Set<Dependency<?>> getDependencies() {
/* 121 */           Key<?> providerKey = ProviderLookup.this.getKey().ofType(Types.providerOf(ProviderLookup.this.getKey().getTypeLiteral().getType()));
/* 122 */           return (Set<Dependency<?>>)ImmutableSet.of(Dependency.get(providerKey));
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 127 */           String str = String.valueOf(ProviderLookup.this.getKey().getTypeLiteral()); return (new StringBuilder(10 + String.valueOf(str).length())).append("Provider<").append(str).append(">").toString();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 134 */     return MoreObjects.toStringHelper(ProviderLookup.class)
/* 135 */       .add("dependency", this.dependency)
/* 136 */       .add("source", Errors.convert(this.source))
/* 137 */       .toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 142 */     return (obj instanceof ProviderLookup && ((ProviderLookup)obj).dependency
/* 143 */       .equals(this.dependency) && ((ProviderLookup)obj).source
/* 144 */       .equals(this.source));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 149 */     return Objects.hashCode(new Object[] { this.dependency, this.source });
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\ProviderLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */