/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Comparators
/*     */ {
/*     */   @Beta
/*     */   public static <T, S extends T> Comparator<Iterable<S>> lexicographical(Comparator<T> comparator) {
/*  66 */     return new LexicographicalOrdering<>((Comparator<? super S>)Preconditions.checkNotNull(comparator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static <T> boolean isInOrder(Iterable<? extends T> iterable, Comparator<T> comparator) {
/*  76 */     Preconditions.checkNotNull(comparator);
/*  77 */     Iterator<? extends T> it = iterable.iterator();
/*  78 */     if (it.hasNext()) {
/*  79 */       T prev = it.next();
/*  80 */       while (it.hasNext()) {
/*  81 */         T next = it.next();
/*  82 */         if (comparator.compare(prev, next) > 0) {
/*  83 */           return false;
/*     */         }
/*  85 */         prev = next;
/*     */       } 
/*     */     } 
/*  88 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static <T> boolean isInStrictOrder(Iterable<? extends T> iterable, Comparator<T> comparator) {
/*  99 */     Preconditions.checkNotNull(comparator);
/* 100 */     Iterator<? extends T> it = iterable.iterator();
/* 101 */     if (it.hasNext()) {
/* 102 */       T prev = it.next();
/* 103 */       while (it.hasNext()) {
/* 104 */         T next = it.next();
/* 105 */         if (comparator.compare(prev, next) >= 0) {
/* 106 */           return false;
/*     */         }
/* 108 */         prev = next;
/*     */       } 
/*     */     } 
/* 111 */     return true;
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
/*     */   public static <T> Collector<T, ?, List<T>> least(int k, Comparator<? super T> comparator) {
/* 135 */     CollectPreconditions.checkNonnegative(k, "k");
/* 136 */     Preconditions.checkNotNull(comparator);
/* 137 */     return Collector.of(() -> TopKSelector.least(k, comparator), TopKSelector::offer, TopKSelector::combine, TopKSelector::topK, new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
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
/*     */   public static <T> Collector<T, ?, List<T>> greatest(int k, Comparator<? super T> comparator) {
/* 166 */     return least(k, comparator.reversed());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static <T> Comparator<Optional<T>> emptiesFirst(Comparator<? super T> valueComparator) {
/* 178 */     Preconditions.checkNotNull(valueComparator);
/* 179 */     return Comparator.comparing(o -> o.orElse(null), Comparator.nullsFirst(valueComparator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static <T> Comparator<Optional<T>> emptiesLast(Comparator<? super T> valueComparator) {
/* 191 */     Preconditions.checkNotNull(valueComparator);
/* 192 */     return Comparator.comparing(o -> o.orElse(null), Comparator.nullsLast(valueComparator));
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
/*     */   @Beta
/*     */   public static <T extends Comparable<? super T>> T min(T a, T b) {
/* 210 */     return (a.compareTo(b) <= 0) ? a : b;
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
/*     */   @Beta
/*     */   public static <T> T min(T a, T b, Comparator<T> comparator) {
/* 230 */     return (comparator.compare(a, b) <= 0) ? a : b;
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
/*     */   @Beta
/*     */   public static <T extends Comparable<? super T>> T max(T a, T b) {
/* 248 */     return (a.compareTo(b) >= 0) ? a : b;
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
/*     */   @Beta
/*     */   public static <T> T max(T a, T b, Comparator<T> comparator) {
/* 268 */     return (comparator.compare(a, b) >= 0) ? a : b;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\Comparators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */