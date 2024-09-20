/*     */ package by.gdev.util;
/*     */ 
/*     */ import by.gdev.util.model.download.Metadata;
/*     */ import java.awt.Desktop;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.FileLock;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.nio.file.attribute.PosixFilePermission;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipInputStream;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
/*     */ import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
/*     */ import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.methods.HttpHead;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DesktopUtil
/*     */ {
/*  53 */   private static final Logger log = LoggerFactory.getLogger(DesktopUtil.class);
/*     */   
/*     */   private static final String PROTECTION = "protection.txt";
/*     */   
/*     */   private static FileLock lock;
/*  58 */   public static Set<PosixFilePermission> PERMISSIONS = new HashSet<PosixFilePermission>()
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static File getSystemPath(OSInfo.OSType type, String path) {
/*  83 */     String applicationData, folder, userHome = System.getProperty("user.home", ".");
/*     */     
/*  85 */     switch (type)
/*     */     { case LINUX:
/*     */       case SOLARIS:
/*  88 */         file = new File(userHome, path);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 101 */         return file;case WINDOWS: applicationData = System.getenv("APPDATA"); folder = (applicationData != null) ? applicationData : userHome; file = new File(folder, path); return file;case MACOSX: file = new File(userHome, "Library/Application Support/" + path); return file; }  File file = new File(userHome, path); return file;
/*     */   }
/*     */   
/*     */   public static String getChecksum(byte[] array, String algorithm) throws IOException, NoSuchAlgorithmException {
/* 105 */     return createChecksum(array, algorithm);
/*     */   }
/*     */   
/*     */   public static String getChecksum(File file, String algorithm) throws IOException, NoSuchAlgorithmException {
/* 109 */     return createChecksum(Files.readAllBytes(file.toPath()), algorithm);
/*     */   }
/*     */   
/*     */   private static String createChecksum(byte[] array, String algorithm) throws NoSuchAlgorithmException {
/* 113 */     MessageDigest complete = MessageDigest.getInstance(algorithm);
/* 114 */     complete.update(array);
/* 115 */     byte[] b = complete.digest();
/* 116 */     StringBuilder result = new StringBuilder();
/* 117 */     for (byte cb : b)
/* 118 */       result.append(Integer.toString((cb & 0xFF) + 256, 16).substring(1)); 
/* 119 */     return result.toString();
/*     */   }
/*     */   
/*     */   public static String getJavaPathByHome(boolean appendBinFolder) {
/* 123 */     String path = System.getProperty("java.home");
/* 124 */     if (appendBinFolder) {
/* 125 */       path = appendToJVM(path);
/*     */     }
/* 127 */     return path;
/*     */   }
/*     */   
/*     */   public static String appendToJVM(String path) {
/* 131 */     char separator = File.separatorChar;
/* 132 */     StringBuilder b = new StringBuilder(path);
/* 133 */     b.append(separator);
/* 134 */     b.append("bin").append(separator).append("java");
/* 135 */     if (OSInfo.getOSType().equals(OSInfo.OSType.WINDOWS))
/* 136 */       b.append("w.exe"); 
/* 137 */     return b.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T uncheckCall(Callable<T> callable) {
/*     */     try {
/* 149 */       return callable.call();
/* 150 */     } catch (Exception e) {
/* 151 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T, R> Function<T, R> wrap(CheckedFunction<T, R> checkedFunction) {
/* 159 */     return t -> {
/*     */         try {
/*     */           return checkedFunction.apply(t);
/* 162 */         } catch (Exception e) {
/*     */           throw new RuntimeException(e);
/*     */         } 
/*     */       };
/*     */   }
/*     */   
/*     */   public static void sleep(int milliSeconds) {
/*     */     try {
/* 170 */       Thread.sleep(milliSeconds);
/* 171 */     } catch (InterruptedException e) {
/* 172 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static int numberOfAttempts(List<String> urls, int maxAttepmts, RequestConfig requestConfig, CloseableHttpClient httpclient) {
/* 178 */     int attempt = 1;
/*     */     
/* 180 */     for (String url : urls) {
/*     */       try {
/* 182 */         HttpHead http = new HttpHead(url);
/* 183 */         http.setConfig(requestConfig);
/* 184 */         httpclient.execute((HttpUriRequest)http);
/* 185 */         return maxAttepmts;
/*     */       }
/* 187 */       catch (IOException e) {
/* 188 */         log.info("error during test net {} {}", url, e.getMessage());
/*     */       } 
/*     */     } 
/* 191 */     return attempt;
/*     */   }
/*     */   
/*     */   private static void createDirectory(File file) throws IOException {
/* 195 */     if (file.isFile())
/*     */       return; 
/* 197 */     if (file.getParentFile() != null)
/* 198 */       file.getParentFile().mkdirs(); 
/*     */   }
/*     */   
/*     */   public static void diactivateDoubleDownloadingResourcesLock() throws IOException {
/* 202 */     if (Objects.nonNull(lock)) {
/* 203 */       lock.release();
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
/*     */   public static String convertListToString(String del, List<Path> list) {
/* 216 */     StringBuilder b = new StringBuilder();
/* 217 */     for (Path string : list) {
/* 218 */       b.append(string).append(del);
/*     */     }
/* 220 */     return b.toString();
/*     */   }
/*     */   
/*     */   public static void activeDoubleDownloadingResourcesLock(String container) throws IOException {
/* 224 */     File f = new File(container, "protection.txt");
/* 225 */     createDirectory(f);
/* 226 */     if (f.exists()) {
/* 227 */       FileChannel ch = FileChannel.open(f.toPath(), new OpenOption[] { StandardOpenOption.WRITE, StandardOpenOption.CREATE });
/* 228 */       lock = ch.tryLock();
/* 229 */       if (Objects.isNull(lock)) {
/* 230 */         log.warn("Lock could not be acquired ");
/* 231 */         System.exit(4);
/*     */       } 
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
/*     */   public static Path getJavaRun(Path java) throws IOException {
/* 244 */     return (Path)Files.walk(java, new java.nio.file.FileVisitOption[0]).filter(e -> (Files.isRegularFile(e, new java.nio.file.LinkOption[0]) && (e.endsWith("java") || e.endsWith("java.exe"))))
/* 245 */       .findAny().orElseThrow(() -> new RuntimeException("java executable not found "));
/*     */   }
/*     */   
/*     */   public static String appendBootstrapperJvm2(String path) {
/* 249 */     StringBuilder b = new StringBuilder();
/* 250 */     if (OSInfo.getOSType() == OSInfo.OSType.MACOSX) {
/* 251 */       b.append("Contents").append(File.separatorChar).append("Home").append(File.separatorChar);
/*     */     }
/* 253 */     return appendToJVM((new File(b.toString())).getPath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void openLink(OSInfo.OSType type, String uri) {
/*     */     try {
/* 265 */       Desktop.getDesktop().browse(new URI(uri));
/* 266 */     } catch (IOException|java.net.URISyntaxException e) {
/* 267 */       log.warn("can't open link", e);
/* 268 */       if (type.equals(OSInfo.OSType.LINUX)) {
/*     */         try {
/* 270 */           Runtime.getRuntime().exec("gnome-open " + uri);
/* 271 */         } catch (IOException e1) {
/* 272 */           log.warn("can't open link for linix", e);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void initLookAndFeel() {
/* 281 */     LookAndFeel defaultLookAndFeel = null;
/*     */     try {
/* 283 */       defaultLookAndFeel = UIManager.getLookAndFeel();
/* 284 */       UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
/* 285 */       new JFileChooser();
/* 286 */     } catch (Throwable t) {
/* 287 */       log.warn("problem with ", t);
/* 288 */       if (Objects.nonNull(defaultLookAndFeel))
/*     */         try {
/* 290 */           UIManager.setLookAndFeel(defaultLookAndFeel);
/* 291 */         } catch (Throwable e) {
/* 292 */           log.warn("coudn't set defualt look and feel", e);
/*     */         }  
/*     */     } 
/*     */   }
/*     */   
/*     */   public static List<String> generatePath(List<String> repositories, List<Metadata> resources) {
/* 298 */     return (List<String>)repositories.stream().map(repo -> (List)resources.stream().map(()).collect(Collectors.toList()))
/*     */ 
/*     */       
/* 301 */       .flatMap(Collection::stream).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public static List<Metadata> generateMetadataForJre(String path, String jrePath) throws IOException {
/* 305 */     return (List<Metadata>)Files.walk(Paths.get(path, new String[] { jrePath }), new java.nio.file.FileVisitOption[0]).filter(x$0 -> Files.isRegularFile(x$0, new java.nio.file.LinkOption[0]))
/* 306 */       .filter(pr -> !pr.getFileName().toString().endsWith(".json")).map(wrap(p -> {
/*     */             Metadata m = new Metadata();
/*     */             m.setSha1(getChecksum(p.toFile(), "SHA-1"));
/*     */             String relativize = Paths.get(path, new String[0]).relativize(p).toString();
/*     */             m.setPath(relativize.replace("\\", "/"));
/*     */             m.setSize(p.toFile().length());
/*     */             return m;
/* 313 */           })).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public static String getRootFolderZip(File zip) {
/*     */     try {
/* 318 */       if (zip.getName().endsWith(".tar.gz"))
/* 319 */         try (TarArchiveInputStream zis = new TarArchiveInputStream((InputStream)new GzipCompressorInputStream(new BufferedInputStream(new FileInputStream(zip))))) {
/*     */           
/* 321 */           TarArchiveEntry ze = zis.getNextTarEntry();
/* 322 */           return ze.getName();
/*     */         }  
/* 324 */       if (zip.getName().endsWith(".zip")) {
/* 325 */         try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zip)), StandardCharsets.UTF_8)) {
/*     */           
/* 327 */           ZipEntry ze = zis.getNextEntry();
/* 328 */           return ze.getName();
/*     */         } 
/*     */       }
/* 331 */       return "";
/*     */     } catch (Throwable $ex) {
/*     */       throw $ex;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gde\\util\DesktopUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */