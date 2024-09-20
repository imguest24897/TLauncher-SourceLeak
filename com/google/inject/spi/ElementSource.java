/*     */ package com.google.inject.spi;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.inject.internal.util.StackTraceElements;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
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
/*     */ public final class ElementSource
/*     */ {
/*     */   final ElementSource originalElementSource;
/*     */   final boolean trustedOriginalElementSource;
/*     */   final ModuleSource moduleSource;
/*     */   final StackTraceElements.InMemoryStackTraceElement[] partialCallStack;
/*     */   final Object declaringSource;
/*     */   final ModuleAnnotatedMethodScanner scanner;
/*     */   
/*     */   ElementSource(@Nullable ElementSource originalSource, boolean trustedOriginalSource, Object declaringSource, ModuleSource moduleSource, StackTraceElement[] partialCallStack, ModuleAnnotatedMethodScanner scanner) {
/* 108 */     Preconditions.checkNotNull(declaringSource, "declaringSource cannot be null.");
/* 109 */     Preconditions.checkNotNull(moduleSource, "moduleSource cannot be null.");
/* 110 */     Preconditions.checkNotNull(partialCallStack, "partialCallStack cannot be null.");
/* 111 */     this.originalElementSource = originalSource;
/* 112 */     this.trustedOriginalElementSource = trustedOriginalSource;
/* 113 */     this.declaringSource = declaringSource;
/* 114 */     this.moduleSource = moduleSource;
/* 115 */     this.partialCallStack = StackTraceElements.convertToInMemoryStackTraceElement(partialCallStack);
/* 116 */     this.scanner = scanner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ElementSource getOriginalElementSource() {
/* 124 */     return this.originalElementSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getDeclaringSource() {
/* 135 */     return this.declaringSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getModuleClassNames() {
/* 144 */     return this.moduleSource.getModuleClassNames();
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
/*     */   public List<Integer> getModuleConfigurePositionsInStackTrace() {
/* 166 */     int size = this.moduleSource.size();
/* 167 */     Integer[] positions = new Integer[size];
/* 168 */     int chunkSize = this.partialCallStack.length;
/* 169 */     positions[0] = Integer.valueOf(chunkSize - 1);
/* 170 */     ModuleSource current = this.moduleSource;
/* 171 */     for (int cursor = 1; cursor < size; cursor++) {
/* 172 */       chunkSize = current.getPartialCallStackSize();
/* 173 */       positions[cursor] = Integer.valueOf(positions[cursor - 1].intValue() + chunkSize);
/* 174 */       current = current.getParent();
/*     */     } 
/* 176 */     return (List<Integer>)ImmutableList.copyOf((Object[])positions);
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
/*     */   public StackTraceElement[] getStackTrace() {
/* 188 */     int modulesCallStackSize = this.moduleSource.getStackTraceSize();
/* 189 */     int chunkSize = this.partialCallStack.length;
/* 190 */     int size = this.moduleSource.getStackTraceSize() + chunkSize;
/* 191 */     StackTraceElement[] callStack = new StackTraceElement[size];
/* 192 */     System.arraycopy(
/* 193 */         StackTraceElements.convertToStackTraceElement(this.partialCallStack), 0, callStack, 0, chunkSize);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 198 */     System.arraycopy(this.moduleSource.getStackTrace(), 0, callStack, chunkSize, modulesCallStackSize);
/* 199 */     return callStack;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 205 */     return getDeclaringSource().toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\ElementSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */