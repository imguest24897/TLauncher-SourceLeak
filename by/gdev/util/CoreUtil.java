/*    */ package by.gdev.util;
/*    */ 
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import javax.imageio.ImageIO;
/*    */ import javax.swing.ImageIcon;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CoreUtil
/*    */ {
/*    */   public static ImageIcon getIcon(String uri) {
/* 14 */     return new ImageIcon(getImage(uri));
/*    */   }
/*    */   
/*    */   public static BufferedImage getImage(String uri) {
/*    */     try {
/* 19 */       return ImageIO.read(CoreUtil.class.getResource(uri));
/* 20 */     } catch (IOException e) {
/* 21 */       throw new RuntimeException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static File getDefaultWorkingDirectory() {
/* 27 */     return getSystemRelatedDirectory("tlauncher/starter");
/*    */   }
/*    */ 
/*    */   
/*    */   public static File getSystemRelatedDirectory(String path) {
/* 32 */     if (!OSInfo.getOSType().equals(OSInfo.OSType.MACOSX)) {
/* 33 */       path = '.' + path;
/*    */     }
/* 35 */     return getSystemRelatedFile(path);
/*    */   }
/*    */ 
/*    */   
/*    */   public static File getSystemRelatedFile(String path) {
/* 40 */     String applicationData, folder, userHome = System.getProperty("user.home", ".");
/*    */ 
/*    */     
/* 43 */     switch (OSInfo.getOSType())
/*    */     { case LINUX:
/*    */       case SOLARIS:
/* 46 */         file = new File(userHome, path);
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
/*    */ 
/*    */ 
/*    */         
/* 60 */         return file;case WINDOWS: applicationData = System.getenv("APPDATA"); folder = (applicationData != null) ? applicationData : userHome; file = new File(folder, path); return file;case MACOSX: file = new File(userHome, "Library/Application Support/" + path); return file; }  File file = new File(userHome, path); return file;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gde\\util\CoreUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */