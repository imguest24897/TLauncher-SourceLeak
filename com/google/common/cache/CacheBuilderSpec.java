/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ public final class CacheBuilderSpec
/*     */ {
/*  88 */   private static final Splitter KEYS_SPLITTER = Splitter.on(',').trimResults();
/*     */ 
/*     */   
/*  91 */   private static final Splitter KEY_VALUE_SPLITTER = Splitter.on('=').trimResults();
/*     */ 
/*     */ 
/*     */   
/*  95 */   private static final ImmutableMap<String, ValueParser> VALUE_PARSERS = ImmutableMap.builder()
/*  96 */     .put("initialCapacity", new InitialCapacityParser())
/*  97 */     .put("maximumSize", new MaximumSizeParser())
/*  98 */     .put("maximumWeight", new MaximumWeightParser())
/*  99 */     .put("concurrencyLevel", new ConcurrencyLevelParser())
/* 100 */     .put("weakKeys", new KeyStrengthParser(LocalCache.Strength.WEAK))
/* 101 */     .put("softValues", new ValueStrengthParser(LocalCache.Strength.SOFT))
/* 102 */     .put("weakValues", new ValueStrengthParser(LocalCache.Strength.WEAK))
/* 103 */     .put("recordStats", new RecordStatsParser())
/* 104 */     .put("expireAfterAccess", new AccessDurationParser())
/* 105 */     .put("expireAfterWrite", new WriteDurationParser())
/* 106 */     .put("refreshAfterWrite", new RefreshDurationParser())
/* 107 */     .put("refreshInterval", new RefreshDurationParser())
/* 108 */     .build();
/*     */   
/*     */   @VisibleForTesting
/*     */   Integer initialCapacity;
/*     */   
/*     */   @VisibleForTesting
/*     */   Long maximumSize;
/*     */   @VisibleForTesting
/*     */   Long maximumWeight;
/*     */   @VisibleForTesting
/*     */   Integer concurrencyLevel;
/*     */   @VisibleForTesting
/*     */   LocalCache.Strength keyStrength;
/*     */   @VisibleForTesting
/*     */   LocalCache.Strength valueStrength;
/*     */   @VisibleForTesting
/*     */   Boolean recordStats;
/*     */   
/*     */   private CacheBuilderSpec(String specification) {
/* 127 */     this.specification = specification;
/*     */   }
/*     */   @VisibleForTesting
/*     */   long writeExpirationDuration; @VisibleForTesting
/*     */   TimeUnit writeExpirationTimeUnit;
/*     */   @VisibleForTesting
/*     */   long accessExpirationDuration;
/*     */   
/*     */   public static CacheBuilderSpec parse(String cacheBuilderSpecification) {
/* 136 */     CacheBuilderSpec spec = new CacheBuilderSpec(cacheBuilderSpecification);
/* 137 */     if (!cacheBuilderSpecification.isEmpty()) {
/* 138 */       for (String keyValuePair : KEYS_SPLITTER.split(cacheBuilderSpecification)) {
/* 139 */         ImmutableList<String> immutableList = ImmutableList.copyOf(KEY_VALUE_SPLITTER.split(keyValuePair));
/* 140 */         Preconditions.checkArgument(!immutableList.isEmpty(), "blank key-value pair");
/* 141 */         Preconditions.checkArgument(
/* 142 */             (immutableList.size() <= 2), "key-value pair %s with more than one equals sign", keyValuePair);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 147 */         String key = immutableList.get(0);
/* 148 */         ValueParser valueParser = (ValueParser)VALUE_PARSERS.get(key);
/* 149 */         Preconditions.checkArgument((valueParser != null), "unknown key %s", key);
/*     */         
/* 151 */         String value = (immutableList.size() == 1) ? null : immutableList.get(1);
/* 152 */         valueParser.parse(spec, key, value);
/*     */       } 
/*     */     }
/*     */     
/* 156 */     return spec;
/*     */   }
/*     */   @VisibleForTesting
/*     */   TimeUnit accessExpirationTimeUnit; @VisibleForTesting
/*     */   long refreshDuration;
/*     */   public static CacheBuilderSpec disableCaching() {
/* 162 */     return parse("maximumSize=0");
/*     */   }
/*     */   @VisibleForTesting
/*     */   TimeUnit refreshTimeUnit; private final String specification;
/*     */   CacheBuilder<Object, Object> toCacheBuilder() {
/* 167 */     CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
/* 168 */     if (this.initialCapacity != null) {
/* 169 */       builder.initialCapacity(this.initialCapacity.intValue());
/*     */     }
/* 171 */     if (this.maximumSize != null) {
/* 172 */       builder.maximumSize(this.maximumSize.longValue());
/*     */     }
/* 174 */     if (this.maximumWeight != null) {
/* 175 */       builder.maximumWeight(this.maximumWeight.longValue());
/*     */     }
/* 177 */     if (this.concurrencyLevel != null) {
/* 178 */       builder.concurrencyLevel(this.concurrencyLevel.intValue());
/*     */     }
/* 180 */     if (this.keyStrength != null) {
/* 181 */       switch (this.keyStrength) {
/*     */         case WEAK:
/* 183 */           builder.weakKeys();
/*     */           break;
/*     */         default:
/* 186 */           throw new AssertionError();
/*     */       } 
/*     */     }
/* 189 */     if (this.valueStrength != null) {
/* 190 */       switch (this.valueStrength) {
/*     */         case SOFT:
/* 192 */           builder.softValues();
/*     */           break;
/*     */         case WEAK:
/* 195 */           builder.weakValues();
/*     */           break;
/*     */         default:
/* 198 */           throw new AssertionError();
/*     */       } 
/*     */     }
/* 201 */     if (this.recordStats != null && this.recordStats.booleanValue()) {
/* 202 */       builder.recordStats();
/*     */     }
/* 204 */     if (this.writeExpirationTimeUnit != null) {
/* 205 */       builder.expireAfterWrite(this.writeExpirationDuration, this.writeExpirationTimeUnit);
/*     */     }
/* 207 */     if (this.accessExpirationTimeUnit != null) {
/* 208 */       builder.expireAfterAccess(this.accessExpirationDuration, this.accessExpirationTimeUnit);
/*     */     }
/* 210 */     if (this.refreshTimeUnit != null) {
/* 211 */       builder.refreshAfterWrite(this.refreshDuration, this.refreshTimeUnit);
/*     */     }
/*     */     
/* 214 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toParsableString() {
/* 223 */     return this.specification;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 232 */     return MoreObjects.toStringHelper(this).addValue(toParsableString()).toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 237 */     return Objects.hashCode(new Object[] { this.initialCapacity, this.maximumSize, this.maximumWeight, this.concurrencyLevel, this.keyStrength, this.valueStrength, this.recordStats, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 245 */           durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), 
/* 246 */           durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), 
/* 247 */           durationInNanos(this.refreshDuration, this.refreshTimeUnit) });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 252 */     if (this == obj) {
/* 253 */       return true;
/*     */     }
/* 255 */     if (!(obj instanceof CacheBuilderSpec)) {
/* 256 */       return false;
/*     */     }
/* 258 */     CacheBuilderSpec that = (CacheBuilderSpec)obj;
/* 259 */     return (Objects.equal(this.initialCapacity, that.initialCapacity) && 
/* 260 */       Objects.equal(this.maximumSize, that.maximumSize) && 
/* 261 */       Objects.equal(this.maximumWeight, that.maximumWeight) && 
/* 262 */       Objects.equal(this.concurrencyLevel, that.concurrencyLevel) && 
/* 263 */       Objects.equal(this.keyStrength, that.keyStrength) && 
/* 264 */       Objects.equal(this.valueStrength, that.valueStrength) && 
/* 265 */       Objects.equal(this.recordStats, that.recordStats) && 
/* 266 */       Objects.equal(
/* 267 */         durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), 
/* 268 */         durationInNanos(that.writeExpirationDuration, that.writeExpirationTimeUnit)) && 
/* 269 */       Objects.equal(
/* 270 */         durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), 
/* 271 */         durationInNanos(that.accessExpirationDuration, that.accessExpirationTimeUnit)) && 
/* 272 */       Objects.equal(
/* 273 */         durationInNanos(this.refreshDuration, this.refreshTimeUnit), 
/* 274 */         durationInNanos(that.refreshDuration, that.refreshTimeUnit)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Long durationInNanos(long duration, TimeUnit unit) {
/* 282 */     return (unit == null) ? null : Long.valueOf(unit.toNanos(duration));
/*     */   }
/*     */   
/*     */   static abstract class IntegerParser
/*     */     implements ValueParser
/*     */   {
/*     */     protected abstract void parseInteger(CacheBuilderSpec param1CacheBuilderSpec, int param1Int);
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, String value) {
/* 291 */       Preconditions.checkArgument((value != null && !value.isEmpty()), "value of key %s omitted", key);
/*     */       try {
/* 293 */         parseInteger(spec, Integer.parseInt(value));
/* 294 */       } catch (NumberFormatException e) {
/* 295 */         throw new IllegalArgumentException(CacheBuilderSpec
/* 296 */             .format("key %s value set to %s, must be integer", new Object[] { key, value }), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract class LongParser
/*     */     implements ValueParser
/*     */   {
/*     */     protected abstract void parseLong(CacheBuilderSpec param1CacheBuilderSpec, long param1Long);
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, String value) {
/* 307 */       Preconditions.checkArgument((value != null && !value.isEmpty()), "value of key %s omitted", key);
/*     */       try {
/* 309 */         parseLong(spec, Long.parseLong(value));
/* 310 */       } catch (NumberFormatException e) {
/* 311 */         throw new IllegalArgumentException(CacheBuilderSpec
/* 312 */             .format("key %s value set to %s, must be integer", new Object[] { key, value }), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class InitialCapacityParser
/*     */     extends IntegerParser
/*     */   {
/*     */     protected void parseInteger(CacheBuilderSpec spec, int value) {
/* 321 */       Preconditions.checkArgument((spec.initialCapacity == null), "initial capacity was already set to ", spec.initialCapacity);
/*     */ 
/*     */ 
/*     */       
/* 325 */       spec.initialCapacity = Integer.valueOf(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class MaximumSizeParser
/*     */     extends LongParser
/*     */   {
/*     */     protected void parseLong(CacheBuilderSpec spec, long value) {
/* 333 */       Preconditions.checkArgument((spec.maximumSize == null), "maximum size was already set to ", spec.maximumSize);
/* 334 */       Preconditions.checkArgument((spec.maximumWeight == null), "maximum weight was already set to ", spec.maximumWeight);
/*     */       
/* 336 */       spec.maximumSize = Long.valueOf(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class MaximumWeightParser
/*     */     extends LongParser
/*     */   {
/*     */     protected void parseLong(CacheBuilderSpec spec, long value) {
/* 344 */       Preconditions.checkArgument((spec.maximumWeight == null), "maximum weight was already set to ", spec.maximumWeight);
/*     */       
/* 346 */       Preconditions.checkArgument((spec.maximumSize == null), "maximum size was already set to ", spec.maximumSize);
/* 347 */       spec.maximumWeight = Long.valueOf(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class ConcurrencyLevelParser
/*     */     extends IntegerParser
/*     */   {
/*     */     protected void parseInteger(CacheBuilderSpec spec, int value) {
/* 355 */       Preconditions.checkArgument((spec.concurrencyLevel == null), "concurrency level was already set to ", spec.concurrencyLevel);
/*     */ 
/*     */ 
/*     */       
/* 359 */       spec.concurrencyLevel = Integer.valueOf(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class KeyStrengthParser
/*     */     implements ValueParser {
/*     */     private final LocalCache.Strength strength;
/*     */     
/*     */     public KeyStrengthParser(LocalCache.Strength strength) {
/* 368 */       this.strength = strength;
/*     */     }
/*     */ 
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, String value) {
/* 373 */       Preconditions.checkArgument((value == null), "key %s does not take values", key);
/* 374 */       Preconditions.checkArgument((spec.keyStrength == null), "%s was already set to %s", key, spec.keyStrength);
/* 375 */       spec.keyStrength = this.strength;
/*     */     }
/*     */   }
/*     */   
/*     */   static class ValueStrengthParser
/*     */     implements ValueParser {
/*     */     private final LocalCache.Strength strength;
/*     */     
/*     */     public ValueStrengthParser(LocalCache.Strength strength) {
/* 384 */       this.strength = strength;
/*     */     }
/*     */ 
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, String value) {
/* 389 */       Preconditions.checkArgument((value == null), "key %s does not take values", key);
/* 390 */       Preconditions.checkArgument((spec.valueStrength == null), "%s was already set to %s", key, spec.valueStrength);
/*     */ 
/*     */       
/* 393 */       spec.valueStrength = this.strength;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class RecordStatsParser
/*     */     implements ValueParser
/*     */   {
/*     */     public void parse(CacheBuilderSpec spec, String key, String value) {
/* 402 */       Preconditions.checkArgument((value == null), "recordStats does not take values");
/* 403 */       Preconditions.checkArgument((spec.recordStats == null), "recordStats already set");
/* 404 */       spec.recordStats = Boolean.valueOf(true);
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract class DurationParser
/*     */     implements ValueParser
/*     */   {
/*     */     protected abstract void parseDuration(CacheBuilderSpec param1CacheBuilderSpec, long param1Long, TimeUnit param1TimeUnit);
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, String value) {
/* 414 */       Preconditions.checkArgument((value != null && !value.isEmpty()), "value of key %s omitted", key); try {
/*     */         TimeUnit timeUnit;
/* 416 */         char lastChar = value.charAt(value.length() - 1);
/*     */         
/* 418 */         switch (lastChar) {
/*     */           case 'd':
/* 420 */             timeUnit = TimeUnit.DAYS;
/*     */             break;
/*     */           case 'h':
/* 423 */             timeUnit = TimeUnit.HOURS;
/*     */             break;
/*     */           case 'm':
/* 426 */             timeUnit = TimeUnit.MINUTES;
/*     */             break;
/*     */           case 's':
/* 429 */             timeUnit = TimeUnit.SECONDS;
/*     */             break;
/*     */           default:
/* 432 */             throw new IllegalArgumentException(CacheBuilderSpec
/* 433 */                 .format("key %s invalid format.  was %s, must end with one of [dDhHmMsS]", new Object[] { key, value }));
/*     */         } 
/*     */ 
/*     */         
/* 437 */         long duration = Long.parseLong(value.substring(0, value.length() - 1));
/* 438 */         parseDuration(spec, duration, timeUnit);
/* 439 */       } catch (NumberFormatException e) {
/* 440 */         throw new IllegalArgumentException(CacheBuilderSpec
/* 441 */             .format("key %s value set to %s, must be integer", new Object[] { key, value }));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class AccessDurationParser
/*     */     extends DurationParser
/*     */   {
/*     */     protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
/* 450 */       Preconditions.checkArgument((spec.accessExpirationTimeUnit == null), "expireAfterAccess already set");
/* 451 */       spec.accessExpirationDuration = duration;
/* 452 */       spec.accessExpirationTimeUnit = unit;
/*     */     }
/*     */   }
/*     */   
/*     */   static class WriteDurationParser
/*     */     extends DurationParser
/*     */   {
/*     */     protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
/* 460 */       Preconditions.checkArgument((spec.writeExpirationTimeUnit == null), "expireAfterWrite already set");
/* 461 */       spec.writeExpirationDuration = duration;
/* 462 */       spec.writeExpirationTimeUnit = unit;
/*     */     }
/*     */   }
/*     */   
/*     */   static class RefreshDurationParser
/*     */     extends DurationParser
/*     */   {
/*     */     protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
/* 470 */       Preconditions.checkArgument((spec.refreshTimeUnit == null), "refreshAfterWrite already set");
/* 471 */       spec.refreshDuration = duration;
/* 472 */       spec.refreshTimeUnit = unit;
/*     */     }
/*     */   }
/*     */   
/*     */   private static String format(String format, Object... args) {
/* 477 */     return String.format(Locale.ROOT, format, args);
/*     */   }
/*     */   
/*     */   private static interface ValueParser {
/*     */     void parse(CacheBuilderSpec param1CacheBuilderSpec, String param1String1, String param1String2);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\cache\CacheBuilderSpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */