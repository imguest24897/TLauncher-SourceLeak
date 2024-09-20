/*     */ package org.apache.commons.text.matcher;
/*     */ 
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
/*     */ public final class StringMatcherFactory
/*     */ {
/*  33 */   private static final AbstractStringMatcher.CharMatcher COMMA_MATCHER = new AbstractStringMatcher.CharMatcher(',');
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  38 */   private static final AbstractStringMatcher.CharMatcher DOUBLE_QUOTE_MATCHER = new AbstractStringMatcher.CharMatcher('"');
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  44 */   public static final StringMatcherFactory INSTANCE = new StringMatcherFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   private static final AbstractStringMatcher.NoneMatcher NONE_MATCHER = new AbstractStringMatcher.NoneMatcher();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   private static final AbstractStringMatcher.CharSetMatcher QUOTE_MATCHER = new AbstractStringMatcher.CharSetMatcher("'\""
/*  55 */       .toCharArray());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   private static final AbstractStringMatcher.CharMatcher SINGLE_QUOTE_MATCHER = new AbstractStringMatcher.CharMatcher('\'');
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   private static final AbstractStringMatcher.CharMatcher SPACE_MATCHER = new AbstractStringMatcher.CharMatcher(' ');
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   private static final AbstractStringMatcher.CharSetMatcher SPLIT_MATCHER = new AbstractStringMatcher.CharSetMatcher(" \t\n\r\f"
/*  72 */       .toCharArray());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   private static final AbstractStringMatcher.CharMatcher TAB_MATCHER = new AbstractStringMatcher.CharMatcher('\t');
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   private static final AbstractStringMatcher.TrimMatcher TRIM_MATCHER = new AbstractStringMatcher.TrimMatcher();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringMatcher andMatcher(StringMatcher... stringMatchers) {
/*  99 */     int len = ArrayUtils.getLength(stringMatchers);
/* 100 */     if (len == 0) {
/* 101 */       return NONE_MATCHER;
/*     */     }
/* 103 */     if (len == 1) {
/* 104 */       return stringMatchers[0];
/*     */     }
/* 106 */     return new AbstractStringMatcher.AndStringMatcher(stringMatchers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringMatcher charMatcher(char ch) {
/* 116 */     return new AbstractStringMatcher.CharMatcher(ch);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringMatcher charSetMatcher(char... chars) {
/* 126 */     int len = ArrayUtils.getLength(chars);
/* 127 */     if (len == 0) {
/* 128 */       return NONE_MATCHER;
/*     */     }
/* 130 */     if (len == 1) {
/* 131 */       return new AbstractStringMatcher.CharMatcher(chars[0]);
/*     */     }
/* 133 */     return new AbstractStringMatcher.CharSetMatcher(chars);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringMatcher charSetMatcher(String chars) {
/* 143 */     int len = StringUtils.length(chars);
/* 144 */     if (len == 0) {
/* 145 */       return NONE_MATCHER;
/*     */     }
/* 147 */     if (len == 1) {
/* 148 */       return new AbstractStringMatcher.CharMatcher(chars.charAt(0));
/*     */     }
/* 150 */     return new AbstractStringMatcher.CharSetMatcher(chars.toCharArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringMatcher commaMatcher() {
/* 159 */     return COMMA_MATCHER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringMatcher doubleQuoteMatcher() {
/* 168 */     return DOUBLE_QUOTE_MATCHER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringMatcher noneMatcher() {
/* 177 */     return NONE_MATCHER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringMatcher quoteMatcher() {
/* 186 */     return QUOTE_MATCHER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringMatcher singleQuoteMatcher() {
/* 195 */     return SINGLE_QUOTE_MATCHER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringMatcher spaceMatcher() {
/* 204 */     return SPACE_MATCHER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringMatcher splitMatcher() {
/* 213 */     return SPLIT_MATCHER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringMatcher stringMatcher(char... chars) {
/* 224 */     int length = ArrayUtils.getLength(chars);
/* 225 */     return (length == 0) ? NONE_MATCHER : (
/* 226 */       (length == 1) ? new AbstractStringMatcher.CharMatcher(chars[0]) : 
/* 227 */       new AbstractStringMatcher.CharArrayMatcher(chars));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringMatcher stringMatcher(String str) {
/* 237 */     return StringUtils.isEmpty(str) ? NONE_MATCHER : stringMatcher(str.toCharArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringMatcher tabMatcher() {
/* 246 */     return TAB_MATCHER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringMatcher trimMatcher() {
/* 255 */     return TRIM_MATCHER;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\matcher\StringMatcherFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */