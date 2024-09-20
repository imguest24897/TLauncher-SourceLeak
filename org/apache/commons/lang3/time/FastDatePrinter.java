/*      */ package org.apache.commons.lang3.time;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Serializable;
/*      */ import java.text.DateFormatSymbols;
/*      */ import java.text.FieldPosition;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.TimeZone;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import org.apache.commons.lang3.ClassUtils;
/*      */ import org.apache.commons.lang3.LocaleUtils;
/*      */ import org.apache.commons.lang3.exception.ExceptionUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class FastDatePrinter
/*      */   implements DatePrinter, Serializable
/*      */ {
/*   97 */   private static final Rule[] EMPTY_RULE_ARRAY = new Rule[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int FULL = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int LONG = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int MEDIUM = 2;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int SHORT = 3;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final String pattern;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final TimeZone timeZone;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final Locale locale;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private transient Rule[] rules;
/*      */ 
/*      */ 
/*      */   
/*      */   private transient int maxLengthEstimate;
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int MAX_DIGITS = 10;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected FastDatePrinter(String pattern, TimeZone timeZone, Locale locale) {
/*  156 */     this.pattern = pattern;
/*  157 */     this.timeZone = timeZone;
/*  158 */     this.locale = LocaleUtils.toLocale(locale);
/*  159 */     init();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void init() {
/*  166 */     List<Rule> rulesList = parsePattern();
/*  167 */     this.rules = rulesList.<Rule>toArray(EMPTY_RULE_ARRAY);
/*      */     
/*  169 */     int len = 0;
/*  170 */     for (int i = this.rules.length; --i >= 0;) {
/*  171 */       len += this.rules[i].estimateLength();
/*      */     }
/*      */     
/*  174 */     this.maxLengthEstimate = len;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected List<Rule> parsePattern() {
/*  185 */     DateFormatSymbols symbols = new DateFormatSymbols(this.locale);
/*  186 */     List<Rule> rules = new ArrayList<>();
/*      */     
/*  188 */     String[] ERAs = symbols.getEras();
/*  189 */     String[] months = symbols.getMonths();
/*  190 */     String[] shortMonths = symbols.getShortMonths();
/*  191 */     String[] weekdays = symbols.getWeekdays();
/*  192 */     String[] shortWeekdays = symbols.getShortWeekdays();
/*  193 */     String[] AmPmStrings = symbols.getAmPmStrings();
/*      */     
/*  195 */     int length = this.pattern.length();
/*  196 */     int[] indexRef = new int[1];
/*      */     
/*  198 */     for (int i = 0; i < length; i++) {
/*  199 */       Rule rule; String sub; indexRef[0] = i;
/*  200 */       String token = parseToken(this.pattern, indexRef);
/*  201 */       i = indexRef[0];
/*      */       
/*  203 */       int tokenLen = token.length();
/*  204 */       if (tokenLen == 0) {
/*      */         break;
/*      */       }
/*      */ 
/*      */       
/*  209 */       char c = token.charAt(0);
/*      */       
/*  211 */       switch (c) {
/*      */         case 'G':
/*  213 */           rule = new TextField(0, ERAs);
/*      */           break;
/*      */         case 'Y':
/*      */         case 'y':
/*  217 */           if (tokenLen == 2) {
/*  218 */             rule = TwoDigitYearField.INSTANCE;
/*      */           } else {
/*  220 */             rule = selectNumberRule(1, Math.max(tokenLen, 4));
/*      */           } 
/*  222 */           if (c == 'Y') {
/*  223 */             rule = new WeekYear((NumberRule)rule);
/*      */           }
/*      */           break;
/*      */         case 'M':
/*  227 */           if (tokenLen >= 4) {
/*  228 */             rule = new TextField(2, months); break;
/*  229 */           }  if (tokenLen == 3) {
/*  230 */             rule = new TextField(2, shortMonths); break;
/*  231 */           }  if (tokenLen == 2) {
/*  232 */             rule = TwoDigitMonthField.INSTANCE; break;
/*      */           } 
/*  234 */           rule = UnpaddedMonthField.INSTANCE;
/*      */           break;
/*      */         
/*      */         case 'L':
/*  238 */           if (tokenLen >= 4) {
/*  239 */             rule = new TextField(2, CalendarUtils.getInstance(this.locale).getStandaloneLongMonthNames()); break;
/*  240 */           }  if (tokenLen == 3) {
/*  241 */             rule = new TextField(2, CalendarUtils.getInstance(this.locale).getStandaloneShortMonthNames()); break;
/*  242 */           }  if (tokenLen == 2) {
/*  243 */             rule = TwoDigitMonthField.INSTANCE; break;
/*      */           } 
/*  245 */           rule = UnpaddedMonthField.INSTANCE;
/*      */           break;
/*      */         
/*      */         case 'd':
/*  249 */           rule = selectNumberRule(5, tokenLen);
/*      */           break;
/*      */         case 'h':
/*  252 */           rule = new TwelveHourField(selectNumberRule(10, tokenLen));
/*      */           break;
/*      */         case 'H':
/*  255 */           rule = selectNumberRule(11, tokenLen);
/*      */           break;
/*      */         case 'm':
/*  258 */           rule = selectNumberRule(12, tokenLen);
/*      */           break;
/*      */         case 's':
/*  261 */           rule = selectNumberRule(13, tokenLen);
/*      */           break;
/*      */         case 'S':
/*  264 */           rule = selectNumberRule(14, tokenLen);
/*      */           break;
/*      */         case 'E':
/*  267 */           rule = new TextField(7, (tokenLen < 4) ? shortWeekdays : weekdays);
/*      */           break;
/*      */         case 'u':
/*  270 */           rule = new DayInWeekField(selectNumberRule(7, tokenLen));
/*      */           break;
/*      */         case 'D':
/*  273 */           rule = selectNumberRule(6, tokenLen);
/*      */           break;
/*      */         case 'F':
/*  276 */           rule = selectNumberRule(8, tokenLen);
/*      */           break;
/*      */         case 'w':
/*  279 */           rule = selectNumberRule(3, tokenLen);
/*      */           break;
/*      */         case 'W':
/*  282 */           rule = selectNumberRule(4, tokenLen);
/*      */           break;
/*      */         case 'a':
/*  285 */           rule = new TextField(9, AmPmStrings);
/*      */           break;
/*      */         case 'k':
/*  288 */           rule = new TwentyFourHourField(selectNumberRule(11, tokenLen));
/*      */           break;
/*      */         case 'K':
/*  291 */           rule = selectNumberRule(10, tokenLen);
/*      */           break;
/*      */         case 'X':
/*  294 */           rule = Iso8601_Rule.getRule(tokenLen);
/*      */           break;
/*      */         case 'z':
/*  297 */           if (tokenLen >= 4) {
/*  298 */             rule = new TimeZoneNameRule(this.timeZone, this.locale, 1); break;
/*      */           } 
/*  300 */           rule = new TimeZoneNameRule(this.timeZone, this.locale, 0);
/*      */           break;
/*      */         
/*      */         case 'Z':
/*  304 */           if (tokenLen == 1) {
/*  305 */             rule = TimeZoneNumberRule.INSTANCE_NO_COLON; break;
/*  306 */           }  if (tokenLen == 2) {
/*  307 */             rule = Iso8601_Rule.ISO8601_HOURS_COLON_MINUTES; break;
/*      */           } 
/*  309 */           rule = TimeZoneNumberRule.INSTANCE_COLON;
/*      */           break;
/*      */         
/*      */         case '\'':
/*  313 */           sub = token.substring(1);
/*  314 */           if (sub.length() == 1) {
/*  315 */             rule = new CharacterLiteral(sub.charAt(0)); break;
/*      */           } 
/*  317 */           rule = new StringLiteral(sub);
/*      */           break;
/*      */         
/*      */         default:
/*  321 */           throw new IllegalArgumentException("Illegal pattern component: " + token);
/*      */       } 
/*      */       
/*  324 */       rules.add(rule);
/*      */     } 
/*      */     
/*  327 */     return rules;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String parseToken(String pattern, int[] indexRef) {
/*  338 */     StringBuilder buf = new StringBuilder();
/*      */     
/*  340 */     int i = indexRef[0];
/*  341 */     int length = pattern.length();
/*      */     
/*  343 */     char c = pattern.charAt(i);
/*  344 */     if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
/*      */ 
/*      */       
/*  347 */       buf.append(c);
/*      */       
/*  349 */       while (i + 1 < length) {
/*  350 */         char peek = pattern.charAt(i + 1);
/*  351 */         if (peek != c) {
/*      */           break;
/*      */         }
/*  354 */         buf.append(c);
/*  355 */         i++;
/*      */       } 
/*      */     } else {
/*      */       
/*  359 */       buf.append('\'');
/*      */       
/*  361 */       boolean inLiteral = false;
/*      */       
/*  363 */       for (; i < length; i++) {
/*  364 */         c = pattern.charAt(i);
/*      */         
/*  366 */         if (c == '\'')
/*  367 */         { if (i + 1 < length && pattern.charAt(i + 1) == '\'') {
/*      */             
/*  369 */             i++;
/*  370 */             buf.append(c);
/*      */           } else {
/*  372 */             inLiteral = !inLiteral;
/*      */           }  }
/*  374 */         else { if (!inLiteral && ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))) {
/*      */             
/*  376 */             i--;
/*      */             break;
/*      */           } 
/*  379 */           buf.append(c); }
/*      */       
/*      */       } 
/*      */     } 
/*      */     
/*  384 */     indexRef[0] = i;
/*  385 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected NumberRule selectNumberRule(int field, int padding) {
/*  396 */     switch (padding) {
/*      */       case 1:
/*  398 */         return new UnpaddedNumberField(field);
/*      */       case 2:
/*  400 */         return new TwoDigitNumberField(field);
/*      */     } 
/*  402 */     return new PaddedNumberField(field, padding);
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
/*      */   @Deprecated
/*      */   public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
/*  419 */     if (obj instanceof Date) {
/*  420 */       return format((Date)obj, toAppendTo);
/*      */     }
/*  422 */     if (obj instanceof Calendar) {
/*  423 */       return format((Calendar)obj, toAppendTo);
/*      */     }
/*  425 */     if (obj instanceof Long) {
/*  426 */       return format(((Long)obj).longValue(), toAppendTo);
/*      */     }
/*  428 */     throw new IllegalArgumentException("Unknown class: " + ClassUtils.getName(obj, "<null>"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String format(Object obj) {
/*  439 */     if (obj instanceof Date) {
/*  440 */       return format((Date)obj);
/*      */     }
/*  442 */     if (obj instanceof Calendar) {
/*  443 */       return format((Calendar)obj);
/*      */     }
/*  445 */     if (obj instanceof Long) {
/*  446 */       return format(((Long)obj).longValue());
/*      */     }
/*  448 */     throw new IllegalArgumentException("Unknown class: " + ClassUtils.getName(obj, "<null>"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String format(long millis) {
/*  456 */     Calendar c = newCalendar();
/*  457 */     c.setTimeInMillis(millis);
/*  458 */     return applyRulesToString(c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String applyRulesToString(Calendar c) {
/*  467 */     return ((StringBuilder)applyRules(c, new StringBuilder(this.maxLengthEstimate))).toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Calendar newCalendar() {
/*  475 */     return Calendar.getInstance(this.timeZone, this.locale);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String format(Date date) {
/*  483 */     Calendar c = newCalendar();
/*  484 */     c.setTime(date);
/*  485 */     return applyRulesToString(c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String format(Calendar calendar) {
/*  493 */     return ((StringBuilder)format(calendar, new StringBuilder(this.maxLengthEstimate))).toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuffer format(long millis, StringBuffer buf) {
/*  501 */     Calendar c = newCalendar();
/*  502 */     c.setTimeInMillis(millis);
/*  503 */     return applyRules(c, buf);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuffer format(Date date, StringBuffer buf) {
/*  511 */     Calendar c = newCalendar();
/*  512 */     c.setTime(date);
/*  513 */     return applyRules(c, buf);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuffer format(Calendar calendar, StringBuffer buf) {
/*  522 */     return format(calendar.getTime(), buf);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <B extends Appendable> B format(long millis, B buf) {
/*  530 */     Calendar c = newCalendar();
/*  531 */     c.setTimeInMillis(millis);
/*  532 */     return applyRules(c, buf);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <B extends Appendable> B format(Date date, B buf) {
/*  540 */     Calendar c = newCalendar();
/*  541 */     c.setTime(date);
/*  542 */     return applyRules(c, buf);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <B extends Appendable> B format(Calendar calendar, B buf) {
/*  551 */     if (!calendar.getTimeZone().equals(this.timeZone)) {
/*  552 */       calendar = (Calendar)calendar.clone();
/*  553 */       calendar.setTimeZone(this.timeZone);
/*      */     } 
/*  555 */     return applyRules(calendar, buf);
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
/*      */   @Deprecated
/*      */   protected StringBuffer applyRules(Calendar calendar, StringBuffer buf) {
/*  570 */     return applyRules(calendar, buf);
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
/*      */   private <B extends Appendable> B applyRules(Calendar calendar, B buf) {
/*      */     try {
/*  584 */       for (Rule rule : this.rules) {
/*  585 */         rule.appendTo((Appendable)buf, calendar);
/*      */       }
/*  587 */     } catch (IOException ioe) {
/*  588 */       ExceptionUtils.rethrow(ioe);
/*      */     } 
/*  590 */     return buf;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPattern() {
/*  599 */     return this.pattern;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TimeZone getTimeZone() {
/*  607 */     return this.timeZone;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Locale getLocale() {
/*  615 */     return this.locale;
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
/*      */   public int getMaxLengthEstimate() {
/*  628 */     return this.maxLengthEstimate;
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
/*      */   public boolean equals(Object obj) {
/*  640 */     if (!(obj instanceof FastDatePrinter)) {
/*  641 */       return false;
/*      */     }
/*  643 */     FastDatePrinter other = (FastDatePrinter)obj;
/*  644 */     return (this.pattern.equals(other.pattern) && this.timeZone
/*  645 */       .equals(other.timeZone) && this.locale
/*  646 */       .equals(other.locale));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  656 */     return this.pattern.hashCode() + 13 * (this.timeZone.hashCode() + 13 * this.locale.hashCode());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  666 */     return "FastDatePrinter[" + this.pattern + "," + this.locale + "," + this.timeZone.getID() + "]";
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
/*      */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/*  679 */     in.defaultReadObject();
/*  680 */     init();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void appendDigits(Appendable buffer, int value) throws IOException {
/*  691 */     buffer.append((char)(value / 10 + 48));
/*  692 */     buffer.append((char)(value % 10 + 48));
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
/*      */   private static void appendFullDigits(Appendable buffer, int value, int minFieldWidth) throws IOException {
/*  708 */     if (value < 10000) {
/*      */ 
/*      */       
/*  711 */       int nDigits = 4;
/*  712 */       if (value < 1000) {
/*  713 */         nDigits--;
/*  714 */         if (value < 100) {
/*  715 */           nDigits--;
/*  716 */           if (value < 10) {
/*  717 */             nDigits--;
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/*  722 */       for (int i = minFieldWidth - nDigits; i > 0; i--) {
/*  723 */         buffer.append('0');
/*      */       }
/*      */       
/*  726 */       switch (nDigits) {
/*      */         case 4:
/*  728 */           buffer.append((char)(value / 1000 + 48));
/*  729 */           value %= 1000;
/*      */         case 3:
/*  731 */           if (value >= 100) {
/*  732 */             buffer.append((char)(value / 100 + 48));
/*  733 */             value %= 100;
/*      */           } else {
/*  735 */             buffer.append('0');
/*      */           } 
/*      */         case 2:
/*  738 */           if (value >= 10) {
/*  739 */             buffer.append((char)(value / 10 + 48));
/*  740 */             value %= 10;
/*      */           } else {
/*  742 */             buffer.append('0');
/*      */           } 
/*      */         case 1:
/*  745 */           buffer.append((char)(value + 48));
/*      */           break;
/*      */       } 
/*      */ 
/*      */     
/*      */     } else {
/*  751 */       char[] work = new char[10];
/*  752 */       int digit = 0;
/*  753 */       while (value != 0) {
/*  754 */         work[digit++] = (char)(value % 10 + 48);
/*  755 */         value /= 10;
/*      */       } 
/*      */ 
/*      */       
/*  759 */       while (digit < minFieldWidth) {
/*  760 */         buffer.append('0');
/*  761 */         minFieldWidth--;
/*      */       } 
/*      */ 
/*      */       
/*  765 */       while (--digit >= 0) {
/*  766 */         buffer.append(work[digit]);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static interface Rule
/*      */   {
/*      */     int estimateLength();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void appendTo(Appendable param1Appendable, Calendar param1Calendar) throws IOException;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static interface NumberRule
/*      */     extends Rule
/*      */   {
/*      */     void appendTo(Appendable param1Appendable, int param1Int) throws IOException;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class CharacterLiteral
/*      */     implements Rule
/*      */   {
/*      */     private final char value;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     CharacterLiteral(char value) {
/*  820 */       this.value = value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  828 */       return 1;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/*  836 */       buffer.append(this.value);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class StringLiteral
/*      */     implements Rule
/*      */   {
/*      */     private final String value;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     StringLiteral(String value) {
/*  853 */       this.value = value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  861 */       return this.value.length();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/*  869 */       buffer.append(this.value);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TextField
/*      */     implements Rule
/*      */   {
/*      */     private final int field;
/*      */ 
/*      */ 
/*      */     
/*      */     private final String[] values;
/*      */ 
/*      */ 
/*      */     
/*      */     TextField(int field, String[] values) {
/*  888 */       this.field = field;
/*  889 */       this.values = values;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  897 */       int max = 0;
/*  898 */       for (int i = this.values.length; --i >= 0; ) {
/*  899 */         int len = this.values[i].length();
/*  900 */         if (len > max) {
/*  901 */           max = len;
/*      */         }
/*      */       } 
/*  904 */       return max;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/*  912 */       buffer.append(this.values[calendar.get(this.field)]);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class UnpaddedNumberField
/*      */     implements NumberRule
/*      */   {
/*      */     private final int field;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     UnpaddedNumberField(int field) {
/*  928 */       this.field = field;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  936 */       return 4;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/*  944 */       appendTo(buffer, calendar.get(this.field));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(Appendable buffer, int value) throws IOException {
/*  952 */       if (value < 10) {
/*  953 */         buffer.append((char)(value + 48));
/*  954 */       } else if (value < 100) {
/*  955 */         FastDatePrinter.appendDigits(buffer, value);
/*      */       } else {
/*  957 */         FastDatePrinter.appendFullDigits(buffer, value, 1);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class UnpaddedMonthField
/*      */     implements NumberRule
/*      */   {
/*  966 */     static final UnpaddedMonthField INSTANCE = new UnpaddedMonthField();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  980 */       return 2;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/*  988 */       appendTo(buffer, calendar.get(2) + 1);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(Appendable buffer, int value) throws IOException {
/*  996 */       if (value < 10) {
/*  997 */         buffer.append((char)(value + 48));
/*      */       } else {
/*  999 */         FastDatePrinter.appendDigits(buffer, value);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class PaddedNumberField
/*      */     implements NumberRule
/*      */   {
/*      */     private final int field;
/*      */ 
/*      */     
/*      */     private final int size;
/*      */ 
/*      */ 
/*      */     
/*      */     PaddedNumberField(int field, int size) {
/* 1018 */       if (size < 3)
/*      */       {
/* 1020 */         throw new IllegalArgumentException();
/*      */       }
/* 1022 */       this.field = field;
/* 1023 */       this.size = size;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1031 */       return this.size;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/* 1039 */       appendTo(buffer, calendar.get(this.field));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(Appendable buffer, int value) throws IOException {
/* 1047 */       FastDatePrinter.appendFullDigits(buffer, value, this.size);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TwoDigitNumberField
/*      */     implements NumberRule
/*      */   {
/*      */     private final int field;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     TwoDigitNumberField(int field) {
/* 1063 */       this.field = field;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1071 */       return 2;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/* 1079 */       appendTo(buffer, calendar.get(this.field));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(Appendable buffer, int value) throws IOException {
/* 1087 */       if (value < 100) {
/* 1088 */         FastDatePrinter.appendDigits(buffer, value);
/*      */       } else {
/* 1090 */         FastDatePrinter.appendFullDigits(buffer, value, 2);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class TwoDigitYearField
/*      */     implements NumberRule
/*      */   {
/* 1099 */     static final TwoDigitYearField INSTANCE = new TwoDigitYearField();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1112 */       return 2;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/* 1120 */       appendTo(buffer, calendar.get(1) % 100);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(Appendable buffer, int value) throws IOException {
/* 1128 */       FastDatePrinter.appendDigits(buffer, value % 100);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class TwoDigitMonthField
/*      */     implements NumberRule
/*      */   {
/* 1136 */     static final TwoDigitMonthField INSTANCE = new TwoDigitMonthField();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1149 */       return 2;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/* 1157 */       appendTo(buffer, calendar.get(2) + 1);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(Appendable buffer, int value) throws IOException {
/* 1165 */       FastDatePrinter.appendDigits(buffer, value);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TwelveHourField
/*      */     implements NumberRule
/*      */   {
/*      */     private final FastDatePrinter.NumberRule rule;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     TwelveHourField(FastDatePrinter.NumberRule rule) {
/* 1182 */       this.rule = rule;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1190 */       return this.rule.estimateLength();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/* 1198 */       int value = calendar.get(10);
/* 1199 */       if (value == 0) {
/* 1200 */         value = calendar.getLeastMaximum(10) + 1;
/*      */       }
/* 1202 */       this.rule.appendTo(buffer, value);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, int value) throws IOException {
/* 1210 */       this.rule.appendTo(buffer, value);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TwentyFourHourField
/*      */     implements NumberRule
/*      */   {
/*      */     private final FastDatePrinter.NumberRule rule;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     TwentyFourHourField(FastDatePrinter.NumberRule rule) {
/* 1227 */       this.rule = rule;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1235 */       return this.rule.estimateLength();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/* 1243 */       int value = calendar.get(11);
/* 1244 */       if (value == 0) {
/* 1245 */         value = calendar.getMaximum(11) + 1;
/*      */       }
/* 1247 */       this.rule.appendTo(buffer, value);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, int value) throws IOException {
/* 1255 */       this.rule.appendTo(buffer, value);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class DayInWeekField
/*      */     implements NumberRule
/*      */   {
/*      */     private final FastDatePrinter.NumberRule rule;
/*      */     
/*      */     DayInWeekField(FastDatePrinter.NumberRule rule) {
/* 1266 */       this.rule = rule;
/*      */     }
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1271 */       return this.rule.estimateLength();
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/* 1276 */       int value = calendar.get(7);
/* 1277 */       this.rule.appendTo(buffer, (value == 1) ? 7 : (value - 1));
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, int value) throws IOException {
/* 1282 */       this.rule.appendTo(buffer, value);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class WeekYear
/*      */     implements NumberRule
/*      */   {
/*      */     private final FastDatePrinter.NumberRule rule;
/*      */     
/*      */     WeekYear(FastDatePrinter.NumberRule rule) {
/* 1293 */       this.rule = rule;
/*      */     }
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1298 */       return this.rule.estimateLength();
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/* 1303 */       this.rule.appendTo(buffer, calendar.getWeekYear());
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, int value) throws IOException {
/* 1308 */       this.rule.appendTo(buffer, value);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/* 1313 */   private static final ConcurrentMap<TimeZoneDisplayKey, String> cTimeZoneDisplayCache = new ConcurrentHashMap<>(7);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String getTimeZoneDisplay(TimeZone tz, boolean daylight, int style, Locale locale) {
/* 1326 */     TimeZoneDisplayKey key = new TimeZoneDisplayKey(tz, daylight, style, locale);
/*      */     
/* 1328 */     return cTimeZoneDisplayCache.computeIfAbsent(key, k -> tz.getDisplayName(daylight, style, locale));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TimeZoneNameRule
/*      */     implements Rule
/*      */   {
/*      */     private final Locale locale;
/*      */ 
/*      */     
/*      */     private final int style;
/*      */ 
/*      */     
/*      */     private final String standard;
/*      */     
/*      */     private final String daylight;
/*      */ 
/*      */     
/*      */     TimeZoneNameRule(TimeZone timeZone, Locale locale, int style) {
/* 1348 */       this.locale = LocaleUtils.toLocale(locale);
/* 1349 */       this.style = style;
/* 1350 */       this.standard = FastDatePrinter.getTimeZoneDisplay(timeZone, false, style, locale);
/* 1351 */       this.daylight = FastDatePrinter.getTimeZoneDisplay(timeZone, true, style, locale);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1362 */       return Math.max(this.standard.length(), this.daylight.length());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/* 1370 */       TimeZone zone = calendar.getTimeZone();
/* 1371 */       if (calendar.get(16) == 0) {
/* 1372 */         buffer.append(FastDatePrinter.getTimeZoneDisplay(zone, false, this.style, this.locale));
/*      */       } else {
/* 1374 */         buffer.append(FastDatePrinter.getTimeZoneDisplay(zone, true, this.style, this.locale));
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TimeZoneNumberRule
/*      */     implements Rule
/*      */   {
/* 1384 */     static final TimeZoneNumberRule INSTANCE_COLON = new TimeZoneNumberRule(true);
/* 1385 */     static final TimeZoneNumberRule INSTANCE_NO_COLON = new TimeZoneNumberRule(false);
/*      */ 
/*      */ 
/*      */     
/*      */     private final boolean colon;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     TimeZoneNumberRule(boolean colon) {
/* 1395 */       this.colon = colon;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1403 */       return 5;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/* 1412 */       int offset = calendar.get(15) + calendar.get(16);
/*      */       
/* 1414 */       if (offset < 0) {
/* 1415 */         buffer.append('-');
/* 1416 */         offset = -offset;
/*      */       } else {
/* 1418 */         buffer.append('+');
/*      */       } 
/*      */       
/* 1421 */       int hours = offset / 3600000;
/* 1422 */       FastDatePrinter.appendDigits(buffer, hours);
/*      */       
/* 1424 */       if (this.colon) {
/* 1425 */         buffer.append(':');
/*      */       }
/*      */       
/* 1428 */       int minutes = offset / 60000 - 60 * hours;
/* 1429 */       FastDatePrinter.appendDigits(buffer, minutes);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class Iso8601_Rule
/*      */     implements Rule
/*      */   {
/* 1440 */     static final Iso8601_Rule ISO8601_HOURS = new Iso8601_Rule(3);
/*      */     
/* 1442 */     static final Iso8601_Rule ISO8601_HOURS_MINUTES = new Iso8601_Rule(5);
/*      */     
/* 1444 */     static final Iso8601_Rule ISO8601_HOURS_COLON_MINUTES = new Iso8601_Rule(6);
/*      */ 
/*      */ 
/*      */     
/*      */     private final int length;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static Iso8601_Rule getRule(int tokenLen) {
/* 1454 */       switch (tokenLen) {
/*      */         case 1:
/* 1456 */           return ISO8601_HOURS;
/*      */         case 2:
/* 1458 */           return ISO8601_HOURS_MINUTES;
/*      */         case 3:
/* 1460 */           return ISO8601_HOURS_COLON_MINUTES;
/*      */       } 
/* 1462 */       throw new IllegalArgumentException("invalid number of X");
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
/*      */     Iso8601_Rule(int length) {
/* 1474 */       this.length = length;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1482 */       return this.length;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/* 1490 */       int offset = calendar.get(15) + calendar.get(16);
/* 1491 */       if (offset == 0) {
/* 1492 */         buffer.append("Z");
/*      */         
/*      */         return;
/*      */       } 
/* 1496 */       if (offset < 0) {
/* 1497 */         buffer.append('-');
/* 1498 */         offset = -offset;
/*      */       } else {
/* 1500 */         buffer.append('+');
/*      */       } 
/*      */       
/* 1503 */       int hours = offset / 3600000;
/* 1504 */       FastDatePrinter.appendDigits(buffer, hours);
/*      */       
/* 1506 */       if (this.length < 5) {
/*      */         return;
/*      */       }
/*      */       
/* 1510 */       if (this.length == 6) {
/* 1511 */         buffer.append(':');
/*      */       }
/*      */       
/* 1514 */       int minutes = offset / 60000 - 60 * hours;
/* 1515 */       FastDatePrinter.appendDigits(buffer, minutes);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TimeZoneDisplayKey
/*      */   {
/*      */     private final TimeZone timeZone;
/*      */ 
/*      */ 
/*      */     
/*      */     private final int style;
/*      */ 
/*      */ 
/*      */     
/*      */     private final Locale locale;
/*      */ 
/*      */ 
/*      */     
/*      */     TimeZoneDisplayKey(TimeZone timeZone, boolean daylight, int style, Locale locale) {
/* 1537 */       this.timeZone = timeZone;
/* 1538 */       if (daylight) {
/* 1539 */         this.style = style | Integer.MIN_VALUE;
/*      */       } else {
/* 1541 */         this.style = style;
/*      */       } 
/* 1543 */       this.locale = LocaleUtils.toLocale(locale);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1551 */       return (this.style * 31 + this.locale.hashCode()) * 31 + this.timeZone.hashCode();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/* 1559 */       if (this == obj) {
/* 1560 */         return true;
/*      */       }
/* 1562 */       if (obj instanceof TimeZoneDisplayKey) {
/* 1563 */         TimeZoneDisplayKey other = (TimeZoneDisplayKey)obj;
/* 1564 */         return (this.timeZone
/* 1565 */           .equals(other.timeZone) && this.style == other.style && this.locale
/*      */           
/* 1567 */           .equals(other.locale));
/*      */       } 
/* 1569 */       return false;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\time\FastDatePrinter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */