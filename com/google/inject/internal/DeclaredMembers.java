/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.collect.Ordering;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Arrays;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
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
/*    */ public final class DeclaredMembers
/*    */ {
/*    */   public static Field[] getDeclaredFields(Class<?> type) {
/* 40 */     return (Field[])Arrays.<Field>stream(type.getDeclaredFields())
/* 41 */       .sorted(
/* 42 */         Comparator.comparing(Field::getName)
/* 43 */         .thenComparing(Field::getType, Comparator.comparing(Class::getName)))
/* 44 */       .toArray(x$0 -> new Field[x$0]);
/*    */   }
/*    */   
/*    */   public static Method[] getDeclaredMethods(Class<?> type) {
/* 48 */     return (Method[])Arrays.<Method>stream(type.getDeclaredMethods())
/* 49 */       .sorted(
/* 50 */         Comparator.comparing(Method::getName)
/* 51 */         .thenComparing(Method::getReturnType, Comparator.comparing(Class::getName))
/* 52 */         .thenComparing(method -> Arrays.asList(method.getParameterTypes()), 
/*    */ 
/*    */           
/* 55 */           (Comparator)Ordering.from(Comparator.comparing(Class::getName))
/* 56 */           .lexicographical()))
/* 57 */       .toArray(x$0 -> new Method[x$0]);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\DeclaredMembers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */