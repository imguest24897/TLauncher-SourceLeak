/*     */ package by.gdev.ui;
/*     */ 
/*     */ import by.gdev.model.AppConfig;
/*     */ import by.gdev.model.AppLocalConfig;
/*     */ import by.gdev.model.StarterAppConfig;
/*     */ import by.gdev.util.DesktopUtil;
/*     */ import by.gdev.util.OSInfo;
/*     */ import by.gdev.utils.service.FileMapperService;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.io.IOException;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class UpdateFrame
/*     */   extends JFrame {
/*  34 */   private static final Logger log = LoggerFactory.getLogger(UpdateFrame.class);
/*     */   
/*     */   private static final long serialVersionUID = 2832387031191814036L;
/*     */   
/*  38 */   private AtomicInteger userChoose = new AtomicInteger();
/*     */ 
/*     */ 
/*     */   
/*     */   public UpdateFrame(JFrame progressFrame, ResourceBundle resourceBundle, AppLocalConfig appLocalConfig, AppConfig remoteAppConfig, StarterAppConfig starterAppConfig, FileMapperService fileMapperService, final OSInfo.OSType osType) {
/*  43 */     progressFrame.setVisible(false);
/*  44 */     setResizable(false);
/*  45 */     GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
/*  46 */     int width = gd.getDisplayMode().getWidth();
/*  47 */     int height = gd.getDisplayMode().getHeight();
/*  48 */     setSize(new Dimension(width / 4, height / 5));
/*  49 */     setLocation(width / 2 - (getSize()).width / 2, height / 2 - (getSize()).height / 2);
/*  50 */     progressFrame.setVisible(false);
/*  51 */     JPanel p = new JPanel(new BorderLayout(0, 0));
/*  52 */     p.setBackground(Color.WHITE);
/*  53 */     p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
/*  54 */     final String link = String.join("/", new CharSequence[] { starterAppConfig.getServerFile().get(0), "changes.log" });
/*  55 */     final JLabel text = new JLabelHtmlWrapper(String.format(resourceBundle.getString("update.app"), new Object[] { appLocalConfig
/*  56 */             .getCurrentAppVersion(), remoteAppConfig.getAppVersion() }));
/*  57 */     text.setFont(text.getFont().deriveFont(1));
/*  58 */     text.setHorizontalAlignment(0);
/*  59 */     JPanel buttonPanel = new JPanel(new FlowLayout());
/*  60 */     JPanel verticalPanel = new JPanel();
/*  61 */     verticalPanel.setOpaque(false);
/*  62 */     BoxLayout boxLayout = new BoxLayout(verticalPanel, 1);
/*  63 */     verticalPanel.setLayout(boxLayout);
/*  64 */     addButton(new JButton(resourceBundle.getString("skip")), buttonPanel, 1);
/*  65 */     addButton(new JButton(resourceBundle.getString("update")), buttonPanel, 2);
/*  66 */     setDefaultCloseOperation(3);
/*  67 */     verticalPanel.add(buttonPanel);
/*     */     
/*  69 */     JCheckBox j = new JCheckBox(String.format(resourceBundle.getString("not.show.again"), new Object[] { remoteAppConfig.getAppVersion() }));
/*  70 */     j.setAlignmentX(0.5F);
/*  71 */     j.addActionListener(e -> {
/*     */           try {
/*     */             AppLocalConfig app = (AppLocalConfig)fileMapperService.read("starter.json", AppLocalConfig.class);
/*     */             
/*     */             app.setSkipUpdateVersion(remoteAppConfig.getAppVersion());
/*     */             fileMapperService.write(app, "starter.json");
/*  77 */           } catch (IOException e1) {
/*     */             log.error("error", e1);
/*     */           } 
/*     */         });
/*  81 */     text.addMouseListener(new MouseAdapter() {
/*  82 */           private Color c = text.getForeground();
/*     */ 
/*     */           
/*     */           public void mouseClicked(MouseEvent e) {
/*  86 */             if (SwingUtilities.isLeftMouseButton(e)) {
/*  87 */               DesktopUtil.openLink(osType, link);
/*     */             }
/*     */           }
/*     */ 
/*     */           
/*     */           public void mouseEntered(MouseEvent e) {
/*  93 */             text.setForeground(Color.BLACK);
/*     */           }
/*     */ 
/*     */           
/*     */           public void mouseExited(MouseEvent e) {
/*  98 */             text.setForeground(this.c);
/*     */           }
/*     */         });
/* 101 */     verticalPanel.add(j);
/*     */     
/* 103 */     buttonPanel.setOpaque(false);
/*     */     
/* 105 */     p.add(text, "Center");
/* 106 */     p.add(verticalPanel, "South");
/* 107 */     add(p);
/*     */     
/* 109 */     setVisible(true);
/*     */     
/* 111 */     while (isVisible()) {
/* 112 */       DesktopUtil.sleep(100);
/*     */     }
/* 114 */     progressFrame.setVisible(true);
/*     */   }
/*     */   
/*     */   private void addButton(JButton b, JPanel panel, int code) {
/* 118 */     b.setAlignmentX(0.5F);
/* 119 */     b.addActionListener(e -> {
/*     */           this.userChoose.set(code);
/*     */           setVisible(false);
/*     */         });
/* 123 */     panel.add(b);
/*     */   }
/*     */   
/*     */   public int getUserChoose() {
/* 127 */     return this.userChoose.get();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gde\\ui\UpdateFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */