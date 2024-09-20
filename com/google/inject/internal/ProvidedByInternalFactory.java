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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ProvidedByInternalFactory<T>
/*    */   extends ProviderInternalFactory<T>
/*    */   implements DelayedInitialize
/*    */ {
/*    */   private final Class<?> rawType;
/*    */   private final Class<? extends Provider<?>> providerType;
/*    */   private final Key<? extends Provider<T>> providerKey;
/*    */   private BindingImpl<? extends Provider<T>> providerBinding;
/*    */   private ProvisionListenerStackCallback<T> provisionCallback;
/*    */   
/*    */   ProvidedByInternalFactory(Class<?> rawType, Class<? extends Provider<?>> providerType, Key<? extends Provider<T>> providerKey) {
/* 42 */     super(providerKey);
/* 43 */     this.rawType = rawType;
/* 44 */     this.providerType = providerType;
/* 45 */     this.providerKey = providerKey;
/*    */   }
/*    */   
/*    */   void setProvisionListenerCallback(ProvisionListenerStackCallback<T> listener) {
/* 49 */     this.provisionCallback = listener;
/*    */   }
/*    */ 
/*    */   
/*    */   public void initialize(InjectorImpl injector, Errors errors) throws ErrorsException {
/* 54 */     this
/* 55 */       .providerBinding = injector.getBindingOrThrow(this.providerKey, errors, InjectorImpl.JitLimitation.NEW_OR_EXISTING_JIT);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public T get(InternalContext context, Dependency<?> dependency, boolean linked) throws InternalProvisionException {
/* 61 */     BindingImpl<? extends Provider<T>> localProviderBinding = this.providerBinding;
/* 62 */     if (localProviderBinding == null) {
/* 63 */       throw new IllegalStateException("not initialized");
/*    */     }
/* 65 */     Key<? extends Provider<T>> localProviderKey = this.providerKey;
/*    */     
/*    */     try {
/* 68 */       Provider<? extends T> provider = localProviderBinding.getInternalFactory().get(context, dependency, true);
/* 69 */       return circularGet(provider, context, dependency, this.provisionCallback);
/* 70 */     } catch (InternalProvisionException ipe) {
/* 71 */       throw ipe.addSource(localProviderKey);
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
/* 82 */       Object o = super.provision(provider, dependency, constructionContext);
/* 83 */       if (o != null && !this.rawType.isInstance(o)) {
/* 84 */         throw InternalProvisionException.subtypeNotProvided(this.providerType, this.rawType);
/*    */       }
/*    */       
/* 87 */       T t = (T)o;
/* 88 */       return t;
/* 89 */     } catch (RuntimeException e) {
/* 90 */       throw InternalProvisionException.errorInProvider(e).addSource(this.source);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ProvidedByInternalFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */