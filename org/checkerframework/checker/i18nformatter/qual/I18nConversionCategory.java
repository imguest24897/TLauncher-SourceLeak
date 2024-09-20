/*     */ package org.checkerframework.checker.i18nformatter.qual;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum I18nConversionCategory
/*     */ {
/*  37 */   UNUSED(null, null),
/*     */ 
/*     */   
/*  40 */   GENERAL(null, null),
/*     */ 
/*     */   
/*  43 */   DATE((Class<? extends Object>[])new Class[] { Date.class, Number.class }, new String[] { "date", "time"
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }),
/*  54 */   NUMBER((Class<? extends Object>[])new Class[] { Number.class }, new String[] { "number", "choice" });
/*     */   
/*     */   public final Class<? extends Object>[] types;
/*     */   
/*     */   public final String[] strings;
/*     */   
/*     */   static I18nConversionCategory[] namedCategories;
/*     */   
/*     */   I18nConversionCategory(Class<? extends Object>[] types, String[] strings) {
/*  63 */     this.types = types;
/*  64 */     this.strings = strings;
/*     */   }
/*     */   
/*     */   static {
/*  68 */     namedCategories = new I18nConversionCategory[] { DATE, NUMBER };
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
/*     */   public static I18nConversionCategory stringToI18nConversionCategory(String string) {
/*  80 */     string = string.toLowerCase();
/*  81 */     for (I18nConversionCategory v : namedCategories) {
/*  82 */       for (String s : v.strings) {
/*  83 */         if (s.equals(string)) {
/*  84 */           return v;
/*     */         }
/*     */       } 
/*     */     } 
/*  88 */     throw new IllegalArgumentException("Invalid format type " + string);
/*     */   }
/*     */   
/*     */   private static <E> Set<E> arrayToSet(E[] a) {
/*  92 */     return new HashSet<>(Arrays.asList(a));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSubsetOf(I18nConversionCategory a, I18nConversionCategory b) {
/* 101 */     return (intersect(a, b) == a);
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
/*     */   public static I18nConversionCategory intersect(I18nConversionCategory a, I18nConversionCategory b) {
/* 117 */     if (a == UNUSED) {
/* 118 */       return b;
/*     */     }
/* 120 */     if (b == UNUSED) {
/* 121 */       return a;
/*     */     }
/* 123 */     if (a == GENERAL) {
/* 124 */       return b;
/*     */     }
/* 126 */     if (b == GENERAL) {
/* 127 */       return a;
/*     */     }
/*     */     
/* 130 */     Set<Class<? extends Object>> as = arrayToSet(a.types);
/* 131 */     Set<Class<? extends Object>> bs = arrayToSet(b.types);
/* 132 */     as.retainAll(bs);
/* 133 */     for (I18nConversionCategory v : new I18nConversionCategory[] { DATE, NUMBER }) {
/* 134 */       Set<Class<? extends Object>> vs = arrayToSet(v.types);
/* 135 */       if (vs.equals(as)) {
/* 136 */         return v;
/*     */       }
/*     */     } 
/* 139 */     throw new RuntimeException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static I18nConversionCategory union(I18nConversionCategory a, I18nConversionCategory b) {
/* 150 */     if (a == UNUSED || b == UNUSED) {
/* 151 */       return UNUSED;
/*     */     }
/* 153 */     if (a == GENERAL || b == GENERAL) {
/* 154 */       return GENERAL;
/*     */     }
/* 156 */     if (a == DATE || b == DATE) {
/* 157 */       return DATE;
/*     */     }
/* 159 */     return NUMBER;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 165 */     StringBuilder sb = new StringBuilder(name());
/* 166 */     if (this.types == null) {
/* 167 */       sb.append(" conversion category (all types)");
/*     */     } else {
/* 169 */       sb.append(" conversion category (one of: ");
/* 170 */       boolean first = true;
/* 171 */       for (Class<? extends Object> cls : this.types) {
/* 172 */         if (!first) {
/* 173 */           sb.append(", ");
/*     */         }
/* 175 */         sb.append(cls.getCanonicalName());
/* 176 */         first = false;
/*     */       } 
/* 178 */       sb.append(")");
/*     */     } 
/* 180 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\checker\i18nformatter\qual\I18nConversionCategory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */