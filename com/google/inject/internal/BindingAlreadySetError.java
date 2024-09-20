/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.inject.Binding;
/*    */ import com.google.inject.spi.ErrorDetail;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Formatter;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ final class BindingAlreadySetError extends InternalErrorDetail<BindingAlreadySetError> {
/*    */   private final Binding<?> binding;
/*    */   private final Binding<?> original;
/*    */   
/*    */   BindingAlreadySetError(Binding<?> binding, Binding<?> original, List<Object> sources) {
/* 17 */     super(ErrorId.BINDING_ALREADY_SET, 
/*    */         
/* 19 */         String.format("%s was bound multiple times.", new Object[] { Messages.convert(binding.getKey()) }), sources, null);
/*    */ 
/*    */     
/* 22 */     this.binding = binding;
/* 23 */     this.original = original;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isMergeable(ErrorDetail<?> otherError) {
/* 28 */     return (otherError instanceof BindingAlreadySetError && ((BindingAlreadySetError)otherError).binding
/* 29 */       .getKey().equals(this.binding.getKey()));
/*    */   }
/*    */ 
/*    */   
/*    */   public void formatDetail(List<ErrorDetail<?>> mergeableErrors, Formatter formatter) {
/* 34 */     List<List<Object>> sourcesList = new ArrayList<>();
/* 35 */     sourcesList.add(ImmutableList.of(this.original.getSource()));
/* 36 */     sourcesList.add(ImmutableList.of(this.binding.getSource()));
/* 37 */     sourcesList.addAll((Collection<? extends List<Object>>)mergeableErrors
/* 38 */         .stream()
/* 39 */         .map(e -> ((BindingAlreadySetError)e).binding.getSource())
/* 40 */         .map(ImmutableList::of)
/* 41 */         .collect(Collectors.toList()));
/* 42 */     formatter.format("%n%s%n", new Object[] { Messages.bold("Bound at:") });
/* 43 */     for (int i = 0; i < sourcesList.size(); i++) {
/* 44 */       ErrorFormatter.formatSources(i + 1, sourcesList.get(i), formatter);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public BindingAlreadySetError withSources(List<Object> newSources) {
/* 50 */     return new BindingAlreadySetError(this.binding, this.original, newSources);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\BindingAlreadySetError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */