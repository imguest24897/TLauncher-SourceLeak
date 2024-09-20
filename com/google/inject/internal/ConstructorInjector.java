/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.inject.spi.Dependency;
/*     */ import com.google.inject.spi.InjectionPoint;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ConstructorInjector<T>
/*     */ {
/*     */   private final ImmutableSet<InjectionPoint> injectableMembers;
/*     */   private final SingleParameterInjector<?>[] parameterInjectors;
/*     */   private final ConstructionProxy<T> constructionProxy;
/*     */   private final MembersInjectorImpl<T> membersInjector;
/*     */   
/*     */   ConstructorInjector(Set<InjectionPoint> injectableMembers, ConstructionProxy<T> constructionProxy, SingleParameterInjector<?>[] parameterInjectors, MembersInjectorImpl<T> membersInjector) {
/*  45 */     this.injectableMembers = ImmutableSet.copyOf(injectableMembers);
/*  46 */     this.constructionProxy = constructionProxy;
/*  47 */     this.parameterInjectors = parameterInjectors;
/*  48 */     this.membersInjector = membersInjector;
/*     */   }
/*     */   
/*     */   public ImmutableSet<InjectionPoint> getInjectableMembers() {
/*  52 */     return this.injectableMembers;
/*     */   }
/*     */   
/*     */   ConstructionProxy<T> getConstructionProxy() {
/*  56 */     return this.constructionProxy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object construct(final InternalContext context, Dependency<?> dependency, @Nullable ProvisionListenerStackCallback<T> provisionCallback) throws InternalProvisionException {
/*  68 */     final ConstructionContext<T> constructionContext = context.getConstructionContext(this);
/*     */     
/*  70 */     if (constructionContext.isConstructing())
/*     */     {
/*  72 */       return constructionContext.createProxy(context
/*  73 */           .getInjectorOptions(), dependency.getKey().getTypeLiteral().getRawType());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  78 */     T t = constructionContext.getCurrentReference();
/*  79 */     if (t != null) {
/*  80 */       if ((context.getInjectorOptions()).disableCircularProxies) {
/*  81 */         throw InternalProvisionException.circularDependenciesDisabled(dependency
/*  82 */             .getKey().getTypeLiteral().getRawType());
/*     */       }
/*  84 */       return t;
/*     */     } 
/*     */     
/*  87 */     constructionContext.startConstruction();
/*     */     
/*     */     try {
/*  90 */       if (provisionCallback == null) {
/*  91 */         return provision(context, constructionContext);
/*     */       }
/*  93 */       return provisionCallback.provision(context, new ProvisionListenerStackCallback.ProvisionCallback<T>()
/*     */           {
/*     */             
/*     */             public T call() throws InternalProvisionException
/*     */             {
/*  98 */               return ConstructorInjector.this.provision(context, constructionContext);
/*     */             }
/*     */           });
/*     */     } finally {
/*     */       
/* 103 */       constructionContext.finishConstruction();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private T provision(InternalContext context, ConstructionContext<T> constructionContext) throws InternalProvisionException {
/*     */     try {
/*     */       T t;
/*     */       try {
/* 113 */         Object[] parameters = SingleParameterInjector.getAll(context, this.parameterInjectors);
/* 114 */         t = this.constructionProxy.newInstance(parameters);
/* 115 */         constructionContext.setProxyDelegates(t);
/*     */       } finally {
/* 117 */         constructionContext.finishConstruction();
/*     */       } 
/*     */ 
/*     */       
/* 121 */       constructionContext.setCurrentReference(t);
/*     */       
/* 123 */       MembersInjectorImpl<T> localMembersInjector = this.membersInjector;
/* 124 */       localMembersInjector.injectMembers(t, context, false);
/* 125 */       localMembersInjector.notifyListeners(t);
/*     */       
/* 127 */       return t;
/* 128 */     } catch (InvocationTargetException userException) {
/* 129 */       T t; Throwable cause = (t.getCause() != null) ? t.getCause() : (Throwable)t;
/* 130 */       throw InternalProvisionException.errorInjectingConstructor(cause)
/* 131 */         .addSource(this.constructionProxy.getInjectionPoint());
/*     */     } finally {
/* 133 */       constructionContext.removeCurrentReference();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ConstructorInjector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */