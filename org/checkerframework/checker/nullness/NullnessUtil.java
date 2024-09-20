/*     */ package org.checkerframework.checker.nullness;
/*     */ 
/*     */ import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class NullnessUtil
/*     */ {
/*     */   private NullnessUtil() {
/*  32 */     throw new AssertionError("shouldn't be instantiated");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EnsuresNonNull({"#1"})
/*     */   public static <T> T castNonNull(T ref) {
/*  71 */     assert ref != null : "Misuse of castNonNull: called with a null argument";
/*  72 */     return ref;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EnsuresNonNull({"#1"})
/*     */   public static <T> T[] castNonNullDeep(T[] arr) {
/*  84 */     return castNonNullArray(arr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EnsuresNonNull({"#1"})
/*     */   public static <T> T[][] castNonNullDeep(T[][] arr) {
/*  96 */     return (T[][])castNonNullArray((Object[][])arr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EnsuresNonNull({"#1"})
/*     */   public static <T> T[][][] castNonNullDeep(T[][][] arr) {
/* 108 */     return (T[][][])castNonNullArray((Object[][][])arr);
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
/*     */   @EnsuresNonNull({"#1"})
/*     */   public static <T> T[][][][] castNonNullDeep(T[][][][] arr) {
/* 121 */     return (T[][][][])castNonNullArray((Object[][][][])arr);
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
/*     */   @EnsuresNonNull({"#1"})
/*     */   public static <T> T[][][][][] castNonNullDeep(T[][][][][] arr) {
/* 134 */     return (T[][][][][])castNonNullArray((Object[][][][][])arr);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> T[] castNonNullArray(T[] arr) {
/* 139 */     assert arr != null : "Misuse of castNonNullArray: called with a null array argument";
/* 140 */     for (int i = 0; i < arr.length; i++) {
/* 141 */       assert arr[i] != null : "Misuse of castNonNull: called with a null array element";
/* 142 */       checkIfArray(arr[i]);
/*     */     } 
/* 144 */     return arr;
/*     */   }
/*     */   
/*     */   private static void checkIfArray(Object ref) {
/* 148 */     assert ref != null : "Misuse of checkIfArray: called with a null argument";
/* 149 */     Class<?> comp = ref.getClass().getComponentType();
/* 150 */     if (comp != null)
/*     */     {
/* 152 */       if (!comp.isPrimitive())
/*     */       {
/*     */ 
/*     */         
/* 156 */         castNonNullArray((Object[])ref);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\checker\nullness\NullnessUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */