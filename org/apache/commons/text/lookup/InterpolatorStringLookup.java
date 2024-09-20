/*     */ package org.apache.commons.text.lookup;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
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
/*     */ class InterpolatorStringLookup
/*     */   extends AbstractStringLookup
/*     */ {
/*  37 */   static final AbstractStringLookup INSTANCE = new InterpolatorStringLookup();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final char PREFIX_SEPARATOR = ':';
/*     */ 
/*     */ 
/*     */   
/*     */   private final StringLookup defaultStringLookup;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<String, StringLookup> stringLookupMap;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   InterpolatorStringLookup() {
/*  55 */     this((Map<String, ?>)null);
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
/*     */   InterpolatorStringLookup(Map<String, StringLookup> stringLookupMap, StringLookup defaultStringLookup, boolean addDefaultLookups) {
/*  67 */     this.defaultStringLookup = defaultStringLookup;
/*  68 */     this.stringLookupMap = (Map<String, StringLookup>)stringLookupMap.entrySet().stream().collect(Collectors.toMap(e -> StringLookupFactory.toKey((String)e.getKey()), Map.Entry::getValue));
/*  69 */     if (addDefaultLookups) {
/*  70 */       StringLookupFactory.INSTANCE.addDefaultStringLookups(this.stringLookupMap);
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
/*     */   <V> InterpolatorStringLookup(Map<String, V> defaultMap) {
/*  84 */     this(StringLookupFactory.INSTANCE.mapStringLookup(defaultMap));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   InterpolatorStringLookup(StringLookup defaultStringLookup) {
/*  93 */     this(Collections.emptyMap(), defaultStringLookup, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, StringLookup> getStringLookupMap() {
/* 102 */     return this.stringLookupMap;
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
/*     */   public String lookup(String key) {
/* 116 */     if (key == null) {
/* 117 */       return null;
/*     */     }
/*     */     
/* 120 */     int prefixPos = key.indexOf(':');
/* 121 */     if (prefixPos >= 0) {
/* 122 */       String prefix = StringLookupFactory.toKey(key.substring(0, prefixPos));
/* 123 */       String name = key.substring(prefixPos + 1);
/* 124 */       StringLookup lookup = this.stringLookupMap.get(prefix);
/* 125 */       String value = null;
/* 126 */       if (lookup != null) {
/* 127 */         value = lookup.lookup(name);
/*     */       }
/*     */       
/* 130 */       if (value != null) {
/* 131 */         return value;
/*     */       }
/* 133 */       key = key.substring(prefixPos + 1);
/*     */     } 
/* 135 */     if (this.defaultStringLookup != null) {
/* 136 */       return this.defaultStringLookup.lookup(key);
/*     */     }
/* 138 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 143 */     return super.toString() + " [stringLookupMap=" + this.stringLookupMap + ", defaultStringLookup=" + this.defaultStringLookup + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\lookup\InterpolatorStringLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */