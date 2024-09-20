/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.inject.spi.InjectionPoint;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Stream;
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
/*    */ final class ConstructorInjectorStore
/*    */ {
/*    */   private final InjectorImpl injector;
/*    */   
/* 33 */   private final FailableCache<InjectionPoint, ConstructorInjector<?>> cache = new FailableCache<InjectionPoint, ConstructorInjector<?>>()
/*    */     {
/*    */       
/*    */       protected ConstructorInjector<?> create(InjectionPoint constructorInjector, Errors errors) throws ErrorsException
/*    */       {
/* 38 */         return ConstructorInjectorStore.this.createConstructor(constructorInjector, errors);
/*    */       }
/*    */     };
/*    */   
/*    */   ConstructorInjectorStore(InjectorImpl injector) {
/* 43 */     this.injector = injector;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ConstructorInjector<?> get(InjectionPoint constructorInjector, Errors errors) throws ErrorsException {
/* 49 */     return this.cache.get(constructorInjector, errors);
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
/*    */   
/*    */   boolean remove(InjectionPoint ip) {
/* 62 */     return this.cache.remove(ip);
/*    */   }
/*    */ 
/*    */   
/*    */   private <T> ConstructorInjector<T> createConstructor(InjectionPoint injectionPoint, Errors errors) throws ErrorsException {
/* 67 */     int numErrorsBefore = errors.size();
/*    */ 
/*    */     
/* 70 */     SingleParameterInjector[] arrayOfSingleParameterInjector = (SingleParameterInjector[])this.injector.getParametersInjectors(injectionPoint.getDependencies(), errors);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 75 */     MembersInjectorImpl<T> membersInjector = this.injector.membersInjectorStore.get(injectionPoint.getDeclaringType(), errors);
/* 76 */     ConstructionProxyFactory<T> factory = null;
/* 77 */     if (InternalFlags.isBytecodeGenEnabled()) {
/* 78 */       ImmutableList<MethodAspect> injectorAspects = this.injector.getBindingData().getMethodAspects();
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 83 */       ImmutableList<MethodAspect> methodAspects = membersInjector.getAddedAspects().isEmpty() ? injectorAspects : (ImmutableList<MethodAspect>)Stream.concat(injectorAspects.stream(), membersInjector.getAddedAspects().stream()).collect(ImmutableList.toImmutableList());
/* 84 */       factory = new ProxyFactory<>(injectionPoint, (Iterable<MethodAspect>)methodAspects);
/*    */     } else {
/* 86 */       factory = new DefaultConstructionProxyFactory<>(injectionPoint);
/*    */     } 
/*    */     
/* 89 */     errors.throwIfNewErrors(numErrorsBefore);
/*    */     
/* 91 */     return new ConstructorInjector<>((Set<InjectionPoint>)membersInjector
/* 92 */         .getInjectionPoints(), factory
/* 93 */         .create(), (SingleParameterInjector<?>[])arrayOfSingleParameterInjector, membersInjector);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ConstructorInjectorStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */