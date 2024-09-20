/*    */ package com.google.inject.internal.util;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Member;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Modifier;
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
/*    */ public final class Classes
/*    */ {
/*    */   public static boolean isInnerClass(Class<?> clazz) {
/* 31 */     return (!Modifier.isStatic(clazz.getModifiers()) && clazz.getEnclosingClass() != null);
/*    */   }
/*    */   
/*    */   public static boolean isConcrete(Class<?> clazz) {
/* 35 */     int modifiers = clazz.getModifiers();
/* 36 */     return (!clazz.isInterface() && !Modifier.isAbstract(modifiers));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String toString(Member member) {
/* 44 */     Class<? extends Member> memberType = memberType(member);
/*    */     
/* 46 */     if (memberType == Method.class) {
/* 47 */       String str1 = member.getDeclaringClass().getName(), str2 = member.getName(); return (new StringBuilder(3 + String.valueOf(str1).length() + String.valueOf(str2).length())).append(str1).append(".").append(str2).append("()").toString();
/* 48 */     }  if (memberType == Field.class) {
/* 49 */       String str1 = member.getDeclaringClass().getName(), str2 = member.getName(); return (new StringBuilder(1 + String.valueOf(str1).length() + String.valueOf(str2).length())).append(str1).append(".").append(str2).toString();
/* 50 */     }  if (memberType == Constructor.class) {
/* 51 */       return String.valueOf(member.getDeclaringClass().getName()).concat(".<init>()");
/*    */     }
/* 53 */     throw new AssertionError();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static Class<? extends Member> memberType(Member member) {
/* 59 */     Preconditions.checkNotNull(member, "member");
/*    */     
/* 61 */     if (member instanceof Field) {
/* 62 */       return (Class)Field.class;
/*    */     }
/* 64 */     if (member instanceof Method) {
/* 65 */       return (Class)Method.class;
/*    */     }
/* 67 */     if (member instanceof Constructor) {
/* 68 */       return (Class)Constructor.class;
/*    */     }
/*    */ 
/*    */     
/* 72 */     String str = String.valueOf(member.getClass()); throw new IllegalArgumentException((new StringBuilder(45 + String.valueOf(str).length())).append("Unsupported implementation class for Member, ").append(str).toString());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\interna\\util\Classes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */