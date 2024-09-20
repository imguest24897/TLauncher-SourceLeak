/*     */ package ch.qos.logback.core.net;
/*     */ 
/*     */ import ch.qos.logback.core.AppenderBase;
/*     */ import ch.qos.logback.core.Layout;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.nio.charset.Charset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SyslogAppenderBase<E>
/*     */   extends AppenderBase<E>
/*     */ {
/*     */   static final String SYSLOG_LAYOUT_URL = "http://logback.qos.ch/codes.html#syslog_layout";
/*     */   static final int MAX_MESSAGE_SIZE_LIMIT = 65000;
/*     */   Layout<E> layout;
/*     */   String facilityStr;
/*     */   String syslogHost;
/*     */   protected String suffixPattern;
/*     */   SyslogOutputStream sos;
/*  43 */   int port = 514;
/*     */   int maxMessageSize;
/*     */   Charset charset;
/*     */   
/*     */   public void start() {
/*  48 */     int errorCount = 0;
/*  49 */     if (this.facilityStr == null) {
/*  50 */       addError("The Facility option is mandatory");
/*  51 */       errorCount++;
/*     */     } 
/*     */     
/*  54 */     if (this.charset == null)
/*     */     {
/*     */       
/*  57 */       this.charset = Charset.defaultCharset();
/*     */     }
/*     */     
/*     */     try {
/*  61 */       this.sos = createOutputStream();
/*     */       
/*  63 */       int systemDatagramSize = this.sos.getSendBufferSize();
/*  64 */       if (this.maxMessageSize == 0) {
/*  65 */         this.maxMessageSize = Math.min(systemDatagramSize, 65000);
/*  66 */         addInfo("Defaulting maxMessageSize to [" + this.maxMessageSize + "]");
/*  67 */       } else if (this.maxMessageSize > systemDatagramSize) {
/*  68 */         addWarn("maxMessageSize of [" + this.maxMessageSize + "] is larger than the system defined datagram size of [" + systemDatagramSize + "].");
/*  69 */         addWarn("This may result in dropped logs.");
/*     */       } 
/*  71 */     } catch (UnknownHostException e) {
/*  72 */       addError("Could not create SyslogWriter", e);
/*  73 */       errorCount++;
/*  74 */     } catch (SocketException e) {
/*  75 */       addWarn("Failed to bind to a random datagram socket. Will try to reconnect later.", e);
/*     */     } 
/*     */     
/*  78 */     if (this.layout == null) {
/*  79 */       this.layout = buildLayout();
/*     */     }
/*     */     
/*  82 */     if (errorCount == 0) {
/*  83 */       super.start();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract SyslogOutputStream createOutputStream() throws UnknownHostException, SocketException;
/*     */   
/*     */   public abstract Layout<E> buildLayout();
/*     */   
/*     */   public abstract int getSeverityForEvent(Object paramObject);
/*     */   
/*     */   protected void append(E eventObject) {
/*  95 */     if (!isStarted()) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/* 100 */       String msg = this.layout.doLayout(eventObject);
/* 101 */       if (msg == null) {
/*     */         return;
/*     */       }
/* 104 */       if (msg.length() > this.maxMessageSize) {
/* 105 */         msg = msg.substring(0, this.maxMessageSize);
/*     */       }
/* 107 */       this.sos.write(msg.getBytes(this.charset));
/* 108 */       this.sos.flush();
/* 109 */       postProcess(eventObject, this.sos);
/* 110 */     } catch (IOException ioe) {
/* 111 */       addError("Failed to send diagram to " + this.syslogHost, ioe);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void postProcess(Object event, OutputStream sw) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int facilityStringToint(String facilityStr) {
/* 126 */     if ("KERN".equalsIgnoreCase(facilityStr))
/* 127 */       return 0; 
/* 128 */     if ("USER".equalsIgnoreCase(facilityStr))
/* 129 */       return 8; 
/* 130 */     if ("MAIL".equalsIgnoreCase(facilityStr))
/* 131 */       return 16; 
/* 132 */     if ("DAEMON".equalsIgnoreCase(facilityStr))
/* 133 */       return 24; 
/* 134 */     if ("AUTH".equalsIgnoreCase(facilityStr))
/* 135 */       return 32; 
/* 136 */     if ("SYSLOG".equalsIgnoreCase(facilityStr))
/* 137 */       return 40; 
/* 138 */     if ("LPR".equalsIgnoreCase(facilityStr))
/* 139 */       return 48; 
/* 140 */     if ("NEWS".equalsIgnoreCase(facilityStr))
/* 141 */       return 56; 
/* 142 */     if ("UUCP".equalsIgnoreCase(facilityStr))
/* 143 */       return 64; 
/* 144 */     if ("CRON".equalsIgnoreCase(facilityStr))
/* 145 */       return 72; 
/* 146 */     if ("AUTHPRIV".equalsIgnoreCase(facilityStr))
/* 147 */       return 80; 
/* 148 */     if ("FTP".equalsIgnoreCase(facilityStr))
/* 149 */       return 88; 
/* 150 */     if ("NTP".equalsIgnoreCase(facilityStr))
/* 151 */       return 96; 
/* 152 */     if ("AUDIT".equalsIgnoreCase(facilityStr))
/* 153 */       return 104; 
/* 154 */     if ("ALERT".equalsIgnoreCase(facilityStr))
/* 155 */       return 112; 
/* 156 */     if ("CLOCK".equalsIgnoreCase(facilityStr))
/* 157 */       return 120; 
/* 158 */     if ("LOCAL0".equalsIgnoreCase(facilityStr))
/* 159 */       return 128; 
/* 160 */     if ("LOCAL1".equalsIgnoreCase(facilityStr))
/* 161 */       return 136; 
/* 162 */     if ("LOCAL2".equalsIgnoreCase(facilityStr))
/* 163 */       return 144; 
/* 164 */     if ("LOCAL3".equalsIgnoreCase(facilityStr))
/* 165 */       return 152; 
/* 166 */     if ("LOCAL4".equalsIgnoreCase(facilityStr))
/* 167 */       return 160; 
/* 168 */     if ("LOCAL5".equalsIgnoreCase(facilityStr))
/* 169 */       return 168; 
/* 170 */     if ("LOCAL6".equalsIgnoreCase(facilityStr))
/* 171 */       return 176; 
/* 172 */     if ("LOCAL7".equalsIgnoreCase(facilityStr)) {
/* 173 */       return 184;
/*     */     }
/* 175 */     throw new IllegalArgumentException(facilityStr + " is not a valid syslog facility string");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSyslogHost() {
/* 183 */     return this.syslogHost;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSyslogHost(String syslogHost) {
/* 193 */     this.syslogHost = syslogHost;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFacility() {
/* 202 */     return this.facilityStr;
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
/*     */   public void setFacility(String facilityStr) {
/* 216 */     if (facilityStr != null) {
/* 217 */       facilityStr = facilityStr.trim();
/*     */     }
/* 219 */     this.facilityStr = facilityStr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPort() {
/* 227 */     return this.port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPort(int port) {
/* 235 */     this.port = port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxMessageSize() {
/* 243 */     return this.maxMessageSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxMessageSize(int maxMessageSize) {
/* 254 */     this.maxMessageSize = maxMessageSize;
/*     */   }
/*     */   
/*     */   public Layout<E> getLayout() {
/* 258 */     return this.layout;
/*     */   }
/*     */   
/*     */   public void setLayout(Layout<E> layout) {
/* 262 */     addWarn("The layout of a SyslogAppender cannot be set directly. See also http://logback.qos.ch/codes.html#syslog_layout");
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 267 */     if (this.sos != null) {
/* 268 */       this.sos.close();
/*     */     }
/* 270 */     super.stop();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSuffixPattern() {
/* 279 */     return this.suffixPattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSuffixPattern(String suffixPattern) {
/* 289 */     this.suffixPattern = suffixPattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Charset getCharset() {
/* 297 */     return this.charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCharset(Charset charset) {
/* 306 */     this.charset = charset;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\ch\qos\logback\core\net\SyslogAppenderBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */