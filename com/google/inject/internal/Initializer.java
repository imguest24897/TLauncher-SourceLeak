/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ListMultimap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.inject.Binding;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.Stage;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.spi.InjectionPoint;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.List;
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
/*     */ final class Initializer
/*     */ {
/*     */   private volatile boolean validationStarted = false;
/*  51 */   private final CycleDetectingLock.CycleDetectingLockFactory<Class<?>> cycleDetectingLockFactory = new CycleDetectingLock.CycleDetectingLockFactory<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   private final List<InjectableReference<?>> pendingInjections = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   private final IdentityHashMap<Object, InjectableReference<?>> initializablesCache = Maps.newIdentityHashMap();
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
/*     */   <T> Initializable<T> requestInjection(InjectorImpl injector, T instance, Binding<T> binding, Object source, Set<InjectionPoint> injectionPoints) {
/*  82 */     Preconditions.checkNotNull(source);
/*  83 */     Preconditions.checkState(!this.validationStarted, "Member injection could not be requested after validation is started");
/*     */ 
/*     */     
/*  86 */     ProvisionListenerStackCallback<T> provisionCallback = (binding == null) ? null : injector.provisionListenerStore.<T>get(binding);
/*     */ 
/*     */     
/*  89 */     if (instance == null || (injectionPoints
/*  90 */       .isEmpty() && 
/*  91 */       !injector.membersInjectorStore.hasTypeListeners() && provisionCallback == null))
/*     */     {
/*  93 */       return Initializables.of(instance);
/*     */     }
/*     */     
/*  96 */     if (this.initializablesCache.containsKey(instance)) {
/*     */       
/*  98 */       Initializable<T> cached = (Initializable<T>)this.initializablesCache.get(instance);
/*  99 */       return cached;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     InjectableReference<T> injectableReference = new InjectableReference<>(injector, instance, (binding == null) ? null : binding.getKey(), provisionCallback, source, this.cycleDetectingLockFactory.create(instance.getClass()));
/* 110 */     this.initializablesCache.put(instance, injectableReference);
/* 111 */     this.pendingInjections.add(injectableReference);
/* 112 */     return injectableReference;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void validateOustandingInjections(Errors errors) {
/* 120 */     this.validationStarted = true;
/* 121 */     this.initializablesCache.clear();
/* 122 */     for (InjectableReference<?> reference : this.pendingInjections) {
/*     */       try {
/* 124 */         reference.validate(errors);
/* 125 */       } catch (ErrorsException e) {
/* 126 */         errors.merge(e.getErrors());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void injectAll(Errors errors) {
/* 137 */     Preconditions.checkState(this.validationStarted, "Validation should be done before injection");
/* 138 */     for (InjectableReference<?> reference : this.pendingInjections) {
/*     */       try {
/* 140 */         reference.get();
/* 141 */       } catch (InternalProvisionException ipe) {
/* 142 */         errors.merge(ipe);
/*     */       } 
/*     */     } 
/* 145 */     this.pendingInjections.clear();
/*     */   }
/*     */   
/*     */   private enum InjectableReferenceState {
/* 149 */     NEW,
/* 150 */     VALIDATED,
/* 151 */     INJECTING,
/* 152 */     READY;
/*     */   }
/*     */   
/*     */   private static class InjectableReference<T> implements Initializable<T> {
/* 156 */     private volatile Initializer.InjectableReferenceState state = Initializer.InjectableReferenceState.NEW;
/* 157 */     private volatile MembersInjectorImpl<T> membersInjector = null;
/*     */     
/*     */     private final InjectorImpl injector;
/*     */     
/*     */     private final T instance;
/*     */     
/*     */     private final Object source;
/*     */     
/*     */     private final Key<T> key;
/*     */     
/*     */     private final ProvisionListenerStackCallback<T> provisionCallback;
/*     */     
/*     */     private final CycleDetectingLock<?> lock;
/*     */ 
/*     */     
/*     */     public InjectableReference(InjectorImpl injector, T instance, Key<T> key, ProvisionListenerStackCallback<T> provisionCallback, Object source, CycleDetectingLock<?> lock) {
/* 173 */       this.injector = injector;
/* 174 */       this.key = key;
/* 175 */       this.provisionCallback = provisionCallback;
/* 176 */       this.instance = (T)Preconditions.checkNotNull(instance, "instance");
/* 177 */       this.source = Preconditions.checkNotNull(source, "source");
/* 178 */       this.lock = (CycleDetectingLock)Preconditions.checkNotNull(lock, "lock");
/*     */     }
/*     */ 
/*     */     
/*     */     public void validate(Errors errors) throws ErrorsException {
/* 183 */       TypeLiteral<T> type = TypeLiteral.get(this.instance.getClass());
/* 184 */       this.membersInjector = this.injector.membersInjectorStore.get(type, errors.withSource(this.source));
/* 185 */       Preconditions.checkNotNull(this.membersInjector, "No membersInjector available for instance: %s, from key: %s", this.instance, this.key);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 190 */       this.state = Initializer.InjectableReferenceState.VALIDATED;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public T get() throws InternalProvisionException {
/* 200 */       if (this.state == Initializer.InjectableReferenceState.READY) {
/* 201 */         return this.instance;
/*     */       }
/*     */ 
/*     */       
/* 205 */       ListMultimap<Thread, ?> listMultimap = this.lock.lockOrDetectPotentialLocksCycle();
/* 206 */       if (!listMultimap.isEmpty())
/*     */       {
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
/* 221 */         return this.instance; } 
/*     */       try {
/*     */         T t;
/*     */         String str;
/* 225 */         switch (this.state) {
/*     */           case READY:
/* 227 */             t = this.instance; return t;
/*     */ 
/*     */ 
/*     */           
/*     */           case INJECTING:
/* 232 */             t = this.instance; return t;
/*     */           case VALIDATED:
/* 234 */             this.state = Initializer.InjectableReferenceState.INJECTING;
/*     */             break;
/*     */           case NEW:
/* 237 */             throw new IllegalStateException("InjectableReference is not validated yet");
/*     */           default:
/* 239 */             str = String.valueOf(this.state); throw new IllegalStateException((new StringBuilder(15 + String.valueOf(str).length())).append("Unknown state: ").append(str).toString());
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 245 */           this.membersInjector.injectAndNotify(this.instance, this.key, this.provisionCallback, this.source, (this.injector.options.stage == Stage.TOOL));
/*     */         }
/* 247 */         catch (InternalProvisionException ipe) {
/* 248 */           throw ipe.addSource(this.source);
/*     */         } 
/*     */         
/* 251 */         this.state = Initializer.InjectableReferenceState.READY;
/* 252 */         return this.instance;
/*     */       } finally {
/*     */         
/* 255 */         this.lock.unlock();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 261 */       return this.instance.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\Initializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */