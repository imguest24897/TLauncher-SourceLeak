/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.collect.Multimap;
/*    */ import com.google.inject.Binding;
/*    */ import com.google.inject.Key;
/*    */ import com.google.inject.spi.ErrorDetail;
/*    */ import java.util.Collection;
/*    */ import java.util.Formatter;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class DuplicateMapKeyError<K, V>
/*    */   extends InternalErrorDetail<DuplicateMapKeyError<K, V>>
/*    */ {
/*    */   private final Key<Map<K, V>> mapKey;
/*    */   private final Multimap<K, Binding<V>> duplicates;
/*    */   
/*    */   DuplicateMapKeyError(Key<Map<K, V>> mapKey, Multimap<K, Binding<V>> duplicates, List<Object> sources) {
/* 23 */     super(ErrorId.DUPLICATE_MAP_KEY, getDuplicateKeysMessage(mapKey, duplicates), sources, (Throwable)null);
/* 24 */     this.mapKey = mapKey;
/* 25 */     this.duplicates = duplicates;
/*    */   }
/*    */ 
/*    */   
/*    */   protected final void formatDetail(List<ErrorDetail<?>> others, Formatter formatter) {
/* 30 */     formatter.format("%n%s%n", new Object[] { Messages.bold("Duplicates:") });
/*    */     
/* 32 */     for (Map.Entry<K, Collection<Binding<V>>> entry : (Iterable<Map.Entry<K, Collection<Binding<V>>>>)this.duplicates.asMap().entrySet()) {
/* 33 */       formatter.format("  Key: \"%s\"%n", new Object[] { Messages.redBold(entry.getKey().toString()) });
/* 34 */       formatter.format("  Bound at:%n", new Object[0]);
/* 35 */       int index = 1;
/* 36 */       for (Binding<V> binding : entry.getValue()) {
/* 37 */         formatter.format("    %-2s: ", new Object[] { Integer.valueOf(index++) });
/* 38 */         (new SourceFormatter(binding
/* 39 */             .getSource(), formatter, true))
/*    */ 
/*    */ 
/*    */           
/* 43 */           .format();
/*    */       } 
/* 45 */       formatter.format("%n", new Object[0]);
/*    */     } 
/*    */     
/* 48 */     formatter.format("%s%n", new Object[] { Messages.bold("MapBinder declared at:") });
/* 49 */     ErrorFormatter.formatSources(getSources(), formatter);
/*    */   }
/*    */ 
/*    */   
/*    */   public DuplicateMapKeyError<K, V> withSources(List<Object> newSources) {
/* 54 */     return new DuplicateMapKeyError(this.mapKey, this.duplicates, newSources);
/*    */   }
/*    */ 
/*    */   
/*    */   private static <K, V> String getDuplicateKeysMessage(Key<Map<K, V>> mapKey, Multimap<K, Binding<V>> duplicates) {
/* 59 */     Set<K> duplicateKeys = duplicates.keySet();
/* 60 */     String mapBinderKey = Messages.convert(mapKey).toString();
/* 61 */     String firstDuplicateKey = duplicateKeys.iterator().next().toString();
/* 62 */     if (duplicateKeys.size() == 1) {
/* 63 */       return String.format("Duplicate key \"%s\" found in %s.", new Object[] { firstDuplicateKey, mapBinderKey });
/*    */     }
/* 65 */     return String.format("\"%s\" and %s other duplicate keys found in %s.", new Object[] { firstDuplicateKey, 
/*    */           
/* 67 */           Integer.valueOf(duplicateKeys.size() - 1), mapBinderKey });
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\DuplicateMapKeyError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */