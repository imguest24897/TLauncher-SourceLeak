/*    */ package com.google.inject.multibindings;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.inject.Binder;
/*    */ import com.google.inject.Key;
/*    */ import com.google.inject.Module;
/*    */ import com.google.inject.spi.InjectionPoint;
/*    */ import com.google.inject.spi.ModuleAnnotatedMethodScanner;
/*    */ import com.google.inject.util.Modules;
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.util.Set;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class MultibindingsScanner
/*    */ {
/*    */   @Deprecated
/*    */   public static Module asModule() {
/* 53 */     return Modules.EMPTY_MODULE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public static ModuleAnnotatedMethodScanner scanner() {
/* 62 */     return new ModuleAnnotatedMethodScanner()
/*    */       {
/*    */         public Set<? extends Class<? extends Annotation>> annotationClasses() {
/* 65 */           return (Set<? extends Class<? extends Annotation>>)ImmutableSet.of();
/*    */         }
/*    */ 
/*    */ 
/*    */         
/*    */         public <T> Key<T> prepareMethod(Binder binder, Annotation annotation, Key<T> key, InjectionPoint injectionPoint) {
/* 71 */           String str = String.valueOf(annotation); throw new IllegalStateException((new StringBuilder(23 + String.valueOf(str).length())).append("Unexpected annotation: ").append(str).toString());
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\multibindings\MultibindingsScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */