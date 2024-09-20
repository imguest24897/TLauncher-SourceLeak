/*     */ package org.apache.commons.text;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.lang3.ArrayUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WordUtils
/*     */ {
/*     */   public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
/*  80 */     Validate.isTrue((upper >= -1), "upper value cannot be less than -1", new Object[0]);
/*  81 */     Validate.isTrue((upper >= lower || upper == -1), "upper value is less than lower value", new Object[0]);
/*  82 */     if (StringUtils.isEmpty(str)) {
/*  83 */       return str;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  88 */     if (lower > str.length()) {
/*  89 */       lower = str.length();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  94 */     if (upper == -1 || upper > str.length()) {
/*  95 */       upper = str.length();
/*     */     }
/*     */     
/*  98 */     StringBuilder result = new StringBuilder();
/*  99 */     int index = StringUtils.indexOf(str, " ", lower);
/* 100 */     if (index == -1) {
/* 101 */       result.append(str, 0, upper);
/*     */       
/* 103 */       if (upper != str.length()) {
/* 104 */         result.append(StringUtils.defaultString(appendToEnd));
/*     */       }
/*     */     } else {
/* 107 */       result.append(str, 0, Math.min(index, upper));
/* 108 */       result.append(StringUtils.defaultString(appendToEnd));
/*     */     } 
/*     */     
/* 111 */     return result.toString();
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
/*     */   public static String capitalize(String str) {
/* 137 */     return capitalize(str, null);
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
/*     */   public static String capitalize(String str, char... delimiters) {
/* 170 */     if (StringUtils.isEmpty(str)) {
/* 171 */       return str;
/*     */     }
/* 173 */     Set<Integer> delimiterSet = generateDelimiterSet(delimiters);
/* 174 */     int strLen = str.length();
/* 175 */     int[] newCodePoints = new int[strLen];
/* 176 */     int outOffset = 0;
/*     */     
/* 178 */     boolean capitalizeNext = true; int index;
/* 179 */     for (index = 0; index < strLen; ) {
/* 180 */       int codePoint = str.codePointAt(index);
/*     */       
/* 182 */       if (delimiterSet.contains(Integer.valueOf(codePoint))) {
/* 183 */         capitalizeNext = true;
/* 184 */         newCodePoints[outOffset++] = codePoint;
/* 185 */         index += Character.charCount(codePoint); continue;
/* 186 */       }  if (capitalizeNext) {
/* 187 */         int titleCaseCodePoint = Character.toTitleCase(codePoint);
/* 188 */         newCodePoints[outOffset++] = titleCaseCodePoint;
/* 189 */         index += Character.charCount(titleCaseCodePoint);
/* 190 */         capitalizeNext = false; continue;
/*     */       } 
/* 192 */       newCodePoints[outOffset++] = codePoint;
/* 193 */       index += Character.charCount(codePoint);
/*     */     } 
/*     */     
/* 196 */     return new String(newCodePoints, 0, outOffset);
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
/*     */   public static String capitalizeFully(String str) {
/* 219 */     return capitalizeFully(str, null);
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
/*     */   public static String capitalizeFully(String str, char... delimiters) {
/* 248 */     if (StringUtils.isEmpty(str)) {
/* 249 */       return str;
/*     */     }
/* 251 */     str = str.toLowerCase();
/* 252 */     return capitalize(str, delimiters);
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
/*     */   public static boolean containsAllWords(CharSequence word, CharSequence... words) {
/* 277 */     if (StringUtils.isEmpty(word) || ArrayUtils.isEmpty((Object[])words)) {
/* 278 */       return false;
/*     */     }
/* 280 */     for (CharSequence w : words) {
/* 281 */       if (StringUtils.isBlank(w)) {
/* 282 */         return false;
/*     */       }
/* 284 */       Pattern p = Pattern.compile(".*\\b" + w + "\\b.*");
/* 285 */       if (!p.matcher(word).matches()) {
/* 286 */         return false;
/*     */       }
/*     */     } 
/* 289 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Set<Integer> generateDelimiterSet(char[] delimiters) {
/* 300 */     Set<Integer> delimiterHashSet = new HashSet<>();
/* 301 */     if (delimiters == null || delimiters.length == 0) {
/* 302 */       if (delimiters == null) {
/* 303 */         delimiterHashSet.add(Integer.valueOf(Character.codePointAt(new char[] { ' ' }, 0)));
/*     */       }
/*     */       
/* 306 */       return delimiterHashSet;
/*     */     } 
/*     */     
/* 309 */     for (int index = 0; index < delimiters.length; index++) {
/* 310 */       delimiterHashSet.add(Integer.valueOf(Character.codePointAt(delimiters, index)));
/*     */     }
/* 312 */     return delimiterHashSet;
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
/*     */   public static String initials(String str) {
/* 336 */     return initials(str, null);
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
/*     */   public static String initials(String str, char... delimiters) {
/* 365 */     if (StringUtils.isEmpty(str)) {
/* 366 */       return str;
/*     */     }
/* 368 */     if (delimiters != null && delimiters.length == 0) {
/* 369 */       return "";
/*     */     }
/* 371 */     Set<Integer> delimiterSet = generateDelimiterSet(delimiters);
/* 372 */     int strLen = str.length();
/* 373 */     int[] newCodePoints = new int[strLen / 2 + 1];
/* 374 */     int count = 0;
/* 375 */     boolean lastWasGap = true; int i;
/* 376 */     for (i = 0; i < strLen; ) {
/* 377 */       int codePoint = str.codePointAt(i);
/*     */       
/* 379 */       if (delimiterSet.contains(Integer.valueOf(codePoint)) || (delimiters == null && Character.isWhitespace(codePoint))) {
/* 380 */         lastWasGap = true;
/* 381 */       } else if (lastWasGap) {
/* 382 */         newCodePoints[count++] = codePoint;
/* 383 */         lastWasGap = false;
/*     */       } 
/*     */       
/* 386 */       i += Character.charCount(codePoint);
/*     */     } 
/* 388 */     return new String(newCodePoints, 0, count);
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
/*     */   @Deprecated
/*     */   public static boolean isDelimiter(char ch, char[] delimiters) {
/* 401 */     if (delimiters == null) {
/* 402 */       return Character.isWhitespace(ch);
/*     */     }
/* 404 */     for (char delimiter : delimiters) {
/* 405 */       if (ch == delimiter) {
/* 406 */         return true;
/*     */       }
/*     */     } 
/* 409 */     return false;
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
/*     */   @Deprecated
/*     */   public static boolean isDelimiter(int codePoint, char[] delimiters) {
/* 422 */     if (delimiters == null) {
/* 423 */       return Character.isWhitespace(codePoint);
/*     */     }
/* 425 */     for (int index = 0; index < delimiters.length; index++) {
/* 426 */       int delimiterCodePoint = Character.codePointAt(delimiters, index);
/* 427 */       if (delimiterCodePoint == codePoint) {
/* 428 */         return true;
/*     */       }
/*     */     } 
/* 431 */     return false;
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
/*     */   public static String swapCase(String str) {
/* 457 */     if (StringUtils.isEmpty(str)) {
/* 458 */       return str;
/*     */     }
/* 460 */     int strLen = str.length();
/* 461 */     int[] newCodePoints = new int[strLen];
/* 462 */     int outOffset = 0;
/* 463 */     boolean whitespace = true; int index;
/* 464 */     for (index = 0; index < strLen; ) {
/* 465 */       int newCodePoint, oldCodepoint = str.codePointAt(index);
/*     */       
/* 467 */       if (Character.isUpperCase(oldCodepoint) || Character.isTitleCase(oldCodepoint)) {
/* 468 */         newCodePoint = Character.toLowerCase(oldCodepoint);
/* 469 */         whitespace = false;
/* 470 */       } else if (Character.isLowerCase(oldCodepoint)) {
/* 471 */         if (whitespace) {
/* 472 */           newCodePoint = Character.toTitleCase(oldCodepoint);
/* 473 */           whitespace = false;
/*     */         } else {
/* 475 */           newCodePoint = Character.toUpperCase(oldCodepoint);
/*     */         } 
/*     */       } else {
/* 478 */         whitespace = Character.isWhitespace(oldCodepoint);
/* 479 */         newCodePoint = oldCodepoint;
/*     */       } 
/* 481 */       newCodePoints[outOffset++] = newCodePoint;
/* 482 */       index += Character.charCount(newCodePoint);
/*     */     } 
/* 484 */     return new String(newCodePoints, 0, outOffset);
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
/*     */   public static String uncapitalize(String str) {
/* 505 */     return uncapitalize(str, null);
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
/*     */   public static String uncapitalize(String str, char... delimiters) {
/* 534 */     if (StringUtils.isEmpty(str)) {
/* 535 */       return str;
/*     */     }
/* 537 */     Set<Integer> delimiterSet = generateDelimiterSet(delimiters);
/* 538 */     int strLen = str.length();
/* 539 */     int[] newCodePoints = new int[strLen];
/* 540 */     int outOffset = 0;
/*     */     
/* 542 */     boolean uncapitalizeNext = true; int index;
/* 543 */     for (index = 0; index < strLen; ) {
/* 544 */       int codePoint = str.codePointAt(index);
/*     */       
/* 546 */       if (delimiterSet.contains(Integer.valueOf(codePoint))) {
/* 547 */         uncapitalizeNext = true;
/* 548 */         newCodePoints[outOffset++] = codePoint;
/* 549 */         index += Character.charCount(codePoint); continue;
/* 550 */       }  if (uncapitalizeNext) {
/* 551 */         int titleCaseCodePoint = Character.toLowerCase(codePoint);
/* 552 */         newCodePoints[outOffset++] = titleCaseCodePoint;
/* 553 */         index += Character.charCount(titleCaseCodePoint);
/* 554 */         uncapitalizeNext = false; continue;
/*     */       } 
/* 556 */       newCodePoints[outOffset++] = codePoint;
/* 557 */       index += Character.charCount(codePoint);
/*     */     } 
/*     */     
/* 560 */     return new String(newCodePoints, 0, outOffset);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String wrap(String str, int wrapLength) {
/* 613 */     return wrap(str, wrapLength, null, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String wrap(String str, int wrapLength, String newLineStr, boolean wrapLongWords) {
/* 695 */     return wrap(str, wrapLength, newLineStr, wrapLongWords, " ");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String wrap(String str, int wrapLength, String newLineStr, boolean wrapLongWords, String wrapOn) {
/* 795 */     if (str == null) {
/* 796 */       return null;
/*     */     }
/* 798 */     if (newLineStr == null) {
/* 799 */       newLineStr = System.lineSeparator();
/*     */     }
/* 801 */     if (wrapLength < 1) {
/* 802 */       wrapLength = 1;
/*     */     }
/* 804 */     if (StringUtils.isBlank(wrapOn)) {
/* 805 */       wrapOn = " ";
/*     */     }
/* 807 */     Pattern patternToWrapOn = Pattern.compile(wrapOn);
/* 808 */     int inputLineLength = str.length();
/* 809 */     int offset = 0;
/* 810 */     StringBuilder wrappedLine = new StringBuilder(inputLineLength + 32);
/* 811 */     int matcherSize = -1;
/*     */     
/* 813 */     while (offset < inputLineLength) {
/* 814 */       int spaceToWrapAt = -1;
/* 815 */       Matcher matcher = patternToWrapOn.matcher(str.substring(offset, 
/* 816 */             Math.min((int)Math.min(2147483647L, (offset + wrapLength) + 1L), inputLineLength)));
/* 817 */       if (matcher.find()) {
/* 818 */         if (matcher.start() == 0) {
/* 819 */           matcherSize = matcher.end();
/* 820 */           if (matcherSize != 0) {
/* 821 */             offset += matcher.end();
/*     */             continue;
/*     */           } 
/* 824 */           offset++;
/*     */         } 
/* 826 */         spaceToWrapAt = matcher.start() + offset;
/*     */       } 
/*     */ 
/*     */       
/* 830 */       if (inputLineLength - offset <= wrapLength) {
/*     */         break;
/*     */       }
/*     */       
/* 834 */       while (matcher.find()) {
/* 835 */         spaceToWrapAt = matcher.start() + offset;
/*     */       }
/*     */       
/* 838 */       if (spaceToWrapAt >= offset) {
/*     */         
/* 840 */         wrappedLine.append(str, offset, spaceToWrapAt);
/* 841 */         wrappedLine.append(newLineStr);
/* 842 */         offset = spaceToWrapAt + 1;
/*     */         continue;
/*     */       } 
/* 845 */       if (wrapLongWords) {
/* 846 */         if (matcherSize == 0) {
/* 847 */           offset--;
/*     */         }
/*     */         
/* 850 */         wrappedLine.append(str, offset, wrapLength + offset);
/* 851 */         wrappedLine.append(newLineStr);
/* 852 */         offset += wrapLength;
/* 853 */         matcherSize = -1;
/*     */         continue;
/*     */       } 
/* 856 */       matcher = patternToWrapOn.matcher(str.substring(offset + wrapLength));
/* 857 */       if (matcher.find()) {
/* 858 */         matcherSize = matcher.end() - matcher.start();
/* 859 */         spaceToWrapAt = matcher.start() + offset + wrapLength;
/*     */       } 
/*     */       
/* 862 */       if (spaceToWrapAt >= 0) {
/* 863 */         if (matcherSize == 0 && offset != 0) {
/* 864 */           offset--;
/*     */         }
/* 866 */         wrappedLine.append(str, offset, spaceToWrapAt);
/* 867 */         wrappedLine.append(newLineStr);
/* 868 */         offset = spaceToWrapAt + 1; continue;
/*     */       } 
/* 870 */       if (matcherSize == 0 && offset != 0) {
/* 871 */         offset--;
/*     */       }
/* 873 */       wrappedLine.append(str, offset, str.length());
/* 874 */       offset = inputLineLength;
/* 875 */       matcherSize = -1;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 880 */     if (matcherSize == 0 && offset < inputLineLength) {
/* 881 */       offset--;
/*     */     }
/*     */ 
/*     */     
/* 885 */     wrappedLine.append(str, offset, str.length());
/*     */     
/* 887 */     return wrappedLine.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\WordUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */