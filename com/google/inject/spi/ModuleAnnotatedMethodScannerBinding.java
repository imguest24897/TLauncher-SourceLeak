/*    */ package com.google.inject.spi;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.inject.Binder;
/*    */ import com.google.inject.internal.Errors;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ModuleAnnotatedMethodScannerBinding
/*    */   implements Element
/*    */ {
/*    */   private final Object source;
/*    */   private final ModuleAnnotatedMethodScanner scanner;
/*    */   
/*    */   public ModuleAnnotatedMethodScannerBinding(Object source, ModuleAnnotatedMethodScanner scanner) {
/* 35 */     this.source = Preconditions.checkNotNull(source, "source");
/* 36 */     this.scanner = (ModuleAnnotatedMethodScanner)Preconditions.checkNotNull(scanner, "scanner");
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getSource() {
/* 41 */     return this.source;
/*    */   }
/*    */   
/*    */   public ModuleAnnotatedMethodScanner getScanner() {
/* 45 */     return this.scanner;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T acceptVisitor(ElementVisitor<T> visitor) {
/* 50 */     return visitor.visit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void applyTo(Binder binder) {
/* 55 */     binder.withSource(getSource()).scanModulesForAnnotatedMethods(this.scanner);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 60 */     String str1 = String.valueOf(this.scanner);
/*    */     
/* 62 */     String str2 = String.valueOf(this.scanner.annotationClasses());
/*    */     
/* 64 */     String str3 = String.valueOf(Errors.convert(this.source)); return (new StringBuilder(29 + String.valueOf(str1).length() + String.valueOf(str2).length() + String.valueOf(str3).length())).append(str1).append(" which scans for ").append(str2).append(" (bound at ").append(str3).append(")").toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\ModuleAnnotatedMethodScannerBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */