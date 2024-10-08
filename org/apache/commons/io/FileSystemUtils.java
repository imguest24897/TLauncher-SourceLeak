/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.time.Duration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class FileSystemUtils
/*     */ {
/*  54 */   private static final FileSystemUtils INSTANCE = new FileSystemUtils();
/*     */ 
/*     */   
/*     */   private static final int INIT_PROBLEM = -1;
/*     */ 
/*     */   
/*     */   private static final int OTHER = 0;
/*     */   
/*     */   private static final int WINDOWS = 1;
/*     */   
/*     */   private static final int UNIX = 2;
/*     */   
/*     */   private static final int POSIX_UNIX = 3;
/*     */   
/*     */   private static final int OS;
/*     */   
/*     */   private static final String DF;
/*     */ 
/*     */   
/*     */   static {
/*  74 */     int os = 0;
/*  75 */     String dfPath = "df";
/*     */     try {
/*  77 */       String osName = System.getProperty("os.name");
/*  78 */       if (osName == null) {
/*  79 */         throw new IOException("os.name not found");
/*     */       }
/*  81 */       osName = osName.toLowerCase(Locale.ENGLISH);
/*     */       
/*  83 */       if (osName.contains("windows")) {
/*  84 */         os = 1;
/*  85 */       } else if (osName.contains("linux") || osName
/*  86 */         .contains("mpe/ix") || osName
/*  87 */         .contains("freebsd") || osName
/*  88 */         .contains("openbsd") || osName
/*  89 */         .contains("irix") || osName
/*  90 */         .contains("digital unix") || osName
/*  91 */         .contains("unix") || osName
/*  92 */         .contains("mac os x")) {
/*  93 */         os = 2;
/*  94 */       } else if (osName.contains("sun os") || osName
/*  95 */         .contains("sunos") || osName
/*  96 */         .contains("solaris")) {
/*  97 */         os = 3;
/*  98 */         dfPath = "/usr/xpg4/bin/df";
/*  99 */       } else if (osName.contains("hp-ux") || osName
/* 100 */         .contains("aix")) {
/* 101 */         os = 3;
/*     */       }
/*     */     
/* 104 */     } catch (Exception ex) {
/* 105 */       os = -1;
/*     */     } 
/* 107 */     OS = os;
/* 108 */     DF = dfPath;
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
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static long freeSpace(String path) throws IOException {
/* 145 */     return INSTANCE.freeSpaceOS(path, OS, false, Duration.ofMillis(-1L));
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
/*     */   @Deprecated
/*     */   public static long freeSpaceKb(String path) throws IOException {
/* 175 */     return freeSpaceKb(path, -1L);
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
/*     */   @Deprecated
/*     */   public static long freeSpaceKb(String path, long timeout) throws IOException {
/* 206 */     return INSTANCE.freeSpaceOS(path, OS, true, Duration.ofMillis(timeout));
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
/*     */   @Deprecated
/*     */   public static long freeSpaceKb() throws IOException {
/* 225 */     return freeSpaceKb(-1L);
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
/*     */   @Deprecated
/*     */   public static long freeSpaceKb(long timeout) throws IOException {
/* 246 */     return freeSpaceKb((new File(".")).getAbsolutePath(), timeout);
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
/*     */   long freeSpaceOS(String path, int os, boolean kb, Duration timeout) throws IOException {
/* 270 */     if (path == null) {
/* 271 */       throw new IllegalArgumentException("Path must not be null");
/*     */     }
/* 273 */     switch (os) {
/*     */       case 1:
/* 275 */         return kb ? (freeSpaceWindows(path, timeout) / 1024L) : freeSpaceWindows(path, timeout);
/*     */       case 2:
/* 277 */         return freeSpaceUnix(path, kb, false, timeout);
/*     */       case 3:
/* 279 */         return freeSpaceUnix(path, kb, true, timeout);
/*     */       case 0:
/* 281 */         throw new IllegalStateException("Unsupported operating system");
/*     */     } 
/* 283 */     throw new IllegalStateException("Exception caught when determining operating system");
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
/*     */   long freeSpaceWindows(String path, Duration timeout) throws IOException {
/* 298 */     String normPath = FilenameUtils.normalize(path, false);
/* 299 */     if (normPath == null) {
/* 300 */       throw new IllegalArgumentException(path);
/*     */     }
/* 302 */     if (!normPath.isEmpty() && normPath.charAt(0) != '"') {
/* 303 */       normPath = "\"" + normPath + "\"";
/*     */     }
/*     */ 
/*     */     
/* 307 */     String[] cmdAttribs = { "cmd.exe", "/C", "dir /a /-c " + normPath };
/*     */ 
/*     */     
/* 310 */     List<String> lines = performCommand(cmdAttribs, 2147483647, timeout);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 316 */     for (int i = lines.size() - 1; i >= 0; i--) {
/* 317 */       String line = lines.get(i);
/* 318 */       if (!line.isEmpty()) {
/* 319 */         return parseDir(line, normPath);
/*     */       }
/*     */     } 
/*     */     
/* 323 */     throw new IOException("Command line 'dir /-c' did not return any info for path '" + normPath + "'");
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
/*     */   long parseDir(String line, String path) throws IOException {
/* 341 */     int bytesStart = 0;
/* 342 */     int bytesEnd = 0;
/* 343 */     int j = line.length() - 1;
/* 344 */     while (j >= 0) {
/* 345 */       char c = line.charAt(j);
/* 346 */       if (Character.isDigit(c)) {
/*     */ 
/*     */         
/* 349 */         bytesEnd = j + 1;
/*     */         break;
/*     */       } 
/* 352 */       j--;
/*     */     } 
/* 354 */     while (j >= 0) {
/* 355 */       char c = line.charAt(j);
/* 356 */       if (!Character.isDigit(c) && c != ',' && c != '.') {
/*     */ 
/*     */         
/* 359 */         bytesStart = j + 1;
/*     */         break;
/*     */       } 
/* 362 */       j--;
/*     */     } 
/* 364 */     if (j < 0) {
/* 365 */       throw new IOException("Command line 'dir /-c' did not return valid info for path '" + path + "'");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 371 */     StringBuilder buf = new StringBuilder(line.substring(bytesStart, bytesEnd));
/* 372 */     for (int k = 0; k < buf.length(); k++) {
/* 373 */       if (buf.charAt(k) == ',' || buf.charAt(k) == '.') {
/* 374 */         buf.deleteCharAt(k--);
/*     */       }
/*     */     } 
/* 377 */     return parseBytes(buf.toString(), path);
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
/*     */   long freeSpaceUnix(String path, boolean kb, boolean posix, Duration timeout) throws IOException {
/* 393 */     if (path.isEmpty()) {
/* 394 */       throw new IllegalArgumentException("Path must not be empty");
/*     */     }
/*     */ 
/*     */     
/* 398 */     String flags = "-";
/* 399 */     if (kb) {
/* 400 */       flags = flags + "k";
/*     */     }
/* 402 */     if (posix) {
/* 403 */       flags = flags + "P";
/*     */     }
/*     */     
/* 406 */     (new String[3])[0] = DF; (new String[3])[1] = flags; (new String[3])[2] = path; (new String[2])[0] = DF; (new String[2])[1] = path; String[] cmdAttribs = (flags.length() > 1) ? new String[3] : new String[2];
/*     */ 
/*     */     
/* 409 */     List<String> lines = performCommand(cmdAttribs, 3, timeout);
/* 410 */     if (lines.size() < 2)
/*     */     {
/* 412 */       throw new IOException("Command line '" + DF + "' did not return info as expected for path '" + path + "'- response was " + lines);
/*     */     }
/*     */ 
/*     */     
/* 416 */     String line2 = lines.get(1);
/*     */ 
/*     */     
/* 419 */     StringTokenizer tok = new StringTokenizer(line2, " ");
/* 420 */     if (tok.countTokens() < 4) {
/*     */       
/* 422 */       if (tok.countTokens() != 1 || lines.size() < 3) {
/* 423 */         throw new IOException("Command line '" + DF + "' did not return data as expected for path '" + path + "'- check path is valid");
/*     */       }
/*     */ 
/*     */       
/* 427 */       String line3 = lines.get(2);
/* 428 */       tok = new StringTokenizer(line3, " ");
/*     */     } else {
/* 430 */       tok.nextToken();
/*     */     } 
/* 432 */     tok.nextToken();
/* 433 */     tok.nextToken();
/* 434 */     String freeSpace = tok.nextToken();
/* 435 */     return parseBytes(freeSpace, path);
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
/*     */   long parseBytes(String freeSpace, String path) throws IOException {
/*     */     try {
/* 448 */       long bytes = Long.parseLong(freeSpace);
/* 449 */       if (bytes < 0L) {
/* 450 */         throw new IOException("Command line '" + DF + "' did not find free space in response for path '" + path + "'- check path is valid");
/*     */       }
/*     */ 
/*     */       
/* 454 */       return bytes;
/*     */     }
/* 456 */     catch (NumberFormatException ex) {
/* 457 */       throw new IOException("Command line '" + DF + "' did not return numeric data as expected for path '" + path + "'- check path is valid", ex);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<String> performCommand(String[] cmdAttribs, int max, Duration timeout) throws IOException {
/* 482 */     List<String> lines = new ArrayList<>(20);
/* 483 */     Process proc = null;
/* 484 */     InputStream in = null;
/* 485 */     OutputStream out = null;
/* 486 */     InputStream err = null;
/* 487 */     BufferedReader inr = null;
/*     */     
/*     */     try {
/* 490 */       Thread monitor = ThreadMonitor.start(timeout);
/*     */       
/* 492 */       proc = openProcess(cmdAttribs);
/* 493 */       in = proc.getInputStream();
/* 494 */       out = proc.getOutputStream();
/* 495 */       err = proc.getErrorStream();
/*     */       
/* 497 */       inr = new BufferedReader(new InputStreamReader(in, Charset.defaultCharset()));
/* 498 */       String line = inr.readLine();
/* 499 */       while (line != null && lines.size() < max) {
/* 500 */         line = line.toLowerCase(Locale.ENGLISH).trim();
/* 501 */         lines.add(line);
/* 502 */         line = inr.readLine();
/*     */       } 
/*     */       
/* 505 */       proc.waitFor();
/*     */       
/* 507 */       ThreadMonitor.stop(monitor);
/*     */       
/* 509 */       if (proc.exitValue() != 0)
/*     */       {
/* 511 */         throw new IOException("Command line returned OS error code '" + proc
/* 512 */             .exitValue() + "' for command " + 
/* 513 */             Arrays.asList(cmdAttribs));
/*     */       }
/* 515 */       if (lines.isEmpty())
/*     */       {
/* 517 */         throw new IOException("Command line did not return any info for command " + 
/*     */             
/* 519 */             Arrays.asList(cmdAttribs));
/*     */       }
/*     */       
/* 522 */       inr.close();
/* 523 */       inr = null;
/*     */       
/* 525 */       in.close();
/* 526 */       in = null;
/*     */       
/* 528 */       if (out != null) {
/* 529 */         out.close();
/* 530 */         out = null;
/*     */       } 
/*     */       
/* 533 */       if (err != null) {
/* 534 */         err.close();
/* 535 */         err = null;
/*     */       } 
/*     */       
/* 538 */       return lines;
/*     */     }
/* 540 */     catch (InterruptedException ex) {
/* 541 */       throw new IOException("Command line threw an InterruptedException for command " + 
/*     */           
/* 543 */           Arrays.asList(cmdAttribs) + " timeout=" + timeout, ex);
/*     */     } finally {
/* 545 */       IOUtils.closeQuietly(in);
/* 546 */       IOUtils.closeQuietly(out);
/* 547 */       IOUtils.closeQuietly(err);
/* 548 */       IOUtils.closeQuietly(inr);
/* 549 */       if (proc != null) {
/* 550 */         proc.destroy();
/*     */       }
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
/*     */   Process openProcess(String[] cmdAttribs) throws IOException {
/* 563 */     return Runtime.getRuntime().exec(cmdAttribs);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\io\FileSystemUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */