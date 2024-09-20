/*     */ package org.apache.commons.lang3.text;
/*     */ 
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class WordUtils
/*     */ {
/*     */   public static String wrap(String str, int wrapLength) {
/* 101 */     return wrap(str, wrapLength, null, false);
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
/*     */   public static String wrap(String str, int wrapLength, String newLineStr, boolean wrapLongWords) {
/* 178 */     return wrap(str, wrapLength, newLineStr, wrapLongWords, " ");
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
/*     */   public static String wrap(String str, int wrapLength, String newLineStr, boolean wrapLongWords, String wrapOn) {
/* 272 */     if (str == null) {
/* 273 */       return null;
/*     */     }
/* 275 */     if (newLineStr == null) {
/* 276 */       newLineStr = System.lineSeparator();
/*     */     }
/* 278 */     if (wrapLength < 1) {
/* 279 */       wrapLength = 1;
/*     */     }
/* 281 */     if (StringUtils.isBlank(wrapOn)) {
/* 282 */       wrapOn = " ";
/*     */     }
/* 284 */     Pattern patternToWrapOn = Pattern.compile(wrapOn);
/* 285 */     int inputLineLength = str.length();
/* 286 */     int offset = 0;
/* 287 */     StringBuilder wrappedLine = new StringBuilder(inputLineLength + 32);
/*     */     
/* 289 */     while (offset < inputLineLength) {
/* 290 */       int spaceToWrapAt = -1;
/* 291 */       Matcher matcher = patternToWrapOn.matcher(str
/* 292 */           .substring(offset, Math.min((int)Math.min(2147483647L, (offset + wrapLength) + 1L), inputLineLength)));
/* 293 */       if (matcher.find()) {
/* 294 */         if (matcher.start() == 0) {
/* 295 */           offset += matcher.end();
/*     */           continue;
/*     */         } 
/* 298 */         spaceToWrapAt = matcher.start() + offset;
/*     */       } 
/*     */ 
/*     */       
/* 302 */       if (inputLineLength - offset <= wrapLength) {
/*     */         break;
/*     */       }
/*     */       
/* 306 */       while (matcher.find()) {
/* 307 */         spaceToWrapAt = matcher.start() + offset;
/*     */       }
/*     */       
/* 310 */       if (spaceToWrapAt >= offset) {
/*     */         
/* 312 */         wrappedLine.append(str, offset, spaceToWrapAt);
/* 313 */         wrappedLine.append(newLineStr);
/* 314 */         offset = spaceToWrapAt + 1;
/*     */         continue;
/*     */       } 
/* 317 */       if (wrapLongWords) {
/*     */         
/* 319 */         wrappedLine.append(str, offset, wrapLength + offset);
/* 320 */         wrappedLine.append(newLineStr);
/* 321 */         offset += wrapLength;
/*     */         continue;
/*     */       } 
/* 324 */       matcher = patternToWrapOn.matcher(str.substring(offset + wrapLength));
/* 325 */       if (matcher.find()) {
/* 326 */         spaceToWrapAt = matcher.start() + offset + wrapLength;
/*     */       }
/*     */       
/* 329 */       if (spaceToWrapAt >= 0) {
/* 330 */         wrappedLine.append(str, offset, spaceToWrapAt);
/* 331 */         wrappedLine.append(newLineStr);
/* 332 */         offset = spaceToWrapAt + 1; continue;
/*     */       } 
/* 334 */       wrappedLine.append(str, offset, str.length());
/* 335 */       offset = inputLineLength;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 341 */     wrappedLine.append(str, offset, str.length());
/*     */     
/* 343 */     return wrappedLine.toString();
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
/*     */   public static String capitalize(String str) {
/* 370 */     return capitalize(str, null);
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
/* 403 */     int delimLen = (delimiters == null) ? -1 : delimiters.length;
/* 404 */     if (StringUtils.isEmpty(str) || delimLen == 0) {
/* 405 */       return str;
/*     */     }
/* 407 */     char[] buffer = str.toCharArray();
/* 408 */     boolean capitalizeNext = true;
/* 409 */     for (int i = 0; i < buffer.length; i++) {
/* 410 */       char ch = buffer[i];
/* 411 */       if (isDelimiter(ch, delimiters)) {
/* 412 */         capitalizeNext = true;
/* 413 */       } else if (capitalizeNext) {
/* 414 */         buffer[i] = Character.toTitleCase(ch);
/* 415 */         capitalizeNext = false;
/*     */       } 
/*     */     } 
/* 418 */     return new String(buffer);
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
/* 441 */     return capitalizeFully(str, null);
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
/*     */   public static String capitalizeFully(String str, char... delimiters) {
/* 471 */     int delimLen = (delimiters == null) ? -1 : delimiters.length;
/* 472 */     if (StringUtils.isEmpty(str) || delimLen == 0) {
/* 473 */       return str;
/*     */     }
/* 475 */     return capitalize(str.toLowerCase(), delimiters);
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
/* 496 */     return uncapitalize(str, null);
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
/* 525 */     int delimLen = (delimiters == null) ? -1 : delimiters.length;
/* 526 */     if (StringUtils.isEmpty(str) || delimLen == 0) {
/* 527 */       return str;
/*     */     }
/* 529 */     char[] buffer = str.toCharArray();
/* 530 */     boolean uncapitalizeNext = true;
/* 531 */     for (int i = 0; i < buffer.length; i++) {
/* 532 */       char ch = buffer[i];
/* 533 */       if (isDelimiter(ch, delimiters)) {
/* 534 */         uncapitalizeNext = true;
/* 535 */       } else if (uncapitalizeNext) {
/* 536 */         buffer[i] = Character.toLowerCase(ch);
/* 537 */         uncapitalizeNext = false;
/*     */       } 
/*     */     } 
/* 540 */     return new String(buffer);
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
/* 566 */     if (StringUtils.isEmpty(str)) {
/* 567 */       return str;
/*     */     }
/* 569 */     char[] buffer = str.toCharArray();
/*     */     
/* 571 */     boolean whitespace = true;
/*     */     
/* 573 */     for (int i = 0; i < buffer.length; i++) {
/* 574 */       char ch = buffer[i];
/* 575 */       if (Character.isUpperCase(ch) || Character.isTitleCase(ch)) {
/* 576 */         buffer[i] = Character.toLowerCase(ch);
/* 577 */         whitespace = false;
/* 578 */       } else if (Character.isLowerCase(ch)) {
/* 579 */         if (whitespace) {
/* 580 */           buffer[i] = Character.toTitleCase(ch);
/* 581 */           whitespace = false;
/*     */         } else {
/* 583 */           buffer[i] = Character.toUpperCase(ch);
/*     */         } 
/*     */       } else {
/* 586 */         whitespace = Character.isWhitespace(ch);
/*     */       } 
/*     */     } 
/* 589 */     return new String(buffer);
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
/*     */   public static String initials(String str) {
/* 614 */     return initials(str, null);
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
/*     */   public static String initials(String str, char... delimiters) {
/* 644 */     if (StringUtils.isEmpty(str)) {
/* 645 */       return str;
/*     */     }
/* 647 */     if (delimiters != null && delimiters.length == 0) {
/* 648 */       return "";
/*     */     }
/* 650 */     int strLen = str.length();
/* 651 */     char[] buf = new char[strLen / 2 + 1];
/* 652 */     int count = 0;
/* 653 */     boolean lastWasGap = true;
/* 654 */     for (int i = 0; i < strLen; i++) {
/* 655 */       char ch = str.charAt(i);
/* 656 */       if (isDelimiter(ch, delimiters)) {
/* 657 */         lastWasGap = true;
/* 658 */       } else if (lastWasGap) {
/* 659 */         buf[count++] = ch;
/* 660 */         lastWasGap = false;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 665 */     return new String(buf, 0, count);
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
/*     */   public static boolean containsAllWords(CharSequence word, CharSequence... words) {
/* 691 */     if (StringUtils.isEmpty(word) || ArrayUtils.isEmpty((Object[])words)) {
/* 692 */       return false;
/*     */     }
/* 694 */     for (CharSequence w : words) {
/* 695 */       if (StringUtils.isBlank(w)) {
/* 696 */         return false;
/*     */       }
/* 698 */       Pattern p = Pattern.compile(".*\\b" + w + "\\b.*");
/* 699 */       if (!p.matcher(word).matches()) {
/* 700 */         return false;
/*     */       }
/*     */     } 
/* 703 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isDelimiter(char ch, char[] delimiters) {
/* 714 */     return (delimiters == null) ? Character.isWhitespace(ch) : ArrayUtils.contains(delimiters, ch);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\text\WordUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */