/*     */ package org.checkerframework.checker.signedness;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.math.BigInteger;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SignednessUtil
/*     */ {
/*     */   private SignednessUtil() {
/*  21 */     throw new Error("Do not instantiate");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuffer wrapUnsigned(byte[] array) {
/*  31 */     return ByteBuffer.wrap(array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuffer wrapUnsigned(byte[] array, int offset, int length) {
/*  41 */     return ByteBuffer.wrap(array, offset, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getUnsignedInt(ByteBuffer b) {
/*  51 */     return b.getInt();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static short getUnsignedShort(ByteBuffer b) {
/*  61 */     return b.getShort();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte getUnsigned(ByteBuffer b) {
/*  71 */     return b.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte getUnsigned(ByteBuffer b, int i) {
/*  81 */     return b.get(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuffer getUnsigned(ByteBuffer b, byte[] bs, int i, int l) {
/*  91 */     return b.get(bs, i, l);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuffer putUnsigned(ByteBuffer b, byte ubyte) {
/* 101 */     return b.put(ubyte);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuffer putUnsigned(ByteBuffer b, int i, byte ubyte) {
/* 111 */     return b.put(i, ubyte);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IntBuffer putUnsigned(IntBuffer b, int uint) {
/* 121 */     return b.put(uint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IntBuffer putUnsigned(IntBuffer b, int i, int uint) {
/* 131 */     return b.put(i, uint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IntBuffer putUnsigned(IntBuffer b, int[] uints) {
/* 141 */     return b.put(uints);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IntBuffer putUnsigned(IntBuffer b, int[] uints, int i, int l) {
/* 151 */     return b.put(uints, i, l);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getUnsigned(IntBuffer b, int i) {
/* 161 */     return b.get(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuffer putUnsignedShort(ByteBuffer b, short ushort) {
/* 171 */     return b.putShort(ushort);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuffer putUnsignedShort(ByteBuffer b, int i, short ushort) {
/* 181 */     return b.putShort(i, ushort);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuffer putUnsignedInt(ByteBuffer b, int uint) {
/* 191 */     return b.putInt(uint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuffer putUnsignedInt(ByteBuffer b, int i, int uint) {
/* 201 */     return b.putInt(i, uint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuffer putUnsignedLong(ByteBuffer b, int i, long ulong) {
/* 211 */     return b.putLong(i, ulong);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char readUnsignedChar(RandomAccessFile f) throws IOException {
/* 221 */     return f.readChar();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int readUnsignedInt(RandomAccessFile f) throws IOException {
/* 231 */     return f.readInt();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long readUnsignedLong(RandomAccessFile f) throws IOException {
/* 241 */     return f.readLong();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int readUnsigned(RandomAccessFile f, byte[] b, int off, int len) throws IOException {
/* 252 */     return f.read(b, off, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void readFullyUnsigned(RandomAccessFile f, byte[] b) throws IOException {
/* 263 */     f.readFully(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeUnsigned(RandomAccessFile f, byte[] bs, int off, int len) throws IOException {
/* 274 */     f.write(bs, off, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeUnsignedByte(RandomAccessFile f, byte b) throws IOException {
/* 284 */     f.writeByte(Byte.toUnsignedInt(b));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeUnsignedChar(RandomAccessFile f, char c) throws IOException {
/* 294 */     f.writeChar(toUnsignedInt(c));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeUnsignedShort(RandomAccessFile f, short s) throws IOException {
/* 305 */     f.writeShort(Short.toUnsignedInt(s));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeUnsignedInt(RandomAccessFile f, int i) throws IOException {
/* 315 */     f.writeInt(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeUnsignedLong(RandomAccessFile f, long l) throws IOException {
/* 325 */     f.writeLong(l);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void getUnsigned(ByteBuffer b, byte[] bs) {
/* 335 */     b.get(bs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int compareUnsigned(short x, short y) {
/* 346 */     return Integer.compareUnsigned(Short.toUnsignedInt(x), Short.toUnsignedInt(y));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int compareUnsigned(byte x, byte y) {
/* 357 */     return Integer.compareUnsigned(Byte.toUnsignedInt(x), Byte.toUnsignedInt(y));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toUnsignedString(short s) {
/* 363 */     return Long.toString(Short.toUnsignedLong(s));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toUnsignedString(short s, int radix) {
/* 369 */     return Integer.toUnsignedString(Short.toUnsignedInt(s), radix);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toUnsignedString(byte b) {
/* 375 */     return Integer.toUnsignedString(Byte.toUnsignedInt(b));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toUnsignedString(byte b, int radix) {
/* 381 */     return Integer.toUnsignedString(Byte.toUnsignedInt(b), radix);
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
/*     */   private static BigInteger toUnsignedBigInteger(long l) {
/* 393 */     if (l >= 0L) {
/* 394 */       return BigInteger.valueOf(l);
/*     */     }
/* 396 */     int upper = (int)(l >>> 32L);
/* 397 */     int lower = (int)l;
/*     */ 
/*     */     
/* 400 */     return BigInteger.valueOf(Integer.toUnsignedLong(upper))
/* 401 */       .shiftLeft(32)
/* 402 */       .add(BigInteger.valueOf(Integer.toUnsignedLong(lower)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static short toUnsignedShort(byte b) {
/* 408 */     return (short)(b & 0xFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public static long toUnsignedLong(char c) {
/* 413 */     return c & 0xFFL;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int toUnsignedInt(char c) {
/* 418 */     return c & 0xFF;
/*     */   }
/*     */ 
/*     */   
/*     */   public static short toUnsignedShort(char c) {
/* 423 */     return (short)(c & 0xFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float toFloat(byte b) {
/* 428 */     return toUnsignedBigInteger(Byte.toUnsignedLong(b)).floatValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public static float toFloat(short s) {
/* 433 */     return toUnsignedBigInteger(Short.toUnsignedLong(s)).floatValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public static float toFloat(int i) {
/* 438 */     return toUnsignedBigInteger(Integer.toUnsignedLong(i)).floatValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public static float toFloat(long l) {
/* 443 */     return toUnsignedBigInteger(l).floatValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public static double toDouble(byte b) {
/* 448 */     return toUnsignedBigInteger(Byte.toUnsignedLong(b)).doubleValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public static double toDouble(short s) {
/* 453 */     return toUnsignedBigInteger(Short.toUnsignedLong(s)).doubleValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public static double toDouble(int i) {
/* 458 */     return toUnsignedBigInteger(Integer.toUnsignedLong(i)).doubleValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public static double toDouble(long l) {
/* 463 */     return toUnsignedBigInteger(l).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte byteFromFloat(float f) {
/* 469 */     assert f >= 0.0F;
/* 470 */     return (byte)(int)f;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static short shortFromFloat(float f) {
/* 476 */     assert f >= 0.0F;
/* 477 */     return (short)(int)f;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int intFromFloat(float f) {
/* 483 */     assert f >= 0.0F;
/* 484 */     return (int)f;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static long longFromFloat(float f) {
/* 490 */     assert f >= 0.0F;
/* 491 */     return (long)f;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte byteFromDouble(double d) {
/* 497 */     assert d >= 0.0D;
/* 498 */     return (byte)(int)d;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static short shortFromDouble(double d) {
/* 504 */     assert d >= 0.0D;
/* 505 */     return (short)(int)d;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int intFromDouble(double d) {
/* 511 */     assert d >= 0.0D;
/* 512 */     return (int)d;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static long longFromDouble(double d) {
/* 518 */     assert d >= 0.0D;
/* 519 */     return (long)d;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\checker\signedness\SignednessUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */