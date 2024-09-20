/*     */ package com.google.inject;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.inject.internal.Annotations;
/*     */ import com.google.inject.internal.MoreTypes;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Type;
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
/*     */ public class Key<T>
/*     */ {
/*     */   private final AnnotationStrategy annotationStrategy;
/*     */   private final TypeLiteral<T> typeLiteral;
/*     */   private final int hashCode;
/*     */   private String toString;
/*     */   
/*     */   protected Key(Class<? extends Annotation> annotationType) {
/*  73 */     this.annotationStrategy = strategyFor(annotationType);
/*  74 */     this
/*  75 */       .typeLiteral = MoreTypes.canonicalizeForKey(
/*  76 */         TypeLiteral.fromSuperclassTypeParameter(getClass()));
/*  77 */     this.hashCode = computeHashCode();
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
/*     */   protected Key(Annotation annotation) {
/*  93 */     this.annotationStrategy = strategyFor(annotation);
/*  94 */     this
/*  95 */       .typeLiteral = MoreTypes.canonicalizeForKey(
/*  96 */         TypeLiteral.fromSuperclassTypeParameter(getClass()));
/*  97 */     this.hashCode = computeHashCode();
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
/*     */   protected Key() {
/* 112 */     this.annotationStrategy = NullAnnotationStrategy.INSTANCE;
/* 113 */     this
/* 114 */       .typeLiteral = MoreTypes.canonicalizeForKey(
/* 115 */         TypeLiteral.fromSuperclassTypeParameter(getClass()));
/* 116 */     this.hashCode = computeHashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Key(Type type, AnnotationStrategy annotationStrategy) {
/* 122 */     this.annotationStrategy = annotationStrategy;
/* 123 */     this.typeLiteral = MoreTypes.canonicalizeForKey(TypeLiteral.get(type));
/* 124 */     this.hashCode = computeHashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   private Key(TypeLiteral<T> typeLiteral, AnnotationStrategy annotationStrategy) {
/* 129 */     this.annotationStrategy = annotationStrategy;
/* 130 */     this.typeLiteral = MoreTypes.canonicalizeForKey(typeLiteral);
/* 131 */     this.hashCode = computeHashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   private int computeHashCode() {
/* 136 */     return this.typeLiteral.hashCode() * 31 + this.annotationStrategy.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public final TypeLiteral<T> getTypeLiteral() {
/* 141 */     return this.typeLiteral;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Class<? extends Annotation> getAnnotationType() {
/* 146 */     return this.annotationStrategy.getAnnotationType();
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
/*     */   public final Annotation getAnnotation() {
/* 159 */     return this.annotationStrategy.getAnnotation();
/*     */   }
/*     */   
/*     */   boolean hasAnnotationType() {
/* 163 */     return (this.annotationStrategy.getAnnotationType() != null);
/*     */   }
/*     */   
/*     */   String getAnnotationName() {
/* 167 */     Annotation annotation = this.annotationStrategy.getAnnotation();
/* 168 */     if (annotation != null) {
/* 169 */       return annotation.toString();
/*     */     }
/*     */ 
/*     */     
/* 173 */     return this.annotationStrategy.getAnnotationType().toString();
/*     */   }
/*     */   
/*     */   Class<? super T> getRawType() {
/* 177 */     return this.typeLiteral.getRawType();
/*     */   }
/*     */ 
/*     */   
/*     */   Key<Provider<T>> providerKey() {
/* 182 */     return ofType(this.typeLiteral.providerType());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean equals(Object o) {
/* 187 */     if (o == this) {
/* 188 */       return true;
/*     */     }
/* 190 */     if (!(o instanceof Key)) {
/* 191 */       return false;
/*     */     }
/* 193 */     Key<?> other = (Key)o;
/* 194 */     return (this.annotationStrategy.equals(other.annotationStrategy) && this.typeLiteral
/* 195 */       .equals(other.typeLiteral));
/*     */   }
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 200 */     return this.hashCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 207 */     String local = this.toString;
/* 208 */     if (local == null) {
/* 209 */       String str1 = String.valueOf(this.typeLiteral), str2 = String.valueOf(this.annotationStrategy); local = (new StringBuilder(23 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("Key[type=").append(str1).append(", annotation=").append(str2).append("]").toString();
/* 210 */       this.toString = local;
/*     */     } 
/* 212 */     return local;
/*     */   }
/*     */ 
/*     */   
/*     */   static <T> Key<T> get(Class<T> type, AnnotationStrategy annotationStrategy) {
/* 217 */     return new Key<>(type, annotationStrategy);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Key<T> get(Class<T> type) {
/* 222 */     return new Key<>(type, NullAnnotationStrategy.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Key<T> get(Class<T> type, Class<? extends Annotation> annotationType) {
/* 227 */     return new Key<>(type, strategyFor(annotationType));
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Key<T> get(Class<T> type, Annotation annotation) {
/* 232 */     return new Key<>(type, strategyFor(annotation));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Key<?> get(Type type) {
/* 237 */     return new Key(type, NullAnnotationStrategy.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Key<?> get(Type type, Class<? extends Annotation> annotationType) {
/* 242 */     return new Key(type, strategyFor(annotationType));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Key<?> get(Type type, Annotation annotation) {
/* 247 */     return new Key(type, strategyFor(annotation));
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Key<T> get(TypeLiteral<T> typeLiteral) {
/* 252 */     return new Key<>(typeLiteral, NullAnnotationStrategy.INSTANCE);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Key<T> get(TypeLiteral<T> typeLiteral, Class<? extends Annotation> annotationType) {
/* 258 */     return new Key<>(typeLiteral, strategyFor(annotationType));
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Key<T> get(TypeLiteral<T> typeLiteral, Annotation annotation) {
/* 263 */     return new Key<>(typeLiteral, strategyFor(annotation));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <U> Key<U> ofType(Class<U> type) {
/* 272 */     return new Key(type, this.annotationStrategy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Key<?> ofType(Type type) {
/* 281 */     return new Key(type, this.annotationStrategy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <U> Key<U> ofType(TypeLiteral<U> type) {
/* 290 */     return new Key(type, this.annotationStrategy);
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
/*     */   public Key<T> withAnnotation(Class<? extends Annotation> annotationType) {
/* 302 */     return new Key(this.typeLiteral, strategyFor(annotationType));
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
/*     */   public Key<T> withAnnotation(Annotation annotation) {
/* 314 */     return new Key(this.typeLiteral, strategyFor(annotation));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasAttributes() {
/* 323 */     return this.annotationStrategy.hasAttributes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Key<T> withoutAttributes() {
/* 332 */     return new Key(this.typeLiteral, this.annotationStrategy.withoutAttributes());
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
/*     */   static AnnotationStrategy strategyFor(Annotation annotation) {
/* 347 */     Preconditions.checkNotNull(annotation, "annotation");
/* 348 */     Class<? extends Annotation> annotationType = annotation.annotationType();
/* 349 */     ensureRetainedAtRuntime(annotationType);
/* 350 */     ensureIsBindingAnnotation(annotationType);
/*     */     
/* 352 */     if (Annotations.isMarker(annotationType)) {
/* 353 */       return new AnnotationTypeStrategy(annotationType, annotation);
/*     */     }
/*     */     
/* 356 */     return new AnnotationInstanceStrategy(Annotations.canonicalizeIfNamed(annotation));
/*     */   }
/*     */ 
/*     */   
/*     */   static AnnotationStrategy strategyFor(Class<? extends Annotation> annotationType) {
/* 361 */     annotationType = Annotations.canonicalizeIfNamed(annotationType);
/* 362 */     if (Annotations.isAllDefaultMethods(annotationType)) {
/* 363 */       return strategyFor(Annotations.generateAnnotation(annotationType));
/*     */     }
/*     */     
/* 366 */     Preconditions.checkNotNull(annotationType, "annotation type");
/* 367 */     ensureRetainedAtRuntime(annotationType);
/* 368 */     ensureIsBindingAnnotation(annotationType);
/* 369 */     return new AnnotationTypeStrategy(annotationType, null);
/*     */   }
/*     */   
/*     */   private static void ensureRetainedAtRuntime(Class<? extends Annotation> annotationType) {
/* 373 */     Preconditions.checkArgument(
/* 374 */         Annotations.isRetainedAtRuntime(annotationType), "%s is not retained at runtime. Please annotate it with @Retention(RUNTIME).", annotationType
/*     */         
/* 376 */         .getName());
/*     */   } static interface AnnotationStrategy {
/*     */     Annotation getAnnotation(); Class<? extends Annotation> getAnnotationType(); boolean hasAttributes(); AnnotationStrategy withoutAttributes(); }
/*     */   private static void ensureIsBindingAnnotation(Class<? extends Annotation> annotationType) {
/* 380 */     Preconditions.checkArgument(
/* 381 */         Annotations.isBindingAnnotation(annotationType), "%s is not a binding annotation. Please annotate it with @BindingAnnotation.", annotationType
/*     */         
/* 383 */         .getName());
/*     */   }
/*     */   
/*     */   enum NullAnnotationStrategy implements AnnotationStrategy {
/* 387 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public boolean hasAttributes() {
/* 391 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Key.AnnotationStrategy withoutAttributes() {
/* 396 */       throw new UnsupportedOperationException("Key already has no attributes.");
/*     */     }
/*     */ 
/*     */     
/*     */     public Annotation getAnnotation() {
/* 401 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<? extends Annotation> getAnnotationType() {
/* 406 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 411 */       return "[none]";
/*     */     }
/*     */   }
/*     */   
/*     */   static class AnnotationInstanceStrategy
/*     */     implements AnnotationStrategy
/*     */   {
/*     */     final Annotation annotation;
/*     */     
/*     */     AnnotationInstanceStrategy(Annotation annotation) {
/* 421 */       this.annotation = (Annotation)Preconditions.checkNotNull(annotation, "annotation");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasAttributes() {
/* 426 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public Key.AnnotationStrategy withoutAttributes() {
/* 431 */       return new Key.AnnotationTypeStrategy(getAnnotationType(), this.annotation);
/*     */     }
/*     */ 
/*     */     
/*     */     public Annotation getAnnotation() {
/* 436 */       return this.annotation;
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<? extends Annotation> getAnnotationType() {
/* 441 */       return this.annotation.annotationType();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 446 */       if (!(o instanceof AnnotationInstanceStrategy)) {
/* 447 */         return false;
/*     */       }
/*     */       
/* 450 */       AnnotationInstanceStrategy other = (AnnotationInstanceStrategy)o;
/* 451 */       return this.annotation.equals(other.annotation);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 456 */       return this.annotation.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 461 */       return this.annotation.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class AnnotationTypeStrategy
/*     */     implements AnnotationStrategy
/*     */   {
/*     */     final Class<? extends Annotation> annotationType;
/*     */     final Annotation annotation;
/*     */     
/*     */     AnnotationTypeStrategy(Class<? extends Annotation> annotationType, Annotation annotation) {
/* 473 */       this.annotationType = (Class<? extends Annotation>)Preconditions.checkNotNull(annotationType, "annotation type");
/* 474 */       this.annotation = annotation;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasAttributes() {
/* 479 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Key.AnnotationStrategy withoutAttributes() {
/* 484 */       throw new UnsupportedOperationException("Key already has no attributes.");
/*     */     }
/*     */ 
/*     */     
/*     */     public Annotation getAnnotation() {
/* 489 */       return this.annotation;
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<? extends Annotation> getAnnotationType() {
/* 494 */       return this.annotationType;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 499 */       if (!(o instanceof AnnotationTypeStrategy)) {
/* 500 */         return false;
/*     */       }
/*     */       
/* 503 */       AnnotationTypeStrategy other = (AnnotationTypeStrategy)o;
/* 504 */       return this.annotationType.equals(other.annotationType);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 509 */       return this.annotationType.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 514 */       String.valueOf(this.annotationType.getName()); return (String.valueOf(this.annotationType.getName()).length() != 0) ? "@".concat(String.valueOf(this.annotationType.getName())) : new String("@");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\Key.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */