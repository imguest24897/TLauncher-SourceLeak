/*     */ package com.google.inject.internal.aop;
/*     */ 
/*     */ import com.google.inject.TypeLiteral;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Consumer;
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
/*     */ final class MethodPartition
/*     */ {
/*  39 */   private final List<Method> candidates = new ArrayList<>();
/*     */ 
/*     */   
/*     */   public MethodPartition(Method first, Method second) {
/*  43 */     this.candidates.add(first);
/*  44 */     this.candidates.add(second);
/*     */   }
/*     */ 
/*     */   
/*     */   public MethodPartition addCandidate(Method method) {
/*  49 */     this.candidates.add(method);
/*  50 */     return this;
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
/*     */   public void collectEnhanceableMethods(TypeLiteral<?> hostType, Consumer<Method> methodVisitor, Map<Method, Method> bridgeDelegates) {
/*  68 */     Map<String, Method> leafMethods = new HashMap<>();
/*  69 */     Map<String, Method> bridgeTargets = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     for (Method candidate : this.candidates) {
/*  75 */       String parametersKey = parametersKey(candidate.getParameterTypes());
/*  76 */       Method existingLeafMethod = leafMethods.putIfAbsent(parametersKey, candidate);
/*  77 */       if (existingLeafMethod == null) {
/*  78 */         if (candidate.isBridge())
/*     */         {
/*  80 */           bridgeTargets.put(parametersKey, null); }  continue;
/*     */       } 
/*  82 */       if (existingLeafMethod.isBridge() && !candidate.isBridge())
/*     */       {
/*  84 */         bridgeTargets.putIfAbsent(parametersKey, candidate);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  90 */     for (Map.Entry<String, Method> methodEntry : leafMethods.entrySet()) {
/*  91 */       Method method = methodEntry.getValue();
/*  92 */       if ((method.getModifiers() & 0x10) != 0) {
/*  93 */         bridgeTargets.remove(methodEntry.getKey()); continue;
/*  94 */       }  if (!method.isBridge()) {
/*  95 */         methodVisitor.accept(method);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 104 */     for (Map.Entry<String, Method> targetEntry : bridgeTargets.entrySet()) {
/* 105 */       Method originalBridge = leafMethods.get(targetEntry.getKey());
/* 106 */       Method superTarget = targetEntry.getValue();
/*     */       
/* 108 */       Method enhanceableMethod = originalBridge;
/*     */ 
/*     */ 
/*     */       
/* 112 */       for (Method candidate : this.candidates) {
/* 113 */         if (!candidate.isBridge()) {
/*     */           
/* 115 */           boolean sameMethod = (candidate == superTarget);
/* 116 */           if (sameMethod) {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 121 */             if (!superTarget.getDeclaringClass().isInterface()) {
/* 122 */               enhanceableMethod = superTarget;
/*     */             }
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 128 */           if (resolvedParametersMatch(originalBridge, hostType, candidate) || (superTarget != null && 
/*     */ 
/*     */             
/* 131 */             resolvedParametersMatch(candidate, hostType, superTarget))) {
/*     */ 
/*     */ 
/*     */             
/* 135 */             bridgeDelegates.put(originalBridge, candidate);
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 142 */       if ((enhanceableMethod.getModifiers() & 0x10) == 0) {
/* 143 */         methodVisitor.accept(enhanceableMethod);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static String parametersKey(Class<?>[] parameterTypes) {
/* 150 */     StringBuilder key = new StringBuilder();
/* 151 */     for (int i = 0, len = parameterTypes.length; i < len; i++) {
/* 152 */       if (i > 0) {
/* 153 */         key.append(',');
/*     */       }
/* 155 */       key.append(parameterTypes[i].getName());
/*     */     } 
/* 157 */     return key.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean resolvedParametersMatch(Method subMethod, TypeLiteral<?> host, Method superMethod) {
/* 163 */     Class<?>[] parameterTypes = subMethod.getParameterTypes();
/* 164 */     List<TypeLiteral<?>> resolvedTypes = host.getParameterTypes(superMethod);
/* 165 */     for (int i = 0, len = parameterTypes.length; i < len; i++) {
/* 166 */       if (parameterTypes[i] != ((TypeLiteral)resolvedTypes.get(i)).getRawType()) {
/* 167 */         return false;
/*     */       }
/*     */     } 
/* 170 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\aop\MethodPartition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */