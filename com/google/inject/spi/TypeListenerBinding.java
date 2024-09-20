/*    */ package com.google.inject.spi;
/*    */ 
/*    */ import com.google.inject.Binder;
/*    */ import com.google.inject.TypeLiteral;
/*    */ import com.google.inject.matcher.Matcher;
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
/*    */ public final class TypeListenerBinding
/*    */   implements Element
/*    */ {
/*    */   private final Object source;
/*    */   private final Matcher<? super TypeLiteral<?>> typeMatcher;
/*    */   private final TypeListener listener;
/*    */   
/*    */   TypeListenerBinding(Object source, TypeListener listener, Matcher<? super TypeLiteral<?>> typeMatcher) {
/* 41 */     this.source = source;
/* 42 */     this.listener = listener;
/* 43 */     this.typeMatcher = typeMatcher;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeListener getListener() {
/* 48 */     return this.listener;
/*    */   }
/*    */ 
/*    */   
/*    */   public Matcher<? super TypeLiteral<?>> getTypeMatcher() {
/* 53 */     return this.typeMatcher;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getSource() {
/* 58 */     return this.source;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T acceptVisitor(ElementVisitor<T> visitor) {
/* 63 */     return visitor.visit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void applyTo(Binder binder) {
/* 68 */     binder.withSource(getSource()).bindListener(this.typeMatcher, this.listener);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\TypeListenerBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */