/*    */ package com.google.inject.multibindings;
/*    */ 
/*    */ import java.lang.annotation.Documented;
/*    */ import java.lang.annotation.ElementType;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
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
/*    */ 
/*    */ @Documented
/*    */ @Target({ElementType.METHOD})
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ public @interface ProvidesIntoOptional
/*    */ {
/*    */   Type value();
/*    */   
/*    */   public enum Type
/*    */   {
/* 54 */     ACTUAL,
/*    */ 
/*    */     
/* 57 */     DEFAULT;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\multibindings\ProvidesIntoOptional.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */