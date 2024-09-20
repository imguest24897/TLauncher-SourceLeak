/*    */ package by.gdev.handler;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Locale;
/*    */ import java.util.Properties;
/*    */ import org.apache.commons.lang3.ArrayUtils;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class Localise {
/* 12 */   private static final Logger log = LoggerFactory.getLogger(Localise.class);
/*    */   
/*    */   static Locale locale;
/*    */ 
/*    */   
/*    */   public Locale getLocal(String lang) {
/*    */     try {
/* 19 */       Properties property = new Properties();
/* 20 */       ClassLoader classloader = Thread.currentThread().getContextClassLoader();
/* 21 */       InputStream inputStream = classloader.getResourceAsStream("application.properties");
/* 22 */       property.load(inputStream);
/* 23 */       String language = property.getProperty("language");
/* 24 */       String[] parts = language.split(",");
/* 25 */       if (ArrayUtils.contains((Object[])parts, lang))
/* 26 */       { locale = (new Locale.Builder()).setLanguage(lang).build(); }
/*    */       else
/* 28 */       { locale = (new Locale.Builder()).setLanguage("en").build(); } 
/* 29 */     } catch (IOException e) {
/* 30 */       log.error("Error", e);
/*    */     } 
/* 32 */     return locale;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\handler\Localise.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */