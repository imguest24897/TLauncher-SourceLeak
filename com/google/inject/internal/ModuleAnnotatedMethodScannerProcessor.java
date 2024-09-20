/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.inject.spi.ModuleAnnotatedMethodScannerBinding;
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
/*    */ final class ModuleAnnotatedMethodScannerProcessor
/*    */   extends AbstractProcessor
/*    */ {
/*    */   ModuleAnnotatedMethodScannerProcessor(Errors errors) {
/* 29 */     super(errors);
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean visit(ModuleAnnotatedMethodScannerBinding command) {
/* 34 */     this.injector.getBindingData().addScanner(command);
/* 35 */     return Boolean.valueOf(true);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ModuleAnnotatedMethodScannerProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */