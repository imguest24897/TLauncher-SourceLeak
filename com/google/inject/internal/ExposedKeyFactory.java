/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.inject.Key;
/*    */ import com.google.inject.spi.Dependency;
/*    */ import com.google.inject.spi.PrivateElements;
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
/*    */ final class ExposedKeyFactory<T>
/*    */   implements InternalFactory<T>, CreationListener
/*    */ {
/*    */   private final Key<T> key;
/*    */   private final PrivateElements privateElements;
/*    */   private BindingImpl<T> delegate;
/*    */   
/*    */   ExposedKeyFactory(Key<T> key, PrivateElements privateElements) {
/* 33 */     this.key = key;
/* 34 */     this.privateElements = privateElements;
/*    */   }
/*    */ 
/*    */   
/*    */   public void notify(Errors errors) {
/* 39 */     InjectorImpl privateInjector = (InjectorImpl)this.privateElements.getInjector();
/* 40 */     BindingImpl<T> explicitBinding = privateInjector.getBindingData().getExplicitBinding(this.key);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 45 */     if (explicitBinding.getInternalFactory() == this) {
/* 46 */       errors.withSource(explicitBinding.getSource()).exposedButNotBound(this.key);
/*    */       
/*    */       return;
/*    */     } 
/* 50 */     this.delegate = explicitBinding;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public T get(InternalContext context, Dependency<?> dependency, boolean linked) throws InternalProvisionException {
/* 57 */     return this.delegate.getInternalFactory().get(context, dependency, linked);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ExposedKeyFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */