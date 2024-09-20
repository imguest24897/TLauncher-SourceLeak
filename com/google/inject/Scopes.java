/*     */ package com.google.inject;
/*     */ 
/*     */ import com.google.inject.internal.BindingImpl;
/*     */ import com.google.inject.internal.BytecodeGen;
/*     */ import com.google.inject.internal.SingletonScope;
/*     */ import com.google.inject.spi.BindingScopingVisitor;
/*     */ import com.google.inject.spi.ExposedBinding;
/*     */ import com.google.inject.spi.LinkedKeyBinding;
/*     */ import java.lang.annotation.Annotation;
/*     */ import javax.inject.Singleton;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Scopes
/*     */ {
/*  37 */   public static final Scope SINGLETON = (Scope)new SingletonScope();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   public static final Scope NO_SCOPE = new Scope()
/*     */     {
/*     */       public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped)
/*     */       {
/*  53 */         return unscoped;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/*  58 */         return "Scopes.NO_SCOPE";
/*     */       }
/*     */     };
/*     */   
/*  62 */   private static final BindingScopingVisitor<Boolean> IS_SINGLETON_VISITOR = new BindingScopingVisitor<Boolean>()
/*     */     {
/*     */       public Boolean visitNoScoping()
/*     */       {
/*  66 */         return Boolean.valueOf(false);
/*     */       }
/*     */ 
/*     */       
/*     */       public Boolean visitScopeAnnotation(Class<? extends Annotation> scopeAnnotation) {
/*  71 */         return Boolean.valueOf((scopeAnnotation == Singleton.class || scopeAnnotation == Singleton.class));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public Boolean visitScope(Scope scope) {
/*  77 */         return Boolean.valueOf((scope == Scopes.SINGLETON));
/*     */       }
/*     */ 
/*     */       
/*     */       public Boolean visitEagerSingleton() {
/*  82 */         return Boolean.valueOf(true);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSingleton(Binding<?> binding) {
/*     */     while (true) {
/*  96 */       boolean singleton = ((Boolean)binding.<Boolean>acceptScopingVisitor(IS_SINGLETON_VISITOR)).booleanValue();
/*  97 */       if (singleton) {
/*  98 */         return true;
/*     */       }
/*     */       
/* 101 */       if (binding instanceof LinkedKeyBinding) {
/* 102 */         LinkedKeyBinding<?> linkedBinding = (LinkedKeyBinding)binding;
/* 103 */         Injector injector = getInjector(linkedBinding);
/* 104 */         if (injector != null) {
/* 105 */           binding = injector.getBinding(linkedBinding.getLinkedKey()); continue;
/*     */         }  break;
/*     */       } 
/* 108 */       if (binding instanceof ExposedBinding) {
/* 109 */         ExposedBinding<?> exposedBinding = (ExposedBinding)binding;
/* 110 */         Injector injector = exposedBinding.getPrivateElements().getInjector();
/* 111 */         if (injector != null) {
/* 112 */           binding = injector.getBinding(exposedBinding.getKey()); continue;
/*     */         } 
/*     */       } 
/*     */       break;
/*     */     } 
/* 117 */     return false;
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
/*     */   public static boolean isScoped(Binding<?> binding, final Scope scope, final Class<? extends Annotation> scopeAnnotation) {
/*     */     while (true) {
/* 136 */       boolean matches = ((Boolean)binding.<Boolean>acceptScopingVisitor(new BindingScopingVisitor<Boolean>()
/*     */           {
/*     */             public Boolean visitNoScoping()
/*     */             {
/* 140 */               return Boolean.valueOf(false);
/*     */             }
/*     */ 
/*     */             
/*     */             public Boolean visitScopeAnnotation(Class<? extends Annotation> visitedAnnotation) {
/* 145 */               return Boolean.valueOf((visitedAnnotation == scopeAnnotation));
/*     */             }
/*     */ 
/*     */             
/*     */             public Boolean visitScope(Scope visitedScope) {
/* 150 */               return Boolean.valueOf((visitedScope == scope));
/*     */             }
/*     */ 
/*     */             
/*     */             public Boolean visitEagerSingleton() {
/* 155 */               return Boolean.valueOf(false);
/*     */             }
/*     */           })).booleanValue();
/*     */       
/* 159 */       if (matches) {
/* 160 */         return true;
/*     */       }
/*     */       
/* 163 */       if (binding instanceof LinkedKeyBinding) {
/* 164 */         LinkedKeyBinding<?> linkedBinding = (LinkedKeyBinding)binding;
/* 165 */         Injector injector = getInjector(linkedBinding);
/* 166 */         if (injector != null) {
/* 167 */           binding = injector.getBinding(linkedBinding.getLinkedKey()); continue;
/*     */         }  break;
/*     */       } 
/* 170 */       if (binding instanceof ExposedBinding) {
/* 171 */         ExposedBinding<?> exposedBinding = (ExposedBinding)binding;
/* 172 */         Injector injector = exposedBinding.getPrivateElements().getInjector();
/* 173 */         if (injector != null) {
/* 174 */           binding = injector.getBinding(exposedBinding.getKey()); continue;
/*     */         } 
/*     */       } 
/*     */       break;
/*     */     } 
/* 179 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Injector getInjector(LinkedKeyBinding<?> linkedKeyBinding) {
/* 184 */     if (linkedKeyBinding instanceof BindingImpl) {
/* 185 */       return (Injector)((BindingImpl)linkedKeyBinding).getInjector();
/*     */     }
/* 187 */     return null;
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
/*     */   public static boolean isCircularProxy(Object object) {
/* 200 */     return BytecodeGen.isCircularProxy(object);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\Scopes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */