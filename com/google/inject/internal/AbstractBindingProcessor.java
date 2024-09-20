/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.inject.AbstractModule;
/*     */ import com.google.inject.Binder;
/*     */ import com.google.inject.Binding;
/*     */ import com.google.inject.Injector;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.MembersInjector;
/*     */ import com.google.inject.Module;
/*     */ import com.google.inject.Provider;
/*     */ import com.google.inject.Scope;
/*     */ import com.google.inject.Stage;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.spi.DefaultBindingTargetVisitor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractBindingProcessor
/*     */   extends AbstractProcessor
/*     */ {
/*  44 */   private static final ImmutableSet<Class<?>> FORBIDDEN_TYPES = ImmutableSet.of(AbstractModule.class, Binder.class, Binding.class, Injector.class, Key.class, MembersInjector.class, (Object[])new Class[] { Module.class, Provider.class, Scope.class, Stage.class, TypeLiteral.class });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ProcessedBindingData processedBindingData;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AbstractBindingProcessor(Errors errors, ProcessedBindingData processedBindingData) {
/*  60 */     super(errors);
/*  61 */     this.processedBindingData = processedBindingData;
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> UntargettedBindingImpl<T> invalidBinding(InjectorImpl injector, Key<T> key, Object source) {
/*  66 */     return new UntargettedBindingImpl<>(injector, key, source);
/*     */   }
/*     */   
/*     */   protected void putBinding(BindingImpl<?> binding) {
/*  70 */     Key<?> key = binding.getKey();
/*     */     
/*  72 */     Class<?> rawType = key.getTypeLiteral().getRawType();
/*  73 */     if (FORBIDDEN_TYPES.contains(rawType)) {
/*  74 */       this.errors.cannotBindToGuiceType(rawType.getSimpleName());
/*     */       
/*     */       return;
/*     */     } 
/*  78 */     BindingImpl<?> original = this.injector.getExistingBinding(key);
/*  79 */     if (original != null)
/*     */     {
/*  81 */       if (this.injector.getBindingData().getExplicitBinding(key) != null) {
/*     */         try {
/*  83 */           if (!isOkayDuplicate(original, binding, this.injector.getBindingData())) {
/*  84 */             this.errors.bindingAlreadySet(binding, original);
/*     */             return;
/*     */           } 
/*  87 */         } catch (Throwable t) {
/*  88 */           this.errors.errorCheckingDuplicateBinding(key, original.getSource(), t);
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/*     */       } else {
/*  94 */         this.errors.jitBindingAlreadySet(key);
/*     */         
/*     */         return;
/*     */       } 
/*     */     }
/*     */     
/* 100 */     this.injector
/* 101 */       .getJitBindingData()
/* 102 */       .banKeyInParent(key, this.injector.getBindingData(), binding.getSource());
/* 103 */     this.injector.getBindingData().putBinding(key, binding);
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
/*     */   private static boolean isOkayDuplicate(BindingImpl<?> original, BindingImpl<?> binding, InjectorBindingData bindingData) {
/* 115 */     if (original instanceof ExposedBindingImpl) {
/* 116 */       ExposedBindingImpl<?> exposed = (ExposedBindingImpl)original;
/* 117 */       InjectorImpl exposedFrom = (InjectorImpl)exposed.getPrivateElements().getInjector();
/* 118 */       return (exposedFrom == binding.getInjector());
/*     */     } 
/* 120 */     original = (BindingImpl)bindingData.getExplicitBindingsThisLevel().get(binding.getKey());
/*     */ 
/*     */     
/* 123 */     if (original == null) {
/* 124 */       return false;
/*     */     }
/* 126 */     return original.equals(binding);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private <T> void validateKey(Object source, Key<T> key) {
/* 132 */     Annotations.checkForMisplacedScopeAnnotations(key
/* 133 */         .getTypeLiteral().getRawType(), source, this.errors);
/*     */   }
/*     */ 
/*     */   
/*     */   abstract class Processor<T, V>
/*     */     extends DefaultBindingTargetVisitor<T, V>
/*     */   {
/*     */     final Object source;
/*     */     
/*     */     final Key<T> key;
/*     */     final Class<? super T> rawType;
/*     */     Scoping scoping;
/*     */     
/*     */     Processor(BindingImpl<T> binding) {
/* 147 */       this.source = binding.getSource();
/* 148 */       this.key = binding.getKey();
/* 149 */       this.rawType = this.key.getTypeLiteral().getRawType();
/* 150 */       this.scoping = binding.getScoping();
/*     */     }
/*     */     
/*     */     protected void prepareBinding() {
/* 154 */       AbstractBindingProcessor.this.validateKey(this.source, this.key);
/* 155 */       this.scoping = Scoping.makeInjectable(this.scoping, AbstractBindingProcessor.this.injector, AbstractBindingProcessor.this.errors);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void scheduleInitialization(BindingImpl<?> binding) {
/* 163 */       AbstractBindingProcessor.this.processedBindingData.addUninitializedBinding(() -> initializeBinding(binding));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void scheduleDelayedInitialization(BindingImpl<?> binding) {
/* 171 */       AbstractBindingProcessor.this.processedBindingData.addDelayedUninitializedBinding(() -> initializeBinding(binding));
/*     */     }
/*     */     
/*     */     private void initializeBinding(BindingImpl<?> binding) {
/*     */       try {
/* 176 */         binding.getInjector().initializeBinding(binding, AbstractBindingProcessor.this.errors.withSource(this.source));
/* 177 */       } catch (ErrorsException e) {
/* 178 */         AbstractBindingProcessor.this.errors.merge(e.getErrors());
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\AbstractBindingProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */