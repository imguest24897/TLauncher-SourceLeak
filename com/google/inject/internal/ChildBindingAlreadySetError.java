/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.Streams;
/*    */ import com.google.common.collect.UnmodifiableIterator;
/*    */ import com.google.inject.Key;
/*    */ import com.google.inject.spi.ErrorDetail;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Formatter;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ final class ChildBindingAlreadySetError
/*    */   extends InternalErrorDetail<ChildBindingAlreadySetError>
/*    */ {
/*    */   private final Key<?> key;
/*    */   private final ImmutableList<Object> existingSources;
/*    */   
/*    */   ChildBindingAlreadySetError(Key<?> key, Iterable<Object> existingSoruces, List<Object> sources) {
/* 21 */     super(ErrorId.CHILD_BINDING_ALREADY_SET, 
/*    */         
/* 23 */         String.format("Unable to create binding for %s because it was already configured on one or more child injectors or private modules.", new Object[] {
/*    */ 
/*    */             
/* 26 */             Messages.convert(key)
/*    */           }), sources, (Throwable)null);
/*    */     
/* 29 */     this.key = key;
/*    */     
/* 31 */     this
/* 32 */       .existingSources = ImmutableList.copyOf(
/* 33 */         (Collection)Streams.stream(existingSoruces)
/* 34 */         .map(source -> (source == null) ? "" : source)
/* 35 */         .collect(Collectors.toList()));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isMergeable(ErrorDetail<?> otherError) {
/* 40 */     return (otherError instanceof ChildBindingAlreadySetError && ((ChildBindingAlreadySetError)otherError).key
/* 41 */       .equals(this.key));
/*    */   }
/*    */ 
/*    */   
/*    */   public void formatDetail(List<ErrorDetail<?>> mergeableErrors, Formatter formatter) {
/* 46 */     formatter.format("%n%s%n", new Object[] { Messages.bold("Bound at:") });
/* 47 */     int index = 1;
/* 48 */     for (UnmodifiableIterator unmodifiableIterator = this.existingSources.iterator(); unmodifiableIterator.hasNext(); ) { Object source = unmodifiableIterator.next();
/* 49 */       formatter.format("%-2s: ", new Object[] { Integer.valueOf(index++) });
/* 50 */       if (source.equals("")) {
/* 51 */         formatter.format("as a just-in-time binding%n", new Object[0]); continue;
/*    */       } 
/* 53 */       (new SourceFormatter(source, formatter, true)).format(); }
/*    */ 
/*    */     
/* 56 */     List<List<Object>> sourcesList = new ArrayList<>();
/* 57 */     sourcesList.add(getSources());
/* 58 */     mergeableErrors.forEach(error -> sourcesList.add(error.getSources()));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 64 */     List<List<Object>> filteredSources = (List<List<Object>>)sourcesList.stream().map(this::trimSource).filter(list -> !list.isEmpty()).collect(Collectors.toList());
/* 65 */     if (!filteredSources.isEmpty()) {
/* 66 */       formatter.format("%n%s%n", new Object[] { Messages.bold("Requested by:") });
/* 67 */       for (int i = 0; i < sourcesList.size(); i++) {
/* 68 */         ErrorFormatter.formatSources(i + 1, sourcesList.get(i), formatter);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ChildBindingAlreadySetError withSources(List<Object> newSources) {
/* 78 */     return new ChildBindingAlreadySetError(this.key, (Iterable<Object>)this.existingSources, newSources);
/*    */   }
/*    */ 
/*    */   
/*    */   private List<Object> trimSource(List<Object> sources) {
/* 83 */     return (List<Object>)sources.stream().filter(source -> !source.equals(this.key)).collect(Collectors.toList());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ChildBindingAlreadySetError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */