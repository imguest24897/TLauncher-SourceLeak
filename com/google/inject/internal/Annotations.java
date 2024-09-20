/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.inject.BindingAnnotation;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.ScopeAnnotation;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.internal.util.Classes;
/*     */ import com.google.inject.name.Named;
/*     */ import com.google.inject.name.Names;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import javax.inject.Named;
/*     */ import javax.inject.Qualifier;
/*     */ import javax.inject.Scope;
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
/*     */ public class Annotations
/*     */ {
/*     */   public static boolean isMarker(Class<? extends Annotation> annotationType) {
/*  57 */     return ((annotationType.getDeclaredMethods()).length == 0);
/*     */   }
/*     */   
/*     */   public static boolean isAllDefaultMethods(Class<? extends Annotation> annotationType) {
/*  61 */     boolean hasMethods = false;
/*  62 */     for (Method m : annotationType.getDeclaredMethods()) {
/*  63 */       hasMethods = true;
/*  64 */       if (m.getDefaultValue() == null) {
/*  65 */         return false;
/*     */       }
/*     */     } 
/*  68 */     return hasMethods;
/*     */   }
/*     */ 
/*     */   
/*  72 */   private static final LoadingCache<Class<? extends Annotation>, Annotation> cache = CacheBuilder.newBuilder()
/*  73 */     .weakKeys()
/*  74 */     .build(new CacheLoader<Class<? extends Annotation>, Annotation>()
/*     */       {
/*     */         public Annotation load(Class<? extends Annotation> input)
/*     */         {
/*  78 */           return (Annotation)Annotations.generateAnnotationImpl((Class)input);
/*     */         }
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Annotation> T generateAnnotation(Class<T> annotationType) {
/*  88 */     Preconditions.checkState(
/*  89 */         isAllDefaultMethods(annotationType), "%s is not all default methods", annotationType);
/*  90 */     return (T)cache.getUnchecked(annotationType);
/*     */   }
/*     */   
/*     */   private static <T extends Annotation> T generateAnnotationImpl(final Class<T> annotationType) {
/*  94 */     final ImmutableMap<String, Object> members = resolveMembers(annotationType);
/*  95 */     return annotationType.cast(
/*  96 */         Proxy.newProxyInstance(annotationType
/*  97 */           .getClassLoader(), new Class[] { annotationType }, new InvocationHandler()
/*     */           {
/*     */             
/*     */             public Object invoke(Object proxy, Method method, Object[] args) throws Exception
/*     */             {
/* 102 */               String name = method.getName();
/* 103 */               if (name.equals("annotationType"))
/* 104 */                 return annotationType; 
/* 105 */               if (name.equals("toString"))
/* 106 */                 return Annotations.annotationToString(annotationType, members); 
/* 107 */               if (name.equals("hashCode"))
/* 108 */                 return Integer.valueOf(Annotations.annotationHashCode(annotationType, members)); 
/* 109 */               if (name.equals("equals")) {
/* 110 */                 return Boolean.valueOf(Annotations.annotationEquals(annotationType, members, args[0]));
/*     */               }
/* 112 */               return members.get(name);
/*     */             }
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static ImmutableMap<String, Object> resolveMembers(Class<? extends Annotation> annotationType) {
/* 120 */     ImmutableMap.Builder<String, Object> result = ImmutableMap.builder();
/* 121 */     for (Method method : annotationType.getDeclaredMethods()) {
/* 122 */       result.put(method.getName(), method.getDefaultValue());
/*     */     }
/* 124 */     return result.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean annotationEquals(Class<? extends Annotation> type, Map<String, Object> members, Object other) throws Exception {
/* 131 */     if (!type.isInstance(other)) {
/* 132 */       return false;
/*     */     }
/* 134 */     for (Method method : type.getDeclaredMethods()) {
/* 135 */       String name = method.getName();
/* 136 */       if (!Arrays.deepEquals(new Object[] { method
/* 137 */             .invoke(other, new Object[0]) }new Object[] { members.get(name) })) {
/* 138 */         return false;
/*     */       }
/*     */     } 
/* 141 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int annotationHashCode(Class<? extends Annotation> type, Map<String, Object> members) throws Exception {
/* 147 */     int result = 0;
/* 148 */     for (Method method : type.getDeclaredMethods()) {
/* 149 */       String name = method.getName();
/* 150 */       Object value = members.get(name);
/* 151 */       result += 127 * name.hashCode() ^ Arrays.deepHashCode(new Object[] { value }) - 31;
/*     */     } 
/* 153 */     return result;
/*     */   }
/*     */   
/* 156 */   private static final Joiner.MapJoiner JOINER = Joiner.on(", ").withKeyValueSeparator("=");
/*     */ 
/*     */ 
/*     */   
/*     */   private static String annotationToString(Class<? extends Annotation> type, Map<String, Object> members) throws Exception {
/* 161 */     StringBuilder sb = (new StringBuilder()).append("@").append(type.getName()).append("(");
/* 162 */     JOINER.appendTo(sb, 
/*     */         
/* 164 */         Maps.transformValues(members, arg -> {
/*     */             String s = Arrays.deepToString(new Object[] { arg });
/*     */             
/*     */             return s.substring(1, s.length() - 1);
/*     */           }));
/*     */     
/* 170 */     return sb.append(")").toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isRetainedAtRuntime(Class<? extends Annotation> annotationType) {
/* 175 */     Retention retention = annotationType.<Retention>getAnnotation(Retention.class);
/* 176 */     return (retention != null && retention.value() == RetentionPolicy.RUNTIME);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<? extends Annotation> findScopeAnnotation(Errors errors, Class<?> implementation) {
/* 182 */     return findScopeAnnotation(errors, implementation.getAnnotations());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<? extends Annotation> findScopeAnnotation(Errors errors, Annotation[] annotations) {
/* 188 */     Class<? extends Annotation> found = null;
/*     */     
/* 190 */     for (Annotation annotation : annotations) {
/* 191 */       Class<? extends Annotation> annotationType = annotation.annotationType();
/* 192 */       if (isScopeAnnotation(annotationType)) {
/* 193 */         if (found != null) {
/* 194 */           errors.duplicateScopeAnnotations(found, annotationType);
/*     */         } else {
/* 196 */           found = annotationType;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 201 */     return found;
/*     */   }
/*     */   
/*     */   static boolean containsComponentAnnotation(Annotation[] annotations) {
/* 205 */     for (Annotation annotation : annotations) {
/*     */       
/* 207 */       if (annotation.annotationType().getSimpleName().equals("Component")) {
/* 208 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 212 */     return false;
/*     */   }
/*     */   
/*     */   private static class AnnotationToStringConfig {
/*     */     final boolean quote;
/*     */     final boolean includeMemberName;
/*     */     
/*     */     AnnotationToStringConfig(boolean quote, boolean includeMemberName) {
/* 220 */       this.quote = quote;
/* 221 */       this.includeMemberName = includeMemberName;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 226 */   private static final AnnotationToStringConfig ANNOTATION_TO_STRING_CONFIG = determineAnnotationToStringConfig();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String memberValueString(String value) {
/* 233 */     return ANNOTATION_TO_STRING_CONFIG.quote ? (new StringBuilder(2 + String.valueOf(value).length())).append("\"").append(value).append("\"").toString() : value;
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
/*     */   public static String memberValueString(String memberName, Object value) {
/* 247 */     StringBuilder sb = new StringBuilder();
/* 248 */     boolean quote = ANNOTATION_TO_STRING_CONFIG.quote;
/* 249 */     boolean includeMemberName = ANNOTATION_TO_STRING_CONFIG.includeMemberName;
/* 250 */     if (includeMemberName) {
/* 251 */       sb.append(memberName).append('=');
/*     */     }
/* 253 */     if (quote && value instanceof String) {
/* 254 */       sb.append('"').append(value).append('"');
/*     */     } else {
/* 256 */       sb.append(value);
/*     */     } 
/* 258 */     return sb.toString();
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
/*     */   @TestAnnotation("determineAnnotationToStringConfig")
/*     */   private static AnnotationToStringConfig determineAnnotationToStringConfig() {
/*     */     try {
/* 273 */       String annotation = ((TestAnnotation)Annotations.class.getDeclaredMethod("determineAnnotationToStringConfig", new Class[0]).<TestAnnotation>getAnnotation(TestAnnotation.class)).toString();
/* 274 */       boolean quote = annotation.contains("\"determineAnnotationToStringConfig\"");
/* 275 */       boolean includeMemberName = annotation.contains("value=");
/* 276 */       return new AnnotationToStringConfig(quote, includeMemberName);
/* 277 */     } catch (NoSuchMethodException e) {
/* 278 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Retention(RetentionPolicy.RUNTIME)
/*     */   private static @interface TestAnnotation {
/*     */     String value(); }
/*     */   
/*     */   static class AnnotationChecker {
/* 287 */     private CacheLoader<Class<? extends Annotation>, Boolean> hasAnnotations = new CacheLoader<Class<? extends Annotation>, Boolean>()
/*     */       {
/*     */         public Boolean load(Class<? extends Annotation> annotationType)
/*     */         {
/* 291 */           for (Annotation annotation : annotationType.getAnnotations()) {
/* 292 */             if (Annotations.AnnotationChecker.this.annotationTypes.contains(annotation.annotationType())) {
/* 293 */               return Boolean.valueOf(true);
/*     */             }
/*     */           } 
/* 296 */           return Boolean.valueOf(false);
/*     */         }
/*     */       };
/*     */     
/*     */     private final Collection<Class<? extends Annotation>> annotationTypes;
/* 301 */     final LoadingCache<Class<? extends Annotation>, Boolean> cache = CacheBuilder.newBuilder().weakKeys().build(this.hasAnnotations);
/*     */ 
/*     */     
/*     */     AnnotationChecker(Collection<Class<? extends Annotation>> annotationTypes) {
/* 305 */       this.annotationTypes = annotationTypes;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean hasAnnotations(Class<? extends Annotation> annotated) {
/* 310 */       return ((Boolean)this.cache.getUnchecked(annotated)).booleanValue();
/*     */     }
/*     */   }
/*     */   
/* 314 */   private static final AnnotationChecker scopeChecker = new AnnotationChecker(
/* 315 */       Arrays.asList((Class<? extends Annotation>[])new Class[] { ScopeAnnotation.class, Scope.class }));
/*     */   
/*     */   public static boolean isScopeAnnotation(Class<? extends Annotation> annotationType) {
/* 318 */     return scopeChecker.hasAnnotations(annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void checkForMisplacedScopeAnnotations(Class<?> type, Object source, Errors errors) {
/* 327 */     if (Classes.isConcrete(type)) {
/*     */       return;
/*     */     }
/*     */     
/* 331 */     Class<? extends Annotation> scopeAnnotation = findScopeAnnotation(errors, type);
/* 332 */     if (scopeAnnotation != null && 
/*     */       
/* 334 */       !containsComponentAnnotation(type.getAnnotations())) {
/* 335 */       errors.withSource(type).scopeAnnotationOnAbstractType(scopeAnnotation, type, source);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Key<?> getKey(TypeLiteral<?> type, Member member, Annotation[] annotations, Errors errors) throws ErrorsException {
/* 346 */     int numErrorsBefore = errors.size();
/* 347 */     Annotation found = findBindingAnnotation(errors, member, annotations);
/* 348 */     errors.throwIfNewErrors(numErrorsBefore);
/* 349 */     return (found == null) ? Key.get(type) : Key.get(type, found);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Annotation findBindingAnnotation(Errors errors, Member member, Annotation[] annotations) {
/* 355 */     Annotation found = null;
/*     */     
/* 357 */     for (Annotation annotation : annotations) {
/* 358 */       Class<? extends Annotation> annotationType = annotation.annotationType();
/* 359 */       if (isBindingAnnotation(annotationType)) {
/* 360 */         if (found != null) {
/* 361 */           errors.duplicateBindingAnnotations(member, found.annotationType(), annotationType);
/*     */         } else {
/* 363 */           found = annotation;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 368 */     return found;
/*     */   }
/*     */   
/* 371 */   private static final AnnotationChecker bindingAnnotationChecker = new AnnotationChecker(
/* 372 */       Arrays.asList((Class<? extends Annotation>[])new Class[] { BindingAnnotation.class, Qualifier.class }));
/*     */ 
/*     */   
/*     */   public static boolean isBindingAnnotation(Class<? extends Annotation> annotationType) {
/* 376 */     return bindingAnnotationChecker.hasAnnotations(annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Annotation canonicalizeIfNamed(Annotation annotation) {
/* 384 */     if (annotation instanceof Named) {
/* 385 */       return (Annotation)Names.named(((Named)annotation).value());
/*     */     }
/* 387 */     return annotation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<? extends Annotation> canonicalizeIfNamed(Class<? extends Annotation> annotationType) {
/* 397 */     if (annotationType == Named.class) {
/* 398 */       return (Class)Named.class;
/*     */     }
/* 400 */     return annotationType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String nameOf(Key<?> key) {
/* 411 */     Annotation annotation = key.getAnnotation();
/* 412 */     Class<? extends Annotation> annotationType = key.getAnnotationType();
/* 413 */     if (annotation != null && !isMarker(annotationType))
/* 414 */       return key.getAnnotation().toString(); 
/* 415 */     if (key.getAnnotationType() != null) {
/* 416 */       String.valueOf(key.getAnnotationType().getName()); return (String.valueOf(key.getAnnotationType().getName()).length() != 0) ? "@".concat(String.valueOf(key.getAnnotationType().getName())) : new String("@");
/*     */     } 
/* 418 */     return "";
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\Annotations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */