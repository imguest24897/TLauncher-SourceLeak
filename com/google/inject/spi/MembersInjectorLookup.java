/*     */ package com.google.inject.spi;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.inject.Binder;
/*     */ import com.google.inject.ConfigurationException;
/*     */ import com.google.inject.MembersInjector;
/*     */ import com.google.inject.TypeLiteral;
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
/*     */ 
/*     */ public final class MembersInjectorLookup<T>
/*     */   implements Element
/*     */ {
/*     */   private final Object source;
/*     */   private final TypeLiteral<T> type;
/*     */   private MembersInjector<T> delegate;
/*     */   
/*     */   public MembersInjectorLookup(Object source, TypeLiteral<T> type) {
/*  47 */     this.source = Preconditions.checkNotNull(source, "source");
/*  48 */     this.type = (TypeLiteral<T>)Preconditions.checkNotNull(type, "type");
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getSource() {
/*  53 */     return this.source;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeLiteral<T> getType() {
/*  58 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T acceptVisitor(ElementVisitor<T> visitor) {
/*  63 */     return visitor.visit(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initializeDelegate(MembersInjector<T> delegate) {
/*  72 */     Preconditions.checkState((this.delegate == null), "delegate already initialized");
/*  73 */     this.delegate = (MembersInjector<T>)Preconditions.checkNotNull(delegate, "delegate");
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyTo(Binder binder) {
/*  78 */     initializeDelegate(binder.withSource(getSource()).getMembersInjector(this.type));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MembersInjector<T> getDelegate() {
/*  87 */     return this.delegate;
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
/*     */   public Set<InjectionPoint> getInjectionPoints() throws ConfigurationException {
/* 103 */     return InjectionPoint.forInstanceMethodsAndFields(this.type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MembersInjector<T> getMembersInjector() {
/* 112 */     return new MembersInjector<T>()
/*     */       {
/*     */         public void injectMembers(T instance) {
/* 115 */           MembersInjector<T> local = MembersInjectorLookup.this.delegate;
/* 116 */           if (local == null) {
/* 117 */             throw new IllegalStateException("This MembersInjector cannot be used until the Injector has been created.");
/*     */           }
/*     */           
/* 120 */           local.injectMembers(instance);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 125 */           String str = String.valueOf(MembersInjectorLookup.this.type); return (new StringBuilder(17 + String.valueOf(str).length())).append("MembersInjector<").append(str).append(">").toString();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 132 */     return (obj instanceof MembersInjectorLookup && ((MembersInjectorLookup)obj).type
/* 133 */       .equals(this.type) && ((MembersInjectorLookup)obj).source
/* 134 */       .equals(this.source));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 139 */     return Objects.hashCode(new Object[] { this.type, this.source });
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\MembersInjectorLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */