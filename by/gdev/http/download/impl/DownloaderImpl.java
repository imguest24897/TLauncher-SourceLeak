/*     */ package by.gdev.http.download.impl;
/*     */ 
/*     */ import by.gdev.http.download.exeption.StatusExeption;
/*     */ import by.gdev.http.download.service.Downloader;
/*     */ import by.gdev.http.upload.download.downloader.DownloadElement;
/*     */ import by.gdev.http.upload.download.downloader.DownloaderContainer;
/*     */ import by.gdev.http.upload.download.downloader.DownloaderStatus;
/*     */ import by.gdev.http.upload.download.downloader.DownloaderStatusEnum;
/*     */ import by.gdev.util.model.download.Metadata;
/*     */ import com.google.common.eventbus.EventBus;
/*     */ import java.io.IOException;
/*     */ import java.time.Duration;
/*     */ import java.time.LocalTime;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import org.apache.http.client.config.RequestConfig;
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
/*     */ 
/*     */ 
/*     */ public class DownloaderImpl
/*     */   implements Downloader
/*     */ {
/*     */   private String pathToDownload;
/*     */   private EventBus eventBus;
/*  40 */   private static final Logger log = LoggerFactory.getLogger(DownloaderImpl.class); private CloseableHttpClient httpclient; private RequestConfig requestConfig; public void setPathToDownload(String pathToDownload) {
/*  41 */     this.pathToDownload = pathToDownload; } public void setEventBus(EventBus eventBus) { this.eventBus = eventBus; } public void setHttpclient(CloseableHttpClient httpclient) { this.httpclient = httpclient; } public void setRequestConfig(RequestConfig requestConfig) { this.requestConfig = requestConfig; } public void setDownloadElements(Queue<DownloadElement> downloadElements) { this.downloadElements = downloadElements; } public void setProcessedElements(List<DownloadElement> processedElements) { this.processedElements = processedElements; } public void setAllContainerSize(List<Long> allContainerSize) { this.allContainerSize = allContainerSize; } public void setAllReadyDownloadSize(List<Long> allReadyDownloadSize) { this.allReadyDownloadSize = allReadyDownloadSize; } public void setStatus(DownloaderStatusEnum status) { this.status = status; } public void setRunnable(DownloadRunnableImpl runnable) { this.runnable = runnable; } public void setAllCountElement(Integer allCountElement) { this.allCountElement = allCountElement; } public void setFullDownloadSize(long fullDownloadSize) { this.fullDownloadSize = fullDownloadSize; } public void setDownloadBytesNow(long downloadBytesNow) { this.downloadBytesNow = downloadBytesNow; } public void setStart(LocalTime start) { this.start = start; } public void setSizeDownloadNow(long sizeDownloadNow) { this.sizeDownloadNow = sizeDownloadNow; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof DownloaderImpl)) return false;  DownloaderImpl other = (DownloaderImpl)o; if (!other.canEqual(this)) return false;  Object this$pathToDownload = getPathToDownload(), other$pathToDownload = other.getPathToDownload(); if ((this$pathToDownload == null) ? (other$pathToDownload != null) : !this$pathToDownload.equals(other$pathToDownload)) return false;  Object this$eventBus = getEventBus(), other$eventBus = other.getEventBus(); if ((this$eventBus == null) ? (other$eventBus != null) : !this$eventBus.equals(other$eventBus)) return false;  Object this$httpclient = getHttpclient(), other$httpclient = other.getHttpclient(); if ((this$httpclient == null) ? (other$httpclient != null) : !this$httpclient.equals(other$httpclient)) return false;  Object this$requestConfig = getRequestConfig(), other$requestConfig = other.getRequestConfig(); if ((this$requestConfig == null) ? (other$requestConfig != null) : !this$requestConfig.equals(other$requestConfig)) return false;  Object<DownloadElement> this$downloadElements = (Object<DownloadElement>)getDownloadElements(), other$downloadElements = (Object<DownloadElement>)other.getDownloadElements(); if ((this$downloadElements == null) ? (other$downloadElements != null) : !this$downloadElements.equals(other$downloadElements)) return false;  Object<DownloadElement> this$processedElements = (Object<DownloadElement>)getProcessedElements(), other$processedElements = (Object<DownloadElement>)other.getProcessedElements(); if ((this$processedElements == null) ? (other$processedElements != null) : !this$processedElements.equals(other$processedElements)) return false;  Object<Long> this$allContainerSize = (Object<Long>)getAllContainerSize(), other$allContainerSize = (Object<Long>)other.getAllContainerSize(); if ((this$allContainerSize == null) ? (other$allContainerSize != null) : !this$allContainerSize.equals(other$allContainerSize)) return false;  Object<Long> this$allReadyDownloadSize = (Object<Long>)getAllReadyDownloadSize(), other$allReadyDownloadSize = (Object<Long>)other.getAllReadyDownloadSize(); if ((this$allReadyDownloadSize == null) ? (other$allReadyDownloadSize != null) : !this$allReadyDownloadSize.equals(other$allReadyDownloadSize)) return false;  Object this$status = getStatus(), other$status = other.getStatus(); if ((this$status == null) ? (other$status != null) : !this$status.equals(other$status)) return false;  Object this$runnable = getRunnable(), other$runnable = other.getRunnable(); if ((this$runnable == null) ? (other$runnable != null) : !this$runnable.equals(other$runnable)) return false;  Object this$allCountElement = getAllCountElement(), other$allCountElement = other.getAllCountElement(); if ((this$allCountElement == null) ? (other$allCountElement != null) : !this$allCountElement.equals(other$allCountElement)) return false;  if (getFullDownloadSize() != other.getFullDownloadSize()) return false;  if (getDownloadBytesNow() != other.getDownloadBytesNow()) return false;  Object this$start = getStart(), other$start = other.getStart(); return ((this$start == null) ? (other$start != null) : !this$start.equals(other$start)) ? false : (!(getSizeDownloadNow() != other.getSizeDownloadNow())); } protected boolean canEqual(Object other) { return other instanceof DownloaderImpl; } public int hashCode() { int PRIME = 59; result = 1; Object $pathToDownload = getPathToDownload(); result = result * 59 + (($pathToDownload == null) ? 43 : $pathToDownload.hashCode()); Object $eventBus = getEventBus(); result = result * 59 + (($eventBus == null) ? 43 : $eventBus.hashCode()); Object $httpclient = getHttpclient(); result = result * 59 + (($httpclient == null) ? 43 : $httpclient.hashCode()); Object $requestConfig = getRequestConfig(); result = result * 59 + (($requestConfig == null) ? 43 : $requestConfig.hashCode()); Object<DownloadElement> $downloadElements = (Object<DownloadElement>)getDownloadElements(); result = result * 59 + (($downloadElements == null) ? 43 : $downloadElements.hashCode()); Object<DownloadElement> $processedElements = (Object<DownloadElement>)getProcessedElements(); result = result * 59 + (($processedElements == null) ? 43 : $processedElements.hashCode()); Object<Long> $allContainerSize = (Object<Long>)getAllContainerSize(); result = result * 59 + (($allContainerSize == null) ? 43 : $allContainerSize.hashCode()); Object<Long> $allReadyDownloadSize = (Object<Long>)getAllReadyDownloadSize(); result = result * 59 + (($allReadyDownloadSize == null) ? 43 : $allReadyDownloadSize.hashCode()); Object $status = getStatus(); result = result * 59 + (($status == null) ? 43 : $status.hashCode()); Object $runnable = getRunnable(); result = result * 59 + (($runnable == null) ? 43 : $runnable.hashCode()); Object $allCountElement = getAllCountElement(); result = result * 59 + (($allCountElement == null) ? 43 : $allCountElement.hashCode()); long $fullDownloadSize = getFullDownloadSize(); result = result * 59 + (int)($fullDownloadSize >>> 32L ^ $fullDownloadSize); long $downloadBytesNow = getDownloadBytesNow(); result = result * 59 + (int)($downloadBytesNow >>> 32L ^ $downloadBytesNow); Object $start = getStart(); result = result * 59 + (($start == null) ? 43 : $start.hashCode()); long $sizeDownloadNow = getSizeDownloadNow(); return result * 59 + (int)($sizeDownloadNow >>> 32L ^ $sizeDownloadNow); } public String toString() { return "DownloaderImpl(pathToDownload=" + getPathToDownload() + ", eventBus=" + getEventBus() + ", httpclient=" + getHttpclient() + ", requestConfig=" + getRequestConfig() + ", downloadElements=" + getDownloadElements() + ", processedElements=" + getProcessedElements() + ", allContainerSize=" + getAllContainerSize() + ", allReadyDownloadSize=" + getAllReadyDownloadSize() + ", status=" + getStatus() + ", runnable=" + getRunnable() + ", allCountElement=" + getAllCountElement() + ", fullDownloadSize=" + getFullDownloadSize() + ", downloadBytesNow=" + getDownloadBytesNow() + ", start=" + getStart() + ", sizeDownloadNow=" + getSizeDownloadNow() + ")"; } public DownloaderImpl(String pathToDownload, EventBus eventBus, CloseableHttpClient httpclient, RequestConfig requestConfig, Queue<DownloadElement> downloadElements, List<DownloadElement> processedElements, List<Long> allContainerSize, List<Long> allReadyDownloadSize, DownloaderStatusEnum status, DownloadRunnableImpl runnable, Integer allCountElement, long fullDownloadSize, long downloadBytesNow, LocalTime start, long sizeDownloadNow) {
/*  42 */     this.pathToDownload = pathToDownload; this.eventBus = eventBus; this.httpclient = httpclient; this.requestConfig = requestConfig; this.downloadElements = downloadElements; this.processedElements = processedElements; this.allContainerSize = allContainerSize; this.allReadyDownloadSize = allReadyDownloadSize; this.status = status; this.runnable = runnable; this.allCountElement = allCountElement; this.fullDownloadSize = fullDownloadSize; this.downloadBytesNow = downloadBytesNow; this.start = start; this.sizeDownloadNow = sizeDownloadNow;
/*     */   }
/*     */   
/*     */   public String getPathToDownload()
/*     */   {
/*  47 */     return this.pathToDownload;
/*  48 */   } public EventBus getEventBus() { return this.eventBus; }
/*  49 */   public CloseableHttpClient getHttpclient() { return this.httpclient; } public RequestConfig getRequestConfig() {
/*  50 */     return this.requestConfig;
/*     */   }
/*     */ 
/*     */   
/*  54 */   private Queue<DownloadElement> downloadElements = new ConcurrentLinkedQueue<>(); public Queue<DownloadElement> getDownloadElements() { return this.downloadElements; }
/*     */ 
/*     */ 
/*     */   
/*  58 */   private List<DownloadElement> processedElements = Collections.synchronizedList(new ArrayList<>()); public List<DownloadElement> getProcessedElements() { return this.processedElements; }
/*     */ 
/*     */ 
/*     */   
/*  62 */   private List<Long> allContainerSize = new ArrayList<>(); private volatile DownloaderStatusEnum status; private DownloadRunnableImpl runnable; public List<Long> getAllContainerSize() { return this.allContainerSize; }
/*  63 */    private volatile Integer allCountElement; private long fullDownloadSize; private List<Long> allReadyDownloadSize = new ArrayList<>(); private long downloadBytesNow; private LocalTime start; private long sizeDownloadNow; public List<Long> getAllReadyDownloadSize() { return this.allReadyDownloadSize; }
/*  64 */   public DownloaderStatusEnum getStatus() { return this.status; }
/*  65 */   public DownloadRunnableImpl getRunnable() { return this.runnable; }
/*  66 */   public Integer getAllCountElement() { return this.allCountElement; }
/*  67 */   public long getFullDownloadSize() { return this.fullDownloadSize; }
/*  68 */   public long getDownloadBytesNow() { return this.downloadBytesNow; }
/*  69 */   public LocalTime getStart() { return this.start; } public long getSizeDownloadNow() {
/*  70 */     return this.sizeDownloadNow;
/*     */   }
/*     */   public DownloaderImpl(EventBus eventBus, CloseableHttpClient httpclient, RequestConfig requestConfig) {
/*  73 */     this.eventBus = eventBus;
/*  74 */     this.httpclient = httpclient;
/*  75 */     this.requestConfig = requestConfig;
/*  76 */     this.status = DownloaderStatusEnum.IDLE;
/*  77 */     this.runnable = new DownloadRunnableImpl(this.downloadElements, this.processedElements, httpclient, requestConfig, eventBus);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addContainer(DownloaderContainer container) {
/*  82 */     if (Objects.nonNull(container.getRepo().getResources())) {
/*  83 */       container.getRepo().getResources().forEach(metadata -> {
/*     */             DownloadElement element = new DownloadElement();
/*     */             element.setMetadata(metadata);
/*     */             element.setRepo(container.getRepo());
/*     */             element.setPathToDownload(container.getDestinationRepositories());
/*     */             element.setHandlers(container.getHandlers());
/*     */             this.downloadElements.add(element);
/*     */           });
/*     */     }
/*  92 */     this.pathToDownload = container.getDestinationRepositories();
/*  93 */     this.allContainerSize.add(Long.valueOf(container.getContainerSize()));
/*  94 */     this.allReadyDownloadSize.add(Long.valueOf(container.getReadyDownloadSize()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startDownload(boolean sync) throws InterruptedException, ExecutionException, StatusExeption, IOException {
/* 100 */     this.fullDownloadSize = ((Long)this.allContainerSize.stream().reduce(Long::sum).orElse(Long.valueOf(0L))).longValue();
/* 101 */     this.sizeDownloadNow = ((Long)this.allReadyDownloadSize.stream().reduce(Long::sum).orElse(Long.valueOf(0L))).longValue();
/* 102 */     this.start = LocalTime.now();
/* 103 */     if (this.status.equals(DownloaderStatusEnum.IDLE)) {
/* 104 */       this.status = DownloaderStatusEnum.WORK;
/* 105 */       this.runnable.setStatus(this.status);
/* 106 */       this.allCountElement = Integer.valueOf(this.downloadElements.size());
/* 107 */       List<CompletableFuture<Void>> listThread = new ArrayList<>();
/* 108 */       for (int i = 0; i < 3; i++)
/* 109 */         listThread.add(CompletableFuture.runAsync(this.runnable)); 
/* 110 */       if (sync) {
/* 111 */         waitThreadDone(listThread);
/*     */       } else {
/*     */         
/* 114 */         CompletableFuture.runAsync(() -> {
/*     */               try {
/*     */                 waitThreadDone(listThread);
/* 117 */               } catch (InterruptedException e) {
/*     */                 log.error("Error", e);
/*     */               } 
/* 120 */             }).get();
/*     */       } 
/*     */     } else {
/* 123 */       throw new StatusExeption(this.status.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancelDownload() {
/* 132 */     this.status = DownloaderStatusEnum.CANCEL;
/* 133 */     this.runnable.setStatus(DownloaderStatusEnum.CANCEL);
/*     */   }
/*     */   
/*     */   private DownloaderStatus buildDownloaderStatus() {
/* 137 */     DownloaderStatus statusDownload = new DownloaderStatus();
/* 138 */     long downloadBytesNow = 0L;
/* 139 */     List<DownloadElement> list = new ArrayList<>(this.processedElements);
/* 140 */     List<Throwable> errorList = new ArrayList<>();
/* 141 */     double thirty = Duration.between(this.start, LocalTime.now()).getSeconds();
/* 142 */     for (DownloadElement elem : list) {
/* 143 */       downloadBytesNow += elem.getDownloadBytes();
/* 144 */       if (Objects.nonNull(elem.getError()))
/* 145 */         errorList.add(elem.getError()); 
/*     */     } 
/* 147 */     statusDownload.setThrowables(errorList);
/* 148 */     statusDownload.setDownloadSize(this.sizeDownloadNow + downloadBytesNow);
/* 149 */     statusDownload.setSpeed((downloadBytesNow / 1048576L) / thirty);
/* 150 */     statusDownload.setDownloaderStatusEnum(this.status);
/* 151 */     statusDownload.setAllDownloadSize(this.fullDownloadSize);
/* 152 */     statusDownload.setLeftFiles(Integer.valueOf(this.processedElements.size()));
/* 153 */     statusDownload.setAllFiles(this.allCountElement);
/* 154 */     return statusDownload;
/*     */   }
/*     */   
/*     */   private void waitThreadDone(List<CompletableFuture<Void>> listThread) throws InterruptedException {
/* 158 */     LocalTime start = LocalTime.now();
/* 159 */     boolean workedAnyThread = true;
/* 160 */     while (workedAnyThread) {
/* 161 */       workedAnyThread = false;
/* 162 */       Thread.sleep(50L);
/* 163 */       workedAnyThread = listThread.stream().anyMatch(e -> !e.isDone());
/* 164 */       if (start.isBefore(LocalTime.now())) {
/* 165 */         start = start.plusSeconds(1L);
/* 166 */         if (this.allCountElement.intValue() != 0 && 
/* 167 */           start.getSecond() != start.plusSeconds(1L).getSecond()) {
/* 168 */           this.eventBus.post(buildDownloaderStatus());
/*     */         }
/*     */       } 
/*     */     } 
/* 172 */     this.status = DownloaderStatusEnum.DONE;
/* 173 */     this.eventBus.post(buildDownloaderStatus());
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\http\download\impl\DownloaderImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */