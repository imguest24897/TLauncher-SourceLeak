/*    */ package com.google.inject.internal.aop;
/*    */ 
/*    */ import com.google.inject.internal.InternalFlags;
/*    */ import java.util.logging.Logger;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ClassDefining
/*    */ {
/* 31 */   private static final Logger logger = Logger.getLogger(ClassDefining.class.getName());
/*    */   
/*    */   private static final String CLASS_DEFINING_UNSUPPORTED = "Unsafe is not accessible and custom classloading is turned OFF.";
/*    */ 
/*    */   
/*    */   private static class ClassDefinerHolder
/*    */   {
/* 38 */     static final ClassDefiner INSTANCE = ClassDefining.bindClassDefiner();
/* 39 */     static final boolean IS_UNSAFE = INSTANCE instanceof UnsafeClassDefiner;
/*    */   }
/*    */ 
/*    */   
/*    */   public static Class<?> define(Class<?> hostClass, byte[] bytecode) throws Exception {
/* 44 */     return ClassDefinerHolder.INSTANCE.define(hostClass, bytecode);
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean hasPackageAccess() {
/* 49 */     return ClassDefinerHolder.IS_UNSAFE;
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean isAnonymousHost(Class<?> hostClass) {
/* 54 */     return (ClassDefinerHolder.IS_UNSAFE && UnsafeClassDefiner.isAnonymousHost(hostClass));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   static ClassDefiner bindClassDefiner() {
/* 60 */     InternalFlags.CustomClassLoadingOption loadingOption = InternalFlags.getCustomClassLoadingOption();
/* 61 */     if (loadingOption == InternalFlags.CustomClassLoadingOption.CHILD)
/* 62 */       return new ChildClassDefiner(); 
/* 63 */     if (UnsafeClassDefiner.isAccessible())
/* 64 */       return new UnsafeClassDefiner(); 
/* 65 */     if (loadingOption != InternalFlags.CustomClassLoadingOption.OFF) {
/* 66 */       return new ChildClassDefiner();
/*    */     }
/* 68 */     logger.warning("Unsafe is not accessible and custom classloading is turned OFF.");
/* 69 */     return (hostClass, bytecode) -> {
/*    */         throw new UnsupportedOperationException("Cannot define class, Unsafe is not accessible and custom classloading is turned OFF.");
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\aop\ClassDefining.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */