/*     */ package org.apache.commons.text.translate;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityArrays
/*     */ {
/*     */   public static final Map<CharSequence, CharSequence> ISO8859_1_ESCAPE;
/*     */   
/*     */   static {
/*  41 */     Map<CharSequence, CharSequence> initialMap = new HashMap<>();
/*  42 */     initialMap.put(" ", "&nbsp;");
/*  43 */     initialMap.put("¡", "&iexcl;");
/*  44 */     initialMap.put("¢", "&cent;");
/*  45 */     initialMap.put("£", "&pound;");
/*  46 */     initialMap.put("¤", "&curren;");
/*  47 */     initialMap.put("¥", "&yen;");
/*  48 */     initialMap.put("¦", "&brvbar;");
/*  49 */     initialMap.put("§", "&sect;");
/*  50 */     initialMap.put("¨", "&uml;");
/*  51 */     initialMap.put("©", "&copy;");
/*  52 */     initialMap.put("ª", "&ordf;");
/*  53 */     initialMap.put("«", "&laquo;");
/*  54 */     initialMap.put("¬", "&not;");
/*  55 */     initialMap.put("­", "&shy;");
/*  56 */     initialMap.put("®", "&reg;");
/*  57 */     initialMap.put("¯", "&macr;");
/*  58 */     initialMap.put("°", "&deg;");
/*  59 */     initialMap.put("±", "&plusmn;");
/*  60 */     initialMap.put("²", "&sup2;");
/*  61 */     initialMap.put("³", "&sup3;");
/*  62 */     initialMap.put("´", "&acute;");
/*  63 */     initialMap.put("µ", "&micro;");
/*  64 */     initialMap.put("¶", "&para;");
/*  65 */     initialMap.put("·", "&middot;");
/*  66 */     initialMap.put("¸", "&cedil;");
/*  67 */     initialMap.put("¹", "&sup1;");
/*  68 */     initialMap.put("º", "&ordm;");
/*  69 */     initialMap.put("»", "&raquo;");
/*  70 */     initialMap.put("¼", "&frac14;");
/*  71 */     initialMap.put("½", "&frac12;");
/*  72 */     initialMap.put("¾", "&frac34;");
/*  73 */     initialMap.put("¿", "&iquest;");
/*  74 */     initialMap.put("À", "&Agrave;");
/*  75 */     initialMap.put("Á", "&Aacute;");
/*  76 */     initialMap.put("Â", "&Acirc;");
/*  77 */     initialMap.put("Ã", "&Atilde;");
/*  78 */     initialMap.put("Ä", "&Auml;");
/*  79 */     initialMap.put("Å", "&Aring;");
/*  80 */     initialMap.put("Æ", "&AElig;");
/*  81 */     initialMap.put("Ç", "&Ccedil;");
/*  82 */     initialMap.put("È", "&Egrave;");
/*  83 */     initialMap.put("É", "&Eacute;");
/*  84 */     initialMap.put("Ê", "&Ecirc;");
/*  85 */     initialMap.put("Ë", "&Euml;");
/*  86 */     initialMap.put("Ì", "&Igrave;");
/*  87 */     initialMap.put("Í", "&Iacute;");
/*  88 */     initialMap.put("Î", "&Icirc;");
/*  89 */     initialMap.put("Ï", "&Iuml;");
/*  90 */     initialMap.put("Ð", "&ETH;");
/*  91 */     initialMap.put("Ñ", "&Ntilde;");
/*  92 */     initialMap.put("Ò", "&Ograve;");
/*  93 */     initialMap.put("Ó", "&Oacute;");
/*  94 */     initialMap.put("Ô", "&Ocirc;");
/*  95 */     initialMap.put("Õ", "&Otilde;");
/*  96 */     initialMap.put("Ö", "&Ouml;");
/*  97 */     initialMap.put("×", "&times;");
/*  98 */     initialMap.put("Ø", "&Oslash;");
/*  99 */     initialMap.put("Ù", "&Ugrave;");
/* 100 */     initialMap.put("Ú", "&Uacute;");
/* 101 */     initialMap.put("Û", "&Ucirc;");
/* 102 */     initialMap.put("Ü", "&Uuml;");
/* 103 */     initialMap.put("Ý", "&Yacute;");
/* 104 */     initialMap.put("Þ", "&THORN;");
/* 105 */     initialMap.put("ß", "&szlig;");
/* 106 */     initialMap.put("à", "&agrave;");
/* 107 */     initialMap.put("á", "&aacute;");
/* 108 */     initialMap.put("â", "&acirc;");
/* 109 */     initialMap.put("ã", "&atilde;");
/* 110 */     initialMap.put("ä", "&auml;");
/* 111 */     initialMap.put("å", "&aring;");
/* 112 */     initialMap.put("æ", "&aelig;");
/* 113 */     initialMap.put("ç", "&ccedil;");
/* 114 */     initialMap.put("è", "&egrave;");
/* 115 */     initialMap.put("é", "&eacute;");
/* 116 */     initialMap.put("ê", "&ecirc;");
/* 117 */     initialMap.put("ë", "&euml;");
/* 118 */     initialMap.put("ì", "&igrave;");
/* 119 */     initialMap.put("í", "&iacute;");
/* 120 */     initialMap.put("î", "&icirc;");
/* 121 */     initialMap.put("ï", "&iuml;");
/* 122 */     initialMap.put("ð", "&eth;");
/* 123 */     initialMap.put("ñ", "&ntilde;");
/* 124 */     initialMap.put("ò", "&ograve;");
/* 125 */     initialMap.put("ó", "&oacute;");
/* 126 */     initialMap.put("ô", "&ocirc;");
/* 127 */     initialMap.put("õ", "&otilde;");
/* 128 */     initialMap.put("ö", "&ouml;");
/* 129 */     initialMap.put("÷", "&divide;");
/* 130 */     initialMap.put("ø", "&oslash;");
/* 131 */     initialMap.put("ù", "&ugrave;");
/* 132 */     initialMap.put("ú", "&uacute;");
/* 133 */     initialMap.put("û", "&ucirc;");
/* 134 */     initialMap.put("ü", "&uuml;");
/* 135 */     initialMap.put("ý", "&yacute;");
/* 136 */     initialMap.put("þ", "&thorn;");
/* 137 */     initialMap.put("ÿ", "&yuml;");
/* 138 */     ISO8859_1_ESCAPE = Collections.unmodifiableMap(initialMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   public static final Map<CharSequence, CharSequence> ISO8859_1_UNESCAPE = Collections.unmodifiableMap(invert(ISO8859_1_ESCAPE));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Map<CharSequence, CharSequence> HTML40_EXTENDED_ESCAPE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 159 */     initialMap = new HashMap<>();
/*     */     
/* 161 */     initialMap.put("ƒ", "&fnof;");
/*     */     
/* 163 */     initialMap.put("Α", "&Alpha;");
/* 164 */     initialMap.put("Β", "&Beta;");
/* 165 */     initialMap.put("Γ", "&Gamma;");
/* 166 */     initialMap.put("Δ", "&Delta;");
/* 167 */     initialMap.put("Ε", "&Epsilon;");
/* 168 */     initialMap.put("Ζ", "&Zeta;");
/* 169 */     initialMap.put("Η", "&Eta;");
/* 170 */     initialMap.put("Θ", "&Theta;");
/* 171 */     initialMap.put("Ι", "&Iota;");
/* 172 */     initialMap.put("Κ", "&Kappa;");
/* 173 */     initialMap.put("Λ", "&Lambda;");
/* 174 */     initialMap.put("Μ", "&Mu;");
/* 175 */     initialMap.put("Ν", "&Nu;");
/* 176 */     initialMap.put("Ξ", "&Xi;");
/* 177 */     initialMap.put("Ο", "&Omicron;");
/* 178 */     initialMap.put("Π", "&Pi;");
/* 179 */     initialMap.put("Ρ", "&Rho;");
/*     */     
/* 181 */     initialMap.put("Σ", "&Sigma;");
/* 182 */     initialMap.put("Τ", "&Tau;");
/* 183 */     initialMap.put("Υ", "&Upsilon;");
/* 184 */     initialMap.put("Φ", "&Phi;");
/* 185 */     initialMap.put("Χ", "&Chi;");
/* 186 */     initialMap.put("Ψ", "&Psi;");
/* 187 */     initialMap.put("Ω", "&Omega;");
/* 188 */     initialMap.put("α", "&alpha;");
/* 189 */     initialMap.put("β", "&beta;");
/* 190 */     initialMap.put("γ", "&gamma;");
/* 191 */     initialMap.put("δ", "&delta;");
/* 192 */     initialMap.put("ε", "&epsilon;");
/* 193 */     initialMap.put("ζ", "&zeta;");
/* 194 */     initialMap.put("η", "&eta;");
/* 195 */     initialMap.put("θ", "&theta;");
/* 196 */     initialMap.put("ι", "&iota;");
/* 197 */     initialMap.put("κ", "&kappa;");
/* 198 */     initialMap.put("λ", "&lambda;");
/* 199 */     initialMap.put("μ", "&mu;");
/* 200 */     initialMap.put("ν", "&nu;");
/* 201 */     initialMap.put("ξ", "&xi;");
/* 202 */     initialMap.put("ο", "&omicron;");
/* 203 */     initialMap.put("π", "&pi;");
/* 204 */     initialMap.put("ρ", "&rho;");
/* 205 */     initialMap.put("ς", "&sigmaf;");
/* 206 */     initialMap.put("σ", "&sigma;");
/* 207 */     initialMap.put("τ", "&tau;");
/* 208 */     initialMap.put("υ", "&upsilon;");
/* 209 */     initialMap.put("φ", "&phi;");
/* 210 */     initialMap.put("χ", "&chi;");
/* 211 */     initialMap.put("ψ", "&psi;");
/* 212 */     initialMap.put("ω", "&omega;");
/* 213 */     initialMap.put("ϑ", "&thetasym;");
/* 214 */     initialMap.put("ϒ", "&upsih;");
/* 215 */     initialMap.put("ϖ", "&piv;");
/*     */     
/* 217 */     initialMap.put("•", "&bull;");
/*     */     
/* 219 */     initialMap.put("…", "&hellip;");
/* 220 */     initialMap.put("′", "&prime;");
/* 221 */     initialMap.put("″", "&Prime;");
/* 222 */     initialMap.put("‾", "&oline;");
/* 223 */     initialMap.put("⁄", "&frasl;");
/*     */     
/* 225 */     initialMap.put("℘", "&weierp;");
/* 226 */     initialMap.put("ℑ", "&image;");
/* 227 */     initialMap.put("ℜ", "&real;");
/* 228 */     initialMap.put("™", "&trade;");
/* 229 */     initialMap.put("ℵ", "&alefsym;");
/*     */ 
/*     */ 
/*     */     
/* 233 */     initialMap.put("←", "&larr;");
/* 234 */     initialMap.put("↑", "&uarr;");
/* 235 */     initialMap.put("→", "&rarr;");
/* 236 */     initialMap.put("↓", "&darr;");
/* 237 */     initialMap.put("↔", "&harr;");
/* 238 */     initialMap.put("↵", "&crarr;");
/* 239 */     initialMap.put("⇐", "&lArr;");
/*     */ 
/*     */ 
/*     */     
/* 243 */     initialMap.put("⇑", "&uArr;");
/* 244 */     initialMap.put("⇒", "&rArr;");
/*     */ 
/*     */ 
/*     */     
/* 248 */     initialMap.put("⇓", "&dArr;");
/* 249 */     initialMap.put("⇔", "&hArr;");
/*     */     
/* 251 */     initialMap.put("∀", "&forall;");
/* 252 */     initialMap.put("∂", "&part;");
/* 253 */     initialMap.put("∃", "&exist;");
/* 254 */     initialMap.put("∅", "&empty;");
/* 255 */     initialMap.put("∇", "&nabla;");
/* 256 */     initialMap.put("∈", "&isin;");
/* 257 */     initialMap.put("∉", "&notin;");
/* 258 */     initialMap.put("∋", "&ni;");
/*     */     
/* 260 */     initialMap.put("∏", "&prod;");
/*     */ 
/*     */     
/* 263 */     initialMap.put("∑", "&sum;");
/*     */ 
/*     */     
/* 266 */     initialMap.put("−", "&minus;");
/* 267 */     initialMap.put("∗", "&lowast;");
/* 268 */     initialMap.put("√", "&radic;");
/* 269 */     initialMap.put("∝", "&prop;");
/* 270 */     initialMap.put("∞", "&infin;");
/* 271 */     initialMap.put("∠", "&ang;");
/* 272 */     initialMap.put("∧", "&and;");
/* 273 */     initialMap.put("∨", "&or;");
/* 274 */     initialMap.put("∩", "&cap;");
/* 275 */     initialMap.put("∪", "&cup;");
/* 276 */     initialMap.put("∫", "&int;");
/* 277 */     initialMap.put("∴", "&there4;");
/* 278 */     initialMap.put("∼", "&sim;");
/*     */ 
/*     */     
/* 281 */     initialMap.put("≅", "&cong;");
/* 282 */     initialMap.put("≈", "&asymp;");
/* 283 */     initialMap.put("≠", "&ne;");
/* 284 */     initialMap.put("≡", "&equiv;");
/* 285 */     initialMap.put("≤", "&le;");
/* 286 */     initialMap.put("≥", "&ge;");
/* 287 */     initialMap.put("⊂", "&sub;");
/* 288 */     initialMap.put("⊃", "&sup;");
/*     */ 
/*     */ 
/*     */     
/* 292 */     initialMap.put("⊄", "&nsub;");
/* 293 */     initialMap.put("⊆", "&sube;");
/* 294 */     initialMap.put("⊇", "&supe;");
/* 295 */     initialMap.put("⊕", "&oplus;");
/* 296 */     initialMap.put("⊗", "&otimes;");
/* 297 */     initialMap.put("⊥", "&perp;");
/* 298 */     initialMap.put("⋅", "&sdot;");
/*     */ 
/*     */     
/* 301 */     initialMap.put("⌈", "&lceil;");
/* 302 */     initialMap.put("⌉", "&rceil;");
/* 303 */     initialMap.put("⌊", "&lfloor;");
/* 304 */     initialMap.put("⌋", "&rfloor;");
/* 305 */     initialMap.put("〈", "&lang;");
/*     */ 
/*     */     
/* 308 */     initialMap.put("〉", "&rang;");
/*     */ 
/*     */ 
/*     */     
/* 312 */     initialMap.put("◊", "&loz;");
/*     */     
/* 314 */     initialMap.put("♠", "&spades;");
/*     */     
/* 316 */     initialMap.put("♣", "&clubs;");
/* 317 */     initialMap.put("♥", "&hearts;");
/* 318 */     initialMap.put("♦", "&diams;");
/*     */ 
/*     */     
/* 321 */     initialMap.put("Œ", "&OElig;");
/* 322 */     initialMap.put("œ", "&oelig;");
/*     */     
/* 324 */     initialMap.put("Š", "&Scaron;");
/* 325 */     initialMap.put("š", "&scaron;");
/* 326 */     initialMap.put("Ÿ", "&Yuml;");
/*     */     
/* 328 */     initialMap.put("ˆ", "&circ;");
/* 329 */     initialMap.put("˜", "&tilde;");
/*     */     
/* 331 */     initialMap.put(" ", "&ensp;");
/* 332 */     initialMap.put(" ", "&emsp;");
/* 333 */     initialMap.put(" ", "&thinsp;");
/* 334 */     initialMap.put("‌", "&zwnj;");
/* 335 */     initialMap.put("‍", "&zwj;");
/* 336 */     initialMap.put("‎", "&lrm;");
/* 337 */     initialMap.put("‏", "&rlm;");
/* 338 */     initialMap.put("–", "&ndash;");
/* 339 */     initialMap.put("—", "&mdash;");
/* 340 */     initialMap.put("‘", "&lsquo;");
/* 341 */     initialMap.put("’", "&rsquo;");
/* 342 */     initialMap.put("‚", "&sbquo;");
/* 343 */     initialMap.put("“", "&ldquo;");
/* 344 */     initialMap.put("”", "&rdquo;");
/* 345 */     initialMap.put("„", "&bdquo;");
/* 346 */     initialMap.put("†", "&dagger;");
/* 347 */     initialMap.put("‡", "&Dagger;");
/* 348 */     initialMap.put("‰", "&permil;");
/* 349 */     initialMap.put("‹", "&lsaquo;");
/*     */     
/* 351 */     initialMap.put("›", "&rsaquo;");
/*     */     
/* 353 */     initialMap.put("€", "&euro;");
/* 354 */     HTML40_EXTENDED_ESCAPE = Collections.unmodifiableMap(initialMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 363 */   public static final Map<CharSequence, CharSequence> HTML40_EXTENDED_UNESCAPE = Collections.unmodifiableMap(invert(HTML40_EXTENDED_ESCAPE));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Map<CharSequence, CharSequence> BASIC_ESCAPE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 375 */     initialMap = new HashMap<>();
/* 376 */     initialMap.put("\"", "&quot;");
/* 377 */     initialMap.put("&", "&amp;");
/* 378 */     initialMap.put("<", "&lt;");
/* 379 */     initialMap.put(">", "&gt;");
/* 380 */     BASIC_ESCAPE = Collections.unmodifiableMap(initialMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 389 */   public static final Map<CharSequence, CharSequence> BASIC_UNESCAPE = Collections.unmodifiableMap(invert(BASIC_ESCAPE));
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Map<CharSequence, CharSequence> APOS_ESCAPE;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 399 */     initialMap = new HashMap<>();
/* 400 */     initialMap.put("'", "&apos;");
/* 401 */     APOS_ESCAPE = Collections.unmodifiableMap(initialMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 410 */   public static final Map<CharSequence, CharSequence> APOS_UNESCAPE = Collections.unmodifiableMap(invert(APOS_ESCAPE));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Map<CharSequence, CharSequence> JAVA_CTRL_CHARS_ESCAPE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 422 */     initialMap = new HashMap<>();
/* 423 */     initialMap.put("\b", "\\b");
/* 424 */     initialMap.put("\n", "\\n");
/* 425 */     initialMap.put("\t", "\\t");
/* 426 */     initialMap.put("\f", "\\f");
/* 427 */     initialMap.put("\r", "\\r");
/* 428 */     JAVA_CTRL_CHARS_ESCAPE = Collections.unmodifiableMap(initialMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 437 */   public static final Map<CharSequence, CharSequence> JAVA_CTRL_CHARS_UNESCAPE = Collections.unmodifiableMap(invert(JAVA_CTRL_CHARS_ESCAPE));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<CharSequence, CharSequence> invert(Map<CharSequence, CharSequence> map) {
/* 447 */     return (Map<CharSequence, CharSequence>)map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\translate\EntityArrays.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */