/*     */ package com.beust.jcommander;
/*     */ 
/*     */ import com.beust.jcommander.internal.Lists;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.ResourceBundle;
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
/*     */ public class DefaultUsageFormatter
/*     */   implements IUsageFormatter
/*     */ {
/*     */   private final JCommander commander;
/*     */   
/*     */   public DefaultUsageFormatter(JCommander commander) {
/*  34 */     this.commander = commander;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void usage(String commandName) {
/*  41 */     StringBuilder sb = new StringBuilder();
/*  42 */     usage(commandName, sb);
/*  43 */     this.commander.getConsole().println(sb.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void usage(String commandName, StringBuilder out) {
/*  50 */     usage(commandName, out, "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void usage(StringBuilder out) {
/*  57 */     usage(out, "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void usage(String commandName, StringBuilder out, String indent) {
/*  65 */     String description = getCommandDescription(commandName);
/*  66 */     JCommander jc = this.commander.findCommandByAlias(commandName);
/*     */     
/*  68 */     if (description != null) {
/*  69 */       out.append(indent).append(description);
/*  70 */       out.append("\n");
/*     */     } 
/*  72 */     jc.getUsageFormatter().usage(out, indent);
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
/*     */   
/*     */   public void usage(StringBuilder out, String indent) {
/*  87 */     if (this.commander.getDescriptions() == null) {
/*  88 */       this.commander.createDescriptions();
/*     */     }
/*  90 */     boolean hasCommands = !this.commander.getCommands().isEmpty();
/*  91 */     boolean hasOptions = !this.commander.getDescriptions().isEmpty();
/*     */ 
/*     */     
/*  94 */     int descriptionIndent = 6;
/*  95 */     int indentCount = indent.length() + 6;
/*     */ 
/*     */     
/*  98 */     appendMainLine(out, hasOptions, hasCommands, indentCount, indent);
/*     */ 
/*     */     
/* 101 */     int longestName = 0;
/* 102 */     List<ParameterDescription> sortedParameters = Lists.newArrayList();
/*     */     
/* 104 */     for (ParameterDescription pd : this.commander.getFields().values()) {
/* 105 */       if (!pd.getParameter().hidden()) {
/* 106 */         sortedParameters.add(pd);
/*     */         
/* 108 */         int length = pd.getNames().length() + 2;
/*     */         
/* 110 */         if (length > longestName) {
/* 111 */           longestName = length;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 117 */     sortedParameters.sort(this.commander.getParameterDescriptionComparator());
/*     */ 
/*     */     
/* 120 */     appendAllParametersDetails(out, indentCount, indent, sortedParameters);
/*     */ 
/*     */     
/* 123 */     if (hasCommands) {
/* 124 */       appendCommands(out, indentCount, 6, indent);
/*     */     }
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
/*     */ 
/*     */   
/*     */   public void appendMainLine(StringBuilder out, boolean hasOptions, boolean hasCommands, int indentCount, String indent) {
/* 141 */     String programName = (this.commander.getProgramDisplayName() != null) ? this.commander.getProgramDisplayName() : "<main class>";
/* 142 */     StringBuilder mainLine = new StringBuilder();
/* 143 */     mainLine.append(indent).append("Usage: ").append(programName);
/*     */     
/* 145 */     if (hasOptions) {
/* 146 */       mainLine.append(" [options]");
/*     */     }
/*     */     
/* 149 */     if (hasCommands) {
/* 150 */       mainLine.append(indent).append(" [command] [command options]");
/*     */     }
/*     */     
/* 153 */     if (this.commander.getMainParameter() != null && this.commander.getMainParameter().getDescription() != null) {
/* 154 */       mainLine.append(" ").append(this.commander.getMainParameter().getDescription().getDescription());
/*     */     }
/* 156 */     wrapDescription(out, indentCount, mainLine.toString());
/* 157 */     out.append("\n");
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
/* 171 */     if (sortedParameters.size() > 0) {
/* 172 */       out.append(indent).append("  Options:\n");
/*     */     }
/*     */     
/* 175 */     for (ParameterDescription pd : sortedParameters) {
/* 176 */       WrappedParameter parameter = pd.getParameter();
/* 177 */       String description = pd.getDescription();
/* 178 */       boolean hasDescription = !description.isEmpty();
/*     */ 
/*     */       
/* 181 */       out.append(indent)
/* 182 */         .append("  ")
/* 183 */         .append(parameter.required() ? "* " : "  ")
/* 184 */         .append(pd.getNames())
/* 185 */         .append("\n");
/*     */       
/* 187 */       if (hasDescription) {
/* 188 */         wrapDescription(out, indentCount, s(indentCount) + description);
/*     */       }
/* 190 */       Object def = pd.getDefault();
/*     */       
/* 192 */       if (pd.isDynamicParameter()) {
/* 193 */         String syntax = "Syntax: " + parameter.names()[0] + "key" + parameter.getAssignment() + "value";
/*     */         
/* 195 */         if (hasDescription) {
/* 196 */           out.append(newLineAndIndent(indentCount));
/*     */         } else {
/* 198 */           out.append(s(indentCount));
/*     */         } 
/* 200 */         out.append(syntax);
/*     */       } 
/*     */       
/* 203 */       if (def != null && !pd.isHelp()) {
/* 204 */         String displayedDef = Strings.isStringEmpty(def.toString()) ? "<empty string>" : def.toString();
/* 205 */         String defaultText = "Default: " + (parameter.password() ? "********" : displayedDef);
/*     */         
/* 207 */         if (hasDescription) {
/* 208 */           out.append(newLineAndIndent(indentCount));
/*     */         } else {
/* 210 */           out.append(s(indentCount));
/*     */         } 
/* 212 */         out.append(defaultText);
/*     */       } 
/* 214 */       Class<?> type = pd.getParameterized().getType();
/*     */ 
/*     */       
/* 217 */       String valueList = EnumSet.allOf(type).toString();
/* 218 */       String possibleValues = "Possible Values: " + valueList;
/*     */ 
/*     */ 
/*     */       
/* 222 */       if (type.isEnum() && !description.contains("Options: " + valueList)) {
/* 223 */         if (hasDescription) {
/* 224 */           out.append(newLineAndIndent(indentCount));
/*     */         } else {
/* 226 */           out.append(s(indentCount));
/*     */         } 
/* 228 */         out.append(possibleValues);
/*     */       } 
/*     */       
/* 231 */       out.append("\n");
/*     */     } 
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
/*     */   
/*     */   public void appendCommands(StringBuilder out, int indentCount, int descriptionIndent, String indent) {
/* 247 */     out.append(indent + "  Commands:\n");
/*     */ 
/*     */     
/* 250 */     for (Map.Entry<JCommander.ProgramName, JCommander> commands : this.commander.getRawCommands().entrySet()) {
/* 251 */       Object arg = ((JCommander)commands.getValue()).getObjects().get(0);
/* 252 */       Parameters p = arg.getClass().<Parameters>getAnnotation(Parameters.class);
/*     */       
/* 254 */       if (p == null || !p.hidden()) {
/* 255 */         JCommander.ProgramName progName = commands.getKey();
/* 256 */         String dispName = progName.getDisplayName();
/* 257 */         String description = indent + s(4) + dispName + s(6) + getCommandDescription(progName.getName());
/* 258 */         wrapDescription(out, indentCount + descriptionIndent, description);
/* 259 */         out.append("\n");
/*     */ 
/*     */         
/* 262 */         JCommander jc = this.commander.findCommandByAlias(progName.getName());
/* 263 */         jc.getUsageFormatter().usage(out, indent + s(6));
/* 264 */         out.append("\n");
/*     */       } 
/*     */     } 
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
/*     */   public String getCommandDescription(String commandName) {
/* 278 */     JCommander jc = this.commander.findCommandByAlias(commandName);
/*     */     
/* 280 */     if (jc == null) {
/* 281 */       throw new ParameterException("Asking description for unknown command: " + commandName);
/*     */     }
/* 283 */     Object arg = jc.getObjects().get(0);
/* 284 */     Parameters p = arg.getClass().<Parameters>getAnnotation(Parameters.class);
/*     */     
/* 286 */     String result = null;
/*     */     
/* 288 */     if (p != null) {
/* 289 */       ResourceBundle bundle; result = p.commandDescription();
/* 290 */       String bundleName = p.resourceBundle();
/*     */       
/* 292 */       if (!bundleName.isEmpty()) {
/* 293 */         bundle = ResourceBundle.getBundle(bundleName, Locale.getDefault());
/*     */       } else {
/* 295 */         bundle = this.commander.getBundle();
/*     */       } 
/*     */       
/* 298 */       if (bundle != null) {
/* 299 */         String descriptionKey = p.commandDescriptionKey();
/*     */         
/* 301 */         if (!descriptionKey.isEmpty()) {
/* 302 */           result = getI18nString(bundle, descriptionKey, p.commandDescription());
/*     */         }
/*     */       } 
/*     */     } 
/* 306 */     return result;
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
/*     */   
/*     */   public void wrapDescription(StringBuilder out, int indent, int currentLineIndent, String description) {
/* 321 */     int max = this.commander.getColumnSize();
/* 322 */     String[] words = description.split(" ");
/* 323 */     int current = currentLineIndent;
/*     */     
/* 325 */     for (int i = 0; i < words.length; i++) {
/* 326 */       String word = words[i];
/*     */       
/* 328 */       if (word.length() > max || current + 1 + word.length() <= max) {
/* 329 */         out.append(word);
/* 330 */         current += word.length();
/*     */         
/* 332 */         if (i != words.length - 1) {
/* 333 */           out.append(" ");
/* 334 */           current++;
/*     */         } 
/*     */       } else {
/* 337 */         out.append("\n").append(s(indent)).append(word).append(" ");
/* 338 */         current = indent + word.length() + 1;
/*     */       } 
/*     */     } 
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
/*     */   public void wrapDescription(StringBuilder out, int indent, String description) {
/* 354 */     wrapDescription(out, indent, 0, description);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getI18nString(ResourceBundle bundle, String key, String def) {
/* 363 */     String s = (bundle != null) ? bundle.getString(key) : null;
/* 364 */     return (s != null) ? s : def;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String s(int count) {
/* 373 */     StringBuilder result = new StringBuilder();
/*     */     
/* 375 */     for (int i = 0; i < count; i++) {
/* 376 */       result.append(" ");
/*     */     }
/* 378 */     return result.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String newLineAndIndent(int indent) {
/* 387 */     return "\n" + s(indent);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\DefaultUsageFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */