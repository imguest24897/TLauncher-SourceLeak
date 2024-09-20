/*     */ package com.google.inject.util;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.inject.AbstractModule;
/*     */ import com.google.inject.Binder;
/*     */ import com.google.inject.Binding;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.Module;
/*     */ import com.google.inject.PrivateBinder;
/*     */ import com.google.inject.Scope;
/*     */ import com.google.inject.internal.Errors;
/*     */ import com.google.inject.spi.BindingScopingVisitor;
/*     */ import com.google.inject.spi.DefaultBindingScopingVisitor;
/*     */ import com.google.inject.spi.DefaultElementVisitor;
/*     */ import com.google.inject.spi.Element;
/*     */ import com.google.inject.spi.ElementVisitor;
/*     */ import com.google.inject.spi.Elements;
/*     */ import com.google.inject.spi.ModuleAnnotatedMethodScannerBinding;
/*     */ import com.google.inject.spi.PrivateElements;
/*     */ import com.google.inject.spi.ScopeBinding;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashSet;
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
/*     */ public final class Modules
/*     */ {
/*  58 */   public static final Module EMPTY_MODULE = new EmptyModule();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class EmptyModule
/*     */     implements Module
/*     */   {
/*     */     private EmptyModule() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void configure(Binder binder) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static OverriddenModuleBuilder override(Module... modules) {
/*  83 */     return override(Arrays.asList(modules));
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static OverriddenModuleBuilder override() {
/*  89 */     return override(Arrays.asList(new Module[0]));
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
/*     */ 
/*     */   
/*     */   public static OverriddenModuleBuilder override(Iterable<? extends Module> modules) {
/* 110 */     return new RealOverriddenModuleBuilder(modules);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Module combine(Module... modules) {
/* 121 */     return combine((Iterable<? extends Module>)ImmutableSet.copyOf((Object[])modules));
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static Module combine(Module module) {
/* 127 */     return module;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static Module combine() {
/* 133 */     return EMPTY_MODULE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Module combine(Iterable<? extends Module> modules) {
/* 144 */     return new CombinedModule(modules);
/*     */   }
/*     */   
/*     */   private static class CombinedModule implements Module {
/*     */     final Set<Module> modulesSet;
/*     */     
/*     */     CombinedModule(Iterable<? extends Module> modules) {
/* 151 */       this.modulesSet = (Set<Module>)ImmutableSet.copyOf(modules);
/*     */     }
/*     */ 
/*     */     
/*     */     public void configure(Binder binder) {
/* 156 */       binder = binder.skipSources(new Class[] { getClass() });
/* 157 */       for (Module module : this.modulesSet) {
/* 158 */         binder.install(module);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class RealOverriddenModuleBuilder
/*     */     implements OverriddenModuleBuilder
/*     */   {
/*     */     private final ImmutableSet<Module> baseModules;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private RealOverriddenModuleBuilder(Iterable<? extends Module> baseModules) {
/* 182 */       this.baseModules = ImmutableSet.copyOf(baseModules);
/*     */     }
/*     */ 
/*     */     
/*     */     public Module with(Module... overrides) {
/* 187 */       return with(Arrays.asList(overrides));
/*     */     }
/*     */ 
/*     */     
/*     */     public Module with() {
/* 192 */       return with(Arrays.asList(new Module[0]));
/*     */     }
/*     */ 
/*     */     
/*     */     public Module with(Iterable<? extends Module> overrides) {
/* 197 */       return (Module)new Modules.OverrideModule(overrides, this.baseModules);
/*     */     }
/*     */   }
/*     */   
/*     */   static class OverrideModule
/*     */     extends AbstractModule {
/*     */     private final ImmutableSet<Module> overrides;
/*     */     private final ImmutableSet<Module> baseModules;
/*     */     
/*     */     OverrideModule(Iterable<? extends Module> overrides, ImmutableSet<Module> baseModules) {
/* 207 */       this.overrides = ImmutableSet.copyOf(overrides);
/* 208 */       this.baseModules = baseModules;
/*     */     }
/*     */     
/*     */     public void configure() {
/*     */       PrivateBinder privateBinder;
/* 213 */       Binder baseBinder = binder();
/* 214 */       List<Element> baseElements = Elements.getElements(currentStage(), (Iterable)this.baseModules);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 219 */       if (baseElements.size() == 1) {
/* 220 */         Element element = (Element)Iterables.getOnlyElement(baseElements);
/* 221 */         if (element instanceof PrivateElements) {
/* 222 */           PrivateElements privateElements = (PrivateElements)element;
/*     */           
/* 224 */           PrivateBinder privateBinder1 = baseBinder.newPrivateBinder().withSource(privateElements.getSource());
/* 225 */           for (Key<?> exposed : (Iterable<Key<?>>)privateElements.getExposedKeys()) {
/* 226 */             privateBinder1.withSource(privateElements.getExposedSource(exposed)).expose(exposed);
/*     */           }
/* 228 */           privateBinder = privateBinder1;
/* 229 */           baseElements = privateElements.getElements();
/*     */         } 
/*     */       } 
/*     */       
/* 233 */       Binder binder = privateBinder.skipSources(new Class[] { getClass() });
/* 234 */       LinkedHashSet<Element> elements = new LinkedHashSet<>(baseElements);
/* 235 */       Module scannersModule = Modules.extractScanners(elements);
/*     */       
/* 237 */       List<Element> overrideElements = Elements.getElements(
/* 238 */           currentStage(), 
/* 239 */           (Iterable)ImmutableList.builder().addAll((Iterable)this.overrides).add(scannersModule).build());
/*     */       
/* 241 */       final Set<Key<?>> overriddenKeys = Sets.newHashSet();
/*     */       
/* 243 */       final Map<Class<? extends Annotation>, ScopeBinding> overridesScopeAnnotations = Maps.newHashMap();
/*     */ 
/*     */       
/* 246 */       (new Modules.ModuleWriter(this, binder)
/*     */         {
/*     */           public <T> Void visit(Binding<T> binding) {
/* 249 */             overriddenKeys.add(binding.getKey());
/* 250 */             return (Void)super.visit(binding);
/*     */           }
/*     */ 
/*     */           
/*     */           public Void visit(ScopeBinding scopeBinding) {
/* 255 */             overridesScopeAnnotations.put(scopeBinding.getAnnotationType(), scopeBinding);
/* 256 */             return (Void)super.visit(scopeBinding);
/*     */           }
/*     */ 
/*     */           
/*     */           public Void visit(PrivateElements privateElements) {
/* 261 */             overriddenKeys.addAll(privateElements.getExposedKeys());
/* 262 */             return (Void)super.visit(privateElements);
/*     */           }
/* 264 */         }).writeAll(overrideElements);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 269 */       final Map<Scope, List<Object>> scopeInstancesInUse = Maps.newHashMap();
/* 270 */       final List<ScopeBinding> scopeBindings = Lists.newArrayList();
/* 271 */       (new Modules.ModuleWriter(binder)
/*     */         {
/*     */           public <T> Void visit(Binding<T> binding) {
/* 274 */             if (!overriddenKeys.remove(binding.getKey())) {
/* 275 */               super.visit(binding);
/*     */ 
/*     */               
/* 278 */               Scope scope = Modules.OverrideModule.this.getScopeInstanceOrNull(binding);
/* 279 */               if (scope != null) {
/* 280 */                 ((List<Object>)scopeInstancesInUse
/* 281 */                   .computeIfAbsent(scope, k -> Lists.newArrayList()))
/* 282 */                   .add(binding.getSource());
/*     */               }
/*     */             } 
/*     */             
/* 286 */             return null;
/*     */           }
/*     */ 
/*     */           
/*     */           void rewrite(Binder binder, PrivateElements privateElements, Set<Key<?>> keysToSkip) {
/* 291 */             PrivateBinder privateBinder = binder.withSource(privateElements.getSource()).newPrivateBinder();
/*     */             
/* 293 */             Set<Key<?>> skippedExposes = Sets.newHashSet();
/*     */             
/* 295 */             for (Key<?> key : (Iterable<Key<?>>)privateElements.getExposedKeys()) {
/* 296 */               if (keysToSkip.remove(key)) {
/* 297 */                 skippedExposes.add(key); continue;
/*     */               } 
/* 299 */               privateBinder.withSource(privateElements.getExposedSource(key)).expose(key);
/*     */             } 
/*     */ 
/*     */             
/* 303 */             for (Element element : privateElements.getElements()) {
/* 304 */               if (element instanceof Binding && skippedExposes.remove(((Binding)element).getKey())) {
/*     */                 continue;
/*     */               }
/* 307 */               if (element instanceof PrivateElements) {
/* 308 */                 rewrite((Binder)privateBinder, (PrivateElements)element, skippedExposes);
/*     */                 continue;
/*     */               } 
/* 311 */               element.applyTo((Binder)privateBinder);
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public Void visit(PrivateElements privateElements) {
/* 317 */             rewrite(this.binder, privateElements, overriddenKeys);
/* 318 */             return null;
/*     */           }
/*     */ 
/*     */           
/*     */           public Void visit(ScopeBinding scopeBinding) {
/* 323 */             scopeBindings.add(scopeBinding);
/* 324 */             return null;
/*     */           }
/* 326 */         }).writeAll(elements);
/*     */ 
/*     */ 
/*     */       
/* 330 */       (new Modules.ModuleWriter(this, binder)
/*     */         {
/*     */           public Void visit(ScopeBinding scopeBinding)
/*     */           {
/* 334 */             ScopeBinding overideBinding = (ScopeBinding)overridesScopeAnnotations.remove(scopeBinding.getAnnotationType());
/* 335 */             if (overideBinding == null) {
/* 336 */               super.visit(scopeBinding);
/*     */             } else {
/* 338 */               List<Object> usedSources = (List<Object>)scopeInstancesInUse.get(scopeBinding.getScope());
/* 339 */               if (usedSources != null) {
/* 340 */                 StringBuilder sb = new StringBuilder("The scope for @%s is bound directly and cannot be overridden.");
/*     */ 
/*     */                 
/* 343 */                 String str = String.valueOf(Errors.convert(scopeBinding.getSource())); sb.append((new StringBuilder(27 + String.valueOf(str).length())).append("%n     original binding at ").append(str).toString());
/* 344 */                 for (Object usedSource : usedSources) {
/* 345 */                   String str1 = String.valueOf(Errors.convert(usedSource)); sb.append((new StringBuilder(25 + String.valueOf(str1).length())).append("%n     bound directly at ").append(str1).toString());
/*     */                 } 
/* 347 */                 this.binder
/* 348 */                   .withSource(overideBinding.getSource())
/* 349 */                   .addError(sb.toString(), new Object[] { scopeBinding.getAnnotationType().getSimpleName() });
/*     */               } 
/*     */             } 
/* 352 */             return null;
/*     */           }
/* 354 */         }).writeAll((Iterable)scopeBindings);
/*     */     }
/*     */     
/*     */     private Scope getScopeInstanceOrNull(Binding<?> binding) {
/* 358 */       return (Scope)binding.acceptScopingVisitor((BindingScopingVisitor)new DefaultBindingScopingVisitor<Scope>(this)
/*     */           {
/*     */             public Scope visitScope(Scope scope)
/*     */             {
/* 362 */               return scope;
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ModuleWriter extends DefaultElementVisitor<Void> {
/*     */     protected final Binder binder;
/*     */     
/*     */     ModuleWriter(Binder binder) {
/* 372 */       this.binder = binder.skipSources(new Class[] { getClass() });
/*     */     }
/*     */ 
/*     */     
/*     */     protected Void visitOther(Element element) {
/* 377 */       element.applyTo(this.binder);
/* 378 */       return null;
/*     */     }
/*     */     
/*     */     void writeAll(Iterable<? extends Element> elements) {
/* 382 */       for (Element element : elements) {
/* 383 */         element.acceptVisitor((ElementVisitor)this);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static Module extractScanners(Iterable<Element> elements) {
/* 389 */     final List<ModuleAnnotatedMethodScannerBinding> scanners = Lists.newArrayList();
/* 390 */     DefaultElementVisitor<Void> defaultElementVisitor = new DefaultElementVisitor<Void>()
/*     */       {
/*     */         public Void visit(ModuleAnnotatedMethodScannerBinding binding)
/*     */         {
/* 394 */           scanners.add(binding);
/* 395 */           return null;
/*     */         }
/*     */       };
/* 398 */     for (Element element : elements) {
/* 399 */       element.acceptVisitor((ElementVisitor)defaultElementVisitor);
/*     */     }
/* 401 */     return (Module)new AbstractModule()
/*     */       {
/*     */         protected void configure() {
/* 404 */           for (ModuleAnnotatedMethodScannerBinding scanner : scanners) {
/* 405 */             scanner.applyTo(binder());
/*     */           }
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Module requireExplicitBindingsModule() {
/* 417 */     return new RequireExplicitBindingsModule();
/*     */   }
/*     */   
/*     */   private static final class RequireExplicitBindingsModule implements Module { private RequireExplicitBindingsModule() {}
/*     */     
/*     */     public void configure(Binder binder) {
/* 423 */       binder.requireExplicitBindings();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Module requireAtInjectOnConstructorsModule() {
/* 435 */     return new RequireAtInjectOnConstructorsModule();
/*     */   }
/*     */   
/*     */   private static final class RequireAtInjectOnConstructorsModule implements Module { private RequireAtInjectOnConstructorsModule() {}
/*     */     
/*     */     public void configure(Binder binder) {
/* 441 */       binder.requireAtInjectOnConstructors();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Module requireExactBindingAnnotationsModule() {
/* 453 */     return new RequireExactBindingAnnotationsModule();
/*     */   }
/*     */   
/*     */   private static final class RequireExactBindingAnnotationsModule implements Module { private RequireExactBindingAnnotationsModule() {}
/*     */     
/*     */     public void configure(Binder binder) {
/* 459 */       binder.requireExactBindingAnnotations();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Module disableCircularProxiesModule() {
/* 469 */     return new DisableCircularProxiesModule();
/*     */   }
/*     */   
/*     */   private static final class DisableCircularProxiesModule implements Module { private DisableCircularProxiesModule() {}
/*     */     
/*     */     public void configure(Binder binder) {
/* 475 */       binder.disableCircularProxies();
/*     */     } }
/*     */ 
/*     */   
/*     */   public static interface OverriddenModuleBuilder {
/*     */     Module with(Module... param1VarArgs);
/*     */     
/*     */     @Deprecated
/*     */     Module with();
/*     */     
/*     */     Module with(Iterable<? extends Module> param1Iterable);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\injec\\util\Modules.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */