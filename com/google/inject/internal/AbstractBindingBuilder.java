/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.inject.Binder;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.Scope;
/*     */ import com.google.inject.spi.Element;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractBindingBuilder<T>
/*     */ {
/*     */   public static final String IMPLEMENTATION_ALREADY_SET = "Implementation is set more than once.";
/*     */   public static final String SINGLE_INSTANCE_AND_SCOPE = "Setting the scope is not permitted when binding to a single instance.";
/*     */   public static final String SCOPE_ALREADY_SET = "Scope is set more than once.";
/*     */   public static final String BINDING_TO_NULL = "Binding to null instances is not allowed. Use toProvider(Providers.of(null)) if this is your intended behaviour.";
/*     */   public static final String CONSTANT_VALUE_ALREADY_SET = "Constant value is set more than once.";
/*     */   public static final String ANNOTATION_ALREADY_SPECIFIED = "More than one annotation is specified for this binding.";
/*  49 */   protected static final Key<?> NULL_KEY = Key.get(Void.class);
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Binder binder;
/*     */ 
/*     */   
/*     */   protected List<Element> elements;
/*     */ 
/*     */   
/*     */   protected int position;
/*     */ 
/*     */   
/*     */   private BindingImpl<T> binding;
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractBindingBuilder(Binder binder, List<Element> elements, Object source, Key<T> key) {
/*  67 */     this.binder = binder;
/*  68 */     this.elements = elements;
/*  69 */     this.position = elements.size();
/*  70 */     this.binding = new UntargettedBindingImpl<>(source, key, Scoping.UNSCOPED);
/*  71 */     elements.add(this.position, this.binding);
/*     */   }
/*     */   
/*     */   protected BindingImpl<T> getBinding() {
/*  75 */     return this.binding;
/*     */   }
/*     */   
/*     */   protected BindingImpl<T> setBinding(BindingImpl<T> binding) {
/*  79 */     this.binding = binding;
/*  80 */     this.elements.set(this.position, binding);
/*  81 */     return binding;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BindingImpl<T> annotatedWithInternal(Class<? extends Annotation> annotationType) {
/*  86 */     Preconditions.checkNotNull(annotationType, "annotationType");
/*  87 */     checkNotAnnotated();
/*  88 */     return setBinding(this.binding.withKey(this.binding.getKey().withAnnotation(annotationType)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BindingImpl<T> annotatedWithInternal(Annotation annotation) {
/*  93 */     Preconditions.checkNotNull(annotation, "annotation");
/*  94 */     checkNotAnnotated();
/*  95 */     return setBinding(this.binding.withKey(this.binding.getKey().withAnnotation(annotation)));
/*     */   }
/*     */   
/*     */   public void in(Class<? extends Annotation> scopeAnnotation) {
/*  99 */     Preconditions.checkNotNull(scopeAnnotation, "scopeAnnotation");
/* 100 */     checkNotScoped();
/* 101 */     setBinding(getBinding().withScoping(Scoping.forAnnotation(scopeAnnotation)));
/*     */   }
/*     */   
/*     */   public void in(Scope scope) {
/* 105 */     Preconditions.checkNotNull(scope, "scope");
/* 106 */     checkNotScoped();
/* 107 */     setBinding(getBinding().withScoping(Scoping.forInstance(scope)));
/*     */   }
/*     */   
/*     */   public void asEagerSingleton() {
/* 111 */     checkNotScoped();
/* 112 */     setBinding(getBinding().withScoping(Scoping.EAGER_SINGLETON));
/*     */   }
/*     */   
/*     */   protected boolean keyTypeIsSet() {
/* 116 */     return !Void.class.equals(this.binding.getKey().getTypeLiteral().getType());
/*     */   }
/*     */   
/*     */   protected void checkNotTargetted() {
/* 120 */     if (!(this.binding instanceof UntargettedBindingImpl)) {
/* 121 */       this.binder.addError("Implementation is set more than once.", new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void checkNotAnnotated() {
/* 126 */     if (this.binding.getKey().getAnnotationType() != null) {
/* 127 */       this.binder.addError("More than one annotation is specified for this binding.", new Object[0]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkNotScoped() {
/* 133 */     if (this.binding instanceof com.google.inject.spi.InstanceBinding) {
/* 134 */       this.binder.addError("Setting the scope is not permitted when binding to a single instance.", new Object[0]);
/*     */       
/*     */       return;
/*     */     } 
/* 138 */     if (this.binding.getScoping().isExplicitlyScoped())
/* 139 */       this.binder.addError("Scope is set more than once.", new Object[0]); 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\AbstractBindingBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */