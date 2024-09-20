/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public enum CaseFormat
/*     */ {
/*  33 */   LOWER_HYPHEN(CharMatcher.is('-'), "-")
/*     */   {
/*     */     String normalizeWord(String word) {
/*  36 */       return Ascii.toLowerCase(word);
/*     */     }
/*     */ 
/*     */     
/*     */     String convert(CaseFormat format, String s) {
/*  41 */       if (format == LOWER_UNDERSCORE) {
/*  42 */         return s.replace('-', '_');
/*     */       }
/*  44 */       if (format == UPPER_UNDERSCORE) {
/*  45 */         return Ascii.toUpperCase(s.replace('-', '_'));
/*     */       }
/*  47 */       return super.convert(format, s);
/*     */     }
/*     */   },
/*     */ 
/*     */   
/*  52 */   LOWER_UNDERSCORE(CharMatcher.is('_'), "_")
/*     */   {
/*     */     String normalizeWord(String word) {
/*  55 */       return Ascii.toLowerCase(word);
/*     */     }
/*     */ 
/*     */     
/*     */     String convert(CaseFormat format, String s) {
/*  60 */       if (format == LOWER_HYPHEN) {
/*  61 */         return s.replace('_', '-');
/*     */       }
/*  63 */       if (format == UPPER_UNDERSCORE) {
/*  64 */         return Ascii.toUpperCase(s);
/*     */       }
/*  66 */       return super.convert(format, s);
/*     */     }
/*     */   },
/*     */ 
/*     */   
/*  71 */   LOWER_CAMEL(CharMatcher.inRange('A', 'Z'), "")
/*     */   {
/*     */     String normalizeWord(String word) {
/*  74 */       return firstCharOnlyToUpper(word);
/*     */     }
/*     */ 
/*     */     
/*     */     String normalizeFirstWord(String word) {
/*  79 */       return Ascii.toLowerCase(word);
/*     */     }
/*     */   },
/*     */ 
/*     */   
/*  84 */   UPPER_CAMEL(CharMatcher.inRange('A', 'Z'), "")
/*     */   {
/*     */     String normalizeWord(String word) {
/*  87 */       return firstCharOnlyToUpper(word);
/*     */     }
/*     */   },
/*     */ 
/*     */   
/*  92 */   UPPER_UNDERSCORE(CharMatcher.is('_'), "_")
/*     */   {
/*     */     String normalizeWord(String word) {
/*  95 */       return Ascii.toUpperCase(word);
/*     */     }
/*     */ 
/*     */     
/*     */     String convert(CaseFormat format, String s) {
/* 100 */       if (format == LOWER_HYPHEN) {
/* 101 */         return Ascii.toLowerCase(s.replace('_', '-'));
/*     */       }
/* 103 */       if (format == LOWER_UNDERSCORE) {
/* 104 */         return Ascii.toLowerCase(s);
/*     */       }
/* 106 */       return super.convert(format, s);
/*     */     }
/*     */   };
/*     */   
/*     */   private final CharMatcher wordBoundary;
/*     */   private final String wordSeparator;
/*     */   
/*     */   CaseFormat(CharMatcher wordBoundary, String wordSeparator) {
/* 114 */     this.wordBoundary = wordBoundary;
/* 115 */     this.wordSeparator = wordSeparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String to(CaseFormat format, String str) {
/* 124 */     Preconditions.checkNotNull(format);
/* 125 */     Preconditions.checkNotNull(str);
/* 126 */     return (format == this) ? str : convert(format, str);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   String convert(CaseFormat format, String s) {
/* 132 */     StringBuilder out = null;
/* 133 */     int i = 0;
/* 134 */     int j = -1;
/* 135 */     while ((j = this.wordBoundary.indexIn(s, ++j)) != -1) {
/* 136 */       if (i == 0) {
/*     */         
/* 138 */         out = new StringBuilder(s.length() + 4 * format.wordSeparator.length());
/* 139 */         out.append(format.normalizeFirstWord(s.substring(i, j)));
/*     */       } else {
/* 141 */         out.append(format.normalizeWord(s.substring(i, j)));
/*     */       } 
/* 143 */       out.append(format.wordSeparator);
/* 144 */       i = j + this.wordSeparator.length();
/*     */     } 
/* 146 */     return (i == 0) ? 
/* 147 */       format.normalizeFirstWord(s) : 
/* 148 */       out.append(format.normalizeWord(s.substring(i))).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Converter<String, String> converterTo(CaseFormat targetFormat) {
/* 157 */     return new StringConverter(this, targetFormat);
/*     */   }
/*     */   
/*     */   private static final class StringConverter
/*     */     extends Converter<String, String> implements Serializable {
/*     */     private final CaseFormat sourceFormat;
/*     */     private final CaseFormat targetFormat;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     StringConverter(CaseFormat sourceFormat, CaseFormat targetFormat) {
/* 167 */       this.sourceFormat = Preconditions.<CaseFormat>checkNotNull(sourceFormat);
/* 168 */       this.targetFormat = Preconditions.<CaseFormat>checkNotNull(targetFormat);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doForward(String s) {
/* 173 */       return this.sourceFormat.to(this.targetFormat, s);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(String s) {
/* 178 */       return this.targetFormat.to(this.sourceFormat, s);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 183 */       if (object instanceof StringConverter) {
/* 184 */         StringConverter that = (StringConverter)object;
/* 185 */         return (this.sourceFormat.equals(that.sourceFormat) && this.targetFormat.equals(that.targetFormat));
/*     */       } 
/* 187 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 192 */       return this.sourceFormat.hashCode() ^ this.targetFormat.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 197 */       String str1 = String.valueOf(this.sourceFormat), str2 = String.valueOf(this.targetFormat); return (new StringBuilder(14 + String.valueOf(str1).length() + String.valueOf(str2).length())).append(str1).append(".converterTo(").append(str2).append(")").toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String normalizeFirstWord(String word) {
/* 206 */     return normalizeWord(word);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String firstCharOnlyToUpper(String word) {
/* 212 */     char c = Ascii.toUpperCase(word.charAt(0)); String str = Ascii.toLowerCase(word.substring(1)); return word.isEmpty() ? word : (new StringBuilder(1 + String.valueOf(str).length())).append(c).append(str).toString();
/*     */   }
/*     */   
/*     */   abstract String normalizeWord(String paramString);
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\base\CaseFormat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */