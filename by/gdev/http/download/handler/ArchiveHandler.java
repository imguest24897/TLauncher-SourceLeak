/*     */ package by.gdev.http.download.handler;
/*     */ 
/*     */ import by.gdev.http.upload.download.downloader.DownloadElement;
/*     */ import by.gdev.http.upload.download.downloader.DownloaderJavaContainer;
/*     */ import by.gdev.util.DesktopUtil;
/*     */ import by.gdev.util.OSInfo;
/*     */ import by.gdev.util.model.download.JvmRepo;
/*     */ import by.gdev.util.model.download.Metadata;
/*     */ import by.gdev.util.model.download.Repo;
/*     */ import by.gdev.utils.service.FileMapperService;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.List;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipInputStream;
/*     */ import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
/*     */ import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
/*     */ import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class ArchiveHandler
/*     */   implements PostHandler
/*     */ {
/*     */   private FileMapperService fileMapperService;
/*  37 */   private static final Logger log = LoggerFactory.getLogger(ArchiveHandler.class); private String jreConfig; public void setFileMapperService(FileMapperService fileMapperService) {
/*  38 */     this.fileMapperService = fileMapperService; } public void setJreConfig(String jreConfig) { this.jreConfig = jreConfig; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof ArchiveHandler)) return false;  ArchiveHandler other = (ArchiveHandler)o; if (!other.canEqual(this)) return false;  Object this$fileMapperService = getFileMapperService(), other$fileMapperService = other.getFileMapperService(); if ((this$fileMapperService == null) ? (other$fileMapperService != null) : !this$fileMapperService.equals(other$fileMapperService)) return false;  Object this$jreConfig = getJreConfig(), other$jreConfig = other.getJreConfig(); return !((this$jreConfig == null) ? (other$jreConfig != null) : !this$jreConfig.equals(other$jreConfig)); } protected boolean canEqual(Object other) { return other instanceof ArchiveHandler; } public int hashCode() { int PRIME = 59; result = 1; Object $fileMapperService = getFileMapperService(); result = result * 59 + (($fileMapperService == null) ? 43 : $fileMapperService.hashCode()); Object $jreConfig = getJreConfig(); return result * 59 + (($jreConfig == null) ? 43 : $jreConfig.hashCode()); } public String toString() { return "ArchiveHandler(fileMapperService=" + getFileMapperService() + ", jreConfig=" + getJreConfig() + ")"; } public ArchiveHandler(FileMapperService fileMapperService, String jreConfig) {
/*  39 */     this.fileMapperService = fileMapperService; this.jreConfig = jreConfig;
/*     */   }
/*     */   
/*  42 */   public FileMapperService getFileMapperService() { return this.fileMapperService; } public String getJreConfig() {
/*  43 */     return this.jreConfig;
/*     */   }
/*     */   
/*     */   public void postProcessDownloadElement(DownloadElement e) throws IOException, NoSuchAlgorithmException {
/*  47 */     Path p = Paths.get(e.getPathToDownload(), new String[] { e.getMetadata().getPath() });
/*     */     
/*  49 */     String jrePath = Paths.get(DownloaderJavaContainer.JRE_DEFAULT, new String[] { ((JvmRepo)e.getRepo()).getJreDirectoryName() }).toString();
/*  50 */     if (String.valueOf(p).endsWith(".zip")) {
/*  51 */       unZip(p.toFile(), new File(e.getPathToDownload()), false, false);
/*     */     } else {
/*  53 */       unTarGz(p.toFile(), new File(e.getPathToDownload()), false, false);
/*  54 */     }  if ((((OSInfo.getOSType() == OSInfo.OSType.LINUX) ? 1 : 0) | ((OSInfo.getOSType() == OSInfo.OSType.MACOSX) ? 1 : 0)) != 0) {
/*  55 */       Files.walk(Paths.get(e.getPathToDownload(), new String[] { jrePath }), new java.nio.file.FileVisitOption[0])
/*  56 */         .filter(f -> (Files.isRegularFile(f, new java.nio.file.LinkOption[0]) && (f.endsWith("java") || f.endsWith("java.exe") || f.endsWith("jspawnhelper") || f.endsWith("jspawnhelper.exe"))))
/*     */         
/*  58 */         .forEach(file -> {
/*     */             try {
/*     */               Files.setPosixFilePermissions(file, DesktopUtil.PERMISSIONS);
/*  61 */             } catch (IOException e1) {
/*     */               log.error("Error with set file permissions ", e1);
/*     */             } 
/*     */           });
/*     */     }
/*  66 */     generateJreConfig(e.getPathToDownload(), jrePath);
/*  67 */     Files.delete(p);
/*     */   }
/*     */ 
/*     */   
/*     */   private void createFile(File file) throws IOException {
/*  72 */     if (file.isFile())
/*     */       return; 
/*  74 */     if (file.getParentFile() != null)
/*  75 */       file.getParentFile().mkdirs(); 
/*  76 */     if (!file.createNewFile()) {
/*  77 */       throw new IOException("Cannot createScrollWrapper file, or it was created during runtime: " + file
/*  78 */           .getAbsolutePath());
/*     */     }
/*     */   }
/*     */   
/*     */   private void unTarGz(File zip, File folder, boolean replace, boolean deleteEmptyFile) throws IOException, NoSuchAlgorithmException {
/*  83 */     try (TarArchiveInputStream zis = new TarArchiveInputStream((InputStream)new GzipCompressorInputStream(new BufferedInputStream(new FileInputStream(zip))))) {
/*     */       TarArchiveEntry ze;
/*     */       
/*  86 */       while ((ze = (TarArchiveEntry)zis.getNextEntry()) != null) {
/*  87 */         String fileName = Paths.get(DownloaderJavaContainer.JRE_DEFAULT, new String[] { ze.getName() }).toString();
/*  88 */         if (ze.isDirectory())
/*     */           continue; 
/*  90 */         unZipAndTarGz(fileName, folder, replace, (InputStream)zis, deleteEmptyFile);
/*     */       } 
/*  92 */       zis.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void unZip(File zip, File folder, boolean replace, boolean deleteEmptyFile) throws IOException, NoSuchAlgorithmException {
/*  98 */     try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zip)), StandardCharsets.UTF_8)) {
/*     */       ZipEntry ze;
/*     */       
/* 101 */       while ((ze = zis.getNextEntry()) != null) {
/* 102 */         String fileName = Paths.get(DownloaderJavaContainer.JRE_DEFAULT, new String[] { ze.getName() }).toString();
/* 103 */         if (ze.isDirectory())
/*     */           continue; 
/* 105 */         unZipAndTarGz(fileName, folder, replace, zis, deleteEmptyFile);
/*     */       } 
/* 107 */       zis.closeEntry();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void unZipAndTarGz(String fileName, File folder, boolean replace, InputStream zis, boolean deleteEmptyFile) throws IOException, NoSuchAlgorithmException {
/* 113 */     byte[] buffer = new byte[1024];
/* 114 */     File newFile = new File(folder, fileName);
/* 115 */     if (!replace && newFile.isFile()) {
/*     */       return;
/*     */     }
/* 118 */     createFile(newFile);
/* 119 */     OutputStream fos = new BufferedOutputStream(new FileOutputStream(newFile));
/*     */     
/* 121 */     int count = 0; int len;
/* 122 */     while ((len = zis.read(buffer)) > 0) {
/* 123 */       count += len;
/* 124 */       fos.write(buffer, 0, len);
/*     */     } 
/* 126 */     fos.close();
/* 127 */     if (deleteEmptyFile && count == 0) {
/* 128 */       Files.delete(newFile.toPath());
/*     */     }
/*     */   }
/*     */   
/*     */   private void generateJreConfig(String path, String jrePath) {
/*     */     try {
/* 134 */       List<Metadata> list = DesktopUtil.generateMetadataForJre(path, jrePath);
/* 135 */       Repo r = new Repo();
/* 136 */       r.setResources(list);
/* 137 */       this.fileMapperService.write(r, Paths.get(jrePath, new String[] { this.jreConfig }).toString());
/* 138 */     } catch (Exception e) {
/* 139 */       log.error("error {}", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\http\download\handler\ArchiveHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */