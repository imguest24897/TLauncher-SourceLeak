/*    */ package org.apache.commons.text.lookup;
/*    */ 
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.StringWriter;
/*    */ import java.net.URL;
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
/*    */ final class UrlStringLookup
/*    */   extends AbstractStringLookup
/*    */ {
/* 43 */   static final UrlStringLookup INSTANCE = new UrlStringLookup();
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
/* 63 */     if (key == null) {
/* 64 */       return null;
/*    */     }
/* 66 */     String[] keys = key.split(SPLIT_STR);
/* 67 */     int keyLen = keys.length;
/* 68 */     if (keyLen < 2) {
/* 69 */       throw IllegalArgumentExceptions.format("Bad URL key format [%s]; expected format is DocumentPath:Key.", new Object[] { key });
/*    */     }
/*    */     
/* 72 */     String charsetName = keys[0];
/* 73 */     String urlStr = StringUtils.substringAfter(key, 58);
/*    */     try {
/* 75 */       URL url = new URL(urlStr);
/* 76 */       int size = 8192;
/* 77 */       StringWriter writer = new StringWriter(8192);
/* 78 */       char[] buffer = new char[8192];
/* 79 */       BufferedInputStream bis = new BufferedInputStream(url.openStream()); 
/* 80 */       try { InputStreamReader reader = new InputStreamReader(bis, charsetName); 
/*    */         try { int n;
/* 82 */           while (-1 != (n = reader.read(buffer))) {
/* 83 */             writer.write(buffer, 0, n);
/*    */           }
/* 85 */           reader.close(); } catch (Throwable throwable) { try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  bis.close(); } catch (Throwable throwable) { try { bis.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/* 86 */        return writer.toString();
/* 87 */     } catch (Exception e) {
/* 88 */       throw IllegalArgumentExceptions.format(e, "Error looking up URL [%s] with Charset [%s].", new Object[] { urlStr, charsetName });
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\lookup\UrlStringLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */