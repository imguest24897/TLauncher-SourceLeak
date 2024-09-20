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
/*    */ public final class RequireExplicitBindingsOption
/*    */   implements Element
/*    */ {
/*    */   private final Object source;
/*    */   
/*    */   RequireExplicitBindingsOption(Object source) {
/* 33 */     this.source = Preconditions.checkNotNull(source, "source");
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getSource() {
/* 38 */     return this.source;
/*    */   }
/*    */ 
/*    */   
/*    */   public void applyTo(Binder binder) {
/* 43 */     binder.withSource(getSource()).requireExplicitBindings();
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T acceptVisitor(ElementVisitor<T> visitor) {
/* 48 */     return visitor.visit(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\RequireExplicitBindingsOption.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */