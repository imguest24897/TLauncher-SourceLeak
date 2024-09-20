/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.inject.BindingAnnotation;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
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
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ @BindingAnnotation
/*    */ @interface Element
/*    */ {
/*    */   String setName();
/*    */   
/*    */   int uniqueId();
/*    */   
/*    */   Type type();
/*    */   
/*    */   String keyType();
/*    */   
/*    */   public enum Type
/*    */   {
/* 36 */     MAPBINDER,
/* 37 */     MULTIBINDER;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\Element.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */