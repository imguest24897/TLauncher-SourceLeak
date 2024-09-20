/*     */ package com.google.inject.spi;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.inject.internal.Messages;
/*     */ import java.io.Serializable;
/*     */ import java.util.Formatter;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ErrorDetail<SelfT extends ErrorDetail<SelfT>>
/*     */   implements Serializable
/*     */ {
/*     */   private final String message;
/*     */   private final ImmutableList<Object> sources;
/*     */   private final Throwable cause;
/*     */   
/*     */   protected ErrorDetail(String message, List<Object> sources, Throwable cause) {
/*  25 */     this.message = message;
/*  26 */     this.sources = ImmutableList.copyOf(sources);
/*  27 */     this.cause = cause;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMergeable(ErrorDetail<?> otherError) {
/*  37 */     return false;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void format(int index, List<ErrorDetail<?>> mergeableErrors, Formatter formatter) {
/*  61 */     String id = getErrorIdentifier().<String>map(s -> { String str = Messages.redBold(s); return (new StringBuilder(4 + String.valueOf(str).length())).append("[").append(str).append("]: ").toString(); }).orElse("");
/*  62 */     formatter.format("%s) %s%s%n", new Object[] { Integer.valueOf(index), id, getMessage() });
/*  63 */     formatDetail(mergeableErrors, formatter);
/*     */     
/*  65 */     Optional<String> learnMoreLink = getLearnMoreLink();
/*  66 */     if (learnMoreLink.isPresent()) {
/*  67 */       formatter.format("%n%s%n", new Object[] { Messages.bold("Learn more:") });
/*  68 */       formatter.format("  %s%n", new Object[] { Messages.underline(learnMoreLink.get()) });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Optional<String> getLearnMoreLink() {
/*  91 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Optional<String> getErrorIdentifier() {
/*  96 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public String getMessage() {
/* 100 */     return this.message;
/*     */   }
/*     */   
/*     */   public List<Object> getSources() {
/* 104 */     return (List<Object>)this.sources;
/*     */   }
/*     */   
/*     */   public Throwable getCause() {
/* 108 */     return this.cause;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 113 */     return Objects.hashCode(new Object[] { this.message, this.cause, this.sources });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 118 */     if (!(o instanceof ErrorDetail)) {
/* 119 */       return false;
/*     */     }
/* 121 */     ErrorDetail<?> e = (ErrorDetail)o;
/* 122 */     return (this.message.equals(e.message) && Objects.equal(this.cause, e.cause) && this.sources.equals(e.sources));
/*     */   }
/*     */   
/*     */   protected abstract void formatDetail(List<ErrorDetail<?>> paramList, Formatter paramFormatter);
/*     */   
/*     */   public abstract SelfT withSources(List<Object> paramList);
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\ErrorDetail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */