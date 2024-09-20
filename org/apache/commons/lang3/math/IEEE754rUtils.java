/*     */ package org.apache.commons.lang3.math;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IEEE754rUtils
/*     */ {
/*     */   public static double min(double... array) {
/*  42 */     Objects.requireNonNull(array, "array");
/*  43 */     Validate.isTrue((array.length != 0), "Array cannot be empty.", new Object[0]);
/*     */ 
/*     */     
/*  46 */     double min = array[0];
/*  47 */     for (int i = 1; i < array.length; i++) {
/*  48 */       min = min(array[i], min);
/*     */     }
/*     */     
/*  51 */     return min;
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
/*     */   public static float min(float... array) {
/*  64 */     Objects.requireNonNull(array, "array");
/*  65 */     Validate.isTrue((array.length != 0), "Array cannot be empty.", new Object[0]);
/*     */ 
/*     */     
/*  68 */     float min = array[0];
/*  69 */     for (int i = 1; i < array.length; i++) {
/*  70 */       min = min(array[i], min);
/*     */     }
/*     */     
/*  73 */     return min;
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
/*     */   public static double min(double a, double b, double c) {
/*  87 */     return min(min(a, b), c);
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
/*     */   public static double min(double a, double b) {
/* 100 */     if (Double.isNaN(a)) {
/* 101 */       return b;
/*     */     }
/* 103 */     if (Double.isNaN(b)) {
/* 104 */       return a;
/*     */     }
/* 106 */     return Math.min(a, b);
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
/*     */   public static float min(float a, float b, float c) {
/* 120 */     return min(min(a, b), c);
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
/*     */   public static float min(float a, float b) {
/* 133 */     if (Float.isNaN(a)) {
/* 134 */       return b;
/*     */     }
/* 136 */     if (Float.isNaN(b)) {
/* 137 */       return a;
/*     */     }
/* 139 */     return Math.min(a, b);
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
/*     */   public static double max(double... array) {
/* 152 */     Objects.requireNonNull(array, "array");
/* 153 */     Validate.isTrue((array.length != 0), "Array cannot be empty.", new Object[0]);
/*     */ 
/*     */     
/* 156 */     double max = array[0];
/* 157 */     for (int j = 1; j < array.length; j++) {
/* 158 */       max = max(array[j], max);
/*     */     }
/*     */     
/* 161 */     return max;
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
/*     */   public static float max(float... array) {
/* 174 */     Objects.requireNonNull(array, "array");
/* 175 */     Validate.isTrue((array.length != 0), "Array cannot be empty.", new Object[0]);
/*     */ 
/*     */     
/* 178 */     float max = array[0];
/* 179 */     for (int j = 1; j < array.length; j++) {
/* 180 */       max = max(array[j], max);
/*     */     }
/*     */     
/* 183 */     return max;
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
/*     */   public static double max(double a, double b, double c) {
/* 197 */     return max(max(a, b), c);
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
/*     */   public static double max(double a, double b) {
/* 210 */     if (Double.isNaN(a)) {
/* 211 */       return b;
/*     */     }
/* 213 */     if (Double.isNaN(b)) {
/* 214 */       return a;
/*     */     }
/* 216 */     return Math.max(a, b);
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
/*     */   public static float max(float a, float b, float c) {
/* 230 */     return max(max(a, b), c);
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
/*     */   public static float max(float a, float b) {
/* 243 */     if (Float.isNaN(a)) {
/* 244 */       return b;
/*     */     }
/* 246 */     if (Float.isNaN(b)) {
/* 247 */       return a;
/*     */     }
/* 249 */     return Math.max(a, b);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\math\IEEE754rUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */