/*     */ package org.apache.commons.text.lookup;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class DnsStringLookup
/*     */   extends AbstractStringLookup
/*     */ {
/*  64 */   static final DnsStringLookup INSTANCE = new DnsStringLookup();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String lookup(String key) {
/*  81 */     if (key == null) {
/*  82 */       return null;
/*     */     }
/*  84 */     String[] keys = key.trim().split("\\|");
/*  85 */     int keyLen = keys.length;
/*  86 */     String subKey = keys[0].trim();
/*  87 */     String subValue = (keyLen < 2) ? key : keys[1].trim();
/*     */     try {
/*  89 */       InetAddress inetAddress = InetAddress.getByName(subValue);
/*  90 */       switch (subKey) {
/*     */         case "name":
/*  92 */           return inetAddress.getHostName();
/*     */         case "canonical-name":
/*  94 */           return inetAddress.getCanonicalHostName();
/*     */         case "address":
/*  96 */           return inetAddress.getHostAddress();
/*     */       } 
/*  98 */       return inetAddress.getHostAddress();
/*     */     }
/* 100 */     catch (UnknownHostException e) {
/* 101 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\lookup\DnsStringLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */