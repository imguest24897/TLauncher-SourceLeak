/*     */ package org.apache.commons.text.lookup;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.lang3.ClassUtils;
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
/*     */ class ConstantStringLookup
/*     */   extends AbstractStringLookup
/*     */ {
/*  67 */   private static final ConcurrentHashMap<String, String> CONSTANT_CACHE = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final char FIELD_SEPARATOR = '.';
/*     */ 
/*     */ 
/*     */   
/*  75 */   static final ConstantStringLookup INSTANCE = new ConstantStringLookup();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void clear() {
/*  81 */     CONSTANT_CACHE.clear();
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
/*     */   protected Class<?> fetchClass(String className) throws ClassNotFoundException {
/*  95 */     return ClassUtils.getClass(className);
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
/*     */   public synchronized String lookup(String key) {
/* 109 */     if (key == null) {
/* 110 */       return null;
/*     */     }
/*     */     
/* 113 */     String result = CONSTANT_CACHE.get(key);
/* 114 */     if (result != null) {
/* 115 */       return result;
/*     */     }
/* 117 */     int fieldPos = key.lastIndexOf('.');
/* 118 */     if (fieldPos < 0) {
/* 119 */       return null;
/*     */     }
/*     */     try {
/* 122 */       Object value = resolveField(key.substring(0, fieldPos), key.substring(fieldPos + 1));
/* 123 */       if (value != null) {
/* 124 */         String string = Objects.toString(value, null);
/* 125 */         CONSTANT_CACHE.put(key, string);
/* 126 */         result = string;
/*     */       } 
/* 128 */     } catch (Exception ex) {
/*     */       
/* 130 */       return null;
/*     */     } 
/* 132 */     return result;
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
/*     */   protected Object resolveField(String className, String fieldName) throws ReflectiveOperationException {
/* 146 */     Class<?> clazz = fetchClass(className);
/* 147 */     if (clazz == null) {
/* 148 */       return null;
/*     */     }
/* 150 */     return clazz.getField(fieldName).get(null);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\lookup\ConstantStringLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */