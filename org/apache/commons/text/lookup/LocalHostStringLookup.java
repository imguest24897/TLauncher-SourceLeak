/*    */ package org.apache.commons.text.lookup;
/*    */ 
/*    */ import java.net.InetAddress;
/*    */ import java.net.UnknownHostException;
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
/*    */ final class LocalHostStringLookup
/*    */   extends AbstractStringLookup
/*    */ {
/* 40 */   static final LocalHostStringLookup INSTANCE = new LocalHostStringLookup();
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
/* 57 */     if (key == null) {
/* 58 */       return null;
/*    */     }
/*    */     try {
/* 61 */       switch (key) {
/*    */         case "name":
/* 63 */           return InetAddress.getLocalHost().getHostName();
/*    */         case "canonical-name":
/* 65 */           return InetAddress.getLocalHost().getCanonicalHostName();
/*    */         case "address":
/* 67 */           return InetAddress.getLocalHost().getHostAddress();
/*    */       } 
/* 69 */       throw new IllegalArgumentException(key);
/*    */     }
/* 71 */     catch (UnknownHostException e) {
/* 72 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\lookup\LocalHostStringLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */