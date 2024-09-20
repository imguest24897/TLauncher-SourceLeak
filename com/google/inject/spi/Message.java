/*     */ package com.google.inject.spi;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.inject.Binder;
/*     */ import com.google.inject.internal.ErrorId;
/*     */ import com.google.inject.internal.Errors;
/*     */ import com.google.inject.internal.GenericErrorDetail;
/*     */ import com.google.inject.internal.GuiceInternal;
/*     */ import com.google.inject.internal.util.SourceProvider;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ import java.util.List;
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
/*     */ public final class Message
/*     */   implements Serializable, Element
/*     */ {
/*     */   private final ErrorId errorId;
/*     */   private final ErrorDetail<?> errorDetail;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public Message(GuiceInternal internalOnly, ErrorId errorId, ErrorDetail<?> errorDetail) {
/*  53 */     Preconditions.checkNotNull(internalOnly);
/*  54 */     this.errorId = errorId;
/*  55 */     this.errorDetail = errorDetail;
/*     */   }
/*     */   
/*     */   private Message(ErrorId errorId, ErrorDetail<?> errorDetail) {
/*  59 */     this.errorId = errorId;
/*  60 */     this.errorDetail = errorDetail;
/*     */   }
/*     */ 
/*     */   
/*     */   public Message(ErrorId errorId, List<Object> sources, String message, Throwable cause) {
/*  65 */     this.errorId = errorId;
/*  66 */     this.errorDetail = (ErrorDetail<?>)new GenericErrorDetail(errorId, message, sources, cause);
/*     */   }
/*     */ 
/*     */   
/*     */   public Message(List<Object> sources, String message, Throwable cause) {
/*  71 */     this(ErrorId.OTHER, sources, message, cause);
/*     */   }
/*     */ 
/*     */   
/*     */   public Message(String message, Throwable cause) {
/*  76 */     this((List<Object>)ImmutableList.of(), message, cause);
/*     */   }
/*     */   
/*     */   public Message(Object source, String message) {
/*  80 */     this((List<Object>)ImmutableList.of(source), message, (Throwable)null);
/*     */   }
/*     */   
/*     */   public Message(String message) {
/*  84 */     this((List<Object>)ImmutableList.of(), message, (Throwable)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ErrorDetail<?> getErrorDetail() {
/*  93 */     return this.errorDetail;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSource() {
/*  98 */     List<Object> sources = this.errorDetail.getSources();
/*  99 */     return sources.isEmpty() ? 
/* 100 */       SourceProvider.UNKNOWN_SOURCE.toString() : 
/* 101 */       Errors.convert(Iterables.getLast(sources)).toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Object> getSources() {
/* 106 */     return this.errorDetail.getSources();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMessage() {
/* 111 */     return this.errorDetail.getMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T acceptVisitor(ElementVisitor<T> visitor) {
/* 117 */     return visitor.visit(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getCause() {
/* 127 */     return this.errorDetail.getCause();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 132 */     return this.errorDetail.getMessage();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 137 */     return this.errorDetail.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 142 */     if (!(o instanceof Message)) {
/* 143 */       return false;
/*     */     }
/* 145 */     Message e = (Message)o;
/* 146 */     return this.errorDetail.equals(e.errorDetail);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyTo(Binder binder) {
/* 152 */     binder.withSource(getSource()).addError(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message withSource(List<Object> newSources) {
/* 161 */     return new Message(this.errorId, (ErrorDetail<?>)this.errorDetail.withSources(newSources));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object writeReplace() throws ObjectStreamException {
/* 170 */     Object[] sourcesAsStrings = getSources().toArray();
/* 171 */     for (int i = 0; i < sourcesAsStrings.length; i++) {
/* 172 */       sourcesAsStrings[i] = Errors.convert(sourcesAsStrings[i]).toString();
/*     */     }
/* 174 */     return new Message(this.errorId, (ErrorDetail<?>)new GenericErrorDetail(this.errorId, 
/*     */ 
/*     */           
/* 177 */           getMessage(), (List)ImmutableList.copyOf(sourcesAsStrings), getCause()));
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\Message.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */