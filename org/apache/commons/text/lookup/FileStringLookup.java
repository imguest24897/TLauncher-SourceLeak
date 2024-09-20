/*    */ package org.apache.commons.text.lookup;
/*    */ 
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Paths;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class FileStringLookup
/*    */   extends AbstractStringLookup
/*    */ {
/* 53 */   static final AbstractStringLookup INSTANCE = new FileStringLookup();
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
/* 73 */     if (key == null) {
/* 74 */       return null;
/*    */     }
/* 76 */     String[] keys = key.split(String.valueOf(':'));
/* 77 */     int keyLen = keys.length;
/* 78 */     if (keyLen < 2)
/*    */     {
/* 80 */       throw IllegalArgumentExceptions.format("Bad file key format [%s], expected format is CharsetName:DocumentPath.", new Object[] { key });
/*    */     }
/* 82 */     String charsetName = keys[0];
/* 83 */     String fileName = StringUtils.substringAfter(key, 58);
/*    */     try {
/* 85 */       return new String(Files.readAllBytes(Paths.get(fileName, new String[0])), charsetName);
/* 86 */     } catch (Exception e) {
/* 87 */       throw IllegalArgumentExceptions.format(e, "Error looking up file [%s] with charset [%s].", new Object[] { fileName, charsetName });
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\lookup\FileStringLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */