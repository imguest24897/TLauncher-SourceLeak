/*    */ package com.google.inject.spi;
/*    */ 
/*    */ import com.google.inject.Scope;
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
/*    */ public class DefaultBindingScopingVisitor<V>
/*    */   implements BindingScopingVisitor<V>
/*    */ {
/*    */   protected V visitOther() {
/* 35 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public V visitEagerSingleton() {
/* 40 */     return visitOther();
/*    */   }
/*    */ 
/*    */   
/*    */   public V visitScope(Scope scope) {
/* 45 */     return visitOther();
/*    */   }
/*    */ 
/*    */   
/*    */   public V visitScopeAnnotation(Class<? extends Annotation> scopeAnnotation) {
/* 50 */     return visitOther();
/*    */   }
/*    */ 
/*    */   
/*    */   public V visitNoScoping() {
/* 55 */     return visitOther();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\DefaultBindingScopingVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */