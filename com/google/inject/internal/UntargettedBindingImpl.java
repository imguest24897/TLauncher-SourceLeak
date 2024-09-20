/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.base.MoreObjects;
/*    */ import com.google.common.base.Objects;
/*    */ import com.google.inject.Binder;
/*    */ import com.google.inject.Key;
/*    */ import com.google.inject.binder.ScopedBindingBuilder;
/*    */ import com.google.inject.spi.BindingTargetVisitor;
/*    */ import com.google.inject.spi.Dependency;
/*    */ import com.google.inject.spi.Elements;
/*    */ import com.google.inject.spi.UntargettedBinding;
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
/*    */ final class UntargettedBindingImpl<T>
/*    */   extends BindingImpl<T>
/*    */   implements UntargettedBinding<T>
/*    */ {
/*    */   UntargettedBindingImpl(InjectorImpl injector, Key<T> key, Object source) {
/* 33 */     super(injector, key, source, new InternalFactory<T>()
/*    */         {
/*    */ 
/*    */ 
/*    */           
/*    */           public T get(InternalContext context, Dependency<?> dependency, boolean linked)
/*    */           {
/* 40 */             throw new AssertionError();
/*    */           }
/*    */         },  Scoping.UNSCOPED);
/*    */   }
/*    */ 
/*    */   
/*    */   public UntargettedBindingImpl(Object source, Key<T> key, Scoping scoping) {
/* 47 */     super(source, key, scoping);
/*    */   }
/*    */ 
/*    */   
/*    */   public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> visitor) {
/* 52 */     return (V)visitor.visit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public BindingImpl<T> withScoping(Scoping scoping) {
/* 57 */     return new UntargettedBindingImpl(getSource(), getKey(), scoping);
/*    */   }
/*    */ 
/*    */   
/*    */   public BindingImpl<T> withKey(Key<T> key) {
/* 62 */     return new UntargettedBindingImpl(getSource(), key, getScoping());
/*    */   }
/*    */ 
/*    */   
/*    */   public void applyTo(Binder binder) {
/* 67 */     getScoping().applyTo((ScopedBindingBuilder)Elements.withTrustedSource(GuiceInternal.GUICE_INTERNAL, binder, getSource()).bind(getKey()));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 72 */     return MoreObjects.toStringHelper(UntargettedBinding.class)
/* 73 */       .add("key", getKey())
/* 74 */       .add("source", getSource())
/* 75 */       .toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 80 */     if (obj instanceof UntargettedBindingImpl) {
/* 81 */       UntargettedBindingImpl<?> o = (UntargettedBindingImpl)obj;
/* 82 */       return (getKey().equals(o.getKey()) && getScoping().equals(o.getScoping()));
/*    */     } 
/* 84 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 90 */     return Objects.hashCode(new Object[] { getKey(), getScoping() });
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\UntargettedBindingImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */