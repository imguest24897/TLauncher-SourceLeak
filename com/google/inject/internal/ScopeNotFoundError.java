/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.inject.spi.ErrorDetail;
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Formatter;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ 
/*    */ final class ScopeNotFoundError
/*    */   extends InternalErrorDetail<ScopeNotFoundError> {
/*    */   private final Class<? extends Annotation> scopeAnnotation;
/*    */   
/*    */   ScopeNotFoundError(Class<? extends Annotation> scopeAnnotation, List<Object> sources) {
/* 16 */     super(ErrorId.SCOPE_NOT_FOUND, 
/*    */         
/* 18 */         String.format("No scope is bound to %s.", new Object[] { Messages.convert(scopeAnnotation) }), sources, (Throwable)null);
/*    */ 
/*    */     
/* 21 */     this.scopeAnnotation = scopeAnnotation;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isMergeable(ErrorDetail<?> other) {
/* 26 */     return (other instanceof ScopeNotFoundError && ((ScopeNotFoundError)other).scopeAnnotation
/* 27 */       .equals(this.scopeAnnotation));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void formatDetail(List<ErrorDetail<?>> mergeableErrors, Formatter formatter) {
/* 32 */     List<List<Object>> sourcesSet = new ArrayList<>();
/* 33 */     sourcesSet.add(getSources());
/* 34 */     Objects.requireNonNull(sourcesSet); mergeableErrors.stream().map(ErrorDetail::getSources).forEach(sourcesSet::add);
/*    */     
/* 36 */     formatter.format("%n%s%n", new Object[] { "Used at:" });
/* 37 */     int sourceListIndex = 1;
/* 38 */     for (List<Object> sources : sourcesSet) {
/* 39 */       ErrorFormatter.formatSources(sourceListIndex++, Lists.reverse(sources), formatter);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public ScopeNotFoundError withSources(List<Object> newSources) {
/* 45 */     return new ScopeNotFoundError(this.scopeAnnotation, newSources);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ScopeNotFoundError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */