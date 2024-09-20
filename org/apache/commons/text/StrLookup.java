/*     */ package org.apache.commons.text;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.ResourceBundle;
/*     */ import org.apache.commons.text.lookup.StringLookup;
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
/*     */ @Deprecated
/*     */ public abstract class StrLookup<V>
/*     */   implements StringLookup
/*     */ {
/*     */   static class MapStrLookup<V>
/*     */     extends StrLookup<V>
/*     */   {
/*     */     private final Map<String, V> map;
/*     */     
/*     */     MapStrLookup(Map<String, V> map) {
/*  65 */       this.map = (map != null) ? map : Collections.<String, V>emptyMap();
/*     */     }
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
/*     */     public String lookup(String key) {
/*  79 */       return Objects.toString(this.map.get(key), null);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  84 */       return super.toString() + " [map=" + this.map + "]";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ResourceBundleLookup
/*     */     extends StrLookup<String>
/*     */   {
/*     */     private final ResourceBundle resourceBundle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private ResourceBundleLookup(ResourceBundle resourceBundle) {
/* 102 */       this.resourceBundle = resourceBundle;
/*     */     }
/*     */ 
/*     */     
/*     */     public String lookup(String key) {
/* 107 */       if (this.resourceBundle == null || key == null || !this.resourceBundle.containsKey(key)) {
/* 108 */         return null;
/*     */       }
/* 110 */       return this.resourceBundle.getString(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 115 */       return super.toString() + " [resourceBundle=" + this.resourceBundle + "]";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class SystemPropertiesStrLookup
/*     */     extends StrLookup<String>
/*     */   {
/*     */     private SystemPropertiesStrLookup() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public String lookup(String key) {
/* 129 */       if (!key.isEmpty()) {
/*     */         try {
/* 131 */           return System.getProperty(key);
/* 132 */         } catch (SecurityException securityException) {}
/*     */       }
/*     */ 
/*     */       
/* 136 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   private static final StrLookup<String> NONE_LOOKUP = new MapStrLookup<>(null);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 148 */   private static final StrLookup<String> SYSTEM_PROPERTIES_LOOKUP = new SystemPropertiesStrLookup();
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
/*     */   public static <V> StrLookup<V> mapLookup(Map<String, V> map) {
/* 162 */     return new MapStrLookup<>(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StrLookup<?> noneLookup() {
/* 171 */     return NONE_LOOKUP;
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
/*     */   public static StrLookup<String> resourceBundleLookup(ResourceBundle resourceBundle) {
/* 186 */     return new ResourceBundleLookup(resourceBundle);
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
/*     */   public static StrLookup<String> systemPropertiesLookup() {
/* 201 */     return SYSTEM_PROPERTIES_LOOKUP;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\StrLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */