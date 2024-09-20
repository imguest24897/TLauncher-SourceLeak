/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.ListMultimap;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.Provider;
/*     */ import com.google.inject.ProvisionException;
/*     */ import com.google.inject.Scope;
/*     */ import com.google.inject.Scopes;
/*     */ import com.google.inject.spi.Dependency;
/*     */ import com.google.inject.spi.Message;
/*     */ import java.util.Formatter;
/*     */ import java.util.List;
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
/*     */ public class SingletonScope
/*     */   implements Scope
/*     */ {
/*  66 */   private static final Object NULL = new Object();
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
/*  77 */   private static final CycleDetectingLock.CycleDetectingLockFactory<Key<?>> cycleDetectingLockFactory = new CycleDetectingLock.CycleDetectingLockFactory<>();
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
/*     */   public <T> Provider<T> scope(final Key<T> key, final Provider<T> creator) {
/* 100 */     return new Provider<T>(this)
/*     */       {
/*     */         volatile Object instance;
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
/* 115 */         final ConstructionContext<T> constructionContext = new ConstructionContext<>();
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
/* 128 */         final CycleDetectingLock<Key<?>> creationLock = SingletonScope.cycleDetectingLockFactory.create(key);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         @Nullable
/*     */         final InjectorImpl injector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public T get() {
/* 149 */           Object initialInstance = this.instance;
/* 150 */           if (initialInstance == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 157 */             InternalContext context = (this.injector == null) ? null : this.injector.getLocalContext();
/*     */ 
/*     */             
/* 160 */             ListMultimap<Thread, Key<?>> locksCycle = this.creationLock.lockOrDetectPotentialLocksCycle();
/*     */             
/* 162 */             if (locksCycle.isEmpty()) {
/*     */ 
/*     */               
/*     */               try {
/* 166 */                 if (this.instance == null)
/*     */                 {
/*     */                   
/* 169 */                   T provided = (T)creator.get();
/* 170 */                   Object providedNotNull = (provided == null) ? SingletonScope.NULL : provided;
/*     */ 
/*     */                   
/* 173 */                   if (this.instance == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */                     
/* 178 */                     if (Scopes.isCircularProxy(provided)) {
/* 179 */                       return provided;
/*     */                     }
/*     */                     
/* 182 */                     synchronized (this.constructionContext) {
/*     */                       
/* 184 */                       this.instance = providedNotNull;
/* 185 */                       this.constructionContext.setProxyDelegates(provided);
/*     */                     } 
/*     */                   } else {
/*     */                     
/* 189 */                     Preconditions.checkState((this.instance == providedNotNull), "Singleton is called recursively returning different results");
/*     */                   }
/*     */                 
/*     */                 }
/*     */               
/* 194 */               } catch (RuntimeException e) {
/*     */ 
/*     */                 
/* 197 */                 synchronized (this.constructionContext) {
/* 198 */                   this.constructionContext.finishConstruction();
/*     */                 } 
/* 200 */                 throw e;
/*     */               } finally {
/*     */                 
/* 203 */                 this.creationLock.unlock();
/*     */               } 
/*     */             } else {
/* 206 */               if (context == null) {
/* 207 */                 throw new ProvisionException(
/* 208 */                     ImmutableList.of(createCycleDependenciesMessage(locksCycle, null)));
/*     */               }
/*     */               
/* 211 */               synchronized (this.constructionContext) {
/*     */                 
/* 213 */                 if (this.instance == null) {
/*     */ 
/*     */                   
/* 216 */                   Dependency<?> dependency = (Dependency)Preconditions.checkNotNull(context
/* 217 */                       .getDependency(), "internalContext.getDependency()");
/* 218 */                   Class<?> rawType = dependency.getKey().getTypeLiteral().getRawType();
/*     */ 
/*     */ 
/*     */                   
/*     */                   try {
/* 223 */                     T proxy = (T)this.constructionContext.createProxy(context.getInjectorOptions(), rawType);
/* 224 */                     return proxy;
/* 225 */                   } catch (InternalProvisionException e) {
/*     */                     
/* 227 */                     Message proxyCreationError = (Message)Iterables.getOnlyElement((Iterable)e.getErrors());
/*     */                     
/* 229 */                     Message cycleDependenciesMessage = createCycleDependenciesMessage(locksCycle, proxyCreationError);
/*     */                     
/* 231 */                     throw new ProvisionException(
/* 232 */                         ImmutableList.of(cycleDependenciesMessage, proxyCreationError));
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 242 */             Object initializedInstance = this.instance;
/* 243 */             Preconditions.checkState((initializedInstance != null), "Internal error: Singleton is not initialized contrary to our expectations");
/*     */ 
/*     */ 
/*     */             
/* 247 */             T initializedTypedInstance = (T)initializedInstance;
/* 248 */             return (initializedInstance == SingletonScope.NULL) ? null : initializedTypedInstance;
/*     */           } 
/*     */ 
/*     */           
/* 252 */           T typedInitialIntance = (T)initialInstance;
/* 253 */           return (initialInstance == SingletonScope.NULL) ? null : typedInitialIntance;
/*     */         }
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
/*     */         private Message createCycleDependenciesMessage(ListMultimap<Thread, Key<?>> locksCycle, @Nullable Message proxyCreationError) {
/* 278 */           StringBuilder sb = new StringBuilder();
/* 279 */           Formatter fmt = new Formatter(sb);
/* 280 */           fmt.format("Encountered circular dependency spanning several threads.", new Object[0]);
/* 281 */           if (proxyCreationError != null) {
/* 282 */             fmt.format(" %s", new Object[] { proxyCreationError.getMessage() });
/*     */           }
/* 284 */           fmt.format("%n", new Object[0]);
/* 285 */           for (Thread lockedThread : locksCycle.keySet()) {
/* 286 */             List<Key<?>> lockedKeys = locksCycle.get(lockedThread);
/* 287 */             fmt.format("%s is holding locks the following singletons in the cycle:%n", new Object[] { lockedThread });
/* 288 */             for (Key<?> lockedKey : lockedKeys) {
/* 289 */               fmt.format("%s%n", new Object[] { Errors.convert(lockedKey) });
/*     */             } 
/* 291 */             for (StackTraceElement traceElement : lockedThread.getStackTrace()) {
/* 292 */               fmt.format("\tat %s%n", new Object[] { traceElement });
/*     */             } 
/*     */           } 
/* 295 */           fmt.close();
/* 296 */           return new Message(Thread.currentThread(), sb.toString());
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 301 */           return String.format("%s[%s]", new Object[] { this.val$creator, Scopes.SINGLETON });
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 308 */     return "Scopes.SINGLETON";
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\SingletonScope.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */