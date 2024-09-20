/*     */ package com.google.inject.multibindings;
/*     */ 
/*     */ import com.google.inject.Binder;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.binder.LinkedBindingBuilder;
/*     */ import com.google.inject.internal.RealOptionalBinder;
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
/*     */ public class OptionalBinder<T>
/*     */ {
/*     */   private final RealOptionalBinder<T> delegate;
/*     */   
/*     */   public static <T> OptionalBinder<T> newOptionalBinder(Binder binder, Class<T> type) {
/* 143 */     return new OptionalBinder<>(
/* 144 */         RealOptionalBinder.newRealOptionalBinder(binder.skipSources(new Class[] { OptionalBinder.class }, ), Key.get(type)));
/*     */   }
/*     */   
/*     */   public static <T> OptionalBinder<T> newOptionalBinder(Binder binder, TypeLiteral<T> type) {
/* 148 */     return new OptionalBinder<>(
/* 149 */         RealOptionalBinder.newRealOptionalBinder(binder.skipSources(new Class[] { OptionalBinder.class }, ), Key.get(type)));
/*     */   }
/*     */   
/*     */   public static <T> OptionalBinder<T> newOptionalBinder(Binder binder, Key<T> type) {
/* 153 */     return new OptionalBinder<>(
/* 154 */         RealOptionalBinder.newRealOptionalBinder(binder.skipSources(new Class[] { OptionalBinder.class }, ), type));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private OptionalBinder(RealOptionalBinder<T> delegate) {
/* 160 */     this.delegate = delegate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkedBindingBuilder<T> setDefault() {
/* 171 */     return this.delegate.setDefault();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkedBindingBuilder<T> setBinding() {
/* 182 */     return this.delegate.setBinding();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 189 */     if (obj instanceof OptionalBinder) {
/* 190 */       return this.delegate.equals(((OptionalBinder)obj).delegate);
/*     */     }
/* 192 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 197 */     return this.delegate.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\multibindings\OptionalBinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */