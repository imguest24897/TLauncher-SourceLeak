/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.inject.Binder;
/*     */ import com.google.inject.Exposed;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.PrivateBinder;
/*     */ import com.google.inject.Provides;
/*     */ import com.google.inject.internal.util.StackTraceElements;
/*     */ import com.google.inject.spi.BindingTargetVisitor;
/*     */ import com.google.inject.spi.Dependency;
/*     */ import com.google.inject.spi.HasDependencies;
/*     */ import com.google.inject.spi.ProviderInstanceBinding;
/*     */ import com.google.inject.spi.ProviderWithExtensionVisitor;
/*     */ import com.google.inject.spi.ProvidesMethodBinding;
/*     */ import com.google.inject.spi.ProvidesMethodTargetVisitor;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ProviderMethod<T>
/*     */   extends InternalProviderInstanceBindingImpl.CyclicFactory<T>
/*     */   implements HasDependencies, ProvidesMethodBinding<T>, ProviderWithExtensionVisitor<T>
/*     */ {
/*     */   protected final Object instance;
/*     */   protected final Method method;
/*     */   private final Key<T> key;
/*     */   private final Class<? extends Annotation> scopeAnnotation;
/*     */   private final ImmutableSet<Dependency<?>> dependencies;
/*     */   private final boolean exposed;
/*     */   private final Annotation annotation;
/*     */   private SingleParameterInjector<?>[] parameterInjectors;
/*     */   
/*     */   static <T> ProviderMethod<T> create(Key<T> key, Method method, Object instance, ImmutableSet<Dependency<?>> dependencies, Class<? extends Annotation> scopeAnnotation, boolean skipFastClassGeneration, Annotation annotation) {
/*  65 */     int modifiers = method.getModifiers();
/*  66 */     if (InternalFlags.isBytecodeGenEnabled() && !skipFastClassGeneration) {
/*     */       try {
/*  68 */         BiFunction<Object, Object[], Object> fastMethod = BytecodeGen.fastMethod(method);
/*  69 */         if (fastMethod != null) {
/*  70 */           return new FastClassProviderMethod<>(key, method, instance, dependencies, scopeAnnotation, annotation, fastMethod);
/*     */         }
/*     */       }
/*  73 */       catch (Exception|LinkageError exception) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  78 */     if (!Modifier.isPublic(modifiers) || 
/*  79 */       !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
/*  80 */       method.setAccessible(true);
/*     */     }
/*     */     
/*  83 */     return new ReflectionProviderMethod<>(key, method, instance, dependencies, scopeAnnotation, annotation);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ProviderMethod(Key<T> key, Method method, Object instance, ImmutableSet<Dependency<?>> dependencies, Class<? extends Annotation> scopeAnnotation, Annotation annotation) {
/* 111 */     super(InternalProviderInstanceBindingImpl.InitializationTiming.EAGER);
/* 112 */     this.key = key;
/* 113 */     this.scopeAnnotation = scopeAnnotation;
/* 114 */     this.instance = instance;
/* 115 */     this.dependencies = dependencies;
/* 116 */     this.method = method;
/* 117 */     this.exposed = method.isAnnotationPresent((Class)Exposed.class);
/* 118 */     this.annotation = annotation;
/*     */   }
/*     */ 
/*     */   
/*     */   public Key<T> getKey() {
/* 123 */     return this.key;
/*     */   }
/*     */ 
/*     */   
/*     */   public Method getMethod() {
/* 128 */     return this.method;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getInstance() {
/* 133 */     return this.instance;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getEnclosingInstance() {
/* 138 */     return this.instance;
/*     */   }
/*     */ 
/*     */   
/*     */   public Annotation getAnnotation() {
/* 143 */     return this.annotation;
/*     */   }
/*     */   
/*     */   public void configure(Binder binder) {
/* 147 */     binder = binder.withSource(this.method);
/*     */     
/* 149 */     if (this.scopeAnnotation != null) {
/* 150 */       binder.bind(this.key).toProvider(this).in(this.scopeAnnotation);
/*     */     } else {
/* 152 */       binder.bind(this.key).toProvider(this);
/*     */     } 
/*     */     
/* 155 */     if (this.exposed)
/*     */     {
/*     */       
/* 158 */       ((PrivateBinder)binder).expose(this.key);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void initialize(InjectorImpl injector, Errors errors) throws ErrorsException {
/* 164 */     this.parameterInjectors = injector.getParametersInjectors((List<Dependency<?>>)this.dependencies.asList(), errors);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected T doProvision(InternalContext context, Dependency<?> dependency) throws InternalProvisionException {
/*     */     try {
/* 171 */       T t = doProvision(SingleParameterInjector.getAll(context, this.parameterInjectors));
/* 172 */       if (t == null && !dependency.isNullable()) {
/* 173 */         InternalProvisionException.onNullInjectedIntoNonNullableDependency(getMethod(), dependency);
/*     */       }
/* 175 */       return t;
/* 176 */     } catch (IllegalAccessException e) {
/* 177 */       throw new AssertionError(e);
/* 178 */     } catch (InvocationTargetException userException) {
/* 179 */       Throwable cause = (userException.getCause() != null) ? userException.getCause() : userException;
/* 180 */       throw InternalProvisionException.errorInProvider(cause).addSource(getSource());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   abstract T doProvision(Object[] paramArrayOfObject) throws IllegalAccessException, InvocationTargetException;
/*     */ 
/*     */   
/*     */   public Set<Dependency<?>> getDependencies() {
/* 190 */     return (Set<Dependency<?>>)this.dependencies;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <B, V> V acceptExtensionVisitor(BindingTargetVisitor<B, V> visitor, ProviderInstanceBinding<? extends B> binding) {
/* 197 */     if (visitor instanceof ProvidesMethodTargetVisitor) {
/* 198 */       return (V)((ProvidesMethodTargetVisitor)visitor).visit(this);
/*     */     }
/* 200 */     return (V)visitor.visit(binding);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 205 */     String annotationString = this.annotation.toString();
/*     */     
/* 207 */     if (this.annotation.annotationType() == Provides.class) {
/* 208 */       annotationString = "@Provides";
/* 209 */     } else if (annotationString.endsWith("()")) {
/*     */       
/* 211 */       annotationString = annotationString.substring(0, annotationString.length() - 2);
/*     */     } 
/* 213 */     String str1 = annotationString, str2 = String.valueOf(StackTraceElements.forMember(this.method)); return (new StringBuilder(1 + String.valueOf(str1).length() + String.valueOf(str2).length())).append(str1).append(" ").append(str2).toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 218 */     if (obj instanceof ProviderMethod) {
/* 219 */       ProviderMethod<?> o = (ProviderMethod)obj;
/* 220 */       return (this.method.equals(o.method) && 
/* 221 */         Objects.equal(this.instance, o.instance) && this.annotation
/* 222 */         .equals(o.annotation));
/*     */     } 
/* 224 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 233 */     return Objects.hashCode(new Object[] { this.method, this.annotation });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class FastClassProviderMethod<T>
/*     */     extends ProviderMethod<T>
/*     */   {
/*     */     final BiFunction<Object, Object[], Object> fastMethod;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     FastClassProviderMethod(Key<T> key, Method method, Object instance, ImmutableSet<Dependency<?>> dependencies, Class<? extends Annotation> scopeAnnotation, Annotation annotation, BiFunction<Object, Object[], Object> fastMethod) {
/* 252 */       super(key, method, instance, dependencies, scopeAnnotation, annotation);
/* 253 */       this.fastMethod = fastMethod;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public T doProvision(Object[] parameters) throws InvocationTargetException {
/*     */       try {
/* 260 */         return (T)this.fastMethod.apply(this.instance, parameters);
/* 261 */       } catch (Throwable e) {
/* 262 */         throw new InvocationTargetException(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ReflectionProviderMethod<T>
/*     */     extends ProviderMethod<T>
/*     */   {
/*     */     ReflectionProviderMethod(Key<T> key, Method method, Object instance, ImmutableSet<Dependency<?>> dependencies, Class<? extends Annotation> scopeAnnotation, Annotation annotation) {
/* 278 */       super(key, method, instance, dependencies, scopeAnnotation, annotation);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     T doProvision(Object[] parameters) throws IllegalAccessException, InvocationTargetException {
/* 284 */       return (T)this.method.invoke(this.instance, parameters);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ProviderMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */