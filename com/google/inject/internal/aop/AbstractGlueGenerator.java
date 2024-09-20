/*     */ package com.google.inject.internal.aop;
/*     */ 
/*     */ import com.google.inject.internal.asm.;
/*     */ import java.lang.invoke.MethodHandle;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Executable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.ToIntFunction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractGlueGenerator
/*     */ {
/*     */   protected static final String GENERATED_SOURCE = "<generated>";
/*     */   protected static final String TRAMPOLINE_NAME = "GUICE$TRAMPOLINE";
/*     */   protected static final String TRAMPOLINE_DESCRIPTOR = "(ILjava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;";
/*     */   protected final Class<?> hostClass;
/*     */   protected final String hostName;
/*     */   protected final String proxyName;
/*  95 */   private static final AtomicInteger COUNTER = new AtomicInteger();
/*     */   
/*     */   protected AbstractGlueGenerator(Class<?> hostClass, String marker) {
/*  98 */     this.hostClass = hostClass;
/*  99 */     this.hostName = .Type.getInternalName(hostClass);
/* 100 */     this.proxyName = proxyName(this.hostName, marker, hashCode());
/*     */   }
/*     */ 
/*     */   
/*     */   private static String proxyName(String hostName, String marker, int hash) {
/* 105 */     int id = hash & 0xFFFFF | COUNTER.getAndIncrement() << 20;
/* 106 */     String proxyName = (new StringBuilder(11 + String.valueOf(hostName).length() + String.valueOf(marker).length())).append(hostName).append(marker).append(id).toString();
/* 107 */     if (proxyName.startsWith("java/") && !ClassDefining.hasPackageAccess()) {
/* 108 */       String str = proxyName; proxyName = (new StringBuilder(1 + String.valueOf(str).length())).append('$').append(str).toString();
/*     */     } 
/* 110 */     return proxyName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Function<String, BiFunction<Object, Object[], Object>> glue(NavigableMap<String, Executable> glueMap) {
/*     */     MethodHandle invokerTable;
/*     */     try {
/* 118 */       byte[] bytecode = generateGlue(glueMap.values());
/* 119 */       Class<?> glueClass = ClassDefining.define(this.hostClass, bytecode);
/* 120 */       invokerTable = lookupInvokerTable(glueClass);
/* 121 */     } catch (Throwable e) {
/* 122 */       String.valueOf(this.proxyName); throw new GlueException((String.valueOf(this.proxyName).length() != 0) ? "Problem generating ".concat(String.valueOf(this.proxyName)) : new String("Problem generating "), e);
/*     */     } 
/*     */ 
/*     */     
/* 126 */     ToIntFunction<String> signatureTable = ImmutableStringTrie.buildTrie(glueMap.keySet());
/* 127 */     return bindSignaturesToInvokers(signatureTable, invokerTable);
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
/*     */   private static Function<String, BiFunction<Object, Object[], Object>> bindSignaturesToInvokers(ToIntFunction<String> signatureTable, MethodHandle invokerTable) {
/* 141 */     if (invokerTable.type().parameterCount() == 1) {
/* 142 */       return signature -> {
/*     */ 
/*     */           
/*     */           try {
/*     */             return invokerTable.invokeExact(signatureTable.applyAsInt(signature));
/* 147 */           } catch (Throwable e) {
/*     */             throw asIfUnchecked(e);
/*     */           } 
/*     */         };
/*     */     }
/*     */ 
/*     */     
/* 154 */     return signature -> {
/*     */         int index = signatureTable.applyAsInt(signature);
/*     */         return ();
/*     */       };
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
/*     */   private static <E extends Throwable> RuntimeException asIfUnchecked(Throwable e) throws E {
/* 171 */     throw (E)e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void generateTrampoline(.ClassWriter cw, Collection<Executable> members) {
/* 180 */     .MethodVisitor mv = cw.visitMethod(9, "GUICE$TRAMPOLINE", "(ILjava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", null, null);
/* 181 */     mv.visitCode();
/*     */     
/* 183 */     .Label[] labels = new .Label[members.size()];
/* 184 */     Arrays.setAll(labels, i -> new .Label());
/* 185 */     .Label defaultLabel = new .Label();
/*     */     
/* 187 */     mv.visitVarInsn(21, 0);
/* 188 */     mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);
/*     */     
/* 190 */     int labelIndex = 0;
/* 191 */     for (Executable member : members) {
/* 192 */       mv.visitLabel(labels[labelIndex++]);
/* 193 */       mv.visitFrame(3, 0, null, 0, null);
/* 194 */       if (member instanceof Constructor) {
/* 195 */         generateConstructorInvoker(mv, (Constructor)member);
/*     */       } else {
/* 197 */         generateMethodInvoker(mv, (Method)member);
/*     */       } 
/* 199 */       mv.visitInsn(176);
/*     */     } 
/*     */     
/* 202 */     mv.visitLabel(defaultLabel);
/* 203 */     mv.visitFrame(3, 0, null, 0, null);
/* 204 */     mv.visitInsn(1);
/* 205 */     mv.visitInsn(176);
/*     */     
/* 207 */     mv.visitMaxs(0, 0);
/* 208 */     mv.visitEnd();
/*     */   }
/*     */   
/*     */   protected abstract byte[] generateGlue(Collection<Executable> paramCollection);
/*     */   
/*     */   protected abstract MethodHandle lookupInvokerTable(Class<?> paramClass) throws Throwable;
/*     */   
/*     */   protected abstract void generateConstructorInvoker(.MethodVisitor paramMethodVisitor, Constructor<?> paramConstructor);
/*     */   
/*     */   protected abstract void generateMethodInvoker(.MethodVisitor paramMethodVisitor, Method paramMethod);
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\aop\AbstractGlueGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */