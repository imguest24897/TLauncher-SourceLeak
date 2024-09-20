/*     */ package by.gdev.updater;
/*     */ 
/*     */ import by.gdev.http.upload.download.downloader.DownloaderStatus;
/*     */ import by.gdev.http.upload.download.downloader.DownloaderStatusEnum;
/*     */ import by.gdev.util.CoreUtil;
/*     */ import com.google.common.eventbus.Subscribe;
/*     */ import com.google.inject.Inject;
/*     */ import java.awt.Color;
/*     */ import java.awt.Composite;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JProgressBar;
/*     */ import javax.swing.SpringLayout;
/*     */ import javax.swing.SwingUtilities;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProgressFrame
/*     */   extends JFrame
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final int WIDTH = 240;
/*     */   private static final int HEIGHT = 114;
/*  37 */   private Font font = new Font("Verdana", 0, 10);
/*     */   private String version_info;
/*     */   private JProgressBar progressBar;
/*  40 */   public final Color VERSION_BACKGROUND = new Color(40, 134, 187);
/*     */   private ResourceBundle resourceBundle;
/*  42 */   private JLabel uploadStatus = new JLabel();
/*     */   
/*     */   private JLabel version;
/*     */   
/*     */   @Inject
/*     */   public ProgressFrame(String info, boolean indeterminate, ResourceBundle resourceBundle) {
/*  48 */     this.resourceBundle = resourceBundle;
/*  49 */     getContentPane().setForeground(Color.LIGHT_GRAY);
/*  50 */     setTitle("TLauncher");
/*  51 */     setSize(240, 114);
/*  52 */     setLocationRelativeTo(null);
/*  53 */     setUndecorated(true);
/*  54 */     setResizable(false);
/*  55 */     setBackground(Color.LIGHT_GRAY);
/*  56 */     setDefaultCloseOperation(3);
/*     */ 
/*     */     
/*  59 */     this.version_info = info;
/*  60 */     SpringLayout springLayout = new SpringLayout();
/*  61 */     getContentPane().setLayout(springLayout);
/*  62 */     getContentPane().setPreferredSize(new Dimension(240, 114));
/*     */     
/*  64 */     this.uploadStatus.setFont(this.uploadStatus.getFont().deriveFont(1));
/*  65 */     this.uploadStatus.setForeground(Color.WHITE);
/*  66 */     this.uploadStatus.setHorizontalAlignment(4);
/*  67 */     this.uploadStatus.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 3));
/*  68 */     this.uploadStatus.setForeground(new Color(220, 220, 220));
/*     */     
/*  70 */     springLayout.putConstraint("North", this.uploadStatus, 0, "North", getContentPane());
/*  71 */     springLayout.putConstraint("West", this.uploadStatus, -220, "East", getContentPane());
/*  72 */     springLayout.putConstraint("South", this.uploadStatus, 160, "North", getContentPane());
/*  73 */     springLayout.putConstraint("East", this.uploadStatus, 0, "East", getContentPane());
/*  74 */     getContentPane().add(this.uploadStatus);
/*     */     
/*  76 */     this.version = new JLabel(this.version_info);
/*  77 */     this.version.setHorizontalAlignment(0);
/*  78 */     this.version.setForeground(Color.WHITE);
/*  79 */     this.version.setFont(this.font);
/*  80 */     this.version.setOpaque(true);
/*  81 */     this.version.setBackground(this.VERSION_BACKGROUND);
/*  82 */     springLayout.putConstraint("North", this.version, 0, "North", getContentPane());
/*  83 */     springLayout.putConstraint("West", this.version, -58, "East", getContentPane());
/*  84 */     springLayout.putConstraint("South", this.version, 21, "North", getContentPane());
/*  85 */     springLayout.putConstraint("East", this.version, 0, "East", getContentPane());
/*  86 */     getContentPane().add(this.version);
/*     */     
/*  88 */     JLabel backgroundImage = new JLabel();
/*  89 */     backgroundImage.setIcon(CoreUtil.getIcon("/newTlauncher.png"));
/*  90 */     springLayout.putConstraint("North", backgroundImage, 0, "North", getContentPane());
/*  91 */     springLayout.putConstraint("West", backgroundImage, 0, "West", getContentPane());
/*  92 */     springLayout.putConstraint("South", backgroundImage, 90, "North", getContentPane());
/*  93 */     springLayout.putConstraint("East", backgroundImage, 240, "West", getContentPane());
/*  94 */     getContentPane().add(backgroundImage);
/*     */     
/*  96 */     this.progressBar = new JProgressBar();
/*  97 */     this.progressBar.setDoubleBuffered(false);
/*  98 */     this.progressBar.setIndeterminate(indeterminate);
/*  99 */     this.progressBar.setBorder(BorderFactory.createEmptyBorder());
/* 100 */     this.progressBar.setUI(new PreloaderProgressUI(CoreUtil.getImage("/bottom-bar.png"), CoreUtil.getImage("/br.png")));
/*     */     
/* 102 */     springLayout.putConstraint("North", this.progressBar, 0, "South", backgroundImage);
/* 103 */     springLayout.putConstraint("West", this.progressBar, 0, "West", getContentPane());
/* 104 */     springLayout.putConstraint("South", this.progressBar, 0, "South", getContentPane());
/* 105 */     springLayout.putConstraint("East", this.progressBar, 4, "East", getContentPane());
/* 106 */     getContentPane().add(this.progressBar);
/* 107 */     pack();
/* 108 */     setVisible(true);
/*     */   }
/*     */   
/*     */   @Subscribe
/*     */   public void messageToSpeed(DownloaderStatus status) {
/* 113 */     if (this.progressBar.isIndeterminate() && DownloaderStatusEnum.WORK.equals(status.getDownloaderStatusEnum())) {
/* 114 */       SwingUtilities.invokeLater(() -> {
/*     */             this.progressBar.setIndeterminate(false);
/*     */             updateUploadProgressBar(status);
/*     */           });
/* 118 */     } else if (!this.progressBar.isIndeterminate()) {
/* 119 */       SwingUtilities.invokeLater(() -> {
/*     */             if (DownloaderStatusEnum.DONE.equals(status.getDownloaderStatusEnum()))
/*     */               this.progressBar.setIndeterminate(true); 
/*     */             updateUploadProgressBar(status);
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateUploadProgressBar(DownloaderStatus status) {
/* 128 */     int uploaded = (int)status.getDownloadSize() / 1048576;
/* 129 */     int allUpload = (int)status.getAllDownloadSize() / 1048576;
/* 130 */     if (!DownloaderStatusEnum.DONE.equals(status.getDownloaderStatusEnum())) {
/* 131 */       this.uploadStatus.setText(String.format("%s %s/%s %s ", new Object[] { this.resourceBundle.getString("uploading"), Integer.valueOf(uploaded), 
/* 132 */               Integer.valueOf(allUpload), this.resourceBundle.getString("mb") }));
/*     */     } else {
/* 134 */       this.uploadStatus.setText("");
/* 135 */     }  this.progressBar.setMaximum(allUpload);
/* 136 */     this.progressBar.setValue(uploaded);
/*     */   }
/*     */   
/*     */   public class ImagePanel extends JComponent {
/*     */     private static final long serialVersionUID = 1L;
/*     */     public static final float DEFAULT_ACTIVE_OPACITY = 1.0F;
/*     */     public static final float DEFAULT_NON_ACTIVE_OPACITY = 0.75F;
/* 143 */     protected final Object animationLock = new Object();
/*     */     
/*     */     private Image originalImage;
/*     */     
/*     */     private Image image;
/*     */     
/*     */     private boolean antiAlias;
/*     */     
/*     */     private boolean shown;
/*     */     private boolean animating;
/*     */     
/*     */     public ImagePanel(String image, float activeOpacity, float nonActiveOpacity, boolean shown) {
/* 155 */       this(CoreUtil.getImage(image), activeOpacity, nonActiveOpacity, shown);
/*     */     }
/*     */     
/*     */     public ImagePanel(Image image, float activeOpacity, float nonActiveOpacity, boolean shown) {
/* 159 */       setImage(image);
/*     */       
/* 161 */       setActiveOpacity(activeOpacity);
/* 162 */       setNonActiveOpacity(nonActiveOpacity);
/*     */       
/* 164 */       this.shown = shown;
/* 165 */       setBackground(new Color(0, 0, 0, 0));
/*     */       
/* 167 */       addMouseListenerOriginally(new MouseAdapter()
/*     */           {
/*     */             public void mouseClicked(MouseEvent e) {
/* 170 */               ProgressFrame.ImagePanel.this.onClick();
/*     */             }
/*     */ 
/*     */             
/*     */             public void mouseEntered(MouseEvent e) {
/* 175 */               ProgressFrame.ImagePanel.this.onMouseEntered();
/*     */             }
/*     */ 
/*     */             
/*     */             public void mouseExited(MouseEvent e) {
/* 180 */               ProgressFrame.ImagePanel.this.onMouseExited();
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     public void setImage(Image image, boolean resetSize) {
/* 186 */       synchronized (this.animationLock) {
/* 187 */         this.originalImage = image;
/* 188 */         this.image = image;
/*     */         
/* 190 */         if (resetSize && image != null)
/* 191 */           setSize(image.getWidth(null), image.getHeight(null)); 
/*     */       } 
/*     */     }
/*     */     
/*     */     protected void setImage(Image image) {
/* 196 */       setImage(image, true);
/*     */     }
/*     */     
/*     */     protected void setActiveOpacity(float opacity) {
/* 200 */       if (opacity > 1.0F || opacity < 0.0F) {
/* 201 */         throw new IllegalArgumentException("Invalid opacity! Condition: 0.0F <= opacity (got: " + opacity + ") <= 1.0F");
/*     */       }
/*     */     }
/*     */     
/*     */     protected void setNonActiveOpacity(float opacity) {
/* 206 */       if (opacity > 1.0F || opacity < 0.0F) {
/* 207 */         throw new IllegalArgumentException("Invalid opacity! Condition: 0.0F <= opacity (got: " + opacity + ") <= 1.0F");
/*     */       }
/*     */     }
/*     */     
/*     */     protected boolean onClick() {
/* 212 */       return this.shown;
/*     */     }
/*     */     
/*     */     protected void onMouseEntered() {
/* 216 */       if (this.animating || !this.shown) {
/*     */         return;
/*     */       }
/* 219 */       repaint();
/*     */     }
/*     */     
/*     */     protected void onMouseExited() {
/* 223 */       if (this.animating || !this.shown)
/*     */         return; 
/* 225 */       repaint();
/*     */     }
/*     */     
/*     */     public synchronized void addMouseListenerOriginally(MouseListener listener) {
/* 229 */       addMouseListener(listener);
/*     */     }
/*     */ 
/*     */     
/*     */     public void paintComponent(Graphics g0) {
/* 234 */       if (this.image == null) {
/*     */         return;
/*     */       }
/* 237 */       Graphics2D g = (Graphics2D)g0;
/* 238 */       Composite oldComp = g.getComposite();
/*     */       
/* 240 */       g.drawImage(this.image, 0, 0, getWidth(), getHeight(), null);
/*     */       
/* 242 */       g.setComposite(oldComp);
/*     */     }
/*     */   }
/*     */   
/*     */   public class ImagePanel1
/*     */     extends JComponent {
/*     */     private static final long serialVersionUID = 1L;
/*     */     public static final float DEFAULT_ACTIVE_OPACITY = 1.0F;
/*     */     public static final float DEFAULT_NON_ACTIVE_OPACITY = 0.75F;
/* 251 */     protected final Object animationLock = new Object();
/*     */     
/*     */     private Image originalImage;
/*     */     
/*     */     private Image image;
/*     */     
/*     */     private boolean antiAlias;
/*     */     
/*     */     private boolean shown;
/*     */     private boolean animating;
/*     */     
/*     */     public ImagePanel1(String image, float activeOpacity, float nonActiveOpacity, boolean shown) {
/* 263 */       this(CoreUtil.getImage(image), activeOpacity, nonActiveOpacity, shown);
/*     */     }
/*     */     
/*     */     public ImagePanel1(Image image, float activeOpacity, float nonActiveOpacity, boolean shown) {
/* 267 */       setImage(image);
/*     */       
/* 269 */       setActiveOpacity(activeOpacity);
/* 270 */       setNonActiveOpacity(nonActiveOpacity);
/*     */       
/* 272 */       this.shown = shown;
/* 273 */       setBackground(new Color(0, 0, 0, 0));
/*     */       
/* 275 */       addMouseListenerOriginally(new MouseAdapter()
/*     */           {
/*     */             public void mouseClicked(MouseEvent e) {
/* 278 */               ProgressFrame.ImagePanel1.this.onClick();
/*     */             }
/*     */ 
/*     */             
/*     */             public void mouseEntered(MouseEvent e) {
/* 283 */               ProgressFrame.ImagePanel1.this.onMouseEntered();
/*     */             }
/*     */ 
/*     */             
/*     */             public void mouseExited(MouseEvent e) {
/* 288 */               ProgressFrame.ImagePanel1.this.onMouseExited();
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     public void setImage(Image image, boolean resetSize) {
/* 294 */       synchronized (this.animationLock) {
/* 295 */         this.originalImage = image;
/* 296 */         this.image = image;
/*     */         
/* 298 */         if (resetSize && image != null)
/* 299 */           setSize(image.getWidth(null), image.getHeight(null)); 
/*     */       } 
/*     */     }
/*     */     
/*     */     protected void setImage(Image image) {
/* 304 */       setImage(image, true);
/*     */     }
/*     */     
/*     */     protected void setActiveOpacity(float opacity) {
/* 308 */       if (opacity > 1.0F || opacity < 0.0F) {
/* 309 */         throw new IllegalArgumentException("Invalid opacity! Condition: 0.0F <= opacity (got: " + opacity + ") <= 1.0F");
/*     */       }
/*     */     }
/*     */     
/*     */     protected void setNonActiveOpacity(float opacity) {
/* 314 */       if (opacity > 1.0F || opacity < 0.0F) {
/* 315 */         throw new IllegalArgumentException("Invalid opacity! Condition: 0.0F <= opacity (got: " + opacity + ") <= 1.0F");
/*     */       }
/*     */     }
/*     */     
/*     */     protected boolean onClick() {
/* 320 */       return this.shown;
/*     */     }
/*     */     
/*     */     protected void onMouseEntered() {
/* 324 */       if (this.animating || !this.shown) {
/*     */         return;
/*     */       }
/* 327 */       repaint();
/*     */     }
/*     */     
/*     */     protected void onMouseExited() {
/* 331 */       if (this.animating || !this.shown)
/*     */         return; 
/* 333 */       repaint();
/*     */     }
/*     */     
/*     */     public synchronized void addMouseListenerOriginally(MouseListener listener) {
/* 337 */       addMouseListener(listener);
/*     */     }
/*     */ 
/*     */     
/*     */     public void paintComponent(Graphics g0) {
/* 342 */       if (this.image == null) {
/*     */         return;
/*     */       }
/* 345 */       Graphics2D g = (Graphics2D)g0;
/* 346 */       Composite oldComp = g.getComposite();
/*     */       
/* 348 */       g.drawImage(this.image, 0, 0, getWidth(), getHeight(), null);
/*     */       
/* 350 */       g.setComposite(oldComp);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNewVersionText(String versionText) {
/* 356 */     this.version.setText(versionText);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gde\\updater\ProgressFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */