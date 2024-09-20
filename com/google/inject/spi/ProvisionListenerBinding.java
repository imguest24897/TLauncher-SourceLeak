/*    */ package com.google.inject.spi;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.inject.Binder;
/*    */ import com.google.inject.Binding;
/*    */ import com.google.inject.matcher.Matcher;
/*    */ import java.util.List;
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
/*    */ public final class ProvisionListenerBinding
/*    */   implements Element
/*    */ {
/*    */   private final Object source;
/*    */   private final Matcher<? super Binding<?>> bindingMatcher;
/*    */   private final List<ProvisionListener> listeners;
/*    */   
/*    */   ProvisionListenerBinding(Object source, Matcher<? super Binding<?>> bindingMatcher, ProvisionListener[] listeners) {
/* 40 */     this.source = source;
/* 41 */     this.bindingMatcher = bindingMatcher;
/* 42 */     this.listeners = (List<ProvisionListener>)ImmutableList.copyOf((Object[])listeners);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<ProvisionListener> getListeners() {
/* 47 */     return this.listeners;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Matcher<? super Binding<?>> getBindingMatcher() {
/* 54 */     return this.bindingMatcher;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getSource() {
/* 59 */     return this.source;
/*    */   }
/*    */ 
/*    */   
/*    */   public <R> R acceptVisitor(ElementVisitor<R> visitor) {
/* 64 */     return visitor.visit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void applyTo(Binder binder) {
/* 69 */     binder
/* 70 */       .withSource(getSource())
/* 71 */       .bindListener(this.bindingMatcher, this.listeners.<ProvisionListener>toArray(new ProvisionListener[this.listeners.size()]));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\ProvisionListenerBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */