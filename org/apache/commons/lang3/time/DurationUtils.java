/*     */ package org.apache.commons.lang3.time;
/*     */ 
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.time.temporal.ChronoUnit;
/*     */ import java.time.temporal.Temporal;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.lang3.LongRange;
/*     */ import org.apache.commons.lang3.ObjectUtils;
/*     */ import org.apache.commons.lang3.function.FailableBiConsumer;
/*     */ import org.apache.commons.lang3.function.FailableConsumer;
/*     */ import org.apache.commons.lang3.function.FailableRunnable;
/*     */ import org.apache.commons.lang3.math.NumberUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DurationUtils
/*     */ {
/*  44 */   static final LongRange LONG_TO_INT_RANGE = LongRange.of(NumberUtils.LONG_INT_MIN_VALUE, NumberUtils.LONG_INT_MAX_VALUE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Throwable> void accept(FailableBiConsumer<Long, Integer, T> consumer, Duration duration) throws T {
/*  57 */     if (consumer != null && duration != null) {
/*  58 */       consumer.accept(Long.valueOf(duration.toMillis()), Integer.valueOf(getNanosOfMilli(duration)));
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
/*     */   @Deprecated
/*     */   public static int getNanosOfMiili(Duration duration) {
/*  78 */     return getNanosOfMilli(duration);
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
/*     */   public static int getNanosOfMilli(Duration duration) {
/*  96 */     return zeroIfNull(duration).getNano() % 1000000;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isPositive(Duration duration) {
/* 106 */     return (!duration.isNegative() && !duration.isZero());
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
/*     */   public static <E extends Throwable> Duration of(FailableConsumer<Instant, E> consumer) throws E {
/* 119 */     return since(now(consumer::accept));
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
/*     */   public static <E extends Throwable> Duration of(FailableRunnable<E> runnable) throws E {
/* 132 */     return of(start -> runnable.run());
/*     */   }
/*     */   
/*     */   private static <E extends Throwable> Instant now(FailableConsumer<Instant, E> nowConsumer) throws E {
/* 136 */     Instant start = Instant.now();
/* 137 */     nowConsumer.accept(start);
/* 138 */     return start;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Duration since(Temporal startInclusive) {
/* 149 */     return Duration.between(startInclusive, Instant.now());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ChronoUnit toChronoUnit(TimeUnit timeUnit) {
/* 160 */     switch ((TimeUnit)Objects.requireNonNull((T)timeUnit)) {
/*     */       case NANOSECONDS:
/* 162 */         return ChronoUnit.NANOS;
/*     */       case MICROSECONDS:
/* 164 */         return ChronoUnit.MICROS;
/*     */       case MILLISECONDS:
/* 166 */         return ChronoUnit.MILLIS;
/*     */       case SECONDS:
/* 168 */         return ChronoUnit.SECONDS;
/*     */       case MINUTES:
/* 170 */         return ChronoUnit.MINUTES;
/*     */       case HOURS:
/* 172 */         return ChronoUnit.HOURS;
/*     */       case DAYS:
/* 174 */         return ChronoUnit.DAYS;
/*     */     } 
/* 176 */     throw new IllegalArgumentException(timeUnit.toString());
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
/*     */   public static Duration toDuration(long amount, TimeUnit timeUnit) {
/* 188 */     return Duration.of(amount, toChronoUnit(timeUnit));
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
/*     */   public static int toMillisInt(Duration duration) {
/* 207 */     Objects.requireNonNull(duration, "duration");
/*     */     
/* 209 */     return ((Long)LONG_TO_INT_RANGE.fit(Long.valueOf(duration.toMillis()))).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Duration zeroIfNull(Duration duration) {
/* 219 */     return (Duration)ObjectUtils.defaultIfNull(duration, Duration.ZERO);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\time\DurationUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */