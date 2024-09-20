/*     */ package by.gdev.http.download.impl;
/*     */ import by.gdev.http.download.exeption.UploadFileException;
/*     */ import by.gdev.http.download.handler.PostHandler;
/*     */ import by.gdev.http.upload.download.downloader.DownloadElement;
/*     */ import by.gdev.http.upload.download.downloader.DownloaderStatusEnum;
/*     */ import com.google.common.eventbus.EventBus;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.time.LocalTime;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Queue;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.methods.CloseableHttpResponse;
/*     */ import org.apache.http.client.methods.HttpGet;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DownloadRunnableImpl implements Runnable {
/*     */   private volatile DownloaderStatusEnum status;
/*     */   private Queue<DownloadElement> downloadElements;
/*     */   private List<DownloadElement> processedElements;
/*     */   private CloseableHttpClient httpclient;
/*     */   private RequestConfig requestConfig;
/*  31 */   private static final Logger log = LoggerFactory.getLogger(DownloadRunnableImpl.class); private EventBus eventBus; public void setStatus(DownloaderStatusEnum status) {
/*  32 */     this.status = status; } public void setDownloadElements(Queue<DownloadElement> downloadElements) { this.downloadElements = downloadElements; } public void setProcessedElements(List<DownloadElement> processedElements) { this.processedElements = processedElements; } public void setHttpclient(CloseableHttpClient httpclient) { this.httpclient = httpclient; } public void setRequestConfig(RequestConfig requestConfig) { this.requestConfig = requestConfig; } public void setEventBus(EventBus eventBus) { this.eventBus = eventBus; } public void setDEFAULT_MAX_ATTEMPTS(int DEFAULT_MAX_ATTEMPTS) { this.DEFAULT_MAX_ATTEMPTS = DEFAULT_MAX_ATTEMPTS; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof DownloadRunnableImpl)) return false;  DownloadRunnableImpl other = (DownloadRunnableImpl)o; if (!other.canEqual(this)) return false;  Object this$status = getStatus(), other$status = other.getStatus(); if ((this$status == null) ? (other$status != null) : !this$status.equals(other$status)) return false;  Object<DownloadElement> this$downloadElements = (Object<DownloadElement>)getDownloadElements(), other$downloadElements = (Object<DownloadElement>)other.getDownloadElements(); if ((this$downloadElements == null) ? (other$downloadElements != null) : !this$downloadElements.equals(other$downloadElements)) return false;  Object<DownloadElement> this$processedElements = (Object<DownloadElement>)getProcessedElements(), other$processedElements = (Object<DownloadElement>)other.getProcessedElements(); if ((this$processedElements == null) ? (other$processedElements != null) : !this$processedElements.equals(other$processedElements)) return false;  Object this$httpclient = getHttpclient(), other$httpclient = other.getHttpclient(); if ((this$httpclient == null) ? (other$httpclient != null) : !this$httpclient.equals(other$httpclient)) return false;  Object this$requestConfig = getRequestConfig(), other$requestConfig = other.getRequestConfig(); if ((this$requestConfig == null) ? (other$requestConfig != null) : !this$requestConfig.equals(other$requestConfig)) return false;  Object this$eventBus = getEventBus(), other$eventBus = other.getEventBus(); return ((this$eventBus == null) ? (other$eventBus != null) : !this$eventBus.equals(other$eventBus)) ? false : (!(getDEFAULT_MAX_ATTEMPTS() != other.getDEFAULT_MAX_ATTEMPTS())); } protected boolean canEqual(Object other) { return other instanceof DownloadRunnableImpl; } public int hashCode() { int PRIME = 59; result = 1; Object $status = getStatus(); result = result * 59 + (($status == null) ? 43 : $status.hashCode()); Object<DownloadElement> $downloadElements = (Object<DownloadElement>)getDownloadElements(); result = result * 59 + (($downloadElements == null) ? 43 : $downloadElements.hashCode()); Object<DownloadElement> $processedElements = (Object<DownloadElement>)getProcessedElements(); result = result * 59 + (($processedElements == null) ? 43 : $processedElements.hashCode()); Object $httpclient = getHttpclient(); result = result * 59 + (($httpclient == null) ? 43 : $httpclient.hashCode()); Object $requestConfig = getRequestConfig(); result = result * 59 + (($requestConfig == null) ? 43 : $requestConfig.hashCode()); Object $eventBus = getEventBus(); result = result * 59 + (($eventBus == null) ? 43 : $eventBus.hashCode()); return result * 59 + getDEFAULT_MAX_ATTEMPTS(); } public String toString() { return "DownloadRunnableImpl(status=" + getStatus() + ", downloadElements=" + getDownloadElements() + ", processedElements=" + getProcessedElements() + ", httpclient=" + getHttpclient() + ", requestConfig=" + getRequestConfig() + ", eventBus=" + getEventBus() + ", DEFAULT_MAX_ATTEMPTS=" + getDEFAULT_MAX_ATTEMPTS() + ")"; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DownloaderStatusEnum getStatus() {
/*  38 */     return this.status;
/*  39 */   } public Queue<DownloadElement> getDownloadElements() { return this.downloadElements; }
/*  40 */   public List<DownloadElement> getProcessedElements() { return this.processedElements; }
/*  41 */   public CloseableHttpClient getHttpclient() { return this.httpclient; }
/*  42 */   public RequestConfig getRequestConfig() { return this.requestConfig; } public EventBus getEventBus() {
/*  43 */     return this.eventBus;
/*  44 */   } private int DEFAULT_MAX_ATTEMPTS = 3; public int getDEFAULT_MAX_ATTEMPTS() { return this.DEFAULT_MAX_ATTEMPTS; }
/*     */ 
/*     */   
/*     */   public DownloadRunnableImpl(Queue<DownloadElement> downloadElements, List<DownloadElement> processedElements, CloseableHttpClient httpclient, RequestConfig requestConfig, EventBus eventBus) {
/*  48 */     this.downloadElements = downloadElements;
/*  49 */     this.processedElements = processedElements;
/*  50 */     this.httpclient = httpclient;
/*  51 */     this.requestConfig = requestConfig;
/*  52 */     this.eventBus = eventBus;
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  57 */     while (this.status.equals(DownloaderStatusEnum.WORK) && 
/*  58 */       !this.status.equals(DownloaderStatusEnum.CANCEL)) {
/*     */ 
/*     */       
/*  61 */       DownloadElement element = this.downloadElements.poll();
/*  62 */       if (Objects.nonNull(element)) {
/*  63 */         for (String repo : element.getRepo().getRepositories()) {
/*     */           try {
/*  65 */             download(element, repo);
/*  66 */             for (PostHandler h : element.getHandlers())
/*  67 */               h.postProcessDownloadElement(element); 
/*     */             break;
/*  69 */           } catch (Throwable e1) {
/*  70 */             element.setError((Throwable)new UploadFileException(repo + element.getMetadata().getRelativeUrl(), element
/*  71 */                   .getMetadata().getPath(), e1.getLocalizedMessage()));
/*     */           } 
/*     */         } 
/*     */         continue;
/*     */       } 
/*     */       break;
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
/*     */ 
/*     */   
/*     */   private void download(DownloadElement element, String repo) throws IOException, InterruptedException {
/*  91 */     this.processedElements.add(element);
/*  92 */     File file = new File(element.getPathToDownload() + element.getMetadata().getPath());
/*  93 */     for (int attempt = 0; attempt < this.DEFAULT_MAX_ATTEMPTS; attempt++) {
/*     */       try {
/*  95 */         BufferedInputStream in = null;
/*  96 */         BufferedOutputStream out = null;
/*  97 */         boolean resume = false;
/*  98 */         String url = repo + element.getMetadata().getRelativeUrl();
/*  99 */         log.trace(url);
/* 100 */         HttpGet httpGet = new HttpGet(url);
/*     */         try {
/* 102 */           if (!file.getParentFile().exists())
/* 103 */             file.getParentFile().mkdirs(); 
/* 104 */           if (file.exists() && Objects.nonNull(element.getMetadata().getSha1()) && file
/* 105 */             .length() < element.getMetadata().getSize()) {
/* 106 */             httpGet.addHeader("Range", "bytes= " + file.length() + "-" + element.getMetadata().getSize());
/* 107 */             resume = true;
/*     */           } 
/* 109 */           httpGet.setConfig(this.requestConfig);
/* 110 */           CloseableHttpResponse response = this.httpclient.execute((HttpUriRequest)httpGet);
/* 111 */           StatusLine sl = response.getStatusLine();
/* 112 */           String responseCode = String.valueOf(sl.getStatusCode());
/* 113 */           if (!responseCode.startsWith("20"))
/* 114 */             throw new IOException(
/* 115 */                 String.format("code %s phrase %s %s", new Object[] { responseCode, sl.getReasonPhrase(), url })); 
/* 116 */           HttpEntity entity = response.getEntity();
/* 117 */           in = new BufferedInputStream(entity.getContent());
/* 118 */           out = new BufferedOutputStream(new FileOutputStream(file, resume));
/*     */           
/* 120 */           byte[] buffer = new byte[1024];
/* 121 */           int curread = in.read(buffer);
/* 122 */           while (curread != -1 && 
/* 123 */             !this.status.equals(DownloaderStatusEnum.CANCEL)) {
/*     */ 
/*     */             
/* 126 */             out.write(buffer, 0, curread);
/* 127 */             curread = in.read(buffer);
/* 128 */             element.setDownloadBytes(element.getDownloadBytes() + curread);
/*     */           } 
/*     */           
/* 131 */           this.eventBus.post(new DownloadFile(url, file.toString()));
/* 132 */           LocalTime endTime = LocalTime.now();
/* 133 */           element.setEnd(endTime);
/*     */           return;
/*     */         } finally {
/* 136 */           httpGet.abort();
/* 137 */           IOUtils.close(out);
/* 138 */           IOUtils.close(in);
/*     */         } 
/* 140 */       } catch (Exception e) {
/* 141 */         if (attempt == this.DEFAULT_MAX_ATTEMPTS)
/* 142 */           throw e; 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\http\download\impl\DownloadRunnableImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */