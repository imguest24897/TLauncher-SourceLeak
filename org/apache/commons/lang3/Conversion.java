/*      */ package org.apache.commons.lang3;
/*      */ 
/*      */ import java.util.UUID;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Conversion
/*      */ {
/*   65 */   private static final boolean[] TTTT = new boolean[] { true, true, true, true };
/*   66 */   private static final boolean[] FTTT = new boolean[] { false, true, true, true };
/*   67 */   private static final boolean[] TFTT = new boolean[] { true, false, true, true };
/*   68 */   private static final boolean[] FFTT = new boolean[] { false, false, true, true };
/*   69 */   private static final boolean[] TTFT = new boolean[] { true, true, false, true };
/*   70 */   private static final boolean[] FTFT = new boolean[] { false, true, false, true };
/*   71 */   private static final boolean[] TFFT = new boolean[] { true, false, false, true };
/*   72 */   private static final boolean[] FFFT = new boolean[] { false, false, false, true };
/*   73 */   private static final boolean[] TTTF = new boolean[] { true, true, true, false };
/*   74 */   private static final boolean[] FTTF = new boolean[] { false, true, true, false };
/*   75 */   private static final boolean[] TFTF = new boolean[] { true, false, true, false };
/*   76 */   private static final boolean[] FFTF = new boolean[] { false, false, true, false };
/*   77 */   private static final boolean[] TTFF = new boolean[] { true, true, false, false };
/*   78 */   private static final boolean[] FTFF = new boolean[] { false, true, false, false };
/*   79 */   private static final boolean[] TFFF = new boolean[] { true, false, false, false };
/*   80 */   private static final boolean[] FFFF = new boolean[] { false, false, false, false };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int hexDigitToInt(char hexDigit) {
/*   94 */     int digit = Character.digit(hexDigit, 16);
/*   95 */     if (digit < 0) {
/*   96 */       throw new IllegalArgumentException("Cannot interpret '" + hexDigit + "' as a hexadecimal digit");
/*      */     }
/*   98 */     return digit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int hexDigitMsb0ToInt(char hexDigit) {
/*  113 */     switch (hexDigit) {
/*      */       case '0':
/*  115 */         return 0;
/*      */       case '1':
/*  117 */         return 8;
/*      */       case '2':
/*  119 */         return 4;
/*      */       case '3':
/*  121 */         return 12;
/*      */       case '4':
/*  123 */         return 2;
/*      */       case '5':
/*  125 */         return 10;
/*      */       case '6':
/*  127 */         return 6;
/*      */       case '7':
/*  129 */         return 14;
/*      */       case '8':
/*  131 */         return 1;
/*      */       case '9':
/*  133 */         return 9;
/*      */       case 'A':
/*      */       case 'a':
/*  136 */         return 5;
/*      */       case 'B':
/*      */       case 'b':
/*  139 */         return 13;
/*      */       case 'C':
/*      */       case 'c':
/*  142 */         return 3;
/*      */       case 'D':
/*      */       case 'd':
/*  145 */         return 11;
/*      */       case 'E':
/*      */       case 'e':
/*  148 */         return 7;
/*      */       case 'F':
/*      */       case 'f':
/*  151 */         return 15;
/*      */     } 
/*  153 */     throw new IllegalArgumentException("Cannot interpret '" + hexDigit + "' as a hexadecimal digit");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] hexDigitToBinary(char hexDigit) {
/*  170 */     switch (hexDigit) {
/*      */       case '0':
/*  172 */         return (boolean[])FFFF.clone();
/*      */       case '1':
/*  174 */         return (boolean[])TFFF.clone();
/*      */       case '2':
/*  176 */         return (boolean[])FTFF.clone();
/*      */       case '3':
/*  178 */         return (boolean[])TTFF.clone();
/*      */       case '4':
/*  180 */         return (boolean[])FFTF.clone();
/*      */       case '5':
/*  182 */         return (boolean[])TFTF.clone();
/*      */       case '6':
/*  184 */         return (boolean[])FTTF.clone();
/*      */       case '7':
/*  186 */         return (boolean[])TTTF.clone();
/*      */       case '8':
/*  188 */         return (boolean[])FFFT.clone();
/*      */       case '9':
/*  190 */         return (boolean[])TFFT.clone();
/*      */       case 'A':
/*      */       case 'a':
/*  193 */         return (boolean[])FTFT.clone();
/*      */       case 'B':
/*      */       case 'b':
/*  196 */         return (boolean[])TTFT.clone();
/*      */       case 'C':
/*      */       case 'c':
/*  199 */         return (boolean[])FFTT.clone();
/*      */       case 'D':
/*      */       case 'd':
/*  202 */         return (boolean[])TFTT.clone();
/*      */       case 'E':
/*      */       case 'e':
/*  205 */         return (boolean[])FTTT.clone();
/*      */       case 'F':
/*      */       case 'f':
/*  208 */         return (boolean[])TTTT.clone();
/*      */     } 
/*  210 */     throw new IllegalArgumentException("Cannot interpret '" + hexDigit + "' as a hexadecimal digit");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] hexDigitMsb0ToBinary(char hexDigit) {
/*  227 */     switch (hexDigit) {
/*      */       case '0':
/*  229 */         return (boolean[])FFFF.clone();
/*      */       case '1':
/*  231 */         return (boolean[])FFFT.clone();
/*      */       case '2':
/*  233 */         return (boolean[])FFTF.clone();
/*      */       case '3':
/*  235 */         return (boolean[])FFTT.clone();
/*      */       case '4':
/*  237 */         return (boolean[])FTFF.clone();
/*      */       case '5':
/*  239 */         return (boolean[])FTFT.clone();
/*      */       case '6':
/*  241 */         return (boolean[])FTTF.clone();
/*      */       case '7':
/*  243 */         return (boolean[])FTTT.clone();
/*      */       case '8':
/*  245 */         return (boolean[])TFFF.clone();
/*      */       case '9':
/*  247 */         return (boolean[])TFFT.clone();
/*      */       case 'A':
/*      */       case 'a':
/*  250 */         return (boolean[])TFTF.clone();
/*      */       case 'B':
/*      */       case 'b':
/*  253 */         return (boolean[])TFTT.clone();
/*      */       case 'C':
/*      */       case 'c':
/*  256 */         return (boolean[])TTFF.clone();
/*      */       case 'D':
/*      */       case 'd':
/*  259 */         return (boolean[])TTFT.clone();
/*      */       case 'E':
/*      */       case 'e':
/*  262 */         return (boolean[])TTTF.clone();
/*      */       case 'F':
/*      */       case 'f':
/*  265 */         return (boolean[])TTTT.clone();
/*      */     } 
/*  267 */     throw new IllegalArgumentException("Cannot interpret '" + hexDigit + "' as a hexadecimal digit");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char binaryToHexDigit(boolean[] src) {
/*  285 */     return binaryToHexDigit(src, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char binaryToHexDigit(boolean[] src, int srcPos) {
/*  303 */     if (src.length == 0) {
/*  304 */       throw new IllegalArgumentException("Cannot convert an empty array.");
/*      */     }
/*  306 */     if (src.length > srcPos + 3 && src[srcPos + 3]) {
/*  307 */       if (src[srcPos + 2]) {
/*  308 */         if (src[srcPos + 1]) {
/*  309 */           return src[srcPos] ? 'f' : 'e';
/*      */         }
/*  311 */         return src[srcPos] ? 'd' : 'c';
/*      */       } 
/*  313 */       if (src[srcPos + 1]) {
/*  314 */         return src[srcPos] ? 'b' : 'a';
/*      */       }
/*  316 */       return src[srcPos] ? '9' : '8';
/*      */     } 
/*  318 */     if (src.length > srcPos + 2 && src[srcPos + 2]) {
/*  319 */       if (src[srcPos + 1]) {
/*  320 */         return src[srcPos] ? '7' : '6';
/*      */       }
/*  322 */       return src[srcPos] ? '5' : '4';
/*      */     } 
/*  324 */     if (src.length > srcPos + 1 && src[srcPos + 1]) {
/*  325 */       return src[srcPos] ? '3' : '2';
/*      */     }
/*  327 */     return src[srcPos] ? '1' : '0';
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char binaryToHexDigitMsb0_4bits(boolean[] src) {
/*  345 */     return binaryToHexDigitMsb0_4bits(src, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char binaryToHexDigitMsb0_4bits(boolean[] src, int srcPos) {
/*  365 */     if (src.length > 8) {
/*  366 */       throw new IllegalArgumentException("src.length>8: src.length=" + src.length);
/*      */     }
/*  368 */     if (src.length - srcPos < 4) {
/*  369 */       throw new IllegalArgumentException("src.length-srcPos<4: src.length=" + src.length + ", srcPos=" + srcPos);
/*      */     }
/*  371 */     if (src[srcPos + 3]) {
/*  372 */       if (src[srcPos + 2]) {
/*  373 */         if (src[srcPos + 1]) {
/*  374 */           return src[srcPos] ? 'f' : '7';
/*      */         }
/*  376 */         return src[srcPos] ? 'b' : '3';
/*      */       } 
/*  378 */       if (src[srcPos + 1]) {
/*  379 */         return src[srcPos] ? 'd' : '5';
/*      */       }
/*  381 */       return src[srcPos] ? '9' : '1';
/*      */     } 
/*  383 */     if (src[srcPos + 2]) {
/*  384 */       if (src[srcPos + 1]) {
/*  385 */         return src[srcPos] ? 'e' : '6';
/*      */       }
/*  387 */       return src[srcPos] ? 'a' : '2';
/*      */     } 
/*  389 */     if (src[srcPos + 1]) {
/*  390 */       return src[srcPos] ? 'c' : '4';
/*      */     }
/*  392 */     return src[srcPos] ? '8' : '0';
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char binaryBeMsb0ToHexDigit(boolean[] src) {
/*  410 */     return binaryBeMsb0ToHexDigit(src, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char binaryBeMsb0ToHexDigit(boolean[] src, int srcPos) {
/*  431 */     if (Integer.compareUnsigned(srcPos, src.length) >= 0) {
/*      */       
/*  433 */       if (src.length == 0) {
/*  434 */         throw new IllegalArgumentException("Cannot convert an empty array.");
/*      */       }
/*  436 */       throw new IndexOutOfBoundsException(srcPos + " is not within array length " + src.length);
/*      */     } 
/*      */     
/*  439 */     int pos = src.length - 1 - srcPos;
/*  440 */     if (3 <= pos && src[pos - 3]) {
/*  441 */       if (src[pos - 2]) {
/*  442 */         if (src[pos - 1]) {
/*  443 */           return src[pos] ? 'f' : 'e';
/*      */         }
/*  445 */         return src[pos] ? 'd' : 'c';
/*      */       } 
/*  447 */       if (src[pos - 1]) {
/*  448 */         return src[pos] ? 'b' : 'a';
/*      */       }
/*  450 */       return src[pos] ? '9' : '8';
/*      */     } 
/*  452 */     if (2 <= pos && src[pos - 2]) {
/*  453 */       if (src[pos - 1]) {
/*  454 */         return src[pos] ? '7' : '6';
/*      */       }
/*  456 */       return src[pos] ? '5' : '4';
/*      */     } 
/*  458 */     if (1 <= pos && src[pos - 1]) {
/*  459 */       return src[pos] ? '3' : '2';
/*      */     }
/*  461 */     return src[pos] ? '1' : '0';
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char intToHexDigit(int nibble) {
/*  482 */     char c = Character.forDigit(nibble, 16);
/*  483 */     if (c == '\000') {
/*  484 */       throw new IllegalArgumentException("nibble value not between 0 and 15: " + nibble);
/*      */     }
/*  486 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char intToHexDigitMsb0(int nibble) {
/*  507 */     switch (nibble) {
/*      */       case 0:
/*  509 */         return '0';
/*      */       case 1:
/*  511 */         return '8';
/*      */       case 2:
/*  513 */         return '4';
/*      */       case 3:
/*  515 */         return 'c';
/*      */       case 4:
/*  517 */         return '2';
/*      */       case 5:
/*  519 */         return 'a';
/*      */       case 6:
/*  521 */         return '6';
/*      */       case 7:
/*  523 */         return 'e';
/*      */       case 8:
/*  525 */         return '1';
/*      */       case 9:
/*  527 */         return '9';
/*      */       case 10:
/*  529 */         return '5';
/*      */       case 11:
/*  531 */         return 'd';
/*      */       case 12:
/*  533 */         return '3';
/*      */       case 13:
/*  535 */         return 'b';
/*      */       case 14:
/*  537 */         return '7';
/*      */       case 15:
/*  539 */         return 'f';
/*      */     } 
/*  541 */     throw new IllegalArgumentException("nibble value not between 0 and 15: " + nibble);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long intArrayToLong(int[] src, int srcPos, long dstInit, int dstPos, int nInts) {
/*  562 */     if ((src.length == 0 && srcPos == 0) || 0 == nInts) {
/*  563 */       return dstInit;
/*      */     }
/*  565 */     if ((nInts - 1) * 32 + dstPos >= 64) {
/*  566 */       throw new IllegalArgumentException("(nInts-1)*32+dstPos is greater or equal to than 64");
/*      */     }
/*  568 */     long out = dstInit;
/*  569 */     for (int i = 0; i < nInts; i++) {
/*  570 */       int shift = i * 32 + dstPos;
/*  571 */       long bits = (0xFFFFFFFFL & src[i + srcPos]) << shift;
/*  572 */       long mask = 4294967295L << shift;
/*  573 */       out = out & (mask ^ 0xFFFFFFFFFFFFFFFFL) | bits;
/*      */     } 
/*  575 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long shortArrayToLong(short[] src, int srcPos, long dstInit, int dstPos, int nShorts) {
/*  595 */     if ((src.length == 0 && srcPos == 0) || 0 == nShorts) {
/*  596 */       return dstInit;
/*      */     }
/*  598 */     if ((nShorts - 1) * 16 + dstPos >= 64) {
/*  599 */       throw new IllegalArgumentException("(nShorts-1)*16+dstPos is greater or equal to than 64");
/*      */     }
/*  601 */     long out = dstInit;
/*  602 */     for (int i = 0; i < nShorts; i++) {
/*  603 */       int shift = i * 16 + dstPos;
/*  604 */       long bits = (0xFFFFL & src[i + srcPos]) << shift;
/*  605 */       long mask = 65535L << shift;
/*  606 */       out = out & (mask ^ 0xFFFFFFFFFFFFFFFFL) | bits;
/*      */     } 
/*  608 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int shortArrayToInt(short[] src, int srcPos, int dstInit, int dstPos, int nShorts) {
/*  628 */     if ((src.length == 0 && srcPos == 0) || 0 == nShorts) {
/*  629 */       return dstInit;
/*      */     }
/*  631 */     if ((nShorts - 1) * 16 + dstPos >= 32) {
/*  632 */       throw new IllegalArgumentException("(nShorts-1)*16+dstPos is greater or equal to than 32");
/*      */     }
/*  634 */     int out = dstInit;
/*  635 */     for (int i = 0; i < nShorts; i++) {
/*  636 */       int shift = i * 16 + dstPos;
/*  637 */       int bits = (0xFFFF & src[i + srcPos]) << shift;
/*  638 */       int mask = 65535 << shift;
/*  639 */       out = out & (mask ^ 0xFFFFFFFF) | bits;
/*      */     } 
/*  641 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long byteArrayToLong(byte[] src, int srcPos, long dstInit, int dstPos, int nBytes) {
/*  661 */     if ((src.length == 0 && srcPos == 0) || 0 == nBytes) {
/*  662 */       return dstInit;
/*      */     }
/*  664 */     if ((nBytes - 1) * 8 + dstPos >= 64) {
/*  665 */       throw new IllegalArgumentException("(nBytes-1)*8+dstPos is greater or equal to than 64");
/*      */     }
/*  667 */     long out = dstInit;
/*  668 */     for (int i = 0; i < nBytes; i++) {
/*  669 */       int shift = i * 8 + dstPos;
/*  670 */       long bits = (0xFFL & src[i + srcPos]) << shift;
/*  671 */       long mask = 255L << shift;
/*  672 */       out = out & (mask ^ 0xFFFFFFFFFFFFFFFFL) | bits;
/*      */     } 
/*  674 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int byteArrayToInt(byte[] src, int srcPos, int dstInit, int dstPos, int nBytes) {
/*  694 */     if ((src.length == 0 && srcPos == 0) || 0 == nBytes) {
/*  695 */       return dstInit;
/*      */     }
/*  697 */     if ((nBytes - 1) * 8 + dstPos >= 32) {
/*  698 */       throw new IllegalArgumentException("(nBytes-1)*8+dstPos is greater or equal to than 32");
/*      */     }
/*  700 */     int out = dstInit;
/*  701 */     for (int i = 0; i < nBytes; i++) {
/*  702 */       int shift = i * 8 + dstPos;
/*  703 */       int bits = (0xFF & src[i + srcPos]) << shift;
/*  704 */       int mask = 255 << shift;
/*  705 */       out = out & (mask ^ 0xFFFFFFFF) | bits;
/*      */     } 
/*  707 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short byteArrayToShort(byte[] src, int srcPos, short dstInit, int dstPos, int nBytes) {
/*  727 */     if ((src.length == 0 && srcPos == 0) || 0 == nBytes) {
/*  728 */       return dstInit;
/*      */     }
/*  730 */     if ((nBytes - 1) * 8 + dstPos >= 16) {
/*  731 */       throw new IllegalArgumentException("(nBytes-1)*8+dstPos is greater or equal to than 16");
/*      */     }
/*  733 */     short out = dstInit;
/*  734 */     for (int i = 0; i < nBytes; i++) {
/*  735 */       int shift = i * 8 + dstPos;
/*  736 */       int bits = (0xFF & src[i + srcPos]) << shift;
/*  737 */       int mask = 255 << shift;
/*  738 */       out = (short)(out & (mask ^ 0xFFFFFFFF) | bits);
/*      */     } 
/*  740 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long hexToLong(String src, int srcPos, long dstInit, int dstPos, int nHex) {
/*  758 */     if (0 == nHex) {
/*  759 */       return dstInit;
/*      */     }
/*  761 */     if ((nHex - 1) * 4 + dstPos >= 64) {
/*  762 */       throw new IllegalArgumentException("(nHexs-1)*4+dstPos is greater or equal to than 64");
/*      */     }
/*  764 */     long out = dstInit;
/*  765 */     for (int i = 0; i < nHex; i++) {
/*  766 */       int shift = i * 4 + dstPos;
/*  767 */       long bits = (0xFL & hexDigitToInt(src.charAt(i + srcPos))) << shift;
/*  768 */       long mask = 15L << shift;
/*  769 */       out = out & (mask ^ 0xFFFFFFFFFFFFFFFFL) | bits;
/*      */     } 
/*  771 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int hexToInt(String src, int srcPos, int dstInit, int dstPos, int nHex) {
/*  788 */     if (0 == nHex) {
/*  789 */       return dstInit;
/*      */     }
/*  791 */     if ((nHex - 1) * 4 + dstPos >= 32) {
/*  792 */       throw new IllegalArgumentException("(nHexs-1)*4+dstPos is greater or equal to than 32");
/*      */     }
/*  794 */     int out = dstInit;
/*  795 */     for (int i = 0; i < nHex; i++) {
/*  796 */       int shift = i * 4 + dstPos;
/*  797 */       int bits = (0xF & hexDigitToInt(src.charAt(i + srcPos))) << shift;
/*  798 */       int mask = 15 << shift;
/*  799 */       out = out & (mask ^ 0xFFFFFFFF) | bits;
/*      */     } 
/*  801 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short hexToShort(String src, int srcPos, short dstInit, int dstPos, int nHex) {
/*  819 */     if (0 == nHex) {
/*  820 */       return dstInit;
/*      */     }
/*  822 */     if ((nHex - 1) * 4 + dstPos >= 16) {
/*  823 */       throw new IllegalArgumentException("(nHexs-1)*4+dstPos is greater or equal to than 16");
/*      */     }
/*  825 */     short out = dstInit;
/*  826 */     for (int i = 0; i < nHex; i++) {
/*  827 */       int shift = i * 4 + dstPos;
/*  828 */       int bits = (0xF & hexDigitToInt(src.charAt(i + srcPos))) << shift;
/*  829 */       int mask = 15 << shift;
/*  830 */       out = (short)(out & (mask ^ 0xFFFFFFFF) | bits);
/*      */     } 
/*  832 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte hexToByte(String src, int srcPos, byte dstInit, int dstPos, int nHex) {
/*  850 */     if (0 == nHex) {
/*  851 */       return dstInit;
/*      */     }
/*  853 */     if ((nHex - 1) * 4 + dstPos >= 8) {
/*  854 */       throw new IllegalArgumentException("(nHexs-1)*4+dstPos is greater or equal to than 8");
/*      */     }
/*  856 */     byte out = dstInit;
/*  857 */     for (int i = 0; i < nHex; i++) {
/*  858 */       int shift = i * 4 + dstPos;
/*  859 */       int bits = (0xF & hexDigitToInt(src.charAt(i + srcPos))) << shift;
/*  860 */       int mask = 15 << shift;
/*  861 */       out = (byte)(out & (mask ^ 0xFFFFFFFF) | bits);
/*      */     } 
/*  863 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long binaryToLong(boolean[] src, int srcPos, long dstInit, int dstPos, int nBools) {
/*  883 */     if ((src.length == 0 && srcPos == 0) || 0 == nBools) {
/*  884 */       return dstInit;
/*      */     }
/*  886 */     if (nBools - 1 + dstPos >= 64) {
/*  887 */       throw new IllegalArgumentException("nBools-1+dstPos is greater or equal to than 64");
/*      */     }
/*  889 */     long out = dstInit;
/*  890 */     for (int i = 0; i < nBools; i++) {
/*  891 */       int shift = i + dstPos;
/*  892 */       long bits = (src[i + srcPos] ? 1L : 0L) << shift;
/*  893 */       long mask = 1L << shift;
/*  894 */       out = out & (mask ^ 0xFFFFFFFFFFFFFFFFL) | bits;
/*      */     } 
/*  896 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int binaryToInt(boolean[] src, int srcPos, int dstInit, int dstPos, int nBools) {
/*  916 */     if ((src.length == 0 && srcPos == 0) || 0 == nBools) {
/*  917 */       return dstInit;
/*      */     }
/*  919 */     if (nBools - 1 + dstPos >= 32) {
/*  920 */       throw new IllegalArgumentException("nBools-1+dstPos is greater or equal to than 32");
/*      */     }
/*  922 */     int out = dstInit;
/*  923 */     for (int i = 0; i < nBools; i++) {
/*  924 */       int shift = i + dstPos;
/*  925 */       int bits = (src[i + srcPos] ? 1 : 0) << shift;
/*  926 */       int mask = 1 << shift;
/*  927 */       out = out & (mask ^ 0xFFFFFFFF) | bits;
/*      */     } 
/*  929 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short binaryToShort(boolean[] src, int srcPos, short dstInit, int dstPos, int nBools) {
/*  949 */     if ((src.length == 0 && srcPos == 0) || 0 == nBools) {
/*  950 */       return dstInit;
/*      */     }
/*  952 */     if (nBools - 1 + dstPos >= 16) {
/*  953 */       throw new IllegalArgumentException("nBools-1+dstPos is greater or equal to than 16");
/*      */     }
/*  955 */     short out = dstInit;
/*  956 */     for (int i = 0; i < nBools; i++) {
/*  957 */       int shift = i + dstPos;
/*  958 */       int bits = (src[i + srcPos] ? 1 : 0) << shift;
/*  959 */       int mask = 1 << shift;
/*  960 */       out = (short)(out & (mask ^ 0xFFFFFFFF) | bits);
/*      */     } 
/*  962 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte binaryToByte(boolean[] src, int srcPos, byte dstInit, int dstPos, int nBools) {
/*  982 */     if ((src.length == 0 && srcPos == 0) || 0 == nBools) {
/*  983 */       return dstInit;
/*      */     }
/*  985 */     if (nBools - 1 + dstPos >= 8) {
/*  986 */       throw new IllegalArgumentException("nBools-1+dstPos is greater or equal to than 8");
/*      */     }
/*  988 */     byte out = dstInit;
/*  989 */     for (int i = 0; i < nBools; i++) {
/*  990 */       int shift = i + dstPos;
/*  991 */       int bits = (src[i + srcPos] ? 1 : 0) << shift;
/*  992 */       int mask = 1 << shift;
/*  993 */       out = (byte)(out & (mask ^ 0xFFFFFFFF) | bits);
/*      */     } 
/*  995 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int[] longToIntArray(long src, int srcPos, int[] dst, int dstPos, int nInts) {
/* 1015 */     if (0 == nInts) {
/* 1016 */       return dst;
/*      */     }
/* 1018 */     if ((nInts - 1) * 32 + srcPos >= 64) {
/* 1019 */       throw new IllegalArgumentException("(nInts-1)*32+srcPos is greater or equal to than 64");
/*      */     }
/* 1021 */     for (int i = 0; i < nInts; i++) {
/* 1022 */       int shift = i * 32 + srcPos;
/* 1023 */       dst[dstPos + i] = (int)(0xFFFFFFFFFFFFFFFFL & src >> shift);
/*      */     } 
/* 1025 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short[] longToShortArray(long src, int srcPos, short[] dst, int dstPos, int nShorts) {
/* 1045 */     if (0 == nShorts) {
/* 1046 */       return dst;
/*      */     }
/* 1048 */     if ((nShorts - 1) * 16 + srcPos >= 64) {
/* 1049 */       throw new IllegalArgumentException("(nShorts-1)*16+srcPos is greater or equal to than 64");
/*      */     }
/* 1051 */     for (int i = 0; i < nShorts; i++) {
/* 1052 */       int shift = i * 16 + srcPos;
/* 1053 */       dst[dstPos + i] = (short)(int)(0xFFFFL & src >> shift);
/*      */     } 
/* 1055 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short[] intToShortArray(int src, int srcPos, short[] dst, int dstPos, int nShorts) {
/* 1075 */     if (0 == nShorts) {
/* 1076 */       return dst;
/*      */     }
/* 1078 */     if ((nShorts - 1) * 16 + srcPos >= 32) {
/* 1079 */       throw new IllegalArgumentException("(nShorts-1)*16+srcPos is greater or equal to than 32");
/*      */     }
/* 1081 */     for (int i = 0; i < nShorts; i++) {
/* 1082 */       int shift = i * 16 + srcPos;
/* 1083 */       dst[dstPos + i] = (short)(0xFFFF & src >> shift);
/*      */     } 
/* 1085 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] longToByteArray(long src, int srcPos, byte[] dst, int dstPos, int nBytes) {
/* 1105 */     if (0 == nBytes) {
/* 1106 */       return dst;
/*      */     }
/* 1108 */     if ((nBytes - 1) * 8 + srcPos >= 64) {
/* 1109 */       throw new IllegalArgumentException("(nBytes-1)*8+srcPos is greater or equal to than 64");
/*      */     }
/* 1111 */     for (int i = 0; i < nBytes; i++) {
/* 1112 */       int shift = i * 8 + srcPos;
/* 1113 */       dst[dstPos + i] = (byte)(int)(0xFFL & src >> shift);
/*      */     } 
/* 1115 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] intToByteArray(int src, int srcPos, byte[] dst, int dstPos, int nBytes) {
/* 1135 */     if (0 == nBytes) {
/* 1136 */       return dst;
/*      */     }
/* 1138 */     if ((nBytes - 1) * 8 + srcPos >= 32) {
/* 1139 */       throw new IllegalArgumentException("(nBytes-1)*8+srcPos is greater or equal to than 32");
/*      */     }
/* 1141 */     for (int i = 0; i < nBytes; i++) {
/* 1142 */       int shift = i * 8 + srcPos;
/* 1143 */       dst[dstPos + i] = (byte)(0xFF & src >> shift);
/*      */     } 
/* 1145 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] shortToByteArray(short src, int srcPos, byte[] dst, int dstPos, int nBytes) {
/* 1165 */     if (0 == nBytes) {
/* 1166 */       return dst;
/*      */     }
/* 1168 */     if ((nBytes - 1) * 8 + srcPos >= 16) {
/* 1169 */       throw new IllegalArgumentException("(nBytes-1)*8+srcPos is greater or equal to than 16");
/*      */     }
/* 1171 */     for (int i = 0; i < nBytes; i++) {
/* 1172 */       int shift = i * 8 + srcPos;
/* 1173 */       dst[dstPos + i] = (byte)(0xFF & src >> shift);
/*      */     } 
/* 1175 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String longToHex(long src, int srcPos, String dstInit, int dstPos, int nHexs) {
/* 1194 */     if (0 == nHexs) {
/* 1195 */       return dstInit;
/*      */     }
/* 1197 */     if ((nHexs - 1) * 4 + srcPos >= 64) {
/* 1198 */       throw new IllegalArgumentException("(nHexs-1)*4+srcPos is greater or equal to than 64");
/*      */     }
/* 1200 */     StringBuilder sb = new StringBuilder(dstInit);
/* 1201 */     int append = sb.length();
/* 1202 */     for (int i = 0; i < nHexs; i++) {
/* 1203 */       int shift = i * 4 + srcPos;
/* 1204 */       int bits = (int)(0xFL & src >> shift);
/* 1205 */       if (dstPos + i == append) {
/* 1206 */         append++;
/* 1207 */         sb.append(intToHexDigit(bits));
/*      */       } else {
/* 1209 */         sb.setCharAt(dstPos + i, intToHexDigit(bits));
/*      */       } 
/*      */     } 
/* 1212 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String intToHex(int src, int srcPos, String dstInit, int dstPos, int nHexs) {
/* 1231 */     if (0 == nHexs) {
/* 1232 */       return dstInit;
/*      */     }
/* 1234 */     if ((nHexs - 1) * 4 + srcPos >= 32) {
/* 1235 */       throw new IllegalArgumentException("(nHexs-1)*4+srcPos is greater or equal to than 32");
/*      */     }
/* 1237 */     StringBuilder sb = new StringBuilder(dstInit);
/* 1238 */     int append = sb.length();
/* 1239 */     for (int i = 0; i < nHexs; i++) {
/* 1240 */       int shift = i * 4 + srcPos;
/* 1241 */       int bits = 0xF & src >> shift;
/* 1242 */       if (dstPos + i == append) {
/* 1243 */         append++;
/* 1244 */         sb.append(intToHexDigit(bits));
/*      */       } else {
/* 1246 */         sb.setCharAt(dstPos + i, intToHexDigit(bits));
/*      */       } 
/*      */     } 
/* 1249 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String shortToHex(short src, int srcPos, String dstInit, int dstPos, int nHexs) {
/* 1268 */     if (0 == nHexs) {
/* 1269 */       return dstInit;
/*      */     }
/* 1271 */     if ((nHexs - 1) * 4 + srcPos >= 16) {
/* 1272 */       throw new IllegalArgumentException("(nHexs-1)*4+srcPos is greater or equal to than 16");
/*      */     }
/* 1274 */     StringBuilder sb = new StringBuilder(dstInit);
/* 1275 */     int append = sb.length();
/* 1276 */     for (int i = 0; i < nHexs; i++) {
/* 1277 */       int shift = i * 4 + srcPos;
/* 1278 */       int bits = 0xF & src >> shift;
/* 1279 */       if (dstPos + i == append) {
/* 1280 */         append++;
/* 1281 */         sb.append(intToHexDigit(bits));
/*      */       } else {
/* 1283 */         sb.setCharAt(dstPos + i, intToHexDigit(bits));
/*      */       } 
/*      */     } 
/* 1286 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String byteToHex(byte src, int srcPos, String dstInit, int dstPos, int nHexs) {
/* 1305 */     if (0 == nHexs) {
/* 1306 */       return dstInit;
/*      */     }
/* 1308 */     if ((nHexs - 1) * 4 + srcPos >= 8) {
/* 1309 */       throw new IllegalArgumentException("(nHexs-1)*4+srcPos is greater or equal to than 8");
/*      */     }
/* 1311 */     StringBuilder sb = new StringBuilder(dstInit);
/* 1312 */     int append = sb.length();
/* 1313 */     for (int i = 0; i < nHexs; i++) {
/* 1314 */       int shift = i * 4 + srcPos;
/* 1315 */       int bits = 0xF & src >> shift;
/* 1316 */       if (dstPos + i == append) {
/* 1317 */         append++;
/* 1318 */         sb.append(intToHexDigit(bits));
/*      */       } else {
/* 1320 */         sb.setCharAt(dstPos + i, intToHexDigit(bits));
/*      */       } 
/*      */     } 
/* 1323 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] longToBinary(long src, int srcPos, boolean[] dst, int dstPos, int nBools) {
/* 1343 */     if (0 == nBools) {
/* 1344 */       return dst;
/*      */     }
/* 1346 */     if (nBools - 1 + srcPos >= 64) {
/* 1347 */       throw new IllegalArgumentException("nBools-1+srcPos is greater or equal to than 64");
/*      */     }
/* 1349 */     for (int i = 0; i < nBools; i++) {
/* 1350 */       int shift = i + srcPos;
/* 1351 */       dst[dstPos + i] = ((0x1L & src >> shift) != 0L);
/*      */     } 
/* 1353 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] intToBinary(int src, int srcPos, boolean[] dst, int dstPos, int nBools) {
/* 1373 */     if (0 == nBools) {
/* 1374 */       return dst;
/*      */     }
/* 1376 */     if (nBools - 1 + srcPos >= 32) {
/* 1377 */       throw new IllegalArgumentException("nBools-1+srcPos is greater or equal to than 32");
/*      */     }
/* 1379 */     for (int i = 0; i < nBools; i++) {
/* 1380 */       int shift = i + srcPos;
/* 1381 */       dst[dstPos + i] = ((0x1 & src >> shift) != 0);
/*      */     } 
/* 1383 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] shortToBinary(short src, int srcPos, boolean[] dst, int dstPos, int nBools) {
/* 1403 */     if (0 == nBools) {
/* 1404 */       return dst;
/*      */     }
/* 1406 */     if (nBools - 1 + srcPos >= 16) {
/* 1407 */       throw new IllegalArgumentException("nBools-1+srcPos is greater or equal to than 16");
/*      */     }
/* 1409 */     assert nBools - 1 < 16 - srcPos;
/* 1410 */     for (int i = 0; i < nBools; i++) {
/* 1411 */       int shift = i + srcPos;
/* 1412 */       dst[dstPos + i] = ((0x1 & src >> shift) != 0);
/*      */     } 
/* 1414 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] byteToBinary(byte src, int srcPos, boolean[] dst, int dstPos, int nBools) {
/* 1434 */     if (0 == nBools) {
/* 1435 */       return dst;
/*      */     }
/* 1437 */     if (nBools - 1 + srcPos >= 8) {
/* 1438 */       throw new IllegalArgumentException("nBools-1+srcPos is greater or equal to than 8");
/*      */     }
/* 1440 */     for (int i = 0; i < nBools; i++) {
/* 1441 */       int shift = i + srcPos;
/* 1442 */       dst[dstPos + i] = ((0x1 & src >> shift) != 0);
/*      */     } 
/* 1444 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] uuidToByteArray(UUID src, byte[] dst, int dstPos, int nBytes) {
/* 1462 */     if (0 == nBytes) {
/* 1463 */       return dst;
/*      */     }
/* 1465 */     if (nBytes > 16) {
/* 1466 */       throw new IllegalArgumentException("nBytes is greater than 16");
/*      */     }
/* 1468 */     longToByteArray(src.getMostSignificantBits(), 0, dst, dstPos, Math.min(nBytes, 8));
/* 1469 */     if (nBytes >= 8) {
/* 1470 */       longToByteArray(src.getLeastSignificantBits(), 0, dst, dstPos + 8, nBytes - 8);
/*      */     }
/* 1472 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static UUID byteArrayToUuid(byte[] src, int srcPos) {
/* 1487 */     if (src.length - srcPos < 16) {
/* 1488 */       throw new IllegalArgumentException("Need at least 16 bytes for UUID");
/*      */     }
/* 1490 */     return new UUID(byteArrayToLong(src, srcPos, 0L, 0, 8), byteArrayToLong(src, srcPos + 8, 0L, 0, 8));
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\Conversion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */