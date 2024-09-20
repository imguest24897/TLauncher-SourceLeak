/*     */ package org.apache.commons.lang3.time;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.text.Format;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.TimeZone;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.apache.commons.lang3.LocaleUtils;
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
/*     */ abstract class FormatCache<F extends Format>
/*     */ {
/*     */   static final int NONE = -1;
/*  46 */   private final ConcurrentMap<ArrayKey, F> cInstanceCache = new ConcurrentHashMap<>(7);
/*     */   
/*  48 */   private static final ConcurrentMap<ArrayKey, String> cDateTimeInstanceCache = new ConcurrentHashMap<>(7);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public F getInstance() {
/*  57 */     return getDateTimeInstance(3, 3, TimeZone.getDefault(), Locale.getDefault());
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
/*     */   public F getInstance(String pattern, TimeZone timeZone, Locale locale) {
/*  73 */     Objects.requireNonNull(pattern, "pattern");
/*  74 */     TimeZone actualTimeZone = TimeZones.toTimeZone(timeZone);
/*  75 */     Locale actualLocale = LocaleUtils.toLocale(locale);
/*  76 */     ArrayKey key = new ArrayKey(new Object[] { pattern, actualTimeZone, actualLocale });
/*  77 */     return this.cInstanceCache.computeIfAbsent(key, k -> createInstance(pattern, actualTimeZone, actualLocale));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private F getDateTimeInstance(Integer dateStyle, Integer timeStyle, TimeZone timeZone, Locale locale) {
/* 108 */     locale = LocaleUtils.toLocale(locale);
/* 109 */     String pattern = getPatternForStyle(dateStyle, timeStyle, locale);
/* 110 */     return getInstance(pattern, timeZone, locale);
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
/*     */   F getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone, Locale locale) {
/* 128 */     return getDateTimeInstance(Integer.valueOf(dateStyle), Integer.valueOf(timeStyle), timeZone, locale);
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
/*     */   F getDateInstance(int dateStyle, TimeZone timeZone, Locale locale) {
/* 145 */     return getDateTimeInstance(Integer.valueOf(dateStyle), (Integer)null, timeZone, locale);
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
/*     */   F getTimeInstance(int timeStyle, TimeZone timeZone, Locale locale) {
/* 162 */     return getDateTimeInstance((Integer)null, Integer.valueOf(timeStyle), timeZone, locale);
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
/*     */   static String getPatternForStyle(Integer dateStyle, Integer timeStyle, Locale locale) {
/* 176 */     Locale safeLocale = LocaleUtils.toLocale(locale);
/* 177 */     ArrayKey key = new ArrayKey(new Object[] { dateStyle, timeStyle, safeLocale });
/* 178 */     return cDateTimeInstanceCache.computeIfAbsent(key, k -> {
/*     */           try {
/*     */             DateFormat formatter;
/*     */             if (dateStyle == null) {
/*     */               formatter = DateFormat.getTimeInstance(timeStyle.intValue(), safeLocale);
/*     */             } else if (timeStyle == null) {
/*     */               formatter = DateFormat.getDateInstance(dateStyle.intValue(), safeLocale);
/*     */             } else {
/*     */               formatter = DateFormat.getDateTimeInstance(dateStyle.intValue(), timeStyle.intValue(), safeLocale);
/*     */             } 
/*     */             return ((SimpleDateFormat)formatter).toPattern();
/* 189 */           } catch (ClassCastException ex) {
/*     */             throw new IllegalArgumentException("No date time pattern for locale: " + safeLocale);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   protected abstract F createInstance(String paramString, TimeZone paramTimeZone, Locale paramLocale);
/*     */   
/*     */   private static final class ArrayKey {
/*     */     private final Object[] keys;
/*     */     
/*     */     private static int computeHashCode(Object[] keys) {
/* 201 */       int prime = 31;
/* 202 */       int result = 1;
/* 203 */       result = 31 * result + Arrays.hashCode(keys);
/* 204 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int hashCode;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ArrayKey(Object... keys) {
/* 216 */       this.keys = keys;
/* 217 */       this.hashCode = computeHashCode(keys);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 222 */       return this.hashCode;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 227 */       if (this == obj) {
/* 228 */         return true;
/*     */       }
/* 230 */       if (obj == null) {
/* 231 */         return false;
/*     */       }
/* 233 */       if (getClass() != obj.getClass()) {
/* 234 */         return false;
/*     */       }
/* 236 */       ArrayKey other = (ArrayKey)obj;
/* 237 */       return Arrays.deepEquals(this.keys, other.keys);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\time\FormatCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */