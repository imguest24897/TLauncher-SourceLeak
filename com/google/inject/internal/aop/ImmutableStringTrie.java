/*     */ package com.google.inject.internal.aop;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.function.ToIntFunction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ImmutableStringTrie
/*     */   implements ToIntFunction<String>
/*     */ {
/*     */   private static final char LEAF_MARKER = '耀';
/*     */   private static final char BUD_MARKER = '䀀';
/*     */   private static final int MAX_ROWS_PER_TRIE = 16384;
/*     */   private final char[] trie;
/*     */   
/*     */   private static int singletonTrie(String key) {
/*  92 */     return 0;
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
/*     */   public int applyAsInt(String key) {
/* 116 */     int keyLength = key.length();
/*     */     
/* 118 */     int keyIndex = 0;
/* 119 */     int dataIndex = 0;
/* 120 */     char[] data = this.trie;
/*     */     
/* 122 */     while (keyIndex < keyLength) {
/*     */       
/* 124 */       int branchCount = data[dataIndex++];
/*     */       
/* 126 */       int branchIndex = Arrays.binarySearch(data, dataIndex, dataIndex + branchCount, key.charAt(keyIndex));
/*     */       
/* 128 */       if (branchIndex < 0) {
/*     */         break;
/*     */       }
/*     */       
/* 132 */       int resultIndex = branchIndex + branchCount;
/* 133 */       char result = data[resultIndex];
/* 134 */       if ((result & 0x8000) != 0) {
/* 135 */         return result & 0xFFFF7FFF;
/*     */       }
/*     */ 
/*     */       
/* 139 */       if ((result & 0x4000) != 0) {
/* 140 */         if (keyIndex == keyLength - 1) {
/* 141 */           return result & 0xFFFFBFFF;
/*     */         }
/* 143 */         result = '\001';
/*     */       } 
/*     */ 
/*     */       
/* 147 */       keyIndex += result;
/*     */ 
/*     */       
/* 150 */       if (branchIndex > dataIndex) {
/* 151 */         int jumpIndex = resultIndex + branchCount - 1;
/* 152 */         dataIndex += data[jumpIndex];
/*     */       } 
/*     */ 
/*     */       
/* 156 */       dataIndex += branchCount * 3 - 1;
/*     */     } 
/*     */     
/* 159 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ToIntFunction<String> buildTrie(Collection<String> table) {
/* 168 */     int numRows = table.size();
/* 169 */     if (numRows > 1) {
/* 170 */       return buildTrie(new StringBuilder(), table.<String>toArray(new String[numRows]), 0, numRows);
/*     */     }
/* 172 */     return ImmutableStringTrie::singletonTrie;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ToIntFunction<String> buildTrie(StringBuilder buf, String[] table, int row, int rowLimit) {
/* 179 */     int trieLimit = row + 16384;
/* 180 */     if (rowLimit <= trieLimit) {
/* 181 */       buildSubTrie(buf, table, 0, row, rowLimit);
/* 182 */       char[] arrayOfChar = new char[buf.length()];
/* 183 */       buf.getChars(0, arrayOfChar.length, arrayOfChar, 0);
/* 184 */       return new ImmutableStringTrie(arrayOfChar);
/*     */     } 
/*     */ 
/*     */     
/* 188 */     buildSubTrie(buf, table, 0, row, trieLimit);
/* 189 */     char[] data = new char[buf.length()];
/* 190 */     buf.getChars(0, data.length, data, 0);
/* 191 */     buf.setLength(0);
/*     */     
/* 193 */     return new Overflow(data, table[trieLimit], buildTrie(buf, table, trieLimit, rowLimit));
/*     */   }
/*     */   
/*     */   ImmutableStringTrie(char[] data) {
/* 197 */     this.trie = data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void buildSubTrie(StringBuilder buf, String[] table, int column, int row, int rowLimit) {
/* 204 */     int trieStart = buf.length();
/*     */     
/* 206 */     int prevRow = row;
/* 207 */     int branchCount = 0;
/* 208 */     int nextJump = 0;
/*     */     
/* 210 */     boolean allLeaves = true;
/*     */     
/* 212 */     while (prevRow < rowLimit) {
/* 213 */       String cells = table[prevRow];
/* 214 */       int columnLimit = cells.length();
/*     */       
/* 216 */       char pivot = cells.charAt(column);
/*     */ 
/*     */       
/* 219 */       int nextRow = nextPivotRow(table, pivot, column, prevRow, rowLimit);
/*     */ 
/*     */       
/* 222 */       int nextColumn = nextPivotColumn(table, column, prevRow, nextRow);
/*     */ 
/*     */       
/* 225 */       if (nextColumn == columnLimit && nextColumn - column > 1 && nextRow - prevRow > 1)
/*     */       {
/* 227 */         nextColumn--;
/*     */       }
/*     */ 
/*     */       
/* 231 */       int branchIndex = trieStart + branchCount;
/* 232 */       buf.insert(branchIndex, pivot);
/*     */       
/* 234 */       int resultIndex = branchIndex + 1 + branchCount;
/*     */ 
/*     */       
/* 237 */       int subTrieStart = buf.length() + 1;
/*     */       
/* 239 */       if (nextColumn < columnLimit) {
/*     */         
/* 241 */         buf.insert(resultIndex, (char)(nextColumn - column));
/* 242 */         buildSubTrie(buf, table, nextColumn, prevRow, nextRow);
/* 243 */         allLeaves = false;
/*     */       } else {
/*     */         
/* 246 */         buildSubTrie(buf, table, nextColumn, prevRow + 1, nextRow);
/*     */         
/* 248 */         boolean isLeaf = (subTrieStart > buf.length());
/* 249 */         char marker = isLeaf ? '耀' : '䀀';
/* 250 */         buf.insert(resultIndex, (char)(prevRow & 0x3FFF | marker));
/* 251 */         allLeaves = (allLeaves && isLeaf);
/*     */       } 
/*     */       
/* 254 */       if (nextRow < rowLimit) {
/*     */         
/* 256 */         int jumpIndex = resultIndex + 1 + branchCount;
/* 257 */         nextJump += buf.length() - subTrieStart;
/* 258 */         buf.insert(jumpIndex, (char)nextJump);
/*     */       } 
/*     */       
/* 261 */       prevRow = nextRow;
/* 262 */       branchCount++;
/*     */     } 
/*     */     
/* 265 */     if (branchCount > 0) {
/* 266 */       buf.insert(trieStart, (char)branchCount);
/* 267 */       if (allLeaves) {
/*     */         
/* 269 */         int jumpStart = trieStart + 1 + branchCount * 2;
/* 270 */         buf.delete(jumpStart, jumpStart + branchCount);
/*     */       } 
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
/*     */   private static int nextPivotRow(String[] table, char pivot, int column, int row, int rowLimit) {
/* 284 */     for (int r = row + 1; r < rowLimit; r++) {
/* 285 */       String cells = table[r];
/* 286 */       if (cells.length() <= column || cells.charAt(column) != pivot) {
/* 287 */         return r;
/*     */       }
/*     */     } 
/*     */     
/* 291 */     return rowLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int nextPivotColumn(String[] table, int column, int row, int rowLimit) {
/* 302 */     String cells = table[row];
/* 303 */     int columnLimit = cells.length();
/*     */     
/* 305 */     for (int c = column + 1; c < columnLimit; c++) {
/* 306 */       if (nextPivotRow(table, cells.charAt(c), c, row, rowLimit) < rowLimit) {
/* 307 */         return c;
/*     */       }
/*     */     } 
/*     */     
/* 311 */     return columnLimit;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class Overflow
/*     */     implements ToIntFunction<String>
/*     */   {
/*     */     private final ImmutableStringTrie trie;
/*     */     private final String overflowKey;
/*     */     private final ToIntFunction<String> next;
/*     */     
/*     */     Overflow(char[] data, String overflowKey, ToIntFunction<String> next) {
/* 323 */       this.trie = new ImmutableStringTrie(data);
/* 324 */       this.overflowKey = overflowKey;
/* 325 */       this.next = next;
/*     */     }
/*     */ 
/*     */     
/*     */     public int applyAsInt(String key) {
/* 330 */       return (key.compareTo(this.overflowKey) < 0) ? 
/* 331 */         this.trie.applyAsInt(key) : (
/* 332 */         16384 + this.next.applyAsInt(key));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\aop\ImmutableStringTrie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */