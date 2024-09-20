/*     */ package com.google.inject.spi;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.inject.AbstractModule;
/*     */ import com.google.inject.Binder;
/*     */ import com.google.inject.Binding;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.MembersInjector;
/*     */ import com.google.inject.Module;
/*     */ import com.google.inject.PrivateBinder;
/*     */ import com.google.inject.Provider;
/*     */ import com.google.inject.Scope;
/*     */ import com.google.inject.Stage;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.binder.AnnotatedBindingBuilder;
/*     */ import com.google.inject.binder.AnnotatedConstantBindingBuilder;
/*     */ import com.google.inject.binder.AnnotatedElementBuilder;
/*     */ import com.google.inject.binder.LinkedBindingBuilder;
/*     */ import com.google.inject.internal.AbstractBindingBuilder;
/*     */ import com.google.inject.internal.BindingBuilder;
/*     */ import com.google.inject.internal.ConstantBindingBuilderImpl;
/*     */ import com.google.inject.internal.Errors;
/*     */ import com.google.inject.internal.ExposureBuilder;
/*     */ import com.google.inject.internal.GuiceInternal;
/*     */ import com.google.inject.internal.InternalFlags;
/*     */ import com.google.inject.internal.MoreTypes;
/*     */ import com.google.inject.internal.PrivateElementsImpl;
/*     */ import com.google.inject.internal.ProviderMethod;
/*     */ import com.google.inject.internal.ProviderMethodsModule;
/*     */ import com.google.inject.internal.util.SourceProvider;
/*     */ import com.google.inject.internal.util.StackTraceElements;
/*     */ import com.google.inject.matcher.Matcher;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
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
/*     */ public final class Elements
/*     */ {
/*  76 */   private static final BindingTargetVisitor<Object, Object> GET_INSTANCE_VISITOR = new DefaultBindingTargetVisitor<Object, Object>()
/*     */     {
/*     */       public Object visit(InstanceBinding<?> binding)
/*     */       {
/*  80 */         return binding.getInstance();
/*     */       }
/*     */ 
/*     */       
/*     */       protected Object visitOther(Binding<?> binding) {
/*  85 */         throw new IllegalArgumentException();
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   public static List<Element> getElements(Module... modules) {
/*  91 */     return getElements(Stage.DEVELOPMENT, Arrays.asList(modules));
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<Element> getElements(Stage stage, Module... modules) {
/*  96 */     return getElements(stage, Arrays.asList(modules));
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<Element> getElements(Iterable<? extends Module> modules) {
/* 101 */     return getElements(Stage.DEVELOPMENT, modules);
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<Element> getElements(Stage stage, Iterable<? extends Module> modules) {
/* 106 */     RecordingBinder binder = new RecordingBinder(stage);
/* 107 */     for (Module module : modules) {
/* 108 */       binder.install(module);
/*     */     }
/* 110 */     binder.scanForAnnotatedMethods();
/* 111 */     for (RecordingBinder child : binder.privateBindersForScanning) {
/* 112 */       child.scanForAnnotatedMethods();
/*     */     }
/* 114 */     binder.permitMapConstruction.finish();
/*     */     
/* 116 */     StackTraceElements.clearCache();
/* 117 */     return Collections.unmodifiableList(binder.elements);
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
/*     */   public static Binder withTrustedSource(GuiceInternal guiceInternal, Binder binder, Object source) {
/* 133 */     Preconditions.checkNotNull(guiceInternal);
/* 134 */     if (binder instanceof RecordingBinder) {
/* 135 */       return ((RecordingBinder)binder).withTrustedSource(source);
/*     */     }
/*     */     
/* 138 */     return binder.withSource(source);
/*     */   }
/*     */   
/*     */   private static class ElementsAsModule implements Module {
/*     */     private final Iterable<? extends Element> elements;
/*     */     
/*     */     ElementsAsModule(Iterable<? extends Element> elements) {
/* 145 */       this.elements = elements;
/*     */     }
/*     */ 
/*     */     
/*     */     public void configure(Binder binder) {
/* 150 */       for (Element element : this.elements) {
/* 151 */         element.applyTo(binder);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static Module getModule(Iterable<? extends Element> elements) {
/* 158 */     return new ElementsAsModule(elements);
/*     */   }
/*     */ 
/*     */   
/*     */   static <T> BindingTargetVisitor<T, T> getInstanceVisitor() {
/* 163 */     return (BindingTargetVisitor)GET_INSTANCE_VISITOR;
/*     */   }
/*     */   
/*     */   private static class ModuleInfo {
/*     */     private final ModuleSource moduleSource;
/*     */     private final boolean skipScanning;
/*     */     
/*     */     private ModuleInfo(ModuleSource moduleSource, boolean skipScanning) {
/* 171 */       this.moduleSource = moduleSource;
/* 172 */       this.skipScanning = skipScanning;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class RecordingBinder
/*     */     implements Binder, PrivateBinder
/*     */   {
/*     */     private final Stage stage;
/*     */     
/*     */     private final Map<Module, Elements.ModuleInfo> modules;
/*     */     
/*     */     private final List<Element> elements;
/*     */     
/*     */     private final Object source;
/*     */     private final SourceProvider sourceProvider;
/*     */     private final Set<ModuleAnnotatedMethodScanner> scanners;
/*     */     private final RecordingBinder parent;
/*     */     private final PrivateElementsImpl privateElements;
/*     */     private final List<RecordingBinder> privateBindersForScanning;
/*     */     private final BindingSourceRestriction.PermitMapConstruction permitMapConstruction;
/* 193 */     private ModuleSource moduleSource = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 201 */     private ModuleAnnotatedMethodScanner scannerSource = null;
/*     */     
/* 203 */     private ModuleAnnotatedMethodScanner currentScanner = null;
/*     */     private boolean trustedSource = false;
/*     */     
/*     */     private RecordingBinder(Stage stage) {
/* 207 */       this.stage = stage;
/* 208 */       this.modules = Maps.newLinkedHashMap();
/* 209 */       this.scanners = Sets.newLinkedHashSet();
/* 210 */       this.elements = Lists.newArrayList();
/* 211 */       this.source = null;
/* 212 */       this
/* 213 */         .sourceProvider = SourceProvider.DEFAULT_INSTANCE.plusSkippedClasses(new Class[] { Elements.class, RecordingBinder.class, AbstractModule.class, ConstantBindingBuilderImpl.class, AbstractBindingBuilder.class, BindingBuilder.class });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 220 */       this.parent = null;
/* 221 */       this.privateElements = null;
/* 222 */       this.privateBindersForScanning = Lists.newArrayList();
/* 223 */       this.permitMapConstruction = new BindingSourceRestriction.PermitMapConstruction();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private RecordingBinder(RecordingBinder prototype, Object source, SourceProvider sourceProvider, boolean trustedSource) {
/* 232 */       Preconditions.checkArgument(((source == null)) ^ ((sourceProvider == null)));
/*     */       
/* 234 */       this.stage = prototype.stage;
/* 235 */       this.modules = prototype.modules;
/* 236 */       this.elements = prototype.elements;
/* 237 */       this.scanners = prototype.scanners;
/* 238 */       this.currentScanner = prototype.currentScanner;
/* 239 */       this.source = source;
/* 240 */       this.trustedSource = trustedSource;
/* 241 */       this.moduleSource = prototype.moduleSource;
/* 242 */       this.sourceProvider = sourceProvider;
/* 243 */       this.parent = prototype.parent;
/* 244 */       this.privateElements = prototype.privateElements;
/* 245 */       this.privateBindersForScanning = prototype.privateBindersForScanning;
/* 246 */       this.permitMapConstruction = prototype.permitMapConstruction;
/* 247 */       this.scannerSource = prototype.scannerSource;
/*     */     }
/*     */ 
/*     */     
/*     */     private RecordingBinder(RecordingBinder parent, PrivateElementsImpl privateElements) {
/* 252 */       this.stage = parent.stage;
/* 253 */       this.modules = Maps.newLinkedHashMap();
/* 254 */       this.scanners = Sets.newLinkedHashSet();
/* 255 */       this.currentScanner = parent.currentScanner;
/* 256 */       this.elements = privateElements.getElementsMutable();
/* 257 */       this.source = parent.source;
/* 258 */       this.moduleSource = parent.moduleSource;
/* 259 */       this.sourceProvider = parent.sourceProvider;
/* 260 */       this.parent = parent;
/* 261 */       this.privateElements = privateElements;
/* 262 */       this.privateBindersForScanning = parent.privateBindersForScanning;
/* 263 */       this.permitMapConstruction = parent.permitMapConstruction;
/* 264 */       this.scannerSource = parent.scannerSource;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void bindInterceptor(Matcher<? super Class<?>> classMatcher, Matcher<? super Method> methodMatcher, MethodInterceptor... interceptors) {
/* 272 */       this.elements.add(new InterceptorBinding(
/* 273 */             getElementSource(), classMatcher, methodMatcher, interceptors));
/*     */     }
/*     */ 
/*     */     
/*     */     public void bindScope(Class<? extends Annotation> annotationType, Scope scope) {
/* 278 */       this.elements.add(new ScopeBinding(getElementSource(), annotationType, scope));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void requestInjection(Object instance) {
/* 284 */       Preconditions.checkNotNull(instance, "instance");
/* 285 */       requestInjection(TypeLiteral.get(instance.getClass()), instance);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> void requestInjection(TypeLiteral<T> type, T instance) {
/* 290 */       Preconditions.checkNotNull(instance, "instance");
/* 291 */       this.elements.add(new InjectionRequest<>(
/*     */             
/* 293 */             getElementSource(), MoreTypes.canonicalizeForKey(type), instance));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral) {
/* 300 */       MembersInjectorLookup<T> element = new MembersInjectorLookup<>(getElementSource(), MoreTypes.canonicalizeForKey(typeLiteral));
/* 301 */       this.elements.add(element);
/* 302 */       return element.getMembersInjector();
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> MembersInjector<T> getMembersInjector(Class<T> type) {
/* 307 */       return getMembersInjector(TypeLiteral.get(type));
/*     */     }
/*     */ 
/*     */     
/*     */     public void bindListener(Matcher<? super TypeLiteral<?>> typeMatcher, TypeListener listener) {
/* 312 */       this.elements.add(new TypeListenerBinding(getElementSource(), listener, typeMatcher));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void bindListener(Matcher<? super Binding<?>> bindingMatcher, ProvisionListener... listeners) {
/* 318 */       this.elements.add(new ProvisionListenerBinding(getElementSource(), bindingMatcher, listeners));
/*     */     }
/*     */ 
/*     */     
/*     */     public void requestStaticInjection(Class<?>... types) {
/* 323 */       for (Class<?> type : types) {
/* 324 */         this.elements.add(new StaticInjectionRequest(getElementSource(), type));
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void scanForAnnotatedMethods() {
/* 334 */       Iterable<ModuleAnnotatedMethodScanner> scanners = getAllScanners();
/*     */ 
/*     */       
/* 337 */       for (Map.Entry<Module, Elements.ModuleInfo> entry : (Iterable<Map.Entry<Module, Elements.ModuleInfo>>)Maps.newLinkedHashMap(this.modules).entrySet()) {
/* 338 */         Module module = entry.getKey();
/* 339 */         Elements.ModuleInfo info = entry.getValue();
/* 340 */         if (info.skipScanning) {
/*     */           continue;
/*     */         }
/* 343 */         for (ModuleAnnotatedMethodScanner scanner : scanners) {
/* 344 */           this.currentScanner = scanner;
/* 345 */           this.moduleSource = (entry.getValue()).moduleSource;
/* 346 */           this.permitMapConstruction.restoreCurrentModulePermits(this.moduleSource);
/*     */           try {
/* 348 */             install(ProviderMethodsModule.forModule(module, scanner));
/* 349 */           } catch (RuntimeException e) {
/* 350 */             Collection<Message> messages = Errors.getMessagesFromThrowable(e);
/* 351 */             if (!messages.isEmpty()) {
/* 352 */               this.elements.addAll((Collection)messages); continue;
/*     */             } 
/* 354 */             addError(e);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 359 */       this.moduleSource = null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void install(Module module) {
/* 365 */       if (this.modules.containsKey(module)) {
/*     */         return;
/*     */       }
/*     */       
/* 369 */       boolean customScanner = false;
/* 370 */       Class<?> newModuleClass = null;
/* 371 */       RecordingBinder binder = this;
/*     */       
/* 373 */       if (module instanceof ProviderMethodsModule) {
/* 374 */         ProviderMethodsModule providerMethodsModule = (ProviderMethodsModule)module;
/* 375 */         if (!providerMethodsModule.isScanningBuiltInProvidesMethods()) {
/* 376 */           this.scannerSource = providerMethodsModule.getScanner();
/* 377 */           customScanner = true;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 383 */         Class<?> delegateClass = providerMethodsModule.getDelegateModuleClass();
/* 384 */         if (this.moduleSource == null || 
/* 385 */           !this.moduleSource.getModuleClassName().equals(delegateClass.getName())) {
/* 386 */           newModuleClass = delegateClass;
/*     */         }
/*     */       } else {
/* 389 */         if (moduleScanning()) {
/* 390 */           forbidNestedScannerMethods(module);
/*     */         }
/* 392 */         newModuleClass = module.getClass();
/*     */       } 
/* 394 */       if (newModuleClass != null) {
/* 395 */         this.moduleSource = getModuleSource(newModuleClass);
/* 396 */         this.permitMapConstruction.pushModule(newModuleClass, this.moduleSource);
/*     */       } 
/* 398 */       boolean skipScanning = false;
/* 399 */       if (module instanceof com.google.inject.PrivateModule) {
/* 400 */         binder = (RecordingBinder)binder.newPrivateBinder();
/*     */         
/* 402 */         binder.modules.put(module, new Elements.ModuleInfo(this.moduleSource, false));
/* 403 */         skipScanning = true;
/*     */       } 
/*     */ 
/*     */       
/* 407 */       this.modules.put(module, new Elements.ModuleInfo(this.moduleSource, skipScanning));
/*     */       try {
/* 409 */         module.configure(binder);
/* 410 */       } catch (RuntimeException e) {
/* 411 */         Collection<Message> messages = Errors.getMessagesFromThrowable(e);
/* 412 */         if (!messages.isEmpty()) {
/* 413 */           this.elements.addAll((Collection)messages);
/*     */         } else {
/* 415 */           addError(e);
/*     */         } 
/*     */       } 
/* 418 */       binder.install(ProviderMethodsModule.forModule(module));
/*     */       
/* 420 */       if (newModuleClass != null) {
/* 421 */         this.moduleSource = this.moduleSource.getParent();
/* 422 */         this.permitMapConstruction.popModule();
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 428 */       if (customScanner) {
/* 429 */         this.scannerSource = null;
/*     */       }
/*     */     }
/*     */     
/*     */     private void forbidNestedScannerMethods(Module module) {
/* 434 */       for (ModuleAnnotatedMethodScanner scanner : getAllScanners()) {
/*     */         
/* 436 */         ProviderMethodsModule providerMethodsModule = (ProviderMethodsModule)ProviderMethodsModule.forModule(module, scanner);
/* 437 */         for (ProviderMethod<?> method : (Iterable<ProviderMethod<?>>)providerMethodsModule.getProviderMethods(this)) {
/* 438 */           addError("Scanner %s is installing a module with %s method. Installing modules with custom provides methods from a ModuleAnnotatedMethodScanner is not supported.", new Object[] { this.currentScanner, method
/*     */ 
/*     */                 
/* 441 */                 .getAnnotation().annotationType().getCanonicalName() });
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Iterable<ModuleAnnotatedMethodScanner> getAllScanners() {
/* 453 */       if (this.privateElements == null) {
/* 454 */         return this.scanners;
/*     */       }
/*     */       
/* 457 */       return Iterables.concat(this.scanners, this.parent.getAllScanners());
/*     */     }
/*     */ 
/*     */     
/*     */     public Stage currentStage() {
/* 462 */       return this.stage;
/*     */     }
/*     */ 
/*     */     
/*     */     public void addError(String message, Object... arguments) {
/* 467 */       this.elements.add(new Message(getElementSource(), Errors.format(message, arguments)));
/*     */     }
/*     */ 
/*     */     
/*     */     public void addError(Throwable t) {
/* 472 */       String.valueOf(t.getMessage()); String message = (String.valueOf(t.getMessage()).length() != 0) ? "An exception was caught and reported. Message: ".concat(String.valueOf(t.getMessage())) : new String("An exception was caught and reported. Message: ");
/* 473 */       this.elements.add(new Message((List<Object>)ImmutableList.of(getElementSource()), message, t));
/*     */     }
/*     */ 
/*     */     
/*     */     public void addError(Message message) {
/* 478 */       this.elements.add(message);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> AnnotatedBindingBuilder<T> bind(Key<T> key) {
/* 484 */       BindingBuilder<T> builder = new BindingBuilder(this, this.elements, getElementSource(), MoreTypes.canonicalizeKey(key));
/* 485 */       return (AnnotatedBindingBuilder<T>)builder;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> AnnotatedBindingBuilder<T> bind(TypeLiteral<T> typeLiteral) {
/* 490 */       return bind(Key.get(typeLiteral));
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> AnnotatedBindingBuilder<T> bind(Class<T> type) {
/* 495 */       return bind(Key.get(type));
/*     */     }
/*     */ 
/*     */     
/*     */     public AnnotatedConstantBindingBuilder bindConstant() {
/* 500 */       return (AnnotatedConstantBindingBuilder)new ConstantBindingBuilderImpl(this, this.elements, getElementSource());
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Provider<T> getProvider(Key<T> key) {
/* 505 */       return getProvider(Dependency.get(key));
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Provider<T> getProvider(Dependency<T> dependency) {
/* 510 */       ProviderLookup<T> element = new ProviderLookup<>(getElementSource(), dependency);
/* 511 */       this.elements.add(element);
/* 512 */       return element.getProvider();
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Provider<T> getProvider(Class<T> type) {
/* 517 */       return getProvider(Key.get(type));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void convertToTypes(Matcher<? super TypeLiteral<?>> typeMatcher, TypeConverter converter) {
/* 523 */       this.elements.add(new TypeConverterBinding(getElementSource(), typeMatcher, converter));
/*     */     }
/*     */ 
/*     */     
/*     */     public RecordingBinder withSource(Object source) {
/* 528 */       return (source == this.source) ? 
/* 529 */         this : 
/* 530 */         new RecordingBinder(this, source, null, false);
/*     */     }
/*     */ 
/*     */     
/*     */     public RecordingBinder withTrustedSource(Object source) {
/* 535 */       return (source == this.source) ? 
/* 536 */         this : 
/* 537 */         new RecordingBinder(this, source, null, true);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public RecordingBinder skipSources(Class<?>... classesToSkip) {
/* 544 */       if (this.source != null) {
/* 545 */         return this;
/*     */       }
/*     */       
/* 548 */       SourceProvider newSourceProvider = this.sourceProvider.plusSkippedClasses(classesToSkip);
/* 549 */       return new RecordingBinder(this, null, newSourceProvider, false);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public PrivateBinder newPrivateBinder() {
/* 555 */       PrivateElementsImpl privateElements = new PrivateElementsImpl(getElementSource());
/* 556 */       RecordingBinder binder = new RecordingBinder(this, privateElements);
/* 557 */       this.elements.add(privateElements);
/*     */       
/* 559 */       if (!moduleScanning()) {
/* 560 */         this.privateBindersForScanning.add(binder);
/*     */       }
/* 562 */       return binder;
/*     */     }
/*     */ 
/*     */     
/*     */     public void disableCircularProxies() {
/* 567 */       this.elements.add(new DisableCircularProxiesOption(getElementSource()));
/*     */     }
/*     */ 
/*     */     
/*     */     public void requireExplicitBindings() {
/* 572 */       this.elements.add(new RequireExplicitBindingsOption(getElementSource()));
/*     */     }
/*     */ 
/*     */     
/*     */     public void requireAtInjectOnConstructors() {
/* 577 */       this.elements.add(new RequireAtInjectOnConstructorsOption(getElementSource()));
/*     */     }
/*     */ 
/*     */     
/*     */     public void requireExactBindingAnnotations() {
/* 582 */       this.elements.add(new RequireExactBindingAnnotationsOption(getElementSource()));
/*     */     }
/*     */ 
/*     */     
/*     */     public void scanModulesForAnnotatedMethods(ModuleAnnotatedMethodScanner scanner) {
/* 587 */       if (moduleScanning()) {
/* 588 */         addError("Attempting to register ModuleAnnotatedMethodScanner %s from scanner %s. Scanners are not allowed to register other scanners.", new Object[] { this.currentScanner, scanner });
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 594 */       this.scanners.add(scanner);
/* 595 */       this.elements.add(new ModuleAnnotatedMethodScannerBinding(getElementSource(), scanner));
/*     */     }
/*     */ 
/*     */     
/*     */     public void expose(Key<?> key) {
/* 600 */       exposeInternal(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public AnnotatedElementBuilder expose(Class<?> type) {
/* 605 */       return exposeInternal(Key.get(type));
/*     */     }
/*     */ 
/*     */     
/*     */     public AnnotatedElementBuilder expose(TypeLiteral<?> type) {
/* 610 */       return exposeInternal(Key.get(type));
/*     */     }
/*     */     
/*     */     private <T> AnnotatedElementBuilder exposeInternal(Key<T> key) {
/* 614 */       if (this.privateElements == null) {
/* 615 */         addError("Cannot expose %s on a standard binder. Exposed bindings are only applicable to private binders.", new Object[] { key });
/*     */ 
/*     */ 
/*     */         
/* 619 */         return new AnnotatedElementBuilder(this)
/*     */           {
/*     */             public void annotatedWith(Class<? extends Annotation> annotationType) {}
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             public void annotatedWith(Annotation annotation) {}
/*     */           };
/*     */       } 
/* 629 */       ExposureBuilder<T> builder = new ExposureBuilder(this, getElementSource(), MoreTypes.canonicalizeKey(key));
/* 630 */       this.privateElements.addExposureBuilder(builder);
/* 631 */       return (AnnotatedElementBuilder)builder;
/*     */     }
/*     */     
/*     */     private ModuleSource getModuleSource(Class<?> module) {
/*     */       StackTraceElement[] partialCallStack;
/* 636 */       if (InternalFlags.getIncludeStackTraceOption() == InternalFlags.IncludeStackTraceOption.COMPLETE) {
/* 637 */         partialCallStack = getPartialCallStack((new Throwable()).getStackTrace());
/*     */       } else {
/* 639 */         partialCallStack = new StackTraceElement[0];
/*     */       } 
/* 641 */       if (this.moduleSource == null) {
/* 642 */         return new ModuleSource(module, partialCallStack, this.permitMapConstruction.getPermitMap());
/*     */       }
/* 644 */       return this.moduleSource.createChild(module, partialCallStack);
/*     */     }
/*     */ 
/*     */     
/*     */     private ElementSource getElementSource() {
/* 649 */       StackTraceElement[] callStack = null;
/*     */       
/* 651 */       StackTraceElement[] partialCallStack = new StackTraceElement[0];
/*     */       
/* 653 */       ElementSource originalSource = null;
/*     */       
/* 655 */       Object declaringSource = this.source;
/* 656 */       if (declaringSource instanceof ElementSource) {
/* 657 */         originalSource = (ElementSource)declaringSource;
/* 658 */         declaringSource = originalSource.getDeclaringSource();
/*     */       } 
/* 660 */       InternalFlags.IncludeStackTraceOption stackTraceOption = InternalFlags.getIncludeStackTraceOption();
/* 661 */       if (stackTraceOption == InternalFlags.IncludeStackTraceOption.COMPLETE || (stackTraceOption == InternalFlags.IncludeStackTraceOption.ONLY_FOR_DECLARING_SOURCE && declaringSource == null))
/*     */       {
/*     */         
/* 664 */         callStack = (new Throwable()).getStackTrace();
/*     */       }
/* 666 */       if (stackTraceOption == InternalFlags.IncludeStackTraceOption.COMPLETE) {
/* 667 */         partialCallStack = getPartialCallStack(callStack);
/*     */       }
/* 669 */       if (declaringSource == null)
/*     */       {
/* 671 */         if (stackTraceOption == InternalFlags.IncludeStackTraceOption.COMPLETE || stackTraceOption == InternalFlags.IncludeStackTraceOption.ONLY_FOR_DECLARING_SOURCE) {
/*     */ 
/*     */           
/* 674 */           StackTraceElement callingSource = this.sourceProvider.get(callStack);
/*     */ 
/*     */           
/* 677 */           if (callingSource
/* 678 */             .getClassName()
/* 679 */             .equals("com.google.inject.internal.InjectorShell$Builder") && callingSource
/* 680 */             .getMethodName().equals("build")) {
/* 681 */             declaringSource = SourceProvider.UNKNOWN_SOURCE;
/*     */           } else {
/* 683 */             declaringSource = callingSource;
/*     */           } 
/*     */         } else {
/*     */           
/* 687 */           declaringSource = this.sourceProvider.getFromClassNames(this.moduleSource.getModuleClassNames());
/*     */         } 
/*     */       }
/*     */       
/* 691 */       return new ElementSource(originalSource, this.trustedSource, declaringSource, this.moduleSource, partialCallStack, this.scannerSource);
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
/*     */ 
/*     */ 
/*     */     
/*     */     private StackTraceElement[] getPartialCallStack(StackTraceElement[] callStack) {
/* 706 */       int toSkip = 0;
/* 707 */       if (this.moduleSource != null) {
/* 708 */         toSkip = this.moduleSource.getStackTraceSize();
/*     */       }
/*     */       
/* 711 */       int chunkSize = callStack.length - toSkip - 1;
/*     */       
/* 713 */       StackTraceElement[] partialCallStack = new StackTraceElement[chunkSize];
/* 714 */       System.arraycopy(callStack, 1, partialCallStack, 0, chunkSize);
/* 715 */       return partialCallStack;
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean moduleScanning() {
/* 720 */       return (this.currentScanner != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 725 */       return "Binder";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\Elements.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */