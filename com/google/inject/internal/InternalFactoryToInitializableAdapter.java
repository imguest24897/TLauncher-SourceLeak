/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
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
/*    */ 
/*    */ final class InternalFactoryToInitializableAdapter<T>
/*    */   extends ProviderInternalFactory<T>
/*    */ {
/*    */   private final ProvisionListenerStackCallback<T> provisionCallback;
/*    */   private final Initializable<? extends Provider<? extends T>> initializable;
/*    */   
/*    */   public InternalFactoryToInitializableAdapter(Initializable<? extends Provider<? extends T>> initializable, Object source, ProvisionListenerStackCallback<T> provisionCallback) {
/* 39 */     super(source);
/* 40 */     this.provisionCallback = provisionCallback;
/* 41 */     this.initializable = (Initializable<? extends Provider<? extends T>>)Preconditions.checkNotNull(initializable, "provider");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public T get(InternalContext context, Dependency<?> dependency, boolean linked) throws InternalProvisionException {
/* 47 */     return circularGet(this.initializable.get(), context, dependency, this.provisionCallback);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected T provision(Provider<? extends T> provider, Dependency<?> dependency, ConstructionContext<T> constructionContext) throws InternalProvisionException {
/*    */     try {
/* 57 */       return super.provision(provider, dependency, constructionContext);
/* 58 */     } catch (RuntimeException userException) {
/* 59 */       throw InternalProvisionException.errorInProvider(userException).addSource(this.source);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 65 */     return this.initializable.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\InternalFactoryToInitializableAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */