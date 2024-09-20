/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.google.inject.Binding;
/*     */ import com.google.inject.Injector;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.MembersInjector;
/*     */ import com.google.inject.Module;
/*     */ import com.google.inject.Provider;
/*     */ import com.google.inject.Scope;
/*     */ import com.google.inject.Stage;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.internal.util.ContinuousStopwatch;
/*     */ import com.google.inject.spi.Dependency;
/*     */ import com.google.inject.spi.Element;
/*     */ import com.google.inject.spi.InjectionPoint;
/*     */ import com.google.inject.spi.TypeConverterBinding;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class InternalInjectorCreator
/*     */ {
/*  63 */   private final ContinuousStopwatch stopwatch = new ContinuousStopwatch(
/*  64 */       Stopwatch.createUnstarted());
/*  65 */   private final Errors errors = new Errors();
/*     */   
/*  67 */   private final Initializer initializer = new Initializer();
/*     */   
/*     */   private final ProcessedBindingData processedBindingData;
/*     */   private final InjectionRequestProcessor injectionRequestProcessor;
/*  71 */   private final InjectorShell.Builder shellBuilder = new InjectorShell.Builder();
/*     */   private List<InjectorShell> shells;
/*     */   
/*     */   public InternalInjectorCreator() {
/*  75 */     this.injectionRequestProcessor = new InjectionRequestProcessor(this.errors, this.initializer);
/*  76 */     this.processedBindingData = new ProcessedBindingData();
/*     */   }
/*     */   
/*     */   public InternalInjectorCreator stage(Stage stage) {
/*  80 */     this.shellBuilder.stage(stage);
/*  81 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InternalInjectorCreator parentInjector(InjectorImpl parent) {
/*  90 */     this.shellBuilder.parent(parent);
/*  91 */     return this;
/*     */   }
/*     */   
/*     */   public InternalInjectorCreator addModules(Iterable<? extends Module> modules) {
/*  95 */     this.shellBuilder.addModules(modules);
/*  96 */     return this;
/*     */   }
/*     */   
/*     */   public Injector build() {
/* 100 */     if (this.shellBuilder == null) {
/* 101 */       throw new AssertionError("Already built, builders are not reusable.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 106 */     synchronized (this.shellBuilder.lock()) {
/* 107 */       this.shells = this.shellBuilder.build(this.initializer, this.processedBindingData, this.stopwatch, this.errors);
/* 108 */       this.stopwatch.resetAndLog("Injector construction");
/*     */       
/* 110 */       initializeStatically();
/*     */     } 
/*     */     
/* 113 */     injectDynamically();
/*     */     
/* 115 */     if (this.shellBuilder.getStage() == Stage.TOOL)
/*     */     {
/*     */       
/* 118 */       return new ToolStageInjector(primaryInjector());
/*     */     }
/* 120 */     return primaryInjector();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void initializeStatically() {
/* 126 */     this.processedBindingData.initializeBindings();
/* 127 */     this.stopwatch.resetAndLog("Binding initialization");
/*     */     
/* 129 */     for (InjectorShell shell : this.shells) {
/* 130 */       shell.getInjector().getBindingData().indexBindingsByType();
/*     */     }
/* 132 */     this.stopwatch.resetAndLog("Binding indexing");
/*     */     
/* 134 */     this.injectionRequestProcessor.process(this.shells);
/* 135 */     this.stopwatch.resetAndLog("Collecting injection requests");
/*     */     
/* 137 */     this.processedBindingData.runCreationListeners(this.errors);
/* 138 */     this.stopwatch.resetAndLog("Binding validation");
/*     */     
/* 140 */     this.injectionRequestProcessor.validate();
/* 141 */     this.stopwatch.resetAndLog("Static validation");
/*     */     
/* 143 */     this.initializer.validateOustandingInjections(this.errors);
/* 144 */     this.stopwatch.resetAndLog("Instance member validation");
/*     */     
/* 146 */     (new LookupProcessor(this.errors)).process(this.shells);
/* 147 */     for (InjectorShell shell : this.shells) {
/* 148 */       ((DeferredLookups)(shell.getInjector()).lookups).initialize(this.errors);
/*     */     }
/* 150 */     this.stopwatch.resetAndLog("Provider verification");
/*     */ 
/*     */ 
/*     */     
/* 154 */     this.processedBindingData.initializeDelayedBindings();
/* 155 */     this.stopwatch.resetAndLog("Delayed Binding initialization");
/*     */     
/* 157 */     for (InjectorShell shell : this.shells) {
/* 158 */       if (!shell.getElements().isEmpty()) {
/* 159 */         String str = String.valueOf(shell.getElements()); throw new AssertionError((new StringBuilder(18 + String.valueOf(str).length())).append("Failed to execute ").append(str).toString());
/*     */       } 
/*     */     } 
/*     */     
/* 163 */     this.errors.throwCreationExceptionIfErrorsExist();
/*     */   }
/*     */ 
/*     */   
/*     */   private Injector primaryInjector() {
/* 168 */     return ((InjectorShell)this.shells.get(0)).getInjector();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void injectDynamically() {
/* 177 */     this.injectionRequestProcessor.injectMembers();
/* 178 */     this.stopwatch.resetAndLog("Static member injection");
/*     */     
/* 180 */     this.initializer.injectAll(this.errors);
/* 181 */     this.stopwatch.resetAndLog("Instance injection");
/* 182 */     this.errors.throwCreationExceptionIfErrorsExist();
/*     */     
/* 184 */     if (this.shellBuilder.getStage() != Stage.TOOL) {
/* 185 */       for (InjectorShell shell : this.shells) {
/* 186 */         loadEagerSingletons(shell.getInjector(), this.shellBuilder.getStage(), this.errors);
/*     */       }
/* 188 */       this.stopwatch.resetAndLog("Preloading singletons");
/*     */     } 
/* 190 */     this.errors.throwCreationExceptionIfErrorsExist();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void loadEagerSingletons(InjectorImpl injector, Stage stage, Errors errors) {
/* 198 */     List<BindingImpl<?>> candidateBindings = new ArrayList<>();
/*     */ 
/*     */     
/* 201 */     Collection<BindingImpl<?>> bindingsAtThisLevel = (Collection)injector.getBindingData().getExplicitBindingsThisLevel().values();
/* 202 */     candidateBindings.addAll(bindingsAtThisLevel);
/* 203 */     synchronized (injector.getJitBindingData().lock()) {
/*     */       
/* 205 */       candidateBindings.addAll(injector.getJitBindingData().getJitBindings().values());
/*     */     } 
/* 207 */     InternalContext context = injector.enterContext();
/*     */     try {
/* 209 */       for (BindingImpl<?> binding : candidateBindings) {
/* 210 */         if (isEagerSingleton(injector, binding, stage)) {
/* 211 */           Dependency<?> dependency = Dependency.get(binding.getKey());
/*     */           try {
/* 213 */             binding.getInternalFactory().get(context, dependency, false);
/* 214 */           } catch (InternalProvisionException e) {
/* 215 */             errors.withSource(dependency).merge(e);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 220 */       context.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isEagerSingleton(InjectorImpl injector, BindingImpl<?> binding, Stage stage) {
/* 225 */     if (binding.getScoping().isEagerSingleton(stage)) {
/* 226 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 232 */     if (binding instanceof LinkedBindingImpl) {
/* 233 */       Key<?> linkedBinding = ((LinkedBindingImpl)binding).getLinkedKey();
/* 234 */       return (binding.getScoping().isNoScope() && 
/* 235 */         isEagerSingleton(injector, injector.getBinding(linkedBinding), stage));
/*     */     } 
/*     */     
/* 238 */     return false;
/*     */   }
/*     */   
/*     */   static class ToolStageInjector
/*     */     implements Injector {
/*     */     private final Injector delegateInjector;
/*     */     
/*     */     ToolStageInjector(Injector delegateInjector) {
/* 246 */       this.delegateInjector = delegateInjector;
/*     */     }
/*     */ 
/*     */     
/*     */     public void injectMembers(Object o) {
/* 251 */       throw new UnsupportedOperationException("Injector.injectMembers(Object) is not supported in Stage.TOOL");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Key<?>, Binding<?>> getBindings() {
/* 257 */       return this.delegateInjector.getBindings();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<Key<?>, Binding<?>> getAllBindings() {
/* 262 */       return this.delegateInjector.getAllBindings();
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Binding<T> getBinding(Key<T> key) {
/* 267 */       return this.delegateInjector.getBinding(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Binding<T> getBinding(Class<T> type) {
/* 272 */       return this.delegateInjector.getBinding(type);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Binding<T> getExistingBinding(Key<T> key) {
/* 277 */       return this.delegateInjector.getExistingBinding(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> List<Binding<T>> findBindingsByType(TypeLiteral<T> type) {
/* 282 */       return this.delegateInjector.findBindingsByType(type);
/*     */     }
/*     */ 
/*     */     
/*     */     public Injector getParent() {
/* 287 */       return this.delegateInjector.getParent();
/*     */     }
/*     */ 
/*     */     
/*     */     public Injector createChildInjector(Iterable<? extends Module> modules) {
/* 292 */       return this.delegateInjector.createChildInjector(modules);
/*     */     }
/*     */ 
/*     */     
/*     */     public Injector createChildInjector(Module... modules) {
/* 297 */       return this.delegateInjector.createChildInjector(modules);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<Class<? extends Annotation>, Scope> getScopeBindings() {
/* 302 */       return this.delegateInjector.getScopeBindings();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<TypeConverterBinding> getTypeConverterBindings() {
/* 307 */       return this.delegateInjector.getTypeConverterBindings();
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Element> getElements() {
/* 312 */       return this.delegateInjector.getElements();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<TypeLiteral<?>, List<InjectionPoint>> getAllMembersInjectorInjectionPoints() {
/* 317 */       return this.delegateInjector.getAllMembersInjectorInjectionPoints();
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Provider<T> getProvider(Key<T> key) {
/* 322 */       throw new UnsupportedOperationException("Injector.getProvider(Key<T>) is not supported in Stage.TOOL");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> Provider<T> getProvider(Class<T> type) {
/* 328 */       throw new UnsupportedOperationException("Injector.getProvider(Class<T>) is not supported in Stage.TOOL");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral) {
/* 334 */       throw new UnsupportedOperationException("Injector.getMembersInjector(TypeLiteral<T>) is not supported in Stage.TOOL");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> MembersInjector<T> getMembersInjector(Class<T> type) {
/* 340 */       throw new UnsupportedOperationException("Injector.getMembersInjector(Class<T>) is not supported in Stage.TOOL");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> T getInstance(Key<T> key) {
/* 346 */       throw new UnsupportedOperationException("Injector.getInstance(Key<T>) is not supported in Stage.TOOL");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> T getInstance(Class<T> type) {
/* 352 */       throw new UnsupportedOperationException("Injector.getInstance(Class<T>) is not supported in Stage.TOOL");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\InternalInjectorCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */