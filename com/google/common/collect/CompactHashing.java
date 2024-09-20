/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class CompactHashing
/*     */ {
/*     */   static final byte UNSET = 0;
/*     */   private static final int HASH_TABLE_BITS_MAX_BITS = 5;
/*     */   static final int MODIFICATION_COUNT_INCREMENT = 32;
/*     */   static final int HASH_TABLE_BITS_MASK = 31;
/*     */   static final int MAX_SIZE = 1073741823;
/*     */   static final int DEFAULT_SIZE = 3;
/*     */   private static final int MIN_HASH_TABLE_SIZE = 4;
/*     */   private static final int BYTE_MAX_SIZE = 256;
/*     */   private static final int BYTE_MASK = 255;
/*     */   private static final int SHORT_MAX_SIZE = 65536;
/*     */   private static final int SHORT_MASK = 65535;
/*     */   
/*     */   static int tableSize(int expectedSize) {
/*  70 */     return Math.max(4, Hashing.closedTableSize(expectedSize + 1, 1.0D));
/*     */   }
/*     */ 
/*     */   
/*     */   static Object createTable(int buckets) {
/*  75 */     if (buckets < 2 || buckets > 1073741824 || 
/*     */       
/*  77 */       Integer.highestOneBit(buckets) != buckets) {
/*  78 */       throw new IllegalArgumentException((new StringBuilder(52)).append("must be power of 2 between 2^1 and 2^30: ").append(buckets).toString());
/*     */     }
/*  80 */     if (buckets <= 256)
/*  81 */       return new byte[buckets]; 
/*  82 */     if (buckets <= 65536) {
/*  83 */       return new short[buckets];
/*     */     }
/*  85 */     return new int[buckets];
/*     */   }
/*     */ 
/*     */   
/*     */   static void tableClear(Object table) {
/*  90 */     if (table instanceof byte[]) {
/*  91 */       Arrays.fill((byte[])table, (byte)0);
/*  92 */     } else if (table instanceof short[]) {
/*  93 */       Arrays.fill((short[])table, (short)0);
/*     */     } else {
/*  95 */       Arrays.fill((int[])table, 0);
/*     */     } 
/*     */   }
/*     */   
/*     */   static int tableGet(Object table, int index) {
/* 100 */     if (table instanceof byte[])
/* 101 */       return ((byte[])table)[index] & 0xFF; 
/* 102 */     if (table instanceof short[]) {
/* 103 */       return ((short[])table)[index] & 0xFFFF;
/*     */     }
/* 105 */     return ((int[])table)[index];
/*     */   }
/*     */ 
/*     */   
/*     */   static void tableSet(Object table, int index, int entry) {
/* 110 */     if (table instanceof byte[]) {
/* 111 */       ((byte[])table)[index] = (byte)entry;
/* 112 */     } else if (table instanceof short[]) {
/* 113 */       ((short[])table)[index] = (short)entry;
/*     */     } else {
/* 115 */       ((int[])table)[index] = entry;
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
/*     */   static int newCapacity(int mask) {
/* 127 */     return ((mask < 32) ? 4 : 2) * (mask + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   static int getHashPrefix(int value, int mask) {
/* 132 */     return value & (mask ^ 0xFFFFFFFF);
/*     */   }
/*     */ 
/*     */   
/*     */   static int getNext(int entry, int mask) {
/* 137 */     return entry & mask;
/*     */   }
/*     */ 
/*     */   
/*     */   static int maskCombine(int prefix, int suffix, int mask) {
/* 142 */     return prefix & (mask ^ 0xFFFFFFFF) | suffix & mask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int remove(Object key, Object value, int mask, Object table, int[] entries, Object[] keys, Object[] values) {
/* 153 */     int hash = Hashing.smearedHash(key);
/* 154 */     int tableIndex = hash & mask;
/* 155 */     int next = tableGet(table, tableIndex);
/* 156 */     if (next == 0) {
/* 157 */       return -1;
/*     */     }
/* 159 */     int hashPrefix = getHashPrefix(hash, mask);
/* 160 */     int lastEntryIndex = -1;
/*     */     while (true) {
/* 162 */       int entryIndex = next - 1;
/* 163 */       int entry = entries[entryIndex];
/* 164 */       if (getHashPrefix(entry, mask) == hashPrefix && 
/* 165 */         Objects.equal(key, keys[entryIndex]) && (values == null || 
/* 166 */         Objects.equal(value, values[entryIndex]))) {
/* 167 */         int newNext = getNext(entry, mask);
/* 168 */         if (lastEntryIndex == -1) {
/*     */           
/* 170 */           tableSet(table, tableIndex, newNext);
/*     */         } else {
/*     */           
/* 173 */           entries[lastEntryIndex] = maskCombine(entries[lastEntryIndex], newNext, mask);
/*     */         } 
/*     */         
/* 176 */         return entryIndex;
/*     */       } 
/* 178 */       lastEntryIndex = entryIndex;
/* 179 */       next = getNext(entry, mask);
/* 180 */       if (next == 0)
/* 181 */         return -1; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\CompactHashing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */