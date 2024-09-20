/*      */ package com.google.common.net;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.CharMatcher;
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.hash.Hashing;
/*      */ import com.google.common.io.ByteStreams;
/*      */ import com.google.common.primitives.Ints;
/*      */ import java.math.BigInteger;
/*      */ import java.net.Inet4Address;
/*      */ import java.net.Inet6Address;
/*      */ import java.net.InetAddress;
/*      */ import java.net.UnknownHostException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.util.Arrays;
/*      */ import java.util.Locale;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Beta
/*      */ @GwtIncompatible
/*      */ public final class InetAddresses
/*      */ {
/*      */   private static final int IPV4_PART_COUNT = 4;
/*      */   private static final int IPV6_PART_COUNT = 8;
/*      */   private static final char IPV4_DELIMITER = '.';
/*      */   private static final char IPV6_DELIMITER = ':';
/*  105 */   private static final CharMatcher IPV4_DELIMITER_MATCHER = CharMatcher.is('.');
/*  106 */   private static final CharMatcher IPV6_DELIMITER_MATCHER = CharMatcher.is(':');
/*  107 */   private static final Inet4Address LOOPBACK4 = (Inet4Address)forString("127.0.0.1");
/*  108 */   private static final Inet4Address ANY4 = (Inet4Address)forString("0.0.0.0");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Inet4Address getInet4Address(byte[] bytes) {
/*  120 */     Preconditions.checkArgument((bytes.length == 4), "Byte array has invalid length for an IPv4 address: %s != 4.", bytes.length);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  126 */     return (Inet4Address)bytesToInetAddress(bytes);
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
/*      */   public static InetAddress forString(String ipString) {
/*  142 */     byte[] addr = ipStringToBytes(ipString);
/*      */ 
/*      */     
/*  145 */     if (addr == null) {
/*  146 */       throw formatIllegalArgumentException("'%s' is not an IP string literal.", new Object[] { ipString });
/*      */     }
/*      */     
/*  149 */     return bytesToInetAddress(addr);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isInetAddress(String ipString) {
/*  160 */     return (ipStringToBytes(ipString) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] ipStringToBytes(String ipString) {
/*  166 */     boolean hasColon = false;
/*  167 */     boolean hasDot = false;
/*  168 */     int percentIndex = -1;
/*  169 */     for (int i = 0; i < ipString.length(); i++) {
/*  170 */       char c = ipString.charAt(i);
/*  171 */       if (c == '.')
/*  172 */       { hasDot = true; }
/*  173 */       else if (c == ':')
/*  174 */       { if (hasDot) {
/*  175 */           return null;
/*      */         }
/*  177 */         hasColon = true; }
/*  178 */       else { if (c == '%') {
/*  179 */           percentIndex = i; break;
/*      */         } 
/*  181 */         if (Character.digit(c, 16) == -1) {
/*  182 */           return null;
/*      */         } }
/*      */     
/*      */     } 
/*      */     
/*  187 */     if (hasColon) {
/*  188 */       if (hasDot) {
/*  189 */         ipString = convertDottedQuadToHex(ipString);
/*  190 */         if (ipString == null) {
/*  191 */           return null;
/*      */         }
/*      */       } 
/*  194 */       if (percentIndex != -1) {
/*  195 */         ipString = ipString.substring(0, percentIndex);
/*      */       }
/*  197 */       return textToNumericFormatV6(ipString);
/*  198 */     }  if (hasDot) {
/*  199 */       if (percentIndex != -1) {
/*  200 */         return null;
/*      */       }
/*  202 */       return textToNumericFormatV4(ipString);
/*      */     } 
/*  204 */     return null;
/*      */   }
/*      */   
/*      */   private static byte[] textToNumericFormatV4(String ipString) {
/*  208 */     if (IPV4_DELIMITER_MATCHER.countIn(ipString) + 1 != 4) {
/*  209 */       return null;
/*      */     }
/*      */     
/*  212 */     byte[] bytes = new byte[4];
/*  213 */     int start = 0;
/*      */ 
/*      */     
/*  216 */     for (int i = 0; i < 4; i++) {
/*  217 */       int end = ipString.indexOf('.', start);
/*  218 */       if (end == -1) {
/*  219 */         end = ipString.length();
/*      */       }
/*      */       try {
/*  222 */         bytes[i] = parseOctet(ipString, start, end);
/*  223 */       } catch (NumberFormatException ex) {
/*  224 */         return null;
/*      */       } 
/*  226 */       start = end + 1;
/*      */     } 
/*      */     
/*  229 */     return bytes;
/*      */   }
/*      */ 
/*      */   
/*      */   private static byte[] textToNumericFormatV6(String ipString) {
/*  234 */     int delimiterCount = IPV6_DELIMITER_MATCHER.countIn(ipString);
/*  235 */     if (delimiterCount < 2 || delimiterCount > 8) {
/*  236 */       return null;
/*      */     }
/*  238 */     int partsSkipped = 8 - delimiterCount + 1;
/*  239 */     boolean hasSkip = false;
/*      */ 
/*      */     
/*  242 */     for (int i = 0; i < ipString.length() - 1; i++) {
/*  243 */       if (ipString.charAt(i) == ':' && ipString.charAt(i + 1) == ':') {
/*  244 */         if (hasSkip) {
/*  245 */           return null;
/*      */         }
/*  247 */         hasSkip = true;
/*  248 */         partsSkipped++;
/*  249 */         if (i == 0) {
/*  250 */           partsSkipped++;
/*      */         }
/*  252 */         if (i == ipString.length() - 2) {
/*  253 */           partsSkipped++;
/*      */         }
/*      */       } 
/*      */     } 
/*  257 */     if (ipString.charAt(0) == ':' && ipString.charAt(1) != ':') {
/*  258 */       return null;
/*      */     }
/*  260 */     if (ipString.charAt(ipString.length() - 1) == ':' && ipString
/*  261 */       .charAt(ipString.length() - 2) != ':') {
/*  262 */       return null;
/*      */     }
/*  264 */     if (hasSkip && partsSkipped <= 0) {
/*  265 */       return null;
/*      */     }
/*  267 */     if (!hasSkip && delimiterCount + 1 != 8) {
/*  268 */       return null;
/*      */     }
/*      */     
/*  271 */     ByteBuffer rawBytes = ByteBuffer.allocate(16);
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  276 */       int start = 0;
/*  277 */       if (ipString.charAt(0) == ':') {
/*  278 */         start = 1;
/*      */       }
/*  280 */       while (start < ipString.length()) {
/*  281 */         int end = ipString.indexOf(':', start);
/*  282 */         if (end == -1) {
/*  283 */           end = ipString.length();
/*      */         }
/*  285 */         if (ipString.charAt(start) == ':') {
/*      */           
/*  287 */           for (int j = 0; j < partsSkipped; j++) {
/*  288 */             rawBytes.putShort((short)0);
/*      */           }
/*      */         } else {
/*      */           
/*  292 */           rawBytes.putShort(parseHextet(ipString, start, end));
/*      */         } 
/*  294 */         start = end + 1;
/*      */       } 
/*  296 */     } catch (NumberFormatException ex) {
/*  297 */       return null;
/*      */     } 
/*  299 */     return rawBytes.array();
/*      */   }
/*      */   
/*      */   private static String convertDottedQuadToHex(String ipString) {
/*  303 */     int lastColon = ipString.lastIndexOf(':');
/*  304 */     String initialPart = ipString.substring(0, lastColon + 1);
/*  305 */     String dottedQuad = ipString.substring(lastColon + 1);
/*  306 */     byte[] quad = textToNumericFormatV4(dottedQuad);
/*  307 */     if (quad == null) {
/*  308 */       return null;
/*      */     }
/*  310 */     String penultimate = Integer.toHexString((quad[0] & 0xFF) << 8 | quad[1] & 0xFF);
/*  311 */     String ultimate = Integer.toHexString((quad[2] & 0xFF) << 8 | quad[3] & 0xFF);
/*  312 */     return (new StringBuilder(1 + String.valueOf(initialPart).length() + String.valueOf(penultimate).length() + String.valueOf(ultimate).length())).append(initialPart).append(penultimate).append(":").append(ultimate).toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte parseOctet(String ipString, int start, int end) {
/*  318 */     int length = end - start;
/*  319 */     if (length <= 0 || length > 3) {
/*  320 */       throw new NumberFormatException();
/*      */     }
/*      */ 
/*      */     
/*  324 */     if (length > 1 && ipString.charAt(start) == '0') {
/*  325 */       throw new NumberFormatException();
/*      */     }
/*  327 */     int octet = 0;
/*  328 */     for (int i = start; i < end; i++) {
/*  329 */       octet *= 10;
/*  330 */       int digit = Character.digit(ipString.charAt(i), 10);
/*  331 */       if (digit < 0) {
/*  332 */         throw new NumberFormatException();
/*      */       }
/*  334 */       octet += digit;
/*      */     } 
/*  336 */     if (octet > 255) {
/*  337 */       throw new NumberFormatException();
/*      */     }
/*  339 */     return (byte)octet;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static short parseHextet(String ipString, int start, int end) {
/*  345 */     int length = end - start;
/*  346 */     if (length <= 0 || length > 4) {
/*  347 */       throw new NumberFormatException();
/*      */     }
/*  349 */     int hextet = 0;
/*  350 */     for (int i = start; i < end; i++) {
/*  351 */       hextet <<= 4;
/*  352 */       hextet |= Character.digit(ipString.charAt(i), 16);
/*      */     } 
/*  354 */     return (short)hextet;
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
/*      */   private static InetAddress bytesToInetAddress(byte[] addr) {
/*      */     try {
/*  369 */       return InetAddress.getByAddress(addr);
/*  370 */     } catch (UnknownHostException e) {
/*  371 */       throw new AssertionError(e);
/*      */     } 
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
/*      */   public static String toAddrString(InetAddress ip) {
/*  391 */     Preconditions.checkNotNull(ip);
/*  392 */     if (ip instanceof Inet4Address)
/*      */     {
/*  394 */       return ip.getHostAddress();
/*      */     }
/*  396 */     Preconditions.checkArgument(ip instanceof Inet6Address);
/*  397 */     byte[] bytes = ip.getAddress();
/*  398 */     int[] hextets = new int[8];
/*  399 */     for (int i = 0; i < hextets.length; i++) {
/*  400 */       hextets[i] = Ints.fromBytes((byte)0, (byte)0, bytes[2 * i], bytes[2 * i + 1]);
/*      */     }
/*  402 */     compressLongestRunOfZeroes(hextets);
/*  403 */     return hextetsToIPv6String(hextets);
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
/*      */   private static void compressLongestRunOfZeroes(int[] hextets) {
/*  415 */     int bestRunStart = -1;
/*  416 */     int bestRunLength = -1;
/*  417 */     int runStart = -1;
/*  418 */     for (int i = 0; i < hextets.length + 1; i++) {
/*  419 */       if (i < hextets.length && hextets[i] == 0) {
/*  420 */         if (runStart < 0) {
/*  421 */           runStart = i;
/*      */         }
/*  423 */       } else if (runStart >= 0) {
/*  424 */         int runLength = i - runStart;
/*  425 */         if (runLength > bestRunLength) {
/*  426 */           bestRunStart = runStart;
/*  427 */           bestRunLength = runLength;
/*      */         } 
/*  429 */         runStart = -1;
/*      */       } 
/*      */     } 
/*  432 */     if (bestRunLength >= 2) {
/*  433 */       Arrays.fill(hextets, bestRunStart, bestRunStart + bestRunLength, -1);
/*      */     }
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
/*      */   private static String hextetsToIPv6String(int[] hextets) {
/*  450 */     StringBuilder buf = new StringBuilder(39);
/*  451 */     boolean lastWasNumber = false;
/*  452 */     for (int i = 0; i < hextets.length; i++) {
/*  453 */       boolean thisIsNumber = (hextets[i] >= 0);
/*  454 */       if (thisIsNumber) {
/*  455 */         if (lastWasNumber) {
/*  456 */           buf.append(':');
/*      */         }
/*  458 */         buf.append(Integer.toHexString(hextets[i]));
/*      */       }
/*  460 */       else if (i == 0 || lastWasNumber) {
/*  461 */         buf.append("::");
/*      */       } 
/*      */       
/*  464 */       lastWasNumber = thisIsNumber;
/*      */     } 
/*  466 */     return buf.toString();
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
/*      */ 
/*      */   
/*      */   public static String toUriString(InetAddress ip) {
/*  489 */     if (ip instanceof Inet6Address) {
/*  490 */       String str = toAddrString(ip); return (new StringBuilder(2 + String.valueOf(str).length())).append("[").append(str).append("]").toString();
/*      */     } 
/*  492 */     return toAddrString(ip);
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
/*      */   public static InetAddress forUriString(String hostAddr) {
/*  510 */     InetAddress addr = forUriStringNoThrow(hostAddr);
/*  511 */     if (addr == null) {
/*  512 */       throw formatIllegalArgumentException("Not a valid URI IP literal: '%s'", new Object[] { hostAddr });
/*      */     }
/*      */     
/*  515 */     return addr;
/*      */   } private static InetAddress forUriStringNoThrow(String hostAddr) {
/*      */     String ipString;
/*      */     int expectBytes;
/*  519 */     Preconditions.checkNotNull(hostAddr);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  524 */     if (hostAddr.startsWith("[") && hostAddr.endsWith("]")) {
/*  525 */       ipString = hostAddr.substring(1, hostAddr.length() - 1);
/*  526 */       expectBytes = 16;
/*      */     } else {
/*  528 */       ipString = hostAddr;
/*  529 */       expectBytes = 4;
/*      */     } 
/*      */ 
/*      */     
/*  533 */     byte[] addr = ipStringToBytes(ipString);
/*  534 */     if (addr == null || addr.length != expectBytes) {
/*  535 */       return null;
/*      */     }
/*      */     
/*  538 */     return bytesToInetAddress(addr);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isUriInetAddress(String ipString) {
/*  549 */     return (forUriStringNoThrow(ipString) != null);
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
/*      */   
/*      */   public static boolean isCompatIPv4Address(Inet6Address ip) {
/*  571 */     if (!ip.isIPv4CompatibleAddress()) {
/*  572 */       return false;
/*      */     }
/*      */     
/*  575 */     byte[] bytes = ip.getAddress();
/*  576 */     if (bytes[12] == 0 && bytes[13] == 0 && bytes[14] == 0 && (bytes[15] == 0 || bytes[15] == 1))
/*      */     {
/*      */ 
/*      */       
/*  580 */       return false;
/*      */     }
/*      */     
/*  583 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Inet4Address getCompatIPv4Address(Inet6Address ip) {
/*  594 */     Preconditions.checkArgument(
/*  595 */         isCompatIPv4Address(ip), "Address '%s' is not IPv4-compatible.", toAddrString(ip));
/*      */     
/*  597 */     return getInet4Address(Arrays.copyOfRange(ip.getAddress(), 12, 16));
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
/*      */   public static boolean is6to4Address(Inet6Address ip) {
/*  613 */     byte[] bytes = ip.getAddress();
/*  614 */     return (bytes[0] == 32 && bytes[1] == 2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Inet4Address get6to4IPv4Address(Inet6Address ip) {
/*  625 */     Preconditions.checkArgument(is6to4Address(ip), "Address '%s' is not a 6to4 address.", toAddrString(ip));
/*      */     
/*  627 */     return getInet4Address(Arrays.copyOfRange(ip.getAddress(), 2, 6));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static final class TeredoInfo
/*      */   {
/*      */     private final Inet4Address server;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final Inet4Address client;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int port;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int flags;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TeredoInfo(Inet4Address server, Inet4Address client, int port, int flags) {
/*  662 */       Preconditions.checkArgument((port >= 0 && port <= 65535), "port '%s' is out of range (0 <= port <= 0xffff)", port);
/*      */       
/*  664 */       Preconditions.checkArgument((flags >= 0 && flags <= 65535), "flags '%s' is out of range (0 <= flags <= 0xffff)", flags);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  669 */       this.server = (Inet4Address)MoreObjects.firstNonNull(server, InetAddresses.ANY4);
/*  670 */       this.client = (Inet4Address)MoreObjects.firstNonNull(client, InetAddresses.ANY4);
/*  671 */       this.port = port;
/*  672 */       this.flags = flags;
/*      */     }
/*      */     
/*      */     public Inet4Address getServer() {
/*  676 */       return this.server;
/*      */     }
/*      */     
/*      */     public Inet4Address getClient() {
/*  680 */       return this.client;
/*      */     }
/*      */     
/*      */     public int getPort() {
/*  684 */       return this.port;
/*      */     }
/*      */     
/*      */     public int getFlags() {
/*  688 */       return this.flags;
/*      */     }
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
/*      */   public static boolean isTeredoAddress(Inet6Address ip) {
/*  701 */     byte[] bytes = ip.getAddress();
/*  702 */     return (bytes[0] == 32 && bytes[1] == 1 && bytes[2] == 0 && bytes[3] == 0);
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
/*      */   public static TeredoInfo getTeredoInfo(Inet6Address ip) {
/*  716 */     Preconditions.checkArgument(isTeredoAddress(ip), "Address '%s' is not a Teredo address.", toAddrString(ip));
/*      */     
/*  718 */     byte[] bytes = ip.getAddress();
/*  719 */     Inet4Address server = getInet4Address(Arrays.copyOfRange(bytes, 4, 8));
/*      */     
/*  721 */     int flags = ByteStreams.newDataInput(bytes, 8).readShort() & 0xFFFF;
/*      */ 
/*      */     
/*  724 */     int port = (ByteStreams.newDataInput(bytes, 10).readShort() ^ 0xFFFFFFFF) & 0xFFFF;
/*      */     
/*  726 */     byte[] clientBytes = Arrays.copyOfRange(bytes, 12, 16);
/*  727 */     for (int i = 0; i < clientBytes.length; i++)
/*      */     {
/*  729 */       clientBytes[i] = (byte)(clientBytes[i] ^ 0xFFFFFFFF);
/*      */     }
/*  731 */     Inet4Address client = getInet4Address(clientBytes);
/*      */     
/*  733 */     return new TeredoInfo(server, client, port, flags);
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
/*      */   public static boolean isIsatapAddress(Inet6Address ip) {
/*  753 */     if (isTeredoAddress(ip)) {
/*  754 */       return false;
/*      */     }
/*      */     
/*  757 */     byte[] bytes = ip.getAddress();
/*      */     
/*  759 */     if ((bytes[8] | 0x3) != 3)
/*      */     {
/*      */ 
/*      */       
/*  763 */       return false;
/*      */     }
/*      */     
/*  766 */     return (bytes[9] == 0 && bytes[10] == 94 && bytes[11] == -2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Inet4Address getIsatapIPv4Address(Inet6Address ip) {
/*  777 */     Preconditions.checkArgument(isIsatapAddress(ip), "Address '%s' is not an ISATAP address.", toAddrString(ip));
/*      */     
/*  779 */     return getInet4Address(Arrays.copyOfRange(ip.getAddress(), 12, 16));
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
/*      */   public static boolean hasEmbeddedIPv4ClientAddress(Inet6Address ip) {
/*  795 */     return (isCompatIPv4Address(ip) || is6to4Address(ip) || isTeredoAddress(ip));
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
/*      */   public static Inet4Address getEmbeddedIPv4ClientAddress(Inet6Address ip) {
/*  811 */     if (isCompatIPv4Address(ip)) {
/*  812 */       return getCompatIPv4Address(ip);
/*      */     }
/*      */     
/*  815 */     if (is6to4Address(ip)) {
/*  816 */       return get6to4IPv4Address(ip);
/*      */     }
/*      */     
/*  819 */     if (isTeredoAddress(ip)) {
/*  820 */       return getTeredoInfo(ip).getClient();
/*      */     }
/*      */     
/*  823 */     throw formatIllegalArgumentException("'%s' has no embedded IPv4 address.", new Object[] { toAddrString(ip) });
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
/*      */   
/*      */   public static boolean isMappedIPv4Address(String ipString) {
/*  845 */     byte[] bytes = ipStringToBytes(ipString);
/*  846 */     if (bytes != null && bytes.length == 16) {
/*  847 */       int i; for (i = 0; i < 10; i++) {
/*  848 */         if (bytes[i] != 0) {
/*  849 */           return false;
/*      */         }
/*      */       } 
/*  852 */       for (i = 10; i < 12; i++) {
/*  853 */         if (bytes[i] != -1) {
/*  854 */           return false;
/*      */         }
/*      */       } 
/*  857 */       return true;
/*      */     } 
/*  859 */     return false;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Inet4Address getCoercedIPv4Address(InetAddress ip) {
/*  886 */     if (ip instanceof Inet4Address) {
/*  887 */       return (Inet4Address)ip;
/*      */     }
/*      */ 
/*      */     
/*  891 */     byte[] bytes = ip.getAddress();
/*  892 */     boolean leadingBytesOfZero = true;
/*  893 */     for (int i = 0; i < 15; i++) {
/*  894 */       if (bytes[i] != 0) {
/*  895 */         leadingBytesOfZero = false;
/*      */         break;
/*      */       } 
/*      */     } 
/*  899 */     if (leadingBytesOfZero && bytes[15] == 1)
/*  900 */       return LOOPBACK4; 
/*  901 */     if (leadingBytesOfZero && bytes[15] == 0) {
/*  902 */       return ANY4;
/*      */     }
/*      */     
/*  905 */     Inet6Address ip6 = (Inet6Address)ip;
/*  906 */     long addressAsLong = 0L;
/*  907 */     if (hasEmbeddedIPv4ClientAddress(ip6)) {
/*  908 */       addressAsLong = getEmbeddedIPv4ClientAddress(ip6).hashCode();
/*      */     } else {
/*      */       
/*  911 */       addressAsLong = ByteBuffer.wrap(ip6.getAddress(), 0, 8).getLong();
/*      */     } 
/*      */ 
/*      */     
/*  915 */     int coercedHash = Hashing.murmur3_32().hashLong(addressAsLong).asInt();
/*      */ 
/*      */     
/*  918 */     coercedHash |= 0xE0000000;
/*      */ 
/*      */ 
/*      */     
/*  922 */     if (coercedHash == -1) {
/*  923 */       coercedHash = -2;
/*      */     }
/*      */     
/*  926 */     return getInet4Address(Ints.toByteArray(coercedHash));
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
/*      */   
/*      */   public static int coerceToInteger(InetAddress ip) {
/*  948 */     return ByteStreams.newDataInput(getCoercedIPv4Address(ip).getAddress()).readInt();
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
/*      */   public static BigInteger toBigInteger(InetAddress address) {
/*  961 */     return new BigInteger(1, address.getAddress());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Inet4Address fromInteger(int address) {
/*  971 */     return getInet4Address(Ints.toByteArray(address));
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
/*      */   public static Inet4Address fromIPv4BigInteger(BigInteger address) {
/*  983 */     return (Inet4Address)fromBigInteger(address, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Inet6Address fromIPv6BigInteger(BigInteger address) {
/*  994 */     return (Inet6Address)fromBigInteger(address, true);
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
/*      */   private static InetAddress fromBigInteger(BigInteger address, boolean isIpv6) {
/* 1008 */     Preconditions.checkArgument((address.signum() >= 0), "BigInteger must be greater than or equal to 0");
/*      */     
/* 1010 */     int numBytes = isIpv6 ? 16 : 4;
/*      */     
/* 1012 */     byte[] addressBytes = address.toByteArray();
/* 1013 */     byte[] targetCopyArray = new byte[numBytes];
/*      */     
/* 1015 */     int srcPos = Math.max(0, addressBytes.length - numBytes);
/* 1016 */     int copyLength = addressBytes.length - srcPos;
/* 1017 */     int destPos = numBytes - copyLength;
/*      */ 
/*      */     
/* 1020 */     for (int i = 0; i < srcPos; i++) {
/* 1021 */       if (addressBytes[i] != 0) {
/* 1022 */         throw formatIllegalArgumentException("BigInteger cannot be converted to InetAddress because it has more than %d bytes: %s", new Object[] {
/*      */ 
/*      */               
/* 1025 */               Integer.valueOf(numBytes), address
/*      */             });
/*      */       }
/*      */     } 
/*      */     
/* 1030 */     System.arraycopy(addressBytes, srcPos, targetCopyArray, destPos, copyLength);
/*      */     
/*      */     try {
/* 1033 */       return InetAddress.getByAddress(targetCopyArray);
/* 1034 */     } catch (UnknownHostException impossible) {
/* 1035 */       throw new AssertionError(impossible);
/*      */     } 
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
/*      */   public static InetAddress fromLittleEndianByteArray(byte[] addr) throws UnknownHostException {
/* 1050 */     byte[] reversed = new byte[addr.length];
/* 1051 */     for (int i = 0; i < addr.length; i++) {
/* 1052 */       reversed[i] = addr[addr.length - i - 1];
/*      */     }
/* 1054 */     return InetAddress.getByAddress(reversed);
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
/*      */   public static InetAddress decrement(InetAddress address) {
/* 1067 */     byte[] addr = address.getAddress();
/* 1068 */     int i = addr.length - 1;
/* 1069 */     while (i >= 0 && addr[i] == 0) {
/* 1070 */       addr[i] = -1;
/* 1071 */       i--;
/*      */     } 
/*      */     
/* 1074 */     Preconditions.checkArgument((i >= 0), "Decrementing %s would wrap.", address);
/*      */     
/* 1076 */     addr[i] = (byte)(addr[i] - 1);
/* 1077 */     return bytesToInetAddress(addr);
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
/*      */   public static InetAddress increment(InetAddress address) {
/* 1090 */     byte[] addr = address.getAddress();
/* 1091 */     int i = addr.length - 1;
/* 1092 */     while (i >= 0 && addr[i] == -1) {
/* 1093 */       addr[i] = 0;
/* 1094 */       i--;
/*      */     } 
/*      */     
/* 1097 */     Preconditions.checkArgument((i >= 0), "Incrementing %s would wrap.", address);
/*      */     
/* 1099 */     addr[i] = (byte)(addr[i] + 1);
/* 1100 */     return bytesToInetAddress(addr);
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
/*      */   public static boolean isMaximum(InetAddress address) {
/* 1112 */     byte[] addr = address.getAddress();
/* 1113 */     for (int i = 0; i < addr.length; i++) {
/* 1114 */       if (addr[i] != -1) {
/* 1115 */         return false;
/*      */       }
/*      */     } 
/* 1118 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private static IllegalArgumentException formatIllegalArgumentException(String format, Object... args) {
/* 1123 */     return new IllegalArgumentException(String.format(Locale.ROOT, format, args));
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\net\InetAddresses.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */