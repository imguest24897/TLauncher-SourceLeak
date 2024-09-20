/*    */ package com.google.inject.spi;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.inject.Binder;
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
/*    */ public final class RequireAtInjectOnConstructorsOption
/*    */   implements Element
/*    */ {
/*    */   private final Object source;
/*    */   
/*    */   RequireAtInjectOnConstructorsOption(Object source) {
/* 34 */     this.source = Preconditions.checkNotNull(source, "source");
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getSource() {
/* 39 */     return this.source;
/*    */   }
/*    */ 
/*    */   
/*    */   public void applyTo(Binder binder) {
/* 44 */     binder.withSource(getSource()).requireAtInjectOnConstructors();
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T acceptVisitor(ElementVisitor<T> visitor) {
/* 49 */     return visitor.visit(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\RequireAtInjectOnConstructorsOption.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */