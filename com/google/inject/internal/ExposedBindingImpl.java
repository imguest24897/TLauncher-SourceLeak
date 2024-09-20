/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.base.MoreObjects;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.inject.Binder;
/*    */ import com.google.inject.Injector;
/*    */ import com.google.inject.Key;
/*    */ import com.google.inject.spi.BindingTargetVisitor;
/*    */ import com.google.inject.spi.Dependency;
/*    */ import com.google.inject.spi.ExposedBinding;
/*    */ import com.google.inject.spi.PrivateElements;
/*    */ import java.util.Set;
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
/*    */ final class ExposedBindingImpl<T>
/*    */   extends BindingImpl<T>
/*    */   implements ExposedBinding<T>
/*    */ {
/*    */   private final PrivateElements privateElements;
/*    */   
/*    */   ExposedBindingImpl(InjectorImpl injector, Object source, Key<T> key, InternalFactory<T> factory, PrivateElements privateElements) {
/* 40 */     super(injector, key, source, factory, Scoping.UNSCOPED);
/* 41 */     this.privateElements = privateElements;
/*    */   }
/*    */ 
/*    */   
/*    */   public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> visitor) {
/* 46 */     return (V)visitor.visit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<Dependency<?>> getDependencies() {
/* 51 */     return (Set<Dependency<?>>)ImmutableSet.of(Dependency.get(Key.get(Injector.class)));
/*    */   }
/*    */ 
/*    */   
/*    */   public PrivateElements getPrivateElements() {
/* 56 */     return this.privateElements;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 61 */     return MoreObjects.toStringHelper(ExposedBinding.class)
/* 62 */       .add("key", getKey())
/* 63 */       .add("source", getSource())
/* 64 */       .add("privateElements", this.privateElements)
/* 65 */       .toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public void applyTo(Binder binder) {
/* 70 */     throw new UnsupportedOperationException("This element represents a synthetic binding.");
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ExposedBindingImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */