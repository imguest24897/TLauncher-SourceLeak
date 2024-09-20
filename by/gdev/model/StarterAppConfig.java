/*     */ package by.gdev.model;
/*     */ 
/*     */ import by.gdev.Main;
/*     */ import by.gdev.util.DesktopUtil;
/*     */ import by.gdev.util.OSInfo;
/*     */ import by.gdev.utils.service.FileMapperService;
/*     */ import com.beust.jcommander.Parameter;
/*     */ import com.beust.jcommander.internal.Lists;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Properties;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StarterAppConfig
/*     */ {
/*     */   private static final String APP_CONFIG = "appConfig.json";
/*     */   public static final String APP_CHANGES_LOG = "changes.log";
/*     */   public static final String APP_STARTER_LOCAL_CONFIG = "starter.json";
/*     */   public static final String APP_STARTER_UPDATE_CONFIG = "starterUpdate.json";
/*     */   public static final String ADVERTISEMENT_CONFIG = "/additionalUpdateConfiguration.json";
/*     */   
/*     */   public void setMinMemorySize(long minMemorySize) {
/*  36 */     this.minMemorySize = minMemorySize; } public void setServerFile(List<String> serverFile) { this.serverFile = serverFile; } public void setWorkDirectory(String workDirectory) { this.workDirectory = workDirectory; } public void setCacheDirectory(Path cacheDirectory) { this.cacheDirectory = cacheDirectory; } public void setVersion(String version) { this.version = version; } public void setUrlConnection(List<String> urlConnection) { this.urlConnection = urlConnection; } public void setMaxAttempts(int maxAttempts) { this.maxAttempts = maxAttempts; } public void setConnectTimeout(int connectTimeout) { this.connectTimeout = connectTimeout; } public void setSocketTimeout(int socketTimeout) { this.socketTimeout = socketTimeout; } public void setTimeToLife(int timeToLife) { this.timeToLife = timeToLife; } public void setStop(boolean stop) { this.stop = stop; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof StarterAppConfig)) return false;  StarterAppConfig other = (StarterAppConfig)o; if (!other.canEqual(this)) return false;  if (isProd() != other.isProd()) return false;  if (getMinMemorySize() != other.getMinMemorySize()) return false;  Object<String> this$serverFile = (Object<String>)getServerFile(), other$serverFile = (Object<String>)other.getServerFile(); if ((this$serverFile == null) ? (other$serverFile != null) : !this$serverFile.equals(other$serverFile)) return false;  Object this$workDirectory = getWorkDirectory(), other$workDirectory = other.getWorkDirectory(); if ((this$workDirectory == null) ? (other$workDirectory != null) : !this$workDirectory.equals(other$workDirectory)) return false;  Object this$cacheDirectory = getCacheDirectory(), other$cacheDirectory = other.getCacheDirectory(); if ((this$cacheDirectory == null) ? (other$cacheDirectory != null) : !this$cacheDirectory.equals(other$cacheDirectory)) return false;  Object this$version = getVersion(), other$version = other.getVersion(); if ((this$version == null) ? (other$version != null) : !this$version.equals(other$version)) return false;  Object<String> this$urlConnection = (Object<String>)getUrlConnection(), other$urlConnection = (Object<String>)other.getUrlConnection(); return ((this$urlConnection == null) ? (other$urlConnection != null) : !this$urlConnection.equals(other$urlConnection)) ? false : ((getMaxAttempts() != other.getMaxAttempts()) ? false : ((getConnectTimeout() != other.getConnectTimeout()) ? false : ((getSocketTimeout() != other.getSocketTimeout()) ? false : ((getTimeToLife() != other.getTimeToLife()) ? false : (!(isStop() != other.isStop())))))); } protected boolean canEqual(Object other) { return other instanceof StarterAppConfig; } public int hashCode() { int PRIME = 59; result = 1; result = result * 59 + (isProd() ? 79 : 97); long $minMemorySize = getMinMemorySize(); result = result * 59 + (int)($minMemorySize >>> 32L ^ $minMemorySize); Object<String> $serverFile = (Object<String>)getServerFile(); result = result * 59 + (($serverFile == null) ? 43 : $serverFile.hashCode()); Object $workDirectory = getWorkDirectory(); result = result * 59 + (($workDirectory == null) ? 43 : $workDirectory.hashCode()); Object $cacheDirectory = getCacheDirectory(); result = result * 59 + (($cacheDirectory == null) ? 43 : $cacheDirectory.hashCode()); Object $version = getVersion(); result = result * 59 + (($version == null) ? 43 : $version.hashCode()); Object<String> $urlConnection = (Object<String>)getUrlConnection(); result = result * 59 + (($urlConnection == null) ? 43 : $urlConnection.hashCode()); result = result * 59 + getMaxAttempts(); result = result * 59 + getConnectTimeout(); result = result * 59 + getSocketTimeout(); result = result * 59 + getTimeToLife(); return result * 59 + (isStop() ? 79 : 97); } public String toString() { return "StarterAppConfig(prod=" + isProd() + ", minMemorySize=" + getMinMemorySize() + ", serverFile=" + getServerFile() + ", workDirectory=" + getWorkDirectory() + ", cacheDirectory=" + getCacheDirectory() + ", version=" + getVersion() + ", urlConnection=" + getUrlConnection() + ", maxAttempts=" + getMaxAttempts() + ", connectTimeout=" + getConnectTimeout() + ", socketTimeout=" + getSocketTimeout() + ", timeToLife=" + getTimeToLife() + ", stop=" + isStop() + ")"; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StarterAppConfig()
/*     */   {
/*  48 */     this.prod = true; } public StarterAppConfig(long minMemorySize, List<String> serverFile, String workDirectory, Path cacheDirectory, String version, List<String> urlConnection, int maxAttempts, int connectTimeout, int socketTimeout, int timeToLife, boolean stop) { this.prod = true; this.minMemorySize = minMemorySize; this.serverFile = serverFile; this.workDirectory = workDirectory; this.cacheDirectory = cacheDirectory; this.version = version; this.urlConnection = urlConnection; this.maxAttempts = maxAttempts; this.connectTimeout = connectTimeout; this.socketTimeout = socketTimeout; this.timeToLife = timeToLife; this.stop = stop; } public static final List<String> URI_APP_CONFIG = Lists.newArrayList((Object[])new String[] { "https://repo.tlauncher.org/tlauncher-sources/prod/release/tlauncher", "https://repo.fastrepo.org/tlauncher-sources/prod/release/tlauncher", "http://advancedrepository.com/tlauncher-sources/prod/release/tlauncher" }); private final boolean prod = true; @Parameter(names = {"-memory"}, description = "The size of the required free disk space to download the application") private long minMemorySize; @Parameter(names = {"-uriAppConfig"}, description = "URI of the directory in which appConfig.json is located, which contains all information about the application being launched, this config is used by all applications by default. URI must be specified without version, see version parameter description") private List<String> serverFile; @Parameter(names = {"-workDirectory"}, description = "Working directory where the files required for the application will be loaded and in which the application will be launched. The param used for test. The second way is to put in file with installer.  The file name is installer.properties which contains work.dir=... This is for production. The default method is DesktopUtil.getSystemPath defined with by.gdev.Main. The priority: StarterAppConfig.workDirectory, file installer.properties and default method") private String workDirectory; @Parameter(names = {"-cacheDirectory"}, description = "Directory for storing caching configs") private Path cacheDirectory; public boolean isProd() { getClass(); return true; } @Parameter(names = {"-version"}, description = "Specifies the version of the application to launch. Therefore, the config http://localhost:81/app/1.0/appConfig.json for version 1.0 will be used. This way we can install old versions of the application. For this you need set exactly version.") private String version; @Parameter(names = {"-urlConnection"}, description = "List of sites for checking Internet connection access") private List<String> urlConnection; @Parameter(names = {"-attempts"}, description = "The number of allowed attempts to restore the connection") private int maxAttempts; @Parameter(names = {"-connectTimeout"}, description = "Set connect timeout") private int connectTimeout; @Parameter(names = {"-socketTimeout"}, description = "Set socket timeout")
/*     */   private int socketTimeout; @Parameter(names = {"-timeToLife"}, description = "The time that the file is up-to-date")
/*     */   private int timeToLife; @Parameter(names = {"-stop"}, description = "Argument to stop the application")
/*  51 */   private boolean stop; public long getMinMemorySize() { return this.minMemorySize; }
/*     */    public List<String> getServerFile() {
/*  53 */     return this.serverFile;
/*     */   }
/*     */   
/*     */   public String getWorkDirectory() {
/*  57 */     return this.workDirectory;
/*     */   } public Path getCacheDirectory() {
/*  59 */     return this.cacheDirectory;
/*     */   }
/*     */   public String getVersion() {
/*  62 */     return this.version;
/*     */   } public List<String> getUrlConnection() {
/*  64 */     return this.urlConnection;
/*     */   } public int getMaxAttempts() {
/*  66 */     return this.maxAttempts;
/*     */   } public int getConnectTimeout() {
/*  68 */     return this.connectTimeout;
/*     */   } public int getSocketTimeout() {
/*  70 */     return this.socketTimeout;
/*     */   } public int getTimeToLife() {
/*  72 */     return this.timeToLife;
/*     */   } public boolean isStop() {
/*  74 */     return this.stop;
/*     */   }
/*  76 */   public static final StarterAppConfig DEFAULT_CONFIG = new StarterAppConfig(500L, URI_APP_CONFIG, "starter", 
/*  77 */       Paths.get("starter/cache", new String[0]), null, Arrays.asList(new String[] { "https://repo.tlauncher.org", "https://repo.fastrepo.org", "http://advancedrepository.com", "https://www.google.com", "https://www.baidu.com" }, ), 3, 60000, 60000, 600000, false);
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getServerFileConfig(StarterAppConfig config, String version) {
/*  82 */     return (List<String>)config.getServerFile().stream().map(file -> Objects.isNull(version) ? String.join("/", new CharSequence[] { file, "appConfig.json"
/*     */           }) : String.join("/", new CharSequence[] {
/*     */             file, version, "appConfig.json"
/*  85 */           })).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public List<String> getStarterUpdateConfig() {
/*  89 */     return (List<String>)getServerFile().stream().map(file -> String.join("/", new CharSequence[] { file, "starterUpdate.json"
/*     */           
/*  91 */           })).collect(Collectors.toList());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String workDir(String workDirectory, OSInfo.OSType osType) throws IOException {
/* 100 */     File starterFile = new File(String.join("/", new CharSequence[] { Paths.get(workDirectory, new String[0]).toAbsolutePath().toString(), "starter.json" }));
/* 101 */     if (starterFile.exists()) {
/* 102 */       AppLocalConfig app = (AppLocalConfig)(new FileMapperService(Main.GSON, Main.charset, "")).read(starterFile.toString(), AppLocalConfig.class);
/*     */       
/* 104 */       if (Objects.nonNull(app) && !StringUtils.isEmpty(app.getDir()))
/* 105 */         return Paths.get(app.getDir(), new String[0]).toAbsolutePath().toString(); 
/*     */     } 
/* 107 */     Path installer = Paths.get("installer.properties", new String[0]).toAbsolutePath();
/* 108 */     String dir = "";
/* 109 */     if (Files.exists(installer, new java.nio.file.LinkOption[0])) {
/* 110 */       Properties property = new Properties();
/* 111 */       FileInputStream fis = new FileInputStream(String.valueOf(installer));
/* 112 */       property.load(fis);
/* 113 */       dir = property.getProperty("work.dir");
/*     */     } 
/* 115 */     if (!StringUtils.isEmpty(workDirectory))
/* 116 */       return Paths.get(workDirectory, new String[0]).toAbsolutePath().toString(); 
/* 117 */     if (!StringUtils.isEmpty(dir)) {
/* 118 */       return Paths.get(dir, new String[0]).toAbsolutePath().toString();
/*     */     }
/* 120 */     return DesktopUtil.getSystemPath(osType, "starter").getAbsolutePath().toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\model\StarterAppConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */