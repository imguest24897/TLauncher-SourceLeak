/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Arrays;
/*     */ import java.util.logging.Logger;
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
/*     */ public final class InternalFlags
/*     */ {
/*  25 */   private static final Logger logger = Logger.getLogger(InternalFlags.class.getName());
/*     */ 
/*     */   
/*  28 */   private static final IncludeStackTraceOption INCLUDE_STACK_TRACES = getSystemOption("guice_include_stack_traces", IncludeStackTraceOption.ONLY_FOR_DECLARING_SOURCE);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  33 */   private static final CustomClassLoadingOption CUSTOM_CLASS_LOADING = getSystemOption("guice_custom_class_loading", CustomClassLoadingOption.BRIDGE, CustomClassLoadingOption.OFF);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  39 */   private static final NullableProvidesOption NULLABLE_PROVIDES = getSystemOption("guice_check_nullable_provides_params", NullableProvidesOption.ERROR);
/*     */ 
/*     */   
/*  42 */   private static final BytecodeGenOption BYTECODE_GEN_OPTION = getSystemOption("guice_bytecode_gen_option", BytecodeGenOption.ENABLED);
/*     */ 
/*     */   
/*  45 */   private static final ColorizeOption COLORIZE_OPTION = getSystemOption("guice_colorize_error_messages", ColorizeOption.OFF);
/*     */ 
/*     */   
/*     */   public enum IncludeStackTraceOption
/*     */   {
/*  50 */     OFF,
/*     */     
/*  52 */     ONLY_FOR_DECLARING_SOURCE,
/*     */     
/*  54 */     COMPLETE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum CustomClassLoadingOption
/*     */   {
/*  63 */     OFF,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  71 */     ANONYMOUS,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     BRIDGE,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     CHILD;
/*     */   }
/*     */ 
/*     */   
/*     */   public enum NullableProvidesOption
/*     */   {
/*  90 */     IGNORE,
/*     */     
/*  92 */     WARN,
/*     */     
/*  94 */     ERROR;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum BytecodeGenOption
/*     */   {
/* 117 */     DISABLED,
/*     */     
/* 119 */     ENABLED;
/*     */   }
/*     */   
/*     */   public enum ColorizeOption
/*     */   {
/* 124 */     AUTO
/*     */     {
/*     */       boolean enabled() {
/* 127 */         return (System.console() != null && System.getenv("TERM") != null);
/*     */       }
/*     */     },
/* 130 */     ON
/*     */     {
/*     */       boolean enabled() {
/* 133 */         return true;
/*     */       }
/*     */     },
/* 136 */     OFF
/*     */     {
/*     */       boolean enabled() {
/* 139 */         return false;
/*     */       }
/*     */     };
/*     */     
/*     */     abstract boolean enabled();
/*     */   }
/*     */   
/*     */   public static IncludeStackTraceOption getIncludeStackTraceOption() {
/* 147 */     return INCLUDE_STACK_TRACES;
/*     */   }
/*     */   
/*     */   public static CustomClassLoadingOption getCustomClassLoadingOption() {
/* 151 */     return CUSTOM_CLASS_LOADING;
/*     */   }
/*     */   
/*     */   public static NullableProvidesOption getNullableProvidesOption() {
/* 155 */     return NULLABLE_PROVIDES;
/*     */   }
/*     */   
/*     */   public static boolean isBytecodeGenEnabled() {
/* 159 */     return (BYTECODE_GEN_OPTION == BytecodeGenOption.ENABLED);
/*     */   }
/*     */   
/*     */   public static boolean enableColorizeErrorMessages() {
/* 163 */     return COLORIZE_OPTION.enabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T extends Enum<T>> T getSystemOption(String name, T defaultValue) {
/* 174 */     return getSystemOption(name, defaultValue, defaultValue);
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
/*     */   private static <T extends Enum<T>> T getSystemOption(final String name, T defaultValue, T secureValue) {
/* 188 */     Class<T> enumType = defaultValue.getDeclaringClass();
/* 189 */     String value = null;
/*     */     
/*     */     try {
/* 192 */       value = AccessController.<String>doPrivileged(new PrivilegedAction<String>()
/*     */           {
/*     */             public String run()
/*     */             {
/* 196 */               return System.getProperty(name);
/*     */             }
/*     */           });
/* 199 */       return (value != null && value.length() > 0) ? Enum.<T>valueOf(enumType, value) : defaultValue;
/* 200 */     } catch (SecurityException e) {
/* 201 */       return secureValue;
/* 202 */     } catch (IllegalArgumentException e) {
/* 203 */       String str1 = value;
/* 204 */       String str2 = String.valueOf(Arrays.asList((Enum[])enumType.getEnumConstants())); logger.warning((new StringBuilder(56 + String.valueOf(str1).length() + String.valueOf(name).length() + String.valueOf(str2).length())).append(str1).append(" is not a valid flag value for ").append(name).append(".  Values must be one of ").append(str2).toString());
/* 205 */       return defaultValue;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\InternalFlags.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */