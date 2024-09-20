/*    */ package org.apache.commons.text.lookup;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Paths;
/*    */ import java.util.Properties;
/*    */ import org.apache.commons.lang3.StringUtils;
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
/*    */ final class PropertiesStringLookup
/*    */   extends AbstractStringLookup
/*    */ {
/* 47 */   static final PropertiesStringLookup INSTANCE = new PropertiesStringLookup();
/*    */ 
/*    */ 
/*    */   
/*    */   static final String SEPARATOR = "::";
/*    */ 
/*    */ 
/*    */   
/*    */   static String toPropertyKey(String file, String key) {
/* 56 */     return AbstractStringLookup.toLookupKey(file, "::", key);
/*    */   }
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
/*    */   public String lookup(String key) {
/* 80 */     if (key == null) {
/* 81 */       return null;
/*    */     }
/* 83 */     String[] keys = key.split("::");
/* 84 */     int keyLen = keys.length;
/* 85 */     if (keyLen < 2) {
/* 86 */       throw IllegalArgumentExceptions.format("Bad properties key format [%s]; expected format is %s.", new Object[] { key, 
/* 87 */             toPropertyKey("DocumentPath", "Key") });
/*    */     }
/* 89 */     String documentPath = keys[0];
/* 90 */     String propertyKey = StringUtils.substringAfter(key, "::");
/*    */     try {
/* 92 */       Properties properties = new Properties();
/* 93 */       InputStream inputStream = Files.newInputStream(Paths.get(documentPath, new String[0]), new java.nio.file.OpenOption[0]); 
/* 94 */       try { properties.load(inputStream);
/* 95 */         if (inputStream != null) inputStream.close();  } catch (Throwable throwable) { if (inputStream != null)
/* 96 */           try { inputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  return properties.getProperty(propertyKey);
/* 97 */     } catch (Exception e) {
/* 98 */       throw IllegalArgumentExceptions.format(e, "Error looking up properties [%s] and key [%s].", new Object[] { documentPath, propertyKey });
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\lookup\PropertiesStringLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */