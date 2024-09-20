/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Converter;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.RandomAccess;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Longs
/*     */ {
/*     */   public static final int BYTES = 8;
/*     */   public static final long MAX_POWER_OF_TWO = 4611686018427387904L;
/*     */   
/*     */   public static int hashCode(long value) {
/*  79 */     return (int)(value ^ value >>> 32L);
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
/*     */   public static int compare(long a, long b) {
/*  95 */     return (a < b) ? -1 : ((a > b) ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean contains(long[] array, long target) {
/* 106 */     for (long value : array) {
/* 107 */       if (value == target) {
/* 108 */         return true;
/*     */       }
/*     */     } 
/* 111 */     return false;
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
/*     */   public static int indexOf(long[] array, long target) {
/* 123 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(long[] array, long target, int start, int end) {
/* 128 */     for (int i = start; i < end; i++) {
/* 129 */       if (array[i] == target) {
/* 130 */         return i;
/*     */       }
/*     */     } 
/* 133 */     return -1;
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
/*     */   public static int indexOf(long[] array, long[] target) {
/* 147 */     Preconditions.checkNotNull(array, "array");
/* 148 */     Preconditions.checkNotNull(target, "target");
/* 149 */     if (target.length == 0) {
/* 150 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 154 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 155 */       int j = 0; while (true) { if (j < target.length) {
/* 156 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 160 */         return i; }
/*     */     
/* 162 */     }  return -1;
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
/*     */   public static int lastIndexOf(long[] array, long target) {
/* 174 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(long[] array, long target, int start, int end) {
/* 179 */     for (int i = end - 1; i >= start; i--) {
/* 180 */       if (array[i] == target) {
/* 181 */         return i;
/*     */       }
/*     */     } 
/* 184 */     return -1;
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
/*     */   public static long min(long... array) {
/* 196 */     Preconditions.checkArgument((array.length > 0));
/* 197 */     long min = array[0];
/* 198 */     for (int i = 1; i < array.length; i++) {
/* 199 */       if (array[i] < min) {
/* 200 */         min = array[i];
/*     */       }
/*     */     } 
/* 203 */     return min;
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
/*     */   public static long max(long... array) {
/* 215 */     Preconditions.checkArgument((array.length > 0));
/* 216 */     long max = array[0];
/* 217 */     for (int i = 1; i < array.length; i++) {
/* 218 */       if (array[i] > max) {
/* 219 */         max = array[i];
/*     */       }
/*     */     } 
/* 222 */     return max;
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
/*     */   public static long constrainToRange(long value, long min, long max) {
/* 240 */     Preconditions.checkArgument((min <= max), "min (%s) must be less than or equal to max (%s)", min, max);
/* 241 */     return Math.min(Math.max(value, min), max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long[] concat(long[]... arrays) {
/* 252 */     int length = 0;
/* 253 */     for (long[] array : arrays) {
/* 254 */       length += array.length;
/*     */     }
/* 256 */     long[] result = new long[length];
/* 257 */     int pos = 0;
/* 258 */     for (long[] array : arrays) {
/* 259 */       System.arraycopy(array, 0, result, pos, array.length);
/* 260 */       pos += array.length;
/*     */     } 
/* 262 */     return result;
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
/*     */   public static byte[] toByteArray(long value) {
/* 278 */     byte[] result = new byte[8];
/* 279 */     for (int i = 7; i >= 0; i--) {
/* 280 */       result[i] = (byte)(int)(value & 0xFFL);
/* 281 */       value >>= 8L;
/*     */     } 
/* 283 */     return result;
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
/*     */   public static long fromByteArray(byte[] bytes) {
/* 298 */     Preconditions.checkArgument((bytes.length >= 8), "array too small: %s < %s", bytes.length, 8);
/* 299 */     return fromBytes(bytes[0], bytes[1], bytes[2], bytes[3], bytes[4], bytes[5], bytes[6], bytes[7]);
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
/*     */   public static long fromBytes(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8) {
/* 311 */     return (b1 & 0xFFL) << 56L | (b2 & 0xFFL) << 48L | (b3 & 0xFFL) << 40L | (b4 & 0xFFL) << 32L | (b5 & 0xFFL) << 24L | (b6 & 0xFFL) << 16L | (b7 & 0xFFL) << 8L | b8 & 0xFFL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class AsciiDigits
/*     */   {
/*     */     private static final byte[] asciiDigits;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/* 331 */       byte[] result = new byte[128];
/* 332 */       Arrays.fill(result, (byte)-1); int i;
/* 333 */       for (i = 0; i < 10; i++) {
/* 334 */         result[48 + i] = (byte)i;
/*     */       }
/* 336 */       for (i = 0; i < 26; i++) {
/* 337 */         result[65 + i] = (byte)(10 + i);
/* 338 */         result[97 + i] = (byte)(10 + i);
/*     */       } 
/* 340 */       asciiDigits = result;
/*     */     }
/*     */     
/*     */     static int digit(char c) {
/* 344 */       return (c < '') ? asciiDigits[c] : -1;
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
/*     */   @Beta
/*     */   public static Long tryParse(String string) {
/* 367 */     return tryParse(string, 10);
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
/*     */   @Beta
/*     */   public static Long tryParse(String string, int radix) {
/* 392 */     if (((String)Preconditions.checkNotNull(string)).isEmpty()) {
/* 393 */       return null;
/*     */     }
/* 395 */     if (radix < 2 || radix > 36) {
/* 396 */       throw new IllegalArgumentException((new StringBuilder(65)).append("radix must be between MIN_RADIX and MAX_RADIX but was ").append(radix).toString());
/*     */     }
/*     */     
/* 399 */     boolean negative = (string.charAt(0) == '-');
/* 400 */     int index = negative ? 1 : 0;
/* 401 */     if (index == string.length()) {
/* 402 */       return null;
/*     */     }
/* 404 */     int digit = AsciiDigits.digit(string.charAt(index++));
/* 405 */     if (digit < 0 || digit >= radix) {
/* 406 */       return null;
/*     */     }
/* 408 */     long accum = -digit;
/*     */     
/* 410 */     long cap = Long.MIN_VALUE / radix;
/*     */     
/* 412 */     while (index < string.length()) {
/* 413 */       digit = AsciiDigits.digit(string.charAt(index++));
/* 414 */       if (digit < 0 || digit >= radix || accum < cap) {
/* 415 */         return null;
/*     */       }
/* 417 */       accum *= radix;
/* 418 */       if (accum < Long.MIN_VALUE + digit) {
/* 419 */         return null;
/*     */       }
/* 421 */       accum -= digit;
/*     */     } 
/*     */     
/* 424 */     if (negative)
/* 425 */       return Long.valueOf(accum); 
/* 426 */     if (accum == Long.MIN_VALUE) {
/* 427 */       return null;
/*     */     }
/* 429 */     return Long.valueOf(-accum);
/*     */   }
/*     */   
/*     */   private static final class LongConverter
/*     */     extends Converter<String, Long> implements Serializable {
/* 434 */     static final LongConverter INSTANCE = new LongConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Long doForward(String value) {
/* 438 */       return Long.decode(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Long value) {
/* 443 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 448 */       return "Longs.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 452 */       return INSTANCE;
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
/*     */   @Beta
/*     */   public static Converter<String, Long> stringConverter() {
/* 471 */     return LongConverter.INSTANCE;
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
/*     */   public static long[] ensureCapacity(long[] array, int minLength, int padding) {
/* 488 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 489 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 490 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static String join(String separator, long... array) {
/* 502 */     Preconditions.checkNotNull(separator);
/* 503 */     if (array.length == 0) {
/* 504 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 508 */     StringBuilder builder = new StringBuilder(array.length * 10);
/* 509 */     builder.append(array[0]);
/* 510 */     for (int i = 1; i < array.length; i++) {
/* 511 */       builder.append(separator).append(array[i]);
/*     */     }
/* 513 */     return builder.toString();
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
/*     */   public static Comparator<long[]> lexicographicalComparator() {
/* 530 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<long[]> {
/* 534 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(long[] left, long[] right) {
/* 538 */       int minLength = Math.min(left.length, right.length);
/* 539 */       for (int i = 0; i < minLength; i++) {
/* 540 */         int result = Longs.compare(left[i], right[i]);
/* 541 */         if (result != 0) {
/* 542 */           return result;
/*     */         }
/*     */       } 
/* 545 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 550 */       return "Longs.lexicographicalComparator()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(long[] array) {
/* 560 */     Preconditions.checkNotNull(array);
/* 561 */     sortDescending(array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(long[] array, int fromIndex, int toIndex) {
/* 571 */     Preconditions.checkNotNull(array);
/* 572 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 573 */     Arrays.sort(array, fromIndex, toIndex);
/* 574 */     reverse(array, fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reverse(long[] array) {
/* 584 */     Preconditions.checkNotNull(array);
/* 585 */     reverse(array, 0, array.length);
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
/*     */   public static void reverse(long[] array, int fromIndex, int toIndex) {
/* 599 */     Preconditions.checkNotNull(array);
/* 600 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 601 */     for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--) {
/* 602 */       long tmp = array[i];
/* 603 */       array[i] = array[j];
/* 604 */       array[j] = tmp;
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
/*     */   public static long[] toArray(Collection<? extends Number> collection) {
/* 622 */     if (collection instanceof LongArrayAsList) {
/* 623 */       return ((LongArrayAsList)collection).toLongArray();
/*     */     }
/*     */     
/* 626 */     Object[] boxedArray = collection.toArray();
/* 627 */     int len = boxedArray.length;
/* 628 */     long[] array = new long[len];
/* 629 */     for (int i = 0; i < len; i++)
/*     */     {
/* 631 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).longValue();
/*     */     }
/* 633 */     return array;
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
/*     */   public static List<Long> asList(long... backingArray) {
/* 652 */     if (backingArray.length == 0) {
/* 653 */       return Collections.emptyList();
/*     */     }
/* 655 */     return new LongArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class LongArrayAsList extends AbstractList<Long> implements RandomAccess, Serializable {
/*     */     final long[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     LongArrayAsList(long[] array) {
/* 666 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     LongArrayAsList(long[] array, int start, int end) {
/* 670 */       this.array = array;
/* 671 */       this.start = start;
/* 672 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 677 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 682 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Long get(int index) {
/* 687 */       Preconditions.checkElementIndex(index, size());
/* 688 */       return Long.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator.OfLong spliterator() {
/* 693 */       return Spliterators.spliterator(this.array, this.start, this.end, 0);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 699 */       return (target instanceof Long && Longs.indexOf(this.array, ((Long)target).longValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 705 */       if (target instanceof Long) {
/* 706 */         int i = Longs.indexOf(this.array, ((Long)target).longValue(), this.start, this.end);
/* 707 */         if (i >= 0) {
/* 708 */           return i - this.start;
/*     */         }
/*     */       } 
/* 711 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 717 */       if (target instanceof Long) {
/* 718 */         int i = Longs.lastIndexOf(this.array, ((Long)target).longValue(), this.start, this.end);
/* 719 */         if (i >= 0) {
/* 720 */           return i - this.start;
/*     */         }
/*     */       } 
/* 723 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Long set(int index, Long element) {
/* 728 */       Preconditions.checkElementIndex(index, size());
/* 729 */       long oldValue = this.array[this.start + index];
/*     */       
/* 731 */       this.array[this.start + index] = ((Long)Preconditions.checkNotNull(element)).longValue();
/* 732 */       return Long.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Long> subList(int fromIndex, int toIndex) {
/* 737 */       int size = size();
/* 738 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 739 */       if (fromIndex == toIndex) {
/* 740 */         return Collections.emptyList();
/*     */       }
/* 742 */       return new LongArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 747 */       if (object == this) {
/* 748 */         return true;
/*     */       }
/* 750 */       if (object instanceof LongArrayAsList) {
/* 751 */         LongArrayAsList that = (LongArrayAsList)object;
/* 752 */         int size = size();
/* 753 */         if (that.size() != size) {
/* 754 */           return false;
/*     */         }
/* 756 */         for (int i = 0; i < size; i++) {
/* 757 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 758 */             return false;
/*     */           }
/*     */         } 
/* 761 */         return true;
/*     */       } 
/* 763 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 768 */       int result = 1;
/* 769 */       for (int i = this.start; i < this.end; i++) {
/* 770 */         result = 31 * result + Longs.hashCode(this.array[i]);
/*     */       }
/* 772 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 777 */       StringBuilder builder = new StringBuilder(size() * 10);
/* 778 */       builder.append('[').append(this.array[this.start]);
/* 779 */       for (int i = this.start + 1; i < this.end; i++) {
/* 780 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 782 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     long[] toLongArray() {
/* 786 */       return Arrays.copyOfRange(this.array, this.start, this.end);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\primitives\Longs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */