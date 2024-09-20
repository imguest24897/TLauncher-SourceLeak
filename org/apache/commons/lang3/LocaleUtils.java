/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocaleUtils
/*     */ {
/*     */   private static final char UNDERSCORE = '_';
/*     */   private static final char DASH = '-';
/*     */   
/*     */   static class SyncAvoid
/*     */   {
/*     */     private static final List<Locale> AVAILABLE_LOCALE_LIST;
/*     */     private static final Set<Locale> AVAILABLE_LOCALE_SET;
/*     */     
/*     */     static {
/*  52 */       List<Locale> list = new ArrayList<>(Arrays.asList(Locale.getAvailableLocales()));
/*  53 */       AVAILABLE_LOCALE_LIST = Collections.unmodifiableList(list);
/*  54 */       AVAILABLE_LOCALE_SET = Collections.unmodifiableSet(new HashSet<>(list));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  59 */   private static final ConcurrentMap<String, List<Locale>> cLanguagesByCountry = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */   
/*  63 */   private static final ConcurrentMap<String, List<Locale>> cCountriesByLanguage = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Locale> availableLocaleList() {
/*  76 */     return SyncAvoid.AVAILABLE_LOCALE_LIST;
/*     */   }
/*     */   
/*     */   private static List<Locale> availableLocaleList(Predicate<Locale> predicate) {
/*  80 */     return (List<Locale>)availableLocaleList().stream().filter(predicate).collect(Collectors.toList());
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
/*     */   public static Set<Locale> availableLocaleSet() {
/*  93 */     return SyncAvoid.AVAILABLE_LOCALE_SET;
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
/*     */   public static List<Locale> countriesByLanguage(String languageCode) {
/* 106 */     if (languageCode == null) {
/* 107 */       return Collections.emptyList();
/*     */     }
/* 109 */     return cCountriesByLanguage.computeIfAbsent(languageCode, lc -> Collections.unmodifiableList(availableLocaleList(())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAvailableLocale(Locale locale) {
/* 120 */     return availableLocaleSet().contains(locale);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isISO3166CountryCode(String str) {
/* 130 */     return (StringUtils.isAllUpperCase(str) && str.length() == 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isISO639LanguageCode(String str) {
/* 140 */     return (StringUtils.isAllLowerCase(str) && (str.length() == 2 || str.length() == 3));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isNumericAreaCode(String str) {
/* 150 */     return (StringUtils.isNumeric(str) && str.length() == 3);
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
/*     */   public static List<Locale> languagesByCountry(String countryCode) {
/* 163 */     if (countryCode == null) {
/* 164 */       return Collections.emptyList();
/*     */     }
/* 166 */     return cLanguagesByCountry.computeIfAbsent(countryCode, k -> Collections.unmodifiableList(availableLocaleList(())));
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
/*     */   public static List<Locale> localeLookupList(Locale locale) {
/* 183 */     return localeLookupList(locale, locale);
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
/*     */   public static List<Locale> localeLookupList(Locale locale, Locale defaultLocale) {
/* 204 */     List<Locale> list = new ArrayList<>(4);
/* 205 */     if (locale != null) {
/* 206 */       list.add(locale);
/* 207 */       if (!locale.getVariant().isEmpty()) {
/* 208 */         list.add(new Locale(locale.getLanguage(), locale.getCountry()));
/*     */       }
/* 210 */       if (!locale.getCountry().isEmpty()) {
/* 211 */         list.add(new Locale(locale.getLanguage(), ""));
/*     */       }
/* 213 */       if (!list.contains(defaultLocale)) {
/* 214 */         list.add(defaultLocale);
/*     */       }
/*     */     } 
/* 217 */     return Collections.unmodifiableList(list);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Locale parseLocale(String str) {
/* 228 */     if (isISO639LanguageCode(str)) {
/* 229 */       return new Locale(str);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 234 */     String[] segments = (str.indexOf('_') != -1) ? str.split(String.valueOf('_'), -1) : str.split(String.valueOf('-'), -1);
/* 235 */     String language = segments[0];
/* 236 */     if (segments.length == 2) {
/* 237 */       String country = segments[1];
/* 238 */       if ((isISO639LanguageCode(language) && isISO3166CountryCode(country)) || 
/* 239 */         isNumericAreaCode(country)) {
/* 240 */         return new Locale(language, country);
/*     */       }
/* 242 */     } else if (segments.length == 3) {
/* 243 */       String country = segments[1];
/* 244 */       String variant = segments[2];
/* 245 */       if (isISO639LanguageCode(language) && (country
/* 246 */         .isEmpty() || isISO3166CountryCode(country) || isNumericAreaCode(country)) && 
/* 247 */         !variant.isEmpty()) {
/* 248 */         return new Locale(language, country, variant);
/*     */       }
/*     */     } 
/* 251 */     throw new IllegalArgumentException("Invalid locale format: " + str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Locale toLocale(Locale locale) {
/* 262 */     return (locale != null) ? locale : Locale.getDefault();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Locale toLocale(String str) {
/* 297 */     if (str == null)
/*     */     {
/* 299 */       return null;
/*     */     }
/* 301 */     if (str.isEmpty()) {
/* 302 */       return new Locale("", "");
/*     */     }
/* 304 */     if (str.contains("#")) {
/* 305 */       throw new IllegalArgumentException("Invalid locale format: " + str);
/*     */     }
/* 307 */     int len = str.length();
/* 308 */     if (len < 2) {
/* 309 */       throw new IllegalArgumentException("Invalid locale format: " + str);
/*     */     }
/* 311 */     char ch0 = str.charAt(0);
/* 312 */     if (ch0 == '_' || ch0 == '-') {
/* 313 */       if (len < 3) {
/* 314 */         throw new IllegalArgumentException("Invalid locale format: " + str);
/*     */       }
/* 316 */       char ch1 = str.charAt(1);
/* 317 */       char ch2 = str.charAt(2);
/* 318 */       if (!Character.isUpperCase(ch1) || !Character.isUpperCase(ch2)) {
/* 319 */         throw new IllegalArgumentException("Invalid locale format: " + str);
/*     */       }
/* 321 */       if (len == 3) {
/* 322 */         return new Locale("", str.substring(1, 3));
/*     */       }
/* 324 */       if (len < 5) {
/* 325 */         throw new IllegalArgumentException("Invalid locale format: " + str);
/*     */       }
/* 327 */       if (str.charAt(3) != ch0) {
/* 328 */         throw new IllegalArgumentException("Invalid locale format: " + str);
/*     */       }
/* 330 */       return new Locale("", str.substring(1, 3), str.substring(4));
/*     */     } 
/*     */     
/* 333 */     return parseLocale(str);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\LocaleUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */