/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Converter;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Strings;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Floats
/*     */   extends FloatsMethodsForWeb
/*     */ {
/*     */   public static final int BYTES = 4;
/*     */   
/*     */   public static int hashCode(float value) {
/*  73 */     return Float.valueOf(value).hashCode();
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
/*     */   public static int compare(float a, float b) {
/*  90 */     return Float.compare(a, b);
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
/*     */   public static boolean isFinite(float value) {
/* 102 */     return (Float.NEGATIVE_INFINITY < value && value < Float.POSITIVE_INFINITY);
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
/*     */   public static boolean contains(float[] array, float target) {
/* 114 */     for (float value : array) {
/* 115 */       if (value == target) {
/* 116 */         return true;
/*     */       }
/*     */     } 
/* 119 */     return false;
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
/*     */   public static int indexOf(float[] array, float target) {
/* 132 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(float[] array, float target, int start, int end) {
/* 137 */     for (int i = start; i < end; i++) {
/* 138 */       if (array[i] == target) {
/* 139 */         return i;
/*     */       }
/*     */     } 
/* 142 */     return -1;
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
/*     */   public static int indexOf(float[] array, float[] target) {
/* 158 */     Preconditions.checkNotNull(array, "array");
/* 159 */     Preconditions.checkNotNull(target, "target");
/* 160 */     if (target.length == 0) {
/* 161 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 165 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 166 */       int j = 0; while (true) { if (j < target.length) {
/* 167 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 171 */         return i; }
/*     */     
/* 173 */     }  return -1;
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
/*     */   public static int lastIndexOf(float[] array, float target) {
/* 186 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(float[] array, float target, int start, int end) {
/* 191 */     for (int i = end - 1; i >= start; i--) {
/* 192 */       if (array[i] == target) {
/* 193 */         return i;
/*     */       }
/*     */     } 
/* 196 */     return -1;
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
/*     */   @GwtIncompatible("Available in GWT! Annotation is to avoid conflict with GWT specialization of base class.")
/*     */   public static float min(float... array) {
/* 211 */     Preconditions.checkArgument((array.length > 0));
/* 212 */     float min = array[0];
/* 213 */     for (int i = 1; i < array.length; i++) {
/* 214 */       min = Math.min(min, array[i]);
/*     */     }
/* 216 */     return min;
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
/*     */   @GwtIncompatible("Available in GWT! Annotation is to avoid conflict with GWT specialization of base class.")
/*     */   public static float max(float... array) {
/* 231 */     Preconditions.checkArgument((array.length > 0));
/* 232 */     float max = array[0];
/* 233 */     for (int i = 1; i < array.length; i++) {
/* 234 */       max = Math.max(max, array[i]);
/*     */     }
/* 236 */     return max;
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
/*     */   public static float constrainToRange(float value, float min, float max) {
/* 256 */     if (min <= max) {
/* 257 */       return Math.min(Math.max(value, min), max);
/*     */     }
/* 259 */     throw new IllegalArgumentException(
/* 260 */         Strings.lenientFormat("min (%s) must be less than or equal to max (%s)", new Object[] { Float.valueOf(min), Float.valueOf(max) }));
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
/*     */   public static float[] concat(float[]... arrays) {
/* 272 */     int length = 0;
/* 273 */     for (float[] array : arrays) {
/* 274 */       length += array.length;
/*     */     }
/* 276 */     float[] result = new float[length];
/* 277 */     int pos = 0;
/* 278 */     for (float[] array : arrays) {
/* 279 */       System.arraycopy(array, 0, result, pos, array.length);
/* 280 */       pos += array.length;
/*     */     } 
/* 282 */     return result;
/*     */   }
/*     */   
/*     */   private static final class FloatConverter
/*     */     extends Converter<String, Float> implements Serializable {
/* 287 */     static final FloatConverter INSTANCE = new FloatConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Float doForward(String value) {
/* 291 */       return Float.valueOf(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Float value) {
/* 296 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 301 */       return "Floats.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 305 */       return INSTANCE;
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
/*     */   @Beta
/*     */   public static Converter<String, Float> stringConverter() {
/* 319 */     return FloatConverter.INSTANCE;
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
/*     */   public static float[] ensureCapacity(float[] array, int minLength, int padding) {
/* 336 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 337 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 338 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static String join(String separator, float... array) {
/* 354 */     Preconditions.checkNotNull(separator);
/* 355 */     if (array.length == 0) {
/* 356 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 360 */     StringBuilder builder = new StringBuilder(array.length * 12);
/* 361 */     builder.append(array[0]);
/* 362 */     for (int i = 1; i < array.length; i++) {
/* 363 */       builder.append(separator).append(array[i]);
/*     */     }
/* 365 */     return builder.toString();
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
/*     */   public static Comparator<float[]> lexicographicalComparator() {
/* 382 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<float[]> {
/* 386 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(float[] left, float[] right) {
/* 390 */       int minLength = Math.min(left.length, right.length);
/* 391 */       for (int i = 0; i < minLength; i++) {
/* 392 */         int result = Float.compare(left[i], right[i]);
/* 393 */         if (result != 0) {
/* 394 */           return result;
/*     */         }
/*     */       } 
/* 397 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 402 */       return "Floats.lexicographicalComparator()";
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
/*     */   public static void sortDescending(float[] array) {
/* 415 */     Preconditions.checkNotNull(array);
/* 416 */     sortDescending(array, 0, array.length);
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
/*     */   public static void sortDescending(float[] array, int fromIndex, int toIndex) {
/* 429 */     Preconditions.checkNotNull(array);
/* 430 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 431 */     Arrays.sort(array, fromIndex, toIndex);
/* 432 */     reverse(array, fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reverse(float[] array) {
/* 442 */     Preconditions.checkNotNull(array);
/* 443 */     reverse(array, 0, array.length);
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
/*     */   public static void reverse(float[] array, int fromIndex, int toIndex) {
/* 457 */     Preconditions.checkNotNull(array);
/* 458 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 459 */     for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--) {
/* 460 */       float tmp = array[i];
/* 461 */       array[i] = array[j];
/* 462 */       array[j] = tmp;
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
/*     */   public static float[] toArray(Collection<? extends Number> collection) {
/* 480 */     if (collection instanceof FloatArrayAsList) {
/* 481 */       return ((FloatArrayAsList)collection).toFloatArray();
/*     */     }
/*     */     
/* 484 */     Object[] boxedArray = collection.toArray();
/* 485 */     int len = boxedArray.length;
/* 486 */     float[] array = new float[len];
/* 487 */     for (int i = 0; i < len; i++)
/*     */     {
/* 489 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).floatValue();
/*     */     }
/* 491 */     return array;
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
/*     */   public static List<Float> asList(float... backingArray) {
/* 510 */     if (backingArray.length == 0) {
/* 511 */       return Collections.emptyList();
/*     */     }
/* 513 */     return new FloatArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class FloatArrayAsList extends AbstractList<Float> implements RandomAccess, Serializable {
/*     */     final float[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     FloatArrayAsList(float[] array) {
/* 524 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     FloatArrayAsList(float[] array, int start, int end) {
/* 528 */       this.array = array;
/* 529 */       this.start = start;
/* 530 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 535 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 540 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Float get(int index) {
/* 545 */       Preconditions.checkElementIndex(index, size());
/* 546 */       return Float.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 552 */       return (target instanceof Float && Floats.indexOf(this.array, ((Float)target).floatValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 558 */       if (target instanceof Float) {
/* 559 */         int i = Floats.indexOf(this.array, ((Float)target).floatValue(), this.start, this.end);
/* 560 */         if (i >= 0) {
/* 561 */           return i - this.start;
/*     */         }
/*     */       } 
/* 564 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 570 */       if (target instanceof Float) {
/* 571 */         int i = Floats.lastIndexOf(this.array, ((Float)target).floatValue(), this.start, this.end);
/* 572 */         if (i >= 0) {
/* 573 */           return i - this.start;
/*     */         }
/*     */       } 
/* 576 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Float set(int index, Float element) {
/* 581 */       Preconditions.checkElementIndex(index, size());
/* 582 */       float oldValue = this.array[this.start + index];
/*     */       
/* 584 */       this.array[this.start + index] = ((Float)Preconditions.checkNotNull(element)).floatValue();
/* 585 */       return Float.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Float> subList(int fromIndex, int toIndex) {
/* 590 */       int size = size();
/* 591 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 592 */       if (fromIndex == toIndex) {
/* 593 */         return Collections.emptyList();
/*     */       }
/* 595 */       return new FloatArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 600 */       if (object == this) {
/* 601 */         return true;
/*     */       }
/* 603 */       if (object instanceof FloatArrayAsList) {
/* 604 */         FloatArrayAsList that = (FloatArrayAsList)object;
/* 605 */         int size = size();
/* 606 */         if (that.size() != size) {
/* 607 */           return false;
/*     */         }
/* 609 */         for (int i = 0; i < size; i++) {
/* 610 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 611 */             return false;
/*     */           }
/*     */         } 
/* 614 */         return true;
/*     */       } 
/* 616 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 621 */       int result = 1;
/* 622 */       for (int i = this.start; i < this.end; i++) {
/* 623 */         result = 31 * result + Floats.hashCode(this.array[i]);
/*     */       }
/* 625 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 630 */       StringBuilder builder = new StringBuilder(size() * 12);
/* 631 */       builder.append('[').append(this.array[this.start]);
/* 632 */       for (int i = this.start + 1; i < this.end; i++) {
/* 633 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 635 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     float[] toFloatArray() {
/* 639 */       return Arrays.copyOfRange(this.array, this.start, this.end);
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
/*     */   @GwtIncompatible
/*     */   public static Float tryParse(String string) {
/* 665 */     if (Doubles.FLOATING_POINT_PATTERN.matcher(string).matches()) {
/*     */       
/*     */       try {
/*     */         
/* 669 */         return Float.valueOf(Float.parseFloat(string));
/* 670 */       } catch (NumberFormatException numberFormatException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 675 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\primitives\Floats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */