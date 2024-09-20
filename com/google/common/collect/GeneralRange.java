/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true)
/*     */ final class GeneralRange<T>
/*     */   implements Serializable
/*     */ {
/*     */   private final Comparator<? super T> comparator;
/*     */   private final boolean hasLowerBound;
/*     */   private final T lowerEndpoint;
/*     */   private final BoundType lowerBoundType;
/*     */   private final boolean hasUpperBound;
/*     */   private final T upperEndpoint;
/*     */   private final BoundType upperBoundType;
/*     */   private transient GeneralRange<T> reverse;
/*     */   
/*     */   static <T extends Comparable> GeneralRange<T> from(Range<T> range) {
/*  41 */     C c1 = range.hasLowerBound() ? (C)range.lowerEndpoint() : null;
/*  42 */     BoundType lowerBoundType = range.hasLowerBound() ? range.lowerBoundType() : BoundType.OPEN;
/*     */     
/*  44 */     C c2 = range.hasUpperBound() ? (C)range.upperEndpoint() : null;
/*  45 */     BoundType upperBoundType = range.hasUpperBound() ? range.upperBoundType() : BoundType.OPEN;
/*  46 */     return new GeneralRange<>(
/*  47 */         Ordering.natural(), range
/*  48 */         .hasLowerBound(), (T)c1, lowerBoundType, range
/*     */ 
/*     */         
/*  51 */         .hasUpperBound(), (T)c2, upperBoundType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> GeneralRange<T> all(Comparator<? super T> comparator) {
/*  58 */     return new GeneralRange<>(comparator, false, null, BoundType.OPEN, false, null, BoundType.OPEN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> GeneralRange<T> downTo(Comparator<? super T> comparator, T endpoint, BoundType boundType) {
/*  67 */     return new GeneralRange<>(comparator, true, endpoint, boundType, false, null, BoundType.OPEN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> GeneralRange<T> upTo(Comparator<? super T> comparator, T endpoint, BoundType boundType) {
/*  76 */     return new GeneralRange<>(comparator, false, null, BoundType.OPEN, true, endpoint, boundType);
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
/*     */   static <T> GeneralRange<T> range(Comparator<? super T> comparator, T lower, BoundType lowerType, T upper, BoundType upperType) {
/*  89 */     return new GeneralRange<>(comparator, true, lower, lowerType, true, upper, upperType);
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
/*     */   private GeneralRange(Comparator<? super T> comparator, boolean hasLowerBound, T lowerEndpoint, BoundType lowerBoundType, boolean hasUpperBound, T upperEndpoint, BoundType upperBoundType) {
/* 108 */     this.comparator = (Comparator<? super T>)Preconditions.checkNotNull(comparator);
/* 109 */     this.hasLowerBound = hasLowerBound;
/* 110 */     this.hasUpperBound = hasUpperBound;
/* 111 */     this.lowerEndpoint = lowerEndpoint;
/* 112 */     this.lowerBoundType = (BoundType)Preconditions.checkNotNull(lowerBoundType);
/* 113 */     this.upperEndpoint = upperEndpoint;
/* 114 */     this.upperBoundType = (BoundType)Preconditions.checkNotNull(upperBoundType);
/*     */     
/* 116 */     if (hasLowerBound) {
/* 117 */       comparator.compare(lowerEndpoint, lowerEndpoint);
/*     */     }
/* 119 */     if (hasUpperBound) {
/* 120 */       comparator.compare(upperEndpoint, upperEndpoint);
/*     */     }
/* 122 */     if (hasLowerBound && hasUpperBound) {
/* 123 */       int cmp = comparator.compare(lowerEndpoint, upperEndpoint);
/*     */       
/* 125 */       Preconditions.checkArgument((cmp <= 0), "lowerEndpoint (%s) > upperEndpoint (%s)", lowerEndpoint, upperEndpoint);
/*     */       
/* 127 */       if (cmp == 0) {
/* 128 */         Preconditions.checkArgument(((lowerBoundType != BoundType.OPEN)) | ((upperBoundType != BoundType.OPEN)));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   Comparator<? super T> comparator() {
/* 134 */     return this.comparator;
/*     */   }
/*     */   
/*     */   boolean hasLowerBound() {
/* 138 */     return this.hasLowerBound;
/*     */   }
/*     */   
/*     */   boolean hasUpperBound() {
/* 142 */     return this.hasUpperBound;
/*     */   }
/*     */   
/*     */   boolean isEmpty() {
/* 146 */     return ((hasUpperBound() && tooLow(getUpperEndpoint())) || (
/* 147 */       hasLowerBound() && tooHigh(getLowerEndpoint())));
/*     */   }
/*     */   
/*     */   boolean tooLow(T t) {
/* 151 */     if (!hasLowerBound()) {
/* 152 */       return false;
/*     */     }
/* 154 */     T lbound = getLowerEndpoint();
/* 155 */     int cmp = this.comparator.compare(t, lbound);
/* 156 */     return ((cmp < 0) ? 1 : 0) | ((cmp == 0)) & ((getLowerBoundType() == BoundType.OPEN));
/*     */   }
/*     */   
/*     */   boolean tooHigh(T t) {
/* 160 */     if (!hasUpperBound()) {
/* 161 */       return false;
/*     */     }
/* 163 */     T ubound = getUpperEndpoint();
/* 164 */     int cmp = this.comparator.compare(t, ubound);
/* 165 */     return ((cmp > 0) ? 1 : 0) | ((cmp == 0)) & ((getUpperBoundType() == BoundType.OPEN));
/*     */   }
/*     */   
/*     */   boolean contains(T t) {
/* 169 */     return (!tooLow(t) && !tooHigh(t));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   GeneralRange<T> intersect(GeneralRange<T> other) {
/* 176 */     Preconditions.checkNotNull(other);
/* 177 */     Preconditions.checkArgument(this.comparator.equals(other.comparator));
/*     */     
/* 179 */     boolean hasLowBound = this.hasLowerBound;
/* 180 */     T lowEnd = getLowerEndpoint();
/* 181 */     BoundType lowType = getLowerBoundType();
/* 182 */     if (!hasLowerBound()) {
/* 183 */       hasLowBound = other.hasLowerBound;
/* 184 */       lowEnd = other.getLowerEndpoint();
/* 185 */       lowType = other.getLowerBoundType();
/* 186 */     } else if (other.hasLowerBound()) {
/* 187 */       int cmp = this.comparator.compare(getLowerEndpoint(), other.getLowerEndpoint());
/* 188 */       if (cmp < 0 || (cmp == 0 && other.getLowerBoundType() == BoundType.OPEN)) {
/* 189 */         lowEnd = other.getLowerEndpoint();
/* 190 */         lowType = other.getLowerBoundType();
/*     */       } 
/*     */     } 
/*     */     
/* 194 */     boolean hasUpBound = this.hasUpperBound;
/* 195 */     T upEnd = getUpperEndpoint();
/* 196 */     BoundType upType = getUpperBoundType();
/* 197 */     if (!hasUpperBound()) {
/* 198 */       hasUpBound = other.hasUpperBound;
/* 199 */       upEnd = other.getUpperEndpoint();
/* 200 */       upType = other.getUpperBoundType();
/* 201 */     } else if (other.hasUpperBound()) {
/* 202 */       int cmp = this.comparator.compare(getUpperEndpoint(), other.getUpperEndpoint());
/* 203 */       if (cmp > 0 || (cmp == 0 && other.getUpperBoundType() == BoundType.OPEN)) {
/* 204 */         upEnd = other.getUpperEndpoint();
/* 205 */         upType = other.getUpperBoundType();
/*     */       } 
/*     */     } 
/*     */     
/* 209 */     if (hasLowBound && hasUpBound) {
/* 210 */       int cmp = this.comparator.compare(lowEnd, upEnd);
/* 211 */       if (cmp > 0 || (cmp == 0 && lowType == BoundType.OPEN && upType == BoundType.OPEN)) {
/*     */         
/* 213 */         lowEnd = upEnd;
/* 214 */         lowType = BoundType.OPEN;
/* 215 */         upType = BoundType.CLOSED;
/*     */       } 
/*     */     } 
/*     */     
/* 219 */     return new GeneralRange(this.comparator, hasLowBound, lowEnd, lowType, hasUpBound, upEnd, upType);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 224 */     if (obj instanceof GeneralRange) {
/* 225 */       GeneralRange<?> r = (GeneralRange)obj;
/* 226 */       return (this.comparator.equals(r.comparator) && this.hasLowerBound == r.hasLowerBound && this.hasUpperBound == r.hasUpperBound && 
/*     */ 
/*     */         
/* 229 */         getLowerBoundType().equals(r.getLowerBoundType()) && 
/* 230 */         getUpperBoundType().equals(r.getUpperBoundType()) && 
/* 231 */         Objects.equal(getLowerEndpoint(), r.getLowerEndpoint()) && 
/* 232 */         Objects.equal(getUpperEndpoint(), r.getUpperEndpoint()));
/*     */     } 
/* 234 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 239 */     return Objects.hashCode(new Object[] { this.comparator, 
/*     */           
/* 241 */           getLowerEndpoint(), 
/* 242 */           getLowerBoundType(), 
/* 243 */           getUpperEndpoint(), 
/* 244 */           getUpperBoundType() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   GeneralRange<T> reverse() {
/* 251 */     GeneralRange<T> result = this.reverse;
/* 252 */     if (result == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 261 */       result = new GeneralRange(Ordering.<T>from(this.comparator).reverse(), this.hasUpperBound, getUpperEndpoint(), getUpperBoundType(), this.hasLowerBound, getLowerEndpoint(), getLowerBoundType());
/* 262 */       result.reverse = this;
/* 263 */       return this.reverse = result;
/*     */     } 
/* 265 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 270 */     String str1 = String.valueOf(this.comparator);
/*     */     
/* 272 */     byte b1 = (this.lowerBoundType == BoundType.CLOSED) ? 91 : 40;
/* 273 */     String str2 = String.valueOf(this.hasLowerBound ? this.lowerEndpoint : "-∞");
/*     */     
/* 275 */     String str3 = String.valueOf(this.hasUpperBound ? this.upperEndpoint : "∞");
/* 276 */     byte b2 = (this.upperBoundType == BoundType.CLOSED) ? 93 : 41; return (new StringBuilder(4 + String.valueOf(str1).length() + String.valueOf(str2).length() + String.valueOf(str3).length())).append(str1).append(":").append(b1).append(str2).append(',').append(str3).append(b2).toString();
/*     */   }
/*     */   
/*     */   T getLowerEndpoint() {
/* 280 */     return this.lowerEndpoint;
/*     */   }
/*     */   
/*     */   BoundType getLowerBoundType() {
/* 284 */     return this.lowerBoundType;
/*     */   }
/*     */   
/*     */   T getUpperEndpoint() {
/* 288 */     return this.upperEndpoint;
/*     */   }
/*     */   
/*     */   BoundType getUpperBoundType() {
/* 292 */     return this.upperBoundType;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\GeneralRange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */