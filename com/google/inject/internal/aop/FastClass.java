/*     */ package com.google.inject.internal.aop;
/*     */ 
/*     */ import com.google.inject.internal.asm.;
/*     */ import java.lang.invoke.MethodHandle;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Executable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class FastClass
/*     */   extends AbstractGlueGenerator
/*     */ {
/* 103 */   private static final String[] FAST_CLASS_API = new String[] { "java/util/function/BiFunction" };
/*     */ 
/*     */   
/*     */   private static final String INVOKERS_NAME = "GUICE$INVOKERS";
/*     */   
/*     */   private static final String INVOKERS_DESCRIPTOR = "Ljava/lang/invoke/MethodHandle;";
/*     */   
/* 110 */   private static final .Type INDEX_TO_INVOKER_METHOD_TYPE = .Type.getMethodType("(I)Ljava/util/function/BiFunction;");
/*     */ 
/*     */   
/*     */   private static final String RAW_INVOKER_DESCRIPTOR = "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;";
/*     */   
/* 115 */   private static final String OBJECT_ARRAY_TYPE = .Type.getInternalName(Object[].class);
/*     */   
/*     */   private final boolean hostIsInterface;
/*     */   
/*     */   FastClass(Class<?> hostClass) {
/* 120 */     super(hostClass, "$$FastClassByGuice$$");
/* 121 */     this.hostIsInterface = hostClass.isInterface();
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] generateGlue(Collection<Executable> members) {
/* 126 */     .ClassWriter cw = new .ClassWriter(1);
/*     */ 
/*     */ 
/*     */     
/* 130 */     cw.visit(52, 49, this.proxyName, null, "java/lang/Object", FAST_CLASS_API);
/* 131 */     cw.visitSource("<generated>", null);
/*     */ 
/*     */     
/* 134 */     cw.visitField(25, "GUICE$INVOKERS", "Ljava/lang/invoke/MethodHandle;", null, null)
/* 135 */       .visitEnd();
/*     */     
/* 137 */     setupInvokerTable(cw);
/*     */     
/* 139 */     cw.visitField(18, "index", "I", null, null).visitEnd();
/*     */ 
/*     */     
/* 142 */     .MethodVisitor mv = cw.visitMethod(1, "<init>", "(I)V", null, null);
/* 143 */     mv.visitCode();
/* 144 */     mv.visitVarInsn(25, 0);
/* 145 */     mv.visitInsn(89);
/* 146 */     mv.visitMethodInsn(183, "java/lang/Object", "<init>", "()V", false);
/* 147 */     mv.visitVarInsn(21, 1);
/* 148 */     mv.visitFieldInsn(181, this.proxyName, "index", "I");
/* 149 */     mv.visitInsn(177);
/* 150 */     mv.visitMaxs(0, 0);
/* 151 */     mv.visitEnd();
/*     */ 
/*     */     
/* 154 */     mv = cw.visitMethod(1, "apply", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", null, null);
/* 155 */     mv.visitCode();
/*     */     
/* 157 */     mv.visitVarInsn(25, 0);
/* 158 */     mv.visitFieldInsn(180, this.proxyName, "index", "I");
/* 159 */     mv.visitVarInsn(25, 1);
/* 160 */     mv.visitVarInsn(25, 2);
/* 161 */     mv.visitTypeInsn(192, OBJECT_ARRAY_TYPE);
/*     */     
/* 163 */     mv.visitMethodInsn(184, this.proxyName, "GUICE$TRAMPOLINE", "(ILjava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", false);
/* 164 */     mv.visitInsn(176);
/* 165 */     mv.visitMaxs(0, 0);
/* 166 */     mv.visitEnd();
/*     */     
/* 168 */     generateTrampoline(cw, members);
/*     */     
/* 170 */     cw.visitEnd();
/* 171 */     return cw.toByteArray();
/*     */   }
/*     */ 
/*     */   
/*     */   private void setupInvokerTable(.ClassWriter cw) {
/* 176 */     .MethodVisitor mv = cw.visitMethod(10, "<clinit>", "()V", null, null);
/* 177 */     mv.visitCode();
/*     */     
/* 179 */     .Handle constructorHandle = new .Handle(8, this.proxyName, "<init>", "(I)V", false);
/*     */     
/* 181 */     mv.visitLdcInsn(constructorHandle);
/*     */ 
/*     */     
/* 184 */     mv.visitLdcInsn(INDEX_TO_INVOKER_METHOD_TYPE);
/* 185 */     mv.visitMethodInsn(182, "java/lang/invoke/MethodHandle", "asType", "(Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/MethodHandle;", false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 192 */     mv.visitFieldInsn(179, this.proxyName, "GUICE$INVOKERS", "Ljava/lang/invoke/MethodHandle;");
/*     */     
/* 194 */     mv.visitInsn(177);
/* 195 */     mv.visitMaxs(0, 0);
/* 196 */     mv.visitEnd();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void generateConstructorInvoker(.MethodVisitor mv, Constructor<?> constructor) {
/* 201 */     mv.visitTypeInsn(187, this.hostName);
/* 202 */     mv.visitInsn(89);
/*     */ 
/*     */ 
/*     */     
/* 206 */     BytecodeTasks.unpackArguments(mv, constructor.getParameterTypes());
/*     */     
/* 208 */     mv.visitMethodInsn(183, this.hostName, "<init>", 
/* 209 */         .Type.getConstructorDescriptor(constructor), false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateMethodInvoker(.MethodVisitor mv, Method method) {
/*     */     int invokeOpcode;
/* 216 */     if ((method.getModifiers() & 0x8) == 0) {
/*     */       
/* 218 */       mv.visitVarInsn(25, 1);
/* 219 */       mv.visitTypeInsn(192, this.hostName);
/* 220 */       invokeOpcode = this.hostIsInterface ? 185 : 182;
/*     */     } else {
/*     */       
/* 223 */       invokeOpcode = 184;
/*     */     } 
/*     */     
/* 226 */     BytecodeTasks.unpackArguments(mv, method.getParameterTypes());
/*     */     
/* 228 */     mv.visitMethodInsn(invokeOpcode, this.hostName, method
/*     */ 
/*     */         
/* 231 */         .getName(), 
/* 232 */         .Type.getMethodDescriptor(method), this.hostIsInterface);
/*     */ 
/*     */     
/* 235 */     Class<?> returnType = method.getReturnType();
/* 236 */     if (returnType == void.class) {
/* 237 */       mv.visitInsn(1);
/* 238 */     } else if (returnType.isPrimitive()) {
/* 239 */       BytecodeTasks.box(mv, .Type.getType(returnType));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected MethodHandle lookupInvokerTable(Class<?> glueClass) throws Throwable {
/* 245 */     return (MethodHandle)glueClass.getField("GUICE$INVOKERS").get(null);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\aop\FastClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */