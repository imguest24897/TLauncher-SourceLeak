/*      */ package org.apache.commons.lang3.text;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.Reader;
/*      */ import java.io.Serializable;
/*      */ import java.io.Writer;
/*      */ import java.nio.CharBuffer;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import org.apache.commons.lang3.ArrayUtils;
/*      */ import org.apache.commons.lang3.StringUtils;
/*      */ import org.apache.commons.lang3.builder.Builder;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Deprecated
/*      */ public class StrBuilder
/*      */   implements CharSequence, Appendable, Serializable, Builder<String>
/*      */ {
/*      */   static final int CAPACITY = 32;
/*      */   private static final long serialVersionUID = 7628716375283629643L;
/*      */   protected char[] buffer;
/*      */   protected int size;
/*      */   private String newLine;
/*      */   private String nullText;
/*      */   
/*      */   public StrBuilder() {
/*  110 */     this(32);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder(int initialCapacity) {
/*  119 */     if (initialCapacity <= 0) {
/*  120 */       initialCapacity = 32;
/*      */     }
/*  122 */     this.buffer = new char[initialCapacity];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder(String str) {
/*  132 */     if (str == null) {
/*  133 */       this.buffer = new char[32];
/*      */     } else {
/*  135 */       this.buffer = new char[str.length() + 32];
/*  136 */       append(str);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getNewLineText() {
/*  146 */     return this.newLine;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder setNewLineText(String newLine) {
/*  156 */     this.newLine = newLine;
/*  157 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getNullText() {
/*  166 */     return this.nullText;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder setNullText(String nullText) {
/*  176 */     if (StringUtils.isEmpty(nullText)) {
/*  177 */       nullText = null;
/*      */     }
/*  179 */     this.nullText = nullText;
/*  180 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int length() {
/*  190 */     return this.size;
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
/*      */   public StrBuilder setLength(int length) {
/*  202 */     if (length < 0) {
/*  203 */       throw new StringIndexOutOfBoundsException(length);
/*      */     }
/*  205 */     if (length < this.size) {
/*  206 */       this.size = length;
/*  207 */     } else if (length > this.size) {
/*  208 */       ensureCapacity(length);
/*  209 */       int oldEnd = this.size;
/*  210 */       this.size = length;
/*  211 */       for (int i = oldEnd; i < length; i++) {
/*  212 */         this.buffer[i] = Character.MIN_VALUE;
/*      */       }
/*      */     } 
/*  215 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int capacity() {
/*  224 */     return this.buffer.length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder ensureCapacity(int capacity) {
/*  234 */     if (capacity > this.buffer.length) {
/*  235 */       char[] old = this.buffer;
/*  236 */       this.buffer = new char[capacity * 2];
/*  237 */       System.arraycopy(old, 0, this.buffer, 0, this.size);
/*      */     } 
/*  239 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder minimizeCapacity() {
/*  248 */     if (this.buffer.length > length()) {
/*  249 */       char[] old = this.buffer;
/*  250 */       this.buffer = new char[length()];
/*  251 */       System.arraycopy(old, 0, this.buffer, 0, this.size);
/*      */     } 
/*  253 */     return this;
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
/*      */   public int size() {
/*  266 */     return this.size;
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
/*      */   public boolean isEmpty() {
/*  279 */     return (this.size == 0);
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
/*      */   public boolean isNotEmpty() {
/*  293 */     return (this.size > 0);
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
/*      */   public StrBuilder clear() {
/*  310 */     this.size = 0;
/*  311 */     return this;
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
/*      */   public char charAt(int index) {
/*  325 */     if (index < 0 || index >= length()) {
/*  326 */       throw new StringIndexOutOfBoundsException(index);
/*      */     }
/*  328 */     return this.buffer[index];
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
/*      */   public StrBuilder setCharAt(int index, char ch) {
/*  342 */     if (index < 0 || index >= length()) {
/*  343 */       throw new StringIndexOutOfBoundsException(index);
/*      */     }
/*  345 */     this.buffer[index] = ch;
/*  346 */     return this;
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
/*      */   public StrBuilder deleteCharAt(int index) {
/*  359 */     if (index < 0 || index >= this.size) {
/*  360 */       throw new StringIndexOutOfBoundsException(index);
/*      */     }
/*  362 */     deleteImpl(index, index + 1, 1);
/*  363 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public char[] toCharArray() {
/*  372 */     if (this.size == 0) {
/*  373 */       return ArrayUtils.EMPTY_CHAR_ARRAY;
/*      */     }
/*  375 */     char[] chars = new char[this.size];
/*  376 */     System.arraycopy(this.buffer, 0, chars, 0, this.size);
/*  377 */     return chars;
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
/*      */   public char[] toCharArray(int startIndex, int endIndex) {
/*  391 */     endIndex = validateRange(startIndex, endIndex);
/*  392 */     int len = endIndex - startIndex;
/*  393 */     if (len == 0) {
/*  394 */       return ArrayUtils.EMPTY_CHAR_ARRAY;
/*      */     }
/*  396 */     char[] chars = new char[len];
/*  397 */     System.arraycopy(this.buffer, startIndex, chars, 0, len);
/*  398 */     return chars;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public char[] getChars(char[] destination) {
/*  408 */     int len = length();
/*  409 */     if (destination == null || destination.length < len) {
/*  410 */       destination = new char[len];
/*      */     }
/*  412 */     System.arraycopy(this.buffer, 0, destination, 0, len);
/*  413 */     return destination;
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
/*      */   public void getChars(int startIndex, int endIndex, char[] destination, int destinationIndex) {
/*  427 */     if (startIndex < 0) {
/*  428 */       throw new StringIndexOutOfBoundsException(startIndex);
/*      */     }
/*  430 */     if (endIndex < 0 || endIndex > length()) {
/*  431 */       throw new StringIndexOutOfBoundsException(endIndex);
/*      */     }
/*  433 */     if (startIndex > endIndex) {
/*  434 */       throw new StringIndexOutOfBoundsException("end < start");
/*      */     }
/*  436 */     System.arraycopy(this.buffer, startIndex, destination, destinationIndex, endIndex - startIndex);
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
/*      */   public int readFrom(Readable readable) throws IOException {
/*  451 */     int oldSize = this.size;
/*  452 */     if (readable instanceof Reader) {
/*  453 */       Reader r = (Reader)readable;
/*  454 */       ensureCapacity(this.size + 1);
/*      */       int read;
/*  456 */       while ((read = r.read(this.buffer, this.size, this.buffer.length - this.size)) != -1) {
/*  457 */         this.size += read;
/*  458 */         ensureCapacity(this.size + 1);
/*      */       } 
/*  460 */     } else if (readable instanceof CharBuffer) {
/*  461 */       CharBuffer cb = (CharBuffer)readable;
/*  462 */       int remaining = cb.remaining();
/*  463 */       ensureCapacity(this.size + remaining);
/*  464 */       cb.get(this.buffer, this.size, remaining);
/*  465 */       this.size += remaining;
/*      */     } else {
/*      */       while (true) {
/*  468 */         ensureCapacity(this.size + 1);
/*  469 */         CharBuffer buf = CharBuffer.wrap(this.buffer, this.size, this.buffer.length - this.size);
/*  470 */         int read = readable.read(buf);
/*  471 */         if (read == -1) {
/*      */           break;
/*      */         }
/*  474 */         this.size += read;
/*      */       } 
/*      */     } 
/*  477 */     return this.size - oldSize;
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
/*      */   public StrBuilder appendNewLine() {
/*  491 */     if (this.newLine == null) {
/*  492 */       append(System.lineSeparator());
/*  493 */       return this;
/*      */     } 
/*  495 */     return append(this.newLine);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder appendNull() {
/*  504 */     if (this.nullText == null) {
/*  505 */       return this;
/*      */     }
/*  507 */     return append(this.nullText);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder append(Object obj) {
/*  518 */     if (obj == null) {
/*  519 */       return appendNull();
/*      */     }
/*  521 */     if (obj instanceof CharSequence) {
/*  522 */       return append((CharSequence)obj);
/*      */     }
/*  524 */     return append(obj.toString());
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
/*      */   public StrBuilder append(CharSequence seq) {
/*  537 */     if (seq == null) {
/*  538 */       return appendNull();
/*      */     }
/*  540 */     if (seq instanceof StrBuilder) {
/*  541 */       return append((StrBuilder)seq);
/*      */     }
/*  543 */     if (seq instanceof StringBuilder) {
/*  544 */       return append((StringBuilder)seq);
/*      */     }
/*  546 */     if (seq instanceof StringBuffer) {
/*  547 */       return append((StringBuffer)seq);
/*      */     }
/*  549 */     if (seq instanceof CharBuffer) {
/*  550 */       return append((CharBuffer)seq);
/*      */     }
/*  552 */     return append(seq.toString());
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
/*      */   public StrBuilder append(CharSequence seq, int startIndex, int length) {
/*  567 */     if (seq == null) {
/*  568 */       return appendNull();
/*      */     }
/*  570 */     return append(seq.toString(), startIndex, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder append(String str) {
/*  581 */     if (str == null) {
/*  582 */       return appendNull();
/*      */     }
/*  584 */     int strLen = str.length();
/*  585 */     if (strLen > 0) {
/*  586 */       int len = length();
/*  587 */       ensureCapacity(len + strLen);
/*  588 */       str.getChars(0, strLen, this.buffer, len);
/*  589 */       this.size += strLen;
/*      */     } 
/*  591 */     return this;
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
/*      */   public StrBuilder append(String str, int startIndex, int length) {
/*  605 */     if (str == null) {
/*  606 */       return appendNull();
/*      */     }
/*  608 */     if (startIndex < 0 || startIndex > str.length()) {
/*  609 */       throw new StringIndexOutOfBoundsException("startIndex must be valid");
/*      */     }
/*  611 */     if (length < 0 || startIndex + length > str.length()) {
/*  612 */       throw new StringIndexOutOfBoundsException("length must be valid");
/*      */     }
/*  614 */     if (length > 0) {
/*  615 */       int len = length();
/*  616 */       ensureCapacity(len + length);
/*  617 */       str.getChars(startIndex, startIndex + length, this.buffer, len);
/*  618 */       this.size += length;
/*      */     } 
/*  620 */     return this;
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
/*      */   public StrBuilder append(String format, Object... objs) {
/*  633 */     return append(String.format(format, objs));
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
/*      */   public StrBuilder append(CharBuffer buf) {
/*  645 */     if (buf == null) {
/*  646 */       return appendNull();
/*      */     }
/*  648 */     if (buf.hasArray()) {
/*  649 */       int length = buf.remaining();
/*  650 */       int len = length();
/*  651 */       ensureCapacity(len + length);
/*  652 */       System.arraycopy(buf.array(), buf.arrayOffset() + buf.position(), this.buffer, len, length);
/*  653 */       this.size += length;
/*      */     } else {
/*  655 */       append(buf.toString());
/*      */     } 
/*  657 */     return this;
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
/*      */   public StrBuilder append(CharBuffer buf, int startIndex, int length) {
/*  671 */     if (buf == null) {
/*  672 */       return appendNull();
/*      */     }
/*  674 */     if (buf.hasArray()) {
/*  675 */       int totalLength = buf.remaining();
/*  676 */       if (startIndex < 0 || startIndex > totalLength) {
/*  677 */         throw new StringIndexOutOfBoundsException("startIndex must be valid");
/*      */       }
/*  679 */       if (length < 0 || startIndex + length > totalLength) {
/*  680 */         throw new StringIndexOutOfBoundsException("length must be valid");
/*      */       }
/*  682 */       int len = length();
/*  683 */       ensureCapacity(len + length);
/*  684 */       System.arraycopy(buf.array(), buf.arrayOffset() + buf.position() + startIndex, this.buffer, len, length);
/*  685 */       this.size += length;
/*      */     } else {
/*  687 */       append(buf.toString(), startIndex, length);
/*      */     } 
/*  689 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder append(StringBuffer str) {
/*  700 */     if (str == null) {
/*  701 */       return appendNull();
/*      */     }
/*  703 */     int strLen = str.length();
/*  704 */     if (strLen > 0) {
/*  705 */       int len = length();
/*  706 */       ensureCapacity(len + strLen);
/*  707 */       str.getChars(0, strLen, this.buffer, len);
/*  708 */       this.size += strLen;
/*      */     } 
/*  710 */     return this;
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
/*      */   public StrBuilder append(StringBuffer str, int startIndex, int length) {
/*  723 */     if (str == null) {
/*  724 */       return appendNull();
/*      */     }
/*  726 */     if (startIndex < 0 || startIndex > str.length()) {
/*  727 */       throw new StringIndexOutOfBoundsException("startIndex must be valid");
/*      */     }
/*  729 */     if (length < 0 || startIndex + length > str.length()) {
/*  730 */       throw new StringIndexOutOfBoundsException("length must be valid");
/*      */     }
/*  732 */     if (length > 0) {
/*  733 */       int len = length();
/*  734 */       ensureCapacity(len + length);
/*  735 */       str.getChars(startIndex, startIndex + length, this.buffer, len);
/*  736 */       this.size += length;
/*      */     } 
/*  738 */     return this;
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
/*      */   public StrBuilder append(StringBuilder str) {
/*  750 */     if (str == null) {
/*  751 */       return appendNull();
/*      */     }
/*  753 */     int strLen = str.length();
/*  754 */     if (strLen > 0) {
/*  755 */       int len = length();
/*  756 */       ensureCapacity(len + strLen);
/*  757 */       str.getChars(0, strLen, this.buffer, len);
/*  758 */       this.size += strLen;
/*      */     } 
/*  760 */     return this;
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
/*      */   public StrBuilder append(StringBuilder str, int startIndex, int length) {
/*  774 */     if (str == null) {
/*  775 */       return appendNull();
/*      */     }
/*  777 */     if (startIndex < 0 || startIndex > str.length()) {
/*  778 */       throw new StringIndexOutOfBoundsException("startIndex must be valid");
/*      */     }
/*  780 */     if (length < 0 || startIndex + length > str.length()) {
/*  781 */       throw new StringIndexOutOfBoundsException("length must be valid");
/*      */     }
/*  783 */     if (length > 0) {
/*  784 */       int len = length();
/*  785 */       ensureCapacity(len + length);
/*  786 */       str.getChars(startIndex, startIndex + length, this.buffer, len);
/*  787 */       this.size += length;
/*      */     } 
/*  789 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder append(StrBuilder str) {
/*  800 */     if (str == null) {
/*  801 */       return appendNull();
/*      */     }
/*  803 */     int strLen = str.length();
/*  804 */     if (strLen > 0) {
/*  805 */       int len = length();
/*  806 */       ensureCapacity(len + strLen);
/*  807 */       System.arraycopy(str.buffer, 0, this.buffer, len, strLen);
/*  808 */       this.size += strLen;
/*      */     } 
/*  810 */     return this;
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
/*      */   public StrBuilder append(StrBuilder str, int startIndex, int length) {
/*  823 */     if (str == null) {
/*  824 */       return appendNull();
/*      */     }
/*  826 */     if (startIndex < 0 || startIndex > str.length()) {
/*  827 */       throw new StringIndexOutOfBoundsException("startIndex must be valid");
/*      */     }
/*  829 */     if (length < 0 || startIndex + length > str.length()) {
/*  830 */       throw new StringIndexOutOfBoundsException("length must be valid");
/*      */     }
/*  832 */     if (length > 0) {
/*  833 */       int len = length();
/*  834 */       ensureCapacity(len + length);
/*  835 */       str.getChars(startIndex, startIndex + length, this.buffer, len);
/*  836 */       this.size += length;
/*      */     } 
/*  838 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder append(char[] chars) {
/*  849 */     if (chars == null) {
/*  850 */       return appendNull();
/*      */     }
/*  852 */     int strLen = chars.length;
/*  853 */     if (strLen > 0) {
/*  854 */       int len = length();
/*  855 */       ensureCapacity(len + strLen);
/*  856 */       System.arraycopy(chars, 0, this.buffer, len, strLen);
/*  857 */       this.size += strLen;
/*      */     } 
/*  859 */     return this;
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
/*      */   public StrBuilder append(char[] chars, int startIndex, int length) {
/*  872 */     if (chars == null) {
/*  873 */       return appendNull();
/*      */     }
/*  875 */     if (startIndex < 0 || startIndex > chars.length) {
/*  876 */       throw new StringIndexOutOfBoundsException("Invalid startIndex: " + length);
/*      */     }
/*  878 */     if (length < 0 || startIndex + length > chars.length) {
/*  879 */       throw new StringIndexOutOfBoundsException("Invalid length: " + length);
/*      */     }
/*  881 */     if (length > 0) {
/*  882 */       int len = length();
/*  883 */       ensureCapacity(len + length);
/*  884 */       System.arraycopy(chars, startIndex, this.buffer, len, length);
/*  885 */       this.size += length;
/*      */     } 
/*  887 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder append(boolean value) {
/*  897 */     if (value) {
/*  898 */       ensureCapacity(this.size + 4);
/*  899 */       this.buffer[this.size++] = 't';
/*  900 */       this.buffer[this.size++] = 'r';
/*  901 */       this.buffer[this.size++] = 'u';
/*      */     } else {
/*  903 */       ensureCapacity(this.size + 5);
/*  904 */       this.buffer[this.size++] = 'f';
/*  905 */       this.buffer[this.size++] = 'a';
/*  906 */       this.buffer[this.size++] = 'l';
/*  907 */       this.buffer[this.size++] = 's';
/*      */     } 
/*  909 */     this.buffer[this.size++] = 'e';
/*  910 */     return this;
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
/*      */   public StrBuilder append(char ch) {
/*  922 */     int len = length();
/*  923 */     ensureCapacity(len + 1);
/*  924 */     this.buffer[this.size++] = ch;
/*  925 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder append(int value) {
/*  935 */     return append(String.valueOf(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder append(long value) {
/*  945 */     return append(String.valueOf(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder append(float value) {
/*  955 */     return append(String.valueOf(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder append(double value) {
/*  965 */     return append(String.valueOf(value));
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
/*      */   public StrBuilder appendln(Object obj) {
/*  977 */     return append(obj).appendNewLine();
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
/*      */   public StrBuilder appendln(String str) {
/*  989 */     return append(str).appendNewLine();
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
/*      */   public StrBuilder appendln(String str, int startIndex, int length) {
/* 1003 */     return append(str, startIndex, length).appendNewLine();
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
/*      */   public StrBuilder appendln(String format, Object... objs) {
/* 1016 */     return append(format, objs).appendNewLine();
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
/*      */   public StrBuilder appendln(StringBuffer str) {
/* 1028 */     return append(str).appendNewLine();
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
/*      */   public StrBuilder appendln(StringBuilder str) {
/* 1040 */     return append(str).appendNewLine();
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
/*      */   public StrBuilder appendln(StringBuilder str, int startIndex, int length) {
/* 1054 */     return append(str, startIndex, length).appendNewLine();
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
/*      */   public StrBuilder appendln(StringBuffer str, int startIndex, int length) {
/* 1068 */     return append(str, startIndex, length).appendNewLine();
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
/*      */   public StrBuilder appendln(StrBuilder str) {
/* 1080 */     return append(str).appendNewLine();
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
/*      */   public StrBuilder appendln(StrBuilder str, int startIndex, int length) {
/* 1094 */     return append(str, startIndex, length).appendNewLine();
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
/*      */   public StrBuilder appendln(char[] chars) {
/* 1106 */     return append(chars).appendNewLine();
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
/*      */   public StrBuilder appendln(char[] chars, int startIndex, int length) {
/* 1120 */     return append(chars, startIndex, length).appendNewLine();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder appendln(boolean value) {
/* 1131 */     return append(value).appendNewLine();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder appendln(char ch) {
/* 1142 */     return append(ch).appendNewLine();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder appendln(int value) {
/* 1153 */     return append(value).appendNewLine();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder appendln(long value) {
/* 1164 */     return append(value).appendNewLine();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder appendln(float value) {
/* 1175 */     return append(value).appendNewLine();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder appendln(double value) {
/* 1186 */     return append(value).appendNewLine();
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
/*      */   public <T> StrBuilder appendAll(T... array) {
/* 1206 */     if (ArrayUtils.isNotEmpty((Object[])array)) {
/* 1207 */       for (T element : array) {
/* 1208 */         append(element);
/*      */       }
/*      */     }
/* 1211 */     return this;
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
/*      */   public StrBuilder appendAll(Iterable<?> iterable) {
/* 1224 */     if (iterable != null) {
/* 1225 */       iterable.forEach(this::append);
/*      */     }
/* 1227 */     return this;
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
/*      */   public StrBuilder appendAll(Iterator<?> it) {
/* 1240 */     if (it != null) {
/* 1241 */       it.forEachRemaining(this::append);
/*      */     }
/* 1243 */     return this;
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
/*      */   public StrBuilder appendWithSeparators(Object[] array, String separator) {
/* 1257 */     if (array != null && array.length > 0) {
/* 1258 */       String sep = Objects.toString(separator, "");
/* 1259 */       append(array[0]);
/* 1260 */       for (int i = 1; i < array.length; i++) {
/* 1261 */         append(sep);
/* 1262 */         append(array[i]);
/*      */       } 
/*      */     } 
/* 1265 */     return this;
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
/*      */   public StrBuilder appendWithSeparators(Iterable<?> iterable, String separator) {
/* 1279 */     if (iterable != null) {
/* 1280 */       String sep = Objects.toString(separator, "");
/* 1281 */       Iterator<?> it = iterable.iterator();
/* 1282 */       while (it.hasNext()) {
/* 1283 */         append(it.next());
/* 1284 */         if (it.hasNext()) {
/* 1285 */           append(sep);
/*      */         }
/*      */       } 
/*      */     } 
/* 1289 */     return this;
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
/*      */   public StrBuilder appendWithSeparators(Iterator<?> it, String separator) {
/* 1303 */     if (it != null) {
/* 1304 */       String sep = Objects.toString(separator, "");
/* 1305 */       while (it.hasNext()) {
/* 1306 */         append(it.next());
/* 1307 */         if (it.hasNext()) {
/* 1308 */           append(sep);
/*      */         }
/*      */       } 
/*      */     } 
/* 1312 */     return this;
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
/*      */   public StrBuilder appendSeparator(String separator) {
/* 1339 */     return appendSeparator(separator, (String)null);
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
/*      */   public StrBuilder appendSeparator(String standard, String defaultIfEmpty) {
/* 1371 */     String str = isEmpty() ? defaultIfEmpty : standard;
/* 1372 */     if (str != null) {
/* 1373 */       append(str);
/*      */     }
/* 1375 */     return this;
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
/*      */   public StrBuilder appendSeparator(char separator) {
/* 1399 */     if (isNotEmpty()) {
/* 1400 */       append(separator);
/*      */     }
/* 1402 */     return this;
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
/*      */   public StrBuilder appendSeparator(char standard, char defaultIfEmpty) {
/* 1417 */     if (isNotEmpty()) {
/* 1418 */       append(standard);
/*      */     } else {
/* 1420 */       append(defaultIfEmpty);
/*      */     } 
/* 1422 */     return this;
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
/*      */   public StrBuilder appendSeparator(String separator, int loopIndex) {
/* 1447 */     if (separator != null && loopIndex > 0) {
/* 1448 */       append(separator);
/*      */     }
/* 1450 */     return this;
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
/*      */   public StrBuilder appendSeparator(char separator, int loopIndex) {
/* 1475 */     if (loopIndex > 0) {
/* 1476 */       append(separator);
/*      */     }
/* 1478 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder appendPadding(int length, char padChar) {
/* 1489 */     if (length >= 0) {
/* 1490 */       ensureCapacity(this.size + length);
/* 1491 */       for (int i = 0; i < length; i++) {
/* 1492 */         this.buffer[this.size++] = padChar;
/*      */       }
/*      */     } 
/* 1495 */     return this;
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
/*      */   public StrBuilder appendFixedWidthPadLeft(Object obj, int width, char padChar) {
/* 1510 */     if (width > 0) {
/* 1511 */       ensureCapacity(this.size + width);
/* 1512 */       String str = (obj == null) ? getNullText() : obj.toString();
/* 1513 */       if (str == null) {
/* 1514 */         str = "";
/*      */       }
/* 1516 */       int strLen = str.length();
/* 1517 */       if (strLen >= width) {
/* 1518 */         str.getChars(strLen - width, strLen, this.buffer, this.size);
/*      */       } else {
/* 1520 */         int padLen = width - strLen;
/* 1521 */         for (int i = 0; i < padLen; i++) {
/* 1522 */           this.buffer[this.size + i] = padChar;
/*      */         }
/* 1524 */         str.getChars(0, strLen, this.buffer, this.size + padLen);
/*      */       } 
/* 1526 */       this.size += width;
/*      */     } 
/* 1528 */     return this;
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
/*      */   public StrBuilder appendFixedWidthPadLeft(int value, int width, char padChar) {
/* 1542 */     return appendFixedWidthPadLeft(String.valueOf(value), width, padChar);
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
/*      */   public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
/* 1557 */     if (width > 0) {
/* 1558 */       ensureCapacity(this.size + width);
/* 1559 */       String str = (obj == null) ? getNullText() : obj.toString();
/* 1560 */       if (str == null) {
/* 1561 */         str = "";
/*      */       }
/* 1563 */       int strLen = str.length();
/* 1564 */       if (strLen >= width) {
/* 1565 */         str.getChars(0, width, this.buffer, this.size);
/*      */       } else {
/* 1567 */         int padLen = width - strLen;
/* 1568 */         str.getChars(0, strLen, this.buffer, this.size);
/* 1569 */         for (int i = 0; i < padLen; i++) {
/* 1570 */           this.buffer[this.size + strLen + i] = padChar;
/*      */         }
/*      */       } 
/* 1573 */       this.size += width;
/*      */     } 
/* 1575 */     return this;
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
/*      */   public StrBuilder appendFixedWidthPadRight(int value, int width, char padChar) {
/* 1589 */     return appendFixedWidthPadRight(String.valueOf(value), width, padChar);
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
/*      */   public StrBuilder insert(int index, Object obj) {
/* 1602 */     if (obj == null) {
/* 1603 */       return insert(index, this.nullText);
/*      */     }
/* 1605 */     return insert(index, obj.toString());
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
/*      */   public StrBuilder insert(int index, String str) {
/* 1618 */     validateIndex(index);
/* 1619 */     if (str == null) {
/* 1620 */       str = this.nullText;
/*      */     }
/* 1622 */     if (str != null) {
/* 1623 */       int strLen = str.length();
/* 1624 */       if (strLen > 0) {
/* 1625 */         int newSize = this.size + strLen;
/* 1626 */         ensureCapacity(newSize);
/* 1627 */         System.arraycopy(this.buffer, index, this.buffer, index + strLen, this.size - index);
/* 1628 */         this.size = newSize;
/* 1629 */         str.getChars(0, strLen, this.buffer, index);
/*      */       } 
/*      */     } 
/* 1632 */     return this;
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
/*      */   public StrBuilder insert(int index, char[] chars) {
/* 1645 */     validateIndex(index);
/* 1646 */     if (chars == null) {
/* 1647 */       return insert(index, this.nullText);
/*      */     }
/* 1649 */     int len = chars.length;
/* 1650 */     if (len > 0) {
/* 1651 */       ensureCapacity(this.size + len);
/* 1652 */       System.arraycopy(this.buffer, index, this.buffer, index + len, this.size - index);
/* 1653 */       System.arraycopy(chars, 0, this.buffer, index, len);
/* 1654 */       this.size += len;
/*      */     } 
/* 1656 */     return this;
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
/*      */   public StrBuilder insert(int index, char[] chars, int offset, int length) {
/* 1671 */     validateIndex(index);
/* 1672 */     if (chars == null) {
/* 1673 */       return insert(index, this.nullText);
/*      */     }
/* 1675 */     if (offset < 0 || offset > chars.length) {
/* 1676 */       throw new StringIndexOutOfBoundsException("Invalid offset: " + offset);
/*      */     }
/* 1678 */     if (length < 0 || offset + length > chars.length) {
/* 1679 */       throw new StringIndexOutOfBoundsException("Invalid length: " + length);
/*      */     }
/* 1681 */     if (length > 0) {
/* 1682 */       ensureCapacity(this.size + length);
/* 1683 */       System.arraycopy(this.buffer, index, this.buffer, index + length, this.size - index);
/* 1684 */       System.arraycopy(chars, offset, this.buffer, index, length);
/* 1685 */       this.size += length;
/*      */     } 
/* 1687 */     return this;
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
/*      */   public StrBuilder insert(int index, boolean value) {
/* 1699 */     validateIndex(index);
/* 1700 */     if (value) {
/* 1701 */       ensureCapacity(this.size + 4);
/* 1702 */       System.arraycopy(this.buffer, index, this.buffer, index + 4, this.size - index);
/* 1703 */       this.buffer[index++] = 't';
/* 1704 */       this.buffer[index++] = 'r';
/* 1705 */       this.buffer[index++] = 'u';
/* 1706 */       this.buffer[index] = 'e';
/* 1707 */       this.size += 4;
/*      */     } else {
/* 1709 */       ensureCapacity(this.size + 5);
/* 1710 */       System.arraycopy(this.buffer, index, this.buffer, index + 5, this.size - index);
/* 1711 */       this.buffer[index++] = 'f';
/* 1712 */       this.buffer[index++] = 'a';
/* 1713 */       this.buffer[index++] = 'l';
/* 1714 */       this.buffer[index++] = 's';
/* 1715 */       this.buffer[index] = 'e';
/* 1716 */       this.size += 5;
/*      */     } 
/* 1718 */     return this;
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
/*      */   public StrBuilder insert(int index, char value) {
/* 1730 */     validateIndex(index);
/* 1731 */     ensureCapacity(this.size + 1);
/* 1732 */     System.arraycopy(this.buffer, index, this.buffer, index + 1, this.size - index);
/* 1733 */     this.buffer[index] = value;
/* 1734 */     this.size++;
/* 1735 */     return this;
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
/*      */   public StrBuilder insert(int index, int value) {
/* 1747 */     return insert(index, String.valueOf(value));
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
/*      */   public StrBuilder insert(int index, long value) {
/* 1759 */     return insert(index, String.valueOf(value));
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
/*      */   public StrBuilder insert(int index, float value) {
/* 1771 */     return insert(index, String.valueOf(value));
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
/*      */   public StrBuilder insert(int index, double value) {
/* 1783 */     return insert(index, String.valueOf(value));
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
/*      */   private void deleteImpl(int startIndex, int endIndex, int len) {
/* 1795 */     System.arraycopy(this.buffer, endIndex, this.buffer, startIndex, this.size - endIndex);
/* 1796 */     this.size -= len;
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
/*      */   public StrBuilder delete(int startIndex, int endIndex) {
/* 1809 */     endIndex = validateRange(startIndex, endIndex);
/* 1810 */     int len = endIndex - startIndex;
/* 1811 */     if (len > 0) {
/* 1812 */       deleteImpl(startIndex, endIndex, len);
/*      */     }
/* 1814 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder deleteAll(char ch) {
/* 1824 */     for (int i = 0; i < this.size; i++) {
/* 1825 */       if (this.buffer[i] == ch) {
/* 1826 */         int start = i; do {  }
/* 1827 */         while (++i < this.size && 
/* 1828 */           this.buffer[i] == ch);
/*      */ 
/*      */ 
/*      */         
/* 1832 */         int len = i - start;
/* 1833 */         deleteImpl(start, i, len);
/* 1834 */         i -= len;
/*      */       } 
/*      */     } 
/* 1837 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder deleteFirst(char ch) {
/* 1847 */     for (int i = 0; i < this.size; i++) {
/* 1848 */       if (this.buffer[i] == ch) {
/* 1849 */         deleteImpl(i, i + 1, 1);
/*      */         break;
/*      */       } 
/*      */     } 
/* 1853 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder deleteAll(String str) {
/* 1863 */     int len = StringUtils.length(str);
/* 1864 */     if (len > 0) {
/* 1865 */       int index = indexOf(str, 0);
/* 1866 */       while (index >= 0) {
/* 1867 */         deleteImpl(index, index + len, len);
/* 1868 */         index = indexOf(str, index);
/*      */       } 
/*      */     } 
/* 1871 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder deleteFirst(String str) {
/* 1881 */     int len = StringUtils.length(str);
/* 1882 */     if (len > 0) {
/* 1883 */       int index = indexOf(str, 0);
/* 1884 */       if (index >= 0) {
/* 1885 */         deleteImpl(index, index + len, len);
/*      */       }
/*      */     } 
/* 1888 */     return this;
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
/*      */   public StrBuilder deleteAll(StrMatcher matcher) {
/* 1903 */     return replace(matcher, null, 0, this.size, -1);
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
/*      */   public StrBuilder deleteFirst(StrMatcher matcher) {
/* 1918 */     return replace(matcher, null, 0, this.size, 1);
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
/*      */   private void replaceImpl(int startIndex, int endIndex, int removeLen, String insertStr, int insertLen) {
/* 1932 */     int newSize = this.size - removeLen + insertLen;
/* 1933 */     if (insertLen != removeLen) {
/* 1934 */       ensureCapacity(newSize);
/* 1935 */       System.arraycopy(this.buffer, endIndex, this.buffer, startIndex + insertLen, this.size - endIndex);
/* 1936 */       this.size = newSize;
/*      */     } 
/* 1938 */     if (insertLen > 0) {
/* 1939 */       insertStr.getChars(0, insertLen, this.buffer, startIndex);
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
/*      */   public StrBuilder replace(int startIndex, int endIndex, String replaceStr) {
/* 1955 */     endIndex = validateRange(startIndex, endIndex);
/* 1956 */     int insertLen = StringUtils.length(replaceStr);
/* 1957 */     replaceImpl(startIndex, endIndex, endIndex - startIndex, replaceStr, insertLen);
/* 1958 */     return this;
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
/*      */   public StrBuilder replaceAll(char search, char replace) {
/* 1970 */     if (search != replace) {
/* 1971 */       for (int i = 0; i < this.size; i++) {
/* 1972 */         if (this.buffer[i] == search) {
/* 1973 */           this.buffer[i] = replace;
/*      */         }
/*      */       } 
/*      */     }
/* 1977 */     return this;
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
/*      */   public StrBuilder replaceFirst(char search, char replace) {
/* 1989 */     if (search != replace) {
/* 1990 */       for (int i = 0; i < this.size; i++) {
/* 1991 */         if (this.buffer[i] == search) {
/* 1992 */           this.buffer[i] = replace;
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     }
/* 1997 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder replaceAll(String searchStr, String replaceStr) {
/* 2008 */     int searchLen = StringUtils.length(searchStr);
/* 2009 */     if (searchLen > 0) {
/* 2010 */       int replaceLen = StringUtils.length(replaceStr);
/* 2011 */       int index = indexOf(searchStr, 0);
/* 2012 */       while (index >= 0) {
/* 2013 */         replaceImpl(index, index + searchLen, searchLen, replaceStr, replaceLen);
/* 2014 */         index = indexOf(searchStr, index + replaceLen);
/*      */       } 
/*      */     } 
/* 2017 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder replaceFirst(String searchStr, String replaceStr) {
/* 2028 */     int searchLen = StringUtils.length(searchStr);
/* 2029 */     if (searchLen > 0) {
/* 2030 */       int index = indexOf(searchStr, 0);
/* 2031 */       if (index >= 0) {
/* 2032 */         int replaceLen = StringUtils.length(replaceStr);
/* 2033 */         replaceImpl(index, index + searchLen, searchLen, replaceStr, replaceLen);
/*      */       } 
/*      */     } 
/* 2036 */     return this;
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
/*      */   public StrBuilder replaceAll(StrMatcher matcher, String replaceStr) {
/* 2052 */     return replace(matcher, replaceStr, 0, this.size, -1);
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
/*      */   public StrBuilder replaceFirst(StrMatcher matcher, String replaceStr) {
/* 2068 */     return replace(matcher, replaceStr, 0, this.size, 1);
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
/*      */   public StrBuilder replace(StrMatcher matcher, String replaceStr, int startIndex, int endIndex, int replaceCount) {
/* 2091 */     endIndex = validateRange(startIndex, endIndex);
/* 2092 */     return replaceImpl(matcher, replaceStr, startIndex, endIndex, replaceCount);
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
/*      */   private StrBuilder replaceImpl(StrMatcher matcher, String replaceStr, int from, int to, int replaceCount) {
/* 2114 */     if (matcher == null || this.size == 0) {
/* 2115 */       return this;
/*      */     }
/* 2117 */     int replaceLen = StringUtils.length(replaceStr);
/* 2118 */     for (int i = from; i < to && replaceCount != 0; i++) {
/* 2119 */       char[] buf = this.buffer;
/* 2120 */       int removeLen = matcher.isMatch(buf, i, from, to);
/* 2121 */       if (removeLen > 0) {
/* 2122 */         replaceImpl(i, i + removeLen, removeLen, replaceStr, replaceLen);
/* 2123 */         to = to - removeLen + replaceLen;
/* 2124 */         i = i + replaceLen - 1;
/* 2125 */         if (replaceCount > 0) {
/* 2126 */           replaceCount--;
/*      */         }
/*      */       } 
/*      */     } 
/* 2130 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder reverse() {
/* 2139 */     if (this.size == 0) {
/* 2140 */       return this;
/*      */     }
/*      */     
/* 2143 */     int half = this.size / 2;
/* 2144 */     char[] buf = this.buffer;
/* 2145 */     for (int leftIdx = 0, rightIdx = this.size - 1; leftIdx < half; leftIdx++, rightIdx--) {
/* 2146 */       char swap = buf[leftIdx];
/* 2147 */       buf[leftIdx] = buf[rightIdx];
/* 2148 */       buf[rightIdx] = swap;
/*      */     } 
/* 2150 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrBuilder trim() {
/* 2160 */     if (this.size == 0) {
/* 2161 */       return this;
/*      */     }
/* 2163 */     int len = this.size;
/* 2164 */     char[] buf = this.buffer;
/* 2165 */     int pos = 0;
/* 2166 */     while (pos < len && buf[pos] <= ' ') {
/* 2167 */       pos++;
/*      */     }
/* 2169 */     while (pos < len && buf[len - 1] <= ' ') {
/* 2170 */       len--;
/*      */     }
/* 2172 */     if (len < this.size) {
/* 2173 */       delete(len, this.size);
/*      */     }
/* 2175 */     if (pos > 0) {
/* 2176 */       delete(0, pos);
/*      */     }
/* 2178 */     return this;
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
/*      */   public boolean startsWith(String str) {
/* 2191 */     if (str == null) {
/* 2192 */       return false;
/*      */     }
/* 2194 */     int len = str.length();
/* 2195 */     if (len == 0) {
/* 2196 */       return true;
/*      */     }
/* 2198 */     if (len > this.size) {
/* 2199 */       return false;
/*      */     }
/* 2201 */     for (int i = 0; i < len; i++) {
/* 2202 */       if (this.buffer[i] != str.charAt(i)) {
/* 2203 */         return false;
/*      */       }
/*      */     } 
/* 2206 */     return true;
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
/*      */   public boolean endsWith(String str) {
/* 2219 */     if (str == null) {
/* 2220 */       return false;
/*      */     }
/* 2222 */     int len = str.length();
/* 2223 */     if (len == 0) {
/* 2224 */       return true;
/*      */     }
/* 2226 */     if (len > this.size) {
/* 2227 */       return false;
/*      */     }
/* 2229 */     int pos = this.size - len;
/* 2230 */     for (int i = 0; i < len; i++, pos++) {
/* 2231 */       if (this.buffer[pos] != str.charAt(i)) {
/* 2232 */         return false;
/*      */       }
/*      */     } 
/* 2235 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CharSequence subSequence(int startIndex, int endIndex) {
/* 2244 */     if (startIndex < 0) {
/* 2245 */       throw new StringIndexOutOfBoundsException(startIndex);
/*      */     }
/* 2247 */     if (endIndex > this.size) {
/* 2248 */       throw new StringIndexOutOfBoundsException(endIndex);
/*      */     }
/* 2250 */     if (startIndex > endIndex) {
/* 2251 */       throw new StringIndexOutOfBoundsException(endIndex - startIndex);
/*      */     }
/* 2253 */     return substring(startIndex, endIndex);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String substring(int start) {
/* 2264 */     return substring(start, this.size);
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
/*      */   public String substring(int startIndex, int endIndex) {
/* 2282 */     endIndex = validateRange(startIndex, endIndex);
/* 2283 */     return new String(this.buffer, startIndex, endIndex - startIndex);
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
/*      */   public String leftString(int length) {
/* 2300 */     if (length <= 0) {
/* 2301 */       return "";
/*      */     }
/* 2303 */     if (length >= this.size) {
/* 2304 */       return new String(this.buffer, 0, this.size);
/*      */     }
/* 2306 */     return new String(this.buffer, 0, length);
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
/*      */   public String rightString(int length) {
/* 2323 */     if (length <= 0) {
/* 2324 */       return "";
/*      */     }
/* 2326 */     if (length >= this.size) {
/* 2327 */       return new String(this.buffer, 0, this.size);
/*      */     }
/* 2329 */     return new String(this.buffer, this.size - length, length);
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
/*      */   public String midString(int index, int length) {
/* 2350 */     if (index < 0) {
/* 2351 */       index = 0;
/*      */     }
/* 2353 */     if (length <= 0 || index >= this.size) {
/* 2354 */       return "";
/*      */     }
/* 2356 */     if (this.size <= index + length) {
/* 2357 */       return new String(this.buffer, index, this.size - index);
/*      */     }
/* 2359 */     return new String(this.buffer, index, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean contains(char ch) {
/* 2369 */     char[] thisBuf = this.buffer;
/* 2370 */     for (int i = 0; i < this.size; i++) {
/* 2371 */       if (thisBuf[i] == ch) {
/* 2372 */         return true;
/*      */       }
/*      */     } 
/* 2375 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean contains(String str) {
/* 2385 */     return (indexOf(str, 0) >= 0);
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
/*      */   public boolean contains(StrMatcher matcher) {
/* 2401 */     return (indexOf(matcher, 0) >= 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int indexOf(char ch) {
/* 2411 */     return indexOf(ch, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int indexOf(char ch, int startIndex) {
/* 2422 */     startIndex = Math.max(startIndex, 0);
/* 2423 */     if (startIndex >= this.size) {
/* 2424 */       return -1;
/*      */     }
/* 2426 */     char[] thisBuf = this.buffer;
/* 2427 */     for (int i = startIndex; i < this.size; i++) {
/* 2428 */       if (thisBuf[i] == ch) {
/* 2429 */         return i;
/*      */       }
/*      */     } 
/* 2432 */     return -1;
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
/*      */   public int indexOf(String str) {
/* 2445 */     return indexOf(str, 0);
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
/*      */   public int indexOf(String str, int startIndex) {
/* 2460 */     return StringUtils.indexOf(this, str, startIndex);
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
/*      */   public int indexOf(StrMatcher matcher) {
/* 2475 */     return indexOf(matcher, 0);
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
/*      */   public int indexOf(StrMatcher matcher, int startIndex) {
/* 2492 */     startIndex = Math.max(startIndex, 0);
/* 2493 */     if (matcher == null || startIndex >= this.size) {
/* 2494 */       return -1;
/*      */     }
/* 2496 */     int len = this.size;
/* 2497 */     char[] buf = this.buffer;
/* 2498 */     for (int i = startIndex; i < len; i++) {
/* 2499 */       if (matcher.isMatch(buf, i, startIndex, len) > 0) {
/* 2500 */         return i;
/*      */       }
/*      */     } 
/* 2503 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int lastIndexOf(char ch) {
/* 2513 */     return lastIndexOf(ch, this.size - 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int lastIndexOf(char ch, int startIndex) {
/* 2524 */     startIndex = (startIndex >= this.size) ? (this.size - 1) : startIndex;
/* 2525 */     if (startIndex < 0) {
/* 2526 */       return -1;
/*      */     }
/* 2528 */     for (int i = startIndex; i >= 0; i--) {
/* 2529 */       if (this.buffer[i] == ch) {
/* 2530 */         return i;
/*      */       }
/*      */     } 
/* 2533 */     return -1;
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
/*      */   public int lastIndexOf(String str) {
/* 2546 */     return lastIndexOf(str, this.size - 1);
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
/*      */   public int lastIndexOf(String str, int startIndex) {
/* 2561 */     return StringUtils.lastIndexOf(this, str, startIndex);
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
/*      */   public int lastIndexOf(StrMatcher matcher) {
/* 2576 */     return lastIndexOf(matcher, this.size);
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
/*      */   public int lastIndexOf(StrMatcher matcher, int startIndex) {
/* 2593 */     startIndex = (startIndex >= this.size) ? (this.size - 1) : startIndex;
/* 2594 */     if (matcher == null || startIndex < 0) {
/* 2595 */       return -1;
/*      */     }
/* 2597 */     char[] buf = this.buffer;
/* 2598 */     int endIndex = startIndex + 1;
/* 2599 */     for (int i = startIndex; i >= 0; i--) {
/* 2600 */       if (matcher.isMatch(buf, i, 0, endIndex) > 0) {
/* 2601 */         return i;
/*      */       }
/*      */     } 
/* 2604 */     return -1;
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
/*      */   public StrTokenizer asTokenizer() {
/* 2645 */     return new StrBuilderTokenizer();
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
/*      */   public Reader asReader() {
/* 2672 */     return new StrBuilderReader();
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
/*      */   public Writer asWriter() {
/* 2700 */     return new StrBuilderWriter();
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
/*      */   public void appendTo(Appendable appendable) throws IOException {
/* 2717 */     if (appendable instanceof Writer) {
/* 2718 */       ((Writer)appendable).write(this.buffer, 0, this.size);
/* 2719 */     } else if (appendable instanceof StringBuilder) {
/* 2720 */       ((StringBuilder)appendable).append(this.buffer, 0, this.size);
/* 2721 */     } else if (appendable instanceof StringBuffer) {
/* 2722 */       ((StringBuffer)appendable).append(this.buffer, 0, this.size);
/* 2723 */     } else if (appendable instanceof CharBuffer) {
/* 2724 */       ((CharBuffer)appendable).put(this.buffer, 0, this.size);
/*      */     } else {
/* 2726 */       appendable.append(this);
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
/*      */   public boolean equalsIgnoreCase(StrBuilder other) {
/* 2738 */     if (this == other) {
/* 2739 */       return true;
/*      */     }
/* 2741 */     if (this.size != other.size) {
/* 2742 */       return false;
/*      */     }
/* 2744 */     char[] thisBuf = this.buffer;
/* 2745 */     char[] otherBuf = other.buffer;
/* 2746 */     for (int i = this.size - 1; i >= 0; i--) {
/* 2747 */       char c1 = thisBuf[i];
/* 2748 */       char c2 = otherBuf[i];
/* 2749 */       if (c1 != c2 && Character.toUpperCase(c1) != Character.toUpperCase(c2)) {
/* 2750 */         return false;
/*      */       }
/*      */     } 
/* 2753 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(StrBuilder other) {
/* 2764 */     if (this == other) {
/* 2765 */       return true;
/*      */     }
/* 2767 */     if (other == null) {
/* 2768 */       return false;
/*      */     }
/* 2770 */     if (this.size != other.size) {
/* 2771 */       return false;
/*      */     }
/* 2773 */     char[] thisBuf = this.buffer;
/* 2774 */     char[] otherBuf = other.buffer;
/* 2775 */     for (int i = this.size - 1; i >= 0; i--) {
/* 2776 */       if (thisBuf[i] != otherBuf[i]) {
/* 2777 */         return false;
/*      */       }
/*      */     } 
/* 2780 */     return true;
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
/*      */   public boolean equals(Object obj) {
/* 2792 */     return (obj instanceof StrBuilder && equals((StrBuilder)obj));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 2802 */     char[] buf = this.buffer;
/* 2803 */     int hash = 0;
/* 2804 */     for (int i = this.size - 1; i >= 0; i--) {
/* 2805 */       hash = 31 * hash + buf[i];
/*      */     }
/* 2807 */     return hash;
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
/*      */   public String toString() {
/* 2822 */     return new String(this.buffer, 0, this.size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuffer toStringBuffer() {
/* 2832 */     return (new StringBuffer(this.size)).append(this.buffer, 0, this.size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder toStringBuilder() {
/* 2843 */     return (new StringBuilder(this.size)).append(this.buffer, 0, this.size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String build() {
/* 2854 */     return toString();
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
/*      */   protected int validateRange(int startIndex, int endIndex) {
/* 2867 */     if (startIndex < 0) {
/* 2868 */       throw new StringIndexOutOfBoundsException(startIndex);
/*      */     }
/* 2870 */     if (endIndex > this.size) {
/* 2871 */       endIndex = this.size;
/*      */     }
/* 2873 */     if (startIndex > endIndex) {
/* 2874 */       throw new StringIndexOutOfBoundsException("end < start");
/*      */     }
/* 2876 */     return endIndex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void validateIndex(int index) {
/* 2886 */     if (index < 0 || index > this.size) {
/* 2887 */       throw new StringIndexOutOfBoundsException(index);
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
/*      */   class StrBuilderTokenizer
/*      */     extends StrTokenizer
/*      */   {
/*      */     protected List<String> tokenize(char[] chars, int offset, int count) {
/* 2905 */       if (chars == null) {
/* 2906 */         return super.tokenize(StrBuilder.this.buffer, 0, StrBuilder.this.size());
/*      */       }
/* 2908 */       return super.tokenize(chars, offset, count);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String getContent() {
/* 2914 */       String str = super.getContent();
/* 2915 */       if (str == null) {
/* 2916 */         return StrBuilder.this.toString();
/*      */       }
/* 2918 */       return str;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class StrBuilderReader
/*      */     extends Reader
/*      */   {
/*      */     private int pos;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int mark;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void close() {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int read() {
/* 2946 */       if (!ready()) {
/* 2947 */         return -1;
/*      */       }
/* 2949 */       return StrBuilder.this.charAt(this.pos++);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int read(char[] b, int off, int len) {
/* 2955 */       if (off < 0 || len < 0 || off > b.length || off + len > b.length || off + len < 0)
/*      */       {
/* 2957 */         throw new IndexOutOfBoundsException();
/*      */       }
/* 2959 */       if (len == 0) {
/* 2960 */         return 0;
/*      */       }
/* 2962 */       if (this.pos >= StrBuilder.this.size()) {
/* 2963 */         return -1;
/*      */       }
/* 2965 */       if (this.pos + len > StrBuilder.this.size()) {
/* 2966 */         len = StrBuilder.this.size() - this.pos;
/*      */       }
/* 2968 */       StrBuilder.this.getChars(this.pos, this.pos + len, b, off);
/* 2969 */       this.pos += len;
/* 2970 */       return len;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public long skip(long n) {
/* 2976 */       if (this.pos + n > StrBuilder.this.size()) {
/* 2977 */         n = (StrBuilder.this.size() - this.pos);
/*      */       }
/* 2979 */       if (n < 0L) {
/* 2980 */         return 0L;
/*      */       }
/* 2982 */       this.pos = Math.addExact(this.pos, Math.toIntExact(n));
/* 2983 */       return n;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean ready() {
/* 2989 */       return (this.pos < StrBuilder.this.size());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean markSupported() {
/* 2995 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void mark(int readAheadLimit) {
/* 3001 */       this.mark = this.pos;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void reset() {
/* 3007 */       this.pos = this.mark;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class StrBuilderWriter
/*      */     extends Writer
/*      */   {
/*      */     public void close() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void flush() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void write(int c) {
/* 3037 */       StrBuilder.this.append((char)c);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void write(char[] cbuf) {
/* 3043 */       StrBuilder.this.append(cbuf);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void write(char[] cbuf, int off, int len) {
/* 3049 */       StrBuilder.this.append(cbuf, off, len);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void write(String str) {
/* 3055 */       StrBuilder.this.append(str);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void write(String str, int off, int len) {
/* 3061 */       StrBuilder.this.append(str, off, len);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\text\StrBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */