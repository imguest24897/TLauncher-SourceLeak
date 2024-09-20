/*     */ package org.apache.commons.lang3.reflect;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.Objects;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.ClassUtils;
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
/*     */ public class ConstructorUtils
/*     */ {
/*     */   public static <T> T invokeConstructor(Class<T> cls, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
/*  81 */     args = ArrayUtils.nullToEmpty(args);
/*  82 */     return invokeConstructor(cls, args, ClassUtils.toClass(args));
/*     */   }
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
/*     */   public static <T> T invokeConstructor(Class<T> cls, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
/* 108 */     args = ArrayUtils.nullToEmpty(args);
/* 109 */     parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
/* 110 */     Constructor<T> ctor = getMatchingAccessibleConstructor(cls, parameterTypes);
/* 111 */     if (ctor == null) {
/* 112 */       throw new NoSuchMethodException("No such accessible constructor on object: " + cls
/* 113 */           .getName());
/*     */     }
/* 115 */     if (ctor.isVarArgs()) {
/* 116 */       Class<?>[] methodParameterTypes = ctor.getParameterTypes();
/* 117 */       args = MethodUtils.getVarArgs(args, methodParameterTypes);
/*     */     } 
/* 119 */     return ctor.newInstance(args);
/*     */   }
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
/*     */   public static <T> T invokeExactConstructor(Class<T> cls, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
/* 144 */     args = ArrayUtils.nullToEmpty(args);
/* 145 */     return invokeExactConstructor(cls, args, ClassUtils.toClass(args));
/*     */   }
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
/*     */   public static <T> T invokeExactConstructor(Class<T> cls, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
/* 171 */     args = ArrayUtils.nullToEmpty(args);
/* 172 */     parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
/* 173 */     Constructor<T> ctor = getAccessibleConstructor(cls, parameterTypes);
/* 174 */     if (ctor == null) {
/* 175 */       throw new NoSuchMethodException("No such accessible constructor on object: " + cls
/* 176 */           .getName());
/*     */     }
/* 178 */     return ctor.newInstance(args);
/*     */   }
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
/*     */   public static <T> Constructor<T> getAccessibleConstructor(Class<T> cls, Class<?>... parameterTypes) {
/* 197 */     Objects.requireNonNull(cls, "cls");
/*     */     try {
/* 199 */       return getAccessibleConstructor(cls.getConstructor(parameterTypes));
/* 200 */     } catch (NoSuchMethodException e) {
/* 201 */       return null;
/*     */     } 
/*     */   }
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
/*     */   public static <T> Constructor<T> getAccessibleConstructor(Constructor<T> ctor) {
/* 217 */     Objects.requireNonNull(ctor, "ctor");
/* 218 */     return (MemberUtils.isAccessible(ctor) && 
/* 219 */       isAccessible(ctor.getDeclaringClass())) ? ctor : null;
/*     */   }
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
/*     */   public static <T> Constructor<T> getMatchingAccessibleConstructor(Class<T> cls, Class<?>... parameterTypes) {
/* 242 */     Objects.requireNonNull(cls, "cls");
/*     */ 
/*     */     
/*     */     try {
/* 246 */       return MemberUtils.<Constructor<T>>setAccessibleWorkaround(cls.getConstructor(parameterTypes));
/* 247 */     } catch (NoSuchMethodException noSuchMethodException) {
/*     */ 
/*     */       
/* 250 */       Constructor<T> result = null;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 255 */       Constructor[] arrayOfConstructor = (Constructor[])cls.getConstructors();
/*     */ 
/*     */       
/* 258 */       for (Constructor<?> ctor : arrayOfConstructor) {
/*     */         
/* 260 */         if (MemberUtils.isMatchingConstructor(ctor, parameterTypes)) {
/*     */           
/* 262 */           ctor = getAccessibleConstructor(ctor);
/* 263 */           if (ctor != null) {
/* 264 */             MemberUtils.setAccessibleWorkaround(ctor);
/* 265 */             if (result == null || MemberUtils.compareConstructorFit(ctor, result, parameterTypes) < 0) {
/*     */ 
/*     */               
/* 268 */               Constructor<T> constructor = (Constructor)ctor;
/* 269 */               result = constructor;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 274 */       return result;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isAccessible(Class<?> type) {
/* 285 */     Class<?> cls = type;
/* 286 */     while (cls != null) {
/* 287 */       if (!ClassUtils.isPublic(cls)) {
/* 288 */         return false;
/*     */       }
/* 290 */       cls = cls.getEnclosingClass();
/*     */     } 
/* 292 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\reflect\ConstructorUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */