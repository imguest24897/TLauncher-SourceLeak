/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.collect.ArrayListMultimap;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ListMultimap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.inject.Binding;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.Scope;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.spi.InjectionRequest;
/*     */ import com.google.inject.spi.MembersInjectorLookup;
/*     */ import com.google.inject.spi.ModuleAnnotatedMethodScannerBinding;
/*     */ import com.google.inject.spi.ProviderLookup;
/*     */ import com.google.inject.spi.ProvisionListenerBinding;
/*     */ import com.google.inject.spi.ScopeBinding;
/*     */ import com.google.inject.spi.StaticInjectionRequest;
/*     */ import com.google.inject.spi.TypeConverterBinding;
/*     */ import com.google.inject.spi.TypeListenerBinding;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
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
/*     */ class InjectorBindingData
/*     */ {
/*     */   private final Optional<InjectorBindingData> parent;
/*  59 */   private final Map<Key<?>, Binding<?>> explicitBindingsMutable = Maps.newLinkedHashMap();
/*     */   
/*  61 */   private final Map<Key<?>, Binding<?>> explicitBindings = Collections.unmodifiableMap(this.explicitBindingsMutable);
/*  62 */   private final Map<Class<? extends Annotation>, ScopeBinding> scopes = Maps.newHashMap();
/*  63 */   private final Set<ProviderLookup<?>> providerLookups = Sets.newLinkedHashSet();
/*  64 */   private final Set<StaticInjectionRequest> staticInjectionRequests = Sets.newLinkedHashSet();
/*  65 */   private final Set<MembersInjectorLookup<?>> membersInjectorLookups = Sets.newLinkedHashSet();
/*  66 */   private final Set<InjectionRequest<?>> injectionRequests = Sets.newLinkedHashSet();
/*  67 */   private final List<TypeConverterBinding> converters = Lists.newArrayList();
/*  68 */   private final List<MethodAspect> methodAspects = Lists.newArrayList();
/*  69 */   private final List<TypeListenerBinding> typeListenerBindings = Lists.newArrayList();
/*  70 */   private final List<ProvisionListenerBinding> provisionListenerBindings = Lists.newArrayList();
/*  71 */   private final List<ModuleAnnotatedMethodScannerBinding> scannerBindings = Lists.newArrayList();
/*     */ 
/*     */   
/*  74 */   private final ListMultimap<TypeLiteral<?>, Binding<?>> indexedExplicitBindings = (ListMultimap<TypeLiteral<?>, Binding<?>>)ArrayListMultimap.create();
/*     */   
/*     */   InjectorBindingData(Optional<InjectorBindingData> parent) {
/*  77 */     this.parent = parent;
/*     */   }
/*     */   
/*     */   public Optional<InjectorBindingData> parent() {
/*  81 */     return this.parent;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> BindingImpl<T> getExplicitBinding(Key<T> key) {
/*  86 */     Binding<?> binding = this.explicitBindings.get(key);
/*  87 */     if (binding == null && this.parent.isPresent()) {
/*  88 */       return ((InjectorBindingData)this.parent.get()).getExplicitBinding(key);
/*     */     }
/*  90 */     return (BindingImpl)binding;
/*     */   }
/*     */   
/*     */   public Map<Key<?>, Binding<?>> getExplicitBindingsThisLevel() {
/*  94 */     return this.explicitBindings;
/*     */   }
/*     */   
/*     */   public void putBinding(Key<?> key, BindingImpl<?> binding) {
/*  98 */     this.explicitBindingsMutable.put(key, binding);
/*     */   }
/*     */   
/*     */   public void putProviderLookup(ProviderLookup<?> lookup) {
/* 102 */     this.providerLookups.add(lookup);
/*     */   }
/*     */   
/*     */   public Set<ProviderLookup<?>> getProviderLookupsThisLevel() {
/* 106 */     return this.providerLookups;
/*     */   }
/*     */   
/*     */   public void putStaticInjectionRequest(StaticInjectionRequest staticInjectionRequest) {
/* 110 */     this.staticInjectionRequests.add(staticInjectionRequest);
/*     */   }
/*     */   
/*     */   public Set<StaticInjectionRequest> getStaticInjectionRequestsThisLevel() {
/* 114 */     return this.staticInjectionRequests;
/*     */   }
/*     */   
/*     */   public void putInjectionRequest(InjectionRequest<?> injectionRequest) {
/* 118 */     this.injectionRequests.add(injectionRequest);
/*     */   }
/*     */   
/*     */   public Set<InjectionRequest<?>> getInjectionRequestsThisLevel() {
/* 122 */     return this.injectionRequests;
/*     */   }
/*     */   
/*     */   public void putMembersInjectorLookup(MembersInjectorLookup<?> membersInjectorLookup) {
/* 126 */     this.membersInjectorLookups.add(membersInjectorLookup);
/*     */   }
/*     */   
/*     */   public Set<MembersInjectorLookup<?>> getMembersInjectorLookupsThisLevel() {
/* 130 */     return this.membersInjectorLookups;
/*     */   }
/*     */   
/*     */   public ScopeBinding getScopeBinding(Class<? extends Annotation> annotationType) {
/* 134 */     ScopeBinding scopeBinding = this.scopes.get(annotationType);
/* 135 */     if (scopeBinding == null && this.parent.isPresent()) {
/* 136 */       return ((InjectorBindingData)this.parent.get()).getScopeBinding(annotationType);
/*     */     }
/* 138 */     return scopeBinding;
/*     */   }
/*     */   
/*     */   public void putScopeBinding(Class<? extends Annotation> annotationType, ScopeBinding scope) {
/* 142 */     this.scopes.put(annotationType, scope);
/*     */   }
/*     */   
/*     */   public Collection<ScopeBinding> getScopeBindingsThisLevel() {
/* 146 */     return this.scopes.values();
/*     */   }
/*     */   
/*     */   public Iterable<TypeConverterBinding> getConvertersThisLevel() {
/* 150 */     return this.converters;
/*     */   }
/*     */   
/*     */   public void addConverter(TypeConverterBinding typeConverterBinding) {
/* 154 */     this.converters.add(typeConverterBinding);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeConverterBinding getConverter(String stringValue, TypeLiteral<?> type, Errors errors, Object source) {
/* 159 */     TypeConverterBinding matchingConverter = null;
/* 160 */     InjectorBindingData b = this;
/* 161 */     while (b != null) {
/* 162 */       for (TypeConverterBinding converter : b.getConvertersThisLevel()) {
/* 163 */         if (converter.getTypeMatcher().matches(type)) {
/* 164 */           if (matchingConverter != null) {
/* 165 */             errors.ambiguousTypeConversion(stringValue, source, type, matchingConverter, converter);
/*     */           }
/* 167 */           matchingConverter = converter;
/*     */         } 
/*     */       } 
/* 170 */       b = b.parent().orElse(null);
/*     */     } 
/* 172 */     return matchingConverter;
/*     */   }
/*     */   
/*     */   public void addMethodAspect(MethodAspect methodAspect) {
/* 176 */     this.methodAspects.add(methodAspect);
/*     */   }
/*     */   
/*     */   public ImmutableList<MethodAspect> getMethodAspects() {
/* 180 */     if (this.parent.isPresent()) {
/* 181 */       return (new ImmutableList.Builder())
/* 182 */         .addAll((Iterable)((InjectorBindingData)this.parent.get()).getMethodAspects())
/* 183 */         .addAll(this.methodAspects)
/* 184 */         .build();
/*     */     }
/* 186 */     return ImmutableList.copyOf(this.methodAspects);
/*     */   }
/*     */   
/*     */   public void addTypeListener(TypeListenerBinding listenerBinding) {
/* 190 */     this.typeListenerBindings.add(listenerBinding);
/*     */   }
/*     */   
/*     */   public ImmutableList<TypeListenerBinding> getTypeListenerBindings() {
/* 194 */     if (this.parent.isPresent()) {
/* 195 */       return (new ImmutableList.Builder())
/* 196 */         .addAll((Iterable)((InjectorBindingData)this.parent.get()).getTypeListenerBindings())
/* 197 */         .addAll(this.typeListenerBindings)
/* 198 */         .build();
/*     */     }
/* 200 */     return ImmutableList.copyOf(this.typeListenerBindings);
/*     */   }
/*     */   
/*     */   public ImmutableList<TypeListenerBinding> getTypeListenerBindingsThisLevel() {
/* 204 */     return ImmutableList.copyOf(this.typeListenerBindings);
/*     */   }
/*     */   
/*     */   public void addProvisionListener(ProvisionListenerBinding listenerBinding) {
/* 208 */     this.provisionListenerBindings.add(listenerBinding);
/*     */   }
/*     */   
/*     */   public ImmutableList<ProvisionListenerBinding> getProvisionListenerBindings() {
/* 212 */     if (this.parent.isPresent()) {
/* 213 */       return (new ImmutableList.Builder())
/* 214 */         .addAll((Iterable)((InjectorBindingData)this.parent.get()).getProvisionListenerBindings())
/* 215 */         .addAll(this.provisionListenerBindings)
/* 216 */         .build();
/*     */     }
/* 218 */     return ImmutableList.copyOf(this.provisionListenerBindings);
/*     */   }
/*     */   
/*     */   public ImmutableList<ProvisionListenerBinding> getProvisionListenerBindingsThisLevel() {
/* 222 */     return ImmutableList.copyOf(this.provisionListenerBindings);
/*     */   }
/*     */   
/*     */   public void addScanner(ModuleAnnotatedMethodScannerBinding scanner) {
/* 226 */     this.scannerBindings.add(scanner);
/*     */   }
/*     */   
/*     */   public ImmutableList<ModuleAnnotatedMethodScannerBinding> getScannerBindings() {
/* 230 */     if (this.parent.isPresent()) {
/* 231 */       return (new ImmutableList.Builder())
/* 232 */         .addAll((Iterable)((InjectorBindingData)this.parent.get()).getScannerBindings())
/* 233 */         .addAll(this.scannerBindings)
/* 234 */         .build();
/*     */     }
/* 236 */     return ImmutableList.copyOf(this.scannerBindings);
/*     */   }
/*     */   
/*     */   public ImmutableList<ModuleAnnotatedMethodScannerBinding> getScannerBindingsThisLevel() {
/* 240 */     return ImmutableList.copyOf(this.scannerBindings);
/*     */   }
/*     */   
/*     */   public Map<Class<? extends Annotation>, Scope> getScopes() {
/* 244 */     ImmutableMap.Builder<Class<? extends Annotation>, Scope> builder = ImmutableMap.builder();
/* 245 */     for (Map.Entry<Class<? extends Annotation>, ScopeBinding> entry : this.scopes.entrySet()) {
/* 246 */       builder.put(entry.getKey(), ((ScopeBinding)entry.getValue()).getScope());
/*     */     }
/* 248 */     return (Map<Class<? extends Annotation>, Scope>)builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void indexBindingsByType() {
/* 256 */     for (Binding<?> binding : getExplicitBindingsThisLevel().values()) {
/* 257 */       this.indexedExplicitBindings.put(binding.getKey().getTypeLiteral(), binding);
/*     */     }
/*     */   }
/*     */   
/*     */   public ListMultimap<TypeLiteral<?>, Binding<?>> getIndexedExplicitBindings() {
/* 262 */     return this.indexedExplicitBindings;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\InjectorBindingData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */