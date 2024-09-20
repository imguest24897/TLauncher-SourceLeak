/*     */ package org.apache.commons.text.io;
/*     */ 
/*     */ import java.io.FilterReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import org.apache.commons.text.StringSubstitutor;
/*     */ import org.apache.commons.text.TextStringBuilder;
/*     */ import org.apache.commons.text.matcher.StringMatcher;
/*     */ import org.apache.commons.text.matcher.StringMatcherFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringSubstitutorReader
/*     */   extends FilterReader
/*     */ {
/*     */   private static final int EOS = -1;
/*  48 */   private final TextStringBuilder buffer = new TextStringBuilder();
/*     */ 
/*     */   
/*     */   private boolean eos;
/*     */ 
/*     */   
/*     */   private final StringMatcher prefixEscapeMatcher;
/*     */ 
/*     */   
/*  57 */   private final char[] read1CharBuffer = new char[] { Character.MIN_VALUE };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final StringSubstitutor stringSubstitutor;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int toDrain;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringSubstitutorReader(Reader reader, StringSubstitutor stringSubstitutor) {
/*  74 */     super(reader);
/*  75 */     this.stringSubstitutor = new StringSubstitutor(stringSubstitutor);
/*  76 */     this
/*  77 */       .prefixEscapeMatcher = StringMatcherFactory.INSTANCE.charMatcher(stringSubstitutor.getEscapeChar()).andThen(stringSubstitutor.getVariablePrefixMatcher());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int buffer(int requestReadCount) throws IOException {
/*  84 */     int actualReadCount = this.buffer.readFrom(this.in, requestReadCount);
/*  85 */     this.eos = (actualReadCount == -1);
/*  86 */     return actualReadCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int bufferOrDrainOnEos(int requestReadCount, char[] target, int targetIndex, int targetLength) throws IOException {
/*  95 */     int actualReadCount = buffer(requestReadCount);
/*  96 */     return drainOnEos(actualReadCount, target, targetIndex, targetLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int drain(char[] target, int targetIndex, int targetLength) {
/* 103 */     int actualLen = Math.min(this.buffer.length(), targetLength);
/* 104 */     int drainCount = this.buffer.drainChars(0, actualLen, target, targetIndex);
/* 105 */     this.toDrain -= drainCount;
/* 106 */     if (this.buffer.isEmpty() || this.toDrain == 0)
/*     */     {
/* 108 */       this.toDrain = 0;
/*     */     }
/* 110 */     return drainCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int drainOnEos(int readCountOrEos, char[] target, int targetIndex, int targetLength) {
/* 119 */     if (readCountOrEos == -1) {
/*     */       
/* 121 */       if (this.buffer.isNotEmpty()) {
/* 122 */         this.toDrain = this.buffer.size();
/* 123 */         return drain(target, targetIndex, targetLength);
/*     */       } 
/* 125 */       return -1;
/*     */     } 
/* 127 */     return readCountOrEos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isBufferMatchAt(StringMatcher stringMatcher, int pos) {
/* 134 */     return (stringMatcher.isMatch((CharSequence)this.buffer, pos) == stringMatcher.size());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isDraining() {
/* 141 */     return (this.toDrain > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 152 */     int count = 0;
/*     */     
/*     */     while (true) {
/* 155 */       count = read(this.read1CharBuffer, 0, 1);
/* 156 */       if (count == -1) {
/* 157 */         return -1;
/*     */       }
/*     */       
/* 160 */       if (count >= 1) {
/* 161 */         return this.read1CharBuffer[0];
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
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(char[] target, int targetIndexIn, int targetLengthIn) throws IOException {
/* 182 */     if (this.eos && this.buffer.isEmpty()) {
/* 183 */       return -1;
/*     */     }
/* 185 */     if (targetLengthIn <= 0)
/*     */     {
/* 187 */       return 0;
/*     */     }
/*     */     
/* 190 */     int targetIndex = targetIndexIn;
/* 191 */     int targetLength = targetLengthIn;
/* 192 */     if (isDraining()) {
/*     */       
/* 194 */       int drainCount = drain(target, targetIndex, Math.min(this.toDrain, targetLength));
/* 195 */       if (drainCount == targetLength)
/*     */       {
/* 197 */         return targetLength;
/*     */       }
/*     */       
/* 200 */       targetIndex += drainCount;
/* 201 */       targetLength -= drainCount;
/*     */     } 
/*     */     
/* 204 */     int minReadLenPrefix = this.prefixEscapeMatcher.size();
/*     */     
/* 206 */     int readCount = buffer(readCount(minReadLenPrefix, 0));
/* 207 */     if (this.buffer.length() < minReadLenPrefix && targetLength < minReadLenPrefix) {
/*     */       
/* 209 */       int drainCount = drain(target, targetIndex, targetLength);
/* 210 */       targetIndex += drainCount;
/* 211 */       int targetSize = targetIndex - targetIndexIn;
/* 212 */       return (this.eos && targetSize <= 0) ? -1 : targetSize;
/*     */     } 
/* 214 */     if (this.eos) {
/*     */       
/* 216 */       this.stringSubstitutor.replaceIn(this.buffer);
/* 217 */       this.toDrain = this.buffer.size();
/* 218 */       int drainCount = drain(target, targetIndex, targetLength);
/* 219 */       targetIndex += drainCount;
/* 220 */       int targetSize = targetIndex - targetIndexIn;
/* 221 */       return (this.eos && targetSize <= 0) ? -1 : targetSize;
/*     */     } 
/*     */ 
/*     */     
/* 225 */     int balance = 0;
/* 226 */     StringMatcher prefixMatcher = this.stringSubstitutor.getVariablePrefixMatcher();
/* 227 */     int pos = 0;
/* 228 */     while (targetLength > 0) {
/* 229 */       if (isBufferMatchAt(prefixMatcher, 0)) {
/* 230 */         balance = 1;
/* 231 */         pos = prefixMatcher.size();
/*     */         break;
/*     */       } 
/* 234 */       if (isBufferMatchAt(this.prefixEscapeMatcher, 0)) {
/* 235 */         balance = 1;
/* 236 */         pos = this.prefixEscapeMatcher.size();
/*     */         
/*     */         break;
/*     */       } 
/* 240 */       int drainCount = drain(target, targetIndex, 1);
/* 241 */       targetIndex += drainCount;
/* 242 */       targetLength -= drainCount;
/* 243 */       if (this.buffer.size() < minReadLenPrefix) {
/* 244 */         readCount = bufferOrDrainOnEos(minReadLenPrefix, target, targetIndex, targetLength);
/* 245 */         if (this.eos || isDraining()) {
/*     */           
/* 247 */           if (readCount != -1) {
/* 248 */             targetIndex += readCount;
/* 249 */             targetLength -= readCount;
/*     */           } 
/* 251 */           int actual = targetIndex - targetIndexIn;
/* 252 */           return (actual > 0) ? actual : -1;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 257 */     if (targetLength <= 0)
/*     */     {
/* 259 */       return targetLengthIn;
/*     */     }
/*     */ 
/*     */     
/* 263 */     StringMatcher suffixMatcher = this.stringSubstitutor.getVariableSuffixMatcher();
/* 264 */     int minReadLenSuffix = Math.max(minReadLenPrefix, suffixMatcher.size());
/* 265 */     readCount = buffer(readCount(minReadLenSuffix, pos));
/* 266 */     if (this.eos) {
/*     */       
/* 268 */       this.stringSubstitutor.replaceIn(this.buffer);
/* 269 */       this.toDrain = this.buffer.size();
/* 270 */       int drainCount = drain(target, targetIndex, targetLength);
/* 271 */       return targetIndex + drainCount - targetIndexIn;
/*     */     } 
/*     */     
/*     */     do {
/* 275 */       if (isBufferMatchAt(suffixMatcher, pos)) {
/* 276 */         balance--;
/* 277 */         pos++;
/* 278 */         if (balance == 0) {
/*     */           break;
/*     */         }
/* 281 */       } else if (isBufferMatchAt(prefixMatcher, pos)) {
/* 282 */         balance++;
/* 283 */         pos += prefixMatcher.size();
/* 284 */       } else if (isBufferMatchAt(this.prefixEscapeMatcher, pos)) {
/* 285 */         balance++;
/* 286 */         pos += this.prefixEscapeMatcher.size();
/*     */       } else {
/* 288 */         pos++;
/*     */       } 
/* 290 */       readCount = buffer(readCount(minReadLenSuffix, pos));
/* 291 */     } while (readCount != -1 || pos < this.buffer.size());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 296 */     int endPos = pos + 1;
/* 297 */     int leftover = Math.max(0, this.buffer.size() - pos);
/* 298 */     this.stringSubstitutor.replaceIn(this.buffer, 0, Math.min(this.buffer.size(), endPos));
/* 299 */     pos = this.buffer.size() - leftover;
/* 300 */     int drainLen = Math.min(targetLength, pos);
/*     */     
/* 302 */     this.toDrain = pos;
/* 303 */     drain(target, targetIndex, drainLen);
/* 304 */     return targetIndex - targetIndexIn + drainLen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int readCount(int count, int pos) {
/* 312 */     int avail = this.buffer.size() - pos;
/* 313 */     return (avail >= count) ? 0 : (count - avail);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\io\StringSubstitutorReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */