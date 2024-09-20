/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.util.Random;
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RandomStringUtils
/*     */ {
/*     */   private static ThreadLocalRandom random() {
/*  53 */     return ThreadLocalRandom.current();
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
/*     */   public static String random(int count) {
/*  68 */     return random(count, false, false);
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
/*     */   public static String random(int count, boolean letters, boolean numbers) {
/*  87 */     return random(count, 0, 0, letters, numbers);
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
/*     */   public static String random(int count, char... chars) {
/* 103 */     if (chars == null) {
/* 104 */       return random(count, 0, 0, false, false, null, random());
/*     */     }
/* 106 */     return random(count, 0, chars.length, false, false, chars, random());
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
/*     */   public static String random(int count, int start, int end, boolean letters, boolean numbers) {
/* 127 */     return random(count, start, end, letters, numbers, null, random());
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
/*     */   public static String random(int count, int start, int end, boolean letters, boolean numbers, char... chars) {
/* 154 */     return random(count, start, end, letters, numbers, chars, random());
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
/*     */   public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random) {
/* 194 */     if (count == 0) {
/* 195 */       return "";
/*     */     }
/* 197 */     if (count < 0) {
/* 198 */       throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
/*     */     }
/* 200 */     if (chars != null && chars.length == 0) {
/* 201 */       throw new IllegalArgumentException("The chars array must not be empty");
/*     */     }
/*     */     
/* 204 */     if (start == 0 && end == 0) {
/* 205 */       if (chars != null) {
/* 206 */         end = chars.length;
/* 207 */       } else if (!letters && !numbers) {
/* 208 */         end = 1114111;
/*     */       } else {
/* 210 */         end = 123;
/* 211 */         start = 32;
/*     */       } 
/* 213 */     } else if (end <= start) {
/* 214 */       throw new IllegalArgumentException("Parameter end (" + end + ") must be greater than start (" + start + ")");
/*     */     } 
/*     */     
/* 217 */     int zeroDigitAscii = 48;
/* 218 */     int firstLetterAscii = 65;
/*     */     
/* 220 */     if (chars == null && ((numbers && end <= 48) || (letters && end <= 65)))
/*     */     {
/* 222 */       throw new IllegalArgumentException("Parameter end (" + end + ") must be greater then (" + '0' + ") for generating digits or greater then (" + 'A' + ") for generating letters.");
/*     */     }
/*     */ 
/*     */     
/* 226 */     StringBuilder builder = new StringBuilder(count);
/* 227 */     int gap = end - start;
/*     */     
/* 229 */     while (count-- != 0) {
/*     */       int codePoint;
/* 231 */       if (chars == null) {
/* 232 */         codePoint = random.nextInt(gap) + start;
/*     */         
/* 234 */         switch (Character.getType(codePoint)) {
/*     */           case 0:
/*     */           case 18:
/*     */           case 19:
/* 238 */             count++;
/*     */             continue;
/*     */         } 
/*     */       
/*     */       } else {
/* 243 */         codePoint = chars[random.nextInt(gap) + start];
/*     */       } 
/*     */       
/* 246 */       int numberOfChars = Character.charCount(codePoint);
/* 247 */       if (count == 0 && numberOfChars > 1) {
/* 248 */         count++;
/*     */         
/*     */         continue;
/*     */       } 
/* 252 */       if ((letters && Character.isLetter(codePoint)) || (numbers && 
/* 253 */         Character.isDigit(codePoint)) || (!letters && !numbers)) {
/*     */         
/* 255 */         builder.appendCodePoint(codePoint);
/*     */         
/* 257 */         if (numberOfChars == 2) {
/* 258 */           count--;
/*     */         }
/*     */         continue;
/*     */       } 
/* 262 */       count++;
/*     */     } 
/*     */     
/* 265 */     return builder.toString();
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
/*     */   public static String random(int count, String chars) {
/* 283 */     if (chars == null) {
/* 284 */       return random(count, 0, 0, false, false, null, random());
/*     */     }
/* 286 */     return random(count, chars.toCharArray());
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
/*     */   public static String randomAlphabetic(int count) {
/* 301 */     return random(count, true, false);
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
/*     */   public static String randomAlphabetic(int minLengthInclusive, int maxLengthExclusive) {
/* 316 */     return randomAlphabetic(RandomUtils.nextInt(minLengthInclusive, maxLengthExclusive));
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
/*     */   public static String randomAlphanumeric(int count) {
/* 331 */     return random(count, true, true);
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
/*     */   public static String randomAlphanumeric(int minLengthInclusive, int maxLengthExclusive) {
/* 347 */     return randomAlphanumeric(RandomUtils.nextInt(minLengthInclusive, maxLengthExclusive));
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
/*     */   public static String randomAscii(int count) {
/* 362 */     return random(count, 32, 127, false, false);
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
/*     */   public static String randomAscii(int minLengthInclusive, int maxLengthExclusive) {
/* 378 */     return randomAscii(RandomUtils.nextInt(minLengthInclusive, maxLengthExclusive));
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
/*     */   public static String randomGraph(int count) {
/* 394 */     return random(count, 33, 126, false, false);
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
/*     */   public static String randomGraph(int minLengthInclusive, int maxLengthExclusive) {
/* 409 */     return randomGraph(RandomUtils.nextInt(minLengthInclusive, maxLengthExclusive));
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
/*     */   public static String randomNumeric(int count) {
/* 424 */     return random(count, false, true);
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
/*     */   public static String randomNumeric(int minLengthInclusive, int maxLengthExclusive) {
/* 439 */     return randomNumeric(RandomUtils.nextInt(minLengthInclusive, maxLengthExclusive));
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
/*     */   public static String randomPrint(int count) {
/* 455 */     return random(count, 32, 126, false, false);
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
/*     */   public static String randomPrint(int minLengthInclusive, int maxLengthExclusive) {
/* 471 */     return randomPrint(RandomUtils.nextInt(minLengthInclusive, maxLengthExclusive));
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\RandomStringUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */