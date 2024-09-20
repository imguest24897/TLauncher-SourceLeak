/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.inject.Provider;
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
/*    */ final class ProviderToInternalFactoryAdapter<T>
/*    */   implements Provider<T>
/*    */ {
/*    */   private final InjectorImpl injector;
/*    */   private final InternalFactory<? extends T> internalFactory;
/*    */   
/*    */   public ProviderToInternalFactoryAdapter(InjectorImpl injector, InternalFactory<? extends T> internalFactory) {
/* 29 */     this.injector = injector;
/* 30 */     this.internalFactory = internalFactory;
/*    */   }
/*    */ 
/*    */   
/*    */   public T get() {
/* 35 */     InternalContext context = this.injector.enterContext();
/*    */ 
/*    */ 
/*    */     
/*    */     try {
/* 40 */       T t = this.internalFactory.get(context, context.getDependency(), true);
/* 41 */       return t;
/* 42 */     } catch (InternalProvisionException e) {
/* 43 */       throw e.toProvisionException();
/*    */     } finally {
/* 45 */       context.close();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   InjectorImpl getInjector() {
/* 51 */     return this.injector;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 56 */     return this.internalFactory.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ProviderToInternalFactoryAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */