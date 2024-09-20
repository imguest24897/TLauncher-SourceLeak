/*     */ package org.apache.commons.lang3.time;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.Objects;
/*     */ import java.util.TimeZone;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DurationFormatUtils
/*     */ {
/*     */   public static final String ISO_EXTENDED_FORMAT_PATTERN = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.SSS'S'";
/*     */   static final String y = "y";
/*     */   static final String M = "M";
/*     */   static final String d = "d";
/*     */   static final String H = "H";
/*     */   static final String m = "m";
/*     */   static final String s = "s";
/*     */   static final String S = "S";
/*     */   
/*     */   public static String formatDurationHMS(long durationMillis) {
/*  84 */     return formatDuration(durationMillis, "HH:mm:ss.SSS");
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
/*     */   public static String formatDurationISO(long durationMillis) {
/* 100 */     return formatDuration(durationMillis, "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.SSS'S'", false);
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
/*     */   public static String formatDuration(long durationMillis, String format) {
/* 115 */     return formatDuration(durationMillis, format, true);
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
/*     */   public static String formatDuration(long durationMillis, String format, boolean padWithZeros) {
/* 132 */     Validate.inclusiveBetween(0L, Long.MAX_VALUE, durationMillis, "durationMillis must not be negative");
/*     */     
/* 134 */     Token[] tokens = lexx(format);
/*     */     
/* 136 */     long days = 0L;
/* 137 */     long hours = 0L;
/* 138 */     long minutes = 0L;
/* 139 */     long seconds = 0L;
/* 140 */     long milliseconds = durationMillis;
/*     */     
/* 142 */     if (Token.containsTokenWithValue(tokens, "d")) {
/* 143 */       days = milliseconds / 86400000L;
/* 144 */       milliseconds -= days * 86400000L;
/*     */     } 
/* 146 */     if (Token.containsTokenWithValue(tokens, "H")) {
/* 147 */       hours = milliseconds / 3600000L;
/* 148 */       milliseconds -= hours * 3600000L;
/*     */     } 
/* 150 */     if (Token.containsTokenWithValue(tokens, "m")) {
/* 151 */       minutes = milliseconds / 60000L;
/* 152 */       milliseconds -= minutes * 60000L;
/*     */     } 
/* 154 */     if (Token.containsTokenWithValue(tokens, "s")) {
/* 155 */       seconds = milliseconds / 1000L;
/* 156 */       milliseconds -= seconds * 1000L;
/*     */     } 
/*     */     
/* 159 */     return format(tokens, 0L, 0L, days, hours, minutes, seconds, milliseconds, padWithZeros);
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
/*     */   public static String formatDurationWords(long durationMillis, boolean suppressLeadingZeroElements, boolean suppressTrailingZeroElements) {
/* 182 */     String duration = formatDuration(durationMillis, "d' days 'H' hours 'm' minutes 's' seconds'");
/* 183 */     if (suppressLeadingZeroElements) {
/*     */       
/* 185 */       duration = " " + duration;
/* 186 */       String tmp = StringUtils.replaceOnce(duration, " 0 days", "");
/* 187 */       if (tmp.length() != duration.length()) {
/* 188 */         duration = tmp;
/* 189 */         tmp = StringUtils.replaceOnce(duration, " 0 hours", "");
/* 190 */         if (tmp.length() != duration.length()) {
/* 191 */           duration = tmp;
/* 192 */           tmp = StringUtils.replaceOnce(duration, " 0 minutes", "");
/* 193 */           duration = tmp;
/*     */         } 
/*     */       } 
/* 196 */       if (!duration.isEmpty())
/*     */       {
/* 198 */         duration = duration.substring(1);
/*     */       }
/*     */     } 
/* 201 */     if (suppressTrailingZeroElements) {
/* 202 */       String tmp = StringUtils.replaceOnce(duration, " 0 seconds", "");
/* 203 */       if (tmp.length() != duration.length()) {
/* 204 */         duration = tmp;
/* 205 */         tmp = StringUtils.replaceOnce(duration, " 0 minutes", "");
/* 206 */         if (tmp.length() != duration.length()) {
/* 207 */           duration = tmp;
/* 208 */           tmp = StringUtils.replaceOnce(duration, " 0 hours", "");
/* 209 */           if (tmp.length() != duration.length()) {
/* 210 */             duration = StringUtils.replaceOnce(tmp, " 0 days", "");
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 216 */     duration = " " + duration;
/* 217 */     duration = StringUtils.replaceOnce(duration, " 1 seconds", " 1 second");
/* 218 */     duration = StringUtils.replaceOnce(duration, " 1 minutes", " 1 minute");
/* 219 */     duration = StringUtils.replaceOnce(duration, " 1 hours", " 1 hour");
/* 220 */     duration = StringUtils.replaceOnce(duration, " 1 days", " 1 day");
/* 221 */     return duration.trim();
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
/*     */   public static String formatPeriodISO(long startMillis, long endMillis) {
/* 235 */     return formatPeriod(startMillis, endMillis, "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.SSS'S'", false, TimeZone.getDefault());
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
/*     */   public static String formatPeriod(long startMillis, long endMillis, String format) {
/* 249 */     return formatPeriod(startMillis, endMillis, format, true, TimeZone.getDefault());
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
/*     */   public static String formatPeriod(long startMillis, long endMillis, String format, boolean padWithZeros, TimeZone timezone) {
/* 278 */     Validate.isTrue((startMillis <= endMillis), "startMillis must not be greater than endMillis", new Object[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 287 */     Token[] tokens = lexx(format);
/*     */ 
/*     */ 
/*     */     
/* 291 */     Calendar start = Calendar.getInstance(timezone);
/* 292 */     start.setTime(new Date(startMillis));
/* 293 */     Calendar end = Calendar.getInstance(timezone);
/* 294 */     end.setTime(new Date(endMillis));
/*     */ 
/*     */     
/* 297 */     int milliseconds = end.get(14) - start.get(14);
/* 298 */     int seconds = end.get(13) - start.get(13);
/* 299 */     int minutes = end.get(12) - start.get(12);
/* 300 */     int hours = end.get(11) - start.get(11);
/* 301 */     int days = end.get(5) - start.get(5);
/* 302 */     int months = end.get(2) - start.get(2);
/* 303 */     int years = end.get(1) - start.get(1);
/*     */ 
/*     */     
/* 306 */     while (milliseconds < 0) {
/* 307 */       milliseconds += 1000;
/* 308 */       seconds--;
/*     */     } 
/* 310 */     while (seconds < 0) {
/* 311 */       seconds += 60;
/* 312 */       minutes--;
/*     */     } 
/* 314 */     while (minutes < 0) {
/* 315 */       minutes += 60;
/* 316 */       hours--;
/*     */     } 
/* 318 */     while (hours < 0) {
/* 319 */       hours += 24;
/* 320 */       days--;
/*     */     } 
/*     */     
/* 323 */     if (Token.containsTokenWithValue(tokens, "M")) {
/* 324 */       while (days < 0) {
/* 325 */         days += start.getActualMaximum(5);
/* 326 */         months--;
/* 327 */         start.add(2, 1);
/*     */       } 
/*     */       
/* 330 */       while (months < 0) {
/* 331 */         months += 12;
/* 332 */         years--;
/*     */       } 
/*     */       
/* 335 */       if (!Token.containsTokenWithValue(tokens, "y") && years != 0) {
/* 336 */         while (years != 0) {
/* 337 */           months += 12 * years;
/* 338 */           years = 0;
/*     */         }
/*     */       
/*     */       }
/*     */     } else {
/*     */       
/* 344 */       if (!Token.containsTokenWithValue(tokens, "y")) {
/* 345 */         int target = end.get(1);
/* 346 */         if (months < 0)
/*     */         {
/* 348 */           target--;
/*     */         }
/*     */         
/* 351 */         while (start.get(1) != target) {
/* 352 */           days += start.getActualMaximum(6) - start.get(6);
/*     */ 
/*     */           
/* 355 */           if (start instanceof java.util.GregorianCalendar && start
/* 356 */             .get(2) == 1 && start
/* 357 */             .get(5) == 29) {
/* 358 */             days++;
/*     */           }
/*     */           
/* 361 */           start.add(1, 1);
/*     */           
/* 363 */           days += start.get(6);
/*     */         } 
/*     */         
/* 366 */         years = 0;
/*     */       } 
/*     */       
/* 369 */       while (start.get(2) != end.get(2)) {
/* 370 */         days += start.getActualMaximum(5);
/* 371 */         start.add(2, 1);
/*     */       } 
/*     */       
/* 374 */       months = 0;
/*     */       
/* 376 */       while (days < 0) {
/* 377 */         days += start.getActualMaximum(5);
/* 378 */         months--;
/* 379 */         start.add(2, 1);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 388 */     if (!Token.containsTokenWithValue(tokens, "d")) {
/* 389 */       hours += 24 * days;
/* 390 */       days = 0;
/*     */     } 
/* 392 */     if (!Token.containsTokenWithValue(tokens, "H")) {
/* 393 */       minutes += 60 * hours;
/* 394 */       hours = 0;
/*     */     } 
/* 396 */     if (!Token.containsTokenWithValue(tokens, "m")) {
/* 397 */       seconds += 60 * minutes;
/* 398 */       minutes = 0;
/*     */     } 
/* 400 */     if (!Token.containsTokenWithValue(tokens, "s")) {
/* 401 */       milliseconds += 1000 * seconds;
/* 402 */       seconds = 0;
/*     */     } 
/*     */     
/* 405 */     return format(tokens, years, months, days, hours, minutes, seconds, milliseconds, padWithZeros);
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
/*     */   static String format(Token[] tokens, long years, long months, long days, long hours, long minutes, long seconds, long milliseconds, boolean padWithZeros) {
/* 424 */     StringBuilder buffer = new StringBuilder();
/* 425 */     boolean lastOutputSeconds = false;
/* 426 */     for (Token token : tokens) {
/* 427 */       Object value = token.getValue();
/* 428 */       int count = token.getCount();
/* 429 */       if (value instanceof StringBuilder) {
/* 430 */         buffer.append(value.toString());
/* 431 */       } else if (value.equals("y")) {
/* 432 */         buffer.append(paddedValue(years, padWithZeros, count));
/* 433 */         lastOutputSeconds = false;
/* 434 */       } else if (value.equals("M")) {
/* 435 */         buffer.append(paddedValue(months, padWithZeros, count));
/* 436 */         lastOutputSeconds = false;
/* 437 */       } else if (value.equals("d")) {
/* 438 */         buffer.append(paddedValue(days, padWithZeros, count));
/* 439 */         lastOutputSeconds = false;
/* 440 */       } else if (value.equals("H")) {
/* 441 */         buffer.append(paddedValue(hours, padWithZeros, count));
/* 442 */         lastOutputSeconds = false;
/* 443 */       } else if (value.equals("m")) {
/* 444 */         buffer.append(paddedValue(minutes, padWithZeros, count));
/* 445 */         lastOutputSeconds = false;
/* 446 */       } else if (value.equals("s")) {
/* 447 */         buffer.append(paddedValue(seconds, padWithZeros, count));
/* 448 */         lastOutputSeconds = true;
/* 449 */       } else if (value.equals("S")) {
/* 450 */         if (lastOutputSeconds) {
/*     */           
/* 452 */           int width = padWithZeros ? Math.max(3, count) : 3;
/* 453 */           buffer.append(paddedValue(milliseconds, true, width));
/*     */         } else {
/* 455 */           buffer.append(paddedValue(milliseconds, padWithZeros, count));
/*     */         } 
/* 457 */         lastOutputSeconds = false;
/*     */       } 
/*     */     } 
/* 460 */     return buffer.toString();
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
/*     */   private static String paddedValue(long value, boolean padWithZeros, int count) {
/* 473 */     String longString = Long.toString(value);
/* 474 */     return padWithZeros ? StringUtils.leftPad(longString, count, '0') : longString;
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
/*     */   static Token[] lexx(String format) {
/* 492 */     ArrayList<Token> list = new ArrayList<>(format.length());
/*     */     
/* 494 */     boolean inLiteral = false;
/*     */ 
/*     */     
/* 497 */     StringBuilder buffer = null;
/* 498 */     Token previous = null;
/* 499 */     for (int i = 0; i < format.length(); i++) {
/* 500 */       char ch = format.charAt(i);
/* 501 */       if (inLiteral && ch != '\'') {
/* 502 */         buffer.append(ch);
/*     */       } else {
/*     */         
/* 505 */         String value = null;
/* 506 */         switch (ch) {
/*     */           
/*     */           case '\'':
/* 509 */             if (inLiteral) {
/* 510 */               buffer = null;
/* 511 */               inLiteral = false; break;
/*     */             } 
/* 513 */             buffer = new StringBuilder();
/* 514 */             list.add(new Token(buffer));
/* 515 */             inLiteral = true;
/*     */             break;
/*     */           
/*     */           case 'y':
/* 519 */             value = "y";
/*     */             break;
/*     */           case 'M':
/* 522 */             value = "M";
/*     */             break;
/*     */           case 'd':
/* 525 */             value = "d";
/*     */             break;
/*     */           case 'H':
/* 528 */             value = "H";
/*     */             break;
/*     */           case 'm':
/* 531 */             value = "m";
/*     */             break;
/*     */           case 's':
/* 534 */             value = "s";
/*     */             break;
/*     */           case 'S':
/* 537 */             value = "S";
/*     */             break;
/*     */           default:
/* 540 */             if (buffer == null) {
/* 541 */               buffer = new StringBuilder();
/* 542 */               list.add(new Token(buffer));
/*     */             } 
/* 544 */             buffer.append(ch);
/*     */             break;
/*     */         } 
/* 547 */         if (value != null) {
/* 548 */           if (previous != null && previous.getValue().equals(value)) {
/* 549 */             previous.increment();
/*     */           } else {
/* 551 */             Token token = new Token(value);
/* 552 */             list.add(token);
/* 553 */             previous = token;
/*     */           } 
/* 555 */           buffer = null;
/*     */         } 
/*     */       } 
/* 558 */     }  if (inLiteral) {
/* 559 */       throw new IllegalArgumentException("Unmatched quote in format: " + format);
/*     */     }
/* 561 */     return list.<Token>toArray(Token.EMPTY_ARRAY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Token
/*     */   {
/* 570 */     private static final Token[] EMPTY_ARRAY = new Token[0];
/*     */ 
/*     */     
/*     */     private final Object value;
/*     */ 
/*     */     
/*     */     private int count;
/*     */ 
/*     */     
/*     */     static boolean containsTokenWithValue(Token[] tokens, Object value) {
/* 580 */       return Stream.<Token>of(tokens).anyMatch(token -> (token.getValue() == value));
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
/*     */     Token(Object value) {
/* 592 */       this(value, 1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Token(Object value, int count) {
/* 603 */       this.value = Objects.requireNonNull(value, "value");
/* 604 */       this.count = count;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void increment() {
/* 611 */       this.count++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int getCount() {
/* 620 */       return this.count;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Object getValue() {
/* 629 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj2) {
/* 640 */       if (obj2 instanceof Token) {
/* 641 */         Token tok2 = (Token)obj2;
/* 642 */         if (this.value.getClass() != tok2.value.getClass()) {
/* 643 */           return false;
/*     */         }
/* 645 */         if (this.count != tok2.count) {
/* 646 */           return false;
/*     */         }
/* 648 */         if (this.value instanceof StringBuilder) {
/* 649 */           return this.value.toString().equals(tok2.value.toString());
/*     */         }
/* 651 */         if (this.value instanceof Number) {
/* 652 */           return this.value.equals(tok2.value);
/*     */         }
/* 654 */         return (this.value == tok2.value);
/*     */       } 
/* 656 */       return false;
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
/*     */     public int hashCode() {
/* 668 */       return this.value.hashCode();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 678 */       return StringUtils.repeat(this.value.toString(), this.count);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\time\DurationFormatUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */