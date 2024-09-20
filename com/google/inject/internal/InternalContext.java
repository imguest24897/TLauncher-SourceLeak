/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.inject.spi.Dependency;
/*     */ import java.util.IdentityHashMap;
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
/*     */ final class InternalContext
/*     */   implements AutoCloseable
/*     */ {
/*     */   private final InjectorImpl.InjectorOptions options;
/*  32 */   private final IdentityHashMap<Object, ConstructionContext<?>> constructionContexts = new IdentityHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Dependency<?> dependency;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int enterCount;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Object[] toClear;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   InternalContext(InjectorImpl.InjectorOptions options, Object[] toClear) {
/*  52 */     this.options = options;
/*  53 */     this.toClear = toClear;
/*  54 */     this.enterCount = 1;
/*     */   }
/*     */ 
/*     */   
/*     */   void enter() {
/*  59 */     this.enterCount++;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/*  65 */     int newCount = --this.enterCount;
/*  66 */     if (newCount < 0) {
/*  67 */       throw new IllegalStateException("Called close() too many times");
/*     */     }
/*  69 */     if (newCount == 0) {
/*  70 */       this.toClear[0] = null;
/*     */     }
/*     */   }
/*     */   
/*     */   InjectorImpl.InjectorOptions getInjectorOptions() {
/*  75 */     return this.options;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   <T> ConstructionContext<T> getConstructionContext(Object key) {
/*  81 */     ConstructionContext<T> constructionContext = (ConstructionContext<T>)this.constructionContexts.get(key);
/*  82 */     if (constructionContext == null) {
/*  83 */       constructionContext = new ConstructionContext<>();
/*  84 */       this.constructionContexts.put(key, constructionContext);
/*     */     } 
/*  86 */     return constructionContext;
/*     */   }
/*     */   
/*     */   Dependency<?> getDependency() {
/*  90 */     return this.dependency;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setDependency(Dependency<?> dependency) {
/* 100 */     this.dependency = dependency;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\InternalContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */