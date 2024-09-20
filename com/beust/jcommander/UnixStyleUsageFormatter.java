/*     */ package com.beust.jcommander;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
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
/*     */ public class UnixStyleUsageFormatter
/*     */   extends DefaultUsageFormatter
/*     */ {
/*     */   public UnixStyleUsageFormatter(JCommander commander) {
/*  32 */     super(commander);
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
/*     */   public void appendAllParametersDetails(StringBuilder out, int indentCount, String indent, List<ParameterDescription> sortedParameters) {
/*  46 */     if (sortedParameters.size() > 0) {
/*  47 */       out.append(indent).append("  Options:\n");
/*     */     }
/*     */ 
/*     */     
/*  51 */     int prefixIndent = 0;
/*     */     
/*  53 */     for (ParameterDescription pd : sortedParameters) {
/*  54 */       WrappedParameter parameter = pd.getParameter();
/*  55 */       String prefix = (parameter.required() ? "* " : "  ") + pd.getNames();
/*     */       
/*  57 */       if (prefix.length() > prefixIndent) {
/*  58 */         prefixIndent = prefix.length();
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  63 */     for (ParameterDescription pd : sortedParameters) {
/*  64 */       WrappedParameter parameter = pd.getParameter();
/*     */       
/*  66 */       String prefix = (parameter.required() ? "* " : "  ") + pd.getNames();
/*  67 */       out.append(indent)
/*  68 */         .append("  ")
/*  69 */         .append(prefix)
/*  70 */         .append(s(prefixIndent - prefix.length()))
/*  71 */         .append(" ");
/*  72 */       int initialLinePrefixLength = indent.length() + prefixIndent + 3;
/*     */ 
/*     */       
/*  75 */       String description = pd.getDescription();
/*  76 */       Object def = pd.getDefault();
/*     */       
/*  78 */       if (pd.isDynamicParameter()) {
/*  79 */         String syntax = "(syntax: " + parameter.names()[0] + "key" + parameter.getAssignment() + "value)";
/*  80 */         description = description + ((description.length() == 0) ? "" : " ") + syntax;
/*     */       } 
/*     */       
/*  83 */       if (def != null && !pd.isHelp()) {
/*  84 */         String displayedDef = Strings.isStringEmpty(def.toString()) ? "<empty string>" : def.toString();
/*  85 */         String defaultText = "(default: " + (parameter.password() ? "********" : displayedDef) + ")";
/*  86 */         description = description + ((description.length() == 0) ? "" : " ") + defaultText;
/*     */       } 
/*  88 */       Class<?> type = pd.getParameterized().getType();
/*     */ 
/*     */       
/*  91 */       String valueList = EnumSet.allOf(type).toString();
/*     */ 
/*     */ 
/*     */       
/*  95 */       if (type.isEnum() && !description.contains("Options: " + valueList)) {
/*  96 */         String possibleValues = "(values: " + valueList + ")";
/*  97 */         description = description + ((description.length() == 0) ? "" : " ") + possibleValues;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 104 */       wrapDescription(out, indentCount + prefixIndent - 3, initialLinePrefixLength, description);
/* 105 */       out.append("\n");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\UnixStyleUsageFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */