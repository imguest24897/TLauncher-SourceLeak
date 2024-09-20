/*     */ package org.apache.commons.lang3.reflect;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
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
/*     */ final class MemberUtils
/*     */ {
/*     */   private static final int ACCESS_TEST = 7;
/*  39 */   private static final Class<?>[] ORDERED_PRIMITIVE_TYPES = new Class[] { byte.class, short.class, char.class, int.class, long.class, float.class, double.class };
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
/*     */   static <T extends java.lang.reflect.AccessibleObject> T setAccessibleWorkaround(T obj) {
/*  56 */     if (obj == null || obj.isAccessible()) {
/*  57 */       return obj;
/*     */     }
/*  59 */     Member m = (Member)obj;
/*  60 */     if (!obj.isAccessible() && isPublic(m) && isPackageAccess(m.getDeclaringClass().getModifiers())) {
/*     */       try {
/*  62 */         obj.setAccessible(true);
/*  63 */         return obj;
/*  64 */       } catch (SecurityException securityException) {}
/*     */     }
/*     */ 
/*     */     
/*  68 */     return obj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isPackageAccess(int modifiers) {
/*  77 */     return ((modifiers & 0x7) == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isPublic(Member member) {
/*  86 */     return (member != null && Modifier.isPublic(member.getModifiers()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isStatic(Member member) {
/*  95 */     return (member != null && Modifier.isStatic(member.getModifiers()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isAccessible(Member member) {
/* 104 */     return (isPublic(member) && !member.isSynthetic());
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
/*     */   static int compareConstructorFit(Constructor<?> left, Constructor<?> right, Class<?>[] actual) {
/* 121 */     return compareParameterTypes(Executable.of(left), Executable.of(right), actual);
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
/*     */   static int compareMethodFit(Method left, Method right, Class<?>[] actual) {
/* 138 */     return compareParameterTypes(Executable.of(left), Executable.of(right), actual);
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
/*     */   private static int compareParameterTypes(Executable left, Executable right, Class<?>[] actual) {
/* 154 */     float leftCost = getTotalTransformationCost(actual, left);
/* 155 */     float rightCost = getTotalTransformationCost(actual, right);
/* 156 */     return Float.compare(leftCost, rightCost);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static float getTotalTransformationCost(Class<?>[] srcArgs, Executable executable) {
/* 167 */     Class<?>[] destArgs = executable.getParameterTypes();
/* 168 */     boolean isVarArgs = executable.isVarArgs();
/*     */ 
/*     */     
/* 171 */     float totalCost = 0.0F;
/* 172 */     long normalArgsLen = isVarArgs ? (destArgs.length - 1) : destArgs.length;
/* 173 */     if (srcArgs.length < normalArgsLen) {
/* 174 */       return Float.MAX_VALUE;
/*     */     }
/* 176 */     for (int i = 0; i < normalArgsLen; i++) {
/* 177 */       totalCost += getObjectTransformationCost(srcArgs[i], destArgs[i]);
/*     */     }
/* 179 */     if (isVarArgs) {
/*     */ 
/*     */       
/* 182 */       boolean noVarArgsPassed = (srcArgs.length < destArgs.length);
/*     */       
/* 184 */       boolean explicitArrayForVarargs = (srcArgs.length == destArgs.length && srcArgs[srcArgs.length - 1] != null && srcArgs[srcArgs.length - 1].isArray());
/*     */       
/* 186 */       float varArgsCost = 0.001F;
/* 187 */       Class<?> destClass = destArgs[destArgs.length - 1].getComponentType();
/* 188 */       if (noVarArgsPassed) {
/*     */         
/* 190 */         totalCost += getObjectTransformationCost(destClass, Object.class) + 0.001F;
/* 191 */       } else if (explicitArrayForVarargs) {
/* 192 */         Class<?> sourceClass = srcArgs[srcArgs.length - 1].getComponentType();
/* 193 */         totalCost += getObjectTransformationCost(sourceClass, destClass) + 0.001F;
/*     */       } else {
/*     */         
/* 196 */         for (int j = destArgs.length - 1; j < srcArgs.length; j++) {
/* 197 */           Class<?> srcClass = srcArgs[j];
/* 198 */           totalCost += getObjectTransformationCost(srcClass, destClass) + 0.001F;
/*     */         } 
/*     */       } 
/*     */     } 
/* 202 */     return totalCost;
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
/*     */   private static float getObjectTransformationCost(Class<?> srcClass, Class<?> destClass) {
/* 214 */     if (destClass.isPrimitive()) {
/* 215 */       return getPrimitivePromotionCost(srcClass, destClass);
/*     */     }
/* 217 */     float cost = 0.0F;
/* 218 */     while (srcClass != null && !destClass.equals(srcClass)) {
/* 219 */       if (destClass.isInterface() && ClassUtils.isAssignable(srcClass, destClass)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 225 */         cost += 0.25F;
/*     */         break;
/*     */       } 
/* 228 */       cost++;
/* 229 */       srcClass = srcClass.getSuperclass();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 235 */     if (srcClass == null) {
/* 236 */       cost += 1.5F;
/*     */     }
/* 238 */     return cost;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static float getPrimitivePromotionCost(Class<?> srcClass, Class<?> destClass) {
/* 249 */     if (srcClass == null) {
/* 250 */       return 1.5F;
/*     */     }
/* 252 */     float cost = 0.0F;
/* 253 */     Class<?> cls = srcClass;
/* 254 */     if (!cls.isPrimitive()) {
/*     */       
/* 256 */       cost += 0.1F;
/* 257 */       cls = ClassUtils.wrapperToPrimitive(cls);
/*     */     } 
/* 259 */     for (int i = 0; cls != destClass && i < ORDERED_PRIMITIVE_TYPES.length; i++) {
/* 260 */       if (cls == ORDERED_PRIMITIVE_TYPES[i]) {
/* 261 */         cost += 0.1F;
/* 262 */         if (i < ORDERED_PRIMITIVE_TYPES.length - 1) {
/* 263 */           cls = ORDERED_PRIMITIVE_TYPES[i + 1];
/*     */         }
/*     */       } 
/*     */     } 
/* 267 */     return cost;
/*     */   }
/*     */   
/*     */   static boolean isMatchingMethod(Method method, Class<?>[] parameterTypes) {
/* 271 */     return isMatchingExecutable(Executable.of(method), parameterTypes);
/*     */   }
/*     */   
/*     */   static boolean isMatchingConstructor(Constructor<?> method, Class<?>[] parameterTypes) {
/* 275 */     return isMatchingExecutable(Executable.of(method), parameterTypes);
/*     */   }
/*     */   
/*     */   private static boolean isMatchingExecutable(Executable method, Class<?>[] parameterTypes) {
/* 279 */     Class<?>[] methodParameterTypes = method.getParameterTypes();
/* 280 */     if (ClassUtils.isAssignable(parameterTypes, methodParameterTypes, true)) {
/* 281 */       return true;
/*     */     }
/*     */     
/* 284 */     if (method.isVarArgs()) {
/*     */       int i;
/* 286 */       for (i = 0; i < methodParameterTypes.length - 1 && i < parameterTypes.length; i++) {
/* 287 */         if (!ClassUtils.isAssignable(parameterTypes[i], methodParameterTypes[i], true)) {
/* 288 */           return false;
/*     */         }
/*     */       } 
/* 291 */       Class<?> varArgParameterType = methodParameterTypes[methodParameterTypes.length - 1].getComponentType();
/* 292 */       for (; i < parameterTypes.length; i++) {
/* 293 */         if (!ClassUtils.isAssignable(parameterTypes[i], varArgParameterType, true)) {
/* 294 */           return false;
/*     */         }
/*     */       } 
/* 297 */       return true;
/*     */     } 
/*     */     
/* 300 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class Executable
/*     */   {
/*     */     private final Class<?>[] parameterTypes;
/*     */     
/*     */     private final boolean isVarArgs;
/*     */ 
/*     */     
/*     */     private static Executable of(Method method) {
/* 312 */       return new Executable(method);
/*     */     }
/*     */     
/*     */     private static Executable of(Constructor<?> constructor) {
/* 316 */       return new Executable(constructor);
/*     */     }
/*     */     
/*     */     private Executable(Method method) {
/* 320 */       this.parameterTypes = method.getParameterTypes();
/* 321 */       this.isVarArgs = method.isVarArgs();
/*     */     }
/*     */     
/*     */     private Executable(Constructor<?> constructor) {
/* 325 */       this.parameterTypes = constructor.getParameterTypes();
/* 326 */       this.isVarArgs = constructor.isVarArgs();
/*     */     }
/*     */     
/*     */     public Class<?>[] getParameterTypes() {
/* 330 */       return this.parameterTypes;
/*     */     }
/*     */     
/*     */     public boolean isVarArgs() {
/* 334 */       return this.isVarArgs;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\reflect\MemberUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */