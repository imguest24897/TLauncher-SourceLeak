/*     */ package org.apache.commons.lang3.stream;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BinaryOperator;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.function.Failable;
/*     */ import org.apache.commons.lang3.function.FailableConsumer;
/*     */ import org.apache.commons.lang3.function.FailableFunction;
/*     */ import org.apache.commons.lang3.function.FailablePredicate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Streams
/*     */ {
/*     */   public static class ArrayCollector<E>
/*     */     implements Collector<E, List<E>, E[]>
/*     */   {
/*  90 */     private static final Set<Collector.Characteristics> characteristics = Collections.emptySet();
/*     */ 
/*     */ 
/*     */     
/*     */     private final Class<E> elementType;
/*     */ 
/*     */ 
/*     */     
/*     */     public ArrayCollector(Class<E> elementType) {
/*  99 */       this.elementType = Objects.<Class<E>>requireNonNull(elementType, "elementType");
/*     */     }
/*     */ 
/*     */     
/*     */     public BiConsumer<List<E>, E> accumulator() {
/* 104 */       return List::add;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Collector.Characteristics> characteristics() {
/* 109 */       return characteristics;
/*     */     }
/*     */ 
/*     */     
/*     */     public BinaryOperator<List<E>> combiner() {
/* 114 */       return (left, right) -> {
/*     */           left.addAll(right);
/*     */           return left;
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public Function<List<E>, E[]> finisher() {
/* 122 */       return list -> list.toArray(ArrayUtils.newInstance(this.elementType, list.size()));
/*     */     }
/*     */ 
/*     */     
/*     */     public Supplier<List<E>> supplier() {
/* 127 */       return java.util.ArrayList::new;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class EnumerationSpliterator<T>
/*     */     extends Spliterators.AbstractSpliterator<T>
/*     */   {
/*     */     private final Enumeration<T> enumeration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected EnumerationSpliterator(long estimatedSize, int additionalCharacteristics, Enumeration<T> enumeration) {
/* 149 */       super(estimatedSize, additionalCharacteristics);
/* 150 */       this.enumeration = Objects.<Enumeration<T>>requireNonNull(enumeration, "enumeration");
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEachRemaining(Consumer<? super T> action) {
/* 155 */       while (this.enumeration.hasMoreElements()) {
/* 156 */         next(action);
/*     */       }
/*     */     }
/*     */     
/*     */     private boolean next(Consumer<? super T> action) {
/* 161 */       action.accept(this.enumeration.nextElement());
/* 162 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean tryAdvance(Consumer<? super T> action) {
/* 168 */       return (this.enumeration.hasMoreElements() && next(action));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class FailableStream<T>
/*     */   {
/*     */     private Stream<T> stream;
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean terminated;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public FailableStream(Stream<T> stream) {
/* 188 */       this.stream = stream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean allMatch(FailablePredicate<T, ?> predicate) {
/* 209 */       assertNotTerminated();
/* 210 */       return stream().allMatch(Failable.asPredicate(predicate));
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean anyMatch(FailablePredicate<T, ?> predicate) {
/* 229 */       assertNotTerminated();
/* 230 */       return stream().anyMatch(Failable.asPredicate(predicate));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void assertNotTerminated() {
/* 239 */       if (this.terminated) {
/* 240 */         throw new IllegalStateException("This stream is already terminated.");
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <A, R> R collect(Collector<? super T, A, R> collector) {
/* 302 */       makeTerminated();
/* 303 */       return stream().collect(collector);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <A, R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
/* 359 */       makeTerminated();
/* 360 */       return stream().collect(supplier, accumulator, combiner);
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
/*     */     public FailableStream<T> filter(FailablePredicate<T, ?> predicate) {
/* 375 */       assertNotTerminated();
/* 376 */       this.stream = this.stream.filter(Failable.asPredicate(predicate));
/* 377 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void forEach(FailableConsumer<T, ?> action) {
/* 397 */       makeTerminated();
/* 398 */       stream().forEach(Failable.asConsumer(action));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void makeTerminated() {
/* 407 */       assertNotTerminated();
/* 408 */       this.terminated = true;
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
/*     */     public <R> FailableStream<R> map(FailableFunction<T, R, ?> mapper) {
/* 423 */       assertNotTerminated();
/* 424 */       return new FailableStream(this.stream.map(Failable.asFunction(mapper)));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public T reduce(T identity, BinaryOperator<T> accumulator) {
/* 480 */       makeTerminated();
/* 481 */       return stream().reduce(identity, accumulator);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Stream<T> stream() {
/* 490 */       return this.stream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> FailableStream<T> failableStream(Collection<T> stream) {
/* 535 */     return failableStream(of(stream));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> FailableStream<T> failableStream(Stream<T> stream) {
/* 579 */     return new FailableStream<>(stream);
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
/*     */   public static <E> Stream<E> instancesOf(Class<? super E> clazz, Collection<? super E> collection) {
/* 598 */     return instancesOf(clazz, of(collection));
/*     */   }
/*     */ 
/*     */   
/*     */   private static <E> Stream<E> instancesOf(Class<? super E> clazz, Stream<?> stream) {
/* 603 */     return of((Stream)stream).filter(clazz::isInstance);
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
/*     */   public static <E> Stream<E> nonNull(Collection<E> collection) {
/* 615 */     return of(collection).filter(Objects::nonNull);
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
/*     */   @SafeVarargs
/*     */   public static <E> Stream<E> nonNull(E... array) {
/* 628 */     return nonNull(of(array));
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
/*     */   public static <E> Stream<E> nonNull(Stream<E> stream) {
/* 640 */     return of(stream).filter(Objects::nonNull);
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
/*     */   public static <E> Stream<E> of(Collection<E> collection) {
/* 652 */     return (collection == null) ? Stream.<E>empty() : collection.stream();
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
/*     */   public static <E> Stream<E> of(Enumeration<E> enumeration) {
/* 664 */     return StreamSupport.stream(new EnumerationSpliterator<>(Long.MAX_VALUE, 16, enumeration), false);
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
/*     */   public static <E> Stream<E> of(Iterable<E> iterable) {
/* 676 */     return (iterable == null) ? Stream.<E>empty() : StreamSupport.<E>stream(iterable.spliterator(), false);
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
/*     */   public static <E> Stream<E> of(Iterator<E> iterator) {
/* 688 */     return (iterator == null) ? Stream.<E>empty() : StreamSupport.<E>stream(Spliterators.spliteratorUnknownSize(iterator, 16), false);
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
/*     */   private static <E> Stream<E> of(Stream<E> stream) {
/* 700 */     return (stream == null) ? Stream.<E>empty() : stream;
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
/*     */   @SafeVarargs
/*     */   public static <T> Stream<T> of(T... values) {
/* 713 */     return (values == null) ? Stream.<T>empty() : Stream.<T>of(values);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static <E> FailableStream<E> stream(Collection<E> collection) {
/* 758 */     return failableStream(collection);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static <T> FailableStream<T> stream(Stream<T> stream) {
/* 803 */     return failableStream(stream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Collector<T, ?, T[]> toArray(Class<T> pElementType) {
/* 814 */     return new ArrayCollector<>(pElementType);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\stream\Streams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */