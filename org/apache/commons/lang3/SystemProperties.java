/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.IntSupplier;
/*     */ import java.util.function.LongSupplier;
/*     */ import java.util.function.Supplier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SystemProperties
/*     */ {
/*     */   private static final Supplier<String> NULL_SUPPLIER = () -> null;
/*     */   public static final String AWT_TOOLKIT = "awt.toolkit";
/*     */   public static final String FILE_ENCODING = "file.encoding";
/*     */   public static final String FILE_SEPARATOR = "file.separator";
/*     */   public static final String JAVA_AWT_FONTS = "java.awt.fonts";
/*     */   public static final String JAVA_AWT_GRAPHICSENV = "java.awt.graphicsenv";
/*     */   public static final String JAVA_AWT_HEADLESS = "java.awt.headless";
/*     */   public static final String JAVA_AWT_PRINTERJOB = "java.awt.printerjob";
/*     */   public static final String JAVA_CLASS_PATH = "java.class.path";
/*     */   public static final String JAVA_CLASS_VERSION = "java.class.version";
/*     */   public static final String JAVA_COMPILER = "java.compiler";
/*     */   public static final String JAVA_ENDORSED_DIRS = "java.endorsed.dirs";
/*     */   public static final String JAVA_EXT_DIRS = "java.ext.dirs";
/*     */   public static final String JAVA_HOME = "java.home";
/*     */   public static final String JAVA_IO_TMPDIR = "java.io.tmpdir";
/*     */   public static final String JAVA_LIBRARY_PATH = "java.library.path";
/*     */   public static final String JAVA_LOCALE_PROVIDERS = "java.locale.providers";
/*     */   public static final String JAVA_RUNTIME_NAME = "java.runtime.name";
/*     */   public static final String JAVA_RUNTIME_VERSION = "java.runtime.version";
/*     */   public static final String JAVA_SPECIFICATION_NAME = "java.specification.name";
/*     */   public static final String JAVA_SPECIFICATION_VENDOR = "java.specification.vendor";
/*     */   public static final String JAVA_SPECIFICATION_VERSION = "java.specification.version";
/*     */   public static final String JAVA_UTIL_PREFS_PREFERENCES_FACTORY = "java.util.prefs.PreferencesFactory";
/*     */   public static final String JAVA_VENDOR = "java.vendor";
/*     */   public static final String JAVA_VENDOR_URL = "java.vendor.url";
/*     */   public static final String JAVA_VERSION = "java.version";
/*     */   public static final String JAVA_VM_INFO = "java.vm.info";
/*     */   public static final String JAVA_VM_NAME = "java.vm.name";
/*     */   public static final String JAVA_VM_SPECIFICATION_NAME = "java.vm.specification.name";
/*     */   public static final String JAVA_VM_SPECIFICATION_VENDOR = "java.vm.specification.vendor";
/*     */   public static final String JAVA_VM_SPECIFICATION_VERSION = "java.vm.specification.version";
/*     */   public static final String JAVA_VM_VENDOR = "java.vm.vendor";
/*     */   public static final String JAVA_VM_VERSION = "java.vm.version";
/*     */   public static final String LINE_SEPARATOR = "line.separator";
/*     */   public static final String OS_ARCH = "os.arch";
/*     */   public static final String OS_NAME = "os.name";
/*     */   public static final String OS_VERSION = "os.version";
/*     */   public static final String PATH_SEPARATOR = "path.separator";
/*     */   public static final String USER_COUNTRY = "user.country";
/*     */   public static final String USER_DIR = "user.dir";
/*     */   public static final String USER_HOME = "user.home";
/*     */   public static final String USER_LANGUAGE = "user.language";
/*     */   public static final String USER_NAME = "user.name";
/*     */   public static final String USER_REGION = "user.region";
/*     */   public static final String USER_TIMEZONE = "user.timezone";
/*     */   
/*     */   public static String getAwtToolkit() {
/* 263 */     return getProperty("awt.toolkit");
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
/*     */   public static boolean getBoolean(String key, BooleanSupplier defaultIfAbsent) {
/* 276 */     String str = getProperty(key);
/* 277 */     return (str == null) ? ((defaultIfAbsent != null && defaultIfAbsent.getAsBoolean())) : Boolean.parseBoolean(str);
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
/*     */   public static String getFileEncoding() {
/* 292 */     return getProperty("file.encoding");
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
/*     */   public static String getFileSeparator() {
/* 304 */     return getProperty("file.separator");
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
/*     */   public static int getInt(String key, IntSupplier defaultIfAbsent) {
/* 317 */     String str = getProperty(key);
/* 318 */     return (str == null) ? ((defaultIfAbsent != null) ? defaultIfAbsent.getAsInt() : 0) : Integer.parseInt(str);
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
/*     */   public static String getJavaAwtFonts() {
/* 330 */     return getProperty("java.awt.fonts");
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
/*     */   public static String getJavaAwtGraphicsenv() {
/* 342 */     return getProperty("java.awt.graphicsenv");
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
/*     */   public static String getJavaAwtHeadless() {
/* 354 */     return getProperty("java.awt.headless");
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
/*     */   public static String getJavaAwtPrinterjob() {
/* 366 */     return getProperty("java.awt.printerjob");
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
/*     */   public static String getJavaClassPath() {
/* 378 */     return getProperty("java.class.path");
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
/*     */   public static String getJavaClassVersion() {
/* 390 */     return getProperty("java.class.version");
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
/*     */   public static String getJavaCompiler() {
/* 402 */     return getProperty("java.compiler");
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
/*     */   public static String getJavaEndorsedDirs() {
/* 414 */     return getProperty("java.endorsed.dirs");
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
/*     */   public static String getJavaExtDirs() {
/* 426 */     return getProperty("java.ext.dirs");
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
/*     */   public static String getJavaHome() {
/* 438 */     return getProperty("java.home");
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
/*     */   public static String getJavaIoTmpdir() {
/* 450 */     return getProperty("java.io.tmpdir");
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
/*     */   public static String getJavaLibraryPath() {
/* 462 */     return getProperty("java.library.path");
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
/*     */   public static String getJavaLocaleProviders() {
/* 476 */     return getProperty("java.locale.providers");
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
/*     */   public static String getJavaRuntimeName() {
/* 488 */     return getProperty("java.runtime.name");
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
/*     */   public static String getJavaRuntimeVersion() {
/* 500 */     return getProperty("java.runtime.version");
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
/*     */   public static String getJavaSpecificationName() {
/* 512 */     return getProperty("java.specification.name");
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
/*     */   public static String getJavaSpecificationVendor() {
/* 524 */     return getProperty("java.specification.vendor");
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
/*     */   public static String getJavaSpecificationVersion() {
/* 536 */     return getProperty("java.specification.version");
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
/*     */   public static String getJavaUtilPrefsPreferencesFactory() {
/* 548 */     return getProperty("java.util.prefs.PreferencesFactory");
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
/*     */   public static String getJavaVendor() {
/* 560 */     return getProperty("java.vendor");
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
/*     */   public static String getJavaVendorUrl() {
/* 572 */     return getProperty("java.vendor.url");
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
/*     */   public static String getJavaVersion() {
/* 584 */     return getProperty("java.version");
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
/*     */   public static String getJavaVmInfo() {
/* 596 */     return getProperty("java.vm.info");
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
/*     */   public static String getJavaVmName() {
/* 608 */     return getProperty("java.vm.name");
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
/*     */   public static String getJavaVmSpecificationName() {
/* 620 */     return getProperty("java.vm.specification.name");
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
/*     */   public static String getJavaVmSpecificationVendor() {
/* 632 */     return getProperty("java.vm.specification.vendor");
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
/*     */   public static String getJavaVmSpecificationVersion() {
/* 644 */     return getProperty("java.vm.specification.version");
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
/*     */   public static String getJavaVmVendor() {
/* 656 */     return getProperty("java.vm.vendor");
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
/*     */   public static String getJavaVmVersion() {
/* 668 */     return getProperty("java.vm.version");
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
/*     */   public static String getLineSeparator() {
/* 680 */     return getProperty("line.separator");
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
/*     */   public static long getLong(String key, LongSupplier defaultIfAbsent) {
/* 693 */     String str = getProperty(key);
/* 694 */     return (str == null) ? ((defaultIfAbsent != null) ? defaultIfAbsent.getAsLong() : 0L) : Long.parseLong(str);
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
/*     */   public static String getOsArch() {
/* 706 */     return getProperty("os.arch");
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
/*     */   public static String getOsName() {
/* 718 */     return getProperty("os.name");
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
/*     */   public static String getOsVersion() {
/* 730 */     return getProperty("os.version");
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
/*     */   public static String getPathSeparator() {
/* 742 */     return getProperty("path.separator");
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
/*     */   public static String getProperty(String property) {
/* 755 */     return getProperty(property, NULL_SUPPLIER);
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
/*     */   static String getProperty(String property, Supplier<String> defaultValue) {
/*     */     try {
/* 770 */       if (StringUtils.isEmpty(property)) {
/* 771 */         return defaultValue.get();
/*     */       }
/* 773 */       String value = System.getProperty(property);
/* 774 */       return StringUtils.<String>getIfEmpty(value, defaultValue);
/* 775 */     } catch (SecurityException ignore) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 780 */       return defaultValue.get();
/*     */     } 
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
/*     */   public static String getUserCountry() {
/* 793 */     return getProperty("user.country");
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
/*     */   public static String getUserDir() {
/* 805 */     return getProperty("user.dir");
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
/*     */   public static String getUserHome() {
/* 817 */     return getProperty("user.home");
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
/*     */   public static String getUserLanguage() {
/* 829 */     return getProperty("user.language");
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
/*     */   public static String getUserName() {
/* 841 */     return getProperty("user.name");
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
/*     */   public static String getUserTimezone() {
/* 853 */     return getProperty("user.timezone");
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\SystemProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */