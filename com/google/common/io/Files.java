/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.graph.SuccessorsFunction;
/*     */ import com.google.common.graph.Traverser;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.MappedByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ public final class Files
/*     */ {
/*     */   private static final int TEMP_DIR_ATTEMPTS = 10000;
/*     */   
/*     */   @Beta
/*     */   public static BufferedReader newReader(File file, Charset charset) throws FileNotFoundException {
/*  87 */     Preconditions.checkNotNull(file);
/*  88 */     Preconditions.checkNotNull(charset);
/*  89 */     return new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
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
/*     */   @Beta
/*     */   public static BufferedWriter newWriter(File file, Charset charset) throws FileNotFoundException {
/* 106 */     Preconditions.checkNotNull(file);
/* 107 */     Preconditions.checkNotNull(charset);
/* 108 */     return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteSource asByteSource(File file) {
/* 117 */     return new FileByteSource(file);
/*     */   }
/*     */   
/*     */   private static final class FileByteSource
/*     */     extends ByteSource {
/*     */     private final File file;
/*     */     
/*     */     private FileByteSource(File file) {
/* 125 */       this.file = (File)Preconditions.checkNotNull(file);
/*     */     }
/*     */ 
/*     */     
/*     */     public FileInputStream openStream() throws IOException {
/* 130 */       return new FileInputStream(this.file);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Long> sizeIfKnown() {
/* 135 */       if (this.file.isFile()) {
/* 136 */         return Optional.of(Long.valueOf(this.file.length()));
/*     */       }
/* 138 */       return Optional.absent();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public long size() throws IOException {
/* 144 */       if (!this.file.isFile()) {
/* 145 */         throw new FileNotFoundException(this.file.toString());
/*     */       }
/* 147 */       return this.file.length();
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] read() throws IOException {
/* 152 */       Closer closer = Closer.create();
/*     */       try {
/* 154 */         FileInputStream in = closer.<FileInputStream>register(openStream());
/* 155 */         return ByteStreams.toByteArray(in, in.getChannel().size());
/* 156 */       } catch (Throwable e) {
/* 157 */         throw closer.rethrow(e);
/*     */       } finally {
/* 159 */         closer.close();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 165 */       String str = String.valueOf(this.file); return (new StringBuilder(20 + String.valueOf(str).length())).append("Files.asByteSource(").append(str).append(")").toString();
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
/*     */   public static ByteSink asByteSink(File file, FileWriteMode... modes) {
/* 178 */     return new FileByteSink(file, modes);
/*     */   }
/*     */   
/*     */   private static final class FileByteSink
/*     */     extends ByteSink {
/*     */     private final File file;
/*     */     private final ImmutableSet<FileWriteMode> modes;
/*     */     
/*     */     private FileByteSink(File file, FileWriteMode... modes) {
/* 187 */       this.file = (File)Preconditions.checkNotNull(file);
/* 188 */       this.modes = ImmutableSet.copyOf((Object[])modes);
/*     */     }
/*     */ 
/*     */     
/*     */     public FileOutputStream openStream() throws IOException {
/* 193 */       return new FileOutputStream(this.file, this.modes.contains(FileWriteMode.APPEND));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 198 */       String str1 = String.valueOf(this.file), str2 = String.valueOf(this.modes); return (new StringBuilder(20 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("Files.asByteSink(").append(str1).append(", ").append(str2).append(")").toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSource asCharSource(File file, Charset charset) {
/* 209 */     return asByteSource(file).asCharSource(charset);
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
/*     */   public static CharSink asCharSink(File file, Charset charset, FileWriteMode... modes) {
/* 221 */     return asByteSink(file, modes).asCharSink(charset);
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
/*     */   @Beta
/*     */   public static byte[] toByteArray(File file) throws IOException {
/* 237 */     return asByteSource(file).read();
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
/*     */   @Deprecated
/*     */   @Beta
/*     */   public static String toString(File file, Charset charset) throws IOException {
/* 254 */     return asCharSource(file, charset).read();
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
/*     */   @Beta
/*     */   public static void write(byte[] from, File to) throws IOException {
/* 269 */     asByteSink(to, new FileWriteMode[0]).write(from);
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
/*     */   @Deprecated
/*     */   @Beta
/*     */   public static void write(CharSequence from, File to, Charset charset) throws IOException {
/* 286 */     asCharSink(to, charset, new FileWriteMode[0]).write(from);
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
/*     */   @Beta
/*     */   public static void copy(File from, OutputStream to) throws IOException {
/* 301 */     asByteSource(from).copyTo(to);
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
/*     */   @Beta
/*     */   public static void copy(File from, File to) throws IOException {
/* 325 */     Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);
/* 326 */     asByteSource(from).copyTo(asByteSink(to, new FileWriteMode[0]));
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
/*     */   @Deprecated
/*     */   @Beta
/*     */   public static void copy(File from, Charset charset, Appendable to) throws IOException {
/* 344 */     asCharSource(from, charset).copyTo(to);
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
/*     */   @Deprecated
/*     */   @Beta
/*     */   public static void append(CharSequence from, File to, Charset charset) throws IOException {
/* 362 */     asCharSink(to, charset, new FileWriteMode[] { FileWriteMode.APPEND }).write(from);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static boolean equal(File file1, File file2) throws IOException {
/* 372 */     Preconditions.checkNotNull(file1);
/* 373 */     Preconditions.checkNotNull(file2);
/* 374 */     if (file1 == file2 || file1.equals(file2)) {
/* 375 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 383 */     long len1 = file1.length();
/* 384 */     long len2 = file2.length();
/* 385 */     if (len1 != 0L && len2 != 0L && len1 != len2) {
/* 386 */       return false;
/*     */     }
/* 388 */     return asByteSource(file1).contentEquals(asByteSource(file2));
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
/*     */   @Deprecated
/*     */   @Beta
/*     */   public static File createTempDir() {
/* 424 */     File baseDir = new File(System.getProperty("java.io.tmpdir"));
/*     */     
/* 426 */     long l = System.currentTimeMillis(); String baseName = (new StringBuilder(21)).append(l).append("-").toString();
/*     */     
/* 428 */     for (int counter = 0; counter < 10000; counter++) {
/* 429 */       int i = counter; File tempDir = new File(baseDir, (new StringBuilder(11 + String.valueOf(baseName).length())).append(baseName).append(i).toString());
/* 430 */       if (tempDir.mkdir()) {
/* 431 */         return tempDir;
/*     */       }
/*     */     } 
/* 434 */     throw new IllegalStateException((new StringBuilder(66 + String.valueOf(baseName).length() + String.valueOf(baseName).length())).append("Failed to create directory within 10000 attempts (tried ").append(baseName).append("0 to ").append(baseName).append(9999).append(')').toString());
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
/*     */   @Beta
/*     */   public static void touch(File file) throws IOException {
/* 455 */     Preconditions.checkNotNull(file);
/* 456 */     if (!file.createNewFile() && !file.setLastModified(System.currentTimeMillis())) {
/* 457 */       String str = String.valueOf(file); throw new IOException((new StringBuilder(38 + String.valueOf(str).length())).append("Unable to update modification time of ").append(str).toString());
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
/*     */   @Beta
/*     */   public static void createParentDirs(File file) throws IOException {
/* 472 */     Preconditions.checkNotNull(file);
/* 473 */     File parent = file.getCanonicalFile().getParentFile();
/* 474 */     if (parent == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 483 */     parent.mkdirs();
/* 484 */     if (!parent.isDirectory()) {
/* 485 */       String str = String.valueOf(file); throw new IOException((new StringBuilder(39 + String.valueOf(str).length())).append("Unable to create parent directories of ").append(str).toString());
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
/*     */   @Beta
/*     */   public static void move(File from, File to) throws IOException {
/* 503 */     Preconditions.checkNotNull(from);
/* 504 */     Preconditions.checkNotNull(to);
/* 505 */     Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);
/*     */     
/* 507 */     if (!from.renameTo(to)) {
/* 508 */       copy(from, to);
/* 509 */       if (!from.delete()) {
/* 510 */         if (!to.delete()) {
/* 511 */           String str1 = String.valueOf(to); throw new IOException((new StringBuilder(17 + String.valueOf(str1).length())).append("Unable to delete ").append(str1).toString());
/*     */         } 
/* 513 */         String str = String.valueOf(from); throw new IOException((new StringBuilder(17 + String.valueOf(str).length())).append("Unable to delete ").append(str).toString());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @Beta
/*     */   public static String readFirstLine(File file, Charset charset) throws IOException {
/* 534 */     return asCharSource(file, charset).readFirstLine();
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
/*     */   @Beta
/*     */   public static List<String> readLines(File file, Charset charset) throws IOException {
/* 557 */     return asCharSource(file, charset)
/* 558 */       .<List<String>>readLines(new LineProcessor<List<String>>()
/*     */         {
/* 560 */           final List<String> result = Lists.newArrayList();
/*     */ 
/*     */           
/*     */           public boolean processLine(String line) {
/* 564 */             this.result.add(line);
/* 565 */             return true;
/*     */           }
/*     */ 
/*     */           
/*     */           public List<String> getResult() {
/* 570 */             return this.result;
/*     */           }
/*     */         });
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
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T readLines(File file, Charset charset, LineProcessor<T> callback) throws IOException {
/* 593 */     return asCharSource(file, charset).readLines(callback);
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
/*     */   @Deprecated
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T readBytes(File file, ByteProcessor<T> processor) throws IOException {
/* 613 */     return asByteSource(file).read(processor);
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
/*     */   @Deprecated
/*     */   @Beta
/*     */   public static HashCode hash(File file, HashFunction hashFunction) throws IOException {
/* 631 */     return asByteSource(file).hash(hashFunction);
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
/*     */   @Beta
/*     */   public static MappedByteBuffer map(File file) throws IOException {
/* 651 */     Preconditions.checkNotNull(file);
/* 652 */     return map(file, FileChannel.MapMode.READ_ONLY);
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
/*     */   @Beta
/*     */   public static MappedByteBuffer map(File file, FileChannel.MapMode mode) throws IOException {
/* 674 */     return mapInternal(file, mode, -1L);
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
/*     */   @Beta
/*     */   public static MappedByteBuffer map(File file, FileChannel.MapMode mode, long size) throws IOException {
/* 698 */     Preconditions.checkArgument((size >= 0L), "size (%s) may not be negative", size);
/* 699 */     return mapInternal(file, mode, size);
/*     */   }
/*     */ 
/*     */   
/*     */   private static MappedByteBuffer mapInternal(File file, FileChannel.MapMode mode, long size) throws IOException {
/* 704 */     Preconditions.checkNotNull(file);
/* 705 */     Preconditions.checkNotNull(mode);
/*     */     
/* 707 */     Closer closer = Closer.create();
/*     */     
/*     */     try {
/* 710 */       RandomAccessFile raf = closer.<RandomAccessFile>register(new RandomAccessFile(file, (mode == FileChannel.MapMode.READ_ONLY) ? "r" : "rw"));
/* 711 */       FileChannel channel = closer.<FileChannel>register(raf.getChannel());
/* 712 */       return channel.map(mode, 0L, (size == -1L) ? channel.size() : size);
/* 713 */     } catch (Throwable e) {
/* 714 */       throw closer.rethrow(e);
/*     */     } finally {
/* 716 */       closer.close();
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
/*     */   @Beta
/*     */   public static String simplifyPath(String pathname) {
/* 742 */     Preconditions.checkNotNull(pathname);
/* 743 */     if (pathname.length() == 0) {
/* 744 */       return ".";
/*     */     }
/*     */ 
/*     */     
/* 748 */     Iterable<String> components = Splitter.on('/').omitEmptyStrings().split(pathname);
/* 749 */     List<String> path = new ArrayList<>();
/*     */ 
/*     */     
/* 752 */     for (String component : components) {
/* 753 */       switch (component) {
/*     */         case ".":
/*     */           continue;
/*     */         case "..":
/* 757 */           if (path.size() > 0 && !((String)path.get(path.size() - 1)).equals("..")) {
/* 758 */             path.remove(path.size() - 1); continue;
/*     */           } 
/* 760 */           path.add("..");
/*     */           continue;
/*     */       } 
/*     */       
/* 764 */       path.add(component);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 770 */     String result = Joiner.on('/').join(path);
/* 771 */     if (pathname.charAt(0) == '/') {
/* 772 */       String.valueOf(result); result = (String.valueOf(result).length() != 0) ? "/".concat(String.valueOf(result)) : new String("/");
/*     */     } 
/*     */     
/* 775 */     while (result.startsWith("/../")) {
/* 776 */       result = result.substring(3);
/*     */     }
/* 778 */     if (result.equals("/..")) {
/* 779 */       result = "/";
/* 780 */     } else if ("".equals(result)) {
/* 781 */       result = ".";
/*     */     } 
/*     */     
/* 784 */     return result;
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
/*     */   @Beta
/*     */   public static String getFileExtension(String fullName) {
/* 803 */     Preconditions.checkNotNull(fullName);
/* 804 */     String fileName = (new File(fullName)).getName();
/* 805 */     int dotIndex = fileName.lastIndexOf('.');
/* 806 */     return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
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
/*     */   @Beta
/*     */   public static String getNameWithoutExtension(String file) {
/* 821 */     Preconditions.checkNotNull(file);
/* 822 */     String fileName = (new File(file)).getName();
/* 823 */     int dotIndex = fileName.lastIndexOf('.');
/* 824 */     return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
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
/*     */   @Beta
/*     */   public static Traverser<File> fileTraverser() {
/* 851 */     return Traverser.forTree(FILE_TREE);
/*     */   }
/*     */   
/* 854 */   private static final SuccessorsFunction<File> FILE_TREE = new SuccessorsFunction<File>()
/*     */     {
/*     */       
/*     */       public Iterable<File> successors(File file)
/*     */       {
/* 859 */         if (file.isDirectory()) {
/* 860 */           File[] files = file.listFiles();
/* 861 */           if (files != null) {
/* 862 */             return Collections.unmodifiableList(Arrays.asList(files));
/*     */           }
/*     */         } 
/*     */         
/* 866 */         return (Iterable<File>)ImmutableList.of();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static Predicate<File> isDirectory() {
/* 877 */     return FilePredicate.IS_DIRECTORY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static Predicate<File> isFile() {
/* 887 */     return FilePredicate.IS_FILE;
/*     */   }
/*     */   
/*     */   private enum FilePredicate implements Predicate<File> {
/* 891 */     IS_DIRECTORY
/*     */     {
/*     */       public boolean apply(File file) {
/* 894 */         return file.isDirectory();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 899 */         return "Files.isDirectory()";
/*     */       }
/*     */     },
/*     */     
/* 903 */     IS_FILE
/*     */     {
/*     */       public boolean apply(File file) {
/* 906 */         return file.isFile();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 911 */         return "Files.isFile()";
/*     */       }
/*     */     };
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\io\Files.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */