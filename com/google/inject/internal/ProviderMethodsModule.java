/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.HashMultimap;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.inject.Binder;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.Module;
/*     */ import com.google.inject.Provides;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.spi.InjectionPoint;
/*     */ import com.google.inject.spi.Message;
/*     */ import com.google.inject.spi.ModuleAnnotatedMethodScanner;
/*     */ import com.google.inject.util.Modules;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
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
/*     */ public final class ProviderMethodsModule
/*     */   implements Module
/*     */ {
/*     */   private final Object delegate;
/*     */   private final TypeLiteral<?> typeLiteral;
/*     */   private final boolean skipFastClassGeneration;
/*     */   private final ModuleAnnotatedMethodScanner scanner;
/*     */   
/*     */   private ProviderMethodsModule(Object delegate, boolean skipFastClassGeneration, ModuleAnnotatedMethodScanner scanner) {
/*  59 */     this.delegate = Preconditions.checkNotNull(delegate, "delegate");
/*  60 */     this.typeLiteral = TypeLiteral.get(getDelegateModuleClass());
/*  61 */     this.skipFastClassGeneration = skipFastClassGeneration;
/*  62 */     this.scanner = scanner;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Module forModule(Module module) {
/*  67 */     return forObject(module, false, ProvidesMethodScanner.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Module forModule(Object module, ModuleAnnotatedMethodScanner scanner) {
/*  72 */     return forObject(module, false, scanner);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Module forObject(Object object) {
/*  83 */     return forObject(object, true, ProvidesMethodScanner.INSTANCE);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Module forObject(Object object, boolean skipFastClassGeneration, ModuleAnnotatedMethodScanner scanner) {
/*  89 */     if (object instanceof ProviderMethodsModule) {
/*  90 */       return Modules.EMPTY_MODULE;
/*     */     }
/*     */     
/*  93 */     return new ProviderMethodsModule(object, skipFastClassGeneration, scanner);
/*     */   }
/*     */   
/*     */   public Class<?> getDelegateModuleClass() {
/*  97 */     return isStaticModule() ? (Class)this.delegate : this.delegate.getClass();
/*     */   }
/*     */   
/*     */   private boolean isStaticModule() {
/* 101 */     return this.delegate instanceof Class;
/*     */   }
/*     */ 
/*     */   
/*     */   public void configure(Binder binder) {
/* 106 */     for (ProviderMethod<?> providerMethod : getProviderMethods(binder))
/* 107 */       providerMethod.configure(binder); 
/*     */   }
/*     */   
/*     */   public List<ProviderMethod<?>> getProviderMethods(Binder binder) {
/*     */     HashMultimap hashMultimap;
/* 112 */     List<ProviderMethod<?>> result = null;
/* 113 */     List<MethodAndAnnotation> methodsAndAnnotations = null;
/*     */     
/* 115 */     Class<?> superMostClass = getDelegateModuleClass();
/* 116 */     for (Class<?> c = superMostClass; c != Object.class && c != null; c = c.getSuperclass()) {
/* 117 */       for (Method method : DeclaredMembers.getDeclaredMethods(c)) {
/* 118 */         Annotation annotation = getAnnotation(binder, method);
/* 119 */         if (annotation != null)
/* 120 */           if (isStaticModule() && 
/* 121 */             !Modifier.isStatic(method.getModifiers()) && 
/* 122 */             !Modifier.isAbstract(method.getModifiers())) {
/* 123 */             binder
/* 124 */               .skipSources(new Class[] { ProviderMethodsModule.class
/* 125 */                 }).addError("%s is an instance method, but a class literal was passed. Make this method static or pass an instance of the module instead.", new Object[] { method });
/*     */           
/*     */           }
/*     */           else {
/*     */ 
/*     */             
/* 131 */             if (result == null) {
/* 132 */               result = new ArrayList<>();
/* 133 */               methodsAndAnnotations = new ArrayList<>();
/*     */             } 
/*     */             
/* 136 */             ProviderMethod<Object> providerMethod = createProviderMethod(binder, method, annotation);
/* 137 */             if (providerMethod != null) {
/* 138 */               result.add(providerMethod);
/*     */             }
/* 140 */             methodsAndAnnotations.add(new MethodAndAnnotation(method, annotation));
/* 141 */             superMostClass = c;
/*     */           }  
/*     */       } 
/*     */     } 
/* 145 */     if (result == null)
/*     */     {
/* 147 */       return (List<ProviderMethod<?>>)ImmutableList.of();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 152 */     Multimap<Signature, Method> methodsBySignature = null;
/*     */ 
/*     */ 
/*     */     
/* 156 */     for (Class<?> clazz1 = getDelegateModuleClass(); clazz1 != superMostClass; clazz1 = clazz1.getSuperclass()) {
/* 157 */       for (Method method : clazz1.getDeclaredMethods()) {
/* 158 */         if ((method.getModifiers() & 0xA) == 0 && 
/* 159 */           !method.isBridge() && 
/* 160 */           !method.isSynthetic()) {
/* 161 */           if (methodsBySignature == null) {
/* 162 */             hashMultimap = HashMultimap.create();
/*     */           }
/* 164 */           hashMultimap.put(new Signature(this.typeLiteral, method), method);
/*     */         } 
/*     */       } 
/*     */     } 
/* 168 */     if (hashMultimap != null)
/*     */     {
/*     */ 
/*     */       
/* 172 */       for (MethodAndAnnotation methodAndAnnotation : methodsAndAnnotations) {
/* 173 */         Method method = methodAndAnnotation.method;
/* 174 */         Annotation annotation = methodAndAnnotation.annotation;
/*     */ 
/*     */         
/* 177 */         for (Method matchingSignature : hashMultimap.get(new Signature(this.typeLiteral, method))) {
/*     */ 
/*     */           
/* 180 */           if (matchingSignature.getDeclaringClass().isAssignableFrom(method.getDeclaringClass())) {
/*     */             continue;
/*     */           }
/*     */           
/* 184 */           if (overrides(matchingSignature, method)) {
/*     */             
/* 186 */             if (annotation.annotationType() == Provides.class);
/*     */             
/* 188 */             String.valueOf(annotation.annotationType().getCanonicalName()); String annotationString = (String.valueOf(annotation.annotationType().getCanonicalName()).length() != 0) ? "@".concat(String.valueOf(annotation.annotationType().getCanonicalName())) : new String("@");
/* 189 */             binder.addError((new StringBuilder(67 + String.valueOf(annotationString).length() + String.valueOf(annotationString).length())).append("Overriding ").append(annotationString).append(" methods is not allowed.\n\t").append(annotationString).append(" method: %s\n\toverridden by: %s").toString(), new Object[] { method, matchingSignature });
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 203 */     return result;
/*     */   }
/*     */   
/*     */   private static class MethodAndAnnotation {
/*     */     final Method method;
/*     */     final Annotation annotation;
/*     */     
/*     */     MethodAndAnnotation(Method method, Annotation annotation) {
/* 211 */       this.method = method;
/* 212 */       this.annotation = annotation;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private Annotation getAnnotation(Binder binder, Method method) {
/* 218 */     if (method.isBridge() || method.isSynthetic()) {
/* 219 */       return null;
/*     */     }
/* 221 */     Annotation annotation = null;
/* 222 */     for (Class<? extends Annotation> annotationClass : (Iterable<Class<? extends Annotation>>)this.scanner.annotationClasses()) {
/* 223 */       Annotation foundAnnotation = method.getAnnotation((Class)annotationClass);
/* 224 */       if (foundAnnotation != null) {
/* 225 */         if (annotation != null) {
/* 226 */           binder.addError("More than one annotation claimed by %s on method %s. Methods can only have one annotation claimed per scanner.", new Object[] { this.scanner, method });
/*     */ 
/*     */ 
/*     */           
/* 230 */           return null;
/*     */         } 
/* 232 */         annotation = foundAnnotation;
/*     */       } 
/*     */     } 
/* 235 */     return annotation;
/*     */   }
/*     */   
/*     */   private static final class Signature {
/*     */     final Class<?>[] parameters;
/*     */     final String name;
/*     */     final int hashCode;
/*     */     
/*     */     Signature(TypeLiteral<?> typeLiteral, Method method) {
/* 244 */       this.name = method.getName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 250 */       List<TypeLiteral<?>> resolvedParameterTypes = typeLiteral.getParameterTypes(method);
/* 251 */       this.parameters = new Class[resolvedParameterTypes.size()];
/* 252 */       int i = 0;
/* 253 */       for (TypeLiteral<?> type : resolvedParameterTypes) {
/* 254 */         this.parameters[i] = type.getRawType();
/*     */       }
/* 256 */       this.hashCode = this.name.hashCode() + 31 * Arrays.hashCode((Object[])this.parameters);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 261 */       if (obj instanceof Signature) {
/* 262 */         Signature other = (Signature)obj;
/* 263 */         return (other.name.equals(this.name) && Arrays.equals((Object[])this.parameters, (Object[])other.parameters));
/*     */       } 
/* 265 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 270 */       return this.hashCode;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean overrides(Method a, Method b) {
/* 277 */     int modifiers = b.getModifiers();
/* 278 */     if (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers)) {
/* 279 */       return true;
/*     */     }
/* 281 */     if (Modifier.isPrivate(modifiers)) {
/* 282 */       return false;
/*     */     }
/*     */     
/* 285 */     return a.getDeclaringClass().getPackage().equals(b.getDeclaringClass().getPackage());
/*     */   }
/*     */ 
/*     */   
/*     */   private <T> ProviderMethod<T> createProviderMethod(Binder binder, Method method, Annotation annotation) {
/* 290 */     binder = binder.withSource(method);
/* 291 */     Errors errors = new Errors(method);
/*     */ 
/*     */     
/* 294 */     InjectionPoint point = InjectionPoint.forMethod(method, this.typeLiteral);
/*     */     
/* 296 */     TypeLiteral<T> returnType = this.typeLiteral.getReturnType(method);
/* 297 */     Key<T> key = getKey(errors, returnType, method, method.getAnnotations());
/* 298 */     boolean prepareMethodError = false;
/*     */     try {
/* 300 */       key = this.scanner.prepareMethod(binder, annotation, key, point);
/* 301 */     } catch (Throwable t) {
/* 302 */       prepareMethodError = true;
/* 303 */       binder.addError(t);
/*     */     } 
/*     */     
/* 306 */     if (Modifier.isAbstract(method.getModifiers())) {
/* 307 */       Preconditions.checkState((prepareMethodError || key == null), "%s returned a non-null key (%s) for %s. prepareMethod() must return null for abstract methods", this.scanner, key, method);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 314 */       return null;
/*     */     } 
/*     */     
/* 317 */     if (key == null) {
/* 318 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 322 */     Class<? extends Annotation> scopeAnnotation = Annotations.findScopeAnnotation(errors, method.getAnnotations());
/* 323 */     for (Message message : errors.getMessages()) {
/* 324 */       binder.addError(message);
/*     */     }
/*     */     
/* 327 */     return ProviderMethod.create(key, method, (
/*     */ 
/*     */         
/* 330 */         isStaticModule() || Modifier.isStatic(method.getModifiers())) ? null : this.delegate, 
/* 331 */         ImmutableSet.copyOf(point.getDependencies()), scopeAnnotation, this.skipFastClassGeneration, annotation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   <T> Key<T> getKey(Errors errors, TypeLiteral<T> type, Member member, Annotation[] annotations) {
/* 338 */     Annotation bindingAnnotation = Annotations.findBindingAnnotation(errors, member, annotations);
/* 339 */     return (bindingAnnotation == null) ? Key.get(type) : Key.get(type, bindingAnnotation);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 344 */     return (o instanceof ProviderMethodsModule && ((ProviderMethodsModule)o).delegate == this.delegate && ((ProviderMethodsModule)o).scanner
/*     */       
/* 346 */       .equals(this.scanner));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 351 */     return Objects.hashCode(new Object[] { this.delegate, this.scanner });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isScanningBuiltInProvidesMethods() {
/* 356 */     return (this.scanner == ProvidesMethodScanner.INSTANCE);
/*     */   }
/*     */   
/*     */   public ModuleAnnotatedMethodScanner getScanner() {
/* 360 */     return this.scanner;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ProviderMethodsModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */