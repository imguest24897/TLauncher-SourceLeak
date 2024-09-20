/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CharSequenceUtils
/*     */ {
/*     */   private static final int NOT_FOUND = -1;
/*     */   static final int TO_STRING_LIMIT = 16;
/*     */   
/*     */   private static boolean checkLaterThan1(CharSequence cs, CharSequence searchChar, int len2, int start1) {
/*  33 */     for (int i = 1, j = len2 - 1; i <= j; i++, j--) {
/*  34 */       if (cs.charAt(start1 + i) != searchChar.charAt(i) || cs.charAt(start1 + j) != searchChar.charAt(j)) {
/*  35 */         return false;
/*     */       }
/*     */     } 
/*  38 */     return true;
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
/*     */   static int indexOf(CharSequence cs, CharSequence searchChar, int start) {
/*  50 */     if (cs instanceof String) {
/*  51 */       return ((String)cs).indexOf(searchChar.toString(), start);
/*     */     }
/*  53 */     if (cs instanceof StringBuilder) {
/*  54 */       return ((StringBuilder)cs).indexOf(searchChar.toString(), start);
/*     */     }
/*  56 */     if (cs instanceof StringBuffer) {
/*  57 */       return ((StringBuffer)cs).indexOf(searchChar.toString(), start);
/*     */     }
/*  59 */     return cs.toString().indexOf(searchChar.toString(), start);
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
/*     */   static int indexOf(CharSequence cs, int searchChar, int start) {
/* 113 */     if (cs instanceof String) {
/* 114 */       return ((String)cs).indexOf(searchChar, start);
/*     */     }
/* 116 */     int sz = cs.length();
/* 117 */     if (start < 0) {
/* 118 */       start = 0;
/*     */     }
/* 120 */     if (searchChar < 65536) {
/* 121 */       for (int i = start; i < sz; i++) {
/* 122 */         if (cs.charAt(i) == searchChar) {
/* 123 */           return i;
/*     */         }
/*     */       } 
/* 126 */       return -1;
/*     */     } 
/*     */     
/* 129 */     if (searchChar <= 1114111) {
/* 130 */       char[] chars = Character.toChars(searchChar);
/* 131 */       for (int i = start; i < sz - 1; i++) {
/* 132 */         char high = cs.charAt(i);
/* 133 */         char low = cs.charAt(i + 1);
/* 134 */         if (high == chars[0] && low == chars[1]) {
/* 135 */           return i;
/*     */         }
/*     */       } 
/*     */     } 
/* 139 */     return -1;
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
/*     */   static int lastIndexOf(CharSequence cs, CharSequence searchChar, int start) {
/* 151 */     if (searchChar == null || cs == null) {
/* 152 */       return -1;
/*     */     }
/* 154 */     if (searchChar instanceof String) {
/* 155 */       if (cs instanceof String) {
/* 156 */         return ((String)cs).lastIndexOf((String)searchChar, start);
/*     */       }
/* 158 */       if (cs instanceof StringBuilder) {
/* 159 */         return ((StringBuilder)cs).lastIndexOf((String)searchChar, start);
/*     */       }
/* 161 */       if (cs instanceof StringBuffer) {
/* 162 */         return ((StringBuffer)cs).lastIndexOf((String)searchChar, start);
/*     */       }
/*     */     } 
/*     */     
/* 166 */     int len1 = cs.length();
/* 167 */     int len2 = searchChar.length();
/*     */     
/* 169 */     if (start > len1) {
/* 170 */       start = len1;
/*     */     }
/*     */     
/* 173 */     if (start < 0 || len2 > len1) {
/* 174 */       return -1;
/*     */     }
/*     */     
/* 177 */     if (len2 == 0) {
/* 178 */       return start;
/*     */     }
/*     */     
/* 181 */     if (len2 <= 16) {
/* 182 */       if (cs instanceof String) {
/* 183 */         return ((String)cs).lastIndexOf(searchChar.toString(), start);
/*     */       }
/* 185 */       if (cs instanceof StringBuilder) {
/* 186 */         return ((StringBuilder)cs).lastIndexOf(searchChar.toString(), start);
/*     */       }
/* 188 */       if (cs instanceof StringBuffer) {
/* 189 */         return ((StringBuffer)cs).lastIndexOf(searchChar.toString(), start);
/*     */       }
/*     */     } 
/*     */     
/* 193 */     if (start + len2 > len1) {
/* 194 */       start = len1 - len2;
/*     */     }
/*     */     
/* 197 */     char char0 = searchChar.charAt(0);
/*     */     
/* 199 */     int i = start;
/*     */     while (true) {
/* 201 */       while (cs.charAt(i) != char0) {
/* 202 */         i--;
/* 203 */         if (i < 0) {
/* 204 */           return -1;
/*     */         }
/*     */       } 
/* 207 */       if (checkLaterThan1(cs, searchChar, len2, i)) {
/* 208 */         return i;
/*     */       }
/* 210 */       i--;
/* 211 */       if (i < 0) {
/* 212 */         return -1;
/*     */       }
/*     */     } 
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
/*     */   static int lastIndexOf(CharSequence cs, int searchChar, int start) {
/* 246 */     if (cs instanceof String) {
/* 247 */       return ((String)cs).lastIndexOf(searchChar, start);
/*     */     }
/* 249 */     int sz = cs.length();
/* 250 */     if (start < 0) {
/* 251 */       return -1;
/*     */     }
/* 253 */     if (start >= sz) {
/* 254 */       start = sz - 1;
/*     */     }
/* 256 */     if (searchChar < 65536) {
/* 257 */       for (int i = start; i >= 0; i--) {
/* 258 */         if (cs.charAt(i) == searchChar) {
/* 259 */           return i;
/*     */         }
/*     */       } 
/* 262 */       return -1;
/*     */     } 
/*     */ 
/*     */     
/* 266 */     if (searchChar <= 1114111) {
/* 267 */       char[] chars = Character.toChars(searchChar);
/*     */       
/* 269 */       if (start == sz - 1) {
/* 270 */         return -1;
/*     */       }
/* 272 */       for (int i = start; i >= 0; i--) {
/* 273 */         char high = cs.charAt(i);
/* 274 */         char low = cs.charAt(i + 1);
/* 275 */         if (chars[0] == high && chars[1] == low) {
/* 276 */           return i;
/*     */         }
/*     */       } 
/*     */     } 
/* 280 */     return -1;
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
/*     */   static boolean regionMatches(CharSequence cs, boolean ignoreCase, int thisStart, CharSequence substring, int start, int length) {
/* 296 */     if (cs instanceof String && substring instanceof String) {
/* 297 */       return ((String)cs).regionMatches(ignoreCase, thisStart, (String)substring, start, length);
/*     */     }
/* 299 */     int index1 = thisStart;
/* 300 */     int index2 = start;
/* 301 */     int tmpLen = length;
/*     */ 
/*     */     
/* 304 */     int srcLen = cs.length() - thisStart;
/* 305 */     int otherLen = substring.length() - start;
/*     */ 
/*     */     
/* 308 */     if (thisStart < 0 || start < 0 || length < 0) {
/* 309 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 313 */     if (srcLen < length || otherLen < length) {
/* 314 */       return false;
/*     */     }
/*     */     
/* 317 */     while (tmpLen-- > 0) {
/* 318 */       char c1 = cs.charAt(index1++);
/* 319 */       char c2 = substring.charAt(index2++);
/*     */       
/* 321 */       if (c1 == c2) {
/*     */         continue;
/*     */       }
/*     */       
/* 325 */       if (!ignoreCase) {
/* 326 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 330 */       char u1 = Character.toUpperCase(c1);
/* 331 */       char u2 = Character.toUpperCase(c2);
/* 332 */       if (u1 != u2 && Character.toLowerCase(u1) != Character.toLowerCase(u2)) {
/* 333 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 337 */     return true;
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
/*     */   public static CharSequence subSequence(CharSequence cs, int start) {
/* 355 */     return (cs == null) ? null : cs.subSequence(start, cs.length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char[] toCharArray(CharSequence source) {
/* 366 */     int len = StringUtils.length(source);
/* 367 */     if (len == 0) {
/* 368 */       return ArrayUtils.EMPTY_CHAR_ARRAY;
/*     */     }
/* 370 */     if (source instanceof String) {
/* 371 */       return ((String)source).toCharArray();
/*     */     }
/* 373 */     char[] array = new char[len];
/* 374 */     for (int i = 0; i < len; i++) {
/* 375 */       array[i] = source.charAt(i);
/*     */     }
/* 377 */     return array;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\CharSequenceUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */