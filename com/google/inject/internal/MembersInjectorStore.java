/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableListMultimap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.inject.ConfigurationException;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.spi.InjectionPoint;
/*     */ import com.google.inject.spi.TypeListener;
/*     */ import com.google.inject.spi.TypeListenerBinding;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
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
/*     */ final class MembersInjectorStore
/*     */ {
/*     */   private final InjectorImpl injector;
/*     */   private final ImmutableList<TypeListenerBinding> typeListenerBindings;
/*     */   
/*  44 */   private final FailableCache<TypeLiteral<?>, MembersInjectorImpl<?>> cache = new FailableCache<TypeLiteral<?>, MembersInjectorImpl<?>>()
/*     */     {
/*     */       
/*     */       protected MembersInjectorImpl<?> create(TypeLiteral<?> type, Errors errors) throws ErrorsException
/*     */       {
/*  49 */         return MembersInjectorStore.this.createWithListeners((TypeLiteral)type, errors);
/*     */       }
/*     */     };
/*     */   
/*     */   MembersInjectorStore(InjectorImpl injector, List<TypeListenerBinding> typeListenerBindings) {
/*  54 */     this.injector = injector;
/*  55 */     this.typeListenerBindings = ImmutableList.copyOf(typeListenerBindings);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasTypeListeners() {
/*  63 */     return !this.typeListenerBindings.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> MembersInjectorImpl<T> get(TypeLiteral<T> key, Errors errors) throws ErrorsException {
/*  69 */     return (MembersInjectorImpl<T>)this.cache.get(key, errors);
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
/*     */   boolean remove(TypeLiteral<?> type) {
/*  82 */     return this.cache.remove(type);
/*     */   }
/*     */ 
/*     */   
/*     */   private <T> MembersInjectorImpl<T> createWithListeners(TypeLiteral<T> type, Errors errors) throws ErrorsException {
/*     */     Set<InjectionPoint> injectionPoints;
/*  88 */     int numErrorsBefore = errors.size();
/*     */ 
/*     */     
/*     */     try {
/*  92 */       injectionPoints = InjectionPoint.forInstanceMethodsAndFields(type);
/*  93 */     } catch (ConfigurationException e) {
/*  94 */       errors.merge(e.getErrorMessages());
/*  95 */       injectionPoints = (Set<InjectionPoint>)e.getPartialValue();
/*     */     } 
/*  97 */     ImmutableList<SingleMemberInjector> injectors = getInjectors(injectionPoints, errors);
/*  98 */     errors.throwIfNewErrors(numErrorsBefore);
/*     */     
/* 100 */     EncounterImpl<T> encounter = new EncounterImpl<>(errors, this.injector.lookups);
/* 101 */     Set<TypeListener> alreadySeenListeners = Sets.newHashSet();
/* 102 */     for (UnmodifiableIterator<TypeListenerBinding> unmodifiableIterator = this.typeListenerBindings.iterator(); unmodifiableIterator.hasNext(); ) { TypeListenerBinding binding = unmodifiableIterator.next();
/* 103 */       TypeListener typeListener = binding.getListener();
/* 104 */       if (!alreadySeenListeners.contains(typeListener) && binding.getTypeMatcher().matches(type)) {
/* 105 */         alreadySeenListeners.add(typeListener);
/*     */         try {
/* 107 */           typeListener.hear(type, encounter);
/* 108 */         } catch (RuntimeException e) {
/* 109 */           errors.errorNotifyingTypeListener(binding, type, e);
/*     */         } 
/*     */       }  }
/*     */     
/* 113 */     encounter.invalidate();
/* 114 */     errors.throwIfNewErrors(numErrorsBefore);
/*     */     
/* 116 */     return new MembersInjectorImpl<>(this.injector, type, encounter, injectors);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableList<SingleMemberInjector> getInjectors(Set<InjectionPoint> injectionPoints, Errors errors) {
/* 122 */     List<SingleMemberInjector> injectors = Lists.newArrayList();
/* 123 */     for (InjectionPoint injectionPoint : injectionPoints) {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/* 128 */         Errors errorsForMember = injectionPoint.isOptional() ? new Errors(injectionPoint) : errors.withSource(injectionPoint);
/*     */ 
/*     */ 
/*     */         
/* 132 */         SingleMemberInjector injector = (injectionPoint.getMember() instanceof java.lang.reflect.Field) ? new SingleFieldInjector(this.injector, injectionPoint, errorsForMember) : new SingleMethodInjector(this.injector, injectionPoint, errorsForMember);
/* 133 */         injectors.add(injector);
/* 134 */       } catch (ErrorsException errorsException) {}
/*     */     } 
/*     */ 
/*     */     
/* 138 */     return ImmutableList.copyOf(injectors);
/*     */   }
/*     */   
/*     */   ImmutableListMultimap<TypeLiteral<?>, InjectionPoint> getAllInjectionPoints() {
/* 142 */     return (ImmutableListMultimap<TypeLiteral<?>, InjectionPoint>)this.cache.asMap().entrySet().stream()
/* 143 */       .collect(
/* 144 */         ImmutableListMultimap.flatteningToImmutableListMultimap(Map.Entry::getKey, entry -> ((MembersInjectorImpl)entry.getValue()).getInjectionPoints().stream()));
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\MembersInjectorStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */