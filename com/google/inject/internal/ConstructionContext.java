/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ final class ConstructionContext<T>
/*    */ {
/*    */   T currentReference;
/*    */   boolean constructing;
/*    */   List<DelegatingInvocationHandler<T>> invocationHandlers;
/*    */   
/*    */   public T getCurrentReference() {
/* 36 */     return this.currentReference;
/*    */   }
/*    */   
/*    */   public void removeCurrentReference() {
/* 40 */     this.currentReference = null;
/*    */   }
/*    */   
/*    */   public void setCurrentReference(T currentReference) {
/* 44 */     this.currentReference = currentReference;
/*    */   }
/*    */   
/*    */   public boolean isConstructing() {
/* 48 */     return this.constructing;
/*    */   }
/*    */   
/*    */   public void startConstruction() {
/* 52 */     this.constructing = true;
/*    */   }
/*    */   
/*    */   public void finishConstruction() {
/* 56 */     this.constructing = false;
/* 57 */     this.invocationHandlers = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object createProxy(InjectorImpl.InjectorOptions injectorOptions, Class<?> expectedType) throws InternalProvisionException {
/* 62 */     if (injectorOptions.disableCircularProxies) {
/* 63 */       throw InternalProvisionException.circularDependenciesDisabled(expectedType);
/*    */     }
/* 65 */     if (!expectedType.isInterface()) {
/* 66 */       throw InternalProvisionException.cannotProxyClass(expectedType);
/*    */     }
/*    */     
/* 69 */     if (this.invocationHandlers == null) {
/* 70 */       this.invocationHandlers = new ArrayList<>();
/*    */     }
/*    */     
/* 73 */     DelegatingInvocationHandler<T> invocationHandler = new DelegatingInvocationHandler<>();
/* 74 */     this.invocationHandlers.add(invocationHandler);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 79 */     return BytecodeGen.newCircularProxy(expectedType, invocationHandler);
/*    */   }
/*    */   
/*    */   public void setProxyDelegates(T delegate) {
/* 83 */     if (this.invocationHandlers != null) {
/* 84 */       for (DelegatingInvocationHandler<T> handler : this.invocationHandlers) {
/* 85 */         handler.setDelegate(delegate);
/*    */       }
/*    */       
/* 88 */       this.invocationHandlers = null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ConstructionContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */