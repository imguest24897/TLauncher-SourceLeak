/*    */ package com.google.inject.internal;
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
/*    */ final class Initializables
/*    */ {
/*    */   static <T> Initializable<T> of(final T instance) {
/* 24 */     return new Initializable<T>()
/*    */       {
/*    */         public T get() {
/* 27 */           return (T)instance;
/*    */         }
/*    */ 
/*    */         
/*    */         public String toString() {
/* 32 */           return String.valueOf(instance);
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\Initializables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */