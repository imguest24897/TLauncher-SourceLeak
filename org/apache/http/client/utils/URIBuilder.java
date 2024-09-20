/*     */ package org.apache.http.client.utils;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.http.Consts;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.conn.util.InetAddressUtils;
/*     */ import org.apache.http.message.BasicNameValuePair;
/*     */ import org.apache.http.util.TextUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class URIBuilder
/*     */ {
/*     */   private String scheme;
/*     */   private String encodedSchemeSpecificPart;
/*     */   private String encodedAuthority;
/*     */   private String userInfo;
/*     */   private String encodedUserInfo;
/*     */   private String host;
/*     */   private int port;
/*     */   private String encodedPath;
/*     */   private List<String> pathSegments;
/*     */   private String encodedQuery;
/*     */   private List<NameValuePair> queryParams;
/*     */   private String query;
/*     */   private Charset charset;
/*     */   private String fragment;
/*     */   private String encodedFragment;
/*     */   
/*     */   public URIBuilder() {
/*  71 */     this.port = -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder(String string) throws URISyntaxException {
/*  82 */     digestURI(new URI(string));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder(URI uri) {
/*  91 */     digestURI(uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setCharset(Charset charset) {
/*  98 */     this.charset = charset;
/*  99 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Charset getCharset() {
/* 106 */     return this.charset;
/*     */   }
/*     */   
/*     */   private List<NameValuePair> parseQuery(String query, Charset charset) {
/* 110 */     if (query != null && !query.isEmpty()) {
/* 111 */       return URLEncodedUtils.parse(query, charset);
/*     */     }
/* 113 */     return null;
/*     */   }
/*     */   
/*     */   private List<String> parsePath(String path, Charset charset) {
/* 117 */     if (path != null && !path.isEmpty()) {
/* 118 */       return URLEncodedUtils.parsePathSegments(path, charset);
/*     */     }
/* 120 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI build() throws URISyntaxException {
/* 127 */     return new URI(buildString());
/*     */   }
/*     */   
/*     */   private String buildString() {
/* 131 */     StringBuilder sb = new StringBuilder();
/* 132 */     if (this.scheme != null) {
/* 133 */       sb.append(this.scheme).append(':');
/*     */     }
/* 135 */     if (this.encodedSchemeSpecificPart != null) {
/* 136 */       sb.append(this.encodedSchemeSpecificPart);
/*     */     } else {
/* 138 */       if (this.encodedAuthority != null) {
/* 139 */         sb.append("//").append(this.encodedAuthority);
/* 140 */       } else if (this.host != null) {
/* 141 */         sb.append("//");
/* 142 */         if (this.encodedUserInfo != null) {
/* 143 */           sb.append(this.encodedUserInfo).append("@");
/* 144 */         } else if (this.userInfo != null) {
/* 145 */           sb.append(encodeUserInfo(this.userInfo)).append("@");
/*     */         } 
/* 147 */         if (InetAddressUtils.isIPv6Address(this.host)) {
/* 148 */           sb.append("[").append(this.host).append("]");
/*     */         } else {
/* 150 */           sb.append(this.host);
/*     */         } 
/* 152 */         if (this.port >= 0) {
/* 153 */           sb.append(":").append(this.port);
/*     */         }
/*     */       } 
/* 156 */       if (this.encodedPath != null) {
/* 157 */         sb.append(normalizePath(this.encodedPath, (sb.length() == 0)));
/* 158 */       } else if (this.pathSegments != null) {
/* 159 */         sb.append(encodePath(this.pathSegments));
/*     */       } 
/* 161 */       if (this.encodedQuery != null) {
/* 162 */         sb.append("?").append(this.encodedQuery);
/* 163 */       } else if (this.queryParams != null && !this.queryParams.isEmpty()) {
/* 164 */         sb.append("?").append(encodeUrlForm(this.queryParams));
/* 165 */       } else if (this.query != null) {
/* 166 */         sb.append("?").append(encodeUric(this.query));
/*     */       } 
/*     */     } 
/* 169 */     if (this.encodedFragment != null) {
/* 170 */       sb.append("#").append(this.encodedFragment);
/* 171 */     } else if (this.fragment != null) {
/* 172 */       sb.append("#").append(encodeUric(this.fragment));
/*     */     } 
/* 174 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private static String normalizePath(String path, boolean relative) {
/* 178 */     String s = path;
/* 179 */     if (TextUtils.isBlank(s)) {
/* 180 */       return "";
/*     */     }
/* 182 */     if (!relative && !s.startsWith("/")) {
/* 183 */       s = "/" + s;
/*     */     }
/* 185 */     return s;
/*     */   }
/*     */   
/*     */   private void digestURI(URI uri) {
/* 189 */     this.scheme = uri.getScheme();
/* 190 */     this.encodedSchemeSpecificPart = uri.getRawSchemeSpecificPart();
/* 191 */     this.encodedAuthority = uri.getRawAuthority();
/* 192 */     this.host = uri.getHost();
/* 193 */     this.port = uri.getPort();
/* 194 */     this.encodedUserInfo = uri.getRawUserInfo();
/* 195 */     this.userInfo = uri.getUserInfo();
/* 196 */     this.encodedPath = uri.getRawPath();
/* 197 */     this.pathSegments = parsePath(uri.getRawPath(), (this.charset != null) ? this.charset : Consts.UTF_8);
/* 198 */     this.encodedQuery = uri.getRawQuery();
/* 199 */     this.queryParams = parseQuery(uri.getRawQuery(), (this.charset != null) ? this.charset : Consts.UTF_8);
/* 200 */     this.encodedFragment = uri.getRawFragment();
/* 201 */     this.fragment = uri.getFragment();
/*     */   }
/*     */   
/*     */   private String encodeUserInfo(String userInfo) {
/* 205 */     return URLEncodedUtils.encUserInfo(userInfo, (this.charset != null) ? this.charset : Consts.UTF_8);
/*     */   }
/*     */   
/*     */   private String encodePath(List<String> pathSegments) {
/* 209 */     return URLEncodedUtils.formatSegments(pathSegments, (this.charset != null) ? this.charset : Consts.UTF_8);
/*     */   }
/*     */   
/*     */   private String encodeUrlForm(List<NameValuePair> params) {
/* 213 */     return URLEncodedUtils.format(params, (this.charset != null) ? this.charset : Consts.UTF_8);
/*     */   }
/*     */   
/*     */   private String encodeUric(String fragment) {
/* 217 */     return URLEncodedUtils.encUric(fragment, (this.charset != null) ? this.charset : Consts.UTF_8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setScheme(String scheme) {
/* 224 */     this.scheme = scheme;
/* 225 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setUserInfo(String userInfo) {
/* 233 */     this.userInfo = userInfo;
/* 234 */     this.encodedSchemeSpecificPart = null;
/* 235 */     this.encodedAuthority = null;
/* 236 */     this.encodedUserInfo = null;
/* 237 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setUserInfo(String username, String password) {
/* 245 */     return setUserInfo(username + ':' + password);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setHost(String host) {
/* 252 */     this.host = host;
/* 253 */     this.encodedSchemeSpecificPart = null;
/* 254 */     this.encodedAuthority = null;
/* 255 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setPort(int port) {
/* 262 */     this.port = (port < 0) ? -1 : port;
/* 263 */     this.encodedSchemeSpecificPart = null;
/* 264 */     this.encodedAuthority = null;
/* 265 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setPath(String path) {
/* 274 */     return setPathSegments((path != null) ? URLEncodedUtils.splitPathSegments(path) : null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setPathSegments(String... pathSegments) {
/* 285 */     this.pathSegments = (pathSegments.length > 0) ? Arrays.<String>asList(pathSegments) : null;
/* 286 */     this.encodedSchemeSpecificPart = null;
/* 287 */     this.encodedPath = null;
/* 288 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setPathSegments(List<String> pathSegments) {
/* 299 */     this.pathSegments = (pathSegments != null && pathSegments.size() > 0) ? new ArrayList<String>(pathSegments) : null;
/* 300 */     this.encodedSchemeSpecificPart = null;
/* 301 */     this.encodedPath = null;
/* 302 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder removeQuery() {
/* 309 */     this.queryParams = null;
/* 310 */     this.query = null;
/* 311 */     this.encodedQuery = null;
/* 312 */     this.encodedSchemeSpecificPart = null;
/* 313 */     return this;
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
/*     */   @Deprecated
/*     */   public URIBuilder setQuery(String query) {
/* 327 */     this.queryParams = parseQuery(query, (this.charset != null) ? this.charset : Consts.UTF_8);
/* 328 */     this.query = null;
/* 329 */     this.encodedQuery = null;
/* 330 */     this.encodedSchemeSpecificPart = null;
/* 331 */     return this;
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
/*     */   public URIBuilder setParameters(List<NameValuePair> nvps) {
/* 345 */     if (this.queryParams == null) {
/* 346 */       this.queryParams = new ArrayList<NameValuePair>();
/*     */     } else {
/* 348 */       this.queryParams.clear();
/*     */     } 
/* 350 */     this.queryParams.addAll(nvps);
/* 351 */     this.encodedQuery = null;
/* 352 */     this.encodedSchemeSpecificPart = null;
/* 353 */     this.query = null;
/* 354 */     return this;
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
/*     */   public URIBuilder addParameters(List<NameValuePair> nvps) {
/* 368 */     if (this.queryParams == null) {
/* 369 */       this.queryParams = new ArrayList<NameValuePair>();
/*     */     }
/* 371 */     this.queryParams.addAll(nvps);
/* 372 */     this.encodedQuery = null;
/* 373 */     this.encodedSchemeSpecificPart = null;
/* 374 */     this.query = null;
/* 375 */     return this;
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
/*     */   public URIBuilder setParameters(NameValuePair... nvps) {
/* 389 */     if (this.queryParams == null) {
/* 390 */       this.queryParams = new ArrayList<NameValuePair>();
/*     */     } else {
/* 392 */       this.queryParams.clear();
/*     */     } 
/* 394 */     for (NameValuePair nvp : nvps) {
/* 395 */       this.queryParams.add(nvp);
/*     */     }
/* 397 */     this.encodedQuery = null;
/* 398 */     this.encodedSchemeSpecificPart = null;
/* 399 */     this.query = null;
/* 400 */     return this;
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
/*     */   public URIBuilder addParameter(String param, String value) {
/* 412 */     if (this.queryParams == null) {
/* 413 */       this.queryParams = new ArrayList<NameValuePair>();
/*     */     }
/* 415 */     this.queryParams.add(new BasicNameValuePair(param, value));
/* 416 */     this.encodedQuery = null;
/* 417 */     this.encodedSchemeSpecificPart = null;
/* 418 */     this.query = null;
/* 419 */     return this;
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
/*     */   public URIBuilder setParameter(String param, String value) {
/* 431 */     if (this.queryParams == null) {
/* 432 */       this.queryParams = new ArrayList<NameValuePair>();
/*     */     }
/* 434 */     if (!this.queryParams.isEmpty()) {
/* 435 */       for (Iterator<NameValuePair> it = this.queryParams.iterator(); it.hasNext(); ) {
/* 436 */         NameValuePair nvp = it.next();
/* 437 */         if (nvp.getName().equals(param)) {
/* 438 */           it.remove();
/*     */         }
/*     */       } 
/*     */     }
/* 442 */     this.queryParams.add(new BasicNameValuePair(param, value));
/* 443 */     this.encodedQuery = null;
/* 444 */     this.encodedSchemeSpecificPart = null;
/* 445 */     this.query = null;
/* 446 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder clearParameters() {
/* 455 */     this.queryParams = null;
/* 456 */     this.encodedQuery = null;
/* 457 */     this.encodedSchemeSpecificPart = null;
/* 458 */     return this;
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
/*     */   public URIBuilder setCustomQuery(String query) {
/* 472 */     this.query = query;
/* 473 */     this.encodedQuery = null;
/* 474 */     this.encodedSchemeSpecificPart = null;
/* 475 */     this.queryParams = null;
/* 476 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setFragment(String fragment) {
/* 484 */     this.fragment = fragment;
/* 485 */     this.encodedFragment = null;
/* 486 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAbsolute() {
/* 493 */     return (this.scheme != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpaque() {
/* 500 */     return isPathEmpty();
/*     */   }
/*     */   
/*     */   public String getScheme() {
/* 504 */     return this.scheme;
/*     */   }
/*     */   
/*     */   public String getUserInfo() {
/* 508 */     return this.userInfo;
/*     */   }
/*     */   
/*     */   public String getHost() {
/* 512 */     return this.host;
/*     */   }
/*     */   
/*     */   public int getPort() {
/* 516 */     return this.port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPathEmpty() {
/* 523 */     return ((this.pathSegments == null || this.pathSegments.isEmpty()) && (this.encodedPath == null || this.encodedPath.isEmpty()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getPathSegments() {
/* 531 */     return (this.pathSegments != null) ? new ArrayList<String>(this.pathSegments) : Collections.<String>emptyList();
/*     */   }
/*     */   
/*     */   public String getPath() {
/* 535 */     if (this.pathSegments == null) {
/* 536 */       return null;
/*     */     }
/* 538 */     StringBuilder result = new StringBuilder();
/* 539 */     for (String segment : this.pathSegments) {
/* 540 */       result.append('/').append(segment);
/*     */     }
/* 542 */     return result.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isQueryEmpty() {
/* 549 */     return ((this.queryParams == null || this.queryParams.isEmpty()) && this.encodedQuery == null);
/*     */   }
/*     */   
/*     */   public List<NameValuePair> getQueryParams() {
/* 553 */     return (this.queryParams != null) ? new ArrayList<NameValuePair>(this.queryParams) : Collections.<NameValuePair>emptyList();
/*     */   }
/*     */   
/*     */   public String getFragment() {
/* 557 */     return this.fragment;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 562 */     return buildString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\http\clien\\utils\URIBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */