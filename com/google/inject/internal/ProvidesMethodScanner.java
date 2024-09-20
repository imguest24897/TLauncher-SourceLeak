/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.inject.Binder;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.Provides;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.multibindings.MapKey;
/*     */ import com.google.inject.multibindings.ProvidesIntoMap;
/*     */ import com.google.inject.multibindings.ProvidesIntoOptional;
/*     */ import com.google.inject.multibindings.ProvidesIntoSet;
/*     */ import com.google.inject.spi.InjectionPoint;
/*     */ import com.google.inject.spi.ModuleAnnotatedMethodScanner;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Set;
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
/*     */ final class ProvidesMethodScanner
/*     */   extends ModuleAnnotatedMethodScanner
/*     */ {
/*  43 */   static final ProvidesMethodScanner INSTANCE = new ProvidesMethodScanner();
/*     */   
/*  45 */   private static final ImmutableSet<Class<? extends Annotation>> ANNOTATIONS = ImmutableSet.of(Provides.class, ProvidesIntoSet.class, ProvidesIntoMap.class, ProvidesIntoOptional.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<? extends Class<? extends Annotation>> annotationClasses() {
/*  52 */     return (Set<? extends Class<? extends Annotation>>)ANNOTATIONS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Key<T> prepareMethod(Binder binder, Annotation annotation, Key<T> key, InjectionPoint injectionPoint) {
/*  59 */     Method method = (Method)injectionPoint.getMember();
/*  60 */     AnnotationOrError mapKey = findMapKeyAnnotation(binder, method);
/*  61 */     if (annotation instanceof Provides) {
/*  62 */       if (mapKey.annotation != null) {
/*  63 */         binder.addError("Found a MapKey annotation on non map binding at %s.", new Object[] { method });
/*     */       }
/*     */       
/*  66 */       return key;
/*     */     } 
/*  68 */     if (annotation instanceof ProvidesIntoSet) {
/*  69 */       if (mapKey.annotation != null) {
/*  70 */         binder.addError("Found a MapKey annotation on non map binding at %s.", new Object[] { method });
/*     */       }
/*  72 */       return RealMultibinder.<T>newRealSetBinder(binder, key).getKeyForNewItem();
/*  73 */     }  if (annotation instanceof ProvidesIntoMap) {
/*  74 */       if (mapKey.error)
/*     */       {
/*  76 */         return key;
/*     */       }
/*  78 */       if (mapKey.annotation == null) {
/*     */         
/*  80 */         binder.addError("No MapKey found for map binding at %s.", new Object[] { method });
/*  81 */         return key;
/*     */       } 
/*  83 */       TypeAndValue<?> typeAndValue = typeAndValueOfMapKey(mapKey.annotation);
/*  84 */       return RealMapBinder.<T, T>newRealMapBinder(binder, (TypeLiteral)typeAndValue.type, key)
/*  85 */         .getKeyForNewValue(typeAndValue.value);
/*  86 */     }  if (annotation instanceof ProvidesIntoOptional) {
/*  87 */       if (mapKey.annotation != null) {
/*  88 */         binder.addError("Found a MapKey annotation on non map binding at %s.", new Object[] { method });
/*     */       }
/*  90 */       switch (((ProvidesIntoOptional)annotation).value()) {
/*     */         case DEFAULT:
/*  92 */           return RealOptionalBinder.<T>newRealOptionalBinder(binder, key).getKeyForDefaultBinding();
/*     */         case ACTUAL:
/*  94 */           return RealOptionalBinder.<T>newRealOptionalBinder(binder, key).getKeyForActualBinding();
/*     */       } 
/*     */     } 
/*  97 */     String str = String.valueOf(annotation); throw new IllegalStateException((new StringBuilder(20 + String.valueOf(str).length())).append("Invalid annotation: ").append(str).toString());
/*     */   }
/*     */   
/*     */   private static class AnnotationOrError {
/*     */     final Annotation annotation;
/*     */     final boolean error;
/*     */     
/*     */     AnnotationOrError(Annotation annotation, boolean error) {
/* 105 */       this.annotation = annotation;
/* 106 */       this.error = error;
/*     */     }
/*     */     
/*     */     static AnnotationOrError forPossiblyNullAnnotation(Annotation annotation) {
/* 110 */       return new AnnotationOrError(annotation, false);
/*     */     }
/*     */     
/*     */     static AnnotationOrError forError() {
/* 114 */       return new AnnotationOrError(null, true);
/*     */     }
/*     */   }
/*     */   
/*     */   private static AnnotationOrError findMapKeyAnnotation(Binder binder, Method method) {
/* 119 */     Annotation foundAnnotation = null;
/* 120 */     for (Annotation annotation : method.getAnnotations()) {
/* 121 */       MapKey mapKey = annotation.annotationType().<MapKey>getAnnotation(MapKey.class);
/* 122 */       if (mapKey != null) {
/* 123 */         if (foundAnnotation != null) {
/* 124 */           binder.addError("Found more than one MapKey annotations on %s.", new Object[] { method });
/* 125 */           return AnnotationOrError.forError();
/*     */         } 
/* 127 */         if (mapKey.unwrapValue()) {
/*     */           
/*     */           try {
/* 130 */             Method valueMethod = annotation.annotationType().getDeclaredMethod("value", new Class[0]);
/* 131 */             if (valueMethod.getReturnType().isArray()) {
/* 132 */               binder.addError("Array types are not allowed in a MapKey with unwrapValue=true: %s", new Object[] { annotation
/*     */                     
/* 134 */                     .annotationType() });
/* 135 */               return AnnotationOrError.forError();
/*     */             } 
/* 137 */           } catch (NoSuchMethodException invalid) {
/* 138 */             binder.addError("No 'value' method in MapKey with unwrapValue=true: %s", new Object[] { annotation
/*     */                   
/* 140 */                   .annotationType() });
/* 141 */             return AnnotationOrError.forError();
/*     */           } 
/*     */         }
/* 144 */         foundAnnotation = annotation;
/*     */       } 
/*     */     } 
/* 147 */     return AnnotationOrError.forPossiblyNullAnnotation(foundAnnotation);
/*     */   }
/*     */ 
/*     */   
/*     */   static TypeAndValue<?> typeAndValueOfMapKey(Annotation mapKeyAnnotation) {
/* 152 */     if (!((MapKey)mapKeyAnnotation.annotationType().<MapKey>getAnnotation(MapKey.class)).unwrapValue()) {
/* 153 */       return new TypeAndValue(TypeLiteral.get(mapKeyAnnotation.annotationType()), mapKeyAnnotation);
/*     */     }
/*     */     try {
/* 156 */       Method valueMethod = mapKeyAnnotation.annotationType().getDeclaredMethod("value", new Class[0]);
/* 157 */       valueMethod.setAccessible(true);
/*     */       
/* 159 */       TypeLiteral<?> returnType = TypeLiteral.get(mapKeyAnnotation.annotationType()).getReturnType(valueMethod);
/* 160 */       return new TypeAndValue(returnType, valueMethod.invoke(mapKeyAnnotation, new Object[0]));
/* 161 */     } catch (NoSuchMethodException e) {
/* 162 */       throw new IllegalStateException(e);
/* 163 */     } catch (SecurityException e) {
/* 164 */       throw new IllegalStateException(e);
/* 165 */     } catch (IllegalAccessException e) {
/* 166 */       throw new IllegalStateException(e);
/* 167 */     } catch (InvocationTargetException e) {
/* 168 */       throw new IllegalStateException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class TypeAndValue<T>
/*     */   {
/*     */     final TypeLiteral<T> type;
/*     */     final T value;
/*     */     
/*     */     TypeAndValue(TypeLiteral<T> type, T value) {
/* 178 */       this.type = type;
/* 179 */       this.value = value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ProvidesMethodScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */