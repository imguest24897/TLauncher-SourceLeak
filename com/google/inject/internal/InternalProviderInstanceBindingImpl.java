/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.Provider;
/*     */ import com.google.inject.spi.Dependency;
/*     */ import com.google.inject.spi.HasDependencies;
/*     */ import com.google.inject.spi.InjectionPoint;
/*     */ import java.util.Set;
/*     */ import javax.inject.Provider;
/*     */ 
/*     */ 
/*     */ 
/*     */ final class InternalProviderInstanceBindingImpl<T>
/*     */   extends ProviderInstanceBindingImpl<T>
/*     */   implements DelayedInitialize
/*     */ {
/*     */   private final Factory<T> originalFactory;
/*     */   
/*     */   enum InitializationTiming
/*     */   {
/*  22 */     EAGER,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  30 */     DELAYED;
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
/*     */   InternalProviderInstanceBindingImpl(InjectorImpl injector, Key<T> key, Object source, Factory<T> originalFactory, InternalFactory<? extends T> scopedFactory, Scoping scoping) {
/*  42 */     super(injector, key, source, scopedFactory, scoping, (Provider<? extends T>)originalFactory, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  49 */         (Set<InjectionPoint>)ImmutableSet.of());
/*  50 */     this.originalFactory = originalFactory;
/*     */   }
/*     */   
/*     */   InitializationTiming getInitializationTiming() {
/*  54 */     return this.originalFactory.initializationTiming;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initialize(InjectorImpl injector, Errors errors) throws ErrorsException {
/*  59 */     this.originalFactory.source = getSource();
/*  60 */     this.originalFactory.provisionCallback = injector.provisionListenerStore.get(this);
/*     */ 
/*     */     
/*  63 */     this.originalFactory.delegateProvider = getProvider();
/*  64 */     this.originalFactory.initialize(injector, errors);
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class Factory<T>
/*     */     implements InternalFactory<T>, Provider<T>, HasDependencies
/*     */   {
/*     */     private final InternalProviderInstanceBindingImpl.InitializationTiming initializationTiming;
/*     */     
/*     */     private Object source;
/*     */     private Provider<T> delegateProvider;
/*     */     ProvisionListenerStackCallback<T> provisionCallback;
/*     */     
/*     */     Factory(InternalProviderInstanceBindingImpl.InitializationTiming initializationTiming) {
/*  78 */       this.initializationTiming = initializationTiming;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final Object getSource() {
/*  89 */       return this.source;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final T get() {
/* 101 */       Provider<T> local = this.delegateProvider;
/* 102 */       if (local == null) {
/* 103 */         throw new IllegalStateException("This Provider cannot be used until the Injector has been created.");
/*     */       }
/*     */       
/* 106 */       return (T)local.get();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public T get(final InternalContext context, final Dependency<?> dependency, boolean linked) throws InternalProvisionException {
/* 112 */       if (this.provisionCallback == null) {
/* 113 */         return doProvision(context, dependency);
/*     */       }
/* 115 */       return this.provisionCallback.provision(context, new ProvisionListenerStackCallback.ProvisionCallback<T>()
/*     */           {
/*     */             
/*     */             public T call() throws InternalProvisionException
/*     */             {
/* 120 */               return InternalProviderInstanceBindingImpl.Factory.this.doProvision(context, dependency);
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract void initialize(InjectorImpl param1InjectorImpl, Errors param1Errors) throws ErrorsException;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract T doProvision(InternalContext param1InternalContext, Dependency<?> param1Dependency) throws InternalProvisionException;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static abstract class CyclicFactory<T>
/*     */     extends Factory<T>
/*     */   {
/*     */     CyclicFactory(InternalProviderInstanceBindingImpl.InitializationTiming initializationTiming) {
/* 142 */       super(initializationTiming);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final T get(final InternalContext context, final Dependency<?> dependency, boolean linked) throws InternalProvisionException {
/* 149 */       final ConstructionContext<T> constructionContext = context.getConstructionContext(this);
/*     */       
/* 151 */       if (constructionContext.isConstructing()) {
/* 152 */         Class<?> expectedType = dependency.getKey().getTypeLiteral().getRawType();
/*     */ 
/*     */         
/* 155 */         T proxyType = (T)constructionContext.createProxy(context.getInjectorOptions(), expectedType);
/* 156 */         return proxyType;
/*     */       } 
/*     */       
/* 159 */       constructionContext.startConstruction();
/*     */       try {
/* 161 */         if (this.provisionCallback == null) {
/* 162 */           return provision(dependency, context, constructionContext);
/*     */         }
/* 164 */         return this.provisionCallback.provision(context, new ProvisionListenerStackCallback.ProvisionCallback<T>()
/*     */             {
/*     */               
/*     */               public T call() throws InternalProvisionException
/*     */               {
/* 169 */                 return InternalProviderInstanceBindingImpl.CyclicFactory.this.provision(dependency, context, constructionContext);
/*     */               }
/*     */             });
/*     */       } finally {
/*     */         
/* 174 */         constructionContext.removeCurrentReference();
/* 175 */         constructionContext.finishConstruction();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private T provision(Dependency<?> dependency, InternalContext context, ConstructionContext<T> constructionContext) throws InternalProvisionException {
/*     */       try {
/* 185 */         T t = doProvision(context, dependency);
/* 186 */         constructionContext.setProxyDelegates(t);
/* 187 */         return t;
/* 188 */       } catch (InternalProvisionException ipe) {
/* 189 */         throw ipe.addSource(getSource());
/* 190 */       } catch (Throwable t) {
/* 191 */         throw InternalProvisionException.errorInProvider(t).addSource(getSource());
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\InternalProviderInstanceBindingImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */