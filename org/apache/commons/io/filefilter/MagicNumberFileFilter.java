/*     */ package org.apache.commons.io.filefilter;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.Arrays;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MagicNumberFileFilter
/*     */   extends AbstractFileFilter
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -547733176983104172L;
/*     */   private final byte[] magicNumbers;
/*     */   private final long byteOffset;
/*     */   
/*     */   public MagicNumberFileFilter(byte[] magicNumber) {
/* 138 */     this(magicNumber, 0L);
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
/*     */   public MagicNumberFileFilter(byte[] magicNumber, long offset) {
/* 166 */     if (magicNumber == null) {
/* 167 */       throw new IllegalArgumentException("The magic number cannot be null");
/*     */     }
/* 169 */     if (magicNumber.length == 0) {
/* 170 */       throw new IllegalArgumentException("The magic number must contain at least one byte");
/*     */     }
/* 172 */     if (offset < 0L) {
/* 173 */       throw new IllegalArgumentException("The offset cannot be negative");
/*     */     }
/*     */     
/* 176 */     this.magicNumbers = IOUtils.byteArray(magicNumber.length);
/* 177 */     System.arraycopy(magicNumber, 0, this.magicNumbers, 0, magicNumber.length);
/* 178 */     this.byteOffset = offset;
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
/*     */   public MagicNumberFileFilter(String magicNumber) {
/* 203 */     this(magicNumber, 0L);
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
/*     */   public MagicNumberFileFilter(String magicNumber, long offset) {
/* 227 */     if (magicNumber == null) {
/* 228 */       throw new IllegalArgumentException("The magic number cannot be null");
/*     */     }
/* 230 */     if (magicNumber.isEmpty()) {
/* 231 */       throw new IllegalArgumentException("The magic number must contain at least one byte");
/*     */     }
/* 233 */     if (offset < 0L) {
/* 234 */       throw new IllegalArgumentException("The offset cannot be negative");
/*     */     }
/*     */     
/* 237 */     this.magicNumbers = magicNumber.getBytes(Charset.defaultCharset());
/*     */     
/* 239 */     this.byteOffset = offset;
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
/*     */   public boolean accept(File file) {
/* 260 */     if (file != null && file.isFile() && file.canRead())
/*     */     {
/* 262 */       try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
/* 263 */         byte[] fileBytes = IOUtils.byteArray(this.magicNumbers.length);
/* 264 */         randomAccessFile.seek(this.byteOffset);
/* 265 */         int read = randomAccessFile.read(fileBytes);
/* 266 */         if (read != this.magicNumbers.length) {
/* 267 */           return false;
/*     */         }
/* 269 */         return Arrays.equals(this.magicNumbers, fileBytes);
/*     */       
/*     */       }
/* 272 */       catch (IOException iOException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 277 */     return false;
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
/*     */   public FileVisitResult accept(Path file, BasicFileAttributes attributes) {
/* 298 */     if (file != null && Files.isRegularFile(file, new java.nio.file.LinkOption[0]) && Files.isReadable(file))
/*     */     {
/* 300 */       try (FileChannel fileChannel = FileChannel.open(file, new java.nio.file.OpenOption[0])) {
/* 301 */         ByteBuffer byteBuffer = ByteBuffer.allocate(this.magicNumbers.length);
/* 302 */         int read = fileChannel.read(byteBuffer);
/* 303 */         if (read != this.magicNumbers.length) {
/* 304 */           return FileVisitResult.TERMINATE;
/*     */         }
/* 306 */         return toFileVisitResult(Arrays.equals(this.magicNumbers, byteBuffer.array()), file);
/*     */       
/*     */       }
/* 309 */       catch (IOException iOException) {}
/*     */     }
/*     */ 
/*     */     
/* 313 */     return FileVisitResult.TERMINATE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 324 */     StringBuilder builder = new StringBuilder(super.toString());
/* 325 */     builder.append("(");
/* 326 */     builder.append(new String(this.magicNumbers, Charset.defaultCharset()));
/*     */     
/* 328 */     builder.append(",");
/* 329 */     builder.append(this.byteOffset);
/* 330 */     builder.append(")");
/* 331 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\io\filefilter\MagicNumberFileFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */