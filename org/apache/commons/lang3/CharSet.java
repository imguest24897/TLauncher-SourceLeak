/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CharSet
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 5947847346149275958L;
/*  48 */   public static final CharSet EMPTY = new CharSet(new String[] { (String)null });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   public static final CharSet ASCII_ALPHA = new CharSet(new String[] { "a-zA-Z" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   public static final CharSet ASCII_ALPHA_LOWER = new CharSet(new String[] { "a-z" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   public static final CharSet ASCII_ALPHA_UPPER = new CharSet(new String[] { "A-Z" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   public static final CharSet ASCII_NUMERIC = new CharSet(new String[] { "0-9" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   protected static final Map<String, CharSet> COMMON = Collections.synchronizedMap(new HashMap<>());
/*     */   
/*     */   static {
/*  82 */     COMMON.put(null, EMPTY);
/*  83 */     COMMON.put("", EMPTY);
/*  84 */     COMMON.put("a-zA-Z", ASCII_ALPHA);
/*  85 */     COMMON.put("A-Za-z", ASCII_ALPHA);
/*  86 */     COMMON.put("a-z", ASCII_ALPHA_LOWER);
/*  87 */     COMMON.put("A-Z", ASCII_ALPHA_UPPER);
/*  88 */     COMMON.put("0-9", ASCII_NUMERIC);
/*     */   }
/*     */ 
/*     */   
/*  92 */   private final Set<CharRange> set = Collections.synchronizedSet(new HashSet<>());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSet getInstance(String... setStrs) {
/* 156 */     if (setStrs == null) {
/* 157 */       return null;
/*     */     }
/* 159 */     if (setStrs.length == 1) {
/* 160 */       CharSet common = COMMON.get(setStrs[0]);
/* 161 */       if (common != null) {
/* 162 */         return common;
/*     */       }
/*     */     } 
/* 165 */     return new CharSet(setStrs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CharSet(String... set) {
/* 176 */     Stream.<String>of(set).forEach(this::add);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void add(String str) {
/* 185 */     if (str == null) {
/*     */       return;
/*     */     }
/*     */     
/* 189 */     int len = str.length();
/* 190 */     int pos = 0;
/* 191 */     while (pos < len) {
/* 192 */       int remainder = len - pos;
/* 193 */       if (remainder >= 4 && str.charAt(pos) == '^' && str.charAt(pos + 2) == '-') {
/*     */         
/* 195 */         this.set.add(CharRange.isNotIn(str.charAt(pos + 1), str.charAt(pos + 3)));
/* 196 */         pos += 4; continue;
/* 197 */       }  if (remainder >= 3 && str.charAt(pos + 1) == '-') {
/*     */         
/* 199 */         this.set.add(CharRange.isIn(str.charAt(pos), str.charAt(pos + 2)));
/* 200 */         pos += 3; continue;
/* 201 */       }  if (remainder >= 2 && str.charAt(pos) == '^') {
/*     */         
/* 203 */         this.set.add(CharRange.isNot(str.charAt(pos + 1)));
/* 204 */         pos += 2;
/*     */         continue;
/*     */       } 
/* 207 */       this.set.add(CharRange.is(str.charAt(pos)));
/* 208 */       pos++;
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
/*     */   CharRange[] getCharRanges() {
/* 222 */     return this.set.<CharRange>toArray(CharRange.EMPTY_ARRAY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(char ch) {
/* 233 */     synchronized (this.set) {
/* 234 */       return this.set.stream().anyMatch(range -> range.contains(ch));
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
/*     */   public boolean equals(Object obj) {
/* 252 */     if (obj == this) {
/* 253 */       return true;
/*     */     }
/* 255 */     if (!(obj instanceof CharSet)) {
/* 256 */       return false;
/*     */     }
/* 258 */     CharSet other = (CharSet)obj;
/* 259 */     return this.set.equals(other.set);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 270 */     return 89 + this.set.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 280 */     return this.set.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\CharSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */