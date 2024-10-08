/*      */ package org.apache.commons.io;
/*      */ 
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileFilter;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.UncheckedIOException;
/*      */ import java.math.BigInteger;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.nio.file.CopyOption;
/*      */ import java.nio.file.FileVisitOption;
/*      */ import java.nio.file.FileVisitor;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.LinkOption;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.StandardCopyOption;
/*      */ import java.time.Instant;
/*      */ import java.time.LocalTime;
/*      */ import java.time.ZoneId;
/*      */ import java.time.chrono.ChronoLocalDate;
/*      */ import java.time.chrono.ChronoLocalDateTime;
/*      */ import java.time.chrono.ChronoZonedDateTime;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.stream.Collector;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import java.util.zip.CRC32;
/*      */ import java.util.zip.CheckedInputStream;
/*      */ import java.util.zip.Checksum;
/*      */ import org.apache.commons.io.file.AccumulatorPathVisitor;
/*      */ import org.apache.commons.io.file.Counters;
/*      */ import org.apache.commons.io.file.DeleteOption;
/*      */ import org.apache.commons.io.file.PathFilter;
/*      */ import org.apache.commons.io.file.PathUtils;
/*      */ import org.apache.commons.io.file.StandardDeleteOption;
/*      */ import org.apache.commons.io.filefilter.FileEqualsFileFilter;
/*      */ import org.apache.commons.io.filefilter.FileFileFilter;
/*      */ import org.apache.commons.io.filefilter.IOFileFilter;
/*      */ import org.apache.commons.io.filefilter.SuffixFileFilter;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class FileUtils
/*      */ {
/*      */   public static final long ONE_KB = 1024L;
/*  115 */   public static final BigInteger ONE_KB_BI = BigInteger.valueOf(1024L);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final long ONE_MB = 1048576L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  127 */   public static final BigInteger ONE_MB_BI = ONE_KB_BI.multiply(ONE_KB_BI);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final long ONE_GB = 1073741824L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  139 */   public static final BigInteger ONE_GB_BI = ONE_KB_BI.multiply(ONE_MB_BI);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final long ONE_TB = 1099511627776L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  151 */   public static final BigInteger ONE_TB_BI = ONE_KB_BI.multiply(ONE_GB_BI);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final long ONE_PB = 1125899906842624L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  163 */   public static final BigInteger ONE_PB_BI = ONE_KB_BI.multiply(ONE_TB_BI);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final long ONE_EB = 1152921504606846976L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  175 */   public static final BigInteger ONE_EB_BI = ONE_KB_BI.multiply(ONE_PB_BI);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  180 */   public static final BigInteger ONE_ZB = BigInteger.valueOf(1024L).multiply(BigInteger.valueOf(1152921504606846976L));
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  185 */   public static final BigInteger ONE_YB = ONE_KB_BI.multiply(ONE_ZB);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  190 */   public static final File[] EMPTY_FILE_ARRAY = new File[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static CopyOption[] addCopyAttributes(CopyOption... copyOptions) {
/*  200 */     CopyOption[] actual = Arrays.<CopyOption>copyOf(copyOptions, copyOptions.length + 1);
/*  201 */     Arrays.sort((Object[])actual, 0, copyOptions.length);
/*  202 */     if (Arrays.binarySearch((Object[])copyOptions, 0, copyOptions.length, StandardCopyOption.COPY_ATTRIBUTES) >= 0) {
/*  203 */       return copyOptions;
/*      */     }
/*  205 */     actual[actual.length - 1] = StandardCopyOption.COPY_ATTRIBUTES;
/*  206 */     return actual;
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
/*      */   public static String byteCountToDisplaySize(BigInteger size) {
/*      */     String displaySize;
/*  227 */     Objects.requireNonNull(size, "size");
/*      */ 
/*      */     
/*  230 */     if (size.divide(ONE_EB_BI).compareTo(BigInteger.ZERO) > 0) {
/*  231 */       displaySize = size.divide(ONE_EB_BI) + " EB";
/*  232 */     } else if (size.divide(ONE_PB_BI).compareTo(BigInteger.ZERO) > 0) {
/*  233 */       displaySize = size.divide(ONE_PB_BI) + " PB";
/*  234 */     } else if (size.divide(ONE_TB_BI).compareTo(BigInteger.ZERO) > 0) {
/*  235 */       displaySize = size.divide(ONE_TB_BI) + " TB";
/*  236 */     } else if (size.divide(ONE_GB_BI).compareTo(BigInteger.ZERO) > 0) {
/*  237 */       displaySize = size.divide(ONE_GB_BI) + " GB";
/*  238 */     } else if (size.divide(ONE_MB_BI).compareTo(BigInteger.ZERO) > 0) {
/*  239 */       displaySize = size.divide(ONE_MB_BI) + " MB";
/*  240 */     } else if (size.divide(ONE_KB_BI).compareTo(BigInteger.ZERO) > 0) {
/*  241 */       displaySize = size.divide(ONE_KB_BI) + " KB";
/*      */     } else {
/*  243 */       displaySize = size + " bytes";
/*      */     } 
/*  245 */     return displaySize;
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
/*      */   public static String byteCountToDisplaySize(long size) {
/*  264 */     return byteCountToDisplaySize(BigInteger.valueOf(size));
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
/*      */   public static Checksum checksum(File file, Checksum checksum) throws IOException {
/*  285 */     requireExistsChecked(file, "file");
/*  286 */     requireFile(file, "file");
/*  287 */     Objects.requireNonNull(checksum, "checksum");
/*  288 */     try (InputStream inputStream = new CheckedInputStream(Files.newInputStream(file.toPath(), new java.nio.file.OpenOption[0]), checksum)) {
/*  289 */       IOUtils.consume(inputStream);
/*      */     } 
/*  291 */     return checksum;
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
/*      */   public static long checksumCRC32(File file) throws IOException {
/*  306 */     return checksum(file, new CRC32()).getValue();
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
/*      */   public static void cleanDirectory(File directory) throws IOException {
/*  319 */     File[] files = listFiles(directory, null);
/*      */     
/*  321 */     List<Exception> causeList = new ArrayList<>();
/*  322 */     for (File file : files) {
/*      */       try {
/*  324 */         forceDelete(file);
/*  325 */       } catch (IOException ioe) {
/*  326 */         causeList.add(ioe);
/*      */       } 
/*      */     } 
/*      */     
/*  330 */     if (!causeList.isEmpty()) {
/*  331 */       throw new IOExceptionList(directory.toString(), causeList);
/*      */     }
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
/*      */   private static void cleanDirectoryOnExit(File directory) throws IOException {
/*  345 */     File[] files = listFiles(directory, null);
/*      */     
/*  347 */     List<Exception> causeList = new ArrayList<>();
/*  348 */     for (File file : files) {
/*      */       try {
/*  350 */         forceDeleteOnExit(file);
/*  351 */       } catch (IOException ioe) {
/*  352 */         causeList.add(ioe);
/*      */       } 
/*      */     } 
/*      */     
/*  356 */     if (!causeList.isEmpty()) {
/*  357 */       throw new IOExceptionList(causeList);
/*      */     }
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
/*      */   public static boolean contentEquals(File file1, File file2) throws IOException {
/*  379 */     if (file1 == null && file2 == null) {
/*  380 */       return true;
/*      */     }
/*  382 */     if (file1 == null || file2 == null) {
/*  383 */       return false;
/*      */     }
/*  385 */     boolean file1Exists = file1.exists();
/*  386 */     if (file1Exists != file2.exists()) {
/*  387 */       return false;
/*      */     }
/*      */     
/*  390 */     if (!file1Exists)
/*      */     {
/*  392 */       return true;
/*      */     }
/*      */     
/*  395 */     requireFile(file1, "file1");
/*  396 */     requireFile(file2, "file2");
/*      */     
/*  398 */     if (file1.length() != file2.length())
/*      */     {
/*  400 */       return false;
/*      */     }
/*      */     
/*  403 */     if (file1.getCanonicalFile().equals(file2.getCanonicalFile()))
/*      */     {
/*  405 */       return true;
/*      */     }
/*      */     
/*  408 */     try(InputStream input1 = Files.newInputStream(file1.toPath(), new java.nio.file.OpenOption[0]); InputStream input2 = Files.newInputStream(file2.toPath(), new java.nio.file.OpenOption[0])) {
/*  409 */       return IOUtils.contentEquals(input1, input2);
/*      */     } 
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
/*      */   public static boolean contentEqualsIgnoreEOL(File file1, File file2, String charsetName) throws IOException {
/*  434 */     if (file1 == null && file2 == null) {
/*  435 */       return true;
/*      */     }
/*  437 */     if (file1 == null || file2 == null) {
/*  438 */       return false;
/*      */     }
/*  440 */     boolean file1Exists = file1.exists();
/*  441 */     if (file1Exists != file2.exists()) {
/*  442 */       return false;
/*      */     }
/*      */     
/*  445 */     if (!file1Exists)
/*      */     {
/*  447 */       return true;
/*      */     }
/*      */     
/*  450 */     requireFile(file1, "file1");
/*  451 */     requireFile(file2, "file2");
/*      */     
/*  453 */     if (file1.getCanonicalFile().equals(file2.getCanonicalFile()))
/*      */     {
/*  455 */       return true;
/*      */     }
/*      */     
/*  458 */     Charset charset = Charsets.toCharset(charsetName);
/*  459 */     try(Reader input1 = new InputStreamReader(Files.newInputStream(file1.toPath(), new java.nio.file.OpenOption[0]), charset); 
/*  460 */         Reader input2 = new InputStreamReader(Files.newInputStream(file2.toPath(), new java.nio.file.OpenOption[0]), charset)) {
/*  461 */       return IOUtils.contentEqualsIgnoreEOL(input1, input2);
/*      */     } 
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
/*      */   public static File[] convertFileCollectionToFileArray(Collection<File> files) {
/*  474 */     return files.<File>toArray(EMPTY_FILE_ARRAY);
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
/*      */   public static void copyDirectory(File srcDir, File destDir) throws IOException {
/*  502 */     copyDirectory(srcDir, destDir, true);
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
/*      */   public static void copyDirectory(File srcDir, File destDir, boolean preserveFileDate) throws IOException {
/*  531 */     copyDirectory(srcDir, destDir, null, preserveFileDate);
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
/*      */   public static void copyDirectory(File srcDir, File destDir, FileFilter filter) throws IOException {
/*  580 */     copyDirectory(srcDir, destDir, filter, true);
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
/*      */   public static void copyDirectory(File srcDir, File destDir, FileFilter filter, boolean preserveFileDate) throws IOException {
/*  630 */     copyDirectory(srcDir, destDir, filter, preserveFileDate, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
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
/*      */   public static void copyDirectory(File srcDir, File destDir, FileFilter fileFilter, boolean preserveFileDate, CopyOption... copyOptions) throws IOException {
/*  681 */     requireFileCopy(srcDir, destDir);
/*  682 */     requireDirectory(srcDir, "srcDir");
/*  683 */     requireCanonicalPathsNotEquals(srcDir, destDir);
/*      */ 
/*      */     
/*  686 */     List<String> exclusionList = null;
/*  687 */     String srcDirCanonicalPath = srcDir.getCanonicalPath();
/*  688 */     String destDirCanonicalPath = destDir.getCanonicalPath();
/*  689 */     if (destDirCanonicalPath.startsWith(srcDirCanonicalPath)) {
/*  690 */       File[] srcFiles = listFiles(srcDir, fileFilter);
/*  691 */       if (srcFiles.length > 0) {
/*  692 */         exclusionList = new ArrayList<>(srcFiles.length);
/*  693 */         for (File srcFile : srcFiles) {
/*  694 */           File copiedFile = new File(destDir, srcFile.getName());
/*  695 */           exclusionList.add(copiedFile.getCanonicalPath());
/*      */         } 
/*      */       } 
/*      */     } 
/*  699 */     doCopyDirectory(srcDir, destDir, fileFilter, exclusionList, preserveFileDate, preserveFileDate ? 
/*  700 */         addCopyAttributes(copyOptions) : copyOptions);
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
/*      */   public static void copyDirectoryToDirectory(File sourceDir, File destinationDir) throws IOException {
/*  728 */     requireDirectoryIfExists(sourceDir, "sourceDir");
/*  729 */     requireDirectoryIfExists(destinationDir, "destinationDir");
/*  730 */     copyDirectory(sourceDir, new File(destinationDir, sourceDir.getName()), true);
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
/*      */   public static void copyFile(File srcFile, File destFile) throws IOException {
/*  756 */     copyFile(srcFile, destFile, new CopyOption[] { StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING });
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
/*      */   public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
/*  783 */     (new CopyOption[2])[0] = StandardCopyOption.COPY_ATTRIBUTES; (new CopyOption[2])[1] = StandardCopyOption.REPLACE_EXISTING; (new CopyOption[1])[0] = StandardCopyOption.REPLACE_EXISTING; copyFile(srcFile, destFile, preserveFileDate ? new CopyOption[2] : new CopyOption[1]);
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
/*      */   public static void copyFile(File srcFile, File destFile, boolean preserveFileDate, CopyOption... copyOptions) throws IOException {
/*  816 */     copyFile(srcFile, destFile, preserveFileDate ? addCopyAttributes(copyOptions) : copyOptions);
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
/*      */   public static void copyFile(File srcFile, File destFile, CopyOption... copyOptions) throws IOException {
/*  840 */     requireFileCopy(srcFile, destFile);
/*  841 */     requireFile(srcFile, "srcFile");
/*  842 */     requireCanonicalPathsNotEquals(srcFile, destFile);
/*  843 */     createParentDirectories(destFile);
/*  844 */     requireFileIfExists(destFile, "destFile");
/*  845 */     if (destFile.exists()) {
/*  846 */       requireCanWrite(destFile, "destFile");
/*      */     }
/*      */ 
/*      */     
/*  850 */     Files.copy(srcFile.toPath(), destFile.toPath(), copyOptions);
/*      */ 
/*      */     
/*  853 */     requireEqualSizes(srcFile, destFile, srcFile.length(), destFile.length());
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
/*      */   public static long copyFile(File input, OutputStream output) throws IOException {
/*  871 */     try (InputStream fis = Files.newInputStream(input.toPath(), new java.nio.file.OpenOption[0])) {
/*  872 */       return IOUtils.copyLarge(fis, output);
/*      */     } 
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
/*      */   public static void copyFileToDirectory(File srcFile, File destDir) throws IOException {
/*  897 */     copyFileToDirectory(srcFile, destDir, true);
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
/*      */   public static void copyFileToDirectory(File sourceFile, File destinationDir, boolean preserveFileDate) throws IOException {
/*  924 */     Objects.requireNonNull(sourceFile, "sourceFile");
/*  925 */     requireDirectoryIfExists(destinationDir, "destinationDir");
/*  926 */     copyFile(sourceFile, new File(destinationDir, sourceFile.getName()), preserveFileDate);
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
/*      */   public static void copyInputStreamToFile(InputStream source, File destination) throws IOException {
/*  951 */     try (InputStream inputStream = source) {
/*  952 */       copyToFile(inputStream, destination);
/*      */     } 
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
/*      */   public static void copyToDirectory(File sourceFile, File destinationDir) throws IOException {
/*  983 */     Objects.requireNonNull(sourceFile, "sourceFile");
/*  984 */     if (sourceFile.isFile()) {
/*  985 */       copyFileToDirectory(sourceFile, destinationDir);
/*  986 */     } else if (sourceFile.isDirectory()) {
/*  987 */       copyDirectoryToDirectory(sourceFile, destinationDir);
/*      */     } else {
/*  989 */       throw new FileNotFoundException("The source " + sourceFile + " does not exist");
/*      */     } 
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
/*      */   public static void copyToDirectory(Iterable<File> sourceIterable, File destinationDir) throws IOException {
/* 1017 */     Objects.requireNonNull(sourceIterable, "sourceIterable");
/* 1018 */     for (File src : sourceIterable) {
/* 1019 */       copyFileToDirectory(src, destinationDir);
/*      */     }
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
/*      */   public static void copyToFile(InputStream inputStream, File file) throws IOException {
/* 1042 */     try (OutputStream out = openOutputStream(file)) {
/* 1043 */       IOUtils.copy(inputStream, out);
/*      */     } 
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
/*      */   public static void copyURLToFile(URL source, File destination) throws IOException {
/* 1068 */     try (InputStream stream = source.openStream()) {
/* 1069 */       copyInputStreamToFile(stream, destination);
/*      */     } 
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
/*      */   public static void copyURLToFile(URL source, File destination, int connectionTimeoutMillis, int readTimeoutMillis) throws IOException {
/* 1094 */     URLConnection connection = source.openConnection();
/* 1095 */     connection.setConnectTimeout(connectionTimeoutMillis);
/* 1096 */     connection.setReadTimeout(readTimeoutMillis);
/* 1097 */     try (InputStream stream = connection.getInputStream()) {
/* 1098 */       copyInputStreamToFile(stream, destination);
/*      */     } 
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
/*      */   public static File createParentDirectories(File file) throws IOException {
/* 1113 */     return mkdirs(getParentFile(file));
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
/*      */   static String decodeUrl(String url) {
/* 1131 */     String decoded = url;
/* 1132 */     if (url != null && url.indexOf('%') >= 0) {
/* 1133 */       int n = url.length();
/* 1134 */       StringBuilder buffer = new StringBuilder();
/* 1135 */       ByteBuffer bytes = ByteBuffer.allocate(n);
/* 1136 */       for (int i = 0; i < n; ) {
/* 1137 */         if (url.charAt(i) == '%') {
/*      */           try {
/*      */             do {
/* 1140 */               byte octet = (byte)Integer.parseInt(url.substring(i + 1, i + 3), 16);
/* 1141 */               bytes.put(octet);
/* 1142 */               i += 3;
/* 1143 */             } while (i < n && url.charAt(i) == '%');
/*      */             continue;
/* 1145 */           } catch (RuntimeException runtimeException) {
/*      */ 
/*      */           
/*      */           } finally {
/* 1149 */             if (bytes.position() > 0) {
/* 1150 */               bytes.flip();
/* 1151 */               buffer.append(StandardCharsets.UTF_8.decode(bytes).toString());
/* 1152 */               bytes.clear();
/*      */             } 
/*      */           } 
/*      */         }
/* 1156 */         buffer.append(url.charAt(i++));
/*      */       } 
/* 1158 */       decoded = buffer.toString();
/*      */     } 
/* 1160 */     return decoded;
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
/*      */   public static File delete(File file) throws IOException {
/* 1174 */     Objects.requireNonNull(file, "file");
/* 1175 */     Files.delete(file.toPath());
/* 1176 */     return file;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void deleteDirectory(File directory) throws IOException {
/* 1187 */     Objects.requireNonNull(directory, "directory");
/* 1188 */     if (!directory.exists()) {
/*      */       return;
/*      */     }
/* 1191 */     if (!isSymlink(directory)) {
/* 1192 */       cleanDirectory(directory);
/*      */     }
/* 1194 */     delete(directory);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void deleteDirectoryOnExit(File directory) throws IOException {
/* 1205 */     if (!directory.exists()) {
/*      */       return;
/*      */     }
/* 1208 */     directory.deleteOnExit();
/* 1209 */     if (!isSymlink(directory)) {
/* 1210 */       cleanDirectoryOnExit(directory);
/*      */     }
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
/*      */   public static boolean deleteQuietly(File file) {
/* 1231 */     if (file == null) {
/* 1232 */       return false;
/*      */     }
/*      */     try {
/* 1235 */       if (file.isDirectory()) {
/* 1236 */         cleanDirectory(file);
/*      */       }
/* 1238 */     } catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1243 */       return file.delete();
/* 1244 */     } catch (Exception ignored) {
/* 1245 */       return false;
/*      */     } 
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
/*      */   public static boolean directoryContains(File directory, File child) throws IOException {
/* 1273 */     requireDirectoryExists(directory, "directory");
/*      */     
/* 1275 */     if (child == null) {
/* 1276 */       return false;
/*      */     }
/*      */     
/* 1279 */     if (!directory.exists() || !child.exists()) {
/* 1280 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1284 */     return FilenameUtils.directoryContains(directory.getCanonicalPath(), child.getCanonicalPath());
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
/*      */   private static void doCopyDirectory(File srcDir, File destDir, FileFilter fileFilter, List<String> exclusionList, boolean preserveDirDate, CopyOption... copyOptions) throws IOException {
/* 1302 */     File[] srcFiles = listFiles(srcDir, fileFilter);
/* 1303 */     requireDirectoryIfExists(destDir, "destDir");
/* 1304 */     mkdirs(destDir);
/* 1305 */     requireCanWrite(destDir, "destDir");
/* 1306 */     for (File srcFile : srcFiles) {
/* 1307 */       File dstFile = new File(destDir, srcFile.getName());
/* 1308 */       if (exclusionList == null || !exclusionList.contains(srcFile.getCanonicalPath())) {
/* 1309 */         if (srcFile.isDirectory()) {
/* 1310 */           doCopyDirectory(srcFile, dstFile, fileFilter, exclusionList, preserveDirDate, copyOptions);
/*      */         } else {
/* 1312 */           copyFile(srcFile, dstFile, copyOptions);
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 1317 */     if (preserveDirDate) {
/* 1318 */       setLastModified(srcDir, destDir);
/*      */     }
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
/*      */   public static void forceDelete(File file) throws IOException {
/*      */     Counters.PathCounters deleteCounters;
/* 1338 */     Objects.requireNonNull(file, "file");
/*      */     
/*      */     try {
/* 1341 */       deleteCounters = PathUtils.delete(file.toPath(), PathUtils.EMPTY_LINK_OPTION_ARRAY, new DeleteOption[] { (DeleteOption)StandardDeleteOption.OVERRIDE_READ_ONLY });
/*      */     }
/* 1343 */     catch (IOException e) {
/* 1344 */       throw new IOException("Cannot delete file: " + file, e);
/*      */     } 
/*      */     
/* 1347 */     if (deleteCounters.getFileCounter().get() < 1L && deleteCounters.getDirectoryCounter().get() < 1L)
/*      */     {
/* 1349 */       throw new FileNotFoundException("File does not exist: " + file);
/*      */     }
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
/*      */   public static void forceDeleteOnExit(File file) throws IOException {
/* 1362 */     Objects.requireNonNull(file, "file");
/* 1363 */     if (file.isDirectory()) {
/* 1364 */       deleteDirectoryOnExit(file);
/*      */     } else {
/* 1366 */       file.deleteOnExit();
/*      */     } 
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
/*      */   public static void forceMkdir(File directory) throws IOException {
/* 1383 */     mkdirs(directory);
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
/*      */   public static void forceMkdirParent(File file) throws IOException {
/* 1396 */     Objects.requireNonNull(file, "file");
/* 1397 */     File parent = getParentFile(file);
/* 1398 */     if (parent == null) {
/*      */       return;
/*      */     }
/* 1401 */     forceMkdir(parent);
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
/*      */   public static File getFile(File directory, String... names) {
/* 1413 */     Objects.requireNonNull(directory, "directory");
/* 1414 */     Objects.requireNonNull(names, "names");
/* 1415 */     File file = directory;
/* 1416 */     for (String name : names) {
/* 1417 */       file = new File(file, name);
/*      */     }
/* 1419 */     return file;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static File getFile(String... names) {
/* 1430 */     Objects.requireNonNull(names, "names");
/* 1431 */     File file = null;
/* 1432 */     for (String name : names) {
/* 1433 */       if (file == null) {
/* 1434 */         file = new File(name);
/*      */       } else {
/* 1436 */         file = new File(file, name);
/*      */       } 
/*      */     } 
/* 1439 */     return file;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static File getParentFile(File file) {
/* 1449 */     return (file == null) ? null : file.getParentFile();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static File getTempDirectory() {
/* 1460 */     return new File(getTempDirectoryPath());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getTempDirectoryPath() {
/* 1471 */     return System.getProperty("java.io.tmpdir");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static File getUserDirectory() {
/* 1482 */     return new File(getUserDirectoryPath());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getUserDirectoryPath() {
/* 1493 */     return System.getProperty("user.home");
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
/*      */   public static boolean isDirectory(File file, LinkOption... options) {
/* 1511 */     return (file != null && Files.isDirectory(file.toPath(), options));
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
/*      */   public static boolean isEmptyDirectory(File directory) throws IOException {
/* 1525 */     return PathUtils.isEmptyDirectory(directory.toPath());
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
/*      */   public static boolean isFileNewer(File file, ChronoLocalDate chronoLocalDate) {
/* 1547 */     return isFileNewer(file, chronoLocalDate, LocalTime.now());
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
/*      */   public static boolean isFileNewer(File file, ChronoLocalDate chronoLocalDate, LocalTime localTime) {
/* 1569 */     Objects.requireNonNull(chronoLocalDate, "chronoLocalDate");
/* 1570 */     Objects.requireNonNull(localTime, "localTime");
/* 1571 */     return isFileNewer(file, chronoLocalDate.atTime(localTime));
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
/*      */   public static boolean isFileNewer(File file, ChronoLocalDateTime<?> chronoLocalDateTime) {
/* 1592 */     return isFileNewer(file, chronoLocalDateTime, ZoneId.systemDefault());
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
/*      */   public static boolean isFileNewer(File file, ChronoLocalDateTime<?> chronoLocalDateTime, ZoneId zoneId) {
/* 1609 */     Objects.requireNonNull(chronoLocalDateTime, "chronoLocalDateTime");
/* 1610 */     Objects.requireNonNull(zoneId, "zoneId");
/* 1611 */     return isFileNewer(file, chronoLocalDateTime.atZone(zoneId));
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
/*      */   public static boolean isFileNewer(File file, ChronoZonedDateTime<?> chronoZonedDateTime) {
/* 1626 */     Objects.requireNonNull(chronoZonedDateTime, "chronoZonedDateTime");
/* 1627 */     return isFileNewer(file, chronoZonedDateTime.toInstant());
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
/*      */   public static boolean isFileNewer(File file, Date date) {
/* 1640 */     Objects.requireNonNull(date, "date");
/* 1641 */     return isFileNewer(file, date.getTime());
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
/*      */   public static boolean isFileNewer(File file, File reference) {
/* 1655 */     requireExists(reference, "reference");
/* 1656 */     return isFileNewer(file, lastModifiedUnchecked(reference));
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
/*      */   public static boolean isFileNewer(File file, Instant instant) {
/* 1670 */     Objects.requireNonNull(instant, "instant");
/* 1671 */     return isFileNewer(file, instant.toEpochMilli());
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
/*      */   public static boolean isFileNewer(File file, long timeMillis) {
/* 1684 */     Objects.requireNonNull(file, "file");
/* 1685 */     return (file.exists() && lastModifiedUnchecked(file) > timeMillis);
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
/*      */   public static boolean isFileOlder(File file, ChronoLocalDate chronoLocalDate) {
/* 1709 */     return isFileOlder(file, chronoLocalDate, LocalTime.now());
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
/*      */   public static boolean isFileOlder(File file, ChronoLocalDate chronoLocalDate, LocalTime localTime) {
/* 1732 */     Objects.requireNonNull(chronoLocalDate, "chronoLocalDate");
/* 1733 */     Objects.requireNonNull(localTime, "localTime");
/* 1734 */     return isFileOlder(file, chronoLocalDate.atTime(localTime));
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
/*      */   public static boolean isFileOlder(File file, ChronoLocalDateTime<?> chronoLocalDateTime) {
/* 1756 */     return isFileOlder(file, chronoLocalDateTime, ZoneId.systemDefault());
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
/*      */   public static boolean isFileOlder(File file, ChronoLocalDateTime<?> chronoLocalDateTime, ZoneId zoneId) {
/* 1773 */     Objects.requireNonNull(chronoLocalDateTime, "chronoLocalDateTime");
/* 1774 */     Objects.requireNonNull(zoneId, "zoneId");
/* 1775 */     return isFileOlder(file, chronoLocalDateTime.atZone(zoneId));
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
/*      */   public static boolean isFileOlder(File file, ChronoZonedDateTime<?> chronoZonedDateTime) {
/* 1790 */     Objects.requireNonNull(chronoZonedDateTime, "chronoZonedDateTime");
/* 1791 */     return isFileOlder(file, chronoZonedDateTime.toInstant());
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
/*      */   public static boolean isFileOlder(File file, Date date) {
/* 1803 */     Objects.requireNonNull(date, "date");
/* 1804 */     return isFileOlder(file, date.getTime());
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
/*      */   public static boolean isFileOlder(File file, File reference) {
/* 1817 */     requireExists(reference, "reference");
/* 1818 */     return isFileOlder(file, lastModifiedUnchecked(reference));
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
/*      */   public static boolean isFileOlder(File file, Instant instant) {
/* 1831 */     Objects.requireNonNull(instant, "instant");
/* 1832 */     return isFileOlder(file, instant.toEpochMilli());
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
/*      */   public static boolean isFileOlder(File file, long timeMillis) {
/* 1845 */     Objects.requireNonNull(file, "file");
/* 1846 */     return (file.exists() && lastModifiedUnchecked(file) < timeMillis);
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
/*      */   public static boolean isRegularFile(File file, LinkOption... options) {
/* 1864 */     return (file != null && Files.isRegularFile(file.toPath(), options));
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
/*      */   public static boolean isSymlink(File file) {
/* 1879 */     return (file != null && Files.isSymbolicLink(file.toPath()));
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
/*      */   public static Iterator<File> iterateFiles(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
/* 1904 */     return listFiles(directory, fileFilter, dirFilter).iterator();
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
/*      */   public static Iterator<File> iterateFiles(File directory, String[] extensions, boolean recursive) {
/*      */     try {
/* 1925 */       return StreamIterator.iterator(streamFiles(directory, recursive, extensions));
/* 1926 */     } catch (IOException e) {
/* 1927 */       throw new UncheckedIOException(directory.toString(), e);
/*      */     } 
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
/*      */   public static Iterator<File> iterateFilesAndDirs(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
/* 1956 */     return listFilesAndDirs(directory, fileFilter, dirFilter).iterator();
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
/*      */   public static long lastModified(File file) throws IOException {
/* 1977 */     return Files.getLastModifiedTime(Objects.<Path>requireNonNull(file.toPath(), "file"), new LinkOption[0]).toMillis();
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
/*      */   public static long lastModifiedUnchecked(File file) {
/*      */     try {
/* 1999 */       return lastModified(file);
/* 2000 */     } catch (IOException e) {
/* 2001 */       throw new UncheckedIOException(file.toString(), e);
/*      */     } 
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
/*      */   public static LineIterator lineIterator(File file) throws IOException {
/* 2018 */     return lineIterator(file, null);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static LineIterator lineIterator(File file, String charsetName) throws IOException {
/* 2059 */     InputStream inputStream = null;
/*      */     try {
/* 2061 */       inputStream = openInputStream(file);
/* 2062 */       return IOUtils.lineIterator(inputStream, charsetName);
/* 2063 */     } catch (IOException|RuntimeException ex) {
/* 2064 */       IOUtils.closeQuietly(inputStream, ex::addSuppressed);
/* 2065 */       throw ex;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static AccumulatorPathVisitor listAccumulate(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) throws IOException {
/* 2071 */     boolean isDirFilterSet = (dirFilter != null);
/* 2072 */     FileEqualsFileFilter rootDirFilter = new FileEqualsFileFilter(directory);
/* 2073 */     PathFilter dirPathFilter = isDirFilterSet ? (PathFilter)rootDirFilter.or(dirFilter) : (PathFilter)rootDirFilter;
/* 2074 */     AccumulatorPathVisitor visitor = new AccumulatorPathVisitor(Counters.noopPathCounters(), (PathFilter)fileFilter, dirPathFilter);
/*      */     
/* 2076 */     Files.walkFileTree(directory.toPath(), Collections.emptySet(), toMaxDepth(isDirFilterSet), (FileVisitor<? super Path>)visitor);
/* 2077 */     return visitor;
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
/*      */   private static File[] listFiles(File directory, FileFilter fileFilter) throws IOException {
/* 2091 */     requireDirectoryExists(directory, "directory");
/* 2092 */     File[] files = (fileFilter == null) ? directory.listFiles() : directory.listFiles(fileFilter);
/* 2093 */     if (files == null)
/*      */     {
/* 2095 */       throw new IOException("Unknown I/O error listing contents of directory: " + directory);
/*      */     }
/* 2097 */     return files;
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
/*      */   
/*      */   public static Collection<File> listFiles(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
/*      */     try {
/* 2132 */       AccumulatorPathVisitor visitor = listAccumulate(directory, fileFilter, dirFilter);
/* 2133 */       return (Collection<File>)visitor.getFileList().stream().map(Path::toFile).collect(Collectors.toList());
/* 2134 */     } catch (IOException e) {
/* 2135 */       throw new UncheckedIOException(directory.toString(), e);
/*      */     } 
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
/*      */   public static Collection<File> listFiles(File directory, String[] extensions, boolean recursive) {
/*      */     try {
/* 2151 */       return toList(streamFiles(directory, recursive, extensions));
/* 2152 */     } catch (IOException e) {
/* 2153 */       throw new UncheckedIOException(directory.toString(), e);
/*      */     } 
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
/*      */   public static Collection<File> listFilesAndDirs(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
/*      */     try {
/* 2179 */       AccumulatorPathVisitor visitor = listAccumulate(directory, fileFilter, dirFilter);
/* 2180 */       List<Path> list = visitor.getFileList();
/* 2181 */       list.addAll(visitor.getDirList());
/* 2182 */       return (Collection<File>)list.stream().map(Path::toFile).collect(Collectors.toList());
/* 2183 */     } catch (IOException e) {
/* 2184 */       throw new UncheckedIOException(directory.toString(), e);
/*      */     } 
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
/*      */   private static File mkdirs(File directory) throws IOException {
/* 2199 */     if (directory != null && !directory.mkdirs() && !directory.isDirectory()) {
/* 2200 */       throw new IOException("Cannot create directory '" + directory + "'.");
/*      */     }
/* 2202 */     return directory;
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
/*      */   public static void moveDirectory(File srcDir, File destDir) throws IOException {
/* 2220 */     validateMoveParameters(srcDir, destDir);
/* 2221 */     requireDirectory(srcDir, "srcDir");
/* 2222 */     requireAbsent(destDir, "destDir");
/* 2223 */     if (!srcDir.renameTo(destDir)) {
/* 2224 */       if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath() + File.separator)) {
/* 2225 */         throw new IOException("Cannot move directory: " + srcDir + " to a subdirectory of itself: " + destDir);
/*      */       }
/* 2227 */       copyDirectory(srcDir, destDir);
/* 2228 */       deleteDirectory(srcDir);
/* 2229 */       if (srcDir.exists()) {
/* 2230 */         throw new IOException("Failed to delete original directory '" + srcDir + "' after copy to '" + destDir + "'");
/*      */       }
/*      */     } 
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
/*      */   public static void moveDirectoryToDirectory(File src, File destDir, boolean createDestDir) throws IOException {
/* 2251 */     validateMoveParameters(src, destDir);
/* 2252 */     if (!destDir.isDirectory()) {
/* 2253 */       if (destDir.exists()) {
/* 2254 */         throw new IOException("Destination '" + destDir + "' is not a directory");
/*      */       }
/* 2256 */       if (!createDestDir) {
/* 2257 */         throw new FileNotFoundException("Destination directory '" + destDir + "' does not exist [createDestDir=" + Character.MIN_VALUE + "]");
/*      */       }
/*      */       
/* 2260 */       mkdirs(destDir);
/*      */     } 
/* 2262 */     moveDirectory(src, new File(destDir, src.getName()));
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
/*      */   public static void moveFile(File srcFile, File destFile) throws IOException {
/* 2283 */     moveFile(srcFile, destFile, new CopyOption[] { StandardCopyOption.COPY_ATTRIBUTES });
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
/*      */   public static void moveFile(File srcFile, File destFile, CopyOption... copyOptions) throws IOException {
/* 2303 */     validateMoveParameters(srcFile, destFile);
/* 2304 */     requireFile(srcFile, "srcFile");
/* 2305 */     requireAbsent(destFile, null);
/* 2306 */     boolean rename = srcFile.renameTo(destFile);
/* 2307 */     if (!rename) {
/* 2308 */       copyFile(srcFile, destFile, copyOptions);
/* 2309 */       if (!srcFile.delete()) {
/* 2310 */         deleteQuietly(destFile);
/* 2311 */         throw new IOException("Failed to delete original file '" + srcFile + "' after copy to '" + destFile + "'");
/*      */       } 
/*      */     } 
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
/*      */   public static void moveFileToDirectory(File srcFile, File destDir, boolean createDestDir) throws IOException {
/* 2332 */     validateMoveParameters(srcFile, destDir);
/* 2333 */     if (!destDir.exists() && createDestDir) {
/* 2334 */       mkdirs(destDir);
/*      */     }
/* 2336 */     requireExistsChecked(destDir, "destDir");
/* 2337 */     requireDirectory(destDir, "destDir");
/* 2338 */     moveFile(srcFile, new File(destDir, srcFile.getName()));
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
/*      */   public static void moveToDirectory(File src, File destDir, boolean createDestDir) throws IOException {
/* 2359 */     validateMoveParameters(src, destDir);
/* 2360 */     if (src.isDirectory()) {
/* 2361 */       moveDirectoryToDirectory(src, destDir, createDestDir);
/*      */     } else {
/* 2363 */       moveFileToDirectory(src, destDir, createDestDir);
/*      */     } 
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
/*      */   public static FileInputStream openInputStream(File file) throws IOException {
/* 2387 */     Objects.requireNonNull(file, "file");
/* 2388 */     return new FileInputStream(file);
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
/*      */   public static FileOutputStream openOutputStream(File file) throws IOException {
/* 2415 */     return openOutputStream(file, false);
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
/*      */   public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
/* 2444 */     Objects.requireNonNull(file, "file");
/* 2445 */     if (file.exists()) {
/* 2446 */       requireFile(file, "file");
/* 2447 */       requireCanWrite(file, "file");
/*      */     } else {
/* 2449 */       createParentDirectories(file);
/*      */     } 
/* 2451 */     return new FileOutputStream(file, append);
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
/*      */   public static byte[] readFileToByteArray(File file) throws IOException {
/* 2467 */     try (InputStream inputStream = openInputStream(file)) {
/* 2468 */       long fileLength = file.length();
/*      */       
/* 2470 */       return (fileLength > 0L) ? IOUtils.toByteArray(inputStream, fileLength) : IOUtils.toByteArray(inputStream);
/*      */     } 
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
/*      */   @Deprecated
/*      */   public static String readFileToString(File file) throws IOException {
/* 2489 */     return readFileToString(file, Charset.defaultCharset());
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
/*      */   public static String readFileToString(File file, Charset charsetName) throws IOException {
/* 2506 */     try (InputStream inputStream = openInputStream(file)) {
/* 2507 */       return IOUtils.toString(inputStream, Charsets.toCharset(charsetName));
/*      */     } 
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
/*      */   public static String readFileToString(File file, String charsetName) throws IOException {
/* 2526 */     return readFileToString(file, Charsets.toCharset(charsetName));
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
/*      */   @Deprecated
/*      */   public static List<String> readLines(File file) throws IOException {
/* 2544 */     return readLines(file, Charset.defaultCharset());
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
/*      */   public static List<String> readLines(File file, Charset charset) throws IOException {
/* 2561 */     try (InputStream inputStream = openInputStream(file)) {
/* 2562 */       return IOUtils.readLines(inputStream, Charsets.toCharset(charset));
/*      */     } 
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
/*      */   public static List<String> readLines(File file, String charsetName) throws IOException {
/* 2581 */     return readLines(file, Charsets.toCharset(charsetName));
/*      */   }
/*      */   
/*      */   private static void requireAbsent(File file, String name) throws FileExistsException {
/* 2585 */     if (file.exists()) {
/* 2586 */       throw new FileExistsException(
/* 2587 */           String.format("File element in parameter '%s' already exists: '%s'", new Object[] { name, file }));
/*      */     }
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
/*      */   private static void requireCanonicalPathsNotEquals(File file1, File file2) throws IOException {
/* 2600 */     String canonicalPath = file1.getCanonicalPath();
/* 2601 */     if (canonicalPath.equals(file2.getCanonicalPath())) {
/* 2602 */       throw new IllegalArgumentException(
/* 2603 */           String.format("File canonical paths are equal: '%s' (file1='%s', file2='%s')", new Object[] { canonicalPath, file1, file2 }));
/*      */     }
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
/*      */   private static void requireCanWrite(File file, String name) {
/* 2617 */     Objects.requireNonNull(file, "file");
/* 2618 */     if (!file.canWrite()) {
/* 2619 */       throw new IllegalArgumentException("File parameter '" + name + " is not writable: '" + file + "'");
/*      */     }
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
/*      */   private static File requireDirectory(File directory, String name) {
/* 2633 */     Objects.requireNonNull(directory, name);
/* 2634 */     if (!directory.isDirectory()) {
/* 2635 */       throw new IllegalArgumentException("Parameter '" + name + "' is not a directory: '" + directory + "'");
/*      */     }
/* 2637 */     return directory;
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
/*      */   private static File requireDirectoryExists(File directory, String name) {
/* 2650 */     requireExists(directory, name);
/* 2651 */     requireDirectory(directory, name);
/* 2652 */     return directory;
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
/*      */   private static File requireDirectoryIfExists(File directory, String name) {
/* 2665 */     Objects.requireNonNull(directory, name);
/* 2666 */     if (directory.exists()) {
/* 2667 */       requireDirectory(directory, name);
/*      */     }
/* 2669 */     return directory;
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
/*      */   private static void requireEqualSizes(File srcFile, File destFile, long srcLen, long dstLen) throws IOException {
/* 2683 */     if (srcLen != dstLen) {
/* 2684 */       throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "' Expected length: " + srcLen + " Actual: " + dstLen);
/*      */     }
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
/*      */   private static File requireExists(File file, String fileParamName) {
/* 2699 */     Objects.requireNonNull(file, fileParamName);
/* 2700 */     if (!file.exists()) {
/* 2701 */       throw new IllegalArgumentException("File system element for parameter '" + fileParamName + "' does not exist: '" + file + "'");
/*      */     }
/*      */     
/* 2704 */     return file;
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
/*      */   private static File requireExistsChecked(File file, String fileParamName) throws FileNotFoundException {
/* 2717 */     Objects.requireNonNull(file, fileParamName);
/* 2718 */     if (!file.exists()) {
/* 2719 */       throw new FileNotFoundException("File system element for parameter '" + fileParamName + "' does not exist: '" + file + "'");
/*      */     }
/*      */     
/* 2722 */     return file;
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
/*      */   private static File requireFile(File file, String name) {
/* 2735 */     Objects.requireNonNull(file, name);
/* 2736 */     if (!file.isFile()) {
/* 2737 */       throw new IllegalArgumentException("Parameter '" + name + "' is not a file: " + file);
/*      */     }
/* 2739 */     return file;
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
/*      */   private static void requireFileCopy(File source, File destination) throws FileNotFoundException {
/* 2751 */     requireExistsChecked(source, "source");
/* 2752 */     Objects.requireNonNull(destination, "destination");
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
/*      */   private static File requireFileIfExists(File file, String name) {
/* 2765 */     Objects.requireNonNull(file, name);
/* 2766 */     return file.exists() ? requireFile(file, name) : file;
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
/*      */   private static void setLastModified(File sourceFile, File targetFile) throws IOException {
/* 2779 */     Objects.requireNonNull(sourceFile, "sourceFile");
/* 2780 */     setLastModified(targetFile, lastModified(sourceFile));
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
/*      */   private static void setLastModified(File file, long timeMillis) throws IOException {
/* 2792 */     Objects.requireNonNull(file, "file");
/* 2793 */     if (!file.setLastModified(timeMillis)) {
/* 2794 */       throw new IOException(String.format("Failed setLastModified(%s) on '%s'", new Object[] { Long.valueOf(timeMillis), file }));
/*      */     }
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
/*      */   public static long sizeOf(File file) {
/* 2822 */     requireExists(file, "file");
/* 2823 */     return file.isDirectory() ? sizeOfDirectory0(file) : file.length();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static long sizeOf0(File file) {
/* 2834 */     Objects.requireNonNull(file, "file");
/* 2835 */     if (file.isDirectory()) {
/* 2836 */       return sizeOfDirectory0(file);
/*      */     }
/* 2838 */     return file.length();
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
/*      */   public static BigInteger sizeOfAsBigInteger(File file) {
/* 2860 */     requireExists(file, "file");
/* 2861 */     return file.isDirectory() ? sizeOfDirectoryBig0(file) : BigInteger.valueOf(file.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static BigInteger sizeOfBig0(File file) {
/* 2871 */     Objects.requireNonNull(file, "fileOrDir");
/* 2872 */     return file.isDirectory() ? sizeOfDirectoryBig0(file) : BigInteger.valueOf(file.length());
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
/*      */   public static long sizeOfDirectory(File directory) {
/* 2889 */     return sizeOfDirectory0(requireDirectoryExists(directory, "directory"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static long sizeOfDirectory0(File directory) {
/* 2900 */     Objects.requireNonNull(directory, "directory");
/* 2901 */     File[] files = directory.listFiles();
/* 2902 */     if (files == null) {
/* 2903 */       return 0L;
/*      */     }
/* 2905 */     long size = 0L;
/*      */     
/* 2907 */     for (File file : files) {
/* 2908 */       if (!isSymlink(file)) {
/* 2909 */         size += sizeOf0(file);
/* 2910 */         if (size < 0L) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 2916 */     return size;
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
/*      */   public static BigInteger sizeOfDirectoryAsBigInteger(File directory) {
/* 2928 */     return sizeOfDirectoryBig0(requireDirectoryExists(directory, "directory"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static BigInteger sizeOfDirectoryBig0(File directory) {
/* 2938 */     Objects.requireNonNull(directory, "directory");
/* 2939 */     File[] files = directory.listFiles();
/* 2940 */     if (files == null)
/*      */     {
/* 2942 */       return BigInteger.ZERO;
/*      */     }
/* 2944 */     BigInteger size = BigInteger.ZERO;
/*      */     
/* 2946 */     for (File file : files) {
/* 2947 */       if (!isSymlink(file)) {
/* 2948 */         size = size.add(sizeOfBig0(file));
/*      */       }
/*      */     } 
/*      */     
/* 2952 */     return size;
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
/*      */   public static Stream<File> streamFiles(File directory, boolean recursive, String... extensions) throws IOException {
/* 2970 */     IOFileFilter filter = (extensions == null) ? FileFileFilter.INSTANCE : FileFileFilter.INSTANCE.and((IOFileFilter)new SuffixFileFilter(toSuffixes(extensions)));
/* 2971 */     return PathUtils.walk(directory.toPath(), (PathFilter)filter, toMaxDepth(recursive), false, new FileVisitOption[] { FileVisitOption.FOLLOW_LINKS
/* 2972 */         }).map(Path::toFile);
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
/*      */   public static File toFile(URL url) {
/* 2991 */     if (url == null || !"file".equalsIgnoreCase(url.getProtocol())) {
/* 2992 */       return null;
/*      */     }
/* 2994 */     String filename = url.getFile().replace('/', File.separatorChar);
/* 2995 */     return new File(decodeUrl(filename));
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
/*      */   public static File[] toFiles(URL... urls) {
/* 3020 */     if (IOUtils.length((Object[])urls) == 0) {
/* 3021 */       return EMPTY_FILE_ARRAY;
/*      */     }
/* 3023 */     File[] files = new File[urls.length];
/* 3024 */     for (int i = 0; i < urls.length; i++) {
/* 3025 */       URL url = urls[i];
/* 3026 */       if (url != null) {
/* 3027 */         if (!"file".equalsIgnoreCase(url.getProtocol())) {
/* 3028 */           throw new IllegalArgumentException("Can only convert file URL to a File: " + url);
/*      */         }
/* 3030 */         files[i] = toFile(url);
/*      */       } 
/*      */     } 
/* 3033 */     return files;
/*      */   }
/*      */   
/*      */   private static List<File> toList(Stream<File> stream) {
/* 3037 */     return stream.collect((Collector)Collectors.toList());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int toMaxDepth(boolean recursive) {
/* 3047 */     return recursive ? Integer.MAX_VALUE : 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String[] toSuffixes(String... extensions) {
/* 3058 */     Objects.requireNonNull(extensions, "extensions");
/* 3059 */     String[] suffixes = new String[extensions.length];
/* 3060 */     for (int i = 0; i < extensions.length; i++) {
/* 3061 */       suffixes[i] = "." + extensions[i];
/*      */     }
/* 3063 */     return suffixes;
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
/*      */   public static void touch(File file) throws IOException {
/* 3081 */     Objects.requireNonNull(file, "file");
/* 3082 */     if (!file.exists()) {
/* 3083 */       openOutputStream(file).close();
/*      */     }
/* 3085 */     setLastModified(file, System.currentTimeMillis());
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
/*      */   public static URL[] toURLs(File... files) throws IOException {
/* 3100 */     Objects.requireNonNull(files, "files");
/* 3101 */     URL[] urls = new URL[files.length];
/* 3102 */     for (int i = 0; i < urls.length; i++) {
/* 3103 */       urls[i] = files[i].toURI().toURL();
/*      */     }
/* 3105 */     return urls;
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
/*      */   private static void validateMoveParameters(File source, File destination) throws FileNotFoundException {
/* 3121 */     Objects.requireNonNull(source, "source");
/* 3122 */     Objects.requireNonNull(destination, "destination");
/* 3123 */     if (!source.exists()) {
/* 3124 */       throw new FileNotFoundException("Source '" + source + "' does not exist");
/*      */     }
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
/*      */   public static boolean waitFor(File file, int seconds) {
/* 3141 */     Objects.requireNonNull(file, "file");
/* 3142 */     long finishAtMillis = System.currentTimeMillis() + seconds * 1000L;
/* 3143 */     boolean wasInterrupted = false;
/*      */     try {
/* 3145 */       while (!file.exists()) {
/* 3146 */         long remainingMillis = finishAtMillis - System.currentTimeMillis();
/* 3147 */         if (remainingMillis < 0L) {
/* 3148 */           return false;
/*      */         }
/*      */         try {
/* 3151 */           Thread.sleep(Math.min(100L, remainingMillis));
/* 3152 */         } catch (InterruptedException ignore) {
/* 3153 */           wasInterrupted = true;
/* 3154 */         } catch (Exception ex) {
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } finally {
/* 3159 */       if (wasInterrupted) {
/* 3160 */         Thread.currentThread().interrupt();
/*      */       }
/*      */     } 
/* 3163 */     return true;
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
/*      */   @Deprecated
/*      */   public static void write(File file, CharSequence data) throws IOException {
/* 3177 */     write(file, data, Charset.defaultCharset(), false);
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
/*      */   @Deprecated
/*      */   public static void write(File file, CharSequence data, boolean append) throws IOException {
/* 3193 */     write(file, data, Charset.defaultCharset(), append);
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
/*      */   public static void write(File file, CharSequence data, Charset charset) throws IOException {
/* 3206 */     write(file, data, charset, false);
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
/*      */   public static void write(File file, CharSequence data, Charset charset, boolean append) throws IOException {
/* 3222 */     writeStringToFile(file, Objects.toString(data, null), charset, append);
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
/*      */   public static void write(File file, CharSequence data, String charsetName) throws IOException {
/* 3238 */     write(file, data, charsetName, false);
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
/*      */   public static void write(File file, CharSequence data, String charsetName, boolean append) throws IOException {
/* 3256 */     write(file, data, Charsets.toCharset(charsetName), append);
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
/*      */   public static void writeByteArrayToFile(File file, byte[] data) throws IOException {
/* 3272 */     writeByteArrayToFile(file, data, false);
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
/*      */   public static void writeByteArrayToFile(File file, byte[] data, boolean append) throws IOException {
/* 3289 */     writeByteArrayToFile(file, data, 0, data.length, append);
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
/*      */   public static void writeByteArrayToFile(File file, byte[] data, int off, int len) throws IOException {
/* 3306 */     writeByteArrayToFile(file, data, off, len, false);
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
/*      */   public static void writeByteArrayToFile(File file, byte[] data, int off, int len, boolean append) throws IOException {
/* 3325 */     try (OutputStream out = openOutputStream(file, append)) {
/* 3326 */       out.write(data, off, len);
/*      */     } 
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
/*      */   public static void writeLines(File file, Collection<?> lines) throws IOException {
/* 3341 */     writeLines(file, null, lines, null, false);
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
/*      */   public static void writeLines(File file, Collection<?> lines, boolean append) throws IOException {
/* 3357 */     writeLines(file, null, lines, null, append);
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
/*      */   public static void writeLines(File file, Collection<?> lines, String lineEnding) throws IOException {
/* 3373 */     writeLines(file, null, lines, lineEnding, false);
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
/*      */   public static void writeLines(File file, Collection<?> lines, String lineEnding, boolean append) throws IOException {
/* 3392 */     writeLines(file, null, lines, lineEnding, append);
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
/*      */   public static void writeLines(File file, String charsetName, Collection<?> lines) throws IOException {
/* 3413 */     writeLines(file, charsetName, lines, null, false);
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
/*      */   public static void writeLines(File file, String charsetName, Collection<?> lines, boolean append) throws IOException {
/* 3432 */     writeLines(file, charsetName, lines, null, append);
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
/*      */   public static void writeLines(File file, String charsetName, Collection<?> lines, String lineEnding) throws IOException {
/* 3454 */     writeLines(file, charsetName, lines, lineEnding, false);
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
/*      */   public static void writeLines(File file, String charsetName, Collection<?> lines, String lineEnding, boolean append) throws IOException {
/* 3474 */     try (OutputStream out = new BufferedOutputStream(openOutputStream(file, append))) {
/* 3475 */       IOUtils.writeLines(lines, lineEnding, out, charsetName);
/*      */     } 
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
/*      */   @Deprecated
/*      */   public static void writeStringToFile(File file, String data) throws IOException {
/* 3489 */     writeStringToFile(file, data, Charset.defaultCharset(), false);
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
/*      */   @Deprecated
/*      */   public static void writeStringToFile(File file, String data, boolean append) throws IOException {
/* 3505 */     writeStringToFile(file, data, Charset.defaultCharset(), append);
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
/*      */   public static void writeStringToFile(File file, String data, Charset charset) throws IOException {
/* 3524 */     writeStringToFile(file, data, charset, false);
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
/*      */   public static void writeStringToFile(File file, String data, Charset charset, boolean append) throws IOException {
/* 3540 */     try (OutputStream out = openOutputStream(file, append)) {
/* 3541 */       IOUtils.write(data, out, charset);
/*      */     } 
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
/*      */   public static void writeStringToFile(File file, String data, String charsetName) throws IOException {
/* 3559 */     writeStringToFile(file, data, charsetName, false);
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
/*      */   public static void writeStringToFile(File file, String data, String charsetName, boolean append) throws IOException {
/* 3577 */     writeStringToFile(file, data, Charsets.toCharset(charsetName), append);
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\io\FileUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */