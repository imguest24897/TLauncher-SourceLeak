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
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Doubles
/*     */   extends DoublesMethodsForWeb
/*     */ {
/*     */   public static final int BYTES = 8;
/*     */   
/*     */   public static int hashCode(double value) {
/*  74 */     return Double.valueOf(value).hashCode();
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
/*     */   public static int compare(double a, double b) {
/*  95 */     return Double.compare(a, b);
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
/*     */   public static boolean isFinite(double value) {
/* 107 */     return (Double.NEGATIVE_INFINITY < value && value < Double.POSITIVE_INFINITY);
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
/*     */   public static boolean contains(double[] array, double target) {
/* 119 */     for (double value : array) {
/* 120 */       if (value == target) {
/* 121 */         return true;
/*     */       }
/*     */     } 
/* 124 */     return false;
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
/*     */   public static int indexOf(double[] array, double target) {
/* 137 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(double[] array, double target, int start, int end) {
/* 142 */     for (int i = start; i < end; i++) {
/* 143 */       if (array[i] == target) {
/* 144 */         return i;
/*     */       }
/*     */     } 
/* 147 */     return -1;
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
/*     */   public static int indexOf(double[] array, double[] target) {
/* 163 */     Preconditions.checkNotNull(array, "array");
/* 164 */     Preconditions.checkNotNull(target, "target");
/* 165 */     if (target.length == 0) {
/* 166 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 170 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 171 */       int j = 0; while (true) { if (j < target.length) {
/* 172 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 176 */         return i; }
/*     */     
/* 178 */     }  return -1;
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
/*     */   public static int lastIndexOf(double[] array, double target) {
/* 191 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(double[] array, double target, int start, int end) {
/* 196 */     for (int i = end - 1; i >= start; i--) {
/* 197 */       if (array[i] == target) {
/* 198 */         return i;
/*     */       }
/*     */     } 
/* 201 */     return -1;
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
/*     */   public static double min(double... array) {
/* 216 */     Preconditions.checkArgument((array.length > 0));
/* 217 */     double min = array[0];
/* 218 */     for (int i = 1; i < array.length; i++) {
/* 219 */       min = Math.min(min, array[i]);
/*     */     }
/* 221 */     return min;
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
/*     */   public static double max(double... array) {
/* 236 */     Preconditions.checkArgument((array.length > 0));
/* 237 */     double max = array[0];
/* 238 */     for (int i = 1; i < array.length; i++) {
/* 239 */       max = Math.max(max, array[i]);
/*     */     }
/* 241 */     return max;
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
/*     */   public static double constrainToRange(double value, double min, double max) {
/* 261 */     if (min <= max) {
/* 262 */       return Math.min(Math.max(value, min), max);
/*     */     }
/* 264 */     throw new IllegalArgumentException(
/* 265 */         Strings.lenientFormat("min (%s) must be less than or equal to max (%s)", new Object[] { Double.valueOf(min), Double.valueOf(max) }));
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
/*     */   public static double[] concat(double[]... arrays) {
/* 277 */     int length = 0;
/* 278 */     for (double[] array : arrays) {
/* 279 */       length += array.length;
/*     */     }
/* 281 */     double[] result = new double[length];
/* 282 */     int pos = 0;
/* 283 */     for (double[] array : arrays) {
/* 284 */       System.arraycopy(array, 0, result, pos, array.length);
/* 285 */       pos += array.length;
/*     */     } 
/* 287 */     return result;
/*     */   }
/*     */   
/*     */   private static final class DoubleConverter
/*     */     extends Converter<String, Double> implements Serializable {
/* 292 */     static final DoubleConverter INSTANCE = new DoubleConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Double doForward(String value) {
/* 296 */       return Double.valueOf(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Double value) {
/* 301 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 306 */       return "Doubles.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 310 */       return INSTANCE;
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
/*     */   public static Converter<String, Double> stringConverter() {
/* 324 */     return DoubleConverter.INSTANCE;
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
/*     */   public static double[] ensureCapacity(double[] array, int minLength, int padding) {
/* 341 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 342 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 343 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static String join(String separator, double... array) {
/* 359 */     Preconditions.checkNotNull(separator);
/* 360 */     if (array.length == 0) {
/* 361 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 365 */     StringBuilder builder = new StringBuilder(array.length * 12);
/* 366 */     builder.append(array[0]);
/* 367 */     for (int i = 1; i < array.length; i++) {
/* 368 */       builder.append(separator).append(array[i]);
/*     */     }
/* 370 */     return builder.toString();
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
/*     */   public static Comparator<double[]> lexicographicalComparator() {
/* 387 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<double[]> {
/* 391 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(double[] left, double[] right) {
/* 395 */       int minLength = Math.min(left.length, right.length);
/* 396 */       for (int i = 0; i < minLength; i++) {
/* 397 */         int result = Double.compare(left[i], right[i]);
/* 398 */         if (result != 0) {
/* 399 */           return result;
/*     */         }
/*     */       } 
/* 402 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 407 */       return "Doubles.lexicographicalComparator()";
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
/*     */   public static void sortDescending(double[] array) {
/* 420 */     Preconditions.checkNotNull(array);
/* 421 */     sortDescending(array, 0, array.length);
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
/*     */   public static void sortDescending(double[] array, int fromIndex, int toIndex) {
/* 434 */     Preconditions.checkNotNull(array);
/* 435 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 436 */     Arrays.sort(array, fromIndex, toIndex);
/* 437 */     reverse(array, fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reverse(double[] array) {
/* 447 */     Preconditions.checkNotNull(array);
/* 448 */     reverse(array, 0, array.length);
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
/*     */   public static void reverse(double[] array, int fromIndex, int toIndex) {
/* 462 */     Preconditions.checkNotNull(array);
/* 463 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 464 */     for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--) {
/* 465 */       double tmp = array[i];
/* 466 */       array[i] = array[j];
/* 467 */       array[j] = tmp;
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
/*     */   public static double[] toArray(Collection<? extends Number> collection) {
/* 485 */     if (collection instanceof DoubleArrayAsList) {
/* 486 */       return ((DoubleArrayAsList)collection).toDoubleArray();
/*     */     }
/*     */     
/* 489 */     Object[] boxedArray = collection.toArray();
/* 490 */     int len = boxedArray.length;
/* 491 */     double[] array = new double[len];
/* 492 */     for (int i = 0; i < len; i++)
/*     */     {
/* 494 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).doubleValue();
/*     */     }
/* 496 */     return array;
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
/*     */   public static List<Double> asList(double... backingArray) {
/* 518 */     if (backingArray.length == 0) {
/* 519 */       return Collections.emptyList();
/*     */     }
/* 521 */     return new DoubleArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class DoubleArrayAsList extends AbstractList<Double> implements RandomAccess, Serializable {
/*     */     final double[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     DoubleArrayAsList(double[] array) {
/* 532 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     DoubleArrayAsList(double[] array, int start, int end) {
/* 536 */       this.array = array;
/* 537 */       this.start = start;
/* 538 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 543 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 548 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Double get(int index) {
/* 553 */       Preconditions.checkElementIndex(index, size());
/* 554 */       return Double.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator.OfDouble spliterator() {
/* 559 */       return Spliterators.spliterator(this.array, this.start, this.end, 0);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 565 */       return (target instanceof Double && Doubles
/* 566 */         .indexOf(this.array, ((Double)target).doubleValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 572 */       if (target instanceof Double) {
/* 573 */         int i = Doubles.indexOf(this.array, ((Double)target).doubleValue(), this.start, this.end);
/* 574 */         if (i >= 0) {
/* 575 */           return i - this.start;
/*     */         }
/*     */       } 
/* 578 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 584 */       if (target instanceof Double) {
/* 585 */         int i = Doubles.lastIndexOf(this.array, ((Double)target).doubleValue(), this.start, this.end);
/* 586 */         if (i >= 0) {
/* 587 */           return i - this.start;
/*     */         }
/*     */       } 
/* 590 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Double set(int index, Double element) {
/* 595 */       Preconditions.checkElementIndex(index, size());
/* 596 */       double oldValue = this.array[this.start + index];
/*     */       
/* 598 */       this.array[this.start + index] = ((Double)Preconditions.checkNotNull(element)).doubleValue();
/* 599 */       return Double.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Double> subList(int fromIndex, int toIndex) {
/* 604 */       int size = size();
/* 605 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 606 */       if (fromIndex == toIndex) {
/* 607 */         return Collections.emptyList();
/*     */       }
/* 609 */       return new DoubleArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 614 */       if (object == this) {
/* 615 */         return true;
/*     */       }
/* 617 */       if (object instanceof DoubleArrayAsList) {
/* 618 */         DoubleArrayAsList that = (DoubleArrayAsList)object;
/* 619 */         int size = size();
/* 620 */         if (that.size() != size) {
/* 621 */           return false;
/*     */         }
/* 623 */         for (int i = 0; i < size; i++) {
/* 624 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 625 */             return false;
/*     */           }
/*     */         } 
/* 628 */         return true;
/*     */       } 
/* 630 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 635 */       int result = 1;
/* 636 */       for (int i = this.start; i < this.end; i++) {
/* 637 */         result = 31 * result + Doubles.hashCode(this.array[i]);
/*     */       }
/* 639 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 644 */       StringBuilder builder = new StringBuilder(size() * 12);
/* 645 */       builder.append('[').append(this.array[this.start]);
/* 646 */       for (int i = this.start + 1; i < this.end; i++) {
/* 647 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 649 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     double[] toDoubleArray() {
/* 653 */       return Arrays.copyOfRange(this.array, this.start, this.end);
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
/*     */   @GwtIncompatible
/* 667 */   static final Pattern FLOATING_POINT_PATTERN = fpPattern();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private static Pattern fpPattern() {
/* 678 */     String decimal = "(?:\\d+#(?:\\.\\d*#)?|\\.\\d+#)";
/* 679 */     String completeDec = String.valueOf(decimal).concat("(?:[eE][+-]?\\d+#)?[fFdD]?");
/* 680 */     String hex = "(?:[0-9a-fA-F]+#(?:\\.[0-9a-fA-F]*#)?|\\.[0-9a-fA-F]+#)";
/* 681 */     String completeHex = (new StringBuilder(25 + String.valueOf(hex).length())).append("0[xX]").append(hex).append("[pP][+-]?\\d+#[fFdD]?").toString();
/* 682 */     String fpPattern = (new StringBuilder(23 + String.valueOf(completeDec).length() + String.valueOf(completeHex).length())).append("[+-]?(?:NaN|Infinity|").append(completeDec).append("|").append(completeHex).append(")").toString();
/*     */     
/* 684 */     fpPattern = fpPattern.replace("#", "+");
/*     */ 
/*     */ 
/*     */     
/* 688 */     return 
/*     */       
/* 690 */       Pattern.compile(fpPattern);
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
/*     */   @GwtIncompatible
/*     */   public static Double tryParse(String string) {
/* 713 */     if (FLOATING_POINT_PATTERN.matcher(string).matches()) {
/*     */       
/*     */       try {
/*     */         
/* 717 */         return Double.valueOf(Double.parseDouble(string));
/* 718 */       } catch (NumberFormatException numberFormatException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 723 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\primitives\Doubles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */