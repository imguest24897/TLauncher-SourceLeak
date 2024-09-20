/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.inject.Scope;
/*    */ import com.google.inject.spi.ScopeBinding;
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
/*    */ final class ScopeBindingProcessor
/*    */   extends AbstractProcessor
/*    */ {
/*    */   ScopeBindingProcessor(Errors errors) {
/* 34 */     super(errors);
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean visit(ScopeBinding command) {
/* 39 */     Scope scope = (Scope)Preconditions.checkNotNull(command.getScope(), "scope");
/*    */     
/* 41 */     Class<? extends Annotation> annotationType = (Class<? extends Annotation>)Preconditions.checkNotNull(command.getAnnotationType(), "annotation type");
/*    */     
/* 43 */     if (!Annotations.isScopeAnnotation(annotationType)) {
/* 44 */       this.errors.missingScopeAnnotation(annotationType);
/*    */     }
/*    */ 
/*    */     
/* 48 */     if (!Annotations.isRetainedAtRuntime(annotationType)) {
/* 49 */       this.errors.missingRuntimeRetention(annotationType);
/*    */     }
/*    */ 
/*    */     
/* 53 */     ScopeBinding existing = this.injector.getBindingData().getScopeBinding(annotationType);
/* 54 */     if (existing != null) {
/* 55 */       if (!scope.equals(existing.getScope())) {
/* 56 */         this.errors.duplicateScopes(existing, annotationType, scope);
/*    */       }
/*    */     } else {
/* 59 */       this.injector.getBindingData().putScopeBinding(annotationType, command);
/*    */     } 
/*    */     
/* 62 */     return Boolean.valueOf(true);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ScopeBindingProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */