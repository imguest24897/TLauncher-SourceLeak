/*      */ package org.apache.commons.text;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Properties;
/*      */ import java.util.function.Function;
/*      */ import java.util.stream.Collectors;
/*      */ import org.apache.commons.lang3.Validate;
/*      */ import org.apache.commons.text.lookup.StringLookup;
/*      */ import org.apache.commons.text.lookup.StringLookupFactory;
/*      */ import org.apache.commons.text.matcher.StringMatcher;
/*      */ import org.apache.commons.text.matcher.StringMatcherFactory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class StringSubstitutor
/*      */ {
/*      */   public static final char DEFAULT_ESCAPE = '$';
/*      */   public static final String DEFAULT_VAR_DEFAULT = ":-";
/*      */   public static final String DEFAULT_VAR_END = "}";
/*      */   public static final String DEFAULT_VAR_START = "${";
/*      */   
/*      */   private static final class Result
/*      */   {
/*      */     public final boolean altered;
/*      */     public final int lengthChange;
/*      */     
/*      */     private Result(boolean altered, int lengthChange) {
/*  236 */       this.altered = altered;
/*  237 */       this.lengthChange = lengthChange;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  242 */       return "Result [altered=" + this.altered + ", lengthChange=" + this.lengthChange + "]";
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
/*  275 */   public static final StringMatcher DEFAULT_PREFIX = StringMatcherFactory.INSTANCE.stringMatcher("${");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  280 */   public static final StringMatcher DEFAULT_SUFFIX = StringMatcherFactory.INSTANCE.stringMatcher("}");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  285 */   public static final StringMatcher DEFAULT_VALUE_DELIMITER = StringMatcherFactory.INSTANCE
/*  286 */     .stringMatcher(":-");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean disableSubstitutionInValues;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean enableSubstitutionInVariables;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean enableUndefinedVariableException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private char escapeChar;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private StringMatcher prefixMatcher;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean preserveEscapes;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private StringMatcher suffixMatcher;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private StringMatcher valueDelimiterMatcher;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private StringLookup variableResolver;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static StringSubstitutor createInterpolator() {
/*  386 */     return new StringSubstitutor(StringLookupFactory.INSTANCE.interpolatorStringLookup());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> String replace(Object source, Map<String, V> valueMap) {
/*  399 */     return (new StringSubstitutor(valueMap)).replace(source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> String replace(Object source, Map<String, V> valueMap, String prefix, String suffix) {
/*  417 */     return (new StringSubstitutor(valueMap, prefix, suffix)).replace(source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String replace(Object source, Properties valueProperties) {
/*  430 */     if (valueProperties == null) {
/*  431 */       return source.toString();
/*      */     }
/*      */     
/*  434 */     Objects.requireNonNull(valueProperties); return replace(source, (Map<String, ?>)valueProperties.stringPropertyNames().stream().collect(Collectors.toMap(Function.identity(), valueProperties::getProperty)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String replaceSystemProperties(Object source) {
/*  446 */     return (new StringSubstitutor(StringLookupFactory.INSTANCE.systemPropertyStringLookup())).replace(source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringSubstitutor() {
/*  498 */     this((StringLookup)null, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <V> StringSubstitutor(Map<String, V> valueMap) {
/*  509 */     this(StringLookupFactory.INSTANCE.mapStringLookup(valueMap), DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <V> StringSubstitutor(Map<String, V> valueMap, String prefix, String suffix) {
/*  522 */     this(StringLookupFactory.INSTANCE.mapStringLookup(valueMap), prefix, suffix, '$');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <V> StringSubstitutor(Map<String, V> valueMap, String prefix, String suffix, char escape) {
/*  537 */     this(StringLookupFactory.INSTANCE.mapStringLookup(valueMap), prefix, suffix, escape);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <V> StringSubstitutor(Map<String, V> valueMap, String prefix, String suffix, char escape, String valueDelimiter) {
/*  553 */     this(StringLookupFactory.INSTANCE.mapStringLookup(valueMap), prefix, suffix, escape, valueDelimiter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringSubstitutor(StringLookup variableResolver) {
/*  562 */     this(variableResolver, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringSubstitutor(StringLookup variableResolver, String prefix, String suffix, char escape) {
/*  576 */     setVariableResolver(variableResolver);
/*  577 */     setVariablePrefix(prefix);
/*  578 */     setVariableSuffix(suffix);
/*  579 */     setEscapeChar(escape);
/*  580 */     setValueDelimiterMatcher(DEFAULT_VALUE_DELIMITER);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringSubstitutor(StringLookup variableResolver, String prefix, String suffix, char escape, String valueDelimiter) {
/*  595 */     setVariableResolver(variableResolver);
/*  596 */     setVariablePrefix(prefix);
/*  597 */     setVariableSuffix(suffix);
/*  598 */     setEscapeChar(escape);
/*  599 */     setValueDelimiter(valueDelimiter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringSubstitutor(StringLookup variableResolver, StringMatcher prefixMatcher, StringMatcher suffixMatcher, char escape) {
/*  613 */     this(variableResolver, prefixMatcher, suffixMatcher, escape, DEFAULT_VALUE_DELIMITER);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringSubstitutor(StringLookup variableResolver, StringMatcher prefixMatcher, StringMatcher suffixMatcher, char escape, StringMatcher valueDelimiterMatcher) {
/*  628 */     setVariableResolver(variableResolver);
/*  629 */     setVariablePrefixMatcher(prefixMatcher);
/*  630 */     setVariableSuffixMatcher(suffixMatcher);
/*  631 */     setEscapeChar(escape);
/*  632 */     setValueDelimiterMatcher(valueDelimiterMatcher);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringSubstitutor(StringSubstitutor other) {
/*  642 */     this.disableSubstitutionInValues = other.isDisableSubstitutionInValues();
/*  643 */     this.enableSubstitutionInVariables = other.isEnableSubstitutionInVariables();
/*  644 */     this.enableUndefinedVariableException = other.isEnableUndefinedVariableException();
/*  645 */     this.escapeChar = other.getEscapeChar();
/*  646 */     this.prefixMatcher = other.getVariablePrefixMatcher();
/*  647 */     this.preserveEscapes = other.isPreserveEscapes();
/*  648 */     this.suffixMatcher = other.getVariableSuffixMatcher();
/*  649 */     this.valueDelimiterMatcher = other.getValueDelimiterMatcher();
/*  650 */     this.variableResolver = other.getStringLookup();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkCyclicSubstitution(String varName, List<String> priorVariables) {
/*  660 */     if (!priorVariables.contains(varName)) {
/*      */       return;
/*      */     }
/*  663 */     TextStringBuilder buf = new TextStringBuilder(256);
/*  664 */     buf.append("Infinite loop in property interpolation of ");
/*  665 */     buf.append(priorVariables.remove(0));
/*  666 */     buf.append(": ");
/*  667 */     buf.appendWithSeparators(priorVariables, "->");
/*  668 */     throw new IllegalStateException(buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public char getEscapeChar() {
/*  678 */     return this.escapeChar;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringLookup getStringLookup() {
/*  687 */     return this.variableResolver;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringMatcher getValueDelimiterMatcher() {
/*  703 */     return this.valueDelimiterMatcher;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringMatcher getVariablePrefixMatcher() {
/*  716 */     return this.prefixMatcher;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringMatcher getVariableSuffixMatcher() {
/*  729 */     return this.suffixMatcher;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDisableSubstitutionInValues() {
/*  754 */     return this.disableSubstitutionInValues;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnableSubstitutionInVariables() {
/*  763 */     return this.enableSubstitutionInVariables;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnableUndefinedVariableException() {
/*  772 */     return this.enableUndefinedVariableException;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPreserveEscapes() {
/*  781 */     return this.preserveEscapes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(char[] source) {
/*  793 */     if (source == null) {
/*  794 */       return null;
/*      */     }
/*  796 */     TextStringBuilder buf = (new TextStringBuilder(source.length)).append(source);
/*  797 */     substitute(buf, 0, source.length);
/*  798 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(char[] source, int offset, int length) {
/*  820 */     if (source == null) {
/*  821 */       return null;
/*      */     }
/*  823 */     TextStringBuilder buf = (new TextStringBuilder(length)).append(source, offset, length);
/*  824 */     substitute(buf, 0, length);
/*  825 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(CharSequence source) {
/*  837 */     if (source == null) {
/*  838 */       return null;
/*      */     }
/*  840 */     return replace(source, 0, source.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(CharSequence source, int offset, int length) {
/*  858 */     if (source == null) {
/*  859 */       return null;
/*      */     }
/*  861 */     TextStringBuilder buf = (new TextStringBuilder(length)).append(source.toString(), offset, length);
/*  862 */     substitute(buf, 0, length);
/*  863 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(Object source) {
/*  875 */     if (source == null) {
/*  876 */       return null;
/*      */     }
/*  878 */     TextStringBuilder buf = (new TextStringBuilder()).append(source);
/*  879 */     substitute(buf, 0, buf.length());
/*  880 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(String source) {
/*  892 */     if (source == null) {
/*  893 */       return null;
/*      */     }
/*  895 */     TextStringBuilder buf = new TextStringBuilder(source);
/*  896 */     if (!substitute(buf, 0, source.length())) {
/*  897 */       return source;
/*      */     }
/*  899 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(String source, int offset, int length) {
/*  921 */     if (source == null) {
/*  922 */       return null;
/*      */     }
/*  924 */     TextStringBuilder buf = (new TextStringBuilder(length)).append(source, offset, length);
/*  925 */     if (!substitute(buf, 0, length)) {
/*  926 */       return source.substring(offset, offset + length);
/*      */     }
/*  928 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(StringBuffer source) {
/*  940 */     if (source == null) {
/*  941 */       return null;
/*      */     }
/*  943 */     TextStringBuilder buf = (new TextStringBuilder(source.length())).append(source);
/*  944 */     substitute(buf, 0, buf.length());
/*  945 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(StringBuffer source, int offset, int length) {
/*  963 */     if (source == null) {
/*  964 */       return null;
/*      */     }
/*  966 */     TextStringBuilder buf = (new TextStringBuilder(length)).append(source, offset, length);
/*  967 */     substitute(buf, 0, length);
/*  968 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(TextStringBuilder source) {
/*  980 */     if (source == null) {
/*  981 */       return null;
/*      */     }
/*  983 */     TextStringBuilder builder = (new TextStringBuilder(source.length())).append(source);
/*  984 */     substitute(builder, 0, builder.length());
/*  985 */     return builder.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(TextStringBuilder source, int offset, int length) {
/* 1003 */     if (source == null) {
/* 1004 */       return null;
/*      */     }
/* 1006 */     TextStringBuilder buf = (new TextStringBuilder(length)).append(source, offset, length);
/* 1007 */     substitute(buf, 0, length);
/* 1008 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceIn(StringBuffer source) {
/* 1019 */     if (source == null) {
/* 1020 */       return false;
/*      */     }
/* 1022 */     return replaceIn(source, 0, source.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceIn(StringBuffer source, int offset, int length) {
/* 1040 */     if (source == null) {
/* 1041 */       return false;
/*      */     }
/* 1043 */     TextStringBuilder buf = (new TextStringBuilder(length)).append(source, offset, length);
/* 1044 */     if (!substitute(buf, 0, length)) {
/* 1045 */       return false;
/*      */     }
/* 1047 */     source.replace(offset, offset + length, buf.toString());
/* 1048 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceIn(StringBuilder source) {
/* 1059 */     if (source == null) {
/* 1060 */       return false;
/*      */     }
/* 1062 */     return replaceIn(source, 0, source.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceIn(StringBuilder source, int offset, int length) {
/* 1080 */     if (source == null) {
/* 1081 */       return false;
/*      */     }
/* 1083 */     TextStringBuilder buf = (new TextStringBuilder(length)).append(source, offset, length);
/* 1084 */     if (!substitute(buf, 0, length)) {
/* 1085 */       return false;
/*      */     }
/* 1087 */     source.replace(offset, offset + length, buf.toString());
/* 1088 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceIn(TextStringBuilder source) {
/* 1100 */     if (source == null) {
/* 1101 */       return false;
/*      */     }
/* 1103 */     return substitute(source, 0, source.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceIn(TextStringBuilder source, int offset, int length) {
/* 1121 */     if (source == null) {
/* 1122 */       return false;
/*      */     }
/* 1124 */     return substitute(source, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String resolveVariable(String variableName, TextStringBuilder buf, int startPos, int endPos) {
/* 1147 */     StringLookup resolver = getStringLookup();
/* 1148 */     if (resolver == null) {
/* 1149 */       return null;
/*      */     }
/* 1151 */     return resolver.lookup(variableName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringSubstitutor setDisableSubstitutionInValues(boolean disableSubstitutionInValues) {
/* 1161 */     this.disableSubstitutionInValues = disableSubstitutionInValues;
/* 1162 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringSubstitutor setEnableSubstitutionInVariables(boolean enableSubstitutionInVariables) {
/* 1174 */     this.enableSubstitutionInVariables = enableSubstitutionInVariables;
/* 1175 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringSubstitutor setEnableUndefinedVariableException(boolean failOnUndefinedVariable) {
/* 1185 */     this.enableUndefinedVariableException = failOnUndefinedVariable;
/* 1186 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringSubstitutor setEscapeChar(char escapeCharacter) {
/* 1197 */     this.escapeChar = escapeCharacter;
/* 1198 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringSubstitutor setPreserveEscapes(boolean preserveEscapes) {
/* 1211 */     this.preserveEscapes = preserveEscapes;
/* 1212 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringSubstitutor setValueDelimiter(char valueDelimiter) {
/* 1226 */     return setValueDelimiterMatcher(StringMatcherFactory.INSTANCE.charMatcher(valueDelimiter));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringSubstitutor setValueDelimiter(String valueDelimiter) {
/* 1244 */     if (valueDelimiter == null || valueDelimiter.isEmpty()) {
/* 1245 */       setValueDelimiterMatcher(null);
/* 1246 */       return this;
/*      */     } 
/* 1248 */     return setValueDelimiterMatcher(StringMatcherFactory.INSTANCE.stringMatcher(valueDelimiter));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringSubstitutor setValueDelimiterMatcher(StringMatcher valueDelimiterMatcher) {
/* 1266 */     this.valueDelimiterMatcher = valueDelimiterMatcher;
/* 1267 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringSubstitutor setVariablePrefix(char prefix) {
/* 1281 */     return setVariablePrefixMatcher(StringMatcherFactory.INSTANCE.charMatcher(prefix));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringSubstitutor setVariablePrefix(String prefix) {
/* 1296 */     Validate.isTrue((prefix != null), "Variable prefix must not be null!", new Object[0]);
/* 1297 */     return setVariablePrefixMatcher(StringMatcherFactory.INSTANCE.stringMatcher(prefix));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringSubstitutor setVariablePrefixMatcher(StringMatcher prefixMatcher) {
/* 1312 */     Validate.isTrue((prefixMatcher != null), "Variable prefix matcher must not be null!", new Object[0]);
/* 1313 */     this.prefixMatcher = prefixMatcher;
/* 1314 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringSubstitutor setVariableResolver(StringLookup variableResolver) {
/* 1324 */     this.variableResolver = variableResolver;
/* 1325 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringSubstitutor setVariableSuffix(char suffix) {
/* 1339 */     return setVariableSuffixMatcher(StringMatcherFactory.INSTANCE.charMatcher(suffix));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringSubstitutor setVariableSuffix(String suffix) {
/* 1354 */     Validate.isTrue((suffix != null), "Variable suffix must not be null!", new Object[0]);
/* 1355 */     return setVariableSuffixMatcher(StringMatcherFactory.INSTANCE.stringMatcher(suffix));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringSubstitutor setVariableSuffixMatcher(StringMatcher suffixMatcher) {
/* 1370 */     Validate.isTrue((suffixMatcher != null), "Variable suffix matcher must not be null!", new Object[0]);
/* 1371 */     this.suffixMatcher = suffixMatcher;
/* 1372 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean substitute(TextStringBuilder builder, int offset, int length) {
/* 1392 */     return (substitute(builder, offset, length, null)).altered;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Result substitute(TextStringBuilder builder, int offset, int length, List<String> priorVariables) {
/* 1409 */     Objects.requireNonNull(builder, "builder");
/* 1410 */     StringMatcher prefixMatcher = getVariablePrefixMatcher();
/* 1411 */     StringMatcher suffixMatcher = getVariableSuffixMatcher();
/* 1412 */     char escapeCh = getEscapeChar();
/* 1413 */     StringMatcher valueDelimMatcher = getValueDelimiterMatcher();
/* 1414 */     boolean substitutionInVariablesEnabled = isEnableSubstitutionInVariables();
/* 1415 */     boolean substitutionInValuesDisabled = isDisableSubstitutionInValues();
/* 1416 */     boolean undefinedVariableException = isEnableUndefinedVariableException();
/* 1417 */     boolean preserveEscapes = isPreserveEscapes();
/*      */     
/* 1419 */     boolean altered = false;
/* 1420 */     int lengthChange = 0;
/* 1421 */     int bufEnd = offset + length;
/* 1422 */     int pos = offset;
/* 1423 */     int escPos = -1;
/* 1424 */     while (pos < bufEnd) {
/* 1425 */       int startMatchLen = prefixMatcher.isMatch(builder, pos, offset, bufEnd);
/* 1426 */       if (startMatchLen == 0) {
/* 1427 */         pos++;
/*      */         continue;
/*      */       } 
/* 1430 */       if (pos > offset && builder.charAt(pos - 1) == escapeCh) {
/*      */         
/* 1432 */         if (preserveEscapes) {
/*      */           
/* 1434 */           pos++;
/*      */           
/*      */           continue;
/*      */         } 
/* 1438 */         escPos = pos - 1;
/*      */       } 
/*      */       
/* 1441 */       int startPos = pos;
/* 1442 */       pos += startMatchLen;
/* 1443 */       int endMatchLen = 0;
/* 1444 */       int nestedVarCount = 0;
/* 1445 */       while (pos < bufEnd) {
/* 1446 */         if (substitutionInVariablesEnabled && prefixMatcher.isMatch(builder, pos, offset, bufEnd) != 0) {
/*      */           
/* 1448 */           endMatchLen = prefixMatcher.isMatch(builder, pos, offset, bufEnd);
/* 1449 */           nestedVarCount++;
/* 1450 */           pos += endMatchLen;
/*      */           
/*      */           continue;
/*      */         } 
/* 1454 */         endMatchLen = suffixMatcher.isMatch(builder, pos, offset, bufEnd);
/* 1455 */         if (endMatchLen == 0) {
/* 1456 */           pos++;
/*      */           continue;
/*      */         } 
/* 1459 */         if (nestedVarCount == 0) {
/* 1460 */           if (escPos >= 0) {
/*      */             
/* 1462 */             builder.deleteCharAt(escPos);
/* 1463 */             escPos = -1;
/* 1464 */             lengthChange--;
/* 1465 */             altered = true;
/* 1466 */             bufEnd--;
/* 1467 */             pos = startPos + 1;
/* 1468 */             startPos--;
/*      */             
/*      */             break;
/*      */           } 
/* 1472 */           String varNameExpr = builder.midString(startPos + startMatchLen, pos - startPos - startMatchLen);
/*      */           
/* 1474 */           if (substitutionInVariablesEnabled) {
/* 1475 */             TextStringBuilder bufName = new TextStringBuilder(varNameExpr);
/* 1476 */             substitute(bufName, 0, bufName.length());
/* 1477 */             varNameExpr = bufName.toString();
/*      */           } 
/* 1479 */           pos += endMatchLen;
/* 1480 */           int endPos = pos;
/*      */           
/* 1482 */           String varName = varNameExpr;
/* 1483 */           String varDefaultValue = null;
/*      */           
/* 1485 */           if (valueDelimMatcher != null) {
/* 1486 */             char[] varNameExprChars = varNameExpr.toCharArray();
/* 1487 */             int valueDelimiterMatchLen = 0;
/* 1488 */             for (int i = 0; i < varNameExprChars.length; i++) {
/*      */ 
/*      */               
/* 1491 */               if (!substitutionInVariablesEnabled && prefixMatcher.isMatch(varNameExprChars, i, i, varNameExprChars.length) != 0) {
/*      */                 break;
/*      */               }
/*      */               
/* 1495 */               if (valueDelimMatcher.isMatch(varNameExprChars, i, 0, varNameExprChars.length) != 0) {
/*      */                 
/* 1497 */                 valueDelimiterMatchLen = valueDelimMatcher.isMatch(varNameExprChars, i, 0, varNameExprChars.length);
/*      */                 
/* 1499 */                 varName = varNameExpr.substring(0, i);
/* 1500 */                 varDefaultValue = varNameExpr.substring(i + valueDelimiterMatchLen);
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           
/* 1507 */           if (priorVariables == null) {
/* 1508 */             priorVariables = new ArrayList<>();
/* 1509 */             priorVariables.add(builder.midString(offset, length));
/*      */           } 
/*      */ 
/*      */           
/* 1513 */           checkCyclicSubstitution(varName, priorVariables);
/* 1514 */           priorVariables.add(varName);
/*      */ 
/*      */           
/* 1517 */           String varValue = resolveVariable(varName, builder, startPos, endPos);
/* 1518 */           if (varValue == null) {
/* 1519 */             varValue = varDefaultValue;
/*      */           }
/* 1521 */           if (varValue != null) {
/* 1522 */             int varLen = varValue.length();
/* 1523 */             builder.replace(startPos, endPos, varValue);
/* 1524 */             altered = true;
/* 1525 */             int change = 0;
/* 1526 */             if (!substitutionInValuesDisabled) {
/* 1527 */               change = (substitute(builder, startPos, varLen, priorVariables)).lengthChange;
/*      */             }
/* 1529 */             change = change + varLen - endPos - startPos;
/* 1530 */             pos += change;
/* 1531 */             bufEnd += change;
/* 1532 */             lengthChange += change;
/* 1533 */           } else if (undefinedVariableException) {
/* 1534 */             throw new IllegalArgumentException(
/* 1535 */                 String.format("Cannot resolve variable '%s' (enableSubstitutionInVariables=%s).", new Object[] {
/* 1536 */                     varName, Boolean.valueOf(substitutionInVariablesEnabled)
/*      */                   }));
/*      */           } 
/*      */           
/* 1540 */           priorVariables.remove(priorVariables.size() - 1);
/*      */           break;
/*      */         } 
/* 1543 */         nestedVarCount--;
/* 1544 */         pos += endMatchLen;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1549 */     return new Result(altered, lengthChange);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1560 */     StringBuilder builder = new StringBuilder();
/* 1561 */     builder.append("StringSubstitutor [disableSubstitutionInValues=").append(this.disableSubstitutionInValues).append(", enableSubstitutionInVariables=")
/* 1562 */       .append(this.enableSubstitutionInVariables).append(", enableUndefinedVariableException=").append(this.enableUndefinedVariableException)
/* 1563 */       .append(", escapeChar=").append(this.escapeChar).append(", prefixMatcher=").append(this.prefixMatcher).append(", preserveEscapes=")
/* 1564 */       .append(this.preserveEscapes).append(", suffixMatcher=").append(this.suffixMatcher).append(", valueDelimiterMatcher=").append(this.valueDelimiterMatcher)
/* 1565 */       .append(", variableResolver=").append(this.variableResolver).append("]");
/* 1566 */     return builder.toString();
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\StringSubstitutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */