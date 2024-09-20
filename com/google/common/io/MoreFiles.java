/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.graph.SuccessorsFunction;
/*     */ import com.google.common.graph.Traverser;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.channels.Channels;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.DirectoryIteratorException;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.FileAlreadyExistsException;
/*     */ import java.nio.file.FileSystemException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.SecureDirectoryStream;
/*     */ import java.nio.file.attribute.BasicFileAttributeView;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class MoreFiles
/*     */ {
/*     */   public static ByteSource asByteSource(Path path, OpenOption... options) {
/*  84 */     return new PathByteSource(path, options);
/*     */   }
/*     */   
/*     */   private static final class PathByteSource
/*     */     extends ByteSource {
/*  89 */     private static final LinkOption[] FOLLOW_LINKS = new LinkOption[0];
/*     */     
/*     */     private final Path path;
/*     */     private final OpenOption[] options;
/*     */     private final boolean followLinks;
/*     */     
/*     */     private PathByteSource(Path path, OpenOption... options) {
/*  96 */       this.path = (Path)Preconditions.checkNotNull(path);
/*  97 */       this.options = (OpenOption[])options.clone();
/*  98 */       this.followLinks = followLinks(this.options);
/*     */     }
/*     */ 
/*     */     
/*     */     private static boolean followLinks(OpenOption[] options) {
/* 103 */       for (OpenOption option : options) {
/* 104 */         if (option == LinkOption.NOFOLLOW_LINKS) {
/* 105 */           return false;
/*     */         }
/*     */       } 
/* 108 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openStream() throws IOException {
/* 113 */       return Files.newInputStream(this.path, this.options);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private BasicFileAttributes readAttributes() throws IOException {
/* 120 */       (new LinkOption[1])[0] = LinkOption.NOFOLLOW_LINKS; return Files.readAttributes(this.path, BasicFileAttributes.class, this.followLinks ? FOLLOW_LINKS : new LinkOption[1]);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Long> sizeIfKnown() {
/*     */       BasicFileAttributes attrs;
/*     */       try {
/* 127 */         attrs = readAttributes();
/* 128 */       } catch (IOException e) {
/*     */         
/* 130 */         return Optional.absent();
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 135 */       if (attrs.isDirectory() || attrs.isSymbolicLink()) {
/* 136 */         return Optional.absent();
/*     */       }
/*     */       
/* 139 */       return Optional.of(Long.valueOf(attrs.size()));
/*     */     }
/*     */ 
/*     */     
/*     */     public long size() throws IOException {
/* 144 */       BasicFileAttributes attrs = readAttributes();
/*     */ 
/*     */ 
/*     */       
/* 148 */       if (attrs.isDirectory())
/* 149 */         throw new IOException("can't read: is a directory"); 
/* 150 */       if (attrs.isSymbolicLink()) {
/* 151 */         throw new IOException("can't read: is a symbolic link");
/*     */       }
/*     */       
/* 154 */       return attrs.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] read() throws IOException {
/* 159 */       SeekableByteChannel channel = Files.newByteChannel(this.path, this.options); 
/* 160 */       try { byte[] arrayOfByte = ByteStreams.toByteArray(Channels.newInputStream(channel), channel.size());
/* 161 */         if (channel != null) channel.close();  return arrayOfByte; }
/*     */       catch (Throwable throwable) { if (channel != null)
/*     */           try { channel.close(); }
/*     */           catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */             throw throwable; }
/* 166 */        } public CharSource asCharSource(Charset charset) { if (this.options.length == 0)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 171 */         return new ByteSource.AsCharSource(charset)
/*     */           {
/*     */             public Stream<String> lines() throws IOException
/*     */             {
/* 175 */               return Files.lines(MoreFiles.PathByteSource.this.path, this.charset);
/*     */             }
/*     */           };
/*     */       }
/*     */       
/* 180 */       return super.asCharSource(charset); }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 185 */       String str1 = String.valueOf(this.path), str2 = Arrays.toString((Object[])this.options); return (new StringBuilder(26 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("MoreFiles.asByteSource(").append(str1).append(", ").append(str2).append(")").toString();
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
/*     */   public static ByteSink asByteSink(Path path, OpenOption... options) {
/* 200 */     return new PathByteSink(path, options);
/*     */   }
/*     */   
/*     */   private static final class PathByteSink
/*     */     extends ByteSink {
/*     */     private final Path path;
/*     */     private final OpenOption[] options;
/*     */     
/*     */     private PathByteSink(Path path, OpenOption... options) {
/* 209 */       this.path = (Path)Preconditions.checkNotNull(path);
/* 210 */       this.options = (OpenOption[])options.clone();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public OutputStream openStream() throws IOException {
/* 216 */       return Files.newOutputStream(this.path, this.options);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 221 */       String str1 = String.valueOf(this.path), str2 = Arrays.toString((Object[])this.options); return (new StringBuilder(24 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("MoreFiles.asByteSink(").append(str1).append(", ").append(str2).append(")").toString();
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
/*     */   public static CharSource asCharSource(Path path, Charset charset, OpenOption... options) {
/* 235 */     return asByteSource(path, options).asCharSource(charset);
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
/*     */   public static CharSink asCharSink(Path path, Charset charset, OpenOption... options) {
/* 249 */     return asByteSink(path, options).asCharSink(charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ImmutableList<Path> listFiles(Path dir) throws IOException {
/*     */     
/* 261 */     try { DirectoryStream<Path> stream = Files.newDirectoryStream(dir); 
/* 262 */       try { ImmutableList<Path> immutableList = ImmutableList.copyOf(stream);
/* 263 */         if (stream != null) stream.close();  return immutableList; } catch (Throwable throwable) { if (stream != null) try { stream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (DirectoryIteratorException e)
/* 264 */     { throw e.getCause(); }
/*     */   
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
/*     */   public static Traverser<Path> fileTraverser() {
/* 292 */     return Traverser.forTree(FILE_TREE);
/*     */   }
/*     */   
/* 295 */   private static final SuccessorsFunction<Path> FILE_TREE = new SuccessorsFunction<Path>()
/*     */     {
/*     */       public Iterable<Path> successors(Path path)
/*     */       {
/* 299 */         return MoreFiles.fileTreeChildren(path);
/*     */       }
/*     */     };
/*     */   
/*     */   private static Iterable<Path> fileTreeChildren(Path dir) {
/* 304 */     if (Files.isDirectory(dir, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
/*     */       try {
/* 306 */         return (Iterable<Path>)listFiles(dir);
/* 307 */       } catch (IOException e) {
/*     */         
/* 309 */         throw new DirectoryIteratorException(e);
/*     */       } 
/*     */     }
/* 312 */     return (Iterable<Path>)ImmutableList.of();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Path> isDirectory(LinkOption... options) {
/* 320 */     final LinkOption[] optionsCopy = (LinkOption[])options.clone();
/* 321 */     return new Predicate<Path>()
/*     */       {
/*     */         public boolean apply(Path input) {
/* 324 */           return Files.isDirectory(input, optionsCopy);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 329 */           String str = Arrays.toString((Object[])optionsCopy); return (new StringBuilder(23 + String.valueOf(str).length())).append("MoreFiles.isDirectory(").append(str).append(")").toString();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isDirectory(SecureDirectoryStream<Path> dir, Path name, LinkOption... options) throws IOException {
/* 337 */     return ((BasicFileAttributeView)dir.<BasicFileAttributeView>getFileAttributeView(name, BasicFileAttributeView.class, options))
/* 338 */       .readAttributes()
/* 339 */       .isDirectory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Path> isRegularFile(LinkOption... options) {
/* 347 */     final LinkOption[] optionsCopy = (LinkOption[])options.clone();
/* 348 */     return new Predicate<Path>()
/*     */       {
/*     */         public boolean apply(Path input) {
/* 351 */           return Files.isRegularFile(input, optionsCopy);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 356 */           String str = Arrays.toString((Object[])optionsCopy); return (new StringBuilder(25 + String.valueOf(str).length())).append("MoreFiles.isRegularFile(").append(str).append(")").toString();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equal(Path path1, Path path2) throws IOException {
/* 369 */     Preconditions.checkNotNull(path1);
/* 370 */     Preconditions.checkNotNull(path2);
/* 371 */     if (Files.isSameFile(path1, path2)) {
/* 372 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 380 */     ByteSource source1 = asByteSource(path1, new OpenOption[0]);
/* 381 */     ByteSource source2 = asByteSource(path2, new OpenOption[0]);
/* 382 */     long len1 = ((Long)source1.sizeIfKnown().or(Long.valueOf(0L))).longValue();
/* 383 */     long len2 = ((Long)source2.sizeIfKnown().or(Long.valueOf(0L))).longValue();
/* 384 */     if (len1 != 0L && len2 != 0L && len1 != len2) {
/* 385 */       return false;
/*     */     }
/* 387 */     return source1.contentEquals(source2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void touch(Path path) throws IOException {
/* 396 */     Preconditions.checkNotNull(path);
/*     */     
/*     */     try {
/* 399 */       Files.setLastModifiedTime(path, FileTime.fromMillis(System.currentTimeMillis()));
/* 400 */     } catch (NoSuchFileException e) {
/*     */       try {
/* 402 */         Files.createFile(path, (FileAttribute<?>[])new FileAttribute[0]);
/* 403 */       } catch (FileAlreadyExistsException fileAlreadyExistsException) {}
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
/*     */   public static void createParentDirectories(Path path, FileAttribute<?>... attrs) throws IOException {
/* 428 */     Path normalizedAbsolutePath = path.toAbsolutePath().normalize();
/* 429 */     Path parent = normalizedAbsolutePath.getParent();
/* 430 */     if (parent == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 442 */     if (!Files.isDirectory(parent, new LinkOption[0])) {
/* 443 */       Files.createDirectories(parent, attrs);
/* 444 */       if (!Files.isDirectory(parent, new LinkOption[0])) {
/* 445 */         String str = String.valueOf(path); throw new IOException((new StringBuilder(39 + String.valueOf(str).length())).append("Unable to create parent directories of ").append(str).toString());
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
/*     */   public static String getFileExtension(Path path) {
/* 463 */     Path name = path.getFileName();
/*     */ 
/*     */     
/* 466 */     if (name == null) {
/* 467 */       return "";
/*     */     }
/*     */     
/* 470 */     String fileName = name.toString();
/* 471 */     int dotIndex = fileName.lastIndexOf('.');
/* 472 */     return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getNameWithoutExtension(Path path) {
/* 481 */     Path name = path.getFileName();
/*     */ 
/*     */     
/* 484 */     if (name == null) {
/* 485 */       return "";
/*     */     }
/*     */     
/* 488 */     String fileName = name.toString();
/* 489 */     int dotIndex = fileName.lastIndexOf('.');
/* 490 */     return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
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
/*     */   public static void deleteRecursively(Path path, RecursiveDeleteOption... options) throws IOException {
/* 523 */     Path parentPath = getParentPath(path);
/* 524 */     if (parentPath == null) {
/* 525 */       throw new FileSystemException(path.toString(), null, "can't delete recursively");
/*     */     }
/*     */     
/* 528 */     Collection<IOException> exceptions = null;
/*     */     try {
/* 530 */       boolean sdsSupported = false;
/* 531 */       DirectoryStream<Path> parent = Files.newDirectoryStream(parentPath); 
/* 532 */       try { if (parent instanceof SecureDirectoryStream) {
/* 533 */           sdsSupported = true;
/*     */           
/* 535 */           exceptions = deleteRecursivelySecure((SecureDirectoryStream<Path>)parent, path.getFileName());
/*     */         } 
/* 537 */         if (parent != null) parent.close();  } catch (Throwable throwable) { if (parent != null)
/*     */           try { parent.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 539 */        if (!sdsSupported) {
/* 540 */         checkAllowsInsecure(path, options);
/* 541 */         exceptions = deleteRecursivelyInsecure(path);
/*     */       } 
/* 543 */     } catch (IOException e) {
/* 544 */       if (exceptions == null) {
/* 545 */         throw e;
/*     */       }
/* 547 */       exceptions.add(e);
/*     */     } 
/*     */ 
/*     */     
/* 551 */     if (exceptions != null) {
/* 552 */       throwDeleteFailed(path, exceptions);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void deleteDirectoryContents(Path path, RecursiveDeleteOption... options) throws IOException {
/* 589 */     Collection<IOException> exceptions = null; 
/* 590 */     try { DirectoryStream<Path> stream = Files.newDirectoryStream(path); 
/* 591 */       try { if (stream instanceof SecureDirectoryStream) {
/* 592 */           SecureDirectoryStream<Path> sds = (SecureDirectoryStream<Path>)stream;
/* 593 */           exceptions = deleteDirectoryContentsSecure(sds);
/*     */         } else {
/* 595 */           checkAllowsInsecure(path, options);
/* 596 */           exceptions = deleteDirectoryContentsInsecure(stream);
/*     */         } 
/* 598 */         if (stream != null) stream.close();  } catch (Throwable throwable) { if (stream != null) try { stream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 599 */     { if (exceptions == null) {
/* 600 */         throw e;
/*     */       }
/* 602 */       exceptions.add(e); }
/*     */ 
/*     */ 
/*     */     
/* 606 */     if (exceptions != null) {
/* 607 */       throwDeleteFailed(path, exceptions);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Collection<IOException> deleteRecursivelySecure(SecureDirectoryStream<Path> dir, Path path) {
/* 617 */     Collection<IOException> exceptions = null;
/*     */     try {
/* 619 */       if (isDirectory(dir, path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
/* 620 */         SecureDirectoryStream<Path> childDir = dir.newDirectoryStream(path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS }); 
/* 621 */         try { exceptions = deleteDirectoryContentsSecure(childDir);
/* 622 */           if (childDir != null) childDir.close();  } catch (Throwable throwable) { if (childDir != null)
/*     */             try { childDir.close(); }
/*     */             catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */               throw throwable; }
/* 626 */          if (exceptions == null) {
/* 627 */           dir.deleteDirectory(path);
/*     */         }
/*     */       } else {
/* 630 */         dir.deleteFile(path);
/*     */       } 
/*     */       
/* 633 */       return exceptions;
/* 634 */     } catch (IOException e) {
/* 635 */       return addException(exceptions, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Collection<IOException> deleteDirectoryContentsSecure(SecureDirectoryStream<Path> dir) {
/* 645 */     Collection<IOException> exceptions = null;
/*     */     try {
/* 647 */       for (Path path : dir) {
/* 648 */         exceptions = concat(exceptions, deleteRecursivelySecure(dir, path.getFileName()));
/*     */       }
/*     */       
/* 651 */       return exceptions;
/* 652 */     } catch (DirectoryIteratorException e) {
/* 653 */       return addException(exceptions, e.getCause());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Collection<IOException> deleteRecursivelyInsecure(Path path) {
/* 662 */     Collection<IOException> exceptions = null;
/*     */     try {
/* 664 */       if (Files.isDirectory(path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
/* 665 */         DirectoryStream<Path> stream = Files.newDirectoryStream(path); 
/* 666 */         try { exceptions = deleteDirectoryContentsInsecure(stream);
/* 667 */           if (stream != null) stream.close();  } catch (Throwable throwable) { if (stream != null)
/*     */             try { stream.close(); }
/*     */             catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */               throw throwable; }
/*     */       
/* 672 */       }  if (exceptions == null) {
/* 673 */         Files.delete(path);
/*     */       }
/*     */       
/* 676 */       return exceptions;
/* 677 */     } catch (IOException e) {
/* 678 */       return addException(exceptions, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Collection<IOException> deleteDirectoryContentsInsecure(DirectoryStream<Path> dir) {
/* 689 */     Collection<IOException> exceptions = null;
/*     */     try {
/* 691 */       for (Path entry : dir) {
/* 692 */         exceptions = concat(exceptions, deleteRecursivelyInsecure(entry));
/*     */       }
/*     */       
/* 695 */       return exceptions;
/* 696 */     } catch (DirectoryIteratorException e) {
/* 697 */       return addException(exceptions, e.getCause());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Path getParentPath(Path path) {
/* 707 */     Path parent = path.getParent();
/*     */ 
/*     */     
/* 710 */     if (parent != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 716 */       return parent;
/*     */     }
/*     */ 
/*     */     
/* 720 */     if (path.getNameCount() == 0)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 732 */       return null;
/*     */     }
/*     */     
/* 735 */     return path.getFileSystem().getPath(".", new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void checkAllowsInsecure(Path path, RecursiveDeleteOption[] options) throws InsecureRecursiveDeleteException {
/* 742 */     if (!Arrays.<RecursiveDeleteOption>asList(options).contains(RecursiveDeleteOption.ALLOW_INSECURE)) {
/* 743 */       throw new InsecureRecursiveDeleteException(path.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Collection<IOException> addException(Collection<IOException> exceptions, IOException e) {
/* 753 */     if (exceptions == null) {
/* 754 */       exceptions = new ArrayList<>();
/*     */     }
/* 756 */     exceptions.add(e);
/* 757 */     return exceptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Collection<IOException> concat(Collection<IOException> exceptions, Collection<IOException> other) {
/* 767 */     if (exceptions == null)
/* 768 */       return other; 
/* 769 */     if (other != null) {
/* 770 */       exceptions.addAll(other);
/*     */     }
/* 772 */     return exceptions;
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
/*     */   private static void throwDeleteFailed(Path path, Collection<IOException> exceptions) throws FileSystemException {
/* 786 */     FileSystemException deleteFailed = new FileSystemException(path.toString(), null, "failed to delete one or more files; see suppressed exceptions for details");
/*     */ 
/*     */     
/* 789 */     for (IOException e : exceptions) {
/* 790 */       deleteFailed.addSuppressed(e);
/*     */     }
/* 792 */     throw deleteFailed;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\io\MoreFiles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */