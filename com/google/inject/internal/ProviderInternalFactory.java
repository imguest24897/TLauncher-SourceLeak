/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.inject.spi.Dependency;
/*    */ import javax.annotation.Nullable;
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
/*    */ abstract class ProviderInternalFactory<T>
/*    */   implements InternalFactory<T>
/*    */ {
/*    */   protected final Object source;
/*    */   
/*    */   ProviderInternalFactory(Object source) {
/* 36 */     this.source = Preconditions.checkNotNull(source, "source");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected T circularGet(final Provider<? extends T> provider, InternalContext context, final Dependency<?> dependency, @Nullable ProvisionListenerStackCallback<T> provisionCallback) throws InternalProvisionException {
/* 45 */     final ConstructionContext<T> constructionContext = context.getConstructionContext(this);
/*    */ 
/*    */     
/* 48 */     if (constructionContext.isConstructing()) {
/* 49 */       Class<?> expectedType = dependency.getKey().getTypeLiteral().getRawType();
/*    */ 
/*    */       
/* 52 */       T proxyType = (T)constructionContext.createProxy(context.getInjectorOptions(), expectedType);
/* 53 */       return proxyType;
/*    */     } 
/*    */ 
/*    */     
/* 57 */     constructionContext.startConstruction();
/*    */     try {
/* 59 */       if (provisionCallback == null) {
/* 60 */         return provision(provider, dependency, constructionContext);
/*    */       }
/* 62 */       return provisionCallback.provision(context, new ProvisionListenerStackCallback.ProvisionCallback<T>()
/*    */           {
/*    */             
/*    */             public T call() throws InternalProvisionException
/*    */             {
/* 67 */               return ProviderInternalFactory.this.provision(provider, dependency, constructionContext);
/*    */             }
/*    */           });
/*    */     } finally {
/*    */       
/* 72 */       constructionContext.removeCurrentReference();
/* 73 */       constructionContext.finishConstruction();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected T provision(Provider<? extends T> provider, Dependency<?> dependency, ConstructionContext<T> constructionContext) throws InternalProvisionException {
/* 86 */     T t = (T)provider.get();
/* 87 */     if (t == null && !dependency.isNullable()) {
/* 88 */       InternalProvisionException.onNullInjectedIntoNonNullableDependency(this.source, dependency);
/*    */     }
/* 90 */     constructionContext.setProxyDelegates(t);
/* 91 */     return t;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ProviderInternalFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */