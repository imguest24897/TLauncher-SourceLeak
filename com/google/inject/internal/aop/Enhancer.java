/*     */ package com.google.inject.internal.aop;
/*     */ 
/*     */ import com.google.inject.internal.asm.;
/*     */ import java.lang.invoke.MethodHandle;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Executable;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ final class Enhancer
/*     */   extends AbstractGlueGenerator
/*     */ {
/*     */   private static final String HANDLERS_NAME = "GUICE$HANDLERS";
/*     */   private static final String HANDLERS_DESCRIPTOR = "[Ljava/lang/reflect/InvocationHandler;";
/* 129 */   private static final String HANDLER_TYPE = .Type.getInternalName(InvocationHandler.class);
/*     */   
/* 131 */   private static final String HANDLER_ARRAY_TYPE = .Type.getInternalName(InvocationHandler[].class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String INVOKERS_NAME = "GUICE$INVOKERS";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String INVOKERS_DESCRIPTOR = "Ljava/lang/invoke/MethodHandle;";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String CALLBACK_DESCRIPTOR = "(Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String METAFACTORY_DESCRIPTOR = "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;";
/*     */ 
/*     */ 
/*     */   
/* 154 */   private static final .Type INDEX_TO_INVOKER_METHOD_TYPE = .Type.getMethodType("(I)Ljava/util/function/BiFunction;");
/*     */ 
/*     */   
/* 157 */   private static final .Type RAW_INVOKER_METHOD_TYPE = .Type.getMethodType("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
/*     */ 
/*     */   
/* 160 */   private static final .Type INVOKER_METHOD_TYPE = .Type.getMethodType("(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;");
/*     */   
/*     */   private final Map<Method, Method> bridgeDelegates;
/*     */   
/*     */   private final String checkcastToProxy;
/*     */   
/*     */   Enhancer(Class<?> hostClass, Map<Method, Method> bridgeDelegates) {
/* 167 */     super(hostClass, "$$EnhancerByGuice$$");
/* 168 */     this.bridgeDelegates = bridgeDelegates;
/*     */ 
/*     */     
/* 171 */     this.checkcastToProxy = ClassDefining.isAnonymousHost(hostClass) ? this.hostName : this.proxyName;
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] generateGlue(Collection<Executable> members) {
/* 176 */     .ClassWriter cw = new .ClassWriter(1);
/*     */ 
/*     */     
/* 179 */     cw.visit(52, 33, this.proxyName, null, this.hostName, null);
/* 180 */     cw.visitSource("<generated>", null);
/*     */ 
/*     */     
/* 183 */     cw.visitField(25, "GUICE$INVOKERS", "Ljava/lang/invoke/MethodHandle;", null, null)
/* 184 */       .visitEnd();
/*     */     
/* 186 */     setupInvokerTable(cw);
/*     */     
/* 188 */     generateTrampoline(cw, members);
/*     */ 
/*     */     
/* 191 */     cw.visitField(18, "GUICE$HANDLERS", "[Ljava/lang/reflect/InvocationHandler;", null, null).visitEnd();
/*     */     
/* 193 */     Set<Method> remainingBridgeMethods = new HashSet<>(this.bridgeDelegates.keySet());
/*     */     
/* 195 */     int methodIndex = 0;
/* 196 */     for (Executable member : members) {
/* 197 */       if (member instanceof Constructor) {
/* 198 */         enhanceConstructor(cw, (Constructor)member); continue;
/*     */       } 
/* 200 */       enhanceMethod(cw, (Method)member, methodIndex++);
/* 201 */       remainingBridgeMethods.remove(member);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 206 */     for (Method method : remainingBridgeMethods) {
/* 207 */       Method target = this.bridgeDelegates.get(method);
/* 208 */       if (target != null) {
/* 209 */         generateVirtualBridge(cw, method, target);
/*     */       }
/*     */     } 
/*     */     
/* 213 */     cw.visitEnd();
/* 214 */     return cw.toByteArray();
/*     */   }
/*     */ 
/*     */   
/*     */   private void setupInvokerTable(.ClassWriter cw) {
/* 219 */     .MethodVisitor mv = cw.visitMethod(10, "<clinit>", "()V", null, null);
/* 220 */     mv.visitCode();
/*     */     
/* 222 */     .Handle trampolineHandle = new .Handle(6, this.proxyName, "GUICE$TRAMPOLINE", "(ILjava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", false);
/*     */ 
/*     */     
/* 225 */     if (ClassDefining.isAnonymousHost(this.hostClass)) {
/*     */       
/* 227 */       mv.visitLdcInsn(trampolineHandle);
/*     */     }
/*     */     else {
/*     */       
/* 231 */       mv.visitMethodInsn(184, "java/lang/invoke/MethodHandles", "lookup", "()Ljava/lang/invoke/MethodHandles$Lookup;", false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 238 */       mv.visitLdcInsn("apply");
/* 239 */       mv.visitLdcInsn(INDEX_TO_INVOKER_METHOD_TYPE);
/* 240 */       mv.visitLdcInsn(RAW_INVOKER_METHOD_TYPE);
/* 241 */       mv.visitLdcInsn(trampolineHandle);
/* 242 */       mv.visitLdcInsn(INVOKER_METHOD_TYPE);
/*     */       
/* 244 */       mv.visitMethodInsn(184, "java/lang/invoke/LambdaMetafactory", "metafactory", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;", false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 251 */       mv.visitMethodInsn(182, "java/lang/invoke/CallSite", "getTarget", "()Ljava/lang/invoke/MethodHandle;", false);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 259 */     mv.visitFieldInsn(179, this.proxyName, "GUICE$INVOKERS", "Ljava/lang/invoke/MethodHandle;");
/*     */     
/* 261 */     mv.visitInsn(177);
/* 262 */     mv.visitMaxs(0, 0);
/* 263 */     mv.visitEnd();
/*     */   }
/*     */ 
/*     */   
/*     */   private void enhanceConstructor(.ClassWriter cw, Constructor<?> constructor) {
/* 268 */     String descriptor = .Type.getConstructorDescriptor(constructor);
/* 269 */     String.valueOf(descriptor.substring(1)); String enhancedDescriptor = (String.valueOf(descriptor.substring(1)).length() != 0) ? String.valueOf("([Ljava/lang/reflect/InvocationHandler;").concat(String.valueOf(descriptor.substring(1))) : new String(String.valueOf("([Ljava/lang/reflect/InvocationHandler;"));
/*     */ 
/*     */     
/* 272 */     .MethodVisitor mv = cw.visitMethod(1, "<init>", enhancedDescriptor, null, exceptionNames(constructor));
/*     */     
/* 274 */     mv.visitCode();
/*     */     
/* 276 */     mv.visitVarInsn(25, 0);
/* 277 */     mv.visitInsn(89);
/* 278 */     mv.visitVarInsn(25, 1);
/*     */     
/* 280 */     mv.visitFieldInsn(181, this.proxyName, "GUICE$HANDLERS", "[Ljava/lang/reflect/InvocationHandler;");
/*     */     
/* 282 */     int slot = 2;
/* 283 */     for (Class<?> parameterType : constructor.getParameterTypes()) {
/* 284 */       slot += BytecodeTasks.loadArgument(mv, parameterType, slot);
/*     */     }
/*     */     
/* 287 */     mv.visitMethodInsn(183, this.hostName, "<init>", descriptor, false);
/*     */     
/* 289 */     mv.visitInsn(177);
/* 290 */     mv.visitMaxs(0, 0);
/* 291 */     mv.visitEnd();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void enhanceMethod(.ClassWriter cw, Method method, int methodIndex) {
/* 297 */     .MethodVisitor mv = cw.visitMethod(0x10 | method
/* 298 */         .getModifiers() & 0xFFFFFADF, method
/* 299 */         .getName(), 
/* 300 */         .Type.getMethodDescriptor(method), null, 
/*     */         
/* 302 */         exceptionNames(method));
/*     */     
/* 304 */     mv.visitVarInsn(25, 0);
/* 305 */     mv.visitInsn(89);
/* 306 */     mv.visitFieldInsn(180, this.proxyName, "GUICE$HANDLERS", "[Ljava/lang/reflect/InvocationHandler;");
/* 307 */     BytecodeTasks.pushInteger(mv, methodIndex);
/* 308 */     mv.visitInsn(50);
/* 309 */     mv.visitInsn(95);
/*     */     
/* 311 */     mv.visitInsn(1);
/* 312 */     BytecodeTasks.packArguments(mv, method.getParameterTypes());
/*     */     
/* 314 */     mv.visitMethodInsn(185, HANDLER_TYPE, "invoke", "(Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;", true);
/*     */     
/* 316 */     Class<?> returnType = method.getReturnType();
/* 317 */     if (returnType == void.class) {
/* 318 */       mv.visitInsn(177);
/* 319 */     } else if (returnType.isPrimitive()) {
/* 320 */       .Type primitiveType = .Type.getType(returnType);
/* 321 */       BytecodeTasks.unbox(mv, primitiveType);
/* 322 */       mv.visitInsn(primitiveType.getOpcode(172));
/*     */     } else {
/* 324 */       mv.visitTypeInsn(192, .Type.getInternalName(returnType));
/* 325 */       mv.visitInsn(176);
/*     */     } 
/*     */     
/* 328 */     mv.visitMaxs(0, 0);
/* 329 */     mv.visitEnd();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void generateConstructorInvoker(.MethodVisitor mv, Constructor<?> constructor) {
/* 334 */     String descriptor = .Type.getConstructorDescriptor(constructor);
/* 335 */     String.valueOf(descriptor.substring(1)); String enhancedDescriptor = (String.valueOf(descriptor.substring(1)).length() != 0) ? String.valueOf("([Ljava/lang/reflect/InvocationHandler;").concat(String.valueOf(descriptor.substring(1))) : new String(String.valueOf("([Ljava/lang/reflect/InvocationHandler;"));
/*     */     
/* 337 */     mv.visitTypeInsn(187, this.proxyName);
/* 338 */     mv.visitInsn(89);
/*     */     
/* 340 */     mv.visitVarInsn(25, 1);
/* 341 */     mv.visitTypeInsn(192, HANDLER_ARRAY_TYPE);
/* 342 */     BytecodeTasks.unpackArguments(mv, constructor.getParameterTypes());
/*     */     
/* 344 */     mv.visitMethodInsn(183, this.proxyName, "<init>", enhancedDescriptor, false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void generateMethodInvoker(.MethodVisitor mv, Method method) {
/* 349 */     Method target = this.bridgeDelegates.getOrDefault(method, method);
/*     */ 
/*     */ 
/*     */     
/* 353 */     int invokeOpcode = (target != method) ? 182 : 183;
/*     */     
/* 355 */     mv.visitVarInsn(25, 1);
/* 356 */     mv.visitTypeInsn(192, this.checkcastToProxy);
/* 357 */     BytecodeTasks.unpackArguments(mv, target.getParameterTypes());
/*     */     
/* 359 */     mv.visitMethodInsn(invokeOpcode, this.hostName, target
/* 360 */         .getName(), .Type.getMethodDescriptor(target), false);
/*     */     
/* 362 */     Class<?> returnType = target.getReturnType();
/* 363 */     if (returnType == void.class) {
/* 364 */       mv.visitInsn(1);
/* 365 */     } else if (returnType.isPrimitive()) {
/* 366 */       BytecodeTasks.box(mv, .Type.getType(returnType));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateVirtualBridge(.ClassWriter cw, Method bridge, Method target) {
/* 373 */     .MethodVisitor mv = cw.visitMethod(0x10 | bridge
/* 374 */         .getModifiers() & 0xFFFFFADF, bridge
/* 375 */         .getName(), 
/* 376 */         .Type.getMethodDescriptor(bridge), null, 
/*     */         
/* 378 */         exceptionNames(bridge));
/*     */     
/* 380 */     mv.visitVarInsn(25, 0);
/* 381 */     mv.visitTypeInsn(192, this.checkcastToProxy);
/*     */     
/* 383 */     Class<?>[] bridgeParameterTypes = bridge.getParameterTypes();
/* 384 */     Class<?>[] targetParameterTypes = target.getParameterTypes();
/*     */     
/* 386 */     int slot = 1;
/* 387 */     for (int i = 0, len = targetParameterTypes.length; i < len; i++) {
/* 388 */       Class<?> parameterType = targetParameterTypes[i];
/* 389 */       slot += BytecodeTasks.loadArgument(mv, parameterType, slot);
/* 390 */       if (parameterType != bridgeParameterTypes[i])
/*     */       {
/* 392 */         mv.visitTypeInsn(192, .Type.getInternalName(parameterType));
/*     */       }
/*     */     } 
/*     */     
/* 396 */     mv.visitMethodInsn(182, this.hostName, target
/* 397 */         .getName(), .Type.getMethodDescriptor(target), false);
/*     */     
/* 399 */     .Type returnType = .Type.getType(bridge.getReturnType());
/* 400 */     if (target.getReturnType() != bridge.getReturnType())
/*     */     {
/* 402 */       mv.visitTypeInsn(192, returnType.getInternalName());
/*     */     }
/* 404 */     mv.visitInsn(returnType.getOpcode(172));
/*     */     
/* 406 */     mv.visitMaxs(0, 0);
/* 407 */     mv.visitEnd();
/*     */   }
/*     */ 
/*     */   
/*     */   protected MethodHandle lookupInvokerTable(Class<?> glueClass) throws Throwable {
/* 412 */     return (MethodHandle)glueClass.getField("GUICE$INVOKERS").get(null);
/*     */   }
/*     */ 
/*     */   
/*     */   private static String[] exceptionNames(Executable member) {
/* 417 */     Class<?>[] exceptionClasses = member.getExceptionTypes();
/* 418 */     String[] exceptionNames = new String[exceptionClasses.length];
/* 419 */     Arrays.setAll(exceptionNames, i -> .Type.getInternalName(exceptionClasses[i]));
/* 420 */     return exceptionNames;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\aop\Enhancer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */