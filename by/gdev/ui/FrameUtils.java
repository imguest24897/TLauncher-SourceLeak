/*    */ package by.gdev.ui;
/*    */ 
/*    */ import java.awt.Image;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.imageio.ImageIO;
/*    */ import javax.swing.JFrame;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class FrameUtils {
/* 14 */   private static final Logger log = LoggerFactory.getLogger(FrameUtils.class);
/*    */ 
/*    */   
/*    */   public static void setFavicons(JFrame frame) {
/*    */     try {
/* 19 */       List<Image> favicons = new ArrayList<>();
/* 20 */       int[] sizes = { 256, 128, 96, 64, 48, 32, 24, 16 };
/* 21 */       StringBuilder loadedBuilder = new StringBuilder();
/* 22 */       for (int i : sizes) {
/* 23 */         BufferedImage image = ImageIO.read(StarterStatusFrame.class.getResourceAsStream("/logo.jpg"));
/* 24 */         if (image != null) {
/*    */           
/* 26 */           loadedBuilder.append(", ").append(i).append("px");
/* 27 */           favicons.add(image);
/*    */         } 
/* 29 */       }  String loaded = loadedBuilder.toString();
/* 30 */       if (loaded.isEmpty()) {
/* 31 */         log.info("No favicon is loaded.");
/*    */       } else {
/* 33 */         log.info("Favicons loaded:", loaded.substring(2));
/* 34 */       }  frame.setIconImages(favicons);
/* 35 */     } catch (IOException e) {
/* 36 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gde\\ui\FrameUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */