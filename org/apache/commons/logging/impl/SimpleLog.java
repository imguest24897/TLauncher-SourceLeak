/*     */ package org.apache.commons.logging.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Properties;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogConfigurationException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleLog
/*     */   implements Log, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 136942970684951178L;
/*     */   protected static final String systemPrefix = "org.apache.commons.logging.simplelog.";
/*  88 */   protected static final Properties simpleLogProps = new Properties();
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final String DEFAULT_DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss:SSS zzz";
/*     */ 
/*     */   
/*     */   protected static boolean showLogName = false;
/*     */ 
/*     */   
/*     */   protected static boolean showShortName = true;
/*     */ 
/*     */   
/*     */   protected static boolean showDateTime = false;
/*     */ 
/*     */   
/* 104 */   protected static String dateTimeFormat = "yyyy/MM/dd HH:mm:ss:SSS zzz";
/*     */   
/* 106 */   protected static DateFormat dateFormatter = null;
/*     */ 
/*     */   
/*     */   public static final int LOG_LEVEL_TRACE = 1;
/*     */ 
/*     */   
/*     */   public static final int LOG_LEVEL_DEBUG = 2;
/*     */ 
/*     */   
/*     */   public static final int LOG_LEVEL_INFO = 3;
/*     */ 
/*     */   
/*     */   public static final int LOG_LEVEL_WARN = 4;
/*     */ 
/*     */   
/*     */   public static final int LOG_LEVEL_ERROR = 5;
/*     */ 
/*     */   
/*     */   public static final int LOG_LEVEL_FATAL = 6;
/*     */   
/*     */   public static final int LOG_LEVEL_ALL = 0;
/*     */   
/*     */   public static final int LOG_LEVEL_OFF = 7;
/*     */ 
/*     */   
/*     */   private static String getStringProperty(String name) {
/* 132 */     String prop = null;
/*     */     try {
/* 134 */       prop = System.getProperty(name);
/* 135 */     } catch (SecurityException e) {}
/*     */ 
/*     */     
/* 138 */     return (prop == null) ? simpleLogProps.getProperty(name) : prop;
/*     */   }
/*     */   
/*     */   private static String getStringProperty(String name, String dephault) {
/* 142 */     String prop = getStringProperty(name);
/* 143 */     return (prop == null) ? dephault : prop;
/*     */   }
/*     */   
/*     */   private static boolean getBooleanProperty(String name, boolean dephault) {
/* 147 */     String prop = getStringProperty(name);
/* 148 */     return (prop == null) ? dephault : "true".equalsIgnoreCase(prop);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 156 */     InputStream in = getResourceAsStream("simplelog.properties");
/* 157 */     if (null != in) {
/*     */       
/* 159 */       try { simpleLogProps.load(in); }
/* 160 */       catch (IOException e)
/*     */       
/*     */       { 
/*     */         try {
/* 164 */           in.close();
/* 165 */         } catch (IOException iOException) {} } finally { try { in.close(); } catch (IOException e) {} }
/*     */     
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 171 */     showLogName = getBooleanProperty("org.apache.commons.logging.simplelog.showlogname", showLogName);
/* 172 */     showShortName = getBooleanProperty("org.apache.commons.logging.simplelog.showShortLogname", showShortName);
/* 173 */     showDateTime = getBooleanProperty("org.apache.commons.logging.simplelog.showdatetime", showDateTime);
/*     */     
/* 175 */     if (showDateTime) {
/* 176 */       dateTimeFormat = getStringProperty("org.apache.commons.logging.simplelog.dateTimeFormat", dateTimeFormat);
/*     */       try {
/* 178 */         dateFormatter = new SimpleDateFormat(dateTimeFormat);
/* 179 */       } catch (IllegalArgumentException e) {
/*     */         
/* 181 */         dateTimeFormat = "yyyy/MM/dd HH:mm:ss:SSS zzz";
/* 182 */         dateFormatter = new SimpleDateFormat(dateTimeFormat);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 190 */   protected String logName = null;
/*     */   
/*     */   protected int currentLogLevel;
/*     */   
/* 194 */   private String shortLogName = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleLog(String name) {
/* 206 */     this.logName = name;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 211 */     setLevel(3);
/*     */ 
/*     */     
/* 214 */     String lvl = getStringProperty("org.apache.commons.logging.simplelog.log." + this.logName);
/* 215 */     int i = String.valueOf(name).lastIndexOf(".");
/* 216 */     while (null == lvl && i > -1) {
/* 217 */       name = name.substring(0, i);
/* 218 */       lvl = getStringProperty("org.apache.commons.logging.simplelog.log." + name);
/* 219 */       i = String.valueOf(name).lastIndexOf(".");
/*     */     } 
/*     */     
/* 222 */     if (null == lvl) {
/* 223 */       lvl = getStringProperty("org.apache.commons.logging.simplelog.defaultlog");
/*     */     }
/*     */     
/* 226 */     if ("all".equalsIgnoreCase(lvl)) {
/* 227 */       setLevel(0);
/* 228 */     } else if ("trace".equalsIgnoreCase(lvl)) {
/* 229 */       setLevel(1);
/* 230 */     } else if ("debug".equalsIgnoreCase(lvl)) {
/* 231 */       setLevel(2);
/* 232 */     } else if ("info".equalsIgnoreCase(lvl)) {
/* 233 */       setLevel(3);
/* 234 */     } else if ("warn".equalsIgnoreCase(lvl)) {
/* 235 */       setLevel(4);
/* 236 */     } else if ("error".equalsIgnoreCase(lvl)) {
/* 237 */       setLevel(5);
/* 238 */     } else if ("fatal".equalsIgnoreCase(lvl)) {
/* 239 */       setLevel(6);
/* 240 */     } else if ("off".equalsIgnoreCase(lvl)) {
/* 241 */       setLevel(7);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLevel(int currentLogLevel) {
/* 258 */     this.currentLogLevel = currentLogLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLevel() {
/* 269 */     return this.currentLogLevel;
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
/*     */   protected void log(int type, Object message, Throwable t) {
/* 289 */     StringBuffer buf = new StringBuffer();
/*     */ 
/*     */     
/* 292 */     if (showDateTime) {
/* 293 */       buf.append(dateFormatter.format(new Date()));
/* 294 */       buf.append(" ");
/*     */     } 
/*     */ 
/*     */     
/* 298 */     switch (type) {
/*     */       case 1:
/* 300 */         buf.append("[TRACE] ");
/*     */         break;
/*     */       case 2:
/* 303 */         buf.append("[DEBUG] ");
/*     */         break;
/*     */       case 3:
/* 306 */         buf.append("[INFO] ");
/*     */         break;
/*     */       case 4:
/* 309 */         buf.append("[WARN] ");
/*     */         break;
/*     */       case 5:
/* 312 */         buf.append("[ERROR] ");
/*     */         break;
/*     */       case 6:
/* 315 */         buf.append("[FATAL] ");
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 320 */     if (showShortName) {
/* 321 */       if (this.shortLogName == null) {
/*     */         
/* 323 */         this.shortLogName = this.logName.substring(this.logName.lastIndexOf(".") + 1);
/* 324 */         this.shortLogName = this.shortLogName.substring(this.shortLogName.lastIndexOf("/") + 1);
/*     */       } 
/* 326 */       buf.append(String.valueOf(this.shortLogName)).append(" - ");
/* 327 */     } else if (showLogName) {
/* 328 */       buf.append(String.valueOf(this.logName)).append(" - ");
/*     */     } 
/*     */ 
/*     */     
/* 332 */     buf.append(String.valueOf(message));
/*     */ 
/*     */     
/* 335 */     if (t != null) {
/* 336 */       buf.append(" <");
/* 337 */       buf.append(t.toString());
/* 338 */       buf.append(">");
/*     */       
/* 340 */       StringWriter sw = new StringWriter(1024);
/* 341 */       PrintWriter pw = new PrintWriter(sw);
/* 342 */       t.printStackTrace(pw);
/* 343 */       pw.close();
/* 344 */       buf.append(sw.toString());
/*     */     } 
/*     */ 
/*     */     
/* 348 */     write(buf);
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
/*     */   protected void write(StringBuffer buffer) {
/* 365 */     System.err.println(buffer.toString());
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
/*     */   protected boolean isLevelEnabled(int logLevel) {
/* 378 */     return (logLevel >= this.currentLogLevel);
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
/*     */   public final void debug(Object message) {
/* 390 */     if (isLevelEnabled(2)) {
/* 391 */       log(2, message, null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void debug(Object message, Throwable t) {
/* 402 */     if (isLevelEnabled(2)) {
/* 403 */       log(2, message, t);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void trace(Object message) {
/* 414 */     if (isLevelEnabled(1)) {
/* 415 */       log(1, message, null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void trace(Object message, Throwable t) {
/* 426 */     if (isLevelEnabled(1)) {
/* 427 */       log(1, message, t);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void info(Object message) {
/* 438 */     if (isLevelEnabled(3)) {
/* 439 */       log(3, message, null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void info(Object message, Throwable t) {
/* 450 */     if (isLevelEnabled(3)) {
/* 451 */       log(3, message, t);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void warn(Object message) {
/* 462 */     if (isLevelEnabled(4)) {
/* 463 */       log(4, message, null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void warn(Object message, Throwable t) {
/* 474 */     if (isLevelEnabled(4)) {
/* 475 */       log(4, message, t);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void error(Object message) {
/* 486 */     if (isLevelEnabled(5)) {
/* 487 */       log(5, message, null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void error(Object message, Throwable t) {
/* 498 */     if (isLevelEnabled(5)) {
/* 499 */       log(5, message, t);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void fatal(Object message) {
/* 510 */     if (isLevelEnabled(6)) {
/* 511 */       log(6, message, null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void fatal(Object message, Throwable t) {
/* 522 */     if (isLevelEnabled(6)) {
/* 523 */       log(6, message, t);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isDebugEnabled() {
/* 539 */     return isLevelEnabled(2);
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
/*     */   public final boolean isErrorEnabled() {
/* 554 */     return isLevelEnabled(5);
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
/*     */   public final boolean isFatalEnabled() {
/* 569 */     return isLevelEnabled(6);
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
/*     */   public final boolean isInfoEnabled() {
/* 584 */     return isLevelEnabled(3);
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
/*     */   public final boolean isTraceEnabled() {
/* 599 */     return isLevelEnabled(1);
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
/*     */   public final boolean isWarnEnabled() {
/* 614 */     return isLevelEnabled(4);
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
/*     */   private static ClassLoader getContextClassLoader() {
/* 627 */     ClassLoader classLoader = null;
/*     */     
/* 629 */     if (classLoader == null) {
/*     */       
/*     */       try {
/* 632 */         Method method = Thread.class.getMethod("getContextClassLoader", new Class[0]);
/*     */ 
/*     */         
/*     */         try {
/* 636 */           classLoader = (ClassLoader)method.invoke(Thread.currentThread(), new Object[0]);
/* 637 */         } catch (IllegalAccessException e) {
/*     */         
/* 639 */         } catch (InvocationTargetException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 653 */           if (!(e.getTargetException() instanceof SecurityException))
/*     */           {
/*     */ 
/*     */ 
/*     */             
/* 658 */             throw new LogConfigurationException("Unexpected InvocationTargetException", e.getTargetException());
/*     */           }
/*     */         } 
/* 661 */       } catch (NoSuchMethodException e) {}
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 667 */     if (classLoader == null) {
/* 668 */       classLoader = SimpleLog.class.getClassLoader();
/*     */     }
/*     */ 
/*     */     
/* 672 */     return classLoader;
/*     */   }
/*     */   
/*     */   private static InputStream getResourceAsStream(final String name) {
/* 676 */     return AccessController.<InputStream>doPrivileged(new PrivilegedAction<InputStream>() {
/*     */           public InputStream run() {
/* 678 */             ClassLoader threadCL = SimpleLog.getContextClassLoader();
/*     */             
/* 680 */             if (threadCL != null) {
/* 681 */               return threadCL.getResourceAsStream(name);
/*     */             }
/* 683 */             return ClassLoader.getSystemResourceAsStream(name);
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\logging\impl\SimpleLog.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */