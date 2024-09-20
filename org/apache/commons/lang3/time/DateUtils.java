/*      */ package org.apache.commons.lang3.time;
/*      */ 
/*      */ import java.text.ParseException;
/*      */ import java.text.ParsePosition;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Objects;
/*      */ import java.util.TimeZone;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import org.apache.commons.lang3.LocaleUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DateUtils
/*      */ {
/*      */   public static final long MILLIS_PER_SECOND = 1000L;
/*      */   public static final long MILLIS_PER_MINUTE = 60000L;
/*      */   public static final long MILLIS_PER_HOUR = 3600000L;
/*      */   public static final long MILLIS_PER_DAY = 86400000L;
/*      */   public static final int SEMI_MONTH = 1001;
/*   84 */   private static final int[][] fields = new int[][] { { 14 }, { 13 }, { 12 }, { 11, 10 }, { 5, 5, 9 }, { 2, 1001 }, { 1 }, { 0 } };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int RANGE_WEEK_SUNDAY = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int RANGE_WEEK_MONDAY = 2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int RANGE_WEEK_RELATIVE = 3;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int RANGE_WEEK_CENTER = 4;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int RANGE_MONTH_SUNDAY = 5;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int RANGE_MONTH_MONDAY = 6;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private enum ModifyType
/*      */   {
/*  128 */     TRUNCATE,
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  133 */     ROUND,
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  138 */     CEILING;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSameDay(Date date1, Date date2) {
/*  166 */     return isSameDay(toCalendar(date1), toCalendar(date2));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSameDay(Calendar cal1, Calendar cal2) {
/*  183 */     Objects.requireNonNull(cal1, "cal1");
/*  184 */     Objects.requireNonNull(cal2, "cal2");
/*  185 */     return (cal1.get(0) == cal2.get(0) && cal1
/*  186 */       .get(1) == cal2.get(1) && cal1
/*  187 */       .get(6) == cal2.get(6));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSameInstant(Date date1, Date date2) {
/*  202 */     Objects.requireNonNull(date1, "date1");
/*  203 */     Objects.requireNonNull(date2, "date2");
/*  204 */     return (date1.getTime() == date2.getTime());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSameInstant(Calendar cal1, Calendar cal2) {
/*  219 */     Objects.requireNonNull(cal1, "cal1");
/*  220 */     Objects.requireNonNull(cal2, "cal2");
/*  221 */     return (cal1.getTime().getTime() == cal2.getTime().getTime());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
/*  237 */     Objects.requireNonNull(cal1, "cal1");
/*  238 */     Objects.requireNonNull(cal2, "cal2");
/*  239 */     return (cal1.get(14) == cal2.get(14) && cal1
/*  240 */       .get(13) == cal2.get(13) && cal1
/*  241 */       .get(12) == cal2.get(12) && cal1
/*  242 */       .get(11) == cal2.get(11) && cal1
/*  243 */       .get(6) == cal2.get(6) && cal1
/*  244 */       .get(1) == cal2.get(1) && cal1
/*  245 */       .get(0) == cal2.get(0) && cal1
/*  246 */       .getClass() == cal2.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date parseDate(String str, String... parsePatterns) throws ParseException {
/*  264 */     return parseDate(str, null, parsePatterns);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date parseDate(String str, Locale locale, String... parsePatterns) throws ParseException {
/*  286 */     return parseDateWithLeniency(str, locale, parsePatterns, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date parseDateStrictly(String str, String... parsePatterns) throws ParseException {
/*  305 */     return parseDateStrictly(str, null, parsePatterns);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date parseDateStrictly(String str, Locale locale, String... parsePatterns) throws ParseException {
/*  327 */     return parseDateWithLeniency(str, locale, parsePatterns, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Date parseDateWithLeniency(String dateStr, Locale locale, String[] parsePatterns, boolean lenient) throws ParseException {
/*  349 */     Objects.requireNonNull(dateStr, "str");
/*  350 */     Objects.requireNonNull(parsePatterns, "parsePatterns");
/*      */     
/*  352 */     TimeZone tz = TimeZone.getDefault();
/*  353 */     Locale lcl = LocaleUtils.toLocale(locale);
/*  354 */     ParsePosition pos = new ParsePosition(0);
/*  355 */     Calendar calendar = Calendar.getInstance(tz, lcl);
/*  356 */     calendar.setLenient(lenient);
/*      */     
/*  358 */     for (String parsePattern : parsePatterns) {
/*  359 */       FastDateParser fdp = new FastDateParser(parsePattern, tz, lcl);
/*  360 */       calendar.clear();
/*      */       try {
/*  362 */         if (fdp.parse(dateStr, pos, calendar) && pos.getIndex() == dateStr.length()) {
/*  363 */           return calendar.getTime();
/*      */         }
/*  365 */       } catch (IllegalArgumentException illegalArgumentException) {}
/*      */ 
/*      */       
/*  368 */       pos.setIndex(0);
/*      */     } 
/*  370 */     throw new ParseException("Unable to parse the date: " + dateStr, -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date addYears(Date date, int amount) {
/*  383 */     return add(date, 1, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date addMonths(Date date, int amount) {
/*  396 */     return add(date, 2, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date addWeeks(Date date, int amount) {
/*  409 */     return add(date, 3, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date addDays(Date date, int amount) {
/*  422 */     return add(date, 5, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date addHours(Date date, int amount) {
/*  435 */     return add(date, 11, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date addMinutes(Date date, int amount) {
/*  448 */     return add(date, 12, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date addSeconds(Date date, int amount) {
/*  461 */     return add(date, 13, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date addMilliseconds(Date date, int amount) {
/*  474 */     return add(date, 14, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Date add(Date date, int calendarField, int amount) {
/*  488 */     validateDateNotNull(date);
/*  489 */     Calendar c = Calendar.getInstance();
/*  490 */     c.setTime(date);
/*  491 */     c.add(calendarField, amount);
/*  492 */     return c.getTime();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date setYears(Date date, int amount) {
/*  506 */     return set(date, 1, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date setMonths(Date date, int amount) {
/*  522 */     return set(date, 2, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date setDays(Date date, int amount) {
/*  538 */     return set(date, 5, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date setHours(Date date, int amount) {
/*  555 */     return set(date, 11, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date setMinutes(Date date, int amount) {
/*  571 */     return set(date, 12, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date setSeconds(Date date, int amount) {
/*  587 */     return set(date, 13, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date setMilliseconds(Date date, int amount) {
/*  603 */     return set(date, 14, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Date set(Date date, int calendarField, int amount) {
/*  619 */     validateDateNotNull(date);
/*      */     
/*  621 */     Calendar c = Calendar.getInstance();
/*  622 */     c.setLenient(false);
/*  623 */     c.setTime(date);
/*  624 */     c.set(calendarField, amount);
/*  625 */     return c.getTime();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Calendar toCalendar(Date date) {
/*  637 */     Calendar c = Calendar.getInstance();
/*  638 */     c.setTime(Objects.<Date>requireNonNull(date, "date"));
/*  639 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Calendar toCalendar(Date date, TimeZone tz) {
/*  650 */     Calendar c = Calendar.getInstance(tz);
/*  651 */     c.setTime(Objects.<Date>requireNonNull(date, "date"));
/*  652 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date round(Date date, int field) {
/*  683 */     return modify(toCalendar(date), field, ModifyType.ROUND).getTime();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Calendar round(Calendar calendar, int field) {
/*  714 */     Objects.requireNonNull(calendar, "calendar");
/*  715 */     return modify((Calendar)calendar.clone(), field, ModifyType.ROUND);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date round(Object date, int field) {
/*  747 */     Objects.requireNonNull(date, "date");
/*  748 */     if (date instanceof Date) {
/*  749 */       return round((Date)date, field);
/*      */     }
/*  751 */     if (date instanceof Calendar) {
/*  752 */       return round((Calendar)date, field).getTime();
/*      */     }
/*  754 */     throw new ClassCastException("Could not round " + date);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date truncate(Date date, int field) {
/*  773 */     return modify(toCalendar(date), field, ModifyType.TRUNCATE).getTime();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Calendar truncate(Calendar date, int field) {
/*  792 */     Objects.requireNonNull(date, "date");
/*  793 */     return modify((Calendar)date.clone(), field, ModifyType.TRUNCATE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date truncate(Object date, int field) {
/*  813 */     Objects.requireNonNull(date, "date");
/*  814 */     if (date instanceof Date) {
/*  815 */       return truncate((Date)date, field);
/*      */     }
/*  817 */     if (date instanceof Calendar) {
/*  818 */       return truncate((Calendar)date, field).getTime();
/*      */     }
/*  820 */     throw new ClassCastException("Could not truncate " + date);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date ceiling(Date date, int field) {
/*  840 */     return modify(toCalendar(date), field, ModifyType.CEILING).getTime();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Calendar ceiling(Calendar calendar, int field) {
/*  860 */     Objects.requireNonNull(calendar, "calendar");
/*  861 */     return modify((Calendar)calendar.clone(), field, ModifyType.CEILING);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date ceiling(Object date, int field) {
/*  882 */     Objects.requireNonNull(date, "date");
/*  883 */     if (date instanceof Date) {
/*  884 */       return ceiling((Date)date, field);
/*      */     }
/*  886 */     if (date instanceof Calendar) {
/*  887 */       return ceiling((Calendar)date, field).getTime();
/*      */     }
/*  889 */     throw new ClassCastException("Could not find ceiling of for type: " + date.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Calendar modify(Calendar val, int field, ModifyType modType) {
/*  902 */     if (val.get(1) > 280000000) {
/*  903 */       throw new ArithmeticException("Calendar value too large for accurate calculations");
/*      */     }
/*      */     
/*  906 */     if (field == 14) {
/*  907 */       return val;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  916 */     Date date = val.getTime();
/*  917 */     long time = date.getTime();
/*  918 */     boolean done = false;
/*      */ 
/*      */     
/*  921 */     int millisecs = val.get(14);
/*  922 */     if (ModifyType.TRUNCATE == modType || millisecs < 500) {
/*  923 */       time -= millisecs;
/*      */     }
/*  925 */     if (field == 13) {
/*  926 */       done = true;
/*      */     }
/*      */ 
/*      */     
/*  930 */     int seconds = val.get(13);
/*  931 */     if (!done && (ModifyType.TRUNCATE == modType || seconds < 30)) {
/*  932 */       time -= seconds * 1000L;
/*      */     }
/*  934 */     if (field == 12) {
/*  935 */       done = true;
/*      */     }
/*      */ 
/*      */     
/*  939 */     int minutes = val.get(12);
/*  940 */     if (!done && (ModifyType.TRUNCATE == modType || minutes < 30)) {
/*  941 */       time -= minutes * 60000L;
/*      */     }
/*      */ 
/*      */     
/*  945 */     if (date.getTime() != time) {
/*  946 */       date.setTime(time);
/*  947 */       val.setTime(date);
/*      */     } 
/*      */ 
/*      */     
/*  951 */     boolean roundUp = false;
/*  952 */     for (int[] aField : fields) {
/*  953 */       for (int element : aField) {
/*  954 */         if (element == field) {
/*      */           
/*  956 */           if (modType == ModifyType.CEILING || (modType == ModifyType.ROUND && roundUp)) {
/*  957 */             if (field == 1001) {
/*      */ 
/*      */ 
/*      */               
/*  961 */               if (val.get(5) == 1) {
/*  962 */                 val.add(5, 15);
/*      */               } else {
/*  964 */                 val.add(5, -15);
/*  965 */                 val.add(2, 1);
/*      */               }
/*      */             
/*  968 */             } else if (field == 9) {
/*      */ 
/*      */ 
/*      */               
/*  972 */               if (val.get(11) == 0) {
/*  973 */                 val.add(11, 12);
/*      */               } else {
/*  975 */                 val.add(11, -12);
/*  976 */                 val.add(5, 1);
/*      */               }
/*      */             
/*      */             }
/*      */             else {
/*      */               
/*  982 */               val.add(aField[0], 1);
/*      */             } 
/*      */           }
/*  985 */           return val;
/*      */         } 
/*      */       } 
/*      */       
/*  989 */       int offset = 0;
/*  990 */       boolean offsetSet = false;
/*      */       
/*  992 */       switch (field) {
/*      */         case 1001:
/*  994 */           if (aField[0] == 5) {
/*      */ 
/*      */ 
/*      */             
/*  998 */             offset = val.get(5) - 1;
/*      */ 
/*      */             
/* 1001 */             if (offset >= 15) {
/* 1002 */               offset -= 15;
/*      */             }
/*      */             
/* 1005 */             roundUp = (offset > 7);
/* 1006 */             offsetSet = true;
/*      */           } 
/*      */           break;
/*      */         case 9:
/* 1010 */           if (aField[0] == 11) {
/*      */ 
/*      */             
/* 1013 */             offset = val.get(11);
/* 1014 */             if (offset >= 12) {
/* 1015 */               offset -= 12;
/*      */             }
/* 1017 */             roundUp = (offset >= 6);
/* 1018 */             offsetSet = true;
/*      */           } 
/*      */           break;
/*      */       } 
/*      */ 
/*      */       
/* 1024 */       if (!offsetSet) {
/* 1025 */         int min = val.getActualMinimum(aField[0]);
/* 1026 */         int max = val.getActualMaximum(aField[0]);
/*      */         
/* 1028 */         offset = val.get(aField[0]) - min;
/*      */         
/* 1030 */         roundUp = (offset > (max - min) / 2);
/*      */       } 
/*      */       
/* 1033 */       if (offset != 0) {
/* 1034 */         val.set(aField[0], val.get(aField[0]) - offset);
/*      */       }
/*      */     } 
/* 1037 */     throw new IllegalArgumentException("The field " + field + " is not supported");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Iterator<Calendar> iterator(Date focus, int rangeStyle) {
/* 1065 */     return iterator(toCalendar(focus), rangeStyle);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Iterator<Calendar> iterator(Calendar calendar, int rangeStyle) {
/*      */     Calendar start, end;
/* 1093 */     Objects.requireNonNull(calendar, "calendar");
/*      */ 
/*      */     
/* 1096 */     int startCutoff = 1;
/* 1097 */     int endCutoff = 7;
/* 1098 */     switch (rangeStyle) {
/*      */       
/*      */       case 5:
/*      */       case 6:
/* 1102 */         start = truncate(calendar, 2);
/*      */         
/* 1104 */         end = (Calendar)start.clone();
/* 1105 */         end.add(2, 1);
/* 1106 */         end.add(5, -1);
/*      */         
/* 1108 */         if (rangeStyle == 6) {
/* 1109 */           startCutoff = 2;
/* 1110 */           endCutoff = 1;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/* 1118 */         start = truncate(calendar, 5);
/* 1119 */         end = truncate(calendar, 5);
/* 1120 */         switch (rangeStyle) {
/*      */ 
/*      */ 
/*      */           
/*      */           case 2:
/* 1125 */             startCutoff = 2;
/* 1126 */             endCutoff = 1;
/*      */             break;
/*      */           case 3:
/* 1129 */             startCutoff = calendar.get(7);
/* 1130 */             endCutoff = startCutoff - 1;
/*      */             break;
/*      */           case 4:
/* 1133 */             startCutoff = calendar.get(7) - 3;
/* 1134 */             endCutoff = calendar.get(7) + 3;
/*      */             break;
/*      */         } 
/*      */         
/*      */         break;
/*      */       
/*      */       default:
/* 1141 */         throw new IllegalArgumentException("The range style " + rangeStyle + " is not valid.");
/*      */     } 
/* 1143 */     if (startCutoff < 1) {
/* 1144 */       startCutoff += 7;
/*      */     }
/* 1146 */     if (startCutoff > 7) {
/* 1147 */       startCutoff -= 7;
/*      */     }
/* 1149 */     if (endCutoff < 1) {
/* 1150 */       endCutoff += 7;
/*      */     }
/* 1152 */     if (endCutoff > 7) {
/* 1153 */       endCutoff -= 7;
/*      */     }
/* 1155 */     while (start.get(7) != startCutoff) {
/* 1156 */       start.add(5, -1);
/*      */     }
/* 1158 */     while (end.get(7) != endCutoff) {
/* 1159 */       end.add(5, 1);
/*      */     }
/* 1161 */     return new DateIterator(start, end);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Iterator<?> iterator(Object calendar, int rangeStyle) {
/* 1181 */     Objects.requireNonNull(calendar, "calendar");
/* 1182 */     if (calendar instanceof Date) {
/* 1183 */       return iterator((Date)calendar, rangeStyle);
/*      */     }
/* 1185 */     if (calendar instanceof Calendar) {
/* 1186 */       return iterator((Calendar)calendar, rangeStyle);
/*      */     }
/* 1188 */     throw new ClassCastException("Could not iterate based on " + calendar);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getFragmentInMilliseconds(Date date, int fragment) {
/* 1223 */     return getFragment(date, fragment, TimeUnit.MILLISECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getFragmentInSeconds(Date date, int fragment) {
/* 1261 */     return getFragment(date, fragment, TimeUnit.SECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getFragmentInMinutes(Date date, int fragment) {
/* 1299 */     return getFragment(date, fragment, TimeUnit.MINUTES);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getFragmentInHours(Date date, int fragment) {
/* 1337 */     return getFragment(date, fragment, TimeUnit.HOURS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getFragmentInDays(Date date, int fragment) {
/* 1375 */     return getFragment(date, fragment, TimeUnit.DAYS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getFragmentInMilliseconds(Calendar calendar, int fragment) {
/* 1413 */     return getFragment(calendar, fragment, TimeUnit.MILLISECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getFragmentInSeconds(Calendar calendar, int fragment) {
/* 1450 */     return getFragment(calendar, fragment, TimeUnit.SECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getFragmentInMinutes(Calendar calendar, int fragment) {
/* 1488 */     return getFragment(calendar, fragment, TimeUnit.MINUTES);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getFragmentInHours(Calendar calendar, int fragment) {
/* 1526 */     return getFragment(calendar, fragment, TimeUnit.HOURS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getFragmentInDays(Calendar calendar, int fragment) {
/* 1566 */     return getFragment(calendar, fragment, TimeUnit.DAYS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static long getFragment(Date date, int fragment, TimeUnit unit) {
/* 1581 */     validateDateNotNull(date);
/* 1582 */     Calendar calendar = Calendar.getInstance();
/* 1583 */     calendar.setTime(date);
/* 1584 */     return getFragment(calendar, fragment, unit);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static long getFragment(Calendar calendar, int fragment, TimeUnit unit) {
/* 1599 */     Objects.requireNonNull(calendar, "calendar");
/* 1600 */     long result = 0L;
/* 1601 */     int offset = (unit == TimeUnit.DAYS) ? 0 : 1;
/*      */ 
/*      */     
/* 1604 */     switch (fragment) {
/*      */       case 1:
/* 1606 */         result += unit.convert((calendar.get(6) - offset), TimeUnit.DAYS);
/*      */         break;
/*      */       case 2:
/* 1609 */         result += unit.convert((calendar.get(5) - offset), TimeUnit.DAYS);
/*      */         break;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1615 */     switch (fragment) {
/*      */ 
/*      */ 
/*      */       
/*      */       case 1:
/*      */       case 2:
/*      */       case 5:
/*      */       case 6:
/* 1623 */         result += unit.convert(calendar.get(11), TimeUnit.HOURS);
/*      */       
/*      */       case 11:
/* 1626 */         result += unit.convert(calendar.get(12), TimeUnit.MINUTES);
/*      */       
/*      */       case 12:
/* 1629 */         result += unit.convert(calendar.get(13), TimeUnit.SECONDS);
/*      */       
/*      */       case 13:
/* 1632 */         result += unit.convert(calendar.get(14), TimeUnit.MILLISECONDS);
/*      */ 
/*      */ 
/*      */       
/*      */       case 14:
/* 1637 */         return result;
/*      */     } 
/*      */     throw new IllegalArgumentException("The fragment " + fragment + " is not supported");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean truncatedEquals(Calendar cal1, Calendar cal2, int field) {
/* 1654 */     return (truncatedCompareTo(cal1, cal2, field) == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean truncatedEquals(Date date1, Date date2, int field) {
/* 1671 */     return (truncatedCompareTo(date1, date2, field) == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int truncatedCompareTo(Calendar cal1, Calendar cal2, int field) {
/* 1689 */     Calendar truncatedCal1 = truncate(cal1, field);
/* 1690 */     Calendar truncatedCal2 = truncate(cal2, field);
/* 1691 */     return truncatedCal1.compareTo(truncatedCal2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int truncatedCompareTo(Date date1, Date date2, int field) {
/* 1709 */     Date truncatedDate1 = truncate(date1, field);
/* 1710 */     Date truncatedDate2 = truncate(date2, field);
/* 1711 */     return truncatedDate1.compareTo(truncatedDate2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void validateDateNotNull(Date date) {
/* 1719 */     Objects.requireNonNull(date, "date");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class DateIterator
/*      */     implements Iterator<Calendar>
/*      */   {
/*      */     private final Calendar endFinal;
/*      */ 
/*      */     
/*      */     private final Calendar spot;
/*      */ 
/*      */ 
/*      */     
/*      */     DateIterator(Calendar startFinal, Calendar endFinal) {
/* 1736 */       this.endFinal = endFinal;
/* 1737 */       this.spot = startFinal;
/* 1738 */       this.spot.add(5, -1);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1748 */       return this.spot.before(this.endFinal);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Calendar next() {
/* 1758 */       if (this.spot.equals(this.endFinal)) {
/* 1759 */         throw new NoSuchElementException();
/*      */       }
/* 1761 */       this.spot.add(5, 1);
/* 1762 */       return (Calendar)this.spot.clone();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1773 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\time\DateUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */