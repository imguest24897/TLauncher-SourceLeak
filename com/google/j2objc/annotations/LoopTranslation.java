/*    */ package com.google.j2objc.annotations;
/*    */ 
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
/*    */ @Target({ElementType.LOCAL_VARIABLE})
/*    */ @Retention(RetentionPolicy.SOURCE)
/*    */ public @interface LoopTranslation
/*    */ {
/*    */   LoopStyle value();
/*    */   
/*    */   public enum LoopStyle
/*    */   {
/* 44 */     JAVA_ITERATOR,
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 50 */     FAST_ENUMERATION;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\j2objc\annotations\LoopTranslation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */