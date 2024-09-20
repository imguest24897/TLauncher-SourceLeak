/*     */ package org.apache.http.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ByteArrayBuffer
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4359112959524048036L;
/*     */   private byte[] buffer;
/*     */   private int len;
/*     */   
/*     */   public ByteArrayBuffer(int capacity) {
/*  52 */     Args.notNegative(capacity, "Buffer capacity");
/*  53 */     this.buffer = new byte[capacity];
/*     */   }
/*     */   
/*     */   private void expand(int newlen) {
/*  57 */     byte[] newbuffer = new byte[Math.max(this.buffer.length << 1, newlen)];
/*  58 */     System.arraycopy(this.buffer, 0, newbuffer, 0, this.len);
/*  59 */     this.buffer = newbuffer;
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
/*     */   public void append(byte[] b, int off, int len) {
/*  75 */     if (b == null) {
/*     */       return;
/*     */     }
/*  78 */     if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length)
/*     */     {
/*  80 */       throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
/*     */     }
/*  82 */     if (len == 0) {
/*     */       return;
/*     */     }
/*  85 */     int newlen = this.len + len;
/*  86 */     if (newlen > this.buffer.length) {
/*  87 */       expand(newlen);
/*     */     }
/*  89 */     System.arraycopy(b, off, this.buffer, this.len, len);
/*  90 */     this.len = newlen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(int b) {
/* 100 */     int newlen = this.len + 1;
/* 101 */     if (newlen > this.buffer.length) {
/* 102 */       expand(newlen);
/*     */     }
/* 104 */     this.buffer[this.len] = (byte)b;
/* 105 */     this.len = newlen;
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
/*     */   public void append(char[] b, int off, int len) {
/* 123 */     if (b == null) {
/*     */       return;
/*     */     }
/* 126 */     if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length)
/*     */     {
/* 128 */       throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
/*     */     }
/* 130 */     if (len == 0) {
/*     */       return;
/*     */     }
/* 133 */     int oldlen = this.len;
/* 134 */     int newlen = oldlen + len;
/* 135 */     if (newlen > this.buffer.length) {
/* 136 */       expand(newlen);
/*     */     }
/*     */     
/* 139 */     for (int i1 = off, i2 = oldlen; i2 < newlen; i1++, i2++) {
/* 140 */       if ((b[i1] >= ' ' && b[i1] <= '~') || (b[i1] >= ' ' && b[i1] <= 'ÿ')) {
/*     */         
/* 142 */         this.buffer[i2] = (byte)b[i1];
/*     */       } else {
/* 144 */         this.buffer[i2] = 63;
/*     */       } 
/*     */     } 
/* 147 */     this.len = newlen;
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
/*     */   public void append(CharArrayBuffer b, int off, int len) {
/* 166 */     if (b == null) {
/*     */       return;
/*     */     }
/* 169 */     append(b.buffer(), off, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 176 */     this.len = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] toByteArray() {
/* 185 */     byte[] b = new byte[this.len];
/* 186 */     if (this.len > 0) {
/* 187 */       System.arraycopy(this.buffer, 0, b, 0, this.len);
/*     */     }
/* 189 */     return b;
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
/*     */   public int byteAt(int i) {
/* 203 */     return this.buffer[i];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 214 */     return this.buffer.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 223 */     return this.len;
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
/*     */   public void ensureCapacity(int required) {
/* 237 */     if (required <= 0) {
/*     */       return;
/*     */     }
/* 240 */     int available = this.buffer.length - this.len;
/* 241 */     if (required > available) {
/* 242 */       expand(this.len + required);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] buffer() {
/* 252 */     return this.buffer;
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
/*     */   public void setLength(int len) {
/* 266 */     if (len < 0 || len > this.buffer.length) {
/* 267 */       throw new IndexOutOfBoundsException("len: " + len + " < 0 or > buffer len: " + this.buffer.length);
/*     */     }
/* 269 */     this.len = len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 279 */     return (this.len == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFull() {
/* 289 */     return (this.len == this.buffer.length);
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
/*     */   public int indexOf(byte b, int from, int to) {
/* 316 */     int beginIndex = from;
/* 317 */     if (beginIndex < 0) {
/* 318 */       beginIndex = 0;
/*     */     }
/* 320 */     int endIndex = to;
/* 321 */     if (endIndex > this.len) {
/* 322 */       endIndex = this.len;
/*     */     }
/* 324 */     if (beginIndex > endIndex) {
/* 325 */       return -1;
/*     */     }
/* 327 */     for (int i = beginIndex; i < endIndex; i++) {
/* 328 */       if (this.buffer[i] == b) {
/* 329 */         return i;
/*     */       }
/*     */     } 
/* 332 */     return -1;
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
/*     */   public int indexOf(byte b) {
/* 348 */     return indexOf(b, 0, this.len);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\htt\\util\ByteArrayBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */