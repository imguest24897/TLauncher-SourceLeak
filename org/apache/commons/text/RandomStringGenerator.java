/*     */ package org.apache.commons.text;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ import org.apache.commons.lang3.ArrayUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class RandomStringGenerator
/*     */ {
/*     */   private final int minimumCodePoint;
/*     */   private final int maximumCodePoint;
/*     */   private final Set<CharacterPredicate> inclusivePredicates;
/*     */   private final TextRandomProvider random;
/*     */   private final List<Character> characterList;
/*     */   
/*     */   public static class Builder
/*     */     implements Builder<RandomStringGenerator>
/*     */   {
/*     */     public static final int DEFAULT_MAXIMUM_CODE_POINT = 1114111;
/*     */     public static final int DEFAULT_LENGTH = 0;
/*     */     public static final int DEFAULT_MINIMUM_CODE_POINT = 0;
/* 106 */     private int minimumCodePoint = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 111 */     private int maximumCodePoint = 1114111;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Set<CharacterPredicate> inclusivePredicates;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private TextRandomProvider random;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private List<Character> characterList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public RandomStringGenerator build() {
/* 135 */       return new RandomStringGenerator(this.minimumCodePoint, this.maximumCodePoint, this.inclusivePredicates, this.random, this.characterList);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder filteredBy(CharacterPredicate... predicates) {
/* 154 */       if (ArrayUtils.isEmpty((Object[])predicates)) {
/* 155 */         this.inclusivePredicates = null;
/* 156 */         return this;
/*     */       } 
/*     */       
/* 159 */       if (this.inclusivePredicates == null) {
/* 160 */         this.inclusivePredicates = new HashSet<>();
/*     */       } else {
/* 162 */         this.inclusivePredicates.clear();
/*     */       } 
/*     */       
/* 165 */       Collections.addAll(this.inclusivePredicates, predicates);
/*     */       
/* 167 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder selectFrom(char... chars) {
/* 186 */       this.characterList = new ArrayList<>();
/* 187 */       if (chars != null) {
/* 188 */         for (char c : chars) {
/* 189 */           this.characterList.add(Character.valueOf(c));
/*     */         }
/*     */       }
/* 192 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder usingRandom(TextRandomProvider random) {
/* 225 */       this.random = random;
/* 226 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withinRange(char[]... pairs) {
/* 246 */       this.characterList = new ArrayList<>();
/* 247 */       if (pairs != null) {
/* 248 */         for (char[] pair : pairs) {
/* 249 */           Validate.isTrue((pair.length == 2), "Each pair must contain minimum and maximum code point", new Object[0]);
/* 250 */           int minimumCodePoint = pair[0];
/* 251 */           int maximumCodePoint = pair[1];
/* 252 */           Validate.isTrue((minimumCodePoint <= maximumCodePoint), "Minimum code point %d is larger than maximum code point %d", new Object[] { Integer.valueOf(minimumCodePoint), 
/* 253 */                 Integer.valueOf(maximumCodePoint) });
/*     */           
/* 255 */           for (int index = minimumCodePoint; index <= maximumCodePoint; index++) {
/* 256 */             this.characterList.add(Character.valueOf((char)index));
/*     */           }
/*     */         } 
/*     */       }
/* 260 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withinRange(int minimumCodePoint, int maximumCodePoint) {
/* 282 */       Validate.isTrue((minimumCodePoint <= maximumCodePoint), "Minimum code point %d is larger than maximum code point %d", new Object[] {
/* 283 */             Integer.valueOf(minimumCodePoint), Integer.valueOf(maximumCodePoint) });
/* 284 */       Validate.isTrue((minimumCodePoint >= 0), "Minimum code point %d is negative", minimumCodePoint);
/* 285 */       Validate.isTrue((maximumCodePoint <= 1114111), "Value %d is larger than Character.MAX_CODE_POINT.", maximumCodePoint);
/*     */ 
/*     */       
/* 288 */       this.minimumCodePoint = minimumCodePoint;
/* 289 */       this.maximumCodePoint = maximumCodePoint;
/* 290 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder builder() {
/* 301 */     return new Builder();
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
/*     */   private RandomStringGenerator(int minimumCodePoint, int maximumCodePoint, Set<CharacterPredicate> inclusivePredicates, TextRandomProvider random, List<Character> characterList) {
/* 345 */     this.minimumCodePoint = minimumCodePoint;
/* 346 */     this.maximumCodePoint = maximumCodePoint;
/* 347 */     this.inclusivePredicates = inclusivePredicates;
/* 348 */     this.random = random;
/* 349 */     this.characterList = characterList;
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
/*     */   public String generate(int length) {
/* 376 */     if (length == 0) {
/* 377 */       return "";
/*     */     }
/* 379 */     Validate.isTrue((length > 0), "Length %d is smaller than zero.", length);
/*     */     
/* 381 */     StringBuilder builder = new StringBuilder(length);
/* 382 */     long remaining = length;
/*     */     
/*     */     do {
/*     */       int codePoint;
/* 386 */       if (this.characterList != null && !this.characterList.isEmpty()) {
/* 387 */         codePoint = generateRandomNumber(this.characterList);
/*     */       } else {
/* 389 */         codePoint = generateRandomNumber(this.minimumCodePoint, this.maximumCodePoint);
/*     */       } 
/* 391 */       switch (Character.getType(codePoint)) {
/*     */         case 0:
/*     */         case 18:
/*     */         case 19:
/*     */           break;
/*     */ 
/*     */         
/*     */         default:
/* 399 */           if (this.inclusivePredicates != null) {
/* 400 */             boolean matchedFilter = false;
/* 401 */             for (CharacterPredicate predicate : this.inclusivePredicates) {
/* 402 */               if (predicate.test(codePoint)) {
/* 403 */                 matchedFilter = true;
/*     */                 break;
/*     */               } 
/*     */             } 
/* 407 */             if (!matchedFilter) {
/*     */               break;
/*     */             }
/*     */           } 
/*     */           
/* 412 */           builder.appendCodePoint(codePoint);
/* 413 */           remaining--; break;
/*     */       } 
/* 415 */     } while (remaining != 0L);
/*     */     
/* 417 */     return builder.toString();
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
/*     */   public String generate(int minLengthInclusive, int maxLengthInclusive) {
/* 435 */     Validate.isTrue((minLengthInclusive >= 0), "Minimum length %d is smaller than zero.", minLengthInclusive);
/* 436 */     Validate.isTrue((minLengthInclusive <= maxLengthInclusive), "Maximum length %d is smaller than minimum length %d.", new Object[] {
/* 437 */           Integer.valueOf(maxLengthInclusive), Integer.valueOf(minLengthInclusive) });
/* 438 */     return generate(generateRandomNumber(minLengthInclusive, maxLengthInclusive));
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
/*     */   private int generateRandomNumber(int minInclusive, int maxInclusive) {
/* 452 */     if (this.random != null) {
/* 453 */       return this.random.nextInt(maxInclusive - minInclusive + 1) + minInclusive;
/*     */     }
/* 455 */     return ThreadLocalRandom.current().nextInt(minInclusive, maxInclusive + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int generateRandomNumber(List<Character> characterList) {
/* 466 */     int listSize = characterList.size();
/* 467 */     if (this.random != null) {
/* 468 */       return String.valueOf(characterList.get(this.random.nextInt(listSize))).codePointAt(0);
/*     */     }
/* 470 */     return String.valueOf(characterList.get(ThreadLocalRandom.current().nextInt(0, listSize))).codePointAt(0);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\RandomStringGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */