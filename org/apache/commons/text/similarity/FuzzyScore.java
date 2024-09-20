/*     */ package org.apache.commons.text.similarity;
/*     */ 
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FuzzyScore
/*     */ {
/*     */   private final Locale locale;
/*     */   
/*     */   public FuzzyScore(Locale locale) {
/*  53 */     if (locale == null) {
/*  54 */       throw new IllegalArgumentException("Locale must not be null");
/*     */     }
/*  56 */     this.locale = locale;
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
/*     */   public Integer fuzzyScore(CharSequence term, CharSequence query) {
/*  83 */     if (term == null || query == null) {
/*  84 */       throw new IllegalArgumentException("CharSequences must not be null");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     String termLowerCase = term.toString().toLowerCase(this.locale);
/*  92 */     String queryLowerCase = query.toString().toLowerCase(this.locale);
/*     */ 
/*     */     
/*  95 */     int score = 0;
/*     */ 
/*     */ 
/*     */     
/*  99 */     int termIndex = 0;
/*     */ 
/*     */     
/* 102 */     int previousMatchingCharacterIndex = Integer.MIN_VALUE;
/*     */     
/* 104 */     for (int queryIndex = 0; queryIndex < queryLowerCase.length(); queryIndex++) {
/* 105 */       char queryChar = queryLowerCase.charAt(queryIndex);
/*     */       
/* 107 */       boolean termCharacterMatchFound = false;
/*     */       
/* 109 */       for (; termIndex < termLowerCase.length() && !termCharacterMatchFound; termIndex++) {
/* 110 */         char termChar = termLowerCase.charAt(termIndex);
/*     */         
/* 112 */         if (queryChar == termChar) {
/*     */           
/* 114 */           score++;
/*     */ 
/*     */ 
/*     */           
/* 118 */           if (previousMatchingCharacterIndex + 1 == termIndex) {
/* 119 */             score += 2;
/*     */           }
/*     */           
/* 122 */           previousMatchingCharacterIndex = termIndex;
/*     */ 
/*     */ 
/*     */           
/* 126 */           termCharacterMatchFound = true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 131 */     return Integer.valueOf(score);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Locale getLocale() {
/* 140 */     return this.locale;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\similarity\FuzzyScore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */