/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ public enum StandardSystemProperty
/*     */ {
/*  30 */   JAVA_VERSION("java.version"),
/*     */ 
/*     */   
/*  33 */   JAVA_VENDOR("java.vendor"),
/*     */ 
/*     */   
/*  36 */   JAVA_VENDOR_URL("java.vendor.url"),
/*     */ 
/*     */   
/*  39 */   JAVA_HOME("java.home"),
/*     */ 
/*     */   
/*  42 */   JAVA_VM_SPECIFICATION_VERSION("java.vm.specification.version"),
/*     */ 
/*     */   
/*  45 */   JAVA_VM_SPECIFICATION_VENDOR("java.vm.specification.vendor"),
/*     */ 
/*     */   
/*  48 */   JAVA_VM_SPECIFICATION_NAME("java.vm.specification.name"),
/*     */ 
/*     */   
/*  51 */   JAVA_VM_VERSION("java.vm.version"),
/*     */ 
/*     */   
/*  54 */   JAVA_VM_VENDOR("java.vm.vendor"),
/*     */ 
/*     */   
/*  57 */   JAVA_VM_NAME("java.vm.name"),
/*     */ 
/*     */   
/*  60 */   JAVA_SPECIFICATION_VERSION("java.specification.version"),
/*     */ 
/*     */   
/*  63 */   JAVA_SPECIFICATION_VENDOR("java.specification.vendor"),
/*     */ 
/*     */   
/*  66 */   JAVA_SPECIFICATION_NAME("java.specification.name"),
/*     */ 
/*     */   
/*  69 */   JAVA_CLASS_VERSION("java.class.version"),
/*     */ 
/*     */   
/*  72 */   JAVA_CLASS_PATH("java.class.path"),
/*     */ 
/*     */   
/*  75 */   JAVA_LIBRARY_PATH("java.library.path"),
/*     */ 
/*     */   
/*  78 */   JAVA_IO_TMPDIR("java.io.tmpdir"),
/*     */ 
/*     */   
/*  81 */   JAVA_COMPILER("java.compiler"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   JAVA_EXT_DIRS("java.ext.dirs"),
/*     */ 
/*     */ 
/*     */   
/*  95 */   OS_NAME("os.name"),
/*     */ 
/*     */   
/*  98 */   OS_ARCH("os.arch"),
/*     */ 
/*     */   
/* 101 */   OS_VERSION("os.version"),
/*     */ 
/*     */   
/* 104 */   FILE_SEPARATOR("file.separator"),
/*     */ 
/*     */   
/* 107 */   PATH_SEPARATOR("path.separator"),
/*     */ 
/*     */   
/* 110 */   LINE_SEPARATOR("line.separator"),
/*     */ 
/*     */   
/* 113 */   USER_NAME("user.name"),
/*     */ 
/*     */   
/* 116 */   USER_HOME("user.home"),
/*     */ 
/*     */   
/* 119 */   USER_DIR("user.dir");
/*     */   
/*     */   private final String key;
/*     */   
/*     */   StandardSystemProperty(String key) {
/* 124 */     this.key = key;
/*     */   }
/*     */ 
/*     */   
/*     */   public String key() {
/* 129 */     return this.key;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String value() {
/* 156 */     return System.getProperty(this.key);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 162 */     String str1 = key(), str2 = value(); return (new StringBuilder(1 + String.valueOf(str1).length() + String.valueOf(str2).length())).append(str1).append("=").append(str2).toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\base\StandardSystemProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */