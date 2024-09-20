/*    */ package org.apache.commons.lang3;
/*    */ 
/*    */ import java.util.Comparator;
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
/*    */ 
/*    */ public class NumberRange<N extends Number>
/*    */   extends Range<N>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public NumberRange(N number1, N number2, Comparator<N> comp) {
/* 45 */     super(number1, number2, comp);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\NumberRange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */