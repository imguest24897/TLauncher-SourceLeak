/*     */ package com.google.inject.internal.aop;
/*     */ 
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.google.inject.internal.InternalFlags;
/*     */ import com.google.inject.internal.asm.;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class UnsafeClassDefiner
/*     */   implements ClassDefiner
/*     */ {
/*  52 */   private static final Logger logger = Logger.getLogger(UnsafeClassDefiner.class.getName());
/*     */ 
/*     */   
/*  55 */   private static final Object THE_UNSAFE = tryPrivileged(UnsafeClassDefiner::bindTheUnsafe, "Cannot bind the Unsafe instance");
/*     */ 
/*     */   
/*  58 */   private static final Method ANONYMOUS_DEFINE_METHOD = tryPrivileged(UnsafeClassDefiner::bindAnonymousDefineMethod, "Cannot bind Unsafe.defineAnonymousClass");
/*     */ 
/*     */ 
/*     */   
/*  62 */   private static final boolean ALWAYS_DEFINE_ANONYMOUSLY = (InternalFlags.getCustomClassLoadingOption() == InternalFlags.CustomClassLoadingOption.ANONYMOUS);
/*     */   
/*     */   private static final String DEFINEACCESS_BY_GUICE_MARKER = "$$DefineAccessByGuice$$";
/*     */ 
/*     */   
/*     */   private static class ClassLoaderDefineMethodHolder
/*     */   {
/*  69 */     static final Method CLASS_LOADER_DEFINE_METHOD = UnsafeClassDefiner.<Method>tryPrivileged(() -> UnsafeClassDefiner.accessDefineMethod(ClassLoader.class), "Cannot access ClassLoader.defineClass");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class DefineMethodCacheHolder
/*     */   {
/*  76 */     static final LoadingCache<Class<?>, Method> DEFINE_METHOD_CACHE = CacheBuilder.newBuilder()
/*  77 */       .weakKeys()
/*  78 */       .weakValues()
/*  79 */       .build(CacheLoader.from(UnsafeClassDefiner::tryAccessDefineMethod));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isAccessible() {
/*  84 */     return (ANONYMOUS_DEFINE_METHOD != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAnonymousHost(Class<?> hostClass) {
/*  90 */     return (findDefineMethod(hostClass.getClassLoader()) == ANONYMOUS_DEFINE_METHOD);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> define(Class<?> hostClass, byte[] bytecode) throws Exception {
/*  96 */     ClassLoader hostLoader = hostClass.getClassLoader();
/*  97 */     Method defineMethod = findDefineMethod(hostLoader);
/*  98 */     if (defineMethod == ANONYMOUS_DEFINE_METHOD) {
/*  99 */       return defineAnonymously(hostClass, bytecode);
/*     */     }
/* 101 */     return (Class)defineMethod.invoke(null, new Object[] { hostLoader, bytecode });
/*     */   }
/*     */ 
/*     */   
/*     */   private static Method findDefineMethod(ClassLoader hostLoader) {
/* 106 */     if (hostLoader == null || ALWAYS_DEFINE_ANONYMOUSLY)
/* 107 */       return ANONYMOUS_DEFINE_METHOD; 
/* 108 */     if (ClassLoaderDefineMethodHolder.CLASS_LOADER_DEFINE_METHOD != null)
/*     */     {
/* 110 */       return ClassLoaderDefineMethodHolder.CLASS_LOADER_DEFINE_METHOD;
/*     */     }
/*     */     
/* 113 */     return (Method)DefineMethodCacheHolder.DEFINE_METHOD_CACHE.getUnchecked(hostLoader.getClass());
/*     */   }
/*     */ 
/*     */   
/*     */   private static Class<?> defineAnonymously(Class<?> hostClass, byte[] bytecode) throws Exception {
/* 118 */     return (Class)ANONYMOUS_DEFINE_METHOD.invoke(THE_UNSAFE, new Object[] { hostClass, bytecode, null });
/*     */   }
/*     */   
/*     */   private static Object bindTheUnsafe() throws Exception {
/* 122 */     Class<?> unsafeType = Class.forName("sun.misc.Unsafe");
/* 123 */     Field theUnsafeField = unsafeType.getDeclaredField("theUnsafe");
/* 124 */     theUnsafeField.setAccessible(true);
/* 125 */     return theUnsafeField.get(null);
/*     */   }
/*     */   
/*     */   private static Method bindAnonymousDefineMethod() throws Exception {
/* 129 */     Class<?> unsafeType = THE_UNSAFE.getClass();
/*     */     
/* 131 */     return unsafeType.getMethod("defineAnonymousClass", new Class[] { Class.class, byte[].class, Object[].class });
/*     */   }
/*     */   
/*     */   static <T> T tryPrivileged(PrivilegedExceptionAction<T> action, String errorMessage) {
/*     */     try {
/* 136 */       return AccessController.doPrivileged(action);
/* 137 */     } catch (Throwable e) {
/* 138 */       logger.log(Level.FINE, errorMessage, e);
/* 139 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   static Method tryAccessDefineMethod(Class<?> loaderClass) {
/*     */     try {
/* 145 */       logger.log(Level.FINE, "Accessing defineClass method in %s", loaderClass);
/* 146 */       return AccessController.<Method>doPrivileged(() -> accessDefineMethod(loaderClass));
/*     */     }
/* 148 */     catch (Throwable e) {
/* 149 */       String str = String.valueOf(loaderClass); logger.log(Level.FINE, (new StringBuilder(36 + String.valueOf(str).length())).append("Cannot access defineClass method in ").append(str).toString(), e);
/* 150 */       return ANONYMOUS_DEFINE_METHOD;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static Method accessDefineMethod(Class<?> loaderClass) throws Exception {
/* 156 */     byte[] bytecode = buildDefineClassAccess(loaderClass);
/* 157 */     Class<?> accessClass = defineAnonymously(loaderClass, bytecode);
/* 158 */     return accessClass.getMethod("defineClass", new Class[] { ClassLoader.class, byte[].class });
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] buildDefineClassAccess(Class<?> loaderClass) {
/* 163 */     .ClassWriter cw = new .ClassWriter(1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 169 */     String.valueOf("$$DefineAccessByGuice$$"); cw.visit(52, 33, (String.valueOf("$$DefineAccessByGuice$$").length() != 0) ? String.valueOf(loaderClass.getName().replace('.', '/')).concat(String.valueOf("$$DefineAccessByGuice$$")) : new String(String.valueOf(loaderClass.getName().replace('.', '/'))), null, "java/lang/Object", null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     .MethodVisitor mv = cw.visitMethod(9, "defineClass", "(Ljava/lang/ClassLoader;[B)Ljava/lang/Class;", null, null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 182 */     mv.visitCode();
/*     */     
/* 184 */     mv.visitVarInsn(25, 0);
/* 185 */     mv.visitInsn(1);
/* 186 */     mv.visitVarInsn(25, 1);
/* 187 */     mv.visitInsn(3);
/* 188 */     mv.visitVarInsn(25, 1);
/* 189 */     mv.visitInsn(190);
/*     */     
/* 191 */     mv.visitMethodInsn(182, "java/lang/ClassLoader", "defineClass", "(Ljava/lang/String;[BII)Ljava/lang/Class;", false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 198 */     mv.visitInsn(176);
/*     */     
/* 200 */     mv.visitMaxs(0, 0);
/* 201 */     mv.visitEnd();
/* 202 */     cw.visitEnd();
/*     */     
/* 204 */     return cw.toByteArray();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\aop\UnsafeClassDefiner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */