/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.inject.Key;
/*    */ import com.google.inject.spi.Dependency;
/*    */ import javax.inject.Provider;
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
/*    */ final class BoundProviderFactory<T>
/*    */   extends ProviderInternalFactory<T>
/*    */   implements CreationListener
/*    */ {
/*    */   private final ProvisionListenerStackCallback<T> provisionCallback;
/*    */   private final InjectorImpl injector;
/*    */   final Key<? extends Provider<? extends T>> providerKey;
/*    */   private InternalFactory<? extends Provider<? extends T>> providerFactory;
/*    */   
/*    */   BoundProviderFactory(InjectorImpl injector, Key<? extends Provider<? extends T>> providerKey, Object source, ProvisionListenerStackCallback<T> provisionCallback) {
/* 37 */     super(source);
/* 38 */     this.provisionCallback = provisionCallback;
/* 39 */     this.injector = injector;
/* 40 */     this.providerKey = providerKey;
/*    */   }
/*    */ 
/*    */   
/*    */   public void notify(Errors errors) {
/*    */     try {
/* 46 */       this
/* 47 */         .providerFactory = this.injector.getInternalFactory((Key)this.providerKey, errors
/* 48 */           .withSource(this.source), InjectorImpl.JitLimitation.NEW_OR_EXISTING_JIT);
/* 49 */     } catch (ErrorsException e) {
/* 50 */       errors.merge(e.getErrors());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public T get(InternalContext context, Dependency<?> dependency, boolean linked) throws InternalProvisionException {
/*    */     try {
/* 58 */       Provider<? extends T> provider = this.providerFactory.get(context, dependency, true);
/* 59 */       return circularGet(provider, context, dependency, this.provisionCallback);
/* 60 */     } catch (InternalProvisionException ipe) {
/* 61 */       throw ipe.addSource(this.providerKey);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected T provision(Provider<? extends T> provider, Dependency<?> dependency, ConstructionContext<T> constructionContext) throws InternalProvisionException {
/*    */     try {
/* 72 */       return super.provision(provider, dependency, constructionContext);
/* 73 */     } catch (RuntimeException userException) {
/* 74 */       throw InternalProvisionException.errorInProvider(userException);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 80 */     return this.providerKey.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\BoundProviderFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */