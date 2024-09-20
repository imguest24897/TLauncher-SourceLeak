/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.inject.Binder;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.binder.AnnotatedConstantBindingBuilder;
/*     */ import com.google.inject.binder.ConstantBindingBuilder;
/*     */ import com.google.inject.spi.Element;
/*     */ import com.google.inject.spi.InjectionPoint;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ConstantBindingBuilderImpl<T>
/*     */   extends AbstractBindingBuilder<T>
/*     */   implements AnnotatedConstantBindingBuilder, ConstantBindingBuilder
/*     */ {
/*     */   public ConstantBindingBuilderImpl(Binder binder, List<Element> elements, Object source) {
/*  39 */     super(binder, elements, source, (Key)NULL_KEY);
/*     */   }
/*     */ 
/*     */   
/*     */   public ConstantBindingBuilder annotatedWith(Class<? extends Annotation> annotationType) {
/*  44 */     annotatedWithInternal(annotationType);
/*  45 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConstantBindingBuilder annotatedWith(Annotation annotation) {
/*  50 */     annotatedWithInternal(annotation);
/*  51 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void to(String value) {
/*  56 */     toConstant(String.class, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void to(int value) {
/*  61 */     toConstant(Integer.class, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void to(long value) {
/*  66 */     toConstant(Long.class, Long.valueOf(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void to(boolean value) {
/*  71 */     toConstant(Boolean.class, Boolean.valueOf(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void to(double value) {
/*  76 */     toConstant(Double.class, Double.valueOf(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void to(float value) {
/*  81 */     toConstant(Float.class, Float.valueOf(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void to(short value) {
/*  86 */     toConstant(Short.class, Short.valueOf(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void to(char value) {
/*  91 */     toConstant(Character.class, Character.valueOf(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void to(byte value) {
/*  96 */     toConstant(Byte.class, Byte.valueOf(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void to(Class<?> value) {
/* 101 */     toConstant(Class.class, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public <E extends Enum<E>> void to(E value) {
/* 106 */     toConstant(value.getDeclaringClass(), value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void toConstant(Class<?> type, Object instance) {
/* 112 */     Class<T> typeAsClassT = (Class)type;
/*     */     
/* 114 */     T instanceAsT = (T)instance;
/*     */     
/* 116 */     if (keyTypeIsSet()) {
/* 117 */       this.binder.addError("Constant value is set more than once.", new Object[0]);
/*     */       
/*     */       return;
/*     */     } 
/* 121 */     BindingImpl<T> base = getBinding();
/* 122 */     Key<T> key = base.getKey().ofType(typeAsClassT);
/*     */     
/* 124 */     if (instanceAsT == null) {
/* 125 */       this.binder.addError("Binding to null instances is not allowed. Use toProvider(Providers.of(null)) if this is your intended behaviour.", new Object[0]);
/*     */     }
/*     */     
/* 128 */     setBinding(new InstanceBindingImpl<>(base
/*     */           
/* 130 */           .getSource(), key, base
/*     */           
/* 132 */           .getScoping(), 
/* 133 */           (Set<InjectionPoint>)ImmutableSet.of(), instanceAsT));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 139 */     return "ConstantBindingBuilder";
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ConstantBindingBuilderImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */