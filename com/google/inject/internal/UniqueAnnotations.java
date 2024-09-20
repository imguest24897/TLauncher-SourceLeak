/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.inject.BindingAnnotation;
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
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
/*    */ public class UniqueAnnotations
/*    */ {
/* 30 */   private static final AtomicInteger nextUniqueValue = new AtomicInteger(1);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Annotation create() {
/* 37 */     return create(nextUniqueValue.getAndIncrement());
/*    */   }
/*    */   
/*    */   static Annotation create(final int value) {
/* 41 */     return new Internal()
/*    */       {
/*    */         public int value() {
/* 44 */           return value;
/*    */         }
/*    */ 
/*    */         
/*    */         public Class<? extends Annotation> annotationType() {
/* 49 */           return (Class)UniqueAnnotations.Internal.class;
/*    */         }
/*    */ 
/*    */ 
/*    */         
/*    */         public String toString() {
/* 55 */           String str1 = UniqueAnnotations.Internal.class.getName();
/*    */           
/* 57 */           String str2 = Annotations.memberValueString("value", Integer.valueOf(value)); return (new StringBuilder(3 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("@").append(str1).append("(").append(str2).append(")").toString();
/*    */         }
/*    */ 
/*    */ 
/*    */         
/*    */         public boolean equals(Object o) {
/* 63 */           return (o instanceof UniqueAnnotations.Internal && ((UniqueAnnotations.Internal)o).value() == value());
/*    */         }
/*    */ 
/*    */         
/*    */         public int hashCode() {
/* 68 */           return 127 * "value".hashCode() ^ value;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   @Retention(RetentionPolicy.RUNTIME)
/*    */   @BindingAnnotation
/*    */   static @interface Internal {
/*    */     int value();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\UniqueAnnotations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */