/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import org.apache.commons.lang3.stream.Streams;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CharSetUtils
/*     */ {
/*     */   public static boolean containsAny(String str, String... set) {
/*  54 */     if (StringUtils.isEmpty(str) || deepEmpty(set)) {
/*  55 */       return false;
/*     */     }
/*  57 */     CharSet chars = CharSet.getInstance(set);
/*  58 */     for (char c : str.toCharArray()) {
/*  59 */       if (chars.contains(c)) {
/*  60 */         return true;
/*     */       }
/*     */     } 
/*  63 */     return false;
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
/*     */   public static int count(String str, String... set) {
/*  85 */     if (StringUtils.isEmpty(str) || deepEmpty(set)) {
/*  86 */       return 0;
/*     */     }
/*  88 */     CharSet chars = CharSet.getInstance(set);
/*  89 */     int count = 0;
/*  90 */     for (char c : str.toCharArray()) {
/*  91 */       if (chars.contains(c)) {
/*  92 */         count++;
/*     */       }
/*     */     } 
/*  95 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean deepEmpty(String[] strings) {
/* 106 */     return Streams.of((Object[])strings).allMatch(StringUtils::isEmpty);
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
/*     */   public static String delete(String str, String... set) {
/* 128 */     if (StringUtils.isEmpty(str) || deepEmpty(set)) {
/* 129 */       return str;
/*     */     }
/* 131 */     return modify(str, set, false);
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
/*     */   public static String keep(String str, String... set) {
/* 154 */     if (str == null) {
/* 155 */       return null;
/*     */     }
/* 157 */     if (str.isEmpty() || deepEmpty(set)) {
/* 158 */       return "";
/*     */     }
/* 160 */     return modify(str, set, true);
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
/*     */   private static String modify(String str, String[] set, boolean expect) {
/* 172 */     CharSet chars = CharSet.getInstance(set);
/* 173 */     StringBuilder buffer = new StringBuilder(str.length());
/* 174 */     char[] chrs = str.toCharArray();
/* 175 */     for (char chr : chrs) {
/* 176 */       if (chars.contains(chr) == expect) {
/* 177 */         buffer.append(chr);
/*     */       }
/*     */     } 
/* 180 */     return buffer.toString();
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
/*     */   public static String squeeze(String str, String... set) {
/* 202 */     if (StringUtils.isEmpty(str) || deepEmpty(set)) {
/* 203 */       return str;
/*     */     }
/* 205 */     CharSet chars = CharSet.getInstance(set);
/* 206 */     StringBuilder buffer = new StringBuilder(str.length());
/* 207 */     char[] chrs = str.toCharArray();
/* 208 */     int sz = chrs.length;
/* 209 */     char lastChar = chrs[0];
/*     */     
/* 211 */     Character inChars = null;
/* 212 */     Character notInChars = null;
/* 213 */     buffer.append(lastChar);
/* 214 */     int i = 1; while (true) { char ch; if (i < sz)
/* 215 */       { ch = chrs[i];
/* 216 */         if (ch == lastChar)
/* 217 */         { if (inChars != null && ch == inChars.charValue()) {
/*     */             continue;
/*     */           }
/* 220 */           if (notInChars == null || ch != notInChars.charValue())
/* 221 */           { if (chars.contains(ch))
/* 222 */             { inChars = Character.valueOf(ch); }
/*     */             else
/*     */             
/* 225 */             { notInChars = Character.valueOf(ch);
/*     */ 
/*     */               
/* 228 */               buffer.append(ch);
/* 229 */               lastChar = ch; }  continue; }  }  } else { break; }  buffer.append(ch); lastChar = ch; i++; }
/*     */     
/* 231 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\CharSetUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */