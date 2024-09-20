/*      */ package org.apache.commons.lang3.reflect;
/*      */ 
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Type;
/*      */ import java.lang.reflect.TypeVariable;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Comparator;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.TreeMap;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import org.apache.commons.lang3.ArrayUtils;
/*      */ import org.apache.commons.lang3.ClassUtils;
/*      */ import org.apache.commons.lang3.Validate;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class MethodUtils
/*      */ {
/*   64 */   private static final Comparator<Method> METHOD_BY_SIGNATURE = Comparator.comparing(Method::toString);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object invokeMethod(Object object, String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/*   98 */     return invokeMethod(object, methodName, ArrayUtils.EMPTY_OBJECT_ARRAY, (Class<?>[])null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object invokeMethod(Object object, boolean forceAccess, String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/*  121 */     return invokeMethod(object, forceAccess, methodName, ArrayUtils.EMPTY_OBJECT_ARRAY, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object invokeMethod(Object object, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/*  150 */     args = ArrayUtils.nullToEmpty(args);
/*  151 */     return invokeMethod(object, methodName, args, ClassUtils.toClass(args));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object invokeMethod(Object object, boolean forceAccess, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/*  180 */     args = ArrayUtils.nullToEmpty(args);
/*  181 */     return invokeMethod(object, forceAccess, methodName, args, ClassUtils.toClass(args));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object invokeMethod(Object object, boolean forceAccess, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/*      */     String messagePrefix;
/*      */     Method method;
/*  206 */     Objects.requireNonNull(object, "object");
/*  207 */     parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
/*  208 */     args = ArrayUtils.nullToEmpty(args);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  213 */     Class<? extends Object> cls = (Class)object.getClass();
/*  214 */     if (forceAccess) {
/*  215 */       messagePrefix = "No such method: ";
/*  216 */       method = getMatchingMethod(cls, methodName, parameterTypes);
/*  217 */       if (method != null && !method.isAccessible()) {
/*  218 */         method.setAccessible(true);
/*      */       }
/*      */     } else {
/*  221 */       messagePrefix = "No such accessible method: ";
/*  222 */       method = getMatchingAccessibleMethod(cls, methodName, parameterTypes);
/*      */     } 
/*      */     
/*  225 */     if (method == null) {
/*  226 */       throw new NoSuchMethodException(messagePrefix + methodName + "() on object: " + cls.getName());
/*      */     }
/*  228 */     args = toVarArgs(method, args);
/*      */     
/*  230 */     return method.invoke(object, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object invokeMethod(Object object, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/*  256 */     return invokeMethod(object, false, methodName, args, parameterTypes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object invokeExactMethod(Object object, String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/*  280 */     return invokeExactMethod(object, methodName, ArrayUtils.EMPTY_OBJECT_ARRAY, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object invokeExactMethod(Object object, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/*  304 */     args = ArrayUtils.nullToEmpty(args);
/*  305 */     return invokeExactMethod(object, methodName, args, ClassUtils.toClass(args));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object invokeExactMethod(Object object, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/*  330 */     Objects.requireNonNull(object, "object");
/*  331 */     args = ArrayUtils.nullToEmpty(args);
/*  332 */     parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
/*  333 */     Class<?> cls = object.getClass();
/*  334 */     Method method = getAccessibleMethod(cls, methodName, parameterTypes);
/*  335 */     if (method == null) {
/*  336 */       throw new NoSuchMethodException("No such accessible method: " + methodName + "() on object: " + cls.getName());
/*      */     }
/*  338 */     return method.invoke(object, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object invokeExactStaticMethod(Class<?> cls, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/*  364 */     args = ArrayUtils.nullToEmpty(args);
/*  365 */     parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
/*  366 */     Method method = getAccessibleMethod(cls, methodName, parameterTypes);
/*  367 */     if (method == null) {
/*  368 */       throw new NoSuchMethodException("No such accessible method: " + methodName + "() on class: " + cls
/*  369 */           .getName());
/*      */     }
/*  371 */     return method.invoke((Object)null, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object invokeStaticMethod(Class<?> cls, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/*  401 */     args = ArrayUtils.nullToEmpty(args);
/*  402 */     return invokeStaticMethod(cls, methodName, args, ClassUtils.toClass(args));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object invokeStaticMethod(Class<?> cls, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/*  430 */     args = ArrayUtils.nullToEmpty(args);
/*  431 */     parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
/*  432 */     Method method = getMatchingAccessibleMethod(cls, methodName, parameterTypes);
/*      */     
/*  434 */     if (method == null) {
/*  435 */       throw new NoSuchMethodException("No such accessible method: " + methodName + "() on class: " + cls
/*  436 */           .getName());
/*      */     }
/*  438 */     args = toVarArgs(method, args);
/*  439 */     return method.invoke((Object)null, args);
/*      */   }
/*      */   
/*      */   private static Object[] toVarArgs(Method method, Object[] args) {
/*  443 */     if (method.isVarArgs()) {
/*  444 */       Class<?>[] methodParameterTypes = method.getParameterTypes();
/*  445 */       args = getVarArgs(args, methodParameterTypes);
/*      */     } 
/*  447 */     return args;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Object[] getVarArgs(Object[] args, Class<?>[] methodParameterTypes) {
/*  460 */     if (args.length == methodParameterTypes.length && (args[args.length - 1] == null || args[args.length - 1]
/*  461 */       .getClass().equals(methodParameterTypes[methodParameterTypes.length - 1])))
/*      */     {
/*  463 */       return args;
/*      */     }
/*      */ 
/*      */     
/*  467 */     Object[] newArgs = new Object[methodParameterTypes.length];
/*      */ 
/*      */     
/*  470 */     System.arraycopy(args, 0, newArgs, 0, methodParameterTypes.length - 1);
/*      */ 
/*      */     
/*  473 */     Class<?> varArgComponentType = methodParameterTypes[methodParameterTypes.length - 1].getComponentType();
/*  474 */     int varArgLength = args.length - methodParameterTypes.length + 1;
/*      */     
/*  476 */     Object varArgsArray = Array.newInstance(ClassUtils.primitiveToWrapper(varArgComponentType), varArgLength);
/*      */     
/*  478 */     System.arraycopy(args, methodParameterTypes.length - 1, varArgsArray, 0, varArgLength);
/*      */     
/*  480 */     if (varArgComponentType.isPrimitive())
/*      */     {
/*  482 */       varArgsArray = ArrayUtils.toPrimitive(varArgsArray);
/*      */     }
/*      */ 
/*      */     
/*  486 */     newArgs[methodParameterTypes.length - 1] = varArgsArray;
/*      */ 
/*      */     
/*  489 */     return newArgs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object invokeExactStaticMethod(Class<?> cls, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/*  513 */     args = ArrayUtils.nullToEmpty(args);
/*  514 */     return invokeExactStaticMethod(cls, methodName, args, ClassUtils.toClass(args));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Method getAccessibleMethod(Class<?> cls, String methodName, Class<?>... parameterTypes) {
/*      */     try {
/*  532 */       return getAccessibleMethod(cls.getMethod(methodName, parameterTypes));
/*  533 */     } catch (NoSuchMethodException e) {
/*  534 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Method getAccessibleMethod(Method method) {
/*  547 */     if (!MemberUtils.isAccessible(method)) {
/*  548 */       return null;
/*      */     }
/*      */     
/*  551 */     Class<?> cls = method.getDeclaringClass();
/*  552 */     if (ClassUtils.isPublic(cls)) {
/*  553 */       return method;
/*      */     }
/*  555 */     String methodName = method.getName();
/*  556 */     Class<?>[] parameterTypes = method.getParameterTypes();
/*      */ 
/*      */     
/*  559 */     method = getAccessibleMethodFromInterfaceNest(cls, methodName, parameterTypes);
/*      */ 
/*      */ 
/*      */     
/*  563 */     if (method == null) {
/*  564 */       method = getAccessibleMethodFromSuperclass(cls, methodName, parameterTypes);
/*      */     }
/*      */     
/*  567 */     return method;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Method getAccessibleMethodFromSuperclass(Class<?> cls, String methodName, Class<?>... parameterTypes) {
/*  582 */     Class<?> parentClass = cls.getSuperclass();
/*  583 */     while (parentClass != null) {
/*  584 */       if (ClassUtils.isPublic(parentClass)) {
/*      */         try {
/*  586 */           return parentClass.getMethod(methodName, parameterTypes);
/*  587 */         } catch (NoSuchMethodException e) {
/*  588 */           return null;
/*      */         } 
/*      */       }
/*  591 */       parentClass = parentClass.getSuperclass();
/*      */     } 
/*  593 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Method getAccessibleMethodFromInterfaceNest(Class<?> cls, String methodName, Class<?>... parameterTypes) {
/*  614 */     for (; cls != null; cls = cls.getSuperclass()) {
/*      */ 
/*      */       
/*  617 */       Class<?>[] interfaces = cls.getInterfaces();
/*  618 */       for (Class<?> anInterface : interfaces) {
/*      */         
/*  620 */         if (ClassUtils.isPublic(anInterface))
/*      */           
/*      */           try {
/*      */ 
/*      */             
/*  625 */             return anInterface.getDeclaredMethod(methodName, parameterTypes);
/*      */           }
/*  627 */           catch (NoSuchMethodException noSuchMethodException) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  634 */             Method method = getAccessibleMethodFromInterfaceNest(anInterface, methodName, parameterTypes);
/*      */             
/*  636 */             if (method != null)
/*  637 */               return method; 
/*      */           }  
/*      */       } 
/*      */     } 
/*  641 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Method getMatchingAccessibleMethod(Class<?> cls, String methodName, Class<?>... parameterTypes) {
/*      */     try {
/*  669 */       return MemberUtils.<Method>setAccessibleWorkaround(cls.getMethod(methodName, parameterTypes));
/*  670 */     } catch (NoSuchMethodException noSuchMethodException) {
/*      */ 
/*      */ 
/*      */       
/*  674 */       Method[] methods = cls.getMethods();
/*      */       
/*  676 */       List<Method> matchingMethods = (List<Method>)Stream.<Method>of(methods).filter(method -> (method.getName().equals(methodName) && MemberUtils.isMatchingMethod(method, parameterTypes))).collect(Collectors.toList());
/*      */ 
/*      */       
/*  679 */       matchingMethods.sort(METHOD_BY_SIGNATURE);
/*      */       
/*  681 */       Method bestMatch = null;
/*  682 */       for (Method method : matchingMethods) {
/*      */         
/*  684 */         Method accessibleMethod = getAccessibleMethod(method);
/*  685 */         if (accessibleMethod != null && (bestMatch == null || MemberUtils.compareMethodFit(accessibleMethod, bestMatch, parameterTypes) < 0)) {
/*  686 */           bestMatch = accessibleMethod;
/*      */         }
/*      */       } 
/*  689 */       if (bestMatch != null) {
/*  690 */         MemberUtils.setAccessibleWorkaround(bestMatch);
/*      */       }
/*      */       
/*  693 */       if (bestMatch != null && bestMatch.isVarArgs() && (bestMatch.getParameterTypes()).length > 0 && parameterTypes.length > 0) {
/*  694 */         Class<?>[] methodParameterTypes = bestMatch.getParameterTypes();
/*  695 */         Class<?> methodParameterComponentType = methodParameterTypes[methodParameterTypes.length - 1].getComponentType();
/*  696 */         String methodParameterComponentTypeName = ClassUtils.primitiveToWrapper(methodParameterComponentType).getName();
/*      */         
/*  698 */         Class<?> lastParameterType = parameterTypes[parameterTypes.length - 1];
/*  699 */         String parameterTypeName = (lastParameterType == null) ? null : lastParameterType.getName();
/*  700 */         String parameterTypeSuperClassName = (lastParameterType == null) ? null : lastParameterType.getSuperclass().getName();
/*      */         
/*  702 */         if (parameterTypeName != null && parameterTypeSuperClassName != null && !methodParameterComponentTypeName.equals(parameterTypeName) && 
/*  703 */           !methodParameterComponentTypeName.equals(parameterTypeSuperClassName)) {
/*  704 */           return null;
/*      */         }
/*      */       } 
/*      */       
/*  708 */       return bestMatch;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Method getMatchingMethod(Class<?> cls, String methodName, Class<?>... parameterTypes) {
/*  725 */     Objects.requireNonNull(cls, "cls");
/*  726 */     Validate.notEmpty(methodName, "methodName", new Object[0]);
/*      */ 
/*      */ 
/*      */     
/*  730 */     List<Method> methods = (List<Method>)Stream.<Method>of(cls.getDeclaredMethods()).filter(method -> method.getName().equals(methodName)).collect(Collectors.toList());
/*      */     
/*  732 */     ClassUtils.getAllSuperclasses(cls).stream()
/*  733 */       .map(Class::getDeclaredMethods)
/*  734 */       .flatMap(Stream::of)
/*  735 */       .filter(method -> method.getName().equals(methodName))
/*  736 */       .forEach(methods::add);
/*      */     
/*  738 */     for (Method method : methods) {
/*  739 */       if (Arrays.deepEquals((Object[])method.getParameterTypes(), (Object[])parameterTypes)) {
/*  740 */         return method;
/*      */       }
/*      */     } 
/*      */     
/*  744 */     TreeMap<Integer, List<Method>> candidates = new TreeMap<>();
/*      */     
/*  746 */     methods.stream()
/*  747 */       .filter(method -> ClassUtils.isAssignable(parameterTypes, method.getParameterTypes(), true))
/*  748 */       .forEach(method -> {
/*      */           int distance = distance(parameterTypes, method.getParameterTypes());
/*      */           
/*      */           List<Method> candidatesAtDistance = candidates.computeIfAbsent(Integer.valueOf(distance), ());
/*      */           candidatesAtDistance.add(method);
/*      */         });
/*  754 */     if (candidates.isEmpty()) {
/*  755 */       return null;
/*      */     }
/*      */     
/*  758 */     List<Method> bestCandidates = candidates.values().iterator().next();
/*  759 */     if (bestCandidates.size() == 1 || !Objects.equals(((Method)bestCandidates.get(0)).getDeclaringClass(), ((Method)bestCandidates
/*  760 */         .get(1)).getDeclaringClass())) {
/*  761 */       return bestCandidates.get(0);
/*      */     }
/*      */     
/*  764 */     throw new IllegalStateException(
/*  765 */         String.format("Found multiple candidates for method %s on class %s : %s", new Object[] {
/*  766 */             methodName + (String)Stream.<Class<?>>of(parameterTypes).map(String::valueOf).collect(Collectors.joining(",", "(", ")")), cls
/*  767 */             .getName(), bestCandidates
/*  768 */             .stream().map(Method::toString).collect(Collectors.joining(",", "[", "]"))
/*      */           }));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int distance(Class<?>[] fromClassArray, Class<?>[] toClassArray) {
/*  780 */     int answer = 0;
/*      */     
/*  782 */     if (!ClassUtils.isAssignable(fromClassArray, toClassArray, true)) {
/*  783 */       return -1;
/*      */     }
/*  785 */     for (int offset = 0; offset < fromClassArray.length; offset++) {
/*      */       
/*  787 */       Class<?> aClass = fromClassArray[offset];
/*  788 */       Class<?> toClass = toClassArray[offset];
/*  789 */       if (aClass != null && !aClass.equals(toClass))
/*      */       {
/*      */         
/*  792 */         if (ClassUtils.isAssignable(aClass, toClass, true) && 
/*  793 */           !ClassUtils.isAssignable(aClass, toClass, false)) {
/*  794 */           answer++;
/*      */         } else {
/*  796 */           answer += 2;
/*      */         } 
/*      */       }
/*      */     } 
/*  800 */     return answer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Set<Method> getOverrideHierarchy(Method method, ClassUtils.Interfaces interfacesBehavior) {
/*  812 */     Objects.requireNonNull(method, "method");
/*  813 */     Set<Method> result = new LinkedHashSet<>();
/*  814 */     result.add(method);
/*      */     
/*  816 */     Class<?>[] parameterTypes = method.getParameterTypes();
/*      */     
/*  818 */     Class<?> declaringClass = method.getDeclaringClass();
/*      */     
/*  820 */     Iterator<Class<?>> hierarchy = ClassUtils.hierarchy(declaringClass, interfacesBehavior).iterator();
/*      */     
/*  822 */     hierarchy.next();
/*  823 */     label21: while (hierarchy.hasNext()) {
/*  824 */       Class<?> c = hierarchy.next();
/*  825 */       Method m = getMatchingAccessibleMethod(c, method.getName(), parameterTypes);
/*  826 */       if (m == null) {
/*      */         continue;
/*      */       }
/*  829 */       if (Arrays.equals((Object[])m.getParameterTypes(), (Object[])parameterTypes)) {
/*      */         
/*  831 */         result.add(m);
/*      */         
/*      */         continue;
/*      */       } 
/*  835 */       Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(declaringClass, m.getDeclaringClass());
/*  836 */       for (int i = 0; i < parameterTypes.length; i++) {
/*  837 */         Type childType = TypeUtils.unrollVariables(typeArguments, method.getGenericParameterTypes()[i]);
/*  838 */         Type parentType = TypeUtils.unrollVariables(typeArguments, m.getGenericParameterTypes()[i]);
/*  839 */         if (!TypeUtils.equals(childType, parentType)) {
/*      */           continue label21;
/*      */         }
/*      */       } 
/*  843 */       result.add(m);
/*      */     } 
/*  845 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Method[] getMethodsWithAnnotation(Class<?> cls, Class<? extends Annotation> annotationCls) {
/*  859 */     return getMethodsWithAnnotation(cls, annotationCls, false, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<Method> getMethodsListWithAnnotation(Class<?> cls, Class<? extends Annotation> annotationCls) {
/*  874 */     return getMethodsListWithAnnotation(cls, annotationCls, false, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Method[] getMethodsWithAnnotation(Class<?> cls, Class<? extends Annotation> annotationCls, boolean searchSupers, boolean ignoreAccess) {
/*  893 */     return getMethodsListWithAnnotation(cls, annotationCls, searchSupers, ignoreAccess).<Method>toArray(ArrayUtils.EMPTY_METHOD_ARRAY);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<Method> getMethodsListWithAnnotation(Class<?> cls, Class<? extends Annotation> annotationCls, boolean searchSupers, boolean ignoreAccess) {
/*  914 */     Objects.requireNonNull(cls, "cls");
/*  915 */     Objects.requireNonNull(annotationCls, "annotationCls");
/*  916 */     List<Class<?>> classes = searchSupers ? getAllSuperclassesAndInterfaces(cls) : new ArrayList<>();
/*  917 */     classes.add(0, cls);
/*  918 */     List<Method> annotatedMethods = new ArrayList<>();
/*  919 */     classes.forEach(acls -> {
/*      */           Method[] methods = ignoreAccess ? acls.getDeclaredMethods() : acls.getMethods();
/*      */           Stream.<Method>of(methods).filter(()).forEachOrdered(annotatedMethods::add);
/*      */         });
/*  923 */     return annotatedMethods;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationCls, boolean searchSupers, boolean ignoreAccess) {
/*  951 */     Objects.requireNonNull(method, "method");
/*  952 */     Objects.requireNonNull(annotationCls, "annotationCls");
/*  953 */     if (!ignoreAccess && !MemberUtils.isAccessible(method)) {
/*  954 */       return null;
/*      */     }
/*      */     
/*  957 */     A annotation = method.getAnnotation(annotationCls);
/*      */     
/*  959 */     if (annotation == null && searchSupers) {
/*  960 */       Class<?> mcls = method.getDeclaringClass();
/*  961 */       List<Class<?>> classes = getAllSuperclassesAndInterfaces(mcls);
/*  962 */       for (Class<?> acls : classes) {
/*      */         
/*  964 */         Method equivalentMethod = ignoreAccess ? getMatchingMethod(acls, method.getName(), method.getParameterTypes()) : getMatchingAccessibleMethod(acls, method.getName(), method.getParameterTypes());
/*  965 */         if (equivalentMethod != null) {
/*  966 */           annotation = equivalentMethod.getAnnotation(annotationCls);
/*  967 */           if (annotation != null) {
/*      */             break;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  974 */     return annotation;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static List<Class<?>> getAllSuperclassesAndInterfaces(Class<?> cls) {
/*  988 */     if (cls == null) {
/*  989 */       return null;
/*      */     }
/*      */     
/*  992 */     List<Class<?>> allSuperClassesAndInterfaces = new ArrayList<>();
/*  993 */     List<Class<?>> allSuperclasses = ClassUtils.getAllSuperclasses(cls);
/*  994 */     int superClassIndex = 0;
/*  995 */     List<Class<?>> allInterfaces = ClassUtils.getAllInterfaces(cls);
/*  996 */     int interfaceIndex = 0;
/*  997 */     while (interfaceIndex < allInterfaces.size() || superClassIndex < allSuperclasses
/*  998 */       .size()) {
/*      */       Class<?> acls;
/* 1000 */       if (interfaceIndex >= allInterfaces.size()) {
/* 1001 */         acls = allSuperclasses.get(superClassIndex++);
/* 1002 */       } else if (superClassIndex >= allSuperclasses.size() || superClassIndex >= interfaceIndex) {
/* 1003 */         acls = allInterfaces.get(interfaceIndex++);
/*      */       } else {
/* 1005 */         acls = allSuperclasses.get(superClassIndex++);
/*      */       } 
/* 1007 */       allSuperClassesAndInterfaces.add(acls);
/*      */     } 
/* 1009 */     return allSuperClassesAndInterfaces;
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\reflect\MethodUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */