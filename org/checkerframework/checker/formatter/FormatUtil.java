/*     */ package org.checkerframework.checker.formatter;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.IllegalFormatConversionException;
/*     */ import java.util.IllegalFormatException;
/*     */ import java.util.Map;
/*     */ import java.util.MissingFormatArgumentException;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.checkerframework.checker.formatter.qual.ConversionCategory;
/*     */ import org.checkerframework.checker.formatter.qual.ReturnsFormat;
/*     */ 
/*     */ public class FormatUtil {
/*     */   private static final String formatSpecifier = "%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])";
/*     */   
/*     */   private static class Conversion {
/*     */     private final int index;
/*     */     
/*     */     public Conversion(char c, int index) {
/*  21 */       this.index = index;
/*  22 */       this.cath = ConversionCategory.fromConversionChar(c);
/*     */     }
/*     */     private final ConversionCategory cath;
/*     */     int index() {
/*  26 */       return this.index;
/*     */     }
/*     */     
/*     */     ConversionCategory category() {
/*  30 */       return this.cath;
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
/*     */   @ReturnsFormat
/*     */   public static String asFormat(String format, ConversionCategory... cc) throws IllegalFormatException {
/*  43 */     ConversionCategory[] fcc = formatParameterCategories(format);
/*  44 */     if (fcc.length != cc.length) {
/*  45 */       throw new ExcessiveOrMissingFormatArgumentException(cc.length, fcc.length);
/*     */     }
/*     */     
/*  48 */     for (int i = 0; i < cc.length; i++) {
/*  49 */       if (cc[i] != fcc[i]) {
/*  50 */         throw new IllegalFormatConversionCategoryException(cc[i], fcc[i]);
/*     */       }
/*     */     } 
/*     */     
/*  54 */     return format;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void tryFormatSatisfiability(String format) throws IllegalFormatException {
/*  60 */     String unused = String.format(format, (Object[])null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ConversionCategory[] formatParameterCategories(String format) throws IllegalFormatException {
/*  70 */     tryFormatSatisfiability(format);
/*     */     
/*  72 */     int last = -1;
/*  73 */     int lasto = -1;
/*  74 */     int maxindex = -1;
/*     */     
/*  76 */     Conversion[] cs = parse(format);
/*  77 */     Map<Integer, ConversionCategory> conv = new HashMap<>();
/*     */     
/*  79 */     for (Conversion c : cs) {
/*  80 */       int index = c.index();
/*  81 */       switch (index) {
/*     */         case -1:
/*     */           break;
/*     */         
/*     */         case 0:
/*  86 */           last = ++lasto;
/*     */           break;
/*     */         default:
/*  89 */           last = index - 1;
/*     */           break;
/*     */       } 
/*  92 */       maxindex = Math.max(maxindex, last);
/*  93 */       conv.put(
/*  94 */           Integer.valueOf(last), 
/*  95 */           ConversionCategory.intersect(
/*  96 */             conv.containsKey(Integer.valueOf(last)) ? conv.get(Integer.valueOf(last)) : ConversionCategory.UNUSED, c
/*  97 */             .category()));
/*     */     } 
/*     */     
/* 100 */     ConversionCategory[] res = new ConversionCategory[maxindex + 1];
/* 101 */     for (int i = 0; i <= maxindex; i++) {
/* 102 */       res[i] = conv.containsKey(Integer.valueOf(i)) ? conv.get(Integer.valueOf(i)) : ConversionCategory.UNUSED;
/*     */     }
/* 104 */     return res;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   private static Pattern fsPattern = Pattern.compile("%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])");
/*     */   
/*     */   private static int indexFromFormat(Matcher m) {
/*     */     int index;
/* 115 */     String s = m.group(1);
/* 116 */     if (s != null) {
/* 117 */       index = Integer.parseInt(s.substring(0, s.length() - 1));
/*     */     }
/* 119 */     else if (m.group(2) != null && m.group(2).contains(String.valueOf('<'))) {
/* 120 */       index = -1;
/*     */     } else {
/* 122 */       index = 0;
/*     */     } 
/*     */     
/* 125 */     return index;
/*     */   }
/*     */   
/*     */   private static char conversionCharFromFormat(Matcher m) {
/* 129 */     String dt = m.group(5);
/* 130 */     if (dt == null) {
/* 131 */       return m.group(6).charAt(0);
/*     */     }
/* 133 */     return dt.charAt(0);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Conversion[] parse(String format) {
/* 138 */     ArrayList<Conversion> cs = new ArrayList<>();
/* 139 */     Matcher m = fsPattern.matcher(format);
/* 140 */     while (m.find()) {
/* 141 */       char c = conversionCharFromFormat(m);
/* 142 */       switch (c) {
/*     */         case '%':
/*     */         case 'n':
/*     */           continue;
/*     */       } 
/* 147 */       cs.add(new Conversion(c, indexFromFormat(m)));
/*     */     } 
/*     */     
/* 150 */     return cs.<Conversion>toArray(new Conversion[cs.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ExcessiveOrMissingFormatArgumentException
/*     */     extends MissingFormatArgumentException
/*     */   {
/*     */     private static final long serialVersionUID = 17000126L;
/*     */     
/*     */     private final int expected;
/*     */     
/*     */     private final int found;
/*     */ 
/*     */     
/*     */     public ExcessiveOrMissingFormatArgumentException(int expected, int found) {
/* 165 */       super("-");
/* 166 */       this.expected = expected;
/* 167 */       this.found = found;
/*     */     }
/*     */     
/*     */     public int getExpected() {
/* 171 */       return this.expected;
/*     */     }
/*     */     
/*     */     public int getFound() {
/* 175 */       return this.found;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getMessage() {
/* 180 */       return String.format("Expected %d arguments but found %d.", new Object[] { Integer.valueOf(this.expected), Integer.valueOf(this.found) });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class IllegalFormatConversionCategoryException
/*     */     extends IllegalFormatConversionException
/*     */   {
/*     */     private static final long serialVersionUID = 17000126L;
/*     */     
/*     */     private final ConversionCategory expected;
/*     */     
/*     */     private final ConversionCategory found;
/*     */ 
/*     */     
/*     */     public IllegalFormatConversionCategoryException(ConversionCategory expected, ConversionCategory found) {
/* 196 */       super(
/* 197 */           (expected.chars.length() == 0) ? 45 : expected.chars.charAt(0), 
/* 198 */           (found.types == null) ? Object.class : found.types[0]);
/* 199 */       this.expected = expected;
/* 200 */       this.found = found;
/*     */     }
/*     */     
/*     */     public ConversionCategory getExpected() {
/* 204 */       return this.expected;
/*     */     }
/*     */     
/*     */     public ConversionCategory getFound() {
/* 208 */       return this.found;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getMessage() {
/* 213 */       return String.format("Expected category %s but found %s.", new Object[] { this.expected, this.found });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\checker\formatter\FormatUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */