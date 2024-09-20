/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.function.Predicate;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class KotlinSupport
/*    */ {
/*    */   public static KotlinSupportInterface getInstance() {
/* 18 */     return KotlinSupportHolder.INSTANCE;
/*    */   }
/*    */   
/*    */   private static class KotlinSupportHolder {
/* 22 */     static final KotlinSupportInterface INSTANCE = KotlinSupport.loadKotlinSupport();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static KotlinSupportInterface loadKotlinSupport() {
/*    */     try {
/* 30 */       Class<? extends KotlinSupportInterface> kotlinSupportClass = (Class)Class.forName("com.google.inject.kotlin.KotlinSupportImpl");
/* 31 */       Field instance = kotlinSupportClass.getField("INSTANCE");
/* 32 */       instance.setAccessible(true);
/* 33 */       return (KotlinSupportInterface)instance.get(null);
/* 34 */     } catch (ReflectiveOperationException e) {
/* 35 */       return new KotlinUnsupported();
/*    */     } 
/*    */   }
/*    */   
/*    */   private static class KotlinUnsupported implements KotlinSupportInterface {
/* 40 */     static final Annotation[] NO_ANNOTATIONS = new Annotation[0];
/*    */     private KotlinUnsupported() {}
/*    */     static final Predicate<Integer> FALSE_PREDICATE = integer -> false;
/*    */     
/*    */     public Annotation[] getAnnotations(Field field) {
/* 45 */       return NO_ANNOTATIONS;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isNullable(Field field) {
/* 50 */       return false;
/*    */     }
/*    */ 
/*    */     
/*    */     public Predicate<Integer> getIsParameterKotlinNullablePredicate(Constructor<?> constructor) {
/* 55 */       return FALSE_PREDICATE;
/*    */     }
/*    */ 
/*    */     
/*    */     public Predicate<Integer> getIsParameterKotlinNullablePredicate(Method method) {
/* 60 */       return FALSE_PREDICATE;
/*    */     }
/*    */     
/*    */     public void checkConstructorParameterAnnotations(Constructor<?> constructor, Errors errors) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\KotlinSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */