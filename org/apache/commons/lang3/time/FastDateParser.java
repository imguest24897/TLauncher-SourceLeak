/*      */ package org.apache.commons.lang3.time;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Serializable;
/*      */ import java.text.DateFormatSymbols;
/*      */ import java.text.ParseException;
/*      */ import java.text.ParsePosition;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Comparator;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.TimeZone;
/*      */ import java.util.TreeSet;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
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
/*      */ public class FastDateParser
/*      */   implements DateParser, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 3L;
/*   86 */   static final Locale JAPANESE_IMPERIAL = new Locale("ja", "JP", "JP");
/*      */ 
/*      */ 
/*      */   
/*      */   private final String pattern;
/*      */ 
/*      */ 
/*      */   
/*      */   private final TimeZone timeZone;
/*      */ 
/*      */ 
/*      */   
/*      */   private final Locale locale;
/*      */ 
/*      */ 
/*      */   
/*      */   private final int century;
/*      */ 
/*      */ 
/*      */   
/*      */   private final int startYear;
/*      */ 
/*      */ 
/*      */   
/*      */   private transient List<StrategyAndWidth> patterns;
/*      */ 
/*      */ 
/*      */   
/*  114 */   private static final Comparator<String> LONGER_FIRST_LOWERCASE = Comparator.reverseOrder();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected FastDateParser(String pattern, TimeZone timeZone, Locale locale) {
/*  128 */     this(pattern, timeZone, locale, null);
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
/*      */   protected FastDateParser(String pattern, TimeZone timeZone, Locale locale, Date centuryStart) {
/*      */     int centuryStartYear;
/*  144 */     this.pattern = Objects.<String>requireNonNull(pattern, "pattern");
/*  145 */     this.timeZone = Objects.<TimeZone>requireNonNull(timeZone, "timeZone");
/*  146 */     this.locale = LocaleUtils.toLocale(locale);
/*      */     
/*  148 */     Calendar definingCalendar = Calendar.getInstance(timeZone, this.locale);
/*      */ 
/*      */     
/*  151 */     if (centuryStart != null) {
/*  152 */       definingCalendar.setTime(centuryStart);
/*  153 */       centuryStartYear = definingCalendar.get(1);
/*  154 */     } else if (this.locale.equals(JAPANESE_IMPERIAL)) {
/*  155 */       centuryStartYear = 0;
/*      */     } else {
/*      */       
/*  158 */       definingCalendar.setTime(new Date());
/*  159 */       centuryStartYear = definingCalendar.get(1) - 80;
/*      */     } 
/*  161 */     this.century = centuryStartYear / 100 * 100;
/*  162 */     this.startYear = centuryStartYear - this.century;
/*      */     
/*  164 */     init(definingCalendar);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void init(Calendar definingCalendar) {
/*  174 */     this.patterns = new ArrayList<>();
/*      */     
/*  176 */     StrategyParser fm = new StrategyParser(definingCalendar);
/*      */     while (true) {
/*  178 */       StrategyAndWidth field = fm.getNextStrategy();
/*  179 */       if (field == null) {
/*      */         break;
/*      */       }
/*  182 */       this.patterns.add(field);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class StrategyAndWidth
/*      */   {
/*      */     final FastDateParser.Strategy strategy;
/*      */ 
/*      */     
/*      */     final int width;
/*      */ 
/*      */     
/*      */     StrategyAndWidth(FastDateParser.Strategy strategy, int width) {
/*  197 */       this.strategy = strategy;
/*  198 */       this.width = width;
/*      */     }
/*      */     
/*      */     int getMaxWidth(ListIterator<StrategyAndWidth> lt) {
/*  202 */       if (!this.strategy.isNumber() || !lt.hasNext()) {
/*  203 */         return 0;
/*      */       }
/*  205 */       FastDateParser.Strategy nextStrategy = ((StrategyAndWidth)lt.next()).strategy;
/*  206 */       lt.previous();
/*  207 */       return nextStrategy.isNumber() ? this.width : 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  212 */       return "StrategyAndWidth [strategy=" + this.strategy + ", width=" + this.width + "]";
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private class StrategyParser
/*      */   {
/*      */     private final Calendar definingCalendar;
/*      */     
/*      */     private int currentIdx;
/*      */     
/*      */     StrategyParser(Calendar definingCalendar) {
/*  224 */       this.definingCalendar = definingCalendar;
/*      */     }
/*      */     
/*      */     FastDateParser.StrategyAndWidth getNextStrategy() {
/*  228 */       if (this.currentIdx >= FastDateParser.this.pattern.length()) {
/*  229 */         return null;
/*      */       }
/*      */       
/*  232 */       char c = FastDateParser.this.pattern.charAt(this.currentIdx);
/*  233 */       if (FastDateParser.isFormatLetter(c)) {
/*  234 */         return letterPattern(c);
/*      */       }
/*  236 */       return literal();
/*      */     }
/*      */     
/*      */     private FastDateParser.StrategyAndWidth letterPattern(char c) {
/*  240 */       int begin = this.currentIdx; do {  }
/*  241 */       while (++this.currentIdx < FastDateParser.this.pattern.length() && 
/*  242 */         FastDateParser.this.pattern.charAt(this.currentIdx) == c);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  247 */       int width = this.currentIdx - begin;
/*  248 */       return new FastDateParser.StrategyAndWidth(FastDateParser.this.getStrategy(c, width, this.definingCalendar), width);
/*      */     }
/*      */     
/*      */     private FastDateParser.StrategyAndWidth literal() {
/*  252 */       boolean activeQuote = false;
/*      */       
/*  254 */       StringBuilder sb = new StringBuilder();
/*  255 */       while (this.currentIdx < FastDateParser.this.pattern.length()) {
/*  256 */         char c = FastDateParser.this.pattern.charAt(this.currentIdx);
/*  257 */         if (!activeQuote && FastDateParser.isFormatLetter(c)) {
/*      */           break;
/*      */         }
/*  260 */         if (c == '\'' && (++this.currentIdx == FastDateParser.this.pattern.length() || FastDateParser.this.pattern.charAt(this.currentIdx) != '\'')) {
/*  261 */           activeQuote = !activeQuote;
/*      */           continue;
/*      */         } 
/*  264 */         this.currentIdx++;
/*  265 */         sb.append(c);
/*      */       } 
/*      */       
/*  268 */       if (activeQuote) {
/*  269 */         throw new IllegalArgumentException("Unterminated quote");
/*      */       }
/*      */       
/*  272 */       String formatField = sb.toString();
/*  273 */       return new FastDateParser.StrategyAndWidth(new FastDateParser.CopyQuotedStrategy(formatField), formatField.length());
/*      */     }
/*      */   }
/*      */   
/*      */   private static boolean isFormatLetter(char c) {
/*  278 */     return ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPattern() {
/*  287 */     return this.pattern;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TimeZone getTimeZone() {
/*  295 */     return this.timeZone;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Locale getLocale() {
/*  303 */     return this.locale;
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
/*      */   public boolean equals(Object obj) {
/*  316 */     if (!(obj instanceof FastDateParser)) {
/*  317 */       return false;
/*      */     }
/*  319 */     FastDateParser other = (FastDateParser)obj;
/*  320 */     return (this.pattern.equals(other.pattern) && this.timeZone.equals(other.timeZone) && this.locale.equals(other.locale));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  330 */     return this.pattern.hashCode() + 13 * (this.timeZone.hashCode() + 13 * this.locale.hashCode());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  340 */     return "FastDateParser[" + this.pattern + ", " + this.locale + ", " + this.timeZone.getID() + "]";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toStringAll() {
/*  350 */     return "FastDateParser [pattern=" + this.pattern + ", timeZone=" + this.timeZone + ", locale=" + this.locale + ", century=" + this.century + ", startYear=" + this.startYear + ", patterns=" + this.patterns + "]";
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
/*      */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/*  364 */     in.defaultReadObject();
/*      */     
/*  366 */     Calendar definingCalendar = Calendar.getInstance(this.timeZone, this.locale);
/*  367 */     init(definingCalendar);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object parseObject(String source) throws ParseException {
/*  375 */     return parse(source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Date parse(String source) throws ParseException {
/*  383 */     ParsePosition pp = new ParsePosition(0);
/*  384 */     Date date = parse(source, pp);
/*  385 */     if (date == null) {
/*      */       
/*  387 */       if (this.locale.equals(JAPANESE_IMPERIAL)) {
/*  388 */         throw new ParseException("(The " + this.locale + " locale does not support dates before 1868 AD)\nUnparseable date: \"" + source, pp
/*  389 */             .getErrorIndex());
/*      */       }
/*  391 */       throw new ParseException("Unparseable date: " + source, pp.getErrorIndex());
/*      */     } 
/*  393 */     return date;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object parseObject(String source, ParsePosition pos) {
/*  401 */     return parse(source, pos);
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
/*      */   public Date parse(String source, ParsePosition pos) {
/*  419 */     Calendar cal = Calendar.getInstance(this.timeZone, this.locale);
/*  420 */     cal.clear();
/*      */     
/*  422 */     return parse(source, pos, cal) ? cal.getTime() : null;
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
/*      */   public boolean parse(String source, ParsePosition pos, Calendar calendar) {
/*  440 */     ListIterator<StrategyAndWidth> lt = this.patterns.listIterator();
/*  441 */     while (lt.hasNext()) {
/*  442 */       StrategyAndWidth strategyAndWidth = lt.next();
/*  443 */       int maxWidth = strategyAndWidth.getMaxWidth(lt);
/*  444 */       if (!strategyAndWidth.strategy.parse(this, calendar, source, pos, maxWidth)) {
/*  445 */         return false;
/*      */       }
/*      */     } 
/*  448 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static StringBuilder simpleQuote(StringBuilder sb, String value) {
/*  454 */     for (int i = 0; i < value.length(); i++) {
/*  455 */       char c = value.charAt(i);
/*  456 */       switch (c) {
/*      */         case '$':
/*      */         case '(':
/*      */         case ')':
/*      */         case '*':
/*      */         case '+':
/*      */         case '.':
/*      */         case '?':
/*      */         case '[':
/*      */         case '\\':
/*      */         case '^':
/*      */         case '{':
/*      */         case '|':
/*  469 */           sb.append('\\'); break;
/*      */       } 
/*  471 */       sb.append(c);
/*      */     } 
/*      */     
/*  474 */     if (sb.charAt(sb.length() - 1) == '.')
/*      */     {
/*  476 */       sb.append('?');
/*      */     }
/*  478 */     return sb;
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
/*      */   private static Map<String, Integer> appendDisplayNames(Calendar calendar, Locale locale, int field, StringBuilder regex) {
/*  491 */     Map<String, Integer> values = new HashMap<>();
/*  492 */     Locale actualLocale = LocaleUtils.toLocale(locale);
/*  493 */     Map<String, Integer> displayNames = calendar.getDisplayNames(field, 0, actualLocale);
/*  494 */     TreeSet<String> sorted = new TreeSet<>(LONGER_FIRST_LOWERCASE);
/*  495 */     displayNames.forEach((k, v) -> {
/*      */           String keyLc = k.toLowerCase(actualLocale);
/*      */           if (sorted.add(keyLc)) {
/*      */             values.put(keyLc, v);
/*      */           }
/*      */         });
/*  501 */     sorted.forEach(symbol -> simpleQuote(regex, symbol).append('|'));
/*  502 */     return values;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int adjustYear(int twoDigitYear) {
/*  511 */     int trial = this.century + twoDigitYear;
/*  512 */     return (twoDigitYear >= this.startYear) ? trial : (trial + 100);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static abstract class Strategy
/*      */   {
/*      */     private Strategy() {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean isNumber() {
/*  526 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     abstract boolean parse(FastDateParser param1FastDateParser, Calendar param1Calendar, String param1String, ParsePosition param1ParsePosition, int param1Int);
/*      */   }
/*      */   
/*      */   private static abstract class PatternStrategy
/*      */     extends Strategy
/*      */   {
/*      */     Pattern pattern;
/*      */     
/*      */     private PatternStrategy() {}
/*      */     
/*      */     void createPattern(StringBuilder regex) {
/*  541 */       createPattern(regex.toString());
/*      */     }
/*      */     
/*      */     void createPattern(String regex) {
/*  545 */       this.pattern = Pattern.compile(regex);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean isNumber() {
/*  555 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     boolean parse(FastDateParser parser, Calendar calendar, String source, ParsePosition pos, int maxWidth) {
/*  561 */       Matcher matcher = this.pattern.matcher(source.substring(pos.getIndex()));
/*  562 */       if (!matcher.lookingAt()) {
/*  563 */         pos.setErrorIndex(pos.getIndex());
/*  564 */         return false;
/*      */       } 
/*  566 */       pos.setIndex(pos.getIndex() + matcher.end(1));
/*  567 */       setCalendar(parser, calendar, matcher.group(1));
/*  568 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  580 */       return getClass().getSimpleName() + " [pattern=" + this.pattern + "]";
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract void setCalendar(FastDateParser param1FastDateParser, Calendar param1Calendar, String param1String);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Strategy getStrategy(char f, int width, Calendar definingCalendar) {
/*  593 */     switch (f) {
/*      */       default:
/*  595 */         throw new IllegalArgumentException("Format '" + f + "' not supported");
/*      */       case 'D':
/*  597 */         return DAY_OF_YEAR_STRATEGY;
/*      */       case 'E':
/*  599 */         return getLocaleSpecificStrategy(7, definingCalendar);
/*      */       case 'F':
/*  601 */         return DAY_OF_WEEK_IN_MONTH_STRATEGY;
/*      */       case 'G':
/*  603 */         return getLocaleSpecificStrategy(0, definingCalendar);
/*      */       case 'H':
/*  605 */         return HOUR_OF_DAY_STRATEGY;
/*      */       case 'K':
/*  607 */         return HOUR_STRATEGY;
/*      */       case 'L':
/*      */       case 'M':
/*  610 */         return (width >= 3) ? getLocaleSpecificStrategy(2, definingCalendar) : NUMBER_MONTH_STRATEGY;
/*      */       case 'S':
/*  612 */         return MILLISECOND_STRATEGY;
/*      */       case 'W':
/*  614 */         return WEEK_OF_MONTH_STRATEGY;
/*      */       case 'a':
/*  616 */         return getLocaleSpecificStrategy(9, definingCalendar);
/*      */       case 'd':
/*  618 */         return DAY_OF_MONTH_STRATEGY;
/*      */       case 'h':
/*  620 */         return HOUR12_STRATEGY;
/*      */       case 'k':
/*  622 */         return HOUR24_OF_DAY_STRATEGY;
/*      */       case 'm':
/*  624 */         return MINUTE_STRATEGY;
/*      */       case 's':
/*  626 */         return SECOND_STRATEGY;
/*      */       case 'u':
/*  628 */         return DAY_OF_WEEK_STRATEGY;
/*      */       case 'w':
/*  630 */         return WEEK_OF_YEAR_STRATEGY;
/*      */       case 'Y':
/*      */       case 'y':
/*  633 */         return (width > 2) ? LITERAL_YEAR_STRATEGY : ABBREVIATED_YEAR_STRATEGY;
/*      */       case 'X':
/*  635 */         return ISO8601TimeZoneStrategy.getStrategy(width);
/*      */       case 'Z':
/*  637 */         if (width == 2)
/*  638 */           return ISO8601TimeZoneStrategy.ISO_8601_3_STRATEGY;  break;
/*      */       case 'z':
/*      */         break;
/*      */     } 
/*  642 */     return getLocaleSpecificStrategy(15, definingCalendar);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  647 */   private static final ConcurrentMap<Locale, Strategy>[] caches = (ConcurrentMap<Locale, Strategy>[])new ConcurrentMap[17];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static ConcurrentMap<Locale, Strategy> getCache(int field) {
/*  655 */     synchronized (caches) {
/*  656 */       if (caches[field] == null) {
/*  657 */         caches[field] = new ConcurrentHashMap<>(3);
/*      */       }
/*  659 */       return caches[field];
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Strategy getLocaleSpecificStrategy(int field, Calendar definingCalendar) {
/*  670 */     ConcurrentMap<Locale, Strategy> cache = getCache(field);
/*  671 */     return cache.computeIfAbsent(this.locale, k -> (field == 15) ? new TimeZoneStrategy(this.locale) : new CaseInsensitiveTextStrategy(field, definingCalendar, this.locale));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class CopyQuotedStrategy
/*      */     extends Strategy
/*      */   {
/*      */     private final String formatField;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     CopyQuotedStrategy(String formatField) {
/*  687 */       this.formatField = formatField;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean isNumber() {
/*  695 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     boolean parse(FastDateParser parser, Calendar calendar, String source, ParsePosition pos, int maxWidth) {
/*  701 */       for (int idx = 0; idx < this.formatField.length(); idx++) {
/*  702 */         int sIdx = idx + pos.getIndex();
/*  703 */         if (sIdx == source.length()) {
/*  704 */           pos.setErrorIndex(sIdx);
/*  705 */           return false;
/*      */         } 
/*  707 */         if (this.formatField.charAt(idx) != source.charAt(sIdx)) {
/*  708 */           pos.setErrorIndex(sIdx);
/*  709 */           return false;
/*      */         } 
/*      */       } 
/*  712 */       pos.setIndex(this.formatField.length() + pos.getIndex());
/*  713 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  723 */       return "CopyQuotedStrategy [formatField=" + this.formatField + "]";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class CaseInsensitiveTextStrategy
/*      */     extends PatternStrategy
/*      */   {
/*      */     private final int field;
/*      */ 
/*      */     
/*      */     final Locale locale;
/*      */ 
/*      */     
/*      */     private final Map<String, Integer> lKeyValues;
/*      */ 
/*      */ 
/*      */     
/*      */     CaseInsensitiveTextStrategy(int field, Calendar definingCalendar, Locale locale) {
/*  743 */       this.field = field;
/*  744 */       this.locale = LocaleUtils.toLocale(locale);
/*      */       
/*  746 */       StringBuilder regex = new StringBuilder();
/*  747 */       regex.append("((?iu)");
/*  748 */       this.lKeyValues = FastDateParser.appendDisplayNames(definingCalendar, locale, field, regex);
/*  749 */       regex.setLength(regex.length() - 1);
/*  750 */       regex.append(")");
/*  751 */       createPattern(regex);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void setCalendar(FastDateParser parser, Calendar calendar, String value) {
/*  759 */       String lowerCase = value.toLowerCase(this.locale);
/*  760 */       Integer iVal = this.lKeyValues.get(lowerCase);
/*  761 */       if (iVal == null)
/*      */       {
/*  763 */         iVal = this.lKeyValues.get(lowerCase + '.');
/*      */       }
/*      */       
/*  766 */       if (9 != this.field || iVal.intValue() <= 1) {
/*  767 */         calendar.set(this.field, iVal.intValue());
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  778 */       return "CaseInsensitiveTextStrategy [field=" + this.field + ", locale=" + this.locale + ", lKeyValues=" + this.lKeyValues + ", pattern=" + this.pattern + "]";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class NumberStrategy
/*      */     extends Strategy
/*      */   {
/*      */     private final int field;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     NumberStrategy(int field) {
/*  797 */       this.field = field;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean isNumber() {
/*  805 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     boolean parse(FastDateParser parser, Calendar calendar, String source, ParsePosition pos, int maxWidth) {
/*  811 */       int idx = pos.getIndex();
/*  812 */       int last = source.length();
/*      */       
/*  814 */       if (maxWidth == 0) {
/*      */         
/*  816 */         for (; idx < last; idx++) {
/*  817 */           char c = source.charAt(idx);
/*  818 */           if (!Character.isWhitespace(c)) {
/*      */             break;
/*      */           }
/*      */         } 
/*  822 */         pos.setIndex(idx);
/*      */       } else {
/*  824 */         int end = idx + maxWidth;
/*  825 */         if (last > end) {
/*  826 */           last = end;
/*      */         }
/*      */       } 
/*      */       
/*  830 */       for (; idx < last; idx++) {
/*  831 */         char c = source.charAt(idx);
/*  832 */         if (!Character.isDigit(c)) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */       
/*  837 */       if (pos.getIndex() == idx) {
/*  838 */         pos.setErrorIndex(idx);
/*  839 */         return false;
/*      */       } 
/*      */       
/*  842 */       int value = Integer.parseInt(source.substring(pos.getIndex(), idx));
/*  843 */       pos.setIndex(idx);
/*      */       
/*  845 */       calendar.set(this.field, modify(parser, value));
/*  846 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int modify(FastDateParser parser, int iValue) {
/*  857 */       return iValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  867 */       return "NumberStrategy [field=" + this.field + "]";
/*      */     }
/*      */   }
/*      */   
/*  871 */   private static final Strategy ABBREVIATED_YEAR_STRATEGY = new NumberStrategy(1)
/*      */     {
/*      */ 
/*      */       
/*      */       int modify(FastDateParser parser, int iValue)
/*      */       {
/*  877 */         return (iValue < 100) ? parser.adjustYear(iValue) : iValue;
/*      */       }
/*      */     };
/*      */ 
/*      */   
/*      */   static class TimeZoneStrategy
/*      */     extends PatternStrategy
/*      */   {
/*      */     private static final String RFC_822_TIME_ZONE = "[+-]\\d{4}";
/*      */     
/*      */     private static final String GMT_OPTION = "GMT[+-]\\d{1,2}:\\d{2}";
/*      */     private final Locale locale;
/*  889 */     private final Map<String, TzInfo> tzNames = new HashMap<>();
/*      */     private static final int ID = 0;
/*      */     
/*      */     private static class TzInfo { final TimeZone zone;
/*      */       final int dstOffset;
/*      */       
/*      */       TzInfo(TimeZone tz, boolean useDst) {
/*  896 */         this.zone = tz;
/*  897 */         this.dstOffset = useDst ? tz.getDSTSavings() : 0;
/*      */       } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     TimeZoneStrategy(Locale locale) {
/*  912 */       this.locale = LocaleUtils.toLocale(locale);
/*      */       
/*  914 */       StringBuilder sb = new StringBuilder();
/*  915 */       sb.append("((?iu)[+-]\\d{4}|GMT[+-]\\d{1,2}:\\d{2}");
/*      */       
/*  917 */       Set<String> sorted = new TreeSet<>(FastDateParser.LONGER_FIRST_LOWERCASE);
/*      */       
/*  919 */       String[][] zones = DateFormatSymbols.getInstance(locale).getZoneStrings();
/*  920 */       for (String[] zoneNames : zones) {
/*      */         
/*  922 */         String tzId = zoneNames[0];
/*  923 */         if (!tzId.equalsIgnoreCase("GMT")) {
/*      */ 
/*      */           
/*  926 */           TimeZone tz = TimeZone.getTimeZone(tzId);
/*      */ 
/*      */           
/*  929 */           TzInfo standard = new TzInfo(tz, false);
/*  930 */           TzInfo tzInfo = standard;
/*  931 */           for (int i = 1; i < zoneNames.length; i++) {
/*  932 */             switch (i) {
/*      */               
/*      */               case 3:
/*  935 */                 tzInfo = new TzInfo(tz, true);
/*      */                 break;
/*      */               case 5:
/*  938 */                 tzInfo = standard;
/*      */                 break;
/*      */             } 
/*      */ 
/*      */             
/*  943 */             if (zoneNames[i] != null) {
/*  944 */               String key = zoneNames[i].toLowerCase(locale);
/*      */ 
/*      */               
/*  947 */               if (sorted.add(key)) {
/*  948 */                 this.tzNames.put(key, tzInfo);
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  955 */       sorted.forEach(zoneName -> FastDateParser.simpleQuote(sb.append('|'), zoneName));
/*  956 */       sb.append(")");
/*  957 */       createPattern(sb);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void setCalendar(FastDateParser parser, Calendar calendar, String timeZone) {
/*  965 */       TimeZone tz = FastTimeZone.getGmtTimeZone(timeZone);
/*  966 */       if (tz != null) {
/*  967 */         calendar.setTimeZone(tz);
/*      */       } else {
/*  969 */         String lowerCase = timeZone.toLowerCase(this.locale);
/*  970 */         TzInfo tzInfo = this.tzNames.get(lowerCase);
/*  971 */         if (tzInfo == null)
/*      */         {
/*  973 */           tzInfo = this.tzNames.get(lowerCase + '.');
/*      */         }
/*  975 */         calendar.set(16, tzInfo.dstOffset);
/*  976 */         calendar.set(15, tzInfo.zone.getRawOffset());
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  987 */       return "TimeZoneStrategy [locale=" + this.locale + ", tzNames=" + this.tzNames + ", pattern=" + this.pattern + "]";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class ISO8601TimeZoneStrategy
/*      */     extends PatternStrategy
/*      */   {
/*      */     ISO8601TimeZoneStrategy(String pattern) {
/* 1000 */       createPattern(pattern);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void setCalendar(FastDateParser parser, Calendar calendar, String value) {
/* 1008 */       calendar.setTimeZone(FastTimeZone.getGmtTimeZone(value));
/*      */     }
/*      */     
/* 1011 */     private static final FastDateParser.Strategy ISO_8601_1_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}))");
/* 1012 */     private static final FastDateParser.Strategy ISO_8601_2_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}\\d{2}))");
/* 1013 */     private static final FastDateParser.Strategy ISO_8601_3_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}(?::)\\d{2}))");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static FastDateParser.Strategy getStrategy(int tokenLen) {
/* 1023 */       switch (tokenLen) {
/*      */         case 1:
/* 1025 */           return ISO_8601_1_STRATEGY;
/*      */         case 2:
/* 1027 */           return ISO_8601_2_STRATEGY;
/*      */         case 3:
/* 1029 */           return ISO_8601_3_STRATEGY;
/*      */       } 
/* 1031 */       throw new IllegalArgumentException("invalid number of X");
/*      */     }
/*      */   }
/*      */ 
/*      */   
/* 1036 */   private static final Strategy NUMBER_MONTH_STRATEGY = new NumberStrategy(2)
/*      */     {
/*      */       int modify(FastDateParser parser, int iValue) {
/* 1039 */         return iValue - 1;
/*      */       }
/*      */     };
/*      */   
/* 1043 */   private static final Strategy LITERAL_YEAR_STRATEGY = new NumberStrategy(1);
/* 1044 */   private static final Strategy WEEK_OF_YEAR_STRATEGY = new NumberStrategy(3);
/* 1045 */   private static final Strategy WEEK_OF_MONTH_STRATEGY = new NumberStrategy(4);
/* 1046 */   private static final Strategy DAY_OF_YEAR_STRATEGY = new NumberStrategy(6);
/* 1047 */   private static final Strategy DAY_OF_MONTH_STRATEGY = new NumberStrategy(5);
/* 1048 */   private static final Strategy DAY_OF_WEEK_STRATEGY = new NumberStrategy(7)
/*      */     {
/*      */       int modify(FastDateParser parser, int iValue) {
/* 1051 */         return (iValue == 7) ? 1 : (iValue + 1);
/*      */       }
/*      */     };
/*      */   
/* 1055 */   private static final Strategy DAY_OF_WEEK_IN_MONTH_STRATEGY = new NumberStrategy(8);
/* 1056 */   private static final Strategy HOUR_OF_DAY_STRATEGY = new NumberStrategy(11);
/* 1057 */   private static final Strategy HOUR24_OF_DAY_STRATEGY = new NumberStrategy(11)
/*      */     {
/*      */       int modify(FastDateParser parser, int iValue) {
/* 1060 */         return (iValue == 24) ? 0 : iValue;
/*      */       }
/*      */     };
/*      */   
/* 1064 */   private static final Strategy HOUR12_STRATEGY = new NumberStrategy(10)
/*      */     {
/*      */       int modify(FastDateParser parser, int iValue) {
/* 1067 */         return (iValue == 12) ? 0 : iValue;
/*      */       }
/*      */     };
/*      */   
/* 1071 */   private static final Strategy HOUR_STRATEGY = new NumberStrategy(10);
/* 1072 */   private static final Strategy MINUTE_STRATEGY = new NumberStrategy(12);
/* 1073 */   private static final Strategy SECOND_STRATEGY = new NumberStrategy(13);
/* 1074 */   private static final Strategy MILLISECOND_STRATEGY = new NumberStrategy(14);
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\time\FastDateParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */