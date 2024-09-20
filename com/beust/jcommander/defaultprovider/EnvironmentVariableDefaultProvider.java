/*     */ package com.beust.jcommander.defaultprovider;
/*     */ 
/*     */ import com.beust.jcommander.IDefaultProvider;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public final class EnvironmentVariableDefaultProvider
/*     */   implements IDefaultProvider
/*     */ {
/*     */   private static final String DEFAULT_VARIABLE_NAME = "JCOMMANDER_OPTS";
/*     */   private static final String DEFAULT_PREFIXES_PATTERN = "-/";
/*     */   private final String environmentVariableValue;
/*     */   private final String optionPrefixesPattern;
/*     */   
/*     */   public EnvironmentVariableDefaultProvider() {
/*  55 */     this("JCOMMANDER_OPTS", "-/");
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
/*     */   public EnvironmentVariableDefaultProvider(String environmentVariableName, String optionPrefixes) {
/*  67 */     this(Objects.<String>requireNonNull(environmentVariableName), Objects.<String>requireNonNull(optionPrefixes), System::getenv);
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
/*     */   EnvironmentVariableDefaultProvider(String environmentVariableName, String optionPrefixes, Function<String, String> resolver) {
/*  81 */     this.environmentVariableValue = resolver.apply(environmentVariableName);
/*  82 */     this.optionPrefixesPattern = Objects.<String>requireNonNull(optionPrefixes);
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getDefaultValueFor(String optionName) {
/*  87 */     if (this.environmentVariableValue == null) {
/*  88 */       return null;
/*     */     }
/*     */     
/*  91 */     Matcher matcher = Pattern.compile("(?:(?:.*\\s+)|(?:^))(" + Pattern.quote(optionName) + ")\\s*((?:'[^']*(?='))|(?:\"[^\"]*(?=\"))|(?:[^" + this.optionPrefixesPattern + "\\s]+))?.*").matcher(this.environmentVariableValue);
/*  92 */     if (!matcher.matches())
/*  93 */       return null; 
/*  94 */     String value = matcher.group(2);
/*  95 */     if (value == null)
/*  96 */       return "true"; 
/*  97 */     char firstCharacter = value.charAt(0);
/*  98 */     if (firstCharacter == '\'' || firstCharacter == '"')
/*  99 */       value = value.substring(1); 
/* 100 */     return value;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\defaultprovider\EnvironmentVariableDefaultProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */