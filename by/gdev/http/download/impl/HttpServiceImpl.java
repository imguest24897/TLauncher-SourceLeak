/*     */ package by.gdev.http.download.impl;
/*     */ 
/*     */ import by.gdev.http.download.model.Headers;
/*     */ import by.gdev.http.download.model.RequestMetadata;
/*     */ import by.gdev.http.download.service.HttpService;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.methods.CloseableHttpResponse;
/*     */ import org.apache.http.client.methods.HttpGet;
/*     */ import org.apache.http.client.methods.HttpHead;
/*     */ import org.apache.http.client.methods.HttpRequestBase;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class HttpServiceImpl
/*     */   implements HttpService
/*     */ {
/*     */   public HttpServiceImpl(String proxy, CloseableHttpClient httpclient, RequestConfig requestConfig, int maxAttepmts) {
/*  37 */     this.proxy = proxy; this.httpclient = httpclient; this.requestConfig = requestConfig; this.maxAttepmts = maxAttepmts;
/*  38 */   } private static final Logger log = LoggerFactory.getLogger(HttpServiceImpl.class);
/*     */ 
/*     */   
/*     */   private String proxy;
/*     */ 
/*     */   
/*     */   private CloseableHttpClient httpclient;
/*     */ 
/*     */   
/*     */   private RequestConfig requestConfig;
/*     */   
/*     */   private int maxAttepmts;
/*     */ 
/*     */   
/*     */   public RequestMetadata getRequestByUrlAndSave(String url, Path path) throws IOException {
/*  53 */     log.debug("do request {}, saved to ", url, path.toAbsolutePath().toString());
/*  54 */     RequestMetadata request = null;
/*  55 */     for (int attepmts = 0; attepmts < this.maxAttepmts; attepmts++) {
/*     */       try {
/*  57 */         request = getResourseByUrl(url, path);
/*     */         break;
/*  59 */       } catch (SocketTimeoutException e1) {
/*  60 */         attepmts++;
/*  61 */         if (attepmts == this.maxAttepmts)
/*  62 */           throw new SocketTimeoutException(); 
/*  63 */       } catch (IOException e) {
/*  64 */         if (Objects.nonNull(this.proxy)) {
/*  65 */           request = getResourseByUrl(this.proxy + url, path);
/*     */         } else {
/*  67 */           throw e;
/*     */         } 
/*     */       } 
/*  70 */     }  return request;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestMetadata getMetaByUrl(String url) throws IOException {
/*  80 */     RequestMetadata request = null;
/*  81 */     for (int attepmts = 0; attepmts < this.maxAttepmts; attepmts++) {
/*     */       try {
/*  83 */         request = getMetadata(url);
/*     */         break;
/*  85 */       } catch (SocketTimeoutException e) {
/*  86 */         attepmts++;
/*  87 */         if (attepmts == this.maxAttepmts)
/*  88 */           throw new SocketTimeoutException(); 
/*     */       } 
/*     */     } 
/*  91 */     return request;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRequestByUrl(String url) throws IOException {
/*  96 */     return getRequestByUrl(url, null);
/*     */   }
/*     */   
/*     */   private String getStringByUrl(String url, Map<String, String> headers) throws IOException {
/* 100 */     InputStream in = null;
/* 101 */     HttpGet httpGet = null;
/*     */     try {
/* 103 */       httpGet = new HttpGet(url);
/* 104 */       if (Objects.nonNull(headers))
/* 105 */         for (Map.Entry<String, String> e : headers.entrySet()) {
/* 106 */           httpGet.addHeader(e.getKey(), e.getValue());
/*     */         } 
/* 108 */       CloseableHttpResponse response = getResponse((HttpRequestBase)httpGet);
/* 109 */       StatusLine st = response.getStatusLine();
/* 110 */       if (200 == response.getStatusLine().getStatusCode()) {
/* 111 */         in = response.getEntity().getContent();
/* 112 */         return IOUtils.toString(in, StandardCharsets.UTF_8);
/*     */       } 
/* 114 */       throw new IOException(
/* 115 */           String.format("code %s phrase %s %s", new Object[] { Integer.valueOf(st.getStatusCode()), st.getReasonPhrase(), url }));
/*     */     } finally {
/*     */       
/* 118 */       if (Objects.nonNull(httpGet))
/* 119 */         httpGet.abort(); 
/* 120 */       IOUtils.closeQuietly(in);
/*     */     } 
/*     */   }
/*     */   
/*     */   private RequestMetadata getMetadata(String url) throws IOException {
/* 125 */     HttpHead httpUrl = new HttpHead(url);
/* 126 */     CloseableHttpResponse response = getResponse((HttpRequestBase)httpUrl);
/* 127 */     RequestMetadata request = generateRequestMetadata(response);
/* 128 */     return request;
/*     */   }
/*     */   private RequestMetadata getResourseByUrl(String url, Path path) throws IOException, SocketTimeoutException {
/*     */     CloseableHttpResponse response;
/* 132 */     HttpGet httpGet = new HttpGet(url);
/* 133 */     BufferedInputStream in = null;
/* 134 */     BufferedOutputStream out = null;
/* 135 */     if (!path.toFile().getParentFile().exists())
/* 136 */       path.toFile().getParentFile().mkdirs(); 
/* 137 */     Path temp = Paths.get(path.toAbsolutePath().toString() + ".temp", new String[0]);
/*     */     
/*     */     try {
/* 140 */       response = getResponse((HttpRequestBase)httpGet);
/* 141 */       StatusLine st = response.getStatusLine();
/* 142 */       HttpEntity entity = response.getEntity();
/* 143 */       if (200 != response.getStatusLine().getStatusCode()) {
/* 144 */         throw new IOException(String.format("code %s phrase %s", new Object[] { Integer.valueOf(st.getStatusCode()), st.getReasonPhrase() }));
/*     */       }
/* 146 */       in = new BufferedInputStream(entity.getContent());
/* 147 */       out = new BufferedOutputStream(new FileOutputStream(temp.toFile()));
/* 148 */       byte[] buffer = new byte[65536];
/* 149 */       int curread = in.read(buffer);
/* 150 */       while (curread != -1) {
/* 151 */         out.write(buffer, 0, curread);
/* 152 */         curread = in.read(buffer);
/*     */       } 
/*     */     } finally {
/* 155 */       if (Objects.nonNull(httpGet))
/* 156 */         httpGet.abort(); 
/* 157 */       IOUtils.closeQuietly(in);
/* 158 */       IOUtils.closeQuietly(out);
/*     */     } 
/* 160 */     Files.move(Paths.get(temp.toString(), new String[0]), path.toAbsolutePath(), new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/* 161 */     RequestMetadata requestMetadata = generateRequestMetadata(response);
/* 162 */     return requestMetadata;
/*     */   }
/*     */ 
/*     */   
/*     */   private CloseableHttpResponse getResponse(HttpRequestBase http) throws IOException {
/* 167 */     http.setConfig(this.requestConfig);
/* 168 */     return this.httpclient.execute((HttpUriRequest)http);
/*     */   }
/*     */   
/*     */   private RequestMetadata generateRequestMetadata(CloseableHttpResponse response) {
/* 172 */     RequestMetadata requestMetadata = new RequestMetadata();
/* 173 */     if (response.containsHeader(Headers.ETAG.getValue()))
/* 174 */       requestMetadata.setETag(response.getFirstHeader(Headers.ETAG.getValue()).getValue().replaceAll("\"", "")); 
/* 175 */     if (response.containsHeader(Headers.LASTMODIFIED.getValue()))
/* 176 */       requestMetadata.setLastModified(response
/* 177 */           .getFirstHeader(Headers.LASTMODIFIED.getValue()).getValue().replaceAll("\"", "")); 
/* 178 */     return requestMetadata;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRequestByUrl(String url, Map<String, String> map) throws IOException {
/* 183 */     SocketTimeoutException ste = null;
/* 184 */     for (int attepmts = 0; attepmts < this.maxAttepmts; attepmts++) {
/*     */       try {
/* 186 */         return getStringByUrl(url, map);
/* 187 */       } catch (SocketTimeoutException e) {
/* 188 */         ste = e;
/*     */       } 
/*     */     } 
/* 191 */     throw ste;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\http\download\impl\HttpServiceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */