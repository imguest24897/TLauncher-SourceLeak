/*    */ package org.apache.commons.lang3;
/*    */ 
/*    */ import java.net.URL;
/*    */ import java.net.URLClassLoader;
/*    */ import java.util.Arrays;
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
/*    */ public class ClassLoaderUtils
/*    */ {
/* 31 */   private static final URL[] EMPTY_URL_ARRAY = new URL[0];
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static URL[] getSystemURLs() {
/* 40 */     return getURLs(ClassLoader.getSystemClassLoader());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static URL[] getThreadURLs() {
/* 50 */     return getURLs(Thread.currentThread().getContextClassLoader());
/*    */   }
/*    */   
/*    */   private static URL[] getURLs(ClassLoader cl) {
/* 54 */     return (cl instanceof URLClassLoader) ? ((URLClassLoader)cl).getURLs() : EMPTY_URL_ARRAY;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String toString(ClassLoader classLoader) {
/* 64 */     if (classLoader instanceof URLClassLoader) {
/* 65 */       return toString((URLClassLoader)classLoader);
/*    */     }
/* 67 */     return classLoader.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String toString(URLClassLoader classLoader) {
/* 77 */     return classLoader + Arrays.toString((Object[])classLoader.getURLs());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\ClassLoaderUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */