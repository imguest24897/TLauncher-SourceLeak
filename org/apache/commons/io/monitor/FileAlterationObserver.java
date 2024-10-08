/*     */ package org.apache.commons.io.monitor;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.IOCase;
/*     */ import org.apache.commons.io.comparator.NameFileComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileAlterationObserver
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1185122225658782848L;
/* 125 */   private final List<FileAlterationListener> listeners = new CopyOnWriteArrayList<>();
/*     */ 
/*     */   
/*     */   private final FileEntry rootEntry;
/*     */   
/*     */   private final FileFilter fileFilter;
/*     */   
/*     */   private final Comparator<File> comparator;
/*     */ 
/*     */   
/*     */   public FileAlterationObserver(String directoryName) {
/* 136 */     this(new File(directoryName));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileAlterationObserver(String directoryName, FileFilter fileFilter) {
/* 146 */     this(new File(directoryName), fileFilter);
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
/*     */   public FileAlterationObserver(String directoryName, FileFilter fileFilter, IOCase caseSensitivity) {
/* 159 */     this(new File(directoryName), fileFilter, caseSensitivity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileAlterationObserver(File directory) {
/* 168 */     this(directory, (FileFilter)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileAlterationObserver(File directory, FileFilter fileFilter) {
/* 178 */     this(directory, fileFilter, (IOCase)null);
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
/*     */   public FileAlterationObserver(File directory, FileFilter fileFilter, IOCase caseSensitivity) {
/* 190 */     this(new FileEntry(directory), fileFilter, caseSensitivity);
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
/*     */   protected FileAlterationObserver(FileEntry rootEntry, FileFilter fileFilter, IOCase caseSensitivity) {
/* 203 */     if (rootEntry == null) {
/* 204 */       throw new IllegalArgumentException("Root entry is missing");
/*     */     }
/* 206 */     if (rootEntry.getFile() == null) {
/* 207 */       throw new IllegalArgumentException("Root directory is missing");
/*     */     }
/* 209 */     this.rootEntry = rootEntry;
/* 210 */     this.fileFilter = fileFilter;
/* 211 */     if (caseSensitivity == null || caseSensitivity.equals(IOCase.SYSTEM)) {
/* 212 */       this.comparator = NameFileComparator.NAME_SYSTEM_COMPARATOR;
/* 213 */     } else if (caseSensitivity.equals(IOCase.INSENSITIVE)) {
/* 214 */       this.comparator = NameFileComparator.NAME_INSENSITIVE_COMPARATOR;
/*     */     } else {
/* 216 */       this.comparator = NameFileComparator.NAME_COMPARATOR;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getDirectory() {
/* 226 */     return this.rootEntry.getFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileFilter getFileFilter() {
/* 236 */     return this.fileFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addListener(FileAlterationListener listener) {
/* 245 */     if (listener != null) {
/* 246 */       this.listeners.add(listener);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeListener(FileAlterationListener listener) {
/* 256 */     if (listener != null) {
/* 257 */       while (this.listeners.remove(listener));
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
/*     */   public Iterable<FileAlterationListener> getListeners() {
/* 269 */     return this.listeners;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize() throws Exception {
/* 279 */     this.rootEntry.refresh(this.rootEntry.getFile());
/* 280 */     FileEntry[] children = doListFiles(this.rootEntry.getFile(), this.rootEntry);
/* 281 */     this.rootEntry.setChildren(children);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkAndNotify() {
/* 300 */     for (FileAlterationListener listener : this.listeners) {
/* 301 */       listener.onStart(this);
/*     */     }
/*     */ 
/*     */     
/* 305 */     File rootFile = this.rootEntry.getFile();
/* 306 */     if (rootFile.exists()) {
/* 307 */       checkAndNotify(this.rootEntry, this.rootEntry.getChildren(), listFiles(rootFile));
/* 308 */     } else if (this.rootEntry.isExists()) {
/* 309 */       checkAndNotify(this.rootEntry, this.rootEntry.getChildren(), FileUtils.EMPTY_FILE_ARRAY);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 315 */     for (FileAlterationListener listener : this.listeners) {
/* 316 */       listener.onStop(this);
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
/*     */   private void checkAndNotify(FileEntry parent, FileEntry[] previous, File[] files) {
/* 328 */     int c = 0;
/* 329 */     FileEntry[] current = (files.length > 0) ? new FileEntry[files.length] : FileEntry.EMPTY_FILE_ENTRY_ARRAY;
/* 330 */     for (FileEntry entry : previous) {
/* 331 */       while (c < files.length && this.comparator.compare(entry.getFile(), files[c]) > 0) {
/* 332 */         current[c] = createFileEntry(parent, files[c]);
/* 333 */         doCreate(current[c]);
/* 334 */         c++;
/*     */       } 
/* 336 */       if (c < files.length && this.comparator.compare(entry.getFile(), files[c]) == 0) {
/* 337 */         doMatch(entry, files[c]);
/* 338 */         checkAndNotify(entry, entry.getChildren(), listFiles(files[c]));
/* 339 */         current[c] = entry;
/* 340 */         c++;
/*     */       } else {
/* 342 */         checkAndNotify(entry, entry.getChildren(), FileUtils.EMPTY_FILE_ARRAY);
/* 343 */         doDelete(entry);
/*     */       } 
/*     */     } 
/* 346 */     for (; c < files.length; c++) {
/* 347 */       current[c] = createFileEntry(parent, files[c]);
/* 348 */       doCreate(current[c]);
/*     */     } 
/* 350 */     parent.setChildren(current);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private FileEntry createFileEntry(FileEntry parent, File file) {
/* 361 */     FileEntry entry = parent.newChildInstance(file);
/* 362 */     entry.refresh(file);
/* 363 */     FileEntry[] children = doListFiles(file, entry);
/* 364 */     entry.setChildren(children);
/* 365 */     return entry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private FileEntry[] doListFiles(File file, FileEntry entry) {
/* 375 */     File[] files = listFiles(file);
/* 376 */     FileEntry[] children = (files.length > 0) ? new FileEntry[files.length] : FileEntry.EMPTY_FILE_ENTRY_ARRAY;
/* 377 */     for (int i = 0; i < files.length; i++) {
/* 378 */       children[i] = createFileEntry(entry, files[i]);
/*     */     }
/* 380 */     return children;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doCreate(FileEntry entry) {
/* 389 */     for (FileAlterationListener listener : this.listeners) {
/* 390 */       if (entry.isDirectory()) {
/* 391 */         listener.onDirectoryCreate(entry.getFile()); continue;
/*     */       } 
/* 393 */       listener.onFileCreate(entry.getFile());
/*     */     } 
/*     */     
/* 396 */     FileEntry[] children = entry.getChildren();
/* 397 */     for (FileEntry aChildren : children) {
/* 398 */       doCreate(aChildren);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doMatch(FileEntry entry, File file) {
/* 409 */     if (entry.refresh(file)) {
/* 410 */       for (FileAlterationListener listener : this.listeners) {
/* 411 */         if (entry.isDirectory()) {
/* 412 */           listener.onDirectoryChange(file); continue;
/*     */         } 
/* 414 */         listener.onFileChange(file);
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
/*     */   private void doDelete(FileEntry entry) {
/* 426 */     for (FileAlterationListener listener : this.listeners) {
/* 427 */       if (entry.isDirectory()) {
/* 428 */         listener.onDirectoryDelete(entry.getFile()); continue;
/*     */       } 
/* 430 */       listener.onFileDelete(entry.getFile());
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
/*     */   private File[] listFiles(File file) {
/* 443 */     File[] children = null;
/* 444 */     if (file.isDirectory()) {
/* 445 */       children = (this.fileFilter == null) ? file.listFiles() : file.listFiles(this.fileFilter);
/*     */     }
/* 447 */     if (children == null) {
/* 448 */       children = FileUtils.EMPTY_FILE_ARRAY;
/*     */     }
/* 450 */     if (this.comparator != null && children.length > 1) {
/* 451 */       Arrays.sort(children, this.comparator);
/*     */     }
/* 453 */     return children;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 463 */     StringBuilder builder = new StringBuilder();
/* 464 */     builder.append(getClass().getSimpleName());
/* 465 */     builder.append("[file='");
/* 466 */     builder.append(getDirectory().getPath());
/* 467 */     builder.append('\'');
/* 468 */     if (this.fileFilter != null) {
/* 469 */       builder.append(", ");
/* 470 */       builder.append(this.fileFilter.toString());
/*     */     } 
/* 472 */     builder.append(", listeners=");
/* 473 */     builder.append(this.listeners.size());
/* 474 */     builder.append("]");
/* 475 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\io\monitor\FileAlterationObserver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */