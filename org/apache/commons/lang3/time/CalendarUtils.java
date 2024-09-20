/*     */ package org.apache.commons.lang3.time;
/*     */ 
/*     */ import java.util.Calendar;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CalendarUtils
/*     */ {
/*  35 */   public static final CalendarUtils INSTANCE = new CalendarUtils(Calendar.getInstance());
/*     */ 
/*     */   
/*     */   private final Calendar calendar;
/*     */ 
/*     */   
/*     */   private final Locale locale;
/*     */ 
/*     */   
/*     */   static CalendarUtils getInstance(Locale locale) {
/*  45 */     return new CalendarUtils(Calendar.getInstance(locale), locale);
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
/*     */   public CalendarUtils(Calendar calendar) {
/*  58 */     this(calendar, Locale.getDefault());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CalendarUtils(Calendar calendar, Locale locale) {
/*  68 */     this.calendar = Objects.<Calendar>requireNonNull(calendar, "calendar");
/*  69 */     this.locale = Objects.<Locale>requireNonNull(locale, "locale");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDayOfMonth() {
/*  77 */     return this.calendar.get(5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDayOfYear() {
/*  87 */     return this.calendar.get(6);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMonth() {
/*  96 */     return this.calendar.get(2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String[] getMonthDisplayNames(int style) {
/* 107 */     Map<String, Integer> displayNames = this.calendar.getDisplayNames(2, style, this.locale);
/* 108 */     if (displayNames == null) {
/* 109 */       return null;
/*     */     }
/* 111 */     String[] monthNames = new String[displayNames.size()];
/* 112 */     displayNames.forEach((k, v) -> monthNames[v.intValue()] = k);
/* 113 */     return monthNames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String[] getStandaloneLongMonthNames() {
/* 121 */     return getMonthDisplayNames(32770);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String[] getStandaloneShortMonthNames() {
/* 129 */     return getMonthDisplayNames(32769);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getYear() {
/* 138 */     return this.calendar.get(1);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\time\CalendarUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */