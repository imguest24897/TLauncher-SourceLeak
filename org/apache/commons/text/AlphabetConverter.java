/*     */ package org.apache.commons.text;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class AlphabetConverter
/*     */ {
/*     */   private static final String ARROW = " -> ";
/*     */   private final Map<Integer, String> originalToEncoded;
/*     */   private final Map<String, String> encodedToOriginal;
/*     */   private final int encodedLetterLength;
/*     */   
/*     */   private static String codePointToString(int i) {
/*  90 */     if (Character.charCount(i) == 1) {
/*  91 */       return String.valueOf((char)i);
/*     */     }
/*  93 */     return new String(Character.toChars(i));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Integer[] convertCharsToIntegers(Character[] chars) {
/* 103 */     if (ArrayUtils.isEmpty((Object[])chars)) {
/* 104 */       return ArrayUtils.EMPTY_INTEGER_OBJECT_ARRAY;
/*     */     }
/* 106 */     Integer[] integers = new Integer[chars.length];
/* 107 */     Arrays.setAll(integers, i -> Integer.valueOf(chars[i].charValue()));
/* 108 */     return integers;
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
/*     */   public static AlphabetConverter createConverter(Integer[] original, Integer[] encoding, Integer[] doNotEncode) {
/* 133 */     Set<Integer> originalCopy = new LinkedHashSet<>(Arrays.asList(original));
/* 134 */     Set<Integer> encodingCopy = new LinkedHashSet<>(Arrays.asList(encoding));
/* 135 */     Set<Integer> doNotEncodeCopy = new LinkedHashSet<>(Arrays.asList(doNotEncode));
/*     */     
/* 137 */     Map<Integer, String> originalToEncoded = new LinkedHashMap<>();
/* 138 */     Map<String, String> encodedToOriginal = new LinkedHashMap<>();
/* 139 */     Map<Integer, String> doNotEncodeMap = new HashMap<>();
/*     */ 
/*     */ 
/*     */     
/* 143 */     for (Iterator<Integer> iterator = doNotEncodeCopy.iterator(); iterator.hasNext(); ) { int i = ((Integer)iterator.next()).intValue();
/* 144 */       if (!originalCopy.contains(Integer.valueOf(i))) {
/* 145 */         throw new IllegalArgumentException("Can not use 'do not encode' list because original alphabet does not contain '" + 
/*     */ 
/*     */             
/* 148 */             codePointToString(i) + "'");
/*     */       }
/*     */       
/* 151 */       if (!encodingCopy.contains(Integer.valueOf(i))) {
/* 152 */         throw new IllegalArgumentException("Can not use 'do not encode' list because encoding alphabet does not contain '" + 
/*     */             
/* 154 */             codePointToString(i) + "'");
/*     */       }
/*     */       
/* 157 */       doNotEncodeMap.put(Integer.valueOf(i), codePointToString(i)); }
/*     */ 
/*     */     
/* 160 */     if (encodingCopy.size() >= originalCopy.size()) {
/* 161 */       int i = 1;
/*     */       
/* 163 */       Iterator<Integer> it = encodingCopy.iterator();
/*     */       
/* 165 */       for (Iterator<Integer> iterator1 = originalCopy.iterator(); iterator1.hasNext(); ) { int originalLetter = ((Integer)iterator1.next()).intValue();
/* 166 */         String originalLetterAsString = codePointToString(originalLetter);
/*     */         
/* 168 */         if (doNotEncodeMap.containsKey(Integer.valueOf(originalLetter))) {
/* 169 */           originalToEncoded.put(Integer.valueOf(originalLetter), originalLetterAsString);
/* 170 */           encodedToOriginal.put(originalLetterAsString, originalLetterAsString); continue;
/*     */         } 
/* 172 */         Integer next = it.next();
/*     */         
/* 174 */         while (doNotEncodeCopy.contains(next)) {
/* 175 */           next = it.next();
/*     */         }
/*     */         
/* 178 */         String encodedLetter = codePointToString(next.intValue());
/*     */         
/* 180 */         originalToEncoded.put(Integer.valueOf(originalLetter), encodedLetter);
/* 181 */         encodedToOriginal.put(encodedLetter, originalLetterAsString); }
/*     */ 
/*     */ 
/*     */       
/* 185 */       return new AlphabetConverter(originalToEncoded, encodedToOriginal, i);
/*     */     } 
/*     */     
/* 188 */     if (encodingCopy.size() - doNotEncodeCopy.size() < 2) {
/* 189 */       throw new IllegalArgumentException("Must have at least two encoding characters (excluding those in the 'do not encode' list), but has " + (encodingCopy
/*     */ 
/*     */           
/* 192 */           .size() - doNotEncodeCopy.size()));
/*     */     }
/*     */ 
/*     */     
/* 196 */     int lettersSoFar = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 201 */     int lettersLeft = (originalCopy.size() - doNotEncodeCopy.size()) / (encodingCopy.size() - doNotEncodeCopy.size());
/*     */     
/* 203 */     while (lettersLeft / encodingCopy.size() >= 1) {
/* 204 */       lettersLeft /= encodingCopy.size();
/* 205 */       lettersSoFar++;
/*     */     } 
/*     */     
/* 208 */     int encodedLetterLength = lettersSoFar + 1;
/*     */     
/* 210 */     AlphabetConverter ac = new AlphabetConverter(originalToEncoded, encodedToOriginal, encodedLetterLength);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 215 */     ac.addSingleEncoding(encodedLetterLength, "", encodingCopy, originalCopy
/*     */ 
/*     */         
/* 218 */         .iterator(), doNotEncodeMap);
/*     */ 
/*     */     
/* 221 */     return ac;
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
/*     */   public static AlphabetConverter createConverterFromChars(Character[] original, Character[] encoding, Character[] doNotEncode) {
/* 245 */     return createConverter(
/* 246 */         convertCharsToIntegers(original), 
/* 247 */         convertCharsToIntegers(encoding), 
/* 248 */         convertCharsToIntegers(doNotEncode));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AlphabetConverter createConverterFromMap(Map<Integer, String> originalToEncoded) {
/* 259 */     Map<Integer, String> unmodifiableOriginalToEncoded = Collections.unmodifiableMap(originalToEncoded);
/* 260 */     Map<String, String> encodedToOriginal = new LinkedHashMap<>();
/*     */     
/* 262 */     int encodedLetterLength = 1;
/*     */     
/* 264 */     for (Map.Entry<Integer, String> e : unmodifiableOriginalToEncoded.entrySet()) {
/* 265 */       encodedToOriginal.put(e.getValue(), codePointToString(((Integer)e.getKey()).intValue()));
/*     */       
/* 267 */       if (((String)e.getValue()).length() > encodedLetterLength) {
/* 268 */         encodedLetterLength = ((String)e.getValue()).length();
/*     */       }
/*     */     } 
/*     */     
/* 272 */     return new AlphabetConverter(unmodifiableOriginalToEncoded, encodedToOriginal, encodedLetterLength);
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
/*     */   private AlphabetConverter(Map<Integer, String> originalToEncoded, Map<String, String> encodedToOriginal, int encodedLetterLength) {
/* 301 */     this.originalToEncoded = originalToEncoded;
/* 302 */     this.encodedToOriginal = encodedToOriginal;
/* 303 */     this.encodedLetterLength = encodedLetterLength;
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
/*     */   private void addSingleEncoding(int level, String currentEncoding, Collection<Integer> encoding, Iterator<Integer> originals, Map<Integer, String> doNotEncodeMap) {
/* 321 */     if (level > 0) {
/* 322 */       for (Iterator<Integer> iterator = encoding.iterator(); iterator.hasNext(); ) { int encodingLetter = ((Integer)iterator.next()).intValue();
/* 323 */         if (!originals.hasNext()) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 328 */         if (level != this.encodedLetterLength || 
/* 329 */           !doNotEncodeMap.containsKey(Integer.valueOf(encodingLetter))) {
/* 330 */           addSingleEncoding(level - 1, currentEncoding + 
/*     */               
/* 332 */               codePointToString(encodingLetter), encoding, originals, doNotEncodeMap);
/*     */         
/*     */         } }
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 340 */       Integer next = originals.next();
/*     */       
/* 342 */       while (doNotEncodeMap.containsKey(next)) {
/* 343 */         String str = codePointToString(next.intValue());
/*     */         
/* 345 */         this.originalToEncoded.put(next, str);
/* 346 */         this.encodedToOriginal.put(str, str);
/*     */ 
/*     */         
/* 349 */         if (!originals.hasNext()) {
/*     */           return;
/*     */         }
/*     */         
/* 353 */         next = originals.next();
/*     */       } 
/*     */       
/* 356 */       String originalLetterAsString = codePointToString(next.intValue());
/*     */       
/* 358 */       this.originalToEncoded.put(next, currentEncoding);
/* 359 */       this.encodedToOriginal.put(currentEncoding, originalLetterAsString);
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
/*     */   public String decode(String encoded) throws UnsupportedEncodingException {
/* 374 */     if (encoded == null) {
/* 375 */       return null;
/*     */     }
/*     */     
/* 378 */     StringBuilder result = new StringBuilder();
/*     */     
/* 380 */     for (int j = 0; j < encoded.length(); ) {
/* 381 */       int i = encoded.codePointAt(j);
/* 382 */       String s = codePointToString(i);
/*     */       
/* 384 */       if (s.equals(this.originalToEncoded.get(Integer.valueOf(i)))) {
/* 385 */         result.append(s);
/* 386 */         j++;
/*     */         continue;
/*     */       } 
/* 389 */       if (j + this.encodedLetterLength > encoded.length()) {
/* 390 */         throw new UnsupportedEncodingException("Unexpected end of string while decoding " + encoded);
/*     */       }
/*     */       
/* 393 */       String nextGroup = encoded.substring(j, j + this.encodedLetterLength);
/*     */       
/* 395 */       String next = this.encodedToOriginal.get(nextGroup);
/* 396 */       if (next == null) {
/* 397 */         throw new UnsupportedEncodingException("Unexpected string without decoding (" + nextGroup + ") in " + encoded);
/*     */       }
/*     */ 
/*     */       
/* 401 */       result.append(next);
/* 402 */       j += this.encodedLetterLength;
/*     */     } 
/*     */ 
/*     */     
/* 406 */     return result.toString();
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
/*     */   public String encode(String original) throws UnsupportedEncodingException {
/* 419 */     if (original == null) {
/* 420 */       return null;
/*     */     }
/*     */     
/* 423 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 425 */     for (int i = 0; i < original.length(); ) {
/* 426 */       int codePoint = original.codePointAt(i);
/*     */       
/* 428 */       String nextLetter = this.originalToEncoded.get(Integer.valueOf(codePoint));
/*     */       
/* 430 */       if (nextLetter == null) {
/* 431 */         throw new UnsupportedEncodingException("Couldn't find encoding for '" + 
/*     */             
/* 433 */             codePointToString(codePoint) + "' in " + original);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 439 */       sb.append(nextLetter);
/*     */       
/* 441 */       i += Character.charCount(codePoint);
/*     */     } 
/*     */     
/* 444 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 449 */     if (obj == null) {
/* 450 */       return false;
/*     */     }
/* 452 */     if (obj == this) {
/* 453 */       return true;
/*     */     }
/* 455 */     if (!(obj instanceof AlphabetConverter)) {
/* 456 */       return false;
/*     */     }
/* 458 */     AlphabetConverter other = (AlphabetConverter)obj;
/* 459 */     return (this.originalToEncoded.equals(other.originalToEncoded) && this.encodedToOriginal
/* 460 */       .equals(other.encodedToOriginal) && this.encodedLetterLength == other.encodedLetterLength);
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
/*     */   public int getEncodedCharLength() {
/* 472 */     return this.encodedLetterLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<Integer, String> getOriginalToEncoded() {
/* 483 */     return Collections.unmodifiableMap(this.originalToEncoded);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 488 */     return Objects.hash(new Object[] { this.originalToEncoded, this.encodedToOriginal, 
/*     */           
/* 490 */           Integer.valueOf(this.encodedLetterLength) });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 495 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 497 */     this.originalToEncoded.forEach((k, v) -> sb.append(codePointToString(k.intValue())).append(" -> ").append(k).append(System.lineSeparator()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 503 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\AlphabetConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */