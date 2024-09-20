/*     */ package org.checkerframework.checker.i18nformatter;
/*     */ 
/*     */ import java.text.ChoiceFormat;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.DecimalFormatSymbols;
/*     */ import java.text.MessageFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.IllegalFormatException;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.checkerframework.checker.i18nformatter.qual.I18nChecksFormat;
/*     */ import org.checkerframework.checker.i18nformatter.qual.I18nConversionCategory;
/*     */ import org.checkerframework.checker.i18nformatter.qual.I18nValidFormat;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class I18nFormatUtil
/*     */ {
/*     */   public static void tryFormatSatisfiability(String format) throws IllegalFormatException {
/*  31 */     MessageFormat.format(format, (Object[])null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static I18nConversionCategory[] formatParameterCategories(String format) throws IllegalFormatException {
/*  42 */     tryFormatSatisfiability(format);
/*  43 */     I18nConversion[] cs = MessageFormatParser.parse(format);
/*     */     
/*  45 */     int maxIndex = -1;
/*  46 */     Map<Integer, I18nConversionCategory> conv = new HashMap<>();
/*     */     
/*  48 */     for (I18nConversion c : cs) {
/*  49 */       int index = c.index;
/*  50 */       conv.put(
/*  51 */           Integer.valueOf(index), 
/*  52 */           I18nConversionCategory.intersect(c.category, 
/*     */             
/*  54 */             conv.containsKey(Integer.valueOf(index)) ? 
/*  55 */             conv.get(Integer.valueOf(index)) : 
/*  56 */             I18nConversionCategory.UNUSED));
/*  57 */       maxIndex = Math.max(maxIndex, index);
/*     */     } 
/*     */     
/*  60 */     I18nConversionCategory[] res = new I18nConversionCategory[maxIndex + 1];
/*  61 */     for (int i = 0; i <= maxIndex; i++) {
/*  62 */       res[i] = conv.containsKey(Integer.valueOf(i)) ? conv.get(Integer.valueOf(i)) : I18nConversionCategory.UNUSED;
/*     */     }
/*  64 */     return res;
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
/*     */   @I18nChecksFormat
/*     */   public static boolean hasFormat(String format, I18nConversionCategory... cc) {
/*  78 */     I18nConversionCategory[] fcc = formatParameterCategories(format);
/*  79 */     if (fcc.length != cc.length) {
/*  80 */       return false;
/*     */     }
/*     */     
/*  83 */     for (int i = 0; i < cc.length; i++) {
/*  84 */       if (!I18nConversionCategory.isSubsetOf(cc[i], fcc[i])) {
/*  85 */         return false;
/*     */       }
/*     */     } 
/*  88 */     return true;
/*     */   }
/*     */   
/*     */   @I18nValidFormat
/*     */   public static boolean isFormat(String format) {
/*     */     try {
/*  94 */       formatParameterCategories(format);
/*  95 */     } catch (Exception e) {
/*  96 */       return false;
/*     */     } 
/*  98 */     return true;
/*     */   }
/*     */   
/*     */   private static class I18nConversion {
/*     */     public int index;
/*     */     public I18nConversionCategory category;
/*     */     
/*     */     public I18nConversion(int index, I18nConversionCategory category) {
/* 106 */       this.index = index;
/* 107 */       this.category = category;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 112 */       return this.category.toString() + "(index: " + this.index + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MessageFormatParser
/*     */   {
/*     */     public static int maxOffset;
/*     */     
/*     */     private static Locale locale;
/*     */     
/*     */     private static List<I18nConversionCategory> categories;
/*     */     
/*     */     private static List<Integer> argumentIndices;
/*     */     
/*     */     private static int numFormat;
/*     */     
/*     */     private static final int SEG_RAW = 0;
/*     */     
/*     */     private static final int SEG_INDEX = 1;
/*     */     
/*     */     private static final int SEG_TYPE = 2;
/*     */     
/*     */     private static final int SEG_MODIFIER = 3;
/*     */     
/*     */     private static final int TYPE_NULL = 0;
/*     */     
/*     */     private static final int TYPE_NUMBER = 1;
/*     */     
/*     */     private static final int TYPE_DATE = 2;
/*     */     
/*     */     private static final int TYPE_TIME = 3;
/*     */     
/*     */     private static final int TYPE_CHOICE = 4;
/*     */     
/* 148 */     private static final String[] TYPE_KEYWORDS = new String[] { "", "number", "date", "time", "choice" };
/*     */     
/*     */     private static final int MODIFIER_DEFAULT = 0;
/*     */     
/*     */     private static final int MODIFIER_CURRENCY = 1;
/*     */     
/*     */     private static final int MODIFIER_PERCENT = 2;
/*     */     private static final int MODIFIER_INTEGER = 3;
/* 156 */     private static final String[] NUMBER_MODIFIER_KEYWORDS = new String[] { "", "currency", "percent", "integer" };
/*     */ 
/*     */ 
/*     */     
/* 160 */     private static final String[] DATE_TIME_MODIFIER_KEYWORDS = new String[] { "", "short", "medium", "long", "full" };
/*     */ 
/*     */ 
/*     */     
/*     */     public static I18nFormatUtil.I18nConversion[] parse(String pattern) {
/* 165 */       categories = new ArrayList<>();
/* 166 */       argumentIndices = new ArrayList<>();
/* 167 */       locale = Locale.getDefault(Locale.Category.FORMAT);
/* 168 */       applyPattern(pattern);
/*     */       
/* 170 */       I18nFormatUtil.I18nConversion[] ret = new I18nFormatUtil.I18nConversion[numFormat];
/* 171 */       for (int i = 0; i < numFormat; i++) {
/* 172 */         ret[i] = new I18nFormatUtil.I18nConversion(((Integer)argumentIndices.get(i)).intValue(), categories.get(i));
/*     */       }
/* 174 */       return ret;
/*     */     }
/*     */     
/*     */     private static void applyPattern(String pattern) {
/* 178 */       StringBuilder[] segments = new StringBuilder[4];
/*     */ 
/*     */       
/* 181 */       segments[0] = new StringBuilder();
/*     */       
/* 183 */       int part = 0;
/* 184 */       numFormat = 0;
/* 185 */       boolean inQuote = false;
/* 186 */       int braceStack = 0;
/* 187 */       maxOffset = -1;
/* 188 */       for (int i = 0; i < pattern.length(); i++) {
/* 189 */         char ch = pattern.charAt(i);
/* 190 */         if (part == 0) {
/* 191 */           if (ch == '\'') {
/* 192 */             if (i + 1 < pattern.length() && pattern.charAt(i + 1) == '\'') {
/* 193 */               segments[part].append(ch);
/* 194 */               i++;
/*     */             } else {
/* 196 */               inQuote = !inQuote;
/*     */             } 
/* 198 */           } else if (ch == '{' && !inQuote) {
/* 199 */             part = 1;
/* 200 */             if (segments[1] == null) {
/* 201 */               segments[1] = new StringBuilder();
/*     */             }
/*     */           } else {
/* 204 */             segments[part].append(ch);
/*     */           }
/*     */         
/* 207 */         } else if (inQuote) {
/* 208 */           segments[part].append(ch);
/* 209 */           if (ch == '\'') {
/* 210 */             inQuote = false;
/*     */           }
/*     */         } else {
/* 213 */           switch (ch) {
/*     */             case ',':
/* 215 */               if (part < 3) {
/* 216 */                 if (segments[++part] == null)
/* 217 */                   segments[part] = new StringBuilder(); 
/*     */                 break;
/*     */               } 
/* 220 */               segments[part].append(ch);
/*     */               break;
/*     */             
/*     */             case '{':
/* 224 */               braceStack++;
/* 225 */               segments[part].append(ch);
/*     */               break;
/*     */             case '}':
/* 228 */               if (braceStack == 0) {
/* 229 */                 part = 0;
/* 230 */                 makeFormat(numFormat, segments);
/* 231 */                 numFormat++;
/*     */                 
/* 233 */                 segments[1] = null;
/* 234 */                 segments[2] = null;
/* 235 */                 segments[3] = null; break;
/*     */               } 
/* 237 */               braceStack--;
/* 238 */               segments[part].append(ch);
/*     */               break;
/*     */ 
/*     */             
/*     */             case ' ':
/* 243 */               if (part != 2 || segments[2].length() > 0) {
/* 244 */                 segments[part].append(ch);
/*     */               }
/*     */               break;
/*     */             case '\'':
/* 248 */               inQuote = true;
/* 249 */               segments[part].append(ch);
/*     */               break;
/*     */             default:
/* 252 */               segments[part].append(ch);
/*     */               break;
/*     */           } 
/*     */         
/*     */         } 
/*     */       } 
/* 258 */       if (braceStack == 0 && part != 0) {
/* 259 */         maxOffset = -1;
/* 260 */         throw new IllegalArgumentException("Unmatched braces in the pattern");
/*     */       } 
/*     */     }
/*     */     
/*     */     private static void makeFormat(int offsetNumber, StringBuilder[] textSegments) {
/*     */       int argumentNumber;
/* 266 */       String[] segments = new String[textSegments.length];
/* 267 */       for (int i = 0; i < textSegments.length; i++) {
/* 268 */         StringBuilder oneseg = textSegments[i];
/* 269 */         segments[i] = (oneseg != null) ? oneseg.toString() : "";
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 275 */         argumentNumber = Integer.parseInt(segments[1]);
/*     */       }
/* 277 */       catch (NumberFormatException e) {
/* 278 */         throw new IllegalArgumentException("can't parse argument number: " + segments[1], e);
/*     */       } 
/*     */       
/* 281 */       if (argumentNumber < 0) {
/* 282 */         throw new IllegalArgumentException("negative argument number: " + argumentNumber);
/*     */       }
/*     */       
/* 285 */       int oldMaxOffset = maxOffset;
/* 286 */       maxOffset = offsetNumber;
/* 287 */       argumentIndices.add(Integer.valueOf(argumentNumber));
/*     */ 
/*     */       
/* 290 */       I18nConversionCategory category = null;
/* 291 */       if (segments[2].length() != 0) {
/* 292 */         int mod, type = findKeyword(segments[2], TYPE_KEYWORDS);
/* 293 */         switch (type) {
/*     */           case 0:
/* 295 */             category = I18nConversionCategory.GENERAL;
/*     */             break;
/*     */           case 1:
/* 298 */             switch (findKeyword(segments[3], NUMBER_MODIFIER_KEYWORDS)) {
/*     */               case 0:
/*     */               case 1:
/*     */               case 2:
/*     */               case 3:
/*     */                 break;
/*     */               default:
/*     */                 try {
/* 306 */                   new DecimalFormat(segments[3], 
/*     */                       
/* 308 */                       DecimalFormatSymbols.getInstance(locale));
/* 309 */                 } catch (IllegalArgumentException e) {
/* 310 */                   maxOffset = oldMaxOffset;
/*     */                   
/* 312 */                   throw e;
/*     */                 } 
/*     */                 break;
/*     */             } 
/* 316 */             category = I18nConversionCategory.NUMBER;
/*     */             break;
/*     */           case 2:
/*     */           case 3:
/* 320 */             mod = findKeyword(segments[3], DATE_TIME_MODIFIER_KEYWORDS);
/* 321 */             if (mod < 0 || mod >= DATE_TIME_MODIFIER_KEYWORDS.length) {
/*     */               
/*     */               try {
/*     */ 
/*     */                 
/* 326 */                 new SimpleDateFormat(segments[3], locale);
/* 327 */               } catch (IllegalArgumentException e) {
/* 328 */                 maxOffset = oldMaxOffset;
/*     */                 
/* 330 */                 throw e;
/*     */               } 
/*     */             }
/* 333 */             category = I18nConversionCategory.DATE;
/*     */             break;
/*     */           case 4:
/* 336 */             if (segments[3].length() == 0) {
/* 337 */               throw new IllegalArgumentException("Choice Pattern requires Subformat Pattern: " + segments[3]);
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/* 343 */               new ChoiceFormat(segments[3]);
/* 344 */             } catch (Exception e) {
/* 345 */               maxOffset = oldMaxOffset;
/*     */               
/* 347 */               throw new IllegalArgumentException("Choice Pattern incorrect: " + segments[3], e);
/*     */             } 
/*     */             
/* 350 */             category = I18nConversionCategory.NUMBER;
/*     */             break;
/*     */           default:
/* 353 */             maxOffset = oldMaxOffset;
/* 354 */             throw new IllegalArgumentException("unknown format type: " + segments[2]);
/*     */         } 
/*     */       
/*     */       } else {
/* 358 */         category = I18nConversionCategory.GENERAL;
/*     */       } 
/* 360 */       categories.add(category);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static final int findKeyword(String s, String[] list) {
/* 368 */       for (int i = 0; i < list.length; i++) {
/* 369 */         if (s.equals(list[i])) {
/* 370 */           return i;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 375 */       String ls = s.trim().toLowerCase(Locale.ROOT);
/* 376 */       if (ls != s) {
/* 377 */         for (int j = 0; j < list.length; j++) {
/* 378 */           if (ls.equals(list[j])) {
/* 379 */             return j;
/*     */           }
/*     */         } 
/*     */       }
/* 383 */       return -1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\checker\i18nformatter\I18nFormatUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */