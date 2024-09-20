/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.inject.Binding;
/*     */ import com.google.inject.Injector;
/*     */ import com.google.inject.Scope;
/*     */ import com.google.inject.Scopes;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.spi.BindingScopingVisitor;
/*     */ import com.google.inject.spi.ConstructorBinding;
/*     */ import com.google.inject.spi.ConvertedConstantBinding;
/*     */ import com.google.inject.spi.DefaultBindingTargetVisitor;
/*     */ import com.google.inject.spi.ExposedBinding;
/*     */ import com.google.inject.spi.InstanceBinding;
/*     */ import com.google.inject.spi.LinkedKeyBinding;
/*     */ import com.google.inject.spi.ProviderBinding;
/*     */ import com.google.inject.spi.ProviderInstanceBinding;
/*     */ import com.google.inject.spi.ProviderKeyBinding;
/*     */ import com.google.inject.spi.UntargettedBinding;
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
/*     */ class Indexer
/*     */   extends DefaultBindingTargetVisitor<Object, Indexer.IndexedBinding>
/*     */   implements BindingScopingVisitor<Object>
/*     */ {
/*     */   final Injector injector;
/*     */   
/*     */   enum BindingType
/*     */   {
/*  50 */     INSTANCE,
/*  51 */     PROVIDER_INSTANCE,
/*  52 */     PROVIDER_KEY,
/*  53 */     LINKED_KEY,
/*  54 */     UNTARGETTED,
/*  55 */     CONSTRUCTOR,
/*  56 */     CONSTANT,
/*  57 */     EXPOSED,
/*  58 */     PROVIDED_BY;
/*     */   }
/*     */   
/*     */   static class IndexedBinding {
/*     */     final String annotationName;
/*     */     final Element.Type annotationType;
/*     */     final TypeLiteral<?> typeLiteral;
/*     */     final Object scope;
/*     */     final Indexer.BindingType type;
/*     */     final Object extraEquality;
/*     */     
/*     */     IndexedBinding(Binding<?> binding, Indexer.BindingType type, Object scope, Object extraEquality) {
/*  70 */       this.scope = scope;
/*  71 */       this.type = type;
/*  72 */       this.extraEquality = extraEquality;
/*  73 */       this.typeLiteral = binding.getKey().getTypeLiteral();
/*  74 */       Element annotation = (Element)binding.getKey().getAnnotation();
/*  75 */       this.annotationName = annotation.setName();
/*  76 */       this.annotationType = annotation.type();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/*  81 */       if (!(obj instanceof IndexedBinding)) {
/*  82 */         return false;
/*     */       }
/*  84 */       IndexedBinding o = (IndexedBinding)obj;
/*  85 */       return (this.type == o.type && 
/*  86 */         Objects.equal(this.scope, o.scope) && this.typeLiteral
/*  87 */         .equals(o.typeLiteral) && this.annotationType == o.annotationType && this.annotationName
/*     */         
/*  89 */         .equals(o.annotationName) && 
/*  90 */         Objects.equal(this.extraEquality, o.extraEquality));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/*  95 */       return Objects.hashCode(new Object[] { this.type, this.scope, this.typeLiteral, this.annotationType, this.annotationName, this.extraEquality });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Indexer(Injector injector) {
/* 103 */     this.injector = injector;
/*     */   }
/*     */   
/*     */   boolean isIndexable(Binding<?> binding) {
/* 107 */     return binding.getKey().getAnnotation() instanceof Element;
/*     */   }
/*     */   
/*     */   private Object scope(Binding<?> binding) {
/* 111 */     return binding.acceptScopingVisitor(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public IndexedBinding visit(ConstructorBinding<? extends Object> binding) {
/* 116 */     return new IndexedBinding((Binding<?>)binding, BindingType.CONSTRUCTOR, 
/* 117 */         scope((Binding<?>)binding), binding.getConstructor());
/*     */   }
/*     */ 
/*     */   
/*     */   public IndexedBinding visit(ConvertedConstantBinding<? extends Object> binding) {
/* 122 */     return new IndexedBinding((Binding<?>)binding, BindingType.CONSTANT, 
/* 123 */         scope((Binding<?>)binding), binding.getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public IndexedBinding visit(ExposedBinding<? extends Object> binding) {
/* 128 */     return new IndexedBinding((Binding<?>)binding, BindingType.EXPOSED, scope((Binding<?>)binding), binding);
/*     */   }
/*     */ 
/*     */   
/*     */   public IndexedBinding visit(InstanceBinding<? extends Object> binding) {
/* 133 */     return new IndexedBinding((Binding<?>)binding, BindingType.INSTANCE, 
/* 134 */         scope((Binding<?>)binding), binding.getInstance());
/*     */   }
/*     */ 
/*     */   
/*     */   public IndexedBinding visit(LinkedKeyBinding<? extends Object> binding) {
/* 139 */     return new IndexedBinding((Binding<?>)binding, BindingType.LINKED_KEY, 
/* 140 */         scope((Binding<?>)binding), binding.getLinkedKey());
/*     */   }
/*     */ 
/*     */   
/*     */   public IndexedBinding visit(ProviderBinding<? extends Object> binding) {
/* 145 */     return new IndexedBinding((Binding<?>)binding, BindingType.PROVIDED_BY, 
/*     */ 
/*     */         
/* 148 */         scope((Binding<?>)binding), this.injector
/* 149 */         .getBinding(binding.getProvidedKey()));
/*     */   }
/*     */ 
/*     */   
/*     */   public IndexedBinding visit(ProviderInstanceBinding<? extends Object> binding) {
/* 154 */     return new IndexedBinding((Binding<?>)binding, BindingType.PROVIDER_INSTANCE, 
/* 155 */         scope((Binding<?>)binding), binding.getUserSuppliedProvider());
/*     */   }
/*     */ 
/*     */   
/*     */   public IndexedBinding visit(ProviderKeyBinding<? extends Object> binding) {
/* 160 */     return new IndexedBinding((Binding<?>)binding, BindingType.PROVIDER_KEY, 
/* 161 */         scope((Binding<?>)binding), binding.getProviderKey());
/*     */   }
/*     */ 
/*     */   
/*     */   public IndexedBinding visit(UntargettedBinding<? extends Object> binding) {
/* 166 */     return new IndexedBinding((Binding<?>)binding, BindingType.UNTARGETTED, scope((Binding<?>)binding), null);
/*     */   }
/*     */   
/* 169 */   private static final Object EAGER_SINGLETON = new Object();
/*     */ 
/*     */   
/*     */   public Object visitEagerSingleton() {
/* 173 */     return EAGER_SINGLETON;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object visitNoScoping() {
/* 178 */     return Scopes.NO_SCOPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object visitScope(Scope scope) {
/* 183 */     return scope;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object visitScopeAnnotation(Class<? extends Annotation> scopeAnnotation) {
/* 188 */     return scopeAnnotation;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\Indexer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */