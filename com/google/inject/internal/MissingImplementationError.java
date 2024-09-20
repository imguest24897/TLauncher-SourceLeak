/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.inject.Injector;
/*    */ import com.google.inject.Key;
/*    */ import com.google.inject.spi.ErrorDetail;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Formatter;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ final class MissingImplementationError<T>
/*    */   extends InternalErrorDetail<MissingImplementationError<T>> {
/*    */   private final Key<T> key;
/*    */   private final ImmutableList<String> suggestions;
/*    */   
/*    */   public MissingImplementationError(Key<T> key, Injector injector, List<Object> sources) {
/* 21 */     this(key, MissingImplementationErrorHints.getSuggestions(key, injector), sources);
/*    */   }
/*    */ 
/*    */   
/*    */   private MissingImplementationError(Key<T> key, ImmutableList<String> suggestions, List<Object> sources) {
/* 26 */     super(ErrorId.MISSING_IMPLEMENTATION, 
/*    */         
/* 28 */         String.format("No implementation for %s was bound.", new Object[] { Messages.convert(key) }), sources, (Throwable)null);
/*    */ 
/*    */     
/* 31 */     this.key = key;
/* 32 */     this.suggestions = suggestions;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isMergeable(ErrorDetail<?> otherError) {
/* 37 */     return (otherError instanceof MissingImplementationError && ((MissingImplementationError)otherError).key
/* 38 */       .equals(this.key));
/*    */   }
/*    */ 
/*    */   
/*    */   public void formatDetail(List<ErrorDetail<?>> mergeableErrors, Formatter formatter) {
/* 43 */     if (!this.suggestions.isEmpty()) {
/* 44 */       Objects.requireNonNull(formatter); this.suggestions.forEach(x$0 -> rec$.format(x$0, new Object[0]));
/*    */     } 
/* 46 */     List<List<Object>> sourcesList = new ArrayList<>();
/* 47 */     sourcesList.add(getSources());
/* 48 */     sourcesList.addAll((Collection<? extends List<Object>>)mergeableErrors
/* 49 */         .stream().map(ErrorDetail::getSources).collect(Collectors.toList()));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 55 */     List<List<Object>> filteredSourcesList = (List<List<Object>>)sourcesList.stream().map(this::trimSource).filter(sources -> !sources.isEmpty()).collect(Collectors.toList());
/*    */     
/* 57 */     if (!filteredSourcesList.isEmpty()) {
/* 58 */       formatter.format("%n%s%n", new Object[] { Messages.bold("Requested by:") });
/* 59 */       int sourceListIndex = 1;
/* 60 */       for (List<Object> sources : filteredSourcesList) {
/* 61 */         ErrorFormatter.formatSources(sourceListIndex++, Lists.reverse(sources), formatter);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public MissingImplementationError<T> withSources(List<Object> newSources) {
/* 68 */     return new MissingImplementationError(this.key, this.suggestions, newSources);
/*    */   }
/*    */ 
/*    */   
/*    */   private List<Object> trimSource(List<Object> sources) {
/* 73 */     return (List<Object>)sources.stream().filter(source -> !source.equals(this.key)).collect(Collectors.toList());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\MissingImplementationError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */