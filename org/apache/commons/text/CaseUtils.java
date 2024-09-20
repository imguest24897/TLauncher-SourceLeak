/*     */ package org.apache.commons.text;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ public class CaseUtils
/*     */ {
/*     */   public static String toCamelCase(String str, boolean capitalizeFirstLetter, char... delimiters) {
/*  70 */     if (StringUtils.isEmpty(str)) {
/*  71 */       return str;
/*     */     }
/*  73 */     str = str.toLowerCase();
/*  74 */     int strLen = str.length();
/*  75 */     int[] newCodePoints = new int[strLen];
/*  76 */     int outOffset = 0;
/*  77 */     Set<Integer> delimiterSet = toDelimiterSet(delimiters);
/*  78 */     boolean capitalizeNext = capitalizeFirstLetter; int index;
/*  79 */     for (index = 0; index < strLen; ) {
/*  80 */       int codePoint = str.codePointAt(index);
/*     */       
/*  82 */       if (delimiterSet.contains(Integer.valueOf(codePoint))) {
/*  83 */         capitalizeNext = (outOffset != 0);
/*  84 */         index += Character.charCount(codePoint); continue;
/*  85 */       }  if (capitalizeNext || (outOffset == 0 && capitalizeFirstLetter)) {
/*  86 */         int titleCaseCodePoint = Character.toTitleCase(codePoint);
/*  87 */         newCodePoints[outOffset++] = titleCaseCodePoint;
/*  88 */         index += Character.charCount(titleCaseCodePoint);
/*  89 */         capitalizeNext = false; continue;
/*     */       } 
/*  91 */       newCodePoints[outOffset++] = codePoint;
/*  92 */       index += Character.charCount(codePoint);
/*     */     } 
/*     */ 
/*     */     
/*  96 */     return new String(newCodePoints, 0, outOffset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Set<Integer> toDelimiterSet(char[] delimiters) {
/* 107 */     Set<Integer> delimiterHashSet = new HashSet<>();
/* 108 */     delimiterHashSet.add(Integer.valueOf(Character.codePointAt(new char[] { ' ' }, 0)));
/* 109 */     if (ArrayUtils.isEmpty(delimiters)) {
/* 110 */       return delimiterHashSet;
/*     */     }
/*     */     
/* 113 */     for (int index = 0; index < delimiters.length; index++) {
/* 114 */       delimiterHashSet.add(Integer.valueOf(Character.codePointAt(delimiters, index)));
/*     */     }
/* 116 */     return delimiterHashSet;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\CaseUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */