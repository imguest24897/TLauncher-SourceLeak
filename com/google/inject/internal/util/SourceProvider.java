/*     */ package com.google.inject.internal.util;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
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
/*     */ public final class SourceProvider
/*     */ {
/*  32 */   public static final Object UNKNOWN_SOURCE = "[unknown source]";
/*     */   
/*     */   private final SourceProvider parent;
/*     */   
/*     */   private final ImmutableSet<String> classNamesToSkip;
/*  37 */   public static final SourceProvider DEFAULT_INSTANCE = new SourceProvider(
/*  38 */       (Iterable<String>)ImmutableSet.of(SourceProvider.class.getName()));
/*     */   
/*     */   private SourceProvider(Iterable<String> classesToSkip) {
/*  41 */     this(null, classesToSkip);
/*     */   }
/*     */   
/*     */   private SourceProvider(SourceProvider parent, Iterable<String> classesToSkip) {
/*  45 */     this.parent = parent;
/*     */     
/*  47 */     ImmutableSet.Builder<String> classNamesToSkipBuilder = ImmutableSet.builder();
/*  48 */     for (String classToSkip : classesToSkip) {
/*  49 */       if (parent == null || !parent.shouldBeSkipped(classToSkip)) {
/*  50 */         classNamesToSkipBuilder.add(classToSkip);
/*     */       }
/*     */     } 
/*  53 */     this.classNamesToSkip = classNamesToSkipBuilder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public SourceProvider plusSkippedClasses(Class<?>... moreClassesToSkip) {
/*  58 */     return new SourceProvider(this, asStrings(moreClassesToSkip));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean shouldBeSkipped(String className) {
/*  63 */     return ((this.parent != null && this.parent.shouldBeSkipped(className)) || this.classNamesToSkip
/*  64 */       .contains(className));
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<String> asStrings(Class<?>... classes) {
/*  69 */     List<String> strings = Lists.newArrayList();
/*  70 */     for (Class<?> c : classes) {
/*  71 */       strings.add(c.getName());
/*     */     }
/*  73 */     return strings;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StackTraceElement get(StackTraceElement[] stackTraceElements) {
/*  81 */     Preconditions.checkNotNull(stackTraceElements, "The stack trace elements cannot be null.");
/*  82 */     for (StackTraceElement element : stackTraceElements) {
/*  83 */       String className = element.getClassName();
/*     */       
/*  85 */       if (!shouldBeSkipped(className)) {
/*  86 */         return element;
/*     */       }
/*     */     } 
/*  89 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getFromClassNames(List<String> moduleClassNames) {
/*  94 */     Preconditions.checkNotNull(moduleClassNames, "The list of module class names cannot be null.");
/*  95 */     for (String moduleClassName : moduleClassNames) {
/*  96 */       if (!shouldBeSkipped(moduleClassName)) {
/*  97 */         return new StackTraceElement(moduleClassName, "configure", null, -1);
/*     */       }
/*     */     } 
/* 100 */     return UNKNOWN_SOURCE;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\interna\\util\SourceProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */