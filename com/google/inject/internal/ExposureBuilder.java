/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.inject.Binder;
/*    */ import com.google.inject.Key;
/*    */ import com.google.inject.binder.AnnotatedElementBuilder;
/*    */ import java.lang.annotation.Annotation;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExposureBuilder<T>
/*    */   implements AnnotatedElementBuilder
/*    */ {
/*    */   private final Binder binder;
/*    */   private final Object source;
/*    */   private Key<T> key;
/*    */   
/*    */   public ExposureBuilder(Binder binder, Object source, Key<T> key) {
/* 32 */     this.binder = binder;
/* 33 */     this.source = source;
/* 34 */     this.key = key;
/*    */   }
/*    */   
/*    */   protected void checkNotAnnotated() {
/* 38 */     if (this.key.getAnnotationType() != null) {
/* 39 */       this.binder.addError("More than one annotation is specified for this binding.", new Object[0]);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void annotatedWith(Class<? extends Annotation> annotationType) {
/* 45 */     Preconditions.checkNotNull(annotationType, "annotationType");
/* 46 */     checkNotAnnotated();
/* 47 */     this.key = this.key.withAnnotation(annotationType);
/*    */   }
/*    */ 
/*    */   
/*    */   public void annotatedWith(Annotation annotation) {
/* 52 */     Preconditions.checkNotNull(annotation, "annotation");
/* 53 */     checkNotAnnotated();
/* 54 */     this.key = this.key.withAnnotation(annotation);
/*    */   }
/*    */   
/*    */   public Key<?> getKey() {
/* 58 */     return this.key;
/*    */   }
/*    */   
/*    */   public Object getSource() {
/* 62 */     return this.source;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 67 */     return "AnnotatedElementBuilder";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ExposureBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */