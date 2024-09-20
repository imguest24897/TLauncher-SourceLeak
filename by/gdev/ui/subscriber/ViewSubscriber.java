/*    */ package by.gdev.ui.subscriber;
/*    */ 
/*    */ import by.gdev.http.download.exeption.HashSumAndSizeError;
/*    */ import by.gdev.http.download.exeption.UploadFileException;
/*    */ import by.gdev.http.upload.download.downloader.DownloaderStatus;
/*    */ import by.gdev.http.upload.download.downloader.DownloaderStatusEnum;
/*    */ import by.gdev.model.ExceptionMessage;
/*    */ import by.gdev.model.StarterAppConfig;
/*    */ import by.gdev.model.StarterAppProcess;
/*    */ import by.gdev.ui.JLabelHtmlWrapper;
/*    */ import by.gdev.updater.ProgressFrame;
/*    */ import by.gdev.util.DesktopUtil;
/*    */ import by.gdev.util.OSInfo;
/*    */ import com.google.common.eventbus.Subscribe;
/*    */ import java.awt.Component;
/*    */ import java.awt.event.MouseAdapter;
/*    */ import java.awt.event.MouseEvent;
/*    */ import java.nio.file.Paths;
/*    */ import java.util.Objects;
/*    */ import java.util.ResourceBundle;
/*    */ import javax.swing.JOptionPane;
/*    */ import javax.swing.SwingUtilities;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.apache.commons.lang3.exception.ExceptionUtils;
/*    */ 
/*    */ public class ViewSubscriber {
/*    */   private ProgressFrame frame;
/*    */   
/*    */   public ViewSubscriber(ProgressFrame frame, ResourceBundle bundle, OSInfo.OSType osType, StarterAppConfig starterConfig) {
/* 30 */     this.frame = frame; this.bundle = bundle; this.osType = osType; this.starterConfig = starterConfig;
/*    */   }
/*    */ 
/*    */   
/*    */   private ResourceBundle bundle;
/*    */   private OSInfo.OSType osType;
/*    */   private StarterAppConfig starterConfig;
/*    */   
/*    */   @Subscribe
/*    */   private void procces(StarterAppProcess status) {
/* 40 */     if (!StringUtils.isEmpty(status.getLine()) && status
/* 41 */       .getLine().equals("java.lang.UnsatisfiedLinkError: no zip in java.library.path"))
/* 42 */       message(new ExceptionMessage(String.format(this.bundle.getString("unsatisfied.link.error"), new Object[] {
/* 43 */                 Paths.get(this.starterConfig.getWorkDirectory(), new String[0]).toAbsolutePath().toString(), "C:\\" + this.starterConfig
/* 44 */                 .getWorkDirectory()
/*    */               }))); 
/* 46 */     if (Objects.nonNull(status.getErrorCode())) {
/* 47 */       if (status.getErrorCode().intValue() == -1073740791) {
/* 48 */         message(new ExceptionMessage(this.bundle.getString("driver.error"), 
/* 49 */               String.format("https://tlauncher.org/%s/closed-minecraft-1073740791.html", new Object[] { this.bundle.getLocale().getLanguage() })));
/* 50 */       } else if (status.getErrorCode().intValue() == -1073740771) {
/* 51 */         message(new ExceptionMessage(this.bundle.getString("msi.afterburner.error")));
/* 52 */       } else if (status.getErrorCode().intValue() != 0) {
/* 53 */         message(new ExceptionMessage(this.bundle.getString("unidentified.error")));
/* 54 */         System.exit(0);
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   @Subscribe
/*    */   public void message(DownloaderStatus status) {
/* 61 */     if (DownloaderStatusEnum.DONE.equals(status.getDownloaderStatusEnum()) && 
/* 62 */       !status.getThrowables().isEmpty()) {
/* 63 */       Throwable t = status.getThrowables().get(0);
/* 64 */       if (t instanceof HashSumAndSizeError) {
/* 65 */         HashSumAndSizeError t1 = (HashSumAndSizeError)t;
/* 66 */         String s = String.format(this.bundle.getString("upload.error.hash.sum"), new Object[] { t1.getUri(), t1.getMessage() });
/* 67 */         message(new ExceptionMessage(s, t1.getUri()));
/* 68 */       } else if (t instanceof UploadFileException) {
/* 69 */         UploadFileException t1 = (UploadFileException)t;
/* 70 */         String s = String.format(this.bundle.getString("upload.error"), new Object[] { t1.getUri(), t1.getMessage() });
/* 71 */         message(new ExceptionMessage(s));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Subscribe
/*    */   public void message(final ExceptionMessage s) {
/* 80 */     JLabelHtmlWrapper label = new JLabelHtmlWrapper(s.getMessage() + (Objects.nonNull(s.getError()) ? ("<br> <br>" + ExceptionUtils.getStackTrace(s.getError()).replaceAll("\n", "<br>")) : ""));
/*    */     
/* 82 */     if (Objects.nonNull(s.getLink())) {
/* 83 */       label.addMouseListener(new MouseAdapter()
/*    */           {
/*    */             public void mouseClicked(MouseEvent e) {
/* 86 */               if (SwingUtilities.isLeftMouseButton(e)) {
/* 87 */                 DesktopUtil.openLink(ViewSubscriber.this.osType, s.getLink());
/*    */               }
/*    */             }
/*    */           });
/*    */     }
/* 92 */     JOptionPane.showMessageDialog((Component)this.frame, label, "", 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gde\\ui\subscriber\ViewSubscriber.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */