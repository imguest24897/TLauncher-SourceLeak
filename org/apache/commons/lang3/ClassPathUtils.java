/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassPathUtils
/*     */ {
/*     */   public static String packageToPath(String path) {
/*  42 */     return ((String)Objects.<String>requireNonNull(path, "path")).replace('.', '/');
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
/*     */   public static String pathToPackage(String path) {
/*  54 */     return ((String)Objects.<String>requireNonNull(path, "path")).replace('/', '.');
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
/*     */ 
/*     */   
/*     */   public static String toFullyQualifiedName(Class<?> context, String resourceName) {
/*  74 */     Objects.requireNonNull(context, "context");
/*  75 */     Objects.requireNonNull(resourceName, "resourceName");
/*  76 */     return toFullyQualifiedName(context.getPackage(), resourceName);
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
/*     */ 
/*     */   
/*     */   public static String toFullyQualifiedName(Package context, String resourceName) {
/*  96 */     Objects.requireNonNull(context, "context");
/*  97 */     Objects.requireNonNull(resourceName, "resourceName");
/*  98 */     return context.getName() + "." + resourceName;
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
/*     */ 
/*     */   
/*     */   public static String toFullyQualifiedPath(Class<?> context, String resourceName) {
/* 118 */     Objects.requireNonNull(context, "context");
/* 119 */     Objects.requireNonNull(resourceName, "resourceName");
/* 120 */     return toFullyQualifiedPath(context.getPackage(), resourceName);
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
/*     */ 
/*     */   
/*     */   public static String toFullyQualifiedPath(Package context, String resourceName) {
/* 140 */     Objects.requireNonNull(context, "context");
/* 141 */     Objects.requireNonNull(resourceName, "resourceName");
/* 142 */     return packageToPath(context.getName()) + "/" + resourceName;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\ClassPathUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */