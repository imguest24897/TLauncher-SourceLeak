/*    */ package com.beust.jcommander.defaultprovider;
/*    */ 
/*    */ import com.beust.jcommander.IDefaultProvider;
/*    */ import com.beust.jcommander.ParameterException;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import java.util.Properties;
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
/*    */ public class PropertyFileDefaultProvider
/*    */   implements IDefaultProvider
/*    */ {
/*    */   public static final String DEFAULT_FILE_NAME = "jcommander.properties";
/*    */   private Properties properties;
/*    */   
/*    */   public PropertyFileDefaultProvider() {
/* 38 */     init("jcommander.properties");
/*    */   }
/*    */   
/*    */   public PropertyFileDefaultProvider(String fileName) {
/* 42 */     init(fileName);
/*    */   }
/*    */   
/*    */   private void init(String fileName) {
/*    */     try {
/* 47 */       this.properties = new Properties();
/* 48 */       URL url = ClassLoader.getSystemResource(fileName);
/* 49 */       if (url != null) {
/* 50 */         this.properties.load(url.openStream());
/*    */       } else {
/* 52 */         throw new ParameterException("Could not find property file: " + fileName + " on the class path");
/*    */       }
/*    */     
/*    */     }
/* 56 */     catch (IOException e) {
/* 57 */       throw new ParameterException("Could not open property file: " + fileName);
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getDefaultValueFor(String optionName) {
/* 62 */     int index = 0;
/* 63 */     while (index < optionName.length() && !Character.isLetterOrDigit(optionName.charAt(index))) {
/* 64 */       index++;
/*    */     }
/* 66 */     String key = optionName.substring(index);
/* 67 */     return this.properties.getProperty(key);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\defaultprovider\PropertyFileDefaultProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */