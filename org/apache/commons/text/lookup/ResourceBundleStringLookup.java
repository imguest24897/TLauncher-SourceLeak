/*     */ package org.apache.commons.text.lookup;
/*     */ 
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
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
/*     */ final class ResourceBundleStringLookup
/*     */   extends AbstractStringLookup
/*     */ {
/*  40 */   static final ResourceBundleStringLookup INSTANCE = new ResourceBundleStringLookup();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String bundleName;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ResourceBundleStringLookup() {
/*  51 */     this(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ResourceBundleStringLookup(String bundleName) {
/*  61 */     this.bundleName = bundleName;
/*     */   }
/*     */ 
/*     */   
/*     */   ResourceBundle getBundle(String keyBundleName) {
/*  66 */     return ResourceBundle.getBundle(keyBundleName);
/*     */   }
/*     */   
/*     */   String getString(String keyBundleName, String bundleKey) {
/*  70 */     return getBundle(keyBundleName).getString(bundleKey);
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
/*     */   public String lookup(String key) {
/*  86 */     if (key == null) {
/*  87 */       return null;
/*     */     }
/*  89 */     String[] keys = key.split(SPLIT_STR);
/*  90 */     int keyLen = keys.length;
/*  91 */     boolean anyBundle = (this.bundleName == null);
/*  92 */     if (anyBundle && keyLen != 2)
/*     */     {
/*  94 */       throw IllegalArgumentExceptions.format("Bad resource bundle key format [%s]; expected format is BundleName:KeyName.", new Object[] { key });
/*     */     }
/*  96 */     if (this.bundleName != null && keyLen != 1) {
/*  97 */       throw IllegalArgumentExceptions.format("Bad resource bundle key format [%s]; expected format is KeyName.", new Object[] { key });
/*     */     }
/*     */     
/* 100 */     String keyBundleName = anyBundle ? keys[0] : this.bundleName;
/* 101 */     String bundleKey = anyBundle ? keys[1] : keys[0];
/*     */     try {
/* 103 */       return getString(keyBundleName, bundleKey);
/* 104 */     } catch (MissingResourceException e) {
/*     */       
/* 106 */       return null;
/* 107 */     } catch (Exception e) {
/*     */       
/* 109 */       throw IllegalArgumentExceptions.format(e, "Error looking up resource bundle [%s] and key [%s].", new Object[] { keyBundleName, bundleKey });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 116 */     return super.toString() + " [bundleName=" + this.bundleName + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\lookup\ResourceBundleStringLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */