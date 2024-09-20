/*    */ package by.gdev.http.download.handler;
/*    */ 
/*    */ import by.gdev.http.upload.download.downloader.DownloadElement;
/*    */ import by.gdev.util.DesktopUtil;
/*    */ import by.gdev.util.OSInfo;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Paths;
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
/*    */ public class AccessHandler
/*    */   implements PostHandler
/*    */ {
/*    */   public void postProcessDownloadElement(DownloadElement e) throws IOException {
/* 23 */     if (e.getMetadata().isExecutable())
/* 24 */       if ((((OSInfo.getOSType() == OSInfo.OSType.LINUX) ? 1 : 0) | ((OSInfo.getOSType() == OSInfo.OSType.MACOSX) ? 1 : 0)) != 0)
/* 25 */         Files.setPosixFilePermissions(Paths.get(e.getPathToDownload() + e.getMetadata().getPath(), new String[0]), DesktopUtil.PERMISSIONS);  
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\http\download\handler\AccessHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */