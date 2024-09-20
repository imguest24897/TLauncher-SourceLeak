/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutput;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Arrays;
/*     */ import java.util.Queue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class ByteStreams
/*     */ {
/*     */   private static final int BUFFER_SIZE = 8192;
/*     */   private static final int ZERO_COPY_CHUNK_SIZE = 524288;
/*     */   private static final int MAX_ARRAY_LEN = 2147483639;
/*     */   private static final int TO_BYTE_ARRAY_DEQUE_SIZE = 20;
/*     */   
/*     */   static byte[] createBuffer() {
/*  59 */     return new byte[8192];
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static long copy(InputStream from, OutputStream to) throws IOException {
/* 104 */     Preconditions.checkNotNull(from);
/* 105 */     Preconditions.checkNotNull(to);
/* 106 */     byte[] buf = createBuffer();
/* 107 */     long total = 0L;
/*     */     while (true) {
/* 109 */       int r = from.read(buf);
/* 110 */       if (r == -1) {
/*     */         break;
/*     */       }
/* 113 */       to.write(buf, 0, r);
/* 114 */       total += r;
/*     */     } 
/* 116 */     return total;
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
/*     */   @CanIgnoreReturnValue
/*     */   public static long copy(ReadableByteChannel from, WritableByteChannel to) throws IOException {
/* 130 */     Preconditions.checkNotNull(from);
/* 131 */     Preconditions.checkNotNull(to);
/* 132 */     if (from instanceof FileChannel) {
/* 133 */       FileChannel sourceChannel = (FileChannel)from;
/* 134 */       long oldPosition = sourceChannel.position();
/* 135 */       long position = oldPosition;
/*     */       
/*     */       while (true) {
/* 138 */         long copied = sourceChannel.transferTo(position, 524288L, to);
/* 139 */         position += copied;
/* 140 */         sourceChannel.position(position);
/* 141 */         if (copied <= 0L && position >= sourceChannel.size())
/* 142 */           return position - oldPosition; 
/*     */       } 
/*     */     } 
/* 145 */     ByteBuffer buf = ByteBuffer.wrap(createBuffer());
/* 146 */     long total = 0L;
/* 147 */     while (from.read(buf) != -1) {
/* 148 */       Java8Compatibility.flip(buf);
/* 149 */       while (buf.hasRemaining()) {
/* 150 */         total += to.write(buf);
/*     */       }
/* 152 */       Java8Compatibility.clear(buf);
/*     */     } 
/* 154 */     return total;
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
/*     */   private static byte[] toByteArrayInternal(InputStream in, Queue<byte[]> bufs, int totalLen) throws IOException {
/* 173 */     int bufSize = 8192;
/* 174 */     for (; totalLen < 2147483639; 
/* 175 */       bufSize = IntMath.saturatedMultiply(bufSize, 2)) {
/* 176 */       byte[] buf = new byte[Math.min(bufSize, 2147483639 - totalLen)];
/* 177 */       bufs.add(buf);
/* 178 */       int off = 0;
/* 179 */       while (off < buf.length) {
/*     */         
/* 181 */         int r = in.read(buf, off, buf.length - off);
/* 182 */         if (r == -1) {
/* 183 */           return combineBuffers(bufs, totalLen);
/*     */         }
/* 185 */         off += r;
/* 186 */         totalLen += r;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 191 */     if (in.read() == -1)
/*     */     {
/* 193 */       return combineBuffers(bufs, 2147483639);
/*     */     }
/* 195 */     throw new OutOfMemoryError("input is too large to fit in a byte array");
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] combineBuffers(Queue<byte[]> bufs, int totalLen) {
/* 200 */     byte[] result = new byte[totalLen];
/* 201 */     int remaining = totalLen;
/* 202 */     while (remaining > 0) {
/* 203 */       byte[] buf = bufs.remove();
/* 204 */       int bytesToCopy = Math.min(remaining, buf.length);
/* 205 */       int resultOffset = totalLen - remaining;
/* 206 */       System.arraycopy(buf, 0, result, resultOffset, bytesToCopy);
/* 207 */       remaining -= bytesToCopy;
/*     */     } 
/* 209 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] toByteArray(InputStream in) throws IOException {
/* 220 */     Preconditions.checkNotNull(in);
/* 221 */     return toByteArrayInternal(in, (Queue)new ArrayDeque<>(20), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static byte[] toByteArray(InputStream in, long expectedSize) throws IOException {
/* 230 */     Preconditions.checkArgument((expectedSize >= 0L), "expectedSize (%s) must be non-negative", expectedSize);
/* 231 */     if (expectedSize > 2147483639L) {
/* 232 */       throw new OutOfMemoryError((new StringBuilder(62)).append(expectedSize).append(" bytes is too large to fit in a byte array").toString());
/*     */     }
/*     */     
/* 235 */     byte[] bytes = new byte[(int)expectedSize];
/* 236 */     int remaining = (int)expectedSize;
/*     */     
/* 238 */     while (remaining > 0) {
/* 239 */       int off = (int)expectedSize - remaining;
/* 240 */       int read = in.read(bytes, off, remaining);
/* 241 */       if (read == -1)
/*     */       {
/*     */         
/* 244 */         return Arrays.copyOf(bytes, off);
/*     */       }
/* 246 */       remaining -= read;
/*     */     } 
/*     */ 
/*     */     
/* 250 */     int b = in.read();
/* 251 */     if (b == -1) {
/* 252 */       return bytes;
/*     */     }
/*     */ 
/*     */     
/* 256 */     Queue<byte[]> bufs = (Queue)new ArrayDeque<>(22);
/* 257 */     bufs.add(bytes);
/* 258 */     bufs.add(new byte[] { (byte)b });
/* 259 */     return toByteArrayInternal(in, bufs, bytes.length + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   @Beta
/*     */   public static long exhaust(InputStream in) throws IOException {
/* 271 */     long total = 0L;
/*     */     
/* 273 */     byte[] buf = createBuffer(); long read;
/* 274 */     while ((read = in.read(buf)) != -1L) {
/* 275 */       total += read;
/*     */     }
/* 277 */     return total;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static ByteArrayDataInput newDataInput(byte[] bytes) {
/* 286 */     return newDataInput(new ByteArrayInputStream(bytes));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static ByteArrayDataInput newDataInput(byte[] bytes, int start) {
/* 298 */     Preconditions.checkPositionIndex(start, bytes.length);
/* 299 */     return newDataInput(new ByteArrayInputStream(bytes, start, bytes.length - start));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static ByteArrayDataInput newDataInput(ByteArrayInputStream byteArrayInputStream) {
/* 311 */     return new ByteArrayDataInputStream((ByteArrayInputStream)Preconditions.checkNotNull(byteArrayInputStream));
/*     */   }
/*     */   
/*     */   private static class ByteArrayDataInputStream implements ByteArrayDataInput {
/*     */     final DataInput input;
/*     */     
/*     */     ByteArrayDataInputStream(ByteArrayInputStream byteArrayInputStream) {
/* 318 */       this.input = new DataInputStream(byteArrayInputStream);
/*     */     }
/*     */ 
/*     */     
/*     */     public void readFully(byte[] b) {
/*     */       try {
/* 324 */         this.input.readFully(b);
/* 325 */       } catch (IOException e) {
/* 326 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void readFully(byte[] b, int off, int len) {
/*     */       try {
/* 333 */         this.input.readFully(b, off, len);
/* 334 */       } catch (IOException e) {
/* 335 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int skipBytes(int n) {
/*     */       try {
/* 342 */         return this.input.skipBytes(n);
/* 343 */       } catch (IOException e) {
/* 344 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean readBoolean() {
/*     */       try {
/* 351 */         return this.input.readBoolean();
/* 352 */       } catch (IOException e) {
/* 353 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public byte readByte() {
/*     */       try {
/* 360 */         return this.input.readByte();
/* 361 */       } catch (EOFException e) {
/* 362 */         throw new IllegalStateException(e);
/* 363 */       } catch (IOException impossible) {
/* 364 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int readUnsignedByte() {
/*     */       try {
/* 371 */         return this.input.readUnsignedByte();
/* 372 */       } catch (IOException e) {
/* 373 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public short readShort() {
/*     */       try {
/* 380 */         return this.input.readShort();
/* 381 */       } catch (IOException e) {
/* 382 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int readUnsignedShort() {
/*     */       try {
/* 389 */         return this.input.readUnsignedShort();
/* 390 */       } catch (IOException e) {
/* 391 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public char readChar() {
/*     */       try {
/* 398 */         return this.input.readChar();
/* 399 */       } catch (IOException e) {
/* 400 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int readInt() {
/*     */       try {
/* 407 */         return this.input.readInt();
/* 408 */       } catch (IOException e) {
/* 409 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public long readLong() {
/*     */       try {
/* 416 */         return this.input.readLong();
/* 417 */       } catch (IOException e) {
/* 418 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public float readFloat() {
/*     */       try {
/* 425 */         return this.input.readFloat();
/* 426 */       } catch (IOException e) {
/* 427 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public double readDouble() {
/*     */       try {
/* 434 */         return this.input.readDouble();
/* 435 */       } catch (IOException e) {
/* 436 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String readLine() {
/*     */       try {
/* 443 */         return this.input.readLine();
/* 444 */       } catch (IOException e) {
/* 445 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String readUTF() {
/*     */       try {
/* 452 */         return this.input.readUTF();
/* 453 */       } catch (IOException e) {
/* 454 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static ByteArrayDataOutput newDataOutput() {
/* 462 */     return newDataOutput(new ByteArrayOutputStream());
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
/*     */   @Beta
/*     */   public static ByteArrayDataOutput newDataOutput(int size) {
/* 475 */     if (size < 0) {
/* 476 */       throw new IllegalArgumentException(String.format("Invalid size: %s", new Object[] { Integer.valueOf(size) }));
/*     */     }
/* 478 */     return newDataOutput(new ByteArrayOutputStream(size));
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
/*     */   public static ByteArrayDataOutput newDataOutput(ByteArrayOutputStream byteArrayOutputStream) {
/* 495 */     return new ByteArrayDataOutputStream((ByteArrayOutputStream)Preconditions.checkNotNull(byteArrayOutputStream));
/*     */   }
/*     */   
/*     */   private static class ByteArrayDataOutputStream
/*     */     implements ByteArrayDataOutput {
/*     */     final DataOutput output;
/*     */     final ByteArrayOutputStream byteArrayOutputStream;
/*     */     
/*     */     ByteArrayDataOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
/* 504 */       this.byteArrayOutputStream = byteArrayOutputStream;
/* 505 */       this.output = new DataOutputStream(byteArrayOutputStream);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(int b) {
/*     */       try {
/* 511 */         this.output.write(b);
/* 512 */       } catch (IOException impossible) {
/* 513 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b) {
/*     */       try {
/* 520 */         this.output.write(b);
/* 521 */       } catch (IOException impossible) {
/* 522 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b, int off, int len) {
/*     */       try {
/* 529 */         this.output.write(b, off, len);
/* 530 */       } catch (IOException impossible) {
/* 531 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeBoolean(boolean v) {
/*     */       try {
/* 538 */         this.output.writeBoolean(v);
/* 539 */       } catch (IOException impossible) {
/* 540 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeByte(int v) {
/*     */       try {
/* 547 */         this.output.writeByte(v);
/* 548 */       } catch (IOException impossible) {
/* 549 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeBytes(String s) {
/*     */       try {
/* 556 */         this.output.writeBytes(s);
/* 557 */       } catch (IOException impossible) {
/* 558 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeChar(int v) {
/*     */       try {
/* 565 */         this.output.writeChar(v);
/* 566 */       } catch (IOException impossible) {
/* 567 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeChars(String s) {
/*     */       try {
/* 574 */         this.output.writeChars(s);
/* 575 */       } catch (IOException impossible) {
/* 576 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeDouble(double v) {
/*     */       try {
/* 583 */         this.output.writeDouble(v);
/* 584 */       } catch (IOException impossible) {
/* 585 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeFloat(float v) {
/*     */       try {
/* 592 */         this.output.writeFloat(v);
/* 593 */       } catch (IOException impossible) {
/* 594 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeInt(int v) {
/*     */       try {
/* 601 */         this.output.writeInt(v);
/* 602 */       } catch (IOException impossible) {
/* 603 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeLong(long v) {
/*     */       try {
/* 610 */         this.output.writeLong(v);
/* 611 */       } catch (IOException impossible) {
/* 612 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeShort(int v) {
/*     */       try {
/* 619 */         this.output.writeShort(v);
/* 620 */       } catch (IOException impossible) {
/* 621 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeUTF(String s) {
/*     */       try {
/* 628 */         this.output.writeUTF(s);
/* 629 */       } catch (IOException impossible) {
/* 630 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] toByteArray() {
/* 636 */       return this.byteArrayOutputStream.toByteArray();
/*     */     }
/*     */   }
/*     */   
/* 640 */   private static final OutputStream NULL_OUTPUT_STREAM = new OutputStream()
/*     */     {
/*     */       public void write(int b) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void write(byte[] b) {
/* 649 */         Preconditions.checkNotNull(b);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void write(byte[] b, int off, int len) {
/* 655 */         Preconditions.checkNotNull(b);
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 660 */         return "ByteStreams.nullOutputStream()";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static OutputStream nullOutputStream() {
/* 671 */     return NULL_OUTPUT_STREAM;
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
/*     */   @Beta
/*     */   public static InputStream limit(InputStream in, long limit) {
/* 684 */     return new LimitedInputStream(in, limit);
/*     */   }
/*     */   
/*     */   private static final class LimitedInputStream
/*     */     extends FilterInputStream {
/*     */     private long left;
/* 690 */     private long mark = -1L;
/*     */     
/*     */     LimitedInputStream(InputStream in, long limit) {
/* 693 */       super(in);
/* 694 */       Preconditions.checkNotNull(in);
/* 695 */       Preconditions.checkArgument((limit >= 0L), "limit must be non-negative");
/* 696 */       this.left = limit;
/*     */     }
/*     */ 
/*     */     
/*     */     public int available() throws IOException {
/* 701 */       return (int)Math.min(this.in.available(), this.left);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized void mark(int readLimit) {
/* 707 */       this.in.mark(readLimit);
/* 708 */       this.mark = this.left;
/*     */     }
/*     */ 
/*     */     
/*     */     public int read() throws IOException {
/* 713 */       if (this.left == 0L) {
/* 714 */         return -1;
/*     */       }
/*     */       
/* 717 */       int result = this.in.read();
/* 718 */       if (result != -1) {
/* 719 */         this.left--;
/*     */       }
/* 721 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public int read(byte[] b, int off, int len) throws IOException {
/* 726 */       if (this.left == 0L) {
/* 727 */         return -1;
/*     */       }
/*     */       
/* 730 */       len = (int)Math.min(len, this.left);
/* 731 */       int result = this.in.read(b, off, len);
/* 732 */       if (result != -1) {
/* 733 */         this.left -= result;
/*     */       }
/* 735 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public synchronized void reset() throws IOException {
/* 740 */       if (!this.in.markSupported()) {
/* 741 */         throw new IOException("Mark not supported");
/*     */       }
/* 743 */       if (this.mark == -1L) {
/* 744 */         throw new IOException("Mark not set");
/*     */       }
/*     */       
/* 747 */       this.in.reset();
/* 748 */       this.left = this.mark;
/*     */     }
/*     */ 
/*     */     
/*     */     public long skip(long n) throws IOException {
/* 753 */       n = Math.min(n, this.left);
/* 754 */       long skipped = this.in.skip(n);
/* 755 */       this.left -= skipped;
/* 756 */       return skipped;
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
/*     */   public static void readFully(InputStream in, byte[] b) throws IOException {
/* 771 */     readFully(in, b, 0, b.length);
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
/*     */   public static void readFully(InputStream in, byte[] b, int off, int len) throws IOException {
/* 788 */     int read = read(in, b, off, len);
/* 789 */     if (read != len) {
/* 790 */       throw new EOFException((new StringBuilder(81)).append("reached end of stream after reading ").append(read).append(" bytes; ").append(len).append(" bytes expected").toString());
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
/*     */   @Beta
/*     */   public static void skipFully(InputStream in, long n) throws IOException {
/* 806 */     long skipped = skipUpTo(in, n);
/* 807 */     if (skipped < n) {
/* 808 */       throw new EOFException((new StringBuilder(100)).append("reached end of stream after skipping ").append(skipped).append(" bytes; ").append(n).append(" bytes expected").toString());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static long skipUpTo(InputStream in, long n) throws IOException {
/* 819 */     long totalSkipped = 0L;
/*     */     
/* 821 */     byte[] buf = null;
/*     */     
/* 823 */     while (totalSkipped < n) {
/* 824 */       long remaining = n - totalSkipped;
/* 825 */       long skipped = skipSafely(in, remaining);
/*     */       
/* 827 */       if (skipped == 0L) {
/*     */ 
/*     */         
/* 830 */         int skip = (int)Math.min(remaining, 8192L);
/* 831 */         if (buf == null)
/*     */         {
/*     */ 
/*     */           
/* 835 */           buf = new byte[skip];
/*     */         }
/* 837 */         if ((skipped = in.read(buf, 0, skip)) == -1L) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 843 */       totalSkipped += skipped;
/*     */     } 
/*     */     
/* 846 */     return totalSkipped;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long skipSafely(InputStream in, long n) throws IOException {
/* 857 */     int available = in.available();
/* 858 */     return (available == 0) ? 0L : in.skip(Math.min(available, n));
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
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T readBytes(InputStream input, ByteProcessor<T> processor) throws IOException {
/*     */     int read;
/* 873 */     Preconditions.checkNotNull(input);
/* 874 */     Preconditions.checkNotNull(processor);
/*     */     
/* 876 */     byte[] buf = createBuffer();
/*     */     
/*     */     do {
/* 879 */       read = input.read(buf);
/* 880 */     } while (read != -1 && processor.processBytes(buf, 0, read));
/* 881 */     return processor.getResult();
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
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   public static int read(InputStream in, byte[] b, int off, int len) throws IOException {
/* 913 */     Preconditions.checkNotNull(in);
/* 914 */     Preconditions.checkNotNull(b);
/* 915 */     if (len < 0) {
/* 916 */       throw new IndexOutOfBoundsException(String.format("len (%s) cannot be negative", new Object[] { Integer.valueOf(len) }));
/*     */     }
/* 918 */     Preconditions.checkPositionIndexes(off, off + len, b.length);
/* 919 */     int total = 0;
/* 920 */     while (total < len) {
/* 921 */       int result = in.read(b, off + total, len - total);
/* 922 */       if (result == -1) {
/*     */         break;
/*     */       }
/* 925 */       total += result;
/*     */     } 
/* 927 */     return total;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\io\ByteStreams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */