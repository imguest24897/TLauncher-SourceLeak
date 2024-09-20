/*     */ package com.google.inject.multibindings;
/*     */ 
/*     */ import com.google.inject.Binder;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.binder.LinkedBindingBuilder;
/*     */ import com.google.inject.internal.RealMultibinder;
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
/*     */ public class Multibinder<T>
/*     */ {
/*     */   private final RealMultibinder<T> delegate;
/*     */   
/*     */   public static <T> Multibinder<T> newSetBinder(Binder binder, TypeLiteral<T> type) {
/*  91 */     return newSetBinder(binder, Key.get(type));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Multibinder<T> newSetBinder(Binder binder, Class<T> type) {
/*  99 */     return newSetBinder(binder, Key.get(type));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Multibinder<T> newSetBinder(Binder binder, TypeLiteral<T> type, Annotation annotation) {
/* 108 */     return newSetBinder(binder, Key.get(type, annotation));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Multibinder<T> newSetBinder(Binder binder, Class<T> type, Annotation annotation) {
/* 117 */     return newSetBinder(binder, Key.get(type, annotation));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Multibinder<T> newSetBinder(Binder binder, TypeLiteral<T> type, Class<? extends Annotation> annotationType) {
/* 126 */     return newSetBinder(binder, Key.get(type, annotationType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Multibinder<T> newSetBinder(Binder binder, Key<T> key) {
/* 136 */     return new Multibinder<>(RealMultibinder.newRealSetBinder(binder.skipSources(new Class[] { Multibinder.class }, ), key));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Multibinder<T> newSetBinder(Binder binder, Class<T> type, Class<? extends Annotation> annotationType) {
/* 145 */     return newSetBinder(binder, Key.get(type, annotationType));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Multibinder(RealMultibinder<T> delegate) {
/* 151 */     this.delegate = delegate;
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
/*     */   public Multibinder<T> permitDuplicates() {
/* 163 */     this.delegate.permitDuplicates();
/* 164 */     return this;
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
/*     */   public LinkedBindingBuilder<T> addBinding() {
/* 178 */     return this.delegate.addBinding();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 185 */     if (obj instanceof Multibinder) {
/* 186 */       return this.delegate.equals(((Multibinder)obj).delegate);
/*     */     }
/* 188 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 193 */     return this.delegate.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\multibindings\Multibinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */