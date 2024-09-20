/*     */ package org.apache.commons.text.lookup;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiFunction;
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
/*     */ final class BiFunctionStringLookup<P, R>
/*     */   implements BiStringLookup<P>
/*     */ {
/*     */   private final BiFunction<String, P, R> biFunction;
/*     */   
/*     */   static <U, T> BiFunctionStringLookup<U, T> on(BiFunction<String, U, T> biFunction) {
/*  41 */     return new BiFunctionStringLookup<>(biFunction);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <U, T> BiFunctionStringLookup<U, T> on(Map<String, T> map) {
/*  52 */     return on((key, u) -> map.get(key));
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
/*     */   private BiFunctionStringLookup(BiFunction<String, P, R> biFunction) {
/*  66 */     this.biFunction = biFunction;
/*     */   }
/*     */ 
/*     */   
/*     */   public String lookup(String key) {
/*  71 */     return lookup(key, null);
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
/*     */   public String lookup(String key, P object) {
/*     */     R obj;
/*  86 */     if (this.biFunction == null) {
/*  87 */       return null;
/*     */     }
/*     */     
/*     */     try {
/*  91 */       obj = this.biFunction.apply(key, object);
/*  92 */     } catch (SecurityException|NullPointerException|IllegalArgumentException e) {
/*     */ 
/*     */       
/*  95 */       return null;
/*     */     } 
/*  97 */     return Objects.toString(obj, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 102 */     return super.toString() + " [function=" + this.biFunction + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\lookup\BiFunctionStringLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */