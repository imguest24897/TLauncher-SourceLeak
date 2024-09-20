/*    */ package com.google.inject.internal;
/*    */ 
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
/*    */ final class SingleParameterInjector<T>
/*    */ {
/* 23 */   private static final Object[] NO_ARGUMENTS = new Object[0];
/*    */   
/*    */   private final Dependency<T> dependency;
/*    */   
/*    */   private final Object source;
/*    */   
/*    */   private final InternalFactory<? extends T> factory;
/*    */   
/*    */   SingleParameterInjector(Dependency<T> dependency, BindingImpl<? extends T> binding) {
/* 32 */     this.dependency = dependency;
/* 33 */     this.source = binding.getSource();
/* 34 */     this.factory = binding.getInternalFactory();
/*    */   }
/*    */   
/*    */   T inject(InternalContext context) throws InternalProvisionException {
/* 38 */     Dependency<T> localDependency = this.dependency;
/*    */     try {
/* 40 */       return this.factory.get(context, localDependency, false);
/* 41 */     } catch (InternalProvisionException ipe) {
/* 42 */       throw ipe.addSource(localDependency);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static Object[] getAll(InternalContext context, SingleParameterInjector<?>[] parameterInjectors) throws InternalProvisionException {
/* 51 */     if (parameterInjectors == null) {
/* 52 */       return NO_ARGUMENTS;
/*    */     }
/*    */     
/* 55 */     int size = parameterInjectors.length;
/* 56 */     Object[] parameters = new Object[size];
/*    */ 
/*    */     
/* 59 */     for (int i = 0; i < size; i++) {
/* 60 */       parameters[i] = parameterInjectors[i].inject(context);
/*    */     }
/* 62 */     return parameters;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\SingleParameterInjector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */