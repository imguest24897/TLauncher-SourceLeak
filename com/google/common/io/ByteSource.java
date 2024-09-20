/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.hash.Funnels;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.google.common.hash.Hasher;
/*     */ import com.google.common.hash.PrimitiveSink;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ByteSource
/*     */ {
/*     */   public CharSource asCharSource(Charset charset) {
/*  79 */     return new AsCharSource(charset);
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
/*     */   public abstract InputStream openStream() throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream openBufferedStream() throws IOException {
/* 105 */     InputStream in = openStream();
/* 106 */     return (in instanceof BufferedInputStream) ? 
/* 107 */       in : 
/* 108 */       new BufferedInputStream(in);
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
/*     */   public ByteSource slice(long offset, long length) {
/* 121 */     return new SlicedByteSource(offset, length);
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
/*     */   public boolean isEmpty() throws IOException {
/* 138 */     Optional<Long> sizeIfKnown = sizeIfKnown();
/* 139 */     if (sizeIfKnown.isPresent()) {
/* 140 */       return (((Long)sizeIfKnown.get()).longValue() == 0L);
/*     */     }
/* 142 */     Closer closer = Closer.create();
/*     */     try {
/* 144 */       InputStream in = closer.<InputStream>register(openStream());
/* 145 */       return (in.read() == -1);
/* 146 */     } catch (Throwable e) {
/* 147 */       throw closer.rethrow(e);
/*     */     } finally {
/* 149 */       closer.close();
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
/*     */   @Beta
/*     */   public Optional<Long> sizeIfKnown() {
/* 169 */     return Optional.absent();
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
/*     */   public long size() throws IOException {
/* 192 */     Optional<Long> sizeIfKnown = sizeIfKnown();
/* 193 */     if (sizeIfKnown.isPresent()) {
/* 194 */       return ((Long)sizeIfKnown.get()).longValue();
/*     */     }
/*     */     
/* 197 */     Closer closer = Closer.create();
/*     */     try {
/* 199 */       InputStream in = closer.<InputStream>register(openStream());
/* 200 */       return countBySkipping(in);
/* 201 */     } catch (IOException iOException) {
/*     */     
/*     */     } finally {
/* 204 */       closer.close();
/*     */     } 
/*     */     
/* 207 */     closer = Closer.create();
/*     */     try {
/* 209 */       InputStream in = closer.<InputStream>register(openStream());
/* 210 */       return ByteStreams.exhaust(in);
/* 211 */     } catch (Throwable e) {
/* 212 */       throw closer.rethrow(e);
/*     */     } finally {
/* 214 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private long countBySkipping(InputStream in) throws IOException {
/* 220 */     long count = 0L;
/*     */     long skipped;
/* 222 */     while ((skipped = ByteStreams.skipUpTo(in, 2147483647L)) > 0L) {
/* 223 */       count += skipped;
/*     */     }
/* 225 */     return count;
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
/*     */   @CanIgnoreReturnValue
/*     */   public long copyTo(OutputStream output) throws IOException {
/* 238 */     Preconditions.checkNotNull(output);
/*     */     
/* 240 */     Closer closer = Closer.create();
/*     */     try {
/* 242 */       InputStream in = closer.<InputStream>register(openStream());
/* 243 */       return ByteStreams.copy(in, output);
/* 244 */     } catch (Throwable e) {
/* 245 */       throw closer.rethrow(e);
/*     */     } finally {
/* 247 */       closer.close();
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
/*     */   @CanIgnoreReturnValue
/*     */   public long copyTo(ByteSink sink) throws IOException {
/* 260 */     Preconditions.checkNotNull(sink);
/*     */     
/* 262 */     Closer closer = Closer.create();
/*     */     try {
/* 264 */       InputStream in = closer.<InputStream>register(openStream());
/* 265 */       OutputStream out = closer.<OutputStream>register(sink.openStream());
/* 266 */       return ByteStreams.copy(in, out);
/* 267 */     } catch (Throwable e) {
/* 268 */       throw closer.rethrow(e);
/*     */     } finally {
/* 270 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] read() throws IOException {
/* 280 */     Closer closer = Closer.create();
/*     */     try {
/* 282 */       InputStream in = closer.<InputStream>register(openStream());
/* 283 */       Optional<Long> size = sizeIfKnown();
/* 284 */       return size.isPresent() ? 
/* 285 */         ByteStreams.toByteArray(in, ((Long)size.get()).longValue()) : 
/* 286 */         ByteStreams.toByteArray(in);
/* 287 */     } catch (Throwable e) {
/* 288 */       throw closer.rethrow(e);
/*     */     } finally {
/* 290 */       closer.close();
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
/*     */   @CanIgnoreReturnValue
/*     */   public <T> T read(ByteProcessor<T> processor) throws IOException {
/* 306 */     Preconditions.checkNotNull(processor);
/*     */     
/* 308 */     Closer closer = Closer.create();
/*     */     try {
/* 310 */       InputStream in = closer.<InputStream>register(openStream());
/* 311 */       return (T)ByteStreams.readBytes(in, (ByteProcessor)processor);
/* 312 */     } catch (Throwable e) {
/* 313 */       throw closer.rethrow(e);
/*     */     } finally {
/* 315 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HashCode hash(HashFunction hashFunction) throws IOException {
/* 325 */     Hasher hasher = hashFunction.newHasher();
/* 326 */     copyTo(Funnels.asOutputStream((PrimitiveSink)hasher));
/* 327 */     return hasher.hash();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contentEquals(ByteSource other) throws IOException {
/* 337 */     Preconditions.checkNotNull(other);
/*     */     
/* 339 */     byte[] buf1 = ByteStreams.createBuffer();
/* 340 */     byte[] buf2 = ByteStreams.createBuffer();
/*     */     
/* 342 */     Closer closer = Closer.create();
/*     */     try {
/* 344 */       InputStream in1 = closer.<InputStream>register(openStream());
/* 345 */       InputStream in2 = closer.<InputStream>register(other.openStream());
/*     */       while (true) {
/* 347 */         int read1 = ByteStreams.read(in1, buf1, 0, buf1.length);
/* 348 */         int read2 = ByteStreams.read(in2, buf2, 0, buf2.length);
/* 349 */         if (read1 != read2 || !Arrays.equals(buf1, buf2))
/* 350 */           return false; 
/* 351 */         if (read1 != buf1.length) {
/* 352 */           return true;
/*     */         }
/*     */       } 
/* 355 */     } catch (Throwable e) {
/* 356 */       throw closer.rethrow(e);
/*     */     } finally {
/* 358 */       closer.close();
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
/*     */   public static ByteSource concat(Iterable<? extends ByteSource> sources) {
/* 374 */     return new ConcatenatedByteSource(sources);
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
/*     */   public static ByteSource concat(Iterator<? extends ByteSource> sources) {
/* 396 */     return concat((Iterable<? extends ByteSource>)ImmutableList.copyOf(sources));
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
/*     */   public static ByteSource concat(ByteSource... sources) {
/* 412 */     return concat((Iterable<? extends ByteSource>)ImmutableList.copyOf((Object[])sources));
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
/*     */   public static ByteSource wrap(byte[] b) {
/* 427 */     return new ByteArrayByteSource(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteSource empty() {
/* 436 */     return EmptyByteSource.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   class AsCharSource
/*     */     extends CharSource
/*     */   {
/*     */     final Charset charset;
/*     */ 
/*     */     
/*     */     AsCharSource(Charset charset) {
/* 447 */       this.charset = (Charset)Preconditions.checkNotNull(charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteSource asByteSource(Charset charset) {
/* 452 */       if (charset.equals(this.charset)) {
/* 453 */         return ByteSource.this;
/*     */       }
/* 455 */       return super.asByteSource(charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public Reader openStream() throws IOException {
/* 460 */       return new InputStreamReader(ByteSource.this.openStream(), this.charset);
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
/*     */     public String read() throws IOException {
/* 472 */       return new String(ByteSource.this.read(), this.charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 477 */       String str1 = ByteSource.this.toString(), str2 = String.valueOf(this.charset); return (new StringBuilder(15 + String.valueOf(str1).length() + String.valueOf(str2).length())).append(str1).append(".asCharSource(").append(str2).append(")").toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private final class SlicedByteSource
/*     */     extends ByteSource
/*     */   {
/*     */     final long offset;
/*     */     final long length;
/*     */     
/*     */     SlicedByteSource(long offset, long length) {
/* 488 */       Preconditions.checkArgument((offset >= 0L), "offset (%s) may not be negative", offset);
/* 489 */       Preconditions.checkArgument((length >= 0L), "length (%s) may not be negative", length);
/* 490 */       this.offset = offset;
/* 491 */       this.length = length;
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openStream() throws IOException {
/* 496 */       return sliceStream(ByteSource.this.openStream());
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openBufferedStream() throws IOException {
/* 501 */       return sliceStream(ByteSource.this.openBufferedStream());
/*     */     }
/*     */     
/*     */     private InputStream sliceStream(InputStream in) throws IOException {
/* 505 */       if (this.offset > 0L) {
/*     */         long skipped;
/*     */         try {
/* 508 */           skipped = ByteStreams.skipUpTo(in, this.offset);
/* 509 */         } catch (Throwable e) {
/* 510 */           Closer closer = Closer.create();
/* 511 */           closer.register(in);
/*     */           try {
/* 513 */             throw closer.rethrow(e);
/*     */           } finally {
/* 515 */             closer.close();
/*     */           } 
/*     */         } 
/*     */         
/* 519 */         if (skipped < this.offset) {
/*     */           
/* 521 */           in.close();
/* 522 */           return new ByteArrayInputStream(new byte[0]);
/*     */         } 
/*     */       } 
/* 525 */       return ByteStreams.limit(in, this.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteSource slice(long offset, long length) {
/* 530 */       Preconditions.checkArgument((offset >= 0L), "offset (%s) may not be negative", offset);
/* 531 */       Preconditions.checkArgument((length >= 0L), "length (%s) may not be negative", length);
/* 532 */       long maxLength = this.length - offset;
/* 533 */       return (maxLength <= 0L) ? 
/* 534 */         ByteSource.empty() : 
/* 535 */         ByteSource.this.slice(this.offset + offset, Math.min(length, maxLength));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() throws IOException {
/* 540 */       return (this.length == 0L || super.isEmpty());
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Long> sizeIfKnown() {
/* 545 */       Optional<Long> optionalUnslicedSize = ByteSource.this.sizeIfKnown();
/* 546 */       if (optionalUnslicedSize.isPresent()) {
/* 547 */         long unslicedSize = ((Long)optionalUnslicedSize.get()).longValue();
/* 548 */         long off = Math.min(this.offset, unslicedSize);
/* 549 */         return Optional.of(Long.valueOf(Math.min(this.length, unslicedSize - off)));
/*     */       } 
/* 551 */       return Optional.absent();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 556 */       String str = ByteSource.this.toString(); long l1 = this.offset, l2 = this.length; return (new StringBuilder(50 + String.valueOf(str).length())).append(str).append(".slice(").append(l1).append(", ").append(l2).append(")").toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ByteArrayByteSource
/*     */     extends ByteSource {
/*     */     final byte[] bytes;
/*     */     final int offset;
/*     */     final int length;
/*     */     
/*     */     ByteArrayByteSource(byte[] bytes) {
/* 567 */       this(bytes, 0, bytes.length);
/*     */     }
/*     */ 
/*     */     
/*     */     ByteArrayByteSource(byte[] bytes, int offset, int length) {
/* 572 */       this.bytes = bytes;
/* 573 */       this.offset = offset;
/* 574 */       this.length = length;
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openStream() {
/* 579 */       return new ByteArrayInputStream(this.bytes, this.offset, this.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openBufferedStream() throws IOException {
/* 584 */       return openStream();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 589 */       return (this.length == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public long size() {
/* 594 */       return this.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Long> sizeIfKnown() {
/* 599 */       return Optional.of(Long.valueOf(this.length));
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] read() {
/* 604 */       return Arrays.copyOfRange(this.bytes, this.offset, this.offset + this.length);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> T read(ByteProcessor<T> processor) throws IOException {
/* 610 */       processor.processBytes(this.bytes, this.offset, this.length);
/* 611 */       return processor.getResult();
/*     */     }
/*     */ 
/*     */     
/*     */     public long copyTo(OutputStream output) throws IOException {
/* 616 */       output.write(this.bytes, this.offset, this.length);
/* 617 */       return this.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public HashCode hash(HashFunction hashFunction) throws IOException {
/* 622 */       return hashFunction.hashBytes(this.bytes, this.offset, this.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteSource slice(long offset, long length) {
/* 627 */       Preconditions.checkArgument((offset >= 0L), "offset (%s) may not be negative", offset);
/* 628 */       Preconditions.checkArgument((length >= 0L), "length (%s) may not be negative", length);
/*     */       
/* 630 */       offset = Math.min(offset, this.length);
/* 631 */       length = Math.min(length, this.length - offset);
/* 632 */       int newOffset = this.offset + (int)offset;
/* 633 */       return new ByteArrayByteSource(this.bytes, newOffset, (int)length);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 639 */       String str = Ascii.truncate(BaseEncoding.base16().encode(this.bytes, this.offset, this.length), 30, "..."); return (new StringBuilder(17 + String.valueOf(str).length())).append("ByteSource.wrap(").append(str).append(")").toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class EmptyByteSource
/*     */     extends ByteArrayByteSource
/*     */   {
/* 646 */     static final EmptyByteSource INSTANCE = new EmptyByteSource();
/*     */     
/*     */     EmptyByteSource() {
/* 649 */       super(new byte[0]);
/*     */     }
/*     */ 
/*     */     
/*     */     public CharSource asCharSource(Charset charset) {
/* 654 */       Preconditions.checkNotNull(charset);
/* 655 */       return CharSource.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] read() {
/* 660 */       return this.bytes;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 665 */       return "ByteSource.empty()";
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ConcatenatedByteSource
/*     */     extends ByteSource {
/*     */     final Iterable<? extends ByteSource> sources;
/*     */     
/*     */     ConcatenatedByteSource(Iterable<? extends ByteSource> sources) {
/* 674 */       this.sources = (Iterable<? extends ByteSource>)Preconditions.checkNotNull(sources);
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openStream() throws IOException {
/* 679 */       return new MultiInputStream(this.sources.iterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() throws IOException {
/* 684 */       for (ByteSource source : this.sources) {
/* 685 */         if (!source.isEmpty()) {
/* 686 */           return false;
/*     */         }
/*     */       } 
/* 689 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Long> sizeIfKnown() {
/* 694 */       if (!(this.sources instanceof java.util.Collection))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 700 */         return Optional.absent();
/*     */       }
/* 702 */       long result = 0L;
/* 703 */       for (ByteSource source : this.sources) {
/* 704 */         Optional<Long> sizeIfKnown = source.sizeIfKnown();
/* 705 */         if (!sizeIfKnown.isPresent()) {
/* 706 */           return Optional.absent();
/*     */         }
/* 708 */         result += ((Long)sizeIfKnown.get()).longValue();
/* 709 */         if (result < 0L)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 715 */           return Optional.of(Long.valueOf(Long.MAX_VALUE));
/*     */         }
/*     */       } 
/* 718 */       return Optional.of(Long.valueOf(result));
/*     */     }
/*     */ 
/*     */     
/*     */     public long size() throws IOException {
/* 723 */       long result = 0L;
/* 724 */       for (ByteSource source : this.sources) {
/* 725 */         result += source.size();
/* 726 */         if (result < 0L)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 732 */           return Long.MAX_VALUE;
/*     */         }
/*     */       } 
/* 735 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 740 */       String str = String.valueOf(this.sources); return (new StringBuilder(19 + String.valueOf(str).length())).append("ByteSource.concat(").append(str).append(")").toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\io\ByteSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */