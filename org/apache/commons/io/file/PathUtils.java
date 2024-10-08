/*      */ package org.apache.commons.io.file;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.UncheckedIOException;
/*      */ import java.net.URI;
/*      */ import java.net.URL;
/*      */ import java.nio.file.CopyOption;
/*      */ import java.nio.file.DirectoryStream;
/*      */ import java.nio.file.FileVisitOption;
/*      */ import java.nio.file.FileVisitResult;
/*      */ import java.nio.file.FileVisitor;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.LinkOption;
/*      */ import java.nio.file.NoSuchFileException;
/*      */ import java.nio.file.OpenOption;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.Paths;
/*      */ import java.nio.file.attribute.AclEntry;
/*      */ import java.nio.file.attribute.AclFileAttributeView;
/*      */ import java.nio.file.attribute.BasicFileAttributes;
/*      */ import java.nio.file.attribute.DosFileAttributeView;
/*      */ import java.nio.file.attribute.FileAttribute;
/*      */ import java.nio.file.attribute.PosixFileAttributeView;
/*      */ import java.nio.file.attribute.PosixFileAttributes;
/*      */ import java.nio.file.attribute.PosixFilePermission;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.stream.Collector;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import org.apache.commons.io.IOExceptionList;
/*      */ import org.apache.commons.io.IOUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class PathUtils
/*      */ {
/*      */   private static class RelativeSortedPaths
/*      */   {
/*      */     final boolean equals;
/*      */     final List<Path> relativeFileList1;
/*      */     final List<Path> relativeFileList2;
/*      */     
/*      */     private RelativeSortedPaths(Path dir1, Path dir2, int maxDepth, LinkOption[] linkOptions, FileVisitOption[] fileVisitOptions) throws IOException {
/*   97 */       List<Path> tmpRelativeFileList1 = null;
/*   98 */       List<Path> tmpRelativeFileList2 = null;
/*   99 */       if (dir1 == null && dir2 == null) {
/*  100 */         this.equals = true;
/*  101 */       } else if ((((dir1 == null) ? 1 : 0) ^ ((dir2 == null) ? 1 : 0)) != 0) {
/*  102 */         this.equals = false;
/*      */       } else {
/*  104 */         boolean parentDirNotExists1 = Files.notExists(dir1, linkOptions);
/*  105 */         boolean parentDirNotExists2 = Files.notExists(dir2, linkOptions);
/*  106 */         if (parentDirNotExists1 || parentDirNotExists2) {
/*  107 */           this.equals = (parentDirNotExists1 && parentDirNotExists2);
/*      */         } else {
/*  109 */           AccumulatorPathVisitor visitor1 = PathUtils.accumulate(dir1, maxDepth, fileVisitOptions);
/*  110 */           AccumulatorPathVisitor visitor2 = PathUtils.accumulate(dir2, maxDepth, fileVisitOptions);
/*  111 */           if (visitor1.getDirList().size() != visitor2.getDirList().size() || visitor1
/*  112 */             .getFileList().size() != visitor2.getFileList().size()) {
/*  113 */             this.equals = false;
/*      */           } else {
/*  115 */             List<Path> tmpRelativeDirList1 = visitor1.relativizeDirectories(dir1, true, null);
/*  116 */             List<Path> tmpRelativeDirList2 = visitor2.relativizeDirectories(dir2, true, null);
/*  117 */             if (!tmpRelativeDirList1.equals(tmpRelativeDirList2)) {
/*  118 */               this.equals = false;
/*      */             } else {
/*  120 */               tmpRelativeFileList1 = visitor1.relativizeFiles(dir1, true, null);
/*  121 */               tmpRelativeFileList2 = visitor2.relativizeFiles(dir2, true, null);
/*  122 */               this.equals = tmpRelativeFileList1.equals(tmpRelativeFileList2);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  129 */       this.relativeFileList1 = tmpRelativeFileList1;
/*  130 */       this.relativeFileList2 = tmpRelativeFileList2;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  139 */   public static final CopyOption[] EMPTY_COPY_OPTIONS = new CopyOption[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  146 */   public static final DeleteOption[] EMPTY_DELETE_OPTION_ARRAY = new DeleteOption[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  151 */   public static final FileVisitOption[] EMPTY_FILE_VISIT_OPTION_ARRAY = new FileVisitOption[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  156 */   public static final LinkOption[] EMPTY_LINK_OPTION_ARRAY = new LinkOption[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  163 */   public static final LinkOption[] NOFOLLOW_LINK_OPTION_ARRAY = new LinkOption[] { LinkOption.NOFOLLOW_LINKS };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  168 */   public static final OpenOption[] EMPTY_OPEN_OPTION_ARRAY = new OpenOption[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  175 */   public static final Path[] EMPTY_PATH_ARRAY = new Path[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static AccumulatorPathVisitor accumulate(Path directory, int maxDepth, FileVisitOption[] fileVisitOptions) throws IOException {
/*  188 */     return visitFileTree(AccumulatorPathVisitor.withLongCounters(), directory, 
/*  189 */         toFileVisitOptionSet(fileVisitOptions), maxDepth);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Counters.PathCounters cleanDirectory(Path directory) throws IOException {
/*  200 */     return cleanDirectory(directory, EMPTY_DELETE_OPTION_ARRAY);
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
/*      */   public static Counters.PathCounters cleanDirectory(Path directory, DeleteOption... deleteOptions) throws IOException {
/*  214 */     return ((CleaningPathVisitor)visitFileTree(new CleaningPathVisitor(Counters.longPathCounters(), deleteOptions, new String[0]), directory))
/*  215 */       .getPathCounters();
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
/*      */   public static Counters.PathCounters copyDirectory(Path sourceDirectory, Path targetDirectory, CopyOption... copyOptions) throws IOException {
/*  229 */     Path absoluteSource = sourceDirectory.toAbsolutePath();
/*  230 */     return ((CopyDirectoryVisitor)visitFileTree(new CopyDirectoryVisitor(
/*  231 */           Counters.longPathCounters(), absoluteSource, targetDirectory, copyOptions), absoluteSource))
/*  232 */       .getPathCounters();
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
/*      */   public static Path copyFile(URL sourceFile, Path targetFile, CopyOption... copyOptions) throws IOException {
/*  247 */     try (InputStream inputStream = sourceFile.openStream()) {
/*  248 */       Files.copy(inputStream, targetFile, copyOptions);
/*  249 */       return targetFile;
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
/*      */   public static Path copyFileToDirectory(Path sourceFile, Path targetDirectory, CopyOption... copyOptions) throws IOException {
/*  265 */     return Files.copy(sourceFile, targetDirectory.resolve(sourceFile.getFileName()), copyOptions);
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
/*      */   public static Path copyFileToDirectory(URL sourceFile, Path targetDirectory, CopyOption... copyOptions) throws IOException {
/*  280 */     try (InputStream inputStream = sourceFile.openStream()) {
/*  281 */       Files.copy(inputStream, targetDirectory.resolve(sourceFile.getFile()), copyOptions);
/*  282 */       return targetDirectory;
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
/*      */   public static Counters.PathCounters countDirectory(Path directory) throws IOException {
/*  294 */     return ((CountingPathVisitor)visitFileTree(new CountingPathVisitor(Counters.longPathCounters()), directory)).getPathCounters();
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
/*      */   public static Path createParentDirectories(Path path, FileAttribute<?>... attrs) throws IOException {
/*  307 */     Path parent = path.getParent();
/*  308 */     if (parent == null) {
/*  309 */       return null;
/*      */     }
/*  311 */     return Files.createDirectories(parent, attrs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Path current() {
/*  322 */     return Paths.get("", new String[0]);
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
/*      */   public static Counters.PathCounters delete(Path path) throws IOException {
/*  342 */     return delete(path, EMPTY_DELETE_OPTION_ARRAY);
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
/*      */   public static Counters.PathCounters delete(Path path, DeleteOption... deleteOptions) throws IOException {
/*  365 */     return Files.isDirectory(path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS }) ? deleteDirectory(path, deleteOptions) : 
/*  366 */       deleteFile(path, deleteOptions);
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
/*      */   public static Counters.PathCounters delete(Path path, LinkOption[] linkOptions, DeleteOption... deleteOptions) throws IOException {
/*  391 */     return Files.isDirectory(path, linkOptions) ? deleteDirectory(path, linkOptions, deleteOptions) : 
/*  392 */       deleteFile(path, linkOptions, deleteOptions);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Counters.PathCounters deleteDirectory(Path directory) throws IOException {
/*  403 */     return deleteDirectory(directory, EMPTY_DELETE_OPTION_ARRAY);
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
/*      */   public static Counters.PathCounters deleteDirectory(Path directory, DeleteOption... deleteOptions) throws IOException {
/*  417 */     return ((DeletingPathVisitor)visitFileTree(new DeletingPathVisitor(
/*  418 */           Counters.longPathCounters(), NOFOLLOW_LINK_OPTION_ARRAY, deleteOptions, new String[0]), directory))
/*  419 */       .getPathCounters();
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
/*      */   public static Counters.PathCounters deleteDirectory(Path directory, LinkOption[] linkOptions, DeleteOption... deleteOptions) throws IOException {
/*  434 */     return ((DeletingPathVisitor)visitFileTree(new DeletingPathVisitor(Counters.longPathCounters(), linkOptions, deleteOptions, new String[0]), directory))
/*  435 */       .getPathCounters();
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
/*      */   public static Counters.PathCounters deleteFile(Path file) throws IOException {
/*  447 */     return deleteFile(file, EMPTY_DELETE_OPTION_ARRAY);
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
/*      */   public static Counters.PathCounters deleteFile(Path file, DeleteOption... deleteOptions) throws IOException {
/*  462 */     return deleteFile(file, NOFOLLOW_LINK_OPTION_ARRAY, deleteOptions);
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
/*      */   public static Counters.PathCounters deleteFile(Path file, LinkOption[] linkOptions, DeleteOption... deleteOptions) throws NoSuchFileException, IOException {
/*  478 */     if (Files.isDirectory(file, linkOptions)) {
/*  479 */       throw new NoSuchFileException(file.toString());
/*      */     }
/*  481 */     Counters.PathCounters pathCounts = Counters.longPathCounters();
/*  482 */     boolean exists = Files.exists(file, linkOptions);
/*  483 */     long size = (exists && !Files.isSymbolicLink(file)) ? Files.size(file) : 0L;
/*  484 */     if (overrideReadOnly(deleteOptions) && exists) {
/*  485 */       setReadOnly(file, false, linkOptions);
/*      */     }
/*  487 */     if (Files.deleteIfExists(file)) {
/*  488 */       pathCounts.getFileCounter().increment();
/*  489 */       pathCounts.getByteCounter().add(size);
/*      */     } 
/*  491 */     return pathCounts;
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
/*      */   public static boolean directoryAndFileContentEquals(Path path1, Path path2) throws IOException {
/*  504 */     return directoryAndFileContentEquals(path1, path2, EMPTY_LINK_OPTION_ARRAY, EMPTY_OPEN_OPTION_ARRAY, EMPTY_FILE_VISIT_OPTION_ARRAY);
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
/*      */   public static boolean directoryAndFileContentEquals(Path path1, Path path2, LinkOption[] linkOptions, OpenOption[] openOptions, FileVisitOption[] fileVisitOption) throws IOException {
/*  524 */     if (path1 == null && path2 == null) {
/*  525 */       return true;
/*      */     }
/*  527 */     if (path1 == null || path2 == null) {
/*  528 */       return false;
/*      */     }
/*  530 */     if (Files.notExists(path1, new LinkOption[0]) && Files.notExists(path2, new LinkOption[0])) {
/*  531 */       return true;
/*      */     }
/*  533 */     RelativeSortedPaths relativeSortedPaths = new RelativeSortedPaths(path1, path2, 2147483647, linkOptions, fileVisitOption);
/*      */ 
/*      */     
/*  536 */     if (!relativeSortedPaths.equals) {
/*  537 */       return false;
/*      */     }
/*      */     
/*  540 */     List<Path> fileList1 = relativeSortedPaths.relativeFileList1;
/*  541 */     List<Path> fileList2 = relativeSortedPaths.relativeFileList2;
/*  542 */     for (Path path : fileList1) {
/*  543 */       int binarySearch = Collections.binarySearch((List)fileList2, path);
/*  544 */       if (binarySearch <= -1) {
/*  545 */         throw new IllegalStateException("Unexpected mismatch.");
/*      */       }
/*  547 */       if (!fileContentEquals(path1.resolve(path), path2.resolve(path), linkOptions, openOptions)) {
/*  548 */         return false;
/*      */       }
/*      */     } 
/*  551 */     return true;
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
/*      */   public static boolean directoryContentEquals(Path path1, Path path2) throws IOException {
/*  564 */     return directoryContentEquals(path1, path2, 2147483647, EMPTY_LINK_OPTION_ARRAY, EMPTY_FILE_VISIT_OPTION_ARRAY);
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
/*      */   public static boolean directoryContentEquals(Path path1, Path path2, int maxDepth, LinkOption[] linkOptions, FileVisitOption[] fileVisitOptions) throws IOException {
/*  582 */     return (new RelativeSortedPaths(path1, path2, maxDepth, linkOptions, fileVisitOptions)).equals;
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
/*      */   public static boolean fileContentEquals(Path path1, Path path2) throws IOException {
/*  599 */     return fileContentEquals(path1, path2, EMPTY_LINK_OPTION_ARRAY, EMPTY_OPEN_OPTION_ARRAY);
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
/*      */   public static boolean fileContentEquals(Path path1, Path path2, LinkOption[] linkOptions, OpenOption[] openOptions) throws IOException {
/*  619 */     if (path1 == null && path2 == null) {
/*  620 */       return true;
/*      */     }
/*  622 */     if (path1 == null || path2 == null) {
/*  623 */       return false;
/*      */     }
/*  625 */     Path nPath1 = path1.normalize();
/*  626 */     Path nPath2 = path2.normalize();
/*  627 */     boolean path1Exists = Files.exists(nPath1, linkOptions);
/*  628 */     if (path1Exists != Files.exists(nPath2, linkOptions)) {
/*  629 */       return false;
/*      */     }
/*  631 */     if (!path1Exists)
/*      */     {
/*      */       
/*  634 */       return true;
/*      */     }
/*  636 */     if (Files.isDirectory(nPath1, linkOptions))
/*      */     {
/*  638 */       throw new IOException("Can't compare directories, only files: " + nPath1);
/*      */     }
/*  640 */     if (Files.isDirectory(nPath2, linkOptions))
/*      */     {
/*  642 */       throw new IOException("Can't compare directories, only files: " + nPath2);
/*      */     }
/*  644 */     if (Files.size(nPath1) != Files.size(nPath2))
/*      */     {
/*  646 */       return false;
/*      */     }
/*  648 */     if (path1.equals(path2))
/*      */     {
/*  650 */       return true;
/*      */     }
/*  652 */     try(InputStream inputStream1 = Files.newInputStream(nPath1, openOptions); 
/*  653 */         InputStream inputStream2 = Files.newInputStream(nPath2, openOptions)) {
/*  654 */       return IOUtils.contentEquals(inputStream1, inputStream2);
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
/*      */   public static Path[] filter(PathFilter filter, Path... paths) {
/*  684 */     Objects.requireNonNull(filter, "filter");
/*  685 */     if (paths == null) {
/*  686 */       return EMPTY_PATH_ARRAY;
/*      */     }
/*  688 */     return (Path[])((List)filterPaths(filter, Stream.of(paths), (Collector)Collectors.toList())).toArray((Object[])EMPTY_PATH_ARRAY);
/*      */   }
/*      */ 
/*      */   
/*      */   private static <R, A> R filterPaths(PathFilter filter, Stream<Path> stream, Collector<? super Path, A, R> collector) {
/*  693 */     Objects.requireNonNull(filter, "filter");
/*  694 */     Objects.requireNonNull(collector, "collector");
/*  695 */     if (stream == null) {
/*  696 */       return Stream.<Path>empty().collect(collector);
/*      */     }
/*  698 */     return stream.filter(p -> {
/*      */           try {
/*  700 */             return (p != null && filter.accept(p, readBasicFileAttributes(p)) == FileVisitResult.CONTINUE);
/*  701 */           } catch (IOException e) {
/*      */             return false;
/*      */           } 
/*  704 */         }).collect(collector);
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
/*      */   public static List<AclEntry> getAclEntryList(Path sourcePath) throws IOException {
/*  716 */     AclFileAttributeView fileAttributeView = Files.<AclFileAttributeView>getFileAttributeView(sourcePath, AclFileAttributeView.class, new LinkOption[0]);
/*      */     
/*  718 */     return (fileAttributeView == null) ? null : fileAttributeView.getAcl();
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
/*      */   public static boolean isDirectory(Path path, LinkOption... options) {
/*  736 */     return (path != null && Files.isDirectory(path, options));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(Path path) throws IOException {
/*  747 */     return Files.isDirectory(path, new LinkOption[0]) ? isEmptyDirectory(path) : isEmptyFile(path);
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
/*      */   public static boolean isEmptyDirectory(Path directory) throws IOException {
/*  763 */     try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory)) {
/*  764 */       return !directoryStream.iterator().hasNext();
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
/*      */   public static boolean isEmptyFile(Path file) throws IOException {
/*  779 */     return (Files.size(file) <= 0L);
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
/*      */   public static boolean isNewer(Path file, long timeMillis, LinkOption... options) throws IOException {
/*  796 */     Objects.requireNonNull(file, "file");
/*  797 */     if (Files.notExists(file, new LinkOption[0])) {
/*  798 */       return false;
/*      */     }
/*  800 */     return (Files.getLastModifiedTime(file, options).toMillis() > timeMillis);
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
/*      */   public static boolean isRegularFile(Path path, LinkOption... options) {
/*  818 */     return (path != null && Files.isRegularFile(path, options));
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
/*      */   public static DirectoryStream<Path> newDirectoryStream(Path dir, PathFilter pathFilter) throws IOException {
/*  831 */     return Files.newDirectoryStream(dir, new DirectoryStreamFilter(pathFilter));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean overrideReadOnly(DeleteOption... deleteOptions) {
/*  841 */     if (deleteOptions == null) {
/*  842 */       return false;
/*      */     }
/*  844 */     return Stream.<DeleteOption>of(deleteOptions).anyMatch(e -> (e == StandardDeleteOption.OVERRIDE_READ_ONLY));
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
/*      */   public static BasicFileAttributes readBasicFileAttributes(Path path) throws IOException {
/*  856 */     return Files.readAttributes(path, BasicFileAttributes.class, new LinkOption[0]);
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
/*      */   public static BasicFileAttributes readBasicFileAttributesUnchecked(Path path) {
/*      */     try {
/*  870 */       return readBasicFileAttributes(path);
/*  871 */     } catch (IOException e) {
/*  872 */       throw new UncheckedIOException(e);
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
/*      */   static List<Path> relativize(Collection<Path> collection, Path parent, boolean sort, Comparator<? super Path> comparator) {
/*  887 */     Stream<Path> stream = collection.stream().map(parent::relativize);
/*  888 */     if (sort) {
/*  889 */       stream = (comparator == null) ? stream.sorted() : stream.sorted(comparator);
/*      */     }
/*  891 */     return stream.collect((Collector)Collectors.toList());
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
/*      */   public static Path setReadOnly(Path path, boolean readOnly, LinkOption... linkOptions) throws IOException {
/*  909 */     List<Exception> causeList = new ArrayList<>(2);
/*  910 */     DosFileAttributeView fileAttributeView = Files.<DosFileAttributeView>getFileAttributeView(path, DosFileAttributeView.class, linkOptions);
/*      */     
/*  912 */     if (fileAttributeView != null) {
/*      */       try {
/*  914 */         fileAttributeView.setReadOnly(readOnly);
/*  915 */         return path;
/*  916 */       } catch (IOException e) {
/*      */         
/*  918 */         causeList.add(e);
/*      */       } 
/*      */     }
/*  921 */     PosixFileAttributeView posixFileAttributeView = Files.<PosixFileAttributeView>getFileAttributeView(path, PosixFileAttributeView.class, linkOptions);
/*      */     
/*  923 */     if (posixFileAttributeView != null) {
/*      */ 
/*      */ 
/*      */       
/*  927 */       PosixFileAttributes readAttributes = posixFileAttributeView.readAttributes();
/*  928 */       Set<PosixFilePermission> permissions = readAttributes.permissions();
/*  929 */       permissions.remove(PosixFilePermission.OWNER_WRITE);
/*  930 */       permissions.remove(PosixFilePermission.GROUP_WRITE);
/*  931 */       permissions.remove(PosixFilePermission.OTHERS_WRITE);
/*      */       try {
/*  933 */         return Files.setPosixFilePermissions(path, permissions);
/*  934 */       } catch (IOException e) {
/*  935 */         causeList.add(e);
/*      */       } 
/*      */     } 
/*  938 */     if (!causeList.isEmpty()) {
/*  939 */       throw new IOExceptionList(path.toString(), causeList);
/*      */     }
/*  941 */     throw new IOException(
/*  942 */         String.format("No DosFileAttributeView or PosixFileAttributeView for '%s' (linkOptions=%s)", new Object[] {
/*  943 */             path, Arrays.toString(linkOptions)
/*      */           }));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Set<FileVisitOption> toFileVisitOptionSet(FileVisitOption... fileVisitOptions) {
/*  953 */     return (fileVisitOptions == null) ? EnumSet.<FileVisitOption>noneOf(FileVisitOption.class) : 
/*  954 */       (Set<FileVisitOption>)Stream.<FileVisitOption>of(fileVisitOptions).collect(Collectors.toSet());
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
/*      */   public static <T extends FileVisitor<? super Path>> T visitFileTree(T visitor, Path directory) throws IOException {
/*  971 */     Files.walkFileTree(directory, (FileVisitor<? super Path>)visitor);
/*  972 */     return visitor;
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
/*      */   public static <T extends FileVisitor<? super Path>> T visitFileTree(T visitor, Path start, Set<FileVisitOption> options, int maxDepth) throws IOException {
/*  991 */     Files.walkFileTree(start, options, maxDepth, (FileVisitor<? super Path>)visitor);
/*  992 */     return visitor;
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
/*      */   public static <T extends FileVisitor<? super Path>> T visitFileTree(T visitor, String first, String... more) throws IOException {
/* 1010 */     return visitFileTree(visitor, Paths.get(first, more));
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
/*      */   public static <T extends FileVisitor<? super Path>> T visitFileTree(T visitor, URI uri) throws IOException {
/* 1027 */     return visitFileTree(visitor, Paths.get(uri));
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
/*      */   public static Stream<Path> walk(Path start, PathFilter pathFilter, int maxDepth, boolean readAttributes, FileVisitOption... options) throws IOException {
/* 1044 */     return Files.walk(start, maxDepth, options).filter(path -> (pathFilter.accept(path, readAttributes ? readBasicFileAttributesUnchecked(path) : null) == FileVisitResult.CONTINUE));
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\io\file\PathUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */