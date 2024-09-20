/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.inject.spi.ErrorDetail;
/*    */ import java.io.Serializable;
/*    */ import java.util.Formatter;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class GenericErrorDetail
/*    */   extends InternalErrorDetail<GenericErrorDetail>
/*    */   implements Serializable
/*    */ {
/*    */   public GenericErrorDetail(ErrorId errorId, String message, List<Object> sources, Throwable cause) {
/* 17 */     super(errorId, (String)Preconditions.checkNotNull(message, "message"), sources, cause);
/*    */   }
/*    */ 
/*    */   
/*    */   public void formatDetail(List<ErrorDetail<?>> mergeableErrors, Formatter formatter) {
/* 22 */     Preconditions.checkArgument(mergeableErrors.isEmpty(), "Unexpected mergeable errors");
/* 23 */     List<Object> dependencies = getSources();
/* 24 */     for (Object source : Lists.reverse(dependencies)) {
/* 25 */       formatter.format("  ", new Object[0]);
/* 26 */       (new SourceFormatter(source, formatter, false)).format();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public GenericErrorDetail withSources(List<Object> newSources) {
/* 32 */     return new GenericErrorDetail(this.errorId, getMessage(), newSources, getCause());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\GenericErrorDetail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */