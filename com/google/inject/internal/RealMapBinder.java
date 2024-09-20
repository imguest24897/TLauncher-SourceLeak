/*      */ package com.google.inject.internal;
/*      */ 
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.collect.HashMultimap;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.google.common.collect.ImmutableMultimap;
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.google.common.collect.LinkedHashMultimap;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Multimap;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.google.common.collect.UnmodifiableIterator;
/*      */ import com.google.inject.Binder;
/*      */ import com.google.inject.Binding;
/*      */ import com.google.inject.Injector;
/*      */ import com.google.inject.Key;
/*      */ import com.google.inject.Module;
/*      */ import com.google.inject.Provider;
/*      */ import com.google.inject.TypeLiteral;
/*      */ import com.google.inject.binder.LinkedBindingBuilder;
/*      */ import com.google.inject.multibindings.MapBinderBinding;
/*      */ import com.google.inject.multibindings.MultibindingsTargetVisitor;
/*      */ import com.google.inject.spi.BindingTargetVisitor;
/*      */ import com.google.inject.spi.Dependency;
/*      */ import com.google.inject.spi.Element;
/*      */ import com.google.inject.spi.ProviderInstanceBinding;
/*      */ import com.google.inject.spi.ProviderWithExtensionVisitor;
/*      */ import com.google.inject.util.Types;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.reflect.Type;
/*      */ import java.util.Collection;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
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
/*      */ public final class RealMapBinder<K, V>
/*      */   implements Module
/*      */ {
/*      */   public static <K, V> RealMapBinder<K, V> newMapRealBinder(Binder binder, TypeLiteral<K> keyType, TypeLiteral<V> valueType) {
/*   74 */     binder = binder.skipSources(new Class[] { RealMapBinder.class });
/*   75 */     return newRealMapBinder(binder, keyType, valueType, 
/*      */ 
/*      */ 
/*      */         
/*   79 */         Key.get(mapOf(keyType, valueType)), 
/*   80 */         RealMultibinder.newRealSetBinder(binder, Key.get(entryOfProviderOf(keyType, valueType))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> RealMapBinder<K, V> newRealMapBinder(Binder binder, TypeLiteral<K> keyType, TypeLiteral<V> valueType, Annotation annotation) {
/*   89 */     binder = binder.skipSources(new Class[] { RealMapBinder.class });
/*   90 */     return newRealMapBinder(binder, keyType, valueType, 
/*      */ 
/*      */ 
/*      */         
/*   94 */         Key.get(mapOf(keyType, valueType), annotation), 
/*   95 */         RealMultibinder.newRealSetBinder(binder, 
/*   96 */           Key.get(entryOfProviderOf(keyType, valueType), annotation)));
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
/*      */   public static <K, V> RealMapBinder<K, V> newRealMapBinder(Binder binder, TypeLiteral<K> keyType, TypeLiteral<V> valueType, Class<? extends Annotation> annotationType) {
/*  108 */     binder = binder.skipSources(new Class[] { RealMapBinder.class });
/*  109 */     return newRealMapBinder(binder, keyType, valueType, 
/*      */ 
/*      */ 
/*      */         
/*  113 */         Key.get(mapOf(keyType, valueType), annotationType), 
/*  114 */         RealMultibinder.newRealSetBinder(binder, 
/*  115 */           Key.get(entryOfProviderOf(keyType, valueType), annotationType)));
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> TypeLiteral<Map<K, V>> mapOf(TypeLiteral<K> keyType, TypeLiteral<V> valueType) {
/*  120 */     return 
/*  121 */       TypeLiteral.get(Types.mapOf(keyType.getType(), valueType.getType()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> TypeLiteral<Map<K, Provider<V>>> mapOfProviderOf(TypeLiteral<K> keyType, TypeLiteral<V> valueType) {
/*  127 */     return 
/*  128 */       TypeLiteral.get(Types.mapOf(keyType.getType(), Types.providerOf(valueType.getType())));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> TypeLiteral<Map<K, Provider<V>>> mapOfJavaxProviderOf(TypeLiteral<K> keyType, TypeLiteral<V> valueType) {
/*  135 */     return 
/*  136 */       TypeLiteral.get(
/*  137 */         Types.mapOf(keyType
/*  138 */           .getType(), 
/*  139 */           Types.newParameterizedType(Provider.class, new Type[] { valueType.getType() })));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> TypeLiteral<Map<K, Set<Provider<V>>>> mapOfSetOfProviderOf(TypeLiteral<K> keyType, TypeLiteral<V> valueType) {
/*  145 */     return 
/*  146 */       TypeLiteral.get(
/*  147 */         Types.mapOf(keyType.getType(), Types.setOf(Types.providerOf(valueType.getType()))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> TypeLiteral<Map<K, Set<Provider<V>>>> mapOfSetOfJavaxProviderOf(TypeLiteral<K> keyType, TypeLiteral<V> valueType) {
/*  153 */     return 
/*  154 */       TypeLiteral.get(
/*  155 */         Types.mapOf(keyType
/*  156 */           .getType(), Types.setOf(Types.javaxProviderOf(valueType.getType()))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> TypeLiteral<Map<K, Collection<Provider<V>>>> mapOfCollectionOfProviderOf(TypeLiteral<K> keyType, TypeLiteral<V> valueType) {
/*  162 */     return 
/*  163 */       TypeLiteral.get(
/*  164 */         Types.mapOf(keyType
/*  165 */           .getType(), Types.collectionOf(Types.providerOf(valueType.getType()))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> TypeLiteral<Map<K, Collection<Provider<V>>>> mapOfCollectionOfJavaxProviderOf(TypeLiteral<K> keyType, TypeLiteral<V> valueType) {
/*  172 */     return 
/*  173 */       TypeLiteral.get(
/*  174 */         Types.mapOf(keyType
/*  175 */           .getType(), Types.collectionOf(Types.javaxProviderOf(valueType.getType()))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> TypeLiteral<Map.Entry<K, Provider<V>>> entryOfProviderOf(TypeLiteral<K> keyType, TypeLiteral<V> valueType) {
/*  181 */     return 
/*  182 */       TypeLiteral.get(
/*  183 */         Types.newParameterizedTypeWithOwner(Map.class, Map.Entry.class, new Type[] {
/*      */ 
/*      */             
/*  186 */             keyType.getType(), 
/*  187 */             Types.providerOf(valueType.getType())
/*      */           }));
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> TypeLiteral<Map.Entry<K, Provider<V>>> entryOfJavaxProviderOf(TypeLiteral<K> keyType, TypeLiteral<V> valueType) {
/*  193 */     return 
/*  194 */       TypeLiteral.get(
/*  195 */         Types.newParameterizedTypeWithOwner(Map.class, Map.Entry.class, new Type[] {
/*      */ 
/*      */             
/*  198 */             keyType.getType(), 
/*  199 */             Types.javaxProviderOf(valueType.getType())
/*      */           }));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> TypeLiteral<Set<Map.Entry<K, Provider<V>>>> setOfEntryOfJavaxProviderOf(TypeLiteral<K> keyType, TypeLiteral<V> valueType) {
/*  206 */     return 
/*  207 */       TypeLiteral.get(Types.setOf(entryOfJavaxProviderOf(keyType, valueType).getType()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static <T> Key<Provider<T>> getKeyOfProvider(Key<T> valueKey) {
/*  213 */     return valueKey
/*  214 */       .ofType(Types.providerOf(valueKey.getTypeLiteral().getType()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> RealMapBinder<K, V> newRealMapBinder(Binder binder, TypeLiteral<K> keyType, Key<V> valueTypeAndAnnotation) {
/*  221 */     binder = binder.skipSources(new Class[] { RealMapBinder.class });
/*  222 */     TypeLiteral<V> valueType = valueTypeAndAnnotation.getTypeLiteral();
/*  223 */     return newRealMapBinder(binder, keyType, valueType, valueTypeAndAnnotation
/*      */ 
/*      */ 
/*      */         
/*  227 */         .ofType(mapOf(keyType, valueType)), 
/*  228 */         RealMultibinder.newRealSetBinder(binder, valueTypeAndAnnotation
/*  229 */           .ofType(entryOfProviderOf(keyType, valueType))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> RealMapBinder<K, V> newRealMapBinder(Binder binder, TypeLiteral<K> keyType, TypeLiteral<V> valueType, Key<Map<K, V>> mapKey, RealMultibinder<Map.Entry<K, Provider<V>>> entrySetBinder) {
/*  238 */     RealMapBinder<K, V> mapBinder = new RealMapBinder<>(binder, keyType, valueType, mapKey, entrySetBinder);
/*      */     
/*  240 */     binder.install(mapBinder);
/*  241 */     return mapBinder;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  247 */   private static final ImmutableSet<Dependency<?>> MODULE_DEPENDENCIES = ImmutableSet.of(Dependency.get(Key.get(Injector.class)));
/*      */ 
/*      */   
/*      */   private final BindingSelection<K, V> bindingSelection;
/*      */ 
/*      */   
/*      */   private final Binder binder;
/*      */ 
/*      */   
/*      */   private final RealMultibinder<Map.Entry<K, Provider<V>>> entrySetBinder;
/*      */ 
/*      */   
/*      */   private RealMapBinder(Binder binder, TypeLiteral<K> keyType, TypeLiteral<V> valueType, Key<Map<K, V>> mapKey, RealMultibinder<Map.Entry<K, Provider<V>>> entrySetBinder) {
/*  260 */     this.bindingSelection = new BindingSelection<>(keyType, valueType, mapKey, entrySetBinder);
/*  261 */     this.binder = binder;
/*  262 */     this.entrySetBinder = entrySetBinder;
/*      */   }
/*      */   
/*      */   public void permitDuplicates() {
/*  266 */     Errors.checkConfiguration(!this.bindingSelection.isInitialized(), "MapBinder was already initialized", new Object[0]);
/*  267 */     this.entrySetBinder.permitDuplicates();
/*  268 */     this.binder.install(new MultimapBinder<>(this.bindingSelection));
/*      */   }
/*      */ 
/*      */   
/*      */   Key<V> getKeyForNewValue(K key) {
/*  273 */     Errors.checkNotNull(key, "key");
/*  274 */     Errors.checkConfiguration(!this.bindingSelection.isInitialized(), "MapBinder was already initialized", new Object[0]);
/*      */     
/*  276 */     RealMultibinder<Map.Entry<K, Provider<V>>> entrySetBinder = this.bindingSelection.getEntrySetBinder();
/*      */ 
/*      */     
/*  279 */     Key<V> valueKey = Key.get(this.bindingSelection
/*  280 */         .getValueType(), new RealElement(entrySetBinder
/*      */           
/*  282 */           .getSetName(), Element.Type.MAPBINDER, this.bindingSelection.getKeyType().toString()));
/*  283 */     entrySetBinder.addBinding().toProvider(new ProviderMapEntry<>(key, valueKey));
/*  284 */     return valueKey;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LinkedBindingBuilder<V> addBinding(K key) {
/*  292 */     return this.binder.bind(getKeyForNewValue(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public void configure(Binder binder) {
/*  297 */     Errors.checkConfiguration(!this.bindingSelection.isInitialized(), "MapBinder was already initialized", new Object[0]);
/*      */ 
/*      */     
/*  300 */     RealProviderMapProvider<K, V> providerMapProvider = new RealProviderMapProvider<>(this.bindingSelection);
/*      */     
/*  302 */     binder.bind(this.bindingSelection.getProviderMapKey()).toProvider(providerMapProvider);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  308 */     RealProviderMapProvider<K, V> realProviderMapProvider1 = providerMapProvider;
/*      */     
/*  310 */     binder.bind(this.bindingSelection.getJavaxProviderMapKey()).toProvider(realProviderMapProvider1);
/*      */     
/*  312 */     RealMapProvider<K, V> mapProvider = new RealMapProvider<>(this.bindingSelection);
/*  313 */     binder.bind(this.bindingSelection.getMapKey()).toProvider(mapProvider);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  319 */     Key<Set<Map.Entry<K, Provider<V>>>> massagedEntrySetProviderKey = this.bindingSelection.getEntrySetBinder().getSetKey();
/*  320 */     binder.bind(this.bindingSelection.getEntrySetJavaxProviderKey()).to(massagedEntrySetProviderKey);
/*      */ 
/*      */     
/*  323 */     binder.bind(this.bindingSelection.getMapOfKeyExtendsValueKey()).to(this.bindingSelection.getMapKey());
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean equals(Object o) {
/*  328 */     return (o instanceof RealMapBinder && ((RealMapBinder)o).bindingSelection
/*  329 */       .equals(this.bindingSelection));
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  334 */     return this.bindingSelection.hashCode();
/*      */   }
/*      */   private static final class BindingSelection<K, V> { private final TypeLiteral<K> keyType; private final TypeLiteral<V> valueType; private final Key<Map<K, V>> mapKey;
/*      */     private Key<Map<K, Provider<V>>> javaxProviderMapKey;
/*      */     private Key<Map<K, Provider<V>>> providerMapKey;
/*      */     private Key<Map<K, Set<V>>> multimapKey;
/*      */     private Key<Map<K, Set<Provider<V>>>> providerSetMultimapKey;
/*      */     private Key<Map<K, Set<Provider<V>>>> javaxProviderSetMultimapKey;
/*      */     private Key<Map<K, Collection<Provider<V>>>> providerCollectionMultimapKey;
/*      */     private Key<Map<K, Collection<Provider<V>>>> javaxProviderCollectionMultimapKey;
/*      */     private Key<Set<Map.Entry<K, Provider<V>>>> entrySetJavaxProviderKey;
/*      */     private Key<Map<K, ? extends V>> mapOfKeyExtendsValueKey;
/*      */     private final RealMultibinder<Map.Entry<K, Provider<V>>> entrySetBinder;
/*      */     private InitializationState initializationState;
/*      */     private ImmutableMap<K, Binding<V>> mapBindings;
/*      */     private ImmutableMap<K, Set<Binding<V>>> multimapBindings;
/*      */     private ImmutableList<Map.Entry<K, Binding<V>>> entries;
/*      */     private boolean permitsDuplicates;
/*      */     
/*  353 */     private enum InitializationState { UNINITIALIZED,
/*  354 */       INITIALIZED,
/*  355 */       HAS_ERRORS; }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private BindingSelection(TypeLiteral<K> keyType, TypeLiteral<V> valueType, Key<Map<K, V>> mapKey, RealMultibinder<Map.Entry<K, Provider<V>>> entrySetBinder) {
/*  398 */       this.keyType = keyType;
/*  399 */       this.valueType = valueType;
/*  400 */       this.mapKey = mapKey;
/*  401 */       this.entrySetBinder = entrySetBinder;
/*  402 */       this.initializationState = InitializationState.UNINITIALIZED;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean tryInitialize(InjectorImpl injector, Errors errors) {
/*      */       LinkedHashMultimap linkedHashMultimap;
/*  412 */       if (this.initializationState != InitializationState.UNINITIALIZED) {
/*  413 */         return (this.initializationState != InitializationState.HAS_ERRORS);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  420 */       this.permitsDuplicates = this.entrySetBinder.permitsDuplicates(injector);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  425 */       Map<K, ImmutableSet.Builder<Binding<V>>> bindingMultimapMutable = new LinkedHashMap<>();
/*      */       
/*  427 */       Map<K, Binding<V>> bindingMapMutable = new LinkedHashMap<>();
/*  428 */       HashMultimap hashMultimap = HashMultimap.create();
/*  429 */       Indexer indexer = new Indexer(injector);
/*  430 */       Multimap<K, Binding<V>> duplicates = null;
/*      */       
/*  432 */       ImmutableList.Builder<Map.Entry<K, Binding<V>>> entriesBuilder = ImmutableList.builder();
/*      */ 
/*      */ 
/*      */       
/*  436 */       for (Binding<Map.Entry<K, Provider<V>>> binding : injector.findBindingsByType(this.entrySetBinder.getElementTypeLiteral())) {
/*  437 */         if (this.entrySetBinder.containsElement((Element)binding)) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  443 */           ProviderInstanceBinding<RealMapBinder.ProviderMapEntry<K, V>> entryBinding = (ProviderInstanceBinding)binding;
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  448 */           RealMapBinder.ProviderMapEntry<K, V> entry = (RealMapBinder.ProviderMapEntry<K, V>)entryBinding.getUserSuppliedProvider();
/*  449 */           K key = entry.getKey();
/*      */           
/*  451 */           Key<V> valueKey = entry.getValueKey();
/*  452 */           Binding<V> valueBinding = injector.getExistingBinding(valueKey);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  458 */           if (hashMultimap.put(key, valueBinding.acceptTargetVisitor((BindingTargetVisitor)indexer))) {
/*      */             
/*  460 */             entriesBuilder.add(Maps.immutableEntry(key, valueBinding));
/*      */             
/*  462 */             Binding<V> previous = bindingMapMutable.put(key, valueBinding);
/*      */             
/*  464 */             if (previous != null && !this.permitsDuplicates) {
/*  465 */               if (duplicates == null)
/*      */               {
/*  467 */                 linkedHashMultimap = LinkedHashMultimap.create();
/*      */               }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  474 */               linkedHashMultimap.put(key, previous);
/*  475 */               linkedHashMultimap.put(key, valueBinding);
/*      */             } 
/*      */ 
/*      */             
/*  479 */             if (this.permitsDuplicates)
/*      */             {
/*  481 */               ((ImmutableSet.Builder)bindingMultimapMutable
/*  482 */                 .computeIfAbsent(key, k -> ImmutableSet.builder()))
/*  483 */                 .add(valueBinding);
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  491 */       if (linkedHashMultimap != null) {
/*  492 */         this.initializationState = InitializationState.HAS_ERRORS;
/*  493 */         reportDuplicateKeysError(this.mapKey, (Multimap<K, Binding<V>>)linkedHashMultimap, errors);
/*      */         
/*  495 */         return false;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  501 */       ImmutableMap.Builder<K, Set<Binding<V>>> bindingsMultimapBuilder = ImmutableMap.builder();
/*      */       
/*  503 */       for (Map.Entry<K, ImmutableSet.Builder<Binding<V>>> entry : bindingMultimapMutable.entrySet()) {
/*  504 */         bindingsMultimapBuilder.put(entry.getKey(), ((ImmutableSet.Builder)entry.getValue()).build());
/*      */       }
/*  506 */       this.mapBindings = ImmutableMap.copyOf(bindingMapMutable);
/*  507 */       this.multimapBindings = bindingsMultimapBuilder.build();
/*      */       
/*  509 */       this.entries = entriesBuilder.build();
/*      */       
/*  511 */       this.initializationState = InitializationState.INITIALIZED;
/*      */       
/*  513 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     private static <K, V> void reportDuplicateKeysError(Key<Map<K, V>> mapKey, Multimap<K, Binding<V>> duplicates, Errors errors) {
/*  518 */       errors.duplicateMapKey(mapKey, duplicates);
/*      */     }
/*      */     
/*      */     private boolean containsElement(Element element) {
/*      */       Key<?> key;
/*  523 */       if (this.entrySetBinder.containsElement(element)) {
/*  524 */         return true;
/*      */       }
/*      */ 
/*      */       
/*  528 */       if (element instanceof Binding) {
/*  529 */         key = ((Binding)element).getKey();
/*      */       } else {
/*  531 */         return false;
/*      */       } 
/*      */       
/*  534 */       return (key.equals(getMapKey()) || key
/*  535 */         .equals(getProviderMapKey()) || key
/*  536 */         .equals(getJavaxProviderMapKey()) || key
/*  537 */         .equals(getMultimapKey()) || key
/*  538 */         .equals(getProviderSetMultimapKey()) || key
/*  539 */         .equals(getJavaxProviderSetMultimapKey()) || key
/*  540 */         .equals(getProviderCollectionMultimapKey()) || key
/*  541 */         .equals(getJavaxProviderCollectionMultimapKey()) || key
/*  542 */         .equals(this.entrySetBinder.getSetKey()) || key
/*  543 */         .equals(getEntrySetJavaxProviderKey()) || key
/*  544 */         .equals(getMapOfKeyExtendsValueKey()) || 
/*  545 */         matchesValueKey(key));
/*      */     }
/*      */ 
/*      */     
/*      */     private boolean matchesValueKey(Key<?> key) {
/*  550 */       return (key.getAnnotation() instanceof RealElement && ((RealElement)key
/*  551 */         .getAnnotation()).setName().equals(this.entrySetBinder.getSetName()) && ((RealElement)key
/*  552 */         .getAnnotation()).type() == Element.Type.MAPBINDER && ((RealElement)key
/*  553 */         .getAnnotation()).keyType().equals(this.keyType.toString()) && key
/*  554 */         .getTypeLiteral().equals(this.valueType));
/*      */     }
/*      */     
/*      */     private Key<Map<K, Provider<V>>> getProviderMapKey() {
/*  558 */       Key<Map<K, Provider<V>>> local = this.providerMapKey;
/*  559 */       if (local == null) {
/*  560 */         local = this.providerMapKey = this.mapKey.ofType(RealMapBinder.mapOfProviderOf(this.keyType, this.valueType));
/*      */       }
/*  562 */       return local;
/*      */     }
/*      */     
/*      */     private Key<Map<K, Provider<V>>> getJavaxProviderMapKey() {
/*  566 */       Key<Map<K, Provider<V>>> local = this.javaxProviderMapKey;
/*  567 */       if (local == null) {
/*  568 */         local = this.javaxProviderMapKey = this.mapKey.ofType(RealMapBinder.mapOfJavaxProviderOf(this.keyType, this.valueType));
/*      */       }
/*  570 */       return local;
/*      */     }
/*      */     
/*      */     private Key<Map<K, Set<V>>> getMultimapKey() {
/*  574 */       Key<Map<K, Set<V>>> local = this.multimapKey;
/*  575 */       if (local == null) {
/*  576 */         local = this.multimapKey = this.mapKey.ofType(RealMapBinder.mapOf(this.keyType, RealMultibinder.setOf(this.valueType)));
/*      */       }
/*  578 */       return local;
/*      */     }
/*      */     
/*      */     private Key<Map<K, Set<Provider<V>>>> getProviderSetMultimapKey() {
/*  582 */       Key<Map<K, Set<Provider<V>>>> local = this.providerSetMultimapKey;
/*  583 */       if (local == null) {
/*  584 */         local = this.providerSetMultimapKey = this.mapKey.ofType(RealMapBinder.mapOfSetOfProviderOf(this.keyType, this.valueType));
/*      */       }
/*  586 */       return local;
/*      */     }
/*      */     
/*      */     private Key<Map<K, Set<Provider<V>>>> getJavaxProviderSetMultimapKey() {
/*  590 */       Key<Map<K, Set<Provider<V>>>> local = this.javaxProviderSetMultimapKey;
/*  591 */       if (local == null)
/*      */       {
/*      */         
/*  594 */         local = this.javaxProviderSetMultimapKey = this.mapKey.ofType(RealMapBinder.mapOfSetOfJavaxProviderOf(this.keyType, this.valueType));
/*      */       }
/*  596 */       return local;
/*      */     }
/*      */     
/*      */     private Key<Map<K, Collection<Provider<V>>>> getProviderCollectionMultimapKey() {
/*  600 */       Key<Map<K, Collection<Provider<V>>>> local = this.providerCollectionMultimapKey;
/*  601 */       if (local == null)
/*      */       {
/*      */         
/*  604 */         local = this.providerCollectionMultimapKey = this.mapKey.ofType(RealMapBinder.mapOfCollectionOfProviderOf(this.keyType, this.valueType));
/*      */       }
/*  606 */       return local;
/*      */     }
/*      */ 
/*      */     
/*      */     private Key<Map<K, Collection<Provider<V>>>> getJavaxProviderCollectionMultimapKey() {
/*  611 */       Key<Map<K, Collection<Provider<V>>>> local = this.javaxProviderCollectionMultimapKey;
/*  612 */       if (local == null)
/*      */       {
/*      */         
/*  615 */         local = this.javaxProviderCollectionMultimapKey = this.mapKey.ofType(RealMapBinder.mapOfCollectionOfJavaxProviderOf(this.keyType, this.valueType));
/*      */       }
/*  617 */       return local;
/*      */     }
/*      */     
/*      */     private Key<Set<Map.Entry<K, Provider<V>>>> getEntrySetJavaxProviderKey() {
/*  621 */       Key<Set<Map.Entry<K, Provider<V>>>> local = this.entrySetJavaxProviderKey;
/*  622 */       if (local == null)
/*      */       {
/*      */         
/*  625 */         local = this.entrySetJavaxProviderKey = this.mapKey.ofType(RealMapBinder.setOfEntryOfJavaxProviderOf(this.keyType, this.valueType));
/*      */       }
/*  627 */       return local;
/*      */     }
/*      */ 
/*      */     
/*      */     private Key<Map<K, ? extends V>> getMapOfKeyExtendsValueKey() {
/*  632 */       Key<Map<K, ? extends V>> local = this.mapOfKeyExtendsValueKey;
/*  633 */       if (local == null) {
/*  634 */         Type extendsValue = Types.subtypeOf(this.valueType.getType());
/*  635 */         Type mapOfKeyAndExtendsValue = Types.mapOf(this.keyType.getType(), extendsValue);
/*      */ 
/*      */         
/*  638 */         local = this.mapOfKeyExtendsValueKey = this.mapKey.ofType(mapOfKeyAndExtendsValue);
/*      */       } 
/*  640 */       return local;
/*      */     }
/*      */     
/*      */     private ImmutableMap<K, Binding<V>> getMapBindings() {
/*  644 */       Errors.checkConfiguration(isInitialized(), "MapBinder has not yet been initialized", new Object[0]);
/*  645 */       return this.mapBindings;
/*      */     }
/*      */     
/*      */     private ImmutableMap<K, Set<Binding<V>>> getMultimapBindings() {
/*  649 */       Errors.checkConfiguration(isInitialized(), "MapBinder has not yet been initialized", new Object[0]);
/*  650 */       return this.multimapBindings;
/*      */     }
/*      */     
/*      */     private ImmutableList<Map.Entry<K, Binding<V>>> getEntries() {
/*  654 */       Errors.checkConfiguration(isInitialized(), "MapBinder has not yet been initialized", new Object[0]);
/*  655 */       return this.entries;
/*      */     }
/*      */     
/*      */     private boolean isInitialized() {
/*  659 */       return (this.initializationState == InitializationState.INITIALIZED);
/*      */     }
/*      */     
/*      */     private TypeLiteral<K> getKeyType() {
/*  663 */       return this.keyType;
/*      */     }
/*      */     
/*      */     private TypeLiteral<V> getValueType() {
/*  667 */       return this.valueType;
/*      */     }
/*      */     
/*      */     private Key<Map<K, V>> getMapKey() {
/*  671 */       return this.mapKey;
/*      */     }
/*      */     
/*      */     private RealMultibinder<Map.Entry<K, Provider<V>>> getEntrySetBinder() {
/*  675 */       return this.entrySetBinder;
/*      */     }
/*      */     
/*      */     private boolean permitsDuplicates() {
/*  679 */       if (isInitialized()) {
/*  680 */         return this.permitsDuplicates;
/*      */       }
/*  682 */       throw new UnsupportedOperationException("permitsDuplicates() not supported for module bindings");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  689 */       return (o instanceof BindingSelection && ((BindingSelection)o).mapKey.equals(this.mapKey));
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  694 */       return this.mapKey.hashCode();
/*      */     } }
/*      */ 
/*      */   
/*      */   private static final class RealProviderMapProvider<K, V>
/*      */     extends RealMapBinderProviderWithDependencies<K, V, Map<K, Provider<V>>> {
/*      */     private Map<K, Provider<V>> mapOfProviders;
/*  701 */     private Set<Dependency<?>> dependencies = (Set<Dependency<?>>)RealMapBinder.MODULE_DEPENDENCIES;
/*      */     
/*      */     private RealProviderMapProvider(RealMapBinder.BindingSelection<K, V> bindingSelection) {
/*  704 */       super(bindingSelection);
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Dependency<?>> getDependencies() {
/*  709 */       return this.dependencies;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void doInitialize(InjectorImpl injector, Errors errors) {
/*  714 */       ImmutableMap.Builder<K, Provider<V>> mapOfProvidersBuilder = ImmutableMap.builder();
/*  715 */       ImmutableSet.Builder<Dependency<?>> dependenciesBuilder = ImmutableSet.builder();
/*  716 */       for (UnmodifiableIterator<Map.Entry<K, Binding<V>>> unmodifiableIterator = this.bindingSelection.getMapBindings().entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<K, Binding<V>> entry = unmodifiableIterator.next();
/*  717 */         mapOfProvidersBuilder.put(entry.getKey(), ((Binding)entry.getValue()).getProvider());
/*  718 */         dependenciesBuilder.add(Dependency.get(RealMapBinder.getKeyOfProvider(((Binding)entry.getValue()).getKey()))); }
/*      */ 
/*      */       
/*  721 */       this.mapOfProviders = (Map<K, Provider<V>>)mapOfProvidersBuilder.build();
/*  722 */       this.dependencies = (Set<Dependency<?>>)dependenciesBuilder.build();
/*      */     }
/*      */ 
/*      */     
/*      */     protected Map<K, Provider<V>> doProvision(InternalContext context, Dependency<?> dependency) {
/*  727 */       return this.mapOfProviders;
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class RealMapProvider<K, V>
/*      */     extends RealMapBinderProviderWithDependencies<K, V, Map<K, V>>
/*      */     implements ProviderWithExtensionVisitor<Map<K, V>>, MapBinderBinding<Map<K, V>> {
/*  734 */     private Set<Dependency<?>> dependencies = (Set<Dependency<?>>)RealMapBinder.MODULE_DEPENDENCIES;
/*      */ 
/*      */ 
/*      */     
/*      */     private SingleParameterInjector<V>[] injectors;
/*      */ 
/*      */     
/*      */     private K[] keys;
/*      */ 
/*      */ 
/*      */     
/*      */     private RealMapProvider(RealMapBinder.BindingSelection<K, V> bindingSelection) {
/*  746 */       super(bindingSelection);
/*      */     }
/*      */     
/*      */     private RealMapBinder.BindingSelection<K, V> getBindingSelection() {
/*  750 */       return this.bindingSelection;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     protected void doInitialize(InjectorImpl injector, Errors errors) throws ErrorsException {
/*  756 */       K[] keysArray = (K[])new Object[this.bindingSelection.getMapBindings().size()];
/*  757 */       this.keys = keysArray;
/*  758 */       ImmutableSet.Builder<Dependency<?>> dependenciesBuilder = ImmutableSet.builder();
/*  759 */       int i = 0;
/*  760 */       for (UnmodifiableIterator<Map.Entry<K, Binding<V>>> unmodifiableIterator = this.bindingSelection.getMapBindings().entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<K, Binding<V>> entry = unmodifiableIterator.next();
/*  761 */         dependenciesBuilder.add(Dependency.get(((Binding)entry.getValue()).getKey()));
/*  762 */         this.keys[i] = entry.getKey();
/*  763 */         i++; }
/*      */ 
/*      */       
/*  766 */       ImmutableSet<Dependency<?>> localDependencies = dependenciesBuilder.build();
/*  767 */       this.dependencies = (Set<Dependency<?>>)localDependencies;
/*      */       
/*  769 */       ImmutableList immutableList = localDependencies.asList();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  775 */       SingleParameterInjector[] arrayOfSingleParameterInjector = (SingleParameterInjector[])injector.getParametersInjectors((List<Dependency<?>>)immutableList, errors);
/*  776 */       this.injectors = (SingleParameterInjector<V>[])arrayOfSingleParameterInjector;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     protected Map<K, V> doProvision(InternalContext context, Dependency<?> dependency) throws InternalProvisionException {
/*  782 */       SingleParameterInjector<V>[] localInjectors = this.injectors;
/*  783 */       if (localInjectors == null)
/*      */       {
/*  785 */         return (Map<K, V>)ImmutableMap.of();
/*      */       }
/*      */       
/*  788 */       ImmutableMap.Builder<K, V> resultBuilder = ImmutableMap.builder();
/*  789 */       K[] localKeys = this.keys;
/*  790 */       for (int i = 0; i < localInjectors.length; i++) {
/*  791 */         SingleParameterInjector<V> injector = localInjectors[i];
/*  792 */         K key = localKeys[i];
/*      */         
/*  794 */         V value = injector.inject(context);
/*      */         
/*  796 */         if (value == null) {
/*  797 */           throw RealMapBinder.createNullValueException(key, (Binding)this.bindingSelection.getMapBindings().get(key));
/*      */         }
/*      */         
/*  800 */         resultBuilder.put(key, value);
/*      */       } 
/*      */       
/*  803 */       return (Map<K, V>)resultBuilder.build();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Dependency<?>> getDependencies() {
/*  808 */       return this.dependencies;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <B, W> W acceptExtensionVisitor(BindingTargetVisitor<B, W> visitor, ProviderInstanceBinding<? extends B> binding) {
/*  815 */       if (visitor instanceof MultibindingsTargetVisitor) {
/*  816 */         return (W)((MultibindingsTargetVisitor)visitor).visit(this);
/*      */       }
/*  818 */       return (W)visitor.visit(binding);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Key<Map<K, V>> getMapKey() {
/*  824 */       return this.bindingSelection.getMapKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Key<?>> getAlternateMapKeys() {
/*  829 */       return (Set<Key<?>>)ImmutableSet.of(this.bindingSelection
/*  830 */           .getJavaxProviderMapKey(), this.bindingSelection
/*  831 */           .getProviderMapKey(), this.bindingSelection
/*  832 */           .getProviderSetMultimapKey(), this.bindingSelection
/*  833 */           .getJavaxProviderSetMultimapKey(), this.bindingSelection
/*  834 */           .getProviderCollectionMultimapKey(), this.bindingSelection
/*  835 */           .getJavaxProviderCollectionMultimapKey(), (Object[])new Key[] {
/*  836 */             RealMapBinder.BindingSelection.access$2200(this.bindingSelection), 
/*  837 */             RealMapBinder.BindingSelection.access$1200(this.bindingSelection)
/*      */           });
/*      */     }
/*      */     
/*      */     public TypeLiteral<K> getKeyTypeLiteral() {
/*  842 */       return this.bindingSelection.getKeyType();
/*      */     }
/*      */ 
/*      */     
/*      */     public TypeLiteral<V> getValueTypeLiteral() {
/*  847 */       return this.bindingSelection.getValueType();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public List<Map.Entry<?, Binding<?>>> getEntries() {
/*  853 */       if (this.bindingSelection.isInitialized()) {
/*  854 */         return (List<Map.Entry<?, Binding<?>>>)this.bindingSelection.getEntries();
/*      */       }
/*  856 */       throw new UnsupportedOperationException("getEntries() not supported for module bindings");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public List<Map.Entry<?, Binding<?>>> getEntries(Iterable<? extends Element> elements) {
/*  865 */       ImmutableMultimap.Builder<K, Key<V>> keyToValueKeyBuilder = ImmutableMultimap.builder();
/*  866 */       ImmutableMap.Builder<Key<V>, Binding<V>> valueKeyToBindingBuilder = ImmutableMap.builder();
/*  867 */       ImmutableMap.Builder<Key<V>, K> valueKeyToKeyBuilder = ImmutableMap.builder();
/*      */       
/*  869 */       ImmutableMap.Builder<Key<V>, Binding<Map.Entry<K, Provider<V>>>> valueKeyToEntryBindingBuilder = ImmutableMap.builder();
/*  870 */       for (Element element : elements) {
/*  871 */         if (element instanceof Binding) {
/*  872 */           Binding<?> binding = (Binding)element;
/*  873 */           if (this.bindingSelection.matchesValueKey(binding.getKey()) && binding
/*  874 */             .getKey().getTypeLiteral().equals(this.bindingSelection.valueType)) {
/*      */ 
/*      */             
/*  877 */             Binding<V> typedBinding = (Binding)binding;
/*  878 */             Key<V> typedKey = typedBinding.getKey();
/*  879 */             valueKeyToBindingBuilder.put(typedKey, typedBinding);
/*      */           } 
/*      */         } 
/*      */         
/*  883 */         if (element instanceof ProviderInstanceBinding && this.bindingSelection
/*  884 */           .getEntrySetBinder().containsElement(element)) {
/*      */ 
/*      */           
/*  887 */           ProviderInstanceBinding<Map.Entry<K, Provider<V>>> entryBinding = (ProviderInstanceBinding<Map.Entry<K, Provider<V>>>)element;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  893 */           Provider<Map.Entry<K, Provider<V>>> typedProvider = (Provider<Map.Entry<K, Provider<V>>>)entryBinding.getUserSuppliedProvider();
/*  894 */           Provider<Map.Entry<K, Provider<V>>> userSuppliedProvider = typedProvider;
/*      */           
/*  896 */           if (userSuppliedProvider instanceof RealMapBinder.ProviderMapEntry) {
/*      */ 
/*      */             
/*  899 */             RealMapBinder.ProviderMapEntry<K, V> typedUserSuppliedProvider = (RealMapBinder.ProviderMapEntry)userSuppliedProvider;
/*      */             
/*  901 */             RealMapBinder.ProviderMapEntry<K, V> entry = typedUserSuppliedProvider;
/*      */             
/*  903 */             keyToValueKeyBuilder.put(entry.getKey(), entry.getValueKey());
/*  904 */             valueKeyToEntryBindingBuilder.put(entry.getValueKey(), entryBinding);
/*  905 */             valueKeyToKeyBuilder.put(entry.getValueKey(), entry.getKey());
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  910 */       ImmutableMultimap<K, Key<V>> keyToValueKey = keyToValueKeyBuilder.build();
/*  911 */       ImmutableMap<Key<V>, K> valueKeyToKey = valueKeyToKeyBuilder.build();
/*  912 */       ImmutableMap<Key<V>, Binding<V>> valueKeyToBinding = valueKeyToBindingBuilder.build();
/*      */       
/*  914 */       ImmutableMap<Key<V>, Binding<Map.Entry<K, Provider<V>>>> valueKeyToEntryBinding = valueKeyToEntryBindingBuilder.build();
/*      */ 
/*      */ 
/*      */       
/*  918 */       Set<Key<V>> keysFromProviderMapEntrys = Sets.newHashSet((Iterable)keyToValueKey.values());
/*  919 */       ImmutableSet immutableSet = valueKeyToBinding.keySet();
/*      */       
/*  921 */       if (!keysFromProviderMapEntrys.equals(immutableSet)) {
/*      */         
/*  923 */         Sets.SetView setView1 = Sets.difference(keysFromProviderMapEntrys, (Set)immutableSet);
/*      */         
/*  925 */         Sets.SetView setView2 = Sets.difference((Set)immutableSet, keysFromProviderMapEntrys);
/*      */         
/*  927 */         StringBuilder sb = new StringBuilder("Expected a 1:1 mapping from map keys to values.");
/*      */         
/*  929 */         if (!setView2.isEmpty()) {
/*  930 */           sb.append(
/*  931 */               Errors.format("%nFound these Bindings that were missing an associated entry:%n", new Object[0]));
/*  932 */           for (Key<V> key : (Iterable<Key<V>>)setView2) {
/*  933 */             sb.append(
/*  934 */                 Errors.format("  %s bound at: %s%n", new Object[] { key, ((Binding)valueKeyToBinding.get(key)).getSource() }));
/*      */           } 
/*      */         } 
/*      */         
/*  938 */         if (!setView1.isEmpty()) {
/*  939 */           sb.append(Errors.format("%nFound these map keys without a corresponding value:%n", new Object[0]));
/*  940 */           for (Key<V> key : (Iterable<Key<V>>)setView1) {
/*  941 */             sb.append(
/*  942 */                 Errors.format("  '%s' bound at: %s%n", new Object[] {
/*      */                     
/*  944 */                     valueKeyToKey.get(key), ((Binding)valueKeyToEntryBinding.get(key)).getSource()
/*      */                   }));
/*      */           } 
/*      */         } 
/*  948 */         throw new IllegalArgumentException(sb.toString());
/*      */       } 
/*      */ 
/*      */       
/*  952 */       ImmutableList.Builder<Map.Entry<?, Binding<?>>> resultBuilder = ImmutableList.builder();
/*  953 */       for (UnmodifiableIterator<Map.Entry<K, Key<V>>> unmodifiableIterator = keyToValueKey.entries().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<K, Key<V>> entry = unmodifiableIterator.next();
/*  954 */         Binding<?> binding = (Binding)valueKeyToBinding.get(entry.getValue());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  960 */         Map.Entry<?, Binding<?>> newEntry = Maps.immutableEntry(entry.getKey(), binding);
/*  961 */         resultBuilder.add(newEntry); }
/*      */       
/*  963 */       return (List<Map.Entry<?, Binding<?>>>)resultBuilder.build();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean permitsDuplicates() {
/*  968 */       if (this.bindingSelection.isInitialized()) {
/*  969 */         return this.bindingSelection.permitsDuplicates();
/*      */       }
/*  971 */       throw new UnsupportedOperationException("permitsDuplicates() not supported for module bindings");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean containsElement(Element element) {
/*  978 */       return this.bindingSelection.containsElement(element);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class MultimapBinder<K, V>
/*      */     implements Module
/*      */   {
/*      */     private final RealMapBinder.BindingSelection<K, V> bindingSelection;
/*      */ 
/*      */     
/*      */     private MultimapBinder(RealMapBinder.BindingSelection<K, V> bindingSelection) {
/*  991 */       this.bindingSelection = bindingSelection;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void configure(Binder binder) {
/*  998 */       Provider<Map<K, Set<Provider<V>>>> multimapProvider = new RealProviderMultimapProvider<>(this.bindingSelection.getMapKey());
/*  999 */       binder.bind(this.bindingSelection.getProviderSetMultimapKey()).toProvider(multimapProvider);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1006 */       Provider<Map<K, Set<Provider<V>>>> provider1 = multimapProvider;
/* 1007 */       binder.bind(this.bindingSelection.getJavaxProviderSetMultimapKey()).toProvider(provider1);
/*      */ 
/*      */       
/* 1010 */       Provider<Map<K, Set<Provider<V>>>> provider2 = multimapProvider;
/* 1011 */       binder
/* 1012 */         .bind(this.bindingSelection.getProviderCollectionMultimapKey())
/* 1013 */         .toProvider(provider2);
/*      */ 
/*      */       
/* 1016 */       Provider<Map<K, Set<Provider<V>>>> provider3 = multimapProvider;
/*      */       
/* 1018 */       binder
/* 1019 */         .bind(this.bindingSelection.getJavaxProviderCollectionMultimapKey())
/* 1020 */         .toProvider(provider3);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1025 */       Provider<Map<K, Set<V>>> realMultimapProvider = new RealMultimapProvider<>(this.bindingSelection.getMapKey());
/* 1026 */       binder.bind(this.bindingSelection.getMultimapKey()).toProvider(realMultimapProvider);
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1031 */       return this.bindingSelection.hashCode();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/* 1036 */       return (o instanceof MultimapBinder && ((MultimapBinder)o).bindingSelection
/* 1037 */         .equals(this.bindingSelection));
/*      */     }
/*      */     
/*      */     private static final class RealProviderMultimapProvider<K, V>
/*      */       extends RealMapBinder.RealMultimapBinderProviderWithDependencies<K, V, Map<K, Set<Provider<V>>>> {
/*      */       private Map<K, Set<Provider<V>>> multimapOfProviders;
/* 1043 */       private Set<Dependency<?>> dependencies = (Set<Dependency<?>>)RealMapBinder.MODULE_DEPENDENCIES;
/*      */       
/*      */       private RealProviderMultimapProvider(Key<Map<K, V>> mapKey) {
/* 1046 */         super(mapKey);
/*      */       }
/*      */ 
/*      */       
/*      */       public Set<Dependency<?>> getDependencies() {
/* 1051 */         return this.dependencies;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       protected void doInitialize(InjectorImpl injector, Errors errors) {
/* 1057 */         ImmutableMap.Builder<K, Set<Provider<V>>> multimapOfProvidersBuilder = ImmutableMap.builder();
/* 1058 */         ImmutableSet.Builder<Dependency<?>> dependenciesBuilder = ImmutableSet.builder();
/*      */         
/* 1060 */         for (UnmodifiableIterator<Map.Entry<K, Set<Binding<V>>>> unmodifiableIterator = this.bindingSelection.getMultimapBindings().entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<K, Set<Binding<V>>> entry = unmodifiableIterator.next();
/* 1061 */           ImmutableSet.Builder<Provider<V>> providersBuilder = ImmutableSet.builder();
/* 1062 */           for (Binding<V> binding : entry.getValue()) {
/* 1063 */             providersBuilder.add(binding.getProvider());
/* 1064 */             dependenciesBuilder.add(Dependency.get(RealMapBinder.getKeyOfProvider(binding.getKey())));
/*      */           } 
/*      */           
/* 1067 */           multimapOfProvidersBuilder.put(entry.getKey(), providersBuilder.build()); }
/*      */         
/* 1069 */         this.multimapOfProviders = (Map<K, Set<Provider<V>>>)multimapOfProvidersBuilder.build();
/* 1070 */         this.dependencies = (Set<Dependency<?>>)dependenciesBuilder.build();
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       protected Map<K, Set<Provider<V>>> doProvision(InternalContext context, Dependency<?> dependency) {
/* 1076 */         return this.multimapOfProviders;
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     private static final class RealMultimapProvider<K, V>
/*      */       extends RealMapBinder.RealMultimapBinderProviderWithDependencies<K, V, Map<K, Set<V>>>
/*      */     {
/*      */       private static final class PerKeyData<K, V>
/*      */       {
/*      */         private final K key;
/*      */         
/*      */         private final Binding<V>[] bindings;
/*      */         
/*      */         private final SingleParameterInjector<V>[] injectors;
/*      */ 
/*      */         
/*      */         private PerKeyData(K key, Binding<V>[] bindings, SingleParameterInjector<V>[] injectors) {
/* 1094 */           Preconditions.checkArgument((bindings.length == injectors.length));
/*      */           
/* 1096 */           this.key = key;
/* 1097 */           this.bindings = bindings;
/* 1098 */           this.injectors = injectors;
/*      */         }
/*      */       }
/*      */       
/* 1102 */       private Set<Dependency<?>> dependencies = (Set<Dependency<?>>)RealMapBinder.MODULE_DEPENDENCIES;
/*      */       
/*      */       private PerKeyData<K, V>[] perKeyDatas;
/*      */       
/*      */       private RealMultimapProvider(Key<Map<K, V>> mapKey) {
/* 1107 */         super(mapKey);
/*      */       }
/*      */ 
/*      */       
/*      */       public Set<Dependency<?>> getDependencies() {
/* 1112 */         return this.dependencies;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       protected void doInitialize(InjectorImpl injector, Errors errors) throws ErrorsException {
/* 1119 */         PerKeyData[] arrayOfPerKeyData = new PerKeyData[this.bindingSelection.getMapBindings().size()];
/* 1120 */         this.perKeyDatas = (PerKeyData<K, V>[])arrayOfPerKeyData;
/* 1121 */         ImmutableSet.Builder<Dependency<?>> dependenciesBuilder = ImmutableSet.builder();
/* 1122 */         List<Dependency<?>> dependenciesForKey = Lists.newArrayList();
/* 1123 */         int i = 0;
/*      */         
/* 1125 */         for (UnmodifiableIterator<Map.Entry<K, Set<Binding<V>>>> unmodifiableIterator = this.bindingSelection.getMultimapBindings().entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<K, Set<Binding<V>>> entry = unmodifiableIterator.next();
/*      */           
/* 1127 */           dependenciesForKey.clear();
/*      */           
/* 1129 */           Set<Binding<V>> bindings = entry.getValue();
/*      */           
/* 1131 */           Binding[] arrayOfBinding1 = new Binding[bindings.size()];
/* 1132 */           Binding[] arrayOfBinding2 = arrayOfBinding1;
/* 1133 */           int j = 0;
/* 1134 */           for (Binding<V> binding : bindings) {
/* 1135 */             Dependency<V> dependency = Dependency.get(binding.getKey());
/* 1136 */             dependenciesBuilder.add(dependency);
/* 1137 */             dependenciesForKey.add(dependency);
/* 1138 */             arrayOfBinding2[j] = binding;
/* 1139 */             j++;
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1145 */           SingleParameterInjector[] arrayOfSingleParameterInjector = (SingleParameterInjector[])injector.getParametersInjectors(dependenciesForKey, errors);
/*      */           
/* 1147 */           this.perKeyDatas[i] = new PerKeyData<>(entry.getKey(), arrayOfBinding2, arrayOfSingleParameterInjector);
/* 1148 */           i++; }
/*      */ 
/*      */         
/* 1151 */         this.dependencies = (Set<Dependency<?>>)dependenciesBuilder.build();
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       protected Map<K, Set<V>> doProvision(InternalContext context, Dependency<?> dependency) throws InternalProvisionException {
/* 1157 */         ImmutableMap.Builder<K, Set<V>> resultBuilder = ImmutableMap.builder();
/*      */         
/* 1159 */         for (PerKeyData<K, V> perKeyData : this.perKeyDatas) {
/* 1160 */           ImmutableSet.Builder<V> bindingsBuilder = ImmutableSet.builder();
/* 1161 */           SingleParameterInjector[] arrayOfSingleParameterInjector = perKeyData.injectors;
/* 1162 */           for (int i = 0; i < arrayOfSingleParameterInjector.length; i++) {
/* 1163 */             SingleParameterInjector<V> injector = arrayOfSingleParameterInjector[i];
/* 1164 */             V value = injector.inject(context);
/*      */             
/* 1166 */             if (value == null) {
/* 1167 */               throw RealMapBinder.createNullValueException(perKeyData.key, perKeyData.bindings[i]);
/*      */             }
/*      */             
/* 1170 */             bindingsBuilder.add(value);
/*      */           } 
/*      */           
/* 1173 */           resultBuilder.put(perKeyData.key, bindingsBuilder.build());
/*      */         } 
/*      */         
/* 1176 */         return (Map<K, Set<V>>)resultBuilder.build();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static final class ProviderMapEntry<K, V>
/*      */     extends InternalProviderInstanceBindingImpl.Factory<Map.Entry<K, Provider<V>>>
/*      */   {
/*      */     private final K key;
/*      */     private final Key<V> valueKey;
/*      */     private Map.Entry<K, Provider<V>> entry;
/*      */     
/*      */     ProviderMapEntry(K key, Key<V> valueKey) {
/* 1190 */       super(InternalProviderInstanceBindingImpl.InitializationTiming.EAGER);
/* 1191 */       this.key = key;
/* 1192 */       this.valueKey = valueKey;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Dependency<?>> getDependencies() {
/* 1198 */       return (Set<Dependency<?>>)ImmutableSet.of(Dependency.get(RealMapBinder.getKeyOfProvider(this.valueKey)));
/*      */     }
/*      */ 
/*      */     
/*      */     void initialize(InjectorImpl injector, Errors errors) {
/* 1203 */       Binding<V> valueBinding = injector.getExistingBinding(this.valueKey);
/* 1204 */       this.entry = Maps.immutableEntry(this.key, valueBinding.getProvider());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     protected Map.Entry<K, Provider<V>> doProvision(InternalContext context, Dependency<?> dependency) {
/* 1210 */       return this.entry;
/*      */     }
/*      */     
/*      */     K getKey() {
/* 1214 */       return this.key;
/*      */     }
/*      */     
/*      */     Key<V> getValueKey() {
/* 1218 */       return this.valueKey;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/* 1223 */       if (obj instanceof ProviderMapEntry) {
/* 1224 */         ProviderMapEntry<?, ?> o = (ProviderMapEntry<?, ?>)obj;
/* 1225 */         return (this.key.equals(o.key) && this.valueKey.equals(o.valueKey));
/*      */       } 
/* 1227 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1232 */       return Objects.hashCode(new Object[] { this.key, this.valueKey });
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1237 */       String str1 = String.valueOf(this.key), str2 = String.valueOf(this.valueKey); return (new StringBuilder(20 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("ProviderMapEntry(").append(str1).append(", ").append(str2).append(")").toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static abstract class RealMapBinderProviderWithDependencies<K, V, P>
/*      */     extends InternalProviderInstanceBindingImpl.Factory<P>
/*      */   {
/*      */     final RealMapBinder.BindingSelection<K, V> bindingSelection;
/*      */ 
/*      */ 
/*      */     
/*      */     private RealMapBinderProviderWithDependencies(RealMapBinder.BindingSelection<K, V> bindingSelection) {
/* 1252 */       super(InternalProviderInstanceBindingImpl.InitializationTiming.DELAYED);
/*      */       
/* 1254 */       this.bindingSelection = bindingSelection;
/*      */     }
/*      */ 
/*      */     
/*      */     final void initialize(InjectorImpl injector, Errors errors) throws ErrorsException {
/* 1259 */       if (this.bindingSelection.tryInitialize(injector, errors)) {
/* 1260 */         doInitialize(injector, errors);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/* 1273 */       return (obj != null && 
/* 1274 */         getClass() == obj.getClass() && this.bindingSelection
/* 1275 */         .equals(((RealMapBinderProviderWithDependencies)obj).bindingSelection));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1281 */       return this.bindingSelection.hashCode();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected abstract void doInitialize(InjectorImpl param1InjectorImpl, Errors param1Errors) throws ErrorsException;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static abstract class RealMultimapBinderProviderWithDependencies<K, V, P>
/*      */     extends InternalProviderInstanceBindingImpl.Factory<P>
/*      */   {
/*      */     final Key<Map<K, V>> mapKey;
/*      */ 
/*      */     
/*      */     RealMapBinder.BindingSelection<K, V> bindingSelection;
/*      */ 
/*      */ 
/*      */     
/*      */     private RealMultimapBinderProviderWithDependencies(Key<Map<K, V>> mapKey) {
/* 1304 */       super(InternalProviderInstanceBindingImpl.InitializationTiming.DELAYED);
/*      */       
/* 1306 */       this.mapKey = mapKey;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final void initialize(InjectorImpl injector, Errors errors) throws ErrorsException {
/* 1316 */       Binding<Map<K, V>> mapBinding = injector.getExistingBinding(this.mapKey);
/* 1317 */       ProviderInstanceBinding<Map<K, V>> providerInstanceBinding = (ProviderInstanceBinding<Map<K, V>>)mapBinding;
/*      */ 
/*      */ 
/*      */       
/* 1321 */       RealMapBinder.RealMapProvider<K, V> mapProvider = (RealMapBinder.RealMapProvider<K, V>)providerInstanceBinding.getUserSuppliedProvider();
/*      */       
/* 1323 */       this.bindingSelection = mapProvider.getBindingSelection();
/*      */       
/* 1325 */       if (this.bindingSelection.tryInitialize(injector, errors)) {
/* 1326 */         doInitialize(injector, errors);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/* 1338 */       return (obj != null && 
/* 1339 */         getClass() == obj.getClass() && this.mapKey
/* 1340 */         .equals(((RealMultimapBinderProviderWithDependencies)obj).mapKey));
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1345 */       return this.mapKey.hashCode();
/*      */     }
/*      */     
/*      */     abstract void doInitialize(InjectorImpl param1InjectorImpl, Errors param1Errors) throws ErrorsException; }
/*      */   
/*      */   private static <K, V> InternalProvisionException createNullValueException(K key, Binding<V> binding) {
/* 1351 */     return InternalProvisionException.create(ErrorId.NULL_VALUE_IN_MAP, "Map injection failed due to null value for key \"%s\", bound at: %s", new Object[] { key, binding
/*      */ 
/*      */ 
/*      */           
/* 1355 */           .getSource() });
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\RealMapBinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */