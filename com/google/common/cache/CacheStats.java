/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.LongMath;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public final class CacheStats
/*     */ {
/*     */   private final long hitCount;
/*     */   private final long missCount;
/*     */   private final long loadSuccessCount;
/*     */   private final long loadExceptionCount;
/*     */   private final long totalLoadTime;
/*     */   private final long evictionCount;
/*     */   
/*     */   public CacheStats(long hitCount, long missCount, long loadSuccessCount, long loadExceptionCount, long totalLoadTime, long evictionCount) {
/*  85 */     Preconditions.checkArgument((hitCount >= 0L));
/*  86 */     Preconditions.checkArgument((missCount >= 0L));
/*  87 */     Preconditions.checkArgument((loadSuccessCount >= 0L));
/*  88 */     Preconditions.checkArgument((loadExceptionCount >= 0L));
/*  89 */     Preconditions.checkArgument((totalLoadTime >= 0L));
/*  90 */     Preconditions.checkArgument((evictionCount >= 0L));
/*     */     
/*  92 */     this.hitCount = hitCount;
/*  93 */     this.missCount = missCount;
/*  94 */     this.loadSuccessCount = loadSuccessCount;
/*  95 */     this.loadExceptionCount = loadExceptionCount;
/*  96 */     this.totalLoadTime = totalLoadTime;
/*  97 */     this.evictionCount = evictionCount;
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
/*     */   public long requestCount() {
/* 109 */     return LongMath.saturatedAdd(this.hitCount, this.missCount);
/*     */   }
/*     */ 
/*     */   
/*     */   public long hitCount() {
/* 114 */     return this.hitCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double hitRate() {
/* 123 */     long requestCount = requestCount();
/* 124 */     return (requestCount == 0L) ? 1.0D : (this.hitCount / requestCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long missCount() {
/* 134 */     return this.missCount;
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
/*     */   public double missRate() {
/* 147 */     long requestCount = requestCount();
/* 148 */     return (requestCount == 0L) ? 0.0D : (this.missCount / requestCount);
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
/*     */   public long loadCount() {
/* 161 */     return LongMath.saturatedAdd(this.loadSuccessCount, this.loadExceptionCount);
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
/*     */   public long loadSuccessCount() {
/* 175 */     return this.loadSuccessCount;
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
/*     */   public long loadExceptionCount() {
/* 189 */     return this.loadExceptionCount;
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
/*     */   public double loadExceptionRate() {
/* 202 */     long totalLoadCount = LongMath.saturatedAdd(this.loadSuccessCount, this.loadExceptionCount);
/* 203 */     return (totalLoadCount == 0L) ? 0.0D : (this.loadExceptionCount / totalLoadCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long totalLoadTime() {
/* 213 */     return this.totalLoadTime;
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
/*     */   public double averageLoadPenalty() {
/* 225 */     long totalLoadCount = LongMath.saturatedAdd(this.loadSuccessCount, this.loadExceptionCount);
/* 226 */     return (totalLoadCount == 0L) ? 0.0D : (this.totalLoadTime / totalLoadCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long evictionCount() {
/* 234 */     return this.evictionCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheStats minus(CacheStats other) {
/* 243 */     return new CacheStats(
/* 244 */         Math.max(0L, LongMath.saturatedSubtract(this.hitCount, other.hitCount)), 
/* 245 */         Math.max(0L, LongMath.saturatedSubtract(this.missCount, other.missCount)), 
/* 246 */         Math.max(0L, LongMath.saturatedSubtract(this.loadSuccessCount, other.loadSuccessCount)), 
/* 247 */         Math.max(0L, LongMath.saturatedSubtract(this.loadExceptionCount, other.loadExceptionCount)), 
/* 248 */         Math.max(0L, LongMath.saturatedSubtract(this.totalLoadTime, other.totalLoadTime)), 
/* 249 */         Math.max(0L, LongMath.saturatedSubtract(this.evictionCount, other.evictionCount)));
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
/*     */   public CacheStats plus(CacheStats other) {
/* 263 */     return new CacheStats(
/* 264 */         LongMath.saturatedAdd(this.hitCount, other.hitCount), 
/* 265 */         LongMath.saturatedAdd(this.missCount, other.missCount), 
/* 266 */         LongMath.saturatedAdd(this.loadSuccessCount, other.loadSuccessCount), 
/* 267 */         LongMath.saturatedAdd(this.loadExceptionCount, other.loadExceptionCount), 
/* 268 */         LongMath.saturatedAdd(this.totalLoadTime, other.totalLoadTime), 
/* 269 */         LongMath.saturatedAdd(this.evictionCount, other.evictionCount));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 274 */     return Objects.hashCode(new Object[] {
/* 275 */           Long.valueOf(this.hitCount), Long.valueOf(this.missCount), Long.valueOf(this.loadSuccessCount), Long.valueOf(this.loadExceptionCount), Long.valueOf(this.totalLoadTime), Long.valueOf(this.evictionCount)
/*     */         });
/*     */   }
/*     */   
/*     */   public boolean equals(Object object) {
/* 280 */     if (object instanceof CacheStats) {
/* 281 */       CacheStats other = (CacheStats)object;
/* 282 */       return (this.hitCount == other.hitCount && this.missCount == other.missCount && this.loadSuccessCount == other.loadSuccessCount && this.loadExceptionCount == other.loadExceptionCount && this.totalLoadTime == other.totalLoadTime && this.evictionCount == other.evictionCount);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 289 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 294 */     return MoreObjects.toStringHelper(this)
/* 295 */       .add("hitCount", this.hitCount)
/* 296 */       .add("missCount", this.missCount)
/* 297 */       .add("loadSuccessCount", this.loadSuccessCount)
/* 298 */       .add("loadExceptionCount", this.loadExceptionCount)
/* 299 */       .add("totalLoadTime", this.totalLoadTime)
/* 300 */       .add("evictionCount", this.evictionCount)
/* 301 */       .toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\cache\CacheStats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */