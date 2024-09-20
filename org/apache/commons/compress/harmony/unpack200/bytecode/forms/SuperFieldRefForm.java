/*    */ package org.apache.commons.compress.harmony.unpack200.bytecode.forms;
/*    */ 
/*    */ import org.apache.commons.compress.harmony.unpack200.bytecode.OperandManager;
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
/*    */ public class SuperFieldRefForm
/*    */   extends ClassSpecificReferenceForm
/*    */ {
/*    */   public SuperFieldRefForm(int opcode, String name, int[] rewrite) {
/* 30 */     super(opcode, name, rewrite);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getOffset(OperandManager operandManager) {
/* 35 */     return operandManager.nextSuperFieldRef();
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getPoolID() {
/* 40 */     return 10;
/*    */   }
/*    */ 
/*    */   
/*    */   protected String context(OperandManager operandManager) {
/* 45 */     return operandManager.getSuperClass();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\compress\harmon\\unpack200\bytecode\forms\SuperFieldRefForm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */