/*    */ package org.apache.commons.text.lookup;
/*    */ 
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.net.URLEncoder;
/*    */ import java.nio.charset.StandardCharsets;
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
/*    */ final class UrlEncoderStringLookup
/*    */   extends AbstractStringLookup
/*    */ {
/* 35 */   static final UrlEncoderStringLookup INSTANCE = new UrlEncoderStringLookup();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   String encode(String key, String enc) throws UnsupportedEncodingException {
/* 45 */     return URLEncoder.encode(key, enc);
/*    */   }
/*    */ 
/*    */   
/*    */   public String lookup(String key) {
/* 50 */     if (key == null) {
/* 51 */       return null;
/*    */     }
/* 53 */     String enc = StandardCharsets.UTF_8.name();
/*    */     try {
/* 55 */       return encode(key, enc);
/* 56 */     } catch (UnsupportedEncodingException e) {
/*    */       
/* 58 */       throw IllegalArgumentExceptions.format(e, "%s: source=%s, encoding=%s", new Object[] { e, key, enc });
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\lookup\UrlEncoderStringLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */