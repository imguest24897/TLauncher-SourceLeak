/*     */ package by.gdev.handler;
/*     */ 
/*     */ import by.gdev.http.download.service.GsonService;
/*     */ import by.gdev.model.StarterUpdate;
/*     */ import by.gdev.ui.JLabelHtmlWrapper;
/*     */ import by.gdev.util.DesktopUtil;
/*     */ import by.gdev.util.OSInfo;
/*     */ import com.google.common.reflect.TypeToken;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.Type;
/*     */ import java.net.URLDecoder;
/*     */ import java.nio.file.Files;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.methods.CloseableHttpResponse;
/*     */ import org.apache.http.client.methods.HttpGet;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class UpdateCore {
/*     */   public UpdateCore(ResourceBundle bundle, GsonService gsonService, CloseableHttpClient httpclient, RequestConfig requestConfig) {
/*  39 */     this.bundle = bundle; this.gsonService = gsonService; this.httpclient = httpclient; this.requestConfig = requestConfig;
/*  40 */   } private static final Logger log = LoggerFactory.getLogger(UpdateCore.class);
/*     */   
/*     */   private ResourceBundle bundle;
/*     */   
/*     */   private GsonService gsonService;
/*     */   
/*     */   private CloseableHttpClient httpclient;
/*     */   
/*     */   private RequestConfig requestConfig;
/*     */   
/*     */   public void checkUpdates(OSInfo.OSType osType, List<String> updateConfigUri) throws IOException, NoSuchAlgorithmException, ClassNotFoundException {
/*  51 */     Map<String, StarterUpdate> map = getUpdateFile(updateConfigUri);
/*     */     
/*  53 */     File jarFile = new File(URLDecoder.decode(FileUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8"));
/*  54 */     String key = osType.name() + (jarFile.toString().endsWith("exe") ? "exe" : "");
/*  55 */     if (map == null || !map.containsKey(key))
/*     */       return; 
/*  57 */     StarterUpdate update = map.get(key);
/*  58 */     String localeSha1 = DesktopUtil.getChecksum(jarFile, "SHA-1");
/*  59 */     File temp = new File(jarFile.toString() + ".temp");
/*  60 */     if (!update.getSha1().equals(localeSha1)) {
/*  61 */       BufferedInputStream in = null;
/*  62 */       BufferedOutputStream out = null;
/*  63 */       HttpGet httpGet = new HttpGet(update.getUri());
/*     */       try {
/*  65 */         httpGet.setConfig(this.requestConfig);
/*  66 */         CloseableHttpResponse response = this.httpclient.execute((HttpUriRequest)httpGet);
/*  67 */         HttpEntity entity = response.getEntity();
/*  68 */         in = new BufferedInputStream(entity.getContent());
/*  69 */         out = new BufferedOutputStream(new FileOutputStream(temp));
/*  70 */         byte[] buffer = new byte[1024];
/*  71 */         int curread = in.read(buffer);
/*  72 */         while (curread != -1) {
/*  73 */           out.write(buffer, 0, curread);
/*  74 */           curread = in.read(buffer);
/*     */         } 
/*     */       } finally {
/*  77 */         httpGet.abort();
/*  78 */         IOUtils.close(out);
/*  79 */         IOUtils.close(in);
/*     */       } 
/*  81 */       JLabelHtmlWrapper label = new JLabelHtmlWrapper(this.bundle.getString("update.message"));
/*  82 */       JOptionPane.showMessageDialog(new JFrame(), label, "", 1);
/*  83 */       log.info("from {} to {}", temp.toPath().toString(), jarFile.toPath().toString());
/*  84 */       try (OutputStream outputStream = new FileOutputStream(jarFile)) {
/*  85 */         IOUtils.copy(new FileInputStream(temp), outputStream);
/*     */       } 
/*  87 */       System.exit(0);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, StarterUpdate> getUpdateFile(List<String> updateConfigUri) throws IOException {
/*  94 */     Type mapType = (new TypeToken<Map<String, StarterUpdate>>() { private static final long serialVersionUID = 1L; }).getType();
/*  95 */     Iterator<String> iterator = updateConfigUri.iterator(); if (iterator.hasNext()) { String uri = iterator.next();
/*  96 */       return (Map<String, StarterUpdate>)this.gsonService.getObjectWithoutSaving(uri, mapType); }
/*     */     
/*  98 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void deleteTmpFileIfExist() {
/*     */     try {
/* 104 */       File jarFile = new File(URLDecoder.decode(FileUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8"));
/* 105 */       Files.deleteIfExists((new File(jarFile.toString() + ".temp")).toPath());
/* 106 */     } catch (Throwable e) {
/* 107 */       log.error("error", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\handler\UpdateCore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */