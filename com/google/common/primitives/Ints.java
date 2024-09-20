/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Ints
/*     */   extends IntsMethodsForWeb
/*     */ {
/*     */   public static final int BYTES = 4;
/*     */   public static final int MAX_POWER_OF_TWO = 1073741824;
/*     */   
/*     */   public static int hashCode(int value) {
/*  76 */     return value;
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
/*     */   public static int checkedCast(long value) {
/*  88 */     int result = (int)value;
/*  89 */     Preconditions.checkArgument((result == value), "Out of range: %s", value);
/*  90 */     return result;
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
/*     */   public static int saturatedCast(long value) {
/* 102 */     if (value > 2147483647L) {
/* 103 */       return Integer.MAX_VALUE;
/*     */     }
/* 105 */     if (value < -2147483648L) {
/* 106 */       return Integer.MIN_VALUE;
/*     */     }
/* 108 */     return (int)value;
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
/*     */   public static int compare(int a, int b) {
/* 124 */     return (a < b) ? -1 : ((a > b) ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean contains(int[] array, int target) {
/* 135 */     for (int value : array) {
/* 136 */       if (value == target) {
/* 137 */         return true;
/*     */       }
/*     */     } 
/* 140 */     return false;
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
/*     */   public static int indexOf(int[] array, int target) {
/* 152 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(int[] array, int target, int start, int end) {
/* 157 */     for (int i = start; i < end; i++) {
/* 158 */       if (array[i] == target) {
/* 159 */         return i;
/*     */       }
/*     */     } 
/* 162 */     return -1;
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
/*     */   public static int indexOf(int[] array, int[] target) {
/* 176 */     Preconditions.checkNotNull(array, "array");
/* 177 */     Preconditions.checkNotNull(target, "target");
/* 178 */     if (target.length == 0) {
/* 179 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 183 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 184 */       int j = 0; while (true) { if (j < target.length) {
/* 185 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 189 */         return i; }
/*     */     
/* 191 */     }  return -1;
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
/*     */   public static int lastIndexOf(int[] array, int target) {
/* 203 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(int[] array, int target, int start, int end) {
/* 208 */     for (int i = end - 1; i >= start; i--) {
/* 209 */       if (array[i] == target) {
/* 210 */         return i;
/*     */       }
/*     */     } 
/* 213 */     return -1;
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
/*     */   @GwtIncompatible("Available in GWT! Annotation is to avoid conflict with GWT specialization of base class.")
/*     */   public static int min(int... array) {
/* 227 */     Preconditions.checkArgument((array.length > 0));
/* 228 */     int min = array[0];
/* 229 */     for (int i = 1; i < array.length; i++) {
/* 230 */       if (array[i] < min) {
/* 231 */         min = array[i];
/*     */       }
/*     */     } 
/* 234 */     return min;
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
/*     */   @GwtIncompatible("Available in GWT! Annotation is to avoid conflict with GWT specialization of base class.")
/*     */   public static int max(int... array) {
/* 248 */     Preconditions.checkArgument((array.length > 0));
/* 249 */     int max = array[0];
/* 250 */     for (int i = 1; i < array.length; i++) {
/* 251 */       if (array[i] > max) {
/* 252 */         max = array[i];
/*     */       }
/*     */     } 
/* 255 */     return max;
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
/*     */   public static int constrainToRange(int value, int min, int max) {
/* 273 */     Preconditions.checkArgument((min <= max), "min (%s) must be less than or equal to max (%s)", min, max);
/* 274 */     return Math.min(Math.max(value, min), max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] concat(int[]... arrays) {
/* 285 */     int length = 0;
/* 286 */     for (int[] array : arrays) {
/* 287 */       length += array.length;
/*     */     }
/* 289 */     int[] result = new int[length];
/* 290 */     int pos = 0;
/* 291 */     for (int[] array : arrays) {
/* 292 */       System.arraycopy(array, 0, result, pos, array.length);
/* 293 */       pos += array.length;
/*     */     } 
/* 295 */     return result;
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
/*     */   public static byte[] toByteArray(int value) {
/* 308 */     return new byte[] { (byte)(value >> 24), (byte)(value >> 16), (byte)(value >> 8), (byte)value };
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
/*     */   public static int fromByteArray(byte[] bytes) {
/* 325 */     Preconditions.checkArgument((bytes.length >= 4), "array too small: %s < %s", bytes.length, 4);
/* 326 */     return fromBytes(bytes[0], bytes[1], bytes[2], bytes[3]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int fromBytes(byte b1, byte b2, byte b3, byte b4) {
/* 336 */     return b1 << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8 | b4 & 0xFF;
/*     */   }
/*     */   
/*     */   private static final class IntConverter
/*     */     extends Converter<String, Integer> implements Serializable {
/* 341 */     static final IntConverter INSTANCE = new IntConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Integer doForward(String value) {
/* 345 */       return Integer.decode(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Integer value) {
/* 350 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 355 */       return "Ints.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 359 */       return INSTANCE;
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
/*     */   public static Converter<String, Integer> stringConverter() {
/* 378 */     return IntConverter.INSTANCE;
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
/*     */   public static int[] ensureCapacity(int[] array, int minLength, int padding) {
/* 395 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 396 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 397 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static String join(String separator, int... array) {
/* 409 */     Preconditions.checkNotNull(separator);
/* 410 */     if (array.length == 0) {
/* 411 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 415 */     StringBuilder builder = new StringBuilder(array.length * 5);
/* 416 */     builder.append(array[0]);
/* 417 */     for (int i = 1; i < array.length; i++) {
/* 418 */       builder.append(separator).append(array[i]);
/*     */     }
/* 420 */     return builder.toString();
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
/*     */   public static Comparator<int[]> lexicographicalComparator() {
/* 436 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<int[]> {
/* 440 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(int[] left, int[] right) {
/* 444 */       int minLength = Math.min(left.length, right.length);
/* 445 */       for (int i = 0; i < minLength; i++) {
/* 446 */         int result = Ints.compare(left[i], right[i]);
/* 447 */         if (result != 0) {
/* 448 */           return result;
/*     */         }
/*     */       } 
/* 451 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 456 */       return "Ints.lexicographicalComparator()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(int[] array) {
/* 466 */     Preconditions.checkNotNull(array);
/* 467 */     sortDescending(array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(int[] array, int fromIndex, int toIndex) {
/* 477 */     Preconditions.checkNotNull(array);
/* 478 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 479 */     Arrays.sort(array, fromIndex, toIndex);
/* 480 */     reverse(array, fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reverse(int[] array) {
/* 490 */     Preconditions.checkNotNull(array);
/* 491 */     reverse(array, 0, array.length);
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
/*     */   public static void reverse(int[] array, int fromIndex, int toIndex) {
/* 505 */     Preconditions.checkNotNull(array);
/* 506 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 507 */     for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--) {
/* 508 */       int tmp = array[i];
/* 509 */       array[i] = array[j];
/* 510 */       array[j] = tmp;
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
/*     */   public static int[] toArray(Collection<? extends Number> collection) {
/* 528 */     if (collection instanceof IntArrayAsList) {
/* 529 */       return ((IntArrayAsList)collection).toIntArray();
/*     */     }
/*     */     
/* 532 */     Object[] boxedArray = collection.toArray();
/* 533 */     int len = boxedArray.length;
/* 534 */     int[] array = new int[len];
/* 535 */     for (int i = 0; i < len; i++)
/*     */     {
/* 537 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).intValue();
/*     */     }
/* 539 */     return array;
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
/*     */   public static List<Integer> asList(int... backingArray) {
/* 558 */     if (backingArray.length == 0) {
/* 559 */       return Collections.emptyList();
/*     */     }
/* 561 */     return new IntArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class IntArrayAsList extends AbstractList<Integer> implements RandomAccess, Serializable {
/*     */     final int[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     IntArrayAsList(int[] array) {
/* 572 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     IntArrayAsList(int[] array, int start, int end) {
/* 576 */       this.array = array;
/* 577 */       this.start = start;
/* 578 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 583 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 588 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Integer get(int index) {
/* 593 */       Preconditions.checkElementIndex(index, size());
/* 594 */       return Integer.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator.OfInt spliterator() {
/* 599 */       return Spliterators.spliterator(this.array, this.start, this.end, 0);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 605 */       return (target instanceof Integer && Ints.indexOf(this.array, ((Integer)target).intValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 611 */       if (target instanceof Integer) {
/* 612 */         int i = Ints.indexOf(this.array, ((Integer)target).intValue(), this.start, this.end);
/* 613 */         if (i >= 0) {
/* 614 */           return i - this.start;
/*     */         }
/*     */       } 
/* 617 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 623 */       if (target instanceof Integer) {
/* 624 */         int i = Ints.lastIndexOf(this.array, ((Integer)target).intValue(), this.start, this.end);
/* 625 */         if (i >= 0) {
/* 626 */           return i - this.start;
/*     */         }
/*     */       } 
/* 629 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Integer set(int index, Integer element) {
/* 634 */       Preconditions.checkElementIndex(index, size());
/* 635 */       int oldValue = this.array[this.start + index];
/*     */       
/* 637 */       this.array[this.start + index] = ((Integer)Preconditions.checkNotNull(element)).intValue();
/* 638 */       return Integer.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Integer> subList(int fromIndex, int toIndex) {
/* 643 */       int size = size();
/* 644 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 645 */       if (fromIndex == toIndex) {
/* 646 */         return Collections.emptyList();
/*     */       }
/* 648 */       return new IntArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 653 */       if (object == this) {
/* 654 */         return true;
/*     */       }
/* 656 */       if (object instanceof IntArrayAsList) {
/* 657 */         IntArrayAsList that = (IntArrayAsList)object;
/* 658 */         int size = size();
/* 659 */         if (that.size() != size) {
/* 660 */           return false;
/*     */         }
/* 662 */         for (int i = 0; i < size; i++) {
/* 663 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 664 */             return false;
/*     */           }
/*     */         } 
/* 667 */         return true;
/*     */       } 
/* 669 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 674 */       int result = 1;
/* 675 */       for (int i = this.start; i < this.end; i++) {
/* 676 */         result = 31 * result + Ints.hashCode(this.array[i]);
/*     */       }
/* 678 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 683 */       StringBuilder builder = new StringBuilder(size() * 5);
/* 684 */       builder.append('[').append(this.array[this.start]);
/* 685 */       for (int i = this.start + 1; i < this.end; i++) {
/* 686 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 688 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     int[] toIntArray() {
/* 692 */       return Arrays.copyOfRange(this.array, this.start, this.end);
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
/*     */   @Beta
/*     */   public static Integer tryParse(String string) {
/* 717 */     return tryParse(string, 10);
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
/*     */   public static Integer tryParse(String string, int radix) {
/* 742 */     Long result = Longs.tryParse(string, radix);
/* 743 */     if (result == null || result.longValue() != result.intValue()) {
/* 744 */       return null;
/*     */     }
/* 746 */     return Integer.valueOf(result.intValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\primitives\Ints.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */