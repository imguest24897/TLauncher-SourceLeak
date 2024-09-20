/*    */ package by.gdev.subscruber;
/*    */ 
/*    */ import by.gdev.http.upload.download.downloader.DownloadFile;
/*    */ import by.gdev.http.upload.download.downloader.DownloaderStatus;
/*    */ import by.gdev.http.upload.download.downloader.DownloaderStatusEnum;
/*    */ import by.gdev.model.AppLocalConfig;
/*    */ import by.gdev.model.ExceptionMessage;
/*    */ import by.gdev.model.StarterAppConfig;
/*    */ import by.gdev.model.StarterAppProcess;
/*    */ import by.gdev.utils.service.FileMapperService;
/*    */ import com.google.common.eventbus.Subscribe;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Paths;
/*    */ import java.util.Objects;
/*    */ import java.util.ResourceBundle;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ public class ConsoleSubscriber
/*    */ {
/* 23 */   private static final Logger log = LoggerFactory.getLogger(ConsoleSubscriber.class); public ConsoleSubscriber(ResourceBundle bundle, FileMapperService fileMapperService, StarterAppConfig starterConfig) {
/* 24 */     this.bundle = bundle; this.fileMapperService = fileMapperService; this.starterConfig = starterConfig;
/*    */   }
/*    */   private ResourceBundle bundle;
/*    */   private FileMapperService fileMapperService;
/*    */   private StarterAppConfig starterConfig;
/*    */   
/*    */   @Subscribe
/*    */   public void downloadStatusMessage(DownloaderStatus status) {
/* 32 */     if (!status.getDownloaderStatusEnum().equals(DownloaderStatusEnum.IDLE) && 
/* 33 */       status.getLeftFiles().intValue() != 0)
/* 34 */       log.info(String.format(this.bundle.getString("upload.speed"), new Object[] { String.format("%.1f", new Object[] { Double.valueOf(status.getSpeed()) }), status
/* 35 */               .getLeftFiles(), status.getAllFiles(), Long.valueOf(status.getDownloadSize() / 1048576L), 
/* 36 */               Long.valueOf(status.getAllDownloadSize() / 1048576L) })); 
/* 37 */     if (status.getDownloaderStatusEnum().equals(DownloaderStatusEnum.DONE) && 
/* 38 */       status.getThrowables().size() != 0) {
/* 39 */       log.error("error", status.getThrowables().get(0));
/* 40 */       System.exit(-1);
/*    */     } 
/*    */   }
/*    */   
/*    */   @Subscribe
/*    */   private void procces(StarterAppProcess status) {
/* 46 */     checkUnsatisfiedLinkError(status);
/* 47 */     if (Objects.nonNull(status.getErrorCode())) {
/* 48 */       if (status.getErrorCode().intValue() == -1073740791) {
/* 49 */         log.error(this.bundle.getString("driver.error"));
/* 50 */       } else if (status.getErrorCode().intValue() == -1073740771) {
/* 51 */         log.error(this.bundle.getString("msi.afterburner.error"));
/* 52 */       } else if (status.getErrorCode().intValue() != 0) {
/* 53 */         log.error(this.bundle.getString("unidentified.error"));
/* 54 */         System.exit(0);
/*    */       } 
/* 56 */     } else if (status.getLine().contains("starter can be closed")) {
/* 57 */       System.exit(0);
/*    */     } else {
/* 59 */       log.info(status.getLine());
/*    */     } 
/*    */   }
/*    */   @Subscribe
/*    */   public void validateMessage(ExceptionMessage message) {
/* 64 */     log.error(message.printValidationMessage());
/* 65 */     System.exit(-1);
/*    */   }
/*    */   
/*    */   @Subscribe
/*    */   public void downloadedFile(DownloadFile entity) {
/* 70 */     log.info("downloaded file: {} -> {}", entity.getUri(), entity.getFile());
/*    */   }
/*    */   
/*    */   private void checkUnsatisfiedLinkError(StarterAppProcess status) {
/* 74 */     if (!StringUtils.isEmpty(status.getLine()) && status
/* 75 */       .getLine().equals("java.lang.UnsatisfiedLinkError: no zip in java.library.path")) {
/* 76 */       String newWorkDir = "C:\\" + this.starterConfig.getWorkDirectory();
/* 77 */       log.error(String.format(this.bundle.getString("unsatisfied.link.error"), new Object[] {
/* 78 */               Paths.get(this.starterConfig.getWorkDirectory(), new String[0]).toAbsolutePath().toString(), newWorkDir }));
/*    */       try {
/* 80 */         AppLocalConfig appLocalConfig = new AppLocalConfig();
/* 81 */         appLocalConfig = (AppLocalConfig)this.fileMapperService.read("starter.json", AppLocalConfig.class);
/*    */         
/* 83 */         appLocalConfig.setDir(newWorkDir);
/* 84 */         log.info(appLocalConfig.toString());
/* 85 */         this.fileMapperService.write(appLocalConfig, "starter.json");
/* 86 */       } catch (IOException e) {
/* 87 */         log.error("Error ", e);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\subscruber\ConsoleSubscriber.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */