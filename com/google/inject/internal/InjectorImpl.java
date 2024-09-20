/*      */ package com.google.inject.internal;
/*      */ 
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.ImmutableListMultimap;
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.google.common.collect.ListMultimap;
/*      */ import com.google.common.collect.Multimap;
/*      */ import com.google.common.collect.Multimaps;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.google.inject.Binder;
/*      */ import com.google.inject.Binding;
/*      */ import com.google.inject.ConfigurationException;
/*      */ import com.google.inject.ImplementedBy;
/*      */ import com.google.inject.Injector;
/*      */ import com.google.inject.Key;
/*      */ import com.google.inject.MembersInjector;
/*      */ import com.google.inject.Module;
/*      */ import com.google.inject.ProvidedBy;
/*      */ import com.google.inject.Provider;
/*      */ import com.google.inject.Scope;
/*      */ import com.google.inject.Stage;
/*      */ import com.google.inject.TypeLiteral;
/*      */ import com.google.inject.internal.util.SourceProvider;
/*      */ import com.google.inject.spi.BindingTargetVisitor;
/*      */ import com.google.inject.spi.ConvertedConstantBinding;
/*      */ import com.google.inject.spi.Dependency;
/*      */ import com.google.inject.spi.Element;
/*      */ import com.google.inject.spi.HasDependencies;
/*      */ import com.google.inject.spi.InjectionPoint;
/*      */ import com.google.inject.spi.InstanceBinding;
/*      */ import com.google.inject.spi.ProviderBinding;
/*      */ import com.google.inject.spi.TypeConverterBinding;
/*      */ import com.google.inject.util.Providers;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.ParameterizedType;
/*      */ import java.lang.reflect.Type;
/*      */ import java.util.Collections;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import javax.inject.Provider;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ final class InjectorImpl
/*      */   implements Injector, Lookups
/*      */ {
/*   70 */   public static final TypeLiteral<String> STRING_TYPE = TypeLiteral.get(String.class);
/*      */   
/*      */   private final InjectorBindingData bindingData;
/*      */   private final InjectorJitBindingData jitBindingData;
/*      */   final InjectorImpl parent;
/*      */   final InjectorOptions options;
/*      */   
/*      */   static class InjectorOptions
/*      */   {
/*      */     final Stage stage;
/*      */     final boolean jitDisabled;
/*      */     final boolean disableCircularProxies;
/*      */     final boolean atInjectRequired;
/*      */     final boolean exactBindingAnnotationsRequired;
/*      */     
/*      */     InjectorOptions(Stage stage, boolean jitDisabled, boolean disableCircularProxies, boolean atInjectRequired, boolean exactBindingAnnotationsRequired) {
/*   86 */       this.stage = stage;
/*   87 */       this.jitDisabled = jitDisabled;
/*   88 */       this.disableCircularProxies = disableCircularProxies;
/*   89 */       this.atInjectRequired = atInjectRequired;
/*   90 */       this.exactBindingAnnotationsRequired = exactBindingAnnotationsRequired;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*   95 */       return MoreObjects.toStringHelper(getClass())
/*   96 */         .add("stage", this.stage)
/*   97 */         .add("jitDisabled", this.jitDisabled)
/*   98 */         .add("disableCircularProxies", this.disableCircularProxies)
/*   99 */         .add("atInjectRequired", this.atInjectRequired)
/*  100 */         .add("exactBindingAnnotationsRequired", this.exactBindingAnnotationsRequired)
/*  101 */         .toString();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   enum JitLimitation
/*      */   {
/*  108 */     NO_JIT,
/*      */     
/*  110 */     EXISTING_JIT,
/*      */     
/*  112 */     NEW_OR_EXISTING_JIT;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  120 */   Lookups lookups = new DeferredLookups(this);
/*      */ 
/*      */   
/*  123 */   final Set<TypeLiteral<?>> userRequestedMembersInjectorTypes = Sets.newConcurrentHashSet();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final ConstructorInjectorStore constructors;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   MembersInjectorStore membersInjectorStore;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ProvisionListenerCallbackStore provisionListenerStore;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final ThreadLocal<Object[]> localContext;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> List<Binding<T>> findBindingsByType(TypeLiteral<T> type) {
/*  150 */     List<Binding<T>> list = this.bindingData.getIndexedExplicitBindings().get(Preconditions.checkNotNull(type, "type"));
/*  151 */     return Collections.unmodifiableList(list);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> BindingImpl<T> getBinding(Key<T> key) {
/*  157 */     Errors errors = new Errors(Preconditions.checkNotNull(key, "key"));
/*      */     try {
/*  159 */       BindingImpl<T> result = getBindingOrThrow(key, errors, JitLimitation.EXISTING_JIT);
/*  160 */       errors.throwConfigurationExceptionIfErrorsExist();
/*  161 */       return result;
/*  162 */     } catch (ErrorsException e) {
/*      */       
/*  164 */       ConfigurationException exception = new ConfigurationException(errors.merge(e.getErrors()).getMessages());
/*      */ 
/*      */       
/*  167 */       throw exception;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> BindingImpl<T> getExistingBinding(Key<T> key) {
/*  174 */     BindingImpl<T> explicitBinding = this.bindingData.getExplicitBinding((Key<T>)Preconditions.checkNotNull(key, "key"));
/*  175 */     if (explicitBinding != null) {
/*  176 */       return explicitBinding;
/*      */     }
/*  178 */     synchronized (this.jitBindingData.lock()) {
/*      */       
/*  180 */       for (InjectorImpl injector = this; injector != null; injector = injector.parent) {
/*      */         
/*  182 */         BindingImpl<T> jitBinding = (BindingImpl)injector.jitBindingData.getJitBinding(key);
/*  183 */         if (jitBinding != null) {
/*  184 */           return jitBinding;
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  191 */     if (isProvider(key)) {
/*      */       
/*      */       try {
/*      */         
/*  195 */         Key<?> providedKey = getProvidedKey((Key)key, new Errors());
/*  196 */         if (getExistingBinding(providedKey) != null) {
/*  197 */           return getBinding(key);
/*      */         }
/*  199 */       } catch (ErrorsException e) {
/*  200 */         ConfigurationException exception = new ConfigurationException(e.getErrors().getMessages());
/*  201 */         throw exception;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  206 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   <T> BindingImpl<T> getBindingOrThrow(Key<T> key, Errors errors, JitLimitation jitType) throws ErrorsException {
/*  218 */     BindingImpl<T> binding = this.bindingData.getExplicitBinding(key);
/*  219 */     if (binding != null) {
/*  220 */       return binding;
/*      */     }
/*      */ 
/*      */     
/*  224 */     return getJustInTimeBinding(key, errors, jitType);
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> Binding<T> getBinding(Class<T> type) {
/*  229 */     return getBinding(Key.get((Class)Preconditions.checkNotNull(type, "type")));
/*      */   }
/*      */ 
/*      */   
/*      */   public Injector getParent() {
/*  234 */     return this.parent;
/*      */   }
/*      */ 
/*      */   
/*      */   public Injector createChildInjector(Iterable<? extends Module> modules) {
/*  239 */     return (new InternalInjectorCreator()).parentInjector(this).addModules(modules).build();
/*      */   }
/*      */ 
/*      */   
/*      */   public Injector createChildInjector(Module... modules) {
/*  244 */     return createChildInjector((Iterable<? extends Module>)ImmutableList.copyOf((Object[])modules));
/*      */   }
/*      */   
/*      */   InjectorBindingData getBindingData() {
/*  248 */     return this.bindingData;
/*      */   }
/*      */   
/*      */   InjectorJitBindingData getJitBindingData() {
/*  252 */     return this.jitBindingData;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private <T> BindingImpl<T> getJustInTimeBinding(Key<T> key, Errors errors, JitLimitation jitType) throws ErrorsException {
/*  263 */     boolean jitOverride = (isProvider(key) || isTypeLiteral(key) || isMembersInjector(key));
/*  264 */     synchronized (this.jitBindingData.lock()) {
/*      */       
/*  266 */       for (InjectorImpl injector = this; injector != null; injector = injector.parent) {
/*      */         
/*  268 */         BindingImpl<T> binding = (BindingImpl<T>)injector.jitBindingData.getJitBindings().get(key);
/*      */         
/*  270 */         if (binding != null) {
/*      */ 
/*      */           
/*  273 */           if (this.options.jitDisabled && jitType == JitLimitation.NO_JIT && !jitOverride && !(binding instanceof ConvertedConstantBindingImpl))
/*      */           {
/*      */ 
/*      */             
/*  277 */             throw errors.jitDisabled(key).toException();
/*      */           }
/*  279 */           return binding;
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  298 */       if (this.jitBindingData.isFailedJitBinding(key) && errors.hasErrors()) {
/*  299 */         throw errors.toException();
/*      */       }
/*  301 */       return createJustInTimeBindingRecursive(key, errors, this.options.jitDisabled, jitType);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isProvider(Key<?> key) {
/*  307 */     return key.getTypeLiteral().getRawType().equals(Provider.class);
/*      */   }
/*      */   
/*      */   private static boolean isTypeLiteral(Key<?> key) {
/*  311 */     return key.getTypeLiteral().getRawType().equals(TypeLiteral.class);
/*      */   }
/*      */ 
/*      */   
/*      */   private static <T> Key<T> getProvidedKey(Key<Provider<T>> key, Errors errors) throws ErrorsException {
/*  316 */     Type providerType = key.getTypeLiteral().getType();
/*      */ 
/*      */     
/*  319 */     if (!(providerType instanceof ParameterizedType)) {
/*  320 */       throw errors.cannotInjectRawProvider().toException();
/*      */     }
/*      */     
/*  323 */     Type entryType = ((ParameterizedType)providerType).getActualTypeArguments()[0];
/*      */ 
/*      */     
/*  326 */     Key<T> providedKey = key.ofType(entryType);
/*  327 */     return providedKey;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isMembersInjector(Key<?> key) {
/*  332 */     return (key.getTypeLiteral().getRawType().equals(MembersInjector.class) && key
/*  333 */       .getAnnotationType() == null);
/*      */   }
/*      */ 
/*      */   
/*      */   private <T> BindingImpl<MembersInjector<T>> createMembersInjectorBinding(Key<MembersInjector<T>> key, Errors errors) throws ErrorsException {
/*  338 */     Type membersInjectorType = key.getTypeLiteral().getType();
/*  339 */     if (!(membersInjectorType instanceof ParameterizedType)) {
/*  340 */       throw errors.cannotInjectRawMembersInjector().toException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  346 */     TypeLiteral<T> instanceType = TypeLiteral.get(((ParameterizedType)membersInjectorType).getActualTypeArguments()[0]);
/*  347 */     MembersInjector<T> membersInjector = this.membersInjectorStore.get(instanceType, errors);
/*      */ 
/*      */     
/*  350 */     InternalFactory<MembersInjector<T>> factory = new ConstantFactory<>(Initializables.of(membersInjector));
/*      */     
/*  352 */     return new InstanceBindingImpl<>(this, key, SourceProvider.UNKNOWN_SOURCE, factory, 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  357 */         (Set<InjectionPoint>)ImmutableSet.of(), membersInjector);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private <T> BindingImpl<Provider<T>> createSyntheticProviderBinding(Key<Provider<T>> key, Errors errors) throws ErrorsException {
/*  367 */     Key<T> providedKey = getProvidedKey(key, errors);
/*  368 */     BindingImpl<T> delegate = getBindingOrThrow(providedKey, errors, JitLimitation.NO_JIT);
/*  369 */     return new SyntheticProviderBindingImpl<>(this, key, delegate);
/*      */   }
/*      */   
/*      */   private static class SyntheticProviderBindingImpl<T>
/*      */     extends BindingImpl<Provider<T>>
/*      */     implements ProviderBinding<Provider<T>>, HasDependencies
/*      */   {
/*      */     final BindingImpl<T> providedBinding;
/*      */     
/*      */     SyntheticProviderBindingImpl(InjectorImpl injector, Key<Provider<T>> key, Binding<T> providedBinding) {
/*  379 */       super(injector, key, providedBinding
/*      */ 
/*      */           
/*  382 */           .getSource(), 
/*  383 */           createInternalFactory(providedBinding), Scoping.UNSCOPED);
/*      */       
/*  385 */       this.providedBinding = (BindingImpl<T>)providedBinding;
/*      */     }
/*      */     
/*      */     static <T> InternalFactory<Provider<T>> createInternalFactory(Binding<T> providedBinding) {
/*  389 */       final Provider<T> provider = providedBinding.getProvider();
/*  390 */       return (InternalFactory)new InternalFactory<Provider<Provider<T>>>()
/*      */         {
/*      */           public Provider<T> get(InternalContext context, Dependency<?> dependency, boolean linked) {
/*  393 */             return provider;
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public Key<? extends T> getProvidedKey() {
/*  400 */       return this.providedBinding.getKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public <V> V acceptTargetVisitor(BindingTargetVisitor<? super Provider<T>, V> visitor) {
/*  405 */       return (V)visitor.visit(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void applyTo(Binder binder) {
/*  410 */       throw new UnsupportedOperationException("This element represents a synthetic binding.");
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  415 */       return MoreObjects.toStringHelper(ProviderBinding.class)
/*  416 */         .add("key", getKey())
/*  417 */         .add("providedKey", getProvidedKey())
/*  418 */         .toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Dependency<?>> getDependencies() {
/*  423 */       return (Set<Dependency<?>>)ImmutableSet.of(Dependency.get(getProvidedKey()));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/*  428 */       if (obj instanceof SyntheticProviderBindingImpl) {
/*  429 */         SyntheticProviderBindingImpl<?> o = (SyntheticProviderBindingImpl)obj;
/*  430 */         return (getKey().equals(o.getKey()) && 
/*  431 */           getScoping().equals(o.getScoping()) && 
/*  432 */           Objects.equal(this.providedBinding, o.providedBinding));
/*      */       } 
/*  434 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  440 */       return Objects.hashCode(new Object[] { getKey(), getScoping(), this.providedBinding });
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private <T> BindingImpl<T> convertConstantStringBinding(Key<T> key, Errors errors) throws ErrorsException {
/*  453 */     Key<String> stringKey = key.ofType(STRING_TYPE);
/*  454 */     BindingImpl<String> stringBinding = this.bindingData.getExplicitBinding(stringKey);
/*  455 */     if (stringBinding == null || !stringBinding.isConstant()) {
/*  456 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  462 */     String stringValue = (String)((InstanceBinding)stringBinding).getInstance();
/*  463 */     Object source = stringBinding.getSource();
/*      */ 
/*      */     
/*  466 */     TypeLiteral<T> type = key.getTypeLiteral();
/*      */     
/*  468 */     TypeConverterBinding typeConverterBinding = this.bindingData.getConverter(stringValue, type, errors, source);
/*      */     
/*  470 */     if (typeConverterBinding == null)
/*      */     {
/*  472 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  478 */       T converted = (T)typeConverterBinding.getTypeConverter().convert(stringValue, type);
/*      */       
/*  480 */       if (converted == null) {
/*  481 */         throw errors
/*  482 */           .converterReturnedNull(stringValue, source, type, typeConverterBinding)
/*  483 */           .toException();
/*      */       }
/*      */       
/*  486 */       if (!type.getRawType().isInstance(converted)) {
/*  487 */         throw errors
/*  488 */           .conversionTypeError(stringValue, source, type, typeConverterBinding, converted)
/*  489 */           .toException();
/*      */       }
/*      */       
/*  492 */       return new ConvertedConstantBindingImpl<>(this, key, converted, stringBinding, typeConverterBinding);
/*      */     }
/*  494 */     catch (ErrorsException e) {
/*  495 */       throw e;
/*  496 */     } catch (RuntimeException e) {
/*  497 */       throw errors
/*  498 */         .conversionError(stringValue, source, type, typeConverterBinding, e)
/*  499 */         .toException();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static class ConvertedConstantBindingImpl<T>
/*      */     extends BindingImpl<T>
/*      */     implements ConvertedConstantBinding<T>
/*      */   {
/*      */     final T value;
/*      */     
/*      */     final Provider<T> provider;
/*      */     
/*      */     final Binding<String> originalBinding;
/*      */     final TypeConverterBinding typeConverterBinding;
/*      */     
/*      */     ConvertedConstantBindingImpl(InjectorImpl injector, Key<T> key, T value, Binding<String> originalBinding, TypeConverterBinding typeConverterBinding) {
/*  516 */       super(injector, key, originalBinding
/*      */ 
/*      */           
/*  519 */           .getSource(), new ConstantFactory<>(
/*  520 */             Initializables.of(value)), Scoping.UNSCOPED);
/*      */       
/*  522 */       this.value = value;
/*  523 */       this.provider = Providers.of(value);
/*  524 */       this.originalBinding = originalBinding;
/*  525 */       this.typeConverterBinding = typeConverterBinding;
/*      */     }
/*      */ 
/*      */     
/*      */     public Provider<T> getProvider() {
/*  530 */       return this.provider;
/*      */     }
/*      */ 
/*      */     
/*      */     public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> visitor) {
/*  535 */       return (V)visitor.visit(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public T getValue() {
/*  540 */       return this.value;
/*      */     }
/*      */ 
/*      */     
/*      */     public TypeConverterBinding getTypeConverterBinding() {
/*  545 */       return this.typeConverterBinding;
/*      */     }
/*      */ 
/*      */     
/*      */     public Key<String> getSourceKey() {
/*  550 */       return this.originalBinding.getKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Dependency<?>> getDependencies() {
/*  555 */       return (Set<Dependency<?>>)ImmutableSet.of(Dependency.get(getSourceKey()));
/*      */     }
/*      */ 
/*      */     
/*      */     public void applyTo(Binder binder) {
/*  560 */       throw new UnsupportedOperationException("This element represents a synthetic binding.");
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  565 */       return MoreObjects.toStringHelper(ConvertedConstantBinding.class)
/*  566 */         .add("key", getKey())
/*  567 */         .add("sourceKey", getSourceKey())
/*  568 */         .add("value", this.value)
/*  569 */         .toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/*  574 */       if (obj instanceof ConvertedConstantBindingImpl) {
/*  575 */         ConvertedConstantBindingImpl<?> o = (ConvertedConstantBindingImpl)obj;
/*  576 */         return (getKey().equals(o.getKey()) && 
/*  577 */           getScoping().equals(o.getScoping()) && 
/*  578 */           Objects.equal(this.value, o.value));
/*      */       } 
/*  580 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  586 */       return Objects.hashCode(new Object[] { getKey(), getScoping(), this.value });
/*      */     }
/*      */   }
/*      */   
/*      */   <T> void initializeBinding(BindingImpl<T> binding, Errors errors) throws ErrorsException {
/*  591 */     if (binding instanceof DelayedInitialize) {
/*  592 */       ((DelayedInitialize)binding).initialize(this, errors);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   <T> void initializeJitBinding(BindingImpl<T> binding, Errors errors) throws ErrorsException {
/*  600 */     if (binding instanceof DelayedInitialize) {
/*  601 */       Key<T> key = binding.getKey();
/*  602 */       this.jitBindingData.putJitBinding(key, binding);
/*  603 */       boolean successful = false;
/*  604 */       DelayedInitialize delayed = (DelayedInitialize)binding;
/*      */       try {
/*  606 */         delayed.initialize(this, errors);
/*  607 */         successful = true;
/*      */       } finally {
/*  609 */         if (!successful) {
/*      */ 
/*      */ 
/*      */           
/*  613 */           removeFailedJitBinding(binding, null);
/*  614 */           cleanup(binding, new HashSet<>());
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean cleanup(BindingImpl<?> binding, Set<Key<?>> encountered) {
/*  627 */     boolean bindingFailed = false;
/*  628 */     Set<Dependency<?>> deps = getInternalDependencies(binding);
/*  629 */     for (Dependency<?> dep : deps) {
/*  630 */       Key<?> depKey = dep.getKey();
/*  631 */       InjectionPoint ip = dep.getInjectionPoint();
/*  632 */       if (encountered.add(depKey)) {
/*  633 */         BindingImpl<?> depBinding = this.jitBindingData.getJitBinding(depKey);
/*  634 */         if (depBinding != null) {
/*  635 */           boolean failed = cleanup(depBinding, encountered);
/*  636 */           if (depBinding instanceof ConstructorBindingImpl) {
/*  637 */             ConstructorBindingImpl<?> ctorBinding = (ConstructorBindingImpl)depBinding;
/*  638 */             ip = ctorBinding.getInternalConstructor();
/*  639 */             if (!ctorBinding.isInitialized()) {
/*  640 */               failed = true;
/*      */             }
/*      */           } 
/*  643 */           if (failed) {
/*  644 */             removeFailedJitBinding(depBinding, ip);
/*  645 */             bindingFailed = true;
/*      */           }  continue;
/*  647 */         }  if (this.bindingData.getExplicitBinding(depKey) == null)
/*      */         {
/*      */           
/*  650 */           bindingFailed = true;
/*      */         }
/*      */       } 
/*      */     } 
/*  654 */     return bindingFailed;
/*      */   }
/*      */ 
/*      */   
/*      */   private void removeFailedJitBinding(Binding<?> binding, InjectionPoint ip) {
/*  659 */     this.jitBindingData.addFailedJitBinding(binding.getKey());
/*  660 */     this.jitBindingData.removeJitBinding(binding.getKey());
/*  661 */     this.membersInjectorStore.remove(binding.getKey().getTypeLiteral());
/*  662 */     this.provisionListenerStore.remove(binding);
/*  663 */     if (ip != null) {
/*  664 */       this.constructors.remove(ip);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private Set<Dependency<?>> getInternalDependencies(BindingImpl<?> binding) {
/*  670 */     if (binding instanceof ConstructorBindingImpl)
/*  671 */       return ((ConstructorBindingImpl)binding).getInternalDependencies(); 
/*  672 */     if (binding instanceof HasDependencies) {
/*  673 */       return ((HasDependencies)binding).getDependencies();
/*      */     }
/*  675 */     return (Set<Dependency<?>>)ImmutableSet.of();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   <T> BindingImpl<T> createUninitializedBinding(Key<T> key, Scoping scoping, Object source, Errors errors, boolean jitBinding) throws ErrorsException {
/*  686 */     Class<?> rawType = key.getTypeLiteral().getRawType();
/*      */     
/*  688 */     ImplementedBy implementedBy = rawType.<ImplementedBy>getAnnotation(ImplementedBy.class);
/*      */ 
/*      */     
/*  691 */     if (rawType.isArray() || (rawType.isEnum() && implementedBy != null)) {
/*  692 */       throw errors.missingImplementationWithHint(key, this).toException();
/*      */     }
/*      */ 
/*      */     
/*  696 */     if (rawType == TypeLiteral.class) {
/*      */ 
/*      */       
/*  699 */       BindingImpl<T> binding = (BindingImpl)createTypeLiteralBinding((Key)key, errors);
/*  700 */       return binding;
/*      */     } 
/*      */ 
/*      */     
/*  704 */     if (implementedBy != null) {
/*  705 */       Annotations.checkForMisplacedScopeAnnotations(rawType, source, errors);
/*  706 */       return createImplementedByBinding(key, scoping, implementedBy, errors);
/*      */     } 
/*      */ 
/*      */     
/*  710 */     ProvidedBy providedBy = rawType.<ProvidedBy>getAnnotation(ProvidedBy.class);
/*  711 */     if (providedBy != null) {
/*  712 */       Annotations.checkForMisplacedScopeAnnotations(rawType, source, errors);
/*  713 */       return createProvidedByBinding(key, scoping, providedBy, errors);
/*      */     } 
/*      */     
/*  716 */     return ConstructorBindingImpl.create(this, key, null, source, scoping, errors, (jitBinding && this.options.jitDisabled), this.options.atInjectRequired);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private <T> BindingImpl<TypeLiteral<T>> createTypeLiteralBinding(Key<TypeLiteral<T>> key, Errors errors) throws ErrorsException {
/*  733 */     Type typeLiteralType = key.getTypeLiteral().getType();
/*  734 */     if (!(typeLiteralType instanceof ParameterizedType)) {
/*  735 */       throw errors.cannotInjectRawTypeLiteral().toException();
/*      */     }
/*      */     
/*  738 */     ParameterizedType parameterizedType = (ParameterizedType)typeLiteralType;
/*  739 */     Type innerType = parameterizedType.getActualTypeArguments()[0];
/*      */ 
/*      */ 
/*      */     
/*  743 */     if (!(innerType instanceof Class) && !(innerType instanceof java.lang.reflect.GenericArrayType) && !(innerType instanceof ParameterizedType))
/*      */     {
/*      */       
/*  746 */       throw errors.cannotInjectTypeLiteralOf(innerType).toException();
/*      */     }
/*      */ 
/*      */     
/*  750 */     TypeLiteral<T> value = TypeLiteral.get(innerType);
/*      */     
/*  752 */     InternalFactory<TypeLiteral<T>> factory = new ConstantFactory<>(Initializables.of(value));
/*  753 */     return new InstanceBindingImpl<>(this, key, SourceProvider.UNKNOWN_SOURCE, factory, 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  758 */         (Set<InjectionPoint>)ImmutableSet.of(), value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   <T> BindingImpl<T> createProvidedByBinding(Key<T> key, Scoping scoping, ProvidedBy providedBy, Errors errors) throws ErrorsException {
/*  765 */     Class<?> rawType = key.getTypeLiteral().getRawType();
/*  766 */     Class<? extends Provider<?>> providerType = providedBy.value();
/*      */ 
/*      */     
/*  769 */     if (providerType == rawType) {
/*  770 */       throw errors.recursiveProviderType().toException();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  775 */     Key<? extends Provider<T>> providerKey = Key.get(providerType);
/*  776 */     ProvidedByInternalFactory<T> internalFactory = new ProvidedByInternalFactory<>(rawType, providerType, (Key)providerKey);
/*      */     
/*  778 */     Object<?> source = (Object<?>)rawType;
/*      */     
/*  780 */     BindingImpl<T> binding = LinkedProviderBindingImpl.createWithInitializer(this, key, source, 
/*      */ 
/*      */ 
/*      */         
/*  784 */         Scoping.scope(key, this, internalFactory, source, scoping), scoping, (Key)providerKey, internalFactory);
/*      */ 
/*      */ 
/*      */     
/*  788 */     internalFactory.setProvisionListenerCallback(this.provisionListenerStore.get(binding));
/*  789 */     return binding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private <T> BindingImpl<T> createImplementedByBinding(Key<T> key, Scoping scoping, ImplementedBy implementedBy, Errors errors) throws ErrorsException {
/*  796 */     Class<?> rawType = key.getTypeLiteral().getRawType();
/*  797 */     Class<?> implementationType = implementedBy.value();
/*      */ 
/*      */     
/*  800 */     if (implementationType == rawType) {
/*  801 */       throw errors.recursiveImplementationType().toException();
/*      */     }
/*      */ 
/*      */     
/*  805 */     if (!rawType.isAssignableFrom(implementationType)) {
/*  806 */       throw errors.notASubtype(implementationType, rawType).toException();
/*      */     }
/*      */ 
/*      */     
/*  810 */     Class<? extends T> subclass = (Class)implementationType;
/*      */ 
/*      */     
/*  813 */     Key<? extends T> targetKey = Key.get(subclass);
/*  814 */     Object<?> source = (Object<?>)rawType;
/*  815 */     FactoryProxy<T> factory = new FactoryProxy<>(this, key, targetKey, source);
/*  816 */     factory.notify(errors);
/*  817 */     return new LinkedBindingImpl<>(this, key, source, 
/*      */ 
/*      */ 
/*      */         
/*  821 */         Scoping.scope(key, this, factory, source, scoping), scoping, targetKey);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private <T> BindingImpl<T> createJustInTimeBindingRecursive(Key<T> key, Errors errors, boolean jitDisabled, JitLimitation jitType) throws ErrorsException {
/*  834 */     if (this.parent != null) {
/*  835 */       if (jitType == JitLimitation.NEW_OR_EXISTING_JIT && jitDisabled && !this.parent.options.jitDisabled)
/*      */       {
/*      */ 
/*      */         
/*  839 */         throw errors.jitDisabledInParent(key).toException();
/*      */       }
/*      */       
/*      */       try {
/*  843 */         return this.parent.createJustInTimeBindingRecursive(key, new Errors(), jitDisabled, 
/*      */ 
/*      */ 
/*      */             
/*  847 */             this.parent.options.jitDisabled ? JitLimitation.NO_JIT : jitType);
/*  848 */       } catch (ErrorsException errorsException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  857 */     Set<Object> sources = this.jitBindingData.getSourcesForBannedKey(key);
/*  858 */     if (this.jitBindingData.isBannedKey(key)) {
/*  859 */       throw errors.childBindingAlreadySet(key, sources).toException();
/*      */     }
/*      */     
/*  862 */     key = MoreTypes.canonicalizeKey(key);
/*  863 */     BindingImpl<T> binding = createJustInTimeBinding(key, errors, jitDisabled, jitType);
/*  864 */     this.jitBindingData.banKeyInParent(key, this.bindingData, binding.getSource());
/*  865 */     this.jitBindingData.putJitBinding(key, binding);
/*  866 */     return binding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private <T> BindingImpl<T> createJustInTimeBinding(Key<T> key, Errors errors, boolean jitDisabled, JitLimitation jitType) throws ErrorsException {
/*  886 */     int numErrorsBefore = errors.size();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  892 */     Set<Object> sources = this.jitBindingData.getSourcesForBannedKey(key);
/*  893 */     if (this.jitBindingData.isBannedKey(key)) {
/*  894 */       throw errors.childBindingAlreadySet(key, sources).toException();
/*      */     }
/*      */ 
/*      */     
/*  898 */     if (isProvider(key)) {
/*      */ 
/*      */ 
/*      */       
/*  902 */       BindingImpl<T> bindingImpl = (BindingImpl)createSyntheticProviderBinding((Key)key, errors);
/*  903 */       return bindingImpl;
/*      */     } 
/*      */ 
/*      */     
/*  907 */     if (isMembersInjector(key)) {
/*      */ 
/*      */ 
/*      */       
/*  911 */       BindingImpl<T> bindingImpl = (BindingImpl)createMembersInjectorBinding((Key)key, errors);
/*  912 */       return bindingImpl;
/*      */     } 
/*      */ 
/*      */     
/*  916 */     BindingImpl<T> convertedBinding = convertConstantStringBinding(key, errors);
/*  917 */     if (convertedBinding != null) {
/*  918 */       return convertedBinding;
/*      */     }
/*      */     
/*  921 */     if (!isTypeLiteral(key) && jitDisabled && jitType != JitLimitation.NEW_OR_EXISTING_JIT) {
/*  922 */       throw errors.jitDisabled(key).toException();
/*      */     }
/*      */ 
/*      */     
/*  926 */     if (key.getAnnotationType() != null) {
/*      */       
/*  928 */       if (key.hasAttributes() && !this.options.exactBindingAnnotationsRequired) {
/*      */         try {
/*  930 */           Errors ignored = new Errors();
/*  931 */           return getBindingOrThrow(key.withoutAttributes(), ignored, JitLimitation.NO_JIT);
/*  932 */         } catch (ErrorsException errorsException) {}
/*      */       }
/*      */ 
/*      */       
/*  936 */       throw errors.missingImplementationWithHint(key, this).toException();
/*      */     } 
/*      */     
/*  939 */     Object source = key.getTypeLiteral().getRawType();
/*      */     
/*  941 */     BindingImpl<T> binding = createUninitializedBinding(key, Scoping.UNSCOPED, source, errors, true);
/*  942 */     errors.throwIfNewErrors(numErrorsBefore);
/*  943 */     initializeJitBinding(binding, errors);
/*  944 */     return binding;
/*      */   }
/*      */ 
/*      */   
/*      */   <T> InternalFactory<? extends T> getInternalFactory(Key<T> key, Errors errors, JitLimitation jitType) throws ErrorsException {
/*  949 */     return getBindingOrThrow(key, errors, jitType).getInternalFactory();
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<Key<?>, Binding<?>> getBindings() {
/*  954 */     return this.bindingData.getExplicitBindingsThisLevel();
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<Key<?>, Binding<?>> getAllBindings() {
/*  959 */     synchronized (this.jitBindingData.lock()) {
/*  960 */       return (Map<Key<?>, Binding<?>>)(new ImmutableMap.Builder())
/*  961 */         .putAll(this.bindingData.getExplicitBindingsThisLevel())
/*  962 */         .putAll(this.jitBindingData.getJitBindings())
/*  963 */         .build();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<Class<? extends Annotation>, Scope> getScopeBindings() {
/*  969 */     return (Map<Class<? extends Annotation>, Scope>)ImmutableMap.copyOf(this.bindingData.getScopes());
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<TypeConverterBinding> getTypeConverterBindings() {
/*  974 */     return (Set<TypeConverterBinding>)ImmutableSet.copyOf(this.bindingData.getConvertersThisLevel());
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Element> getElements() {
/*  979 */     ImmutableList.Builder<Element> elements = ImmutableList.builder();
/*  980 */     elements.addAll(getAllBindings().values());
/*  981 */     elements.addAll(this.bindingData.getProviderLookupsThisLevel());
/*  982 */     elements.addAll(this.bindingData.getConvertersThisLevel());
/*  983 */     elements.addAll(this.bindingData.getScopeBindingsThisLevel());
/*  984 */     elements.addAll((Iterable)this.bindingData.getTypeListenerBindingsThisLevel());
/*  985 */     elements.addAll((Iterable)this.bindingData.getProvisionListenerBindingsThisLevel());
/*  986 */     elements.addAll((Iterable)this.bindingData.getScannerBindingsThisLevel());
/*  987 */     elements.addAll(this.bindingData.getStaticInjectionRequestsThisLevel());
/*  988 */     elements.addAll(this.bindingData.getMembersInjectorLookupsThisLevel());
/*  989 */     elements.addAll(this.bindingData.getInjectionRequestsThisLevel());
/*      */     
/*  991 */     return (List<Element>)elements.build();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<TypeLiteral<?>, List<InjectionPoint>> getAllMembersInjectorInjectionPoints() {
/* 1005 */     Objects.requireNonNull(this.userRequestedMembersInjectorTypes);
/* 1006 */     return (Map<TypeLiteral<?>, List<InjectionPoint>>)ImmutableListMultimap.copyOf((Multimap)Multimaps.filterKeys((ListMultimap)this.membersInjectorStore.getAllInjectionPoints(), this.userRequestedMembersInjectorTypes::contains)).asMap();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   SingleParameterInjector<?>[] getParametersInjectors(List<Dependency<?>> parameters, Errors errors) throws ErrorsException {
/* 1013 */     if (parameters.isEmpty()) {
/* 1014 */       return null;
/*      */     }
/*      */     
/* 1017 */     int numErrorsBefore = errors.size();
/* 1018 */     SingleParameterInjector[] arrayOfSingleParameterInjector = new SingleParameterInjector[parameters.size()];
/* 1019 */     int i = 0;
/* 1020 */     for (Dependency<?> parameter : parameters) {
/*      */       try {
/* 1022 */         arrayOfSingleParameterInjector[i++] = createParameterInjector(parameter, errors.withSource(parameter));
/* 1023 */       } catch (ErrorsException errorsException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1028 */     errors.throwIfNewErrors(numErrorsBefore);
/* 1029 */     return (SingleParameterInjector<?>[])arrayOfSingleParameterInjector;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   <T> SingleParameterInjector<T> createParameterInjector(Dependency<T> dependency, Errors errors) throws ErrorsException {
/* 1035 */     BindingImpl<? extends T> binding = getBindingOrThrow(dependency.getKey(), errors, JitLimitation.NO_JIT);
/* 1036 */     return new SingleParameterInjector<>(dependency, binding);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   InjectorImpl(InjectorImpl parent, InjectorBindingData bindingData, InjectorJitBindingData jitBindingData, InjectorOptions injectorOptions) {
/* 1046 */     this.constructors = new ConstructorInjectorStore(this);
/*      */     this.parent = parent;
/*      */     this.bindingData = bindingData;
/*      */     this.jitBindingData = jitBindingData;
/*      */     this.options = injectorOptions;
/*      */     if (parent != null) {
/*      */       this.localContext = parent.localContext;
/*      */     } else {
/*      */       this.localContext = new ThreadLocal();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void injectMembers(Object instance) {
/* 1060 */     MembersInjector<?> membersInjector = getMembersInjector(instance.getClass());
/* 1061 */     membersInjector.injectMembers(instance);
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral) {
/* 1066 */     Preconditions.checkNotNull(typeLiteral, "typeLiteral");
/* 1067 */     this.userRequestedMembersInjectorTypes.add(typeLiteral);
/*      */     
/* 1069 */     Errors errors = new Errors(typeLiteral);
/*      */     try {
/* 1071 */       return this.membersInjectorStore.get(typeLiteral, errors);
/* 1072 */     } catch (ErrorsException e) {
/*      */       
/* 1074 */       ConfigurationException exception = new ConfigurationException(errors.merge(e.getErrors()).getMessages());
/* 1075 */       throw exception;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> MembersInjector<T> getMembersInjector(Class<T> type) {
/* 1081 */     return getMembersInjector(TypeLiteral.get(type));
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> Provider<T> getProvider(Class<T> type) {
/* 1086 */     return getProvider(Key.get((Class)Preconditions.checkNotNull(type, "type")));
/*      */   }
/*      */ 
/*      */   
/*      */   <T> Provider<T> getProviderOrThrow(final Dependency<T> dependency, Errors errors) throws ErrorsException {
/* 1091 */     Key<T> key = dependency.getKey();
/* 1092 */     BindingImpl<? extends T> binding = getBindingOrThrow(key, errors, JitLimitation.NO_JIT);
/* 1093 */     final InternalFactory<? extends T> internalFactory = binding.getInternalFactory();
/*      */     
/* 1095 */     return new Provider<T>()
/*      */       {
/*      */         public T get() {
/* 1098 */           InternalContext currentContext = InjectorImpl.this.enterContext();
/*      */           try {
/* 1100 */             T t = internalFactory.get(currentContext, dependency, false);
/* 1101 */             return t;
/* 1102 */           } catch (InternalProvisionException e) {
/* 1103 */             throw e.addSource(dependency).toProvisionException();
/*      */           } finally {
/* 1105 */             currentContext.close();
/*      */           } 
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/* 1111 */           return internalFactory.toString();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> Provider<T> getProvider(Key<T> key) {
/* 1118 */     Preconditions.checkNotNull(key, "key");
/* 1119 */     Errors errors = new Errors(key);
/*      */     try {
/* 1121 */       Provider<T> result = getProviderOrThrow(Dependency.get(key), errors);
/* 1122 */       errors.throwIfNewErrors(0);
/* 1123 */       return result;
/* 1124 */     } catch (ErrorsException e) {
/*      */       
/* 1126 */       ConfigurationException exception = new ConfigurationException(errors.merge(e.getErrors()).getMessages());
/* 1127 */       throw exception;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> T getInstance(Key<T> key) {
/* 1133 */     return (T)getProvider(key).get();
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> T getInstance(Class<T> type) {
/* 1138 */     return (T)getProvider(type).get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   InternalContext getLocalContext() {
/* 1156 */     return (InternalContext)((Object[])this.localContext.get())[0];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   InternalContext enterContext() {
/* 1176 */     Object[] reference = this.localContext.get();
/* 1177 */     if (reference == null) {
/* 1178 */       reference = new Object[1];
/* 1179 */       this.localContext.set(reference);
/*      */     } 
/* 1181 */     InternalContext ctx = (InternalContext)reference[0];
/* 1182 */     if (ctx == null) {
/* 1183 */       reference[0] = ctx = new InternalContext(this.options, reference);
/*      */     } else {
/* 1185 */       ctx.enter();
/*      */     } 
/* 1187 */     return ctx;
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1192 */     return MoreObjects.toStringHelper(Injector.class)
/* 1193 */       .add("bindings", this.bindingData.getExplicitBindingsThisLevel().values())
/* 1194 */       .toString();
/*      */   }
/*      */   
/*      */   static interface MethodInvoker {
/*      */     Object invoke(Object param1Object, Object... param1VarArgs) throws IllegalAccessException, InvocationTargetException;
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\InjectorImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */