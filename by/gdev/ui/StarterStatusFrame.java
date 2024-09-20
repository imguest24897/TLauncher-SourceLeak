/*     */ package by.gdev.ui;
/*     */ 
/*     */ import by.gdev.http.upload.download.downloader.DownloaderStatus;
/*     */ import by.gdev.http.upload.download.downloader.DownloaderStatusEnum;
/*     */ import by.gdev.util.DesktopUtil;
/*     */ import by.gdev.util.OSInfo;
/*     */ import com.google.common.eventbus.Subscribe;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.IOException;
/*     */ import java.util.Objects;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JProgressBar;
/*     */ import javax.swing.SwingUtilities;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class StarterStatusFrame extends JFrame {
/*  33 */   private static final Logger log = LoggerFactory.getLogger(StarterStatusFrame.class);
/*     */   
/*     */   private static final long serialVersionUID = 1L;
/*     */   private JProgressBar progressBar;
/*  37 */   private String gdevBy = "https://github.com/gdevby/desktop-starter-launch-update-bootstrap";
/*  38 */   private JLabel uploadStatus = new JLabel();
/*     */   private ResourceBundle resourceBundle;
/*     */   
/*     */   public StarterStatusFrame(final OSInfo.OSType type, String appName, boolean indeterminate, ResourceBundle resourceBundle) {
/*  42 */     this.resourceBundle = resourceBundle;
/*  43 */     setResizable(false);
/*  44 */     setUndecorated(true);
/*  45 */     setAlwaysOnTop(true);
/*  46 */     FrameUtils.setFavicons(this);
/*  47 */     GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
/*  48 */     int width = gd.getDisplayMode().getWidth();
/*  49 */     int height = gd.getDisplayMode().getHeight();
/*  50 */     setSize(new Dimension(width / 5, height / 5));
/*  51 */     DesktopUtil.initLookAndFeel();
/*     */     
/*  53 */     JPanel p = new JPanel(new BorderLayout(0, 0));
/*  54 */     final BufferedImage image = getImage();
/*  55 */     JPanel background = new JPanel(new BorderLayout(0, 0))
/*     */       {
/*     */         private static final long serialVersionUID = 1L;
/*     */         
/*     */         public void paint(Graphics g) {
/*  60 */           if (Objects.nonNull(image)) {
/*  61 */             g.drawImage(image, 0, 0, (getSize()).width, (getSize()).height, null);
/*     */           }
/*  63 */           super.paint(g);
/*     */         }
/*     */       };
/*     */     
/*  67 */     background.setOpaque(false);
/*  68 */     final JLabel label = new JLabel("app launcher gdev.by");
/*  69 */     label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
/*  70 */     Font f = label.getFont();
/*  71 */     label.setFont(f.deriveFont(1).deriveFont((f.getSize() - 2)));
/*  72 */     p.setOpaque(true);
/*  73 */     JLabel nameLabel = new JLabel(appName);
/*  74 */     f = nameLabel.getFont();
/*  75 */     nameLabel.setFont(f.deriveFont((f.getSize() + 5)));
/*  76 */     nameLabel.setHorizontalAlignment(0);
/*  77 */     nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, (getSize()).height / 3, 0));
/*     */     
/*  79 */     this.uploadStatus.setFont(this.uploadStatus.getFont().deriveFont(1));
/*  80 */     this.uploadStatus.setHorizontalAlignment(4);
/*  81 */     this.uploadStatus.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 3));
/*     */     
/*  83 */     this.progressBar = new JProgressBar();
/*  84 */     this.progressBar.setDoubleBuffered(true);
/*  85 */     this.progressBar.setIndeterminate(indeterminate);
/*  86 */     this.progressBar.setBorder(BorderFactory.createEmptyBorder());
/*     */     
/*  88 */     background.add(nameLabel, "Center");
/*  89 */     background.add(label, "North");
/*  90 */     background.add(this.uploadStatus, "South");
/*     */     
/*  92 */     p.add(background, "Center");
/*  93 */     p.add(this.progressBar, "South");
/*     */     
/*  95 */     add(p);
/*  96 */     setLocation(width / 2 - (getSize()).width / 2, height / 2 - (getSize()).height / 2);
/*     */     
/*  98 */     label.addMouseListener(new MouseAdapter() {
/*  99 */           private Color c = label.getForeground();
/*     */ 
/*     */           
/*     */           public void mouseClicked(MouseEvent e) {
/* 103 */             if (SwingUtilities.isLeftMouseButton(e)) {
/* 104 */               DesktopUtil.openLink(type, StarterStatusFrame.this.gdevBy);
/*     */             }
/*     */           }
/*     */ 
/*     */           
/*     */           public void mouseEntered(MouseEvent e) {
/* 110 */             label.setForeground(Color.BLACK);
/*     */           }
/*     */ 
/*     */           
/*     */           public void mouseExited(MouseEvent e) {
/* 115 */             label.setForeground(this.c);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private BufferedImage getImage() {
/* 121 */     BufferedImage image = null;
/*     */     try {
/* 123 */       image = ImageIO.read(StarterStatusFrame.class.getResourceAsStream("/background.jpg"));
/* 124 */     } catch (IOException e) {
/* 125 */       log.warn("can't load image", e);
/*     */     } 
/* 127 */     return image;
/*     */   }
/*     */   
/*     */   @Subscribe
/*     */   public void messageToSpeed(DownloaderStatus status) {
/* 132 */     if (this.progressBar.isIndeterminate() && DownloaderStatusEnum.WORK.equals(status.getDownloaderStatusEnum())) {
/* 133 */       SwingUtilities.invokeLater(() -> {
/*     */             this.progressBar.setIndeterminate(false);
/*     */             updateUploadProgressBar(status);
/*     */           });
/* 137 */     } else if (!this.progressBar.isIndeterminate()) {
/* 138 */       SwingUtilities.invokeLater(() -> updateUploadProgressBar(status));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateUploadProgressBar(DownloaderStatus status) {
/* 145 */     int uploaded = (int)status.getDownloadSize() / 1048576;
/* 146 */     int allUpload = (int)status.getAllDownloadSize() / 1048576;
/* 147 */     this.uploadStatus.setText(String.format("%s %s/%s %s ", new Object[] { this.resourceBundle.getString("uploading"), Integer.valueOf(uploaded), Integer.valueOf(allUpload), this.resourceBundle
/* 148 */             .getString("mb") }));
/* 149 */     this.progressBar.setMaximum(allUpload);
/* 150 */     this.progressBar.setValue(uploaded);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gde\\ui\StarterStatusFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */