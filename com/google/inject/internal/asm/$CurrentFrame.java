/*    */ package com.google.inject.internal.asm;
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
/*    */ final class $CurrentFrame
/*    */   extends $Frame
/*    */ {
/*    */   $CurrentFrame($Label owner) {
/* 40 */     super(owner);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void execute(int opcode, int arg, $Symbol symbolArg, $SymbolTable symbolTable) {
/* 51 */     super.execute(opcode, arg, symbolArg, symbolTable);
/* 52 */     $Frame successor = new $Frame(null);
/* 53 */     merge(symbolTable, successor, 0);
/* 54 */     copyFrom(successor);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\asm\$CurrentFrame.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */