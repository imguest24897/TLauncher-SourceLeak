/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.CharMatcher;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.base.StandardSystemProperty;
/*     */ import com.google.common.collect.FluentIterable;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.common.io.ByteSource;
/*     */ import com.google.common.io.CharSource;
/*     */ import com.google.common.io.Resources;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class ClassPath
/*     */ {
/*  76 */   private static final Logger logger = Logger.getLogger(ClassPath.class.getName());
/*     */ 
/*     */ 
/*     */   
/*  80 */   private static final Splitter CLASS_PATH_ATTRIBUTE_SEPARATOR = Splitter.on(" ").omitEmptyStrings();
/*     */   
/*     */   private static final String CLASS_FILE_NAME_EXTENSION = ".class";
/*     */   
/*     */   private final ImmutableSet<ResourceInfo> resources;
/*     */   
/*     */   private ClassPath(ImmutableSet<ResourceInfo> resources) {
/*  87 */     this.resources = resources;
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
/*     */   public static ClassPath from(ClassLoader classloader) throws IOException {
/* 107 */     ImmutableSet<LocationInfo> locations = locationsFrom(classloader);
/*     */ 
/*     */ 
/*     */     
/* 111 */     Set<File> scanned = new HashSet<>();
/* 112 */     for (UnmodifiableIterator<LocationInfo> unmodifiableIterator1 = locations.iterator(); unmodifiableIterator1.hasNext(); ) { LocationInfo location = unmodifiableIterator1.next();
/* 113 */       scanned.add(location.file()); }
/*     */ 
/*     */ 
/*     */     
/* 117 */     ImmutableSet.Builder<ResourceInfo> builder = ImmutableSet.builder();
/* 118 */     for (UnmodifiableIterator<LocationInfo> unmodifiableIterator2 = locations.iterator(); unmodifiableIterator2.hasNext(); ) { LocationInfo location = unmodifiableIterator2.next();
/* 119 */       builder.addAll((Iterable)location.scanResources(scanned)); }
/*     */     
/* 121 */     return new ClassPath(builder.build());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<ResourceInfo> getResources() {
/* 129 */     return this.resources;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<ClassInfo> getAllClasses() {
/* 138 */     return FluentIterable.from((Iterable)this.resources).filter(ClassInfo.class).toSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<ClassInfo> getTopLevelClasses() {
/* 146 */     return FluentIterable.from((Iterable)this.resources)
/* 147 */       .filter(ClassInfo.class)
/* 148 */       .filter(new Predicate<ClassInfo>(this)
/*     */         {
/*     */           public boolean apply(ClassPath.ClassInfo info)
/*     */           {
/* 152 */             return info.isTopLevel();
/*     */           }
/* 155 */         }).toSet();
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<ClassInfo> getTopLevelClasses(String packageName) {
/* 160 */     Preconditions.checkNotNull(packageName);
/* 161 */     ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
/* 162 */     for (UnmodifiableIterator<ClassInfo> unmodifiableIterator = getTopLevelClasses().iterator(); unmodifiableIterator.hasNext(); ) { ClassInfo classInfo = unmodifiableIterator.next();
/* 163 */       if (classInfo.getPackageName().equals(packageName)) {
/* 164 */         builder.add(classInfo);
/*     */       } }
/*     */     
/* 167 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<ClassInfo> getTopLevelClassesRecursive(String packageName) {
/* 175 */     Preconditions.checkNotNull(packageName);
/* 176 */     String packagePrefix = (new StringBuilder(1 + String.valueOf(packageName).length())).append(packageName).append('.').toString();
/* 177 */     ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
/* 178 */     for (UnmodifiableIterator<ClassInfo> unmodifiableIterator = getTopLevelClasses().iterator(); unmodifiableIterator.hasNext(); ) { ClassInfo classInfo = unmodifiableIterator.next();
/* 179 */       if (classInfo.getName().startsWith(packagePrefix)) {
/* 180 */         builder.add(classInfo);
/*     */       } }
/*     */     
/* 183 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static class ResourceInfo
/*     */   {
/*     */     private final File file;
/*     */ 
/*     */     
/*     */     private final String resourceName;
/*     */     
/*     */     final ClassLoader loader;
/*     */ 
/*     */     
/*     */     static ResourceInfo of(File file, String resourceName, ClassLoader loader) {
/* 200 */       if (resourceName.endsWith(".class")) {
/* 201 */         return new ClassPath.ClassInfo(file, resourceName, loader);
/*     */       }
/* 203 */       return new ResourceInfo(file, resourceName, loader);
/*     */     }
/*     */ 
/*     */     
/*     */     ResourceInfo(File file, String resourceName, ClassLoader loader) {
/* 208 */       this.file = (File)Preconditions.checkNotNull(file);
/* 209 */       this.resourceName = (String)Preconditions.checkNotNull(resourceName);
/* 210 */       this.loader = (ClassLoader)Preconditions.checkNotNull(loader);
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
/*     */     public final URL url() {
/* 222 */       URL url = this.loader.getResource(this.resourceName);
/* 223 */       if (url == null) {
/* 224 */         throw new NoSuchElementException(this.resourceName);
/*     */       }
/* 226 */       return url;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final ByteSource asByteSource() {
/* 237 */       return Resources.asByteSource(url());
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
/*     */     public final CharSource asCharSource(Charset charset) {
/* 249 */       return Resources.asCharSource(url(), charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public final String getResourceName() {
/* 254 */       return this.resourceName;
/*     */     }
/*     */ 
/*     */     
/*     */     final File getFile() {
/* 259 */       return this.file;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 264 */       return this.resourceName.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 269 */       if (obj instanceof ResourceInfo) {
/* 270 */         ResourceInfo that = (ResourceInfo)obj;
/* 271 */         return (this.resourceName.equals(that.resourceName) && this.loader == that.loader);
/*     */       } 
/* 273 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 279 */       return this.resourceName;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static final class ClassInfo
/*     */     extends ResourceInfo
/*     */   {
/*     */     private final String className;
/*     */ 
/*     */     
/*     */     ClassInfo(File file, String resourceName, ClassLoader loader) {
/* 293 */       super(file, resourceName, loader);
/* 294 */       this.className = ClassPath.getClassName(resourceName);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getPackageName() {
/* 304 */       return Reflection.getPackageName(this.className);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getSimpleName() {
/* 314 */       int lastDollarSign = this.className.lastIndexOf('$');
/* 315 */       if (lastDollarSign != -1) {
/* 316 */         String innerClassName = this.className.substring(lastDollarSign + 1);
/*     */ 
/*     */         
/* 319 */         return CharMatcher.inRange('0', '9').trimLeadingFrom(innerClassName);
/*     */       } 
/* 321 */       String packageName = getPackageName();
/* 322 */       if (packageName.isEmpty()) {
/* 323 */         return this.className;
/*     */       }
/*     */ 
/*     */       
/* 327 */       return this.className.substring(packageName.length() + 1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/* 337 */       return this.className;
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
/*     */     public boolean isTopLevel() {
/* 349 */       return (this.className.indexOf('$') == -1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Class<?> load() {
/*     */       try {
/* 360 */         return this.loader.loadClass(this.className);
/* 361 */       } catch (ClassNotFoundException e) {
/*     */         
/* 363 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 369 */       return this.className;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ImmutableSet<LocationInfo> locationsFrom(ClassLoader classloader) {
/* 379 */     ImmutableSet.Builder<LocationInfo> builder = ImmutableSet.builder();
/* 380 */     for (UnmodifiableIterator<Map.Entry<File, ClassLoader>> unmodifiableIterator = getClassPathEntries(classloader).entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<File, ClassLoader> entry = unmodifiableIterator.next();
/* 381 */       builder.add(new LocationInfo(entry.getKey(), entry.getValue())); }
/*     */     
/* 383 */     return builder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   static final class LocationInfo
/*     */   {
/*     */     final File home;
/*     */     
/*     */     private final ClassLoader classloader;
/*     */ 
/*     */     
/*     */     LocationInfo(File home, ClassLoader classloader) {
/* 395 */       this.home = (File)Preconditions.checkNotNull(home);
/* 396 */       this.classloader = (ClassLoader)Preconditions.checkNotNull(classloader);
/*     */     }
/*     */ 
/*     */     
/*     */     public final File file() {
/* 401 */       return this.home;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableSet<ClassPath.ResourceInfo> scanResources() throws IOException {
/* 406 */       return scanResources(new HashSet<>());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableSet<ClassPath.ResourceInfo> scanResources(Set<File> scannedFiles) throws IOException {
/* 424 */       ImmutableSet.Builder<ClassPath.ResourceInfo> builder = ImmutableSet.builder();
/* 425 */       scannedFiles.add(this.home);
/* 426 */       scan(this.home, scannedFiles, builder);
/* 427 */       return builder.build();
/*     */     }
/*     */ 
/*     */     
/*     */     private void scan(File file, Set<File> scannedUris, ImmutableSet.Builder<ClassPath.ResourceInfo> builder) throws IOException {
/*     */       try {
/* 433 */         if (!file.exists()) {
/*     */           return;
/*     */         }
/* 436 */       } catch (SecurityException e) {
/* 437 */         String str1 = String.valueOf(file), str2 = String.valueOf(e); ClassPath.logger.warning((new StringBuilder(16 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("Cannot access ").append(str1).append(": ").append(str2).toString());
/*     */         
/*     */         return;
/*     */       } 
/* 441 */       if (file.isDirectory()) {
/* 442 */         scanDirectory(file, builder);
/*     */       } else {
/* 444 */         scanJar(file, scannedUris, builder);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void scanJar(File file, Set<File> scannedUris, ImmutableSet.Builder<ClassPath.ResourceInfo> builder) throws IOException {
/*     */       JarFile jarFile;
/*     */       try {
/* 453 */         jarFile = new JarFile(file);
/* 454 */       } catch (IOException e) {
/*     */         return;
/*     */       } 
/*     */       
/*     */       try {
/* 459 */         for (UnmodifiableIterator<File> unmodifiableIterator = ClassPath.getClassPathFromManifest(file, jarFile.getManifest()).iterator(); unmodifiableIterator.hasNext(); ) { File path = unmodifiableIterator.next();
/*     */ 
/*     */           
/* 462 */           if (scannedUris.add(path.getCanonicalFile())) {
/* 463 */             scan(path, scannedUris, builder);
/*     */           } }
/*     */         
/* 466 */         scanJarFile(jarFile, builder);
/*     */       } finally {
/*     */         try {
/* 469 */           jarFile.close();
/* 470 */         } catch (IOException iOException) {}
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private void scanJarFile(JarFile file, ImmutableSet.Builder<ClassPath.ResourceInfo> builder) {
/* 476 */       Enumeration<JarEntry> entries = file.entries();
/* 477 */       while (entries.hasMoreElements()) {
/* 478 */         JarEntry entry = entries.nextElement();
/* 479 */         if (entry.isDirectory() || entry.getName().equals("META-INF/MANIFEST.MF")) {
/*     */           continue;
/*     */         }
/* 482 */         builder.add(ClassPath.ResourceInfo.of(new File(file.getName()), entry.getName(), this.classloader));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private void scanDirectory(File directory, ImmutableSet.Builder<ClassPath.ResourceInfo> builder) throws IOException {
/* 488 */       Set<File> currentPath = new HashSet<>();
/* 489 */       currentPath.add(directory.getCanonicalFile());
/* 490 */       scanDirectory(directory, "", currentPath, builder);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void scanDirectory(File directory, String packagePrefix, Set<File> currentPath, ImmutableSet.Builder<ClassPath.ResourceInfo> builder) throws IOException {
/* 510 */       File[] files = directory.listFiles();
/* 511 */       if (files == null) {
/* 512 */         String str = String.valueOf(directory); ClassPath.logger.warning((new StringBuilder(22 + String.valueOf(str).length())).append("Cannot read directory ").append(str).toString());
/*     */         
/*     */         return;
/*     */       } 
/* 516 */       for (File f : files) {
/* 517 */         String name = f.getName();
/* 518 */         if (f.isDirectory()) {
/* 519 */           File deref = f.getCanonicalFile();
/* 520 */           if (currentPath.add(deref)) {
/* 521 */             scanDirectory(deref, (new StringBuilder(1 + String.valueOf(packagePrefix).length() + String.valueOf(name).length())).append(packagePrefix).append(name).append("/").toString(), currentPath, builder);
/* 522 */             currentPath.remove(deref);
/*     */           } 
/*     */         } else {
/* 525 */           String.valueOf(name); String resourceName = (String.valueOf(name).length() != 0) ? String.valueOf(packagePrefix).concat(String.valueOf(name)) : new String(String.valueOf(packagePrefix));
/* 526 */           if (!resourceName.equals("META-INF/MANIFEST.MF")) {
/* 527 */             builder.add(ClassPath.ResourceInfo.of(f, resourceName, this.classloader));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 535 */       if (obj instanceof LocationInfo) {
/* 536 */         LocationInfo that = (LocationInfo)obj;
/* 537 */         return (this.home.equals(that.home) && this.classloader.equals(that.classloader));
/*     */       } 
/* 539 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 544 */       return this.home.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 549 */       return this.home.toString();
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
/*     */   @VisibleForTesting
/*     */   static ImmutableSet<File> getClassPathFromManifest(File jarFile, Manifest manifest) {
/* 562 */     if (manifest == null) {
/* 563 */       return ImmutableSet.of();
/*     */     }
/* 565 */     ImmutableSet.Builder<File> builder = ImmutableSet.builder();
/*     */     
/* 567 */     String classpathAttribute = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH.toString());
/* 568 */     if (classpathAttribute != null) {
/* 569 */       for (String path : CLASS_PATH_ATTRIBUTE_SEPARATOR.split(classpathAttribute)) {
/*     */         URL url;
/*     */         try {
/* 572 */           url = getClassPathEntry(jarFile, path);
/* 573 */         } catch (MalformedURLException e) {
/*     */           
/* 575 */           String.valueOf(path); logger.warning((String.valueOf(path).length() != 0) ? "Invalid Class-Path entry: ".concat(String.valueOf(path)) : new String("Invalid Class-Path entry: "));
/*     */           continue;
/*     */         } 
/* 578 */         if (url.getProtocol().equals("file")) {
/* 579 */           builder.add(toFile(url));
/*     */         }
/*     */       } 
/*     */     }
/* 583 */     return builder.build();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static ImmutableMap<File, ClassLoader> getClassPathEntries(ClassLoader classloader) {
/* 588 */     LinkedHashMap<File, ClassLoader> entries = Maps.newLinkedHashMap();
/*     */     
/* 590 */     ClassLoader parent = classloader.getParent();
/* 591 */     if (parent != null) {
/* 592 */       entries.putAll((Map<? extends File, ? extends ClassLoader>)getClassPathEntries(parent));
/*     */     }
/* 594 */     for (UnmodifiableIterator<URL> unmodifiableIterator = getClassLoaderUrls(classloader).iterator(); unmodifiableIterator.hasNext(); ) { URL url = unmodifiableIterator.next();
/* 595 */       if (url.getProtocol().equals("file")) {
/* 596 */         File file = toFile(url);
/* 597 */         if (!entries.containsKey(file)) {
/* 598 */           entries.put(file, classloader);
/*     */         }
/*     */       }  }
/*     */     
/* 602 */     return ImmutableMap.copyOf(entries);
/*     */   }
/*     */   
/*     */   private static ImmutableList<URL> getClassLoaderUrls(ClassLoader classloader) {
/* 606 */     if (classloader instanceof URLClassLoader) {
/* 607 */       return ImmutableList.copyOf((Object[])((URLClassLoader)classloader).getURLs());
/*     */     }
/* 609 */     if (classloader.equals(ClassLoader.getSystemClassLoader())) {
/* 610 */       return parseJavaClassPath();
/*     */     }
/* 612 */     return ImmutableList.of();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static ImmutableList<URL> parseJavaClassPath() {
/* 621 */     ImmutableList.Builder<URL> urls = ImmutableList.builder();
/* 622 */     for (String entry : Splitter.on(StandardSystemProperty.PATH_SEPARATOR.value()).split(StandardSystemProperty.JAVA_CLASS_PATH.value())) {
/*     */       try {
/*     */         try {
/* 625 */           urls.add((new File(entry)).toURI().toURL());
/* 626 */         } catch (SecurityException e) {
/* 627 */           urls.add(new URL("file", null, (new File(entry)).getAbsolutePath()));
/*     */         } 
/* 629 */       } catch (MalformedURLException e) {
/* 630 */         String.valueOf(entry); logger.log(Level.WARNING, (String.valueOf(entry).length() != 0) ? "malformed classpath entry: ".concat(String.valueOf(entry)) : new String("malformed classpath entry: "), e);
/*     */       } 
/*     */     } 
/* 633 */     return urls.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static URL getClassPathEntry(File jarFile, String path) throws MalformedURLException {
/* 644 */     return new URL(jarFile.toURI().toURL(), path);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static String getClassName(String filename) {
/* 649 */     int classNameEnd = filename.length() - ".class".length();
/* 650 */     return filename.substring(0, classNameEnd).replace('/', '.');
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static File toFile(URL url) {
/* 656 */     Preconditions.checkArgument(url.getProtocol().equals("file"));
/*     */     try {
/* 658 */       return new File(url.toURI());
/* 659 */     } catch (URISyntaxException e) {
/* 660 */       return new File(url.getPath());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\reflect\ClassPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */