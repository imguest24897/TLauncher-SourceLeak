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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */ public class StrSubstitutor
/*      */ {
/*      */   public static final char DEFAULT_ESCAPE = '$';
/*  137 */   public static final StrMatcher DEFAULT_PREFIX = StrMatcher.stringMatcher("${");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  142 */   public static final StrMatcher DEFAULT_SUFFIX = StrMatcher.stringMatcher("}");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  147 */   public static final StrMatcher DEFAULT_VALUE_DELIMITER = StrMatcher.stringMatcher(":-");
/*      */   
/*      */   private char escapeChar;
/*      */   private StrMatcher prefixMatcher;
/*      */   private StrMatcher suffixMatcher;
/*      */   private StrMatcher valueDelimiterMatcher;
/*      */   private StrLookup<?> variableResolver;
/*      */   private boolean enableSubstitutionInVariables;
/*      */   private boolean preserveEscapes;
/*      */   private boolean disableSubstitutionInValues;
/*      */   
/*      */   public static <V> String replace(Object source, Map<String, V> valueMap) {
/*  159 */     return (new StrSubstitutor(valueMap)).replace(source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  179 */     return (new StrSubstitutor(valueMap, prefix, suffix)).replace(source);
/*      */   }
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
/*  191 */     if (valueProperties == null) {
/*  192 */       return source.toString();
/*      */     }
/*      */     
/*  195 */     Objects.requireNonNull(valueProperties); return replace(source, (Map<String, ?>)valueProperties.stringPropertyNames().stream().collect(Collectors.toMap(Function.identity(), valueProperties::getProperty)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String replaceSystemProperties(Object source) {
/*  206 */     return (new StrSubstitutor(StrLookup.systemPropertiesLookup())).replace(source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor() {
/*  254 */     this((StrLookup)null, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <V> StrSubstitutor(Map<String, V> valueMap) {
/*  265 */     this(StrLookup.mapLookup(valueMap), DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <V> StrSubstitutor(Map<String, V> valueMap, String prefix, String suffix) {
/*  278 */     this(StrLookup.mapLookup(valueMap), prefix, suffix, '$');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <V> StrSubstitutor(Map<String, V> valueMap, String prefix, String suffix, char escape) {
/*  293 */     this(StrLookup.mapLookup(valueMap), prefix, suffix, escape);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <V> StrSubstitutor(Map<String, V> valueMap, String prefix, String suffix, char escape, String valueDelimiter) {
/*  309 */     this(StrLookup.mapLookup(valueMap), prefix, suffix, escape, valueDelimiter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(StrLookup<?> variableResolver) {
/*  318 */     this(variableResolver, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(StrLookup<?> variableResolver, String prefix, String suffix, char escape) {
/*  332 */     setVariableResolver(variableResolver);
/*  333 */     setVariablePrefix(prefix);
/*  334 */     setVariableSuffix(suffix);
/*  335 */     setEscapeChar(escape);
/*  336 */     setValueDelimiterMatcher(DEFAULT_VALUE_DELIMITER);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(StrLookup<?> variableResolver, String prefix, String suffix, char escape, String valueDelimiter) {
/*  351 */     setVariableResolver(variableResolver);
/*  352 */     setVariablePrefix(prefix);
/*  353 */     setVariableSuffix(suffix);
/*  354 */     setEscapeChar(escape);
/*  355 */     setValueDelimiter(valueDelimiter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(StrLookup<?> variableResolver, StrMatcher prefixMatcher, StrMatcher suffixMatcher, char escape) {
/*  370 */     this(variableResolver, prefixMatcher, suffixMatcher, escape, DEFAULT_VALUE_DELIMITER);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(StrLookup<?> variableResolver, StrMatcher prefixMatcher, StrMatcher suffixMatcher, char escape, StrMatcher valueDelimiterMatcher) {
/*  386 */     setVariableResolver(variableResolver);
/*  387 */     setVariablePrefixMatcher(prefixMatcher);
/*  388 */     setVariableSuffixMatcher(suffixMatcher);
/*  389 */     setEscapeChar(escape);
/*  390 */     setValueDelimiterMatcher(valueDelimiterMatcher);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkCyclicSubstitution(String varName, List<String> priorVariables) {
/*  400 */     if (!priorVariables.contains(varName)) {
/*      */       return;
/*      */     }
/*  403 */     StrBuilder buf = new StrBuilder(256);
/*  404 */     buf.append("Infinite loop in property interpolation of ");
/*  405 */     buf.append(priorVariables.remove(0));
/*  406 */     buf.append(": ");
/*  407 */     buf.appendWithSeparators(priorVariables, "->");
/*  408 */     throw new IllegalStateException(buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public char getEscapeChar() {
/*  417 */     return this.escapeChar;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrMatcher getValueDelimiterMatcher() {
/*  434 */     return this.valueDelimiterMatcher;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrMatcher getVariablePrefixMatcher() {
/*  448 */     return this.prefixMatcher;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrLookup<?> getVariableResolver() {
/*  457 */     return this.variableResolver;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrMatcher getVariableSuffixMatcher() {
/*  471 */     return this.suffixMatcher;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  496 */     return this.disableSubstitutionInValues;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnableSubstitutionInVariables() {
/*  505 */     return this.enableSubstitutionInVariables;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPreserveEscapes() {
/*  515 */     return this.preserveEscapes;
/*      */   }
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
/*  527 */     if (source == null) {
/*  528 */       return null;
/*      */     }
/*  530 */     StrBuilder buf = (new StrBuilder(source.length)).append(source);
/*  531 */     substitute(buf, 0, source.length);
/*  532 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  550 */     if (source == null) {
/*  551 */       return null;
/*      */     }
/*  553 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  554 */     substitute(buf, 0, length);
/*  555 */     return buf.toString();
/*      */   }
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
/*  567 */     if (source == null) {
/*  568 */       return null;
/*      */     }
/*  570 */     return replace(source, 0, source.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  588 */     if (source == null) {
/*  589 */       return null;
/*      */     }
/*  591 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  592 */     substitute(buf, 0, length);
/*  593 */     return buf.toString();
/*      */   }
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
/*  605 */     if (source == null) {
/*  606 */       return null;
/*      */     }
/*  608 */     StrBuilder buf = (new StrBuilder()).append(source);
/*  609 */     substitute(buf, 0, buf.length());
/*  610 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(StrBuilder source) {
/*  622 */     if (source == null) {
/*  623 */       return null;
/*      */     }
/*  625 */     StrBuilder buf = (new StrBuilder(source.length())).append(source);
/*  626 */     substitute(buf, 0, buf.length());
/*  627 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(StrBuilder source, int offset, int length) {
/*  645 */     if (source == null) {
/*  646 */       return null;
/*      */     }
/*  648 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  649 */     substitute(buf, 0, length);
/*  650 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(String source) {
/*  661 */     if (source == null) {
/*  662 */       return null;
/*      */     }
/*  664 */     StrBuilder buf = new StrBuilder(source);
/*  665 */     if (!substitute(buf, 0, source.length())) {
/*  666 */       return source;
/*      */     }
/*  668 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  685 */     if (source == null) {
/*  686 */       return null;
/*      */     }
/*  688 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  689 */     if (!substitute(buf, 0, length)) {
/*  690 */       return source.substring(offset, offset + length);
/*      */     }
/*  692 */     return buf.toString();
/*      */   }
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
/*  704 */     if (source == null) {
/*  705 */       return null;
/*      */     }
/*  707 */     StrBuilder buf = (new StrBuilder(source.length())).append(source);
/*  708 */     substitute(buf, 0, buf.length());
/*  709 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  727 */     if (source == null) {
/*  728 */       return null;
/*      */     }
/*  730 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  731 */     substitute(buf, 0, length);
/*  732 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceIn(StrBuilder source) {
/*  743 */     if (source == null) {
/*  744 */       return false;
/*      */     }
/*  746 */     return substitute(source, 0, source.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceIn(StrBuilder source, int offset, int length) {
/*  763 */     if (source == null) {
/*  764 */       return false;
/*      */     }
/*  766 */     return substitute(source, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceIn(StringBuffer source) {
/*  778 */     if (source == null) {
/*  779 */       return false;
/*      */     }
/*  781 */     return replaceIn(source, 0, source.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  799 */     if (source == null) {
/*  800 */       return false;
/*      */     }
/*  802 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  803 */     if (!substitute(buf, 0, length)) {
/*  804 */       return false;
/*      */     }
/*  806 */     source.replace(offset, offset + length, buf.toString());
/*  807 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceIn(StringBuilder source) {
/*  819 */     if (source == null) {
/*  820 */       return false;
/*      */     }
/*  822 */     return replaceIn(source, 0, source.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  840 */     if (source == null) {
/*  841 */       return false;
/*      */     }
/*  843 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  844 */     if (!substitute(buf, 0, length)) {
/*  845 */       return false;
/*      */     }
/*  847 */     source.replace(offset, offset + length, buf.toString());
/*  848 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String resolveVariable(String variableName, StrBuilder buf, int startPos, int endPos) {
/*  874 */     StrLookup<?> resolver = getVariableResolver();
/*  875 */     if (resolver == null) {
/*  876 */       return null;
/*      */     }
/*  878 */     return resolver.lookup(variableName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDisableSubstitutionInValues(boolean disableSubstitutionInValues) {
/*  889 */     this.disableSubstitutionInValues = disableSubstitutionInValues;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEnableSubstitutionInVariables(boolean enableSubstitutionInVariables) {
/*  902 */     this.enableSubstitutionInVariables = enableSubstitutionInVariables;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEscapeChar(char escapeCharacter) {
/*  913 */     this.escapeChar = escapeCharacter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPreserveEscapes(boolean preserveEscapes) {
/*  928 */     this.preserveEscapes = preserveEscapes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setValueDelimiter(char valueDelimiter) {
/*  943 */     return setValueDelimiterMatcher(StrMatcher.charMatcher(valueDelimiter));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setValueDelimiter(String valueDelimiter) {
/*  962 */     if (valueDelimiter == null || valueDelimiter.isEmpty()) {
/*  963 */       setValueDelimiterMatcher(null);
/*  964 */       return this;
/*      */     } 
/*  966 */     return setValueDelimiterMatcher(StrMatcher.stringMatcher(valueDelimiter));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setValueDelimiterMatcher(StrMatcher valueDelimiterMatcher) {
/*  985 */     this.valueDelimiterMatcher = valueDelimiterMatcher;
/*  986 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setVariablePrefix(char prefix) {
/* 1001 */     return setVariablePrefixMatcher(StrMatcher.charMatcher(prefix));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setVariablePrefix(String prefix) {
/* 1016 */     Validate.isTrue((prefix != null), "Variable prefix must not be null!", new Object[0]);
/* 1017 */     return setVariablePrefixMatcher(StrMatcher.stringMatcher(prefix));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setVariablePrefixMatcher(StrMatcher prefixMatcher) {
/* 1033 */     Validate.isTrue((prefixMatcher != null), "Variable prefix matcher must not be null!", new Object[0]);
/* 1034 */     this.prefixMatcher = prefixMatcher;
/* 1035 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVariableResolver(StrLookup<?> variableResolver) {
/* 1044 */     this.variableResolver = variableResolver;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setVariableSuffix(char suffix) {
/* 1059 */     return setVariableSuffixMatcher(StrMatcher.charMatcher(suffix));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setVariableSuffix(String suffix) {
/* 1074 */     Validate.isTrue((suffix != null), "Variable suffix must not be null!", new Object[0]);
/* 1075 */     return setVariableSuffixMatcher(StrMatcher.stringMatcher(suffix));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setVariableSuffixMatcher(StrMatcher suffixMatcher) {
/* 1091 */     Validate.isTrue((suffixMatcher != null), "Variable suffix matcher must not be null!", new Object[0]);
/* 1092 */     this.suffixMatcher = suffixMatcher;
/* 1093 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean substitute(StrBuilder buf, int offset, int length) {
/* 1113 */     return (substitute(buf, offset, length, null) > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int substitute(StrBuilder buf, int offset, int length, List<String> priorVariables) {
/* 1129 */     StrMatcher pfxMatcher = getVariablePrefixMatcher();
/* 1130 */     StrMatcher suffMatcher = getVariableSuffixMatcher();
/* 1131 */     char escape = getEscapeChar();
/* 1132 */     StrMatcher valueDelimMatcher = getValueDelimiterMatcher();
/* 1133 */     boolean substitutionInVariablesEnabled = isEnableSubstitutionInVariables();
/* 1134 */     boolean substitutionInValuesDisabled = isDisableSubstitutionInValues();
/*      */     
/* 1136 */     boolean top = (priorVariables == null);
/* 1137 */     boolean altered = false;
/* 1138 */     int lengthChange = 0;
/* 1139 */     char[] chars = buf.buffer;
/* 1140 */     int bufEnd = offset + length;
/* 1141 */     int pos = offset;
/* 1142 */     while (pos < bufEnd) {
/* 1143 */       int startMatchLen = pfxMatcher.isMatch(chars, pos, offset, bufEnd);
/*      */       
/* 1145 */       if (startMatchLen == 0) {
/* 1146 */         pos++;
/*      */         continue;
/*      */       } 
/* 1149 */       if (pos > offset && chars[pos - 1] == escape) {
/*      */         
/* 1151 */         if (this.preserveEscapes) {
/* 1152 */           pos++;
/*      */           continue;
/*      */         } 
/* 1155 */         buf.deleteCharAt(pos - 1);
/* 1156 */         chars = buf.buffer;
/* 1157 */         lengthChange--;
/* 1158 */         altered = true;
/* 1159 */         bufEnd--;
/*      */         continue;
/*      */       } 
/* 1162 */       int startPos = pos;
/* 1163 */       pos += startMatchLen;
/* 1164 */       int endMatchLen = 0;
/* 1165 */       int nestedVarCount = 0;
/* 1166 */       while (pos < bufEnd) {
/* 1167 */         if (substitutionInVariablesEnabled && pfxMatcher
/* 1168 */           .isMatch(chars, pos, offset, bufEnd) != 0) {
/*      */ 
/*      */           
/* 1171 */           endMatchLen = pfxMatcher.isMatch(chars, pos, offset, bufEnd);
/*      */           
/* 1173 */           nestedVarCount++;
/* 1174 */           pos += endMatchLen;
/*      */           
/*      */           continue;
/*      */         } 
/* 1178 */         endMatchLen = suffMatcher.isMatch(chars, pos, offset, bufEnd);
/*      */         
/* 1180 */         if (endMatchLen == 0) {
/* 1181 */           pos++;
/*      */           continue;
/*      */         } 
/* 1184 */         if (nestedVarCount == 0) {
/* 1185 */           String varNameExpr = new String(chars, startPos + startMatchLen, pos - startPos - startMatchLen);
/*      */ 
/*      */           
/* 1188 */           if (substitutionInVariablesEnabled) {
/* 1189 */             StrBuilder bufName = new StrBuilder(varNameExpr);
/* 1190 */             substitute(bufName, 0, bufName.length());
/* 1191 */             varNameExpr = bufName.toString();
/*      */           } 
/* 1193 */           pos += endMatchLen;
/* 1194 */           int endPos = pos;
/*      */           
/* 1196 */           String varName = varNameExpr;
/* 1197 */           String varDefaultValue = null;
/*      */           
/* 1199 */           if (valueDelimMatcher != null) {
/* 1200 */             char[] varNameExprChars = varNameExpr.toCharArray();
/* 1201 */             int valueDelimiterMatchLen = 0;
/* 1202 */             for (int i = 0; i < varNameExprChars.length; i++) {
/*      */ 
/*      */               
/* 1205 */               if (!substitutionInVariablesEnabled && pfxMatcher
/* 1206 */                 .isMatch(varNameExprChars, i, i, varNameExprChars.length) != 0) {
/*      */                 break;
/*      */               }
/*      */ 
/*      */ 
/*      */               
/* 1212 */               if (valueDelimMatcher.isMatch(varNameExprChars, i) != 0) {
/* 1213 */                 valueDelimiterMatchLen = valueDelimMatcher.isMatch(varNameExprChars, i);
/* 1214 */                 varName = varNameExpr.substring(0, i);
/* 1215 */                 varDefaultValue = varNameExpr.substring(i + valueDelimiterMatchLen);
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           
/* 1222 */           if (priorVariables == null) {
/* 1223 */             priorVariables = new ArrayList<>();
/* 1224 */             priorVariables.add(new String(chars, offset, length));
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/* 1229 */           checkCyclicSubstitution(varName, priorVariables);
/* 1230 */           priorVariables.add(varName);
/*      */ 
/*      */           
/* 1233 */           String varValue = resolveVariable(varName, buf, startPos, endPos);
/*      */           
/* 1235 */           if (varValue == null) {
/* 1236 */             varValue = varDefaultValue;
/*      */           }
/* 1238 */           if (varValue != null) {
/* 1239 */             int varLen = varValue.length();
/* 1240 */             buf.replace(startPos, endPos, varValue);
/* 1241 */             altered = true;
/* 1242 */             int change = 0;
/* 1243 */             if (!substitutionInValuesDisabled) {
/* 1244 */               change = substitute(buf, startPos, varLen, priorVariables);
/*      */             }
/*      */             
/* 1247 */             change = change + varLen - endPos - startPos;
/*      */             
/* 1249 */             pos += change;
/* 1250 */             bufEnd += change;
/* 1251 */             lengthChange += change;
/* 1252 */             chars = buf.buffer;
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/* 1257 */           priorVariables
/* 1258 */             .remove(priorVariables.size() - 1);
/*      */           break;
/*      */         } 
/* 1261 */         nestedVarCount--;
/* 1262 */         pos += endMatchLen;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1268 */     if (top) {
/* 1269 */       return altered ? 1 : 0;
/*      */     }
/* 1271 */     return lengthChange;
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\StrSubstitutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */