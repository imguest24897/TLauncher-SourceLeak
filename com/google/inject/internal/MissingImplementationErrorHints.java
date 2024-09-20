/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.primitives.Primitives;
/*     */ import com.google.inject.Binding;
/*     */ import com.google.inject.Injector;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.spi.BindingSourceRestriction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Formatter;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
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
/*     */ final class MissingImplementationErrorHints
/*     */ {
/*     */   private static final int MAX_MATCHING_TYPES_REPORTED = 3;
/*     */   private static final int MAX_RELATED_TYPES_REPORTED = 3;
/*  35 */   private static final ImmutableSet<Class<?>> COMMON_AMBIGUOUS_TYPES = ImmutableSet.builder()
/*  36 */     .add(Object.class)
/*  37 */     .add(String.class)
/*  38 */     .addAll(Primitives.allWrapperTypes())
/*  39 */     .build();
/*     */   
/*     */   static <T> ImmutableList<String> getSuggestions(Key<T> key, Injector injector) {
/*  42 */     ImmutableList.Builder<String> suggestions = ImmutableList.builder();
/*  43 */     TypeLiteral<T> type = key.getTypeLiteral();
/*     */ 
/*     */     
/*  46 */     Objects.requireNonNull(suggestions); BindingSourceRestriction.getMissingImplementationSuggestion(GuiceInternal.GUICE_INTERNAL, key).ifPresent(suggestions::add);
/*     */ 
/*     */     
/*  49 */     List<String> possibleMatches = new ArrayList<>();
/*  50 */     List<Binding<T>> sameTypes = injector.findBindingsByType(type);
/*  51 */     if (!sameTypes.isEmpty()) {
/*  52 */       suggestions.add("%nDid you mean?");
/*  53 */       int howMany = Math.min(sameTypes.size(), 3);
/*  54 */       for (int i = 0; i < howMany; i++) {
/*     */ 
/*     */         
/*  57 */         suggestions.add(Messages.format("%n    * %s", new Object[] { ((Binding)sameTypes.get(i)).getKey() }));
/*     */       } 
/*  59 */       int remaining = sameTypes.size() - 3;
/*  60 */       if (remaining > 0) {
/*  61 */         String plural = (remaining == 1) ? "" : "s";
/*  62 */         suggestions.add(
/*  63 */             Messages.format("%n    %d more binding%s with other annotations.", new Object[] { Integer.valueOf(remaining), plural }));
/*     */       } 
/*  65 */       suggestions.add("%n");
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */       
/*  73 */       String want = type.toString();
/*  74 */       Map<Key<?>, Binding<?>> bindingMap = injector.getAllBindings();
/*  75 */       for (Key<?> bindingKey : bindingMap.keySet()) {
/*  76 */         String have = bindingKey.getTypeLiteral().toString();
/*  77 */         if (have.contains(want) || want.contains(have)) {
/*  78 */           Formatter fmt = new Formatter();
/*  79 */           fmt.format("%s bound ", new Object[] { Messages.convert(bindingKey) });
/*  80 */           (new SourceFormatter(((Binding)bindingMap
/*  81 */               .get(bindingKey)).getSource(), fmt, false))
/*  82 */             .format();
/*  83 */           possibleMatches.add(fmt.toString());
/*     */ 
/*     */           
/*  86 */           if (possibleMatches.size() > 3) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/*  93 */       if (!possibleMatches.isEmpty() && possibleMatches.size() <= 3) {
/*  94 */         suggestions.add("%nDid you mean?");
/*  95 */         for (String possibleMatch : possibleMatches) {
/*  96 */           suggestions.add(Messages.format("%n    %s", new Object[] { possibleMatch }));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 103 */     if (sameTypes.isEmpty() && possibleMatches
/* 104 */       .isEmpty() && key
/* 105 */       .getAnnotationType() == null && COMMON_AMBIGUOUS_TYPES
/* 106 */       .contains(key.getTypeLiteral().getRawType()))
/*     */     {
/* 108 */       suggestions.add("%nThe key seems very generic, did you forget an annotation?");
/*     */     }
/*     */     
/* 111 */     return suggestions.build();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\MissingImplementationErrorHints.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */