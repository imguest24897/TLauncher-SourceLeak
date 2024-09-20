/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.inject.Binding;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.Provider;
/*     */ import com.google.inject.spi.BindingTargetVisitor;
/*     */ import com.google.inject.spi.ConstructorBinding;
/*     */ import com.google.inject.spi.ConvertedConstantBinding;
/*     */ import com.google.inject.spi.ExposedBinding;
/*     */ import com.google.inject.spi.InjectionPoint;
/*     */ import com.google.inject.spi.InstanceBinding;
/*     */ import com.google.inject.spi.LinkedKeyBinding;
/*     */ import com.google.inject.spi.PrivateElements;
/*     */ import com.google.inject.spi.ProviderBinding;
/*     */ import com.google.inject.spi.ProviderInstanceBinding;
/*     */ import com.google.inject.spi.ProviderKeyBinding;
/*     */ import com.google.inject.spi.UntargettedBinding;
/*     */ import java.util.Set;
/*     */ import javax.inject.Provider;
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
/*     */ final class BindingProcessor
/*     */   extends AbstractBindingProcessor
/*     */ {
/*     */   private final Initializer initializer;
/*     */   
/*     */   BindingProcessor(Errors errors, Initializer initializer, ProcessedBindingData processedBindingData) {
/*  48 */     super(errors, processedBindingData);
/*  49 */     this.initializer = initializer;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Boolean visit(Binding<T> command) {
/*  54 */     Class<?> rawType = command.getKey().getTypeLiteral().getRawType();
/*  55 */     if (Void.class.equals(rawType)) {
/*  56 */       if (command instanceof ProviderInstanceBinding && ((ProviderInstanceBinding)command)
/*  57 */         .getUserSuppliedProvider() instanceof ProviderMethod) {
/*     */         
/*  59 */         this.errors.voidProviderMethod();
/*     */       } else {
/*  61 */         this.errors.missingConstantValues();
/*     */       } 
/*  63 */       return Boolean.valueOf(true);
/*     */     } 
/*     */     
/*  66 */     if (rawType == Provider.class) {
/*  67 */       this.errors.bindingToProvider();
/*  68 */       return Boolean.valueOf(true);
/*     */     } 
/*     */     
/*  71 */     return (Boolean)command.acceptTargetVisitor((BindingTargetVisitor)new AbstractBindingProcessor.Processor<T, Boolean>((BindingImpl)command)
/*     */         {
/*     */           public Boolean visit(ConstructorBinding<? extends T> binding)
/*     */           {
/*  75 */             prepareBinding();
/*     */             
/*     */             try {
/*  78 */               ConstructorBindingImpl<T> onInjector = ConstructorBindingImpl.create(BindingProcessor.this.injector, this.key, binding
/*     */ 
/*     */                   
/*  81 */                   .getConstructor(), this.source, this.scoping, BindingProcessor.this.errors, false, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*  87 */               scheduleInitialization(onInjector);
/*  88 */               BindingProcessor.this.putBinding(onInjector);
/*  89 */             } catch (ErrorsException e) {
/*  90 */               BindingProcessor.this.errors.merge(e.getErrors());
/*  91 */               BindingProcessor.this.putBinding(BindingProcessor.this.invalidBinding(BindingProcessor.this.injector, this.key, this.source));
/*     */             } 
/*  93 */             return Boolean.valueOf(true);
/*     */           }
/*     */ 
/*     */           
/*     */           public Boolean visit(InstanceBinding<? extends T> binding) {
/*  98 */             prepareBinding();
/*  99 */             Set<InjectionPoint> injectionPoints = binding.getInjectionPoints();
/* 100 */             T instance = (T)binding.getInstance();
/*     */ 
/*     */ 
/*     */             
/* 104 */             Initializable<T> ref = BindingProcessor.this.initializer.requestInjection(BindingProcessor.this.injector, instance, (Binding)binding, this.source, injectionPoints);
/*     */             
/* 106 */             ConstantFactory<? extends T> factory = new ConstantFactory<>(ref);
/*     */             
/* 108 */             InternalFactory<? extends T> scopedFactory = Scoping.scope(this.key, BindingProcessor.this.injector, factory, this.source, this.scoping);
/* 109 */             BindingProcessor.this.putBinding(new InstanceBindingImpl(BindingProcessor.this.injector, this.key, this.source, scopedFactory, injectionPoints, instance));
/*     */ 
/*     */             
/* 112 */             return Boolean.valueOf(true);
/*     */           }
/*     */ 
/*     */           
/*     */           public Boolean visit(ProviderInstanceBinding<? extends T> binding) {
/* 117 */             prepareBinding();
/* 118 */             Provider<? extends T> provider = binding.getUserSuppliedProvider();
/* 119 */             if (provider instanceof InternalProviderInstanceBindingImpl.Factory) {
/*     */               
/* 121 */               InternalProviderInstanceBindingImpl.Factory<T> asProviderMethod = (InternalProviderInstanceBindingImpl.Factory)provider;
/*     */               
/* 123 */               return visitInternalProviderInstanceBindingFactory(asProviderMethod);
/*     */             } 
/* 125 */             Set<InjectionPoint> injectionPoints = binding.getInjectionPoints();
/*     */             
/* 127 */             Initializable<? extends Provider<? extends T>> initializable = BindingProcessor.this.initializer.requestInjection(BindingProcessor.this.injector, provider, null, this.source, injectionPoints);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 135 */             InternalFactory<T> factory = new InternalFactoryToInitializableAdapter<>(initializable, this.source, BindingProcessor.this.injector.provisionListenerStore.get((Binding)binding));
/*     */             
/* 137 */             InternalFactory<? extends T> scopedFactory = Scoping.scope(this.key, BindingProcessor.this.injector, factory, this.source, this.scoping);
/* 138 */             BindingProcessor.this.putBinding(new ProviderInstanceBindingImpl(BindingProcessor.this.injector, this.key, this.source, scopedFactory, this.scoping, provider, injectionPoints));
/*     */ 
/*     */             
/* 141 */             return Boolean.valueOf(true);
/*     */           }
/*     */ 
/*     */           
/*     */           public Boolean visit(ProviderKeyBinding<? extends T> binding) {
/* 146 */             prepareBinding();
/*     */             
/* 148 */             Key<? extends Provider<? extends T>> providerKey = binding.getProviderKey();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 156 */             BoundProviderFactory<T> boundProviderFactory = new BoundProviderFactory<>(BindingProcessor.this.injector, providerKey, this.source, BindingProcessor.this.injector.provisionListenerStore.get((Binding)binding));
/* 157 */             BindingProcessor.this.processedBindingData.addCreationListener(boundProviderFactory);
/*     */             
/* 159 */             InternalFactory<? extends T> scopedFactory = Scoping.scope(this.key, BindingProcessor.this.injector, boundProviderFactory, this.source, this.scoping);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 165 */             BindingProcessor.this.putBinding(new LinkedProviderBindingImpl(BindingProcessor.this.injector, this.key, this.source, scopedFactory, this.scoping, providerKey));
/*     */ 
/*     */             
/* 168 */             return Boolean.valueOf(true);
/*     */           }
/*     */ 
/*     */           
/*     */           public Boolean visit(LinkedKeyBinding<? extends T> binding) {
/* 173 */             prepareBinding();
/* 174 */             Key<? extends T> linkedKey = binding.getLinkedKey();
/* 175 */             if (this.key.equals(linkedKey))
/*     */             {
/* 177 */               BindingProcessor.this.errors.recursiveBinding(this.key, linkedKey);
/*     */             }
/*     */             
/* 180 */             FactoryProxy<T> factory = new FactoryProxy<>(BindingProcessor.this.injector, this.key, linkedKey, this.source);
/* 181 */             BindingProcessor.this.processedBindingData.addCreationListener(factory);
/*     */             
/* 183 */             InternalFactory<? extends T> scopedFactory = Scoping.scope(this.key, BindingProcessor.this.injector, factory, this.source, this.scoping);
/* 184 */             BindingProcessor.this.putBinding(new LinkedBindingImpl(BindingProcessor.this.injector, this.key, this.source, scopedFactory, this.scoping, linkedKey));
/*     */             
/* 186 */             return Boolean.valueOf(true);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           private Boolean visitInternalProviderInstanceBindingFactory(InternalProviderInstanceBindingImpl.Factory<T> provider) {
/* 198 */             InternalProviderInstanceBindingImpl<T> binding = new InternalProviderInstanceBindingImpl<>(BindingProcessor.this.injector, this.key, this.source, provider, Scoping.scope(this.key, BindingProcessor.this.injector, provider, this.source, this.scoping), this.scoping);
/*     */             
/* 200 */             switch (binding.getInitializationTiming()) {
/*     */               case DELAYED:
/* 202 */                 scheduleDelayedInitialization(binding);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 210 */                 BindingProcessor.this.putBinding(binding);
/* 211 */                 return Boolean.valueOf(true);case EAGER: scheduleInitialization(binding); BindingProcessor.this.putBinding(binding); return Boolean.valueOf(true);
/*     */             } 
/*     */             throw new AssertionError();
/*     */           }
/*     */           public Boolean visit(UntargettedBinding<? extends T> untargetted) {
/* 216 */             return Boolean.valueOf(false);
/*     */           }
/*     */ 
/*     */           
/*     */           public Boolean visit(ExposedBinding<? extends T> binding) {
/* 221 */             throw new IllegalArgumentException("Cannot apply a non-module element");
/*     */           }
/*     */ 
/*     */           
/*     */           public Boolean visit(ConvertedConstantBinding<? extends T> binding) {
/* 226 */             throw new IllegalArgumentException("Cannot apply a non-module element");
/*     */           }
/*     */ 
/*     */           
/*     */           public Boolean visit(ProviderBinding<? extends T> binding) {
/* 231 */             throw new IllegalArgumentException("Cannot apply a non-module element");
/*     */           }
/*     */ 
/*     */           
/*     */           protected Boolean visitOther(Binding<? extends T> binding) {
/* 236 */             throw new IllegalStateException("BindingProcessor should override all visitations");
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean visit(PrivateElements privateElements) {
/* 243 */     for (Key<?> key : (Iterable<Key<?>>)privateElements.getExposedKeys()) {
/* 244 */       bindExposed(privateElements, key);
/*     */     }
/* 246 */     return Boolean.valueOf(false);
/*     */   }
/*     */   
/*     */   private <T> void bindExposed(PrivateElements privateElements, Key<T> key) {
/* 250 */     ExposedKeyFactory<T> exposedKeyFactory = new ExposedKeyFactory<>(key, privateElements);
/* 251 */     this.processedBindingData.addCreationListener(exposedKeyFactory);
/* 252 */     putBinding(new ExposedBindingImpl(this.injector, privateElements
/*     */ 
/*     */           
/* 255 */           .getExposedSource(key), key, exposedKeyFactory, privateElements));
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\BindingProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */