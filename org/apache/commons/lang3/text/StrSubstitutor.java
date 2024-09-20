/*      */ package org.apache.commons.lang3.text;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Properties;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  158 */   public static final StrMatcher DEFAULT_PREFIX = StrMatcher.stringMatcher("${");
/*      */ 
/*      */ 
/*      */   
/*  162 */   public static final StrMatcher DEFAULT_SUFFIX = StrMatcher.stringMatcher("}");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  167 */   public static final StrMatcher DEFAULT_VALUE_DELIMITER = StrMatcher.stringMatcher(":-");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private char escapeChar;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private StrMatcher prefixMatcher;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private StrMatcher suffixMatcher;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private StrMatcher valueDelimiterMatcher;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private StrLookup<?> variableResolver;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean enableSubstitutionInVariables;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean preserveEscapes;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> String replace(Object source, Map<String, V> valueMap) {
/*  208 */     return (new StrSubstitutor(valueMap)).replace(source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  225 */     return (new StrSubstitutor(valueMap, prefix, suffix)).replace(source);
/*      */   }
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
/*  237 */     if (valueProperties == null) {
/*  238 */       return source.toString();
/*      */     }
/*  240 */     Map<String, String> valueMap = new HashMap<>();
/*  241 */     Enumeration<?> propNames = valueProperties.propertyNames();
/*  242 */     while (propNames.hasMoreElements()) {
/*  243 */       String propName = String.valueOf(propNames.nextElement());
/*  244 */       String propValue = valueProperties.getProperty(propName);
/*  245 */       valueMap.put(propName, propValue);
/*      */     } 
/*  247 */     return replace(source, valueMap);
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
/*  258 */     return (new StrSubstitutor(StrLookup.systemPropertiesLookup())).replace(source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor() {
/*  266 */     this((StrLookup<?>)null, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
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
/*  277 */     this(StrLookup.mapLookup(valueMap), DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
/*      */   }
/*      */ 
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
/*  290 */     this(StrLookup.mapLookup(valueMap), prefix, suffix, '$');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
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
/*  305 */     this(StrLookup.mapLookup(valueMap), prefix, suffix, escape);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  322 */     this(StrLookup.mapLookup(valueMap), prefix, suffix, escape, valueDelimiter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(StrLookup<?> variableResolver) {
/*  331 */     this(variableResolver, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
/*      */   }
/*      */ 
/*      */ 
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
/*  345 */     setVariableResolver(variableResolver);
/*  346 */     setVariablePrefix(prefix);
/*  347 */     setVariableSuffix(suffix);
/*  348 */     setEscapeChar(escape);
/*  349 */     setValueDelimiterMatcher(DEFAULT_VALUE_DELIMITER);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  365 */     setVariableResolver(variableResolver);
/*  366 */     setVariablePrefix(prefix);
/*  367 */     setVariableSuffix(suffix);
/*  368 */     setEscapeChar(escape);
/*  369 */     setValueDelimiter(valueDelimiter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
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
/*  384 */     this(variableResolver, prefixMatcher, suffixMatcher, escape, DEFAULT_VALUE_DELIMITER);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  401 */     setVariableResolver(variableResolver);
/*  402 */     setVariablePrefixMatcher(prefixMatcher);
/*  403 */     setVariableSuffixMatcher(suffixMatcher);
/*  404 */     setEscapeChar(escape);
/*  405 */     setValueDelimiterMatcher(valueDelimiterMatcher);
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
/*  416 */     if (source == null) {
/*  417 */       return null;
/*      */     }
/*  419 */     StrBuilder buf = new StrBuilder(source);
/*  420 */     if (!substitute(buf, 0, source.length())) {
/*  421 */       return source;
/*      */     }
/*  423 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  440 */     if (source == null) {
/*  441 */       return null;
/*      */     }
/*  443 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  444 */     if (!substitute(buf, 0, length)) {
/*  445 */       return source.substring(offset, offset + length);
/*      */     }
/*  447 */     return buf.toString();
/*      */   }
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
/*  459 */     if (source == null) {
/*  460 */       return null;
/*      */     }
/*  462 */     StrBuilder buf = (new StrBuilder(source.length)).append(source);
/*  463 */     substitute(buf, 0, source.length);
/*  464 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  482 */     if (source == null) {
/*  483 */       return null;
/*      */     }
/*  485 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  486 */     substitute(buf, 0, length);
/*  487 */     return buf.toString();
/*      */   }
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
/*  499 */     if (source == null) {
/*  500 */       return null;
/*      */     }
/*  502 */     StrBuilder buf = (new StrBuilder(source.length())).append(source);
/*  503 */     substitute(buf, 0, buf.length());
/*  504 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  522 */     if (source == null) {
/*  523 */       return null;
/*      */     }
/*  525 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  526 */     substitute(buf, 0, length);
/*  527 */     return buf.toString();
/*      */   }
/*      */ 
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
/*  540 */     if (source == null) {
/*  541 */       return null;
/*      */     }
/*  543 */     return replace(source, 0, source.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  562 */     if (source == null) {
/*  563 */       return null;
/*      */     }
/*  565 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  566 */     substitute(buf, 0, length);
/*  567 */     return buf.toString();
/*      */   }
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
/*  579 */     if (source == null) {
/*  580 */       return null;
/*      */     }
/*  582 */     StrBuilder buf = (new StrBuilder(source.length())).append(source);
/*  583 */     substitute(buf, 0, buf.length());
/*  584 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  602 */     if (source == null) {
/*  603 */       return null;
/*      */     }
/*  605 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  606 */     substitute(buf, 0, length);
/*  607 */     return buf.toString();
/*      */   }
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
/*  619 */     if (source == null) {
/*  620 */       return null;
/*      */     }
/*  622 */     StrBuilder buf = (new StrBuilder()).append(source);
/*  623 */     substitute(buf, 0, buf.length());
/*  624 */     return buf.toString();
/*      */   }
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
/*  636 */     if (source == null) {
/*  637 */       return false;
/*      */     }
/*  639 */     return replaceIn(source, 0, source.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  657 */     if (source == null) {
/*  658 */       return false;
/*      */     }
/*  660 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  661 */     if (!substitute(buf, 0, length)) {
/*  662 */       return false;
/*      */     }
/*  664 */     source.replace(offset, offset + length, buf.toString());
/*  665 */     return true;
/*      */   }
/*      */ 
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
/*  678 */     if (source == null) {
/*  679 */       return false;
/*      */     }
/*  681 */     return replaceIn(source, 0, source.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  700 */     if (source == null) {
/*  701 */       return false;
/*      */     }
/*  703 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  704 */     if (!substitute(buf, 0, length)) {
/*  705 */       return false;
/*      */     }
/*  707 */     source.replace(offset, offset + length, buf.toString());
/*  708 */     return true;
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
/*  719 */     if (source == null) {
/*  720 */       return false;
/*      */     }
/*  722 */     return substitute(source, 0, source.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  739 */     if (source == null) {
/*  740 */       return false;
/*      */     }
/*  742 */     return substitute(source, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  762 */     return (substitute(buf, offset, length, null) > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  778 */     StrMatcher pfxMatcher = getVariablePrefixMatcher();
/*  779 */     StrMatcher suffMatcher = getVariableSuffixMatcher();
/*  780 */     char escape = getEscapeChar();
/*  781 */     StrMatcher valueDelimMatcher = getValueDelimiterMatcher();
/*  782 */     boolean substitutionInVariablesEnabled = isEnableSubstitutionInVariables();
/*      */     
/*  784 */     boolean top = (priorVariables == null);
/*  785 */     boolean altered = false;
/*  786 */     int lengthChange = 0;
/*  787 */     char[] chars = buf.buffer;
/*  788 */     int bufEnd = offset + length;
/*  789 */     int pos = offset;
/*  790 */     while (pos < bufEnd) {
/*  791 */       int startMatchLen = pfxMatcher.isMatch(chars, pos, offset, bufEnd);
/*      */       
/*  793 */       if (startMatchLen == 0) {
/*  794 */         pos++; continue;
/*      */       } 
/*  796 */       if (pos > offset && chars[pos - 1] == escape) {
/*      */         
/*  798 */         if (this.preserveEscapes) {
/*  799 */           pos++;
/*      */           continue;
/*      */         } 
/*  802 */         buf.deleteCharAt(pos - 1);
/*  803 */         chars = buf.buffer;
/*  804 */         lengthChange--;
/*  805 */         altered = true;
/*  806 */         bufEnd--;
/*      */         continue;
/*      */       } 
/*  809 */       int startPos = pos;
/*  810 */       pos += startMatchLen;
/*      */       
/*  812 */       int nestedVarCount = 0;
/*  813 */       while (pos < bufEnd) {
/*  814 */         int endMatchLen; if (substitutionInVariablesEnabled && (
/*  815 */           endMatchLen = pfxMatcher.isMatch(chars, pos, offset, bufEnd)) != 0) {
/*      */ 
/*      */           
/*  818 */           nestedVarCount++;
/*  819 */           pos += endMatchLen;
/*      */           
/*      */           continue;
/*      */         } 
/*  823 */         endMatchLen = suffMatcher.isMatch(chars, pos, offset, bufEnd);
/*      */         
/*  825 */         if (endMatchLen == 0) {
/*  826 */           pos++;
/*      */           continue;
/*      */         } 
/*  829 */         if (nestedVarCount == 0) {
/*  830 */           String varNameExpr = new String(chars, startPos + startMatchLen, pos - startPos - startMatchLen);
/*      */ 
/*      */           
/*  833 */           if (substitutionInVariablesEnabled) {
/*  834 */             StrBuilder bufName = new StrBuilder(varNameExpr);
/*  835 */             substitute(bufName, 0, bufName.length());
/*  836 */             varNameExpr = bufName.toString();
/*      */           } 
/*  838 */           pos += endMatchLen;
/*  839 */           int endPos = pos;
/*      */           
/*  841 */           String varName = varNameExpr;
/*  842 */           String varDefaultValue = null;
/*      */           
/*  844 */           if (valueDelimMatcher != null) {
/*  845 */             char[] varNameExprChars = varNameExpr.toCharArray();
/*      */             
/*  847 */             for (int i = 0; i < varNameExprChars.length; i++) {
/*      */               
/*  849 */               if (!substitutionInVariablesEnabled && pfxMatcher
/*  850 */                 .isMatch(varNameExprChars, i, i, varNameExprChars.length) != 0)
/*      */                 break; 
/*      */               int valueDelimiterMatchLen;
/*  853 */               if ((valueDelimiterMatchLen = valueDelimMatcher.isMatch(varNameExprChars, i)) != 0) {
/*  854 */                 varName = varNameExpr.substring(0, i);
/*  855 */                 varDefaultValue = varNameExpr.substring(i + valueDelimiterMatchLen);
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           
/*  862 */           if (priorVariables == null) {
/*  863 */             priorVariables = new ArrayList<>();
/*  864 */             priorVariables.add(new String(chars, offset, length));
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  869 */           checkCyclicSubstitution(varName, priorVariables);
/*  870 */           priorVariables.add(varName);
/*      */ 
/*      */           
/*  873 */           String varValue = resolveVariable(varName, buf, startPos, endPos);
/*      */           
/*  875 */           if (varValue == null) {
/*  876 */             varValue = varDefaultValue;
/*      */           }
/*  878 */           if (varValue != null) {
/*      */             
/*  880 */             int varLen = varValue.length();
/*  881 */             buf.replace(startPos, endPos, varValue);
/*  882 */             altered = true;
/*  883 */             int change = substitute(buf, startPos, varLen, priorVariables);
/*      */             
/*  885 */             change = change + varLen - endPos - startPos;
/*      */             
/*  887 */             pos += change;
/*  888 */             bufEnd += change;
/*  889 */             lengthChange += change;
/*  890 */             chars = buf.buffer;
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  895 */           priorVariables
/*  896 */             .remove(priorVariables.size() - 1);
/*      */           break;
/*      */         } 
/*  899 */         nestedVarCount--;
/*  900 */         pos += endMatchLen;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  905 */     if (top) {
/*  906 */       return altered ? 1 : 0;
/*      */     }
/*  908 */     return lengthChange;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkCyclicSubstitution(String varName, List<String> priorVariables) {
/*  918 */     if (!priorVariables.contains(varName)) {
/*      */       return;
/*      */     }
/*  921 */     StrBuilder buf = new StrBuilder(256);
/*  922 */     buf.append("Infinite loop in property interpolation of ");
/*  923 */     buf.append(priorVariables.remove(0));
/*  924 */     buf.append(": ");
/*  925 */     buf.appendWithSeparators(priorVariables, "->");
/*  926 */     throw new IllegalStateException(buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  949 */     StrLookup<?> resolver = getVariableResolver();
/*  950 */     if (resolver == null) {
/*  951 */       return null;
/*      */     }
/*  953 */     return resolver.lookup(variableName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public char getEscapeChar() {
/*  962 */     return this.escapeChar;
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
/*  973 */     this.escapeChar = escapeCharacter;
/*      */   }
/*      */ 
/*      */ 
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
/*  987 */     return this.prefixMatcher;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 1003 */     this.prefixMatcher = Objects.<StrMatcher>requireNonNull(prefixMatcher, "prefixMatcher");
/* 1004 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
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
/* 1019 */     return setVariablePrefixMatcher(StrMatcher.charMatcher(prefix));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
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
/* 1034 */     return setVariablePrefixMatcher(StrMatcher.stringMatcher(Objects.<String>requireNonNull(prefix)));
/*      */   }
/*      */ 
/*      */ 
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
/* 1048 */     return this.suffixMatcher;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 1064 */     this.suffixMatcher = Objects.<StrMatcher>requireNonNull(suffixMatcher);
/* 1065 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
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
/* 1080 */     return setVariableSuffixMatcher(StrMatcher.charMatcher(suffix));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
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
/* 1095 */     return setVariableSuffixMatcher(StrMatcher.stringMatcher(Objects.<String>requireNonNull(suffix)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 1113 */     return this.valueDelimiterMatcher;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 1133 */     this.valueDelimiterMatcher = valueDelimiterMatcher;
/* 1134 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 1150 */     return setValueDelimiterMatcher(StrMatcher.charMatcher(valueDelimiter));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 1170 */     if (StringUtils.isEmpty(valueDelimiter)) {
/* 1171 */       setValueDelimiterMatcher(null);
/* 1172 */       return this;
/*      */     } 
/* 1174 */     return setValueDelimiterMatcher(StrMatcher.stringMatcher(valueDelimiter));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrLookup<?> getVariableResolver() {
/* 1183 */     return this.variableResolver;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVariableResolver(StrLookup<?> variableResolver) {
/* 1192 */     this.variableResolver = variableResolver;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnableSubstitutionInVariables() {
/* 1202 */     return this.enableSubstitutionInVariables;
/*      */   }
/*      */ 
/*      */ 
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
/* 1216 */     this.enableSubstitutionInVariables = enableSubstitutionInVariables;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPreserveEscapes() {
/* 1227 */     return this.preserveEscapes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 1243 */     this.preserveEscapes = preserveEscapes;
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\text\StrSubstitutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */