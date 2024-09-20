/*    */ package by.gdev.http.download.handler;
/*    */ 
/*    */ import by.gdev.http.download.exeption.HashSumAndSizeError;
/*    */ import by.gdev.http.download.model.Headers;
/*    */ import by.gdev.http.upload.download.downloader.DownloadElement;
/*    */ import by.gdev.util.DesktopUtil;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PostHandlerImpl
/*    */   implements PostHandler
/*    */ {
/*    */   public void postProcessDownloadElement(DownloadElement element) throws NoSuchAlgorithmException, IOException {
/* 30 */     Path localeFile = Paths.get(element.getPathToDownload(), new String[] { element.getMetadata().getPath() });
/* 31 */     String shaLocalFile = DesktopUtil.getChecksum(localeFile.toFile(), Headers.SHA1.getValue());
/* 32 */     long sizeLocalFile = localeFile.toFile().length();
/* 33 */     if (sizeLocalFile != element.getMetadata().getSize() && StringUtils.isEmpty(element.getMetadata().getLink())) {
/* 34 */       element.setError((Throwable)new HashSumAndSizeError(element.getMetadata().getRelativeUrl(), element
/* 35 */             .getPathToDownload() + element.getMetadata().getPath(), String.format("The size should be %s, but was %s", new Object[] {
/* 36 */                 Long.valueOf(element.getMetadata().getSize()), Long.valueOf(sizeLocalFile) })));
/* 37 */       Files.deleteIfExists(localeFile.toAbsolutePath());
/*    */     } 
/*    */     
/* 40 */     if (!shaLocalFile.equals(element.getMetadata().getSha1()) && 
/* 41 */       StringUtils.isEmpty(element.getMetadata().getLink())) {
/* 42 */       element.setError((Throwable)new HashSumAndSizeError((String)element
/* 43 */             .getRepo().getRepositories().get(0) + element.getMetadata().getRelativeUrl(), element
/* 44 */             .getPathToDownload() + element.getMetadata().getPath(), String.format("The hash sum should be %s, but was %s", new Object[] { element
/* 45 */                 .getMetadata().getSha1(), shaLocalFile })));
/* 46 */       Files.deleteIfExists(localeFile.toAbsolutePath());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\http\download\handler\PostHandlerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */