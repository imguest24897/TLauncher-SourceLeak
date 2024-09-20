/*    */ package by.gdev.updater;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Rectangle;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.util.Objects;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.plaf.basic.BasicProgressBarUI;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class PreloaderProgressUI extends BasicProgressBarUI {
/* 15 */   private static final Logger log = LoggerFactory.getLogger(PreloaderProgressUI.class);
/*    */   
/* 17 */   public final Color border = new Color(156, 155, 155);
/* 18 */   public final Color bottomBorderLine = new Color(146, 154, 140);
/* 19 */   public final Color REST_COLOR = new Color(200, 203, 199);
/*    */   
/*    */   public static final int PROGRESS_HEIGHT = 24;
/*    */   public static final int PROGRESS_BAR_WIDTH = 40;
/*    */   BufferedImage bottom;
/*    */   BufferedImage top;
/*    */   
/*    */   public PreloaderProgressUI(BufferedImage bottom, BufferedImage top) {
/* 27 */     this.bottom = bottom;
/* 28 */     this.top = top;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintDeterminate(Graphics g, JComponent c) {
/* 33 */     Graphics2D g2d = (Graphics2D)g.create();
/* 34 */     Rectangle rec = this.progressBar.getVisibleRect();
/* 35 */     double complete = this.progressBar.getPercentComplete();
/* 36 */     int width = this.progressBar.getWidth();
/*    */     
/* 38 */     int completeWidth = (int)(complete * this.bottom.getWidth());
/*    */     
/* 40 */     g2d.setColor(this.REST_COLOR);
/* 41 */     g2d.fillRect(rec.x, rec.y, width, this.bottom.getHeight());
/*    */ 
/*    */     
/* 44 */     if (completeWidth > 0) {
/* 45 */       g2d.drawImage(this.bottom, completeWidth, rec.y, width, this.bottom.getHeight(), null);
/* 46 */       g2d.drawImage(this.top.getSubimage(rec.x, rec.y, completeWidth, 24), rec.x, rec.y, completeWidth, this.bottom.getHeight(), null);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintIndeterminate(Graphics g, JComponent c) {
/* 52 */     if (!(g instanceof Graphics2D)) {
/*    */       return;
/*    */     }
/* 55 */     Rectangle rec = null;
/*    */     try {
/* 57 */       Graphics2D g2d = (Graphics2D)g;
/* 58 */       rec = this.progressBar.getVisibleRect();
/* 59 */       this.boxRect = getBox(this.boxRect);
/* 60 */       g2d.drawImage(this.bottom, rec.x, rec.y, this.bottom.getWidth(), this.bottom.getHeight(), null);
/* 61 */       g2d.drawImage(this.top, this.boxRect.x, this.boxRect.y, this.boxRect.width, this.boxRect.height, null);
/* 62 */     } catch (NullPointerException e) {
/* 63 */       log.info("bottom is null " + Objects.isNull(this.bottom));
/* 64 */       log.info("rec is null " + Objects.isNull(rec));
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Rectangle getBox(Rectangle r) {
/* 71 */     r.x += 4;
/* 72 */     if (r.x > this.progressBar.getWidth()) {
/* 73 */       r.x = 0;
/*    */     }
/* 75 */     r.height = 24;
/* 76 */     r.width = 40;
/* 77 */     return r;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gde\\updater\PreloaderProgressUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */