/*    */ package com.google.inject.spi;
/*    */ 
/*    */ import com.google.common.base.MoreObjects;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.inject.Binder;
/*    */ import com.google.inject.Scope;
/*    */ import com.google.inject.internal.Errors;
/*    */ import java.lang.annotation.Annotation;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ScopeBinding
/*    */   implements Element
/*    */ {
/*    */   private final Object source;
/*    */   private final Class<? extends Annotation> annotationType;
/*    */   private final Scope scope;
/*    */   
/*    */   ScopeBinding(Object source, Class<? extends Annotation> annotationType, Scope scope) {
/* 45 */     this.source = Preconditions.checkNotNull(source, "source");
/* 46 */     this.annotationType = (Class<? extends Annotation>)Preconditions.checkNotNull(annotationType, "annotationType");
/* 47 */     this.scope = (Scope)Preconditions.checkNotNull(scope, "scope");
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getSource() {
/* 52 */     return this.source;
/*    */   }
/*    */   
/*    */   public Class<? extends Annotation> getAnnotationType() {
/* 56 */     return this.annotationType;
/*    */   }
/*    */   
/*    */   public Scope getScope() {
/* 60 */     return this.scope;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T acceptVisitor(ElementVisitor<T> visitor) {
/* 65 */     return visitor.visit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void applyTo(Binder binder) {
/* 70 */     binder.withSource(getSource()).bindScope(this.annotationType, this.scope);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 75 */     return MoreObjects.toStringHelper(ScopeBinding.class)
/* 76 */       .add("annotationType", this.annotationType)
/* 77 */       .add("scope", this.scope)
/* 78 */       .add("source", Errors.convert(this.source))
/* 79 */       .toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\ScopeBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */