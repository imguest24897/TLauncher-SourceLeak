/*      */ package org.apache.commons.lang3;
/*      */ 
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.nio.charset.Charset;
/*      */ import java.text.Normalizer;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.regex.Pattern;
/*      */ import org.apache.commons.lang3.function.Suppliers;
/*      */ import org.apache.commons.lang3.function.ToBooleanBiFunction;
/*      */ import org.apache.commons.lang3.stream.LangCollectors;
/*      */ import org.apache.commons.lang3.stream.Streams;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class StringUtils
/*      */ {
/*      */   public static final String SPACE = " ";
/*      */   public static final String EMPTY = "";
/*      */   public static final String LF = "\n";
/*      */   public static final String CR = "\r";
/*      */   public static final int INDEX_NOT_FOUND = -1;
/*      */   private static final int PAD_LIMIT = 8192;
/*  188 */   private static final Pattern STRIP_ACCENTS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String abbreviate(String str, int maxWidth) {
/*  222 */     return abbreviate(str, "...", 0, maxWidth);
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
/*      */   public static String abbreviate(String str, int offset, int maxWidth) {
/*  261 */     return abbreviate(str, "...", offset, maxWidth);
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
/*      */   public static String abbreviate(String str, String abbrevMarker, int maxWidth) {
/*  301 */     return abbreviate(str, abbrevMarker, 0, maxWidth);
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
/*      */   public static String abbreviate(String str, String abbrevMarker, int offset, int maxWidth) {
/*  341 */     if (isNotEmpty(str) && "".equals(abbrevMarker) && maxWidth > 0) {
/*  342 */       return substring(str, 0, maxWidth);
/*      */     }
/*  344 */     if (isAnyEmpty(new CharSequence[] { str, abbrevMarker })) {
/*  345 */       return str;
/*      */     }
/*  347 */     int abbrevMarkerLength = abbrevMarker.length();
/*  348 */     int minAbbrevWidth = abbrevMarkerLength + 1;
/*  349 */     int minAbbrevWidthOffset = abbrevMarkerLength + abbrevMarkerLength + 1;
/*      */     
/*  351 */     if (maxWidth < minAbbrevWidth) {
/*  352 */       throw new IllegalArgumentException(String.format("Minimum abbreviation width is %d", new Object[] { Integer.valueOf(minAbbrevWidth) }));
/*      */     }
/*  354 */     int strLen = str.length();
/*  355 */     if (strLen <= maxWidth) {
/*  356 */       return str;
/*      */     }
/*  358 */     if (offset > strLen) {
/*  359 */       offset = strLen;
/*      */     }
/*  361 */     if (strLen - offset < maxWidth - abbrevMarkerLength) {
/*  362 */       offset = strLen - maxWidth - abbrevMarkerLength;
/*      */     }
/*  364 */     if (offset <= abbrevMarkerLength + 1) {
/*  365 */       return str.substring(0, maxWidth - abbrevMarkerLength) + abbrevMarker;
/*      */     }
/*  367 */     if (maxWidth < minAbbrevWidthOffset) {
/*  368 */       throw new IllegalArgumentException(String.format("Minimum abbreviation width with offset is %d", new Object[] { Integer.valueOf(minAbbrevWidthOffset) }));
/*      */     }
/*  370 */     if (offset + maxWidth - abbrevMarkerLength < strLen) {
/*  371 */       return abbrevMarker + abbreviate(str.substring(offset), abbrevMarker, maxWidth - abbrevMarkerLength);
/*      */     }
/*  373 */     return abbrevMarker + str.substring(strLen - maxWidth - abbrevMarkerLength);
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
/*      */   public static String abbreviateMiddle(String str, String middle, int length) {
/*  406 */     if (isAnyEmpty(new CharSequence[] { str, middle }) || length >= str.length() || length < middle.length() + 2) {
/*  407 */       return str;
/*      */     }
/*      */     
/*  410 */     int targetSting = length - middle.length();
/*  411 */     int startOffset = targetSting / 2 + targetSting % 2;
/*  412 */     int endOffset = str.length() - targetSting / 2;
/*      */     
/*  414 */     return str.substring(0, startOffset) + middle + str
/*      */       
/*  416 */       .substring(endOffset);
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
/*      */   private static String appendIfMissing(String str, CharSequence suffix, boolean ignoreCase, CharSequence... suffixes) {
/*  431 */     if (str == null || isEmpty(suffix) || endsWith(str, suffix, ignoreCase)) {
/*  432 */       return str;
/*      */     }
/*  434 */     if (ArrayUtils.isNotEmpty(suffixes)) {
/*  435 */       for (CharSequence s : suffixes) {
/*  436 */         if (endsWith(str, s, ignoreCase)) {
/*  437 */           return str;
/*      */         }
/*      */       } 
/*      */     }
/*  441 */     return str + suffix.toString();
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
/*      */   public static String appendIfMissing(String str, CharSequence suffix, CharSequence... suffixes) {
/*  479 */     return appendIfMissing(str, suffix, false, suffixes);
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
/*      */   public static String appendIfMissingIgnoreCase(String str, CharSequence suffix, CharSequence... suffixes) {
/*  517 */     return appendIfMissing(str, suffix, true, suffixes);
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
/*      */   public static String capitalize(String str) {
/*  542 */     int strLen = length(str);
/*  543 */     if (strLen == 0) {
/*  544 */       return str;
/*      */     }
/*      */     
/*  547 */     int firstCodepoint = str.codePointAt(0);
/*  548 */     int newCodePoint = Character.toTitleCase(firstCodepoint);
/*  549 */     if (firstCodepoint == newCodePoint)
/*      */     {
/*  551 */       return str;
/*      */     }
/*      */     
/*  554 */     int[] newCodePoints = new int[strLen];
/*  555 */     int outOffset = 0;
/*  556 */     newCodePoints[outOffset++] = newCodePoint; int inOffset;
/*  557 */     for (inOffset = Character.charCount(firstCodepoint); inOffset < strLen; ) {
/*  558 */       int codePoint = str.codePointAt(inOffset);
/*  559 */       newCodePoints[outOffset++] = codePoint;
/*  560 */       inOffset += Character.charCount(codePoint);
/*      */     } 
/*  562 */     return new String(newCodePoints, 0, outOffset);
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
/*      */   public static String center(String str, int size) {
/*  589 */     return center(str, size, ' ');
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
/*      */   public static String center(String str, int size, char padChar) {
/*  617 */     if (str == null || size <= 0) {
/*  618 */       return str;
/*      */     }
/*  620 */     int strLen = str.length();
/*  621 */     int pads = size - strLen;
/*  622 */     if (pads <= 0) {
/*  623 */       return str;
/*      */     }
/*  625 */     str = leftPad(str, strLen + pads / 2, padChar);
/*  626 */     str = rightPad(str, size, padChar);
/*  627 */     return str;
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
/*      */   public static String center(String str, int size, String padStr) {
/*  657 */     if (str == null || size <= 0) {
/*  658 */       return str;
/*      */     }
/*  660 */     if (isEmpty(padStr)) {
/*  661 */       padStr = " ";
/*      */     }
/*  663 */     int strLen = str.length();
/*  664 */     int pads = size - strLen;
/*  665 */     if (pads <= 0) {
/*  666 */       return str;
/*      */     }
/*  668 */     str = leftPad(str, strLen + pads / 2, padStr);
/*  669 */     str = rightPad(str, size, padStr);
/*  670 */     return str;
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
/*      */   public static String chomp(String str) {
/*  699 */     if (isEmpty(str)) {
/*  700 */       return str;
/*      */     }
/*      */     
/*  703 */     if (str.length() == 1) {
/*  704 */       char ch = str.charAt(0);
/*  705 */       if (ch == '\r' || ch == '\n') {
/*  706 */         return "";
/*      */       }
/*  708 */       return str;
/*      */     } 
/*      */     
/*  711 */     int lastIdx = str.length() - 1;
/*  712 */     char last = str.charAt(lastIdx);
/*      */     
/*  714 */     if (last == '\n') {
/*  715 */       if (str.charAt(lastIdx - 1) == '\r') {
/*  716 */         lastIdx--;
/*      */       }
/*  718 */     } else if (last != '\r') {
/*  719 */       lastIdx++;
/*      */     } 
/*  721 */     return str.substring(0, lastIdx);
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
/*      */   @Deprecated
/*      */   public static String chomp(String str, String separator) {
/*  753 */     return removeEnd(str, separator);
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
/*      */   public static String chop(String str) {
/*  780 */     if (str == null) {
/*  781 */       return null;
/*      */     }
/*  783 */     int strLen = str.length();
/*  784 */     if (strLen < 2) {
/*  785 */       return "";
/*      */     }
/*  787 */     int lastIdx = strLen - 1;
/*  788 */     String ret = str.substring(0, lastIdx);
/*  789 */     char last = str.charAt(lastIdx);
/*  790 */     if (last == '\n' && ret.charAt(lastIdx - 1) == '\r') {
/*  791 */       return ret.substring(0, lastIdx - 1);
/*      */     }
/*  793 */     return ret;
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
/*      */   public static int compare(String str1, String str2) {
/*  829 */     return compare(str1, str2, true);
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
/*      */   public static int compare(String str1, String str2, boolean nullIsLess) {
/*  867 */     if (str1 == str2) {
/*  868 */       return 0;
/*      */     }
/*  870 */     if (str1 == null) {
/*  871 */       return nullIsLess ? -1 : 1;
/*      */     }
/*  873 */     if (str2 == null) {
/*  874 */       return nullIsLess ? 1 : -1;
/*      */     }
/*  876 */     return str1.compareTo(str2);
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
/*      */   
/*      */   public static int compareIgnoreCase(String str1, String str2) {
/*  917 */     return compareIgnoreCase(str1, str2, true);
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
/*      */ 
/*      */ 
/*      */   
/*      */   public static int compareIgnoreCase(String str1, String str2, boolean nullIsLess) {
/*  960 */     if (str1 == str2) {
/*  961 */       return 0;
/*      */     }
/*  963 */     if (str1 == null) {
/*  964 */       return nullIsLess ? -1 : 1;
/*      */     }
/*  966 */     if (str2 == null) {
/*  967 */       return nullIsLess ? 1 : -1;
/*      */     }
/*  969 */     return str1.compareToIgnoreCase(str2);
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
/*      */   public static boolean contains(CharSequence seq, CharSequence searchSeq) {
/*  995 */     if (seq == null || searchSeq == null) {
/*  996 */       return false;
/*      */     }
/*  998 */     return (CharSequenceUtils.indexOf(seq, searchSeq, 0) >= 0);
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
/*      */   public static boolean contains(CharSequence seq, int searchChar) {
/* 1022 */     if (isEmpty(seq)) {
/* 1023 */       return false;
/*      */     }
/* 1025 */     return (CharSequenceUtils.indexOf(seq, searchChar, 0) >= 0);
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
/*      */   public static boolean containsAny(CharSequence cs, char... searchChars) {
/* 1054 */     if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
/* 1055 */       return false;
/*      */     }
/* 1057 */     int csLength = cs.length();
/* 1058 */     int searchLength = searchChars.length;
/* 1059 */     int csLast = csLength - 1;
/* 1060 */     int searchLast = searchLength - 1;
/* 1061 */     for (int i = 0; i < csLength; i++) {
/* 1062 */       char ch = cs.charAt(i);
/* 1063 */       for (int j = 0; j < searchLength; j++) {
/* 1064 */         if (searchChars[j] == ch) {
/* 1065 */           if (!Character.isHighSurrogate(ch))
/*      */           {
/* 1067 */             return true;
/*      */           }
/* 1069 */           if (j == searchLast)
/*      */           {
/* 1071 */             return true;
/*      */           }
/* 1073 */           if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
/* 1074 */             return true;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 1079 */     return false;
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
/*      */   public static boolean containsAny(CharSequence cs, CharSequence searchChars) {
/* 1112 */     if (searchChars == null) {
/* 1113 */       return false;
/*      */     }
/* 1115 */     return containsAny(cs, CharSequenceUtils.toCharArray(searchChars));
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
/*      */   public static boolean containsAny(CharSequence cs, CharSequence... searchCharSequences) {
/* 1143 */     return containsAny(StringUtils::contains, cs, searchCharSequences);
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
/*      */   private static boolean containsAny(ToBooleanBiFunction<CharSequence, CharSequence> test, CharSequence cs, CharSequence... searchCharSequences) {
/* 1162 */     if (isEmpty(cs) || ArrayUtils.isEmpty((Object[])searchCharSequences)) {
/* 1163 */       return false;
/*      */     }
/* 1165 */     for (CharSequence searchCharSequence : searchCharSequences) {
/* 1166 */       if (test.applyAsBoolean(cs, searchCharSequence)) {
/* 1167 */         return true;
/*      */       }
/*      */     } 
/* 1170 */     return false;
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
/*      */   public static boolean containsAnyIgnoreCase(CharSequence cs, CharSequence... searchCharSequences) {
/* 1200 */     return containsAny(StringUtils::containsIgnoreCase, cs, searchCharSequences);
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
/*      */   public static boolean containsIgnoreCase(CharSequence str, CharSequence searchStr) {
/* 1228 */     if (str == null || searchStr == null) {
/* 1229 */       return false;
/*      */     }
/* 1231 */     int len = searchStr.length();
/* 1232 */     int max = str.length() - len;
/* 1233 */     for (int i = 0; i <= max; i++) {
/* 1234 */       if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, len)) {
/* 1235 */         return true;
/*      */       }
/*      */     } 
/* 1238 */     return false;
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
/*      */   public static boolean containsNone(CharSequence cs, char... searchChars) {
/* 1265 */     if (cs == null || searchChars == null) {
/* 1266 */       return true;
/*      */     }
/* 1268 */     int csLen = cs.length();
/* 1269 */     int csLast = csLen - 1;
/* 1270 */     int searchLen = searchChars.length;
/* 1271 */     int searchLast = searchLen - 1;
/* 1272 */     for (int i = 0; i < csLen; i++) {
/* 1273 */       char ch = cs.charAt(i);
/* 1274 */       for (int j = 0; j < searchLen; j++) {
/* 1275 */         if (searchChars[j] == ch) {
/* 1276 */           if (!Character.isHighSurrogate(ch))
/*      */           {
/* 1278 */             return false;
/*      */           }
/* 1280 */           if (j == searchLast)
/*      */           {
/* 1282 */             return false;
/*      */           }
/* 1284 */           if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
/* 1285 */             return false;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 1290 */     return true;
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
/*      */   public static boolean containsNone(CharSequence cs, String invalidChars) {
/* 1317 */     if (invalidChars == null) {
/* 1318 */       return true;
/*      */     }
/* 1320 */     return containsNone(cs, invalidChars.toCharArray());
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
/*      */   public static boolean containsOnly(CharSequence cs, char... valid) {
/* 1347 */     if (valid == null || cs == null) {
/* 1348 */       return false;
/*      */     }
/* 1350 */     if (cs.length() == 0) {
/* 1351 */       return true;
/*      */     }
/* 1353 */     if (valid.length == 0) {
/* 1354 */       return false;
/*      */     }
/* 1356 */     return (indexOfAnyBut(cs, valid) == -1);
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
/*      */   public static boolean containsOnly(CharSequence cs, String validChars) {
/* 1383 */     if (cs == null || validChars == null) {
/* 1384 */       return false;
/*      */     }
/* 1386 */     return containsOnly(cs, validChars.toCharArray());
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
/*      */   public static boolean containsWhitespace(CharSequence seq) {
/* 1401 */     if (isEmpty(seq)) {
/* 1402 */       return false;
/*      */     }
/* 1404 */     int strLen = seq.length();
/* 1405 */     for (int i = 0; i < strLen; i++) {
/* 1406 */       if (Character.isWhitespace(seq.charAt(i))) {
/* 1407 */         return true;
/*      */       }
/*      */     } 
/* 1410 */     return false;
/*      */   }
/*      */   
/*      */   private static void convertRemainingAccentCharacters(StringBuilder decomposed) {
/* 1414 */     for (int i = 0; i < decomposed.length(); i++) {
/* 1415 */       if (decomposed.charAt(i) == 'Ł') {
/* 1416 */         decomposed.setCharAt(i, 'L');
/* 1417 */       } else if (decomposed.charAt(i) == 'ł') {
/* 1418 */         decomposed.setCharAt(i, 'l');
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int countMatches(CharSequence str, char ch) {
/* 1443 */     if (isEmpty(str)) {
/* 1444 */       return 0;
/*      */     }
/* 1446 */     int count = 0;
/*      */     
/* 1448 */     for (int i = 0; i < str.length(); i++) {
/* 1449 */       if (ch == str.charAt(i)) {
/* 1450 */         count++;
/*      */       }
/*      */     } 
/* 1453 */     return count;
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
/*      */   public static int countMatches(CharSequence str, CharSequence sub) {
/* 1479 */     if (isEmpty(str) || isEmpty(sub)) {
/* 1480 */       return 0;
/*      */     }
/* 1482 */     int count = 0;
/* 1483 */     int idx = 0;
/* 1484 */     while ((idx = CharSequenceUtils.indexOf(str, sub, idx)) != -1) {
/* 1485 */       count++;
/* 1486 */       idx += sub.length();
/*      */     } 
/* 1488 */     return count;
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
/*      */   public static <T extends CharSequence> T defaultIfBlank(T str, T defaultStr) {
/* 1512 */     return isBlank((CharSequence)str) ? defaultStr : str;
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
/*      */   public static <T extends CharSequence> T defaultIfEmpty(T str, T defaultStr) {
/* 1534 */     return isEmpty((CharSequence)str) ? defaultStr : str;
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
/*      */   public static String defaultString(String str) {
/* 1554 */     return Objects.toString(str, "");
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
/*      */   @Deprecated
/*      */   public static String defaultString(String str, String nullDefault) {
/* 1577 */     return Objects.toString(str, nullDefault);
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
/*      */   public static String deleteWhitespace(String str) {
/* 1595 */     if (isEmpty(str)) {
/* 1596 */       return str;
/*      */     }
/* 1598 */     int sz = str.length();
/* 1599 */     char[] chs = new char[sz];
/* 1600 */     int count = 0;
/* 1601 */     for (int i = 0; i < sz; i++) {
/* 1602 */       if (!Character.isWhitespace(str.charAt(i))) {
/* 1603 */         chs[count++] = str.charAt(i);
/*      */       }
/*      */     } 
/* 1606 */     if (count == sz) {
/* 1607 */       return str;
/*      */     }
/* 1609 */     if (count == 0) {
/* 1610 */       return "";
/*      */     }
/* 1612 */     return new String(chs, 0, count);
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
/*      */   public static String difference(String str1, String str2) {
/* 1644 */     if (str1 == null) {
/* 1645 */       return str2;
/*      */     }
/* 1647 */     if (str2 == null) {
/* 1648 */       return str1;
/*      */     }
/* 1650 */     int at = indexOfDifference(str1, str2);
/* 1651 */     if (at == -1) {
/* 1652 */       return "";
/*      */     }
/* 1654 */     return str2.substring(at);
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
/*      */   public static boolean endsWith(CharSequence str, CharSequence suffix) {
/* 1682 */     return endsWith(str, suffix, false);
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
/*      */   private static boolean endsWith(CharSequence str, CharSequence suffix, boolean ignoreCase) {
/* 1697 */     if (str == null || suffix == null) {
/* 1698 */       return (str == suffix);
/*      */     }
/* 1700 */     if (suffix.length() > str.length()) {
/* 1701 */       return false;
/*      */     }
/* 1703 */     int strOffset = str.length() - suffix.length();
/* 1704 */     return CharSequenceUtils.regionMatches(str, ignoreCase, strOffset, suffix, 0, suffix.length());
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
/*      */   public static boolean endsWithAny(CharSequence sequence, CharSequence... searchStrings) {
/* 1729 */     if (isEmpty(sequence) || ArrayUtils.isEmpty((Object[])searchStrings)) {
/* 1730 */       return false;
/*      */     }
/* 1732 */     for (CharSequence searchString : searchStrings) {
/* 1733 */       if (endsWith(sequence, searchString)) {
/* 1734 */         return true;
/*      */       }
/*      */     } 
/* 1737 */     return false;
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
/*      */   public static boolean endsWithIgnoreCase(CharSequence str, CharSequence suffix) {
/* 1764 */     return endsWith(str, suffix, true);
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
/*      */   public static boolean equals(CharSequence cs1, CharSequence cs2) {
/* 1790 */     if (cs1 == cs2) {
/* 1791 */       return true;
/*      */     }
/* 1793 */     if (cs1 == null || cs2 == null) {
/* 1794 */       return false;
/*      */     }
/* 1796 */     if (cs1.length() != cs2.length()) {
/* 1797 */       return false;
/*      */     }
/* 1799 */     if (cs1 instanceof String && cs2 instanceof String) {
/* 1800 */       return cs1.equals(cs2);
/*      */     }
/*      */     
/* 1803 */     int length = cs1.length();
/* 1804 */     for (int i = 0; i < length; i++) {
/* 1805 */       if (cs1.charAt(i) != cs2.charAt(i)) {
/* 1806 */         return false;
/*      */       }
/*      */     } 
/* 1809 */     return true;
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
/*      */   public static boolean equalsAny(CharSequence string, CharSequence... searchStrings) {
/* 1832 */     if (ArrayUtils.isNotEmpty(searchStrings)) {
/* 1833 */       for (CharSequence next : searchStrings) {
/* 1834 */         if (equals(string, next)) {
/* 1835 */           return true;
/*      */         }
/*      */       } 
/*      */     }
/* 1839 */     return false;
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
/*      */   public static boolean equalsAnyIgnoreCase(CharSequence string, CharSequence... searchStrings) {
/* 1862 */     if (ArrayUtils.isNotEmpty(searchStrings)) {
/* 1863 */       for (CharSequence next : searchStrings) {
/* 1864 */         if (equalsIgnoreCase(string, next)) {
/* 1865 */           return true;
/*      */         }
/*      */       } 
/*      */     }
/* 1869 */     return false;
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
/*      */   public static boolean equalsIgnoreCase(CharSequence cs1, CharSequence cs2) {
/* 1894 */     if (cs1 == cs2) {
/* 1895 */       return true;
/*      */     }
/* 1897 */     if (cs1 == null || cs2 == null) {
/* 1898 */       return false;
/*      */     }
/* 1900 */     if (cs1.length() != cs2.length()) {
/* 1901 */       return false;
/*      */     }
/* 1903 */     return CharSequenceUtils.regionMatches(cs1, true, 0, cs2, 0, cs1.length());
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
/*      */   @SafeVarargs
/*      */   public static <T extends CharSequence> T firstNonBlank(T... values) {
/* 1933 */     if (values != null) {
/* 1934 */       for (T val : values) {
/* 1935 */         if (isNotBlank((CharSequence)val)) {
/* 1936 */           return val;
/*      */         }
/*      */       } 
/*      */     }
/* 1940 */     return null;
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
/*      */   @SafeVarargs
/*      */   public static <T extends CharSequence> T firstNonEmpty(T... values) {
/* 1968 */     if (values != null) {
/* 1969 */       for (T val : values) {
/* 1970 */         if (isNotEmpty((CharSequence)val)) {
/* 1971 */           return val;
/*      */         }
/*      */       } 
/*      */     }
/* 1975 */     return null;
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
/*      */   public static byte[] getBytes(String string, Charset charset) {
/* 1988 */     return (string == null) ? ArrayUtils.EMPTY_BYTE_ARRAY : string.getBytes(Charsets.toCharset(charset));
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
/*      */   public static byte[] getBytes(String string, String charset) throws UnsupportedEncodingException {
/* 2002 */     return (string == null) ? ArrayUtils.EMPTY_BYTE_ARRAY : string.getBytes(Charsets.toCharsetName(charset));
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
/*      */   public static String getCommonPrefix(String... strs) {
/* 2039 */     if (ArrayUtils.isEmpty((Object[])strs)) {
/* 2040 */       return "";
/*      */     }
/* 2042 */     int smallestIndexOfDiff = indexOfDifference((CharSequence[])strs);
/* 2043 */     if (smallestIndexOfDiff == -1) {
/*      */       
/* 2045 */       if (strs[0] == null) {
/* 2046 */         return "";
/*      */       }
/* 2048 */       return strs[0];
/*      */     } 
/* 2050 */     if (smallestIndexOfDiff == 0)
/*      */     {
/* 2052 */       return "";
/*      */     }
/*      */     
/* 2055 */     return strs[0].substring(0, smallestIndexOfDiff);
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
/*      */   public static String getDigits(String str) {
/* 2081 */     if (isEmpty(str)) {
/* 2082 */       return str;
/*      */     }
/* 2084 */     int sz = str.length();
/* 2085 */     StringBuilder strDigits = new StringBuilder(sz);
/* 2086 */     for (int i = 0; i < sz; i++) {
/* 2087 */       char tempChar = str.charAt(i);
/* 2088 */       if (Character.isDigit(tempChar)) {
/* 2089 */         strDigits.append(tempChar);
/*      */       }
/*      */     } 
/* 2092 */     return strDigits.toString();
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
/*      */   @Deprecated
/*      */   public static int getFuzzyDistance(CharSequence term, CharSequence query, Locale locale) {
/* 2126 */     if (term == null || query == null) {
/* 2127 */       throw new IllegalArgumentException("Strings must not be null");
/*      */     }
/* 2129 */     if (locale == null) {
/* 2130 */       throw new IllegalArgumentException("Locale must not be null");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2137 */     String termLowerCase = term.toString().toLowerCase(locale);
/* 2138 */     String queryLowerCase = query.toString().toLowerCase(locale);
/*      */ 
/*      */     
/* 2141 */     int score = 0;
/*      */ 
/*      */ 
/*      */     
/* 2145 */     int termIndex = 0;
/*      */ 
/*      */     
/* 2148 */     int previousMatchingCharacterIndex = Integer.MIN_VALUE;
/*      */     
/* 2150 */     for (int queryIndex = 0; queryIndex < queryLowerCase.length(); queryIndex++) {
/* 2151 */       char queryChar = queryLowerCase.charAt(queryIndex);
/*      */       
/* 2153 */       boolean termCharacterMatchFound = false;
/* 2154 */       for (; termIndex < termLowerCase.length() && !termCharacterMatchFound; termIndex++) {
/* 2155 */         char termChar = termLowerCase.charAt(termIndex);
/*      */         
/* 2157 */         if (queryChar == termChar) {
/*      */           
/* 2159 */           score++;
/*      */ 
/*      */ 
/*      */           
/* 2163 */           if (previousMatchingCharacterIndex + 1 == termIndex) {
/* 2164 */             score += 2;
/*      */           }
/*      */           
/* 2167 */           previousMatchingCharacterIndex = termIndex;
/*      */ 
/*      */ 
/*      */           
/* 2171 */           termCharacterMatchFound = true;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 2176 */     return score;
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
/*      */   public static <T extends CharSequence> T getIfBlank(T str, Supplier<T> defaultSupplier) {
/* 2205 */     return isBlank((CharSequence)str) ? (T)Suppliers.get(defaultSupplier) : str;
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
/*      */   public static <T extends CharSequence> T getIfEmpty(T str, Supplier<T> defaultSupplier) {
/* 2233 */     return isEmpty((CharSequence)str) ? (T)Suppliers.get(defaultSupplier) : str;
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
/*      */   @Deprecated
/*      */   public static double getJaroWinklerDistance(CharSequence first, CharSequence second) {
/* 2273 */     double DEFAULT_SCALING_FACTOR = 0.1D;
/*      */     
/* 2275 */     if (first == null || second == null) {
/* 2276 */       throw new IllegalArgumentException("Strings must not be null");
/*      */     }
/*      */     
/* 2279 */     int[] mtp = matches(first, second);
/* 2280 */     double m = mtp[0];
/* 2281 */     if (m == 0.0D) {
/* 2282 */       return 0.0D;
/*      */     }
/* 2284 */     double j = (m / first.length() + m / second.length() + (m - mtp[1]) / m) / 3.0D;
/* 2285 */     double jw = (j < 0.7D) ? j : (j + Math.min(0.1D, 1.0D / mtp[3]) * mtp[2] * (1.0D - j));
/* 2286 */     return Math.round(jw * 100.0D) / 100.0D;
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
/*      */   @Deprecated
/*      */   public static int getLevenshteinDistance(CharSequence s, CharSequence t) {
/* 2326 */     if (s == null || t == null) {
/* 2327 */       throw new IllegalArgumentException("Strings must not be null");
/*      */     }
/*      */     
/* 2330 */     int n = s.length();
/* 2331 */     int m = t.length();
/*      */     
/* 2333 */     if (n == 0) {
/* 2334 */       return m;
/*      */     }
/* 2336 */     if (m == 0) {
/* 2337 */       return n;
/*      */     }
/*      */     
/* 2340 */     if (n > m) {
/*      */       
/* 2342 */       CharSequence tmp = s;
/* 2343 */       s = t;
/* 2344 */       t = tmp;
/* 2345 */       n = m;
/* 2346 */       m = t.length();
/*      */     } 
/*      */     
/* 2349 */     int[] p = new int[n + 1];
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int i;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2359 */     for (i = 0; i <= n; i++) {
/* 2360 */       p[i] = i;
/*      */     }
/*      */     
/* 2363 */     for (int j = 1; j <= m; j++) {
/* 2364 */       int upperleft = p[0];
/* 2365 */       char jOfT = t.charAt(j - 1);
/* 2366 */       p[0] = j;
/*      */       
/* 2368 */       for (i = 1; i <= n; i++) {
/* 2369 */         int upper = p[i];
/* 2370 */         int cost = (s.charAt(i - 1) == jOfT) ? 0 : 1;
/*      */         
/* 2372 */         p[i] = Math.min(Math.min(p[i - 1] + 1, p[i] + 1), upperleft + cost);
/* 2373 */         upperleft = upper;
/*      */       } 
/*      */     } 
/*      */     
/* 2377 */     return p[n];
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
/*      */   @Deprecated
/*      */   public static int getLevenshteinDistance(CharSequence s, CharSequence t, int threshold) {
/* 2417 */     if (s == null || t == null) {
/* 2418 */       throw new IllegalArgumentException("Strings must not be null");
/*      */     }
/* 2420 */     if (threshold < 0) {
/* 2421 */       throw new IllegalArgumentException("Threshold must not be negative");
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2468 */     int n = s.length();
/* 2469 */     int m = t.length();
/*      */ 
/*      */     
/* 2472 */     if (n == 0) {
/* 2473 */       return (m <= threshold) ? m : -1;
/*      */     }
/* 2475 */     if (m == 0) {
/* 2476 */       return (n <= threshold) ? n : -1;
/*      */     }
/* 2478 */     if (Math.abs(n - m) > threshold)
/*      */     {
/* 2480 */       return -1;
/*      */     }
/*      */     
/* 2483 */     if (n > m) {
/*      */       
/* 2485 */       CharSequence tmp = s;
/* 2486 */       s = t;
/* 2487 */       t = tmp;
/* 2488 */       n = m;
/* 2489 */       m = t.length();
/*      */     } 
/*      */     
/* 2492 */     int[] p = new int[n + 1];
/* 2493 */     int[] d = new int[n + 1];
/*      */ 
/*      */ 
/*      */     
/* 2497 */     int boundary = Math.min(n, threshold) + 1;
/* 2498 */     for (int i = 0; i < boundary; i++) {
/* 2499 */       p[i] = i;
/*      */     }
/*      */ 
/*      */     
/* 2503 */     Arrays.fill(p, boundary, p.length, 2147483647);
/* 2504 */     Arrays.fill(d, 2147483647);
/*      */ 
/*      */     
/* 2507 */     for (int j = 1; j <= m; j++) {
/* 2508 */       char jOfT = t.charAt(j - 1);
/* 2509 */       d[0] = j;
/*      */ 
/*      */       
/* 2512 */       int min = Math.max(1, j - threshold);
/* 2513 */       int max = (j > Integer.MAX_VALUE - threshold) ? n : Math.min(n, j + threshold);
/*      */ 
/*      */       
/* 2516 */       if (min > max) {
/* 2517 */         return -1;
/*      */       }
/*      */ 
/*      */       
/* 2521 */       if (min > 1) {
/* 2522 */         d[min - 1] = Integer.MAX_VALUE;
/*      */       }
/*      */ 
/*      */       
/* 2526 */       for (int k = min; k <= max; k++) {
/* 2527 */         if (s.charAt(k - 1) == jOfT) {
/*      */           
/* 2529 */           d[k] = p[k - 1];
/*      */         } else {
/*      */           
/* 2532 */           d[k] = 1 + Math.min(Math.min(d[k - 1], p[k]), p[k - 1]);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 2537 */       int[] tmp = p;
/* 2538 */       p = d;
/* 2539 */       d = tmp;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2544 */     if (p[n] <= threshold) {
/* 2545 */       return p[n];
/*      */     }
/* 2547 */     return -1;
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
/*      */   public static int indexOf(CharSequence seq, CharSequence searchSeq) {
/* 2575 */     if (seq == null || searchSeq == null) {
/* 2576 */       return -1;
/*      */     }
/* 2578 */     return CharSequenceUtils.indexOf(seq, searchSeq, 0);
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
/*      */   public static int indexOf(CharSequence seq, CharSequence searchSeq, int startPos) {
/* 2615 */     if (seq == null || searchSeq == null) {
/* 2616 */       return -1;
/*      */     }
/* 2618 */     return CharSequenceUtils.indexOf(seq, searchSeq, startPos);
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
/*      */   
/*      */   public static int indexOf(CharSequence seq, int searchChar) {
/* 2659 */     if (isEmpty(seq)) {
/* 2660 */       return -1;
/*      */     }
/* 2662 */     return CharSequenceUtils.indexOf(seq, searchChar, 0);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOf(CharSequence seq, int searchChar, int startPos) {
/* 2718 */     if (isEmpty(seq)) {
/* 2719 */       return -1;
/*      */     }
/* 2721 */     return CharSequenceUtils.indexOf(seq, searchChar, startPos);
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
/*      */   public static int indexOfAny(CharSequence cs, char... searchChars) {
/* 2748 */     if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
/* 2749 */       return -1;
/*      */     }
/* 2751 */     int csLen = cs.length();
/* 2752 */     int csLast = csLen - 1;
/* 2753 */     int searchLen = searchChars.length;
/* 2754 */     int searchLast = searchLen - 1;
/* 2755 */     for (int i = 0; i < csLen; i++) {
/* 2756 */       char ch = cs.charAt(i);
/* 2757 */       for (int j = 0; j < searchLen; j++) {
/* 2758 */         if (searchChars[j] == ch) {
/* 2759 */           if (i >= csLast || j >= searchLast || !Character.isHighSurrogate(ch)) {
/* 2760 */             return i;
/*      */           }
/*      */           
/* 2763 */           if (searchChars[j + 1] == cs.charAt(i + 1)) {
/* 2764 */             return i;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 2769 */     return -1;
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
/*      */   public static int indexOfAny(CharSequence str, CharSequence... searchStrs) {
/* 2800 */     if (str == null || searchStrs == null) {
/* 2801 */       return -1;
/*      */     }
/*      */ 
/*      */     
/* 2805 */     int ret = Integer.MAX_VALUE;
/*      */ 
/*      */     
/* 2808 */     for (CharSequence search : searchStrs) {
/* 2809 */       if (search != null) {
/*      */ 
/*      */         
/* 2812 */         int tmp = CharSequenceUtils.indexOf(str, search, 0);
/* 2813 */         if (tmp != -1)
/*      */         {
/*      */ 
/*      */           
/* 2817 */           if (tmp < ret)
/* 2818 */             ret = tmp; 
/*      */         }
/*      */       } 
/*      */     } 
/* 2822 */     return (ret == Integer.MAX_VALUE) ? -1 : ret;
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
/*      */   public static int indexOfAny(CharSequence cs, String searchChars) {
/* 2849 */     if (isEmpty(cs) || isEmpty(searchChars)) {
/* 2850 */       return -1;
/*      */     }
/* 2852 */     return indexOfAny(cs, searchChars.toCharArray());
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
/*      */   public static int indexOfAnyBut(CharSequence cs, char... searchChars) {
/* 2880 */     if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
/* 2881 */       return -1;
/*      */     }
/* 2883 */     int csLen = cs.length();
/* 2884 */     int csLast = csLen - 1;
/* 2885 */     int searchLen = searchChars.length;
/* 2886 */     int searchLast = searchLen - 1;
/*      */     
/* 2888 */     for (int i = 0; i < csLen; i++) {
/* 2889 */       char ch = cs.charAt(i);
/* 2890 */       int j = 0; while (true) { if (j < searchLen) {
/* 2891 */           if (searchChars[j] == ch) {
/* 2892 */             if (i >= csLast || j >= searchLast || !Character.isHighSurrogate(ch)) {
/*      */               break;
/*      */             }
/* 2895 */             if (searchChars[j + 1] == cs.charAt(i + 1))
/*      */               break; 
/*      */           }  j++;
/*      */           continue;
/*      */         } 
/* 2900 */         return i; }
/*      */     
/* 2902 */     }  return -1;
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
/*      */   public static int indexOfAnyBut(CharSequence seq, CharSequence searchChars) {
/* 2929 */     if (isEmpty(seq) || isEmpty(searchChars)) {
/* 2930 */       return -1;
/*      */     }
/* 2932 */     int strLen = seq.length();
/* 2933 */     for (int i = 0; i < strLen; i++) {
/* 2934 */       char ch = seq.charAt(i);
/* 2935 */       boolean chFound = (CharSequenceUtils.indexOf(searchChars, ch, 0) >= 0);
/* 2936 */       if (i + 1 < strLen && Character.isHighSurrogate(ch)) {
/* 2937 */         char ch2 = seq.charAt(i + 1);
/* 2938 */         if (chFound && CharSequenceUtils.indexOf(searchChars, ch2, 0) < 0) {
/* 2939 */           return i;
/*      */         }
/* 2941 */       } else if (!chFound) {
/* 2942 */         return i;
/*      */       } 
/*      */     } 
/* 2945 */     return -1;
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
/*      */   public static int indexOfDifference(CharSequence... css) {
/* 2981 */     if (ArrayUtils.getLength(css) <= 1) {
/* 2982 */       return -1;
/*      */     }
/* 2984 */     boolean anyStringNull = false;
/* 2985 */     boolean allStringsNull = true;
/* 2986 */     int arrayLen = css.length;
/* 2987 */     int shortestStrLen = Integer.MAX_VALUE;
/* 2988 */     int longestStrLen = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2993 */     for (CharSequence cs : css) {
/* 2994 */       if (cs == null) {
/* 2995 */         anyStringNull = true;
/* 2996 */         shortestStrLen = 0;
/*      */       } else {
/* 2998 */         allStringsNull = false;
/* 2999 */         shortestStrLen = Math.min(cs.length(), shortestStrLen);
/* 3000 */         longestStrLen = Math.max(cs.length(), longestStrLen);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 3005 */     if (allStringsNull || (longestStrLen == 0 && !anyStringNull)) {
/* 3006 */       return -1;
/*      */     }
/*      */ 
/*      */     
/* 3010 */     if (shortestStrLen == 0) {
/* 3011 */       return 0;
/*      */     }
/*      */ 
/*      */     
/* 3015 */     int firstDiff = -1;
/* 3016 */     for (int stringPos = 0; stringPos < shortestStrLen; stringPos++) {
/* 3017 */       char comparisonChar = css[0].charAt(stringPos);
/* 3018 */       for (int arrayPos = 1; arrayPos < arrayLen; arrayPos++) {
/* 3019 */         if (css[arrayPos].charAt(stringPos) != comparisonChar) {
/* 3020 */           firstDiff = stringPos;
/*      */           break;
/*      */         } 
/*      */       } 
/* 3024 */       if (firstDiff != -1) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */     
/* 3029 */     if (firstDiff == -1 && shortestStrLen != longestStrLen)
/*      */     {
/*      */ 
/*      */       
/* 3033 */       return shortestStrLen;
/*      */     }
/* 3035 */     return firstDiff;
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
/*      */   public static int indexOfDifference(CharSequence cs1, CharSequence cs2) {
/* 3064 */     if (cs1 == cs2) {
/* 3065 */       return -1;
/*      */     }
/* 3067 */     if (cs1 == null || cs2 == null) {
/* 3068 */       return 0;
/*      */     }
/*      */     int i;
/* 3071 */     for (i = 0; i < cs1.length() && i < cs2.length() && 
/* 3072 */       cs1.charAt(i) == cs2.charAt(i); i++);
/*      */ 
/*      */ 
/*      */     
/* 3076 */     if (i < cs2.length() || i < cs1.length()) {
/* 3077 */       return i;
/*      */     }
/* 3079 */     return -1;
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
/*      */   public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
/* 3109 */     return indexOfIgnoreCase(str, searchStr, 0);
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
/*      */   public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
/* 3145 */     if (str == null || searchStr == null) {
/* 3146 */       return -1;
/*      */     }
/* 3148 */     if (startPos < 0) {
/* 3149 */       startPos = 0;
/*      */     }
/* 3151 */     int endLimit = str.length() - searchStr.length() + 1;
/* 3152 */     if (startPos > endLimit) {
/* 3153 */       return -1;
/*      */     }
/* 3155 */     if (searchStr.length() == 0) {
/* 3156 */       return startPos;
/*      */     }
/* 3158 */     for (int i = startPos; i < endLimit; i++) {
/* 3159 */       if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
/* 3160 */         return i;
/*      */       }
/*      */     } 
/* 3163 */     return -1;
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
/*      */   public static boolean isAllBlank(CharSequence... css) {
/* 3188 */     if (ArrayUtils.isEmpty((Object[])css)) {
/* 3189 */       return true;
/*      */     }
/* 3191 */     for (CharSequence cs : css) {
/* 3192 */       if (isNotBlank(cs)) {
/* 3193 */         return false;
/*      */       }
/*      */     } 
/* 3196 */     return true;
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
/*      */   public static boolean isAllEmpty(CharSequence... css) {
/* 3219 */     if (ArrayUtils.isEmpty((Object[])css)) {
/* 3220 */       return true;
/*      */     }
/* 3222 */     for (CharSequence cs : css) {
/* 3223 */       if (isNotEmpty(cs)) {
/* 3224 */         return false;
/*      */       }
/*      */     } 
/* 3227 */     return true;
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
/*      */   public static boolean isAllLowerCase(CharSequence cs) {
/* 3253 */     if (isEmpty(cs)) {
/* 3254 */       return false;
/*      */     }
/* 3256 */     int sz = cs.length();
/* 3257 */     for (int i = 0; i < sz; i++) {
/* 3258 */       if (!Character.isLowerCase(cs.charAt(i))) {
/* 3259 */         return false;
/*      */       }
/*      */     } 
/* 3262 */     return true;
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
/*      */   public static boolean isAllUpperCase(CharSequence cs) {
/* 3288 */     if (isEmpty(cs)) {
/* 3289 */       return false;
/*      */     }
/* 3291 */     int sz = cs.length();
/* 3292 */     for (int i = 0; i < sz; i++) {
/* 3293 */       if (!Character.isUpperCase(cs.charAt(i))) {
/* 3294 */         return false;
/*      */       }
/*      */     } 
/* 3297 */     return true;
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
/*      */   public static boolean isAlpha(CharSequence cs) {
/* 3321 */     if (isEmpty(cs)) {
/* 3322 */       return false;
/*      */     }
/* 3324 */     int sz = cs.length();
/* 3325 */     for (int i = 0; i < sz; i++) {
/* 3326 */       if (!Character.isLetter(cs.charAt(i))) {
/* 3327 */         return false;
/*      */       }
/*      */     } 
/* 3330 */     return true;
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
/*      */   public static boolean isAlphanumeric(CharSequence cs) {
/* 3356 */     if (isEmpty(cs)) {
/* 3357 */       return false;
/*      */     }
/* 3359 */     int sz = cs.length();
/* 3360 */     for (int i = 0; i < sz; i++) {
/* 3361 */       if (!Character.isLetterOrDigit(cs.charAt(i))) {
/* 3362 */         return false;
/*      */       }
/*      */     } 
/* 3365 */     return true;
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
/*      */   public static boolean isAlphanumericSpace(CharSequence cs) {
/* 3391 */     if (cs == null) {
/* 3392 */       return false;
/*      */     }
/* 3394 */     int sz = cs.length();
/* 3395 */     for (int i = 0; i < sz; i++) {
/* 3396 */       char nowChar = cs.charAt(i);
/* 3397 */       if (nowChar != ' ' && !Character.isLetterOrDigit(nowChar)) {
/* 3398 */         return false;
/*      */       }
/*      */     } 
/* 3401 */     return true;
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
/*      */   public static boolean isAlphaSpace(CharSequence cs) {
/* 3427 */     if (cs == null) {
/* 3428 */       return false;
/*      */     }
/* 3430 */     int sz = cs.length();
/* 3431 */     for (int i = 0; i < sz; i++) {
/* 3432 */       char nowChar = cs.charAt(i);
/* 3433 */       if (nowChar != ' ' && !Character.isLetter(nowChar)) {
/* 3434 */         return false;
/*      */       }
/*      */     } 
/* 3437 */     return true;
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
/*      */   public static boolean isAnyBlank(CharSequence... css) {
/* 3464 */     if (ArrayUtils.isEmpty((Object[])css)) {
/* 3465 */       return false;
/*      */     }
/* 3467 */     for (CharSequence cs : css) {
/* 3468 */       if (isBlank(cs)) {
/* 3469 */         return true;
/*      */       }
/*      */     } 
/* 3472 */     return false;
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
/*      */   public static boolean isAnyEmpty(CharSequence... css) {
/* 3496 */     if (ArrayUtils.isEmpty((Object[])css)) {
/* 3497 */       return false;
/*      */     }
/* 3499 */     for (CharSequence cs : css) {
/* 3500 */       if (isEmpty(cs)) {
/* 3501 */         return true;
/*      */       }
/*      */     } 
/* 3504 */     return false;
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
/*      */   public static boolean isAsciiPrintable(CharSequence cs) {
/* 3534 */     if (cs == null) {
/* 3535 */       return false;
/*      */     }
/* 3537 */     int sz = cs.length();
/* 3538 */     for (int i = 0; i < sz; i++) {
/* 3539 */       if (!CharUtils.isAsciiPrintable(cs.charAt(i))) {
/* 3540 */         return false;
/*      */       }
/*      */     } 
/* 3543 */     return true;
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
/*      */   public static boolean isBlank(CharSequence cs) {
/* 3565 */     int strLen = length(cs);
/* 3566 */     if (strLen == 0) {
/* 3567 */       return true;
/*      */     }
/* 3569 */     for (int i = 0; i < strLen; i++) {
/* 3570 */       if (!Character.isWhitespace(cs.charAt(i))) {
/* 3571 */         return false;
/*      */       }
/*      */     } 
/* 3574 */     return true;
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
/*      */   public static boolean isEmpty(CharSequence cs) {
/* 3597 */     return (cs == null || cs.length() == 0);
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
/*      */   public static boolean isMixedCase(CharSequence cs) {
/* 3624 */     if (isEmpty(cs) || cs.length() == 1) {
/* 3625 */       return false;
/*      */     }
/* 3627 */     boolean containsUppercase = false;
/* 3628 */     boolean containsLowercase = false;
/* 3629 */     int sz = cs.length();
/* 3630 */     for (int i = 0; i < sz; i++) {
/* 3631 */       if (containsUppercase && containsLowercase) {
/* 3632 */         return true;
/*      */       }
/* 3634 */       if (Character.isUpperCase(cs.charAt(i))) {
/* 3635 */         containsUppercase = true;
/* 3636 */       } else if (Character.isLowerCase(cs.charAt(i))) {
/* 3637 */         containsLowercase = true;
/*      */       } 
/*      */     } 
/* 3640 */     return (containsUppercase && containsLowercase);
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
/*      */   public static boolean isNoneBlank(CharSequence... css) {
/* 3667 */     return !isAnyBlank(css);
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
/*      */   public static boolean isNoneEmpty(CharSequence... css) {
/* 3691 */     return !isAnyEmpty(css);
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
/*      */   public static boolean isNotBlank(CharSequence cs) {
/* 3714 */     return !isBlank(cs);
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
/*      */   public static boolean isNotEmpty(CharSequence cs) {
/* 3733 */     return !isEmpty(cs);
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
/*      */   public static boolean isNumeric(CharSequence cs) {
/* 3768 */     if (isEmpty(cs)) {
/* 3769 */       return false;
/*      */     }
/* 3771 */     int sz = cs.length();
/* 3772 */     for (int i = 0; i < sz; i++) {
/* 3773 */       if (!Character.isDigit(cs.charAt(i))) {
/* 3774 */         return false;
/*      */       }
/*      */     } 
/* 3777 */     return true;
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
/*      */   public static boolean isNumericSpace(CharSequence cs) {
/* 3807 */     if (cs == null) {
/* 3808 */       return false;
/*      */     }
/* 3810 */     int sz = cs.length();
/* 3811 */     for (int i = 0; i < sz; i++) {
/* 3812 */       char nowChar = cs.charAt(i);
/* 3813 */       if (nowChar != ' ' && !Character.isDigit(nowChar)) {
/* 3814 */         return false;
/*      */       }
/*      */     } 
/* 3817 */     return true;
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
/*      */   public static boolean isWhitespace(CharSequence cs) {
/* 3843 */     if (cs == null) {
/* 3844 */       return false;
/*      */     }
/* 3846 */     int sz = cs.length();
/* 3847 */     for (int i = 0; i < sz; i++) {
/* 3848 */       if (!Character.isWhitespace(cs.charAt(i))) {
/* 3849 */         return false;
/*      */       }
/*      */     } 
/* 3852 */     return true;
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
/*      */   public static String join(boolean[] array, char delimiter) {
/* 3878 */     if (array == null) {
/* 3879 */       return null;
/*      */     }
/* 3881 */     return join(array, delimiter, 0, array.length);
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
/*      */   public static String join(boolean[] array, char delimiter, int startIndex, int endIndex) {
/* 3913 */     if (array == null) {
/* 3914 */       return null;
/*      */     }
/* 3916 */     if (endIndex - startIndex <= 0) {
/* 3917 */       return "";
/*      */     }
/* 3919 */     StringBuilder stringBuilder = new StringBuilder(array.length * 5 + array.length - 1);
/* 3920 */     for (int i = startIndex; i < endIndex; i++) {
/* 3921 */       stringBuilder
/* 3922 */         .append(array[i])
/* 3923 */         .append(delimiter);
/*      */     }
/* 3925 */     return stringBuilder.substring(0, stringBuilder.length() - 1);
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
/*      */   public static String join(byte[] array, char delimiter) {
/* 3952 */     if (array == null) {
/* 3953 */       return null;
/*      */     }
/* 3955 */     return join(array, delimiter, 0, array.length);
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
/*      */   public static String join(byte[] array, char delimiter, int startIndex, int endIndex) {
/* 3988 */     if (array == null) {
/* 3989 */       return null;
/*      */     }
/* 3991 */     if (endIndex - startIndex <= 0) {
/* 3992 */       return "";
/*      */     }
/* 3994 */     StringBuilder stringBuilder = new StringBuilder();
/* 3995 */     for (int i = startIndex; i < endIndex; i++) {
/* 3996 */       stringBuilder
/* 3997 */         .append(array[i])
/* 3998 */         .append(delimiter);
/*      */     }
/* 4000 */     return stringBuilder.substring(0, stringBuilder.length() - 1);
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
/*      */   public static String join(char[] array, char delimiter) {
/* 4027 */     if (array == null) {
/* 4028 */       return null;
/*      */     }
/* 4030 */     return join(array, delimiter, 0, array.length);
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
/*      */   public static String join(char[] array, char delimiter, int startIndex, int endIndex) {
/* 4063 */     if (array == null) {
/* 4064 */       return null;
/*      */     }
/* 4066 */     if (endIndex - startIndex <= 0) {
/* 4067 */       return "";
/*      */     }
/* 4069 */     StringBuilder stringBuilder = new StringBuilder(array.length * 2 - 1);
/* 4070 */     for (int i = startIndex; i < endIndex; i++) {
/* 4071 */       stringBuilder
/* 4072 */         .append(array[i])
/* 4073 */         .append(delimiter);
/*      */     }
/* 4075 */     return stringBuilder.substring(0, stringBuilder.length() - 1);
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
/*      */   public static String join(double[] array, char delimiter) {
/* 4102 */     if (array == null) {
/* 4103 */       return null;
/*      */     }
/* 4105 */     return join(array, delimiter, 0, array.length);
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
/*      */   public static String join(double[] array, char delimiter, int startIndex, int endIndex) {
/* 4138 */     if (array == null) {
/* 4139 */       return null;
/*      */     }
/* 4141 */     if (endIndex - startIndex <= 0) {
/* 4142 */       return "";
/*      */     }
/* 4144 */     StringBuilder stringBuilder = new StringBuilder();
/* 4145 */     for (int i = startIndex; i < endIndex; i++) {
/* 4146 */       stringBuilder
/* 4147 */         .append(array[i])
/* 4148 */         .append(delimiter);
/*      */     }
/* 4150 */     return stringBuilder.substring(0, stringBuilder.length() - 1);
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
/*      */   public static String join(float[] array, char delimiter) {
/* 4177 */     if (array == null) {
/* 4178 */       return null;
/*      */     }
/* 4180 */     return join(array, delimiter, 0, array.length);
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
/*      */   public static String join(float[] array, char delimiter, int startIndex, int endIndex) {
/* 4213 */     if (array == null) {
/* 4214 */       return null;
/*      */     }
/* 4216 */     if (endIndex - startIndex <= 0) {
/* 4217 */       return "";
/*      */     }
/* 4219 */     StringBuilder stringBuilder = new StringBuilder();
/* 4220 */     for (int i = startIndex; i < endIndex; i++) {
/* 4221 */       stringBuilder
/* 4222 */         .append(array[i])
/* 4223 */         .append(delimiter);
/*      */     }
/* 4225 */     return stringBuilder.substring(0, stringBuilder.length() - 1);
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
/*      */   public static String join(int[] array, char separator) {
/* 4252 */     if (array == null) {
/* 4253 */       return null;
/*      */     }
/* 4255 */     return join(array, separator, 0, array.length);
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
/*      */   public static String join(int[] array, char delimiter, int startIndex, int endIndex) {
/* 4288 */     if (array == null) {
/* 4289 */       return null;
/*      */     }
/* 4291 */     if (endIndex - startIndex <= 0) {
/* 4292 */       return "";
/*      */     }
/* 4294 */     StringBuilder stringBuilder = new StringBuilder();
/* 4295 */     for (int i = startIndex; i < endIndex; i++) {
/* 4296 */       stringBuilder
/* 4297 */         .append(array[i])
/* 4298 */         .append(delimiter);
/*      */     }
/* 4300 */     return stringBuilder.substring(0, stringBuilder.length() - 1);
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
/*      */   public static String join(Iterable<?> iterable, char separator) {
/* 4318 */     return (iterable != null) ? join(iterable.iterator(), separator) : null;
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
/*      */   public static String join(Iterable<?> iterable, String separator) {
/* 4336 */     return (iterable != null) ? join(iterable.iterator(), separator) : null;
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
/*      */   public static String join(Iterator<?> iterator, char separator) {
/* 4355 */     if (iterator == null) {
/* 4356 */       return null;
/*      */     }
/* 4358 */     if (!iterator.hasNext()) {
/* 4359 */       return "";
/*      */     }
/* 4361 */     return (String)Streams.of(iterator).collect(LangCollectors.joining(toStringOrEmpty(String.valueOf(separator)), "", "", StringUtils::toStringOrEmpty));
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
/*      */   public static String join(Iterator<?> iterator, String separator) {
/* 4379 */     if (iterator == null) {
/* 4380 */       return null;
/*      */     }
/* 4382 */     if (!iterator.hasNext()) {
/* 4383 */       return "";
/*      */     }
/* 4385 */     return (String)Streams.of(iterator).collect(LangCollectors.joining(toStringOrEmpty(separator), "", "", StringUtils::toStringOrEmpty));
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
/*      */   public static String join(List<?> list, char separator, int startIndex, int endIndex) {
/* 4415 */     if (list == null) {
/* 4416 */       return null;
/*      */     }
/* 4418 */     int noOfItems = endIndex - startIndex;
/* 4419 */     if (noOfItems <= 0) {
/* 4420 */       return "";
/*      */     }
/* 4422 */     List<?> subList = list.subList(startIndex, endIndex);
/* 4423 */     return join(subList.iterator(), separator);
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
/*      */   public static String join(List<?> list, String separator, int startIndex, int endIndex) {
/* 4453 */     if (list == null) {
/* 4454 */       return null;
/*      */     }
/* 4456 */     int noOfItems = endIndex - startIndex;
/* 4457 */     if (noOfItems <= 0) {
/* 4458 */       return "";
/*      */     }
/* 4460 */     List<?> subList = list.subList(startIndex, endIndex);
/* 4461 */     return join(subList.iterator(), separator);
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
/*      */   public static String join(long[] array, char separator) {
/* 4489 */     if (array == null) {
/* 4490 */       return null;
/*      */     }
/* 4492 */     return join(array, separator, 0, array.length);
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
/*      */   public static String join(long[] array, char delimiter, int startIndex, int endIndex) {
/* 4525 */     if (array == null) {
/* 4526 */       return null;
/*      */     }
/* 4528 */     if (endIndex - startIndex <= 0) {
/* 4529 */       return "";
/*      */     }
/* 4531 */     StringBuilder stringBuilder = new StringBuilder();
/* 4532 */     for (int i = startIndex; i < endIndex; i++) {
/* 4533 */       stringBuilder
/* 4534 */         .append(array[i])
/* 4535 */         .append(delimiter);
/*      */     }
/* 4537 */     return stringBuilder.substring(0, stringBuilder.length() - 1);
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
/*      */   public static String join(Object[] array, char delimiter) {
/* 4563 */     if (array == null) {
/* 4564 */       return null;
/*      */     }
/* 4566 */     return join(array, delimiter, 0, array.length);
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
/*      */   public static String join(Object[] array, char delimiter, int startIndex, int endIndex) {
/* 4596 */     return join(array, String.valueOf(delimiter), startIndex, endIndex);
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
/*      */   public static String join(Object[] array, String delimiter) {
/* 4623 */     return (array != null) ? join(array, toStringOrEmpty(delimiter), 0, array.length) : null;
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
/*      */   public static String join(Object[] array, String delimiter, int startIndex, int endIndex) {
/* 4662 */     return (array != null) ? (String)Streams.of(array).skip(startIndex).limit(Math.max(0, endIndex - startIndex))
/* 4663 */       .collect(LangCollectors.joining(delimiter, "", "", StringUtils::toStringOrEmpty)) : null;
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
/*      */   public static String join(short[] array, char delimiter) {
/* 4690 */     if (array == null) {
/* 4691 */       return null;
/*      */     }
/* 4693 */     return join(array, delimiter, 0, array.length);
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
/*      */   public static String join(short[] array, char delimiter, int startIndex, int endIndex) {
/* 4726 */     if (array == null) {
/* 4727 */       return null;
/*      */     }
/* 4729 */     if (endIndex - startIndex <= 0) {
/* 4730 */       return "";
/*      */     }
/* 4732 */     StringBuilder stringBuilder = new StringBuilder();
/* 4733 */     for (int i = startIndex; i < endIndex; i++) {
/* 4734 */       stringBuilder
/* 4735 */         .append(array[i])
/* 4736 */         .append(delimiter);
/*      */     }
/* 4738 */     return stringBuilder.substring(0, stringBuilder.length() - 1);
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
/*      */   @SafeVarargs
/*      */   public static <T> String join(T... elements) {
/* 4765 */     return join((Object[])elements, (String)null);
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
/*      */   public static String joinWith(String delimiter, Object... array) {
/* 4789 */     if (array == null) {
/* 4790 */       throw new IllegalArgumentException("Object varargs must not be null");
/*      */     }
/* 4792 */     return join(array, delimiter);
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
/*      */   public static int lastIndexOf(CharSequence seq, CharSequence searchSeq) {
/* 4819 */     if (seq == null) {
/* 4820 */       return -1;
/*      */     }
/* 4822 */     return CharSequenceUtils.lastIndexOf(seq, searchSeq, seq.length());
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
/*      */   public static int lastIndexOf(CharSequence seq, CharSequence searchSeq, int startPos) {
/* 4861 */     return CharSequenceUtils.lastIndexOf(seq, searchSeq, startPos);
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
/*      */   public static int lastIndexOf(CharSequence seq, int searchChar) {
/* 4899 */     if (isEmpty(seq)) {
/* 4900 */       return -1;
/*      */     }
/* 4902 */     return CharSequenceUtils.lastIndexOf(seq, searchChar, seq.length());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int lastIndexOf(CharSequence seq, int searchChar, int startPos) {
/* 4950 */     if (isEmpty(seq)) {
/* 4951 */       return -1;
/*      */     }
/* 4953 */     return CharSequenceUtils.lastIndexOf(seq, searchChar, startPos);
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
/*      */   public static int lastIndexOfAny(CharSequence str, CharSequence... searchStrs) {
/* 4983 */     if (str == null || searchStrs == null) {
/* 4984 */       return -1;
/*      */     }
/* 4986 */     int ret = -1;
/*      */     
/* 4988 */     for (CharSequence search : searchStrs) {
/* 4989 */       if (search != null) {
/*      */ 
/*      */         
/* 4992 */         int tmp = CharSequenceUtils.lastIndexOf(str, search, str.length());
/* 4993 */         if (tmp > ret)
/* 4994 */           ret = tmp; 
/*      */       } 
/*      */     } 
/* 4997 */     return ret;
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
/*      */   public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
/* 5024 */     if (str == null || searchStr == null) {
/* 5025 */       return -1;
/*      */     }
/* 5027 */     return lastIndexOfIgnoreCase(str, searchStr, str.length());
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
/*      */   public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
/* 5063 */     if (str == null || searchStr == null) {
/* 5064 */       return -1;
/*      */     }
/* 5066 */     int searchStrLength = searchStr.length();
/* 5067 */     int strLength = str.length();
/* 5068 */     if (startPos > strLength - searchStrLength) {
/* 5069 */       startPos = strLength - searchStrLength;
/*      */     }
/* 5071 */     if (startPos < 0) {
/* 5072 */       return -1;
/*      */     }
/* 5074 */     if (searchStrLength == 0) {
/* 5075 */       return startPos;
/*      */     }
/*      */     
/* 5078 */     for (int i = startPos; i >= 0; i--) {
/* 5079 */       if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, searchStrLength)) {
/* 5080 */         return i;
/*      */       }
/*      */     } 
/* 5083 */     return -1;
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
/*      */   public static int lastOrdinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
/* 5121 */     return ordinalIndexOf(str, searchStr, ordinal, true);
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
/*      */   public static String left(String str, int len) {
/* 5145 */     if (str == null) {
/* 5146 */       return null;
/*      */     }
/* 5148 */     if (len < 0) {
/* 5149 */       return "";
/*      */     }
/* 5151 */     if (str.length() <= len) {
/* 5152 */       return str;
/*      */     }
/* 5154 */     return str.substring(0, len);
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
/*      */   public static String leftPad(String str, int size) {
/* 5177 */     return leftPad(str, size, ' ');
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
/*      */   public static String leftPad(String str, int size, char padChar) {
/* 5202 */     if (str == null) {
/* 5203 */       return null;
/*      */     }
/* 5205 */     int pads = size - str.length();
/* 5206 */     if (pads <= 0) {
/* 5207 */       return str;
/*      */     }
/* 5209 */     if (pads > 8192) {
/* 5210 */       return leftPad(str, size, String.valueOf(padChar));
/*      */     }
/* 5212 */     return repeat(padChar, pads).concat(str);
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
/*      */   public static String leftPad(String str, int size, String padStr) {
/* 5239 */     if (str == null) {
/* 5240 */       return null;
/*      */     }
/* 5242 */     if (isEmpty(padStr)) {
/* 5243 */       padStr = " ";
/*      */     }
/* 5245 */     int padLen = padStr.length();
/* 5246 */     int strLen = str.length();
/* 5247 */     int pads = size - strLen;
/* 5248 */     if (pads <= 0) {
/* 5249 */       return str;
/*      */     }
/* 5251 */     if (padLen == 1 && pads <= 8192) {
/* 5252 */       return leftPad(str, size, padStr.charAt(0));
/*      */     }
/*      */     
/* 5255 */     if (pads == padLen) {
/* 5256 */       return padStr.concat(str);
/*      */     }
/* 5258 */     if (pads < padLen) {
/* 5259 */       return padStr.substring(0, pads).concat(str);
/*      */     }
/* 5261 */     char[] padding = new char[pads];
/* 5262 */     char[] padChars = padStr.toCharArray();
/* 5263 */     for (int i = 0; i < pads; i++) {
/* 5264 */       padding[i] = padChars[i % padLen];
/*      */     }
/* 5266 */     return (new String(padding)).concat(str);
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
/*      */   public static int length(CharSequence cs) {
/* 5281 */     return (cs == null) ? 0 : cs.length();
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
/*      */   public static String lowerCase(String str) {
/* 5304 */     if (str == null) {
/* 5305 */       return null;
/*      */     }
/* 5307 */     return str.toLowerCase();
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
/*      */   public static String lowerCase(String str, Locale locale) {
/* 5327 */     if (str == null) {
/* 5328 */       return null;
/*      */     }
/* 5330 */     return str.toLowerCase(LocaleUtils.toLocale(locale));
/*      */   }
/*      */ 
/*      */   
/*      */   private static int[] matches(CharSequence first, CharSequence second) {
/*      */     CharSequence max, min;
/* 5336 */     if (first.length() > second.length()) {
/* 5337 */       max = first;
/* 5338 */       min = second;
/*      */     } else {
/* 5340 */       max = second;
/* 5341 */       min = first;
/*      */     } 
/* 5343 */     int range = Math.max(max.length() / 2 - 1, 0);
/* 5344 */     int[] matchIndexes = new int[min.length()];
/* 5345 */     Arrays.fill(matchIndexes, -1);
/* 5346 */     boolean[] matchFlags = new boolean[max.length()];
/* 5347 */     int matches = 0;
/* 5348 */     for (int mi = 0; mi < min.length(); mi++) {
/* 5349 */       char c1 = min.charAt(mi);
/* 5350 */       for (int xi = Math.max(mi - range, 0), xn = Math.min(mi + range + 1, max.length()); xi < xn; xi++) {
/* 5351 */         if (!matchFlags[xi] && c1 == max.charAt(xi)) {
/* 5352 */           matchIndexes[mi] = xi;
/* 5353 */           matchFlags[xi] = true;
/* 5354 */           matches++;
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/* 5359 */     char[] ms1 = new char[matches];
/* 5360 */     char[] ms2 = new char[matches]; int i, si;
/* 5361 */     for (i = 0, si = 0; i < min.length(); i++) {
/* 5362 */       if (matchIndexes[i] != -1) {
/* 5363 */         ms1[si] = min.charAt(i);
/* 5364 */         si++;
/*      */       } 
/*      */     } 
/* 5367 */     for (i = 0, si = 0; i < max.length(); i++) {
/* 5368 */       if (matchFlags[i]) {
/* 5369 */         ms2[si] = max.charAt(i);
/* 5370 */         si++;
/*      */       } 
/*      */     } 
/* 5373 */     int transpositions = 0;
/* 5374 */     for (int j = 0; j < ms1.length; j++) {
/* 5375 */       if (ms1[j] != ms2[j]) {
/* 5376 */         transpositions++;
/*      */       }
/*      */     } 
/* 5379 */     int prefix = 0;
/* 5380 */     for (int k = 0; k < min.length() && 
/* 5381 */       first.charAt(k) == second.charAt(k); k++)
/*      */     {
/*      */       
/* 5384 */       prefix++;
/*      */     }
/* 5386 */     return new int[] { matches, transpositions / 2, prefix, max.length() };
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
/*      */   public static String mid(String str, int pos, int len) {
/* 5415 */     if (str == null) {
/* 5416 */       return null;
/*      */     }
/* 5418 */     if (len < 0 || pos > str.length()) {
/* 5419 */       return "";
/*      */     }
/* 5421 */     if (pos < 0) {
/* 5422 */       pos = 0;
/*      */     }
/* 5424 */     if (str.length() <= pos + len) {
/* 5425 */       return str.substring(pos);
/*      */     }
/* 5427 */     return str.substring(pos, pos + len);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String normalizeSpace(String str) {
/* 5473 */     if (isEmpty(str)) {
/* 5474 */       return str;
/*      */     }
/* 5476 */     int size = str.length();
/* 5477 */     char[] newChars = new char[size];
/* 5478 */     int count = 0;
/* 5479 */     int whitespacesCount = 0;
/* 5480 */     boolean startWhitespaces = true;
/* 5481 */     for (int i = 0; i < size; i++) {
/* 5482 */       char actualChar = str.charAt(i);
/* 5483 */       boolean isWhitespace = Character.isWhitespace(actualChar);
/* 5484 */       if (isWhitespace) {
/* 5485 */         if (whitespacesCount == 0 && !startWhitespaces) {
/* 5486 */           newChars[count++] = " ".charAt(0);
/*      */         }
/* 5488 */         whitespacesCount++;
/*      */       } else {
/* 5490 */         startWhitespaces = false;
/* 5491 */         newChars[count++] = (actualChar == ' ') ? ' ' : actualChar;
/* 5492 */         whitespacesCount = 0;
/*      */       } 
/*      */     } 
/* 5495 */     if (startWhitespaces) {
/* 5496 */       return "";
/*      */     }
/* 5498 */     return (new String(newChars, 0, count - ((whitespacesCount > 0) ? 1 : 0))).trim();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
/* 5552 */     return ordinalIndexOf(str, searchStr, ordinal, false);
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
/*      */   private static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal, boolean lastIndex) {
/* 5571 */     if (str == null || searchStr == null || ordinal <= 0) {
/* 5572 */       return -1;
/*      */     }
/* 5574 */     if (searchStr.length() == 0) {
/* 5575 */       return lastIndex ? str.length() : 0;
/*      */     }
/* 5577 */     int found = 0;
/*      */ 
/*      */     
/* 5580 */     int index = lastIndex ? str.length() : -1;
/*      */     while (true) {
/* 5582 */       if (lastIndex) {
/* 5583 */         index = CharSequenceUtils.lastIndexOf(str, searchStr, index - 1);
/*      */       } else {
/* 5585 */         index = CharSequenceUtils.indexOf(str, searchStr, index + 1);
/*      */       } 
/* 5587 */       if (index < 0) {
/* 5588 */         return index;
/*      */       }
/* 5590 */       found++;
/* 5591 */       if (found >= ordinal) {
/* 5592 */         return index;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String overlay(String str, String overlay, int start, int end) {
/* 5625 */     if (str == null) {
/* 5626 */       return null;
/*      */     }
/* 5628 */     if (overlay == null) {
/* 5629 */       overlay = "";
/*      */     }
/* 5631 */     int len = str.length();
/* 5632 */     if (start < 0) {
/* 5633 */       start = 0;
/*      */     }
/* 5635 */     if (start > len) {
/* 5636 */       start = len;
/*      */     }
/* 5638 */     if (end < 0) {
/* 5639 */       end = 0;
/*      */     }
/* 5641 */     if (end > len) {
/* 5642 */       end = len;
/*      */     }
/* 5644 */     if (start > end) {
/* 5645 */       int temp = start;
/* 5646 */       start = end;
/* 5647 */       end = temp;
/*      */     } 
/* 5649 */     return str.substring(0, start) + overlay + str
/*      */       
/* 5651 */       .substring(end);
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
/*      */   private static String prependIfMissing(String str, CharSequence prefix, boolean ignoreCase, CharSequence... prefixes) {
/* 5666 */     if (str == null || isEmpty(prefix) || startsWith(str, prefix, ignoreCase)) {
/* 5667 */       return str;
/*      */     }
/* 5669 */     if (ArrayUtils.isNotEmpty(prefixes)) {
/* 5670 */       for (CharSequence p : prefixes) {
/* 5671 */         if (startsWith(str, p, ignoreCase)) {
/* 5672 */           return str;
/*      */         }
/*      */       } 
/*      */     }
/* 5676 */     return prefix.toString() + str;
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
/*      */   public static String prependIfMissing(String str, CharSequence prefix, CharSequence... prefixes) {
/* 5714 */     return prependIfMissing(str, prefix, false, prefixes);
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
/*      */   public static String prependIfMissingIgnoreCase(String str, CharSequence prefix, CharSequence... prefixes) {
/* 5752 */     return prependIfMissing(str, prefix, true, prefixes);
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
/*      */   public static String remove(String str, char remove) {
/* 5775 */     if (isEmpty(str) || str.indexOf(remove) == -1) {
/* 5776 */       return str;
/*      */     }
/* 5778 */     char[] chars = str.toCharArray();
/* 5779 */     int pos = 0;
/* 5780 */     for (int i = 0; i < chars.length; i++) {
/* 5781 */       if (chars[i] != remove) {
/* 5782 */         chars[pos++] = chars[i];
/*      */       }
/*      */     } 
/* 5785 */     return new String(chars, 0, pos);
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
/*      */   public static String remove(String str, String remove) {
/* 5812 */     if (isEmpty(str) || isEmpty(remove)) {
/* 5813 */       return str;
/*      */     }
/* 5815 */     return replace(str, remove, "", -1);
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
/*      */   public static String removeAll(String text, String regex) {
/* 5865 */     return RegExUtils.removeAll(text, regex);
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
/*      */   public static String removeEnd(String str, String remove) {
/* 5893 */     if (isEmpty(str) || isEmpty(remove)) {
/* 5894 */       return str;
/*      */     }
/* 5896 */     if (str.endsWith(remove)) {
/* 5897 */       return str.substring(0, str.length() - remove.length());
/*      */     }
/* 5899 */     return str;
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
/*      */   public static String removeEndIgnoreCase(String str, String remove) {
/* 5929 */     if (isEmpty(str) || isEmpty(remove)) {
/* 5930 */       return str;
/*      */     }
/* 5932 */     if (endsWithIgnoreCase(str, remove)) {
/* 5933 */       return str.substring(0, str.length() - remove.length());
/*      */     }
/* 5935 */     return str;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static String removeFirst(String text, String regex) {
/* 5984 */     return replaceFirst(text, regex, "");
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
/*      */   public static String removeIgnoreCase(String str, String remove) {
/* 6019 */     return replaceIgnoreCase(str, remove, "", -1);
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
/*      */   @Deprecated
/*      */   public static String removePattern(String source, String regex) {
/* 6055 */     return RegExUtils.removePattern(source, regex);
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
/*      */   public static String removeStart(String str, char remove) {
/* 6082 */     if (isEmpty(str)) {
/* 6083 */       return str;
/*      */     }
/* 6085 */     return (str.charAt(0) == remove) ? str.substring(1) : str;
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
/*      */   public static String removeStart(String str, String remove) {
/* 6113 */     if (isEmpty(str) || isEmpty(remove)) {
/* 6114 */       return str;
/*      */     }
/* 6116 */     if (str.startsWith(remove)) {
/* 6117 */       return str.substring(remove.length());
/*      */     }
/* 6119 */     return str;
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
/*      */   public static String removeStartIgnoreCase(String str, String remove) {
/* 6148 */     if (str != null && startsWithIgnoreCase(str, remove)) {
/* 6149 */       return str.substring(length(remove));
/*      */     }
/* 6151 */     return str;
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
/*      */   public static String repeat(char ch, int repeat) {
/* 6177 */     if (repeat <= 0) {
/* 6178 */       return "";
/*      */     }
/* 6180 */     char[] buf = new char[repeat];
/* 6181 */     Arrays.fill(buf, ch);
/* 6182 */     return new String(buf);
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
/*      */   public static String repeat(String str, int repeat) {
/*      */     char ch0, ch1, output2[];
/*      */     int i;
/* 6205 */     if (str == null) {
/* 6206 */       return null;
/*      */     }
/* 6208 */     if (repeat <= 0) {
/* 6209 */       return "";
/*      */     }
/* 6211 */     int inputLength = str.length();
/* 6212 */     if (repeat == 1 || inputLength == 0) {
/* 6213 */       return str;
/*      */     }
/* 6215 */     if (inputLength == 1 && repeat <= 8192) {
/* 6216 */       return repeat(str.charAt(0), repeat);
/*      */     }
/*      */     
/* 6219 */     int outputLength = inputLength * repeat;
/* 6220 */     switch (inputLength) {
/*      */       case 1:
/* 6222 */         return repeat(str.charAt(0), repeat);
/*      */       case 2:
/* 6224 */         ch0 = str.charAt(0);
/* 6225 */         ch1 = str.charAt(1);
/* 6226 */         output2 = new char[outputLength];
/* 6227 */         for (i = repeat * 2 - 2; i >= 0; i--, i--) {
/* 6228 */           output2[i] = ch0;
/* 6229 */           output2[i + 1] = ch1;
/*      */         } 
/* 6231 */         return new String(output2);
/*      */     } 
/* 6233 */     StringBuilder buf = new StringBuilder(outputLength);
/* 6234 */     for (int j = 0; j < repeat; j++) {
/* 6235 */       buf.append(str);
/*      */     }
/* 6237 */     return buf.toString();
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
/*      */   public static String repeat(String str, String separator, int repeat) {
/* 6262 */     if (str == null || separator == null) {
/* 6263 */       return repeat(str, repeat);
/*      */     }
/*      */     
/* 6266 */     String result = repeat(str + separator, repeat);
/* 6267 */     return removeEnd(result, separator);
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
/*      */   public static String replace(String text, String searchString, String replacement) {
/* 6294 */     return replace(text, searchString, replacement, -1);
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
/*      */   public static String replace(String text, String searchString, String replacement, int max) {
/* 6326 */     return replace(text, searchString, replacement, max, false);
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
/*      */   private static String replace(String text, String searchString, String replacement, int max, boolean ignoreCase) {
/* 6361 */     if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
/* 6362 */       return text;
/*      */     }
/* 6364 */     if (ignoreCase) {
/* 6365 */       searchString = searchString.toLowerCase();
/*      */     }
/* 6367 */     int start = 0;
/* 6368 */     int end = ignoreCase ? indexOfIgnoreCase(text, searchString, start) : indexOf(text, searchString, start);
/* 6369 */     if (end == -1) {
/* 6370 */       return text;
/*      */     }
/* 6372 */     int replLength = searchString.length();
/* 6373 */     int increase = Math.max(replacement.length() - replLength, 0);
/* 6374 */     increase *= (max < 0) ? 16 : Math.min(max, 64);
/* 6375 */     StringBuilder buf = new StringBuilder(text.length() + increase);
/* 6376 */     while (end != -1) {
/* 6377 */       buf.append(text, start, end).append(replacement);
/* 6378 */       start = end + replLength;
/* 6379 */       if (--max == 0) {
/*      */         break;
/*      */       }
/* 6382 */       end = ignoreCase ? indexOfIgnoreCase(text, searchString, start) : indexOf(text, searchString, start);
/*      */     } 
/* 6384 */     buf.append(text, start, text.length());
/* 6385 */     return buf.toString();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static String replaceAll(String text, String regex, String replacement) {
/* 6440 */     return RegExUtils.replaceAll(text, regex, replacement);
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
/*      */   public static String replaceChars(String str, char searchChar, char replaceChar) {
/* 6464 */     if (str == null) {
/* 6465 */       return null;
/*      */     }
/* 6467 */     return str.replace(searchChar, replaceChar);
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
/*      */   public static String replaceChars(String str, String searchChars, String replaceChars) {
/* 6507 */     if (isEmpty(str) || isEmpty(searchChars)) {
/* 6508 */       return str;
/*      */     }
/* 6510 */     if (replaceChars == null) {
/* 6511 */       replaceChars = "";
/*      */     }
/* 6513 */     boolean modified = false;
/* 6514 */     int replaceCharsLength = replaceChars.length();
/* 6515 */     int strLength = str.length();
/* 6516 */     StringBuilder buf = new StringBuilder(strLength);
/* 6517 */     for (int i = 0; i < strLength; i++) {
/* 6518 */       char ch = str.charAt(i);
/* 6519 */       int index = searchChars.indexOf(ch);
/* 6520 */       if (index >= 0) {
/* 6521 */         modified = true;
/* 6522 */         if (index < replaceCharsLength) {
/* 6523 */           buf.append(replaceChars.charAt(index));
/*      */         }
/*      */       } else {
/* 6526 */         buf.append(ch);
/*      */       } 
/*      */     } 
/* 6529 */     if (modified) {
/* 6530 */       return buf.toString();
/*      */     }
/* 6532 */     return str;
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
/*      */   
/*      */   public static String replaceEach(String text, String[] searchList, String[] replacementList) {
/* 6573 */     return replaceEach(text, searchList, replacementList, false, 0);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String replaceEach(String text, String[] searchList, String[] replacementList, boolean repeat, int timeToLive) {
/* 6631 */     if (timeToLive < 0) {
/* 6632 */       Set<String> searchSet = new HashSet<>(Arrays.asList(searchList));
/* 6633 */       Set<String> replacementSet = new HashSet<>(Arrays.asList(replacementList));
/* 6634 */       searchSet.retainAll(replacementSet);
/* 6635 */       if (!searchSet.isEmpty()) {
/* 6636 */         throw new IllegalStateException("Aborting to protect against StackOverflowError - output of one loop is the input of another");
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 6641 */     if (isEmpty(text) || ArrayUtils.isEmpty((Object[])searchList) || ArrayUtils.isEmpty((Object[])replacementList) || (ArrayUtils.isNotEmpty(searchList) && timeToLive == -1)) {
/* 6642 */       return text;
/*      */     }
/*      */     
/* 6645 */     int searchLength = searchList.length;
/* 6646 */     int replacementLength = replacementList.length;
/*      */ 
/*      */     
/* 6649 */     if (searchLength != replacementLength) {
/* 6650 */       throw new IllegalArgumentException("Search and Replace array lengths don't match: " + searchLength + " vs " + replacementLength);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6657 */     boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];
/*      */ 
/*      */     
/* 6660 */     int textIndex = -1;
/* 6661 */     int replaceIndex = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6666 */     for (int i = 0; i < searchLength; i++) {
/* 6667 */       if (!noMoreMatchesForReplIndex[i] && !isEmpty(searchList[i]) && replacementList[i] != null) {
/*      */ 
/*      */         
/* 6670 */         int tempIndex = text.indexOf(searchList[i]);
/*      */ 
/*      */         
/* 6673 */         if (tempIndex == -1) {
/* 6674 */           noMoreMatchesForReplIndex[i] = true;
/* 6675 */         } else if (textIndex == -1 || tempIndex < textIndex) {
/* 6676 */           textIndex = tempIndex;
/* 6677 */           replaceIndex = i;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 6683 */     if (textIndex == -1) {
/* 6684 */       return text;
/*      */     }
/*      */     
/* 6687 */     int start = 0;
/*      */ 
/*      */     
/* 6690 */     int increase = 0;
/*      */ 
/*      */     
/* 6693 */     for (int j = 0; j < searchList.length; j++) {
/* 6694 */       if (searchList[j] != null && replacementList[j] != null) {
/*      */ 
/*      */         
/* 6697 */         int greater = replacementList[j].length() - searchList[j].length();
/* 6698 */         if (greater > 0) {
/* 6699 */           increase += 3 * greater;
/*      */         }
/*      */       } 
/*      */     } 
/* 6703 */     increase = Math.min(increase, text.length() / 5);
/*      */     
/* 6705 */     StringBuilder buf = new StringBuilder(text.length() + increase);
/*      */     
/* 6707 */     while (textIndex != -1) {
/*      */       int m;
/* 6709 */       for (m = start; m < textIndex; m++) {
/* 6710 */         buf.append(text.charAt(m));
/*      */       }
/* 6712 */       buf.append(replacementList[replaceIndex]);
/*      */       
/* 6714 */       start = textIndex + searchList[replaceIndex].length();
/*      */       
/* 6716 */       textIndex = -1;
/* 6717 */       replaceIndex = -1;
/*      */ 
/*      */       
/* 6720 */       for (m = 0; m < searchLength; m++) {
/* 6721 */         if (!noMoreMatchesForReplIndex[m] && !isEmpty(searchList[m]) && replacementList[m] != null) {
/*      */ 
/*      */           
/* 6724 */           int tempIndex = text.indexOf(searchList[m], start);
/*      */ 
/*      */           
/* 6727 */           if (tempIndex == -1) {
/* 6728 */             noMoreMatchesForReplIndex[m] = true;
/* 6729 */           } else if (textIndex == -1 || tempIndex < textIndex) {
/* 6730 */             textIndex = tempIndex;
/* 6731 */             replaceIndex = m;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 6737 */     int textLength = text.length();
/* 6738 */     for (int k = start; k < textLength; k++) {
/* 6739 */       buf.append(text.charAt(k));
/*      */     }
/* 6741 */     String result = buf.toString();
/* 6742 */     if (!repeat) {
/* 6743 */       return result;
/*      */     }
/*      */     
/* 6746 */     return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String replaceEachRepeatedly(String text, String[] searchList, String[] replacementList) {
/* 6790 */     return replaceEach(text, searchList, replacementList, true, ArrayUtils.getLength(searchList));
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
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static String replaceFirst(String text, String regex, String replacement) {
/* 6843 */     return RegExUtils.replaceFirst(text, regex, replacement);
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
/*      */   public static String replaceIgnoreCase(String text, String searchString, String replacement) {
/* 6871 */     return replaceIgnoreCase(text, searchString, replacement, -1);
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
/*      */   public static String replaceIgnoreCase(String text, String searchString, String replacement, int max) {
/* 6904 */     return replace(text, searchString, replacement, max, true);
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
/*      */   public static String replaceOnce(String text, String searchString, String replacement) {
/* 6931 */     return replace(text, searchString, replacement, 1);
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
/*      */   public static String replaceOnceIgnoreCase(String text, String searchString, String replacement) {
/* 6960 */     return replaceIgnoreCase(text, searchString, replacement, 1);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static String replacePattern(String source, String regex, String replacement) {
/* 7006 */     return RegExUtils.replacePattern(source, regex, replacement);
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
/*      */   public static String reverse(String str) {
/* 7024 */     if (str == null) {
/* 7025 */       return null;
/*      */     }
/* 7027 */     return (new StringBuilder(str)).reverse().toString();
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
/*      */   public static String reverseDelimited(String str, char separatorChar) {
/* 7050 */     if (str == null) {
/* 7051 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 7055 */     String[] strs = split(str, separatorChar);
/* 7056 */     ArrayUtils.reverse((Object[])strs);
/* 7057 */     return join((Object[])strs, separatorChar);
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
/*      */   public static String right(String str, int len) {
/* 7081 */     if (str == null) {
/* 7082 */       return null;
/*      */     }
/* 7084 */     if (len < 0) {
/* 7085 */       return "";
/*      */     }
/* 7087 */     if (str.length() <= len) {
/* 7088 */       return str;
/*      */     }
/* 7090 */     return str.substring(str.length() - len);
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
/*      */   public static String rightPad(String str, int size) {
/* 7113 */     return rightPad(str, size, ' ');
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
/*      */   public static String rightPad(String str, int size, char padChar) {
/* 7138 */     if (str == null) {
/* 7139 */       return null;
/*      */     }
/* 7141 */     int pads = size - str.length();
/* 7142 */     if (pads <= 0) {
/* 7143 */       return str;
/*      */     }
/* 7145 */     if (pads > 8192) {
/* 7146 */       return rightPad(str, size, String.valueOf(padChar));
/*      */     }
/* 7148 */     return str.concat(repeat(padChar, pads));
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
/*      */   public static String rightPad(String str, int size, String padStr) {
/* 7175 */     if (str == null) {
/* 7176 */       return null;
/*      */     }
/* 7178 */     if (isEmpty(padStr)) {
/* 7179 */       padStr = " ";
/*      */     }
/* 7181 */     int padLen = padStr.length();
/* 7182 */     int strLen = str.length();
/* 7183 */     int pads = size - strLen;
/* 7184 */     if (pads <= 0) {
/* 7185 */       return str;
/*      */     }
/* 7187 */     if (padLen == 1 && pads <= 8192) {
/* 7188 */       return rightPad(str, size, padStr.charAt(0));
/*      */     }
/*      */     
/* 7191 */     if (pads == padLen) {
/* 7192 */       return str.concat(padStr);
/*      */     }
/* 7194 */     if (pads < padLen) {
/* 7195 */       return str.concat(padStr.substring(0, pads));
/*      */     }
/* 7197 */     char[] padding = new char[pads];
/* 7198 */     char[] padChars = padStr.toCharArray();
/* 7199 */     for (int i = 0; i < pads; i++) {
/* 7200 */       padding[i] = padChars[i % padLen];
/*      */     }
/* 7202 */     return str.concat(new String(padding));
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
/*      */   public static String rotate(String str, int shift) {
/* 7232 */     if (str == null) {
/* 7233 */       return null;
/*      */     }
/*      */     
/* 7236 */     int strLen = str.length();
/* 7237 */     if (shift == 0 || strLen == 0 || shift % strLen == 0) {
/* 7238 */       return str;
/*      */     }
/*      */     
/* 7241 */     StringBuilder builder = new StringBuilder(strLen);
/* 7242 */     int offset = -(shift % strLen);
/* 7243 */     builder.append(substring(str, offset));
/* 7244 */     builder.append(substring(str, 0, offset));
/* 7245 */     return builder.toString();
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
/*      */   public static String[] split(String str) {
/* 7271 */     return split(str, null, -1);
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
/*      */   public static String[] split(String str, char separatorChar) {
/* 7299 */     return splitWorker(str, separatorChar, false);
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
/*      */   public static String[] split(String str, String separatorChars) {
/* 7328 */     return splitWorker(str, separatorChars, -1, false);
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
/*      */   public static String[] split(String str, String separatorChars, int max) {
/* 7362 */     return splitWorker(str, separatorChars, max, false);
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
/*      */   public static String[] splitByCharacterType(String str) {
/* 7385 */     return splitByCharacterType(str, false);
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
/*      */   private static String[] splitByCharacterType(String str, boolean camelCase) {
/* 7403 */     if (str == null) {
/* 7404 */       return null;
/*      */     }
/* 7406 */     if (str.isEmpty()) {
/* 7407 */       return ArrayUtils.EMPTY_STRING_ARRAY;
/*      */     }
/* 7409 */     char[] c = str.toCharArray();
/* 7410 */     List<String> list = new ArrayList<>();
/* 7411 */     int tokenStart = 0;
/* 7412 */     int currentType = Character.getType(c[tokenStart]);
/* 7413 */     for (int pos = tokenStart + 1; pos < c.length; pos++) {
/* 7414 */       int type = Character.getType(c[pos]);
/* 7415 */       if (type != currentType) {
/*      */ 
/*      */         
/* 7418 */         if (camelCase && type == 2 && currentType == 1) {
/* 7419 */           int newTokenStart = pos - 1;
/* 7420 */           if (newTokenStart != tokenStart) {
/* 7421 */             list.add(new String(c, tokenStart, newTokenStart - tokenStart));
/* 7422 */             tokenStart = newTokenStart;
/*      */           } 
/*      */         } else {
/* 7425 */           list.add(new String(c, tokenStart, pos - tokenStart));
/* 7426 */           tokenStart = pos;
/*      */         } 
/* 7428 */         currentType = type;
/*      */       } 
/* 7430 */     }  list.add(new String(c, tokenStart, c.length - tokenStart));
/* 7431 */     return list.<String>toArray(ArrayUtils.EMPTY_STRING_ARRAY);
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
/*      */   public static String[] splitByCharacterTypeCamelCase(String str) {
/* 7459 */     return splitByCharacterType(str, true);
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
/*      */   public static String[] splitByWholeSeparator(String str, String separator) {
/* 7486 */     return splitByWholeSeparatorWorker(str, separator, -1, false);
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
/*      */   public static String[] splitByWholeSeparator(String str, String separator, int max) {
/* 7517 */     return splitByWholeSeparatorWorker(str, separator, max, false);
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
/*      */   public static String[] splitByWholeSeparatorPreserveAllTokens(String str, String separator) {
/* 7546 */     return splitByWholeSeparatorWorker(str, separator, -1, true);
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
/*      */   public static String[] splitByWholeSeparatorPreserveAllTokens(String str, String separator, int max) {
/* 7579 */     return splitByWholeSeparatorWorker(str, separator, max, true);
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
/*      */   private static String[] splitByWholeSeparatorWorker(String str, String separator, int max, boolean preserveAllTokens) {
/* 7598 */     if (str == null) {
/* 7599 */       return null;
/*      */     }
/*      */     
/* 7602 */     int len = str.length();
/*      */     
/* 7604 */     if (len == 0) {
/* 7605 */       return ArrayUtils.EMPTY_STRING_ARRAY;
/*      */     }
/*      */     
/* 7608 */     if (separator == null || "".equals(separator))
/*      */     {
/* 7610 */       return splitWorker(str, null, max, preserveAllTokens);
/*      */     }
/*      */     
/* 7613 */     int separatorLength = separator.length();
/*      */     
/* 7615 */     ArrayList<String> substrings = new ArrayList<>();
/* 7616 */     int numberOfSubstrings = 0;
/* 7617 */     int beg = 0;
/* 7618 */     int end = 0;
/* 7619 */     while (end < len) {
/* 7620 */       end = str.indexOf(separator, beg);
/*      */       
/* 7622 */       if (end > -1) {
/* 7623 */         if (end > beg) {
/* 7624 */           numberOfSubstrings++;
/*      */           
/* 7626 */           if (numberOfSubstrings == max) {
/* 7627 */             end = len;
/* 7628 */             substrings.add(str.substring(beg));
/*      */             
/*      */             continue;
/*      */           } 
/* 7632 */           substrings.add(str.substring(beg, end));
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 7637 */           beg = end + separatorLength;
/*      */           
/*      */           continue;
/*      */         } 
/* 7641 */         if (preserveAllTokens) {
/* 7642 */           numberOfSubstrings++;
/* 7643 */           if (numberOfSubstrings == max) {
/* 7644 */             end = len;
/* 7645 */             substrings.add(str.substring(beg));
/*      */           } else {
/* 7647 */             substrings.add("");
/*      */           } 
/*      */         } 
/* 7650 */         beg = end + separatorLength;
/*      */         
/*      */         continue;
/*      */       } 
/* 7654 */       substrings.add(str.substring(beg));
/* 7655 */       end = len;
/*      */     } 
/*      */ 
/*      */     
/* 7659 */     return substrings.<String>toArray(ArrayUtils.EMPTY_STRING_ARRAY);
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
/*      */   public static String[] splitPreserveAllTokens(String str) {
/* 7687 */     return splitWorker(str, null, -1, true);
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
/*      */   public static String[] splitPreserveAllTokens(String str, char separatorChar) {
/* 7723 */     return splitWorker(str, separatorChar, true);
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
/*      */   public static String[] splitPreserveAllTokens(String str, String separatorChars) {
/* 7760 */     return splitWorker(str, separatorChars, -1, true);
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
/*      */   public static String[] splitPreserveAllTokens(String str, String separatorChars, int max) {
/* 7800 */     return splitWorker(str, separatorChars, max, true);
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
/*      */   private static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens) {
/* 7818 */     if (str == null) {
/* 7819 */       return null;
/*      */     }
/* 7821 */     int len = str.length();
/* 7822 */     if (len == 0) {
/* 7823 */       return ArrayUtils.EMPTY_STRING_ARRAY;
/*      */     }
/* 7825 */     List<String> list = new ArrayList<>();
/* 7826 */     int i = 0;
/* 7827 */     int start = 0;
/* 7828 */     boolean match = false;
/* 7829 */     boolean lastMatch = false;
/* 7830 */     while (i < len) {
/* 7831 */       if (str.charAt(i) == separatorChar) {
/* 7832 */         if (match || preserveAllTokens) {
/* 7833 */           list.add(str.substring(start, i));
/* 7834 */           match = false;
/* 7835 */           lastMatch = true;
/*      */         } 
/* 7837 */         start = ++i;
/*      */         continue;
/*      */       } 
/* 7840 */       lastMatch = false;
/* 7841 */       match = true;
/* 7842 */       i++;
/*      */     } 
/* 7844 */     if (match || (preserveAllTokens && lastMatch)) {
/* 7845 */       list.add(str.substring(start, i));
/*      */     }
/* 7847 */     return list.<String>toArray(ArrayUtils.EMPTY_STRING_ARRAY);
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
/*      */   private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
/* 7869 */     if (str == null) {
/* 7870 */       return null;
/*      */     }
/* 7872 */     int len = str.length();
/* 7873 */     if (len == 0) {
/* 7874 */       return ArrayUtils.EMPTY_STRING_ARRAY;
/*      */     }
/* 7876 */     List<String> list = new ArrayList<>();
/* 7877 */     int sizePlus1 = 1;
/* 7878 */     int i = 0;
/* 7879 */     int start = 0;
/* 7880 */     boolean match = false;
/* 7881 */     boolean lastMatch = false;
/* 7882 */     if (separatorChars == null) {
/*      */       
/* 7884 */       while (i < len) {
/* 7885 */         if (Character.isWhitespace(str.charAt(i))) {
/* 7886 */           if (match || preserveAllTokens) {
/* 7887 */             lastMatch = true;
/* 7888 */             if (sizePlus1++ == max) {
/* 7889 */               i = len;
/* 7890 */               lastMatch = false;
/*      */             } 
/* 7892 */             list.add(str.substring(start, i));
/* 7893 */             match = false;
/*      */           } 
/* 7895 */           start = ++i;
/*      */           continue;
/*      */         } 
/* 7898 */         lastMatch = false;
/* 7899 */         match = true;
/* 7900 */         i++;
/*      */       } 
/* 7902 */     } else if (separatorChars.length() == 1) {
/*      */       
/* 7904 */       char sep = separatorChars.charAt(0);
/* 7905 */       while (i < len) {
/* 7906 */         if (str.charAt(i) == sep) {
/* 7907 */           if (match || preserveAllTokens) {
/* 7908 */             lastMatch = true;
/* 7909 */             if (sizePlus1++ == max) {
/* 7910 */               i = len;
/* 7911 */               lastMatch = false;
/*      */             } 
/* 7913 */             list.add(str.substring(start, i));
/* 7914 */             match = false;
/*      */           } 
/* 7916 */           start = ++i;
/*      */           continue;
/*      */         } 
/* 7919 */         lastMatch = false;
/* 7920 */         match = true;
/* 7921 */         i++;
/*      */       } 
/*      */     } else {
/*      */       
/* 7925 */       while (i < len) {
/* 7926 */         if (separatorChars.indexOf(str.charAt(i)) >= 0) {
/* 7927 */           if (match || preserveAllTokens) {
/* 7928 */             lastMatch = true;
/* 7929 */             if (sizePlus1++ == max) {
/* 7930 */               i = len;
/* 7931 */               lastMatch = false;
/*      */             } 
/* 7933 */             list.add(str.substring(start, i));
/* 7934 */             match = false;
/*      */           } 
/* 7936 */           start = ++i;
/*      */           continue;
/*      */         } 
/* 7939 */         lastMatch = false;
/* 7940 */         match = true;
/* 7941 */         i++;
/*      */       } 
/*      */     } 
/* 7944 */     if (match || (preserveAllTokens && lastMatch)) {
/* 7945 */       list.add(str.substring(start, i));
/*      */     }
/* 7947 */     return list.<String>toArray(ArrayUtils.EMPTY_STRING_ARRAY);
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
/*      */   public static boolean startsWith(CharSequence str, CharSequence prefix) {
/* 7973 */     return startsWith(str, prefix, false);
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
/*      */   private static boolean startsWith(CharSequence str, CharSequence prefix, boolean ignoreCase) {
/* 7988 */     if (str == null || prefix == null) {
/* 7989 */       return (str == prefix);
/*      */     }
/*      */     
/* 7992 */     int preLen = prefix.length();
/* 7993 */     if (preLen > str.length()) {
/* 7994 */       return false;
/*      */     }
/* 7996 */     return CharSequenceUtils.regionMatches(str, ignoreCase, 0, prefix, 0, preLen);
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
/*      */   public static boolean startsWithAny(CharSequence sequence, CharSequence... searchStrings) {
/* 8022 */     if (isEmpty(sequence) || ArrayUtils.isEmpty((Object[])searchStrings)) {
/* 8023 */       return false;
/*      */     }
/* 8025 */     for (CharSequence searchString : searchStrings) {
/* 8026 */       if (startsWith(sequence, searchString)) {
/* 8027 */         return true;
/*      */       }
/*      */     } 
/* 8030 */     return false;
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
/*      */   public static boolean startsWithIgnoreCase(CharSequence str, CharSequence prefix) {
/* 8056 */     return startsWith(str, prefix, true);
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
/*      */   public static String strip(String str) {
/* 8082 */     return strip(str, null);
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
/*      */   public static String strip(String str, String stripChars) {
/* 8112 */     str = stripStart(str, stripChars);
/* 8113 */     return stripEnd(str, stripChars);
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
/*      */   public static String stripAccents(String input) {
/* 8135 */     if (input == null) {
/* 8136 */       return null;
/*      */     }
/* 8138 */     StringBuilder decomposed = new StringBuilder(Normalizer.normalize(input, Normalizer.Form.NFD));
/* 8139 */     convertRemainingAccentCharacters(decomposed);
/*      */     
/* 8141 */     return STRIP_ACCENTS_PATTERN.matcher(decomposed).replaceAll("");
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
/*      */   public static String[] stripAll(String... strs) {
/* 8164 */     return stripAll(strs, null);
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
/*      */   public static String[] stripAll(String[] strs, String stripChars) {
/* 8193 */     int strsLen = ArrayUtils.getLength(strs);
/* 8194 */     if (strsLen == 0) {
/* 8195 */       return strs;
/*      */     }
/* 8197 */     String[] newArr = new String[strsLen];
/* 8198 */     Arrays.setAll(newArr, i -> strip(strs[i], stripChars));
/* 8199 */     return newArr;
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
/*      */   public static String stripEnd(String str, String stripChars) {
/* 8228 */     int end = length(str);
/* 8229 */     if (end == 0) {
/* 8230 */       return str;
/*      */     }
/*      */     
/* 8233 */     if (stripChars == null) {
/* 8234 */       while (end != 0 && Character.isWhitespace(str.charAt(end - 1)))
/* 8235 */         end--; 
/*      */     } else {
/* 8237 */       if (stripChars.isEmpty()) {
/* 8238 */         return str;
/*      */       }
/* 8240 */       while (end != 0 && stripChars.indexOf(str.charAt(end - 1)) != -1) {
/* 8241 */         end--;
/*      */       }
/*      */     } 
/* 8244 */     return str.substring(0, end);
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
/*      */   public static String stripStart(String str, String stripChars) {
/* 8272 */     int strLen = length(str);
/* 8273 */     if (strLen == 0) {
/* 8274 */       return str;
/*      */     }
/* 8276 */     int start = 0;
/* 8277 */     if (stripChars == null) {
/* 8278 */       while (start != strLen && Character.isWhitespace(str.charAt(start)))
/* 8279 */         start++; 
/*      */     } else {
/* 8281 */       if (stripChars.isEmpty()) {
/* 8282 */         return str;
/*      */       }
/* 8284 */       while (start != strLen && stripChars.indexOf(str.charAt(start)) != -1) {
/* 8285 */         start++;
/*      */       }
/*      */     } 
/* 8288 */     return str.substring(start);
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
/*      */   public static String stripToEmpty(String str) {
/* 8314 */     return (str == null) ? "" : strip(str, null);
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
/*      */   public static String stripToNull(String str) {
/* 8341 */     if (str == null) {
/* 8342 */       return null;
/*      */     }
/* 8344 */     str = strip(str, null);
/* 8345 */     return str.isEmpty() ? null : str;
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
/*      */   public static String substring(String str, int start) {
/* 8373 */     if (str == null) {
/* 8374 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 8378 */     if (start < 0) {
/* 8379 */       start = str.length() + start;
/*      */     }
/*      */     
/* 8382 */     if (start < 0) {
/* 8383 */       start = 0;
/*      */     }
/* 8385 */     if (start > str.length()) {
/* 8386 */       return "";
/*      */     }
/*      */     
/* 8389 */     return str.substring(start);
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
/*      */   public static String substring(String str, int start, int end) {
/* 8428 */     if (str == null) {
/* 8429 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 8433 */     if (end < 0) {
/* 8434 */       end = str.length() + end;
/*      */     }
/* 8436 */     if (start < 0) {
/* 8437 */       start = str.length() + start;
/*      */     }
/*      */ 
/*      */     
/* 8441 */     if (end > str.length()) {
/* 8442 */       end = str.length();
/*      */     }
/*      */ 
/*      */     
/* 8446 */     if (start > end) {
/* 8447 */       return "";
/*      */     }
/*      */     
/* 8450 */     if (start < 0) {
/* 8451 */       start = 0;
/*      */     }
/* 8453 */     if (end < 0) {
/* 8454 */       end = 0;
/*      */     }
/*      */     
/* 8457 */     return str.substring(start, end);
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
/*      */   public static String substringAfter(String str, int separator) {
/* 8486 */     if (isEmpty(str)) {
/* 8487 */       return str;
/*      */     }
/* 8489 */     int pos = str.indexOf(separator);
/* 8490 */     if (pos == -1) {
/* 8491 */       return "";
/*      */     }
/* 8493 */     return str.substring(pos + 1);
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
/*      */   public static String substringAfter(String str, String separator) {
/* 8525 */     if (isEmpty(str)) {
/* 8526 */       return str;
/*      */     }
/* 8528 */     if (separator == null) {
/* 8529 */       return "";
/*      */     }
/* 8531 */     int pos = str.indexOf(separator);
/* 8532 */     if (pos == -1) {
/* 8533 */       return "";
/*      */     }
/* 8535 */     return str.substring(pos + separator.length());
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
/*      */   public static String substringAfterLast(String str, int separator) {
/* 8565 */     if (isEmpty(str)) {
/* 8566 */       return str;
/*      */     }
/* 8568 */     int pos = str.lastIndexOf(separator);
/* 8569 */     if (pos == -1 || pos == str.length() - 1) {
/* 8570 */       return "";
/*      */     }
/* 8572 */     return str.substring(pos + 1);
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
/*      */   public static String substringAfterLast(String str, String separator) {
/* 8605 */     if (isEmpty(str)) {
/* 8606 */       return str;
/*      */     }
/* 8608 */     if (isEmpty(separator)) {
/* 8609 */       return "";
/*      */     }
/* 8611 */     int pos = str.lastIndexOf(separator);
/* 8612 */     if (pos == -1 || pos == str.length() - separator.length()) {
/* 8613 */       return "";
/*      */     }
/* 8615 */     return str.substring(pos + separator.length());
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
/*      */   public static String substringBefore(String str, int separator) {
/* 8644 */     if (isEmpty(str)) {
/* 8645 */       return str;
/*      */     }
/* 8647 */     int pos = str.indexOf(separator);
/* 8648 */     if (pos == -1) {
/* 8649 */       return str;
/*      */     }
/* 8651 */     return str.substring(0, pos);
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
/*      */   public static String substringBefore(String str, String separator) {
/* 8682 */     if (isEmpty(str) || separator == null) {
/* 8683 */       return str;
/*      */     }
/* 8685 */     if (separator.isEmpty()) {
/* 8686 */       return "";
/*      */     }
/* 8688 */     int pos = str.indexOf(separator);
/* 8689 */     if (pos == -1) {
/* 8690 */       return str;
/*      */     }
/* 8692 */     return str.substring(0, pos);
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
/*      */   public static String substringBeforeLast(String str, String separator) {
/* 8723 */     if (isEmpty(str) || isEmpty(separator)) {
/* 8724 */       return str;
/*      */     }
/* 8726 */     int pos = str.lastIndexOf(separator);
/* 8727 */     if (pos == -1) {
/* 8728 */       return str;
/*      */     }
/* 8730 */     return str.substring(0, pos);
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
/*      */   public static String substringBetween(String str, String tag) {
/* 8755 */     return substringBetween(str, tag, tag);
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
/*      */   public static String substringBetween(String str, String open, String close) {
/* 8786 */     if (!ObjectUtils.allNotNull(new Object[] { str, open, close })) {
/* 8787 */       return null;
/*      */     }
/* 8789 */     int start = str.indexOf(open);
/* 8790 */     if (start != -1) {
/* 8791 */       int end = str.indexOf(close, start + open.length());
/* 8792 */       if (end != -1) {
/* 8793 */         return str.substring(start + open.length(), end);
/*      */       }
/*      */     } 
/* 8796 */     return null;
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
/*      */   public static String[] substringsBetween(String str, String open, String close) {
/* 8822 */     if (str == null || isEmpty(open) || isEmpty(close)) {
/* 8823 */       return null;
/*      */     }
/* 8825 */     int strLen = str.length();
/* 8826 */     if (strLen == 0) {
/* 8827 */       return ArrayUtils.EMPTY_STRING_ARRAY;
/*      */     }
/* 8829 */     int closeLen = close.length();
/* 8830 */     int openLen = open.length();
/* 8831 */     List<String> list = new ArrayList<>();
/* 8832 */     int pos = 0;
/* 8833 */     while (pos < strLen - closeLen) {
/* 8834 */       int start = str.indexOf(open, pos);
/* 8835 */       if (start < 0) {
/*      */         break;
/*      */       }
/* 8838 */       start += openLen;
/* 8839 */       int end = str.indexOf(close, start);
/* 8840 */       if (end < 0) {
/*      */         break;
/*      */       }
/* 8843 */       list.add(str.substring(start, end));
/* 8844 */       pos = end + closeLen;
/*      */     } 
/* 8846 */     if (list.isEmpty()) {
/* 8847 */       return null;
/*      */     }
/* 8849 */     return list.<String>toArray(ArrayUtils.EMPTY_STRING_ARRAY);
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
/*      */   public static String swapCase(String str) {
/* 8880 */     if (isEmpty(str)) {
/* 8881 */       return str;
/*      */     }
/*      */     
/* 8884 */     int strLen = str.length();
/* 8885 */     int[] newCodePoints = new int[strLen];
/* 8886 */     int outOffset = 0; int i;
/* 8887 */     for (i = 0; i < strLen; ) {
/* 8888 */       int newCodePoint, oldCodepoint = str.codePointAt(i);
/*      */       
/* 8890 */       if (Character.isUpperCase(oldCodepoint) || Character.isTitleCase(oldCodepoint)) {
/* 8891 */         newCodePoint = Character.toLowerCase(oldCodepoint);
/* 8892 */       } else if (Character.isLowerCase(oldCodepoint)) {
/* 8893 */         newCodePoint = Character.toUpperCase(oldCodepoint);
/*      */       } else {
/* 8895 */         newCodePoint = oldCodepoint;
/*      */       } 
/* 8897 */       newCodePoints[outOffset++] = newCodePoint;
/* 8898 */       i += Character.charCount(newCodePoint);
/*      */     } 
/* 8900 */     return new String(newCodePoints, 0, outOffset);
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
/*      */   public static int[] toCodePoints(CharSequence cs) {
/* 8920 */     if (cs == null) {
/* 8921 */       return null;
/*      */     }
/* 8923 */     if (cs.length() == 0) {
/* 8924 */       return ArrayUtils.EMPTY_INT_ARRAY;
/*      */     }
/*      */     
/* 8927 */     String s = cs.toString();
/* 8928 */     int[] result = new int[s.codePointCount(0, s.length())];
/* 8929 */     int index = 0;
/* 8930 */     for (int i = 0; i < result.length; i++) {
/* 8931 */       result[i] = s.codePointAt(index);
/* 8932 */       index += Character.charCount(result[i]);
/*      */     } 
/* 8934 */     return result;
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
/*      */   public static String toEncodedString(byte[] bytes, Charset charset) {
/* 8951 */     return new String(bytes, Charsets.toCharset(charset));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toRootLowerCase(String source) {
/* 8962 */     return (source == null) ? null : source.toLowerCase(Locale.ROOT);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toRootUpperCase(String source) {
/* 8973 */     return (source == null) ? null : source.toUpperCase(Locale.ROOT);
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
/*      */   @Deprecated
/*      */   public static String toString(byte[] bytes, String charsetName) throws UnsupportedEncodingException {
/* 8993 */     return new String(bytes, Charsets.toCharset(charsetName));
/*      */   }
/*      */   
/*      */   private static String toStringOrEmpty(Object obj) {
/* 8997 */     return Objects.toString(obj, "");
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
/*      */   public static String trim(String str) {
/* 9024 */     return (str == null) ? null : str.trim();
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
/*      */   public static String trimToEmpty(String str) {
/* 9049 */     return (str == null) ? "" : str.trim();
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
/*      */   public static String trimToNull(String str) {
/* 9075 */     String ts = trim(str);
/* 9076 */     return isEmpty(ts) ? null : ts;
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
/*      */   public static String truncate(String str, int maxWidth) {
/* 9112 */     return truncate(str, 0, maxWidth);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String truncate(String str, int offset, int maxWidth) {
/* 9176 */     if (offset < 0) {
/* 9177 */       throw new IllegalArgumentException("offset cannot be negative");
/*      */     }
/* 9179 */     if (maxWidth < 0) {
/* 9180 */       throw new IllegalArgumentException("maxWith cannot be negative");
/*      */     }
/* 9182 */     if (str == null) {
/* 9183 */       return null;
/*      */     }
/* 9185 */     if (offset > str.length()) {
/* 9186 */       return "";
/*      */     }
/* 9188 */     if (str.length() > maxWidth) {
/* 9189 */       int ix = Math.min(offset + maxWidth, str.length());
/* 9190 */       return str.substring(offset, ix);
/*      */     } 
/* 9192 */     return str.substring(offset);
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
/*      */   public static String uncapitalize(String str) {
/* 9217 */     int strLen = length(str);
/* 9218 */     if (strLen == 0) {
/* 9219 */       return str;
/*      */     }
/*      */     
/* 9222 */     int firstCodePoint = str.codePointAt(0);
/* 9223 */     int newCodePoint = Character.toLowerCase(firstCodePoint);
/* 9224 */     if (firstCodePoint == newCodePoint)
/*      */     {
/* 9226 */       return str;
/*      */     }
/*      */     
/* 9229 */     int[] newCodePoints = new int[strLen];
/* 9230 */     int outOffset = 0;
/* 9231 */     newCodePoints[outOffset++] = newCodePoint; int inOffset;
/* 9232 */     for (inOffset = Character.charCount(firstCodePoint); inOffset < strLen; ) {
/* 9233 */       int codePoint = str.codePointAt(inOffset);
/* 9234 */       newCodePoints[outOffset++] = codePoint;
/* 9235 */       inOffset += Character.charCount(codePoint);
/*      */     } 
/* 9237 */     return new String(newCodePoints, 0, outOffset);
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
/*      */   public static String unwrap(String str, char wrapChar) {
/* 9265 */     if (isEmpty(str) || wrapChar == '\000' || str.length() == 1) {
/* 9266 */       return str;
/*      */     }
/*      */     
/* 9269 */     if (str.charAt(0) == wrapChar && str.charAt(str.length() - 1) == wrapChar) {
/* 9270 */       int startIndex = 0;
/* 9271 */       int endIndex = str.length() - 1;
/*      */       
/* 9273 */       return str.substring(1, endIndex);
/*      */     } 
/*      */     
/* 9276 */     return str;
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
/*      */   public static String unwrap(String str, String wrapToken) {
/* 9305 */     if (isEmpty(str) || isEmpty(wrapToken) || str.length() < 2 * wrapToken.length()) {
/* 9306 */       return str;
/*      */     }
/*      */     
/* 9309 */     if (startsWith(str, wrapToken) && endsWith(str, wrapToken)) {
/* 9310 */       return str.substring(wrapToken.length(), str.lastIndexOf(wrapToken));
/*      */     }
/*      */     
/* 9313 */     return str;
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
/*      */   public static String upperCase(String str) {
/* 9336 */     if (str == null) {
/* 9337 */       return null;
/*      */     }
/* 9339 */     return str.toUpperCase();
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
/*      */   public static String upperCase(String str, Locale locale) {
/* 9359 */     if (str == null) {
/* 9360 */       return null;
/*      */     }
/* 9362 */     return str.toUpperCase(LocaleUtils.toLocale(locale));
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
/*      */   public static String valueOf(char[] value) {
/* 9374 */     return (value == null) ? null : String.valueOf(value);
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
/*      */   public static String wrap(String str, char wrapWith) {
/* 9398 */     if (isEmpty(str) || wrapWith == '\000') {
/* 9399 */       return str;
/*      */     }
/*      */     
/* 9402 */     return wrapWith + str + wrapWith;
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
/*      */   public static String wrap(String str, String wrapWith) {
/* 9434 */     if (isEmpty(str) || isEmpty(wrapWith)) {
/* 9435 */       return str;
/*      */     }
/*      */     
/* 9438 */     return wrapWith.concat(str).concat(wrapWith);
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
/*      */   public static String wrapIfMissing(String str, char wrapWith) {
/* 9467 */     if (isEmpty(str) || wrapWith == '\000') {
/* 9468 */       return str;
/*      */     }
/* 9470 */     boolean wrapStart = (str.charAt(0) != wrapWith);
/* 9471 */     boolean wrapEnd = (str.charAt(str.length() - 1) != wrapWith);
/* 9472 */     if (!wrapStart && !wrapEnd) {
/* 9473 */       return str;
/*      */     }
/*      */     
/* 9476 */     StringBuilder builder = new StringBuilder(str.length() + 2);
/* 9477 */     if (wrapStart) {
/* 9478 */       builder.append(wrapWith);
/*      */     }
/* 9480 */     builder.append(str);
/* 9481 */     if (wrapEnd) {
/* 9482 */       builder.append(wrapWith);
/*      */     }
/* 9484 */     return builder.toString();
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
/*      */   public static String wrapIfMissing(String str, String wrapWith) {
/* 9517 */     if (isEmpty(str) || isEmpty(wrapWith)) {
/* 9518 */       return str;
/*      */     }
/*      */     
/* 9521 */     boolean wrapStart = !str.startsWith(wrapWith);
/* 9522 */     boolean wrapEnd = !str.endsWith(wrapWith);
/* 9523 */     if (!wrapStart && !wrapEnd) {
/* 9524 */       return str;
/*      */     }
/*      */     
/* 9527 */     StringBuilder builder = new StringBuilder(str.length() + wrapWith.length() + wrapWith.length());
/* 9528 */     if (wrapStart) {
/* 9529 */       builder.append(wrapWith);
/*      */     }
/* 9531 */     builder.append(str);
/* 9532 */     if (wrapEnd) {
/* 9533 */       builder.append(wrapWith);
/*      */     }
/* 9535 */     return builder.toString();
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\StringUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */