/*    */ package com.google.inject.internal.aop;
/*    */ 
/*    */ import com.google.common.cache.CacheBuilder;
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import com.google.common.cache.LoadingCache;
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
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
/*    */ final class ChildClassDefiner
/*    */   implements ClassDefiner
/*    */ {
/* 33 */   private static final Logger logger = Logger.getLogger(ChildClassDefiner.class.getName());
/*    */   
/*    */   private static class SystemChildLoaderHolder
/*    */   {
/* 37 */     static final ChildClassDefiner.ChildLoader SYSTEM_CHILD_LOADER = ChildClassDefiner.<ChildClassDefiner.ChildLoader>doPrivileged(ChildLoader::new);
/*    */   }
/*    */ 
/*    */   
/*    */   private static class ChildLoaderCacheHolder
/*    */   {
/* 43 */     static final LoadingCache<ClassLoader, ChildClassDefiner.ChildLoader> CHILD_LOADER_CACHE = CacheBuilder.newBuilder()
/* 44 */       .weakKeys()
/* 45 */       .weakValues()
/* 46 */       .build(CacheLoader.from(ChildClassDefiner::childLoader));
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<?> define(Class<?> hostClass, byte[] bytecode) throws Exception {
/* 51 */     ClassLoader hostLoader = hostClass.getClassLoader();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 56 */     ChildLoader childLoader = (hostLoader != null) ? (ChildLoader)ChildLoaderCacheHolder.CHILD_LOADER_CACHE.get(hostLoader) : SystemChildLoaderHolder.SYSTEM_CHILD_LOADER;
/*    */     
/* 58 */     return childLoader.defineInChild(bytecode);
/*    */   }
/*    */ 
/*    */   
/*    */   static <T> T doPrivileged(PrivilegedAction<T> action) {
/* 63 */     return AccessController.doPrivileged(action);
/*    */   }
/*    */ 
/*    */   
/*    */   static ChildLoader childLoader(ClassLoader hostLoader) {
/* 68 */     String str = String.valueOf(hostLoader); logger.fine((new StringBuilder(28 + String.valueOf(str).length())).append("Creating a child loader for ").append(str).toString());
/* 69 */     return doPrivileged(() -> new ChildLoader(hostLoader));
/*    */   }
/*    */   
/*    */   private static final class ChildLoader
/*    */     extends ClassLoader {
/*    */     ChildLoader(ClassLoader parent) {
/* 75 */       super(parent);
/*    */     }
/*    */ 
/*    */     
/*    */     ChildLoader() {}
/*    */ 
/*    */     
/*    */     Class<?> defineInChild(byte[] bytecode) {
/* 83 */       Class<?> type = defineClass(null, bytecode, 0, bytecode.length, null);
/* 84 */       resolveClass(type);
/* 85 */       return type;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\aop\ChildClassDefiner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */