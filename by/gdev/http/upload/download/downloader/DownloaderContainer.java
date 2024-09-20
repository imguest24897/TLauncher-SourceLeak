/*    */ package by.gdev.http.upload.download.downloader;
/*    */ 
/*    */ import by.gdev.http.download.handler.PostHandler;
/*    */ import by.gdev.http.download.model.Headers;
/*    */ import by.gdev.util.DesktopUtil;
/*    */ import by.gdev.util.model.download.Metadata;
/*    */ import by.gdev.util.model.download.Repo;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.LinkOption;
/*    */ import java.nio.file.Paths;
/*    */ import java.nio.file.attribute.BasicFileAttributes;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DownloaderContainer
/*    */ {
/*    */   public void setDestinationRepositories(String destinationRepositories) {
/* 30 */     this.destinationRepositories = destinationRepositories; } public void setContainerSize(long containerSize) { this.containerSize = containerSize; } public void setReadyDownloadSize(long readyDownloadSize) { this.readyDownloadSize = readyDownloadSize; } public void setRepo(Repo repo) { this.repo = repo; } public void setHandlers(List<PostHandler> handlers) { this.handlers = handlers; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof DownloaderContainer)) return false;  DownloaderContainer other = (DownloaderContainer)o; if (!other.canEqual(this)) return false;  Object this$destinationRepositories = getDestinationRepositories(), other$destinationRepositories = other.getDestinationRepositories(); if ((this$destinationRepositories == null) ? (other$destinationRepositories != null) : !this$destinationRepositories.equals(other$destinationRepositories)) return false;  if (getContainerSize() != other.getContainerSize()) return false;  if (getReadyDownloadSize() != other.getReadyDownloadSize()) return false;  Object this$repo = getRepo(), other$repo = other.getRepo(); if ((this$repo == null) ? (other$repo != null) : !this$repo.equals(other$repo)) return false;  Object<PostHandler> this$handlers = (Object<PostHandler>)getHandlers(), other$handlers = (Object<PostHandler>)other.getHandlers(); return !((this$handlers == null) ? (other$handlers != null) : !this$handlers.equals(other$handlers)); } protected boolean canEqual(Object other) { return other instanceof DownloaderContainer; } public int hashCode() { int PRIME = 59; result = 1; Object $destinationRepositories = getDestinationRepositories(); result = result * 59 + (($destinationRepositories == null) ? 43 : $destinationRepositories.hashCode()); long $containerSize = getContainerSize(); result = result * 59 + (int)($containerSize >>> 32L ^ $containerSize); long $readyDownloadSize = getReadyDownloadSize(); result = result * 59 + (int)($readyDownloadSize >>> 32L ^ $readyDownloadSize); Object $repo = getRepo(); result = result * 59 + (($repo == null) ? 43 : $repo.hashCode()); Object<PostHandler> $handlers = (Object<PostHandler>)getHandlers(); return result * 59 + (($handlers == null) ? 43 : $handlers.hashCode()); } public String toString() { return "DownloaderContainer(destinationRepositories=" + getDestinationRepositories() + ", containerSize=" + getContainerSize() + ", readyDownloadSize=" + getReadyDownloadSize() + ", repo=" + getRepo() + ", handlers=" + getHandlers() + ")"; }
/* 31 */    String destinationRepositories; long containerSize; long readyDownloadSize; private static final Logger log = LoggerFactory.getLogger(DownloaderContainer.class); Repo repo; List<PostHandler> handlers;
/*    */   public String getDestinationRepositories() {
/* 33 */     return this.destinationRepositories; }
/* 34 */   public long getContainerSize() { return this.containerSize; }
/* 35 */   public long getReadyDownloadSize() { return this.readyDownloadSize; }
/* 36 */   public Repo getRepo() { return this.repo; } public List<PostHandler> getHandlers() {
/* 37 */     return this.handlers;
/*    */   }
/*    */   
/*    */   public void filterNotExistResoursesAndSetRepo(Repo repo, String workDirectory) throws NoSuchAlgorithmException, IOException {
/* 41 */     this.repo = new Repo();
/* 42 */     List<Metadata> listRes = new ArrayList<>();
/* 43 */     for (Metadata meta : repo.getResources()) {
/* 44 */       File localFile = Paths.get(workDirectory, new String[] { meta.getPath() }).toAbsolutePath().toFile();
/* 45 */       if (localFile.exists()) {
/* 46 */         String shaLocalFile = DesktopUtil.getChecksum(localFile, Headers.SHA1.getValue());
/* 47 */         BasicFileAttributes attr = Files.readAttributes(localFile.toPath(), BasicFileAttributes.class, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
/*    */         
/* 49 */         if (((!attr.isSymbolicLink() ? 1 : 0) & (!shaLocalFile.equals(meta.getSha1()) ? 1 : 0)) != 0) {
/* 50 */           listRes.add(meta);
/* 51 */           log.warn("The hash sum of the file is not equal. File " + localFile + " will be deleted. Size = " + (localFile
/* 52 */               .length() / 1024L / 1024L));
/* 53 */           Files.delete(localFile.toPath());
/*    */         }  continue;
/*    */       } 
/* 56 */       listRes.add(meta);
/*    */     } 
/*    */     
/* 59 */     this.repo.setResources(listRes);
/* 60 */     this.repo.setRepositories(repo.getRepositories());
/*    */   }
/*    */   
/*    */   public void containerAllSize(Repo repo) {
/* 64 */     this.containerSize = ((Long)repo.getResources().stream().map(Metadata::getSize).reduce(Long::sum).orElse(Long.valueOf(0L))).longValue();
/*    */   }
/*    */   
/*    */   public void downloadSize(Repo repo, String workDirectory) {
/* 68 */     this
/*    */       
/* 70 */       .readyDownloadSize = ((Long)repo.getResources().stream().map(e -> Long.valueOf(Paths.get(workDirectory, new String[] { e.getPath() }).toFile().length())).reduce(Long::sum).orElse(Long.valueOf(0L))).longValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\htt\\upload\download\downloader\DownloaderContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */