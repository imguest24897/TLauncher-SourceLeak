/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.base.MoreObjects;
/*    */ import com.google.inject.Key;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class FactoryProxy<T>
/*    */   implements InternalFactory<T>, CreationListener
/*    */ {
/*    */   private final InjectorImpl injector;
/*    */   private final Key<T> key;
/*    */   private final Key<? extends T> targetKey;
/*    */   private final Object source;
/*    */   private InternalFactory<? extends T> targetFactory;
/*    */   
/*    */   FactoryProxy(InjectorImpl injector, Key<T> key, Key<? extends T> targetKey, Object source) {
/* 38 */     this.injector = injector;
/* 39 */     this.key = key;
/* 40 */     this.targetKey = targetKey;
/* 41 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   public void notify(Errors errors) {
/*    */     try {
/* 47 */       this
/* 48 */         .targetFactory = this.injector.getInternalFactory((Key)this.targetKey, errors
/* 49 */           .withSource(this.source), InjectorImpl.JitLimitation.NEW_OR_EXISTING_JIT);
/* 50 */     } catch (ErrorsException e) {
/* 51 */       errors.merge(e.getErrors());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public T get(InternalContext context, Dependency<?> dependency, boolean linked) throws InternalProvisionException {
/* 58 */     Key<? extends T> localTargetKey = this.targetKey;
/*    */     try {
/* 60 */       return this.targetFactory.get(context, dependency, true);
/* 61 */     } catch (InternalProvisionException ipe) {
/* 62 */       throw ipe.addSource(localTargetKey);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 68 */     return MoreObjects.toStringHelper(FactoryProxy.class)
/* 69 */       .add("key", this.key)
/* 70 */       .add("provider", this.targetFactory)
/* 71 */       .toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\FactoryProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */