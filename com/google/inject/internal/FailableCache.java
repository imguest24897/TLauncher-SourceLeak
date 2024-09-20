/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.cache.CacheBuilder;
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import com.google.common.cache.LoadingCache;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class FailableCache<K, V>
/*    */ {
/* 35 */   private final LoadingCache<K, Object> delegate = CacheBuilder.newBuilder()
/* 36 */     .build(new CacheLoader<K, Object>()
/*    */       {
/*    */         public Object load(K key)
/*    */         {
/* 40 */           Errors errors = new Errors();
/* 41 */           V result = null;
/*    */           try {
/* 43 */             result = FailableCache.this.create(key, errors);
/* 44 */           } catch (ErrorsException e) {
/* 45 */             errors.merge(e.getErrors());
/*    */           } 
/* 47 */           return errors.hasErrors() ? errors : result;
/*    */         }
/*    */       });
/*    */ 
/*    */ 
/*    */   
/*    */   public V get(K key, Errors errors) throws ErrorsException {
/* 54 */     Object resultOrError = this.delegate.getUnchecked(key);
/* 55 */     if (resultOrError instanceof Errors) {
/* 56 */       errors.merge((Errors)resultOrError);
/* 57 */       throw errors.toException();
/*    */     } 
/*    */     
/* 60 */     V result = (V)resultOrError;
/* 61 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   boolean remove(K key) {
/* 66 */     return (this.delegate.asMap().remove(key) != null);
/*    */   }
/*    */   
/*    */   Map<K, V> asMap() {
/* 70 */     return Maps.transformValues(
/* 71 */         Maps.filterValues(
/* 72 */           (Map)ImmutableMap.copyOf(this.delegate.asMap()), resultOrError -> !(resultOrError instanceof Errors)), resultOrError -> resultOrError);
/*    */   }
/*    */   
/*    */   protected abstract V create(K paramK, Errors paramErrors) throws ErrorsException;
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\FailableCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */