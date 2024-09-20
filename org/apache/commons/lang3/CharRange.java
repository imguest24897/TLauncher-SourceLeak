/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class CharRange
/*     */   implements Iterable<Character>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 8270183163158333422L;
/*     */   private final char start;
/*     */   private final char end;
/*     */   private final boolean negated;
/*     */   private transient String iToString;
/*  56 */   static final CharRange[] EMPTY_ARRAY = new CharRange[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CharRange(char start, char end, boolean negated) {
/*  73 */     if (start > end) {
/*  74 */       char temp = start;
/*  75 */       start = end;
/*  76 */       end = temp;
/*     */     } 
/*     */     
/*  79 */     this.start = start;
/*  80 */     this.end = end;
/*  81 */     this.negated = negated;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharRange is(char ch) {
/*  92 */     return new CharRange(ch, ch, false);
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
/*     */   public static CharRange isNot(char ch) {
/* 106 */     return new CharRange(ch, ch, true);
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
/*     */   public static CharRange isIn(char start, char end) {
/* 121 */     return new CharRange(start, end, false);
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
/*     */   public static CharRange isNotIn(char start, char end) {
/* 139 */     return new CharRange(start, end, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getStart() {
/* 149 */     return this.start;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getEnd() {
/* 158 */     return this.end;
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
/*     */   public boolean isNegated() {
/* 170 */     return this.negated;
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
/* 181 */     return (((ch >= this.start && ch <= this.end)) != this.negated);
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
/*     */   public boolean contains(CharRange range) {
/* 193 */     Objects.requireNonNull(range, "range");
/* 194 */     if (this.negated) {
/* 195 */       if (range.negated) {
/* 196 */         return (this.start >= range.start && this.end <= range.end);
/*     */       }
/* 198 */       return (range.end < this.start || range.start > this.end);
/*     */     } 
/* 200 */     if (range.negated) {
/* 201 */       return (this.start == '\000' && this.end == Character.MAX_VALUE);
/*     */     }
/* 203 */     return (this.start <= range.start && this.end >= range.end);
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
/*     */   public boolean equals(Object obj) {
/* 216 */     if (obj == this) {
/* 217 */       return true;
/*     */     }
/* 219 */     if (!(obj instanceof CharRange)) {
/* 220 */       return false;
/*     */     }
/* 222 */     CharRange other = (CharRange)obj;
/* 223 */     return (this.start == other.start && this.end == other.end && this.negated == other.negated);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 233 */     return 83 + this.start + 7 * this.end + (this.negated ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 243 */     if (this.iToString == null) {
/* 244 */       StringBuilder buf = new StringBuilder(4);
/* 245 */       if (isNegated()) {
/* 246 */         buf.append('^');
/*     */       }
/* 248 */       buf.append(this.start);
/* 249 */       if (this.start != this.end) {
/* 250 */         buf.append('-');
/* 251 */         buf.append(this.end);
/*     */       } 
/* 253 */       this.iToString = buf.toString();
/*     */     } 
/* 255 */     return this.iToString;
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
/*     */   public Iterator<Character> iterator() {
/* 267 */     return new CharacterIterator(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CharacterIterator
/*     */     implements Iterator<Character>
/*     */   {
/*     */     private char current;
/*     */ 
/*     */     
/*     */     private final CharRange range;
/*     */ 
/*     */     
/*     */     private boolean hasNext;
/*     */ 
/*     */ 
/*     */     
/*     */     private CharacterIterator(CharRange r) {
/* 287 */       this.range = r;
/* 288 */       this.hasNext = true;
/*     */       
/* 290 */       if (this.range.negated) {
/* 291 */         if (this.range.start == '\000') {
/* 292 */           if (this.range.end == Character.MAX_VALUE) {
/*     */             
/* 294 */             this.hasNext = false;
/*     */           } else {
/* 296 */             this.current = (char)(this.range.end + 1);
/*     */           } 
/*     */         } else {
/* 299 */           this.current = Character.MIN_VALUE;
/*     */         } 
/*     */       } else {
/* 302 */         this.current = this.range.start;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void prepareNext() {
/* 310 */       if (this.range.negated) {
/* 311 */         if (this.current == Character.MAX_VALUE) {
/* 312 */           this.hasNext = false;
/* 313 */         } else if (this.current + 1 == this.range.start) {
/* 314 */           if (this.range.end == Character.MAX_VALUE) {
/* 315 */             this.hasNext = false;
/*     */           } else {
/* 317 */             this.current = (char)(this.range.end + 1);
/*     */           } 
/*     */         } else {
/* 320 */           this.current = (char)(this.current + 1);
/*     */         } 
/* 322 */       } else if (this.current < this.range.end) {
/* 323 */         this.current = (char)(this.current + 1);
/*     */       } else {
/* 325 */         this.hasNext = false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 336 */       return this.hasNext;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Character next() {
/* 346 */       if (!this.hasNext) {
/* 347 */         throw new NoSuchElementException();
/*     */       }
/* 349 */       char cur = this.current;
/* 350 */       prepareNext();
/* 351 */       return Character.valueOf(cur);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void remove() {
/* 362 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\CharRange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */