/*     */ package org.apache.commons.lang3.concurrent;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutorService;
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
/*     */ public class MultiBackgroundInitializer
/*     */   extends BackgroundInitializer<MultiBackgroundInitializer.MultiBackgroundInitializerResults>
/*     */ {
/* 101 */   private final Map<String, BackgroundInitializer<?>> childInitializers = new HashMap<>();
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
/*     */   public MultiBackgroundInitializer(ExecutorService exec) {
/* 117 */     super(exec);
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
/*     */   public void addInitializer(String name, BackgroundInitializer<?> backgroundInitializer) {
/* 134 */     Objects.requireNonNull(name, "name");
/* 135 */     Objects.requireNonNull(backgroundInitializer, "backgroundInitializer");
/*     */     
/* 137 */     synchronized (this) {
/* 138 */       if (isStarted()) {
/* 139 */         throw new IllegalStateException("addInitializer() must not be called after start()!");
/*     */       }
/* 141 */       this.childInitializers.put(name, backgroundInitializer);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getTaskCount() {
/* 157 */     return 1 + this.childInitializers.values().stream().mapToInt(BackgroundInitializer::getTaskCount).sum();
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
/*     */   protected MultiBackgroundInitializerResults initialize() throws Exception {
/*     */     Map<String, BackgroundInitializer<?>> inits;
/* 173 */     synchronized (this) {
/*     */       
/* 175 */       inits = new HashMap<>(this.childInitializers);
/*     */     } 
/*     */ 
/*     */     
/* 179 */     ExecutorService exec = getActiveExecutor();
/* 180 */     inits.values().forEach(bi -> {
/*     */           if (bi.getExternalExecutor() == null) {
/*     */             bi.setExternalExecutor(exec);
/*     */           }
/*     */ 
/*     */           
/*     */           bi.start();
/*     */         });
/*     */     
/* 189 */     Map<String, Object> results = new HashMap<>();
/* 190 */     Map<String, ConcurrentException> excepts = new HashMap<>();
/* 191 */     inits.forEach((k, v) -> {
/*     */           try {
/*     */             results.put(k, v.get());
/* 194 */           } catch (ConcurrentException cex) {
/*     */             excepts.put(k, cex);
/*     */           } 
/*     */         });
/*     */     
/* 199 */     return new MultiBackgroundInitializerResults(inits, results, excepts);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultiBackgroundInitializer() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class MultiBackgroundInitializerResults
/*     */   {
/*     */     private final Map<String, BackgroundInitializer<?>> initializers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Map<String, Object> resultObjects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Map<String, ConcurrentException> exceptions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private MultiBackgroundInitializerResults(Map<String, BackgroundInitializer<?>> inits, Map<String, Object> results, Map<String, ConcurrentException> excepts) {
/* 235 */       this.initializers = inits;
/* 236 */       this.resultObjects = results;
/* 237 */       this.exceptions = excepts;
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
/*     */     public BackgroundInitializer<?> getInitializer(String name) {
/* 249 */       return checkName(name);
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object getResultObject(String name) {
/* 265 */       checkName(name);
/* 266 */       return this.resultObjects.get(name);
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
/*     */     public boolean isException(String name) {
/* 278 */       checkName(name);
/* 279 */       return this.exceptions.containsKey(name);
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
/*     */ 
/*     */     
/*     */     public ConcurrentException getException(String name) {
/* 293 */       checkName(name);
/* 294 */       return this.exceptions.get(name);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Set<String> initializerNames() {
/* 305 */       return Collections.unmodifiableSet(this.initializers.keySet());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isSuccessful() {
/* 315 */       return this.exceptions.isEmpty();
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
/*     */     
/*     */     private BackgroundInitializer<?> checkName(String name) {
/* 328 */       BackgroundInitializer<?> init = this.initializers.get(name);
/* 329 */       if (init == null) {
/* 330 */         throw new NoSuchElementException("No child initializer with name " + name);
/*     */       }
/*     */ 
/*     */       
/* 334 */       return init;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\concurrent\MultiBackgroundInitializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */