/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.Locale;
/*     */ import java.util.ServiceConfigurationError;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ final class Platform
/*     */ {
/*  33 */   private static final Logger logger = Logger.getLogger(Platform.class.getName());
/*  34 */   private static final PatternCompiler patternCompiler = loadPatternCompiler();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static long systemNanoTime() {
/*  41 */     return System.nanoTime();
/*     */   }
/*     */   
/*     */   static CharMatcher precomputeCharMatcher(CharMatcher matcher) {
/*  45 */     return matcher.precomputedInternal();
/*     */   }
/*     */   
/*     */   static <T extends Enum<T>> Optional<T> getEnumIfPresent(Class<T> enumClass, String value) {
/*  49 */     WeakReference<? extends Enum<?>> ref = Enums.<T>getEnumConstants(enumClass).get(value);
/*  50 */     return (ref == null) ? Optional.<T>absent() : Optional.<T>of(enumClass.cast(ref.get()));
/*     */   }
/*     */   
/*     */   static String formatCompact4Digits(double value) {
/*  54 */     return String.format(Locale.ROOT, "%.4g", new Object[] { Double.valueOf(value) });
/*     */   }
/*     */   
/*     */   static boolean stringIsNullOrEmpty(String string) {
/*  58 */     return (string == null || string.isEmpty());
/*     */   }
/*     */   
/*     */   static String nullToEmpty(String string) {
/*  62 */     return (string == null) ? "" : string;
/*     */   }
/*     */   
/*     */   static String emptyToNull(String string) {
/*  66 */     return stringIsNullOrEmpty(string) ? null : string;
/*     */   }
/*     */   
/*     */   static CommonPattern compilePattern(String pattern) {
/*  70 */     Preconditions.checkNotNull(pattern);
/*  71 */     return patternCompiler.compile(pattern);
/*     */   }
/*     */   
/*     */   static boolean patternCompilerIsPcreLike() {
/*  75 */     return patternCompiler.isPcreLike();
/*     */   }
/*     */   
/*     */   private static PatternCompiler loadPatternCompiler() {
/*  79 */     return new JdkPatternCompiler();
/*     */   }
/*     */   
/*     */   private static void logPatternCompilerError(ServiceConfigurationError e) {
/*  83 */     logger.log(Level.WARNING, "Error loading regex compiler, falling back to next option", e);
/*     */   }
/*     */   
/*     */   private static final class JdkPatternCompiler
/*     */     implements PatternCompiler {
/*     */     public CommonPattern compile(String pattern) {
/*  89 */       return new JdkPattern(Pattern.compile(pattern));
/*     */     }
/*     */     private JdkPatternCompiler() {}
/*     */     
/*     */     public boolean isPcreLike() {
/*  94 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   static void checkGwtRpcEnabled() {
/*  99 */     String propertyName = "guava.gwt.emergency_reenable_rpc";
/*     */     
/* 101 */     if (!Boolean.parseBoolean(System.getProperty(propertyName, "false"))) {
/* 102 */       throw new UnsupportedOperationException(
/* 103 */           Strings.lenientFormat("We are removing GWT-RPC support for Guava types. You can temporarily reenable support by setting the system property %s to true. For more about system properties, see %s. For more about Guava's GWT-RPC support, see %s.", new Object[] { propertyName, "https://stackoverflow.com/q/5189914/28465", "https://groups.google.com/d/msg/guava-announce/zHZTFg7YF3o/rQNnwdHeEwAJ" }));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 111 */     logger.log(Level.WARNING, "Later in 2020, we will remove GWT-RPC support for Guava types. You are seeing this warning because you are sending a Guava type over GWT-RPC, which will break. You can identify which type by looking at the class name in the attached stack trace.", new Throwable());
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\base\Platform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */