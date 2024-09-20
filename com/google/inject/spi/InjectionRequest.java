/*     */ package com.google.inject.spi;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.inject.Binder;
/*     */ import com.google.inject.ConfigurationException;
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
/*     */ public final class InjectionRequest<T>
/*     */   implements Element
/*     */ {
/*     */   private final Object source;
/*     */   private final TypeLiteral<T> type;
/*     */   private final T instance;
/*     */   
/*     */   public InjectionRequest(Object source, TypeLiteral<T> type, T instance) {
/*  45 */     this.source = Preconditions.checkNotNull(source, "source");
/*  46 */     this.type = (TypeLiteral<T>)Preconditions.checkNotNull(type, "type");
/*  47 */     this.instance = instance;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getSource() {
/*  52 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T getInstance() {
/*  60 */     return this.instance;
/*     */   }
/*     */   
/*     */   public TypeLiteral<T> getType() {
/*  64 */     return this.type;
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
/*  80 */     return InjectionPoint.forInstanceMethodsAndFields(
/*  81 */         (this.instance != null) ? TypeLiteral.get(this.instance.getClass()) : this.type);
/*     */   }
/*     */ 
/*     */   
/*     */   public <R> R acceptVisitor(ElementVisitor<R> visitor) {
/*  86 */     return visitor.visit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyTo(Binder binder) {
/*  91 */     binder.withSource(getSource()).requestInjection(this.type, this.instance);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  96 */     return (obj instanceof InjectionRequest && 
/*  97 */       Objects.equal(((InjectionRequest)obj).instance, this.instance) && ((InjectionRequest)obj).type
/*  98 */       .equals(this.type) && ((InjectionRequest)obj).source
/*  99 */       .equals(this.source));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 104 */     return Objects.hashCode(new Object[] { this.type, this.source });
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\InjectionRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */