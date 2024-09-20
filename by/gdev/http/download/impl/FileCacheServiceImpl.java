/*     */ package by.gdev.http.download.impl;
/*     */ 
/*     */ import by.gdev.http.download.model.Headers;
/*     */ import by.gdev.http.download.model.RequestMetadata;
/*     */ import by.gdev.http.download.service.FileCacheService;
/*     */ import by.gdev.http.download.service.HttpService;
/*     */ import by.gdev.util.DesktopUtil;
/*     */ import by.gdev.util.model.download.Metadata;
/*     */ import by.gdev.utils.service.FileMapperService;
/*     */ import com.google.gson.Gson;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class FileCacheServiceImpl
/*     */   implements FileCacheService {
/*  25 */   private static final Logger log = LoggerFactory.getLogger(FileCacheServiceImpl.class);
/*     */ 
/*     */   
/*     */   private HttpService httpService;
/*     */ 
/*     */   
/*     */   private Path directory;
/*     */ 
/*     */   
/*     */   private int timeToLife;
/*     */   
/*     */   private FileMapperService fileMapperService;
/*     */ 
/*     */   
/*     */   public FileCacheServiceImpl(HttpService httpService, Gson gson, Charset charset, Path directory, int timeToLife) {
/*  40 */     this.httpService = httpService;
/*  41 */     this.directory = directory;
/*  42 */     this.timeToLife = timeToLife;
/*  43 */     this.fileMapperService = new FileMapperService(gson, charset, "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getRawObject(String url, boolean cache) throws IOException, NoSuchAlgorithmException {
/*  52 */     Path urlPath = Paths.get(this.directory.toString(), new String[] { url.replaceAll("://", "_").replaceAll("[:?=]", "_") });
/*  53 */     Path metaFile = Paths.get(String.valueOf(urlPath).concat(".metadata"), new String[0]);
/*  54 */     if (cache) {
/*  55 */       return getResourceWithoutHttpHead(url, metaFile, urlPath);
/*     */     }
/*  57 */     return getResourceWithHttpHead(url, urlPath, metaFile);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getRawObject(List<String> urls, boolean cache) throws IOException, NoSuchAlgorithmException {
/*  63 */     IOException error = null;
/*  64 */     for (String url : urls) {
/*     */       try {
/*  66 */         Path urlPath = Paths.get(this.directory.toString(), new String[] { url.replaceAll("://", "_").replaceAll("[:?=]", "_") });
/*  67 */         Path metaFile = Paths.get(String.valueOf(urlPath).concat(".metadata"), new String[0]);
/*  68 */         return cache ? getResourceWithoutHttpHead(url, metaFile, urlPath) : 
/*  69 */           getResourceWithHttpHead(url, urlPath, metaFile);
/*  70 */       } catch (IOException e) {
/*  71 */         error = e;
/*     */       } 
/*     */     } 
/*  74 */     throw error;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getRawObject(List<String> urls, Metadata metadata, boolean cache) throws IOException, NoSuchAlgorithmException {
/*  80 */     IOException error = null;
/*  81 */     for (String url : urls) {
/*     */       try {
/*  83 */         return getRawObject(url + metadata.getRelativeUrl(), cache);
/*  84 */       } catch (IOException e) {
/*  85 */         error = e;
/*     */       } 
/*     */     } 
/*  88 */     throw error;
/*     */   }
/*     */ 
/*     */   
/*     */   public Path getRawObject(List<String> urls) throws NoSuchAlgorithmException, IOException {
/*  93 */     IOException error = null;
/*  94 */     for (String url : urls) {
/*     */       
/*     */       try {
/*  97 */         Path urlPath = Paths.get(this.directory.toString(), new String[] { url.replaceAll("://", "_").replaceAll("[:?=]", "_") }).toAbsolutePath();
/*  98 */         Path metaFile = Paths.get(String.valueOf(urlPath).concat(".metadata"), new String[0]).toAbsolutePath();
/*  99 */         if (urlPath.toFile().exists() && Files.exists(metaFile, new java.nio.file.LinkOption[0])) {
/* 100 */           RequestMetadata localMetadata = (RequestMetadata)this.fileMapperService.read(metaFile.toString(), RequestMetadata.class);
/* 101 */           String sha = DesktopUtil.getChecksum(urlPath.toFile(), Headers.SHA1.getValue());
/* 102 */           if (Objects.isNull(localMetadata) || !Objects.equals(localMetadata.getSha1(), sha))
/* 103 */             throw new IOException("sha not equals"); 
/* 104 */           return urlPath;
/*     */         } 
/* 106 */       } catch (IOException e) {
/* 107 */         error = e;
/*     */       } 
/*     */     } 
/* 110 */     throw error;
/*     */   }
/*     */ 
/*     */   
/*     */   private Path getResourceWithoutHttpHead(String url, Path metaFile, Path urlPath) throws IOException, NoSuchAlgorithmException {
/* 115 */     long purgeTime = System.currentTimeMillis() - (this.timeToLife * 1000);
/* 116 */     if (urlPath.toFile().lastModified() < purgeTime)
/* 117 */       Files.deleteIfExists(urlPath); 
/* 118 */     if (urlPath.toFile().exists() && Files.exists(metaFile, new java.nio.file.LinkOption[0])) {
/* 119 */       RequestMetadata localMetadata = (RequestMetadata)this.fileMapperService.read(metaFile.toString(), RequestMetadata.class);
/* 120 */       String sha = DesktopUtil.getChecksum(urlPath.toFile(), Headers.SHA1.getValue());
/* 121 */       if (Objects.nonNull(localMetadata) && Objects.equals(localMetadata.getSha1(), sha)) {
/* 122 */         log.trace("use local file -> " + url);
/* 123 */         return urlPath;
/*     */       } 
/* 125 */       log.trace("not proper hashsum HTTP GET -> " + url);
/* 126 */       generateRequestMetadata(url, urlPath, metaFile);
/* 127 */       return urlPath;
/*     */     } 
/*     */     
/* 130 */     log.trace("HTTP GET -> " + url);
/* 131 */     generateRequestMetadata(url, urlPath, metaFile);
/* 132 */     return urlPath;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Path getResourceWithHttpHead(String url, Path urlPath, Path metaFile) throws IOException, NoSuchAlgorithmException {
/* 138 */     boolean fileExists = urlPath.toFile().exists();
/*     */     try {
/* 140 */       if (fileExists) {
/* 141 */         RequestMetadata serverMetadata = this.httpService.getMetaByUrl(url);
/* 142 */         RequestMetadata localMetadata = (RequestMetadata)this.fileMapperService.read(metaFile.toString(), RequestMetadata.class);
/* 143 */         if (Objects.nonNull(localMetadata) && 
/* 144 */           StringUtils.equals(serverMetadata.getETag(), localMetadata.getETag()) && 
/* 145 */           StringUtils.equals(serverMetadata.getLastModified(), localMetadata.getLastModified()) && 
/* 146 */           StringUtils.equals(DesktopUtil.getChecksum(urlPath.toFile(), "SHA-1"), localMetadata
/* 147 */             .getSha1())) {
/* 148 */           return urlPath;
/*     */         }
/* 150 */         return generateRequestMetadata(url, urlPath, metaFile);
/*     */       } 
/*     */       
/* 153 */       return generateRequestMetadata(url, urlPath, metaFile);
/*     */     }
/* 155 */     catch (Exception e) {
/* 156 */       log.error("error with url " + url);
/* 157 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Path generateRequestMetadata(String url, Path urlPath, Path metaFile) throws IOException, NoSuchAlgorithmException {
/* 163 */     RequestMetadata requestMetadata = this.httpService.getRequestByUrlAndSave(url, urlPath);
/* 164 */     requestMetadata.setSha1(DesktopUtil.getChecksum(urlPath.toFile(), "SHA-1"));
/* 165 */     this.fileMapperService.write(requestMetadata, metaFile.toString());
/* 166 */     return urlPath;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\http\download\impl\FileCacheServiceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */