/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.inject.AbstractModule;
/*     */ import com.google.inject.Binder;
/*     */ import com.google.inject.Binding;
/*     */ import com.google.inject.Injector;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.Module;
/*     */ import com.google.inject.Provider;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.binder.LinkedBindingBuilder;
/*     */ import com.google.inject.multibindings.MultibinderBinding;
/*     */ import com.google.inject.multibindings.MultibindingsTargetVisitor;
/*     */ import com.google.inject.name.Names;
/*     */ import com.google.inject.spi.BindingTargetVisitor;
/*     */ import com.google.inject.spi.Dependency;
/*     */ import com.google.inject.spi.Element;
/*     */ import com.google.inject.spi.Message;
/*     */ import com.google.inject.spi.ProviderInstanceBinding;
/*     */ import com.google.inject.spi.ProviderWithExtensionVisitor;
/*     */ import com.google.inject.util.Types;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
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
/*     */ public final class RealMultibinder<T>
/*     */   implements Module
/*     */ {
/*     */   private final BindingSelection<T> bindingSelection;
/*     */   private final Binder binder;
/*     */   
/*     */   public static <T> RealMultibinder<T> newRealSetBinder(Binder binder, Key<T> key) {
/*  55 */     binder = binder.skipSources(new Class[] { RealMultibinder.class });
/*  56 */     RealMultibinder<T> result = new RealMultibinder<>(binder, key);
/*  57 */     binder.install(result);
/*  58 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   static <T> TypeLiteral<Set<T>> setOf(TypeLiteral<T> elementType) {
/*  63 */     Type type = Types.setOf(elementType.getType());
/*  64 */     return TypeLiteral.get(type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> TypeLiteral<Collection<Provider<T>>> collectionOfProvidersOf(TypeLiteral<T> elementType) {
/*  70 */     Type providerType = Types.providerOf(elementType.getType());
/*  71 */     Type type = Types.collectionOf(providerType);
/*  72 */     return TypeLiteral.get(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> TypeLiteral<Collection<Provider<T>>> collectionOfJavaxProvidersOf(TypeLiteral<T> elementType) {
/*  79 */     Type providerType = Types.newParameterizedType(Provider.class, new Type[] { elementType.getType() });
/*  80 */     Type type = Types.collectionOf(providerType);
/*  81 */     return TypeLiteral.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   static <T> TypeLiteral<Set<? extends T>> setOfExtendsOf(TypeLiteral<T> elementType) {
/*  86 */     Type extendsType = Types.subtypeOf(elementType.getType());
/*  87 */     Type setOfExtendsType = Types.setOf(extendsType);
/*  88 */     return TypeLiteral.get(setOfExtendsType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   RealMultibinder(Binder binder, Key<T> key) {
/*  95 */     this.binder = Errors.<Binder>checkNotNull(binder, "binder");
/*  96 */     this.bindingSelection = new BindingSelection<>(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void configure(Binder binder) {
/* 101 */     Errors.checkConfiguration(!this.bindingSelection.isInitialized(), "Multibinder was already initialized", new Object[0]);
/* 102 */     binder
/* 103 */       .bind(this.bindingSelection.getSetKey())
/* 104 */       .toProvider(new RealMultibinderProvider<>(this.bindingSelection));
/* 105 */     Provider<Collection<Provider<T>>> collectionOfProvidersProvider = new RealMultibinderCollectionOfProvidersProvider<>(this.bindingSelection);
/*     */     
/* 107 */     binder
/* 108 */       .bind(this.bindingSelection.getCollectionOfProvidersKey())
/* 109 */       .toProvider(collectionOfProvidersProvider);
/* 110 */     binder.bind(this.bindingSelection.getSetOfExtendsKey()).to(this.bindingSelection.getSetKey());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     Provider<Collection<Provider<T>>> provider1 = collectionOfProvidersProvider;
/*     */     
/* 118 */     binder.bind(this.bindingSelection.getCollectionOfJavaxProvidersKey()).toProvider(provider1);
/*     */   }
/*     */   
/*     */   public void permitDuplicates() {
/* 122 */     this.binder.install((Module)new PermitDuplicatesModule(this.bindingSelection.getPermitDuplicatesKey()));
/*     */   }
/*     */ 
/*     */   
/*     */   Key<T> getKeyForNewItem() {
/* 127 */     Errors.checkConfiguration(!this.bindingSelection.isInitialized(), "Multibinder was already initialized", new Object[0]);
/* 128 */     return Key.get(this.bindingSelection
/* 129 */         .getElementTypeLiteral(), new RealElement(this.bindingSelection
/* 130 */           .getSetName(), Element.Type.MULTIBINDER, ""));
/*     */   }
/*     */   
/*     */   public LinkedBindingBuilder<T> addBinding() {
/* 134 */     return this.binder.bind(getKeyForNewItem());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Key<Set<T>> getSetKey() {
/* 140 */     return this.bindingSelection.getSetKey();
/*     */   }
/*     */   
/*     */   TypeLiteral<T> getElementTypeLiteral() {
/* 144 */     return this.bindingSelection.getElementTypeLiteral();
/*     */   }
/*     */   
/*     */   String getSetName() {
/* 148 */     return this.bindingSelection.getSetName();
/*     */   }
/*     */   
/*     */   boolean permitsDuplicates(Injector injector) {
/* 152 */     return this.bindingSelection.permitsDuplicates(injector);
/*     */   }
/*     */   
/*     */   boolean containsElement(Element element) {
/* 156 */     return this.bindingSelection.containsElement(element);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class RealMultibinderProvider<T>
/*     */     extends InternalProviderInstanceBindingImpl.Factory<Set<T>>
/*     */     implements ProviderWithExtensionVisitor<Set<T>>, MultibinderBinding<Set<T>>
/*     */   {
/*     */     private final RealMultibinder.BindingSelection<T> bindingSelection;
/*     */     
/*     */     private List<Binding<T>> bindings;
/*     */     
/*     */     private SingleParameterInjector<T>[] injectors;
/*     */     
/*     */     private boolean permitDuplicates;
/*     */     
/*     */     RealMultibinderProvider(RealMultibinder.BindingSelection<T> bindingSelection) {
/* 173 */       super(InternalProviderInstanceBindingImpl.InitializationTiming.DELAYED);
/* 174 */       this.bindingSelection = bindingSelection;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Dependency<?>> getDependencies() {
/* 179 */       return (Set<Dependency<?>>)this.bindingSelection.getDependencies();
/*     */     }
/*     */ 
/*     */     
/*     */     void initialize(InjectorImpl injector, Errors errors) throws ErrorsException {
/* 184 */       this.bindingSelection.initialize(injector, errors);
/* 185 */       this.bindings = (List<Binding<T>>)this.bindingSelection.getBindings();
/* 186 */       this.injectors = this.bindingSelection.getParameterInjectors();
/* 187 */       this.permitDuplicates = this.bindingSelection.permitsDuplicates();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected Set<T> doProvision(InternalContext context, Dependency<?> dependency) throws InternalProvisionException {
/* 193 */       SingleParameterInjector<T>[] localInjectors = this.injectors;
/* 194 */       if (localInjectors == null)
/*     */       {
/* 196 */         return (Set<T>)ImmutableSet.of();
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 203 */       T[] values = (T[])new Object[localInjectors.length];
/* 204 */       for (int i = 0; i < localInjectors.length; i++) {
/* 205 */         SingleParameterInjector<T> parameterInjector = localInjectors[i];
/* 206 */         T newValue = parameterInjector.inject(context);
/* 207 */         if (newValue == null) {
/* 208 */           throw newNullEntryException(i);
/*     */         }
/* 210 */         values[i] = newValue;
/*     */       } 
/* 212 */       ImmutableSet<T> set = ImmutableSet.copyOf((Object[])values);
/*     */       
/* 214 */       if (!this.permitDuplicates && set.size() < values.length) {
/* 215 */         throw newDuplicateValuesException(values);
/*     */       }
/* 217 */       return (Set<T>)set;
/*     */     }
/*     */     
/*     */     private InternalProvisionException newNullEntryException(int i) {
/* 221 */       return InternalProvisionException.create(ErrorId.NULL_ELEMENT_IN_SET, "Set injection failed due to null element bound at: %s", new Object[] { ((Binding)this.bindings
/*     */ 
/*     */             
/* 224 */             .get(i)).getSource() });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <B, V> V acceptExtensionVisitor(BindingTargetVisitor<B, V> visitor, ProviderInstanceBinding<? extends B> binding) {
/* 231 */       if (visitor instanceof MultibindingsTargetVisitor) {
/* 232 */         return (V)((MultibindingsTargetVisitor)visitor).visit(this);
/*     */       }
/* 234 */       return (V)visitor.visit(binding);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private InternalProvisionException newDuplicateValuesException(T[] values) {
/* 244 */       Message message = new Message(GuiceInternal.GUICE_INTERNAL, ErrorId.DUPLICATE_ELEMENT, new DuplicateElementError<>(getSetKey(), this.bindings, values, (List)ImmutableList.of(getSource())));
/* 245 */       return new InternalProvisionException(message);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 250 */       return (obj instanceof RealMultibinderProvider && this.bindingSelection
/* 251 */         .equals(((RealMultibinderProvider)obj).bindingSelection));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 256 */       return this.bindingSelection.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public Key<Set<T>> getSetKey() {
/* 261 */       return this.bindingSelection.getSetKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Key<?>> getAlternateSetKeys() {
/* 266 */       return (Set<Key<?>>)ImmutableSet.of(this.bindingSelection
/* 267 */           .getCollectionOfProvidersKey(), this.bindingSelection
/* 268 */           .getCollectionOfJavaxProvidersKey(), this.bindingSelection
/* 269 */           .getSetOfExtendsKey());
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeLiteral<?> getElementTypeLiteral() {
/* 274 */       return this.bindingSelection.getElementTypeLiteral();
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Binding<?>> getElements() {
/* 279 */       return this.bindingSelection.getElements();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean permitsDuplicates() {
/* 284 */       return this.bindingSelection.permitsDuplicates();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsElement(Element element) {
/* 289 */       return this.bindingSelection.containsElement(element);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class BindingSelection<T>
/*     */   {
/* 297 */     private static final ImmutableSet<Dependency<?>> MODULE_DEPENDENCIES = ImmutableSet.of(Dependency.get(Key.get(Injector.class)));
/*     */     
/*     */     private final TypeLiteral<T> elementType;
/*     */     
/*     */     private final Key<Set<T>> setKey;
/*     */     
/*     */     private String setName;
/*     */     
/*     */     private Key<Collection<Provider<T>>> collectionOfProvidersKey;
/*     */     
/*     */     private Key<Collection<Provider<T>>> collectionOfJavaxProvidersKey;
/*     */     
/*     */     private Key<Set<? extends T>> setOfExtendsKey;
/*     */     private Key<Boolean> permitDuplicatesKey;
/*     */     private boolean isInitialized;
/*     */     private ImmutableList<Binding<T>> bindings;
/* 313 */     private ImmutableSet<Dependency<?>> dependencies = MODULE_DEPENDENCIES;
/* 314 */     private ImmutableSet<Dependency<?>> providerDependencies = MODULE_DEPENDENCIES;
/*     */     
/*     */     private boolean permitDuplicates;
/*     */     
/*     */     private SingleParameterInjector<T>[] parameterinjectors;
/*     */ 
/*     */     
/*     */     BindingSelection(Key<T> key) {
/* 322 */       this.setKey = key.ofType(RealMultibinder.setOf(key.getTypeLiteral()));
/* 323 */       this.elementType = key.getTypeLiteral();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void initialize(InjectorImpl injector, Errors errors) throws ErrorsException {
/* 330 */       if (this.isInitialized) {
/*     */         return;
/*     */       }
/* 333 */       List<Binding<T>> bindings = Lists.newArrayList();
/* 334 */       Set<Indexer.IndexedBinding> index = Sets.newHashSet();
/* 335 */       Indexer indexer = new Indexer(injector);
/* 336 */       List<Dependency<?>> dependencies = Lists.newArrayList();
/* 337 */       List<Dependency<?>> providerDependencies = Lists.newArrayList();
/* 338 */       for (Binding<?> entry : injector.<T>findBindingsByType(this.elementType)) {
/* 339 */         if (keyMatches(entry.getKey())) {
/*     */           
/* 341 */           Binding<T> binding = (Binding)entry;
/* 342 */           if (index.add((Indexer.IndexedBinding)binding.acceptTargetVisitor((BindingTargetVisitor)indexer))) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 348 */             bindings.add(binding);
/* 349 */             Key<T> key = binding.getKey();
/*     */ 
/*     */ 
/*     */             
/* 353 */             dependencies.add(Dependency.get(key));
/*     */             
/* 355 */             providerDependencies.add(
/* 356 */                 Dependency.get(key.ofType(Types.providerOf(key.getTypeLiteral().getType()))));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 361 */       this.bindings = ImmutableList.copyOf(bindings);
/* 362 */       this.dependencies = ImmutableSet.copyOf(dependencies);
/* 363 */       this.providerDependencies = ImmutableSet.copyOf(providerDependencies);
/* 364 */       this.permitDuplicates = permitsDuplicates(injector);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 369 */       SingleParameterInjector[] arrayOfSingleParameterInjector = (SingleParameterInjector[])injector.getParametersInjectors(dependencies, errors);
/* 370 */       this.parameterinjectors = (SingleParameterInjector<T>[])arrayOfSingleParameterInjector;
/* 371 */       this.isInitialized = true;
/*     */     }
/*     */     
/*     */     boolean permitsDuplicates(Injector injector) {
/* 375 */       return injector.getBindings().containsKey(getPermitDuplicatesKey());
/*     */     }
/*     */     
/*     */     ImmutableList<Binding<T>> getBindings() {
/* 379 */       Errors.checkConfiguration(this.isInitialized, "not initialized", new Object[0]);
/* 380 */       return this.bindings;
/*     */     }
/*     */     
/*     */     SingleParameterInjector<T>[] getParameterInjectors() {
/* 384 */       Errors.checkConfiguration(this.isInitialized, "not initialized", new Object[0]);
/* 385 */       return this.parameterinjectors;
/*     */     }
/*     */     
/*     */     ImmutableSet<Dependency<?>> getDependencies() {
/* 389 */       return this.dependencies;
/*     */     }
/*     */     
/*     */     ImmutableSet<Dependency<?>> getProviderDependencies() {
/* 393 */       return this.providerDependencies;
/*     */     }
/*     */ 
/*     */     
/*     */     String getSetName() {
/* 398 */       if (this.setName == null) {
/* 399 */         this.setName = Annotations.nameOf(this.setKey);
/*     */       }
/* 401 */       return this.setName;
/*     */     }
/*     */     
/*     */     Key<Boolean> getPermitDuplicatesKey() {
/* 405 */       Key<Boolean> local = this.permitDuplicatesKey;
/* 406 */       if (local == null)
/*     */       {
/* 408 */         local = this.permitDuplicatesKey = Key.get(Boolean.class, (Annotation)Names.named(String.valueOf(toString()).concat(" permits duplicates")));
/*     */       }
/* 410 */       return local;
/*     */     }
/*     */     
/*     */     Key<Collection<Provider<T>>> getCollectionOfProvidersKey() {
/* 414 */       Key<Collection<Provider<T>>> local = this.collectionOfProvidersKey;
/* 415 */       if (local == null) {
/* 416 */         local = this.collectionOfProvidersKey = this.setKey.ofType(RealMultibinder.collectionOfProvidersOf(this.elementType));
/*     */       }
/* 418 */       return local;
/*     */     }
/*     */     
/*     */     Key<Collection<Provider<T>>> getCollectionOfJavaxProvidersKey() {
/* 422 */       Key<Collection<Provider<T>>> local = this.collectionOfJavaxProvidersKey;
/* 423 */       if (local == null)
/*     */       {
/*     */         
/* 426 */         local = this.collectionOfJavaxProvidersKey = this.setKey.ofType(RealMultibinder.collectionOfJavaxProvidersOf(this.elementType));
/*     */       }
/* 428 */       return local;
/*     */     }
/*     */     
/*     */     Key<Set<? extends T>> getSetOfExtendsKey() {
/* 432 */       Key<Set<? extends T>> local = this.setOfExtendsKey;
/* 433 */       if (local == null) {
/* 434 */         local = this.setOfExtendsKey = this.setKey.ofType(RealMultibinder.setOfExtendsOf(this.elementType));
/*     */       }
/* 436 */       return local;
/*     */     }
/*     */     
/*     */     boolean isInitialized() {
/* 440 */       return this.isInitialized;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     TypeLiteral<T> getElementTypeLiteral() {
/* 446 */       return this.elementType;
/*     */     }
/*     */     
/*     */     Key<Set<T>> getSetKey() {
/* 450 */       return this.setKey;
/*     */     }
/*     */ 
/*     */     
/*     */     List<Binding<?>> getElements() {
/* 455 */       if (isInitialized()) {
/* 456 */         return (List<Binding<?>>)this.bindings;
/*     */       }
/* 458 */       throw new UnsupportedOperationException("getElements() not supported for module bindings");
/*     */     }
/*     */ 
/*     */     
/*     */     boolean permitsDuplicates() {
/* 463 */       if (isInitialized()) {
/* 464 */         return this.permitDuplicates;
/*     */       }
/* 466 */       throw new UnsupportedOperationException("permitsDuplicates() not supported for module bindings");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     boolean containsElement(Element element) {
/* 472 */       if (element instanceof Binding) {
/* 473 */         Binding<?> binding = (Binding)element;
/* 474 */         return (keyMatches(binding.getKey()) || binding
/* 475 */           .getKey().equals(getPermitDuplicatesKey()) || binding
/* 476 */           .getKey().equals(this.setKey) || binding
/* 477 */           .getKey().equals(this.collectionOfProvidersKey) || binding
/* 478 */           .getKey().equals(this.collectionOfJavaxProvidersKey) || binding
/* 479 */           .getKey().equals(this.setOfExtendsKey));
/*     */       } 
/* 481 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean keyMatches(Key<?> key) {
/* 486 */       return (key.getTypeLiteral().equals(this.elementType) && key
/* 487 */         .getAnnotation() instanceof Element && ((Element)key
/* 488 */         .getAnnotation()).setName().equals(getSetName()) && ((Element)key
/* 489 */         .getAnnotation()).type() == Element.Type.MULTIBINDER);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 494 */       if (obj instanceof BindingSelection) {
/* 495 */         return this.setKey.equals(((BindingSelection)obj).setKey);
/*     */       }
/* 497 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 502 */       return this.setKey.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 507 */       String str1 = getSetName().isEmpty() ? "" : String.valueOf(getSetName()).concat(" "), str2 = String.valueOf(this.elementType); return (new StringBuilder(13 + String.valueOf(str1).length() + String.valueOf(str2).length())).append(str1).append("Multibinder<").append(str2).append(">").toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 516 */     return (o instanceof RealMultibinder && ((RealMultibinder)o).bindingSelection
/* 517 */       .equals(this.bindingSelection));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 522 */     return this.bindingSelection.hashCode();
/*     */   }
/*     */   
/*     */   private static final class RealMultibinderCollectionOfProvidersProvider<T>
/*     */     extends InternalProviderInstanceBindingImpl.Factory<Collection<Provider<T>>>
/*     */   {
/*     */     private final RealMultibinder.BindingSelection<T> bindingSelection;
/*     */     private ImmutableList<Provider<T>> collectionOfProviders;
/*     */     
/*     */     RealMultibinderCollectionOfProvidersProvider(RealMultibinder.BindingSelection<T> bindingSelection) {
/* 532 */       super(InternalProviderInstanceBindingImpl.InitializationTiming.DELAYED);
/* 533 */       this.bindingSelection = bindingSelection;
/*     */     }
/*     */ 
/*     */     
/*     */     void initialize(InjectorImpl injector, Errors errors) throws ErrorsException {
/* 538 */       this.bindingSelection.initialize(injector, errors);
/* 539 */       ImmutableList.Builder<Provider<T>> providers = ImmutableList.builder();
/* 540 */       for (UnmodifiableIterator<Binding<T>> unmodifiableIterator = this.bindingSelection.getBindings().iterator(); unmodifiableIterator.hasNext(); ) { Binding<T> binding = unmodifiableIterator.next();
/* 541 */         providers.add(binding.getProvider()); }
/*     */       
/* 543 */       this.collectionOfProviders = providers.build();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected Collection<Provider<T>> doProvision(InternalContext context, Dependency<?> dependency) {
/* 549 */       return (Collection<Provider<T>>)this.collectionOfProviders;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Dependency<?>> getDependencies() {
/* 554 */       return (Set<Dependency<?>>)this.bindingSelection.getProviderDependencies();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 559 */       return (obj instanceof RealMultibinderCollectionOfProvidersProvider && this.bindingSelection
/* 560 */         .equals(((RealMultibinderCollectionOfProvidersProvider)obj).bindingSelection));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 566 */       return this.bindingSelection.hashCode();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class PermitDuplicatesModule
/*     */     extends AbstractModule
/*     */   {
/*     */     private final Key<Boolean> key;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     PermitDuplicatesModule(Key<Boolean> key) {
/* 582 */       this.key = key;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void configure() {
/* 587 */       bind(this.key).toInstance(Boolean.valueOf(true));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 592 */       return (o instanceof PermitDuplicatesModule && ((PermitDuplicatesModule)o).key.equals(this.key));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 597 */       return getClass().hashCode() ^ this.key.hashCode();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\RealMultibinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */