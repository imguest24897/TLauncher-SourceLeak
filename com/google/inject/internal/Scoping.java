/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.Provider;
/*     */ import com.google.inject.Scope;
/*     */ import com.google.inject.Scopes;
/*     */ import com.google.inject.Singleton;
/*     */ import com.google.inject.Stage;
/*     */ import com.google.inject.binder.ScopedBindingBuilder;
/*     */ import com.google.inject.spi.BindingScopingVisitor;
/*     */ import com.google.inject.spi.ScopeBinding;
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
/*     */ 
/*     */ 
/*     */ public abstract class Scoping
/*     */ {
/*  43 */   public static final Scoping UNSCOPED = new Scoping()
/*     */     {
/*     */       public <V> V acceptVisitor(BindingScopingVisitor<V> visitor)
/*     */       {
/*  47 */         return (V)visitor.visitNoScoping();
/*     */       }
/*     */ 
/*     */       
/*     */       public Scope getScopeInstance() {
/*  52 */         return Scopes.NO_SCOPE;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/*  57 */         return Scopes.NO_SCOPE.toString();
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void applyTo(ScopedBindingBuilder scopedBindingBuilder) {}
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   private static final Scoping EXPLICITLY_UNSCOPED = new Scoping()
/*     */     {
/*     */       public <V> V acceptVisitor(BindingScopingVisitor<V> visitor)
/*     */       {
/*  74 */         return (V)visitor.visitNoScoping();
/*     */       }
/*     */ 
/*     */       
/*     */       public Scope getScopeInstance() {
/*  79 */         return Scopes.NO_SCOPE;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/*  84 */         return Scopes.NO_SCOPE.toString();
/*     */       }
/*     */ 
/*     */       
/*     */       public void applyTo(ScopedBindingBuilder scopedBindingBuilder) {
/*  89 */         scopedBindingBuilder.in(Scopes.NO_SCOPE);
/*     */       }
/*     */     };
/*     */   
/*  93 */   public static final Scoping SINGLETON_ANNOTATION = new Scoping()
/*     */     {
/*     */       public <V> V acceptVisitor(BindingScopingVisitor<V> visitor)
/*     */       {
/*  97 */         return (V)visitor.visitScopeAnnotation(Singleton.class);
/*     */       }
/*     */ 
/*     */       
/*     */       public Class<? extends Annotation> getScopeAnnotation() {
/* 102 */         return (Class)Singleton.class;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 107 */         return Singleton.class.getName();
/*     */       }
/*     */ 
/*     */       
/*     */       public void applyTo(ScopedBindingBuilder scopedBindingBuilder) {
/* 112 */         scopedBindingBuilder.in(Singleton.class);
/*     */       }
/*     */     };
/*     */   
/* 116 */   public static final Scoping SINGLETON_INSTANCE = new Scoping()
/*     */     {
/*     */       public <V> V acceptVisitor(BindingScopingVisitor<V> visitor)
/*     */       {
/* 120 */         return (V)visitor.visitScope(Scopes.SINGLETON);
/*     */       }
/*     */ 
/*     */       
/*     */       public Scope getScopeInstance() {
/* 125 */         return Scopes.SINGLETON;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 130 */         return Scopes.SINGLETON.toString();
/*     */       }
/*     */ 
/*     */       
/*     */       public void applyTo(ScopedBindingBuilder scopedBindingBuilder) {
/* 135 */         scopedBindingBuilder.in(Scopes.SINGLETON);
/*     */       }
/*     */     };
/*     */   
/* 139 */   public static final Scoping EAGER_SINGLETON = new Scoping()
/*     */     {
/*     */       public <V> V acceptVisitor(BindingScopingVisitor<V> visitor)
/*     */       {
/* 143 */         return (V)visitor.visitEagerSingleton();
/*     */       }
/*     */ 
/*     */       
/*     */       public Scope getScopeInstance() {
/* 148 */         return Scopes.SINGLETON;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 153 */         return "eager singleton";
/*     */       }
/*     */ 
/*     */       
/*     */       public void applyTo(ScopedBindingBuilder scopedBindingBuilder) {
/* 158 */         scopedBindingBuilder.asEagerSingleton();
/*     */       }
/*     */     };
/*     */   
/*     */   public static Scoping forAnnotation(final Class<? extends Annotation> scopingAnnotation) {
/* 163 */     if (scopingAnnotation == Singleton.class || scopingAnnotation == Singleton.class) {
/* 164 */       return SINGLETON_ANNOTATION;
/*     */     }
/*     */     
/* 167 */     return new Scoping()
/*     */       {
/*     */         public <V> V acceptVisitor(BindingScopingVisitor<V> visitor) {
/* 170 */           return (V)visitor.visitScopeAnnotation(scopingAnnotation);
/*     */         }
/*     */ 
/*     */         
/*     */         public Class<? extends Annotation> getScopeAnnotation() {
/* 175 */           return scopingAnnotation;
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 180 */           return scopingAnnotation.getName();
/*     */         }
/*     */ 
/*     */         
/*     */         public void applyTo(ScopedBindingBuilder scopedBindingBuilder) {
/* 185 */           scopedBindingBuilder.in(scopingAnnotation);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static Scoping forInstance(final Scope scope) {
/* 191 */     if (scope == Scopes.SINGLETON)
/* 192 */       return SINGLETON_INSTANCE; 
/* 193 */     if (scope == Scopes.NO_SCOPE) {
/* 194 */       return EXPLICITLY_UNSCOPED;
/*     */     }
/*     */     
/* 197 */     return new Scoping()
/*     */       {
/*     */         public <V> V acceptVisitor(BindingScopingVisitor<V> visitor) {
/* 200 */           return (V)visitor.visitScope(scope);
/*     */         }
/*     */ 
/*     */         
/*     */         public Scope getScopeInstance() {
/* 205 */           return scope;
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 210 */           return scope.toString();
/*     */         }
/*     */ 
/*     */         
/*     */         public void applyTo(ScopedBindingBuilder scopedBindingBuilder) {
/* 215 */           scopedBindingBuilder.in(scope);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExplicitlyScoped() {
/* 225 */     return (this != UNSCOPED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNoScope() {
/* 233 */     return (getScopeInstance() == Scopes.NO_SCOPE);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEagerSingleton(Stage stage) {
/* 238 */     if (this == EAGER_SINGLETON) {
/* 239 */       return true;
/*     */     }
/*     */     
/* 242 */     if (stage == Stage.PRODUCTION) {
/* 243 */       return (this == SINGLETON_ANNOTATION || this == SINGLETON_INSTANCE);
/*     */     }
/*     */     
/* 246 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Scope getScopeInstance() {
/* 251 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<? extends Annotation> getScopeAnnotation() {
/* 256 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 261 */     if (obj instanceof Scoping) {
/* 262 */       Scoping o = (Scoping)obj;
/* 263 */       return (Objects.equal(getScopeAnnotation(), o.getScopeAnnotation()) && 
/* 264 */         Objects.equal(getScopeInstance(), o.getScopeInstance()));
/*     */     } 
/* 266 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 272 */     return Objects.hashCode(new Object[] { getScopeAnnotation(), getScopeInstance() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Scoping() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> InternalFactory<? extends T> scope(Key<T> key, InjectorImpl injector, InternalFactory<? extends T> creator, Object source, Scoping scoping) {
/* 289 */     if (scoping.isNoScope()) {
/* 290 */       return creator;
/*     */     }
/*     */     
/* 293 */     Scope scope = scoping.getScopeInstance();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 299 */     Provider<T> scoped = scope.scope(key, new ProviderToInternalFactoryAdapter<>(injector, creator));
/* 300 */     return new InternalFactoryToProviderAdapter<>(scoped, source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Scoping makeInjectable(Scoping scoping, InjectorImpl injector, Errors errors) {
/* 309 */     Class<? extends Annotation> scopeAnnotation = scoping.getScopeAnnotation();
/* 310 */     if (scopeAnnotation == null) {
/* 311 */       return scoping;
/*     */     }
/*     */     
/* 314 */     ScopeBinding scope = injector.getBindingData().getScopeBinding(scopeAnnotation);
/* 315 */     if (scope != null) {
/* 316 */       return forInstance(scope.getScope());
/*     */     }
/*     */     
/* 319 */     errors.scopeNotFound(scopeAnnotation);
/* 320 */     return UNSCOPED;
/*     */   }
/*     */   
/*     */   public abstract <V> V acceptVisitor(BindingScopingVisitor<V> paramBindingScopingVisitor);
/*     */   
/*     */   public abstract void applyTo(ScopedBindingBuilder paramScopedBindingBuilder);
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\Scoping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */