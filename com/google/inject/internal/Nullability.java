/*    */ package com.google.inject.internal;
/*    */ 
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
/*    */ public class Nullability
/*    */ {
/*    */   public static boolean hasNullableAnnotation(Annotation[] annotations) {
/* 40 */     for (Annotation a : annotations) {
/* 41 */       Class<? extends Annotation> type = a.annotationType();
/* 42 */       if ("Nullable".equals(type.getSimpleName())) {
/* 43 */         return true;
/*    */       }
/*    */     } 
/* 46 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\Nullability.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */