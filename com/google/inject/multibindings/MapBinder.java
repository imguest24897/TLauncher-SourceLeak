/*     */ package com.google.inject.multibindings;
/*     */ 
/*     */ import com.google.inject.Binder;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.binder.LinkedBindingBuilder;
/*     */ import com.google.inject.internal.RealMapBinder;
/*     */ import java.lang.annotation.Annotation;
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
/*     */ public class MapBinder<K, V>
/*     */ {
/*     */   private final RealMapBinder<K, V> delegate;
/*     */   
/*     */   public static <K, V> MapBinder<K, V> newMapBinder(Binder binder, TypeLiteral<K> keyType, TypeLiteral<V> valueType) {
/* 102 */     return new MapBinder<>(
/* 103 */         RealMapBinder.newMapRealBinder(binder.skipSources(new Class[] { MapBinder.class }, ), keyType, valueType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> MapBinder<K, V> newMapBinder(Binder binder, Class<K> keyType, Class<V> valueType) {
/* 112 */     return newMapBinder(binder, TypeLiteral.get(keyType), TypeLiteral.get(valueType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> MapBinder<K, V> newMapBinder(Binder binder, TypeLiteral<K> keyType, TypeLiteral<V> valueType, Annotation annotation) {
/* 121 */     return new MapBinder<>(
/* 122 */         RealMapBinder.newRealMapBinder(binder.skipSources(new Class[] { MapBinder.class }, ), keyType, valueType, annotation));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> MapBinder<K, V> newMapBinder(Binder binder, Class<K> keyType, Class<V> valueType, Annotation annotation) {
/* 131 */     return newMapBinder(binder, TypeLiteral.get(keyType), TypeLiteral.get(valueType), annotation);
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
/*     */   public static <K, V> MapBinder<K, V> newMapBinder(Binder binder, TypeLiteral<K> keyType, TypeLiteral<V> valueType, Class<? extends Annotation> annotationType) {
/* 143 */     return new MapBinder<>(
/* 144 */         RealMapBinder.newRealMapBinder(binder.skipSources(new Class[] { MapBinder.class }, ), keyType, valueType, annotationType));
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
/*     */   public static <K, V> MapBinder<K, V> newMapBinder(Binder binder, Class<K> keyType, Class<V> valueType, Class<? extends Annotation> annotationType) {
/* 156 */     return newMapBinder(binder, 
/* 157 */         TypeLiteral.get(keyType), TypeLiteral.get(valueType), annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private MapBinder(RealMapBinder<K, V> delegate) {
/* 163 */     this.delegate = delegate;
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
/*     */   public MapBinder<K, V> permitDuplicates() {
/* 182 */     this.delegate.permitDuplicates();
/* 183 */     return this;
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
/*     */   public LinkedBindingBuilder<V> addBinding(K key) {
/* 197 */     return this.delegate.addBinding(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 204 */     if (obj instanceof MapBinder) {
/* 205 */       return this.delegate.equals(((MapBinder)obj).delegate);
/*     */     }
/* 207 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 212 */     return this.delegate.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\multibindings\MapBinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */