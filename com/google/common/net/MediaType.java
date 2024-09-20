/*      */ package com.google.common.net;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.base.Ascii;
/*      */ import com.google.common.base.CharMatcher;
/*      */ import com.google.common.base.Charsets;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Joiner;
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Optional;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.collect.ImmutableListMultimap;
/*      */ import com.google.common.collect.ImmutableMultiset;
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.google.common.collect.ListMultimap;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Multimap;
/*      */ import com.google.common.collect.Multimaps;
/*      */ import com.google.common.collect.UnmodifiableIterator;
/*      */ import com.google.errorprone.annotations.Immutable;
/*      */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.Collection;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Immutable
/*      */ @Beta
/*      */ @GwtCompatible
/*      */ public final class MediaType
/*      */ {
/*      */   private static final String CHARSET_ATTRIBUTE = "charset";
/*   81 */   private static final ImmutableListMultimap<String, String> UTF_8_CONSTANT_PARAMETERS = ImmutableListMultimap.of("charset", Ascii.toLowerCase(Charsets.UTF_8.name()));
/*      */ 
/*      */ 
/*      */   
/*   85 */   private static final CharMatcher TOKEN_MATCHER = CharMatcher.ascii()
/*   86 */     .and(CharMatcher.javaIsoControl().negate())
/*   87 */     .and(CharMatcher.isNot(' '))
/*   88 */     .and(CharMatcher.noneOf("()<>@,;:\\\"/[]?="));
/*      */   
/*   90 */   private static final CharMatcher QUOTED_TEXT_MATCHER = CharMatcher.ascii().and(CharMatcher.noneOf("\"\\\r"));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   96 */   private static final CharMatcher LINEAR_WHITE_SPACE = CharMatcher.anyOf(" \t\r\n");
/*      */   
/*      */   private static final String APPLICATION_TYPE = "application";
/*      */   
/*      */   private static final String AUDIO_TYPE = "audio";
/*      */   
/*      */   private static final String IMAGE_TYPE = "image";
/*      */   
/*      */   private static final String TEXT_TYPE = "text";
/*      */   private static final String VIDEO_TYPE = "video";
/*      */   private static final String FONT_TYPE = "font";
/*      */   private static final String WILDCARD = "*";
/*  108 */   private static final Map<MediaType, MediaType> KNOWN_TYPES = Maps.newHashMap();
/*      */ 
/*      */   
/*      */   private static MediaType createConstant(String type, String subtype) {
/*  112 */     MediaType mediaType = addKnownType(new MediaType(type, subtype, ImmutableListMultimap.of()));
/*  113 */     mediaType.parsedCharset = Optional.absent();
/*  114 */     return mediaType;
/*      */   }
/*      */   
/*      */   private static MediaType createConstantUtf8(String type, String subtype) {
/*  118 */     MediaType mediaType = addKnownType(new MediaType(type, subtype, UTF_8_CONSTANT_PARAMETERS));
/*  119 */     mediaType.parsedCharset = Optional.of(Charsets.UTF_8);
/*  120 */     return mediaType;
/*      */   }
/*      */   
/*      */   private static MediaType addKnownType(MediaType mediaType) {
/*  124 */     KNOWN_TYPES.put(mediaType, mediaType);
/*  125 */     return mediaType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  138 */   public static final MediaType ANY_TYPE = createConstant("*", "*");
/*  139 */   public static final MediaType ANY_TEXT_TYPE = createConstant("text", "*");
/*  140 */   public static final MediaType ANY_IMAGE_TYPE = createConstant("image", "*");
/*  141 */   public static final MediaType ANY_AUDIO_TYPE = createConstant("audio", "*");
/*  142 */   public static final MediaType ANY_VIDEO_TYPE = createConstant("video", "*");
/*  143 */   public static final MediaType ANY_APPLICATION_TYPE = createConstant("application", "*");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  150 */   public static final MediaType ANY_FONT_TYPE = createConstant("font", "*");
/*      */ 
/*      */ 
/*      */   
/*  154 */   public static final MediaType CACHE_MANIFEST_UTF_8 = createConstantUtf8("text", "cache-manifest");
/*  155 */   public static final MediaType CSS_UTF_8 = createConstantUtf8("text", "css");
/*  156 */   public static final MediaType CSV_UTF_8 = createConstantUtf8("text", "csv");
/*  157 */   public static final MediaType HTML_UTF_8 = createConstantUtf8("text", "html");
/*  158 */   public static final MediaType I_CALENDAR_UTF_8 = createConstantUtf8("text", "calendar");
/*  159 */   public static final MediaType PLAIN_TEXT_UTF_8 = createConstantUtf8("text", "plain");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  166 */   public static final MediaType TEXT_JAVASCRIPT_UTF_8 = createConstantUtf8("text", "javascript");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  173 */   public static final MediaType TSV_UTF_8 = createConstantUtf8("text", "tab-separated-values");
/*      */   
/*  175 */   public static final MediaType VCARD_UTF_8 = createConstantUtf8("text", "vcard");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  183 */   public static final MediaType WML_UTF_8 = createConstantUtf8("text", "vnd.wap.wml");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  190 */   public static final MediaType XML_UTF_8 = createConstantUtf8("text", "xml");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  198 */   public static final MediaType VTT_UTF_8 = createConstantUtf8("text", "vtt");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  207 */   public static final MediaType BMP = createConstant("image", "bmp");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  218 */   public static final MediaType CRW = createConstant("image", "x-canon-crw");
/*      */   
/*  220 */   public static final MediaType GIF = createConstant("image", "gif");
/*  221 */   public static final MediaType ICO = createConstant("image", "vnd.microsoft.icon");
/*  222 */   public static final MediaType JPEG = createConstant("image", "jpeg");
/*  223 */   public static final MediaType PNG = createConstant("image", "png");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  242 */   public static final MediaType PSD = createConstant("image", "vnd.adobe.photoshop");
/*      */   
/*  244 */   public static final MediaType SVG_UTF_8 = createConstantUtf8("image", "svg+xml");
/*  245 */   public static final MediaType TIFF = createConstant("image", "tiff");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  252 */   public static final MediaType WEBP = createConstant("image", "webp");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  259 */   public static final MediaType HEIF = createConstant("image", "heif");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  266 */   public static final MediaType JP2K = createConstant("image", "jp2");
/*      */ 
/*      */   
/*  269 */   public static final MediaType MP4_AUDIO = createConstant("audio", "mp4");
/*  270 */   public static final MediaType MPEG_AUDIO = createConstant("audio", "mpeg");
/*  271 */   public static final MediaType OGG_AUDIO = createConstant("audio", "ogg");
/*  272 */   public static final MediaType WEBM_AUDIO = createConstant("audio", "webm");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  279 */   public static final MediaType L16_AUDIO = createConstant("audio", "l16");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  286 */   public static final MediaType L24_AUDIO = createConstant("audio", "l24");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  294 */   public static final MediaType BASIC_AUDIO = createConstant("audio", "basic");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  302 */   public static final MediaType AAC_AUDIO = createConstant("audio", "aac");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  309 */   public static final MediaType VORBIS_AUDIO = createConstant("audio", "vorbis");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  318 */   public static final MediaType WMA_AUDIO = createConstant("audio", "x-ms-wma");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  327 */   public static final MediaType WAX_AUDIO = createConstant("audio", "x-ms-wax");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  335 */   public static final MediaType VND_REAL_AUDIO = createConstant("audio", "vnd.rn-realaudio");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  342 */   public static final MediaType VND_WAVE_AUDIO = createConstant("audio", "vnd.wave");
/*      */ 
/*      */   
/*  345 */   public static final MediaType MP4_VIDEO = createConstant("video", "mp4");
/*  346 */   public static final MediaType MPEG_VIDEO = createConstant("video", "mpeg");
/*  347 */   public static final MediaType OGG_VIDEO = createConstant("video", "ogg");
/*  348 */   public static final MediaType QUICKTIME = createConstant("video", "quicktime");
/*  349 */   public static final MediaType WEBM_VIDEO = createConstant("video", "webm");
/*  350 */   public static final MediaType WMV = createConstant("video", "x-ms-wmv");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  359 */   public static final MediaType FLV_VIDEO = createConstant("video", "x-flv");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  368 */   public static final MediaType THREE_GPP_VIDEO = createConstant("video", "3gpp");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  377 */   public static final MediaType THREE_GPP2_VIDEO = createConstant("video", "3gpp2");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  387 */   public static final MediaType APPLICATION_XML_UTF_8 = createConstantUtf8("application", "xml");
/*      */   
/*  389 */   public static final MediaType ATOM_UTF_8 = createConstantUtf8("application", "atom+xml");
/*  390 */   public static final MediaType BZIP2 = createConstant("application", "x-bzip2");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  398 */   public static final MediaType DART_UTF_8 = createConstantUtf8("application", "dart");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  406 */   public static final MediaType APPLE_PASSBOOK = createConstant("application", "vnd.apple.pkpass");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  415 */   public static final MediaType EOT = createConstant("application", "vnd.ms-fontobject");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  426 */   public static final MediaType EPUB = createConstant("application", "epub+zip");
/*      */ 
/*      */   
/*  429 */   public static final MediaType FORM_DATA = createConstant("application", "x-www-form-urlencoded");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  438 */   public static final MediaType KEY_ARCHIVE = createConstant("application", "pkcs12");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  450 */   public static final MediaType APPLICATION_BINARY = createConstant("application", "binary");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  458 */   public static final MediaType GEO_JSON = createConstant("application", "geo+json");
/*      */   
/*  460 */   public static final MediaType GZIP = createConstant("application", "x-gzip");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  468 */   public static final MediaType HAL_JSON = createConstant("application", "hal+json");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  476 */   public static final MediaType JAVASCRIPT_UTF_8 = createConstantUtf8("application", "javascript");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  484 */   public static final MediaType JOSE = createConstant("application", "jose");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  492 */   public static final MediaType JOSE_JSON = createConstant("application", "jose+json");
/*      */   
/*  494 */   public static final MediaType JSON_UTF_8 = createConstantUtf8("application", "json");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  502 */   public static final MediaType MANIFEST_JSON_UTF_8 = createConstantUtf8("application", "manifest+json");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  507 */   public static final MediaType KML = createConstant("application", "vnd.google-earth.kml+xml");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  513 */   public static final MediaType KMZ = createConstant("application", "vnd.google-earth.kmz");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  520 */   public static final MediaType MBOX = createConstant("application", "mbox");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  528 */   public static final MediaType APPLE_MOBILE_CONFIG = createConstant("application", "x-apple-aspen-config");
/*      */ 
/*      */   
/*  531 */   public static final MediaType MICROSOFT_EXCEL = createConstant("application", "vnd.ms-excel");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  539 */   public static final MediaType MICROSOFT_OUTLOOK = createConstant("application", "vnd.ms-outlook");
/*      */ 
/*      */ 
/*      */   
/*  543 */   public static final MediaType MICROSOFT_POWERPOINT = createConstant("application", "vnd.ms-powerpoint");
/*      */ 
/*      */   
/*  546 */   public static final MediaType MICROSOFT_WORD = createConstant("application", "msword");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  558 */   public static final MediaType MEDIA_PRESENTATION_DESCRIPTION = createConstant("application", "dash+xml");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  566 */   public static final MediaType WASM_APPLICATION = createConstant("application", "wasm");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  575 */   public static final MediaType NACL_APPLICATION = createConstant("application", "x-nacl");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  585 */   public static final MediaType NACL_PORTABLE_APPLICATION = createConstant("application", "x-pnacl");
/*      */   
/*  587 */   public static final MediaType OCTET_STREAM = createConstant("application", "octet-stream");
/*      */   
/*  589 */   public static final MediaType OGG_CONTAINER = createConstant("application", "ogg");
/*      */   
/*  591 */   public static final MediaType OOXML_DOCUMENT = createConstant("application", "vnd.openxmlformats-officedocument.wordprocessingml.document");
/*      */ 
/*      */   
/*  594 */   public static final MediaType OOXML_PRESENTATION = createConstant("application", "vnd.openxmlformats-officedocument.presentationml.presentation");
/*      */ 
/*      */   
/*  597 */   public static final MediaType OOXML_SHEET = createConstant("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet");
/*      */   
/*  599 */   public static final MediaType OPENDOCUMENT_GRAPHICS = createConstant("application", "vnd.oasis.opendocument.graphics");
/*      */   
/*  601 */   public static final MediaType OPENDOCUMENT_PRESENTATION = createConstant("application", "vnd.oasis.opendocument.presentation");
/*      */   
/*  603 */   public static final MediaType OPENDOCUMENT_SPREADSHEET = createConstant("application", "vnd.oasis.opendocument.spreadsheet");
/*      */   
/*  605 */   public static final MediaType OPENDOCUMENT_TEXT = createConstant("application", "vnd.oasis.opendocument.text");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  615 */   public static final MediaType OPENSEARCH_DESCRIPTION_UTF_8 = createConstantUtf8("application", "opensearchdescription+xml");
/*      */   
/*  617 */   public static final MediaType PDF = createConstant("application", "pdf");
/*  618 */   public static final MediaType POSTSCRIPT = createConstant("application", "postscript");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  625 */   public static final MediaType PROTOBUF = createConstant("application", "protobuf");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  635 */   public static final MediaType RDF_XML_UTF_8 = createConstantUtf8("application", "rdf+xml");
/*      */   
/*  637 */   public static final MediaType RTF_UTF_8 = createConstantUtf8("application", "rtf");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  646 */   public static final MediaType SFNT = createConstant("application", "font-sfnt");
/*      */ 
/*      */   
/*  649 */   public static final MediaType SHOCKWAVE_FLASH = createConstant("application", "x-shockwave-flash");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  657 */   public static final MediaType SKETCHUP = createConstant("application", "vnd.sketchup.skp");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  670 */   public static final MediaType SOAP_XML_UTF_8 = createConstantUtf8("application", "soap+xml");
/*      */   
/*  672 */   public static final MediaType TAR = createConstant("application", "x-tar");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  681 */   public static final MediaType WOFF = createConstant("application", "font-woff");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  690 */   public static final MediaType WOFF2 = createConstant("application", "font-woff2");
/*      */   
/*  692 */   public static final MediaType XHTML_UTF_8 = createConstantUtf8("application", "xhtml+xml");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  702 */   public static final MediaType XRD_UTF_8 = createConstantUtf8("application", "xrd+xml");
/*      */   
/*  704 */   public static final MediaType ZIP = createConstant("application", "zip");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  714 */   public static final MediaType FONT_COLLECTION = createConstant("font", "collection");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  722 */   public static final MediaType FONT_OTF = createConstant("font", "otf");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  732 */   public static final MediaType FONT_SFNT = createConstant("font", "sfnt");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  740 */   public static final MediaType FONT_TTF = createConstant("font", "ttf");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  750 */   public static final MediaType FONT_WOFF = createConstant("font", "woff");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  760 */   public static final MediaType FONT_WOFF2 = createConstant("font", "woff2");
/*      */   
/*      */   private final String type;
/*      */   private final String subtype;
/*      */   private final ImmutableListMultimap<String, String> parameters;
/*      */   @LazyInit
/*      */   private String toString;
/*      */   @LazyInit
/*      */   private int hashCode;
/*      */   @LazyInit
/*      */   private Optional<Charset> parsedCharset;
/*      */   
/*      */   private MediaType(String type, String subtype, ImmutableListMultimap<String, String> parameters) {
/*  773 */     this.type = type;
/*  774 */     this.subtype = subtype;
/*  775 */     this.parameters = parameters;
/*      */   }
/*      */ 
/*      */   
/*      */   public String type() {
/*  780 */     return this.type;
/*      */   }
/*      */ 
/*      */   
/*      */   public String subtype() {
/*  785 */     return this.subtype;
/*      */   }
/*      */ 
/*      */   
/*      */   public ImmutableListMultimap<String, String> parameters() {
/*  790 */     return this.parameters;
/*      */   }
/*      */   
/*      */   private Map<String, ImmutableMultiset<String>> parametersAsMap() {
/*  794 */     return Maps.transformValues((Map)this.parameters
/*  795 */         .asMap(), new Function<Collection<String>, ImmutableMultiset<String>>(this)
/*      */         {
/*      */           public ImmutableMultiset<String> apply(Collection<String> input)
/*      */           {
/*  799 */             return ImmutableMultiset.copyOf(input);
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Optional<Charset> charset() {
/*  814 */     Optional<Charset> local = this.parsedCharset;
/*  815 */     if (local == null) {
/*  816 */       String value = null;
/*  817 */       local = Optional.absent();
/*  818 */       for (UnmodifiableIterator<String> unmodifiableIterator = this.parameters.get("charset").iterator(); unmodifiableIterator.hasNext(); ) { String currentValue = unmodifiableIterator.next();
/*  819 */         if (value == null) {
/*  820 */           value = currentValue;
/*  821 */           local = Optional.of(Charset.forName(value)); continue;
/*  822 */         }  if (!value.equals(currentValue)) {
/*  823 */           String str = value; throw new IllegalStateException((new StringBuilder(35 + String.valueOf(str).length() + String.valueOf(currentValue).length())).append("Multiple charset values defined: ").append(str).append(", ").append(currentValue).toString());
/*      */         }  }
/*      */ 
/*      */       
/*  827 */       this.parsedCharset = local;
/*      */     } 
/*  829 */     return local;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MediaType withoutParameters() {
/*  837 */     return this.parameters.isEmpty() ? this : create(this.type, this.subtype);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MediaType withParameters(Multimap<String, String> parameters) {
/*  846 */     return create(this.type, this.subtype, parameters);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MediaType withParameters(String attribute, Iterable<String> values) {
/*  857 */     Preconditions.checkNotNull(attribute);
/*  858 */     Preconditions.checkNotNull(values);
/*  859 */     String normalizedAttribute = normalizeToken(attribute);
/*  860 */     ImmutableListMultimap.Builder<String, String> builder = ImmutableListMultimap.builder();
/*  861 */     for (UnmodifiableIterator<Map.Entry<String, String>> unmodifiableIterator = this.parameters.entries().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<String, String> entry = unmodifiableIterator.next();
/*  862 */       String key = entry.getKey();
/*  863 */       if (!normalizedAttribute.equals(key)) {
/*  864 */         builder.put(key, entry.getValue());
/*      */       } }
/*      */     
/*  867 */     for (String value : values) {
/*  868 */       builder.put(normalizedAttribute, normalizeParameterValue(normalizedAttribute, value));
/*      */     }
/*  870 */     MediaType mediaType = new MediaType(this.type, this.subtype, builder.build());
/*      */     
/*  872 */     if (!normalizedAttribute.equals("charset")) {
/*  873 */       mediaType.parsedCharset = this.parsedCharset;
/*      */     }
/*      */     
/*  876 */     return (MediaType)MoreObjects.firstNonNull(KNOWN_TYPES.get(mediaType), mediaType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MediaType withParameter(String attribute, String value) {
/*  888 */     return withParameters(attribute, (Iterable<String>)ImmutableSet.of(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MediaType withCharset(Charset charset) {
/*  901 */     Preconditions.checkNotNull(charset);
/*  902 */     MediaType withCharset = withParameter("charset", charset.name());
/*      */     
/*  904 */     withCharset.parsedCharset = Optional.of(charset);
/*  905 */     return withCharset;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasWildcard() {
/*  910 */     return ("*".equals(this.type) || "*".equals(this.subtype));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean is(MediaType mediaTypeRange) {
/*  943 */     return ((mediaTypeRange.type.equals("*") || mediaTypeRange.type.equals(this.type)) && (mediaTypeRange.subtype
/*  944 */       .equals("*") || mediaTypeRange.subtype.equals(this.subtype)) && this.parameters
/*  945 */       .entries().containsAll((Collection)mediaTypeRange.parameters.entries()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static MediaType create(String type, String subtype) {
/*  955 */     MediaType mediaType = create(type, subtype, (Multimap<String, String>)ImmutableListMultimap.of());
/*  956 */     mediaType.parsedCharset = Optional.absent();
/*  957 */     return mediaType;
/*      */   }
/*      */ 
/*      */   
/*      */   private static MediaType create(String type, String subtype, Multimap<String, String> parameters) {
/*  962 */     Preconditions.checkNotNull(type);
/*  963 */     Preconditions.checkNotNull(subtype);
/*  964 */     Preconditions.checkNotNull(parameters);
/*  965 */     String normalizedType = normalizeToken(type);
/*  966 */     String normalizedSubtype = normalizeToken(subtype);
/*  967 */     Preconditions.checkArgument((
/*  968 */         !"*".equals(normalizedType) || "*".equals(normalizedSubtype)), "A wildcard type cannot be used with a non-wildcard subtype");
/*      */     
/*  970 */     ImmutableListMultimap.Builder<String, String> builder = ImmutableListMultimap.builder();
/*  971 */     for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)parameters.entries()) {
/*  972 */       String attribute = normalizeToken(entry.getKey());
/*  973 */       builder.put(attribute, normalizeParameterValue(attribute, entry.getValue()));
/*      */     } 
/*  975 */     MediaType mediaType = new MediaType(normalizedType, normalizedSubtype, builder.build());
/*      */     
/*  977 */     return (MediaType)MoreObjects.firstNonNull(KNOWN_TYPES.get(mediaType), mediaType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static MediaType createApplicationType(String subtype) {
/*  986 */     return create("application", subtype);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static MediaType createAudioType(String subtype) {
/*  995 */     return create("audio", subtype);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static MediaType createFontType(String subtype) {
/* 1004 */     return create("font", subtype);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static MediaType createImageType(String subtype) {
/* 1013 */     return create("image", subtype);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static MediaType createTextType(String subtype) {
/* 1022 */     return create("text", subtype);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static MediaType createVideoType(String subtype) {
/* 1031 */     return create("video", subtype);
/*      */   }
/*      */   
/*      */   private static String normalizeToken(String token) {
/* 1035 */     Preconditions.checkArgument(TOKEN_MATCHER.matchesAllOf(token));
/* 1036 */     Preconditions.checkArgument(!token.isEmpty());
/* 1037 */     return Ascii.toLowerCase(token);
/*      */   }
/*      */   
/*      */   private static String normalizeParameterValue(String attribute, String value) {
/* 1041 */     Preconditions.checkNotNull(value);
/* 1042 */     Preconditions.checkArgument(CharMatcher.ascii().matchesAllOf(value), "parameter values must be ASCII: %s", value);
/* 1043 */     return "charset".equals(attribute) ? Ascii.toLowerCase(value) : value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static MediaType parse(String input) {
/* 1052 */     Preconditions.checkNotNull(input);
/* 1053 */     Tokenizer tokenizer = new Tokenizer(input);
/*      */     try {
/* 1055 */       String type = tokenizer.consumeToken(TOKEN_MATCHER);
/* 1056 */       tokenizer.consumeCharacter('/');
/* 1057 */       String subtype = tokenizer.consumeToken(TOKEN_MATCHER);
/* 1058 */       ImmutableListMultimap.Builder<String, String> parameters = ImmutableListMultimap.builder();
/* 1059 */       while (tokenizer.hasMore()) {
/* 1060 */         String value; tokenizer.consumeTokenIfPresent(LINEAR_WHITE_SPACE);
/* 1061 */         tokenizer.consumeCharacter(';');
/* 1062 */         tokenizer.consumeTokenIfPresent(LINEAR_WHITE_SPACE);
/* 1063 */         String attribute = tokenizer.consumeToken(TOKEN_MATCHER);
/* 1064 */         tokenizer.consumeCharacter('=');
/*      */         
/* 1066 */         if ('"' == tokenizer.previewChar()) {
/* 1067 */           tokenizer.consumeCharacter('"');
/* 1068 */           StringBuilder valueBuilder = new StringBuilder();
/* 1069 */           while ('"' != tokenizer.previewChar()) {
/* 1070 */             if ('\\' == tokenizer.previewChar()) {
/* 1071 */               tokenizer.consumeCharacter('\\');
/* 1072 */               valueBuilder.append(tokenizer.consumeCharacter(CharMatcher.ascii())); continue;
/*      */             } 
/* 1074 */             valueBuilder.append(tokenizer.consumeToken(QUOTED_TEXT_MATCHER));
/*      */           } 
/*      */           
/* 1077 */           value = valueBuilder.toString();
/* 1078 */           tokenizer.consumeCharacter('"');
/*      */         } else {
/* 1080 */           value = tokenizer.consumeToken(TOKEN_MATCHER);
/*      */         } 
/* 1082 */         parameters.put(attribute, value);
/*      */       } 
/* 1084 */       return create(type, subtype, (Multimap<String, String>)parameters.build());
/* 1085 */     } catch (IllegalStateException e) {
/* 1086 */       throw new IllegalArgumentException((new StringBuilder(18 + String.valueOf(input).length())).append("Could not parse '").append(input).append("'").toString(), e);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static final class Tokenizer {
/*      */     final String input;
/* 1092 */     int position = 0;
/*      */     
/*      */     Tokenizer(String input) {
/* 1095 */       this.input = input;
/*      */     }
/*      */     
/*      */     String consumeTokenIfPresent(CharMatcher matcher) {
/* 1099 */       Preconditions.checkState(hasMore());
/* 1100 */       int startPosition = this.position;
/* 1101 */       this.position = matcher.negate().indexIn(this.input, startPosition);
/* 1102 */       return hasMore() ? this.input.substring(startPosition, this.position) : this.input.substring(startPosition);
/*      */     }
/*      */     
/*      */     String consumeToken(CharMatcher matcher) {
/* 1106 */       int startPosition = this.position;
/* 1107 */       String token = consumeTokenIfPresent(matcher);
/* 1108 */       Preconditions.checkState((this.position != startPosition));
/* 1109 */       return token;
/*      */     }
/*      */     
/*      */     char consumeCharacter(CharMatcher matcher) {
/* 1113 */       Preconditions.checkState(hasMore());
/* 1114 */       char c = previewChar();
/* 1115 */       Preconditions.checkState(matcher.matches(c));
/* 1116 */       this.position++;
/* 1117 */       return c;
/*      */     }
/*      */     
/*      */     char consumeCharacter(char c) {
/* 1121 */       Preconditions.checkState(hasMore());
/* 1122 */       Preconditions.checkState((previewChar() == c));
/* 1123 */       this.position++;
/* 1124 */       return c;
/*      */     }
/*      */     
/*      */     char previewChar() {
/* 1128 */       Preconditions.checkState(hasMore());
/* 1129 */       return this.input.charAt(this.position);
/*      */     }
/*      */     
/*      */     boolean hasMore() {
/* 1133 */       return (this.position >= 0 && this.position < this.input.length());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean equals(Object obj) {
/* 1139 */     if (obj == this)
/* 1140 */       return true; 
/* 1141 */     if (obj instanceof MediaType) {
/* 1142 */       MediaType that = (MediaType)obj;
/* 1143 */       return (this.type.equals(that.type) && this.subtype
/* 1144 */         .equals(that.subtype) && 
/*      */         
/* 1146 */         parametersAsMap().equals(that.parametersAsMap()));
/*      */     } 
/* 1148 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 1155 */     int h = this.hashCode;
/* 1156 */     if (h == 0) {
/* 1157 */       h = Objects.hashCode(new Object[] { this.type, this.subtype, parametersAsMap() });
/* 1158 */       this.hashCode = h;
/*      */     } 
/* 1160 */     return h;
/*      */   }
/*      */   
/* 1163 */   private static final Joiner.MapJoiner PARAMETER_JOINER = Joiner.on("; ").withKeyValueSeparator("=");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1172 */     String result = this.toString;
/* 1173 */     if (result == null) {
/* 1174 */       result = computeToString();
/* 1175 */       this.toString = result;
/*      */     } 
/* 1177 */     return result;
/*      */   }
/*      */   
/*      */   private String computeToString() {
/* 1181 */     StringBuilder builder = (new StringBuilder()).append(this.type).append('/').append(this.subtype);
/* 1182 */     if (!this.parameters.isEmpty()) {
/* 1183 */       builder.append("; ");
/*      */       
/* 1185 */       ListMultimap listMultimap = Multimaps.transformValues((ListMultimap)this.parameters, new Function<String, String>(this)
/*      */           {
/*      */             
/*      */             public String apply(String value)
/*      */             {
/* 1190 */               return (MediaType.TOKEN_MATCHER.matchesAllOf(value) && !value.isEmpty()) ? 
/* 1191 */                 value : MediaType
/* 1192 */                 .escapeAndQuote(value);
/*      */             }
/*      */           });
/* 1195 */       PARAMETER_JOINER.appendTo(builder, listMultimap.entries());
/*      */     } 
/* 1197 */     return builder.toString();
/*      */   }
/*      */   
/*      */   private static String escapeAndQuote(String value) {
/* 1201 */     StringBuilder escaped = (new StringBuilder(value.length() + 16)).append('"');
/* 1202 */     for (int i = 0; i < value.length(); i++) {
/* 1203 */       char ch = value.charAt(i);
/* 1204 */       if (ch == '\r' || ch == '\\' || ch == '"') {
/* 1205 */         escaped.append('\\');
/*      */       }
/* 1207 */       escaped.append(ch);
/*      */     } 
/* 1209 */     return escaped.append('"').toString();
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\net\MediaType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */