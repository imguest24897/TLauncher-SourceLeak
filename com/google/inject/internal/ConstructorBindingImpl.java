/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.inject.Binder;
/*     */ import com.google.inject.ConfigurationException;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.internal.util.Classes;
/*     */ import com.google.inject.spi.BindingTargetVisitor;
/*     */ import com.google.inject.spi.ConstructorBinding;
/*     */ import com.google.inject.spi.Dependency;
/*     */ import com.google.inject.spi.Elements;
/*     */ import com.google.inject.spi.InjectionPoint;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ConstructorBindingImpl<T>
/*     */   extends BindingImpl<T>
/*     */   implements ConstructorBinding<T>, DelayedInitialize
/*     */ {
/*     */   private final Factory<T> factory;
/*     */   private final InjectionPoint constructorInjectionPoint;
/*     */   
/*     */   private ConstructorBindingImpl(InjectorImpl injector, Key<T> key, Object source, InternalFactory<? extends T> scopedFactory, Scoping scoping, Factory<T> factory, InjectionPoint constructorInjectionPoint) {
/*  59 */     super(injector, key, source, scopedFactory, scoping);
/*  60 */     this.factory = factory;
/*  61 */     this.constructorInjectionPoint = constructorInjectionPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstructorBindingImpl(Key<T> key, Object source, Scoping scoping, InjectionPoint constructorInjectionPoint, Set<InjectionPoint> injectionPoints) {
/*  70 */     super(source, key, scoping);
/*  71 */     this.factory = new Factory<>(false, key);
/*     */     
/*  73 */     ConstructionProxy<T> constructionProxy = (new DefaultConstructionProxyFactory<>(constructorInjectionPoint)).create();
/*  74 */     this.constructorInjectionPoint = constructorInjectionPoint;
/*  75 */     this.factory.constructorInjector = new ConstructorInjector<>(injectionPoints, constructionProxy, null, null);
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
/*     */   
/*     */   static <T> ConstructorBindingImpl<T> create(InjectorImpl injector, Key<T> key, InjectionPoint constructorInjector, Object source, Scoping scoping, Errors errors, boolean failIfNotLinked, boolean atInjectRequired) throws ErrorsException {
/*  94 */     int numErrors = errors.size();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  99 */     Class<?> rawType = (constructorInjector == null) ? key.getTypeLiteral().getRawType() : constructorInjector.getDeclaringType().getRawType();
/*     */ 
/*     */     
/* 102 */     if (Modifier.isAbstract(rawType.getModifiers())) {
/* 103 */       errors.missingImplementationWithHint(key, injector);
/*     */     }
/*     */ 
/*     */     
/* 107 */     if (Classes.isInnerClass(rawType)) {
/* 108 */       errors.cannotInjectInnerClass(rawType);
/*     */     }
/*     */     
/* 111 */     errors.throwIfNewErrors(numErrors);
/*     */ 
/*     */     
/* 114 */     if (constructorInjector == null) {
/*     */       
/*     */       try {
/* 117 */         constructorInjector = InjectionPoint.forConstructorOf(key.getTypeLiteral(), atInjectRequired);
/* 118 */       } catch (ConfigurationException e) {
/* 119 */         throw errors.merge(e.getErrorMessages()).toException();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 124 */     if (!scoping.isExplicitlyScoped()) {
/* 125 */       Class<?> annotatedType = constructorInjector.getMember().getDeclaringClass();
/* 126 */       Class<? extends Annotation> scopeAnnotation = Annotations.findScopeAnnotation(errors, annotatedType);
/* 127 */       if (scopeAnnotation != null)
/*     */       {
/* 129 */         scoping = Scoping.makeInjectable(
/* 130 */             Scoping.forAnnotation(scopeAnnotation), injector, errors.withSource(rawType));
/*     */       }
/*     */     } 
/*     */     
/* 134 */     errors.throwIfNewErrors(numErrors);
/*     */     
/* 136 */     Factory<T> factoryFactory = new Factory<>(failIfNotLinked, key);
/*     */     
/* 138 */     InternalFactory<? extends T> scopedFactory = Scoping.scope(key, injector, factoryFactory, source, scoping);
/*     */     
/* 140 */     return new ConstructorBindingImpl<>(injector, key, source, scopedFactory, scoping, factoryFactory, constructorInjector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize(InjectorImpl injector, Errors errors) throws ErrorsException {
/* 147 */     this.factory.constructorInjector = injector.constructors
/* 148 */       .get(this.constructorInjectionPoint, errors);
/* 149 */     this.factory.provisionCallback = injector.provisionListenerStore.get(this);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isInitialized() {
/* 154 */     return (this.factory.constructorInjector != null);
/*     */   }
/*     */ 
/*     */   
/*     */   InjectionPoint getInternalConstructor() {
/* 159 */     if (this.factory.constructorInjector != null) {
/* 160 */       return this.factory.constructorInjector.getConstructionProxy().getInjectionPoint();
/*     */     }
/* 162 */     return this.constructorInjectionPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Set<Dependency<?>> getInternalDependencies() {
/* 168 */     ImmutableSet.Builder<InjectionPoint> builder = ImmutableSet.builder();
/* 169 */     if (this.factory.constructorInjector == null) {
/* 170 */       builder.add(this.constructorInjectionPoint);
/*     */       try {
/* 172 */         builder.addAll(
/* 173 */             InjectionPoint.forInstanceMethodsAndFields(this.constructorInjectionPoint
/* 174 */               .getDeclaringType()));
/* 175 */       } catch (ConfigurationException configurationException) {}
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 180 */       builder.add(getConstructor()).addAll(getInjectableMembers());
/*     */     } 
/*     */     
/* 183 */     return Dependency.forInjectionPoints((Set)builder.build());
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> visitor) {
/* 188 */     Preconditions.checkState((this.factory.constructorInjector != null), "not initialized");
/* 189 */     return (V)visitor.visit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public InjectionPoint getConstructor() {
/* 194 */     Preconditions.checkState((this.factory.constructorInjector != null), "Binding is not ready");
/* 195 */     return this.factory.constructorInjector.getConstructionProxy().getInjectionPoint();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<InjectionPoint> getInjectableMembers() {
/* 200 */     Preconditions.checkState((this.factory.constructorInjector != null), "Binding is not ready");
/* 201 */     return (Set<InjectionPoint>)this.factory.constructorInjector.getInjectableMembers();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Method, List<MethodInterceptor>> getMethodInterceptors() {
/* 206 */     Preconditions.checkState((this.factory.constructorInjector != null), "Binding is not ready");
/* 207 */     return (Map<Method, List<MethodInterceptor>>)this.factory.constructorInjector.getConstructionProxy().getMethodInterceptors();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Dependency<?>> getDependencies() {
/* 212 */     return Dependency.forInjectionPoints((Set)(new ImmutableSet.Builder())
/*     */         
/* 214 */         .add(getConstructor())
/* 215 */         .addAll(getInjectableMembers())
/* 216 */         .build());
/*     */   }
/*     */ 
/*     */   
/*     */   protected BindingImpl<T> withScoping(Scoping scoping) {
/* 221 */     return new ConstructorBindingImpl(null, 
/* 222 */         getKey(), getSource(), this.factory, scoping, this.factory, this.constructorInjectionPoint);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BindingImpl<T> withKey(Key<T> key) {
/* 227 */     return new ConstructorBindingImpl(null, key, 
/* 228 */         getSource(), this.factory, getScoping(), this.factory, this.constructorInjectionPoint);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyTo(Binder binder) {
/* 234 */     InjectionPoint constructor = getConstructor();
/* 235 */     getScoping()
/* 236 */       .applyTo(
/* 237 */         Elements.withTrustedSource(GuiceInternal.GUICE_INTERNAL, binder, getSource())
/* 238 */         .bind(getKey())
/* 239 */         .toConstructor((Constructor)
/* 240 */           getConstructor().getMember(), constructor
/* 241 */           .getDeclaringType()));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 246 */     return MoreObjects.toStringHelper(ConstructorBinding.class)
/* 247 */       .add("key", getKey())
/* 248 */       .add("source", getSource())
/* 249 */       .add("scope", getScoping())
/* 250 */       .toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 255 */     if (obj instanceof ConstructorBindingImpl) {
/* 256 */       ConstructorBindingImpl<?> o = (ConstructorBindingImpl)obj;
/* 257 */       return (getKey().equals(o.getKey()) && 
/* 258 */         getScoping().equals(o.getScoping()) && 
/* 259 */         Objects.equal(this.constructorInjectionPoint, o.constructorInjectionPoint));
/*     */     } 
/* 261 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 267 */     return Objects.hashCode(new Object[] { getKey(), getScoping(), this.constructorInjectionPoint });
/*     */   }
/*     */   
/*     */   private static class Factory<T> implements InternalFactory<T> {
/*     */     private final boolean failIfNotLinked;
/*     */     private final Key<?> key;
/*     */     private ConstructorInjector<T> constructorInjector;
/*     */     private ProvisionListenerStackCallback<T> provisionCallback;
/*     */     
/*     */     Factory(boolean failIfNotLinked, Key<?> key) {
/* 277 */       this.failIfNotLinked = failIfNotLinked;
/* 278 */       this.key = key;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public T get(InternalContext context, Dependency<?> dependency, boolean linked) throws InternalProvisionException {
/* 285 */       ConstructorInjector<T> localInjector = this.constructorInjector;
/* 286 */       if (localInjector == null) {
/* 287 */         throw new IllegalStateException("Constructor not ready");
/*     */       }
/*     */       
/* 290 */       if (!linked && this.failIfNotLinked) {
/* 291 */         throw InternalProvisionException.jitDisabled(this.key);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 296 */       return (T)localInjector.construct(context, dependency, this.provisionCallback);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ConstructorBindingImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */