/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.util.ArrayList;
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
/*     */ @GwtIncompatible
/*     */ public final class CharStreams
/*     */ {
/*     */   private static final int DEFAULT_BUF_SIZE = 2048;
/*     */   
/*     */   static CharBuffer createBuffer() {
/*  55 */     return CharBuffer.allocate(2048);
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
/*     */   @CanIgnoreReturnValue
/*     */   public static long copy(Readable from, Appendable to) throws IOException {
/*  73 */     if (from instanceof Reader) {
/*     */       
/*  75 */       if (to instanceof StringBuilder) {
/*  76 */         return copyReaderToBuilder((Reader)from, (StringBuilder)to);
/*     */       }
/*  78 */       return copyReaderToWriter((Reader)from, asWriter(to));
/*     */     } 
/*     */     
/*  81 */     Preconditions.checkNotNull(from);
/*  82 */     Preconditions.checkNotNull(to);
/*  83 */     long total = 0L;
/*  84 */     CharBuffer buf = createBuffer();
/*  85 */     while (from.read(buf) != -1) {
/*  86 */       Java8Compatibility.flip(buf);
/*  87 */       to.append(buf);
/*  88 */       total += buf.remaining();
/*  89 */       Java8Compatibility.clear(buf);
/*     */     } 
/*  91 */     return total;
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
/*     */   @CanIgnoreReturnValue
/*     */   static long copyReaderToBuilder(Reader from, StringBuilder to) throws IOException {
/* 115 */     Preconditions.checkNotNull(from);
/* 116 */     Preconditions.checkNotNull(to);
/* 117 */     char[] buf = new char[2048];
/*     */     
/* 119 */     long total = 0L; int nRead;
/* 120 */     while ((nRead = from.read(buf)) != -1) {
/* 121 */       to.append(buf, 0, nRead);
/* 122 */       total += nRead;
/*     */     } 
/* 124 */     return total;
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
/*     */   @CanIgnoreReturnValue
/*     */   static long copyReaderToWriter(Reader from, Writer to) throws IOException {
/* 143 */     Preconditions.checkNotNull(from);
/* 144 */     Preconditions.checkNotNull(to);
/* 145 */     char[] buf = new char[2048];
/*     */     
/* 147 */     long total = 0L; int nRead;
/* 148 */     while ((nRead = from.read(buf)) != -1) {
/* 149 */       to.write(buf, 0, nRead);
/* 150 */       total += nRead;
/*     */     } 
/* 152 */     return total;
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
/*     */   public static String toString(Readable r) throws IOException {
/* 164 */     return toStringBuilder(r).toString();
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
/*     */   private static StringBuilder toStringBuilder(Readable r) throws IOException {
/* 176 */     StringBuilder sb = new StringBuilder();
/* 177 */     if (r instanceof Reader) {
/* 178 */       copyReaderToBuilder((Reader)r, sb);
/*     */     } else {
/* 180 */       copy(r, sb);
/*     */     } 
/* 182 */     return sb;
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
/*     */   public static List<String> readLines(Readable r) throws IOException {
/* 198 */     List<String> result = new ArrayList<>();
/* 199 */     LineReader lineReader = new LineReader(r);
/*     */     String line;
/* 201 */     while ((line = lineReader.readLine()) != null) {
/* 202 */       result.add(line);
/*     */     }
/* 204 */     return result;
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
/*     */   public static <T> T readLines(Readable readable, LineProcessor<T> processor) throws IOException {
/* 219 */     Preconditions.checkNotNull(readable);
/* 220 */     Preconditions.checkNotNull(processor);
/*     */     
/* 222 */     LineReader lineReader = new LineReader(readable); String line; do {
/*     */     
/* 224 */     } while ((line = lineReader.readLine()) != null && 
/* 225 */       processor.processLine(line));
/*     */ 
/*     */ 
/*     */     
/* 229 */     return processor.getResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   public static long exhaust(Readable readable) throws IOException {
/* 241 */     long total = 0L;
/*     */     
/* 243 */     CharBuffer buf = createBuffer(); long read;
/* 244 */     while ((read = readable.read(buf)) != -1L) {
/* 245 */       total += read;
/* 246 */       Java8Compatibility.clear(buf);
/*     */     } 
/* 248 */     return total;
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
/*     */   public static void skipFully(Reader reader, long n) throws IOException {
/* 262 */     Preconditions.checkNotNull(reader);
/* 263 */     while (n > 0L) {
/* 264 */       long amt = reader.skip(n);
/* 265 */       if (amt == 0L) {
/* 266 */         throw new EOFException();
/*     */       }
/* 268 */       n -= amt;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static Writer nullWriter() {
/* 279 */     return NullWriter.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class NullWriter
/*     */     extends Writer {
/* 284 */     private static final NullWriter INSTANCE = new NullWriter();
/*     */ 
/*     */     
/*     */     public void write(int c) {}
/*     */ 
/*     */     
/*     */     public void write(char[] cbuf) {
/* 291 */       Preconditions.checkNotNull(cbuf);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(char[] cbuf, int off, int len) {
/* 296 */       Preconditions.checkPositionIndexes(off, off + len, cbuf.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(String str) {
/* 301 */       Preconditions.checkNotNull(str);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(String str, int off, int len) {
/* 306 */       Preconditions.checkPositionIndexes(off, off + len, str.length());
/*     */     }
/*     */ 
/*     */     
/*     */     public Writer append(CharSequence csq) {
/* 311 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Writer append(CharSequence csq, int start, int end) {
/* 316 */       Preconditions.checkPositionIndexes(start, end, (csq == null) ? "null".length() : csq.length());
/* 317 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Writer append(char c) {
/* 322 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public void flush() {}
/*     */ 
/*     */     
/*     */     public void close() {}
/*     */ 
/*     */     
/*     */     public String toString() {
/* 333 */       return "CharStreams.nullWriter()";
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
/*     */   @Beta
/*     */   public static Writer asWriter(Appendable target) {
/* 347 */     if (target instanceof Writer) {
/* 348 */       return (Writer)target;
/*     */     }
/* 350 */     return new AppendableWriter(target);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\io\CharStreams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */