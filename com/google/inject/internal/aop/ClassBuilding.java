/*     */ package com.google.inject.internal.aop;
/*     */ 
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.internal.BytecodeGen;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Executable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Deque;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.TreeMap;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
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
/*     */ public final class ClassBuilding
/*     */ {
/*  51 */   private static final Method[] OVERRIDABLE_OBJECT_METHODS = getOverridableObjectMethods();
/*     */ 
/*     */   
/*     */   public static String signature(Constructor<?> constructor) {
/*  55 */     return signature("<init>", constructor.getParameterTypes());
/*     */   }
/*     */ 
/*     */   
/*     */   public static String signature(Method method) {
/*  60 */     return signature(method.getName(), method.getParameterTypes());
/*     */   }
/*     */ 
/*     */   
/*     */   private static String signature(String name, Class<?>[] parameterTypes) {
/*  65 */     StringBuilder signature = new StringBuilder(name);
/*  66 */     for (Class<?> type : parameterTypes) {
/*  67 */       signature.append(';').append(type.getName());
/*     */     }
/*  69 */     return signature.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean canEnhance(Executable member) {
/*  74 */     return canAccess(member, ClassDefining.hasPackageAccess());
/*     */   }
/*     */ 
/*     */   
/*     */   public static BytecodeGen.EnhancerBuilder buildEnhancerBuilder(Class<?> hostClass) {
/*  79 */     Map<String, Object> methodPartitions = new HashMap<>();
/*     */     
/*  81 */     visitMethodHierarchy(hostClass, method -> {
/*     */           if ((method.getModifiers() & 0x8) == 0) {
/*     */             partitionMethod(method, methodPartitions);
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  90 */     Map<String, Method> enhanceableMethods = new TreeMap<>();
/*  91 */     Map<Method, Method> bridgeDelegates = new HashMap<>();
/*     */     
/*  93 */     TypeLiteral<?> hostType = TypeLiteral.get(hostClass);
/*  94 */     for (Object partition : methodPartitions.values()) {
/*  95 */       if (partition instanceof Method) {
/*     */         
/*  97 */         Method method = (Method)partition;
/*  98 */         if ((method.getModifiers() & 0x10) == 0)
/*  99 */           enhanceableMethods.put(signature(method), method); 
/*     */         continue;
/*     */       } 
/* 102 */       ((MethodPartition)partition)
/* 103 */         .collectEnhanceableMethods(hostType, method -> enhanceableMethods.put(signature(method), method), bridgeDelegates);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     return new EnhancerBuilderImpl(hostClass, enhanceableMethods.values(), bridgeDelegates);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void partitionMethod(Method method, Map<String, Object> partitions) {
/* 119 */     String str1 = method.getName(); int i = method.getParameterCount(); String partitionKey = (new StringBuilder(12 + String.valueOf(str1).length())).append(str1).append('/').append(i).toString();
/* 120 */     partitions.merge(partitionKey, method, ClassBuilding::mergeMethods);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Object mergeMethods(Object existing, Object added) {
/* 125 */     Method newMethod = (Method)added;
/* 126 */     if (existing instanceof Method) {
/* 127 */       return new MethodPartition((Method)existing, newMethod);
/*     */     }
/* 129 */     return ((MethodPartition)existing).addCandidate(newMethod);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void visitMethodHierarchy(Class<?> hostClass, Consumer<Method> visitor) {
/* 140 */     Deque<Class<?>[]> interfaceStack = (Deque)new ArrayDeque<>();
/*     */ 
/*     */     
/* 143 */     String hostPackage = ClassDefining.hasPackageAccess() ? packageName(hostClass.getName()) : null;
/*     */     
/* 145 */     Class<?> clazz = hostClass;
/* 146 */     for (; clazz != Object.class && clazz != null; 
/* 147 */       clazz = clazz.getSuperclass()) {
/*     */ 
/*     */       
/* 150 */       boolean samePackage = (hostPackage != null && hostPackage.equals(packageName(clazz.getName())));
/*     */       
/* 152 */       visitMembers(clazz.getDeclaredMethods(), samePackage, visitor);
/* 153 */       pushInterfaces(interfaceStack, clazz.getInterfaces());
/*     */     } 
/*     */     
/* 156 */     for (Method method : OVERRIDABLE_OBJECT_METHODS) {
/* 157 */       visitor.accept(method);
/*     */     }
/*     */ 
/*     */     
/* 161 */     List<Class<?>> interfaces = new ArrayList<>();
/* 162 */     while (!interfaceStack.isEmpty()) {
/* 163 */       for (Class<?> intf : (Class[])interfaceStack.pop()) {
/* 164 */         if (mergeInterface(interfaces, intf)) {
/* 165 */           pushInterfaces(interfaceStack, intf.getInterfaces());
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 171 */     for (Class<?> intf : interfaces) {
/* 172 */       visitMembers(intf.getDeclaredMethods(), false, visitor);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void pushInterfaces(Deque<Class<?>[]> interfaceStack, Class<?>[] interfaces) {
/* 178 */     if (interfaces.length > 0) {
/* 179 */       interfaceStack.push(interfaces);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean mergeInterface(List<Class<?>> interfaces, Class<?> candidate) {
/* 186 */     for (int i = 0, len = interfaces.size(); i < len; i++) {
/* 187 */       Class<?> existingInterface = interfaces.get(i);
/* 188 */       if (existingInterface == candidate)
/*     */       {
/* 190 */         return false; } 
/* 191 */       if (existingInterface.isAssignableFrom(candidate)) {
/*     */         
/* 193 */         interfaces.add(i, candidate);
/* 194 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 198 */     return interfaces.add(candidate);
/*     */   }
/*     */ 
/*     */   
/*     */   private static String packageName(String className) {
/* 203 */     return className.substring(0, className.lastIndexOf('.') + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Method[] getOverridableObjectMethods() {
/* 208 */     List<Method> objectMethods = new ArrayList<>();
/*     */     
/* 210 */     visitMembers(Object.class
/* 211 */         .getDeclaredMethods(), false, method -> {
/*     */           if ((method.getModifiers() & 0x18) == 0 && !"finalize".equals(method.getName())) {
/*     */             objectMethods.add(method);
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 221 */     return objectMethods.<Method>toArray(new Method[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean canFastInvoke(Executable member) {
/* 226 */     int modifiers = member.getModifiers() & 0x3;
/* 227 */     if (ClassDefining.hasPackageAccess())
/*     */     {
/* 229 */       return (modifiers != 2);
/*     */     }
/*     */     
/* 232 */     boolean visible = (modifiers == 1 && isPublic(member.getDeclaringClass()));
/* 233 */     if (visible) {
/* 234 */       for (Class<?> type : member.getParameterTypes()) {
/* 235 */         if (!isPublic(type)) {
/* 236 */           return false;
/*     */         }
/*     */       } 
/*     */     }
/* 240 */     return visible;
/*     */   }
/*     */   
/*     */   private static boolean isPublic(Class<?> clazz) {
/* 244 */     return ((clazz.getModifiers() & 0x1) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Function<String, BiFunction<Object, Object[], Object>> buildFastClass(Class<?> hostClass) {
/* 250 */     NavigableMap<String, Executable> glueMap = new TreeMap<>();
/*     */     
/* 252 */     visitFastConstructors(hostClass, ctor -> glueMap.put(signature(ctor), ctor));
/* 253 */     visitFastMethods(hostClass, method -> glueMap.put(signature(method), method));
/*     */     
/* 255 */     return (new FastClass(hostClass)).glue(glueMap);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void visitFastConstructors(Class<?> hostClass, Consumer<Constructor<?>> visitor) {
/* 260 */     if (ClassDefining.hasPackageAccess()) {
/*     */       
/* 262 */       visitMembers(hostClass.getDeclaredConstructors(), true, visitor);
/*     */     } else {
/*     */       
/* 265 */       for (Constructor<?> constructor : hostClass.getConstructors()) {
/* 266 */         visitor.accept(constructor);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void visitFastMethods(Class<?> hostClass, Consumer<Method> visitor) {
/* 273 */     if (ClassDefining.hasPackageAccess()) {
/*     */       
/* 275 */       visitMembers(hostClass.getDeclaredMethods(), true, visitor);
/*     */     } else {
/*     */       
/* 278 */       for (Method method : hostClass.getMethods()) {
/*     */         
/* 280 */         if (hostClass == method.getDeclaringClass()) {
/* 281 */           visitor.accept(method);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static <T extends Executable> void visitMembers(T[] members, boolean samePackage, Consumer<T> visitor) {
/* 290 */     for (T member : members) {
/* 291 */       if (canAccess((Executable)member, samePackage)) {
/* 292 */         visitor.accept(member);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean canAccess(Executable member, boolean samePackage) {
/* 299 */     int modifiers = member.getModifiers();
/*     */ 
/*     */     
/* 302 */     return ((modifiers & 0x5) != 0 || (samePackage && (modifiers & 0x2) == 0));
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\aop\ClassBuilding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */