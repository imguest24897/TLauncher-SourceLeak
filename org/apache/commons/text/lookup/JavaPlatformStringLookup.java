/*     */ package org.apache.commons.text.lookup;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class JavaPlatformStringLookup
/*     */   extends AbstractStringLookup
/*     */ {
/*  64 */   static final JavaPlatformStringLookup INSTANCE = new JavaPlatformStringLookup();
/*     */ 
/*     */   
/*     */   private static final String KEY_HARDWARE = "hardware";
/*     */ 
/*     */   
/*     */   private static final String KEY_LOCALE = "locale";
/*     */ 
/*     */   
/*     */   private static final String KEY_OS = "os";
/*     */ 
/*     */   
/*     */   private static final String KEY_RUNTIME = "runtime";
/*     */ 
/*     */   
/*     */   private static final String KEY_VERSION = "version";
/*     */   
/*     */   private static final String KEY_VM = "vm";
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/*  85 */     System.out.println(JavaPlatformStringLookup.class);
/*  86 */     System.out.printf("%s = %s%n", new Object[] { "version", INSTANCE.lookup("version") });
/*  87 */     System.out.printf("%s = %s%n", new Object[] { "runtime", INSTANCE.lookup("runtime") });
/*  88 */     System.out.printf("%s = %s%n", new Object[] { "vm", INSTANCE.lookup("vm") });
/*  89 */     System.out.printf("%s = %s%n", new Object[] { "os", INSTANCE.lookup("os") });
/*  90 */     System.out.printf("%s = %s%n", new Object[] { "hardware", INSTANCE.lookup("hardware") });
/*  91 */     System.out.printf("%s = %s%n", new Object[] { "locale", INSTANCE.lookup("locale") });
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
/*     */   String getHardware() {
/* 107 */     return "processors: " + Runtime.getRuntime().availableProcessors() + ", architecture: " + 
/* 108 */       getSystemProperty("os.arch") + getSystemProperty("-", "sun.arch.data.model") + 
/* 109 */       getSystemProperty(", instruction sets: ", "sun.cpu.isalist");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getLocale() {
/* 118 */     return "default locale: " + Locale.getDefault() + ", platform encoding: " + getSystemProperty("file.encoding");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getOperatingSystem() {
/* 127 */     return getSystemProperty("os.name") + " " + getSystemProperty("os.version") + 
/* 128 */       getSystemProperty(" ", "sun.os.patch.level") + ", architecture: " + getSystemProperty("os.arch") + 
/* 129 */       getSystemProperty("-", "sun.arch.data.model");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getRuntime() {
/* 138 */     return getSystemProperty("java.runtime.name") + " (build " + getSystemProperty("java.runtime.version") + ") from " + 
/* 139 */       getSystemProperty("java.vendor");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getSystemProperty(String name) {
/* 149 */     return StringLookupFactory.INSTANCE_SYSTEM_PROPERTIES.lookup(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getSystemProperty(String prefix, String name) {
/* 160 */     String value = getSystemProperty(name);
/* 161 */     if (StringUtils.isEmpty(value)) {
/* 162 */       return "";
/*     */     }
/* 164 */     return prefix + value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getVirtualMachine() {
/* 173 */     return getSystemProperty("java.vm.name") + " (build " + getSystemProperty("java.vm.version") + ", " + 
/* 174 */       getSystemProperty("java.vm.info") + ")";
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
/*     */   public String lookup(String key) {
/* 196 */     if (key == null) {
/* 197 */       return null;
/*     */     }
/* 199 */     switch (key) {
/*     */       case "version":
/* 201 */         return "Java version " + getSystemProperty("java.version");
/*     */       case "runtime":
/* 203 */         return getRuntime();
/*     */       case "vm":
/* 205 */         return getVirtualMachine();
/*     */       case "os":
/* 207 */         return getOperatingSystem();
/*     */       case "hardware":
/* 209 */         return getHardware();
/*     */       case "locale":
/* 211 */         return getLocale();
/*     */     } 
/* 213 */     throw new IllegalArgumentException(key);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\lookup\JavaPlatformStringLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */