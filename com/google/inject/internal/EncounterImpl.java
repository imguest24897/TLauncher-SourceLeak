/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.MembersInjector;
/*     */ import com.google.inject.Provider;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.matcher.Matcher;
/*     */ import com.google.inject.matcher.Matchers;
/*     */ import com.google.inject.spi.InjectionListener;
/*     */ import com.google.inject.spi.Message;
/*     */ import com.google.inject.spi.TypeEncounter;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.List;
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
/*     */ final class EncounterImpl<T>
/*     */   implements TypeEncounter<T>
/*     */ {
/*     */   private final Errors errors;
/*     */   private final Lookups lookups;
/*     */   private List<MembersInjector<? super T>> membersInjectors;
/*     */   private List<InjectionListener<? super T>> injectionListeners;
/*     */   private List<MethodAspect> aspects;
/*     */   private boolean valid = true;
/*     */   
/*     */   EncounterImpl(Errors errors, Lookups lookups) {
/*  47 */     this.errors = errors;
/*  48 */     this.lookups = lookups;
/*     */   }
/*     */   
/*     */   void invalidate() {
/*  52 */     this.valid = false;
/*     */   }
/*     */   
/*     */   ImmutableList<MethodAspect> getAspects() {
/*  56 */     return (this.aspects == null) ? ImmutableList.of() : ImmutableList.copyOf(this.aspects);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bindInterceptor(Matcher<? super Method> methodMatcher, MethodInterceptor... interceptors) {
/*  63 */     Preconditions.checkState(this.valid, "Encounters may not be used after hear() returns.");
/*     */ 
/*     */     
/*  66 */     if (this.aspects == null) {
/*  67 */       this.aspects = Lists.newArrayList();
/*     */     }
/*     */     
/*  70 */     this.aspects.add(new MethodAspect(Matchers.any(), methodMatcher, interceptors));
/*     */   }
/*     */   
/*     */   ImmutableSet<MembersInjector<? super T>> getMembersInjectors() {
/*  74 */     return (this.membersInjectors == null) ? 
/*  75 */       ImmutableSet.of() : 
/*  76 */       ImmutableSet.copyOf(this.membersInjectors);
/*     */   }
/*     */   
/*     */   ImmutableSet<InjectionListener<? super T>> getInjectionListeners() {
/*  80 */     return (this.injectionListeners == null) ? 
/*  81 */       ImmutableSet.of() : 
/*  82 */       ImmutableSet.copyOf(this.injectionListeners);
/*     */   }
/*     */ 
/*     */   
/*     */   public void register(MembersInjector<? super T> membersInjector) {
/*  87 */     Preconditions.checkState(this.valid, "Encounters may not be used after hear() returns.");
/*     */     
/*  89 */     if (this.membersInjectors == null) {
/*  90 */       this.membersInjectors = Lists.newArrayList();
/*     */     }
/*     */     
/*  93 */     this.membersInjectors.add(membersInjector);
/*     */   }
/*     */ 
/*     */   
/*     */   public void register(InjectionListener<? super T> injectionListener) {
/*  98 */     Preconditions.checkState(this.valid, "Encounters may not be used after hear() returns.");
/*     */     
/* 100 */     if (this.injectionListeners == null) {
/* 101 */       this.injectionListeners = Lists.newArrayList();
/*     */     }
/*     */     
/* 104 */     this.injectionListeners.add(injectionListener);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addError(String message, Object... arguments) {
/* 109 */     Preconditions.checkState(this.valid, "Encounters may not be used after hear() returns.");
/* 110 */     this.errors.addMessage(message, arguments);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addError(Throwable t) {
/* 115 */     Preconditions.checkState(this.valid, "Encounters may not be used after hear() returns.");
/* 116 */     this.errors.errorInUserCode(t, "An exception was caught and reported. Message: %s", new Object[] { t.getMessage() });
/*     */   }
/*     */ 
/*     */   
/*     */   public void addError(Message message) {
/* 121 */     Preconditions.checkState(this.valid, "Encounters may not be used after hear() returns.");
/* 122 */     this.errors.addMessage(message);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Provider<T> getProvider(Key<T> key) {
/* 127 */     Preconditions.checkState(this.valid, "Encounters may not be used after hear() returns.");
/* 128 */     return this.lookups.getProvider(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Provider<T> getProvider(Class<T> type) {
/* 133 */     return getProvider(Key.get(type));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral) {
/* 138 */     Preconditions.checkState(this.valid, "Encounters may not be used after hear() returns.");
/* 139 */     return this.lookups.getMembersInjector(typeLiteral);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> MembersInjector<T> getMembersInjector(Class<T> type) {
/* 144 */     return getMembersInjector(TypeLiteral.get(type));
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\EncounterImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */