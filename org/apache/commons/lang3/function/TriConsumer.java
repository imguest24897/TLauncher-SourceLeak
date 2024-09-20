/*    */ package org.apache.commons.lang3.function;
/*    */ 
/*    */ import java.util.Objects;
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
/*    */ @FunctionalInterface
/*    */ public interface TriConsumer<T, U, V>
/*    */ {
/*    */   void accept(T paramT, U paramU, V paramV);
/*    */   
/*    */   default TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after) {
/* 63 */     Objects.requireNonNull(after);
/*    */     
/* 65 */     return (t, u, v) -> {
/*    */         accept((T)t, (U)u, (V)v);
/*    */         after.accept(t, u, v);
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\function\TriConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */