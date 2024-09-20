/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.inject.Binder;
/*     */ import com.google.inject.Binding;
/*     */ import com.google.inject.Injector;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.Module;
/*     */ import com.google.inject.Provider;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.binder.LinkedBindingBuilder;
/*     */ import com.google.inject.multibindings.MultibindingsTargetVisitor;
/*     */ import com.google.inject.multibindings.OptionalBinderBinding;
/*     */ import com.google.inject.spi.BindingTargetVisitor;
/*     */ import com.google.inject.spi.Dependency;
/*     */ import com.google.inject.spi.Element;
/*     */ import com.google.inject.spi.ProviderInstanceBinding;
/*     */ import com.google.inject.spi.ProviderWithExtensionVisitor;
/*     */ import com.google.inject.util.Types;
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import javax.inject.Provider;
/*     */ import javax.inject.Qualifier;
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
/*     */ public final class RealOptionalBinder<T>
/*     */   implements Module
/*     */ {
/*     */   private final BindingSelection<T> bindingSelection;
/*     */   private final Binder binder;
/*     */   
/*     */   public static <T> RealOptionalBinder<T> newRealOptionalBinder(Binder binder, Key<T> type) {
/*  57 */     binder = binder.skipSources(new Class[] { RealOptionalBinder.class });
/*  58 */     RealOptionalBinder<T> optionalBinder = new RealOptionalBinder<>(binder, type);
/*  59 */     binder.install(optionalBinder);
/*  60 */     return optionalBinder;
/*     */   }
/*     */ 
/*     */   
/*     */   static <T> TypeLiteral<Optional<T>> optionalOf(TypeLiteral<T> type) {
/*  65 */     return 
/*  66 */       TypeLiteral.get(Types.newParameterizedType(Optional.class, new Type[] { type.getType() }));
/*     */   }
/*     */ 
/*     */   
/*     */   static <T> TypeLiteral<Optional<T>> javaOptionalOf(TypeLiteral<T> type) {
/*  71 */     return 
/*  72 */       TypeLiteral.get(Types.newParameterizedType(Optional.class, new Type[] { type.getType() }));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> TypeLiteral<Optional<Provider<T>>> optionalOfJavaxProvider(TypeLiteral<T> type) {
/*  78 */     return 
/*  79 */       TypeLiteral.get(
/*  80 */         Types.newParameterizedType(Optional.class, new Type[] {
/*  81 */             Types.newParameterizedType(Provider.class, new Type[] { type.getType() })
/*     */           }));
/*     */   }
/*     */ 
/*     */   
/*     */   static <T> TypeLiteral<Optional<Provider<T>>> javaOptionalOfJavaxProvider(TypeLiteral<T> type) {
/*  87 */     return 
/*  88 */       TypeLiteral.get(
/*  89 */         Types.newParameterizedType(Optional.class, new Type[] {
/*     */             
/*  91 */             Types.newParameterizedType(Provider.class, new Type[] { type.getType() })
/*     */           }));
/*     */   }
/*     */   
/*     */   static <T> TypeLiteral<Optional<Provider<T>>> optionalOfProvider(TypeLiteral<T> type) {
/*  96 */     return 
/*  97 */       TypeLiteral.get(
/*  98 */         Types.newParameterizedType(Optional.class, new Type[] {
/*  99 */             Types.newParameterizedType(Provider.class, new Type[] { type.getType() })
/*     */           }));
/*     */   }
/*     */ 
/*     */   
/*     */   static <T> TypeLiteral<Optional<Provider<T>>> javaOptionalOfProvider(TypeLiteral<T> type) {
/* 105 */     return 
/* 106 */       TypeLiteral.get(
/* 107 */         Types.newParameterizedType(Optional.class, new Type[] {
/* 108 */             Types.newParameterizedType(Provider.class, new Type[] { type.getType() })
/*     */           }));
/*     */   }
/*     */   
/*     */   static <T> Key<Provider<T>> providerOf(Key<T> key) {
/* 113 */     Type providerT = Types.providerOf(key.getTypeLiteral().getType());
/* 114 */     return key.ofType(providerT);
/*     */   }
/*     */   
/*     */   enum Source {
/* 118 */     DEFAULT,
/* 119 */     ACTUAL;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RealOptionalBinder(Binder binder, Key<T> typeKey) {
/* 138 */     this.bindingSelection = new BindingSelection<>(typeKey);
/* 139 */     this.binder = binder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addDirectTypeBinding(Binder binder) {
/* 147 */     binder
/* 148 */       .bind(this.bindingSelection.getDirectKey())
/* 149 */       .toProvider(new RealDirectTypeProvider<>(this.bindingSelection));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Key<T> getKeyForDefaultBinding() {
/* 159 */     this.bindingSelection.checkNotInitialized();
/* 160 */     addDirectTypeBinding(this.binder);
/* 161 */     return this.bindingSelection.getKeyForDefaultBinding();
/*     */   }
/*     */   
/*     */   public LinkedBindingBuilder<T> setDefault() {
/* 165 */     return this.binder.bind(getKeyForDefaultBinding());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Key<T> getKeyForActualBinding() {
/* 175 */     this.bindingSelection.checkNotInitialized();
/* 176 */     addDirectTypeBinding(this.binder);
/* 177 */     return this.bindingSelection.getKeyForActualBinding();
/*     */   }
/*     */   
/*     */   public LinkedBindingBuilder<T> setBinding() {
/* 181 */     return this.binder.bind(getKeyForActualBinding());
/*     */   }
/*     */ 
/*     */   
/*     */   public void configure(Binder binder) {
/* 186 */     this.bindingSelection.checkNotInitialized();
/* 187 */     Key<T> key = this.bindingSelection.getDirectKey();
/* 188 */     TypeLiteral<T> typeLiteral = key.getTypeLiteral();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 197 */     InternalProviderInstanceBindingImpl.Factory<Optional<Provider<T>>> optionalProviderFactory = new RealOptionalProviderProvider<>(this.bindingSelection);
/*     */     
/* 199 */     binder.bind(key.ofType(optionalOfProvider(typeLiteral))).toProvider(optionalProviderFactory);
/*     */ 
/*     */     
/* 202 */     InternalProviderInstanceBindingImpl.Factory<Optional<Provider<T>>> javaOptionalProviderFactory = new JavaOptionalProviderProvider<>(this.bindingSelection);
/* 203 */     binder
/* 204 */       .bind(key.ofType(javaOptionalOfProvider(typeLiteral)))
/* 205 */       .toProvider(javaOptionalProviderFactory);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 213 */     InternalProviderInstanceBindingImpl.Factory<Optional<Provider<T>>> factory1 = optionalProviderFactory;
/*     */     
/* 215 */     binder
/* 216 */       .bind(key.ofType(optionalOfJavaxProvider(typeLiteral)))
/* 217 */       .toProvider(factory1);
/*     */ 
/*     */ 
/*     */     
/* 221 */     InternalProviderInstanceBindingImpl.Factory<Optional<Provider<T>>> factory2 = javaOptionalProviderFactory;
/*     */     
/* 223 */     binder
/* 224 */       .bind(key.ofType(javaOptionalOfJavaxProvider(typeLiteral)))
/* 225 */       .toProvider(factory2);
/*     */ 
/*     */     
/* 228 */     Key<Optional<T>> optionalKey = key.ofType(optionalOf(typeLiteral));
/* 229 */     binder
/* 230 */       .bind(optionalKey)
/* 231 */       .toProvider(new RealOptionalKeyProvider<>(this.bindingSelection, optionalKey));
/*     */     
/* 233 */     Key<Optional<T>> javaOptionalKey = key.ofType(javaOptionalOf(typeLiteral));
/* 234 */     binder
/* 235 */       .bind(javaOptionalKey)
/* 236 */       .toProvider(new JavaOptionalProvider<>(this.bindingSelection, javaOptionalKey));
/*     */   }
/*     */   
/*     */   @Retention(RetentionPolicy.RUNTIME)
/*     */   @Qualifier
/*     */   static @interface Default {
/*     */     String value(); }
/*     */   
/*     */   @Retention(RetentionPolicy.RUNTIME)
/*     */   @Qualifier
/*     */   static @interface Actual {
/*     */     String value(); }
/*     */   
/*     */   private static final class JavaOptionalProvider<T> extends RealOptionalBinderProviderWithDependencies<T, Optional<T>> implements ProviderWithExtensionVisitor<Optional<T>>, OptionalBinderBinding<Optional<T>> { private final Key<Optional<T>> optionalKey;
/*     */     
/*     */     JavaOptionalProvider(RealOptionalBinder.BindingSelection<T> bindingSelection, Key<Optional<T>> optionalKey) {
/* 252 */       super(bindingSelection);
/* 253 */       this.optionalKey = optionalKey;
/*     */     }
/*     */     private Dependency<?> targetDependency; private InternalFactory<? extends T> target;
/*     */     
/*     */     void doInitialize() {
/* 258 */       if (this.bindingSelection.getBinding() != null) {
/* 259 */         this.target = this.bindingSelection.getBinding().getInternalFactory();
/* 260 */         this.targetDependency = this.bindingSelection.getDependency();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected Optional<T> doProvision(InternalContext context, Dependency<?> currentDependency) throws InternalProvisionException {
/*     */       T result;
/* 268 */       InternalFactory<? extends T> local = this.target;
/* 269 */       if (local == null) {
/* 270 */         return Optional.empty();
/*     */       }
/* 272 */       Dependency<?> localDependency = this.targetDependency;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 278 */         result = local.get(context, localDependency, false);
/* 279 */       } catch (InternalProvisionException ipe) {
/* 280 */         throw ipe.addSource(localDependency);
/*     */       } 
/* 282 */       return Optional.ofNullable(result);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Dependency<?>> getDependencies() {
/* 287 */       return (Set<Dependency<?>>)this.bindingSelection.dependencies;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <B, R> R acceptExtensionVisitor(BindingTargetVisitor<B, R> visitor, ProviderInstanceBinding<? extends B> binding) {
/* 294 */       if (visitor instanceof MultibindingsTargetVisitor) {
/* 295 */         return (R)((MultibindingsTargetVisitor)visitor).visit(this);
/*     */       }
/* 297 */       return (R)visitor.visit(binding);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean containsElement(Element element) {
/* 303 */       return this.bindingSelection.containsElement(element);
/*     */     }
/*     */ 
/*     */     
/*     */     public Binding<?> getActualBinding() {
/* 308 */       return this.bindingSelection.getActualBinding();
/*     */     }
/*     */ 
/*     */     
/*     */     public Binding<?> getDefaultBinding() {
/* 313 */       return this.bindingSelection.getDefaultBinding();
/*     */     }
/*     */ 
/*     */     
/*     */     public Key<Optional<T>> getKey() {
/* 318 */       return this.optionalKey;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Key<?>> getAlternateKeys() {
/* 323 */       Key<?> key = this.bindingSelection.getDirectKey();
/* 324 */       TypeLiteral<?> typeLiteral = key.getTypeLiteral();
/* 325 */       return (Set<Key<?>>)ImmutableSet.of(key
/* 326 */           .ofType(RealOptionalBinder.javaOptionalOfProvider(typeLiteral)), key
/* 327 */           .ofType(RealOptionalBinder.javaOptionalOfJavaxProvider(typeLiteral)));
/*     */     } }
/*     */ 
/*     */   
/*     */   private static final class JavaOptionalProviderProvider<T>
/*     */     extends RealOptionalBinderProviderWithDependencies<T, Optional<Provider<T>>>
/*     */   {
/*     */     private Optional<Provider<T>> value;
/*     */     
/*     */     JavaOptionalProviderProvider(RealOptionalBinder.BindingSelection<T> bindingSelection) {
/* 337 */       super(bindingSelection);
/*     */     }
/*     */ 
/*     */     
/*     */     void doInitialize() {
/* 342 */       if (this.bindingSelection.getBinding() == null) {
/* 343 */         this.value = Optional.empty();
/*     */       } else {
/* 345 */         this.value = Optional.of(this.bindingSelection.getBinding().getProvider());
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected Optional<Provider<T>> doProvision(InternalContext context, Dependency<?> dependency) {
/* 352 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Dependency<?>> getDependencies() {
/* 357 */       return (Set<Dependency<?>>)this.bindingSelection.providerDependencies();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class RealDirectTypeProvider<T>
/*     */     extends RealOptionalBinderProviderWithDependencies<T, T>
/*     */   {
/*     */     private Key<? extends T> targetKey;
/*     */     private InternalFactory<? extends T> targetFactory;
/*     */     
/*     */     RealDirectTypeProvider(RealOptionalBinder.BindingSelection<T> bindingSelection) {
/* 369 */       super(bindingSelection);
/*     */     }
/*     */ 
/*     */     
/*     */     void doInitialize() {
/* 374 */       BindingImpl<T> targetBinding = this.bindingSelection.getBinding();
/*     */ 
/*     */       
/* 377 */       this.targetKey = targetBinding.getKey();
/* 378 */       this.targetFactory = targetBinding.getInternalFactory();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected T doProvision(InternalContext context, Dependency<?> dependency) throws InternalProvisionException {
/*     */       try {
/* 385 */         return this.targetFactory.get(context, dependency, true);
/* 386 */       } catch (InternalProvisionException ipe) {
/* 387 */         throw ipe.addSource(this.targetKey);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Dependency<?>> getDependencies() {
/* 393 */       return (Set<Dependency<?>>)this.bindingSelection.dependencies;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class RealOptionalProviderProvider<T>
/*     */     extends RealOptionalBinderProviderWithDependencies<T, Optional<Provider<T>>>
/*     */   {
/*     */     private Optional<Provider<T>> value;
/*     */     
/*     */     RealOptionalProviderProvider(RealOptionalBinder.BindingSelection<T> bindingSelection) {
/* 403 */       super(bindingSelection);
/*     */     }
/*     */ 
/*     */     
/*     */     void doInitialize() {
/* 408 */       if (this.bindingSelection.getBinding() == null) {
/* 409 */         this.value = Optional.absent();
/*     */       } else {
/* 411 */         this.value = Optional.of(this.bindingSelection.getBinding().getProvider());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected Optional<Provider<T>> doProvision(InternalContext context, Dependency<?> dependency) {
/* 417 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Dependency<?>> getDependencies() {
/* 422 */       return (Set<Dependency<?>>)this.bindingSelection.providerDependencies();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class RealOptionalKeyProvider<T>
/*     */     extends RealOptionalBinderProviderWithDependencies<T, Optional<T>>
/*     */     implements ProviderWithExtensionVisitor<Optional<T>>, OptionalBinderBinding<Optional<T>>
/*     */   {
/*     */     private final Key<Optional<T>> optionalKey;
/*     */     
/*     */     private Dependency<?> targetDependency;
/*     */     
/*     */     private InternalFactory<? extends T> delegate;
/*     */ 
/*     */     
/*     */     RealOptionalKeyProvider(RealOptionalBinder.BindingSelection<T> bindingSelection, Key<Optional<T>> optionalKey) {
/* 439 */       super(bindingSelection);
/* 440 */       this.optionalKey = optionalKey;
/*     */     }
/*     */ 
/*     */     
/*     */     void doInitialize() {
/* 445 */       if (this.bindingSelection.getBinding() != null) {
/* 446 */         this.delegate = this.bindingSelection.getBinding().getInternalFactory();
/* 447 */         this.targetDependency = this.bindingSelection.getDependency();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected Optional<T> doProvision(InternalContext context, Dependency<?> currentDependency) throws InternalProvisionException {
/*     */       T result;
/* 454 */       InternalFactory<? extends T> local = this.delegate;
/* 455 */       if (local == null) {
/* 456 */         return Optional.absent();
/*     */       }
/* 458 */       Dependency<?> localDependency = this.targetDependency;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 465 */         result = local.get(context, localDependency, false);
/* 466 */       } catch (InternalProvisionException ipe) {
/* 467 */         throw ipe.addSource(localDependency);
/*     */       } 
/* 469 */       return Optional.fromNullable(result);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Dependency<?>> getDependencies() {
/* 474 */       return (Set<Dependency<?>>)this.bindingSelection.dependencies();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <B, R> R acceptExtensionVisitor(BindingTargetVisitor<B, R> visitor, ProviderInstanceBinding<? extends B> binding) {
/* 481 */       if (visitor instanceof MultibindingsTargetVisitor) {
/* 482 */         return (R)((MultibindingsTargetVisitor)visitor).visit(this);
/*     */       }
/* 484 */       return (R)visitor.visit(binding);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Key<Optional<T>> getKey() {
/* 490 */       return this.optionalKey;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Key<?>> getAlternateKeys() {
/* 495 */       Key<?> key = this.bindingSelection.getDirectKey();
/* 496 */       TypeLiteral<?> typeLiteral = key.getTypeLiteral();
/* 497 */       return (Set<Key<?>>)ImmutableSet.of(key
/* 498 */           .ofType(RealOptionalBinder.optionalOfProvider(typeLiteral)), key
/* 499 */           .ofType(RealOptionalBinder.optionalOfJavaxProvider(typeLiteral)));
/*     */     }
/*     */ 
/*     */     
/*     */     public Binding<?> getActualBinding() {
/* 504 */       return this.bindingSelection.getActualBinding();
/*     */     }
/*     */ 
/*     */     
/*     */     public Binding<?> getDefaultBinding() {
/* 509 */       return this.bindingSelection.getDefaultBinding();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsElement(Element element) {
/* 514 */       return this.bindingSelection.containsElement(element);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class BindingSelection<T>
/*     */   {
/* 526 */     private static final ImmutableSet<Dependency<?>> MODULE_DEPENDENCIES = ImmutableSet.of(Dependency.get(Key.get(Injector.class)));
/*     */     
/*     */     BindingImpl<T> actualBinding;
/*     */     
/*     */     BindingImpl<T> defaultBinding;
/*     */     
/*     */     BindingImpl<T> binding;
/*     */     
/*     */     private boolean initialized;
/*     */     private final Key<T> key;
/* 536 */     private ImmutableSet<Dependency<?>> dependencies = MODULE_DEPENDENCIES;
/* 537 */     private ImmutableSet<Dependency<?>> providerDependencies = MODULE_DEPENDENCIES;
/*     */ 
/*     */     
/*     */     private String bindingName;
/*     */ 
/*     */     
/*     */     private Key<T> defaultBindingKey;
/*     */     
/*     */     private Key<T> actualBindingKey;
/*     */ 
/*     */     
/*     */     BindingSelection(Key<T> key) {
/* 549 */       this.key = key;
/*     */     }
/*     */     
/*     */     void checkNotInitialized() {
/* 553 */       Errors.checkConfiguration(!this.initialized, "already initialized", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*     */     void initialize(InjectorImpl injector, Errors errors) {
/* 558 */       if (this.initialized) {
/*     */         return;
/*     */       }
/* 561 */       this.actualBinding = injector.getExistingBinding(getKeyForActualBinding());
/* 562 */       this.defaultBinding = injector.getExistingBinding(getKeyForDefaultBinding());
/*     */       
/* 564 */       BindingImpl<T> userBinding = injector.getExistingBinding(this.key);
/* 565 */       if (this.actualBinding != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 570 */         this.binding = this.actualBinding;
/* 571 */       } else if (this.defaultBinding != null) {
/* 572 */         this.binding = this.defaultBinding;
/* 573 */       } else if (userBinding != null) {
/*     */ 
/*     */ 
/*     */         
/* 577 */         this.binding = userBinding;
/* 578 */         this.actualBinding = userBinding;
/*     */       } 
/* 580 */       if (this.binding != null) {
/* 581 */         this.dependencies = ImmutableSet.of(Dependency.get(this.binding.getKey()));
/* 582 */         this
/* 583 */           .providerDependencies = ImmutableSet.of(Dependency.get(RealOptionalBinder.providerOf(this.binding.getKey())));
/*     */       } else {
/* 585 */         this.dependencies = ImmutableSet.of();
/* 586 */         this.providerDependencies = ImmutableSet.of();
/*     */       } 
/* 588 */       checkBindingIsNotRecursive(errors);
/* 589 */       this.initialized = true;
/*     */     }
/*     */     
/*     */     private void checkBindingIsNotRecursive(Errors errors) {
/* 593 */       if (this.binding instanceof LinkedBindingImpl) {
/* 594 */         LinkedBindingImpl<T> linkedBindingImpl = (LinkedBindingImpl<T>)this.binding;
/* 595 */         if (linkedBindingImpl.getLinkedKey().equals(this.key))
/*     */         {
/* 597 */           errors.recursiveBinding(this.key, linkedBindingImpl.getLinkedKey());
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     Key<T> getKeyForDefaultBinding() {
/* 603 */       if (this.defaultBindingKey == null) {
/* 604 */         this.defaultBindingKey = this.key.withAnnotation(new RealOptionalBinder.DefaultImpl(getBindingName()));
/*     */       }
/* 606 */       return this.defaultBindingKey;
/*     */     }
/*     */     
/*     */     Key<T> getKeyForActualBinding() {
/* 610 */       if (this.actualBindingKey == null) {
/* 611 */         this.actualBindingKey = this.key.withAnnotation(new RealOptionalBinder.ActualImpl(getBindingName()));
/*     */       }
/* 613 */       return this.actualBindingKey;
/*     */     }
/*     */     
/*     */     Key<T> getDirectKey() {
/* 617 */       return this.key;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private String getBindingName() {
/* 623 */       if (this.bindingName == null) {
/* 624 */         this.bindingName = Annotations.nameOf(this.key);
/*     */       }
/* 626 */       return this.bindingName;
/*     */     }
/*     */     
/*     */     BindingImpl<T> getBinding() {
/* 630 */       return this.binding;
/*     */     }
/*     */ 
/*     */     
/*     */     BindingImpl<T> getDefaultBinding() {
/* 635 */       return this.defaultBinding;
/*     */     }
/*     */     
/*     */     BindingImpl<T> getActualBinding() {
/* 639 */       return this.actualBinding;
/*     */     }
/*     */     
/*     */     ImmutableSet<Dependency<?>> providerDependencies() {
/* 643 */       return this.providerDependencies;
/*     */     }
/*     */     
/*     */     ImmutableSet<Dependency<?>> dependencies() {
/* 647 */       return this.dependencies;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Dependency<?> getDependency() {
/* 658 */       return (Dependency)Iterables.getOnlyElement((Iterable)this.dependencies);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean containsElement(Element element) {
/* 665 */       if (element instanceof ProviderInstanceBinding) {
/*     */         
/* 667 */         Provider<?> providerInstance = ((ProviderInstanceBinding)element).getUserSuppliedProvider();
/* 668 */         if (providerInstance instanceof RealOptionalBinder.RealOptionalBinderProviderWithDependencies) {
/* 669 */           return ((RealOptionalBinder.RealOptionalBinderProviderWithDependencies)providerInstance).bindingSelection
/* 670 */             .equals(this);
/*     */         }
/*     */       } 
/* 673 */       if (element instanceof Binding) {
/* 674 */         Key<?> elementKey = ((Binding)element).getKey();
/*     */         
/* 676 */         return (elementKey.equals(getKeyForActualBinding()) || elementKey
/* 677 */           .equals(getKeyForDefaultBinding()));
/*     */       } 
/* 679 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 684 */       return (o instanceof BindingSelection && ((BindingSelection)o).key.equals(this.key));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 689 */       return this.key.hashCode();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 695 */     return (o instanceof RealOptionalBinder && ((RealOptionalBinder)o).bindingSelection
/* 696 */       .equals(this.bindingSelection));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 701 */     return this.bindingSelection.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class RealOptionalBinderProviderWithDependencies<T, P>
/*     */     extends InternalProviderInstanceBindingImpl.Factory<P>
/*     */   {
/*     */     protected final RealOptionalBinder.BindingSelection<T> bindingSelection;
/*     */ 
/*     */     
/*     */     RealOptionalBinderProviderWithDependencies(RealOptionalBinder.BindingSelection<T> bindingSelection) {
/* 713 */       super(InternalProviderInstanceBindingImpl.InitializationTiming.DELAYED);
/* 714 */       this.bindingSelection = bindingSelection;
/*     */     }
/*     */ 
/*     */     
/*     */     final void initialize(InjectorImpl injector, Errors errors) throws ErrorsException {
/* 719 */       this.bindingSelection.initialize(injector, errors);
/* 720 */       doInitialize();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract void doInitialize();
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 731 */       return (obj != null && 
/* 732 */         getClass() == obj.getClass() && this.bindingSelection
/* 733 */         .equals(((RealOptionalBinderProviderWithDependencies)obj).bindingSelection));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 739 */       return this.bindingSelection.hashCode();
/*     */     }
/*     */   }
/*     */   
/*     */   static class DefaultImpl extends BaseAnnotation implements Default {
/*     */     public DefaultImpl(String value) {
/* 745 */       super((Class)RealOptionalBinder.Default.class, value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class ActualImpl extends BaseAnnotation implements Actual {
/*     */     public ActualImpl(String value) {
/* 751 */       super((Class)RealOptionalBinder.Actual.class, value);
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract class BaseAnnotation implements Serializable, Annotation {
/*     */     private final String value;
/*     */     private final Class<? extends Annotation> clazz;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     BaseAnnotation(Class<? extends Annotation> clazz, String value) {
/* 761 */       this.clazz = (Class<? extends Annotation>)Preconditions.checkNotNull(clazz, "clazz");
/* 762 */       this.value = (String)Preconditions.checkNotNull(value, "value");
/*     */     }
/*     */     
/*     */     public String value() {
/* 766 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 772 */       return 127 * "value".hashCode() ^ this.value.hashCode();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 779 */       if (o instanceof RealOptionalBinder.Actual && this.clazz == RealOptionalBinder.Actual.class) {
/* 780 */         RealOptionalBinder.Actual other = (RealOptionalBinder.Actual)o;
/* 781 */         return this.value.equals(other.value());
/* 782 */       }  if (o instanceof RealOptionalBinder.Default && this.clazz == RealOptionalBinder.Default.class) {
/* 783 */         RealOptionalBinder.Default other = (RealOptionalBinder.Default)o;
/* 784 */         return this.value.equals(other.value());
/*     */       } 
/* 786 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 791 */       String str1 = this.clazz.getName(), str2 = this.value; str2 = this.value.isEmpty() ? "" : (new StringBuilder(8 + String.valueOf(str2).length())).append("(value=").append(str2).append(")").toString(); return (new StringBuilder(1 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("@").append(str1).append(str2).toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<? extends Annotation> annotationType() {
/* 796 */       return this.clazz;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\RealOptionalBinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */