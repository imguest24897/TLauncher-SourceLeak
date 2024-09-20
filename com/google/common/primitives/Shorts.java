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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Shorts
/*     */   extends ShortsMethodsForWeb
/*     */ {
/*     */   public static final int BYTES = 2;
/*     */   public static final short MAX_POWER_OF_TWO = 16384;
/*     */   
/*     */   public static int hashCode(short value) {
/*  74 */     return value;
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
/*     */   public static short checkedCast(long value) {
/*  86 */     short result = (short)(int)value;
/*  87 */     Preconditions.checkArgument((result == value), "Out of range: %s", value);
/*  88 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static short saturatedCast(long value) {
/*  99 */     if (value > 32767L) {
/* 100 */       return Short.MAX_VALUE;
/*     */     }
/* 102 */     if (value < -32768L) {
/* 103 */       return Short.MIN_VALUE;
/*     */     }
/* 105 */     return (short)(int)value;
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
/*     */   public static int compare(short a, short b) {
/* 121 */     return a - b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean contains(short[] array, short target) {
/* 132 */     for (short value : array) {
/* 133 */       if (value == target) {
/* 134 */         return true;
/*     */       }
/*     */     } 
/* 137 */     return false;
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
/*     */   public static int indexOf(short[] array, short target) {
/* 149 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(short[] array, short target, int start, int end) {
/* 154 */     for (int i = start; i < end; i++) {
/* 155 */       if (array[i] == target) {
/* 156 */         return i;
/*     */       }
/*     */     } 
/* 159 */     return -1;
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
/*     */   public static int indexOf(short[] array, short[] target) {
/* 173 */     Preconditions.checkNotNull(array, "array");
/* 174 */     Preconditions.checkNotNull(target, "target");
/* 175 */     if (target.length == 0) {
/* 176 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 180 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 181 */       int j = 0; while (true) { if (j < target.length) {
/* 182 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 186 */         return i; }
/*     */     
/* 188 */     }  return -1;
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
/*     */   public static int lastIndexOf(short[] array, short target) {
/* 200 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(short[] array, short target, int start, int end) {
/* 205 */     for (int i = end - 1; i >= start; i--) {
/* 206 */       if (array[i] == target) {
/* 207 */         return i;
/*     */       }
/*     */     } 
/* 210 */     return -1;
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
/*     */   public static short min(short... array) {
/* 224 */     Preconditions.checkArgument((array.length > 0));
/* 225 */     short min = array[0];
/* 226 */     for (int i = 1; i < array.length; i++) {
/* 227 */       if (array[i] < min) {
/* 228 */         min = array[i];
/*     */       }
/*     */     } 
/* 231 */     return min;
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
/*     */   public static short max(short... array) {
/* 245 */     Preconditions.checkArgument((array.length > 0));
/* 246 */     short max = array[0];
/* 247 */     for (int i = 1; i < array.length; i++) {
/* 248 */       if (array[i] > max) {
/* 249 */         max = array[i];
/*     */       }
/*     */     } 
/* 252 */     return max;
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
/*     */   public static short constrainToRange(short value, short min, short max) {
/* 270 */     Preconditions.checkArgument((min <= max), "min (%s) must be less than or equal to max (%s)", min, max);
/* 271 */     return (value < min) ? min : ((value < max) ? value : max);
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
/*     */   public static short[] concat(short[]... arrays) {
/* 283 */     int length = 0;
/* 284 */     for (short[] array : arrays) {
/* 285 */       length += array.length;
/*     */     }
/* 287 */     short[] result = new short[length];
/* 288 */     int pos = 0;
/* 289 */     for (short[] array : arrays) {
/* 290 */       System.arraycopy(array, 0, result, pos, array.length);
/* 291 */       pos += array.length;
/*     */     } 
/* 293 */     return result;
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
/*     */   @GwtIncompatible
/*     */   public static byte[] toByteArray(short value) {
/* 307 */     return new byte[] { (byte)(value >> 8), (byte)value };
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
/*     */   public static short fromByteArray(byte[] bytes) {
/* 322 */     Preconditions.checkArgument((bytes.length >= 2), "array too small: %s < %s", bytes.length, 2);
/* 323 */     return fromBytes(bytes[0], bytes[1]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static short fromBytes(byte b1, byte b2) {
/* 334 */     return (short)(b1 << 8 | b2 & 0xFF);
/*     */   }
/*     */   
/*     */   private static final class ShortConverter
/*     */     extends Converter<String, Short> implements Serializable {
/* 339 */     static final ShortConverter INSTANCE = new ShortConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Short doForward(String value) {
/* 343 */       return Short.decode(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Short value) {
/* 348 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 353 */       return "Shorts.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 357 */       return INSTANCE;
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
/*     */   public static Converter<String, Short> stringConverter() {
/* 376 */     return ShortConverter.INSTANCE;
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
/*     */   public static short[] ensureCapacity(short[] array, int minLength, int padding) {
/* 393 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 394 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 395 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static String join(String separator, short... array) {
/* 408 */     Preconditions.checkNotNull(separator);
/* 409 */     if (array.length == 0) {
/* 410 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 414 */     StringBuilder builder = new StringBuilder(array.length * 6);
/* 415 */     builder.append(array[0]);
/* 416 */     for (int i = 1; i < array.length; i++) {
/* 417 */       builder.append(separator).append(array[i]);
/*     */     }
/* 419 */     return builder.toString();
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
/*     */   public static Comparator<short[]> lexicographicalComparator() {
/* 436 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<short[]> {
/* 440 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(short[] left, short[] right) {
/* 444 */       int minLength = Math.min(left.length, right.length);
/* 445 */       for (int i = 0; i < minLength; i++) {
/* 446 */         int result = Shorts.compare(left[i], right[i]);
/* 447 */         if (result != 0) {
/* 448 */           return result;
/*     */         }
/*     */       } 
/* 451 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 456 */       return "Shorts.lexicographicalComparator()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(short[] array) {
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
/*     */   public static void sortDescending(short[] array, int fromIndex, int toIndex) {
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
/*     */   public static void reverse(short[] array) {
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
/*     */   public static void reverse(short[] array, int fromIndex, int toIndex) {
/* 505 */     Preconditions.checkNotNull(array);
/* 506 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 507 */     for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--) {
/* 508 */       short tmp = array[i];
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
/*     */   public static short[] toArray(Collection<? extends Number> collection) {
/* 528 */     if (collection instanceof ShortArrayAsList) {
/* 529 */       return ((ShortArrayAsList)collection).toShortArray();
/*     */     }
/*     */     
/* 532 */     Object[] boxedArray = collection.toArray();
/* 533 */     int len = boxedArray.length;
/* 534 */     short[] array = new short[len];
/* 535 */     for (int i = 0; i < len; i++)
/*     */     {
/* 537 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).shortValue();
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
/*     */   public static List<Short> asList(short... backingArray) {
/* 555 */     if (backingArray.length == 0) {
/* 556 */       return Collections.emptyList();
/*     */     }
/* 558 */     return new ShortArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class ShortArrayAsList extends AbstractList<Short> implements RandomAccess, Serializable {
/*     */     final short[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ShortArrayAsList(short[] array) {
/* 569 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     ShortArrayAsList(short[] array, int start, int end) {
/* 573 */       this.array = array;
/* 574 */       this.start = start;
/* 575 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 580 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 585 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Short get(int index) {
/* 590 */       Preconditions.checkElementIndex(index, size());
/* 591 */       return Short.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 597 */       return (target instanceof Short && Shorts.indexOf(this.array, ((Short)target).shortValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 603 */       if (target instanceof Short) {
/* 604 */         int i = Shorts.indexOf(this.array, ((Short)target).shortValue(), this.start, this.end);
/* 605 */         if (i >= 0) {
/* 606 */           return i - this.start;
/*     */         }
/*     */       } 
/* 609 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 615 */       if (target instanceof Short) {
/* 616 */         int i = Shorts.lastIndexOf(this.array, ((Short)target).shortValue(), this.start, this.end);
/* 617 */         if (i >= 0) {
/* 618 */           return i - this.start;
/*     */         }
/*     */       } 
/* 621 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Short set(int index, Short element) {
/* 626 */       Preconditions.checkElementIndex(index, size());
/* 627 */       short oldValue = this.array[this.start + index];
/*     */       
/* 629 */       this.array[this.start + index] = ((Short)Preconditions.checkNotNull(element)).shortValue();
/* 630 */       return Short.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Short> subList(int fromIndex, int toIndex) {
/* 635 */       int size = size();
/* 636 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 637 */       if (fromIndex == toIndex) {
/* 638 */         return Collections.emptyList();
/*     */       }
/* 640 */       return new ShortArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 645 */       if (object == this) {
/* 646 */         return true;
/*     */       }
/* 648 */       if (object instanceof ShortArrayAsList) {
/* 649 */         ShortArrayAsList that = (ShortArrayAsList)object;
/* 650 */         int size = size();
/* 651 */         if (that.size() != size) {
/* 652 */           return false;
/*     */         }
/* 654 */         for (int i = 0; i < size; i++) {
/* 655 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 656 */             return false;
/*     */           }
/*     */         } 
/* 659 */         return true;
/*     */       } 
/* 661 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 666 */       int result = 1;
/* 667 */       for (int i = this.start; i < this.end; i++) {
/* 668 */         result = 31 * result + Shorts.hashCode(this.array[i]);
/*     */       }
/* 670 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 675 */       StringBuilder builder = new StringBuilder(size() * 6);
/* 676 */       builder.append('[').append(this.array[this.start]);
/* 677 */       for (int i = this.start + 1; i < this.end; i++) {
/* 678 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 680 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     short[] toShortArray() {
/* 684 */       return Arrays.copyOfRange(this.array, this.start, this.end);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\primitives\Shorts.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */