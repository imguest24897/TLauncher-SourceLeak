/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class FileBackedOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private final int fileThreshold;
/*     */   private final boolean resetOnFinalize;
/*     */   private final ByteSource source;
/*     */   private final File parentDirectory;
/*     */   @GuardedBy("this")
/*     */   private OutputStream out;
/*     */   @GuardedBy("this")
/*     */   private MemoryOutput memory;
/*     */   @GuardedBy("this")
/*     */   private File file;
/*     */   
/*     */   private static class MemoryOutput
/*     */     extends ByteArrayOutputStream
/*     */   {
/*     */     private MemoryOutput() {}
/*     */     
/*     */     byte[] getBuffer() {
/*  71 */       return this.buf;
/*     */     }
/*     */     
/*     */     int getCount() {
/*  75 */       return this.count;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   synchronized File getFile() {
/*  82 */     return this.file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileBackedOutputStream(int fileThreshold) {
/*  92 */     this(fileThreshold, false);
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
/*     */   public FileBackedOutputStream(int fileThreshold, boolean resetOnFinalize) {
/* 104 */     this(fileThreshold, resetOnFinalize, null);
/*     */   }
/*     */ 
/*     */   
/*     */   private FileBackedOutputStream(int fileThreshold, boolean resetOnFinalize, File parentDirectory) {
/* 109 */     this.fileThreshold = fileThreshold;
/* 110 */     this.resetOnFinalize = resetOnFinalize;
/* 111 */     this.parentDirectory = parentDirectory;
/* 112 */     this.memory = new MemoryOutput();
/* 113 */     this.out = this.memory;
/*     */     
/* 115 */     if (resetOnFinalize) {
/* 116 */       this.source = new ByteSource()
/*     */         {
/*     */           public InputStream openStream() throws IOException
/*     */           {
/* 120 */             return FileBackedOutputStream.this.openInputStream();
/*     */           }
/*     */ 
/*     */           
/*     */           protected void finalize() {
/*     */             try {
/* 126 */               FileBackedOutputStream.this.reset();
/* 127 */             } catch (Throwable t) {
/* 128 */               t.printStackTrace(System.err);
/*     */             } 
/*     */           }
/*     */         };
/*     */     } else {
/* 133 */       this.source = new ByteSource()
/*     */         {
/*     */           public InputStream openStream() throws IOException
/*     */           {
/* 137 */             return FileBackedOutputStream.this.openInputStream();
/*     */           }
/*     */         };
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteSource asByteSource() {
/* 149 */     return this.source;
/*     */   }
/*     */   
/*     */   private synchronized InputStream openInputStream() throws IOException {
/* 153 */     if (this.file != null) {
/* 154 */       return new FileInputStream(this.file);
/*     */     }
/* 156 */     return new ByteArrayInputStream(this.memory.getBuffer(), 0, this.memory.getCount());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void reset() throws IOException {
/*     */     try {
/* 168 */       close();
/*     */     } finally {
/* 170 */       if (this.memory == null) {
/* 171 */         this.memory = new MemoryOutput();
/*     */       } else {
/* 173 */         this.memory.reset();
/*     */       } 
/* 175 */       this.out = this.memory;
/* 176 */       if (this.file != null) {
/* 177 */         File deleteMe = this.file;
/* 178 */         this.file = null;
/* 179 */         if (!deleteMe.delete()) {
/* 180 */           String str = String.valueOf(deleteMe); throw new IOException((new StringBuilder(18 + String.valueOf(str).length())).append("Could not delete: ").append(str).toString());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void write(int b) throws IOException {
/* 188 */     update(1);
/* 189 */     this.out.write(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void write(byte[] b) throws IOException {
/* 194 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void write(byte[] b, int off, int len) throws IOException {
/* 199 */     update(len);
/* 200 */     this.out.write(b, off, len);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void close() throws IOException {
/* 205 */     this.out.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void flush() throws IOException {
/* 210 */     this.out.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   private void update(int len) throws IOException {
/* 219 */     if (this.file == null && this.memory.getCount() + len > this.fileThreshold) {
/* 220 */       File temp = File.createTempFile("FileBackedOutputStream", null, this.parentDirectory);
/* 221 */       if (this.resetOnFinalize)
/*     */       {
/*     */         
/* 224 */         temp.deleteOnExit();
/*     */       }
/* 226 */       FileOutputStream transfer = new FileOutputStream(temp);
/* 227 */       transfer.write(this.memory.getBuffer(), 0, this.memory.getCount());
/* 228 */       transfer.flush();
/*     */ 
/*     */       
/* 231 */       this.out = transfer;
/* 232 */       this.file = temp;
/* 233 */       this.memory = null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\io\FileBackedOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */