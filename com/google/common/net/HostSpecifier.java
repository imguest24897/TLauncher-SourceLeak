/*     */ package com.google.common.net;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.net.InetAddress;
/*     */ import java.text.ParseException;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class HostSpecifier
/*     */ {
/*     */   private final String canonicalForm;
/*     */   
/*     */   private HostSpecifier(String canonicalForm) {
/*  51 */     this.canonicalForm = canonicalForm;
/*     */   }
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
/*     */   public static HostSpecifier fromValid(String specifier) {
/*  73 */     HostAndPort parsedHost = HostAndPort.fromString(specifier);
/*  74 */     Preconditions.checkArgument(!parsedHost.hasPort());
/*  75 */     String host = parsedHost.getHost();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     InetAddress addr = null;
/*     */     try {
/*  83 */       addr = InetAddresses.forString(host);
/*  84 */     } catch (IllegalArgumentException illegalArgumentException) {}
/*     */ 
/*     */ 
/*     */     
/*  88 */     if (addr != null) {
/*  89 */       return new HostSpecifier(InetAddresses.toUriString(addr));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  95 */     InternetDomainName domain = InternetDomainName.from(host);
/*     */     
/*  97 */     if (domain.hasPublicSuffix()) {
/*  98 */       return new HostSpecifier(domain.toString());
/*     */     }
/*     */     
/* 101 */     String.valueOf(host); throw new IllegalArgumentException((String.valueOf(host).length() != 0) ? "Domain name does not have a recognized public suffix: ".concat(String.valueOf(host)) : new String("Domain name does not have a recognized public suffix: "));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HostSpecifier from(String specifier) throws ParseException {
/*     */     try {
/* 114 */       return fromValid(specifier);
/* 115 */     } catch (IllegalArgumentException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 120 */       String.valueOf(specifier); ParseException parseException = new ParseException((String.valueOf(specifier).length() != 0) ? "Invalid host specifier: ".concat(String.valueOf(specifier)) : new String("Invalid host specifier: "), 0);
/* 121 */       parseException.initCause(e);
/* 122 */       throw parseException;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isValid(String specifier) {
/*     */     try {
/* 132 */       fromValid(specifier);
/* 133 */       return true;
/* 134 */     } catch (IllegalArgumentException e) {
/* 135 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 141 */     if (this == other) {
/* 142 */       return true;
/*     */     }
/*     */     
/* 145 */     if (other instanceof HostSpecifier) {
/* 146 */       HostSpecifier that = (HostSpecifier)other;
/* 147 */       return this.canonicalForm.equals(that.canonicalForm);
/*     */     } 
/*     */     
/* 150 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 155 */     return this.canonicalForm.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 166 */     return this.canonicalForm;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\net\HostSpecifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */