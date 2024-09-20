/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import org.apache.commons.lang3.math.NumberUtils;
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
/*     */ public enum JavaVersion
/*     */ {
/*  33 */   JAVA_0_9(1.5F, "0.9"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  38 */   JAVA_1_1(1.1F, "1.1"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   JAVA_1_2(1.2F, "1.2"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   JAVA_1_3(1.3F, "1.3"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   JAVA_1_4(1.4F, "1.4"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   JAVA_1_5(1.5F, "1.5"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   JAVA_1_6(1.6F, "1.6"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   JAVA_1_7(1.7F, "1.7"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   JAVA_1_8(1.8F, "1.8"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   JAVA_1_9(9.0F, "9"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   JAVA_9(9.0F, "9"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   JAVA_10(10.0F, "10"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   JAVA_11(11.0F, "11"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   JAVA_12(12.0F, "12"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   JAVA_13(13.0F, "13"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   JAVA_14(14.0F, "14"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   JAVA_15(15.0F, "15"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   JAVA_16(16.0F, "16"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 144 */   JAVA_17(17.0F, "17"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 151 */   JAVA_18(18.0F, "18"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 158 */   JAVA_19(19.0F, "19"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 165 */   JAVA_20(20.0F, "20"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 172 */   JAVA_21(21.0F, "21"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 177 */   JAVA_RECENT(maxVersion(), Float.toString(maxVersion()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final float value;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JavaVersion(float value, String name) {
/* 196 */     this.value = value;
/* 197 */     this.name = name;
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
/*     */   public boolean atLeast(JavaVersion requiredVersion) {
/* 210 */     return (this.value >= requiredVersion.value);
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
/*     */   public boolean atMost(JavaVersion requiredVersion) {
/* 224 */     return (this.value <= requiredVersion.value);
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
/*     */   static JavaVersion getJavaVersion(String versionStr) {
/* 238 */     return get(versionStr);
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
/*     */   static JavaVersion get(String versionStr) {
/* 251 */     if (versionStr == null) {
/* 252 */       return null;
/*     */     }
/* 254 */     switch (versionStr) {
/*     */       case "0.9":
/* 256 */         return JAVA_0_9;
/*     */       case "1.1":
/* 258 */         return JAVA_1_1;
/*     */       case "1.2":
/* 260 */         return JAVA_1_2;
/*     */       case "1.3":
/* 262 */         return JAVA_1_3;
/*     */       case "1.4":
/* 264 */         return JAVA_1_4;
/*     */       case "1.5":
/* 266 */         return JAVA_1_5;
/*     */       case "1.6":
/* 268 */         return JAVA_1_6;
/*     */       case "1.7":
/* 270 */         return JAVA_1_7;
/*     */       case "1.8":
/* 272 */         return JAVA_1_8;
/*     */       case "9":
/* 274 */         return JAVA_9;
/*     */       case "10":
/* 276 */         return JAVA_10;
/*     */       case "11":
/* 278 */         return JAVA_11;
/*     */       case "12":
/* 280 */         return JAVA_12;
/*     */       case "13":
/* 282 */         return JAVA_13;
/*     */       case "14":
/* 284 */         return JAVA_14;
/*     */       case "15":
/* 286 */         return JAVA_15;
/*     */       case "16":
/* 288 */         return JAVA_16;
/*     */       case "17":
/* 290 */         return JAVA_17;
/*     */       case "18":
/* 292 */         return JAVA_18;
/*     */       case "19":
/* 294 */         return JAVA_19;
/*     */       case "20":
/* 296 */         return JAVA_20;
/*     */       case "21":
/* 298 */         return JAVA_21;
/*     */     } 
/* 300 */     float v = toFloatVersion(versionStr);
/* 301 */     if (v - 1.0D < 1.0D) {
/* 302 */       int firstComma = Math.max(versionStr.indexOf('.'), versionStr.indexOf(','));
/* 303 */       int end = Math.max(versionStr.length(), versionStr.indexOf(',', firstComma));
/* 304 */       if (Float.parseFloat(versionStr.substring(firstComma + 1, end)) > 0.9F) {
/* 305 */         return JAVA_RECENT;
/*     */       }
/* 307 */     } else if (v > 10.0F) {
/* 308 */       return JAVA_RECENT;
/*     */     } 
/* 310 */     return null;
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
/*     */   public String toString() {
/* 323 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static float maxVersion() {
/* 332 */     float v = toFloatVersion(System.getProperty("java.specification.version", "99.0"));
/* 333 */     return (v > 0.0F) ? v : 99.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static float toFloatVersion(String value) {
/* 343 */     int defaultReturnValue = -1;
/* 344 */     if (!value.contains(".")) {
/* 345 */       return NumberUtils.toFloat(value, -1.0F);
/*     */     }
/* 347 */     String[] toParse = value.split("\\.");
/* 348 */     if (toParse.length >= 2) {
/* 349 */       return NumberUtils.toFloat(toParse[0] + '.' + toParse[1], -1.0F);
/*     */     }
/* 351 */     return -1.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\JavaVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */