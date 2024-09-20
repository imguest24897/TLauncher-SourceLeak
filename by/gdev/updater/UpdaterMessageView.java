/*     */ package by.gdev.updater;
/*     */ 
/*     */ import by.gdev.model.AppConfig;
/*     */ import by.gdev.model.AppLocalConfig;
/*     */ import by.gdev.util.CoreUtil;
/*     */ import by.gdev.util.DesktopUtil;
/*     */ import by.gdev.util.OSInfo;
/*     */ import java.awt.Color;
/*     */ import java.awt.Composite;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.SpringLayout;
/*     */ 
/*     */ 
/*     */ public class UpdaterMessageView
/*     */   extends JPanel
/*     */ {
/*     */   private static final long serialVersionUID = -9173760470917959755L;
/*  45 */   private static final JFrame frame = new JFrame();
/*  46 */   private static final Dimension SIZE = new Dimension(900, 600);
/*     */   private RoundUpdaterButton updater; private RoundUpdaterButton laterUpdater;
/*     */   private RoundUpdaterButton ok;
/*     */   private JPanel down;
/*     */   private ImagePanel imageTop;
/*     */   private AtomicInteger userChoose;
/*     */   public int result;
/*     */   private List<JCheckBox> checkBoxList;
/*     */   private ResourceBundle bundle;
/*     */   
/*  56 */   static { frame.setAlwaysOnTop(true); } public UpdaterMessageView(Update update, int messageType, String langCase, boolean isAdmin, AppLocalConfig appLocalConfig, ResourceBundle bundle, AppConfig remoteAppConfig) throws IOException {
/*     */     String image;
/*     */     this.down = new JPanel();
/*     */     this.userChoose = new AtomicInteger();
/*     */     this.checkBoxList = new ArrayList<>();
/*  61 */     this.bundle = bundle;
/*  62 */     this.ok = new RoundUpdaterButton(new Color(107, 202, 45), new Color(91, 174, 37), "OK", new Color(107, 202, 45), 1, this.userChoose);
/*     */     
/*  64 */     this
/*  65 */       .updater = new RoundUpdaterButton(new Color(107, 202, 45), new Color(91, 174, 37), bundle.getString("launcher.update.updater.button"), new Color(107, 202, 45), 1, this.userChoose);
/*  66 */     this
/*  67 */       .laterUpdater = new RoundUpdaterButton(new Color(235, 132, 46), new Color(200, 112, 38), bundle.getString("launcher.update.later.button"), new Color(235, 132, 46), 2, this.userChoose);
/*  68 */     this.ok.setFont(getFont().deriveFont(13.0F));
/*  69 */     this.updater.setFont(getFont().deriveFont(13.0F));
/*  70 */     this.laterUpdater.setFont(getFont().deriveFont(13.0F));
/*  71 */     setPreferredSize(SIZE);
/*     */     
/*  73 */     if (messageType == 2) {
/*  74 */       image = "offer.png";
/*  75 */     } else if (messageType == 1) {
/*  76 */       image = "banner.png";
/*     */     } else {
/*  78 */       image = "without-banner-offer.png";
/*     */     } 
/*  80 */     this.imageTop = new ImagePanel(CoreUtil.getImage("/" + image), 1.0F, 1.0F, true);
/*     */     
/*  82 */     JLabel tlauncher = new JLabel("TLAUNCHER  " + remoteAppConfig.getAppVersion());
/*  83 */     tlauncher.setForeground(Color.BLACK);
/*  84 */     tlauncher.setFont(getFont().deriveFont(1, 26.0F));
/*     */     
/*  86 */     JScrollPane message = HtmlTextPane.createNewAndWrap(update.isMandatory() ? bundle.getString("launcher.update.message.mandatory") : bundle
/*  87 */         .getString("launcher.update.message.optional"), 246);
/*  88 */     message.setFont(getFont().deriveFont(1, 20.0F));
/*  89 */     String res = update.getDescription(langCase);
/*  90 */     JScrollPane changes = HtmlTextPane.createNewAndWrap(Objects.isNull(res) ? update.getDescription("en") : res, 246);
/*     */     
/*  92 */     changes.setForeground(Color.BLACK);
/*  93 */     JScrollPane notice = HtmlTextPane.createNewAndWrap(bundle.getString("updater.notice"), 700);
/*     */     
/*  95 */     SpringLayout spring = new SpringLayout();
/*  96 */     SpringLayout topSpring = new SpringLayout();
/*  97 */     SpringLayout downSpring = new SpringLayout();
/*  98 */     setLayout(spring);
/*  99 */     this.imageTop.setLayout(topSpring);
/* 100 */     this.down.setLayout(downSpring);
/*     */     
/* 102 */     spring.putConstraint("West", this.imageTop, 0, "West", this);
/* 103 */     spring.putConstraint("East", this.imageTop, 0, "East", this);
/* 104 */     spring.putConstraint("North", this.imageTop, 0, "North", this);
/* 105 */     spring.putConstraint("South", this.imageTop, 453, "North", this);
/* 106 */     add(this.imageTop);
/*     */     
/* 108 */     spring.putConstraint("West", this.down, 0, "West", this);
/* 109 */     spring.putConstraint("East", this.down, 0, "East", this);
/* 110 */     spring.putConstraint("North", this.down, 453, "North", this);
/* 111 */     spring.putConstraint("South", this.down, 600, "North", this);
/* 112 */     add(this.down);
/*     */     
/* 114 */     topSpring.putConstraint("West", tlauncher, 40, "West", this.imageTop);
/* 115 */     topSpring.putConstraint("East", tlauncher, 306, "West", this.imageTop);
/* 116 */     topSpring.putConstraint("North", tlauncher, 37, "North", this.imageTop);
/* 117 */     topSpring.putConstraint("South", tlauncher, 67, "North", this.imageTop);
/* 118 */     this.imageTop.add(tlauncher);
/*     */     
/* 120 */     topSpring.putConstraint("West", message, 40, "West", this.imageTop);
/* 121 */     topSpring.putConstraint("East", message, 286, "West", this.imageTop);
/* 122 */     topSpring.putConstraint("North", message, 60, "North", this.imageTop);
/* 123 */     topSpring.putConstraint("South", message, 160, "North", this.imageTop);
/* 124 */     this.imageTop.add(message);
/*     */     
/* 126 */     topSpring.putConstraint("West", changes, 40, "West", this.imageTop);
/* 127 */     topSpring.putConstraint("East", changes, 286, "West", this.imageTop);
/* 128 */     topSpring.putConstraint("North", changes, 144, "North", this.imageTop);
/* 129 */     topSpring.putConstraint("South", changes, -40, "South", this.imageTop);
/* 130 */     this.imageTop.add(changes);
/*     */     
/* 132 */     downSpring.putConstraint("West", notice, 40, "West", this.down);
/* 133 */     downSpring.putConstraint("East", notice, 0, "East", this.down);
/* 134 */     downSpring.putConstraint("North", notice, 5, "North", this.down);
/* 135 */     downSpring.putConstraint("South", notice, 65, "North", this.down);
/* 136 */     this.down.add(notice);
/*     */     
/* 138 */     if (update.isMandatory(appLocalConfig.getCurrentAppVersion())) {
/* 139 */       downSpring.putConstraint("West", this.ok, 365, "West", this.down);
/* 140 */       downSpring.putConstraint("East", this.ok, 535, "West", this.down);
/* 141 */       downSpring.putConstraint("North", this.ok, 86, "North", this.down);
/* 142 */       downSpring.putConstraint("South", this.ok, 123, "North", this.down);
/* 143 */       this.down.add(this.ok);
/*     */     } else {
/* 145 */       downSpring.putConstraint("West", this.updater, 273, "West", this.down);
/* 146 */       downSpring.putConstraint("East", this.updater, 443, "West", this.down);
/* 147 */       downSpring.putConstraint("North", this.updater, 86, "North", this.down);
/* 148 */       downSpring.putConstraint("South", this.updater, 123, "North", this.down);
/* 149 */       this.down.add(this.updater);
/*     */       
/* 151 */       downSpring.putConstraint("West", this.laterUpdater, 454, "West", this.down);
/* 152 */       downSpring.putConstraint("East", this.laterUpdater, 624, "West", this.down);
/* 153 */       downSpring.putConstraint("North", this.laterUpdater, 86, "North", this.down);
/* 154 */       downSpring.putConstraint("South", this.laterUpdater, 123, "North", this.down);
/* 155 */       this.down.add(this.laterUpdater);
/*     */     } 
/*     */     
/* 158 */     if (messageType == 1) {
/* 159 */       final Banner banner = ((List<Banner>)update.getBanners().get(langCase)).get(0);
/* 160 */       JLabel imagePanel = null;
/*     */       try {
/* 162 */         imagePanel = new JLabel(new ImageIcon(ImageIO.read(new URL(banner.getImage()))));
/* 163 */         imagePanel.addMouseListener(new MouseAdapter()
/*     */             {
/*     */               public void mouseClicked(MouseEvent e) {
/* 166 */                 DesktopUtil.openLink(OSInfo.getOSType(), banner.getClickLink());
/*     */               }
/*     */             });
/* 169 */       } catch (MalformedURLException malformedURLException) {}
/*     */       
/* 171 */       topSpring.putConstraint("West", imagePanel, 326, "West", this.imageTop);
/* 172 */       topSpring.putConstraint("East", imagePanel, 0, "East", this.imageTop);
/* 173 */       topSpring.putConstraint("North", imagePanel, 0, "North", this.imageTop);
/* 174 */       topSpring.putConstraint("South", imagePanel, 453, "North", this.imageTop);
/* 175 */       this.imageTop.add(imagePanel);
/* 176 */     } else if (messageType == 2) {
/* 177 */       Offer offer = update.getSelectedOffer();
/* 178 */       JScrollPane pane = HtmlTextPane.createNewAndWrap(offer.getTopText().get(langCase), 574);
/* 179 */       topSpring.putConstraint("West", pane, 326, "West", this.imageTop);
/* 180 */       topSpring.putConstraint("East", pane, 0, "East", this.imageTop);
/* 181 */       topSpring.putConstraint("North", pane, 0, "North", this.imageTop);
/* 182 */       topSpring.putConstraint("South", pane, offer.getStartCheckboxSouth(), "North", this.imageTop);
/*     */       
/* 184 */       this.imageTop.add(pane);
/* 185 */       int start = offer.getStartCheckboxSouth();
/* 186 */       for (PointOffer p : offer.getCheckBoxes()) {
/* 187 */         JCheckBox checkBox = new OwnImageCheckBox(p.getTexts().get(langCase), "/updater-checkbox-on.png", "/updater-checkbox-off.png");
/*     */         
/* 189 */         checkBox.setForeground(new Color(60, 60, 60));
/* 190 */         checkBox.setFont(getFont().deriveFont(15.0F));
/* 191 */         if (isAdmin) {
/* 192 */           checkBox.setSelected(p.isActive());
/*     */         } else {
/* 194 */           checkBox.setSelected(false);
/* 195 */         }  checkBox.setIconTextGap(18);
/* 196 */         checkBox.setActionCommand(p.getName());
/* 197 */         checkBox.setVerticalAlignment(0);
/* 198 */         topSpring.putConstraint("West", checkBox, 378, "West", this.imageTop);
/* 199 */         topSpring.putConstraint("East", checkBox, 0, "East", this.imageTop);
/* 200 */         topSpring.putConstraint("North", checkBox, start, "North", this.imageTop);
/* 201 */         topSpring.putConstraint("South", checkBox, start + 39, "North", this.imageTop);
/* 202 */         start += 39;
/* 203 */         this.checkBoxList.add(checkBox);
/* 204 */         this.imageTop.add(checkBox);
/*     */       } 
/* 206 */       JScrollPane downDescription = HtmlTextPane.createNewAndWrap(offer.getDownText().get(langCase), 574);
/* 207 */       topSpring.putConstraint("West", downDescription, 328, "West", this.imageTop);
/* 208 */       topSpring.putConstraint("East", downDescription, 0, "East", this.imageTop);
/* 209 */       topSpring.putConstraint("North", downDescription, 320, "North", this.imageTop);
/* 210 */       topSpring.putConstraint("South", downDescription, 0, "South", this.imageTop);
/* 211 */       this.imageTop.add(downDescription);
/*     */     } 
/*     */     
/* 214 */     this.down.setBackground(Color.WHITE);
/* 215 */     tlauncher.setHorizontalTextPosition(2);
/* 216 */     this.ok.addActionListener(e -> this.result = 1);
/* 217 */     this.updater.addActionListener(e -> this.result = 1);
/* 218 */     this.laterUpdater.addActionListener(e -> this.result = 0);
/*     */   }
/*     */   
/*     */   public class ImagePanel extends JComponent {
/*     */     private static final long serialVersionUID = 1L;
/*     */     public static final float DEFAULT_ACTIVE_OPACITY = 1.0F;
/*     */     public static final float DEFAULT_NON_ACTIVE_OPACITY = 0.75F;
/* 225 */     protected final Object animationLock = new Object();
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
/* 237 */       this(CoreUtil.getImage(image), activeOpacity, nonActiveOpacity, shown);
/*     */     }
/*     */     
/*     */     public ImagePanel(Image image, float activeOpacity, float nonActiveOpacity, boolean shown) {
/* 241 */       setImage(image);
/*     */       
/* 243 */       setActiveOpacity(activeOpacity);
/* 244 */       setNonActiveOpacity(nonActiveOpacity);
/*     */       
/* 246 */       this.shown = shown;
/* 247 */       setBackground(new Color(0, 0, 0, 0));
/*     */       
/* 249 */       addMouseListenerOriginally(new MouseAdapter()
/*     */           {
/*     */             public void mouseClicked(MouseEvent e) {
/* 252 */               UpdaterMessageView.ImagePanel.this.onClick();
/*     */             }
/*     */ 
/*     */             
/*     */             public void mouseEntered(MouseEvent e) {
/* 257 */               UpdaterMessageView.ImagePanel.this.onMouseEntered();
/*     */             }
/*     */ 
/*     */             
/*     */             public void mouseExited(MouseEvent e) {
/* 262 */               UpdaterMessageView.ImagePanel.this.onMouseExited();
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     public void setImage(Image image, boolean resetSize) {
/* 268 */       synchronized (this.animationLock) {
/* 269 */         this.originalImage = image;
/* 270 */         this.image = image;
/*     */         
/* 272 */         if (resetSize && image != null)
/* 273 */           setSize(image.getWidth(null), image.getHeight(null)); 
/*     */       } 
/*     */     }
/*     */     
/*     */     protected void setImage(Image image) {
/* 278 */       setImage(image, true);
/*     */     }
/*     */     
/*     */     protected void setActiveOpacity(float opacity) {
/* 282 */       if (opacity > 1.0F || opacity < 0.0F) {
/* 283 */         throw new IllegalArgumentException("Invalid opacity! Condition: 0.0F <= opacity (got: " + opacity + ") <= 1.0F");
/*     */       }
/*     */     }
/*     */     
/*     */     protected void setNonActiveOpacity(float opacity) {
/* 288 */       if (opacity > 1.0F || opacity < 0.0F) {
/* 289 */         throw new IllegalArgumentException("Invalid opacity! Condition: 0.0F <= opacity (got: " + opacity + ") <= 1.0F");
/*     */       }
/*     */     }
/*     */     
/*     */     protected boolean onClick() {
/* 294 */       return this.shown;
/*     */     }
/*     */     
/*     */     protected void onMouseEntered() {
/* 298 */       if (this.animating || !this.shown) {
/*     */         return;
/*     */       }
/* 301 */       repaint();
/*     */     }
/*     */     
/*     */     protected void onMouseExited() {
/* 305 */       if (this.animating || !this.shown)
/*     */         return; 
/* 307 */       repaint();
/*     */     }
/*     */     
/*     */     public synchronized void addMouseListenerOriginally(MouseListener listener) {
/* 311 */       addMouseListener(listener);
/*     */     }
/*     */ 
/*     */     
/*     */     public void paintComponent(Graphics g0) {
/* 316 */       if (this.image == null) {
/*     */         return;
/*     */       }
/* 319 */       Graphics2D g = (Graphics2D)g0;
/* 320 */       Composite oldComp = g.getComposite();
/*     */       
/* 322 */       g.drawImage(this.image, 0, 0, getWidth(), getHeight(), null);
/*     */       
/* 324 */       g.setComposite(oldComp);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public UpdaterFormController.UserResult showMessage(Update updates, AppLocalConfig appLocalConfig) {
/* 330 */     this.result = -1;
/* 331 */     if (updates.isMandatory(appLocalConfig.getCurrentAppVersion())) {
/* 332 */       showMessage(this.bundle.getString("launcher.update.title"), this, new JButton[] { this.ok });
/*     */     } else {
/* 334 */       showMessage(this.bundle.getString("launcher.update.title"), this, new JButton[] { this.updater, this.laterUpdater });
/*     */     } 
/* 336 */     UpdaterFormController.UserResult res = new UpdaterFormController.UserResult();
/* 337 */     res.setUserChooser(this.result);
/* 338 */     StringBuilder builder = new StringBuilder();
/* 339 */     for (JCheckBox box : this.checkBoxList) {
/* 340 */       if (box.isSelected()) {
/* 341 */         if (builder.length() > 0)
/* 342 */           builder.append("+"); 
/* 343 */         builder.append(box.getActionCommand());
/* 344 */         res.setSelectedAnyCheckBox(true);
/*     */       } 
/*     */     } 
/* 347 */     res.setOfferArgs(builder.toString());
/* 348 */     return res;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void showMessage(String title, JPanel content, JButton[] buttons) {
/* 353 */     JDialog jDialog = new JDialog(frame, title);
/* 354 */     Arrays.<JButton>stream(buttons).forEach(e -> e.addActionListener(()));
/* 355 */     jDialog.setAlwaysOnTop(true);
/* 356 */     jDialog.setResizable(false);
/* 357 */     jDialog.setContentPane(content);
/* 358 */     jDialog.setModal(true);
/* 359 */     jDialog.pack();
/* 360 */     jDialog.setLocationRelativeTo(null);
/* 361 */     jDialog.setVisible(true);
/*     */   }
/*     */   
/*     */   public static void addListener(final JButton changes, final Color color) {
/* 365 */     changes.addActionListener(e -> {
/*     */           JOptionPane pane = getOptionPane((JComponent)e.getSource());
/*     */           pane.setValue(changes);
/*     */         });
/* 369 */     changes.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseEntered(MouseEvent e) {
/* 372 */             changes.setBackground(color);
/*     */           }
/*     */ 
/*     */           
/*     */           public void mouseExited(MouseEvent e) {
/* 377 */             changes.setBackground(changes.getBackground());
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected static JOptionPane getOptionPane(JComponent parent) {
/*     */     JOptionPane pane;
/* 385 */     if (!(parent instanceof JOptionPane)) {
/* 386 */       pane = getOptionPane((JComponent)parent.getParent());
/*     */     } else {
/* 388 */       pane = (JOptionPane)parent;
/*     */     } 
/* 390 */     return pane;
/*     */   }
/*     */   
/*     */   public int getUserChoose() {
/* 394 */     return this.userChoose.get();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gde\\updater\UpdaterMessageView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */