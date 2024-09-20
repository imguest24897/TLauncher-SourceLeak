/*     */ package org.apache.commons.text.lookup;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import javax.xml.xpath.XPathFactory;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.xml.sax.InputSource;
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
/*     */ final class XmlStringLookup
/*     */   extends AbstractStringLookup
/*     */ {
/*  53 */   private static final Map<String, Boolean> DEFAULT_FEATURES = new HashMap<>(1); static {
/*  54 */     DEFAULT_FEATURES.put("http://javax.xml.XMLConstants/feature/secure-processing", Boolean.TRUE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   static final XmlStringLookup INSTANCE = new XmlStringLookup(DEFAULT_FEATURES);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<String, Boolean> xPathFactoryFeatures;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   XmlStringLookup(Map<String, Boolean> xPathFactoryFeatures) {
/*  74 */     this.xPathFactoryFeatures = Objects.<Map<String, Boolean>>requireNonNull(xPathFactoryFeatures, "xPathFfactoryFeatures");
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
/*     */   public String lookup(String key) {
/*  88 */     if (key == null) {
/*  89 */       return null;
/*     */     }
/*  91 */     String[] keys = key.split(SPLIT_STR);
/*  92 */     int keyLen = keys.length;
/*  93 */     if (keyLen != 2) {
/*  94 */       throw IllegalArgumentExceptions.format("Bad XML key format [%s]; expected format is DocumentPath:XPath.", new Object[] { key });
/*     */     }
/*     */     
/*  97 */     String documentPath = keys[0];
/*  98 */     String xpath = StringUtils.substringAfter(key, 58); 
/*  99 */     try { InputStream inputStream = Files.newInputStream(Paths.get(documentPath, new String[0]), new java.nio.file.OpenOption[0]); 
/* 100 */       try { XPathFactory factory = XPathFactory.newInstance();
/* 101 */         for (Map.Entry<String, Boolean> p : this.xPathFactoryFeatures.entrySet()) {
/* 102 */           factory.setFeature(p.getKey(), ((Boolean)p.getValue()).booleanValue());
/*     */         }
/* 104 */         String str = factory.newXPath().evaluate(xpath, new InputSource(inputStream));
/* 105 */         if (inputStream != null) inputStream.close();  return str; } catch (Throwable throwable) { if (inputStream != null) try { inputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
/* 106 */     { throw IllegalArgumentExceptions.format(e, "Error looking up XML document [%s] and XPath [%s].", new Object[] { documentPath, xpath }); }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\lookup\XmlStringLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */