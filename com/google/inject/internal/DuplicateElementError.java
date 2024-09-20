/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMultimap;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.inject.Binding;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.spi.Dependency;
/*     */ import com.google.inject.spi.ErrorDetail;
/*     */ import java.util.Collection;
/*     */ import java.util.Formatter;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ 
/*     */ final class DuplicateElementError<T>
/*     */   extends InternalErrorDetail<DuplicateElementError<T>>
/*     */ {
/*     */   private final Key<Set<T>> setKey;
/*     */   private final ImmutableMultimap<T, Element<T>> elements;
/*     */   
/*     */   DuplicateElementError(Key<Set<T>> setKey, List<Binding<T>> bindings, T[] values, List<Object> sources) {
/*  25 */     this(setKey, indexElements(bindings, values), sources);
/*     */   }
/*     */ 
/*     */   
/*     */   private DuplicateElementError(Key<Set<T>> setKey, ImmutableMultimap<T, Element<T>> elements, List<Object> sources) {
/*  30 */     super(ErrorId.DUPLICATE_ELEMENT, 
/*     */         
/*  32 */         String.format("Duplicate elements found in Multibinder %s.", new Object[] { Messages.convert(setKey) }), sources, (Throwable)null);
/*     */ 
/*     */     
/*  35 */     this.setKey = setKey;
/*  36 */     this.elements = elements;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void formatDetail(List<ErrorDetail<?>> others, Formatter formatter) {
/*  41 */     formatter.format("%n%s%n", new Object[] { Messages.bold("Duplicates:") });
/*  42 */     int duplicateIndex = 1;
/*  43 */     for (UnmodifiableIterator<Map.Entry<T, Collection<Element<T>>>> unmodifiableIterator = this.elements.asMap().entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<T, Collection<Element<T>>> entry = unmodifiableIterator.next();
/*  44 */       formatter.format("%-2s: ", new Object[] { Integer.valueOf(duplicateIndex++) });
/*  45 */       if (((Collection)entry.getValue()).size() > 1) {
/*     */ 
/*     */ 
/*     */         
/*  49 */         Set<String> valuesAsString = (Set<String>)((Collection)entry.getValue()).stream().map(element -> element.value.toString()).collect(Collectors.toSet());
/*  50 */         if (valuesAsString.size() == 1) {
/*     */           
/*  52 */           formatter.format("Element: %s%n", new Object[] { Messages.redBold(valuesAsString.iterator().next()) });
/*  53 */           formatter.format("    Bound at:%n", new Object[0]);
/*  54 */           int index = 1;
/*  55 */           for (Element<T> element : entry.getValue()) {
/*  56 */             formatter.format("    %-2s: ", new Object[] { Integer.valueOf(index++) });
/*  57 */             formatElement(element, formatter);
/*     */           } 
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */ 
/*     */         
/*  65 */         boolean indent = false;
/*  66 */         for (Element<T> element : entry.getValue()) {
/*  67 */           if (indent) {
/*  68 */             formatter.format("    ", new Object[0]);
/*     */           } else {
/*  70 */             indent = true;
/*     */           } 
/*  72 */           formatter.format("Element: %s%n", new Object[] { Messages.redBold(element.value.toString()) });
/*  73 */           formatter.format("    Bound at: ", new Object[0]);
/*  74 */           formatElement(element, formatter);
/*     */         } 
/*     */       }  }
/*     */ 
/*     */     
/*  79 */     formatter.format("%n%s%n", new Object[] { Messages.bold("Multibinder declared at:") });
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
/*  91 */     List<Object> filteredSource = (List<Object>)getSources().stream().filter(source -> (source instanceof Dependency) ? (!((Dependency)source).getKey().equals(this.setKey)) : true).collect(Collectors.toList());
/*  92 */     ErrorFormatter.formatSources(filteredSource, formatter);
/*     */   }
/*     */   
/*     */   private void formatElement(Element<T> element, Formatter formatter) {
/*  96 */     Object source = element.binding.getSource();
/*  97 */     (new SourceFormatter(source, formatter, true))
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 102 */       .format();
/*     */   }
/*     */ 
/*     */   
/*     */   public DuplicateElementError<T> withSources(List<Object> newSources) {
/* 107 */     return new DuplicateElementError(this.setKey, this.elements, newSources);
/*     */   }
/*     */   
/*     */   static <T> ImmutableMultimap<T, Element<T>> indexElements(List<Binding<T>> bindings, T[] values) {
/* 111 */     ImmutableMultimap.Builder<T, Element<T>> map = ImmutableMultimap.builder();
/* 112 */     for (int i = 0; i < values.length; i++) {
/* 113 */       map.put(values[i], new Element<>(values[i], bindings.get(i)));
/*     */     }
/* 115 */     return map.build();
/*     */   }
/*     */   
/*     */   static class Element<T> {
/*     */     T value;
/*     */     Binding<T> binding;
/*     */     
/*     */     Element(T value, Binding<T> binding) {
/* 123 */       this.value = value;
/* 124 */       this.binding = binding;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\DuplicateElementError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */