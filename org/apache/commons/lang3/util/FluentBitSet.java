/*     */ package org.apache.commons.lang3.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.BitSet;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.IntStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FluentBitSet
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final BitSet bitSet;
/*     */   
/*     */   public FluentBitSet() {
/*  44 */     this(new BitSet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FluentBitSet(BitSet set) {
/*  53 */     this.bitSet = Objects.<BitSet>requireNonNull(set, "set");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FluentBitSet(int nbits) {
/*  64 */     this(new BitSet(nbits));
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
/*     */   public FluentBitSet and(BitSet set) {
/*  76 */     this.bitSet.and(set);
/*  77 */     return this;
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
/*     */   public FluentBitSet and(FluentBitSet set) {
/*  89 */     this.bitSet.and(set.bitSet);
/*  90 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FluentBitSet andNot(BitSet set) {
/* 100 */     this.bitSet.andNot(set);
/* 101 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FluentBitSet andNot(FluentBitSet set) {
/* 111 */     this.bitSet.andNot(set.bitSet);
/* 112 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BitSet bitSet() {
/* 121 */     return this.bitSet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int cardinality() {
/* 130 */     return this.bitSet.cardinality();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FluentBitSet clear() {
/* 139 */     this.bitSet.clear();
/* 140 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FluentBitSet clear(int... bitIndexArray) {
/* 151 */     for (int e : bitIndexArray) {
/* 152 */       this.bitSet.clear(e);
/*     */     }
/* 154 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FluentBitSet clear(int bitIndex) {
/* 165 */     this.bitSet.clear(bitIndex);
/* 166 */     return this;
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
/*     */   public FluentBitSet clear(int fromIndex, int toIndex) {
/* 180 */     this.bitSet.clear(fromIndex, toIndex);
/* 181 */     return this;
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
/*     */   public Object clone() {
/* 193 */     return new FluentBitSet((BitSet)this.bitSet.clone());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 198 */     if (this == obj) {
/* 199 */       return true;
/*     */     }
/* 201 */     if (!(obj instanceof FluentBitSet)) {
/* 202 */       return false;
/*     */     }
/* 204 */     FluentBitSet other = (FluentBitSet)obj;
/* 205 */     return Objects.equals(this.bitSet, other.bitSet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FluentBitSet flip(int bitIndex) {
/* 216 */     this.bitSet.flip(bitIndex);
/* 217 */     return this;
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
/*     */   public FluentBitSet flip(int fromIndex, int toIndex) {
/* 231 */     this.bitSet.flip(fromIndex, toIndex);
/* 232 */     return this;
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
/*     */   public boolean get(int bitIndex) {
/* 244 */     return this.bitSet.get(bitIndex);
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
/*     */   public FluentBitSet get(int fromIndex, int toIndex) {
/* 258 */     return new FluentBitSet(this.bitSet.get(fromIndex, toIndex));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 263 */     return this.bitSet.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean intersects(BitSet set) {
/* 274 */     return this.bitSet.intersects(set);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean intersects(FluentBitSet set) {
/* 285 */     return this.bitSet.intersects(set.bitSet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 294 */     return this.bitSet.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 304 */     return this.bitSet.length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int nextClearBit(int fromIndex) {
/* 315 */     return this.bitSet.nextClearBit(fromIndex);
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
/*     */   public int nextSetBit(int fromIndex) {
/* 340 */     return this.bitSet.nextSetBit(fromIndex);
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
/*     */   public FluentBitSet or(BitSet set) {
/* 352 */     this.bitSet.or(set);
/* 353 */     return this;
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
/*     */   public FluentBitSet or(FluentBitSet... set) {
/* 365 */     for (FluentBitSet e : set) {
/* 366 */       this.bitSet.or(e.bitSet);
/*     */     }
/* 368 */     return this;
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
/*     */   public FluentBitSet or(FluentBitSet set) {
/* 380 */     this.bitSet.or(set.bitSet);
/* 381 */     return this;
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
/*     */   public int previousClearBit(int fromIndex) {
/* 393 */     return this.bitSet.previousClearBit(fromIndex);
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
/*     */   public int previousSetBit(int fromIndex) {
/* 415 */     return this.bitSet.previousSetBit(fromIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FluentBitSet set(int... bitIndexArray) {
/* 426 */     for (int e : bitIndexArray) {
/* 427 */       this.bitSet.set(e);
/*     */     }
/* 429 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FluentBitSet set(int bitIndex) {
/* 440 */     this.bitSet.set(bitIndex);
/* 441 */     return this;
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
/*     */   public FluentBitSet set(int bitIndex, boolean value) {
/* 453 */     this.bitSet.set(bitIndex, value);
/* 454 */     return this;
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
/*     */   public FluentBitSet set(int fromIndex, int toIndex) {
/* 468 */     this.bitSet.set(fromIndex, toIndex);
/* 469 */     return this;
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
/*     */   public FluentBitSet set(int fromIndex, int toIndex, boolean value) {
/* 484 */     this.bitSet.set(fromIndex, toIndex, value);
/* 485 */     return this;
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
/*     */   public FluentBitSet setInclusive(int fromIndex, int toIndex) {
/* 499 */     this.bitSet.set(fromIndex, toIndex + 1);
/* 500 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 510 */     return this.bitSet.size();
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
/*     */   public IntStream stream() {
/* 527 */     return this.bitSet.stream();
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
/*     */   public byte[] toByteArray() {
/* 546 */     return this.bitSet.toByteArray();
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
/*     */   public long[] toLongArray() {
/* 565 */     return this.bitSet.toLongArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 570 */     return this.bitSet.toString();
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
/*     */   public FluentBitSet xor(BitSet set) {
/* 587 */     this.bitSet.xor(set);
/* 588 */     return this;
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
/*     */   public FluentBitSet xor(FluentBitSet set) {
/* 605 */     this.bitSet.xor(set.bitSet);
/* 606 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang\\util\FluentBitSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */