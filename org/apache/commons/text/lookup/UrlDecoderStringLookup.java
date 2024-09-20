/*    */ package org.apache.commons.text.lookup;
/*    */ 
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.net.URLDecoder;
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
/*    */ 
/*    */ final class UrlDecoderStringLookup
/*    */   extends AbstractStringLookup
/*    */ {
/* 36 */   static final UrlDecoderStringLookup INSTANCE = new UrlDecoderStringLookup();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   String decode(String key, String enc) throws UnsupportedEncodingException {
/* 46 */     return URLDecoder.decode(key, enc);
/*    */   }
/*    */ 
/*    */   
/*    */   public String lookup(String key) {
/* 51 */     if (key == null) {
/* 52 */       return null;
/*    */     }
/* 54 */     String enc = StandardCharsets.UTF_8.name();
/*    */     try {
/* 56 */       return decode(key, enc);
/* 57 */     } catch (UnsupportedEncodingException e) {
/*    */       
/* 59 */       throw IllegalArgumentExceptions.format(e, "%s: source=%s, encoding=%s", new Object[] { e, key, enc });
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\lookup\UrlDecoderStringLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */