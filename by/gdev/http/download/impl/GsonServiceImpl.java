/*     */ package by.gdev.http.download.impl;
/*     */ 
/*     */ import by.gdev.http.download.service.FileCacheService;
/*     */ import by.gdev.http.download.service.GsonService;
/*     */ import by.gdev.http.download.service.HttpService;
/*     */ import by.gdev.util.model.download.Metadata;
/*     */ import com.google.gson.Gson;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.lang.reflect.Type;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Path;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GsonServiceImpl
/*     */   implements GsonService
/*     */ {
/*  26 */   private static final Logger log = LoggerFactory.getLogger(GsonServiceImpl.class); public GsonServiceImpl(Gson gson, FileCacheService fileService, HttpService httpService) {
/*  27 */     this.gson = gson; this.fileService = fileService; this.httpService = httpService;
/*     */   }
/*     */ 
/*     */   
/*     */   private Gson gson;
/*     */   
/*     */   private FileCacheService fileService;
/*     */   
/*     */   private HttpService httpService;
/*     */ 
/*     */   
/*     */   public <T> T getObject(String url, Class<T> class1, boolean cache) throws IOException, NoSuchAlgorithmException {
/*  39 */     Path pathFile = this.fileService.getRawObject(url, cache);
/*  40 */     try (InputStreamReader read = new InputStreamReader(new FileInputStream(pathFile.toFile()), StandardCharsets.UTF_8)) {
/*     */       
/*  42 */       return (T)this.gson.fromJson(read, class1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getObjectByUrls(List<String> urls, String urn, Class<T> class1, boolean cache) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
/*  52 */     T returnValue = null;
/*  53 */     for (String url : urls) {
/*     */       try {
/*  55 */         Path pathFile = this.fileService.getRawObject(url + urn, cache);
/*  56 */         try (InputStreamReader read = new InputStreamReader(new FileInputStream(pathFile.toFile()), StandardCharsets.UTF_8)) {
/*     */           
/*  58 */           returnValue = (T)this.gson.fromJson(read, class1);
/*     */         } 
/*  60 */       } catch (IOException e) {
/*  61 */         log.error("Error = " + e.getMessage());
/*     */       } 
/*     */     } 
/*  64 */     return returnValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getObjectWithoutSaving(String url, Class<T> class1) throws IOException {
/*  69 */     return getObjectWithoutSaving(url, class1, (Map<String, String>)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getObjectWithoutSaving(String url, Type type) throws IOException {
/*  74 */     return getObjectWithoutSaving(url, type, (Map<String, String>)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getObjectWithoutSaving(String url, Class<T> class1, Map<String, String> headers) throws IOException {
/*  79 */     return (T)this.gson.fromJson(this.httpService.getRequestByUrl(url, headers), class1);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getObjectWithoutSaving(String url, Type type, Map<String, String> headers) throws IOException {
/*  84 */     return (T)this.gson.fromJson(this.httpService.getRequestByUrl(url, headers), type);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getLocalObject(List<String> uris, Class<T> class1) throws IOException, NoSuchAlgorithmException {
/*  89 */     Path pathFile = this.fileService.getRawObject(uris);
/*  90 */     try (InputStreamReader read = new InputStreamReader(new FileInputStream(pathFile.toFile()), StandardCharsets.UTF_8)) {
/*     */       
/*  92 */       return (T)this.gson.fromJson(read, class1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getObjectByUrls(List<String> url, Class<T> class1, boolean cache) throws IOException, NoSuchAlgorithmException {
/*  98 */     Path pathFile = this.fileService.getRawObject(url, cache);
/*  99 */     try (InputStreamReader read = new InputStreamReader(new FileInputStream(pathFile.toFile()), StandardCharsets.UTF_8)) {
/*     */       
/* 101 */       return (T)this.gson.fromJson(read, class1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getObjectByUrls(List<String> urls, List<Metadata> urns, Class<T> class1, boolean cache) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
/* 108 */     for (String url : urls) {
/* 109 */       for (Metadata urn : urns) {
/*     */         try {
/* 111 */           Path pathFile = this.fileService.getRawObject(url + urn.getRelativeUrl(), cache);
/* 112 */           try (InputStreamReader read = new InputStreamReader(new FileInputStream(pathFile.toFile()), StandardCharsets.UTF_8)) {
/*     */             
/* 114 */             return (T)this.gson.fromJson(read, class1);
/*     */           } 
/* 116 */         } catch (IOException e) {
/* 117 */           log.error("Error", e);
/*     */         } 
/*     */       } 
/*     */     } 
/* 121 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\http\download\impl\GsonServiceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */