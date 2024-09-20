/*     */ package org.apache.commons.text;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.text.translate.AggregateTranslator;
/*     */ import org.apache.commons.text.translate.CharSequenceTranslator;
/*     */ import org.apache.commons.text.translate.CsvTranslators;
/*     */ import org.apache.commons.text.translate.EntityArrays;
/*     */ import org.apache.commons.text.translate.JavaUnicodeEscaper;
/*     */ import org.apache.commons.text.translate.LookupTranslator;
/*     */ import org.apache.commons.text.translate.NumericEntityEscaper;
/*     */ import org.apache.commons.text.translate.NumericEntityUnescaper;
/*     */ import org.apache.commons.text.translate.OctalUnescaper;
/*     */ import org.apache.commons.text.translate.UnicodeUnescaper;
/*     */ import org.apache.commons.text.translate.UnicodeUnpairedSurrogateRemover;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringEscapeUtils
/*     */ {
/*     */   public static final CharSequenceTranslator ESCAPE_JAVA;
/*     */   public static final CharSequenceTranslator ESCAPE_ECMASCRIPT;
/*     */   public static final CharSequenceTranslator ESCAPE_JSON;
/*     */   public static final CharSequenceTranslator ESCAPE_XML10;
/*     */   public static final CharSequenceTranslator ESCAPE_XML11;
/*     */   
/*     */   public static final class Builder
/*     */   {
/*     */     private final StringBuilder sb;
/*     */     private final CharSequenceTranslator translator;
/*     */     
/*     */     private Builder(CharSequenceTranslator translator) {
/*  90 */       this.sb = new StringBuilder();
/*  91 */       this.translator = translator;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder append(String input) {
/* 101 */       this.sb.append(input);
/* 102 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder escape(String input) {
/* 112 */       this.sb.append(this.translator.translate(input));
/* 113 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 123 */       return this.sb.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class XsiUnescaper
/*     */     extends CharSequenceTranslator
/*     */   {
/*     */     private static final char BACKSLASH = '\\';
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int translate(CharSequence input, int index, Writer writer) throws IOException {
/* 139 */       if (index != 0) {
/* 140 */         throw new IllegalStateException("XsiUnescaper should never reach the [1] index");
/*     */       }
/*     */       
/* 143 */       String s = input.toString();
/*     */       
/* 145 */       int segmentStart = 0;
/* 146 */       int searchOffset = 0;
/*     */       while (true) {
/* 148 */         int pos = s.indexOf('\\', searchOffset);
/* 149 */         if (pos == -1) {
/* 150 */           if (segmentStart < s.length()) {
/* 151 */             writer.write(s.substring(segmentStart));
/*     */           }
/*     */           break;
/*     */         } 
/* 155 */         if (pos > segmentStart) {
/* 156 */           writer.write(s.substring(segmentStart, pos));
/*     */         }
/* 158 */         segmentStart = pos + 1;
/* 159 */         searchOffset = pos + 2;
/*     */       } 
/*     */       
/* 162 */       return Character.codePointCount(input, 0, input.length());
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
/*     */   static {
/* 175 */     Map<CharSequence, CharSequence> escapeJavaMap = new HashMap<>();
/* 176 */     escapeJavaMap.put("\"", "\\\"");
/* 177 */     escapeJavaMap.put("\\", "\\\\");
/*     */ 
/*     */ 
/*     */     
/* 181 */     ESCAPE_JAVA = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator(Collections.unmodifiableMap(escapeJavaMap)), (CharSequenceTranslator)new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE), (CharSequenceTranslator)JavaUnicodeEscaper.outsideOf(32, 127) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 194 */     Map<CharSequence, CharSequence> escapeEcmaScriptMap = new HashMap<>();
/* 195 */     escapeEcmaScriptMap.put("'", "\\'");
/* 196 */     escapeEcmaScriptMap.put("\"", "\\\"");
/* 197 */     escapeEcmaScriptMap.put("\\", "\\\\");
/* 198 */     escapeEcmaScriptMap.put("/", "\\/");
/*     */ 
/*     */ 
/*     */     
/* 202 */     ESCAPE_ECMASCRIPT = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator(Collections.unmodifiableMap(escapeEcmaScriptMap)), (CharSequenceTranslator)new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE), (CharSequenceTranslator)JavaUnicodeEscaper.outsideOf(32, 127) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 215 */     Map<CharSequence, CharSequence> escapeJsonMap = new HashMap<>();
/* 216 */     escapeJsonMap.put("\"", "\\\"");
/* 217 */     escapeJsonMap.put("\\", "\\\\");
/* 218 */     escapeJsonMap.put("/", "\\/");
/*     */ 
/*     */ 
/*     */     
/* 222 */     ESCAPE_JSON = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator(Collections.unmodifiableMap(escapeJsonMap)), (CharSequenceTranslator)new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE), (CharSequenceTranslator)JavaUnicodeEscaper.outsideOf(32, 126) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 235 */     Map<CharSequence, CharSequence> escapeXml10Map = new HashMap<>();
/* 236 */     escapeXml10Map.put("\000", "");
/* 237 */     escapeXml10Map.put("\001", "");
/* 238 */     escapeXml10Map.put("\002", "");
/* 239 */     escapeXml10Map.put("\003", "");
/* 240 */     escapeXml10Map.put("\004", "");
/* 241 */     escapeXml10Map.put("\005", "");
/* 242 */     escapeXml10Map.put("\006", "");
/* 243 */     escapeXml10Map.put("\007", "");
/* 244 */     escapeXml10Map.put("\b", "");
/* 245 */     escapeXml10Map.put("\013", "");
/* 246 */     escapeXml10Map.put("\f", "");
/* 247 */     escapeXml10Map.put("\016", "");
/* 248 */     escapeXml10Map.put("\017", "");
/* 249 */     escapeXml10Map.put("\020", "");
/* 250 */     escapeXml10Map.put("\021", "");
/* 251 */     escapeXml10Map.put("\022", "");
/* 252 */     escapeXml10Map.put("\023", "");
/* 253 */     escapeXml10Map.put("\024", "");
/* 254 */     escapeXml10Map.put("\025", "");
/* 255 */     escapeXml10Map.put("\026", "");
/* 256 */     escapeXml10Map.put("\027", "");
/* 257 */     escapeXml10Map.put("\030", "");
/* 258 */     escapeXml10Map.put("\031", "");
/* 259 */     escapeXml10Map.put("\032", "");
/* 260 */     escapeXml10Map.put("\033", "");
/* 261 */     escapeXml10Map.put("\034", "");
/* 262 */     escapeXml10Map.put("\035", "");
/* 263 */     escapeXml10Map.put("\036", "");
/* 264 */     escapeXml10Map.put("\037", "");
/* 265 */     escapeXml10Map.put("￾", "");
/* 266 */     escapeXml10Map.put("￿", "");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 272 */     ESCAPE_XML10 = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator(EntityArrays.BASIC_ESCAPE), (CharSequenceTranslator)new LookupTranslator(EntityArrays.APOS_ESCAPE), (CharSequenceTranslator)new LookupTranslator(Collections.unmodifiableMap(escapeXml10Map)), (CharSequenceTranslator)NumericEntityEscaper.between(127, 132), (CharSequenceTranslator)NumericEntityEscaper.between(134, 159), (CharSequenceTranslator)new UnicodeUnpairedSurrogateRemover() });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 287 */     Map<CharSequence, CharSequence> escapeXml11Map = new HashMap<>();
/* 288 */     escapeXml11Map.put("\000", "");
/* 289 */     escapeXml11Map.put("\013", "&#11;");
/* 290 */     escapeXml11Map.put("\f", "&#12;");
/* 291 */     escapeXml11Map.put("￾", "");
/* 292 */     escapeXml11Map.put("￿", "");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 300 */     ESCAPE_XML11 = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator(EntityArrays.BASIC_ESCAPE), (CharSequenceTranslator)new LookupTranslator(EntityArrays.APOS_ESCAPE), (CharSequenceTranslator)new LookupTranslator(Collections.unmodifiableMap(escapeXml11Map)), (CharSequenceTranslator)NumericEntityEscaper.between(1, 8), (CharSequenceTranslator)NumericEntityEscaper.between(14, 31), (CharSequenceTranslator)NumericEntityEscaper.between(127, 132), (CharSequenceTranslator)NumericEntityEscaper.between(134, 159), (CharSequenceTranslator)new UnicodeUnpairedSurrogateRemover() });
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
/* 312 */   public static final CharSequenceTranslator ESCAPE_HTML3 = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator(EntityArrays.BASIC_ESCAPE), (CharSequenceTranslator)new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 325 */   public static final CharSequenceTranslator ESCAPE_HTML4 = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator(EntityArrays.BASIC_ESCAPE), (CharSequenceTranslator)new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE), (CharSequenceTranslator)new LookupTranslator(EntityArrays.HTML40_EXTENDED_ESCAPE) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 338 */   public static final CharSequenceTranslator ESCAPE_CSV = (CharSequenceTranslator)new CsvTranslators.CsvEscaper();
/*     */ 
/*     */   
/*     */   public static final CharSequenceTranslator ESCAPE_XSI;
/*     */ 
/*     */   
/*     */   public static final CharSequenceTranslator UNESCAPE_JAVA;
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 349 */     Map<CharSequence, CharSequence> escapeXsiMap = new HashMap<>();
/* 350 */     escapeXsiMap.put("|", "\\|");
/* 351 */     escapeXsiMap.put("&", "\\&");
/* 352 */     escapeXsiMap.put(";", "\\;");
/* 353 */     escapeXsiMap.put("<", "\\<");
/* 354 */     escapeXsiMap.put(">", "\\>");
/* 355 */     escapeXsiMap.put("(", "\\(");
/* 356 */     escapeXsiMap.put(")", "\\)");
/* 357 */     escapeXsiMap.put("$", "\\$");
/* 358 */     escapeXsiMap.put("`", "\\`");
/* 359 */     escapeXsiMap.put("\\", "\\\\");
/* 360 */     escapeXsiMap.put("\"", "\\\"");
/* 361 */     escapeXsiMap.put("'", "\\'");
/* 362 */     escapeXsiMap.put(" ", "\\ ");
/* 363 */     escapeXsiMap.put("\t", "\\\t");
/* 364 */     escapeXsiMap.put("\r\n", "");
/* 365 */     escapeXsiMap.put("\n", "");
/* 366 */     escapeXsiMap.put("*", "\\*");
/* 367 */     escapeXsiMap.put("?", "\\?");
/* 368 */     escapeXsiMap.put("[", "\\[");
/* 369 */     escapeXsiMap.put("#", "\\#");
/* 370 */     escapeXsiMap.put("~", "\\~");
/* 371 */     escapeXsiMap.put("=", "\\=");
/* 372 */     escapeXsiMap.put("%", "\\%");
/*     */     
/* 374 */     ESCAPE_XSI = (CharSequenceTranslator)new LookupTranslator(Collections.unmodifiableMap(escapeXsiMap));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 388 */     Map<CharSequence, CharSequence> unescapeJavaMap = new HashMap<>();
/* 389 */     unescapeJavaMap.put("\\\\", "\\");
/* 390 */     unescapeJavaMap.put("\\\"", "\"");
/* 391 */     unescapeJavaMap.put("\\'", "'");
/* 392 */     unescapeJavaMap.put("\\", "");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 397 */     UNESCAPE_JAVA = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new OctalUnescaper(), (CharSequenceTranslator)new UnicodeUnescaper(), (CharSequenceTranslator)new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_UNESCAPE), (CharSequenceTranslator)new LookupTranslator(Collections.unmodifiableMap(unescapeJavaMap)) });
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
/* 408 */   public static final CharSequenceTranslator UNESCAPE_ECMASCRIPT = UNESCAPE_JAVA;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 417 */   public static final CharSequenceTranslator UNESCAPE_JSON = UNESCAPE_JAVA;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 426 */   public static final CharSequenceTranslator UNESCAPE_HTML3 = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator(EntityArrays.BASIC_UNESCAPE), (CharSequenceTranslator)new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE), (CharSequenceTranslator)new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0]) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 440 */   public static final CharSequenceTranslator UNESCAPE_HTML4 = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator(EntityArrays.BASIC_UNESCAPE), (CharSequenceTranslator)new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE), (CharSequenceTranslator)new LookupTranslator(EntityArrays.HTML40_EXTENDED_UNESCAPE), (CharSequenceTranslator)new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0]) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 455 */   public static final CharSequenceTranslator UNESCAPE_XML = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator(EntityArrays.BASIC_UNESCAPE), (CharSequenceTranslator)new LookupTranslator(EntityArrays.APOS_UNESCAPE), (CharSequenceTranslator)new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0]) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 469 */   public static final CharSequenceTranslator UNESCAPE_CSV = (CharSequenceTranslator)new CsvTranslators.CsvUnescaper();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 480 */   public static final CharSequenceTranslator UNESCAPE_XSI = new XsiUnescaper();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder builder(CharSequenceTranslator translator) {
/* 488 */     return new Builder(translator);
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
/*     */   public static final String escapeCsv(String input) {
/* 511 */     return ESCAPE_CSV.translate(input);
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
/*     */   public static final String escapeEcmaScript(String input) {
/* 545 */     return ESCAPE_ECMASCRIPT.translate(input);
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
/*     */   public static final String escapeHtml3(String input) {
/* 557 */     return ESCAPE_HTML3.translate(input);
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
/*     */   public static final String escapeHtml4(String input) {
/* 587 */     return ESCAPE_HTML4.translate(input);
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
/*     */   public static final String escapeJava(String input) {
/* 612 */     return ESCAPE_JAVA.translate(input);
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
/*     */   public static final String escapeJson(String input) {
/* 639 */     return ESCAPE_JSON.translate(input);
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
/*     */   public static String escapeXml10(String input) {
/* 670 */     return ESCAPE_XML10.translate(input);
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
/*     */   public static String escapeXml11(String input) {
/* 699 */     return ESCAPE_XML11.translate(input);
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
/*     */   public static final String escapeXSI(String input) {
/* 720 */     return ESCAPE_XSI.translate(input);
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
/*     */   public static final String unescapeCsv(String input) {
/* 744 */     return UNESCAPE_CSV.translate(input);
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
/*     */   public static final String unescapeEcmaScript(String input) {
/* 759 */     return UNESCAPE_ECMASCRIPT.translate(input);
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
/*     */   public static final String unescapeHtml3(String input) {
/* 771 */     return UNESCAPE_HTML3.translate(input);
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
/*     */   public static final String unescapeHtml4(String input) {
/* 790 */     return UNESCAPE_HTML4.translate(input);
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
/*     */   public static final String unescapeJava(String input) {
/* 803 */     return UNESCAPE_JAVA.translate(input);
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
/*     */   public static final String unescapeJson(String input) {
/* 818 */     return UNESCAPE_JSON.translate(input);
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
/*     */   public static final String unescapeXml(String input) {
/* 838 */     return UNESCAPE_XML.translate(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String unescapeXSI(String input) {
/* 849 */     return UNESCAPE_XSI.translate(input);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\StringEscapeUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */