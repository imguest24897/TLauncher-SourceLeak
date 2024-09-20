/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EnumUtils
/*     */ {
/*     */   private static final String CANNOT_STORE_S_S_VALUES_IN_S_BITS = "Cannot store %s %s values in %s bits";
/*     */   private static final String ENUM_CLASS_MUST_BE_DEFINED = "EnumClass must be defined.";
/*     */   private static final String NULL_ELEMENTS_NOT_PERMITTED = "null elements not permitted";
/*     */   private static final String S_DOES_NOT_SEEM_TO_BE_AN_ENUM_TYPE = "%s does not seem to be an Enum type";
/*     */   
/*     */   private static <E extends Enum<E>> Class<E> asEnum(Class<E> enumClass) {
/*  54 */     Objects.requireNonNull(enumClass, "EnumClass must be defined.");
/*  55 */     Validate.isTrue(enumClass.isEnum(), "%s does not seem to be an Enum type", new Object[] { enumClass });
/*  56 */     return enumClass;
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
/*     */   private static <E extends Enum<E>> Class<E> checkBitVectorable(Class<E> enumClass) {
/*  69 */     Enum[] arrayOfEnum = asEnum(enumClass).getEnumConstants();
/*  70 */     Validate.isTrue((arrayOfEnum.length <= 64), "Cannot store %s %s values in %s bits", new Object[] {
/*  71 */           Integer.valueOf(arrayOfEnum.length), enumClass.getSimpleName(), Integer.valueOf(64)
/*     */         });
/*  73 */     return enumClass;
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
/*     */   @SafeVarargs
/*     */   public static <E extends Enum<E>> long generateBitVector(Class<E> enumClass, E... values) {
/*  95 */     Validate.noNullElements(values);
/*  96 */     return generateBitVector(enumClass, Arrays.asList(values));
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
/*     */   public static <E extends Enum<E>> long generateBitVector(Class<E> enumClass, Iterable<? extends E> values) {
/* 118 */     checkBitVectorable(enumClass);
/* 119 */     Objects.requireNonNull(values, "values");
/* 120 */     long total = 0L;
/* 121 */     for (Enum enum_ : values) {
/* 122 */       Objects.requireNonNull(enum_, "null elements not permitted");
/* 123 */       total |= 1L << enum_.ordinal();
/*     */     } 
/* 125 */     return total;
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
/*     */   @SafeVarargs
/*     */   public static <E extends Enum<E>> long[] generateBitVectors(Class<E> enumClass, E... values) {
/* 146 */     asEnum(enumClass);
/* 147 */     Validate.noNullElements(values);
/* 148 */     EnumSet<E> condensed = EnumSet.noneOf(enumClass);
/* 149 */     Collections.addAll(condensed, values);
/* 150 */     long[] result = new long[(((Enum[])enumClass.getEnumConstants()).length - 1) / 64 + 1];
/* 151 */     for (Enum enum_ : condensed) {
/* 152 */       result[enum_.ordinal() / 64] = result[enum_.ordinal() / 64] | 1L << enum_.ordinal() % 64;
/*     */     }
/* 154 */     ArrayUtils.reverse(result);
/* 155 */     return result;
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
/*     */   public static <E extends Enum<E>> long[] generateBitVectors(Class<E> enumClass, Iterable<? extends E> values) {
/* 175 */     asEnum(enumClass);
/* 176 */     Objects.requireNonNull(values, "values");
/* 177 */     EnumSet<E> condensed = EnumSet.noneOf(enumClass);
/* 178 */     values.forEach(constant -> condensed.add(Objects.requireNonNull(constant, "null elements not permitted")));
/* 179 */     long[] result = new long[(((Enum[])enumClass.getEnumConstants()).length - 1) / 64 + 1];
/* 180 */     for (Enum enum_ : condensed) {
/* 181 */       result[enum_.ordinal() / 64] = result[enum_.ordinal() / 64] | 1L << enum_.ordinal() % 64;
/*     */     }
/* 183 */     ArrayUtils.reverse(result);
/* 184 */     return result;
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
/*     */   public static <E extends Enum<E>> E getEnum(Class<E> enumClass, String enumName) {
/* 199 */     return getEnum(enumClass, enumName, null);
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
/*     */   public static <E extends Enum<E>> E getEnum(Class<E> enumClass, String enumName, E defaultEnum) {
/* 216 */     if (enumName == null) {
/* 217 */       return defaultEnum;
/*     */     }
/*     */     try {
/* 220 */       return Enum.valueOf(enumClass, enumName);
/* 221 */     } catch (IllegalArgumentException ex) {
/* 222 */       return defaultEnum;
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
/*     */   public static <E extends Enum<E>> E getEnumIgnoreCase(Class<E> enumClass, String enumName) {
/* 239 */     return getEnumIgnoreCase(enumClass, enumName, null);
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
/*     */   public static <E extends Enum<E>> E getEnumIgnoreCase(Class<E> enumClass, String enumName, E defaultEnum) {
/* 257 */     return getFirstEnumIgnoreCase(enumClass, enumName, Enum::name, defaultEnum);
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
/*     */   public static <E extends Enum<E>> List<E> getEnumList(Class<E> enumClass) {
/* 270 */     return new ArrayList<>(Arrays.asList(enumClass.getEnumConstants()));
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
/*     */   public static <E extends Enum<E>> Map<String, E> getEnumMap(Class<E> enumClass) {
/* 283 */     return getEnumMap(enumClass, Enum::name);
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
/*     */   public static <E extends Enum<E>, K> Map<K, E> getEnumMap(Class<E> enumClass, Function<E, K> keyFunction) {
/* 301 */     return (Map<K, E>)Stream.of((Object[])enumClass.getEnumConstants()).collect(Collectors.toMap(keyFunction::apply, Function.identity()));
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
/*     */   public static <E extends Enum<E>> E getEnumSystemProperty(Class<E> enumClass, String propName, E defaultEnum) {
/* 320 */     return (enumClass == null || propName == null) ? defaultEnum : 
/* 321 */       getEnum(enumClass, System.getProperty(propName), defaultEnum);
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
/*     */   public static <E extends Enum<E>> E getFirstEnumIgnoreCase(Class<E> enumClass, String enumName, Function<E, String> stringFunction, E defaultEnum) {
/* 340 */     if (enumName == null || !enumClass.isEnum()) {
/* 341 */       return defaultEnum;
/*     */     }
/* 343 */     return (E)Stream.<Enum>of((Enum[])enumClass.getEnumConstants()).filter(e -> enumName.equalsIgnoreCase(stringFunction.apply(e))).findFirst().orElse((Enum)defaultEnum);
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
/*     */   public static <E extends Enum<E>> boolean isValidEnum(Class<E> enumClass, String enumName) {
/* 358 */     return (getEnum(enumClass, enumName) != null);
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
/*     */   public static <E extends Enum<E>> boolean isValidEnumIgnoreCase(Class<E> enumClass, String enumName) {
/* 375 */     return (getEnumIgnoreCase(enumClass, enumName) != null);
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
/*     */   public static <E extends Enum<E>> EnumSet<E> processBitVector(Class<E> enumClass, long value) {
/* 392 */     checkBitVectorable(enumClass).getEnumConstants();
/* 393 */     return processBitVectors(enumClass, new long[] { value });
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
/*     */   public static <E extends Enum<E>> EnumSet<E> processBitVectors(Class<E> enumClass, long... values) {
/* 410 */     EnumSet<E> results = EnumSet.noneOf(asEnum(enumClass));
/* 411 */     long[] lvalues = ArrayUtils.clone(Objects.<long[]>requireNonNull(values, "values"));
/* 412 */     ArrayUtils.reverse(lvalues);
/* 413 */     for (Enum enum_ : (Enum[])enumClass.getEnumConstants()) {
/* 414 */       int block = enum_.ordinal() / 64;
/* 415 */       if (block < lvalues.length && (lvalues[block] & 1L << enum_.ordinal() % 64) != 0L) {
/* 416 */         results.add((E)enum_);
/*     */       }
/*     */     } 
/* 419 */     return results;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\EnumUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */