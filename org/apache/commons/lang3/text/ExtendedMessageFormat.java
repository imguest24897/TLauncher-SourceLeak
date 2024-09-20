/*     */ package org.apache.commons.lang3.text;
/*     */ 
/*     */ import java.text.Format;
/*     */ import java.text.MessageFormat;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.commons.lang3.LocaleUtils;
/*     */ import org.apache.commons.lang3.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class ExtendedMessageFormat
/*     */   extends MessageFormat
/*     */ {
/*     */   private static final long serialVersionUID = -2362048321261811743L;
/*     */   private static final int HASH_SEED = 31;
/*     */   private static final String DUMMY_PATTERN = "";
/*     */   private static final char START_FMT = ',';
/*     */   private static final char END_FE = '}';
/*     */   private static final char START_FE = '{';
/*     */   private static final char QUOTE = '\'';
/*     */   private String toPattern;
/*     */   private final Map<String, ? extends FormatFactory> registry;
/*     */   
/*     */   public ExtendedMessageFormat(String pattern) {
/* 100 */     this(pattern, Locale.getDefault());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendedMessageFormat(String pattern, Locale locale) {
/* 111 */     this(pattern, locale, (Map<String, ? extends FormatFactory>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendedMessageFormat(String pattern, Map<String, ? extends FormatFactory> registry) {
/* 122 */     this(pattern, Locale.getDefault(), registry);
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
/*     */   public ExtendedMessageFormat(String pattern, Locale locale, Map<String, ? extends FormatFactory> registry) {
/* 134 */     super("");
/* 135 */     setLocale(LocaleUtils.toLocale(locale));
/* 136 */     this.registry = registry;
/* 137 */     applyPattern(pattern);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toPattern() {
/* 145 */     return this.toPattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void applyPattern(String pattern) {
/* 155 */     if (this.registry == null) {
/* 156 */       super.applyPattern(pattern);
/* 157 */       this.toPattern = super.toPattern();
/*     */       return;
/*     */     } 
/* 160 */     ArrayList<Format> foundFormats = new ArrayList<>();
/* 161 */     ArrayList<String> foundDescriptions = new ArrayList<>();
/* 162 */     StringBuilder stripCustom = new StringBuilder(pattern.length());
/*     */     
/* 164 */     ParsePosition pos = new ParsePosition(0);
/* 165 */     char[] c = pattern.toCharArray();
/* 166 */     int fmtCount = 0;
/* 167 */     while (pos.getIndex() < pattern.length()) {
/* 168 */       int start, index; Format format; String formatDescription; switch (c[pos.getIndex()]) {
/*     */         case '\'':
/* 170 */           appendQuotedString(pattern, pos, stripCustom);
/*     */           continue;
/*     */         case '{':
/* 173 */           fmtCount++;
/* 174 */           seekNonWs(pattern, pos);
/* 175 */           start = pos.getIndex();
/* 176 */           index = readArgumentIndex(pattern, next(pos));
/* 177 */           stripCustom.append('{').append(index);
/* 178 */           seekNonWs(pattern, pos);
/* 179 */           format = null;
/* 180 */           formatDescription = null;
/* 181 */           if (c[pos.getIndex()] == ',') {
/* 182 */             formatDescription = parseFormatDescription(pattern, 
/* 183 */                 next(pos));
/* 184 */             format = getFormat(formatDescription);
/* 185 */             if (format == null) {
/* 186 */               stripCustom.append(',').append(formatDescription);
/*     */             }
/*     */           } 
/* 189 */           foundFormats.add(format);
/* 190 */           foundDescriptions.add((format == null) ? null : formatDescription);
/* 191 */           Validate.isTrue((foundFormats.size() == fmtCount));
/* 192 */           Validate.isTrue((foundDescriptions.size() == fmtCount));
/* 193 */           if (c[pos.getIndex()] != '}') {
/* 194 */             throw new IllegalArgumentException("Unreadable format element at position " + start);
/*     */           }
/*     */           break;
/*     */       } 
/*     */       
/* 199 */       stripCustom.append(c[pos.getIndex()]);
/* 200 */       next(pos);
/*     */     } 
/*     */     
/* 203 */     super.applyPattern(stripCustom.toString());
/* 204 */     this.toPattern = insertFormats(super.toPattern(), foundDescriptions);
/* 205 */     if (containsElements(foundFormats)) {
/* 206 */       Format[] origFormats = getFormats();
/*     */ 
/*     */       
/* 209 */       int i = 0;
/* 210 */       for (Format f : foundFormats) {
/* 211 */         if (f != null) {
/* 212 */           origFormats[i] = f;
/*     */         }
/* 214 */         i++;
/*     */       } 
/* 216 */       super.setFormats(origFormats);
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
/*     */   public void setFormat(int formatElementIndex, Format newFormat) {
/* 229 */     throw new UnsupportedOperationException();
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
/*     */   public void setFormatByArgumentIndex(int argumentIndex, Format newFormat) {
/* 241 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFormats(Format[] newFormats) {
/* 252 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFormatsByArgumentIndex(Format[] newFormats) {
/* 263 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 274 */     if (obj == this) {
/* 275 */       return true;
/*     */     }
/* 277 */     if (obj == null) {
/* 278 */       return false;
/*     */     }
/* 280 */     if (!super.equals(obj)) {
/* 281 */       return false;
/*     */     }
/* 283 */     if (ObjectUtils.notEqual(getClass(), obj.getClass())) {
/* 284 */       return false;
/*     */     }
/* 286 */     ExtendedMessageFormat rhs = (ExtendedMessageFormat)obj;
/* 287 */     if (ObjectUtils.notEqual(this.toPattern, rhs.toPattern)) {
/* 288 */       return false;
/*     */     }
/* 290 */     return !ObjectUtils.notEqual(this.registry, rhs.registry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 298 */     int result = super.hashCode();
/* 299 */     result = 31 * result + Objects.hashCode(this.registry);
/* 300 */     result = 31 * result + Objects.hashCode(this.toPattern);
/* 301 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Format getFormat(String desc) {
/* 311 */     if (this.registry != null) {
/* 312 */       String name = desc;
/* 313 */       String args = null;
/* 314 */       int i = desc.indexOf(',');
/* 315 */       if (i > 0) {
/* 316 */         name = desc.substring(0, i).trim();
/* 317 */         args = desc.substring(i + 1).trim();
/*     */       } 
/* 319 */       FormatFactory factory = this.registry.get(name);
/* 320 */       if (factory != null) {
/* 321 */         return factory.getFormat(name, args, getLocale());
/*     */       }
/*     */     } 
/* 324 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int readArgumentIndex(String pattern, ParsePosition pos) {
/* 335 */     int start = pos.getIndex();
/* 336 */     seekNonWs(pattern, pos);
/* 337 */     StringBuilder result = new StringBuilder();
/* 338 */     boolean error = false;
/* 339 */     for (; !error && pos.getIndex() < pattern.length(); next(pos)) {
/* 340 */       char c = pattern.charAt(pos.getIndex());
/* 341 */       if (Character.isWhitespace(c)) {
/* 342 */         seekNonWs(pattern, pos);
/* 343 */         c = pattern.charAt(pos.getIndex());
/* 344 */         if (c != ',' && c != '}') {
/* 345 */           error = true;
/*     */           continue;
/*     */         } 
/*     */       } 
/* 349 */       if ((c == ',' || c == '}') && result.length() > 0) {
/*     */         try {
/* 351 */           return Integer.parseInt(result.toString());
/* 352 */         } catch (NumberFormatException numberFormatException) {}
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 357 */       error = !Character.isDigit(c);
/* 358 */       result.append(c); continue;
/*     */     } 
/* 360 */     if (error) {
/* 361 */       throw new IllegalArgumentException("Invalid format argument index at position " + start + ": " + pattern
/*     */           
/* 363 */           .substring(start, pos.getIndex()));
/*     */     }
/* 365 */     throw new IllegalArgumentException("Unterminated format element at position " + start);
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
/*     */   private String parseFormatDescription(String pattern, ParsePosition pos) {
/* 377 */     int start = pos.getIndex();
/* 378 */     seekNonWs(pattern, pos);
/* 379 */     int text = pos.getIndex();
/* 380 */     int depth = 1;
/* 381 */     for (; pos.getIndex() < pattern.length(); next(pos)) {
/* 382 */       switch (pattern.charAt(pos.getIndex())) {
/*     */         case '{':
/* 384 */           depth++;
/*     */           break;
/*     */         case '}':
/* 387 */           depth--;
/* 388 */           if (depth == 0) {
/* 389 */             return pattern.substring(text, pos.getIndex());
/*     */           }
/*     */           break;
/*     */         case '\'':
/* 393 */           getQuotedString(pattern, pos);
/*     */           break;
/*     */       } 
/*     */ 
/*     */     
/*     */     } 
/* 399 */     throw new IllegalArgumentException("Unterminated format element at position " + start);
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
/*     */   private String insertFormats(String pattern, ArrayList<String> customPatterns) {
/* 411 */     if (!containsElements(customPatterns)) {
/* 412 */       return pattern;
/*     */     }
/* 414 */     StringBuilder sb = new StringBuilder(pattern.length() * 2);
/* 415 */     ParsePosition pos = new ParsePosition(0);
/* 416 */     int fe = -1;
/* 417 */     int depth = 0;
/* 418 */     while (pos.getIndex() < pattern.length()) {
/* 419 */       char c = pattern.charAt(pos.getIndex());
/* 420 */       switch (c) {
/*     */         case '\'':
/* 422 */           appendQuotedString(pattern, pos, sb);
/*     */           continue;
/*     */         case '{':
/* 425 */           depth++;
/* 426 */           sb.append('{').append(readArgumentIndex(pattern, next(pos)));
/*     */           
/* 428 */           if (depth == 1) {
/* 429 */             fe++;
/* 430 */             String customPattern = customPatterns.get(fe);
/* 431 */             if (customPattern != null) {
/* 432 */               sb.append(',').append(customPattern);
/*     */             }
/*     */           } 
/*     */           continue;
/*     */         case '}':
/* 437 */           depth--;
/*     */           break;
/*     */       } 
/* 440 */       sb.append(c);
/* 441 */       next(pos);
/*     */     } 
/*     */     
/* 444 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void seekNonWs(String pattern, ParsePosition pos) {
/*     */     int len;
/* 455 */     char[] buffer = pattern.toCharArray();
/*     */     do {
/* 457 */       len = StrMatcher.splitMatcher().isMatch(buffer, pos.getIndex());
/* 458 */       pos.setIndex(pos.getIndex() + len);
/* 459 */     } while (len > 0 && pos.getIndex() < pattern.length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ParsePosition next(ParsePosition pos) {
/* 469 */     pos.setIndex(pos.getIndex() + 1);
/* 470 */     return pos;
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
/*     */   private StringBuilder appendQuotedString(String pattern, ParsePosition pos, StringBuilder appendTo) {
/* 484 */     assert pattern.toCharArray()[pos.getIndex()] == '\'' : "Quoted string must start with quote character";
/*     */ 
/*     */ 
/*     */     
/* 488 */     if (appendTo != null) {
/* 489 */       appendTo.append('\'');
/*     */     }
/* 491 */     next(pos);
/*     */     
/* 493 */     int start = pos.getIndex();
/* 494 */     char[] c = pattern.toCharArray();
/* 495 */     for (int i = pos.getIndex(); i < pattern.length(); i++) {
/* 496 */       if (c[pos.getIndex()] == '\'') {
/* 497 */         next(pos);
/* 498 */         return (appendTo == null) ? null : appendTo.append(c, start, pos
/* 499 */             .getIndex() - start);
/*     */       } 
/* 501 */       next(pos);
/*     */     } 
/* 503 */     throw new IllegalArgumentException("Unterminated quoted string at position " + start);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getQuotedString(String pattern, ParsePosition pos) {
/* 514 */     appendQuotedString(pattern, pos, (StringBuilder)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean containsElements(Collection<?> coll) {
/* 523 */     if (coll == null || coll.isEmpty()) {
/* 524 */       return false;
/*     */     }
/* 526 */     return coll.stream().anyMatch(Objects::nonNull);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\text\ExtendedMessageFormat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */