/*      */ package org.apache.commons.lang3.text;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import org.apache.commons.lang3.ArrayUtils;
/*      */ import org.apache.commons.lang3.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */ public class StrTokenizer
/*      */   implements ListIterator<String>, Cloneable
/*      */ {
/*  102 */   private static final StrTokenizer CSV_TOKENIZER_PROTOTYPE = new StrTokenizer(); static {
/*  103 */     CSV_TOKENIZER_PROTOTYPE.setDelimiterMatcher(StrMatcher.commaMatcher());
/*  104 */     CSV_TOKENIZER_PROTOTYPE.setQuoteMatcher(StrMatcher.doubleQuoteMatcher());
/*  105 */     CSV_TOKENIZER_PROTOTYPE.setIgnoredMatcher(StrMatcher.noneMatcher());
/*  106 */     CSV_TOKENIZER_PROTOTYPE.setTrimmerMatcher(StrMatcher.trimMatcher());
/*  107 */     CSV_TOKENIZER_PROTOTYPE.setEmptyTokenAsNull(false);
/*  108 */     CSV_TOKENIZER_PROTOTYPE.setIgnoreEmptyTokens(false);
/*      */   }
/*  110 */   private static final StrTokenizer TSV_TOKENIZER_PROTOTYPE = new StrTokenizer(); static {
/*  111 */     TSV_TOKENIZER_PROTOTYPE.setDelimiterMatcher(StrMatcher.tabMatcher());
/*  112 */     TSV_TOKENIZER_PROTOTYPE.setQuoteMatcher(StrMatcher.doubleQuoteMatcher());
/*  113 */     TSV_TOKENIZER_PROTOTYPE.setIgnoredMatcher(StrMatcher.noneMatcher());
/*  114 */     TSV_TOKENIZER_PROTOTYPE.setTrimmerMatcher(StrMatcher.trimMatcher());
/*  115 */     TSV_TOKENIZER_PROTOTYPE.setEmptyTokenAsNull(false);
/*  116 */     TSV_TOKENIZER_PROTOTYPE.setIgnoreEmptyTokens(false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private char[] chars;
/*      */   
/*      */   private String[] tokens;
/*      */   
/*      */   private int tokenPos;
/*      */   
/*  127 */   private StrMatcher delimMatcher = StrMatcher.splitMatcher();
/*      */   
/*  129 */   private StrMatcher quoteMatcher = StrMatcher.noneMatcher();
/*      */   
/*  131 */   private StrMatcher ignoredMatcher = StrMatcher.noneMatcher();
/*      */   
/*  133 */   private StrMatcher trimmerMatcher = StrMatcher.noneMatcher();
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean emptyAsNull;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean ignoreEmptyTokens = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static StrTokenizer getCSVClone() {
/*  147 */     return (StrTokenizer)CSV_TOKENIZER_PROTOTYPE.clone();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static StrTokenizer getCSVInstance() {
/*  161 */     return getCSVClone();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static StrTokenizer getCSVInstance(String input) {
/*  174 */     StrTokenizer tok = getCSVClone();
/*  175 */     tok.reset(input);
/*  176 */     return tok;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static StrTokenizer getCSVInstance(char[] input) {
/*  189 */     StrTokenizer tok = getCSVClone();
/*  190 */     tok.reset(input);
/*  191 */     return tok;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static StrTokenizer getTSVClone() {
/*  200 */     return (StrTokenizer)TSV_TOKENIZER_PROTOTYPE.clone();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static StrTokenizer getTSVInstance() {
/*  214 */     return getTSVClone();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static StrTokenizer getTSVInstance(String input) {
/*  225 */     StrTokenizer tok = getTSVClone();
/*  226 */     tok.reset(input);
/*  227 */     return tok;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static StrTokenizer getTSVInstance(char[] input) {
/*  238 */     StrTokenizer tok = getTSVClone();
/*  239 */     tok.reset(input);
/*  240 */     return tok;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer() {
/*  251 */     this.chars = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(String input) {
/*  261 */     if (input != null) {
/*  262 */       this.chars = input.toCharArray();
/*      */     } else {
/*  264 */       this.chars = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(String input, char delim) {
/*  275 */     this(input);
/*  276 */     setDelimiterChar(delim);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(String input, String delim) {
/*  286 */     this(input);
/*  287 */     setDelimiterString(delim);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(String input, StrMatcher delim) {
/*  297 */     this(input);
/*  298 */     setDelimiterMatcher(delim);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(String input, char delim, char quote) {
/*  310 */     this(input, delim);
/*  311 */     setQuoteChar(quote);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(String input, StrMatcher delim, StrMatcher quote) {
/*  323 */     this(input, delim);
/*  324 */     setQuoteMatcher(quote);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(char[] input) {
/*  334 */     this.chars = ArrayUtils.clone(input);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(char[] input, char delim) {
/*  344 */     this(input);
/*  345 */     setDelimiterChar(delim);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(char[] input, String delim) {
/*  355 */     this(input);
/*  356 */     setDelimiterString(delim);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(char[] input, StrMatcher delim) {
/*  366 */     this(input);
/*  367 */     setDelimiterMatcher(delim);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(char[] input, char delim, char quote) {
/*  379 */     this(input, delim);
/*  380 */     setQuoteChar(quote);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(char[] input, StrMatcher delim, StrMatcher quote) {
/*  392 */     this(input, delim);
/*  393 */     setQuoteMatcher(quote);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  403 */     checkTokenized();
/*  404 */     return this.tokens.length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String nextToken() {
/*  415 */     if (hasNext()) {
/*  416 */       return this.tokens[this.tokenPos++];
/*      */     }
/*  418 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String previousToken() {
/*  427 */     if (hasPrevious()) {
/*  428 */       return this.tokens[--this.tokenPos];
/*      */     }
/*  430 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getTokenArray() {
/*  439 */     checkTokenized();
/*  440 */     return (String[])this.tokens.clone();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getTokenList() {
/*  449 */     checkTokenized();
/*  450 */     List<String> list = new ArrayList<>(this.tokens.length);
/*  451 */     list.addAll(Arrays.asList(this.tokens));
/*  452 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer reset() {
/*  464 */     this.tokenPos = 0;
/*  465 */     this.tokens = null;
/*  466 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer reset(String input) {
/*  478 */     reset();
/*  479 */     if (input != null) {
/*  480 */       this.chars = input.toCharArray();
/*      */     } else {
/*  482 */       this.chars = null;
/*      */     } 
/*  484 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer reset(char[] input) {
/*  496 */     reset();
/*  497 */     this.chars = ArrayUtils.clone(input);
/*  498 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasNext() {
/*  508 */     checkTokenized();
/*  509 */     return (this.tokenPos < this.tokens.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String next() {
/*  520 */     if (hasNext()) {
/*  521 */       return this.tokens[this.tokenPos++];
/*      */     }
/*  523 */     throw new NoSuchElementException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int nextIndex() {
/*  533 */     return this.tokenPos;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasPrevious() {
/*  543 */     checkTokenized();
/*  544 */     return (this.tokenPos > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String previous() {
/*  554 */     if (hasPrevious()) {
/*  555 */       return this.tokens[--this.tokenPos];
/*      */     }
/*  557 */     throw new NoSuchElementException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int previousIndex() {
/*  567 */     return this.tokenPos - 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void remove() {
/*  577 */     throw new UnsupportedOperationException("remove() is unsupported");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void set(String obj) {
/*  587 */     throw new UnsupportedOperationException("set() is unsupported");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add(String obj) {
/*  597 */     throw new UnsupportedOperationException("add() is unsupported");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkTokenized() {
/*  604 */     if (this.tokens == null) {
/*  605 */       if (this.chars == null) {
/*      */         
/*  607 */         List<String> split = tokenize(null, 0, 0);
/*  608 */         this.tokens = split.<String>toArray(ArrayUtils.EMPTY_STRING_ARRAY);
/*      */       } else {
/*  610 */         List<String> split = tokenize(this.chars, 0, this.chars.length);
/*  611 */         this.tokens = split.<String>toArray(ArrayUtils.EMPTY_STRING_ARRAY);
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
/*      */   protected List<String> tokenize(char[] srcChars, int offset, int count) {
/*  640 */     if (ArrayUtils.isEmpty(srcChars)) {
/*  641 */       return Collections.emptyList();
/*      */     }
/*  643 */     StrBuilder buf = new StrBuilder();
/*  644 */     List<String> tokenList = new ArrayList<>();
/*  645 */     int pos = offset;
/*      */ 
/*      */     
/*  648 */     while (pos >= 0 && pos < count) {
/*      */       
/*  650 */       pos = readNextToken(srcChars, pos, count, buf, tokenList);
/*      */ 
/*      */       
/*  653 */       if (pos >= count) {
/*  654 */         addToken(tokenList, "");
/*      */       }
/*      */     } 
/*  657 */     return tokenList;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addToken(List<String> list, String tok) {
/*  667 */     if (StringUtils.isEmpty(tok)) {
/*  668 */       if (isIgnoreEmptyTokens()) {
/*      */         return;
/*      */       }
/*  671 */       if (isEmptyTokenAsNull()) {
/*  672 */         tok = null;
/*      */       }
/*      */     } 
/*  675 */     list.add(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int readNextToken(char[] srcChars, int start, int len, StrBuilder workArea, List<String> tokenList) {
/*  692 */     while (start < len) {
/*  693 */       int removeLen = Math.max(
/*  694 */           getIgnoredMatcher().isMatch(srcChars, start, start, len), 
/*  695 */           getTrimmerMatcher().isMatch(srcChars, start, start, len));
/*  696 */       if (removeLen == 0 || 
/*  697 */         getDelimiterMatcher().isMatch(srcChars, start, start, len) > 0 || 
/*  698 */         getQuoteMatcher().isMatch(srcChars, start, start, len) > 0) {
/*      */         break;
/*      */       }
/*  701 */       start += removeLen;
/*      */     } 
/*      */ 
/*      */     
/*  705 */     if (start >= len) {
/*  706 */       addToken(tokenList, "");
/*  707 */       return -1;
/*      */     } 
/*      */ 
/*      */     
/*  711 */     int delimLen = getDelimiterMatcher().isMatch(srcChars, start, start, len);
/*  712 */     if (delimLen > 0) {
/*  713 */       addToken(tokenList, "");
/*  714 */       return start + delimLen;
/*      */     } 
/*      */ 
/*      */     
/*  718 */     int quoteLen = getQuoteMatcher().isMatch(srcChars, start, start, len);
/*  719 */     if (quoteLen > 0) {
/*  720 */       return readWithQuotes(srcChars, start + quoteLen, len, workArea, tokenList, start, quoteLen);
/*      */     }
/*  722 */     return readWithQuotes(srcChars, start, len, workArea, tokenList, 0, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int readWithQuotes(char[] srcChars, int start, int len, StrBuilder workArea, List<String> tokenList, int quoteStart, int quoteLen) {
/*  743 */     workArea.clear();
/*  744 */     int pos = start;
/*  745 */     boolean quoting = (quoteLen > 0);
/*  746 */     int trimStart = 0;
/*      */     
/*  748 */     while (pos < len) {
/*      */ 
/*      */ 
/*      */       
/*  752 */       if (quoting) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  759 */         if (isQuote(srcChars, pos, len, quoteStart, quoteLen)) {
/*  760 */           if (isQuote(srcChars, pos + quoteLen, len, quoteStart, quoteLen)) {
/*      */             
/*  762 */             workArea.append(srcChars, pos, quoteLen);
/*  763 */             pos += quoteLen * 2;
/*  764 */             trimStart = workArea.size();
/*      */             
/*      */             continue;
/*      */           } 
/*      */           
/*  769 */           quoting = false;
/*  770 */           pos += quoteLen;
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           continue;
/*      */         } 
/*      */       } else {
/*  778 */         int delimLen = getDelimiterMatcher().isMatch(srcChars, pos, start, len);
/*  779 */         if (delimLen > 0) {
/*      */           
/*  781 */           addToken(tokenList, workArea.substring(0, trimStart));
/*  782 */           return pos + delimLen;
/*      */         } 
/*      */ 
/*      */         
/*  786 */         if (quoteLen > 0 && isQuote(srcChars, pos, len, quoteStart, quoteLen)) {
/*  787 */           quoting = true;
/*  788 */           pos += quoteLen;
/*      */           
/*      */           continue;
/*      */         } 
/*      */         
/*  793 */         int ignoredLen = getIgnoredMatcher().isMatch(srcChars, pos, start, len);
/*  794 */         if (ignoredLen > 0) {
/*  795 */           pos += ignoredLen;
/*      */ 
/*      */           
/*      */           continue;
/*      */         } 
/*      */ 
/*      */         
/*  802 */         int trimmedLen = getTrimmerMatcher().isMatch(srcChars, pos, start, len);
/*  803 */         if (trimmedLen > 0) {
/*  804 */           workArea.append(srcChars, pos, trimmedLen);
/*  805 */           pos += trimmedLen;
/*      */           
/*      */           continue;
/*      */         } 
/*      */       } 
/*  810 */       workArea.append(srcChars[pos++]);
/*  811 */       trimStart = workArea.size();
/*      */     } 
/*      */ 
/*      */     
/*  815 */     addToken(tokenList, workArea.substring(0, trimStart));
/*  816 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isQuote(char[] srcChars, int pos, int len, int quoteStart, int quoteLen) {
/*  831 */     for (int i = 0; i < quoteLen; i++) {
/*  832 */       if (pos + i >= len || srcChars[pos + i] != srcChars[quoteStart + i]) {
/*  833 */         return false;
/*      */       }
/*      */     } 
/*  836 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrMatcher getDelimiterMatcher() {
/*  845 */     return this.delimMatcher;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer setDelimiterMatcher(StrMatcher delim) {
/*  858 */     if (delim == null) {
/*  859 */       this.delimMatcher = StrMatcher.noneMatcher();
/*      */     } else {
/*  861 */       this.delimMatcher = delim;
/*      */     } 
/*  863 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer setDelimiterChar(char delim) {
/*  873 */     return setDelimiterMatcher(StrMatcher.charMatcher(delim));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer setDelimiterString(String delim) {
/*  883 */     return setDelimiterMatcher(StrMatcher.stringMatcher(delim));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrMatcher getQuoteMatcher() {
/*  897 */     return this.quoteMatcher;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer setQuoteMatcher(StrMatcher quote) {
/*  911 */     if (quote != null) {
/*  912 */       this.quoteMatcher = quote;
/*      */     }
/*  914 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer setQuoteChar(char quote) {
/*  928 */     return setQuoteMatcher(StrMatcher.charMatcher(quote));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrMatcher getIgnoredMatcher() {
/*  943 */     return this.ignoredMatcher;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer setIgnoredMatcher(StrMatcher ignored) {
/*  957 */     if (ignored != null) {
/*  958 */       this.ignoredMatcher = ignored;
/*      */     }
/*  960 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer setIgnoredChar(char ignored) {
/*  973 */     return setIgnoredMatcher(StrMatcher.charMatcher(ignored));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrMatcher getTrimmerMatcher() {
/*  987 */     return this.trimmerMatcher;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer setTrimmerMatcher(StrMatcher trimmer) {
/* 1001 */     if (trimmer != null) {
/* 1002 */       this.trimmerMatcher = trimmer;
/*      */     }
/* 1004 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEmptyTokenAsNull() {
/* 1014 */     return this.emptyAsNull;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer setEmptyTokenAsNull(boolean emptyAsNull) {
/* 1025 */     this.emptyAsNull = emptyAsNull;
/* 1026 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isIgnoreEmptyTokens() {
/* 1036 */     return this.ignoreEmptyTokens;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer setIgnoreEmptyTokens(boolean ignoreEmptyTokens) {
/* 1047 */     this.ignoreEmptyTokens = ignoreEmptyTokens;
/* 1048 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getContent() {
/* 1057 */     if (this.chars == null) {
/* 1058 */       return null;
/*      */     }
/* 1060 */     return new String(this.chars);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object clone() {
/*      */     try {
/* 1073 */       return cloneReset();
/* 1074 */     } catch (CloneNotSupportedException ex) {
/* 1075 */       return null;
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
/*      */   Object cloneReset() throws CloneNotSupportedException {
/* 1088 */     StrTokenizer cloned = (StrTokenizer)super.clone();
/* 1089 */     if (cloned.chars != null) {
/* 1090 */       cloned.chars = (char[])cloned.chars.clone();
/*      */     }
/* 1092 */     cloned.reset();
/* 1093 */     return cloned;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1103 */     if (this.tokens == null) {
/* 1104 */       return "StrTokenizer[not tokenized yet]";
/*      */     }
/* 1106 */     return "StrTokenizer" + getTokenList();
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\text\StrTokenizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */