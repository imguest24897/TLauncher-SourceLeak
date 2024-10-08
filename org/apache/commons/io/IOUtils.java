/*      */ package org.apache.commons.io;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.CharArrayWriter;
/*      */ import java.io.Closeable;
/*      */ import java.io.EOFException;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.Reader;
/*      */ import java.io.Writer;
/*      */ import java.net.HttpURLConnection;
/*      */ import java.net.ServerSocket;
/*      */ import java.net.Socket;
/*      */ import java.net.URI;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.CharBuffer;
/*      */ import java.nio.channels.ReadableByteChannel;
/*      */ import java.nio.channels.Selector;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.file.Files;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.function.Consumer;
/*      */ import org.apache.commons.io.function.IOConsumer;
/*      */ import org.apache.commons.io.output.AppendableWriter;
/*      */ import org.apache.commons.io.output.ByteArrayOutputStream;
/*      */ import org.apache.commons.io.output.NullOutputStream;
/*      */ import org.apache.commons.io.output.StringBuilderWriter;
/*      */ import org.apache.commons.io.output.ThresholdingOutputStream;
/*      */ import org.apache.commons.io.output.UnsynchronizedByteArrayOutputStream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class IOUtils
/*      */ {
/*      */   public static final int CR = 13;
/*      */   public static final int DEFAULT_BUFFER_SIZE = 8192;
/*  126 */   public static final char DIR_SEPARATOR = File.separatorChar;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final char DIR_SEPARATOR_UNIX = '/';
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final char DIR_SEPARATOR_WINDOWS = '\\';
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  143 */   public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int EOF = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int LF = 10;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  164 */   public static final String LINE_SEPARATOR = System.lineSeparator();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  171 */   public static final String LINE_SEPARATOR_UNIX = StandardLineSeparator.LF.getString();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  178 */   public static final String LINE_SEPARATOR_WINDOWS = StandardLineSeparator.CRLF.getString();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  183 */   private static final ThreadLocal<byte[]> SKIP_BYTE_BUFFER = (ThreadLocal)ThreadLocal.withInitial(IOUtils::byteArray);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  188 */   private static final ThreadLocal<char[]> SKIP_CHAR_BUFFER = (ThreadLocal)ThreadLocal.withInitial(IOUtils::charArray);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BufferedInputStream buffer(InputStream inputStream) {
/*  203 */     Objects.requireNonNull(inputStream, "inputStream");
/*  204 */     return (inputStream instanceof BufferedInputStream) ? (BufferedInputStream)inputStream : new BufferedInputStream(inputStream);
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
/*      */   public static BufferedInputStream buffer(InputStream inputStream, int size) {
/*  222 */     Objects.requireNonNull(inputStream, "inputStream");
/*  223 */     return (inputStream instanceof BufferedInputStream) ? (BufferedInputStream)inputStream : new BufferedInputStream(inputStream, size);
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
/*      */   public static BufferedOutputStream buffer(OutputStream outputStream) {
/*  240 */     Objects.requireNonNull(outputStream, "outputStream");
/*  241 */     return (outputStream instanceof BufferedOutputStream) ? (BufferedOutputStream)outputStream : new BufferedOutputStream(outputStream);
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
/*      */   public static BufferedOutputStream buffer(OutputStream outputStream, int size) {
/*  259 */     Objects.requireNonNull(outputStream, "outputStream");
/*  260 */     return (outputStream instanceof BufferedOutputStream) ? (BufferedOutputStream)outputStream : new BufferedOutputStream(outputStream, size);
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
/*      */   public static BufferedReader buffer(Reader reader) {
/*  274 */     return (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader);
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
/*      */   public static BufferedReader buffer(Reader reader, int size) {
/*  288 */     return (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader, size);
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
/*      */   public static BufferedWriter buffer(Writer writer) {
/*  301 */     return (writer instanceof BufferedWriter) ? (BufferedWriter)writer : new BufferedWriter(writer);
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
/*      */   public static BufferedWriter buffer(Writer writer, int size) {
/*  315 */     return (writer instanceof BufferedWriter) ? (BufferedWriter)writer : new BufferedWriter(writer, size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] byteArray() {
/*  325 */     return byteArray(8192);
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
/*      */   public static byte[] byteArray(int size) {
/*  338 */     return new byte[size];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static char[] charArray() {
/*  348 */     return charArray(8192);
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
/*      */   private static char[] charArray(int size) {
/*  361 */     return new char[size];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void close(Closeable closeable) throws IOException {
/*  372 */     if (closeable != null) {
/*  373 */       closeable.close();
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
/*      */   public static void close(Closeable... closeables) throws IOException {
/*  385 */     if (closeables != null) {
/*  386 */       for (Closeable closeable : closeables) {
/*  387 */         close(closeable);
/*      */       }
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
/*      */   public static void close(Closeable closeable, IOConsumer<IOException> consumer) throws IOException {
/*  401 */     if (closeable != null) {
/*      */       try {
/*  403 */         closeable.close();
/*  404 */       } catch (IOException e) {
/*  405 */         if (consumer != null) {
/*  406 */           consumer.accept(e);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void close(URLConnection conn) {
/*  419 */     if (conn instanceof HttpURLConnection) {
/*  420 */       ((HttpURLConnection)conn).disconnect();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(Closeable closeable) {
/*  466 */     closeQuietly(closeable, (Consumer<IOException>)null);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(Closeable... closeables) {
/*  516 */     if (closeables == null) {
/*      */       return;
/*      */     }
/*  519 */     for (Closeable closeable : closeables) {
/*  520 */       closeQuietly(closeable);
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
/*      */   public static void closeQuietly(Closeable closeable, Consumer<IOException> consumer) {
/*  532 */     if (closeable != null) {
/*      */       try {
/*  534 */         closeable.close();
/*  535 */       } catch (IOException e) {
/*  536 */         if (consumer != null) {
/*  537 */           consumer.accept(e);
/*      */         }
/*      */       } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(InputStream input) {
/*  571 */     closeQuietly(input);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(OutputStream output) {
/*  603 */     closeQuietly(output);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(Reader reader) {
/*  634 */     closeQuietly(reader);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(Selector selector) {
/*  665 */     closeQuietly(selector);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(ServerSocket serverSocket) {
/*  696 */     closeQuietly(serverSocket);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(Socket socket) {
/*  727 */     closeQuietly(socket);
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
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(Writer writer) {
/*  757 */     closeQuietly(writer);
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
/*      */   public static long consume(InputStream input) throws IOException {
/*  775 */     return copyLarge(input, (OutputStream)NullOutputStream.NULL_OUTPUT_STREAM, getByteArray());
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
/*      */   public static boolean contentEquals(InputStream input1, InputStream input2) throws IOException {
/*  796 */     if (input1 == input2) {
/*  797 */       return true;
/*      */     }
/*  799 */     if (input1 == null || input2 == null) {
/*  800 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  804 */     byte[] array1 = getByteArray();
/*      */     
/*  806 */     byte[] array2 = byteArray();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/*  812 */       int pos1 = 0;
/*  813 */       int pos2 = 0;
/*  814 */       for (int index = 0; index < 8192; index++) {
/*  815 */         if (pos1 == index)
/*      */           while (true) {
/*  817 */             int count1 = input1.read(array1, pos1, 8192 - pos1);
/*  818 */             if (count1 != 0) {
/*  819 */               if (count1 == -1) {
/*  820 */                 return (pos2 == index && input2.read() == -1);
/*      */               }
/*  822 */               pos1 += count1; break;
/*      */             } 
/*  824 */           }   if (pos2 == index)
/*      */           while (true) {
/*  826 */             int count2 = input2.read(array2, pos2, 8192 - pos2);
/*  827 */             if (count2 != 0) {
/*  828 */               if (count2 == -1) {
/*  829 */                 return (pos1 == index && input1.read() == -1);
/*      */               }
/*  831 */               pos2 += count2; break;
/*      */             } 
/*  833 */           }   if (array1[index] != array2[index]) {
/*  834 */           return false;
/*      */         }
/*      */       } 
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
/*      */   public static boolean contentEquals(Reader input1, Reader input2) throws IOException {
/*  854 */     if (input1 == input2) {
/*  855 */       return true;
/*      */     }
/*  857 */     if (input1 == null || input2 == null) {
/*  858 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  862 */     char[] array1 = getCharArray();
/*      */     
/*  864 */     char[] array2 = charArray();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/*  870 */       int pos1 = 0;
/*  871 */       int pos2 = 0;
/*  872 */       for (int index = 0; index < 8192; index++) {
/*  873 */         if (pos1 == index)
/*      */           while (true) {
/*  875 */             int count1 = input1.read(array1, pos1, 8192 - pos1);
/*  876 */             if (count1 != 0) {
/*  877 */               if (count1 == -1) {
/*  878 */                 return (pos2 == index && input2.read() == -1);
/*      */               }
/*  880 */               pos1 += count1; break;
/*      */             } 
/*  882 */           }   if (pos2 == index)
/*      */           while (true) {
/*  884 */             int count2 = input2.read(array2, pos2, 8192 - pos2);
/*  885 */             if (count2 != 0) {
/*  886 */               if (count2 == -1) {
/*  887 */                 return (pos1 == index && input1.read() == -1);
/*      */               }
/*  889 */               pos2 += count2; break;
/*      */             } 
/*  891 */           }   if (array1[index] != array2[index]) {
/*  892 */           return false;
/*      */         }
/*      */       } 
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
/*      */   
/*      */   public static boolean contentEqualsIgnoreEOL(Reader reader1, Reader reader2) throws IOException {
/*  915 */     if (reader1 == reader2) {
/*  916 */       return true;
/*      */     }
/*  918 */     if ((((reader1 == null) ? 1 : 0) ^ ((reader2 == null) ? 1 : 0)) != 0) {
/*  919 */       return false;
/*      */     }
/*  921 */     BufferedReader br1 = toBufferedReader(reader1);
/*  922 */     BufferedReader br2 = toBufferedReader(reader2);
/*      */     
/*  924 */     String line1 = br1.readLine();
/*  925 */     String line2 = br2.readLine();
/*  926 */     while (line1 != null && line1.equals(line2)) {
/*  927 */       line1 = br1.readLine();
/*  928 */       line2 = br2.readLine();
/*      */     } 
/*  930 */     return Objects.equals(line1, line2);
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
/*      */   public static int copy(InputStream inputStream, OutputStream outputStream) throws IOException {
/*  953 */     long count = copyLarge(inputStream, outputStream);
/*  954 */     if (count > 2147483647L) {
/*  955 */       return -1;
/*      */     }
/*  957 */     return (int)count;
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
/*      */   public static long copy(InputStream inputStream, OutputStream outputStream, int bufferSize) throws IOException {
/*  978 */     return copyLarge(inputStream, outputStream, byteArray(bufferSize));
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
/*      */   @Deprecated
/*      */   public static void copy(InputStream input, Writer writer) throws IOException {
/* 1000 */     copy(input, writer, Charset.defaultCharset());
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
/*      */   public static void copy(InputStream input, Writer writer, Charset inputCharset) throws IOException {
/* 1021 */     InputStreamReader reader = new InputStreamReader(input, Charsets.toCharset(inputCharset));
/* 1022 */     copy(reader, writer);
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
/*      */   public static void copy(InputStream input, Writer writer, String inputCharsetName) throws IOException {
/* 1049 */     copy(input, writer, Charsets.toCharset(inputCharsetName));
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
/*      */   public static long copy(Reader reader, Appendable output) throws IOException {
/* 1071 */     return copy(reader, output, CharBuffer.allocate(8192));
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
/*      */   public static long copy(Reader reader, Appendable output, CharBuffer buffer) throws IOException {
/* 1090 */     long count = 0L;
/*      */     int n;
/* 1092 */     while (-1 != (n = reader.read(buffer))) {
/* 1093 */       buffer.flip();
/* 1094 */       output.append(buffer, 0, n);
/* 1095 */       count += n;
/*      */     } 
/* 1097 */     return count;
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
/*      */   @Deprecated
/*      */   public static void copy(Reader reader, OutputStream output) throws IOException {
/* 1123 */     copy(reader, output, Charset.defaultCharset());
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
/*      */   
/*      */   public static void copy(Reader reader, OutputStream output, Charset outputCharset) throws IOException {
/* 1151 */     OutputStreamWriter writer = new OutputStreamWriter(output, Charsets.toCharset(outputCharset));
/* 1152 */     copy(reader, writer);
/*      */ 
/*      */     
/* 1155 */     writer.flush();
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copy(Reader reader, OutputStream output, String outputCharsetName) throws IOException {
/* 1186 */     copy(reader, output, Charsets.toCharset(outputCharsetName));
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
/*      */   public static int copy(Reader reader, Writer writer) throws IOException {
/* 1208 */     long count = copyLarge(reader, writer);
/* 1209 */     if (count > 2147483647L) {
/* 1210 */       return -1;
/*      */     }
/* 1212 */     return (int)count;
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
/*      */   public static long copy(URL url, File file) throws IOException {
/* 1233 */     try (OutputStream outputStream = Files.newOutputStream(((File)Objects.<File>requireNonNull(file, "file")).toPath(), new java.nio.file.OpenOption[0])) {
/* 1234 */       return copy(url, outputStream);
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
/*      */ 
/*      */   
/*      */   public static long copy(URL url, OutputStream outputStream) throws IOException {
/* 1256 */     try (InputStream inputStream = ((URL)Objects.<URL>requireNonNull(url, "url")).openStream()) {
/* 1257 */       return copyLarge(inputStream, outputStream);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long copyLarge(InputStream inputStream, OutputStream outputStream) throws IOException {
/* 1282 */     return copy(inputStream, outputStream, 8192);
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
/*      */   public static long copyLarge(InputStream inputStream, OutputStream outputStream, byte[] buffer) throws IOException {
/* 1305 */     Objects.requireNonNull(inputStream, "inputStream");
/* 1306 */     Objects.requireNonNull(outputStream, "outputStream");
/* 1307 */     long count = 0L;
/*      */     int n;
/* 1309 */     while (-1 != (n = inputStream.read(buffer))) {
/* 1310 */       outputStream.write(buffer, 0, n);
/* 1311 */       count += n;
/*      */     } 
/* 1313 */     return count;
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
/*      */ 
/*      */   
/*      */   public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length) throws IOException {
/* 1342 */     return copyLarge(input, output, inputOffset, length, getByteArray());
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
/*      */ 
/*      */   
/*      */   public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length, byte[] buffer) throws IOException {
/* 1371 */     if (inputOffset > 0L) {
/* 1372 */       skipFully(input, inputOffset);
/*      */     }
/* 1374 */     if (length == 0L) {
/* 1375 */       return 0L;
/*      */     }
/* 1377 */     int bufferLength = buffer.length;
/* 1378 */     int bytesToRead = bufferLength;
/* 1379 */     if (length > 0L && length < bufferLength) {
/* 1380 */       bytesToRead = (int)length;
/*      */     }
/*      */     
/* 1383 */     long totalRead = 0L; int read;
/* 1384 */     while (bytesToRead > 0 && -1 != (read = input.read(buffer, 0, bytesToRead))) {
/* 1385 */       output.write(buffer, 0, read);
/* 1386 */       totalRead += read;
/* 1387 */       if (length > 0L)
/*      */       {
/* 1389 */         bytesToRead = (int)Math.min(length - totalRead, bufferLength);
/*      */       }
/*      */     } 
/* 1392 */     return totalRead;
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
/*      */   public static long copyLarge(Reader reader, Writer writer) throws IOException {
/* 1411 */     return copyLarge(reader, writer, getCharArray());
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
/*      */   public static long copyLarge(Reader reader, Writer writer, char[] buffer) throws IOException {
/* 1430 */     long count = 0L;
/*      */     int n;
/* 1432 */     while (-1 != (n = reader.read(buffer))) {
/* 1433 */       writer.write(buffer, 0, n);
/* 1434 */       count += n;
/*      */     } 
/* 1436 */     return count;
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
/*      */   public static long copyLarge(Reader reader, Writer writer, long inputOffset, long length) throws IOException {
/* 1460 */     return copyLarge(reader, writer, inputOffset, length, getCharArray());
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
/*      */   public static long copyLarge(Reader reader, Writer writer, long inputOffset, long length, char[] buffer) throws IOException {
/* 1485 */     if (inputOffset > 0L) {
/* 1486 */       skipFully(reader, inputOffset);
/*      */     }
/* 1488 */     if (length == 0L) {
/* 1489 */       return 0L;
/*      */     }
/* 1491 */     int bytesToRead = buffer.length;
/* 1492 */     if (length > 0L && length < buffer.length) {
/* 1493 */       bytesToRead = (int)length;
/*      */     }
/*      */     
/* 1496 */     long totalRead = 0L; int read;
/* 1497 */     while (bytesToRead > 0 && -1 != (read = reader.read(buffer, 0, bytesToRead))) {
/* 1498 */       writer.write(buffer, 0, read);
/* 1499 */       totalRead += read;
/* 1500 */       if (length > 0L)
/*      */       {
/* 1502 */         bytesToRead = (int)Math.min(length - totalRead, buffer.length);
/*      */       }
/*      */     } 
/* 1505 */     return totalRead;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static byte[] getByteArray() {
/* 1514 */     return SKIP_BYTE_BUFFER.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static char[] getCharArray() {
/* 1523 */     return SKIP_CHAR_BUFFER.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int length(byte[] array) {
/* 1534 */     return (array == null) ? 0 : array.length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int length(char[] array) {
/* 1545 */     return (array == null) ? 0 : array.length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int length(CharSequence csq) {
/* 1556 */     return (csq == null) ? 0 : csq.length();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int length(Object[] array) {
/* 1567 */     return (array == null) ? 0 : array.length;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static LineIterator lineIterator(InputStream input, Charset charset) {
/* 1600 */     return new LineIterator(new InputStreamReader(input, Charsets.toCharset(charset)));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static LineIterator lineIterator(InputStream input, String charsetName) {
/* 1636 */     return lineIterator(input, Charsets.toCharset(charsetName));
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static LineIterator lineIterator(Reader reader) {
/* 1667 */     return new LineIterator(reader);
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
/*      */   public static int read(InputStream input, byte[] buffer) throws IOException {
/* 1683 */     return read(input, buffer, 0, buffer.length);
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
/*      */   public static int read(InputStream input, byte[] buffer, int offset, int length) throws IOException {
/* 1702 */     if (length < 0) {
/* 1703 */       throw new IllegalArgumentException("Length must not be negative: " + length);
/*      */     }
/* 1705 */     int remaining = length;
/* 1706 */     while (remaining > 0) {
/* 1707 */       int location = length - remaining;
/* 1708 */       int count = input.read(buffer, offset + location, remaining);
/* 1709 */       if (-1 == count) {
/*      */         break;
/*      */       }
/* 1712 */       remaining -= count;
/*      */     } 
/* 1714 */     return length - remaining;
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
/*      */   public static int read(ReadableByteChannel input, ByteBuffer buffer) throws IOException {
/* 1731 */     int length = buffer.remaining();
/* 1732 */     while (buffer.remaining() > 0) {
/* 1733 */       int count = input.read(buffer);
/* 1734 */       if (-1 == count) {
/*      */         break;
/*      */       }
/*      */     } 
/* 1738 */     return length - buffer.remaining();
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
/*      */   public static int read(Reader reader, char[] buffer) throws IOException {
/* 1754 */     return read(reader, buffer, 0, buffer.length);
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
/*      */   public static int read(Reader reader, char[] buffer, int offset, int length) throws IOException {
/* 1773 */     if (length < 0) {
/* 1774 */       throw new IllegalArgumentException("Length must not be negative: " + length);
/*      */     }
/* 1776 */     int remaining = length;
/* 1777 */     while (remaining > 0) {
/* 1778 */       int location = length - remaining;
/* 1779 */       int count = reader.read(buffer, offset + location, remaining);
/* 1780 */       if (-1 == count) {
/*      */         break;
/*      */       }
/* 1783 */       remaining -= count;
/*      */     } 
/* 1785 */     return length - remaining;
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
/*      */   public static void readFully(InputStream input, byte[] buffer) throws IOException {
/* 1803 */     readFully(input, buffer, 0, buffer.length);
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
/*      */   public static void readFully(InputStream input, byte[] buffer, int offset, int length) throws IOException {
/* 1824 */     int actual = read(input, buffer, offset, length);
/* 1825 */     if (actual != length) {
/* 1826 */       throw new EOFException("Length to read: " + length + " actual: " + actual);
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
/*      */   public static byte[] readFully(InputStream input, int length) throws IOException {
/* 1845 */     byte[] buffer = byteArray(length);
/* 1846 */     readFully(input, buffer, 0, buffer.length);
/* 1847 */     return buffer;
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
/*      */   public static void readFully(ReadableByteChannel input, ByteBuffer buffer) throws IOException {
/* 1863 */     int expected = buffer.remaining();
/* 1864 */     int actual = read(input, buffer);
/* 1865 */     if (actual != expected) {
/* 1866 */       throw new EOFException("Length to read: " + expected + " actual: " + actual);
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
/*      */   public static void readFully(Reader reader, char[] buffer) throws IOException {
/* 1884 */     readFully(reader, buffer, 0, buffer.length);
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
/*      */   public static void readFully(Reader reader, char[] buffer, int offset, int length) throws IOException {
/* 1904 */     int actual = read(reader, buffer, offset, length);
/* 1905 */     if (actual != length) {
/* 1906 */       throw new EOFException("Length to read: " + length + " actual: " + actual);
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
/*      */   @Deprecated
/*      */   public static List<String> readLines(InputStream input) throws IOException {
/* 1926 */     return readLines(input, Charset.defaultCharset());
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
/*      */   public static List<String> readLines(InputStream input, Charset charset) throws IOException {
/* 1944 */     InputStreamReader reader = new InputStreamReader(input, Charsets.toCharset(charset));
/* 1945 */     return readLines(reader);
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
/*      */   public static List<String> readLines(InputStream input, String charsetName) throws IOException {
/* 1969 */     return readLines(input, Charsets.toCharset(charsetName));
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
/*      */   public static List<String> readLines(Reader reader) throws IOException {
/* 1987 */     BufferedReader bufReader = toBufferedReader(reader);
/* 1988 */     List<String> list = new ArrayList<>();
/*      */     String line;
/* 1990 */     while ((line = bufReader.readLine()) != null) {
/* 1991 */       list.add(line);
/*      */     }
/* 1993 */     return list;
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
/*      */   public static byte[] resourceToByteArray(String name) throws IOException {
/* 2011 */     return resourceToByteArray(name, null);
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
/*      */   public static byte[] resourceToByteArray(String name, ClassLoader classLoader) throws IOException {
/* 2030 */     return toByteArray(resourceToURL(name, classLoader));
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
/*      */   public static String resourceToString(String name, Charset charset) throws IOException {
/* 2050 */     return resourceToString(name, charset, null);
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
/*      */   public static String resourceToString(String name, Charset charset, ClassLoader classLoader) throws IOException {
/* 2071 */     return toString(resourceToURL(name, classLoader), charset);
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
/*      */   public static URL resourceToURL(String name) throws IOException {
/* 2089 */     return resourceToURL(name, null);
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
/*      */   public static URL resourceToURL(String name, ClassLoader classLoader) throws IOException {
/* 2110 */     URL resource = (classLoader == null) ? IOUtils.class.getResource(name) : classLoader.getResource(name);
/*      */     
/* 2112 */     if (resource == null) {
/* 2113 */       throw new IOException("Resource not found: " + name);
/*      */     }
/*      */     
/* 2116 */     return resource;
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
/*      */   public static long skip(InputStream input, long toSkip) throws IOException {
/* 2141 */     if (toSkip < 0L) {
/* 2142 */       throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2150 */     long remain = toSkip;
/* 2151 */     while (remain > 0L) {
/*      */       
/* 2153 */       byte[] byteArray = getByteArray();
/* 2154 */       long n = input.read(byteArray, 0, (int)Math.min(remain, byteArray.length));
/* 2155 */       if (n < 0L) {
/*      */         break;
/*      */       }
/* 2158 */       remain -= n;
/*      */     } 
/* 2160 */     return toSkip - remain;
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
/*      */   public static long skip(ReadableByteChannel input, long toSkip) throws IOException {
/* 2176 */     if (toSkip < 0L) {
/* 2177 */       throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
/*      */     }
/* 2179 */     ByteBuffer skipByteBuffer = ByteBuffer.allocate((int)Math.min(toSkip, 8192L));
/* 2180 */     long remain = toSkip;
/* 2181 */     while (remain > 0L) {
/* 2182 */       skipByteBuffer.position(0);
/* 2183 */       skipByteBuffer.limit((int)Math.min(remain, 8192L));
/* 2184 */       int n = input.read(skipByteBuffer);
/* 2185 */       if (n == -1) {
/*      */         break;
/*      */       }
/* 2188 */       remain -= n;
/*      */     } 
/* 2190 */     return toSkip - remain;
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
/*      */   public static long skip(Reader reader, long toSkip) throws IOException {
/* 2215 */     if (toSkip < 0L) {
/* 2216 */       throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
/*      */     }
/* 2218 */     long remain = toSkip;
/* 2219 */     while (remain > 0L) {
/*      */       
/* 2221 */       char[] charArray = getCharArray();
/* 2222 */       long n = reader.read(charArray, 0, (int)Math.min(remain, charArray.length));
/* 2223 */       if (n < 0L) {
/*      */         break;
/*      */       }
/* 2226 */       remain -= n;
/*      */     } 
/* 2228 */     return toSkip - remain;
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
/*      */   public static void skipFully(InputStream input, long toSkip) throws IOException {
/* 2251 */     if (toSkip < 0L) {
/* 2252 */       throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
/*      */     }
/* 2254 */     long skipped = skip(input, toSkip);
/* 2255 */     if (skipped != toSkip) {
/* 2256 */       throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
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
/*      */   public static void skipFully(ReadableByteChannel input, long toSkip) throws IOException {
/* 2271 */     if (toSkip < 0L) {
/* 2272 */       throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
/*      */     }
/* 2274 */     long skipped = skip(input, toSkip);
/* 2275 */     if (skipped != toSkip) {
/* 2276 */       throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void skipFully(Reader reader, long toSkip) throws IOException {
/* 2300 */     long skipped = skip(reader, toSkip);
/* 2301 */     if (skipped != toSkip) {
/* 2302 */       throw new EOFException("Chars to skip: " + toSkip + " actual: " + skipped);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static InputStream toBufferedInputStream(InputStream input) throws IOException {
/* 2328 */     return ByteArrayOutputStream.toBufferedInputStream(input);
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
/*      */   public static InputStream toBufferedInputStream(InputStream input, int size) throws IOException {
/* 2354 */     return ByteArrayOutputStream.toBufferedInputStream(input, size);
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
/*      */   public static BufferedReader toBufferedReader(Reader reader) {
/* 2368 */     return (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader);
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
/*      */   public static BufferedReader toBufferedReader(Reader reader, int size) {
/* 2383 */     return (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader, size);
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
/*      */   public static byte[] toByteArray(InputStream inputStream) throws IOException {
/* 2400 */     try(UnsynchronizedByteArrayOutputStream ubaOutput = new UnsynchronizedByteArrayOutputStream(); 
/* 2401 */         ThresholdingOutputStream thresholdOuput = new ThresholdingOutputStream(2147483647, os -> {
/*      */             
/*      */             throw new IllegalArgumentException(String.format("Cannot read more than %,d into a byte array", new Object[] { Integer.valueOf(2147483647) }));
/*      */           }os -> ubaOutput)) {
/* 2405 */       copy(inputStream, (OutputStream)thresholdOuput);
/* 2406 */       return ubaOutput.toByteArray();
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
/*      */   public static byte[] toByteArray(InputStream input, int size) throws IOException {
/* 2423 */     if (size < 0) {
/* 2424 */       throw new IllegalArgumentException("Size must be equal or greater than zero: " + size);
/*      */     }
/*      */     
/* 2427 */     if (size == 0) {
/* 2428 */       return EMPTY_BYTE_ARRAY;
/*      */     }
/*      */     
/* 2431 */     byte[] data = byteArray(size);
/* 2432 */     int offset = 0;
/*      */     
/*      */     int read;
/* 2435 */     while (offset < size && (read = input.read(data, offset, size - offset)) != -1) {
/* 2436 */       offset += read;
/*      */     }
/*      */     
/* 2439 */     if (offset != size) {
/* 2440 */       throw new IOException("Unexpected read size, current: " + offset + ", expected: " + size);
/*      */     }
/*      */     
/* 2443 */     return data;
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
/*      */   public static byte[] toByteArray(InputStream input, long size) throws IOException {
/* 2465 */     if (size > 2147483647L) {
/* 2466 */       throw new IllegalArgumentException("Size cannot be greater than Integer max value: " + size);
/*      */     }
/*      */     
/* 2469 */     return toByteArray(input, (int)size);
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
/*      */   @Deprecated
/*      */   public static byte[] toByteArray(Reader reader) throws IOException {
/* 2487 */     return toByteArray(reader, Charset.defaultCharset());
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
/*      */   public static byte[] toByteArray(Reader reader, Charset charset) throws IOException {
/* 2505 */     try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
/* 2506 */       copy(reader, (OutputStream)output, charset);
/* 2507 */       return output.toByteArray();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(Reader reader, String charsetName) throws IOException {
/* 2532 */     return toByteArray(reader, Charsets.toCharset(charsetName));
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
/*      */   @Deprecated
/*      */   public static byte[] toByteArray(String input) {
/* 2549 */     return input.getBytes(Charset.defaultCharset());
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
/*      */   public static byte[] toByteArray(URI uri) throws IOException {
/* 2562 */     return toByteArray(uri.toURL());
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
/*      */   public static byte[] toByteArray(URL url) throws IOException {
/* 2575 */     URLConnection conn = url.openConnection();
/*      */     try {
/* 2577 */       return toByteArray(conn);
/*      */     } finally {
/* 2579 */       close(conn);
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
/*      */   public static byte[] toByteArray(URLConnection urlConn) throws IOException {
/* 2593 */     try (InputStream inputStream = urlConn.getInputStream()) {
/* 2594 */       return toByteArray(inputStream);
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
/*      */   @Deprecated
/*      */   public static char[] toCharArray(InputStream inputStream) throws IOException {
/* 2614 */     return toCharArray(inputStream, Charset.defaultCharset());
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
/*      */   public static char[] toCharArray(InputStream inputStream, Charset charset) throws IOException {
/* 2633 */     CharArrayWriter writer = new CharArrayWriter();
/* 2634 */     copy(inputStream, writer, charset);
/* 2635 */     return writer.toCharArray();
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
/*      */   public static char[] toCharArray(InputStream inputStream, String charsetName) throws IOException {
/* 2659 */     return toCharArray(inputStream, Charsets.toCharset(charsetName));
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
/*      */   public static char[] toCharArray(Reader reader) throws IOException {
/* 2675 */     CharArrayWriter sw = new CharArrayWriter();
/* 2676 */     copy(reader, sw);
/* 2677 */     return sw.toCharArray();
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
/*      */   @Deprecated
/*      */   public static InputStream toInputStream(CharSequence input) {
/* 2691 */     return toInputStream(input, Charset.defaultCharset());
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
/*      */   public static InputStream toInputStream(CharSequence input, Charset charset) {
/* 2704 */     return toInputStream(input.toString(), charset);
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
/*      */   public static InputStream toInputStream(CharSequence input, String charsetName) {
/* 2723 */     return toInputStream(input, Charsets.toCharset(charsetName));
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
/*      */   @Deprecated
/*      */   public static InputStream toInputStream(String input) {
/* 2737 */     return toInputStream(input, Charset.defaultCharset());
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
/*      */   public static InputStream toInputStream(String input, Charset charset) {
/* 2750 */     return new ByteArrayInputStream(input.getBytes(Charsets.toCharset(charset)));
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
/*      */   public static InputStream toInputStream(String input, String charsetName) {
/* 2769 */     byte[] bytes = input.getBytes(Charsets.toCharset(charsetName));
/* 2770 */     return new ByteArrayInputStream(bytes);
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
/*      */   @Deprecated
/*      */   public static String toString(byte[] input) {
/* 2785 */     return new String(input, Charset.defaultCharset());
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
/*      */   public static String toString(byte[] input, String charsetName) {
/* 2801 */     return new String(input, Charsets.toCharset(charsetName));
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
/*      */   @Deprecated
/*      */   public static String toString(InputStream input) throws IOException {
/* 2819 */     return toString(input, Charset.defaultCharset());
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
/*      */   public static String toString(InputStream input, Charset charset) throws IOException {
/* 2838 */     try (StringBuilderWriter sw = new StringBuilderWriter()) {
/* 2839 */       copy(input, (Writer)sw, charset);
/* 2840 */       return sw.toString();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(InputStream input, String charsetName) throws IOException {
/* 2865 */     return toString(input, Charsets.toCharset(charsetName));
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
/*      */   public static String toString(Reader reader) throws IOException {
/* 2880 */     try (StringBuilderWriter sw = new StringBuilderWriter()) {
/* 2881 */       copy(reader, (Writer)sw);
/* 2882 */       return sw.toString();
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
/*      */   @Deprecated
/*      */   public static String toString(URI uri) throws IOException {
/* 2897 */     return toString(uri, Charset.defaultCharset());
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
/*      */   public static String toString(URI uri, Charset encoding) throws IOException {
/* 2910 */     return toString(uri.toURL(), Charsets.toCharset(encoding));
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
/*      */   public static String toString(URI uri, String charsetName) throws IOException {
/* 2926 */     return toString(uri, Charsets.toCharset(charsetName));
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
/*      */   @Deprecated
/*      */   public static String toString(URL url) throws IOException {
/* 2940 */     return toString(url, Charset.defaultCharset());
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
/*      */   public static String toString(URL url, Charset encoding) throws IOException {
/* 2953 */     try (InputStream inputStream = url.openStream()) {
/* 2954 */       return toString(inputStream, encoding);
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
/*      */   public static String toString(URL url, String charsetName) throws IOException {
/* 2971 */     return toString(url, Charsets.toCharset(charsetName));
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
/*      */   public static void write(byte[] data, OutputStream output) throws IOException {
/* 2986 */     if (data != null) {
/* 2987 */       output.write(data);
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
/*      */   @Deprecated
/*      */   public static void write(byte[] data, Writer writer) throws IOException {
/* 3007 */     write(data, writer, Charset.defaultCharset());
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
/*      */   public static void write(byte[] data, Writer writer, Charset charset) throws IOException {
/* 3025 */     if (data != null) {
/* 3026 */       writer.write(new String(data, Charsets.toCharset(charset)));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(byte[] data, Writer writer, String charsetName) throws IOException {
/* 3051 */     write(data, writer, Charsets.toCharset(charsetName));
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
/*      */   @Deprecated
/*      */   public static void write(char[] data, OutputStream output) throws IOException {
/* 3072 */     write(data, output, Charset.defaultCharset());
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
/*      */   public static void write(char[] data, OutputStream output, Charset charset) throws IOException {
/* 3091 */     if (data != null) {
/* 3092 */       output.write((new String(data)).getBytes(Charsets.toCharset(charset)));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(char[] data, OutputStream output, String charsetName) throws IOException {
/* 3118 */     write(data, output, Charsets.toCharset(charsetName));
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
/*      */   public static void write(char[] data, Writer writer) throws IOException {
/* 3132 */     if (data != null) {
/* 3133 */       writer.write(data);
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
/*      */   @Deprecated
/*      */   public static void write(CharSequence data, OutputStream output) throws IOException {
/* 3154 */     write(data, output, Charset.defaultCharset());
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
/*      */   public static void write(CharSequence data, OutputStream output, Charset charset) throws IOException {
/* 3172 */     if (data != null) {
/* 3173 */       write(data.toString(), output, charset);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(CharSequence data, OutputStream output, String charsetName) throws IOException {
/* 3197 */     write(data, output, Charsets.toCharset(charsetName));
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
/*      */   public static void write(CharSequence data, Writer writer) throws IOException {
/* 3210 */     if (data != null) {
/* 3211 */       write(data.toString(), writer);
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
/*      */   
/*      */   @Deprecated
/*      */   public static void write(String data, OutputStream output) throws IOException {
/* 3233 */     write(data, output, Charset.defaultCharset());
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
/*      */   public static void write(String data, OutputStream output, Charset charset) throws IOException {
/* 3250 */     if (data != null) {
/* 3251 */       output.write(data.getBytes(Charsets.toCharset(charset)));
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(String data, OutputStream output, String charsetName) throws IOException {
/* 3275 */     write(data, output, Charsets.toCharset(charsetName));
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
/*      */   public static void write(String data, Writer writer) throws IOException {
/* 3288 */     if (data != null) {
/* 3289 */       writer.write(data);
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
/*      */   @Deprecated
/*      */   public static void write(StringBuffer data, OutputStream output) throws IOException {
/* 3310 */     write(data, output, (String)null);
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
/*      */   @Deprecated
/*      */   public static void write(StringBuffer data, OutputStream output, String charsetName) throws IOException {
/* 3335 */     if (data != null) {
/* 3336 */       output.write(data.toString().getBytes(Charsets.toCharset(charsetName)));
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
/*      */   @Deprecated
/*      */   public static void write(StringBuffer data, Writer writer) throws IOException {
/* 3353 */     if (data != null) {
/* 3354 */       writer.write(data.toString());
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
/*      */   public static void writeChunked(byte[] data, OutputStream output) throws IOException {
/* 3372 */     if (data != null) {
/* 3373 */       int bytes = data.length;
/* 3374 */       int offset = 0;
/* 3375 */       while (bytes > 0) {
/* 3376 */         int chunk = Math.min(bytes, 8192);
/* 3377 */         output.write(data, offset, chunk);
/* 3378 */         bytes -= chunk;
/* 3379 */         offset += chunk;
/*      */       } 
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
/*      */   public static void writeChunked(char[] data, Writer writer) throws IOException {
/* 3397 */     if (data != null) {
/* 3398 */       int bytes = data.length;
/* 3399 */       int offset = 0;
/* 3400 */       while (bytes > 0) {
/* 3401 */         int chunk = Math.min(bytes, 8192);
/* 3402 */         writer.write(data, offset, chunk);
/* 3403 */         bytes -= chunk;
/* 3404 */         offset += chunk;
/*      */       } 
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
/*      */   @Deprecated
/*      */   public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output) throws IOException {
/* 3425 */     writeLines(lines, lineEnding, output, Charset.defaultCharset());
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
/*      */   public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output, Charset charset) throws IOException {
/* 3443 */     if (lines == null) {
/*      */       return;
/*      */     }
/* 3446 */     if (lineEnding == null) {
/* 3447 */       lineEnding = System.lineSeparator();
/*      */     }
/* 3449 */     Charset cs = Charsets.toCharset(charset);
/* 3450 */     for (Object line : lines) {
/* 3451 */       if (line != null) {
/* 3452 */         output.write(line.toString().getBytes(cs));
/*      */       }
/* 3454 */       output.write(lineEnding.getBytes(cs));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output, String charsetName) throws IOException {
/* 3479 */     writeLines(lines, lineEnding, output, Charsets.toCharset(charsetName));
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
/*      */   public static void writeLines(Collection<?> lines, String lineEnding, Writer writer) throws IOException {
/* 3495 */     if (lines == null) {
/*      */       return;
/*      */     }
/* 3498 */     if (lineEnding == null) {
/* 3499 */       lineEnding = System.lineSeparator();
/*      */     }
/* 3501 */     for (Object line : lines) {
/* 3502 */       if (line != null) {
/* 3503 */         writer.write(line.toString());
/*      */       }
/* 3505 */       writer.write(lineEnding);
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
/*      */   public static Writer writer(Appendable appendable) {
/* 3519 */     Objects.requireNonNull(appendable, "appendable");
/* 3520 */     if (appendable instanceof Writer) {
/* 3521 */       return (Writer)appendable;
/*      */     }
/* 3523 */     if (appendable instanceof StringBuilder) {
/* 3524 */       return (Writer)new StringBuilderWriter((StringBuilder)appendable);
/*      */     }
/* 3526 */     return (Writer)new AppendableWriter(appendable);
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\io\IOUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */