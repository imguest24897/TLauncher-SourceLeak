/*      */ package org.apache.commons.lang3;
/*      */ 
/*      */ import java.util.Iterator;
/*      */ import java.util.Objects;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.regex.Pattern;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Validate
/*      */ {
/*      */   private static final String DEFAULT_NOT_NAN_EX_MESSAGE = "The validated value is not a number";
/*      */   private static final String DEFAULT_FINITE_EX_MESSAGE = "The value is invalid: %f";
/*      */   private static final String DEFAULT_EXCLUSIVE_BETWEEN_EX_MESSAGE = "The value %s is not in the specified exclusive range of %s to %s";
/*      */   private static final String DEFAULT_INCLUSIVE_BETWEEN_EX_MESSAGE = "The value %s is not in the specified inclusive range of %s to %s";
/*      */   private static final String DEFAULT_MATCHES_PATTERN_EX = "The string %s does not match the pattern %s";
/*      */   private static final String DEFAULT_IS_NULL_EX_MESSAGE = "The validated object is null";
/*      */   private static final String DEFAULT_IS_TRUE_EX_MESSAGE = "The validated expression is false";
/*      */   private static final String DEFAULT_NO_NULL_ELEMENTS_ARRAY_EX_MESSAGE = "The validated array contains null element at index: %d";
/*      */   private static final String DEFAULT_NO_NULL_ELEMENTS_COLLECTION_EX_MESSAGE = "The validated collection contains null element at index: %d";
/*      */   private static final String DEFAULT_NOT_BLANK_EX_MESSAGE = "The validated character sequence is blank";
/*      */   private static final String DEFAULT_NOT_EMPTY_ARRAY_EX_MESSAGE = "The validated array is empty";
/*      */   private static final String DEFAULT_NOT_EMPTY_CHAR_SEQUENCE_EX_MESSAGE = "The validated character sequence is empty";
/*      */   private static final String DEFAULT_NOT_EMPTY_COLLECTION_EX_MESSAGE = "The validated collection is empty";
/*      */   private static final String DEFAULT_NOT_EMPTY_MAP_EX_MESSAGE = "The validated map is empty";
/*      */   private static final String DEFAULT_VALID_INDEX_ARRAY_EX_MESSAGE = "The validated array index is invalid: %d";
/*      */   private static final String DEFAULT_VALID_INDEX_CHAR_SEQUENCE_EX_MESSAGE = "The validated character sequence index is invalid: %d";
/*      */   private static final String DEFAULT_VALID_INDEX_COLLECTION_EX_MESSAGE = "The validated collection index is invalid: %d";
/*      */   private static final String DEFAULT_VALID_STATE_EX_MESSAGE = "The validated state is false";
/*      */   private static final String DEFAULT_IS_ASSIGNABLE_EX_MESSAGE = "Cannot assign a %s to a %s";
/*      */   private static final String DEFAULT_IS_INSTANCE_OF_EX_MESSAGE = "Expected type: %s, actual: %s";
/*      */   
/*      */   public static void isTrue(boolean expression, String message, long value) {
/*  106 */     if (!expression) {
/*  107 */       throw new IllegalArgumentException(String.format(message, new Object[] { Long.valueOf(value) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void isTrue(boolean expression, String message, double value) {
/*  131 */     if (!expression) {
/*  132 */       throw new IllegalArgumentException(String.format(message, new Object[] { Double.valueOf(value) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void isTrue(boolean expression, String message, Object... values) {
/*  154 */     if (!expression) {
/*  155 */       throw new IllegalArgumentException(getMessage(message, values));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void isTrue(boolean expression) {
/*  179 */     if (!expression) {
/*  180 */       throw new IllegalArgumentException("The validated expression is false");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <T> T notNull(T object) {
/*  202 */     return notNull(object, "The validated object is null", new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T notNull(T object, String message, Object... values) {
/*  220 */     return Objects.requireNonNull(object, toSupplier(message, values));
/*      */   }
/*      */   
/*      */   private static Supplier<String> toSupplier(String message, Object... values) {
/*  224 */     return () -> getMessage(message, values);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T[] notEmpty(T[] array, String message, Object... values) {
/*  244 */     Objects.requireNonNull(array, toSupplier(message, values));
/*  245 */     if (array.length == 0) {
/*  246 */       throw new IllegalArgumentException(getMessage(message, values));
/*      */     }
/*  248 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T[] notEmpty(T[] array) {
/*  268 */     return notEmpty(array, "The validated array is empty", new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends java.util.Collection<?>> T notEmpty(T collection, String message, Object... values) {
/*  288 */     Objects.requireNonNull(collection, toSupplier(message, values));
/*  289 */     if (collection.isEmpty()) {
/*  290 */       throw new IllegalArgumentException(getMessage(message, values));
/*      */     }
/*  292 */     return collection;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends java.util.Collection<?>> T notEmpty(T collection) {
/*  312 */     return notEmpty(collection, "The validated collection is empty", new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends java.util.Map<?, ?>> T notEmpty(T map, String message, Object... values) {
/*  332 */     Objects.requireNonNull(map, toSupplier(message, values));
/*  333 */     if (map.isEmpty()) {
/*  334 */       throw new IllegalArgumentException(getMessage(message, values));
/*      */     }
/*  336 */     return map;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends java.util.Map<?, ?>> T notEmpty(T map) {
/*  356 */     return notEmpty(map, "The validated map is empty", new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends CharSequence> T notEmpty(T chars, String message, Object... values) {
/*  376 */     Objects.requireNonNull(chars, toSupplier(message, values));
/*  377 */     if (chars.length() == 0) {
/*  378 */       throw new IllegalArgumentException(getMessage(message, values));
/*      */     }
/*  380 */     return chars;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends CharSequence> T notEmpty(T chars) {
/*  401 */     return notEmpty(chars, "The validated character sequence is empty", new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends CharSequence> T notBlank(T chars, String message, Object... values) {
/*  423 */     Objects.requireNonNull(chars, toSupplier(message, values));
/*  424 */     if (StringUtils.isBlank((CharSequence)chars)) {
/*  425 */       throw new IllegalArgumentException(getMessage(message, values));
/*      */     }
/*  427 */     return chars;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends CharSequence> T notBlank(T chars) {
/*  449 */     return notBlank(chars, "The validated character sequence is blank", new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T[] noNullElements(T[] array, String message, Object... values) {
/*  476 */     Objects.requireNonNull(array, "array");
/*  477 */     for (int i = 0; i < array.length; i++) {
/*  478 */       if (array[i] == null) {
/*  479 */         Object[] values2 = ArrayUtils.add(values, Integer.valueOf(i));
/*  480 */         throw new IllegalArgumentException(getMessage(message, values2));
/*      */       } 
/*      */     } 
/*  483 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T[] noNullElements(T[] array) {
/*  508 */     return noNullElements(array, "The validated array contains null element at index: %d", new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends Iterable<?>> T noNullElements(T iterable, String message, Object... values) {
/*  535 */     Objects.requireNonNull(iterable, "iterable");
/*  536 */     int i = 0;
/*  537 */     for (Iterator<?> it = iterable.iterator(); it.hasNext(); i++) {
/*  538 */       if (it.next() == null) {
/*  539 */         Object[] values2 = ArrayUtils.addAll(values, new Object[] { Integer.valueOf(i) });
/*  540 */         throw new IllegalArgumentException(getMessage(message, values2));
/*      */       } 
/*      */     } 
/*  543 */     return iterable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends Iterable<?>> T noNullElements(T iterable) {
/*  568 */     return noNullElements(iterable, "The validated collection contains null element at index: %d", new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T[] validIndex(T[] array, int index, String message, Object... values) {
/*  592 */     Objects.requireNonNull(array, "array");
/*  593 */     if (index < 0 || index >= array.length) {
/*  594 */       throw new IndexOutOfBoundsException(getMessage(message, values));
/*      */     }
/*  596 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T[] validIndex(T[] array, int index) {
/*  622 */     return validIndex(array, index, "The validated array index is invalid: %d", new Object[] { Integer.valueOf(index) });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends java.util.Collection<?>> T validIndex(T collection, int index, String message, Object... values) {
/*  646 */     Objects.requireNonNull(collection, "collection");
/*  647 */     if (index < 0 || index >= collection.size()) {
/*  648 */       throw new IndexOutOfBoundsException(getMessage(message, values));
/*      */     }
/*  650 */     return collection;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends java.util.Collection<?>> T validIndex(T collection, int index) {
/*  673 */     return validIndex(collection, index, "The validated collection index is invalid: %d", new Object[] { Integer.valueOf(index) });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends CharSequence> T validIndex(T chars, int index, String message, Object... values) {
/*  698 */     Objects.requireNonNull(chars, "chars");
/*  699 */     if (index < 0 || index >= chars.length()) {
/*  700 */       throw new IndexOutOfBoundsException(getMessage(message, values));
/*      */     }
/*  702 */     return chars;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends CharSequence> T validIndex(T chars, int index) {
/*  729 */     return validIndex(chars, index, "The validated character sequence index is invalid: %d", new Object[] { Integer.valueOf(index) });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void validState(boolean expression) {
/*  751 */     if (!expression) {
/*  752 */       throw new IllegalStateException("The validated state is false");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void validState(boolean expression, String message, Object... values) {
/*  772 */     if (!expression) {
/*  773 */       throw new IllegalStateException(getMessage(message, values));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void matchesPattern(CharSequence input, String pattern) {
/*  793 */     if (!Pattern.matches(pattern, input)) {
/*  794 */       throw new IllegalArgumentException(String.format("The string %s does not match the pattern %s", new Object[] { input, pattern }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void matchesPattern(CharSequence input, String pattern, String message, Object... values) {
/*  816 */     if (!Pattern.matches(pattern, input)) {
/*  817 */       throw new IllegalArgumentException(getMessage(message, values));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void notNaN(double value) {
/*  836 */     notNaN(value, "The validated value is not a number", new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void notNaN(double value, String message, Object... values) {
/*  853 */     if (Double.isNaN(value)) {
/*  854 */       throw new IllegalArgumentException(getMessage(message, values));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void finite(double value) {
/*  872 */     finite(value, "The value is invalid: %f", new Object[] { Double.valueOf(value) });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void finite(double value, String message, Object... values) {
/*  889 */     if (Double.isNaN(value) || Double.isInfinite(value)) {
/*  890 */       throw new IllegalArgumentException(getMessage(message, values));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> void inclusiveBetween(T start, T end, Comparable<T> value) {
/*  910 */     if (value.compareTo(start) < 0 || value.compareTo(end) > 0) {
/*  911 */       throw new IllegalArgumentException(String.format("The value %s is not in the specified inclusive range of %s to %s", new Object[] { value, start, end }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> void inclusiveBetween(T start, T end, Comparable<T> value, String message, Object... values) {
/*  934 */     if (value.compareTo(start) < 0 || value.compareTo(end) > 0) {
/*  935 */       throw new IllegalArgumentException(getMessage(message, values));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void inclusiveBetween(long start, long end, long value) {
/*  954 */     if (value < start || value > end) {
/*  955 */       throw new IllegalArgumentException(String.format("The value %s is not in the specified inclusive range of %s to %s", new Object[] { Long.valueOf(value), Long.valueOf(start), Long.valueOf(end) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void inclusiveBetween(long start, long end, long value, String message) {
/*  975 */     if (value < start || value > end) {
/*  976 */       throw new IllegalArgumentException(message);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void inclusiveBetween(double start, double end, double value) {
/*  995 */     if (value < start || value > end) {
/*  996 */       throw new IllegalArgumentException(String.format("The value %s is not in the specified inclusive range of %s to %s", new Object[] { Double.valueOf(value), Double.valueOf(start), Double.valueOf(end) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void inclusiveBetween(double start, double end, double value, String message) {
/* 1016 */     if (value < start || value > end) {
/* 1017 */       throw new IllegalArgumentException(message);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> void exclusiveBetween(T start, T end, Comparable<T> value) {
/* 1037 */     if (value.compareTo(start) <= 0 || value.compareTo(end) >= 0) {
/* 1038 */       throw new IllegalArgumentException(String.format("The value %s is not in the specified exclusive range of %s to %s", new Object[] { value, start, end }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> void exclusiveBetween(T start, T end, Comparable<T> value, String message, Object... values) {
/* 1061 */     if (value.compareTo(start) <= 0 || value.compareTo(end) >= 0) {
/* 1062 */       throw new IllegalArgumentException(getMessage(message, values));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void exclusiveBetween(long start, long end, long value) {
/* 1081 */     if (value <= start || value >= end) {
/* 1082 */       throw new IllegalArgumentException(String.format("The value %s is not in the specified exclusive range of %s to %s", new Object[] { Long.valueOf(value), Long.valueOf(start), Long.valueOf(end) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void exclusiveBetween(long start, long end, long value, String message) {
/* 1102 */     if (value <= start || value >= end) {
/* 1103 */       throw new IllegalArgumentException(message);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void exclusiveBetween(double start, double end, double value) {
/* 1122 */     if (value <= start || value >= end) {
/* 1123 */       throw new IllegalArgumentException(String.format("The value %s is not in the specified exclusive range of %s to %s", new Object[] { Double.valueOf(value), Double.valueOf(start), Double.valueOf(end) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void exclusiveBetween(double start, double end, double value, String message) {
/* 1143 */     if (value <= start || value >= end) {
/* 1144 */       throw new IllegalArgumentException(message);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void isInstanceOf(Class<?> type, Object obj) {
/* 1165 */     if (!type.isInstance(obj)) {
/* 1166 */       throw new IllegalArgumentException(String.format("Expected type: %s, actual: %s", new Object[] { type.getName(), ClassUtils.getName(obj, "null") }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void isInstanceOf(Class<?> type, Object obj, String message, Object... values) {
/* 1188 */     if (!type.isInstance(obj)) {
/* 1189 */       throw new IllegalArgumentException(getMessage(message, values));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void isAssignableFrom(Class<?> superType, Class<?> type) {
/* 1210 */     if (type == null || superType == null || !superType.isAssignableFrom(type)) {
/* 1211 */       throw new IllegalArgumentException(
/* 1212 */           String.format("Cannot assign a %s to a %s", new Object[] { ClassUtils.getName(type, "null type"), ClassUtils.getName(superType, "null type") }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void isAssignableFrom(Class<?> superType, Class<?> type, String message, Object... values) {
/* 1235 */     if (!superType.isAssignableFrom(type)) {
/* 1236 */       throw new IllegalArgumentException(getMessage(message, values));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String getMessage(String message, Object... values) {
/* 1253 */     return ArrayUtils.isEmpty(values) ? message : String.format(message, values);
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\Validate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */