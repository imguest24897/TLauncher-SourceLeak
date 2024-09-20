/*     */ package org.apache.commons.lang3.stream;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.StringJoiner;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BinaryOperator;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
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
/*     */ public final class LangCollectors
/*     */ {
/*     */   private static class SimpleCollector<T, A, R>
/*     */     implements Collector<T, A, R>
/*     */   {
/*     */     private final BiConsumer<A, T> accumulator;
/*     */     private final Set<Collector.Characteristics> characteristics;
/*     */     private final BinaryOperator<A> combiner;
/*     */     private final Function<A, R> finisher;
/*     */     private final Supplier<A> supplier;
/*     */     
/*     */     private SimpleCollector(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner, Function<A, R> finisher, Set<Collector.Characteristics> characteristics) {
/*  59 */       this.supplier = supplier;
/*  60 */       this.accumulator = accumulator;
/*  61 */       this.combiner = combiner;
/*  62 */       this.finisher = finisher;
/*  63 */       this.characteristics = characteristics;
/*     */     }
/*     */ 
/*     */     
/*     */     public BiConsumer<A, T> accumulator() {
/*  68 */       return this.accumulator;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Collector.Characteristics> characteristics() {
/*  73 */       return this.characteristics;
/*     */     }
/*     */ 
/*     */     
/*     */     public BinaryOperator<A> combiner() {
/*  78 */       return this.combiner;
/*     */     }
/*     */ 
/*     */     
/*     */     public Function<A, R> finisher() {
/*  83 */       return this.finisher;
/*     */     }
/*     */ 
/*     */     
/*     */     public Supplier<A> supplier() {
/*  88 */       return this.supplier;
/*     */     }
/*     */   }
/*     */   
/*  92 */   private static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Collector<Object, ?, String> joining() {
/* 105 */     return new SimpleCollector<>(StringBuilder::new, StringBuilder::append, StringBuilder::append, StringBuilder::toString, CH_NOID);
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
/*     */   public static Collector<Object, ?, String> joining(CharSequence delimiter) {
/* 121 */     return joining(delimiter, "", "");
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
/*     */   public static Collector<Object, ?, String> joining(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
/* 139 */     return joining(delimiter, prefix, suffix, Objects::toString);
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
/*     */   public static Collector<Object, ?, String> joining(CharSequence delimiter, CharSequence prefix, CharSequence suffix, Function<Object, String> toString) {
/* 159 */     return new SimpleCollector<>(() -> new StringJoiner(delimiter, prefix, suffix), (a, t) -> a.add(toString.apply(t)), StringJoiner::merge, StringJoiner::toString, CH_NOID);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\stream\LangCollectors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */