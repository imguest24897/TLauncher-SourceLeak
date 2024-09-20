/*     */ package org.checkerframework.checker.formatter.qual;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Arrays;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.checkerframework.dataflow.qual.Pure;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum ConversionCategory
/*     */ {
/*  33 */   GENERAL(null, "bBhHsS"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  40 */   CHAR(new Class[] { Character.class, Byte.class, Short.class, Integer.class }, "cC"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   INT(new Class[] { Byte.class, Short.class, Integer.class, Long.class, BigInteger.class }, "doxX"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   FLOAT(new Class[] { Float.class, Double.class, BigDecimal.class }, "eEfgGaA"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   TIME(new Class[] { Long.class, Calendar.class, Date.class }, "tT"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   CHAR_AND_INT(new Class[] { Byte.class, Short.class, Integer.class }, null),
/*     */   
/*  83 */   INT_AND_TIME(new Class[] { Long.class }, null),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   NULL(new Class[0], null),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   UNUSED(null, null);
/*     */   public final Class<?>[] types;
/*     */   
/*     */   ConversionCategory(Class<?>[] types, String chars) {
/* 112 */     this.types = types;
/* 113 */     this.chars = chars;
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
/*     */   public final String chars;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ConversionCategory fromConversionChar(char c) {
/* 135 */     for (ConversionCategory v : new ConversionCategory[] { GENERAL, CHAR, INT, FLOAT, TIME }) {
/* 136 */       if (v.chars.contains(String.valueOf(c))) {
/* 137 */         return v;
/*     */       }
/*     */     } 
/* 140 */     throw new IllegalArgumentException("Bad conversion character " + c);
/*     */   }
/*     */   
/*     */   private static <E> Set<E> arrayToSet(E[] a) {
/* 144 */     return new HashSet<>(Arrays.asList(a));
/*     */   }
/*     */   
/*     */   public static boolean isSubsetOf(ConversionCategory a, ConversionCategory b) {
/* 148 */     return (intersect(a, b) == a);
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
/*     */   public static ConversionCategory intersect(ConversionCategory a, ConversionCategory b) {
/* 167 */     if (a == UNUSED) {
/* 168 */       return b;
/*     */     }
/* 170 */     if (b == UNUSED) {
/* 171 */       return a;
/*     */     }
/* 173 */     if (a == GENERAL) {
/* 174 */       return b;
/*     */     }
/* 176 */     if (b == GENERAL) {
/* 177 */       return a;
/*     */     }
/*     */     
/* 180 */     Set<Class<? extends Object>> as = (Set)arrayToSet(a.types);
/* 181 */     Set<Class<? extends Object>> bs = (Set)arrayToSet(b.types);
/* 182 */     as.retainAll(bs);
/*     */     
/* 184 */     for (ConversionCategory v : new ConversionCategory[] { CHAR, INT, FLOAT, TIME, CHAR_AND_INT, INT_AND_TIME, NULL }) {
/*     */ 
/*     */       
/* 187 */       Set<Class<? extends Object>> vs = (Set)arrayToSet(v.types);
/* 188 */       if (vs.equals(as)) {
/* 189 */         return v;
/*     */       }
/*     */     } 
/* 192 */     throw new RuntimeException();
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
/*     */   public static ConversionCategory union(ConversionCategory a, ConversionCategory b) {
/* 211 */     if (a == UNUSED || b == UNUSED) {
/* 212 */       return UNUSED;
/*     */     }
/* 214 */     if (a == GENERAL || b == GENERAL) {
/* 215 */       return GENERAL;
/*     */     }
/* 217 */     if ((a == CHAR_AND_INT && b == INT_AND_TIME) || (a == INT_AND_TIME && b == CHAR_AND_INT))
/*     */     {
/*     */ 
/*     */       
/* 221 */       return INT;
/*     */     }
/*     */     
/* 224 */     Set<Class<? extends Object>> as = (Set)arrayToSet(a.types);
/* 225 */     Set<Class<? extends Object>> bs = (Set)arrayToSet(b.types);
/* 226 */     as.addAll(bs);
/*     */     
/* 228 */     for (ConversionCategory v : new ConversionCategory[] { NULL, CHAR_AND_INT, INT_AND_TIME, CHAR, INT, FLOAT, TIME }) {
/*     */ 
/*     */       
/* 231 */       Set<Class<? extends Object>> vs = (Set)arrayToSet(v.types);
/* 232 */       if (vs.equals(as)) {
/* 233 */         return v;
/*     */       }
/*     */     } 
/*     */     
/* 237 */     return GENERAL;
/*     */   }
/*     */   
/*     */   private String className(Class<?> cls) {
/* 241 */     if (cls == Boolean.class) {
/* 242 */       return "boolean";
/*     */     }
/* 244 */     if (cls == Character.class) {
/* 245 */       return "char";
/*     */     }
/* 247 */     if (cls == Byte.class) {
/* 248 */       return "byte";
/*     */     }
/* 250 */     if (cls == Short.class) {
/* 251 */       return "short";
/*     */     }
/* 253 */     if (cls == Integer.class) {
/* 254 */       return "int";
/*     */     }
/* 256 */     if (cls == Long.class) {
/* 257 */       return "long";
/*     */     }
/* 259 */     if (cls == Float.class) {
/* 260 */       return "float";
/*     */     }
/* 262 */     if (cls == Double.class) {
/* 263 */       return "double";
/*     */     }
/* 265 */     return cls.getSimpleName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Pure
/*     */   public String toString() {
/* 272 */     StringBuilder sb = new StringBuilder(name());
/* 273 */     sb.append(" conversion category (one of: ");
/* 274 */     boolean first = true;
/* 275 */     for (Class<? extends Object> cls : this.types) {
/* 276 */       if (!first) {
/* 277 */         sb.append(", ");
/*     */       }
/* 279 */       sb.append(className(cls));
/* 280 */       first = false;
/*     */     } 
/* 282 */     sb.append(")");
/* 283 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\checker\formatter\qual\ConversionCategory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */