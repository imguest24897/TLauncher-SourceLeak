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
/*     */ final class ModuleSource
/*     */ {
/*     */   private final String moduleClassName;
/*     */   private final ModuleSource parent;
/*     */   private final BindingSourceRestriction.PermitMap permitMap;
/*     */   private final StackTraceElements.InMemoryStackTraceElement[] partialCallStack;
/*     */   
/*     */   ModuleSource(Class<?> moduleClass, StackTraceElement[] partialCallStack, BindingSourceRestriction.PermitMap permitMap) {
/*  69 */     this(null, moduleClass, partialCallStack, permitMap);
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
/*     */   private ModuleSource(@Nullable ModuleSource parent, Class<?> moduleClass, StackTraceElement[] partialCallStack, BindingSourceRestriction.PermitMap permitMap) {
/*  86 */     Preconditions.checkNotNull(moduleClass, "module cannot be null.");
/*  87 */     Preconditions.checkNotNull(partialCallStack, "partialCallStack cannot be null.");
/*  88 */     this.parent = parent;
/*  89 */     this.moduleClassName = moduleClass.getName();
/*  90 */     this.partialCallStack = StackTraceElements.convertToInMemoryStackTraceElement(partialCallStack);
/*  91 */     this.permitMap = permitMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getModuleClassName() {
/* 100 */     return this.moduleClassName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   StackTraceElement[] getPartialCallStack() {
/* 110 */     return StackTraceElements.convertToStackTraceElement(this.partialCallStack);
/*     */   }
/*     */ 
/*     */   
/*     */   int getPartialCallStackSize() {
/* 115 */     return this.partialCallStack.length;
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
/*     */   ModuleSource createChild(Class<?> moduleClass, StackTraceElement[] partialCallStack) {
/* 127 */     return new ModuleSource(this, moduleClass, partialCallStack, this.permitMap);
/*     */   }
/*     */ 
/*     */   
/*     */   ModuleSource getParent() {
/* 132 */     return this.parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<String> getModuleClassNames() {
/* 141 */     ImmutableList.Builder<String> classNames = ImmutableList.builder();
/* 142 */     ModuleSource current = this;
/* 143 */     while (current != null) {
/* 144 */       String className = current.moduleClassName;
/* 145 */       classNames.add(className);
/* 146 */       current = current.parent;
/*     */     } 
/* 148 */     return (List<String>)classNames.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int size() {
/* 156 */     if (this.parent == null) {
/* 157 */       return 1;
/*     */     }
/* 159 */     return this.parent.size() + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getStackTraceSize() {
/* 167 */     if (this.parent == null) {
/* 168 */       return this.partialCallStack.length;
/*     */     }
/* 170 */     return this.parent.getStackTraceSize() + this.partialCallStack.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   StackTraceElement[] getStackTrace() {
/* 179 */     int stackTraceSize = getStackTraceSize();
/* 180 */     StackTraceElement[] callStack = new StackTraceElement[stackTraceSize];
/* 181 */     int cursor = 0;
/* 182 */     ModuleSource current = this;
/* 183 */     while (current != null) {
/*     */       
/* 185 */       StackTraceElement[] chunk = StackTraceElements.convertToStackTraceElement(current.partialCallStack);
/* 186 */       int chunkSize = chunk.length;
/* 187 */       System.arraycopy(chunk, 0, callStack, cursor, chunkSize);
/* 188 */       current = current.parent;
/* 189 */       cursor += chunkSize;
/*     */     } 
/* 191 */     return callStack;
/*     */   }
/*     */ 
/*     */   
/*     */   BindingSourceRestriction.PermitMap getPermitMap() {
/* 196 */     return this.permitMap;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\ModuleSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */