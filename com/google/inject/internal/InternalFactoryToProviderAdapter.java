/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.inject.Provider;
/*    */ import com.google.inject.spi.Dependency;
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
/*    */ final class InternalFactoryToProviderAdapter<T>
/*    */   implements InternalFactory<T>
/*    */ {
/*    */   private final Provider<? extends T> provider;
/*    */   private final Object source;
/*    */   
/*    */   public InternalFactoryToProviderAdapter(Provider<? extends T> provider, Object source) {
/* 31 */     this.provider = (Provider<? extends T>)Preconditions.checkNotNull(provider, "provider");
/* 32 */     this.source = Preconditions.checkNotNull(source, "source");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public T get(InternalContext context, Dependency<?> dependency, boolean linked) throws InternalProvisionException {
/* 43 */     context.setDependency(dependency);
/*    */     try {
/* 45 */       T t = (T)this.provider.get();
/* 46 */       if (t == null && !dependency.isNullable()) {
/* 47 */         InternalProvisionException.onNullInjectedIntoNonNullableDependency(this.source, dependency);
/*    */       }
/* 49 */       return t;
/* 50 */     } catch (RuntimeException userException) {
/* 51 */       throw InternalProvisionException.errorInProvider(userException).addSource(this.source);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return this.provider.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\InternalFactoryToProviderAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */