/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.inject.Binder;
/*     */ import com.google.inject.ConfigurationException;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.Provider;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.binder.AnnotatedBindingBuilder;
/*     */ import com.google.inject.binder.LinkedBindingBuilder;
/*     */ import com.google.inject.binder.ScopedBindingBuilder;
/*     */ import com.google.inject.spi.Element;
/*     */ import com.google.inject.spi.InjectionPoint;
/*     */ import com.google.inject.spi.Message;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.inject.Provider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BindingBuilder<T>
/*     */   extends AbstractBindingBuilder<T>
/*     */   implements AnnotatedBindingBuilder<T>
/*     */ {
/*     */   public BindingBuilder(Binder binder, List<Element> elements, Object source, Key<T> key) {
/*  46 */     super(binder, elements, source, key);
/*     */   }
/*     */ 
/*     */   
/*     */   public BindingBuilder<T> annotatedWith(Class<? extends Annotation> annotationType) {
/*  51 */     annotatedWithInternal(annotationType);
/*  52 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BindingBuilder<T> annotatedWith(Annotation annotation) {
/*  57 */     annotatedWithInternal(annotation);
/*  58 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BindingBuilder<T> to(Class<? extends T> implementation) {
/*  63 */     return to(Key.get(implementation));
/*     */   }
/*     */ 
/*     */   
/*     */   public BindingBuilder<T> to(TypeLiteral<? extends T> implementation) {
/*  68 */     return to(Key.get(implementation));
/*     */   }
/*     */ 
/*     */   
/*     */   public BindingBuilder<T> to(Key<? extends T> linkedKey) {
/*  73 */     Preconditions.checkNotNull(linkedKey, "linkedKey");
/*  74 */     checkNotTargetted();
/*  75 */     BindingImpl<T> base = getBinding();
/*  76 */     setBinding(new LinkedBindingImpl<>(base
/*  77 */           .getSource(), base.getKey(), base.getScoping(), linkedKey));
/*  78 */     return this;
/*     */   }
/*     */   
/*     */   public void toInstance(T instance) {
/*     */     ImmutableSet immutableSet;
/*  83 */     checkNotTargetted();
/*     */ 
/*     */ 
/*     */     
/*  87 */     if (instance != null) {
/*     */       try {
/*  89 */         Set<InjectionPoint> injectionPoints = InjectionPoint.forInstanceMethodsAndFields(instance.getClass());
/*  90 */       } catch (ConfigurationException e) {
/*  91 */         copyErrorsToBinder(e);
/*  92 */         Set<InjectionPoint> injectionPoints = (Set<InjectionPoint>)e.getPartialValue();
/*     */       } 
/*     */     } else {
/*  95 */       this.binder.addError("Binding to null instances is not allowed. Use toProvider(Providers.of(null)) if this is your intended behaviour.", new Object[0]);
/*  96 */       immutableSet = ImmutableSet.of();
/*     */     } 
/*     */     
/*  99 */     BindingImpl<T> base = getBinding();
/* 100 */     setBinding(new InstanceBindingImpl<>(base
/*     */           
/* 102 */           .getSource(), base.getKey(), Scoping.EAGER_SINGLETON, (Set<InjectionPoint>)immutableSet, instance));
/*     */   }
/*     */ 
/*     */   
/*     */   public BindingBuilder<T> toProvider(Provider<? extends T> provider) {
/* 107 */     return toProvider((Provider<? extends T>)provider);
/*     */   }
/*     */   
/*     */   public BindingBuilder<T> toProvider(Provider<? extends T> provider) {
/*     */     Set<InjectionPoint> injectionPoints;
/* 112 */     Preconditions.checkNotNull(provider, "provider");
/* 113 */     checkNotTargetted();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 118 */       injectionPoints = InjectionPoint.forInstanceMethodsAndFields(provider.getClass());
/* 119 */     } catch (ConfigurationException e) {
/* 120 */       copyErrorsToBinder(e);
/* 121 */       injectionPoints = (Set<InjectionPoint>)e.getPartialValue();
/*     */     } 
/*     */     
/* 124 */     BindingImpl<T> base = getBinding();
/* 125 */     setBinding(new ProviderInstanceBindingImpl<>(base
/*     */           
/* 127 */           .getSource(), base.getKey(), base.getScoping(), injectionPoints, provider));
/* 128 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BindingBuilder<T> toProvider(Class<? extends Provider<? extends T>> providerType) {
/* 134 */     return toProvider(Key.get(providerType));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BindingBuilder<T> toProvider(TypeLiteral<? extends Provider<? extends T>> providerType) {
/* 140 */     return toProvider(Key.get(providerType));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BindingBuilder<T> toProvider(Key<? extends Provider<? extends T>> providerKey) {
/* 146 */     Preconditions.checkNotNull(providerKey, "providerKey");
/* 147 */     checkNotTargetted();
/*     */     
/* 149 */     BindingImpl<T> base = getBinding();
/* 150 */     setBinding(new LinkedProviderBindingImpl<>(base
/*     */           
/* 152 */           .getSource(), base.getKey(), base.getScoping(), providerKey));
/* 153 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public <S extends T> ScopedBindingBuilder toConstructor(Constructor<S> constructor) {
/* 158 */     return toConstructor(constructor, TypeLiteral.get(constructor.getDeclaringClass()));
/*     */   }
/*     */ 
/*     */   
/*     */   public <S extends T> ScopedBindingBuilder toConstructor(Constructor<S> constructor, TypeLiteral<? extends S> type) {
/*     */     Set<InjectionPoint> injectionPoints;
/* 164 */     Preconditions.checkNotNull(constructor, "constructor");
/* 165 */     Preconditions.checkNotNull(type, "type");
/* 166 */     checkNotTargetted();
/*     */     
/* 168 */     BindingImpl<T> base = getBinding();
/*     */ 
/*     */     
/*     */     try {
/* 172 */       injectionPoints = InjectionPoint.forInstanceMethodsAndFields(type);
/* 173 */     } catch (ConfigurationException e) {
/* 174 */       copyErrorsToBinder(e);
/* 175 */       injectionPoints = (Set<InjectionPoint>)e.getPartialValue();
/*     */     } 
/*     */     
/*     */     try {
/* 179 */       InjectionPoint constructorPoint = InjectionPoint.forConstructor(constructor, type);
/* 180 */       setBinding(new ConstructorBindingImpl<>(base
/*     */             
/* 182 */             .getKey(), base
/* 183 */             .getSource(), base
/* 184 */             .getScoping(), constructorPoint, injectionPoints));
/*     */     
/*     */     }
/* 187 */     catch (ConfigurationException e) {
/* 188 */       copyErrorsToBinder(e);
/*     */     } 
/*     */     
/* 191 */     return (ScopedBindingBuilder)this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 196 */     String str = String.valueOf(getBinding().getKey().getTypeLiteral()); return (new StringBuilder(16 + String.valueOf(str).length())).append("BindingBuilder<").append(str).append(">").toString();
/*     */   }
/*     */   
/*     */   private void copyErrorsToBinder(ConfigurationException e) {
/* 200 */     for (Message message : e.getErrorMessages())
/* 201 */       this.binder.addError(message); 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\BindingBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */