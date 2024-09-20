/*    */ package ch.qos.logback.core.net.ssl;
/*    */ 
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import java.security.NoSuchProviderException;
/*    */ import java.security.SecureRandom;
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
/*    */ public class SecureRandomFactoryBean
/*    */ {
/*    */   private String algorithm;
/*    */   private String provider;
/*    */   
/*    */   public SecureRandom createSecureRandom() throws NoSuchProviderException, NoSuchAlgorithmException {
/*    */     try {
/* 46 */       return (getProvider() != null) ? SecureRandom.getInstance(getAlgorithm(), getProvider()) : SecureRandom.getInstance(getAlgorithm());
/* 47 */     } catch (NoSuchProviderException ex) {
/* 48 */       throw new NoSuchProviderException("no such secure random provider: " + getProvider());
/* 49 */     } catch (NoSuchAlgorithmException ex) {
/* 50 */       throw new NoSuchAlgorithmException("no such secure random algorithm: " + getAlgorithm());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getAlgorithm() {
/* 61 */     if (this.algorithm == null) {
/* 62 */       return "SHA1PRNG";
/*    */     }
/* 64 */     return this.algorithm;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAlgorithm(String algorithm) {
/* 74 */     this.algorithm = algorithm;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getProvider() {
/* 82 */     return this.provider;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setProvider(String provider) {
/* 91 */     this.provider = provider;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\ch\qos\logback\core\net\ssl\SecureRandomFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */