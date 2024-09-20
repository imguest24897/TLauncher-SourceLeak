/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Ordering;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ @Beta
/*     */ public final class ElementOrder<T>
/*     */ {
/*     */   private final Type type;
/*     */   private final Comparator<T> comparator;
/*     */   
/*     */   public enum Type
/*     */   {
/*  67 */     UNORDERED,
/*  68 */     STABLE,
/*  69 */     INSERTION,
/*  70 */     SORTED;
/*     */   }
/*     */   
/*     */   private ElementOrder(Type type, Comparator<T> comparator) {
/*  74 */     this.type = (Type)Preconditions.checkNotNull(type);
/*  75 */     this.comparator = comparator;
/*  76 */     Preconditions.checkState((((type == Type.SORTED) ? true : false) == ((comparator != null) ? true : false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static <S> ElementOrder<S> unordered() {
/*  81 */     return new ElementOrder<>(Type.UNORDERED, null);
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
/*     */   public static <S> ElementOrder<S> stable() {
/* 121 */     return new ElementOrder<>(Type.STABLE, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <S> ElementOrder<S> insertion() {
/* 126 */     return new ElementOrder<>(Type.INSERTION, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <S extends Comparable<? super S>> ElementOrder<S> natural() {
/* 133 */     return new ElementOrder<>(Type.SORTED, (Comparator<S>)Ordering.natural());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <S> ElementOrder<S> sorted(Comparator<S> comparator) {
/* 141 */     return new ElementOrder<>(Type.SORTED, (Comparator<S>)Preconditions.checkNotNull(comparator));
/*     */   }
/*     */ 
/*     */   
/*     */   public Type type() {
/* 146 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<T> comparator() {
/* 155 */     if (this.comparator != null) {
/* 156 */       return this.comparator;
/*     */     }
/* 158 */     throw new UnsupportedOperationException("This ordering does not define a comparator.");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 163 */     if (obj == this) {
/* 164 */       return true;
/*     */     }
/* 166 */     if (!(obj instanceof ElementOrder)) {
/* 167 */       return false;
/*     */     }
/*     */     
/* 170 */     ElementOrder<?> other = (ElementOrder)obj;
/* 171 */     return (this.type == other.type && Objects.equal(this.comparator, other.comparator));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 176 */     return Objects.hashCode(new Object[] { this.type, this.comparator });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 181 */     MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this).add("type", this.type);
/* 182 */     if (this.comparator != null) {
/* 183 */       helper.add("comparator", this.comparator);
/*     */     }
/* 185 */     return helper.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   <K extends T, V> Map<K, V> createMap(int expectedSize) {
/* 190 */     switch (this.type) {
/*     */       case UNORDERED:
/* 192 */         return Maps.newHashMapWithExpectedSize(expectedSize);
/*     */       case INSERTION:
/*     */       case STABLE:
/* 195 */         return Maps.newLinkedHashMapWithExpectedSize(expectedSize);
/*     */       case SORTED:
/* 197 */         return Maps.newTreeMap(comparator());
/*     */     } 
/* 199 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   <T1 extends T> ElementOrder<T1> cast() {
/* 205 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\graph\ElementOrder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */