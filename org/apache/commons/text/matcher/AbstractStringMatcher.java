/*     */ package org.apache.commons.text.matcher;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractStringMatcher
/*     */   implements StringMatcher
/*     */ {
/*     */   static final class AndStringMatcher
/*     */     extends AbstractStringMatcher
/*     */   {
/*     */     private final StringMatcher[] stringMatchers;
/*     */     
/*     */     AndStringMatcher(StringMatcher... stringMatchers) {
/*  51 */       this.stringMatchers = (StringMatcher[])stringMatchers.clone();
/*     */     }
/*     */ 
/*     */     
/*     */     public int isMatch(char[] buffer, int start, int bufferStart, int bufferEnd) {
/*  56 */       int total = 0;
/*  57 */       int curStart = start;
/*  58 */       for (StringMatcher stringMatcher : this.stringMatchers) {
/*  59 */         if (stringMatcher != null) {
/*  60 */           int len = stringMatcher.isMatch(buffer, curStart, bufferStart, bufferEnd);
/*  61 */           if (len == 0) {
/*  62 */             return 0;
/*     */           }
/*  64 */           total += len;
/*  65 */           curStart += len;
/*     */         } 
/*     */       } 
/*  68 */       return total;
/*     */     }
/*     */ 
/*     */     
/*     */     public int isMatch(CharSequence buffer, int start, int bufferStart, int bufferEnd) {
/*  73 */       int total = 0;
/*  74 */       int curStart = start;
/*  75 */       for (StringMatcher stringMatcher : this.stringMatchers) {
/*  76 */         if (stringMatcher != null) {
/*  77 */           int len = stringMatcher.isMatch(buffer, curStart, bufferStart, bufferEnd);
/*  78 */           if (len == 0) {
/*  79 */             return 0;
/*     */           }
/*  81 */           total += len;
/*  82 */           curStart += len;
/*     */         } 
/*     */       } 
/*  85 */       return total;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/*  90 */       int total = 0;
/*  91 */       for (StringMatcher stringMatcher : this.stringMatchers) {
/*  92 */         if (stringMatcher != null) {
/*  93 */           total += stringMatcher.size();
/*     */         }
/*     */       } 
/*  96 */       return total;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class CharArrayMatcher
/*     */     extends AbstractStringMatcher
/*     */   {
/*     */     private final char[] chars;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String string;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     CharArrayMatcher(char... chars) {
/* 120 */       this.string = String.valueOf(chars);
/* 121 */       this.chars = (char[])chars.clone();
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
/*     */ 
/*     */     
/*     */     public int isMatch(char[] buffer, int start, int bufferStart, int bufferEnd) {
/* 135 */       int len = size();
/* 136 */       if (start + len > bufferEnd) {
/* 137 */         return 0;
/*     */       }
/* 139 */       int j = start;
/* 140 */       for (int i = 0; i < len; i++, j++) {
/* 141 */         if (this.chars[i] != buffer[j]) {
/* 142 */           return 0;
/*     */         }
/*     */       } 
/* 145 */       return len;
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
/*     */ 
/*     */     
/*     */     public int isMatch(CharSequence buffer, int start, int bufferStart, int bufferEnd) {
/* 159 */       int len = size();
/* 160 */       if (start + len > bufferEnd) {
/* 161 */         return 0;
/*     */       }
/* 163 */       int j = start;
/* 164 */       for (int i = 0; i < len; i++, j++) {
/* 165 */         if (this.chars[i] != buffer.charAt(j)) {
/* 166 */           return 0;
/*     */         }
/*     */       } 
/* 169 */       return len;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 179 */       return this.chars.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 184 */       return super.toString() + "[\"" + this.string + "\"]";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class CharMatcher
/*     */     extends AbstractStringMatcher
/*     */   {
/*     */     private final char ch;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     CharMatcher(char ch) {
/* 206 */       this.ch = ch;
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
/*     */ 
/*     */     
/*     */     public int isMatch(char[] buffer, int start, int bufferStart, int bufferEnd) {
/* 220 */       return (this.ch == buffer[start]) ? 1 : 0;
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
/*     */ 
/*     */     
/*     */     public int isMatch(CharSequence buffer, int start, int bufferStart, int bufferEnd) {
/* 234 */       return (this.ch == buffer.charAt(start)) ? 1 : 0;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 244 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 249 */       return super.toString() + "['" + this.ch + "']";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class CharSetMatcher
/*     */     extends AbstractStringMatcher
/*     */   {
/*     */     private final char[] chars;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     CharSetMatcher(char[] chars) {
/* 270 */       this.chars = (char[])chars.clone();
/* 271 */       Arrays.sort(this.chars);
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
/*     */ 
/*     */     
/*     */     public int isMatch(char[] buffer, int start, int bufferStart, int bufferEnd) {
/* 285 */       return (Arrays.binarySearch(this.chars, buffer[start]) >= 0) ? 1 : 0;
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
/*     */ 
/*     */     
/*     */     public int isMatch(CharSequence buffer, int start, int bufferStart, int bufferEnd) {
/* 299 */       return (Arrays.binarySearch(this.chars, buffer.charAt(start)) >= 0) ? 1 : 0;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 309 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 314 */       return super.toString() + Arrays.toString(this.chars);
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
/*     */   static final class NoneMatcher
/*     */     extends AbstractStringMatcher
/*     */   {
/*     */     public int isMatch(char[] buffer, int start, int bufferStart, int bufferEnd) {
/* 344 */       return 0;
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
/*     */ 
/*     */     
/*     */     public int isMatch(CharSequence buffer, int start, int bufferStart, int bufferEnd) {
/* 358 */       return 0;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 368 */       return 0;
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
/*     */   static final class TrimMatcher
/*     */     extends AbstractStringMatcher
/*     */   {
/*     */     private static final int SPACE_INT = 32;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int isMatch(char[] buffer, int start, int bufferStart, int bufferEnd) {
/* 403 */       return (buffer[start] <= ' ') ? 1 : 0;
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
/*     */ 
/*     */     
/*     */     public int isMatch(CharSequence buffer, int start, int bufferStart, int bufferEnd) {
/* 417 */       return (buffer.charAt(start) <= ' ') ? 1 : 0;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 427 */       return 1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\matcher\AbstractStringMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */