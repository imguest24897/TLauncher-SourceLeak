/*     */ package org.apache.commons.text;
/*     */ 
/*     */ import java.text.Format;
/*     */ import java.text.MessageFormat;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.commons.text.matcher.StringMatcherFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 125 */     this(pattern, Locale.getDefault(Locale.Category.FORMAT));
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
/* 136 */     this(pattern, locale, (Map<String, ? extends FormatFactory>)null);
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
/*     */   public ExtendedMessageFormat(String pattern, Locale locale, Map<String, ? extends FormatFactory> registry) {
/* 150 */     super("");
/* 151 */     setLocale(locale);
/* 152 */     this
/*     */       
/* 154 */       .registry = (registry != null) ? Collections.<String, FormatFactory>unmodifiableMap(new HashMap<>(registry)) : null;
/* 155 */     applyPattern(pattern);
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
/*     */   public ExtendedMessageFormat(String pattern, Map<String, ? extends FormatFactory> registry) {
/* 167 */     this(pattern, Locale.getDefault(Locale.Category.FORMAT), registry);
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
/*     */   private void appendQuotedString(String pattern, ParsePosition pos, StringBuilder appendTo) {
/* 180 */     assert pattern.toCharArray()[pos.getIndex()] == '\'' : "Quoted string must start with quote character";
/*     */ 
/*     */ 
/*     */     
/* 184 */     if (appendTo != null) {
/* 185 */       appendTo.append('\'');
/*     */     }
/* 187 */     next(pos);
/*     */     
/* 189 */     int start = pos.getIndex();
/* 190 */     char[] c = pattern.toCharArray();
/* 191 */     for (int i = pos.getIndex(); i < pattern.length(); i++) {
/* 192 */       switch (c[pos.getIndex()]) {
/*     */         case '\'':
/* 194 */           next(pos);
/* 195 */           if (appendTo != null) {
/* 196 */             appendTo.append(c, start, pos.getIndex() - start);
/*     */           }
/*     */           return;
/*     */       } 
/* 200 */       next(pos);
/*     */     } 
/*     */     
/* 203 */     throw new IllegalArgumentException("Unterminated quoted string at position " + start);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void applyPattern(String pattern) {
/* 214 */     if (this.registry == null) {
/* 215 */       super.applyPattern(pattern);
/* 216 */       this.toPattern = super.toPattern();
/*     */       return;
/*     */     } 
/* 219 */     ArrayList<Format> foundFormats = new ArrayList<>();
/* 220 */     ArrayList<String> foundDescriptions = new ArrayList<>();
/* 221 */     StringBuilder stripCustom = new StringBuilder(pattern.length());
/*     */     
/* 223 */     ParsePosition pos = new ParsePosition(0);
/* 224 */     char[] c = pattern.toCharArray();
/* 225 */     int fmtCount = 0;
/* 226 */     while (pos.getIndex() < pattern.length()) {
/* 227 */       int start, index; Format format; String formatDescription; switch (c[pos.getIndex()]) {
/*     */         case '\'':
/* 229 */           appendQuotedString(pattern, pos, stripCustom);
/*     */           continue;
/*     */         case '{':
/* 232 */           fmtCount++;
/* 233 */           seekNonWs(pattern, pos);
/* 234 */           start = pos.getIndex();
/* 235 */           index = readArgumentIndex(pattern, next(pos));
/* 236 */           stripCustom.append('{').append(index);
/* 237 */           seekNonWs(pattern, pos);
/* 238 */           format = null;
/* 239 */           formatDescription = null;
/* 240 */           if (c[pos.getIndex()] == ',') {
/* 241 */             formatDescription = parseFormatDescription(pattern, 
/* 242 */                 next(pos));
/* 243 */             format = getFormat(formatDescription);
/* 244 */             if (format == null) {
/* 245 */               stripCustom.append(',').append(formatDescription);
/*     */             }
/*     */           } 
/* 248 */           foundFormats.add(format);
/* 249 */           foundDescriptions.add((format == null) ? null : formatDescription);
/* 250 */           if (foundFormats.size() != fmtCount) {
/* 251 */             throw new IllegalArgumentException("The validated expression is false");
/*     */           }
/* 253 */           if (foundDescriptions.size() != fmtCount) {
/* 254 */             throw new IllegalArgumentException("The validated expression is false");
/*     */           }
/* 256 */           if (c[pos.getIndex()] != '}') {
/* 257 */             throw new IllegalArgumentException("Unreadable format element at position " + start);
/*     */           }
/*     */           break;
/*     */       } 
/*     */       
/* 262 */       stripCustom.append(c[pos.getIndex()]);
/* 263 */       next(pos);
/*     */     } 
/*     */     
/* 266 */     super.applyPattern(stripCustom.toString());
/* 267 */     this.toPattern = insertFormats(super.toPattern(), foundDescriptions);
/* 268 */     if (containsElements(foundFormats)) {
/* 269 */       Format[] origFormats = getFormats();
/*     */ 
/*     */       
/* 272 */       int i = 0;
/* 273 */       for (Format f : foundFormats) {
/* 274 */         if (f != null) {
/* 275 */           origFormats[i] = f;
/*     */         }
/* 277 */         i++;
/*     */       } 
/* 279 */       super.setFormats(origFormats);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean containsElements(Collection<?> coll) {
/* 289 */     if (coll == null || coll.isEmpty()) {
/* 290 */       return false;
/*     */     }
/* 292 */     return coll.stream().anyMatch(Objects::nonNull);
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
/* 303 */     if (obj == this) {
/* 304 */       return true;
/*     */     }
/* 306 */     if (obj == null) {
/* 307 */       return false;
/*     */     }
/* 309 */     if (!Objects.equals(getClass(), obj.getClass())) {
/* 310 */       return false;
/*     */     }
/* 312 */     ExtendedMessageFormat rhs = (ExtendedMessageFormat)obj;
/* 313 */     if (!Objects.equals(this.toPattern, rhs.toPattern)) {
/* 314 */       return false;
/*     */     }
/* 316 */     if (!super.equals(obj)) {
/* 317 */       return false;
/*     */     }
/* 319 */     return Objects.equals(this.registry, rhs.registry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Format getFormat(String desc) {
/* 329 */     if (this.registry != null) {
/* 330 */       String name = desc;
/* 331 */       String args = null;
/* 332 */       int i = desc.indexOf(',');
/* 333 */       if (i > 0) {
/* 334 */         name = desc.substring(0, i).trim();
/* 335 */         args = desc.substring(i + 1).trim();
/*     */       } 
/* 337 */       FormatFactory factory = this.registry.get(name);
/* 338 */       if (factory != null) {
/* 339 */         return factory.getFormat(name, args, getLocale());
/*     */       }
/*     */     } 
/* 342 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getQuotedString(String pattern, ParsePosition pos) {
/* 352 */     appendQuotedString(pattern, pos, (StringBuilder)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 360 */     int result = super.hashCode();
/* 361 */     result = 31 * result + Objects.hashCode(this.registry);
/* 362 */     result = 31 * result + Objects.hashCode(this.toPattern);
/* 363 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String insertFormats(String pattern, ArrayList<String> customPatterns) {
/* 374 */     if (!containsElements(customPatterns)) {
/* 375 */       return pattern;
/*     */     }
/* 377 */     StringBuilder sb = new StringBuilder(pattern.length() * 2);
/* 378 */     ParsePosition pos = new ParsePosition(0);
/* 379 */     int fe = -1;
/* 380 */     int depth = 0;
/* 381 */     while (pos.getIndex() < pattern.length()) {
/* 382 */       char c = pattern.charAt(pos.getIndex());
/* 383 */       switch (c) {
/*     */         case '\'':
/* 385 */           appendQuotedString(pattern, pos, sb);
/*     */           continue;
/*     */         case '{':
/* 388 */           depth++;
/* 389 */           sb.append('{').append(readArgumentIndex(pattern, next(pos)));
/*     */           
/* 391 */           if (depth == 1) {
/* 392 */             fe++;
/* 393 */             String customPattern = customPatterns.get(fe);
/* 394 */             if (customPattern != null) {
/* 395 */               sb.append(',').append(customPattern);
/*     */             }
/*     */           } 
/*     */           continue;
/*     */         case '}':
/* 400 */           depth--;
/*     */           break;
/*     */       } 
/* 403 */       sb.append(c);
/* 404 */       next(pos);
/*     */     } 
/*     */     
/* 407 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ParsePosition next(ParsePosition pos) {
/* 417 */     pos.setIndex(pos.getIndex() + 1);
/* 418 */     return pos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String parseFormatDescription(String pattern, ParsePosition pos) {
/* 429 */     int start = pos.getIndex();
/* 430 */     seekNonWs(pattern, pos);
/* 431 */     int text = pos.getIndex();
/* 432 */     int depth = 1;
/* 433 */     while (pos.getIndex() < pattern.length()) {
/* 434 */       switch (pattern.charAt(pos.getIndex())) {
/*     */         case '{':
/* 436 */           depth++;
/* 437 */           next(pos);
/*     */           continue;
/*     */         case '}':
/* 440 */           depth--;
/* 441 */           if (depth == 0) {
/* 442 */             return pattern.substring(text, pos.getIndex());
/*     */           }
/* 444 */           next(pos);
/*     */           continue;
/*     */         case '\'':
/* 447 */           getQuotedString(pattern, pos);
/*     */           continue;
/*     */       } 
/* 450 */       next(pos);
/*     */     } 
/*     */ 
/*     */     
/* 454 */     throw new IllegalArgumentException("Unterminated format element at position " + start);
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
/*     */   private int readArgumentIndex(String pattern, ParsePosition pos) {
/* 466 */     int start = pos.getIndex();
/* 467 */     seekNonWs(pattern, pos);
/* 468 */     StringBuilder result = new StringBuilder();
/* 469 */     boolean error = false;
/* 470 */     for (; !error && pos.getIndex() < pattern.length(); next(pos)) {
/* 471 */       char c = pattern.charAt(pos.getIndex());
/* 472 */       if (Character.isWhitespace(c)) {
/* 473 */         seekNonWs(pattern, pos);
/* 474 */         c = pattern.charAt(pos.getIndex());
/* 475 */         if (c != ',' && c != '}') {
/* 476 */           error = true;
/*     */           continue;
/*     */         } 
/*     */       } 
/* 480 */       if ((c == ',' || c == '}') && result.length() > 0) {
/*     */         try {
/* 482 */           return Integer.parseInt(result.toString());
/* 483 */         } catch (NumberFormatException numberFormatException) {}
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 488 */       error = !Character.isDigit(c);
/* 489 */       result.append(c); continue;
/*     */     } 
/* 491 */     if (error) {
/* 492 */       throw new IllegalArgumentException("Invalid format argument index at position " + start + ": " + pattern
/*     */           
/* 494 */           .substring(start, pos.getIndex()));
/*     */     }
/* 496 */     throw new IllegalArgumentException("Unterminated format element at position " + start);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void seekNonWs(String pattern, ParsePosition pos) {
/* 507 */     int len = 0;
/* 508 */     char[] buffer = pattern.toCharArray();
/*     */     do {
/* 510 */       len = StringMatcherFactory.INSTANCE.splitMatcher().isMatch(buffer, pos.getIndex(), 0, buffer.length);
/* 511 */       pos.setIndex(pos.getIndex() + len);
/* 512 */     } while (len > 0 && pos.getIndex() < pattern.length());
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
/*     */   public void setFormat(int formatElementIndex, Format newFormat) {
/* 525 */     throw new UnsupportedOperationException();
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
/*     */   public void setFormatByArgumentIndex(int argumentIndex, Format newFormat) {
/* 539 */     throw new UnsupportedOperationException();
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
/*     */   public void setFormats(Format[] newFormats) {
/* 551 */     throw new UnsupportedOperationException();
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
/*     */   public void setFormatsByArgumentIndex(Format[] newFormats) {
/* 563 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toPattern() {
/* 571 */     return this.toPattern;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\ExtendedMessageFormat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */