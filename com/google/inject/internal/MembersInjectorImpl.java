/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.MembersInjector;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.spi.InjectionListener;
/*     */ import com.google.inject.spi.InjectionPoint;
/*     */ import javax.annotation.Nullable;
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
/*     */ final class MembersInjectorImpl<T>
/*     */   implements MembersInjector<T>
/*     */ {
/*     */   private final TypeLiteral<T> typeLiteral;
/*     */   private final InjectorImpl injector;
/*     */   @Nullable
/*     */   private final ImmutableList<SingleMemberInjector> memberInjectors;
/*     */   @Nullable
/*     */   private final ImmutableList<MembersInjector<? super T>> userMembersInjectors;
/*     */   @Nullable
/*     */   private final ImmutableList<InjectionListener<? super T>> injectionListeners;
/*     */   @Nullable
/*     */   private final ImmutableList<MethodAspect> addedAspects;
/*     */   
/*     */   MembersInjectorImpl(InjectorImpl injector, TypeLiteral<T> typeLiteral, EncounterImpl<T> encounter, ImmutableList<SingleMemberInjector> memberInjectors) {
/*  49 */     this.injector = injector;
/*  50 */     this.typeLiteral = typeLiteral;
/*  51 */     this.memberInjectors = memberInjectors.isEmpty() ? null : memberInjectors;
/*  52 */     this
/*  53 */       .userMembersInjectors = encounter.getMembersInjectors().isEmpty() ? null : encounter.getMembersInjectors().asList();
/*  54 */     this
/*     */ 
/*     */       
/*  57 */       .injectionListeners = encounter.getInjectionListeners().isEmpty() ? null : encounter.getInjectionListeners().asList();
/*  58 */     this
/*     */ 
/*     */       
/*  61 */       .addedAspects = (InternalFlags.isBytecodeGenEnabled() && !encounter.getAspects().isEmpty()) ? encounter.getAspects() : null;
/*     */   }
/*     */   
/*     */   public ImmutableList<SingleMemberInjector> getMemberInjectors() {
/*  65 */     return (this.memberInjectors == null) ? ImmutableList.of() : this.memberInjectors;
/*     */   }
/*     */ 
/*     */   
/*     */   public void injectMembers(T instance) {
/*  70 */     TypeLiteral<T> localTypeLiteral = this.typeLiteral;
/*     */     try {
/*  72 */       injectAndNotify(instance, null, null, localTypeLiteral, false);
/*  73 */     } catch (InternalProvisionException ipe) {
/*  74 */       throw ipe.addSource(localTypeLiteral).toProvisionException();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void injectAndNotify(final T instance, Key<T> key, ProvisionListenerStackCallback<T> provisionCallback, Object source, final boolean toolableOnly) throws InternalProvisionException {
/*  85 */     if (instance == null) {
/*     */       return;
/*     */     }
/*  88 */     final InternalContext context = this.injector.enterContext();
/*     */     try {
/*  90 */       if (provisionCallback != null && provisionCallback.hasListeners()) {
/*  91 */         provisionCallback.provision(context, new ProvisionListenerStackCallback.ProvisionCallback<T>()
/*     */             {
/*     */               
/*     */               public T call() throws InternalProvisionException
/*     */               {
/*  96 */                 MembersInjectorImpl.this.injectMembers(instance, context, toolableOnly);
/*  97 */                 return (T)instance;
/*     */               }
/*     */             });
/*     */       } else {
/* 101 */         injectMembers(instance, context, toolableOnly);
/*     */       } 
/*     */     } finally {
/* 104 */       context.close();
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
/* 115 */     if (!toolableOnly) {
/* 116 */       notifyListeners(instance);
/*     */     }
/*     */   }
/*     */   
/*     */   void notifyListeners(T instance) throws InternalProvisionException {
/* 121 */     ImmutableList<InjectionListener<? super T>> localInjectionListeners = this.injectionListeners;
/* 122 */     if (localInjectionListeners == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 127 */     for (int i = 0; i < localInjectionListeners.size(); i++) {
/* 128 */       InjectionListener<? super T> injectionListener = (InjectionListener<? super T>)localInjectionListeners.get(i);
/*     */       try {
/* 130 */         injectionListener.afterInjection(instance);
/* 131 */       } catch (RuntimeException e) {
/* 132 */         throw InternalProvisionException.errorNotifyingInjectionListener(injectionListener, this.typeLiteral, e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void injectMembers(T t, InternalContext context, boolean toolableOnly) throws InternalProvisionException {
/* 140 */     ImmutableList<SingleMemberInjector> localMembersInjectors = this.memberInjectors;
/* 141 */     if (localMembersInjectors != null)
/*     */     {
/* 143 */       for (int i = 0, size = localMembersInjectors.size(); i < size; i++) {
/* 144 */         SingleMemberInjector injector = (SingleMemberInjector)localMembersInjectors.get(i);
/* 145 */         if (!toolableOnly || injector.getInjectionPoint().isToolable()) {
/* 146 */           injector.inject(context, t);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 152 */     if (!toolableOnly) {
/* 153 */       ImmutableList<MembersInjector<? super T>> localUsersMembersInjectors = this.userMembersInjectors;
/* 154 */       if (localUsersMembersInjectors != null)
/*     */       {
/* 156 */         for (int i = 0; i < localUsersMembersInjectors.size(); i++) {
/* 157 */           MembersInjector<? super T> userMembersInjector = (MembersInjector<? super T>)localUsersMembersInjectors.get(i);
/*     */           try {
/* 159 */             userMembersInjector.injectMembers(t);
/* 160 */           } catch (RuntimeException e) {
/* 161 */             throw InternalProvisionException.errorInUserInjector(userMembersInjector, this.typeLiteral, e);
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 171 */     String str = String.valueOf(this.typeLiteral); return (new StringBuilder(17 + String.valueOf(str).length())).append("MembersInjector<").append(str).append(">").toString();
/*     */   }
/*     */   
/*     */   public ImmutableSet<InjectionPoint> getInjectionPoints() {
/* 175 */     ImmutableList<SingleMemberInjector> localMemberInjectors = this.memberInjectors;
/* 176 */     if (localMemberInjectors != null) {
/* 177 */       ImmutableSet.Builder<InjectionPoint> builder = ImmutableSet.builder();
/* 178 */       for (UnmodifiableIterator<SingleMemberInjector> unmodifiableIterator = localMemberInjectors.iterator(); unmodifiableIterator.hasNext(); ) { SingleMemberInjector memberInjector = unmodifiableIterator.next();
/* 179 */         builder.add(memberInjector.getInjectionPoint()); }
/*     */       
/* 181 */       return builder.build();
/*     */     } 
/* 183 */     return ImmutableSet.of();
/*     */   }
/*     */   
/*     */   public ImmutableList<MethodAspect> getAddedAspects() {
/* 187 */     return (this.addedAspects == null) ? ImmutableList.of() : this.addedAspects;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\MembersInjectorImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */