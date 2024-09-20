/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Splitter
/*     */ {
/*     */   private final CharMatcher trimmer;
/*     */   private final boolean omitEmptyStrings;
/*     */   private final Strategy strategy;
/*     */   private final int limit;
/*     */   
/*     */   private Splitter(Strategy strategy) {
/* 109 */     this(strategy, false, CharMatcher.none(), 2147483647);
/*     */   }
/*     */   
/*     */   private Splitter(Strategy strategy, boolean omitEmptyStrings, CharMatcher trimmer, int limit) {
/* 113 */     this.strategy = strategy;
/* 114 */     this.omitEmptyStrings = omitEmptyStrings;
/* 115 */     this.trimmer = trimmer;
/* 116 */     this.limit = limit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Splitter on(char separator) {
/* 127 */     return on(CharMatcher.is(separator));
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
/*     */   public static Splitter on(final CharMatcher separatorMatcher) {
/* 141 */     Preconditions.checkNotNull(separatorMatcher);
/*     */     
/* 143 */     return new Splitter(new Strategy()
/*     */         {
/*     */           public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit)
/*     */           {
/* 147 */             return new Splitter.SplittingIterator(splitter, toSplit)
/*     */               {
/*     */                 int separatorStart(int start) {
/* 150 */                   return separatorMatcher.indexIn(this.toSplit, start);
/*     */                 }
/*     */ 
/*     */                 
/*     */                 int separatorEnd(int separatorPosition) {
/* 155 */                   return separatorPosition + 1;
/*     */                 }
/*     */               };
/*     */           }
/*     */         });
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
/*     */   public static Splitter on(final String separator) {
/* 171 */     Preconditions.checkArgument((separator.length() != 0), "The separator may not be the empty string.");
/* 172 */     if (separator.length() == 1) {
/* 173 */       return on(separator.charAt(0));
/*     */     }
/* 175 */     return new Splitter(new Strategy()
/*     */         {
/*     */           public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit)
/*     */           {
/* 179 */             return new Splitter.SplittingIterator(splitter, toSplit)
/*     */               {
/*     */                 public int separatorStart(int start) {
/* 182 */                   int separatorLength = separator.length();
/*     */ 
/*     */                   
/* 185 */                   for (int p = start, last = this.toSplit.length() - separatorLength; p <= last; p++) {
/* 186 */                     int i = 0; while (true) { if (i < separatorLength) {
/* 187 */                         if (this.toSplit.charAt(i + p) != separator.charAt(i))
/*     */                           break;  i++;
/*     */                         continue;
/*     */                       } 
/* 191 */                       return p; }
/*     */                   
/* 193 */                   }  return -1;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public int separatorEnd(int separatorPosition) {
/* 198 */                   return separatorPosition + separator.length();
/*     */                 }
/*     */               };
/*     */           }
/*     */         });
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
/*     */   @GwtIncompatible
/*     */   public static Splitter on(Pattern separatorPattern) {
/* 217 */     return on(new JdkPattern(separatorPattern));
/*     */   }
/*     */   
/*     */   private static Splitter on(final CommonPattern separatorPattern) {
/* 221 */     Preconditions.checkArgument(
/* 222 */         !separatorPattern.matcher("").matches(), "The pattern may not match the empty string: %s", separatorPattern);
/*     */ 
/*     */ 
/*     */     
/* 226 */     return new Splitter(new Strategy()
/*     */         {
/*     */           public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit)
/*     */           {
/* 230 */             final CommonMatcher matcher = separatorPattern.matcher(toSplit);
/* 231 */             return new Splitter.SplittingIterator(this, splitter, toSplit)
/*     */               {
/*     */                 public int separatorStart(int start) {
/* 234 */                   return matcher.find(start) ? matcher.start() : -1;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public int separatorEnd(int separatorPosition) {
/* 239 */                   return matcher.end();
/*     */                 }
/*     */               };
/*     */           }
/*     */         });
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
/*     */   @GwtIncompatible
/*     */   public static Splitter onPattern(String separatorPattern) {
/* 260 */     return on(Platform.compilePattern(separatorPattern));
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
/*     */   public static Splitter fixedLength(final int length) {
/* 282 */     Preconditions.checkArgument((length > 0), "The length may not be less than 1");
/*     */     
/* 284 */     return new Splitter(new Strategy()
/*     */         {
/*     */           public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit)
/*     */           {
/* 288 */             return new Splitter.SplittingIterator(splitter, toSplit)
/*     */               {
/*     */                 public int separatorStart(int start) {
/* 291 */                   int nextChunkStart = start + length;
/* 292 */                   return (nextChunkStart < this.toSplit.length()) ? nextChunkStart : -1;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public int separatorEnd(int separatorPosition) {
/* 297 */                   return separatorPosition;
/*     */                 }
/*     */               };
/*     */           }
/*     */         });
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
/*     */   public Splitter omitEmptyStrings() {
/* 321 */     return new Splitter(this.strategy, true, this.trimmer, this.limit);
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
/*     */   public Splitter limit(int maxItems) {
/* 341 */     Preconditions.checkArgument((maxItems > 0), "must be greater than zero: %s", maxItems);
/* 342 */     return new Splitter(this.strategy, this.omitEmptyStrings, this.trimmer, maxItems);
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
/*     */   public Splitter trimResults() {
/* 355 */     return trimResults(CharMatcher.whitespace());
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
/*     */   public Splitter trimResults(CharMatcher trimmer) {
/* 370 */     Preconditions.checkNotNull(trimmer);
/* 371 */     return new Splitter(this.strategy, this.omitEmptyStrings, trimmer, this.limit);
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
/*     */   public Iterable<String> split(final CharSequence sequence) {
/* 383 */     Preconditions.checkNotNull(sequence);
/*     */     
/* 385 */     return new Iterable<String>()
/*     */       {
/*     */         public Iterator<String> iterator() {
/* 388 */           return Splitter.this.splittingIterator(sequence);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 393 */           return Joiner.on(", ")
/* 394 */             .appendTo((new StringBuilder()).append('['), this)
/* 395 */             .append(']')
/* 396 */             .toString();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private Iterator<String> splittingIterator(CharSequence sequence) {
/* 402 */     return this.strategy.iterator(this, sequence);
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
/*     */   public List<String> splitToList(CharSequence sequence) {
/* 414 */     Preconditions.checkNotNull(sequence);
/*     */     
/* 416 */     Iterator<String> iterator = splittingIterator(sequence);
/* 417 */     List<String> result = new ArrayList<>();
/*     */     
/* 419 */     while (iterator.hasNext()) {
/* 420 */       result.add(iterator.next());
/*     */     }
/*     */     
/* 423 */     return Collections.unmodifiableList(result);
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
/*     */   @Beta
/*     */   public Stream<String> splitToStream(CharSequence sequence) {
/* 438 */     return StreamSupport.stream(split(sequence).spliterator(), false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public MapSplitter withKeyValueSeparator(String separator) {
/* 449 */     return withKeyValueSeparator(on(separator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public MapSplitter withKeyValueSeparator(char separator) {
/* 460 */     return withKeyValueSeparator(on(separator));
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
/*     */   @Beta
/*     */   public MapSplitter withKeyValueSeparator(Splitter keyValueSplitter) {
/* 484 */     return new MapSplitter(this, keyValueSplitter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static final class MapSplitter
/*     */   {
/*     */     private static final String INVALID_ENTRY_MESSAGE = "Chunk [%s] is not a valid entry";
/*     */ 
/*     */     
/*     */     private final Splitter outerSplitter;
/*     */ 
/*     */     
/*     */     private final Splitter entrySplitter;
/*     */ 
/*     */     
/*     */     private MapSplitter(Splitter outerSplitter, Splitter entrySplitter) {
/* 502 */       this.outerSplitter = outerSplitter;
/* 503 */       this.entrySplitter = Preconditions.<Splitter>checkNotNull(entrySplitter);
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
/*     */     public Map<String, String> split(CharSequence sequence) {
/* 518 */       Map<String, String> map = new LinkedHashMap<>();
/* 519 */       for (String entry : this.outerSplitter.split(sequence)) {
/* 520 */         Iterator<String> entryFields = this.entrySplitter.splittingIterator(entry);
/*     */         
/* 522 */         Preconditions.checkArgument(entryFields.hasNext(), "Chunk [%s] is not a valid entry", entry);
/* 523 */         String key = entryFields.next();
/* 524 */         Preconditions.checkArgument(!map.containsKey(key), "Duplicate key [%s] found.", key);
/*     */         
/* 526 */         Preconditions.checkArgument(entryFields.hasNext(), "Chunk [%s] is not a valid entry", entry);
/* 527 */         String value = entryFields.next();
/* 528 */         map.put(key, value);
/*     */         
/* 530 */         Preconditions.checkArgument(!entryFields.hasNext(), "Chunk [%s] is not a valid entry", entry);
/*     */       } 
/* 532 */       return Collections.unmodifiableMap(map);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static interface Strategy
/*     */   {
/*     */     Iterator<String> iterator(Splitter param1Splitter, CharSequence param1CharSequence);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class SplittingIterator
/*     */     extends AbstractIterator<String>
/*     */   {
/*     */     final CharSequence toSplit;
/*     */ 
/*     */     
/*     */     final CharMatcher trimmer;
/*     */ 
/*     */     
/*     */     final boolean omitEmptyStrings;
/*     */ 
/*     */     
/* 557 */     int offset = 0;
/*     */     int limit;
/*     */     
/*     */     protected SplittingIterator(Splitter splitter, CharSequence toSplit) {
/* 561 */       this.trimmer = splitter.trimmer;
/* 562 */       this.omitEmptyStrings = splitter.omitEmptyStrings;
/* 563 */       this.limit = splitter.limit;
/* 564 */       this.toSplit = toSplit;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected String computeNext() {
/* 574 */       int nextStart = this.offset;
/* 575 */       while (this.offset != -1) {
/* 576 */         int end, start = nextStart;
/*     */ 
/*     */         
/* 579 */         int separatorPosition = separatorStart(this.offset);
/* 580 */         if (separatorPosition == -1) {
/* 581 */           end = this.toSplit.length();
/* 582 */           this.offset = -1;
/*     */         } else {
/* 584 */           end = separatorPosition;
/* 585 */           this.offset = separatorEnd(separatorPosition);
/*     */         } 
/* 587 */         if (this.offset == nextStart) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 594 */           this.offset++;
/* 595 */           if (this.offset > this.toSplit.length()) {
/* 596 */             this.offset = -1;
/*     */           }
/*     */           
/*     */           continue;
/*     */         } 
/* 601 */         while (start < end && this.trimmer.matches(this.toSplit.charAt(start))) {
/* 602 */           start++;
/*     */         }
/* 604 */         while (end > start && this.trimmer.matches(this.toSplit.charAt(end - 1))) {
/* 605 */           end--;
/*     */         }
/*     */         
/* 608 */         if (this.omitEmptyStrings && start == end) {
/*     */           
/* 610 */           nextStart = this.offset;
/*     */           
/*     */           continue;
/*     */         } 
/* 614 */         if (this.limit == 1) {
/*     */ 
/*     */ 
/*     */           
/* 618 */           end = this.toSplit.length();
/* 619 */           this.offset = -1;
/*     */           
/* 621 */           while (end > start && this.trimmer.matches(this.toSplit.charAt(end - 1))) {
/* 622 */             end--;
/*     */           }
/*     */         } else {
/* 625 */           this.limit--;
/*     */         } 
/*     */         
/* 628 */         return this.toSplit.subSequence(start, end).toString();
/*     */       } 
/* 630 */       return endOfData();
/*     */     }
/*     */     
/*     */     abstract int separatorStart(int param1Int);
/*     */     
/*     */     abstract int separatorEnd(int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\base\Splitter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */