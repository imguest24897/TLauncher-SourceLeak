/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.inject.Binder;
/*     */ import com.google.inject.Injector;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.Module;
/*     */ import com.google.inject.Provider;
/*     */ import com.google.inject.Scopes;
/*     */ import com.google.inject.Singleton;
/*     */ import com.google.inject.Stage;
/*     */ import com.google.inject.internal.util.ContinuousStopwatch;
/*     */ import com.google.inject.internal.util.SourceProvider;
/*     */ import com.google.inject.spi.BindingSourceRestriction;
/*     */ import com.google.inject.spi.Dependency;
/*     */ import com.google.inject.spi.Element;
/*     */ import com.google.inject.spi.Elements;
/*     */ import com.google.inject.spi.InjectionPoint;
/*     */ import com.google.inject.spi.ModuleAnnotatedMethodScannerBinding;
/*     */ import com.google.inject.spi.PrivateElements;
/*     */ import com.google.inject.spi.ProvisionListenerBinding;
/*     */ import com.google.inject.spi.TypeListenerBinding;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Logger;
/*     */ import javax.inject.Provider;
/*     */ import javax.inject.Singleton;
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
/*     */ 
/*     */ final class InjectorShell
/*     */ {
/*     */   private final List<Element> elements;
/*     */   private final InjectorImpl injector;
/*     */   
/*     */   private InjectorShell(List<Element> elements, InjectorImpl injector) {
/*  69 */     this.elements = elements;
/*  70 */     this.injector = injector;
/*     */   }
/*     */   
/*     */   InjectorImpl getInjector() {
/*  74 */     return this.injector;
/*     */   }
/*     */   
/*     */   List<Element> getElements() {
/*  78 */     return this.elements;
/*     */   }
/*     */   
/*     */   static class Builder {
/*  82 */     private final List<Element> elements = Lists.newArrayList();
/*  83 */     private final List<Module> modules = Lists.newArrayList();
/*     */     
/*     */     private InjectorBindingData bindingData;
/*     */     
/*     */     private InjectorJitBindingData jitBindingData;
/*     */     
/*     */     private InjectorImpl parent;
/*     */     
/*     */     private InjectorImpl.InjectorOptions options;
/*     */     
/*     */     private Stage stage;
/*     */     private PrivateElementsImpl privateElements;
/*     */     
/*     */     Builder stage(Stage stage) {
/*  97 */       this.stage = stage;
/*  98 */       return this;
/*     */     }
/*     */     
/*     */     Builder parent(InjectorImpl parent) {
/* 102 */       this.parent = parent;
/* 103 */       this.jitBindingData = new InjectorJitBindingData(Optional.of(parent.getJitBindingData()));
/* 104 */       this.bindingData = new InjectorBindingData(Optional.of(parent.getBindingData()));
/* 105 */       this.options = parent.options;
/* 106 */       this.stage = this.options.stage;
/* 107 */       return this;
/*     */     }
/*     */     
/*     */     Builder privateElements(PrivateElements privateElements) {
/* 111 */       this.privateElements = (PrivateElementsImpl)privateElements;
/* 112 */       this.elements.addAll(privateElements.getElements());
/* 113 */       return this;
/*     */     }
/*     */     
/*     */     void addModules(Iterable<? extends Module> modules) {
/* 117 */       for (Module module : modules) {
/* 118 */         this.modules.add(module);
/*     */       }
/*     */     }
/*     */     
/*     */     Stage getStage() {
/* 123 */       return this.options.stage;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Object lock() {
/* 130 */       if (this.bindingData == null) {
/* 131 */         this.jitBindingData = new InjectorJitBindingData(Optional.empty());
/* 132 */         this.bindingData = new InjectorBindingData(Optional.empty());
/*     */       } 
/* 134 */       return this.jitBindingData.lock();
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
/*     */     List<InjectorShell> build(Initializer initializer, ProcessedBindingData processedBindingData, ContinuousStopwatch stopwatch, Errors errors) {
/* 147 */       Preconditions.checkState((this.stage != null), "Stage not initialized");
/* 148 */       Preconditions.checkState((this.privateElements == null || this.parent != null), "PrivateElements with no parent");
/* 149 */       Preconditions.checkState((this.bindingData != null), "no binding data. Did you remember to lock() ?");
/* 150 */       Preconditions.checkState(((this.privateElements == null && this.elements
/* 151 */           .isEmpty()) || this.modules.isEmpty()), "The shell is either built from modules (root) or from PrivateElements (children).");
/*     */ 
/*     */ 
/*     */       
/* 155 */       if (this.parent == null) {
/* 156 */         this.modules.add(0, new InjectorShell.RootModule());
/*     */       } else {
/* 158 */         this.modules.add(0, new InjectorShell.InheritedScannersModule(this.parent.getBindingData()));
/*     */       } 
/* 160 */       this.elements.addAll(Elements.getElements(this.stage, this.modules));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 166 */       if (this.privateElements == null) {
/* 167 */         this.elements.addAll((Collection<? extends Element>)BindingSourceRestriction.check(GuiceInternal.GUICE_INTERNAL, this.elements));
/*     */       }
/*     */ 
/*     */       
/* 171 */       InjectorOptionsProcessor optionsProcessor = new InjectorOptionsProcessor(errors);
/* 172 */       optionsProcessor.process(null, this.elements);
/* 173 */       this.options = optionsProcessor.getOptions(this.stage, this.options);
/*     */       
/* 175 */       InjectorImpl injector = new InjectorImpl(this.parent, this.bindingData, this.jitBindingData, this.options);
/* 176 */       if (this.privateElements != null) {
/* 177 */         this.privateElements.initInjector(injector);
/*     */       }
/*     */ 
/*     */       
/* 181 */       if (this.parent == null) {
/* 182 */         TypeConverterBindingProcessor.prepareBuiltInConverters(injector);
/*     */       }
/*     */       
/* 185 */       stopwatch.resetAndLog("Module execution");
/*     */       
/* 187 */       (new MessageProcessor(errors)).process(injector, this.elements);
/*     */       
/* 189 */       (new InterceptorBindingProcessor(errors)).process(injector, this.elements);
/* 190 */       stopwatch.resetAndLog("Interceptors creation");
/*     */       
/* 192 */       (new ListenerBindingProcessor(errors)).process(injector, this.elements);
/*     */       
/* 194 */       ImmutableList<TypeListenerBinding> immutableList = injector.getBindingData().getTypeListenerBindings();
/* 195 */       injector.membersInjectorStore = new MembersInjectorStore(injector, (List<TypeListenerBinding>)immutableList);
/*     */       
/* 197 */       ImmutableList<ProvisionListenerBinding> immutableList1 = injector.getBindingData().getProvisionListenerBindings();
/* 198 */       injector.provisionListenerStore = new ProvisionListenerCallbackStore((List<ProvisionListenerBinding>)immutableList1);
/*     */       
/* 200 */       stopwatch.resetAndLog("TypeListeners & ProvisionListener creation");
/*     */       
/* 202 */       (new ScopeBindingProcessor(errors)).process(injector, this.elements);
/* 203 */       stopwatch.resetAndLog("Scopes creation");
/*     */       
/* 205 */       (new TypeConverterBindingProcessor(errors)).process(injector, this.elements);
/* 206 */       stopwatch.resetAndLog("Converters creation");
/*     */       
/* 208 */       InjectorShell.bindStage(injector, this.stage);
/* 209 */       InjectorShell.bindInjector(injector);
/* 210 */       InjectorShell.bindLogger(injector);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 215 */       (new BindingProcessor(errors, initializer, processedBindingData)).process(injector, this.elements);
/* 216 */       (new UntargettedBindingProcessor(errors, processedBindingData)).process(injector, this.elements);
/* 217 */       stopwatch.resetAndLog("Binding creation");
/*     */       
/* 219 */       (new ModuleAnnotatedMethodScannerProcessor(errors)).process(injector, this.elements);
/* 220 */       stopwatch.resetAndLog("Module annotated method scanners creation");
/*     */       
/* 222 */       List<InjectorShell> injectorShells = Lists.newArrayList();
/* 223 */       injectorShells.add(new InjectorShell(this.elements, injector));
/*     */ 
/*     */       
/* 226 */       PrivateElementProcessor processor = new PrivateElementProcessor(errors);
/* 227 */       processor.process(injector, this.elements);
/* 228 */       for (Builder builder : processor.getInjectorShellBuilders()) {
/* 229 */         injectorShells.addAll(builder.build(initializer, processedBindingData, stopwatch, errors));
/*     */       }
/* 231 */       stopwatch.resetAndLog("Private environment creation");
/*     */       
/* 233 */       return injectorShells;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void bindInjector(InjectorImpl injector) {
/* 243 */     Key<Injector> key = Key.get(Injector.class);
/* 244 */     InjectorFactory injectorFactory = new InjectorFactory(injector);
/* 245 */     injector
/* 246 */       .getBindingData()
/* 247 */       .putBinding(key, new ProviderInstanceBindingImpl(injector, key, SourceProvider.UNKNOWN_SOURCE, injectorFactory, Scoping.UNSCOPED, (Provider<?>)injectorFactory, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 256 */           (Set<InjectionPoint>)ImmutableSet.of()));
/*     */   }
/*     */   
/*     */   private static class InjectorFactory implements InternalFactory<Injector>, Provider<Injector> {
/*     */     private final Injector injector;
/*     */     
/*     */     private InjectorFactory(Injector injector) {
/* 263 */       this.injector = injector;
/*     */     }
/*     */ 
/*     */     
/*     */     public Injector get(InternalContext context, Dependency<?> dependency, boolean linked) {
/* 268 */       return this.injector;
/*     */     }
/*     */ 
/*     */     
/*     */     public Injector get() {
/* 273 */       return this.injector;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 278 */       return "Provider<Injector>";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void bindLogger(InjectorImpl injector) {
/* 287 */     Key<Logger> key = Key.get(Logger.class);
/* 288 */     LoggerFactory loggerFactory = new LoggerFactory();
/* 289 */     injector
/* 290 */       .getBindingData()
/* 291 */       .putBinding(key, new ProviderInstanceBindingImpl(injector, key, SourceProvider.UNKNOWN_SOURCE, loggerFactory, Scoping.UNSCOPED, (Provider<?>)loggerFactory, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 300 */           (Set<InjectionPoint>)ImmutableSet.of()));
/*     */   }
/*     */   
/*     */   private static class LoggerFactory
/*     */     implements InternalFactory<Logger>, Provider<Logger> {
/*     */     public Logger get(InternalContext context, Dependency<?> dependency, boolean linked) {
/* 306 */       InjectionPoint injectionPoint = dependency.getInjectionPoint();
/* 307 */       return (injectionPoint == null) ? 
/* 308 */         Logger.getAnonymousLogger() : 
/* 309 */         Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
/*     */     }
/*     */     private LoggerFactory() {}
/*     */     
/*     */     public Logger get() {
/* 314 */       return Logger.getAnonymousLogger();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 319 */       return "Provider<Logger>";
/*     */     }
/*     */   }
/*     */   
/*     */   private static void bindStage(InjectorImpl injector, Stage stage) {
/* 324 */     Key<Stage> key = Key.get(Stage.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 331 */     InstanceBindingImpl<Stage> stageBinding = new InstanceBindingImpl<>(injector, key, SourceProvider.UNKNOWN_SOURCE, new ConstantFactory<>(Initializables.of(stage)), (Set<InjectionPoint>)ImmutableSet.of(), stage);
/*     */     
/* 333 */     injector.getBindingData().putBinding(key, stageBinding);
/*     */   }
/*     */   
/*     */   private static class RootModule implements Module { private RootModule() {}
/*     */     
/*     */     public void configure(Binder binder) {
/* 339 */       binder = binder.withSource(SourceProvider.UNKNOWN_SOURCE);
/* 340 */       binder.bindScope(Singleton.class, Scopes.SINGLETON);
/* 341 */       binder.bindScope(Singleton.class, Scopes.SINGLETON);
/*     */     } }
/*     */ 
/*     */   
/*     */   private static class InheritedScannersModule implements Module {
/*     */     private final InjectorBindingData bindingData;
/*     */     
/*     */     InheritedScannersModule(InjectorBindingData bindingData) {
/* 349 */       this.bindingData = bindingData;
/*     */     }
/*     */ 
/*     */     
/*     */     public void configure(Binder binder) {
/* 354 */       for (UnmodifiableIterator<ModuleAnnotatedMethodScannerBinding> unmodifiableIterator = this.bindingData.getScannerBindings().iterator(); unmodifiableIterator.hasNext(); ) { ModuleAnnotatedMethodScannerBinding binding = unmodifiableIterator.next();
/* 355 */         binding.applyTo(binder); }
/*     */     
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\InjectorShell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */