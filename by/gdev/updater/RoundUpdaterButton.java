/*    */ package by.gdev.updater;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.FontMetrics;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Rectangle;
/*    */ import java.awt.RenderingHints;
/*    */ import java.awt.event.MouseAdapter;
/*    */ import java.awt.event.MouseEvent;
/*    */ import java.awt.geom.Rectangle2D;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JComponent;
/*    */ 
/*    */ public class RoundUpdaterButton extends JButton {
/*    */   private static final long serialVersionUID = 1L;
/* 18 */   public Color TEXT_COLOR = Color.white;
/* 19 */   int ARC_SIZE = 14;
/*    */   
/*    */   public RoundUpdaterButton(final Color background, final Color mouseUnder, String value, Color color, final int code, final AtomicInteger userChoose) {
/* 22 */     setText(value);
/* 23 */     setBackground(color);
/* 24 */     setForeground(Color.BLACK);
/* 25 */     setOpaque(true);
/* 26 */     addMouseListener(new MouseAdapter()
/*    */         {
/*    */           public void mouseEntered(MouseEvent e) {
/* 29 */             userChoose.set(code);
/* 30 */             RoundUpdaterButton.this.setBackground(mouseUnder);
/*    */           }
/*    */ 
/*    */           
/*    */           public void mouseExited(MouseEvent e) {
/* 35 */             RoundUpdaterButton.this.setBackground(background);
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintComponent(Graphics g0) {
/* 42 */     int x = 0;
/* 43 */     Graphics2D g = (Graphics2D)g0;
/* 44 */     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*    */     
/* 46 */     g.setColor(getBackground());
/* 47 */     g.fillRoundRect(x, x, getWidth(), getHeight(), this.ARC_SIZE, this.ARC_SIZE);
/*    */     
/* 49 */     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
/* 50 */     g0.setColor(this.TEXT_COLOR);
/* 51 */     paintText(g0, this, getVisibleRect(), getText());
/*    */   }
/*    */   
/*    */   protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
/* 55 */     Graphics2D g2d = (Graphics2D)g;
/* 56 */     g2d.setFont(getFont());
/* 57 */     g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/* 58 */     FontMetrics fm = g2d.getFontMetrics();
/* 59 */     Rectangle2D r = fm.getStringBounds(text, g2d);
/* 60 */     int x = (getWidth() - (int)r.getWidth()) / 2;
/* 61 */     int y = (getHeight() - (int)r.getHeight()) / 2 + fm.getAscent();
/* 62 */     g2d.drawString(text, x, y);
/* 63 */     g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gde\\updater\RoundUpdaterButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */