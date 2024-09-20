/*     */ package by.gdev.util;
/*     */ 
/*     */ import by.gdev.util.os.OSExecutor;
/*     */ import by.gdev.util.os.OSExecutorFactoryMethod;
/*     */ import java.io.IOException;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ public class OSInfo
/*     */ {
/*  16 */   private static final Logger log = LoggerFactory.getLogger(OSInfo.class);
/*     */   
/*     */   private static final String OS_NAME = "os.name";
/*     */   private static final String OS_VERSION = "os.version";
/*     */   private static final PrivilegedAction<OSType> osTypeAction;
/*  21 */   private static final Map<String, WindowsVersion> windowsVersionMap = new HashMap<>();
/*  22 */   public static final WindowsVersion WINDOWS_UNKNOWN = new WindowsVersion(-1, -1);
/*  23 */   public static final WindowsVersion WINDOWS_95 = new WindowsVersion(4, 0);
/*  24 */   public static final WindowsVersion WINDOWS_98 = new WindowsVersion(4, 10);
/*  25 */   public static final WindowsVersion WINDOWS_ME = new WindowsVersion(4, 90);
/*  26 */   public static final WindowsVersion WINDOWS_2000 = new WindowsVersion(5, 0);
/*  27 */   public static final WindowsVersion WINDOWS_XP = new WindowsVersion(5, 1);
/*  28 */   public static final WindowsVersion WINDOWS_2003 = new WindowsVersion(5, 2);
/*  29 */   public static final WindowsVersion WINDOWS_VISTA = new WindowsVersion(6, 0);
/*  30 */   public static final WindowsVersion WINDOWS_7 = new WindowsVersion(6, 1);
/*  31 */   public static final WindowsVersion WINDOWS_8 = new WindowsVersion(6, 2);
/*  32 */   public static final WindowsVersion WINDOWS_8_1 = new WindowsVersion(6, 3);
/*  33 */   public static final WindowsVersion WINDOWS_10 = new WindowsVersion(10, 0);
/*     */   
/*     */   public enum OSType {
/*  36 */     WINDOWS, LINUX, SOLARIS, MACOSX, UNKNOWN;
/*     */   }
/*     */ 
/*     */   
/*     */   public static OSType getOSType() throws SecurityException {
/*  41 */     String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
/*  42 */     if (osName != null) {
/*  43 */       if (osName.contains("windows")) {
/*  44 */         return OSType.WINDOWS;
/*     */       }
/*     */       
/*  47 */       if (osName.contains("os x") || osName.contains("mac")) {
/*  48 */         return OSType.MACOSX;
/*     */       }
/*     */       
/*  51 */       if (osName.contains("linux") || osName.contains("unix")) {
/*  52 */         return OSType.LINUX;
/*     */       }
/*     */       
/*  55 */       if (osName.contains("solaris") || osName.contains("sunos")) {
/*  56 */         return OSType.SOLARIS;
/*     */       }
/*     */     } 
/*     */     
/*  60 */     return OSType.UNKNOWN;
/*     */   }
/*     */   
/*     */   public static Arch getJavaBit() throws IOException, InterruptedException {
/*  64 */     if (getOSType().equals(OSType.MACOSX)) {
/*  65 */       OSExecutor e = (new OSExecutorFactoryMethod()).createOsExecutor();
/*  66 */       String res = e.execute("uname -a", 30);
/*  67 */       log.info("uname -p" + res);
/*  68 */       if (res.toLowerCase().contains("arm"))
/*  69 */         return Arch.arm; 
/*     */     } 
/*  71 */     return Arch.x64;
/*     */   }
/*     */   
/*     */   public static PrivilegedAction<OSType> getOSTypeAction() {
/*  75 */     return osTypeAction;
/*     */   }
/*     */   
/*     */   public static WindowsVersion getWindowsVersion() throws SecurityException {
/*  79 */     String windowsVersion = System.getProperty("os.version");
/*  80 */     if (windowsVersion == null) {
/*  81 */       return WINDOWS_UNKNOWN;
/*     */     }
/*  83 */     synchronized (windowsVersionMap) {
/*  84 */       WindowsVersion currentVersion = windowsVersionMap.get(windowsVersion);
/*  85 */       if (currentVersion == null) {
/*  86 */         String[] data = windowsVersion.split("\\.");
/*  87 */         if (data.length != 2) {
/*  88 */           return WINDOWS_UNKNOWN;
/*     */         }
/*     */         
/*     */         try {
/*  92 */           currentVersion = new WindowsVersion(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
/*  93 */         } catch (NumberFormatException var6) {
/*  94 */           return WINDOWS_UNKNOWN;
/*     */         } 
/*     */         
/*  97 */         windowsVersionMap.put(windowsVersion, currentVersion);
/*     */       } 
/*     */       
/* 100 */       return currentVersion;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/* 106 */     windowsVersionMap.put(WINDOWS_95.toString(), WINDOWS_95);
/* 107 */     windowsVersionMap.put(WINDOWS_98.toString(), WINDOWS_98);
/* 108 */     windowsVersionMap.put(WINDOWS_ME.toString(), WINDOWS_ME);
/* 109 */     windowsVersionMap.put(WINDOWS_2000.toString(), WINDOWS_2000);
/* 110 */     windowsVersionMap.put(WINDOWS_XP.toString(), WINDOWS_XP);
/* 111 */     windowsVersionMap.put(WINDOWS_2003.toString(), WINDOWS_2003);
/* 112 */     windowsVersionMap.put(WINDOWS_VISTA.toString(), WINDOWS_VISTA);
/* 113 */     windowsVersionMap.put(WINDOWS_7.toString(), WINDOWS_7);
/* 114 */     windowsVersionMap.put(WINDOWS_8.toString(), WINDOWS_8);
/* 115 */     windowsVersionMap.put(WINDOWS_8_1.toString(), WINDOWS_8_1);
/* 116 */     windowsVersionMap.put(WINDOWS_10.toString(), WINDOWS_10);
/* 117 */     osTypeAction = OSInfo::getOSType;
/*     */   }
/*     */   
/*     */   public static class WindowsVersion implements Comparable<WindowsVersion> {
/*     */     private final int major;
/*     */     private final int minor;
/*     */     
/*     */     private WindowsVersion(int var1, int var2) {
/* 125 */       this.major = var1;
/* 126 */       this.minor = var2;
/*     */     }
/*     */     
/*     */     public int getMajor() {
/* 130 */       return this.major;
/*     */     }
/*     */     
/*     */     public int getMinor() {
/* 134 */       return this.minor;
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(WindowsVersion version) {
/* 139 */       int major = this.major - version.getMajor();
/* 140 */       if (major == 0) {
/* 141 */         major = this.minor - version.getMinor();
/*     */       }
/*     */       
/* 144 */       return major;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 149 */       return (obj instanceof WindowsVersion && compareTo((WindowsVersion)obj) == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 154 */       return 31 * this.major + this.minor;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 159 */       return this.major + "." + this.minor;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum Arch {
/* 164 */     x32, x64, arm;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gde\\util\OSInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */