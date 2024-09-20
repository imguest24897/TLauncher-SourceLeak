/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
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
/*    */ final class Java8Usage
/*    */ {
/*    */   @CanIgnoreReturnValue
/*    */   static String performCheck() {
/* 39 */     Runnable r = () -> { 
/* 40 */       }; r.run();
/* 41 */     return "";
/*    */   }
/*    */   
/*    */   @Target({ElementType.TYPE_USE})
/*    */   @Retention(RetentionPolicy.RUNTIME)
/*    */   private static @interface SomeTypeAnnotation {}
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\base\Java8Usage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */