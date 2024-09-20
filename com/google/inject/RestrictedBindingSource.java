/*     */ package com.google.inject;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Documented;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.annotation.Inherited;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.annotation.Target;
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
/*     */ @Inherited
/*     */ @Retention(RetentionPolicy.RUNTIME)
/*     */ @Target({ElementType.TYPE})
/*     */ public @interface RestrictedBindingSource
/*     */ {
/*     */   String explanation();
/*     */   
/*     */   Class<? extends Annotation>[] permits();
/*     */   
/*     */   String exemptModules() default "";
/*     */   
/*     */   RestrictionLevel restrictionLevel() default RestrictionLevel.ERROR;
/*     */   
/*     */   public enum RestrictionLevel
/*     */   {
/* 115 */     WARNING,
/* 116 */     ERROR;
/*     */   }
/*     */   
/*     */   @Documented
/*     */   @Retention(RetentionPolicy.RUNTIME)
/*     */   @Target({ElementType.ANNOTATION_TYPE})
/*     */   public static @interface Permit {}
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\RestrictedBindingSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */